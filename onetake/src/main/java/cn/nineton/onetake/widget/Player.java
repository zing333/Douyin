package cn.nineton.onetake.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

//import com.alipay.sdk.data.a;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.VideoTools.FastStart.MalformedFileException;
//import com.blink.academy.onetake.VideoTools.FastStart.UnsupportedFileException;
//import com.blink.academy.onetake.VideoTools.OutputSurfaceArray.Buffer;
//import com.blink.academy.onetake.VideoTools.OutputSurfaceArray.Frame;
//import com.blink.academy.onetake.VideoTools.Playlist.FileMedia;
//import com.blink.academy.onetake.VideoTools.Playlist.Rotation;
//import com.blink.academy.onetake.VideoTools.TimingSource.AudioMixerTimingSource;
//import com.blink.academy.onetake.VideoTools.TimingSource.ExplicitTimingSource;
//import com.blink.academy.onetake.bean.AudioTrimBean;
//import com.blink.academy.onetake.support.database.table.IMMsgTable;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.share.ShareBitmapEntity;
//import com.blink.academy.onetake.support.utils.BitmapUtil;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
//import com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.OnHandleMP4ToVideoListener;

import org.greenrobot.eventbus.EventBus;

import cn.nineton.glsurfacedemo.onetake.FilterActivity;
import cn.nineton.onetake.App;
import cn.nineton.onetake.db.IMMsgTable;
import cn.nineton.onetake.event.VideoSavingEvent;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.media.FrameRenderer;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.Ratio;
import cn.nineton.onetake.media.audiotool.AudioMixer;
import cn.nineton.onetake.media.audiotool.AudioOutput;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.media.audiotool.DummyAudioOutput;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnable;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.Framebuffer;
import cn.nineton.onetake.media.gpuimage.FramebufferCache;
import cn.nineton.onetake.media.gpuimage.GPUImageCopyFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageLookupFilter2;
import cn.nineton.onetake.media.gpuimage.GPUImageTextureMatrixFilter;
import cn.nineton.onetake.media.gpuimage.ResourceTracker;
import cn.nineton.onetake.media.videotool.MP4Output;
import cn.nineton.onetake.util.FileUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.PerfTimer;
import cn.nineton.onetake.util.SaveVideoTool;
import cn.nineton.onetake.util.TextUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//import jp.co.cyberagent.android.gpuimage.Framebuffer;
//import jp.co.cyberagent.android.gpuimage.FramebufferCache;
//import jp.co.cyberagent.android.gpuimage.GPUImageCopyFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//import jp.co.cyberagent.android.gpuimage.ResourceTracker;

public class Player {
    public static final int ANIMATION_FILTER_INDEX = 2002;
    private static final String TAG = "Player";
    private static final boolean usePreactivation = false;
    BlockingQueue<Runnable> mActivatorMessages;
    Thread mActivatorThread;
    ArrayList<Playlist.Instance> mActive;
    private Comparator<Playlist.Instance> mActiveSorter;
    AudioMixer mAudioMixer;
    AudioOutput mAudioOutput;
    private float[] mBackgroundColor;
    long mBaseSample;
    private GPUImageCopyFilter mCopyFilter;
    private FloatBuffer mFilterCubeBuffer;
    private FloatBuffer mFilterTextureBuffer;
    private HashMap<Integer, GPUImageFilter> mFilters;
    long mHeadPlayPos;
    long mHeadPos;
    private FloatBuffer mInputCubeBuffer;
    private GPUImageTextureMatrixFilter mInputFilter;
    private FloatBuffer mInputTextureBuffer;
    private float[] mInputTextureMatrix;
    boolean mIsPaused;
    private boolean mIsStopped;
    int mNextSeekCount;
    long mNextSeekTime;
    private GPUImageFilter mNullFilter;
    private int mOutHeight;
    private int mOutWidth;
    private int mOutX;
    private int mOutY;
    private FloatBuffer mOutputCubeBuffer;
    private FloatBuffer mOutputTextureBuffer;
    PlayQueue mPlayQueue;
    long mPrevFrameNum;
    int mSeekCount;
    long mSeekSample;
    Ratio mSpeed;
    boolean mStopActivator;
    TimingSource mTimingSource;

    public enum AspectMode {
        RENDER_STRETCH,
        RENDER_CROP,
        RENDER_FILL
    }

    final class SourceTexture {
        Framebuffer fb;
        int height;
        int texid;
        int width;

        SourceTexture(OutputSurfaceArray.Buffer b) {
            this.width = b.mWidth;
            this.height = b.mHeight;
            this.texid = b.mTextureId;
            this.fb = null;
        }

        SourceTexture(Framebuffer _fb) {
            this.width = _fb.width;
            this.height = _fb.height;
            this.texid = _fb.texid();
            this.fb = _fb;
        }
    }

    public int getFiltersSize() {
        return this.mFilters.size();
    }

