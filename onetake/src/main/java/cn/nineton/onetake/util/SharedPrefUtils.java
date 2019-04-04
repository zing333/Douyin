package cn.nineton.onetake.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.nineton.onetake.App;

public class SharedPrefUtils {
    private static SharedPreferences mSp;

    private static Context getContext() {
        return App.getContext();
    }

    private static SharedPreferences getSharedPreferences() {
        if (mSp == null) {
            mSp = getContext().getSharedPreferences("config", 0);
        }
        return mSp;
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void putFloat(String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {
        return getSharedPreferences().getFloat(key, defValue);
    }

    public static void removeString(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }
}