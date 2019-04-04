package cn.nineton.onetake.event;

public class ChangeStatusBarColorEvent {
    public static final int COLOR_INVALID_VALUE = -1;
    private int mColor;

    public ChangeStatusBarColorEvent(int color) {
        this.mColor = color;
    }

    public int getColor() {
        return this.mColor;
    }
}