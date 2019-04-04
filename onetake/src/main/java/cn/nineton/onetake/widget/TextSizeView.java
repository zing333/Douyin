package cn.nineton.onetake.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateInterpolator;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FileUtil;

public class TextSizeView extends VTBaseView {
    private static final int STATE_CHANGE = 1;
    private static final int STATE_DOWN = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_MOVE = 3;
    private static final int STATE_SET = 4;
    private boolean canChangeFlag = false;
    private int circleColor;
    private float circleOutTouchWidth;
    private float circlePaddingTop;
    private float circleRadius;
    private Rect circleRect;
    private int count;
    private int currentPosition = 0;
    private float cutLineWidth;
    private int id;
    private boolean isNeedShowOutTouch = false;
    private boolean isReady = false;
    private int lineColor;
    private float lineheight;
    private Paint mOuterPaint;
    private Paint mPaint;
    private PointBean movePoint;
    private List<PointBean> nodes;
    private int oldCurrentPosition = 0;
    private int stateOfCircle = 0;
    private float touchRange;
    private ValueAnimator valueX;

    public TextSizeView(Context context) {
        super(context);
    }

    public TextSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextSizeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public TextSizeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init() {
        setLayerType(1, null);
        this.lineColor = getResources().getColor(R.color.colorWhite);
        this.lineheight = 10.0f;
        this.circleColor = getResources().getColor(R.color.colorWhite);
        this.circleRadius = 23.0f;
        this.circleOutTouchWidth = 20.0f;
        this.circlePaddingTop = (float) DensityUtil.dip2px(getContext(), 12.0f);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeWidth(1.0f);
        this.mOuterPaint = new Paint();
        this.mOuterPaint.setAntiAlias(true);
        this.mOuterPaint.setStyle(Style.FILL);
        this.mOuterPaint.setColor(getResources().getColor(R.color.color66));
        this.movePoint = new PointBean();
        this.nodes = new ArrayList();
        this.valueX = ValueAnimator.ofFloat(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd});
        this.valueX.setDuration(200);
        this.valueX.setInterpolator(new AccelerateInterpolator());
        this.valueX.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                TextSizeView.this.movePoint.set(((Float) animation.getAnimatedValue()).floatValue(), StaticLayoutUtil.DefaultSpacingadd);
                TextSizeView.this.invalidate();
            }
        });
        this.count = 0;
        this.currentPosition = 0;
        this.oldCurrentPosition = this.currentPosition;
        this.circleRect = new Rect();
        this.touchRange = (float) DensityUtil.dip2px(getContext(), 44.0f);
        this.canChangeFlag = false;
    }

    protected void init(AttributeSet attrs) {
        init();
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TextSizeView);
        try {
            this.lineColor = array.getColor(R.styleable.TextSizeView_lineColor, this.lineColor);
            this.lineheight = array.getDimension(R.styleable.TextSizeView_lineheight, this.lineheight);
            this.circleColor = array.getColor(R.styleable.TextSizeView_circleColor, this.circleColor);
            this.circleRadius = array.getDimension(R.styleable.TextSizeView_circleRadius, this.circleRadius);
            this.circleOutTouchWidth = array.getDimension(R.styleable.TextSizeView_circleOutTouchWidth, this.circleOutTouchWidth);
            this.circlePaddingTop = array.getDimension(R.styleable.TextSizeView_circlePaddingTop, this.circlePaddingTop);
        } catch (Exception e) {
        } finally {
            array.recycle();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculator();
        postInvalidate();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurrentPosition(int currentPosition) {
        if (currentPosition >= 0 && currentPosition < this.nodes.size() && currentPosition != this.oldCurrentPosition) {
            this.currentPosition = currentPosition;
            this.stateOfCircle = 4;
            if (this.mListener != null) {
                this.mListener.change(currentPosition);
            }
            this.isReady = true;
            this.movePoint.set(((PointBean) this.nodes.get(currentPosition)).x, ((PointBean) this.nodes.get(currentPosition)).y);
            postInvalidate();
        }
    }

    public void setHintText(int count) {
        if (count > 0) {
            this.nodes.clear();
            this.count = count - 1;
            this.currentPosition = count / 2;
            this.oldCurrentPosition = this.currentPosition;
            this.isReady = false;
            this.stateOfCircle = 0;
            calculator();
            postInvalidate();
        }
    }

    public void setHintText(int count, int currentPosition) {
        if (count > 0) {
            this.nodes.clear();
            this.count = count - 1;
            if (currentPosition < 0 || currentPosition >= count) {
                this.currentPosition = count / 2;
            } else {
                this.currentPosition = currentPosition;
            }
            this.oldCurrentPosition = this.currentPosition;
            this.isReady = false;
            this.stateOfCircle = 0;
            calculator();
            postInvalidate();
        }
    }

    public void reset() {
        this.isReady = false;
        this.stateOfCircle = 4;
        postInvalidate();
    }

    private void calculator() {
        float startLineX = (float) getPaddingLeft();
        float startLineY = (((float) getPaddingTop()) + this.circlePaddingTop) + (this.circleRadius > this.lineheight ? this.circleRadius : this.lineheight);
        this.cutLineWidth = ((float) ((getWidth() - getPaddingRight()) - getPaddingLeft())) / ((float) this.count);
        this.nodes.clear();
        if (this.nodes.size() == 0) {
            for (int i = 0; i < this.count + 1; i++) {
                this.nodes.add(new PointBean((((float) i) * this.cutLineWidth) + startLineX, startLineY));
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(0, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY)), resolveSize((int) (((this.circlePaddingTop * 2.0f) + (2.0f * (this.circleRadius > this.lineheight ? this.circleRadius : this.lineheight))) + 1.0f), heightMeasureSpec));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.nodes.size() != 0) {
            float width = (float) ((getWidth() - getPaddingRight()) - getPaddingLeft());
            float startX = (float) getPaddingLeft();
            float startLineX = startX;
            float startLineY = (((float) getPaddingTop()) + this.circlePaddingTop) + (this.circleRadius > this.lineheight ? this.circleRadius : this.lineheight);
            this.mPaint.setColor(this.lineColor);
            for (int i = 0; i < this.count + 1; i++) {
                canvas.drawLine(startLineX + (((float) i) * this.cutLineWidth), startLineY - this.lineheight, startLineX + (((float) i) * this.cutLineWidth), startLineY + this.lineheight, this.mPaint);
            }
            canvas.drawLine(startLineX, startLineY, startLineX + width, startLineY, this.mPaint);
            this.mPaint.setColor(this.circleColor);
            float startCircleX;
            if (this.isReady) {
                if (this.stateOfCircle == 3) {
                    startCircleX = this.movePoint.x;
                    if (((float) getPaddingLeft()) > this.movePoint.x) {
                        startCircleX = (float) getPaddingLeft();
                    } else if (this.movePoint.x > ((float) (getWidth() - getPaddingRight()))) {
                        startCircleX = (float) (getWidth() - getPaddingRight());
                    }
                    if (this.isNeedShowOutTouch) {
                        canvas.drawCircle(startCircleX, startLineY, this.circleRadius + this.circleOutTouchWidth, this.mOuterPaint);
                    }
                    canvas.drawCircle(startCircleX, startLineY, this.circleRadius, this.mPaint);
                    this.circleRect.set((int) (startCircleX - this.circleRadius), (int) (startLineY - this.circleRadius), (int) (this.circleRadius + startCircleX), (int) (this.circleRadius + startLineY));
                } else if (this.stateOfCircle == 4 || this.stateOfCircle == 1) {
                    startCircleX = startX + (this.cutLineWidth * ((float) this.currentPosition));
                    canvas.drawCircle(startCircleX, startLineY, this.circleRadius, this.mPaint);
                    this.circleRect.set((int) (startCircleX - this.circleRadius), (int) (startLineY - this.circleRadius), (int) (this.circleRadius + startCircleX), (int) (this.circleRadius + startLineY));
                } else if (this.stateOfCircle == 2) {
                    canvas.drawCircle(this.movePoint.x, startLineY, this.circleRadius, this.mPaint);
                    this.circleRect.set((int) (this.movePoint.x - this.circleRadius), (int) ((startLineY - this.circleRadius) - this.lineheight), (int) (this.movePoint.x + this.circleRadius), (int) ((this.circleRadius + startLineY) - this.lineheight));
                }
            } else if (this.stateOfCircle == 0) {
                startCircleX = startX + (this.cutLineWidth * ((float) this.currentPosition));
                float startCircleY = startLineY;
                canvas.drawCircle(startCircleX, startCircleY, this.circleRadius, this.mPaint);
                this.circleRect.set((int) (startCircleX - this.circleRadius), (int) (startCircleY - this.circleRadius), (int) (this.circleRadius + startCircleX), (int) (this.circleRadius + startCircleY));
                this.movePoint.set(startCircleX, startCircleY);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float padding = this.cutLineWidth / 2.0f;
        this.isReady = true;
        int i;
        switch (event.getAction()) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (this.nodes.size() > 0) {
                    i = 0;
                    while (i < this.nodes.size()) {
                        if (((PointBean) this.nodes.get(i)).x - padding <= x && x <= ((PointBean) this.nodes.get(i)).x + padding && ((PointBean) this.nodes.get(i)).y - (this.touchRange / 2.0f) <= y && y <= ((PointBean) this.nodes.get(i)).y + (this.touchRange / 2.0f)) {
                            if (!(this.oldCurrentPosition == i || this.mListener == null)) {
                                this.mListener.change(i);
                            }
                            this.currentPosition = i;
                            this.oldCurrentPosition = this.currentPosition;
                            this.stateOfCircle = 2;
                            this.movePoint.set(((PointBean) this.nodes.get(this.currentPosition)).x, ((PointBean) this.nodes.get(this.currentPosition)).y);
                            invalidate();
                            if (((PointBean) this.nodes.get(this.oldCurrentPosition)).x - padding <= x && x <= ((PointBean) this.nodes.get(this.oldCurrentPosition)).x + padding && ((PointBean) this.nodes.get(this.oldCurrentPosition)).y - (this.touchRange / 2.0f) <= y && y <= ((PointBean) this.nodes.get(this.oldCurrentPosition)).y + (this.touchRange / 2.0f)) {
                                this.canChangeFlag = true;
                                break;
                            }
                        }
                        i++;
                    }
                    this.canChangeFlag = true;
                    break;
                }
                break;
            case 1:
                if (this.nodes.size() > 0) {
                    if (this.canChangeFlag) {
                        float minWidth = (float) getWidth();
                        int targetPosition = 0;
                        for (i = 0; i < this.nodes.size(); i++) {
                            if (Math.abs(((PointBean) this.nodes.get(i)).x - this.movePoint.x) <= minWidth) {
                                minWidth = Math.abs(((PointBean) this.nodes.get(i)).x - this.movePoint.x);
                                targetPosition = i;
                            }
                        }
                        if (!(this.oldCurrentPosition == targetPosition || this.mListener == null)) {
                            this.mListener.change(targetPosition);
                        }
                        this.currentPosition = targetPosition;
                        this.oldCurrentPosition = this.currentPosition;
                        this.stateOfCircle = 4;
                        this.movePoint.set(((PointBean) this.nodes.get(this.currentPosition)).x, ((PointBean) this.nodes.get(this.currentPosition)).y);
                        invalidate();
                    }
                }
                this.canChangeFlag = false;
                break;
            case 2:
                if (this.nodes.size() > 0 && this.canChangeFlag) {
                    i = 0;
                    while (i < this.nodes.size()) {
                        if (((PointBean) this.nodes.get(i)).x - padding <= x && x <= ((PointBean) this.nodes.get(i)).x + padding && ((PointBean) this.nodes.get(i)).y - (this.touchRange / 2.0f) <= y && y <= ((PointBean) this.nodes.get(i)).y + (this.touchRange / 2.0f)) {
                            if (!(this.oldCurrentPosition == i || this.mListener == null)) {
                                this.mListener.change(i);
                            }
                            this.currentPosition = i;
                            this.oldCurrentPosition = this.currentPosition;
                            this.stateOfCircle = 3;
                            this.movePoint.set(((PointBean) this.nodes.get(this.currentPosition)).x, ((PointBean) this.nodes.get(this.currentPosition)).y);
                            invalidate();
                            break;
                        }
                        i++;
                    }
                    break;
                }
                break;
        }
        return true;
    }
}