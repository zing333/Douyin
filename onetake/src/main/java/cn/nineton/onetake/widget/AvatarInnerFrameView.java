package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
//import com.blink.academy.onetake.fresco.view.AvatarFrameView.OnLoadingCompleteListener;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.MovieFileUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.nineton.onetake.R;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.MovieFileUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.TintColorUtil;

public class AvatarInnerFrameView extends ImageView {
    private static final int FRAME_DURATION = 100;
    public static final String TAG = AvatarInnerFrameView.class.getSimpleName();
    int count;
    private String currentTime;
    private boolean mDrawCircle;
    private DraweeHolder<GenericDraweeHierarchy> mDraweeHolder;
    private int mGender;
    private Handler mHandler;
    private boolean mIsLiveAvatar;
    private float mItemSize;
    private float mLongHeight;
    private Matrix mMatrix;
    private AvatarFrameView.OnLoadingCompleteListener mOnLoadingCompleteListener;
    private Path mPath;
    private int mPhotoSize;
    private float mScale;
    private String mUrl;

    public AvatarInnerFrameView(Context context) {
        this(context, null);
    }

    public AvatarInnerFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarInnerFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mScale = 1.0f;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 10) {
                    AvatarInnerFrameView.this.start();
                }
            }
        };
        this.count = 0;
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(1, 1, 1, 1);
        setLayoutParams(layoutParams);
        this.mMatrix = new Matrix();
        stop();
        setScaleType(ScaleType.CENTER_CROP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFlags(1);
        paint.setFilterBitmap(true);
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorLightGray));
        this.mDraweeHolder = DraweeHolder.create(new GenericDraweeHierarchyBuilder(getResources()).setPlaceholderImage(new ColorDrawable(getResources().getColor(R.color.colorLightGray))).setFailureImage(new ColorDrawable(getResources().getColor(R.color.colorLightGray))).setFadeDuration(0).build(), getContext());
    }

    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.getSaveCount();
        canvas.save();
        if (this.mDrawCircle) {
            canvas.clipPath(this.mPath);
        }
        canvas.drawColor(getResources().getColor(R.color.colorHeadBackground));
        try {
            super.onDraw(canvas);
        } catch (RuntimeException e) {
            LogUtil.e(TAG, "caught trying to use a recycled bitmap !");
            setImageUrl(this.mUrl, this.mGender, this.mItemSize, this.mDrawCircle);
        }
        canvas.restoreToCount(saveCount);
    }

    public void setImageUrl(String url, int gender, float itemSize, boolean drawCircle) {
        clearAnimation();
        this.mDrawCircle = drawCircle;
        float radius = (itemSize - 2.0f) / 2.0f;
        this.mPath = new Path();
        this.mPath.addCircle(radius, radius, radius, Direction.CCW);
        this.mPath.close();
        this.mGender = gender;
        this.mItemSize = itemSize;
        stop();
        setScaleType(ScaleType.CENTER_CROP);
        setImageResource(this.mGender == 0 ? R.drawable.icon_80_avatar_female : R.drawable.icon_80_avatar_male);
        setScaleType(ScaleType.CENTER_CROP);
        TintColorUtil.tintDrawable(getContext(), (ImageView) this, 2131755074);
        this.mUrl = url;
        if (url == null) {
            this.mIsLiveAvatar = false;
            if (this.mOnLoadingCompleteListener != null) {
                this.mOnLoadingCompleteListener.onLoadingComplete(true);
                return;
            }
            return;
        }
        setImageUri(Uri.parse(url));
    }

    public void setImageUrl(String url, int gender, float itemSize, boolean drawCircle, String currentTime) {
        clearAnimation();
        this.currentTime = currentTime;
        this.mDrawCircle = drawCircle;
        float radius = (itemSize - 2.0f) / 2.0f;
        this.mPath = new Path();
        this.mPath.addCircle(radius, radius, radius, Direction.CCW);
        this.mPath.close();
        this.mGender = gender;
        this.mItemSize = itemSize;
        stop();
        setScaleType(ScaleType.CENTER_CROP);
        setImageResource(this.mGender == 0 ? R.drawable.icon_80_avatar_female : R.drawable.icon_80_avatar_male);
        setScaleType(ScaleType.CENTER_CROP);
        TintColorUtil.tintDrawable(getContext(), (ImageView) this, R.color.colorLightGray);
        this.mUrl = url;
        if (url == null) {
            this.mIsLiveAvatar = false;
            if (this.mOnLoadingCompleteListener != null) {
                this.mOnLoadingCompleteListener.onLoadingComplete(true);
                return;
            }
            return;
        }
        setImageUri(Uri.parse(url));
    }

    public String getUrl() {
        return this.mUrl;
    }

    private void saveBitmapToLocal(Bitmap bitmap) {
        File f = new File(MovieFileUtil.getOutputAvatarFile(this.currentTime));
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            this.currentTime = "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void setImageUri(Uri uri) {
        final DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(ImageRequest.fromUri(uri), getContext());
        this.mDraweeHolder.setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(uri).setOldController(this.mDraweeHolder.getController())).setControllerListener(new BaseControllerListener<ImageInfo>() {
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                boolean z = true;
                super.onFinalImageSet(id, imageInfo, animatable);
                CloseableReference<CloseableImage> imageReference = (CloseableReference) dataSource.getResult();
                if (imageReference != null) {
                    try {
                        CloseableImage image = (CloseableImage) imageReference.get();
                        if (image != null && (image instanceof CloseableStaticBitmap)) {
                            Bitmap bitmap = ((CloseableStaticBitmap) image).getUnderlyingBitmap();
                            if (bitmap != null) {
                                if (TextUtil.isValidate(AvatarInnerFrameView.this.currentTime)) {
                                    AvatarInnerFrameView.this.saveBitmapToLocal(bitmap);
                                }
                                AvatarInnerFrameView.this.setScaleType(ScaleType.MATRIX);
                                AvatarInnerFrameView.this.mPhotoSize = bitmap.getWidth();
                                AvatarInnerFrameView.this.mLongHeight = (float) bitmap.getHeight();
                                AvatarInnerFrameView.this.mScale = AvatarInnerFrameView.this.mItemSize / ((float) AvatarInnerFrameView.this.mPhotoSize);
                                AvatarInnerFrameView avatarInnerFrameView = AvatarInnerFrameView.this;
                                if (AvatarInnerFrameView.this.mLongHeight <= ((float) (AvatarInnerFrameView.this.mPhotoSize * 2))) {
                                    z = false;
                                }
                                avatarInnerFrameView.mIsLiveAvatar = z;
                                AvatarInnerFrameView.this.setImageBitmap(bitmap);
                                AvatarInnerFrameView.this.count = 0;
                                AvatarInnerFrameView.this.stop();
                                if (AvatarInnerFrameView.this.mIsLiveAvatar) {
                                    AvatarInnerFrameView.this.setScaleType(ScaleType.MATRIX);
                                    AvatarInnerFrameView.this.start();
                                } else {
                                    AvatarInnerFrameView.this.setScaleType(ScaleType.CENTER_CROP);
                                }
                                if (AvatarInnerFrameView.this.mOnLoadingCompleteListener != null) {
                                    AvatarInnerFrameView.this.mOnLoadingCompleteListener.onLoadingComplete(true);
                                }
                            } else {
                                AvatarInnerFrameView.this.setImageResource(AvatarInnerFrameView.this.mGender == 0 ? 2130838120 : 2130838121);
                                AvatarInnerFrameView.this.setScaleType(ScaleType.CENTER_CROP);
                                AvatarInnerFrameView.this.mIsLiveAvatar = false;
                                TintColorUtil.tintDrawable(AvatarInnerFrameView.this.getContext(), AvatarInnerFrameView.this, 2131755074);
                                if (AvatarInnerFrameView.this.mOnLoadingCompleteListener != null) {
                                    AvatarInnerFrameView.this.mOnLoadingCompleteListener.onLoadingComplete(false);
                                }
                            }
                        } else if (AvatarInnerFrameView.this.mOnLoadingCompleteListener != null) {
                            AvatarInnerFrameView.this.mOnLoadingCompleteListener.onLoadingComplete(false);
                        }
                    } catch (Throwable th) {
                        dataSource.close();
                        CloseableReference.closeSafely(imageReference);
                    }
                } else if (AvatarInnerFrameView.this.mOnLoadingCompleteListener != null) {
                    AvatarInnerFrameView.this.mOnLoadingCompleteListener.onLoadingComplete(false);
                }
                dataSource.close();
                CloseableReference.closeSafely(imageReference);
            }

            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }
        })).build());
        invalidate();
    }

    private void start() {
        clearAnimation();
        if (this.mIsLiveAvatar) {
            int current = this.mPhotoSize * this.count;
            this.mMatrix.setTranslate(StaticLayoutUtil.DefaultSpacingadd, (float) (-current));
            this.mMatrix.postScale(this.mScale, this.mScale);
            setImageMatrix(this.mMatrix);
            this.count++;
            if (((float) ((this.mPhotoSize + current) + 10)) > this.mLongHeight) {
                this.count = 0;
            }
            this.mHandler.sendEmptyMessageDelayed(10, 100);
        }
    }

    private void stop() {
        clearAnimation();
        if (this.mHandler != null) {
            this.mMatrix.setTranslate(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
            this.mMatrix.postScale(this.mScale, this.mScale);
            setImageMatrix(this.mMatrix);
            this.mHandler.removeMessages(10);
        }
    }

    public void startFrameAnim() {
        stop();
        start();
    }

    public void stopFrameAnim() {
        stop();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDraweeHolder.onDetach();
        if (getDrawable() != null) {
        }
    }

    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        this.mDraweeHolder.onDetach();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDraweeHolder.onAttach();
        if (getDrawable() != null) {
        }
    }

    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        this.mDraweeHolder.onAttach();
    }

    public void setOnLoadingCompleteListener(AvatarFrameView.OnLoadingCompleteListener onLoadingCompleteListener) {
        this.mOnLoadingCompleteListener = onLoadingCompleteListener;
    }
}