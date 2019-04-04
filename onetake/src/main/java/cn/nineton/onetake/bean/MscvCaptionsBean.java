package cn.nineton.onetake.bean;

public class MscvCaptionsBean {
    private double confidence;
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String toString() {
        return "MscvCaptionsBean{text='" + this.text + '\'' + ", confidence=" + this.confidence + '}';
    }
}