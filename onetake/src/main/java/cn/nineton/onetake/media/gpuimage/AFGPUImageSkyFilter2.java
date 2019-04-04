package cn.nineton.onetake.media.gpuimage;

import android.graphics.Bitmap;
import java.io.IOException;

import cn.nineton.onetake.App;

public class AFGPUImageSkyFilter2 extends GPUImageFilterGroup {
    private GPUImageLookupFilter2 lookupFilter = new GPUImageLookupFilter2();
    private AFGPUImageSkyFilter skyFilter1 = new AFGPUImageSkyFilter();

    public AFGPUImageSkyFilter2() {
        addFilter(this.skyFilter1);
        try {
            this.lookupFilter.setData(App.getContext().getAssets().open("dat/Sky.dat"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addFilter(this.lookupFilter);
    }

    public void setBitmap(Bitmap bitmap) {
        this.skyFilter1.setBitmap(bitmap);
    }

    public void setOverPercent(float value) {
        this.skyFilter1.setOverPercent(value);
        this.lookupFilter.setIntensity(value);
    }
}