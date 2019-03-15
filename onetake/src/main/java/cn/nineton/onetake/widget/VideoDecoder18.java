package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.Callback;
import android.media.MediaCodec.CodecException;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Looper;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnable;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.ResourceTracker;
import cn.nineton.onetake.media.videotool.CodecOutputSurface;
import cn.nineton.onetake.media.videotool.VideoDecoder;
@TargetApi(21)
public class VideoDecoder18 extends VideoDecoder implements CodecOutputSurface.OnFrameAvailableListener {
    static int mNextDecoderSerial = 0;
    static int mNumDecoders = 0;
    private String TAG = "VideoDecoder18";
    private boolean VERBOSE = false;
    boolean mAborted = false;
    OutputSurfaceArray.BufferPool mBufferPool;
    Object mBufferPoolLock = new Object();
    MediaCodec mCodec;
    Callback mCodecCallbacks = new Callback() {
        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
            Trace.beginSection("onInputBufferAvailable");
            InputMessage m = new InputMessage();
            m.index = index;
            VideoDecoder18.this.mInputMessages.offer(m);
            Trace.endSection();
        }

        public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull BufferInfo info) {
            Trace.beginSection("onOutputBufferAvailable");
            OutputMessage m = new OutputMessage();
            m.index = index;
            m.info = info;
            VideoDecoder18.this.mOutputMessages.offer(m);
            Trace.endSection();
        }

        @RequiresApi(api = 21)
        public void onError(@NonNull MediaCodec codec, @NonNull CodecException e) {
            Log.e(VideoDecoder18.this.TAG, "Exception", e);
            synchronized (this) {
                VideoDecoder18.this.mSawOutputEOS = true;
                VideoDecoder18.this.mLooper.quit();
            }
        }

        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
            OutputMessage m = new OutputMessage();
            m.index = -1;
            m.info = null;
            m.format = format;
            VideoDecoder18.this.mOutputMessages.offer(m);
        }
    };
    String mCodecName;
    CodecOutputSurface mCodecSurface = null;
    private DecoderCallbacks mDecoderCallbacks;
    int mEncoderHeight;
    int mEncoderRotation;
    Surface mEncoderSurface = null;
    int mEncoderWidth;
    MediaExtractor mExtractor;
    LinkedBlockingQueue<ByteBuffer> mExtractorBuffers = new LinkedBlockingQueue();
    LinkedBlockingQueue<ExtractorPacket> mExtractorPackets = new LinkedBlockingQueue();
    Thread mExtractorThread;
    long mFirstPresentationTimeUs = -1;
    int mFrameDuration = 0;
    boolean mHasFinishedBuffering = false;
    private boolean mHaveReadSentinel = false;
    private boolean mHaveWrittenSentinel = false;
    MediaFormat mInputFormat;
    LinkedBlockingQueue<InputMessage> mInputMessages = new LinkedBlockingQueue();
    Thread mInputThread;
    boolean mIsAsync = false;
    private boolean mIsGIFMode = false;
    boolean mIsStopping;
    long mLastFrameUs;
    Looper mLooper = null;
    long mMediaTimeDurationUs = 0;
    long mMediaTimeStartUs = 0;
    long mNextPresentationTimeUs = -1;
    private int mNumInputBuffers = 0;
    private int mNumOutputBuffers = 0;
    MediaFormat mOutputFormat;
    long mOutputFrameDurationUs;
    LinkedBlockingQueue<OutputMessage> mOutputMessages = new LinkedBlockingQueue();
    Thread mOutputThread;
    BlockingQueue<OutputSurfaceArray.Buffer> mReadableFrames = new LinkedBlockingQueue();
    boolean mSawInputEOS = false;
    boolean mSawOutputEOS = false;
    Thread mWorkerThread;
    private int serial = 0;

    public interface DecoderCallbacks {
        void onFinish();

        void onFrameDecoded(long j);
    }

    static final class ExtractorPacket {
        ByteBuffer buffer;
        boolean eos;
        long pts;
        int size;

        ExtractorPacket() {
        }
    }

    static final class InputMessage {
        int index;
        boolean quit;

        InputMessage() {
        }

        static InputMessage createQuit() {
            InputMessage m = new InputMessage();
            m.quit = true;
            return m;
        }
    }

    static final class OutputMessage {
        MediaFormat format;
        int index;
        BufferInfo info;
        boolean quit;

        OutputMessage() {
        }

        static OutputMessage createQuit() {
            OutputMessage m = new OutputMessage();
            m.quit = true;
            return m;
        }
    }

    static final class Size {
        final int height;
        final int width;

        Size(int w, int h) {
            this.width = w;
            this.height = h;
        }
    }

    public static void test() {
//        EGL10Helper.withContext("VideoDecoder18.test", VideoDecoder18$$Lambda$1.lambdaFactory$(new File(new File(Environment.getExternalStorageDirectory() + "/tmp"), "jellyfish.mp4").getAbsolutePath()));
        EGL10Helper.withContext("VideoDecoder18.test", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                String name = new File(new File(Environment.getExternalStorageDirectory() + "/tmp"), "jellyfish.mp4").getAbsolutePath();
                lambda$test$0(name, eGL10Helper);
            }
        });
    }

    private static /* synthetic */ void lambda$test$0(String filename, EGL10Helper egl) {
        VideoDecoder18 decoder = new VideoDecoder18();
        try {
            decoder.open(filename);
            Size size = decoder.getOutputSize();
            decoder.start(new OutputSurfaceArray.BufferPool(3, size.width, size.height), 0, 0, 20000000, false);
            long t1 = System.nanoTime();
            int count = 0;
            while (true) {
                OutputSurfaceArray.Frame frame = decoder.getFrame();
                if (frame == null) {
                    Log.d("VideoDecoder18", String.format("test decode took:%dus size:%dx%d nframes:%d", new Object[]{Long.valueOf((System.nanoTime() - t1) / 1000), Integer.valueOf(size.width), Integer.valueOf(size.height), Integer.valueOf(count)}));
                    decoder.stop();
                    decoder.close(true);
                    return;
                }
                decoder.freeFrame(frame);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getProxyTestFileBaseLineSpeed(final String filePath) {
//        return ((Double) EGL10Helper.withContext("proxyBaseLine", VideoDecoder18$$Lambda$2.lambdaFactory$(filePath))).doubleValue();
        return EGL10Helper.withContext("proxyBaseLine", new EGLRunnable<Double>() {
            @Override
            public Double run(EGL10Helper eGL10Helper) {
                return lambda$getProxyTestFileBaseLineSpeed$1(filePath, eGL10Helper);
            }
        }).doubleValue();
    }

    private static /* synthetic */ Double lambda$getProxyTestFileBaseLineSpeed$1(String filePath, EGL10Helper egl) {
        VideoDecoder18 decoder18 = new VideoDecoder18();
        try {
            int count;
            decoder18.open(filePath);
            Size size = decoder18.getOutputSize();
            decoder18.start(new OutputSurfaceArray.BufferPool(6, size.width, size.height), 0, 0, 0, false);
            long t1 = 0;
            for (count = 0; count <= 310; count++) {
                if (count == 10) {
                    t1 = System.nanoTime();
                }
                OutputSurfaceArray.Frame frame = decoder18.getFrame();
                if (frame == null) {
                    break;
                }
                decoder18.freeFrame(frame);
            }
            long t2 = System.nanoTime();
            decoder18.stop();
            decoder18.close(true);
            Log.d("VideoDecoder18", String.format("benchmark decode took:%dus size:%dx%d nframes:%d", new Object[]{Long.valueOf((t2 - t1) / 1000), Integer.valueOf(size.width), Integer.valueOf(size.height), Integer.valueOf(count)}));
            return Double.valueOf(300.0d / (((double) (t2 - t1)) / 1.0E9d));
        } catch (IOException e) {
            e.printStackTrace();
            return Double.valueOf(0.0d);
        }
    }

    public static double benchmark(final String filename) {
//        return ((Double) EGL10Helper.withContext("benchmark", VideoDecoder18$$Lambda$3.lambdaFactory$(filename))).doubleValue();
        return EGL10Helper.withContext("benchmark", new EGLRunnable<Double>() {
            @Override
            public Double run(EGL10Helper eGL10Helper) {
                return lambda$benchmark$2(filename,eGL10Helper);
            }
        }).doubleValue();
    }

    private static /* synthetic */ Double lambda$benchmark$2(String filename, EGL10Helper egl) {
        VideoDecoder18 decoder = new VideoDecoder18();
        try {
            int count;
            decoder.open(filename);
            Size size = decoder.getOutputSize();
            decoder.start(new OutputSurfaceArray.BufferPool(6, size.width, size.height), 0, 0, 0, false);
            long t1 = System.nanoTime();
            for (count = 0; count < 600; count++) {
                OutputSurfaceArray.Frame frame = decoder.getFrame();
                if (frame == null) {
                    break;
                }
                decoder.freeFrame(frame);
            }
            decoder.stop();
            decoder.close(true);
            Log.d("VideoDecoder18", String.format("benchmark decode took:%dus size:%dx%d nframes:%d", new Object[]{Long.valueOf((System.nanoTime() - t1) / 1000), Integer.valueOf(size.width), Integer.valueOf(size.height), Integer.valueOf(count)}));
            return Double.valueOf(20.0d / (((double) (System.nanoTime() - t1)) / 1.0E9d));
        } catch (IOException e) {
            e.printStackTrace();
            return Double.valueOf(0.0d);
        }
    }

    public OutputSurfaceArray getFrames(String filename, int outWidth, int outHeight, int outFramerate, double startTime, double duration) throws IOException {
        open(filename);
        if (outWidth == 0) {
            outWidth = getOutputWidth();
        }
        if (outHeight == 0) {
            outHeight = getOutputHeight();
        }
        if (outFramerate == 0) {
            outFramerate = getFrameRate();
        }
        int numFrames = (int) (((double) outFramerate) * duration);
        OutputSurfaceArray output = new OutputSurfaceArray();
        output.allocatePool(numFrames, outWidth, outHeight);
        output.mRenderMatrix = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        output.mFramerate = outFramerate;
        long durationUs = (long) Math.ceil(1000000.0d * duration);
        long j = (long) (AudioTrimBean.UNIT / outFramerate);
        start(output.getBufferPool(), j, (long) (1000000.0d * startTime), 0, true);
        OutputSurfaceArray.Frame f;
        do {
            f = getFrame();
            if (f == null) {
                break;
            }
            output.addFrame(f);
            if (this.mCallbacks != null) {
                this.mCallbacks.onFrameReady(output);
            }
        } while (f.mTimestampUs - this.mFirstPresentationTimeUs <= durationUs);
        stop();
        close(false);
        return output;
    }

    public void open(String filename) throws IOException {
        synchronized (VideoDecoder18.class) {
            this.serial = mNextDecoderSerial;
            mNextDecoderSerial++;
            this.TAG = String.format("VideoDecoder:%d (%s)", new Object[]{Integer.valueOf(this.serial), new File(filename).getName()});
            ResourceTracker.markVideo(this.serial, filename);
            Log.d(this.TAG, "input file is " + filename);
            mNumDecoders++;
            Log.d(this.TAG, String.format("opened VideoDecoder, %d active", new Object[]{Integer.valueOf(mNumDecoders)}));
        }
        this.mExtractor = MediaUtils.createExtractor(filename);
        this.mInputFormat = MediaUtils.getVideoTrack(this.mExtractor);
        if (this.mInputFormat != null) {
            long pts0 = this.mExtractor.getSampleTime();
            for (int i = 0; i < 10; i++) {
                this.mExtractor.advance();
            }
            this.mFrameDuration = ((int) (this.mExtractor.getSampleTime() - pts0)) / 10;
            if (this.mInputFormat.containsKey("durationUs")) {
                long duration = this.mInputFormat.getLong("durationUs");
                long frameRate = 30;
                if (this.mInputFormat.containsKey("frame-rate")) {
                    frameRate = (long) this.mInputFormat.getInteger("frame-rate");
                }
                if (frameRate > 0) {
                    this.mLastFrameUs = this.mInputFormat.getLong("durationUs") - (2 * (1000000 / frameRate));
                }
            }
        }
        Log.i(this.TAG, "format: " + this.mInputFormat);
    }

    public int getOutputWidth() {
        return getOutputSize().width;
    }

    public int getOutputHeight() {
        return getOutputSize().height;
    }

    public Size getOutputSize() {
        int width = getInputWidth();
        int height = getInputHeight();
        return isRotated() ? new Size(height, width) : new Size(width, height);
    }

    public Size getOutputSize(int longest) {
        Size os = getOutputSize();
        long width = (long) os.width;
        long height = (long) os.height;
        if (width > height) {
            os = new Size(longest, (int) ((((long) longest) * height) / width));
        } else {
            os = new Size((int) ((((long) longest) * width) / height), longest);
        }
        return new Size((os.width + 1) & -2, (os.height + 1) & -2);
    }

    public int getInputWidth() {
        return this.mInputFormat.getInteger("width");
    }

    public int getInputHeight() {
        return this.mInputFormat.getInteger("height");
    }

    public int getInputRotation() {
        if (this.mInputFormat.containsKey("rotation-degrees")) {
            return this.mInputFormat.getInteger("rotation-degrees");
        }
        return 0;
    }

    public boolean isRotated() {
        int rotation = getInputRotation();
        return rotation == 90 || rotation == 270;
    }

    public int getFrameRate() {
        return this.mInputFormat.containsKey("frame-rate") ? this.mInputFormat.getInteger("frame-rate") : 15;
    }

    public int getFrameDuration() {
        return this.mFrameDuration;
    }

    public void start(Surface surface, int width, int height, int rotation, long outFrameDurationUs, long mediaStartTimeUs, long mediaDurationUs) {
        this.mEncoderSurface = surface;
        this.mEncoderWidth = width;
        this.mEncoderHeight = height;
        this.mEncoderRotation = rotation;
        start(outFrameDurationUs, mediaStartTimeUs, mediaDurationUs);
    }

    public void start(OutputSurfaceArray.BufferPool bufferPool, long outFrameDurationUs, long mediaStartTimeUs, long mediaDurationUs, boolean isGIFMode) {
        this.mBufferPool = bufferPool;
        this.mIsGIFMode = isGIFMode;
        start(outFrameDurationUs, mediaStartTimeUs, mediaDurationUs);
    }

    private void start(long outFrameDurationUs, long mediaStartTimeUs, long mediaDurationUs) {
        Log.d(this.TAG, String.format("initial seek to time %d, duration is %d", new Object[]{Long.valueOf(mediaStartTimeUs), Long.valueOf(mediaDurationUs)}));
        this.mOutputFrameDurationUs = outFrameDurationUs;
        this.mMediaTimeStartUs = mediaStartTimeUs;
        this.mMediaTimeDurationUs = mediaDurationUs;
        this.mExtractor.seekTo(mediaStartTimeUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
//        this.mExtractorThread = new Thread(VideoDecoder18$$Lambda$4.lambdaFactory$(this));
        this.mExtractorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                access$lambda$3();
            }
        });
        this.mExtractorThread.setName(this.TAG + ":extractor");
        this.mExtractorThread.start();
        if (this.mIsAsync) {
//            this.mInputThread = new Thread(VideoDecoder18$$Lambda$5.lambdaFactory$(this));
            this.mInputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    inputThreadEntry();
                }
            });
            this.mInputThread.setName(this.TAG + ":input");
            this.mInputThread.start();
