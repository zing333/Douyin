package cn.nineton.onetake.widget;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.util.Config;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.ViewUtil;
import cn.nineton.onetake.util.WaveformCacheUtils;

//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.custom.CircleCoverView;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.ViewUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
import java.io.File;
import java.util.List;

public class VideoEditAudioEditHolder extends VideoEditViewHolder {
    private static final String TAG = VideoEditAudioEditHolder.class.getSimpleName();
    int dip20 = DensityUtil.dip2px(20.0f);
    int dip24 = DensityUtil.dip2px(24.0f);
    int dip28 = DensityUtil.dip2px(28.0f);
    int dip44 = DensityUtil.dip2px(44.0f);
    int dip48 = DensityUtil.dip2px(48.0f);
    int dip52 = DensityUtil.dip2px(52.0f);
    private int mScreenWidth;
    @BindView(R.id.music_audio_wave_view)
    AudioWaveView music_audio_wave_view;
    @BindView(R.id.music_bg_cover)
    CircleCoverView music_bg_cover;
    @BindView(R.id.music_bg_view)
    View music_bg_view;
    @BindView(R.id.music_edit_white_parent_rl)
    RelativeLayout music_edit_white_parent_rl;
    @BindView(R.id.music_volume_center_line)
    View music_volume_center_line;
    @BindView(R.id.music_wave_content_ccv)
    CircleCoverView music_wave_content_ccv;
    @BindView(R.id.music_wave_content_fl)
    FrameLayout music_wave_content_fl;
    @BindView(R.id.video_edit_audio_center_ver_line)
    View video_edit_audio_center_ver_line;
    @BindView(R.id.video_edit_audio_cover_view)
    View video_edit_audio_cover_view;
    @BindView(R.id.video_edit_audio_volume_line)
    View video_edit_audio_volume_line;
    @BindView(R.id.video_edit_bottom_line)
    View video_edit_bottom_line;
    @BindView(R.id.video_edit_bottom_line_bottom)
    View video_edit_bottom_line_bottom;
    @BindView(R.id.video_edit_inner_cover)
    CircleCoverView video_edit_inner_cover;
    @BindView(R.id.video_edit_left_iv_slide_view)
    View video_edit_left_iv_slide_view;
    @BindView(R.id.video_edit_mute_tv)
    TextView video_edit_mute_tv;
    @BindView(R.id.video_edit_right_iv_slide_view)
    View video_edit_right_iv_slide_view;
    @BindView(R.id.video_edit_slider_left_iv)
    ImageView video_edit_slider_left_iv;
    @BindView(R.id.video_edit_slider_left_iv_bottom)
    View video_edit_slider_left_iv_bottom;
    @BindView(R.id.video_edit_slider_right_iv)
    ImageView video_edit_slider_right_iv;
    @BindView(R.id.video_edit_slider_right_iv_bottom)
    View video_edit_slider_right_iv_bottom;
    @BindView(R.id.video_edit_top_line)
    View video_edit_top_line;
    @BindView(R.id.video_edit_top_line_bottom)
    View video_edit_top_line_bottom;

