package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MeasureSizeFrameLayout extends FrameLayout {
    private OnSizeChange onSizeChange;

    public interface OnSizeChange {
        void size(int i, int i2, int i3, int i4);
    }

    public MeasureSizeFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MeasureSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.onSizeChange != null) {
            this.onSizeChange.size(getMeasuredHeight(), getMeasuredWidth(), getLeft(), getRight());
        }
    }

    public void setOnSizeChangeListener(OnSizeChange onSizeChange) {
        this.onSizeChange = onSizeChange;
    }
}