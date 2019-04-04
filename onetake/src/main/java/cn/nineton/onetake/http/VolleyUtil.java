package cn.nineton.onetake.http;

//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.ui.activity.material.GiphyLibraryActivity;
//import com.lzy.okhttputils.model.HttpHeaders;
//import com.qiniu.android.http.Client;
import com.lzy.okgo.model.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import cn.nineton.onetake.util.LocaleUtil;

public class VolleyUtil {
    public static Map<String, String> getJsonHeader() {
        HashMap<String, String> headers = new HashMap();
        headers.put(HttpHeaders.HEAD_KEY_ACCEPT, "application/json");//Client.JsonMime);
        if (LocaleUtil.isChineseCode()) {
            headers.put(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, "zh-cn,zh");
        } else {
            headers.put(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, "en-US");//GiphyLibraryActivity.LOCALE_EN_US);
        }
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }
}