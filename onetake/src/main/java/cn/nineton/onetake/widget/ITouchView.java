package cn.nineton.onetake.widget;

public interface ITouchView {
    void hideTouchView();

    void refreshLeftViewPos(int i);

    void refreshRightViewPos(int i);

    void setCurSelectAudioModelPosition(int i);
}