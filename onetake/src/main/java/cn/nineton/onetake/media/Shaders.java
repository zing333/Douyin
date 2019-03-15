package cn.nineton.onetake.media;

import android.opengl.GLES20;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;

public class Shaders {

    public static final class LineRenderer {
        float[] color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        private final String fragmentShader = "precision mediump float;uniform vec4 vColor;void main() {  gl_FragColor = vColor;}";
        protected int mColorHandle = GLES20.glGetUniformLocation(this.mGLProgId, "vColor");
        protected int mGLProgId = OpenGlUtils.loadProgram("uniform mat4 uMVPMatrix;attribute vec4 vPosition;void main() {  gl_Position = uMVPMatrix * vPosition;}", "precision mediump float;uniform vec4 vColor;void main() {  gl_FragColor = vColor;}");
        protected int mMVPHandle = GLES20.glGetUniformLocation(this.mGLProgId, "uMVPMatrix");
        protected int mPositionHandle = GLES20.glGetAttribLocation(this.mGLProgId, "vPosition");
        private FloatBuffer mVertexBuffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asFloatBuffer();
        private final String vertexShader = "uniform mat4 uMVPMatrix;attribute vec4 vPosition;void main() {  gl_Position = uMVPMatrix * vPosition;}";

        public void destroy() {
            OpenGlUtils.unloadProgram(this.mGLProgId);
        }

        public void SetColor(float red, float green, float blue, float alpha) {
            this.color[0] = red;
            this.color[1] = green;
            this.color[2] = blue;
            this.color[3] = alpha;
        }

        public void draw(float[] mvpMatrix, float x0, float y0, float x1, float y1) {
            float[] coords = new float[]{x0, y0, x1, y1};
            GLES20.glUseProgram(this.mGLProgId);
            this.mVertexBuffer.put(coords);
            this.mVertexBuffer.position(0);
            GLES20.glEnableVertexAttribArray(this.mPositionHandle);
            GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 8, this.mVertexBuffer);
            GLES20.glUniform4fv(this.mColorHandle, 1, this.color, 0);
            GLES20.glUniformMatrix4fv(this.mMVPHandle, 1, false, mvpMatrix, 0);
            GLES20.glDrawArrays(1, 0, 2);
            GLES20.glDisableVertexAttribArray(this.mPositionHandle);
        }
    }

    public static final class PointRenderer {
        static final String fragmentShader = "void main()\n{\n     gl_FragColor = vec4(1,0,0,1);\n}";
        static final String vertexShader = "attribute vec4 position;\n\nuniform mat4 mvp;\n \nvoid main()\n{\n    gl_Position = mvp * position;\n    gl_PointSize = 10.0;\n}";
        FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder()).asFloatBuffer();
        int mGLAttribPosition;
        int mGLProgId = OpenGlUtils.loadProgram(vertexShader, fragmentShader);
        int mGLUniformMVP;

        public PointRenderer() {
            if (this.mGLProgId == -1) {
                throw new AssertionError();
            }
            this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
            this.mGLUniformMVP = GLES20.glGetUniformLocation(this.mGLProgId, "mvp");
        }

        public void destroy() {
            OpenGlUtils.unloadProgram(this.mGLProgId);
        }

        public void activate(float[] mvp) {
            GLES20.glUseProgram(this.mGLProgId);
            GLES20.glUniformMatrix4fv(this.mGLUniformMVP, 1, false, mvp, 0);
        }

        public void draw(int n, float[] coords) {
            this.cubeBuffer.clear();
            this.cubeBuffer.put(coords).position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, this.cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            GLES20.glDrawArrays(0, 0, n);
        }
    }

    public static final class SurfaceRenderer3D {
        static final String fragmentShader = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
        static final String vertexShader = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform mat4 mvp;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = mvp * position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
        FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(3200).order(ByteOrder.nativeOrder()).asFloatBuffer();
        IntBuffer indexBuffer = ByteBuffer.allocateDirect(1600).order(ByteOrder.nativeOrder()).asIntBuffer();
        int mGLAttribPosition;
        int mGLAttribTextureCoordinate;
        int mGLProgId = OpenGlUtils.loadProgram(vertexShader, fragmentShader);
        int mGLUniformMVP;
        int mGLUniformTexture;
        FloatBuffer textureBuffer = ByteBuffer.allocateDirect(1600).order(ByteOrder.nativeOrder()).asFloatBuffer();

        public SurfaceRenderer3D() {
            if (this.mGLProgId == -1) {
                throw new AssertionError();
            }
            this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
            this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
            this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
            this.mGLUniformMVP = GLES20.glGetUniformLocation(this.mGLProgId, "mvp");
        }

        public void destroy() {
            OpenGlUtils.unloadProgram(this.mGLProgId);
        }

        public void draw(int n, float[] cubeCoords, float[] texCoords, int textureId, float[] mvp) {
            GLES20.glUseProgram(this.mGLProgId);
            this.cubeBuffer.clear();
            this.cubeBuffer.put(cubeCoords).position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 4, 5126, false, 0, this.cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            this.textureBuffer.position(0);
            this.textureBuffer.put(texCoords).position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, textureId);
            GLES20.glUniform1i(this.mGLUniformTexture, 0);
            GLES20.glUniformMatrix4fv(this.mGLUniformMVP, 1, false, mvp, 0);
            GLES20.glDrawArrays(6, 0, n);
            GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
            GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glBindTexture(3553, 0);
        }

        public void drawIndices(int[] indices, float[] cubeCoords, float[] texCoords, int textureId, float[] mvp) {
            GLES20.glUseProgram(this.mGLProgId);
            this.indexBuffer.clear();
            this.indexBuffer.put(indices).position(0);
            this.cubeBuffer.clear();
            this.cubeBuffer.put(cubeCoords).position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 4, 5126, false, 0, this.cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            this.textureBuffer.position(0);
            this.textureBuffer.put(texCoords).position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, textureId);
            GLES20.glUniform1i(this.mGLUniformTexture, 0);
            GLES20.glUniformMatrix4fv(this.mGLUniformMVP, 1, false, mvp, 0);
            GLES20.glDrawElements(5, indices.length, 5125, this.indexBuffer);
            GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
            GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glBindTexture(3553, 0);
        }
    }

    public static String readRawTextFile(int resId) {
        BufferedReader buffreader = new BufferedReader(new InputStreamReader(App.getContext().getResources().openRawResource(resId)));
        StringBuilder text = new StringBuilder();
        while (true) {
            try {
                String line = buffreader.readLine();
                if (line == null) {
                    return text.toString();
                }
                text.append(line);
                text.append(10);
            } catch (IOException e) {
                return null;
            }
        }
    }
}