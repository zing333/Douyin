package cn.nineton.onetake.util;

import cn.nineton.onetake.widget.HorizontalLoopView;

public final class HorizontalOnItemSelectedRunnable implements Runnable {
    private final HorizontalLoopView loopView;

    public HorizontalOnItemSelectedRunnable(HorizontalLoopView loopview) {
        this.loopView = loopview;
    }

    public final void run() {
        this.loopView.onItemSelectedListener.onItemSelected(this.loopView.getSelectedItem());
    }
}