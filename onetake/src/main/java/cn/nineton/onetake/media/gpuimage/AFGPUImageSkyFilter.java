package cn.nineton.onetake.media.gpuimage;


public class AFGPUImageSkyFilter extends GPUImageTwoInputFilter {
    public static final String SKY_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate; varying highp vec2 textureCoordinate2; uniform sampler2D inputImageTexture; uniform sampler2D inputImageTexture2; uniform highp float overPercent; void main()   {       lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);       lowp vec4 overlayer = texture2D(inputImageTexture2, textureCoordinate2) * overPercent;       gl_FragColor = overlayer * base + overlayer * (1.0 - base.a) + base * (1.0 - overlayer.a);   }";
    private float mOverPercent;
    private int mOverPercentLocation;

    public AFGPUImageSkyFilter() {
        this(0.0f);
    }

    public AFGPUImageSkyFilter(float mOverPercent) {
        super(SKY_FRAGMENT_SHADER);
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
}