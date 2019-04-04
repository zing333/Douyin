package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ChildDrawingOrderCallback;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.VideoTools.FilterView.VideoSpeedup;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.custom.videoedit.VolumeSlideView;
//import com.blink.academy.onetake.custom.videoedit.VolumeSlideView.PressCallback;
//import com.blink.academy.onetake.support.callbacks.ValueAnimatorCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager;
//import com.blink.academy.onetake.support.utils.AnimationUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.NormalBaseAdapter.OnItemClickListener;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditAudioEditHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditMusicEditHolder;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditTextHolder;
//import com.blink.academy.onetake.ui.adapter.videoedit.VideoEditImageAdapter;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.10;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.11;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.12;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.2;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.3;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.4;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.5;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.6;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.7;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.8;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$.Lambda.9;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper$16$.Lambda.1;
//import com.blink.academy.onetake.ui.presenter.ITouchPresenter;
//import com.blink.academy.onetake.ui.presenter.TouchPresenterImpl;
//import com.blink.academy.onetake.widgets.LinearLayout.VideoEditModuleView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.adapter.NormalBaseAdapter;
import cn.nineton.onetake.adapter.VideoEditImageAdapter;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.listener.ITouchPresenter;
import cn.nineton.onetake.listener.ValueAnimatorCallback;
import cn.nineton.onetake.util.AnimationUtil;
import cn.nineton.onetake.util.Constants;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;
import cn.nineton.onetake.util.VideoModelHelper;
import cn.nineton.onetake.util.WaterMarkBitmapUtil;

public class VideoEditHelper {
    private static final int AUDIO_MUTE_INDICATOR_CHANGE = 1;
    private static final int AUDIO_MUTE_INDICATOR_DEFAULT = 0;
    private static final float AUDIO_MUTE_INDICATOR_DEFAULT_BOTTOM_MARGIN = 4.0f;
    private static final int AUDIO_MUTE_INDICATOR_DEFAULT_HEIGHT = 44;
    private static final int AUDIO_MUTE_INDICATOR_HIDE = 0;
    private static final int AUDIO_MUTE_INDICATOR_SHOW = 1;
    public static final BigDecimal IMAGE_SLIDE_MIN_EXPANSION = new BigDecimal(0.1d);
    public static final int EXPANSION_OF_AUTO_SCROLL = BigDecimal.valueOf(0.5d).divide(IMAGE_SLIDE_MIN_EXPANSION, 0, RoundingMode.HALF_UP).intValue();
    private static final int IMAGE_TYPE_FIRST_OFFSET = 2;
    private static final int IMAGE_TYPE_LAST_OFFSET = 2;
    public static final int IMAGE_UNIT_WIDTH = DensityUtil.dip2px(44.0f);
    public static int MIN_AUDIO_TIME_DURATION_MS = 2000;
    private static final int MISSING_FOOTAGE_IV_HALF_WIDTH = DensityUtil.dip2px(10.0f);
    private static final String TAG = VideoEditHelper.class.getSimpleName();
    public static final int TIME_UNIT_MS = 1000;
    public static final int VALUE_SCALE = 1;
    private static final int VOLUME_SLIDE_WIDTH = DensityUtil.dip2px(40.0f);
    private int ALL_MUTE_TV_WIDTH;
    private int AUDIO_ADD_WIDTH;
    private int VIDEO_ADD_WIDTH;
    private int VIDEO_HALF_SCREEN;
    private int VIDEO_MUTE_BUTTON_WIDTH;
    private int VIDEO_MUTE_LEFT_EMPTY_WIDTH;
    private int VIDEO_START_AND_END_EMPTY_WIDTH;
    private View audioMuteIndicator;
    private TextView audioMuteTv;
    private int audioSelectEnd = -1;
    private int audioSelectStart = -1;
    private float audioSelectedEndScrollX;
    private float audioSelectedStartScrollX;
    private View audioVolumeLine;
    private float audioVolumeSlideUnit;
    private VolumeSlideView audioVolumeTouch;
    private long beforeEndTime;
    private long beforeSlideVideoDurationTime = -1;
    private long beforeStartTime;
    private boolean contentHasChanged = false;
    private int curSelectAudioEndPosition = -1;
    int curSelectAudioModelPosition = -1;
    private LongVideosModel curSelectVideo;
    private ArrayList<LongVideosModel.AudioVolume> curSelectedAudioVolumes = new ArrayList();
    private LongVideosModel curTextVideoModel;
    int defaultAudioLineBottomMargin = DensityUtil.dip2px(48.0f);
    int defaultAudioTouchBottomMargin = ((int) (((float) this.defaultAudioLineBottomMargin) - ((((float) DensityUtil.dip2px(35.0f)) * 1.0f) / 2.0f)));
    int defaultVideoLineBottomMargin = DensityUtil.dip2px(86.0f);
    int defaultVideoTouchBottomMargin = ((int) (((float) this.defaultVideoLineBottomMargin) - ((((float) DensityUtil.dip2px(35.0f)) * 1.0f) / 2.0f)));
    private int insertPosition;
    private boolean isAudioChangeSliding = false;
    private boolean isAudioSliding = false;
    private boolean isAudioTouching = false;
    private boolean isAutoScrolling = false;
    private boolean isHavenInMusicEdit;
    private boolean isJumpToNearby;
    private boolean isLongPressing;
    private boolean isMusicEdit;
    private boolean isSlideVideoAutoScrolling;
    private boolean isSliding = false;
    private boolean isTextSliding = false;
    private boolean isTextTouching = false;
    private boolean isTouch = false;
    private int lastInVisibleScrollX = 0;
    private int lastNeedAutoScrollX;
    private int lastNeedScrollX;
    private View leftTouchView;
    private ArrayList<VideoEditImageEntity> mAudioEntities;
    private LinearLayoutManager mAudioLayoutManager;
    private HashMap<LongVideosModel, int[]> mAudioModelsListHashMap;
    private int mAudioMuteIndicatorBottomMargin = DensityUtil.dip2px(AUDIO_MUTE_INDICATOR_DEFAULT_BOTTOM_MARGIN);
    private int mAudioMuteIndicatorHeight = DensityUtil.dip2px(44.0f);
    private RecyclerView mAudioRecyclerView;
    private int mAudioScrollX;
    private ChangeOtherCallback mChangeOtherCallback;
    private Context mContext;
    private LongVideosModel mCurSelectAudioModel;
    private ArrayList<VideoEditImageEntity> mImageEntities;
    private LinearLayoutManager mImagesLayoutManager;
    private RecyclerView mImagesRecyclerView;
    private int mImagesScrollX;
    private int mMaxScrollX;
    private HashMap<LongVideosModel, int[]> mModelListHashMap;
    private SlideVideoAreaCallback mSlideVideoAreaCallback = new SlideVideoAreaCallback() {
        public void onShowSlideText() {
            VideoEditHelper.this.mChangeOtherCallback.onShowSlideText();
        }

        public void onHideSlideText() {
            VideoEditHelper.this.mChangeOtherCallback.onHideSlideText();
        }

        public void onStartSlideVideoRight(int position) {
            VideoEditHelper.this.isSliding = true;
            VideoEditHelper.this.isAudioChangeSliding = true;
            VideoEditHelper.this.contentHasChanged = false;
            VideoEditHelper.this.refreshCacheTextModels();
            VideoEditHelper.this.refreshCacheAudioTextModels();
            LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mImageEntities.get(position)).getLongVideosModel();
            if (longVideosModel != null) {
                VideoEditHelper.this.beforeSlideVideoDurationTime = longVideosModel.getCurrentDuration();
            } else {
                VideoEditHelper.this.beforeSlideVideoDurationTime = -1;
            }
        }

