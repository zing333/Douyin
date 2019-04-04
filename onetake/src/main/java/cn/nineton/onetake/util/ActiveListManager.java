package cn.nineton.onetake.util;

import android.os.Build;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.utils.SharedPrefUtils;
//import com.blink.academy.onetake.support.utils.SpannedUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nineton.onetake.bean.IExceptionCallback;

public class ActiveListManager {
    public static final String OneMinuteActive = "一分钟";
    private static ActiveListManager instance;
    private List<String> activeList;
    private String phoneName;

    private ActiveListManager() {
    }

    public void updatePhoneName(Map<String, String> map) {
        if (TextUtil.isValidate(map)) {
            Set<String> strings = map.keySet();
            String firstKey = "";
            String result = "";
            for (String s : strings) {
                if (s.equalsIgnoreCase(Build.MANUFACTURER)) {
                    firstKey = s;
                }
            }
            if (TextUtil.isValidate(firstKey)) {
                result = (String) map.get(firstKey);
                if ("needModel".equalsIgnoreCase(result)) {
                    String secondKey = "";
                    for (String s2 : strings) {
                        if (s2.equalsIgnoreCase(Build.MANUFACTURER + SpannedUtil.IMG_REPLACE_STR + Build.MODEL)) {
                            secondKey = s2;
                        }
                    }
                    if (TextUtil.isValidate(secondKey)) {
                        result = (String) map.get(secondKey);
                    } else {
                        result = (String) map.get(firstKey + "-default");
                    }
                }
            }
            LogUtil.d("slim", "save phone name:" + result);
            instance.phoneName = result;
            SharedPrefUtils.putString(Constants.PHONE_NAME_STR, result);
        }
    }

    public String getPhoneName() {
        if (TextUtil.isValidate(instance.phoneName)) {
            return instance.phoneName;
        }
        return "Android 手机";
    }

    public void updateActiveList(List<String> list) {
        instance.activeList = list;
        SharedPrefUtils.putString(Constants.ACTIVE_LIST, JsonParserUtil.serializeToJson(list));
        boolean z = instance.activeList != null && instance.activeList.contains(OneMinuteActive);
        Constants.OPEN_ACTIVE_ONE_MINUTE = z;
    }

    public List<String> getActiveList() {
        return instance.activeList;
    }

    public static ActiveListManager getInstance() {
        if (instance == null) {
            instance = new ActiveListManager();
            instance.phoneName = SharedPrefUtils.getString(Constants.PHONE_NAME_STR, "");
            String string = SharedPrefUtils.getString(Constants.ACTIVE_LIST, "");
            if (TextUtil.isValidate(string)) {
                instance.activeList = JsonParserUtil.parseList(string, new TypeToken<ArrayList<String>>() {
                }.getType(), new IExceptionCallback() {
                    public void doException() {
                    }
                });
                boolean z = instance.activeList != null && instance.activeList.contains(OneMinuteActive);
                Constants.OPEN_ACTIVE_ONE_MINUTE = z;
            }
        }
        return instance;
    }
}