package cn.nineton.onetake.media.audiotool;

public class DummyAudioOutput implements AudioOutput {
    long mBaseTime;
    boolean mPaused;
    int mSampleRate;
    int mSampleRateHundreds;

    public DummyAudioOutput() {
        this.mPaused = false;
        this.mBaseTime = 0;
        this.mSampleRate = 44100;
        this.mSampleRateHundreds = this.mSampleRate / 100;
        this.mBaseTime = System.nanoTime();
    }

    public boolean isRealtime() {
        return false;
    }

    public boolean needsZeroTrack() {
        return true;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getRecommendedBufferSize() {
        return this.mSampleRate * 3;
    }

    public long getPlayPosition() {
        long now = System.nanoTime();
        if (this.mPaused) {
            now = this.mBaseTime;
        }
        return (((long) this.mSampleRateHundreds) * (now - this.mBaseTime)) / 10000000;
    }

    public void pause() {
        this.mPaused = true;
    }

    public void resume() {
        this.mPaused = false;
        this.mBaseTime = System.nanoTime();
    }

    public void flush() {
    }

    public void clear() {
    }

    public void writeAudio(long presentationSample, short[] data, int offset, int num_shorts) {
        long delta = (presentationSample - getPlayPosition()) - ((long) (this.mSampleRate / 2));
        if (delta > 0) {
            try {
                Thread.sleep((1000 * delta) / ((long) this.mSampleRate));
            } catch (InterruptedException e) {
            }
        }
    }

    public void finished() {
    }

    public void close() {
    }
}