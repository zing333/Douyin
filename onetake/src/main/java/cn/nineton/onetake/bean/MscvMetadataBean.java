package cn.nineton.onetake.bean;

public class MscvMetadataBean {
    private String format;
    private int height;
    private int width;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String toString() {
        return "MscvMetadataBean{width=" + this.width + ", height=" + this.height + ", format='" + this.format + '\'' + '}';
    }
}