package cn.nineton.onetake.media.gpuimage;


public class GPUImageGaussianBlurFilter extends GPUImageTwoPassTextureSamplingFilter {
    public static final String FRAGMENT_SHADER = "uniform sampler2D inputImageTexture;\n\nconst lowp int GAUSSIAN_SAMPLES = 9;\n\nvarying highp vec2 textureCoordinate;\nvarying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n\nvoid main()\n{\n\tlowp vec3 sum = vec3(0.0);\n   lowp vec4 fragColor=texture2D(inputImageTexture,textureCoordinate);\n\t\n    sum += texture2D(inputImageTexture, blurCoordinates[0]).rgb * 0.05;\n    sum += texture2D(inputImageTexture, blurCoordinates[1]).rgb * 0.09;\n    sum += texture2D(inputImageTexture, blurCoordinates[2]).rgb * 0.12;\n    sum += texture2D(inputImageTexture, blurCoordinates[3]).rgb * 0.15;\n    sum += texture2D(inputImageTexture, blurCoordinates[4]).rgb * 0.18;\n    sum += texture2D(inputImageTexture, blurCoordinates[5]).rgb * 0.15;\n    sum += texture2D(inputImageTexture, blurCoordinates[6]).rgb * 0.12;\n    sum += texture2D(inputImageTexture, blurCoordinates[7]).rgb * 0.09;\n    sum += texture2D(inputImageTexture, blurCoordinates[8]).rgb * 0.05;\n\n\tgl_FragColor = vec4(sum,fragColor.a);\n}";
    public static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nconst int GAUSSIAN_SAMPLES = 9;\n\nuniform float texelWidthOffset;\nuniform float texelHeightOffset;\n\nvarying vec2 textureCoordinate;\nvarying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n\nvoid main()\n{\n\tgl_Position = position;\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\t\n\t// Calculate the positions for the blur\n\tint multiplier = 0;\n\tvec2 blurStep;\n   vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);\n    \n\tfor (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n   {\n\t\tmultiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n       // Blur in x (horizontal)\n       blurStep = float(multiplier) * singleStepOffset;\n\t\tblurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n\t}\n}\n";
    protected float mBlurSize;

    static String vertexShaderForBlur(float fBlurRadius) {
        int _blurRadiusInPixels = Math.round(fBlurRadius);
        int calculatedSampleRadius = 0;
        if (_blurRadiusInPixels >= 1) {
            calculatedSampleRadius = (int) Math.floor(Math.sqrt((-2.0d * Math.pow((double) _blurRadiusInPixels, 2.0d)) * Math.log(Math.sqrt(6.283185307179586d * Math.pow((double) _blurRadiusInPixels, 2.0d)) * 0.00390625d)));
            calculatedSampleRadius += calculatedSampleRadius % 2;
        }
        return vertexShaderForBlur(calculatedSampleRadius, (double) _blurRadiusInPixels);
    }

    static String fragmentShaderForBlur(float fBlurRadius) {
        int _blurRadiusInPixels = Math.round(fBlurRadius);
        int calculatedSampleRadius = 0;
        if (_blurRadiusInPixels >= 1) {
            calculatedSampleRadius = (int) Math.floor(Math.sqrt((-2.0d * Math.pow((double) _blurRadiusInPixels, 2.0d)) * Math.log(Math.sqrt(6.283185307179586d * Math.pow((double) _blurRadiusInPixels, 2.0d)) * 0.00390625d)));
            calculatedSampleRadius += calculatedSampleRadius % 2;
        }
        return fragmentShaderForBlur(calculatedSampleRadius, (double) _blurRadiusInPixels);
    }

    static String vertexShaderForBlur(int blurRadius, double sigma) {
        int currentGaussianWeightIndex;
        int currentOptimizedOffset;
        double[] standardGaussianWeights = new double[(blurRadius + 1)];
        double sumOfWeights = 0.0d;
        for (currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            double d;
            standardGaussianWeights[currentGaussianWeightIndex] = (1.0d / Math.sqrt(6.283185307179586d * Math.pow(sigma, 2.0d))) * Math.exp((-Math.pow((double) currentGaussianWeightIndex, 2.0d)) / (2.0d * Math.pow(sigma, 2.0d)));
            if (currentGaussianWeightIndex == 0) {
                d = standardGaussianWeights[currentGaussianWeightIndex];
            } else {
                d = 2.0d * standardGaussianWeights[currentGaussianWeightIndex];
            }
            sumOfWeights += d;
        }
        for (currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = standardGaussianWeights[currentGaussianWeightIndex] / sumOfWeights;
        }
        int numberOfOptimizedOffsets = Math.min((blurRadius / 2) + (blurRadius % 2), 7);
        float[] optimizedGaussianOffsets = new float[numberOfOptimizedOffsets];
        for (currentOptimizedOffset = 0; currentOptimizedOffset < numberOfOptimizedOffsets; currentOptimizedOffset++) {
            float firstWeight = (float) standardGaussianWeights[(currentOptimizedOffset * 2) + 1];
            float secondWeight = (float) standardGaussianWeights[(currentOptimizedOffset * 2) + 2];
            optimizedGaussianOffsets[currentOptimizedOffset] = ((((float) ((currentOptimizedOffset * 2) + 1)) * firstWeight) + (((float) ((currentOptimizedOffset * 2) + 2)) * secondWeight)) / (firstWeight + secondWeight);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform float texelWidthOffset;\nuniform float texelHeightOffset;\nvarying vec2 blurCoordinates[%d];\nvoid main()\n{\n   gl_Position = position;\n   vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n", new Object[]{Integer.valueOf((numberOfOptimizedOffsets * 2) + 1)}));
        sb.append("    blurCoordinates[0] = inputTextureCoordinate.xy;\n");
        for (currentOptimizedOffset = 0; currentOptimizedOffset < numberOfOptimizedOffsets; currentOptimizedOffset++) {
            sb.append(String.format("   blurCoordinates[%d] = inputTextureCoordinate.xy + singleStepOffset * %f;\n   blurCoordinates[%d] = inputTextureCoordinate.xy - singleStepOffset * %f;\n", new Object[]{Integer.valueOf((currentOptimizedOffset * 2) + 1), Float.valueOf(optimizedGaussianOffsets[currentOptimizedOffset]), Integer.valueOf((currentOptimizedOffset * 2) + 2), Float.valueOf(optimizedGaussianOffsets[currentOptimizedOffset])}));
        }
        sb.append("}\n");
        return sb.toString();
    }

    static String fragmentShaderForBlur(int blurRadius, double sigma) {
        int currentGaussianWeightIndex;
        float optimizedWeight=0.0f;
        double[] standardGaussianWeights = new double[(blurRadius + 1)];
        double sumOfWeights = 0.0d;
        for (currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            double d;
            standardGaussianWeights[currentGaussianWeightIndex] = (1.0d / Math.sqrt(6.283185307179586d * Math.pow(sigma, 2.0d))) * Math.exp((-Math.pow((double) currentGaussianWeightIndex, 2.0d)) / (2.0d * Math.pow(sigma, 2.0d)));
            if (currentGaussianWeightIndex == 0) {
                d = standardGaussianWeights[currentGaussianWeightIndex];
            } else {
                d = 2.0d * standardGaussianWeights[currentGaussianWeightIndex];
            }
            sumOfWeights += d;
        }
        for (currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = standardGaussianWeights[currentGaussianWeightIndex] / sumOfWeights;
        }
        int numberOfOptimizedOffsets = Math.min((blurRadius / 2) + (blurRadius % 2), 7);
        int trueNumberOfOptimizedOffsets = (blurRadius / 2) + (blurRadius % 2);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("uniform sampler2D inputImageTexture;\nuniform highp float texelWidthOffset;\nuniform highp float texelHeightOffset;\nvarying highp vec2 blurCoordinates[%d];\nvoid main()\n{\nlowp vec4 sum = vec4(0.0);\n", new Object[]{Integer.valueOf((numberOfOptimizedOffsets * 2) + 1)}));
        sb.append(String.format("sum += texture2D(inputImageTexture, blurCoordinates[0]) * %f;\n", new Object[]{Double.valueOf(standardGaussianWeights[0])}));
        for (int currentBlurCoordinateIndex = 0; currentBlurCoordinateIndex < numberOfOptimizedOffsets; currentBlurCoordinateIndex++) {
            optimizedWeight = ((float) standardGaussianWeights[(currentBlurCoordinateIndex * 2) + 1]) + ((float) standardGaussianWeights[(currentBlurCoordinateIndex * 2) + 2]);
            sb.append(String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", new Object[]{Integer.valueOf((currentBlurCoordinateIndex * 2) + 1), Float.valueOf(optimizedWeight)}));
            sb.append(String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", new Object[]{Integer.valueOf((currentBlurCoordinateIndex * 2) + 2), Float.valueOf(optimizedWeight)}));
        }
        if (trueNumberOfOptimizedOffsets > numberOfOptimizedOffsets) {
            sb.append("highp vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n");
            for (int currentOverlowTextureRead = numberOfOptimizedOffsets; currentOverlowTextureRead < trueNumberOfOptimizedOffsets; currentOverlowTextureRead++) {
                float firstWeight = (float) standardGaussianWeights[(currentOverlowTextureRead * 2) + 1];
                float secondWeight = (float) standardGaussianWeights[(currentOverlowTextureRead * 2) + 2];
                float optimizedOffset = ((((float) ((currentOverlowTextureRead * 2) + 1)) * firstWeight) + (((float) ((currentOverlowTextureRead * 2) + 2)) * secondWeight)) / (firstWeight + secondWeight);
                sb.append(String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] + singleStepOffset * %f) * %f;\n", new Object[]{Float.valueOf(optimizedOffset), Float.valueOf(firstWeight + secondWeight)}));
                sb.append(String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] - singleStepOffset * %f) * %f;\n", new Object[]{Float.valueOf(optimizedOffset), Float.valueOf(optimizedWeight)}));
            }
        }
        sb.append("gl_FragColor = sum;\n}\n");
        return sb.toString();
    }

    public GPUImageGaussianBlurFilter() {
        this(2.0f);
    }

    public GPUImageGaussianBlurFilter(float blurSize) {
        this(blurSize, vertexShaderForBlur(blurSize), fragmentShaderForBlur(blurSize));
    }

    public GPUImageGaussianBlurFilter(float blurSize, String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader, vertexShader, fragmentShader);
        this.mBlurSize = 0.0f;
        this.mBlurSize = blurSize;
    }

    public void setBlurSize(final float blurSize) {
        this.mBlurSize = blurSize;
        runOnDraw(new Runnable() {
            public void run() {
                for (GPUImageFilter filter : GPUImageGaussianBlurFilter.this.mFilters) {
                    filter.destroy();
                }
                String vertexShader = GPUImageGaussianBlurFilter.vertexShaderForBlur(blurSize);
                String fragmentShader = GPUImageGaussianBlurFilter.fragmentShaderForBlur(blurSize);
                GPUImageGaussianBlurFilter.this.mFilters.clear();
                GPUImageGaussianBlurFilter.this.addFilter(new GPUImageFilter(vertexShader, fragmentShader));
                GPUImageGaussianBlurFilter.this.addFilter(new GPUImageFilter(vertexShader, fragmentShader));
                GPUImageGaussianBlurFilter.this.updateMergedFilters();
                for (GPUImageFilter filter2 : GPUImageGaussianBlurFilter.this.mFilters) {
                    if (GPUImageGaussianBlurFilter.this.isInitialized()) {
                        filter2.init();
                    }
                    if (!(GPUImageGaussianBlurFilter.this.mOutputWidth == 0 || GPUImageGaussianBlurFilter.this.mOutputHeight == 0)) {
                        filter2.onOutputSizeChanged(GPUImageGaussianBlurFilter.this.mOutputWidth, GPUImageGaussianBlurFilter.this.mOutputHeight);
                    }
                }
                GPUImageGaussianBlurFilter.this.initTexelOffsets();
            }
        });
    }
}