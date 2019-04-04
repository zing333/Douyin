package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;

public class VideoEditImageMuteHolder extends VideoEditViewHolder {
    @BindView(R.id.video_edit_image_mute_iv)
    ImageView video_edit_image_mute_iv;

    public VideoEditImageMuteHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
    }

    public void onBindViewHolder(int position) {
        if (((VideoEditImageEntity) this.mHelper.getAllDatas().get(position)).isVideoMuteOn()) {
            this.video_edit_image_mute_iv.setImageResource(R.drawable.icon_20_mute_on);
        } else {
            this.video_edit_image_mute_iv.setImageResource(R.drawable.icon_20_mute_off);
        }
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }
}