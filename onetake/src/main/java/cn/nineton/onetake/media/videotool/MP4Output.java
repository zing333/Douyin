package cn.nineton.onetake.media.videotool;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cn.nineton.onetake.db.table.IMMsgTable;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.media.Ratio;
import cn.nineton.onetake.media.audiotool.AudioEncoderAAC;
import cn.nineton.onetake.media.audiotool.AudioOutput;
import cn.nineton.onetake.media.audiotool.AudioSink;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;

public class MP4Output implements AudioOutput, AudioSink {
    private static final String TAG = "MP4Output";
    int framecount = 0;
    AudioEncoderAAC mAudioEncoder;
    AudioEncoderAAC.Callbacks mAudioEncoderCallbacks = new AudioEncoderAAC.Callbacks() {
        public void onAudioFormatChanged(MediaFormat format) {
            MP4Output.this.mAudioTrackIndex = MP4Output.this.mMediaMuxer.addTrack(format);
            MP4Output.this.tryStartMuxer();
        }

        public void onAudioBufferAvailable(BufferInfo info, ByteBuffer data) {
            synchronized (MP4Output.this) {
                if ((info.flags & 2) != 0) {
                    Log.w(MP4Output.TAG, String.format("got encoded audio buffer with config flag, dropping", new Object[0]));
                } else if ((info.flags & 4) != 0) {
                    Log.w(MP4Output.TAG, String.format("got encoded audio buffer with eos flag, dropping", new Object[0]));
                } else {
                    Log.d(MP4Output.TAG, String.format("got encoded audio with pts:%dus", new Object[]{Long.valueOf(info.presentationTimeUs)}));
                    MP4Output.this.mAudioFrames.add(new EncodedFrame(info, data));
                    MP4Output.this.mux(false);
                }
            }
        }
    };
    private Queue<EncodedFrame> mAudioFrames = new LinkedList();
    int mAudioTrackIndex = -1;
    long mExpectedPresentationSample = 0;
    MediaMuxer mMediaMuxer;
    boolean mMuxerStarted;
    boolean mMuxerStopped;
    long mPlayPosition;
    int mSampleRate = 44100;
    public VideoEncoderHW mVideoEncoder;
    VideoEncoderHW.Callbacks mVideoEncoderCallbacks = new VideoEncoderHW.Callbacks() {
        public void onVideoFormatChanged(MediaFormat format) {
            MP4Output.this.mVideoTrackIndex = MP4Output.this.mMediaMuxer.addTrack(format);
            MP4Output.this.tryStartMuxer();
        }

        public void onVideoFrameAvailable(BufferInfo info, ByteBuffer data) {
            synchronized (MP4Output.this) {
                if ((info.flags & 2) != 0) {
                    Log.w(MP4Output.TAG, String.format("got encoded video buffer with config flag, dropping", new Object[0]));
                } else if ((info.flags & 4) != 0) {
                    Log.w(MP4Output.TAG, String.format("got encoded video buffer with eos flag, dropping", new Object[0]));
                } else {
                    long newPts = MP4Output.this.mVideoTimestampMultiplier.multiply(info.presentationTimeUs);
                    Log.d(MP4Output.TAG, String.format("got encoded video with pts:%dus -> %dus", new Object[]{Long.valueOf(info.presentationTimeUs), Long.valueOf(newPts)}));
                    info.presentationTimeUs = newPts;
                    MP4Output.this.mVideoFrames.add(new EncodedFrame(info, data));
                    MP4Output.this.mux(false);
                }
            }
        }
    };
    private Queue<EncodedFrame> mVideoFrames = new LinkedList();
    Ratio mVideoTimestampMultiplier = new Ratio(1, 1);
    int mVideoTrackIndex = -1;
    boolean save = false;

    static final class EncodedFrame {
        ByteBuffer mData;
        BufferInfo mInfo = new BufferInfo();

        EncodedFrame(BufferInfo info, ByteBuffer data) {
            this.mInfo.set(info.offset, info.size, info.presentationTimeUs, info.flags);
            this.mData = ByteBuffer.allocate(data.limit());
            this.mData.put(data);
        }
    }

