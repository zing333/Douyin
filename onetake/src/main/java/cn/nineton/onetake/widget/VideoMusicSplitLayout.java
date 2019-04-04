package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.util.ColorFilterUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
//import com.blink.academy.onetake.widgets.TextView.AvenirNextCondensedRegularTextView;
import java.text.DecimalFormat;

public class VideoMusicSplitLayout extends FrameLayout {
    float audioMaxTime;
    float audioShowMaxTime;
    float curVerse;
    @BindView(R.id.filter_loop_slider_root_rll)
    RelativeLayout filter_loop_slider_root_rll;
    private boolean isPlaying;
    private boolean isVideoTimeLessAudioTime;
    @BindView(R.id.left_iv_slide_view)
    View left_iv_slide_view;
    VideoPlayControlCallback mCallback;
    private int mHeight;
    private OnTouchListener mLeftImageOnTouchListener;
    private LongVideosModel mLongVideosModel;
    private int mWaveViewWidth;
    float max;
    int maxCenterAreaMarginLeft;
    private int maxLeftMargin;
    float min;
    private int moveAreaLeftMargin;
    private float[] points;
    private float[] verses;
    long videoSumTime;
    @BindView(R.id.video_music_split_awv)
    AudioWaveView video_music_split_awv;
    @BindView(R.id.video_music_split_bottom_parent)
    FrameLayout video_music_split_bottom_parent;
    @BindView(R.id.video_music_split_bottom_tv)
    TextView video_music_split_bottom_tv;
    @BindView(R.id.video_music_split_cancel_iv)
    ImageView video_music_split_cancel_iv;
    @BindView(R.id.video_music_split_center_area)
    View video_music_split_center_area;
    @BindView(R.id.video_music_split_confirm_iv)
    ImageView video_music_split_confirm_iv;
    @BindView(R.id.video_music_split_parent)
    FrameLayout video_music_split_parent;
    @BindView(R.id.video_music_split_play_iv)
    ImageView video_music_split_play_iv;
    @BindView(R.id.video_music_split_tv_parent)
    FrameLayout video_music_split_tv_parent;
    @BindView(R.id.view_music_split_cover_back1_view)
    View view_music_split_cover_back1_view;

    public interface VideoPlayControlCallback {
        void pauseVideo();
    }

    public void setLongVideosModel(LongVideosModel longVideosModel) {
        this.mLongVideosModel = longVideosModel;
        if (this.mLongVideosModel != null) {
            AudioTrackBean audioTrackBean = this.mLongVideosModel.getAudioTrackBean();
            if (audioTrackBean != null) {
                String poi = audioTrackBean.getPoi();
                if (TextUtil.isValidate(poi)) {
                    String[] split = poi.split(",");
                    int length = split.length;
                    if (length > 1) {
                        int i;
                        this.verses = new float[length];
                        for (i = 0; i < length; i++) {
                            this.verses[i] = Float.valueOf(split[i]).floatValue();
                        }
                        this.min = this.verses[0];
                        this.max = this.verses[length - 1];
                        float sum = this.max - this.min;
                        this.curVerse = this.mLongVideosModel.getTrueStartTime();
                        this.videoSumTime = this.mLongVideosModel.getVideoSumTime();
                        this.points = new float[length];
                        float result = this.curVerse - this.min;
                        setBottomText(sum - result);
                        float leftMargin = (result / sum) * ((float) this.maxLeftMargin);
                        this.audioMaxTime = sum / 0.7f;
                        reCalculateCenterAreaMaxValue(leftMargin);
                        setMoveAreaLeftMargin((int) leftMargin);
                        this.video_music_split_tv_parent.removeAllViews();
                        for (i = 0; i < length; i++) {
                            boolean z;
                            this.points[i] = ((this.verses[i] - this.min) / sum) * ((float) this.maxLeftMargin);
                            if (i == length - 1) {
                                z = true;
                            } else {
                                z = false;
                            }
                            addTopTextView(length, i, z, (int) this.points[i]);
                        }
                    }
                }
            }
        }
    }

    private void reCalculateCenterAreaMaxValue(float leftMargin) {
        this.audioShowMaxTime = this.audioMaxTime - this.curVerse;
        if (this.audioShowMaxTime * 1000.0f >= ((float) this.videoSumTime)) {
            this.isVideoTimeLessAudioTime = true;
        } else {
            this.isVideoTimeLessAudioTime = false;
        }
        this.maxCenterAreaMarginLeft = (int) ((((float) this.videoSumTime) / (this.audioShowMaxTime * 1000.0f)) * (((float) this.mWaveViewWidth) - leftMargin));
    }

