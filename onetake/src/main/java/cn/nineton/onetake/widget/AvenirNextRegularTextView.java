package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.nineton.onetake.util.FontsUtil;

public class AvenirNextRegularTextView extends AppCompatTextView {
    public AvenirNextRegularTextView(Context context) {
        this(context, null);
    }

    public AvenirNextRegularTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvenirNextRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        setPaintFlags((getPaintFlags() | 128) | 1);
        setTypeface(FontsUtil.setDefaultTypeFace());
    }
}