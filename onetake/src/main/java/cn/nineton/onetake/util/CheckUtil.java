package cn.nineton.onetake.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageFilterGroup;

public class CheckUtil {
    public static boolean isNetworkConnected() {
        NetworkInfo networkInfo = getConnectivityManager().getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isWifiConnected() {
        NetworkInfo wiFiNetworkInfo = getConnectivityManager().getNetworkInfo(1);
        if (wiFiNetworkInfo == null || !wiFiNetworkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isFlyme() {
        if (Build.BRAND.contains("Meizu")) {
            return true;
        }
        return false;
    }

    public static boolean memoryIsGreaterThan2G() {
        long t1 = System.currentTimeMillis();
        long initial_memory = 0;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);//Utility.DEFAULT_STREAM_BUFFER_SIZE);
            String str2 = localBufferedReader.readLine();
            String[] arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = (long) ((((float) Integer.valueOf(arrayOfString[1]).intValue()) * 1.0f) / 1024.0f);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        if (initial_memory >= Constants.VIDEO_BLACK_DURATION) {
            return true;
        }
        return false;
    }

    public static int getSupportMaxPictureSize() {
        int[] array = new int[1];
        GLES10.glGetIntegerv(3379, array, 0);
        if (array[0] == 0) {
            GLES11.glGetIntegerv(3379, array, 0);
            if (array[0] == 0) {
                GLES20.glGetIntegerv(3379, array, 0);
                if (array[0] == 0) {
                    GLES30.glGetIntegerv(3379, array, 0);
                }
            }
        }
        return array[0] != 0 ? array[0] : 2048;
    }

    public static boolean isNowSizeMoreThanSupportSize(int nowSize) {
        return nowSize > getSupportMaxPictureSize();
    }

    public static boolean checkTwoFilterIsSame(GPUImageFilter filter1, GPUImageFilter filter2) {
        if (filter1 == null) {
            return false;
        }
        if (filter1 == filter2) {
            return true;
        }
        if (!(filter1 instanceof GPUImageFilterGroup) || !(filter2 instanceof GPUImageFilterGroup)) {
            return false;
        }
        GPUImageFilterGroup group2 = (GPUImageFilterGroup) filter2;
        List<GPUImageFilter> list1 = ((GPUImageFilterGroup) filter1).mFilters;
        List<GPUImageFilter> list2 = group2.mFilters;
        if (list1.size() != list2.size()) {
            return false;
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            if (!checkTwoFilterIsSame((GPUImageFilter) list1.get(i), (GPUImageFilter) list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static int[] redefineWidthHeight(int width, int height) {
        int[] widthHeight = new int[2];
        int finalWidth = width;
        int finalHeight = height;
        widthHeight[0] = finalWidth;
        widthHeight[1] = finalHeight;
        double x;
        if (memoryIsGreaterThan2G()) {
            if (finalWidth * finalHeight > 2000000) {
                x = Math.sqrt((double) ((2000000.0f / ((float) finalWidth)) / ((float) finalHeight)));
                finalWidth = (int) (((double) finalWidth) * x);
                finalHeight = (int) (((double) finalHeight) * x);
            }
        } else if (finalWidth * finalHeight > 1500000) {
            x = Math.sqrt((double) ((1500000.0f / ((float) finalWidth)) / ((float) height)));
            finalWidth = (int) (((double) finalWidth) * x);
            finalHeight = (int) (((double) finalHeight) * x);
        }
        widthHeight[0] = finalWidth;
        widthHeight[1] = finalHeight;
        return widthHeight;
    }

    public static float[] bubbleSort(float[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            for (int j = 0; j < (values.length - 1) - i; j++) {
                if (values[j] > values[j + 1]) {
                    float temp = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = temp;
                }
            }
        }
        return values;
    }

    public static String bubbleSortString(String[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            for (int j = 0; j < (values.length - 1) - i; j++) {
                if (values[j].length() > values[j + 1].length()) {
                    String temp = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = temp;
                }
            }
        }
        return values[values.length - 1];
    }

    public static boolean hasNetwork(Context context) {
        if (VERSION.SDK_INT < 21) {
            return NetworkUtils.isNetworkAvailable();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networkArr = new Network[0];
        for (Network allNetwork : cm.getAllNetworks()) {
            NetworkInfo networkInfo = cm.getNetworkInfo(allNetwork);
            if (networkInfo == null) {
                return false;
            }
            int type = networkInfo.getType();
            if ((type == 1 || type == 0) && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }
}