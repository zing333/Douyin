package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
//import butterknife.InjectView;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.TintColorUtil;

//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;
//import com.blink.academy.onetake.widgets.ProgressBar.IMCameraProgressBar;
//import com.blink.academy.onetake.widgets.TextView.AvenirNextCondensedRegularTextView;

public class FilterEffectSetRelativeLayout extends RelativeLayout {
    private Drawable drawable;
    @BindView(R.id.filter_effect_iv)
    ImageView filter_effect_iv;
    @BindView(R.id.filter_effect_pb)
    IMCameraProgressBar filter_effect_pb;
    @BindView(R.id.filter_effect_text)
    AvenirNextCondensedRegularTextView filter_effect_text;
    private boolean isSelect;
    private boolean is_show_circle_line;

    public FilterEffectSetRelativeLayout(Context context) {
        super(context);
    }

    public FilterEffectSetRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FilterEffectSetRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        ButterKnife.bind(this, LayoutInflater.from(context).inflate(R.layout.layout_filter_effect_set, this, true));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FilterEffectSetRelativeLayout);
        CharSequence text = a.getText(R.styleable.FilterEffectSetRelativeLayout_text);
        if (text != null) {
            this.filter_effect_text.setText(text);
        }
        this.drawable = a.getDrawable(R.styleable.FilterEffectSetRelativeLayout_drawable);
        if (this.drawable != null) {
            this.filter_effect_iv.setImageDrawable(this.drawable);
        }
        TintColorUtil.tintDrawable(context, this.filter_effect_iv, R.color.colorDate);
        this.is_show_circle_line = a.getBoolean(R.styleable.FilterEffectSetRelativeLayout_is_show_circle_line, true);
        if (!this.is_show_circle_line) {
            this.filter_effect_pb.setVisibility(GONE);
        }
        a.recycle();
        setClipChildren(false);
        setClipToPadding(false);
        this.filter_effect_pb.setOutLineMultiple(3);
    }

    public float getProgressValue() {
        return this.filter_effect_pb.getValue();
    }

    public int getDefaultMultiplier() {
        return this.filter_effect_pb.getDefaultMultiplier();
    }

    public void setProgressValue(float value) {
        if (value == StaticLayoutUtil.DefaultSpacingadd) {
            setProgressBorderWidth(0.5f);
        } else {
            setProgressBorderWidth(1.0f);
        }
        this.filter_effect_pb.setFilterEffectValue(value);
    }

    public void setProgressDefaultMultiplier(int multiplier) {
        this.filter_effect_pb.setDefaultMultiplier(multiplier);
    }

    private void setProgressBorderWidth(float borderWidth) {
        this.filter_effect_pb.setBorderWidth(borderWidth);
    }

    public float getProgressMaxValue() {
        return (float) this.filter_effect_pb.getMax();
    }

    public String getText() {
        return this.filter_effect_text.getText().toString();
    }

    public void setFilterEffectImage(int resId) {
        this.filter_effect_iv.setImageResource(resId);
    }

    public void setFilterEffectText(String text) {
        this.filter_effect_text.setText(text);
    }

    public void setWhiteColor(Context context) {
        TintColorUtil.tintDrawable(context, this.filter_effect_iv, R.color.colorWhite);
        if (this.is_show_circle_line) {
            this.filter_effect_iv.setBackgroundResource(R.drawable.shape_filter_effect_whilte_1px);
        } else {
            this.filter_effect_iv.setBackgroundResource(R.drawable.shape_filter_effect_whilte_0px);
        }
        this.isSelect = true;
        this.filter_effect_text.setTextColor(getResources().getColor(R.color.colorWhite));
        this.filter_effect_pb.setAlpha(1.0f);
    }

    public void setDateColor(Context context) {
        TintColorUtil.tintDrawable(context, this.filter_effect_iv, R.color.colorDate);
        if (this.is_show_circle_line) {
            this.filter_effect_iv.setBackgroundResource(R.drawable.shape_filter_effect_gray666_1px);
        } else {
            this.filter_effect_iv.setBackgroundResource(R.drawable.shape_filter_effect_gray666_0px);
        }
        this.isSelect = false;
        this.filter_effect_text.setTextColor(getResources().getColor(R.color.colorDate));
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }
}