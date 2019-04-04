package cn.nineton.onetake.bean;

public class MscvTagBean {
    private double confidence;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String toString() {
        return "MscvTagBean{name='" + this.name + '\'' + ", confidence=" + this.confidence + '}';
    }
}