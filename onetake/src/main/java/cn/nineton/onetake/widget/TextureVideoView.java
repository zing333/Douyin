package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;

import cn.nineton.onetake.util.LogUtil;

public class TextureVideoView extends ScalableTextureView implements SurfaceTextureListener, Callback, OnPreparedListener, OnVideoSizeChangedListener, OnCompletionListener, OnErrorListener, OnInfoListener, OnBufferingUpdateListener, OnSeekCompleteListener {
    private static final int MSG_PAUSE = 4;
    private static final int MSG_START = 1;
    private static final int MSG_STOP = 6;
    private static final int SHARE_NO_STATE = -1;
    private static final int SHARE_STATE = 0;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final String TAG = TextureVideoView.class.getSimpleName();
    private static final HandlerThread sThread = new HandlerThread("VideoPlayThread");
    private boolean canComplete = true;
    private boolean isGif;
    private int lastPlayPosition = 0;
    private Context mContext;
    private int mCurrentShareState = -1;
    private int mCurrentState = 0;
    private int mDuration = 1000;
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private MediaPlayerCallback mMediaPlayerCallback;
    private OnMediaPlayCountListener mOnMediaPlayCountListener;
    private PlayCountingCallBack mPlayCountingCallBack;
    private Surface mSurface;
    private int mTargetState = 0;
    private Uri mUri;
    private Handler mVideoHandler;
    private int mVideoID = -1;
    private String mVideoPath;
    private int playCount = 0;

    public interface MediaPlayerCallback {
        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);

        void onCompletion(MediaPlayer mediaPlayer);

        boolean onError(MediaPlayer mediaPlayer, int i, int i2);

        boolean onInfo(MediaPlayer mediaPlayer, int i, int i2);

        void onMediaplayer(MediaPlayer mediaPlayer);

        void onPrepared(MediaPlayer mediaPlayer);

        void onSeekComplete(MediaPlayer mediaPlayer);

        void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnMediaPlayCountListener {
        void onEnd(int i);

        void onStart();
    }

    public interface PlayCountingCallBack {
        void UpdateCount(int i);
    }

    static {
        sThread.start();
    }

    public void setGif(boolean gif) {
        this.isGif = gif;
    }

    public MediaPlayer getMediaPlayer() {
        return this.mMediaPlayer;
    }

    public void onSeekComplete(MediaPlayer mp) {
        if (this.mMediaPlayerCallback != null) {
            this.mMediaPlayerCallback.onSeekComplete(mp);
        }
    }

    public void setCanComplete(boolean complete) {
        this.canComplete = complete;
    }

