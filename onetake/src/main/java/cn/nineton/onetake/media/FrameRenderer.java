package cn.nineton.onetake.media;

import android.graphics.Bitmap;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.Framebuffer;
import cn.nineton.onetake.media.gpuimage.FramebufferCache;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageOESFilter;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;
import cn.nineton.onetake.media.gpuimage.RenderException;
import cn.nineton.onetake.util.CheckUtil;

public class FrameRenderer {
    static final String TAG = "FrameRenderer";
    static final float[] identity = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    static long tsetfilters;
    private GPUImageFilter copyFilter = new GPUImageFilter();
    private boolean enableStabilizer = true;
    private float h = 1.0f;
    private int index = -1;
    public boolean isFilterPreview = false;
    private boolean mDidResize = false;
    private int mFilterHeight;
    private int mFilterWidth;
    private GPUImageFilter[] mFilters0;
    private GPUImageFilter[] mFilters1;
    private FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private FloatBuffer mGLTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private boolean mHaveFilterDimensions = false;
    GPUImageFilter mOverlayFilter;
    HAlign mOverlayHAlign;
    FloatBuffer mOverlayTexCoords = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    Texture mOverlayTexture;
    VAlign mOverlayVAlign;
    FloatBuffer mOverlayVerts = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    float mOverlayWidth;
    private VidStabilizer mStabilizer;
    GPUImageFilter mTargetFilter = new GPUImageFilter();
    Framebuffer mTargetFramebuffer = null;
    int mTargetTextureId = -1;
    private VidStabilizer.Transform[] mTransforms;
    private OutputSurfaceArray mVideoFrames;
    private int mWrapFrameIndex = 0;
    public RenderMode mode = RenderMode.RENDER_CROP;
    private double newFrameTime = 0.0d;
    private int newWrapFrameIndex = 0;
    private Framebuffer stabfbo;
    private float w = 1.0f;
    private boolean wrapFrameIndexHadChange = false;
    private float x = 0.0f;
    public float xOffset = 0.0f;
    private float y = 0.0f;
    public float yOffset = 0.0f;

    public enum HAlign {
        LEFT,
        RIGHT
    }

    public enum RenderMode {
        RENDER_STRETCH,
        RENDER_CROP,
        RENDER_FILL
    }

    public enum VAlign {
        TOP,
        BOTTOM
    }

    public void partialReset() {
        if (this.mFilters1 != null && this.mFilters1.length > 0) {
            for (GPUImageFilter filter : this.mFilters1) {
                filter.destroy();
                filter.destroySecondary();
            }
            this.mFilters1 = null;
        }
        if (this.stabfbo != null) {
            this.stabfbo.destroy();
            this.stabfbo = null;
        }
    }

    public void destroy() {
        destroyFilters();
        if (this.mFilters1 != null && this.mFilters1.length > 0) {
            for (GPUImageFilter filter : this.mFilters1) {
                filter.destroy();
                filter.destroySecondary();
            }
            this.mFilters1 = null;
        }
        if (this.copyFilter != null) {
            this.copyFilter.destroy();
            this.copyFilter = null;
        }
        if (this.mTargetFramebuffer != null) {
            this.mTargetFramebuffer.destroy();
            this.mTargetFramebuffer = null;
        }
        if (this.stabfbo != null) {
            this.stabfbo.destroy();
            this.stabfbo = null;
        }
        if (this.mStabilizer != null) {
            this.mStabilizer.destroy();
            this.mStabilizer = null;
        }
        this.mVideoFrames = null;
        this.index = -1;
    }

    public OutputSurfaceArray getVideoFrames() {
        return this.mVideoFrames;
    }

    public boolean isStabilizerEnabled() {
        return this.enableStabilizer;
    }

    public void setEnableStabilizer(boolean enableStabilizer) {
        this.enableStabilizer = enableStabilizer;
    }

