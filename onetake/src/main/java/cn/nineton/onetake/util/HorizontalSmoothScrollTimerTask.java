package cn.nineton.onetake.util;


import cn.nineton.onetake.widget.HorizontalLoopView;

public final class HorizontalSmoothScrollTimerTask implements Runnable {
    private final HorizontalLoopView loopView;
    private int offset;
    private int realOffset = 0;
    private int realTotalOffset = AppMessage.PRIORITY_HIGH;

    public HorizontalSmoothScrollTimerTask(HorizontalLoopView loopview, int offset) {
        this.loopView = loopview;
        this.offset = offset;
    }

    public final void run() {
        if (this.realTotalOffset == AppMessage.PRIORITY_HIGH) {
            this.realTotalOffset = this.offset;
        }
        this.realOffset = (int) (((float) this.realTotalOffset) * 0.1f);
        if (this.realOffset == 0) {
            if (this.realTotalOffset < 0) {
                this.realOffset = -1;
            } else {
                this.realOffset = 1;
            }
        }
        if (Math.abs(this.realTotalOffset) <= 0) {
            this.loopView.cancelFuture();
            this.loopView.mHandler.sendEmptyMessage(3000);
            return;
        }
        this.loopView.totalScrollX += this.realOffset;
        this.loopView.mHandler.sendEmptyMessage(1000);
        this.realTotalOffset -= this.realOffset;
    }
}