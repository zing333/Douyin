package cn.nineton.onetake.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.error.ErrorBean;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.http.VolleyUtil;
//import com.blink.academy.onetake.support.thread.PriorityRunnable;
//import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.qiniu.android.common.Constants;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.ErrorBean;
import cn.nineton.onetake.bean.IExceptionCallback;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.PriorityRunnable;
import cn.nineton.onetake.util.PriorityThreadPoolManager;
import cn.nineton.onetake.util.TextUtil;

public class RequestManager implements IRequestManager {
    private static final int CUR_DEFAULT_TIME_OUT_MS = 15000;
    private RequestQueue mRequestQueue;

    RequestManager() {
        initRequestQueue();
    }

    private void initRequestQueue() {
        this.mRequestQueue = Volley.newRequestQueue(App.getContext());
        this.mRequestQueue.getCache().clear();
    }

    private void addRequestQueue(Request request) {
        if (this.mRequestQueue == null) {
            initRequestQueue();
        }
        this.mRequestQueue.add(request);
    }

    public void cancel(String tag) {
        if (tag != null) {
            this.mRequestQueue.cancelAll(tag);
        }
    }

    public void sendGetObjectRequest(String url, RequestCallback<JSONObject> callback) {
        RequestLog(url, "");
        requestObject(0, url, null, callback);
    }

    public void sendGetObjectRequestNoTimeout(String url, RequestCallback<JSONObject> callback) {
        RequestLog(url, "");
        requestObjectLargeTimeOut(0, url, null, callback);
    }

    public void sendGetArrayRequest(String url, RequestCallback<JSONArray> callback) {
        RequestLog(url, "");
        requestArray(0, url, null, callback);
    }

    public void sendGetArrayRequestNoTimeout(String url, RequestCallback<JSONArray> callback) {
        RequestLog(url, "");
        requestArrayNoTimeout(0, url, null, callback);
    }

    public void sendPostObjectRequest(String url, String params, RequestCallback<JSONObject> callback) {
        RequestLog(url, params != null ? params : "");
        requestObject(1, url, params, callback);
    }

    public void sendPostArrayRequest(String url, String params, RequestCallback<JSONArray> callback) {
        RequestLog(url, params != null ? params : "");
        requestArray(1, url, params, callback);
    }

    public void sendPostArrayRequestNoCache(String url, String params, RequestCallback<JSONArray> callback) {
        RequestLog(url, params != null ? params : "");
        requestArrayNoCache(1, url, params, callback);
    }

    public void sendPutObjectRequest(String url, String params, RequestCallback<JSONObject> callback) {
        RequestLog(url, params != null ? params : "");
        requestObject(2, url, params, callback);
    }

    public void sendPutArrayRequest(String url, String params, RequestCallback<JSONArray> callback) {
        RequestLog(url, params != null ? params : "");
        requestArray(2, url, params, callback);
    }

    public void sendDeleteObjectRequest(String url, String params, RequestCallback<JSONObject> callback) {
        RequestLog(url, params != null ? params : "");
        requestObject(3, url, params, callback);
    }

    public void sendDeleteArrayRequest(String url, String params, RequestCallback<JSONArray> callback) {
        RequestLog(url, params != null ? params : "");
        requestArray(3, url, params, callback);
    }

    public void sendGetNextPageRequest(String url, int cursor, boolean isNextPage, RequestCallback<JSONArray> callback) {
        RequestLog(url, "");
        requestArray(0, url, null, callback);
    }

    public void sendPutJsonRequest(String url, String params, RequestCallback<String> callback) {
        RequestLog(url, "");
        requestJson(2, url, params, callback);
    }

    public void sendPostJsonRequest(String url, String params, RequestCallback<String> callback) {
        RequestLog(url, "");
        requestJson(1, url, params, callback);
    }

    private void requestObject(int method, String url, String params, RequestCallback<JSONObject> callback) {
        final int i = method;
        final String str = url;
        final String str2 = params;
        final RequestCallback<JSONObject> requestCallback = callback;
//        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//            public void run() {
//                JsonObjectRequest request = new JsonObjectRequest(i, str, str2, new Listener<JSONObject>() {
//                    public void onResponse(final JSONObject response) {
//                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//                            public void run() {
//                                requestCallback.onSuccess(response);
//                            }
//                        });
//                    }
//                }, new ErrorListener() {
//                    public void onErrorResponse(final VolleyError error) {
//                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//                            public void run() {
//                                if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                                    ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                        public void doException() {
//                                            requestCallback.onFailure(new VolleyError());
//                                        }
//                                    });
//                                    if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                        requestCallback.error(errorBean);
//                                        return;
//                                    } else {
//                                        requestCallback.onFailure(error);
//                                        return;
//                                    }
//                                }
//                                requestCallback.onFailure(error);
//                            }
//                        });
//                    }
//                }) {
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        return VolleyUtil.getJsonHeader();
//                    }
//                };
//                request.setRetryPolicy(new DefaultRetryPolicy(RequestManager.CUR_DEFAULT_TIME_OUT_MS, 0, 1.0f));
//                RequestManager.this.addRequestQueue(request);
//            }
//        });
    }

