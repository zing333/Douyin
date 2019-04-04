package cn.nineton.onetake.util;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class BaseViewHolder extends ViewHolder {
    Object associatedObject;
    private final LinkedHashSet<Integer> childClickViewIds = new LinkedHashSet();
    public View convertView;
    private final LinkedHashSet<Integer> itemChildLongClickViewIds = new LinkedHashSet();
    private final SparseArray<View> views = new SparseArray();

    public BaseViewHolder(View view) {
        super(view);
        this.convertView = view;
    }

    public HashSet<Integer> getItemChildLongClickViewIds() {
        return this.itemChildLongClickViewIds;
    }

    public HashSet<Integer> getChildClickViewIds() {
        return this.childClickViewIds;
    }

    public View getConvertView() {
        return this.convertView;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        ((TextView) getView(viewId)).setText(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, @StringRes int strId) {
        ((TextView) getView(viewId)).setText(strId);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, @DrawableRes int imageResId) {
        ((ImageView) getView(viewId)).setImageResource(imageResId);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundRes(int viewId, @DrawableRes int backgroundRes) {
        getView(viewId).setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        ((TextView) getView(viewId)).setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ((ImageView) getView(viewId)).setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ((ImageView) getView(viewId)).setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setAlpha(int viewId, float value) {
        if (VERSION.SDK_INT >= 11) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        getView(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder linkify(int viewId) {
        Linkify.addLinks((TextView) getView(viewId), Linkify.ALL);
        return this;
    }

    public BaseViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = (TextView) getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | 128);
        return this;
    }

    public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = (TextView) getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | 128);
        }
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress) {
        ((ProgressBar) getView(viewId)).setProgress(progress);
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = (ProgressBar) getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public BaseViewHolder setMax(int viewId, int max) {
        ((ProgressBar) getView(viewId)).setMax(max);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating) {
        ((RatingBar) getView(viewId)).setRating(rating);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = (RatingBar) getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    @Deprecated
    public BaseViewHolder setOnClickListener(int viewId, OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder addOnClickListener(int viewId) {
        this.childClickViewIds.add(Integer.valueOf(viewId));
        return this;
    }

    public BaseViewHolder addOnLongClickListener(int viewId) {
        this.itemChildLongClickViewIds.add(Integer.valueOf(viewId));
        return this;
    }

    public BaseViewHolder setOnTouchListener(int viewId, OnTouchListener listener) {
        getView(viewId).setOnTouchListener(listener);
        return this;
    }

    public BaseViewHolder setOnLongClickListener(int viewId, OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemClickListener(int viewId, OnItemClickListener listener) {
        ((AdapterView) getView(viewId)).setOnItemClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemLongClickListener(int viewId, OnItemLongClickListener listener) {
        ((AdapterView) getView(viewId)).setOnItemLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemSelectedClickListener(int viewId, OnItemSelectedListener listener) {
        ((AdapterView) getView(viewId)).setOnItemSelectedListener(listener);
        return this;
    }

    public BaseViewHolder setOnCheckedChangeListener(int viewId, OnCheckedChangeListener listener) {
        ((CompoundButton) getView(viewId)).setOnCheckedChangeListener(listener);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        getView(viewId).setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        getView(viewId).setTag(key, tag);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    public BaseViewHolder setAdapter(int viewId, Adapter adapter) {
        ((AdapterView) getView(viewId)).setAdapter(adapter);
        return this;
    }

    public <T extends View> T getView(int viewId) {
        View view = (View) this.views.get(viewId);
        if (view != null) {
            return (T) view;
        }
        view = this.convertView.findViewById(viewId);
        this.views.put(viewId, view);
        return (T) view;
    }

    public Object getAssociatedObject() {
        return this.associatedObject;
    }

    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }
}