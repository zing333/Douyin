package cn.nineton.onetake.listener;

public interface ValueAnimatorCallback {
    void endCallback();

    void startCallback();

    void updateCallback(float f);
}