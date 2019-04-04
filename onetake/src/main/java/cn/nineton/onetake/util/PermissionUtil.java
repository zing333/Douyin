package cn.nineton.onetake.util;

import android.support.v4.content.ContextCompat;

import cn.nineton.onetake.App;

public class PermissionUtil {
    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            LogUtil.d("granted", "verifyPermissions:grantResults.length < 1:false");
            return false;
        }
        for (int result : grantResults) {
            LogUtil.d("granted", "verifyPermissions:result:" + result);
            if (result != 0) {
                LogUtil.d("granted", "verifyPermissions:grantResults:false");
                return false;
            }
        }
        LogUtil.d("granted", "verifyPermissions:true");
        return true;
    }

    public static boolean verifyAudioPermission() {
        return ContextCompat.checkSelfPermission(App.getContext(), "android.permission.RECORD_AUDIO") == 0;
    }

    public static boolean verifyCameraPermission() {
        return ContextCompat.checkSelfPermission(App.getContext(), "android.permission.CAMERA") == 0;
    }

    public static boolean verifyAccessFineLocationPermission() {
        return ContextCompat.checkSelfPermission(App.getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public static boolean verifyReadExternalPermission() {
        return ContextCompat.checkSelfPermission(App.getContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0;
    }
}