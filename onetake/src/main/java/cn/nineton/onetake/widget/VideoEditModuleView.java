package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.listener.VideoEditModuleListener;
import cn.nineton.onetake.listener.VideoEditModuleParentListener;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;

//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
//import com.blink.academy.onetake.widgets.ChangeChildDrawOrderView;
//import com.blink.academy.onetake.widgets.CornerLinearLayout;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class VideoEditModuleView extends RelativeLayout implements OnLongClickListener, OnClickListener {
    public static final int IMAGE_UNIT_WIDTH = DensityUtil.dip2px(44.0f);
    private static final String TAG = VideoEditModuleView.class.getSimpleName();
    private float SLIDER_DELTA;
    private HashMap<Float, ImageView> childViews;
    float down_end;
    float down_start;
    private float end;
    @BindView(R.id.filter_loop_gesture)
    View filter_loop_gesture;
    @BindView(R.id.filter_loop_preview_ll)
    LinearLayout filter_loop_preview_ll;
    @BindView(R.id.filter_loop_preview_parent_cll)
    CornerLinearLayout filter_loop_preview_parent_cll;
    @BindView(R.id.filter_loop_slider_left_iv)
    ImageView filter_loop_slider_left_iv;
    @BindView(R.id.filter_loop_slider_right_iv)
    ImageView filter_loop_slider_right_iv;
    @BindView(R.id.filter_loop_slider_root_rl)
    RelativeLayout filter_loop_slider_root_rl;
    @BindView(R.id.filter_loop_slider_root_rll)
    RelativeLayout filter_loop_slider_root_rll;
    private float lastMoveX;
    private float lastRightMoveX;
    private float last_end;
    private float last_start;
    @BindView(R.id.left_iv_slide_view)
    View left_iv_slide_view;
    private OnTouchListener mLeftImageOnTouchListener;
    private int mLeftReduceWidth;
    private LongVideosModel mLongVideosModel;
    private int mMaxChildCount;
    private int mMaxHeight;
    private int mMaxItemWidth;
    private int mMaxWidth;
    private int mMinWidth;
    private OnTouchListener mRightImageOnTouchListener;
    private int mRightReduceWidth;
    VideoEditModuleListener mVideoEditModuleListener;
    VideoEditModuleParentListener mVideoEditModuleParentListener;
    private boolean needMoveRightAnimate;
    private boolean needPaddingLeftAnimate;
    private boolean needPause;
    @BindView(R.id.player_loop_corner_ccv)
    View player_loop_corner_ccv;
    @BindView(R.id.right_iv_slide_view)
    View right_iv_slide_view;
    private float start;
    private float sumSize;
    @BindView(R.id.video_edit_audiowaveview)
    AudioWaveView video_edit_audiowaveview;

    public void setVideoSize(float sumSize, float start, float end) {
        this.sumSize = sumSize;
        this.start = start;
        this.end = end;
        if (start <= StaticLayoutUtil.DefaultSpacingadd) {
            hideLeftSlideArrowView();
        }
        if (end >= sumSize) {
            hideRightSlideArrowView();
        }
        LogUtil.d(TAG, String.format("setVideoSize   sumSize : %s , start : %s , end : %s ", new Object[]{Float.valueOf(sumSize), Float.valueOf(start), Float.valueOf(end)}));
    }

    public void checkStartOrEndWithFive() {
        float resultWidth;
        LogUtil.d(TAG, String.format("checkStartOrEndWithFive   sumSize : %s , start : %s , end : %s ", new Object[]{Float.valueOf(this.sumSize), Float.valueOf(this.start), Float.valueOf(this.end)}));
        if (isEndWidthPointFive(this.end)) {
            resultWidth = (float) ((this.filter_loop_preview_parent_cll.getWidth() + DensityUtil.dip2px(20.0f)) + ((int) (-this.SLIDER_DELTA)));
            updateViewWidthWhenTouchLeftSlideToLeft((int) resultWidth);
            this.mRightReduceWidth = (int) ((((float) (this.mMaxWidth - this.mLeftReduceWidth)) - resultWidth) - ((float) getCantSlideWidth()));
            //LogUtil.d(TAG, String.format("checkStartOrEndWithFive   end   down_width : %s  ,  move_x : %s  ,  resultWidth : %s  ,  mRightReduceWidth : %s ", new Object[]{Integer.valueOf(down_width), Integer.valueOf(move_x), Float.valueOf(resultWidth), Integer.valueOf(this.mRightReduceWidth)}));
        }
        if (isEndWidthPointFive(this.start)) {
            resultWidth = ((float) (this.filter_loop_preview_parent_cll.getWidth() + DensityUtil.dip2px(20.0f))) - this.SLIDER_DELTA;
            ((LayoutParams) this.filter_loop_preview_ll.getLayoutParams()).leftMargin = (int) (-this.SLIDER_DELTA);
            updateViewWidthWhenTouchLeftSlideToLeft((int) resultWidth);
            this.mLeftReduceWidth = (int) ((((float) (this.mMaxWidth - this.mRightReduceWidth)) - resultWidth) - ((float) getCantSlideWidth()));
            //LogUtil.d(TAG, String.format("checkStartOrEndWithFive   start   down_width : %s  ,  resultWidth : %s  ,  leftMargin : %s  ,  mLeftReduceWidth : %s ", new Object[]{Integer.valueOf(down_width), Float.valueOf(resultWidth), Float.valueOf(-this.SLIDER_DELTA), Integer.valueOf(this.mLeftReduceWidth)}));
        }
    }

    public void initMaxWidthAndHeight() {
        this.mMaxWidth = getMeasuredWidth();
        if (this.sumSize != StaticLayoutUtil.DefaultSpacingadd) {
            this.mMaxWidth = (int) ((((float) IMAGE_UNIT_WIDTH) * this.sumSize) + ((float) DensityUtil.dip2px(20.0f)));
        }
        this.mMaxHeight = getMeasuredHeight();
        this.mMaxChildCount = (int) this.sumSize;
        this.mMaxItemWidth = IMAGE_UNIT_WIDTH;
        this.SLIDER_DELTA = (float) (IMAGE_UNIT_WIDTH / 2);
        this.mMinWidth = (int) (this.SLIDER_DELTA + ((float) DensityUtil.dip2px(20.0f)));
        LogUtil.d(TAG, String.format("mMaxWidth : %s , mMaxHeight : %s , mMaxChildCount : %s , mMaxItemWidth : %s , SLIDER_DELTA : %s ", new Object[]{Integer.valueOf(this.mMaxWidth), Integer.valueOf(this.mMaxHeight), Integer.valueOf(this.mMaxChildCount), Integer.valueOf(this.mMaxItemWidth), Float.valueOf(this.SLIDER_DELTA)}));
        checkStartOrEndWithFive();
    }

    public void setLongVideosModel(LongVideosModel longVideosModel) {
        this.mLongVideosModel = longVideosModel;
    }

    public LongVideosModel getLongVideosModel() {
        return this.mLongVideosModel;
    }

    private float dealMoveX(float move_x, boolean isLeft) {
        if (this.SLIDER_DELTA == StaticLayoutUtil.DefaultSpacingadd) {
            return move_x;
        }
        int count = (int) (move_x / this.SLIDER_DELTA);
        LogUtil.d(TAG, String.format("dealMoveX :  count : %s , move_x : %s , SLIDER_DELTA : %s , start : %s , end : %s ", new Object[]{Integer.valueOf(count), Float.valueOf(move_x), Float.valueOf(this.SLIDER_DELTA), Float.valueOf(this.start), Float.valueOf(this.end)}));
        return this.SLIDER_DELTA * ((float) count);
    }

    public void setVideoEditModuleListener(VideoEditModuleListener videoEditModuleListener) {
        this.mVideoEditModuleListener = videoEditModuleListener;
    }

    public void setVideoEditModuleParentListener(VideoEditModuleParentListener videoEditModuleParentListener) {
        this.mVideoEditModuleParentListener = videoEditModuleParentListener;
    }

    public VideoEditModuleView(Context context) {
        this(context, null);
    }

    public VideoEditModuleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoEditModuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLeftImageOnTouchListener = new OnTouchListener() {
            int down_leftMargin;
            int down_width;
            float down_x;
            float down_y;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & 255) {
                    case 0:
                        VideoEditModuleView.this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.down_width = VideoEditModuleView.this.filter_loop_slider_root_rl.getWidth();
                        this.down_leftMargin = ((LayoutParams) VideoEditModuleView.this.filter_loop_preview_ll.getLayoutParams()).leftMargin;
                        VideoEditModuleView.this.down_start = VideoEditModuleView.this.start;
                        VideoEditModuleView.this.last_start = VideoEditModuleView.this.start;
                        if (VideoEditModuleView.this.mVideoEditModuleListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleListener.needMeasureWidth();
                        }
                        if (VideoEditModuleView.this.mVideoEditModuleParentListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleParentListener.onStartSlide();
                        }
                        LogUtil.d(VideoEditModuleView.TAG, String.format("filter_loop_slider_left_iv : ACTION_DOWN , down_x : %s , down_y : %s , down_width : %s , down_start : %s ", new Object[]{Float.valueOf(this.down_x), Float.valueOf(this.down_y), Integer.valueOf(this.down_width), Float.valueOf(VideoEditModuleView.this.down_start)}));
                        VideoEditModuleView.this.initCurrentScrollX();
                        break;
                    case 1:
                    case 3:
                        VideoEditModuleView.this.getParent().requestDisallowInterceptTouchEvent(false);
                        VideoEditModuleView.this.post(new Runnable() {
                            public void run() {
                                VideoEditModuleView.this.animateRestoreAfterSlideLeft();
                            }
                        });
                        if (VideoEditModuleView.this.mVideoEditModuleParentListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleParentListener.onEndSlide();
                        }
                        LogUtil.d(VideoEditModuleView.TAG, "filter_loop_slider_left_iv : ACTION_UP ACTION_CANCEL");
                        break;
                    case 2:
                        float move_x = event.getRawX() - this.down_x;
                        float move_y = event.getRawY() - this.down_y;
                        float true_move_x = move_x;
                        move_x = VideoEditModuleView.this.dealMoveX(move_x, true);
                        VideoEditModuleView.this.updateViewWhenSlideLeft((int) (((float) this.down_width) - move_x), move_x, (int) (((float) this.down_leftMargin) - move_x), this.down_width, true_move_x, this.down_leftMargin);
                        //LogUtil.d(VideoEditModuleView.TAG, String.format("filter_loop_slider_left_iv : ACTION_MOVE , move_x : %s , move_y : %s , resultWidth : %s , leftMargin : %s , down_width : %s ", new Object[]{Float.valueOf(move_x), Float.valueOf(move_y), Float.valueOf(resultWidth), Float.valueOf(leftMargin), Integer.valueOf(this.down_width)}));
                        break;
                }
                return true;
            }
        };
        this.mLeftReduceWidth = 0;
        this.mRightReduceWidth = 0;
        this.needPaddingLeftAnimate = false;
        this.needMoveRightAnimate = false;
        this.lastMoveX = -1.0f;
        this.mRightImageOnTouchListener = new OnTouchListener() {
            float down_width;
            float down_x;
            float down_y;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & 255) {
                    case 0:
                        VideoEditModuleView.this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.down_width = (float) VideoEditModuleView.this.filter_loop_slider_root_rl.getWidth();
                        VideoEditModuleView.this.down_end = VideoEditModuleView.this.end;
                        VideoEditModuleView.this.last_end = VideoEditModuleView.this.end;
                        if (VideoEditModuleView.this.mVideoEditModuleListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleListener.needMeasureWidth();
                        }
                        if (VideoEditModuleView.this.mVideoEditModuleParentListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleParentListener.initCurrentScrollX();
                            VideoEditModuleView.this.mVideoEditModuleParentListener.onStartSlide();
                        }
                        LogUtil.d(VideoEditModuleView.TAG, String.format("filter_loop_slider_right_iv : ACTION_DOWN , down_x : %s , down_y : %s , down_width : %s , down_end : %s ", new Object[]{Float.valueOf(this.down_x), Float.valueOf(this.down_y), Float.valueOf(this.down_width), Float.valueOf(VideoEditModuleView.this.down_end)}));
                        break;
                    case 1:
                    case 3:
                        VideoEditModuleView.this.getParent().requestDisallowInterceptTouchEvent(false);
                        LogUtil.d(VideoEditModuleView.TAG, "filter_loop_slider_right_iv : ACTION_UP ACTION_CANCEL");
                        if (VideoEditModuleView.this.needMoveRightAnimate) {
                            int upWidth = VideoEditModuleView.this.filter_loop_slider_root_rl.getWidth();
                            if (((float) upWidth) < this.down_width && VideoEditModuleView.this.mVideoEditModuleParentListener != null) {
                                VideoEditModuleView.this.mVideoEditModuleParentListener.onScrollViewSmoothScrollTo((int) (this.down_width - ((float) upWidth)));
                            }
                        }
                        if (VideoEditModuleView.this.mVideoEditModuleParentListener != null) {
                            VideoEditModuleView.this.mVideoEditModuleParentListener.onEndSlide();
                            break;
                        }
                        break;
                    case 2:
                        float move_y = event.getRawY() - this.down_y;
                        float move_x = VideoEditModuleView.this.dealMoveX(event.getRawX() - this.down_x, false);
                        VideoEditModuleView.this.updateViewWhenSlideRight((int) (this.down_width + move_x), move_x, this.down_width);
                        //LogUtil.d(VideoEditModuleView.TAG, String.format("filter_loop_slider_right_iv : ACTION_MOVE , move_X : %s , move_y : %s , resultWidth : %s", new Object[]{Float.valueOf(move_x), Float.valueOf(move_y), Float.valueOf(resultWidth)}));
                        break;
                }
                return true;
            }
        };
        this.lastRightMoveX = -1.0f;
        this.needPause = true;
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_video_edit_module, this, true);
        ButterKnife.bind(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
        this.right_iv_slide_view.setOnTouchListener(this.mRightImageOnTouchListener);
        this.left_iv_slide_view.setOnTouchListener(this.mLeftImageOnTouchListener);
        this.childViews = new HashMap();
    }

    public void setChildImage(float time, Bitmap bitmap) {
        Set<Float> floats = this.childViews.keySet();
        LogUtil.d(TAG, String.format("time : %s , floats : %s ,", new Object[]{Float.valueOf(time), Arrays.toString(floats.toArray())}));
        if (bitmap != null) {
            LogUtil.d(TAG, String.format("bitmap != null time : %s , floats : %s ,", new Object[]{Float.valueOf(time), Arrays.toString(floats.toArray())}));
            ImageView imageView = (ImageView) this.childViews.get(Float.valueOf(time));
            if (imageView != null) {
                LogUtil.d(TAG, String.format("imageView != null time : %s , floats : %s ,", new Object[]{Float.valueOf(time), Arrays.toString(floats.toArray())}));
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void insertChildImage(float time, ImageView imageView) {
        if (!this.childViews.containsKey(Float.valueOf(time))) {
            this.childViews.put(Float.valueOf(time), imageView);
            imageView.setTag(Float.valueOf(time));
            this.filter_loop_preview_ll.addView(imageView, 0);
            LogUtil.d(TAG, String.format("onStartGetBitmap insertChildImage time : %s ", new Object[]{Float.valueOf(time)}));
            if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onStartGetBitmap((long) (1000.0f * time), this.mLongVideosModel);
            }
        }
    }

    public void appendChildImage(float time, ImageView imageView, Bitmap bitmap) {
        boolean z = false;
        if (!this.childViews.containsKey(Float.valueOf(time))) {
            this.childViews.put(Float.valueOf(time), imageView);
            imageView.setTag(Float.valueOf(time));
            this.filter_loop_preview_ll.addView(imageView);
            String str = TAG;
            String str2 = "onStartGetBitmap appendChildImage time : %s , bitmap == null : %b ";
            Object[] objArr = new Object[2];
            objArr[0] = Float.valueOf(time);
            if (bitmap == null) {
                z = true;
            }
            objArr[1] = Boolean.valueOf(z);
            LogUtil.d(str, String.format(str2, objArr));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onStartGetBitmap((long) (1000.0f * time), this.mLongVideosModel);
            }
        }
    }

    private void removeChildImage(float time) {
        ImageView remove = (ImageView) this.childViews.remove(Float.valueOf(time));
        if (remove != null) {
            this.filter_loop_preview_ll.removeView(remove);
        }
    }

    private void initCurrentScrollX() {
        int indexOfChild = ((ViewGroup) getParent()).indexOfChild(this);
        if (this.mVideoEditModuleParentListener != null) {
            this.mVideoEditModuleParentListener.initCurrentScrollX();
        }
    }

    private void animateRestoreAfterSlideLeft() {
        final int width = getWidth();
        final int paddingLeft = getPaddingLeft();
        if (this.mVideoEditModuleListener != null) {
            this.mVideoEditModuleListener.needMeasureWidth();
        }
        if (paddingLeft > 0) {
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.setFloatValues(1.0f);
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    LogUtil.d(VideoEditModuleView.TAG, "animateRestoreAfterSlideLeft : animateRestoreAfterSlideLeft  value : " + value);
                    int changeWidth = (int) (((float) paddingLeft) * value);
                    int left = paddingLeft - changeWidth;
                    VideoEditModuleView.this.setPadding(left, 0, 0, 0);
                    ViewGroup.LayoutParams layoutParams3 = VideoEditModuleView.this.getLayoutParams();
                    layoutParams3.width = width - changeWidth;
                    VideoEditModuleView.this.setLayoutParams(layoutParams3);
                    if (VideoEditModuleView.this.mVideoEditModuleListener != null) {
                        VideoEditModuleView.this.mVideoEditModuleListener.animateRestoreAfterSlideLeft(changeWidth, left);
                    }
                }
            });
            valueAnimator.start();
        }
    }

    public void setNeedPaddingLeftAnimate(boolean needPaddingLeftAnimate) {
        this.needPaddingLeftAnimate = needPaddingLeftAnimate;
    }

    public void setNeedMoveRightAnimate(boolean needMoveRightAnimate) {
        this.needMoveRightAnimate = needMoveRightAnimate;
    }

    private void hideLeftSlideArrowView() {
        this.filter_loop_slider_left_iv.setImageBitmap(null);
    }

    private void showLeftSlideArrowView() {
        this.filter_loop_slider_left_iv.setImageResource(R.drawable.icon_10_trim_video_right);
    }

    private void hideRightSlideArrowView() {
        this.filter_loop_slider_right_iv.setImageBitmap(null);
    }

    private void showRightSlideArrowView() {
        this.filter_loop_slider_right_iv.setImageResource(R.drawable.icon_10_trim_video_right);
    }

    private void updateViewWhenSlideLeft(int width, float move_x, int leftMargin, int downWidth, float true_move_x, int down_leftMargin) {
        if (this.lastMoveX != move_x) {
            boolean useTrueMoveX = false;
            this.start = this.down_start + ((move_x / this.SLIDER_DELTA) / 2.0f);
            if (this.start <= StaticLayoutUtil.DefaultSpacingadd) {
                this.start = StaticLayoutUtil.DefaultSpacingadd;
                useTrueMoveX = true;
                hideLeftSlideArrowView();
            } else if (this.start >= this.end) {
                this.start = this.end - 0.5f;
                showLeftSlideArrowView();
            } else {
                showLeftSlideArrowView();
            }
            move_x = ((this.start - this.down_start) * 2.0f) * this.SLIDER_DELTA;
            width = (int) (((float) downWidth) - move_x);
            leftMargin = (int) (((float) down_leftMargin) - move_x);
            int minLeftMargin = (getLeftMoveMaxWidth() * -1) - DensityUtil.dip2px(10.0f);
            if (leftMargin < minLeftMargin) {
                LogUtil.d(TAG, String.format("updateViewWhenSlideLeft1   leftMargin < minLeftMargin leftMargin : %s , minLeftMargin : %s ", new Object[]{Integer.valueOf(leftMargin), Integer.valueOf(minLeftMargin)}));
                leftMargin = minLeftMargin;
            } else if (leftMargin > 0) {
                leftMargin = DensityUtil.dip2px(10.0f);
            }
            LogUtil.d(TAG, String.format("updateViewWhenSlideLeft1   width : %s , move_x : %s , leftMargin : %s  , start : %s ", new Object[]{Integer.valueOf(width), Float.valueOf(move_x), Integer.valueOf(leftMargin), Float.valueOf(this.start)}));
            LayoutParams layoutParams;
            VideoEditModuleParentListener videoEditModuleParentListener;
            int i;
            if (move_x < StaticLayoutUtil.DefaultSpacingadd) {
                setPadding(0, 0, 0, 0);
                layoutParams = (LayoutParams) this.filter_loop_preview_ll.getLayoutParams();
                if (isEndWidthPointFive(this.start)) {
                    layoutParams.leftMargin = (int) (-this.SLIDER_DELTA);
                } else {
                    layoutParams.leftMargin = 0;
                }
                updateViewWidthWhenTouchLeftSlideToLeft(width);
                this.mLeftReduceWidth = ((this.mMaxWidth - this.mRightReduceWidth) - width) - getCantSlideWidth();
                postInvalidate();
                if (this.mVideoEditModuleListener != null) {
                    this.mVideoEditModuleListener.updateViewWhenSlideLeft(width, move_x, leftMargin, this.needPaddingLeftAnimate);
                }
                if (this.mVideoEditModuleParentListener != null) {
                    videoEditModuleParentListener = this.mVideoEditModuleParentListener;
                    if (useTrueMoveX) {
                        i = (int) move_x;
                    } else {
                        i = (int) move_x;
                    }
                    videoEditModuleParentListener.onSlideLeftParentScroll(i);
                }
            } else {
                if (move_x >= StaticLayoutUtil.DefaultSpacingadd && this.needPaddingLeftAnimate) {
                    setPadding((int) move_x, 0, 0, 0);
                }
                layoutParams = (LayoutParams) this.filter_loop_preview_ll.getLayoutParams();
                if (isEndWidthPointFive(this.start)) {
                    layoutParams.leftMargin = (int) (-this.SLIDER_DELTA);
                } else {
                    layoutParams.leftMargin = 0;
                }
                updateViewWidthWhenTouchLeftSlideToRight(width);
                if (!this.needPaddingLeftAnimate || move_x == StaticLayoutUtil.DefaultSpacingadd) {
                    updateViewWidth(width);
                }
                this.mLeftReduceWidth = ((this.mMaxWidth - this.mRightReduceWidth) - width) - getCantSlideWidth();
                postInvalidate();
                if (this.mVideoEditModuleListener != null) {
                    this.mVideoEditModuleListener.updateViewWhenSlideLeft(width, move_x, leftMargin, this.needPaddingLeftAnimate);
                }
                if ((!this.needPaddingLeftAnimate || move_x == StaticLayoutUtil.DefaultSpacingadd) && this.mVideoEditModuleParentListener != null) {
                    videoEditModuleParentListener = this.mVideoEditModuleParentListener;
                    if (useTrueMoveX) {
                        i = (int) move_x;
                    } else {
                        i = (int) move_x;
                    }
                    videoEditModuleParentListener.onSlideLeftParentScroll(i);
                }
            }
            float i2;
            if (this.start < this.last_start) {
                for (i2 = dealLeftTimeNumber(this.last_start); i2 >= dealLeftTimeNumber(this.start); i2 -= 1.0f) {
                    LogUtil.d(TAG, String.format("dealLeftTimeNumber last_start add i : %s ", new Object[]{Float.valueOf(i2)}));
                    if (!this.childViews.containsKey(Float.valueOf(i2))) {
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(IMAGE_UNIT_WIDTH, -1));
                        imageView.setScaleType(ScaleType.FIT_XY);
                        insertChildImage(i2, imageView);
                    }
                }
            } else if (this.start > this.last_start) {
                for (i2 = dealLeftTimeNumber(this.last_start); i2 < dealLeftTimeNumber(this.start); i2 += 1.0f) {
                    LogUtil.d(TAG, String.format("dealLeftTimeNumber last_start remove i : %s ", new Object[]{Float.valueOf(i2)}));
                    removeChildImage(i2);
                }
            }
            this.last_start = this.start;
            this.lastMoveX = move_x;
            if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onSliding();
            }
        }
    }

    private void updateViewWhenSlideRight(int width, float move_x, float down_width) {
        if (this.lastRightMoveX != move_x) {
            this.end = this.down_end + ((move_x / this.SLIDER_DELTA) / 2.0f);
            if (this.end > this.sumSize) {
                this.end = this.sumSize;
                hideRightSlideArrowView();
            } else if (this.end <= this.start) {
                this.end = this.start + 0.5f;
                showRightSlideArrowView();
            } else {
                showRightSlideArrowView();
            }
            move_x = ((this.end - this.down_end) * 2.0f) * this.SLIDER_DELTA;
            width = (int) (move_x + down_width);
            LogUtil.d(TAG, String.format("updateViewWhenSlideRight1   width : %s , move_x : %s , down_width : %s  , end : %s  , down_end : %s ", new Object[]{Integer.valueOf(width), Float.valueOf(move_x), Float.valueOf(down_width), Float.valueOf(this.end), Float.valueOf(this.down_end)}));
            if (this.needMoveRightAnimate && this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onChangeLastViewWidth(move_x);
            }
            updateViewWidthWhenTouchLeftSlideToLeft(width);
            float i;
            if (this.end < this.last_end) {
                for (i = dealTimeNumber(this.last_end + 0.5f); i > dealTimeNumber(this.end + 0.5f); i -= 1.0f) {
                    LogUtil.d(TAG, String.format("dealTimeNumber last_end remove i : %s ", new Object[]{Float.valueOf(i)}));
                    removeChildImage(i);
                }
            } else if (this.end > this.last_end) {
                for (i = dealTimeNumber(this.last_end + 0.5f); i <= dealTimeNumber(this.end + 0.5f); i += 1.0f) {
                    LogUtil.d(TAG, String.format("dealTimeNumber last_end add i : %s ", new Object[]{Float.valueOf(i)}));
                    if (!this.childViews.containsKey(Float.valueOf(i))) {
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(IMAGE_UNIT_WIDTH, -1));
                        imageView.setScaleType(ScaleType.FIT_XY);
                        appendChildImage(i, imageView, null);
                    }
                }
            }
            this.last_end = this.end;
            postInvalidate();
            this.mRightReduceWidth = ((this.mMaxWidth - this.mLeftReduceWidth) - width) - getCantSlideWidth();
            if (this.mVideoEditModuleListener != null) {
                this.mVideoEditModuleListener.updateViewWhenSlideRight(width, move_x);
            }
            this.lastRightMoveX = move_x;
            if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onSliding();
            }
        }
    }

    private float dealLeftTimeNumber(float number) {
        if (isEndWidthPointFive(number)) {
            return number - 0.5f;
        }
        return number;
    }

    private boolean isEndWidthPointFive(float number) {
        if ((((int) (10.0f * number)) / 5) % 2 == 1) {
            return true;
        }
        return false;
    }

    private float dealTimeNumber(float number) {
        if (isEndWidthPointFive(number)) {
            return number - 0.5f;
        }
        return number;
    }

    private void updateCornerCcvWidth(int width) {
        ViewGroup.LayoutParams layoutParams = this.player_loop_corner_ccv.getLayoutParams();
        layoutParams.width = width;
        this.player_loop_corner_ccv.setLayoutParams(layoutParams);
    }

    private void updateSliderRootRllWidth(int width) {
        ViewGroup.LayoutParams layoutParams = this.filter_loop_slider_root_rll.getLayoutParams();
        layoutParams.width = width;
        this.filter_loop_slider_root_rll.setLayoutParams(layoutParams);
    }

    private void updateSliderRootRlWidth(int width) {
        ViewGroup.LayoutParams layoutParams = this.filter_loop_slider_root_rl.getLayoutParams();
        layoutParams.width = width;
        this.filter_loop_slider_root_rl.setLayoutParams(layoutParams);
    }

    private void updateViewWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);
    }

    private void updateFilterLoopPreviewParentCll(int width) {
        ViewGroup.LayoutParams layoutParams = this.filter_loop_preview_parent_cll.getLayoutParams();
        layoutParams.width = width - DensityUtil.dip2px(20.0f);
        this.filter_loop_preview_parent_cll.setLayoutParams(layoutParams);
    }

    private void updateFilterLoopPreviewLlWidth(int width) {
        LayoutParams layoutParams = (LayoutParams) this.filter_loop_preview_ll.getLayoutParams();
        layoutParams.width = (width - DensityUtil.dip2px(20.0f)) - layoutParams.leftMargin;
        this.filter_loop_preview_ll.setLayoutParams(layoutParams);
    }

    private void updateViewWidthWhenTouchLeftSlideToRight(int width) {
        updateSliderRootRlWidth(width);
        updateSliderRootRllWidth(width);
        updateCornerCcvWidth(width);
        updateFilterLoopPreviewLlWidth(width);
        updateFilterLoopPreviewParentCll(width);
    }

    private void updateViewWidthWhenTouchLeftSlideToLeft(int width) {
        updateViewWidth(width);
        updateSliderRootRlWidth(width);
        updateSliderRootRllWidth(width);
        updateCornerCcvWidth(width);
        updateFilterLoopPreviewLlWidth(width);
        updateFilterLoopPreviewParentCll(width);
    }

    private int getMaxWidth() {
        return (this.mMaxWidth - this.mLeftReduceWidth) - this.mRightReduceWidth;
    }

    private int getCantSlideWidth() {
        int result = (int) (((this.sumSize - this.end) + (this.start - StaticLayoutUtil.DefaultSpacingadd)) * ((float) IMAGE_UNIT_WIDTH));
        LogUtil.d(TAG, String.format("getCantSlideWidth  result : %s ", new Object[]{Integer.valueOf(result)}));
        return result;
    }

    private int getRightMoveMaxWidth() {
        return (this.mMaxWidth - this.mLeftReduceWidth) - getCantSlideWidth();
    }

    private int getLeftMoveMaxWidth() {
        return (this.mMaxWidth - this.mRightReduceWidth) - getCantSlideWidth();
    }

    private int getMinWidth() {
        return this.mMinWidth;
    }

    public boolean onLongClick(View v) {
        return false;
    }

    public void setNeedPause(boolean needPause) {
        this.needPause = needPause;
    }

    public void onClick(View v) {
        //LogUtil.d("refreshViewEditView", String.format("onClick", new Object[0]));
        if (this.filter_loop_slider_root_rl.getVisibility() != VISIBLE) {
            ChangeChildDrawOrderView parent = (ChangeChildDrawOrderView) getParent();
            parent.setTopPosition(parent.indexOfChild(this));
            //LogUtil.d("refreshViewEditView", String.format("onClick 1 position : %s ", new Object[]{Integer.valueOf(position)}));
            int width = getWidth();
            this.filter_loop_slider_root_rl.getLayoutParams().width = width;
            this.filter_loop_slider_root_rll.getLayoutParams().width = width;
            this.player_loop_corner_ccv.getLayoutParams().width = width;
            this.filter_loop_preview_parent_cll.clearLeftRightPadding();
            this.filter_loop_slider_root_rl.setVisibility(VISIBLE);
            this.video_edit_audiowaveview.setViewDrawData(this.mLongVideosModel.getNeedData());
            this.video_edit_audiowaveview.setVisibility(VISIBLE);
            if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onVideoEditModuleViewClick(this, 0, this.needPause);
            }
        } else {
            //LogUtil.d("refreshViewEditView", String.format("onClick 2", new Object[0]));
            hideSliderRootRl();
            this.video_edit_audiowaveview.setVisibility(GONE);
            if (this.mVideoEditModuleParentListener != null) {
                this.mVideoEditModuleParentListener.onVideoEditModuleViewClick(this, 1, this.needPause);
            }
        }
        this.needPause = true;
    }

    public void hideSliderRootRl() {
        this.filter_loop_preview_parent_cll.restoreLeftRightPadding();
        this.filter_loop_slider_root_rl.setVisibility(GONE);
    }
}