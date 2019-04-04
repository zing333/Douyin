package cn.nineton.onetake.http;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IRequestManager {
    void sendDeleteArrayRequest(String str, String str2, RequestCallback<JSONArray> requestCallback);

    void sendDeleteObjectRequest(String str, String str2, RequestCallback<JSONObject> requestCallback);

    void sendGetArrayRequest(String str, RequestCallback<JSONArray> requestCallback);

    void sendGetNextPageRequest(String str, int i, boolean z, RequestCallback<JSONArray> requestCallback);

    void sendGetObjectRequest(String str, RequestCallback<JSONObject> requestCallback);

    void sendPostArrayRequest(String str, String str2, RequestCallback<JSONArray> requestCallback);

    void sendPostJsonRequest(String str, String str2, RequestCallback<String> requestCallback);

    void sendPostObjectRequest(String str, String str2, RequestCallback<JSONObject> requestCallback);

    void sendPutArrayRequest(String str, String str2, RequestCallback<JSONArray> requestCallback);

    void sendPutJsonRequest(String str, String str2, RequestCallback<String> requestCallback);

    void sendPutObjectRequest(String str, String str2, RequestCallback<JSONObject> requestCallback);
}