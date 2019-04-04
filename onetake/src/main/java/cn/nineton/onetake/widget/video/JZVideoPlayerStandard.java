package cn.nineton.onetake.widget.video;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.interfaces.AnimatorEndListener;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.widgets.RelativeLayout.MeasureSizeRelativeLayout;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerStandard$.Lambda.1;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.listener.AnimatorEndListener;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.widget.MeasureSizeRelativeLayout;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class JZVideoPlayerStandard extends JZVideoPlayer {
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;
    public ImageView backButton;
    public ProgressBar bottomProgressBar;
    private float distanceX = StaticLayoutUtil.DefaultSpacingadd;
    private View gradient_view;
    private boolean hasRequestShowFrame = false;
    public ProgressBar loadingProgressBar;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    private float moveX = StaticLayoutUtil.DefaultSpacingadd;
    private OnRootMeasure onRootMeasure;
    protected OnSurfaceClick onSurfaceClick;
    public MeasureSizeRelativeLayout player_root_view;
    private boolean showLoadingAndBottomProgress = true;
    private int startPro = 0;
    private float startX = StaticLayoutUtil.DefaultSpacingadd;
    public SimpleDraweeView thumbImageView;
    private TextView tv_duration;

    public interface OnSurfaceClick {
        void onClick();

        void onDoubleClick();

        void onNoWifiClick();
    }

    public interface OnRootMeasure {
        void onMeasure(int i, int i2);
    }

    public class DismissControlViewTimerTask extends TimerTask {
        public void run() {
            JZVideoPlayerStandard.this.dissmissControlView();
        }
    }

    class GestureListener extends SimpleOnGestureListener {
        GestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                if (JZVideoPlayerManager.getCurrentJzvd().currentScreen == 2) {
                    JZVideoPlayerStandard.this.onClickUiToggle();
                } else if (JZVideoPlayerStandard.this.onSurfaceClick != null) {
                    JZVideoPlayerStandard.this.onSurfaceClick.onClick();
                }
            }
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (!(JZVideoPlayerManager.getCurrentJzvd() == null || JZVideoPlayerManager.getCurrentJzvd().currentScreen == 2 || JZVideoPlayerStandard.this.onSurfaceClick == null)) {
                JZVideoPlayerStandard.this.onSurfaceClick.onDoubleClick();
            }
            return true;
        }
    }

    public void setOnRootMeasure(OnRootMeasure onRootMeasure) {
        this.onRootMeasure = onRootMeasure;
    }

    public JZVideoPlayerStandard(Context context) {
        super(context);
    }

    public JZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context) {
        super.init(context);
        this.bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress);
        this.backButton = (ImageView) findViewById(R.id.back);
        this.thumbImageView = (SimpleDraweeView) findViewById(R.id.thumb);
        this.gradient_view = findViewById(R.id.gradient_view);
        this.tv_duration = (TextView) findViewById(R.id.tv_duration);
        this.player_root_view = (MeasureSizeRelativeLayout) findViewById(R.id.player_root_view);
        this.player_root_view.setOnSizeChangeListener(new MeasureSizeRelativeLayout.OnSizeChange() {
            @Override
            public void size(int i, int i2, int i3, int i4) {
                lambda$init$0(i,i2,i3,i4);
            }
        });//1.lambdaFactory$(this));
        this.thumbImageView.setHierarchy(new GenericDraweeHierarchyBuilder(App.getResource()).build());
        this.loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        this.thumbImageView.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        final GestureDetectorCompat tapDetector = new GestureDetectorCompat(getContext(), new GestureListener());
        this.textureViewContainer.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (JZVideoPlayerStandard.this.currentScreen == 2 && JZVideoPlayerStandard.this.progressBar != null) {
                            JZVideoPlayerStandard.this.startX = event.getRawX();
                            JZVideoPlayerStandard.this.startPro = JZVideoPlayerStandard.this.progressBar.getProgress();
                            break;
                        }
                    case 1:
                    case 3:
                        if (JZVideoPlayerStandard.this.mTouchingProgressBar) {
                            JZVideoPlayerStandard.this.mTouchingProgressBar = false;
                            JZVideoPlayerStandard.this.hasRequestShowFrame = false;
                            JZVideoPlayerStandard.this.startX = StaticLayoutUtil.DefaultSpacingadd;
                            JZVideoPlayerStandard.this.startPro = 0;
                            JZVideoPlayerStandard.this.startProgressTimer();
                            JZVideoPlayerStandard.this.startDismissControlViewTimer();
                            if (JZVideoPlayerStandard.this.currentScreen == 2 && JZVideoPlayerStandard.this.flFrameAt.getVisibility() == VISIBLE) {
                                JZVideoPlayerStandard.this.flFrameAt.setVisibility(GONE);
                            }
                            if (JZVideoPlayerStandard.this.currentState == 3 || JZVideoPlayerStandard.this.currentState == 5) {
                                JZMediaManager.instance().mediaPlayer.seekTo((JZVideoPlayerStandard.this.progressBar.getProgress() * JZVideoPlayerStandard.this.getDuration()) / 100);
                                break;
                            }
                        }
                        break;
                    case 2:
                        if (JZVideoPlayerStandard.this.currentScreen == 2) {
                            float pro;
                            JZVideoPlayerStandard.this.moveX = event.getRawX();
                            JZVideoPlayerStandard.this.distanceX = JZVideoPlayerStandard.this.moveX - JZVideoPlayerStandard.this.startX;
                            if (JZVideoPlayerStandard.this.getResources().getConfiguration().orientation == 1) {
                                pro = ((JZVideoPlayerStandard.this.distanceX * 100.0f) * 1.0f) / ((float) JZVideoPlayerStandard.this.mScreenWidth);
                            } else {
                                pro = ((JZVideoPlayerStandard.this.distanceX * 100.0f) * 1.0f) / ((float) JZVideoPlayerStandard.this.mScreenHeight);
                            }
                            LogUtil.d("slim", "startX:" + JZVideoPlayerStandard.this.startX + "  moveX:" + JZVideoPlayerStandard.this.moveX + "  distanceX:" + JZVideoPlayerStandard.this.distanceX + "  pro:" + pro);
                            if (Math.abs(pro) > 1.0f) {
                                if (!JZVideoPlayerStandard.this.mTouchingProgressBar) {
                                    JZVideoPlayerStandard.this.mTouchingProgressBar = true;
                                }
                                JZVideoPlayerStandard.this.cancelDismissControlViewTimer();
                                if (JZVideoPlayerStandard.this.currentState != 7) {
                                    if (JZVideoPlayerStandard.this.bottomContainer.getVisibility() != VISIBLE) {
                                        JZVideoPlayerStandard.this.onClickUiToggle();
                                        JZVideoPlayerStandard.this.cancelProgressTimer();
                                    }
                                    if (!JZVideoPlayerStandard.this.hasRequestShowFrame) {
                                        JZVideoPlayerStandard.this.showSampleImages();
                                        JZVideoPlayerStandard.this.hasRequestShowFrame = true;
                                    }
                                    JZVideoPlayerStandard.this.progressBar.setProgress((int) (((float) JZVideoPlayerStandard.this.startPro) + pro));
                                    JZVideoPlayerStandard.this.invalidateFrame((int) (((float) JZVideoPlayerStandard.this.startPro) + pro));
                                    break;
                                }
                            }
                        }
                        break;
                }
                tapDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private /* synthetic */ void lambda$init$0(int height, int width, int oldw, int oldh) {
        if (oldh <= 0 && oldh <= 0 && this.onRootMeasure != null) {
            this.onRootMeasure.onMeasure(width, height);
        }
    }

    public void setUp(LinkedHashMap urlMap, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(urlMap, defaultUrlMapIndex, screen, objects);
        if (objects.length != 0) {
            if (this.currentScreen == 2) {
                this.backButton.setVisibility(VISIBLE);
            } else if (this.currentScreen == 0 || this.currentScreen == 1) {
                this.backButton.setVisibility(GONE);
            }
        }
    }

    public void setShowLoadingAndBottomProgress(boolean showLoadingAndBottomProgress) {
        this.showLoadingAndBottomProgress = showLoadingAndBottomProgress;
        if (!showLoadingAndBottomProgress) {
            this.bottomProgressBar.setVisibility(GONE);
            this.loadingProgressBar.setVisibility(GONE);
            this.ignoreNetState = true;
        }
    }

    public int getLayoutId() {
        return 2130968885;
    }

    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
        startDismissControlViewTimer();
    }

    public void onStatePreparingChangingUrl(int urlMapIndex, int seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
        if (this.showLoadingAndBottomProgress) {
            this.loadingProgressBar.setVisibility(VISIBLE);
        }
        this.startButton.setVisibility(INVISIBLE);
    }

    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingShow();
        startDismissControlViewTimer();
    }

    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        this.bottomProgressBar.setProgress(100);
    }

    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == 2131691375) {
            if (this.urlMap != null && !TextUtils.isEmpty(JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex)) && this.currentState == 0) {
                if (JZUtils.isWifiConnected(getContext())) {
                    if (this.onSurfaceClick != null) {
                        this.onSurfaceClick.onClick();
                    }
                } else if (this.onSurfaceClick != null) {
                    this.onSurfaceClick.onNoWifiClick();
                }
            }
        } else if (i == 2131691382) {
            JZVideoPlayer.backPress();
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if (this.currentState == 3) {
            startDismissControlViewTimer();
        } else {
            startDismissControlViewTimer();
        }
    }

    public void onClickUiToggle() {
        if (this.bottomContainer.getVisibility() == VISIBLE && this.bottomContainer.getAlpha() == 1.0f) {
            alphaAnimate(this.bottomContainer, StaticLayoutUtil.DefaultSpacingadd, 200, null);
            alphaAnimate(this.startButton, StaticLayoutUtil.DefaultSpacingadd, 200, null);
            alphaAnimate(this.bottomProgressBar, 1.0f, 200, null);
            alphaAnimate(this.gradient_view, StaticLayoutUtil.DefaultSpacingadd, 200, null);
            return;
        }
        alphaAnimate(this.bottomContainer, 1.0f, 200, null);
        alphaAnimate(this.startButton, 1.0f, 200, null);
        alphaAnimate(this.bottomProgressBar, StaticLayoutUtil.DefaultSpacingadd, 200, null);
        alphaAnimate(this.gradient_view, 1.0f, 200, null);
        startDismissControlViewTimer();
    }

    private void alphaAnimate(final View view, final float alpha, int duration, AnimatorEndListener listener) {
        if (view.getVisibility() != VISIBLE) {
            view.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            view.setVisibility(VISIBLE);
        }
        ViewPropertyAnimator.animate(view).alpha(alpha).setDuration((long) duration).setInterpolator(new LinearInterpolator()).setListener(new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (alpha == StaticLayoutUtil.DefaultSpacingadd) {
                    view.setVisibility(INVISIBLE);
                }
            }
        }).start();
    }

    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
        if (progress != 0) {
            this.bottomProgressBar.setProgress(progress);
        }
    }

    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
        if (bufferProgress != 0) {
            this.bottomProgressBar.setSecondaryProgress(bufferProgress);
        }
    }

    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        this.bottomProgressBar.setProgress(0);
        this.bottomProgressBar.setSecondaryProgress(0);
    }

    public void changeUiToNormal() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisibility(4, 4, 4, 0, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisibility(4, 4, 4, 0, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPreparing() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisibility(4, 4, 0, 0, 4, true);
                return;
            case 2:
                setAllControlsVisibility(4, 4, 0, 0, 4, true);
                return;
            default:
                return;
        }
    }

    public void onVideoRendingStart() {
        super.onVideoRendingStart();
        if (!this.mTouchingProgressBar) {
            setAllControlsVisibility(4, 4, 4, 4, 0);
            startDismissControlViewTimer();
        }
    }

    public void changeUiToPlayingShow() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisibility(0, 4, 4, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisibility(4, 4, 4, 4, 0);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPauseShow() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                if (JZUtils.isWifiConnected(getContext())) {
                    setAllControlsVisibility(0, 0, 4, 4, 4);
                } else {
                    setAllControlsVisibility(0, 0, 4, 0, 4);
                }
                updateStartImage();
                return;
            case 2:
                setAllControlsVisibility(0, 0, 4, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToComplete() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisibility(4, 4, 4, 0, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisibility(4, 4, 4, 0, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToError() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisibility(4, 4, 4, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisibility(4, 4, 4, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void setAllControlsVisibility(int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro) {
        setAllControlsVisibility(bottomCon, startBtn, loadingPro, thumbImg, bottomPro, false);
    }

    public void setAllControlsVisibility(int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, boolean isPrepare) {
        if (!(this.isVideoRendingStart || this.currentScreen == 2 || this.currentScreen == 3)) {
            thumbImg = 0;
        }
        if (this.currentScreen != 2) {
            this.bottomContainer.setVisibility(INVISIBLE);
            if (this.showLoadingAndBottomProgress) {
                this.bottomProgressBar.setVisibility(VISIBLE);
            }
            if (JZUtils.isWifiConnected(App.getContext()) || isPrepare) {
                this.startButton.setVisibility(INVISIBLE);
            } else {
                this.startButton.setVisibility(thumbImg);
            }
            this.backButton.setVisibility(INVISIBLE);
            this.gradient_view.setVisibility(INVISIBLE);
        } else {
            this.bottomContainer.setVisibility(bottomCon);
            if (this.showLoadingAndBottomProgress) {
                this.bottomProgressBar.setVisibility(bottomPro);
            }
            this.startButton.setVisibility(startBtn);
            this.backButton.setVisibility(VISIBLE);
            this.gradient_view.setVisibility(bottomCon);
        }
        if (this.showLoadingAndBottomProgress) {
            this.loadingProgressBar.setVisibility(loadingPro);
        }
        this.thumbImageView.setVisibility(thumbImg);
        if (thumbImg != 0 || JZUtils.isWifiConnected(getContext()) || isPrepare || !this.showLoadingAndBottomProgress) {
            if (this.showLoadingAndBottomProgress) {
                this.bottomProgressBar.setVisibility(VISIBLE);
            }
            this.tv_duration.setVisibility(GONE);
            return;
        }
        this.tv_duration.setText(JZUtils.stringForTime(this.vDuration * 1000));
        this.tv_duration.setVisibility(VISIBLE);
        this.bottomProgressBar.setVisibility(GONE);
    }

    public void updateStartImage() {
        if (this.currentState == 3) {
            this.startButton.setImageResource(R.drawable.icon_60_video_pause);
        } else {
            this.startButton.setImageResource(R.drawable.icon_60_video_play);
        }
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        this.mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(this.mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (this.mDismissControlViewTimerTask != null) {
            this.mDismissControlViewTimerTask.cancel();
        }
    }

    public void checkPauseState() {
        if (this.tv_duration != null && this.startButton != null) {
            this.tv_duration.setVisibility(VISIBLE);
            this.startButton.setVisibility(VISIBLE);
            this.bottomProgressBar.setProgress(0);
        }
    }

    public void onAutoCompletion() {
        super.onAutoCompletion();
    }

    public void onCompletion(int i) {
        super.onCompletion(3);
        this.bottomProgressBar.setVisibility(GONE);
        cancelDismissControlViewTimer();
    }

    public void dissmissControlView() {
        if (this.currentState != 0 && this.currentState != 7 && this.currentState != 6 && getContext() != null && (getContext() instanceof Activity)) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    JZVideoPlayerStandard.this.alphaAnimate(JZVideoPlayerStandard.this.bottomContainer, StaticLayoutUtil.DefaultSpacingadd, 200, null);
                    JZVideoPlayerStandard.this.alphaAnimate(JZVideoPlayerStandard.this.startButton, StaticLayoutUtil.DefaultSpacingadd, 200, null);
                    JZVideoPlayerStandard.this.alphaAnimate(JZVideoPlayerStandard.this.gradient_view, StaticLayoutUtil.DefaultSpacingadd, 200, null);
                    if (JZVideoPlayerStandard.this.currentScreen != 3 && JZVideoPlayerStandard.this.showLoadingAndBottomProgress) {
                        JZVideoPlayerStandard.this.alphaAnimate(JZVideoPlayerStandard.this.bottomProgressBar, 1.0f, 200, null);
                    }
                }
            });
        }
    }

    public void setOnSurfaceClick(OnSurfaceClick click) {
        this.onSurfaceClick = click;
        setThisSurfaceClick(this.onSurfaceClick);
    }

    public void setProBarsBottomMargins(int margin) {
        if (this.currentScreen == 2 && this.bottomContainer != null && this.bottomProgressBar != null) {
            LayoutParams layoutParams = (LayoutParams) this.bottomProgressBar.getLayoutParams();
            layoutParams.bottomMargin = margin;
            this.bottomProgressBar.setLayoutParams(layoutParams);
            LayoutParams params = (LayoutParams) this.bottomContainer.getLayoutParams();
            params.bottomMargin = margin;
            this.bottomContainer.setLayoutParams(params);
            LayoutParams layoutParams1 = (LayoutParams) this.gradient_view.getLayoutParams();
            layoutParams1.bottomMargin = margin;
            this.gradient_view.setLayoutParams(layoutParams1);
        }
    }
}