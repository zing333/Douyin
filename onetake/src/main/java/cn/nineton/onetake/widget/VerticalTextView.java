package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.FontsUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.umeng.commonsdk.statistics.UMErrorCode;

import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.LogUtil;

public class VerticalTextView extends android.support.v7.widget.AppCompatTextView {
    boolean topDown;

    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        setPaintFlags((getPaintFlags() | 128) | 1);
        setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
        int gravity = getGravity();
//        if (Gravity.isVertical(gravity) && (gravity & UMErrorCode.E_UM_BE_DEFLATE_FAILED) == 80) {
        if (Gravity.isVertical(gravity) && (gravity & 112) == 80) {
            setGravity((gravity & 7) | 48);
            this.topDown = false;
            return;
        }
        this.topDown = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.d("huangweijie", String.format("VerticalTextView onmeasure width : %s ; height : %s", new Object[]{Integer.valueOf(getMeasuredWidth()), Integer.valueOf(getMeasuredHeight())}));
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        LogUtil.d("huangweijie", String.format("VerticalTextView setFrame l : %s ; t : %s ; r: %s ; b: %s", new Object[]{Integer.valueOf(l), Integer.valueOf(t), Integer.valueOf((b - t) + l), Integer.valueOf((r - l) + t)}));
        return super.setFrame(l, t, (b - t) + l, (r - l) + t);
    }

    public void draw(Canvas canvas) {
        if (this.topDown) {
            LogUtil.d("huangweijie", String.format("VerticalTextView draw if", new Object[0]));
            canvas.translate((float) getHeight(), StaticLayoutUtil.DefaultSpacingadd);
            canvas.rotate(90.0f);
        } else {
            LogUtil.d("huangweijie", String.format("VerticalTextView draw else", new Object[0]));
            canvas.translate(StaticLayoutUtil.DefaultSpacingadd, (float) getWidth());
            canvas.rotate(-90.0f);
        }
        canvas.clipRect(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, (float) getWidth(), (float) getHeight(), Op.REPLACE);
        super.draw(canvas);
    }
}