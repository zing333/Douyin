package cn.nineton.onetake.media;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import cn.nineton.onetake.media.gpuimage.ResourceTracker;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;

public class Texture {
    final int height;
    final int[] texs = new int[]{0};
    final int width;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glGenTextures(1, this.texs, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.texs[0]);
        GLES20.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, null);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
    }

    public Texture(Bitmap bitmap) {
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.texs[0] = OpenGlUtils.loadTexture(bitmap, -1, false);
    }

    public int texid() {
        return this.texs[0];
    }

    public void destroy() {
        ResourceTracker.freeTexture(this.texs[0]);
    }
}