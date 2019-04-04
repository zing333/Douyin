package cn.nineton.onetake.util;

import java.util.Comparator;

import cn.nineton.onetake.bean.FilterEffectBean;

public class FilterEffectCompare implements Comparator {
    public int compare(Object o1, Object o2) {
        return ((FilterEffectBean) o1).filterLocation.compareTo(((FilterEffectBean) o2).filterLocation);
    }
}