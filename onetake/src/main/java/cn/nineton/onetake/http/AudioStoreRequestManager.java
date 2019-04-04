package cn.nineton.onetake.http;

import org.json.JSONArray;
import org.json.JSONObject;

public class AudioStoreRequestManager extends BasicRequestManager {
    public static void getAudioStoreIndex(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.getAudioIndexUrl(), callback);
    }

    public static void getAudioStoreDetail(int id, int page, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.getAudioDetailUrl(id, page), callback);
    }

    public static void postPurchaseAudio(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.getPurchaseAudioUrl(), params, callback);
    }

    public static void getAudioInfoByTrackIds(String ids, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.get_track_albums(ids), callback);
    }
}