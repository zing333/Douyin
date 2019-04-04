package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.GetWaveformDataRunnable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.WaveformCacheUtils;

public class AudioWaveView extends View {
    private static final String TAG = AudioWaveView.class.getSimpleName();
    public static final int WAVE_DRAW_TYPE_BOTTOM_SINGLE = 1;
    public static final int WAVE_DRAW_TYPE_CENTER_DOUBLE = 2;
    public static final int WAVE_DRAW_TYPE_VIDEO_LINE = 3;
    private Bitmap cacheBitmap;
    private float compressPercent;
    private int controllSize;
    private float darkPathPercent;
    private float[] drawData;
    private boolean hasLeft;
    private boolean hasRight;
    private Canvas mCanvas;
    private Paint mDarkPaint;
    private int mHeight;
    private Paint mPOIPaint;
    private Paint mPaint;
    private Path mPath;
    private int mWaveDrawType;
    private int mWidth;
    private int multiple;
    private boolean needDrawDarkPath;
    private float[] originData;
    private WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> waveFormWorkReference;

    public AudioWaveView(Context context) {
        this(context, null);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = DensityUtil.dip2px(44.0f);
        this.mHeight = DensityUtil.dip2px(44.0f);
        this.multiple = 6;
        this.mWaveDrawType = 1;
        this.controllSize = 2;
        this.compressPercent = 1.0f;
        init(context, attrs);
    }

    public void setWaveFormWorkReference(WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> waveFormWorkReference) {
        this.waveFormWorkReference = waveFormWorkReference;
    }

    public WeakReference<WaveformCacheUtils.GetWaveformDataRunnable> getWaveFormWorkReference() {
        return this.waveFormWorkReference;
    }

    private void init(Context context, AttributeSet attrs) {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
        this.mPath = new Path();
    }

    public void initDarkPaint(int color) {
        if (this.mDarkPaint == null) {
            this.mDarkPaint = new Paint(1);
            this.mDarkPaint.setColor(color);
        }
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
        if (drawData != null) {
            this.drawData = Arrays.copyOf(drawData, drawData.length);
            calculateWavePath();
        }
    }

    public void reDraw() {
        calculateWavePath();
    }

