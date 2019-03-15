package cn.nineton.onetake.media.gpuimage;

public class GPUImageCopyFilter extends GPUImageFilter {
    public static final String COPY_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform highp float inputAlpha;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     textureColor.a *= inputAlpha;\n     gl_FragColor = textureColor;\n }";
    private float mAlpha = 1.0f;
    private int mAlphaLocation = getUniformLocation("inputAlpha");

    public GPUImageCopyFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COPY_FRAGMENT_SHADER);
    }

    public void onInitialized() {
        super.onInitialized();
        setAlpha(this.mAlpha);
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
        setFloat(this.mAlphaLocation, this.mAlpha);
    }
}