    private void addTopTextView(int length, int i, boolean isMain, int leftMargin) {
        String content;
        AvenirNextCondensedRegularTextView textView = new AvenirNextCondensedRegularTextView(getContext());
        if (isMain) {
            content = App.getResource().getString(R.string.TEXT_AUDIO_EDIT_VERSE);
        } else {
            char c = (char) (i + 65);
            if (length > 2) {
                content = String.format("%s%s", new Object[]{App.getResource().getString(R.string.TEXT_AUDIO_EDIT_INTRO), String.valueOf(c)});
            } else {
                content = App.getResource().getString(R.string.TEXT_AUDIO_EDIT_INTRO);
            }
        }
        textView.setTextSize(1, 10.0f);
        textView.setText(content);
        textView.setGravity(17);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.color92));
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.leftMargin = leftMargin;
        textView.setLayoutParams(layoutParams);
        this.video_music_split_tv_parent.addView(textView);
    }

    public VideoMusicSplitLayout(@NonNull Context context) {
        this(context, null);
    }

    public VideoMusicSplitLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoMusicSplitLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isVideoTimeLessAudioTime = false;
        this.mLeftImageOnTouchListener = new OnTouchListener() {
            int down_leftMargin;
            float down_x;
            float down_y;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case 0:
                        this.down_x = event.getRawX();
                        this.down_y = event.getRawY();
                        this.down_leftMargin = ((LayoutParams) VideoMusicSplitLayout.this.filter_loop_slider_root_rll.getLayoutParams()).leftMargin;
                        if (VideoMusicSplitLayout.this.mCallback != null) {
                            VideoMusicSplitLayout.this.mCallback.pauseVideo();
                            break;
                        }
                        break;
                    case 2:
                        float move_y = event.getRawY() - this.down_y;
                        VideoMusicSplitLayout.this.dealMoveX(this.down_leftMargin, event.getRawX() - this.down_x);
                        if (VideoMusicSplitLayout.this.isPlaying()) {
                            VideoMusicSplitLayout.this.video_music_split_play_iv.callOnClick();
                            break;
                        }
                        break;
                }
                return true;
            }
        };
        this.isPlaying = false;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_video_music_split, this, true);
        ButterKnife.bind(this);
        this.left_iv_slide_view.setOnTouchListener(this.mLeftImageOnTouchListener);
        this.mWaveViewWidth = DensityUtil.getMetricsWidth(context) - DensityUtil.dip2px(100.0f);
        this.maxLeftMargin = (int) (((float) this.mWaveViewWidth) * 0.7f);
        this.video_music_split_awv.setColor(ContextCompat.getColor(getContext(), R.color.color66));
        LayoutParams layoutParams = (LayoutParams) this.video_music_split_bottom_parent.getLayoutParams();
        layoutParams.rightMargin = (this.mWaveViewWidth - this.maxLeftMargin) + layoutParams.rightMargin;
        ((LayoutParams) this.view_music_split_cover_back1_view.getLayoutParams()).width = layoutParams.rightMargin - DensityUtil.dip2px(50.0f);
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.video_music_split_play_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.video_music_split_play_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        setVisibility(GONE);
    }

    public void onPlaying(double percent, double sumPercent, boolean moreThanOnce) {
        int marginLeft = (int) (((double) this.maxCenterAreaMarginLeft) * percent);
        if (!this.isVideoTimeLessAudioTime) {
            setCenterAreaMarginLeft(DensityUtil.dip2px(10.0f) + marginLeft);
            if (this.moveAreaLeftMargin + marginLeft >= this.mWaveViewWidth && isPlaying()) {
                this.video_music_split_play_iv.callOnClick();
            }
        } else if (!moreThanOnce) {
            setCenterAreaMarginLeft(DensityUtil.dip2px(10.0f) + marginLeft);
        } else if (isPlaying() && this.mCallback != null) {
            this.mCallback.pauseVideo();
        }
        LogUtil.d(String.format("onPlaying   sumPercent : %s , percent : %s  ,  marginLeft : %s  ,  maxCenterAreaMarginLeft : %s  ", new Object[]{Double.valueOf(sumPercent), Double.valueOf(percent), Integer.valueOf(marginLeft), Integer.valueOf(this.maxCenterAreaMarginLeft)}));
    }

    private void setCenterAreaMarginLeft(int marginLeft) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.video_music_split_center_area.getLayoutParams();
        if (layoutParams.leftMargin != marginLeft) {
            layoutParams.leftMargin = marginLeft;
            this.video_music_split_center_area.setLayoutParams(layoutParams);
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
        refreshPlayBtnIcon();
    }

    public void setPlayBtnClickListener(OnClickListener listener) {
        this.video_music_split_play_iv.setOnClickListener(listener);
    }

    public void refreshPlayBtnIcon() {
        if (isPlaying()) {
            this.video_music_split_play_iv.setImageResource(R.drawable.icon_20_audio_stop);
            setCenterAreaMarginLeft(DensityUtil.dip2px(10.0f));
            this.video_music_split_center_area.setVisibility(VISIBLE);
            return;
        }
        this.video_music_split_play_iv.setImageResource(R.drawable.icon_20_audio_play);
        this.video_music_split_center_area.setVisibility(GONE);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWaveViewWidth = w - DensityUtil.dip2px(100.0f);
        this.mHeight = h;
        this.maxLeftMargin = (int) (((float) (w - DensityUtil.dip2px(100.0f))) * 0.7f);
    }

    private void dealMoveX(int down_leftMargin, float moveX) {
        int result = (int) (((float) down_leftMargin) + moveX);
        if (result < 0) {
            result = 0;
        } else if (result > this.maxLeftMargin) {
            result = this.maxLeftMargin;
        }
        setMoveAreaLeftMargin(result);
    }

    private void measureCurVerse(int leftMargin) {
        float percent = (((float) leftMargin) * 1.0f) / ((float) this.maxLeftMargin);
        if (this.verses != null) {
            this.curVerse = ((this.max - this.min) * percent) + this.min;
            this.mLongVideosModel.setCurStartTime(this.curVerse);
            LogUtil.d(String.format("measureCurVerse   curVerse : %s , max : %s , min : %s ", new Object[]{Float.valueOf(this.curVerse), Float.valueOf(this.max), Float.valueOf(this.min)}));
        }
    }

    private void setMoveAreaLeftMargin(int leftMargin) {
        leftMargin = checkIsNeedSlide(leftMargin);
        measureCurVerse(leftMargin);
        reCalculateCenterAreaMaxValue((float) leftMargin);
        LayoutParams layoutParams = (LayoutParams) this.filter_loop_slider_root_rll.getLayoutParams();
        if (leftMargin != layoutParams.leftMargin) {
            this.moveAreaLeftMargin = leftMargin;
            layoutParams.leftMargin = leftMargin;
            this.filter_loop_slider_root_rll.setLayoutParams(layoutParams);
            dealBottomAreaLeftMargin(leftMargin);
            checkBottomTextVisible();
        }
    }

    private void checkBottomTextVisible() {
        int width = this.video_music_split_bottom_parent.getWidth();
        LogUtil.d(String.format("checkBottomTextVisible : textWidth : %s , width : %s ", new Object[]{Float.valueOf(this.video_music_split_bottom_tv.getPaint().measureText((String) this.video_music_split_bottom_tv.getText())), Integer.valueOf(width)}));
        if (((float) width) > ((float) DensityUtil.dip2px(2.0f)) + this.video_music_split_bottom_tv.getPaint().measureText((String) this.video_music_split_bottom_tv.getText())) {
            if (this.video_music_split_bottom_parent.getVisibility() != VISIBLE) {
                this.video_music_split_bottom_parent.setVisibility(VISIBLE);
            }
        } else if (this.video_music_split_bottom_parent.getVisibility() == VISIBLE) {
            this.video_music_split_bottom_parent.setVisibility(INVISIBLE);
        }
    }

    private int checkIsNeedSlide(int leftMargin) {
        if (this.points == null) {
            return leftMargin;
        }
        int divide = this.maxLeftMargin / 20;
        for (float point : this.points) {
            if (((float) leftMargin) > point - ((float) divide) && ((float) leftMargin) < ((float) divide) + point) {
                return (int) point;
            }
        }
        return leftMargin;
    }

    private void dealBottomAreaLeftMargin(int moveX) {
        LayoutParams layoutParams = (LayoutParams) this.video_music_split_bottom_parent.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.dip2px(50.0f) + moveX;
        this.video_music_split_bottom_parent.setLayoutParams(layoutParams);
        float percent = (((float) moveX) * 1.0f) / ((float) this.maxLeftMargin);
        if (this.verses != null) {
            float sum = this.max - this.min;
            setBottomText(sum - (percent * sum));
        }
    }

    private void setBottomText(float result) {
        DecimalFormat df = new DecimalFormat("#0.0");
        this.video_music_split_bottom_tv.setText(String.format("%s\"", new Object[]{df.format((double) result)}));
    }

    public void saveCurVerse() {
        this.mLongVideosModel.setTrueStartTime(this.curVerse);
    }

    public void restoreTrueVerse() {
        this.mLongVideosModel.setTrueStartTime(this.mLongVideosModel.getTrueStartTime());
    }

    public void setCallback(VideoPlayControlCallback callback) {
        this.mCallback = callback;
    }
}