    public float[] getDrawData() {
        return this.drawData;
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

    public void setOriginData(float[] data) {
        setOriginData(data, false);
    }

    public void setOriginData(float[] data, boolean isAudio) {
        if (data != null && data.length > 0) {
            this.originData = Arrays.copyOf(data, data.length);
            if (this.mWidth > 0) {
                int tempWidth = this.mWidth;
                if (this.hasLeft) {
                    tempWidth += this.mWidth;
                }
                if (this.hasRight) {
                    tempWidth += this.mWidth;
                }
                setViewDrawData(getNeedData(this.originData, getCount(this.originData, tempWidth, this.controllSize)));
            }
        }
    }

    private float[] getNeedData(float[] data, int count) {
        int length = data.length;
        int size = length / count;
        if (length % count > 0) {
            size++;
        }
        float[] result = new float[size];
        for (int i = 0; i < size; i++) {
            result[i] = getAvgPerCount(data, i * count, count);
        }
        return getCenterAreaData(result);
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

    private float getDoubleWaveTopY(float value) {
        value *= (float) this.multiple;
        if (value > 1.0f) {
            value = 1.0f;
        }
        if (value <= StaticLayoutUtil.DefaultSpacingadd) {
            value = StaticLayoutUtil.DefaultSpacingadd;
        }
        return ((float) (this.mHeight / 2)) - (((((float) this.mHeight) * value) / 2.0f) * 0.7f);
    }

    private float getDoubleWaveBottomY(float value) {
        value *= (float) this.multiple;
        if (value > 1.0f) {
            value = 1.0f;
        }
        if (value <= StaticLayoutUtil.DefaultSpacingadd) {
            value = StaticLayoutUtil.DefaultSpacingadd;
        }
        return ((float) (this.mHeight / 2)) + (((((float) this.mHeight) * value) / 2.0f) * 0.7f);
    }

    public void setWaveDrawType(int type) {
        this.mWaveDrawType = type;
    }

    public void clearDrawCanvas() {
        this.originData = null;
        this.drawData = null;
        if (this.mCanvas != null) {
            this.mCanvas.drawColor(0, Mode.CLEAR);
            postInvalidate();
        }
    }

    public void setControllSize(int controllSize) {
        this.controllSize = controllSize;
    }

    private void calculateWavePath() {
        if (this.drawData != null && this.cacheBitmap != null && !this.cacheBitmap.isRecycled()) {
            int min;
            this.mCanvas.drawColor(0, Mode.CLEAR);
            this.mPath.reset();
            Path tempPath = new Path();
            Path darkPath = null;
            Path tempDarkPath = null;
            int length = this.drawData.length;
            if (length > this.mWidth / this.controllSize) {
                min = this.mWidth / this.controllSize;
            } else {
                min = length;
            }
            if (this.needDrawDarkPath) {
                darkPath = new Path();
                tempDarkPath = new Path();
            }
            int lineX = getLineX(min);
            float[] points = new float[(min * 4)];
            for (int i = 0; i < min; i++) {
                int x1 = (i * 4) + 0;
                points[x1] = (float) (this.controllSize * i);
                int x2 = (i * 4) + 2;
                points[x2] = (float) (this.controllSize * i);
                int y1 = (i * 4) + 1;
                int y2 = (i * 4) + 3;
                switch (this.mWaveDrawType) {
                    case 1:
                        points[y1] = (float) this.mHeight;
                        points[y2] = getSingleWaveY(this.drawData[i]);
                        if (i == 0) {
                            this.mPath.moveTo(points[x1], points[y1]);
                            this.mPath.lineTo(points[x2], points[y2]);
                        } else {
                            this.mPath.lineTo(points[x2], points[y2]);
                        }
                        if (i != min - 1) {
                            break;
                        }
                        this.mPath.lineTo((float) this.mWidth, points[y2]);
                        this.mPath.lineTo((float) this.mWidth, points[y1]);
                        this.mPath.close();
                        break;
                    case 2:
                        points[y1] = getDoubleWaveTopY(this.drawData[i]);
                        points[y2] = getDoubleWaveBottomY(this.drawData[i]);
                        if (this.needDrawDarkPath) {
                            if (darkPath != null) {
                                if (points[x1] > ((float) lineX)) {
                                    if (points[x1] == ((float) (this.controllSize + lineX))) {
                                        this.mPath.moveTo(points[x2], (float) (this.mHeight / 2));
                                        this.mPath.lineTo(points[x2], points[y2]);
                                        tempPath.moveTo(points[x1], (float) (this.mHeight / 2));
                                        tempPath.lineTo(points[x1], points[y1]);
                                    } else {
                                        this.mPath.lineTo(points[x2], points[y2]);
                                        tempPath.lineTo(points[x1], points[y1]);
                                    }
                                    if (i != min - 1) {
                                        break;
                                    }
                                    this.mPath.lineTo((float) this.mWidth, points[y2]);
                                    this.mPath.lineTo((float) this.mWidth, (float) (this.mHeight / 2));
                                    this.mPath.close();
                                    tempPath.lineTo((float) this.mWidth, points[y1]);
                                    tempPath.lineTo((float) this.mWidth, (float) (this.mHeight / 2));
                                    tempPath.close();
                                    break;
                                }
                                if (i == 0) {
                                    darkPath.moveTo(points[x2], (float) (this.mHeight / 2));
                                    darkPath.lineTo(points[x2], points[y2]);
                                    tempDarkPath.moveTo(points[x1], (float) (this.mHeight / 2));
                                    tempDarkPath.lineTo(points[x1], points[y1]);
                                } else {
                                    darkPath.lineTo(points[x2], points[y2]);
                                    tempDarkPath.lineTo(points[x1], points[y1]);
                                }
                                if (points[x1] != ((float) lineX)) {
                                    break;
                                }
                                darkPath.lineTo((float) lineX, points[y2]);
                                darkPath.lineTo((float) lineX, (float) (this.mHeight / 2));
                                darkPath.close();
                                tempDarkPath.lineTo((float) lineX, points[y1]);
                                tempDarkPath.lineTo((float) lineX, (float) (this.mHeight / 2));
                                tempDarkPath.close();
                                break;
                            }
                            break;
                        }
                        if (i == 0) {
                            this.mPath.moveTo(points[x2], (float) (this.mHeight / 2));
                            this.mPath.lineTo(points[x2], points[y2]);
                            tempPath.moveTo(points[x1], (float) (this.mHeight / 2));
                            tempPath.lineTo(points[x1], points[y1]);
                        } else {
                            this.mPath.lineTo(points[x2], points[y2]);
                            tempPath.lineTo(points[x1], points[y1]);
                        }
                        if (i != min - 1) {
                            break;
                        }
                        this.mPath.lineTo((float) this.mWidth, points[y2]);
                        this.mPath.lineTo((float) this.mWidth, (float) (this.mHeight / 2));
                        this.mPath.close();
                        tempPath.lineTo((float) this.mWidth, points[y1]);
                        tempPath.lineTo((float) this.mWidth, (float) (this.mHeight / 2));
                        tempPath.close();
                        break;
                    case 3:
                        points[y1] = (float) this.mHeight;
                        points[y2] = getSingleWaveY(this.drawData[i]);
                        if (i == 0) {
                            this.mPath.moveTo(points[x1], points[y1]);
                            this.mPath.lineTo(points[x2], points[y2]);
                        } else {
                            this.mPath.lineTo(points[x2], points[y2]);
                        }
                        if (i != min - 1) {
                            break;
                        }
                        this.mPath.lineTo((float) this.mWidth, points[y2]);
                        this.mPath.lineTo((float) this.mWidth, points[y1]);
                        this.mPath.close();
                        break;
                    default:
                        break;
                }
            }
            if (!this.needDrawDarkPath) {
                this.mCanvas.drawPath(this.mPath, this.mPaint);
                if (this.mWaveDrawType == 2) {
                    this.mCanvas.drawPath(tempPath, this.mPaint);
                }
            } else if (!(this.mDarkPaint == null || darkPath == null)) {
                this.mCanvas.drawPath(this.mPath, this.mPaint);
                this.mCanvas.drawPath(darkPath, this.mDarkPaint);
                if (this.mWaveDrawType == 2) {
                    this.mCanvas.drawPath(tempPath, this.mPaint);
                    this.mCanvas.drawPath(tempDarkPath, this.mDarkPaint);
                }
            }
            invalidate();
        }
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

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.cacheBitmap != null && !this.cacheBitmap.isRecycled()) {
            canvas.save();
            canvas.translate(StaticLayoutUtil.DefaultSpacingadd, (1.0f - this.compressPercent) * ((float) this.mHeight));
            canvas.scale(1.0f, this.compressPercent);
            canvas.drawBitmap(this.cacheBitmap, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, null);
            canvas.restore();
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
        if (w == oldw && h == oldh) {
            calculateWavePath();
            return;
        }
        if (this.cacheBitmap != null && this.cacheBitmap.isRecycled()) {
            this.cacheBitmap.recycle();
        }
        this.cacheBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        this.mCanvas = new Canvas(this.cacheBitmap);
        if (this.originData != null) {
            int tempWidth = this.mWidth;
            if (this.hasLeft) {
                tempWidth += this.mWidth;
            }
            if (this.hasRight) {
                tempWidth += this.mWidth;
            }
            setViewDrawData(getNeedData(this.originData, getCount(this.originData, tempWidth, this.controllSize)));
            return;
        }
        calculateWavePath();
    }

    public int getDoubleWaveLineHeight(float xPercent) {
        if (!TextUtil.isValidate(this.drawData)) {
            return 0;
        }
        int min;
        int length = this.drawData.length;
        if (length > this.mWidth / this.controllSize) {
            min = this.mWidth / this.controllSize;
        } else {
            min = length;
        }
        int index = (int) (((float) min) * xPercent);
        return (int) (getDoubleWaveBottomY(this.drawData[index]) - getDoubleWaveTopY(this.drawData[index]));
    }
}