package cn.nineton.glsurfacedemo;

import android.app.Application;

import cn.nineton.glsurfacedemo.util.AppProfile;

/**
 * @Author: savion
 * @Date: 2019/3/13 16:23
 * @Des:
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppProfile.setContext(this);
    }
}
