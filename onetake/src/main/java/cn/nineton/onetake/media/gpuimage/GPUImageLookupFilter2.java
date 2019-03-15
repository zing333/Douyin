package cn.nineton.onetake.media.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import cn.nineton.onetake.util.TexDecrypt;
//import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
//import jp.co.cyberagent.android.gpuimage.ResourceTracker;

public class GPUImageLookupFilter2 extends GPUImageTwoInputFilter {
    public static final String LOOKUP_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2; // TODO: This is not used\n \n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2; // lookup texture\n \n uniform lowp float intensity;\n uniform lowp float gridsize;\n uniform lowp float texwidth;\n uniform lowp mat4 colorMatrix;\n uniform lowp float isBnW;\n \n void main()\n {\n    // get texel (bilinear sampled) from source image\n    highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    \n    //textureColor = vec4(94.0/255.0, 86.0/255.0, 76.0/255.0, 1.0);   //127,119,90\n    // slice 0   maps to value blue=0\n    // slice 31  maps to value blue=1\n    highp float slice = textureColor.b * (gridsize - 1.0);\n\n    highp float islice0 = floor(slice);\n    highp float islice1 = min(31.0, islice0 + 1.0);\n    highp float fslice = fract(slice);\n    \n    // we need to use the range 0.5 - 31.5 within the slice in order to avoid\n    // sampling pixels of the slice next to it.\n    // multiply by 31 and add 0.5\n    highp float x = textureColor.r * (gridsize - 1.0);\n    highp float x1 = x + islice0*gridsize + 0.5;\n    highp float x2 = x + islice1*gridsize + 0.5;\n    \n    highp float y = textureColor.g * (gridsize - 1.0) + 0.5;\n    \n    highp vec2 texPos1 = vec2(x1 / texwidth, y/32.0);\n    highp vec2 texPos2 = vec2(x2 / texwidth, y/32.0);\n    \n    highp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n    highp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n    if (isBnW == 1.0){\n       textureColor = textureColor * colorMatrix;\n    }\n    highp vec4 newColor = mix(newColor1, newColor2, fslice);\n    \n    gl_FragColor.rgb = mix(textureColor.rgb, newColor.rgb, intensity);\n    gl_FragColor.a = textureColor.a;\n }";
    private static final String TAG = "GPUImageLookupFilter2";
    private int gridsize;
    private int mColorMatrixLocation;
    private ByteBuffer mData;
    private float mIntensity = 1.0f;
    private int mIntensityLocation;
    private int mIsBnWLocation;
    private int texwidth;

    public GPUImageLookupFilter2() {
        super(LOOKUP_FRAGMENT_SHADER);
        initUniforms();
    }

    public GPUImageLookupFilter2(Bitmap bitmap) {
        super(LOOKUP_FRAGMENT_SHADER);
        initUniforms();
        setBitmap(bitmap);
    }

    public GPUImageLookupFilter2(InputStream input) {
        super(LOOKUP_FRAGMENT_SHADER);
        initUniforms();
        try {
            setData(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUniforms() {
        this.mIntensityLocation = getUniformLocation("intensity");
        this.mIsBnWLocation = getUniformLocation("isBnW");
        this.mColorMatrixLocation = getUniformLocation("colorMatrix");
    }

    public void onInit() {
        super.onInit();
        onDataChanged();
    }

    public void setIsBnW(float mIsBnW) {
        this.isBnW = mIsBnW;
        Log.d(TAG, String.format("isBnW == %s", new Object[]{Float.valueOf(this.isBnW)}));
        setFloat(this.mIsBnWLocation, this.isBnW);
    }

    private void setColorMatrix() {
        setUniformMatrix4f(this.mColorMatrixLocation, new float[]{0.24f, 0.68f, 0.08f, 0.0f, 0.24f, 0.68f, 0.08f, 0.0f, 0.24f, 0.68f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
    }

    public void onInitialized() {
        super.onInitialized();
        setIntensity(this.mIntensity);
        Log.d(TAG, String.format("onInitialized : %s", new Object[]{Float.valueOf(this.isBnW)}));
        setIsBnW(this.isBnW);
        setColorMatrix();
    }

    public void setIntensity(float intensity) {
        this.mIntensity = intensity;
        setFloat(this.mIntensityLocation, this.mIntensity);
    }

    void setData(InputStream stream) throws IOException {
        int length = stream.available();
        byte[] ciphertext = new byte[length];
        new DataInputStream(stream).readFully(ciphertext);
        this.mData = ByteBuffer.allocateDirect(length);
        this.mData.put(ciphertext);
        onDataChanged();
    }

    void onDataChanged() {
        runOnDraw(new Runnable() {
            public void run() {
                if (GPUImageLookupFilter2.this.mFilterSourceTexture2 == -1) {
                    int texid = TexDecrypt.createTexture(GPUImageLookupFilter2.this.mData);
                    GLES20.glActiveTexture(33987);
                    ResourceTracker.markTexture(texid, "lut", 1024, 32, 4);
                    GPUImageLookupFilter2.this.setTexture(texid);
                }
                GPUImageLookupFilter2.this.texwidth = 1024;
                GPUImageLookupFilter2.this.gridsize = 32;
                GPUImageLookupFilter2.this.setFloat(GPUImageLookupFilter2.this.getUniformLocation("gridsize"), (float) GPUImageLookupFilter2.this.gridsize);
                GPUImageLookupFilter2.this.setFloat(GPUImageLookupFilter2.this.getUniformLocation("texwidth"), (float) GPUImageLookupFilter2.this.texwidth);
            }
        });
    }

    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w % h != 0) {
            throw new RuntimeException("LUT size is incorrect.");
        }
        this.texwidth = w;
        this.gridsize = h;
        setFloat(getUniformLocation("gridsize"), (float) this.gridsize);
        setFloat(getUniformLocation("texwidth"), (float) this.texwidth);
    }
}