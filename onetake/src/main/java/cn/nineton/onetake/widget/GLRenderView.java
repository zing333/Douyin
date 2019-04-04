package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.FramebufferCache;

public class GLRenderView extends SurfaceView implements Callback, FrameCallback {
    static final String TAG = "GLRenderView";
    private boolean bInitialized;
    protected boolean bSingleStep;
    protected EGL10Helper egl;
    int frameCount = 0;
    Object frameReadySync = new Object();
    protected Choreographer mChoreographer;
    protected EGLSurface mEGLSurface;
    protected String mEGLUser;
    private Thread mGLThread;
    private Handler mHandler;
    protected int mHeight;
    private boolean mIsPaused = false;
    protected BlockingQueue<Integer> mReturnQueue = new ArrayBlockingQueue(1);
    private long mStartTime;
    protected Surface mSurface;
    private boolean mWasPaused = true;
    protected int mWidth;

    abstract class InitRunnable implements Runnable {
        InitRunnable() {
        }
    }

    protected void onGLInit() {
    }

    protected void onGLChangedSize(int width, int height) {
    }

    protected boolean isGLReady() {
        return true;
    }

    protected void onGLRender(double time) {
    }

    protected void onGLRender(double time, long nanos) {
        onGLRender(time);
    }

    protected void onGLDestroy() {
    }

