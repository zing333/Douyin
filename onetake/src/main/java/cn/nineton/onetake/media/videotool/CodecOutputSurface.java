package cn.nineton.onetake.media.videotool;

import android.graphics.SurfaceTexture;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;
import android.util.Log;
import android.view.Surface;

import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;

public class CodecOutputSurface extends Thread implements android.graphics.SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "CodecOutputSurface";
    private int h;
    private boolean mAborted = false;
    private Downscaler mDownscaler;
    EGLSurface mEncoderEGLSurface;
    int mEncoderHeight;
    Surface mEncoderSurface;
    int mEncoderWidth;
    float[] mFlipMatrix = new float[16];
    private Object mFrameSyncObject = new Object();
    private Handler mHandler;
    private OnFrameAvailableListener mListener;
    private Looper mLooper;
    private OutputSurfaceArray.Buffer mRenderBuffer;
    float[] mRenderMatrix = new float[16];
    private float mScaleRatio;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    float[] mTempMatrix = new float[16];
    private int mTextureID = 0;
    int[] textures = new int[1];
    private int w;
    private int x0;
    private int x1;
    private int y0;
    private int y1;

    public interface OnFrameAvailableListener {
        void onFrameAborted(OutputSurfaceArray.Buffer buffer);

        void onFrameAvailable(OutputSurfaceArray.Buffer buffer);
    }

    public CodecOutputSurface(float scaleRatio, int rotationDegrees, OnFrameAvailableListener listener) {
        this.mListener = listener;
        this.mScaleRatio = scaleRatio;
        Matrix.setIdentityM(this.mFlipMatrix, 0);
        Matrix.translateM(this.mFlipMatrix, 0, 0.5f, 0.5f, 0.0f);
        Matrix.rotateM(this.mFlipMatrix, 0, (float) rotationDegrees, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(this.mFlipMatrix, 0, 1.0f, -1.0f, 1.0f);
        Matrix.translateM(this.mFlipMatrix, 0, -0.5f, -0.5f, 0.0f);
        start();
        synchronized (this) {
            while (this.mSurface == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        setName("SurfaceTextureThread");
        Looper.prepare();
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
            }
        };
        this.mLooper = Looper.myLooper();
//        EGL10Helper.withContext("CodecOutputSurface.init", CodecOutputSurface$$Lambda$1.lambdaFactory$(this));
        EGL10Helper.withContext("CodecOutputSurface.init", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$run$0(eGL10Helper);
            }
        });
        Looper.loop();
