package cn.nineton.onetake.widget.video;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import cn.nineton.onetake.widget.StaticLayoutUtil;

public class JZResizeTextureView extends TextureView {
    protected static final String TAG = "JZResizeTextureView";
    private boolean isHorizon = false;
    private Integer mContentHeight;
    private float mContentRotation = StaticLayoutUtil.DefaultSpacingadd;
    private float mContentScaleMultiplier = 1.0f;
    private float mContentScaleX = 1.0f;
    private float mContentScaleY = 1.0f;
    private Integer mContentWidth;
    private float mPivotPointX = StaticLayoutUtil.DefaultSpacingadd;
    private float mPivotPointY = StaticLayoutUtil.DefaultSpacingadd;
    private ScaleType mScaleType = ScaleType.CENTER_CROP;
    private final Matrix mTransformMatrix = new Matrix();
    protected Point mVideoSize;
    protected Point mVideoSize2;

    public enum ScaleType {
        CENTER_CROP,
        TOP,
        BOTTOM,
        FILL,
        RIGHT,
        GIF
    }

    public JZResizeTextureView(Context context) {
        super(context);
        init();
    }

    public JZResizeTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.mVideoSize = new Point(0, 0);
        this.mVideoSize2 = new Point(0, 0);
    }

    public void setVideoSize(Point videoSize, Point videoSize2) {
        boolean change = false;
        if (!(videoSize == null || this.mVideoSize.equals(videoSize))) {
            this.mVideoSize = videoSize;
            change = true;
        }
        if (!(videoSize2 == null || this.mVideoSize2.equals(videoSize2))) {
            this.mVideoSize2 = videoSize2;
            change = true;
        }
        if (change) {
            requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean z = widthMeasureSpec > heightMeasureSpec && JZVideoPlayerManager.getCurrentJzvd().currentScreen == 2;
        this.isHorizon = z;
        if (this.isHorizon) {
            this.mContentHeight = Integer.valueOf(this.mVideoSize2.y);
            this.mContentWidth = Integer.valueOf(this.mVideoSize2.x);
        } else {
            this.mContentHeight = Integer.valueOf(this.mVideoSize.y);
            this.mContentWidth = Integer.valueOf(this.mVideoSize.x);
        }
        updateTextureViewSize();
    }

    public void updateTextureViewSize() {
        if (this.mContentWidth == null || this.mContentHeight == null) {
            throw new RuntimeException("null content size");
        }
        float viewWidth = (float) getMeasuredWidth();
        float viewHeight = (float) getMeasuredHeight();
        float contentWidth = (float) this.mContentWidth.intValue();
        float contentHeight = (float) this.mContentHeight.intValue();
        float viewRatio = viewHeight / viewWidth;
        float contentRatio = contentHeight / contentWidth;
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        if (this.isHorizon) {
            this.mContentScaleX = contentWidth / viewWidth;
            this.mContentScaleY = contentHeight / viewHeight;
            this.mPivotPointX = viewWidth / 2.0f;
            this.mPivotPointY = viewHeight / 2.0f;
        } else {
            float pivotPointX;
            float pivotPointY;
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
                case GIF:
                    if (viewWidth > StaticLayoutUtil.DefaultSpacingadd) {
                        scaleX = contentWidth / viewWidth;
                    }
                    if (viewHeight > StaticLayoutUtil.DefaultSpacingadd) {
                        scaleY = contentHeight / viewHeight;
                        break;
                    }
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
                case GIF:
                    pivotPointX = viewWidth / 2.0f;
                    pivotPointY = viewHeight / 2.0f;
                    break;
                case FILL:
                    pivotPointX = this.mPivotPointX;
                    pivotPointY = this.mPivotPointY;
                    break;
                case CENTER_CROP:
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
            float fitCoefX = 1.0f;
            float fitCoefY = 1.0f;
            switch (this.mScaleType) {
                case GIF:
                    if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
                        fitCoefY = viewHeight / (viewHeight * scaleY);
                        fitCoefX = fitCoefY;
                        break;
                    }
                    fitCoefY = viewWidth / (viewWidth * scaleX);
                    fitCoefX = fitCoefY;
                    break;
                case CENTER_CROP:
                case BOTTOM:
                case TOP:
                    if (JZVideoPlayerManager.getCurrentJzvd().currentScreen != 2) {
                        if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
                            fitCoefX = viewWidth / (viewWidth * scaleX);
                            fitCoefY = viewWidth / (viewWidth * scaleX);
                            break;
                        }
                        fitCoefX = viewWidth / (viewWidth * scaleX);
                        fitCoefY = viewWidth / (viewWidth * scaleX);
                        break;
                    } else if (contentRatio <= viewRatio) {
                        fitCoefX = viewWidth / (viewWidth * scaleX);
                        fitCoefY = fitCoefX;
                        break;
                    } else {
                        fitCoefY = viewHeight / (viewHeight * scaleY);
                        fitCoefX = fitCoefY;
                        break;
                    }
            }
            this.mContentScaleX = fitCoefX * scaleX;
            this.mContentScaleY = fitCoefY * scaleY;
            this.mPivotPointX = pivotPointX;
            this.mPivotPointY = pivotPointY;
        }
        if (contentWidth > StaticLayoutUtil.DefaultSpacingadd) {
            int margin = (int) ((viewHeight - this.mPivotPointY) - ((((1.0f * viewWidth) * contentHeight) / contentWidth) / 2.0f));
            if (margin < 0) {
                margin = 0;
            }
            updateJZViewsBottomMargins(margin);
        }
        updateMatrixScaleRotate();
    }

    private void updateJZViewsBottomMargins(int margin) {
        ((JZVideoPlayerStandard) JZVideoPlayerManager.getCurrentJzvd()).setProBarsBottomMargins(margin);
    }

    private void updateMatrixScaleRotate() {
        this.mTransformMatrix.reset();
        this.mTransformMatrix.setScale(this.mContentScaleX * this.mContentScaleMultiplier, this.mContentScaleY * this.mContentScaleMultiplier, this.mPivotPointX, this.mPivotPointY);
        this.mTransformMatrix.postRotate(this.mContentRotation, this.mPivotPointX, this.mPivotPointY);
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

    public void setScaleType(ScaleType mScaleType) {
        this.mScaleType = mScaleType;
    }
}