package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;

public class VideoEditEmptyHolder extends VideoEditViewHolder {
    public VideoEditEmptyHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
    }

    public void onBindViewHolder(int position) {
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        refreshItemWidth(videoEditImageEntity.getEmpty_type_width());
        if (videoEditImageEntity.getBgColor() != 0) {
            this.itemView.setBackgroundColor(videoEditImageEntity.getBgColor());
        }
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }

    private void refreshItemWidth(int width) {
        LayoutParams layoutParams = this.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        layoutParams.width = width;
        this.itemView.setLayoutParams(layoutParams);
    }
}