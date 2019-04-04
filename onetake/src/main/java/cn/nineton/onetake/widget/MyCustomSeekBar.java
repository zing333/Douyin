package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.manager.FilterEffectManager.EffectType;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.FilterEffectManager;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;

public class MyCustomSeekBar extends View {
    private float XCenter;
    private float centerLeft;
    private float distance;
    private float height;
    private boolean isCenter;
    private boolean isSpecial;
    private float left;
    private int mColor;
    private GestureDetector mGestureDetector;
    private Paint mPaint;
    private float magneticForceCoefficient;
    private float max;
    private float min;
    private OnCustomProgressChangedListener onProgressChangedListener;
    private float pro;
    private float radius;
    private float right;
    private float seekCritical;
    private FilterEffectManager.EffectType type;
    private float unit;
    private float upCenter;
    private float upDiff;
    private float upPro;

    public interface OnCustomProgressChangedListener {
        void onProgressChanged(float f);

        void onProgressUp(float f);
    }

    private class SeekGestureDetector implements OnGestureListener {
        private SeekGestureDetector() {
        }

        public boolean onDown(MotionEvent e) {
            MyCustomSeekBar.this.centerLeft = MyCustomSeekBar.this.XCenter;
            MyCustomSeekBar.this.distance = StaticLayoutUtil.DefaultSpacingadd;
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            MyCustomSeekBar.this.XCenter = e.getX();
            if (MyCustomSeekBar.this.type == FilterEffectManager.EffectType.ROTATE || MyCustomSeekBar.this.type == FilterEffectManager.EffectType.VERTICAL || MyCustomSeekBar.this.type == FilterEffectManager.EffectType.HORIZONTAL) {
                MyCustomSeekBar.this.moveToX(MyCustomSeekBar.this.XCenter);
            } else {
                MyCustomSeekBar.this.moveStrengthToX(MyCustomSeekBar.this.XCenter);
            }
            if (MyCustomSeekBar.this.onProgressChangedListener != null) {
                MyCustomSeekBar.this.onProgressChangedListener.onProgressUp(MyCustomSeekBar.this.pro);
            }
            MyCustomSeekBar.this.invalidate();
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (MyCustomSeekBar.this.type == FilterEffectManager.EffectType.ROTATE || MyCustomSeekBar.this.type == FilterEffectManager.EffectType.VERTICAL || MyCustomSeekBar.this.type == FilterEffectManager.EffectType.HORIZONTAL) {
                MyCustomSeekBar.this.distance = MyCustomSeekBar.this.distance + distanceX;
                if (Math.abs(MyCustomSeekBar.this.distance) > MyCustomSeekBar.this.seekCritical) {
                    MyCustomSeekBar.this.upDiff = e2.getX() - e1.getX();
                    MyCustomSeekBar.this.XCenter = MyCustomSeekBar.this.centerLeft + MyCustomSeekBar.this.upDiff;
                    MyCustomSeekBar.this.moveToX(MyCustomSeekBar.this.XCenter);
                    MyCustomSeekBar.this.distance = StaticLayoutUtil.DefaultSpacingadd;
                }
            } else {
                MyCustomSeekBar.this.upDiff = e2.getX() - e1.getX();
                MyCustomSeekBar.this.XCenter = MyCustomSeekBar.this.centerLeft + MyCustomSeekBar.this.upDiff;
                MyCustomSeekBar.this.moveStrengthToX(MyCustomSeekBar.this.XCenter);
            }
            if (MyCustomSeekBar.this.pro > MyCustomSeekBar.this.max) {
                MyCustomSeekBar.this.pro = MyCustomSeekBar.this.max;
            }
            if (MyCustomSeekBar.this.pro < MyCustomSeekBar.this.min) {
                MyCustomSeekBar.this.pro = MyCustomSeekBar.this.min;
            }
            MyCustomSeekBar.this.invalidate();
            if (MyCustomSeekBar.this.onProgressChangedListener != null) {
                MyCustomSeekBar.this.onProgressChangedListener.onProgressChanged(MyCustomSeekBar.this.pro);
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    public MyCustomSeekBar(Context context) {
        this(context, null);
    }

    public MyCustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.min = -5.0f;
        this.max = 5.0f;
        this.isCenter = true;
        this.pro = 2.2f;
        this.isSpecial = false;
        this.magneticForceCoefficient = 2.0f;
        this.mColor = ContextCompat.getColor(context, R.color.colorWhite);
        this.height = (float) DensityUtil.dip2px(10.0f);
        this.radius = (float) DensityUtil.dip2px(15.0f);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mGestureDetector = new GestureDetector(context, new SeekGestureDetector());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.left = (float) getPaddingLeft();
        this.right = (float) (getMeasuredWidth() - getPaddingRight());
        this.mPaint.setColor(this.mColor);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.left, this.radius - (this.height / 2.0f), this.left, (this.height / 2.0f) + this.radius, this.mPaint);
        canvas2 = canvas;
        canvas2.drawLine(this.right, this.radius - (this.height / 2.0f), this.right, (this.height / 2.0f) + this.radius, this.mPaint);
        canvas.drawLine(this.left, this.radius, this.right, this.radius, this.mPaint);
        if (this.type == FilterEffectManager.EffectType.ROTATE || this.type == FilterEffectManager.EffectType.VERTICAL || this.type == FilterEffectManager.EffectType.HORIZONTAL || this.isSpecial) {
            drawNoStrengthType(canvas);
        } else {
            drawStrengthType(canvas);
        }
        if (this.XCenter > this.right) {
            this.XCenter = this.right;
        }
        if (this.XCenter < this.left) {
            this.XCenter = this.left;
        }
        canvas.drawCircle(this.XCenter, this.radius, this.radius, this.mPaint);
    }

