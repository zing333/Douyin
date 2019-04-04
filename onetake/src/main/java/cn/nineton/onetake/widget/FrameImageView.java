package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$.Lambda;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$.Lambda.2;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$.Lambda.3;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$.Lambda.4;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$.Lambda.5;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView$PhotoTask$.Lambda.1;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Timer;
import java.util.TimerTask;

import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

public class FrameImageView extends ImageView {
    private int alphaEffectTime;
    DraweeHolder<GenericDraweeHierarchy> holder;
    private boolean isAttached;
    private boolean isLoading;
    private Handler mHandler;
    private float mItemWidth;
    private PhotoTask mPhotoTask;
    private float mScale;
    private Timer mTimer;
    private boolean needAlphaEffect;
    private int photoCount;
    private String url;

    class PhotoTask extends TimerTask {
        int i = 0;
        private ImageView imageView;
        private Matrix mMatrix = new Matrix();
        private float scale;
        private float translateX;
        private float translateY;
        private String url;

        public PhotoTask(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
            this.scale = FrameImageView.this.mScale;
            this.i = 0;
            this.translateX = 1.0f;
            this.translateY = 1.0f;
        }

        public void run() {
            if (FrameImageView.this.getDrawable() != null) {
                if (this.i >= FrameImageView.this.photoCount) {
                    this.i = 0;
                }
                this.mMatrix.setScale(this.scale, this.scale);
                this.mMatrix.postTranslate(-this.translateX, (-this.translateY) - (((float) this.i) * FrameImageView.this.mItemWidth));
                this.mMatrix.getValues(new float[9]);
                this.i++;
                //((Activity) FrameImageView.this.getContext()).runOnUiThread(1.lambdaFactory$(this));
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lambda$run$0();
                    }
                });
            }
        }

        private /* synthetic */ void lambda$run$0() {
            this.imageView.setImageMatrix(this.mMatrix);
        }
    }

    public void setNeedAlphaEffect(boolean needAlphaEffect) {
        this.needAlphaEffect = needAlphaEffect;
    }

    public FrameImageView(Context context) {
        super(context);
        this.photoCount = 8;
        this.alphaEffectTime = 200;
        this.needAlphaEffect = true;
        this.isLoading = false;
        this.isAttached = false;
        //this.mHandler = new Handler(Lambda.1.lambdaFactory$());
        initData();
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.photoCount = 8;
        this.alphaEffectTime = 200;
        this.needAlphaEffect = true;
        this.isLoading = false;
        this.isAttached = false;
        //this.mHandler = new Handler(2.lambdaFactory$());
        initData();
    }

    public FrameImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.photoCount = 8;
        this.alphaEffectTime = 200;
        this.needAlphaEffect = true;
        this.isLoading = false;
        this.isAttached = false;
        //this.mHandler = new Handler(3.lambdaFactory$());
        initData();
    }

    @TargetApi(21)
    public FrameImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
        this.photoCount = 8;
        this.alphaEffectTime = 200;
        this.needAlphaEffect = true;
        this.isLoading = false;
        this.isAttached = false;
        //this.mHandler = new Handler(4.lambdaFactory$());
        initData();
    }

    private void initData() {
        this.holder = DraweeHolder.create(new GenericDraweeHierarchyBuilder(getResources()).setFadeDuration(0).build(), getContext());
    }

    public void setTimer(Timer mTimer) {
        this.mTimer = mTimer;
    }

    public Timer getTimer() {
        return this.mTimer;
    }

    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("greason", "onDraw:Exception:" + e.getMessage());
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.holder.onDetach();
        stopTask();
    }

    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        this.holder.onDetach();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.holder.onAttach();
        if (TextUtil.isValidate(getUrl()) && this.mPhotoTask == null) {
            startFrames(getUrl());
        }
    }

    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        this.holder.onAttach();
    }

    public void onResume() {
        if (TextUtil.isValidate(getUrl()) && this.mPhotoTask == null) {
            startFrames(getUrl());
        }
    }

    public void onStop() {
        stopTask();
    }

    public String getUrl() {
        return this.url;
    }

    private void setThisUrl(String url) {
        this.url = url;
    }

    public void setUrl(String url, float itemWidth) {
        setUrl(url, itemWidth, 200.0f);
    }

    public void setUrl(String url, float itemWidth, float shortLength) {
        setUrl(url, itemWidth, shortLength, false);
    }

    public void setUrl(final String url, float itemWidth, float shortLength, final boolean needReset) {
        this.mItemWidth = 2.0f + itemWidth;
        if (needReset) {
            this.mScale = 1.0f;
        } else {
            this.mScale = this.mItemWidth / shortLength;
        }
        this.isLoading = true;
        Context context = getContext();
        final DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(ImageRequest.fromUri(Uri.parse(url)), context);
        this.holder.setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(url).setOldController(this.holder.getController())).setControllerListener(new BaseControllerListener<ImageInfo>() {
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                CloseableReference<CloseableImage> imageReference = (CloseableReference) dataSource.getResult();
                if (imageReference != null) {
                    try {
                        CloseableImage image = (CloseableImage) imageReference.get();
                        if (image != null && (image instanceof CloseableStaticBitmap)) {
                            Bitmap bitmap = ((CloseableStaticBitmap) image).getUnderlyingBitmap();
                            if (bitmap != null) {
                                FrameImageView.this.photoCount = (int) (((float) (bitmap.getHeight() / bitmap.getWidth())) + 0.5f);
                                if (needReset) {
                                    FrameImageView.this.mScale = FrameImageView.this.mItemWidth / ((float) (bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth()));
                                }
                                Matrix mMatrix = new Matrix();
                                mMatrix.setScale(FrameImageView.this.mScale, FrameImageView.this.mScale);
                                mMatrix.postTranslate(-1.0f, -1.0f);
                                FrameImageView.this.setImageMatrix(mMatrix);
                                boolean flag;
                                if (FrameImageView.this.getUrl() == null || FrameImageView.this.getTag() == null || !FrameImageView.this.getTag().equals(url)) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                                FrameImageView.this.setThisUrl(url);
                                FrameImageView.this.setImageBitmap(bitmap);
                                FrameImageView.this.clearAnimation();
                                if (FrameImageView.this.needAlphaEffect && flag) {
                                    Animation alphaAnimation = new AlphaAnimation(StaticLayoutUtil.DefaultSpacingadd, 1.0f);
                                    alphaAnimation.setDuration((long) FrameImageView.this.alphaEffectTime);
                                    FrameImageView.this.startAnimation(alphaAnimation);
                                    alphaAnimation.setAnimationListener(new AnimationListener() {
                                        public void onAnimationStart(Animation animation) {
                                        }

                                        public void onAnimationEnd(Animation animation) {
                                            FrameImageView.this.startFrames(url);
                                        }

                                        public void onAnimationRepeat(Animation animation) {
                                        }
                                    });
                                } else {
                                    FrameImageView.this.startFrames(url);
                                }
                            } else {
                                FrameImageView.this.setImageBitmap(null);
                            }
                        }
                    } catch (Throwable th) {
                        dataSource.close();
                        CloseableReference.closeSafely(imageReference);
                        FrameImageView.this.isLoading = false;
                    }
                }
                dataSource.close();
                CloseableReference.closeSafely(imageReference);
                FrameImageView.this.isLoading = false;
            }

            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                FrameImageView.this.isLoading = false;
                FrameImageView.this.setImageBitmap(null);
            }
        })).build());
    }

    private void startFrames(final String url) {
        clearAnimation();
        if (getDrawable() != null) {
            //((Activity) getContext()).runOnUiThread(5.lambdaFactory$(this, url));
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lambda$startFrames$1(url);
                }
            });
        }
    }

    private /* synthetic */ void lambda$startFrames$1(String url) {
        if (this.mPhotoTask == null) {
            this.mPhotoTask = new PhotoTask(this, url);
            if (this.mTimer != null) {
                try {
                    this.mTimer.schedule(this.mPhotoTask, 0, 100);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopTask() {
        clearAnimation();
        if (this.mPhotoTask != null) {
            this.mPhotoTask.cancel();
            this.mPhotoTask = null;
        }
        if (this.mTimer != null) {
            this.mTimer.purge();
        }
    }

    public void stop() {
        setImageBitmap(null);
        clearAnimation();
        if (this.mPhotoTask != null) {
            this.mPhotoTask.cancel();
            this.mPhotoTask = null;
        }
        if (this.mTimer != null) {
            this.mTimer.purge();
        }
    }

    public void destory() {
        this.photoCount = 0;
        invalidate();
    }
}