package cn.nineton.onetake.util;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.R;
import cn.nineton.onetake.widget.LeftImageRightTextLayout;

//import com.blink.academy.onetake.support.utils.ColorFilterUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.widgets.LinearLayout.LeftImageRightTextLayout;

public class VideoEditSwitchTabsHolder {
    View parent;
    @BindView(R.id.video_edit_add_ll)
    LeftImageRightTextLayout video_edit_add_ll;
    @BindView(R.id.video_edit_back_image)
    ImageView video_edit_back_image;
    @BindView(R.id.video_edit_edit_top)
    RelativeLayout video_edit_edit_top;
    @BindView(R.id.video_edit_effect_ll)
    LeftImageRightTextLayout video_edit_effect_ll;
    @BindView(R.id.video_edit_music_remove_ll)
    LeftImageRightTextLayout video_edit_music_remove_ll;
    @BindView(R.id.video_edit_music_replace_ll)
    LeftImageRightTextLayout video_edit_music_replace_ll;
    @BindView(R.id.video_edit_music_top)
    RelativeLayout video_edit_music_top;
    @BindView(R.id.video_edit_music_trim_ll)
    LeftImageRightTextLayout video_edit_music_trim_ll;
    @BindView(R.id.video_edit_remove_ll)
    LeftImageRightTextLayout video_edit_remove_ll;
    @BindView(R.id.video_edit_remove_parent)
    RelativeLayout video_edit_remove_parent;
    @BindView(R.id.video_edit_reorder_ll)
    LeftImageRightTextLayout video_edit_reorder_ll;
    @BindView(R.id.video_edit_split_ll)
    LeftImageRightTextLayout video_edit_split_ll;
    @BindView(R.id.video_edit_text_ll)
    LeftImageRightTextLayout video_edit_text_ll;

    public VideoEditSwitchTabsHolder(View parent) {
        this.parent = parent;
        ButterKnife.bind(this, parent);
        this.video_edit_back_image.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        showOptButton(false, false);
    }

    public void onSwitchTabClick(boolean isMusic) {
        if (isMusic) {
            this.video_edit_music_top.setVisibility(View.VISIBLE);
            this.video_edit_edit_top.setVisibility(View.GONE);
            return;
        }
        this.video_edit_music_top.setVisibility(View.GONE);
        this.video_edit_edit_top.setVisibility(View.VISIBLE);
    }

    public void setMusicTopOnlyRemoveEnable() {
        setMusicTopClickable(false);
        this.video_edit_music_remove_ll.setEnabled(true);
        this.video_edit_music_remove_ll.setAlpha(1.0f);
    }

    public void setMusicTopClickable(boolean enable) {
        this.video_edit_music_remove_ll.setEnabled(enable);
        this.video_edit_music_trim_ll.setEnabled(enable);
        this.video_edit_music_replace_ll.setEnabled(enable);
        if (enable) {
            this.video_edit_music_remove_ll.setAlpha(1.0f);
            this.video_edit_music_trim_ll.setAlpha(1.0f);
            this.video_edit_music_replace_ll.setAlpha(1.0f);
            return;
        }
        this.video_edit_music_remove_ll.setAlpha(0.3f);
        this.video_edit_music_trim_ll.setAlpha(0.3f);
        this.video_edit_music_replace_ll.setAlpha(0.3f);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.video_edit_split_ll.setOnClickListener(listener);
        this.video_edit_reorder_ll.setOnClickListener(listener);
        this.video_edit_text_ll.setOnClickListener(listener);
        this.video_edit_remove_ll.setOnClickListener(listener);
        this.video_edit_effect_ll.setOnClickListener(listener);
        this.video_edit_add_ll.setOnClickListener(listener);
        this.video_edit_back_image.setOnClickListener(listener);
        this.video_edit_music_replace_ll.setOnClickListener(listener);
        this.video_edit_music_trim_ll.setOnClickListener(listener);
        this.video_edit_music_remove_ll.setOnClickListener(listener);
        refrashViewPosition();
    }

    public void setAddTextCanClick(boolean canAdd) {
        if (this.video_edit_text_ll != null) {
            this.video_edit_text_ll.setEnabled(canAdd);
            if (canAdd) {
                this.video_edit_text_ll.setAlpha(1.0f);
            } else {
                this.video_edit_text_ll.setAlpha(0.3f);
            }
        }
    }

    public void release() {
        this.parent = null;
        this.video_edit_split_ll = null;
        this.video_edit_reorder_ll = null;
        this.video_edit_text_ll = null;
        this.video_edit_remove_ll = null;
        this.video_edit_effect_ll = null;
        this.video_edit_add_ll = null;
    }

    public void setAlpha(float alpha) {
        if (this.parent != null) {
            this.parent.setAlpha(alpha);
        }
    }

    public void setVisibility(int visibility) {
        if (this.parent != null) {
            this.parent.setVisibility(visibility);
        }
    }

