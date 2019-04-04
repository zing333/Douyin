package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class MirrorSeekBar extends View {
    private int CanScrollPercent;
    private boolean canLeftRight;
    private boolean canTopBottom;
    private int length;
    private int mHeight;
    private Paint mPaint;
    private int mWeight;
    private int move_x;
    private int move_y;
    private OnProgressChangedListener onProgressChangedListener;
    private int progress;
    private int showType;
    private int upRange;
    private float upx;
    private float upy;
    private int wide;

    public interface OnProgressChangedListener {
        void onProgressChanged(float f, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowType {
        public static final int SHOW_TYPE_VERTICAL_LEFT = 3;
        public static final int SHOW_TYPE_VERTICAL_RIGHT = 4;
        public static final int SHOW_TYPE__HORIZONTAL_BOTTOM = 2;
        public static final int SHOW_TYPE__HORIZONTAL_TOP = 1;
    }

    public MirrorSeekBar(Context context) {
        this(context, null);
    }

    public MirrorSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MirrorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.move_x = 0;
        this.move_y = 0;
        this.wide = DensityUtil.dip2px(8.0f);
        this.length = DensityUtil.dip2px(80.0f);
        this.upRange = DensityUtil.dip2px(40.0f);
        this.CanScrollPercent = 5;
        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth((float) DensityUtil.dip2px(1.0f));
    }

    public void setMirrorSize(int showType) {
        this.showType = showType;
        this.move_x = 0;
        this.move_y = 0;
        invalidate();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public void setX_Y(int move_x, int move_y) {
        this.move_x = move_x;
        this.move_y = move_y;
    }

    public void setProgress(int progress, int mWeight, int mHeight) {
        this.progress = progress;
        switch (this.showType) {
            case 1:
                this.move_y = (((mHeight * progress) / 100) - (mHeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                invalidate();
                return;
            case 2:
                this.move_y = (((mHeight * progress) / 100) - (mHeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                invalidate();
                return;
            case 3:
                this.move_x = (((mWeight * progress) / 100) - (mWeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                invalidate();
                return;
            case 4:
                this.move_x = (((mWeight * progress) / 100) - (mWeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                invalidate();
                return;
            default:
                return;
        }
    }

    protected void onDraw(Canvas canvas) {
        this.mPaint.setColor(getResources().getColor(R.color.colorWhite));
        this.mWeight = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();
        switch (this.showType) {
            case 1:
                this.move_y = (((this.mHeight * this.progress) / 100) - (this.mHeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                drawHorizontalLine(canvas);
                this.mPaint.setAlpha(51);
                canvas.drawRect(0.0f, (float) ((this.mHeight / 2) + this.move_y), (float) this.mWeight, (float) this.mHeight, this.mPaint);
                return;
            case 2:
                this.move_y = (((this.mHeight * this.progress) / 100) - (this.mHeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                drawHorizontalLine(canvas);
                this.mPaint.setAlpha(51);
                canvas.drawRect(0.0f, 0.0f, (float) this.mWeight, (float) ((this.mHeight / 2) + this.move_y), this.mPaint);
                return;
            case 3:
                this.move_x = (((this.mWeight * this.progress) / 100) - (this.mWeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                drawVerticalLine(canvas);
                this.mPaint.setAlpha(51);
                canvas.drawRect((float) ((this.mWeight / 2) + this.move_x), 0.0f, (float) this.mWeight, (float) this.mHeight, this.mPaint);
                return;
            case 4:
                this.move_x = (((this.mWeight * this.progress) / 100) - (this.mWeight / 2)) - (DensityUtil.dip2px(1.0f) / 2);
                drawVerticalLine(canvas);
                this.mPaint.setAlpha(51);
                canvas.drawRect(0.0f, 0.0f, (float) ((this.mWeight / 2) + this.move_x), (float) this.mHeight, this.mPaint);
                return;
            default:
                return;
        }
    }

    private void drawVerticalLine(Canvas canvas) {
        canvas.drawLine((float) ((this.mWeight / 2) + this.move_x), 0.0f, (float) ((this.mWeight / 2) + this.move_x), (float) ((this.mHeight - this.length) / 2), this.mPaint);
        canvas.drawRoundRect(new RectF((float) (((this.mWeight - this.wide) / 2) + this.move_x), (float) ((this.mHeight - this.length) / 2), (float) (((this.mWeight + this.wide) / 2) + this.move_x), (float) ((this.mHeight + this.length) / 2)), 8.0f, 8.0f, this.mPaint);
        canvas.drawLine((float) ((this.mWeight / 2) + this.move_x), (float) ((this.mHeight + this.length) / 2), (float) ((this.mWeight / 2) + this.move_x), (float) this.mHeight, this.mPaint);
    }

    private void drawHorizontalLine(Canvas canvas) {
        canvas.drawLine(0.0f, (float) ((this.mHeight / 2) + this.move_y), (float) ((this.mWeight - this.length) / 2), (float) ((this.mHeight / 2) + this.move_y), this.mPaint);
        canvas.drawRoundRect(new RectF((float) ((this.mWeight - this.length) / 2), (float) (((this.mHeight - this.wide) / 2) + this.move_y), (float) ((this.mWeight + this.length) / 2), (float) (((this.mHeight + this.wide) / 2) + this.move_y)), 8.0f, 8.0f, this.mPaint);
        canvas.drawLine((float) ((this.mWeight + this.length) / 2), (float) ((this.mHeight / 2) + this.move_y), (float) this.mWeight, (float) ((this.mHeight / 2) + this.move_y), this.mPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z = false;
        switch (event.getActionMasked()) {
            case 0:
                this.upx = event.getX();
                this.upy = event.getY();
                switch (this.showType) {
                    case 1:
                    case 2:
                        if (this.upy > ((float) (((this.mHeight / 2) + this.move_y) - (this.upRange / 2))) && this.upy < ((float) (((this.mHeight / 2) + this.move_y) + (this.upRange / 2)))) {
                            z = true;
                        }
                        this.canTopBottom = z;
                        break;
                    case 3:
                    case 4:
                        if (this.upx > ((float) (((this.mWeight / 2) + this.move_x) - (this.upRange / 2))) && this.upx < ((float) (((this.mWeight / 2) + this.move_x) + (this.upRange / 2)))) {
                            z = true;
                        }
                        this.canLeftRight = z;
                        break;
                }
                break;
            case 2:
                float percent;
                switch (this.showType) {
                    case 1:
                        if (this.canTopBottom) {
                            this.move_y = ((int) event.getY()) - (this.mHeight / 2);
                            if (this.move_y > 0 && event.getY() < ((float) ((this.mHeight / this.CanScrollPercent) * (this.CanScrollPercent - 1)))) {
                                percent = (event.getY() + ((float) (DensityUtil.dip2px(1.0f) / 2))) / ((float) this.mHeight);
                                this.progress = (int) (percent * 100.0f);
                            } else if (this.move_y <= 0) {
                                percent = 0.5f;
                                this.progress = 50;
                            } else {
                                percent = 0.8f;
                                this.progress = 80;
                            }
                            invalidate();
                            if (this.onProgressChangedListener != null) {
                                this.onProgressChangedListener.onProgressChanged(percent, 1);
                                break;
                            }
                        }
                        break;
                    case 2:
                        if (this.canTopBottom) {
                            this.move_y = ((int) event.getY()) - (this.mHeight / 2);
                            if (this.move_y < 0 && event.getY() > ((float) (this.mHeight / this.CanScrollPercent))) {
                                percent = (event.getY() + ((float) (DensityUtil.dip2px(1.0f) / 2))) / ((float) this.mHeight);
                                this.progress = (int) (percent * 100.0f);
                            } else if (this.move_y >= 0) {
                                percent = 0.5f;
                                this.progress = 50;
                            } else {
                                percent = 0.2f;
                                this.progress = 20;
                            }
                            invalidate();
                            if (this.onProgressChangedListener != null) {
                                this.onProgressChangedListener.onProgressChanged(percent, 2);
                                break;
                            }
                        }
                        break;
                    case 3:
                        if (this.canLeftRight) {
                            this.move_x = ((int) event.getX()) - (this.mWeight / 2);
                            if (this.move_x > 0 && event.getX() < ((float) ((this.mWeight / this.CanScrollPercent) * (this.CanScrollPercent - 1)))) {
                                percent = (event.getX() + ((float) (DensityUtil.dip2px(1.0f) / 2))) / ((float) this.mWeight);
                                this.progress = (int) (percent * 100.0f);
                            } else if (this.move_x <= 0) {
                                percent = 0.5f;
                                this.progress = 50;
                            } else {
                                percent = 0.8f;
                                this.progress = 80;
                            }
                            invalidate();
                            if (this.onProgressChangedListener != null) {
                                this.onProgressChangedListener.onProgressChanged(percent, 3);
                                break;
                            }
                        }
                        break;
                    case 4:
                        if (this.canLeftRight) {
                            this.move_x = ((int) event.getX()) - (this.mWeight / 2);
                            if (this.move_x < 0 && event.getX() > ((float) (this.mWeight / this.CanScrollPercent))) {
                                percent = (event.getX() + ((float) (DensityUtil.dip2px(1.0f) / 2))) / ((float) this.mWeight);
                                this.progress = (int) (percent * 100.0f);
                            } else if (this.move_x >= 0) {
                                percent = 0.5f;
                                this.progress = 50;
                            } else {
                                percent = 0.2f;
                                this.progress = 20;
                            }
                            invalidate();
                            if (this.onProgressChangedListener != null) {
                                this.onProgressChangedListener.onProgressChanged(percent, 4);
                                break;
                            }
                        }
                        break;
                }
                break;
        }
        return true;
    }

    public int getProgress() {
        return this.progress;
    }
}