//        EGL10Helper.withContext("CodecOutputSurface.close", CodecOutputSurface$$Lambda$2.lambdaFactory$(this));
        EGL10Helper.withContext("CodecOutputSurface.close", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$run$1(eGL10Helper);
            }
        });
        Log.d(TAG, "looper quit");
    }

    private /* synthetic */ void lambda$run$0(EGL10Helper egl) {
        this.mDownscaler = new Downscaler(this.mScaleRatio < 6.0f);
        this.mDownscaler.init();
        synchronized (this) {
            generateSurfaceTextureObjects();
            this.mSurfaceTexture = new SurfaceTexture(this.mTextureID);
            this.mSurfaceTexture.setOnFrameAvailableListener(this);
            this.mSurface = new Surface(this.mSurfaceTexture);
            detachSurfaceTexture();
            notifyAll();
        }
    }

    private /* synthetic */ void lambda$run$1(EGL10Helper egl) {
        this.mSurfaceTexture.release();
        this.mSurfaceTexture = null;
        this.mSurface.release();
        this.mSurface = null;
        if (this.mEncoderEGLSurface != null) {
            egl.destroySurface(this.mEncoderEGLSurface);
            this.mEncoderEGLSurface = null;
        }
        if (this.mEncoderSurface != null) {
            this.mEncoderSurface.release();
            this.mEncoderSurface = null;
        }
        GLES20.glDeleteTextures(1, new int[]{this.mTextureID}, 0);
        OpenGlUtils.checkGLError("codecoutputsurface1");
        this.mDownscaler.destroy();
    }

    public void release() {
        this.mLooper.quit();
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateSurfaceTextureObjects() {
        GLES20.glGenTextures(1, this.textures, 0);
        this.mTextureID = this.textures[0];
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mTextureID);
        GLES20.glTexParameterf(36197, 10241, 9729.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        EGL10Helper.checkGLError("glBindTexture mTextureID");
    }

    public void attachSurfaceTexture() {
        this.mSurfaceTexture.attachToGLContext(this.mTextureID);
    }

    public void detachSurfaceTexture() {
        GLES20.glBindTexture(36197, 0);
        this.mSurfaceTexture.detachFromGLContext();
        OpenGlUtils.clearGLError("detachSurfaceTexture 1");
        GLES20.glDeleteTextures(1, new int[]{this.mTextureID}, 0);
        this.mTextureID = 0;
        OpenGlUtils.clearGLError("detachSurfaceTexture 2");
    }

    public void abort() {
        synchronized (this.mFrameSyncObject) {
            this.mAborted = true;
            if (this.mRenderBuffer != null) {
                this.mListener.onFrameAborted(this.mRenderBuffer);
                this.mRenderBuffer = null;
            }
            this.mFrameSyncObject.notifyAll();
        }
    }

    public void waitForRender() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mAborted && this.mRenderBuffer != null) {
                try {
                    this.mFrameSyncObject.wait(500);
                    if (this.mRenderBuffer != null) {
                        Log.w(TAG, "frame not available yet");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("wait interrupted");
                }
            }
        }
    }

    public void setRenderBuffer(OutputSurfaceArray.Buffer b) {
        if (b == null) {
            throw new RuntimeException("cannot add null render buffer");
        } else if (this.mRenderBuffer != null) {
            throw new RuntimeException("existing render buffer");
        } else {
            synchronized (this.mFrameSyncObject) {
                if (this.mAborted) {
                    this.mListener.onFrameAborted(b);
                    return;
                }
                this.mRenderBuffer = b;
            }
        }
    }

    public void setEncoderSurface(final Surface surface, final int width, final int height) {
//        EGL10Helper.withContext("setEncoderSurface", CodecOutputSurface$$Lambda$3.lambdaFactory$(this, surface, width, height));
        EGL10Helper.withContext("setEncoderSurface", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$setEncoderSurface$2(surface,width,height,eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$setEncoderSurface$2(Surface surface, int width, int height, EGL10Helper egl) {
        this.mEncoderSurface = surface;
        this.mEncoderWidth = width;
        this.mEncoderHeight = height;
        this.mEncoderEGLSurface = egl.createWindowSurface(this.mEncoderSurface, false);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public synchronized void onFrameAvailable(final SurfaceTexture st) {
        Trace.beginSection("CodecOutputSurface.onFrameAvailable");
        synchronized (this.mFrameSyncObject) {
            if (!this.mAborted) {
//                EGL10Helper.withContext("onFrameAvailable", CodecOutputSurface$$Lambda$4.lambdaFactory$(this, st));
                EGL10Helper.withContext("onFrameAvailable", new EGLRunnableVoid() {
                    @Override
                    public void run(EGL10Helper eGL10Helper) {
                        lambda$onFrameAvailable$3(st,eGL10Helper);
                    }
                });
            }
        }
        Trace.endSection();
    }

    private /* synthetic */ void lambda$onFrameAvailable$3(SurfaceTexture st, EGL10Helper egl) {
        int outWidth;
        int outHeight;
        if (this.mEncoderEGLSurface != null) {
            egl.makeCurrent(this.mEncoderEGLSurface);
            attachSurfaceTexture();
            this.mSurfaceTexture.updateTexImage();
            this.mSurfaceTexture.getTransformMatrix(this.mTempMatrix);
            Matrix.multiplyMM(this.mRenderMatrix, 0, this.mTempMatrix, 0, this.mFlipMatrix, 0);
            egl.setPresentationTime(this.mEncoderEGLSurface, st.getTimestamp());
            outWidth = this.mEncoderWidth;
            outHeight = this.mEncoderHeight;
            GLES20.glScissor(0, 0, outWidth, outHeight);
            GLES20.glViewport(0, 0, outWidth, outHeight);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(36197, this.mTextureID);
            this.mDownscaler.onOutputSizeChanged(outWidth, outHeight);
            this.mDownscaler.blit(false, outWidth, outHeight, (float) this.w, (float) this.h, this.mRenderMatrix, -1);
            egl.swap(this.mEncoderEGLSurface);
            detachSurfaceTexture();
            this.mRenderBuffer = null;
            this.mFrameSyncObject.notifyAll();
        } else if (this.mRenderBuffer == null) {
            throw new RuntimeException("no render buffer");
        } else {
            attachSurfaceTexture();
            this.mSurfaceTexture.updateTexImage();
            this.mSurfaceTexture.getTransformMatrix(this.mRenderMatrix);
            EGL10Helper.clearGLError("before attach texture to framebuffer");
            int[] fb = new int[]{0};
            GLES20.glGenFramebuffers(1, fb, 0);
            GLES20.glBindFramebuffer(36160, fb[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mRenderBuffer.mTextureId, 0);
            EGL10Helper.clearGLError("attach texture to framebuffer");
            outWidth = this.mRenderBuffer.mWidth;
            outHeight = this.mRenderBuffer.mHeight;
            GLES20.glScissor(0, 0, outWidth, outHeight);
            GLES20.glViewport(0, 0, outWidth, outHeight);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(36197, this.mTextureID);
            this.mDownscaler.onOutputSizeChanged(outWidth, outHeight);
            this.mDownscaler.blit(false, outWidth, outHeight, (float) this.w, (float) this.h, this.mRenderMatrix, -1);
            GLES20.glDeleteFramebuffers(1, fb, 0);
            detachSurfaceTexture();
            GLES20.glFinish();
            this.mListener.onFrameAvailable(this.mRenderBuffer);
            this.mRenderBuffer = null;
            this.mFrameSyncObject.notifyAll();
        }
        EGL10Helper.clearGLError("onFrameAvailable");
    }

    public void setRect(int x0, int y0, int x1, int y1, int w, int h) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.w = w;
        this.h = h;
    }
}