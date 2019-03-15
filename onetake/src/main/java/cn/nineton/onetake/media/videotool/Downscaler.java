package cn.nineton.onetake.media.videotool;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.nineton.onetake.media.gpuimage.GPUImageFilter;


public class Downscaler extends GPUImageFilter {
    static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying highp vec2 textureCoordinate;\nuniform highp vec2 clamp_min;\nuniform highp vec2 clamp_max;\n\nuniform samplerExternalOES inputImageTexture;\nuniform bool opaque;\n\nuniform highp vec2 scale;\nuniform highp vec2 dDx;\nuniform highp vec2 dDy;\n\n#define FILTER_SHARPNESS    2.0\n#define AREA_LIMIT          128.0\n#define LENGTH_LIMIT        32\n#define FILTER_FUNC         gaussFilter\n\n#define M_PI 3.14159265358979323846\n\n\n//========================= FILTER FUNCTIONS =======================\n// We only use the Gaussian filter function. The other filters give \n// very similar results.\n\nfloat boxFilter(float r2){\n    return 1.0;\n}\n\nfloat gaussFilter(float r2){\n    float alpha = FILTER_SHARPNESS;\n    return exp(-alpha * r2);\n}\n\nfloat triFilter(float r2){\n    float alpha = FILTER_SHARPNESS;\n    float r= sqrt(r2);\n    return max(0.0, 1.-r/alpha);\n}\n\nfloat sinc(float x){\n    return sin(M_PI*x)/(M_PI*x);\n}\n\nfloat lanczosFilter(float r2){\n    if (r2==0.0)\n        return 1.0;\n    float r= sqrt(r2);\n    return sinc(r)*sinc(r/1.3);\n}\n\n//catmull-rom filter\nfloat crFilter(float r2){\n    float r = sqrt(r2);\n    return (r>=2.)?.0:(r<1.)?(3.*r*r2-5.*r2+2.):(-r*r2+5.*r2-8.*r+4.);\n}\n\nfloat quadraticFilter(float r2){\n    float a = FILTER_SHARPNESS;\n    return 1.0 - r2/(a*a);\n}\n\nfloat cubicFilter(float r2){\n    float a = FILTER_SHARPNESS;\n    float r = sqrt(r2);\n    return 1.0 - 3.0*r2/(a*a) + 2.0*r*r2/(a*a*a);\n}\n\nvec4 texture2DEWA(vec2 coords) {\n\n    vec2 du = dDx;\n    vec2 dv = dDy;\n\n    if((length(du) + length(dv)) < 4.0)\n        return texture2D(inputImageTexture, clamp(coords/scale, clamp_min, clamp_max));\n\n    vec2 p = coords - vec2(0.5,0.5);\n\n    float ux = du.s;\n    float vx = du.t;\n    float uy = dv.s;\n    float vy = dv.t;\n\n\n    // compute ellipse coefficients \n    // A*x*x + B*x*y + C*y*y = F.\n    float A = vx*vx+vy*vy+1.0;\n    float B = -2.0*(ux*vx+uy*vy);\n    float C = ux*ux+uy*uy+1.0;\n    float F = A*C-B*B/4.0;\n\n    // Compute the ellipse's (u,v) bounding box in texture space\n    float bbox_du = 2.0 / (-B*B+4.0*C*A) * sqrt((-B*B+4.0*C*A)*C*F);\n    float bbox_dv = 2.0 / (-B*B+4.0*C*A) * sqrt(A*(-B*B+4.0*C*A)*F);\n\n    // Clamp individual dimensions\n    bbox_du = min(bbox_du, float(LENGTH_LIMIT));\n    bbox_dv = min(bbox_dv, float(LENGTH_LIMIT));\n\n    // Clamp to max area\n    if(bbox_du*bbox_dv>AREA_LIMIT){\n        float ll = sqrt(bbox_du*bbox_dv / AREA_LIMIT);\n        bbox_du/=ll;\n        bbox_dv/=ll;\n    }\n\n    //the ellipse bbox              \n    int u0 = int(floor(p.s - bbox_du));\n    int u1 = int(ceil (p.s + bbox_du));\n    int v0 = int(floor(p.t - bbox_dv));\n    int v1 = int(ceil (p.t + bbox_dv));\n\n\n    // Heckbert MS thesis, p. 59; scan over the bounding box of the ellipse\n    // and incrementally update the value of Ax^2+Bxy*Cy^2; when this\n    // value, q, is less than F, we're inside the ellipse so we filter\n    // away..\n    vec4 num = vec4(0.0);\n    float den = 0.0;\n    float ddq = 2.0 * A;\n    float U = float(u0) - p.s;\n\n    // 4 pixels per iteration\n    for(int vcnt = 0; vcnt < LENGTH_LIMIT; vcnt+=2) {\n        int v = v0 + vcnt;\n        if(v > v1)\n            break;\n\n        float V = float(v) - p.t;\n        float dq = A*(2.0*U+1.0) + B*V;\n        float q = (C*V + B*U)*V + A*U*U;\n        \n        float V2 = float(v)+1.0 - p.t;\n        float dq2 = A*(2.0*U+1.0) + B*V2;\n        float q2 = (C*V2 + B*U)*V2 + A*U*U;\n\n        for (int ucnt = 0; ucnt < LENGTH_LIMIT; ucnt+=2) {\n            int u = u0 + ucnt;\n            if(u > u1)\n                break;\n\n            float w1 = FILTER_FUNC(q / F);\n            w1 = (q < F)? w1: 0.0;\n            q += dq;\n            dq += ddq;\n            float w2 = FILTER_FUNC(q / F);\n            w2 = (q < F)? w2: 0.0;\n                        \n            float w3 = FILTER_FUNC(q2 / F);\n            //w3 = (q2 < F)? w3: 0;\n            q2 += dq2;\n            dq2 += ddq;\n            float w4 = FILTER_FUNC(q2 / F);\n            //w4 = (q2 < F)? w4: 0;\n            \n            q += dq;\n            dq += ddq;\n            q2 += dq2;\n            dq2 += ddq;\n            \n            float offset_v = (w3+w4)/(w1+w2+w3+w4);\n            float offset_u = (w4)/(w4+w3);\n            float weight =(w1+w2+w3+w4);\n\n            if(weight>0.1)\n            {\n                vec2 uv = vec2( float(u)+offset_u+0.5, float(v)+offset_v+0.5);\n                num += weight * texture2D(inputImageTexture, clamp(uv/scale, clamp_min, clamp_max));\n                den += weight;\n            }\n        }\n    }\n\n    vec4 color = num*(1./den);\n\n    return color;\n}\n\n\nvoid main(void)\n{\n    vec4 color = texture2DEWA(textureCoordinate*scale);\n\n    if(opaque)\n        color.a = 1.0;\n\n    gl_FragColor = color;\n}\n";
    static final String SIMPLE_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying highp vec2 textureCoordinate;\n\nuniform samplerExternalOES inputImageTexture;\nuniform bool opaque;\n\nvoid main(void)\n{\n    vec4 color = texture2D(inputImageTexture, textureCoordinate);\n\n    if(opaque)\n        color.a = 1.0;\n\n    gl_FragColor = color;\n}\n";
    public static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 inputTextureMatrix;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (inputTextureMatrix * inputTextureCoordinate).xy;\n}";
    private boolean mApplyCroppingFix;
    private FloatBuffer mGLCubeBuffer;
    private FloatBuffer mGLTextureBuffer;
    private String mRenderer;
    private boolean mUseSimple;

    private static final class TestShader {
        final float AREA_LIMIT = 128.0f;
        final float FILTER_SHARPNESS = 2.0f;
        final float LENGTH_LIMIT = 32.0f;
        final float M_PI = 3.1415927f;
        float[] clamp_max = new float[2];
        float[] clamp_min = new float[2];
        float[] dDx;
        float[] dDy;
        float[] mat;
        float[] scale;

        private TestShader() {
        }

        float boxFilter(float r2) {
            return 1.0f;
        }

        float gaussFilter(float r2) {
            return (float) Math.exp((double) ((-2.0f) * r2));
        }

        void texture2DEWA(float ps, float pt) {
            Log.d("foo", String.format("ps:%f pt:%f ", new Object[]{Float.valueOf(ps), Float.valueOf(pt)}));
            ps -= 0.5f;
            pt -= 0.5f;
            float ux = this.dDx[0];
            float vx = this.dDx[1];
            float uy = this.dDy[0];
            float vy = this.dDy[1];
            float A = ((vx * vx) + (vy * vy)) + 1.0f;
            float B = -2.0f * ((ux * vx) + (uy * vy));
            float C = ((ux * ux) + (uy * uy)) + 1.0f;
            float F = (A * C) - ((B * B) / 4.0f);
            float bbox_dv = (float) (((double) (2.0f / (((-B) * B) + ((4.0f * C) * A)))) * Math.sqrt((double) (((((-B) * B) + ((4.0f * C) * A)) * A) * F)));
            float bbox_du = Math.min((float) (((double) (2.0f / (((-B) * B) + ((4.0f * C) * A)))) * Math.sqrt((double) (((((-B) * B) + ((4.0f * C) * A)) * C) * F))), 32.0f);
            bbox_dv = Math.min(bbox_dv, 32.0f);
            if (bbox_du * bbox_dv > 128.0f) {
                float ll = (float) Math.sqrt((double) ((bbox_du * bbox_dv) / 128.0f));
                bbox_du /= ll;
                bbox_dv /= ll;
            }
            int u0 = (int) Math.floor((double) (ps - bbox_du));
            int u1 = (int) Math.ceil((double) (ps + bbox_du));
            int v0 = (int) Math.floor((double) (pt - bbox_dv));
            int v1 = (int) Math.ceil((double) (pt + bbox_dv));
            float ddq = 2.0f * A;
            float U = ((float) u0) - ps;
            int vcnt = 0;
            while (((float) vcnt) < 32.0f) {
                int v = v0 + vcnt;
                if (v <= v1) {
                    float V = ((float) v) - pt;
                    float dq = (((2.0f * U) + 1.0f) * A) + (B * V);
                    float q = (((C * V) + (B * U)) * V) + ((A * U) * U);
                    float V2 = (((float) v) + 1.0f) - pt;
                    float dq2 = (((2.0f * U) + 1.0f) * A) + (B * V2);
                    float q2 = (((C * V2) + (B * U)) * V2) + ((A * U) * U);
                    for (int ucnt = 0; ((float) ucnt) < 32.0f; ucnt += 2) {
                        int u = u0 + ucnt;
                        if (u > u1) {
                            break;
                        }
                        float w1 = gaussFilter(q / F);
                        if (q >= F) {
                            w1 = 0.0f;
                        }
                        q += dq;
                        dq += ddq;
                        float w2 = gaussFilter(q / F);
                        if (q >= F) {
                            w2 = 0.0f;
                        }
                        float w3 = gaussFilter(q2 / F);
                        q2 += dq2;
                        dq2 += ddq;
                        float w4 = gaussFilter(q2 / F);
                        q += dq;
                        dq += ddq;
                        q2 += dq2;
                        dq2 += ddq;
                        float offset_v = (w3 + w4) / (((w1 + w2) + w3) + w4);
                        float offset_u = w4 / (w4 + w3);
                        if (((double) (((w1 + w2) + w3) + w4)) > 0.1d) {
                            float texu = (((float) u) + offset_u) + 0.5f;
                            float texv = (((float) v) + offset_v) + 0.5f;
                            Log.d("foo", String.format("sampled %f,%f weight:%f", new Object[]{Float.valueOf(texu), Float.valueOf(texv), Float.valueOf(((w1 + w2) + w3) + w4)}));
                        }
                    }
                    vcnt += 2;
                } else {
                    return;
                }
            }
        }

        public void run(int outw, int outh) {
            Log.d("foo", String.format("outw/h:%d %d ", new Object[]{Integer.valueOf(outw), Integer.valueOf(outh)}));
            Log.d("foo", String.format("scale :%f %f ", new Object[]{Float.valueOf(this.scale[0]), Float.valueOf(this.scale[1])}));
            for (int y = 0; y < outh; y += 100) {
                float t0 = ((float) y) / ((float) outh);
                float t1 = ((float) (y + 1)) / ((float) outh);
                for (int x = 0; x < outw; x += 100) {
                    float[] st = new float[4];
                    float s0 = ((float) x) / ((float) outw);
                    float s1 = ((float) (x + 1)) / ((float) outw);
                    Log.d("foo", String.format("x:%d y:%d", new Object[]{Integer.valueOf(x), Integer.valueOf(y)}));
                    Matrix.multiplyMV(st, 0, this.mat, 0, new float[]{s0, t0, 0.0f, 1.0f}, 0);
                    texture2DEWA(st[0] * this.scale[0], st[1] * this.scale[1]);
                }
            }
        }
    }
    static String str2 = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 inputTextureMatrix;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (inputTextureMatrix * inputTextureCoordinate).xy;\n}";

    public Downscaler(boolean useSimple) {
        super(str2, useSimple?SIMPLE_FRAGMENT_SHADER:FRAGMENT_SHADER);
        this.mGLCubeBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mUseSimple = useSimple;
        this.mRenderer = GLES20.glGetString(7937);
        if (this.mRenderer.equals("Mali-T420")) {
            this.mApplyCroppingFix = true;
        }
        if (this.mRenderer.equals("Mali-T720")) {
            this.mApplyCroppingFix = true;
        }
    }

    public void blit(boolean opaque, int outw, int outh, float w, float h, float[] mat, int textureId) {
        if (this.mUseSimple) {
            simpleBlit(opaque, mat, textureId);
        } else {
            fullBlit(opaque, outw, outh, w, h, mat, textureId);
        }
    }

    public void simpleBlit(boolean opaque, float[] mat, int textureId) {
        setInteger(getUniformLocation("opaque"), opaque ? 1 : 0);
        setUniformMatrix4f(getUniformLocation("inputTextureMatrix"), mat);
        drawQuad(textureId);
    }

    public void fullBlit(boolean opaque, int outw, int outh, float w, float h, float[] mat, int textureId) {
        float[] scale = new float[]{w, h};
        float[] st0 = new float[4];
        float[] st1 = new float[4];
        Matrix.multiplyMV(st0, 0, mat, 0, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        Matrix.multiplyMV(st1, 0, mat, 0, new float[]{1.0f, 1.0f, 0.0f, 1.0f}, 0);
        float min_s = Math.min(st0[0], st1[0]);
        float min_t = Math.min(st0[1], st1[1]);
        float max_s = Math.max(st0[0], st1[0]);
        float max_t = Math.max(st0[1], st1[1]);
        if (this.mApplyCroppingFix) {
            max_s = ((max_s * w) - 3.5f) / w;
            max_t = ((max_t * h) - 3.5f) / h;
        }
        float cropw = (max_s - min_s) * w;
        float croph = (max_t - min_t) * h;
        float[] dDx = new float[4];
        float[] dDy = new float[4];
        Matrix.multiplyMV(dDx, 0, mat, 0, new float[]{1.0f / ((float) outw), 0.0f, 0.0f, 1.0f}, 0);
        Matrix.multiplyMV(dDy, 0, mat, 0, new float[]{0.0f, 1.0f / ((float) outh), 0.0f, 1.0f}, 0);
        dDx[0] = dDx[0] - st0[0];
        dDx[1] = dDx[1] - st0[1];
        dDy[0] = dDy[0] - st0[0];
        dDy[1] = dDy[1] - st0[1];
        dDx[0] = dDx[0] * cropw;
        dDx[1] = dDx[1] * cropw;
        dDy[0] = dDy[0] * croph;
        dDy[1] = dDy[1] * croph;
        setFloatVec2(getUniformLocation("scale"), scale);
        setInteger(getUniformLocation("opaque"), opaque ? 1 : 0);
        setFloatVec2(getUniformLocation("dDx"), new float[]{dDx[0], dDx[1]});
        setFloatVec2(getUniformLocation("dDy"), new float[]{dDy[0], dDy[1]});
        setFloatVec2(getUniformLocation("clamp_min"), new float[]{min_s, min_t});
        setFloatVec2(getUniformLocation("clamp_max"), new float[]{max_s, max_t});
        setUniformMatrix4f(getUniformLocation("inputTextureMatrix"), mat);
        drawQuad(textureId);
    }

    void drawQuad(int textureId) {
        float[] cubeCoords = new float[]{-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
        float[] texCoords = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mGLCubeBuffer.clear();
        this.mGLCubeBuffer.put(cubeCoords).position(0);
        this.mGLTextureBuffer.clear();
        this.mGLTextureBuffer.put(texCoords).position(0);
        onDraw(textureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
    }
}