package cn.nineton.onetake.bean;

public class MscvFaceRectangleBean {
    private int height;
    private int left;
    private int top;
    private int width;

    public int getLeft() {
        return this.left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }

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

    public String toString() {
        return "MscvFaceRectangleBean{left=" + this.left + ", top=" + this.top + ", width=" + this.width + ", height=" + this.height + '}';
    }
}