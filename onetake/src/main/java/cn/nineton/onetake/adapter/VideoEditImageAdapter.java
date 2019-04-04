package cn.nineton.onetake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager.GetBitmapCallback;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NewABRecyclerViewHolder;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NormalBaseAdapter;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NormalBaseAdapter.OnItemClickListener;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAddHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioAddHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioEditHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioEmptyHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioIconHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioMuteHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditEmptyHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageMuteHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditMusicEditHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditTextHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditTimeHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper.SlideVideoAreaCallback;
//import com.blink.academy.onetake.widgets.AudioWave.LargeAudioWaveView;
//import com.blink.academy.onetake.widgets.LinearLayout.LeftTextCenterLayout;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;
import cn.nineton.onetake.util.WaveformCacheUtils;
import cn.nineton.onetake.widget.LargeAudioWaveView;
import cn.nineton.onetake.widget.LeftTextCenterLayout;
import cn.nineton.onetake.widget.VideoEditAddHolder;
import cn.nineton.onetake.widget.VideoEditAudioEmptyHolder;
import cn.nineton.onetake.widget.VideoEditAudioHolder;
import cn.nineton.onetake.widget.VideoEditAudioIconHolder;
import cn.nineton.onetake.widget.VideoEditAudioMuteHolder;
import cn.nineton.onetake.widget.VideoEditEmptyHolder;
import cn.nineton.onetake.widget.VideoEditHelper;
import cn.nineton.onetake.widget.VideoEditImageHolder;
import cn.nineton.onetake.widget.VideoEditImageMuteHolder;
import cn.nineton.onetake.widget.VideoEditTextHolder;
import cn.nineton.onetake.widget.VideoEditTimeHolder;

import static cn.nineton.onetake.App.getContext;

public class VideoEditImageAdapter extends NormalBaseAdapter<NewABRecyclerViewHolder> implements VideoEditViewHolder.VideoEditHolderHelper<VideoEditImageEntity> {
    VideoEditHelper.SlideVideoAreaCallback mSlideVideoAreaCallback;
    ArrayList<VideoEditImageEntity> mVideoEditImageEntities;
    VideoAudioPlaybackManager manager;

    public VideoEditImageAdapter(Context context, ArrayList<VideoEditImageEntity> videoEditImageEntities, VideoAudioPlaybackManager videoAudioPlaybackManager, VideoEditHelper.SlideVideoAreaCallback callback) {
        super(context);
        this.mVideoEditImageEntities = videoEditImageEntities;
        this.manager = videoAudioPlaybackManager;
        this.mSlideVideoAreaCallback = callback;
    }

