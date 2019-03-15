package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.LogUtil;

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {
    private TextView borderText = null;

    public StrokeTextView(Context context) {
        super(context);
        this.borderText = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderText = new TextView(context, attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderText = new TextView(context, attrs, defStyle);
        init();
    }

    public void init() {
        setTypeface(FontsUtil.setDefaultTypeFace());
        TextPaint tp1 = this.borderText.getPaint();
        tp1.setStrokeWidth((float) DensityUtil.dip2px(2.0f));
        tp1.setStyle(Style.STROKE);
        tp1.setStrokeJoin(Join.ROUND);
        this.borderText.setTextColor(getResources().getColor(R.color.colorBlack));
        this.borderText.setGravity(getGravity());
        this.borderText.setTypeface(FontsUtil.setDefaultTypeFace());
    }

    public void setStrokeWidth(float scale) {
        setStrokeWidth(scale, true);
    }

    public void setStrokeWidth(float scale, boolean isOld) {
        if (isOld) {
            float v = ((float) DensityUtil.dip2px(4.0f)) * scale;
            LogUtil.d("setStrokeWidth", "scale : " + scale + " v : " + v);
            this.borderText.getPaint().setStrokeWidth(v);
            return;
        }
        this.borderText.getPaint().setStrokeWidth(StaticLayoutUtil.DefaultSpacingadd);
        this.borderText.setAlpha(0.5f);
        setShadowLayer(1.0f, 1.0f, 1.0f, getResources().getColor(R.color.colorHalfBlack));
    }

    public void setTypeFonts(Typeface typeFonts) {
        setTypeface(typeFonts);
        this.borderText.setTypeface(typeFonts);
    }

    public void setLayoutParams(LayoutParams params) {
        super.setLayoutParams(params);
        this.borderText.setLayoutParams(params);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = this.borderText.getText();
        if (tt == null || !tt.equals(getText())) {
            this.borderText.setText(getText());
            postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.borderText.layout(left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        this.borderText.draw(canvas);
        super.onDraw(canvas);
    }

    public void setTextSize(float size) {
        this.borderText.setTextSize(size);
        super.setTextSize(size);
    }

    public void setTextSize(int unit, float size) {
        this.borderText.setTextSize(unit, size);
        super.setTextSize(unit, size);
    }

    public boolean isInEditMode() {
        return true;
    }
}