    public static void test() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/tmp");
        Playlist.FileMedia mp4 = Playlist.FileMedia.create(new File(dir, "jellyfish.mp4"));
        Playlist pl = new Playlist();
        pl.add(mp4, 0 * 1000000, 0 * 1000000, 10 * 1000000);
        String outFilename = new File(dir, "output.mp4").getAbsolutePath();
        try {
            long t1 = System.nanoTime();
            encodeMP4(null, outFilename, pl, 8000000, 1920, 1080, 30, -1, 0);
            long t2 = System.nanoTime();
            Log.d(TAG, String.format("test player took:%dus", new Object[]{Long.valueOf((t2 - t1) / 1000)}));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FastStart.MalformedFileException e) {
            e.printStackTrace();
        } catch (FastStart.UnsupportedFileException e) {
            e.printStackTrace();
        }
        Log.d("App", "finished player test");
    }

    public static Player create() {
        SystemOutput systemOutput = new SystemOutput();
        if (!(systemOutput == null || systemOutput.isValid())) {
            systemOutput.close();
            systemOutput = null;
        }
        if (systemOutput != null) {
            return new Player(systemOutput);
        }
        return new Player(new DummyAudioOutput());
    }

    private Player(AudioOutput output) {
        this.mIsStopped = false;
        this.mBackgroundColor = new float[]{0.0745f, 0.0706f, 0.0667f, 1.0f};
        this.mPrevFrameNum = -1;
        this.mInputFilter = new GPUImageTextureMatrixFilter();
        this.mCopyFilter = new GPUImageCopyFilter();
        this.mNullFilter = new GPUImageFilter();
        this.mInputTextureMatrix = new float[16];
        this.mFilters = new HashMap();
        this.mStopActivator = false;
        this.mIsPaused = false;
        this.mSpeed = new Ratio(1, 1);
        this.mActivatorMessages = new LinkedBlockingDeque();
        this.mHeadPos = 0;
        this.mHeadPlayPos = 0;
        this.mNextSeekCount = 0;
        this.mNextSeekTime = 0;
        this.mBaseSample = 0;
        this.mSeekSample = 0;
        this.mActive = new ArrayList();
        this.mActiveSorter = new Comparator<Playlist.Instance>() {
            public int compare(Playlist.Instance a, Playlist.Instance b) {
                return a.mEntry.mZIndex - b.mEntry.mZIndex;
            }
        };
        this.mAudioOutput = output;
        this.mAudioMixer = new AudioMixer(output);
        this.mTimingSource = new TimingSource.AudioMixerTimingSource(this.mAudioMixer);
        init();
    }

    private Player(TimingSource timingSource) {
        this.mIsStopped = false;
        this.mBackgroundColor = new float[]{0.0745f, 0.0706f, 0.0667f, 1.0f};
        this.mPrevFrameNum = -1;
        this.mInputFilter = new GPUImageTextureMatrixFilter();
        this.mCopyFilter = new GPUImageCopyFilter();
        this.mNullFilter = new GPUImageFilter();
        this.mInputTextureMatrix = new float[16];
        this.mFilters = new HashMap();
        this.mStopActivator = false;
        this.mIsPaused = false;
        this.mSpeed = new Ratio(1, 1);
        this.mActivatorMessages = new LinkedBlockingDeque();
        this.mHeadPos = 0;
        this.mHeadPlayPos = 0;
        this.mNextSeekCount = 0;
        this.mNextSeekTime = 0;
        this.mBaseSample = 0;
        this.mSeekSample = 0;
        this.mActive = new ArrayList();
        this.mActiveSorter = null;/* anonymous class already generated */;
        this.mAudioMixer = null;
        this.mTimingSource = timingSource;
        init();
    }

    private void init() {
        float[] cubeCoords = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        float[] texCoords = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mPlayQueue = new PlayQueue(this.mTimingSource);
        this.mFilters.put(Integer.valueOf(-1), new GPUImageFilter());
        this.mInputCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mInputCubeBuffer.put(cubeCoords).position(0);
        this.mInputTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mInputTextureBuffer.put(texCoords).position(0);
        this.mFilterCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mFilterCubeBuffer.put(cubeCoords).position(0);
        this.mFilterTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mFilterTextureBuffer.put(texCoords).position(0);
        this.mOutputCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mOutputTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mActivatorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                activatorThread();
            }
        });
        this.mActivatorThread.setName("Activator");
        this.mActivatorThread.start();
    }

    void setAnimationFilter(GPUImageFilter filter) {
        this.mFilters.put(Integer.valueOf(2002), filter);
    }

    public void setFilterWithAnimation(int index, GPUImageFilter filter) {
        GPUImageFilter oldFilter;
        if (3 == index) {
            oldFilter = (GPUImageFilter) this.mFilters.get(Integer.valueOf(2));
            if (oldFilter != null) {
                setFilterWithAnimation(1, oldFilter);
            }
            this.mFilters.put(Integer.valueOf(2), filter);
            return;
        }
        oldFilter = (GPUImageFilter) this.mFilters.get(Integer.valueOf(1));
        if (oldFilter != null) {
            oldFilter.destroy();
            oldFilter.destroySecondary();
            this.mFilters.remove(Integer.valueOf(index));
        }
        this.mFilters.put(Integer.valueOf(index), filter);
    }

    public void setFilter(int index, GPUImageFilter filter) {
        GPUImageFilter oldFilter = (GPUImageFilter) this.mFilters.get(Integer.valueOf(index));
        if (oldFilter != null) {
            oldFilter.destroy();
            oldFilter.destroySecondary();
            this.mFilters.remove(Integer.valueOf(index));
        }
        this.mFilters.put(Integer.valueOf(index), filter);
    }

    public Playlist getPlaylist() {
        return this.mPlayQueue.mPlaylist;
    }

    public void pause() {
        Log.d(TAG, "pause");
        if (!this.mIsStopped && !this.mIsPaused) {
            Log.d(TAG, "pause");
            this.mTimingSource.pause();
            this.mIsPaused = true;
        }
    }

    public void resume() {
        Log.d(TAG, "resume");
        if (!this.mIsStopped && this.mIsPaused) {
            Log.d(TAG, "resume");
            this.mTimingSource.resume();
            this.mIsPaused = false;
        }
    }

    public synchronized void stop() {
        Log.d(TAG, "stop");
        this.mIsStopped = true;
        deactivateAll();
        if (this.mActivatorThread != null) {
            try {
                this.mActivatorMessages.add(new Runnable() {
                    public void run() {
                        Player.this.mStopActivator = true;
                    }
                });
                this.mActivatorThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mActivatorThread = null;
        }
//        EGL10Helper.withContext("Player.stop", Player$$Lambda$2.lambdaFactory$(this));
        EGL10Helper.withContext("Player.stop", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$stop$0(eGL10Helper);
            }
        });
        if (this.mAudioMixer != null) {
            this.mAudioMixer.close();
            this.mAudioMixer = null;
        }
        if (this.mAudioOutput != null) {
            this.mAudioOutput.close();
            this.mAudioOutput = null;
        }
        return;
    }

    private /* synthetic */ void lambda$stop$0(EGL10Helper egl) {
        for (GPUImageFilter filter : this.mFilters.values()) {
            filter.destroy();
            filter.destroySecondary();
        }
        if (this.mCopyFilter != null) {
            this.mCopyFilter.destroy();
            this.mCopyFilter = null;
        }
        if (this.mNullFilter != null) {
            this.mNullFilter.destroy();
            this.mNullFilter = null;
        }
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        this.mBackgroundColor[0] = r;
        this.mBackgroundColor[1] = g;
        this.mBackgroundColor[2] = b;
        this.mBackgroundColor[3] = a;
    }

    public void setOutputRect(int x, int y, int width, int height) {
        this.mOutX = x;
        this.mOutY = y;
        this.mOutWidth = width;
        this.mOutHeight = height;
    }

    public static void encodeMP4withWatermark(ShareBitmapEntity entity, String filePath, String savePaht, int width, int height, ShareActionSheetDialog.OnHandleMP4ToVideoListener listener) throws IOException {
        long tt1 = System.nanoTime() / 1000000;
        if (new File(filePath).exists()) {
            if (listener != null) {
                listener.onStart();
            }
            Playlist playlist = new Playlist();
            Playlist.FileMedia fileMedia = Playlist.FileMedia.create(new File(filePath));
            if (fileMedia != null) {
                playlist.add(fileMedia, 0);
                if (!(entity.getBitmap() == null || entity.getBitmap().isRecycled())) {
                    playlist.add(entity.getBitmap(), 0, 0, fileMedia.getDurationUs()).setOutputRect(entity.getSize()[0].floatValue(), entity.getSize()[1].floatValue(), entity.getSize()[2].floatValue(), entity.getSize()[3].floatValue()).setAlpha(entity.getSize()[4].floatValue());//setZIndex(a.c);
                }
                try {
                    encodeMP4(null, savePaht, playlist, 4000000, width, height, 30, -1, 0);
                } catch (FastStart.UnsupportedFileException e) {
                    e.printStackTrace();
                } catch (FastStart.MalformedFileException e) {
                    e.printStackTrace();
                }
                LogUtil.d("slim", "cost:" + ((System.nanoTime() / 1000000) - tt1));
                if (listener != null) {
                    listener.onFinish(savePaht, entity.getBitmap());
                }
            }
        } else if (listener != null) {
            listener.onFail(savePaht);
        }
    }

    public static boolean encodeMP4(@Nullable HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, String filename, Playlist playlist, int bitrate, int width, int height, int frameRate, long progressTag, int startProgress) throws IOException, FastStart.UnsupportedFileException, FastStart.MalformedFileException {
        return encodeMP4(filters, filename, playlist, bitrate, width, height, frameRate, progressTag, startProgress, false);
    }

    public static boolean encodeMP4(@Nullable final HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, String filename, final Playlist playlist, int bitrate, int _width, int _height, int frameRate, long progressTag, int startProgress, boolean needStopAt95) throws IOException, FastStart.UnsupportedFileException, FastStart.MalformedFileException {
        final File file = new File(filename + ".tmp.mp4");
        File dstFile = new File(filename);
        final MP4Output mp4 = new MP4Output(file.getAbsolutePath());
        mp4.enableVideo(bitrate, _width, _height, AudioTrimBean.UNIT / frameRate, null, false);
        mp4.enableAudio(44100, 128000);
        final int width = mp4.mVideoEncoder.getWidth();
        final int height = mp4.mVideoEncoder.getHeight();
        final Player player = new Player(mp4);
        //EGL10Helper.withContext("encodeMP4", Player$$Lambda$3.lambdaFactory$(player, playlist, width, height, filters));
        EGL10Helper.withContext("encodeMP4", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                //lambda$encodeMP4$1(player,playlist,width,height,filters,eGL10Helper);
                player.mAudioMixer.setMixLimit(0);
                player.setPlaylist(playlist);
                player.setOutputRect(0, 0, width, height);
                if (filters != null) {
                    for (Map.Entry<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> entry : filters.entrySet()) {
                        HashMap<Integer, GPUImageFilter> publicArray = entry.getKey();
                        HashMap<Integer, GPUImageFilter> publicValue = entry.getValue();
                        for (Map.Entry<Integer, GPUImageFilter> entry1 : publicArray.entrySet()) {
                            player.setFilter(entry1.getKey().intValue(), entry1.getValue());
                        }
                        publicArray.clear();
                        for (Map.Entry<Integer, GPUImageFilter> entry12 : publicValue.entrySet()) {
                            player.setFilter(entry12.getKey().intValue(), entry12.getValue());
                        }
                    }
                }
            }
        });
        long playlistDurationUs = playlist.getDurationUs();
        player.mAudioMixer.enableMixing();
        long frameNum = 0;
        long t1 = System.currentTimeMillis();
        while (!App.shouldAbortEncoding()) {
            final long timeUs = (1000000 * frameNum) / ((long) frameRate);
            long timeSamples = (player.mAudioMixer.getSampleRate() * frameNum) / ((long) frameRate);
            long remapTimeUs = player.remapSample(timeUs);
            long remapTimeSamples = player.remapSample(timeSamples);
            if (remapTimeUs >= playlistDurationUs) {
                long t2 = System.currentTimeMillis();
                player.flush();
                player.stop();
                long t3 = System.currentTimeMillis();
                if (!(FastStart.fastStart(file, dstFile) || (dstFile.delete() && file.renameTo(dstFile)))) {
                    FileUtil.copyFile(file, dstFile.getAbsolutePath());
                }
                LogUtil.e("PlayerEncodeVideo", String.format("videoPath : %s, exit : %s", new Object[]{dstFile.getAbsolutePath(), Boolean.valueOf(dstFile.exists())}));
                file.delete();
                long t4 = System.currentTimeMillis();
                Log.e("encodeMp4", String.format("t2-t1:%s, t3-t2:%s, t4-t3:%s", new Object[]{Long.valueOf(t2 - t1), Long.valueOf(t3 - t2), Long.valueOf(t4 - t3)}));
                return true;
            }
            Log.d(TAG, String.format("frame:%d timeSamples:%d", new Object[]{Long.valueOf(frameNum), Long.valueOf(timeSamples)}));
            player.mAudioMixer.setMixLimit(remapTimeSamples);
            player.mAudioMixer.waitUntilTime(remapTimeSamples);
            mp4.setPlayPosition(timeSamples);
            //EGL10Helper.withContext("Player.drawFrame", Player$$Lambda$4.lambdaFactory$(mp4, timeUs, player));
            EGL10Helper.withContext("Player.drawFrame", new EGLRunnableVoid() {
                @Override
                public void run(EGL10Helper eGL10Helper) {
                    mp4.startFrame(eGL10Helper, timeUs);
                    if (player.drawFrame(true)) {
                        mp4.finishFrame(eGL10Helper);
                    }
                }
            });
            double percent = ((double) player.getPlaylistTimeUs()) / ((double) player.getPlaylist().getDurationUs());
            if (percent > 0.0d) {
                String percentString = String.valueOf(percent);
                if (percent != 0.0d) {
                    percentString = "0." + percentString.split("\\.")[1];
                } else {
                    percentString = "0.0";
                }
                int displayProgress = (int) Math.ceil(((double) startProgress) + ((100.0d * Double.parseDouble(percentString)) * ((double) FilterActivity.Encode_Video_Percent)));
                if (needStopAt95 && displayProgress > 95) {
                    displayProgress = 95;
                }
                EventBus.getDefault().post(new VideoSavingEvent(displayProgress, IMMsgTable.TYPE_VIDEO, progressTag));
            }
            frameNum++;
        }
        player.stop();
        if (file.exists()) {
            file.delete();
        }
        return false;
    }


    private static /* synthetic */ void lambda$encodeMP4$2(MP4Output mp4, long timeUs, Player player, EGL10Helper egl) {
        mp4.startFrame(egl, timeUs);
        if (player.drawFrame(true)) {
            mp4.finishFrame(egl);
        }
    }

    public static List<Bitmap> getBitmaps(final String videoPath, final long startUs, final int targetCount, final long intervalUs, final int width, final int height) {
        //return (List) EGL10Helper.withContext("Player.getBitmaps", Player$$Lambda$5.lambdaFactory$(videoPath, width, height, startUs, targetCount, intervalUs));
        return EGL10Helper.withContext("Player.getBitmaps", new EGLRunnable<List<Bitmap>>() {
            @Override
            public List<Bitmap> run(EGL10Helper eGL10Helper) {
                if (TextUtil.isNull(videoPath)) {
                    return null;
                }
                File file = new File(videoPath);
                if (!file.exists()) {
                    return null;
                }
                Playlist pl = new Playlist();
                pl.add(Playlist.FileMedia.create(file), 0);
                List mList = new ArrayList();
                try {
                    EGLSurface eglSurface = eGL10Helper.createPBuffer(width, height);
                    eGL10Helper.makeCurrent(eglSurface);
                    TimingSource.ExplicitTimingSource timing = new TimingSource.ExplicitTimingSource();
                    Player player = new Player(timing);
                    player.setPlaylist(pl);
                    player.setOutputRect(0, 0, width, height);
                    timing.setPlayTime(0);
                    player.seekTo(startUs);
                    for (int i = 0; i < targetCount; i++) {
                        timing.setPlayTime(((long) i) * intervalUs);
                        player.drawFrame(true);
                        GLES20.glViewport(0, 0, width, height);
                        mList.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(width, height, width, height)));
                    }
                    player.stop();
                    eGL10Helper.destroySurface(eglSurface);
                    return mList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return mList;
                }
            }
        });
    }

    private static /* synthetic */ List lambda$getBitmaps$3(String videoPath, int width, int height, long startUs, int targetCount, long intervalUs, EGL10Helper egl) {
        return null;
    }

    public static List<Bitmap> getBitmaps(@NonNull final HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, final Playlist playlist, final long startUs, final int targetCount, final int width, final int height, final long progressTag) {
        //return (List) EGL10Helper.withContext("Player.getBitmaps", Player$$Lambda$6.lambdaFactory$(width, height, playlist, filters, startUs, targetCount, progressTag));
        return EGL10Helper.withContext("Player.getBitmaps", new EGLRunnable<List<Bitmap>>() {
            @Override
            public List<Bitmap> run(EGL10Helper egl) {
                List<Bitmap> mList = new ArrayList();
                EGLSurface eglSurface = egl.createPBuffer(width, height);
                try {
                    long t3 = System.currentTimeMillis();
                    egl.makeCurrent(eglSurface);
                    TimingSource.ExplicitTimingSource timing = new TimingSource.ExplicitTimingSource();
                    Player player = new Player(timing);
                    player.setPlaylist(playlist);
                    player.setOutputRect(0, 0, width, height);
                    if (filters != null) {
                        for (Map.Entry<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> entry : filters.entrySet()) {
                            HashMap<Integer, GPUImageFilter> publicArray = entry.getKey();
                            HashMap<Integer, GPUImageFilter> publicValue = entry.getValue();
                            for (Map.Entry<Integer, GPUImageFilter> entry1 : publicArray.entrySet()) {
                                player.setFilter(entry1.getKey(), entry1.getValue());
                            }
                            publicArray.clear();
                            for (Map.Entry<Integer, GPUImageFilter> entry12 : publicValue.entrySet()) {
                                player.setFilter(entry12.getKey(), entry12.getValue());
                            }
                        }
                    }
                    timing.setPlayTime(0);
                    player.seekTo(startUs);
                    long t4 = System.currentTimeMillis();
                    for (int i = 0; i < targetCount; i++) {
                        if (App.shouldAbortEncoding()) {
                            for (int j = mList.size() - 1; j >= 0; j++) {
                                BitmapUtils.recycleBitmap((Bitmap) mList.remove(j));
                            }
                            mList.clear();
                            player.stop();
                            mList = null;
                            return mList;
                        }
                        long playTimeUs = (long) (100000 * i);
                        LogUtil.d(TAG, String.format("getBitmaps setPlayTime : %s", new Object[]{Long.valueOf(playTimeUs)}));
                        timing.setPlayTime(playTimeUs);
                        player.drawFrame(true);
                        GLES20.glViewport(0, 0, width, height);
                        mList.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(width, height, width, height)));
                        EventBus.getDefault().post(new VideoSavingEvent(i + 5, "longVideoBs", progressTag));
                    }
                    long t1 = System.currentTimeMillis();
                    player.stop();
                    Log.d("longvideobitmaps", "time1: " + (System.currentTimeMillis() - t1));
                    Log.d("longvideobitmaps", "time2: " + (t4 - t3));
                    Log.d("longvideobitmaps", "time3: " + (t1 - t4));
                    egl.destroySurface(eglSurface);
                    return mList;
                } catch (Exception e) {
                    Log.e(TAG, "error in Player.getBitmaps", e);
                } finally {
                    egl.destroySurface(eglSurface);
                }
                return mList;
            }
        });
    }

    public static List<Bitmap> getBitmaps(final Playlist playlist, final long startUs, final int targetCount, final int width, final int height, final long progressTag) {
//        return (List) EGL10Helper.withContext("Player.getBitmap", Player$$Lambda$7.lambdaFactory$(width, height, playlist, startUs, targetCount, progressTag));
        return EGL10Helper.withContext("Player.getBitmap", new EGLRunnable<List<Bitmap>>() {
            @Override
            public List<Bitmap> run(EGL10Helper egl) {
                List<Bitmap> mList = new ArrayList();
                EGLSurface eglSurface = egl.createPBuffer(width, height);
                try {
                    long t3 = System.currentTimeMillis();
                    egl.makeCurrent(eglSurface);
                    TimingSource.ExplicitTimingSource timing = new TimingSource.ExplicitTimingSource();
                    Player player = new Player((TimingSource) timing);
                    player.setPlaylist(playlist);
                    player.setOutputRect(0, 0, width, height);
                    player.setFilter(0, new GPUImageFilter());
                    timing.setPlayTime(0);
                    player.seekTo(startUs);
                    long t4 = System.currentTimeMillis();
                    for (int i = 0; i < targetCount; i++) {
                        if (App.shouldAbortEncoding()) {
                            for (int j = mList.size() - 1; j >= 0; j++) {
                                BitmapUtils.recycleBitmap((Bitmap) mList.remove(j));
                            }
                            mList.clear();
                            player.stop();
                            mList = null;
                            return mList;
                        }
                        long playTimeUs = (long) (100000 * i);
                        LogUtil.d(TAG, String.format("getBitmaps setPlayTime : %s", new Object[]{Long.valueOf(playTimeUs)}));
                        timing.setPlayTime(playTimeUs);
                        player.drawFrame(true);
                        GLES20.glViewport(0, 0, width, height);
                        mList.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(width, height, width, height)));
                        EventBus.getDefault().post(new VideoSavingEvent(i + 5, "longVideoBs", progressTag));
                    }
                    long t1 = System.currentTimeMillis();
                    player.stop();
                    Log.d("longvideobitmaps", "time1: " + (System.currentTimeMillis() - t1));
                    Log.d("longvideobitmaps", "time2: " + (t4 - t3));
                    Log.d("longvideobitmaps", "time3: " + (t1 - t4));
                    egl.destroySurface(eglSurface);
                    return mList;
                } catch (Exception e) {
                    Log.e(TAG, "error in Player.getBitmaps", e);
                } finally {
                    egl.destroySurface(eglSurface);
                }
                return mList;
            }
        });
    }

