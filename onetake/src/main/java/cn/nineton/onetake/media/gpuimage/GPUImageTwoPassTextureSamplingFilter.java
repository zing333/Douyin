package cn.nineton.onetake.media.gpuimage;

public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
    public GPUImageTwoPassTextureSamplingFilter(String firstVertexShader, String firstFragmentShader, String secondVertexShader, String secondFragmentShader) {
        super(firstVertexShader, firstFragmentShader, secondVertexShader, secondFragmentShader);
    }

    public void onInit() {
        super.onInit();
        initTexelOffsets();
    }

    protected void initTexelOffsets() {
        float ratio = getHorizontalTexelOffsetRatio();
        GPUImageFilter filter = (GPUImageFilter) this.mFilters.get(0);
        int texelWidthOffsetLocation = filter.getUniformLocation("texelWidthOffset");
        int texelHeightOffsetLocation = filter.getUniformLocation("texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, ratio / 1000.0f);
        filter.setFloat(texelHeightOffsetLocation, 0.0f);
        ratio = getVerticalTexelOffsetRatio();
        filter = (GPUImageFilter) this.mFilters.get(1);
        texelWidthOffsetLocation = filter.getUniformLocation("texelWidthOffset");
        texelHeightOffsetLocation = filter.getUniformLocation("texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, 0.0f);
        filter.setFloat(texelHeightOffsetLocation, ratio / ((((float) getOutputHeight()) * 1000.0f) / ((float) getOutputWidth())));
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        initTexelOffsets();
    }

    public float getVerticalTexelOffsetRatio() {
        return 1.0f;
    }

    public float getHorizontalTexelOffsetRatio() {
        return 1.0f;
    }
}