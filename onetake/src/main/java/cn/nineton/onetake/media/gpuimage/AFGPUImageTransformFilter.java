package cn.nineton.onetake.media.gpuimage;

import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class AFGPUImageTransformFilter extends GPUImageFilter {
    public static final String TRANSFORM_VERTEX_SHADER = "attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n uniform mat4 transformMatrix;\n uniform mat4 orthographicMatrix;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n     gl_Position = transformMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;\n     textureCoordinate = inputTextureCoordinate.xy;\n }";
    private boolean anchorTopLeft;
    private int currentHeight;
    private int currentWidth;
    private boolean ignoreAspectRatio;
    private float mOrientation = 0.0f;
    private float[] orthographicMatrix;
    private int orthographicMatrixUniform = getUniformLocation("orthographicMatrix");
    private float[] transform3D;
    private int transformMatrixUniform = getUniformLocation("transformMatrix");
    private float videoRatio = 0.0f;

    public AFGPUImageTransformFilter(int mOrientation) {
        super(TRANSFORM_VERTEX_SHADER, "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     highp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = color;\n}");
        this.mOrientation = (float) mOrientation;
        this.orthographicMatrix = new float[16];
        Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
        this.transform3D = new float[16];
        Matrix.setIdentityM(this.transform3D, 0);
    }

    public void setVideoRatio(int videoWidth, int videoHeight) {
        this.videoRatio = (((float) videoWidth) * 1.0f) / ((float) videoHeight);
        onOutputSizeChanged(getOutputWidth(), getOutputHeight());
    }

    public void setOrientation(float mOrientation) {
        this.mOrientation = mOrientation;
    }

    public float getOrientation() {
        return this.mOrientation;
    }

    public void onInit() {
        super.onInit();
        setUniformMatrix4f(this.transformMatrixUniform, this.transform3D);
        setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    }

    public void onInitialized() {
        super.onInitialized();
    }

    public void onOutputSizeChanged(int width, int height) {
        if (this.videoRatio != 0.0f) {
            if (width > height) {
                height = (int) ((((float) width) * 1.0f) / this.videoRatio);
            } else {
                width = (int) (((float) height) * this.videoRatio);
            }
        }
        super.onOutputSizeChanged(width, height);
        if (!this.ignoreAspectRatio) {
            Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, (((float) height) * -1.0f) / ((float) width), (((float) height) * 1.0f) / ((float) width), -1.0f, 1.0f);
            setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
        }
    }

    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        FloatBuffer vertBuffer = cubeBuffer;
        if (!this.ignoreAspectRatio) {
            float[] adjustedVertices = new float[8];
            cubeBuffer.position(0);
            cubeBuffer.get(adjustedVertices);
            float normalizedHeight = ((float) getOutputHeight()) / ((float) getOutputWidth());
            adjustedVertices[1] = adjustedVertices[1] * normalizedHeight;
            adjustedVertices[3] = adjustedVertices[3] * normalizedHeight;
            adjustedVertices[5] = adjustedVertices[5] * normalizedHeight;
            adjustedVertices[7] = adjustedVertices[7] * normalizedHeight;
            float[] newAdjustedVertices = new float[8];
            if (this.mOrientation == 0.0f) {
                for (int i = 0; i < 8; i++) {
                    newAdjustedVertices[i] = adjustedVertices[i];
                }
            } else if (this.mOrientation == 90.0f) {
                newAdjustedVertices[0] = adjustedVertices[4];
                newAdjustedVertices[1] = adjustedVertices[5];
                newAdjustedVertices[2] = adjustedVertices[0];
                newAdjustedVertices[3] = adjustedVertices[1];
                newAdjustedVertices[4] = adjustedVertices[6];
                newAdjustedVertices[5] = adjustedVertices[7];
                newAdjustedVertices[6] = adjustedVertices[2];
                newAdjustedVertices[7] = adjustedVertices[3];
            } else if (this.mOrientation == 180.0f) {
                newAdjustedVertices[0] = adjustedVertices[6];
                newAdjustedVertices[1] = adjustedVertices[7];
                newAdjustedVertices[2] = adjustedVertices[4];
                newAdjustedVertices[3] = adjustedVertices[5];
                newAdjustedVertices[4] = adjustedVertices[2];
                newAdjustedVertices[5] = adjustedVertices[3];
                newAdjustedVertices[6] = adjustedVertices[0];
                newAdjustedVertices[7] = adjustedVertices[1];
            } else if (this.mOrientation == 270.0f) {
                newAdjustedVertices[0] = adjustedVertices[2];
                newAdjustedVertices[1] = adjustedVertices[3];
                newAdjustedVertices[2] = adjustedVertices[6];
                newAdjustedVertices[3] = adjustedVertices[7];
                newAdjustedVertices[4] = adjustedVertices[0];
                newAdjustedVertices[5] = adjustedVertices[1];
                newAdjustedVertices[6] = adjustedVertices[4];
                newAdjustedVertices[7] = adjustedVertices[5];
            }
            vertBuffer = ByteBuffer.allocateDirect(newAdjustedVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertBuffer.put(newAdjustedVertices).position(0);
        }
        return super.onDraw2(target, source, vertBuffer, textureBuffer);
    }

    public void setTransform3D(float[] transform3D) {
        this.transform3D = transform3D;
        setUniformMatrix4f(this.transformMatrixUniform, transform3D);
    }

    public float[] getTransform3D() {
        return this.transform3D;
    }

    public void setIgnoreAspectRatio(boolean ignoreAspectRatio) {
        this.ignoreAspectRatio = ignoreAspectRatio;
        if (ignoreAspectRatio) {
            Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
            setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
            return;
        }
        onOutputSizeChanged(getOutputWidth(), getOutputHeight());
    }

    public boolean ignoreAspectRatio() {
        return this.ignoreAspectRatio;
    }

    public void setAnchorTopLeft(boolean anchorTopLeft) {
        this.anchorTopLeft = anchorTopLeft;
        setIgnoreAspectRatio(this.ignoreAspectRatio);
    }

    public boolean anchorTopLeft() {
        return this.anchorTopLeft;
    }
}