//    private static /* synthetic */ List lambda$getBitmaps$5(int width, int height, Playlist playlist, long startUs, int targetCount, long progressTag, EGL10Helper egl) {
//
//    }

    public static Bitmap getBitmap(@NonNull final HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> filters, final Playlist playlist, final long timeUs, final int width, final int height) {
        //return (Bitmap) EGL10Helper.withContext("Player.getBitmap", Player$$Lambda$8.lambdaFactory$(width, height, playlist, filters, timeUs));
        return EGL10Helper.withContext("Player.getBitmap", new EGLRunnable<Bitmap>() {
            @Override
            public Bitmap run(EGL10Helper egl) {
                EGLSurface eglSurface = egl.createPBuffer(width, height);
                egl.makeCurrent(eglSurface);
                Player player = new Player(new TimingSource.ExplicitTimingSource());
                player.setPlaylist(playlist);
                player.setOutputRect(0, 0, width, height);
                if (filters != null) {
                    for (Map.Entry<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> entry : filters.entrySet()) {
                        HashMap<Integer, GPUImageFilter> publicArray = entry.getKey();
                        HashMap<Integer, GPUImageFilter> publicValue = entry.getValue();
                        for (Map.Entry<Integer, GPUImageFilter> entry1 : publicArray.entrySet()) {
                            player.setFilter(entry1.getKey(), entry1.getValue());
                        }
                        publicArray.clear();
                        for (Map.Entry<Integer, GPUImageFilter> entry12 : publicValue.entrySet()) {
                            player.setFilter(entry12.getKey(), entry12.getValue());
                        }
                    }
                }
                LogUtil.d(TAG, String.format("getBitmap time : %s", new Object[]{Long.valueOf(timeUs)}));
                player.seekTo(timeUs);
                GLES20.glBindFramebuffer(36160, 0);
                player.drawFrame(true);
                GLES20.glViewport(0, 0, width, height);
                GLES20.glBindFramebuffer(36160, 0);
                Bitmap bitmap = BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(width, height, width, height));
                player.stop();
                egl.destroySurface(eglSurface);
                return bitmap;
            }
        });
    }

