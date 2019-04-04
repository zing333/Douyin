package cn.nineton.onetake.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
//import butterknife.InjectView;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.TintColorUtil;
//
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;
import java.math.BigDecimal;

public class VolumeSlideView extends FrameLayout {
    private float SLIDE_UNIT;
    float down_x;
    float down_y;
    float last_move_y;
    private PressCallback mPressCallback;
    @BindView(R.id.volume_slide_iv)
    ImageView volume_slide_iv;
    @BindView(R.id.volume_slide_tv)
    TextView volume_slide_tv;

    public interface PressCallback {
        void onActionCancel(MotionEvent motionEvent);

        void onActionDown();

        void onActionMove(MotionEvent motionEvent, int i);

        void onActionUp(MotionEvent motionEvent);
    }

    public void setVolumeTvText(String text) {
        this.volume_slide_tv.setText(text);
    }

    public VolumeSlideView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VolumeSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VolumeSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        int volumeViewWidth = DensityUtil.dip2px(40.0f);
        int volumeViewHeight = DensityUtil.dip2px(45.0f);
        if (layoutParams == null) {
            layoutParams = new LayoutParams(volumeViewWidth, volumeViewHeight);
        }
        layoutParams.width = volumeViewWidth;
        layoutParams.height = volumeViewHeight;
        LayoutInflater.from(context).inflate(R.layout.layout_volume_slide_view, this);
//        ButterKnife.inject(this);
        ButterKnife.bind(this);
        this.SLIDE_UNIT = (((float) DensityUtil.dip2px(30.0f)) * 1.0f) / 9.0f;
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case 0:
                        VolumeSlideView.this.down_x = event.getRawX();
                        VolumeSlideView.this.down_y = event.getRawY();
                        VolumeSlideView.this.last_move_y = StaticLayoutUtil.DefaultSpacingadd;
                        if (VolumeSlideView.this.mPressCallback != null) {
                            VolumeSlideView.this.mPressCallback.onActionDown();
                            break;
                        }
                        break;
                    case 1:
                        if (VolumeSlideView.this.mPressCallback != null) {
                            VolumeSlideView.this.mPressCallback.onActionUp(event);
                            break;
                        }
                        break;
                    case 2:
                        float move_y = VolumeSlideView.this.dealMoveY(event.getRawY() - VolumeSlideView.this.down_y);
                        float result_move_y = BigDecimal.valueOf((double) move_y).subtract(BigDecimal.valueOf((double) VolumeSlideView.this.last_move_y)).setScale(1, 4).floatValue();
                        VolumeSlideView.this.last_move_y = move_y;
                        int moveTime = (int) (result_move_y / VolumeSlideView.this.SLIDE_UNIT);
                        if (VolumeSlideView.this.mPressCallback != null) {
                            VolumeSlideView.this.mPressCallback.onActionMove(event, moveTime);
                            break;
                        }
                        break;
                    case 3:
                        if (VolumeSlideView.this.mPressCallback != null) {
                            VolumeSlideView.this.mPressCallback.onActionCancel(event);
                            break;
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setVolumeSlideColor(int color) {
        TintColorUtil.tintDrawable(this.volume_slide_iv, color);
        this.volume_slide_tv.setTextColor(color);
    }

    public void setSLIDE_UNIT(float slide_unit) {
        this.SLIDE_UNIT = slide_unit;
    }

    private float dealMoveY(float move_y) {
        if (this.SLIDE_UNIT == StaticLayoutUtil.DefaultSpacingadd) {
            return move_y;
        }
        return this.SLIDE_UNIT * ((float) ((int) (move_y / this.SLIDE_UNIT)));
    }

    public void setPressCallback(PressCallback pressCallback) {
        this.mPressCallback = pressCallback;
    }
}