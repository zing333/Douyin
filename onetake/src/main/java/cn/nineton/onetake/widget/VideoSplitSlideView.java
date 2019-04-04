package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class VideoSplitSlideView extends View {
    private final float LENGTH_3;
    private final float WIDTH_HALF_1;
    private int backgroundColor;
    private Paint mPaint;
    private float[] mPercentSplits;
    private int splitLineColor;
    private int splitLineWidth;

    public VideoSplitSlideView(@NonNull Context context) {
        this(context, null);
    }

    public VideoSplitSlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSplitSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.splitLineWidth = DensityUtil.dip2px(1.0f);
        this.splitLineColor = ContextCompat.getColor(getContext(), R.color.colorBlack);
        this.backgroundColor = ContextCompat.getColor(getContext(), R.color.color30White);
        this.WIDTH_HALF_1 = (float) DensityUtil.dip2px(0.5f);
        this.LENGTH_3 = (float) DensityUtil.dip2px(3.0f);
        setBackgroundColor(this.backgroundColor);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeWidth((float) this.splitLineWidth);
        this.mPaint.setColor(this.splitLineColor);
        this.mPaint.setAntiAlias(true);
    }

    public void setLongVideosModels(float[] percentSplits) {
        this.mPercentSplits = percentSplits;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.clearShadowLayer();
        if (this.mPercentSplits != null) {
            for (int i = 0; i < this.mPercentSplits.length; i++) {
                if (this.mPercentSplits[i] != StaticLayoutUtil.DefaultSpacingadd) {
                    float x = ((float) getWidth()) * this.mPercentSplits[i];
                    canvas.drawLine(x - this.WIDTH_HALF_1, StaticLayoutUtil.DefaultSpacingadd, x - this.WIDTH_HALF_1, this.LENGTH_3, this.mPaint);
                }
            }
        }
    }
}