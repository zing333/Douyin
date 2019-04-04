package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class RecyclerLoadingView extends RecyclerView {
    public RecyclerLoadingView(Context context) {
        super(context);
    }

    public RecyclerLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerLoadingView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}