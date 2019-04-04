package cn.nineton.onetake.widget;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.SpannedUtil;
import cn.nineton.onetake.util.TextUtil;

public class LinkUnderlineSpan extends ClickableSpan {
    private int bgColor = App.getResource().getColor(R.color.colorAccent);
    private int end = 0;
    private OnClickListener listener;
    private int start = 0;
    private int textColor = App.getResource().getColor(R.color.colorGray);

    public LinkUnderlineSpan(int textColor, int start, int end, OnClickListener listener) {
        this.textColor = textColor;
        this.listener = listener;
        this.start = start;
        this.end = end;
    }

    public LinkUnderlineSpan(int textColor, int bgColor, int start, int end, OnClickListener listener) {
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.listener = listener;
        this.start = start;
        this.end = end;
    }

    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(this.textColor);
        ds.setFlags(1);
        ds.setFlags((ds.getFlags() | 128) | 1);
        ds.bgColor = this.bgColor;
        ds.linkColor = this.textColor;
        ds.setUnderlineText(true);
    }

    public void onClick(final View view) {
        if (TextUtil.isValidate(this.listener)) {
            CharSequence charSequence = ((TextView) view).getText();
            if (charSequence instanceof SpannableStringBuilder) {
                final SpannableStringBuilder ssb = (SpannableStringBuilder) ((TextView) view).getText();
                ssb.setSpan(new LinkUnderlineSpan(this.textColor, App.getResource().getColor(R.color.colorLightGray), this.start, this.end, null), this.start, this.end, SpannedUtil.flags);
                ((TextView) view).setText(ssb);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ssb.setSpan(new LinkUnderlineSpan(LinkUnderlineSpan.this.textColor, LinkUnderlineSpan.this.start, LinkUnderlineSpan.this.end, null), LinkUnderlineSpan.this.start, LinkUnderlineSpan.this.end, SpannedUtil.flags);
                        ((TextView) view).setText(ssb);
                    }
                }, 200);
            } else if (charSequence instanceof SpannableString) {
                final SpannableString ssb2 = (SpannableString) ((TextView) view).getText();
                ssb2.setSpan(new LinkUnderlineSpan(this.textColor, App.getResource().getColor(R.color.colorLightGray), this.start, this.end, null), this.start, this.end, SpannedUtil.flags);
                ((TextView) view).setText(ssb2);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ssb2.setSpan(new LinkUnderlineSpan(LinkUnderlineSpan.this.textColor, LinkUnderlineSpan.this.start, LinkUnderlineSpan.this.end, null), LinkUnderlineSpan.this.start, LinkUnderlineSpan.this.end, SpannedUtil.flags);
                        ((TextView) view).setText(ssb2);
                    }
                }, 200);
            }
            if (this.listener != null) {
                this.listener.onClick(view);
                return;
            }
            return;
        }
//        AnonymousClass3 anonymousClass3 = new OnClickListener() {
//            public void onClick(View v) {
//            }
//        };
    }
}