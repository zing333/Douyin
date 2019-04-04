package cn.nineton.onetake.media.gpuimage;

import cn.nineton.onetake.util.LogUtil;

public class GPUImageMirrorFilter extends GPUImageFilter {
    public static final String MIRROR_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform lowp float mirrorstart;\nuniform lowp float orientation;//方向，上1.0 下2.0 左3.0 右4.0\nhighp vec2 mirrorPoint;\nvoid main()\n{\n   lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n   if(orientation == 1.0){\n        if(textureCoordinate.y > mirrorstart){\n            mirrorPoint = vec2(textureCoordinate.x, 2.0 * mirrorstart - textureCoordinate.y);\n            lowp vec4 texture = texture2D(inputImageTexture, mirrorPoint);\n            gl_FragColor = vec4(texture.r, texture.g, texture.b, texture.a);\n        }else{\n            gl_FragColor = vec4(textureColor.r,textureColor.g,textureColor.b,textureColor.a);\n        }\n   }else if(orientation == 3.0){\n        if(textureCoordinate.y < mirrorstart){\n            mirrorPoint = vec2(textureCoordinate.x, 2.0 * mirrorstart - textureCoordinate.y);\n            lowp vec4 texture = texture2D(inputImageTexture, mirrorPoint);\n            gl_FragColor = vec4(texture.r, texture.g, texture.b, texture.a);\n        }else{\n            gl_FragColor = vec4(textureColor.r,textureColor.g,textureColor.b,textureColor.a);\n        }\n   }else if(orientation == 4.0){\n        if(textureCoordinate.x > mirrorstart){\n            mirrorPoint = vec2(2.0 * mirrorstart - textureCoordinate.x, textureCoordinate.y);\n            lowp vec4 texture = texture2D(inputImageTexture, mirrorPoint);\n            gl_FragColor = vec4(texture.r, texture.g, texture.b, texture.a);\n        }else{\n            gl_FragColor = vec4(textureColor.r,textureColor.g,textureColor.b,textureColor.a);\n        }\n   }else if(orientation == 2.0){\n        if(textureCoordinate.x < mirrorstart){\n            mirrorPoint = vec2(2.0 * mirrorstart - textureCoordinate.x, textureCoordinate.y);\n            lowp vec4 texture = texture2D(inputImageTexture, mirrorPoint);\n            gl_FragColor = vec4(texture.r, texture.g, texture.b, texture.a);\n        }else{\n            gl_FragColor = vec4(textureColor.r,textureColor.g,textureColor.b,textureColor.a);\n        }\n   }\n}";
    public static final float ORIENTATION_DOWN = 3.0f;
    public static final float ORIENTATION_LEFT = 4.0f;
    public static final float ORIENTATION_RIGHT = 2.0f;
    public static final float ORIENTATION_UP = 1.0f;
    private int mCaptureOrientation;
    private boolean mIsFrontCamera;
    private float mMirrorStart;
    private int mMirrorStartUniform = getUniformLocation("mirrorstart");
    private float mOrientation;
    private int mOrientationUniform = getUniformLocation("orientation");

    public GPUImageMirrorFilter(int captureOrientation, boolean isFrontCamera) {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", MIRROR_FRAGMENT_SHADER);
        this.mCaptureOrientation = captureOrientation;
        this.mMirrorStart = 1.0f;
        this.mOrientation = 1.0f;
        this.mIsFrontCamera = isFrontCamera;
    }

    public void onInit() {
        super.onInit();
        refresh();
    }

    public void onInitialized() {
        super.onInitialized();
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
    }

    public void setMirrorStartAndOrientation(float mirrorStart, float orientation, int captureOrientation) {
        LogUtil.d("GPUImageMirrorFilter", String.format("mirrorStart : %s , orientation : %s , captureOrientation : %s , mIsFrontCamera : %b ", new Object[]{Float.valueOf(mirrorStart), Float.valueOf(orientation), Integer.valueOf(this.mCaptureOrientation), Boolean.valueOf(this.mIsFrontCamera)}));
        if (this.mIsFrontCamera) {
            if (this.mCaptureOrientation == 270) {
                this.mMirrorStart = mirrorStart;
                this.mOrientation = orientation;
                if (this.mOrientation == 4.0f) {
                    this.mOrientation = 2.0f;
                    this.mMirrorStart = 1.0f - mirrorStart;
                } else if (this.mOrientation == 2.0f) {
                    this.mOrientation = 4.0f;
                    this.mMirrorStart = 1.0f - mirrorStart;
                }
            } else if (this.mCaptureOrientation == 0) {
                this.mMirrorStart = 1.0f - mirrorStart;
                if (1.0f == orientation) {
                    this.mOrientation = 2.0f;
                } else if (3.0f == orientation) {
                    this.mOrientation = 4.0f;
                } else if (4.0f == orientation) {
                    this.mOrientation = 3.0f;
                } else if (2.0f == orientation) {
                    this.mOrientation = 1.0f;
                }
            } else if (this.mCaptureOrientation == 90) {
                this.mMirrorStart = 1.0f - mirrorStart;
                if (1.0f == orientation) {
                    this.mOrientation = 3.0f;
                } else if (3.0f == orientation) {
                    this.mOrientation = 1.0f;
                } else if (4.0f == orientation) {
                    this.mOrientation = 4.0f;
                    this.mMirrorStart = mirrorStart;
                } else if (2.0f == orientation) {
                    this.mOrientation = 2.0f;
                    this.mMirrorStart = mirrorStart;
                }
            } else if (this.mCaptureOrientation == 180) {
                this.mMirrorStart = 1.0f - mirrorStart;
                if (1.0f == orientation) {
                    this.mOrientation = 4.0f;
                    this.mMirrorStart = mirrorStart;
                } else if (3.0f == orientation) {
                    this.mOrientation = 2.0f;
                    this.mMirrorStart = mirrorStart;
                } else if (4.0f == orientation) {
                    this.mOrientation = 1.0f;
                    this.mMirrorStart = mirrorStart;
                } else if (2.0f == orientation) {
                    this.mOrientation = 3.0f;
                    this.mMirrorStart = mirrorStart;
                }
            }
        } else if (this.mCaptureOrientation == 270) {
            this.mMirrorStart = mirrorStart;
            this.mOrientation = orientation;
        } else if (this.mCaptureOrientation == 0) {
            this.mMirrorStart = mirrorStart;
            if (orientation == 4.0f || orientation == 2.0f) {
                this.mMirrorStart = 1.0f - mirrorStart;
            }
            this.mOrientation = orientation - 1.0f;
            if (this.mOrientation < 1.0f) {
                this.mOrientation += 4.0f;
            }
        } else if (this.mCaptureOrientation == 90) {
            this.mMirrorStart = 1.0f - mirrorStart;
            this.mOrientation = orientation - 2.0f;
            if (this.mOrientation < 1.0f) {
                this.mOrientation += 4.0f;
            }
        } else if (this.mCaptureOrientation == 180) {
            this.mMirrorStart = mirrorStart;
            if (orientation == 1.0f || orientation == 3.0f) {
                this.mMirrorStart = 1.0f - mirrorStart;
            }
            this.mOrientation = orientation - 3.0f;
            if (this.mOrientation < 1.0f) {
                this.mOrientation += 4.0f;
            }
        }
        refresh();
    }

    private void refresh() {
        setFloat(this.mMirrorStartUniform, this.mMirrorStart);
        setFloat(this.mOrientationUniform, this.mOrientation);
    }
}