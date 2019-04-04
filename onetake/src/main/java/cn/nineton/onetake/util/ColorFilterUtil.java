package cn.nineton.onetake.util;
import android.app.Activity;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import cn.nineton.onetake.listener.IComplateCallback;
//import com.blink.academy.onetake.support.callbacks.IComplateCallback;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil$.Lambda.1;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil$.Lambda.2;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil$.Lambda.3;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil$.Lambda.4;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil$.Lambda.5;

public class ColorFilterUtil {
    public static final int color92 = -3355444;
    public static final int colorAlert = -1619113;
    public static final int colorBlack = -14540254;
    public static final int colorGray = -6710887;
    public static final int colorLightgray = -3355444;
    public static final int colorTheme = -8960;
    public static final int colorTransparent = 0;
    public static final int colorTrueWhite = -1;
    public static final int colorWhite = -1;

    public static void setDrawableColorFilterLightgray(ImageView imageView) {
        setDrawableColorFilter(imageView, -1, colorGray);
    }

    public static void setDrawableColorFilter(ImageView imageView, int mulcolor, int addColor) {
        imageView.getDrawable().setColorFilter(new LightingColorFilter(mulcolor, addColor));
    }

    public static void setDrawable(Drawable drawable, int mulcolor, int addColor) {
        drawable.setColorFilter(new LightingColorFilter(mulcolor, addColor));
    }

    public static void drawableClearColorFilter(ImageView imageView) {
        imageView.getDrawable().clearColorFilter();
    }

    public static OnTouchListener TouchFocusChange() {
        return TouchFocusChange(true, 1.0f, 0.3f);
    }

    public static OnTouchListener TouchFocusChange(boolean isDelay) {
        return TouchFocusChange(isDelay, 1.0f, 0.3f);
    }

    public static OnTouchListener TouchFocusChange(final boolean isDelay, final float normalAlpha, final float pressAlpha) {
        //return 1.lambdaFactory$(pressAlpha, isDelay, normalAlpha);
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lambda$TouchFocusChange$0(pressAlpha,isDelay,normalAlpha,v,event);
            }
        };
    }

    private static /* synthetic */ boolean lambda$TouchFocusChange$0(float pressAlpha, boolean isDelay, final float normalAlpha, final View view, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                view.setAlpha(pressAlpha);
                break;
            case 1:
            case 3:
                if (!isDelay) {
                    view.setAlpha(normalAlpha);
                    break;
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (view.getAlpha() == 0.3f) {
                            view.setAlpha(normalAlpha);
                        }
                    }
                }, 100);
                break;
        }
        return false;
    }

    public static OnTouchListener TouchFocusBackGroundChange() {
        return TouchFocusBackGroundChange(true);
    }

    public static OnTouchListener TouchFocusBackGroundChange(final boolean isDelay) {
        //return 2.lambdaFactory$(isDelay);
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lambda$TouchFocusBackGroundChange$2(isDelay,v,event);
            }
        };
    }

    private static /* synthetic */ boolean lambda$TouchFocusBackGroundChange$2(boolean isDelay, final View view, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                view.setBackgroundColor(-3355444);
                break;
            case 1:
            case 3:
                if (!isDelay) {
                    view.setBackgroundColor(0);
                    break;
                }
                //new Handler().postDelayed(5.lambdaFactory$(view), 300);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setAlpha(0.5f);
                    }
                },300);
                break;
        }
        return false;
    }

    public static OnTouchListener TouchFocusBackGroundChange(final View changeView, final boolean isDelay) {
        //return 3.lambdaFactory$(changeView, isDelay);
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lambda$TouchFocusBackGroundChange$4(changeView,isDelay,v,event);
            }
        };
    }

    private static /* synthetic */ boolean lambda$TouchFocusBackGroundChange$4(final View changeView, boolean isDelay, final View view, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                changeView.setBackgroundColor(-3355444);
                break;
            case 1:
            case 3:
                if (!isDelay) {
                    changeView.setBackgroundColor(0);
                    break;
                }
                //new Handler().postDelayed(4.lambdaFactory$(changeView), 300);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeView.setAlpha(0.5f);
                    }
                },300);
                break;
        }
        return false;
    }

    public static OnTouchListener TouchScrollChange(final Activity activity, final IComplateCallback callback) {
        return new OnTouchListener() {
            private int mLastY;
            private int mTouchSlop = ViewConfiguration.get(activity).getScaledTouchSlop();
            private int mYOffset;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.mLastY = (int) motionEvent.getRawY();
                        break;
                    case 2:
                        this.mYOffset = (int) (motionEvent.getRawY() - ((float) this.mLastY));
                        if (Math.abs(this.mYOffset) > this.mTouchSlop) {
                            callback.done();
                            break;
                        }
                        break;
                }
                return false;
            }
        };
    }

    public static OnTouchListener TouchScrollChange(final IComplateCallback callback) {
        return new OnTouchListener() {
            private int mLastY;
            private int mTouchSlop = DensityUtil.dip2px(5.0f);
            private int mYOffset;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.mLastY = (int) motionEvent.getRawY();
                        break;
                    case 2:
                        this.mYOffset = (int) (motionEvent.getRawY() - ((float) this.mLastY));
                        if (Math.abs(this.mYOffset) > this.mTouchSlop) {
                            callback.done();
                            break;
                        }
                        break;
                }
                return false;
            }
        };
    }
}