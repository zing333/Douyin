package cn.nineton.onetake.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.nineton.onetake.App;

public class NetworkUtils {
    public static boolean isNetworkAvailable() {
        NetworkInfo activeNetwork = ((ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isAvailable();
    }
}