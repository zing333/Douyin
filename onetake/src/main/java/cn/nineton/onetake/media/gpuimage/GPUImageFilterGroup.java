package cn.nineton.onetake.media.gpuimage;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class GPUImageFilterGroup extends GPUImageFilter {
    static final String TAG = "GPUImageFilterGroup";
    public List<GPUImageFilter> mFilters;
    private final FloatBuffer mGLCubeBuffer;
    private final FloatBuffer mGLTextureBuffer;
    private final FloatBuffer mGLTextureFlipBuffer;
    protected List<GPUImageFilter> mMergedFilters;

    public GPUImageFilterGroup() {
        this(null);
    }

    public GPUImageFilterGroup(List<GPUImageFilter> filters) {
        this.mFilters = filters;
        if (this.mFilters == null) {
            this.mFilters = new ArrayList();
        } else {
            updateMergedFilters();
        }
        this.mGLCubeBuffer = ByteBuffer.allocateDirect(GPUImageRenderer.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer.put(GPUImageRenderer.CUBE).position(0);
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureBuffer.put(TextureRotationUtil.TEXTURE_CUBE).position(0);
        float[] flipTexture = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        this.mGLTextureFlipBuffer = ByteBuffer.allocateDirect(flipTexture.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureFlipBuffer.put(flipTexture).position(0);
    }

    public void addFilter(GPUImageFilter aFilter) {
        if (aFilter != null) {
            this.mFilters.add(aFilter);
            updateMergedFilters();
        }
    }

    public synchronized void onInit() {
        super.onInit();
        for (GPUImageFilter filter : this.mFilters) {
            filter.init();
        }
    }

    public synchronized void onDestroy() {
        for (GPUImageFilter filter : this.mFilters) {
            filter.destroy();
        }
        super.onDestroy();
    }

    public void destroySecondary() {
        for (GPUImageFilter filter : this.mFilters) {
            filter.destroySecondary();
        }
        super.destroySecondary();
    }

    public synchronized void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        for (int i = 0; i < this.mFilters.size(); i++) {
            ((GPUImageFilter) this.mFilters.get(i)).onOutputSizeChanged(width, height);
        }
    }

    public void setAlpha(float value) {
    }

    @SuppressLint({"WrongCall"})
    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        GLES20.glUseProgram(this.mGLProgId);
        if (getClass() == GPUImageFilterGroup.class && !this.mRunOnDraw.isEmpty()) {
            Log.e(TAG, "filter group has a pending onDraw task, weird.");
            this.mRunOnDraw.clear();
        }
        runPendingOnDrawTasks();
        if (isInitialized()) {
            Framebuffer current = source;
            if (this.mMergedFilters == null) {
                return current;
            }
            int size = this.mMergedFilters.size();
            int i = 0;
            while (i < size) {
                GPUImageFilter filter = (GPUImageFilter) this.mMergedFilters.get(i);
                if (i == size + -1) {
                    current = filter.onDraw2(target, current, cubeBuffer, textureBuffer);
                } else {
                    current = filter.onDraw2(null, current, cubeBuffer, textureBuffer);
                }
                cubeBuffer = this.mGLCubeBuffer;
                textureBuffer = this.mGLTextureBuffer;
                i++;
            }
            return current;
        }
        Log.e(TAG, "rendering filter which is not initialized");
        return null;
    }

    public List<GPUImageFilter> getFilters() {
        return this.mFilters;
    }

    public List<GPUImageFilter> getMergedFilters() {
        return this.mMergedFilters;
    }

    public void updateMergedFilters() {
        if (this.mFilters != null) {
            if (this.mMergedFilters == null) {
                this.mMergedFilters = new ArrayList();
            } else {
                this.mMergedFilters.clear();
            }
            for (GPUImageFilter filter : this.mFilters) {
                if (filter instanceof GPUImageFilterGroup) {
                    ((GPUImageFilterGroup) filter).updateMergedFilters();
                    List<GPUImageFilter> filters = ((GPUImageFilterGroup) filter).getMergedFilters();
                    if (!(filters == null || filters.isEmpty())) {
                        this.mMergedFilters.addAll(filters);
                    }
                } else {
                    this.mMergedFilters.add(filter);
                }
            }
        }
    }

    public void onDrawAtOneFrame(int frameIndex) {
        super.onDrawAtOneFrame(frameIndex);
        for (GPUImageFilter filter : this.mMergedFilters) {
            filter.onDrawAtOneFrame(frameIndex);
        }
    }
}