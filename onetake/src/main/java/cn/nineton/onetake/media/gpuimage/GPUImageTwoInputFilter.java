package cn.nineton.onetake.media.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GPUImageTwoInputFilter extends GPUImageFilter {
    private static final String TAG = "GPUImageTwoInputFilter";
    private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}";
    protected Bitmap mBitmap;
    public int mFilterInputTextureUniform2;
    public int mFilterSecondTextureCoordinateAttribute;
    public int mFilterSourceTexture2;
    private boolean mFilterSourceTexture2IsExternal;
    private boolean mFilterSourceTexture2IsSecondary;
    FloatBuffer mGLTextureBuffer;
    private GPUImageFilter mSecondFilter;
    Framebuffer mSecondSource;
    protected ByteBuffer mTexture2CoordinatesBuffer;

    public GPUImageTwoInputFilter(String fragmentShader) {
        this(VERTEX_SHADER, fragmentShader);
    }

    public GPUImageTwoInputFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        this.mFilterSourceTexture2 = -1;
        this.mFilterSourceTexture2IsSecondary = false;
        this.mFilterSourceTexture2IsExternal = false;
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        setRotation(Rotation.NORMAL, false, false);
        this.mFilterInputTextureUniform2 = getUniformLocation("inputImageTexture2");
    }

    public void setSecondFilter(GPUImageFilter filter) {
        this.mSecondFilter = filter;
    }

    public void onInit() {
        super.onInit();
        this.mFilterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        }
        if (!(this.mBitmap == null || this.mBitmap.isRecycled())) {
            setBitmap(this.mBitmap);
        }
        this.mGLTextureBuffer.put(TextureRotationUtil.TEXTURE_CUBE).position(0);
        if (this.mSecondFilter != null) {
            this.mSecondFilter.init();
            setRotation(Rotation.NORMAL, false, true);
        }
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        if (this.mSecondFilter != null) {
            this.mSecondFilter.onOutputSizeChanged(width, height);
        }
    }

    public void setBitmap(final Bitmap bitmap) {
        if (bitmap == null || !bitmap.isRecycled()) {
            this.mBitmap = bitmap;
            if (this.mBitmap != null) {
                runOnDraw(new Runnable() {
                    public void run() {
                        if (GPUImageTwoInputFilter.this.mFilterSourceTexture2 == -1 && bitmap != null && !bitmap.isRecycled()) {
                            GLES20.glActiveTexture(33987);
                            GPUImageTwoInputFilter.this.mFilterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, -1, false);
                        }
                    }
                });
            }
        }
    }

    public void setTexture(int id) {
        this.mFilterSourceTexture2 = id;
        this.mFilterSourceTexture2IsSecondary = false;
        this.mFilterSourceTexture2IsExternal = false;
    }

    public void setSecondaryTexture(int id) {
        this.mFilterSourceTexture2 = id;
        this.mFilterSourceTexture2IsSecondary = true;
        this.mFilterSourceTexture2IsExternal = false;
    }

    public void setExternalTexture(int id) {
        this.mFilterSourceTexture2 = id;
        this.mFilterSourceTexture2IsSecondary = false;
        this.mFilterSourceTexture2IsExternal = true;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void recycleBitmap() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
    }

    public void onDestroy() {
        if (this.mSecondFilter != null) {
            this.mSecondFilter.destroy();
        } else if (!this.mFilterSourceTexture2IsSecondary) {
            destroyTexture();
        }
        super.onDestroy();
    }

    public void destroySecondary() {
        if (this.mSecondFilter != null) {
            this.mSecondFilter.destroySecondary();
        }
        if (this.mFilterSourceTexture2IsSecondary) {
            destroyTexture();
        }
    }

    private void destroyTexture() {
        if (this.mFilterSourceTexture2 != -1 && !this.mFilterSourceTexture2IsExternal) {
            ResourceTracker.freeTexture(this.mFilterSourceTexture2);
            this.mFilterSourceTexture2 = -1;
        }
    }

    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        FramebufferCache cache = FramebufferCache.shared;
        if (this.mSecondFilter != null) {
            cache.lock(source);
            this.mSecondSource = this.mSecondFilter.onDraw2(null, source, cubeBuffer, this.mGLTextureBuffer);
            cache.unlock(source);
        }
        Framebuffer result = super.onDraw2(target, source, cubeBuffer, textureBuffer);
        if (this.mSecondSource != null) {
            FramebufferCache.shared.offerFramebuffer(this.mSecondSource);
            this.mSecondSource = null;
        }
        return result;
    }

    protected void onDrawArraysPre() {
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        }
        GLES20.glActiveTexture(33987);
        if (this.mSecondSource != null) {
            GLES20.glBindTexture(3553, this.mSecondSource.texid());
        } else {
            GLES20.glBindTexture(3553, this.mFilterSourceTexture2);
        }
        GLES20.glUniform1i(getPhysUniformLocation(this.mFilterInputTextureUniform2), 3);
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            this.mTexture2CoordinatesBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.mTexture2CoordinatesBuffer);
        }
    }

    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();
        this.mTexture2CoordinatesBuffer = bBuffer;
    }
}