package cn.nineton.onetake.widget;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.listener.ValueAnimatorCallback;
import cn.nineton.onetake.util.AnimationUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.ViewUtil;
import cn.nineton.onetake.util.WaveformCacheUtils;
import cn.nineton.onetake.util.WeakHandler;

//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.custom.CircleCoverView;
//import com.blink.academy.onetake.custom.WeakHandler;
//import com.blink.academy.onetake.support.LongPressTouchListener;
//import com.blink.academy.onetake.support.LongPressTouchListener.CustomPressCallback;
//import com.blink.academy.onetake.support.callbacks.ValueAnimatorCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager.GetBitmapCallback;
//import com.blink.academy.onetake.support.utils.AnimationUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.ViewUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
import java.util.List;

public class VideoEditImageHolder extends VideoEditViewHolder implements Callback {
    public static int CIRCLE_COVER_RADIO = DensityUtil.dip2px(5.0f);
    public static int LEFT_OR_RIGHT_SPACE = DensityUtil.dip2px(3.0f);
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_LEFT = 3;
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_RIGHT = 4;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_LEFT = 1;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_RIGHT = 2;
    private static final String TAG = VideoEditImageHolder.class.getSimpleName();
    private int DELAY_TIME = 50;
    private int IMAGE_UNIT_WIDTH = VideoEditHelper.IMAGE_UNIT_WIDTH;
    private float SLIDER_DELTA = (((float) this.IMAGE_UNIT_WIDTH) * VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue());
    private long lastDealTime;
    private WeakHandler mHandler;
    private float mLastDownMoveX;
    private LongPressTouchListener.CustomPressCallback mLeftImagePressTouchListener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            mHelper.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            ViewParent itemViewParent = itemView.getParent();
            if (itemViewParent != null) {
                itemViewParent.requestDisallowInterceptTouchEvent(true);
                mHelper.onStartSlideVideoLeft(getAdapterPosition() + 1);
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            mLastDownMoveX = (float) (mScreenWidth / 2);
            mHandler.removeMessages(3);
            mHandler.removeMessages(4);
            ViewParent parent = itemView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
                mHelper.onSlideVideoLeftEnd(getAdapterPosition() + 1);
                mHelper.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            mLastDownMoveX = event.getRawX();
            if (isInStartArea()) {
                mHandler.sendEmptyMessageDelayed(4, (long) DELAY_TIME);
            } else if (isInEndArea()) {
                mHandler.sendEmptyMessageDelayed(3, (long) DELAY_TIME);
            } else {
                mHandler.removeMessages(3);
                mHandler.removeMessages(4);
                if (moveTime != 0) {
                    mHelper.onSlideVideoLeft(getAdapterPosition() + 1, moveTime);
                }
            }
        }
    };
    private LongPressTouchListener.CustomPressCallback mRightPressTouchlistener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            mHelper.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            if (itemView.getParent() != null) {
                itemView.getParent().requestDisallowInterceptTouchEvent(true);
                mHelper.onStartSlideVideoRight(getAdapterPosition() - 1);
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            mLastDownMoveX = (float) (mScreenWidth / 2);
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
            if (itemView.getParent() != null) {
                itemView.getParent().requestDisallowInterceptTouchEvent(false);
                mHelper.onSlideVideoRightEnd(getAdapterPosition() - 1);
                mHelper.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            mLastDownMoveX = event.getRawX();
            if (isInStartArea()) {
                mHandler.sendEmptyMessageDelayed(2, (long) DELAY_TIME);
            } else if (isInEndArea()) {
                mHandler.sendEmptyMessageDelayed(1, (long) DELAY_TIME);
            } else {
                mHandler.removeMessages(1);
                mHandler.removeMessages(2);
                if (moveTime != 0) {
                    mHelper.onSlideVideoRight(getAdapterPosition() - 1, moveTime);
                }
            }
        }
    };
    private int mScreenWidth;
    @BindView(R.id.video_edit_black_cover)
    View video_edit_black_cover;
    @BindView(R.id.video_edit_bottom_line)
    View video_edit_bottom_line;
    @BindView(R.id.video_edit_bottom_line_bottom)
    View video_edit_bottom_line_bottom;
    @BindView(R.id.video_edit_effect_iv)
    ImageView video_edit_effect_iv;
    @BindView(R.id.video_edit_image_ccv)
    CircleCoverView video_edit_image_ccv;
    @BindView(R.id.video_edit_image_iv)
    ImageView video_edit_image_iv;
    @BindView(R.id.video_edit_image_iv1)
    ImageView video_edit_image_iv1;
    @BindView(R.id.video_edit_image_iv2)
    ImageView video_edit_image_iv2;
    @BindView(R.id.video_edit_image_parent_fl)
    FrameLayout video_edit_image_parent_fl;
    @BindView(R.id.video_edit_image_volume_line)
    View video_edit_image_volume_line;
    @BindView(R.id.video_edit_inner_cover)
    CircleCoverView video_edit_inner_cover;
    @BindView(R.id.video_edit_left_iv_slide_view)
    View video_edit_left_iv_slide_view;
    @BindView(R.id.video_edit_mute_tv)
    TextView video_edit_mute_tv;
    @BindView(R.id.video_edit_parent)
    FrameLayout video_edit_parent;
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
    @BindView(R.id.video_edit_wave_view)
    AudioWaveView video_edit_wave_view;
    @BindView(R.id.video_edit_wave_view1)
    AudioWaveView video_edit_wave_view1;
    @BindView(R.id.video_edit_wave_view2)
    AudioWaveView video_edit_wave_view2;

    public VideoEditImageHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.video_edit_image_parent_fl.setClipChildren(true);
        this.mScreenWidth = DensityUtil.getMetricsWidth(context);
        this.video_edit_right_iv_slide_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.video_edit_right_iv_slide_view.setOnTouchListener(new LongPressTouchListener(getActivity(), this.SLIDER_DELTA, this.mRightPressTouchlistener));
        this.video_edit_left_iv_slide_view.setOnTouchListener(new LongPressTouchListener(getActivity(), this.SLIDER_DELTA, this.mLeftImagePressTouchListener));
        this.video_edit_left_iv_slide_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.video_edit_image_ccv.setNeedDrawOutLine(true);
        this.mHandler = new WeakHandler((Callback) this);
    }

    public boolean handleMessage(Message msg) {
        long currentTimeMillis;
        switch (msg.what) {
            case 1:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideVideoRightAutoScrollToLeft(getAdapterPosition() - 1);
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(1, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 2:
                if (isInStartArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideVideoRightAutoScrollToRight(getAdapterPosition() - 1);
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(2, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 3:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideVideoLeftAutoScrollToLeft(getAdapterPosition() + 1);
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(3, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 4:
                if (isInStartArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideVideoLeftAutoScrollToRight(getAdapterPosition() + 1);
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(4, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private boolean isInStartArea() {
        return this.mLastDownMoveX < this.SLIDER_DELTA * ((float) VideoEditHelper.EXPANSION_OF_AUTO_SCROLL);
    }

    private boolean isInEndArea() {
        return this.mLastDownMoveX > ((float) this.mScreenWidth) - (this.SLIDER_DELTA * ((float) VideoEditHelper.EXPANSION_OF_AUTO_SCROLL));
    }

    public void onBindViewHolder(int position, List<Object> payloads) {
        super.onBindViewHolder(position, (List) payloads);
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
    }

    public void onBindViewHolder(int position) {
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        int white_area_type = videoEditImageEntity.getWhite_area_type();
        boolean musicEdit = this.mHelper.isMusicEdit();
        LongVideosModel longVideosModel = videoEditImageEntity.getLongVideosModel();
        float videoVolume = longVideosModel.getVideoVolume();
        if (this.mHelper.isAllVideoMute()) {
            videoVolume = StaticLayoutUtil.DefaultSpacingadd;
        }
        if (musicEdit) {
            refreshWhiteAreaView(0);
            if (white_area_type == 1) {
                this.video_edit_image_ccv.setNeedDrawOutLineWidthAndColor((float) DensityUtil.dip2px(1.0f), ContextCompat.getColor(getActivity(), R.color.colorWhite));
            } else {
                this.video_edit_image_ccv.setNeedDrawOutLineWidthAndColor(1.0f, ContextCompat.getColor(getActivity(), R.color.color30White));
            }
            refreshVideoVolumeLineView(videoVolume);
        } else {
            refreshWhiteAreaView(white_area_type);
            this.video_edit_image_ccv.setNeedDrawOutLineWidthAndColor(1.0f, ContextCompat.getColor(getActivity(), R.color.color30White));
            this.video_edit_image_volume_line.setVisibility(View.GONE);
        }
        refreshAudioWaveDrawHeight(videoVolume);
        float showStart = videoEditImageEntity.getShowStart();
        float showEnd = videoEditImageEntity.getShowEnd();
        refreshImageShowStatus(showStart, showEnd);
        int videoRotate = longVideosModel.getVideoRotate();
        LogUtil.d(TAG, String.format("onBindViewHolder position : %s , videoRotate : %s ", getAdapterPosition(), videoRotate));
        if (longVideosModel.mediaType == 0) {
            showVideoWaveView(showStart, showEnd, longVideosModel.getOriginalMediaPath(), longVideosModel, videoEditImageEntity.getImageTime() * 1000);
        } else {
            hideVideoWaveView();
        }
        int circleCoverType = videoEditImageEntity.getCircleCoverType();
        if (white_area_type == 1) {
            refreshBlackCoverAndWaveAlpha(true, videoVolume);
            if (musicEdit) {
                refreshCircleCoverViewStatus(circleCoverType);
            } else {
                refreshCircleCoverViewStatus(0);
            }
            refreshInnerCircleCoverViewStatus(videoEditImageEntity.getInnerCircleCoverType());
        } else {
            refreshBlackCoverAndWaveAlpha(false, videoVolume);
            refreshInnerCircleCoverViewStatus(0);
            refreshCircleCoverViewStatus(circleCoverType);
        }
        if (this.mHelper.isMusicEdit()) {
            refreshInnerCircleCoverViewStatus(0);
        }
        refreshSlideArrowView(videoEditImageEntity.isHideLeftArrowImage(), videoEditImageEntity.isHideRightArrowImage());
        boolean showMuteTv = videoEditImageEntity.isShowMuteTv();
        int muteTvLeftMargin = videoEditImageEntity.getMuteTvLeftMargin();
        LogUtil.d(TAG, String.format("showMuteTv : %s , muteTvLeftMargin : %s ", showMuteTv, muteTvLeftMargin));
        if (!this.mHelper.isMusicEdit() || white_area_type == 1 || videoVolume != StaticLayoutUtil.DefaultSpacingadd || this.mHelper.isAllVideoMute()) {
            if (this.video_edit_mute_tv.getVisibility() == View.VISIBLE) {
                this.video_edit_mute_tv.setVisibility(View.GONE);
            }
        } else if (videoEditImageEntity.isShowMuteTv()) {
            ((LayoutParams) this.video_edit_mute_tv.getLayoutParams()).leftMargin = videoEditImageEntity.getMuteTvLeftMargin();
            this.video_edit_mute_tv.setVisibility(View.VISIBLE);
        } else if (this.video_edit_mute_tv.getVisibility() == View.VISIBLE) {
            this.video_edit_mute_tv.setVisibility(View.GONE);
        }
        bindImageViewBitmap(showStart, showEnd, position, videoEditImageEntity, longVideosModel);
        if (!videoEditImageEntity.isShowEffectImage() || this.mHelper.isMusicEdit()) {
            this.video_edit_effect_iv.setVisibility(View.GONE);
            return;
        }
        this.video_edit_effect_iv.setVisibility(View.VISIBLE);
        if (getItemViewWidth() < DensityUtil.dip2px(25.0f)) {
            this.video_edit_effect_iv.setVisibility(View.GONE);
        }
    }

    private void refreshAudioWaveDrawHeight(float volume) {
        this.video_edit_wave_view.compressYLength(volume);
        this.video_edit_wave_view1.compressYLength(volume);
        this.video_edit_wave_view2.compressYLength(volume);
    }

    public void refreshVideoVolumeLineView(float volume) {
        if (volume == 1.0f || volume == StaticLayoutUtil.DefaultSpacingadd) {
            this.video_edit_image_volume_line.setVisibility(View.GONE);
        } else {
            LayoutParams layoutParams = (LayoutParams) this.video_edit_image_volume_line.getLayoutParams();
            layoutParams.topMargin = (int) (((((float) DensityUtil.dip2px(28.0f)) * 1.0f) * (1.0f - volume)) - ((((float) DensityUtil.dip2px(1.0f)) * 1.0f) / 2.0f));
            this.video_edit_image_volume_line.setLayoutParams(layoutParams);
            this.video_edit_image_volume_line.setVisibility(View.VISIBLE);
        }
        refreshAudioWaveDrawHeight(volume);
        refreshBlackCoverAlphaWhenSlide(volume);
    }

    private void refreshBlackCoverAlphaWhenSlide(float volume) {
        if (volume == StaticLayoutUtil.DefaultSpacingadd) {
            this.video_edit_black_cover.setAlpha(0.6f);
        } else {
            this.video_edit_black_cover.setAlpha(0.3f);
        }
    }

    private void refreshBlackCoverAndWaveAlpha(boolean isSelected, float videoVolume) {
        if (this.mHelper.isMusicEdit()) {
            if (this.mHelper.isAllVideoMute()) {
                this.video_edit_black_cover.setAlpha(0.6f);
            } else if (isSelected) {
                if (videoVolume != StaticLayoutUtil.DefaultSpacingadd) {
                    this.video_edit_black_cover.setAlpha(0.3f);
                } else {
                    this.video_edit_black_cover.setAlpha(0.6f);
                }
            } else if (videoVolume != StaticLayoutUtil.DefaultSpacingadd) {
                this.video_edit_black_cover.setAlpha(0.15f);
            } else {
                this.video_edit_black_cover.setAlpha(0.45f);
            }
        } else if (isSelected) {
            this.video_edit_black_cover.setAlpha(0.3f);
        } else {
            this.video_edit_black_cover.setAlpha(0.15f);
        }
        if (isSelected) {
            this.video_edit_wave_view1.setAlpha(0.8f);
            this.video_edit_wave_view.setAlpha(0.8f);
            this.video_edit_wave_view2.setAlpha(0.8f);
            return;
        }
        this.video_edit_wave_view1.setAlpha(0.6f);
        this.video_edit_wave_view.setAlpha(0.6f);
        this.video_edit_wave_view2.setAlpha(0.6f);
    }

    private void loadBitmapForImageView(ImageView imageView, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel, long imageTime, int position, boolean needShowAnim) {
        if (imageView != null && videoEditImageEntity != null && longVideosModel != null) {
            final int videoRotate = longVideosModel.getVideoRotate();
            imageView.setImageBitmap(null);
            imageView.setTag(R.id.image_tag_1, longVideosModel.getOriginalMediaPath());
            imageView.setTag(R.id.image_tag_2, imageTime);
            final ImageView imageView2 = imageView;
            final VideoEditImageEntity videoEditImageEntity2 = videoEditImageEntity;
            final boolean z = needShowAnim;
            this.mHelper.loadBitmap(imageView, longVideosModel, imageTime, (filePath, bitmapTime, bitmap) -> dealGetBitmapFinished(filePath, bitmapTime, bitmap, imageView2, videoEditImageEntity2, z, videoRotate));
        }
    }

    private void dealGetBitmapFinished(String filePath, long bitmapTime, Bitmap bitmap, ImageView imageView, VideoEditImageEntity videoEditImageEntity, boolean needShowAnim, int videoRotate) {
        final ImageView imageView2 = imageView;
        final String str = filePath;
        final long j = bitmapTime;
        final boolean z = needShowAnim;
        final Bitmap bitmap2 = bitmap;
        final int i = videoRotate;
        //access$200(new Runnable() {
        App.runOnUiThread(new Runnable() {
            public void run() {
                Long tag1 = (Long) imageView2.getTag(R.id.image_tag_2);
                if (!TextUtils.equals((String) imageView2.getTag(R.id.image_tag_1), str) || tag1.longValue() != j) {
                    return;
                }
                if (z) {
                    AnimationUtil.setValueAnimator(true, new ValueAnimatorCallback() {
                        public void updateCallback(float value) {
                            imageView2.setAlpha(value);
                        }

                        public void startCallback() {
                            imageView2.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
                            if (bitmap2 != null && !bitmap2.isRecycled()) {
                                video_edit_image_iv.setImageBitmap(bitmap2);
                                int needRotate = i;
                                if (needRotate == 90) {
                                    needRotate = BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
                                } else if (needRotate == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                                    needRotate = 90;
                                }
                                video_edit_image_iv.setRotation((float) needRotate);
                            }
                        }

                        public void endCallback() {
                            imageView2.setAlpha(1.0f);
                        }
                    });
                } else if (bitmap2 != null && !bitmap2.isRecycled()) {
                    imageView2.setImageBitmap(bitmap2);
                    int needRotate = i;
                    if (needRotate == 90) {
                        needRotate = BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
                    } else if (needRotate == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                        needRotate = 90;
                    }
                    video_edit_image_iv.setRotation((float) needRotate);
                }
            }
        });
    }
    /* JADX WARNING: Removed duplicated region for block: B:14:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00ce  */
//    private void bindImageViewBitmap(float r33, float r34, int r35, VideoEditImageEntity r36, LongVideosModel r37) {
//        /*
//        r32 = this;
//        r2 = r36.getImageTime();
//        r2 = (float) r2;
//        r3 = r37.getSpeed();
//        r2 = r2 * r3;
//        r6 = (long) r2;
//        r0 = r32;
//        r2 = r0.video_edit_image_iv;
//        r3 = 2131689494; // 0x7f0f0016 float:1.9008005E38 double:1.0531945466E-314;
//        r26 = r2.getTag(r3);
//        r26 = (java.lang.String) r26;
//        r0 = r32;
//        r2 = r0.video_edit_image_iv;
//        r3 = 2131689495; // 0x7f0f0017 float:1.9008007E38 double:1.053194547E-314;
//        r27 = r2.getTag(r3);
//        r27 = (java.lang.Long) r27;
//        r9 = r36.isNeedImageShowAnim();
//        if (r9 == 0) goto L_0x0031;
//    L_0x002b:
//        r2 = 0;
//        r0 = r36;
//        r0.setNeedImageShowAnim(r2);
//    L_0x0031:
//        if (r9 != 0) goto L_0x0160;
//    L_0x0033:
//        r2 = r37.getOriginalMediaPath();
//        r0 = r26;
//        r2 = android.text.TextUtils.equals(r0, r2);
//        if (r2 == 0) goto L_0x0160;
//    L_0x003f:
//        if (r27 == 0) goto L_0x0160;
//    L_0x0041:
//        r2 = r27.longValue();
//        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
//        if (r2 != 0) goto L_0x0160;
//    L_0x0049:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv;
//        r2 = r2.getDrawable();
//        if (r2 == 0) goto L_0x014f;
//    L_0x0053:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv;
//        r2.postInvalidate();
//    L_0x005a:
//        r2 = 0;
//        r2 = (r33 > r2 ? 1 : (r33 == r2 ? 0 : -1));
//        if (r2 >= 0) goto L_0x00c8;
//    L_0x005f:
//        r24 = r36.isLeftNeedShowAnim();
//        r0 = r32;
//        r2 = r0.video_edit_image_iv1;
//        r3 = 2131689494; // 0x7f0f0016 float:1.9008005E38 double:1.0531945466E-314;
//        r30 = r2.getTag(r3);
//        r30 = (java.lang.String) r30;
//        r0 = r32;
//        r2 = r0.video_edit_image_iv1;
//        r3 = 2131689495; // 0x7f0f0017 float:1.9008007E38 double:1.053194547E-314;
//        r28 = r2.getTag(r3);
//        r28 = (java.lang.Long) r28;
//        if (r24 == 0) goto L_0x0085;
//    L_0x007f:
//        r2 = 0;
//        r0 = r36;
//        r0.setLeftNeedShowAnim(r2);
//    L_0x0085:
//        r2 = (float) r6;
//        r3 = r37.getSpeed();
//        r4 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
//        r3 = r3 * r4;
//        r2 = r2 - r3;
//        r14 = (long) r2;
//        r2 = 0;
//        r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1));
//        if (r2 >= 0) goto L_0x0097;
//    L_0x0095:
//        r14 = 0;
//    L_0x0097:
//        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//        r2 = r14 / r2;
//        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//        r14 = r2 * r4;
//        if (r24 != 0) goto L_0x0184;
//    L_0x00a1:
//        r2 = r37.getOriginalMediaPath();
//        r0 = r30;
//        r2 = android.text.TextUtils.equals(r0, r2);
//        if (r2 == 0) goto L_0x0184;
//    L_0x00ad:
//        if (r28 == 0) goto L_0x0184;
//    L_0x00af:
//        r2 = r28.longValue();
//        r2 = (r2 > r14 ? 1 : (r2 == r14 ? 0 : -1));
//        if (r2 != 0) goto L_0x0184;
//    L_0x00b7:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv1;
//        r2 = r2.getDrawable();
//        if (r2 == 0) goto L_0x0171;
//    L_0x00c1:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv1;
//        r2.postInvalidate();
//    L_0x00c8:
//        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
//        r2 = (r34 > r2 ? 1 : (r34 == r2 ? 0 : -1));
//        if (r2 <= 0) goto L_0x013d;
//    L_0x00ce:
//        r25 = r36.isRightNeedShowAnim();
//        r0 = r32;
//        r2 = r0.video_edit_image_iv2;
//        r3 = 2131689494; // 0x7f0f0016 float:1.9008005E38 double:1.0531945466E-314;
//        r31 = r2.getTag(r3);
//        r31 = (java.lang.String) r31;
//        r0 = r32;
//        r2 = r0.video_edit_image_iv2;
//        r3 = 2131689495; // 0x7f0f0017 float:1.9008007E38 double:1.053194547E-314;
//        r29 = r2.getTag(r3);
//        r29 = (java.lang.Long) r29;
//        if (r25 == 0) goto L_0x00f4;
//    L_0x00ee:
//        r2 = 0;
//        r0 = r36;
//        r0.setRightNeedShowAnim(r2);
//    L_0x00f4:
//        r2 = (float) r6;
//        r3 = r37.getSpeed();
//        r4 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
//        r3 = r3 * r4;
//        r2 = r2 + r3;
//        r0 = (long) r2;
//        r20 = r0;
//        r2 = r37.getTotalDurationValue();
//        r2 = (r20 > r2 ? 1 : (r20 == r2 ? 0 : -1));
//        if (r2 <= 0) goto L_0x010c;
//    L_0x0108:
//        r20 = r37.getTotalDurationValue();
//    L_0x010c:
//        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//        r2 = r20 / r2;
//        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//        r20 = r2 * r4;
//        if (r25 != 0) goto L_0x01ab;
//    L_0x0116:
//        r2 = r37.getOriginalMediaPath();
//        r0 = r31;
//        r2 = android.text.TextUtils.equals(r0, r2);
//        if (r2 == 0) goto L_0x01ab;
//    L_0x0122:
//        if (r29 == 0) goto L_0x01ab;
//    L_0x0124:
//        r2 = r29.longValue();
//        r2 = (r2 > r20 ? 1 : (r2 == r20 ? 0 : -1));
//        if (r2 != 0) goto L_0x01ab;
//    L_0x012c:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv2;
//        r2 = r2.getDrawable();
//        if (r2 == 0) goto L_0x0197;
//    L_0x0136:
//        r0 = r32;
//        r2 = r0.video_edit_image_iv2;
//        r2.postInvalidate();
//    L_0x013d:
//        r0 = r32;
//        r2 = r0.mHelper;
//        r2 = r2.getAllDatas();
//        r0 = r32;
//        r3 = r0.mHelper;
//        r0 = r35;
//        com.blink.academy.onetake.fresco.PreDownloadUtil.preLoadVideoEditImage(r0, r2, r3);
//        return;
//    L_0x014f:
//        r0 = r32;
//        r3 = r0.video_edit_image_iv;
//        r2 = r32;
//        r4 = r36;
//        r5 = r37;
//        r8 = r35;
//        r2.loadBitmapForImageView(r3, r4, r5, r6, r8, r9);
//        goto L_0x005a;
//    L_0x0160:
//        r0 = r32;
//        r3 = r0.video_edit_image_iv;
//        r2 = r32;
//        r4 = r36;
//        r5 = r37;
//        r8 = r35;
//        r2.loadBitmapForImageView(r3, r4, r5, r6, r8, r9);
//        goto L_0x005a;
//    L_0x0171:
//        r0 = r32;
//        r11 = r0.video_edit_image_iv1;
//        r17 = 0;
//        r10 = r32;
//        r12 = r36;
//        r13 = r37;
//        r16 = r35;
//        r10.loadBitmapForImageView(r11, r12, r13, r14, r16, r17);
//        goto L_0x00c8;
//    L_0x0184:
//        r0 = r32;
//        r11 = r0.video_edit_image_iv1;
//        r17 = 0;
//        r10 = r32;
//        r12 = r36;
//        r13 = r37;
//        r16 = r35;
//        r10.loadBitmapForImageView(r11, r12, r13, r14, r16, r17);
//        goto L_0x00c8;
//    L_0x0197:
//        r0 = r32;
//        r0 = r0.video_edit_image_iv2;
//        r17 = r0;
//        r23 = 0;
//        r16 = r32;
//        r18 = r36;
//        r19 = r37;
//        r22 = r35;
//        r16.loadBitmapForImageView(r17, r18, r19, r20, r22, r23);
//        goto L_0x013d;
//    L_0x01ab:
//        r0 = r32;
//        r0 = r0.video_edit_image_iv2;
//        r17 = r0;
//        r23 = 0;
//        r16 = r32;
//        r18 = r36;
//        r19 = r37;
//        r22 = r35;
//        r16.loadBitmapForImageView(r17, r18, r19, r20, r22, r23);
//        goto L_0x013d;
//        */
//        //throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditImageHolder.bindImageViewBitmap(float, float, int, com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity, com.blink.academy.onetake.bean.longvideo.LongVideosModel):void");
//    }
    private void bindImageViewBitmap(float f, float f2, int i, VideoEditImageEntity videoEditImageEntity, LongVideosModel longVideosModel) {
        boolean isLeftNeedShowAnim;
        long speed;
        long imageTime = (long) (((float) videoEditImageEntity.getImageTime()) * longVideosModel.getSpeed());
        String str = (String) this.video_edit_image_iv.getTag(R.id.image_tag_1);
        Long l = (Long) this.video_edit_image_iv.getTag(R.id.image_tag_2);
        boolean isNeedImageShowAnim = videoEditImageEntity.isNeedImageShowAnim();
        if (isNeedImageShowAnim) {
            videoEditImageEntity.setNeedImageShowAnim(false);
        }
        if (isNeedImageShowAnim || !TextUtils.equals(str, longVideosModel.getOriginalMediaPath()) || l == null || l.longValue() != imageTime) {
            loadBitmapForImageView(this.video_edit_image_iv, videoEditImageEntity, longVideosModel, imageTime, i, isNeedImageShowAnim);
        } else if (this.video_edit_image_iv.getDrawable() != null) {
            this.video_edit_image_iv.postInvalidate();
        } else {
            loadBitmapForImageView(this.video_edit_image_iv, videoEditImageEntity, longVideosModel, imageTime, i, isNeedImageShowAnim);
        }
        if (f < StaticLayoutUtil.DefaultSpacingadd) {
            isLeftNeedShowAnim = videoEditImageEntity.isLeftNeedShowAnim();
            str = (String) this.video_edit_image_iv1.getTag(R.id.image_tag_1);
            l = (Long) this.video_edit_image_iv1.getTag(R.id.image_tag_2);
            if (isLeftNeedShowAnim) {
                videoEditImageEntity.setLeftNeedShowAnim(false);
            }
            speed = (long) (((float) imageTime) - (longVideosModel.getSpeed() * 1000.0f));
            if (speed < 0) {
                speed = 0;
            }
            long j = (speed / 1000) * 1000;
            if (isLeftNeedShowAnim || !TextUtils.equals(str, longVideosModel.getOriginalMediaPath()) || l == null || l.longValue() != j) {
                loadBitmapForImageView(this.video_edit_image_iv1, videoEditImageEntity, longVideosModel, j, i, false);
            } else if (this.video_edit_image_iv1.getDrawable() != null) {
                this.video_edit_image_iv1.postInvalidate();
            } else {
                loadBitmapForImageView(this.video_edit_image_iv1, videoEditImageEntity, longVideosModel, j, i, false);
            }
        }
        if (f2 > 1.0f) {
            isLeftNeedShowAnim = videoEditImageEntity.isRightNeedShowAnim();
            str = (String) this.video_edit_image_iv2.getTag(R.id.image_tag_1);
            l = (Long) this.video_edit_image_iv2.getTag(R.id.image_tag_2);
            if (isLeftNeedShowAnim) {
                videoEditImageEntity.setRightNeedShowAnim(false);
            }
            speed = (long) (((float) imageTime) + (longVideosModel.getSpeed() * 1000.0f));
            if (speed > longVideosModel.getTotalDurationValue()) {
                speed = longVideosModel.getTotalDurationValue();
            }
            imageTime = 1000 * (speed / 1000);
            if (isLeftNeedShowAnim || !TextUtils.equals(str, longVideosModel.getOriginalMediaPath()) || l == null || l.longValue() != imageTime) {
                loadBitmapForImageView(this.video_edit_image_iv2, videoEditImageEntity, longVideosModel, imageTime, i, false);
            } else if (this.video_edit_image_iv2.getDrawable() != null) {
                this.video_edit_image_iv2.postInvalidate();
            } else {
                loadBitmapForImageView(this.video_edit_image_iv2, videoEditImageEntity, longVideosModel, imageTime, i, false);
            }
        }
        //PreDownloadUtil.preLoadVideoEditImage(i, this.mHelper.getAllDatas(), this.mHelper);
        preLoadVideoEditImage(i,mHelper.getAllDatas(),mHelper);
    }


    public static void preLoadVideoEditImage(int i, List<VideoEditImageEntity> list, VideoEditHolderHelper<VideoEditImageEntity> videoEditHolderHelper) {
        int i2 = -1;
        if (list != null) {
            VideoEditImageEntity videoEditImageEntity;
            int size = list.size();
            int i3 = i + 1;
            int i4 = i - 1;
            if (i3 < size) {
                int i5 = i3 + 3;
                if (i5 > size) {
                    i5 = size;
                    size = i3;
                } else {
                    size = i3;
                }
                while (size < i5) {
                    videoEditImageEntity = list.get(size);
                    if (videoEditImageEntity != null && videoEditImageEntity.getViewType() == 274) {
                        videoEditHolderHelper.preloadBitmap(videoEditImageEntity.getLongVideosModel(), videoEditImageEntity.getImageTime(), null);
                    }
                    size++;
                }
            }
            if (i4 >= 0) {
                i3 = i4 - 3;
                if (i3 >= -1) {
                    i2 = i3;
                }
                while (i4 > i2) {
                    videoEditImageEntity = list.get(i4);
                    if (videoEditImageEntity != null && videoEditImageEntity.getViewType() == 274) {
                        videoEditHolderHelper.preloadBitmap(videoEditImageEntity.getLongVideosModel(), videoEditImageEntity.getImageTime(), null);
                    }
                    i4--;
                }
            }
        }
    }




    private void refreshInnerCircleCoverViewStatus(int circleCoverType) {
        switch (circleCoverType) {
            case 0:
                this.video_edit_inner_cover.setVisibility(View.GONE);
                return;
            case 1:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, CIRCLE_COVER_RADIO);
                return;
            case 3:
                this.video_edit_inner_cover.setVisibility(View.VISIBLE);
                this.video_edit_inner_cover.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, CIRCLE_COVER_RADIO, CIRCLE_COVER_RADIO);
                return;
            default:
                return;
        }
    }

    private void refreshCircleCoverViewStatus(int circleCoverType) {
        switch (circleCoverType) {
            case 0:
                this.video_edit_image_ccv.setVisibility(View.VISIBLE);
                this.video_edit_image_ccv.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 0, 0);
                return;
            case 1:
                this.video_edit_image_ccv.setVisibility(View.VISIBLE);
                this.video_edit_image_ccv.setLeftRightSpace((float) LEFT_OR_RIGHT_SPACE, StaticLayoutUtil.DefaultSpacingadd, CIRCLE_COVER_RADIO, 0);
                return;
            case 2:
                this.video_edit_image_ccv.setVisibility(View.VISIBLE);
                this.video_edit_image_ccv.setLeftRightSpace(StaticLayoutUtil.DefaultSpacingadd, (float) LEFT_OR_RIGHT_SPACE, 0, CIRCLE_COVER_RADIO);
                return;
            case 3:
                this.video_edit_image_ccv.setVisibility(View.VISIBLE);
                this.video_edit_image_ccv.setLeftRightSpace((float) LEFT_OR_RIGHT_SPACE, (float) LEFT_OR_RIGHT_SPACE, CIRCLE_COVER_RADIO, CIRCLE_COVER_RADIO);
                return;
            default:
                return;
        }
    }

    private void refreshImageShowStatus(float start, float end) {
        refreshItemViewWidth(getItemViewWidth(start, end));
        refreshImageParent();
        refreshLeftImageShowStatus(start, end);
        refreshCenterImageShowStatus(start, end);
        refreshRightImageShowStatus(start, end);
    }

    private void refreshImageParent() {
        if (this.mHelper.isMusicEdit()) {
            ViewUtil.setViewHeight(this.video_edit_image_parent_fl, DensityUtil.dip2px(30.0f));
        } else {
            ViewUtil.setViewHeight(this.video_edit_image_parent_fl, DensityUtil.dip2px(44.0f));
        }
    }

    private int getItemViewWidth(float start, float end) {
        return (int) ((end - start) * ((float) VideoEditHelper.IMAGE_UNIT_WIDTH));
    }

    private int getImageViewLeftMargin(float start) {
        return (int) ((-start) * ((float) VideoEditHelper.IMAGE_UNIT_WIDTH));
    }

    private void refreshLeftImageShowStatus(float originStart, float originEnd) {
        if (originStart < StaticLayoutUtil.DefaultSpacingadd) {
            int imageViewLeftMargin = getImageViewLeftMargin(originStart + 1.0f);
            setImageViewMarginLeft(this.video_edit_image_iv1, imageViewLeftMargin);
            setWaveViewMarginLeft(this.video_edit_wave_view1, imageViewLeftMargin);
            this.video_edit_image_iv1.setVisibility(View.VISIBLE);
            this.video_edit_wave_view1.setVisibility(View.VISIBLE);
            return;
        }
        this.video_edit_image_iv1.setVisibility(View.GONE);
        this.video_edit_wave_view1.setVisibility(View.GONE);
    }

    private void refreshCenterImageShowStatus(float originStart, float originEnd) {
        int imageViewLeftMargin;
        if (originStart < StaticLayoutUtil.DefaultSpacingadd) {
            imageViewLeftMargin = -getImageViewLeftMargin(-originStart);
            setImageViewMarginLeft(this.video_edit_image_iv, imageViewLeftMargin);
            setWaveViewMarginLeft(this.video_edit_wave_view, imageViewLeftMargin);
            return;
        }
        imageViewLeftMargin = getImageViewLeftMargin(originStart);
        setImageViewMarginLeft(this.video_edit_image_iv, imageViewLeftMargin);
        setWaveViewMarginLeft(this.video_edit_wave_view, imageViewLeftMargin);
    }

    private void refreshRightImageShowStatus(float originStart, float originEnd) {
        if (originEnd > 1.0f) {
            int imageViewLeftMargin = -getImageViewLeftMargin(1.0f - originStart);
            setImageViewMarginLeft(this.video_edit_image_iv2, imageViewLeftMargin);
            setWaveViewMarginLeft(this.video_edit_wave_view2, imageViewLeftMargin);
            this.video_edit_image_iv2.setVisibility(View.VISIBLE);
            this.video_edit_wave_view2.setVisibility(View.VISIBLE);
            return;
        }
        this.video_edit_image_iv2.setVisibility(View.GONE);
        this.video_edit_wave_view2.setVisibility(View.GONE);
    }

    private void setWaveViewMarginLeft(AudioWaveView audioWaveView, int marginLeft) {
        LayoutParams layoutParams = (LayoutParams) audioWaveView.getLayoutParams();
        layoutParams.leftMargin = marginLeft;
        audioWaveView.setLayoutParams(layoutParams);
    }

    private void setImageViewMarginLeft(ImageView imageView, int marginLeft) {
        LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.leftMargin = marginLeft;
        imageView.setLayoutParams(layoutParams);
    }

    private void refreshItemViewWidth(int width) {
        ViewGroup.LayoutParams layoutParams = this.video_edit_parent.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -1);
        }
        layoutParams.width = width;
        layoutParams.height = -1;
        this.video_edit_parent.setLayoutParams(layoutParams);
    }

    private int getItemViewWidth() {
        ViewGroup.LayoutParams layoutParams = this.video_edit_parent.getLayoutParams();
        if (layoutParams != null) {
            return layoutParams.width;
        }
        return 0;
    }

    private void refreshWhiteAreaView(int white_area_type) {
        switch (white_area_type) {
            case 0:
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
                return;
            case 2:
                this.video_edit_top_line_bottom.setVisibility(View.GONE);
                this.video_edit_top_line.setVisibility(View.GONE);
                this.video_edit_bottom_line_bottom.setVisibility(View.GONE);
                this.video_edit_bottom_line.setVisibility(View.GONE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_right_iv.setVisibility(View.VISIBLE);
                this.video_edit_right_iv_slide_view.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_left_iv.setVisibility(View.GONE);
                this.video_edit_left_iv_slide_view.setVisibility(View.GONE);
                return;
            case 3:
                this.video_edit_top_line_bottom.setVisibility(View.GONE);
                this.video_edit_top_line.setVisibility(View.GONE);
                this.video_edit_bottom_line_bottom.setVisibility(View.GONE);
                this.video_edit_bottom_line.setVisibility(View.GONE);
                this.video_edit_slider_right_iv_bottom.setVisibility(View.GONE);
                this.video_edit_slider_right_iv.setVisibility(View.GONE);
                this.video_edit_right_iv_slide_view.setVisibility(View.GONE);
                this.video_edit_slider_left_iv_bottom.setVisibility(View.VISIBLE);
                this.video_edit_slider_left_iv.setVisibility(View.VISIBLE);
                this.video_edit_left_iv_slide_view.setVisibility(View.VISIBLE);
                return;
            default:
                return;
        }
    }

    private void refreshSlideArrowView(boolean needHideLeft, boolean needHideRight) {
        if (needHideLeft) {
            hideLeftSlideArrowView();
        } else {
            showLeftSlideArrowView();
        }
        if (needHideRight) {
            hideRightSlideArrowView();
        } else {
            showRightSlideArrowView();
        }
    }

    private void hideLeftSlideArrowView() {
        this.video_edit_slider_right_iv.setScaleX(-1.0f);
        this.video_edit_slider_right_iv.setBackgroundResource(R.drawable.round_color_white_left_button);
        int position = getAdapterPosition() - 1;
        List<VideoEditImageEntity> allDatas = this.mHelper.getAllDatas();
        if (allDatas != null) {
            int size = allDatas.size();
            if (position >= 0 && position < size) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) allDatas.get(position)).getLongVideosModel();
                if (longVideosModel == null) {
                    return;
                }
                if (longVideosModel.getCurrentDuration() == 500) {
                    this.video_edit_slider_right_iv.setImageBitmap(null);
                } else {
                    this.video_edit_slider_right_iv.setImageResource(R.drawable.icon_10_trim_video_right);
                }
            }
        }
    }

    private void showLeftSlideArrowView() {
        this.video_edit_slider_right_iv.setScaleX(1.0f);
        this.video_edit_slider_right_iv.setBackgroundResource(R.drawable.round_color_white_right_button);
    }

    private void hideRightSlideArrowView() {
        this.video_edit_slider_left_iv.setScaleX(1.0f);
        this.video_edit_slider_left_iv.setBackgroundResource(R.drawable.round_color_white_left_button);
        int position = getAdapterPosition() + 1;
        List<VideoEditImageEntity> allDatas = this.mHelper.getAllDatas();
        if (allDatas != null) {
            int size = allDatas.size();
            if (position >= 0 && position < size) {
                LongVideosModel longVideosModel = ((VideoEditImageEntity) allDatas.get(position)).getLongVideosModel();
                if (longVideosModel == null) {
                    return;
                }
                if (longVideosModel.getCurrentDuration() == 500) {
                    this.video_edit_slider_left_iv.setImageBitmap(null);
                } else {
                    this.video_edit_slider_left_iv.setImageResource(R.drawable.icon_10_trim_video_right);
                }
            }
        }
    }

    private void showRightSlideArrowView() {
        this.video_edit_slider_left_iv.setScaleX(-1.0f);
        this.video_edit_slider_left_iv.setBackgroundResource(R.drawable.round_color_white_right_button);
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
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
            audioWaveView.clearDrawCanvas();
            audioWaveView.setTag(2131689506, filePath);
            audioWaveView.setTag(2131689507, Long.valueOf(start));
            final AudioWaveView audioWaveView2 = audioWaveView;
            final long j = start;
            this.mHelper.getAudioWaveData(audioWaveView, filePath, start, needStart, duration, sumTimeUs, new WaveformCacheUtils.WaveDataCallback() {
                public void onLoadFinished(final String filePath, long startTimeUs, long durationUs, final float[] data) {
//                    access$200(new Runnable() {
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
//                    access$200(new Runnable() {
//                        public void run() {
//                            Long tag2 = (Long) audioWaveView2.getTag(2131689507);
//                            if (TextUtils.equals((String) audioWaveView2.getTag(2131689506), filePath) && tag2 != null && tag2.longValue() == j) {
//                                hideVideoWaveView();
//                            }
//                        }
//                    });
                    App.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Long tag2 = (Long) audioWaveView2.getTag(R.id.wave_tag_2);
                            if (TextUtils.equals((String) audioWaveView2.getTag(R.id.wave_tag_1), filePath) && tag2 != null && tag2.longValue() == j) {
                                hideVideoWaveView();
                            }
                        }
                    });
                }
            });
        }
    }

    private void showVideoWaveView(float showStart, float showEnd, String filePath, LongVideosModel model, long imageStartTimeUs) {
        float speed = model.getSpeed();
        long waveStartTime = (long) (((float) imageStartTimeUs) * speed);
        long waveDuration = (long) (1000000.0f * speed);
        long sumTime = model.getTotalDurationValue() * 1000;
        bindVideoWaveViewData(this.video_edit_wave_view, filePath, (waveStartTime / 1000000) * 1000000, waveStartTime, waveDuration, sumTime);
        if (showStart < StaticLayoutUtil.DefaultSpacingadd) {
            long beforeStart = (long) (((float) (imageStartTimeUs - 1000000)) * speed);
            bindVideoWaveViewData(this.video_edit_wave_view1, filePath, (beforeStart / 1000000) * 1000000, beforeStart, waveDuration, sumTime);
        }
        if (showEnd > 1.0f) {
            long nextStart = (long) (((float) (1000000 + imageStartTimeUs)) * speed);
            long start = (nextStart / 1000000) * 1000000;
            if (nextStart + waveDuration > model.getTotalDuration() * 1000) {
                waveDuration = (model.getTotalDuration() * 1000) - nextStart;
            }
            bindVideoWaveViewData(this.video_edit_wave_view2, filePath, start, nextStart, waveDuration, sumTime);
        }
    }

    private void hideVideoWaveView() {
        this.video_edit_wave_view1.setVisibility(View.GONE);
        this.video_edit_wave_view.setVisibility(View.GONE);
        this.video_edit_wave_view2.setVisibility(View.GONE);
    }
}