    public void setSpecial(boolean special) {
        this.isSpecial = special;
    }

    public void drawNoStrengthType(Canvas canvas) {
        this.unit = (((this.max - this.min) - 0.1f) * 20.0f) / (18.0f * (this.right - this.left));
        if (this.isCenter) {
            this.unit = (((this.max - this.min) - 0.2f) * 20.0f) / (16.0f * (this.right - this.left));
            if (((double) this.pro) < ((double) this.min) + 0.05d) {
                this.XCenter = this.left;
            } else if (((double) this.pro) > ((double) this.max) - 0.05d) {
                this.XCenter = this.right;
            } else if (((double) this.pro) > ((double) ((this.max + this.min) / this.magneticForceCoefficient)) - 0.05d && ((double) this.pro) < ((double) ((this.max + this.min) / this.magneticForceCoefficient)) + 0.05d) {
                this.XCenter = getMagneticForceX();
            } else if (this.pro > (this.max + this.min) / this.magneticForceCoefficient) {
                this.XCenter = (((this.pro - this.min) - 0.15f) / this.unit) + (this.left + (3.0f * getUnitLength()));
            } else {
                this.XCenter = (((this.pro - this.min) - 0.05f) / this.unit) + (this.left + getUnitLength());
            }
            this.seekCritical = (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd);
            Canvas canvas2 = canvas;
            canvas2.drawLine(getMagneticForceX(), this.radius - (this.height / 2.0f), getMagneticForceX(), (this.height / 2.0f) + this.radius, this.mPaint);
            return;
        }
        if (((double) this.pro) < ((double) this.min) + 0.05d) {
            this.XCenter = this.left;
        } else if (((double) this.pro) > ((double) this.max) - 0.05d) {
            this.XCenter = this.right;
        } else {
            this.XCenter = (((this.pro - this.min) - 0.05f) / this.unit) + (this.left + getUnitLength());
        }
        this.seekCritical = (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd);
    }

    public void drawStrengthType(Canvas canvas) {
        if (this.isCenter) {
            Canvas canvas2 = canvas;
            canvas2.drawLine(getMagneticForceX(), this.radius - (this.height / 2.0f), getMagneticForceX(), (this.height / 2.0f) + this.radius, this.mPaint);
        }
        this.unit = (this.right - this.left) / 10.0f;
        this.XCenter = this.left + (((this.pro - this.min) * (this.right - this.left)) / (this.max - this.min));
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void moveToX(float xCenter) {
        if (this.XCenter < this.left + getUnitLength()) {
            this.XCenter = this.left;
            this.pro = this.min;
        } else if (this.XCenter > this.right - getUnitLength()) {
            this.XCenter = this.right;
            this.pro = this.max;
        } else if (!this.isCenter) {
            this.pro = (this.min + 0.05f) + ((this.XCenter - (this.left + getUnitLength())) * this.unit);
        } else if (this.XCenter < getMagneticForceX() + getUnitLength() && this.XCenter > getMagneticForceX() - getUnitLength()) {
            this.XCenter = getMagneticForceX();
            this.pro = (this.max + this.min) / this.magneticForceCoefficient;
        } else if (this.XCenter > getMagneticForceX() + getUnitLength()) {
            this.pro = (this.min + 0.15f) + ((this.XCenter - (this.left + (3.0f * getUnitLength()))) * this.unit);
        } else {
            this.pro = (this.min + 0.05f) + ((this.XCenter - (this.left + getUnitLength())) * this.unit);
        }
    }

    private float getUnitPro() {
        return (this.max - this.min) / 200.0f;
    }

    private float getUnitLength() {
        return (this.right - this.left) / 20.0f;
    }

    public void setMagneticForceCoefficient(float magneticForceCoefficient) {
        this.magneticForceCoefficient = magneticForceCoefficient;
    }

    private float getMagneticForceX() {
        return (this.right + this.left) / this.magneticForceCoefficient;
    }

    private void moveStrengthToX(float xCenter) {
        int i = (int) ((this.XCenter - this.left) / this.unit);
        this.XCenter = this.left + (((float) i) * this.unit);
        this.pro = this.min + ((((float) i) * (this.max - this.min)) / 10.0f);
    }

    public void setProAndInvalidate(int pro) {
        LogUtil.d("setProAndInvalidate", "pro : " + pro);
        this.pro = (float) pro;
        this.XCenter = (((float) ((int) (((((float) pro) - this.min) * 10.0f) / (this.max - this.min)))) * this.unit) + this.left;
        if (this.onProgressChangedListener != null) {
            this.onProgressChangedListener.onProgressChanged((float) pro);
        }
        postInvalidate();
    }

    public MyCustomSeekBar min(float min) {
        this.min = min;
        return this;
    }

    public MyCustomSeekBar max(float max) {
        this.max = max;
        return this;
    }

    public float getProgressFloat() {
        return this.pro;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public MyCustomSeekBar pro(float pro) {
        this.pro = pro;
        if (this.onProgressChangedListener != null) {
            this.onProgressChangedListener.onProgressUp(pro);
        }
        return this;
    }

    public MyCustomSeekBar isCenter(boolean isCenter) {
        this.isCenter = isCenter;
        return this;
    }

    public MyCustomSeekBar type(FilterEffectManager.EffectType type) {
        this.type = type;
        return this;
    }

    public void setOnCustomProgressChangedListener(OnCustomProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public void build() {
        invalidate();
    }
}