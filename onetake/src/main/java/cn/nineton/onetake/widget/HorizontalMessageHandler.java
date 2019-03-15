package cn.nineton.onetake.widget;

import android.os.Handler;
import android.os.Message;

final class HorizontalMessageHandler extends Handler {
    static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    static final int WHAT_ITEM_SELECTED = 3000;
    static final int WHAT_SMOOTH_SCROLL = 2000;
    private final HorizontalLoopView loopview;

    HorizontalMessageHandler(HorizontalLoopView loopview) {
        this.loopview = loopview;
    }

    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case 1000:
                this.loopview.invalidate();
                return;
            case 2000:
                this.loopview.smoothScroll(HorizontalLoopView.ACTION.FLING);
                return;
            case WHAT_ITEM_SELECTED /*3000*/:
                this.loopview.onItemSelected();
                return;
            default:
                return;
        }
    }
}