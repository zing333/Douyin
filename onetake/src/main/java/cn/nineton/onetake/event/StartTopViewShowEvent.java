package cn.nineton.onetake.event;

public class StartTopViewShowEvent {
    public int color;
    public String message;

    public StartTopViewShowEvent(String message, int color) {
        this.message = message;
        this.color = color;
    }
}