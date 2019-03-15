package cn.nineton.onetake.widget;

import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.WeakHashMap;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.Ratio;
import cn.nineton.onetake.media.audiotool.AudioDecoder;
import cn.nineton.onetake.media.audiotool.AudioMixer;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnable;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;
import cn.nineton.onetake.media.gpuimage.ResourceTracker;

public class Playlist {
    static final String TAG = "Playlist";
    private ArrayList<Entry> mEntries = new ArrayList();
    private ArrayList<Entry> mOriginalEntries = new ArrayList();
    Comparator<Entry> mTimeSorter = new Comparator<Entry>() {
        public int compare(Entry a, Entry b) {
            return (int) (a.mTimelineStartTimeUs - b.mTimelineStartTimeUs);
        }
    };

    public interface Drawable {
        void draw(Instance instance, Player.SourceTexture sourceTexture);
    }

    public interface Animatable {
        void animate(Instance instance);
    }

    static final class BitmapCache {
        static final String TAG = "BitmapCache";
        static BitmapCache shared = new BitmapCache();
        ArrayList<Entry> mEntries = new ArrayList();

        static final class Entry {
            Bitmap bitmap;
            int refCount = 1;
            int textureId;

            Entry(Bitmap bitmap, int textureId) {
                this.bitmap = bitmap;
                this.textureId = textureId;
            }
        }

        BitmapCache() {
        }

        synchronized void dump() {
            int i = 0;
            Iterator it = this.mEntries.iterator();
            while (it.hasNext()) {
                Entry e = (Entry) it.next();
                BitmapUtils.saveBitmap(String.format("bm%d.png", new Object[]{Integer.valueOf(i)}), e.bitmap);
                BitmapUtils.saveTexture(String.format("tex%d.png", new Object[]{Integer.valueOf(i)}), e.textureId, e.bitmap.getWidth(), e.bitmap.getHeight());
                i++;
            }
        }

        synchronized int get(final Bitmap bitmap) {
            int i;
            if (bitmap.isRecycled()) {
                throw new RuntimeException("bitmap has been recycled");
            }
            Iterator it = this.mEntries.iterator();
            while (it.hasNext()) {
                Entry e = (Entry) it.next();
                if (e.bitmap == bitmap) {
                    e.refCount++;
                    Log.d(TAG, "reftexture:" + e.textureId);
                    dumpState("ref");
                    i = e.textureId;
                    break;
                }
            }
            int size = bitmap.getWidth() * bitmap.getHeight();
            int[] pixels = new int[size];
            final IntBuffer buffer = IntBuffer.wrap(pixels);
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            BitmapUtils.swizzle(pixels, 0, size);
//            i = ((Integer) EGL10Helper.withContext("activate bitmap", Playlist$BitmapCache$$Lambda$1.lambdaFactory$(this, buffer, bitmap))).intValue();
            i = ((Integer) EGL10Helper.withContext("activate bitmap", new EGLRunnable() {
                @Override
                public Object run(EGL10Helper eGL10Helper) {
                    return lambda$get$0(buffer, bitmap, eGL10Helper);
                }
            })).intValue();
            return i;
        }

        private /* synthetic */ Integer lambda$get$0(IntBuffer buffer, Bitmap bitmap, EGL10Helper egl) {
            int textureId = OpenGlUtils.loadTexture(buffer, bitmap.getWidth(), bitmap.getHeight(), -1);
            this.mEntries.add(new Entry(bitmap, textureId));
            Log.d(TAG, "added texture:" + textureId);
            dumpState("added");
            return Integer.valueOf(textureId);
        }

        synchronized void free(int textureId) {
            Log.d(TAG, "deleting texture:" + textureId);
            Iterator it = this.mEntries.iterator();
            while (it.hasNext()) {
                final Entry e = (Entry) it.next();
                if (e.textureId == textureId) {
                    e.refCount--;
                    if (e.refCount == 0) {
//                        EGL10Helper.withContext("deactivate bitmap", Playlist$BitmapCache$$Lambda$2.lambdaFactory$(e));
                        EGL10Helper.withContext("deactivate bitmap", new EGLRunnableVoid() {
                            @Override
                            public void run(EGL10Helper eGL10Helper) {
                                ResourceTracker.freeTexture(e.textureId);
                            }
                        });
                        this.mEntries.remove(e);
                        dumpState("delete");
                    }
                }
            }
            throw new RuntimeException("texture not in cache");
        }

