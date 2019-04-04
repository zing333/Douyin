package cn.nineton.onetake.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import cn.nineton.onetake.util.LogUtil;

public abstract class ABRecyclerViewHolder extends ViewHolder {
    public abstract void onBindViewHolder(int i);

    public ABRecyclerViewHolder(View itemView) {
        super(itemView);
        LogUtil.d("Adapter", "Class Name:" + getClass().getSimpleName() + ", (itemView.getParent() != null):" + (itemView.getParent() != null));
    }

    public void onResume() {
    }

    public void onStop() {
    }
}