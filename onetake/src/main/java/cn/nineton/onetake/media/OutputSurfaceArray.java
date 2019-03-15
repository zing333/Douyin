package cn.nineton.onetake.media;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnable;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;
import cn.nineton.onetake.media.gpuimage.ResourceTracker;

public class OutputSurfaceArray {
    static final /* synthetic */ boolean $assertionsDisabled = (!OutputSurfaceArray.class.desiredAssertionStatus());
    private static int MEGABYTE = 1048576;
    public static final int PLAY_TYPE_CYCLES = 2222;
    public static final int PLAY_TYPE_NORMAL = 1111;
    public static final int PLAY_TYPE_REVERSE = 3333;
    private static final String TAG = "OutputSurfaceArray";
    public static float[] _tmpvec = new float[4];
    private static int mAllocatedBufferCount = 0;
    private static int mAllocatedMemorySize = 0;
    static BufferPool mGlobalPool;
    private ArrayList<Frame> allFrames = new ArrayList();
    public byte[] audioData;
    long basetime = 0;
    public ArrayList<Frame> frames = new ArrayList();
    public boolean getAlphaFrame = false;
    private BufferPool mBufferPool;
    public boolean mCameraFlipped;
    public int mCameraOrientation;
    public int mCaptureHeight;
    public int mCaptureWidth;
    public int mCropHeight;
    public int mCropWidth;
    public int mFramerate;
    private FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private FloatBuffer mGLTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    public boolean mIsLooped;
    public int mPhysHeight;
    public int mPhysWidth;
    public int mPoolSize;
    public float[] mRenderMatrix = new float[16];
    public int mScreenOrientation;
    private int playType;

    public static final class Buffer {
        private static int gID = 0;
        public static Buffer sentinel = new Buffer();
        public int mHeight;
        public int mID;
        public int mTextureId;
        public long mTimestamp;
        public int mWidth;

        private Buffer() {
            int i = gID;
            gID = i + 1;
            this.mID = i;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mTextureId = 0;
            this.mTimestamp = 0;
        }

        Buffer(int width, int height) {
            this(width, height, OutputSurfaceArray.createTexture(width, height));
            OutputSurfaceArray.mAllocatedBufferCount = OutputSurfaceArray.mAllocatedBufferCount + 1;
            OutputSurfaceArray.mAllocatedMemorySize = OutputSurfaceArray.mAllocatedMemorySize + ((this.mWidth * this.mHeight) * 3);
            Log.d(OutputSurfaceArray.TAG, String.format("Allocated a buffer, texture:%d, total:%d size:%d\n", new Object[]{Integer.valueOf(this.mTextureId), Integer.valueOf(OutputSurfaceArray.mAllocatedBufferCount), Integer.valueOf(OutputSurfaceArray.mAllocatedMemorySize / OutputSurfaceArray.MEGABYTE)}));
            ResourceTracker.markTexture(this.mTextureId, "surfacearray", this.mWidth, this.mHeight, 3);
        }

        public Buffer(int width, int height, int texid) {
            int i = gID;
            gID = i + 1;
            this.mID = i;
            this.mWidth = width;
            this.mHeight = height;
            this.mTextureId = texid;
        }

