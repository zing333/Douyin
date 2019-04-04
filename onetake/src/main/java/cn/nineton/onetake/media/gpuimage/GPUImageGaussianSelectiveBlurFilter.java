package cn.nineton.onetake.media.gpuimage;


public class GPUImageGaussianSelectiveBlurFilter extends GPUImageTwoInputFilter {
    public static final String FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nvarying highp vec2 textureCoordinate2;\n\nuniform sampler2D inputImageTexture;\nuniform sampler2D inputImageTexture2; \n\nuniform lowp float excludeCircleRadius;\nuniform lowp vec2 excludeCirclePoint;\nuniform lowp float excludeBlurSize;\nuniform highp float aspectRatio;\n\nvoid main()\n{\n    lowp vec4 sharpImageColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 blurredImageColor = texture2D(inputImageTexture2, textureCoordinate2);\n    \n    highp vec2 textureCoordinateToUse = vec2(textureCoordinate2.x, (textureCoordinate2.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n    highp float distanceFromCenter = distance(excludeCirclePoint, textureCoordinateToUse);\n    \nhighp float foo=smoothstep(excludeCircleRadius - excludeBlurSize, excludeCircleRadius, distanceFromCenter);    gl_FragColor = mix(sharpImageColor, blurredImageColor, foo);\n}\n";
    protected float mAspectRatio = 1.0f;
    protected float mExcludeBlurSize = 0.3f;
    protected float[] mExcludeCirclePoint = new float[]{0.5f, 0.5f};
    protected float mExcludeCircleRadius = 0.5f;
    protected GPUImageGaussianBlurFilter mGaussianBlurFilter = new GPUImageGaussianBlurFilter(1.0f);
    protected int mUniformAspectRatio = getUniformLocation("aspectRatio");
    protected int mUniformExcludeBlurSize = getUniformLocation("excludeBlurSize");
    protected int mUniformExcludeCirclePoint = getUniformLocation("excludeCirclePoint");
    protected int mUniformExcludeCircleRadius = getUniformLocation("excludeCircleRadius");

    public GPUImageGaussianSelectiveBlurFilter() {
        super(FRAGMENT_SHADER);
        setSecondFilter(this.mGaussianBlurFilter);
    }

    public void onInit() {
        super.onInit();
        setExcludeCircleRadius(this.mExcludeCircleRadius);
        setExcludeCirclePoint(this.mExcludeCirclePoint[0], this.mExcludeCirclePoint[1]);
        setExcludeBlurSize(this.mExcludeBlurSize);
        setAspectRatio(this.mAspectRatio);
    }

    public void setExcludeCircleRadius(float value) {
        this.mExcludeCircleRadius = value;
        setFloat(this.mUniformExcludeCircleRadius, value);
    }

    public void setExcludeCirclePoint(float x, float y) {
        this.mExcludeCirclePoint = new float[]{x, y};
        setFloatVec2(this.mUniformExcludeCirclePoint, this.mExcludeCirclePoint);
    }

    public void setExcludeBlurSize(float value) {
        this.mExcludeBlurSize = value;
        setFloat(this.mUniformExcludeBlurSize, value);
    }

    public void setAspectRatio(float value) {
        this.mAspectRatio = value;
        setFloat(this.mUniformAspectRatio, value);
    }

    public void setBlurSize(float blurSize) {
        this.mGaussianBlurFilter.setBlurSize(blurSize);
    }
}