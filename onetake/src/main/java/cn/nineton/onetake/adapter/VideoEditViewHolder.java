package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager.GetBitmapCallback;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NewABRecyclerViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NewABRecyclerViewHolder.HolderHelper;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NormalBaseAdapter.OnItemClickListener;
//import com.blink.academy.onetake.widgets.AudioWave.LargeAudioWaveView;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.util.WaveformCacheUtils;
import cn.nineton.onetake.widget.LargeAudioWaveView;

public abstract class VideoEditViewHolder extends NewABRecyclerViewHolder {
    protected VideoEditHolderHelper<VideoEditImageEntity> mHelper;

    public interface VideoEditHolderHelper<T> extends HolderHelper<T> {
        boolean checkAddTextVisible(int i);

        int getAudioCurSelectPos();

        void getAudioWaveData(View view, String str, long j, long j2, long j3, long j4, long j5, WaveformCacheUtils.WaveDataCallback waveDataCallback);

        void getAudioWaveData(View view, String str, long j, long j2, long j3, long j4, WaveformCacheUtils.WaveDataCallback waveDataCallback);

        void getAudioWaveData(LargeAudioWaveView largeAudioWaveView, String str, long j, long j2, long j3, long j4, long j5, WaveformCacheUtils.WaveDataCallback waveDataCallback);

        int getCurAudioScrollX();

        LongVideosModel getCurSelectAudioModel();

        NormalBaseAdapter.OnItemClickListener getItemClickListener();

        long getVideoSumTime();

        boolean hasJustSeeAudioItem();

        boolean isAllAudioMute();

        boolean isAllVideoMute();

        boolean isAudioSliding();

        boolean isHavenInMusicEdit();

        boolean isMusicEdit();

        float[] justGetAudioWaveDataFromMem(View view, String str, long j, long j2, long j3, long j4, long j5);

        void loadBitmap(ImageView imageView, LongVideosModel longVideosModel, long j, GetBitmapCallback getBitmapCallback);

        void onAudioContentClick(int i);

        void onEditAudioEmptyClick(int i);

        void onHideSlideText();

        void onLeftAddMusicClick(int i);

        void onMusicVolumeSlideEnd(int i, long j, long j2, float f, boolean z);

        void onRightAddMusicClick(int i);

        void onShowSlideText();

        void onSlideAudioLeft(int i, int i2);

        void onSlideAudioLeftAutoScrollToLeft(int i);

        void onSlideAudioLeftAutoScrollToRight(int i);

        void onSlideAudioLeftEnd(int i);

        void onSlideAudioRight(int i, int i2);

        void onSlideAudioRightAutoScrollToLeft(int i);

        void onSlideAudioRightAutoScrollToRight(int i);

        void onSlideAudioRightEnd(int i);

        void onSlideTextContent(int i, int i2);

        void onSlideTextContentAutoScrollToLeft(int i);

        void onSlideTextContentAutoScrollToRight(int i);

        void onSlideTextContentEnd(int i);

        void onSlideTextContentStart(int i);

        void onSlideTextLeft(int i, int i2);

        void onSlideTextLeftAutoScrollToLeft(int i);

        void onSlideTextLeftAutoScrollToRight(int i);

        void onSlideTextLeftEnd(int i);

        void onSlideTextLeftStart(int i);

        void onSlideTextRight(int i, int i2);

        void onSlideTextRightAutoScrollToLeft(int i);

        void onSlideTextRightAutoScrollToRight(int i);

        void onSlideTextRightEnd(int i);

        void onSlideTextRightStart(int i);

        void onSlideVideoLeft(int i, int i2);

        void onSlideVideoLeftAutoScrollToLeft(int i);

        void onSlideVideoLeftAutoScrollToRight(int i);

        void onSlideVideoLeftEnd(int i);

        void onSlideVideoRight(int i, int i2);

        void onSlideVideoRightAutoScrollToLeft(int i);

        void onSlideVideoRightAutoScrollToRight(int i);

        void onSlideVideoRightEnd(int i);

        void onStartSlideAudioLeft(int i);

        void onStartSlideAudioRight(int i);

        void onStartSlideVideoLeft(int i);

        void onStartSlideVideoRight(int i);

        void onTextDeleteClick(int i);

        void onTextEditClick(int i);

        void preloadBitmap(LongVideosModel longVideosModel, long j, GetBitmapCallback getBitmapCallback);

        void setLongPressingStatus(boolean z);
    }

    public VideoEditViewHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        this.mHelper = helper;
    }
}