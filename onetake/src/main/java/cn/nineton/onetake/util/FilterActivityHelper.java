package cn.nineton.onetake.util;

import android.support.v4.view.ViewCompat;
import android.view.View;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.FilterInfo;
//import com.blink.academy.onetake.support.interfaces.AnimatorEndListener;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.FilterActivity;
import cn.nineton.onetake.bean.FilterInfo;
import cn.nineton.onetake.listener.AnimatorEndListener;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class FilterActivityHelper {
    public static final int FILTER_TAB_SORT_INDEX = 1;

    public int shouldSelectFilterPosition(List<FilterInfo> filterInfos, String groupName) {
        int i = 0;
        int size = filterInfos.size();
        while (i < size) {
            FilterInfo info = (FilterInfo) filterInfos.get(i);
            if (groupName.equalsIgnoreCase(info.groupNameChs) || groupName.equalsIgnoreCase(info.groupNameEn)) {
                if (!(groupName.equalsIgnoreCase("无") || groupName.equalsIgnoreCase(FilterActivity.NONE_FILTER) || groupName.equalsIgnoreCase(getString(2131296406)))) {
                    i++;
                }
                return i;
            }
            i++;
        }
        return 1;
    }

    public int getFilterGroupPositionByGrouName(List<FilterInfo> mFilterGroupNames, String groudName) {
        int size = mFilterGroupNames.size();
        for (int i = 0; i < size; i++) {
            if (((FilterInfo) mFilterGroupNames.get(i)).name_en.equalsIgnoreCase(groudName)) {
                return i;
            }
        }
        return 0;
    }

    public int shouldSelectFilterGroupPosition(List<FilterInfo> mFilterGroupNames, ArrayList<FilterInfo> filterInfos, int filterPosition) {
        FilterInfo filterInfo = (FilterInfo) filterInfos.get(filterPosition);
        int size = mFilterGroupNames.size();
        for (int i = 0; i < size; i++) {
            if (((FilterInfo) mFilterGroupNames.get(i)).name_en.equalsIgnoreCase(filterInfo.groupNameEn)) {
                return i;
            }
        }
        return 0;
    }

    public FilterInfo getFilterInfoByFilterName(ArrayList<FilterInfo> filterInfos, String filterName) {
        if (!TextUtil.isValidate(filterInfos)) {
            return null;
        }
        if (TextUtil.isNull(filterName)) {
            return null;
        }
        Iterator it = filterInfos.iterator();
        while (it.hasNext()) {
            FilterInfo info = (FilterInfo) it.next();
            if (filterName.equals(info.name_en)) {
                return info;
            }
        }
        return null;
    }

    private String getString(int resId) {
        return App.getResource().getString(resId);
    }

    public static void alphaEnter(final View enterView) {
        if (enterView != null) {
            clearViewAnimation(enterView);
            if (enterView.getAlpha() != StaticLayoutUtil.DefaultSpacingadd) {
                enterView.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            }
            if (enterView.getVisibility() != View.VISIBLE) {
                enterView.setVisibility(View.VISIBLE);
            }
            ViewPropertyAnimator.animate(enterView).alpha(1.0f).setDuration(200).setListener(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    enterView.setVisibility(View.VISIBLE);
                }
            }).start();
        }
    }

    public static void alphaExit(final View exitView) {
        if (exitView != null && exitView.getVisibility() == View.VISIBLE) {
            clearViewAnimation(exitView);
            if (exitView.getAlpha() != 1.0f) {
                exitView.setAlpha(1.0f);
            }
            ViewPropertyAnimator.animate(exitView).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(200).setListener(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    exitView.setVisibility(View.GONE);
                }
            }).start();
        }
    }

    private static void clearViewAnimation(View view) {
        if (view != null) {
            ViewCompat.animate(view).cancel();
            if (view.getAnimation() != null) {
                view.clearAnimation();
            }
        }
    }
}