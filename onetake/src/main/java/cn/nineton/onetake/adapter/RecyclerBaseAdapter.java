package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.blink.academy.onetake.ui.adapter.holder.ABRecyclerViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.FooterViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.HeaderViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter;
//import com.blink.academy.onetake.widgets.loading.LoadingFooterView;
import java.util.List;

import cn.nineton.onetake.util.FooterViewHolder;
import cn.nineton.onetake.util.HeaderViewHolder;
import cn.nineton.onetake.widget.LoadingFooterView;

public abstract class RecyclerBaseAdapter<T> extends BaseAdapter<ABRecyclerViewHolder> {
    protected LoadingFooterView footerView;
    protected boolean hasFooter = false;
    protected boolean hasHeader = false;
    protected View headerView;
    protected List<T> list = null;
    private Activity mActivity;
    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    protected abstract void onBindFooterView(View view);

    protected abstract void onBindHeaderView(View view);

    protected abstract void onBindItemView(ABRecyclerViewHolder aBRecyclerViewHolder, T t, int i);

    public abstract ABRecyclerViewHolder onCreateHolder(ViewGroup viewGroup, int i);

    public RecyclerBaseAdapter(Activity mActivity, List<T> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isFooter(int position) {
        if (hasHeader()) {
            if (hasFooter() && position == this.list.size() + 1) {
                return true;
            }
            return false;
        } else if (hasFooter() && position == this.list.size()) {
            return true;
        } else {
            return false;
        }
    }

    public View getHeaderView() {
        return this.headerView;
    }

    public View getFooterView() {
        return this.footerView.getLoadingFooterView();
    }

    public void setHeaderView(View headerView) {
        if (headerView != null) {
            this.hasHeader = true;
            this.headerView = headerView;
        }
    }

    public void setFooterView(LoadingFooterView footerView) {
        if (footerView != null) {
            this.hasFooter = true;
            this.footerView = footerView;
        }
    }

    public boolean hasHeader() {
        return this.hasHeader;
    }

    public boolean hasFooter() {
        return this.hasFooter;
    }

    public int getHeaderCount() {
        return hasHeader() ? 1 : 0;
    }

    public int getFooterCount() {
        return hasFooter() ? 1 : 0;
    }

    public ABRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (hasHeader() && viewType == 10) {
            return new HeaderViewHolder(getHeaderView());
        }
        if (hasFooter() && viewType == 11) {
            return new FooterViewHolder(getFooterView());
        }
        return onCreateHolder(parent, viewType);
    }

    public void onBindViewHolder(ABRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == 10) {
            onBindHeaderView(holder.itemView);
        } else if (getItemViewType(position) == 11) {
            onBindFooterView(holder.itemView);
        } else {
            onBindItemView(holder, getItemByPosition(position), getPosition(position));
        }
    }

    protected T getItemByPosition(int position) {
        return this.list.get(getPosition(position));
    }

    protected int getPosition(int position) {
        if (hasHeader()) {
            return position - 1;
        }
        return position;
    }

    public int getItemCount() {
        int i;
        int i2 = 1;
        if (hasHeader()) {
            i = 1;
        } else {
            i = 0;
        }
        int count = 0 + i;
        if (!hasFooter()) {
            i2 = 0;
        }
        return (count + i2) + this.list.size();
    }

    public int getItemViewType(int position) {
        int size = getItemCount();
        if (hasHeader() && position == 0) {
            return 10;
        }
        if (hasFooter() && position == size - 1) {
            return 11;
        }
        return 2;
    }

    public void removeAll() {
        if (getList() != null) {
            getList().removeAll(getList());
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> list) {
        if (getList() != null) {
            getList().addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removeItem(T item) {
        if (getList() != null) {
            getList().remove(item);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (getList() != null && getHeaderCount() <= position && position < getItemCount()) {
            getList().remove(position);
            notifyDataSetChanged();
        }
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    protected View inflateView(int resId, ViewGroup root) {
        return LayoutInflater.from(getActivity()).inflate(resId, root, false);
    }
}