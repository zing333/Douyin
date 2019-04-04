package cn.nineton.onetake.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.StatusBarModel;
import cn.nineton.onetake.widget.AvenirNextRegularTextView;
import cn.nineton.onetake.widget.StaticLayoutUtil;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.model.StatusBarModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.FontsUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.widgets.TextView.AvenirNextRegularTextView;

public class AppMessage {
    public static final int LENGTH_LONG = 5000;
    public static final int LENGTH_SHORT = 2000;
    public static final int LENGTH_STICKY = -1;
    public static final int PRIORITY_HIGH = Integer.MAX_VALUE;
    public static final int PRIORITY_LOW = Integer.MIN_VALUE;
    public static final int PRIORITY_NORMAL = 0;
    public static final int SHOW_TYPE_DEFAULT = 0;
    public static final int SHOW_TYPE_NO_STATUSBAR_CHANGE = 1;
    public static final Style STYLE_ALERT = new Style(2000, 2131755045);
    public static final Style STYLE_INFO = new Style(2000, 2131755045);
    public static final Style STYLE_SUCCESS = new Style(2000, 2131755085);
    public static final Style STYLE_Tip = new Style(2000, 2131755050);
    private final Activity mActivity;
    private int mDuration = 2000;
    private boolean mFloating;
    Animation mInAnimation;
    private LayoutParams mLayoutParams;
    Animation mOutAnimation;
    private ViewGroup mParent;
    int mPriority = 0;
    private int mShowType = 0;
    private Style mStyle;
    private View mView;
    public String messageText;

    public static class Style {
        private final int background;
        private final int duration;

        public Style(int bgColorResId) {
            this(2000, bgColorResId);
        }

        public Style(int duration, int bgColorResId) {
            this.duration = duration;
            this.background = bgColorResId;
        }

        public int getDuration() {
            return this.duration;
        }

