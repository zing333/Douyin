package cn.nineton.onetake.media.gpuimage;

import cn.nineton.onetake.R;
import cn.nineton.onetake.media.Shaders;

public class GPUImagePrismFilter extends GPUImageFilter {
    float mRadius = 0.075f;
    int mRadiusLocation = getUniformLocation("prismR");
    int mRefractionLocation = getUniformLocation("refraction");
    float mRefractionScale;
    float mStrength = 0.0f;
    int mStrengthLocation = getUniformLocation("strength");

    public GPUImagePrismFilter() {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", Shaders.readRawTextFile(R.raw.prism4));
    }

    public void onInit() {
        super.onInit();
        setRadius(this.mRadius);
        setStrength(this.mStrength);
    }

    public void setRadius(float r) {
        float refraction = (float) ((0.08d * Math.exp(((double) r) * 1.466d)) + (2.3990000000000002E-8d * Math.exp(19.1d * ((double) r))));
        setFloat(this.mRadiusLocation, r);
        setFloat(this.mRefractionLocation, refraction);
        this.mRadius = r;
        this.mRefractionScale = refraction;
    }

    public void setStrength(float strength) {
        if (strength < 0.0f) {
            strength = 0.0f;
        }
        setFloat(this.mStrengthLocation, 0.12f * strength);
        this.mStrength = strength;
    }

    public void onOutputSizeChanged(int w, int h) {
        super.onOutputSizeChanged(w, h);
    }
}