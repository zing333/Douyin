package cn.nineton.onetake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TintColorUtil;
//
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;

public class LeftImageRightTextLayout extends LinearLayout {
    private static final int DEFAULT_LEFT_IMAGE_PADDING = 0;
    private static final int DEFAULT_LEFT_IMAGE_WIDTH = DensityUtil.dip2px(20.0f);
    private static final int DEFAULT_RIGHT_TEXT_SIZE = App.getResource().getDimensionPixelSize(R.dimen.fontRegular12);
    private static final String TAG = LeftImageRightTextLayout.class.getSimpleName();
    @BindView(R.id.left_iv)
    ImageView left_iv;
    private int mLeftImageHeight;
    private int mLeftImagePadding;
    private int mLeftImageRes;
    private int mLeftImageTintColor;
    private int mLeftImageWidth;
    private int mRightTextColor;
    private int mRightTextSize;
    private String mRightTextStr;
    @BindView(R.id.right_tv)
    TextView right_tv;

    public LeftImageRightTextLayout(Context context) {
        this(context, null);
    }

    public LeftImageRightTextLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftImageRightTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        LeftImageRightTextLayout.this.left_iv.setAlpha(0.3f);
                        LeftImageRightTextLayout.this.right_tv.setAlpha(0.3f);
                        break;
                    case 1:
                    case 3:
                        if (LeftImageRightTextLayout.this.getHandler() == null) {
                            LeftImageRightTextLayout.this.left_iv.setAlpha(1.0f);
                            LeftImageRightTextLayout.this.right_tv.setAlpha(1.0f);
                            break;
                        }
                        LeftImageRightTextLayout.this.getHandler().postDelayed(new Runnable() {
                            public void run() {
                                LeftImageRightTextLayout.this.left_iv.setAlpha(1.0f);
                                LeftImageRightTextLayout.this.right_tv.setAlpha(1.0f);
                            }
                        }, 100);
                        break;
                }
                return false;
            }
        });
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_left_image_right_text, this, true);
        ButterKnife.bind(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftImageRightTextLayout);
        this.mLeftImageWidth = typedArray.getDimensionPixelOffset(R.styleable.LeftImageRightTextLayout_leftImageWidth, DEFAULT_LEFT_IMAGE_WIDTH);
        this.mLeftImageHeight = typedArray.getDimensionPixelOffset(R.styleable.LeftImageRightTextLayout_leftImageHeight, DEFAULT_LEFT_IMAGE_WIDTH);
        this.mLeftImageTintColor = typedArray.getColor(R.styleable.LeftImageRightTextLayout_leftImageTint, ContextCompat.getColor(context, R.color.colorWhite));
        this.mLeftImagePadding = typedArray.getDimensionPixelOffset(R.styleable.LeftImageRightTextLayout_leftImagePadding, 0);
        this.mLeftImageRes = typedArray.getResourceId(R.styleable.LeftImageRightTextLayout_leftImageRes, R.drawable.icon_20_delete);
        this.mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.LeftImageRightTextLayout_rightTextSize, DEFAULT_RIGHT_TEXT_SIZE);
        this.mRightTextColor = typedArray.getColor(R.styleable.LeftImageRightTextLayout_rightTextColor, ContextCompat.getColor(context, R.color.colorWhite));
        this.mRightTextStr = typedArray.getString(R.styleable.LeftImageRightTextLayout_rightTextStr);
        typedArray.recycle();
        setGravity(17);
        setOrientation(LinearLayout.VERTICAL);
        float textSize = this.right_tv.getTextSize();
        LogUtil.d(TAG, String.format("leftImageWidth : %s , leftImageHeight : %s , mRightTextSize : %s , textSize : %s , mRightTextStr : %s , mLeftImageTintColor : %s ", new Object[]{Integer.valueOf(this.mLeftImageWidth), Integer.valueOf(this.mLeftImageHeight), Integer.valueOf(this.mRightTextSize), Float.valueOf(textSize), this.mRightTextStr, Integer.valueOf(this.mLeftImageTintColor)}));
        LayoutParams layoutParams = (LayoutParams) this.left_iv.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(this.mLeftImageWidth, this.mLeftImageHeight);
        }
        layoutParams.width = this.mLeftImageWidth;
        layoutParams.height = this.mLeftImageHeight;
        this.left_iv.setLayoutParams(layoutParams);
        this.left_iv.setImageResource(this.mLeftImageRes);
        if (this.mLeftImagePadding != 0) {
            this.left_iv.setPadding(this.mLeftImagePadding, this.mLeftImagePadding, this.mLeftImagePadding, this.mLeftImagePadding);
        }
        TintColorUtil.tintDrawable(this.left_iv, this.mLeftImageTintColor);
        this.right_tv.setTextColor(this.mRightTextColor);
        this.right_tv.setTextSize(0, (float) this.mRightTextSize);
        if (!TextUtils.isEmpty(this.mRightTextStr)) {
            this.right_tv.setText(this.mRightTextStr);
        }
    }

    public float getTextWidth() {
        return this.right_tv.getPaint().measureText(this.right_tv.getText().toString());
    }
}