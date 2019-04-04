package cn.nineton.onetake.widget.video;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import java.util.Map;

import cn.nineton.onetake.App;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class JZMediaManager implements SurfaceTextureListener, OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener, OnSeekCompleteListener, OnErrorListener, OnInfoListener, OnVideoSizeChangedListener {
    public static String CURRENT_PLAYING_URL = null;
    public static boolean CURRENT_PLING_LOOP = false;
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;
    private static JZMediaManager JZMediaManager = null;
    public static Map<String, String> MAP_HEADER_DATA = null;
    public static final String TAG = "JiaoZiVideoPlayer";
    public static int listPos = 0;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static JZResizeTextureView textureView;
    public int currentVideoHeight = 0;
    public int currentVideoHeight2 = 0;
    public int currentVideoWidth = 0;
    public int currentVideoWidth2 = 0;
    MediaHandler mMediaHandler;
    HandlerThread mMediaHandlerThread = new HandlerThread("JiaoZiVideoPlayer");
    private boolean mSoundMute;
    Handler mainThreadHandler;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private int screenHeight;
    private float screenWHRate;
    private int screenWidth;

    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JZMediaManager.this.currentVideoWidth = 0;
                        JZMediaManager.this.currentVideoWidth2 = 0;
                        JZMediaManager.this.currentVideoHeight = 0;
                        JZMediaManager.this.currentVideoHeight2 = 0;
                        JZMediaManager.this.mediaPlayer.release();
                        JZMediaManager.this.mediaPlayer = new MediaPlayer();
                        JZMediaManager.this.mediaPlayer.setAudioStreamType(3);
                        JZMediaManager.this.mediaPlayer.setLooping(JZMediaManager.CURRENT_PLING_LOOP);
                        JZMediaManager.this.mediaPlayer.setOnPreparedListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setOnCompletionListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setOnBufferingUpdateListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setScreenOnWhilePlaying(true);
                        JZMediaManager.this.mediaPlayer.setOnSeekCompleteListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setOnErrorListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setOnInfoListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setOnVideoSizeChangedListener(JZMediaManager.this);
                        JZMediaManager.this.mediaPlayer.setVolume(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
                        JZMediaManager.this.mSoundMute = true;
                        MediaPlayer.class.getDeclaredMethod("setDataSource", new Class[]{String.class, Map.class}).invoke(JZMediaManager.this.mediaPlayer, new Object[]{JZMediaManager.CURRENT_PLAYING_URL, JZMediaManager.MAP_HEADER_DATA});
                        JZMediaManager.this.mediaPlayer.prepareAsync();
                        if (JZMediaManager.surface != null) {
                            JZMediaManager.surface.release();
                        }
                        JZMediaManager.surface = new Surface(JZMediaManager.savedSurfaceTexture);
                        JZMediaManager.this.mediaPlayer.setSurface(JZMediaManager.surface);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case 2:
                    if (JZMediaManager.this.mediaPlayer != null) {
                        JZMediaManager.this.mediaPlayer.release();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public JZMediaManager() {
        this.mMediaHandlerThread.start();
        this.mMediaHandler = new MediaHandler(this.mMediaHandlerThread.getLooper());
        this.mainThreadHandler = new Handler();
        this.screenWidth = DensityUtil.getMetricsWidth(App.getContext());
        this.screenHeight = DensityUtil.getMetricsHeight(App.getContext());
        this.screenWHRate = (1.0f * ((float) this.screenWidth)) / ((float) this.screenHeight);
    }

    public static JZMediaManager instance() {
        if (JZMediaManager == null) {
            JZMediaManager = new JZMediaManager();
        }
        return JZMediaManager;
    }

    public Point getVideoSize() {
        if (this.currentVideoWidth == 0 || this.currentVideoHeight == 0) {
            return null;
        }
        return new Point(this.currentVideoWidth, this.currentVideoHeight);
    }

    public Point getVideoSize2() {
        if (this.currentVideoHeight2 == 0 || this.currentVideoWidth2 == 0) {
            return null;
        }
        return new Point(this.currentVideoWidth2, this.currentVideoHeight2);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = 0;
        this.mMediaHandler.sendMessage(msg);
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = 2;
        this.mMediaHandler.sendMessage(msg);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i("JiaoZiVideoPlayer", "onSurfaceTextureAvailable [" + JZVideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
            return;
        }
        textureView.setSurfaceTexture(savedSurfaceTexture);
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i("JiaoZiVideoPlayer", "onSurfaceTextureSizeChanged [" + JZVideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void onPrepared(MediaPlayer mp) {
        this.mediaPlayer.start();
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                }
            }
        });
    }

    public void onCompletion(MediaPlayer mp) {
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    public void onBufferingUpdate(MediaPlayer mp, final int percent) {
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    public void onSeekComplete(MediaPlayer mp) {
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    public boolean onError(MediaPlayer mp, final int what, final int extra) {
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    public boolean onInfo(MediaPlayer mp, final int what, final int extra) {
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        this.currentVideoWidth = width;
        this.currentVideoHeight = height;
        float rate = (1.0f * ((float) width)) / ((float) height);
        if (this.screenWHRate > rate) {
            if (this.screenWidth >= height) {
                this.currentVideoHeight2 = this.screenWidth;
                this.currentVideoWidth2 = (int) (((float) this.screenWidth) * rate);
            } else {
                this.currentVideoHeight2 = height;
                this.currentVideoWidth2 = width;
            }
        } else if (this.screenWHRate >= rate) {
            this.currentVideoHeight2 = height;
            this.currentVideoWidth2 = width;
        } else if (this.screenWidth >= height) {
            this.currentVideoHeight2 = this.screenWidth;
            this.currentVideoWidth2 = (int) (((float) this.screenWidth) * rate);
        } else {
            this.currentVideoHeight2 = height;
            this.currentVideoWidth2 = width;
        }
        this.mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }

    public boolean isHasAudio() {
        return JZVideoPlayerManager.getCurrentJzvd().isHasAudio();
    }

    public boolean isMute() {
        return this.mSoundMute;
    }

    public void mute() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentState == 3 && !isMute() && this.mediaPlayer != null) {
            this.mediaPlayer.setVolume(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
            this.mSoundMute = true;
        }
    }

    public void unMute() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentState == 3 && this.mediaPlayer != null) {
            float volume = (float) (1.0d - (0.0d / Math.log((double) 100)));
            this.mediaPlayer.setVolume(1.0f, 1.0f);
            this.mSoundMute = false;
        }
    }
}