    public void toggleStabilizer() {
        boolean z;
        if (this.enableStabilizer) {
            z = false;
        } else {
            z = true;
        }
        this.enableStabilizer = z;
        String str = TAG;
        String str2 = "stabilizer:%s";
        Object[] objArr = new Object[1];
        objArr[0] = this.enableStabilizer ? "yes" : "no";
        Log.e(str, String.format(str2, objArr));
    }

    public void setVideoFrames(OutputSurfaceArray frames) {
        this.mVideoFrames = frames;
        this.mDidResize = false;
        this.mTargetTextureId = -1;
        if (this.mTargetFramebuffer != null) {
            this.mTargetFramebuffer.destroy();
            this.mTargetFramebuffer = null;
        }
        if (this.mStabilizer != null) {
            this.mStabilizer.destroy();
            this.mStabilizer = null;
        }
        if (this.stabfbo != null) {
            this.stabfbo.destroy();
            this.stabfbo = null;
        }
    }

    public VidStabilizer.Transform[] getTransforms() {
        return this.mTransforms;
    }

    public void setTransforms(VidStabilizer.Transform[] transforms) {
        this.mTransforms = transforms;
        if (this.mStabilizer != null) {
            this.mStabilizer.setTransforms(transforms);
        }
    }

    public GPUImageFilter[] getFilters() {
        return this.mFilters0;
    }

    public void setFilter(GPUImageFilter newFilter) {
        if (this.mFilters0 != null && this.mFilters0.length > 0) {
            if (this.mFilters0.length != 1 || this.mFilters0[0] != newFilter) {
                for (GPUImageFilter destroy : this.mFilters0) {
                    destroy.destroy();
                }
                this.mFilters0 = null;
            } else {
                return;
            }
        }
        if (newFilter != null) {
            this.mFilters0 = new GPUImageFilter[1];
            this.mFilters0[0] = newFilter;
        }
    }

    public void setOverlay(GPUImageFilter filter, Texture texture, HAlign halign, VAlign valign, float width) {
    }

    public void setFilters(GPUImageFilter[] filters) {
        if (this.mFilters0 != filters) {
            int i;
            FramebufferCache.shared.clear();
            tsetfilters = System.nanoTime();
            if (this.mFilters0 != null && this.mFilters0.length > 0) {
                for (i = 0; i < this.mFilters0.length; i++) {
                    this.mFilters0[i].destroy();
                    this.mFilters0[i].destroySecondary();
                }
                this.mFilters0 = null;
            }
            if (this.mFilters1 != null && this.mFilters1.length > 0) {
                for (i = 0; i < this.mFilters1.length; i++) {
                    this.mFilters1[i].destroy();
                    this.mFilters1[i].destroySecondary();
                }
                this.mFilters1 = null;
            }
            for (GPUImageFilter destroy : filters) {
                destroy.destroy();
            }
            this.mFilters0 = filters;
        }
    }

    public void setFilters(GPUImageFilter[] filters0, GPUImageFilter[] filters1) {
        Set<GPUImageFilter> filters = new HashSet();
        FramebufferCache.shared.clear();
        if (this.mFilters0 != null && this.mFilters0.length > 0) {
            for (Object add : this.mFilters0) {
                filters.add((GPUImageFilter) add);
            }
            this.mFilters0 = null;
        }
        if (this.mFilters1 != null && this.mFilters1.length > 0) {
            for (Object add2 : this.mFilters1) {
                filters.add((GPUImageFilter) add2);
            }
            this.mFilters1 = null;
        }
        if (filters0 != null && filters0.length > 0) {
            for (Object add22 : filters0) {
                filters.remove(add22);
            }
        }
        if (filters1 != null && filters1.length > 0) {
            for (Object add222 : filters1) {
                filters.remove(add222);
            }
        }
        for (GPUImageFilter f : filters) {
            f.onDestroy();
            f.destroySecondary();
        }
        this.mFilters0 = filters0;
        this.mFilters1 = filters1;
    }

    public void destroyFilters() {
        if (this.mFilters0 != null && this.mFilters0.length > 0) {
            for (GPUImageFilter filter : this.mFilters0) {
                filter.destroy();
                filter.destroySecondary();
            }
            this.mFilters0 = null;
        }
    }

