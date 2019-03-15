package cn.nineton.onetake.media.gpuimage;

import android.opengl.Matrix;

public class GPUImageTextureMatrixFilter extends GPUImageFilter {
    static final String FRAGMENT_SHADER = "precision highp float;\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main(void)\n{\n    vec2 tc = textureCoordinate;\n    if(tc.x < 0.0) tc.x += 1.0;\n    if(tc.y < 0.0) tc.y += 1.0;\n    vec4 color = texture2D(inputImageTexture, tc);\n    gl_FragColor = color;\n}\n";
    public static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 inputTextureMatrix;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (inputTextureMatrix * inputTextureCoordinate).xy;\n}";
    float[] mTextureMatrix = new float[16];
    int mTextureMatrixLocation = getUniformLocation("inputTextureMatrix");

    public GPUImageTextureMatrixFilter() {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 inputTextureMatrix;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (inputTextureMatrix * inputTextureCoordinate).xy;\n}", FRAGMENT_SHADER);
        Matrix.setIdentityM(this.mTextureMatrix, 0);
    }

    public void onInitialized() {
        super.onInitialized();
        setUniformMatrix4f(this.mTextureMatrixLocation, this.mTextureMatrix);
    }

    public void setMatrix(float[] mat) {
        for (int i = 0; i < 16; i++) {
            this.mTextureMatrix[i] = mat[i];
        }
        setUniformMatrix4f(this.mTextureMatrixLocation, this.mTextureMatrix);
    }
}