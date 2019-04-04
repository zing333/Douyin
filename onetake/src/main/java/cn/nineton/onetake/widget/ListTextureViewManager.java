package cn.nineton.onetake.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Window;

public class ListTextureViewManager {
    public static boolean ACTION_BAR_EXIST = true;
    public static boolean TOOL_BAR_EXIST = true;
    public static boolean lastEdit = false;
    public static int postion = 0;
    public static TextureVideoView textureVideoView;

    public static void hideSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        if (TOOL_BAR_EXIST) {
            getWindow(context).setFlags(1024, 1024);
        }
    }

    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        return context instanceof ContextThemeWrapper ? getAppCompActivity(((ContextThemeWrapper) context).getBaseContext()) : null;
    }

    public static void showSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }
        if (TOOL_BAR_EXIST) {
            getWindow(context).clearFlags(1024);
        }
    }

    public static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        }
        return scanForActivity(context).getWindow();
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return context instanceof ContextWrapper ? scanForActivity(((ContextWrapper) context).getBaseContext()) : null;
    }

    public static void setRequestedOrientation(Context context, int orientation) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context).setRequestedOrientation(orientation);
        } else {
            scanForActivity(context).setRequestedOrientation(orientation);
        }
    }
}