package cn.nineton.onetake.util;

import android.graphics.Bitmap;

import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;

public class FramesHolder {
    private static final FramesHolder holder = new FramesHolder();
    private Bitmap mBlurBitmap;
    private OutputSurfaceArray mFrames;
    private EGL10Helper mSharedEGL;

    public Bitmap getBlurBitmap() {
        return this.mBlurBitmap;
    }

    public void setBlurBitmap(Bitmap bitmap) {
        this.mBlurBitmap = bitmap;
    }

    public OutputSurfaceArray getFrames() {
        return this.mFrames;
    }

    public void setFrames(OutputSurfaceArray frames) {
        this.mFrames = frames;
    }

    public static FramesHolder getInstance() {
        return holder;
    }
}