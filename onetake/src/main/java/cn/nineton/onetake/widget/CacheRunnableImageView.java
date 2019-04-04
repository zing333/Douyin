package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

import cn.nineton.onetake.util.ImageCacheUtils;

@SuppressLint({"AppCompatCustomView"})
public class CacheRunnableImageView extends ImageView {
    private WeakReference<ImageCacheUtils.PreloadImageRunnable> bitmapWorkerTaskReference;

    public CacheRunnableImageView(Context context) {
        super(context);
    }

    public CacheRunnableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CacheRunnableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WeakReference<ImageCacheUtils.PreloadImageRunnable> getBitmapWorkerTaskReference() {
        return this.bitmapWorkerTaskReference;
    }

    public void setBitmapWorkerTaskReference(WeakReference<ImageCacheUtils.PreloadImageRunnable> bitmapWorkerTaskReference) {
        this.bitmapWorkerTaskReference = bitmapWorkerTaskReference;
    }
}