        void dumpState(String reason) {
            Log.d(TAG, String.format("-------- bitmap cache dump (%s) --------", new Object[]{reason}));
            int index = 0;
            Iterator it = this.mEntries.iterator();
            while (it.hasNext()) {
                Entry e = (Entry) it.next();
                Log.d(TAG, String.format("%d: bitmap:%s refs:%d texid:%d", new Object[]{Integer.valueOf(index), e.bitmap, Integer.valueOf(e.refCount), Integer.valueOf(e.textureId)}));
                index++;
            }
        }
    }

    static abstract class Instance {
        float mAlpha = 1.0f;
        Entry mEntry;
        long mFirstPresentationSample;
        long mGlobalTimeUs;
        long mInstanceTimeUs;
        boolean mIsActivated;
        boolean mIsDeactivated;
        long mLastPresentationSample;
        long mMediaTimeUs;
        float mOutputRotation = 0.0f;
        float mOutputScale = 1.0f;
        TimingSource mTimingSource;
        float mZoom = 1.0f;

        abstract OutputSurfaceArray.Frame getFrameAtTime(long j);

        abstract void waitUntilBuffered();

        Instance(Entry entry, TimingSource timingSource, long mediaTimeUs, long firstPresentationSample, long lastPresentationSample) {
            this.mEntry = entry;
            this.mTimingSource = timingSource;
            this.mMediaTimeUs = mediaTimeUs;
            this.mFirstPresentationSample = firstPresentationSample;
            this.mLastPresentationSample = lastPresentationSample;
            this.mAlpha = entry.mAlpha;
        }

        Entry getEntry() {
            return this.mEntry;
        }

        synchronized void activate() {
            this.mIsActivated = true;
            notifyAll();
        }

        synchronized void deactivate() {
            this.mIsDeactivated = true;
            notifyAll();
        }

