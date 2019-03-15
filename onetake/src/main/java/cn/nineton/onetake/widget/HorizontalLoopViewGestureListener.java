package cn.nineton.onetake.widget;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

final class HorizontalLoopViewGestureListener extends SimpleOnGestureListener {
    private final HorizontalLoopView loopView;

    HorizontalLoopViewGestureListener(HorizontalLoopView loopview) {
        this.loopView = loopview;
    }

    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        this.loopView.scrollBy(velocityX);
        return true;
    }
}