package cn.nineton.onetake.media.interfaces;

public interface LongVideoPlayCallback {
    void couldRenderPreview();

    void couldUseRotateButton(boolean z);

    void finishInitPlayer();

    void playlistPercent(double d, double d2, boolean z, long j);

    void playlistPercentForLocal(double d);

    void startDrawFrame();
}