package cn.nineton.onetake.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import cn.nineton.onetake.App;

public class DensityUtil {
    public static int mScreenHeight = 0;
    public static int mScreenWidth = 0;

    public static int dip2px(float dpValue) {
        return (int) ((dpValue * Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) ((pxValue / Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int getDensity() {
        return (int) (0.5f + Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int get50DP() {
        return dip2px(App.getContext(), 50.0f);
    }

    public static int get55DP() {
        return dip2px(App.getContext(), 55.0f);
    }

    public static int sp2px(Context context, int spValue) {
        return (int) ((((float) spValue) - 0.5f) * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static float getGridViewPhotoWidth(int numColumns, int space) {
        return (((float) getMetricsWidth(App.getContext())) - (TypedValue.applyDimension(1, (float) space, App.getResource().getDisplayMetrics()) * ((float) (numColumns - 1)))) / ((float) numColumns);
    }

    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getMetricsWidth(Context context) {
        if (context == null) {
            return 0;
        }
        if (mScreenWidth != 0) {
            return mScreenWidth;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getMetricsHeight(Context context) {
        if (context == null) {
            return 0;
        }
        if (mScreenHeight != 0) {
            return mScreenHeight;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static double getLayoutScale() {
        return ((double) (((float) getMetricsHeight(App.getContext())) / App.getContext().getResources().getDisplayMetrics().density)) / 568.0d;
    }
}