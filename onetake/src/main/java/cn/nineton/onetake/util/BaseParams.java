package cn.nineton.onetake.util;

import com.google.gson.Gson;
import java.util.HashMap;

public class BaseParams {
    public static final String TAG = BaseParams.class.getSimpleName();
    protected static final String access_token_str = "access_token";
    protected static final String screen_name_str = "screen_name";
    protected String access_token;
    protected String screen_name;

    public BaseParams() {
        this.screen_name = GlobalHelper.getUserScreenName();
        this.access_token = GlobalHelper.getUserAccessToken();
    }

    public BaseParams(String screen_name, String access_token) {
        this.screen_name = screen_name;
        this.access_token = access_token;
    }

    public String getRequestParam() {
        HashMap mapParam = new HashMap();
        mapParam.put("screen_name", this.screen_name);
        mapParam.put("access_token", this.access_token);
        return new Gson().toJson(mapParam);
    }
}