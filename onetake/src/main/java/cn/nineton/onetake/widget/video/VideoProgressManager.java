package cn.nineton.onetake.widget.video;

import java.util.HashMap;
import java.util.Map;

import cn.nineton.onetake.util.TextUtil;

public class VideoProgressManager {
    private static VideoProgressManager instance;
    private Map<String, Integer> mMap;

    private VideoProgressManager() {
    }

    public static VideoProgressManager getInstance() {
        if (instance == null) {
            synchronized (VideoProgressManager.class) {
                instance = new VideoProgressManager();
                instance.init();
            }
        }
        return instance;
    }

    private void init() {
        if (TextUtil.isValidate(this.mMap)) {
            this.mMap.clear();
        }
        this.mMap = new HashMap();
    }

    public void saveProgress(String url, int pos) {
        if (!TextUtil.isValidate(this.mMap)) {
            init();
        }
        this.mMap.put(url, Integer.valueOf(pos));
    }

    public int getProgress(String url) {
        if (TextUtil.isValidate(this.mMap) && this.mMap.containsKey(url)) {
            return ((Integer) this.mMap.get(url)).intValue();
        }
        return 0;
    }
}