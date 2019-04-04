package cn.nineton.onetake.util;

import android.text.TextUtils;
import com.lidroid.xutils.cache.FileNameGenerator;

public class Md5FileNameGenerator implements FileNameGenerator {
    private static final int MAX_EXTENSION_LENGTH = 4;

    public String generate(String url) {
        String extension = getExtension(url);
        String name = ProxyCacheUtils.computeMD5(url);
        return TextUtils.isEmpty(extension) ? name : name + "." + extension;
    }

    public static String getExtension(String url) {
        int dotIndex = url.lastIndexOf(46);
        return (dotIndex == -1 || dotIndex <= url.lastIndexOf(47) || (dotIndex + 2) + 4 <= url.length()) ? "" : url.substring(dotIndex + 1, url.length());
    }

    public static String getAudioFileName(String url) {
        return url.substring(url.lastIndexOf(47) + 1);
    }
}