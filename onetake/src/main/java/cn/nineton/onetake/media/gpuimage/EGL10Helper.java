package cn.nineton.onetake.media.gpuimage;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.nineton.onetake.media.ObjectPool;

@RequiresApi(api = 17)
public class EGL10Helper {
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    private static final String TAG = "EGL10Helper";
    static ArrayList<EGL10Helper> allInstances = new ArrayList();
    private static Object eglLock = new Object();
    static ObjectPool<EGL10Helper> mContextPool = new ObjectPool();
    static EGLDisplay mEGLDisplay = EGL14.eglGetDisplay(0);
    private static final ThreadLocal<EGL10Helper> myEGL = new ThreadLocal();
    private static EGL10Helper sharedContext = new EGL10Helper("shared");
    EGLSurface mDefaultSurface;
    EGLConfig[] mEGLConfigs;
    EGLContext mEGLContext;
    boolean mIsAdreno;
    private boolean mIsFrozen;
    boolean mPrintedGLVersion;
    String mUser;
    private ArrayList<EGLSurface> surfaces = new ArrayList();

    static {
        for (int i = 0; i < 2; i++) {
            EGL10Helper egl = create("pool:" + i);
            egl.mIsFrozen = true;
            mContextPool.offer(egl);
        }
        EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        myEGL.set(null);
    }