        public int getBackground() {
            return this.background;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Style)) {
                return false;
            }
            Style style = (Style) o;
            if (style.duration == this.duration && style.background == this.background) {
                return true;
            }
            return false;
        }
    }

    public int getShowType() {
        return this.mShowType;
    }

    public AppMessage(Activity activity) {
        this.mActivity = activity;
    }

    public static AppMessage makeAlertText(Activity context, CharSequence text) {
        LogUtil.d("lookforcolorandstate", "color : 2131755045 , state : 0");
        return makeText(context, text, STYLE_ALERT, 2130968683, 2131755096);
    }

    public static AppMessage makeSucText(Activity context, CharSequence text) {
        LogUtil.d("lookforcolorandstate", "color : 2131755045 , state : 0");
        StatusBarModel.getInstance().setColorAndState(R.color.colorAlert, 0);
        return makeText(context, text, STYLE_ALERT, 2130968683, 2131755096);
    }

    public static AppMessage makeAlertTextNoStatusBarChange(Activity context, CharSequence text, int marginTop) {
        return makeTextNoStatusBarChange(context, text, STYLE_ALERT, 2130968683, 2131755096, marginTop);
    }

    public static AppMessage makeAlertTextNoStatusBarChange(Activity context, CharSequence text) {
        return makeTextNoStatusBarChange(context, text, STYLE_ALERT, 2130968683, 2131755096);
    }

    public static AppMessage makeAlertTextHigher(Activity context, CharSequence text) {
        return makeTextHigher(context, text, STYLE_ALERT, 2130968683, 2131755096);
    }

    public static AppMessage makeAlertTextHigherNoStatusBarChange(Activity context, CharSequence text) {
        return makeTextHigherNoStatusBarChange(context, text, STYLE_ALERT, 2130968683, 2131755096);
    }

    public static AppMessage makeInfoText(Activity context, CharSequence text) {
        return makeText(context, text, STYLE_INFO, 2130968683, 2131755050);
    }

    public static AppMessage makeSuccessTextNoStatusBarChange(Activity context, CharSequence text, int marginTop) {
        return makeTextNoStatusBarChange(context, text, STYLE_SUCCESS, 2130968683, 2131755096, marginTop);
    }

    public static AppMessage makeSuccessText(Activity context, CharSequence text) {
        return makeText(context, text, STYLE_SUCCESS, 2130968683, 2131755096);
    }

    public static AppMessage makeTipText(Activity context, CharSequence text) {
        return makeText(context, text, STYLE_Tip, 2130968683, 2131755096);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style) {
        return makeText(context, text, style, 2130968683, 2131755096);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int textColorResId) {
        return makeText(context, text, style, 2130968683, textColorResId);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, 2130968683, textColorResId, clickListener);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, float textSize, int textColorResId) {
        return makeText(context, text, style, 2130968683, textSize, textColorResId);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, float textSize, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, 2130968683, textSize, textColorResId, clickListener);
    }

    public static AppMessage makeTextNoStatusBarChange(Activity context, CharSequence text, Style style, int layoutId, int textColorResId) {
        return makeTextNoStatusBarChange(context, text, style, layoutId, textColorResId, 0);
    }

    public static AppMessage makeTextNoStatusBarChange(Activity context, CharSequence text, Style style, int layoutId, int textColorResId, int marginTop) {
        return makeTextNoStatusBarChange(context, text, style, ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null), true, textColorResId, marginTop);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int layoutId, int textColorResId) {
        return makeText(context, text, style, ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null), true, textColorResId);
    }

    public static AppMessage makeTextHigher(Activity context, CharSequence text, Style style, int layoutId, int textColorResId) {
        View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
        v.setMinimumHeight((int) (context.getResources().getDimension(R.dimen._63dp) + 0.5f));
        return makeText(context, text, style, v, true, textColorResId);
    }

    public static AppMessage makeTextHigherNoStatusBarChange(Activity context, CharSequence text, Style style, int layoutId, int textColorResId) {
        View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
        v.setMinimumHeight((int) (context.getResources().getDimension(R.dimen._63dp) + 0.5f));
        return makeTextNoStatusBarChange(context, text, style, v, true, textColorResId, 0);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int layoutId, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null), true, textColorResId, clickListener);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int layoutId, float textSize, int textColorResId) {
        return makeText(context, text, style, ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null), true, textSize, textColorResId);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, int layoutId, float textSize, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null), true, textSize, textColorResId, clickListener);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, View customView, int textColorResId) {
        return makeText(context, text, style, customView, false, textColorResId);
    }

    public static AppMessage makeText(Activity context, CharSequence text, Style style, View customView, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, customView, false, textColorResId, clickListener);
    }

    private static AppMessage makeTextNoStatusBarChange(Activity context, CharSequence text, Style style, View view, boolean floating, int textColorResId, int marginTop) {
        return makeTextNoStatusBarChange(context, text, style, view, floating, StaticLayoutUtil.DefaultSpacingadd, textColorResId, marginTop);
    }

    private static AppMessage makeText(Activity context, CharSequence text, Style style, View view, boolean floating, int textColorResId) {
        return makeText(context, text, style, view, floating, (float) StaticLayoutUtil.DefaultSpacingadd, textColorResId);
    }

    private static AppMessage makeText(Activity context, CharSequence text, Style style, View view, boolean floating, int textColorResId, OnClickListener clickListener) {
        return makeText(context, text, style, view, floating, StaticLayoutUtil.DefaultSpacingadd, textColorResId, clickListener);
    }

    private static AppMessage makeTextNoStatusBarChange(Activity context, CharSequence text, Style style, View view, boolean floating, float textSize, int textColorResId, int marginTop) {
        AppMessage result = new AppMessage(context);
        result.mStyle = style;
        result.mShowType = 1;
        view.setBackgroundResource(R.color.colorTransparent);
        AvenirNextRegularTextView tv = (AvenirNextRegularTextView) view.findViewById(android.R.id.message);//16908299);
        if (textSize > StaticLayoutUtil.DefaultSpacingadd) {
            tv.setTextSize(textSize);
        }
        tv.setText(text);
        tv.setTextColor(App.getResource().getColor(textColorResId));
        tv.setBackgroundDrawable(getRoundShape(App.getResource().getColor(style.background)));
        tv.getPaint().setFakeBoldText(true);
        FontsUtil.applyARegularFont(context, tv);
        if (marginTop != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
            layoutParams.topMargin = marginTop;
            tv.setLayoutParams(layoutParams);
        }
        result.mView = view;
        result.mDuration = style.duration;
        result.mFloating = floating;
        result.messageText = text.toString();
        result.mStyle = style;
        return result;
    }

    private static AppMessage makeText(Activity context, CharSequence text, Style style, View view, boolean floating, float textSize, int textColorResId) {
        AppMessage result = new AppMessage(context);
        result.mStyle = style;
        view.setBackgroundResource(R.color.colorTransparent);
        AvenirNextRegularTextView tv = view.findViewById(android.R.id.message);//16908299);
        if (textSize > StaticLayoutUtil.DefaultSpacingadd) {
            tv.setTextSize(textSize);
        }
        tv.setText(text);
        tv.setTextColor(App.getResource().getColor(textColorResId));
        tv.setBackgroundDrawable(getRoundShape(App.getResource().getColor(style.background)));
        tv.getPaint().setFakeBoldText(true);
        FontsUtil.applyARegularFont(context, tv);
        result.mView = view;
        result.mDuration = style.duration;
        result.mFloating = floating;
        result.messageText = text.toString();
        result.mStyle = style;
        LogUtil.d("ljc", "time" + System.currentTimeMillis());
        return result;
    }

    public static GradientDrawable getRoundShape(int color) {
        GradientDrawable gd = new GradientDrawable();
        int roundRadius = DensityUtil.dip2px(15.0f);
        gd.setColor(color);
        gd.setCornerRadius((float) roundRadius);
        return gd;
    }

    public Style getStyle() {
        return this.mStyle;
    }

    private static AppMessage makeText(Activity context, CharSequence text, Style style, View view, boolean floating, float textSize, int textColorResId, OnClickListener clickListener) {
        AppMessage result = new AppMessage(context);
        view.setBackgroundResource(R.color.colorTransparent);
        view.setClickable(true);
        AvenirNextRegularTextView tv = view.findViewById(android.R.id.message);//16908299);
        if (textSize > StaticLayoutUtil.DefaultSpacingadd) {
            tv.setTextSize(textSize);
        }
        tv.setText(text);
        tv.setTextColor(App.getResource().getColor(textColorResId));
        FontsUtil.applyARegularFont(context, tv);
        tv.getPaint().setFakeBoldText(true);
        tv.setBackgroundDrawable(getRoundShape(App.getResource().getColor(style.background)));
        result.mView = view;
        result.mDuration = style.duration;
        result.mFloating = floating;
        result.messageText = text.toString();
        result.mStyle = style;
        view.setOnClickListener(clickListener);
        return result;
    }

    public static AppMessage makeText(Activity context, int stringResId, Style style, View customView, boolean floating, int textColorResId) {
        return makeText(context, context.getResources().getText(stringResId), style, customView, floating, textColorResId);
    }

    public static AppMessage makeText(Activity context, int resId, Style style, int textColorResId) throws NotFoundException {
        return makeText(context, context.getResources().getText(resId), style, textColorResId);
    }

    public static AppMessage makeText(Activity context, int resId, Style style, int layoutId, int textColorResId) throws NotFoundException {
        return makeText(context, context.getResources().getText(resId), style, layoutId, textColorResId);
    }

    public void show() {
        MessageManager manager = MessageManager.obtain(this.mActivity);
        manager.clearAllMessage();
        manager.add(this);
    }

    public boolean isShowing() {
        if (this.mFloating) {
            if (this.mView == null || this.mView.getParent() == null) {
                return false;
            }
            return true;
        } else if (this.mView.getVisibility() != View.VISIBLE) {
            return false;
        } else {
            return true;
        }
    }

    public void cancel() {
        MessageManager.obtain(this.mActivity).clearMessage(this);
    }

    public static void cancelAll() {
        MessageManager.clearAll();
    }

    public static void cancelAll(Activity activity) {
        MessageManager.release(activity);
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public View getView() {
        return this.mView;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setText(int resId) {
        setText(this.mActivity.getText(resId));
    }

    public void setText(CharSequence s) {
        if (this.mView == null) {
            throw new RuntimeException("This AppMsg was not created with AppMsg.makeText()");
        }
        AvenirNextRegularTextView tv = (AvenirNextRegularTextView) this.mView.findViewById(android.R.id.message);//16908299);
        if (tv == null) {
            throw new RuntimeException("This AppMsg was not created with AppMsg.makeText()");
        }
        tv.setText(s);
        FontsUtil.applyARegularFont(App.getContext(), tv);
    }

    public LayoutParams getLayoutParams() {
        if (this.mLayoutParams == null) {
            this.mLayoutParams = new LayoutParams(-1, -2);
        }
        return this.mLayoutParams;
    }

    public AppMessage setLayoutParams(LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        return this;
    }

    public AppMessage setLayoutGravity(int gravity) {
        this.mLayoutParams = new FrameLayout.LayoutParams(-1, -2, gravity);
        return this;
    }

    public boolean isFloating() {
        return this.mFloating;
    }

    public void setFloating(boolean mFloating) {
        this.mFloating = mFloating;
    }

    public AppMessage setAnimation(int inAnimation, int outAnimation) {
        return setAnimation(AnimationUtils.loadAnimation(this.mActivity, inAnimation), AnimationUtils.loadAnimation(this.mActivity, outAnimation));
    }

    public AppMessage setAnimation(Animation inAnimation, Animation outAnimation) {
        this.mInAnimation = inAnimation;
        this.mOutAnimation = outAnimation;
        return this;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public ViewGroup getParent() {
        return this.mParent;
    }

    public void setParent(ViewGroup parent) {
        this.mParent = parent;
    }

    public void setParent(int parentId) {
        setParent((ViewGroup) this.mActivity.findViewById(parentId));
    }
}