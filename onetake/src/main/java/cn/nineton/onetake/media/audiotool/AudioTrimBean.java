package cn.nineton.onetake.media.audiotool;

public class AudioTrimBean {
    public static final int UNIT = 1000000;
    private float duration;
    private float startTime;
    private float verse;

    public AudioTrimBean(float verse, float startTime, float duration) {
        this.verse = verse;
        this.startTime = startTime;
        this.duration = duration;
    }

    public float getVerse() {
        return this.verse;
    }

    public void setVerse(float verse) {
        this.verse = verse;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}