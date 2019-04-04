package cn.nineton.onetake.util;

import java.io.IOException;

public class MIUIUtil {
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

    public static boolean isMIUI() {
        try {
            BuildPropertiesUtil prop = BuildPropertiesUtil.newInstance();
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) == null && prop.getProperty(KEY_MIUI_VERSION_NAME, null) == null && prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}