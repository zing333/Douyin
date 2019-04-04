package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.ViewUtil;

//import com.blink.academy.onetake.custom.CircleCoverView;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.ViewUtil;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;

public class VideoEditAudioEmptyHolder extends VideoEditViewHolder {
    @BindView(R.id.audio_empty_bg_ccv)
    CircleCoverView audio_empty_bg_ccv;
    @BindView(R.id.audio_empty_bg_view)
    View audio_empty_bg_view;

    public VideoEditAudioEmptyHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
    }

    public void onBindViewHolder(int position) {
        VideoEditImageEntity entity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        refreshItemWidth(entity.getEmpty_type_width());
        int dip44 = DensityUtil.dip2px(44.0f);
        int dip28 = DensityUtil.dip2px(20.0f);
        if (this.mHelper.isMusicEdit()) {
            ViewUtil.setViewHeight(this.audio_empty_bg_view, dip44);
            ViewUtil.setViewHeight(this.audio_empty_bg_ccv, dip44);
        } else {
            ViewUtil.setViewHeight(this.audio_empty_bg_view, dip28);
            ViewUtil.setViewHeight(this.audio_empty_bg_ccv, dip28);
        }
        refreshCircleCoverViewStatus(this.audio_empty_bg_ccv, entity.getCircleCoverType());
    }

    private void refreshCircleCoverViewStatus(CircleCoverView video_edit_image_ccv, int circleCoverType) {
        switch (circleCoverType) {
            case 0:
                video_edit_image_ccv.setVisibility(View.VISIBLE);
                video_edit_image_ccv.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, 0);
                return;
            case 1:
                video_edit_image_ccv.setVisibility(View.VISIBLE);
                video_edit_image_ccv.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                video_edit_image_ccv.setVisibility(View.VISIBLE);
                video_edit_image_ccv.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, (float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, 0, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            case 3:
                video_edit_image_ccv.setVisibility(View.VISIBLE);
                video_edit_image_ccv.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, (float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, VideoEditImageHolder.CIRCLE_COVER_RADIO, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            default:
                return;
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