//    private static /* synthetic */ Bitmap lambda$getBitmap$6(int width, int height, Playlist playlist, @NonNull HashMap filters, long timeUs, EGL10Helper egl) {
//
//    }

    public static Bitmap getBitmap(final Playlist playlist, final long timeUs, final int width, final int height) {
        //return (Bitmap) EGL10Helper.withContext("Player.getBitmap", Player$$Lambda$9.lambdaFactory$(width, height, playlist, timeUs));
        return EGL10Helper.withContext("Player.getBitmap", new EGLRunnable<Bitmap>() {
            @Override
            public Bitmap run(EGL10Helper egl) {
                EGLSurface eglSurface = egl.createPBuffer(width, height);
                egl.makeCurrent(eglSurface);
                Player player = new Player(new TimingSource.ExplicitTimingSource());
                player.setPlaylist(playlist);
                player.setOutputRect(0, 0, width, height);
                LogUtil.d(TAG, String.format("getBitmap time : %s", new Object[]{Long.valueOf(timeUs)}));
                player.seekTo(timeUs);
                GLES20.glBindFramebuffer(36160, 0);
                player.drawFrame(true);
                GLES20.glViewport(0, 0, width, height);
                GLES20.glBindFramebuffer(36160, 0);
                Bitmap bitmap = BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(width, height, width, height));
                player.stop();
                egl.destroySurface(eglSurface);
                return bitmap;
            }
        });
    }

