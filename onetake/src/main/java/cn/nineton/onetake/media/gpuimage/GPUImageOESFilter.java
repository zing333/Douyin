package cn.nineton.onetake.media.gpuimage;

import android.opengl.GLES20;
import java.nio.FloatBuffer;

public class GPUImageOESFilter extends GPUImageFilter {
    public static final String OES_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;\n\nuniform samplerExternalOES inputImageTexture;\n\nvoid main()\n{\n    highp vec4 col = texture2D(inputImageTexture, textureCoordinate);\n    if(textureCoordinate.x < 0.0 || textureCoordinate.y < 0.0 || textureCoordinate.x > 1.0 || textureCoordinate.y > 1.0) {\n       col = vec4(0.0, 0.0, 0.0, 1.0);\n    }\n    gl_FragColor = col;\n}\n";

    public GPUImageOESFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, OES_FRAGMENT_SHADER);
    }

    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        if (target == null) {
            target = FramebufferCache.shared.takeFramebuffer(this.mOutputWidth, this.mOutputHeight, 6408);
        }
        GLES20.glBindFramebuffer(36160, target.fboid());
        GLES20.glUseProgram(this.mGLProgId);
        runPendingOnDrawTasks();
        cubeBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindTexture(36197, source.texid());
        GLES20.glUniform1i(getPhysUniformLocation(this.mGLUniformTexture), 0);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
        GLES20.glBindTexture(36197, 0);
        FramebufferCache.shared.offerFramebuffer(source);
        return target;
    }
}