    public MP4Output(String outputFilename) throws IOException {
        new File(outputFilename).delete();
        this.mMediaMuxer = new MediaMuxer(outputFilename, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }

    public synchronized void enableVideo(int bitrate, int width, int height, int frameDurationUs, Map<String, Object> formatProps, boolean directSurface) {
        this.mVideoEncoder = new VideoEncoderHW();
        this.mVideoEncoder.start(null, width, height, frameDurationUs, bitrate, formatProps);
        this.mVideoEncoder.prepareEncoder(this.mVideoEncoderCallbacks, directSurface);
    }

    public synchronized void enableAudio(int sampleRate, int bitrate) {
        this.mSampleRate = sampleRate;
        this.mAudioEncoder = new AudioEncoderAAC(this.mSampleRate, 128000, 2);
        this.mAudioEncoder.prepareEncoder(this.mAudioEncoderCallbacks);
    }

    public boolean isRealtime() {
        return false;
    }

    public boolean needsZeroTrack() {
        return true;
    }

    public synchronized void close() {
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.flush();
            this.mAudioEncoder.close();
        }
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.flush();
            this.mVideoEncoder.close();
        }
        mux(true);
        if (this.mMuxerStarted && !this.mMuxerStopped) {
            try {
                this.mMediaMuxer.stop();
            } catch (IllegalStateException e) {
            }
            this.mMuxerStopped = true;
        }
        try {
            this.mMediaMuxer.release();
        } catch (IllegalStateException e2) {
        }
    }

    public void clear() {
    }

    public void pause() {
        throw new RuntimeException("unimplemented");
    }

    public void resume() {
        throw new RuntimeException("unimplemented");
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getRecommendedBufferSize() {
        return this.mSampleRate;
    }

    public long getPlayPosition() {
        return this.mPlayPosition;
    }

    public void setPlayPosition(long pos) {
        this.mPlayPosition = pos;
    }

    private byte[] shortArrayToByteArray(short[] data, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(length * 2);
        bb.order(ByteOrder.nativeOrder());
        bb.asShortBuffer().put(data, offset, length);
        return bb.array();
    }

    public void flush() {
    }

    public void writeAudio(long presentationSample, short[] data, int offset, int numSamples) {
        if (presentationSample != this.mExpectedPresentationSample) {
            Log.e(TAG, "non-contiguous audio");
        }
        this.mExpectedPresentationSample = ((long) (numSamples / 2)) + presentationSample;
        Log.d(TAG, String.format("got audio pts:%d -> %d", new Object[]{Long.valueOf(presentationSample), Long.valueOf(this.mExpectedPresentationSample - 1)}));
        this.mAudioEncoder.offerEncoder(shortArrayToByteArray(data, offset, numSamples), false);
    }

    public void finished() {
    }

    void tryStartMuxer() {
        if (!this.mMuxerStarted) {
            boolean ready = true;
            if (this.mAudioEncoder != null && this.mAudioTrackIndex == -1) {
                ready = false;
            }
            if (this.mVideoEncoder != null && this.mVideoTrackIndex == -1) {
                ready = false;
            }
            if (ready) {
                this.mMuxerStarted = true;
                this.mMediaMuxer.start();
            }
        }
    }

    private synchronized void mux(boolean isFlushing) {
        while (true) {
            EncodedFrame firstAudio = (EncodedFrame) this.mAudioFrames.peek();
            EncodedFrame firstVideo = (EncodedFrame) this.mVideoFrames.peek();
            if (firstAudio == null && firstVideo == null) {
                break;
            } else if (this.mAudioEncoder == null || this.mVideoEncoder == null) {
                if (!(this.mAudioEncoder == null || firstAudio == null)) {
                    emitAudio();
                }
                if (!(this.mVideoEncoder == null || firstVideo == null)) {
                    emitVideo();
                }
            } else if (firstAudio == null || firstVideo == null) {
                if (!isFlushing) {
                    break;
                } else if (firstAudio != null) {
                    emitAudio();
                } else {
                    emitVideo();
                }
            } else if (firstAudio.mInfo.presentationTimeUs < firstVideo.mInfo.presentationTimeUs) {
                emitAudio();
            } else {
                emitVideo();
            }
        }
    }

    private void emitAudio() {
        emit(this.mAudioTrackIndex, (EncodedFrame) this.mAudioFrames.poll());
    }

    private void emitVideo() {
        emit(this.mVideoTrackIndex, (EncodedFrame) this.mVideoFrames.poll());
    }

    private void emit(int track, EncodedFrame frame) {
        String str = TAG;
        String str2 = "emit: track:%d/%s pts:%d";
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(track);
        objArr[1] = track == this.mAudioTrackIndex ? "audio" : IMMsgTable.TYPE_VIDEO;
        objArr[2] = Long.valueOf(frame.mInfo.presentationTimeUs);
        Log.d(str, String.format(str2, objArr));
        if (!this.mMuxerStarted) {
            Log.e(TAG, "writing data on muxer that has not started");
        } else if (this.mMuxerStopped) {
            Log.e(TAG, "writing data on stopped muxer!");
        } else {
            try {
                this.mMediaMuxer.writeSampleData(track, frame.mData, frame.mInfo);
            } catch (IllegalStateException e) {
                Log.e(TAG, "muxer error", e);
                throw e;
            }
        }
    }

    public void startFrame(EGL10Helper egl, long timeUs) {
        this.mVideoEncoder.mInputSurface.makeCurrent(egl, timeUs);
    }

    public void finishFrame(EGL10Helper egl) {
        if (this.save) {
            BitmapUtils.savePixels(String.format("frame-%d.jpg", new Object[]{Integer.valueOf(this.framecount)}), this.mVideoEncoder.getWidth(), this.mVideoEncoder.getHeight());
            this.framecount++;
        }
        this.mVideoEncoder.mInputSurface.swap(egl);
        this.mVideoEncoder.drainEncoder(false);
    }
}