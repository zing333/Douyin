package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class AvatarOuterCircleView extends ImageView {
    public AvatarOuterCircleView(Context context) {
        this(context, null);
    }

    public AvatarOuterCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarOuterCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new LayoutParams(-1, -1));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(1.0f);
        mPaint.setColor(getContext().getResources().getColor(2131755074));
        canvas.drawCircle((float) radius, (float) radius, (float) (radius - 1), mPaint);
    }
}