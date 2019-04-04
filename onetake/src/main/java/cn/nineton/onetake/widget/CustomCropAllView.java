package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class CustomCropAllView extends View {
    private float mHeight;
    private Paint mPaint;
    private float mWidth;
    private float unitHeight;
    private float unitWidth;

    public CustomCropAllView(Context context) {
        this(context, null);
    }

    public CustomCropAllView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCropAllView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth((float) DensityUtil.dip2px(1.0f));
        this.mPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
    }

    protected void onDraw(Canvas canvas) {
        this.mWidth = (float) getMeasuredWidth();
        this.mHeight = (float) getMeasuredHeight();
        this.unitWidth = this.mWidth / 6.0f;
        this.unitHeight = this.mHeight / 6.0f;
        for (int i = 0; i < 7; i++) {
            Canvas canvas2 = canvas;
            canvas2.drawLine(StaticLayoutUtil.DefaultSpacingadd, ((float) i) * this.unitHeight, this.mWidth, ((float) i) * this.unitHeight, this.mPaint);
            canvas.drawLine(StaticLayoutUtil.DefaultSpacingadd + (this.unitWidth * ((float) i)), StaticLayoutUtil.DefaultSpacingadd, this.unitWidth * ((float) i), this.mHeight, this.mPaint);
        }
    }
}