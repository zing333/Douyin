package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.FrameLayout;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;

public class ShadowLayout extends FrameLayout {
    private float mCornerRadius;
    private float mDx;
    private float mDy;
    private boolean mForceInvalidateShadow = false;
    private boolean mInvalidateShadowOnSizeChanged = true;
    private int mShadowColor;
    private float mShadowRadius;

    public ShadowLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            if (getBackground() == null || this.mInvalidateShadowOnSizeChanged || this.mForceInvalidateShadow) {
                this.mForceInvalidateShadow = false;
                setBackgroundCompat(w, h);
            }
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mForceInvalidateShadow) {
            this.mForceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        this.mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    public void invalidateShadow() {
        this.mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        int xPadding = (int) (this.mShadowRadius + Math.abs(this.mDx));
        int yPadding = (int) (this.mShadowRadius + Math.abs(this.mDy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void setBackgroundCompat(int w, int h) {
        BitmapDrawable drawable = new BitmapDrawable(getResources(), createShadowBitmap(w, h, this.mCornerRadius, this.mShadowRadius, this.mDx, this.mDy, this.mShadowColor, 0));
        if (VERSION.SDK_INT <= 16) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.ShadowLayout);
        if (attr != null) {
            try {
                this.mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, (float) DensityUtil.dip2px(4.0f));
                this.mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, (float) DensityUtil.dip2px(4.0f));
                this.mDx = attr.getDimension(R.styleable.ShadowLayout_sl_dx, StaticLayoutUtil.DefaultSpacingadd);
                this.mDy = attr.getDimension(R.styleable.ShadowLayout_sl_dy, StaticLayoutUtil.DefaultSpacingadd);
                this.mShadowColor = attr.getColor(R.styleable.ShadowLayout_sl_shadowColor, Color.parseColor("#000000"));
            } finally {
                attr.recycle();
            }
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius, float dx, float dy, int shadowColor, int fillColor) {
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Config.ALPHA_8);
        Canvas canvas = new Canvas(output);
        RectF shadowRect = new RectF(shadowRadius, shadowRadius, ((float) shadowWidth) - shadowRadius, ((float) shadowHeight) - shadowRadius);
        if (dy > StaticLayoutUtil.DefaultSpacingadd) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < StaticLayoutUtil.DefaultSpacingadd) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }
        if (dx > StaticLayoutUtil.DefaultSpacingadd) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < StaticLayoutUtil.DefaultSpacingadd) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }
        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Style.FILL);
        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        return output;
    }

    public void setShadowRadius(float radius) {
        this.mShadowRadius = radius;
        LogUtil.d("slim", "set radius:" + radius);
        postInvalidate();
    }
}