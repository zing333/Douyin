package cn.nineton.onetake.bean;

public class MscvCelebritiesBean {
    private double confidence;
    private MscvFaceRectangleBean faceRectangle;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MscvFaceRectangleBean getFaceRectangle() {
        return this.faceRectangle;
    }

    public void setFaceRectangle(MscvFaceRectangleBean faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String toString() {
        return "MscvCelebritiesBean{name='" + this.name + '\'' + ", faceRectangle=" + this.faceRectangle + ", confidence=" + this.confidence + '}';
    }
}