        void waitUntilActivated() {
            if (!this.mIsActivated && !this.mIsDeactivated) {
                try {
                    long t1 = System.nanoTime();
                    synchronized (this) {
                        while (!this.mIsActivated && !this.mIsDeactivated) {
                            wait();
                        }
                    }
                    long delta = (System.nanoTime() - t1) / 1000000;
                    Log.d(Playlist.TAG, String.format("waited %dms for activation", new Object[]{Long.valueOf(delta)}));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        long getSample(long outputPlayPosition) {
            return outputPlayPosition - this.mFirstPresentationSample;
        }

        long getTime(long outputPlayPosition) {
            return this.mTimingSource.samplesToTime(getSample(outputPlayPosition));
        }

        String getLabel() {
            return this.mEntry.getLabel();
        }

        boolean isVideoEnabled() {
            return this.mEntry.mVideoEnabled;
        }

        boolean hasAlphaChannel() {
            return false;
        }

        void update(long timeUs, long globalTimeUs) {
            this.mInstanceTimeUs = timeUs;
            this.mGlobalTimeUs = globalTimeUs;
            if (this.mEntry.mInterpolatedZoom != null) {
                this.mZoom = this.mEntry.mInterpolatedZoom.getValue(timeUs);
            }
            if (this.mEntry.mInterpolatedAlpha != null) {
                this.mAlpha = this.mEntry.mInterpolatedAlpha.getValue(timeUs);
            } else {
                this.mAlpha = this.mEntry.mAlpha;
            }
            this.mOutputRotation = this.mEntry.mOutputRotation;
            this.mOutputScale = this.mEntry.mOutputScale;
            if (this.mEntry.mAnimatable != null) {
                this.mEntry.mAnimatable.animate(this);
            }
        }
    }

    static class BitmapInstance extends Instance {
        private OutputSurfaceArray.Buffer mBuffer;
        private OutputSurfaceArray.Frame mFrame;

        BitmapInstance(Entry entry, TimingSource timingSource, long mediaTimeUs, long firstPresentationSample, long lastPresentationSample) {
            super(entry, timingSource, mediaTimeUs, firstPresentationSample, lastPresentationSample);
        }

        synchronized void activate() {
            if (!(this.mIsActivated || this.mIsDeactivated)) {
                BitmapMedia media = (BitmapMedia) this.mEntry.mMedia;
                this.mBuffer = new OutputSurfaceArray.Buffer(media.getWidth(), media.getHeight(), BitmapCache.shared.get(media.mBitmap));
                this.mFrame = new OutputSurfaceArray.Frame(this.mBuffer, 0, 0);
                super.activate();
            }
        }

        synchronized void deactivate() {
            BitmapCache.shared.free(this.mBuffer.mTextureId);
            this.mBuffer = null;
            this.mFrame = null;
            super.deactivate();
        }

        void waitUntilBuffered() {
        }

        boolean hasAlphaChannel() {
            return true;
        }

        synchronized OutputSurfaceArray.Frame getFrameAtTime(long timeUs) {
            OutputSurfaceArray.Frame frame;
            if (this.mIsDeactivated) {
                frame = null;
            } else {
                frame = this.mFrame;
            }
            return frame;
        }

        void update(long timeUs, long globalTimeUs) {
            super.update(timeUs, globalTimeUs);
        }
    }

    public interface Media {
        Instance createInstance(Entry entry, TimingSource timingSource, AudioMixer audioMixer, long j, long j2, long j3);

        boolean equals(Media media);

        long getDurationUs();

        String getLabel();

        boolean hasAudioTrack();

        boolean hasVideoTrack();
    }

    static final class BitmapMedia implements Media {
        private Bitmap mBitmap;
        int mHeight;
        int mWidth;

        BitmapMedia(Bitmap bitmap) {
            this.mBitmap = bitmap;
            this.mWidth = bitmap.getWidth();
            this.mHeight = bitmap.getHeight();
        }

        public boolean equals(Media _m) {
            BitmapMedia m = (BitmapMedia) _m;
            if (m != null && this.mBitmap == m.mBitmap) {
                return true;
            }
            return false;
        }

        int getWidth() {
            return this.mWidth;
        }

        int getHeight() {
            return this.mHeight;
        }

        public long getDurationUs() {
            return 0;
        }

        public String getLabel() {
            return "bitmap";
        }

        public boolean hasVideoTrack() {
            return false;
        }

        public boolean hasAudioTrack() {
            return false;
        }

        public Instance createInstance(Entry entry, TimingSource timingSource, AudioMixer mixer, long mediaTimeUs, long firstPresentationSample, long lastPresentationSample) {
            return new BitmapInstance(entry, timingSource, mediaTimeUs, firstPresentationSample, lastPresentationSample);
        }
    }

    public static class Entry {
        float mAlpha = 1.0f;
        Animatable mAnimatable = null;
        Player.AspectMode mAspectMode = Player.AspectMode.RENDER_CROP;
        boolean mAudioEnabled = true;
        int mCopyFilterIndex = -1;
        float mCropH = 1.0f;
        float mCropW = 1.0f;
        float mCropX = 0.0f;
        float mCropY = 0.0f;
        Drawable mDrawable = null;
        int mFilterIndex = -1;
        long mFrameDurationUs = 0;
        boolean mInputFlipH = false;
        boolean mInputFlipV = false;
        Rotation mInputRotation = Rotation.ROTATE_0;
        InterpolatedFloat mInterpolatedAlpha = null;
        InterpolatedFloat mInterpolatedCrop = null;
        InterpolatedFloat mInterpolatedVolume = null;
        InterpolatedFloat mInterpolatedZoom = null;
        public Media mMedia;
        long mMediaStartTimeUs;
        int mMediaType = 0;
        float mOutCropH = 1.0f;
        float mOutCropW = 1.0f;
        float mOutCropX = 0.0f;
        float mOutCropY = 0.0f;
        float mOutViewH = 1.0f;
        float mOutViewW = 1.0f;
        float mOutViewX = 0.0f;
        float mOutViewY = 0.0f;
        float mOutputRotation = 0.0f;
        float mOutputScale = 1.0f;
        long mScaledDurationUs;
        Ratio mSpeed = new Ratio(100, 100);
        long mTimelineStartTimeUs;
        boolean mVideoEnabled = true;
        public float mVolume = 1.0f;
        int mZIndex = 0;

        int getAnimationIndex() {
            return this.mCopyFilterIndex;
        }

        void copyIndex() {
            if (this.mCopyFilterIndex == -1) {
                this.mCopyFilterIndex = this.mFilterIndex;
            }
        }

        void resetFilterIndex() {
            if (this.mCopyFilterIndex != -1) {
                this.mFilterIndex = this.mCopyFilterIndex;
                this.mCopyFilterIndex = -1;
            }
        }

        Entry(Media media, long timelineStartUs, long mediaTimeStartUs, long durationUs) {
            this.mMedia = media;
            this.mTimelineStartTimeUs = timelineStartUs;
            this.mMediaStartTimeUs = mediaTimeStartUs;
            this.mScaledDurationUs = durationUs;
        }

        public Entry clone() {
            Entry dup = new Entry(this.mMedia, this.mTimelineStartTimeUs, this.mMediaStartTimeUs, this.mScaledDurationUs);
            dup.copyValues(this);
            return dup;
        }

        public void copyValues(Entry other) {
            this.mVideoEnabled = other.mVideoEnabled;
            this.mAudioEnabled = other.mAudioEnabled;
            this.mVolume = other.mVolume;
            this.mCropX = other.mCropX;
            this.mCropY = other.mCropY;
            this.mCropW = other.mCropW;
            this.mCropH = other.mCropH;
            if (other.mInterpolatedZoom != null) {
                this.mInterpolatedZoom = other.mInterpolatedZoom.clone();
            }
            if (other.mInterpolatedVolume != null) {
                this.mInterpolatedVolume = this.mInterpolatedVolume.clone();
            }
            if (other.mInterpolatedAlpha != null) {
                this.mInterpolatedAlpha = this.mInterpolatedAlpha.clone();
            }
            if (other.mInterpolatedCrop != null) {
                this.mInterpolatedCrop = this.mInterpolatedCrop.clone();
            }
            this.mSpeed = other.mSpeed.clone();
            this.mOutViewX = other.mOutViewX;
            this.mOutViewY = other.mOutViewY;
            this.mOutViewW = other.mOutViewW;
            this.mOutViewH = other.mOutViewH;
            this.mOutCropX = other.mOutCropX;
            this.mOutCropY = other.mOutCropY;
            this.mOutCropW = other.mOutCropW;
            this.mOutCropH = other.mOutCropH;
            this.mAspectMode = other.mAspectMode;
            this.mOutputRotation = other.mOutputRotation;
            this.mOutputScale = other.mOutputScale;
            this.mAlpha = 1.0f;
            this.mInputRotation = other.mInputRotation;
            this.mInputFlipH = other.mInputFlipH;
            this.mInputFlipV = other.mInputFlipV;
            this.mMediaType = other.mMediaType;
            this.mZIndex = other.mZIndex;
            this.mFilterIndex = other.mFilterIndex;
            this.mFrameDurationUs = other.mFrameDurationUs;
            this.mAnimatable = other.mAnimatable;
            this.mDrawable = other.mDrawable;
        }

        String getLabel() {
            return this.mMedia != null ? this.mMedia.getLabel() : EnvironmentCompat.MEDIA_UNKNOWN;
        }

        long getEndTimeUs() {
            return this.mTimelineStartTimeUs + getDurationUs();
        }

        long getDurationUs() {
            return this.mScaledDurationUs;
        }

        public Entry disableVideo() {
            this.mVideoEnabled = false;
            return this;
        }

        Entry setAnimatable(Animatable value) {
            this.mAnimatable = value;
            return this;
        }

        Entry setDrawable(Drawable value) {
            this.mDrawable = value;
            return this;
        }

        public Entry setMediaType(int mediaType) {
            this.mMediaType = mediaType;
            return this;
        }

        public Entry setFramerate(long frameDurationUs) {
            this.mFrameDurationUs = frameDurationUs;
            return this;
        }

        public Entry disableAudio() {
            this.mAudioEnabled = false;
            return this;
        }

        public Entry setFilterIndex(int filterIndex) {
            this.mFilterIndex = filterIndex;
            return this;
        }

        public Entry setVolume(float val) {
            this.mVolume = val;
            return this;
        }

        public Entry setVolume(InterpolatedFloat val) {
            this.mInterpolatedVolume = val;
            return this;
        }

        public Entry setAlpha(float val) {
            this.mAlpha = val;
            return this;
        }

        public Entry setAlpha(InterpolatedFloat val) {
            this.mInterpolatedAlpha = val;
            return this;
        }

        public Entry setSpeed(Ratio val) {
            this.mSpeed = val.clone();
            return this;
        }

        public Entry setZoom(InterpolatedFloat value) {
            this.mInterpolatedZoom = value;
            return this;
        }

        public Entry setZIndex(int value) {
            this.mZIndex = value;
            return this;
        }

        float getVolume() {
            return this.mVolume;
        }

        public Entry setInputCrop(float x, float y, float w, float h) {
            this.mCropX = x;
            this.mCropY = y;
            this.mCropW = w;
            this.mCropH = h;
            return this;
        }

        Entry setInputCrop(float[] rect) {
            this.mCropX = rect[0];
            this.mCropY = rect[1];
            this.mCropW = rect[2] - rect[0];
            this.mCropH = rect[3] - rect[1];
            return this;
        }

        public Entry setInputOrientation(Rotation rotation, boolean flipH, boolean flipV) {
            this.mInputRotation = rotation;
            this.mInputFlipH = flipH;
            this.mInputFlipV = flipV;
            return this;
        }

        public Entry setOutputRect(float x, float y, float w, float h) {
            this.mOutViewX = x;
            this.mOutViewY = y;
            this.mOutViewW = w;
            this.mOutViewH = h;
            return this;
        }

        public void setAspectMode(Player.AspectMode mode) {
            this.mAspectMode = mode;
        }

        public Entry setOutputRotation(float angle) {
            this.mOutputRotation = angle;
            return this;
        }

        public Entry setOutputCrop(float x, float y, float w, float h) {
            this.mOutCropX = x;
            this.mOutCropY = y;
            this.mOutCropW = w;
            this.mOutCropH = h;
            return this;
        }

        public float getScaleForOutputRotation(Player.AspectMode aspectMode, int outWidth, int outHeight, int outRotation, int bufferWidth, int bufferHeight) {
            boolean isRotated = outRotation == 90 || outRotation == 270;
            int outW = (int) (this.mOutViewW * ((float) outWidth));
            int outH = (int) (this.mOutViewH * ((float) outHeight));
            float inRatio = ((float) bufferWidth) / ((float) bufferHeight);
            float outRatio = ((float) outH) / ((float) outW);
            float dispW = (((float) outW) * inRatio) * outRatio;
            float dispH = (float) outH;
            if (isRotated) {
                dispH = (((float) outW) * inRatio) * outRatio;
                dispW = (float) outH;
            }
            float hRatio = ((float) outW) / dispW;
            float vRatio = ((float) outH) / dispH;
            if (aspectMode == Player.AspectMode.RENDER_CROP) {
                return Math.min(hRatio, vRatio);
            }
            if (aspectMode == Player.AspectMode.RENDER_FILL) {
                return Math.max(hRatio, vRatio);
            }
            return 1.0f;
        }
    }

    public static final class FileMedia implements Media {
        private static WeakHashMap<String, FileMedia> mMediaMap = new WeakHashMap();
        MediaFormat mAudioFormat;
        long mDurationUs;
        String mFilename;
        String mLabel;
        long mLastModified;
        MediaFormat mVideoFormat;

        private FileMedia(long duration, String filename, MediaFormat videoFormat, MediaFormat audioFormat) {
            Log.d(Playlist.TAG, String.format("created new media: duration:%d file:%s", new Object[]{Long.valueOf(duration), filename}));
            File file = new File(filename);
            this.mDurationUs = duration;
            this.mFilename = filename;
            this.mLabel = file.getName();
            this.mVideoFormat = videoFormat;
            this.mAudioFormat = audioFormat;
            this.mLastModified = file.lastModified();
        }

        public boolean equals(Media _m) {
            FileMedia m = (FileMedia) _m;
            if (m != null && this.mFilename.equals(m.mFilename)) {
                return true;
            }
            return false;
        }

        public long getDurationUs() {
            return this.mDurationUs;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public boolean hasVideoTrack() {
            return this.mVideoFormat != null;
        }

        public boolean hasAudioTrack() {
            return this.mAudioFormat != null;
        }

        public static FileMedia create(LongVideosModel model) {
            return create(new File(model.getPlaylistMediaPath()));
        }

        public static synchronized FileMedia create(File file) {
            FileMedia m;
            synchronized (FileMedia.class) {
                try {
                    String filename = file.getAbsolutePath();
                    if (mMediaMap.containsKey(filename)) {
                        m = (FileMedia) mMediaMap.get(filename);
                        if (m == null || m.mLastModified != file.lastModified()) {
                            mMediaMap.remove(filename);
                        }
                    }
                    MediaExtractor extractor = MediaUtils.createExtractor(filename);
                    long vduration = Long.MAX_VALUE;
                    long aduration = Long.MAX_VALUE;
                    MediaFormat vformat = MediaUtils.getVideoTrack(extractor);
                    if (vformat != null) {
                        vduration = vformat.getLong("durationUs");
                    }
                    MediaFormat aformat = MediaUtils.getAudioTrack(extractor);
                    if (aformat != null) {
                        aduration = aformat.getLong("durationUs");
                    }
                    long duration = Math.min(vduration, aduration);
                    if (duration <= 0) {
                        m = null;
                    } else {
                        m = new FileMedia(duration, filename, vformat, aformat);
                        mMediaMap.put(filename, m);
                        extractor.release();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    m = null;
                }
            }
            return m;
        }

        public long getKeyFrameAt(long timeUs) {
            try {
                MediaExtractor extractor = MediaUtils.createExtractor(this.mFilename);
                MediaUtils.getVideoTrack(extractor);
                extractor.seekTo(timeUs, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                long kfTimeUs = extractor.getSampleTime();
                extractor.release();
                return kfTimeUs;
            } catch (IOException e) {
                return -1;
            }
        }

        public Instance createInstance(Entry entry, TimingSource timing, AudioMixer mixer, long mediaTimeUs, long firstPresentationSample, long lastPresentationSample) {
            return new FileMediaInstance(entry, timing, mixer, mediaTimeUs, firstPresentationSample, lastPresentationSample);
        }
    }

    public static class FileMediaInstance extends Instance {
        public OutputSurfaceArray.Frame currFrame;
        private AudioDecoder mAudioDecoder;
        private AudioMixer mAudioMixer;
        private AudioMixer.Track mAudioMixerTrack;
        private OutputSurfaceArray.BufferPool mBufferPool;
        private VideoDecoder18 mVideoDecoder;
        private long mVideoFrameDurationUs;
        private OutputSurfaceArray.Frame nextFrame;

        FileMediaInstance(Entry entry, TimingSource timingSource, AudioMixer mixer, long mediaTimeUs, long firstPresentationSample, long lastPresentationSample) {
            super(entry, timingSource, mediaTimeUs, firstPresentationSample, lastPresentationSample);
            this.mVideoFrameDurationUs = entry.mFrameDurationUs;
            this.mAudioMixer = mixer;
            if (this.mAudioMixer != null && this.mAudioMixer.isAudioEnabled() && entry.mAudioEnabled) {
                this.mAudioMixerTrack = this.mAudioMixer.createTrack(firstPresentationSample, lastPresentationSample, this.mEntry.mVolume, this.mEntry.mSpeed);
            }
        }

        long getSample(long outputPlayPosition) {
            return this.mEntry.mSpeed.multiply(outputPlayPosition - this.mFirstPresentationSample);
        }

        synchronized void activate() {
            if (!(this.mIsActivated || this.mIsDeactivated)) {
                long durationSamples = this.mEntry.mSpeed.multiply(this.mLastPresentationSample - this.mFirstPresentationSample);
                long durationUs = this.mTimingSource.samplesToTime(durationSamples);
                FileMedia media = (FileMedia) this.mEntry.mMedia;
                Log.d(Playlist.TAG, String.format("activating media:%s", new Object[]{getLabel()}));
                try {
                    if (this.mEntry.mVideoEnabled && media.mVideoFormat != null) {
                        if (this.mVideoDecoder != null) {
                            throw new RuntimeException("VideoDecoder already active");
                        }
                        this.mVideoDecoder = new VideoDecoder18();
                        this.mVideoDecoder.open(media.mFilename);
//                        EGL10Helper.withContext("activate video", Playlist$FileMediaInstance$$Lambda$1.lambdaFactory$(this, this.mVideoDecoder.getOutputSize()));
                        EGL10Helper.withContext("activate video", new EGLRunnableVoid() {
                            @Override
                            public void run(EGL10Helper eGL10Helper) {
                                lambda$activate$0(mVideoDecoder.getOutputSize(),eGL10Helper);
                            }
                        });
                        this.mVideoDecoder.start(this.mBufferPool, this.mVideoFrameDurationUs, this.mMediaTimeUs, durationUs, false);
                    }
                    if (this.mAudioMixerTrack != null) {
                        if (this.mAudioDecoder != null) {
                            throw new RuntimeException("AudioDecoder already active");
                        }
                        try {
                            this.mAudioDecoder = new AudioDecoder();
                            this.mAudioDecoder.open(media.mFilename);
                            this.mAudioDecoder.start(this.mMediaTimeUs, this.mAudioMixer, this.mAudioMixerTrack, durationSamples);
                        } catch (Exception e) {
                            Log.e(Playlist.TAG, "audio enabled but error, disabling");
                            this.mAudioMixer.deleteTrack(this.mAudioMixerTrack);
                            this.mAudioMixerTrack = null;
                            this.mAudioDecoder = null;
                            this.mEntry.mAudioEnabled = false;
                        }
                    }
                    if (this.mEntry.mVideoEnabled) {
                        getFrameAtTime(this.mMediaTimeUs);
                    }
                    super.activate();
                } catch (IOException e2) {
                    Log.e(Playlist.TAG, e2.getMessage());
                }
            }
            return;
        }

        private /* synthetic */ void lambda$activate$0(VideoDecoder18.Size size, EGL10Helper egl) {
            this.mBufferPool = new OutputSurfaceArray.BufferPool(3, size.width, size.height);
        }

        synchronized void deactivate() {
            if (this.mAudioDecoder != null) {
                this.mAudioDecoder.close();
                this.mAudioDecoder = null;
            }
            if (this.mVideoDecoder != null) {
                this.mVideoDecoder.stop();
                if (this.currFrame != null) {
                    freeFrame(this.currFrame);
                }
                if (this.nextFrame != null) {
                    freeFrame(this.nextFrame);
                }
                this.mVideoDecoder.close(true);
            }
            if (this.mBufferPool != null) {
//                EGL10Helper.withContext("deactivate video", Playlist$FileMediaInstance$$Lambda$2.lambdaFactory$(this));
                EGL10Helper.withContext("deactivate video", new EGLRunnableVoid() {
                    @Override
                    public void run(EGL10Helper eGL10Helper) {
                        lambda$deactivate$1(eGL10Helper);
                    }
                });
            }
            this.currFrame = null;
            this.nextFrame = null;
            this.mVideoDecoder = null;
            this.mAudioMixer = null;
            super.deactivate();
        }

        private /* synthetic */ void lambda$deactivate$1(EGL10Helper egl) {
            this.mBufferPool.releaseAll();
            this.mBufferPool = null;
        }

        void waitUntilBuffered() {
            if (!this.mIsDeactivated && this.mAudioDecoder != null) {
                this.mAudioDecoder.waitUntilBuffered();
            }
        }

        synchronized OutputSurfaceArray.Frame getFrameAtTime(long timeUs) {
            OutputSurfaceArray.Frame frame = null;
            synchronized (this) {
                if (!this.mIsDeactivated) {
                    if (this.currFrame == null && this.nextFrame == null) {
                        this.currFrame = this.mVideoDecoder.getFrame();
                        this.nextFrame = this.mVideoDecoder.getFrame();
                    }
                    if (this.currFrame != null) {
                        if (timeUs >= this.currFrame.mTimestampUs) {
                            while (this.currFrame != null) {
                                if (this.nextFrame != null) {
                                    if (timeUs >= this.currFrame.mTimestampUs && (this.nextFrame == null || timeUs < this.nextFrame.mTimestampUs)) {
                                        frame = this.currFrame;
                                        break;
                                    }
                                    freeFrame(this.currFrame);
                                    this.currFrame = this.nextFrame;
                                    this.nextFrame = this.mVideoDecoder.getFrame();
                                } else {
                                    frame = this.currFrame;
                                    break;
                                }
                            }
                        }
                        frame = this.currFrame;
                    } else {
                        Log.w(Playlist.TAG, "no frames from decoder");
                    }
                }
            }
            return frame;
        }

        void freeFrame(OutputSurfaceArray.Frame f) {
            this.mVideoDecoder.freeFrame(f);
        }

        void update(long timeUs, long globalTimeUs) {
            super.update(timeUs, globalTimeUs);
            if (this.mAudioMixerTrack != null) {
                float value = this.mEntry.mVolume;
                if (this.mEntry.mInterpolatedVolume != null) {
                    value = this.mEntry.mInterpolatedVolume.getValue(timeUs);
                    Log.d(Playlist.TAG, String.format("time:%d value:%f", new Object[]{Long.valueOf(timeUs), Float.valueOf(value)}));
                }
                this.mAudioMixerTrack.mVolume = value * value;
                this.mAudioMixer.setTrackSpeed(this.mAudioMixerTrack, this.mEntry.mSpeed);
            }
        }
    }

    public enum Rotation {
        ROTATE_0,
        ROTATE_90,
        ROTATE_180,
        ROTATE_270
    }

    public long getDurationUs() {
        long duration = 0;
        for (int i = 0; i < this.mEntries.size(); i++) {
            duration = Math.max(duration, ((Entry) this.mEntries.get(i)).getEndTimeUs());
        }
        return duration;
    }

    public Entry add(Entry entry) {
        return addEntry(entry);
    }

    public Entry add(FileMedia m, long timelineStartUs) {
        return add(m, timelineStartUs, 0, m.getDurationUs());
    }

    public Entry add(FileMedia m, long timelineStartUs, long mediaTimeStartUs, long duration) {
        return addEntry(new Entry(m, timelineStartUs, mediaTimeStartUs, duration));
    }

    public Entry add(Bitmap bitmap, long timelineStartUs, long mediaTimeStartUs, long duration, Player.AspectMode aspectMode) {
        if (bitmap == null) {
            throw new RuntimeException("null bitmap used");
        }
        Entry e = new Entry(new BitmapMedia(bitmap), timelineStartUs, mediaTimeStartUs, duration);
        e.disableAudio();
        e.setAspectMode(aspectMode);
        return addEntry(e);
    }

    public Entry add(Bitmap bitmap, long timelineStartUs, long mediaTimeStartUs, long duration) {
        if (bitmap == null) {
            throw new RuntimeException("null bitmap used");
        }
        Entry e = new Entry(new BitmapMedia(bitmap), timelineStartUs, mediaTimeStartUs, duration);
        e.disableAudio();
        e.setAspectMode(Player.AspectMode.RENDER_STRETCH);
        return addEntry(e);
    }

    public Entry remove(int position) {
        Entry remove = (Entry) this.mOriginalEntries.remove(position);
        updateEntries();
        return remove;
    }

    private Entry addEntry(Entry entry) {
        this.mOriginalEntries.add(entry);
        updateEntries();
        return entry;
    }

    private void updateEntries() {
        this.mEntries.clear();
        this.mEntries.addAll(this.mOriginalEntries);
        Collections.sort(this.mEntries, this.mTimeSorter);
    }

    public int size() {
        return this.mOriginalEntries.size();
    }

    public Entry get(int index) {
        return (Entry) this.mOriginalEntries.get(index);
    }

    public int indexOf(Entry e) {
        return this.mOriginalEntries.indexOf(e);
    }

    int findSorted(Entry e) {
        for (int i = 0; i < this.mEntries.size(); i++) {
            if (this.mEntries.get(i) == e) {
                return i;
            }
        }
        return -1;
    }

    int sortedSize() {
        return this.mEntries.size();
    }

    Entry getSorted(int index) {
        return (Entry) this.mEntries.get(index);
    }

    public ArrayList<Entry> getEntries() {
        return this.mEntries;
    }

    ArrayList<Entry> getActiveAt(long timeUs) {
        ArrayList<Entry> active = new ArrayList();
        Iterator it = this.mEntries.iterator();
        while (it.hasNext()) {
            Entry e = (Entry) it.next();
            long start = e.mTimelineStartTimeUs;
            long end = start + e.mScaledDurationUs;
            if (timeUs >= start && timeUs < end) {
                active.add(e);
            }
        }
        return active;
    }

    void dump() {
        Iterator it = this.mEntries.iterator();
        while (it.hasNext()) {
            Entry e = (Entry) it.next();
            Log.d(TAG, String.format("media:%20s timeline:[%d -> %d] duration:%d mediaStart:%d", new Object[]{e.getLabel(), Long.valueOf(e.mTimelineStartTimeUs), Long.valueOf(e.mTimelineStartTimeUs + e.mScaledDurationUs), Long.valueOf(e.mScaledDurationUs), Long.valueOf(e.mMediaStartTimeUs)}));
        }
    }
}