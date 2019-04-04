package cn.nineton.onetake.media.gpuimage;


public class GPUImageAlphaBlendFilter extends GPUImageMixBlendFilter {
    public static final String ALPHA_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n uniform lowp float mixturePercent;\n\n void main()\n {\n   lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n   lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n\n   if (textureColor2.a == 0.0) {\n       gl_FragColor = textureColor;\n   } else {\n       gl_FragColor = vec4(mix(textureColor.rgb, textureColor2.rgb / textureColor2.a,mixturePercent * textureColor2.a), textureColor.a);\n   }\n}";

    public GPUImageAlphaBlendFilter() {
        super(ALPHA_BLEND_FRAGMENT_SHADER);
    }

    public GPUImageAlphaBlendFilter(float mix) {
        super(ALPHA_BLEND_FRAGMENT_SHADER, mix);
    }
}