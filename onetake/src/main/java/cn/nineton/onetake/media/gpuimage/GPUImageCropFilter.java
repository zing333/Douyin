package cn.nineton.onetake.media.gpuimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GPUImageCropFilter extends GPUImageFilter {
    public static final String CROP_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n \n void main()\n {\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n }";
    public static final int INPUT_ROTATION_FLIP_VERTICAL = 2;
    public static final int INPUT_ROTATION_NORMAL = 0;
    public static final int INPUT_ROTATION_ROTATE_LEFT = 1;
    public static final int INPUT_ROTATION_ROTATE_RIGHT = 3;
    private float[] cropTextureCoordinates;
    private int mCaptureOrientation;
    private CropRegion mCropRegion;
    private int mInputRotation;
    private boolean mIsFrontCamera;
    private int mScreenOrientation;

    public static class CropRegion {
        float mHeight;
        float mWidth;
        float mX;
        float mY;

        public CropRegion(float x, float y, float width, float height) {
            this.mX = x;
            this.mY = y;
            this.mWidth = width;
            this.mHeight = height;
        }

        public String toString() {
            return "CropRegion{mX=" + this.mX + ", mY=" + this.mY + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + '}';
        }
    }

    public void setScreenOrientation(int screenOrientation) {
        this.mScreenOrientation = screenOrientation;
    }

    public int getScreenOrientation() {
        return this.mScreenOrientation;
    }

    public GPUImageCropFilter cloneFilter() {
        return new GPUImageCropFilter(this.mCropRegion, this.mInputRotation, this.mCaptureOrientation, this.mIsFrontCamera);
    }

    public GPUImageCropFilter(int captureOrientation, boolean isFrontCamera) {
        this(new CropRegion(0.0f, 0.0f, 1.0f, 1.0f), 0, captureOrientation, isFrontCamera);
    }

    public GPUImageCropFilter(CropRegion cropRegion, int inputRotation, int captureOrientation, boolean isFrontCamera) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, GPUImageFilter.NO_FILTER_FRAGMENT_SHADER);
        this.cropTextureCoordinates = new float[16];
        this.mInputRotation = inputRotation;
        this.mCaptureOrientation = captureOrientation;
        this.mIsFrontCamera = isFrontCamera;
        setCropRegion(cropRegion);
    }

    public void onInitialized() {
        super.onInitialized();
        setCropRegion(this.mCropRegion);
    }

    public void setCropRegion(CropRegion cropRegion) {
        this.mCropRegion = cropRegion;
        float finalX = this.mCropRegion.mX;
        float finalY = this.mCropRegion.mY;
        float finalW = this.mCropRegion.mWidth;
        float finalH = this.mCropRegion.mHeight;
        switch (this.mCaptureOrientation) {
            case 0:
                finalX = this.mCropRegion.mY;
                finalY = (1.0f - this.mCropRegion.mX) - this.mCropRegion.mWidth;
                finalW = this.mCropRegion.mHeight;
                finalH = this.mCropRegion.mWidth;
                break;
            case 90:
                finalX = (1.0f - this.mCropRegion.mX) - this.mCropRegion.mWidth;
                finalY = (1.0f - this.mCropRegion.mY) - this.mCropRegion.mHeight;
                finalW = this.mCropRegion.mWidth;
                finalH = this.mCropRegion.mHeight;
                break;
            case 180:
                finalX = (1.0f - this.mCropRegion.mY) - this.mCropRegion.mHeight;
                finalY = this.mCropRegion.mY;
                finalW = this.mCropRegion.mHeight;
                finalH = this.mCropRegion.mWidth;
                break;
        }
        float tempX = finalX;
        float tempY = finalY;
        float tempW = finalW;
        float tempH = finalH;
        if (this.mIsFrontCamera) {
            tempX = (1.0f - finalX) - finalW;
            tempY = finalY;
            tempW = finalW;
            tempH = finalH;
        }
        float minX = tempX;
        float minY = tempY;
        float maxX = tempX + tempW;
        float maxY = tempY + tempH;
        switch (this.mInputRotation) {
            case 0:
                this.cropTextureCoordinates[0] = maxX;
                this.cropTextureCoordinates[1] = minY;
                this.cropTextureCoordinates[2] = minX;
                this.cropTextureCoordinates[3] = minY;
                this.cropTextureCoordinates[4] = maxX;
                this.cropTextureCoordinates[5] = maxY;
                this.cropTextureCoordinates[6] = minX;
                this.cropTextureCoordinates[7] = maxY;
                return;
            case 1:
                this.cropTextureCoordinates[0] = maxX;
                this.cropTextureCoordinates[1] = minY;
                this.cropTextureCoordinates[2] = minX;
                this.cropTextureCoordinates[3] = minY;
                this.cropTextureCoordinates[4] = maxX;
                this.cropTextureCoordinates[5] = maxY;
                this.cropTextureCoordinates[6] = minX;
                this.cropTextureCoordinates[7] = maxY;
                return;
            case 3:
                this.cropTextureCoordinates[6] = maxX;
                this.cropTextureCoordinates[7] = minY;
                this.cropTextureCoordinates[4] = minX;
                this.cropTextureCoordinates[5] = minY;
                this.cropTextureCoordinates[2] = maxX;
                this.cropTextureCoordinates[3] = maxY;
                this.cropTextureCoordinates[0] = minX;
                this.cropTextureCoordinates[1] = maxY;
                return;
            default:
                return;
        }
    }

    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        return super.onDraw2(target, source, convertArrayToBuffer(new float[]{1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f}), convertArrayToBuffer(this.cropTextureCoordinates));
    }

    private FloatBuffer convertArrayToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(array);
        return buffer;
    }
}