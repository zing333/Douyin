package cn.nineton.onetake.media.videotool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.audiotool.AudioDecoder;

public class VideoTranscoder {
    static final String STATUS_ACTION = "com.blink.academy.onetake.VideoTranscoder.Status";
    static final String TAG = "VideoTranscoder.test";
    static WorkQueue mWorkQueue = new WorkQueue();

    public interface Progress {
        void onProgress(float f);
    }

    public static final class Client {
        static Handler mHandler;
        boolean aborted;
        boolean finished;
        ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(VideoTranscoder.TAG, "onServiceConnected");
                Client.this.mService = new Messenger(service);
                if (Client.this.paused) {
                    Client.this.sendMessage(1);
                }
                if (Client.this.aborted) {
                    Client.this.sendMessage(3);
                }
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(VideoTranscoder.TAG, "onServiceDisconnected");
            }
        };
        Intent mIntent;
        Progress mProgress;
        ResultReceiver mReceiver;
        Request mRequest;
        String mResultStatus;
        Messenger mService;
        boolean paused;
        boolean running;

        final class ClientResultReceiver extends ResultReceiver {
            ClientResultReceiver(Handler handler) {
                super(handler);
            }

            public void onReceiveResult(int code, Bundle data) {
                String status = data.getString("status");
                if (status != null) {
                    Log.i(VideoTranscoder.TAG, String.format("status:%s", new Object[]{status}));
                    synchronized (Client.this) {
                        Client.this.mResultStatus = status;
                        Client.this.finished = true;
                        Client.this.notifyAll();
                    }
                }
                if (data.containsKey("progress")) {
                    Client.this.mProgress.onProgress(data.getFloat("progress", 0.0f));
                }
            }
        }

        static {
            HandlerThread handlerThread = new HandlerThread("VideoTranscoder.ResultReceiver");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }

        Client(Request request, Progress progress) {
            this.mRequest = request;
            this.mProgress = progress;
        }

        public synchronized void run() {
            if (!this.aborted) {
                this.running = true;
                Context context = App.getContext();
                this.mIntent = this.mRequest.getIntent(context);
                this.mReceiver = new ClientResultReceiver(mHandler);
                this.mIntent.putExtra("receiver", this.mReceiver);
                Log.d(VideoTranscoder.TAG, "starting transcode service");
                context.bindService(this.mIntent, this.mConnection, Context.BIND_AUTO_CREATE);
                context.startService(this.mIntent);
            }
        }

        public synchronized String waitUntilFinished() {
            String str = null;
            synchronized (this) {
                Context context = App.getContext();
                while (!this.finished) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        if (this.mIntent != null) {
                            context.unbindService(this.mConnection);
                            context.stopService(this.mIntent);
                            this.mIntent = null;
                        }
                        this.running = false;
                    } catch (Throwable th) {
                        if (this.mIntent != null) {
                            context.unbindService(this.mConnection);
                            context.stopService(this.mIntent);
                            this.mIntent = null;
                        }
                        this.running = false;
                    }
                }
                if (this.mIntent != null) {
                    context.unbindService(this.mConnection);
                    context.stopService(this.mIntent);
                    this.mIntent = null;
                }
                this.running = false;
                if (this.mResultStatus == null) {
                    str = "unknown error";
                } else if (!this.mResultStatus.equals("success")) {
                    str = this.mResultStatus;
                }
            }
            return str;
        }

        private void sendMessage(int what) {
            Message msg = Message.obtain(null, what);
            try {
                if (this.mService != null) {
                    this.mService.send(msg);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public synchronized void pause() {
            this.paused = true;
            if (this.running) {
                sendMessage(1);
            }
        }

        public synchronized void resume() {
            this.paused = false;
            if (this.running) {
                sendMessage(2);
            }
        }

        public synchronized void abort() {
            this.aborted = true;
            if (this.running) {
                sendMessage(3);
            }
            notifyAll();
        }
    }

    static final class Executor {
        boolean aborted;
        AudioDecoder mAudioDecoder = null;
        private AudioDecoder.Output mAudioDecoderOutput = new AudioDecoder.Output() {
            public void writeAudio(long presentationSample, short[] data, int numSamples) {
                Executor.this.mOutput.writeAudio(presentationSample, data, 0, numSamples * 2);
            }

            public void finish() {
                Executor.this.mAudioFinished = true;
            }

            public void disable() {
            }
        };
        boolean mAudioFinished;
        long mInputDurationUs;
        MP4Output mOutput;
        Progress mProgress;
        VideoDecoder18 mVideoDecoder = null;
        private DecoderCallbacks mVideoDecoderCallbacks = new DecoderCallbacks() {
            public void onFrameDecoded(long pts) {
                if (Executor.this.mInputDurationUs > 0) {
                    Executor.this.mProgress.onProgress((float) (((double) pts) / ((double) Executor.this.mInputDurationUs)));
                }
            }

            public void onFinish() {
                Executor.this.mVideoFinished = true;
            }
        };
        boolean mVideoFinished;
        BufferPool mVideoPool = null;
        boolean paused;

        Executor() {
        }

        public synchronized void pause() {
            this.paused = true;
            notifyAll();
        }

        public synchronized void resume() {
            this.paused = false;
            notifyAll();
        }

        public synchronized void abort() {
            this.aborted = true;
            notifyAll();
        }

        public void run(Request req, Progress progress) throws Exception {
            this.mProgress = progress;
            this.mInputDurationUs = req.mInputDurationUs;
            this.mOutput = new MP4Output(req.mOutputPath);
            if (req.mEnableVideo) {
                try {
                    this.mVideoDecoder = new VideoDecoder18();
                    this.mVideoDecoder.open(req.mInputPath);
                    this.mVideoDecoder.setDecoderCallbacks(this.mVideoDecoderCallbacks);
                    int inputWidth = this.mVideoDecoder.getInputWidth();
                    int inputHeight = this.mVideoDecoder.getInputHeight();
                    if (req.mOutputWidth == 0) {
                        req.mOutputWidth = inputWidth;
                    }
                    if (req.mOutputHeight == 0) {
                        req.mOutputHeight = inputHeight;
                    }
                    int decoderOutputFrameDurationUs = (int) req.mInputRatio.multiplyInverse((long) this.mVideoDecoder.getFrameDuration());
                    int encoderOutputFrameDurationUs = (int) req.mOutputRatio.multiply((long) decoderOutputFrameDurationUs);
                    HashMap<String, Object> formatProps = new HashMap();
                    if (req.mOutputKeyframeInterval != 0) {
                        formatProps.put("i-frame-interval", Integer.valueOf(req.mOutputKeyframeInterval));
                    }
                    if (req.mOutputRotation != 0) {
                        formatProps.put("rotation-degrees", Integer.valueOf(req.mOutputRotation));
                    }
                    this.mOutput.enableVideo(req.mOutputBitrate, req.mOutputWidth, req.mOutputHeight, encoderOutputFrameDurationUs, formatProps, true);
                    this.mOutput.mVideoTimestampMultiplier = req.mOutputRatio.clone();
                    this.mVideoDecoder.start(this.mOutput.mVideoEncoder.mEncoderSurface, req.mOutputWidth, req.mOutputHeight, req.mOutputRotation, (long) decoderOutputFrameDurationUs, req.mInputStartTimeUs, req.mInputDurationUs);
                } catch (IOException e) {
                    this.mVideoDecoder = null;
                } catch (Throwable th) {
                    if (this.mAudioDecoder != null) {
                        this.mAudioDecoder.close(true);
                        this.mAudioDecoder = null;
                    }
                    if (this.mVideoPool != null) {
                        this.mVideoPool.releaseAll();
                        this.mVideoPool = null;
                    }
                    if (this.mVideoDecoder != null) {
                        this.mVideoDecoder.close(true);
                        this.mVideoDecoder = null;
                    }
                    if (this.mOutput != null) {
                        this.mOutput.close();
                        this.mOutput = null;
                    }
                }
            }
            if (req.mEnableAudio) {
                try {
                    this.mAudioDecoder = new AudioDecoder();
                    this.mAudioDecoder.open(req.mInputPath);
                    this.mOutput.enableAudio(44100, 128000);
                    int outputSampleRate = (int) req.mOutputRatio.multiply(44100);
                    this.mAudioDecoder.start(this.mAudioDecoderOutput, outputSampleRate, req.mInputStartTimeUs, (((long) outputSampleRate) * this.mInputDurationUs) / 1000000);
                } catch (IOException e2) {
                    this.mAudioDecoder = null;
                } catch (RuntimeException e3) {
                    this.mAudioDecoder = null;
                }
            }
            if (this.mAudioDecoder == null) {
                this.mAudioFinished = true;
            }
            if (this.mVideoDecoder == null) {
                this.mVideoFinished = true;
            }
            while (true) {
                if (this.mAudioFinished && this.mVideoFinished) {
                    break;
                }
                if (this.paused) {
                    synchronized (this) {
                        try {
                            Log.d(VideoTranscoder.TAG, "entering pause state");
                            while (this.paused && !this.aborted) {
                                wait();
                            }
                            Log.d(VideoTranscoder.TAG, "leaving pause state");
                        } catch (InterruptedException e4) {
                        }
                    }
                }
                if (this.aborted) {
                    Log.d(VideoTranscoder.TAG, "aborted");
                    break;
                }
                this.mOutput.mVideoEncoder.drainEncoder(false, 50000);
            }
            if (this.mAudioDecoder != null) {
                this.mAudioDecoder.close(true);
                this.mAudioDecoder = null;
            }
            if (this.mVideoPool != null) {
                this.mVideoPool.releaseAll();
                this.mVideoPool = null;
            }
            if (this.mVideoDecoder != null) {
                this.mVideoDecoder.close(true);
                this.mVideoDecoder = null;
            }
            if (this.mOutput != null) {
                this.mOutput.close();
                this.mOutput = null;
            }
        }
    }

    public static final class Request {
        static final String ENABLE_AUDIO = "enableAudio";
        static final String ENABLE_VIDEO = "enableVideo";
        static final String INPUT_DURATION_US = "inputDurationUs";
        static final String INPUT_PATH = "inputPath";
        static final String INPUT_RATIO_DEN = "inputRatioDen";
        static final String INPUT_RATIO_NUM = "inputRatioNum";
        static final String INPUT_STARTTIME_US = "inputStartTimeUs";
        static final String OUTPUT_HEIGHT = "outputHeight";
        static final String OUTPUT_KEYFRAME_INTERVAL = "outputKeyframeInterval";
        static final String OUTPUT_PATH = "outputPath";
        static final String OUTPUT_RATIO_DEN = "outputRatioDen";
        static final String OUTPUT_RATIO_NUM = "outputRatioNum";
        static final String OUTPUT_WIDTH = "outputWidth";
        boolean mEnableAudio = true;
        boolean mEnableVideo = true;
        long mInputDurationUs;
        String mInputPath;
        Ratio mInputRatio = new Ratio(1, 1);
        long mInputStartTimeUs;
        int mOutputBitrate = 10000000;
        int mOutputHeight;
        int mOutputKeyframeInterval = 0;
        String mOutputPath;
        Ratio mOutputRatio = new Ratio(1, 1);
        int mOutputRotation = 0;
        int mOutputWidth;

        public void setInputPath(String path) {
            this.mInputPath = path;
        }

        public void setInputTime(long timeUs) {
            this.mInputStartTimeUs = timeUs;
        }

        public void setInputDuration(long durationUs) {
            this.mInputDurationUs = durationUs;
        }

        public void setInputRatio(Ratio speed) {
            this.mInputRatio = speed.clone();
        }

        public void setOutputPath(String path) {
            this.mOutputPath = path;
        }

        public void setOutputSize(int width, int height) {
            this.mOutputWidth = width;
            this.mOutputHeight = height;
        }

        public void setOutputRotation(int degrees) {
            this.mOutputRotation = degrees;
        }

        public void setOutputRatio(Ratio speed) {
            this.mOutputRatio = speed.clone();
        }

        public void setOutputKeyframeInterval(int interval) {
            this.mOutputKeyframeInterval = interval;
        }

        public void disableVideo() {
            this.mEnableVideo = false;
        }

        public void disableAudio() {
            this.mEnableAudio = false;
        }

        protected Request(Intent intent) {
            setInputPath(intent.getStringExtra(INPUT_PATH));
            setInputTime(intent.getLongExtra(INPUT_STARTTIME_US, 0));
            setInputDuration(intent.getLongExtra(INPUT_DURATION_US, 0));
            setInputRatio(new Ratio(intent.getIntExtra(INPUT_RATIO_NUM, 1), intent.getIntExtra(INPUT_RATIO_DEN, 1)));
            setOutputPath(intent.getStringExtra(OUTPUT_PATH));
            setOutputSize(intent.getIntExtra(OUTPUT_WIDTH, 0), intent.getIntExtra(OUTPUT_HEIGHT, 0));
            setOutputRatio(new Ratio(intent.getIntExtra(OUTPUT_RATIO_NUM, 1), intent.getIntExtra(OUTPUT_RATIO_DEN, 1)));
            setOutputKeyframeInterval(intent.getIntExtra(OUTPUT_KEYFRAME_INTERVAL, 0));
            this.mEnableVideo = intent.getBooleanExtra(ENABLE_VIDEO, true);
            this.mEnableAudio = intent.getBooleanExtra(ENABLE_AUDIO, true);
        }

        public Intent getIntent(Context context) {
            Intent intent = new Intent(context, VideoService.class);
            intent.putExtra("type", "transcode");
            intent.putExtra(INPUT_PATH, this.mInputPath);
            intent.putExtra(INPUT_STARTTIME_US, this.mInputStartTimeUs);
            intent.putExtra(INPUT_DURATION_US, this.mInputDurationUs);
            intent.putExtra(INPUT_RATIO_NUM, this.mInputRatio.num);
            intent.putExtra(INPUT_RATIO_DEN, this.mInputRatio.den);
            intent.putExtra(OUTPUT_PATH, this.mOutputPath);
            intent.putExtra(OUTPUT_WIDTH, this.mOutputWidth);
            intent.putExtra(OUTPUT_HEIGHT, this.mOutputHeight);
            intent.putExtra(OUTPUT_RATIO_NUM, this.mOutputRatio.num);
            intent.putExtra(OUTPUT_RATIO_DEN, this.mOutputRatio.den);
            intent.putExtra(OUTPUT_KEYFRAME_INTERVAL, this.mOutputKeyframeInterval);
            intent.putExtra(ENABLE_VIDEO, this.mEnableVideo);
            intent.putExtra(ENABLE_AUDIO, this.mEnableAudio);
            return intent;
        }
    }

    static final class Server {
        Executor mExecutor = new Executor();

        Server() {
        }

        public void handleRequest(Intent intent, Context context) {
            Request req = new Request(intent);
            final ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
            Log.d(VideoTranscoder.TAG, "starting executor in service");
            Bundle bundle;
            try {
                this.mExecutor.run(req, new Progress() {
                    int lastProgress = -1;

                    public void onProgress(float progress) {
                        int iprogress = (int) Math.round(((double) progress) * 100.0d);
                        if (iprogress != this.lastProgress) {
                            Bundle bundle = new Bundle();
                            bundle.putFloat("progress", progress);
                            receiver.send(0, bundle);
                            Log.d(VideoTranscoder.TAG, String.format("sent progress:%.2f", new Object[]{Float.valueOf(progress)}));
                            this.lastProgress = iprogress;
                        }
                    }
                });
                bundle = new Bundle();
                bundle.putString("status", "success");
                receiver.send(0, bundle);
                Log.d(VideoTranscoder.TAG, "sent status: success");
            } catch (Exception e) {
                bundle = new Bundle();
                bundle.putString("status", e.getMessage());
                receiver.send(0, bundle);
                Log.d(VideoTranscoder.TAG, "sent status: " + e.getMessage());
            } finally {
                Log.d(VideoTranscoder.TAG, "finished executor in service");
            }
        }

        public void pause() {
            this.mExecutor.pause();
        }

        public void resume() {
            this.mExecutor.resume();
        }

        public void abort() {
            this.mExecutor.abort();
        }
    }

    static final class WorkQueue {
        BlockingQueue<Client> mJobs = new LinkedBlockingQueue();
        Thread mThread = new Thread(VideoTranscoder$WorkQueue$$Lambda$1.lambdaFactory$(this));

        WorkQueue() {
            this.mThread.setName("VideoTranscoder.WorkQueue");
            this.mThread.start();
        }

        /* renamed from: threadEntry */
        void access$lambda$0() {
            while (true) {
                try {
                    ((Client) this.mJobs.take()).run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        void add(Client client) {
            this.mJobs.add(client);
        }
    }

    public static void test() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/tmp");
            String inputFile = dir + "/BattleBlock Theater.mp4";
            String outputFile = dir + "/transcoded.mp4";
            MediaFormat inputFormat = MediaUtils.getFormat(inputFile);
            int outWidth = MediaUtils.getRotatedWidth(inputFormat);
            int outHeight = MediaUtils.getRotatedHeight(inputFormat);
            long duration = Math.min(20000000, inputFormat.getLong("durationUs"));
            Request req = new Request();
            req.setInputPath(inputFile);
            req.setInputDuration(duration);
            req.setInputRatio(new Ratio(1, 4));
            req.setOutputPath(outputFile);
            req.setOutputSize(outWidth, outHeight);
            req.setOutputRotation(0);
            req.setOutputKeyframeInterval(1);
            req.setOutputRatio(new Ratio(1, 4));
            long t1 = System.nanoTime();
            try {
                run(req, new Progress() {
                    public void onProgress(float progress) {
                        Log.i(VideoTranscoder.TAG, String.format("progress:%.2f", new Object[]{Float.valueOf(progress)}));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            long t2 = System.nanoTime();
            Log.d(TAG, String.format("done transcode test, took %d ms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
        } catch (Exception e2) {
            Log.e(TAG, "Exception in test", e2);
        }
    }

    public static void run(Request req, Progress progress) throws Exception {
        new Executor().run(req, progress);
    }

    public static Client runInService(Request req, Progress progress) throws Exception {
        Client client = new Client(req, progress);
        mWorkQueue.add(client);
        return client;
    }
}