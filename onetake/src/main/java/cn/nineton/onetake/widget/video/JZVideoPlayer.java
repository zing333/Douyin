package cn.nineton.onetake.widget.video;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.controller.TimelineController;
//import com.blink.academy.onetake.fresco.FrescoUriUtil;
//import com.blink.academy.onetake.glide.CacheKeyGlideUrl;
//import com.blink.academy.onetake.glide.LoadLocalCatchFileTask;
//import com.blink.academy.onetake.glide.VideoListGlideModule;
//import com.blink.academy.onetake.support.callbacks.IControllerCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.StatusBarUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZResizeTextureView.ScaleType;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer$.Lambda.1;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerStandard.OnRootMeasure;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerStandard.OnSurfaceClick;
//import com.blink.academy.onetake.widgets.VideoView.TextureVideoView.MediaPlayerCallback;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.imageutils.JfifUtil;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.listener.LoadLocalFileCallback;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FrescoUriUtil;
import cn.nineton.onetake.util.LoadLocalCatchFileTask;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.StatusBarUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.widget.StaticLayoutUtil;
import cn.nineton.onetake.widget.TextureVideoView;

public abstract class JZVideoPlayer extends FrameLayout implements OnClickListener, OnSeekBarChangeListener {
    public static boolean ACTION_BAR_EXIST = true;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    public static final int CURRENT_STATE_ERROR = 7;
    public static final int CURRENT_STATE_NORMAL = 0;
    public static final int CURRENT_STATE_PAUSE = 5;
    public static final int CURRENT_STATE_PLAYING = 3;
    public static final int CURRENT_STATE_PREPARING = 1;
    public static final int CURRENT_STATE_PREPARING_CHANGING_URL = 2;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;
    protected static JZUserAction JZ_USER_EVENT = null;
    public static int NORMAL_ORIENTATION = 1;
    public static boolean SAVE_PROGRESS = true;
    public static final int SCREEN_LAYOUT_LIST = 1;
    public static final int SCREEN_LAYOUT_NORMAL = 0;
    public static final int SCREEN_WINDOW_FULLSCREEN = 2;
    public static final int SCREEN_WINDOW_TINY = 3;
    public static final String TAG = "JiaoZiVideoPlayer";
    public static boolean TOOL_BAR_EXIST = true;
    protected static Timer UPDATE_PROGRESS_TIMER = null;
    public static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";
    private static final int changeOrientationRes = 2130837899;
    private static int currentOrientation = 0;
    private static final int defaultOrientationRes = 2130837898;
    public static OnAudioFocusChangeListener onAudioFocusChangeListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case -2:
                    try {
                        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentState == 5 && JZMediaManager.instance().mediaPlayer != null && JZMediaManager.instance().mediaPlayer.isPlaying()) {
                            JZMediaManager.instance().mediaPlayer.pause();
                            return;
                        }
                        return;
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        return;
                    }
                case -1:
                    try {
                        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentState == 3 && JZMediaManager.instance().mediaPlayer != null && JZMediaManager.instance().mediaPlayer.isPlaying()) {
                            JZMediaManager.instance().mediaPlayer.pause();
                            return;
                        }
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    public ViewGroup bottomContainer;
    public int currentScreen = -1;
    public int currentState = -1;
    public TextView currentTimeTextView;
    protected int currentUrlMapIndex = 0;
    private Map<Integer, File> fileMap = new HashMap();
    FrameLayout flFrameAt;
    private int has_audio;
    public Map<String, String> headData;
    public int heightRatio = 0;
    protected boolean ignoreNetState = false;
    protected boolean isVideoRendingStart = false;
    protected ImageView ivChangeOritation;
    protected ImageView ivChangeOritation2;
    ImageView ivFrameAt;
    public boolean loop = false;
    protected AudioManager mAudioManager;
    private int mFlFrameAtWidth = 0;
    protected Handler mHandler;
    private OnMediaPlayCountListener mOnMediaPlayCountListener;
    private PlayCountingCallBack mPlayCountingCallBack;
    private Point mPoint = new Point();
    protected ProgressTimerTask mProgressTimerTask;
    private List<String> mSampleUrls;
    protected int mScreenHeight;
    protected int mScreenWidth;
    protected boolean mTouchingProgressBar;
    private TextureVideoView.MediaPlayerCallback mediaPlayerCallback;
    private MyOrientoinListener myOrientoinListener;
    public Object[] objects = null;
    private OnPlayerStateChange onPlayerStateChange;
    protected JZVideoPlayerStandard.OnSurfaceClick onSurfaceClick;
    int orientationClickState = -1;
    public SeekBar progressBar;
    private boolean resetActive = false;
    private ContentObserver rotationObserver;
    private JZResizeTextureView.ScaleType scaleType;
    private Drawable seekThumbNormal;
    private Drawable seekThumbPress;
    public int seekToInAdvance = 0;
    private int sourceWindowState = -1;
    public ImageView startButton;
    public ViewGroup textureViewContainer;
    TextView tvFrameCurrent;
    protected LinkedHashMap urlMap;
    protected int vDuration;
    public int widthRatio = 0;

    public interface PlayCountingCallBack {
        void UpdateCount();
    }

    public interface OnMediaPlayCountListener {
        void onEnd(int i);

        void onStart();
    }

    public interface OnPlayerStateChange {
        void onCloseFullscreen();

        void onPauseOrStop();

        void onPlaying();
    }

    class MyOrientoinListener extends OrientationEventListener {
        public MyOrientoinListener(Context context) {
            super(context);
        }

        public MyOrientoinListener(Context context, int rate) {
            super(context, rate);
        }

        public void onOrientationChanged(int orientation) {
            if (JZVideoPlayer.this.currentScreen == 2 && android.provider.Settings.System.getInt(JZVideoPlayer.this.getContext().getContentResolver(), "accelerometer_rotation", 0) == 1) {
                int screenOrientation = JZVideoPlayer.this.getResources().getConfiguration().orientation;
                if ((orientation < 0 || orientation >= 45) && orientation <= 315) {
                    if (orientation <= JfifUtil.MARKER_APP1 || orientation >= 315) {
                        if (orientation <= 45 || orientation >= 135) {
                            if (orientation <= 135 || orientation >= JfifUtil.MARKER_APP1) {
                            }
                        } else if (JZVideoPlayer.this.orientationClickState != 0) {
                            if (JZVideoPlayer.this.orientationClickState == 1) {
                                JZVideoPlayer.this.orientationClickState = -1;
                            }
                            if (screenOrientation != 8) {
                                LogUtil.d("JiaoZiVideoPlayer", "反向横屏");
                                JZVideoPlayer.currentOrientation = 1;
                                JZVideoPlayer.this.ivChangeOritation.setImageResource(JZVideoPlayer.changeOrientationRes);
                                JZUtils.setRequestedOrientation(JZVideoPlayer.this.getContext(), 8);
                            }
                        }
                    } else if (JZVideoPlayer.this.orientationClickState != 0) {
                        if (JZVideoPlayer.this.orientationClickState == 1) {
                            JZVideoPlayer.this.orientationClickState = -1;
                        }
                        if (screenOrientation != 0) {
                            LogUtil.d("JiaoZiVideoPlayer", "设置横屏");
                            JZVideoPlayer.currentOrientation = 1;
                            JZVideoPlayer.this.ivChangeOritation.setImageResource(JZVideoPlayer.changeOrientationRes);
                            JZUtils.setRequestedOrientation(JZVideoPlayer.this.getContext(), 0);
                        }
                    }
                } else if (JZVideoPlayer.this.orientationClickState != 1) {
                    if (JZVideoPlayer.this.orientationClickState == 0) {
                        JZVideoPlayer.this.orientationClickState = -1;
                    }
                    if (screenOrientation != 1 && orientation != 9) {
                        LogUtil.d("JiaoZiVideoPlayer", "设置竖屏");
                        JZVideoPlayer.currentOrientation = 0;
                        JZVideoPlayer.this.ivChangeOritation.setImageResource(JZVideoPlayer.defaultOrientationRes);
                        JZUtils.setRequestedOrientation(JZVideoPlayer.this.getContext(), 1);
                    }
                }
            }
        }
    }

    public class ProgressTimerTask extends TimerTask {
        public void run() {
            if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                if (JZVideoPlayerManager.getCurrentJzvd().currentState == 3 || JZVideoPlayerManager.getCurrentJzvd().currentState == 5) {
                    JZVideoPlayer.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                                int i;
                                int position = JZVideoPlayerManager.getCurrentJzvd().getCurrentPositionWhenPlaying();
                                int duration = JZVideoPlayerManager.getCurrentJzvd().getDuration();
                                int i2 = position * 100;
                                if (duration == 0) {
                                    i = 1;
                                } else {
                                    i = duration;
                                }
                                JZVideoPlayerManager.getCurrentJzvd().setProgressAndText(i2 / i, position, duration);
                            }
                        }
                    });
                }
            }
        }
    }

    public abstract int getLayoutId();

    public void setScaleType(JZResizeTextureView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public JZVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JZVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static void releaseAllVideos() {
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME > 300) {
            JZVideoPlayer currentJzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (currentJzvd != null) {
                if (currentJzvd.myOrientoinListener != null) {
                    currentJzvd.myOrientoinListener.disable();
                }
                if (currentJzvd.rotationObserver != null) {
                    currentJzvd.getContext().getContentResolver().unregisterContentObserver(currentJzvd.rotationObserver);
                    currentJzvd.rotationObserver = null;
                }
                if (currentJzvd.getContext().getResources().getConfiguration().orientation != 1) {
                    JZUtils.setRequestedOrientation(currentJzvd.getContext(), 1);
                }
            }
            JZVideoPlayerManager.completeAll();
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    public static boolean backPress() {
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME < 300) {
            return false;
        }
        if (JZVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            JZVideoPlayer jzVideoPlayer = JZVideoPlayerManager.getSecondFloor();
            jzVideoPlayer.onEvent(jzVideoPlayer.currentScreen == 2 ? 8 : 10);
            JZVideoPlayerManager.getFirstFloor().playOnThisJzvd();
            return true;
        } else if (JZVideoPlayerManager.getFirstFloor() == null) {
            return false;
        } else {
            if (JZVideoPlayerManager.getFirstFloor().currentScreen != 2 && JZVideoPlayerManager.getFirstFloor().currentScreen != 3) {
                return false;
            }
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            JZVideoPlayerManager.getCurrentJzvd().currentState = 0;
            JZVideoPlayerManager.getFirstFloor().clearFloatScreen();
            JZMediaManager.instance().releaseMediaPlayer();
            JZVideoPlayerManager.setFirstFloor(null);
            return true;
        }
    }

    public void showSupportActionBar(Context context) {
        if (this.sourceWindowState != 0) {
            if (ACTION_BAR_EXIST && JZUtils.getAppCompActivity(context) != null) {
                ActionBar ab = JZUtils.getAppCompActivity(context).getSupportActionBar();
                if (ab != null) {
                    ab.setShowHideAnimationEnabled(false);
                    ab.show();
                }
            }
            if (TOOL_BAR_EXIST && JZUtils.getAppCompActivity(context) != null) {
                StatusBarUtil.setColorNav(JZUtils.getAppCompActivity(context));
                JZUtils.getWindow(context).clearFlags(1024);
            }
        }
    }

    public void hideSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && JZUtils.getAppCompActivity(context) != null) {
            ActionBar ab = JZUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        if (TOOL_BAR_EXIST) {
            deleteAll((ViewGroup) JZUtils.getWindow(context).getDecorView());
            JZUtils.getWindow(context).setFlags(1024, 1024);
        }
    }

    private static void deleteAll(ViewGroup decorView) {
        View view = decorView.findViewById(R.id.home_decorView);
        if (view != null) {
            decorView.removeView(view);
            deleteAll(decorView);
        }
    }

    public static void goOnPlayOnResume() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == 5) {
                jzvd.onStatePlaying();
                JZMediaManager.instance().mediaPlayer.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState != 6 && jzvd.currentState != 0) {
                jzvd.onStatePause();
                JZMediaManager.instance().mediaPlayer.pause();
            }
        }
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        this.startButton = (ImageView) findViewById(R.id.start);
        this.progressBar = (SeekBar) findViewById(R.id.bottom_seek_progress);
        this.ivChangeOritation = (ImageView) findViewById(R.id.change_orientation);
        this.ivChangeOritation2 = (ImageView) findViewById(R.id.change_orientation2);
        this.currentTimeTextView = (TextView) findViewById(R.id.current);
        this.bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        this.textureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        this.flFrameAt = (FrameLayout) findViewById(R.id.fl_frame_at);
        this.ivFrameAt = (ImageView) findViewById(R.id.iv_frame_at);
        this.tvFrameCurrent = (TextView) findViewById(R.id.tv_frame_current);
        this.ivChangeOritation.setOnClickListener(this);
        this.startButton.setOnClickListener(this);
        this.progressBar.setOnSeekBarChangeListener(this);
        this.bottomContainer.setOnClickListener(this);
        this.mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        this.mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        this.mHandler = new Handler();
    }

    public void setvDuration(int vDuration) {
        this.vDuration = vDuration;
    }

    public void setUp(String url, int screen, Object... objects) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        setUp(map, 0, screen, objects);
    }

    public void setUp(LinkedHashMap urlMap, int defaultUrlMapIndex, int screen, Object... objects) {
        if (this.urlMap == null || TextUtils.isEmpty(JZUtils.getCurrentUrlFromMap(urlMap, this.currentUrlMapIndex)) || !TextUtils.equals(JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex), JZUtils.getCurrentUrlFromMap(urlMap, this.currentUrlMapIndex))) {
            this.urlMap = urlMap;
            this.currentUrlMapIndex = defaultUrlMapIndex;
            this.currentScreen = screen;
            this.objects = objects;
            this.headData = null;
            this.isVideoRendingStart = false;
            if (screen == 2) {
                this.myOrientoinListener = new MyOrientoinListener(getContext());
                this.rotationObserver = new ContentObserver(new Handler()) {
                    public void onChange(boolean selfChange) {
                        if (android.provider.Settings.System.getInt(JZVideoPlayer.this.getContext().getContentResolver(), "accelerometer_rotation", 0) != 1 && JZVideoPlayer.this.currentScreen == 2 && JZVideoPlayer.this.getResources().getConfiguration().orientation != 1) {
                            JZVideoPlayer.currentOrientation = 0;
                            JZVideoPlayer.this.ivChangeOritation.setImageResource(JZVideoPlayer.defaultOrientationRes);
                            JZUtils.setRequestedOrientation(JZVideoPlayer.this.getContext(), 1);
                        }
                    }
                };
            }
            onStateNormal();
        }
    }

    protected void setThisSurfaceClick(JZVideoPlayerStandard.OnSurfaceClick onSurfaceClick) {
        this.onSurfaceClick = onSurfaceClick;
    }

    public void onClick(View v) {
        int i = 0;
        int i2 = v.getId();
        if (i2 == R.id.start) {
            if (this.urlMap != null && !TextUtils.isEmpty(JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex))) {
                if (this.currentState == 0 || this.currentState == 7) {
                    boolean fullScreen = false;
                    if (JZUtils.toFullScreen) {
                        fullScreen = true;
                        JZUtils.toFullScreen = false;
                    }
                    if (JZUtils.isWifiConnected(getContext()) || fullScreen || JZUtils.singlePhotoDetail || this.ignoreNetState) {
                        startVideo();
                        if (this.currentState == 7) {
                            i = 1;
                        }
                        onEvent(i);
                    } else if (this.onSurfaceClick != null) {
                        this.onSurfaceClick.onNoWifiClick();
                    }
                } else if (this.currentState == 3) {
                    onEvent(3);
                    JZMediaManager.instance().mediaPlayer.pause();
                    onStatePause();
                } else if (this.currentState == 5) {
                    onEvent(4);
                    JZMediaManager.instance().mediaPlayer.start();
                    onStatePlaying();
                } else if (this.currentState == 6) {
                    onEvent(2);
                    startVideo();
                }
            }
        } else if (i2 != R.id.change_orientation) {
        } else {
            if (currentOrientation == 0) {
                JZUtils.setRequestedOrientation(getContext(), 0);
                currentOrientation = 1;
                this.orientationClickState = 1;
                this.ivChangeOritation.setImageResource(changeOrientationRes);
                return;
            }
            JZUtils.setRequestedOrientation(getContext(), 1);
            currentOrientation = 0;
            this.orientationClickState = 0;
            this.ivChangeOritation.setImageResource(defaultOrientationRes);
        }
    }

    public void startVideo() {
        JZVideoPlayerManager.completeAll();
        initTextureView();
        addTextureView();
        JZUtils.scanForActivity(getContext()).getWindow().addFlags(128);
        JZMediaManager.CURRENT_PLAYING_URL = JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex);
        JZMediaManager.CURRENT_PLING_LOOP = this.loop;
        JZMediaManager.MAP_HEADER_DATA = this.headData;
        onStatePreparing();
        JZVideoPlayerManager.setFirstFloor(this);
    }

    public void setMediaPlayerCallback(TextureVideoView.MediaPlayerCallback mediaPlayerCallback) {
        this.mediaPlayerCallback = mediaPlayerCallback;
    }

    public void onPrepared() {
        if (JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex).toLowerCase().contains("mp3")) {
            onVideoRendingStart();
        }
        if (this.mediaPlayerCallback != null) {
            this.mediaPlayerCallback.onPrepared(JZMediaManager.instance().mediaPlayer);
        }
        if (this.mOnMediaPlayCountListener != null) {
            this.mOnMediaPlayCountListener.onStart();
        }
    }

    public void onVideoRendingStart() {
        this.isVideoRendingStart = true;
        if (this.currentState == 1 || this.currentState == 2) {
            try {
                if (this.seekToInAdvance != 0) {
                    JZMediaManager.instance().mediaPlayer.seekTo(this.seekToInAdvance);
                    this.seekToInAdvance = 0;
                } else {
                    int position = JZUtils.getSavedProgress(JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex));
                    if (position != 0) {
                        JZMediaManager.instance().mediaPlayer.seekTo(position);
                    }
                }
                startProgressTimer();
                onStatePlaying();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void setState(int state) {
        setState(state, 0, 0);
    }

    public void setState(int state, int urlMapIndex, int seekToInAdvance) {
        switch (state) {
            case 0:
                onStateNormal();
                return;
            case 1:
                onStatePreparing();
                return;
            case 2:
                onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
                return;
            case 3:
                onStatePlaying();
                return;
            case 5:
                onStatePause();
                return;
            case 6:
                onStateAutoComplete();
                return;
            case 7:
                onStateError();
                return;
            default:
                return;
        }
    }

    public void onStateNormal() {
        this.currentState = 0;
        cancelProgressTimer();
        if (isCurrentJzvd()) {
            int position = 0;
            try {
                position = JZMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            if (position != 0) {
                JZUtils.saveProgress(JZMediaManager.CURRENT_PLAYING_URL, position);
            }
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    public int getCurrentPos() {
        int position = 0;
        try {
            return JZMediaManager.instance().mediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return position;
        }
    }

    public void onStatePreparing() {
        this.currentState = 1;
        resetProgressAndTime();
    }

    public void onStatePreparingChangingUrl(int urlMapIndex, int seekToInAdvance) {
        this.currentState = 2;
        this.currentUrlMapIndex = urlMapIndex;
        this.seekToInAdvance = seekToInAdvance;
        JZMediaManager.CURRENT_PLAYING_URL = JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex);
        JZMediaManager.CURRENT_PLING_LOOP = this.loop;
        JZMediaManager.MAP_HEADER_DATA = this.headData;
        JZMediaManager.instance().prepare();
    }

    public void onStatePlaying() {
        this.currentState = 3;
        if (this.currentScreen == 2 && JZMediaManager.instance() != null && JZMediaManager.instance().isMute()) {
            JZMediaManager.instance().unMute();
        }
        if (this.onPlayerStateChange != null) {
            this.onPlayerStateChange.onPlaying();
        }
        if (this.mPlayCountingCallBack != null) {
            this.mPlayCountingCallBack.UpdateCount();
        }
        startProgressTimer();
    }

    public void onStatePause() {
        this.currentState = 5;
        if (this.onPlayerStateChange != null) {
            this.onPlayerStateChange.onPauseOrStop();
        }
        startProgressTimer();
    }

    public void onStateError() {
        this.currentState = 7;
        cancelProgressTimer();
    }

    public void onStateAutoComplete() {
        this.currentState = 6;
        cancelProgressTimer();
        this.progressBar.setProgress(100);
    }

    public void onInfo(int what, int extra) {
        if (what == 3) {
            onVideoRendingStart();
        }
        if (this.mediaPlayerCallback != null) {
            this.mediaPlayerCallback.onInfo(JZMediaManager.instance().mediaPlayer, what, extra);
        }
    }

    public void onError(int what, int extra) {
        if (what != 38 && what != -38 && extra != -38) {
            onStateError();
            if (isCurrentJzvd()) {
                JZMediaManager.instance().releaseMediaPlayer();
            }
            if (this.mediaPlayerCallback != null) {
                this.mediaPlayerCallback.onError(JZMediaManager.instance().mediaPlayer, what, extra);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.currentScreen == 2 || this.currentScreen == 3) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (this.widthRatio == 0 || this.heightRatio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int specWidth = MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = (int) ((((float) specWidth) * ((float) this.heightRatio)) / ((float) this.widthRatio));
            setMeasuredDimension(specWidth, specHeight);
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.EXACTLY));
        }
    }

    public void setOnMediaPlayCountListener(OnMediaPlayCountListener onMediaPlayCountListener) {
        this.mOnMediaPlayCountListener = onMediaPlayCountListener;
    }

    public void setPlayCountingCallBack(PlayCountingCallBack mCallBack) {
        this.mPlayCountingCallBack = mCallBack;
    }

    public void onAutoCompletion() {
        Runtime.getRuntime().gc();
        JZVideoPlayerManager.getCurrentJzvd().setProgressAndText(100, JZVideoPlayerManager.getCurrentJzvd().getCurrentPositionWhenPlaying(), JZVideoPlayerManager.getCurrentJzvd().getDuration());
        try {
            if (this.currentState != 5) {
                JZMediaManager.instance().mediaPlayer.seekTo(0);
                JZMediaManager.instance().mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if (this.mPlayCountingCallBack != null) {
            this.mPlayCountingCallBack.UpdateCount();
        }
    }

    public void onCompletion(int i) {
        if (this.onPlayerStateChange != null) {
            this.onPlayerStateChange.onPauseOrStop();
        }
        if (this.currentState == 3 || this.currentState == 5) {
            JZUtils.saveProgress(JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex), getCurrentPositionWhenPlaying());
        }
        cancelProgressTimer();
        onStateNormal();
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        JZMediaManager.instance().currentVideoWidth = 0;
        JZMediaManager.instance().currentVideoHeight = 0;
        ((AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE)).abandonAudioFocus(onAudioFocusChangeListener);
        JZUtils.scanForActivity(getContext()).getWindow().clearFlags(128);
        clearFullscreenLayout();
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        JZMediaManager.textureView = null;
        JZMediaManager.savedSurfaceTexture = null;
        this.isVideoRendingStart = false;
    }

    public void release() {
        if (JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex).equals(JZMediaManager.CURRENT_PLAYING_URL) && System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME > 300) {
            if (JZVideoPlayerManager.getSecondFloor() != null && JZVideoPlayerManager.getSecondFloor().currentScreen == 2) {
                return;
            }
            if (JZVideoPlayerManager.getSecondFloor() != null || JZVideoPlayerManager.getFirstFloor() == null || JZVideoPlayerManager.getFirstFloor().currentScreen != 2) {
                releaseAllVideos();
            }
        }
    }

    public void initTextureView() {
        removeTextureView();
        JZMediaManager.textureView = new JZResizeTextureView(getContext());
        if (this.scaleType == null) {
            this.scaleType = JZResizeTextureView.ScaleType.CENTER_CROP;
        }
        JZMediaManager.textureView.setScaleType(this.scaleType);
        JZMediaManager.textureView.setSurfaceTextureListener(JZMediaManager.instance());
    }

    public void addTextureView() {
        LayoutParams layoutParams = new LayoutParams(-1, -1, 17);
        ViewParent parent = JZMediaManager.textureView.getParent();
        if (parent == null) {
            this.textureViewContainer.addView(JZMediaManager.textureView, layoutParams);
        } else if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(JZMediaManager.textureView);
            this.textureViewContainer.addView(JZMediaManager.textureView, layoutParams);
        }
    }

    public void removeTextureView() {
        JZMediaManager.savedSurfaceTexture = null;
        if (JZMediaManager.textureView != null && JZMediaManager.textureView.getParent() != null) {
            ((ViewGroup) JZMediaManager.textureView.getParent()).removeView(JZMediaManager.textureView);
        }
    }

    public void clearFullscreenLayout() {
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(android.R.id.content);
        View oldF = vp.findViewById(R.id.texture_full_screen);
        if (oldF != null) {
            vp.removeView(oldF);
        }
        showSupportActionBar(getContext());
    }

    public void clearFloatScreen() {
        ((AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE)).abandonAudioFocus(onAudioFocusChangeListener);
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        showSupportActionBar(getContext());
        JZVideoPlayer currJzvd = JZVideoPlayerManager.getCurrentJzvd();
        if (currJzvd.myOrientoinListener != null) {
            currJzvd.myOrientoinListener.disable();
        }
        if (currJzvd.rotationObserver != null) {
            currJzvd.getContext().getContentResolver().unregisterContentObserver(currJzvd.rotationObserver);
            currJzvd.rotationObserver = null;
        }
        ViewParent viewParent = JZMediaManager.textureView.getParent();
        if (viewParent != null && (viewParent instanceof ViewGroup)) {
            ((ViewGroup) viewParent).removeView(JZMediaManager.textureView);
        }
        ((ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(android.R.id.content)).removeView(currJzvd);
        JZVideoPlayerManager.setSecondFloor(null);
    }

    public void onVideoSizeChanged() {
        if (JZMediaManager.textureView != null && JZMediaManager.instance().getVideoSize() != null && JZMediaManager.instance().getVideoSize2() != null) {
            Point videoSize = JZMediaManager.instance().getVideoSize();
            Point videoSize2 = JZMediaManager.instance().getVideoSize2();
            if (this.mPoint == null) {
                this.mPoint = new Point();
            }
            this.mPoint.x = videoSize.x;
            this.mPoint.y = videoSize.y;
            JZMediaManager.textureView.setVideoSize(videoSize, videoSize2);
        }
    }

    public void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        this.mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(this.mProgressTimerTask, 0, 300);
    }

    public void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (this.mProgressTimerTask != null) {
            this.mProgressTimerTask.cancel();
        }
    }

    public void setProgressAndText(int progress, int position, int duration) {
        String durStr;
        if (!(this.mTouchingProgressBar || progress == 0)) {
            this.progressBar.setProgress(progress);
        }
        if (position != 0) {
            durStr = JZUtils.stringForTime(position) + " / " + JZUtils.stringForTime(duration);
        } else {
            durStr = "0:00 / " + JZUtils.stringForTime(duration);
        }
        this.currentTimeTextView.setText(durStr);
    }

    public void setBufferProgress(int bufferProgress) {
        if (bufferProgress != 0) {
            this.progressBar.setSecondaryProgress(bufferProgress);
        }
        if (this.mediaPlayerCallback != null) {
            this.mediaPlayerCallback.onBufferingUpdate(JZMediaManager.instance().mediaPlayer, bufferProgress);
        }
    }

    public void resetProgressAndTime() {
        this.progressBar.setProgress(0);
        this.progressBar.setSecondaryProgress(0);
        this.currentTimeTextView.setText(JZUtils.stringForTime(0) + " / " + JZUtils.stringForTime(0));
    }

    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (JZMediaManager.instance().mediaPlayer == null) {
            return position;
        }
        if (this.currentState == 3 || this.currentState == 5) {
            try {
                position = JZMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    public int getDuration() {
        int duration = 0;
        if (JZMediaManager.instance().mediaPlayer == null) {
            return duration;
        }
        try {
            return JZMediaManager.instance().mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelProgressTimer();
        this.mTouchingProgressBar = true;
        for (ViewParent vpdown = getParent(); vpdown != null; vpdown = vpdown.getParent()) {
            vpdown.requestDisallowInterceptTouchEvent(true);
        }
        showSampleImages();
        if (this.progressBar != null) {
            this.progressBar.setThumb(getPressDrawable());
        }
    }

    protected void showSampleImages() {
        if (this.currentScreen != 2) {
            return;
        }
        if (this.fileMap == null || this.mSampleUrls == null || this.mSampleUrls.size() <= 0 || this.fileMap.size() != this.mSampleUrls.size()) {
            checkSampleUrlsCache(0, 0, false);
            if (this.fileMap != null && this.mSampleUrls != null && this.mSampleUrls.size() > 0 && this.fileMap.size() == this.mSampleUrls.size()) {
                showSample();
                return;
            }
            return;
        }
        showSample();
    }

    private void showSample() {
        int position = (int) (((((float) this.progressBar.getProgress()) * 1.0f) / 100.0f) * ((float) JZVideoPlayerManager.getCurrentJzvd().getDuration()));
        int imageLocation = Math.round((((float) position) * 1.0f) / 3000.0f);
        if (this.fileMap.get(Integer.valueOf(imageLocation)) != null) {
            Glide.with(getContext()).load(FrescoUriUtil.SchemeFile + this.fileMap.get(Integer.valueOf(imageLocation))).apply(RequestOptions.centerCropTransform().dontAnimate()).into(this.ivFrameAt);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.flFrameAt.getLayoutParams();
            layoutParams.leftMargin = (int) (((((((float) this.progressBar.getProgress()) * 1.0f) * ((float) (this.progressBar.getWidth() - DensityUtil.dip2px(40.0f)))) / ((float) this.progressBar.getMax())) + ((float) DensityUtil.dip2px(60.0f))) - ((float) (this.mFlFrameAtWidth / 2)));
            this.flFrameAt.setLayoutParams(layoutParams);
            this.tvFrameCurrent.setText(JZUtils.stringForTime(position));
            this.flFrameAt.setVisibility(VISIBLE);
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        onEvent(5);
        this.mTouchingProgressBar = false;
        startProgressTimer();
        for (ViewParent vpup = getParent(); vpup != null; vpup = vpup.getParent()) {
            vpup.requestDisallowInterceptTouchEvent(false);
        }
        if (this.currentScreen == 2 && this.flFrameAt.getVisibility() == VISIBLE) {
            this.flFrameAt.setVisibility(GONE);
        }
        if (this.progressBar != null) {
            this.progressBar.setThumb(getNormalDrawable());
        }
        if (this.currentState == 3 || this.currentState == 5) {
            JZMediaManager.instance().mediaPlayer.seekTo((seekBar.getProgress() * getDuration()) / 100);
        }
    }

    private Drawable getNormalDrawable() {
        if (this.seekThumbNormal == null) {
            synchronized (JZVideoPlayer.class) {
                this.seekThumbNormal = getResources().getDrawable(R.drawable.jz_seek_thumb_normal);
            }
        }
        return this.seekThumbNormal;
    }

    private Drawable getPressDrawable() {
        if (this.seekThumbPress == null) {
            synchronized (JZVideoPlayer.class) {
                this.seekThumbPress = getResources().getDrawable(R.drawable.jz_seek_thumb_pressed);
            }
        }
        return this.seekThumbPress;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            invalidateFrame(progress);
        }
    }

    protected void invalidateFrame(int progress) {
        if (this.currentScreen != 2) {
            return;
        }
        if (this.flFrameAt.getVisibility() == VISIBLE) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.flFrameAt.getLayoutParams();
            layoutParams.leftMargin = (int) (((((((float) this.progressBar.getProgress()) * 1.0f) * ((float) (this.progressBar.getWidth() - DensityUtil.dip2px(40.0f)))) / ((float) this.progressBar.getMax())) + ((float) DensityUtil.dip2px(60.0f))) - ((float) (this.mFlFrameAtWidth / 2)));
            this.flFrameAt.setLayoutParams(layoutParams);
            int position = (int) (((((float) progress) * 1.0f) / 100.0f) * ((float) JZVideoPlayerManager.getCurrentJzvd().getDuration()));
            this.tvFrameCurrent.setText(JZUtils.stringForTime(position));
            int imageLocation = Math.round((((float) position) * 1.0f) / 3000.0f);
            if (this.fileMap.get(Integer.valueOf(imageLocation)) != null) {
                Glide.with(getContext()).load(FrescoUriUtil.SchemeFile + this.fileMap.get(Integer.valueOf(imageLocation))).apply(RequestOptions.centerCropTransform().dontAnimate()).into(this.ivFrameAt);
                return;
            }
            return;
        }
        showSampleImages();
    }

    public void setResetActive(boolean reset) {
        this.resetActive = reset;
    }

    public void startWindowFullscreen(List<String> urls, int story_id, int id, @Nullable PlayCountingCallBack playCountListener) {
        int orientation;
        hideSupportActionBar(getContext());
        ((AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE)).requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        currentOrientation = 0;
        if (this.mPoint.x > this.mPoint.y) {
            orientation = 1;
        } else {
            orientation = 1;
        }
        JZUtils.setRequestedOrientation(getContext(), orientation);
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(android.R.id.content);
        View old = vp.findViewById(R.id.texture_full_screen);
        if (old != null) {
            vp.removeView(old);
        }
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            JZVideoPlayer jzVideoPlayer = (JZVideoPlayer) getClass().getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()});
            jzVideoPlayer.setId(R.id.texture_full_screen);
            LayoutParams lp = new LayoutParams(-1, -1);
            lp.gravity = 16;
            vp.addView(jzVideoPlayer, lp);
            jzVideoPlayer.setUp(this.urlMap, this.currentUrlMapIndex, 2, this.objects);
            jzVideoPlayer.setState(this.currentState);
            if (JZMediaManager.textureView == null) {
                initTextureView();
            }
            jzVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
            float rate = (1.0f * TypedValue.applyDimension(3, 9000.0f, getResources().getDisplayMetrics())) / ((float) (this.mPoint.x * this.mPoint.y));
            int frameWidth = (int) (((double) this.mPoint.x) * Math.sqrt((double) rate));
            int frameHeight = (int) (((double) this.mPoint.y) * Math.sqrt((double) rate));
            LayoutParams layoutParams = (LayoutParams) jzVideoPlayer.ivFrameAt.getLayoutParams();
            jzVideoPlayer.mFlFrameAtWidth = frameWidth;
            layoutParams.width = frameWidth;
            layoutParams.height = frameHeight;
            jzVideoPlayer.ivFrameAt.setLayoutParams(layoutParams);
            jzVideoPlayer.mSampleUrls = urls;
            jzVideoPlayer.checkSampleUrlsCache(story_id, id, true);
            if (playCountListener != null) {
                jzVideoPlayer.setPlayCountingCallBack(playCountListener);
            }
            if (this.mPoint.x > this.mPoint.y) {
                jzVideoPlayer.ivChangeOritation.setVisibility(VISIBLE);
                jzVideoPlayer.ivChangeOritation2.setVisibility(GONE);
            } else {
                jzVideoPlayer.ivChangeOritation2.setVisibility(VISIBLE);
                jzVideoPlayer.ivChangeOritation.setVisibility(GONE);
            }
            jzVideoPlayer.myOrientoinListener.enable();
            jzVideoPlayer.getContext().getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("accelerometer_rotation"), true, jzVideoPlayer.rotationObserver);
            onStateNormal();
            jzVideoPlayer.progressBar.setSecondaryProgress(this.progressBar.getSecondaryProgress());
            jzVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkSampleUrlsCache(int story_id, int id, final boolean needLoad) {
        if (this.mSampleUrls != null && this.mSampleUrls.size() > 0) {
            for (int i = 0; i < this.mSampleUrls.size(); i++) {
                final int finalInt = i;
//                new LoadLocalCatchFileTask(App.getContext(), true, 1.lambdaFactory$(this, finalInt, needLoad, (String) this.mSampleUrls.get(i))).execute(new String[]{url});
                new LoadLocalCatchFileTask(App.getContext(), true, new LoadLocalFileCallback() {
                    @Override
                    public void localHasCatch(File file) {
                        lambda$checkSampleUrlsCache$0(finalInt,needLoad,mSampleUrls.get(finalInt),file);
                    }
                });
            }
        }
    }

    private /* synthetic */ void lambda$checkSampleUrlsCache$0(int finalInt, boolean needLoad, String url, File resultFile) {
        if (resultFile != null && resultFile.exists()) {
            this.fileMap.put(Integer.valueOf(finalInt), resultFile);
        } else if (needLoad) {
            doCacheOpt(url);
        }
    }

    private void reRequestUrls(int story_id, int id) {
        Log.e("debug",getClass().getSimpleName()+":reRequestUrls");
//        TimelineController.getSampleUrls(id, story_id, new IControllerCallback<TimelineBean>() {
//            public void success(TimelineBean timelineBean, String jsonContent, long cursorId, boolean isAllDone) {
//                super.success(timelineBean, jsonContent, cursorId, isAllDone);
//            }
//        });
    }

    private void doCacheOpt(String url) {
        //Glide.with(App.getContext()).using(VideoListGlideModule.getOkHttpUrlLoader(), InputStream.class).load(new CacheKeyGlideUrl(url, true)).as(File.class).downloadOnly(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Log.e("debug",getClass().getSimpleName()+":doCacheOpt:"+url);
    }

    public boolean isCurrentJzvd() {
        return JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd() == this;
    }

    public void playOnThisJzvd() {
        if (this.myOrientoinListener != null) {
            this.myOrientoinListener.disable();
        }
        if (this.rotationObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(this.rotationObserver);
            this.rotationObserver = null;
        }
        this.currentState = JZVideoPlayerManager.getSecondFloor().currentState;
        this.currentUrlMapIndex = JZVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
        clearFloatScreen();
        if (this.resetActive) {
            setState(0);
        } else if (this.currentState == 5) {
            this.currentState = 3;
            setState(this.currentState);
            try {
                JZMediaManager.instance().mediaPlayer.start();
            } catch (IllegalStateException e) {
            }
        } else {
            setState(this.currentState);
        }
        addTextureView();
        if (this.resetActive) {
            this.resetActive = false;
            if (this.onPlayerStateChange != null) {
                this.onPlayerStateChange.onCloseFullscreen();
            }
        }
        JZMediaManager.instance().mute();
    }

    public void onEvent(int type) {
        if (JZ_USER_EVENT != null && isCurrentJzvd() && this.urlMap != null) {
            JZ_USER_EVENT.onEvent(type, JZUtils.getCurrentUrlFromMap(this.urlMap, this.currentUrlMapIndex), this.currentScreen, this.objects);
        }
    }

    public void setOnPlayerStateChange(OnPlayerStateChange stateChange) {
        this.onPlayerStateChange = stateChange;
    }

    public void setHasAudio(int has_audio) {
        this.has_audio = has_audio;
    }

    public boolean isHasAudio() {
        return this.has_audio == 1;
    }

    public void onSeekComplete() {
    }

    public static void startFullscreen(Context context, int state, String thumbUrl, String sign, Class _class, Point point, String url, Object... objects) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        startFullscreen(context, state, thumbUrl, sign, _class, point, map, 0, objects);
    }

    public static void startFullscreen(Context context, int state, String thumbUrl, String sign, Class _class, Point point, LinkedHashMap urlMap, int defaultUrlMapIndex, Object... objects) {
        int orientation;
        if (state != 0) {
            if (ACTION_BAR_EXIST && JZUtils.getAppCompActivity(context) != null) {
                ActionBar ab = JZUtils.getAppCompActivity(context).getSupportActionBar();
                if (ab != null) {
                    ab.setShowHideAnimationEnabled(false);
                    ab.hide();
                }
            }
            if (TOOL_BAR_EXIST) {
                deleteAll((ViewGroup) JZUtils.getWindow(context).getDecorView());
                JZUtils.getWindow(context).setFlags(1024, 1024);
            }
        }
        currentOrientation = 0;
        if (point.x > point.y) {
            orientation = 1;
        } else {
            orientation = 1;
        }
        ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        JZUtils.setRequestedOrientation(context, orientation);
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(context).findViewById(android.R.id.content);
        View old = vp.findViewById(R.id.texture_full_screen);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            JZVideoPlayer jzVideoPlayer = (JZVideoPlayer) _class.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            jzVideoPlayer.setId(R.id.texture_full_screen);
            vp.addView(jzVideoPlayer, new LayoutParams(-1, -1));
            jzVideoPlayer.sourceWindowState = state;
            jzVideoPlayer.setUp(urlMap, defaultUrlMapIndex, 2, objects);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (point.x > point.y) {
                jzVideoPlayer.ivChangeOritation.setVisibility(VISIBLE);
                jzVideoPlayer.ivChangeOritation2.setVisibility(GONE);
            } else {
                jzVideoPlayer.ivChangeOritation2.setVisibility(VISIBLE);
                jzVideoPlayer.ivChangeOritation.setVisibility(GONE);
            }
            jzVideoPlayer.myOrientoinListener.enable();
            jzVideoPlayer.getContext().getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("accelerometer_rotation"), true, jzVideoPlayer.rotationObserver);
            if (jzVideoPlayer instanceof JZVideoPlayerStandard) {
                ViewGroup.LayoutParams layoutParams = ((JZVideoPlayerStandard) jzVideoPlayer).thumbImageView.getLayoutParams();
                final Point p = getPreviewImageSize(context, (float) point.x, (float) point.y);
                layoutParams.width = p.x;
                layoutParams.height = p.y;
                ((JZVideoPlayerStandard) jzVideoPlayer).thumbImageView.setLayoutParams(layoutParams);
                ((JZVideoPlayerStandard) jzVideoPlayer).setOnRootMeasure(new JZVideoPlayerStandard.OnRootMeasure() {
                    final /* synthetic */ JZVideoPlayer val$jzVideoPlayer=null;

                    public void onMeasure(int w, int h) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((JZVideoPlayerStandard) this.val$jzVideoPlayer).bottomProgressBar.getLayoutParams();
                        params.bottomMargin = (h - p.y) / 2;
                        ((JZVideoPlayerStandard) this.val$jzVideoPlayer).bottomProgressBar.setLayoutParams(params);
                    }
                });
                loadImageUseGlide(context, thumbUrl, sign, ((JZVideoPlayerStandard) jzVideoPlayer).thumbImageView);
            }
            jzVideoPlayer.startButton.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Point getPreviewImageSize(Context context, float w, float h) {
        float fitCoef;
        Point p = new Point();
        int screenWidth = DensityUtil.getMetricsWidth(context);
        int screenHeight = DensityUtil.getMetricsHeight(context);
        float scaleX = StaticLayoutUtil.DefaultSpacingadd;
        float scaleY = StaticLayoutUtil.DefaultSpacingadd;
        if (w > StaticLayoutUtil.DefaultSpacingadd) {
            scaleX = w / ((float) screenWidth);
        }
        if (h > StaticLayoutUtil.DefaultSpacingadd) {
            scaleY = h / ((float) screenHeight);
        }
        if (h > w) {
            fitCoef = ((float) screenWidth) / (((float) screenWidth) * scaleX);
        } else {
            fitCoef = ((float) screenWidth) / (((float) screenWidth) * scaleX);
        }
        p.x = (int) ((((float) screenWidth) * fitCoef) * scaleX);
        p.y = (int) ((((float) screenHeight) * fitCoef) * scaleY);
        return p;
    }

    public int getCurrentDuration() {
        int result = 0;
        if (this.progressBar != null) {
            result = (this.progressBar.getProgress() * getDuration()) / 100;
        }
        if (result < 0) {
            return 0;
        }
        return result;
    }

    private static void loadImageUseGlide(Context context, String url, String signature, ImageView imageView) {
        if (context != null && TextUtil.isValidate(url) && TextUtil.isValidate(signature)) {
            //Glide.with(context).load(url).signature(new StringSignature(signature)).centerCrop().dontAnimate().into(imageView);
            Log.e("debug","JZVideoPlayer:loadImageUseGlide:"+url+":"+signature);
        }
    }
}