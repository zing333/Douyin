package cn.nineton.onetake.media.gpuimage;

public class GPUImageBilateralFilter0 extends GPUImageFilter {
    public static final String BILATERAL_FRAGMENT_SHADER = "uniform sampler2D inputImageTexture;\n const lowp int GAUSSIAN_SAMPLES = 9;\n varying highp vec2 textureCoordinate;\n varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n uniform mediump float distanceNormalizationFactor;\n uniform mediump float blurWeight;\n void main()\n {\n     lowp vec4 centralColor;\n     lowp float gaussianWeightTotal;\n     lowp vec4 sum;\n     lowp vec4 sampleColor;\n     lowp float distanceFromCentralColor;\n     lowp float gaussianWeight;\n     \nlowp vec2 basicCoor = blurCoordinates[4];     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n     gaussianWeightTotal = 0.36;\n     sum = centralColor * 0.36;\n     \nlowp vec2 coor = blurCoordinates[0];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[1];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[2];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[3];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[5];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[6];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[7];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[8];     sampleColor = texture2D(inputImageTexture, vec2(coor.x,basicCoor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[0];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[1];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[2];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[3];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[5];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[6];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[7];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\ncoor = blurCoordinates[8];     sampleColor = texture2D(inputImageTexture, vec2(basicCoor.x,coor.y));\n     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor) * blurWeight;\n     gaussianWeightTotal += gaussianWeight;\n     sum += sampleColor * gaussianWeight;\n     gl_FragColor = sum / gaussianWeightTotal;\n }";
    public static final String BILATERAL_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nconst int GAUSSIAN_SAMPLES = 9;\nuniform vec2 singleStepOffset;\nvarying vec2 textureCoordinate;\nvarying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\nvoid main()\n{\n\tgl_Position = position;\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\tint multiplier = 0;\n\tvec2 blurStep;\n\tfor (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n\t{\n\t\tmultiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n\t\tblurStep = float(multiplier) * singleStepOffset;\n\t\tblurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n\t}\n}";
    private int mBlurWeightLocation;
    private float mBlurWeightLocationValue;
    private int mDisFactorLocation;
    private float mDistanceNormalizationFactor;
    private int mSingleStepOffsetLocation;

    public GPUImageBilateralFilter0() {
        this(8.0f);
    }

    public GPUImageBilateralFilter0(float distanceNormalizationFactor) {
        super(BILATERAL_VERTEX_SHADER, BILATERAL_FRAGMENT_SHADER);
        this.mDisFactorLocation = getUniformLocation("distanceNormalizationFactor");
        this.mSingleStepOffsetLocation = getUniformLocation("singleStepOffset");
        this.mBlurWeightLocation = getUniformLocation("blurWeight");
        this.mDistanceNormalizationFactor = distanceNormalizationFactor;
        this.mBlurWeightLocationValue = 0.5f;
    }

    public void onInit() {
        super.onInit();
    }

    public void onInitialized() {
        super.onInitialized();
        setDistanceNormalizationFactor(this.mDistanceNormalizationFactor);
        setBlurWeightLocation(this.mBlurWeightLocationValue);
    }

    public void setDistanceNormalizationFactor(float newValue) {
        this.mDistanceNormalizationFactor = newValue;
        setFloat(this.mDisFactorLocation, newValue);
    }

    public void setBlurWeightLocation(float newValue) {
        this.mBlurWeightLocationValue = newValue;
        setFloat(this.mBlurWeightLocation, newValue);
    }

    private void setTexelSize(float w, float h) {
        setFloatVec2(this.mSingleStepOffsetLocation, new float[]{4.0f / w, 4.0f / h});
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setTexelSize((float) width, (float) height);
    }
}