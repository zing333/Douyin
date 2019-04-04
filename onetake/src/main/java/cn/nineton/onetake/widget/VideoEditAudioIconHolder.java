package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;

public class VideoEditAudioIconHolder extends VideoEditViewHolder {
    public VideoEditAudioIconHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
    }

    public void onBindViewHolder(int position) {
        refreshItemWidth(((VideoEditImageEntity) this.mHelper.getAllDatas().get(position)).getEmpty_type_width());
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