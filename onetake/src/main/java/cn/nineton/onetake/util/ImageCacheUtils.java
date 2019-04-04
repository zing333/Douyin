package cn.nineton.onetake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager.GetBitmapCallback;
//import com.blink.academy.onetake.support.thread.PriorityRunnable;
//import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
//import com.blink.academy.onetake.support.utils.BitmapUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videocache.ProxyCacheUtils;
//import com.blink.academy.onetake.ui.activity.video.LongVideoSelectActivity.BitmapViewInfo;
//import com.blink.academy.onetake.widgets.imageview.CacheRunnableImageView;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import cn.nineton.onetake.bean.BitmapViewInfo;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.widget.CacheRunnableImageView;

public class ImageCacheUtils {
    private static final int DEFAULT_MEM_CACHE_SIZE = ((((DensityUtil.dip2px(44.0f) * DensityUtil.dip2px(44.0f)) * 4) * 20) / 1024);
    private static int INSTANCE_COUNT = 0;
    public static final String ONEMIN_BITMAP_PATH = (Config.getFilterActivityTempImgSavePath() + "onemine.jpg");
    private static final String TAG = ImageCacheUtils.class.getSimpleName();
    private static ArrayList<ImageCacheUtils> sImageCacheUtilsArrayList;
    private LinkedBlockingDeque<Runnable> LoadImagesWorks = new LinkedBlockingDeque();
    private LinkedBlockingDeque<Runnable> PreloadImagesWorks = new LinkedBlockingDeque();
    private boolean isReleased = false;
    LruCache<ImageInfo, Bitmap> mBitmapLruCache = new LruCache<ImageInfo, Bitmap>(DEFAULT_MEM_CACHE_SIZE) {
        protected void entryRemoved(boolean evicted, ImageInfo key, Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
        }

        protected int sizeOf(ImageInfo key, Bitmap value) {
            int bitmapSize = BitmapUtils.getBitmapSize(value) / 1024;
            return bitmapSize == 0 ? 1 : bitmapSize;
        }
    };
    private Thread mThread;
    private Handler mUiHandler;
    private boolean paused;
    private ArrayList<Runnable> runningRunnable;

    static class ImageInfo {
        public String path;
        public long time;

        public String getPath() {
            return this.path;
        }

        public long getTime() {
            return this.time;
        }

        public ImageInfo(String path, long time) {
            this.path = path;
            this.time = time;
        }

        public static ImageInfo makeImageInfo(String path, long time) {
            return new ImageInfo(path, time);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ImageInfo imageInfo = (ImageInfo) o;
            if (this.time != imageInfo.time) {
                return false;
            }
            if (this.path != null) {
                return this.path.equals(imageInfo.path);
            }
            if (imageInfo.path != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return ((this.path != null ? this.path.hashCode() : 0) * 31) + ((int) (this.time ^ (this.time >>> 32)));
        }

        public String toString() {
            return "ImageInfo{path='" + this.path + '\'' + ", time=" + this.time + '}';
        }
    }

    public class PreloadImageRunnable implements Runnable {
        GetBitmapCallback callback;
        boolean canceled;
        final ImageInfo imageInfo;
        final long imageTime;
        private boolean isSpecialPre = false;
        final int modelType;

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        public void setSpecialPre(boolean specialPre) {
            this.isSpecialPre = specialPre;
        }

        public PreloadImageRunnable(int modelType, long imageTime, ImageInfo imageInfo, GetBitmapCallback callback) {
            this.modelType = modelType;
            this.imageTime = imageTime;
            this.imageInfo = imageInfo;
            this.callback = callback;
        }

        public void setCallback(GetBitmapCallback callback) {
            this.callback = callback;
        }

