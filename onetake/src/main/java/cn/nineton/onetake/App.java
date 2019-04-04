package cn.nineton.onetake;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nineton.onetake.bean.FilterInfo;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.MIUIUtil;
import cn.nineton.onetake.util.WorkGroup;

/**
 * @Author: savion
 * @Date: 2019/3/14 16:43
 * @Des:
 **/
public class App extends MultiDexApplication {
    static Context mApplicationContext;
    public static final boolean Debug = true;
    static Resources mResources;
    private static Typeface RegularTypeface = null;
    private static Typeface MediumTypeface = null;
    private static Typeface AvenirNextCondensedRegularTypeFace = null;
    private static Typeface AvenirNextCondensedMediumTypeFace = null;
    private static Typeface AvenirNextUltralightTypeFace = null;
    private static Typeface AvenirNextCondensedDemiBoldTypeFace = null;
    private static Typeface foundersGroteskXCondensedappLightTypeFace = null;
    private static Typeface FoundersGroteskXCondensedappMediumTypeFace = null;
    public static HashMap<String, FilterInfo> mFilterMap;
    public static ArrayList<FilterInfo> mFilterInfos;
    public static List<FilterInfo> jsonGroupInfoList2;
    public static int mSystemUiVisibility=-1;
    public static Handler sHandler = new Handler();
    private static boolean shouldBreakVideo = false;
    public static WorkGroup encoders = new WorkGroup();
    private static boolean haveDraftData;
    public static boolean isHaveDraftData() {
        return haveDraftData;
    }

    public static void setHaveDraftData(boolean haveDraftData) {
        haveDraftData = haveDraftData;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
        mResources = getResources();
        RegularTypeface = FontsUtil.setARegularTypeFace(mApplicationContext);
        MediumTypeface = FontsUtil.setAMediumTypeFace(mApplicationContext);
        mFilterInfos = new ArrayList<>();
        mFilterMap = new HashMap<>();
        jsonGroupInfoList2 = new ArrayList<>();
    }

    public static Context getContext() {
        return mApplicationContext;
    }

    public static Resources getResource() {
        return mResources;
    }

    public static Typeface getRegularTypeface() {
        return RegularTypeface;
    }

    public static Typeface getMediumTypeface() {
        return MediumTypeface;
    }

    public static Typeface getAveNextCondensedRegularTypeFace() {
        if (AvenirNextCondensedRegularTypeFace == null) {
            AvenirNextCondensedRegularTypeFace = FontsUtil.setAveNextCondensedRegularTypeFace(getContext());
        }
        return AvenirNextCondensedRegularTypeFace;
    }

    public static Typeface getAvenirNextCondensedMediumTypeFace() {
        if (AvenirNextCondensedMediumTypeFace == null) {
            AvenirNextCondensedMediumTypeFace = FontsUtil.setAvenirNextCondensedMediumTypeFace(getContext());
        }
        return AvenirNextCondensedMediumTypeFace;
    }

    public static Typeface getAvenirNextUltralightTypeFace() {
        if (AvenirNextUltralightTypeFace == null) {
            AvenirNextUltralightTypeFace = FontsUtil.setAvenirNextUltralightTypeFace(getContext());
        }
        return AvenirNextUltralightTypeFace;
    }

    public static Typeface getAvenirNextCondensedDemiBoldTypeFace() {
        if (AvenirNextCondensedDemiBoldTypeFace == null) {
            AvenirNextCondensedDemiBoldTypeFace = FontsUtil.setAvenirNextCondensedDemiBoldTypeFace(getContext());
        }
        return AvenirNextCondensedDemiBoldTypeFace;
    }

    public static Typeface getFoundersGroteskXCondensedappLightTypeFace() {
        if (foundersGroteskXCondensedappLightTypeFace == null) {
            foundersGroteskXCondensedappLightTypeFace = FontsUtil.setFoundersGroteskXCondensedappLightTypeFace(getContext());
        }
        return foundersGroteskXCondensedappLightTypeFace;
    }

    public static Typeface getFoundersGroteskXCondensedappMediumTypeFace() {
        if (FoundersGroteskXCondensedappMediumTypeFace == null) {
            FoundersGroteskXCondensedappMediumTypeFace = FontsUtil.setFoundersGroteskXCondensedappMediumTypeFace(getContext());
        }
        return FoundersGroteskXCondensedappMediumTypeFace;
    }


    public static int getHardCodeWidth(boolean isGiphy) {
        return (int) (((float) (DensityUtil.getMetricsWidth(getContext()) - DensityUtil.dip2px(45.0f))) - (isGiphy ? getResource().getDimension(R.dimen.leftSpacePadding) : 0.0f));
    }

    public static void warnException(Exception e) {
        Log.w("debug", "Exception", e);
    }
    public static void fatalException(Exception e) {
        Log.e("Onetake", "Exception", e);
        throw new RuntimeException(e);
    }

    public static void UnregisterEventBus(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void RegisterEventBus(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }
    public static void runOnUiThread(Runnable r) {
        sHandler.post(r);
    }
    public static void runOnUiThreadDelay(Runnable r, long delaytimes) {
        sHandler.postDelayed(r, delaytimes);
    }

    public static boolean shouldAbortEncoding() {
        if (shouldBreakVideo) {
            return true;
        }
        return false;
    }


    public static boolean isMIUI() {
        return MIUIUtil.isMIUI();
    }

    public static void onActivityPausePenaltyArea(Activity activity) {
        onActivityPause(activity);
    }

    public static void onActivityPause(Activity activity) {
    }
    public static void onActivityResumePenaltyArea(Activity activity) {
        onActivityResume(activity, false);
    }

    public static void onActivityResume(Activity activity) {
        onActivityResume(activity, true);
    }
    public static void onActivityResume(Activity activity, boolean needGo) {
    }


}
