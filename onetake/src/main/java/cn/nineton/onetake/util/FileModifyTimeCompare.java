package cn.nineton.onetake.util;

import java.util.Comparator;

import cn.nineton.onetake.bean.ProxySaveBean;

public class FileModifyTimeCompare implements Comparator {
    public int compare(Object o1, Object o2) {
        return Long.valueOf(((ProxySaveBean) o1).getSaveTime()).compareTo(Long.valueOf(((ProxySaveBean) o2).getSaveTime()));
    }
}