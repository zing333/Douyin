package cn.nineton.onetake.widget;

import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;

import cn.nineton.onetake.listener.ITouchPresenter;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.WeakHandler;
//import com.blink.academy.onetake.custom.WeakHandler;
//import com.blink.academy.onetake.support.LongPressTouchListener;
//import com.blink.academy.onetake.support.LongPressTouchListener.CustomPressCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
//import com.blink.academy.onetake.ui.presenter.ITouchPresenter;
//import com.blink.academy.onetake.ui.view.TouchViewImpl$.Lambda.1;
//import com.blink.academy.onetake.ui.view.TouchViewImpl$.Lambda.2;

public class TouchViewImpl implements ITouchView, Callback {
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_LEFT = 3;
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_RIGHT = 4;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_LEFT = 1;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_RIGHT = 2;
    private static final String TAG = TouchViewImpl.class.getSimpleName();
    private int DELAY_TIME = 50;
    private float SLIDER_DELTA;
    private int curSelectAudioModelPosition;
    private long lastDealTime;
    private View leftTouchView;
    private WeakHandler mHandler;
    private float mLastDownMoveX;
    private LongPressTouchListener.CustomPressCallback mLeftImagePressTouchListener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            TouchViewImpl.this.mTouchPresenter.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            ViewParent itemViewParent = TouchViewImpl.this.leftTouchView.getParent();
            if (itemViewParent != null) {
                itemViewParent.requestDisallowInterceptTouchEvent(true);
                TouchViewImpl.this.mTouchPresenter.onStartSlideAudioLeft(TouchViewImpl.this.getCurSelectAudioModelPosition());
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            TouchViewImpl.this.mLastDownMoveX = (float) (TouchViewImpl.this.mScreenWidth / 2);
            TouchViewImpl.this.mHandler.removeMessages(3);
            TouchViewImpl.this.mHandler.removeMessages(4);
            ViewParent parent = TouchViewImpl.this.leftTouchView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
                LogUtil.d(TouchViewImpl.TAG, "filter_loop_slider_left_iv : ACTION_UP ACTION_CANCEL");
                TouchViewImpl.this.mTouchPresenter.onSlideAudioLeftEnd(TouchViewImpl.this.getCurSelectAudioModelPosition());
                TouchViewImpl.this.mTouchPresenter.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            TouchViewImpl.this.mLastDownMoveX = event.getRawX();
            if (TouchViewImpl.this.isInStartArea()) {
                TouchViewImpl.this.mHandler.sendEmptyMessageDelayed(4, (long) TouchViewImpl.this.DELAY_TIME);
            } else if (TouchViewImpl.this.isInEndArea()) {
                TouchViewImpl.this.mHandler.sendEmptyMessageDelayed(3, (long) TouchViewImpl.this.DELAY_TIME);
            } else {
                TouchViewImpl.this.mHandler.removeMessages(3);
                TouchViewImpl.this.mHandler.removeMessages(4);
                if (moveTime != 0) {
                    //LogUtil.d(TouchViewImpl.TAG, String.format("filter_loop_slider_right_iv2  moveTime : %s ", new Object[]{Integer.valueOf(moveTime)}));
                    TouchViewImpl.this.mTouchPresenter.onSlideAudioLeft(TouchViewImpl.this.getCurSelectAudioModelPosition(), moveTime);
                }
            }
        }
    };
    private LongPressTouchListener.CustomPressCallback mRightPressTouchListener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            TouchViewImpl.this.mTouchPresenter.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            if (TouchViewImpl.this.rightTouchView.getParent() != null) {
                TouchViewImpl.this.rightTouchView.getParent().requestDisallowInterceptTouchEvent(true);
                TouchViewImpl.this.mTouchPresenter.onStartSlideAudioRight(TouchViewImpl.this.getCurSelectAudioModelPosition());
                LogUtil.d(TouchViewImpl.TAG, String.format("dispatchLongPress onLongPress  ", new Object[0]));
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            LogUtil.d(TouchViewImpl.TAG, String.format("dispatchLongPress onActionUpOrCancel isLongPress : %s ", new Object[]{Boolean.valueOf(isLongPress)}));
            TouchViewImpl.this.mLastDownMoveX = (float) (TouchViewImpl.this.mScreenWidth / 2);
            TouchViewImpl.this.mHandler.removeMessages(1);
            TouchViewImpl.this.mHandler.removeMessages(2);
            if (TouchViewImpl.this.rightTouchView.getParent() != null) {
                TouchViewImpl.this.rightTouchView.getParent().requestDisallowInterceptTouchEvent(false);
                LogUtil.d(TouchViewImpl.TAG, "filter_loop_slider_right_iv : ACTION_UP ACTION_CANCEL");
                TouchViewImpl.this.mTouchPresenter.onSlideAudioRightEnd(TouchViewImpl.this.getCurSelectAudioModelPosition());
                TouchViewImpl.this.mTouchPresenter.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            float rawX = event.getRawX();
            LogUtil.d(TouchViewImpl.TAG, String.format("dispatchLongPress onActionMove moveTime : %s ", new Object[]{Integer.valueOf(moveTime)}));
            TouchViewImpl.this.mLastDownMoveX = rawX;
            if (TouchViewImpl.this.isInStartArea()) {
                TouchViewImpl.this.mHandler.sendEmptyMessageDelayed(2, (long) TouchViewImpl.this.DELAY_TIME);
            } else if (TouchViewImpl.this.isInEndArea()) {
                TouchViewImpl.this.mHandler.sendEmptyMessageDelayed(1, (long) TouchViewImpl.this.DELAY_TIME);
            } else {
                TouchViewImpl.this.mHandler.removeMessages(1);
                TouchViewImpl.this.mHandler.removeMessages(2);
                if (moveTime != 0) {
                    LogUtil.d(TouchViewImpl.TAG, String.format("filter_loop_slider_right_iv2  moveTime : %s ", new Object[]{Integer.valueOf(moveTime)}));
                    TouchViewImpl.this.mTouchPresenter.onSlideAudioRight(TouchViewImpl.this.getCurSelectAudioModelPosition(), moveTime);
                }
            }
        }
    };
    private int mScreenWidth;
    private ITouchPresenter mTouchPresenter;
    private View rightTouchView;

    public TouchViewImpl(@NonNull View leftTouchView, @NonNull View rightTouchView, @NonNull ITouchPresenter touchPresenter) {
        this.leftTouchView = leftTouchView;
        this.rightTouchView = rightTouchView;
        this.mTouchPresenter = touchPresenter;
        this.SLIDER_DELTA = ((float) VideoEditHelper.IMAGE_UNIT_WIDTH) * VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue();
        this.mScreenWidth = DensityUtil.getMetricsWidth(getActivity());
        leftTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("debug",getClass().getSimpleName()+":leftTouchView onclick");
            }
        });
        leftTouchView.setOnTouchListener(new LongPressTouchListener(getActivity(), this.SLIDER_DELTA, this.mLeftImagePressTouchListener));
        rightTouchView.setOnTouchListener(new LongPressTouchListener(getActivity(), this.SLIDER_DELTA, this.mRightPressTouchListener));
        rightTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("debug",getClass().getSimpleName()+":rightTouchView onclick");
            }
        });
        this.mHandler = new WeakHandler((Callback) this);
    }

    private Activity getActivity() {
        return this.mTouchPresenter.getActivity();
    }

    public void setCurSelectAudioModelPosition(int curSelectAudioModelPosition) {
        this.curSelectAudioModelPosition = curSelectAudioModelPosition;
    }

    private int getCurSelectAudioModelPosition() {
        return this.curSelectAudioModelPosition;
    }

    private boolean isInStartArea() {
        return this.mLastDownMoveX < this.SLIDER_DELTA * ((float) VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
    }

    private boolean isInEndArea() {
        return this.mLastDownMoveX > ((float) this.mScreenWidth) - (this.SLIDER_DELTA * ((float) VideoEditHelper.EXPANSION_OF_AUTO_SCROLL));
    }

    public void refreshLeftViewPos(int leftMargin) {
        if (leftMargin > 0 && leftMargin < this.mScreenWidth) {
            LayoutParams layoutParams = this.leftTouchView.getLayoutParams();
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
                if (params.leftMargin != leftMargin) {
                    params.leftMargin = leftMargin;
                    this.leftTouchView.setLayoutParams(params);
                }
            }
            if (this.leftTouchView.getVisibility() != View.VISIBLE) {
                this.leftTouchView.setVisibility(View.VISIBLE);
            }
        } else if (this.leftTouchView.getVisibility() == View.VISIBLE) {
            this.leftTouchView.setVisibility(View.GONE);
        }
    }

    public void refreshRightViewPos(int leftMargin) {
        if (leftMargin > 0 && leftMargin < this.mScreenWidth) {
            LayoutParams layoutParams = this.rightTouchView.getLayoutParams();
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
                if (params.leftMargin != leftMargin) {
                    params.leftMargin = leftMargin;
                    this.rightTouchView.setLayoutParams(params);
                }
            }
            if (this.rightTouchView.getVisibility() != View.VISIBLE) {
                this.rightTouchView.setVisibility(View.VISIBLE);
            }
        } else if (this.rightTouchView.getVisibility() == View.VISIBLE) {
            this.rightTouchView.setVisibility(View.GONE);
        }
    }

    public void hideTouchView() {
        if (this.leftTouchView.getVisibility() == View.VISIBLE) {
            this.leftTouchView.setVisibility(View.GONE);
        }
        if (this.rightTouchView.getVisibility() == View.VISIBLE) {
            this.rightTouchView.setVisibility(View.GONE);
        }
    }

    public boolean handleMessage(Message msg) {
        long currentTimeMillis;
        switch (msg.what) {
            case 1:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        LogUtil.d(TAG, String.format("MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_LEFT", new Object[0]));
                        this.mTouchPresenter.onSlideAudioRightAutoScrollToLeft(getCurSelectAudioModelPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(1, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 2:
                if (isInStartArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        LogUtil.d(TAG, String.format("MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_RIGHT", new Object[0]));
                        this.mTouchPresenter.onSlideAudioRightAutoScrollToRight(getCurSelectAudioModelPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(2, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 3:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        LogUtil.d(TAG, String.format("MSG_SLIDE_LEFT_AUTO_SCROLL_TO_LEFT", new Object[0]));
                        this.mTouchPresenter.onSlideAudioLeftAutoScrollToLeft(getCurSelectAudioModelPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(3, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 4:
                if (isInStartArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        LogUtil.d(TAG, String.format("MSG_SLIDE_LEFT_AUTO_SCROLL_TO_RIGHT", new Object[0]));
                        this.mTouchPresenter.onSlideAudioLeftAutoScrollToRight(getCurSelectAudioModelPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(4, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
        }
        return true;
    }
}