package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.nineton.onetake.util.DensityUtil;

public class CustomCropView extends View {
    private int allColor;
    private int black;
    private float bottomY;
    private float cropBottom;
    float cropHeight;
    private float cropLeft;
    private float cropRight;
    private float cropTop;
    float cropWidth;
    private float diffBottom;
    private float diffLeft;
    private float diffRight;
    private float diffTop;
    private float initHeight;
    private float initWidth;
    boolean isCenter;
    boolean isLeftBottom;
    boolean isLeftTop;
    boolean isRightBottom;
    boolean isRightTop;
    private float leftX;
    private float mHeight;
    private float mLastX;
    private float mLastY;
    private Paint mPaint;
    private float mWidth;
    private float marginLeft;
    private float marginTop;
    private float percentHeight;
    private float percentWidth;
    private float percentX;
    private float percentY;
    private float radius;
    private float range;
    private float rightX;
    private float topY;
    private float touchBottom;
    private float touchTop;
    private float type;
    float unitWidth;
    float x;
    float y;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CropShowType {
        public static final int SHOW_TYPE_CROP_16_9 = 1;
        public static final int SHOW_TYPE_CROP_1_1 = 4;
        public static final int SHOW_TYPE_CROP_2_3 = 6;
        public static final int SHOW_TYPE_CROP_3_2 = 2;
        public static final int SHOW_TYPE_CROP_3_4 = 5;
        public static final int SHOW_TYPE_CROP_4_3 = 3;
        public static final int SHOW_TYPE_CROP_9_16 = 7;
    }

    public CustomCropView(Context context) {
        this(context, null);
    }

