package cn.nineton.onetake.controller;

import android.app.Activity;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.error.ErrorBean;
//import com.blink.academy.onetake.bean.giphy.GiphyElementModel;
//import com.blink.academy.onetake.bean.tag.NewTagTopicBean;
//import com.blink.academy.onetake.bean.tag.OfficialTagBean;
//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.fresco.PreDownloadUtil;
//import com.blink.academy.onetake.http.request.MSCVRequestManager;
//import com.blink.academy.onetake.http.request.RequestCallback;
//import com.blink.academy.onetake.model.MscvModel;
//import com.blink.academy.onetake.support.callbacks.IControllerCallback;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.OfficialTagListEvent;
//import com.blink.academy.onetake.support.helper.GlobalHelper;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.adapter.entities.TimeLineCardEntity;
//import com.google.gson.reflect.TypeToken;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.ErrorBean;
import cn.nineton.onetake.bean.GiphyElementModel;
import cn.nineton.onetake.bean.IExceptionCallback;
import cn.nineton.onetake.bean.MscvModel;
import cn.nineton.onetake.bean.NewTagTopicBean;
import cn.nineton.onetake.bean.OfficialTagBean;
import cn.nineton.onetake.bean.TimeLineCardEntity;
import cn.nineton.onetake.bean.TimelineBean;
import cn.nineton.onetake.event.OfficialTagListEvent;
import cn.nineton.onetake.http.MSCVRequestManager;
import cn.nineton.onetake.http.RequestCallback;
import cn.nineton.onetake.listener.IControllerCallback;
import cn.nineton.onetake.util.GlobalHelper;
import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

public class MSCVController {
    public static final String TAG = MSCVController.class.getSimpleName();

