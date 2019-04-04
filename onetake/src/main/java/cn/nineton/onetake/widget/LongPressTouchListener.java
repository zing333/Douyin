package cn.nineton.onetake.widget;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
//import com.blink.academy.onetake.custom.WeakHandler;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import java.math.BigDecimal;

import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.WeakHandler;

public class LongPressTouchListener implements OnTouchListener {
    private static final int LONGPRESS_TIMEOUT = 100;
    private static final int LONG_PRESS = 2;
    private static final String TAG = LongPressTouchListener.class.getSimpleName();
    private float SLIDE_UNIT;
    float down_x;
    float down_y;
    float last_move_x;
    private MotionEvent mCurrentDownEvent;
    private float mDownFocusX;
    private float mDownFocusY;
    WeakHandler mHandler;
    private boolean mInLongPress;
    private float mLastFocusX;
    private float mLastFocusY;
    PressCallback mPressCallback;
    private int mTouchSlopSquare;

    public interface PressCallback {
        boolean needLongPress();

        void onActionDown();

        void onActionMove(MotionEvent motionEvent, int i);

        void onActionUpOrCancel(MotionEvent motionEvent, boolean z);

        void onLongPress(MotionEvent motionEvent);
    }

    public static class CustomPressCallback implements PressCallback {
        public void onActionDown() {
        }

        public void onLongPress(MotionEvent event) {
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
        }

        public void onActionMove(MotionEvent event, int time) {
        }

        public boolean needLongPress() {
            return true;
        }
    }

    public void setPressCallback(PressCallback callback) {
        this.mPressCallback = callback;
    }

    private void dispatchLongPress() {
        boolean z = false;
        if (this.mPressCallback == null || this.mPressCallback.needLongPress()) {
            this.mInLongPress = true;
            String str = TAG;
            String str2 = "dispatchLongPress mInLongPress : %s  mPressCallback != null : %s ";
            Object[] objArr = new Object[2];
            objArr[0] = Boolean.valueOf(this.mInLongPress);
            if (this.mPressCallback != null) {
                z = true;
            }
            objArr[1] = Boolean.valueOf(z);
            LogUtil.d(str, String.format(str2, objArr));
            if (this.mPressCallback != null) {
                this.mPressCallback.onLongPress(this.mCurrentDownEvent);
            }
        }
    }

    public LongPressTouchListener(Context context, float slide_unit) {
        this.mHandler = new WeakHandler(new Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 2:
                        LongPressTouchListener.this.dispatchLongPress();
                        break;
                }
                return false;
            }
        });
        this.SLIDE_UNIT = slide_unit;
        int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mTouchSlopSquare = touchSlop * touchSlop;
    }

    public LongPressTouchListener(Context context, float slide_unit, PressCallback callback) {
        this(context, slide_unit);
        setPressCallback(callback);
    }

    public boolean onTouch(View v, MotionEvent event) {
        int div;
        boolean pointerUp = (event.getAction() & 255) == 6;
        int skipIndex = pointerUp ? event.getActionIndex() : -1;
        float sumX = StaticLayoutUtil.DefaultSpacingadd;
        float sumY = StaticLayoutUtil.DefaultSpacingadd;
        int count = event.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex != i) {
                sumX += event.getX(i);
                sumY += event.getY(i);
            }
        }
        if (pointerUp) {
            div = count - 1;
        } else {
            div = count;
        }
        float focusX = sumX / ((float) div);
        float focusY = sumY / ((float) div);
        switch (event.getActionMasked()) {
            case 0:
                this.down_x = event.getRawX();
                this.down_y = event.getRawY();
                this.last_move_x = StaticLayoutUtil.DefaultSpacingadd;
                this.mLastFocusX = focusX;
                this.mDownFocusX = focusX;
                this.mLastFocusY = focusY;
                this.mDownFocusY = focusY;
                if (this.mCurrentDownEvent != null) {
                    this.mCurrentDownEvent.recycle();
                }
                this.mCurrentDownEvent = MotionEvent.obtain(event);
                this.mInLongPress = false;
                this.mHandler.removeMessages(2);
                this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + 100);
                if (this.mPressCallback != null) {
                    this.mPressCallback.onActionDown();
                    break;
                }
                break;
            case 1:
            case 3:
                this.mHandler.removeMessages(2);
                if (this.mPressCallback != null) {
                    this.mPressCallback.onActionUpOrCancel(event, this.mInLongPress);
                }
                if (this.mInLongPress) {
                    this.mInLongPress = false;
                    return true;
                }
                break;
            case 2:
                float move_y = event.getRawY() - this.down_y;
                float move_x = dealMoveX(event.getRawX() - this.down_x);
                float result_move_x = BigDecimal.valueOf((double) move_x).subtract(BigDecimal.valueOf((double) this.last_move_x)).setScale(1, 4).floatValue();
                this.last_move_x = move_x;
                int moveTime = (int) (result_move_x / this.SLIDE_UNIT);
                if (moveTime != 0) {
                }
                float scrollX = this.mLastFocusX - focusX;
                float scrollY = this.mLastFocusY - focusY;
                if (!this.mInLongPress) {
                    int deltaX = (int) (focusX - this.mDownFocusX);
                    int deltaY = (int) (focusY - this.mDownFocusY);
                    if ((deltaX * deltaX) + (deltaY * deltaY) > this.mTouchSlopSquare) {
                        this.mLastFocusX = focusX;
                        this.mLastFocusY = focusY;
                        this.mHandler.removeMessages(2);
                        return false;
                    }
                } else if (this.mInLongPress) {
                    if (this.mPressCallback != null) {
                        this.mPressCallback.onActionMove(event, moveTime);
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    private float dealMoveX(float move_x) {
        if (this.SLIDE_UNIT == StaticLayoutUtil.DefaultSpacingadd) {
            return move_x;
        }
        return this.SLIDE_UNIT * ((float) ((int) (move_x / this.SLIDE_UNIT)));
    }
}