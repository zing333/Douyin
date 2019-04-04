package cn.nineton.onetake.util;

import android.view.View;

import cn.nineton.onetake.adapter.ABRecyclerViewHolder;
import cn.nineton.onetake.adapter.StaggeredGridRecyclerBaseAdapter;

public class FooterViewHolder extends ABRecyclerViewHolder implements StaggeredGridRecyclerBaseAdapter.StaggeredGridLayoutFooterItem {
    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    public void onBindViewHolder(int position) {
    }
}