package cn.nineton.onetake.util;

import android.app.Activity;

public class ToastMessageEvent {
    public boolean isSuccess;
    public Activity mActivity;
    public int witchOne;

    public ToastMessageEvent(Activity activity, boolean isSuccess, int witchOne) {
        this.mActivity = activity;
        this.isSuccess = isSuccess;
        this.witchOne = witchOne;
    }
}