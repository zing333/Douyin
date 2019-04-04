package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
//import com.blink.academy.onetake.support.utils.FontsUtil;

import cn.nineton.onetake.util.FontsUtil;

public class AvenirNextCondensedRegularTextView extends AppCompatTextView {
    public AvenirNextCondensedRegularTextView(Context context) {
        this(context, null);
    }

    public AvenirNextCondensedRegularTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvenirNextCondensedRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        setPaintFlags((getPaintFlags() | 128) | 1);
        setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
    }
}