    public GLRenderView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public GLRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void startGL(String eglUser) {
        this.mEGLUser = eglUser;
        if (this.mGLThread == null) {
//            this.mGLThread = new Thread(GLRenderView$$Lambda$1.lambdaFactory$(this));
            this.mGLThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    access$lambda$0();
                }
            });
            this.mGLThread.setName("GLRenderView:" + eglUser);
            this.mGLThread.start();
            try {
                if (mReturnQueue!=null&&mReturnQueue.size()>0) {
                    this.mReturnQueue.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "thread is ready");
        }
    }

    public void stopGL() {
        if (this.mGLThread != null) {
            if (this.mHandler != null) {
                queueRunnable(new Runnable() {
                    @TargetApi(18)
                    public void run() {
                        GLRenderView.this.setSurface(null, 0, 0);
                        GLRenderView.this.mReturnQueue.offer(Integer.valueOf(1));
                        Looper.myLooper().quitSafely();
                    }
                });
                try {
                    this.mReturnQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                this.mGLThread.join();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.mGLThread = null;
        }
    }

    public void queueRunnable(final Runnable r) {
        if (this.mHandler == null) {
            Log.e(TAG, "queuing a runnable on a dead handler");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (GLRenderView.this.mEGLSurface != null) {
                        GLRenderView.this.egl.makeCurrent(GLRenderView.this.mEGLSurface);
                    } else {
                        GLRenderView.this.egl.makeDefaultCurrent();
                    }
                    r.run();
                }
            });
        }
    }

    public synchronized void queueRunnableSync(String reason, Runnable r) {
        if (this.mHandler == null) {
            Log.e(TAG, "queuing a queueRunnableSync runnable on a dead handler");
        } else {
            queueRunnable(r);
            drainQueue(reason);
        }
    }

    private void drainQueue(String reason) {
        long t1 = System.nanoTime();
        queueRunnable(new Runnable() {
            public void run() {
                GLRenderView.this.mReturnQueue.offer(Integer.valueOf(1));
            }
        });
        long t3 = System.nanoTime();
        try {
            this.mReturnQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long t2 = System.nanoTime();
        Log.d(TAG, String.format("drained for %s, took %d ms", new Object[]{reason, Long.valueOf((t3 - t1) / 1000000)}));
        Log.d(TAG, String.format("drained for %s, took %d ms", new Object[]{reason, Long.valueOf((t2 - t3) / 1000000)}));
    }

    protected void onDetachedFromWindow() {
        stopGL();
        super.onDetachedFromWindow();
    }

    public void onPause() {
        queueRunnableSync("onPause", new Runnable() {
            public void run() {
                GLRenderView.this.mIsPaused = true;
                GLRenderView.this.onGLPause();
            }
        });
    }

    public void onResume() {
        startGL(this.mEGLUser);
        queueRunnable(new Runnable() {
            public void run() {
                GLRenderView.this.mIsPaused = false;
                GLRenderView.this.onGLResume();
            }
        });
    }

    public void onGLPause() {
    }

    public void onGLResume() {
    }

    /* renamed from: threadEntry */
    protected void access$lambda$0() {
        Looper.prepare();
        this.egl = EGL10Helper.create(this.mEGLUser);
        this.mChoreographer = Choreographer.getInstance();
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
            }
        };
        this.mReturnQueue.offer(1);
        Looper.loop();
        this.mHandler = null;
        onGLDestroy();
        this.egl.release();
    }

    private void startChoreographer() {
        this.mChoreographer.postFrameCallback(this);
    }

    private void stopChoreographer() {
        this.mChoreographer.removeFrameCallback(this);
    }

    public void doFrame(long frameTimeNanos) {
        if (this.mEGLSurface == null || this.mIsPaused || !isGLReady()) {
            this.mChoreographer.postFrameCallback(this);
            return;
        }
        if (this.mWasPaused) {
            this.mStartTime = frameTimeNanos;
            this.mWasPaused = false;
        }
        this.egl.makeCurrent(this.mEGLSurface);
        EGL10Helper.clearGLError("before render");
        GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
        GLES20.glScissor(0, 0, this.mWidth, this.mHeight);
        long time = (frameTimeNanos - this.mStartTime) / 1000000;
        long t1 = System.nanoTime();
        onGLRender(((double) time) / 1000.0d, frameTimeNanos);
        long t2 = System.nanoTime();
        FramebufferCache.shared.endFrame();
        EGL10Helper.clearGLError("after render");
        synchronized (this.frameReadySync) {
            this.frameCount++;
            this.frameReadySync.notifyAll();
        }
        if (!this.bSingleStep) {
            this.mChoreographer.postFrameCallback(this);
        }
    }

    public void waitForFrame() {
        synchronized (this.frameReadySync) {
            while (this.frameCount == 0) {
                try {
                    this.frameReadySync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void setSurface(Surface surface, int width, int height) {
        stopChoreographer();
        if (surface != this.mSurface) {
            if (this.mEGLSurface != null) {
                this.egl.destroySurface(this.mEGLSurface);
                this.mEGLSurface = null;
                this.mWidth = 0;
                this.mHeight = 0;
            }
            this.mSurface = surface;
            if (this.mSurface != null) {
                this.mEGLSurface = this.egl.createWindowSurface(this.mSurface);
                this.egl.makeCurrent(this.mEGLSurface);
                this.egl.clearEglError("fresh init (safely ignore)");
                this.mWidth = width;
                this.mHeight = height;
                if (!this.bInitialized) {
                    this.bInitialized = true;
                    onGLInit();
                }
                onGLChangedSize(this.mWidth, this.mHeight);
                if (!this.bSingleStep) {
                    startChoreographer();
                    return;
                }
                return;
            }
            onGLDestroy();
            this.bInitialized = false;
        } else if (this.mSurface == null) {
            onGLDestroy();
            this.bInitialized = false;
        } else if (width != this.mWidth || height != this.mHeight) {
            this.egl.makeCurrent(this.mEGLSurface);
            this.mWidth = width;
            this.mHeight = height;
            onGLChangedSize(this.mWidth, this.mHeight);
            if (!this.bInitialized) {
                this.bInitialized = true;
                this.egl.clearEglError("fresh init (safely ignore)");
                onGLInit();
            }
            if (!this.bSingleStep) {
                startChoreographer();
            }
        }
    }

    public void releasePool(final OutputSurfaceArray videoFrames) {
        queueRunnableSync("releasePool", new Runnable() {
            public void run() {
                videoFrames.releasePool();
            }
        });
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
    }

    public void surfaceChanged(final SurfaceHolder holder, int format, final int width, final int height) {
        //Log.d(TAG, "surfaceChanged:" + width + MiPushClient.ACCEPT_TIME_SEPARATOR + height);
        queueRunnable(new InitRunnable() {
            public void run() {
                GLRenderView.this.setSurface(holder.getSurface(), width, height);
            }
        });
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        queueRunnableSync("surfaceDestroyed", new Runnable() {
            public void run() {
                GLRenderView.this.setSurface(null, 0, 0);
            }
        });
    }
}