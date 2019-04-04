package cn.nineton.onetake.listener;

import android.app.Activity;

import cn.nineton.onetake.bean.LongVideosModel;

public interface ITouchPresenter {
    Activity getActivity();

    void hideTouchView();

    void onHideSlideText();

    void onShowSlideText();

    void onSlideAudioLeft(int i, int i2);

    void onSlideAudioLeftAutoScrollToLeft(int i);

    void onSlideAudioLeftAutoScrollToRight(int i);

    void onSlideAudioLeftEnd(int i);

    void onSlideAudioRight(int i, int i2);

    void onSlideAudioRightAutoScrollToLeft(int i);

    void onSlideAudioRightAutoScrollToRight(int i);

    void onSlideAudioRightEnd(int i);

    void onStartSlideAudioLeft(int i);

    void onStartSlideAudioRight(int i);

    void refreshTouchViewPos(LongVideosModel longVideosModel, int i, int i2, float f, int i3, int i4);
}