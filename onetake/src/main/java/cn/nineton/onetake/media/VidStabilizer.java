package cn.nineton.onetake.media;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class VidStabilizer {
    static final String TAG = "VidStabilizer";
    int mHeight;
    Shaders.SurfaceRenderer3D mSurfaceRenderer = new Shaders.SurfaceRenderer3D();
    int mWidth;
    Transform[] transforms;

    public static final class Transform {
        public float alpha;
        public float x;
        public float y;
        public float zoom;
    }

    VidStabilizer(OutputSurfaceArray frames) {
        this.mWidth = frames.mCaptureWidth;
        this.mHeight = frames.mCaptureHeight;
    }

    void destroy() {
        this.mSurfaceRenderer.destroy();
    }

    public void setTransforms(Transform[] transforms) {
        this.transforms = transforms;
    }

    public void drawFrameNoStabilization(int outWidth, int outHeight, int textureId) {
        drawFrame(outWidth, outHeight, textureId, -1);
    }

    public synchronized void drawFrame(int outWidth, int outHeight, int textureId, int frameIndex) {
        GLES20.glViewport(0, 0, outWidth, outHeight);
        GLES20.glScissor(0, 0, outWidth, outHeight);
        GLES20.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
        GLES20.glClear(16384);
        float cw = (float) this.mWidth;
        float ch = (float) this.mHeight;
        float[] cubeCoords = new float[]{-cw, -ch, 0.0f, 1.0f, cw, -ch, 0.0f, 1.0f, cw, ch, 0.0f, 1.0f, -cw, ch, 0.0f, 1.0f};
        float[] texCoords = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        this.mSurfaceRenderer.draw(4, cubeCoords, texCoords, textureId, getStablizedMatrix(frameIndex, (float) this.mWidth, (float) this.mHeight));
    }

    float[] getStablizedMatrix(int frameIndex, float w, float h) {
        float x;
        float y;
        float alpha;
        float zoom;
        float[] mv = new float[16];
        Matrix.setIdentityM(mv, 0);
        if (frameIndex == -1 || this.transforms == null || frameIndex < 0 || frameIndex >= this.transforms.length) {
            x = 0.0f;
            y = 0.0f;
            alpha = 0.0f;
            zoom = 0.0f;
        } else {
            Transform t = this.transforms[frameIndex];
            x = t.x;
            y = t.y;
            alpha = t.alpha;
            zoom = t.zoom;
        }
        float z = 1.0f - (zoom / 100.0f);
        float hw = w / 2.0f;
        float hh = h / 2.0f;
        float cos_a = (float) Math.cos((double) (-alpha));
        float sin_a = (float) Math.sin((double) (-alpha));
        float sx = w;
        float sy = h;
        mv[0] = cos_a / (sx * z);
        mv[1] = sin_a / (sy * z);
        mv[4] = (-sin_a) / (sx * z);
        mv[5] = cos_a / (sy * z);
        mv[12] = x / hw;
        mv[13] = y / hh;
        return mv;
    }
}