        public void freeTexture() {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, 0);
            ResourceTracker.freeTexture(this.mTextureId);
            this.mTextureId = -1;
            OutputSurfaceArray.mAllocatedBufferCount = OutputSurfaceArray.mAllocatedBufferCount - 1;
            OutputSurfaceArray.mAllocatedMemorySize = OutputSurfaceArray.mAllocatedMemorySize - ((this.mWidth * this.mHeight) * 3);
            Log.d(OutputSurfaceArray.TAG, String.format("Freed a buffer, texture:%d total:%d size:%d\n", new Object[]{Integer.valueOf(this.mTextureId), Integer.valueOf(OutputSurfaceArray.mAllocatedBufferCount), Integer.valueOf(OutputSurfaceArray.mAllocatedMemorySize / OutputSurfaceArray.MEGABYTE)}));
            this.mTextureId = 0;
        }
    }

    public static final class BufferPool {
        private ArrayList<Buffer> allBuffers = new ArrayList();
        private ArrayList<Buffer> freeBuffers = new ArrayList();
        private int mCount;
        public final int mHeight;
        public final int mWidth;

        public BufferPool(int count, int width, int height) {
            this.mCount = count;
            this.mWidth = width;
            this.mHeight = height;
            for (int i = 0; i < count; i++) {
                Buffer b = new Buffer(width, height);
                this.freeBuffers.add(b);
                this.allBuffers.add(b);
            }
        }

        public void releaseAll() {
            if (!allBuffersAvailable()) {
                Log.e(OutputSurfaceArray.TAG, "not all buffers safely released!");
            }
            if (this != OutputSurfaceArray.mGlobalPool) {
                for (int i = 0; i < this.allBuffers.size(); i++) {
                    ((Buffer) this.allBuffers.get(i)).freeTexture();
                }
                this.freeBuffers.clear();
                this.allBuffers.clear();
                this.mCount = 0;
                return;
            }
            this.freeBuffers.clear();
            this.freeBuffers.addAll(this.allBuffers);
        }

        public int initialCount() {
            return this.mCount;
        }

        public int freeCount() {
            return this.freeBuffers.size();
        }

        public Buffer alloc() {
            if (this.freeBuffers.size() != 0) {
                return (Buffer) this.freeBuffers.remove(this.freeBuffers.size() - 1);
            }
            throw new RuntimeException("out of buffers");
        }

        public void free(Buffer b) {
            if (this.allBuffers.contains(b)) {
                if (!this.freeBuffers.contains(b)) {
                    this.freeBuffers.add(b);
                }
            } else if (b == Buffer.sentinel) {
                throw new RuntimeException("freeing sentinel buffer!");
            } else {
                throw new RuntimeException("freeing unowned buffer!");
            }
        }

        public boolean allBuffersAvailable() {
            return this.freeBuffers.size() == this.mCount;
        }
    }

    public static final class Frame {
        public Buffer mBuffer;
        public int mIndex;
        public long mTimestampUs;

        Frame() {
        }

        public Frame(Buffer b, long timestamp, int index) {
            this.mTimestampUs = timestamp;
            this.mBuffer = b;
            this.mIndex = index;
        }

        public Frame clone(boolean retain) {
            Frame f = new Frame();
            f.mTimestampUs = this.mTimestampUs;
            f.mBuffer = this.mBuffer;
            f.mIndex = this.mIndex;
            return f;
        }
    }

    public int getPlayType() {
        return this.playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public OutputSurfaceArray() {
        Matrix.setIdentityM(this.mRenderMatrix, 0);
    }

    public static void initGlobalPool() {
//        EGL10Helper.withContext("initGlobalPool", OutputSurfaceArray$$Lambda$1.lambdaFactory$());
        EGL10Helper.withContext("initGlobalPool", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                mGlobalPool = new BufferPool(32, 1920, 1088);
            }
        });
    }

    public synchronized void allocateSharedPool(int count, int width, int height) {
        this.mBufferPool = mGlobalPool;
        if (count > this.mBufferPool.mCount) {
            throw new RuntimeException("not enough buffers in global pool");
        }
        this.mPoolSize = count;
        this.mPhysWidth = mGlobalPool.mWidth;
        this.mPhysHeight = mGlobalPool.mHeight;
        this.mCaptureWidth = width;
        this.mCaptureHeight = height;
        this.mCropWidth = width;
        this.mCropHeight = height;
    }

    public synchronized void allocatePool(final int count, final int width, final int height) {
        this.mPoolSize = count;
        this.mPhysWidth = width;
        this.mPhysHeight = height;
        this.mCaptureWidth = width;
        this.mCaptureHeight = height;
        this.mCropWidth = width;
        this.mCropHeight = height;
        if (!(this.mBufferPool != null && count == this.mBufferPool.mCount && width == this.mBufferPool.mWidth && height == this.mBufferPool.mHeight)) {
//            EGL10Helper.withContext("allocatePool", OutputSurfaceArray$$Lambda$2.lambdaFactory$(this, count, width, height));
            EGL10Helper.withContext("allocatePool", new EGLRunnableVoid() {
                @Override
                public void run(EGL10Helper eGL10Helper) {
                    lambda$allocatePool$1(count,width,height,eGL10Helper);
                }
            });
        }
    }

    private /* synthetic */ void lambda$allocatePool$1(int count, int width, int height, EGL10Helper egl) {
        if (this.mBufferPool != null) {
            this.mBufferPool.releaseAll();
        }
        this.mBufferPool = new BufferPool(count, width, height);
    }

    public boolean isReadyToCapture() {
        return this.mBufferPool.allBuffersAvailable();
    }

    public synchronized BufferPool getBufferPool() {
        return this.mBufferPool;
    }

    public void resetCropWidthHeight() {
        this.mCropWidth = this.mCaptureWidth;
        this.mCropHeight = this.mCaptureHeight;
    }

    public synchronized void releasePool() {
//        EGL10Helper.withContext("releasePoolOnGLThread", OutputSurfaceArray$$Lambda$3.lambdaFactory$(this));
        EGL10Helper.withContext("releasePoolOnGLThread", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$releasePool$2(eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$releasePool$2(EGL10Helper egl) {
        if (this.mBufferPool != null) {
            this.mBufferPool.releaseAll();
        }
        this.mPoolSize = 0;
    }

    public synchronized int size() {
        return this.frames.size();
    }

    public synchronized int size2() {
        return this.allFrames.size();
    }

    public synchronized int poolSize() {
        int i;
        if (this.mBufferPool == null) {
            i = 0;
        } else {
            i = this.mPoolSize;
        }
        return i;
    }

    public synchronized Frame get(int i) {
        Frame frame;
        if (App.Debug) {
            frame = (Frame) this.frames.get(i);
        } else {
            try {
                frame = (Frame) this.frames.get(i);
            } catch (RuntimeException e) {
                e.printStackTrace();
                frame = null;
            }
        }
        return frame;
    }

    public synchronized Frame getFrameByOriginal(int i) {
        Frame frame;
        try {
            frame = (Frame) this.allFrames.get(i);
        } catch (RuntimeException e) {
            e.printStackTrace();
            frame = null;
        }
        return frame;
    }

    @TargetApi(8)
    private static int createTexture(int width, int height) {
        EGL10Helper.checkContextActive();
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureID = textures[0];
        EGL10Helper.checkGLError("before glBindTexture mTextureID");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, textureID);
        EGL10Helper.checkGLError("glBindTexture mTextureID" + textureID);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        checkGlError("glTexParameter");
        GLES20.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, null);
        EGL10Helper.checkGLError("glTexImage2D");
        GLES20.glFinish();
        return textureID;
    }

    public synchronized void releaseFrames() {
        Iterator it = this.allFrames.iterator();
        while (it.hasNext()) {
            Frame f = (Frame) it.next();
            if (f != null) {
                this.mBufferPool.free(f.mBuffer);
            }
        }
        this.frames.clear();
        this.allFrames.clear();
        if (this.mBufferPool != null && !this.mBufferPool.allBuffersAvailable()) {
            throw new RuntimeException("not all buffers have been released!");
        }
    }

    @TargetApi(8)
    public static void checkGlError(String op) {
        EGL10Helper.checkGLError(op);
    }

    @TargetApi(8)
    Frame copyFrameBuffer(long origTimestamp, long timestamp, int index) {
        Buffer b = this.mBufferPool.alloc();
        b.mTimestamp = origTimestamp;
        Frame f = new Frame(b, timestamp, index);
        checkGlError("before copyTexImage2D");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, b.mTextureId);
        GLES20.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, this.mCaptureWidth, this.mCaptureHeight);
        GLES20.glFinish();
        checkGlError("copyTexImage2D");
        return f;
    }

    synchronized Frame createNewFrame(Bitmap bitmap, long timestamp) {
        Frame f;
        Buffer b = this.mBufferPool.alloc();
        if (!$assertionsDisabled && b.mWidth != bitmap.getWidth()) {
            throw new AssertionError();
        } else if ($assertionsDisabled || b.mHeight == bitmap.getHeight()) {
            b.mTimestamp = timestamp;
            checkGlError("before texImage2D(bitmap)");
            f = new Frame(b, timestamp, this.frames.size());
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, b.mTextureId);
            checkGlError("texImage2D(bitmap)");
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            checkGlError("after texImage2D(bitmap)");
            addFrame(f);
        } else {
            throw new AssertionError();
        }
        return f;
    }

    public synchronized void addFrame(Frame f) {
        this.frames.add(f);
        this.allFrames.add(f);
    }

    synchronized Frame createNewFrame(long timestamp) {
        Frame f;
        if (this.frames.size() == 0) {
            this.basetime = timestamp;
        }
        f = copyFrameBuffer(timestamp, timestamp - this.basetime, this.frames.size());
        this.frames.add(f);
        this.allFrames.add(f);
        return f;
    }

    public synchronized void slideUnloop(int start, int end) {
        if (start < 0) {
            start = 0;
        }
        int size = this.allFrames.size();
        int allFramesEnd = size - 1;
        if (end >= size) {
            end = allFramesEnd;
        }
        int i;
        if (this.playType == PLAY_TYPE_NORMAL) {
            this.frames.clear();
            for (i = start; i <= end; i++) {
                this.frames.add(this.allFrames.get(i));
            }
        } else if (this.playType == PLAY_TYPE_REVERSE) {
            this.frames.clear();
            long delta = ((Frame) this.allFrames.get(1)).mTimestampUs - ((Frame) this.allFrames.get(0)).mTimestampUs;
            long time = ((Frame) this.allFrames.get(end)).mTimestampUs;
            for (i = end; i >= start; i--) {
                Frame frame = ((Frame) this.allFrames.get(i)).clone(false);
                frame.mTimestampUs = (((long) (end - i)) * delta) + time;
                this.frames.add(frame);
            }
        }
        this.mIsLooped = false;
    }

    public void toggleLoop(int start, int end) {
        if (this.mIsLooped) {
            unloop();
        } else {
            loop(start, end);
        }
    }

    public boolean isRotated() {
        return Math.abs(this.mRenderMatrix[4]) > Math.abs(this.mRenderMatrix[0]);
    }

    public int getRotatedWidth() {
        return isRotated() ? this.mCaptureHeight : this.mCaptureWidth;
    }

    public int getRotatedHeight() {
        return isRotated() ? this.mCaptureWidth : this.mCaptureHeight;
    }

    public synchronized void loop(int level) {
        int i;
        this.frames.clear();
        for (i = level; i <= 5 + level; i++) {
            this.frames.add(this.allFrames.get(i));
        }
        long delta = ((Frame) this.allFrames.get(1)).mTimestampUs - ((Frame) this.allFrames.get(0)).mTimestampUs;
        long time = ((Frame) this.frames.get(5)).mTimestampUs;
        for (i = 1; i < 5; i++) {
            Frame f = ((Frame) this.frames.get(5 - i)).clone(false);
            f.mTimestampUs = (((long) i) * delta) + time;
            this.frames.add(f);
        }
        this.mIsLooped = true;
        this.playType = PLAY_TYPE_CYCLES;
    }

    public synchronized void loop(int start, int end) {
        if (end <= this.allFrames.size() && start >= 0) {
            int i;
            if (end == this.allFrames.size()) {
                end = this.allFrames.size() - 1;
                start = start + -1 < 0 ? 0 : start - 1;
            }
            if (end - start >= 16) {
                end = start + 15;
            }
            this.frames.clear();
            for (i = start; i <= end; i++) {
                this.frames.add(this.allFrames.get(i));
            }
            long delta = ((Frame) this.allFrames.get(1)).mTimestampUs - ((Frame) this.allFrames.get(0)).mTimestampUs;
            long time = ((Frame) this.frames.get(end - start)).mTimestampUs;
            for (i = 1; i < end - start; i++) {
                Frame f = ((Frame) this.frames.get((end - start) - i)).clone(false);
                f.mTimestampUs = (((long) i) * delta) + time;
                this.frames.add(f);
            }
            this.mIsLooped = true;
            this.playType = PLAY_TYPE_CYCLES;
        }
    }

    public synchronized void unloop() {
        this.frames.clear();
        this.frames.addAll(this.allFrames);
        this.mIsLooped = false;
        this.playType = PLAY_TYPE_NORMAL;
    }

    public void limitDuration(long totalDurationUs) {
        ArrayList<Frame> newFrames = new ArrayList();
        for (int i = 0; i < this.frames.size(); i++) {
            newFrames.add(getFrameAt(i).clone(true));
        }
        Iterator it = this.frames.iterator();
        while (it.hasNext()) {
            Frame f = (Frame) it.next();
            if (!hasBufferRef(newFrames, f)) {
                this.mBufferPool.free(f.mBuffer);
            }
        }
        this.frames = newFrames;
        this.allFrames.clear();
        this.allFrames.addAll(newFrames);
        this.mFramerate = 10;
        this.mIsLooped = false;
        this.playType = PLAY_TYPE_NORMAL;
    }

    private boolean hasBufferRef(ArrayList<Frame> list, Frame f) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((Frame) it.next()).mBuffer == f.mBuffer) {
                return true;
            }
        }
        return false;
    }

    public Frame getFrameAt(int index) {
        try {
            return (Frame) this.frames.get(index);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return (Frame) this.frames.get(this.frames.size() - 1);
        }
    }

    @TargetApi(8)
    public synchronized void drawFrame(float outWidth, float outHeight, GPUImageFilter filter, int frameIndex, boolean fromOrigin) {
        if (this.frames.size() != 0) {
            Frame f;
            frameIndex %= fromOrigin ? this.allFrames.size() : this.frames.size();
            boolean flipped = false;
            if (outHeight <= 0.0f) {
                flipped = true;
                outHeight = -outHeight;
            }
            if (fromOrigin) {
                f = (Frame) this.allFrames.get(frameIndex);
            } else {
                f = (Frame) this.frames.get(frameIndex);
            }
            GLES20.glViewport(0, 0, (int) outWidth, (int) outHeight);
            GLES20.glScissor(0, 0, (int) outWidth, (int) outHeight);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            float[] m = this.mRenderMatrix;
            if ($assertionsDisabled || (m != null && m.length == 16)) {
                float[] cubeCoords = getCubeCoords(FrameRenderer.RenderMode.RENDER_FILL, m, 0.0f, 0.0f, outWidth, outHeight, (float) this.mCaptureWidth, (float) this.mCaptureHeight);
                this.mGLCubeBuffer.clear();
                this.mGLCubeBuffer.put(cubeCoords).position(0);
                float[] texCoords = transformedTexCoords((double) this.mCaptureWidth, (double) this.mCaptureHeight, (double) this.mPhysWidth, (double) this.mPhysHeight, flipped);
                this.mGLTextureBuffer.clear();
                this.mGLTextureBuffer.put(texCoords).position(0);
                filter.onDraw(f.mBuffer.mTextureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
            } else {
                throw new AssertionError();
            }
        }
    }

    @TargetApi(8)
    public synchronized void drawFrameUnrotated(float outWidth, float outHeight, GPUImageFilter filter, Frame f) {
        GLES20.glViewport(0, 0, (int) outWidth, (int) outHeight);
        GLES20.glScissor(0, 0, (int) outWidth, (int) outHeight);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16384);
        EGL10Helper.checkGLError("foo10");
        float[] cubeCoords = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        this.mGLCubeBuffer.clear();
        this.mGLCubeBuffer.put(cubeCoords).position(0);
        float[] texCoords = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mGLTextureBuffer.clear();
        this.mGLTextureBuffer.put(texCoords).position(0);
        EGL10Helper.checkGLError("foo11");
        filter.onDraw(f.mBuffer.mTextureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
        EGL10Helper.checkGLError("foo12");
    }

    @TargetApi(8)
    void clearRect(int x, int y, int w, int h) {
        GLES20.glScissor(x, y, w, h);
        GLES20.glClear(16384);
    }

    public int originalSize() {
        if (this.allFrames == null) {
            return 0;
        }
        return this.allFrames.size();
    }

    public static void transformCoords(float[] coords, float[] m) {
        for (int i = 0; i < 8; i += 2) {
            _tmpvec[0] = coords[i + 0];
            _tmpvec[1] = coords[i + 1];
            _tmpvec[2] = 0.0f;
            _tmpvec[3] = 1.0f;
            Matrix.multiplyMV(_tmpvec, 0, m, 0, _tmpvec, 0);
            coords[i + 0] = _tmpvec[0];
            coords[i + 1] = _tmpvec[1];
        }
    }

    public static float[] getCubeCoords(FrameRenderer.RenderMode mode, float[] m, float xOffset, float yOffset, float outWidth, float outHeight, float texCropWidth, float texCropHeight) {
        float[] invm = new float[16];
        Matrix.invertM(invm, 0, m, 0);
        float inWidth = texCropWidth;
        float inHeight = texCropHeight;
        float outInvWidth = Math.abs((invm[0] * outWidth) + (invm[4] * outHeight));
        float outInvHeight = Math.abs((invm[1] * outWidth) + (invm[5] * outHeight));
        float hRatio = outInvWidth / inWidth;
        float vRatio = outInvHeight / inHeight;
        float ratio = 1.0f;
        if (mode == FrameRenderer.RenderMode.RENDER_CROP) {
            ratio = Math.min(hRatio, vRatio);
        } else if (mode == FrameRenderer.RenderMode.RENDER_FILL) {
            ratio = Math.max(hRatio, vRatio);
        }
        int imageWidthNew = Math.round(inWidth * ratio);
        float ratioWidth = outInvWidth / ((float) imageWidthNew);
        float ratioHeight = outInvHeight / ((float) Math.round(inHeight * ratio));
        if (mode == FrameRenderer.RenderMode.RENDER_STRETCH) {
            ratioWidth = 1.0f;
            ratioHeight = 1.0f;
        }
        float[] cubeCoords = new float[]{(-ratioHeight) + xOffset, ratioWidth + yOffset, ratioHeight + xOffset, ratioWidth + yOffset, (-ratioHeight) + xOffset, (-ratioWidth) + yOffset, ratioHeight + xOffset, (-ratioWidth) + yOffset};
        transformCoords(cubeCoords, m);
        return cubeCoords;
    }

    public static float[] transformedTexCoords(double cropW, double cropH, double texW, double texH, boolean flipped) {
        float U1 = ((float) cropW) / ((float) texW);
        float V1 = ((float) cropH) / ((float) texH);
        if (flipped) {
            return new float[]{0.0f, V1, U1, V1, 0.0f, 0.0f, U1, 0.0f};
        }
        return new float[]{0.0f, 0.0f, U1, 0.0f, 0.0f, V1, U1, V1};
    }

    public synchronized ArrayList<Bitmap> getBitmaps(int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices) {
        return getBitmaps(targetWidth, targetHeight, filters, frameIndices, false);
    }

    @TargetApi(8)
    public synchronized ArrayList<Bitmap> getBitmaps(final int targetWidth, final int targetHeight, final GPUImageFilter[] filters, final int[] frameIndices, final boolean fromOrigin) {
//        return (ArrayList) EGL10Helper.withContext("OutputSurfaceArray.getBitmaps", OutputSurfaceArray$$Lambda$4.lambdaFactory$(this, targetWidth, targetHeight, filters, frameIndices, fromOrigin));
        return EGL10Helper.withContext("OutputSurfaceArray.getBitmaps", new EGLRunnable<ArrayList<Bitmap>>() {
            @Override
            public ArrayList<Bitmap> run(EGL10Helper eGL10Helper) {
                return lambda$getBitmaps$3(targetWidth,targetHeight,filters,frameIndices,fromOrigin,eGL10Helper);
            }
        });
    }

    private /* synthetic */ ArrayList lambda$getBitmaps$3(int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices, boolean fromOrigin, EGL10Helper egl) {
        float[] m = this.mRenderMatrix;
        int sourceWidth = (int) Math.abs((((float) this.mCaptureWidth) * m[0]) + (((float) this.mCaptureHeight) * m[4]));
        int sourceHeight = (int) Math.abs((((float) this.mCaptureWidth) * m[1]) + (((float) this.mCaptureHeight) * m[5]));
        float ratio = BitmapUtils.squishyRatio(targetWidth, targetHeight, sourceWidth, sourceHeight);
        int width = (int) Math.ceil((double) (((float) sourceWidth) / ratio));
        int height = (int) Math.ceil((double) (((float) sourceHeight) / ratio));
        int startWidth = width;
        int startHeight = height;
        EGLSurface surface = egl.createPBuffer(width, height);
        egl.makeCurrent(surface);
        GLES20.glBindFramebuffer(36160, 0);
        ArrayList<Bitmap> bitmaps = new ArrayList();
        int length = filters.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length) {
                GPUImageFilter filter = filters[i2];
                filter.init();
                filter.onOutputSizeChanged(startWidth, startHeight);
                int length2 = frameIndices.length;
                i = 0;
                while (true) {
                    int i3 = i;
                    if (i3 >= length2) {
                        break;
                    }
                    width = startWidth;
                    height = startHeight;
                    drawFrame((float) width, (float) height, filter, frameIndices[i3], fromOrigin);
                    bitmaps.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(targetWidth, targetHeight, width, height)));
                    i = i3 + 1;
                }
                filter.destroy();
                i = i2 + 1;
            } else {
                egl.destroySurface(surface);
                return bitmaps;
            }
        }
    }

    public synchronized void getPixels(int frameIndex, int targetWidth, int targetHeight, Rect region, GPUImageFilter filter, ByteBuffer pixels, boolean fromOrigin) {
        drawFrame((float) targetWidth, (float) targetHeight, filter, frameIndex, fromOrigin);
        targetHeight = Math.abs(targetHeight);
        int x = 0;
        int y = 0;
        int w = targetWidth;
        int h = targetHeight;
        if (region != null) {
            x = region.left;
            y = region.top;
            w = region.width();
            h = region.height();
            if (x + w > targetWidth) {
                throw new RuntimeException("invalid readpixels width");
            } else if (y + h > targetHeight) {
                throw new RuntimeException("invalid readpixels height");
            }
        }
        pixels.position(0);
        GLES20.glReadPixels(x, y, w, h, 6408, 5121, pixels);
    }

    @TargetApi(8)
    public synchronized ArrayList<Bitmap> getBitmapsFBO(EGL10Helper egl, int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices) {
        ArrayList<Bitmap> bitmaps;
        float[] m = this.mRenderMatrix;
        int sourceWidth = (int) Math.abs((((float) this.mCaptureWidth) * m[0]) + (((float) this.mCaptureHeight) * m[4]));
        int sourceHeight = (int) Math.abs((((float) this.mCaptureWidth) * m[1]) + (((float) this.mCaptureHeight) * m[5]));
        float ratio = BitmapUtils.squishyRatio(targetWidth, targetHeight, sourceWidth, sourceHeight);
        int width = (int) Math.ceil((double) (((float) sourceWidth) / ratio));
        int height = (int) Math.ceil((double) (((float) sourceHeight) / ratio));
        int startWidth = width;
        int startHeight = height;
        int[] rbs = new int[]{0};
        GLES20.glGenRenderbuffers(1, rbs, 0);
        GLES20.glBindRenderbuffer(36161, rbs[0]);
        GLES20.glRenderbufferStorage(36161, 32856, width, height);
        ResourceTracker.markRenderbuffer(rbs[0], "outputsurfacearray", width, height, 4);
        int[] fbos = new int[]{0};
        GLES20.glGenFramebuffers(1, fbos, 0);
        GLES20.glBindFramebuffer(36160, fbos[0]);
        GLES20.glFramebufferRenderbuffer(36160, 36064, 36161, rbs[0]);
        if (GLES20.glCheckFramebufferStatus(36160) != 36053) {
            throw new RuntimeException("framebuffer incomplete.");
        }
        bitmaps = new ArrayList();
        IntBuffer pixels = IntBuffer.allocate((width * height) * 4);
        int length = filters.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length) {
                GPUImageFilter filter = filters[i2];
                filter.init();
                filter.onOutputSizeChanged(startWidth, startHeight);
                int length2 = frameIndices.length;
                i = 0;
                while (true) {
                    int i3 = i;
                    if (i3 >= length2) {
                        break;
                    }
                    width = startWidth;
                    height = startHeight;
                    drawFrame((float) width, (float) height, filter, frameIndices[i3], false);
                    pixels.position(0);
                    GLES20.glReadPixels(0, 0, width, height, 6408, 5121, pixels);
                    bitmaps.add(BitmapUtils.captureBitmap(targetWidth, targetHeight, width, height));
                    i = i3 + 1;
                }
                filter.destroy();
                i = i2 + 1;
            } else {
                ResourceTracker.freeRenderbuffer(rbs[0]);
                GLES20.glBindFramebuffer(36160, 0);
                GLES20.glDeleteRenderbuffers(1, rbs, 0);
                GLES20.glDeleteFramebuffers(1, fbos, 0);
                OpenGlUtils.checkGLError("getbitmapsfbo");
            }
        }
    }

    public void setAudioData(byte[] data) {
        this.audioData = data;
    }
}