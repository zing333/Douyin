package cn.nineton.onetake.util;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import java.net.URL;

public class CacheKeyGlideUrl extends GlideUrl {
    private boolean isSplitSecondLast;

    public CacheKeyGlideUrl(URL url) {
        super(url);
    }

    public CacheKeyGlideUrl(String url) {
        super(url);
        this.isSplitSecondLast = false;
    }

    public CacheKeyGlideUrl(String url, boolean b) {
        super(url);
        this.isSplitSecondLast = b;
    }

    public CacheKeyGlideUrl(URL url, Headers headers) {
        super(url, headers);
    }

    public CacheKeyGlideUrl(String url, Headers headers) {
        super(url, headers);
    }

    public String getCacheKey() {
        String[] keyString = toStringUrl().split("/");
        if (this.isSplitSecondLast) {
            if (keyString.length > 1) {
                return keyString[keyString.length - 2];
            }
        } else if (keyString.length > 0) {
            return keyString[keyString.length - 1];
        }
        return super.getCacheKey();
    }
}