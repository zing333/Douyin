package cn.nineton.onetake.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import cn.nineton.onetake.R;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

public class CircleProgressBar extends View {
    private int backgroundColor = Color.argb(76, 255, 255, 255);
    private Paint backgroundPaint;
    private float backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
    private int color = Color.argb(204, 255, 255, 255);
    private Paint foregroundPaint;
    private float progress = StaticLayoutUtil.DefaultSpacingadd;
    private RectF rectF;
    private int startAngle = -90;
    private float strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
        try {
            this.strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_progressbar_width, this.strokeWidth);
            this.backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_background_progressbar_width, this.backgroundStrokeWidth);
            this.color = typedArray.getInt(R.styleable.CircleProgressBar_cpb_progressbar_color, this.color);
            this.backgroundColor = typedArray.getInt(R.styleable.CircleProgressBar_cpb_background_progressbar_color, this.backgroundColor);
            this.backgroundPaint = new Paint(1);
            this.backgroundPaint.setColor(this.backgroundColor);
            this.backgroundPaint.setStyle(Style.STROKE);
            this.backgroundPaint.setStrokeWidth(this.backgroundStrokeWidth);
            this.foregroundPaint = new Paint(1);
            this.foregroundPaint.setColor(this.color);
            this.foregroundPaint.setStyle(Style.STROKE);
            this.foregroundPaint.setStrokeWidth(this.strokeWidth);
        } finally {
            typedArray.recycle();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(this.rectF, this.backgroundPaint);
        Canvas canvas2 = canvas;
        canvas2.drawArc(this.rectF, (float) this.startAngle, (360.0f * this.progress) / 100.0f, false, this.foregroundPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int min = Math.min(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        setMeasuredDimension(min, min);
        float highStroke = this.strokeWidth > this.backgroundStrokeWidth ? this.strokeWidth : this.backgroundStrokeWidth;
        this.rectF.set((highStroke / 2.0f) + StaticLayoutUtil.DefaultSpacingadd, (highStroke / 2.0f) + StaticLayoutUtil.DefaultSpacingadd, ((float) min) - (highStroke / 2.0f), ((float) min) - (highStroke / 2.0f));
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float progress) {
        if (progress > 100.0f) {
            progress = 100.0f;
        }
        this.progress = progress;
        invalidate();
    }

    public float getProgressBarWidth() {
        return this.strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return this.backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        this.backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();
        invalidate();
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1500);
    }

    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{progress});
        objectAnimator.setDuration((long) duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}