    public TextureVideoView(Context context) {
        super(context);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMediaPlayerCallback(MediaPlayerCallback mediaPlayerCallback) {
        this.mMediaPlayerCallback = mediaPlayerCallback;
    }

    public void setOnMediaPlayCountListener(OnMediaPlayCountListener onMediaPlayCountListener) {
        this.mOnMediaPlayCountListener = onMediaPlayCountListener;
    }

    public boolean handleMessage(Message msg) {
        synchronized (TextureVideoView.class) {
            switch (msg.what) {
                case 1:
                    LogUtil.i(TAG, "<< handleMessage init");
                    openVideo();
                    LogUtil.i(TAG, ">> handleMessage init");
                    break;
                case 4:
                    LogUtil.i(TAG, "<< handleMessage pause");
                    if (this.mMediaPlayer != null) {
                        this.mMediaPlayer.pause();
                    }
                    this.mCurrentState = 4;
                    LogUtil.i(TAG, ">> handleMessage pause");
                    break;
                case 6:
                    LogUtil.i(TAG, "<< handleMessage stop");
                    release(true);
                    LogUtil.i(TAG, ">> handleMessage stop");
                    break;
            }
        }
        return true;
    }

    private void init() {
        this.mContext = getContext();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mHandler = new Handler();
        this.mVideoHandler = new Handler(sThread.getLooper(), this);
        setSurfaceTextureListener(this);
    }

    private void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            try {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
                if (this.mOnMediaPlayCountListener != null) {
                    this.mOnMediaPlayCountListener.onEnd(this.mDuration);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openVideo() {
        if (this.mUri != null && this.mSurface != null) {
            if (!this.isGif) {
            }
            release(false);
            try {
                if (this.mMediaPlayer == null) {
                    this.mMediaPlayer = new MediaPlayer();
                    if (this.mMediaPlayerCallback != null) {
                        this.mMediaPlayerCallback.onMediaplayer(this.mMediaPlayer);
                    }
                    this.mMediaPlayer.setOnPreparedListener(this);
                    this.mMediaPlayer.setOnVideoSizeChangedListener(this);
                    this.mMediaPlayer.setOnCompletionListener(this);
                    this.mMediaPlayer.setOnErrorListener(this);
                    this.mMediaPlayer.setOnInfoListener(this);
                    this.mMediaPlayer.setOnBufferingUpdateListener(this);
                    this.mMediaPlayer.setOnSeekCompleteListener(this);
                    if (this.mUri != null) {
                        LogUtil.d("video_download", "++++++++++++++++++++++++++++++++++++++++++++++++++++++setDataSource:photoID:" + this.mVideoID + ", path:" + this.mUri.toString());
                        this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                    } else {
                        LogUtil.d("video_download", "++++++++++++++++++++++++++++++++++++++++++++++++++++++setDataSource:photoID:" + this.mVideoID + ", path is null");
                    }
                    this.mMediaPlayer.setSurface(this.mSurface);
                    this.mMediaPlayer.setScreenOnWhilePlaying(false);
                    this.mMediaPlayer.setAudioStreamType(3);
                    this.mMediaPlayer.setVolume(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
                    this.mMediaPlayer.prepareAsync();
                }
                this.mCurrentState = 1;
                this.mTargetState = 1;
            } catch (Exception ex) {
                LogUtil.e(TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                if (this.mMediaPlayerCallback != null) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            if (TextureVideoView.this.mMediaPlayerCallback != null) {
                                TextureVideoView.this.mMediaPlayerCallback.onError(TextureVideoView.this.mMediaPlayer, 1, 0);
                            }
                        }
                    });
                }
            }
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.d("surface", "onSurfaceTextureAvailable");
        this.mSurface = new Surface(surface);
        if (this.mTargetState == 3 || this.mCurrentShareState == 0) {
            start();
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.d("surface", "onSurfaceTextureSizeChanged");
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtil.d("surface", "onSurfaceTextureDestroyed");
        if (ListTextureViewManager.lastEdit) {
            ListTextureViewManager.lastEdit = false;
            return false;
        }
        stop();
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public synchronized void setVideoPath(String path) {
        this.mVideoPath = path;
        LogUtil.i(TAG, "setVideoPath " + path);
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        if (uri != null) {
            LogUtil.i(TAG, "setVideoURI " + uri.toString());
        } else {
            this.mVideoPath = null;
        }
        this.mUri = uri;
    }

    public void setVideoID(int videoID) {
        this.mVideoID = videoID;
    }

    public String getVideoPath() {
        return this.mVideoPath;
    }

    public void shareStart() {
        LogUtil.d("fragment", TAG + ", shareStart");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
        if (this.mOnMediaPlayCountListener != null) {
            this.mOnMediaPlayCountListener.onStart();
        }
        this.mCurrentState = 3;
        this.mCurrentShareState = -1;
    }

    public void sharePause() {
        LogUtil.d("fragment", TAG + ", sharePause");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
        }
        if (this.mOnMediaPlayCountListener != null) {
            this.mOnMediaPlayCountListener.onEnd(this.mDuration);
        }
        this.mCurrentState = 4;
        this.mCurrentShareState = 0;
    }

    public void start() {
        this.mTargetState = 3;
        if (isInPlaybackState()) {
            this.mVideoHandler.obtainMessage(6).sendToTarget();
        }
        if (this.mUri != null && this.mSurface != null) {
            this.mVideoHandler.obtainMessage(1).sendToTarget();
        }
    }

    public void destory() {
        release(true);
    }

    public void pause() {
        if (isPlaying()) {
            this.mVideoHandler.obtainMessage(4).sendToTarget();
        }
        this.mTargetState = 4;
    }

    public void resume() {
        if (!isPlaying()) {
            this.mVideoHandler.obtainMessage(1).sendToTarget();
        }
        this.mTargetState = 3;
    }

    public void checkIsPlaying() {
        if (!isInPlaybackState()) {
            this.mVideoHandler.obtainMessage(1).sendToTarget();
        }
    }

    public void stop() {
        if (isInPlaybackState()) {
            this.mVideoHandler.obtainMessage(6).sendToTarget();
        }
        this.mTargetState = 5;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public void onCompletion(final MediaPlayer mp) {
        LogUtil.d("test", "onCompletion");
        this.mCurrentState = 5;
        this.mTargetState = 5;
        if (this.canComplete) {
            mp.start();
            if (this.mPlayCountingCallBack != null) {
                this.playCount++;
                this.mPlayCountingCallBack.UpdateCount(this.playCount);
            }
            if (this.mMediaPlayerCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (TextureVideoView.this.mMediaPlayerCallback != null) {
                            TextureVideoView.this.mMediaPlayerCallback.onCompletion(mp);
                        }
                    }
                });
            }
        }
    }

    public void setPlayCountingCallBack(PlayCountingCallBack mCallBack) {
        this.mPlayCountingCallBack = mCallBack;
    }

    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        LogUtil.e(TAG, "onError() called with mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
        this.mCurrentState = -1;
        this.mTargetState = -1;
        if (this.mMediaPlayerCallback != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (TextureVideoView.this.mMediaPlayerCallback != null) {
                        TextureVideoView.this.mMediaPlayerCallback.onError(mp, what, extra);
                    }
                }
            });
        }
        return true;
    }

    public void onPrepared(final MediaPlayer mp) {
        this.mCurrentState = 2;
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            if (ListTextureViewManager.postion != 0) {
                this.mMediaPlayer.seekTo(ListTextureViewManager.postion);
                ListTextureViewManager.postion = 0;
            }
            this.mDuration = this.mMediaPlayer.getDuration();
            this.mCurrentState = 3;
            this.mTargetState = 3;
            if (this.mOnMediaPlayCountListener != null) {
                this.mOnMediaPlayCountListener.onStart();
            }
        }
        if (this.mMediaPlayerCallback != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (TextureVideoView.this.mMediaPlayerCallback != null) {
                        TextureVideoView.this.mMediaPlayerCallback.onPrepared(mp);
                    }
                }
            });
        }
    }

    public void onVideoSizeChanged(final MediaPlayer mp, final int width, final int height) {
        if (this.mMediaPlayerCallback != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (TextureVideoView.this.mMediaPlayerCallback != null) {
                        TextureVideoView.this.mMediaPlayerCallback.onVideoSizeChanged(mp, width, height);
                    }
                }
            });
        }
    }

    public void onBufferingUpdate(MediaPlayer mp, final int percent) {
        if (this.mMediaPlayerCallback != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (TextureVideoView.this.mMediaPlayerCallback != null) {
                        TextureVideoView.this.mMediaPlayerCallback.onBufferingUpdate(TextureVideoView.this.mMediaPlayer, percent);
                    }
                }
            });
        }
    }

    public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
        LogUtil.e(TAG, "onInfo() called with mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
        if (this.mMediaPlayerCallback != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (TextureVideoView.this.mMediaPlayerCallback != null) {
                        TextureVideoView.this.mMediaPlayerCallback.onInfo(mp, what, extra);
                    }
                }
            });
        }
        return true;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public boolean canSeeInWindow() {
        return true;
    }
}