    public void setFilterDimensions(int width, int height) {
        FramebufferCache.shared.clear();
        this.mHaveFilterDimensions = true;
        this.mFilterWidth = width;
        this.mFilterHeight = height;
        if (this.mFilters0 != null) {
            for (GPUImageFilter filter : this.mFilters0) {
                initFilter(filter, width, height);
            }
        }
    }

    public void setOutputDimensions(int outWidth, int outHeight) {
        initFilter(this.copyFilter, outWidth, outHeight);
    }

    public void createStabilizerResources() {
        if (this.mVideoFrames != null) {
            EGL10Helper.clearGLError("before createStabilizerResources");
            if (this.stabfbo == null) {
                this.stabfbo = new Framebuffer("stablizerfbo", this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight);
                EGL10Helper.checkGLError("createFBO");
            }
            if (this.mStabilizer == null) {
                this.mStabilizer = new VidStabilizer(this.mVideoFrames);
                this.mStabilizer.setTransforms(this.mTransforms);
                EGL10Helper.checkGLError("createStabilizer");
            }
        }
    }

    public static void initFilter(GPUImageFilter filter, int width, int height) {
        boolean updateSize = false;
        if (!filter.isInitialized()) {
            filter.init();
            updateSize = true;
            OpenGlUtils.clearGLError("createFilter");
        }
        if (updateSize || filter.getOutputWidth() != width || filter.getOutputHeight() != height) {
            filter.onOutputSizeChanged(width, height);
        }
    }

    public int getFrameIndex(double time, boolean pauseAtEnd, boolean isSliding) {
        if (this.wrapFrameIndexHadChange) {
            this.wrapFrameIndexHadChange = false;
            this.newFrameTime = time;
        }
        int frameIndex = (int) Math.floor((time - this.newFrameTime) * ((double) this.mVideoFrames.mFramerate));
        int numFrames = this.mVideoFrames.size() - this.mWrapFrameIndex;
        if (numFrames <= 0) {
            return -1;
        }
        frameIndex = pauseAtEnd ? this.mVideoFrames.size() - 1 : (frameIndex % numFrames) + this.mWrapFrameIndex;
        if (this.mVideoFrames.getPlayType() == OutputSurfaceArray.PLAY_TYPE_REVERSE && isSliding) {
            frameIndex = pauseAtEnd ? 0 : numFrames - 1;
        }
        if (this.mVideoFrames.getPlayType() == OutputSurfaceArray.PLAY_TYPE_CYCLES && isSliding) {
            if (pauseAtEnd) {
                frameIndex = this.mVideoFrames.size() / 2;
            } else {
                frameIndex = 0;
            }
        }
        if (this.mVideoFrames.getPlayType() == OutputSurfaceArray.PLAY_TYPE_NORMAL && isSliding) {
            if (pauseAtEnd) {
                frameIndex = this.mVideoFrames.size() - 1;
            } else {
                frameIndex = 0;
            }
        }
        if (this.mWrapFrameIndex != this.newWrapFrameIndex && frameIndex == this.mVideoFrames.size() - 1) {
            this.mWrapFrameIndex = this.newWrapFrameIndex;
            this.wrapFrameIndexHadChange = true;
        }
        return frameIndex;
    }

    public int getClampedFrameIndex(double time) {
        return Math.min(((int) Math.floor(((double) this.mVideoFrames.mFramerate) * time)) + this.mWrapFrameIndex, this.mVideoFrames.size() - 1);
    }

    public int getWrapFrameIndex() {
        return this.mWrapFrameIndex;
    }

    public void setWrapFrame(int index) {
        this.mWrapFrameIndex = index;
        this.newWrapFrameIndex = index;
    }

    public void setWrapFrame2(int index) {
        this.newWrapFrameIndex = index;
    }

