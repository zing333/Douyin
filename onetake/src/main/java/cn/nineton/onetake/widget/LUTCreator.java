package cn.nineton.onetake.widget;

import android.opengl.GLES20;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.media.gpuimage.FramebufferTexture;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageFilterGroup;
import cn.nineton.onetake.media.gpuimage.ResourceTracker;
import cn.nineton.onetake.util.CheckUtil;

public class LUTCreator {
    static final String TAG = "LUTCreator";
    static int _index = 0;
    public static final FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    public static final FloatBuffer mGLTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private static boolean mInitialized = false;
    private static int mTextureId = -1;
    private GPUImageFilter mFilter = null;
    private List<GPUImageFilter> mLastFilters = new ArrayList();

    public static synchronized void initialize() {
        synchronized (LUTCreator.class) {
            if (!mInitialized) {
                mInitialized = true;
                int[] textures = new int[1];
                GLES20.glGenTextures(1, textures, 0);
                GLES20.glBindTexture(3553, textures[0]);
                GLES20.glTexParameterf(3553, 10240, 9729.0f);
                GLES20.glTexParameterf(3553, 10241, 9729.0f);
                GLES20.glTexParameterf(3553, 10242, 33071.0f);
                GLES20.glTexParameterf(3553, 10243, 33071.0f);
                mTextureId = textures[0];
                byte[] data = new byte[98304];
                for (int slice = 0; slice < 32; slice++) {
                    for (int y = 0; y < 32; y++) {
                        for (int x = 0; x < 32; x++) {
                            float g = ((float) y) / 31.0f;
                            float b = ((float) slice) / 31.0f;
                            int offset = ((y * 3072) + (x * 3)) + ((slice * 32) * 3);
                            data[offset + 0] = (byte) ((int) (255.0f * (((float) x) / 31.0f)));
                            data[offset + 1] = (byte) ((int) (255.0f * g));
                            data[offset + 2] = (byte) ((int) (255.0f * b));
                        }
                    }
                }
                ByteBuffer buffer = ByteBuffer.allocateDirect(98304);
                buffer.put(data);
                buffer.position(0);
                GLES20.glTexImage2D(3553, 0, 6407, 1024, 32, 0, 6407, 5121, buffer);
                ResourceTracker.markTexture(mTextureId, TAG, 1024, 32, 4);
                float[] cubeCoords = new float[]{-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
                float[] texCoords = new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
                mGLCubeBuffer.clear();
                mGLCubeBuffer.put(cubeCoords).position(0);
                mGLTextureBuffer.clear();
                mGLTextureBuffer.put(texCoords).position(0);
            }
        }
    }

    public static synchronized void shutdown() {
        synchronized (LUTCreator.class) {
            if (mTextureId != -1) {
                ResourceTracker.freeTexture(mTextureId);
                mTextureId = -1;
            }
            mInitialized = false;
        }
    }

    public void destroy() {
        if (this.mFilter != null) {
            this.mFilter.destroy();
            this.mFilter.destroySecondary();
            this.mFilter = null;
        }
    }

    public void update(GPUImageFilter filter, FramebufferTexture target) {
        long t1 = System.nanoTime();
        if (!CheckUtil.checkTwoFilterIsSame(this.mFilter, filter)) {
            if (this.mFilter != null) {
                this.mFilter.destroy();
            }
            this.mFilter = filter;
            this.mFilter.init();
            this.mFilter.onOutputSizeChanged(1024, 32);
        }
        long t2 = System.nanoTime();
        target.bindAsFramebuffer();
        GLES20.glViewport(0, 0, 1024, 32);
        GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear(16384);
        GLES20.glDisable(3089);
        this.mFilter.onDraw(mTextureId, mGLCubeBuffer, mGLTextureBuffer);
        long t3 = System.nanoTime();
        target.unbindAsFramebuffer();
        GLES20.glBindFramebuffer(36160, 0);
        long t4 = System.nanoTime();
        Log.d(TAG, String.format("init:%dms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
        Log.d(TAG, String.format("draw:%dms", new Object[]{Long.valueOf((t3 - t2) / 1000000)}));
        Log.d(TAG, String.format("bind:%dms", new Object[]{Long.valueOf((t4 - t3) / 1000000)}));
    }

    public void update2(GPUImageFilter filter, FramebufferTexture target) {
        long t1 = System.nanoTime();
        if (!CheckUtil.checkTwoFilterIsSame(this.mFilter, filter)) {
            if (this.mFilter != null) {
                List<GPUImageFilter> filters = ((GPUImageFilterGroup) this.mFilter).mFilters;
                GPUImageFilter filter1 = (GPUImageFilter) filters.remove(filters.size() - 1);
                filter1.destroy();
                filter1.destroySecondary();
                List<GPUImageFilter> currentLastFilters = ((GPUImageFilterGroup) filter).mFilters;
                GPUImageFilter currentFilter = (GPUImageFilter) currentLastFilters.remove(currentLastFilters.size() - 1);
                currentFilter.init();
                currentFilter.onOutputSizeChanged(1024, 32);
                ((GPUImageFilterGroup) this.mFilter).addFilter(currentFilter);
            } else {
                this.mFilter = filter;
                this.mFilter.init();
                this.mFilter.onOutputSizeChanged(1024, 32);
            }
        }
        long t2 = System.nanoTime();
        target.bindAsFramebuffer();
        GLES20.glViewport(0, 0, 1024, 32);
        GLES20.glClearColor(1.0f, 0.0f, 0.5f, 1.0f);
        GLES20.glClear(16384);
        GLES20.glDisable(3089);
        this.mFilter.onDraw(mTextureId, mGLCubeBuffer, mGLTextureBuffer);
        long t3 = System.nanoTime();
        target.unbindAsFramebuffer();
        long t4 = System.nanoTime();
        Log.d(TAG, String.format("init:%dms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
        Log.d(TAG, String.format("draw:%dms", new Object[]{Long.valueOf((t3 - t2) / 1000000)}));
        Log.d(TAG, String.format("bind:%dms", new Object[]{Long.valueOf((t4 - t3) / 1000000)}));
    }

    void saveData(ByteBuffer data, String path) {
        try {
            FileOutputStream os = new FileOutputStream(path);
            WritableByteChannel channel = Channels.newChannel(os);
            channel.write(data);
            channel.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static int texid() {
        return mTextureId;
    }
}