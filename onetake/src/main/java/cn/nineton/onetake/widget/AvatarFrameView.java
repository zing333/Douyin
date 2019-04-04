package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class AvatarFrameView extends RelativeLayout {
    private AvatarInnerFrameView mAvatarInnerFrameView;
    private AvatarOuterCircleView mAvatarOuterCircleView;

    public interface OnLoadingCompleteListener {
        void onLoadingComplete(boolean z);
    }

    public AvatarFrameView(Context context) {
        this(context, null);
    }

    public AvatarFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAvatarInnerFrameView = new AvatarInnerFrameView(context);
        addView(this.mAvatarInnerFrameView);
        this.mAvatarOuterCircleView = new AvatarOuterCircleView(context);
        addView(this.mAvatarOuterCircleView);
    }

    public void setImageUrl(String url, int gender, float itemSize) {
        setImageUrl(url, gender, itemSize, true);
    }

    public void setImageUrlForNet(String url, int gender, float itemSize, String currentTime) {
        setImageUrl(url, gender, itemSize, true, currentTime);
    }

    public void setImageUrl(String url, int gender, float itemSize, boolean drawCircle, String currentTime) {
        this.mAvatarInnerFrameView.setImageUrl(url, gender, itemSize, drawCircle, currentTime);
        if (!drawCircle) {
            removeView(this.mAvatarOuterCircleView);
        }
    }

    public void setImageUrl(String url, int gender, float itemSize, boolean drawCircle) {
        this.mAvatarInnerFrameView.setImageUrl(url, gender, itemSize, drawCircle);
        if (!drawCircle) {
            removeView(this.mAvatarOuterCircleView);
        }
    }

    public void startFrameAnim() {
        this.mAvatarInnerFrameView.startFrameAnim();
    }

    public void stopFrameAnim() {
        this.mAvatarInnerFrameView.stopFrameAnim();
    }

    public void setOnLoadingCompleteListener(OnLoadingCompleteListener onLoadingCompleteListener) {
        this.mAvatarInnerFrameView.setOnLoadingCompleteListener(onLoadingCompleteListener);
    }

    public Drawable getDrawable() {
        return this.mAvatarInnerFrameView.getDrawable();
    }

    public String getUrl() {
        return this.mAvatarInnerFrameView.getUrl();
    }
}