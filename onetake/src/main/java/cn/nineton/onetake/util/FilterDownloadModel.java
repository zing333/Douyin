package cn.nineton.onetake.util;

import java.util.HashMap;

public class FilterDownloadModel {
    public static final int Downloading = 2;
    public static final int FailDownload = 4;
    public static final int FinishDownload = 3;
    public static final int RequestDownload = 1;
    public static final int UnDownload = 0;
    private static FilterDownloadModel filterDownloadModel;
    private HashMap<Integer, Integer> downloadMap;
    private HashMap<Integer, Integer> downloadProgressMap;

    private FilterDownloadModel() {
        if (this.downloadMap == null) {
            this.downloadMap = new HashMap();
        }
        if (this.downloadProgressMap == null) {
            this.downloadProgressMap = new HashMap();
        }
    }

    public static FilterDownloadModel getInstance() {
        if (filterDownloadModel == null) {
            filterDownloadModel = new FilterDownloadModel();
        }
        return filterDownloadModel;
    }

    public int getDownloadState(int filterId) {
        if (this.downloadMap.containsKey(Integer.valueOf(filterId))) {
            return ((Integer) this.downloadMap.get(Integer.valueOf(filterId))).intValue();
        }
        return 0;
    }

    public int getDownloadProgress(int filterId) {
        if (this.downloadProgressMap.containsKey(Integer.valueOf(filterId))) {
            return ((Integer) this.downloadProgressMap.get(Integer.valueOf(filterId))).intValue();
        }
        return 0;
    }

    public void setDownloadProgress(int filterId, int progress) {
        this.downloadProgressMap.put(Integer.valueOf(filterId), Integer.valueOf(progress));
    }

    public void setDownloadState(int filterId, int downloadState) {
        this.downloadMap.put(Integer.valueOf(filterId), Integer.valueOf(downloadState));
    }

    public void removeDownloadStateBuFilterId(int filterId) {
        if (this.downloadMap.size() > 0 && this.downloadMap.containsKey(Integer.valueOf(filterId))) {
            this.downloadMap.remove(Integer.valueOf(filterId));
        }
        if (this.downloadProgressMap.size() > 0 && this.downloadProgressMap.containsKey(Integer.valueOf(filterId))) {
            this.downloadProgressMap.remove(Integer.valueOf(filterId));
        }
    }

    public boolean isHasDownloadingFilters() {
        if (this.downloadMap == null) {
            return false;
        }
        for (Integer integer : this.downloadMap.keySet()) {
            Integer integer1 = (Integer) this.downloadMap.get(integer);
            if (integer1.intValue() != 1) {
                if (integer1.intValue() == 2) {
                }
            }
            return true;
        }
        return false;
    }
}