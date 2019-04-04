package cn.nineton.onetake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.SpecificCombination;
import cn.nineton.onetake.listener.RecyclerViewItemClickListener;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.LocaleUtil;

//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.combination.SpecificCombination;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FontsUtil;
//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.ui.adapter.callback.RecyclerViewItemClickListener;
import java.util.ArrayList;

public class SpecificCombinationAdapter extends Adapter<ViewHolder> {
    private Context mContext;
    private ArrayList<SpecificCombination> mDatas;
    private RecyclerViewItemClickListener<SpecificCombination> mListener;
    private int mSelected = -1;

    public class SpecificCombinationViewHolder extends ViewHolder {
        @BindView(R.id.specific_combination_name_tv)
        TextView specific_combination_name_tv;
        @BindView(R.id.specific_combination_point_iv)
        ImageView specific_combination_point_iv;

        SpecificCombinationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindViewHolder(SpecificCombination bean, int position) {
            String value;
            LayoutParams layoutParams = (LayoutParams) this.specific_combination_name_tv.getLayoutParams();
            if (position == SpecificCombinationAdapter.this.getItemCount() - 1) {
                this.specific_combination_point_iv.setVisibility(View.VISIBLE);
                layoutParams.leftMargin = DensityUtil.dip2px(19.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(30.0f);
            } else if (position == SpecificCombinationAdapter.this.getItemCount() - 2) {
                this.specific_combination_point_iv.setVisibility(View.GONE);
                layoutParams.leftMargin = DensityUtil.dip2px(10.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(19.0f);
            } else if (position == 0) {
                this.specific_combination_point_iv.setVisibility(View.GONE);
                layoutParams.leftMargin = DensityUtil.dip2px(30.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(10.0f);
            } else {
                this.specific_combination_point_iv.setVisibility(View.GONE);
                layoutParams.leftMargin = DensityUtil.dip2px(10.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(10.0f);
            }
            if (LocaleUtil.isChinese()) {
                value = bean.getName_chs();
            } else {
                value = bean.getName_en();
            }
            this.specific_combination_name_tv.setText(value);
            if (position != SpecificCombinationAdapter.this.mSelected || position == SpecificCombinationAdapter.this.getItemCount() - 1) {
                this.specific_combination_name_tv.setAlpha(0.4f);
                this.specific_combination_name_tv.setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
                return;
            }
            this.specific_combination_name_tv.setAlpha(1.0f);
            this.specific_combination_name_tv.setTypeface(FontsUtil.setAvenirNextCondensedMediumTypeFace());
        }

        void setNameTvSelected() {
            this.specific_combination_name_tv.setAlpha(1.0f);
            this.specific_combination_name_tv.setTypeface(FontsUtil.setAvenirNextCondensedMediumTypeFace());
        }

        void cancelNameTvSelected() {
            this.specific_combination_name_tv.setAlpha(0.4f);
            this.specific_combination_name_tv.setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
        }
    }

    public void setListener(RecyclerViewItemClickListener<SpecificCombination> listener) {
        this.mListener = listener;
    }

    public int getSelected() {
        return this.mSelected;
    }

    public void setSelected(int selected) {
        this.mSelected = selected;
    }

    public SpecificCombinationAdapter(ArrayList<SpecificCombination> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpecificCombinationViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.layout_item_specific_combination, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SpecificCombination bean = (SpecificCombination) this.mDatas.get(position);
        holder.itemView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & 255) {
                    case 0:
                        ((SpecificCombinationViewHolder) holder).setNameTvSelected();
                        break;
                    case 3:
                        App.runOnUiThread(new Runnable() {
                            public void run() {
                                SpecificCombinationAdapter.this.notifyDataSetChanged();
                            }
                        });
                        break;
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                SpecificCombinationAdapter.this.setSelected(adapterPosition);
                SpecificCombinationAdapter.this.notifyDataSetChanged();
                if (SpecificCombinationAdapter.this.mListener != null) {
                    SpecificCombinationAdapter.this.mListener.onItemClickListener(bean, adapterPosition, holder.itemView);
                }
            }
        });
        ((SpecificCombinationViewHolder) holder).onBindViewHolder(bean, position);
    }

    public int getItemCount() {
        return this.mDatas == null ? 0 : this.mDatas.size();
    }
}