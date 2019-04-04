package cn.nineton.onetake.util;

import android.text.TextUtils;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.filterview.FilterEffectBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.draft.DraftInfoBean;
//import com.blink.academy.onetake.model.DraftLongVideoBean;
//import com.blink.academy.onetake.model.DraftModel;
//import com.blink.academy.onetake.model.FilterModel;
//import com.blink.academy.onetake.model.GlobalLocationModel;
//import com.blink.academy.onetake.model.MscvModel;
//import com.blink.academy.onetake.model.StoryModel;
//import com.blink.academy.onetake.model.video.UndoBean;
//import com.blink.academy.onetake.model.video.UndoModel;
//import com.blink.academy.onetake.support.comparator.DraftComparator;
//import com.blink.academy.onetake.support.events.DraftAddEvent;
//import com.blink.academy.onetake.support.manager.DraftBoxManager$.Lambda.1;
//import com.blink.academy.onetake.support.manager.DraftBoxManager$.Lambda.2;
//import com.blink.academy.onetake.support.manager.DraftBoxManager$.Lambda.3;
//import com.blink.academy.onetake.support.manager.DraftBoxManager$.Lambda.4;
//import com.blink.academy.onetake.support.manager.DraftBoxManager$.Lambda.5;
//import com.blink.academy.onetake.support.manager.FilterEffectManager.EffectType;
//import com.blink.academy.onetake.support.utils.BuglyLogUtil;
//import com.blink.academy.onetake.support.utils.FileUtil;
//import com.blink.academy.onetake.support.utils.MovieFileUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.FilterActivity;
import cn.nineton.onetake.adapter.UndoBean;
import cn.nineton.onetake.bean.DraftComparator;
import cn.nineton.onetake.bean.DraftInfoBean;
import cn.nineton.onetake.bean.DraftLongVideoBean;
import cn.nineton.onetake.bean.FilterEffectBean;
import cn.nineton.onetake.bean.FilterEffectManager;
import cn.nineton.onetake.bean.FilterModel;
import cn.nineton.onetake.bean.IExceptionCallback;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.MscvModel;
import cn.nineton.onetake.bean.UndoModel;
import cn.nineton.onetake.event.DraftAddEvent;

public class DraftBoxManager {
    private static DraftBoxManager sDraftBoxManager;
    private List<DraftModel> mDraftModelList = new ArrayList();

    private DraftBoxManager() {
        loadDraftData();
    }

    private void loadDraftData() {
        new Thread(new Runnable() {
            public void run() {
                DraftBoxManager.this.loadDataFromDisk();
            }
        }).start();
    }

