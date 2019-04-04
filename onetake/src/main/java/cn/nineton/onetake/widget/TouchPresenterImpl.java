package cn.nineton.onetake.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.listener.ITouchPresenter;
import cn.nineton.onetake.util.DensityUtil;

public class TouchPresenterImpl implements ITouchPresenter {
    public static int INVALIDATEPOSITION = -1;
    private VideoEditHelper.SlideVideoAreaCallback mSlideVideoAreaCallback;
    private ITouchView mTouchView;

    public TouchPresenterImpl(@NonNull View leftTouchView, @NonNull View rightTouchView, @NonNull VideoEditHelper.SlideVideoAreaCallback slideVideoAreaCallback) {
        this.mSlideVideoAreaCallback = slideVideoAreaCallback;
        this.mTouchView = new TouchViewImpl(leftTouchView, rightTouchView, this);
    }

    public void refreshTouchViewPos(LongVideosModel longVideosModel, int curScrollX, int sumWidth, float sumTime, int screenWidth, int curSelectAudioModelPosition) {
        if (longVideosModel != null) {
            if (curSelectAudioModelPosition != INVALIDATEPOSITION) {
                this.mTouchView.setCurSelectAudioModelPosition(curSelectAudioModelPosition);
            }
            long audioStartTime = longVideosModel.getAudioStartTime();
            int startX = (int) (((((float) audioStartTime) * 1.0f) / sumTime) * ((float) sumWidth));
            int endX = (int) (((((float) (audioStartTime + longVideosModel.getAudioDuration())) * 1.0f) / sumTime) * ((float) sumWidth));
            int dp20 = DensityUtil.dip2px(20.0f);
            this.mTouchView.refreshLeftViewPos(((screenWidth / 2) - (curScrollX - startX)) - dp20);
            this.mTouchView.refreshRightViewPos(((screenWidth / 2) - (curScrollX - endX)) - dp20);
        }
    }

    public void hideTouchView() {
        this.mTouchView.hideTouchView();
    }

    public Activity getActivity() {
        return this.mSlideVideoAreaCallback.getActivity();
    }

    public void onShowSlideText() {
        this.mSlideVideoAreaCallback.onShowSlideText();
    }

    public void onHideSlideText() {
        this.mSlideVideoAreaCallback.onHideSlideText();
    }

    public void onStartSlideAudioRight(int position) {
        this.mSlideVideoAreaCallback.onStartSlideAudioRight(position);
    }

    public void onSlideAudioRight(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideAudioRight(position, time);
    }

    public void onSlideAudioRightEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightEnd(position);
    }

    public void onStartSlideAudioLeft(int position) {
        this.mSlideVideoAreaCallback.onStartSlideAudioLeft(position);
    }

    public void onSlideAudioLeft(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideAudioLeft(position, time);
    }

    public void onSlideAudioLeftEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftEnd(position);
    }

    public void onSlideAudioRightAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightAutoScrollToLeft(position);
    }

    public void onSlideAudioRightAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightAutoScrollToRight(position);
    }

    public void onSlideAudioLeftAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftAutoScrollToLeft(position);
    }

    public void onSlideAudioLeftAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftAutoScrollToRight(position);
    }
}