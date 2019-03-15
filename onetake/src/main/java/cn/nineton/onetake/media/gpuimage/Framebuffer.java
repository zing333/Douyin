package cn.nineton.onetake.media.gpuimage;

import android.opengl.GLES20;
import android.util.Log;

public class Framebuffer {
    private static final String TAG = "Framebuffer";
    long context;
    boolean destroyed;
    private final int[] fbos;
    public final int format;
    public final int height;
    final boolean owned;
    private FramebufferTexture texture;
    public final int width;

    public Framebuffer(String usage, int width, int height) {
        this(usage, width, height, 6407);
    }

    public Framebuffer(String usage, int width, int height, int format) {
        this.fbos = new int[]{0};
        this.width = width;
        this.height = height;
        this.format = format;
        this.owned = true;
        this.context = OpenGlUtils.currentContextHandle();
        this.texture = new FramebufferTexture(width, height, format);
        GLES20.glGenFramebuffers(1, this.fbos, 0);
        GLES20.glBindFramebuffer(36160, this.fbos[0]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, texid(), 0);
        if (GLES20.glCheckFramebufferStatus(36160) != 36053) {
            Log.e(TAG, String.format("framebuffer incomplete: width:%d, height:%d format:%d", new Object[]{Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(format)}));
            throw new RuntimeException("framebuffer incomplete.");
        }
        Log.d(TAG, String.format("allocated a new fbo:%d for context:%d usage:%s texture:%d width:%d height:%d", new Object[]{Integer.valueOf(this.fbos[0]), Long.valueOf(this.context), usage, Integer.valueOf(texid()), Integer.valueOf(width), Integer.valueOf(height)}));
    }

    public Framebuffer(String usage, FramebufferTexture texture) {
        this(usage, texture, false);
    }

    public Framebuffer(String usage, FramebufferTexture texture, boolean silent) {
        this.fbos = new int[]{0};
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.format = texture.format;
        this.owned = true;
        this.context = OpenGlUtils.currentContextHandle();
        this.texture = texture;
        GLES20.glGenFramebuffers(1, this.fbos, 0);
        GLES20.glBindFramebuffer(36160, this.fbos[0]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, texid(), 0);
        if (GLES20.glCheckFramebufferStatus(36160) != 36053) {
            Log.e(TAG, String.format("framebuffer incomplete: width:%d, height:%d format:%d", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.format)}));
            throw new RuntimeException("framebuffer incomplete.");
        }
    }

    public Framebuffer(int width, int height, int format, int fbo, int tex) {
        this.fbos = new int[]{0};
        this.width = width;
        this.height = height;
        this.format = format;
        this.context = OpenGlUtils.currentContextHandle();
        this.fbos[0] = fbo;
        this.texture = new FramebufferTexture(width, height, format, tex);
        this.owned = false;
    }

    public static Framebuffer createTarget(int fbo) {
        return new Framebuffer(0, 0, 0, fbo, -1);
    }

    public static Framebuffer createSource(int tex, int width, int height) {
        return new Framebuffer(width, height, 0, 0, tex);
    }

    public synchronized void destroy() {
        this.destroyed = true;
        if (this.owned) {
            if (this.texture != null) {
                this.texture.destroy();
            }
            checkContext();
            if (this.fbos[0] != 0) {
                GLES20.glDeleteFramebuffers(1, this.fbos, 0);
                this.fbos[0] = 0;
            }
        }
        this.texture = null;
    }

    public void checkContext() {
        if (OpenGlUtils.currentContextHandle() != this.context) {
            Log.e(TAG, "FBO context mismatch");
            throw new RenderException("FBO context mismatch");
        }
    }

    public int transferTextureOwnership() {
        int texid = texid();
        this.texture = null;
        return texid;
    }

    public void bind() {
        GLES20.glBindFramebuffer(36160, fboid());
        GLES20.glViewport(0, 0, this.width, this.height);
        GLES20.glScissor(0, 0, this.width, this.height);
    }

    public int fboid() {
        checkContext();
        if (!this.destroyed) {
            return this.fbos[0];
        }
        Log.e(TAG, "fbo already destroyed");
        return 0;
    }

    public int texid() {
        return this.texture.texid();
    }
}