package cn.nineton.onetake.widget;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import com.blink.academy.onetake.App;
import com.blink.academy.onetake.support.utils.FontsUtil;
import com.blink.academy.onetake.support.utils.TextUtil;

import cn.nineton.onetake.App;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.TextUtil;

public class LinkSpan extends TouchableSpan {
    private OnClickListener listener;
    private int textColor = App.getResource().getColor(R.color.colorBlack);

    public LinkSpan(OnClickListener listener) {
        this.listener = listener;
    }

    public LinkSpan(OnClickListener listener, int textColor) {
        this.listener = listener;
        this.textColor = textColor;
    }

    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(this.textColor);
        ds.setFlags(1);
        ds.setFlags((ds.getFlags() | 128) | 1);
        ds.setTypeface(FontsUtil.setDefaultTypeFace());
        ds.setUnderlineText(false);
    }

    public void onClick(View view) {
        if (TextUtil.isValidate(this.listener)) {
            this.listener.onClick(view);
        }
    }
}