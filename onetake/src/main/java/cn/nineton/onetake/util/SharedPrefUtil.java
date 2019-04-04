package cn.nineton.onetake.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.nineton.onetake.App;

public class SharedPrefUtil {
    private static String defValue = "";
    public static SharedPreferences mAppInfoSharedPreferences = App.getContext().getSharedPreferences("appinfo_pref", 0);
    public static SharedPreferences mSMSSharedPreferences = App.getContext().getSharedPreferences("sms_pref", 0);
    public static SharedPreferences mUserSharedPreferences = App.getContext().getSharedPreferences("user_pref", 0);

    public static void setAppInfoBoolean(String key, boolean value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setAppInfoLong(String key, long value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setAppInfoString(String key, String value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setAppInfoToInt(String key, int value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setFeedBackInfo(String key, String value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setFilterSpeedId(String key, int value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getFilterSpeedId(String key) {
        return mAppInfoSharedPreferences.getInt(key, -1);
    }

    public static void setFilterGroupName(String key, String value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFilterGroupName(String key) {
        return mAppInfoSharedPreferences.getString(key, "");
    }

    public static void setTwitterAccessToken(String key, String value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setTwitterSecretToken(String key, String value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setIsFirstJoinAPP(String key, boolean value) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getIsFirstJoinAPP(String key) {
        return mAppInfoSharedPreferences.getBoolean(key, true);
    }

    public static String getTwitterAccessToken(String key) {
        return mAppInfoSharedPreferences.getString(key, defValue);
    }

    public static String getTwitterSecretToken(String key) {
        return mAppInfoSharedPreferences.getString(key, defValue);
    }

    public static String getFeedBackInfo(String key, String defValue) {
        return mAppInfoSharedPreferences.getString(key, defValue);
    }

    public static int getAppInfoToInt(String key, int defValue) {
        return mAppInfoSharedPreferences.getInt(key, defValue);
    }

    public static void setSendMessageNumber(String key, int value) {
        Editor editor = mSMSSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getSendMessageNumber(String key) {
        return mSMSSharedPreferences.getInt(key, 0);
    }

    public static void setIsNeedToRefreshToken(String key, boolean value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setIsShowSharePop(String key, boolean value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getIsShowSharePop(String key) {
        return mUserSharedPreferences.getBoolean(key, true);
    }

    public static void setSharePopPhotoId(String key, int photoId) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putInt(key, photoId);
        editor.apply();
    }

    public static int getSharePopPhotoId(String key) {
        return mUserSharedPreferences.getInt(key, -1);
    }

    public static boolean getIsNeedToRefreshToken(String key) {
        return mUserSharedPreferences.getBoolean(key, true);
    }

    public static boolean getAppInfoBoolean(String key) {
        return mAppInfoSharedPreferences.getBoolean(key, false);
    }

    public static long getAppInfoLong(String key) {
        return mAppInfoSharedPreferences.getLong(key, 0);
    }

    public static boolean getAppInfoBoolean(String key, boolean defaultValue) {
        return mAppInfoSharedPreferences.getBoolean(key, defaultValue);
    }

    public static boolean getAppSplash() {
        return mAppInfoSharedPreferences.getBoolean("app_had_splash", false);
    }

    public static void setAppSplash() {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putBoolean("app_had_splash", true);
        editor.apply();
    }

    public static boolean getPhoneIsFirstOpenCamera(String key) {
        return mAppInfoSharedPreferences.getBoolean(key, true);
    }

    public static void setPhoneIsFirstOpenCamera(String key, boolean isFirst) {
        Editor editor = mAppInfoSharedPreferences.edit();
        editor.putBoolean(key, isFirst);
        editor.apply();
    }

    public static String getAppInfoString(String key) {
        return mAppInfoSharedPreferences.getString(key, defValue);
    }

    public static void setUserInfoToString(String key, String value) {
        Editor editor = mUserSharedPreferences.edit();
        if (TextUtil.isNull(value)) {
            value = "";
        }
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserInfoToString(String key) {
        return mUserSharedPreferences.getString(key, defValue);
    }

    public static void setUserInfoToInt(String key, int value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getUserInfoToInt(String key) {
        return mUserSharedPreferences.getInt(key, 280);
    }

    public static void setUserInfoToBoolean(String key, boolean value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setUserSaveOriginPhoto(boolean value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("user_save_origin_photo", value);
        editor.apply();
    }

    public static boolean getUserSaveOtiginPhoto() {
        return mUserSharedPreferences.getBoolean("user_save_origin_photo", false);
    }

    public static void setUserSaveTemplates(boolean value) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("user_save_templates", value);
        editor.apply();
    }

    public static boolean getUserSaveTemplates() {
        return mUserSharedPreferences.getBoolean("user_save_templates", false);
    }

    public static boolean getUserInfoToBoolean(String key, boolean defDefault) {
        return mUserSharedPreferences.getBoolean(key, defDefault);
    }

    public static void clearUserSharePreferences() {
        Editor editor = mUserSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static synchronized void setInfoList(String key, List<String> strList) {
        synchronized (SharedPrefUtil.class) {
            Editor editor = mUserSharedPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();
            int size = strList.size();
            for (int index = 0; index < size; index++) {
                stringBuilder.append((String) strList.get(index));
                if (index != size - 1) {
                    stringBuilder.append(SpannedUtil.comma);
                }
            }
            if (size > 0) {
                editor.putString(key, stringBuilder.toString());
            } else {
                editor.putString(key, "");
            }
            editor.apply();
        }
    }

    public static synchronized List<String> getInfoList(String key) {
        List<String> strList;
        synchronized (SharedPrefUtil.class) {
            String manifestArray = mUserSharedPreferences.getString(key, null);
            strList = new ArrayList();
            if (manifestArray != null) {
                Collections.addAll(strList, manifestArray.split(SpannedUtil.comma));
            }
        }
        return strList;
    }

    public static double getLocalVersionCode() {
        return (double) mUserSharedPreferences.getInt("local_version_code", 1);
    }

    public static void setLocalVersionCode(int localVersionCode) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putInt("local_version_code", localVersionCode);
        editor.apply();
    }

    public static void setFirstIntoOnetake() {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("the_user_first_into_onetake", false);
        editor.apply();
    }

    public static boolean getFirstIntoOnetake() {
        return mUserSharedPreferences.getBoolean("the_user_first_into_onetake", true);
    }

    public static void setUserFirstSaveDraft() {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("first_save_video_draft", false);
        editor.apply();
    }

    public static boolean getUserFirstSaveDraft() {
        return mUserSharedPreferences.getBoolean("first_save_video_draft", true);
    }

    public static void setFirstIntoAppDiscover() {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("first_into_app_no_login_or_discover", false);
        editor.apply();
    }

    public static boolean getFirstInfoOrDiscover() {
        return mUserSharedPreferences.getBoolean("first_into_app_no_login_or_discover", true);
    }

    public static boolean getUserCouldUseCamera() {
        return mUserSharedPreferences.getBoolean("test_user_can_camera", false);
    }

    public static void setUserCouldUseCamera(boolean canCamera) {
        Editor editor = mUserSharedPreferences.edit();
        editor.putBoolean("test_user_can_camera", canCamera);
        editor.apply();
    }
}