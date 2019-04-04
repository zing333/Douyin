package cn.nineton.onetake.media.gpuimage;


public class AFGPUImageOverlayBlendFilter extends GPUImageTwoInputFilter {
    public static final String OVERLAY_BALEND_GRAIN_GRAMENT_SHADER = "precision highp float;\n varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n uniform float overPercent;\n void main()\n {\n   mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n   mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2) * overPercent;\n   mediump float ra;\n   if (2.0 * base.r < base.a) {\n       ra = 2.0 * overlay.r * base.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n   } else {\n       ra = overlay.a * base.a - 2.0 * (base.a - base.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n   }\n   \n   mediump float ga;\n   \n   if (2.0 * base.g < base.a) {\n       ga = 2.0 * overlay.g * base.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n   } else {\n       ga = overlay.a * base.a - 2.0 * (base.a - base.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n   }\n   \n   mediump float ba;\n   if (2.0 * base.b < base.a) {\n       ba = 2.0 * overlay.b * base.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n   } else {\n       ba = overlay.a * base.a - 2.0 * (base.a - base.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n   }\n   \n   gl_FragColor = vec4(ra, ga, ba, 1.0);\n}";
    private float mOverPercent;
    private int mOverPercentLocation;

    public AFGPUImageOverlayBlendFilter() {
        this(1.0f);
    }

    public AFGPUImageOverlayBlendFilter(float mOverPercent) {
        super(OVERLAY_BALEND_GRAIN_GRAMENT_SHADER);
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