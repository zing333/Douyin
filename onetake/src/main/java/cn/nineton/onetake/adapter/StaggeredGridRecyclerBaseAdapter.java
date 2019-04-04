package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup.LayoutParams;
import java.util.List;

public abstract class StaggeredGridRecyclerBaseAdapter<T> extends RecyclerBaseAdapter<T> {

    public interface StaggeredGridLayoutFooterItem {
    }

    public interface StaggeredGridLayoutHeaderItem {
    }

    public StaggeredGridRecyclerBaseAdapter(Activity mActivity, List<T> list) {
        super(mActivity, list);
    }

    public void onViewAttachedToWindow(ABRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    private boolean isStaggeredGridLayout(ViewHolder holder) {
        LayoutParams layoutParams = holder.itemView.getLayoutParams();
        return ((holder instanceof StaggeredGridLayoutHeaderItem) || (holder instanceof StaggeredGridLayoutFooterItem)) && layoutParams != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams);
    }

    protected void handleLayoutIfStaggeredGridLayout(ViewHolder holder, int position) {
        if (isHeader(position) || isFooter(position)) {
            ((StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams()).setFullSpan(true);
        }
    }
}