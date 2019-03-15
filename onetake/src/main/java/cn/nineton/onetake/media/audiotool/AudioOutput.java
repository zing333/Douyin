package cn.nineton.onetake.media.audiotool;

public interface AudioOutput extends AudioSink {
    void close();

    long getPlayPosition();

    int getRecommendedBufferSize();

    int getSampleRate();

    boolean isRealtime();

    boolean needsZeroTrack();

    void pause();

    void resume();
}