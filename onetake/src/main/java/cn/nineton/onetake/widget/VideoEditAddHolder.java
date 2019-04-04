package cn.nineton.onetake.widget;

import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.WeakHandler;

//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.custom.CircleCoverView;
//import com.blink.academy.onetake.custom.WeakHandler;
//import com.blink.academy.onetake.support.LongPressTouchListener;
//import com.blink.academy.onetake.support.LongPressTouchListener.CustomPressCallback;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
import java.math.BigDecimal;
import java.util.List;

public class VideoEditAddHolder extends VideoEditViewHolder implements Callback {
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_LEFT = 3;
    private static final int MSG_SLIDE_LEFT_AUTO_SCROLL_TO_RIGHT = 4;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_LEFT = 1;
    private static final int MSG_SLIDE_RIGHT_AUTO_SCROLL_TO_RIGHT = 2;
    private static final String TAG = VideoEditAddHolder.class.getSimpleName();
    private int DELAY_TIME = 50;
    private int IMAGE_UNIT_WIDTH = VideoEditHelper.IMAGE_UNIT_WIDTH;
    private float SLIDER_DELTA = (((float) this.IMAGE_UNIT_WIDTH) * VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue());
    private long lastDealTime;
    private WeakHandler mHandler;
    private float mLastDownMoveX;
    private OnTouchListener mLeftImageOnTouchListener = new OnTouchListener() {
        int down_leftMargin;
        int down_width;
        float down_x;
        float down_y;
        float last_move_x;

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & 255) {
                case 0:
                    ViewParent itemViewParent = VideoEditAddHolder.this.itemView.getParent();
                    if (itemViewParent != null) {
                        itemViewParent.requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.last_move_x = StaticLayoutUtil.DefaultSpacingadd;
                        VideoEditAddHolder.this.mHelper.onStartSlideVideoLeft(VideoEditAddHolder.this.getAdapterPosition() + 1);
                        break;
                    }
                    return false;
                case 1:
                case 3:
                    VideoEditAddHolder.this.mLastDownMoveX = (float) (VideoEditAddHolder.this.mScreenWidth / 2);
                    VideoEditAddHolder.this.mHandler.removeMessages(3);
                    VideoEditAddHolder.this.mHandler.removeMessages(4);
                    ViewParent parent = VideoEditAddHolder.this.itemView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                        VideoEditAddHolder.this.mHelper.onSlideVideoLeftEnd(VideoEditAddHolder.this.getAdapterPosition() + 1);
                        break;
                    }
                    return false;
                case 2:
                    float rawX = event.getRawX();
                    VideoEditAddHolder.this.mLastDownMoveX = rawX;
                    if (!VideoEditAddHolder.this.isInStartArea()) {
                        if (!VideoEditAddHolder.this.isInEndArea()) {
                            VideoEditAddHolder.this.mHandler.removeMessages(4);
                            VideoEditAddHolder.this.mHandler.removeMessages(3);
                            float move_y = event.getRawY() - this.down_y;
                            float move_x = VideoEditAddHolder.this.dealMoveX(rawX - this.down_x, true);
                            float result_move_x = BigDecimal.valueOf((double) move_x).subtract(BigDecimal.valueOf((double) this.last_move_x)).setScale(1, 4).floatValue();
                            this.last_move_x = move_x;
                            int moveTime = (int) (result_move_x / VideoEditAddHolder.this.SLIDER_DELTA);
                            if (moveTime != 0) {
                                VideoEditAddHolder.this.mHelper.onSlideVideoLeft(VideoEditAddHolder.this.getAdapterPosition() + 1, moveTime);
                                break;
                            }
                        }
                        VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(3, (long) VideoEditAddHolder.this.DELAY_TIME);
                        break;
                    }
                    VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(4, (long) VideoEditAddHolder.this.DELAY_TIME);
                    break;
            }
            return true;
        }
    };
    private LongPressTouchListener.CustomPressCallback mLeftImagePressTouchListener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            VideoEditAddHolder.this.mHelper.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            ViewParent itemViewParent = VideoEditAddHolder.this.itemView.getParent();
            if (itemViewParent != null) {
                itemViewParent.requestDisallowInterceptTouchEvent(true);
                VideoEditAddHolder.this.mHelper.onStartSlideVideoLeft(VideoEditAddHolder.this.getAdapterPosition() + 1);
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            VideoEditAddHolder.this.mLastDownMoveX = (float) (VideoEditAddHolder.this.mScreenWidth / 2);
            VideoEditAddHolder.this.mHandler.removeMessages(3);
            VideoEditAddHolder.this.mHandler.removeMessages(4);
            ViewParent parent = VideoEditAddHolder.this.itemView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
                VideoEditAddHolder.this.mHelper.onSlideVideoLeftEnd(VideoEditAddHolder.this.getAdapterPosition() + 1);
                VideoEditAddHolder.this.mHelper.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            VideoEditAddHolder.this.mLastDownMoveX = event.getRawX();
            if (VideoEditAddHolder.this.isInStartArea()) {
                VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(4, (long) VideoEditAddHolder.this.DELAY_TIME);
            } else if (VideoEditAddHolder.this.isInEndArea()) {
                VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(3, (long) VideoEditAddHolder.this.DELAY_TIME);
            } else {
                VideoEditAddHolder.this.mHandler.removeMessages(1);
                VideoEditAddHolder.this.mHandler.removeMessages(2);
                if (moveTime != 0) {
                    VideoEditAddHolder.this.mHelper.onSlideVideoLeft(VideoEditAddHolder.this.getAdapterPosition() + 1, moveTime);
                }
            }
        }
    };
    private OnTouchListener mRightImageOnTouchListener = new OnTouchListener() {
        float down_width;
        float down_x;
        float down_y;
        float last_move_x;

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & 255) {
                case 0:
                    ViewParent itemViewParent = VideoEditAddHolder.this.itemView.getParent();
                    if (itemViewParent != null) {
                        itemViewParent.requestDisallowInterceptTouchEvent(true);
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.last_move_x = StaticLayoutUtil.DefaultSpacingadd;
                        VideoEditAddHolder.this.mHelper.onStartSlideVideoRight(VideoEditAddHolder.this.getAdapterPosition() - 1);
                        break;
                    }
                    return false;
                case 1:
                case 3:
                    VideoEditAddHolder.this.mLastDownMoveX = (float) (VideoEditAddHolder.this.mScreenWidth / 2);
                    VideoEditAddHolder.this.mHandler.removeMessages(1);
                    VideoEditAddHolder.this.mHandler.removeMessages(2);
                    ViewParent parent = VideoEditAddHolder.this.itemView.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                        VideoEditAddHolder.this.mHelper.onSlideVideoRightEnd(VideoEditAddHolder.this.getAdapterPosition() - 1);
                        break;
                    }
                    return false;
                case 2:
                    float rawX = event.getRawX();
                    VideoEditAddHolder.this.mLastDownMoveX = rawX;
                    if (!VideoEditAddHolder.this.isInStartArea()) {
                        if (!VideoEditAddHolder.this.isInEndArea()) {
                            VideoEditAddHolder.this.mHandler.removeMessages(1);
                            VideoEditAddHolder.this.mHandler.removeMessages(2);
                            float move_y = event.getRawY() - this.down_y;
                            float move_x = VideoEditAddHolder.this.dealMoveX(rawX - this.down_x, false);
                            float result_move_x = BigDecimal.valueOf((double) move_x).subtract(BigDecimal.valueOf((double) this.last_move_x)).setScale(1, 4).floatValue();
                            this.last_move_x = move_x;
                            int moveTime = (int) (result_move_x / VideoEditAddHolder.this.SLIDER_DELTA);
                            if (moveTime != 0) {
                                VideoEditAddHolder.this.mHelper.onSlideVideoRight(VideoEditAddHolder.this.getAdapterPosition() - 1, moveTime);
                                break;
                            }
                        }
                        VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(1, (long) VideoEditAddHolder.this.DELAY_TIME);
                        break;
                    }
                    VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(2, (long) VideoEditAddHolder.this.DELAY_TIME);
                    break;
            }
            return true;
        }
    };
    private LongPressTouchListener.CustomPressCallback mRightPressTouchlistener = new LongPressTouchListener.CustomPressCallback() {
        public void onActionDown() {
            super.onActionDown();
            VideoEditAddHolder.this.mHelper.onShowSlideText();
        }

        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
            if (VideoEditAddHolder.this.itemView.getParent() != null) {
                VideoEditAddHolder.this.itemView.getParent().requestDisallowInterceptTouchEvent(true);
                VideoEditAddHolder.this.mHelper.onStartSlideVideoRight(VideoEditAddHolder.this.getAdapterPosition() - 1);
            }
        }

        public void onActionUpOrCancel(MotionEvent event, boolean isLongPress) {
            super.onActionUpOrCancel(event, isLongPress);
            VideoEditAddHolder.this.mLastDownMoveX = (float) (VideoEditAddHolder.this.mScreenWidth / 2);
            VideoEditAddHolder.this.mHandler.removeMessages(1);
            VideoEditAddHolder.this.mHandler.removeMessages(2);
            if (VideoEditAddHolder.this.itemView.getParent() != null) {
                VideoEditAddHolder.this.itemView.getParent().requestDisallowInterceptTouchEvent(false);
                VideoEditAddHolder.this.mHelper.onSlideVideoRightEnd(VideoEditAddHolder.this.getAdapterPosition() - 1);
                VideoEditAddHolder.this.mHelper.onHideSlideText();
            }
        }

        public void onActionMove(MotionEvent event, int moveTime) {
            super.onActionMove(event, moveTime);
            VideoEditAddHolder.this.mLastDownMoveX = event.getRawX();
            if (VideoEditAddHolder.this.isInStartArea()) {
                VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(2, (long) VideoEditAddHolder.this.DELAY_TIME);
            } else if (VideoEditAddHolder.this.isInEndArea()) {
                VideoEditAddHolder.this.mHandler.sendEmptyMessageDelayed(1, (long) VideoEditAddHolder.this.DELAY_TIME);
            } else {
                VideoEditAddHolder.this.mHandler.removeMessages(1);
                VideoEditAddHolder.this.mHandler.removeMessages(2);
                if (moveTime != 0) {
                    VideoEditAddHolder.this.mHelper.onSlideVideoRight(VideoEditAddHolder.this.getAdapterPosition() - 1, moveTime);
                }
            }
        }
    };
    private int mScreenWidth;
    @BindView(R.id.video_edit_add_cover_ccv)
    CircleCoverView video_edit_add_cover_ccv;
    @BindView(R.id.video_edit_add_parent)
    FrameLayout video_edit_add_parent;
    @BindView(R.id.video_edit_bottom_line)
    View video_edit_bottom_line;
    @BindView(R.id.video_edit_bottom_line_bottom)
    View video_edit_bottom_line_bottom;
    @BindView(R.id.video_edit_left_iv_slide_view)
    View video_edit_left_iv_slide_view;
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

    public VideoEditAddHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
        ButterKnife.bind(this, itemView);
        this.video_edit_add_cover_ccv.setLeftRightSpace((float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, (float) VideoEditImageHolder.LEFT_OR_RIGHT_SPACE, VideoEditImageHolder.CIRCLE_COVER_RADIO, VideoEditImageHolder.CIRCLE_COVER_RADIO);
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
        this.mHandler = new WeakHandler((Callback) this);
    }

    public boolean handleMessage(Message message) {
        long currentTimeMillis;
        switch (message.what) {
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

    public void onBindViewHolder(int position) {
        VideoEditImageEntity videoEditImageEntity = (VideoEditImageEntity) this.mHelper.getAllDatas().get(position);
        refreshWhiteAreaView(videoEditImageEntity.getWhite_area_type());
        refreshItemWidth(videoEditImageEntity.getAdd_type_width());
        refreshSlideArrowView(videoEditImageEntity.isHideLeftArrowImage(), videoEditImageEntity.isHideRightArrowImage());
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
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

    private void refreshItemWidth(int width) {
        LayoutParams layoutParams = this.video_edit_add_parent.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        layoutParams.width = width;
        this.video_edit_add_parent.setLayoutParams(layoutParams);
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

    private float dealMoveX(float move_x, boolean isLeft) {
        if (this.SLIDER_DELTA == StaticLayoutUtil.DefaultSpacingadd) {
            return move_x;
        }
        return this.SLIDER_DELTA * ((float) ((int) (move_x / this.SLIDER_DELTA)));
    }
}