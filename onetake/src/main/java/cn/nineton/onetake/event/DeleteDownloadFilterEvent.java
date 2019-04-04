package cn.nineton.onetake.event;


import cn.nineton.onetake.bean.FilterInfo;

public class DeleteDownloadFilterEvent {
    public FilterInfo removGroup;

    public DeleteDownloadFilterEvent(FilterInfo removGroup) {
        this.removGroup = removGroup;
    }
}