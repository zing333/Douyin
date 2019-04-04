package cn.nineton.onetake.util;

import android.content.Context;
import android.os.AsyncTask;
import java.io.File;

import cn.nineton.onetake.listener.LoadLocalFileCallback;

public class LoadLocalCatchFileTask extends AsyncTask<String, Void, File> {
    public static final String GLIDE_CACHE_DIR = "image_manager_disk_cache/";
    private Context context;
    private boolean isSplitSecondLast;
    private boolean isVideo;
    private final LoadLocalFileCallback mCallback;

    public LoadLocalCatchFileTask(Context context, LoadLocalFileCallback callback) {
        this(context, false, callback);
        this.isVideo = true;
    }

    public LoadLocalCatchFileTask(Context context, boolean b, LoadLocalFileCallback callback) {
        this.context = context;
        this.mCallback = callback;
        this.isSplitSecondLast = b;
    }

    protected File doInBackground(String... params) {
        if (this.isVideo) {
            return null;
        }
        String url = params[0];
        if (!TextUtil.isValidate(url)) {
            return new File("");
        }
        String[] keyString = url.split("/");
        String glideCacheFileName;
        if (this.isSplitSecondLast) {
            if (keyString.length <= 1) {
                return null;
            }
            glideCacheFileName = KeyUtils.getGlideCacheFileName(keyString[keyString.length - 2]);
            if (this.context == null) {
                return new File("");
            }
            return new File(this.context.getCacheDir().getPath() + "/" + GLIDE_CACHE_DIR + glideCacheFileName + ".0");
        } else if (keyString.length <= 0) {
            return null;
        } else {
            glideCacheFileName = KeyUtils.getGlideCacheFileName(keyString[keyString.length - 1]);
            if (this.context == null) {
                return new File("");
            }
            return new File(this.context.getCacheDir().getPath() + "/" + GLIDE_CACHE_DIR + glideCacheFileName + ".0");
        }
    }

    protected void onPostExecute(File result) {
        if (this.mCallback != null) {
            this.mCallback.localHasCatch(result);
        }
    }
}