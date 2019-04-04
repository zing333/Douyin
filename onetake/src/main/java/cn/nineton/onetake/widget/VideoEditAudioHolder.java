package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.WaveformCacheUtils;

//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;

public class VideoEditAudioHolder extends VideoEditViewHolder {
    @BindView(R.id.video_edit_audio_awv)
    AudioWaveView video_edit_audio_awv;
    @BindView(R.id.video_edit_audio_cover_view)
    View video_edit_audio_cover_view;

    public VideoEditAudioHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.video_edit_audio_awv.setWaveDrawType(2);
        this.video_edit_audio_awv.setColor(ContextCompat.getColor(getActivity(), R.color.color66));
    }

    public void onBindViewHolder(int position) {
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        refreshItemWidth(videoEditImageEntity.getEmpty_type_width());
        refreshCoverView(videoEditImageEntity.getAudioCoverWidth(), videoEditImageEntity.getAudioCoverShowType());
        float audioWaveStartTime = videoEditImageEntity.getAudioWaveStartTime();
        float audioWaveStartDuration = videoEditImageEntity.getAudioWaveStartDuration();
        String audioFilePath = videoEditImageEntity.getAudioFilePath();
        float audioWaveSecondDuration = videoEditImageEntity.getAudioWaveSecondDuration();
        long startTimeUs = (long) ((((int) audioWaveStartTime) * 1000) * 1000);
        long needStartTimeUs = (long) ((1000.0f * audioWaveStartTime) * 1000.0f);
        long durationUs = (long) ((1000.0f * audioWaveStartDuration) * 1000.0f);
        long audioSumTime = videoEditImageEntity.getAudioSumTime();
        if (audioWaveSecondDuration == StaticLayoutUtil.DefaultSpacingadd) {
            bindVideoWaveViewData(this.video_edit_audio_awv, audioFilePath, startTimeUs, needStartTimeUs, durationUs, audioSumTime);
        }
    }

    private void bindVideoWaveViewData(AudioWaveView audioWaveView, String filePath, long start, long needStart, long duration, long sumTimeUs) {
        if (audioWaveView.getVisibility() != View.VISIBLE) {
            audioWaveView.setVisibility(View.VISIBLE);
        }
        String tag1 = (String) audioWaveView.getTag(R.id.wave_tag_1);
        Long tag2 = (Long) audioWaveView.getTag(R.id.wave_tag_2);
        String tag1Str = null;
        if (tag1 instanceof String) {
            tag1Str = tag1;
        }
        Long tag2Long = null;
        if (tag2 instanceof Long) {
            tag2Long = tag2;
        }
        if (tag1Str == null || tag2Long == null || !TextUtils.equals(tag1Str, filePath) || tag2Long.longValue() != start || audioWaveView.getDrawData() == null) {
            LogUtil.d("VideoEditAudioHolder", String.format("bindVideoWaveViewData position : %s , start : %s , needStart : %s ,  duration : %s , filePath : %s ", new Object[]{Integer.valueOf(getAdapterPosition()), Long.valueOf(start), Long.valueOf(needStart), Long.valueOf(duration), filePath}));
            audioWaveView.clearDrawCanvas();
            audioWaveView.setTag(2131689506, filePath);
            audioWaveView.setTag(2131689507, Long.valueOf(start));
            final AudioWaveView audioWaveView2 = audioWaveView;
            final long j = start;
            this.mHelper.getAudioWaveData(audioWaveView, filePath, start, needStart, duration, sumTimeUs, new WaveformCacheUtils.WaveDataCallback() {
                public void onLoadFinished(final String filePath, long startTimeUs, long durationUs, final float[] data) {
//                    VideoEditAudioHolder.this.access$200(new Runnable() {
//                        public void run() {
//                            Long tag2 = (Long) audioWaveView2.getTag(2131689507);
//                            if (TextUtils.equals((String) audioWaveView2.getTag(2131689506), filePath) && tag2 != null && tag2.longValue() == j) {
//                                audioWaveView2.setOriginData(data);
//                            }
//                        }
//                    });
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Long tag2 = (Long) audioWaveView2.getTag(R.id.wave_tag_2);
                            if (TextUtils.equals((String) audioWaveView2.getTag(R.id.wave_tag_1), filePath) && tag2 != null && tag2.longValue() == j) {
                                audioWaveView2.setOriginData(data);
                            }
                        }
                    });
                }

                public void onLoadFailed(final String filePath, long startTimeUs, long durationUs) {
//                    VideoEditAudioHolder.this.access$200(new Runnable() {
//                        public void run() {
//                            Long tag2 = (Long) audioWaveView2.getTag(2131689507);
//                            if (TextUtils.equals((String) audioWaveView2.getTag(2131689506), filePath) && tag2 != null && tag2.longValue() == j) {
//                                VideoEditAudioHolder.this.hideVideoWaveView();
//                            }
//                        }
//                    });
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Long tag2 = (Long) audioWaveView2.getTag(R.id.wave_tag_2);
                            if (TextUtils.equals((String) audioWaveView2.getTag(R.id.wave_tag_1), filePath) && tag2 != null && tag2.longValue() == j) {
                                VideoEditAudioHolder.this.hideVideoWaveView();
                        }
                        }
                    });
                }
            });
        }
    }

    private void hideVideoWaveView() {
        this.video_edit_audio_awv.setVisibility(View.GONE);
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }

    @SuppressLint({"RtlHardcoded"})
    private void refreshCoverView(int width, int showType) {
        if (width != 0) {
            LayoutParams layoutParams = (LayoutParams) this.video_edit_audio_cover_view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(-2, -1);
            }
            switch (showType) {
                case 0:
                    layoutParams.gravity = 3;
                    break;
                case 1:
                    layoutParams.gravity = 5;
                    break;
            }
            layoutParams.width = width;
            this.video_edit_audio_cover_view.setLayoutParams(layoutParams);
            this.video_edit_audio_cover_view.setVisibility(View.VISIBLE);
            return;
        }
        this.video_edit_audio_cover_view.setVisibility(View.GONE);
    }

    private void refreshItemWidth(int width) {
        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -1);
        }
        if (layoutParams.width != width) {
            layoutParams.width = width;
            this.itemView.setLayoutParams(layoutParams);
        }
    }
}