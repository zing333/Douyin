package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.nineton.onetake.util.FontsUtil;

public class AvenirNextCondensedDemiBoldTextView extends AppCompatTextView {
    public AvenirNextCondensedDemiBoldTextView(Context context) {
        this(context, null);
    }

    public AvenirNextCondensedDemiBoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvenirNextCondensedDemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        if (!isInEditMode()) {
            setPaintFlags((getPaintFlags() | 128) | 1);
            setTypeface(FontsUtil.setAvenirNextCondensedDemiBoldTypeFace());
        }
    }
}