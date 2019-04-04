package cn.nineton.onetake.media.gpuimage;


public class AFGPUImageMultiplyBlendFilter extends GPUImageTwoInputFilter {
    public static final String MULTIPLY_BLEND_MOVIE_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n uniform lowp float overPercent;\n \n void main()\n {\n     lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n     lowp vec4 overlayer = texture2D(inputImageTexture2, textureCoordinate2) * overPercent;\n          \n     gl_FragColor = overlayer * base + overlayer * (1.0 - base.a) + base * (1.0 - overlayer.a);\n }";
    private float mOverPercent;
    private int mOverPercentLocation;

    public AFGPUImageMultiplyBlendFilter() {
        this(0.0f);
    }

    public AFGPUImageMultiplyBlendFilter(float overPercent) {
        super(MULTIPLY_BLEND_MOVIE_SHADER);
        this.mOverPercentLocation = getUniformLocation("overPercent");
        this.mOverPercent = overPercent;
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
}