package cn.nineton.onetake.media.gpuimage;

public class GPUImageMixBlendFilter extends GPUImageTwoInputFilter {
    private float mMix;
    private int mMixLocation;

    public GPUImageMixBlendFilter(String fragmentShader) {
        this(fragmentShader, 0.5f);
    }

    public GPUImageMixBlendFilter(String fragmentShader, float mix) {
        super(fragmentShader);
        this.mMixLocation = getUniformLocation("mixturePercent");
        this.mMix = mix;
    }

    public void onInit() {
        super.onInit();
    }

    public void onInitialized() {
        super.onInitialized();
        setMix(this.mMix);
    }

    public void setMix(float mix) {
        this.mMix = mix;
        setFloat(this.mMixLocation, this.mMix);
    }
}