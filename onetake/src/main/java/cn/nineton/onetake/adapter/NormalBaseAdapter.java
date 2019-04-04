package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class NormalBaseAdapter<VH extends ViewHolder> extends BaseAdapter<VH> {
    Context mContext;
    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public NormalBaseAdapter(Context context) {
        this.mContext = context;
    }

    protected View getResView(int resId, ViewGroup parent) {
        return LayoutInflater.from(this.mContext).inflate(resId, parent, false);
    }

    protected Context getContext() {
        return this.mContext;
    }

    protected Activity getActivity() {
        if (this.mContext instanceof Activity) {
            return (Activity) this.mContext;
        }
        return null;
    }

    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NormalBaseAdapter.this.onItemClickListener != null) {
                    NormalBaseAdapter.this.onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}