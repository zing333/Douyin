package cn.nineton.onetake.media.audiotool;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.media.videotool.ResamplerCubic;

public class AudioDecoder {
    static int mDecoderSerial = 0;
    static int mNumDecoders = 0;
    private String TAG = "AudioDecoder";
    private boolean VERBOSE = false;
    MediaCodec mCodec;
    String mCodecName;
    short[] mDecoderOutput;
    long mDurationSamples;
    MediaExtractor mExtractor;
    private boolean mHasFinishedBuffering;
    MediaFormat mInputFormat;
    boolean mIsStopping;
    long mMediaTimeStartUs;
    private int mNumChannels;
    int mNumOutputBuffers;
    Output mOutput;
    MediaFormat mOutputFormat;
    int mOutputRate;
    long mPresentationSample;
    ResamplerCubic mResampler = new ResamplerCubic();
    private int mSampleRate;
    boolean mSawInputEOS = false;
    boolean mSawOutputEOS = false;
    Thread mThread;

    public interface Output {
        void disable();

        void finish();

        void writeAudio(long j, short[] sArr, int i);
    }

    private static final class AudioMixerOutput implements Output {
        private final String TAG = "AudioDecoder.AudioMixerOutput";
        AudioMixer mAudioMixer;
        AudioMixer.Track mAudioMixerTrack;

        AudioMixerOutput(AudioMixer audioMixer, AudioMixer.Track audioMixerTrack) {
            this.mAudioMixer = audioMixer;
            this.mAudioMixerTrack = audioMixerTrack;
        }

        public void writeAudio(long presentationSample, short[] data, int numFrames) {
            this.mAudioMixer.addFrame(this.mAudioMixerTrack, new AudioMixer.Frame(presentationSample + this.mAudioMixerTrack.mFirstPresentationSample, data, numFrames));
        }

        public void finish() {
            this.mAudioMixer.trackFinished(this.mAudioMixerTrack);
        }

        public void disable() {
            this.mAudioMixer.disableTrack(this.mAudioMixerTrack);
        }
    }

    public AudioDecoder() {
    }

    public void open(String filename) throws IOException {
        this.TAG = String.format("AudioDecoder:%d (%s)", mDecoderSerial, new File(filename).getName());
        mDecoderSerial++;
        synchronized (AudioDecoder.class) {
            mNumDecoders++;
            Log.d(this.TAG, String.format("opened AudioDecoder, %d active", Integer.valueOf(mNumDecoders)));
        }
        this.mExtractor = MediaUtils.createExtractor(filename);
        this.mInputFormat = MediaUtils.getAudioTrack(this.mExtractor);
        if (this.mInputFormat == null) {
            throw new RuntimeException("audio enabled for media but has no audio");
        }
        Log.i(this.TAG, "format: " + this.mInputFormat);
        this.mCodec = MediaUtils.createCodec(this.mInputFormat.getString("mime"));
        this.mCodecName = this.mCodec.getName();
        String mime = this.mInputFormat.getString("mime");
        Log.i(this.TAG, String.format("created codec: MIME:%s Name:%s", mime, this.mCodecName));
    }

    public long getDurationUs() {
        return this.mInputFormat.getLong("durationUs");
    }

    public void start(long mediaStartTimeUs, AudioMixer audioMixer, AudioMixer.Track track, long durationSamples) {
        start(new AudioMixerOutput(audioMixer, track), (int) audioMixer.getSampleRate(), mediaStartTimeUs, durationSamples);
    }

