package cn.nineton.onetake.util;

//import com.blink.academy.onetake.draft.DraftInfoBean;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.manager.DraftBoxManager;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.MovieFileUtil;
//import com.blink.academy.onetake.ui.adapter.entities.BaseEntity;
import java.io.File;
import java.io.Serializable;

import cn.nineton.onetake.bean.BaseEntity;
import cn.nineton.onetake.bean.DraftInfoBean;
import cn.nineton.onetake.bean.DraftLongVideoBean;
import cn.nineton.onetake.bean.FilterModel;
import cn.nineton.onetake.bean.MscvModel;

public class DraftModel extends BaseEntity implements Serializable {
//    GlobalLocationModel globalLocation;
    DraftInfoBean mDraftInfoBean;
    FilterModel mFilterModel;
    DraftLongVideoBean mLongVideoBean;
    MscvModel mMscvModel;
    String mSignature;
    StoryModel mStoryModel;
    String timeStamp;

    public DraftLongVideoBean getmLongVideoBean() {
        return this.mLongVideoBean;
    }

    public void setmLongVideoBean(DraftLongVideoBean mLongVideoBean) {
        this.mLongVideoBean = mLongVideoBean;
    }

    public DraftLongVideoBean getLongVideoBean() {
        return this.mLongVideoBean;
    }

    public MscvModel getMscvModel() {
        return this.mMscvModel;
    }

    public void setMscvModel(MscvModel mscvModel) {
        this.mMscvModel = mscvModel;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public void setSignature(String signature) {
        this.mSignature = signature;
        if (this.mFilterModel != null) {
            this.mFilterModel.setSignature(signature);
        }
    }

    public DraftModel(String timeStamp) {
        this(false, 0);
        this.timeStamp = timeStamp;
        this.mSignature = this.timeStamp;
    }

    public DraftModel(boolean pin, int viewType) {
        super(pin, viewType);
    }

    public int getDataType() {
        if (this.mDraftInfoBean != null) {
            return this.mDraftInfoBean.vtype;
        }
        return -1;
    }

    public boolean dirIsValid() {
        File videoFile = new File(videoPath());
        File previewFile = new File(previewPath());
        File originalFile = new File(originalPath());
        File picFile = new File(MovieFileUtil.getOriginalSDFilePath(this.timeStamp));
        if (this.mDraftInfoBean == null) {
            return false;
        }
        String str = "AddDraft";
        String str2 = "type : %s, longVideoBean : %s, version > version : %s";
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(this.mDraftInfoBean.vtype);
        objArr[1] = this.mLongVideoBean;
        objArr[2] = Boolean.valueOf(this.mDraftInfoBean.version >= 44);
        LogUtil.d(str, String.format(str2, objArr));
        if (this.mDraftInfoBean.vtype == 0) {
            if (this.mDraftInfoBean == null || this.mDraftInfoBean.version < 33 || this.timeStamp == null || !this.timeStamp.equals(this.mDraftInfoBean.timestamp) || !previewFile.exists() || !videoFile.exists()) {
                return false;
            }
            return true;
        } else if (this.mDraftInfoBean.vtype != 2 || this.mLongVideoBean == null) {
            return false;
        } else {
            if (this.mDraftInfoBean.version < 42) {
                return false;
            }
            return true;
        }
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public int getWidth() {
        return this.mStoryModel.getWidth();
    }

    public int getHeight() {
        return this.mStoryModel.getHeight();
    }

    public void setDraftInfoBean(DraftInfoBean draftInfoBean) {
        this.mDraftInfoBean = draftInfoBean;
    }

    public DraftInfoBean getDraftInfoBean() {
        return this.mDraftInfoBean;
    }

    public void setStoryModel(StoryModel storyModel) {
        this.mStoryModel = storyModel;
    }

//    public void setGlobalLocation(GlobalLocationModel globalLocation) {
//        this.globalLocation = globalLocation;
//    }

    public FilterModel getFilterModel() {
        return this.mFilterModel;
    }

    public void setFilterModel(FilterModel filterModel) {
        this.mFilterModel = filterModel;
    }

    public String previewPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/preview.jpg";
    }

    public String originalPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/original.jpg";
    }

    public static String getDraftPreviewPath(String timeStamp) {
        return DraftBoxManager.dirWithTimeStamp(timeStamp) + "/preview.jpg";
    }

    public String longThumbnailPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/long.jpg";
    }

    public String moreShortLongThumbnailPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/more_short_long.jpg";
    }

    public String videoPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/video.mp4";
    }

    public String infoPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/info.json";
    }

    public String storyPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/story.json";
    }

    public String binaryPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/binary.data";
    }

    public String filterPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/filter.json";
    }

    public String videoModelPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/video.json";
    }

    public String videoUndoPath(int position) {
        String path = DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "/undo/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + "" + position + ".json";
    }

    public String mscvPath() {
        return DraftBoxManager.dirWithTimeStamp(this.timeStamp) + "mscv.data";
    }

    //public GlobalLocationModel getGlobalLocation() {
    //    return this.globalLocation;
    //}

    public StoryModel getStoryModel() {
        return this.mStoryModel;
    }

    public void deleteInfo() {
        FileUtil.deleteDirectory(DraftBoxManager.dirWithTimeStamp(this.timeStamp));
    }
}