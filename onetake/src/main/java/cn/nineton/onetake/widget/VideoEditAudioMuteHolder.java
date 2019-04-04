package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;
//
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper.SlideVideoAreaCallback;

public class VideoEditAudioMuteHolder extends VideoEditViewHolder {
    VideoEditHelper.SlideVideoAreaCallback mSlideVideoAreaCallback;
    @BindView(R.id.video_edit_audio_mute_iv)
    ImageView video_edit_audio_mute_iv;

    public VideoEditAudioMuteHolder(View itemView, Activity context, VideoEditHelper.SlideVideoAreaCallback slideVideoAreaCallback, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.mSlideVideoAreaCallback = slideVideoAreaCallback;
    }

    public void onBindViewHolder(int position) {
        final VideoEditImageEntity entity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        if (entity.isAudioMuteOn()) {
            this.video_edit_audio_mute_iv.setImageResource(R.drawable.icon_20_mute_on_music);
        } else {
            this.video_edit_audio_mute_iv.setImageResource(R.drawable.icon_20_mute_off_music);
        }
        this.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean z = true;
                if (VideoEditAudioMuteHolder.this.mSlideVideoAreaCallback != null) {
                    boolean z2;
                    boolean audioMuteOn = entity.isAudioMuteOn();
                    if (audioMuteOn) {
                        VideoEditAudioMuteHolder.this.video_edit_audio_mute_iv.setImageResource(R.drawable.icon_20_mute_off_music);
                    } else {
                        VideoEditAudioMuteHolder.this.video_edit_audio_mute_iv.setImageResource(R.drawable.icon_20_mute_on_music);
                    }
                    VideoEditImageEntity videoEditImageEntity = entity;
                    if (audioMuteOn) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    videoEditImageEntity.setAudioMuteOn(z2);
                    VideoEditHelper.SlideVideoAreaCallback slideVideoAreaCallback = VideoEditAudioMuteHolder.this.mSlideVideoAreaCallback;
                    if (audioMuteOn) {
                        z = false;
                    }
                    slideVideoAreaCallback.onAudioMuteClick(z);
                }
            }
        });
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }
}