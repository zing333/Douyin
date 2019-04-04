package cn.nineton.onetake.media.gpuimage;

import java.util.Random;

public class AFGPUImageColorUnstableFilter extends GPUImageFilter {
    public static final String COLOR_UNSTABLE_FRAGMENT_SHADER = "precision highp float;\n \n varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n \n uniform float colorUnstable;\n \n void main()\n {\n     vec4 color = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = vec4(color.rgb*colorUnstable, 1.0);\n }";
    private float colorUnstable = 1.0f;
    private int colorUnstableLocation = getUniformLocation("colorUnstable");

    public AFGPUImageColorUnstableFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_UNSTABLE_FRAGMENT_SHADER);
    }

    public void onInit() {
        super.onInit();
        setColorUnstable(this.colorUnstable);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
    }

    public void setColorUnstable(float colorUnstable) {
        this.colorUnstable = colorUnstable;
        setFloat(this.colorUnstableLocation, colorUnstable);
    }

    public void onDrawAtOneFrame(int frameIndex) {
        super.onDrawAtOneFrame(frameIndex);
        dim();
    }

    private void dim() {
        setFloat(this.colorUnstableLocation, (new Random().nextFloat() * 0.060000002f) + 0.94f);
    }
}