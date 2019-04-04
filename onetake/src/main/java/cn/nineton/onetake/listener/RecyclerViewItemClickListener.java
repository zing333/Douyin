package cn.nineton.onetake.listener;

import android.view.View;

public interface RecyclerViewItemClickListener<T> {
    void onItemClickListener(T t, int i, View view);

    void onItemLongClickListener(T t, int i, View view);
}