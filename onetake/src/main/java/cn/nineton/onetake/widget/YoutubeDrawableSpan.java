package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class YoutubeDrawableSpan extends DynamicDrawableSpan {
    private Context context;
    private Paint mPaint;
    private String mStr;
    private int size = 0;
    private Rect tempR;
    private boolean touchDown;

    public YoutubeDrawableSpan(Context context) {
        this.context = context;
        this.mPaint = new Paint();
        this.mPaint.setColor(context.getResources().getColor(R.color.colorBlack));
        this.mPaint.setTextAlign(Align.CENTER);
        this.mPaint.setFakeBoldText(true);
        this.mPaint.setTextSize((float) DensityUtil.dip2px(15.5f));
        this.mPaint.setFlags(1);
        this.mStr = " " + context.getResources().getString(R.string.SOCIAL_YOUTUBE);
        this.tempR = new Rect();
    }

    public void setTouchDown(boolean isTouchDown) {
        this.touchDown = isTouchDown;
    }

    public Drawable getDrawable() {
        Drawable finalDrawable = App.getContext().getResources().getDrawable(R.drawable.icon_12_link_youtube);
        finalDrawable.setBounds(0, 0, finalDrawable.getIntrinsicWidth(), finalDrawable.getIntrinsicHeight());
        return finalDrawable;
    }

    public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
        Rect rect = getDrawable().getBounds();
        if (fm != null) {
            FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = (drHeight / 2) - (fontHeight / 4);
            int bottom = (drHeight / 2) + (fontHeight / 4);
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        //this.size = rect.right + WaterMarkBitmapUtil.getTextWidth(this.mStr, this.mPaint, this.tempR);
        return this.size;
    }

    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (this.touchDown) {
            canvas.save();
            int i = ((int) x) - 5;
            float f = x - 5.0f;
            if (this.size > 0) {
                y = this.size;
            }
            canvas.clipRect(new Rect(i, top, (int) ((f + ((float) y)) + 15.0f), bottom));
            canvas.drawColor(App.getContext().getResources().getColor(R.color.colorLightGray));
            canvas.restore();
        }
        Drawable b = getDrawable();
        canvas.save();
        canvas.translate(x, (float) ((((bottom - top) - b.getBounds().bottom) / 2) + top));
        b.draw(canvas);
        canvas.restore();
        paint.setColor(this.context.getResources().getColor(R.color.colorBlack));
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        canvas.drawText(this.mStr, ((float) b.getBounds().right) + x, (float) ((((((bottom - top) - fontMetrics.bottom) + fontMetrics.top) / 2) - fontMetrics.top) + top), paint);
    }
}