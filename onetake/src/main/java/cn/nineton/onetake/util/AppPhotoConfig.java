package cn.nineton.onetake.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.nineton.onetake.App;

public class AppPhotoConfig {

    static class Postprocessor extends BasePostprocessor {
        public String getName() {
            return super.getName();
        }

        public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
            int width = sourceBitmap.getWidth();
            int height = sourceBitmap.getHeight();
            int showWidth = width - 2;
            int showHeight = height - 2;
            CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(width - 2, height - 2);
            try {
                new Canvas((Bitmap) bitmapRef.get()).drawBitmap(sourceBitmap, new Rect(0, 0, showWidth, showHeight), new Rect(0, 0, showWidth, showHeight), null);
                CloseableReference<Bitmap> cloneOrNull = CloseableReference.cloneOrNull(bitmapRef);
                return cloneOrNull;
            } finally {
                CloseableReference.closeSafely(bitmapRef);
            }
        }

        public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
            super.process(destBitmap, sourceBitmap);
        }

        public void process(Bitmap bitmap) {
            super.process(bitmap);
        }

        @Nullable
        public CacheKey getPostprocessorCacheKey() {
            return super.getPostprocessorCacheKey();
        }
    }

    public static DraweeController getMainPhotoConfig(@NonNull String url, SimpleDraweeView simpleDraweeView, BaseControllerListener<ImageInfo> baseControllerListener) {
        return ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(Uri.parse(url)).setOldController(simpleDraweeView.getController())).setControllerListener(baseControllerListener)).setAutoPlayAnimations(false)).build();
    }

    public static DraweeController getAlbumPhotoConfig(@NonNull String url, SimpleDraweeView simpleDraweeView, BaseControllerListener<ImageInfo> baseControllerListener) {
        int width = (int) ((((float) DensityUtil.getMetricsWidth(App.getContext())) * 1.0f) / 3.0f);
        return ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setOldController(simpleDraweeView.getController())).setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setResizeOptions(new ResizeOptions(width, width)).setAutoRotateEnabled(true).build())).setControllerListener(baseControllerListener)).setAutoPlayAnimations(false)).build();
    }

    public static DraweeController getWebpPhotoConfig(String url, SimpleDraweeView simpleDraweeView, BaseControllerListener<ImageInfo> baseControllerListener) {
        return ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setUri(Uri.parse(url)).setOldController(simpleDraweeView.getController())).setControllerListener(baseControllerListener)).setAutoPlayAnimations(true)).build();
    }
}