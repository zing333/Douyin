package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.nineton.onetake.util.LogUtil;

public class ChangeChildDrawOrderView extends LinearLayout {
    private int topPosition;

    public ChangeChildDrawOrderView(Context context) {
        this(context, null);
    }

    public ChangeChildDrawOrderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeChildDrawOrderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.topPosition = 1;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setChildrenDrawingOrderEnabled(true);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int result;
        if (i == childCount - 1) {
            result = this.topPosition;
        } else if (i >= this.topPosition) {
            result = i + 1;
        } else {
            result = i;
        }
        LogUtil.d("ChangeChildDrawOrderView", String.format("getChildDrawingOrder  childCount : %s , i : %s  , topPosition : %s  , result : %s  ", new Object[]{Integer.valueOf(childCount), Integer.valueOf(i), Integer.valueOf(this.topPosition), Integer.valueOf(result)}));
        return result;
    }

    public int getTopPosition() {
        return this.topPosition;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }
}