    private void requestObjectLargeTimeOut(int method, String url, String params, final RequestCallback<JSONObject> callback) {
        checkCallback(callback);
//        JsonObjectRequest request = new JsonObjectRequest(method, url, params, new Listener<JSONObject>() {
//            public void onResponse(JSONObject response) {
//                RequestManager.this.dealObjectResponse(response, callback);
//            }
//        }, new ErrorListener() {
//            public void onErrorResponse(final VolleyError error) {
//                RequestManager.this.execInNetPriorityThread(new PriorityRunnable(6) {
//                    public void run() {
//                        if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                            ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                public void doException() {
//                                    callback.onFailure(new VolleyError());
//                                }
//                            });
//                            if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                callback.error(errorBean);
//                                return;
//                            } else {
//                                callback.onFailure(error);
//                                return;
//                            }
//                        }
//                        callback.onFailure(error);
//                    }
//                });
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtil.getJsonHeader();
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(80000, 0, 3.0f));
//        addRequestQueue(request);
    }

    private void requestArray(int method, String url, String params, RequestCallback<JSONArray> callback) {
        final int i = method;
        final String str = url;
        final String str2 = params;
        final RequestCallback<JSONArray> requestCallback = callback;
//        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//            public void run() {
//                JsonArrayRequest request = new JsonArrayRequest(i, str, str2, new Listener<JSONArray>() {
//                    public void onResponse(final JSONArray response) {
//                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//                            public void run() {
//                                requestCallback.onSuccess(response);
//                            }
//                        });
//                    }
//                }, new ErrorListener() {
//                    public void onErrorResponse(final VolleyError error) {
//                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
//                            public void run() {
//                                if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                                    ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                        public void doException() {
//                                            requestCallback.onFailure(new VolleyError());
//                                        }
//                                    });
//                                    if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                        requestCallback.error(errorBean);
//                                        return;
//                                    } else {
//                                        requestCallback.onFailure(error);
//                                        return;
//                                    }
//                                }
//                                requestCallback.onFailure(error);
//                            }
//                        });
//                    }
//                }) {
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        return VolleyUtil.getJsonHeader();
//                    }
//                };
//                request.setRetryPolicy(new DefaultRetryPolicy(RequestManager.CUR_DEFAULT_TIME_OUT_MS, 0, 1.0f));
//                RequestManager.this.addRequestQueue(request);
//            }
//        });
    }

    public void sendGetObjectRequestHasCancel(String url, String tag, RequestCallback<JSONObject> callback) {
        RequestLog(url, "");
        requestObjectHasCancel(0, url, null, tag, callback);
    }

    public void sendPostObjectRequestHasCancel(String url, String params, String tag, RequestCallback<JSONObject> callback) {
        RequestLog(url, "");
        requestObjectHasCancel(1, url, params, tag, callback);
    }

    private void execInNetPriorityThread(PriorityRunnable runnable) {
        runnable.setPriority(6);
        PriorityThreadPoolManager.execute(runnable);
    }

    private void requestObjectHasCancel(int method, String url, String params, String tag, final RequestCallback<JSONObject> callback) {
        checkCallback(callback);
//        JsonObjectRequest request = new JsonObjectRequest(method, url, params, new Listener<JSONObject>() {
//            public void onResponse(final JSONObject response) {
//                RequestManager.this.execInNetPriorityThread(new PriorityRunnable(6) {
//                    public void run() {
//                        callback.onSuccess(response);
//                    }
//                });
//            }
//        }, new ErrorListener() {
//            public void onErrorResponse(final VolleyError error) {
//                RequestManager.this.execInNetPriorityThread(new PriorityRunnable(6) {
//                    public void run() {
//                        if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                            ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                public void doException() {
//                                    callback.onFailure(new VolleyError());
//                                }
//                            });
//                            if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                callback.error(errorBean);
//                                return;
//                            } else {
//                                callback.onFailure(error);
//                                return;
//                            }
//                        }
//                        callback.onFailure(error);
//                    }
//                });
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtil.getJsonHeader();
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(CUR_DEFAULT_TIME_OUT_MS, 0, 1.0f));
//        if (TextUtil.isValidate(tag)) {
//            request.setTag(tag);
//        }
//        addRequestQueue(request);
    }

    private void requestArrayNoTimeout(int method, String url, String params, final RequestCallback<JSONArray> callback) {
        checkCallback(callback);
//        JsonArrayRequest request = new JsonArrayRequest(method, url, params, new Listener<JSONArray>() {
//            public void onResponse(JSONArray response) {
//                RequestManager.this.dealArrayResponse(response, callback);
//            }
//        }, new ErrorListener() {
//            public void onErrorResponse(final VolleyError error) {
//                RequestManager.this.execInNetPriorityThread(new PriorityRunnable() {
//                    public void run() {
//                        if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                            ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                public void doException() {
//                                    callback.onFailure(new VolleyError());
//                                }
//                            });
//                            if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                callback.error(errorBean);
//                                return;
//                            } else {
//                                callback.onFailure(error);
//                                return;
//                            }
//                        }
//                        callback.onFailure(error);
//                    }
//                });
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtil.getJsonHeader();
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, 1.0f));
//        addRequestQueue(request);
    }

    private void dealObjectResponse(final JSONObject response, final RequestCallback<JSONObject> callback) {
        execInNetPriorityThread(new PriorityRunnable(6) {
            public void run() {
                callback.onSuccess(response);
            }
        });
    }

    private void dealArrayResponse(final JSONArray response, final RequestCallback<JSONArray> callback) {
        execInNetPriorityThread(new PriorityRunnable(6) {
            public void run() {
                callback.onSuccess(response);
            }
        });
    }

    private <T> void checkCallback(RequestCallback<T> callback) {
        if (callback == null) {
            throw new RuntimeException("callBack why is null!");
        }
    }

    private void requestArrayNoCache(int method, String url, String params, final RequestCallback<JSONArray> callback) {
        checkCallback(callback);
//        JsonArrayRequest request = new JsonArrayRequest(method, url, params, new Listener<JSONArray>() {
//            public void onResponse(JSONArray response) {
//                RequestManager.this.dealArrayResponse(response, callback);
//            }
//        }, new ErrorListener() {
//            public void onErrorResponse(final VolleyError error) {
//                RequestManager.this.execInNetPriorityThread(new PriorityRunnable(6) {
//                    public void run() {
//                        if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
//                            ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
//                                public void doException() {
//                                    callback.onFailure(new VolleyError());
//                                }
//                            });
//                            if (TextUtil.isValidate(errorBean) && errorBean.error) {
//                                callback.error(errorBean);
//                                return;
//                            } else {
//                                callback.onFailure(error);
//                                return;
//                            }
//                        }
//                        callback.onFailure(error);
//                    }
//                });
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtil.getJsonHeader();
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(CUR_DEFAULT_TIME_OUT_MS, 0, 1.0f));
//        request.setShouldCache(false);
//        addRequestQueue(request);
    }

    private void requestJson(int method, String url, String params, RequestCallback<String> callback) {
        final int i = method;
        final String str = url;
        final String str2 = params;
        final RequestCallback<String> requestCallback = callback;
        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
            public void run() {
                JsonRequest<String> request = new JsonRequest<String>(i, str, str2, new Listener<String>() {
                    public void onResponse(final String response) {
                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
                            public void run() {
                                requestCallback.onSuccess(response);
                            }
                        });
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(final VolleyError error) {
                        PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
                            public void run() {
                                if (TextUtil.isValidate(error.networkResponse) && TextUtil.isValidate(error.networkResponse.data)) {
                                    ErrorBean errorBean = ErrorBean.parse(new String(error.networkResponse.data), new IExceptionCallback() {
                                        public void doException() {
                                            requestCallback.onFailure(new VolleyError());
                                        }
                                    });
                                    if (TextUtil.isValidate(errorBean) && errorBean.error) {
                                        requestCallback.error(errorBean);
                                        return;
                                    } else {
                                        requestCallback.onFailure(error);
                                        return;
                                    }
                                }
                                requestCallback.onFailure(error);
                            }
                        });
                    }
                }) {
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        try {
                            return Response.success(new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8")),HttpHeaderParser.parseCacheHeaders(response));//Constants.UTF_8)), HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        }
                    }
                };
                request.setRetryPolicy(new DefaultRetryPolicy(RequestManager.CUR_DEFAULT_TIME_OUT_MS, 0, 1.0f));
                RequestManager.this.addRequestQueue(request);
            }
        });
    }

    public void stopRequest() {
        this.mRequestQueue.stop();
    }

    private void RequestLog(String url, String params) {
        LogUtil.d("URL", "url:" + url + ", params:" + params);
    }
}