//    private static /* synthetic */ Bitmap lambda$getBitmap$7(int width, int height, Playlist playlist, long timeUs, EGL10Helper egl) {
//
//    }

    private void flush() {
        flushAll();
        if (this.mAudioMixer != null) {
            this.mAudioMixer.flush(this.mPlayQueue.mPlaylist.getDurationUs());
        }
    }

    private void flushAll() {
        for (int i = 0; i < this.mActive.size(); i++) {
            this.mActive.get(i).waitUntilBuffered();
        }
    }

    private void deactivateAll() {
        long t1 = System.nanoTime();
        for (int i = 0; i < this.mActive.size(); i++) {
            this.mActive.get(i).deactivate();
        }
        this.mActive.clear();
        long t2 = System.nanoTime();
        Log.d(TAG, String.format("deactivateAll took %dms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
    }

    private void activatorThread() {
        while (!this.mStopActivator) {
            try {
                this.mActivatorMessages.take().run();
            } catch (Exception e) {
                App.fatalException(e);
                return;
            }
        }
    }

    private void activateInstanceAsync(final Playlist.Instance instance) {
        this.mActivatorMessages.add(new Runnable() {
            public void run() {
                instance.activate();
            }
        });
    }

    private void deactivateInstanceAsync(final Playlist.Instance instance) {
        this.mActivatorMessages.add(new Runnable() {
            public void run() {
                instance.waitUntilBuffered();
                instance.deactivate();
            }
        });
    }

    public long getGlobalTimeUs() {
        return this.mTimingSource.samplesToTime(remapSample(this.mTimingSource.getPlayPosition() - this.mBaseSample));
    }

    public long getCurrentPlayTimeUs() {
        long playTime = this.mTimingSource.samplesToTime(remapSample((this.mTimingSource.getPlayPosition() - this.mBaseSample) + this.mSeekSample));
        if (this.mPlayQueue.mPlaylist == null) {
            return playTime;
        }
        if (this.mPlayQueue.mIsLooped) {
            return playTime % this.mPlayQueue.mPlaylist.getDurationUs();
        }
        return Math.min(playTime, this.mPlayQueue.mPlaylist.getDurationUs());
    }

    public long getPlaylistTimeUs() {
        return getCurrentPlayTimeUs();
    }

    private static boolean canReuse(Playlist a, Playlist b) {
        if (a == null || b == null || a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            Playlist.Entry x = a.get(i);
            Playlist.Entry y = b.get(i);
            if (x.mMedia != y.mMedia || !x.mSpeed.equals(y.mSpeed) || x.mTimelineStartTimeUs != y.mTimelineStartTimeUs || x.mMediaStartTimeUs != y.mMediaStartTimeUs || x.mScaledDurationUs != y.mScaledDurationUs) {
                return false;
            }
        }
        return true;
    }

    public void setPlaylist(Playlist playlist) {
        setPlaylist(playlist, true);
    }

    public void setPlaylist(Playlist playlist, boolean isLooped, long seekTimeUs) {
        Playlist old = this.mPlayQueue.mPlaylist;
        if (canReuse(old, playlist)) {
            for (int i = 0; i < old.size(); i++) {
                old.get(i).copyValues(playlist.get(i));
            }
            seekTo(seekTimeUs);
        } else {
            this.mPlayQueue.setPlaylist(playlist, isLooped);
            seekTo(seekTimeUs);
        }
        Log.d(TAG, "------ Revised Playlist ------");
        this.mPlayQueue.mPlaylist.dump();
    }

    public void setPlaylist(Playlist playlist, boolean isLooped) {
        Playlist old = this.mPlayQueue.mPlaylist;
        if (canReuse(old, playlist)) {
            for (int i = 0; i < old.size(); i++) {
                old.get(i).copyValues(playlist.get(i));
            }
        } else {
            long time = getPlaylistTimeUs();
            this.mPlayQueue.setPlaylist(playlist, isLooped);
            seekTo(time);
        }
        Log.d(TAG, "------ Revised Playlist ------");
        this.mPlayQueue.mPlaylist.dump();
    }

    long remapSample(long in) {
        return (((long) this.mSpeed.getNum()) * in) / ((long) this.mSpeed.getDen());
    }

    public synchronized void setEntrySpeed(Playlist.Entry entry, Ratio speed) {
        if (!this.mIsStopped) {
            reset();
            entry.setSpeed(speed);
            this.mPlayQueue.reset();
        }
    }

    public synchronized void seekFaster(long timeUs) {
        if (!this.mIsStopped) {
            long duration = this.mPlayQueue.mPlaylist.getDurationUs();
            timeUs = Math.min(Math.max(timeUs, 0), duration - 33333);
            ArrayList<Playlist.Entry> active = this.mPlayQueue.mPlaylist.getActiveAt(timeUs);
            if (active.size() == 0) {
                seekTo(timeUs);
            } else {
                Iterator it = active.iterator();
                while (it.hasNext()) {
                    Playlist.Entry e = (Playlist.Entry) it.next();
                    if (e.mMedia instanceof Playlist.FileMedia) {
                        long kfTimeUs = ((Playlist.FileMedia)e.mMedia).getKeyFrameAt(timeUs);
                        long delta = Math.abs(timeUs - kfTimeUs);
                        Log.d(TAG, String.format("got keyframe at %d vs %d (delta:%d)", new Object[]{Long.valueOf(kfTimeUs), Long.valueOf(timeUs), Long.valueOf(delta)}));
                        if (delta < 200000) {
                            seekTo(kfTimeUs);
                            break;
                        }
                    }
                }
                seekTo(timeUs);
            }
        }
    }

    public synchronized void seekTo(long timeUs) {
        if (!this.mIsStopped) {
            timeUs = Math.min(Math.max(timeUs, 0), this.mPlayQueue.mPlaylist.getDurationUs() - 33333);
            Log.d(TAG, String.format("seekTo %dus", new Object[]{Long.valueOf(timeUs)}));
            this.mNextSeekCount++;
            this.mNextSeekTime = timeUs;
        }
    }

    public void handleSeek() {
        if (this.mSeekCount != this.mNextSeekCount) {
            this.mSeekCount = this.mNextSeekCount;
            long timeUs = this.mNextSeekTime;
            reset();
            this.mBaseSample = this.mTimingSource.getPlayPosition();
            this.mSeekSample = this.mTimingSource.timeToSamples(timeUs);
            this.mPlayQueue.reset();
        }
    }

    private void reset() {
        Log.d(TAG, "------ starting reset -------");
        PerfTimer t = new PerfTimer();
        if (this.mAudioMixer != null) {
            this.mAudioMixer.disableMixing();
            this.mAudioMixer.disableInputs();
        }
        deactivateAll();
        if (this.mAudioMixer != null) {
            this.mAudioMixer.clear();
            this.mAudioMixer.enableInputs();
        }
        this.mPrevFrameNum = -1;
        t.printElapsed(TAG, "reset");
    }

    private PlayQueue.Entry getPlayQueueHead() {
        PlayQueue.Entry head = this.mPlayQueue.head();
        if (head != null) {
            head.mStartSample -= this.mSeekSample;
            head.mEndSample -= this.mSeekSample;
        }
        return head;
    }

    private void activate(PlayQueue.Entry head) {
        Playlist.Entry entry = head.mPlayListEntry;
        long startSample = head.mStartSample;
        long endSample = head.mEndSample;
        long mediaTimeUs = head.mMediaTimeUs;
        Log.d(TAG, String.format("queue activate %s: pts:%d->%d(samples) mts:%d(us)", new Object[]{entry.getLabel(), Long.valueOf(startSample), Long.valueOf(endSample), Long.valueOf(mediaTimeUs)}));
        if (startSample < this.mHeadPlayPos) {
            Log.w(TAG, String.format("Trimming instance start to playPos: start:%d playPos:%d", new Object[]{Long.valueOf(startSample), Long.valueOf(this.mHeadPlayPos)}));
            long delta = this.mHeadPlayPos - startSample;
            startSample += delta;
            mediaTimeUs += this.mTimingSource.samplesToTime(head.mPlayListEntry.mSpeed.multiply(delta));
        }
        long firstPresentationTime = startSample + this.mBaseSample;
        long lastPresentationTime = endSample + this.mBaseSample;
        if (entry.mMedia != null) {
            Playlist.Instance instance = entry.mMedia.createInstance(entry, this.mTimingSource, this.mAudioMixer, mediaTimeUs, firstPresentationTime, lastPresentationTime);
            activateInstanceAsync(instance);
            this.mActive.add(instance);
        }
    }

    public boolean drawFrame() {
        return drawFrame(false);
    }

    private boolean drawFrame(boolean offline) {
        if (this.mIsStopped) {
            return false;
        }
        int i;
        Playlist.Instance instance;
        handleSeek();
        long preactivationDurationUs = (offline || this.mIsPaused) ? 0 : SaveVideoTool.MIN_DURATION_LIMIT;
        long preactivationDurationSamples = this.mTimingSource.timeToSamples(preactivationDurationUs);
        long playPos = remapSample(this.mTimingSource.getPlayPosition() - this.mBaseSample);
        long playTime = this.mTimingSource.samplesToTime(playPos);
        long mixPos = this.mTimingSource.getMixPosition();
        long durationUs = this.mPlayQueue.mPlaylist.getDurationUs();
        ArrayList<Playlist.Instance> nextActive = new ArrayList();
        for (i = 0; i < this.mActive.size(); i++) {
            instance = (Playlist.Instance) this.mActive.get(i);
            boolean needsDeactivation = playPos >= instance.mLastPresentationSample;
            if (!(this.mPlayQueue.mPlaylist == null || this.mPlayQueue.mIsLooped || instance.mEntry.getEndTimeUs() <= durationUs - 100000)) {
                needsDeactivation = false;
            }
            if (needsDeactivation) {
                deactivateInstanceAsync(instance);
            } else {
                nextActive.add(instance);
            }
        }
        this.mActive = nextActive;
        this.mHeadPos = mixPos + preactivationDurationSamples;
        this.mHeadPlayPos = playPos;
//        while (true) {
//            PlayQueue.Entry head = getPlayQueueHead();
//            if (head != null && head.mStartSample <= this.mHeadPos) {
//                if (head.mEndSample > this.mHeadPlayPos) {
//                    activate(head);
//                }
//                this.mPlayQueue.advance();
//            }
//        }
        while (mPlayQueue.mIndex>=mPlayQueue.size()){
            PlayQueue.Entry head = getPlayQueueHead();
            if (head != null && head.mStartSample <= this.mHeadPos) {
                if (head.mEndSample > this.mHeadPlayPos) {
                    activate(head);
                }
                this.mPlayQueue.advance();
            }
        }
        for(int j = 0; j < this.mActive.size(); j++) {
            instance = this.mActive.get(j);
            if (instance.getSample(playPos) >= 0) {
                instance.waitUntilActivated();
            }
        }
        if (this.mAudioMixer != null) {
            this.mAudioMixer.enableMixing();
        }
        Collections.sort(this.mActive, this.mActiveSorter);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glDisable(3089);
        GLES20.glClearColor(this.mBackgroundColor[0], this.mBackgroundColor[1], this.mBackgroundColor[2], this.mBackgroundColor[3]);
        GLES20.glClear(16384);
        long videoLatency = offline ? 0 : 100000;
        int layerIndex = 0;
        for (i = 0; i < this.mActive.size(); i++) {
            instance = (Playlist.Instance) this.mActive.get(i);
            long refTimeUs = instance.getTime(playPos);
            long medfiaTimeUs = refTimeUs + instance.mMediaTimeUs;
            if (instance.mEntry.mFrameDurationUs == 0) {
            }
            instance.update(refTimeUs, playTime);
            if (instance.isVideoEnabled() && refTimeUs >= 0) {
                OutputSurfaceArray.Frame frame = instance.getFrameAtTime(medfiaTimeUs - videoLatency);
                if (frame != null) {
                    if (!ResourceTracker.isTextureAlive(frame.mBuffer.mTextureId)) {
                        Log.e(TAG, String.format("ERROR: texture:%d is dead", new Object[]{Integer.valueOf(frame.mBuffer.mTextureId)}));
                    }
                    drawFrame(instance, layerIndex, new SourceTexture(frame.mBuffer));
                    layerIndex++;
                } else {
                    Log.d(TAG, String.format("draw: name:%s reftime:%d mediatime:%d no frame", new Object[]{instance.getLabel(), Long.valueOf(refTimeUs), Long.valueOf(medfiaTimeUs)}));
                }
            }
        }
        GLES20.glBindFramebuffer(36160, 0);
        return true;
    }

    public void drawFrame(Playlist.Instance instance, int layerIndex, SourceTexture inputBuffer) {
        SourceTexture croppedBuffer = drawCrop(instance, inputBuffer);
        if (instance.mEntry.mDrawable != null) {
            instance.mEntry.mDrawable.draw(instance, croppedBuffer);
        } else {
            drawEffects(instance, croppedBuffer);
        }
        if (croppedBuffer != inputBuffer) {
            FramebufferCache.shared.offerFramebuffer(croppedBuffer.fb);
        }
    }

    public static float getRotationAngle(Playlist.Rotation r) {
        switch (r) {
            case ROTATE_90:
                return 90.0f;
            case ROTATE_180:
                return 180.0f;
            case ROTATE_270:
                return 270.0f;
            default:
                return 0.0f;
        }
    }

    private SourceTexture drawCrop(Playlist.Instance instance, SourceTexture buffer) {
        Playlist.Entry entry = instance.getEntry();
        if (((double) entry.mCropX) == 0.0d && ((double) entry.mCropY) == 0.0d && ((double) entry.mCropW) == 1.0d && ((double) entry.mCropH) == 1.0d && entry.mInputRotation == Playlist.Rotation.ROTATE_0 && !entry.mInputFlipH && !entry.mInputFlipV && instance.mZoom == 1.0f) {
            return buffer;
        }
        float f;
        int inputW = (int) Math.ceil((double) (entry.mCropW * ((float) buffer.width)));
        int inputH = (int) Math.ceil((double) (entry.mCropH * ((float) buffer.height)));
        switch (entry.mInputRotation) {
            case ROTATE_90:
            case ROTATE_270:
                int tmp = inputW;
                inputW = inputH;
                inputH = tmp;
                break;
        }
        Framebuffer cropped = FramebufferCache.shared.takeFramebuffer(inputW, inputH, 6408);
        Matrix.setIdentityM(this.mInputTextureMatrix, 0);
        float[] fArr = this.mInputTextureMatrix;
        float f2 = entry.mInputFlipH ? -1.0f : 1.0f;
        if (entry.mInputFlipV) {
            f = -1.0f;
        } else {
            f = 1.0f;
        }
        Matrix.scaleM(fArr, 0, f2, f, 0.0f);
        Matrix.rotateM(this.mInputTextureMatrix, 0, getRotationAngle(entry.mInputRotation), 0.0f, 0.0f, 1.0f);
        float cx = entry.mCropX + (entry.mCropW * 0.5f);
        float cy = entry.mCropY + (entry.mCropH * 0.5f);
        float zoom = instance.mZoom;
        Matrix.translateM(this.mInputTextureMatrix, 0, -cx, -cy, 0.0f);
        Matrix.scaleM(this.mInputTextureMatrix, 0, entry.mCropW, entry.mCropH, 0.0f);
        Matrix.scaleM(this.mInputTextureMatrix, 0, 1.0f / zoom, 1.0f / zoom, 0.0f);
        Matrix.translateM(this.mInputTextureMatrix, 0, -0.5f, -0.5f, 0.0f);
        FrameRenderer.initFilter(this.mInputFilter, inputW, inputH);
        cropped.bind();
        this.mInputFilter.setMatrix(this.mInputTextureMatrix);
        this.mInputFilter.onDraw(buffer.texid, this.mInputCubeBuffer, this.mInputTextureBuffer);
        return new SourceTexture(cropped);
    }

    public void drawEffects(Playlist.Instance instance, SourceTexture buffer) {
        Playlist.Entry entry = instance.getEntry();
        int outX = (int) (entry.mOutViewX * ((float) this.mOutWidth));
        int outY = (int) (entry.mOutViewY * ((float) this.mOutHeight));
        int outW = (int) (entry.mOutViewW * ((float) this.mOutWidth));
        int outH = (int) (entry.mOutViewH * ((float) this.mOutHeight));
        int cropX = (int) (entry.mOutCropX * ((float) this.mOutWidth));
        int cropY = (int) (entry.mOutCropY * ((float) this.mOutHeight));
        int cropW = (int) (entry.mOutCropW * ((float) this.mOutWidth));
        int cropH = (int) (entry.mOutCropH * ((float) this.mOutHeight));
        float outAngle = instance.mOutputRotation;
        int angle0 = ((int) Math.floor((double) (outAngle / 90.0f))) * 90;
        int angle1 = angle0 + 90;
        float alpha = (outAngle - ((float) angle0)) / ((float) (angle1 - angle0));
        float scale0 = entry.getScaleForOutputRotation(entry.mAspectMode, outW, outH, angle0, buffer.width, buffer.height);
        float scale = scale0 + ((entry.getScaleForOutputRotation(entry.mAspectMode, outW, outH, angle1, buffer.width, buffer.height) - scale0) * alpha);
        float inRatio = ((float) buffer.width) / ((float) buffer.height);
        float outRatio = ((float) outH) / ((float) outW);
        float[] fArr = new float[16];
        fArr = new float[]{-1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f};
        float[] rotatedCoords = new float[16];
        float[] cubeM = new float[16];
        Matrix.setIdentityM(cubeM, 0);
        if (entry.mAspectMode != AspectMode.RENDER_STRETCH) {
            Matrix.scaleM(cubeM, 0, outRatio, 1.0f, 1.0f);
            Matrix.rotateM(cubeM, 0, instance.mOutputRotation, 0.0f, 0.0f, 1.0f);
            Matrix.scaleM(cubeM, 0, scale, scale, 1.0f);
            Matrix.scaleM(cubeM, 0, inRatio, 1.0f, 1.0f);
        }
        float[] outCoords = new float[8];
        int j = 0;
        for (int i = 0; i < 16; i += 4) {
            Matrix.multiplyMV(rotatedCoords, i, cubeM, 0, fArr, i);
            outCoords[j + 0] = rotatedCoords[i + 0];
            outCoords[j + 1] = rotatedCoords[i + 1];
            j += 2;
        }
        this.mOutputCubeBuffer.clear();
        this.mOutputCubeBuffer.put(outCoords).position(0);
        float[] texCoords = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mOutputTextureBuffer.clear();
        this.mOutputTextureBuffer.put(texCoords).position(0);
        GPUImageFilter filter = (GPUImageFilter) this.mFilters.get(Integer.valueOf(instance.mEntry.mFilterIndex));
        if (filter == null) {
            filter = (GPUImageFilter) this.mFilters.get(Integer.valueOf(-1));
        }
        boolean isSimpleFilter = false;
        if (filter.getClass().equals(GPUImageFilter.class)) {
            isSimpleFilter = true;
        }
        if (filter.getClass().equals(GPUImageLookupFilter2.class)) {
            isSimpleFilter = true;
        }
        boolean hasCrop = (entry.mOutCropX == 0.0f && entry.mOutCropY == 0.0f && entry.mOutCropW == 1.0f && entry.mOutCropH == 1.0f) ? false : true;
        boolean directFilter = true;
        if (!isSimpleFilter) {
            directFilter = false;
        }
        if (instance.mEntry.mAlpha != 1.0f || instance.hasAlphaChannel()) {
            directFilter = false;
        }
        if (hasCrop) {
            directFilter = false;
        }
        if (directFilter) {
            FrameRenderer.initFilter(filter, this.mOutWidth, this.mOutHeight);
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glViewport(outX, outY, outW, outH);
            GLES20.glDisable(3042);
            if (hasCrop) {
                GLES20.glEnable(3089);
                GLES20.glScissor(cropX, cropY, cropW, cropH);
            }
            filter.onDraw(buffer.texid, this.mOutputCubeBuffer, this.mOutputTextureBuffer);
        } else {
            FrameRenderer.initFilter(filter, buffer.width, buffer.height);
            FrameRenderer.initFilter(this.mCopyFilter, this.mOutWidth, this.mOutHeight);
            GLES20.glDisable(3089);
            GLES20.glDisable(3042);
            GLES20.glBlendFunc(1, 0);
            Framebuffer effectsBuffer = FramebufferCache.shared.takeFramebuffer(buffer.width, buffer.height, 6408);
            effectsBuffer.bind();
            if (GLES20.glCheckFramebufferStatus(36160) != 36053) {
                Log.d(TAG, "incomplete framebuffer");
            }
            if (filter != null) {
                filter.onDraw(buffer.texid, this.mFilterCubeBuffer, this.mFilterTextureBuffer);
            }
            if (null != null) {
                BitmapUtils.saveTexture("foo-fx.png", effectsBuffer.texid(), effectsBuffer.width, effectsBuffer.height);
                BitmapUtils.saveTexture("foo-in.png", buffer.texid, buffer.width, buffer.height);
            }
            GLES20.glBindFramebuffer(36160, 0);
            if (null != null) {
                BitmapUtils.savePixels("frame-in.png", this.mOutWidth, this.mOutHeight);
            }
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
            GLES20.glBlendEquation(32774);
            GLES20.glEnable(3089);
            GLES20.glViewport(outX, outY, outW, outH);
            GLES20.glScissor(cropX, cropY, cropW, cropH);
            this.mCopyFilter.setAlpha(entry.mAlpha);
            this.mCopyFilter.onDraw(effectsBuffer.texid(), this.mOutputCubeBuffer, this.mOutputTextureBuffer);
            FramebufferCache.shared.offerFramebuffer(effectsBuffer);
            if (null != null) {
                BitmapUtils.savePixels("frame.png", this.mOutWidth, this.mOutHeight);
            }
        }
        GLES20.glDisable(3042);
        GLES20.glDisable(3089);
    }

    static float[] computeZoom(float centerX, float centerY, float zoom) {
        float delta;
        zoom = Math.max(1.0f, zoom);
        float minx = centerX - (0.5f / zoom);
        float maxx = centerX + (0.5f / zoom);
        float miny = centerY - (0.5f / zoom);
        float maxy = centerY + (0.5f / zoom);
        if (((double) minx) < 0.0d) {
            delta = -minx;
            minx += -delta;
            maxx += -delta;
        }
        if (((double) maxx) > 1.0d) {
            delta = 1.0f - maxx;
            minx += -delta;
            maxx += -delta;
        }
        if (((double) miny) < 0.0d) {
            delta = -miny;
            miny += -delta;
            maxy += -delta;
        }
        if (((double) maxy) > 1.0d) {
            delta = 1.0f - maxy;
            miny += -delta;
            maxy += -delta;
        }
        return new float[]{minx, miny, maxx, maxy};
    }

    static void WriteBitmap(Bitmap b) {
        FileNotFoundException e;
        IOException e2;
        try {
            FileOutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/debug.png");
            FileOutputStream fileOutputStream;
            try {
                b.compress(CompressFormat.PNG, 100, os);
                os.close();
                fileOutputStream = os;
            } catch (FileNotFoundException e3) {
                e = e3;
                fileOutputStream = os;
                App.warnException(e);
            } catch (IOException e4) {
                e2 = e4;
                fileOutputStream = os;
                App.warnException(e2);
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            App.warnException(e);
        } catch (IOException e6) {
            e2 = e6;
            App.warnException(e2);
        }
    }
}