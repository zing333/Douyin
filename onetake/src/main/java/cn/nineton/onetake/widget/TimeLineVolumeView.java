package cn.nineton.onetake.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.widgets.TimeLineVolumeView$.Lambda.1;
import com.nineoldandroids.view.ViewPropertyAnimator;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;

public class TimeLineVolumeView extends RelativeLayout {
    private View bgView;
    private int endWidth;
    private boolean hasAudio;
    private LayoutParams lp;
    private Rect tempRect;
    private TextView tv;
    private int viewWidth;

    public TimeLineVolumeView(Context context) {
        this(context, null);
    }

    public TimeLineVolumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.timeline_volume_content, this);
        this.tv = (TextView) findViewById(R.id.tv);
        this.bgView = findViewById(R.id.volume_bg);
        this.lp = (LayoutParams) this.bgView.getLayoutParams();
        this.tempRect = new Rect();
        this.endWidth = (DensityUtil.dip2px(5.0f) * 2) + DensityUtil.dip2px(15.0f);
    }

    public int getViewWidth() {
        return (this.endWidth + getTextWidth()) + DensityUtil.dip2px(5.0f);
    }

    public void resetView() {
        this.tv.setAlpha(1.0f);
        this.lp.width = getViewWidth();
        this.bgView.setLayoutParams(this.lp);
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
        if (hasAudio) {
            this.tv.setText(getContext().getString(R.string.BUTTON_PLAYER_TAP_FOR_SOUND));
        } else {
            this.tv.setText(getContext().getString(R.string.TEXT_TIMELINE_VIDEO_NO_AUDIO));
        }
        resetView();
    }

    public void doAnim() {
        if (this.hasAudio) {
            ViewPropertyAnimator.animate(this.tv).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(100).setInterpolator(new LinearInterpolator()).start();
            ValueAnimator widthAnimation = ValueAnimator.ofInt(new int[]{getViewWidth(), this.endWidth});
//            widthAnimation.addUpdateListener(1.lambdaFactory$(this));
            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lp.width = (Integer) animation.getAnimatedValue();
                    bgView.setLayoutParams(lp);
                }
            });
            widthAnimation.setDuration(400);
            widthAnimation.start();
        }
    }

    private /* synthetic */ void lambda$doAnim$0(ValueAnimator animation) {
        this.lp.width = ((Integer) animation.getAnimatedValue()).intValue();
        this.bgView.setLayoutParams(this.lp);
    }

    private int getTextWidth() {
        String a = this.tv.getText().toString();
        this.tv.getPaint().getTextBounds(a, 0, a.length(), this.tempRect);
        return this.tempRect.width();
    }
}