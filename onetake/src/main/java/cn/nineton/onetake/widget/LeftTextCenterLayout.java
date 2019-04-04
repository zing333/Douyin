package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;

public class LeftTextCenterLayout extends RelativeLayout {
    @BindView(R.id.left_tv)
    TextView left_tv;
    @BindView(R.id.right_tv)
    TextView right_tv;

    public LeftTextCenterLayout(Context context) {
        this(context, null);
    }

    public LeftTextCenterLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftTextCenterLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        this.right_tv.setText("\"");
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_left_text_center, this, true);
        ButterKnife.bind(this);
    }

    public void removeCenterInParent() {
        LayoutParams layoutParams = (LayoutParams) this.left_tv.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        layoutParams.removeRule(13);
        layoutParams.addRule(15);
        this.left_tv.setLayoutParams(layoutParams);
    }

    public void restoreCenterInParent() {
        LayoutParams layoutParams = (LayoutParams) this.left_tv.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        layoutParams.addRule(13);
        this.left_tv.setLayoutParams(layoutParams);
    }

    public void setText(String content) {
        this.left_tv.setText(content);
    }

    public String getTextString() {
        return (String) this.left_tv.getText();
    }

    public void setTextColor(int color) {
        this.left_tv.setTextColor(color);
        this.right_tv.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.left_tv.setTextSize(size);
        this.right_tv.setTextSize(size);
    }
}