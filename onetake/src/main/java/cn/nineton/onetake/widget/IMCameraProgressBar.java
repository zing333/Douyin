package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.LogUtil;

public class IMCameraProgressBar extends View {
    private static final int STROKE = 0;
    private static final int STROKE_FILL = 2;
    private static final int STROKE_TEXT = 1;
    private int defaultMultiplier;
    private boolean isFill;
    private int left;
    private Paint linePaint;
    private int mBackColor;
    private float mBorderWidth;
    private int mFontColor;
    private float mHalfBorder;
    private int mMode;
    private int mOutLineMultiple;
    private Paint mPaint;
    private int mTextColor;
    private float mTextSize;
    private int max;
    private RectF rectF;
    private int startPos;
    private Style style;
    private int textHalfSize;
    private int top;
    private float value;

    public IMCameraProgressBar(Context context) {
        super(context, null);
        this.value = StaticLayoutUtil.DefaultSpacingadd;
        this.startPos = -90;
        this.mOutLineMultiple = 1;
    }

    public IMCameraProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean z = true;
        this.value = StaticLayoutUtil.DefaultSpacingadd;
        this.startPos = -90;
        this.mOutLineMultiple = 1;
        this.mPaint = new Paint();
        this.linePaint = new Paint();
        this.left = 12;
        this.top = 12;
        this.rectF = new RectF();
        this.rectF.left = (float) this.left;
        this.rectF.top = (float) this.top;
        this.rectF.right = (float) ((18 - this.left) * 2);
        this.rectF.bottom = (float) ((18 - this.top) * 2);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.IMCameraProgressBar);
        this.mBackColor = mTypedArray.getColor(R.styleable.IMCameraProgressBar_backColor, Color.WHITE);
        this.mFontColor = mTypedArray.getColor(R.styleable.IMCameraProgressBar_frontColor, Color.BLACK);
        this.mTextColor = mTypedArray.getColor(R.styleable.IMCameraProgressBar_textColor, Color.BLACK);
        this.mTextSize = mTypedArray.getDimension(R.styleable.IMCameraProgressBar_textSizeIM, (float) getResources().getDimensionPixelSize(R.dimen.round_progress_text_size));
        this.defaultMultiplier = mTypedArray.getInt(R.styleable.IMCameraProgressBar_defaultMultiplier, 1);
        this.max = mTypedArray.getInt(R.styleable.IMCameraProgressBar_maxValue, 100);
        this.value = StaticLayoutUtil.DefaultSpacingadd;
        this.textHalfSize = (int) (this.mTextSize * 0.4f);
        this.mBorderWidth = mTypedArray.getDimension(R.styleable.IMCameraProgressBar_borderWidth, (float) getResources().getDimensionPixelSize(R.dimen.round_progress_border_width));
        this.mHalfBorder = this.mBorderWidth * 0.5f;
        this.mMode = mTypedArray.getInteger(R.styleable.IMCameraProgressBar_mode, 1);
        if (this.mMode != 2) {
            z = false;
        }
        this.isFill = z;
        this.style = this.isFill ? Style.FILL_AND_STROKE : Style.STROKE;
        mTypedArray.recycle();
    }

    public int getDefaultMultiplier() {
        return this.defaultMultiplier;
    }

    public void setDefaultMultiplier(int defaultMultiplier) {
        this.defaultMultiplier = defaultMultiplier;
        postInvalidate();
    }

    public void setBorderWidth(float mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        this.mHalfBorder = this.mBorderWidth * 0.5f;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getValue() {
        return this.value;
    }

    public boolean differentValueAndMax() {
        return this.value < ((float) this.max);
    }

    public synchronized void setValue(int newValue) {
        LogUtil.d("progressBar", "setValue value : " + newValue);
        this.value = (float) Math.min(this.max, Math.max(0, newValue));
        postInvalidate();
    }

    public synchronized void setFilterEffectValue(float newValue) {
        this.value = Math.min((float) this.max, Math.max(StaticLayoutUtil.DefaultSpacingadd, newValue));
        postInvalidate();
    }

    public synchronized void setValueByTime(float newValue) {
        this.value = Math.min((float) this.max, (1.0f * Math.max(StaticLayoutUtil.DefaultSpacingadd, newValue)) * ((float) this.max));
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center = ((float) getWidth()) * 0.5f;
        float radius = center - this.mHalfBorder;
        this.mPaint.setColor(this.mBackColor);
        this.mPaint.setStyle(this.style);
        this.mPaint.setStrokeWidth(this.mBorderWidth);
        this.mPaint.setAntiAlias(true);
        canvas.drawCircle(center, center, radius, this.mPaint);
        float percent = (this.value * 100.0f) / ((float) this.max);
        if (this.value > StaticLayoutUtil.DefaultSpacingadd) {
            this.mPaint.setStrokeWidth(this.mBorderWidth * ((float) this.mOutLineMultiple));
            this.mPaint.setColor(this.mFontColor);
            RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
            float angle = (360.0f * percent) / 100.0f;
            this.mPaint.setStyle(this.style);
            canvas.drawArc(oval, (float) this.startPos, ((float) this.defaultMultiplier) * angle, this.isFill, this.mPaint);
        }
    }

    public void setOutLineMultiple(int multiple) {
        this.mOutLineMultiple = multiple;
    }
}