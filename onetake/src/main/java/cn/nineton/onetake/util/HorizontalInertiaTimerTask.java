package cn.nineton.onetake.util;


import cn.nineton.onetake.widget.HorizontalLoopView;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public final class HorizontalInertiaTimerTask implements Runnable {
    private float a = 2.14748365E9f;
    private final HorizontalLoopView loopView;
    private final float velocityX;

    public HorizontalInertiaTimerTask(HorizontalLoopView loopview, float velocityX) {
        this.loopView = loopview;
        this.velocityX = velocityX;
    }

    public final void run() {
        if (this.a == 2.14748365E9f) {
            if (Math.abs(this.velocityX) <= 2000.0f) {
                this.a = this.velocityX;
            } else if (this.velocityX > StaticLayoutUtil.DefaultSpacingadd) {
                this.a = 2000.0f;
            } else {
                this.a = -2000.0f;
            }
        }
        if (Math.abs(this.a) < StaticLayoutUtil.DefaultSpacingadd || Math.abs(this.a) > 20.0f) {
            int i = (int) ((this.a * 10.0f) / 1000.0f);
            HorizontalLoopView loopview = this.loopView;
            loopview.totalScrollX -= i;
            if (!this.loopView.isLoop) {
                float itemHeight = this.loopView.lineSpacingMultiplier * ((float) this.loopView.maxItemWidth);
                if (this.loopView.totalScrollX <= ((int) (((float) (-this.loopView.initPosition)) * itemHeight))) {
                    this.a = 40.0f;
                    this.loopView.totalScrollX = (int) (((float) (-this.loopView.initPosition)) * itemHeight);
                } else if (this.loopView.totalScrollX >= ((int) (((float) ((this.loopView.items.size() - 1) - this.loopView.initPosition)) * itemHeight))) {
                    this.loopView.totalScrollX = (int) (((float) ((this.loopView.items.size() - 1) - this.loopView.initPosition)) * itemHeight);
                    this.a = -40.0f;
                }
            }
            if (this.a < StaticLayoutUtil.DefaultSpacingadd) {
                this.a += 20.0f;
            } else {
                this.a -= 20.0f;
            }
            this.loopView.mHandler.sendEmptyMessage(1000);
            return;
        }
        this.loopView.cancelFuture();
        this.loopView.mHandler.sendEmptyMessage(2000);
    }
}