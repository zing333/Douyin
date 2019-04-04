package cn.nineton.onetake.widget.video;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Locale;

public class JZUtils {
    public static final String TAG = "JiaoZiVideoPlayer";
    public static boolean singlePhotoDetail = false;
    public static boolean toFullScreen = false;

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 86400000) {
            return "0:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        Formatter mFormatter = new Formatter(new StringBuilder(), Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        } else if (minutes >= 10) {
            return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        } else {
            return mFormatter.format("%01d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 1) {
            return false;
        }
        return true;
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

    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        return context instanceof ContextThemeWrapper ? getAppCompActivity(((ContextThemeWrapper) context).getBaseContext()) : null;
    }

    public static void setRequestedOrientation(Context context, int orientation) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context).setRequestedOrientation(orientation);
            return;
        }
        Activity activity = scanForActivity(context);
        if (activity != null) {
            activity.setRequestedOrientation(orientation);
        }
    }

    public static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        }
        return scanForActivity(context).getWindow();
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void saveProgress(String url, int progress) {
        if (JZVideoPlayer.SAVE_PROGRESS) {
            Log.i("JiaoZiVideoPlayer", "saveProgress: " + progress);
            VideoProgressManager.getInstance().saveProgress(url, progress);
        }
    }

    public static int getSavedProgress(String url) {
        if (JZVideoPlayer.SAVE_PROGRESS) {
            return VideoProgressManager.getInstance().getProgress(url);
        }
        return 0;
    }

    public static void clearSavedProgress(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().clear().apply();
        } else {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().putInt(url, 0).apply();
        }
    }

    public static String getCurrentUrlFromMap(LinkedHashMap<String, String> map, int index) {
        if (map.size() == 1) {
            return getValueFromLinkedMap(map, index);
        }
        return getValueFromLinkedMap(map, index);
    }

    public static String getValueFromLinkedMap(LinkedHashMap<String, String> map, int index) {
        int currentIndex = 0;
        for (Object key : map.keySet()) {
            if (currentIndex == index) {
                return (String) map.get(key);
            }
            currentIndex++;
        }
        return null;
    }

    public static String getKeyFromLinkedMap(LinkedHashMap<String, String> map, int index) {
        int currentIndex = 0;
        for (Object key : map.keySet()) {
            if (currentIndex == index) {
                return key.toString();
            }
            currentIndex++;
        }
        return null;
    }
}