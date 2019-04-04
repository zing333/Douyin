package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

public class CircleCoverView extends View {
    private static final int CIRCLE_RADIO_DP = 5;
    private static final int IN_LINE_SIZE_PX = 1;
    private int circleOutAreaColor;
    private int circleRadio;
    private int defaultCircleOutPaintColor = R.color.colorLightGray;
    private Path inPath;
    private int leftOutColor;
    private Path leftOutPath;
    private int leftRadio;
    private float leftRightSpace = StaticLayoutUtil.DefaultSpacingadd;
    private float leftSpace = StaticLayoutUtil.DefaultSpacingadd;
    private Path linePath;
    private int lineSize = 1;
    private int mHeight;
    private Paint mLinePathPaint;
    private Paint mPaint;
    private RectF mRectf;
    private int mWidth;
    private boolean needDrawLine = true;
    private boolean needDrawOutAreaDiff;
    private boolean needDrawOutLine = false;
    private int outLineColor;
    private Path outPath;
    private int radio = 5;
    private int rightOutColor;
    private Path rightOutPath;
    private int rightRadio;
    private float rightSpace = StaticLayoutUtil.DefaultSpacingadd;

    public CircleCoverView(Context context) {
        super(context);
        init(context, null);
    }

    public void setVideoSelectActivtyConfig() {
        this.lineSize = 0;
        this.circleOutAreaColor = R.color.colorBlack242322;
        invalidate();
    }

    public void setLineSize(int lineSizePix) {
        this.lineSize = lineSizePix;
        invalidate();
    }

    public void setCircleOutAreaColor(int circleOutAreaColor) {
        this.circleOutAreaColor = circleOutAreaColor;
        invalidate();
    }

    public void setCircleOutColor(int circleOutColor) {
        this.circleOutAreaColor = circleOutColor;
        this.outLineColor = circleOutColor;
        this.mPaint.setColor(this.outLineColor);
        invalidate();
    }

    public void setNeedDrawOutLine(boolean needDrawOutLine) {
        this.needDrawOutLine = needDrawOutLine;
    }

    public void setNeedDrawOutLineWidthAndColor(float width, int color) {
        this.mLinePathPaint.setStrokeWidth(width);
        this.mLinePathPaint.setColor(color);
        postInvalidate();
    }

