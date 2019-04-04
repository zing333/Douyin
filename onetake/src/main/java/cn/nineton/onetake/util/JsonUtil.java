package cn.nineton.onetake.util;

import android.os.Build.VERSION;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static String mapToJsonString(Map<String, Object> params) {
        if (params == null) {
            return "";
        }
//        if (VERSION.SDK_INT >= 19) {
            return new Gson().toJson((Object) params);
//        }
//        StringBuilder paramsContent = new StringBuilder();
//        paramsContent.append("{");
//        for (String key : params.keySet()) {
//            List<Object> obj = (List<Object>) params.get(key);
//            if (obj != null) {
//                if (obj instanceof Map) {
//                    Object obj2 = mapToJsonString((Map) obj);
//                    paramsContent.append(String.format("\"%1$s\":%2$s,", new Object[]{key, obj2}));
//                } else if (obj instanceof List) {
//                    paramsContent.append(String.format("\"%1$s\":[", new Object[]{key}));
//                    List<Object> objList = obj;
//                    if (TextUtil.isValidate(objList)) {
//                        for (Object o : objList) {
//                            if (o instanceof Map) {
//                                paramsContent.append(String.format("%1$s,", new Object[]{mapToJsonString((Map) o)}));
//                            } else if (o instanceof String) {
//                                paramsContent.append(String.format("\"%1$s\",", new Object[]{(String) o}));
//                            } else if ((o instanceof Integer) || (o instanceof Long)) {
//                                paramsContent.append(String.format("%1$s,", new Object[]{o}));
//                            }
//                        }
//                        paramsContent.deleteCharAt(paramsContent.length() - 1);
//                    }
//                    paramsContent.append("],");
//                } else if (obj instanceof String) {
//                    String objStr = ((String) obj).replaceAll("\n", "\\\\n");
//                    paramsContent.append(String.format("\"%1$s\":\"%2$s\",", new Object[]{key, objStr}));
//                } else if ((obj instanceof Integer) || (obj instanceof Long)) {
//                    paramsContent.append(String.format("\"%1$s\":%2$s,", new Object[]{key, obj}));
//                } else {
//                    paramsContent.append(String.format("\"%1$s\":\"%2$s\",", new Object[]{key, obj}));
//                }
//            }
//        }
//        paramsContent.deleteCharAt(paramsContent.length() - 1);
//        paramsContent.append("}");
//        return paramsContent.toString();
    }
}