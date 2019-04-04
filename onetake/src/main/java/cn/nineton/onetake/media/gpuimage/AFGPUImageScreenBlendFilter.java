package cn.nineton.onetake.media.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import cn.nineton.onetake.util.ByteBufferUtils;
import cn.nineton.onetake.util.LogUtil;

public class AFGPUImageScreenBlendFilter extends GPUImageTwoInputFilter {
    public static final int DEPTH_ONE = 1;
    public static final int DEPTH_THREE = 3;
    public static final int DEPTH_TWO = 2;
    public static final int DEPTH_ZERO = 0;
    public static final int ORIENTATION_BOTTOM = 3;
    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_NONE = 0;
    public static final int ORIENTATION_RIGHT = 2;
    public static final String SCREEN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n uniform lowp float overPercent;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2) * overPercent;\n     mediump vec4 whiteColor = vec4(1.0);\n     gl_FragColor = whiteColor - ((whiteColor - textureColor2) * (whiteColor - textureColor));\n }";
    boolean changed;
    private ByteBuffer mByteBuffer;
    private int mCaptureOrientation;
    private float[] mCurBuffer;
    private int mDepth;
    private float[] mFirstBuffer;
    private boolean mIsFrontCamera;
    private float mLastScale;
    private int mOrientation;
    private float mOverPercent;
    private int mOverPercentLocation;

    public AFGPUImageScreenBlendFilter() {
        this(1.0f);
    }

    public AFGPUImageScreenBlendFilter(float mOverPercent) {
        super(SCREEN_BLEND_FRAGMENT_SHADER);
        this.mFirstBuffer = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mCurBuffer = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mByteBuffer = convertArrayToBuffer(this.mFirstBuffer);
        this.mDepth = 0;
        this.changed = false;
        this.mOrientation = 1;
        this.mOverPercentLocation = getUniformLocation("overPercent");
        this.mOverPercent = mOverPercent;
    }

    public void onInit() {
        super.onInit();
    }

    public void onInitialized() {
        super.onInitialized();
        setOverPercent(this.mOverPercent);
    }

    public void setOverPercent(float overPercent) {
        this.mOverPercent = overPercent;
        setFloat(this.mOverPercentLocation, this.mOverPercent);
    }

    protected void onDrawArraysPre() {
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        }
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFilterSourceTexture2);
        GLES20.glUniform1i(getPhysUniformLocation(this.mFilterInputTextureUniform2), 3);
        if (this.mFilterSecondTextureCoordinateAttribute != -1) {
            this.mTexture2CoordinatesBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.mByteBuffer);
        }
    }

    public AFGPUImageScreenBlendFilter cloneFilter() {
        AFGPUImageScreenBlendFilter filter = new AFGPUImageScreenBlendFilter();
        filter.setCaptureOrientation(this.mCaptureOrientation, this.mIsFrontCamera);
        filter.setOverPercent(this.mOverPercent);
        filter.setDepth(this.mDepth);
        filter.setBitmap(this.mBitmap);
        filter.setOrientation(this.mOrientation);
        filter.setByteBuffer(this.mByteBuffer);
        return filter;
    }

    private void setByteBuffer(ByteBuffer byteBuffer) {
        this.mByteBuffer = byteBuffer;
    }

    public float[] getFirstBuffer() {
        return this.mFirstBuffer;
    }

    public float[] getCurBuffer() {
        return this.mCurBuffer;
    }

    public void setByteBufferFirst(float[] buffer) {
        this.mFirstBuffer = buffer;
        this.mCurBuffer = buffer;
        setByteBuffer(convertArrayToBuffer(this.mFirstBuffer));
    }

    public void setByteBuffer(float[] buffer) {
        this.mCurBuffer = buffer;
        setByteBuffer(convertArrayToBuffer(this.mCurBuffer));
    }

    private ByteBuffer convertArrayToBuffer(float[] array) {
        float[] bufferByDepth = ByteBufferUtils.getBufferByDepth(array, this.mDepth, this.mCaptureOrientation);
        ByteBuffer bb = ByteBuffer.allocateDirect(bufferByDepth.length * 4);
        bb.order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(bufferByDepth);
        return bb;
    }

    public void setDepth(int depth) {
        this.mDepth = depth;
    }

    public int getDepth() {
        return this.mDepth;
    }

    public void setCaptureOrientation(int captureOrientation, boolean isFrontCamera) {
        this.mCaptureOrientation = captureOrientation;
        this.mIsFrontCamera = isFrontCamera;
    }

    public void refreshCropLeakFilterFirst(float scale) {
        if (scale > 0.0f) {
            if (scale < 1.0f) {
                scale = 1.0f / scale;
                this.changed = true;
                LogUtil.d("refreshCropLeakFilterFirst", "scale : " + scale + " changed : " + this.changed);
            }
            this.mLastScale = scale;
            if (this.mBitmap != null) {
                int width = this.mBitmap.getWidth();
                int height = this.mBitmap.getHeight();
                float[] newBuffer = new float[8];
                dealPoint(scale, width, height, (((float) width) * 1.0f) / ((float) height), newBuffer);
                newBuffer = ByteBufferUtils.getFloatByCaptureOrientationValue(newBuffer, this.mCaptureOrientation, this.changed);
                if (this.mIsFrontCamera) {
                    float[] frontCameraValue = getFrontCameraValue(newBuffer);
                    newBuffer[0] = frontCameraValue[0];
                    newBuffer[1] = frontCameraValue[1];
                    newBuffer[2] = frontCameraValue[2];
                    newBuffer[3] = frontCameraValue[3];
                    newBuffer[4] = frontCameraValue[4];
                    newBuffer[5] = frontCameraValue[5];
                    newBuffer[6] = frontCameraValue[6];
                    newBuffer[7] = frontCameraValue[7];
                }
                setByteBufferFirst(newBuffer);
            }
        }
    }

    private void dealPoint(float scale, int width, int height, float originF, float[] newBuffer) {
        if (scale > originF) {
            float b = (1.0f - ((((float) width) / scale) / ((float) height))) / 2.0f;
            switch (this.mOrientation) {
                case 3:
                    newBuffer[0] = 0.0f;
                    newBuffer[1] = (2.0f * b) + 0.0f;
                    newBuffer[2] = 1.0f;
                    newBuffer[3] = (2.0f * b) + 0.0f;
                    newBuffer[4] = 0.0f;
                    newBuffer[5] = 1.0f;
                    newBuffer[6] = 1.0f;
                    newBuffer[7] = 1.0f;
                    return;
                default:
                    newBuffer[0] = 0.0f;
                    newBuffer[1] = 0.0f + b;
                    newBuffer[2] = 1.0f;
                    newBuffer[3] = 0.0f + b;
                    newBuffer[4] = 0.0f;
                    newBuffer[5] = 1.0f - b;
                    newBuffer[6] = 1.0f;
                    newBuffer[7] = 1.0f - b;
                    return;
            }
        }
        float a = 1.0f - ((((float) height) * scale) / ((float) width));
        switch (this.mOrientation) {
            case 0:
                newBuffer[0] = (a / 2.0f) + 0.0f;
                newBuffer[1] = 0.0f;
                newBuffer[2] = 1.0f - (a / 2.0f);
                newBuffer[3] = 0.0f;
                newBuffer[4] = (a / 2.0f) + 0.0f;
                newBuffer[5] = 1.0f;
                newBuffer[6] = 1.0f - (a / 2.0f);
                newBuffer[7] = 1.0f;
                return;
            case 2:
                newBuffer[0] = 0.0f + a;
                newBuffer[1] = 0.0f;
                newBuffer[2] = 1.0f;
                newBuffer[3] = 0.0f;
                newBuffer[4] = 0.0f + a;
                newBuffer[5] = 1.0f;
                newBuffer[6] = 1.0f;
                newBuffer[7] = 1.0f;
                return;
            default:
                newBuffer[0] = 0.0f;
                newBuffer[1] = 0.0f;
                newBuffer[2] = 1.0f - a;
                newBuffer[3] = 0.0f;
                newBuffer[4] = 0.0f;
                newBuffer[5] = 1.0f;
                newBuffer[6] = 1.0f - a;
                newBuffer[7] = 1.0f;
                return;
        }
    }

    private float[] getFrontCameraValue(float[] buffer) {
        return new float[]{buffer[2], buffer[3], buffer[0], buffer[1], buffer[6], buffer[7], buffer[4], buffer[5]};
    }

    public void refreshCropLeakFilter(float scale) {
        this.mLastScale = scale;
        if (this.mBitmap != null) {
            int width = this.mBitmap.getWidth();
            int height = this.mBitmap.getHeight();
            float[] newBuffer = new float[8];
            dealPoint(scale, width, height, (((float) width) * 1.0f) / ((float) height), newBuffer);
            setByteBuffer(ByteBufferUtils.getFloatByCaptureOrientationValue(newBuffer, this.mCaptureOrientation, this.changed));
        }
    }

    public void restoreDepth() {
        this.mDepth = 0;
    }

    public void onClickForDepth() {
        if (this.mOverPercent != 0.0f) {
            this.mDepth++;
            if (this.mDepth > 3) {
                this.mDepth = 0;
            }
        }
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public void setBitmap(Bitmap bitmap, int orientation, int degree) {
        setBitmap(bitmap, orientation);
    }

    public void setBitmap(Bitmap bitmap, int orientation) {
        if (this.mOrientation != orientation) {
        }
        this.mOrientation = orientation;
        setBitmap(bitmap);
        refreshCropLeakFilterFirst(this.mLastScale);
    }

    public String toString() {
        return "AFGPUImageScreenBlendFilter{mOverPercentLocation=" + this.mOverPercentLocation + ", mOverPercent=" + this.mOverPercent + ", mFirstBuffer=" + Arrays.toString(this.mFirstBuffer) + ", mCurBuffer=" + Arrays.toString(this.mCurBuffer) + ", mByteBuffer=" + this.mByteBuffer + ", mLastScale=" + this.mLastScale + ", mCaptureOrientation=" + this.mCaptureOrientation + ", mIsFrontCamera=" + this.mIsFrontCamera + ", mDepth=" + this.mDepth + ", changed=" + this.changed + ", mOrientation=" + this.mOrientation + '}';
    }
}