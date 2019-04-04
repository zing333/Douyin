package cn.nineton.onetake.util;

import android.support.annotation.Nullable;
import com.android.volley.VolleyError;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.audio.AudioDetailBean;
//import com.blink.academy.onetake.bean.audio.AudioDownloadInfoBean;
//import com.blink.academy.onetake.bean.audio.AudioIndexBean;
//import com.blink.academy.onetake.bean.audio.AudioPurchaseBean;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.bean.error.ErrorBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.http.request.AudioStoreRequestManager;
//import com.blink.academy.onetake.http.request.RequestCallback;
//import com.blink.academy.onetake.model.AudioDownloadModel;
//import com.blink.academy.onetake.support.callbacks.IControllerCallback;
//import com.blink.academy.onetake.support.database.task.AudioTrackTask;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.audio.AudiosDownloadEvent;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videocache.file.Md5FileNameGenerator;
//import com.google.gson.reflect.TypeToken;
//import com.lzy.okhttputils.OkHttpUtils;
//import com.lzy.okhttputils.cache.CacheMode;
//import com.lzy.okhttputils.callback.FileCallback;
//import com.lzy.okhttputils.request.BaseRequest;
//import com.lzy.okhttputils.request.GetRequest;

import cn.nineton.onetake.bean.AudioDetailBean;
import cn.nineton.onetake.bean.AudioIndexBean;
import cn.nineton.onetake.bean.AudioPurchaseBean;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.ErrorBean;
import cn.nineton.onetake.http.AudioStoreRequestManager;
import cn.nineton.onetake.http.RequestCallback;
import cn.nineton.onetake.listener.IControllerCallback;
//import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class AudioStoreController {
    private static final String TAG = AudioStoreController.class.getSimpleName();

    public static void getAudioStoreIndex(final IControllerCallback<AudioIndexBean> callback) {
        AudioStoreRequestManager.getAudioStoreIndex(new RequestCallback<JSONObject>() {
            public void onSuccess(JSONObject object) {
                AudioIndexBean audioGenreBean = AudioIndexBean.objectFromData(object.toString());
                if (audioGenreBean != null) {
                    if (callback != null) {
                        callback.success(audioGenreBean, object.toString(), 0, true);
                    }
                } else if (callback != null) {
                    callback.failure(new VolleyError());
                }
            }

            public void error(ErrorBean error) {
                super.error(error);
                if (callback != null) {
                    callback.error(error);
                }
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }
        });
    }

    public static void getAudioDetail(int id, int page, final IControllerCallback<AudioDetailBean> callback) {
        AudioStoreRequestManager.getAudioStoreDetail(id, page, new RequestCallback<JSONObject>() {
            public void onSuccess(JSONObject object) {
                AudioDetailBean audioDetailBean = AudioDetailBean.objectFromData(object.toString());
                if (audioDetailBean != null) {
                    if (callback != null) {
                        callback.success(audioDetailBean, object.toString(), 0, true);
                    }
                } else if (callback != null) {
                    callback.failure(new VolleyError());
                }
            }

            public void error(ErrorBean error) {
                super.error(error);
                if (callback != null) {
                    callback.error(error);
                }
            }

            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }
        });
    }

    public static void purchaseAudio(int item_id, final IControllerCallback<AudioPurchaseBean> callback) {
        AudioStoreRequestManager.postPurchaseAudio(getPurchaseAudioParams(item_id), new RequestCallback<JSONObject>() {
            public void onSuccess(JSONObject object) {
                AudioPurchaseBean bean = AudioPurchaseBean.objectFromData(object.toString());
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

    private static String getPurchaseAudioParams(int item_id) {
        HashMap<String, Object> map = new HashMap();
        map.put("item_id", Integer.valueOf(item_id));
        return new JSONObject(map).toString();
    }

    private static String getAudioFileName(String url) {
        if (TextUtil.isNull(url)) {
            return "";
        }
        return Md5FileNameGenerator.getAudioFileName(url);
    }

    public static void downloadAudio(AudioDetailBean audioDetailBean, AudioTrackBean bean) {
//        if (bean != null) {
//            final int id = bean.getId();
//            AudioDownloadModel.getInstance().setDownloadState(id, 1);
//            EventBus.getDefault().post(new AudiosDownloadEvent(id, 0));
//            String full_url = bean.getFull_url();
//            if (TextUtil.isNull(full_url)) {
//                dealDownloadFailed(id);
//                return;
//            }
//            String audioFileName = getAudioFileName(bean);
//            LogUtil.d(TAG, String.format("audioFileName : %s , url : %s ", new Object[]{audioFileName, full_url}));
//            final AudioDetailBean audioDetailBean2 = audioDetailBean;
//            final AudioTrackBean audioTrackBean = bean;
//            ((GetRequest) OkHttpUtils.get(full_url).cacheMode(CacheMode.NO_CACHE)).execute(new FileCallback(Config.getAudiosDownloadPath(), audioFileName) {
//                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                    LogUtil.d(AudioStoreController.TAG, "downloadProgress:" + String.format("currentSize : %s, totalSize : %s, progress : %s, networkSpeed : %s ", new Object[]{Long.valueOf(currentSize), Long.valueOf(totalSize), Float.valueOf(progress), Long.valueOf(networkSpeed)}));
//                    super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
//                    int curProgress = (int) (100.0f * progress);
//                    if (curProgress >= 90) {
//                        curProgress = 90;
//                    }
//                    AudioDownloadModel.getInstance().setDownloadState(id, 2);
//                    AudioDownloadModel.getInstance().setDownloadProgress(id, curProgress);
//                    EventBus.getDefault().post(new AudiosDownloadEvent(id, curProgress));
//                }
//
//                public File parseNetworkResponse(Response response) throws Exception {
//                    LogUtil.d(AudioStoreController.TAG, "parseNetworkResponse:");
//                    return super.parseNetworkResponse(response);
//                }
//
//                public void onBefore(BaseRequest request) {
//                    super.onBefore(request);
//                    LogUtil.d(AudioStoreController.TAG, "onBefore:");
//                }
//
//                public void parseNetworkFail(Call call, IOException e) {
//                    super.parseNetworkFail(call, e);
//                    LogUtil.d(AudioStoreController.TAG, "parseNetworkFail:");
//                }
//
//                public void onCacheSuccess(File file, Call call) {
//                    super.onCacheSuccess(file, call);
//                    LogUtil.d(AudioStoreController.TAG, "onCacheSuccess:");
//                }
//
//                public void onCacheError(Call call, Exception e) {
//                    super.onCacheError(call, e);
//                    LogUtil.d(AudioStoreController.TAG, "onCacheError:");
//                }
//
//                public void onAfter(@Nullable File file, @Nullable Exception e) {
//                    super.onAfter(file, e);
//                    LogUtil.d(AudioStoreController.TAG, "onAfter:  file : " + (file == null ? "" : file.getAbsolutePath()));
//                }
//
//                public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                    super.upProgress(currentSize, totalSize, progress, networkSpeed);
//                    LogUtil.d(AudioStoreController.TAG, "upProgress: " + String.format("currentSize : %s, totalSize : %s, progress : %s, networkSpeed : %s ", new Object[]{Long.valueOf(currentSize), Long.valueOf(totalSize), Float.valueOf(progress), Long.valueOf(networkSpeed)}));
//                }
//
//                public void onError(Call call, Response response, Exception e) {
//                    super.onError(call, response, e);
//                    LogUtil.d(AudioStoreController.TAG, "onError:");
//                    AudioStoreController.dealDownloadFailed(id);
//                }
//
//                public void onSuccess(File file, Call call, Response response) {
//                    LogUtil.d(AudioStoreController.TAG, "onSuccess: fileName : " + (file != null ? file.getName() : "") + " file : " + (file != null ? file.getAbsolutePath() : ""));
//                    if (file == null || !file.exists()) {
//                        AudioStoreController.dealDownloadFailed(id);
//                        LogUtil.d(AudioStoreController.TAG, "unZip fail:  file == null || !file.exists: ");
//                        return;
//                    }
//                    AudioDownloadModel.getInstance().setDownloadState(id, 3);
//                    AudioDownloadModel.getInstance().addDownloadedFileName(file.getName());
//                    AudioTrackTask.addAudioTrack(audioDetailBean2, audioTrackBean);
//                    EventBus.getDefault().post(new AudiosDownloadEvent(id, 100));
//                }
//            });
//        }
    }

//    public static void getAlbumInfo(String ids, final IControllerCallback<List<AudioDownloadInfoBean>> callback) {
//        AudioStoreRequestManager.getAudioInfoByTrackIds(ids, new RequestCallback<JSONArray>() {
//            public void onSuccess(JSONArray jsonArray) {
//                LogUtil.d("slim", "jsonArray:" + jsonArray.toString());
//                List<AudioDownloadInfoBean> audioDownloadInfoBeans = JsonParserUtil.parseList(jsonArray.toString(), new TypeToken<List<AudioDownloadInfoBean>>() {
//                }.getType(), AudioStoreController$5$$Lambda$1.lambdaFactory$(callback));
//                if (callback != null) {
//                    callback.success(audioDownloadInfoBeans, jsonArray.toString(), -1, true);
//                }
//            }
//
//            private static /* synthetic */ void lambda$onSuccess$0(IControllerCallback callback) {
//                if (callback != null) {
//                    callback.failure(new VolleyError());
//                }
//            }
//
//            public void onFailure(VolleyError error) {
//                super.onFailure(error);
//                LogUtil.d("slim", "error:" + error.getMessage());
//            }
//        });
//    }

//    private static void dealDownloadFailed(int id) {
//        AudioDownloadModel.getInstance().setDownloadState(id, 0);
//        EventBus.getDefault().post(new AudiosDownloadEvent(id, -1));
//    }

    public static String getAudioFileName(AudioTrackBean bean) {
        if (bean == null) {
            return "";
        }
        if (bean.isLocal()) {
            return bean.getFull_url();
        }
        return String.valueOf(bean.getId());
    }
}