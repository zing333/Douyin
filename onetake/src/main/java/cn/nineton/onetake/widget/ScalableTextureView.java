package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

public abstract class ScalableTextureView extends TextureView {
    private Integer mContentHeight;
    private float mContentRotation = StaticLayoutUtil.DefaultSpacingadd;
    private float mContentScaleMultiplier = 1.0f;
    private float mContentScaleX = 1.0f;
    private float mContentScaleY = 1.0f;
    private Integer mContentWidth;
    private int mContentX = 0;
    private int mContentY = 0;
    private float mPivotPointX = StaticLayoutUtil.DefaultSpacingadd;
    private float mPivotPointY = StaticLayoutUtil.DefaultSpacingadd;
    private ScaleType mScaleType = ScaleType.CENTER_CROP;
    private final Matrix mTransformMatrix = new Matrix();

    public enum ScaleType {
        CENTER_CROP,
        TOP,
        BOTTOM,
        FILL,
        RIGHT,
        BANNER
    }

    public ScalableTextureView(Context context) {
        super(context);
    }

    public ScalableTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ScalableTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScaleType(ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mContentWidth != null && this.mContentHeight != null) {
            updateTextureViewSize();
        }
    }

    public void updateTextureViewSize() {
        if (this.mContentWidth == null || this.mContentHeight == null) {
            throw new RuntimeException("null content size");
        }
        float pivotPointX;
        float pivotPointY;
        float viewWidth = (float) getMeasuredWidth();
        float viewHeight = (float) getMeasuredHeight();
        float contentWidth = (float) this.mContentWidth.intValue();
        float contentHeight = (float) this.mContentHeight.intValue();
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        switch (this.mScaleType) {
            case RIGHT:
                if (contentWidth <= viewWidth || contentHeight <= viewHeight) {
                    if (viewWidth > contentWidth) {
                        scaleY = (viewWidth / contentWidth) / (viewHeight / contentHeight);
                        break;
                    }
                }
                scaleX = contentWidth / viewWidth;
                break;
            case FILL:
                if (viewWidth <= viewHeight) {
                    scaleY = (viewWidth * contentHeight) / (viewHeight * contentWidth);
                    break;
                } else {
                    scaleX = (viewHeight * contentWidth) / (viewWidth * contentHeight);
                    break;
                }
            case CENTER_CROP:
                if (viewWidth > StaticLayoutUtil.DefaultSpacingadd) {
                    scaleX = contentWidth / viewWidth;
                }
                if (viewHeight > StaticLayoutUtil.DefaultSpacingadd) {
                    scaleY = contentHeight / viewHeight;
                    break;
                }
                break;
            case BANNER:
                scaleX = 1.0f;
                scaleY = (contentHeight / contentWidth) / (viewHeight / viewWidth);
                break;
            case BOTTOM:
            case TOP:
                if (contentWidth <= viewWidth || contentHeight <= viewHeight) {
                    if (contentWidth >= viewWidth || contentHeight >= viewHeight) {
                        if (viewWidth <= contentWidth) {
                            if (viewHeight > contentHeight) {
                                scaleX = (viewHeight / contentHeight) / (viewWidth / contentWidth);
                                break;
                            }
                        }
                        scaleY = (viewWidth / contentWidth) / (viewHeight / contentHeight);
                        break;
                    }
                    scaleY = viewWidth / contentWidth;
                    scaleX = viewHeight / contentHeight;
                    break;
                }
                scaleX = contentWidth / viewWidth;
                scaleY = contentHeight / viewHeight;
                break;
        }
        switch (this.mScaleType) {
            case RIGHT:
                pivotPointX = StaticLayoutUtil.DefaultSpacingadd;
                pivotPointY = viewHeight;
                break;
            case FILL:
                pivotPointX = this.mPivotPointX;
                pivotPointY = this.mPivotPointY;
                break;
            case CENTER_CROP:
            case BANNER:
                pivotPointX = viewWidth / 2.0f;
                pivotPointY = viewHeight / 2.0f;
                break;
            case BOTTOM:
                pivotPointX = viewWidth;
                pivotPointY = viewHeight;
                break;
            case TOP:
                pivotPointX = StaticLayoutUtil.DefaultSpacingadd;
                pivotPointY = StaticLayoutUtil.DefaultSpacingadd;
                break;
            default:
                throw new IllegalStateException("pivotPointX, pivotPointY for ScaleType " + this.mScaleType + " are not defined");
        }
        float fitCoef = 1.0f;
        switch (this.mScaleType) {
            case CENTER_CROP:
            case BOTTOM:
            case TOP:
                if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
                    fitCoef = viewHeight / (viewHeight * scaleY);
                    break;
                } else {
                    fitCoef = viewWidth / (viewWidth * scaleX);
                    break;
                }
        }
        this.mContentScaleX = fitCoef * scaleX;
        this.mContentScaleY = fitCoef * scaleY;
        this.mPivotPointX = pivotPointX;
        this.mPivotPointY = pivotPointY;
        updateMatrixScaleRotate();
    }

    private void updateMatrixScaleRotate() {
        this.mTransformMatrix.reset();
        this.mTransformMatrix.setScale(this.mContentScaleX * this.mContentScaleMultiplier, this.mContentScaleY * this.mContentScaleMultiplier, this.mPivotPointX, this.mPivotPointY);
        this.mTransformMatrix.postRotate(this.mContentRotation, this.mPivotPointX, this.mPivotPointY);
        setTransform(this.mTransformMatrix);
    }

    private void updateMatrixTranslate() {
        float scaleX = this.mContentScaleX * this.mContentScaleMultiplier;
        float scaleY = this.mContentScaleY * this.mContentScaleMultiplier;
        this.mTransformMatrix.reset();
        this.mTransformMatrix.setScale(scaleX, scaleY, this.mPivotPointX, this.mPivotPointY);
        this.mTransformMatrix.postTranslate((float) this.mContentX, (float) this.mContentY);
        setTransform(this.mTransformMatrix);
    }

    public void setRotation(float degrees) {
        this.mContentRotation = degrees;
        updateMatrixScaleRotate();
    }

    public float getRotation() {
        return this.mContentRotation;
    }

    public void setPivotX(float pivotX) {
        this.mPivotPointX = pivotX;
    }

    public void setPivotY(float pivotY) {
        this.mPivotPointY = pivotY;
    }

    public float getPivotX() {
        return this.mPivotPointX;
    }

    public float getPivotY() {
        return this.mPivotPointY;
    }

    public float getContentAspectRatio() {
        return (this.mContentWidth == null || this.mContentHeight == null) ? StaticLayoutUtil.DefaultSpacingadd : ((float) this.mContentWidth.intValue()) / ((float) this.mContentHeight.intValue());
    }

    public final void setContentX(float x) {
        this.mContentX = ((int) x) - ((getMeasuredWidth() - getScaledContentWidth().intValue()) / 2);
        updateMatrixTranslate();
    }

    public final void setContentY(float y) {
        this.mContentY = ((int) y) - ((getMeasuredHeight() - getScaledContentHeight().intValue()) / 2);
        updateMatrixTranslate();
    }

    protected final float getContentX() {
        return (float) this.mContentX;
    }

    protected final float getContentY() {
        return (float) this.mContentY;
    }

    public void centralizeContent() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int scaledContentWidth = getScaledContentWidth().intValue();
        int scaledContentHeight = getScaledContentHeight().intValue();
        this.mContentX = 0;
        this.mContentY = 0;
        updateMatrixScaleRotate();
    }

    public Integer getScaledContentWidth() {
        return Integer.valueOf((int) ((this.mContentScaleX * this.mContentScaleMultiplier) * ((float) getMeasuredWidth())));
    }

    public Integer getScaledContentHeight() {
        return Integer.valueOf((int) ((this.mContentScaleY * this.mContentScaleMultiplier) * ((float) getMeasuredHeight())));
    }

    public float getContentScale() {
        return this.mContentScaleMultiplier;
    }

    public void setContentScale(float contentScale) {
        this.mContentScaleMultiplier = contentScale;
        updateMatrixScaleRotate();
    }

    public final void setContentHeight(int height) {
        this.mContentHeight = Integer.valueOf(height);
    }

    public final Integer getContentHeight() {
        return this.mContentHeight;
    }

    public final void setContentWidth(int width) {
        this.mContentWidth = Integer.valueOf(width);
    }

    public final Integer getContentWidth() {
        return this.mContentWidth;
    }
}