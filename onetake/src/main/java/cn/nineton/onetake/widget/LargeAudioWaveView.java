package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.GetWaveformDataRunnable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.WaveformCacheUtils;

public class LargeAudioWaveView extends View {
    private static final String TAG = LargeAudioWaveView.class.getSimpleName();
    public static final int WAVE_DRAW_TYPE_BOTTOM_SINGLE = 1;
    private float allScaleY;
    private float compressPercent;
    private int controllSize;
    private float darkPathPercent;
    private boolean hasLeft;
    private boolean hasRight;
    ArrayList<DrawPathHolder> mDrawPathHolders;
    private int mHeight;
    private HashMap<LongVideosModel.AudioVolume, DrawPathHolder> mHolderHashMap;
    private Paint mPaint;
    private int mWaveDrawType;
    private ArrayList<WeakReference<Runnable>> mWeakReferences;
    private int mWidth;
    private int multiple;
    private boolean needDrawDarkPath;
    private WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> waveFormWorkReference;

    public static class DrawPathData {
        int drawWidth;
        float[] needData;
        int startX;

        public int getStartX() {
            return this.startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getDrawWidth() {
            return this.drawWidth;
        }

        public void setDrawWidth(int drawWidth) {
            this.drawWidth = drawWidth;
        }

        public float[] getNeedData() {
            return this.needData;
        }

        public void setNeedData(float[] needData) {
            this.needData = needData;
        }
    }

    public static class DrawPathHolder {
        private long endTime;
        DrawPathData mDrawPathData;
        Path mPath;
        ArrayList<DrawPathData> mPathDataArrayList;
        ArrayList<Path> mPaths;
        int pathArrayNeedSize;
        float scaleY = 1.0f;
        private long startTime;

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getPathArrayNeedSize() {
            return this.pathArrayNeedSize;
        }

        public void setPathArrayNeedSize(int pathArrayNeedSize) {
            this.pathArrayNeedSize = pathArrayNeedSize;
        }

        public DrawPathData getDrawPathData() {
            return this.mDrawPathData;
        }

        public void setDrawPathData(DrawPathData drawPathData) {
            this.mDrawPathData = drawPathData;
        }

        public ArrayList<DrawPathData> getPathDataArrayList() {
            return this.mPathDataArrayList;
        }

        public void setPathDataArrayList(ArrayList<DrawPathData> pathDataArrayList) {
            this.mPathDataArrayList = pathDataArrayList;
        }

        public ArrayList<Path> getPaths() {
            return this.mPaths;
        }

        public void setPaths(ArrayList<Path> paths) {
            this.mPaths = paths;
        }

        public Path getPath() {
            return this.mPath;
        }

        public void setPath(Path path) {
            this.mPath = path;
        }

        public float getScaleY() {
            return this.scaleY;
        }

        public void setScaleY(float scaleY) {
            this.scaleY = scaleY;
        }

        void drawPath(Canvas canvas, Paint paint, int viewHeight, float allScaleY) {
            float scale = this.scaleY;
            if (allScaleY != 1.0f) {
                scale = allScaleY;
            }
            if (this.mPath != null) {
                canvas.save();
                canvas.translate(StaticLayoutUtil.DefaultSpacingadd, (1.0f - scale) * ((float) viewHeight));
                canvas.scale(1.0f, scale);
                canvas.drawPath(this.mPath, paint);
                canvas.restore();
            } else if (this.mPaths != null) {
                Iterator it = this.mPaths.iterator();
                while (it.hasNext()) {
                    Path path = (Path) it.next();
                    canvas.save();
                    canvas.translate(StaticLayoutUtil.DefaultSpacingadd, (1.0f - scale) * ((float) viewHeight));
                    canvas.scale(1.0f, scale);
                    canvas.drawPath(path, paint);
                    canvas.restore();
                }
            }
        }
    }

    public LargeAudioWaveView(Context context) {
        this(context, null);
    }