    public void showOptButton(boolean showEffects, boolean showRemove) {
        if (this.video_edit_effect_ll != null) {
            if (showEffects) {
                this.video_edit_effect_ll.setAlpha(1.0f);
                this.video_edit_effect_ll.setEnabled(true);
            } else {
                this.video_edit_effect_ll.setAlpha(0.3f);
                this.video_edit_effect_ll.setEnabled(false);
            }
            if (showRemove) {
                this.video_edit_remove_ll.setAlpha(1.0f);
                this.video_edit_remove_ll.setEnabled(true);
            } else {
                this.video_edit_remove_ll.setAlpha(0.3f);
                this.video_edit_remove_ll.setEnabled(false);
            }
            if (showEffects || showRemove) {
                isGoneUndoImage();
            } else {
                this.video_edit_back_image.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refrashViewPosition() {
        if (this.video_edit_split_ll != null) {
            if (this.video_edit_split_ll.getTextWidth() > ((float) DensityUtil.dip2px(25.0f))) {
                this.video_edit_text_ll.setTranslationX((this.video_edit_split_ll.getTextWidth() - ((float) DensityUtil.dip2px(25.0f))) / 2.0f);
                this.video_edit_effect_ll.setTranslationX((this.video_edit_split_ll.getTextWidth() - ((float) DensityUtil.dip2px(25.0f))) / 2.0f);
                this.video_edit_reorder_ll.setTranslationX((((float) DensityUtil.dip2px(25.0f)) - this.video_edit_split_ll.getTextWidth()) / 2.0f);
                this.video_edit_remove_ll.setTranslationX((((float) DensityUtil.dip2px(25.0f)) - this.video_edit_split_ll.getTextWidth()) / 2.0f);
            }
            if (this.video_edit_text_ll.getTextWidth() > ((float) DensityUtil.dip2px(25.0f))) {
                this.video_edit_text_ll.setTranslationX(((this.video_edit_text_ll.getTextWidth() - ((float) DensityUtil.dip2px(25.0f))) / 2.0f) + this.video_edit_text_ll.getTranslationX());
                this.video_edit_effect_ll.setTranslationX(this.video_edit_text_ll.getTranslationX() + this.video_edit_effect_ll.getTranslationX());
            }
            if (this.video_edit_effect_ll.getTextWidth() > ((float) DensityUtil.dip2px(25.0f))) {
                this.video_edit_effect_ll.setTranslationX(((this.video_edit_effect_ll.getTextWidth() - ((float) DensityUtil.dip2px(25.0f))) / 2.0f) + this.video_edit_effect_ll.getTranslationX());
            }
            if (this.video_edit_reorder_ll.getTextWidth() > ((float) DensityUtil.dip2px(25.0f))) {
                this.video_edit_reorder_ll.setTranslationX(((((float) DensityUtil.dip2px(25.0f)) - this.video_edit_reorder_ll.getTextWidth()) / 2.0f) + this.video_edit_reorder_ll.getTranslationX());
                this.video_edit_remove_ll.setTranslationX(this.video_edit_reorder_ll.getTranslationX() + this.video_edit_remove_ll.getTranslationX());
            }
            if (this.video_edit_remove_ll.getTextWidth() > ((float) DensityUtil.dip2px(25.0f))) {
                this.video_edit_remove_ll.setTranslationX(((((float) DensityUtil.dip2px(25.0f)) - this.video_edit_remove_ll.getTextWidth()) / 2.0f) + this.video_edit_remove_ll.getTranslationX());
            }
        }
    }

    private void isGoneUndoImage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (VideoEditSwitchTabsHolder.this.video_edit_back_image.getX() - (VideoEditSwitchTabsHolder.this.video_edit_remove_ll.getX() + ((float) (VideoEditSwitchTabsHolder.this.video_edit_remove_ll.getMeasuredWidth() / 2))) < ((float) DensityUtil.dip2px(45.0f))) {
                    VideoEditSwitchTabsHolder.this.video_edit_back_image.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    public void setRemoveViewClickable(boolean enable) {
        this.video_edit_remove_ll.setEnabled(enable);
        if (enable) {
            this.video_edit_remove_ll.setAlpha(1.0f);
        } else {
            this.video_edit_remove_ll.setAlpha(0.3f);
        }
    }

    public void switchSpitAndAdd(boolean isSplit, boolean enable) {
        if (this.video_edit_split_ll != null) {
            if (isSplit) {
                this.video_edit_split_ll.setEnabled(enable);
                if (enable) {
                    this.video_edit_split_ll.setAlpha(1.0f);
                } else {
                    this.video_edit_split_ll.setAlpha(0.3f);
                }
                if (this.video_edit_split_ll.getVisibility() != View.VISIBLE) {
                    this.video_edit_add_ll.setVisibility(View.GONE);
                    this.video_edit_split_ll.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            }
            this.video_edit_split_ll.setEnabled(enable);
            if (this.video_edit_add_ll.getVisibility() != View.VISIBLE) {
                this.video_edit_add_ll.setVisibility(View.VISIBLE);
                this.video_edit_split_ll.setVisibility(View.INVISIBLE);
            }
        }
    }
}