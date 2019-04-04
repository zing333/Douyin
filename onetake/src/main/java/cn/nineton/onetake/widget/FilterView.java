package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.GLES20;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
//import com.blink.academy.onetake.VideoTools.FastStart.MalformedFileException;
//import com.blink.academy.onetake.VideoTools.FastStart.UnsupportedFileException;
//import com.blink.academy.onetake.VideoTools.FrameRenderer.HAlign;
//import com.blink.academy.onetake.VideoTools.FrameRenderer.RenderMode;
//import com.blink.academy.onetake.VideoTools.FrameRenderer.VAlign;
//import com.blink.academy.onetake.VideoTools.Player.AspectMode;
//import com.blink.academy.onetake.VideoTools.Playlist.Drawable;
//import com.blink.academy.onetake.VideoTools.Playlist.Entry;
//import com.blink.academy.onetake.VideoTools.Playlist.FileMedia;
//import com.blink.academy.onetake.VideoTools.Playlist.Rotation;
//import com.blink.academy.onetake.VideoTools.VidStabilizer.Transform;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.model.video.VideoBitmapsModel;
//import com.blink.academy.onetake.support.callbacks.VideoRotateCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.VideoPreviewEvent;
//import com.blink.academy.onetake.support.interfaces.LongVideoPlayCallback;
//import com.blink.academy.onetake.support.utils.FilterViewUtils;
//import com.blink.academy.onetake.ui.activity.video.VideoCoverActivity.VideoSeekCallback;

