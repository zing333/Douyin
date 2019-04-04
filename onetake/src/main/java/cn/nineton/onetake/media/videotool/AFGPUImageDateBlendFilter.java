package cn.nineton.onetake.media.videotool;

import android.graphics.Bitmap;
import android.opengl.GLES20;
//import com.blink.academy.onetake.support.ByteBufferUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import cn.nineton.onetake.media.gpuimage.GPUImageTwoInputFilter;
import cn.nineton.onetake.util.ByteBufferUtils;
//import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;

public class AFGPUImageDateBlendFilter extends GPUImageTwoInputFilter {
    private static final String SCREEN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n uniform lowp float overPercent;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2) * overPercent;\n     if (textureCoordinate2.x<=0.0 || textureCoordinate2.x>=1.0 || textureCoordinate2.y<=0.0 || textureCoordinate2.y>=1.0){\n         textureColor2 = vec4(0.0);\n     }     mediump vec4 whiteColor = vec4(1.0);\n     gl_FragColor = whiteColor - ((whiteColor - textureColor2) * (whiteColor - textureColor));\n }";
    private int blendHeight;
    private int blendWidth;
    private int inputHeight;
    private int inputWidth;
    private ByteBuffer mByteBuffer;
    private int mCaptureOrientation;
    private float[] mFirstBuffer;
    private boolean mIsFrontCamera;
    private float mOverPercent;
    private int mOverPercentLocation;

    public AFGPUImageDateBlendFilter() {
        this(1.0f);
    }

    public AFGPUImageDateBlendFilter(float mOverPercent) {
        super(SCREEN_BLEND_FRAGMENT_SHADER);
        this.mFirstBuffer = new float[]{0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f};
        this.mByteBuffer = convertArrayToBuffer(this.mFirstBuffer);
        this.mOverPercentLocation = getUniformLocation("overPercent");
        this.mOverPercent = mOverPercent;
    }

    public void onInit() {
        super.onInit();
    }

    private ByteBuffer convertArrayToBuffer(float[] array) {
        float[] bufferByDepth = ByteBufferUtils.getBufferByDepth(array, 0, 0);
        ByteBuffer bb = ByteBuffer.allocateDirect(bufferByDepth.length * 4);
        bb.order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(bufferByDepth);
        return bb;
    }

    public void setCaptureOrientation(int captureOrientation, boolean isFrontCamera) {
        this.mCaptureOrientation = captureOrientation;
        this.mIsFrontCamera = isFrontCamera;
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        this.inputHeight = height;
        this.inputWidth = width;
    }

    protected void onDrawArraysPre() {
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        }
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFilterSourceTexture2);
        GLES20.glUniform1i(getPhysUniformLocation(this.mFilterInputTextureUniform2), 3);
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            int disagree;
            float[] firstBuffer;
            this.mTexture2CoordinatesBuffer.position(0);
            float minLen = (float) Math.min(this.inputWidth, this.inputHeight);
            float textH = minLen * 0.053f;
            float textW = (((float) this.blendWidth) * textH) / ((float) this.blendHeight);
            float space = minLen * 0.0695f;
            float spaceR = space * 1.5f;
            if (this.mCaptureOrientation == 0 || this.mCaptureOrientation == 180) {
                disagree = 3;
            } else {
                disagree = -3;
            }
            boolean mirrorH = false;
            boolean mirrorV = false;
            float minX;
            float minY;
            float maxX;
            float maxY;
            float t;
            if (this.inputWidth > this.inputHeight + disagree) {
                if (this.mCaptureOrientation == 90) {
                    mirrorH = true;
                    mirrorV = true;
                }
                if (this.mCaptureOrientation == 180) {
                    mirrorH = true;
                    mirrorV = true;
                }
                if (this.mIsFrontCamera) {
                    mirrorH = !mirrorH;
                }
                minX = -(((((float) this.inputWidth) - spaceR) - textW) / textW);
                minY = -(((((float) this.inputHeight) - space) - textH) / textH);
                maxX = (((float) this.inputWidth) / textW) + minX;
                maxY = (((float) this.inputHeight) / textH) + minY;
                if (mirrorH) {
                    t = minX;
                    minX = maxX;
                    maxX = t;
                }
                if (mirrorV) {
                    t = minY;
                    minY = maxY;
                    maxY = t;
                }
                firstBuffer = new float[]{minX, minY, maxX, minY, minX, maxY, maxX, maxY};
            } else {
                if (this.mCaptureOrientation == 0) {
                    mirrorH = true;
                    mirrorV = true;
                }
                if (this.mCaptureOrientation == 90) {
                    mirrorH = true;
                    mirrorV = true;
                }
                if (this.mIsFrontCamera) {
                    mirrorV = !mirrorV;
                }
                minX = -(((((float) this.inputHeight) - spaceR) - textW) / textW);
                maxY = -(((((float) this.inputWidth) - space) - textH) / textH);
                maxX = (((float) this.inputHeight) / textW) + minX;
                minY = (((float) this.inputWidth) / textH) + maxY;
                if (mirrorH) {
                    t = minX;
                    minX = maxX;
                    maxX = t;
                }
                if (mirrorV) {
                    t = minY;
                    minY = maxY;
                    maxY = t;
                }
                firstBuffer = new float[]{minX, minY, minX, maxY, maxX, minY, maxX, maxY};
            }
            this.mByteBuffer = convertArrayToBuffer(firstBuffer);
            GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.mByteBuffer);
        }
    }

    public void onInitialized() {
        super.onInitialized();
        setOverPercent(this.mOverPercent);
    }

    public void setOverPercent(float overPercent) {
        this.mOverPercent = overPercent;
        setFloat(this.mOverPercentLocation, this.mOverPercent);
    }

    public AFGPUImageDateBlendFilter cloneFilter() {
        AFGPUImageDateBlendFilter filter = new AFGPUImageDateBlendFilter();
        filter.setCaptureOrientation(this.mCaptureOrientation, this.mIsFrontCamera);
        filter.setOverPercent(this.mOverPercent);
        filter.setBitmap(this.mBitmap);
        return filter;
    }

    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        this.blendHeight = bitmap.getHeight();
        this.blendWidth = bitmap.getWidth();
    }
}