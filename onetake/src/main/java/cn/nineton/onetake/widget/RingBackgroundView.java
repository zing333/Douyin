package cn.nineton.onetake.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import java.util.List;

import cn.nineton.onetake.R;

public class RingBackgroundView extends View {
    private List<String> data;
    private boolean hasInit;
    private Paint mBgPaint;
    private Paint mProPaint;
    private float mSwipeAngle;
    private int mViewHeight;
    private int mViewWidth;
    private float perPro;
    private float pro;
    private RectF rect;

    public void setData(List<String> data) {
        this.data = data;
        this.perPro = 1.0f / ((float) data.size());
    }

    public void setNowValue(String str) {
        if (this.data != null && this.data.size() > 0 && this.data.contains(str)) {
            int offset = this.data.indexOf(str);
            if (this.pro == ((float) (offset + 1)) * this.perPro) {
                return;
            }
            if (this.hasInit) {
                float lastPro = this.pro;
                this.pro = ((float) (offset + 1)) * this.perPro;
                final ValueAnimator animator = ValueAnimator.ofFloat(new float[]{lastPro, this.pro});
                animator.setDuration(200);
                animator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        RingBackgroundView.this.mSwipeAngle = 360.0f * ((Float) animation.getAnimatedValue()).floatValue();
                        RingBackgroundView.this.invalidate();
                    }
                });
                postDelayed(new Runnable() {
                    public void run() {
                        animator.start();
                    }
                }, 200);
                return;
            }
            this.hasInit = true;
            this.pro = ((float) (offset + 1)) * this.perPro;
            this.mSwipeAngle = 360.0f * this.pro;
            invalidate();
        }
    }

    public RingBackgroundView(Context context) {
        this(context, null);
    }

    public RingBackgroundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.hasInit = false;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        this.pro = StaticLayoutUtil.DefaultSpacingadd;
        this.perPro = StaticLayoutUtil.DefaultSpacingadd;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RingBackgroundView);
        int bgColor = a.getColor(R.styleable.RingBackgroundView_bgRingColor, Color.parseColor("#ffffff"));
        int proColor = a.getColor(R.styleable.RingBackgroundView_proRingColor, Color.parseColor("#ffffff"));
        int bgWidth = a.getDimensionPixelOffset(R.styleable.RingBackgroundView_bgLineWidth, 1);
        int proWidth = a.getDimensionPixelOffset(R.styleable.RingBackgroundView_proLineWidth, 1);
        this.mBgPaint = new Paint();
        this.mBgPaint.setStrokeWidth((float) bgWidth);
        this.mBgPaint.setColor(bgColor);
        this.mBgPaint.setStyle(Style.STROKE);
        this.mBgPaint.setAntiAlias(true);
        this.mProPaint = new Paint();
        this.mProPaint.setStrokeWidth((float) proWidth);
        this.mProPaint.setColor(proColor);
        this.mProPaint.setStyle(Style.STROKE);
        this.mProPaint.setAntiAlias(true);
        a.recycle();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw == 0 && oldh == 0) {
            this.mViewWidth = w;
            this.mViewHeight = h;
            this.rect = new RectF(0.5f, 0.5f, ((float) w) - 0.5f, ((float) h) - 0.5f);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((((float) this.mViewWidth) * 1.0f) / 2.0f, (((float) this.mViewHeight) * 1.0f) / 2.0f, (float) ((this.mViewWidth - 1) / 2), this.mBgPaint);
        if (this.rect != null) {
            canvas.drawArc(this.rect, -90.0f, this.mSwipeAngle, false, this.mProPaint);
        }
    }
}