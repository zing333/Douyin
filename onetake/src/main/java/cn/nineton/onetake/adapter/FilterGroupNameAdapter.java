package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
//import com.blink.academy.onetake.bean.FilterInfo;
//import com.blink.academy.onetake.support.interfaces.FilterGroupOnclickListener;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FontsUtil;
//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil;
import java.util.List;

import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.FilterInfo;
import cn.nineton.onetake.listener.FilterGroupOnclickListener;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.WaterMarkBitmapUtil;

public class FilterGroupNameAdapter extends Adapter<ViewHolder> {
    private Activity context;
    private List<FilterInfo> filterGroupNameList;
    private FilterGroupOnclickListener mOnClickListener;
    private int selectPosition = -1;
    private Rect tempRect;

    public class GroupNameViewHolder extends ViewHolder {
        View filter_group_name_bottom_point;
        LinearLayout filter_group_name_rl;
        TextView filter_group_name_tv;
        ImageView filter_group_point_iv;

        public GroupNameViewHolder(View itemView) {
            super(itemView);
            this.filter_group_name_tv = (TextView) itemView.findViewById(R.id.filter_group_name_tv);
            this.filter_group_name_rl = (LinearLayout) itemView.findViewById(R.id.filter_group_name_rl);
            this.filter_group_point_iv = (ImageView) itemView.findViewById(R.id.filter_group_point_iv);
            this.filter_group_name_bottom_point = itemView.findViewById(R.id.filter_group_name_bottom_point);
        }

        public void onBindViewHolder(final int position) {
            String filterGroupName;
            this.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (FilterGroupNameAdapter.this.mOnClickListener != null) {
                        FilterGroupNameAdapter.this.mOnClickListener.onClick(v, position);
                    }
                }
            });
            FilterInfo filterInfo = (FilterInfo) FilterGroupNameAdapter.this.filterGroupNameList.get(position);
            LayoutParams layoutParams = (LayoutParams) this.filter_group_name_tv.getLayoutParams();
            LayoutParams pointParams = (LayoutParams) this.filter_group_name_bottom_point.getLayoutParams();
            if (position == 0) {
                layoutParams.leftMargin = DensityUtil.dip2px(30.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(19.0f);
                this.filter_group_point_iv.setVisibility(View.VISIBLE);
                pointParams.leftMargin = DensityUtil.dip2px(30.0f);
            } else if (position == 1) {
                layoutParams.leftMargin = DensityUtil.dip2px(19.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(10.0f);
                this.filter_group_point_iv.setVisibility(View.GONE);
                pointParams.leftMargin = DensityUtil.dip2px(19.0f);
            } else if (position == FilterGroupNameAdapter.this.getItemCount() - 1) {
                layoutParams.leftMargin = DensityUtil.dip2px(10.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(30.0f);
                this.filter_group_point_iv.setVisibility(View.GONE);
                pointParams.leftMargin = DensityUtil.dip2px(10.0f);
            } else {
                layoutParams.leftMargin = DensityUtil.dip2px(10.0f);
                layoutParams.rightMargin = DensityUtil.dip2px(10.0f);
                this.filter_group_point_iv.setVisibility(View.GONE);
                pointParams.leftMargin = DensityUtil.dip2px(10.0f);
            }
            this.filter_group_name_tv.setLayoutParams(layoutParams);
            if (LocaleUtil.isChinese()) {
                filterGroupName = filterInfo.name_chs;
            } else {
                filterGroupName = filterInfo.name_en;
            }
            if (TextUtil.isNull(filterGroupName)) {
                filterGroupName = "";
            }
            this.filter_group_name_tv.setText(filterGroupName);
            if (position == FilterGroupNameAdapter.this.selectPosition) {
                this.filter_group_name_tv.setAlpha(1.0f);
                this.filter_group_name_tv.setTypeface(FontsUtil.setAvenirNextCondensedMediumTypeFace());
                pointParams.leftMargin += WaterMarkBitmapUtil.getTextWidth(filterGroupName, this.filter_group_name_tv.getPaint(), FilterGroupNameAdapter.this.tempRect) / 2;
                this.filter_group_name_bottom_point.setLayoutParams(pointParams);
                this.filter_group_name_bottom_point.setVisibility(View.VISIBLE);
                return;
            }
            this.filter_group_name_tv.setAlpha(0.4f);
            this.filter_group_name_tv.setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
            pointParams.leftMargin += WaterMarkBitmapUtil.getTextWidth(filterGroupName, this.filter_group_name_tv.getPaint(), FilterGroupNameAdapter.this.tempRect) / 2;
            this.filter_group_name_bottom_point.setLayoutParams(pointParams);
            this.filter_group_name_bottom_point.setVisibility(View.INVISIBLE);
        }
    }

    public FilterGroupNameAdapter(Activity context, List<FilterInfo> mList) {
        this.context = context;
        if (mList != null) {
            this.filterGroupNameList = mList;
        }
        this.tempRect = new Rect();
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return this.selectPosition;
    }

    public void setOnClickListener(FilterGroupOnclickListener listener) {
        this.mOnClickListener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupNameViewHolder(LayoutInflater.from(this.context).inflate(R.layout.recyclerview_filter_group_name, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((GroupNameViewHolder) holder).onBindViewHolder(position);
    }

    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public int getItemCount() {
        return this.filterGroupNameList == null ? 0 : this.filterGroupNameList.size();
    }
}