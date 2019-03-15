package cn.nineton.onetake.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import cn.nineton.onetake.util.FontsUtil;

public class AvenirNextRegularButton extends android.support.v7.widget.AppCompatButton {
    public AvenirNextRegularButton(Context context) {
        this(context, null);
    }

    public AvenirNextRegularButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvenirNextRegularButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        setPaintFlags((getPaintFlags() | 128) | 1);
        setTypeface(FontsUtil.setDefaultTypeFace());
    }
}