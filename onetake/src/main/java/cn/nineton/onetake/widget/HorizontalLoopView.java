package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.blink.academy.onetake.App;
import com.blink.academy.onetake.support.debug.LogUtil;
import com.blink.academy.onetake.support.utils.DensityUtil;
import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.weigan.loopview.R;
import com.weigan.loopview.bean.Bean;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HorizontalLoopView extends View {
    private static final float DEFAULT_LINE_SPACE = 2.0f;
    private static final int DEFAULT_TEXT_SIZE = ((int) (Resources.getSystem().getDisplayMetrics().density * 12.0f));
    private static final int DEFAULT_VISIBIE_ITEMS = 9;
    int centerTextColor;
    int change;
    private Context context;
    int dividerColor;
    Bean[] drawingStrings;
    int firstLineX;
    private GestureDetector flingGestureDetector;
    int halfCircumference;
    int initPosition;
    boolean isLoop;
    public boolean isTouch = false;
    List<Bean> items;
    int itemsVisibleCount;
    private int lastItem = -1;
    float lineSpacingMultiplier;
    private Bean mBean = new Bean("", "", null);
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private float mFirstLineStringStartX;
    private ScheduledFuture<?> mFuture;
    Handler mHandler;
    private int mOffset = 0;
    int maxItemWidth;
    int measuredHeight;
    int measuredWidth;
    OnItemSelectedListener onItemSelectedListener;
    private OnSelectItemChange onSelect;
    int outerTextColor;
    private Paint paintCenterText;
    private Paint paintIndicator;
    private Paint paintOuterText;
    int preCurrentIndex;
    private float previousX;
    int radius;
    private float scaleX = 1.0f;
    int secondLineX;
    private int selectedItem;
    long startTime = 0;
    private Rect tempRect = new Rect();
    int textSize;
    public int totalScrollX;

    public interface OnSelectItemChange {
        void onSelect(int i);
    }

    public enum ACTION {
        CLICK,
        FLING,
        DAGGLE
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
        this.paintCenterText.setColor(centerTextColor);
    }

    public void setOuterTextColor(int outerTextColor) {
        this.outerTextColor = outerTextColor;
        this.paintOuterText.setColor(outerTextColor);
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        this.paintIndicator.setColor(dividerColor);
    }

    public HorizontalLoopView(Context context) {
        super(context);
        initLoopView(context, null);
    }

    public HorizontalLoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context, attributeset);
    }

    public HorizontalLoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    private void initLoopView(Context context, AttributeSet attributeset) {
        this.context = context;
        this.mHandler = new HorizontalMessageHandler(this);
        this.flingGestureDetector = new GestureDetector(context, new HorizontalLoopViewGestureListener(this));
        this.flingGestureDetector.setIsLongpressEnabled(false);
        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.androidWheelView);
        this.textSize = typedArray.getInteger(0, DEFAULT_TEXT_SIZE);
        this.textSize = (int) (Resources.getSystem().getDisplayMetrics().density * ((float) this.textSize));
        this.lineSpacingMultiplier = typedArray.getFloat(R.styleable.androidWheelView_awv_lineSpace, DEFAULT_LINE_SPACE);
        this.centerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_centerTextColor, -1);
        this.outerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_outerTextColor, 1308622847);
        this.dividerColor = typedArray.getInteger(R.styleable.androidWheelView_awv_dividerTextColor, -3815995);
        this.itemsVisibleCount = typedArray.getInteger(R.styleable.androidWheelView_awv_itemsVisibleCount, 9);
        if (this.itemsVisibleCount % 2 == 0) {
            this.itemsVisibleCount = 9;
        }
        this.isLoop = typedArray.getBoolean(R.styleable.androidWheelView_awv_isLoop, true);
        typedArray.recycle();
        this.drawingStrings = new Bean[this.itemsVisibleCount];
        this.totalScrollX = 0;
        this.initPosition = -1;
        initPaints();
    }

    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 != 0 && visibleNumber != this.itemsVisibleCount) {
            this.itemsVisibleCount = visibleNumber;
            this.drawingStrings = new Bean[this.itemsVisibleCount];
        }
    }

    private void initPaints() {
        this.paintOuterText = new Paint();
        this.paintOuterText.setColor(this.outerTextColor);
        this.paintOuterText.setAntiAlias(true);
        this.paintOuterText.setTypeface(Typeface.MONOSPACE);
        this.paintOuterText.setTextSize((float) this.textSize);
        this.paintOuterText.setFakeBoldText(false);
        this.paintOuterText.setTypeface(App.getMediumTypeface());
        this.paintCenterText = new Paint();
        this.paintCenterText.setColor(this.centerTextColor);
        this.paintCenterText.setAntiAlias(true);
        this.paintCenterText.setTextScaleX(this.scaleX);
        this.paintCenterText.setTypeface(Typeface.MONOSPACE);
        this.paintCenterText.setTextSize((float) this.textSize);
        this.paintCenterText.setFakeBoldText(true);
        this.paintCenterText.setTypeface(App.getRegularTypeface());
        this.paintIndicator = new Paint();
        this.paintIndicator.setColor(this.dividerColor);
        this.paintIndicator.setAntiAlias(true);
    }

    private void remeasure() {
        if (this.items != null) {
            this.measuredWidth = getMeasuredWidth();
            this.measuredHeight = getMeasuredHeight();
            if (this.measuredWidth != 0 && this.measuredHeight != 0) {
                int paddingLeft = getPaddingLeft();
                this.measuredWidth -= getPaddingRight();
                this.halfCircumference = (int) ((((double) this.measuredWidth) * 3.141592653589793d) / 2.0d);
                this.maxItemWidth = (int) (((float) this.halfCircumference) / (this.lineSpacingMultiplier * ((float) (this.itemsVisibleCount - 1))));
                this.radius = this.measuredWidth / 2;
                this.firstLineX = (int) ((((float) this.measuredWidth) - (this.lineSpacingMultiplier * ((float) this.maxItemWidth))) / DEFAULT_LINE_SPACE);
                this.secondLineX = (int) ((((float) this.measuredWidth) + (this.lineSpacingMultiplier * ((float) this.maxItemWidth))) / DEFAULT_LINE_SPACE);
                if (this.initPosition == -1) {
                    if (this.isLoop) {
                        this.initPosition = (this.items.size() + 1) / 2;
                    } else {
                        this.initPosition = 0;
                    }
                }
                this.mFirstLineStringStartX = (float) (this.maxItemWidth / 2);
                this.preCurrentIndex = this.initPosition;
            }
        }
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemW = this.lineSpacingMultiplier * ((float) this.maxItemWidth);
            this.mOffset = (int) (((((float) this.totalScrollX) % itemW) + itemW) % itemW);
            if (((float) this.mOffset) > itemW / DEFAULT_LINE_SPACE) {
                this.mOffset = (int) (itemW - ((float) this.mOffset));
            } else {
                this.mOffset = -this.mOffset;
            }
        }
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new HorizontalSmoothScrollTimerTask(this, this.mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityX) {
        cancelFuture();
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new HorizontalInertiaTimerTask(this, velocityX), 0, (long) 10, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (this.mFuture != null && !this.mFuture.isCancelled()) {
            this.mFuture.cancel(true);
            this.mFuture = null;
        }
    }

    public void setNotLoop() {
        this.isLoop = false;
    }

    public final void setTextSize(float size) {
        if (size > StaticLayoutUtil.DefaultSpacingadd) {
            this.textSize = (int) (this.context.getResources().getDisplayMetrics().density * size);
            this.paintOuterText.setTextSize((float) this.textSize);
            this.paintCenterText.setTextSize((float) this.textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else if (this.items != null && this.items.size() > initPosition) {
            this.initPosition = initPosition;
        }
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        this.onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<Bean> items) {
        this.items = items;
        remeasure();
        invalidate();
    }

    public void resetItems(List<Bean> items) {
        this.items = items;
        invalidate();
    }

    public final int getSelectedItem() {
        return this.selectedItem;
    }

    protected final void onItemSelected() {
        this.isTouch = false;
        invalidate();
        if (this.onItemSelectedListener != null) {
            postDelayed(new HorizontalOnItemSelectedRunnable(this), 200);
        }
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setCurrentPosition(int position) {
        if (this.items != null && !this.items.isEmpty()) {
            int size = this.items.size();
            if (position >= 0 && position < size && position != this.selectedItem) {
                this.initPosition = position;
                this.totalScrollX = 0;
                this.selectedItem = position;
                this.lastItem = position;
                this.mOffset = 0;
                invalidate();
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.items != null) {
            this.change = (int) (((float) this.totalScrollX) / (this.lineSpacingMultiplier * ((float) this.maxItemWidth)));
            this.preCurrentIndex = this.initPosition + (this.change % this.items.size());
            if (this.isLoop) {
                if (this.preCurrentIndex < 0) {
                    this.preCurrentIndex = this.items.size() + this.preCurrentIndex;
                }
                if (this.preCurrentIndex > this.items.size() - 1) {
                    this.preCurrentIndex -= this.items.size();
                }
            } else {
                if (this.preCurrentIndex < 0) {
                    this.preCurrentIndex = 0;
                }
                if (this.preCurrentIndex > this.items.size() - 1) {
                    this.preCurrentIndex = this.items.size() - 1;
                }
            }
            int j2 = (int) (((float) this.totalScrollX) % (this.lineSpacingMultiplier * ((float) this.maxItemWidth)));
            for (int k1 = 0; k1 < this.itemsVisibleCount; k1++) {
                int l1 = this.preCurrentIndex - ((this.itemsVisibleCount / 2) - k1);
                if (this.isLoop) {
                    while (l1 < 0) {
                        l1 += this.items.size();
                    }
                    while (l1 > this.items.size() - 1) {
                        l1 -= this.items.size();
                    }
                    this.drawingStrings[k1] = (Bean) this.items.get(l1);
                } else if (l1 < 0) {
                    this.drawingStrings[k1] = this.mBean;
                } else if (l1 > this.items.size() - 1) {
                    this.drawingStrings[k1] = this.mBean;
                } else {
                    this.drawingStrings[k1] = (Bean) this.items.get(l1);
                }
            }
            for (int i = 0; i < this.itemsVisibleCount; i++) {
                canvas.save();
                float itemW = ((float) this.maxItemWidth) * this.lineSpacingMultiplier;
                double radian = (((double) ((((float) i) * itemW) - ((float) j2))) * 3.141592653589793d) / ((double) this.halfCircumference);
                if (radian >= 3.141592653589793d || radian <= 0.0d) {
                    canvas.restore();
                } else {
                    int translateX = (int) ((((double) this.radius) - (Math.cos(radian) * ((double) this.radius))) - ((Math.sin(radian) * ((double) this.maxItemWidth)) / 2.0d));
                    canvas.translate((float) translateX, StaticLayoutUtil.DefaultSpacingadd);
                    canvas.scale((float) Math.sin(radian), 1.0f);
                    Bean drawingString = this.drawingStrings[i];
                    int xOffset = getTextX(drawingString.getFirtLine(), this.paintOuterText, this.tempRect);
                    if (((float) xOffset) > this.mFirstLineStringStartX) {
                        xOffset = (int) this.mFirstLineStringStartX;
                    }
                    if (translateX <= this.firstLineX && this.maxItemWidth + translateX >= this.firstLineX) {
                        canvas.save();
                        canvas.clipRect(0, 0, this.firstLineX - translateX, this.measuredHeight);
                        if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintOuterText);
                        }
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect((float) (this.firstLineX - translateX), StaticLayoutUtil.DefaultSpacingadd, itemW, (float) this.measuredHeight);
                        if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintOuterText);
                        }
                        canvas.restore();
                    } else if (translateX <= this.secondLineX && this.maxItemWidth + translateX >= this.secondLineX) {
                        canvas.save();
                        canvas.clipRect(0, 0, this.secondLineX - translateX, this.measuredHeight);
                        if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintOuterText);
                        }
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(this.secondLineX - translateX, 0, (int) itemW, this.measuredHeight);
                        if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintOuterText);
                        }
                        canvas.restore();
                    } else if (translateX < this.firstLineX || this.maxItemWidth + translateX > this.secondLineX) {
                        canvas.clipRect(0, 0, (int) itemW, this.measuredHeight);
                        if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintOuterText, this.tempRect), this.paintOuterText);
                        }
                    } else {
                        canvas.clipRect(-DensityUtil.dip2px((float) this.textSize), 0, (int) itemW, this.measuredHeight);
                        xOffset = getTextX(drawingString.getFirtLine(), this.paintCenterText, this.tempRect);
                        if (!this.isTouch) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintCenterText, this.tempRect), this.paintCenterText);
                            int indexOf = this.items.indexOf(drawingString);
                            if (this.selectedItem != indexOf) {
                                this.selectedItem = indexOf;
                                if (this.onSelect != null) {
                                    this.onSelect.onSelect(indexOf);
                                }
                            }
                            if (this.lastItem != this.selectedItem) {
                                this.lastItem = this.selectedItem;
                                invalidate();
                            }
                        } else if (this.items.indexOf(drawingString) == this.selectedItem) {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintCenterText, this.tempRect), this.paintCenterText);
                        } else {
                            canvas.drawText(drawingString.getFirtLine(), this.mFirstLineStringStartX - ((float) xOffset), (float) getTextY(drawingString.getFirtLine(), this.paintCenterText, this.tempRect), this.paintOuterText);
                        }
                    }
                    canvas.restore();
                }
            }
        }
    }

    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        return (((int) (((float) rect.width()) * this.scaleX)) / 2) - 10;
    }

    private int getTextY(String content, Paint paint, Rect rect) {
        paint.getTextBounds(content, 0, content.length(), rect);
        return (this.measuredHeight / 2) - (rect.height() / 2);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = this.flingGestureDetector.onTouchEvent(event);
        float itemW = this.lineSpacingMultiplier * ((float) this.maxItemWidth);
        switch (event.getAction()) {
            case 0:
                this.isTouch = true;
                this.startTime = System.currentTimeMillis();
                cancelFuture();
                this.previousX = event.getRawX();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
            case 2:
                float dx = this.previousX - event.getRawX();
                this.previousX = event.getRawX();
                this.totalScrollX = (int) (((float) this.totalScrollX) + dx);
                if (!this.isLoop) {
                    float top = ((float) (-this.initPosition)) * itemW;
                    float bottom = ((float) ((this.items.size() - 1) - this.initPosition)) * itemW;
                    if (((float) this.totalScrollX) >= top) {
                        if (((float) this.totalScrollX) > bottom) {
                            this.totalScrollX = (int) bottom;
                            break;
                        }
                    }
                    this.totalScrollX = (int) top;
                    break;
                }
                break;
            default:
                if (!eventConsumed) {
                    int circlePosition = (int) ((((double) (itemW / DEFAULT_LINE_SPACE)) + (Math.acos((double) ((((float) this.radius) - event.getX()) / ((float) this.radius))) * ((double) this.radius))) / ((double) itemW));
                    this.mOffset = (int) ((((float) (circlePosition - (this.itemsVisibleCount / 2))) * itemW) - (((((float) this.totalScrollX) % itemW) + itemW) % itemW));
                    if (System.currentTimeMillis() - this.startTime <= 120) {
                        LogUtil.d("slim", "selectItem:" + this.selectedItem + " circlePosition:" + circlePosition);
                        smoothScroll(ACTION.CLICK);
                        if (this.itemsVisibleCount != 7) {
                            if (this.items.size() != 1) {
                                if (this.items.size() != 2) {
                                    if (this.items.size() != 3) {
                                        if (this.items.size() != 4) {
                                            if (this.items.size() != 5) {
                                                if (this.items.size() != 6) {
                                                    if (this.items.size() != 7) {
                                                        if (this.items.size() == 8) {
                                                            switch (this.selectedItem) {
                                                                case 0:
                                                                    if (circlePosition <= 4) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                case 1:
                                                                    if (circlePosition < 3) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                case 2:
                                                                    if (circlePosition < 2) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                case 4:
                                                                    if (circlePosition > 7) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                case 5:
                                                                    if (circlePosition <= 6) {
                                                                        smoothScroll(ACTION.CLICK);
                                                                        break;
                                                                    }
                                                                    break;
                                                                case 6:
                                                                    if (circlePosition > 5) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                case 7:
                                                                    if (circlePosition >= 4) {
                                                                        smoothScroll(ACTION.DAGGLE);
                                                                        break;
                                                                    }
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                                default:
                                                                    smoothScroll(ACTION.CLICK);
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                    switch (this.selectedItem) {
                                                        case 0:
                                                            if (circlePosition <= 4) {
                                                                smoothScroll(ACTION.DAGGLE);
                                                                break;
                                                            }
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        case 1:
                                                            if (circlePosition < 3) {
                                                                smoothScroll(ACTION.DAGGLE);
                                                                break;
                                                            }
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        case 2:
                                                            if (circlePosition < 2) {
                                                                smoothScroll(ACTION.DAGGLE);
                                                                break;
                                                            }
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        case 4:
                                                            if (circlePosition > 6) {
                                                                smoothScroll(ACTION.DAGGLE);
                                                                break;
                                                            }
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        case 5:
                                                            if (circlePosition <= 5) {
                                                                smoothScroll(ACTION.CLICK);
                                                                break;
                                                            }
                                                            break;
                                                        case 6:
                                                            if (circlePosition >= 4) {
                                                                smoothScroll(ACTION.DAGGLE);
                                                                break;
                                                            }
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        default:
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                    }
                                                }
                                                switch (this.selectedItem) {
                                                    case 0:
                                                        if (circlePosition <= 4) {
                                                            smoothScroll(ACTION.DAGGLE);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                    case 1:
                                                        if (circlePosition < 3) {
                                                            smoothScroll(ACTION.DAGGLE);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                    case 2:
                                                        if (circlePosition >= 2 && circlePosition <= 7) {
                                                            smoothScroll(ACTION.CLICK);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.DAGGLE);
                                                        break;
                                                        break;
                                                    case 3:
                                                        if (circlePosition > 6) {
                                                            smoothScroll(ACTION.DAGGLE);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                    case 4:
                                                        if (circlePosition > 5) {
                                                            smoothScroll(ACTION.DAGGLE);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                    case 5:
                                                        if (circlePosition >= 4) {
                                                            smoothScroll(ACTION.DAGGLE);
                                                            break;
                                                        }
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                }
                                            }
                                            switch (this.selectedItem) {
                                                case 0:
                                                    if (circlePosition <= 4) {
                                                        smoothScroll(ACTION.DAGGLE);
                                                        break;
                                                    }
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                case 1:
                                                    if (circlePosition < 3) {
                                                        smoothScroll(ACTION.DAGGLE);
                                                        break;
                                                    }
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                case 2:
                                                    if (circlePosition >= 2 && circlePosition <= 6) {
                                                        smoothScroll(ACTION.CLICK);
                                                        break;
                                                    }
                                                    smoothScroll(ACTION.DAGGLE);
                                                    break;
                                                    break;
                                                case 3:
                                                    if (circlePosition > 5) {
                                                        smoothScroll(ACTION.DAGGLE);
                                                        break;
                                                    }
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                case 4:
                                                    if (circlePosition >= 4) {
                                                        smoothScroll(ACTION.DAGGLE);
                                                        break;
                                                    }
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                            }
                                        }
                                        switch (this.selectedItem) {
                                            case 0:
                                                if (circlePosition > 4 && circlePosition < 8) {
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                }
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                                break;
                                            case 1:
                                                if (circlePosition >= 3 && circlePosition <= 6) {
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                }
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                                break;
                                            case 2:
                                                if (circlePosition >= 2 && circlePosition <= 5) {
                                                    smoothScroll(ACTION.CLICK);
                                                    break;
                                                }
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                                break;
                                            case 3:
                                                if (circlePosition >= 4) {
                                                    smoothScroll(ACTION.DAGGLE);
                                                    break;
                                                }
                                                smoothScroll(ACTION.CLICK);
                                                break;
                                        }
                                    }
                                    switch (this.selectedItem) {
                                        case 0:
                                            if (circlePosition != 5 && circlePosition != 6) {
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                            }
                                            smoothScroll(ACTION.CLICK);
                                            break;
                                            break;
                                        case 1:
                                            if (circlePosition != 5 && circlePosition != 3) {
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                            }
                                            smoothScroll(ACTION.CLICK);
                                            break;
                                            break;
                                        case 2:
                                            if (circlePosition != 2 && circlePosition != 3) {
                                                smoothScroll(ACTION.DAGGLE);
                                                break;
                                            }
                                            smoothScroll(ACTION.CLICK);
                                            break;
                                    }
                                }
                                switch (this.selectedItem) {
                                    case 0:
                                        if (circlePosition != 5) {
                                            smoothScroll(ACTION.DAGGLE);
                                            break;
                                        }
                                        smoothScroll(ACTION.CLICK);
                                        break;
                                    case 1:
                                        if (circlePosition != 3) {
                                            smoothScroll(ACTION.DAGGLE);
                                            break;
                                        }
                                        smoothScroll(ACTION.CLICK);
                                        break;
                                }
                            }
                            smoothScroll(ACTION.DAGGLE);
                        } else {
                            switch (this.selectedItem) {
                                case 0:
                                    if (circlePosition > 3 && circlePosition <= 5) {
                                        smoothScroll(ACTION.CLICK);
                                        break;
                                    }
                                    smoothScroll(ACTION.DAGGLE);
                                    break;
                                    break;
                                case 1:
                                    if (circlePosition >= 2 && circlePosition <= 4) {
                                        smoothScroll(ACTION.CLICK);
                                        break;
                                    }
                                    smoothScroll(ACTION.DAGGLE);
                                    break;
                                case 2:
                                    if (circlePosition >= 1 && circlePosition < 3) {
                                        smoothScroll(ACTION.CLICK);
                                        break;
                                    }
                                    smoothScroll(ACTION.DAGGLE);
                                    break;
                            }
                        }
                    }
                    smoothScroll(ACTION.DAGGLE);
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setOnSelectItemChange(OnSelectItemChange onSelect) {
        this.onSelect = onSelect;
    }
}