package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.BaseEntity;
import cn.nineton.onetake.util.DensityUtil;
//
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.ui.adapter.entities.BaseEntity;
//import com.blink.academy.onetake.ui.adapter.holder.ABRecyclerViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends BaseAdapter<ABRecyclerViewHolder> {
    protected List<T> list = null;
    private Activity mActivity;
    protected OnItemClickListener onItemClickListener;

    public class FooterViewHolder extends ABRecyclerViewHolder {
        BaseEntity baseEntity;
        boolean isDiscover = false;
        @BindView(R.id.loading_layout_rl)
        View loading_layout_rl;
        @BindView(R.id.loading_pb)
        ProgressBar loading_pb;

        public FooterViewHolder(View itemView, boolean isDiscover) {
            super(itemView);
            this.isDiscover = isDiscover;
            ButterKnife.bind(this, itemView);
        }

        public void onBindViewHolder(int position) {
            this.baseEntity = (BaseEntity) BaseRecyclerAdapter.this.getCards().get(position);
            switch (this.baseEntity.getState()) {
                case Loading:
                    if (this.isDiscover) {
                        this.loading_layout_rl.getLayoutParams().height = DensityUtil.dip2px(40.0f);
                    }
                    this.loading_pb.setVisibility(View.VISIBLE);
                    return;
                case Idle:
                case TheEnd:
                    if (this.isDiscover) {
                        this.loading_layout_rl.getLayoutParams().height = DensityUtil.dip2px(40.0f);
                    }
                    this.loading_pb.setVisibility(View.GONE);
                    return;
                case TheOver:
                    if (this.isDiscover) {
                        this.loading_layout_rl.getLayoutParams().height = 0;
                    }
                    this.loading_pb.setVisibility(View.GONE);
                    return;
                default:
                    return;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    protected abstract void onBindItemView(ABRecyclerViewHolder aBRecyclerViewHolder, int i);

    public abstract ABRecyclerViewHolder onCreateHolder(ViewGroup viewGroup, int i);

    public BaseRecyclerAdapter(Activity mActivity, List<T> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

    public List<T> getCards() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public ABRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    public void onBindViewHolder(ABRecyclerViewHolder holder, int position) {
        onBindItemView(holder, position);
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void removeAll() {
        if (getCards() != null) {
            getCards().removeAll(getCards());
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> list) {
        if (getCards() != null) {
            getCards().addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removeItem(T item) {
        if (getCards() != null) {
            getCards().remove(item);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (getCards() != null && position < getItemCount()) {
            getCards().remove(position);
            notifyDataSetChanged();
        }
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setRetryLocListener(OnClickListener retryClickListener) {
    }
}