package cn.nineton.onetake.listener;

public interface VideoEditModuleListener {
    void addChildView();

    void animateRestoreAfterSlideLeft(int i, int i2);

    void initMaxWidthAndHeight();

    void needMeasureWidth();

    void removeChildView();

    void updateViewWhenSlideLeft(int i, float f, int i2, boolean z);

    void updateViewWhenSlideRight(int i, float f);
}