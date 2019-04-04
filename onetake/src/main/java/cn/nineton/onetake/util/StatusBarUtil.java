package cn.nineton.onetake.util;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;

public class StatusBarUtil {
    public static void setColorNav(Activity activity) {
        if (VERSION.SDK_INT > 19) {
            if (VERSION.SDK_INT == 19) {
                activity.getWindow().addFlags(67108864);
            }
            View statusView = createStatusView(activity, VERSION.SDK_INT >= 23 ? activity.getResources().getColor(R.color.colorWhite) : activity.getResources().getColor(R.color.colorTrueBlack));
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (VERSION.SDK_INT < 23) {
                decorView.setSystemUiVisibility(0);
            } else if (App.isMIUI()) {
                MIUISetStatusBarLightMode(activity.getWindow(), true);
            } else {
                decorView.setSystemUiVisibility(8192);//(Utility.DEFAULT_STREAM_BUFFER_SIZE);
            }
            statusView.setId(R.id.home_decorView);
            decorView.addView(statusView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    public static void setColor(Activity activity, int color) {
        if (VERSION.SDK_INT > 19) {
            if (VERSION.SDK_INT == 19) {
                activity.getWindow().addFlags(67108864);
            }
            View statusView = createStatusView(activity, color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (VERSION.SDK_INT >= 23) {
                if (App.isMIUI()) {
                    MIUISetStatusBarLightMode(activity.getWindow(), false);
                } else if (App.mSystemUiVisibility != -1) {
                    decorView.setSystemUiVisibility(App.mSystemUiVisibility);
                }
            }
            statusView.setId(R.id.home_decorView);
            decorView.addView(statusView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private static View createStatusView(Activity activity, int color) {
        int statusBarHeight = activity.getResources().getDimensionPixelSize(activity.getResources().getIdentifier("status_bar_height", "dimen", "android"));
        View statusView = new View(activity);
        statusView.setLayoutParams(new LayoutParams(-1, statusBarHeight));
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public static int statusViewHeight(Activity activity) {
        if (VERSION.SDK_INT < 19) {
            return 0;
        }
        return activity.getResources().getDimensionPixelSize(activity.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static void setTranslucent(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= bit ^ -1;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        Class clazz = window.getClass();
        try {
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int darkModeFlag = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
            if (dark) {
                extraFlagField.invoke(window, new Object[]{Integer.valueOf(darkModeFlag), Integer.valueOf(darkModeFlag)});
            } else {
                extraFlagField.invoke(window, new Object[]{Integer.valueOf(0), Integer.valueOf(darkModeFlag)});
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}