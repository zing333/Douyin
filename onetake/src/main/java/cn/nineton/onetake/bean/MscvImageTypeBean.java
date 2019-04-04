package cn.nineton.onetake.bean;

public class MscvImageTypeBean {
    private int clipArtType;
    private int lineDrawingType;

    public int getClipArtType() {
        return this.clipArtType;
    }

    public void setClipArtType(int clipArtType) {
        this.clipArtType = clipArtType;
    }

    public int getLineDrawingType() {
        return this.lineDrawingType;
    }

    public void setLineDrawingType(int lineDrawingType) {
        this.lineDrawingType = lineDrawingType;
    }

    public String toString() {
        return "MscvImageTypeBean{clipArtType=" + this.clipArtType + ", lineDrawingType=" + this.lineDrawingType + '}';
    }
}