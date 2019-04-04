package cn.nineton.onetake.http;

import android.text.TextUtils;
import com.android.volley.VolleyError;
//import com.blink.academy.onetake.bean.mscv.MscvResultBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.callbacks.IControllerCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.helper.GlobalHelper;
//import com.blink.academy.onetake.support.helper.UrlHelper;
//import com.blink.academy.onetake.support.thread.PriorityRunnable;
//import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
import java.io.IOException;
import java.util.HashMap;

import cn.nineton.onetake.bean.MscvResultBean;
import cn.nineton.onetake.listener.IControllerCallback;
import cn.nineton.onetake.util.GlobalHelper;
import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.PriorityRunnable;
import cn.nineton.onetake.util.PriorityThreadPoolManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class MSCVRequestManager extends BasicRequestManager {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
    private static final String MEDIATYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final String OT_SEARCH_TYPE_HOT = "hot";
    public static final String OT_SEARCH_TYPE_NEW = "new";
    public static final String OT_SEARCH_TYPE_NEW3 = "new3";
    private static final String PARAMS_BODY_NAME_ACC = "accbody";
    private static final String PARAMS_BODY_NAME_AMAP = "amapbody";
    private static final String PARAMS_BODY_NAME_EXIF = "exifbody";
    private static final String PARAMS_BODY_NAME_GEN = "genbody";
    private static final String PARAMS_BODY_NAME_GZHCN = "gzhcnbody";
    private static final String PARAMS_BODY_NAME_LATLNGRADIUS = "latlngradius";
    private static final String PARAMS_BODY_NAME_REC = "recbody";
    private static final String PARAMS_TYPE = "type";
    public static final String TYPE_AMAPGEO = "amapgeo";
    public static final String TYPE_GOOGLEGEO = "googlegeo";
    public static final String TYPE_RECOGNITION = "recognition";

    public static void requestGetMSCSKey(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.getMSCSKeyUrl(), callback);
    }

    public static void postGetMSCSResult(String url, String mscsKey, byte[] bytes, IControllerCallback<MscvResultBean> callback) {
        final byte[] bArr = bytes;
        final String str = url;
        final String str2 = mscsKey;
        final IControllerCallback<MscvResultBean> iControllerCallback = callback;
        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
            public void run() {
                Builder builder = new Builder();
                builder.setType(MultipartBody.FORM);
                builder.addPart(RequestBody.create(MSCVRequestManager.MEDIA_TYPE_PNG, bArr));
                MultipartBody build = builder.build();
                MSCVRequestManager.RequestLog(str, " mscsKey : " + str2 + " type : " + build.type());
                try {
                    Response response = new OkHttpClient().newCall(new Request.Builder().header("Content-Type", MSCVRequestManager.MEDIATYPE_MULTIPART_FORM_DATA).addHeader(MSCVRequestManager.HEADER_OCP_APIM_SUBSCRIPTION_KEY, str2).url(str).post(build).build()).execute();
                    LogUtil.d("URL", "response:" + response.toString());
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        LogUtil.d("URL", "body:" + body);
                        if (!"".equals(body)) {
                            MscvResultBean mscvResultBean = (MscvResultBean) JsonParserUtil.deserializeByJson(body, MscvResultBean.class);
                            if (iControllerCallback != null) {
                                iControllerCallback.success(mscvResultBean, body, 0, true);
                                return;
                            }
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (iControllerCallback != null) {
                    iControllerCallback.failure(new VolleyError());
                }
            }
        });
    }

    public static void postGetRecommendOfficialTags(String type, JSONObject recbody, JSONObject genbody, JSONObject gzhcnbody, JSONObject amapbody, String latlngradius, RequestCallback<JSONArray> callback) {
        postGetRecommendOfficialTags(type, recbody, genbody, gzhcnbody, amapbody, null, latlngradius, null, callback);
    }

    public static void postGetRecommendOfficialTags(String type, JSONObject recbody, JSONObject genbody, JSONObject gzhcnbody, JSONObject amapbody, JSONObject accbody, String latlngradius, JSONObject exifbody, RequestCallback<JSONArray> callback) {
        mRequestManager.sendPostArrayRequest(UrlHelper.getRecommendOt(), getOTParams(type, recbody, genbody, gzhcnbody, amapbody, accbody, latlngradius, exifbody), callback);
    }

    private static String getOTParams(String type, JSONObject recbody, JSONObject genbody, JSONObject gzhcnbody, JSONObject amapbody, JSONObject accbody, String latlngradius, JSONObject exifbody) {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("screen_name", GlobalHelper.getUserScreenName());
        hashMap.put("access_token", GlobalHelper.getUserAccessToken());
        if (recbody != null) {
            hashMap.put(PARAMS_BODY_NAME_REC, recbody);
        }
        if (genbody != null) {
            hashMap.put(PARAMS_BODY_NAME_GEN, genbody);
        }
        if (gzhcnbody != null) {
            hashMap.put(PARAMS_BODY_NAME_GZHCN, gzhcnbody);
        }
        if (amapbody != null) {
            hashMap.put(PARAMS_BODY_NAME_AMAP, amapbody);
        }
        if (accbody != null) {
            hashMap.put(PARAMS_BODY_NAME_ACC, accbody);
        }
        if (!TextUtils.isEmpty(latlngradius)) {
            hashMap.put(PARAMS_BODY_NAME_LATLNGRADIUS, latlngradius);
        }
        if (exifbody != null) {
            hashMap.put(PARAMS_BODY_NAME_EXIF, exifbody);
        }
        return new JSONObject(hashMap).toString();
    }

    public static void searchOfficialTagsPhoto(String value, String intent, String type, int page, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.getSearchOtUrl(value, intent, type, (long) page), callback);
    }

    private static void RequestLog(String url, String params) {
        LogUtil.d("URL", "url:" + url + ", params:" + params);
    }

    public static void getNewTagTopic(String value, String type, String zh, String en, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.getNewTagTopicUrl(value, type, zh, en), callback);
    }

    public static void getPromoteTags(RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.getPromoteTagUrl(), callback);
    }

    public static void getRecommendCategories(String params, RequestCallback<JSONArray> callback) {
        mRequestManager.sendPostArrayRequest(UrlHelper.getRecommendCategories(), params, callback);
    }
}