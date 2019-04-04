package cn.nineton.onetake.util;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
//import com.blink.academy.onetake.VideoTools.WaveformOutput;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.LongVideoSelectActivity.BitmapViewInfo;
//import com.blink.academy.onetake.ui.activity.video.VideoSelectActivity;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
//import com.blink.academy.onetake.widgets.AudioWave.LargeAudioWaveView;
//import com.tencent.android.tpush.common.Constants;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import cn.nineton.onetake.bean.BitmapViewInfo;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.WaveformOutput;
import cn.nineton.onetake.widget.AudioWaveView;
import cn.nineton.onetake.widget.LargeAudioWaveView;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class WaveformCacheUtils {
    private static final long CACHE_DURATION_SPLIT_UNIT = 10000000;
    private static final long DEFAULT_LOAD_WAVEFORM_DURATION = 5000000;
    private static final String TAG = WaveformCacheUtils.class.getSimpleName();
    private static WaveformCacheUtils sWaveformCacheUtils;
    private int DEFAULT_GET_WAVE_FORM_RATE = 10000;//Constants.ERRORCODE_UNKNOWN;
    private int DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S = 10000;// Constants.ERRORCODE_UNKNOWN;
    private LinkedBlockingDeque<Runnable> LoadWavesWorks = new LinkedBlockingDeque();
    private LinkedList<Runnable> PreloadWaveDatasWorks = new LinkedList();
    private int UNIT_WAVE_CACHE_DURATION_TIME = 1000000;//VideoSelectActivity.MILLION;
    private boolean canRunnableRun;
    private boolean isReleased = false;
    private Thread mThread = new Thread() {
        public void run() {
            try {
                WaveformCacheUtils.this.doLoadWaveData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private HashMap<String, WaveformCacheModel> mWaveformDataHashMap = new HashMap();
    private boolean paused = false;
    private ArrayList<Runnable> runningRunnable = new ArrayList();
    private LinkedBlockingDeque<Runnable> topLevelWorks = new LinkedBlockingDeque();

    public class GetWaveformDataRunnable implements Runnable {
        long audioVerse;
        WaveDataCallback callback;
        boolean canceled;
        long durationUs;
        String filePath;
        private boolean needCheckTag;
        long needStartUs;
        long sumDuration;

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        public GetWaveformDataRunnable(WaveformCacheUtils this$0, String filePath, long needStartUs, long durationUs, long sumDuration) {
            this(-1, filePath, needStartUs, durationUs, sumDuration);
        }

        public GetWaveformDataRunnable(long audioVerse, String filePath, long needStartUs, long durationUs, long sumDurationUs) {
            this.audioVerse = audioVerse;
            this.filePath = filePath;
            this.needStartUs = needStartUs;
            this.durationUs = durationUs;
            this.sumDuration = sumDurationUs;
        }

        public void setCallback(WaveDataCallback callback) {
            this.callback = callback;
        }

        public void setNeedCheckTag(boolean needCheckTag) {
            this.needCheckTag = needCheckTag;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GetWaveformDataRunnable that = (GetWaveformDataRunnable) o;
            if (this.needStartUs != that.needStartUs || this.durationUs != that.durationUs || this.sumDuration != that.sumDuration) {
                return false;
            }
            if (this.filePath != null) {
                z = this.filePath.equals(that.filePath);
            } else if (that.filePath != null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((((this.filePath != null ? this.filePath.hashCode() : 0) * 31) + ((int) (this.needStartUs ^ (this.needStartUs >>> 32)))) * 31) + ((int) (this.durationUs ^ (this.durationUs >>> 32)))) * 31) + ((int) (this.sumDuration ^ (this.sumDuration >>> 32)));
        }

        public void run() {
            if (checkCanceled() || isCanceled() || this.sumDuration == 0 || this.needStartUs < 0 || this.durationUs < 0 || this.sumDuration < 0) {
                return;
            }
            if (this.audioVerse < 0 && this.needStartUs + this.durationUs > this.sumDuration) {
                return;
            }
            float[] result;
            if (this.needStartUs + this.durationUs <= this.sumDuration) {
                result = WaveformCacheUtils.this.loadData(this.needStartUs, this.durationUs, this.sumDuration, this.filePath);
                if (result != null) {
                    if (this.callback != null) {
                        this.callback.onLoadFinished(this.filePath, this.needStartUs, this.durationUs, result);
                        return;
                    }
                    return;
                } else if (this.callback != null) {
                    this.callback.onLoadFailed(this.filePath, this.needStartUs, this.durationUs);
                    return;
                } else {
                    return;
                }
            }
            long firstStart;
            long firstDuration;
            boolean needSecond;
            int firstSize;
            long needEndTimeUs = this.needStartUs + this.durationUs;
            long repeatTimeUs = this.sumDuration - this.audioVerse;
            if (this.needStartUs > this.sumDuration) {
                firstStart = ((this.needStartUs - this.sumDuration) % repeatTimeUs) + this.audioVerse;
            } else {
                firstStart = this.needStartUs;
            }
            if (this.durationUs + firstStart > this.sumDuration) {
                firstDuration = this.sumDuration - firstStart;
                needSecond = true;
            } else {
                firstDuration = this.durationUs;
                needSecond = false;
            }
            float[] firstResult = WaveformCacheUtils.this.loadData(firstStart, firstDuration, this.sumDuration, this.filePath);
            boolean firstIsNotValid = firstResult == null;
            if (firstIsNotValid) {
                firstSize = 0;
            } else {
                firstSize = firstResult.length;
            }
            int multiple = 0;
            boolean restDurationNotValid = true;
            boolean secondIsNotValid = true;
            if (needSecond) {
                long secondStart;
                long secondDuration;
                int resultSize;
                if (needEndTimeUs - this.sumDuration > repeatTimeUs) {
                    long remainder = (needEndTimeUs - this.sumDuration) % repeatTimeUs;
                    secondStart = this.audioVerse;
                    secondDuration = remainder;
                    multiple = (int) (((this.durationUs - this.sumDuration) - firstStart) / repeatTimeUs);
                } else {
                    secondStart = this.audioVerse;
                    secondDuration = needEndTimeUs - this.sumDuration;
                }
                float[] restDurationResult = null;
                if (multiple > 0) {
                    restDurationResult = WaveformCacheUtils.this.loadData(this.audioVerse, this.sumDuration - this.audioVerse, this.sumDuration, this.filePath);
                }
                restDurationNotValid = restDurationResult == null;
                float[] secondResult = WaveformCacheUtils.this.loadData(secondStart, secondDuration, this.sumDuration, this.filePath);
                secondIsNotValid = secondResult == null;
                int secondSize = secondIsNotValid ? 0 : secondResult.length;
                if (multiple <= 0 || restDurationNotValid) {
                    resultSize = firstSize + secondSize;
                } else {
                    resultSize = ((restDurationResult.length * multiple) + firstSize) + secondSize;
                }
                result = new float[resultSize];
                if (resultSize > firstSize + secondSize) {
                    if (!firstIsNotValid) {
                        System.arraycopy(firstResult, 0, result, 0, firstSize);
                    }
                    if (!restDurationNotValid) {
                        for (int i = 0; i < multiple; i++) {
                            System.arraycopy(restDurationResult, 0, result, (restDurationResult.length * i) + firstSize, restDurationResult.length);
                        }
                    }
                    if (!secondIsNotValid) {
                        if (restDurationNotValid) {
                            System.arraycopy(secondResult, 0, result, firstSize, secondSize);
                        } else {
                            System.arraycopy(secondResult, 0, result, (restDurationResult.length * multiple) + firstSize, secondSize);
                        }
                    }
                } else {
                    if (!firstIsNotValid) {
                        System.arraycopy(firstResult, 0, result, 0, firstSize);
                    }
                    if (!secondIsNotValid) {
                        System.arraycopy(secondResult, 0, result, firstSize, secondSize);
                    }
                }
            } else {
                result = new float[firstSize];
                if (!firstIsNotValid) {
                    System.arraycopy(firstResult, 0, result, 0, firstSize);
                }
            }
            if (!firstIsNotValid) {
                if (!needSecond) {
                    this.callback.onLoadFinished(this.filePath, this.needStartUs, this.durationUs, result);
                    return;
                } else if (!secondIsNotValid) {
                    if (multiple <= 0) {
                        this.callback.onLoadFinished(this.filePath, this.needStartUs, this.durationUs, result);
                        return;
                    } else if (!restDurationNotValid) {
                        this.callback.onLoadFinished(this.filePath, this.needStartUs, this.durationUs, result);
                        return;
                    }
                }
            }
            if (this.callback != null) {
                this.callback.onLoadFailed(this.filePath, this.needStartUs, this.durationUs);
            }
        }

        private boolean checkCanceled() {
            return this.needCheckTag && !WaveformCacheUtils.this.canRunnableRun;
        }
    }

    public interface WaveDataCallback {
        void onLoadFailed(String str, long j, long j2);

        void onLoadFinished(String str, long j, long j2, float[] fArr);
    }

    private class WaveformCacheModel {
        HashMap<String, float[]> datas = new HashMap();
        long durationUs;
        long startTimeUs;
        public WaveformCacheModel(){}
        public WaveformCacheModel(long startTimeUs, long durationUs) {
            this.startTimeUs = startTimeUs;
            this.durationUs = durationUs;
        }

        public long getStartTimeUs() {
            return this.startTimeUs;
        }

        public void setStartTimeUs(long startTimeUs) {
            this.startTimeUs = startTimeUs;
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public void setDurationUs(long durationUs) {
            this.durationUs = durationUs;
        }

        public float[] getData(String index, float startPercent, float lengthPercent) {
            float[] floats = (float[]) this.datas.get(index);
            int length = (int) (((float) floats.length) * lengthPercent);
            float[] result = new float[length];
            System.arraycopy(floats, (int) (((float) floats.length) * startPercent), result, 0, length);
            return result;
        }

        public HashMap<String, float[]> getDatas() {
            return this.datas;
        }

        public void setData(String index, float[] data) {
            this.datas.put(index, data);
        }

        public boolean containIndexes(String indexes) {
            if (TextUtil.isValidate(this.datas) && this.datas.keySet().contains(indexes)) {
                return true;
            }
            return false;
        }
    }

    public static WaveformCacheUtils getWaveformCacheUtils() {
        if (sWaveformCacheUtils == null) {
            sWaveformCacheUtils = new WaveformCacheUtils();
        }
        return sWaveformCacheUtils;
    }

    private WaveformCacheUtils() {
        this.mThread.setPriority(1);
        this.mThread.setName("WaveformCacheUtils");
        this.mThread.start();
    }

    private Runnable getFirstTopLevelRunnable() {
        return (Runnable) this.topLevelWorks.pollFirst();
    }

    private void putToTopLevelRunnable(Runnable runnable) {
        this.topLevelWorks.addLast(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private void putToTopLevelRunnableFirst(Runnable runnable) {
        this.topLevelWorks.addFirst(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private Runnable getFirstRunnable() {
        return (Runnable) this.LoadWavesWorks.pollFirst();
    }

    private void putRunnable(Runnable runnable) {
        while (this.LoadWavesWorks.size() > 20) {
            this.LoadWavesWorks.pollFirst();
        }
        this.LoadWavesWorks.addLast(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private synchronized WaveformCacheModel getWavedataFromCache(String filePath) {
        return (WaveformCacheModel) this.mWaveformDataHashMap.get(filePath);
    }

    private synchronized void saveWavedataToCache(String filePath, WaveformCacheModel waveformCacheModel) {
        this.mWaveformDataHashMap.put(filePath, waveformCacheModel);
    }

    private synchronized void removeWavedataFromCache(String filePath) {
        this.mWaveformDataHashMap.remove(filePath);
    }

    public void getAudioWaveData(View audioWaveView, String filePath, long startTimeUs, long needStartTimeUs, long durationUs, long sumTimeUs, WaveDataCallback callback) {
        getAudioWaveData(audioWaveView, -1, filePath, startTimeUs, durationUs, sumTimeUs, callback);
    }

    public void getAudioWaveData(View audioWaveView, long verse, String filePath, long needStartTimeUs, long durationUs, long sumTimeUs, WaveDataCallback callback) {
        getAudioWaveData(false, audioWaveView, verse, filePath, needStartTimeUs, durationUs, sumTimeUs, callback);
    }

    public void getAudioWaveData(boolean addTofirst, View audioWaveView, long verse, String filePath, long needStartTimeUs, long durationUs, long sumTimeUs, WaveDataCallback callback) {
        final View view = audioWaveView;
        final long j = verse;
        final String str = filePath;
        final long j2 = needStartTimeUs;
        final long j3 = durationUs;
        final long j4 = sumTimeUs;
        final WaveDataCallback waveDataCallback = callback;
        final boolean z = addTofirst;
        runOnUIThread(new Runnable() {
            public void run() {
                WaveformCacheUtils.this.cancelLoadingRunnable(view);
                GetWaveformDataRunnable getWaveformDataRunnable = new GetWaveformDataRunnable(j, str, j2, j3, j4);
                getWaveformDataRunnable.setCallback(waveDataCallback);
                if (view != null) {
                    WeakReference<GetWaveformDataRunnable> reference = new WeakReference(getWaveformDataRunnable);
                    if (view instanceof AudioWaveView) {
                        ((AudioWaveView) view).setWaveFormWorkReference(reference);
                    } else if (view instanceof LargeAudioWaveView) {
                        ((LargeAudioWaveView) view).setWaveFormWorkReference(reference);
                    }
                }
                if (j == -1) {
                    WaveformCacheUtils.this.putRunnable(getWaveformDataRunnable);
                } else if (z) {
                    getWaveformDataRunnable.setNeedCheckTag(true);
                    WaveformCacheUtils.this.putToTopLevelRunnableFirst(getWaveformDataRunnable);
                } else {
                    WaveformCacheUtils.this.putToTopLevelRunnable(getWaveformDataRunnable);
                }
            }
        });
    }

    private void cancelLoadingRunnable(View audioWaveView) {
        if (audioWaveView != null && (audioWaveView instanceof AudioWaveView)) {
            WeakReference<GetWaveformDataRunnable> waveFormWorkReference = ((AudioWaveView) audioWaveView).getWaveFormWorkReference();
            if (waveFormWorkReference != null) {
                GetWaveformDataRunnable getWaveformDataRunnable = (GetWaveformDataRunnable) waveFormWorkReference.get();
                if (getWaveformDataRunnable != null) {
                    getWaveformDataRunnable.setCanceled(true);
                }
            }
        }
    }

    public void getAudioWaveData(LargeAudioWaveView largeAudioWaveView, long verse, String filePath, long needStartTimeUs, long durationUs, long sumTimeUs, WaveDataCallback callback) {
        final long j = verse;
        final String str = filePath;
        final long j2 = needStartTimeUs;
        final long j3 = durationUs;
        final long j4 = sumTimeUs;
        final WaveDataCallback waveDataCallback = callback;
        final LargeAudioWaveView largeAudioWaveView2 = largeAudioWaveView;
        runOnUIThread(new Runnable() {
            public void run() {
                GetWaveformDataRunnable getWaveformDataRunnable = new GetWaveformDataRunnable(j, str, j2, j3, j4);
                getWaveformDataRunnable.setCallback(waveDataCallback);
                largeAudioWaveView2.putIntoReferences(getWaveformDataRunnable);
                if (j != -1) {
                    WaveformCacheUtils.this.putToTopLevelRunnable(getWaveformDataRunnable);
                } else {
                    WaveformCacheUtils.this.putRunnable(getWaveformDataRunnable);
                }
            }
        });
    }

    private void saveNewData(float[] src, float[] dest, int desPos) {
        if (src != null && dest != null && src.length + desPos <= dest.length) {
            System.arraycopy(src, 0, dest, desPos, src.length);
        }
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

    private void doLoadWaveData() throws InterruptedException {
        while (!this.isReleased) {
            if (this.paused) {
                synchronized (this) {
                    while (this.paused) {
                        wait();
                    }
                }
            }
            Runnable runnable = getFirstTopLevelRunnable();
            if (runnable == null) {
                runnable = getFirstRunnable();
            }
            if (runnable == null) {
                runnable = getFirstPreRunnable();
            }
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

    private synchronized void putIntoRunning(Runnable runnable) {
        this.runningRunnable.add(runnable);
    }

    private synchronized void removeFromRunning(Runnable runnable) {
        this.runningRunnable.remove(runnable);
    }

    public void preLoadAudioWaveData(String filePath, float startTime, float durationTime, long sumTimeUs) {
        runOnUIThread(new Runnable() {
            public void run() {
            }
        });
    }

    public void preLoadLocalWaveDatas(final List<BitmapViewInfo> bitmapViewInfos) {
        if (bitmapViewInfos != null && bitmapViewInfos.size() > 0) {
            runOnUIThread(new Runnable() {
                public void run() {
                    for (BitmapViewInfo info : bitmapViewInfos) {
                        WaveformCacheUtils.this.putPreRunnable(new GetWaveformDataRunnable(WaveformCacheUtils.this, info.filePath, info.waveStartTime, info.waveDuration, info.fileSumTimeUs));
                    }
                }
            });
        }
    }

    public void preloadWaveDatas(final List<LongVideosModel> modelList) {
        if (modelList != null && modelList.size() > 0) {
            runOnUIThread(new Runnable() {
                public void run() {
                    for (LongVideosModel model : modelList) {
                        String videoPath = model.getOriginalMediaPath();
                        long totalDurationValueUs = model.getTotalDurationValue() * 1000;
                        long currentDurationUs = model.getCurrentDurationValue() * 1000;
                        long startTimeUs = model.getStartTimeMs() * 1000;
                        long durationUs = WaveformCacheUtils.DEFAULT_LOAD_WAVEFORM_DURATION;
                        if (totalDurationValueUs < WaveformCacheUtils.DEFAULT_LOAD_WAVEFORM_DURATION) {
                            durationUs = totalDurationValueUs;
                        }
                        if (currentDurationUs < durationUs) {
                            durationUs = currentDurationUs;
                        }
                        WaveformCacheUtils.this.putPreRunnable(new GetWaveformDataRunnable(WaveformCacheUtils.this, videoPath, startTimeUs, durationUs, totalDurationValueUs));
                    }
                }
            });
        }
    }

    public void setCanRunnableRun(boolean canRunnableRun) {
        this.canRunnableRun = canRunnableRun;
    }

    private Runnable getFirstPreRunnable() {
        return (Runnable) this.PreloadWaveDatasWorks.pollFirst();
    }

    private void putPreRunnable(Runnable runnable) {
        this.PreloadWaveDatasWorks.addLast(runnable);
        if (!this.paused) {
            synchronized (this) {
                notify();
            }
        }
    }

    private void putFirstPreRunnable(Runnable runnable) {
        this.PreloadWaveDatasWorks.addFirst(runnable);
    }

    private long dealUsTime(long time) {
        return (time / 1000000) * 1000000;
    }

    public float[] loadDataFromMemCache(long needStartUs, long durationUs, long sumDuration, String filePath, long audioVerse) {
        if (sumDuration == 0) {
            return null;
        }
        if (needStartUs < 0 || durationUs < 0 || sumDuration < 0) {
            return null;
        }
        if (audioVerse < 0) {
            return null;
        }
        if (needStartUs + durationUs <= sumDuration) {
            return loadDataFromMem(needStartUs, durationUs, sumDuration, filePath);
        }
        long firstStart;
        long firstDuration;
        boolean needSecond;
        int firstSize;
        long needEndTimeUs = needStartUs + durationUs;
        long repeatTimeUs = sumDuration - audioVerse;
        if (needStartUs > sumDuration) {
            firstStart = ((needStartUs - sumDuration) % repeatTimeUs) + audioVerse;
        } else {
            firstStart = needStartUs;
        }
        if (firstStart + durationUs > sumDuration) {
            firstDuration = sumDuration - firstStart;
            needSecond = true;
        } else {
            firstDuration = durationUs;
            needSecond = false;
        }
        float[] firstResult = loadDataFromMem(firstStart, firstDuration, sumDuration, filePath);
        boolean firstIsNotValid = firstResult == null;
        if (firstIsNotValid) {
            firstSize = 0;
        } else {
            firstSize = firstResult.length;
        }
        int multiple = 0;
        boolean restDurationNotValid = true;
        boolean secondIsNotValid = true;
        float[] result;
        if (needSecond) {
            long secondStart;
            long secondDuration;
            int resultSize;
            if (needEndTimeUs - sumDuration > repeatTimeUs) {
                secondStart = audioVerse;
                secondDuration = (needEndTimeUs - sumDuration) % repeatTimeUs;
                multiple = (int) (((durationUs - sumDuration) - firstStart) / repeatTimeUs);
            } else {
                secondStart = audioVerse;
                secondDuration = needEndTimeUs - sumDuration;
            }
            float[] restDurationResult = null;
            if (multiple > 0) {
                restDurationResult = loadDataFromMem(audioVerse, sumDuration - audioVerse, sumDuration, filePath);
            }
            restDurationNotValid = restDurationResult == null;
            float[] secondResult = loadDataFromMem(secondStart, secondDuration, sumDuration, filePath);
            secondIsNotValid = secondResult == null;
            int secondSize = secondIsNotValid ? 0 : secondResult.length;
            if (multiple <= 0 || restDurationNotValid) {
                resultSize = firstSize + secondSize;
            } else {
                resultSize = ((restDurationResult.length * multiple) + firstSize) + secondSize;
            }
            result = new float[resultSize];
            if (resultSize > firstSize + secondSize) {
                if (!firstIsNotValid) {
                    System.arraycopy(firstResult, 0, result, 0, firstSize);
                }
                if (!restDurationNotValid) {
                    for (int i = 0; i < multiple; i++) {
                        System.arraycopy(restDurationResult, 0, result, (restDurationResult.length * i) + firstSize, restDurationResult.length);
                    }
                }
                if (!secondIsNotValid) {
                    if (restDurationNotValid) {
                        System.arraycopy(secondResult, 0, result, firstSize, secondSize);
                    } else {
                        System.arraycopy(secondResult, 0, result, (restDurationResult.length * multiple) + firstSize, secondSize);
                    }
                }
            } else {
                if (!firstIsNotValid) {
                    System.arraycopy(firstResult, 0, result, 0, firstSize);
                }
                if (!secondIsNotValid) {
                    System.arraycopy(secondResult, 0, result, firstSize, secondSize);
                }
            }
        } else {
            result = new float[firstSize];
            if (!firstIsNotValid) {
                System.arraycopy(firstResult, 0, result, 0, firstSize);
            }
        }
        if (firstIsNotValid) {
            return null;
        }
        if (!needSecond) {
            return result;
        }
        if (secondIsNotValid) {
            return null;
        }
        if (multiple <= 0 || !restDurationNotValid) {
            return result;
        }
        return null;
    }

    private float[] loadDataFromMem(long needStartUs, long durationUs, long sumDuration, String filePath) {
        WaveformCacheModel wavedataFromCache = getWavedataFromCache(filePath);
        List<String[]> indexes = getCacheIndexes(needStartUs, durationUs, sumDuration);
        List<float[]> tempCachesFloats = new ArrayList();
        if (wavedataFromCache == null) {
            return null;
        }
        boolean contain = true;
        List<String> noCacheIndexes = new ArrayList();
        for (int i = 0; i < indexes.size(); i++) {
            String[] strings = (String[]) indexes.get(i);
            if (wavedataFromCache.containIndexes(strings[0])) {
                tempCachesFloats.add(wavedataFromCache.getData(strings[0], Float.valueOf(strings[1]).floatValue(), Float.valueOf(strings[2]).floatValue()));
            } else {
                contain = false;
                noCacheIndexes.add(strings[0]);
            }
        }
        if (contain) {
            return getCacheData(tempCachesFloats);
        }
        return null;
    }

    private float[] loadData(long needStartUs, long durationUs, long sumDuration, String filePath) {
        WaveformCacheModel wavedataFromCache = getWavedataFromCache(filePath);
        List<String[]> indexes = getCacheIndexes(needStartUs, durationUs, sumDuration);
        List<float[]> tempCachesFloats = new ArrayList();
        int i;
        String[] strings;
        int index;
        if (wavedataFromCache != null) {
            boolean contain = true;
            List<String> noCacheIndexes = new ArrayList();
            for (i = 0; i < indexes.size(); i++) {
                strings = (String[]) indexes.get(i);
                if (wavedataFromCache.containIndexes(strings[0])) {
                    tempCachesFloats.add(wavedataFromCache.getData(strings[0], Float.valueOf(strings[1]).floatValue(), Float.valueOf(strings[2]).floatValue()));
                } else {
                    contain = false;
                    noCacheIndexes.add(strings[0]);
                }
            }
            if (contain) {
                return getCacheData(tempCachesFloats);
            }
            for (i = 0; i < noCacheIndexes.size(); i++) {
                index = Integer.valueOf((String) noCacheIndexes.get(i)).intValue();
                float[] trueDate = getTrueData((((long) index) * CACHE_DURATION_SPLIT_UNIT) - ((long) this.UNIT_WAVE_CACHE_DURATION_TIME), CACHE_DURATION_SPLIT_UNIT + ((long) (this.UNIT_WAVE_CACHE_DURATION_TIME * 2)), filePath, sumDuration);
                if (trueDate != null) {
                    wavedataFromCache.setData(String.valueOf(index), trueDate);
                }
            }
            tempCachesFloats.clear();
            for (i = 0; i < indexes.size(); i++) {
                strings = (String[]) indexes.get(i);
                tempCachesFloats.add(wavedataFromCache.getData(strings[0], Float.valueOf(strings[1]).floatValue(), Float.valueOf(strings[2]).floatValue()));
            }
            return getCacheData(tempCachesFloats);
        }
        WaveformCacheModel waveformCacheModel = new WaveformCacheModel();
        for (i = 0; i < indexes.size(); i++) {
            index = Integer.valueOf(((String[]) indexes.get(i))[0]).intValue();
            float[] trueData = getTrueData((((long) index) * CACHE_DURATION_SPLIT_UNIT) - ((long) this.UNIT_WAVE_CACHE_DURATION_TIME), CACHE_DURATION_SPLIT_UNIT + ((long) (this.UNIT_WAVE_CACHE_DURATION_TIME * 2)), filePath, sumDuration);
            if (trueData != null) {
                waveformCacheModel.setData(String.valueOf(index), trueData);
            }
        }
        tempCachesFloats.clear();
        for (i = 0; i < indexes.size(); i++) {
            strings = (String[]) indexes.get(i);
            tempCachesFloats.add(waveformCacheModel.getData(strings[0], Float.valueOf(strings[1]).floatValue(), Float.valueOf(strings[2]).floatValue()));
        }
        float[] result = getCacheData(tempCachesFloats);
        saveWavedataToCache(filePath, waveformCacheModel);
        return result;
    }

    private float[] getTrueData(long startTime, long duration, String filePath, long sumDuration) {
        if (startTime < 0) {
            startTime = 0;
        }
        if (duration + startTime > sumDuration) {
            duration = sumDuration - startTime;
        } else {
            duration = dealUsTime(duration);
        }
        float[] waveformDataFromFile = getWaveformDataFromFile(filePath, startTime, duration);
        if (waveformDataFromFile == null) {
            return null;
        }
        int start;
        int length;
        if (startTime == 0) {
            start = 0;
        } else {
            start = (int) (((1.0f * ((float) this.UNIT_WAVE_CACHE_DURATION_TIME)) / ((float) duration)) * ((float) waveformDataFromFile.length));
        }
        if (duration != sumDuration - startTime) {
            length = waveformDataFromFile.length - (start * 2);
        } else {
            length = waveformDataFromFile.length - start;
        }
        float[] trueData = new float[length];
        System.arraycopy(waveformDataFromFile, start, trueData, 0, length);
        return trueData;
    }

    private float[] getCacheData(List<float[]> tempCachesFloats) {
        int i;
        int size = 0;
        for (i = 0; i < tempCachesFloats.size(); i++) {
            size += ((float[]) tempCachesFloats.get(i)).length;
        }
        float[] cacheData = new float[size];
        int sizeToAdd = 0;
        for (i = 0; i < tempCachesFloats.size(); i++) {
            System.arraycopy(tempCachesFloats.get(i), 0, cacheData, sizeToAdd, ((float[]) tempCachesFloats.get(i)).length);
            sizeToAdd += ((float[]) tempCachesFloats.get(i)).length;
        }
        return cacheData;
    }

    private String[] getCacheInfoArray(String index, String start, String duration) {
        return new String[]{index, start, duration};
    }

    private List<String[]> getCacheIndexes(long needStartTime, long durationUs, long sumDuration) {
        List<String[]> list = new ArrayList();
        long tempRemainder = sumDuration % CACHE_DURATION_SPLIT_UNIT;
        int lastIndex = (int) (sumDuration / CACHE_DURATION_SPLIT_UNIT);
        if (tempRemainder != 0) {
            lastIndex++;
        } else {
            tempRemainder = CACHE_DURATION_SPLIT_UNIT;
        }
        int index = (int) (needStartTime / CACHE_DURATION_SPLIT_UNIT);
        float startPercent = (1.0f * ((float) (needStartTime - (((long) index) * CACHE_DURATION_SPLIT_UNIT)))) / 1.0E7f;
        float durationPercent;
        if ((needStartTime - (((long) index) * CACHE_DURATION_SPLIT_UNIT)) + durationUs <= CACHE_DURATION_SPLIT_UNIT) {
            if (index != lastIndex) {
                durationPercent = (((float) durationUs) * 1.0f) / 1.0E7f;
            } else {
                durationPercent = (((float) durationUs) * 1.0f) / ((float) tempRemainder);
            }
            list.add(getCacheInfoArray(String.valueOf(index), String.valueOf(startPercent), String.valueOf(durationPercent)));
        } else {
            long l = CACHE_DURATION_SPLIT_UNIT - (needStartTime - (((long) index) * CACHE_DURATION_SPLIT_UNIT));
            durationUs -= l;
            durationPercent = (((float) l) * 1.0f) / 1.0E7f;
            list.add(getCacheInfoArray(String.valueOf(index), String.valueOf(startPercent), String.valueOf(durationPercent)));
            int multiple = (int) (durationUs / CACHE_DURATION_SPLIT_UNIT);
            if (multiple > 0) {
                int remainder = (int) (durationUs % CACHE_DURATION_SPLIT_UNIT);
                for (int i = 0; i < multiple; i++) {
                    list.add(getCacheInfoArray(String.valueOf((index + i) + 1), String.valueOf(StaticLayoutUtil.DefaultSpacingadd), String.valueOf(1.0f)));
                    if (i == multiple - 1 && remainder != 0) {
                        if ((index + i) + 2 == lastIndex) {
                            list.add(getCacheInfoArray(String.valueOf((index + i) + 2), String.valueOf(StaticLayoutUtil.DefaultSpacingadd), String.valueOf((1.0f * ((float) remainder)) / ((float) tempRemainder))));
                        } else {
                            list.add(getCacheInfoArray(String.valueOf((index + i) + 2), String.valueOf(StaticLayoutUtil.DefaultSpacingadd), String.valueOf((1.0f * ((float) remainder)) / 1.0E7f)));
                        }
                    }
                }
            } else if (index + 1 == lastIndex) {
                list.add(getCacheInfoArray(String.valueOf(index + 1), String.valueOf(StaticLayoutUtil.DefaultSpacingadd), String.valueOf((1.0f * ((float) durationUs)) / ((float) tempRemainder))));
            } else {
                list.add(getCacheInfoArray(String.valueOf(index + 1), String.valueOf(StaticLayoutUtil.DefaultSpacingadd), String.valueOf((1.0f * ((float) durationUs)) / 1.0E7f)));
            }
        }
        return list;
    }

    private float[] saveWaveformToCache(float[] waveformCache, long cacheStartTime, long cacheDuration, float[] data, long dataStartTime, long dataDuration) {
        int dataLength = data.length;
        int waveformCacheLength = waveformCache.length;
        long cacheEnd = cacheStartTime + cacheDuration;
        long dataEnd = dataStartTime + dataDuration;
        if (dataLength == 0) {
            return waveformCache;
        }
        if (waveformCacheLength == 0) {
            return data;
        }
        int startPos;
        int newLength;
        float[] result;
        if (dataStartTime < cacheStartTime && dataStartTime + dataDuration > cacheStartTime) {
            startPos = (((int) ((cacheStartTime - dataStartTime) / ((long) this.UNIT_WAVE_CACHE_DURATION_TIME))) + 1) * this.DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S;
            int cacheLeftLength = waveformCacheLength - this.DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S;
            newLength = startPos + cacheLeftLength;
            if (newLength >= dataLength && newLength >= waveformCacheLength) {
                result = Arrays.copyOf(data, newLength);
                System.arraycopy(waveformCache, this.DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S, result, startPos, cacheLeftLength);
                return result;
            } else if (dataLength > waveformCacheLength && dataLength > 0) {
                return data;
            } else {
                if (waveformCacheLength <= dataLength || waveformCacheLength <= 0) {
                    return waveformCache;
                }
                return waveformCache;
            }
        } else if (dataStartTime < cacheStartTime || dataEnd <= cacheEnd) {
            return waveformCache;
        } else {
            if (dealUsTime(cacheEnd) != cacheEnd) {
                return waveformCache;
            }
            startPos = waveformCacheLength - (waveformCacheLength % this.DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S);
            int secondStartPos = ((int) (((cacheEnd - ((long) this.UNIT_WAVE_CACHE_DURATION_TIME)) - dataStartTime) / ((long) this.UNIT_WAVE_CACHE_DURATION_TIME))) * this.DEFAULT_NUMBER_OF_WAVE_FORM_DATA_1_S;
            int needWriteLength = dataLength - secondStartPos;
            newLength = startPos + needWriteLength;
            if (newLength >= dataLength && newLength >= waveformCacheLength) {
                result = Arrays.copyOf(waveformCache, newLength);
                System.arraycopy(data, secondStartPos, result, startPos, needWriteLength);
                return result;
            } else if (dataLength > waveformCacheLength && dataLength > 0) {
                return data;
            } else {
                if (waveformCacheLength <= dataLength || waveformCacheLength <= 0) {
                    return waveformCache;
                }
                return waveformCache;
            }
        }
    }

    private float[] getWaveformDataFromFile(String filepath, long startUs, long durationUs) {
        try {
            WaveformOutput waveformOutput = new WaveformOutput(filepath, startUs, durationUs, this.DEFAULT_GET_WAVE_FORM_RATE);
            waveformOutput.waitUntilFinished();
            waveformOutput.close();
            return Arrays.copyOfRange(waveformOutput.getAvailableData(), 0, waveformOutput.getAvailableCount());
        } catch (IOException e) {
            return null;
        }
    }

    private float[] getNeedAreaData(float[] data, long dataStartTime, long dataDurationUs, long needStartTimeUs, long needDurationUs) {
        if (dataStartTime == needStartTimeUs && dataDurationUs == needDurationUs) {
            return data;
        }
        if (needStartTimeUs < dataStartTime || needDurationUs > dataDurationUs) {
            return data;
        }
        float startPercent = (((float) (needStartTimeUs - dataStartTime)) * 1.0f) / ((float) dataDurationUs);
        float endPercent = startPercent + ((((float) needDurationUs) * 1.0f) / ((float) dataDurationUs));
        if (startPercent < StaticLayoutUtil.DefaultSpacingadd) {
            startPercent = StaticLayoutUtil.DefaultSpacingadd;
        }
        if (endPercent > 1.0f) {
            endPercent = 1.0f;
        }
        return Arrays.copyOfRange(data, (int) (((float) data.length) * startPercent), (int) (((float) data.length) * endPercent));
    }

    private void runOnUIThread(Runnable runnable) {
        if (this.mUiHandler != null) {
            this.mUiHandler.post(runnable);
        }
    }

    public void clearAllCurRunnables() {
        this.PreloadWaveDatasWorks.clear();
        this.LoadWavesWorks.clear();
    }

    public void release() {
    }

    public void destory() {
        if (this.mUiHandler != null) {
            this.mUiHandler.removeCallbacksAndMessages(null);
            this.mUiHandler = null;
        }
        this.isReleased = true;
    }
}