import org.greenrobot.eventbus.EventBus;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoBitmapsModel;
import cn.nineton.onetake.event.VideoPreviewEvent;
import cn.nineton.onetake.listener.VideoRotateCallback;
import cn.nineton.onetake.listener.VideoSeekCallback;
import cn.nineton.onetake.media.FrameRenderer;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.Texture;
import cn.nineton.onetake.media.VidStabilizer;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnable;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.Framebuffer;
import cn.nineton.onetake.media.gpuimage.FramebufferTexture;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.interfaces.LongVideoPlayCallback;
import cn.nineton.onetake.util.FilterViewUtils;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.ProgramLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import jp.co.cyberagent.android.gpuimage.Framebuffer;
//import jp.co.cyberagent.android.gpuimage.FramebufferTexture;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class FilterView extends GLRenderView {
    private static final String TAG = "VideoSurfaceView";
    final long SEEK_WAIT_TIME = 0;
    private GPUImageFilter animationFilter = null;
    private List<LongVideosModel> bgmModelList;
    private float currentRotate = 0.0f;
    private boolean enableStabilizer;
    private boolean isSliding;
    double lastPercent = 0.0d;
    private int mCaptureMode;
    private int mDataFrom = 0;
    private int mDataType = 0;
    private FrameRenderer mFrameRenderer;
    private Object mFrameRendererSync = new Object();
    private LUTCreator mLUTCreator = new LUTCreator();
    private LongVideoPlayCallback mLongVideoCallback;
    private OnFrameIndexChangedListener mOnFrameIndexChangedListener;
    Player mPlayer;
    private VideoRotateCallback mRotateCallback;
    private VideoSeekCallback mSeekCallback;
    boolean mTargetSeekActive;
    long mTargetSeekTimeUs;
    private List<LongVideosModel> mVideoModelList;
    private boolean[] mediaMute;
    private boolean pauseAtEnd;
    private Playlist pausePlaylist = null;
    private double playbackPercent;
    private long playlistTimeUs = 0;
    private FrameRenderer.RenderMode renderMode;
    public boolean shouldInitPlayer = false;
    public boolean shouldPauseVideoFirst = true;

    public interface OnFrameIndexChangedListener {
        void indexCHanged(int i);
    }

    public enum VideoSpeedup {
        STEP,
        SLOW,
        EIGHTMM,
        NORMAL,
        CHAPLIN,
        DOUBLE,
        QUADRUPLE,
        TIMELAPSE
    }

    public void setDataType(int dataType, int dataFrom) {
        this.mDataType = dataType;
        this.mDataFrom = dataFrom;
    }

    public void setVideoModelList(List<LongVideosModel> videoPaths, List<LongVideosModel> musicModels, boolean[] mediaMute) {
        this.mVideoModelList = videoPaths;
        this.bgmModelList = musicModels;
        this.mediaMute = mediaMute;
    }

    public void setLongVideoCallback(LongVideoPlayCallback callback) {
        this.mLongVideoCallback = callback;
    }

    public void setVideoSeekCallback(VideoSeekCallback callback) {
        this.mSeekCallback = callback;
    }

    public void setRotateCallback(VideoRotateCallback mRotateCallback) {
        this.mRotateCallback = mRotateCallback;
    }

    public void setCaptureMode(int mCaptureMode) {
        this.mCaptureMode = mCaptureMode;
    }

    public FilterView(Context context) {
        super(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFrameRendererMode(FrameRenderer.RenderMode mode) {
        this.renderMode = mode;
    }

    public FrameRenderer getFrameRenderer() {
        FrameRenderer frameRenderer;
        synchronized (this.mFrameRendererSync) {
            if (this.mFrameRenderer == null) {
                this.mFrameRenderer = new FrameRenderer();
                this.mFrameRenderer.mode = this.renderMode;
                this.mFrameRenderer.isFilterPreview = true;
            }
            frameRenderer = this.mFrameRenderer;
        }
        return frameRenderer;
    }

    public void cancelOverlayBitmap() {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setOverlay(null, null, FrameRenderer.HAlign.LEFT, FrameRenderer.VAlign.BOTTOM, 0.0f);
            }
        });
    }

    public void setOverlayBitmap(Bitmap bitmap, FrameRenderer.HAlign hAlign, FrameRenderer.VAlign vAlign, float width) {
        final Bitmap bitmap2 = bitmap;
        final FrameRenderer.HAlign hAlign2 = hAlign;
        final FrameRenderer.VAlign vAlign2 = vAlign;
        final float f = width;
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setOverlay(new GPUImageFilter(), new Texture(bitmap2), hAlign2, vAlign2, f);
            }
        });
    }

    public long getPlaylistDurationMS() {
        return (long) ((((float) this.mPlayer.getPlaylist().getDurationUs()) * 1.0f) / 1000.0f);
    }

    public void setWrapFrame(final int frameIndex) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setWrapFrame(frameIndex);
            }
        });
    }

    public void setWrapFrame2(final int newFrameIndex) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setWrapFrame2(newFrameIndex);
            }
        });
    }

    public void setFilter(final GPUImageFilter filter) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setFilter(filter);
            }
        });
    }

    public void destroyFilters() {
        queueRunnable(new Runnable() {
            public void run() {
                if (FilterView.this.mFrameRenderer != null) {
                    FilterView.this.mFrameRenderer.destroyFilters();
                }
            }
        });
    }

    public void setFilters(final GPUImageFilter[] filters) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setFilters(filters);
            }
        });
    }

    public void setFilters(final GPUImageFilter[] filters0, final GPUImageFilter[] filters1) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setFilters(filters0, filters1);
            }
        });
    }

    public void setVideoFrames(final OutputSurfaceArray frames) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setVideoFrames(frames);
                FilterView.this.updateFilterDimensions();
            }
        });
    }

    public void setVideoRotateWithAnimator() {
        final Playlist playlist = this.mPlayer.getPlaylist();
        if (playlist != null && playlist.size() > 0) {
            queueRunnable(new Runnable() {
                public void run() {
                    long t0 = System.nanoTime();
                    final InterpolatedFloat rotateAnimator = InterpolatedFloat.create(t0, FilterView.this.currentRotate, t0 + 200000000, FilterView.this.currentRotate + 90.0f, InterpolatedFloat.Ease.Linear);
                    int size = playlist.size();
                    final int targetRotate = (((int) FilterView.this.currentRotate) + 90) % 360;
                    for (int i = 0; i < size; i++) {
                        final Playlist.Entry e = playlist.get(i);
                        if (e.mMediaType != 1) {
                            e.setDrawable(new Playlist.Drawable() {
                                public void draw(Playlist.Instance instance, Player.SourceTexture buffer) {
                                    long t = System.nanoTime();
                                    float w = rotateAnimator.getValue(t);
                                    if (FilterView.this.mRotateCallback != null) {
                                        FilterView.this.mRotateCallback.onRotate(FilterView.this.currentRotate % 360.0f, (FilterView.this.currentRotate + 90.0f) % 360.0f, w % 360.0f);
                                    }
                                    Playlist.Entry entry = instance.mEntry;
                                    instance.mOutputRotation = w;
                                    entry.mOutputRotation = w;
                                    FilterView.this.mPlayer.drawEffects(instance, buffer);
                                    if (rotateAnimator.hasEnded(t)) {
                                        if (FilterView.this.mRotateCallback != null) {
                                            float end = (FilterView.this.currentRotate + 90.0f) % 360.0f;
                                            FilterView.this.mRotateCallback.onRotate(FilterView.this.currentRotate % 360.0f, end, end);
                                        }
                                        Log.d(FilterView.TAG, "animation over");
                                        FilterView.this.currentRotate = (float) targetRotate;
                                        instance.mEntry.mOutputRotation = (float) targetRotate;
                                        e.setDrawable(null);
                                        if (FilterView.this.mLongVideoCallback != null) {
                                            FilterView.this.mLongVideoCallback.couldUseRotateButton(true);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void stopVideoFilterAnimation() {
        if (this.animationFilter != null && this.mPlayer != null && isGLReady()) {
            queueRunnableSync("endFilter", new Runnable() {
                public void run() {
                    Playlist playlist = FilterView.this.mPlayer.getPlaylist();
                    int size = playlist.size();
                    for (int i = 0; i < size; i++) {
                        Playlist.Entry e = playlist.get(i);
                        if (e.mMediaType != 1) {
                            e.mOutCropX = 0.0f;
                            e.mOutCropW = 1.0f;
                            e.setDrawable(null);
                        }
                    }
                    FilterView.this.animationFilter = null;
                }
            });
        }
    }

    public void setLongVideoFilterWithAnimation(final GPUImageFilter filter, final boolean leftToRight, final List<Integer> filterIndexList) {
        if (this.mPlayer != null && isGLReady()) {
            this.animationFilter = filter;
            queueRunnable(new Runnable() {
                public void run() {
                    float v0;
                    float v1;
                    FilterView.this.mPlayer.setAnimationFilter(filter);
                    Playlist playlist = FilterView.this.mPlayer.getPlaylist();
                    long t0 = System.nanoTime();
                    long t1 = t0 + 500000000;
                    Log.d(FilterView.TAG, String.format("setfilter at %d", new Object[]{Long.valueOf(t0)}));
                    if (leftToRight) {
                        v0 = 0.0f;
                        v1 = 1.0f;
                    } else {
                        v0 = 1.0f;
                        v1 = 0.0f;
                    }
                    final InterpolatedFloat cropAnimation = InterpolatedFloat.create(t0, v0, t1, v1, InterpolatedFloat.Ease.QuadIn);
                    int size = playlist.size();
                    for (int i = 0; i < size; i++) {
                        final Playlist.Entry e = playlist.get(i);
                        if (e.mMediaType != 1) {
                            e.setDrawable(new Playlist.Drawable() {
                                public void draw(Playlist.Instance instance, Player.SourceTexture buffer) {
                                    int i = 2002;
                                    long t = System.nanoTime();
                                    float w = cropAnimation.getValue(t);
                                    Log.d(FilterView.TAG, String.format(" at instance:%d global:%d value:%f", new Object[]{Long.valueOf(instance.mInstanceTimeUs), Long.valueOf(instance.mGlobalTimeUs), Float.valueOf(w)}));
                                    instance.mEntry.mOutCropX = 0.0f;
                                    instance.mEntry.mOutCropW = w;
                                    instance.mEntry.copyIndex();
                                    instance.mEntry.mFilterIndex = leftToRight ? 2002 : instance.mEntry.getAnimationIndex();
                                    FilterView.this.mPlayer.drawEffects(instance, buffer);
                                    instance.mEntry.mOutCropX = w;
                                    instance.mEntry.mOutCropW = 1.0f - w;
                                    instance.mEntry.copyIndex();
                                    Playlist.Entry entry = instance.mEntry;
                                    if (leftToRight) {
                                        i = instance.mEntry.getAnimationIndex();
                                    }
                                    entry.mFilterIndex = i;
                                    FilterView.this.mPlayer.drawEffects(instance, buffer);
                                    if (cropAnimation.hasEnded(t)) {
                                        FilterView.this.animationFilter = null;
                                        Log.d(FilterView.TAG, "animation over");
                                        //AnonymousClass12.this.finalFilterSetting(instance, e, filterIndexList, filter);
                                        finalFilterSetting(instance, e, filterIndexList, filter);
                                    }
                                }
                            });
                        }
                    }
                }

                private void finalFilterSetting(Playlist.Instance instance, Playlist.Entry e, List<Integer> filterIndexList, GPUImageFilter targetFilter) {
                    instance.mEntry.mOutCropX = 0.0f;
                    instance.mEntry.mOutCropW = 1.0f;
                    e.setDrawable(null);
                    Iterator it = FilterView.this.mPlayer.getPlaylist().getEntries().iterator();
                    while (it.hasNext()) {
                        ((Playlist.Entry) it.next()).resetFilterIndex();
                    }
                    int size = filterIndexList.size();
                    for (int i = 0; i < size; i++) {
                        FilterView.this.mPlayer.setFilter(((Integer) filterIndexList.get(i)).intValue(), targetFilter);
                    }
                }
            });
        }
    }

    public void setViewport(float x, float y, float w, float h) {
        final float f = x;
        final float f2 = y;
        final float f3 = w;
        final float f4 = h;
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setViewport(f, f2, f3, f4);
            }
        });
    }

    public void setViewOffset(final float xOffset, final float yOffset) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.xOffset = xOffset;
                FilterView.this.mFrameRenderer.yOffset = yOffset;
            }
        });
    }

    public VidStabilizer.Transform[] getTransforms() {
        return this.mFrameRenderer == null ? null : this.mFrameRenderer.getTransforms();
    }

    public void setTransforms(final VidStabilizer.Transform[] transforms) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mFrameRenderer = FilterView.this.getFrameRenderer();
                FilterView.this.mFrameRenderer.setTransforms(transforms);
            }
        });
    }

    protected void onGLInit() {
        ProgramLoader.suspend();
        this.mFrameRenderer = getFrameRenderer();
        this.mFrameRenderer.setEnableStabilizer(this.enableStabilizer);
        LUTCreator.initialize();
    }

    public boolean oldPlaylistIsNull() {
        return this.pausePlaylist == null;
    }

    public void setLastPlaylistData(final List<LongVideosModel> videos, final List<LongVideosModel> bgmModels, long playTimeUs, final boolean[] mediaMute) {
        this.pausePlaylist = new Playlist();
        this.playlistTimeUs = playTimeUs;
        queueRunnableSync("oldList", new Runnable() {
            public void run() {
                FilterView.this._setLastPlaylistData(videos, bgmModels, FilterView.this.pausePlaylist, mediaMute);
            }
        });
    }

    private void _setLastPlaylistData(List<LongVideosModel> videos, List<LongVideosModel> bgmModels, Playlist playlist, boolean[] mediaMute) {
        if (this.mPlayer != null) {
            int i;
            long totalDuration = 0;
            boolean allVideoVolumeGone = mediaMute[0];
            boolean allAudioVolumeGone = mediaMute[1];
            int size = videos.size();
            for (i = 0; i < size; i++) {
                LongVideosModel model = (LongVideosModel) videos.get(i);
                if (model.mediaType != 1) {
                    long currentDuration = model.getCurrentDuration();
                    Playlist.Rotation mRotation;
                    float[] cropParams;
                    if (model.mediaType == 0) {
                        long startTimeUs = model.getMediaStartTimeUs();
                        File file = new File(model.getPlaylistMediaPath());
                        if (file.exists()) {
                            Playlist.FileMedia fileMedia = Playlist.FileMedia.create(file);
                            model.setTimeLineStartTime(totalDuration);
                            Playlist.Entry entry = playlist.add(fileMedia, totalDuration, startTimeUs, currentDuration * 1000).setMediaType(0);
                            entry.setVolume(allVideoVolumeGone ? 0.0f : model.getVideoVolume());
                            entry.setFilterIndex(i);
                            entry.setSpeed(model.getVideoSpeed());
                            entry.setFramerate(model.getVideoFrameRate());
                            mRotation = FilterViewUtils.getVideoRotation(model.getVideoRotate());
                            entry.setOutputRotation((float) model.getVideoRotate());
                            boolean shouldVertical = model.isShouldVertical();
                            if (model.isFrontCamera) {
                                entry.setInputOrientation(Playlist.Rotation.ROTATE_0, !shouldVertical, shouldVertical);
                            } else {
                                entry.setInputOrientation(Playlist.Rotation.ROTATE_0, shouldVertical, shouldVertical);
                            }
                            cropParams = model.getInputCropParams();
                            entry.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                            entry.setZoom(InterpolatedFloat.createLinear(0, model.getZoomStart(), model.getOriginalCurrentDuration() * 1000, model.getZoomEnd()));
                        }
                    } else if (model.mediaType == 2 || model.mediaType == 4) {
                        Bitmap bitmap;
                        model.setTimeLineStartTime(totalDuration);
                        if (model.mediaType == 2) {
                            bitmap = VideoBitmapsModel.instancesModel().getBitmap(model.getPlaylistMediaPath());
                            cropParams = model.getInputCropParams();
                        } else {
                            bitmap = VideoBitmapsModel.instancesModel().getActivityBitmap();
                            cropParams = new float[4];
                            cropParams = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
                        }
                        Playlist.Entry add = playlist.add(bitmap, totalDuration, 0, currentDuration * 1000, Player.AspectMode.RENDER_CROP);
                        add.setSpeed(model.getVideoSpeed());
                        add.setFilterIndex(i);
                        add.setFramerate(model.getVideoFrameRate());
                        mRotation = FilterViewUtils.getVideoRotation(model.getVideoRotate());
                        add.setOutputRotation((float) model.getVideoRotate());
                        if (model.isFrontCamera) {
                            add.setInputOrientation(Playlist.Rotation.ROTATE_0, true, false);
                        } else {
                            add.setInputOrientation(Playlist.Rotation.ROTATE_0, false, false);
                        }
                        add.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                        add.setZoom(InterpolatedFloat.createLinear(0, model.getZoomStart(), model.getCurrentDuration() * 1000, model.getZoomEnd()));
                    }
                    totalDuration += currentDuration * 1000;
                }
            }
            FilterViewUtils.addActiveBGM(videos, playlist);
            if (bgmModels == null || allAudioVolumeGone) {
                for (i = playlist.size() - 1; i >= 0; i--) {
                    if (playlist.get(i).mMediaType == 1) {
                        playlist.remove(i);
                    }
                }
                return;
            }
            FilterViewUtils.setPlaylistBGMs(bgmModels, playlist);
        }
    }

    public void setPlaybackPercent(double playbackPercent) {
        this.playbackPercent = playbackPercent;
    }

    public Playlist getLongVideoPlaylist() {
        if (this.mPlayer != null) {
            return this.mPlayer.getPlaylist();
        }
        return null;
    }

    protected void onGLDestroy() {
        synchronized (this.mFrameRendererSync) {
            if (this.mFrameRenderer != null) {
                this.mFrameRenderer.destroy();
                this.mFrameRenderer = null;
            }
        }
        ProgramLoader.resume();
    }

    public void destroyCreator() {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.mLUTCreator.destroy();
            }
        });
    }

    protected void onGLChangedSize(int width, int height) {
        if (this.mPlayer != null) {
            this.mPlayer.setOutputRect(0, 0, width, height);
        }
        updateFilterDimensions();
    }

    public void setVideoPauseOrResume(boolean shouldPause) {
        if (this.mPlayer != null) {
            if (shouldPause) {
                if (!this.mPlayer.mIsPaused) {
                    this.mPlayer.pause();
                }
            } else if (this.mPlayer.mIsPaused) {
                this.mPlayer.resume();
            }
        }
    }

    private void setLongVideoStop() {
        if (this.mPlayer != null) {
            this.pausePlaylist = this.mPlayer.getPlaylist();
            this.mPlayer.stop();
        }
    }

    public void resetPlaylist() {
        if (this.mPlayer != null) {
            Playlist pl = this.mPlayer.getPlaylist();
            if (pl != null) {
                this.mPlayer.setPlaylist(pl, false);
                this.mPlayer.resume();
            }
        }
    }

    public boolean getLongVideoPlayState() {
        if (this.mPlayer != null) {
            return this.mPlayer.mIsPaused;
        }
        return false;
    }

    private void updateFilterDimensions() {
        if (this.mFrameRenderer != null) {
            OutputSurfaceArray frames = this.mFrameRenderer.getVideoFrames();
            if (frames != null && this.mWidth > 0 && this.mHeight > 0) {
                if (frames.isRotated()) {
                    this.mFrameRenderer.setFilterDimensions(this.mHeight, this.mWidth);
                } else {
                    this.mFrameRenderer.setFilterDimensions(this.mWidth, this.mHeight);
                }
            }
        }
    }

    protected boolean isGLReady() {
        if (2 == this.mDataType) {
            if (this.mVideoModelList == null || this.mVideoModelList.size() <= 0) {
                return false;
            }
            return true;
        } else if (this.mFrameRenderer == null || this.mFrameRenderer.getFilters() == null || this.mFrameRenderer.getFilters().length <= 0 || this.mFrameRenderer.getVideoFrames() == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isStabilizerEnabled() {
        return this.mFrameRenderer != null && this.mFrameRenderer.isStabilizerEnabled();
    }

    public void setEnableStabilizer(boolean enabled) {
        this.mFrameRenderer = getFrameRenderer();
        this.mFrameRenderer.setEnableStabilizer(enabled);
        this.enableStabilizer = enabled;
    }

    public void toggleStabilizer() {
        this.mFrameRenderer = getFrameRenderer();
        this.mFrameRenderer.toggleStabilizer();
        boolean z = this.mFrameRenderer != null && this.mFrameRenderer.isStabilizerEnabled();
        this.enableStabilizer = z;
    }

    public void requestAnimation(Runnable r) {
        queueRunnable(r);
    }

    public void setPlaylistTimeUs(long playlistTimeUs) {
        this.playlistTimeUs = playlistTimeUs;
    }

    public void setLongVideoSeekTo(float timePercent) {
        if (this.mPlayer != null && this.mPlayer.getPlaylist() != null) {
            this.mTargetSeekTimeUs = (long) (((double) this.mPlayer.getPlaylist().getDurationUs()) * ((double) timePercent));
            this.mTargetSeekActive = true;
            if (this.mSeekCallback != null) {
                this.mSeekCallback.onCurrentSeekTime(this.mTargetSeekTimeUs);
            }
            LogUtil.d("VideoSeek", String.format("time : %s, percent : %s", new Object[]{Long.valueOf(this.mTargetSeekTimeUs), Float.valueOf(timePercent)}));
            if (timePercent == 0.0f) {
                this.lastPercent = 0.0d;
            }
        }
    }

    public void setLongVideoSeekTo(long seekPlayTimeUs) {
        if (this.mPlayer != null) {
            this.mTargetSeekTimeUs = seekPlayTimeUs;
            this.mTargetSeekActive = true;
            if (seekPlayTimeUs == 0) {
                this.lastPercent = 0.0d;
            }
        }
    }

    public void setAllBGMVolume(final LongVideosModel musicModel, final List<Integer> mList) {
        queueRunnable(new Runnable() {
            public void run() {
                Playlist playlist = FilterView.this.mPlayer.getPlaylist();
                boolean hasValue = mList != null && mList.size() > 0;
                int i = 0;
                int size = playlist.size();
                while (i < size) {
                    if (!hasValue || !mList.contains(Integer.valueOf(i))) {
                        Playlist.Entry entry = playlist.get(i);
                        if (entry.mMediaType == 1) {
                            entry.setVolume(musicModel.getAudioVolume());
                        }
                    }
                    i++;
                }
            }
        });
    }

    public void changePlaylist(List<LongVideosModel> videos, List<LongVideosModel> bgmModels, long playTimeUs, boolean[] mediaMute) {
        if (videos != null && videos.size() != 0) {
            this.playlistTimeUs = playTimeUs;
            final List<LongVideosModel> list = videos;
            final List<LongVideosModel> list2 = bgmModels;
            final long j = playTimeUs;
            final boolean[] zArr = mediaMute;
            queueRunnable(new Runnable() {
                public void run() {
                    FilterView.this.setLongVideoPlaylist(list, list2, j, zArr);
                }
            });
        }
    }

    public void changeSpeedZoomVideoVolume(List<LongVideosModel> videoModels, @Nullable List<LongVideosModel> bgmVideoModels, long playTimeUs, boolean[] mediaMute) {
        this.playlistTimeUs = playTimeUs;
        final List<LongVideosModel> list = videoModels;
        final List<LongVideosModel> list2 = bgmVideoModels;
        final long j = playTimeUs;
        final boolean[] zArr = mediaMute;
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this.setLongVideoPlaylist(list, list2, j, zArr);
            }
        });
    }

    private void setLongVideoPlaylist(List<LongVideosModel> videos, List<LongVideosModel> bgmModels, long playTimeUs, boolean[] mediaMute) {
        int i;
        long totalDuration = 0;
        Playlist playlist = new Playlist();
        boolean allVideoVolumeGone = mediaMute[0];
        boolean allAudioVolumeGone = mediaMute[1];
        int size = videos.size();
        for (i = 0; i < size; i++) {
            LongVideosModel model = (LongVideosModel) videos.get(i);
            if (model.mediaType != 1) {
                long currentDuration = model.getCurrentDuration();
                float[] cropParams;
                if (model.mediaType == 0) {
                    long mediaStartTimeUs = model.getMediaStartTimeUs();
                    File file = new File(model.getPlaylistMediaPath());
                    if (file.exists()) {
                        Playlist.FileMedia fileMedia = Playlist.FileMedia.create(file);
                        model.setTimeLineStartTime(totalDuration);
                        Playlist.Entry entry = playlist.add(fileMedia, totalDuration, mediaStartTimeUs, currentDuration * 1000).setMediaType(0);
                        entry.setVolume(allVideoVolumeGone ? 0.0f : model.getVideoVolume());
                        entry.setFilterIndex(i);
                        entry.setSpeed(model.getVideoSpeed());
                        entry.setFramerate(model.getVideoFrameRate());
                        entry.setOutputRotation((float) model.getVideoRotate());
                        boolean shouldVertical = model.isShouldVertical();
                        if (model.isFrontCamera) {
                            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, !shouldVertical, shouldVertical);
                        } else {
                            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, shouldVertical, shouldVertical);
                        }
                        cropParams = model.getInputCropParams();
                        entry.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                        entry.setZoom(InterpolatedFloat.createLinear(0, model.getZoomStart(), currentDuration * 1000, model.getZoomEnd()));
                    } else {
                        playlist = FilterViewUtils.resetEntryWhenNoFile(model, totalDuration, 1000, playlist, i, VideoBitmapsModel.instancesModel().getBitmap(model.getPlaylistMediaPath()));
                    }
                } else if (model.mediaType == 2 || model.mediaType == 4) {
                    Bitmap bitmap;
                    model.setTimeLineStartTime(totalDuration);
                    if (model.mediaType == 2) {
                        bitmap = VideoBitmapsModel.instancesModel().getBitmap(model.getPlaylistMediaPath());
                        cropParams = model.getInputCropParams();
                    } else {
                        bitmap = VideoBitmapsModel.instancesModel().getActivityBitmap();
                        cropParams = new float[4];
                        cropParams = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
                    }
                    Playlist.Entry add = playlist.add(bitmap, totalDuration, 0, currentDuration * 1000, Player.AspectMode.RENDER_CROP);
                    add.setSpeed(model.getVideoSpeed());
                    add.setFilterIndex(i);
                    add.setFramerate(model.getVideoFrameRate());
                    add.setOutputRotation((float) model.getVideoRotate());
                    if (model.isFrontCamera) {
                        add.setInputOrientation(Playlist.Rotation.ROTATE_0, true, false);
                    } else {
                        add.setInputOrientation(Playlist.Rotation.ROTATE_0, false, false);
                    }
                    add.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                    add.setZoom(InterpolatedFloat.createLinear(0, model.getZoomStart(), currentDuration * 1000, model.getZoomEnd()));
                }
                totalDuration += currentDuration * 1000;
            }
        }
        FilterViewUtils.addActiveBGM(videos, playlist);
        if (bgmModels == null || allAudioVolumeGone) {
            for (i = playlist.size() - 1; i >= 0; i--) {
                if (playlist.get(i).mMediaType == 1) {
                    playlist.remove(i);
                }
            }
        } else {
            FilterViewUtils.setPlaylistBGMs(bgmModels, playlist);
        }
        if (playlist.size() > 0 && this.mPlayer != null) {
            this.mPlayer.setPlaylist(playlist, false, playTimeUs);
        }
    }

    public void saveLongVideoPreviewPicture(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, long bitmapTime, int width, int height, String pictureSavePath, Playlist playlist) {
        getLongVideoPreviewPicture(filter, bitmapTime, width, height, pictureSavePath, playlist);
    }

    public void saveLongVideoPreviewPicture(long bitmapTime, int width, int height, String pictureSavePath, Playlist playlist) {
        getLongVideoPreviewPicture(bitmapTime, width, height, pictureSavePath, playlist);
    }

    public List<Bitmap> getVideoLongBitmaps(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, int bitmapCount, int width, int height, long progressTag, Playlist playlist) {
        return getVideoLongBitmaps(filter, 0, bitmapCount, width, height, progressTag, playlist);
    }

    public List<Bitmap> getVideoLongBitmaps(int bitmapCount, int width, int height, long progressTag, Playlist playlist) {
        return getVideoLongBitmaps(0, bitmapCount, width, height, progressTag, playlist);
    }

    public List<Bitmap> getVideoLongBitmaps(long startUs, int bitmapCount, int width, int height, long progressTag, Playlist playlist) {
        return Player.getBitmaps(playlist, startUs, bitmapCount, width, height, progressTag);
    }

    public List<Bitmap> getVideoLongBitmaps(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, long startUs, int bitmapCount, int width, int height, long progressTag, Playlist playlist) {
        return Player.getBitmaps(filter, playlist, startUs, bitmapCount, width, height, progressTag);
    }

    public boolean saveLongVideo(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, long videoProgressTag, int width, int height, String videoPath, int frameRate, Playlist playlist, int startProgress, boolean localVideo, boolean needStop) {
        File videoFile = new File(videoPath);
        if (videoFile.exists()) {
            videoFile.delete();
        }
        return encodeLongVideo(filters, videoProgressTag, videoPath, width, height, frameRate, localVideo, playlist, startProgress, needStop);
    }

    public long getVideoCurrentPlayTime() {
        return this.mPlayer.getPlaylistTimeUs();
    }

    private void getCurrentPlayPercent() {
        if (!this.mPlayer.mIsPaused) {
            long mPlaylistTimeUs = this.mPlayer.getPlaylistTimeUs();
            this.playlistTimeUs = mPlaylistTimeUs;
            double percent = ((double) mPlaylistTimeUs) / ((double) this.mPlayer.getPlaylist().getDurationUs());
            double percent2 = ((double) this.mPlayer.getCurrentPlayTimeUs()) / ((double) this.mPlayer.getPlaylist().getDurationUs());
            double sumPercent = percent;
            if (this.mLongVideoCallback != null) {
                if (percent > 0.0d) {
                    String percentString = String.valueOf(percent);
                    if (percent != 0.0d) {
                        percentString = "0." + percentString.split("\\.")[1];
                    } else {
                        percentString = "0.0";
                    }
                    percent = Double.parseDouble(percentString);
                }
                boolean moreThanOnce = false;
                if (percent < this.lastPercent) {
                    moreThanOnce = true;
                }
                this.lastPercent = percent;
                this.mLongVideoCallback.playlistPercent(percent, sumPercent, moreThanOnce, this.playlistTimeUs);
                this.mLongVideoCallback.playlistPercentForLocal(percent2);
            }
        }
    }

    private Bitmap loadBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath(), new Options());
    }

    public Playlist initDraftVideo(List<LongVideosModel> videoModels, List<LongVideosModel> musicModels, boolean[] mediaMute) {
        return FilterViewUtils.initDraftPlaylist(videoModels, musicModels, VideoBitmapsModel.instancesModel().getBitmap(""), mediaMute);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0040 A:{SYNTHETIC, Splitter: B:23:0x0040} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0034 A:{SYNTHETIC, Splitter: B:17:0x0034} */
    private void getLongVideoPreviewPicture(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> r13, long r14, int r16, int r17, java.lang.String r18, Playlist r19) {
        /*
        r12 = this;
        r2 = r13;
        r3 = r19;
        r4 = r14;
        r6 = r16;
        r7 = r17;
        r8 = com.blink.academy.onetake.VideoTools.Player.getBitmap(r2, r3, r4, r6, r7);
        r10 = 0;
        r11 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x002e }
        r0 = r18;
        r11.<init>(r0);	 Catch:{ FileNotFoundException -> 0x002e }
        r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ FileNotFoundException -> 0x004c, all -> 0x0049 }
        r3 = 100;
        r8.compress(r2, r3, r11);	 Catch:{ FileNotFoundException -> 0x004c, all -> 0x0049 }
        if (r11 == 0) goto L_0x004f;
    L_0x001d:
        r11.close();	 Catch:{ IOException -> 0x0028 }
        r10 = r11;
    L_0x0021:
        com.blink.academy.onetake.support.utils.BitmapUtil.recycleBitmap(r8);
        java.lang.System.gc();
        return;
    L_0x0028:
        r9 = move-exception;
        r9.printStackTrace();
        r10 = r11;
        goto L_0x0021;
    L_0x002e:
        r9 = move-exception;
    L_0x002f:
        r9.printStackTrace();	 Catch:{ all -> 0x003d }
        if (r10 == 0) goto L_0x0021;
    L_0x0034:
        r10.close();	 Catch:{ IOException -> 0x0038 }
        goto L_0x0021;
    L_0x0038:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0021;
    L_0x003d:
        r2 = move-exception;
    L_0x003e:
        if (r10 == 0) goto L_0x0043;
    L_0x0040:
        r10.close();	 Catch:{ IOException -> 0x0044 }
    L_0x0043:
        throw r2;
    L_0x0044:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0043;
    L_0x0049:
        r2 = move-exception;
        r10 = r11;
        goto L_0x003e;
    L_0x004c:
        r9 = move-exception;
        r10 = r11;
        goto L_0x002f;
    L_0x004f:
        r10 = r11;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.FilterView.getLongVideoPreviewPicture(java.util.HashMap, long, int, int, java.lang.String, com.blink.academy.onetake.VideoTools.Playlist):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002a A:{SYNTHETIC, Splitter: B:17:0x002a} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0036 A:{SYNTHETIC, Splitter: B:23:0x0036} */
    private void getLongVideoPreviewPicture(long r8, int r10, int r11, java.lang.String r12, Playlist r13) {
        /*
        r7 = this;
        r0 = com.blink.academy.onetake.VideoTools.Player.getBitmap(r13, r8, r10, r11);
        r2 = 0;
        r3 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x0024 }
        r3.<init>(r12);	 Catch:{ FileNotFoundException -> 0x0024 }
        r4 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ FileNotFoundException -> 0x0042, all -> 0x003f }
        r5 = 100;
        r0.compress(r4, r5, r3);	 Catch:{ FileNotFoundException -> 0x0042, all -> 0x003f }
        if (r3 == 0) goto L_0x0045;
    L_0x0013:
        r3.close();	 Catch:{ IOException -> 0x001e }
        r2 = r3;
    L_0x0017:
        com.blink.academy.onetake.support.utils.BitmapUtil.recycleBitmap(r0);
        java.lang.System.gc();
        return;
    L_0x001e:
        r1 = move-exception;
        r1.printStackTrace();
        r2 = r3;
        goto L_0x0017;
    L_0x0024:
        r1 = move-exception;
    L_0x0025:
        r1.printStackTrace();	 Catch:{ all -> 0x0033 }
        if (r2 == 0) goto L_0x0017;
    L_0x002a:
        r2.close();	 Catch:{ IOException -> 0x002e }
        goto L_0x0017;
    L_0x002e:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0017;
    L_0x0033:
        r4 = move-exception;
    L_0x0034:
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2.close();	 Catch:{ IOException -> 0x003a }
    L_0x0039:
        throw r4;
    L_0x003a:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0039;
    L_0x003f:
        r4 = move-exception;
        r2 = r3;
        goto L_0x0034;
    L_0x0042:
        r1 = move-exception;
        r2 = r3;
        goto L_0x0025;
    L_0x0045:
        r2 = r3;
        goto L_0x0017;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.FilterView.getLongVideoPreviewPicture(long, int, int, java.lang.String, com.blink.academy.onetake.VideoTools.Playlist):void");
    }

    public boolean encodeLongVideo(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, long videoProgressTag, String savePath, int width, int height, int frameRate, boolean largeVideo, Playlist playlist, int startProgress) {
        return encodeLongVideo(filters, videoProgressTag, savePath, width, height, frameRate, largeVideo, playlist, startProgress, false);
    }

    public boolean encodeLongVideo(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, long videoProgressTag, String savePath, int width, int height, int frameRate, boolean largeVideo, Playlist playlist, int startProgress, boolean needStop) {
        int bitrate;
        long t1 = System.currentTimeMillis();
        if (largeVideo) {
            bitrate = (int) ((10.1f * ((float) width)) * ((float) height));
        } else {
            bitrate = (int) ((5.13f * ((float) width)) * ((float) height));
        }
        try {
            if (!Player.encodeMP4(filters, savePath, playlist, bitrate, width, height, frameRate, videoProgressTag, startProgress, needStop)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FastStart.MalformedFileException e2) {
            e2.printStackTrace();
        } catch (FastStart.UnsupportedFileException e3) {
            e3.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        EventBus.getDefault().post(new VideoPreviewEvent(videoProgressTag));
        LogUtil.e("encodeMP4", String.format("encode time : %s", new Object[]{Long.valueOf(t2 - t1)}));
        return true;
    }

    private void changeVideoZoomOutIn(int position, LongVideosModel videosModel, int changeState) {
        float startValue;
        float endValue;
        switch (changeState) {
            case 1:
                startValue = 1.0f;
                endValue = 1.3f;
                break;
            case 2:
                startValue = 1.3f;
                endValue = 1.0f;
                break;
            default:
                startValue = 1.0f;
                endValue = 1.0f;
                break;
        }
        this.mPlayer.getPlaylist().get(position).setZoom(InterpolatedFloat.createLinear(videosModel.getMediaStartTimeUs(), startValue, videosModel.getMediaStartTimeUs() + (videosModel.getOriginalCurrentDuration() * 1000), endValue));
    }

    public void stopLongVideo() {
        queueRunnableSync("stopLongVideo", new Runnable() {
            public void run() {
                FilterView.this.setVideoPauseOrResume(true);
                FilterView.this.shouldInitPlayer = false;
                FilterView.this.setLongVideoStop();
            }
        });
    }

    private void renderLongVideo() {
        Playlist pl;
        if (this.mPlayer == null) {
            if (this.mDataFrom == 3) {
                pl = initDraftVideo(this.mVideoModelList, this.bgmModelList, this.mediaMute);
            } else if (this.mDataFrom == 4) {
                pl = FilterViewUtils.initializeVideo(this.mVideoModelList, false);
            } else {
                pl = FilterViewUtils.initializeVideo(this.mVideoModelList, true);
            }
            if (pl != null && pl.size() != 0) {
                this.mPlayer = Player.create();
                this.mPlayer.setOutputRect(0, 0, this.mWidth, this.mHeight);
                if (this.mDataFrom == 4 || this.shouldPauseVideoFirst) {
                    this.mPlayer.pause();
                }
                this.mPlayer.setPlaylist(pl, false);
                if (this.mLongVideoCallback != null) {
                    this.mLongVideoCallback.couldRenderPreview();
                }
            } else {
                return;
            }
        } else if (this.pausePlaylist != null) {
            if (this.shouldInitPlayer) {
                this.shouldInitPlayer = false;
                this.mPlayer = Player.create();
                this.mPlayer.setOutputRect(0, 0, this.mWidth, this.mHeight);
                pl = new Playlist();
                int size = this.pausePlaylist.size();
                for (int i = 0; i < size; i++) {
                    pl.add(this.pausePlaylist.get(i).clone());
                }
                this.mPlayer.pause();
                this.mPlayer.setPlaylist(pl, false, this.playlistTimeUs);
                if (this.mLongVideoCallback != null) {
                    this.mLongVideoCallback.couldRenderPreview();
                }
                this.pausePlaylist = null;
            } else {
                return;
            }
        }
        if (this.mTargetSeekActive) {
            Log.d(TAG, String.format("processing seek request for %dus", new Object[]{Long.valueOf(this.mTargetSeekTimeUs)}));
            this.mPlayer.seekTo(this.mTargetSeekTimeUs);
            this.mTargetSeekActive = false;
        }
        GLES20.glBindFramebuffer(36160, 0);
        if (this.mPlayer.drawFrame()) {
            this.egl.swap(this.mEGLSurface);
        }
        getCurrentPlayPercent();
        if (this.mLongVideoCallback != null) {
            this.mLongVideoCallback.startDrawFrame();
        }
        long t2 = System.nanoTime();
    }

    public Bitmap getLongVideoPreview(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, int width, int height, Playlist playlist) {
        return getLongVideoPreview(filter, 0, width, height, playlist);
    }

    public Bitmap getLongVideoPreview(int width, int height, Playlist playlist) {
        return getLongVideoPreview(0, width, height, playlist);
    }

    public Bitmap getLongVideoPreview(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, Playlist playlist, int width, int height) {
        return Player.getBitmap(filter, playlist, 0, width, height);
    }

    public Bitmap getLongVideoPreview(HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filter, long startTime, int width, int height, Playlist playlist) {
        return Player.getBitmap(filter, playlist, startTime, width, height);
    }

    public Bitmap getLongVideoPreview(long startTime, int width, int height, Playlist playlist) {
        return Player.getBitmap(playlist, startTime, width, height);
    }

    public Player getPlayer() {
        return this.mPlayer;
    }

    protected void onGLRender(double time, long nanos) {
        long t1 = System.nanoTime();
        if (this.mDataType == 2) {
            renderLongVideo();
            return;
        }
        int outX = 0;
        int outY = 0;
        int outWidth = this.mWidth;
        int outHeight = this.mHeight;
        if (this.mFrameRenderer.mode == FrameRenderer.RenderMode.RENDER_CROP) {
            if (this.mCaptureMode == 1) {
                if (this.mWidth < this.mHeight) {
                    outX = 0;
                    outY = (int) (((float) Math.abs(outHeight - outWidth)) / 2.0f);
                    outHeight = outWidth;
                } else {
                    outY = 0;
                    outX = (int) (((float) Math.abs(outHeight - outWidth)) / 2.0f);
                    outWidth = outHeight;
                }
            } else if (this.mCaptureMode == 2) {
            }
        }
        this.mFrameRenderer.drawFrameAtTime(0, outX, outY, outWidth, outHeight, 0, 0, this.mWidth, this.mHeight, time, this.pauseAtEnd, this.isSliding);
        if (this.mEGLSurface != null) {
            this.egl.swap(this.mEGLSurface);
        }
        long t2 = System.nanoTime();
    }

    public void pauseAtZero() {
        pauseAtZero(false, false);
    }

    public void pauseAtZero(boolean pauseAtEnd, boolean isSliding) {
        this.pauseAtEnd = pauseAtEnd;
        this.isSliding = isSliding;
    }

    public void start() {
        this.pauseAtEnd = false;
        this.isSliding = false;
    }

    public void setOnFrameIndexChangedListener(OnFrameIndexChangedListener listener) {
        this.mOnFrameIndexChangedListener = listener;
    }

    private void _changePlaylistFilters(GPUImageFilter publicFilter, List<Integer> publicArray, HashMap<Integer, GPUImageFilter> privateFilters) {
        if (privateFilters != null) {
            for (Map.Entry<Integer, GPUImageFilter> entry : privateFilters.entrySet()) {
                this.mPlayer.setFilter(((Integer) entry.getKey()).intValue(), (GPUImageFilter) entry.getValue());
            }
            privateFilters.clear();
        }
        if (publicFilter != null) {
            int size = publicArray.size();
            for (int i = 0; i < size; i++) {
                this.mPlayer.setFilter(((Integer) publicArray.get(i)).intValue(), publicFilter);
            }
            publicArray.clear();
        }
    }

    public void changePlaylistFilters(final GPUImageFilter publicFilter, final List<Integer> publicArray, final HashMap<Integer, GPUImageFilter> privateFilters) {
        queueRunnable(new Runnable() {
            public void run() {
                FilterView.this._changePlaylistFilters(publicFilter, publicArray, privateFilters);
            }
        });
    }

    public Framebuffer newFramebuffer(final int width, final int height) {
        Runnable cmd = new Runnable() {
            Framebuffer fb = null;

            public void run() {
                if (FilterView.this.mEGLSurface != null) {
                    FilterView.this.egl.makeCurrent(FilterView.this.mEGLSurface);
                }
                this.fb = new Framebuffer("newFramebuffer", width, height);
            }
        };
        queueRunnableSync("newFramebufferTexture", cmd);
        //return cmd.fb;
        return null;
    }

    public void destroyFramebuffer(final Framebuffer fb) {
        queueRunnableSync("destroyFramebufferTexture", new Runnable() {
            public void run() {
                fb.destroy();
            }
        });
    }

    public void drawFrameToBuffer(FrameRenderer renderer, GPUImageFilter filter, int frame, Framebuffer fb) {
        final FrameRenderer frameRenderer = renderer;
        final GPUImageFilter gPUImageFilter = filter;
        final int i = frame;
        final Framebuffer framebuffer = fb;
        queueRunnableSync("drawFrameTobuffer", new Runnable() {
            public void run() {
                if (FilterView.this.mEGLSurface != null) {
                    FilterView.this.egl.makeCurrent(FilterView.this.mEGLSurface);
                }
                FilterView.this.drawFrameToBufferInCurrent(frameRenderer, gPUImageFilter, i, framebuffer);
            }
        });
    }

    public void drawFrameToBufferInCurrent(FrameRenderer renderer, GPUImageFilter filter, int frame, Framebuffer fb) {
        int w = fb.width;
        int h = fb.height;
        OutputSurfaceArray frames = renderer.getVideoFrames();
        if (frames != null) {
            float[] oldMatrix = frames.mRenderMatrix;
            float[] fArr = new float[16];
            frames.mRenderMatrix = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
            int cropWidth = frames.mCropWidth;
            int cropHeight = frames.mCropHeight;
            frames.mCropWidth = frames.mCaptureWidth;
            frames.mCropHeight = frames.mCaptureHeight;
            frames.getAlphaFrame = true;
            renderer.drawFrameAtIndexWithFilter(fb.fboid(), 0, 0, w, h, 0, 0, w, h, frame, filter);
            frames.getAlphaFrame = false;
            frames.mCropWidth = cropWidth;
            frames.mCropHeight = cropHeight;
            frames.mRenderMatrix = oldMatrix;
        }
    }

    public void destroyFilter(final GPUImageFilter filter) {
        queueRunnableSync("destroFilter", new Runnable() {
            public void run() {
                filter.destroy();
                filter.destroySecondary();
            }
        });
    }

    public FramebufferTexture newFramebufferTexture(final int width, final int height) {
//        return (FramebufferTexture) EGL10Helper.withContext("newFramebufferTexture", FilterView$$Lambda$1.lambdaFactory$(width, height));
        return (FramebufferTexture) EGL10Helper.withContext("newFramebufferTexture", new EGLRunnable<FramebufferTexture>() {
            @Override
            public FramebufferTexture run(EGL10Helper eGL10Helper) {
                return new FramebufferTexture(width, height, 6407);
            }
        });
    }

    public void destroyFramebufferTexture(final FramebufferTexture fb) {
        //EGL10Helper.withContext("destroyFramebufferTexture", FilterView$$Lambda$2.lambdaFactory$(fb));
        EGL10Helper.withContext("destroyFramebufferTexture", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                fb.destroy();
            }
        });
    }

    public void updateLUT(final GPUImageFilter filter, final FramebufferTexture target) {
        queueRunnable(new Runnable() {
            public void run() {
                LUTCreator.initialize();
                FilterView.this.mLUTCreator.update(filter, target);
            }
        });
    }

    public void updateLUT2(final GPUImageFilter filter, final FramebufferTexture target) {
        queueRunnable(new Runnable() {
            public void run() {
                LUTCreator.initialize();
                FilterView.this.mLUTCreator.update2(filter, target);
            }
        });
    }
}