    private void loadDataFromDisk() {
        final ArrayList<DraftModel> draftModels = new ArrayList();
//        new File(Config.getDraftsPath()).list(1.lambdaFactory$(this, draftModels));
        new File(Config.getDraftsPath()).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return lambda$loadDataFromDisk$4(draftModels,dir,name);
            }
        });
        if (draftModels.size() > 0) {
            App.runOnUiThread(new Runnable() {
                public void run() {
                    DraftBoxManager.this.mDraftModelList.addAll(draftModels);
                    Collections.sort(DraftBoxManager.this.mDraftModelList, new DraftComparator());
                    EventBus.getDefault().post(new DraftAddEvent("-1"));
                }
            });
        }
    }

    private /* synthetic */ boolean lambda$loadDataFromDisk$4(ArrayList draftModels, File fileDir, String name) {
        File file = new File(fileDir, name);
        if (!file.isDirectory()) {
            return false;
        }
        FilterModel filterModel;
        String filterName;
        int i;
        file.list(null);
        DraftModel draftModel = new DraftModel(file.getName());
        String infoContent = FileUtil.readFile(draftModel.infoPath(), Charset.defaultCharset());
        DraftInfoBean draftInfo = JsonParserUtil.parse(infoContent, DraftInfoBean.class, null);
        StoryModel storyModel = JsonParserUtil.parse(FileUtil.readFile(draftModel.storyPath(), Charset.defaultCharset()), StoryModel.class, null);
        String mscvContent = FileUtil.readFile(draftModel.mscvPath(), Charset.defaultCharset());
        if (!TextUtils.isEmpty(mscvContent)) {
            MscvModel mscvModel = JsonParserUtil.parse(mscvContent, MscvModel.class, new IExceptionCallback() {
                public void doException() {
                }
            });
            if (mscvModel != null) {
                draftModel.setMscvModel(mscvModel);
            }
        }
        draftModel.setDraftInfoBean(draftInfo);
        draftModel.setStoryModel(storyModel);
        if (new File(draftModel.filterPath()).exists()) {
            filterModel = JsonParserUtil.parse(FileUtil.readFile(draftModel.filterPath(), Charset.defaultCharset()), FilterModel.class, null);
            draftModel.setFilterModel(filterModel);
            if (!(filterModel == null || TextUtils.isEmpty(filterModel.getSignature()))) {
                draftModel.setSignature(filterModel.getSignature());
            }
        }
        DraftLongVideoBean videoModel = null;
        if (new File(draftModel.videoModelPath()).exists()) {
            try {
                videoModel = new Gson().fromJson(FileUtil.readFile(draftModel.videoModelPath(), Charset.defaultCharset()), DraftLongVideoBean.class);
            } catch (Exception e) {
                e.printStackTrace();
//                BuglyLogUtil.writeKeyAndValueLog("JsonCrash", e.getMessage());
            }
            if (videoModel != null) {
                draftModel.setmLongVideoBean(videoModel);
                ArrayList<LongVideosModel> longVideoModels = videoModel.getLongVideoModels();
                if (TextUtil.isValidate(longVideoModels)) {
                    Iterator it;
                    if (draftInfo != null && draftInfo.version <= 42) {
                        it = longVideoModels.iterator();
                        while (it.hasNext()) {
                            LongVideosModel m = (LongVideosModel) it.next();
                            m.setAudioConfirmVolume(0.5f);
                            m.setAudioDefaultVolume(0.5f);
                            m.setVideoConfirmVolume(1.0f);
                            m.setVideoDefaultVolume(1.0f);
                        }
                    }
                    if (draftInfo != null && draftInfo.version <= 43) {
                        filterModel = draftModel.getFilterModel();
                        if (filterModel != null) {
                            filterName = filterModel.getFilterName();
                            List<FilterEffectBean> selectEffectTypeList = filterModel.getSelectEffectTypeList();
                            float strength = 0.1f;
                            if (TextUtil.isValidate(selectEffectTypeList)) {
                                for (FilterEffectBean bean : selectEffectTypeList) {
                                    if (bean.effectType == FilterEffectManager.EffectType.STRENGTH) {
                                        strength = bean.value[1] / 10.0f;
                                    }
                                }
                            }
                            int size = longVideoModels.size();
                            for (i = 0; i < size; i++) {
                                LongVideosModel model = (LongVideosModel) longVideoModels.get(i);
                                model.setPublicFilterName(filterName);
                                model.setPrivateFilterName(FilterActivity.NONE_FILTER);
                                model.setPrivateIntensity(1.0f);
                                model.setPublicIntensity(strength);
                            }
                        }
                    }
                }
            }
        }
        if (videoModel != null) {
            int undoCount = videoModel.getUndoCount();
            if (undoCount > 0) {
                List<UndoModel> undoModels = new ArrayList();
                for (i = 0; i < undoCount; i++) {
                    UndoModel undoModel = new Gson().fromJson(FileUtil.readFile(draftModel.videoUndoPath(i), Charset.defaultCharset()), UndoModel.class);
                    if (undoModel != null) {
                        if (draftInfo != null && draftInfo.version <= 43) {
                            List<LongVideosModel> videoModels = undoModel.getEditModel().videoModels;
                            filterModel = draftModel.getFilterModel();
                            if (filterModel != null) {
                                filterName = filterModel.getFilterName();
                                int sizeq = videoModels.size();
                                for (int j = 0; j < sizeq; j++) {
                                    videoModels.get(j).setPublicFilterName(filterName);
                                }
                            }
                        }
                        undoModels.add(undoModel);
                    }
                }
                UndoBean undoBean = new UndoBean();
                undoBean.setUndoModels(undoModels);
                videoModel.setUndoBean(undoBean);
            }
        }
        if (draftModel.dirIsValid()) {
            App.setHaveDraftData(true);
            draftModels.add(draftModel);
        }
        return true;
    }

    private static /* synthetic */ void lambda$null$1() {
    }

    private static /* synthetic */ void lambda$null$2() {
    }

    private static /* synthetic */ void lambda$null$3() {
    }

    public static DraftBoxManager getInstance() {
        if (sDraftBoxManager == null) {
            synchronized (DraftBoxManager.class) {
                if (sDraftBoxManager == null) {
                    sDraftBoxManager = new DraftBoxManager();
                }
            }
        }
        return sDraftBoxManager;
    }

    public int deleteDraftWithTimeStamp(String timeStamp) {
        if (!(this.mDraftModelList == null || timeStamp == null)) {
            for (int index = 0; index < this.mDraftModelList.size(); index++) {
                DraftModel model = (DraftModel) this.mDraftModelList.get(index);
                if (model.getTimeStamp().equals(timeStamp)) {
                    model.deleteInfo();
                    this.mDraftModelList.remove(model);
                    return index;
                }
            }
        }
        return -1;
    }

    public DraftModel newestModel() {
        if (this.mDraftModelList == null || this.mDraftModelList.size() == 0) {
            return null;
        }
        return (DraftModel) this.mDraftModelList.get(0);
    }

    public void saveDraftWithStory(DraftModel draftModel) {
        if (this.mDraftModelList == null) {
            this.mDraftModelList = new ArrayList();
        }
        if (draftModel != null && draftModel.dirIsValid()) {
            this.mDraftModelList.add(0, draftModel);
        }
    }

    public void saveDraftLocationAndMsvToSDcard(DraftModel draftModel) {
        if (draftModel != null) {
            File timeStampFile = new File(Config.getDraftsPath() + draftModel.getTimeStamp());
            if (!timeStampFile.exists()) {
                timeStampFile.mkdir();
            }
            FileUtil.writeInfoToFile(draftModel.infoPath(), JsonParserUtil.serializeToJson(draftModel.getDraftInfoBean()));
            try {
                //GlobalLocationModel globalLocationModel = new GlobalLocationModel(GlobalLocationManager.getInstance());
                //FileUtil.writeInfoToFile(draftModel.binaryPath(), JsonParserUtil.serializeToJson(globalLocationModel));
                //draftModel.setGlobalLocation(globalLocationModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (draftModel.getMscvModel() == null) {
                FileUtil.writeInfoToFile(draftModel.mscvPath(), JsonParserUtil.serializeToJson(MscvModel.getInstance()));
                String mscvContent = FileUtil.readFile(draftModel.mscvPath(), Charset.defaultCharset());
                if (!TextUtils.isEmpty(mscvContent)) {
                    MscvModel mscvModel = JsonParserUtil.parse(mscvContent, MscvModel.class, new IExceptionCallback() {
                        public void doException() {
                        }
                    });
                    if (mscvModel != null) {
                        draftModel.setMscvModel(mscvModel);
                    }
                }
            }
        }
    }

    public void savePreviewToSDcard(DraftModel draftModel) {
        if (draftModel != null) {
            long t1 = System.currentTimeMillis();
            File timeStampFile = new File(Config.getDraftsPath() + draftModel.getTimeStamp());
            if (!timeStampFile.exists()) {
                boolean mkdirs = timeStampFile.mkdirs();
            }
            long t2 = System.currentTimeMillis();
            String previewPath = MovieFileUtil.getJPGFilePathWithIndex(draftModel.getTimeStamp(), 0);
            if (new File(previewPath).exists()) {
                FileUtil.copyFile(previewPath, draftModel.previewPath());
            }
            long t3 = System.currentTimeMillis();
            File originalFile = new File(MovieFileUtil.getOriginalFilePath(draftModel.getTimeStamp()));
            if (originalFile.exists()) {
                FileUtil.copyFile(originalFile, draftModel.originalPath());
            }
            long t4 = System.currentTimeMillis();
            FileUtil.writeInfoToFile(draftModel.filterPath(), JsonParserUtil.serializeToJson(draftModel.getFilterModel()));
            long t5 = System.currentTimeMillis();
            DraftLongVideoBean longVideoBean = draftModel.getLongVideoBean();
            UndoBean undoBean = longVideoBean.getUndoBean();
            longVideoBean.clearUndoModels();
            FileUtil.writeInfoToFile(draftModel.videoModelPath(), JsonParserUtil.serializeToJson(longVideoBean));
            long t6 = System.currentTimeMillis();
            List<UndoModel> undoModels = undoBean.getUndoModels();
            int size = undoModels.size();
            if (size > 0) {
                FileUtil.writeInfoToFile(draftModel.videoUndoPath(size - 1), JsonParserUtil.serializeToJson((UndoModel) undoModels.get(size - 1)));
            }
            long t7 = System.currentTimeMillis();
            updateDraftModelFilterModelData(draftModel);
        }
    }

    public void saveMoreShortLongThumbnail(DraftModel draftModel) {
        if (draftModel != null) {
            File timeStampFile = new File(Config.getDraftsPath() + draftModel.getTimeStamp());
            if (!timeStampFile.exists()) {
                timeStampFile.mkdir();
            }
            String moreShortLongThumbnailPath = MovieFileUtil.getMoreShortLongThumbnailFilePath(draftModel.getTimeStamp());
            if (new File(moreShortLongThumbnailPath).exists()) {
                FileUtil.copyFile(moreShortLongThumbnailPath, draftModel.moreShortLongThumbnailPath());
            }
            String longThumbnailFilePath = MovieFileUtil.getLongThumbnailFilePath(draftModel.getTimeStamp());
            if (new File(longThumbnailFilePath).exists()) {
                FileUtil.copyFile(longThumbnailFilePath, draftModel.longThumbnailPath());
            }
            FileUtil.writeInfoToFile(draftModel.filterPath(), JsonParserUtil.serializeToJson(draftModel.getFilterModel()));
            FileUtil.writeInfoToFile(draftModel.videoModelPath(), JsonParserUtil.serializeToJson(draftModel.getLongVideoBean()));
            updateDraftModelFilterModelData(draftModel);
            FileUtil.writeInfoToFile(draftModel.mscvPath(), JsonParserUtil.serializeToJson(MscvModel.getInstance()));
            String mscvContent = FileUtil.readFile(draftModel.mscvPath(), Charset.defaultCharset());
            if (!TextUtils.isEmpty(mscvContent)) {
                MscvModel mscvModel = (MscvModel) JsonParserUtil.parse(mscvContent, MscvModel.class, new IExceptionCallback() {
                    public void doException() {
                    }
                });
                if (mscvModel != null) {
                    draftModel.setMscvModel(mscvModel);
                }
            }
        }
    }

    public void savePublishInfoDraftToSDCard(DraftModel draftModel) {
        File timeStampFile = new File(Config.getDraftsPath() + draftModel.getTimeStamp());
        if (!timeStampFile.exists()) {
            timeStampFile.mkdir();
        }
        //FileUtil.writeInfoToFile(draftModel.binaryPath(), JsonParserUtil.serializeToJson(draftModel.getGlobalLocation()));
        FileUtil.writeInfoToFile(draftModel.storyPath(), JsonParserUtil.serializeToJson(draftModel.getStoryModel()));
        updateDraftModelStoryModelData(draftModel);
    }

    private DraftModel getDraftModelByTimeStamp(String timeStap) {
        for (DraftModel draftModel : this.mDraftModelList) {
            if (draftModel.getTimeStamp().equals(timeStap)) {
                return draftModel;
            }
        }
        return null;
    }

    private void updateDraftModelStoryModelData(DraftModel draftModel) {
        DraftModel draftModelByTimeStamp = getDraftModelByTimeStamp(draftModel.getTimeStamp());
        if (draftModelByTimeStamp != null) {
           // draftModelByTimeStamp.setGlobalLocation(draftModel.getGlobalLocation());
            draftModelByTimeStamp.setStoryModel(draftModel.getStoryModel());
        }
    }

    private void updateDraftModelFilterModelData(DraftModel draftModel) {
        DraftModel draftModelByTimeStamp = getDraftModelByTimeStamp(draftModel.getTimeStamp());
        if (draftModelByTimeStamp != null) {
            draftModelByTimeStamp.setFilterModel(draftModel.getFilterModel());
            draftModelByTimeStamp.setSignature(draftModel.getSignature());
        }
    }

    public DraftModel getDraftModelWithIndex(int index) {
        if (this.mDraftModelList == null) {
            this.mDraftModelList = new ArrayList();
        }
        if (index < this.mDraftModelList.size()) {
            return (DraftModel) this.mDraftModelList.get(index);
        }
        return null;
    }

    public List<DraftModel> getDraftModelList() {
        if (this.mDraftModelList == null) {
            this.mDraftModelList = new ArrayList();
        }
        return this.mDraftModelList;
    }

    public List<DraftModel> getDraftNoPicModelList() {
        List<DraftModel> draftModelList = getInstance().getDraftModelList();
        for (int i = draftModelList.size() - 1; i >= 0; i--) {
            if (((DraftModel) draftModelList.get(i)).getDraftInfoBean().vtype == 1) {
                draftModelList.remove(i);
            }
        }
        return draftModelList;
    }

    public static String dirWithTimeStamp(String timeStamp) {
        return Config.getDraftsPath() + timeStamp;
    }

    public static void deleteJPGDir(String timeStamp) {
        FileUtil.deleteDirectory(MovieFileUtil.getCurrentTimeStampFilePath(timeStamp));
    }

    public static void deleteVideoDir(String timeStamp) {
        FileUtil.deleteFile(MovieFileUtil.getOutputMediaFile(timeStamp));
    }

    public static void deleteTimeStampDir(String timeStamp) {
        deleteVideoDir(timeStamp);
        deleteJPGDir(timeStamp);
    }
}