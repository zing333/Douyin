package cn.nineton.onetake.util;

import android.view.View;

import cn.nineton.onetake.adapter.ABRecyclerViewHolder;
import cn.nineton.onetake.adapter.StaggeredGridRecyclerBaseAdapter;

public class HeaderViewHolder extends ABRecyclerViewHolder implements StaggeredGridRecyclerBaseAdapter.StaggeredGridLayoutHeaderItem {
    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    public void onBindViewHolder(int position) {
    }
}