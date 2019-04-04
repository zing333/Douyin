package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.VideoTools.Ratio;

import cn.nineton.onetake.media.Ratio;

public class ProxyFileInfo {
    private long inputVideoDurationUs;
    private int inputVideoHeight;
    private String inputVideoPath;
    private long inputVideoStartTimeUs;
    private int inputVideoWidth;
    private String outputVideoPath;
    private long timeLineDurationUs;
    private long timeLineStartTimeUs;
    private float videoSpeed;

    public ProxyFileInfo(String inputVideoPath, String outputVideoPath, long inputVideoStartTimeUs, long inputVideoDurationUs, long timeLineStartTimeUs, long timeLineDurationUs, int inputVideoWidth, int inputVideoHeight, float videoSpeed) {
        this.inputVideoPath = inputVideoPath;
        this.outputVideoPath = outputVideoPath;
        this.inputVideoStartTimeUs = inputVideoStartTimeUs;
        this.inputVideoDurationUs = inputVideoDurationUs;
        this.timeLineStartTimeUs = timeLineStartTimeUs;
        this.timeLineDurationUs = timeLineDurationUs;
        this.inputVideoWidth = inputVideoWidth;
        this.inputVideoHeight = inputVideoHeight;
        this.videoSpeed = videoSpeed;
    }

    public void setVideoSpeed(float videoSpeed) {
        this.videoSpeed = videoSpeed;
    }

    public Ratio getProxyVideoSpeed() {
        return new Ratio(100, (int) (this.videoSpeed * 100.0f));
    }

    public long getTimeLineStartTimeUs() {
        return this.timeLineStartTimeUs;
    }

    public long getTimeLineStartTimeMs() {
        return (long) ((((float) this.timeLineStartTimeUs) * 1.0f) / 1000.0f);
    }

    public void setTimeLineStartTimeUs(long timeLineStartTimeUs) {
        this.timeLineStartTimeUs = timeLineStartTimeUs;
    }

    public long getTimeLineDurationUs() {
        return this.timeLineDurationUs;
    }

    public void setTimeLineDurationUs(long timeLineDurationUs) {
        this.timeLineDurationUs = timeLineDurationUs;
    }

    public String getInputVideoPath() {
        return this.inputVideoPath;
    }

    public void setInputVideoPath(String inputVideoPath) {
        this.inputVideoPath = inputVideoPath;
    }

    public String getOutputVideoPath() {
        return this.outputVideoPath;
    }

    public void setOutputVideoPath(String outputVideoPath) {
        this.outputVideoPath = outputVideoPath;
    }

    public long getInputVideoStartTimeUs() {
        return this.inputVideoStartTimeUs;
    }

    public void setInputVideoStartTimeUs(long inputVideoStartTimeUs) {
        this.inputVideoStartTimeUs = inputVideoStartTimeUs;
    }

    public long getInputVideoDurationUs() {
        return this.inputVideoDurationUs;
    }

    public void setInputVideoDurationUs(long inputVideoDurationUs) {
        this.inputVideoDurationUs = inputVideoDurationUs;
    }

    public int getInputVideoWidth() {
        return this.inputVideoWidth;
    }

    public void setInputVideoWidth(int inputVideoWidth) {
        this.inputVideoWidth = inputVideoWidth;
    }

    public int getInputVideoHeight() {
        return this.inputVideoHeight;
    }

    public void setInputVideoHeight(int inputVideoHeight) {
        this.inputVideoHeight = inputVideoHeight;
    }
}