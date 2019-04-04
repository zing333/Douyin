package cn.nineton.onetake.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.StringUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.WeakHandler;

//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.custom.WeakHandler;
//import com.blink.academy.onetake.support.LongPressTouchListener;
//import com.blink.academy.onetake.support.LongPressTouchListener.CustomPressCallback;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.StringUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
import java.math.BigDecimal;

public class VideoEditTextHolder extends VideoEditViewHolder implements OnClickListener, Callback {
    private static int CONTENT_MAX_HEIGHT = DensityUtil.dip2px(44.0f);
    private static int CONTENT_MIN_HEIGHT = DensityUtil.dip2px(26.0f);
    public static int ITEM_EDIT_HEIGHT = DensityUtil.dip2px(101.0f);
    public static int ITEM_MIN_HEIGHT = DensityUtil.dip2px(44.0f);
    public static int ITEM_MIN_SLIDE_HEIGHT = DensityUtil.dip2px(59.0f);
    public static int ITEM_SLIDE_HEIGHT = DensityUtil.dip2px(116.0f);
    private static final int MSG_SLIDE_CONTENT_AUTO_SCROLL_TO_LEFT = 5;
    private static final int MSG_SLIDE_CONTENT_AUTO_SCROLL_TO_RIGHT = 6;
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_LEFT = 3;
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_RIGHT = 4;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_LEFT = 1;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_RIGHT = 2;
    private static final String TAG = VideoEditTextHolder.class.getSimpleName();
    private static int TEXT_BOTTOM_RECT_HEIGHT_LONGPRESS = DensityUtil.dip2px(9.0f);
    private static int TEXT_BOTTOM_RECT_HEIGHT_NORMAL = DensityUtil.dip2px(9.0f);
    private static int TEXT_CENTER_BOTTOM_MARGIN_LONGPRESS = DensityUtil.dip2px(20.0f);
    private static int TEXT_CENTER_BOTTOM_MARGIN_NORMAL = DensityUtil.dip2px(5.0f);
    public static float TEXT_CIRCLE_CENTER_PERCENT = 0.3f;
    private static int TEXT_TOP_HEIGHT = DensityUtil.dip2px(40.0f);
    private int DELAY_TIME = 50;
    private float SLIDER_DELTA = (((float) VideoEditHelper.IMAGE_UNIT_WIDTH) * VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue());
    private long lastDealTime;
    @BindView(R.id.left_top_edit_iv)
    ImageView left_top_edit_iv;
    @BindView(R.id.left_top_edit_tv)
    TextView left_top_edit_tv;
    private boolean longPressing;
    private AnimationEndListener mAnimationEndListener;
    private WeakHandler mHandler;
    private float mLastDownMoveX;
    OnTouchListener mLeftTextOnTouchListener = new OnTouchListener() {
        int down_leftMargin;
        int down_width;
        float down_x;
        float down_y;
        float last_move_x;

        public boolean onTouch(View v, MotionEvent event) {
            if (VideoEditTextHolder.this.longPressing) {
                return false;
            }
            switch (event.getActionMasked()) {
                case 0:
                    ViewParent itemViewParent = VideoEditTextHolder.this.itemView.getParent();
                    if (itemViewParent != null) {
                        itemViewParent.requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.last_move_x = StaticLayoutUtil.DefaultSpacingadd;
                        VideoEditTextHolder.this.mHelper.onSlideTextLeftStart(VideoEditTextHolder.this.getAdapterPosition());
                        break;
                    }
                    return false;
                case 1:
                case 3:
                    VideoEditTextHolder.this.mLastDownMoveX = (float) (VideoEditTextHolder.this.mScreenWidth / 2);
                    VideoEditTextHolder.this.mHandler.removeMessages(3);
                    VideoEditTextHolder.this.mHandler.removeMessages(4);
                    ViewParent parent = VideoEditTextHolder.this.itemView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                        VideoEditTextHolder.this.mHelper.onSlideTextLeftEnd(VideoEditTextHolder.this.getAdapterPosition());
                        break;
                    }
                    return false;
                case 2:
                    VideoEditTextHolder.this.mLastDownMoveX = event.getRawX();
                    if (!VideoEditTextHolder.this.isInStartArea()) {
                        if (!VideoEditTextHolder.this.isInEndArea()) {
                            VideoEditTextHolder.this.mHandler.removeMessages(3);
                            VideoEditTextHolder.this.mHandler.removeMessages(4);
                            float move_y = event.getRawY() - this.down_y;
                            float move_x = VideoEditTextHolder.this.dealMoveX(event.getRawX() - this.down_x, true);
                            float result_move_x = BigDecimal.valueOf((double) move_x).subtract(BigDecimal.valueOf((double) this.last_move_x)).setScale(1, 4).floatValue();
                            this.last_move_x = move_x;
                            float resultWidth = ((float) this.down_width) - move_x;
                            float leftMargin = ((float) this.down_leftMargin) - move_x;
                            int moveTime = (int) (result_move_x / VideoEditTextHolder.this.SLIDER_DELTA);
                            if (moveTime != 0) {
                                VideoEditTextHolder.this.mHelper.onSlideTextLeft(VideoEditTextHolder.this.getAdapterPosition(), moveTime);
                                break;
                            }
                        }
                        VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(3, (long) VideoEditTextHolder.this.DELAY_TIME);
                        break;
                    }
                    VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(4, (long) VideoEditTextHolder.this.DELAY_TIME);
                    break;
            }
            return true;
        }
    };
    OnTouchListener mRightTextOnTouchListener = new OnTouchListener() {
        float down_width;
        float down_x;
        float down_y;
        float last_move_x;

        public boolean onTouch(View v, MotionEvent event) {
            if (VideoEditTextHolder.this.longPressing) {
                return false;
            }
            switch (event.getAction() & 255) {
                case 0:
                    if (VideoEditTextHolder.this.itemView.getParent() != null) {
                        VideoEditTextHolder.this.itemView.getParent().requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.last_move_x = StaticLayoutUtil.DefaultSpacingadd;
                        VideoEditTextHolder.this.mHelper.onSlideTextRightStart(VideoEditTextHolder.this.getAdapterPosition());
                        break;
                    }
                    return false;
                case 1:
                case 3:
                    VideoEditTextHolder.this.mLastDownMoveX = (float) (VideoEditTextHolder.this.mScreenWidth / 2);
                    VideoEditTextHolder.this.mHandler.removeMessages(1);
                    VideoEditTextHolder.this.mHandler.removeMessages(2);
                    if (VideoEditTextHolder.this.itemView.getParent() != null) {
                        VideoEditTextHolder.this.itemView.getParent().requestDisallowInterceptTouchEvent(false);
                        VideoEditTextHolder.this.mHelper.onSlideTextRightEnd(VideoEditTextHolder.this.getAdapterPosition());
                        break;
                    }
                    return false;
                case 2:
                    VideoEditTextHolder.this.mLastDownMoveX = event.getRawX();
                    if (!VideoEditTextHolder.this.isInStartArea()) {
                        if (!VideoEditTextHolder.this.isInEndArea()) {
                            VideoEditTextHolder.this.mHandler.removeMessages(1);
                            VideoEditTextHolder.this.mHandler.removeMessages(2);
                            float move_y = event.getRawY() - this.down_y;
                            float move_x = VideoEditTextHolder.this.dealMoveX(event.getRawX() - this.down_x, false);
                            float result_move_x = BigDecimal.valueOf((double) move_x).subtract(BigDecimal.valueOf((double) this.last_move_x)).setScale(1, 4).floatValue();
                            this.last_move_x = move_x;
                            float resultWidth = this.down_width + move_x;
                            int moveTime = (int) (result_move_x / VideoEditTextHolder.this.SLIDER_DELTA);
                            if (moveTime != 0) {
                                VideoEditTextHolder.this.mHelper.onSlideTextRight(VideoEditTextHolder.this.getAdapterPosition(), moveTime);
                                break;
                            }
                        }
                        VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(1, (long) VideoEditTextHolder.this.DELAY_TIME);
                        break;
                    }
                    VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(2, (long) VideoEditTextHolder.this.DELAY_TIME);
                    break;
            }
            return true;
        }
    };
    private int mScreenWidth;
    @BindView(R.id.right_top_delete_iv)
    ImageView right_top_delete_iv;
    @BindView(R.id.text_bottom_circle_iv)
    ImageView text_bottom_circle_iv;
    @BindView(R.id.text_bottom_rect_bottom_iv)
    ImageView text_bottom_rect_bottom_iv;
    @BindView(R.id.text_bottom_rect_iv)
    ImageView text_bottom_rect_iv;
    @BindView(R.id.text_bottom_right_circle_iv)
    ImageView text_bottom_right_circle_iv;
    @BindView(R.id.text_bottom_right_rect_bottom_iv)
    ImageView text_bottom_right_rect_bottom_iv;
    @BindView(R.id.text_bottom_right_rect_iv)
    ImageView text_bottom_right_rect_iv;
    @BindView(R.id.text_center_cv)
    CardView text_center_cv;
    @BindView(R.id.text_edit_click_view)
    View text_edit_click_view;
    @BindView(R.id.text_edit_delete_view)
    View text_edit_delete_view;
    @BindView(R.id.text_left_slide_iv)
    ImageView text_left_slide_iv;
    @BindView(R.id.text_left_slide_touch_view)
    View text_left_slide_touch_view;
    @BindView(R.id.text_parent_rl)
    RelativeLayout text_parent_rl;
    @BindView(R.id.text_right_slide_iv)
    ImageView text_right_slide_iv;
    @BindView(R.id.text_right_slide_touch_view)
    View text_right_slide_touch_view;
    @BindView(R.id.text_top_rl)
    RelativeLayout text_top_rl;
    @BindView(R.id.text_tv)
    TextView text_tv;

    public interface AnimationEndListener {
        void endListener();
    }

    public boolean handleMessage(Message msg) {
        long currentTimeMillis;
        switch (msg.what) {
            case 1:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideTextRightAutoScrollToLeft(getAdapterPosition());
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
                        this.mHelper.onSlideTextRightAutoScrollToRight(getAdapterPosition());
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
                        this.mHelper.onSlideTextLeftAutoScrollToLeft(getAdapterPosition());
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
                        this.mHelper.onSlideTextLeftAutoScrollToRight(getAdapterPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(4, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 5:
                if (isInEndArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideTextContentAutoScrollToLeft(getAdapterPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(5, (long) this.DELAY_TIME);
                        break;
                    }
                }
                break;
            case 6:
                if (isInStartArea()) {
                    currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastDealTime >= ((long) this.DELAY_TIME)) {
                        this.mHelper.onSlideTextContentAutoScrollToRight(getAdapterPosition());
                        this.lastDealTime = currentTimeMillis;
                        this.mHandler.sendEmptyMessageDelayed(6, (long) this.DELAY_TIME);
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

    @SuppressLint({"ClickableViewAccessibility"})
    public VideoEditTextHolder(final View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.mScreenWidth = DensityUtil.getMetricsWidth(context);
        this.text_left_slide_touch_view.setOnTouchListener(this.mLeftTextOnTouchListener);
        this.text_right_slide_touch_view.setOnTouchListener(this.mRightTextOnTouchListener);
        LongPressTouchListener pressTouchListener = new LongPressTouchListener(getActivity(), this.SLIDER_DELTA);
        pressTouchListener.setPressCallback(new LongPressTouchListener.CustomPressCallback() {
            public boolean needLongPress() {
                int adapterPosition = VideoEditTextHolder.this.getAdapterPosition();
                if (adapterPosition >= 0 && adapterPosition < VideoEditTextHolder.this.mHelper.getAllDatas().size()) {
                    VideoEditImageEntity entity = (VideoEditImageEntity) VideoEditTextHolder.this.mHelper.getAllDatas().get(adapterPosition);
                    if (VideoEditTextHolder.this.hasSelected() && !entity.isPin()) {
                        return false;
                    }
                }
                return super.needLongPress();
            }

            public void onLongPress(MotionEvent event) {
                ViewParent itemViewParent = itemView.getParent();
                if (itemViewParent != null) {
                    itemViewParent.requestDisallowInterceptTouchEvent(true);
                    VideoEditTextHolder.this.longPressing = true;
                    int adapterPosition = VideoEditTextHolder.this.getAdapterPosition();
                    if (adapterPosition >= 0 && adapterPosition < VideoEditTextHolder.this.mHelper.getAllDatas().size()) {
                        VideoEditImageEntity entity = (VideoEditImageEntity) VideoEditTextHolder.this.mHelper.getAllDatas().get(adapterPosition);
                        LongVideosModel longVideosModel = entity.getLongVideosModel();
                        if (!VideoEditTextHolder.this.hasSelected() || entity.isPin()) {
                            longVideosModel.setLongPressing(true);
                            longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                        } else {
                            return;
                        }
                    }
                    VideoEditTextHolder.this.changeToLongPressedStatus();
                    VideoEditTextHolder.this.mHelper.setLongPressingStatus(VideoEditTextHolder.this.longPressing);
                    VideoEditTextHolder.this.mHelper.onSlideTextContentStart(VideoEditTextHolder.this.getAdapterPosition());
                }
            }

            public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
                VideoEditTextHolder.this.mLastDownMoveX = (float) (VideoEditTextHolder.this.mScreenWidth / 2);
                VideoEditTextHolder.this.mHandler.removeMessages(5);
                VideoEditTextHolder.this.mHandler.removeMessages(6);
                int adapterPosition = VideoEditTextHolder.this.getAdapterPosition();
                LongVideosModel longVideosModel;
                VideoEditImageEntity entity;
                if (adapterPosition < 0 || adapterPosition >= VideoEditTextHolder.this.mHelper.getAllDatas().size()) {
                    for (VideoEditImageEntity entity2 : VideoEditTextHolder.this.mHelper.getAllDatas()) {
                        longVideosModel = entity2.getLongVideosModel();
                        if (longVideosModel != null) {
                            longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                            if (longVideosModel.isLongPressing()) {
                                longVideosModel.setLongPressing(false);
                                break;
                            }
                        }
                    }
                } else {
                    VideoEditImageEntity entity2 = (VideoEditImageEntity) VideoEditTextHolder.this.mHelper.getAllDatas().get(adapterPosition);
                    longVideosModel = entity2.getLongVideosModel();
                    if (!VideoEditTextHolder.this.hasSelected() || entity2.isPin()) {
                        longVideosModel.setLongPressing(false);
                        longVideosModel.setUsageStartTime(longVideosModel.getStartTimeMs());
                    } else {
                        return;
                    }
                }
                if (isLongPress) {
                    VideoEditTextHolder.this.restoreFromLongPressedStatus();
                }
                ViewParent parent = itemView.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    VideoEditTextHolder.this.longPressing = false;
                    VideoEditTextHolder.this.mHelper.setLongPressingStatus(VideoEditTextHolder.this.longPressing);
                    VideoEditTextHolder.this.mHelper.onSlideTextContentEnd(VideoEditTextHolder.this.getAdapterPosition());
                }
            }

            public void onActionMove(MotionEvent event, int time) {
                VideoEditTextHolder.this.mLastDownMoveX = event.getRawX();
                if (VideoEditTextHolder.this.isInStartArea()) {
                    VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(6, (long) VideoEditTextHolder.this.DELAY_TIME);
                } else if (VideoEditTextHolder.this.isInEndArea()) {
                    VideoEditTextHolder.this.mHandler.sendEmptyMessageDelayed(5, (long) VideoEditTextHolder.this.DELAY_TIME);
                } else {
                    VideoEditTextHolder.this.mHandler.removeMessages(5);
                    VideoEditTextHolder.this.mHandler.removeMessages(6);
                    if (time != 0) {
                        VideoEditTextHolder.this.mHelper.onSlideTextContent(VideoEditTextHolder.this.getAdapterPosition(), time);
                    }
                }
            }
        });
        this.text_parent_rl.setOnTouchListener(pressTouchListener);
        refreshBottomMarginLeftAndRight(0, 0);
        this.mHandler = new WeakHandler((Callback) this);
    }

    private void refreshBottomMarginLeftAndRight(int paddingLeft, int paddingRight) {
        int margin = (int) (((float) VideoEditHelper.IMAGE_UNIT_WIDTH) * TEXT_CIRCLE_CENTER_PERCENT);
        LayoutParams layoutParams4 = (LayoutParams) this.text_bottom_rect_iv.getLayoutParams();
        layoutParams4.leftMargin = (margin - (layoutParams4.width / 2)) - paddingLeft;
        this.text_bottom_rect_iv.setLayoutParams(layoutParams4);
        LayoutParams layoutParams2 = (LayoutParams) this.text_bottom_rect_bottom_iv.getLayoutParams();
        layoutParams2.leftMargin = (margin - (layoutParams2.width / 2)) - paddingLeft;
        this.text_bottom_rect_bottom_iv.setLayoutParams(layoutParams2);
        LayoutParams layoutParams = (LayoutParams) this.text_bottom_circle_iv.getLayoutParams();
        layoutParams.leftMargin = (margin - (layoutParams.width / 2)) - paddingLeft;
        this.text_bottom_circle_iv.setLayoutParams(layoutParams);
        LayoutParams layoutParams1 = (LayoutParams) this.text_bottom_right_circle_iv.getLayoutParams();
        layoutParams1.rightMargin = (margin - (layoutParams1.width / 2)) - paddingRight;
        this.text_bottom_right_circle_iv.setLayoutParams(layoutParams1);
        LayoutParams layoutParams5 = (LayoutParams) this.text_bottom_right_rect_iv.getLayoutParams();
        layoutParams5.rightMargin = (margin - (layoutParams5.width / 2)) - paddingRight;
        this.text_bottom_right_rect_iv.setLayoutParams(layoutParams5);
        LayoutParams layoutParams3 = (LayoutParams) this.text_bottom_right_rect_bottom_iv.getLayoutParams();
        layoutParams3.rightMargin = (margin - (layoutParams3.width / 2)) - paddingRight;
        this.text_bottom_right_rect_bottom_iv.setLayoutParams(layoutParams3);
    }

    public void onBindViewHolder(int position) {
        this.text_edit_delete_view.setOnClickListener(this);
        this.left_top_edit_tv.setOnClickListener(this);
        this.text_edit_click_view.setOnClickListener(this);
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(getAdapterPosition());
        LongVideosModel longVideosModel = videoEditImageEntity.getLongVideosModel();
        int circleCoverType = 0;
        if (longVideosModel != null) {
            circleCoverType = longVideosModel.getCircleCoverType();
        }
        refreshItemWidth(videoEditImageEntity.getEmpty_type_width(), circleCoverType);
        boolean isLongPressing = false;
        if (longVideosModel != null) {
            String clearContentEnter = StringUtil.clearContentEnter(longVideosModel.getTextContent());
            if (TextUtil.isValidate(clearContentEnter)) {
                this.text_tv.setText(clearContentEnter);
            }
            isLongPressing = longVideosModel.isLongPressing();
        }
        refreshSelectedStatus(videoEditImageEntity.isPin(), isLongPressing);
        checkEditTextVisible(videoEditImageEntity.getEmpty_type_width());
        boolean z = (longVideosModel == null || longVideosModel.getEndRelateTextModel() == null) ? false : true;
        setRightBottomVisible(z);
    }

    private void setRightBottomVisible(boolean visible) {
        if (visible) {
            this.text_bottom_right_circle_iv.setVisibility(View.VISIBLE);
            this.text_bottom_right_rect_iv.setVisibility(View.VISIBLE);
            this.text_bottom_right_rect_bottom_iv.setVisibility(View.VISIBLE);
            return;
        }
        this.text_bottom_right_circle_iv.setVisibility(View.GONE);
        this.text_bottom_right_rect_iv.setVisibility(View.GONE);
        this.text_bottom_right_rect_bottom_iv.setVisibility(View.GONE);
    }

    private void checkEditTextVisible(int itemWidth) {
        if (((float) DensityUtil.dip2px(45.0f)) + this.left_top_edit_tv.getPaint().measureText((String) this.left_top_edit_tv.getText()) > ((float) itemWidth)) {
            this.left_top_edit_tv.setVisibility(View.GONE);
        } else {
            this.left_top_edit_tv.setVisibility(View.VISIBLE);
        }
    }

    private void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    private void setParentRecyclerViewHeight(int height) {
        ViewParent parent = this.itemView.getParent();
        if (parent instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) parent;
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-1, -2);
            }
            layoutParams.height = height;
            recyclerView.setLayoutParams(layoutParams);
        }
    }

    private void refreshSelectedStatus(boolean selected, boolean isLongPressing) {
        int i;
        int i2 = 4;
        int i3 = 0;
        setViewHeight(this.text_top_rl, TEXT_TOP_HEIGHT);
        setViewHeight(this.text_bottom_rect_iv, TEXT_BOTTOM_RECT_HEIGHT_NORMAL);
        setViewHeight(this.text_bottom_right_rect_iv, TEXT_BOTTOM_RECT_HEIGHT_NORMAL);
        RelativeLayout relativeLayout = this.text_top_rl;
        if (selected) {
            i = 0;
        } else {
            i = 8;
        }
        relativeLayout.setVisibility(i);
        ImageView imageView = this.text_left_slide_iv;
        if (selected) {
            i = 0;
        } else {
            i = 4;
        }
        imageView.setVisibility(i);
        View view = this.text_left_slide_touch_view;
        if (selected) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        ImageView imageView2 = this.text_right_slide_iv;
        if (selected) {
            i2 = 0;
        }
        imageView2.setVisibility(i2);
        View view2 = this.text_right_slide_touch_view;
        if (!selected) {
            i3 = 8;
        }
        view2.setVisibility(i3);
        LayoutParams layoutParams = (LayoutParams) this.text_center_cv.getLayoutParams();
        if (selected) {
            layoutParams.height = CONTENT_MAX_HEIGHT;
            if (hasLongPressing()) {
            }
            this.itemView.setAlpha(1.0f);
            this.text_top_rl.setAlpha(1.0f);
            this.text_left_slide_iv.setAlpha(1.0f);
            this.text_right_slide_iv.setAlpha(1.0f);
        } else {
            layoutParams.height = CONTENT_MIN_HEIGHT;
            if (hasSelected()) {
                if (hasLongPressing()) {
                }
                this.itemView.setAlpha(0.3f);
            } else {
                if (hasLongPressing()) {
                }
                this.itemView.setAlpha(1.0f);
            }
        }
        layoutParams.bottomMargin = TEXT_CENTER_BOTTOM_MARGIN_NORMAL;
        this.text_center_cv.setLayoutParams(layoutParams);
        if (isLongPressing) {
            ViewGroup.LayoutParams layoutParams4 = this.text_bottom_rect_iv.getLayoutParams();
            layoutParams4.height += DensityUtil.dip2px(15.0f);
            this.text_bottom_rect_iv.setLayoutParams(layoutParams4);
            ViewGroup.LayoutParams layoutParams5 = this.text_bottom_right_rect_iv.getLayoutParams();
            layoutParams5.height += DensityUtil.dip2px(15.0f);
            this.text_bottom_right_rect_iv.setLayoutParams(layoutParams5);
            LayoutParams layoutParams1 = (LayoutParams) this.text_center_cv.getLayoutParams();
            layoutParams1.bottomMargin += DensityUtil.dip2px(15.0f);
            this.text_center_cv.setLayoutParams(layoutParams1);
        }
    }

    private boolean hasSelected() {
        for (VideoEditImageEntity entity : this.mHelper.getAllDatas()) {
            if (entity.isPin()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLongPressing() {
        for (VideoEditImageEntity entity : this.mHelper.getAllDatas()) {
            if (entity.getLongVideosModel() != null && entity.getLongVideosModel().isLongPressing()) {
                return true;
            }
        }
        return false;
    }

    private void refreshItemWidth(int width, int leftRightPaddingStatus) {
        int paddingLeft;
        int paddingRight;
        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        if (layoutParams.width != width) {
            layoutParams.width = width;
            this.itemView.setLayoutParams(layoutParams);
        }
        switch (leftRightPaddingStatus) {
            case 1:
                paddingLeft = VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                paddingRight = 0;
                break;
            case 2:
                paddingLeft = 0;
                paddingRight = VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                break;
            case 3:
                paddingLeft = VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                paddingRight = VideoEditImageHolder.LEFT_OR_RIGHT_SPACE;
                break;
            default:
                paddingLeft = 0;
                paddingRight = 0;
                break;
        }
        this.itemView.setPadding(paddingLeft, 0, paddingRight, 0);
        refreshBottomMarginLeftAndRight(paddingLeft, paddingRight);
    }

    private void refreshItemHeight(int height) {
        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        if (layoutParams.height != height) {
            layoutParams.height = height;
            this.itemView.setLayoutParams(layoutParams);
        }
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131691707:
            case 2131691708:
            case 2131691710:
                this.mHelper.onTextEditClick(getAdapterPosition());
                return;
            case 2131691709:
            case 2131691711:
                this.mHelper.onTextDeleteClick(getAdapterPosition());
                return;
            default:
                return;
        }
    }

    private void changeToLongPressedStatus() {
        RecyclerView parent = (RecyclerView) this.itemView.getParent();
        if (parent != null) {
            ViewGroup.LayoutParams layoutParams2 = parent.getLayoutParams();
            layoutParams2.height += DensityUtil.dip2px(15.0f);
            parent.setLayoutParams(layoutParams2);
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            parent.getAdapter().notifyItemRangeChanged(firstVisibleItemPosition, (layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1);
        }
    }

    private void restoreFromLongPressedStatus() {
        RecyclerView parent = (RecyclerView) this.itemView.getParent();
        if (parent != null) {
            ViewGroup.LayoutParams layoutParams2 = parent.getLayoutParams();
            layoutParams2.height -= DensityUtil.dip2px(15.0f);
            parent.setLayoutParams(layoutParams2);
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (!parent.isComputingLayout()) {
                parent.getAdapter().notifyItemRangeChanged(firstVisibleItemPosition, (lastVisibleItemPosition - firstVisibleItemPosition) + 1);
            }
        }
    }

    private float dealMoveX(float move_x, boolean isLeft) {
        if (this.SLIDER_DELTA == StaticLayoutUtil.DefaultSpacingadd) {
            return move_x;
        }
        return this.SLIDER_DELTA * ((float) ((int) (move_x / this.SLIDER_DELTA)));
    }

    public void beginAnimation() {
        ObjectAnimator leftSlideAlphaAnim = ObjectAnimator.ofFloat(this.text_left_slide_iv, "alpha", new float[]{StaticLayoutUtil.DefaultSpacingadd, 1.0f});
        ObjectAnimator rightSlideAlphaAnim = ObjectAnimator.ofFloat(this.text_right_slide_iv, "alpha", new float[]{StaticLayoutUtil.DefaultSpacingadd, 1.0f});
        ObjectAnimator textTopAlphaAnim = ObjectAnimator.ofFloat(this.text_top_rl, "alpha", new float[]{StaticLayoutUtil.DefaultSpacingadd, 1.0f});
        ValueAnimator.ofInt(new int[]{CONTENT_MIN_HEIGHT, CONTENT_MAX_HEIGHT}).addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                VideoEditTextHolder.this.text_center_cv.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                VideoEditTextHolder.this.text_center_cv.requestLayout();
                if (valueAnimator.getAnimatedFraction() >= 1.0f && VideoEditTextHolder.this.mAnimationEndListener != null) {
                    VideoEditTextHolder.this.mAnimationEndListener.endListener();
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(new Animator[]{leftSlideAlphaAnim, rightSlideAlphaAnim, textTopAlphaAnim, valueAnimator});
        animatorSet.playTogether(new Animator[]{leftSlideAlphaAnim, rightSlideAlphaAnim, textTopAlphaAnim});
        animatorSet.setDuration(100);
        animatorSet.start();
    }

    public void finalAnimation() {
        ObjectAnimator leftSlideAlphaAnim = ObjectAnimator.ofFloat(this.text_left_slide_iv, "alpha", new float[]{1.0f, StaticLayoutUtil.DefaultSpacingadd});
        ObjectAnimator rightSlideAlphaAnim = ObjectAnimator.ofFloat(this.text_right_slide_iv, "alpha", new float[]{1.0f, StaticLayoutUtil.DefaultSpacingadd});
        ObjectAnimator textTopAlphaAnim = ObjectAnimator.ofFloat(this.text_top_rl, "alpha", new float[]{1.0f, StaticLayoutUtil.DefaultSpacingadd});
        ValueAnimator.ofInt(new int[]{CONTENT_MAX_HEIGHT, CONTENT_MIN_HEIGHT}).addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                VideoEditTextHolder.this.text_center_cv.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                VideoEditTextHolder.this.text_center_cv.requestLayout();
                if (valueAnimator.getAnimatedFraction() >= 1.0f && VideoEditTextHolder.this.mAnimationEndListener != null) {
                    VideoEditTextHolder.this.mAnimationEndListener.endListener();
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(new Animator[]{leftSlideAlphaAnim, rightSlideAlphaAnim, textTopAlphaAnim, valueAnimator});
        animatorSet.playTogether(new Animator[]{leftSlideAlphaAnim, rightSlideAlphaAnim, textTopAlphaAnim});
        animatorSet.setDuration(100);
        animatorSet.start();
    }

    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.mAnimationEndListener = animationEndListener;
    }
}