    public LargeAudioWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeAudioWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = DensityUtil.dip2px(44.0f);
        this.mHeight = DensityUtil.dip2px(44.0f);
        this.multiple = 6;
        this.mWaveDrawType = 1;
        this.controllSize = 2;
        this.compressPercent = 1.0f;
        this.allScaleY = 1.0f;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
    }

    public WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> getWaveFormWorkReference() {
        return this.waveFormWorkReference;
    }

    public void setWaveFormWorkReference(WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> waveFormWorkReference) {
        this.waveFormWorkReference = waveFormWorkReference;
    }

    public void initDarkPaint(int color) {
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public void setNeedDrawDarkPath(boolean check) {
        setNeedDrawDarkPath(check, StaticLayoutUtil.DefaultSpacingadd);
    }

    public void setNeedDrawDarkPath(boolean check, float percent) {
        this.needDrawDarkPath = check;
        this.darkPathPercent = percent;
    }

    public void setViewDrawData(float[] drawData) {
    }

    private int getCount(float[] data, int width, int controllSize) {
        int count = 0;
        if (data != null) {
            count = (data.length / width) * controllSize;
        }
        if (count == 0) {
            return controllSize * 1;
        }
        return count;
    }

    public void updatePathByVolume(LongVideosModel.AudioVolume audioVolume, float volume) {
        if (this.mHolderHashMap != null) {
            DrawPathHolder drawPathHolder = (DrawPathHolder) this.mHolderHashMap.get(audioVolume);
            if (drawPathHolder != null) {
                drawPathHolder.setScaleY(volume);
            }
        }
    }

    public void updateOriginDataNeedSize(LongVideosModel.AudioVolume audioVolume, int size) {
        DrawPathHolder drawPathHolder = (DrawPathHolder) this.mHolderHashMap.get(audioVolume);
        if (drawPathHolder != null) {
            drawPathHolder.setPathArrayNeedSize(size);
        }
    }

    public void updateOriginDataAndPath(float[] data, LongVideosModel.AudioVolume audioVolume, boolean isList, long relateStart, long relateDuration, long curViewSumDuration) {
        if (data != null && data.length > 0) {
            if (this.mWidth > 0) {
                LogUtil.d(TAG, String.format("relateStart : %s , relateDuration : %s , curViewSumDuration : %s , isList : %s ", new Object[]{Long.valueOf(relateStart), Long.valueOf(relateDuration), Long.valueOf(curViewSumDuration), Boolean.valueOf(isList)}));
                DrawPathHolder drawPathHolder = (DrawPathHolder) this.mHolderHashMap.get(audioVolume);
                if (drawPathHolder != null) {
                    int drawWidth = ((int) (((((float) relateDuration) * 1.0f) / ((float) curViewSumDuration)) * ((float) this.mWidth))) + 1;
                    int startX = (int) (((((float) relateStart) * 1.0f) / ((float) curViewSumDuration)) * ((float) this.mWidth));
                    float[] needData = getNeedData(data, getCount(data, drawWidth, this.controllSize));
                    DrawPathData drawPathData = new DrawPathData();
                    drawPathData.setNeedData(needData);
                    drawPathData.setStartX(startX);
                    drawPathData.setDrawWidth(drawWidth);
                    Path pathFromData = createPathFromData(needData, startX, drawWidth);
                    if (isList) {
                        drawPathHolder.setPath(null);
                        drawPathHolder.setDrawPathData(null);
                        ArrayList<Path> paths = drawPathHolder.getPaths();
                        if (paths == null) {
                            paths = new ArrayList();
                        }
                        paths.add(pathFromData);
                        drawPathHolder.setPaths(paths);
                        ArrayList<DrawPathData> pathDataArrayList = drawPathHolder.getPathDataArrayList();
                        if (pathDataArrayList == null) {
                            pathDataArrayList = new ArrayList();
                        }
                        pathDataArrayList.add(drawPathData);
                        drawPathHolder.setPathDataArrayList(pathDataArrayList);
                    } else {
                        drawPathHolder.setPath(pathFromData);
                        drawPathHolder.setDrawPathData(drawPathData);
                    }
                    postInvalidate();
                    return;
                }
                return;
            }
            final float[] fArr = data;
            final LongVideosModel.AudioVolume audioVolume2 = audioVolume;
            final boolean z = isList;
            final long j = relateStart;
            final long j2 = relateDuration;
            final long j3 = curViewSumDuration;
            post(new Runnable() {
                public void run() {
                    LargeAudioWaveView.this.updateOriginDataAndPath(fArr, audioVolume2, z, j, j2, j3);
                }
            });
        }
    }

    private Path createPathFromData(float[] data, int startX, int drawWidth) {
        Path path = new Path();
        if (data != null) {
            int min;
            path.reset();
            int length = data.length;
            if (length > drawWidth / this.controllSize) {
                min = drawWidth / this.controllSize;
            } else {
                min = length;
            }
            float[] points = new float[(min * 4)];
            for (int i = 0; i < min; i++) {
                int x1 = (i * 4) + 0;
                points[x1] = (float) ((this.controllSize * i) + startX);
                int x2 = (i * 4) + 2;
                points[x2] = (float) ((this.controllSize * i) + startX);
                int y1 = (i * 4) + 1;
                int y2 = (i * 4) + 3;
                switch (this.mWaveDrawType) {
                    case 1:
                        points[y1] = (float) this.mHeight;
                        points[y2] = getSingleWaveY(data[i]);
                        if (i == 0) {
                            path.moveTo(points[x1], points[y1]);
                            path.lineTo(points[x2], points[y2]);
                        } else {
                            path.lineTo(points[x2], points[y2]);
                        }
                        if (i != min - 1) {
                            break;
                        }
                        path.lineTo((float) (startX + drawWidth), points[y2]);
                        path.lineTo((float) (startX + drawWidth), points[y1]);
                        path.close();
                        break;
                    default:
                        break;
                }
            }
        }
        return path;
    }

    private float[] getNeedData(float[] data, int count) {
        LogUtil.d(TAG, String.format("getNeedData start", new Object[0]));
        int length = data.length;
        int size = length / count;
        if (length % count > 0) {
            size++;
        }
        float[] result = new float[size];
        for (int i = 0; i < size; i++) {
            result[i] = getAvgPerCount(data, i * count, count);
        }
        LogUtil.d(TAG, String.format("getNeedData result.size : %s ", new Object[]{Integer.valueOf(result.length)}));
        return result;
    }

    public boolean isHasLeft() {
        return this.hasLeft;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    public boolean isHasRight() {
        return this.hasRight;
    }

    public void setHasRight(boolean hasRight) {
        this.hasRight = hasRight;
    }

    private float[] getCenterAreaData(float[] data) {
        if (data == null) {
            return null;
        }
        int length = data.length;
        int needSize = this.mWidth / this.controllSize;
        int noUseLength;
        if (this.hasLeft && this.hasRight) {
            noUseLength = (length - needSize) / 2;
            return Arrays.copyOfRange(data, noUseLength, noUseLength + needSize);
        } else if (this.hasLeft) {
            noUseLength = length - needSize;
            return Arrays.copyOfRange(data, noUseLength, noUseLength + needSize);
        } else if (this.hasRight) {
            return Arrays.copyOfRange(data, 0, needSize);
        } else {
            return data;
        }
    }

    private float getAvgPerCount(float[] data, int start, int count) {
        int end;
        int length = data.length;
        float avg = StaticLayoutUtil.DefaultSpacingadd;
        float sum = StaticLayoutUtil.DefaultSpacingadd;
        int last = start + count;
        if (last > length) {
            end = length;
        } else {
            end = last;
        }
        for (int i = start; i < end; i++) {
            if (data[i] > StaticLayoutUtil.DefaultSpacingadd) {
                sum += data[i];
                if (data[i] > avg) {
                    avg = data[i];
                }
            } else {
                sum -= data[i];
                if ((-data[i]) > avg) {
                    avg = -data[i];
                }
            }
        }
        return sum / ((float) count);
    }

    private float getSingleWaveY(float value) {
        value *= (float) this.multiple;
        if (value > 1.0f) {
            value = 1.0f;
        }
        if (value < StaticLayoutUtil.DefaultSpacingadd) {
            value = StaticLayoutUtil.DefaultSpacingadd;
        }
        return ((float) this.mHeight) - ((((float) this.mHeight) * value) / 2.0f);
    }

    public void setWaveDrawType(int type) {
        this.mWaveDrawType = type;
    }

    public void clearDrawCanvas() {
        if (this.mDrawPathHolders != null) {
            this.mDrawPathHolders.clear();
        }
        if (this.mHolderHashMap != null) {
            this.mHolderHashMap.clear();
        }
        postInvalidate();
    }

    private int getLineX(int min) {
        int lineX = (int) (this.darkPathPercent * ((float) this.mWidth));
        int remainder = lineX % this.controllSize;
        if (remainder == 0) {
            return lineX;
        }
        lineX += this.controllSize - remainder;
        if (this.controllSize + lineX > (min - 1) * this.controllSize) {
            return lineX - this.controllSize;
        }
        return lineX;
    }

    public void compressYLength(float percent) {
        this.compressPercent = percent;
        invalidate();
    }

    public void initDrawPathHolder(ArrayList<LongVideosModel.AudioVolume> audioVolumes) {
        if (audioVolumes == null) {
            if (this.mDrawPathHolders != null) {
                this.mDrawPathHolders.clear();
                this.mDrawPathHolders = null;
            }
            if (this.mHolderHashMap != null) {
                this.mHolderHashMap.clear();
                this.mHolderHashMap = null;
                return;
            }
            return;
        }
        int size = audioVolumes.size();
        this.mDrawPathHolders = new ArrayList();
        this.mHolderHashMap = new HashMap();
        for (int i = 0; i < size; i++) {
            LongVideosModel.AudioVolume audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
            DrawPathHolder drawPathHolder = new DrawPathHolder();
            drawPathHolder.setStartTime(audioVolume.getStartTime());
            drawPathHolder.setEndTime(audioVolume.getEndTime());
            drawPathHolder.setScaleY(audioVolume.getVolume());
            this.mDrawPathHolders.add(drawPathHolder);
            this.mHolderHashMap.put(audioVolume, drawPathHolder);
        }
    }

    public List<LongVideosModel.AudioVolume> checkNeedRefresh(List<LongVideosModel.AudioVolume> audioVolumes) {
        if (TextUtil.isNull(audioVolumes)) {
            return audioVolumes;
        }
        LongVideosModel.AudioVolume volume;
        HashMap<LongVideosModel.AudioVolume, DrawPathHolder> notNeedChangeMap = new HashMap();
        ArrayList<LongVideosModel.AudioVolume> notNeedChange = new ArrayList();
        ArrayList<LongVideosModel.AudioVolume> needRefresh = new ArrayList();
        for (LongVideosModel.AudioVolume volume2 : audioVolumes) {
            DrawPathHolder pathHolder = (DrawPathHolder) this.mHolderHashMap.get(volume2);
            if (pathHolder != null) {
                LongVideosModel.AudioVolume needCheck = null;
                for (LongVideosModel.AudioVolume audioVolume : this.mHolderHashMap.keySet()) {
                    if (audioVolume.equals(volume2)) {
                        needCheck = audioVolume;
                        break;
                    }
                }
                if (needCheck == null || needCheck.getUserChooseStart() != volume2.getUserChooseStart() || (pathHolder.getDrawPathData() == null && (pathHolder.getPathDataArrayList() == null || pathHolder.getPathDataArrayList().size() != pathHolder.getPathArrayNeedSize()))) {
                    needRefresh.add(volume2);
                } else {
                    notNeedChange.add(volume2);
                    notNeedChangeMap.put(volume2, pathHolder);
                    this.mHolderHashMap.remove(volume2);
                    this.mDrawPathHolders.remove(pathHolder);
                }
            } else {
                needRefresh.add(volume2);
            }
        }
        this.mHolderHashMap.clear();
        this.mDrawPathHolders.clear();
        this.mHolderHashMap.putAll(notNeedChangeMap);
        this.mDrawPathHolders.addAll(notNeedChangeMap.values());
        Iterator it = needRefresh.iterator();
        while (it.hasNext()) {
            LongVideosModel.AudioVolume volume2 = (LongVideosModel.AudioVolume) it.next();
            DrawPathHolder holder = new DrawPathHolder();
            holder.setStartTime(volume2.getStartTime());
            holder.setEndTime(volume2.getEndTime());
            holder.setScaleY(volume2.getVolume());
            this.mDrawPathHolders.add(holder);
            this.mHolderHashMap.put(volume2, holder);
        }
        LogUtil.d(TAG, String.format("needRefresh : %s ", new Object[]{needRefresh}));
        return needRefresh;
    }

    public void setAllScaleY(float allScaleY) {
        this.allScaleY = allScaleY;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawPathHolders != null) {
            Iterator it = this.mDrawPathHolders.iterator();
            while (it.hasNext()) {
                ((DrawPathHolder) it.next()).drawPath(canvas, this.mPaint, this.mHeight, this.allScaleY);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getWidth();
        int height = getHeight();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        LogUtil.d(TAG, String.format("onSizeChanged width : %s , height : %s ", new Object[]{Integer.valueOf(w), Integer.valueOf(h)}));
        if (h != oldh) {
            reDrawWaveData();
        }
    }

    private void reDrawWaveData() {
        if (this.mDrawPathHolders != null) {
            Iterator it = this.mDrawPathHolders.iterator();
            while (it.hasNext()) {
                DrawPathHolder holder = (DrawPathHolder) it.next();
                DrawPathData drawPathData = holder.getDrawPathData();
                if (drawPathData != null) {
                    holder.setPath(createPathFromData(drawPathData.getNeedData(), drawPathData.getStartX(), drawPathData.getDrawWidth()));
                } else {
                    ArrayList<Path> paths = holder.getPaths();
                    if (paths == null) {
                        paths = new ArrayList();
                    }
                    paths.clear();
                    ArrayList<DrawPathData> pathDataArrayList = holder.getPathDataArrayList();
                    if (pathDataArrayList != null) {
                        Iterator it2 = pathDataArrayList.iterator();
                        while (it2.hasNext()) {
                            DrawPathData pathData = (DrawPathData) it2.next();
                            paths.add(createPathFromData(pathData.getNeedData(), pathData.getStartX(), pathData.getDrawWidth()));
                        }
                        holder.setPaths(paths);
                    }
                }
            }
        }
    }

    public void putIntoReferences(Runnable runnable) {
        if (this.mWeakReferences == null) {
            this.mWeakReferences = new ArrayList();
        }
        this.mWeakReferences.add(new WeakReference(runnable));
    }

    public void cancelAllRunnable() {
        if (this.mWeakReferences != null) {
            Iterator it = this.mWeakReferences.iterator();
            while (it.hasNext()) {
                Runnable runnable = (Runnable) ((WeakReference) it.next()).get();
                if (runnable != null && (runnable instanceof WaveformCacheUtils.GetWaveformDataRunnable)) {
                    ((WaveformCacheUtils.GetWaveformDataRunnable) runnable).setCanceled(true);
                }
            }
        }
    }
}