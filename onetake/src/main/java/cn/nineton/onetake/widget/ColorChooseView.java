package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
//import android.view.View.MeasureSpec;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.FileUtil;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.FileUtil;

public class ColorChooseView extends VTBaseView {
    private float circleInPadding;
    private float circleOutTouchWidth;
    private List<PointBean> circlePoints;
    private float circleRadius;
    private float circleStrokeWidth;
    List<Integer> colors;
    private int countColorOfCircle;
    private boolean isNeedShowOutTouch = false;
    private Paint mOuterPaint;
    private Paint mPaint;
    private int touchPosition = -1;

    public ColorChooseView(Context context) {
        super(context);
    }

    public ColorChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public ColorChooseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init() {
        setLayerType(1, null);
        this.circleRadius = 32.0f;
        this.circleInPadding = 10.0f;
        this.circleOutTouchWidth = 20.0f;
        this.circleStrokeWidth = 4.0f;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mOuterPaint = new Paint();
        this.mOuterPaint.setAntiAlias(true);
        this.mOuterPaint.setStyle(Style.STROKE);
        this.mOuterPaint.setStrokeWidth(this.circleStrokeWidth);
        this.mOuterPaint.setColor(-65536);
        this.circlePoints = new ArrayList();
        this.colors = new ArrayList();
        this.colors.add(Integer.valueOf(-1));
        this.colors.add(Integer.valueOf(-16777216));
        this.colors.add(Integer.valueOf(-256));
        this.countColorOfCircle = this.colors.size();
        this.currentPosition = (this.countColorOfCircle + 1) / 2;
    }

    protected void init(AttributeSet attrs) {
        init();
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ColorChooseView);
        try {
            this.circleRadius = array.getDimension(R.styleable.ColorChooseView_ccv_circleRadius, this.circleRadius);
            this.circleInPadding = array.getDimension(R.styleable.ColorChooseView_ccv_circleInPadding, this.circleInPadding);
            this.circleOutTouchWidth = array.getDimension(R.styleable.ColorChooseView_ccv_circleOutTouchWidth, this.circleOutTouchWidth);
            this.circleStrokeWidth = array.getDimension(R.styleable.ColorChooseView_ccv_circleStrokeWidth, this.circleStrokeWidth);
            this.mOuterPaint.setStrokeWidth(this.circleStrokeWidth);
        } catch (Exception e) {
        } finally {
            array.recycle();
        }
    }

    public void setColorOfCircle(List<Integer> colors) {
        if (colors != null && colors.size() > 0) {
            this.countColorOfCircle = colors.size();
            this.circlePoints.clear();
            this.colors.clear();
            this.colors.addAll(colors);
            this.currentPosition = (this.countColorOfCircle + 1) / 2;
            calculator();
            postInvalidate();
        }
    }

    public void setColorOfCircle(List<Integer> colors, int currentPosition) {
        if (colors != null && colors.size() > 0) {
            this.countColorOfCircle = colors.size();
            this.circlePoints.clear();
            this.colors.clear();
            this.colors.addAll(colors);
            if (currentPosition < 0 || currentPosition >= colors.size()) {
                this.currentPosition = (this.countColorOfCircle + 1) / 2;
            } else {
                this.currentPosition = currentPosition;
            }
            calculator();
            postInvalidate();
        }
    }

    private void calculator() {
        int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        float cutLine = (float) (width / this.countColorOfCircle);
        float startX = this.circleRadius + this.circleInPadding;
        float startY = (((this.circleRadius + ((float) getPaddingTop())) + this.circleInPadding) + this.circleStrokeWidth) + 1.0f;
        this.circlePoints.clear();
        if (this.countColorOfCircle > 1) {
            float paddingOfCircles = (((float) width) - ((this.circleRadius + this.circleInPadding) * 2.0f)) / ((float) (this.countColorOfCircle - 1));
            for (int i = 0; i < this.countColorOfCircle; i++) {
                this.circlePoints.add(new PointBean((((float) getPaddingLeft()) + (cutLine / 2.0f)) + (((float) i) * cutLine), startY));
            }
            return;
        }
        this.circlePoints.add(new PointBean((float) (getWidth() / 2), startY));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculator();
        postInvalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(0, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY)), resolveSize(((getPaddingTop() + getPaddingBottom()) + (((int) ((this.circleRadius + this.circleInPadding) + this.circleStrokeWidth)) * 2)) + 2, heightMeasureSpec));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.circlePoints.size() != 0) {
            int i = 0;
            while (i < this.circlePoints.size()) {
                this.mPaint.setColor(((Integer) this.colors.get(i)).intValue());
                if (this.currentPosition == i) {
                    this.mOuterPaint.setColor(((Integer) this.colors.get(i)).intValue());
                    canvas.drawCircle(((PointBean) this.circlePoints.get(i)).x, ((PointBean) this.circlePoints.get(i)).y, (this.circleRadius + this.circleInPadding) + (this.circleStrokeWidth / 2.0f), this.mOuterPaint);
                }
                if (this.isNeedShowOutTouch && this.touchPosition >= 0 && this.touchPosition == i) {
                    this.mOuterPaint.setColor(((Integer) this.colors.get(i)).intValue());
                    canvas.drawCircle(((PointBean) this.circlePoints.get(i)).x, ((PointBean) this.circlePoints.get(i)).y, (this.circleRadius + this.circleInPadding) + (this.circleStrokeWidth / 2.0f), this.mOuterPaint);
                }
                if (((Integer) this.colors.get(i)).intValue() == -16777216) {
                    canvas.drawCircle(((PointBean) this.circlePoints.get(i)).x, ((PointBean) this.circlePoints.get(i)).y, this.circleRadius + 1.0f, this.mPaint);
                } else {
                    canvas.drawCircle(((PointBean) this.circlePoints.get(i)).x, ((PointBean) this.circlePoints.get(i)).y, this.circleRadius, this.mPaint);
                }
                i++;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float padding = this.circleRadius + this.circleOutTouchWidth;
        int i;
        PointBean bean;
        switch (event.getAction()) {
            case 0:
                if (this.circlePoints.size() != 0) {
                    for (i = 0; i < this.circlePoints.size(); i++) {
                        bean = (PointBean) this.circlePoints.get(i);
                        if (bean.x - padding <= x && x <= bean.x + padding && bean.y - padding <= y && y <= bean.y + padding) {
                            if (i != this.currentPosition) {
                                this.touchPosition = i;
                                invalidate();
                                break;
                            }
                        }
                    }
                    break;
                }
                break;
            case 1:
                this.touchPosition = -1;
                if (this.circlePoints.size() == 0) {
                    invalidate();
                    break;
                }
                for (i = 0; i < this.circlePoints.size(); i++) {
                    bean = (PointBean) this.circlePoints.get(i);
                    if (bean.x - padding <= x && x <= bean.x + padding && bean.y - padding <= y && y <= bean.y + padding) {
                        if (i != this.currentPosition) {
                            this.currentPosition = i;
                            invalidate();
                            if (this.mListener != null) {
                                this.mListener.change(i);
                            }
                        }
                        invalidate();
                        break;
                    }
                }
                invalidate();
                break;
        }
        return true;
    }
}