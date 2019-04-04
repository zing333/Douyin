package cn.nineton.onetake.bean;

import java.util.Comparator;

import cn.nineton.onetake.util.DraftModel;

public class DraftComparator implements Comparator<DraftModel> {
    public int compare(DraftModel o1, DraftModel o2) {
        return -o1.getTimeStamp().compareTo(o2.getTimeStamp());
    }
}