        public void onSlideVideoRight(int position, int time) {
            time = VideoEditHelper.this.dealRightPosition(position, time, false);
            if (time != 0) {
                VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.mTextScrollX);
            }
            if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                VideoEditHelper.this.contentHasChanged = true;
            }
        }

        public void onSlideVideoRightEnd(int position) {
            VideoEditHelper.this.isJumpToNearby = true;
            VideoEditHelper.this.isSlideVideoAutoScrolling = false;
            VideoEditHelper.this.isAudioChangeSliding = false;
            VideoEditHelper.this.measureSelectedStartAndEndX();
            LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mImageEntities.get(position)).getLongVideosModel();
            boolean audioChanged = false;
            if (VideoEditHelper.this.getVideoLongVideosModels().get(VideoEditHelper.this.getVideoLongVideosModels().size() - 1) == longVideosModel && VideoEditHelper.this.beforeSlideVideoDurationTime != -1) {
                long changeDuration = longVideosModel.getCurrentDuration() - VideoEditHelper.this.beforeSlideVideoDurationTime;
                if (changeDuration > Constants.VIDEO_BLACK_DURATION) {
                    int audioSize = VideoEditHelper.this.getAudioLongVideoModels().size();
                    if (audioSize > 0) {
                        LongVideosModel end = (LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(audioSize - 1);
                        if (end.isJustSeeForAudio() && audioSize - 2 >= 0) {
                            LongVideosModel before = (LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(audioSize - 2);
                            if (end.getAudioStartTime() > before.getAudioStartTime() + before.getAudioDuration()) {
                                end.setJustSeeForAudio(false);
                                end.setAudioDuration(changeDuration);
                                audioChanged = true;
                            }
                        }
                    }
                }
            }
            boolean hasRemoved = VideoEditHelper.this.removeInvalidAudioModel();
            if (audioChanged || hasRemoved) {
                VideoEditHelper.this.refreshAudioView();
            }
            VideoEditHelper.this.callVideoChangeAfterSlide(-1, longVideosModel);
            if (VideoEditHelper.this.contentHasChanged) {
                VideoEditHelper.this.onSlideTouchUp();
                VideoEditHelper.this.contentHasChanged = false;
            }
            VideoEditHelper.this.removeInvalidTextModelAndRefresh();
            final int imageEndPos = VideoEditHelper.this.mImageEntities.size() - 1;
            final VideoEditImageEntity imageEnd = (VideoEditImageEntity) VideoEditHelper.this.mImageEntities.get(imageEndPos);
            if (imageEnd.getEmpty_type_width() > VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                final int needReduceWidth = imageEnd.getEmpty_type_width() - VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH;
                final int timeEndPos = VideoEditHelper.this.mTimesEntities.size() - 1;
                final VideoEditImageEntity timeEnd = (VideoEditImageEntity) VideoEditHelper.this.mTimesEntities.get(timeEndPos);
                final int audioEndPos = VideoEditHelper.this.mAudioEntities.size() - 1;
                final VideoEditImageEntity audioEnd = (VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(audioEndPos);
                VideoEditImageEntity textEndTemp = null;
                if (VideoEditHelper.this.mTextEntities != null && VideoEditHelper.this.mTextEntities.size() > 0) {
                    textEndTemp = (VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(VideoEditHelper.this.mTextEntities.size() - 1);
                }
                final VideoEditImageEntity textEnd = textEndTemp;
                final int i = position;
                AnimationUtil.setValueAnimator(false, new ValueAnimatorCallback() {
                    public void updateCallback(float value) {
                        VideoEditHelper.this.findVideoDurationTvShowRect(VideoEditHelper.this.curSelectVideo);
                        if (VideoEditHelper.this.needCheckMissingFootage) {
                            VideoEditHelper.this.refreshMissingFootage();
                        }
                        imageEnd.setEmpty_type_width(VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH + ((int) (((float) needReduceWidth) * value)));
                        int time_empty_type_width = ((VideoEditHelper.this.screenWidth / 2) + imageEnd.getEmpty_type_width()) - VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH;
                        timeEnd.setEmpty_type_width(time_empty_type_width);
                        //LogUtil.d(VideoEditHelper.TAG, String.format("onSlideVideoRightEnd image_empty_type_width : %s , time_empty_type_width : %s ", new Object[]{Integer.valueOf(image_empty_type_width), Integer.valueOf(time_empty_type_width)}));
                        if (textEnd != null) {
                            textEnd.setEmpty_type_width(time_empty_type_width);
                        }
                        audioEnd.setEmpty_type_width(time_empty_type_width);
                        if (VideoEditHelper.this.mTextEntities != null && VideoEditHelper.this.mTextEntities.size() > 0) {
                            VideoEditHelper.this.mVideoEditTextAdapter.notifyItemChanged(VideoEditHelper.this.mTextEntities.size() - 1);
                        }
                        VideoEditHelper.this.mVideoEditAudioAdapter.notifyItemChanged(audioEndPos);
                        VideoEditHelper.this.mVideoEditImageAdapter.notifyItemChanged(imageEndPos);
                        VideoEditHelper.this.mVideoEditTimesAdapter.notifyItemChanged(timeEndPos);
                    }

                    public void startCallback() {
                    }

                    public void endCallback() {
                        VideoEditHelper.this.dealScrollXWhenSlideEnd(i, true);
                    }
                });
            } else {
                VideoEditHelper.this.dealScrollXWhenSlideEnd(position, true);
            }
            VideoEditHelper.this.isSliding = false;
        }

        public void onSlideVideoRightAutoScrollToLeft(int position) {
            int time = VideoEditHelper.this.dealRightPosition(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL, true);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideVideoRightAutoScrollToLeft position : %s , needScrollX : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(needScrollX)}));
            if (needScrollX != 0) {
                VideoEditHelper.this.calculateExactlyScrollX();
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideVideoRightAutoScrollToRight(int position) {
            if (!VideoEditHelper.this.isSlideVideoAutoScrolling) {
                VideoEditHelper.this.isSlideVideoAutoScrolling = true;
            }
            int time = VideoEditHelper.this.dealRightPosition(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL, true);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideVideoRightAutoScrollToRight position : %s , needScrollX : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(needScrollX)}));
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onStartSlideVideoLeft(int position) {
            VideoEditHelper.this.isSliding = true;
            VideoEditHelper.this.isAudioChangeSliding = true;
            VideoEditHelper.this.contentHasChanged = false;
            VideoEditHelper.this.refreshCacheTextModels();
        }

        public void onSlideVideoLeft(int position, int time) {
            time = VideoEditHelper.this.dealLeftPosition(position, time);
            if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                VideoEditHelper.this.contentHasChanged = true;
            }
        }

        public void onSlideVideoLeftEnd(int position) {
            VideoEditHelper.this.isJumpToNearby = true;
            VideoEditHelper.this.isAudioChangeSliding = false;
            VideoEditHelper.this.measureSelectedStartAndEndX();
            if (VideoEditHelper.this.removeInvalidAudioModel()) {
                VideoEditHelper.this.refreshAudioView();
            }
            VideoEditHelper.this.callVideoChangeAfterSlide(-1, ((VideoEditImageEntity) VideoEditHelper.this.mImageEntities.get(position)).getLongVideosModel());
            if (VideoEditHelper.this.contentHasChanged) {
                VideoEditHelper.this.onSlideTouchUp();
                VideoEditHelper.this.contentHasChanged = false;
            }
            VideoEditHelper.this.removeInvalidTextModelAndRefresh();
            final VideoEditImageEntity imageHead = (VideoEditImageEntity) VideoEditHelper.this.mImageEntities.get(0);
            if (imageHead.getEmpty_type_width() > VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                final int needReduceWidth = imageHead.getEmpty_type_width() - VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH;
                VideoEditImageEntity textHeadtemp = null;
                if (VideoEditHelper.this.mTextEntities.size() > 0) {
                    textHeadtemp = (VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(0);
                }
                final VideoEditImageEntity timeHead = (VideoEditImageEntity) VideoEditHelper.this.mTimesEntities.get(0);
                final VideoEditImageEntity audioHead = (VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(0);
                final VideoEditImageEntity textHead = textHeadtemp;
                final int i = position;
                AnimationUtil.setValueAnimator(false, new ValueAnimatorCallback() {
                    public void updateCallback(float value) {
                        VideoEditHelper.this.findVideoDurationTvShowRect(VideoEditHelper.this.curSelectVideo);
                        if (VideoEditHelper.this.needCheckMissingFootage) {
                            VideoEditHelper.this.refreshMissingFootage();
                        }
                        imageHead.setEmpty_type_width(VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH + ((int) (((float) needReduceWidth) * value)));
                        int changeWidth = imageHead.getEmpty_type_width() - VideoEditHelper.this.VIDEO_START_AND_END_EMPTY_WIDTH;
                        int time_empty_type_width = (VideoEditHelper.this.screenWidth / 2) + changeWidth;
                        timeHead.setEmpty_type_width(time_empty_type_width);
                        if (textHead != null) {
                            textHead.setEmpty_type_width(time_empty_type_width);
                        }
                        audioHead.setEmpty_type_width(imageHead.getEmpty_type_width());
                       // LogUtil.d(VideoEditHelper.TAG, String.format("onSlideVideoLeftEnd image_empty_type_width : %s , time_empty_type_width : %s ", new Object[]{Integer.valueOf(image_empty_type_width), Integer.valueOf(time_empty_type_width)}));
                        if (textHead != null) {
                            VideoEditHelper.this.mVideoEditTextAdapter.notifyItemChanged(0);
                        }
                        VideoEditHelper.this.mVideoEditImageAdapter.notifyItemChanged(0);
                        VideoEditHelper.this.mVideoEditTimesAdapter.notifyItemChanged(0);
                        VideoEditHelper.this.mVideoEditAudioAdapter.notifyItemChanged(0);
                        if (VideoEditHelper.this.mChangeOtherCallback != null) {
                            VideoEditHelper.this.mChangeOtherCallback.addVideoAddMusicTvLeftMargin(changeWidth);
                        }
                    }

                    public void startCallback() {
                    }

                    public void endCallback() {
                        VideoEditHelper.this.dealScrollXWhenSlideEnd(i, false);
                    }
                });
            } else {
                VideoEditHelper.this.dealScrollXWhenSlideEnd(position, false);
            }
            VideoEditHelper.this.isSliding = false;
        }

        public void onSlideVideoLeftAutoScrollToLeft(int position) {
            VideoEditHelper.this.calculateExactlyScrollX();
            int time = VideoEditHelper.this.dealLeftPosition(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideVideoLeftAutoScrollToRight(int position) {
            VideoEditHelper.this.calculateExactlyScrollX();
            int time = VideoEditHelper.this.dealLeftPosition(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL * -1);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onTextEditClick(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = VideoEditHelper.this.mTextEntities.get(position).getLongVideosModel();
                if (VideoEditHelper.this.mChangeOtherCallback != null) {
                    VideoEditHelper.this.mChangeOtherCallback.onVideoEditTextClick(longVideosModel);
                }
            }
        }

        public void onTextDeleteClick(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = VideoEditHelper.this.mTextEntities.get(position).getLongVideosModel();
                List<LongVideosModel> textVideosModels = VideoEditHelper.this.getTextVideosModels();
                textVideosModels.remove(longVideosModel);
                VideoEditHelper.this.refreshTextView(textVideosModels, null, true);
                if (VideoEditHelper.this.mChangeOtherCallback != null) {
                    VideoEditHelper.this.mChangeOtherCallback.refreshBottomCoverViewVisibility(false);
                }
                VideoEditHelper.this.onSlideTouchUp();
            }
        }

        public void onSlideTextLeftStart(int position) {
            VideoEditHelper.this.contentHasChanged = false;
            VideoEditHelper.this.prepareNearbyJumpData();
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(position)).getLongVideosModel();
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
            }
        }

        public void onSlideTextLeft(int position, int time) {
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideTextLeft position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
            time = VideoEditHelper.this.dealSlideTextLeft(position, time);
            if (time != 0) {
                VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.mTextScrollX);
            }
            if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                VideoEditHelper.this.contentHasChanged = true;
            }
        }

        public void onSlideTextLeftEnd(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(position)).getLongVideosModel();
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                VideoEditHelper.this.refreshEndRelateAndRefresh();
                if (VideoEditHelper.this.contentHasChanged) {
                    VideoEditHelper.this.onSlideTouchUp();
                    VideoEditHelper.this.contentHasChanged = false;
                }
            }
        }

        public void onSlideTextLeftAutoScrollToLeft(int position) {
            int time = VideoEditHelper.this.dealSlideTextLeft(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideTextLeftAutoScrollToRight(int position) {
            int time = VideoEditHelper.this.dealSlideTextLeft(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideTextRightStart(int position) {
            VideoEditHelper.this.contentHasChanged = false;
            VideoEditHelper.this.prepareNearbyJumpData();
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(position)).getLongVideosModel();
                longVideosModel.setUsageDurationValue(longVideosModel.getCurrentDurationValue());
            }
        }

        public void onSlideTextRight(int position, int time) {
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideTextRight position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
            time = VideoEditHelper.this.dealSlideTextRight(position, time);
            if (time != 0) {
                VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.mTextScrollX);
            }
            if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                VideoEditHelper.this.contentHasChanged = true;
            }
        }

        public void onSlideTextRightEnd(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(position)).getLongVideosModel();
                longVideosModel.setUsageDurationValue(longVideosModel.getCurrentDurationValue());
                VideoEditHelper.this.refreshEndRelateAndRefresh();
                if (VideoEditHelper.this.contentHasChanged) {
                    VideoEditHelper.this.onSlideTouchUp();
                    VideoEditHelper.this.contentHasChanged = false;
                }
            }
        }

        public void onSlideTextRightAutoScrollToLeft(int position) {
            int time = VideoEditHelper.this.dealSlideTextRight(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideTextRightAutoScrollToLeft time : %s , needScrollX : %s ", new Object[]{Integer.valueOf(time), Integer.valueOf(needScrollX)}));
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideTextRightAutoScrollToRight(int position) {
            int time = VideoEditHelper.this.dealSlideTextRight(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideTextContentStart(int position) {
            VideoEditHelper.this.contentHasChanged = false;
            VideoEditHelper.this.prepareNearbyJumpData();
        }

        public void onSlideTextContent(int position, int time) {
            if (!VideoEditHelper.this.checkTextPositionIsValid(position)) {
                return;
            }
            if (VideoEditHelper.this.curTextVideoModel == null || ((VideoEditImageEntity) VideoEditHelper.this.mTextEntities.get(position)).getLongVideosModel() == VideoEditHelper.this.curTextVideoModel) {
                time = VideoEditHelper.this.dealSlideTextContent(position, time, false);
                if (time != 0) {
                    VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.mTextScrollX);
                }
                if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                    VideoEditHelper.this.contentHasChanged = true;
                }
            }
        }

        public void onSlideTextContentEnd(int position) {
            VideoEditHelper.this.refreshEndRelateAndRefresh();
            if (VideoEditHelper.this.contentHasChanged) {
                VideoEditHelper.this.onSlideTouchUp();
                VideoEditHelper.this.contentHasChanged = false;
            }
        }

        public void onSlideTextContentAutoScrollToLeft(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                VideoEditHelper.this.calculateExactlyTextScrollX();
                int time = VideoEditHelper.this.dealSlideTextContent(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL, true);
                if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                    VideoEditHelper.this.contentHasChanged = true;
                }
                int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
                LogUtil.d(VideoEditHelper.TAG, String.format("onSlideTextContentAutoScrollToLeft time : %s , needScrollX : %s ", new Object[]{Integer.valueOf(time), Integer.valueOf(needScrollX)}));
                if (needScrollX != 0) {
                    VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
                }
            }
        }

        public void onSlideTextContentAutoScrollToRight(int position) {
            if (VideoEditHelper.this.checkTextPositionIsValid(position)) {
                VideoEditHelper.this.calculateExactlyTextScrollX();
                boolean isSpecial = false;
                int firstVisibleItemPosition = VideoEditHelper.this.mTextLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != -1 && firstVisibleItemPosition == position) {
                    isSpecial = true;
                }
                int time = VideoEditHelper.this.dealSlideTextContent(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL, true);
                if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                    VideoEditHelper.this.contentHasChanged = true;
                }
                final int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
                LogUtil.d(VideoEditHelper.TAG, String.format("onSlideTextContentAutoScrollToRight time : %s , needScrollX : %s ", new Object[]{Integer.valueOf(time), Integer.valueOf(needScrollX)}));
                if (needScrollX == 0) {
                    return;
                }
                if (isSpecial) {
                    //VideoEditHelper.this.mTextRecyclerView.post(1.lambdaFactory$(this, needScrollX));
                    mTextRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            lambda$onSlideTextContentAutoScrollToRight$0(needScrollX);
                        }
                    });
                } else {
                    VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
                }
            }
        }

        private /* synthetic */ void lambda$onSlideTextContentAutoScrollToRight$0(int needScrollX) {
            VideoEditHelper.this.calculateExactlyTextScrollX();
            VideoEditHelper.this.relateScroll(VideoEditHelper.this.mImagesScrollX, 0);
            VideoEditHelper.this.mImagesRecyclerView.scrollBy(needScrollX, 0);
        }

        public void setLongPressingStatus(boolean isLongPressing) {
            VideoEditHelper.this.setLongPressing(isLongPressing);
        }

        public boolean isMusicEdit() {
            return VideoEditHelper.this.isMusicEdit;
        }

        public boolean isHavenInMusicEdit() {
            return VideoEditHelper.this.isHavenInMusicEdit;
        }

        public void onStartSlideAudioLeft(int position) {
            VideoEditHelper.this.cacheAudioModelsData();
            VideoEditHelper.this.prepareNearbyJumpData();
            LongVideosModel longVideosModel = (LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(position);
            if (longVideosModel != null) {
                longVideosModel.setUsageStartTime(longVideosModel.getAudioStartTime());
                VideoEditHelper.this.isAudioChangeSliding = true;
                VideoEditHelper.this.isAudioSliding = true;
                VideoEditHelper.this.contentHasChanged = false;
            }
        }

        public void onSlideAudioLeft(int position, int time) {
            time = VideoEditHelper.this.dealSlideAudioLeft(position, time);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            if (((LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(position)) != null && position == 0) {
                VideoEditHelper.this.refreshAddMusicView();
            }
        }

        public void onSlideAudioLeftEnd(int position) {
            VideoEditHelper.this.isAudioChangeSliding = false;
            VideoEditHelper.this.isAudioSliding = false;
            VideoEditHelper.this.cacheAudioModelsData();
            VideoEditHelper.this.removeInvalidAudioModel();
            VideoEditHelper.this.refreshAudioView();
            VideoEditHelper.this.measureAudioSelectedStartAndEndX();
            VideoEditHelper.this.updateAudioSlideView(position);
            VideoEditHelper.this.callAudioChange();
            if (VideoEditHelper.this.contentHasChanged) {
                VideoEditHelper.this.onSlideTouchUp();
                VideoEditHelper.this.contentHasChanged = false;
            }
        }

        public void onSlideAudioLeftAutoScrollToLeft(int position) {
            int time = VideoEditHelper.this.dealSlideAudioLeft(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mAudioRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideAudioLeftAutoScrollToRight(int position) {
            int time = VideoEditHelper.this.dealSlideAudioLeft(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mAudioRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onStartSlideAudioRight(int position) {
            VideoEditHelper.this.cacheAudioModelsData();
            VideoEditHelper.this.prepareNearbyJumpData();
            if (position >= 0 && position < VideoEditHelper.this.getAudioLongVideoModels().size()) {
                LongVideosModel longVideosModel = (LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(position);
                if (longVideosModel != null) {
                    longVideosModel.setUsageDurationValue(longVideosModel.getAudioDuration());
                    VideoEditHelper.this.isAudioChangeSliding = true;
                    VideoEditHelper.this.isAudioSliding = true;
                    VideoEditHelper.this.contentHasChanged = false;
                }
            }
        }

        public void onSlideAudioRight(int position, int time) {
            time = VideoEditHelper.this.dealSlideAudioRight(position, time);
            if (!VideoEditHelper.this.contentHasChanged && time != 0) {
                VideoEditHelper.this.contentHasChanged = true;
            }
        }

        public void onSlideAudioRightEnd(int position) {
            VideoEditHelper.this.isAudioChangeSliding = false;
            VideoEditHelper.this.isAudioSliding = false;
            VideoEditHelper.this.cacheAudioModelsData();
            VideoEditHelper.this.removeInvalidAudioModel();
            VideoEditHelper.this.refreshAudioView();
            VideoEditHelper.this.measureAudioSelectedStartAndEndX();
            VideoEditHelper.this.updateAudioSlideView(position);
            VideoEditHelper.this.callAudioChange();
            if (VideoEditHelper.this.contentHasChanged) {
                VideoEditHelper.this.onSlideTouchUp();
                VideoEditHelper.this.contentHasChanged = false;
            }
        }

        public void onSlideAudioRightAutoScrollToLeft(int position) {
            int time = VideoEditHelper.this.dealSlideAudioRight(position, VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            LogUtil.d(VideoEditHelper.TAG, String.format("onSlideAudioRightAutoScrollToLeft time : %s , needScrollX : %s ", new Object[]{Integer.valueOf(time), Integer.valueOf(needScrollX)}));
            if (needScrollX != 0) {
                VideoEditHelper.this.mAudioRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void onSlideAudioRightAutoScrollToRight(int position) {
            int time = VideoEditHelper.this.dealSlideAudioRight(position, -VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
            if (!(VideoEditHelper.this.contentHasChanged || time == 0)) {
                VideoEditHelper.this.contentHasChanged = true;
            }
            int needScrollX = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(0, RoundingMode.HALF_UP).intValue();
            if (needScrollX != 0) {
                VideoEditHelper.this.mAudioRecyclerView.scrollBy(needScrollX, 0);
            }
        }

        public void changeChildDrawingOrder(int position) {
            VideoEditHelper.this.onAudioItemClick(position);
        }

        public int getAudioCurSelectPos() {
            return VideoEditHelper.this.curSelectAudioModelPosition;
        }

        public LongVideosModel getCurSelectAudioModel() {
            return VideoEditHelper.this.getCurSelectAudioModel();
        }

        public void onEditAudioEmptyClick(int position) {
            VideoEditImageEntity entity = (VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(position);
            if (entity != null && BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE == entity.getViewType() && VideoEditHelper.this.mChangeOtherCallback != null) {
                VideoEditHelper.this.mChangeOtherCallback.onAddMusicClick(-1);
            }
        }

        public boolean checkAddTextVisible(int width) {
            if (VideoEditHelper.this.mChangeOtherCallback != null) {
                return VideoEditHelper.this.mChangeOtherCallback.checkAddTextVisible((float) width);
            }
            return false;
        }

        public void onRightAddMusicClick(int position) {
            if (VideoEditHelper.this.checkListPositionIsValid(VideoEditHelper.this.mAudioEntities, position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(position)).getLongVideosModel();
                if (longVideosModel != null) {
                    int indexOf = VideoEditHelper.this.getAudioLongVideoModels().indexOf(longVideosModel);
                    if (VideoEditHelper.this.mChangeOtherCallback != null) {
                        VideoEditHelper.this.mChangeOtherCallback.onAddMusicClick(indexOf + 1);
                    }
                }
            }
        }

        public void onLeftAddMusicClick(int position) {
            if (VideoEditHelper.this.checkListPositionIsValid(VideoEditHelper.this.mAudioEntities, position)) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(position)).getLongVideosModel();
                if (longVideosModel != null) {
                    int indexOf = VideoEditHelper.this.getAudioLongVideoModels().indexOf(longVideosModel);
                    if (VideoEditHelper.this.mChangeOtherCallback != null) {
                        VideoEditHelper.this.mChangeOtherCallback.onAddMusicClick(indexOf);
                    }
                }
            }
        }

        public int getCurAudioScrollX() {
            return VideoEditHelper.this.mAudioScrollX;
        }

        public long getVideoSumTime() {
            return Float.valueOf(VideoEditHelper.this.sumTime).longValue();
        }

        public void onAudioMuteClick(boolean mute) {
            VideoEditHelper.this.manager.setAllAudioMute(mute);
            VideoEditHelper.this.refreshAudioMuteTvPos();
            if (!mute) {
                VideoEditHelper.this.refreshVisibleAudioItem();
            } else if (!VideoEditHelper.this.clearCurAudioSelected()) {
                VideoEditHelper.this.refreshVisibleAudioItem();
            }
            if (VideoEditHelper.this.mChangeOtherCallback != null) {
                VideoEditHelper.this.mChangeOtherCallback.onAudioMuteClick(mute, VideoEditHelper.this.isMusicEdit);
            }
            VideoEditHelper.this.onSwitchMute();
        }

        public Activity getActivity() {
            if (VideoEditHelper.this.mContext instanceof Activity) {
                return (Activity) VideoEditHelper.this.mContext;
            }
            return null;
        }

        public boolean isAudioSliding() {
            return VideoEditHelper.this.isAudioChangeSliding;
        }

        public boolean hasJustSeeAudioItem() {
            List<LongVideosModel> audioLongVideoModels = VideoEditHelper.this.getAudioLongVideoModels();
            if (TextUtil.isValidate(audioLongVideoModels) && ((LongVideosModel) audioLongVideoModels.get(audioLongVideoModels.size() - 1)).isJustSeeForAudio()) {
                return true;
            }
            return false;
        }

        public void callChangeAudio(int adapterPosition, long volumeStart, long volumeEnd, float volumeValue, boolean volumeChanged) {
            int size = VideoEditHelper.this.mAudioEntities.size();
            if (adapterPosition >= 0 && adapterPosition < size) {
                Iterator it;
                long endTime;
                if (volumeStart != volumeEnd && volumeValue != StaticLayoutUtil.DefaultSpacingadd) {
                    long sumTime = 0;
                    for (LongVideosModel model : VideoEditHelper.this.getVideoLongVideosModels()) {
                        long startTime = sumTime;
                        sumTime += model.getCurrentDuration();
                        endTime = sumTime;
                        if (startTime == volumeStart && endTime == volumeEnd) {
                            model.setAudioVolume(volumeValue);
                        } else if (volumeEnd < startTime) {
                            break;
                        }
                    }
                }
                LongVideosModel longVideosModel = ((VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(adapterPosition)).getLongVideosModel();
                if (longVideosModel != null) {
                    ArrayList<LongVideosModel.AudioVolume> audioVolumes = longVideosModel.getAudioVolumes();
                    if (TextUtil.isValidate(audioVolumes)) {
                        boolean isAllMute = true;
                        it = audioVolumes.iterator();
                        while (it.hasNext()) {
                            if (((LongVideosModel.AudioVolume) it.next()).getVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                                isAllMute = false;
                                break;
                            }
                        }
                        boolean changed = false;
                        if (isAllMute) {
                            int size1 = VideoEditHelper.this.getAudioLongVideoModels().size();
                            if (size1 > 1) {
                                if (size1 != 2) {
                                    VideoEditHelper.this.getAudioLongVideoModels().remove(longVideosModel);
                                    changed = true;
                                    VideoEditHelper.this.clearCurSelectAudioAndRefreshTouchView();
                                } else if (!((LongVideosModel) VideoEditHelper.this.getAudioLongVideoModels().get(size1 - 1)).isJustSeeForAudio()) {
                                    VideoEditHelper.this.getAudioLongVideoModels().remove(longVideosModel);
                                    changed = true;
                                    VideoEditHelper.this.clearCurSelectAudioAndRefreshTouchView();
                                }
                            }
                        } else {
                            LongVideosModel.AudioVolume first = (LongVideosModel.AudioVolume) audioVolumes.get(0);
                            if (first.getVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                                endTime = first.getEndTime();
                                long duration = first.getDuration();
                                longVideosModel.setAudioStartTime(endTime);
                                longVideosModel.setAudioDuration(longVideosModel.getAudioDuration() - duration);
                                audioVolumes.remove(first);
                                changed = true;
                                LongVideosModel.AudioVolume next = (LongVideosModel.AudioVolume) audioVolumes.get(0);
                                next.setSelected(true);
                                VideoEditHelper.this.refreshAudioSelectPos(VideoEditHelper.this.mAudioEntities, next, next);
                            }
                            LongVideosModel.AudioVolume end = (LongVideosModel.AudioVolume) audioVolumes.get(audioVolumes.size() - 1);
                            if (end.getVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                                longVideosModel.setAudioDuration(longVideosModel.getAudioDuration() - end.getDuration());
                                audioVolumes.remove(end);
                                changed = true;
                            }
                        }
                        if (changed) {
                            VideoEditHelper.this.removeInvalidAudioModel();
                            VideoEditHelper.this.refreshAudioView(VideoEditHelper.this.getAudioLongVideoModels());
                            VideoEditHelper.this.measureAudioSelectedStartAndEndX();
                            VideoEditHelper.this.updateAudioSlideView(VideoEditHelper.this.curSelectAudioModelPosition);
                            VideoEditHelper.this.refreshAudioSelectPos();
                        }
                    }
                }
                VideoEditHelper.this.callAudioChange();
                if (volumeChanged) {
                    VideoEditHelper.this.onSlideTouchUp();
                }
            }
        }
    };
    private ArrayList<VideoEditImageEntity> mTextEntities;
    private LinearLayoutManager mTextLayoutManager;
    private RecyclerView mTextRecyclerView;
    private int mTextScrollX;
    private ArrayList<VideoEditImageEntity> mTimesEntities;
    private LinearLayoutManager mTimesLayoutManager;
    private RecyclerView mTimesRecyclerView;
    private int mTimesScrollX;
    private ITouchPresenter mTouchPresenter;
    private VideoEditImageAdapter mVideoEditAudioAdapter;
    private VideoEditImageAdapter mVideoEditImageAdapter;
    private FrameLayout mVideoEditMissingFootageFl;
    private VideoEditImageAdapter mVideoEditTextAdapter;
    private VideoEditImageAdapter mVideoEditTimesAdapter;
    private int mVideoMuteIndicatorBottomMargin = DensityUtil.dip2px(57.0f);
    private int mVideoMuteIndicatorHeight = DensityUtil.dip2px(30.0f);
    private VideoAudioPlaybackManager manager;
    private List<ImageView> missingFootageIvs;
    private boolean needCheckMissingFootage;
    private ArrayList<Integer> needHideLeftArrowPos = new ArrayList();
    private int needResetOutScrollX;
    private boolean needResetOutTextModel = false;
    private View rightTouchView;
    private int screenWidth;
    private float selectedEndScrollX;
    private float selectedStartScrollX;
    private float sumTime = StaticLayoutUtil.DefaultSpacingadd;
    private ArrayList<Long> timeList = new ArrayList();
    private TextView videoDurationTv;
    private View videoMuteIndicator;
    private View videoMuteIndicatorCover;
    private TextView videoMuteTv;
    private int videoSelectEnd = -1;
    private int videoSelectStart = -1;
    private View videoVolumeLine;
    private float videoVolumeSlideUnit;
    private VolumeSlideView videoVolumeTouch;

    public interface ChangeOtherCallback {
        void addVideoAddMusicTvLeftMargin(int i);

        void animationJumpToMusicEdit();

        void callAudioChange();

        void callSetVideoPause();

        void callVideoChange(long j);

        void callVideoChangeAfterSlide(long j, LongVideosModel longVideosModel);

        void callVideoChangeAfterSplit(long j);

        void callVideoEditSwitchTabsVisibility(boolean z);

        boolean checkAddTextVisible(float f);

        int getVideoAddMusicTvWidth();

        void onAddMusicClick(int i);

        void onAddVideosClick(int i);

        void onAudioMuteClick(boolean z, boolean z2);

        void onHideSlideText();

        boolean onNeedAddOneMissingFootageIv();

        void onShowSlideText();

        void onVideoEditSaveToDraft();

        void onVideoEditTextClick(LongVideosModel longVideosModel);

        void onVideoModelChange(LongVideosModel longVideosModel);

        void onVideoMuteClick(boolean z, boolean z2);

        void onVideoSelectAreaShownOrHide(boolean z, boolean z2, LongVideosModel longVideosModel);

        void openVideoMusicStore();

        void refreshAudioEditButton();

        void refreshBottomCoverViewVisibility(boolean z);

        void refreshCuTextModel(LongVideosModel longVideosModel, boolean z);

        void refreshVideoAddMusicTv(int i, int i2);

        void refreshVideoAddMusicTvVisible(int i);

        void setSplitBtnVisible(boolean z, boolean z2, boolean z3);

        void setVideoSeekTo(float f);

        void setVideoSlideBarWidthMargin(int i, int i2);

        void videoTimeChange();
    }

    public interface SlideVideoAreaCallback {
        void callChangeAudio(int i, long j, long j2, float f, boolean z);

        void changeChildDrawingOrder(int i);

        boolean checkAddTextVisible(int i);

        Activity getActivity();

        int getAudioCurSelectPos();

        int getCurAudioScrollX();

        LongVideosModel getCurSelectAudioModel();

        long getVideoSumTime();

        boolean hasJustSeeAudioItem();

        boolean isAudioSliding();

        boolean isHavenInMusicEdit();

        boolean isMusicEdit();

        void onAudioMuteClick(boolean z);

        void onEditAudioEmptyClick(int i);

        void onHideSlideText();

        void onLeftAddMusicClick(int i);

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

        void setLongPressingStatus(boolean z);
    }

    private List<LongVideosModel> getVideoLongVideosModels() {
        if (this.manager != null) {
            return this.manager.getVideosModelList();
        }
        return new ArrayList();
    }

    private List<LongVideosModel> getAudioLongVideoModels() {
        if (this.manager != null) {
            return this.manager.getMusicModelList();
        }
        return new ArrayList();
    }

    private List<LongVideosModel> getTextVideosModels() {
        if (this.manager != null) {
            return this.manager.getTextModelList();
        }
        return new ArrayList();
    }

    public void setMissingFootageIvs(List<ImageView> missingFootageIvs) {
        this.missingFootageIvs = missingFootageIvs;
        this.needCheckMissingFootage = true;
    }

    public boolean isInTextSelectStatus() {
        return this.curTextVideoModel != null;
    }

    public int getCurSelectVideoPosition() {
        if (this.curSelectVideo != null) {
            return getVideoLongVideosModels().indexOf(this.curSelectVideo);
        }
        return 0;
    }

    public boolean isCanShowTextRV() {
        return getTextVideosModels() != null && getTextVideosModels().size() > 0;
    }

    private LongVideosModel getCurSelectVideo() {
        return this.curSelectVideo;
    }

    public VideoEditHelper(Context context, RecyclerView imagesRecyclerView, RecyclerView timesRecyclerView, RecyclerView audioRecyclerView, RecyclerView textRecyclerView, TextView videoDurationTv, View videoVolumeLine, VolumeSlideView videoVolumeTouch, View leftTouchVie, View rightTouchView, View audioVolumeLine, VolumeSlideView audioVolumeTouch, int screenWidth, VideoAudioPlaybackManager videoAudioPlaybackManager, View videoMuteIndicator, View videoMuteIndicatorCover, View audioMuteIndicator, TextView videoMuteTv, TextView audioMuteTv, FrameLayout videoEditMissingFootageFl, ChangeOtherCallback callback) {
        this.mContext = context;
        this.mImagesRecyclerView = imagesRecyclerView;
        this.screenWidth = screenWidth;
        this.manager = videoAudioPlaybackManager;
        this.mChangeOtherCallback = callback;
        this.VIDEO_HALF_SCREEN = screenWidth / 2;
        this.VIDEO_MUTE_BUTTON_WIDTH = DensityUtil.dip2px(59.0f);
        this.VIDEO_ADD_WIDTH = IMAGE_UNIT_WIDTH + DensityUtil.dip2px(3.0f);//(VideoEditImageHolder.LEFT_OR_RIGHT_SPACE * 2);
        this.VIDEO_START_AND_END_EMPTY_WIDTH = this.VIDEO_HALF_SCREEN - this.VIDEO_ADD_WIDTH;
        this.VIDEO_MUTE_LEFT_EMPTY_WIDTH = this.VIDEO_HALF_SCREEN - this.VIDEO_MUTE_BUTTON_WIDTH;
        this.AUDIO_ADD_WIDTH = VideoEditModuleView.IMAGE_UNIT_WIDTH + DensityUtil.dip2px(3.0f);// VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        this.ALL_MUTE_TV_WIDTH = WaterMarkBitmapUtil.getTextWidth(videoMuteTv.getText().toString(), videoMuteTv.getPaint(), new Rect());
        initImagesView(screenWidth);
        this.mTimesRecyclerView = timesRecyclerView;
        initTimesView();
        this.mAudioRecyclerView = audioRecyclerView;
        initAudioView();
        this.mTextRecyclerView = textRecyclerView;
        initTextView();
        this.videoDurationTv = videoDurationTv;
        this.videoVolumeLine = videoVolumeLine;
        this.videoVolumeTouch = videoVolumeTouch;
        this.audioVolumeLine = audioVolumeLine;
        this.audioVolumeTouch = audioVolumeTouch;
        this.mVideoEditMissingFootageFl = videoEditMissingFootageFl;
        this.videoMuteIndicator = videoMuteIndicator;
        this.videoMuteIndicatorCover = videoMuteIndicatorCover;
        this.audioMuteIndicator = audioMuteIndicator;
        this.audioMuteTv = audioMuteTv;
        this.videoMuteTv = videoMuteTv;
        this.leftTouchView = leftTouchVie;
        this.rightTouchView = rightTouchView;
        this.videoVolumeSlideUnit = (((float) DensityUtil.dip2px(28.0f)) * 1.0f) / 10.0f;
        videoVolumeTouch.setSLIDE_UNIT(this.videoVolumeSlideUnit);
        videoVolumeTouch.setPressCallback(new VolumeSlideView.PressCallback() {
            public void onActionDown() {
                //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionDown", new Object[0]));
                if (VideoEditHelper.this.curSelectVideo == null) {
                    VideoEditHelper.this.videoSelectStart = -1;
                    VideoEditHelper.this.videoSelectEnd = -1;
                    return;
                }
                int[] ints = (int[]) VideoEditHelper.this.mModelListHashMap.get(VideoEditHelper.this.curSelectVideo);
                if (ints == null) {
                    VideoEditHelper.this.videoSelectStart = -1;
                    VideoEditHelper.this.videoSelectEnd = -1;
                    return;
                }
                VideoEditHelper.this.videoSelectStart = ints[0] + 1;
                VideoEditHelper.this.videoSelectEnd = ints[1] - 1;
                int firstVisibleItemPosition = VideoEditHelper.this.mImagesLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = VideoEditHelper.this.mImagesLayoutManager.findLastVisibleItemPosition();
                if (VideoEditHelper.this.videoSelectStart < firstVisibleItemPosition) {
                    VideoEditHelper.this.videoSelectStart = firstVisibleItemPosition;
                }
                if (VideoEditHelper.this.videoSelectEnd > lastVisibleItemPosition) {
                    VideoEditHelper.this.videoSelectEnd = lastVisibleItemPosition;
                }
            }

            public void onActionUp(MotionEvent event) {
                //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionUp", new Object[0]));
                VideoEditHelper.this.callAudioChange();
            }

            public void onActionMove(MotionEvent event, int time) {
                //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionMove time : %s ", new Object[]{Integer.valueOf(time)}));
                if (VideoEditHelper.this.curSelectVideo != null && time != 0) {
                    if (!VideoEditHelper.this.curSelectVideo.isHasEditVideoVolume()) {
                        VideoEditHelper.this.curSelectVideo.setHasEditVideoVolume(true);
                    }
                    float newVideoVolume = VideoEditHelper.sub(BigDecimal.valueOf((double) VideoEditHelper.this.curSelectVideo.getVideoVolume()), VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)));
                    if (newVideoVolume < StaticLayoutUtil.DefaultSpacingadd) {
                        newVideoVolume = StaticLayoutUtil.DefaultSpacingadd;
                    }
                    if (newVideoVolume > 1.0f) {
                        newVideoVolume = 1.0f;
                    }
                    VideoEditHelper.this.curSelectVideo.setVideoVolume(newVideoVolume);
                    //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionMove videoVolume : %s , newVideoVolume : %s ", new Object[]{Float.valueOf(videoVolume), Float.valueOf(newVideoVolume)}));
                    VideoEditHelper.this.refreshVideoVolumeView(newVideoVolume);
                    if (newVideoVolume == StaticLayoutUtil.DefaultSpacingadd) {
                        VideoEditHelper.this.refreshCurSelectVideoHolderMute();
                    }
                    VideoEditHelper.this.refreshImageHolderVolumeLine(newVideoVolume);
                }
            }

            public void onActionCancel(MotionEvent event) {
                //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionCancel", new Object[0]));
            }
        });
        this.audioVolumeSlideUnit = (((float) DensityUtil.dip2px(44.0f)) * 1.0f) / 10.0f;
        audioVolumeTouch.setSLIDE_UNIT(this.audioVolumeSlideUnit);
        audioVolumeTouch.setPressCallback(new VolumeSlideView.PressCallback() {
            private float downVolume = 1.0f;

            public void onActionDown() {
                //LogUtil.d(VideoEditHelper.TAG, String.format("videoVolumeTouch onActionDown", new Object[0]));
                this.downVolume = VideoEditHelper.this.getCurSlideAudioVolume();
            }

            public void onActionUp(MotionEvent event) {
                if (VideoEditHelper.this.getCurSelectAudioModel() != null) {
                    VideoEditHelper.this.getCurSelectAudioModel().cacheAudioData();
                }
                long volumeStart = ((VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(VideoEditHelper.this.audioSelectStart)).getAudioVolume().getStartTime();
                long volumeEnd = ((VideoEditImageEntity) VideoEditHelper.this.mAudioEntities.get(VideoEditHelper.this.audioSelectEnd)).getAudioVolume().getEndTime();
                float volume = VideoEditHelper.this.getCurSlideAudioVolume();
                boolean volumeChanged = volume != this.downVolume;
                int audioMuteStartPos = VideoEditHelper.this.audioSelectStart;
                int audioMuteEndPos = VideoEditHelper.this.audioSelectEnd;
                long resultVolumeStart = volumeStart;
                long resultVolumeEnd = volumeEnd;
                if (volume == StaticLayoutUtil.DefaultSpacingadd) {
                    VideoEditHelper.this.refreshMuteVolumeAreaAndHolder(VideoEditHelper.this.getCurSelectAudioModel(), resultVolumeStart, resultVolumeEnd, VideoEditHelper.this.audioSelectStart, VideoEditHelper.this.audioSelectEnd);
                } else {
                    VideoEditHelper.this.clearMuteTvShowStatus(audioMuteStartPos, audioMuteEndPos);
                }
                VideoEditHelper.this.refreshAudioMuteTv(VideoEditHelper.this.getCurSelectAudioModel());
                VideoEditHelper.this.onAudioVolumeTouchUp(VideoEditHelper.this.mCurSelectAudioModel, VideoEditHelper.this.audioSelectStart, volumeStart, volumeEnd, volume, volumeChanged);
            }

            public void onActionMove(MotionEvent event, int time) {
                if (VideoEditHelper.this.mCurSelectAudioModel != null && time != 0) {
                    float newVideoVolume = VideoEditHelper.sub(BigDecimal.valueOf((double) VideoEditHelper.this.getCurSlideAudioVolume()), VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)));
                    if (newVideoVolume < StaticLayoutUtil.DefaultSpacingadd) {
                        newVideoVolume = StaticLayoutUtil.DefaultSpacingadd;
                    }
                    if (newVideoVolume > 1.0f) {
                        newVideoVolume = 1.0f;
                    }
                    VideoEditHelper.this.setCurSlideAudioVolume(newVideoVolume);
                    //LogUtil.d(VideoEditHelper.TAG, String.format("audioVolumeTouch onActionMove videoVolume : %s , newVideoVolume : %s ", new Object[]{Float.valueOf(videoVolume), Float.valueOf(newVideoVolume)}));
                    VideoEditHelper.this.refreshAudioVolumeView(newVideoVolume);
                    VideoEditHelper.this.refreshAudioHolderVolumeLine(newVideoVolume);
                }
            }

            public void onActionCancel(MotionEvent event) {
            }
        });
        refreshAudioMuteIndicator();
    }

    private void refreshMuteVolumeAreaAndHolder(LongVideosModel model, long resultVolumeStart, long resultVolumeEnd, int muteStartPos, int muteEndPos) {
        VideoModelHelper.refreshMuteVolumeAreaAndHolder(model, resultVolumeStart, resultVolumeEnd, muteStartPos, muteEndPos, getMuteTextWidth(), this.mAudioEntities);
    }

    private void refreshAudioMuteTv(LongVideosModel audioModel) {
        if (audioModel != null) {
            int[] ints = (int[]) this.mAudioModelsListHashMap.get(audioModel);
            VideoModelHelper.refreshAudioMuteTv(audioModel, this.mAudioEntities, ints[0], ints[1], getMuteTextWidth());
        }
    }

    private float getCurSlideAudioVolume() {
        return ((VideoEditImageEntity) this.mAudioEntities.get(this.audioSelectStart)).getAudioVolume().getVolume();
    }

    private void setCurSlideAudioVolume(float volume) {
        for (int i = this.audioSelectStart; i <= this.audioSelectEnd; i++) {
            ((VideoEditImageEntity) this.mAudioEntities.get(i)).getAudioVolume().setVolume(volume);
        }
    }

    private float getMuteTextWidth() {
        Paint paint = new Paint();
        paint.setTextSize((float) DensityUtil.dip2px(10.0f));
        return paint.measureText(App.getResource().getString(R.string.TEXT_VIDEO_MUSIC_MUTE));
    }

    private void refreshAudioHolderMute(LongVideosModel model, long audioMuteDuration, int audioMuteStartPos, int audioMuteEndPos) {
        VideoModelHelper.refreshAudioHolderMute(model, audioMuteDuration, audioMuteStartPos, audioMuteEndPos, getMuteTextWidth(), this.mAudioEntities);
    }

    private void clearMuteTvShowStatus(int start, int end) {
        VideoModelHelper.clearMuteTvShowStatus(start, end, this.mAudioEntities);
    }

    private void refreshVideoHolderMute(LongVideosModel model) {
        if (model != null) {
            int[] ints = (int[]) this.mModelListHashMap.get(model);
            VideoModelHelper.refreshVideoHolderMute(model, ints[0] + 1, ints[1] - 1, getMuteTextWidth(), this.mImageEntities);
        }
    }

    private void refreshCurSelectVideoHolderMute() {
        refreshVideoHolderMute(this.curSelectVideo);
    }

    private void refreshImageHolderVolumeLine(float newVideoVolume) {
        if (this.videoSelectStart != -1 || this.videoSelectEnd != -1) {
            for (int i = this.videoSelectStart; i <= this.videoSelectEnd; i++) {
                ViewHolder viewHolderForAdapterPosition = this.mImagesRecyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolderForAdapterPosition != null && (viewHolderForAdapterPosition instanceof VideoEditImageHolder)) {
                    ((VideoEditImageHolder) viewHolderForAdapterPosition).refreshVideoVolumeLineView(newVideoVolume);
                }
            }
        }
    }

    private void refreshAudioHolderVolumeLine(float newAudioVolume) {
        if (this.audioSelectStart != -1 || this.audioSelectEnd != -1) {
            for (int i = this.audioSelectStart; i <= this.audioSelectEnd; i++) {
                ViewHolder viewHolderForAdapterPosition = this.mAudioRecyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolderForAdapterPosition != null && (viewHolderForAdapterPosition instanceof VideoEditAudioEditHolder)) {
                    ((VideoEditAudioEditHolder) viewHolderForAdapterPosition).refreshVideoVolumeLineView(newAudioVolume);
                }
            }
        }
    }

    public boolean isMusicEdit() {
        return this.isHavenInMusicEdit;
    }

    public void onSwitchTabClick(boolean isMusic) {
        this.isMusicEdit = isMusic;
        this.isHavenInMusicEdit = this.isMusicEdit;
        if (this.mChangeOtherCallback != null) {
            refreshVideoMuteTvPos();
            refreshAudioMuteTvPos();
            this.mChangeOtherCallback.onVideoMuteClick(this.manager.isAllVideoMute(), this.isMusicEdit);
            this.mChangeOtherCallback.onAudioMuteClick(this.manager.isAllAudioMute(), this.isMusicEdit);
        }
        LayoutParams timesParams = (LayoutParams) this.mTimesRecyclerView.getLayoutParams();
        LayoutParams imagesParams = (LayoutParams) this.mImagesRecyclerView.getLayoutParams();
        LayoutParams audiosParams = (LayoutParams) this.mAudioRecyclerView.getLayoutParams();
        LayoutParams missingFootageParams = (LayoutParams) this.mVideoEditMissingFootageFl.getLayoutParams();
        LayoutParams videoMuteIndicatorParams = (LayoutParams) this.videoMuteIndicator.getLayoutParams();
        LayoutParams videoMuteIndicatorCoverParams = (LayoutParams) this.videoMuteIndicatorCover.getLayoutParams();
        LayoutParams audioMuteIndicatorParams = (LayoutParams) this.audioMuteIndicator.getLayoutParams();
        LayoutParams videoMuteTvParams = (LayoutParams) this.videoMuteTv.getLayoutParams();
        LayoutParams audioMuteTvParams = (LayoutParams) this.audioMuteTv.getLayoutParams();
        if (isMusic) {
            timesParams.topMargin = DensityUtil.dip2px(10.0f);
            imagesParams.height = DensityUtil.dip2px(36.0f);
            imagesParams.bottomMargin = DensityUtil.dip2px(54.0f);
            audiosParams.height = DensityUtil.dip2px(52.0f);
            audioMuteTvParams.height = DensityUtil.dip2px(52.0f);
            missingFootageParams.height = DensityUtil.dip2px(36.0f);
            missingFootageParams.bottomMargin = DensityUtil.dip2px(54.0f);
            videoMuteIndicatorParams.height = this.mVideoMuteIndicatorHeight;
            videoMuteIndicatorParams.bottomMargin = this.mVideoMuteIndicatorBottomMargin;
            videoMuteIndicatorCoverParams.height = this.mVideoMuteIndicatorHeight;
            videoMuteIndicatorCoverParams.bottomMargin = this.mVideoMuteIndicatorBottomMargin;
            audioMuteIndicatorParams.height = this.mAudioMuteIndicatorHeight;
            audioMuteIndicatorParams.bottomMargin = this.mAudioMuteIndicatorBottomMargin;
            int dip2px = DensityUtil.dip2px(36.0f);
            imagesParams.height = dip2px;
            videoMuteTvParams.height = dip2px;
            videoMuteTvParams.bottomMargin = DensityUtil.dip2px(54.0f);
            hideDurationTvView();
        } else {
            timesParams.topMargin = DensityUtil.dip2px(15.0f);
            imagesParams.height = DensityUtil.dip2px(52.0f);
            imagesParams.bottomMargin = DensityUtil.dip2px(30.0f);
            audiosParams.height = DensityUtil.dip2px(28.0f);
            missingFootageParams.height = DensityUtil.dip2px(52.0f);
            missingFootageParams.bottomMargin = DensityUtil.dip2px(30.0f);
            this.mImagesRecyclerView.post(new Runnable() {
                public void run() {
                    VideoEditHelper.this.findVideoDurationTvShowRect(VideoEditHelper.this.curSelectVideo);
                }
            });
        }
        this.mTimesRecyclerView.setLayoutParams(timesParams);
        this.mImagesRecyclerView.setLayoutParams(imagesParams);
        this.mAudioRecyclerView.setLayoutParams(audiosParams);
        this.mVideoEditMissingFootageFl.setLayoutParams(missingFootageParams);
        this.audioMuteIndicator.setLayoutParams(audioMuteIndicatorParams);
        refreshVideosView(getVideoLongVideosModels());
        refreshAddMusicView(this.mAudioScrollX);
        refreshVideoVolumeTouchView();
        refreshAudioVolumeTouchView();
        refreshAudioMuteIndicator();
        this.mImagesRecyclerView.post(new Runnable() {
            public void run() {
                VideoEditHelper.this.refreshAudioSelectPos();
                VideoEditHelper.this.calculateExactlyScrollX();
                VideoEditHelper.this.relateScroll(VideoEditHelper.this.mImagesScrollX, 0);
            }
        });
    }

    private void refreshVisibleAudioItem() {
        VideoModelHelper.refreshVisibleRecyclerView(this.mAudioLayoutManager, this.mVideoEditAudioAdapter, 3, this.mAudioEntities.size());
    }

    private void changeOtherUnSelected() {
        if (!TextUtil.isNull(this.mTextEntities)) {
            Iterator it = this.mTextEntities.iterator();
            while (it.hasNext()) {
                VideoEditImageEntity entity = (VideoEditImageEntity) it.next();
                if (entity.isPin()) {
                    entity.setPin(false);
                }
            }
        }
    }

    private void setLongPressing(boolean longPressing) {
        this.isLongPressing = longPressing;
    }

    private boolean clearIfIsInTextSelectModel() {
        if (this.curTextVideoModel == null) {
            return false;
        }
        this.curTextVideoModel = null;
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.refreshBottomCoverViewVisibility(false);
        }
        changeOtherUnSelected();
        refreshEndRelate();
        int firstVisibleItemPosition = this.mTextLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = this.mTextLayoutManager.findLastVisibleItemPosition();
        int needStart = firstVisibleItemPosition + -1 < 0 ? firstVisibleItemPosition : firstVisibleItemPosition - 1;
        this.mVideoEditTextAdapter.notifyItemRangeChanged(needStart, ((lastVisibleItemPosition + 1 > this.mTextEntities.size() + -1 ? lastVisibleItemPosition : lastVisibleItemPosition + 1) - needStart) + 1);
        refreshTextRecyclerViewHeight(false);
        return true;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initTextView() {
        this.mTextEntities = new ArrayList();
        this.mVideoEditTextAdapter = new VideoEditImageAdapter(this.mContext, this.mTextEntities, this.manager, this.mSlideVideoAreaCallback);
//        this.mVideoEditTextAdapter.setOnItemClickListener(Lambda.1.lambdaFactory$(this));
        this.mVideoEditTextAdapter.setOnItemClickListener(new NormalBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                lambda$initTextView$2(view,i);
            }
        });
//        this.mTextRecyclerView.setOnTouchListener(2.lambdaFactory$(this));
        this.mTextRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lambda$initTextView$3(v,event);
            }
        });
        this.mTextLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.mTextRecyclerView.setLayoutManager(this.mTextLayoutManager);
        this.mTextRecyclerView.setAdapter(this.mVideoEditTextAdapter);
        ((SimpleItemAnimator) this.mTextRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mTextRecyclerView.setItemAnimator(null);
        this.mTextRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0 && !VideoEditHelper.this.isTextTouching) {
                    VideoEditHelper.this.isTextSliding = false;
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!VideoEditHelper.this.isLongPressing) {
                    VideoEditHelper.this.mTextScrollX = VideoEditHelper.this.mTextScrollX + dx;
                    if (VideoEditHelper.this.mTextScrollX > VideoEditHelper.this.mMaxScrollX) {
                        VideoEditHelper.this.mTextScrollX = VideoEditHelper.this.mMaxScrollX;
                    } else if (VideoEditHelper.this.mTextScrollX < 0) {
                        VideoEditHelper.this.mTextScrollX = 0;
                    }
                    if (VideoEditHelper.this.isTextSliding) {
                        VideoEditHelper.this.relateScroll(VideoEditHelper.this.mTextScrollX, dx);
                    }
                }
                VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.mTextScrollX);
                VideoEditHelper.this.refreshCurTextModelSelected(VideoEditHelper.this.mTextScrollX);
            }
        });
    }

    private /* synthetic */ void lambda$initTextView$2(View view, int position) {
        boolean z = false;
        if (checkTextPositionIsValid(position)) {
            LogUtil.d(TAG, String.format("textItemClick position : %s ", new Object[]{Integer.valueOf(position)}));
            VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mTextEntities.get(position);
            if (videoEditImageEntity.getViewType() != BaseAdapter.Item.VIDEO_EDIT_TEXT_TYPE) {
                if (clearIfIsInTextSelectModel() || clearIfExistSelect()) {
                }
                return;
            }
            boolean pin = videoEditImageEntity.isPin();
            if (this.mChangeOtherCallback != null) {
                boolean z2;
                ChangeOtherCallback changeOtherCallback = this.mChangeOtherCallback;
                if (pin) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                changeOtherCallback.refreshBottomCoverViewVisibility(z2);
            }
            changeOtherUnSelected();
            if (!pin) {
                z = true;
            }
            videoEditImageEntity.setPin(z);
            int firstVisibleItemPosition = this.mTextLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = this.mTextLayoutManager.findLastVisibleItemPosition();
            final int needStart = firstVisibleItemPosition + -1 < 0 ? firstVisibleItemPosition : firstVisibleItemPosition - 1;
            final int needEnd = lastVisibleItemPosition + 1 > this.mTextEntities.size() + -1 ? lastVisibleItemPosition : lastVisibleItemPosition + 1;
            VideoEditTextHolder videoEditTextHolder = (VideoEditTextHolder) this.mTextRecyclerView.getChildViewHolder(view);
            if (pin) {
                this.curTextVideoModel = null;
                videoEditTextHolder.finalAnimation();
//                videoEditTextHolder.setAnimationEndListener(11.lambdaFactory$(this, needStart, needEnd));
                videoEditTextHolder.setAnimationEndListener(new VideoEditTextHolder.AnimationEndListener() {
                    @Override
                    public void endListener() {
                        lambda$null$0(needStart,needEnd);
                    }
                });
                return;
            }
            refreshTextRecyclerViewHeight(true);
            this.curTextVideoModel = videoEditImageEntity.getLongVideosModel();
            videoEditTextHolder.beginAnimation();
//            videoEditTextHolder.setAnimationEndListener(12.lambdaFactory$(this, needStart, needEnd));
            videoEditTextHolder.setAnimationEndListener(new VideoEditTextHolder.AnimationEndListener() {
                @Override
                public void endListener() {
                    lambda$null$1(needStart,needEnd);
                }
            });
        }
    }

    private /* synthetic */ void lambda$null$0(int needStart, int needEnd) {
        this.mVideoEditTextAdapter.notifyItemRangeChanged(needStart, (needEnd - needStart) + 1);
        refreshTextRecyclerViewHeight(false);
    }

    private /* synthetic */ void lambda$null$1(int needStart, int needEnd) {
        this.mVideoEditTextAdapter.notifyItemRangeChanged(needStart, (needEnd - needStart) + 1);
    }

    private /* synthetic */ boolean lambda$initTextView$3(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                this.isTextTouching = true;
                break;
            case 1:
            case 3:
                this.isTextTouching = false;
                break;
            case 2:
                if (this.mChangeOtherCallback != null) {
                    this.mChangeOtherCallback.callSetVideoPause();
                }
                this.isTextSliding = true;
                this.isAutoScrolling = false;
                break;
        }
        return false;
    }

    public void refreshTextViewWhenInVisible(double percent) {
        if (this.mMaxScrollX != 0 && percent >= 0.0d && percent <= 1.0d && !TextUtil.isNull(getTextVideosModels())) {
            int scrollX = (int) (((double) this.mMaxScrollX) * percent);
            if (scrollX != this.lastInVisibleScrollX) {
                refreshOutCurTextModel(scrollX);
                this.lastInVisibleScrollX = scrollX;
            }
        }
    }

    private void refreshCurTextModelSelected(int textScrollX) {
        if (this.curTextVideoModel == null) {
            return;
        }
        if (this.curTextVideoModel.getStartTimeMs() > ((long) (1000.0f * div((float) ((this.screenWidth / 2) + textScrollX), (float) IMAGE_UNIT_WIDTH)))) {
            refreshEndRelate();
            refreshTextView(getTextVideosModels(), null, true);
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshCuTextModel(null, true);
                this.mChangeOtherCallback.refreshBottomCoverViewVisibility(false);
                return;
            }
            return;
        }
        if (((long) (1000.0f * div((float) (textScrollX - (this.screenWidth / 2)), (float) IMAGE_UNIT_WIDTH))) > this.curTextVideoModel.getStartTimeMs() + this.curTextVideoModel.getCurrentDurationValue()) {
            refreshEndRelate();
            refreshTextView(getTextVideosModels(), null, true);
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshCuTextModel(null, true);
                this.mChangeOtherCallback.refreshBottomCoverViewVisibility(false);
            }
        }
    }

    private void refreshOutCurTextModel(int textScrollX) {
        long timeUs = (long) (1000.0f * div((float) textScrollX, (float) IMAGE_UNIT_WIDTH));
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (TextUtil.isValidate(textVideosModels)) {
            int size = textVideosModels.size();
            long firstStartTime = ((LongVideosModel) textVideosModels.get(0)).getStartTimeMs();
            if (timeUs >= firstStartTime) {
                long endTime = ((LongVideosModel) textVideosModels.get(size - 1)).getStartTimeMs() + ((LongVideosModel) textVideosModels.get(size - 1)).getCurrentDurationValue();
                if (timeUs <= endTime) {
                    int i = 0;
                    while (i < size) {
                        LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                        long end = model.getStartTimeMs() + model.getCurrentDurationValue();
                        if (timeUs < model.getStartTimeMs() || timeUs >= end) {
                            if (timeUs >= end && i + 1 < size) {
                                model = (LongVideosModel) textVideosModels.get(i + 1);
                                if (timeUs >= end && timeUs < model.getStartTimeMs()) {
                                    if (this.mChangeOtherCallback != null) {
                                        this.mChangeOtherCallback.refreshCuTextModel(null, model.getStartTimeMs() - timeUs >= 1000);
                                        return;
                                    }
                                    return;
                                }
                            }
                            i++;
                        } else if (this.mChangeOtherCallback != null) {
                            this.mChangeOtherCallback.refreshCuTextModel(model, false);
                            return;
                        } else {
                            return;
                        }
                    }
                } else if (this.mChangeOtherCallback != null) {
                    this.mChangeOtherCallback.refreshCuTextModel(null, this.sumTime - ((float) endTime) >= 1000.0f);
                }
            } else if (this.mChangeOtherCallback != null) {
                boolean z;
                ChangeOtherCallback changeOtherCallback = this.mChangeOtherCallback;
                if (firstStartTime - timeUs >= 1000) {
                    z = true;
                } else {
                    z = false;
                }
                changeOtherCallback.refreshCuTextModel(null, z);
            }
        } else if (this.sumTime < 1000.0f) {
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshCuTextModel(null, false);
            }
        } else if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.refreshCuTextModel(null, true);
        }
    }

    private void refreshTextRecyclerViewHeight(boolean hasSelected) {
        boolean hasLongPressing = hasLongPressing();
        ViewGroup.LayoutParams layoutParams = this.mTextRecyclerView.getLayoutParams();
        boolean needChange = false;
        if (hasSelected) {
            if (hasLongPressing) {
                if (layoutParams.height != VideoEditTextHolder.ITEM_SLIDE_HEIGHT) {
                    layoutParams.height = VideoEditTextHolder.ITEM_SLIDE_HEIGHT;
                    needChange = true;
                }
            } else if (layoutParams.height != VideoEditTextHolder.ITEM_EDIT_HEIGHT) {
                layoutParams.height = VideoEditTextHolder.ITEM_EDIT_HEIGHT;
                needChange = true;
            }
        } else if (hasLongPressing) {
            if (layoutParams.height != VideoEditTextHolder.ITEM_MIN_SLIDE_HEIGHT) {
                layoutParams.height = VideoEditTextHolder.ITEM_MIN_SLIDE_HEIGHT;
                needChange = true;
            }
        } else if (layoutParams.height != VideoEditTextHolder.ITEM_MIN_HEIGHT) {
            layoutParams.height = VideoEditTextHolder.ITEM_MIN_HEIGHT;
            needChange = true;
        }
        if (needChange) {
            this.mTextRecyclerView.setLayoutParams(layoutParams);
        }
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.callVideoEditSwitchTabsVisibility(!hasSelected);
        }
    }

    private boolean hasLongPressing() {
        if (TextUtil.isNull(this.mTextEntities)) {
            return false;
        }
        Iterator it = this.mTextEntities.iterator();
        while (it.hasNext()) {
            VideoEditImageEntity entity = (VideoEditImageEntity) it.next();
            if (entity.getLongVideosModel() != null && entity.getLongVideosModel().isLongPressing()) {
                return true;
            }
        }
        return false;
    }

    public LongVideosModel getCurSelectAudioModel() {
        return this.mCurSelectAudioModel;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initAudioView() {
        this.mAudioEntities = new ArrayList();
        this.mVideoEditAudioAdapter = new VideoEditImageAdapter(this.mContext, this.mAudioEntities, this.manager, this.mSlideVideoAreaCallback);
        this.mAudioLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.mAudioRecyclerView.setLayoutManager(this.mAudioLayoutManager);
        this.mAudioRecyclerView.setAdapter(this.mVideoEditAudioAdapter);
        this.mVideoEditAudioAdapter.setOnItemClickListener(new NormalBaseAdapter.OnItemClickListener() {
            public void onItemClick(View view, int position) {
                VideoEditHelper.this.dealAudioItemClick(view, position);
            }
        });
        this.mAudioModelsListHashMap = new HashMap();
        ((SimpleItemAnimator) this.mAudioRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mAudioRecyclerView.setItemAnimator(null);
        this.mAudioRecyclerView.setChildDrawingOrderCallback(new ChildDrawingOrderCallback() {
            public int onGetChildDrawingOrder(int childCount, int i) {
                int result = i;
                int tempTopPosition = VideoEditHelper.this.curSelectAudioEndPosition - VideoEditHelper.this.mAudioLayoutManager.findFirstVisibleItemPosition();
                if (tempTopPosition < 0) {
                    return result;
                }
                if (tempTopPosition >= childCount) {
                    return result;
                }
                if (i == childCount - 1) {
                    result = tempTopPosition;
                } else if (i >= tempTopPosition) {
                    result = i + 1;
                } else {
                    result = i;
                }
                return result;
            }
        });
        this.mAudioRecyclerView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case 0:
                        VideoEditHelper.this.isAudioTouching = true;
                        break;
                    case 1:
                    case 3:
                        VideoEditHelper.this.isAudioTouching = false;
                        break;
                    case 2:
                        if (VideoEditHelper.this.mChangeOtherCallback != null) {
                            VideoEditHelper.this.mChangeOtherCallback.callSetVideoPause();
                        }
                        VideoEditHelper.this.isAudioSliding = true;
                        VideoEditHelper.this.isAutoScrolling = false;
                        break;
                }
                return false;
            }
        });
        this.mAudioRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0 && VideoEditHelper.this.isAudioSliding) {
                    VideoEditHelper.this.isAudioSliding = false;
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                VideoEditHelper.this.mAudioScrollX = VideoEditHelper.this.mAudioScrollX + dx;
                if (VideoEditHelper.this.mAudioScrollX > VideoEditHelper.this.mMaxScrollX) {
                    VideoEditHelper.this.mAudioScrollX = VideoEditHelper.this.mMaxScrollX;
                } else if (VideoEditHelper.this.mAudioScrollX < 0) {
                    VideoEditHelper.this.mAudioScrollX = 0;
                }
                if (VideoEditHelper.this.isAudioSliding) {
                    VideoEditHelper.this.relateScroll(VideoEditHelper.this.mAudioScrollX, dx);
                }
                if (!(VideoEditHelper.this.mCurSelectAudioModel == null || VideoEditHelper.this.isJumpToNearby || VideoEditHelper.this.isSliding || VideoEditHelper.this.isAudioChangeSliding || VideoEditHelper.this.checkAudioCenterIsInSelectedArea(VideoEditHelper.this.mAudioScrollX))) {
                    VideoEditHelper.this.clearCurAudioSelected();
                }
                VideoEditHelper.this.refreshAddMusicView(VideoEditHelper.this.mAudioScrollX);
                VideoEditHelper.this.refreshAudioMuteTvPos();
                if (VideoEditHelper.this.mCurSelectAudioModel != null) {
                    VideoEditHelper.this.updateAudioSlideView(TouchPresenterImpl.INVALIDATEPOSITION);
                }
                VideoEditHelper.this.refreshAudioVolumeTouchView();
                VideoEditHelper.this.refreshAudioMuteIndicator();
            }
        });
    }

    private void refreshMusicAudioSelectedArea(int adapterPosition) {
        List<VideoEditImageEntity> allDatas = this.mAudioEntities;
        int size1 = allDatas.size();
        if (adapterPosition >= 0 && adapterPosition < size1) {
            VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) allDatas.get(adapterPosition);
            LongVideosModel audioModel = videoEditImageEntity.getLongVideosModel();
            LongVideosModel.AudioVolume selectedVolume = videoEditImageEntity.getAudioVolume();
            ArrayList<LongVideosModel.AudioVolume> audioVolumes = audioModel.getAudioVolumes();
            if (!TextUtil.isNull(audioVolumes)) {
                int volumeSize = audioVolumes.size();
                int needPos = audioVolumes.indexOf(selectedVolume);
                if (needPos >= 0 && needPos < volumeSize) {
                    LongVideosModel.AudioVolume needAudioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(needPos);
                    if (needAudioVolume != null) {
                        int i;
                        LongVideosModel.AudioVolume audioVolume;
                        Iterator it = audioVolumes.iterator();
                        while (it.hasNext()) {
                            ((LongVideosModel.AudioVolume) it.next()).setSelected(false);
                        }
                        LongVideosModel.AudioVolume firstAudioVolume = needAudioVolume;
                        needAudioVolume.setSelected(true);
                        for (i = needPos - 1; i >= 0; i--) {
                            audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
                            if (audioVolume.getVolume() != needAudioVolume.getVolume()) {
                                break;
                            }
                            audioVolume.setSelected(true);
                            firstAudioVolume = audioVolume;
                        }
                        LongVideosModel.AudioVolume endAudioVolume = needAudioVolume;
                        for (i = needPos + 1; i < volumeSize; i++) {
                            audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
                            if (audioVolume.getVolume() != needAudioVolume.getVolume()) {
                                break;
                            }
                            audioVolume.setSelected(true);
                            endAudioVolume = audioVolume;
                        }
                        refreshAudioSelectPos(allDatas, firstAudioVolume, endAudioVolume);
                    }
                }
            }
        }
    }

    private void refreshAudioSelectPos() {
        if (this.mCurSelectAudioModel == null) {
            this.audioSelectStart = -1;
            this.audioSelectEnd = -1;
            refreshAudioVolumeTouchView();
            return;
        }
        int[] ints = (int[]) this.mAudioModelsListHashMap.get(this.mCurSelectAudioModel);
        int first = ints[0];
        int end = ints[1];
        this.audioSelectStart = end;
        this.audioSelectEnd = first;
        for (int i = first; i <= end; i++) {
            LongVideosModel.AudioVolume audioVolume = ((VideoEditImageEntity) this.mAudioEntities.get(i)).getAudioVolume();
            if (audioVolume != null && audioVolume.isSelected()) {
                if (i < this.audioSelectStart) {
                    this.audioSelectStart = i;
                }
                if (i > this.audioSelectEnd) {
                    this.audioSelectEnd = i;
                }
            }
        }
        LogUtil.d(TAG, String.format("refreshAudioSelectPos audioSelectStart : %s , audioSelectEnd : %s ", new Object[]{Integer.valueOf(this.audioSelectStart), Integer.valueOf(this.audioSelectEnd)}));
        if (this.audioSelectStart > this.audioSelectEnd) {
            this.audioSelectStart = -1;
            this.audioSelectEnd = -1;
            refreshAudioVolumeTouchView();
            return;
        }
        refreshAudioVolumeTouchView();
    }

    private void refreshAudioSelectPos(List<VideoEditImageEntity> allDatas, LongVideosModel.AudioVolume firstAudioVolume, LongVideosModel.AudioVolume endAudioVolume) {
        if (firstAudioVolume == null || endAudioVolume == null) {
            this.audioSelectStart = -1;
            this.audioSelectEnd = -1;
            return;
        }
        int i;
        int[] ints = (int[]) this.mAudioModelsListHashMap.get(this.mCurSelectAudioModel);
        int first = ints[0];
        int end = ints[1];
        LogUtil.d(TAG, String.format("first : %s ,end : %s ", new Object[]{Integer.valueOf(first), Integer.valueOf(end)}));
        for (i = first; i <= end; i++) {
            if (((VideoEditImageEntity) allDatas.get(i)).getAudioVolume() == firstAudioVolume) {
                first = i;
                break;
            }
        }
        for (i = end; i >= first; i--) {
            if (((VideoEditImageEntity) allDatas.get(i)).getAudioVolume() == endAudioVolume) {
                end = i;
                break;
            }
        }
        this.audioSelectStart = first;
        this.audioSelectEnd = end;
        LogUtil.d(TAG, String.format("audioSelectStart : %s ,audioSelectEnd : %s ", new Object[]{Integer.valueOf(this.audioSelectStart), Integer.valueOf(this.audioSelectEnd)}));
    }

    private void refreshAudioVolumeSelectPostAndRefreshVolumeTouch() {
        refreshAudioSelectPos(this.mImageEntities, null, null);
        refreshAudioVolumeTouchView();
    }

    private void dealAudioItemClick(View view, int position) {
        if (position >= 0 && position < this.mAudioEntities.size()) {
            VideoEditImageEntity entity = (VideoEditImageEntity) this.mAudioEntities.get(position);
            if (BaseAdapter.Item.VIDEO_EDIT_MUSIC_EDIT_TYPE == entity.getViewType() || entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_AUDIO_EDIT_TYPE) {
                boolean needAnimationJump = false;
                if (!this.isMusicEdit) {
                    this.isMusicEdit = true;
                    needAnimationJump = true;
                }
                this.mCurSelectAudioModel = entity.getLongVideosModel();
                refreshCurSelectAudioPosData();
                refreshMusicAudioSelectedArea(position);
                refreshAudioVolumeTouchView();
                refreshVisibleAudioItem();
                measureAudioSelectedStartAndEndX();
                if (this.mChangeOtherCallback != null) {
                    this.mChangeOtherCallback.refreshAudioEditButton();
                }
                updateAudioSlideView(this.curSelectAudioModelPosition);
                clearSelectVideoIfIsntInSelectVideoArea();
                if (needAnimationJump) {
                    this.mChangeOtherCallback.animationJumpToMusicEdit();
                }
            } else if (BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE == entity.getViewType()) {
                if (TextUtil.isNull(getAudioLongVideoModels())) {
                    if (this.mChangeOtherCallback != null) {
                        this.mChangeOtherCallback.onAddMusicClick(-1);
                    }
                } else if (position < ((int[]) this.mAudioModelsListHashMap.get(getAudioLongVideoModels().get(0)))[0] && this.mChangeOtherCallback != null) {
                    this.mChangeOtherCallback.onAddMusicClick(0);
                }
            } else if (BaseAdapter.Item.VIDEO_EDIT_AUDIO_ADD_TYPE == entity.getViewType()) {
                int prevPos = position - 1;
                if (prevPos >= 0) {
                    int indexOf = getAudioLongVideoModels().indexOf(((VideoEditImageEntity) this.mAudioEntities.get(prevPos)).getLongVideosModel());
                    if (this.mChangeOtherCallback != null) {
                        this.mChangeOtherCallback.onAddMusicClick(indexOf + 1);
                    }
                }
            } else if (BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE == entity.getViewType() && this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.onAddMusicClick(getAudioLongVideoModels().size());
            }
        }
    }

    private void refreshCurSelectAudioPosData() {
        if (getCurSelectAudioModel() == null) {
            this.curSelectAudioModelPosition = -1;
            this.curSelectAudioEndPosition = -1;
            return;
        }
        this.curSelectAudioModelPosition = getAudioLongVideoModels().indexOf(getCurSelectAudioModel());
        refreshCurSelectAudioEndPos();
    }

    private void refreshCurSelectAudioEndPos() {
        if (getCurSelectAudioModel() == null) {
            this.curSelectAudioEndPosition = -1;
            return;
        }
        int[] ints = (int[]) this.mAudioModelsListHashMap.get(getCurSelectAudioModel());
        if (ints != null) {
            this.curSelectAudioEndPosition = ints[1];
        }
    }

    private void refreshAddMusicTvVisible(int visible) {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.refreshVideoAddMusicTvVisible(visible);
        }
    }

    public void refreshAddMusicView() {
        refreshAddMusicView(this.mAudioScrollX);
    }

    private void refreshAddMusicView(int audioScrollX) {
        if (getVideoLongVideosModels().size() != 0) {
            int dp_15 = DensityUtil.dip2px(15.0f);
            int leftMargin = ((this.screenWidth / 2) - audioScrollX) + dp_15;
            if (leftMargin < dp_15) {
                leftMargin = dp_15;
            }
            List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
            if (audioLongVideoModels.size() > 0) {
                int minWidth = this.mChangeOtherCallback.getVideoAddMusicTvWidth() + DensityUtil.dip2px(25.0f);
                int rightLeft = audioScrollX - ((int) (((((float) ((LongVideosModel) audioLongVideoModels.get(0)).getAudioStartTime()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH)));
                if (rightLeft > 0) {
                    int leftLeft = (this.screenWidth / 2) - rightLeft;
                    if (leftLeft < minWidth) {
                        leftMargin += leftLeft - minWidth;
                    }
                }
            }
            int height = DensityUtil.dip2px(28.0f);
            if (this.isMusicEdit) {
                height = DensityUtil.dip2px(52.0f);
            }
            VideoEditImageEntity head = (VideoEditImageEntity) this.mImageEntities.get(0);
            if (head.getEmpty_type_width() > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                leftMargin = ((this.screenWidth / 2) + DensityUtil.dip2px(15.0f)) + (head.getEmpty_type_width() - this.VIDEO_START_AND_END_EMPTY_WIDTH);
            }
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshVideoAddMusicTv(leftMargin, height);
            }
        } else if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.refreshVideoAddMusicTvVisible(4);
        }
    }

    private void refreshAudioCenterLineView() {
        int firstVisibleItemPosition = this.mAudioLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = this.mAudioLayoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            ViewHolder viewHolder = this.mAudioRecyclerView.findViewHolderForAdapterPosition(i);
//            if (viewHolder != null && (viewHolder instanceof VideoEditMusicEditHolder)) {
//                ((VideoEditMusicEditHolder) viewHolder).refreshCenterLineView();
//            }
        }
    }

    private void refreshAudioSelectView() {
        if (this.curSelectAudioModelPosition != -1) {
            ViewHolder viewHolder = this.mAudioRecyclerView.findViewHolderForAdapterPosition(this.curSelectAudioModelPosition);
//            if (viewHolder != null && (viewHolder instanceof VideoEditMusicEditHolder)) {
//                ((VideoEditMusicEditHolder) viewHolder).refreshMusicVolumeSlideView(getCurSelectAudioModel());
//            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initTimesView() {
        this.mTimesEntities = new ArrayList();
        this.mVideoEditTimesAdapter = new VideoEditImageAdapter(this.mContext, this.mTimesEntities, this.manager, this.mSlideVideoAreaCallback);
        this.mTimesLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.mTimesRecyclerView.setLayoutManager(this.mTimesLayoutManager);
        this.mTimesRecyclerView.setAdapter(this.mVideoEditTimesAdapter);
        ((SimpleItemAnimator) this.mTimesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mTimesRecyclerView.setItemAnimator(null);
//        this.mTimesRecyclerView.setOnTouchListener(3.lambdaFactory$());
        this.mTimesRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("debug",getClass().getSimpleName()+":initTimesView:mTimesRecyclerview ontouchListener");
                return true;
            }
        });
        this.mTimesRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                VideoEditHelper.this.mTimesScrollX = VideoEditHelper.this.mTimesScrollX + dx;
                if (VideoEditHelper.this.mTimesScrollX > VideoEditHelper.this.mMaxScrollX) {
                    VideoEditHelper.this.mTimesScrollX = VideoEditHelper.this.mMaxScrollX;
                } else if (VideoEditHelper.this.mTimesScrollX < 0) {
                    VideoEditHelper.this.mTimesScrollX = 0;
                }
            }
        });
    }

    private void callVideoSeekTo(int currentScrollX) {
        float percent = (((float) currentScrollX) * 1.0f) / ((float) this.mMaxScrollX);
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.setVideoSeekTo(percent);
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initImagesView(final int screenWidth) {
        this.mImageEntities = new ArrayList();
        this.mVideoEditImageAdapter = new VideoEditImageAdapter(this.mContext, this.mImageEntities, this.manager, this.mSlideVideoAreaCallback);
//        this.mVideoEditImageAdapter.setOnItemClickListener(4.lambdaFactory$(this));
        this.mVideoEditImageAdapter.setOnItemClickListener(new NormalBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Log.e("debug",getClass().getSimpleName()+":initImageView:mVideoEditImageAdapter onItemclick : "+i);
                Toast.makeText(mContext,getClass().getSimpleName()+":initImageView:mVideoEditImageAdapter onItemclick : "+i,Toast.LENGTH_LONG).show();
                dealImageTypeItemClick(i);
            }
        });
        this.mImagesLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.mImagesRecyclerView.setLayoutManager(this.mImagesLayoutManager);
        this.mImagesRecyclerView.setAdapter(this.mVideoEditImageAdapter);
        ((SimpleItemAnimator) this.mImagesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mImagesRecyclerView.setItemAnimator(null);
        this.mModelListHashMap = new HashMap();
        this.mImagesRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lambda$initImagesView$6(v,event);
            }
        });
        this.mImagesRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    int scrollX = VideoEditHelper.this.mImagesScrollX;
                    float unit = VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) VideoEditHelper.IMAGE_UNIT_WIDTH)).setScale(1, RoundingMode.HALF_UP).floatValue();
                    float left = BigDecimal.valueOf((double) (((float) scrollX) % unit)).setScale(1, RoundingMode.HALF_UP).floatValue();
                    if (left <= 1.0f || left >= unit - 1.0f) {
                        VideoEditHelper.this.isJumpToNearby = false;
                        VideoEditHelper.this.calculateExactlyScrollX();
                        VideoEditHelper.this.relateScroll(VideoEditHelper.this.mImagesScrollX, 0);
                        if (VideoEditHelper.this.needResetOutTextModel) {
                            VideoEditHelper.this.needResetOutTextModel = false;
                            VideoEditHelper.this.refreshOutCurTextModel(VideoEditHelper.this.needResetOutScrollX);
                            return;
                        }
                        return;
                    }
                    int scroll;
                    if (left < unit / 2.0f) {
                        scroll = -BigDecimal.valueOf((double) left).setScale(0, RoundingMode.HALF_UP).intValue();
                    } else {
                        scroll = BigDecimal.valueOf((double) (unit - left)).setScale(0, RoundingMode.HALF_UP).intValue();
                    }
                    if (scroll != VideoEditHelper.this.lastNeedScrollX || scroll > 5) {
                        VideoEditHelper.this.lastNeedScrollX = scroll;
                        VideoEditHelper.this.mImagesRecyclerView.smoothScrollBy(scroll, 0);
                    }
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int showWidth;
                super.onScrolled(recyclerView, dx, dy);
                if (!VideoEditHelper.this.isSlideVideoAutoScrolling) {
                    VideoEditHelper.this.mImagesScrollX = VideoEditHelper.this.mImagesScrollX + dx;
                }
                if (VideoEditHelper.this.mImagesScrollX > VideoEditHelper.this.mMaxScrollX) {
                    VideoEditHelper.this.mImagesScrollX = VideoEditHelper.this.mMaxScrollX;
                } else if (VideoEditHelper.this.mImagesScrollX < 0) {
                    VideoEditHelper.this.mImagesScrollX = 0;
                }
                if (!VideoEditHelper.this.isSlideVideoAutoScrolling) {
                    VideoEditHelper.this.relateScroll(VideoEditHelper.this.mImagesScrollX, dx);
                }
                int leftHideWidth = VideoEditHelper.this.mImagesScrollX - VideoEditHelper.this.getLeftUnNeedWidth();
                int sum_width = VideoEditHelper.this.mMaxScrollX;
                int rightWidth = sum_width - VideoEditHelper.this.mImagesScrollX;
                if (rightWidth > screenWidth / 2) {
                    rightWidth = screenWidth / 2;
                }
                if (rightWidth < 0) {
                    rightWidth = 0;
                }
                if (leftHideWidth > 0) {
                    showWidth = rightWidth + (screenWidth / 2);
                } else {
                    showWidth = rightWidth + VideoEditHelper.this.mImagesScrollX;
                }
                int width = (int) (((((float) showWidth) * 1.0f) / ((float) sum_width)) * ((float) screenWidth));
                if (leftHideWidth < 0) {
                    leftHideWidth = 0;
                }
                int leftMargin = (int) (((((double) leftHideWidth) * 1.0d) / ((double) sum_width)) * ((double) screenWidth));
                if (VideoEditHelper.this.mChangeOtherCallback != null) {
                    VideoEditHelper.this.mChangeOtherCallback.setVideoSlideBarWidthMargin(width, leftMargin);
                }
                boolean isSplit = true;
                List<LongVideosModel> longVideosModels = VideoEditHelper.this.getVideoLongVideosModels();
                int size = longVideosModels.size();
                if (size > 0) {
                    int firstPosition = 0;
                    long splitLeftDuration = 0;
                    long splitRightDuration = 0;
                    long accSplitRightDuration = 0;
                    long accSplitLeftDuration = 0;
                    LongVideosModel model = null;
                    if (VideoEditHelper.this.isInCenterArea(VideoEditHelper.this.mImagesScrollX, 0)) {
                        VideoEditHelper.this.insertPosition = 0;
                        isSplit = false;
                        model = (LongVideosModel) longVideosModels.get(0);
                    } else if (VideoEditHelper.this.isInCenterArea(VideoEditHelper.this.mImagesScrollX, VideoEditHelper.this.mMaxScrollX)) {
                        VideoEditHelper.this.insertPosition = size;
                        firstPosition = VideoEditHelper.this.mMaxScrollX;
                        isSplit = false;
                        model = (LongVideosModel) longVideosModels.get(size - 1);
                    } else {
                        int startTime = 0;
                        for (int i = 0; i < size; i++) {
                            model = (LongVideosModel) longVideosModels.get(i);
                            firstPosition = (int) (((float) firstPosition) + (((((float) model.getCurrentDuration()) * 1.0f) / 1000.0f) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH)));
                            if (VideoEditHelper.this.isInCenterArea(VideoEditHelper.this.mImagesScrollX, firstPosition)) {
                                VideoEditHelper.this.insertPosition = i + 1;
                                isSplit = false;
                                break;
                            }
                            if (VideoEditHelper.this.mImagesScrollX <= firstPosition - VideoEditImageHolder.LEFT_OR_RIGHT_SPACE) {
                                isSplit = true;
                                splitLeftDuration = ((long) (VideoEditHelper.div((float) VideoEditHelper.this.mImagesScrollX, (float) VideoEditHelper.IMAGE_UNIT_WIDTH) * 1000.0f)) - ((long) startTime);
                                splitRightDuration = model.getCurrentDuration() - splitLeftDuration;
                                accSplitLeftDuration = (long) ((((((float) VideoEditHelper.this.mImagesScrollX) * 1.0f) / ((float) VideoEditHelper.IMAGE_UNIT_WIDTH)) * 1000.0f) - ((float) startTime));
                                accSplitRightDuration = model.getCurrentDuration() - accSplitLeftDuration;
                                break;
                            }
                            startTime = (int) (((long) startTime) + model.getCurrentDuration());
                        }
                    }
                    if (VideoEditHelper.this.mChangeOtherCallback != null) {
                        VideoEditHelper.this.mChangeOtherCallback.onVideoModelChange(model);
                    }
                    boolean canSplitCurrent = true;
                    if (isSplit) {
                        long offset = 0;
                        if (splitLeftDuration < 500 || splitRightDuration < 500) {
                            if (splitLeftDuration < 500) {
                                offset = accSplitLeftDuration;
                            } else {
                                offset = accSplitRightDuration;
                            }
                            canSplitCurrent = false;
                        }
                        VideoEditHelper.this.refreshVideoMuteIndicatorHeight(canSplitCurrent, offset);
                    }
                    if (VideoEditHelper.this.mChangeOtherCallback != null) {
                        VideoEditHelper.this.mChangeOtherCallback.setSplitBtnVisible(isSplit, canSplitCurrent, VideoEditHelper.this.isMusicEdit);
                    }
                    if (isSplit) {
                        if (!VideoEditHelper.this.isAutoScrolling) {
                            VideoEditHelper.this.callVideoSeekTo(VideoEditHelper.this.mImagesScrollX);
                        }
                    } else if (!VideoEditHelper.this.isAutoScrolling) {
                        int resultScrollX;
                        if (VideoEditHelper.this.mImagesScrollX < firstPosition) {
                            resultScrollX = firstPosition - VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                        } else if (VideoEditHelper.this.mImagesScrollX > firstPosition) {
                            resultScrollX = firstPosition + VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                        } else if (dx > 0) {
                            resultScrollX = firstPosition - VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                        } else {
                            resultScrollX = firstPosition + VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                        }
                        if (resultScrollX > VideoEditHelper.this.mMaxScrollX) {
                            resultScrollX = VideoEditHelper.this.mMaxScrollX;
                        }
                        if (resultScrollX < 0) {
                            resultScrollX = 0;
                        }
                        VideoEditHelper.this.callVideoSeekTo(resultScrollX);
                        VideoEditHelper.this.refreshOutCurTextModel(resultScrollX);
                    }
                    if (!(((VideoEditHelper.this.curSelectVideo == null || VideoEditHelper.this.isJumpToNearby || VideoEditHelper.this.isSliding) && (VideoEditHelper.this.curSelectVideo == null || !VideoEditHelper.this.isTouch || VideoEditHelper.this.isSliding)) || VideoEditHelper.this.checkCenterIsInSelectedArea(VideoEditHelper.this.mImagesScrollX))) {
                        VideoEditHelper.this.clearCurSelectVideo();
                        VideoEditHelper.this.refreshVisibleVideoItem();
                    }
                    VideoEditHelper.this.findVideoDurationTvShowRect(VideoEditHelper.this.curSelectVideo);
                    VideoEditHelper.this.refreshVideoVolumeTouchView();
                    if (VideoEditHelper.this.needCheckMissingFootage) {
                        VideoEditHelper.this.refreshMissingFootage();
                    }
                    VideoEditHelper.this.refreshVideoMuteTvPos();
                }
            }
        });
    }

    private /* synthetic */ boolean lambda$initImagesView$6(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case 2:
                if (this.mChangeOtherCallback != null) {
                    this.mChangeOtherCallback.callSetVideoPause();
                }
                this.isAutoScrolling = false;
                break;
        }
        return false;
    }

    private void refreshAudioMuteIndicator() {
        if (this.isMusicEdit) {
            int[] result = checkAudioMuteIndicatorVisibility();
            if (result[0] == 1) {
                if (result[1] != 0) {
                    this.mAudioMuteIndicatorHeight = result[2];
                    this.mAudioMuteIndicatorBottomMargin = result[3];
                    resetAudioMuteIndicatorLP();
                } else if (this.mAudioMuteIndicatorHeight != DensityUtil.dip2px(44.0f)) {
                    this.mAudioMuteIndicatorHeight = DensityUtil.dip2px(44.0f);
                    this.mAudioMuteIndicatorBottomMargin = DensityUtil.dip2px(AUDIO_MUTE_INDICATOR_DEFAULT_BOTTOM_MARGIN);
                    resetAudioMuteIndicatorLP();
                }
                this.audioMuteIndicator.setVisibility(View.VISIBLE);
                return;
            }
            this.audioMuteIndicator.setVisibility(View.GONE);
            return;
        }
        this.audioMuteIndicator.setVisibility(View.GONE);
    }

    private int[] checkAudioMuteIndicatorVisibility() {
        int[] result = new int[4];
        result[0] = 0;
        result[1] = 0;
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (TextUtil.isValidate(audioLongVideoModels)) {
            int index = -1;
            for (int i = 0; i < audioLongVideoModels.size(); i++) {
                if (isAudioModelContainCenter((LongVideosModel) audioLongVideoModels.get(i))) {
                    index = i;
                }
            }
            if (index != -1) {
                LongVideosModel audioModel = (LongVideosModel) audioLongVideoModels.get(index);
                int startX = ((((int) (((((float) audioModel.getAudioStartTime()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH))) + this.VIDEO_HALF_SCREEN) + DensityUtil.dip2px(3.0f)) - this.mAudioScrollX;
                int leftOffset = this.VIDEO_HALF_SCREEN - startX;
                int rightOffset = ((((int) (((((float) audioModel.getAudioDuration()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH))) + startX) - DensityUtil.dip2px(6.0f)) - this.VIDEO_HALF_SCREEN;
                int limit = (int) (0.5f * ((float) IMAGE_UNIT_WIDTH));
                if (leftOffset <= limit) {
                    result[1] = 1;
                    result[2] = getAudioLineHeightByOffset(leftOffset);
                    result[3] = DensityUtil.dip2px(AUDIO_MUTE_INDICATOR_DEFAULT_BOTTOM_MARGIN) + ((DensityUtil.dip2px(44.0f) - result[2]) / 2);
                } else if (rightOffset <= limit) {
                    result[1] = 1;
                    result[2] = getAudioLineHeightByOffset(rightOffset);
                    result[3] = DensityUtil.dip2px(AUDIO_MUTE_INDICATOR_DEFAULT_BOTTOM_MARGIN) + ((DensityUtil.dip2px(44.0f) - result[2]) / 2);
                }
                result[0] = 1;
            }
        }
        return result;
    }

    private int getAudioLineHeightByOffset(int itemWidth) {
        if (itemWidth <= DensityUtil.dip2px(0.5f)) {
            return DensityUtil.dip2px(34.0f);
        }
        if (itemWidth >= DensityUtil.dip2px(5.0f)) {
            return DensityUtil.dip2px(44.0f);
        }
        int height = (DensityUtil.dip2px(34.0f) + DensityUtil.dip2px((float) ((itemWidth * 10) / DensityUtil.dip2px(5.0f)))) + 2;
        if (height > DensityUtil.dip2px(44.0f)) {
            return DensityUtil.dip2px(44.0f);
        }
        return height;
    }

    private boolean isAudioModelContainCenter(LongVideosModel model) {
        int startX = ((((int) (((((float) model.getAudioStartTime()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH))) + this.VIDEO_HALF_SCREEN) + DensityUtil.dip2px(3.0f)) - this.mAudioScrollX;
        return startX <= this.VIDEO_HALF_SCREEN && (((int) (((((float) model.getAudioDuration()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH))) + startX) - DensityUtil.dip2px(6.0f) > this.VIDEO_HALF_SCREEN;
    }

    private void resetAudioMuteIndicatorLP() {
        LayoutParams layoutParams = (LayoutParams) this.audioMuteIndicator.getLayoutParams();
        layoutParams.height = this.mAudioMuteIndicatorHeight;
        layoutParams.bottomMargin = this.mAudioMuteIndicatorBottomMargin;
        this.audioMuteIndicator.setLayoutParams(layoutParams);
    }

    private void refreshVideoMuteIndicatorHeight(boolean canSplitCurrent, long offset) {
        if (!canSplitCurrent) {
            this.mVideoMuteIndicatorHeight = getLineHeightByOffset(offset);
            this.mVideoMuteIndicatorBottomMargin = DensityUtil.dip2px(57.0f) + ((DensityUtil.dip2px(30.0f) - this.mVideoMuteIndicatorHeight) / 2);
            refreshVideoMuteIndicator();
        } else if (this.mVideoMuteIndicatorHeight != DensityUtil.dip2px(30.0f)) {
            this.mVideoMuteIndicatorHeight = DensityUtil.dip2px(30.0f);
            this.mVideoMuteIndicatorBottomMargin = DensityUtil.dip2px(57.0f);
            refreshVideoMuteIndicator();
        }
    }

    private void refreshVideoMuteIndicator() {
        LayoutParams layoutParams = (LayoutParams) this.videoMuteIndicator.getLayoutParams();
        LayoutParams layoutParams2 = (LayoutParams) this.videoMuteIndicatorCover.getLayoutParams();
        layoutParams.height = this.mVideoMuteIndicatorHeight;
        layoutParams2.height = this.mVideoMuteIndicatorHeight;
        layoutParams.bottomMargin = this.mVideoMuteIndicatorBottomMargin;
        layoutParams2.bottomMargin = this.mVideoMuteIndicatorBottomMargin;
        this.videoMuteIndicator.setLayoutParams(layoutParams);
        this.videoMuteIndicatorCover.setLayoutParams(layoutParams2);
    }

    private void refreshVideoCenterLineColor(int color) {
        if (this.videoMuteIndicatorCover != null) {
            this.videoMuteIndicatorCover.setBackgroundColor(color);
        }
    }

    private int getLineHeightByOffset(long offset) {
        int itemWidth = (int) (((float) IMAGE_UNIT_WIDTH) * ((1.0f * ((float) offset)) / 1000.0f));
        if (itemWidth <= DensityUtil.dip2px(3.0f)) {
            return DensityUtil.dip2px(20.0f);
        }
        if (itemWidth >= DensityUtil.dip2px(5.0f)) {
            return DensityUtil.dip2px(30.0f);
        }
        return DensityUtil.dip2px(20.0f) + DensityUtil.dip2px((float) (((itemWidth * 10) / DensityUtil.dip2px(5.0f)) - 2));
    }

    private void refreshVideoMuteTvPos() {
        if (this.manager.isAllVideoMute()) {
            int right = getImagesRecyclerLastItemRight();
            if (right >= this.screenWidth) {
                right = this.screenWidth;
            }
            int left = getImagesRecyclerFirstItemLeft();
            if (left <= 0) {
                left = 0;
            }
            LayoutParams layoutParams = (LayoutParams) this.videoMuteTv.getLayoutParams();
            layoutParams.leftMargin = (((right - left) - this.ALL_MUTE_TV_WIDTH) / 2) + left;
            this.videoMuteTv.setLayoutParams(layoutParams);
        }
    }

    private int getImagesRecyclerFirstItemLeft() {
        int firstVisibleItemPosition = this.mImagesLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition <= getImagesRecyclerRealFirstItemPos()) {
            return this.mImagesLayoutManager.findViewByPosition(getImagesRecyclerRealFirstItemPos()).getLeft();
        }
        return 0;
    }

    private int getImagesRecyclerLastItemRight() {
        int lastVisibleItemPosition = this.mImagesLayoutManager.findLastVisibleItemPosition();
        if (lastVisibleItemPosition < 0) {
            return 0;
        }
        if (lastVisibleItemPosition >= getImagesRecyclerRealLastItemPos()) {
            return this.mImagesLayoutManager.findViewByPosition(getImagesRecyclerRealLastItemPos()).getRight();
        }
        return this.screenWidth;
    }

    private int getImagesRecyclerRealFirstItemPos() {
        return 2;
    }

    private int getImagesRecyclerRealLastItemPos() {
        return this.mImageEntities.size() - 2;
    }

    private void refreshAudioMuteTvPos() {
        if (this.manager.isAllAudioMute() && TextUtil.isValidate(getAudioLongVideoModels())) {
            int right = getAudioRecyclerLastItemRight();
            if (right > this.screenWidth) {
                right = this.screenWidth;
            }
            int left = getAudioRecyclerFirstItemLeft();
            if (left < 0) {
                left = 0;
            }
            LayoutParams layoutParams = (LayoutParams) this.audioMuteTv.getLayoutParams();
            layoutParams.leftMargin = (((right - left) - this.ALL_MUTE_TV_WIDTH) / 2) + left;
            this.audioMuteTv.setLayoutParams(layoutParams);
        }
    }

    private int getAudioRecyclerFirstItemLeft() {
        List<LongVideosModel> list = getAudioLongVideoModels();
        if (!TextUtil.isValidate(list)) {
            return 0;
        }
        LongVideosModel entity = (LongVideosModel) list.get(0);
        if (entity == null) {
            return 0;
        }
        int left = (((int) ((entity.getAudioStartTime() * ((long) IMAGE_UNIT_WIDTH)) / 1000)) - this.mAudioScrollX) + this.VIDEO_HALF_SCREEN;
        if (left < 0) {
            return 0;
        }
        return left;
    }

    private int getAudioRecyclerLastItemRight() {
        List<LongVideosModel> list = getAudioLongVideoModels();
        if (TextUtil.isValidate(list)) {
            LongVideosModel entity = (LongVideosModel) list.get(list.size() - 1);
            if (entity != null) {
                int right = (((int) (((entity.getAudioDuration() + entity.getAudioStartTime()) * ((long) IMAGE_UNIT_WIDTH)) / 1000)) - this.mAudioScrollX) + this.VIDEO_HALF_SCREEN;
                if (right >= this.screenWidth) {
                    return this.screenWidth;
                }
                return right;
            }
        }
        return 0;
    }

    private int getTrueNeedScrollX(int pos) {
        int trueScrollX = 0;
        for (int i = 0; i < pos; i++) {
            VideoEditImageEntity entity = (VideoEditImageEntity) this.mImageEntities.get(i);
            if (entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_IMAGE_TYPE) {
                trueScrollX += (int) ((entity.getShowEnd() - entity.getShowStart()) * ((float) IMAGE_UNIT_WIDTH));
            } else if (entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE) {
                trueScrollX += entity.getAdd_type_width();
            } else if (entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE) {
                trueScrollX += entity.getEmpty_type_width();
            } else if (entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_IMAGE_MUTE_TYPE) {
                trueScrollX += this.VIDEO_MUTE_BUTTON_WIDTH;
            }
        }
        return trueScrollX;
    }

    private void calculateExactlyScrollX() {
        int firstVisibleItemPosition = this.mImagesLayoutManager.findFirstVisibleItemPosition();
        View viewByPosition = this.mImagesLayoutManager.findViewByPosition(firstVisibleItemPosition);
        if (viewByPosition != null) {
            int[] rect = new int[2];
            viewByPosition.getLocationOnScreen(rect);
            int x = rect[0];
            int y = rect[1];
            int trueScrollX = getTrueNeedScrollX(firstVisibleItemPosition) - x;
            if (this.mImagesScrollX != trueScrollX) {
                this.mImagesScrollX = trueScrollX;
            }
            calculateExactlyTimeScrollX();
            calculateExactlyTextScrollX();
            calculateExactlyAudioScrollX();
        }
    }

    private void calculateExactlyAudioScrollX() {
        int firstCompletelyVisibleItemPosition = this.mAudioLayoutManager.findFirstCompletelyVisibleItemPosition();
        View viewByPosition = this.mAudioLayoutManager.findViewByPosition(firstCompletelyVisibleItemPosition);
        if (viewByPosition != null) {
            int[] rect = new int[2];
            viewByPosition.getLocationOnScreen(rect);
            int x = rect[0];
            int y = rect[1];
            int trueScrollX = 0;
            for (int i = 0; i < firstCompletelyVisibleItemPosition; i++) {
                trueScrollX += ((VideoEditImageEntity) this.mAudioEntities.get(i)).getEmpty_type_width();
            }
            trueScrollX -= x;
            if (this.mAudioScrollX != trueScrollX) {
                this.mAudioScrollX = trueScrollX;
            }
        }
    }

    private void calculateExactlyTextScrollX() {
        if (this.mTextRecyclerView.getVisibility() == View.VISIBLE) {
            int firstVisibleItemPosition = this.mTextLayoutManager.findFirstVisibleItemPosition();
            View viewByPosition1 = this.mTextLayoutManager.findViewByPosition(firstVisibleItemPosition);
            if (viewByPosition1 != null) {
                int[] rect1 = new int[2];
                viewByPosition1.getLocationOnScreen(rect1);
                int x1 = rect1[0];
                int y1 = rect1[1];
                int trueScrollX1 = 0;
                for (int i = 0; i < firstVisibleItemPosition; i++) {
                    trueScrollX1 += ((VideoEditImageEntity) this.mTextEntities.get(i)).getEmpty_type_width();
                }
                int tempTrueScrollX = trueScrollX1 - x1;
                if (this.mTextScrollX != tempTrueScrollX) {
                    this.mTextScrollX = tempTrueScrollX;
                }
            }
        }
    }

    private void calculateExactlyTimeScrollX() {
        int firstCompletelyVisibleItemPosition = this.mTimesLayoutManager.findFirstCompletelyVisibleItemPosition();
        View viewByPosition = this.mTimesLayoutManager.findViewByPosition(firstCompletelyVisibleItemPosition);
        if (viewByPosition != null) {
            int[] rect = new int[2];
            viewByPosition.getLocationOnScreen(rect);
            int x = rect[0];
            int y = rect[1];
            int trueScrollX = 0;
            for (int i = 0; i < firstCompletelyVisibleItemPosition; i++) {
                VideoEditImageEntity entity = (VideoEditImageEntity) this.mTimesEntities.get(i);
                if (entity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE) {
                    trueScrollX += entity.getEmpty_type_width();
                } else if (entity.getViewType() == 280) {
                    if (i == 1) {
                        trueScrollX += IMAGE_UNIT_WIDTH / 2;
                    } else {
                        trueScrollX += IMAGE_UNIT_WIDTH;
                    }
                }
            }
            trueScrollX -= x;
            if (this.mTimesScrollX != trueScrollX) {
                this.mTimesScrollX = trueScrollX;
            }
        }
    }

    private void relateScroll(int needScrollX, int dx) {
        if (this.mImagesScrollX != needScrollX) {
            this.mImagesRecyclerView.scrollBy(needScrollX - this.mImagesScrollX, 0);
        }
        if (this.mTimesScrollX != needScrollX) {
            this.mTimesRecyclerView.scrollBy(needScrollX - this.mTimesScrollX, 0);
        }
        if (this.mAudioRecyclerView.getVisibility() == View.VISIBLE && this.mAudioScrollX != needScrollX) {
            this.mAudioRecyclerView.scrollBy(needScrollX - this.mAudioScrollX, 0);
        }
        if (this.mTextRecyclerView.getVisibility() == View.VISIBLE && this.mTextScrollX != needScrollX && !this.mTextRecyclerView.isComputingLayout()) {
            this.mTextRecyclerView.scrollBy(needScrollX - this.mTextScrollX, 0);
        }
    }

    public boolean clearIfExistSelect() {
        if (this.curSelectVideo != null) {
            clearCurSelectVideoAndAudio();
            return true;
        }
        clearCurAudioSelected();
        return false;
    }

    public boolean clearCurAudioSelected() {
        if (getCurSelectAudioModel() == null) {
            return false;
        }
        clearMusicTempUiData();
        this.mCurSelectAudioModel = null;
        refreshVisibleAudioItem();
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.refreshAudioEditButton();
        }
        hideAudioSlideView();
        hideAudioVolumeTouchView();
        return true;
    }

    private void clearCurSelectVideo() {
        boolean z = true;
        if (this.curSelectVideo != null) {
            int[] ints = (int[]) this.mModelListHashMap.get(this.curSelectVideo);
            this.curSelectVideo = null;
            hideDurationTvView();
            hideVideoVolumeTouchView();
            if (ints != null) {
                restoreAreaUnSelected(ints);
                if (this.mChangeOtherCallback != null) {
                    ChangeOtherCallback changeOtherCallback = this.mChangeOtherCallback;
                    if (getVideoLongVideosModels().size() != 1) {
                        z = false;
                    }
                    changeOtherCallback.onVideoSelectAreaShownOrHide(false, z, getCurSelectVideo());
                }
            }
        }
    }

    private void clearCurSelectVideoAndAudio() {
        boolean z = true;
        if (this.curSelectVideo == null) {
            clearCurAudioSelected();
            return;
        }
        int[] ints = (int[]) this.mModelListHashMap.get(this.curSelectVideo);
        this.curSelectVideo = null;
        hideDurationTvView();
        hideVideoVolumeTouchView();
        if (ints != null) {
            restoreAreaUnSelected(ints);
            refreshVisibleVideoItem();
            if (this.mChangeOtherCallback != null) {
                ChangeOtherCallback changeOtherCallback = this.mChangeOtherCallback;
                if (getVideoLongVideosModels().size() != 1) {
                    z = false;
                }
                changeOtherCallback.onVideoSelectAreaShownOrHide(false, z, getCurSelectVideo());
            }
            clearCurAudioSelected();
        }
    }

    private void hideAudioVolumeTouchView() {
        if (this.audioVolumeLine.getVisibility() == View.VISIBLE) {
            this.audioVolumeLine.setVisibility(View.GONE);
        }
        if (this.audioVolumeTouch.getVisibility() == View.VISIBLE) {
            this.audioVolumeTouch.setVisibility(View.GONE);
        }
    }

    private void showAudioVolumeTouchView() {
        if (this.audioVolumeLine.getVisibility() != View.VISIBLE) {
            this.audioVolumeLine.setVisibility(View.VISIBLE);
        }
        if (this.audioVolumeTouch.getVisibility() != View.VISIBLE) {
            this.audioVolumeTouch.setVisibility(View.VISIBLE);
        }
    }

    private void hideVideoVolumeTouchView() {
        if (this.videoVolumeLine.getVisibility() == View.VISIBLE) {
            this.videoVolumeLine.setVisibility(View.GONE);
        }
        if (this.videoVolumeTouch.getVisibility() == View.VISIBLE) {
            this.videoVolumeTouch.setVisibility(View.GONE);
        }
    }

    private void showVideoVolumeTouchView() {
        if (this.videoVolumeLine.getVisibility() != View.VISIBLE) {
            this.videoVolumeLine.setVisibility(View.VISIBLE);
        }
        if (this.videoVolumeTouch.getVisibility() != View.VISIBLE) {
            this.videoVolumeTouch.setVisibility(View.VISIBLE);
        }
    }

    private void refreshVideoVolumeTouchView() {
        if (this.curSelectVideo == null || this.curSelectVideo.mediaType != 0 || !this.isMusicEdit || this.manager.isAllVideoMute()) {
            hideVideoVolumeTouchView();
            return;
        }
        int size = getVideoLongVideosModels().size();
        int position = getVideoLongVideosModels().indexOf(this.curSelectVideo);
        if (position < 0 || position >= size) {
            hideVideoVolumeTouchView();
            return;
        }
        int[] ints = (int[]) this.mModelListHashMap.get(this.curSelectVideo);
        if (ints == null) {
            hideVideoVolumeTouchView();
            return;
        }
        int startX;
        int tempStartX;
        View viewByPosition;
        int[] rect;
        int endX;
        int tempEndX;
        int start = ints[0] + 1;
        int end = ints[1] - 1;
        int lastVisibleItemPosition = this.mImagesLayoutManager.findLastVisibleItemPosition();
        if (start < this.mImagesLayoutManager.findFirstVisibleItemPosition()) {
            startX = 0;
            tempStartX = 0;
        } else {
            viewByPosition = this.mImagesLayoutManager.findViewByPosition(start);
            if (viewByPosition != null) {
                rect = new int[2];
                viewByPosition.getLocationOnScreen(rect);
                tempStartX = rect[0];
                startX = rect[0] + VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                if (startX < 0) {
                    startX = 0;
                }
                if (tempStartX < 0) {
                    tempStartX = 0;
                }
            } else {
                return;
            }
        }
        if (end > lastVisibleItemPosition) {
            endX = this.screenWidth;
            tempEndX = endX;
        } else {
            viewByPosition = this.mImagesLayoutManager.findViewByPosition(end);
            if (viewByPosition != null) {
                rect = new int[2];
                viewByPosition.getLocationOnScreen(rect);
                endX = rect[0] + viewByPosition.getWidth();
                if (endX > this.screenWidth) {
                    endX = this.screenWidth;
                    tempEndX = endX;
                } else {
                    tempEndX = endX;
                    endX -= VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                }
            } else {
                return;
            }
        }
        int lineWidth = endX - startX;
        int lineLeftMargin = startX;
        int touchLeftMargin = (((tempEndX - tempStartX) / 2) + tempStartX) - (VOLUME_SLIDE_WIDTH / 2);
        showVideoVolumeTouchView();
        //LogUtil.d(TAG, String.format("startX : %s , endX : %s , lineWidth : %s , lineLeftMargin : %s , touchLeftMargin : %s  , mImageScrollX : %s ", new Object[]{Integer.valueOf(tempStartX), Integer.valueOf(tempEndX), Integer.valueOf(tempLineWidth), Integer.valueOf(lineLeftMargin), Integer.valueOf(touchLeftMargin), Integer.valueOf(this.mImagesScrollX)}));
        setVideoVolumeTouchView(lineWidth, lineLeftMargin, touchLeftMargin, this.curSelectVideo.getVideoVolume());
    }

    private void refreshAudioVolumeTouchView() {
        if (getCurSelectAudioModel() == null || getCurSelectAudioModel().isJustSeeForAudio() || !this.isMusicEdit || this.manager.isAllAudioMute()) {
            hideAudioVolumeTouchView();
            return;
        }
        int size = getAudioLongVideoModels().size();
        int position = getAudioLongVideoModels().indexOf(getCurSelectAudioModel());
        if (position < 0 || position >= size) {
            hideAudioVolumeTouchView();
        } else if (this.audioSelectStart == -1 && this.audioSelectEnd == -1) {
            hideAudioVolumeTouchView();
        } else {
            int start = this.audioSelectStart;
            int end = this.audioSelectEnd;
            LongVideosModel.AudioVolume audioVolume = ((VideoEditImageEntity) this.mAudioEntities.get(this.audioSelectStart)).getAudioVolume();
            int lastVisibleItemPosition = this.mAudioLayoutManager.findLastVisibleItemPosition();
            int firstVisibleItemPosition = this.mAudioLayoutManager.findFirstVisibleItemPosition();
            //LogUtil.d(TAG, String.format("start : %s , end : %s , firstVisibleItemPosition : %s , lastVisibleItemPosition : %s , mAudioScrollX : %s ", new Object[]{Integer.valueOf(start), Integer.valueOf(end), Integer.valueOf(firstVisibleItemPosition), Integer.valueOf(lastVisibleItemPosition), Integer.valueOf(this.mAudioScrollX)}));
            LongVideosModel.AudioVolume startVolume = ((VideoEditImageEntity) this.mAudioEntities.get(this.audioSelectStart)).getAudioVolume();
            LongVideosModel.AudioVolume endVolume = ((VideoEditImageEntity) this.mAudioEntities.get(this.audioSelectEnd)).getAudioVolume();
            if (startVolume == null || endVolume == null) {
                hideAudioVolumeTouchView();
                return;
            }
            int color;
            int startX = (int) (((div((float) startVolume.getStartTime(), 1000.0f) * ((float) IMAGE_UNIT_WIDTH)) - ((float) this.mAudioScrollX)) + ((float) (this.screenWidth / 2)));
            int endX = (int) (((div((float) endVolume.getEndTime(), 1000.0f) * ((float) IMAGE_UNIT_WIDTH)) - ((float) this.mAudioScrollX)) + ((float) (this.screenWidth / 2)));
            if (startX < 0) {
                startX = 0;
            } else if (startX > this.screenWidth) {
                startX = this.screenWidth;
            }
            if (endX < 0) {
                endX = 0;
            } else if (endX > this.screenWidth) {
                endX = this.screenWidth;
            }
            int lineWidth = endX - startX;
            int lineLeftMargin = startX;
            int touchLeftMargin = ((lineWidth / 2) + lineLeftMargin) - (VOLUME_SLIDE_WIDTH / 2);
            if (lineWidth < VOLUME_SLIDE_WIDTH) {
                if (startX == 0) {
                    touchLeftMargin = (endX - VOLUME_SLIDE_WIDTH) - 1;
                } else if (endX == this.screenWidth) {
                    touchLeftMargin = startX + 1;
                }
            }
            LogUtil.d(TAG, String.format("startX : %s , endX : %s , lineWidth : %s , lineLeftMargin : %s , touchLeftMargin : %s , mAudioScrollX : %s ", new Object[]{Integer.valueOf(startX), Integer.valueOf(endX), Integer.valueOf(lineWidth), Integer.valueOf(lineLeftMargin), Integer.valueOf(touchLeftMargin), Integer.valueOf(this.mAudioScrollX)}));
            showAudioVolumeTouchView();
            float volume = audioVolume != null ? audioVolume.getVolume() : this.mCurSelectAudioModel.getAudioVolume();
            if (position % 2 == 0) {
                color = ContextCompat.getColor(App.getContext(), R.color.colorAudioBlue);
            } else {
                color = ContextCompat.getColor(App.getContext(), R.color.colorAudioGreen);
            }
            setAudioVolumeTouchView(lineWidth, lineLeftMargin, touchLeftMargin, volume, color);
        }
    }

    private void setAudioVolumeTouchView(int lineWidth, int lineLeftMargin, int touchLeftMargin, float videoVolume, int color) {
        float moveHeight = (sub(1.0f, videoVolume) * 10.0f) * this.audioVolumeSlideUnit;
        LayoutParams lineLayoutParams = (LayoutParams) this.audioVolumeLine.getLayoutParams();
        if (lineWidth >= 0) {
            lineLayoutParams.width = lineWidth;
        }
        if (lineLeftMargin >= 0) {
            lineLayoutParams.leftMargin = lineLeftMargin;
        }
        lineLayoutParams.bottomMargin = (int) (((float) this.defaultAudioLineBottomMargin) - moveHeight);
        this.audioVolumeLine.setLayoutParams(lineLayoutParams);
        this.audioVolumeLine.setVisibility(View.GONE);
        LayoutParams videoLayoutParams = (LayoutParams) this.audioVolumeTouch.getLayoutParams();
        if (touchLeftMargin != -2147483647) {
            videoLayoutParams.leftMargin = touchLeftMargin;
        }
        videoLayoutParams.bottomMargin = (int) (((float) this.defaultAudioTouchBottomMargin) - moveHeight);
        this.audioVolumeTouch.setLayoutParams(videoLayoutParams);
        this.audioVolumeTouch.setVolumeTvText(getVolumeTextString(videoVolume));
        if (color != -1) {
            this.audioVolumeTouch.setVolumeSlideColor(color);
        }
    }

    private void setVideoVolumeTouchView(int lineWidth, int lineLeftMargin, int touchLeftMargin, float videoVolume) {
        float moveHeight = (sub(1.0f, videoVolume) * 10.0f) * this.videoVolumeSlideUnit;
        LayoutParams lineLayoutParams = (LayoutParams) this.videoVolumeLine.getLayoutParams();
        if (lineWidth >= 0) {
            lineLayoutParams.width = lineWidth;
        }
        if (lineLeftMargin >= 0) {
            lineLayoutParams.leftMargin = lineLeftMargin;
        }
        lineLayoutParams.bottomMargin = (int) (((float) this.defaultVideoLineBottomMargin) - moveHeight);
        this.videoVolumeLine.setLayoutParams(lineLayoutParams);
        if (videoVolume == StaticLayoutUtil.DefaultSpacingadd || videoVolume == 1.0f) {
            this.videoVolumeLine.setVisibility(View.GONE);
        } else {
            this.videoVolumeLine.setVisibility(View.VISIBLE);
        }
        LayoutParams videoLayoutParams = (LayoutParams) this.videoVolumeTouch.getLayoutParams();
        if (touchLeftMargin >= 0) {
            videoLayoutParams.leftMargin = touchLeftMargin;
        }
        videoLayoutParams.bottomMargin = (int) (((float) this.defaultVideoTouchBottomMargin) - moveHeight);
        this.videoVolumeTouch.setLayoutParams(videoLayoutParams);
        this.videoVolumeTouch.setVolumeTvText(getVolumeTextString(videoVolume));
    }

    public static String getVolumeTextString(float volume) {
        int value = (int) (100.0f * volume);
        return String.format("%s%%", new Object[]{Integer.valueOf(value)});
    }

    private void refreshVideoVolumeView(float videoVolume) {
        setVideoVolumeTouchView(-2147483647, -2147483647, -2147483647, videoVolume);
    }

    private void refreshAudioVolumeView(float videoVolume) {
        setAudioVolumeTouchView(-2147483647, -2147483647, -2147483647, videoVolume, -1);
    }

    private void dealImageTypeItemClick(int position) {
        int i = 0;
        if (!clearIfIsInTextSelectModel()) {
            VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mImageEntities.get(position);
            ChangeOtherCallback changeOtherCallback;
            if (videoEditImageEntity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_IMAGE_TYPE) {
                LongVideosModel longVideosModel = videoEditImageEntity.getLongVideosModel();
                if (this.curSelectVideo == longVideosModel) {
                    clearCurSelectVideoAndAudio();
                    hideDurationTvView();
                    hideVideoVolumeTouchView();
                    return;
                }
                if (this.curSelectVideo != null) {
                    restoreAreaUnSelected((int[]) this.mModelListHashMap.get(this.curSelectVideo));
                }
                this.curSelectVideo = longVideosModel;
                measureSelectedStartAndEndX();
                findVideoDurationTvShowRect(this.curSelectVideo);
                if (longVideosModel.mediaType == 0) {
                    refreshVideoVolumeTouchView();
                } else {
                    hideVideoVolumeTouchView();
                }
                this.isJumpToNearby = jumpToNearbySelected();
                setAreaSelected((int[]) this.mModelListHashMap.get(this.curSelectVideo));
                findMusicSelectedPosition();
                if (this.mChangeOtherCallback != null) {
                    boolean z = false;
                    changeOtherCallback = this.mChangeOtherCallback;
                    if (getVideoLongVideosModels().size() == 1) {
                        z = true;
                    }
                    changeOtherCallback.onVideoSelectAreaShownOrHide(true, z, getCurSelectVideo());
                }
            } else if (videoEditImageEntity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE) {
                if (this.mChangeOtherCallback != null) {
                    changeOtherCallback = this.mChangeOtherCallback;
                    if (position != 1) {
                        i = getVideoLongVideosModels().size();
                    }
                    changeOtherCallback.onAddVideosClick(i);
                }
            } else if (videoEditImageEntity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE) {
                clearIfExistSelect();
            } else if (videoEditImageEntity.getViewType() == BaseAdapter.Item.VIDEO_EDIT_IMAGE_MUTE_TYPE) {
                boolean mute = dealVideoMuteClick(videoEditImageEntity);
                if (mute) {
                    clearCurSelectVideo();
                }
                refreshVisibleVideoItem();
                refreshVideoVolumeTouchView();
                this.mVideoEditImageAdapter.notifyItemChanged(position);
                if (this.mChangeOtherCallback != null) {
                    refreshVideoMuteTvPos();
                    this.mChangeOtherCallback.onVideoMuteClick(mute, this.isMusicEdit);
                }
                onSwitchMute();
            }
        }
    }

    private boolean dealVideoMuteClick(VideoEditImageEntity editImageEntity) {
        boolean z;
        boolean videoMuteOn = editImageEntity.isVideoMuteOn();
        if (videoMuteOn) {
            z = false;
        } else {
            z = true;
        }
        editImageEntity.setVideoMuteOn(z);
        VideoAudioPlaybackManager videoAudioPlaybackManager = this.manager;
        if (videoMuteOn) {
            z = false;
        } else {
            z = true;
        }
        videoAudioPlaybackManager.setAllVideoMute(z);
        if (videoMuteOn) {
            return false;
        }
        return true;
    }

    private void clearMusicTempUiData() {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        int size = audioLongVideoModels.size();
        for (int i = 0; i < size; i++) {
            clearAudioModelVolumesSelect((LongVideosModel) audioLongVideoModels.get(i));
        }
    }

    private void clearAudioModelVolumesSelect(LongVideosModel model) {
        if (model != null) {
            ArrayList<LongVideosModel.AudioVolume> audioVolumes = model.getAudioVolumes();
            if (audioVolumes != null) {
                int audioVolumeSize = audioVolumes.size();
                for (int j = 0; j < audioVolumeSize; j++) {
                    ((LongVideosModel.AudioVolume) audioVolumes.get(j)).setSelected(false);
                }
            }
        }
    }

    private void findMusicSelectedPosition() {
        if (this.curSelectVideo != null) {
            int i;
            clearMusicTempUiData();
            List<LongVideosModel> videoLongVideosModels = getVideoLongVideosModels();
            long videoStartTime = 0;
            for (i = 0; i < videoLongVideosModels.indexOf(this.curSelectVideo); i++) {
                videoStartTime += ((LongVideosModel) videoLongVideosModels.get(i)).getCurrentDuration();
            }
            long videoEndTime = videoStartTime + this.curSelectVideo.getCurrentDuration();
            boolean hasFind = false;
            List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
            int size = audioLongVideoModels.size();
            LongVideosModel.AudioVolume selectFirstVolume = null;
            LongVideosModel.AudioVolume selectEndVolume = null;
            for (i = 0; i < size; i++) {
                LongVideosModel audioModel = (LongVideosModel) audioLongVideoModels.get(i);
                long audioEndTime = audioModel.getAudioStartTime() + audioModel.getAudioDuration();
                ArrayList<LongVideosModel.AudioVolume> audioVolumes;
                LongVideosModel.AudioVolume audioVolume;
                if (audioModel.getAudioStartTime() <= videoStartTime && audioEndTime > videoStartTime) {
                    this.mCurSelectAudioModel = audioModel;
                    this.curSelectAudioModelPosition = i;
                    refreshCurSelectAudioEndPos();
                    hasFind = true;
                    audioVolumes = audioModel.getAudioVolumes();
                    if (audioEndTime >= videoEndTime) {
                        int audioSize = audioVolumes.size();
                        for (int m = 0; m < audioSize; m++) {
                            audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(m);
                            if (audioVolume.getStartTime() >= videoStartTime && audioVolume.getEndTime() <= videoEndTime) {
                                audioVolume.setSelected(true);
                                selectEndVolume = audioVolume;
                                selectFirstVolume = audioVolume;
                                break;
                            }
                        }
                    } else {
                        LongVideosModel.AudioVolume end = (LongVideosModel.AudioVolume) audioVolumes.get(audioVolumes.size() - 1);
                        end.setSelected(true);
                        selectEndVolume = end;
                        selectFirstVolume = end;
                    }
                } else if (audioModel.getAudioStartTime() >= videoStartTime && audioModel.getAudioStartTime() < videoEndTime) {
                    boolean finalFind = false;
                    if (!hasFind) {
                        finalFind = true;
                        this.mCurSelectAudioModel = audioModel;
                        this.curSelectAudioModelPosition = i;
                        refreshCurSelectAudioEndPos();
                        hasFind = true;
                    }
                    audioVolumes = audioModel.getAudioVolumes();
                    int size1 = audioVolumes.size();
                    for (int j = 0; j < size1; j++) {
                        audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(j);
                        if (audioVolume.getEndTime() <= videoEndTime) {
                            audioVolume.setSelected(true);
                            if (finalFind) {
                                selectEndVolume = audioVolume;
                                selectFirstVolume = audioVolume;
                            }
                        }
                    }
                } else if (audioModel.getAudioStartTime() > videoEndTime) {
                    break;
                }
            }
            refreshAudioSelectPos(this.mAudioEntities, selectFirstVolume, selectEndVolume);
            refreshAudioVolumeTouchView();
            measureAudioSelectedStartAndEndX();
            updateAudioSlideView(this.curSelectAudioModelPosition);
            refreshVisibleAudioItem();
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshAudioEditButton();
            }
        }
    }

    private void findVideoDurationTvShowRect(LongVideosModel curSelectVideo) {
        if (curSelectVideo == null || this.isMusicEdit) {
            hideDurationTvView();
            return;
        }
        int size = getVideoLongVideosModels().size();
        int position = getVideoLongVideosModels().indexOf(curSelectVideo);
        if (position < 0 || position >= size) {
            hideDurationTvView();
            return;
        }
        int[] ints = (int[]) this.mModelListHashMap.get(curSelectVideo);
        if (ints == null) {
            hideDurationTvView();
            return;
        }
        int x;
        int end = ints[1];
        if (end <= this.mImagesLayoutManager.findLastVisibleItemPosition()) {
            View viewByPosition = this.mImagesLayoutManager.findViewByPosition(end);
            if (viewByPosition != null) {
                int[] rect = new int[2];
                viewByPosition.getLocationOnScreen(rect);
                x = rect[0];
            } else {
                return;
            }
        }
        x = this.screenWidth;
        refreshDurationTvPosition(x, curSelectVideo.getCurrentDuration());
    }

    private void hideDurationTvView() {
        if (this.videoDurationTv != null && this.videoDurationTv.getVisibility() == View.VISIBLE) {
            this.videoDurationTv.setVisibility(View.GONE);
        }
    }

    private String getDurationString(long duration) {
        float div = div((float) duration, 1000.0f);
        return String.format("%s\"", new Object[]{Float.valueOf(div)});
    }

    private void changeDurationTvLeftMargin(int changeTime, long duration) {
        if (this.videoDurationTv != null) {
            this.videoDurationTv.setText(getDurationString(duration));
            LayoutParams layoutParams = (LayoutParams) this.videoDurationTv.getLayoutParams();
            layoutParams.leftMargin += IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (IMAGE_UNIT_WIDTH * changeTime))).intValue();
            this.videoDurationTv.setLayoutParams(layoutParams);
        }
    }

    private void refreshDurationTvPosition(int x, long duration) {
        if (this.videoDurationTv != null) {
            boolean isLeft = true;
            int itemWidth = (int) (((float) IMAGE_UNIT_WIDTH) * div((float) duration, 1000.0f));
            String durationString = getDurationString(duration);
            this.videoDurationTv.setText(durationString);
            int measureText = (int) this.videoDurationTv.getPaint().measureText(durationString);
            if (itemWidth < DensityUtil.dip2px(20.0f) + measureText) {
                isLeft = false;
            }
            LayoutParams layoutParams = (LayoutParams) this.videoDurationTv.getLayoutParams();
            if (isLeft) {
                layoutParams.leftMargin = (x - DensityUtil.dip2px(10.0f)) - measureText;
            } else {
                layoutParams.leftMargin = DensityUtil.dip2px(20.0f) + x;
            }
            this.videoDurationTv.setLayoutParams(layoutParams);
            if (this.videoDurationTv.getVisibility() != View.VISIBLE) {
                this.videoDurationTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkAndBindRelateTextModelAndVideoModel() {
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        if (!TextUtil.isNull(longVideosModels)) {
            List<LongVideosModel> textVideosModels = getTextVideosModels();
            if (!TextUtil.isNull(textVideosModels)) {
                int size = longVideosModels.size();
                int textSize = textVideosModels.size();
                long startTime = 0;
                long endTime = 0;
                int textStartPos = 0;
                for (int i = 0; i < size; i++) {
                    LongVideosModel model = (LongVideosModel) longVideosModels.get(i);
                    endTime += model.getCurrentDuration();
                    for (int j = textStartPos; j < textSize; j++) {
                        LongVideosModel textModel = (LongVideosModel) textVideosModels.get(j);
                        long textPoint = (long) (((float) textModel.getStartTimeMs()) + (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT));
                        if (textPoint > endTime) {
                            break;
                        }
                        if (textPoint >= startTime) {
                            textModel.setPointToVideoModel(model);
                            textModel.setRelateStartTime(textModel.getStartTimeMs() - startTime);
                           // LogUtil.d(TAG, String.format(" checkAndBindRelateTextModelAndVideoModel  relateStartTime : %s ", new Object[]{Long.valueOf(relateStartTime)}));
                            textStartPos = j + 1;
                        }
                    }
                    startTime += model.getCurrentDuration();
                }
            }
        }
    }

    public List<LongVideosModel> resetTextPosAfterOrder(List<LongVideosModel> videosModels) {
        if (TextUtil.isNull(videosModels)) {
            return null;
        }
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (TextUtil.isNull(textVideosModels)) {
            return null;
        }
        int i;
        LongVideosModel textModel;
        LogUtil.d("videosModelList : " + videosModels.toString());
        ArrayList<LongVideosModel> newTextModels = new ArrayList();
        int size = videosModels.size();
        int textSize = textVideosModels.size();
        long startTime = 0;
        for (i = 0; i < size; i++) {
            LongVideosModel model = (LongVideosModel) videosModels.get(i);
            for (int j = 0; j < textSize; j++) {
                textModel = (LongVideosModel) textVideosModels.get(j);
                if (textModel.getPointToVideoModel() == model) {
                    long time = textModel.getStartTimeMs();
                    textModel.setStartTime(textModel.getRelateStartTime() + startTime);
                    long result = textModel.getStartTimeMs();
                    //LogUtil.d(TAG, String.format("resetTextPosAfterOrder time : %s , result : %s ", new Object[]{Long.valueOf(time), Long.valueOf(result)}));
                    newTextModels.add(textModel);
                }
            }
            startTime += model.getCurrentDuration();
        }
        int newSize = newTextModels.size();
        for (i = 0; i < newSize; i++) {
            textModel = (LongVideosModel) newTextModels.get(i);
            int next = i + 1;
            if (next < newSize) {
                LongVideosModel nextModel = (LongVideosModel) newTextModels.get(next);
                if (textModel.getStartTimeMs() + textModel.getCurrentDurationValue() > nextModel.getStartTimeMs()) {
                    textModel.setCurrentDuration(nextModel.getStartTimeMs() - textModel.getStartTimeMs());
                }
            }
        }
        LogUtil.d(TAG, String.format("resetTextPosAfterOrder newSize : %s ", new Object[]{Integer.valueOf(newSize)}));
        textVideosModels.clear();
        textVideosModels.addAll(newTextModels);
        removeInvalidTextModel();
        checkAndBindRelateTextModelAndVideoModel();
        refreshEndRelate();
        refreshCacheTextModels();
        refreshTextView(getTextVideosModels(), null, true);
        return getTextVideosModels();
    }

    private long[] deleteRelateTextModel(int index) {
        long[] result = new long[2];
        int firstTextIndex = -1;
        result[0] = (long) -1;
        result[1] = 0;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        if (!TextUtil.isNull(longVideosModels)) {
            List<LongVideosModel> textVideosModels = getTextVideosModels();
            if (!TextUtil.isNull(textVideosModels)) {
                int i;
                long start = 0;
                long end = 0;
                long deleteDuration = 0;
                for (i = 0; i <= index; i++) {
                    LongVideosModel model = (LongVideosModel) longVideosModels.get(i);
                    end += model.getCurrentDuration();
                    if (i == index) {
                        deleteDuration = model.getCurrentDuration();
                        start = end - deleteDuration;
                    }
                }
                result[1] = deleteDuration;
                for (i = textVideosModels.size() - 1; i >= 0; i--) {
                    long temp = (long) (((float) ((LongVideosModel) textVideosModels.get(i)).getStartTimeMs()) + (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT));
                    if (temp > end) {
                        firstTextIndex = i;
                    } else if (temp >= start && temp <= end) {
                        textVideosModels.remove(i);
                        firstTextIndex = i;
                    }
                }
                result[0] = (long) firstTextIndex;
            }
        }
        return result;
    }

    public void removeCurSelectAudio() {
        LongVideosModel curSelectAudioModel = getCurSelectAudioModel();
        if (curSelectAudioModel != null) {
            List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
            audioLongVideoModels.remove(curSelectAudioModel);
            clearCurSelectAudioAndRefreshTouchView();
            refreshAudioEntities(audioLongVideoModels);
            refreshVisibleAudioItem();
            this.mAudioRecyclerView.post(new Runnable() {
                public void run() {
                    VideoEditHelper.this.calculateExactlyAudioScrollX();
                    VideoEditHelper.this.relateScroll(VideoEditHelper.this.mImagesScrollX, 0);
                }
            });
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshAudioEditButton();
            }
            callAudioChange();
            onSlideTouchUp();
        }
    }

    public void cancelGetWaveRunnableByModelPos(int modelPos) {
        int size = getAudioLongVideoModels().size();
        if (modelPos >= 0 && modelPos < size) {
            LongVideosModel model = (LongVideosModel) getAudioLongVideoModels().get(modelPos);
            if (model != null) {
                VideoEditImageEntity videoEditImageEntity = model.getVideoEditImageEntity();
                if (videoEditImageEntity != null) {
                    cancelRunningGetWaveRunable(this.mAudioEntities.indexOf(videoEditImageEntity));
                }
            }
        }
    }

    private void cancelRunningGetWaveRunable(int indexOf) {
        ViewHolder viewHolder = this.mAudioRecyclerView.findViewHolderForAdapterPosition(indexOf);
        Log.e("debug",getClass().getSimpleName()+":cancelRunningGetWaveRunnable==>"+indexOf);
//        if (viewHolder instanceof VideoEditMusicEditHolder) {
//            ((VideoEditMusicEditHolder) viewHolder).cancelAllRunningRunable();
//        }
    }

    public void removeCurSelectVideo() {
        if (this.curSelectVideo != null) {
            int i;
            int visibleStart;
            int visibleEnd;
            final int selectIndex = getVideoLongVideosModels().indexOf(this.curSelectVideo);
            long[] textDeleteResult = deleteRelateTextModel(selectIndex);
            final int firstTextIndex = (int) textDeleteResult[0];
            final long deleteDuration = textDeleteResult[1];
            LogUtil.d(TAG, String.format("removeCurSelectVideo firstTextIndex : %s , deleteDuration : %s ", new Object[]{Integer.valueOf(firstTextIndex), Long.valueOf(deleteDuration)}));
            int[] ints = (int[]) this.mModelListHashMap.get(this.curSelectVideo);
            ((VideoEditImageEntity) this.mImageEntities.get(ints[0])).setWhite_area_type(0);
            ((VideoEditImageEntity) this.mImageEntities.get(ints[1])).setWhite_area_type(0);
            int start = ints[0];
            int end = ints[1];
            for (i = end - 1; i > start; i--) {
                this.mImageEntities.remove(i);
            }
            int count = (end - 1) - start;
            int size = getVideoLongVideosModels().size();
            if (selectIndex < size - 1) {
                for (i = selectIndex + 1; i < size; i++) {
                    reduceVideoModelArea((LongVideosModel) getVideoLongVideosModels().get(i), count);
                }
            }
            getVideoLongVideosModels().remove(this.curSelectVideo);
            long currentDuration = this.curSelectVideo.getCurrentDuration();
            this.sumTime -= (float) currentDuration;
            calculateMaxScrollX();
            int firstVisibleItemPosition = this.mImagesLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = this.mImagesLayoutManager.findLastVisibleItemPosition();
            if (start > firstVisibleItemPosition) {
                visibleStart = start;
            } else {
                visibleStart = firstVisibleItemPosition;
            }
            if (end < lastVisibleItemPosition) {
                visibleEnd = end;
            } else {
                visibleEnd = lastVisibleItemPosition;
            }
            final VideoEditImageEntity editImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            int itemCount = (visibleEnd - visibleStart) + 1;
            final int empty_type_width = itemCount * VideoEditModuleView.IMAGE_UNIT_WIDTH;
            editImageEntity.setEmpty_type_width(empty_type_width);
            final int index = start + 1;
            this.mImageEntities.add(index, editImageEntity);
            if (this.mModelListHashMap != null && this.mModelListHashMap.containsKey(this.curSelectVideo)) {
                this.mModelListHashMap.remove(this.curSelectVideo);
            }
            this.curSelectVideo = null;
            this.mVideoEditImageAdapter.notifyItemRangeChanged(visibleStart, itemCount);
            refreshTextView(getTextVideosModels(), this.curTextVideoModel, true);
            List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
            if (TextUtil.isValidate(audioLongVideoModels)) {
                long needDelete = currentDuration;
                int audioEnd = audioLongVideoModels.size() - 1;
                long endTime = Float.valueOf(this.sumTime).longValue();
                boolean changed = false;
                while (needDelete > 0 && audioEnd >= 0) {
                    LongVideosModel audioEndModel = (LongVideosModel) audioLongVideoModels.get(audioEnd);
                    if (!audioEndModel.isJustSeeForAudio()) {
                        if (audioEndModel.getAudioStartTime() + audioEndModel.getAudioDuration() <= endTime) {
                            break;
                        }
                        audioEndModel.setAudioDuration(endTime - audioEndModel.getAudioStartTime());
                        changed = true;
                        if (audioEndModel.getAudioDuration() >= 0) {
                            break;
                        }
                        audioEnd--;
                        audioLongVideoModels.remove(audioEndModel);
                    } else {
                        audioEnd--;
                    }
                }
                if (changed) {
                    removeInvalidAudioModel();
                    refreshAudioVolumeSelectPostAndRefreshVolumeTouch();
                }
            }
            AnimationUtil.setValueAnimator(false, new ValueAnimatorCallback() {
                public void updateCallback(float value) {
                    editImageEntity.setEmpty_type_width((int) (((float) empty_type_width) * value));
                    VideoEditHelper.this.mVideoEditImageAdapter.notifyItemChanged(index);
                    VideoEditHelper.this.reduceStartTimeFromTextIndexAndRefresh(firstTextIndex, deleteDuration - ((long) (((float) deleteDuration) * value)));
                }

                public void startCallback() {
                }

                public void endCallback() {
                    VideoEditHelper.this.mImageEntities.remove(index);
                    VideoEditHelper.this.mVideoEditImageAdapter.notifyItemRemoved(index);
                    List<LongVideosModel> textVideosModels = VideoEditHelper.this.getTextVideosModels();
                    if (textVideosModels != null) {
                        int size1 = textVideosModels.size();
                        if (size1 > 0) {
                            for (int i = size1 - 1; i >= 0; i--) {
                                LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                                if (((float) (model.getStartTimeMs() + model.getCurrentDurationValue())) <= VideoEditHelper.this.sumTime) {
                                    break;
                                }
                                model.setCurrentDuration((long) (VideoEditHelper.this.sumTime - ((float) model.getStartTimeMs())));
                                if (model.getStartTimeMs() < 0 || model.getCurrentDurationValue() < 1000) {
                                    textVideosModels.remove(i);
                                }
                            }
                        }
                    }
                    VideoEditHelper.this.checkAndBindRelateTextModelAndVideoModel();
                    VideoEditHelper.this.refreshCacheTextModels();
                    VideoEditHelper.this.refreshEndRelate();
                    VideoEditHelper.this.refreshTextView(textVideosModels, VideoEditHelper.this.curTextVideoModel, true);
                    VideoEditHelper.this.refreshTimesView();
                    long needPlayTime = VideoEditHelper.getNeedPlayTime(selectIndex, VideoEditHelper.this.getVideoLongVideosModels());
                    VideoEditHelper.this.refreshAudioView();
                    if (VideoEditHelper.this.needCheckMissingFootage) {
                        VideoEditHelper.this.refreshMissingFootage();
                    }
                    VideoEditHelper.this.callVideoChange(needPlayTime * 1000);
                    VideoEditHelper.this.mChangeOtherCallback.videoTimeChange();
//                    VideoEditHelper.this.mImagesRecyclerView.post(VideoEditHelper$13$.Lambda.1.lambdaFactory$(this, index));
                    mImagesRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            lambda$endCallback$0(index);
                        }
                    });
                }

                private /* synthetic */ void lambda$endCallback$0(int index) {
                    VideoEditHelper.this.calculateExactlyScrollX();
                    VideoEditHelper.this.scrollToRelatePosAfterRemove(index);
                }
            });
        }
    }

    public static long getNeedPlayTime(int insertPosition, List<LongVideosModel> longVideosModels) {
        long result = 0;
        if (TextUtil.isValidate(longVideosModels)) {
            for (int i = 0; i < insertPosition; i++) {
                result += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
            }
        }
        return result;
    }

    private void reduceStartTimeFromTextIndexAndRefresh(int index, long reduceDuration) {
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (textVideosModels != null) {
            int size = textVideosModels.size();
            if (index >= 0 && index < size) {
                for (int i = index; i < size; i++) {
                    LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                    model.setStartTime(model.getCacheStartTime() - reduceDuration);
                    if (i == index) {
                        int pre = index - 1;
                        if (pre >= 0) {
                            LongVideosModel prev = (LongVideosModel) textVideosModels.get(pre);
                            if (prev.getStartTimeMs() + prev.getCurrentDurationValue() > model.getStartTimeMs()) {
                                prev.setCurrentDuration(model.getStartTimeMs() - prev.getStartTimeMs());
                            }
                        }
                    }
                    LogUtil.d(TAG, String.format("reduceStartTimeFromTextIndexAndRefresh i : %s , cacheStartTime : %s , startTime : %s ", new Object[]{Integer.valueOf(i), Long.valueOf(model.getCacheStartTime()), Long.valueOf(model.getStartTimeMs())}));
                }
                if (reduceDuration != 0) {
                    refreshTextView(textVideosModels, this.curTextVideoModel, false);
                }
            }
        }
    }

    public void scrollToRelatePosAfterReorder(final int pos) {
        if (getVideoLongVideosModels() != null) {
            int size = getVideoLongVideosModels().size();
            if (pos >= 0 && pos < size) {
//                this.mImagesRecyclerView.post(6.lambdaFactory$(this, ((int[]) this.mModelListHashMap.get((LongVideosModel) getVideoLongVideosModels().get(pos)))[0] + 1));
                mImagesRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = ((int[]) mModelListHashMap.get((LongVideosModel) getVideoLongVideosModels().get(pos)))[0] + 1;
                        lambda$scrollToRelatePosAfterReorder$7(index);
                    }
                });
            }
        }
    }

    private /* synthetic */ void lambda$scrollToRelatePosAfterReorder$7(int position) {
        this.isAutoScrolling = true;
        calculateExactlyScrollX();
        scrollToRelatePosAfterRemove(position);
    }

    private void scrollToRelatePosAfterRemove(int position) {
        int trueNeedScrollX = getTrueNeedScrollX(position) - (this.screenWidth / 2);
        if (trueNeedScrollX < 0) {
            trueNeedScrollX = 0;
        }
        int scrollX = trueNeedScrollX - this.mImagesScrollX;
        this.mImagesRecyclerView.smoothScrollBy(scrollX, 0);
        if (scrollX == 0) {
            relateScroll(this.mImagesScrollX, 0);
        }
    }

    private int getLeftUnNeedWidth() {
        return this.screenWidth / 2;
    }

    public void setViewScrollTo(double percent) {
        if (this.mMaxScrollX != 0) {
            this.isAutoScrolling = true;
            if (percent < 0.0d) {
                percent = 0.0d;
            }
            if (percent > 1.0d) {
                percent = 1.0d;
            }
            double needScrollX = ((double) this.mMaxScrollX) * percent;
            if (((double) this.lastNeedAutoScrollX) != needScrollX) {
                int scrollX = (int) (needScrollX - ((double) this.mImagesScrollX));
                this.mImagesRecyclerView.scrollBy(scrollX, 0);
                this.lastNeedAutoScrollX = scrollX;
            }
        }
    }

    public long getCurrentCenterTime() {
        return (long) (div((float) this.mImagesScrollX, (float) IMAGE_UNIT_WIDTH) * 1000.0f);
    }

    public long getTextStartTimeAfterInsertBlack(int insertPosition) {
        long startTime = -1;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        if (longVideosModels != null) {
            int size = longVideosModels.size();
            if (insertPosition >= 0 && insertPosition < size) {
                startTime = 0;
                for (int i = 0; i < insertPosition; i++) {
                    startTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
                }
            }
        }
        return startTime;
    }

    public float[] getLongVideosModelsTime() {
        int size = getVideoLongVideosModels().size();
        float[] percentTimes = new float[size];
        long startTime = 0;
        for (int i = 0; i < size; i++) {
            startTime += ((LongVideosModel) getVideoLongVideosModels().get(i)).getCurrentDuration();
            percentTimes[i] = ((float) startTime) / this.sumTime;
        }
        return percentTimes;
    }

    private boolean jumpToNearbySelected() {
        int firstPosition = 0;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        int end = longVideosModels.indexOf(this.curSelectVideo);
        float videoWidth = StaticLayoutUtil.DefaultSpacingadd;
        for (int i = 0; i <= end; i++) {
            videoWidth = ((((float) ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration()) * 1.0f) / 1000.0f) * ((float) IMAGE_UNIT_WIDTH);
            firstPosition = (int) (((float) firstPosition) + videoWidth);
        }
        float start = ((float) firstPosition) - videoWidth;
        boolean needScroll = true;
        if (this.mImagesScrollX <= firstPosition && ((float) this.mImagesScrollX) >= start) {
            needScroll = false;
        }
        LogUtil.d(TAG, String.format("jumpToNearbySelected mImagesScrollX : %s , firstPosition : %s , start : %s ", new Object[]{Integer.valueOf(this.mImagesScrollX), Integer.valueOf(firstPosition), Float.valueOf(start)}));
        int resultScrollX;
        if (needScroll) {
            int needScrollX;
            int toEnd = firstPosition - this.mImagesScrollX;
            int toStart = (int) (((float) this.mImagesScrollX) - start);
            int absToEnd = Math.abs(toEnd);
            int absToStart = Math.abs(toStart);
            resultScrollX = this.mImagesScrollX;
            if (absToEnd > absToStart) {
                needScrollX = -toStart;
                resultScrollX = (resultScrollX + (-toStart)) + VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
            } else {
                needScrollX = toEnd;
                resultScrollX = (resultScrollX + toEnd) - VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
            }
            if (resultScrollX > this.mMaxScrollX) {
                resultScrollX = this.mMaxScrollX;
            }
            if (resultScrollX < 0) {
                resultScrollX = 0;
            }
            callVideoSeekTo(resultScrollX);
            this.needResetOutScrollX = resultScrollX;
            this.needResetOutTextModel = true;
            this.isAutoScrolling = true;
            this.mImagesRecyclerView.smoothScrollBy(needScrollX, 0);
            return true;
        }
        boolean nearbyEnd = ((float) Math.abs(this.mImagesScrollX - firstPosition)) < Math.abs(((float) this.mImagesScrollX) - start);
        int i2 = this.mImagesScrollX;
        if (!nearbyEnd) {
            firstPosition = (int) start;
        }
        if (isInCenterArea(i2, firstPosition)) {
            resultScrollX = this.mImagesScrollX;
            if (nearbyEnd) {
                resultScrollX -= VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
            } else {
                resultScrollX += VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
            }
            if (resultScrollX > this.mMaxScrollX) {
                resultScrollX = this.mMaxScrollX;
            }
            if (resultScrollX < 0) {
                resultScrollX = 0;
            }
            callVideoSeekTo(resultScrollX);
            refreshOutCurTextModel(resultScrollX);
        }
        return false;
    }

    private void measureSelectedStartAndEndX() {
        if (this.curSelectVideo != null) {
            this.selectedEndScrollX = StaticLayoutUtil.DefaultSpacingadd;
            List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
            int end = longVideosModels.indexOf(this.curSelectVideo);
            float videoWidth = StaticLayoutUtil.DefaultSpacingadd;
            for (int i = 0; i <= end; i++) {
                videoWidth = ((((float) ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration()) * 1.0f) / 1000.0f) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH);
                this.selectedEndScrollX += videoWidth;
            }
            this.selectedStartScrollX = this.selectedEndScrollX - videoWidth;
            LogUtil.d(TAG, String.format("measureSelectedStartAndEndX  selectedStartScrollX : %s , selectedEndScrollX : %s ", new Object[]{Float.valueOf(this.selectedStartScrollX), Float.valueOf(this.selectedEndScrollX)}));
        }
    }

    private void measureAudioSelectedStartAndEndX() {
        if (this.curSelectAudioModelPosition != -1) {
            this.audioSelectedEndScrollX = StaticLayoutUtil.DefaultSpacingadd;
            LongVideosModel model = getCurSelectAudioModel();
            if (model != null) {
                long audioStartTime = model.getAudioStartTime();
                this.audioSelectedStartScrollX = ((((float) audioStartTime) * 1.0f) / 1000.0f) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH);
                this.audioSelectedEndScrollX = ((((float) (audioStartTime + model.getAudioDuration())) * 1.0f) / 1000.0f) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH);
            }
        }
    }

    private boolean checkAudioCenterIsInSelectedArea(int scrollX) {
        if (this.curSelectAudioModelPosition != -1 && ((float) scrollX) >= this.audioSelectedStartScrollX && ((float) scrollX) <= this.audioSelectedEndScrollX) {
            return true;
        }
        return false;
    }

    private boolean checkCenterIsInSelectedArea(int scrollX) {
        boolean z = true;
        if (this.curSelectVideo == null) {
            return false;
        }
        LogUtil.d(TAG, String.format("checkCenterIsInSelectedArea  selectedStartScrollX : %s , selectedEndScrollX : %s , scrollX : %s , ", new Object[]{Float.valueOf(this.selectedStartScrollX), Float.valueOf(this.selectedEndScrollX), Integer.valueOf(scrollX)}));
        if (((float) scrollX) < this.selectedStartScrollX || ((float) scrollX) > this.selectedEndScrollX) {
            z = false;
        }
        return z;
    }

    public int getInsertPosition() {
        return this.insertPosition;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0154  */
    public void splitCurrentCenter() {
        /*
        r25 = this;
        r0 = r25;
        r0 = r0.mImagesScrollX;
        r20 = r0;
        r0 = r20;
        r0 = (float) r0;
        r20 = r0;
        r21 = IMAGE_UNIT_WIDTH;
        r0 = r21;
        r0 = (float) r0;
        r21 = r0;
        r20 = div(r20, r21);
        r21 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r12 = r20 * r21;
        r0 = (long) r12;
        r18 = r0;
        r17 = 0;
        r8 = r25.getVideoLongVideosModels();
        r13 = r8.size();
        r20 = TAG;
        r21 = "splitCurrentCenter originSplitTime : %s , splitTime : %s , size : %s ";
        r22 = 3;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = java.lang.Float.valueOf(r12);
        r22[r23] = r24;
        r23 = 1;
        r24 = java.lang.Long.valueOf(r18);
        r22[r23] = r24;
        r23 = 2;
        r24 = java.lang.Integer.valueOf(r13);
        r22[r23] = r24;
        r21 = java.lang.String.format(r21, r22);
        com.blink.academy.onetake.support.debug.LogUtil.d(r20, r21);
        r16 = 0;
        r7 = 0;
        r6 = 0;
    L_0x0056:
        if (r6 >= r13) goto L_0x00f0;
    L_0x0058:
        r9 = r8.get(r6);
        r9 = (com.blink.academy.onetake.bean.longvideo.LongVideosModel) r9;
        r0 = r17;
        r0 = (long) r0;
        r20 = r0;
        r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r20 <= 0) goto L_0x0168;
    L_0x0067:
        r0 = r17;
        r0 = (long) r0;
        r20 = r0;
        r22 = r9.getCurrentDuration();
        r20 = r20 + r22;
        r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r20 >= 0) goto L_0x0168;
    L_0x0076:
        r7 = r6;
        r16 = r9.splitModel();
        r0 = r17;
        r0 = (long) r0;
        r20 = r0;
        r14 = r18 - r20;
        r0 = r16;
        r0.setDurationShowValue(r14);
        r20 = r9.getAudioVolumes();
        r0 = r16;
        r1 = r20;
        r0.setAudioVolumes(r1);
        r20 = r9.getStartTimeMs();
        r22 = r16.getCurrentDuration();
        r10 = r20 + r22;
        r20 = r9.getCurrentDuration();
        r4 = r20 - r14;
        r20 = TAG;
        r21 = "splitCurrentCenter splitCurDuration : %s , newStartTime : %s , currentDuration : %s ";
        r22 = 3;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = java.lang.Long.valueOf(r14);
        r22[r23] = r24;
        r23 = 1;
        r24 = java.lang.Long.valueOf(r10);
        r22[r23] = r24;
        r23 = 2;
        r24 = java.lang.Long.valueOf(r4);
        r22[r23] = r24;
        r21 = java.lang.String.format(r21, r22);
        com.blink.academy.onetake.support.debug.LogUtil.d(r20, r21);
        r20 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r20 = (r14 > r20 ? 1 : (r14 == r20 ? 0 : -1));
        if (r20 < 0) goto L_0x00d9;
    L_0x00d3:
        r20 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r20 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
        if (r20 >= 0) goto L_0x00da;
    L_0x00d9:
        return;
    L_0x00da:
        r9.setDurationShowValue(r4);
        r9.setStartTime(r10);
        r0 = r25;
        r0 = r0.curSelectVideo;
        r20 = r0;
        if (r20 == 0) goto L_0x00f0;
    L_0x00e8:
        r20 = 0;
        r0 = r20;
        r1 = r25;
        r1.curSelectVideo = r0;
    L_0x00f0:
        if (r16 == 0) goto L_0x00ff;
    L_0x00f2:
        r0 = r16;
        r8.add(r7, r0);
        r25.refreshEndRelate();
        r0 = r25;
        r0.refreshVideosView(r8);
    L_0x00ff:
        r0 = r25;
        r0 = r0.mChangeOtherCallback;
        r20 = r0;
        if (r20 == 0) goto L_0x013f;
    L_0x0107:
        r0 = r25;
        r0 = r0.mChangeOtherCallback;
        r20 = r0;
        r21 = 0;
        r22 = 0;
        r0 = r25;
        r0 = r0.isMusicEdit;
        r23 = r0;
        r20.setSplitBtnVisible(r21, r22, r23);
        r0 = r25;
        r0 = r0.mChangeOtherCallback;
        r21 = r0;
        r22 = 0;
        r20 = r8.size();
        r23 = 1;
        r0 = r20;
        r1 = r23;
        if (r0 != r1) goto L_0x017c;
    L_0x012e:
        r20 = 1;
    L_0x0130:
        r23 = r25.getCurSelectVideo();
        r0 = r21;
        r1 = r22;
        r2 = r20;
        r3 = r23;
        r0.onVideoSelectAreaShownOrHide(r1, r2, r3);
    L_0x013f:
        r0 = r25;
        r0 = r0.curSelectVideo;
        r20 = r0;
        r0 = r25;
        r1 = r20;
        r0.findVideoDurationTvShowRect(r1);
        r0 = r25;
        r0 = r0.needCheckMissingFootage;
        r20 = r0;
        if (r20 == 0) goto L_0x0157;
    L_0x0154:
        r25.refreshMissingFootage();
    L_0x0157:
        r25.refreshCacheTextModels();
        r25.refreshAudioAndVideoRelation();
        r20 = -1;
        r0 = r25;
        r1 = r20;
        r0.callVideoChangeAfterSplit(r1);
        goto L_0x00d9;
    L_0x0168:
        r0 = r17;
        r0 = (long) r0;
        r20 = r0;
        r22 = r9.getCurrentDuration();
        r20 = r20 + r22;
        r0 = r20;
        r0 = (int) r0;
        r17 = r0;
        r6 = r6 + 1;
        goto L_0x0056;
    L_0x017c:
        r20 = 0;
        goto L_0x0130;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.ui.helper.VideoEditHelper.splitCurrentCenter():void");
    }

    private boolean isInCenterArea(int scrollX, int videoWidth) {
        int dip2px = VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        return scrollX > videoWidth - dip2px && scrollX < videoWidth + dip2px;
    }

    private void restoreAreaUnSelected(int[] ints) {
        if (ints != null && ints.length == 2) {
            int start = ints[0];
            int end = ints[1];
            ((VideoEditImageEntity) this.mImageEntities.get(start)).setWhite_area_type(0);
            ((VideoEditImageEntity) this.mImageEntities.get(end)).setWhite_area_type(0);
            for (int i = start + 1; i < end; i++) {
                ((VideoEditImageEntity) this.mImageEntities.get(i)).setWhite_area_type(0);
                ((VideoEditImageEntity) this.mImageEntities.get(i)).setInnerCircleCoverType(0);
            }
        }
    }

    private void setAreaSelected(int[] ints) {
        if (ints != null && ints.length == 2) {
            int size = this.mImageEntities.size();
            int start = ints[0];
            int end = ints[1];
            if (start >= 0 && start < size && end >= 0 && end < size) {
                ((VideoEditImageEntity) this.mImageEntities.get(start)).setWhite_area_type(3);
                ((VideoEditImageEntity) this.mImageEntities.get(end)).setWhite_area_type(2);
                for (int i = start + 1; i < end; i++) {
                    ((VideoEditImageEntity) this.mImageEntities.get(i)).setWhite_area_type(1);
                    if (i == start + 1) {
                        ((VideoEditImageEntity) this.mImageEntities.get(i)).setInnerCircleCoverType(1);
                    }
                    if (i == end - 1) {
                        int innerCircleCoverType = ((VideoEditImageEntity) this.mImageEntities.get(i)).getInnerCircleCoverType();
                        if (innerCircleCoverType == 1) {
                            ((VideoEditImageEntity) this.mImageEntities.get(i)).setInnerCircleCoverType(3);
                        } else if (innerCircleCoverType == 0) {
                            ((VideoEditImageEntity) this.mImageEntities.get(i)).setInnerCircleCoverType(2);
                        }
                    }
                }
                refreshVisibleVideoItem();
            }
        }
    }

    private void refreshVisibleVideoItem() {
        VideoModelHelper.refreshVisibleRecyclerView(this.mImagesLayoutManager, this.mVideoEditImageAdapter, 4, this.mImageEntities.size());
    }

    public float getSumTime() {
        return this.sumTime;
    }

    private void reduceSumTime(int number) {
        if (number != 0) {
            this.sumTime += (1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) number);
            calculateMaxScrollX();
            LogUtil.d(TAG, String.format("reduceSumTime sumTime : %s , mMaxScrollX : %s ", new Object[]{Float.valueOf(this.sumTime), Integer.valueOf(this.mMaxScrollX)}));
        }
    }

    private void refreshOtherView() {
        refreshTimesView();
        refreshAudioView();
        refreshTextView(getTextVideosModels(), this.curTextVideoModel, false);
        if (this.needCheckMissingFootage) {
            refreshMissingFootage();
        }
    }

    private void addSumTime(int number) {
        if (number != 0) {
            this.sumTime += (1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) number);
            calculateMaxScrollX();
            LogUtil.d(TAG, String.format("reduceSumTime sumTime : %s , mMaxScrollX : %s ", new Object[]{Float.valueOf(this.sumTime), Integer.valueOf(this.mMaxScrollX)}));
        }
    }

    public void recordVideoEditModelData(LongVideosModel originModel) {
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        int indexOf = longVideosModels.indexOf(originModel);
        long sumTime = 0;
        for (int i = 0; i < indexOf; i++) {
            sumTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
        }
        LongVideosModel model = (LongVideosModel) longVideosModels.get(indexOf);
        this.beforeStartTime = sumTime;
        this.beforeEndTime = model.getCurrentDuration() + sumTime;
        LogUtil.d(TAG, String.format("recordVideoEditModelData beforeStartTime : %s , beforeEndTime : %s ", new Object[]{Long.valueOf(this.beforeStartTime), Long.valueOf(this.beforeEndTime)}));
    }

    public void refreshForVideoEditConfirm() {
        if (this.curSelectVideo != null) {
            refreshAudioDurationAfterVideoSpeedSet(this.beforeStartTime, this.beforeEndTime, this.curSelectVideo.getCurrentDuration());
            removeInvalidAudioModel(VideoModelHelper.getVideosSumTime(getVideoLongVideosModels()));
            this.curSelectVideo.setSelectedForSplit(true);
            this.curSelectVideo = null;
        }
        resetTextPosAfterOrder(getVideoLongVideosModels());
        refreshVideosView(getVideoLongVideosModels());
        findVideoDurationTvShowRect(this.curSelectVideo);
        if (this.needCheckMissingFootage) {
            refreshMissingFootage();
        }
        callAudioChange();
//        this.mImagesRecyclerView.post(7.lambdaFactory$(this));
        mImagesRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                lambda$refreshForVideoEditConfirm$8();
            }
        });
    }

    private /* synthetic */ void lambda$refreshForVideoEditConfirm$8() {
        calculateExactlyScrollX();
        relateScroll(this.mImagesScrollX, 0);
    }

    private void refreshAudioDurationAfterVideoSpeedSet(long beforeStartTime, long beforeEndTime, long currentDuration) {
        VideoModelHelper.refreshAudioDurationAfterVideoSpeedSet(beforeStartTime, beforeEndTime, currentDuration, getAudioLongVideoModels());
    }

    private void refreshTextView(List<LongVideosModel> textVideosModels, LongVideosModel curSelectVideo, boolean isRefresh) {
        refreshTextView(textVideosModels, curSelectVideo, isRefresh, true, -1, 0, false, false, false);
    }

    public void refreshTextView(List<LongVideosModel> textVideosModels, LongVideosModel curSelectVideo, boolean isRefresh, boolean needRefreshHeight, int curLongPressingPos, int changeTime, boolean isMinValue, boolean needRefreshTextData, boolean isAutoScrolling) {
        if (textVideosModels != null) {
            VideoModelHelper.removeInvalidTextModelLowLevel(textVideosModels);
            this.curTextVideoModel = curSelectVideo;
            this.mTextEntities.clear();
            int imageHeadWidth;
            VideoEditImageEntity videoEditImageEntity;
            int i;
            VideoEditImageEntity end;
            int imageEndWidth;
            if (TextUtil.isValidate(textVideosModels)) {
                if (needRefreshTextData) {
                    checkAndBindRelateTextModelAndVideoModel();
                    refreshEndRelate();
                    refreshCacheTextModels();
                }
                int size = textVideosModels.size();
                imageHeadWidth = ((VideoEditImageEntity) this.mImageEntities.get(0)).getEmpty_type_width();
                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                videoEditImageEntity.setEmpty_type_width(this.screenWidth / 2);
                if (imageHeadWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    videoEditImageEntity.setEmpty_type_width((videoEditImageEntity.getEmpty_type_width() + imageHeadWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
                this.mTextEntities.add(videoEditImageEntity);
                int sumWidth = 0;
                for (i = 0; i < size; i++) {
                    int empty_type_width;
                    LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                    long curStart = model.getStartTimeMs();
                    if (curStart == 0) {
                        if (model.getCircleCoverType() == 0) {
                            model.setCircleCoverType(1);
                        } else if (model.getCircleCoverType() == 2) {
                            model.setCircleCoverType(3);
                        }
                    }
                    long curEnd = curStart + model.getCurrentDurationValue();
                    if (i == 0) {
                        if (curStart > 0) {
                            videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                            empty_type_width = BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH).multiply(BigDecimal.valueOf((double) div((float) curStart, 1000.0f))).setScale(0, RoundingMode.HALF_UP).intValue();
                            sumWidth += empty_type_width;
                            videoEditImageEntity.setEmpty_type_width(empty_type_width);
                            LogUtil.d(TAG, String.format("refreshTextView  i == 0   empty_type_width : %s , startTime : %s , duration : %s ", new Object[]{Integer.valueOf(empty_type_width), Long.valueOf(curStart), Long.valueOf(model.getCurrentDurationValue())}));
                        } else {
                            videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                            videoEditImageEntity.setEmpty_type_width(0);
                        }
//                        this.mTextEntities.add(textEntity);
                        mTextEntities.add(videoEditImageEntity);
                    }
                    if (i == size - 1 && ((float) curEnd) == this.sumTime) {
                        if (model.getCircleCoverType() == 0) {
                            model.setCircleCoverType(2);
                        } else if (model.getCircleCoverType() == 1) {
                            model.setCircleCoverType(3);
                        }
                    }
                    videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_TEXT_TYPE);
                    if (model.isHideForSpecial()) {
                        videoEditImageEntity.setViewType(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                    }
                    if (model.getCurrentDurationValue() < 1000) {
                        videoEditImageEntity.setViewType(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                    }
                    empty_type_width = BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH).multiply(BigDecimal.valueOf((double) div((float) model.getCurrentDurationValue(), 1000.0f))).setScale(0, RoundingMode.HALF_UP).intValue();
                    if (empty_type_width < 0) {
                        empty_type_width = 0;
                    }
                    sumWidth += empty_type_width;
                    LogUtil.d(TAG, String.format("refreshTextView  i == %s   width : %s , startTime : %s , duration : %s ", new Object[]{Integer.valueOf(i), Integer.valueOf(empty_type_width), Long.valueOf(curStart), Long.valueOf(model.getCurrentDurationValue())}));
                    videoEditImageEntity.setEmpty_type_width(empty_type_width);
                    if (model.isHideForSpecial()) {
                        videoEditImageEntity.setEmpty_type_width(0);
                    }
                    videoEditImageEntity.setLongVideosModel(model);
                    if (model == curSelectVideo) {
                        videoEditImageEntity.setPin(true);
                    }
                    this.mTextEntities.add(videoEditImageEntity);
                    int nextPos = i + 1;
                    if (nextPos < size) {
                        LongVideosModel next = textVideosModels.get(nextPos);
                        while (true) {
                            next = next;
                            if (!next.isHideForSpecial()) {
                                break;
                            }
                            nextPos++;
                            if (nextPos >= size) {
                                break;
                            }
                            next = textVideosModels.get(nextPos);
                        }
                        if (!next.isHideForSpecial()) {
                            if (model.getStartTimeMs() + model.getCurrentDurationValue() == next.getStartTimeMs()) {
                                if (model.getCircleCoverType() == 0) {
                                    model.setCircleCoverType(2);
                                } else if (model.getCircleCoverType() == 1) {
                                    model.setCircleCoverType(3);
                                }
                                next.setCircleCoverType(1);
                            }
                            long emptyDistance = next.getStartTimeMs() - curEnd;
                            if (emptyDistance > 0) {
                                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                                int width = BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH).multiply(BigDecimal.valueOf((double) div((float) emptyDistance, 1000.0f))).setScale(0, RoundingMode.HALF_UP).intValue();
                                sumWidth += width;
                                videoEditImageEntity.setEmpty_type_width(width);
                                LogUtil.d(TAG, String.format("refreshTextView  i == %s  ,  empty_type_width : %s , emptyDistance : %s ", new Object[]{Integer.valueOf(i), Integer.valueOf(width), Long.valueOf(emptyDistance)}));
                            } else {
                                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                                videoEditImageEntity.setEmpty_type_width(0);
                            }
                            this.mTextEntities.add(videoEditImageEntity);
                        }
                    }
                }
                if (sumWidth < this.mMaxScrollX) {
                    videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                    videoEditImageEntity.setEmpty_type_width(this.mMaxScrollX - sumWidth);
                    LogUtil.d(TAG, String.format("refreshTextView  end   width : %s ", new Object[]{Integer.valueOf(this.mMaxScrollX - sumWidth)}));
                    this.mTextEntities.add(videoEditImageEntity);
                }
                end = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                end.setEmpty_type_width(this.screenWidth / 2);
                imageEndWidth = ((VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1)).getEmpty_type_width();
                if (imageEndWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    end.setEmpty_type_width((end.getEmpty_type_width() + imageEndWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
                this.mTextEntities.add(end);
                if (this.isMusicEdit) {
                    this.mTextRecyclerView.setVisibility(View.GONE);
                } else {
                    this.mTextRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                imageHeadWidth = ((VideoEditImageEntity) this.mImageEntities.get(0)).getEmpty_type_width();
                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                videoEditImageEntity.setEmpty_type_width(this.screenWidth / 2);
                if (imageHeadWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    videoEditImageEntity.setEmpty_type_width((videoEditImageEntity.getEmpty_type_width() + imageHeadWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
                this.mTextEntities.add(videoEditImageEntity);
                int sum = (int) (this.sumTime / 1000.0f);
                if (this.sumTime % 1000.0f > StaticLayoutUtil.DefaultSpacingadd) {
                    sum++;
                }
                for (i = 0; i < sum; i++) {
                    VideoEditImageEntity entity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                    entity.setEmpty_type_width(IMAGE_UNIT_WIDTH);
                    this.mTextEntities.add(entity);
                }
                end = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
                end.setEmpty_type_width(this.screenWidth / 2);
                imageEndWidth = ((VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1)).getEmpty_type_width();
                if (imageEndWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    end.setEmpty_type_width((end.getEmpty_type_width() + imageEndWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
                this.mTextEntities.add(end);
                if (this.isMusicEdit) {
                    this.mTextRecyclerView.setVisibility(View.GONE);
                } else {
                    this.mTextRecyclerView.setVisibility(View.VISIBLE);
                }
                curSelectVideo = null;
                needRefreshHeight = true;
                isRefresh = true;
            }
            checkAndBindRelateTextModelAndVideoModel();
            if (needRefreshHeight) {
                refreshTextRecyclerViewHeight(curSelectVideo != null);
            }
            if (isRefresh) {
                this.mVideoEditTextAdapter.notifyDataSetChanged();
            } else {
                int firstVisibleItemPosition = this.mTextLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = this.mTextLayoutManager.findLastVisibleItemPosition();
                int needStart = firstVisibleItemPosition - 2;
                while (needStart < 0) {
                    needStart++;
                }
                int needEnd = lastVisibleItemPosition + 2;
                while (needEnd >= this.mTextEntities.size()) {
                    needEnd--;
                }
                LogUtil.d(TAG, String.format("refreshTextView firstVisibleItemPosition : %s , needStart : %s , lastVisibleItemPosition : %s , needEnd : %s , isAutoScrolling : %s ,curLongPressingPos : %s ", new Object[]{Integer.valueOf(firstVisibleItemPosition), Integer.valueOf(needStart), Integer.valueOf(lastVisibleItemPosition), Integer.valueOf(needEnd), Boolean.valueOf(isAutoScrolling), Integer.valueOf(curLongPressingPos)}));
                if (!this.mTextRecyclerView.isComputingLayout()) {
                    this.mVideoEditTextAdapter.notifyItemRangeChanged(needStart, (needEnd - needStart) + 1);
                } else {
                    return;
                }
            }
            if (!this.isSlideVideoAutoScrolling) {
//                this.mTextRecyclerView.post(8.lambdaFactory$(this));
                mTextRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        lambda$refreshTextView$9();
                    }
                });
            }
        }
    }

    private /* synthetic */ void lambda$refreshTextView$9() {
        calculateExactlyTextScrollX();
        relateScroll(this.mImagesScrollX, 0);
    }

    public void clearCurSelectVideoModel() {
        this.curSelectVideo = null;
        hideDurationTvView();
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.onVideoSelectAreaShownOrHide(false, false, getCurSelectVideo());
        }
    }

    public void clearCurSelectAudioModel() {
        if (getCurSelectAudioModel() != null) {
            clearAudioModelVolumesSelect(getCurSelectAudioModel());
            this.mCurSelectAudioModel = null;
            if (this.mChangeOtherCallback != null) {
                this.mChangeOtherCallback.refreshAudioEditButton();
            }
            hideAudioSlideView();
            hideAudioVolumeTouchView();
        }
    }

    public void hideVolumeSlideView() {
        hideVideoVolumeTouchView();
        hideAudioVolumeTouchView();
    }

    public void refreshVideosView(List<LongVideosModel> longVideosModels) {
        refreshVideosView(longVideosModels, false);
    }

    public void refreshVideosView(List<LongVideosModel> longVideosModels, boolean isUndo) {
        if (longVideosModels != null) {
            VideoEditImageEntity videoEditImageEntity;
            int[] ints = null;
            if (this.curSelectVideo != null) {
                ints = (int[]) this.mModelListHashMap.get(this.curSelectVideo);
            }
            this.mImageEntities.clear();
            this.mModelListHashMap.clear();
            this.needHideLeftArrowPos.clear();
            this.sumTime = StaticLayoutUtil.DefaultSpacingadd;
            VideoEditImageEntity emptyHead = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            emptyHead.setWhite_area_type(0);
            if (this.isMusicEdit) {
                emptyHead.setEmpty_type_width(this.VIDEO_MUTE_LEFT_EMPTY_WIDTH);
            } else {
                emptyHead.setEmpty_type_width(this.VIDEO_START_AND_END_EMPTY_WIDTH);
            }
            this.mImageEntities.add(emptyHead);
            if (this.isMusicEdit) {
                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_IMAGE_MUTE_TYPE);
                videoEditImageEntity.setWhite_area_type(0);
                videoEditImageEntity.setAdd_type_width(this.VIDEO_MUTE_BUTTON_WIDTH);
                videoEditImageEntity.setVideoMuteOn(this.manager.isAllVideoMute());
                this.mImageEntities.add(videoEditImageEntity);
            } else {
                VideoEditImageEntity addHead = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE);
                addHead.setWhite_area_type(0);
                addHead.setAdd_type_width(this.VIDEO_ADD_WIDTH);
                this.mImageEntities.add(addHead);
            }
            int size = longVideosModels.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel longVideosModel = (LongVideosModel) longVideosModels.get(i);
                int[] area = new int[2];
                this.sumTime += (float) longVideosModel.getCurrentDuration();
                long videoTotalDuration = longVideosModel.getTotalDuration();
                long startTime = longVideosModel.getStartTimeMs();
                long showStartTime = longVideosModel.getShowStartTime();
                long videoCurrentDuration = longVideosModel.getCurrentDuration();
                float imageSize = getVideoValue(videoTotalDuration);
                float start = getVideoStartValue(showStartTime);
                float end = getVideoEndValue(showStartTime + videoCurrentDuration);
                area[0] = this.mImageEntities.size() - 1;
                int start_int = (int) start;
                int end_int = (int) ((1.0f - IMAGE_SLIDE_MIN_EXPANSION.floatValue()) + end);
                for (int j = start_int; j < end_int; j++) {
                    VideoEditImageEntity imageEntity;
                    videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_IMAGE_TYPE);
                    videoEditImageEntity.setTimeIndex(j);
                    videoEditImageEntity.setCircleCoverType(0);
                    int imageTime = (((start_int * 1000) + ((j - start_int) * 1000)) / 1000) * 1000;
                    videoEditImageEntity.setImageTime((long) imageTime);
                    if (j == start_int) {
                        videoEditImageEntity.setCircleCoverType(1);
                        if (!(longVideosModel.getVideoSpeedUp() == FilterView.VideoSpeedup.NORMAL && longVideosModel.getZoomStart() == 1.0f && longVideosModel.getZoomEnd() == 1.0f)) {
                            videoEditImageEntity.setShowEffectImage(true);
                        }
                        float showStart = sub(start, (float) start_int);
                        if (showStart < StaticLayoutUtil.DefaultSpacingadd) {
                            showStart = StaticLayoutUtil.DefaultSpacingadd;
                        }
                        videoEditImageEntity.setShowStart(showStart);
                        if (start == StaticLayoutUtil.DefaultSpacingadd) {
                            ((VideoEditImageEntity) this.mImageEntities.get(area[0])).setHideRightArrowImage(true);
                        } else {
                            ((VideoEditImageEntity) this.mImageEntities.get(area[0])).setHideRightArrowImage(false);
                        }
                    }
                    if (j == start_int + 1) {
                        imageEntity = (VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1);
                        float oldStart = imageEntity.getShowStart();
                        if (oldStart > 0.5f) {
                            imageEntity.setShowStart(sub(oldStart, 1.0f));
                            imageEntity.setTimeIndex(j);
                            imageEntity.setImageTime((long) imageTime);
                            if (j == end_int - 1) {
                                if (imageEntity.getCircleCoverType() == 1) {
                                    imageEntity.setCircleCoverType(3);
                                } else {
                                    imageEntity.setCircleCoverType(2);
                                }
                                imageEntity.setShowEnd(sub(end, (float) (end_int - 1)));
                            }
                            LogUtil.d(TAG, String.format(" j : %s , showStart : %s , showEnd : %s , imageTime: %s ", new Object[]{Integer.valueOf(j), Float.valueOf(imageEntity.getShowStart()), Float.valueOf(imageEntity.getShowEnd()), Long.valueOf(imageEntity.getImageTime())}));
                        }
                    }
                    if (j == end_int - 1) {
                        if (videoEditImageEntity.getCircleCoverType() == 1) {
                            videoEditImageEntity.setCircleCoverType(3);
                        } else {
                            videoEditImageEntity.setCircleCoverType(2);
                        }
                        float showEnd = sub(end, (float) (end_int - 1));
                        if (((double) showEnd) < 0.5d) {
                            imageEntity = (VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1);
                            imageEntity.setShowEnd(1.0f + showEnd);
                            if (imageEntity.getCircleCoverType() == 1) {
                                imageEntity.setCircleCoverType(3);
                            } else {
                                imageEntity.setCircleCoverType(2);
                            }
                            LogUtil.d(TAG, String.format(" j : %s , showStart : %s , showEnd : %s , imageTime: %s ", new Object[]{Integer.valueOf(j), Float.valueOf(videoEditImageEntity.getShowStart()), Float.valueOf(videoEditImageEntity.getShowEnd()), Long.valueOf(imageEntity.getImageTime())}));
                        } else {
                            videoEditImageEntity.setShowEnd(showEnd);
                        }
                    }
                    videoEditImageEntity.setLongVideosModel(longVideosModel);
                    if (longVideosModel.mediaType == 2) {
                        videoEditImageEntity.setImageTime(0);
                    }
                    this.mImageEntities.add(videoEditImageEntity);
                }
                area[1] = this.mImageEntities.size();
                if (end >= imageSize) {
                    this.needHideLeftArrowPos.add(Integer.valueOf(area[1]));
                }
                this.mModelListHashMap.put(longVideosModel, area);
                if (longVideosModel.isSelectedForSplit()) {
                    longVideosModel.setSelectedForSplit(false);
                    this.curSelectVideo = longVideosModel;
                    measureSelectedStartAndEndX();
                    ints = area;
                    if (this.mChangeOtherCallback != null) {
                        this.mChangeOtherCallback.onVideoSelectAreaShownOrHide(true, longVideosModels.size() == 1, getCurSelectVideo());
                    }
                }
                if (longVideosModel.getVideoVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                    refreshVideoHolderMute(longVideosModel);
                }
            }
            if (!this.isMusicEdit) {
                VideoEditImageEntity addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE);
                addEnd.setWhite_area_type(0);
                addEnd.setAdd_type_width(this.VIDEO_ADD_WIDTH);
                this.mImageEntities.add(addEnd);
            }
            VideoEditImageEntity emptyEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            emptyEnd.setWhite_area_type(0);
            if (this.isMusicEdit) {
                emptyEnd.setEmpty_type_width(this.VIDEO_HALF_SCREEN);
            } else {
                emptyEnd.setEmpty_type_width(this.VIDEO_START_AND_END_EMPTY_WIDTH);
            }
            emptyEnd.setBgColor(ContextCompat.getColor(App.getContext(), R.color.colorGray));
            this.mImageEntities.add(emptyEnd);
            Iterator it = this.needHideLeftArrowPos.iterator();
            while (it.hasNext()) {
                ((VideoEditImageEntity) this.mImageEntities.get(((Integer) it.next()).intValue())).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyDataSetChanged();
            if (ints != null) {
                setAreaSelected(ints);
            }
            calculateMaxScrollX();
            prepareNearbyJumpData();
            refreshTimesView();
            refreshAudioView(!isUndo);
            refreshTextView(getTextVideosModels(), this.curTextVideoModel, true);
            if (this.needCheckMissingFootage) {
                refreshMissingFootage();
            }
        }
    }

    private void refreshMissingFootage() {
        int firstPos = this.mImagesLayoutManager.findFirstVisibleItemPosition();
        int lastPos = this.mImagesLayoutManager.findLastVisibleItemPosition();
        Set<LongVideosModel> longVideosModels = this.mModelListHashMap.keySet();
        if (TextUtil.isValidate(this.missingFootageIvs) && TextUtil.isValidate(longVideosModels)) {
            int index = 0;
            for (LongVideosModel model : longVideosModels) {
                if (!model.isVideoFileExist()) {
                    int[] ints = (int[]) this.mModelListHashMap.get(model);
                    int start = ints[0] + 1;
                    int end = ints[1] - 1;
                    if (start <= lastPos && end >= firstPos) {
                        if (index < this.missingFootageIvs.size() || (this.mChangeOtherCallback != null && this.mChangeOtherCallback.onNeedAddOneMissingFootageIv())) {
                            ImageView iv = (ImageView) this.missingFootageIvs.get(index);
                            LayoutParams params = (LayoutParams) iv.getLayoutParams();
                            int[] rect = new int[2];
                            int[] rect2 = new int[2];
                            View viewByPosition;
                            int leftBorder;
                            View lastView;
                            int rightBorder;
                            if (start >= firstPos && end <= lastPos) {
                                viewByPosition = this.mImagesLayoutManager.findViewByPosition(start);
                                if (viewByPosition != null) {
                                    viewByPosition.getLocationOnScreen(rect);
                                    leftBorder = rect[0] + DensityUtil.dip2px(5.0f);
                                    lastView = this.mImagesLayoutManager.findViewByPosition(end);
                                    if (lastView != null) {
                                        lastView.getLocationOnScreen(rect2);
                                        rightBorder = (rect2[0] - DensityUtil.dip2px(5.0f)) + lastView.getMeasuredWidth();
                                        if (leftBorder < 0) {
                                            if (rightBorder < this.screenWidth) {
                                                params.leftMargin = (rightBorder / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                            } else {
                                                params.leftMargin = (this.screenWidth / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                            }
                                        } else {
                                            if (rightBorder <= this.screenWidth) {
                                                params.leftMargin = (((rightBorder - leftBorder) / 2) + leftBorder) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                            } else {
                                                params.leftMargin = (((this.screenWidth - leftBorder) / 2) + leftBorder) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                            }
                                        }
                                    }
                                }
                            } else if (start <= firstPos && end <= lastPos) {
                                lastView = this.mImagesLayoutManager.findViewByPosition(end);
                                if (lastView != null) {
                                    lastView.getLocationOnScreen(rect2);
                                    rightBorder = (rect2[0] - DensityUtil.dip2px(5.0f)) + lastView.getMeasuredWidth();
                                    if (rightBorder <= this.screenWidth) {
                                        params.leftMargin = (rightBorder / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                    } else {
                                        params.leftMargin = (this.screenWidth / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                    }
                                    if (params.leftMargin + (MISSING_FOOTAGE_IV_HALF_WIDTH * 2) > rightBorder) {
                                        params.leftMargin = rightBorder - (MISSING_FOOTAGE_IV_HALF_WIDTH * 2);
                                    }
                                }
                            } else if (start >= firstPos && end > lastPos) {
                                viewByPosition = this.mImagesLayoutManager.findViewByPosition(start);
                                if (viewByPosition != null) {
                                    int restWidth;
                                    viewByPosition.getLocationOnScreen(rect);
                                    leftBorder = rect[0] + DensityUtil.dip2px(5.0f);
                                    if (leftBorder >= 0) {
                                        restWidth = this.screenWidth - leftBorder;
                                    } else {
                                        restWidth = this.screenWidth;
                                    }
                                    int addLeft = (restWidth / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                                    if (addLeft < DensityUtil.dip2px(5.0f)) {
                                        addLeft = DensityUtil.dip2px(5.0f);
                                    }
                                    if (leftBorder >= 0) {
                                        addLeft += leftBorder;
                                    }
                                    params.leftMargin = addLeft;
                                }
                            } else if (start >= firstPos || end <= lastPos) {
                                LogUtil.d(TAG, "CHECK THE CASE");
                            } else {
                                params.leftMargin = (this.screenWidth / 2) - MISSING_FOOTAGE_IV_HALF_WIDTH;
                            }
                            iv.setLayoutParams(params);
                            iv.setVisibility(View.VISIBLE);
                            index++;
                        }
                    }
                }
            }
            for (int i = index; i < this.missingFootageIvs.size(); i++) {
                ((ImageView) this.missingFootageIvs.get(i)).setVisibility(View.GONE);
            }
        }
    }

    private void refreshAudioModelData(LongVideosModel model, VideoEditImageEntity content, List<LongVideosModel> audioModels, int audioModelPos) {
        if (TextUtil.isValidate(audioModels)) {
            int size = audioModels.size();
            long audioStartTime = model.getAudioStartTime();
            long audioDuration = model.getAudioDuration();
            long audioEndTime = audioStartTime + audioDuration;
            content.setMusicWaveContentWidth((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) audioDuration, 1000.0f)));
            if (audioModelPos == 0) {
                if (audioStartTime != 0) {
                    content.setLeftWidth((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) audioStartTime, 1000.0f)));
                } else {
                    content.setLeftWidth(0);
                }
            } else if (!model.isJustSeeForAudio()) {
                int prevIndex = audioModelPos - 1;
                LongVideosModel prev = audioModels.get(prevIndex);
//                while (true) {
//                    prev = prev;
//                    if (!prev.isAudioDataValid() && prevIndex > 0) {
//                        prevIndex--;
//                        prev = audioModels.get(prevIndex);
//                    }
//                }
                if (prev.isAudioDataValid()) {
                    long endTime = prev.getAudioStartTime() + prev.getAudioDuration();
                    if (model.getAudioStartTime() > endTime) {
                        content.setLeftWidth((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) (model.getAudioStartTime() - endTime), 1000.0f)));
                    } else {
                        model.setAudioStartTime(endTime);
                        content.setLeftWidth(0);
                    }
                } else if (audioStartTime != 0) {
                    content.setLeftWidth((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) audioStartTime, 1000.0f)));
                } else {
                    content.setLeftWidth(0);
                }
            }
            content.setMusicBgCoverWidth(content.getLeftWidth() + content.getMusicWaveContentWidth());
            if (!model.isJustSeeForAudio() && audioModelPos == size - 1) {
                content.setRightWidth(IMAGE_UNIT_WIDTH + VideoEditImageHolder.LEFT_OR_RIGHT_SPACE);
                if (((float) audioEndTime) != this.sumTime) {
                    content.setRightWidth(content.getRightWidth() + ((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div(this.sumTime - ((float) audioEndTime), 1000.0f))));
                }
                content.setMusicBgCoverWidth(((content.getMusicBgCoverWidth() + content.getRightWidth()) - IMAGE_UNIT_WIDTH) - VideoEditImageHolder.LEFT_OR_RIGHT_SPACE);
            } else if (audioModelPos == size - 2 && ((LongVideosModel) audioModels.get(size - 1)).isJustSeeForAudio()) {
                content.setRightWidth(0);
                if (((float) audioEndTime) != this.sumTime) {
                    content.setRightWidth(content.getRightWidth() + ((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div(this.sumTime - ((float) audioEndTime), 1000.0f))));
                }
                content.setMusicBgCoverWidth(content.getMusicBgCoverWidth() + content.getRightWidth());
            }
            content.setEmpty_type_width((content.getMusicWaveContentWidth() + content.getLeftWidth()) + content.getRightWidth());
        }
    }

    private void refreshAudioView(List<LongVideosModel> audioModels) {
        refreshAudioEntities(audioModels);
        this.mVideoEditAudioAdapter.notifyDataSetChanged();
        if (!this.isSlideVideoAutoScrolling) {
//            this.mAudioRecyclerView.post(9.lambdaFactory$(this));
            mAudioRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    lambda$refreshAudioView$10();
                }
            });
        }
    }

    private /* synthetic */ void lambda$refreshAudioView$10() {
        calculateExactlyAudioScrollX();
        relateScroll(this.mImagesScrollX, 0);
    }

    private void refreshAudioEntities(List<LongVideosModel> audioModels) {
        this.mAudioEntities.clear();
        VideoEditImageEntity videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
        if (this.isMusicEdit) {
            videoEditImageEntity.setEmpty_type_width(this.VIDEO_MUTE_LEFT_EMPTY_WIDTH);
        } else {
            videoEditImageEntity.setEmpty_type_width(this.VIDEO_START_AND_END_EMPTY_WIDTH);
        }
        if (!this.isMusicEdit) {
            VideoEditImageEntity imageHead = (VideoEditImageEntity) this.mImageEntities.get(0);
            if (imageHead.getEmpty_type_width() > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                videoEditImageEntity.setEmpty_type_width((videoEditImageEntity.getEmpty_type_width() + imageHead.getEmpty_type_width()) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
            }
        }
        this.mAudioEntities.add(videoEditImageEntity);
        videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
        if (this.isMusicEdit) {
            videoEditImageEntity.setViewType(BaseAdapter.Item.VIDEO_EDIT_AUDIO_MUTE_TYPE);
            videoEditImageEntity.setAudioMuteOn(this.manager.isAllAudioMute());
            videoEditImageEntity.setEmpty_type_width(this.VIDEO_MUTE_BUTTON_WIDTH);
        } else {
            videoEditImageEntity.setEmpty_type_width(this.VIDEO_ADD_WIDTH);
        }
        this.mAudioEntities.add(videoEditImageEntity);
        boolean hasJustSeeItem = false;
        int endWidth = 0;
        int i;
        int sum;
        float timeS;
        if (TextUtil.isValidate(audioModels)) {
            int size = audioModels.size();
            hasJustSeeItem = ((LongVideosModel) audioModels.get(size - 1)).isJustSeeForAudio();
            i = 0;
            while (i < size) {
                LongVideosModel model = (LongVideosModel) audioModels.get(i);
                if (model.isAudioDataValid()) {
                    long emptySumTime;
                    VideoEditImageEntity addEnd;
                    int tempWidth;
                    VideoEditImageEntity end;
                    int color;
                    VideoEditImageEntity entity;
                    long audioStartTime = model.getAudioStartTime();
                    long audioDuration = model.getAudioDuration();
                    long audioEndTime = audioStartTime + audioDuration;
                    if (i == size - 1 && model.isJustSeeForAudio()) {
                        if (((float) audioStartTime) > this.sumTime) {
                            model.setAudioStartTime(Float.valueOf(this.sumTime).longValue());
                        }
                        hasJustSeeItem = true;
                    }
                    if (i == 0) {
                        emptySumTime = audioStartTime;
                    } else {
                        int prevPos = i - 1;
                        LongVideosModel prev = audioModels.get(prevPos);
                        while (true) {
                            prev = prev;
                            if (!prev.isAudioDataValid()) {
                                prevPos--;
                                if (prevPos < 0) {
                                    break;
                                }
                                prev = audioModels.get(prevPos);
                            } else {
                                break;
                            }
                        }
                        long prevEndTime = 0;
                        if (prev.isAudioDataValid()) {
                            prevEndTime = prev.getAudioStartTime() + prev.getAudioDuration();
                        }
                        emptySumTime = audioStartTime - prevEndTime;
                    }
                    if (model.isJustSeeForAudio()) {
                        emptySumTime = 0;
                    }
                    if (emptySumTime != 0) {
                        sum = (int) (emptySumTime / 1000);
                        timeS = div((float) emptySumTime, 1000.0f);
                        float emptySumWidth = timeS * ((float) IMAGE_UNIT_WIDTH);
                        if (i == 0) {
                            checkClickAddAudioVisible(emptySumWidth);
                        }
                        if (emptySumTime < 1000) {
                            sum = 1;
                        }
                        if (i == 0) {
                            for (int j = 0; j < sum; j++) {
                                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                videoEditImageEntity.setCircleCoverType(0);
                                if (j == 0) {
                                    videoEditImageEntity.setCircleCoverType(1);
                                }
                                if (j == sum - 1) {
                                    videoEditImageEntity.setEmpty_type_width((int) ((timeS - ((float) j)) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH)));
                                } else {
                                    videoEditImageEntity.setEmpty_type_width(IMAGE_UNIT_WIDTH);
                                }
                                this.mAudioEntities.add(videoEditImageEntity);
                            }
                        } else if (emptySumWidth < ((float) (this.AUDIO_ADD_WIDTH + IMAGE_UNIT_WIDTH))) {
                            addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                            addEnd.setEmpty_type_width((int) emptySumWidth);
                            addEnd.setCircleCoverType(0);
                            this.mAudioEntities.add(addEnd);
                        } else {
                            addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_ADD_TYPE);
                            if (!this.isMusicEdit) {
                                addEnd.setViewType(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                            }
                            int audioAddTypeWidth = this.AUDIO_ADD_WIDTH;
                            addEnd.setEmpty_type_width(audioAddTypeWidth);
                            this.mAudioEntities.add(addEnd);
                            tempWidth = (int) (emptySumWidth - ((float) audioAddTypeWidth));
                            while (tempWidth > VideoEditModuleView.IMAGE_UNIT_WIDTH) {
                                addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                addEnd.setCircleCoverType(0);
                                addEnd.setEmpty_type_width(VideoEditModuleView.IMAGE_UNIT_WIDTH);
                                this.mAudioEntities.add(addEnd);
                                tempWidth -= VideoEditModuleView.IMAGE_UNIT_WIDTH;
                            }
                            end = (VideoEditImageEntity) this.mAudioEntities.get(this.mAudioEntities.size() - 1);
                            if (tempWidth > 0) {
                                end.setEmpty_type_width(end.getEmpty_type_width() + tempWidth);
                            }
                        }
                    } else if (i == 0) {
                        checkClickAddAudioVisible(StaticLayoutUtil.DefaultSpacingadd);
                    }
                    if (i % 2 == 0) {
                        color = ContextCompat.getColor(App.getContext(), R.color.colorAudioBlue);
                    } else {
                        color = ContextCompat.getColor(App.getContext(), R.color.colorAudioGreen);
                    }
                    long tempStart = audioStartTime;
                    long tempDuration = audioDuration;
                    int[] position = new int[2];
                    position[0] = this.mAudioEntities.size();
                    if (model.isJustSeeForAudio()) {
                        videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_MUSIC_EDIT_TYPE);
                        videoEditImageEntity.setLongVideosModel(model);
                        model.setVideoEditImageEntity(videoEditImageEntity);
                        videoEditImageEntity.setCircleCoverType(0);
                        videoEditImageEntity.setBgColor(color);
                        refreshAudioModelData(model, videoEditImageEntity, audioModels, i);
                        this.mAudioEntities.add(videoEditImageEntity);
                    } else {
                        while (tempDuration > 0) {
                            videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EDIT_TYPE);
                            videoEditImageEntity.setLongVideosModel(model);
                            videoEditImageEntity.setCircleCoverType(0);
                            videoEditImageEntity.setBgColor(color);
                            if (tempDuration == audioDuration) {
                                videoEditImageEntity.setCircleCoverType(1);
                            }
                            long audioWaveViewDuration = getAudioWaveViewDuration(tempStart);
                            videoEditImageEntity.setAudioVolume(getRelateAudioVolume(tempStart, model.getAudioVolumes()));
                            if (audioWaveViewDuration > tempDuration) {
                                audioWaveViewDuration = tempDuration;
                                if (videoEditImageEntity.getCircleCoverType() == 1) {
                                    videoEditImageEntity.setCircleCoverType(3);
                                } else {
                                    videoEditImageEntity.setCircleCoverType(2);
                                }
                            }
                            videoEditImageEntity.setEmpty_type_width((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) audioWaveViewDuration, 1000.0f)));
                            videoEditImageEntity.setAudioStartTime(tempStart);
                            videoEditImageEntity.setAudioDuration(audioWaveViewDuration);
                            this.mAudioEntities.add(videoEditImageEntity);
                            tempStart += audioWaveViewDuration;
                            tempDuration -= audioWaveViewDuration;
                        }
                        entity = (VideoEditImageEntity) this.mAudioEntities.get(this.mAudioEntities.size() - 1);
                        if (entity.getCircleCoverType() == 1) {
                            entity.setCircleCoverType(3);
                        } else if (entity.getCircleCoverType() == 0) {
                            entity.setCircleCoverType(2);
                        }
                        if (entity.getAudioDuration() < 500) {
                            int curSize = this.mAudioEntities.size();
                            if (curSize - 2 >= 0) {
                                VideoEditImageEntity before = (VideoEditImageEntity) this.mAudioEntities.get(curSize - 2);
                                if (before.getAudioVolume() == entity.getAudioVolume()) {
                                    before.setAudioDuration(before.getAudioDuration() + entity.getAudioDuration());
                                    before.setEmpty_type_width((int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div((float) before.getAudioDuration(), 1000.0f)));
                                    if (before.getCircleCoverType() == 0) {
                                        before.setCircleCoverType(entity.getCircleCoverType());
                                    } else if (before.getCircleCoverType() == 1) {
                                        before.setCircleCoverType(3);
                                    }
                                    this.mAudioEntities.remove(curSize - 1);
                                }
                            }
                        }
                    }
                    position[1] = this.mAudioEntities.size() - 1;
                    LogUtil.d(TAG, String.format("start : %s , end : %s ", new Object[]{Integer.valueOf(position[0]), Integer.valueOf(position[1])}));
                    this.mAudioModelsListHashMap.put(model, position);
                    if (this.mCurSelectAudioModel == model && this.curSelectAudioEndPosition != position[1]) {
                        this.curSelectAudioEndPosition = position[1];
                        this.mAudioRecyclerView.postInvalidate();
                    }
                    if ((!hasJustSeeItem && i == size - 1) || (hasJustSeeItem && i == size - 2)) {
                        if (((float) audioEndTime) != this.sumTime) {
                            int empty_type_width = (int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * div(this.sumTime - ((float) audioEndTime), 1000.0f));
                            if (empty_type_width >= this.AUDIO_ADD_WIDTH + VideoEditModuleView.IMAGE_UNIT_WIDTH) {
                                addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_ADD_TYPE);
                                if (!this.isMusicEdit) {
                                    addEnd.setViewType(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                }
                                addEnd.setEmpty_type_width(this.AUDIO_ADD_WIDTH);
                                this.mAudioEntities.add(addEnd);
                                tempWidth = empty_type_width - this.AUDIO_ADD_WIDTH;
                                while (tempWidth > VideoEditModuleView.IMAGE_UNIT_WIDTH) {
                                    addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                    addEnd.setCircleCoverType(0);
                                    addEnd.setEmpty_type_width(VideoEditModuleView.IMAGE_UNIT_WIDTH);
                                    this.mAudioEntities.add(addEnd);
                                    tempWidth -= VideoEditModuleView.IMAGE_UNIT_WIDTH;
                                }
                                end = (VideoEditImageEntity) this.mAudioEntities.get(this.mAudioEntities.size() - 1);
                                if (tempWidth > 0) {
                                    end.setEmpty_type_width(end.getEmpty_type_width() + tempWidth);
                                }
                                end.setCircleCoverType(2);
                            } else if (hasJustSeeItem) {
                                addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                addEnd.setEmpty_type_width(empty_type_width);
                                addEnd.setCircleCoverType(2);
                                this.mAudioEntities.add(addEnd);
                            } else {
                                addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_ADD_TYPE);
                                if (!this.isMusicEdit) {
                                    addEnd.setViewType(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                                }
                                if (empty_type_width < this.AUDIO_ADD_WIDTH) {
                                    addEnd.setEmpty_type_width(this.AUDIO_ADD_WIDTH);
                                    endWidth = this.AUDIO_ADD_WIDTH - empty_type_width;
                                } else {
                                    addEnd.setEmpty_type_width(empty_type_width);
                                }
                                addEnd.setCircleCoverType(2);
                                this.mAudioEntities.add(addEnd);
                            }
                        } else if (this.isMusicEdit && !hasJustSeeItem) {
                            entity = (VideoEditImageEntity) this.mAudioEntities.get(this.mAudioEntities.size() - 1);
                            if (entity.getCircleCoverType() == 0) {
                                entity.setCircleCoverType(2);
                            } else if (entity.getCircleCoverType() == 1) {
                                entity.setCircleCoverType(3);
                            }
                            addEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_ADD_TYPE);
                            addEnd.setWhite_area_type(0);
                            addEnd.setAdd_type_width(this.AUDIO_ADD_WIDTH);
                            endWidth = this.AUDIO_ADD_WIDTH;
                            this.mAudioEntities.add(addEnd);
                        }
                    }
                }
                i++;
            }
        } else {
            timeS = div(this.sumTime, 1000.0f);
            int width = (int) (((float) VideoEditModuleView.IMAGE_UNIT_WIDTH) * timeS);
            sum = (int) (this.sumTime / 1000.0f);
            if (this.sumTime < 1000.0f) {
                sum = 1;
            }
            for (i = 0; i < sum; i++) {
                videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_AUDIO_EMPTY_TYPE);
                videoEditImageEntity.setCircleCoverType(0);
                if (i == 0) {
                    videoEditImageEntity.setCircleCoverType(1);
                }
                if (i == sum - 1) {
                    videoEditImageEntity.setCircleCoverType(2);
                    if (i == 0) {
                        videoEditImageEntity.setCircleCoverType(3);
                    }
                }
                if (i == sum - 1) {
                    videoEditImageEntity.setEmpty_type_width((int) ((timeS - ((float) i)) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH)));
                } else {
                    videoEditImageEntity.setEmpty_type_width(IMAGE_UNIT_WIDTH);
                }
                this.mAudioEntities.add(videoEditImageEntity);
            }
            checkClickAddAudioVisible((float) width);
        }
        VideoEditImageEntity imageEnd;
        if (!hasJustSeeItem) {
            videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            int end_empty_type_width = this.screenWidth / 2;
            if (getAudioLongVideoModels().size() > 0) {
                end_empty_type_width -= endWidth;
            }
            videoEditImageEntity.setEmpty_type_width(end_empty_type_width);
            if (!this.isMusicEdit) {
                imageEnd = (VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1);
                if (imageEnd.getEmpty_type_width() > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    videoEditImageEntity.setEmpty_type_width((videoEditImageEntity.getEmpty_type_width() + imageEnd.getEmpty_type_width()) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
            }
            this.mAudioEntities.add(videoEditImageEntity);
        } else if (!this.isMusicEdit) {
            this.mAudioEntities.remove(this.mAudioEntities.size() - 1);
            videoEditImageEntity = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            videoEditImageEntity.setEmpty_type_width(this.screenWidth / 2);
            if (!this.isMusicEdit) {
                imageEnd = (VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1);
                if (imageEnd.getEmpty_type_width() > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
                    videoEditImageEntity.setEmpty_type_width((videoEditImageEntity.getEmpty_type_width() + imageEnd.getEmpty_type_width()) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
                }
            }
            this.mAudioEntities.add(videoEditImageEntity);
        }
    }

    private LongVideosModel.AudioVolume getRelateAudioVolume(long startTime, List<LongVideosModel.AudioVolume> audioVolumes) {
        if (TextUtil.isValidate(audioVolumes)) {
            for (LongVideosModel.AudioVolume audioVolume : audioVolumes) {
                if (startTime >= audioVolume.getStartTime() && startTime < audioVolume.getEndTime()) {
                    return audioVolume;
                }
            }
        }
        return null;
    }

    private long getAudioWaveViewDuration(long startTime) {
        if (!TextUtil.isValidate(this.timeList)) {
            return 1000;
        }
        Iterator it = this.timeList.iterator();
        while (it.hasNext()) {
            Long time = (Long) it.next();
            if (startTime < time.longValue() && startTime + 1000 > time.longValue()) {
                return time.longValue() - startTime;
            }
        }
        return 1000;
    }

    private void checkClickAddAudioVisible(float width) {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.checkAddTextVisible(width);
        }
    }

    private void showClickAddAudio() {
        refreshAddMusicTvVisible(0);
    }

    public void refreshAudioViewAfterInsertOrReplace(int modelPosition, boolean isReplace) {
        refreshAudioView();
        LongVideosModel model = (LongVideosModel) getAudioLongVideoModels().get(modelPosition);
        if (model != null) {
            if (!isReplace) {
                ArrayList<LongVideosModel.AudioVolume> audioVolumes = model.getAudioVolumes();
                if (TextUtil.isValidate(audioVolumes)) {
                    Iterator it = audioVolumes.iterator();
                    while (it.hasNext()) {
                        ((LongVideosModel.AudioVolume) it.next()).setSelected(true);
                    }
                }
            }
            final int position = ((int[]) this.mAudioModelsListHashMap.get(model))[0];
            this.mAudioRecyclerView.postDelayed(new Runnable() {
                public void run() {
                    VideoEditHelper.this.dealAudioItemClick(null, position);
                }
            }, 50);
        }
    }

    public void refreshAudioView() {
        refreshAudioView(true);
    }

    public void refreshAudioView(boolean needRefreshVolume) {
        if (needRefreshVolume) {
            refreshAudioAndVideoRelation();
        }
        refreshAudioView(getAudioLongVideoModels());
    }

    private void refreshAudioAndVideoRelation() {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (TextUtil.isValidate(audioLongVideoModels)) {
            List<LongVideosModel> videoLongVideosModels = getVideoLongVideosModels();
            int videoSize = videoLongVideosModels.size();
            int audioSize = audioLongVideoModels.size();
            for (int i = 0; i < audioSize; i++) {
                refreshAudioModelVolumes(videoLongVideosModels, videoSize, (LongVideosModel) audioLongVideoModels.get(i));
            }
        }
    }

    private void refreshAudioModelVolumes(List<LongVideosModel> videoLongVideosModels, int videoSize, LongVideosModel audioModel) {
        VideoModelHelper.refreshAudioModelVolumes(videoLongVideosModels, videoSize, audioModel);
    }

    private void refreshTimesView() {
        this.mTimesEntities.clear();
        int imageHeadWidth = ((VideoEditImageEntity) this.mImageEntities.get(0)).getEmpty_type_width();
        VideoEditImageEntity head = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
        head.setEmpty_type_width(this.screenWidth / 2);
        if (imageHeadWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
            head.setEmpty_type_width((head.getEmpty_type_width() + imageHeadWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
        }
        this.mTimesEntities.add(head);
        int sum = (int) (this.sumTime / 1000.0f);
        for (int i = 0; i <= sum; i++) {
            VideoEditImageEntity videoEditImageEntity = new VideoEditImageEntity(280);
            videoEditImageEntity.setShowTimeText(i);
            this.mTimesEntities.add(videoEditImageEntity);
        }
        double fix = ((((double) (this.sumTime % 1000.0f)) * 1.0d) / 1000.0d) - 0.5d;
        if (fix > 0.0d) {
            VideoEditImageEntity secondEnd = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
            secondEnd.setEmpty_type_width((int) (((double) IMAGE_UNIT_WIDTH) * fix));
            this.mTimesEntities.add(secondEnd);
        }
        VideoEditImageEntity end = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_EMPTY_TYPE);
        end.setEmpty_type_width(this.screenWidth / 2);
        int imageEndWidth = ((VideoEditImageEntity) this.mImageEntities.get(this.mImageEntities.size() - 1)).getEmpty_type_width();
        if (imageEndWidth > this.VIDEO_START_AND_END_EMPTY_WIDTH) {
            end.setEmpty_type_width((end.getEmpty_type_width() + imageEndWidth) - this.VIDEO_START_AND_END_EMPTY_WIDTH);
        }
        this.mTimesEntities.add(end);
        this.mVideoEditTimesAdapter.notifyDataSetChanged();
    }

    private void calculateMaxScrollX() {
        this.mMaxScrollX = (int) ((this.sumTime / 1000.0f) * ((float) IMAGE_UNIT_WIDTH));
    }

    private float getVideoStartValue(long startTime) {
        return div(((float) startTime) * 1.0f, 1000.0f);
    }

    private float getVideoValue(long value) {
        return divDOWN(((float) value) * 1.0f, 1000.0f);
    }

    private float getVideoEndValue(long endTime) {
        return div(((float) endTime) * 1.0f, 1000.0f);
    }

    public void changeTextDurationAfterInsert(int insertPosition, long changeTime) {
        changeTextDurationAfterInsert(insertPosition, changeTime, false);
    }

    public void changeAudioDurationAfterInsert(long changeTime) {
        LogUtil.d(TAG, String.format("changeAudioDurationAfterInsert changeTime : %s ", new Object[]{Long.valueOf(changeTime)}));
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (!TextUtil.isNull(audioLongVideoModels)) {
            LongVideosModel end = (LongVideosModel) audioLongVideoModels.get(audioLongVideoModels.size() - 1);
            if (end.isJustSeeForAudio()) {
                end.setJustSeeForAudio(false);
                end.setAudioDuration(changeTime);
            } else if (((float) (end.getAudioStartTime() + end.getAudioDuration())) == this.sumTime) {
                end.setAudioDuration(end.getAudioDuration() + changeTime);
            }
        }
    }

    public void changeTextDurationAfterInsert(int insertPosition, long changeTime, boolean needCutInsertPosText) {
        LogUtil.d(TAG, String.format("changeTextDurationAfterInsert insertPosition : %s , changeTime : %s , needCut : %s ", new Object[]{Integer.valueOf(insertPosition), Long.valueOf(changeTime), Boolean.valueOf(needCutInsertPosText)}));
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        if (longVideosModels != null) {
            List<LongVideosModel> textVideosModels = getTextVideosModels();
            if (!TextUtil.isNull(textVideosModels)) {
                int size = longVideosModels.size();
                if (insertPosition >= 0 && insertPosition < size) {
                    int i;
                    long endTime = 0;
                    for (i = 0; i < insertPosition; i++) {
                        endTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
                    }
                    int startPosition = -1;
                    int textSize = textVideosModels.size();
                    for (i = 0; i < textSize; i++) {
                        LongVideosModel textModel = (LongVideosModel) textVideosModels.get(i);
                        if (textModel.getStartTimeMs() >= endTime || textModel.getStartTimeMs() + textModel.getCurrentDurationValue() <= endTime) {
                            if (textModel.getStartTimeMs() >= endTime) {
                                startPosition = i;
                                break;
                            }
                        } else if (needCutInsertPosText) {
                            long currentDuration = endTime - textModel.getStartTimeMs();
                            if (currentDuration < 1000) {
                                currentDuration = 0;
                            }
                            textModel.setCurrentDuration(currentDuration);
                        }
                    }
                    LogUtil.d(TAG, String.format("changeTextDurationAfterInsert endTime : %s , startPos : %s , textVideosModels : %s ", new Object[]{Long.valueOf(endTime), Integer.valueOf(startPosition), textVideosModels.toString()}));
                    removeInvalidTextModel();
                    if (startPosition != -1) {
                        changeAfterTextDuration(startPosition, changeTime);
                    }
                    refreshCacheTextModels();
                    LogUtil.d(TAG, String.format("changeTextDurationAfterInsert  startPos : %s , textVideosModels : %s ", new Object[]{Integer.valueOf(startPosition), textVideosModels.toString()}));
                }
            }
        }
    }

    private void removeInvalidTextModel() {
        VideoModelHelper.removeInvalidTextModel(getTextVideosModels());
    }

    private void changeAfterTextDuration(int position, long changeTime) {
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (textVideosModels != null) {
            int size = textVideosModels.size();
            if (position >= 0 && position < size) {
                for (int i = position; i < size; i++) {
                    LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                    model.setStartTime(model.getStartTimeMs() + changeTime);
                }
            }
        }
    }

    private int dealRightPosition(int position, int time, boolean isAutoScroll) {
        LogUtil.d(TAG, String.format("dealRightPosition start  position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mImageEntities.get(position);
        LongVideosModel longVideosModel = videoEditImageEntity.getLongVideosModel();
        if (longVideosModel == null) {
            return 0;
        }
        float showStart = videoEditImageEntity.getShowStart();
        float showEnd = videoEditImageEntity.getShowEnd();
        LogUtil.d(TAG, String.format("showStartAndEnd showStart : %s , showEnd : %s ", new Object[]{Float.valueOf(showStart), Float.valueOf(showEnd)}));
        long curSlideSumTime = 0;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        for (int i = 0; i <= longVideosModels.indexOf(longVideosModel); i++) {
            curSlideSumTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
        }
        long textChangeTime;
        if (time > 0) {
            time = longVideosModel.addCurrentDuration(time);
            if (time == 0) {
                return time;
            }
            textChangeTime = (long) ((1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) time));
            changeRelateAudioData(curSlideSumTime, textChangeTime);
            addSumTime(time);
            changeOtherStartTime(true, curSlideSumTime, textChangeTime, longVideosModel);
            refreshOtherView();
            addEndTime(position, time, videoEditImageEntity, longVideosModel, showEnd);
        } else {
            time = longVideosModel.reduceCurrentDuration(time, true);
            if (time == 0) {
                return time;
            }
            textChangeTime = (long) ((1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) time));
            changeRelateAudioData(curSlideSumTime, textChangeTime);
            reduceSumTime(time);
            changeOtherStartTime(true, curSlideSumTime, textChangeTime, longVideosModel);
            refreshOtherView();
            reduceEndTime(position, time, videoEditImageEntity, longVideosModel, showEnd, isAutoScroll);
        }
        changeDurationTvLeftMargin(time, longVideosModel.getCurrentDuration());
        LogUtil.d(TAG, String.format("dealRightPosition end  position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
        this.mChangeOtherCallback.videoTimeChange();
        return time;
    }

    private void changeRelateAudioData(long curSlideSumTime, long textChangeTime) {
        VideoModelHelper.changeRelateAudioData(curSlideSumTime, textChangeTime, getAudioLongVideoModels());
    }

    private void addEndTime(int position, int time, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel, float showEnd) {
        float end = new BigDecimal((double) showEnd).add(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time))).setScale(1, 4).floatValue();
        videoEditImageEntity.setShowEnd(end);
        if (showEnd <= 1.0f && end > 1.0f && ((double) end) < 1.5d) {
            videoEditImageEntity.setRightNeedShowAnim(true);
        }
        if (end >= 1.5f) {
            checkEndIsValid(position, videoEditImageEntity, longVideosModel);
        } else if (end < 0.5f) {
            float showStart = videoEditImageEntity.getShowStart();
            if (showStart < 1.0f) {
                if (longVideosModel.isCanSlideRight()) {
                    ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
                } else {
                    ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
                }
                this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
                this.mVideoEditImageAdapter.notifyItemChanged(((int[]) this.mModelListHashMap.get(longVideosModel))[0]);
                this.mVideoEditImageAdapter.notifyItemChanged(position);
            } else if (showStart == 1.0f) {
                this.mImageEntities.remove(position);
                this.mVideoEditImageAdapter.notifyItemRemoved(position);
                int[] ints = (int[]) this.mModelListHashMap.get(longVideosModel);
                ints[1] = ints[1] - 1;
                this.mModelListHashMap.put(longVideosModel, ints);
                List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
                int size = longVideosModels.size();
                for (int i = longVideosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                    reduceVideoModelArea((LongVideosModel) longVideosModels.get(i));
                }
                VideoEditImageEntity before = (VideoEditImageEntity) this.mImageEntities.get(position - 1);
                before.setShowEnd(add(1.0f, end));
                before.setInnerCircleCoverType(2);
                before.setWhite_area_type(1);
                before.setCircleCoverType(2);
                before.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
                before.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
                this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
            }
        } else {
            if (longVideosModel.isCanSlideRight()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            this.mVideoEditImageAdapter.notifyItemChanged(((int[]) this.mModelListHashMap.get(longVideosModel))[0]);
            this.mVideoEditImageAdapter.notifyItemChanged(position);
        }
    }

    private void checkEndIsValid(int position, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel) {
        float curShowEnd = videoEditImageEntity.getShowEnd();
        if (curShowEnd >= 1.5f) {
            videoEditImageEntity.setShowEnd(1.0f);
            this.mVideoEditImageAdapter.notifyItemChanged(position);
            if (longVideosModel.isCanSlideRight()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            int[] ints = (int[]) this.mModelListHashMap.get(longVideosModel);
            this.mVideoEditImageAdapter.notifyItemChanged(ints[0]);
            int circleCoverType = videoEditImageEntity.getCircleCoverType();
            if (circleCoverType == 3) {
                videoEditImageEntity.setCircleCoverType(1);
            } else if (circleCoverType == 2) {
                videoEditImageEntity.setCircleCoverType(0);
            }
            int innerCircleCoverType = videoEditImageEntity.getInnerCircleCoverType();
            if (innerCircleCoverType == 3) {
                videoEditImageEntity.setInnerCircleCoverType(1);
            } else if (innerCircleCoverType == 2) {
                videoEditImageEntity.setInnerCircleCoverType(0);
            }
            int index = videoEditImageEntity.getTimeIndex();
            long imageTime = videoEditImageEntity.getImageTime();
            VideoEditImageEntity videoEditImageEntity2 = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_IMAGE_TYPE);
            videoEditImageEntity2.setWhite_area_type(1);
            videoEditImageEntity2.setCircleCoverType(2);
            videoEditImageEntity2.setLongVideosModel(longVideosModel);
            videoEditImageEntity2.setTimeIndex(index + 1);
            videoEditImageEntity2.setImageTime(1000 + imageTime);
            videoEditImageEntity2.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
            videoEditImageEntity2.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
            videoEditImageEntity2.setInnerCircleCoverType(2);
            videoEditImageEntity2.setShowEnd(sub(curShowEnd, 1.0f));
            this.mImageEntities.add(position + 1, videoEditImageEntity2);
            this.mVideoEditImageAdapter.notifyItemInserted(position + 1);
            ints[1] = ints[1] + 1;
            this.mModelListHashMap.put(longVideosModel, ints);
            List<LongVideosModel> videosModels = getVideoLongVideosModels();
            int size = videosModels.size();
            for (int i = videosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                addVideoModelArea((LongVideosModel) videosModels.get(i));
            }
            checkEndIsValid(position + 1, videoEditImageEntity2, longVideosModel);
        }
    }

    private void reduceEndTime(int position, int time, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel, float showEnd, boolean isAutoScroll) {
        videoEditImageEntity.setShowEnd(new BigDecimal((double) showEnd).add(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time))).setScale(1, 4).floatValue());
        float curShowEnd = videoEditImageEntity.getShowEnd();
        int needScrollX;
        int finalChangeTime;
        if (curShowEnd >= 0.5f) {
            if (longVideosModel.isCanSlideRight()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            this.mVideoEditImageAdapter.notifyItemChanged(((int[]) this.mModelListHashMap.get(longVideosModel))[0]);
            this.mVideoEditImageAdapter.notifyItemChanged(position);
            if (!isAutoScroll) {
                needScrollX = IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).setScale(1, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH)).intValue();
                if (this.mImagesScrollX - needScrollX >= this.mMaxScrollX) {
                    finalChangeTime = BigDecimal.valueOf((long) (needScrollX - (this.mImagesScrollX - this.mMaxScrollX))).divide(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH)), 0, RoundingMode.HALF_UP).intValue();
                    //LogUtil.d(TAG, String.format("reduceEndTime mImagesScrollX : %s , needScrollX : %s , mMaxScrollX : %s , finalNeedScrollX : %s , finalChangeTime : %s ", new Object[]{Integer.valueOf(this.mImagesScrollX), Integer.valueOf(needScrollX), Integer.valueOf(this.mMaxScrollX), Integer.valueOf(finalNeedScrollX), Integer.valueOf(finalChangeTime)}));
                    changeEndEmptyViewWidth(-finalChangeTime);
                }
            }
        } else if (videoEditImageEntity.getShowStart() == StaticLayoutUtil.DefaultSpacingadd) {
            if (longVideosModel.isCanSlideRight()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            int[] ints = (int[]) this.mModelListHashMap.get(longVideosModel);
            this.mVideoEditImageAdapter.notifyItemChanged(ints[0]);
            VideoEditImageEntity beforeEntity = (VideoEditImageEntity) this.mImageEntities.get(position - 1);
            beforeEntity.setShowEnd(add(1.0f, curShowEnd));
            beforeEntity.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
            beforeEntity.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
            if (beforeEntity.getCircleCoverType() == 0) {
                beforeEntity.setCircleCoverType(2);
            } else if (beforeEntity.getCircleCoverType() == 1) {
                beforeEntity.setCircleCoverType(3);
            }
            int innerCircleCoverType = beforeEntity.getInnerCircleCoverType();
            if (innerCircleCoverType == 0) {
                beforeEntity.setInnerCircleCoverType(2);
            } else if (innerCircleCoverType == 1) {
                beforeEntity.setInnerCircleCoverType(3);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
            this.mImageEntities.remove(position);
            this.mVideoEditImageAdapter.notifyItemRemoved(position);
            ints[1] = ints[1] - 1;
            this.mModelListHashMap.put(longVideosModel, ints);
            List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
            int size = longVideosModels.size();
            for (int i = longVideosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                reduceVideoModelArea((LongVideosModel) longVideosModels.get(i));
            }
        } else {
            if (longVideosModel.isCanSlideRight()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position + 1)).setHideLeftArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            this.mVideoEditImageAdapter.notifyItemChanged(((int[]) this.mModelListHashMap.get(longVideosModel))[0]);
            this.mVideoEditImageAdapter.notifyItemChanged(position);
            if (!isAutoScroll) {
                needScrollX = IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).setScale(1, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH)).intValue();
                if (this.mImagesScrollX - needScrollX >= this.mMaxScrollX) {
                    finalChangeTime = BigDecimal.valueOf((long) (needScrollX - (this.mImagesScrollX - this.mMaxScrollX))).divide(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH)), 0, RoundingMode.HALF_UP).intValue();
                    //LogUtil.d(TAG, String.format("reduceEndTime mImagesScrollX : %s , needScrollX : %s , mMaxScrollX : %s , finalNeedScrollX : %s , finalChangeTime : %s ", new Object[]{Integer.valueOf(this.mImagesScrollX), Integer.valueOf(needScrollX), Integer.valueOf(this.mMaxScrollX), Integer.valueOf(finalNeedScrollX), Integer.valueOf(finalChangeTime)}));
                    changeEndEmptyViewWidth(-finalChangeTime);
                }
            }
        }
    }

    private void changeEndEmptyViewWidth(int changeTime) {
        int endPos = this.mImageEntities.size() - 1;
        VideoEditImageEntity imageEnd = this.mImageEntities.get(endPos);
        int empty_type_width = this.VIDEO_START_AND_END_EMPTY_WIDTH + IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) changeTime)).multiply(BigDecimal.valueOf((long) IMAGE_UNIT_WIDTH)).setScale(0, 4).intValue();
        LogUtil.d(TAG, String.format("changeEndEmptyViewWidth changeTime : %s , empty_type_width : %s ", changeTime, empty_type_width));
        imageEnd.setEmpty_type_width(empty_type_width);
        this.mVideoEditImageAdapter.notifyItemChanged(endPos);
        int timeEndPos = this.mTimesEntities.size() - 1;
        int emptyTypeWidth = (this.screenWidth / 2) + (imageEnd.getEmpty_type_width() - this.VIDEO_START_AND_END_EMPTY_WIDTH);
        this.mTimesEntities.get(timeEndPos).setEmpty_type_width(emptyTypeWidth);
        this.mVideoEditTimesAdapter.notifyItemChanged(timeEndPos);
        if (this.mTextEntities != null && this.mTextEntities.size() > 0) {
            int textEndPos = this.mTextEntities.size() - 1;
            this.mTextEntities.get(textEndPos).setEmpty_type_width(emptyTypeWidth);
            this.mVideoEditTextAdapter.notifyItemChanged(textEndPos);
        }
        int audioEndPos = this.mAudioEntities.size() - 1;
        this.mAudioEntities.get(audioEndPos).setEmpty_type_width(emptyTypeWidth);
        this.mVideoEditAudioAdapter.notifyItemChanged(audioEndPos);
    }

    private void addVideoModelArea(LongVideosModel longVideosModel) {
        int[] ints = this.mModelListHashMap.get(longVideosModel);
        int curStart = ints[0];
        int curEnd = ints[1];
        ints[0] = curStart + 1;
        ints[1] = curEnd + 1;
        this.mModelListHashMap.put(longVideosModel, ints);
    }

    private void reduceVideoModelArea(LongVideosModel longVideosModel) {
        reduceVideoModelArea(longVideosModel, 1);
    }

    private void reduceVideoModelArea(LongVideosModel longVideosModel, int count) {
        int[] ints = this.mModelListHashMap.get(longVideosModel);
        int curStart = ints[0];
        int curEnd = ints[1];
        ints[0] = curStart - count;
        ints[1] = curEnd - count;
        this.mModelListHashMap.put(longVideosModel, ints);
    }

    private void dealScrollXWhenSlideEnd(int position, boolean isRight) {
        calculateExactlyScrollX();
        LogUtil.d(TAG, String.format("dealScrollXWhenSlideEnd position : %s , isRight : %s ", position, isRight));
        LongVideosModel longVideosModel = ((VideoEditImageEntity) this.mImageEntities.get(position)).getLongVideosModel();
        int firstPosition = 0;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        int end = longVideosModels.indexOf(longVideosModel);
        float videoWidth = StaticLayoutUtil.DefaultSpacingadd;
        for (int i = 0; i <= end; i++) {
            videoWidth = ((((float) ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration()) * 1.0f) / 1000.0f) * ((float) VideoEditModuleView.IMAGE_UNIT_WIDTH);
            firstPosition = (int) (((float) firstPosition) + videoWidth);
        }
        int toEnd = firstPosition - this.mImagesScrollX;
        int toStart = (int) (((float) this.mImagesScrollX) - (((float) firstPosition) - videoWidth));
        if (isRight) {
            if (toEnd == 0) {
                this.isJumpToNearby = false;
                return;
            }
            this.mImagesRecyclerView.smoothScrollBy(toEnd, 0);
        } else if (toStart == 0) {
            this.isJumpToNearby = false;
            return;
        } else {
            this.mImagesRecyclerView.smoothScrollBy(-toStart, 0);
        }
        if (position >= 0 && position < this.mImageEntities.size()) {
            refreshVideoMuteTvPosAfterSlide(position);
        }
    }

    private void refreshVideoMuteTvPosAfterSlide(final int position) {
        LongVideosModel longVideosModel = this.mImageEntities.get(position).getLongVideosModel();
        if (longVideosModel != null && longVideosModel.getVideoVolume() == StaticLayoutUtil.DefaultSpacingadd) {
            this.mImagesRecyclerView.post(new Runnable() {
                public void run() {
                    if (position < VideoEditHelper.this.mImageEntities.size()) {
                        LongVideosModel longVideosModel = VideoEditHelper.this.mImageEntities.get(position).getLongVideosModel();
                        if (longVideosModel != null && longVideosModel.getVideoVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                            VideoEditHelper.this.refreshVideoHolderMute(longVideosModel);
                        }
                    }
                }
            });
        }
    }

    public void onAudioVolumeTouchUp(LongVideosModel audioModel, int adapterPosition, long volumeStart, long volumeEnd, float volumeValue, boolean volumeChanged) {
        int size = this.mAudioEntities.size();
        if (adapterPosition >= 0 && adapterPosition < size) {
            Iterator it;
            long endTime;
            if (volumeStart != volumeEnd && volumeValue != StaticLayoutUtil.DefaultSpacingadd) {
                long sumTime = 0;
                for (LongVideosModel model : getVideoLongVideosModels()) {
                    long startTime = sumTime;
                    sumTime += model.getCurrentDuration();
                    endTime = sumTime;
                    if (startTime == volumeStart && endTime == volumeEnd) {
                        model.setAudioVolume(volumeValue);
                    } else if (volumeEnd < startTime) {
                        break;
                    }
                }
            }
            LongVideosModel longVideosModel = this.mAudioEntities.get(adapterPosition).getLongVideosModel();
            if (longVideosModel != null) {
                ArrayList<LongVideosModel.AudioVolume> audioVolumes = longVideosModel.getAudioVolumes();
                if (TextUtil.isValidate(audioVolumes)) {
                    boolean isAllMute = true;
                    it = audioVolumes.iterator();
                    while (it.hasNext()) {
                        if (((LongVideosModel.AudioVolume) it.next()).getVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                            isAllMute = false;
                            break;
                        }
                    }
                    boolean changed = false;
                    if (isAllMute) {
                        int size1 = getAudioLongVideoModels().size();
                        if (size1 > 1) {
                            if (size1 != 2) {
                                getAudioLongVideoModels().remove(longVideosModel);
                                changed = true;
                                clearCurSelectAudioAndRefreshTouchView();
                            } else if (!getAudioLongVideoModels().get(size1 - 1).isJustSeeForAudio()) {
                                getAudioLongVideoModels().remove(longVideosModel);
                                changed = true;
                                clearCurSelectAudioAndRefreshTouchView();
                            }
                        }
                    } else {
                        LongVideosModel.AudioVolume first = audioVolumes.get(0);
                        if (first.getVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                            endTime = first.getEndTime();
                            long duration = first.getDuration();
                            longVideosModel.setAudioStartTime(endTime);
                            longVideosModel.setAudioDuration(longVideosModel.getAudioDuration() - duration);
                            audioVolumes.remove(first);
                            changed = true;
                            LongVideosModel.AudioVolume next = audioVolumes.get(0);
                            next.setSelected(true);
                            refreshAudioSelectPos(this.mAudioEntities, next, next);
                        }
                        LongVideosModel.AudioVolume end = audioVolumes.get(audioVolumes.size() - 1);
                        if (end.getVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                            longVideosModel.setAudioDuration(longVideosModel.getAudioDuration() - end.getDuration());
                            audioVolumes.remove(end);
                            changed = true;
                        }
                    }
                    if (changed) {
                        removeInvalidAudioModel();
                        refreshAudioEntities(getAudioLongVideoModels());
                        refreshAudioMuteTv(getCurSelectAudioModel());
                        this.mVideoEditAudioAdapter.notifyDataSetChanged();
                        if (!this.isSlideVideoAutoScrolling) {
//                            this.mAudioRecyclerView.post(10.lambdaFactory$(this));
                            mAudioRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    lambda$onAudioVolumeTouchUp$11();
                                }
                            });
                        }
                        measureAudioSelectedStartAndEndX();
                        updateAudioSlideView(this.curSelectAudioModelPosition);
                        refreshAudioSelectPos();
                    }
                }
            }
            callAudioChange();
            if (volumeChanged) {
                onSlideTouchUp();
            }
        }
    }

    private /* synthetic */ void lambda$onAudioVolumeTouchUp$11() {
        calculateExactlyAudioScrollX();
        relateScroll(this.mImagesScrollX, 0);
    }

    private void clearCurSelectAudioAndRefreshTouchView() {
        this.mCurSelectAudioModel = null;
        refreshCurSelectAudioPosData();
        refreshAudioVolumeTouchView();
    }

    private void onSwitchMute() {
    }

    private void onSlideTouchUp() {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.onVideoEditSaveToDraft();
        }
    }

    private void onAudioItemClick(int position) {
    }

    private void clearSelectVideoIfIsntInSelectVideoArea() {
        if (this.curSelectVideo != null) {
            LongVideosModel curSelectAudioModel = getCurSelectAudioModel();
            if (curSelectAudioModel != null) {
                ArrayList<LongVideosModel.AudioVolume> audioVolumes = curSelectAudioModel.getAudioVolumes();
                ArrayList<LongVideosModel.AudioVolume> selectVolumes = new ArrayList();
                Iterator it = audioVolumes.iterator();
                while (it.hasNext()) {
                    LongVideosModel.AudioVolume audioVolume = (LongVideosModel.AudioVolume) it.next();
                    if (audioVolume.isSelected()) {
                        selectVolumes.add(audioVolume);
                    }
                }
                if (TextUtil.isValidate(selectVolumes)) {
                    LongVideosModel.AudioVolume end = (LongVideosModel.AudioVolume) selectVolumes.get(selectVolumes.size() - 1);
                    long startTime = ((LongVideosModel.AudioVolume) selectVolumes.get(0)).getStartTime();
                    long endTime = end.getEndTime();
                    int curSelectVideoPosition = getCurSelectVideoPosition();
                    List<LongVideosModel> videoLongVideosModels = getVideoLongVideosModels();
                    long videoStartTime = 0;
                    long videoEndTime = 0;
                    for (int i = 0; i <= curSelectVideoPosition; i++) {
                        LongVideosModel model = (LongVideosModel) videoLongVideosModels.get(i);
                        videoEndTime += model.getCurrentDuration();
                        videoStartTime = videoEndTime - model.getCurrentDuration();
                    }
                    if (startTime >= videoEndTime || endTime <= videoStartTime) {
                        clearCurSelectVideo();
                        refreshVisibleVideoItem();
                    }
                }
            }
        }
    }

    private void hideAudioSlideView() {
        initTouchPresenter();
        this.mTouchPresenter.hideTouchView();
    }

    private void updateAudioSlideView(int curSelectAudioModelPosition) {
        initTouchPresenter();
        if (getCurSelectAudioModel() == null || getCurSelectAudioModel().isJustSeeForAudio()) {
            hideAudioSlideView();
        } else if (this.isMusicEdit) {
            this.mTouchPresenter.refreshTouchViewPos(getCurSelectAudioModel(), this.mAudioScrollX, this.mMaxScrollX, this.sumTime, this.screenWidth, curSelectAudioModelPosition);
        } else {
            this.mTouchPresenter.hideTouchView();
        }
    }

    private void initTouchPresenter() {
        if (this.mTouchPresenter == null) {
            this.mTouchPresenter = new TouchPresenterImpl(this.leftTouchView, this.rightTouchView, this.mSlideVideoAreaCallback);
        }
    }

    private boolean removeInvalidAudioModel(long sumTime) {
        return VideoModelHelper.removeInvalidAudioModel(getAudioLongVideoModels(), (float) sumTime);
    }

    private boolean removeInvalidAudioModel() {
        return VideoModelHelper.removeInvalidAudioModel(getAudioLongVideoModels(), this.sumTime);
    }

    private int dealSlideAudioRight(int position, int time) {
        if (position < 0 || position >= getAudioLongVideoModels().size()) {
            return 0;
        }
        LongVideosModel longVideosModel = (LongVideosModel) getAudioLongVideoModels().get(position);
        if (longVideosModel == null) {
            return 0;
        }
        int size = getAudioLongVideoModels().size();
        int indexOf = getAudioLongVideoModels().indexOf(longVideosModel);
        long startDuration = longVideosModel.getAudioDuration();
        longVideosModel.setUsageDurationValue(longVideosModel.getUsageDurationValue() + ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (time * 1000))).intValue()));
        longVideosModel.setAudioDuration(checkNearbyNumber(longVideosModel.getUsageDurationValue() + longVideosModel.getStartTimeMs()) - longVideosModel.getStartTimeMs());
        float maxEndTime = this.sumTime;
        if (time > 0) {
            if (((float) (longVideosModel.getAudioStartTime() + longVideosModel.getAudioDuration())) >= maxEndTime) {
                longVideosModel.setAudioDuration((long) (maxEndTime - ((float) longVideosModel.getAudioStartTime())));
                longVideosModel.setUsageDurationValue(longVideosModel.getAudioDuration());
            }
        } else if (time < 0 && longVideosModel.getAudioDuration() <= ((long) MIN_AUDIO_TIME_DURATION_MS)) {
            longVideosModel.setAudioDuration((long) MIN_AUDIO_TIME_DURATION_MS);
            longVideosModel.setUsageDurationValue(longVideosModel.getAudioDuration());
        }
        long endDuration = longVideosModel.getAudioDuration();
        int nextPos = indexOf + 1;
        long resultEnd = longVideosModel.getAudioStartTime() + longVideosModel.getAudioDuration();
        while (nextPos < size) {
            LongVideosModel next = (LongVideosModel) getAudioLongVideoModels().get(nextPos);
            if (!next.isJustSeeForAudio()) {
                long nextAudioStartTime = next.getAudioStartTime();
                long nextAudioDuration = next.getAudioDuration();
                long audioCacheStartTime = next.getAudioCacheStartTime();
                long audioCacheDuration = next.getAudioCacheDuration();
                long nextAudioMaxStartTime = audioCacheStartTime + audioCacheDuration;
                if (resultEnd <= nextAudioStartTime) {
                    if (audioCacheStartTime == nextAudioStartTime) {
                        break;
                    }
                    if (resultEnd < audioCacheStartTime) {
                        next.setAudioStartTime(audioCacheStartTime);
                        next.setAudioDuration(audioCacheDuration);
                    } else {
                        next.setAudioStartTime(resultEnd);
                        next.setAudioDuration(nextAudioMaxStartTime - resultEnd);
                        if (resultEnd > nextAudioMaxStartTime) {
                            next.setAudioStartTime(nextAudioMaxStartTime);
                            next.setAudioDuration(0);
                        }
                    }
                    nextPos++;
                } else {
                    next.setAudioStartTime(resultEnd);
                    next.setAudioDuration(nextAudioMaxStartTime - resultEnd);
                    if (resultEnd > nextAudioMaxStartTime) {
                        next.setAudioStartTime(nextAudioMaxStartTime);
                        next.setAudioDuration(0);
                    }
                    nextPos++;
                }
            } else {
                break;
            }
        }
        List<LongVideosModel> videoLongVideosModels = getVideoLongVideosModels();
        refreshAudioModelVolumes(videoLongVideosModels, videoLongVideosModels.size(), longVideosModel);
        refreshAudioEntities(getAudioLongVideoModels());
        refreshAudioMuteIndicator();
        refreshAudioSelectPos();
        refreshVisibleAudioItem();
        return (int) ((endDuration - startDuration) / ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf(1000)).intValue()));
    }

    private void cacheAudioModelsData() {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (TextUtil.isValidate(audioLongVideoModels)) {
            for (LongVideosModel model : audioLongVideoModels) {
                model.cacheAudioData();
            }
        }
    }

    private void clearAudioModelsCacheVolumes() {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (TextUtil.isValidate(audioLongVideoModels)) {
            for (LongVideosModel model : audioLongVideoModels) {
                clearAudioModelCacheVolumes(model);
            }
        }
    }

    private void clearAudioModelCacheVolumes(LongVideosModel audioModel) {
        if (audioModel != null) {
            audioModel.clearAudioVolumesCache();
        }
    }

    private int dealSlideAudioLeft(int position, int time) {
        if (position < 0 || position >= getAudioLongVideoModels().size() || time == 0) {
            return 0;
        }
        LongVideosModel longVideosModel = (LongVideosModel) getAudioLongVideoModels().get(position);
        if (longVideosModel == null) {
            return 0;
        }
        int audioPos = getAudioLongVideoModels().indexOf(longVideosModel);
        long beforeStartTime = longVideosModel.getAudioStartTime();
        long usageStartTime = longVideosModel.getUsageStartTime();
        long curDuration = longVideosModel.getAudioDuration();
        long resultStartTime = usageStartTime + ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (time * 1000))).intValue());
        longVideosModel.setUsageStartTime(resultStartTime);
        resultStartTime = checkNearbyNumber(resultStartTime);
        long changeTime = resultStartTime - beforeStartTime;
        if (time > 0) {
            longVideosModel.setAudioDuration(curDuration - changeTime);
            if (longVideosModel.getAudioDuration() < ((long) MIN_AUDIO_TIME_DURATION_MS)) {
                longVideosModel.setAudioDuration((long) MIN_AUDIO_TIME_DURATION_MS);
                changeTime = curDuration - ((long) MIN_AUDIO_TIME_DURATION_MS);
                longVideosModel.setUsageStartTime(beforeStartTime + changeTime);
            }
            longVideosModel.setAudioStartTime(beforeStartTime + changeTime);
        } else if (time < 0) {
            if (resultStartTime <= 0) {
                changeTime = 0 - beforeStartTime;
                longVideosModel.setUsageStartTime(beforeStartTime + changeTime);
            }
            longVideosModel.setAudioStartTime(beforeStartTime + changeTime);
            longVideosModel.setAudioDuration(curDuration - changeTime);
        }
        LogUtil.d(TAG, String.format("audioStartTime : %s , audioDuration : %s ", new Object[]{Long.valueOf(longVideosModel.getAudioStartTime()), Long.valueOf(longVideosModel.getAudioDuration())}));
        int prevPos = audioPos - 1;
        while (prevPos >= 0) {
            LongVideosModel prev = (LongVideosModel) getAudioLongVideoModels().get(prevPos);
            long prevAudioStartTime = prev.getAudioStartTime();
            long prevAudioDuration = prev.getAudioDuration();
            long audioCacheStartTime = prev.getAudioCacheStartTime();
            long audioCacheDuration = prev.getAudioCacheDuration();
            if (longVideosModel.getAudioStartTime() >= prevAudioStartTime + prevAudioDuration) {
                if (prevAudioDuration == audioCacheDuration) {
                    break;
                }
                if (longVideosModel.getAudioStartTime() - prevAudioStartTime <= audioCacheDuration) {
                    prev.setAudioDuration(longVideosModel.getAudioStartTime() - prevAudioStartTime);
                } else {
                    prev.setAudioDuration(audioCacheDuration);
                }
                prevPos--;
            } else {
                if (longVideosModel.getAudioStartTime() < prevAudioStartTime) {
                    prev.setAudioDuration(0);
                } else {
                    prev.setAudioDuration(longVideosModel.getAudioStartTime() - prevAudioStartTime);
                }
                prevPos--;
            }
        }
        List<LongVideosModel> videoLongVideosModels = getVideoLongVideosModels();
        refreshAudioModelVolumes(videoLongVideosModels, videoLongVideosModels.size(), longVideosModel);
        refreshAudioEntities(getAudioLongVideoModels());
        refreshVisibleAudioItem();
        if (position == 0) {
            refreshAddMusicView(this.mAudioScrollX);
        }
        refreshAudioMuteIndicator();
        refreshAudioSelectPos();
        return (int) ((longVideosModel.getAudioStartTime() - beforeStartTime) / ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf(1000)).intValue()));
    }

    private boolean checkListPositionIsValid(List<VideoEditImageEntity> data, int position) {
        if (data != null) {
            int size = data.size();
            if (position >= 0 && position < size) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTextPositionIsValid(int position) {
        if (this.mTextEntities != null) {
            int size = this.mTextEntities.size();
            if (position >= 0 && position < size) {
                return true;
            }
        }
        return false;
    }

    private int dealSlideTextContent(int position, int time, boolean isAutoScrolling) {
        if (!checkTextPositionIsValid(position)) {
            return 0;
        }
        LongVideosModel longVideosModel = ((VideoEditImageEntity) this.mTextEntities.get(position)).getLongVideosModel();
        if (longVideosModel == null) {
            return 0;
        }
        LongVideosModel prev;
        LongVideosModel next;
        long beforeStartTime = longVideosModel.getStartTimeMs();
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        int size = textVideosModels.size();
        int indexOf = textVideosModels.indexOf(longVideosModel);
        long currentDurationValue = longVideosModel.getCurrentDurationValue();
        long change = (long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (time * 1000))).intValue();
        if (indexOf > 0) {
            prev = (LongVideosModel) textVideosModels.get(indexOf - 1);
        } else {
            prev = null;
        }
        if (indexOf < size - 1) {
            next = (LongVideosModel) textVideosModels.get(indexOf + 1);
        } else {
            next = null;
        }
        long resultValue = longVideosModel.getUsageStartTime() + change;
        longVideosModel.setUsageStartTime(resultValue);
        longVideosModel.setStartTime(checkNearbyNumber(resultValue));
        boolean isMinValue = false;
        int circleCoverType;
        if (time > 0) {
            long endValue;
            if (next != null) {
                endValue = next.getStartTimeMs();
            } else {
                endValue = (long) this.sumTime;
            }
            if (longVideosModel.getStartTimeMs() + currentDurationValue >= endValue) {
                longVideosModel.setStartTime(endValue - currentDurationValue);
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                longVideosModel.setCircleCoverType(2);
                if (next != null) {
                    circleCoverType = next.getCircleCoverType();
                    if (circleCoverType == 2) {
                        next.setCircleCoverType(3);
                    } else if (circleCoverType == 0) {
                        next.setCircleCoverType(1);
                    }
                }
            } else {
                longVideosModel.setCircleCoverType(0);
                if (prev != null) {
                    circleCoverType = prev.getCircleCoverType();
                    if (circleCoverType == 2) {
                        prev.setCircleCoverType(0);
                    } else if (circleCoverType == 3) {
                        prev.setCircleCoverType(1);
                    }
                }
            }
        } else if (time < 0) {
            long minValue;
            if (prev != null) {
                minValue = prev.getStartTimeMs() + prev.getCurrentDurationValue();
            } else {
                minValue = 0;
            }
            if (longVideosModel.getStartTimeMs() <= minValue) {
                LogUtil.d(TAG, String.format("minValue : %s ", new Object[]{Long.valueOf(minValue)}));
                longVideosModel.setStartTime(minValue);
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                isMinValue = true;
                longVideosModel.setCircleCoverType(1);
                if (prev != null) {
                    circleCoverType = prev.getCircleCoverType();
                    if (circleCoverType == 1) {
                        prev.setCircleCoverType(3);
                    } else if (circleCoverType == 0) {
                        prev.setCircleCoverType(2);
                    }
                }
            } else {
                longVideosModel.setCircleCoverType(0);
                if (next != null) {
                    circleCoverType = next.getCircleCoverType();
                    if (circleCoverType == 3) {
                        next.setCircleCoverType(2);
                    } else if (circleCoverType == 1) {
                        next.setCircleCoverType(0);
                    }
                }
            }
        }
        int resultTime = (int) ((longVideosModel.getStartTimeMs() - beforeStartTime) / ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf(1000)).intValue()));
        LogUtil.d(TAG, String.format("onSlideTextContent startTime : %s ", new Object[]{Long.valueOf(longVideosModel.getStartTimeMs())}));
        refreshTextView(textVideosModels, this.curTextVideoModel, false, false, position, resultTime, isMinValue, false, isAutoScrolling);
        return resultTime;
    }

    private int dealSlideTextRight(int position, int time) {
        LogUtil.d(TAG, String.format("dealSlideTextRight position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
        if (!checkTextPositionIsValid(position)) {
            return 0;
        }
        LongVideosModel longVideosModel = ((VideoEditImageEntity) this.mTextEntities.get(position)).getLongVideosModel();
        long startDuration = longVideosModel.getCurrentDurationValue();
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        int size = textVideosModels.size();
        int indexOf = textVideosModels.indexOf(longVideosModel);
        longVideosModel.setUsageDurationValue(longVideosModel.getUsageDurationValue() + ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (time * 1000))).intValue()));
        longVideosModel.setCurrentDuration(checkNearbyNumber(longVideosModel.getUsageDurationValue() + longVideosModel.getStartTimeMs()) - longVideosModel.getStartTimeMs());
        float maxEndTime = this.sumTime;
        if (indexOf < size - 1) {
            maxEndTime = (float) ((LongVideosModel) textVideosModels.get(indexOf + 1)).getStartTimeMs();
        }
        int circleCoverType1;
        LongVideosModel next;
        int circleCoverType;
        if (time > 0) {
            if (((float) (longVideosModel.getStartTimeMs() + longVideosModel.getCurrentDurationValue())) >= maxEndTime) {
                longVideosModel.setCurrentDuration((long) (maxEndTime - ((float) longVideosModel.getStartTimeMs())));
                longVideosModel.setUsageDurationValue(longVideosModel.getCurrentDurationValue());
                circleCoverType1 = longVideosModel.getCircleCoverType();
                if (circleCoverType1 == 0) {
                    longVideosModel.setCircleCoverType(2);
                } else if (circleCoverType1 == 1) {
                    longVideosModel.setCircleCoverType(3);
                }
                if (indexOf < size - 1) {
                    next = (LongVideosModel) textVideosModels.get(indexOf + 1);
                    if (next != null) {
                        circleCoverType = next.getCircleCoverType();
                        if (circleCoverType == 2) {
                            next.setCircleCoverType(3);
                        } else if (circleCoverType == 0) {
                            next.setCircleCoverType(1);
                        }
                    }
                }
            }
        } else if (time < 0) {
            if (longVideosModel.getCurrentDurationValue() <= 1000) {
                longVideosModel.setCurrentDuration(1000);
                longVideosModel.setUsageDurationValue(longVideosModel.getCurrentDurationValue());
            }
            circleCoverType1 = longVideosModel.getCircleCoverType();
            if (circleCoverType1 == 2) {
                longVideosModel.setCircleCoverType(0);
            } else if (circleCoverType1 == 3) {
                longVideosModel.setCircleCoverType(0);
            }
            if (indexOf < size - 1) {
                next = (LongVideosModel) textVideosModels.get(indexOf + 1);
                if (next != null) {
                    circleCoverType = next.getCircleCoverType();
                    if (circleCoverType == 3) {
                        next.setCircleCoverType(2);
                    } else if (circleCoverType == 1) {
                        next.setCircleCoverType(0);
                    }
                }
            }
        }
        int resultTime = (int) ((longVideosModel.getCurrentDurationValue() - startDuration) / ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf(1000)).intValue()));
        refreshTextView(textVideosModels, longVideosModel, false);
        return resultTime;
    }

    private int dealSlideTextLeft(int position, int time) {
        if (!checkTextPositionIsValid(position)) {
            return 0;
        }
        LongVideosModel longVideosModel = ((VideoEditImageEntity) this.mTextEntities.get(position)).getLongVideosModel();
        long beforeStartTime = longVideosModel.getStartTimeMs();
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        int indexOf = textVideosModels.indexOf(longVideosModel);
        long currentDurationValue = longVideosModel.getCurrentDurationValue();
        long resultStart = longVideosModel.getUsageStartTime() + ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (time * 1000))).intValue());
        longVideosModel.setUsageStartTime(resultStart);
        resultStart = checkNearbyNumber(resultStart);
        long change = resultStart - longVideosModel.getStartTimeMs();
        long endTime = 0;
        if (indexOf > 0) {
            LongVideosModel before = (LongVideosModel) textVideosModels.get(indexOf - 1);
            endTime = before.getStartTimeMs() + before.getCurrentDurationValue();
        }
        int circleCoverType1;
        LongVideosModel prev;
        int circleCoverType;
        if (time > 0) {
            longVideosModel.setCurrentDuration(currentDurationValue - change);
            if (longVideosModel.getCurrentDurationValue() <= 1000) {
                longVideosModel.setCurrentDuration(1000);
                change = currentDurationValue - 1000;
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs() + change);
            }
            circleCoverType1 = longVideosModel.getCircleCoverType();
            if (circleCoverType1 == 3) {
                longVideosModel.setCircleCoverType(2);
            } else if (circleCoverType1 == 1) {
                longVideosModel.setCircleCoverType(0);
            }
            if (indexOf > 0) {
                prev = (LongVideosModel) textVideosModels.get(indexOf - 1);
                if (prev != null) {
                    circleCoverType = prev.getCircleCoverType();
                    if (circleCoverType == 3) {
                        prev.setCircleCoverType(1);
                    } else if (circleCoverType == 2) {
                        prev.setCircleCoverType(0);
                    }
                }
            }
            longVideosModel.setStartTime(longVideosModel.getStartTimeMs() + change);
        } else if (time < 0) {
            if (resultStart <= endTime) {
                change = endTime - longVideosModel.getStartTimeMs();
                longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs() + change);
                circleCoverType1 = longVideosModel.getCircleCoverType();
                if (circleCoverType1 == 2) {
                    longVideosModel.setCircleCoverType(3);
                } else if (circleCoverType1 == 0) {
                    longVideosModel.setCircleCoverType(1);
                }
                if (indexOf > 0) {
                    prev = (LongVideosModel) textVideosModels.get(indexOf - 1);
                    if (prev != null) {
                        circleCoverType = prev.getCircleCoverType();
                        if (circleCoverType == 1) {
                            prev.setCircleCoverType(3);
                        } else if (circleCoverType == 0) {
                            prev.setCircleCoverType(2);
                        }
                    }
                }
            }
            longVideosModel.setStartTime(longVideosModel.getStartTimeMs() + change);
            longVideosModel.setCurrentDuration(currentDurationValue - change);
        }
        int resultTime = (int) ((longVideosModel.getStartTimeMs() - beforeStartTime) / ((long) IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf(1000)).intValue()));
        refreshTextView(textVideosModels, longVideosModel, false);
        return resultTime;
    }

    private void refreshEndRelate() {
        for (LongVideosModel model : getTextVideosModels()) {
            if (!(refreshEndRelate(model) || model.getEndRelateTextModel() == null)) {
                model.getEndRelateTextModel().setEndRelateTextModel(null);
                model.setEndRelateTextModel(null);
            }
        }
    }

    private void refreshEndRelateAndRefresh() {
        checkAndBindRelateTextModelAndVideoModel();
        refreshEndRelate();
        refreshCacheTextModels();
        refreshTextView(getTextVideosModels(), this.curTextVideoModel, false);
    }

    private boolean refreshEndRelate(LongVideosModel textModel) {
        long durationValue = textModel.getCurrentDurationValue();
        long startTime = textModel.getStartTimeMs();
        long time = startTime + durationValue;
        long start = 0;
        long end = 0;
        for (LongVideosModel model : getVideoLongVideosModels()) {
            end += model.getCurrentDuration();
            if (time != end || startTime < start) {
                start += model.getCurrentDuration();
            } else {
                model.setEndRelateTextModel(textModel);
                textModel.setEndRelateTextModel(model);
                return true;
            }
        }
        return false;
    }

    private long checkNearbyNumber(long number) {
        Iterator it = this.timeList.iterator();
        while (it.hasNext()) {
            long time = ((Long) it.next()).longValue();
            if (number >= time - 200 && number <= time + 200) {
                return time;
            }
        }
        return number;
    }

    private void prepareNearbyJumpData() {
        this.timeList.clear();
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        if (!TextUtil.isNull(longVideosModels)) {
            long jumpTime = 0;
            this.timeList.add(Long.valueOf(0));
            for (LongVideosModel model : longVideosModels) {
                jumpTime += model.getCurrentDuration();
                this.timeList.add(Long.valueOf(jumpTime));
            }
            LogUtil.d(TAG, String.format("timeList size : %s , timeList.toString : %s", new Object[]{Integer.valueOf(this.timeList.size()), this.timeList.toString()}));
        }
    }

    private void refreshCacheAudioTextModels() {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        if (audioLongVideoModels != null) {
            for (LongVideosModel model : audioLongVideoModels) {
                model.setCacheStartTime(model.getAudioStartTime());
                model.setCacheDuration(model.getCacheDuration());
            }
        }
    }

    private void refreshCacheTextModels() {
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (textVideosModels != null) {
            for (LongVideosModel model : textVideosModels) {
                model.cacheTextRelateData();
            }
        }
    }

    private int dealLeftPosition(int position, int time) {
        LogUtil.d(TAG, String.format("dealLeftPosition start  position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mImageEntities.get(position);
        LongVideosModel longVideosModel = videoEditImageEntity.getLongVideosModel();
        if (longVideosModel == null) {
            return 0;
        }
        if (longVideosModel.getStartTimeMs() <= 0 && time < 0) {
            return 0;
        }
        float showStart = videoEditImageEntity.getShowStart();
        float showEnd = videoEditImageEntity.getShowEnd();
        LogUtil.d(TAG, String.format("showStartAndEnd showStart : %s , showEnd : %s ", new Object[]{Float.valueOf(showStart), Float.valueOf(showEnd)}));
        long curSlideSumTime = 0;
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        for (int i = 0; i < longVideosModels.indexOf(longVideosModel); i++) {
            curSlideSumTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
        }
        long changeTime;
        if (time > 0) {
            time = longVideosModel.addStartTime(time, true);
            if (time == 0) {
                return time;
            }
            reduceSumTime(-time);
            changeTime = (long) ((1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) time));
            refreshRelateAudioData(curSlideSumTime, changeTime);
            changeOtherStartTime(false, curSlideSumTime, -changeTime, longVideosModel);
            refreshOtherView();
            addStartTime(position, time, videoEditImageEntity, longVideosModel, showStart, showEnd);
        } else {
            time = longVideosModel.reduceStartTime(time);
            if (time == 0) {
                return time;
            }
            addSumTime(-time);
            changeTime = (long) ((1000.0f * IMAGE_SLIDE_MIN_EXPANSION.floatValue()) * ((float) time));
            refreshRelateAudioData(curSlideSumTime, changeTime);
            changeOtherStartTime(false, curSlideSumTime, -changeTime, longVideosModel);
            refreshOtherView();
            reduceStartTime(position, time, videoEditImageEntity, longVideosModel, showStart);
        }
        LogUtil.d(TAG, String.format("dealLeftPosition end  position : %s , time : %s ", new Object[]{Integer.valueOf(position), Integer.valueOf(time)}));
        this.mChangeOtherCallback.videoTimeChange();
        return time;
    }

    private void refreshRelateAudioData(long curSlideSumTime, long changeTime) {
        List<LongVideosModel> audioLongVideoModels = getAudioLongVideoModels();
        int endRelateAudioPos = -1;
        if (TextUtil.isValidate(audioLongVideoModels)) {
            LongVideosModel audioModel;
            int size = audioLongVideoModels.size();
            int i = 0;
            while (i < size) {
                audioModel = (LongVideosModel) audioLongVideoModels.get(i);
                long audioStartTime = audioModel.getAudioStartTime();
                long audioEndTime = audioModel.getAudioStartTime() + audioModel.getAudioDuration();
                if (curSlideSumTime >= audioStartTime && curSlideSumTime <= audioEndTime) {
                    endRelateAudioPos = i;
                    audioModel.setAudioDuration(audioModel.getAudioDuration() - changeTime);
                    break;
                } else if (audioStartTime > curSlideSumTime) {
                    endRelateAudioPos = i - 1;
                    break;
                } else {
                    i++;
                }
            }
            if (endRelateAudioPos != -1) {
                for (i = endRelateAudioPos + 1; i < size; i++) {
                    audioModel = (LongVideosModel) audioLongVideoModels.get(i);
                    audioModel.setAudioStartTime(audioModel.getAudioStartTime() - changeTime);
                }
            }
        }
    }

    private void changeOtherStartTimeWhenSlideEnd(long thanTimeValue, long changeTime, LongVideosModel videoModel) {
        LogUtil.d(TAG, String.format("changeOtherStartTimeWhenSlideEnd thanTimeValue : %s , changeTime : %s ", new Object[]{Long.valueOf(thanTimeValue), Long.valueOf(changeTime)}));
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (textVideosModels != null) {
            LongVideosModel textModel = videoModel.getEndRelateTextModel();
            if (textModel != null) {
                textModel.setCurrentDuration(textModel.getCurrentDurationValue() + changeTime);
                LongVideosModel endRelateTextModel = textModel.getEndRelateTextModel();
                if (endRelateTextModel != null && textModel.getRelateStartTime() + textModel.getCurrentDurationValue() > endRelateTextModel.getCurrentDuration()) {
                    textModel.setCurrentDuration(endRelateTextModel.getCurrentDuration() - textModel.getRelateStartTime());
                }
                if (textModel.getCurrentDurationValue() < 1000) {
                    textModel.setCurrentDuration(1000);
                    if (textModel.getEndRelateTextModel() != null) {
                        LongVideosModel endRelateTextModel1 = textModel.getEndRelateTextModel();
                        if (endRelateTextModel1 != null) {
                            endRelateTextModel1.setEndRelateTextModel(null);
                        }
                        textModel.setEndRelateTextModel(null);
                    }
                }
            }
            int size = textVideosModels.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                LogUtil.d(TAG, String.format("changeOtherStartTimeWhenSlideEnd model.getStartTimeMs() : %s , thanTimeValue : %s ", new Object[]{Long.valueOf(model.getStartTimeMs()), Long.valueOf(thanTimeValue)}));
                if (model.getStartTimeMs() >= thanTimeValue) {
                    if (!model.isHideForSpecial()) {
                        model.setStartTime(model.getStartTimeMs() + changeTime);
                        model.setCacheStartTime(model.getStartTimeMs());
                        if (model.getPointToVideoModel() == videoModel) {
                            model.setRelateStartTime(model.getRelateStartTime() + changeTime);
                            if (model.getRelateStartTime() < 0) {
                                model.setHideForSpecial(true);
                            } else {
                                model.setHideForSpecial(false);
                            }
                        }
                        int prev = i - 1;
                        if (prev >= 0) {
                            long startTime = model.getStartTimeMs();
                            model = (LongVideosModel) textVideosModels.get(prev);
                            if (model.getStartTimeMs() + model.getCurrentDurationValue() > startTime) {
                                model.setCurrentDuration(startTime - model.getStartTimeMs());
                            } else if (model.getCurrentDurationValue() <= model.getCacheDuration() && model.getEndRelateTextModel() == null) {
                                model.setCurrentDuration(startTime - model.getStartTimeMs());
                                if (model.getCurrentDurationValue() > model.getCacheDuration()) {
                                    model.setCurrentDuration(model.getCacheDuration());
                                }
                            }
                        }
                    }
                } else if (model.getStartTimeMs() + ((long) (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT)) >= thanTimeValue) {
                    model.setHideForSpecial(true);
                    if (model.getEndRelateTextModel() == videoModel) {
                        model.setEndRelateTextModel(null);
                        if (videoModel.getEndRelateTextModel() == model) {
                            videoModel.setEndRelateTextModel(null);
                        }
                    }
                } else {
                    model.setHideForSpecial(false);
                    if (((float) (model.getStartTimeMs() + model.getCurrentDurationValue())) >= this.sumTime) {
                        model.setCurrentDuration((long) (this.sumTime - ((float) model.getStartTimeMs())));
                        if (model.getCurrentDurationValue() < 1000) {
                            model.setHideForSpecial(true);
                        }
                    } else if (!(model.getStartTimeMs() + model.getCurrentDurationValue() >= thanTimeValue || model.getCurrentDurationValue() == model.getCacheDuration() || model.getEndRelateTextModel() == videoModel)) {
                        model.setCurrentDuration(model.getCacheDuration());
                        if (model.getStartTimeMs() + model.getCurrentDurationValue() > thanTimeValue) {
                            model.setCurrentDuration(thanTimeValue - model.getStartTimeMs());
                            if (model.getCurrentDurationValue() < 1000) {
                                model.setHideForSpecial(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void changeOtherStartTimeWhenSlideStart(long thanTimeValue, long changeTime, LongVideosModel videoModel) {
        LogUtil.d(TAG, String.format("changeOtherStartTimeWhenSlideStart thanTimeValue : %s , changeTime : %s ", new Object[]{Long.valueOf(thanTimeValue), Long.valueOf(changeTime)}));
        List<LongVideosModel> textVideosModels = getTextVideosModels();
        if (textVideosModels != null) {
            int size = textVideosModels.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                if (model.getStartTimeMs() + ((long) (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT)) > thanTimeValue) {
                    model.setStartTime(model.getStartTimeMs() + changeTime);
                    if (model.getPointToVideoModel() == videoModel) {
                        model.setRelateStartTime(model.getRelateStartTime() + changeTime);
                        if (model.getRelateStartTime() <= (-((long) (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT)))) {
                            model.setHideForSpecial(true);
                        } else {
                            model.setHideForSpecial(false);
                        }
                    }
                    int prev = i - 1;
                    if (prev >= 0) {
                        long startTime = model.getStartTimeMs();
                        if (!model.isHideForSpecial()) {
                            model = (LongVideosModel) textVideosModels.get(prev);
                            if (model.isHideForSpecial()) {
                                prev--;
                                if (prev >= 0) {
                                    model = (LongVideosModel) textVideosModels.get(prev);
                                }
                            }
                            if (model.getStartTimeMs() + model.getCurrentDurationValue() > startTime) {
                                model.setCurrentDuration(startTime - model.getStartTimeMs());
                            } else if (model.getCurrentDurationValue() <= model.getCacheDuration()) {
                                model.setCurrentDuration(startTime - model.getStartTimeMs());
                                if (model.getCurrentDurationValue() > model.getCacheDuration()) {
                                    model.setCurrentDuration(model.getCacheDuration());
                                }
                            }
                        }
                    }
                } else if (model.isHideForSpecial()) {
                    model.setStartTime(model.getStartTimeMs() + changeTime);
                    if (model.getPointToVideoModel() == videoModel) {
                        model.setRelateStartTime(model.getRelateStartTime() + changeTime);
                        if (model.getRelateStartTime() <= (-((long) (1000.0f * VideoEditTextHolder.TEXT_CIRCLE_CENTER_PERCENT)))) {
                            model.setHideForSpecial(true);
                        } else {
                            model.setHideForSpecial(false);
                        }
                    }
                } else if (model.getCurrentDurationValue() != model.getCacheDuration()) {
                    model.setCurrentDuration(model.getCacheDuration());
                }
            }
        }
    }

    private void changeOtherStartTime(boolean isEnd, long thanTimeValue, long changeTime, LongVideosModel videoModel) {
        if (isEnd) {
            changeOtherStartTimeWhenSlideEnd(thanTimeValue, changeTime, videoModel);
        } else {
            changeOtherStartTimeWhenSlideStart(thanTimeValue, changeTime, videoModel);
        }
    }

    private void removeInvalidTextModelAndRefresh() {
        removeInvalidTextModel();
        refreshTextView(getTextVideosModels(), this.curTextVideoModel, true);
    }

    private void reduceStartTime(int position, int time, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel, float showStart) {
        float reducedStart;
        float start = BigDecimal.valueOf((double) showStart).add(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time))).setScale(1, 4).floatValue();
        LogUtil.d(TAG, String.format("reduceStartTime start : %s ", new Object[]{Float.valueOf(start)}));
        videoEditImageEntity.setShowStart(start);
        if (showStart >= StaticLayoutUtil.DefaultSpacingadd && start < StaticLayoutUtil.DefaultSpacingadd && ((double) start) > -0.5d) {
            videoEditImageEntity.setLeftNeedShowAnim(true);
        }
        boolean needAdd = ((double) videoEditImageEntity.getShowStart()) <= -0.5d;
        if (needAdd) {
            reducedStart = showStart - StaticLayoutUtil.DefaultSpacingadd;
        } else {
            reducedStart = showStart - videoEditImageEntity.getShowStart();
        }
        if (needAdd) {
            videoEditImageEntity.setShowStart(StaticLayoutUtil.DefaultSpacingadd);
        }
        if (longVideosModel.isCanSlideLeft()) {
            ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(false);
        } else {
            ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(true);
        }
        this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
        int[] ints = (int[]) this.mModelListHashMap.get(longVideosModel);
        int curEnd = ints[1];
        this.mVideoEditImageAdapter.notifyItemChanged(curEnd);
        this.mVideoEditImageAdapter.notifyItemChanged(position);
        LogUtil.d(TAG, String.format("reduceStartTime time : %s , firstScrollX : %s , reducedStart : %s ", time, (int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((double) (10.0f * reducedStart))).setScale(1, RoundingMode.HALF_UP).floatValue()), reducedStart));
        int firstScrollX = (int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((double) (10.0f * reducedStart))).setScale(1, RoundingMode.HALF_UP).floatValue());
        this.mImagesRecyclerView.scrollBy(firstScrollX, 0);
        if (needAdd) {
            int tempTime = ((int) (10.0f * showStart)) + time;
            LogUtil.d(TAG, String.format("reduceStartTime tempTime : %s ", new Object[]{Integer.valueOf(tempTime)}));
            if (longVideosModel.isCanSlideLeft()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
            int circleCoverType = videoEditImageEntity.getCircleCoverType();
            if (circleCoverType == 3) {
                videoEditImageEntity.setCircleCoverType(2);
            } else if (circleCoverType == 1) {
                videoEditImageEntity.setCircleCoverType(0);
            }
            int innerCircleCoverType = videoEditImageEntity.getInnerCircleCoverType();
            if (innerCircleCoverType == 3) {
                videoEditImageEntity.setInnerCircleCoverType(2);
            } else if (innerCircleCoverType == 1) {
                videoEditImageEntity.setInnerCircleCoverType(0);
            }
            boolean showEffectImage = videoEditImageEntity.isShowEffectImage();
            videoEditImageEntity.setShowEffectImage(false);
            this.mVideoEditImageAdapter.notifyItemChanged(position);
            int index = videoEditImageEntity.getTimeIndex();
            long imageTime = videoEditImageEntity.getImageTime();
            VideoEditImageEntity videoEditImageEntity2 = new VideoEditImageEntity(BaseAdapter.Item.VIDEO_EDIT_IMAGE_TYPE);
            videoEditImageEntity2.setWhite_area_type(1);
            videoEditImageEntity2.setCircleCoverType(1);
            videoEditImageEntity2.setLongVideosModel(longVideosModel);
            videoEditImageEntity2.setTimeIndex(index - 1);
            videoEditImageEntity2.setImageTime(imageTime - 1000);
            videoEditImageEntity2.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
            videoEditImageEntity2.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
            videoEditImageEntity2.setInnerCircleCoverType(1);
            videoEditImageEntity2.setShowStart(new BigDecimal(1).add(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) tempTime))).floatValue());
            videoEditImageEntity2.setShowEnd(1.0f);
            videoEditImageEntity2.setShowEffectImage(showEffectImage);
            this.mImageEntities.add(position, videoEditImageEntity2);
            this.mVideoEditImageAdapter.notifyItemInserted(position);
            LogUtil.d(TAG, String.format("reduceStartTime newImageEntity.start : %s ", new Object[]{Float.valueOf(videoEditImageEntity2.getShowStart())}));
            ints[1] = curEnd + 1;
            this.mModelListHashMap.put(longVideosModel, ints);
            List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
            int size = longVideosModels.size();
            for (int i = longVideosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                addVideoModelArea((LongVideosModel) longVideosModels.get(i));
            }
            LogUtil.d(TAG, String.format("reduceStartTime tempTime : %s , secondScrollX : %s ", tempTime, (int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (-tempTime))).setScale(1, RoundingMode.HALF_UP).floatValue())));
            int secondScrollX = (int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) (-tempTime))).setScale(1, RoundingMode.HALF_UP).floatValue());
            this.mImagesRecyclerView.scrollBy(secondScrollX, 0);
        }
    }

    private void addStartTime(int position, int time, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel, float showStart, float showEnd) {
        float start = new BigDecimal((double) showStart).add(IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time))).setScale(1, 4).floatValue();
        LogUtil.d(TAG, String.format("addStartTime start : %s , end : %s ", new Object[]{Float.valueOf(start), Float.valueOf(videoEditImageEntity.getShowEnd())}));
        videoEditImageEntity.setShowStart(start);
        float curShowStart = videoEditImageEntity.getShowStart();
        List<LongVideosModel> longVideosModels = getVideoLongVideosModels();
        int[] ints;
        int curEnd;
        int innerCircleCoverType;
        int size;
        int i;
        if (curShowStart >= videoEditImageEntity.getShowEnd()) {
            int tempTime = (((int) (10.0f * showEnd)) - ((int) (10.0f * showStart))) - time;
            LogUtil.d(TAG, String.format("addStartTime tempTime : %s ", new Object[]{Integer.valueOf(tempTime)}));
            ints = (int[]) this.mModelListHashMap.get(longVideosModel);
            curEnd = ints[1];
            if (longVideosModel.isCanSlideLeft()) {
                ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(false);
            } else {
                ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(true);
            }
            this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
            this.mVideoEditImageAdapter.notifyItemChanged(curEnd);
            VideoEditImageEntity afterEntity = (VideoEditImageEntity) this.mImageEntities.get(position + 1);
            afterEntity.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
            afterEntity.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
            if (afterEntity.getCircleCoverType() == 0) {
                afterEntity.setCircleCoverType(1);
            } else if (afterEntity.getCircleCoverType() == 2) {
                afterEntity.setCircleCoverType(3);
            }
            innerCircleCoverType = afterEntity.getInnerCircleCoverType();
            if (innerCircleCoverType == 0) {
                afterEntity.setCircleCoverType(1);
            } else if (innerCircleCoverType == 2) {
                afterEntity.setCircleCoverType(3);
            }
            afterEntity.setShowEffectImage(videoEditImageEntity.isShowEffectImage());
            this.mImageEntities.remove(position);
            this.mVideoEditImageAdapter.notifyItemRemoved(position);
            ints[1] = curEnd - 1;
            this.mModelListHashMap.put(longVideosModel, ints);
            size = longVideosModels.size();
            for (i = longVideosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                reduceVideoModelArea((LongVideosModel) longVideosModels.get(i));
            }
            int changeTime = BigDecimal.valueOf((double) ((showEnd - showStart) / IMAGE_SLIDE_MIN_EXPANSION.floatValue())).setScale(0, 4).intValue();
            if (this.mImagesScrollX > 0) {
                int firstScrollX = -((int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) changeTime)).setScale(1, RoundingMode.HALF_UP).floatValue()));
                LogUtil.d(TAG, String.format("addStartTime firstScrollX : %s , changeTime : %s ", new Object[]{Integer.valueOf(firstScrollX), Integer.valueOf(changeTime)}));
                this.mImagesRecyclerView.scrollBy(firstScrollX, 0);
            } else {
                resetFirstEmptyViewWidth(changeTime);
            }
            if (tempTime < 0) {
                VideoEditImageEntity entity = (VideoEditImageEntity) this.mImageEntities.get(position);
                addStartTime(position, -tempTime, entity, longVideosModel, entity.getShowStart(), entity.getShowEnd());
                return;
            }
            return;
        }
        if (longVideosModel.isCanSlideLeft()) {
            ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(false);
        } else {
            ((VideoEditImageEntity) this.mImageEntities.get(position - 1)).setHideRightArrowImage(true);
        }
        this.mVideoEditImageAdapter.notifyItemChanged(position - 1);
        ints = (int[]) this.mModelListHashMap.get(longVideosModel);
        curEnd = ints[1];
        this.mVideoEditImageAdapter.notifyItemChanged(curEnd);
        if (((double) start) > 0.5d && videoEditImageEntity.getShowEnd() == 1.0f) {
            VideoEditImageEntity next = (VideoEditImageEntity) this.mImageEntities.get(position + 1);
            next.setShowStart(sub(start, 1.0f));
            next.setHideLeftArrowImage(videoEditImageEntity.isHideLeftArrowImage());
            next.setHideRightArrowImage(videoEditImageEntity.isHideRightArrowImage());
            if (next.getCircleCoverType() == 0) {
                next.setCircleCoverType(1);
            } else if (next.getCircleCoverType() == 2) {
                next.setCircleCoverType(3);
            }
            innerCircleCoverType = next.getInnerCircleCoverType();
            if (innerCircleCoverType == 0) {
                next.setInnerCircleCoverType(1);
            } else if (innerCircleCoverType == 2) {
                next.setInnerCircleCoverType(3);
            }
            next.setShowEffectImage(videoEditImageEntity.isShowEffectImage());
            this.mVideoEditImageAdapter.notifyItemChanged(position + 1);
            this.mImageEntities.remove(position);
            this.mVideoEditImageAdapter.notifyItemRemoved(position);
            ints[1] = curEnd - 1;
            this.mModelListHashMap.put(longVideosModel, ints);
            size = longVideosModels.size();
            for (i = longVideosModels.indexOf(longVideosModel) + 1; i < size; i++) {
                reduceVideoModelArea((LongVideosModel) longVideosModels.get(i));
            }
        }
        this.mVideoEditImageAdapter.notifyItemChanged(position);
        if (this.mImagesScrollX > 0) {
            int secondScrollX = -((int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) time)).setScale(1, RoundingMode.HALF_UP).floatValue()));
            LogUtil.d(TAG, String.format("addStartTime secondScrollX : %s , time : %s ", new Object[]{Integer.valueOf(secondScrollX), Integer.valueOf(time)}));
            this.mImagesRecyclerView.scrollBy(secondScrollX, 0);
            return;
        }
        resetFirstEmptyViewWidth(time);
    }

    private void resetFirstEmptyViewWidth(int number) {
        VideoEditImageEntity head = (VideoEditImageEntity) this.mImageEntities.get(0);
        int empty_type_width = head.getEmpty_type_width() + ((int) (((float) IMAGE_UNIT_WIDTH) * IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((long) number)).setScale(1, RoundingMode.HALF_UP).floatValue()));
        LogUtil.d(TAG, String.format("resetFirstEmptyViewWidth  empty_type_width : %s , number : %s", new Object[]{Integer.valueOf(empty_type_width), Integer.valueOf(number)}));
        head.setEmpty_type_width(empty_type_width);
        int changeWidth = head.getEmpty_type_width() - this.VIDEO_START_AND_END_EMPTY_WIDTH;
        int emptyTypeWidth = (this.screenWidth / 2) + changeWidth;
        ((VideoEditImageEntity) this.mTimesEntities.get(0)).setEmpty_type_width(emptyTypeWidth);
        ((VideoEditImageEntity) this.mAudioEntities.get(0)).setEmpty_type_width(head.getEmpty_type_width());
        VideoEditImageEntity textHead = null;
        if (this.mTextEntities != null && this.mTextEntities.size() > 0) {
            textHead = (VideoEditImageEntity) this.mTextEntities.get(0);
            textHead.setEmpty_type_width(emptyTypeWidth);
        }
        this.mVideoEditAudioAdapter.notifyItemChanged(0);
        this.mVideoEditImageAdapter.notifyItemChanged(0);
        if (textHead != null) {
            this.mVideoEditTextAdapter.notifyItemChanged(0);
        }
        this.mVideoEditTimesAdapter.notifyDataSetChanged();
        findVideoDurationTvShowRect(this.curSelectVideo);
        if (this.needCheckMissingFootage) {
            refreshMissingFootage();
        }
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.addVideoAddMusicTvLeftMargin(changeWidth);
        }
    }

    public void getModelCurrentDuration(int r_or_l) {
        this.isAutoScrolling = true;
        partVideosCurrentDuration(r_or_l);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0079  */
    private void partVideosCurrentDuration(int r13) {
        /*
        r12 = this;
        r1 = r12.getVideoLongVideosModels();
        if (r1 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r7 = 0;
        r6 = r1.size();
        r2 = 0;
        r0 = 0;
    L_0x000e:
        if (r0 >= r6) goto L_0x0039;
    L_0x0010:
        r8 = r1.get(r0);
        r8 = (com.blink.academy.onetake.bean.longvideo.LongVideosModel) r8;
        r8 = r8.getCurrentDuration();
        r10 = IMAGE_UNIT_WIDTH;
        r10 = (long) r10;
        r8 = r8 * r10;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r10;
        r5 = (int) r8;
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r7 - r9;
        if (r8 <= r9) goto L_0x00a0;
    L_0x002a:
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r9 + r7;
        if (r8 >= r9) goto L_0x00a0;
    L_0x0031:
        r8 = 1;
        if (r13 != r8) goto L_0x0083;
    L_0x0034:
        r7 = r7 + r5;
    L_0x0035:
        r8 = r12.mImagesScrollX;
        r2 = r7 - r8;
    L_0x0039:
        if (r0 != r6) goto L_0x0051;
    L_0x003b:
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r7 - r9;
        if (r8 <= r9) goto L_0x00d9;
    L_0x0043:
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r9 + r7;
        if (r8 >= r9) goto L_0x00d9;
    L_0x004a:
        r8 = 1;
        if (r13 != r8) goto L_0x00bb;
    L_0x004d:
        r8 = r12.mImagesScrollX;
        r2 = r7 - r8;
    L_0x0051:
        r8 = r12.mImagesScrollX;
        r4 = r8 + r2;
        r8 = r12.screenWidth;
        if (r2 <= r8) goto L_0x0107;
    L_0x0059:
        r8 = r12.mImagesRecyclerView;
        r9 = r12.screenWidth;
        r9 = r2 - r9;
        r10 = 0;
        r8.scrollBy(r9, r10);
        r8 = r12.mImagesRecyclerView;
        r9 = r12.screenWidth;
        r10 = 0;
        r8.smoothScrollBy(r9, r10);
    L_0x006b:
        r8 = 1;
        if (r13 != r8) goto L_0x0128;
    L_0x006e:
        r8 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r4 = r4 + r8;
    L_0x0071:
        r8 = r12.mMaxScrollX;
        if (r4 <= r8) goto L_0x0077;
    L_0x0075:
        r4 = r12.mMaxScrollX;
    L_0x0077:
        if (r4 >= 0) goto L_0x007a;
    L_0x0079:
        r4 = 0;
    L_0x007a:
        r12.callVideoSeekTo(r4);
        r12.needResetOutScrollX = r4;
        r8 = 1;
        r12.needResetOutTextModel = r8;
        goto L_0x0006;
    L_0x0083:
        r8 = 2;
        if (r13 != r8) goto L_0x0035;
    L_0x0086:
        r8 = r0 + -1;
        if (r8 < 0) goto L_0x0035;
    L_0x008a:
        r8 = r0 + -1;
        r8 = r1.get(r8);
        r8 = (com.blink.academy.onetake.bean.longvideo.LongVideosModel) r8;
        r8 = r8.getCurrentDuration();
        r10 = IMAGE_UNIT_WIDTH;
        r10 = (long) r10;
        r8 = r8 * r10;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r10;
        r3 = (int) r8;
        r7 = r7 - r3;
        goto L_0x0035;
    L_0x00a0:
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r9 + r7;
        if (r8 <= r9) goto L_0x00b6;
    L_0x00a7:
        r8 = r12.mImagesScrollX;
        r9 = r7 + r5;
        if (r8 >= r9) goto L_0x00b6;
    L_0x00ad:
        r8 = 1;
        if (r13 != r8) goto L_0x00b1;
    L_0x00b0:
        r7 = r7 + r5;
    L_0x00b1:
        r8 = r12.mImagesScrollX;
        r2 = r7 - r8;
        goto L_0x0039;
    L_0x00b6:
        r7 = r7 + r5;
        r0 = r0 + 1;
        goto L_0x000e;
    L_0x00bb:
        r8 = 2;
        if (r13 != r8) goto L_0x004d;
    L_0x00be:
        r8 = r0 + -1;
        if (r8 < 0) goto L_0x004d;
    L_0x00c2:
        r8 = r0 + -1;
        r8 = r1.get(r8);
        r8 = (com.blink.academy.onetake.bean.longvideo.LongVideosModel) r8;
        r8 = r8.getCurrentDuration();
        r10 = IMAGE_UNIT_WIDTH;
        r10 = (long) r10;
        r8 = r8 * r10;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r10;
        r3 = (int) r8;
        r7 = r7 - r3;
        goto L_0x004d;
    L_0x00d9:
        r8 = r12.mImagesScrollX;
        r9 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r9 = r7 - r9;
        if (r8 > r9) goto L_0x0051;
    L_0x00e1:
        r8 = 1;
        if (r13 != r8) goto L_0x00ea;
    L_0x00e4:
        r8 = r12.mImagesScrollX;
        r2 = r7 - r8;
        goto L_0x0051;
    L_0x00ea:
        r8 = 2;
        if (r13 != r8) goto L_0x00e4;
    L_0x00ed:
        r8 = r0 + -1;
        if (r8 < 0) goto L_0x00e4;
    L_0x00f1:
        r8 = r0 + -1;
        r8 = r1.get(r8);
        r8 = (com.blink.academy.onetake.bean.longvideo.LongVideosModel) r8;
        r8 = r8.getCurrentDuration();
        r10 = IMAGE_UNIT_WIDTH;
        r10 = (long) r10;
        r8 = r8 * r10;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r10;
        r3 = (int) r8;
        r7 = r7 - r3;
        goto L_0x00e4;
    L_0x0107:
        r8 = r12.screenWidth;
        r8 = -r8;
        if (r2 >= r8) goto L_0x0120;
    L_0x010c:
        r8 = r12.mImagesRecyclerView;
        r9 = r12.screenWidth;
        r9 = r9 + r2;
        r10 = 0;
        r8.scrollBy(r9, r10);
        r8 = r12.mImagesRecyclerView;
        r9 = r12.screenWidth;
        r9 = -r9;
        r10 = 0;
        r8.smoothScrollBy(r9, r10);
        goto L_0x006b;
    L_0x0120:
        r8 = r12.mImagesRecyclerView;
        r9 = 0;
        r8.smoothScrollBy(r2, r9);
        goto L_0x006b;
    L_0x0128:
        r8 = 2;
        if (r13 != r8) goto L_0x0071;
    L_0x012b:
        r8 = com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
        r4 = r4 - r8;
        goto L_0x0071;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.ui.helper.VideoEditHelper.partVideosCurrentDuration(int):void");
    }

    public void refreshSlideTouchView() {
        refreshVideoVolumeTouchView();
        refreshAudioVolumeTouchView();
    }

    private void callAudioChange() {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.callAudioChange();
        }
    }

    private void callVideoChange(long needPlayTimeUs) {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.callVideoChange(needPlayTimeUs);
        }
    }

    private void callVideoChangeAfterSplit(long needPlayTimeUs) {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.callVideoChangeAfterSplit(needPlayTimeUs);
        }
    }

    private void callVideoChangeAfterSlide(long needPlayTimeUs, LongVideosModel changedModel) {
        if (this.mChangeOtherCallback != null) {
            this.mChangeOtherCallback.callVideoChangeAfterSlide(needPlayTimeUs, changedModel);
        }
    }

    public static float add(float a, float b) {
        return add(BigDecimal.valueOf((double) a), BigDecimal.valueOf((double) b));
    }

    public static float add(BigDecimal a, BigDecimal b) {
        return a.add(b).setScale(1, 4).floatValue();
    }

    public static float sub(float a, float b) {
        return sub(BigDecimal.valueOf((double) a), BigDecimal.valueOf((double) b));
    }

    public static float sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b).setScale(1, 4).floatValue();
    }

    public static float mul(float a, float b) {
        return mul(BigDecimal.valueOf((double) a), BigDecimal.valueOf((double) b));
    }

    public static float mul(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(1, 4).floatValue();
    }

    public static float div(float a, float b) {
        return div(BigDecimal.valueOf((double) a), BigDecimal.valueOf((double) b));
    }

    public static float div(BigDecimal a, BigDecimal b) {
        return a.divide(b, 4).setScale(1, 4).floatValue();
    }

    private static float divDOWN(float a, float b) {
        return divDOWN(BigDecimal.valueOf((double) a), BigDecimal.valueOf((double) b));
    }

    private static float divDOWN(BigDecimal a, BigDecimal b) {
        return a.divide(b, 4).setScale(1, 5).floatValue();
    }
}