package cn.nineton.onetake.event;

import java.util.List;

import cn.nineton.onetake.bean.FilterInfo;

public class RefreshFilterSortEvent {
    public boolean isNeedRefresh;
    public List<FilterInfo> orderedCollectedList;
    public List<FilterInfo> orderedDownloadlist;

    public RefreshFilterSortEvent(List<FilterInfo> orderedCollectedList, List<FilterInfo> orderedDownloadlist, boolean isNeedRefresh) {
        this.orderedCollectedList = orderedCollectedList;
        this.orderedDownloadlist = orderedDownloadlist;
        this.isNeedRefresh = isNeedRefresh;
    }
}