package cn.nineton.onetake.widget;

import android.media.AudioTrack;

import cn.nineton.onetake.media.audiotool.AudioOutput;
import cn.nineton.onetake.media.audiotool.AudioSink;

public class SystemOutput implements AudioOutput, AudioSink {
    private AudioTrack mAudioTrack;
    private int mRecommendedBufferSize;
    private int mSampleRate = AudioTrack.getNativeOutputSampleRate(3);

    SystemOutput() {
        int minBuffSizeBytes = AudioTrack.getMinBufferSize(this.mSampleRate, 12, 2);
        this.mRecommendedBufferSize = minBuffSizeBytes / 4;
        this.mAudioTrack = new AudioTrack(3, this.mSampleRate, 12, 2, minBuffSizeBytes, 1);
        if (this.mAudioTrack != null) {
            this.mAudioTrack.play();
        }
    }

    public boolean isRealtime() {
        return true;
    }

    public boolean needsZeroTrack() {
        return true;
    }

    public boolean isValid() {
        return this.mAudioTrack != null;
    }

    public void close() {
        if (this.mAudioTrack != null) {
            this.mAudioTrack.flush();
            this.mAudioTrack.release();
            this.mAudioTrack = null;
        }
    }

    public void clear() {
        this.mAudioTrack.pause();
        this.mAudioTrack.flush();
        this.mAudioTrack.play();
    }

    public void pause() {
        this.mAudioTrack.pause();
    }

    public void resume() {
        this.mAudioTrack.play();
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getRecommendedBufferSize() {
        return this.mRecommendedBufferSize;
    }

    public long getPlayPosition() {
        return (long) this.mAudioTrack.getPlaybackHeadPosition();
    }

    public void flush() {
    }

    public void writeAudio(long presentationSample, short[] data, int offset, int num_shorts) {
        this.mAudioTrack.write(data, offset, num_shorts);
    }

    public void finished() {
    }
}