    public NewABRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Item.VIDEO_EDIT_IMAGE_TYPE:
                return new VideoEditImageHolder(getResView(R.layout.layout_video_edit_image_item, parent), getActivity(), this);
            case Item.VIDEO_EDIT_IMAGE_MUTE_TYPE /*275*/:
                return new VideoEditImageMuteHolder(getResView(R.layout.layout_video_edit_image_mute, parent), getActivity(), this);
            case Item.VIDEO_EDIT_ADD_TYPE /*276*/:
                return new VideoEditAddHolder(getResView(R.layout.layout_video_edit_add_view, parent), getActivity(), this);
            case Item.VIDEO_EDIT_EMPTY_TYPE /*278*/:
                return new VideoEditEmptyHolder(new View(getContext()), getActivity(), this);
            case Item.VIDEO_EDIT_TIME_TYPE:
                return new VideoEditTimeHolder(new LeftTextCenterLayout(getContext()), getActivity(), this);
            case Item.VIDEO_EDIT_AUDIO_TYPE /*282*/:
                return new VideoEditAudioHolder(getResView(R.layout.layout_video_edit_audio_item, parent), getActivity(), this);
            case Item.VIDEO_EDIT_AUDIO_MUTE_TYPE /*283*/:
                return new VideoEditAudioMuteHolder(getResView(R.layout.layout_video_edit_audio_mute, parent), getActivity(), this.mSlideVideoAreaCallback, this);
            case Item.VIDEO_EDIT_AUDIO_ICON_TYPE /*284*/:
                return new VideoEditAudioIconHolder(getResView(R.layout.layout_video_edit_audioicon_item, parent), getActivity(), this);
            case Item.VIDEO_EDIT_TEXT_TYPE /*286*/:
                return new VideoEditTextHolder(getResView(R.layout.layout_video_edit_text_item, parent), getActivity(), this);
            case Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE /*288*/:
                return new VideoEditAudioEmptyHolder(getResView(R.layout.layout_video_edit_audio_empty_item, parent), getActivity(), this);
            case Item.VIDEO_EDIT_MUSIC_EDIT_TYPE /*290*/:
                //return new VideoEditMusicEditHolder(getResView(R.layout.layout_video_edit_music_edit, parent), getActivity(), this);
            case Item.VIDEO_EDIT_AUDIO_EDIT_TYPE /*292*/:
                //return new VideoEditAudioEditHolder(getResView(2130968930, parent), getActivity(), this);
            case Item.VIDEO_EDIT_AUDIO_ADD_TYPE /*294*/:
                //return new VideoEditAudioAddHolder(getResView(2130968929, parent), getActivity(), this);
            default:
                return new VideoEditEmptyHolder(new View(getContext()), getActivity(), this);
        }
    }

    public void onBindViewHolder(NewABRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.onBindViewHolder(position);
    }

    public void onBindViewHolder(NewABRecyclerViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getItemViewType(int position) {
        return this.mVideoEditImageEntities.get(position).getViewType();
    }

    public int getItemCount() {
        return this.mVideoEditImageEntities.size();
    }

    public List<VideoEditImageEntity> getAllDatas() {
        return this.mVideoEditImageEntities;
    }

    public void shareAction(VideoEditImageEntity videoEditImageEntity, int duration) {
    }

    public void preloadBitmap(LongVideosModel model, long time, GetBitmapCallback callback) {
        this.manager.getImageCacheUtils().preloadBitmap(model.getOriginalMediaPath(), model.mediaType, time, callback);
    }

    public void loadBitmap(ImageView imageView, LongVideosModel model, long time, GetBitmapCallback callback) {
        this.manager.getSingleBitmapByVideo(imageView, model, time, callback);
    }

    public void onShowSlideText() {
        this.mSlideVideoAreaCallback.onShowSlideText();
    }

    public void onHideSlideText() {
        this.mSlideVideoAreaCallback.onHideSlideText();
    }

    public void onStartSlideVideoRight(int position) {
        this.mSlideVideoAreaCallback.onStartSlideVideoRight(position);
    }

    public void onSlideVideoRight(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideVideoRight(position, time);
    }

    public void onSlideVideoRightEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoRightEnd(position);
    }

    public void onSlideVideoRightAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoRightAutoScrollToLeft(position);
    }

    public void onSlideVideoRightAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoRightAutoScrollToRight(position);
    }

    public void onStartSlideVideoLeft(int position) {
        this.mSlideVideoAreaCallback.onStartSlideVideoLeft(position);
    }

    public void onSlideVideoLeft(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideVideoLeft(position, time);
    }

    public void onSlideVideoLeftEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoLeftEnd(position);
    }

    public void onSlideVideoLeftAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoLeftAutoScrollToLeft(position);
    }

    public void onSlideVideoLeftAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideVideoLeftAutoScrollToRight(position);
    }

    public void getAudioWaveData(View audioWaveView, String filePath, long startTimeUs, long needStartTimeUs, long durationUs, long sumTimeUs, WaveformCacheUtils.WaveDataCallback callback) {
        this.manager.getWaveformCacheUtils().getAudioWaveData(audioWaveView, filePath, startTimeUs, needStartTimeUs, durationUs, sumTimeUs, callback);
    }

    public void getAudioWaveData(View audioWaveView, String filePath, long startTimeUs, long needStartTimeUs, long durationUs, long sumTimeUs, long audioVerseUs, WaveformCacheUtils.WaveDataCallback callback) {
        this.manager.getWaveformCacheUtils().getAudioWaveData(audioWaveView, audioVerseUs, filePath, startTimeUs, durationUs, sumTimeUs, callback);
    }

    public void getAudioWaveData(LargeAudioWaveView audioWaveView, String filePath, long startTimeUs, long needStartTimeUs, long durationUs, long sumTimeUs, long audioVerseUs, WaveformCacheUtils.WaveDataCallback callback) {
        this.manager.getWaveformCacheUtils().getAudioWaveData(audioWaveView, audioVerseUs, filePath, startTimeUs, durationUs, sumTimeUs, callback);
    }

    public float[] justGetAudioWaveDataFromMem(View audioWaveView, String filePath, long startTimeUs, long needStartTimeUs, long durationUs, long sumTimeUs, long audioVerseUs) {
        return this.manager.getWaveformCacheUtils().loadDataFromMemCache(needStartTimeUs, durationUs, sumTimeUs, filePath, audioVerseUs);
    }

    public void onTextEditClick(int position) {
        this.mSlideVideoAreaCallback.onTextEditClick(position);
    }

    public void onTextDeleteClick(int position) {
        this.mSlideVideoAreaCallback.onTextDeleteClick(position);
    }

    public void onSlideTextLeftStart(int position) {
        this.mSlideVideoAreaCallback.onSlideTextLeftStart(position);
    }

    public void onSlideTextLeft(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideTextLeft(position, time);
    }

    public void onSlideTextLeftEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideTextLeftEnd(position);
    }

    public void onSlideTextLeftAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideTextLeftAutoScrollToLeft(position);
    }

    public void onSlideTextLeftAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideTextLeftAutoScrollToRight(position);
    }

    public void onSlideTextRightStart(int position) {
        this.mSlideVideoAreaCallback.onSlideTextRightStart(position);
    }

    public void onSlideTextRight(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideTextRight(position, time);
    }

    public void onSlideTextRightEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideTextRightEnd(position);
    }

    public void onSlideTextRightAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideTextRightAutoScrollToLeft(position);
    }

    public void onSlideTextRightAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideTextRightAutoScrollToRight(position);
    }

    public OnItemClickListener getItemClickListener() {
        return this.onItemClickListener;
    }

    public void onSlideTextContentStart(int position) {
        this.mSlideVideoAreaCallback.onSlideTextContentStart(position);
    }

    public void onSlideTextContent(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideTextContent(position, time);
    }

    public void onSlideTextContentEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideTextContentEnd(position);
    }

    public void onSlideTextContentAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideTextContentAutoScrollToLeft(position);
    }

    public void onSlideTextContentAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideTextContentAutoScrollToRight(position);
    }

    public void setLongPressingStatus(boolean isLongPressing) {
        this.mSlideVideoAreaCallback.setLongPressingStatus(isLongPressing);
    }

    public boolean isMusicEdit() {
        return this.mSlideVideoAreaCallback.isMusicEdit();
    }

    public boolean isHavenInMusicEdit() {
        return this.mSlideVideoAreaCallback.isHavenInMusicEdit();
    }

    public void onStartSlideAudioLeft(int position) {
        this.mSlideVideoAreaCallback.onStartSlideAudioLeft(position);
    }

    public void onSlideAudioLeft(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideAudioLeft(position, time);
    }

    public void onSlideAudioLeftEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftEnd(position);
    }

    public void onSlideAudioLeftAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftAutoScrollToLeft(position);
    }

    public void onSlideAudioLeftAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioLeftAutoScrollToRight(position);
    }

    public void onStartSlideAudioRight(int position) {
        this.mSlideVideoAreaCallback.onStartSlideAudioRight(position);
    }

    public void onSlideAudioRight(int position, int time) {
        this.mSlideVideoAreaCallback.onSlideAudioRight(position, time);
    }

    public void onSlideAudioRightEnd(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightEnd(position);
    }

    public void onSlideAudioRightAutoScrollToLeft(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightAutoScrollToLeft(position);
    }

    public void onSlideAudioRightAutoScrollToRight(int position) {
        this.mSlideVideoAreaCallback.onSlideAudioRightAutoScrollToRight(position);
    }

    public void onAudioContentClick(int position) {
        this.mSlideVideoAreaCallback.changeChildDrawingOrder(position);
    }

    public int getAudioCurSelectPos() {
        return this.mSlideVideoAreaCallback.getAudioCurSelectPos();
    }

    public LongVideosModel getCurSelectAudioModel() {
        return this.mSlideVideoAreaCallback.getCurSelectAudioModel();
    }

    public void onEditAudioEmptyClick(int position) {
        this.mSlideVideoAreaCallback.onEditAudioEmptyClick(position);
    }

    public boolean checkAddTextVisible(int width) {
        return this.mSlideVideoAreaCallback.checkAddTextVisible(width);
    }

    public void onRightAddMusicClick(int position) {
        this.mSlideVideoAreaCallback.onRightAddMusicClick(position);
    }

    public void onLeftAddMusicClick(int position) {
        this.mSlideVideoAreaCallback.onLeftAddMusicClick(position);
    }

    public int getCurAudioScrollX() {
        return this.mSlideVideoAreaCallback.getCurAudioScrollX();
    }

    public long getVideoSumTime() {
        return this.mSlideVideoAreaCallback.getVideoSumTime();
    }

    public boolean isAllVideoMute() {
        return this.manager.isAllVideoMute();
    }

    public boolean isAllAudioMute() {
        return this.manager.isAllAudioMute();
    }

    public boolean isAudioSliding() {
        return this.mSlideVideoAreaCallback.isAudioSliding();
    }

    public boolean hasJustSeeAudioItem() {
        return this.mSlideVideoAreaCallback.hasJustSeeAudioItem();
    }

    public void onMusicVolumeSlideEnd(int adapterPosition, long volumeStartTime, long volumeEndTime, float volume, boolean volumeChanged) {
        this.mSlideVideoAreaCallback.callChangeAudio(adapterPosition, volumeStartTime, volumeEndTime, volume, volumeChanged);
    }
}