    public CustomCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.x = 0.0f;
        this.y = 0.0f;
        this.isLeftTop = false;
        this.isLeftBottom = false;
        this.isRightTop = false;
        this.isRightBottom = false;
        this.isCenter = false;
        initPaint();
        setLayerType(1, null);
        this.allColor = ContextCompat.getColor(context, R.color.colorWhite);
        this.black = ContextCompat.getColor(context, R.color.colorBlack);
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setTextAlign(Align.CENTER);
    }

    protected void onDraw(Canvas canvas) {
        this.unitWidth = (float) DensityUtil.dip2px(1.0f);
        this.radius = (float) DensityUtil.dip2px(4.0f);
        this.range = (float) DensityUtil.dip2px(22.0f);
        this.mPaint.setColor(this.allColor);
        drawCircle(canvas);
        drawAroundLine(canvas);
        drawCenterLine(canvas);
    }

    public void setType(int mtype) {
        switch (mtype) {
            case 1:
                this.type = 1.7777778f;
                break;
            case 2:
                this.type = 1.5f;
                break;
            case 3:
                this.type = 1.3333334f;
                break;
            case 4:
                this.type = 1.0f;
                break;
            case 5:
                this.type = 0.75f;
                break;
            case 6:
                this.type = 0.6666667f;
                break;
            case 7:
                this.type = 0.5625f;
                break;
        }
        this.diffLeft = 0.0f;
        this.diffTop = 0.0f;
        if (this.mWidth / this.mHeight < this.type) {
            this.cropWidth = this.mWidth;
            this.cropHeight = this.mWidth / this.type;
        } else {
            this.cropHeight = this.mHeight;
            this.cropWidth = this.mHeight * this.type;
        }
        this.cropLeft = ((this.mWidth / 2.0f) - (this.cropWidth / 2.0f)) + this.marginLeft;
        this.cropRight = ((this.mWidth / 2.0f) + (this.cropWidth / 2.0f)) + this.marginLeft;
        this.cropTop = ((this.mHeight / 2.0f) - (this.cropHeight / 2.0f)) + this.marginTop;
        this.cropBottom = ((this.mHeight / 2.0f) + (this.cropHeight / 2.0f)) + this.marginTop;
        this.leftX = this.cropLeft;
        this.rightX = this.cropRight;
        this.topY = this.cropTop;
        this.bottomY = this.cropBottom;
        invalidate();
    }

    public void setWidthAndHeight(float width, float height, float topMargin, float leftMargin, float touchtop, float touchbottom) {
        this.mHeight = height;
        this.mWidth = width;
        this.marginTop = topMargin;
        this.marginLeft = leftMargin;
        this.touchTop = touchtop;
        this.touchBottom = touchbottom;
        invalidate();
    }

    public void setLeftWidthHeight(float left, float top, float width, float height) {
        this.leftX = left;
        this.rightX = this.leftX + width;
        this.topY = top;
        this.bottomY = top + height;
        invalidate();
    }

    public void setLeftWidthHeightPercent(float leftPercent, float topPercent, float widthPercent, float heightPercent) {
        this.leftX = this.marginLeft + (this.mWidth * leftPercent);
        this.rightX = this.leftX + (this.mWidth * widthPercent);
        this.topY = this.marginTop + (this.mHeight * topPercent);
        this.bottomY = this.topY + (this.mHeight * heightPercent);
        invalidate();
    }

    private void drawCenterLine(Canvas canvas) {
        this.mPaint.setAlpha(255);
        float unitWidth = (this.rightX - this.leftX) / 3.0f;
        float unitHeight = (this.bottomY - this.topY) / 3.0f;
        this.mPaint.setStrokeWidth((float) DensityUtil.dip2px(1.0f));
        this.mPaint.setShadowLayer(0.0f, 0.0f, 0.0f, this.allColor);
        canvas.drawLine(this.leftX, this.topY + unitHeight, this.rightX, this.topY + unitHeight, this.mPaint);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.leftX, (unitHeight * 2.0f) + this.topY, this.rightX, (unitHeight * 2.0f) + this.topY, this.mPaint);
        canvas.drawLine(this.leftX + unitWidth, this.topY, this.leftX + unitWidth, this.bottomY, this.mPaint);
        canvas2 = canvas;
        canvas2.drawLine((unitWidth * 2.0f) + this.leftX, this.topY, (unitWidth * 2.0f) + this.leftX, this.bottomY, this.mPaint);
    }

    private void drawAroundLine(Canvas canvas) {
        this.mPaint.setShadowLayer((float) DensityUtil.dip2px(3.0f), 0.0f, 0.0f, this.black);
        float strokeWidth = (float) DensityUtil.dip2px(2.0f);
        this.mPaint.setColor(this.allColor);
        this.mPaint.setStrokeWidth(strokeWidth);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.radius + this.leftX, this.topY, this.rightX - this.radius, this.topY, this.mPaint);
        canvas2 = canvas;
        canvas2.drawLine(this.leftX, this.radius + this.topY, this.leftX, this.bottomY - this.radius, this.mPaint);
        canvas2 = canvas;
        canvas2.drawLine(this.radius + this.leftX, this.bottomY, this.rightX - this.radius, this.bottomY, this.mPaint);
        canvas2 = canvas;
        canvas2.drawLine(this.rightX, this.bottomY - this.radius, this.rightX, this.radius + this.topY, this.mPaint);
    }

    private void drawCircle(Canvas canvas) {
        this.mPaint.setShadowLayer((float) DensityUtil.dip2px(3.0f), 0.0f, 0.0f, this.black);
        this.mPaint.setColor(this.allColor);
        canvas.drawCircle(this.leftX, this.topY, this.radius, this.mPaint);
        canvas.drawCircle(this.leftX, this.bottomY, this.radius, this.mPaint);
        canvas.drawCircle(this.rightX, this.topY, this.radius, this.mPaint);
        canvas.drawCircle(this.rightX, this.bottomY, this.radius, this.mPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                this.isLeftTop = isLeftTop(event);
                this.isLeftBottom = isLeftBottom(event);
                this.isRightTop = isRightTop(event);
                this.isRightBottom = isRightBottom(event);
                this.isCenter = isCenter(event);
                this.mLastX = event.getX();
                this.mLastY = event.getY();
                if (event.getY() < this.touchTop && !this.isLeftTop && !this.isRightTop) {
                    return false;
                }
                if (event.getY() > ((float) (getMeasuredHeight() - DensityUtil.dip2px(205.0f))) && !this.isLeftBottom && !this.isRightBottom) {
                    return false;
                }
                if (!(this.isLeftTop || this.isLeftBottom || this.isRightTop || this.isRightBottom || this.isCenter)) {
                    return false;
                }
                break;
            case 1:
                this.cropLeft = this.leftX;
                this.cropTop = this.topY;
                this.cropRight = this.rightX;
                this.cropBottom = this.bottomY;
                break;
            case 2:
                if (!this.isLeftTop) {
                    if (!this.isLeftBottom) {
                        if (!this.isRightTop) {
                            if (!this.isRightBottom) {
                                if (this.isCenter) {
                                    this.diffTop = event.getY() - this.mLastY;
                                    this.diffLeft = event.getX() - this.mLastX;
                                    this.diffRight = event.getX() - this.mLastX;
                                    this.diffBottom = event.getY() - this.mLastY;
                                    this.leftX = this.cropLeft + this.diffLeft;
                                    this.rightX = this.cropRight + this.diffRight;
                                    this.topY = this.cropTop + this.diffTop;
                                    this.bottomY = this.cropBottom + this.diffBottom;
                                    if (this.leftX < this.marginLeft) {
                                        this.leftX = this.marginLeft;
                                        this.rightX = this.leftX + (this.cropRight - this.cropLeft);
                                    }
                                    if (this.topY < this.marginTop) {
                                        this.topY = this.marginTop;
                                        this.bottomY = this.topY + (this.cropBottom - this.cropTop);
                                    }
                                    if (this.rightX > this.marginLeft + this.mWidth) {
                                        this.rightX = this.marginLeft + this.mWidth;
                                        this.leftX = this.rightX - (this.cropRight - this.cropLeft);
                                    }
                                    if (this.bottomY > this.marginTop + this.mHeight) {
                                        this.bottomY = this.marginTop + this.mHeight;
                                        this.topY = this.bottomY - (this.cropBottom - this.cropTop);
                                    }
                                    invalidate();
                                    break;
                                }
                            }
                            this.diffTop = 0.0f;
                            this.diffLeft = 0.0f;
                            this.diffRight = event.getX() - this.mLastX;
                            this.diffBottom = this.diffRight / this.type;
                            if (((this.leftX >= this.marginLeft && this.rightX <= this.marginLeft + this.mWidth && this.topY >= this.marginTop && this.bottomY <= this.marginTop + this.mHeight) || this.diffTop > 0.0f || this.diffRight < 0.0f) && ((this.rightX - this.leftX >= this.range * 4.0f && this.bottomY - this.topY > this.range * 4.0f) || this.diffTop < 0.0f || this.diffRight > 0.0f)) {
                                this.leftX = this.cropLeft + this.diffLeft;
                                this.rightX = this.cropRight + this.diffRight;
                                this.topY = this.cropTop + this.diffTop;
                                this.bottomY = this.cropBottom + this.diffBottom;
                                if (this.rightX > this.marginLeft + this.mWidth && this.diffRight > 0.0f) {
                                    this.rightX = this.marginLeft + this.mWidth;
                                    this.bottomY = this.topY + ((this.rightX - this.leftX) / this.type);
                                }
                                if (this.bottomY > this.marginTop + this.mHeight && this.diffBottom < 0.0f) {
                                    this.bottomY = this.marginTop + this.mHeight;
                                    this.rightX = this.leftX + ((this.bottomY - this.topY) * this.type);
                                }
                                invalidate();
                                break;
                            }
                        }
                        this.diffBottom = 0.0f;
                        this.diffLeft = 0.0f;
                        this.diffRight = event.getX() - this.mLastX;
                        this.diffTop = -(this.diffRight / this.type);
                        if (((this.leftX >= this.marginLeft && this.rightX <= this.marginLeft + this.mWidth && this.topY >= this.marginTop && this.bottomY <= this.marginTop + this.mHeight) || this.diffTop > 0.0f || this.diffRight < 0.0f) && ((this.rightX - this.leftX >= this.range * 4.0f && this.bottomY - this.topY > this.range * 4.0f) || this.diffTop < 0.0f || this.diffRight > 0.0f)) {
                            this.leftX = this.cropLeft + this.diffLeft;
                            this.rightX = this.cropRight + this.diffRight;
                            this.topY = this.cropTop + this.diffTop;
                            this.bottomY = this.cropBottom + this.diffBottom;
                            if (this.rightX > this.marginLeft + this.mWidth && this.diffRight > 0.0f) {
                                this.rightX = this.marginLeft + this.mWidth;
                                this.topY = this.bottomY - ((this.rightX - this.leftX) / this.type);
                            }
                            if (this.topY < this.marginTop && this.diffTop < 0.0f) {
                                this.topY = this.marginTop;
                                this.rightX = this.leftX + ((this.bottomY - this.topY) * this.type);
                            }
                            invalidate();
                            break;
                        }
                    }
                    this.diffRight = 0.0f;
                    this.diffTop = 0.0f;
                    this.diffLeft = event.getX() - this.mLastX;
                    this.diffBottom = -(this.diffLeft / this.type);
                    if (((this.leftX >= this.marginLeft && this.rightX <= this.marginLeft + this.mWidth && this.topY >= this.marginTop && this.bottomY <= this.marginTop + this.mHeight) || this.diffLeft > 0.0f || this.diffBottom < 0.0f) && ((this.rightX - this.leftX >= this.range * 4.0f && this.bottomY - this.topY > this.range * 4.0f) || this.diffLeft < 0.0f || this.diffBottom > 0.0f)) {
                        this.leftX = this.cropLeft + this.diffLeft;
                        this.rightX = this.cropRight + this.diffRight;
                        this.topY = this.cropTop + this.diffTop;
                        this.bottomY = this.cropBottom + this.diffBottom;
                        if (this.leftX < this.marginLeft && this.diffLeft < 0.0f) {
                            this.leftX = this.marginLeft;
                            this.bottomY = this.topY + ((this.rightX - this.marginLeft) / this.type);
                        }
                        if (this.bottomY > this.marginTop + this.mHeight && this.diffBottom > 0.0f) {
                            this.bottomY = this.marginTop + this.mHeight;
                            this.leftX = this.rightX - ((this.bottomY - this.topY) * this.type);
                        }
                        invalidate();
                        break;
                    }
                }
                this.diffRight = 0.0f;
                this.diffBottom = 0.0f;
                this.diffLeft = event.getX() - this.mLastX;
                this.diffTop = this.diffLeft / this.type;
                if (((this.leftX >= this.marginLeft && this.rightX <= this.marginLeft + this.mWidth && this.topY >= this.marginTop && this.bottomY <= this.marginTop + this.mHeight) || this.diffLeft > 0.0f || this.diffTop > 0.0f) && ((this.rightX - this.leftX >= this.range * 4.0f && this.bottomY - this.topY > this.range * 4.0f) || this.diffLeft < 0.0f || this.diffTop < 0.0f)) {
                    this.leftX = this.cropLeft + this.diffLeft;
                    this.rightX = this.cropRight + this.diffRight;
                    this.topY = this.cropTop + this.diffTop;
                    this.bottomY = this.cropBottom + this.diffBottom;
                    if (this.leftX < this.marginLeft && this.diffLeft < 0.0f) {
                        this.leftX = this.marginLeft;
                        this.topY = this.bottomY - ((this.rightX - this.marginLeft) / this.type);
                    }
                    if (this.topY < this.marginTop && this.diffTop < 0.0f) {
                        this.topY = this.marginTop;
                        this.leftX = this.rightX - ((this.bottomY - this.marginTop) * this.type);
                    }
                    invalidate();
                    break;
                }
                break;
        }
        return true;
    }

    public boolean isLeftTop(MotionEvent event) {
        return event.getX() > this.leftX - this.range && event.getX() < this.leftX + this.range && event.getY() > this.topY - this.range && event.getY() < this.topY + this.range;
    }

    public boolean isLeftBottom(MotionEvent event) {
        return event.getX() > this.leftX - this.range && event.getX() < this.leftX + this.range && event.getY() > this.bottomY - this.range && event.getY() < this.bottomY + this.range;
    }

    public boolean isRightTop(MotionEvent event) {
        return event.getX() > this.rightX - this.range && event.getX() < this.rightX + this.range && event.getY() > this.topY - this.range && event.getY() < this.topY + this.range;
    }

    public boolean isRightBottom(MotionEvent event) {
        return event.getX() > this.rightX - this.range && event.getX() < this.rightX + this.range && event.getY() > this.bottomY - this.range && event.getY() < this.bottomY + this.range;
    }

    public boolean isCenter(MotionEvent event) {
        return (event.getX() < this.rightX - this.range && event.getX() > this.leftX + this.range && event.getY() > this.topY && event.getY() < this.bottomY) || (event.getY() > this.topY + this.range && event.getY() < this.bottomY - this.range && event.getX() > this.leftX && event.getX() < this.rightX);
    }

    public float[] getCropDetail() {
        if (getVisibility() == GONE) {
            return new float[]{0.0f, 0.0f, this.mWidth, this.mHeight, 0.0f, 0.0f, 1.0f, 1.0f};
        }
        return new float[]{this.leftX, this.topY, this.rightX - this.leftX, this.bottomY - this.topY, this.percentX, this.percentY, this.percentWidth, this.percentHeight};
    }

    public void refreshPercentValue() {
        this.percentX = (this.leftX - this.marginLeft) / this.mWidth;
        this.percentY = (this.topY - this.marginTop) / this.mHeight;
        this.percentWidth = (this.rightX - this.leftX) / this.mWidth;
        this.percentHeight = (this.bottomY - this.topY) / this.mHeight;
    }

    public float[] getCropPercent() {
        if (getVisibility() == GONE) {
            return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        }
        return new float[]{this.percentX, this.percentY, this.percentWidth, this.percentHeight};
    }
}