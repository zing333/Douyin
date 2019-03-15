package cn.nineton.onetake.media.gpuimage;

import android.opengl.GLES20;
import android.util.Log;

public class FramebufferTexture {
    static final String TAG = "FramebufferTexture";
    private int[] fbos;
    final int format;
    final int height;
    boolean owned;
    private final int[] texs;
    final int width;

    public FramebufferTexture(int width, int height, int format) {
        this.texs = new int[]{-1};
        this.fbos = new int[1];
        this.width = width;
        this.height = height;
        this.format = format;
        this.owned = true;
        OpenGlUtils.clearGLError("before texture alloc");
        GLES20.glGenTextures(1, this.texs, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.texs[0]);
        GLES20.glTexImage2D(3553, 0, format, width, height, 0, format, 5121, null);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        int error = GLES20.glGetError();
        if (error != 0) {
            throw new RuntimeException("got error during texture allocation:" + error);
        }
        Log.d(TAG, String.format("allocated new framebuffer storage: texture:%d width:%d height:%d", new Object[]{Integer.valueOf(this.texs[0]), Integer.valueOf(width), Integer.valueOf(height)}));
        int bpp = 3;
        if (format == 6408) {
            bpp = 4;
        }
        ResourceTracker.markTexture(this.texs[0], "texture", width, height, bpp);
    }

    public FramebufferTexture(int width, int height, int format, int tex) {
        this.texs = new int[]{-1};
        this.fbos = new int[1];
        this.width = width;
        this.height = height;
        this.format = format;
        this.owned = false;
        this.texs[0] = tex;
    }

    public void destroy() {
        if (this.owned && this.texs[0] != -1) {
            ResourceTracker.freeTexture(this.texs[0]);
            this.texs[0] = -1;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int texid() {
        return this.texs[0];
    }

    public int bindAsFramebuffer() {
        GLES20.glGenFramebuffers(1, this.fbos, 0);
        GLES20.glBindFramebuffer(36160, this.fbos[0]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, texid(), 0);
        if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
            return this.fbos[0];
        }
        throw new RuntimeException("framebuffer incomplete.");
    }

    public void unbindAsFramebuffer() {
        GLES20.glDeleteFramebuffers(1, this.fbos, 0);
        GLES20.glBindFramebuffer(36160, 0);
    }

    public int transferOwnership() {
        int texid = this.texs[0];
        this.texs[0] = 0;
        this.owned = false;
        return texid;
    }
}