    public static void postRecognitionGetOfficialTag(JSONObject recbody) {
        MSCVRequestManager.postGetRecommendOfficialTags("recognition", recbody, null, null, null, null, new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                LogUtil.d(MSCVController.TAG, "postRecognitionGetOfficialTag jsonArray : " + jsonArray.toString());
                MSCVController.dealRecommendOT(jsonArray, true);
            }

            public void error(ErrorBean error) {
                super.error(error);
                MSCVController.dealRecommendOTFaile(true);
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                MSCVController.dealRecommendOTFaile(true);
            }
        });
    }

    private static void dealRecommendOTFaile(final boolean isMscv) {
        App.runOnUiThread(new Runnable() {
            public void run() {
                if (isMscv) {
                    MscvModel.getInstance().setMscvRecTagList(new ArrayList());
                    EventBus.getDefault().post(new OfficialTagListEvent(-1));
                    return;
                }
                MscvModel.getInstance().setGpsRecTagList(new ArrayList());
                EventBus.getDefault().post(new OfficialTagListEvent(-2));
            }
        });
    }

    private static void dealRecommendOT(JSONArray jsonArray, final boolean isMscv) {
        final List<OfficialTagBean> officialTagBeanList = JsonParserUtil.parseList(jsonArray.toString(), new TypeToken<List<OfficialTagBean>>() {
        }.getType(), new IExceptionCallback() {
            public void doException() {
            }
        });
        if (officialTagBeanList != null) {
            App.runOnUiThread(new Runnable() {
                public void run() {
                    if (isMscv) {
                        MscvModel.getInstance().setMscvRecTagList(officialTagBeanList);
                        EventBus.getDefault().post(new OfficialTagListEvent(1));
                        return;
                    }
                    MscvModel.getInstance().setGpsRecTagList(officialTagBeanList);
                    EventBus.getDefault().post(new OfficialTagListEvent(2));
                }
            });
        }
    }

    public static void postGooglegeoGetOfficialTag(JSONObject genbody, JSONObject gzhcnbody, String latlngradius) {
        MSCVRequestManager.postGetRecommendOfficialTags("googlegeo", null, genbody, gzhcnbody, null, latlngradius, new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                LogUtil.d(MSCVController.TAG, "postEnGooglegeoGetOfficialTag jsonArray : " + jsonArray.toString());
                MSCVController.dealRecommendOT(jsonArray, false);
            }

            public void error(ErrorBean error) {
                super.error(error);
                MSCVController.dealRecommendOTFaile(false);
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                MSCVController.dealRecommendOTFaile(false);
            }
        });
    }

    public static void postAccuMapgeoGetOfficialTag(JSONObject accbody, String latlngradius) {
        MSCVRequestManager.postGetRecommendOfficialTags(null, null, null, null, null, accbody, latlngradius, null, new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                LogUtil.d(MSCVController.TAG, "postAccuMapgeoGetOfficialTag jsonArray : " + jsonArray.toString());
                MSCVController.dealRecommendOT(jsonArray, false);
            }

            public void error(ErrorBean error) {
                super.error(error);
                MSCVController.dealRecommendOTFaile(false);
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                MSCVController.dealRecommendOTFaile(false);
            }
        });
    }

    public static void postAMapgeoGetOfficialTag(JSONObject amapbody, String latlngradius) {
        MSCVRequestManager.postGetRecommendOfficialTags("amapgeo", null, null, null, amapbody, latlngradius, new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                LogUtil.d(MSCVController.TAG, "postZhCNMapgeoGetOfficialTag jsonArray : " + jsonArray.toString());
                MSCVController.dealRecommendOT(jsonArray, false);
            }

            public void error(ErrorBean error) {
                super.error(error);
                MSCVController.dealRecommendOTFaile(false);
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                MSCVController.dealRecommendOTFaile(false);
            }
        });
    }

    public static void postExifGetOfficialTag(JSONObject exifBody) {
        MSCVRequestManager.postGetRecommendOfficialTags(null, null, null, null, null, null, null, exifBody, new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                LogUtil.d(MSCVController.TAG, "postExifGetOfficialTag jsonArray : " + jsonArray.toString());
                MSCVController.dealRecommendOTForExif(jsonArray);
            }

            public void error(ErrorBean error) {
                super.error(error);
                MSCVController.dealRecommendOTFailForExif();
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                MSCVController.dealRecommendOTFailForExif();
            }
        });
    }

    private static void dealRecommendOTFailForExif() {
        App.runOnUiThread(new Runnable() {
            public void run() {
                MscvModel.getInstance().setExifTagsList(new ArrayList());
                EventBus.getDefault().post(new OfficialTagListEvent(-3));
            }
        });
    }

    private static void dealRecommendOTForExif(JSONArray jsonArray) {
        final List<OfficialTagBean> officialTagBeanList = JsonParserUtil.parseList(jsonArray.toString(), new TypeToken<List<OfficialTagBean>>() {
        }.getType(), new IExceptionCallback() {
            public void doException() {
            }
        });
        if (officialTagBeanList != null) {
            App.runOnUiThread(new Runnable() {
                public void run() {
                    MscvModel.getInstance().setExifTagsList(officialTagBeanList);
                    EventBus.getDefault().post(new OfficialTagListEvent(3));
                }
            });
            return;
        }
        MscvModel.getInstance().setExifTagsList(new ArrayList());
        EventBus.getDefault().post(new OfficialTagListEvent(-3));
    }

    public static void searchOfficialTagsHotPhotos(final Activity activity, String value, String type, int page, final IControllerCallback<List<TimeLineCardEntity>> callback) {
//        MSCVRequestManager.searchOfficialTagsPhoto(value, GiphyElementModel.TYPE_HOT, type, page, new RequestCallback<JSONArray>() {
//            public void onSuccess(JSONArray jsonArray) {
//                TimelineBean timelineBean;
//                boolean z = false;
//                LogUtil.d(MSCVController.TAG, "searchOfficialTagsHotPhotos jsonArray : " + jsonArray.toString());
//                Collection<TimelineBean> timelineBeanList = TimelineBean.parseList(jsonArray.toString(), new IExceptionCallback() {
//                    public void doException() {
//                    }
//                });
//                if (timelineBeanList != null && timelineBeanList.size() > 0) {
//                    for (int i = timelineBeanList.size() - 1; i >= 0; i--) {
//                        timelineBean = (TimelineBean) timelineBeanList.get(i);
//                        if (timelineBean.width == 0 || timelineBean.height == 0) {
//                            timelineBeanList.remove(i);
//                        }
//                    }
//                }
//                List<TimeLineCardEntity> timeLineCardEntityList = new ArrayList();
//                long cursorId = -1;
//                if (TextUtil.isValidate((Collection) timelineBeanList)) {
//                    for (TimelineBean timelineBean2 : timelineBeanList) {
//                        Object timeLineCardEntity = TimelineController.getTimelineCardEntity(activity, timelineBean2, false);
//                        if (TextUtil.isValidate(timeLineCardEntity)) {
//                            timeLineCardEntityList.add(timeLineCardEntity);
//                        }
//                        cursorId = timelineBean2.published_at;
//                    }
//                    for (TimeLineCardEntity timeLineCardEntity2 : timeLineCardEntityList) {
//                        PreDownloadUtil.prefetchMainToBitmapCache(timeLineCardEntity2.getAvatarUrl());
//                    }
//                    for (TimeLineCardEntity timeLineCardEntity22 : timeLineCardEntityList) {
//                        if (TextUtil.isValidate(timeLineCardEntity22.getLong_thumbnail_url())) {
//                            PreDownloadUtil.prefetchMainToBitmapCache(timeLineCardEntity22.getLong_thumbnail_url());
//                        }
//                    }
//                }
//                if (callback != null) {
//                    IControllerCallback iControllerCallback = callback;
//                    String jSONArray = jsonArray.toString();
//                    if (timeLineCardEntityList.size() <= 0) {
//                        z = true;
//                    }
//                    iControllerCallback.success(timeLineCardEntityList, jSONArray, cursorId, z);
//                }
//            }
//
//            public void onFailure(VolleyError error) {
//                super.onFailure(error);
//                if (callback != null) {
//                    callback.failure(error);
//                }
//            }
//
//            public void error(ErrorBean error) {
//                super.error(error);
//                if (callback != null) {
//                    callback.error(error);
//                }
//            }
//        });
    }

    public static void searchOfficialTagsNewPhotos(final Activity activity, String value, String type, int page, final IControllerCallback<List<TimeLineCardEntity>> callback) {
//        MSCVRequestManager.searchOfficialTagsPhoto(value, "new", type, page, new RequestCallback<JSONArray>() {
//            public void onSuccess(JSONArray jsonArray) {
//                TimelineBean timelineBean;
//                boolean z = false;
//                LogUtil.d(MSCVController.TAG, "searchOfficialTagsNewPhotos jsonArray : " + jsonArray.toString());
//                Collection<TimelineBean> timelineBeanList = TimelineBean.parseList(jsonArray.toString(), MSCVController$15$$Lambda$1.lambdaFactory$(callback));
//                if (timelineBeanList != null && timelineBeanList.size() > 0) {
//                    for (int i = timelineBeanList.size() - 1; i >= 0; i--) {
//                        timelineBean = (TimelineBean) timelineBeanList.get(i);
//                        if (timelineBean.width == 0 || timelineBean.height == 0) {
//                            timelineBeanList.remove(i);
//                        }
//                    }
//                }
//                List<TimeLineCardEntity> timeLineCardEntityList = new ArrayList();
//                long cursorId = -1;
//                if (TextUtil.isValidate((Collection) timelineBeanList)) {
//                    for (TimelineBean timelineBean2 : timelineBeanList) {
//                        Object timeLineCardEntity = TimelineController.getTimelineCardEntity(activity, timelineBean2, false);
//                        if (TextUtil.isValidate(timeLineCardEntity)) {
//                            timeLineCardEntityList.add(timeLineCardEntity);
//                        }
//                        cursorId = timelineBean2.published_at;
//                    }
//                    for (TimeLineCardEntity timeLineCardEntity2 : timeLineCardEntityList) {
//                        PreDownloadUtil.prefetchMainToBitmapCache(timeLineCardEntity2.getAvatarUrl());
//                    }
//                    for (TimeLineCardEntity timeLineCardEntity22 : timeLineCardEntityList) {
//                        if (TextUtil.isValidate(timeLineCardEntity22.getLong_thumbnail_url())) {
//                            PreDownloadUtil.prefetchMainToBitmapCache(timeLineCardEntity22.getLong_thumbnail_url());
//                        }
//                    }
//                }
//                if (callback != null) {
//                    IControllerCallback iControllerCallback = callback;
//                    String jSONArray = jsonArray.toString();
//                    if (cursorId == -1) {
//                        z = true;
//                    }
//                    iControllerCallback.success(timeLineCardEntityList, jSONArray, cursorId, z);
//                }
//            }
//
//            private /* synthetic */ void lambda$onSuccess$0(IControllerCallback callback) {
//                if (callback != null) {
//                    callback.failure(new VolleyError());
//                }
//            }
//
//            public void onFailure(VolleyError error) {
//                super.onFailure(error);
//                if (callback != null) {
//                    callback.failure(error);
//                }
//            }
//
//            public void error(ErrorBean error) {
//                super.error(error);
//                if (callback != null) {
//                    callback.error(error);
//                }
//            }
//        });
    }

    public static void searchOfficialTagsNew3Photos(final Activity activity, String value, String type, final IControllerCallback<List<TimeLineCardEntity>> callback) {
//        MSCVRequestManager.searchOfficialTagsPhoto(value, "new3", type, 0, new RequestCallback<JSONArray>() {
//            public void onSuccess(JSONArray jsonArray) {
//                TimelineBean timelineBean;
//                LogUtil.d(MSCVController.TAG, "searchOfficialTagsNew3Photos jsonArray : " + jsonArray.toString());
//                Collection<TimelineBean> timelineBeanList = JsonParserUtil.parseList(jsonArray.toString(), new TypeToken<ArrayList<TimelineBean>>() {
//                }.getType(), new IExceptionCallback() {
//                    public void doException() {
//                    }
//                });
//                if (timelineBeanList != null && timelineBeanList.size() > 0) {
//                    for (int i = timelineBeanList.size() - 1; i >= 0; i--) {
//                        timelineBean = (TimelineBean) timelineBeanList.get(i);
//                        if (timelineBean.width == 0 || timelineBean.height == 0) {
//                            timelineBeanList.remove(i);
//                        }
//                    }
//                }
//                List<TimeLineCardEntity> timeLineCardEntityList = new ArrayList();
//                if (TextUtil.isValidate((Collection) timelineBeanList)) {
//                    for (TimelineBean timelineBean2 : timelineBeanList) {
//                        Object timeLineCardEntity = TimelineController.getTimelineCardEntity(activity, timelineBean2, false);
//                        if (TextUtil.isValidate(timeLineCardEntity)) {
//                            timeLineCardEntityList.add(timeLineCardEntity);
//                        }
//                    }
//                    for (TimeLineCardEntity timeLineCardEntity2 : timeLineCardEntityList) {
//                        PreDownloadUtil.prefetchMainToBitmapCache(timeLineCardEntity2.getAvatarUrl());
//                    }
//                }
//                if (callback != null) {
//                    callback.success(timeLineCardEntityList, jsonArray.toString(), 0, true);
//                }
//            }
//
//            public void onFailure(VolleyError error) {
//                super.onFailure(error);
//                if (callback != null) {
//                    callback.failure(error);
//                }
//            }
//
//            public void error(ErrorBean error) {
//                super.error(error);
//                if (callback != null) {
//                    callback.error(error);
//                }
//            }
//        });
    }

    public static void getNewTagTopic(String value, String type, String zh, String en, final IControllerCallback<NewTagTopicBean> callback) {
        MSCVRequestManager.getNewTagTopic(value, type, zh, en, new RequestCallback<JSONObject>() {
            public void onSuccess(JSONObject object) {
                NewTagTopicBean bean = NewTagTopicBean.parse(object.toString(), new IExceptionCallback() {
                    public void doException() {
                    }
                });
                if (bean != null) {
                    if (callback != null) {
                        callback.success(bean, object.toString(), 0, true);
                    }
                } else if (callback != null) {
                    callback.failure(new VolleyError());
                }
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }

            public void error(ErrorBean error) {
                super.error(error);
                if (callback != null) {
                    callback.error(error);
                }
            }
        });
    }

    public static void getPromoteTags(final IControllerCallback<List<OfficialTagBean>> callback) {
        MSCVRequestManager.getPromoteTags(new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                String jsonContent = jsonArray.toString();
                List<OfficialTagBean> officialTagBeanList = JsonParserUtil.parseList(jsonContent, new TypeToken<ArrayList<OfficialTagBean>>() {
                }.getType(), null);
                if (officialTagBeanList != null) {
                    if (callback != null) {
                        callback.success(officialTagBeanList, jsonContent, 0, true);
                    }
                } else if (callback != null) {
                    callback.failure(new VolleyError());
                }
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }

            public void error(ErrorBean error) {
                super.error(error);
                if (callback != null) {
                    callback.error(error);
                }
            }
        });
    }

    private static String getRecommendCategoriesParams(int vType) {
        HashMap<String, Object> hashMap = new HashMap();
        //if (App.isLogin()) {
            hashMap.put("screen_name", GlobalHelper.getUserScreenName());
            hashMap.put("access_token", GlobalHelper.getUserAccessToken());
        //}
        hashMap.put("vtype", Integer.valueOf(vType));
        return new JSONObject(hashMap).toString();
    }

    public static void getRecommendCategories(int vType, final IControllerCallback<List<OfficialTagBean>> callback) {
        MSCVRequestManager.getRecommendCategories(getRecommendCategoriesParams(vType), new RequestCallback<JSONArray>() {
            public void onSuccess(JSONArray jsonArray) {
                String jsonContent = jsonArray.toString();
                LogUtil.d(MSCVController.TAG, String.format("getRecommendCategories jsonContent : %s ", new Object[]{jsonContent}));
                List<OfficialTagBean> officialTagBeanList = JsonParserUtil.parseList(jsonContent, new TypeToken<ArrayList<OfficialTagBean>>() {
                }.getType(), null);
                if (officialTagBeanList != null) {
                    if (callback != null) {
                        callback.success(officialTagBeanList, jsonContent, 0, true);
                    }
                } else if (callback != null) {
                    callback.failure(new VolleyError());
                }
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }

            public void error(ErrorBean error) {
                super.error(error);
                if (callback != null) {
                    callback.error(error);
                }
            }
        });
    }
}