    public int updateStabilizedImage(int frameIndex) {
        createStabilizerResources();
        OutputSurfaceArray.Frame f = this.mVideoFrames.get(frameIndex);
        if (!(f == null || f.mBuffer == null)) {
            if (this.enableStabilizer) {
                this.mStabilizer.setTransforms(this.mTransforms);
            } else {
                this.mStabilizer.setTransforms(null);
            }
            this.stabfbo.bind();
            try {
                this.mStabilizer.drawFrame(this.stabfbo.width, this.stabfbo.height, f.mBuffer.mTextureId, frameIndex);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return this.stabfbo.texid();
    }

    public void drawFrameAtTime(int tgtFBO, int outX, int outY, int outWidth, int outHeight, int scissorX, int scissorY, int scissorW, int scissorH, double time, boolean pauseAtEnd, boolean isSliding) {
        if (this.mVideoFrames.size() != 0) {
            int frameIndex = getFrameIndex(time, pauseAtEnd, isSliding);
            if (frameIndex != -1) {
                drawFrameAtIndex(tgtFBO, outX, outY, outWidth, outHeight, scissorX, scissorY, scissorW, scissorH, frameIndex);
            }
        }
    }

    public void drawFrameAtIndex(int tgtFBO, int outX, int outY, int outWidth, int outHeight, int scissorX, int scissorY, int scissorW, int scissorH, int frameIndex) {
        drawFrameAtIndexWithFilter(tgtFBO, outX, outY, outWidth, outHeight, scissorX, scissorY, scissorW, scissorH, frameIndex, this.mFilters0[frameIndex % this.mFilters0.length]);
    }

    public void drawFrameAtIndexWithFilter(int tgtFBO, int outX, int outY, int outWidth, int outHeight, int scissorX, int scissorY, int scissorW, int scissorH, int frameIndex, GPUImageFilter filter) {
        long t1 = System.nanoTime();
        EGL10Helper.clearGLError("before drawFrameAtIndex");
        GLES20.glBindFramebuffer(36160, 0);
        OutputSurfaceArray.Frame f = null;
        if (frameIndex < this.mVideoFrames.size()) {
            f = this.mVideoFrames.get(frameIndex);
        }
        if (f != null) {
            int texid = f.mBuffer.mTextureId;
            if (this.enableStabilizer && this.mTransforms != null) {
                texid = updateStabilizedImage(frameIndex);
            }
            int cropWidth = this.mVideoFrames.mCropWidth;
            int cropHeight = this.mVideoFrames.mCropHeight;
            int captureWidth = this.mVideoFrames.mCaptureWidth;
            int captureHeight = this.mVideoFrames.mCaptureHeight;
            int texWidth = this.mVideoFrames.mPhysWidth;
            int texHeight = this.mVideoFrames.mPhysHeight;
            long t2 = System.nanoTime();
            drawFrame(true, tgtFBO, outX, outY, outWidth, outHeight, 0, 0, scissorW, scissorH, filter, texid, texWidth, texHeight, captureWidth, captureHeight, cropWidth, cropHeight, this.mVideoFrames.mRenderMatrix);
            if (this.mFilters1 != null && this.mFilters1.length > 0) {
                GPUImageFilter filter1 = this.mFilters1[frameIndex % this.mFilters1.length];
                if (filter1 != null) {
                    drawFrame(false, tgtFBO, outX, outY, outWidth, outHeight, (int) (this.x * ((float) outWidth)), (int) (this.y * ((float) outHeight)), (int) (this.w * ((float) scissorW)), (int) (this.h * ((float) scissorH)), filter1, texid, texWidth, texHeight, captureWidth, captureHeight, cropWidth, cropHeight, this.mVideoFrames.mRenderMatrix);
                }
            }
            EGL10Helper.clearGLError("after drawFrameAtIndex");
            long t3 = System.nanoTime();
        }
    }

    public void drawFrameAtIndex2(int tgtFBO, int outX, int outY, int outWidth, int outHeight, int scissorX, int scissorY, int scissorW, int scissorH, int frameIndex) {
        EGL10Helper.clearGLError("before drawFrameAtIndex");
        GPUImageFilter filter = this.mFilters0[frameIndex % this.mFilters0.length];
        int texid = this.mVideoFrames.getFrameByOriginal(frameIndex).mBuffer.mTextureId;
        if (this.enableStabilizer && this.mTransforms != null) {
            texid = updateStabilizedImage(frameIndex);
        }
        if (frameIndex != this.index) {
            this.index = frameIndex;
            drawFrameAtOneFrame(filter, this.index);
        }
        drawFrame(true, tgtFBO, outX, outY, outWidth, outHeight, scissorX, scissorY, scissorW, scissorH, filter, texid, this.mVideoFrames.mPhysWidth, this.mVideoFrames.mPhysHeight, this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight, this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight, this.mVideoFrames.mRenderMatrix);
        EGL10Helper.clearGLError("after drawFrameAtIndex");
    }

    private void drawFrameAtOneFrame(GPUImageFilter filter, int frameIndex) {
        filter.onDrawAtOneFrame(frameIndex);
    }

    public void setViewport(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean resizeFrame(int textureId, int texWidth, int texHeight, int captureWidth, int captureHeight, int filterWidth, int filterHeight, int outWidth, int outHeight) {
        float scale = Math.min(((float) outWidth) / ((float) filterWidth), ((float) outHeight) / ((float) filterHeight));
        if (((double) scale) > 1.0d) {
            return false;
        }
        int targetWidth = (int) (((float) filterWidth) * scale);
        int targetHeight = (int) (((float) filterHeight) * scale);
        if (!(this.mTargetFramebuffer != null && this.mTargetFramebuffer.width == targetWidth && this.mTargetFramebuffer.height == targetHeight)) {
            if (this.mTargetFramebuffer != null) {
                this.mTargetFramebuffer.destroy();
                this.mTargetFramebuffer = null;
            }
            this.mTargetFramebuffer = new Framebuffer("FrameRenderer.mTargetFrameBuffer", targetWidth, targetHeight);
            initFilter(this.mTargetFilter, targetWidth, targetHeight);
        }
        float[] cubeCoords = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        float u1 = ((float) captureWidth) / ((float) texWidth);
        float v1 = ((float) captureHeight) / ((float) texHeight);
        float[] texCoords = new float[]{0.0f, 0.0f, u1, 0.0f, 0.0f, v1, u1, v1};
        this.mGLCubeBuffer.clear();
        this.mGLCubeBuffer.put(cubeCoords).position(0);
        this.mGLTextureBuffer.clear();
        this.mGLTextureBuffer.put(texCoords).position(0);
        this.mTargetFramebuffer.bind();
        this.mTargetFilter.onDraw(textureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
        this.mTargetTextureId = textureId;
        return true;
    }

    public void drawFrame(boolean clearOutput, int tgtfbo, int outX, int outY, int outWidth, int outHeight, int scissorX, int scissorY, int scissorW, int scissorH, GPUImageFilter filter, int textureId, int texWidth, int texHeight, int captureWidth, int captureHeight, int filterWidth, int filterHeight, float[] m) {
        EGL10Helper.clearGLError("before FrameRenderer");
        for (int i = 0; i < 4; i++) {
            GLES20.glActiveTexture(33984 + i);
            GLES20.glBindTexture(3553, 0);
            GLES20.glBindTexture(36197, 0);
        }
        FramebufferCache cache = FramebufferCache.shared;
        Framebuffer source = Framebuffer.createSource(textureId, texWidth, texHeight);
        Framebuffer target = Framebuffer.createTarget(tgtfbo);
        cache.addExternal(source);
        cache.addExternal(target);
        Framebuffer current = source;
        try {
            float[] cubeCoords;
            float[] texCoords;
            long t3;
            if (filter.getClass().equals(GPUImageOESFilter.class) || filter.getClass().equals(GPUImageFilter.class)) {
            }
            if (null == null) {
                long t1 = System.nanoTime();
                if (!(filterWidth == this.mFilterWidth && filterHeight == this.mFilterHeight)) {
                    setFilterDimensions(filterWidth, filterHeight);
                    this.mHaveFilterDimensions = false;
                }
                long t2 = System.nanoTime();
                EGL10Helper.clearGLError("foo2");
                initFilter(filter, this.mFilterWidth, this.mFilterHeight);
                cubeCoords = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
                float u1 = ((float) captureWidth) / ((float) texWidth);
                float v1 = ((float) captureHeight) / ((float) texHeight);
                texCoords = new float[]{0.0f, 0.0f, u1, 0.0f, 0.0f, v1, u1, v1};
                this.mGLCubeBuffer.clear();
                this.mGLCubeBuffer.put(cubeCoords).position(0);
                this.mGLTextureBuffer.clear();
                this.mGLTextureBuffer.put(texCoords).position(0);
                GLES20.glViewport(0, 0, this.mFilterWidth, this.mFilterHeight);
                GLES20.glScissor(0, 0, this.mFilterWidth, this.mFilterHeight);
                t3 = System.nanoTime();
                current = filter.onDraw2(null, current, this.mGLCubeBuffer, this.mGLTextureBuffer);
                drawOverlay(this.mFilterWidth, this.mFilterHeight);
                if (false) {
                    BitmapUtils.savePixels("tmp1.png", this.mFilterWidth, this.mFilterHeight);
                }
                captureWidth = this.mFilterWidth;
                texWidth = captureWidth;
                captureHeight = this.mFilterHeight;
                texHeight = captureHeight;
                EGL10Helper.clearGLError("foo3");
                System.nanoTime();
            }
            t3 = System.nanoTime();
            if (outHeight < 0) {
                throw new RuntimeException("upside down");
            }
            GPUImageFilter simpleFilter;
            cubeCoords = OutputSurfaceArray.getCubeCoords(this.mode, m, this.xOffset, this.yOffset, (float) outWidth, (float) outHeight, (float) filterWidth, (float) filterHeight);
            texCoords = OutputSurfaceArray.transformedTexCoords((double) captureWidth, (double) captureHeight, (double) texWidth, (double) texHeight, false);
            this.mGLCubeBuffer.clear();
            this.mGLCubeBuffer.put(cubeCoords).position(0);
            this.mGLTextureBuffer.clear();
            this.mGLTextureBuffer.put(texCoords).position(0);
            long t4 = System.nanoTime();
            EGL10Helper.clearGLError("foo4");
            GLES20.glBindFramebuffer(36160, tgtfbo);
            if (null != null) {
                simpleFilter = filter;
            } else {
                simpleFilter = this.copyFilter;
            }
            initFilter(simpleFilter, outWidth, outHeight);
            GLES20.glViewport(outX, outY, outWidth, outHeight);
            GLES20.glScissor(scissorX, scissorY, scissorW, scissorH);
            GLES20.glEnable(3089);
            if (clearOutput) {
                GLES20.glClearColor(0.0745f, 0.0706f, 0.0667f, 1.0f);
                GLES20.glClear(16384);
            }
            simpleFilter.onDraw2(target, current, this.mGLCubeBuffer, this.mGLTextureBuffer);
            GLES20.glDisable(3089);
            GLES20.glBindFramebuffer(36160, 0);
            EGL10Helper.clearGLError("after FrameRenderer");
            if (cache.isUsed(source)) {
                Log.w(TAG, "source still in use!");
                cache.offerFramebuffer(source);
            }
            if (!cache.isUsed(target)) {
                Log.w(TAG, "target has been freed!");
            }
            cache.offerFramebuffer(target);
        } catch (RenderException e) {
            Log.e(TAG, "RenderException", e);
        }
    }

    public ArrayList<Bitmap> getBitmaps(EGL10Helper egl, int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices) {
        return getBitmaps(egl, targetWidth, targetHeight, filters, frameIndices, true, false);
    }

    public ArrayList<Bitmap> getBitmaps(EGL10Helper egl, int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices, boolean rotated, boolean cropOrRotate) {
        int width;
        int height;
        float[] m = rotated ? this.mVideoFrames.mRenderMatrix : identity;
        int sourceWidth = (int) Math.abs((((float) this.mVideoFrames.mCaptureWidth) * m[0]) + (((float) this.mVideoFrames.mCaptureHeight) * m[4]));
        int sourceHeight = (int) Math.abs((((float) this.mVideoFrames.mCaptureWidth) * m[1]) + (((float) this.mVideoFrames.mCaptureHeight) * m[5]));
        if (this.mVideoFrames.mCaptureWidth != this.mVideoFrames.mCropWidth) {
            int temp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = temp;
        }
        float ratio = BitmapUtils.squishyRatio(targetWidth, targetHeight, sourceWidth, sourceHeight);
        if (cropOrRotate) {
            width = targetWidth;
            height = targetHeight;
        } else {
            width = (int) Math.ceil((double) (((float) sourceWidth) / ratio));
            height = (int) Math.ceil((double) (((float) sourceHeight) / ratio));
        }
        int startWidth = width;
        int startHeight = height;
        EGLSurface surface = egl.createPBuffer(width, height);
        egl.makeCurrent(surface);
        try {
            GLES20.glBindFramebuffer(36160, 0);
            EGL10Helper.clearGLError("before getBitmaps");
            ArrayList<Bitmap> bitmaps = new ArrayList();
            int length = filters.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
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
                    int frameIndex = frameIndices[i3];
                    width = startWidth;
                    height = startHeight;
                    EGL10Helper.clearGLError("before drawFrameAtIndex");
                    OutputSurfaceArray.Frame f = this.mVideoFrames.get(frameIndex);
                    if (!(f == null || f.mBuffer == null)) {
                        int cropWidth;
                        int cropHeight;
                        int texid = f.mBuffer.mTextureId;
                        if (this.enableStabilizer && this.mTransforms != null) {
                            texid = updateStabilizedImage(frameIndex);
                        }
                        if (this.mVideoFrames.getAlphaFrame) {
                            cropWidth = this.mVideoFrames.mCaptureWidth;
                            cropHeight = this.mVideoFrames.mCaptureHeight;
                        } else {
                            cropWidth = this.mVideoFrames.mCropWidth;
                            cropHeight = this.mVideoFrames.mCropHeight;
                        }
                        int[] crop = CheckUtil.redefineWidthHeight(cropWidth, cropHeight);
                        cropWidth = crop[0];
                        cropHeight = crop[1];
                        drawFrame(true, 0, 0, 0, width, height, 0, 0, width, height, filter, texid, f.mBuffer.mWidth, f.mBuffer.mHeight, this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight, cropWidth + (cropWidth % 2), cropHeight + (cropHeight % 2), m);
                        EGL10Helper.clearGLError("after drawFrameAtIndex");
                        bitmaps.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(targetWidth, targetHeight, width, height)));
                    }
                    i = i3 + 1;
                }
                i = i2 + 1;
            }
            return bitmaps;
        } finally {
            egl.destroySurface(surface);
        }
    }

