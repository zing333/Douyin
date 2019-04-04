package cn.nineton.onetake.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import cn.nineton.onetake.util.DensityUtil;

public class VideoEditOrderItemDecoration extends ItemDecoration {
    private int smallOffset = DensityUtil.dip2px(5.0f);

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = this.smallOffset;
        }
    }
}