    public void start(Output output, int outputRate, long mediaStartTimeUs, long durationSamples) {
        Log.d(this.TAG, String.format("start with time %d", mediaStartTimeUs));
        this.mOutput = output;
        this.mOutputRate = outputRate;
        this.mPresentationSample = Long.MAX_VALUE;
        this.mDurationSamples = durationSamples;
        this.mMediaTimeStartUs = mediaStartTimeUs;
        this.mExtractor.seekTo(mediaStartTimeUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        this.mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                workerThread();
            }
        });
        this.mThread.setName("AudioDecoder");
        this.mThread.start();
    }

    public void waitUntilBuffered() {
        try {
            this.mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        close(false);
    }

    public void close(boolean blocking) {
        this.mIsStopping = true;
        if (this.mOutput != null) {
            this.mOutput.disable();
        }
        if (blocking && this.mThread != null) {
            try {
                if (this.mThread != null) {
                    this.mThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (AudioDecoder.class) {
            mNumDecoders--;
            Log.d(this.TAG, String.format("closed AudioDecoder, %d active", new Object[]{Integer.valueOf(mNumDecoders)}));
        }
    }

    private void workerThread() {
        Log.d(this.TAG, "workerThread starting");
        this.mCodec.configure(this.mInputFormat, null, null, 0);
        Log.d(this.TAG, "codec configured");
        this.mCodec.start();
        Log.d(this.TAG, "codec started");
        BufferInfo info = new BufferInfo();
        boolean submitted = false;
        this.mNumOutputBuffers = 0;
        ByteBuffer[] codecInputBuffers = null;
        ByteBuffer[] codecOutputBuffers = null;
        if (VERSION.SDK_INT < 21) {
            codecInputBuffers = this.mCodec.getInputBuffers();
            codecOutputBuffers = this.mCodec.getOutputBuffers();
        }
        while (!this.mIsStopping && !this.mSawOutputEOS) {
            while (!this.mSawInputEOS) {
                int inputBufferId = this.mCodec.dequeueInputBuffer(0);
                if (inputBufferId < 0) {
                    break;
                }
                ByteBuffer inputBuffer;
                if (VERSION.SDK_INT >= 21) {
                    inputBuffer = this.mCodec.getInputBuffer(inputBufferId);
                } else {
                    inputBuffer = codecInputBuffers[inputBufferId];
                }
                onInputBufferAvailable(this.mCodec, inputBufferId, inputBuffer);
                submitted = true;
            }
            if (submitted) {
                int outputBufferId = this.mCodec.dequeueOutputBuffer(info, 100000);
                if (outputBufferId >= 0) {
                    ByteBuffer outputBuffer;
                    if (VERSION.SDK_INT >= 21) {
                        outputBuffer = this.mCodec.getOutputBuffer(outputBufferId);
                    } else {
                        outputBuffer = codecOutputBuffers[outputBufferId];
                    }
                    onOutputBufferAvailable(this.mCodec, outputBufferId, outputBuffer, info);
                } else if (outputBufferId == -2) {
                    onOutputFormatChanged(this.mCodec, this.mCodec.getOutputFormat());
                }
            }
        }
        Log.d(this.TAG, "worker thread exiting");
        this.mHasFinishedBuffering = true;
        if (this.mOutput != null) {
            this.mOutput.finish();
            this.mOutput = null;
        }
        if (this.mExtractor != null) {
            this.mExtractor.release();
            this.mExtractor = null;
        }
        try {
            this.mCodec.stop();
            this.mCodec.release();
        } catch (IllegalStateException e) {
        }
        Log.d(this.TAG, "worker thread finished.");
    }

    public boolean hasFinishedBuffering() {
        return this.mHasFinishedBuffering;
    }

    public void onInputBufferAvailable(MediaCodec codec, int inputBufferId, ByteBuffer inputBuffer) {
        boolean isEOS;
        int i = 4;
        int sampleSize = this.mExtractor.readSampleData(inputBuffer, 0);
        long presentationTimeUs = 0;
        if (sampleSize < 0) {
            isEOS = true;
        } else {
            isEOS = false;
        }
        if (isEOS) {
            Log.d(this.TAG, "saw input EOS.");
            this.mSawInputEOS = true;
            sampleSize = 0;
        } else {
            presentationTimeUs = this.mExtractor.getSampleTime();
        }
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("onInputBufferAvailable: id:%d size:%d time:%d eos:%b", new Object[]{Integer.valueOf(inputBufferId), Integer.valueOf(sampleSize), Long.valueOf(presentationTimeUs), Boolean.valueOf(isEOS)}));
        }
        MediaCodec mediaCodec = this.mCodec;
        if (!isEOS) {
            i = 0;
        }
        mediaCodec.queueInputBuffer(inputBufferId, 0, sampleSize, presentationTimeUs, i);
        if (!isEOS) {
            this.mExtractor.advance();
        }
    }

    public void onOutputBufferAvailable(MediaCodec codec, int outputBufferId, ByteBuffer buf, BufferInfo info) {
        if (this.VERBOSE) {
            Log.d(this.TAG, String.format("onOutputBufferAvailable: id:%d size:%d time:%d", new Object[]{Integer.valueOf(outputBufferId), Integer.valueOf(info.size), Long.valueOf(info.presentationTimeUs)}));
        }
        boolean eos = false;
        if ((info.flags & 4) != 0) {
            Log.d(this.TAG, "saw output EOS.");
            eos = true;
        }
        if (info.size > 0) {
            int resampledLengthInShorts;
            int size = info.size / 2;
            if (this.mDecoderOutput == null || this.mDecoderOutput.length < size) {
                this.mDecoderOutput = new short[size];
            }
            buf.asShortBuffer().get(this.mDecoderOutput, 0, size);
            int resampledCapacity = ((this.mResampler.getmOutputRate() * size) / this.mResampler.getmInputRate()) + 20;
            if (this.mNumChannels == 1) {
                resampledCapacity *= 2;
            }
            short[] resamplerOutput = new short[resampledCapacity];
            if (this.mNumChannels == 1) {
                resampledLengthInShorts = this.mResampler.resampleMono(resamplerOutput, this.mDecoderOutput, size);
            } else {
                resampledLengthInShorts = this.mResampler.resampleStereo(resamplerOutput, this.mDecoderOutput, size);
            }
            if (this.mPresentationSample == Long.MAX_VALUE) {
                this.mPresentationSample = (((long) this.mOutputRate) * (info.presentationTimeUs - this.mMediaTimeStartUs)) / 1000000;
            }
            int resampledDuration = resampledLengthInShorts / 2;
            this.mOutput.writeAudio(this.mPresentationSample, resamplerOutput, resampledDuration);
            if (this.VERBOSE) {
                Log.d(this.TAG, String.format("writeAudio: pts:%d duration:%d", new Object[]{Long.valueOf(this.mPresentationSample), Integer.valueOf(resampledDuration)}));
            }
            this.mPresentationSample += (long) resampledDuration;
            if (this.mDurationSamples != 0 && this.mPresentationSample >= this.mDurationSamples) {
                Log.d(this.TAG, "reached end of requested chunk");
                eos = true;
            }
            this.mNumOutputBuffers++;
            codec.releaseOutputBuffer(outputBufferId, false);
        }
        if (eos) {
            this.mSawOutputEOS = true;
        }
    }

    public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
        this.mOutputFormat = format;
        this.mSampleRate = format.getInteger("sample-rate");
        this.mNumChannels = format.getInteger("channel-count");
        this.mResampler.configure(this.mOutputRate, this.mSampleRate);
    }
}