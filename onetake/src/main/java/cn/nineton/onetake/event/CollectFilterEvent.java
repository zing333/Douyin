package cn.nineton.onetake.event;


import cn.nineton.onetake.bean.FilterInfo;

public class CollectFilterEvent {
    public FilterInfo filterInfo;
    public int position;

    public CollectFilterEvent(FilterInfo filterInfo, int position) {
        this.filterInfo = filterInfo;
        this.position = position;
    }
}