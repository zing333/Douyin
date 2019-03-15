package cn.nineton.onetake.media.videotool;

import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

import javax.microedition.khronos.egl.EGL;

import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;

public class CodecInputSurface {
    private static String TAG = "CodecInputSurface";
    private EGLSurface mEGLSurface = null;
    public int mHeight;
    private Surface mSurface;
    public int mWidth;

    public CodecInputSurface(Surface surface, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        if (surface == null) {
            throw new NullPointerException();
        }
        this.mSurface = surface;
//        EGL10Helper.withContext("CodecInputSurface.init", CodecInputSurface$$Lambda$1.lambdaFactory$(this));
        EGL10Helper.withContext("CodecInputSurface.init", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$new$0(eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$new$0(EGL10Helper egl) {
        this.mEGLSurface = egl.createWindowSurface(this.mSurface, false);
    }

    public void release() {
//        EGL10Helper.withContext("CodecInputSurface.release", CodecInputSurface$$Lambda$2.lambdaFactory$(this));
        EGL10Helper.withContext("CodecInputSurface.release", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$release$1(eGL10Helper);
            }
        });
        this.mSurface.release();
        this.mSurface = null;
    }

    private /* synthetic */ void lambda$release$1(EGL10Helper egl) {
        egl.destroySurface(this.mEGLSurface);
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
    }

    public void makeCurrent(EGL10Helper egl) {
        egl.makeCurrent(this.mEGLSurface);
    }

    public void makeCurrent(EGL10Helper egl, long timeUs) {
        egl.makeCurrent(this.mEGLSurface);
        egl.setPresentationTime(this.mEGLSurface, timeUs * 1000);
    }

    public void swap(EGL10Helper egl) {
        if (!egl.swap(this.mEGLSurface)) {
            Log.d(TAG, "frame failed to swap");
        }
    }
}