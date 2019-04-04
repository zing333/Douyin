package cn.nineton.onetake.http;

import com.android.volley.VolleyError;

import cn.nineton.onetake.bean.ErrorBean;

public abstract class RequestCallback<T> {
    public abstract void onSuccess(T t);

    public void error(ErrorBean error) {
    }

    public void onFailure(VolleyError error) {
    }

    public void checkIfCanceled() {
    }

    public void cancel() {
    }
}