    public void drawOverlay(int outputWidth, int outputHeight) {
        if (this.mOverlayFilter != null && this.mOverlayTexture != null) {
            initFilter(this.mOverlayFilter, this.mOverlayTexture.width, this.mOverlayTexture.height);
            float outputAspect = ((float) outputWidth) / ((float) outputHeight);
            float overlayAspect = ((float) this.mOverlayTexture.width) / ((float) this.mOverlayTexture.height);
            float x = 0.0f;
            float y = 0.0f;
            float w = this.mOverlayWidth * ((float) outputWidth);
            float h = (((float) this.mOverlayTexture.height) * w) / ((float) this.mOverlayTexture.width);
            int shorSide = Math.min(outputHeight, outputWidth);
            switch (this.mOverlayHAlign) {
                case LEFT:
                    x = (float) ((((double) shorSide) * 0.075d) * 1.5d);
                    break;
                case RIGHT:
                    x = (float) (((double) (((float) outputWidth) - w)) - ((((double) shorSide) * 0.075d) * 1.5d));
                    break;
            }
            switch (this.mOverlayVAlign) {
                case TOP:
                    y = ((float) outputHeight) - h;
                    break;
                case BOTTOM:
                    y = (((float) outputHeight) - ((float) (((double) shorSide) * 0.075d))) - h;
                    break;
            }
            x = (2.0f * (x / ((float) outputWidth))) - 1.0f;
            y = (2.0f * (y / ((float) outputHeight))) - 1.0f;
            float x0 = x;
            float y0 = y;
            float x1 = x + ((w / ((float) outputWidth)) * 2.0f);
            float y1 = y + ((h / ((float) outputHeight)) * 2.0f);
            float[] cubeCoords = new float[]{x0, y1, x1, y1, x0, y0, x1, y0};
            this.mOverlayVerts.clear();
            this.mOverlayVerts.put(cubeCoords).position(0);
            float[] texCoords = new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            this.mOverlayTexCoords.clear();
            this.mOverlayTexCoords.put(texCoords).position(0);
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 1);
            this.mOverlayFilter.onDraw(this.mOverlayTexture.texid(), this.mOverlayVerts, this.mOverlayTexCoords);
            GLES20.glDisable(3042);
        }
    }

    public ArrayList<Bitmap> getBitmaps2(EGL10Helper egl, int targetWidth, int targetHeight, GPUImageFilter[] filters, int[] frameIndices, boolean rotated, boolean cropOrRotate) {
        int width;
        int height;
        float[] m = rotated ? this.mVideoFrames.mRenderMatrix : identity;
        int sourceWidth = (int) Math.abs((((float) this.mVideoFrames.mCaptureWidth) * m[0]) + (((float) this.mVideoFrames.mCaptureHeight) * m[4]));
        int sourceHeight = (int) Math.abs((((float) this.mVideoFrames.mCaptureWidth) * m[1]) + (((float) this.mVideoFrames.mCaptureHeight) * m[5]));
        if (this.mVideoFrames.mCaptureWidth != this.mVideoFrames.mCropWidth) {
            int temp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = temp;
        }
        float ratio = BitmapUtils.squishyRatio(targetWidth, targetHeight, sourceWidth, sourceHeight);
        if (cropOrRotate) {
            width = targetWidth;
            height = targetHeight;
        } else {
            width = (int) Math.ceil((double) (((float) sourceWidth) / ratio));
            height = (int) Math.ceil((double) (((float) sourceHeight) / ratio));
        }
        int startWidth = width;
        int startHeight = height;
        EGLSurface surface = egl.createPBuffer(width, height);
        egl.makeCurrent(surface);
        GLES20.glBindFramebuffer(36160, 0);
        EGL10Helper.clearGLError("before getBitmaps");
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
                    int frameIndex = frameIndices[i3];
                    width = startWidth;
                    height = startHeight;
                    EGL10Helper.clearGLError("before drawFrameAtIndex");
                    OutputSurfaceArray.Frame f = this.mVideoFrames.get(frameIndex);
                    if (!(f == null || f.mBuffer == null)) {
                        int cropWidth;
                        int cropHeight;
                        int texid = f.mBuffer.mTextureId;
                        if (this.enableStabilizer && this.mTransforms != null) {
                            texid = updateStabilizedImage(frameIndex);
                        }
                        if (this.mVideoFrames.getAlphaFrame) {
                            cropWidth = this.mVideoFrames.mCaptureWidth;
                            cropHeight = this.mVideoFrames.mCaptureHeight;
                        } else {
                            cropWidth = this.mVideoFrames.mCropWidth;
                            cropHeight = this.mVideoFrames.mCropHeight;
                        }
                        drawFrame(true, 0, 0, 0, width, height, 0, 0, width, height, filter, texid, this.mVideoFrames.mPhysWidth, this.mVideoFrames.mPhysHeight, this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight, cropWidth, cropHeight, m);
                        EGL10Helper.clearGLError("after drawFrameAtIndex");
                        bitmaps.add(BitmapUtils.flipBitmap(BitmapUtils.captureBitmap(targetWidth, targetHeight, width, height)));
                    }
                    i = i3 + 1;
                }
                filter.destroy();
                filter.destroySecondary();
                i = i2 + 1;
            } else {
                egl.destroySurface(surface);
                return bitmaps;
            }
        }
    }
}