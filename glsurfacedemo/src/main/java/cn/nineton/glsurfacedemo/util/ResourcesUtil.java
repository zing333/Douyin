package cn.nineton.glsurfacedemo.util;

import android.support.annotation.DimenRes;

/**
 * Created by 薛贤俊 on 2018/4/23.
 */

public class ResourcesUtil {

    public static int getDimensionPixel(@DimenRes int id) {
        return AppProfile.getContext().getResources().getDimensionPixelSize(id);
    }
}
