package cn.nineton.onetake.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.nineton.onetake.bean.IExceptionCallback;

public class JsonParserUtil {
    public static final String TAG = JsonParserUtil.class.getSimpleName();

    public static <T> T deserializeByJson(String data, Type type) {
        try {
            if (TextUtil.isValidate(data)) {
                return new Gson().fromJson(jsonReader(data), type);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public static <T> T deserializeByJsonNoJsonReader(String data, Type type) {
        if (TextUtil.isValidate(data)) {
            return new Gson().fromJson(data, type);
        }
        return null;
    }

    public static <T> T deserializeByJson(String data, Class<T> clz) {
        try {
            if (TextUtil.isValidate(data)) {
                return new Gson().fromJson(jsonReader(data), clz);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public static <T> String serializeToJson(T t) {
        if (t == null) {
            return "";
        }
        return new Gson().toJson(t);
    }

    private static JsonReader jsonReader(String data) {
        JsonReader reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        return reader;
    }

    public static <T> T parse(String jsonString, Class<T> clz, IExceptionCallback callback) {
        T t = null;
        if (TextUtil.isNull(jsonString)) {
            return t;
        }
        try {
            return deserializeByJson(jsonString, TypeToken.get((Class) clz).getType());
        } catch (JsonSyntaxException e) {
            if (callback == null) {
                return t;
            }
            callback.doException();
            return t;
        }
    }

    public static <T> List<T> parseList(String jsonString, Type type, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (List) deserializeByJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            if (callback != null) {
                callback.doException();
            }
            return null;
        }
    }

    public static <T> ArrayList<T> parseList(Reader reader, Type type, IExceptionCallback callback) {
        if (TextUtil.isNull((Object) reader)) {
            return null;
        }
        try {
            return (ArrayList) new Gson().fromJson(reader, type);
        } catch (JsonSyntaxException e) {
            if (callback != null) {
                callback.doException();
            }
            return null;
        }
    }

    public static <T> String fromList(List<T> list, IExceptionCallback callback) {
        String str = null;
        if (TextUtil.isNull((Collection) list)) {
            return str;
        }
        try {
            return new Gson().toJson(list);
        } catch (JsonSyntaxException e) {
            if (callback == null) {
                return str;
            }
            callback.doException();
            return str;
        }
    }
}