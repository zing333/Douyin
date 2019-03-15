package cn.nineton.onetake.media.audiotool;

public interface AudioSink {
    void clear();

    void finished();

    void flush();

    void writeAudio(long j, short[] sArr, int i, int i2);
}