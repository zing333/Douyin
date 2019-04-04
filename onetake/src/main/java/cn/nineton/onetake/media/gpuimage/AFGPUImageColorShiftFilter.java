package cn.nineton.onetake.media.gpuimage;

import android.graphics.Point;

public class AFGPUImageColorShiftFilter extends GPUImageFilter {
    public static final String COLOR_SHIFT_FRAGMENT_SHADER = " precision highp float;\n \n varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n \n uniform float imageWidthFactor;\n uniform float imageHeightFactor;\n \n uniform float redHorizontalColorShift;\n uniform float redVerticalColorShift;\n uniform float greenHorizontalColorShift;\n uniform float greenVerticalColorShift;\n uniform float blueHorizontalColorShift;\n uniform float blueVerticalColorShift;\n uniform float alphaHorizontalColorShift;\n uniform float alphaVerticalColorShift;\n void main()\n {\n     highp vec2 redPoint = vec2(textureCoordinate.x + imageWidthFactor * redHorizontalColorShift, textureCoordinate.y - imageHeightFactor * redVerticalColorShift);\n     highp float redColor = texture2D(inputImageTexture, redPoint).r;\n     \n     highp vec2 greenPoint = vec2(textureCoordinate.x + imageWidthFactor * greenHorizontalColorShift, textureCoordinate.y - imageHeightFactor * greenVerticalColorShift);\n     highp float greenColor = texture2D(inputImageTexture, greenPoint).g;\n     \n     highp vec2 bluePoint = vec2(textureCoordinate.x + imageWidthFactor * blueHorizontalColorShift, textureCoordinate.y - imageHeightFactor * blueVerticalColorShift);\n     highp float blueColor = texture2D(inputImageTexture, bluePoint).b;\n     \n     highp vec2 alphaPoint = vec2(textureCoordinate.x + imageWidthFactor * alphaHorizontalColorShift, textureCoordinate.y - imageHeightFactor * alphaVerticalColorShift);\n     highp float alpha = texture2D(inputImageTexture, alphaPoint).a;\n    \n     gl_FragColor = vec4(redColor, greenColor, blueColor, alpha);\n }";
    private int alphaHorizontalColorShiftUniform = getUniformLocation("alphaHorizontalColorShift");
    private int alphaVerticalColorShiftUniform = getUniformLocation("alphaVerticalColorShift");
    private int blueHorizontalColorShiftUniform = getUniformLocation("blueHorizontalColorShift");
    private int blueVerticalColorShiftUniform = getUniformLocation("blueVerticalColorShift");
    private int greenHorizontalColorShiftUniform = getUniformLocation("greenHorizontalColorShift");
    private int greenVerticalColorShiftUniform = getUniformLocation("greenVerticalColorShift");
    private Point mAlphacolorShift = new Point(0, 0);
    private Point mBluecolorShift = new Point(0, 0);
    private Point mGreencolorShift = new Point(0, 0);
    private int mImageHeightFactorLocation = getUniformLocation("imageHeightFactor");
    private int mImageWidthFactorLocation = getUniformLocation("imageWidthFactor");
    private Point mRedcolorShift = new Point(0, 0);
    private int redHorizontalColorShiftUniform = getUniformLocation("redHorizontalColorShift");
    private int redVerticalColorShiftUniform = getUniformLocation("redVerticalColorShift");

    public AFGPUImageColorShiftFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_SHIFT_FRAGMENT_SHADER);
    }

    public void onInit() {
        super.onInit();
        setRedColorShift(this.mRedcolorShift);
        setGreenColorShift(this.mGreencolorShift);
        setBlueColorShift(this.mBluecolorShift);
        setAlphaColorShift(this.mAlphacolorShift);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(this.mImageWidthFactorLocation, 1.0f / ((float) width));
        setFloat(this.mImageHeightFactorLocation, 1.0f / ((float) height));
    }

    public void setRedColorShift(Point mRedcolorShift) {
        this.mRedcolorShift = mRedcolorShift;
        setFloat(this.redHorizontalColorShiftUniform, (float) mRedcolorShift.y);
        setFloat(this.redVerticalColorShiftUniform, (float) mRedcolorShift.x);
    }

    public void setGreenColorShift(Point mGreencolorShift) {
        this.mGreencolorShift = mGreencolorShift;
        setFloat(this.greenHorizontalColorShiftUniform, (float) mGreencolorShift.y);
        setFloat(this.greenVerticalColorShiftUniform, (float) mGreencolorShift.x);
    }

    public void setBlueColorShift(Point mBluecolorShift) {
        this.mBluecolorShift = mBluecolorShift;
        setFloat(this.blueHorizontalColorShiftUniform, (float) mBluecolorShift.y);
        setFloat(this.blueVerticalColorShiftUniform, (float) mBluecolorShift.x);
    }

    public void setAlphaColorShift(Point mAlphacolorShift) {
        this.mAlphacolorShift = mAlphacolorShift;
        setFloat(this.alphaHorizontalColorShiftUniform, (float) mAlphacolorShift.y);
        setFloat(this.alphaVerticalColorShiftUniform, (float) mAlphacolorShift.x);
    }
}