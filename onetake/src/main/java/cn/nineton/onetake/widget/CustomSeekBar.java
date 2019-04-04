package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigDecimal;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class CustomSeekBar extends View {
    private static final float DAMP_RANGE = 0.05f;
    private float DAMP_FACTOR;
    private float XCenter;
    private boolean canDrawText;
    private float centerLeft;
    private int disableColor;
    private boolean drawDampLine;
    private int enableColor;
    private float height;
    private float left;
    private String mDrawStr;
    private GestureDetector mGestureDetector;
    private Paint mLinePaint;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private boolean needDamp01;
    private boolean needGesture;
    private OnCustomProgressProStateChangeListener onCustomProgressProStateChangeListener;
    private OnCustomProgressChangeListener onProgressChangeListener;
    private float pro;
    private float radius;
    private float right;
    private float textSize;
    private float textTopOffset;
    private boolean toMove;

    public interface OnCustomProgressChangeListener {
        void onProgressChanged(float f);

        void onProgressUp(float f);

        void onVolumeTouchDown();

        void onVolumeTouchUp(float f);
    }

    public interface OnCustomProgressProStateChangeListener {
        void onPro0(boolean z);

        void onPro100(boolean z);
    }

    private class SeekGestureDetector implements OnGestureListener {
        private SeekGestureDetector() {
        }

        public boolean onDown(MotionEvent e) {
            if (CustomSeekBar.this.needGesture) {
                CustomSeekBar.this.centerLeft = CustomSeekBar.this.XCenter;
                if (CustomSeekBar.this.onProgressChangeListener != null) {
                    CustomSeekBar.this.onProgressChangeListener.onVolumeTouchDown();
                }
            }
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            boolean z = true;
            if (CustomSeekBar.this.needGesture) {
                CustomSeekBar.this.XCenter = e.getX();
                CustomSeekBar.this.moveToX(CustomSeekBar.this.XCenter);
                if (CustomSeekBar.this.onProgressChangeListener != null) {
                    CustomSeekBar.this.onProgressChangeListener.onProgressChanged(CustomSeekBar.this.pro);
                    CustomSeekBar.this.onProgressChangeListener.onProgressUp(CustomSeekBar.this.pro);
                }
                if (CustomSeekBar.this.onCustomProgressProStateChangeListener != null) {
                    CustomSeekBar.this.onCustomProgressProStateChangeListener.onPro0(CustomSeekBar.this.pro > StaticLayoutUtil.DefaultSpacingadd);
                    OnCustomProgressProStateChangeListener access$700 = CustomSeekBar.this.onCustomProgressProStateChangeListener;
                    if (CustomSeekBar.this.pro != 100.0f) {
                        z = false;
                    }
                    access$700.onPro100(z);
                }
                CustomSeekBar.this.invalidate();
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean z = true;
            if (CustomSeekBar.this.needGesture) {
                CustomSeekBar.this.XCenter = CustomSeekBar.this.centerLeft + (e2.getX() - e1.getX());
                CustomSeekBar.this.moveToX(CustomSeekBar.this.XCenter);
                if (CustomSeekBar.this.pro > 1.0f) {
                    CustomSeekBar.this.pro = 1.0f;
                }
                if (CustomSeekBar.this.pro < StaticLayoutUtil.DefaultSpacingadd) {
                    CustomSeekBar.this.pro = StaticLayoutUtil.DefaultSpacingadd;
                }
                CustomSeekBar.this.invalidate();
                if (CustomSeekBar.this.onProgressChangeListener != null) {
                    CustomSeekBar.this.onProgressChangeListener.onProgressChanged(CustomSeekBar.this.pro);
                }
                if (CustomSeekBar.this.onCustomProgressProStateChangeListener != null) {
                    CustomSeekBar.this.onCustomProgressProStateChangeListener.onPro0(CustomSeekBar.this.pro > StaticLayoutUtil.DefaultSpacingadd);
                    OnCustomProgressProStateChangeListener access$700 = CustomSeekBar.this.onCustomProgressProStateChangeListener;
                    if (CustomSeekBar.this.pro != 100.0f) {
                        z = false;
                    }
                    access$700.onPro100(z);
                }
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.needGesture = true;
        this.toMove = false;
        this.canDrawText = true;
        this.DAMP_FACTOR = 0.7f;
        this.drawDampLine = false;
        this.needDamp01 = false;
        init(context, attrs);
    }

    public void setDAMP_FACTOR(float DAMP_FACTOR) {
        this.DAMP_FACTOR = DAMP_FACTOR;
    }

    public void setNeedDamp01(boolean needDamp01) {
        this.needDamp01 = needDamp01;
    }

    public void setDrawDampLine(boolean drawDampLine) {
        this.drawDampLine = drawDampLine;
    }

    public void setCanDrawText(boolean canDrawText) {
        this.canDrawText = canDrawText;
    }

    private void init(Context context, AttributeSet attrs) {
        this.enableColor = ContextCompat.getColor(context, R.color.colorWhite);
        this.disableColor = ContextCompat.getColor(context, R.color.colorWhite_5);
        this.radius = (float) DensityUtil.dip2px(getContext(), 11.0f);
        this.textSize = (float) DensityUtil.dip2px(getContext(), 9.0f);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setColor(this.enableColor);
        this.mPaint.setStyle(Style.FILL);
        this.mLinePaint = new Paint();
        this.mLinePaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
        this.mLinePaint.setStyle(Style.STROKE);
        this.mLinePaint.setStrokeWidth(1.0f);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setTextSize(this.textSize);
        this.mTextPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite_5));
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTypeface(App.getRegularTypeface());
        this.mDrawStr = getResources().getString(R.string.BUTTON_VIDEO_EDIT_EFFECTS_GLOBAL_SETTING);
        this.mGestureDetector = new GestureDetector(context, new SeekGestureDetector());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.left = ((float) getPaddingLeft()) + this.textSize;
        this.right = ((float) (getMeasuredWidth() - getPaddingRight())) - this.textSize;
        this.height = (float) (getMeasuredHeight() / 2);
        if (this.toMove) {
            this.XCenter = (this.pro * (this.right - this.left)) + this.left;
            this.toMove = false;
        } else {
            this.XCenter = this.right;
            this.pro = 1.0f;
        }
        this.textTopOffset = ((this.radius + this.height) + ((float) DensityUtil.dip2px(getContext(), 5.0f))) + this.textSize;
    }

    protected void onDraw(Canvas canvas) {
        Canvas canvas2;
        super.onDraw(canvas);
        if (this.needGesture) {
            canvas2 = canvas;
            canvas2.drawLine(this.left, this.height - (this.radius / 2.0f), this.left, this.height + (this.radius / 2.0f), this.mLinePaint);
            canvas.drawLine(this.left, this.height, this.right, this.height, this.mLinePaint);
            canvas2 = canvas;
            canvas2.drawLine(this.right, this.height - (this.radius / 2.0f), this.right, this.height + (this.radius / 2.0f), this.mPaint);
        } else {
            canvas2 = canvas;
            canvas2.drawLine(this.left, this.height - (this.radius / 2.0f), this.left, this.height + (this.radius / 2.0f), this.mLinePaint);
            canvas.drawLine(this.left, this.height, this.right - this.radius, this.height, this.mLinePaint);
        }
        if (this.XCenter > this.right) {
            this.XCenter = this.right;
        }
        if (this.XCenter < this.left) {
            this.XCenter = this.left;
        }
        if (this.drawDampLine && this.needGesture) {
            canvas2 = canvas;
            canvas2.drawLine(((this.right - this.left) * this.DAMP_FACTOR) + this.left, this.height - (this.radius / 2.0f), ((this.right - this.left) * this.DAMP_FACTOR) + this.left, (this.radius / 2.0f) + this.height, this.mLinePaint);
            if (this.XCenter == (this.DAMP_FACTOR * (this.right - this.left)) + this.left && this.canDrawText) {
                canvas.drawText(this.mDrawStr, this.XCenter, this.textTopOffset, this.mTextPaint);
            }
        } else if (this.XCenter == this.right && this.needGesture && this.canDrawText) {
            canvas.drawText(this.mDrawStr, this.XCenter, this.textTopOffset, this.mTextPaint);
        }
        canvas.drawCircle(this.XCenter, this.height, this.radius, this.mPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        if ((event.getAction() == 1 || event.getAction() == 3) && this.onProgressChangeListener != null) {
            this.onProgressChangeListener.onVolumeTouchUp(this.pro);
        }
        return true;
    }

    private void moveToX(float xCenter) {
        if (this.XCenter < this.left) {
            this.XCenter = this.left;
            this.pro = StaticLayoutUtil.DefaultSpacingadd;
        } else if (this.XCenter > this.right) {
            this.XCenter = this.right;
            this.pro = 1.0f;
        } else {
            this.pro = getProFloat((xCenter - this.left) / (this.right - this.left));
        }
        if (this.drawDampLine && this.pro >= this.DAMP_FACTOR - DAMP_RANGE && this.pro <= this.DAMP_FACTOR + DAMP_RANGE) {
            this.pro = this.DAMP_FACTOR;
            this.XCenter = (this.pro * (this.right - this.left)) + this.left;
        }
        if (!this.needDamp01) {
            return;
        }
        if (this.pro >= StaticLayoutUtil.DefaultSpacingadd && this.pro <= DAMP_RANGE) {
            this.pro = StaticLayoutUtil.DefaultSpacingadd;
            this.XCenter = (this.pro * (this.right - this.left)) + this.left;
        } else if (this.pro >= 0.95f) {
            this.pro = 1.0f;
            this.XCenter = (this.pro * (this.right - this.left)) + this.left;
        }
    }

    public float getProgressFloat() {
        return this.pro;
    }

    private float getProFloat(float pro) {
        return new BigDecimal((double) pro).setScale(2, 4).floatValue();
    }

    //    public void setPro(float p) {
//        post(1.lambdaFactory$(this, p));
//    }
//
//    private /* synthetic */ void lambda$setPro$0(float p) {
//        this.pro = p;
//        if (this.right > StaticLayoutUtil.DefaultSpacingadd) {
//            this.XCenter = (this.pro * (this.right - this.left)) + this.left;
//            moveToX((this.pro * (this.right - this.left)) + this.left);
//            invalidate();
//        } else {
//            this.toMove = true;
//        }
//        invalidate();
//    }
    public void setPro(final float p) {
        post(new Runnable() {
            @Override
            public void run() {
                pro = p;
                if (right > StaticLayoutUtil.DefaultSpacingadd) {
                    XCenter = (pro * (right - left)) + left;
                    moveToX((pro * (right - left)) + left);
                    invalidate();
                } else {
                    toMove = true;
                }
                invalidate();
            }
        });
    }

    public boolean isNeedGesture() {
        return this.needGesture;
    }

    public void setOnCustomProgressChangedListener(OnCustomProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public void build() {
        invalidate();
    }

    public void setCanEdit(boolean enable) {
        this.needGesture = enable;
        if (enable) {
            this.mLinePaint.setColor(this.enableColor);
            this.mPaint.setColor(this.enableColor);
        } else {
            this.mLinePaint.setColor(this.disableColor);
            this.mPaint.setColor(this.disableColor);
        }
        invalidate();
    }

    public void setOnCustomProgressProStateChangeListener(OnCustomProgressProStateChangeListener onCustomProgressProStateChangeListener) {
        this.onCustomProgressProStateChangeListener = onCustomProgressProStateChangeListener;
    }
}