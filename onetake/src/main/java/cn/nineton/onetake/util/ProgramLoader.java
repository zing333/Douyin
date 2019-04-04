package cn.nineton.onetake.util;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.gpuimage.AFGPUImageColorShiftFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageColorUnstableFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageOverlayBlendFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageScreenBlendFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageSkyFilter;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.GPUImageBilateralFilter0;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageMultiplyBlendFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageOESFilter;
import cn.nineton.onetake.media.gpuimage.GPUImagePrismFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageSaturationFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageToneCurveFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageWhiteBalanceFilter;
import cn.nineton.onetake.media.videotool.AFGPUImageDateBlendFilter;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;
//import jp.co.cyberagent.android.gpuimage.AFGPUImageColorShiftFilter;
//import jp.co.cyberagent.android.gpuimage.AFGPUImageColorUnstableFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter0;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageOESFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageMultiplyBlendFilter;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;

public class ProgramLoader {
    private static final String TAG = "ProgramLoader";
    static ProgramLoader instance = null;
    Thread thread = new Thread(new Runnable() {
        public void run() {
            ProgramLoader.this.entry();
        }
    });

    private ProgramLoader() {
        instance = this;
        this.thread.setName("Program Preloader");
        this.thread.start();
    }

    public static synchronized void create() {
        synchronized (ProgramLoader.class) {
            if (instance == null) {
                instance = new ProgramLoader();
            }
        }
    }

    public static void suspend() {
    }

    public static void resume() {
    }

    void entry() {
        //EGL10Helper.withContext("loadPrograms", ProgramLoader$$Lambda$1.lambdaFactory$(this));
        EGL10Helper.withContext("loadPrograms", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$entry$0(eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$entry$0(EGL10Helper egl) {
        if (!egl.isAdreno()) {
            loadPrograms();
            Log.d(TAG, "program loader done");
        }
    }

    void loadFilter(GPUImageFilter filter) {
        filter.init();
        filter.destroy();
        filter.destroySecondary();
    }

    void loadPrograms() {
        loadFilter(new GPUImageOESFilter());
        loadFilter(new GPUImageMultiplyBlendFilter());
        loadFilter(getBilateralFilter());
        loadFilter(getFadeFilter());
        loadFilter(getLUTFilters());
        loadFilter(getWhiteBalanceFilter());
        loadFilter(getSaturationFilter());
        loadFilter(getGrainFilter());
        loadFilter(getScreenBlendFilter());
        loadFilter(getPrismFilter());
        loadFilter(getDateFilter());
    }

    @NonNull
    public static GPUImageToneCurveFilter getBrightFilter() {
        GPUImageToneCurveFilter brightFilter = new GPUImageToneCurveFilter();
        InputStream in2 = null;
        try {
            in2 = App.getContext().getAssets().open("filters/special/bright.acv");
            brightFilter.setFromCurveFileInputStream(in2);
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return brightFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getSmoothFilter() {
        GPUImageToneCurveFilter smoothFilter = new GPUImageToneCurveFilter();
        InputStream in = null;
        try {
            in = App.getContext().getAssets().open("filters/special/smooth2.acv");
            smoothFilter.setFromCurveFileInputStream(in);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return smoothFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getDarkFilter() {
        GPUImageToneCurveFilter darkFilter = new GPUImageToneCurveFilter();
        InputStream in = null;
        try {
            in = App.getContext().getAssets().open("filters/special/dark.acv");
            darkFilter.setFromCurveFileInputStream(in);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return darkFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getMovieFilter1() {
        GPUImageToneCurveFilter movieFilter1 = new GPUImageToneCurveFilter();
        InputStream in = null;
        try {
            in = App.getContext().getAssets().open("filters/special/movie.acv");
            movieFilter1.setFromCurveFileInputStream(in);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return movieFilter1;
    }

    @NonNull
    public static GPUImageBilateralFilter0 getBilateralFilter() {
        GPUImageBilateralFilter0 bilateralFilter = new GPUImageBilateralFilter0();
        bilateralFilter.setDistanceNormalizationFactor(4.0f);
        return bilateralFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getFadeFilter() {
        GPUImageToneCurveFilter fadeFilter = new GPUImageToneCurveFilter();
        InputStream in4 = null;
        try {
            in4 = App.getContext().getAssets().open("filters/special/fade.acv");
            fadeFilter.setFromCurveFileInputStream(in4);
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return fadeFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getEnhanceFilter() {
        GPUImageToneCurveFilter enhanceFilter = new GPUImageToneCurveFilter();
        InputStream in1 = null;
        try {
            in1 = App.getContext().getAssets().open("filters/special/enhance3.acv");
            enhanceFilter.setFromCurveFileInputStream(in1);
            if (in1 != null) {
                try {
                    in1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in1 != null) {
                try {
                    in1.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in1 != null) {
                try {
                    in1.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return enhanceFilter;
    }

    @NonNull
    public static GPUImageToneCurveFilter getContrastFilter() {
        GPUImageToneCurveFilter contrastFilter = new GPUImageToneCurveFilter();
        InputStream in4 = null;
        try {
            in4 = App.getContext().getAssets().open("filters/special/contrast.acv");
            contrastFilter.setFromCurveFileInputStream(in4);
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in4 != null) {
                try {
                    in4.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return contrastFilter;
    }

    @NonNull
    public static AFGPUImageColorShiftFilter getShiftFilter() {
        AFGPUImageColorShiftFilter mShiftFilter = new AFGPUImageColorShiftFilter();
        mShiftFilter.setGreenColorShift(new Point(10, 0));
        return mShiftFilter;
    }

    @NonNull
    public static AFGPUImageColorUnstableFilter getColorUnstableFilter() {
        return new AFGPUImageColorUnstableFilter();
    }

    @NonNull
    public static GPUImageToneCurveFilter getLUTFilters() {
        return new GPUImageToneCurveFilter();
    }

    @NonNull
    public static GPUImageWhiteBalanceFilter getWhiteBalanceFilter() {
        return new GPUImageWhiteBalanceFilter();
    }

    @NonNull
    public static GPUImageSaturationFilter getSaturationFilter() {
        return new GPUImageSaturationFilter();
    }

    @NonNull
    public static AFGPUImageOverlayBlendFilter getGrainFilter() {
        return new AFGPUImageOverlayBlendFilter();
    }

    @NonNull
    public static AFGPUImageScreenBlendFilter getScreenBlendFilter() {
        return new AFGPUImageScreenBlendFilter();
    }

    @NonNull
    public static AFGPUImageSkyFilter getSkyFilter() {
        return new AFGPUImageSkyFilter();
    }

    @NonNull
    public static GPUImagePrismFilter getPrismFilter() {
        return new GPUImagePrismFilter();
    }

    public static AFGPUImageDateBlendFilter getDateFilter() {
        return new AFGPUImageDateBlendFilter();
    }
}