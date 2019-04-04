package cn.nineton.onetake.event;

import java.util.List;

import cn.nineton.onetake.bean.FilterInfo;

public class FiltersDownloadEvent {
    public static final int DOWNLOAD_FROM_FILTER_STORE_DETAIL = 2;
    public static final int DOWNLOAD_FROM_FILTER_STORE_LIST = 1;
    private int download_from;
    private FilterInfo filterInfo;
    private List<FilterInfo> filterInfos;
    private int filters_id;
    private int position;
    private int progress;

    public FiltersDownloadEvent(int download_from, int filters_id, int progress, int position) {
        this.download_from = download_from;
        this.filters_id = filters_id;
        this.progress = progress;
        this.position = position;
    }

    public FiltersDownloadEvent(int download_from, int filters_id, int progress, int position, List<FilterInfo> filterInfos, FilterInfo filterInfo) {
        this.download_from = download_from;
        this.filters_id = filters_id;
        this.progress = progress;
        this.position = position;
        this.filterInfos = filterInfos;
        this.filterInfo = filterInfo;
    }

    public List<FilterInfo> getFilterInfos() {
        return this.filterInfos;
    }

    public void setFilterInfos(List<FilterInfo> filterInfos) {
        this.filterInfos = filterInfos;
    }

    public FilterInfo getFilterInfo() {
        return this.filterInfo;
    }

    public void setFilterInfo(FilterInfo filterInfo) {
        this.filterInfo = filterInfo;
    }

    public int getDownload_from() {
        return this.download_from;
    }

    public int getFilters_id() {
        return this.filters_id;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getPosition() {
        return this.position;
    }
}