        public void run() {
            if (!isCanceled()) {
                Bitmap cachedBitmap = ImageCacheUtils.this.getCachedBitmap(this.imageInfo);
                if (!isCanceled()) {
                    if (cachedBitmap == null || cachedBitmap.isRecycled()) {
                        if (!isCanceled()) {
                            File file = new File(Config.getFilterActivityTempImgSavePath(), ImageCacheUtils.this.getFileName(this.imageInfo));
                            if (!isCanceled()) {
                                if (!file.exists()) {
                                    Bitmap mediaBitmap = null;
                                    if (!isCanceled()) {
                                        if (this.modelType == 0) {
                                            mediaBitmap = BitmapUtils.getMediaBitmap(this.imageInfo.getPath(), this.imageTime);
                                        } else if (this.modelType == 2) {
                                            mediaBitmap = BitmapUtils.getLocalBitmap(this.imageInfo.getPath());
                                        } else if (this.modelType == 4) {
                                            while (mediaBitmap == null) {
                                                try {
                                                    Thread.sleep(50);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                if (!isCanceled()) {
                                                    mediaBitmap = BitmapUtils.getLocalBitmap(this.imageInfo.getPath());
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                        boolean needRecycle = this.callback == null;
                                        ImageCacheUtils.this.saveBitmapToLocal(this.imageInfo, mediaBitmap, needRecycle);
                                        if (!isCanceled() && !needRecycle && this.callback != null) {
                                            ImageCacheUtils.this.saveBitmapToCache(mediaBitmap, this.imageInfo);
                                            this.callback.onGetBitmapFinished(this.imageInfo.getPath(), this.imageTime, mediaBitmap);
                                        }
                                    }
                                } else if (this.callback != null || this.isSpecialPre) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    ImageCacheUtils.this.saveBitmapToCache(bitmap, this.imageInfo);
                                    if (this.callback != null) {
                                        this.callback.onGetBitmapFinished(this.imageInfo.getPath(), this.imageTime, bitmap);
                                    }
                                }
                            }
                        }
                    } else if (this.callback != null) {
                        this.callback.onGetBitmapFinished(this.imageInfo.getPath(), this.imageTime, cachedBitmap);
                    }
                }
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PreloadImageRunnable that = (PreloadImageRunnable) o;
            if (this.imageInfo != null) {
                return this.imageInfo.equals(that.imageInfo);
            }
            if (that.imageInfo != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.imageInfo != null ? this.imageInfo.hashCode() : 0;
        }
    }

    private static void saveInstanceToCache(ImageCacheUtils cacheUtils) {
        if (sImageCacheUtilsArrayList == null) {
            sImageCacheUtilsArrayList = new ArrayList();
        }
        if (cacheUtils != null) {
            sImageCacheUtilsArrayList.add(cacheUtils);
        }
    }

    private static void removeFromCache(ImageCacheUtils cacheUtils) {
        if (sImageCacheUtilsArrayList != null && cacheUtils != null) {
            sImageCacheUtilsArrayList.remove(cacheUtils);
        }
    }

    public static void releaseAllImageCacheUtils() {
        if (sImageCacheUtilsArrayList != null) {
            Iterator it = sImageCacheUtilsArrayList.iterator();
            while (it.hasNext()) {
                ((ImageCacheUtils) it.next()).release();
            }
            sImageCacheUtilsArrayList.clear();
            sImageCacheUtilsArrayList = null;
        }
    }

    public ImageCacheUtils() {
        INSTANCE_COUNT++;
        this.runningRunnable = new ArrayList();
        this.mUiHandler = new Handler(Looper.getMainLooper());
        this.mThread = new Thread() {
            public void run() {
                try {
                    ImageCacheUtils.this.doLoadBitmap();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        this.mThread.setPriority(1);
        this.mThread.setName("ImageCacheUtils" + INSTANCE_COUNT);
        this.mThread.start();
        saveInstanceToCache(this);
    }

    private synchronized void putIntoRunning(Runnable runnable) {
        this.runningRunnable.add(runnable);
    }

    private synchronized void removeFromRunning(Runnable runnable) {
        this.runningRunnable.remove(runnable);
    }

    public void pause() {
        this.paused = true;
        synchronized (this) {
            notify();
        }
    }

    public void resume() {
        this.paused = false;
        synchronized (this) {
            notify();
        }
    }

    private void doLoadBitmap() throws InterruptedException {
        while (!this.isReleased) {
            if (this.paused) {
                synchronized (this) {
                    while (this.paused) {
                        wait();
                    }
                }
            }
            Runnable runnable = getLastRunnable();
            if (runnable != null) {
                putIntoRunning(runnable);
                runnable.run();
                removeFromRunning(runnable);
            } else {
                runnable = getFirstPreRunnable();
                if (runnable != null) {
                    putIntoRunning(runnable);
                    runnable.run();
                    removeFromRunning(runnable);
                } else if (this.paused) {
                    continue;
                } else {
                    synchronized (this) {
                        if (!this.paused) {
                            wait();
                        }
                    }
                }
            }
        }
    }

    private Runnable getLastRunnable() {
        return (Runnable) this.LoadImagesWorks.pollLast();
    }

    private Runnable getFirstRunnable() {
        return (Runnable) this.LoadImagesWorks.pollFirst();
    }

    private void putRunnable(Runnable runnable) {
        while (this.LoadImagesWorks.size() > 20) {
            this.LoadImagesWorks.pollFirst();
        }
        this.LoadImagesWorks.addLast(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private Runnable getFirstPreRunnable() {
        return (Runnable) this.PreloadImagesWorks.pollFirst();
    }

    private void putPreRunnable(Runnable runnable) {
        this.PreloadImagesWorks.addLast(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private void putPreRunnableFirst(Runnable runnable) {
        this.PreloadImagesWorks.addFirst(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    public void preloadLocalBitmaps(final List<BitmapViewInfo> bitmapViewInfos) {
        if (bitmapViewInfos != null && bitmapViewInfos.size() > 0) {
            runOnUIThread(new Runnable() {
                public void run() {
                    int count = 0;
                    for (BitmapViewInfo info : bitmapViewInfos) {
                        count++;
                        String filePath = info.filePath;
                        long startDuration = info.imageTime;
                        PreloadImageRunnable preloadImageRunnable = new PreloadImageRunnable(0, startDuration, ImageInfo.makeImageInfo(filePath, startDuration), null);
                        if (count < 10) {
                            preloadImageRunnable.setSpecialPre(true);
                        }
                        ImageCacheUtils.this.putPreRunnableFirst(preloadImageRunnable);
                    }
                }
            });
        }
    }

    public static void clearLocalCache() {
        if (INSTANCE_COUNT == 0) {
            PriorityThreadPoolManager.execute(new PriorityRunnable(1) {
                public void run() {
                    boolean deleteDirectory = FileUtil.deleteDirectory(Config.getFilterActivityTempImgSavePath());
                }
            });
        }
    }

    public void preloadByPath(final String localPath, final long videoDuration) {
        if (!TextUtil.isNull(localPath) && videoDuration != 0) {
            runOnUIThread(new Runnable() {
                public void run() {
                    long usedDuration = videoDuration;
                    if (videoDuration > 20000) {
                        usedDuration = 20000;
                    }
                    for (int i = 0; ((long) i) < usedDuration; i += 3000) {
                        ImageCacheUtils.this.putPreRunnable(new PreloadImageRunnable(0, (long) i, new ImageInfo(localPath, (long) i), null));
                    }
                }
            });
        }
    }

    public void preloadBitmaps(final List<LongVideosModel> modelList) {
        if (modelList != null && modelList.size() > 0) {
            runOnUIThread(new Runnable() {
                public void run() {
                    int count = 0;
                    for (LongVideosModel model : modelList) {
                        String videoPath = model.getOriginalMediaPath();
                        int startInt = (int) (model.getStartTimeMs() / 1000);
                        int durationInt = (int) (model.getCurrentDuration() / 1000);
                        if (durationInt > 20) {
                            durationInt = 20;
                        }
                        for (int i = 0; i < durationInt; i++) {
                            count++;
                            int time = (startInt + i) * 1000;
                            PreloadImageRunnable preloadImageRunnable = new PreloadImageRunnable(model.mediaType, (long) time, new ImageInfo(videoPath, (long) time), null);
                            if (count <= 10) {
                                preloadImageRunnable.setSpecialPre(true);
                            }
                            ImageCacheUtils.this.putPreRunnable(preloadImageRunnable);
                        }
                    }
                }
            });
        }
    }

    public void preloadBitmap(String filePath, int modelType, long startDuration, GetBitmapCallback callback) {
        PreloadImageRunnable preloadImageRunnable = new PreloadImageRunnable(modelType, startDuration, ImageInfo.makeImageInfo(filePath, startDuration), callback);
        preloadImageRunnable.setSpecialPre(true);
        putPreRunnableFirst(preloadImageRunnable);
    }

    public void getBitmap(ImageView imageView, String filePath, int modelType, long startDuration, GetBitmapCallback callback) {
        if (!TextUtil.isNull(filePath)) {
            final ImageView imageView2 = imageView;
            final String str = filePath;
            final long j = startDuration;
            final int i = modelType;
            final GetBitmapCallback getBitmapCallback = callback;
            runOnUIThread(new Runnable() {
                public void run() {
                    ImageCacheUtils.this.cancelLoadingRunnable(imageView2);
                    PreloadImageRunnable preloadImageRunnable = new PreloadImageRunnable(i, j, ImageInfo.makeImageInfo(str, j), getBitmapCallback);
                    if (imageView2 != null && (imageView2 instanceof CacheRunnableImageView)) {
                        ((CacheRunnableImageView) imageView2).setBitmapWorkerTaskReference(new WeakReference(preloadImageRunnable));
                    }
                    ImageCacheUtils.this.putRunnable(preloadImageRunnable);
                }
            });
        }
    }

    private void cancelLoadingRunnable(ImageView imageView) {
        if (imageView instanceof CacheRunnableImageView) {
            WeakReference<PreloadImageRunnable> bitmapWorkerTaskReference = ((CacheRunnableImageView) imageView).getBitmapWorkerTaskReference();
            if (bitmapWorkerTaskReference != null) {
                PreloadImageRunnable preloadImageRunnable = (PreloadImageRunnable) bitmapWorkerTaskReference.get();
                if (preloadImageRunnable != null) {
                    preloadImageRunnable.setCanceled(true);
                }
            }
        }
    }

    private void saveBitmapToLocal(ImageInfo imageInfo, Bitmap bitmap, boolean needRecycle) {
        if (bitmap != null && imageInfo != null) {
            try {
                BitmapUtils.saveBitmapToLocal(bitmap, new File(Config.getFilterActivityTempImgSavePath(), getFileName(imageInfo)).getAbsolutePath());
                if (needRecycle) {
                    recycleBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recycleBitmap(Bitmap mediaBitmap) {
        if (mediaBitmap != null && !mediaBitmap.isRecycled()) {
            mediaBitmap.recycle();
        }
    }

    private String getFileName(ImageInfo imageInfo) {
        return getFileName(imageInfo.path, imageInfo.time);
    }

    private String getFileName(String filePath, long imageTime) {
        return ProxyCacheUtils.computeMD5(String.format("%s_%s", new Object[]{filePath, Long.valueOf(imageTime)}));
    }

    private void runOnUIThread(Runnable runnable) {
        if (this.mUiHandler != null) {
            this.mUiHandler.post(runnable);
        }
    }

    private synchronized Bitmap getCachedBitmap(ImageInfo info) {
        return (Bitmap) this.mBitmapLruCache.get(info);
    }

    private synchronized void saveBitmapToCache(Bitmap bitmap, ImageInfo info) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                this.mBitmapLruCache.put(info, bitmap);
            }
        }
    }

    private synchronized void freeBitmaps() {
    }

    public void clearAllCurRunnables() {
        this.PreloadImagesWorks.clear();
        this.LoadImagesWorks.clear();
    }

    public void release() {
        if (!this.isReleased) {
            if (this.mUiHandler != null) {
                this.mUiHandler.removeCallbacksAndMessages(null);
                this.mUiHandler = null;
            }
            this.isReleased = true;
            freeBitmaps();
            if (this.mBitmapLruCache != null) {
                this.mBitmapLruCache.evictAll();
            }
            INSTANCE_COUNT--;
            removeFromCache(this);
        }
    }
}