    public VideoEditAudioEditHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.mScreenWidth = DensityUtil.getMetricsWidth(context);
        this.music_wave_content_ccv.setNeedDrawOutLine(true);
        this.music_audio_wave_view.setAlpha(0.8f);
    }

    public void onBindViewHolder(int position) {
        List<VideoEditImageEntity> allDatas = this.mHelper.getAllDatas();
        if (allDatas != null) {
            String filePath;
            int leftOutAreaColor;
            int rightOutAreaColor;
            int size = allDatas.size();
            VideoEditImageEntity entity = (VideoEditImageEntity) allDatas.get(position);
            ViewUtil.setViewWidth(this.itemView, entity.getEmpty_type_width());
            int circleCoverType = entity.getCircleCoverType();
            refreshCircleCoverViewStatus(this.music_bg_cover, circleCoverType);
            this.music_audio_wave_view.setBackgroundColor(entity.getBgColor());
            boolean musicEdit = this.mHelper.isMusicEdit();
            refreshNeedChangeHeightView(entity, musicEdit);
            LongVideosModel curSelectAudioModel = this.mHelper.getCurSelectAudioModel();
            LongVideosModel longVideosModel = entity.getLongVideosModel();
            LongVideosModel.AudioVolume audioVolume = entity.getAudioVolume();
            int white_area_type = 0;
            boolean selected = isAudioModelSelected(longVideosModel, curSelectAudioModel);
            if (selected && musicEdit) {
                if (circleCoverType == 1) {
                    white_area_type = 2;
                } else if (circleCoverType == 2) {
                    white_area_type = 3;
                } else if (circleCoverType == 3) {
                    white_area_type = 4;
                } else if (circleCoverType == 0) {
                    white_area_type = 1;
                }
                this.music_wave_content_ccv.setVisibility(View.GONE);
            } else {
                refreshCircleCoverViewStatus(this.music_wave_content_ccv, circleCoverType);
                this.music_wave_content_ccv.setVisibility(View.VISIBLE);
            }
            refreshWhiteAreaView(white_area_type);
            refreshInnerCircleCoverViewStatus(circleCoverType);
            float volume = audioVolume.getVolume();
            if (this.mHelper.isAllAudioMute()) {
                volume = StaticLayoutUtil.DefaultSpacingadd;
            }
            refreshVideoVolumeLineView(longVideosModel, volume, audioVolume, selected);
            AudioTrackBean audioTrackBean = longVideosModel.getAudioTrackBean();
            float userChooseAudioStart = longVideosModel.getTrueStartTime();
            float audioVerse = StaticLayoutUtil.DefaultSpacingadd;
            String poi = audioTrackBean.getPoi();
            if (TextUtil.isValidate(poi)) {
                String[] split = poi.split(",");
                int length = split.length;
                if (length > 1) {
                    audioVerse = Float.valueOf(split[length - 1]).floatValue();
                }
            }
            float audioSumTime = Float.valueOf(audioTrackBean.getDuration()).floatValue();
            if (audioTrackBean.isLocal()) {
                filePath = audioTrackBean.getFull_url();
            } else {
                filePath = new File(Config.getAudiosDownloadPath(), String.valueOf(audioTrackBean.getId())).getAbsolutePath();
            }
            long start = (long) ((1000.0f * userChooseAudioStart) * 1000.0f);
            long audioModelDuration = longVideosModel.getAudioDuration() * 1000;
            long sumTimeUs = (long) ((1000.0f * audioSumTime) * 1000.0f);
            long verse = (long) ((1000.0f * audioVerse) * 1000.0f);
            long audioStartTime = longVideosModel.getAudioStartTime();
            long audioDuration = longVideosModel.getAudioDuration();
            long startTimeUs = ((entity.getAudioStartTime() - audioStartTime) * 1000) + start;
            bindVideoWaveViewData(this.music_audio_wave_view, filePath, startTimeUs, startTimeUs, entity.getAudioDuration() * 1000, sumTimeUs, verse);
            if (audioStartTime != 0) {
                leftOutAreaColor = ContextCompat.getColor(getActivity(), R.color.colorMusicEditBg);
            } else {
                leftOutAreaColor = ContextCompat.getColor(getActivity(), R.color.colorGray);
            }
            if (audioStartTime + audioDuration != this.mHelper.getVideoSumTime()) {
                rightOutAreaColor = ContextCompat.getColor(getActivity(), R.color.colorMusicEditBg);
            } else {
                rightOutAreaColor = ContextCompat.getColor(getActivity(), R.color.colorGray);
            }
            this.music_wave_content_ccv.setLeftAndRightOutAreaColor(leftOutAreaColor, rightOutAreaColor, true);
            this.video_edit_audio_center_ver_line.setVisibility(View.GONE);
            int prevPos = position - 1;
            if (prevPos >= 0) {
                LongVideosModel.AudioVolume prevVolume = ((VideoEditImageEntity) this.mHelper.getAllDatas().get(prevPos)).getAudioVolume();
                if (!(prevVolume == null || prevVolume == audioVolume || (audioVolume.getVolume() == prevVolume.getVolume() && prevVolume.isSelected() == audioVolume.isSelected()))) {
                    refreshMusicVolumeVerLine(this.video_edit_audio_center_ver_line);
                }
            }
            if (!this.mHelper.isMusicEdit() || this.mHelper.isAllAudioMute()) {
                if (this.video_edit_mute_tv.getVisibility() == View.VISIBLE) {
                    this.video_edit_mute_tv.setVisibility(View.GONE);
                }
            } else if (!selected && volume == StaticLayoutUtil.DefaultSpacingadd && entity.isShowMuteTv()) {
                ((LayoutParams) this.video_edit_mute_tv.getLayoutParams()).leftMargin = entity.getMuteTvLeftMargin();
                this.video_edit_mute_tv.setVisibility(View.VISIBLE);
            } else if (this.video_edit_mute_tv.getVisibility() == View.VISIBLE) {
                this.video_edit_mute_tv.setVisibility(View.GONE);
            }
        }
    }

    private void refreshMusicVolumeVerLine(View verLineView) {
        LayoutParams frameLayoutParams;
        ViewGroup.LayoutParams layoutParams = verLineView.getLayoutParams();
        int width = DensityUtil.dip2px(1.0f);
        if (!this.mHelper.isMusicEdit()) {
            width = 1;
        }
        if (layoutParams == null || !(layoutParams instanceof LayoutParams)) {
            frameLayoutParams = new LayoutParams(width, -1);
        } else {
            frameLayoutParams = (LayoutParams) layoutParams;
        }
        frameLayoutParams.width = width;
        frameLayoutParams.leftMargin = (int) ((((float) (-width)) * 1.0f) / 2.0f);
        verLineView.setLayoutParams(frameLayoutParams);
        verLineView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        verLineView.setVisibility(View.VISIBLE);
    }

    public void refreshVideoVolumeLineView(float volume) {
        if (volume == 1.0f || volume == StaticLayoutUtil.DefaultSpacingadd) {
            this.video_edit_audio_volume_line.setVisibility(View.GONE);
        } else {
            LayoutParams layoutParams = (LayoutParams) this.video_edit_audio_volume_line.getLayoutParams();
            int lineHeight = DensityUtil.dip2px(1.0f);
            layoutParams.height = lineHeight;
            layoutParams.topMargin = (int) (((((float) this.dip44) * 1.0f) * (1.0f - volume)) - ((((float) lineHeight) * 1.0f) / 2.0f));
            this.video_edit_audio_volume_line.setLayoutParams(layoutParams);
            this.video_edit_audio_volume_line.setVisibility(View.GONE);
            this.video_edit_audio_volume_line.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        }
        refreshAudioWaveDrawHeight(volume);
        refreshBlackCoverAlphaWhenSlide(volume);
    }

    private void refreshBlackCoverAlphaWhenSlide(float volume) {
        if (volume == StaticLayoutUtil.DefaultSpacingadd) {
            this.video_edit_audio_cover_view.setAlpha(0.3f);
        } else {
            this.video_edit_audio_cover_view.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
        }
    }

    private void refreshVideoVolumeLineView(LongVideosModel model, float volume, LongVideosModel.AudioVolume audioVolume, boolean isSelected) {
        if (volume == 1.0f || volume == StaticLayoutUtil.DefaultSpacingadd) {
            this.video_edit_audio_volume_line.setVisibility(View.GONE);
        } else {
            int lineHeight = DensityUtil.dip2px(1.0f);
            if (!this.mHelper.isMusicEdit()) {
                lineHeight = 1;
            }
            int maxHeight = this.dip44;
            if (!this.mHelper.isMusicEdit()) {
                maxHeight = this.dip20;
            }
            LayoutParams layoutParams = (LayoutParams) this.video_edit_audio_volume_line.getLayoutParams();
            layoutParams.topMargin = (int) (((((1.0f - volume) * 10.0f) / 10.0f) * ((float) maxHeight)) - ((((float) lineHeight) * 1.0f) / 2.0f));
            layoutParams.height = lineHeight;
            this.video_edit_audio_volume_line.setLayoutParams(layoutParams);
            this.video_edit_audio_volume_line.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(getActivity(), R.color.color30White);
            if (this.mHelper.isMusicEdit() && isSelected && audioVolume.isSelected()) {
                color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
            }
            this.video_edit_audio_volume_line.setBackgroundColor(color);
        }
        refreshAudioWaveDrawHeight(volume);
        refreshMusicCoverViewAlpha(model, this.video_edit_audio_cover_view, audioVolume);
    }

    private void refreshMusicCoverViewAlpha(LongVideosModel model, View view, LongVideosModel.AudioVolume audioVolume) {
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack242322));
            boolean isJustSeeForAudio = false;
            if (model != null) {
                isJustSeeForAudio = model.isJustSeeForAudio();
            }
            if (!this.mHelper.isMusicEdit()) {
                view.setAlpha(0.3f);
            } else if (this.mHelper.isAllAudioMute()) {
                view.setAlpha(0.3f);
            } else if (isJustSeeForAudio && audioVolume.isSelected()) {
                view.setAlpha(0.3f);
            } else if (isJustSeeForAudio) {
                view.setAlpha(0.6f);
            } else if (isAudioModelSelected(model, this.mHelper.getCurSelectAudioModel()) && audioVolume.isSelected() && audioVolume.getVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                view.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            } else {
                view.setAlpha(0.3f);
            }
        }
    }

    private void refreshAudioWaveDrawHeight(float volume) {
        this.music_audio_wave_view.compressYLength(volume);
    }

    private void bindVideoWaveViewData(AudioWaveView audioWaveView, String filePath, long start, long needStart, long duration, long sumTimeUs, long audioVerseUs) {
        if (audioWaveView.getVisibility() != View.VISIBLE) {
            audioWaveView.setVisibility(View.VISIBLE);
        }
        long resultDurationUs = 1000000;
        if (duration > 1000000) {
            ViewUtil.setViewWidth(audioWaveView, VideoEditHelper.IMAGE_UNIT_WIDTH * 2);
            resultDurationUs = 2000000;
        } else {
            ViewUtil.setViewWidth(audioWaveView, VideoEditHelper.IMAGE_UNIT_WIDTH);
        }
        LogUtil.d("AudioWaveView", String.format("position : %s , hashCode : %s ", getAdapterPosition(), audioWaveView.hashCode()));
        String tag1 = (String) audioWaveView.getTag(R.id.wave_tag_1);
        Long tag2 = (Long) audioWaveView.getTag(R.id.wave_tag_2);
        Long tag3 = (Long) audioWaveView.getTag(R.id.wave_tag_3);
        String tag1Str = null;
        if (tag1 instanceof String) {
            tag1Str = tag1;
        }
        Long tag2Long = null;
        if (tag2 instanceof Long) {
            tag2Long = tag2;
        }
        Long tag3Long = null;
        if (tag3 instanceof Long) {
            tag3Long = tag3;
        }
        if (tag1Str == null || tag2Long == null || tag3Long == null || !TextUtils.equals(tag1Str, filePath) || tag2Long.longValue() != start || tag3Long.longValue() != resultDurationUs || audioWaveView.getDrawData() == null) {
            LogUtil.d("onSizeChanged", String.format("bindVideoWaveViewData position : %s , start : %s , needStart : %s ,  duration : %s , resultDurationUs : %s , filePath : %s", new Object[]{Integer.valueOf(getAdapterPosition()), Long.valueOf(start), Long.valueOf(needStart), Long.valueOf(duration), Long.valueOf(resultDurationUs), filePath}));
            audioWaveView.setTag(R.id.wave_tag_1, filePath);
            audioWaveView.setTag(R.id.wave_tag_2, Long.valueOf(start));
            audioWaveView.setTag(R.id.wave_tag_3, Long.valueOf(resultDurationUs));
            float[] dataFromMem = this.mHelper.justGetAudioWaveDataFromMem(audioWaveView, filePath, start, needStart, resultDurationUs, sumTimeUs, audioVerseUs);
            if (dataFromMem != null) {
                audioWaveView.setOriginData(dataFromMem, true);
                return;
            }
            audioWaveView.clearDrawCanvas();
            final AudioWaveView audioWaveView2 = audioWaveView;
            final long j = start;
            this.mHelper.getAudioWaveData((View) audioWaveView, filePath, start, needStart, resultDurationUs, sumTimeUs, audioVerseUs, new WaveformCacheUtils.WaveDataCallback() {
                public void onLoadFinished(final String filePath, long startTimeUs, long durationUs, final float[] data) {
//                    VideoEditAudioEditHolder.this.access$100(new Runnable() {
//                        public void run() {
//                            Long tag2 = (Long) audioWaveView2.getTag(2131689507);
//                            if (TextUtils.equals((String) audioWaveView2.getTag(2131689506), filePath) && tag2 != null && tag2.longValue() == j) {
//                                audioWaveView2.setOriginData(data, true);
//                            }
//                        }
//                    });
                    Long tag2 = (Long) audioWaveView2.getTag(R.id.wave_tag_2);
                    if (TextUtils.equals((String) audioWaveView2.getTag(R.id.wave_tag_1), filePath) && tag2 != null && tag2.longValue() == j) {
                        audioWaveView2.setOriginData(data, true);
                    }
                }

                public void onLoadFailed(final String filePath, long startTimeUs, long durationUs) {
//                    VideoEditAudioEditHolder.this.access$100(new Runnable() {
//                        public void run() {
//                            Long tag2 = (Long) audioWaveView2.getTag(2131689507);
//                            if (!TextUtils.equals((String) audioWaveView2.getTag(2131689506), filePath) || tag2 == null || tag2.longValue() == j) {
//                            }
//                        }
//                    });
                    Long tag2 = (Long) audioWaveView2.getTag(R.id.wave_tag_2);
                    if (!TextUtils.equals((String) audioWaveView2.getTag(R.id.wave_tag_1), filePath) || tag2 == null || tag2.longValue() == j) {
                    }
                }
            });
        }
    }

    private void refreshWhiteAreaView(int white_area_type) {
        switch (white_area_type) {
            case 0:
                this.music_edit_white_parent_rl.setVisibility(View.GONE);
                this.video_edit_top_line_bottom.setVisibility(View.GONE);
                this.video_edit_top_line.setVisibility(View.GONE);
                this.video_edit_bottom_line_bottom.setVisibility(View.GONE);
                this.video_edit_bottom_line.setVisibility(View.GONE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_right_iv.setVisibility(View.GONE);
                this.video_edit_right_iv_slide_view.setVisibility(View.GONE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_left_iv.setVisibility(View.GONE);
                this.video_edit_left_iv_slide_view.setVisibility(View.GONE);
                return;
            case 1:
                this.video_edit_top_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_top_line.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_right_iv.setVisibility(View.GONE);
                this.video_edit_right_iv_slide_view.setVisibility(View.GONE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_left_iv.setVisibility(View.GONE);
                this.video_edit_left_iv_slide_view.setVisibility(View.GONE);
                this.music_edit_white_parent_rl.setVisibility(View.VISIBLE);
                return;
            case 2:
                this.video_edit_top_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_top_line.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_right_iv.setVisibility(View.GONE);
                this.video_edit_right_iv_slide_view.setVisibility(View.GONE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv.setVisibility(View.VISIBLE);
                this.video_edit_left_iv_slide_view.setVisibility(View.VISIBLE);
                this.music_edit_white_parent_rl.setVisibility(View.VISIBLE);
                return;
            case 3:
                this.video_edit_top_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_top_line.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv.setVisibility(View.VISIBLE);
                this.video_edit_right_iv_slide_view.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_left_iv.setVisibility(View.GONE);
                this.video_edit_left_iv_slide_view.setVisibility(View.GONE);
                this.music_edit_white_parent_rl.setVisibility(View.VISIBLE);
                return;
            case 4:
                this.video_edit_top_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_top_line.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line_bottom.setVisibility(View.VISIBLE);
                this.video_edit_bottom_line.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv.setVisibility(View.VISIBLE);
                this.video_edit_right_iv_slide_view.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv.setVisibility(View.VISIBLE);
                this.video_edit_left_iv_slide_view.setVisibility(View.VISIBLE);
                this.music_edit_white_parent_rl.setVisibility(View.VISIBLE);
                return;
            default:
                return;
        }
    }

    private void hideVideoWaveView() {
        this.music_audio_wave_view.setVisibility(View.GONE);
    }

    private void refreshNeedChangeHeightView(VideoEditImageEntity entity, boolean musicEdit) {
        int resultHeight = musicEdit ? this.dip44 : this.dip20;
        int musicBgCoverWidth = entity.getMusicBgCoverWidth();
        ViewUtil.setViewWidthAndHeight(this.music_bg_view, musicBgCoverWidth, resultHeight);
        ViewUtil.setViewWidthAndHeight(this.music_bg_cover, musicBgCoverWidth, resultHeight);
        int slideIvHeight = musicEdit ? this.dip48 : this.dip24;
        ViewUtil.setViewHeight(this.video_edit_slider_right_iv, slideIvHeight);
        ViewUtil.setViewHeight(this.video_edit_slider_left_iv, slideIvHeight);
        int slideIvBottomHeight = musicEdit ? this.dip52 : this.dip28;
        ViewUtil.setViewHeight(this.video_edit_slider_right_iv_bottom, slideIvBottomHeight);
        ViewUtil.setViewHeight(this.video_edit_right_iv_slide_view, slideIvBottomHeight);
        ViewUtil.setViewHeight(this.video_edit_slider_left_iv_bottom, slideIvBottomHeight);
        ViewUtil.setViewHeight(this.video_edit_left_iv_slide_view, slideIvBottomHeight);
        ViewUtil.setViewHeight(this.video_edit_inner_cover, musicEdit ? this.dip44 : this.dip20);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.video_edit_bottom_line.getLayoutParams();
        layoutParams.topMargin = musicEdit ? this.dip48 : this.dip24;
        this.video_edit_bottom_line.setLayoutParams(layoutParams);
    }

    private void setContentWidth(int leftWidth, int musicWaveContentWidth, boolean controlAddMusicTextShow) {
        LayoutParams layoutParams = (LayoutParams) this.music_wave_content_fl.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -1);
        }
        layoutParams.width = musicWaveContentWidth;
        layoutParams.height = -1;
        layoutParams.leftMargin = leftWidth;
        this.music_wave_content_fl.setLayoutParams(layoutParams);
        if (!controlAddMusicTextShow || this.mHelper.checkAddTextVisible(leftWidth)) {
        }
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }

    private boolean isAudioModelSelected(LongVideosModel longVideosModel, LongVideosModel curSelectedModel) {
        return longVideosModel == curSelectedModel;
    }

    private boolean isAudioVolumeSelected() {
        return false;
    }

    private void refreshInnerCircleCoverViewStatus(int circleCoverType) {
        switch (circleCoverType) {
            case 0:
                this.video_edit_inner_cover.setVisibility(View.GONE);
                return;
            case 1:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            case 3:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            default:
                return;
        }
    }

    private void refreshInnerCircleCoverViewStatus(CircleCoverView circleCoverView, int circleCoverType) {
        switch (circleCoverType) {
            case 0:
                circleCoverView.setVisibility(View.GONE);
                return;
            case 1:
                circleCoverView.setVisibility(View.GONE);
                circleCoverView.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                circleCoverView.setVisibility(View.VISIBLE);
                circleCoverView.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            case 3:
                circleCoverView.setVisibility(View.VISIBLE);
                circleCoverView.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            default:
                return;
        }
    }

    private void refreshCircleCoverViewStatus(CircleCoverView circleCoverView, int circleCoverType) {
        refreshCircleCoverViewStatus(circleCoverView, circleCoverType, false);
    }

    private void refreshCircleCoverViewStatus(CircleCoverView circleCoverView, int circleCoverType, boolean isJustSee) {
        switch (circleCoverType) {
            case 0:
                circleCoverView.setVisibility(View.VISIBLE);
                circleCoverView.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, 0);
                return;
            case 1:
                circleCoverView.setVisibility(View.VISIBLE);
                circleCoverView.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                circleCoverView.setVisibility(View.VISIBLE);
                circleCoverView.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, (float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, 0, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                return;
            case 3:
                circleCoverView.setVisibility(View.VISIBLE);
                if (isJustSee) {
                    circleCoverView.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, StaticLayoutUtil.DefaultSpacingadd, VideoEditImageHolder.CIRCLE_COVER_RADIO, 0);
                    return;
                } else {
                    circleCoverView.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, (float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, VideoEditImageHolder.CIRCLE_COVER_RADIO, VideoEditImageHolder.CIRCLE_COVER_RADIO);
                    return;
                }
            default:
                return;
        }
    }
}