//            this.mOutputThread = new Thread(VideoDecoder18$$Lambda$6.lambdaFactory$(this));
            this.mOutputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    outputThreadEntry();
                }
            });
            this.mOutputThread.setName(this.TAG + ":output");
            this.mOutputThread.start();
        }
//        this.mWorkerThread = new Thread(VideoDecoder18$$Lambda$7.lambdaFactory$(this));
        this.mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                workerThread();
            }
        });
        this.mWorkerThread.setName(this.TAG);
        this.mWorkerThread.start();
    }

    void joinThread(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            App.warnException(e);
        }
    }

    ByteBuffer getExtractorBuffer() {
        ByteBuffer byteBuffer;
        try {
            Trace.beginSection("getExtractorBuffer");
            byteBuffer = (ByteBuffer) this.mExtractorBuffers.take();
        } catch (InterruptedException e) {
            byteBuffer = null;
        } finally {
            Trace.endSection();
        }
        return byteBuffer;
    }

    ExtractorPacket getExtractorPacket() {
        ExtractorPacket extractorPacket;
        try {
            Trace.beginSection("getExtractorPacket");
            extractorPacket = (ExtractorPacket) this.mExtractorPackets.take();
        } catch (InterruptedException e) {
            extractorPacket = null;
        } finally {
            Trace.endSection();
        }
        return extractorPacket;
    }

    /* renamed from: extractorThreadEntry */
    public void access$lambda$3() {
        ExtractorPacket p;
        for (int i = 0; i < 4; i++) {
            this.mExtractorBuffers.add(ByteBuffer.allocate(2000000));
        }
        while (!this.mIsStopping) {
            Trace.beginSection("ExtractorLoop");
            ByteBuffer buffer = getExtractorBuffer();
            if (buffer != null) {
                Trace.beginSection("readSampleData");
                int sampleSize = this.mExtractor.readSampleData(buffer, 0);
                Trace.endSection();
                if (sampleSize < 0) {
                    break;
                }
                long time = this.mExtractor.getSampleTime();
                if (this.mMediaTimeDurationUs != 0 && time > this.mMediaTimeStartUs + this.mMediaTimeDurationUs) {
                    break;
                }
                p = new ExtractorPacket();
                p.buffer = buffer;
                p.size = sampleSize;
                p.pts = time;
                p.eos = false;
                Log.d(this.TAG, String.format("extractor pts:%d size:%d", new Object[]{Long.valueOf(time), Integer.valueOf(sampleSize)}));
                Trace.beginSection("add packet");
                this.mExtractorPackets.add(p);
                Trace.endSection();
                this.mExtractor.advance();
                Trace.endSection();
            } else {
                break;
            }
        }
        Log.d(this.TAG, "extractor eos");
        p = new ExtractorPacket();
        p.eos = true;
        this.mExtractorPackets.add(p);
        if (this.mExtractor != null) {
            this.mExtractor.release();
            this.mExtractor = null;
        }
    }

    public void setDecoderCallbacks(DecoderCallbacks cb) {
        this.mDecoderCallbacks = cb;
    }

    public void stop() {
        synchronized (this.mBufferPoolLock) {
            this.mIsStopping = true;
            this.mBufferPoolLock.notifyAll();
        }
    }

    public void close(boolean blocking) {
        Log.d(this.TAG, String.format("close: blocking:%b", new Object[]{Boolean.valueOf(blocking)}));
        this.mIsStopping = true;
        if (this.mIsAsync) {
            this.mInputMessages.offer(InputMessage.createQuit());
            joinThread(this.mInputThread);
            this.mOutputMessages.offer(OutputMessage.createQuit());
            joinThread(this.mOutputThread);
        }
        if (this.mBufferPool != null) {
            while (true) {
                OutputSurfaceArray.Buffer b = getReadableBuffer(-1);
                if (b == null) {
                    break;
                }
                freeBuffer(b);
            }
            if (!this.mBufferPool.allBuffersAvailable()) {
                Log.d(this.TAG, "buffers not free");
            }
        }
        if (blocking && this.mWorkerThread != null) {
            joinThread(this.mWorkerThread);
        }
        if (blocking && this.mExtractorThread != null) {
            joinThread(this.mExtractorThread);
        }
        synchronized (VideoDecoder18.class) {
            ResourceTracker.freeVideo(this.serial);
            mNumDecoders--;
            Log.d(this.TAG, String.format("closed VideoDecoder, %d active", new Object[]{Integer.valueOf(mNumDecoders)}));
        }
    }

    public boolean hasFinishedBuffering() {
        return this.mHasFinishedBuffering;
    }

    private void workerThread() {
        Log.d(this.TAG, "workerThread starting");
        if (this.mBufferPool != null) {
            this.mCodecSurface = new CodecOutputSurface(Math.max(((float) getOutputWidth()) / ((float) this.mBufferPool.mWidth), ((float) getOutputHeight()) / ((float) this.mBufferPool.mHeight)), 0, this);
        }
        if (this.mEncoderSurface != null) {
            float scale = Math.max(((float) getInputWidth()) / ((float) this.mEncoderWidth), ((float) getInputHeight()) / ((float) this.mEncoderHeight));
            Log.i(this.TAG, "blit transcode used");
            this.mCodecSurface = new CodecOutputSurface(scale, -this.mEncoderRotation, this);
            this.mCodecSurface.setEncoderSurface(this.mEncoderSurface, this.mEncoderWidth, this.mEncoderHeight);
        }
        if (this.mIsAsync) {
            Looper.prepare();
            this.mLooper = Looper.myLooper();
        }
        for (int i = 0; i < 3; i++) {
            try {
                this.mCodec = MediaUtils.createCodec(this.mInputFormat.getString("mime"));
                this.mCodecName = this.mCodec.getName();
                Log.i(this.TAG, String.format("created codec: MIME:%s Name:%s", new Object[]{this.mInputFormat.getString("mime"), this.mCodecName}));
                if (this.mIsAsync) {
                    this.mCodec.setCallback(this.mCodecCallbacks);
                }
                this.mCodec.configure(this.mInputFormat, this.mCodecSurface != null ? this.mCodecSurface.getSurface() : this.mEncoderSurface, null, 0);
                Log.d(this.TAG, "codec configured");
                this.mCodec.start();
                Log.d(this.TAG, "codec started");
            } catch (IllegalStateException e) {
                Log.e(this.TAG, String.format("cannot configure codec %s", new Object[]{e}));
                App.warnException(e);
                this.mCodec = null;
            }
            if (this.mCodec != null) {
                break;
            }
        }
        if (this.mCodec != null) {
            if (this.mIsAsync) {
                asyncDecode();
            } else {
                singleThreadedDecode();
            }
            while (this.mExtractorPackets.size() > 0) {
                ExtractorPacket p = getExtractorPacket();
                if (p.buffer != null) {
                    this.mExtractorBuffers.add(p.buffer);
                }
            }
            writeSentinel();
            try {
                this.mCodec.stop();
                this.mCodec.release();
            } catch (IllegalStateException e2) {
                App.fatalException(e2);
            }
        } else {
            Log.e(this.TAG, "codec failed to configure or start");
            writeSentinel();
        }
        this.mHasFinishedBuffering = true;
        Log.d(this.TAG, "worker thread exiting");
        if (this.mCodecSurface != null) {
            this.mCodecSurface.release();
        }
        if (this.mCodec == null) {
            writeSentinel();
            throw new RuntimeException("codec failed to configure or start");
        } else {
            Log.d(this.TAG, "worker thread finished.");
        }
    }

    private void inputThreadEntry() {
        while (!this.mSawInputEOS) {
            try {
                Trace.beginSection("inputMessage");
                InputMessage m = (InputMessage) this.mInputMessages.take();
                if (m.quit) {
                    Trace.endSection();
                    break;
                }
                Trace.beginSection("getInputBuffer");
                ByteBuffer buffer = this.mCodec.getInputBuffer(m.index);
                Trace.endSection();
                enqueueInputBuffer(m.index, buffer);
                Trace.endSection();
            } catch (InterruptedException e) {
                Log.d(this.TAG, "input interrupted");
            }
        }
        Log.d(this.TAG, "inputThread exiting.");
    }

    private void outputThreadEntry() {
        while (true) {
            try {
                Trace.beginSection("outputMessage");
                OutputMessage m = (OutputMessage) this.mOutputMessages.take();
                if (m.quit) {
                    break;
                }
                if (m.format != null) {
                    updateOutputFormat(m.format);
                }
                if (!(m.index == -1 || m.info == null)) {
                    processOutputBuffer(m.index, m.info);
                }
                Trace.endSection();
            } catch (InterruptedException e) {
                Log.d(this.TAG, "output interrupted");
            }
        }
        Trace.endSection();
        Log.d(this.TAG, "outputThread exiting.");
    }

    @TargetApi(21)
    public void asyncDecode() {
        Looper looper = this.mLooper;
        Looper.loop();
        Log.d(this.TAG, String.format("looper quit: inputEos:%b outputEos:%b", new Object[]{Boolean.valueOf(this.mSawInputEOS), Boolean.valueOf(this.mSawOutputEOS)}));
    }

    public void singleThreadedDecode() {
        BufferInfo info = new BufferInfo();
        ByteBuffer[] codecInputBuffers = null;
        if (VERSION.SDK_INT < 21) {
            codecInputBuffers = this.mCodec.getInputBuffers();
        }
        boolean submitted = false;
        while (!this.mIsStopping && !this.mSawOutputEOS) {
            Trace.beginSection("frame");
            while (!this.mSawInputEOS) {
                Trace.beginSection("dequeueInputBuffer");
                int inputBufferId = this.mCodec.dequeueInputBuffer(0);
                Trace.endSection();
                if (inputBufferId < 0) {
                    break;
                }
                ByteBuffer inputBuffer;
                if (VERSION.SDK_INT >= 21) {
                    inputBuffer = this.mCodec.getInputBuffer(inputBufferId);
                } else {
                    inputBuffer = codecInputBuffers[inputBufferId];
                }
                enqueueInputBuffer(inputBufferId, inputBuffer);
                submitted = true;
            }
            if (submitted) {
                Trace.beginSection("dequeueOutputBuffer");
                int outputBufferId = this.mCodec.dequeueOutputBuffer(info, 100000);
                Trace.endSection();
                if (outputBufferId >= 0) {
                    Trace.beginSection("onOutputBufferAvailable");
                    processOutputBuffer(outputBufferId, info);
                    Trace.endSection();
                } else if (outputBufferId == -2) {
                    updateOutputFormat(this.mCodec.getOutputFormat());
                }
                Trace.endSection();
            } else {
                Trace.endSection();
            }
        }
        if (this.mCodecSurface != null) {
            Trace.beginSection("waitForRender (eos)");
            this.mCodecSurface.waitForRender();
            Trace.endSection();
        }
    }

    public void writeSentinel() {
        Log.d(this.TAG, String.format("writing sentinel, mHaveWrittenSentinel:%b", new Object[]{Boolean.valueOf(this.mHaveWrittenSentinel)}));
        if (!this.mHaveWrittenSentinel) {
            this.mHaveWrittenSentinel = true;
            this.mReadableFrames.offer(OutputSurfaceArray.Buffer.sentinel);
        }
        if (this.mDecoderCallbacks != null) {
            this.mDecoderCallbacks.onFinish();
        }
    }

    public void enqueueInputBuffer(int inputBufferId, ByteBuffer inputBuffer) {
        Trace.beginSection("enqueueInputBuffer");
        ExtractorPacket p = getExtractorPacket();
        if (p == null || p.eos) {
            Log.d(this.TAG, "saw input EOS.");
            this.mCodec.queueInputBuffer(inputBufferId, 0, 0, 0, 4);
            this.mSawInputEOS = true;
            if (!(p == null || p.buffer == null)) {
                this.mExtractorBuffers.add(p.buffer);
            }
            Trace.endSection();
            return;
        }
        int sampleSize = p.size;
        long presentationTimeUs = p.pts;
        inputBuffer.put(p.buffer);
        this.mExtractorBuffers.add(p.buffer);
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("enqueueInputBuffer: id:%d size:%d time:%d", new Object[]{Integer.valueOf(inputBufferId), Integer.valueOf(sampleSize), Long.valueOf(presentationTimeUs)}));
        }
        this.mNumInputBuffers++;
        Trace.beginSection("queueInputBuffer");
        this.mCodec.queueInputBuffer(inputBufferId, 0, sampleSize, presentationTimeUs, 0);
        Trace.endSection();
        Trace.endSection();
    }

    public void processOutputBuffer(int outputBufferId, BufferInfo info) {
        boolean doRender;
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("processOutputBuffer: id:%d size:%d time:%d", new Object[]{Integer.valueOf(outputBufferId), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs)}));
        }
        this.mNumOutputBuffers++;
        if (info.size != 0) {
            doRender = true;
        } else {
            doRender = false;
        }
        if (!doRender || (info.presentationTimeUs <= this.mLastFrameUs && info.presentationTimeUs < this.mMediaTimeStartUs)) {
            doRender = false;
        } else if (this.mFirstPresentationTimeUs == -1) {
            this.mFirstPresentationTimeUs = info.presentationTimeUs;
            this.mNextPresentationTimeUs = info.presentationTimeUs;
        }
        if (info.presentationTimeUs < this.mNextPresentationTimeUs) {
            doRender = false;
        }
        if (this.mMediaTimeDurationUs != 0 && info.presentationTimeUs >= this.mMediaTimeStartUs + this.mMediaTimeDurationUs) {
            Log.d(this.TAG, "eos: output timestamp is past requested duration.");
            this.mSawOutputEOS = true;
        }
        if (this.mOutputFormat == null) {
            doRender = false;
        }
        if (doRender) {
            if (this.VERBOSE) {
                Log.d(this.TAG, String.format("doRender pts:%d", new Object[]{Long.valueOf(info.presentationTimeUs)}));
            }
            if (this.mCodecSurface != null) {
                Trace.beginSection("waitForRender");
                this.mCodecSurface.waitForRender();
                Trace.endSection();
            }
            OutputSurfaceArray.Buffer b;
            if (this.mEncoderSurface != null) {
                if (this.mCodecSurface != null) {
                    b = OutputSurfaceArray.Buffer.sentinel;
                    b.mTimestamp = info.presentationTimeUs;
                    Trace.beginSection("setRenderBuffer");
                    this.mCodecSurface.setRenderBuffer(b);
                    Trace.endSection();
                }
                Trace.beginSection("releaseOutputSurface");
                this.mCodec.releaseOutputBuffer(outputBufferId, true);
                Trace.endSection();
            } else {
                b = getWritableBuffer();
                if (b != null) {
                    b.mTimestamp = info.presentationTimeUs;
                    this.mCodecSurface.setRenderBuffer(b);
                    Trace.beginSection("releaseOutputSurface(true)");
                    this.mCodec.releaseOutputBuffer(outputBufferId, true);
                    Trace.endSection();
                } else {
                    Trace.beginSection("releaseOutputSurface(false)");
                    this.mCodec.releaseOutputBuffer(outputBufferId, false);
                    Trace.endSection();
                    this.mSawOutputEOS = true;
                }
            }
            if (this.mDecoderCallbacks != null) {
                this.mDecoderCallbacks.onFrameDecoded(info.presentationTimeUs);
            }
            this.mNextPresentationTimeUs += this.mOutputFrameDurationUs;
        } else {
            Trace.beginSection("releaseOutputSurface(false)");
            this.mCodec.releaseOutputBuffer(outputBufferId, false);
            Trace.endSection();
        }
        if ((info.flags & 4) != 0) {
            Log.d(this.TAG, "saw output EOS.");
            this.mSawOutputEOS = true;
            if (this.mLooper != null) {
                this.mLooper.quit();
            }
        }
    }

    public void updateOutputFormat(MediaFormat format) {
        this.mOutputFormat = format;
        Log.d(this.TAG, "output format has changed to " + this.mOutputFormat);
        int x0 = this.mOutputFormat.getInteger("crop-left");
        int y0 = this.mOutputFormat.getInteger("crop-top");
        int x1 = this.mOutputFormat.getInteger("crop-right");
        int y1 = this.mOutputFormat.getInteger("crop-bottom");
        int w = this.mOutputFormat.getInteger("width");
        int h = this.mOutputFormat.getInteger("height");
        Log.d(this.TAG, String.format("CROP: original x0:%d y0:%d x1:%d y1:%d w:%d h:%d", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(w), Integer.valueOf(h)}));
        if (!this.mCodecName.equals("OMX.qcom.video.decoder.avc")) {
            x1++;
            y1++;
        } else if (this.mOutputFormat.getInteger("color-format") == 2141391876) {
            w = (x1 - x0) + 1;
            h = (y1 - y0) + 1;
            x1 -= x0;
            y1 -= y0;
            x0 = 0;
            y0 = 0;
        }
        if (this.mCodecSurface != null) {
            this.mCodecSurface.setRect(x0, y0, x1, y1, w, h);
        }
    }

    public void onFrameAvailable(OutputSurfaceArray.Buffer b) {
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("onFrameAvailable id:%d, timestamp:%d", new Object[]{Integer.valueOf(b.mID), Long.valueOf(b.mTimestamp)}));
        }
        if (!this.mReadableFrames.offer(b)) {
            throw new RuntimeException("mReadableFrames did not accept the offer");
        }
    }

    public void onFrameAborted(OutputSurfaceArray.Buffer b) {
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("onFrameAborted id:%d", new Object[]{Integer.valueOf(b.mID)}));
        }
        freeBuffer(b);
    }

    private OutputSurfaceArray.Buffer getWritableBuffer() {
        synchronized (this.mBufferPoolLock) {
            if (!this.mIsGIFMode) {
                while (this.mBufferPool.freeCount() == 0 && !this.mIsStopping) {
                    try {
                        this.mBufferPoolLock.wait();
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
            } else if (this.mBufferPool.freeCount() == 0) {
                return null;
            }
            if (this.mIsStopping) {
                return null;
            }
            OutputSurfaceArray.Buffer b = this.mBufferPool.alloc();
            if (this.VERBOSE) {
                Log.d(this.TAG, String.format("getWritableBuffer id:%d", new Object[]{Integer.valueOf(b.mID)}));
            }
            return b;
        }
    }

    public void freeFrame(OutputSurfaceArray.Frame f) {
        freeBuffer(f.mBuffer);
    }

    public void freeBuffer(OutputSurfaceArray.Buffer b) {
        synchronized (this.mBufferPoolLock) {
            if (this.VERBOSE) {
                Log.d(this.TAG, String.format("freeFrame id:%d", new Object[]{Integer.valueOf(b.mID)}));
            }
            this.mBufferPool.free(b);
            this.mBufferPoolLock.notifyAll();
        }
    }

    public OutputSurfaceArray.Frame getFrame() {
        OutputSurfaceArray.Buffer b = getReadableBuffer(-1);
        if (b == null) {
            return null;
        }
        return new OutputSurfaceArray.Frame(b, b.mTimestamp, 0);
    }

    private synchronized OutputSurfaceArray.Buffer getReadableBuffer(long timeoutUs) {
        OutputSurfaceArray.Buffer b;
        try {
            if (this.mHaveReadSentinel) {
                Log.d(this.TAG, "getReadableBuffer sentinel");
                b = null;
            } else {
                if (timeoutUs >= 0) {
                    b = (OutputSurfaceArray.Buffer) this.mReadableFrames.poll(timeoutUs, TimeUnit.MICROSECONDS);
                } else {
                    b = (OutputSurfaceArray.Buffer) this.mReadableFrames.take();
                }
                if (b == OutputSurfaceArray.Buffer.sentinel) {
                    Log.d(this.TAG, "getReadableBuffer sentinel");
                    this.mHaveReadSentinel = true;
                    b = null;
                } else if (this.VERBOSE) {
                    Log.d(this.TAG, String.format("getReadableBuffer id:%d", new Object[]{Integer.valueOf(b.mID)}));
                }
            }
        } catch (InterruptedException e) {
            b = null;
        }
        return b;
    }

    private boolean shouldAbort() {
        if (this.mCallbacks != null && this.mCallbacks.shouldAbort()) {
            this.mAborted = true;
        }
        return this.mAborted;
    }
}