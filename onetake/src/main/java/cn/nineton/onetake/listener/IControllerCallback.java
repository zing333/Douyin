package cn.nineton.onetake.listener;

import com.android.volley.VolleyError;
//import com.blink.academy.onetake.bean.error.ErrorBean;
//import com.blink.academy.onetake.bean.user.UserBean;

import cn.nineton.onetake.bean.ErrorBean;
import cn.nineton.onetake.bean.UserBean;

public abstract class IControllerCallback<T> {
    public void successWithPush(T t, boolean result, boolean all) {
    }

    public void successWithPush(T t, boolean result, String type) {
    }

    public void success(T t, String jsonContent, long cursorId, boolean isAllDone) {
    }

    public void success(T t, String jsonContent, long cursorId, boolean isAllDown, int contentType) {
    }

    public void success(T t, T t2, UserBean userBean, String jsonContent, long cursorId, boolean isAllDone) {
    }

    public void error(ErrorBean error) {
    }

    public void failure(VolleyError error) {
    }
}