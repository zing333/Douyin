package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.nineton.onetake.util.DensityUtil;

public class CornerLinearLayout extends LinearLayout {
    private int leftPadding;
    private int mLastLeftPadding;
    private int mLastRightPadding;
    private Paint paint;
    private Paint paint2;
    private int rightPadding;
    private int roundHeight;
    private boolean roundLeft;
    private boolean roundRight;
    private int roundWidth;

    public CornerLinearLayout(Context context) {
        this(context, null);
    }

    public CornerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.roundWidth = 5;
        this.roundHeight = 5;
        this.roundLeft = true;
        this.roundRight = true;
        this.leftPadding = DensityUtil.dip2px(2.5f);
        this.rightPadding = DensityUtil.dip2px(2.5f);
        this.mLastLeftPadding = this.leftPadding;
        this.mLastRightPadding = this.rightPadding;
        init(context, attrs);
    }

    public void clearLeftRightPadding() {
        this.mLastLeftPadding = this.leftPadding;
        this.mLastRightPadding = this.rightPadding;
        this.leftPadding = 0;
        this.rightPadding = 0;
        invalidate();
    }

    public void restoreLeftRightPadding() {
        this.leftPadding = this.mLastLeftPadding;
        this.rightPadding = this.mLastRightPadding;
        invalidate();
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        float density = context.getResources().getDisplayMetrics().density;
        this.roundWidth = (int) (((float) this.roundWidth) * density);
        this.roundHeight = (int) (((float) this.roundHeight) * density);
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setAntiAlias(true);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.paint2 = new Paint();
        this.paint2.setXfermode(null);
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        if (this.roundLeft) {
            drawLeftPadding(canvas2);
            drawLeftUp(canvas2);
            drawLeftDown(canvas2);
        }
        if (this.roundRight) {
            drawRightPadding(canvas2);
            drawRightDown(canvas2);
            drawRightUp(canvas2);
        }
        canvas.drawBitmap(bitmap, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, this.paint2);
        bitmap.recycle();
    }

    private void drawRightPadding(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) (getWidth() - this.rightPadding), StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) getWidth(), StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) getWidth(), (float) getHeight());
        path.lineTo((float) (getWidth() - this.rightPadding), (float) getHeight());
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawLeftPadding(Canvas canvas) {
        Path path = new Path();
        path.moveTo(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) this.leftPadding, StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) this.leftPadding, (float) getHeight());
        path.lineTo(StaticLayoutUtil.DefaultSpacingadd, (float) getHeight());
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) this.leftPadding, (float) this.roundHeight);
        path.lineTo((float) this.leftPadding, StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) (this.roundWidth + this.leftPadding), StaticLayoutUtil.DefaultSpacingadd);
        path.arcTo(new RectF((float) this.leftPadding, StaticLayoutUtil.DefaultSpacingadd, (float) (this.leftPadding + (this.roundWidth * 2)), (float) (this.roundHeight * 2)), -90.0f, -90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) this.leftPadding, (float) (getHeight() - this.roundHeight));
        path.lineTo((float) this.leftPadding, (float) getHeight());
        path.lineTo((float) (this.roundWidth + this.leftPadding), (float) getHeight());
        path.arcTo(new RectF((float) this.leftPadding, (float) (getHeight() - (this.roundHeight * 2)), (float) (this.leftPadding + (this.roundWidth * 2)), (float) getHeight()), 90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) ((getWidth() - this.roundWidth) - this.rightPadding), (float) getHeight());
        path.lineTo((float) (getWidth() - this.rightPadding), (float) getHeight());
        path.lineTo((float) (getWidth() - this.rightPadding), (float) (getHeight() - this.roundHeight));
        path.arcTo(new RectF((float) ((getWidth() - (this.roundWidth * 2)) - this.rightPadding), (float) (getHeight() - (this.roundHeight * 2)), (float) (getWidth() - this.rightPadding), (float) getHeight()), StaticLayoutUtil.DefaultSpacingadd, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) (getWidth() - this.rightPadding), (float) this.roundHeight);
        path.lineTo((float) (getWidth() - this.rightPadding), StaticLayoutUtil.DefaultSpacingadd);
        path.lineTo((float) ((getWidth() - this.roundWidth) - this.rightPadding), StaticLayoutUtil.DefaultSpacingadd);
        path.arcTo(new RectF((float) ((getWidth() - (this.roundWidth * 2)) - this.rightPadding), StaticLayoutUtil.DefaultSpacingadd, (float) (getWidth() - this.rightPadding), (float) ((this.roundHeight * 2) + 0)), -90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }
}