    public static <T> T withContext(String ident, EGLRunnable<T> r) {
        EGL10Helper egl = (EGL10Helper) myEGL.get();
        if (egl != null) {
            return r.run(egl);
        }
        if (EGL14.eglGetCurrentContext().equals(EGL14.EGL_NO_CONTEXT)) {
            Log.d(TAG, String.format("waiting context for %s", new Object[]{ident}));
            egl = (EGL10Helper) mContextPool.take();
            Log.d(TAG, String.format("got context for %s", new Object[]{ident}));
            egl.makeDefaultCurrent();
            if (EGL14.eglGetCurrentContext().equals(EGL14.EGL_NO_CONTEXT)) {
                throw new RuntimeException("invalid EGL context, did it deleted?");
            }
            resetGLState();
            try {
                Log.d(TAG, String.format("enter block for %s", new Object[]{ident}));
                T result = r.run(egl);
                Log.d(TAG, String.format("finally for %s", new Object[]{ident}));
                clearGLError("withContext done");
                myEGL.set(null);
                egl.unmakeCurrentInternal();
                mContextPool.offer(egl);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "exception in block", e);
                throw e;
            } catch (Throwable th) {
                Log.d(TAG, String.format("finally for %s", new Object[]{ident}));
                clearGLError("withContext done");
                myEGL.set(null);
                egl.unmakeCurrentInternal();
                mContextPool.offer(egl);
            }
        } else {
            throw new RuntimeException("already have EGL context");
        }
        return null;
    }

    public static void withContext(String ident, final EGLRunnableVoid r) {
//        withContext(ident, EGL10Helper$$Lambda$1.lambdaFactory$(r));
        withContext(ident, new EGLRunnable() {
            @Override
            public Object run(EGL10Helper eGL10Helper) {
                r.run(eGL10Helper);
                return null;
            }
        });
    }

    public static void resetGLState() {
        clearGLError("resetGLState 1");
        GLES20.glBlendFunc(1, 0);
        GLES20.glDisable(3042);
        GLES20.glDisable(3089);
        GLES20.glUseProgram(0);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(36197, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(36197, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(36197, 0);
        GLES20.glBindTexture(3553, 0);
        clearGLError("resetGLState 2");
    }

    public static EGL10Helper create(String user) {
        Throwable th;
        synchronized (eglLock) {
            try {
                Log.d(TAG, String.format("New EGL for %s", new Object[]{user}));
                EGL10Helper egl = new EGL10Helper(user);
                try {
                    resetGLState();
                    return egl;
                } catch (Throwable th2) {
                    th = th2;
                    EGL10Helper eGL10Helper = egl;
                    //throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                //throw th;
            }
            return null;
        }
    }

    public static void checkContextActive() {
        OpenGlUtils.checkContextActive();
    }

    private EGL10Helper(String user) {
        this.mUser = user;
        Log.d(TAG, String.format("New EGL for %s, currently %d instances", new Object[]{user, Integer.valueOf(allInstances.size())}));
        int[] version = new int[2];
        if (EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            int[] attribList = new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12339, 1, 12352, 4, EGL_RECORDABLE_ANDROID, 1, 12344};
            this.mEGLConfigs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            if (EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, this.mEGLConfigs, 0, numConfigs.length, numConfigs, 0)) {
                this.mEGLContext = EGL14.eglCreateContext(mEGLDisplay, this.mEGLConfigs[0], sharedContext != null ? sharedContext.mEGLContext : EGL14.EGL_NO_CONTEXT, new int[]{EGL_CONTEXT_CLIENT_VERSION, 2, 12344}, 0);
                checkEglError("eglCreateContext");
                if (this.mEGLContext == null) {
                    throw new RuntimeException("null context");
                }
                this.mDefaultSurface = createPBuffer(16, 16);
                makeCurrent(this.mDefaultSurface);
                this.mIsAdreno = GLES20.glGetString(7937).startsWith("Adreno");
                allInstances.add(this);
                printInstances();
                return;
            }
            throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
        }
        throw new RuntimeException("unable to initialize EGL10");
    }

    public void release() {
        if (this.mIsFrozen) {
            throw new RuntimeException("releasing frozen context");
        }
        synchronized (eglLock) {
            Log.d(TAG, String.format("release EGL for user:%s handle:%d", new Object[]{this.mUser, Long.valueOf(OpenGlUtils.currentContextHandle())}));
            unmakeCurrentInternal();
            allInstances.remove(this);
            printInstances();
            Log.d(TAG, String.format("Delete EGL, now %d instances", new Object[]{Integer.valueOf(allInstances.size())}));
            if (this.mDefaultSurface != null) {
                destroySurface(this.mDefaultSurface);
                this.mDefaultSurface = null;
            }
            if (this.mEGLContext != null) {
                EGL14.eglDestroyContext(mEGLDisplay, this.mEGLContext);
                this.mEGLContext = null;
            } else {
                Log.d(TAG, "Deleting already deleted EGL context");
            }
        }
    }

    private void unmakeCurrentInternal() {
        if (OpenGlUtils.currentContextHandle() != OpenGlUtils.contextHandle(this.mEGLContext)) {
            Log.e(TAG, "releasing EGL when not current!");
            makeDefaultCurrent();
        }
        if (this.surfaces.size() != 1) {
            Log.e(TAG, String.format("not all EGL surfaces free when releasing egl", new Object[0]));
            throw new RuntimeException("surfaces remaining when releasing egl");
        }
        if (mEGLDisplay != null) {
            EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        }
        myEGL.set(null);
    }

    public static synchronized EGL10Helper getShared() {
        EGL10Helper eGL10Helper;
        synchronized (EGL10Helper.class) {
            eGL10Helper = sharedContext;
        }
        return eGL10Helper;
    }

    public static synchronized void printInstances() {
        synchronized (EGL10Helper.class) {
            Log.d(TAG, String.format("----- egl: %d instances -----", new Object[]{Integer.valueOf(allInstances.size())}));
            Iterator it = allInstances.iterator();
            while (it.hasNext()) {
                EGL10Helper e = (EGL10Helper) it.next();
                Log.d(TAG, String.format("instance:%s handle:%d", new Object[]{e.mUser, Long.valueOf(OpenGlUtils.contextHandle(e.mEGLContext))}));
            }
        }
    }

    public boolean isAdreno() {
        return this.mIsAdreno;
    }

    public int getMaxCaptureWidth() {
        int[] out = new int[1];
        EGL14.eglGetConfigAttrib(mEGLDisplay, this.mEGLConfigs[0], 12332, out, 0);
        return out[0];
    }

    public int getMaxCaptureHeight() {
        int[] out = new int[1];
        EGL14.eglGetConfigAttrib(mEGLDisplay, this.mEGLConfigs[0], 12330, out, 0);
        return out[0];
    }

    public int numSurfaces() {
        return this.surfaces.size();
    }

    public EGLDisplay getDisplay() {
        return mEGLDisplay;
    }

    public synchronized EGLSurface createWindowSurface(Surface surface) {
        return createWindowSurface(surface, true);
    }

    public synchronized EGLSurface createWindowSurface(Surface surface, boolean tracked) {
        EGLSurface eglSurface;
        eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, this.mEGLConfigs[0], surface, new int[]{12344}, 0);
        checkEglError("createSurface");
        if (tracked) {
            this.surfaces.add(eglSurface);
        }
        return eglSurface;
    }

    public synchronized EGLSurface createPBuffer(int width, int height) {
        EGLSurface eglSurface;
        eglSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, this.mEGLConfigs[0], new int[]{12375, width, 12374, height, 12344}, 0);
        this.surfaces.add(eglSurface);
        checkEglError("eglCreatePbufferSurface");
        return eglSurface;
    }

    public synchronized void destroySurface(EGLSurface eglSurface) {
        this.surfaces.remove(eglSurface);
        EGL14.eglDestroySurface(mEGLDisplay, eglSurface);
        checkEglError("destroySurface");
    }

    public EGLContext getContext() {
        return this.mEGLContext;
    }

    public void makeDefaultCurrent() {
        makeCurrent(this.mDefaultSurface);
        myEGL.set(this);
    }

    public void makeCurrent(EGLSurface surface) {
        if (surface != null) {
            EGL14.eglMakeCurrent(mEGLDisplay, surface, surface, this.mEGLContext);
            checkEglError("eglMakeCurrent");
            myEGL.set(this);
            if (!this.mPrintedGLVersion) {
                Log.d(TAG, String.format("GL_RENDERER:%s", new Object[]{GLES20.glGetString(7937)}));
                Log.d(TAG, String.format("GL_VENDOR:%s", new Object[]{GLES20.glGetString(7936)}));
                Log.d(TAG, String.format("GL_VERSION:%s", new Object[]{GLES20.glGetString(7938)}));
                this.mPrintedGLVersion = true;
            }
        }
    }

    public void setPresentationTime(EGLSurface surface, long nsecs) {
        if (!EGLExt.eglPresentationTimeANDROID(mEGLDisplay, surface, nsecs)) {
            Log.d(TAG, "could not set pts");
        }
        checkEglError("eglPresentationTimeANDROID");
    }

    public boolean swap(EGLSurface surface) {
        boolean result = EGL14.eglSwapBuffers(mEGLDisplay, surface);
        clearEglError("eglSwapBuffers");
        return result;
    }

    public synchronized void checkEglError(String msg) {
        boolean failed = false;
        while (true) {
            int error = EGL14.eglGetError();
            if (error != 12288) {
                Log.e(TAG, msg + ": EGL error: 0x" + Integer.toHexString(error));
                failed = true;
            } else if (failed) {
                throw new RuntimeException("EGL error encountered (see log)");
            }
        }
    }

    public void clearEglError(String msg) {
        while (true) {
            int error = EGL14.eglGetError();
            if (error != 12288) {
                Log.e(TAG, msg + ": EGL error: 0x" + Integer.toHexString(error));
            } else {
                return;
            }
        }
    }

    public static void checkGLError(String msg) {
        OpenGlUtils.checkGLError(msg);
    }

    public static void clearGLError(String msg) {
        OpenGlUtils.clearGLError(msg);
    }

    public static void test() {
        ConcurrentLinkedQueue<Thread> threads = new ConcurrentLinkedQueue();
        for (int i = 0; i < 15; i++) {
            final int index = i;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    EGL10Helper.create("test:" + index);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
            });
            t.setName("egltest:" + i);
            t.start();
            threads.add(t);
        }
        Iterator it = threads.iterator();
        while (it.hasNext()) {
            try {
                ((Thread) it.next()).join();
            } catch (InterruptedException e) {
            }
        }
        Log.d(TAG, "okay");
    }
}