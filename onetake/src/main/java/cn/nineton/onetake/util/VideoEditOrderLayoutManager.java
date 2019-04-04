package cn.nineton.onetake.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class VideoEditOrderLayoutManager extends LinearLayoutManager {
    private final int borderWidth = (this.singleBorder * 2);
    private final int itemDecoration;
    private final int itemWidth;
    private int numItems;
    private final int parentWidth;
    private final int singleBorder = DensityUtil.dip2px(20.0f);

    public VideoEditOrderLayoutManager(Context context, int orientation, boolean reverseLayout, int parentWidth, int itemWidth, int numItems, int itemDecoration) {
        super(context, orientation, reverseLayout);
        this.parentWidth = parentWidth;
        this.itemWidth = itemWidth;
        this.numItems = numItems;
        this.itemDecoration = itemDecoration;
    }

    public void updateChange(int numItems) {
        this.numItems = numItems;
    }

    public int getPaddingLeft() {
        int totalItemWidth = (this.itemWidth * this.numItems) + (this.itemDecoration * (this.numItems - 1));
        if (totalItemWidth >= this.parentWidth - this.borderWidth) {
            return this.singleBorder;
        }
        return Math.round((((float) this.parentWidth) / 2.0f) - (((float) totalItemWidth) / 2.0f));
    }

    public int getPaddingRight() {
        return getPaddingLeft();
    }
}