    public void setLeftRightSpace(float leftSpace, float rightSpace, int leftRadio, int rightRadio) {
        this.needDrawLine = false;
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.leftRadio = leftRadio;
        this.rightRadio = rightRadio;
        resetDrawPath();
        invalidate();
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (!isInEditMode()) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleCoverView);
            this.circleOutAreaColor = typedArray.getColor(R.styleable.CircleCoverView_circleOutColor, -1);
            this.outLineColor = typedArray.getColor(R.styleable.CircleCoverView_outLineColor, getResources().getColor(this.defaultCircleOutPaintColor));
            this.radio = (int) typedArray.getDimension(R.styleable.CircleCoverView_radius, (float) DensityUtil.dip2px(5.0f));
            this.lineSize = (int) typedArray.getDimension(R.styleable.CircleCoverView_lineSize, (float) DensityUtil.px2dip((float) this.lineSize));
            this.leftRightSpace = typedArray.getDimension(R.styleable.CircleCoverView_leftRightSpace, StaticLayoutUtil.DefaultSpacingadd);
            this.leftSpace = typedArray.getDimension(R.styleable.CircleCoverView_leftSpace, this.leftRightSpace);
            this.rightSpace = typedArray.getDimension(R.styleable.CircleCoverView_rightSpace, this.leftRightSpace);
            this.mPaint = new Paint(1);
            this.mPaint.setColor(this.outLineColor);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth((float) DensityUtil.dip2px((float) this.lineSize));
            this.mLinePathPaint = new Paint(1);
            this.mLinePathPaint.setColor(ContextCompat.getColor(getContext(), R.color.color30White));
            this.mLinePathPaint.setStyle(Style.STROKE);
            this.mLinePathPaint.setStrokeWidth(1.0f);
            this.outPath = new Path();
            this.inPath = new Path();
            this.linePath = new Path();
            this.circleRadio = this.radio;
            int i = this.circleRadio;
            this.rightRadio = i;
            this.leftRadio = i;
            typedArray.recycle();
            this.leftOutPath = new Path();
            this.rightOutPath = new Path();
        }
    }

    public CircleCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setLeftAndRightOutAreaColor(int leftColor, int rightColor, boolean needDrawOutAreaDiff) {
        this.leftOutColor = leftColor;
        this.rightOutColor = rightColor;
        this.needDrawOutAreaDiff = needDrawOutAreaDiff;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            canvas.save();
            canvas.clipPath(this.outPath);
            canvas.clipPath(this.inPath, Op.DIFFERENCE);
            if (this.needDrawOutAreaDiff) {
                canvas.save();
                canvas.clipPath(this.leftOutPath, Op.INTERSECT);
                canvas.drawColor(this.leftOutColor);
                canvas.restore();
                canvas.save();
                canvas.clipPath(this.rightOutPath, Op.INTERSECT);
                canvas.drawColor(this.rightOutColor);
                canvas.restore();
            } else {
                canvas.drawColor(this.circleOutAreaColor);
            }
            canvas.restore();
            if (this.needDrawLine) {
                canvas.drawPath(this.inPath, this.mPaint);
            }
            if (this.needDrawOutLine) {
                canvas.drawPath(this.linePath, this.mLinePathPaint);
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        if (!isInEditMode()) {
            resetDrawPath();
        }
    }

    private void resetDrawPath() {
        this.mRectf = new RectF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, (float) this.mWidth, (float) this.mHeight);
        this.outPath.reset();
        this.outPath.addRect(this.mRectf, Direction.CW);
        this.inPath.reset();
        this.linePath.reset();
        this.mRectf.left = this.leftSpace + StaticLayoutUtil.DefaultSpacingadd;
        this.mRectf.right = ((float) this.mWidth) - this.rightSpace;
        this.inPath.addRoundRect(this.mRectf, new float[]{(float) this.leftRadio, (float) this.leftRadio, (float) this.rightRadio, (float) this.rightRadio, (float) this.rightRadio, (float) this.rightRadio, (float) this.leftRadio, (float) this.leftRadio}, Direction.CW);
        RectF leftRectF = new RectF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, this.leftSpace + ((float) this.leftRadio), (float) this.mHeight);
        this.leftOutPath.reset();
        this.leftOutPath.addRect(leftRectF, Direction.CW);
        RectF rightRectF = new RectF((((float) this.mWidth) - this.rightSpace) - ((float) this.rightRadio), StaticLayoutUtil.DefaultSpacingadd, (float) this.mWidth, (float) this.mHeight);
        this.rightOutPath.reset();
        this.rightOutPath.addRect(rightRectF, Direction.CW);
        if (!this.needDrawOutLine) {
            return;
        }
        if (this.leftSpace != StaticLayoutUtil.DefaultSpacingadd && this.rightSpace == StaticLayoutUtil.DefaultSpacingadd) {
            this.linePath.moveTo(((float) this.mWidth) - this.rightSpace, StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.lineTo(this.leftSpace + ((float) this.leftRadio), StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.arcTo(new RectF(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, (this.leftSpace + StaticLayoutUtil.DefaultSpacingadd) + ((float) (this.leftRadio * 2)), (float) (this.leftRadio * 2)), 270.0f, -90.0f, false);
            this.linePath.lineTo(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, (float) (this.mHeight - this.leftRadio));
            this.linePath.arcTo(new RectF(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, (float) (this.mHeight - (this.leftRadio * 2)), (this.leftSpace + StaticLayoutUtil.DefaultSpacingadd) + ((float) (this.leftRadio * 2)), (float) this.mHeight), 180.0f, -90.0f, false);
            this.linePath.lineTo(((float) this.mWidth) - this.rightSpace, (float) this.mHeight);
        } else if (this.leftSpace == StaticLayoutUtil.DefaultSpacingadd && this.rightSpace != StaticLayoutUtil.DefaultSpacingadd) {
            this.linePath.moveTo(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.lineTo((((float) this.mWidth) - this.rightSpace) - ((float) this.rightRadio), StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.arcTo(new RectF((((float) this.mWidth) - this.rightSpace) - ((float) (this.rightRadio * 2)), StaticLayoutUtil.DefaultSpacingadd, ((float) this.mWidth) - this.rightSpace, (float) (this.rightRadio * 2)), 270.0f, 90.0f, false);
            this.linePath.lineTo(((float) this.mWidth) - this.rightSpace, (float) (this.mHeight - this.rightRadio));
            this.linePath.arcTo(new RectF((((float) this.mWidth) - this.rightSpace) - ((float) (this.rightRadio * 2)), (float) (this.mHeight - (this.rightRadio * 2)), ((float) this.mWidth) - this.rightSpace, (float) this.mHeight), StaticLayoutUtil.DefaultSpacingadd, 90.0f, false);
            this.linePath.lineTo(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, (float) this.mHeight);
        } else if (this.leftSpace != StaticLayoutUtil.DefaultSpacingadd && this.rightSpace != StaticLayoutUtil.DefaultSpacingadd) {
            this.linePath.moveTo((((float) this.mWidth) - this.rightSpace) - ((float) this.rightRadio), StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.lineTo(this.leftSpace + ((float) this.leftRadio), StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.arcTo(new RectF(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, (this.leftSpace + StaticLayoutUtil.DefaultSpacingadd) + ((float) (this.leftRadio * 2)), (float) (this.leftRadio * 2)), 270.0f, -90.0f, false);
            this.linePath.lineTo(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, (float) (this.mHeight - this.leftRadio));
            this.linePath.arcTo(new RectF(this.leftSpace + StaticLayoutUtil.DefaultSpacingadd, (float) (this.mHeight - (this.leftRadio * 2)), (this.leftSpace + StaticLayoutUtil.DefaultSpacingadd) + ((float) (this.leftRadio * 2)), (float) this.mHeight), 180.0f, -90.0f, false);
            this.linePath.lineTo((((float) this.mWidth) - this.rightSpace) - ((float) this.rightRadio), (float) this.mHeight);
            this.linePath.arcTo(new RectF((((float) this.mWidth) - this.rightSpace) - ((float) (this.rightRadio * 2)), (float) (this.mHeight - (this.rightRadio * 2)), ((float) this.mWidth) - this.rightSpace, (float) this.mHeight), 90.0f, -90.0f, false);
            this.linePath.lineTo(((float) this.mWidth) - this.rightSpace, (float) this.rightRadio);
            this.linePath.arcTo(new RectF((((float) this.mWidth) - this.rightSpace) - ((float) (this.rightRadio * 2)), StaticLayoutUtil.DefaultSpacingadd, ((float) this.mWidth) - this.rightSpace, (float) (this.rightRadio * 2)), StaticLayoutUtil.DefaultSpacingadd, -90.0f, false);
        } else if (this.leftSpace == StaticLayoutUtil.DefaultSpacingadd && this.rightSpace == StaticLayoutUtil.DefaultSpacingadd) {
            this.linePath.moveTo(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.lineTo((float) this.mWidth, StaticLayoutUtil.DefaultSpacingadd);
            this.linePath.moveTo((float) this.mWidth, (float) this.mHeight);
            this.linePath.lineTo(StaticLayoutUtil.DefaultSpacingadd, (float) this.mHeight);
        }
    }
}