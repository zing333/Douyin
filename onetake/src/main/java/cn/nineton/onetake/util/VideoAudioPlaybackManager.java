package cn.nineton.onetake.util;

import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.ImageView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.VideoTools.FilterView.VideoSpeedup;
//import com.blink.academy.onetake.VideoTools.MediaUtils;
//import com.blink.academy.onetake.bean.filterview.FilterEffectBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.UploadVideoAllInfoBean;
//import com.blink.academy.onetake.bean.longvideo.UploadVideoBean;
//import com.blink.academy.onetake.model.video.UndoEditModel;
//import com.blink.academy.onetake.model.video.UndoFilterModel;
//import com.blink.academy.onetake.model.video.UndoModel;
//import com.blink.academy.onetake.model.video.VideoInputRatio;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.BitmapUtil;
//import com.blink.academy.onetake.support.utils.FilterViewUtils;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videoeditimage.ImageCacheUtils;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils;
//import com.blink.academy.onetake.ui.helper.VideoModelHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.FilterEffectBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.UndoEditModel;
import cn.nineton.onetake.bean.UndoFilterModel;
import cn.nineton.onetake.bean.UndoModel;
import cn.nineton.onetake.bean.UploadVideoAllInfoBean;
import cn.nineton.onetake.bean.UploadVideoBean;
import cn.nineton.onetake.bean.VideoInputRatio;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.widget.FilterView;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class VideoAudioPlaybackManager {
    private static final String TAG = "VideoAudioPlaybackManager";
    private boolean allAudioMute;
    private boolean allVideoMute;
    private int longVideoSize = 0;
    private LongVideosModel mCurrentVideoModel;
    ImageCacheUtils mImageCacheUtils = new ImageCacheUtils();
    private ArrayList<UndoModel> mUndoModels = new ArrayList();
    private List<String> mVideoPathList = new ArrayList();
    WaveformCacheUtils mWaveformCacheUtils = WaveformCacheUtils.getWaveformCacheUtils();
    private ArrayList<LongVideosModel> musicModelList = new ArrayList();
    private List<UndoModel> removeUndoModels = new ArrayList();
    private ArrayList<LongVideosModel> textModelList = new ArrayList();
    private ArrayList<LongVideosModel> videosModelList = new ArrayList();

    public void changeAllPlaylistForUndo(List<LongVideosModel> mList) {
        initVideoModelList();
        this.videosModelList.clear();
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            LongVideosModel model = ((LongVideosModel) mList.get(i)).cloneVideoTypeModel();
            if (Build.MODEL.contains("SM-") && model.mediaType == 0 && Math.max(model.getVideoWidth(), model.getVideoHeight()) > 3500) {
                model.mediaType = 2;
                model.setVideoPath("");
            }
            this.videosModelList.add(model);
        }
        VideoModelHelper.refreshVideoFileExists(this.videosModelList);
    }

    public boolean[] getMediaMute() {
        return new boolean[]{this.allVideoMute, this.allAudioMute};
    }

    public boolean isAllVideoMute() {
        return this.allVideoMute;
    }

    public void setAllVideoMute(boolean allVideoMute) {
        this.allVideoMute = allVideoMute;
    }

    public boolean isAllAudioMute() {
        return this.allAudioMute;
    }

    public void setAllAudioMute(boolean allAudioMute) {
        this.allAudioMute = allAudioMute;
    }

    public void setCurrentVideoModel(LongVideosModel mCurrentVideoModel) {
        this.mCurrentVideoModel = mCurrentVideoModel;
    }

    public LongVideosModel getCurrentVideoModel() {
        return this.mCurrentVideoModel;
    }

    public void setTextModelList(List<LongVideosModel> mList) {
        judgeTextModelListNull();
        if (mList != null && mList.size() > 0) {
            this.textModelList.addAll(mList);
        }
    }

    public void changeTextModelsWhenUndo(List<LongVideosModel> mList) {
        judgeTextModelListNull();
        this.textModelList.clear();
        if (mList != null && mList.size() > 0) {
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                this.textModelList.add(((LongVideosModel) mList.get(i)).cloneVideoTypeModel());
            }
        }
    }

    public void changeTextModels(List<LongVideosModel> mList) {
        judgeTextModelListNull();
        this.textModelList.clear();
        if (mList != null && mList.size() > 0) {
            this.textModelList.addAll(mList);
        }
    }

    public ArrayList<LongVideosModel> getTextModelList() {
        judgeTextModelListNull();
        return this.textModelList;
    }

    public ArrayList<LongVideosModel> draftCloneTextModels() {
        ArrayList<LongVideosModel> mList = new ArrayList();
        judgeTextModelListNull();
        Iterator it = this.textModelList.iterator();
        while (it.hasNext()) {
            mList.add(((LongVideosModel) it.next()).cloneVideoTypeModel());
        }
        return mList;
    }

    public void addTextModel(int position, LongVideosModel textModel) {
        judgeTextModelListNull();
        this.textModelList.add(position, textModel);
    }

    public LongVideosModel getTextModelByPosition(int position) {
        judgeTextModelListNull();
        int size = this.textModelList.size();
        if (size == 0 || position >= size) {
            return null;
        }
        return (LongVideosModel) this.textModelList.get(position);
    }

    public void changeTextModel(int position, LongVideosModel textModel) {
        judgeTextModelListNull();
        this.textModelList.set(position, textModel);
    }

    public void recycleTextBitmap() {
        if (this.textModelList != null && this.textModelList.size() > 0) {
            Iterator it = this.textModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (!(model.getBitmap() == null || model.getBitmap().isRecycled())) {
                    model.getBitmap().recycle();
                    model.setBitmapNull();
                }
            }
        }
    }

    public boolean removeTextModel(int position) {
        if (this.textModelList == null || this.textModelList.size() == 0 || position >= this.textModelList.size()) {
            return false;
        }
        LongVideosModel remove = (LongVideosModel) this.textModelList.remove(position);
        if (!(remove.getBitmap() == null || remove.getBitmap().isRecycled())) {
            remove.getBitmap().recycle();
            remove.setBitmapNull();
        }
        return true;
    }

    private void judgeTextModelListNull() {
        if (this.textModelList == null) {
            this.textModelList = new ArrayList();
        }
    }

    private boolean judgeCollection(Object obj) {
        return obj == null;
    }

    private void initVideoModelList() {
        if (judgeCollection(this.videosModelList)) {
            this.videosModelList = new ArrayList();
        }
    }

    public int getLongVideoModelSize() {
        return this.longVideoSize;
    }

    public ArrayList<LongVideosModel> getVideosModelList() {
        if (this.videosModelList == null) {
            this.videosModelList = new ArrayList();
        }
        return this.videosModelList;
    }

    public ArrayList<LongVideosModel> draftCloneVideoModels() {
        ArrayList<LongVideosModel> mList = new ArrayList();
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            Iterator it = this.videosModelList.iterator();
            while (it.hasNext()) {
                mList.add(((LongVideosModel) it.next()).cloneVideoTypeModel());
            }
        }
        return mList;
    }

    public float compareTotalTime(float suppertTotalTime) {
        float videoTotalTime = StaticLayoutUtil.DefaultSpacingadd;
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            Iterator it = this.videosModelList.iterator();
            while (it.hasNext()) {
                videoTotalTime += (float) ((LongVideosModel) it.next()).getCurrentDuration();
            }
        }
        return (videoTotalTime / 1000.0f) - suppertTotalTime;
    }

    public LongVideosModel getSingleMusicModel(int position) {
        if (this.musicModelList == null || position >= this.musicModelList.size()) {
            return null;
        }
        return (LongVideosModel) this.musicModelList.get(position);
    }

    public ArrayList<LongVideosModel> getMusicModelList() {
        if (this.musicModelList == null) {
            this.musicModelList = new ArrayList();
        }
        return this.musicModelList;
    }

    public ArrayList<LongVideosModel> draftCloneAudioModels() {
        if (this.musicModelList == null) {
            this.musicModelList = new ArrayList();
        }
        ArrayList<LongVideosModel> cloneList = new ArrayList();
        int size = this.musicModelList.size();
        for (int i = 0; i < size; i++) {
            cloneList.add(((LongVideosModel) this.musicModelList.get(i)).cloneVideoTypeModel());
        }
        return cloneList;
    }

    public void setMusicModelList(List<LongVideosModel> musicList) {
        if (this.musicModelList == null) {
            this.musicModelList = new ArrayList();
        }
        this.musicModelList.clear();
        if (musicList != null && musicList.size() > 0) {
            int size = musicList.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel musicModel = (LongVideosModel) musicList.get(i);
                String filePath = musicModel.getOriginalMediaPath();
                if (!TextUtil.isNull(filePath) && new File(filePath).exists()) {
                    this.musicModelList.add(musicModel.cloneVideoTypeModel());
                }
            }
        }
    }

    public void addCameraModelsByPaths(List<String> filePathList, boolean hasAudio) {
        int i;
        this.longVideoSize = filePathList.size();
        initVideoModelList();
        this.videosModelList.clear();
        this.mVideoPathList.addAll(filePathList);
        for (i = 0; i < filePathList.size(); i++) {
            int currentI = i;
            LongVideosModel singleVideoModel = getSingleVideoModel((String) filePathList.get(i), i);
            if (singleVideoModel != null) {
                this.videosModelList.add(singleVideoModel);
            }
        }
        getFrameBitmapsFromVideoList();
        if (App.Debug) {
            int size = this.videosModelList.size();
            for (i = 0; i < size; i++) {
                LogUtil.d(TAG, ((LongVideosModel) this.videosModelList.get(i)).toString());
            }
        }
    }

    public void addVideoModelsForCamera(List<LongVideosModel> intentVideoModes) {
        int i;
        this.longVideoSize = intentVideoModes.size();
        initVideoModelList();
        this.videosModelList.clear();
        for (LongVideosModel model : intentVideoModes) {
            this.mVideoPathList.add(model.getOriginalMediaPath());
        }
        int frameRate = getVideoFrameRate(((LongVideosModel) intentVideoModes.get(0)).getOriginalMediaPath());
        for (i = 0; i < intentVideoModes.size(); i++) {
            int orientation = ((LongVideosModel) intentVideoModes.get(i)).videoOrientation;
            LongVideosModel singleVideoModel = getSingleVideoModel((LongVideosModel) intentVideoModes.get(i), i);
            if (singleVideoModel != null) {
                singleVideoModel.setVideoFPS(frameRate);
                LogUtil.d("addVideoModelsForCamera", String.format("orientation : %s", new Object[]{Integer.valueOf(orientation)}));
                this.videosModelList.add(singleVideoModel);
            }
        }
        startInitUploadVideoInfo();
        getFrameBitmapsFromVideoList();
        if (App.Debug) {
            int size = this.videosModelList.size();
            for (i = 0; i < size; i++) {
                LogUtil.d(TAG, ((LongVideosModel) this.videosModelList.get(i)).toString());
            }
        }
    }

    private void startInitUploadVideoInfo() {
        if (this.videosModelList.size() > 0) {
            new Thread() {
                public void run() {
                    super.run();
                    int size = VideoAudioPlaybackManager.this.videosModelList.size();
                    for (int i = 0; i < size; i++) {
                        ((LongVideosModel) VideoAudioPlaybackManager.this.videosModelList.get(i)).initUploadVideoBean();
                    }
                }
            }.start();
        }
    }

    public void addAllVideoModelsByPaths2(List<LongVideosModel> videoModels, int position) {
        this.longVideoSize += videoModels.size();
        initVideoModelList();
        for (LongVideosModel model : videoModels) {
            this.mVideoPathList.add(model.getOriginalMediaPath());
        }
        List<LongVideosModel> mList = new ArrayList();
        int frameRate = ((LongVideosModel) videoModels.get(0)).getVideoFPS();
        for (int i = 0; i < videoModels.size(); i++) {
            LongVideosModel singleVideoModel = getSingleVideoModel((LongVideosModel) videoModels.get(i), i);
            if (singleVideoModel != null) {
                singleVideoModel.setVideoFPS(frameRate);
                mList.add(singleVideoModel);
            }
        }
        this.videosModelList.addAll(position, mList);
        getFrameBitmapsFromVideoList(mList);
    }

    public void getFrameBitmapsFromVideoList(List<LongVideosModel> models) {
        if (models.size() > 0) {
            this.mImageCacheUtils.preloadBitmaps(models);
            this.mWaveformCacheUtils.preloadWaveDatas(models);
        }
    }

    public void getFrameBitmapsFromVideoList() {
        if (this.videosModelList.size() > 0) {
            this.mImageCacheUtils.preloadBitmaps(this.videosModelList);
            this.mWaveformCacheUtils.preloadWaveDatas(this.videosModelList);
        }
    }

    private LongVideosModel getSingleVideoModel(LongVideosModel model, int position) {
        IOException e;
        LongVideosModel videosModel = null;
        try {
            if (new File(model.getOriginalMediaPath()).exists()) {
                MediaExtractor extractor = MediaUtils.createExtractor(model.getOriginalMediaPath());
                MediaFormat vformat = MediaUtils.getVideoTrack(extractor);
                if (vformat != null) {
                    long duration = vformat.getLong("durationUs") / 1000;
                    LongVideosModel videosModel2 = new LongVideosModel(0);
                    VideoInputRatio ratio;
                    int videoWidth = vformat.getInteger("width");
                    int videoHeight = vformat.getInteger("height");
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(model.getOriginalMediaPath());
                    String rotationS = mmr.extractMetadata(24);
                    if (TextUtil.isValidate(rotationS)) {
                        int rotation = Integer.valueOf(rotationS).intValue();
                        if (rotation == 90 || rotation == 270) {
                            int temp = videoWidth;
                            videoWidth = videoHeight;
                            videoHeight = temp;
                        }
                    }
                    videosModel2.setVideoWidth(videoWidth);
                    videosModel2.setVideoHeight(videoHeight);
                    videosModel2.setVideoPath(model.getOriginalMediaPath());
                    videosModel2.totalDuration = duration;
                    videosModel2.setStartTime(0);
                    videosModel2.setCurrentDuration(FilterViewUtils.getUseDuration(duration));
                    videosModel2.videoTag = model.getOriginalMediaPath() + position;
                    videosModel2.videoSpeedUp = FilterView.VideoSpeedup.NORMAL;
                    videosModel2.setShouldVertical(model.isShouldVertical());
                    videosModel2.videoFormat = vformat;
                    videosModel2.audioFormat = MediaUtils.getAudioTrack(extractor);
                    videosModel2.isFrontCamera = model.isFrontCamera;
                    videosModel2.videoOrientation = model.videoOrientation;
                    if (this.videosModelList.size() <= 0) {
                        ratio = new VideoInputRatio();
                        ratio.ratioWidth = videoWidth;
                        ratio.ratioHeight = videoHeight;
                    } else {
                        ratio = ((LongVideosModel) this.videosModelList.get(0)).cloneInputRatio();
                    }
                    videosModel2.setInputRatio(ratio);
                    videosModel = videosModel2;
                }
                extractor.release();
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return videosModel;
        }
        return videosModel;
    }

    private LongVideosModel getSingleVideoModel(String filePath, int position) {
        IOException e;
        LongVideosModel videosModel = null;
        try {
            MediaExtractor extractor = MediaUtils.createExtractor(filePath);
            MediaFormat vformat = MediaUtils.getVideoTrack(extractor);
            if (vformat != null) {
                long duration = vformat.getLong("durationUs") / 1000;
                LongVideosModel videosModel2 = new LongVideosModel(0);
                VideoInputRatio ratio;
                videosModel2.setVideoPath(filePath);
                videosModel2.totalDuration = duration;
                videosModel2.setStartTime(0);
                videosModel2.setCurrentDuration(FilterViewUtils.getUseDuration(duration));
                videosModel2.videoTag = filePath + position;
                videosModel2.videoSpeedUp = FilterView.VideoSpeedup.NORMAL;
                videosModel2.videoFormat = vformat;
                videosModel2.audioFormat = MediaUtils.getAudioTrack(extractor);
                if (this.videosModelList.size() <= 0) {
                    ratio = new VideoInputRatio();
                    ratio.ratioWidth = vformat.getInteger("width");
                    ratio.ratioHeight = vformat.getInteger("height");
                } else {
                    ratio = ((LongVideosModel) this.videosModelList.get(0)).cloneInputRatio();
                }
                videosModel2.setInputRatio(ratio);
                videosModel = videosModel2;
            }
            extractor.release();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return videosModel;
        }
        return videosModel;
    }

    private LongVideosModel getSingleVideoModel(String filePath, long startDuration, boolean needTotal, long currentDuration, int position, FilterView.VideoSpeedup speedUp) {
        IOException e;
        LongVideosModel videosModel = null;
        try {
            MediaExtractor extractor = MediaUtils.createExtractor(filePath);
            MediaFormat vformat = MediaUtils.getVideoTrack(extractor);
            if (vformat != null) {
                long duration = vformat.getLong("durationUs") / 1000;
                LongVideosModel videosModel2 = new LongVideosModel(0);
                videosModel2.setVideoPath(filePath);
                videosModel2.totalDuration = duration;
                videosModel2.setStartTime(startDuration);
                currentDuration = FilterViewUtils.getUseDuration(currentDuration);
                duration = FilterViewUtils.getUseDuration(duration);
                if (!needTotal) {
                    duration = currentDuration;
                }
                videosModel2.setCurrentDuration(duration);
                videosModel2.videoTag = filePath + "_" + position;
                videosModel2.videoSpeedUp = speedUp;
                videosModel2.videoFormat = vformat;
                videosModel2.audioFormat = MediaUtils.getAudioTrack(extractor);
                videosModel = videosModel2;
            }
            extractor.release();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return videosModel;
        }
        return videosModel;
    }

    public void addVideoModelsForDraft(List<LongVideosModel> videosModels) {
        initVideoModelList();
        this.videosModelList.clear();
        if (Build.MODEL.contains("SM-")) {
            int size = videosModels.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel model = (LongVideosModel) videosModels.get(i);
                if (model.mediaType == 0 && Math.max(model.getVideoWidth(), model.getVideoHeight()) >= 3500) {
                    model.mediaType = 2;
                    model.setVideoPath("");
                }
            }
        }
        VideoModelHelper.refreshVideoFileExists(videosModels);
        this.videosModelList.addAll(videosModels);
    }

    private int getVideoFrameRate(String videoPath) {
        LogUtil.d("getVideoFrameRate", String.format("path : %s", new Object[]{videoPath}));
        int videoFrameRate = 30;
        if (videoPath == null) {
            return 30;
        }
        if (!new File(videoPath).exists()) {
            return 30;
        }
        try {
            MediaExtractor extractor = MediaUtils.createExtractor(videoPath);
            MediaFormat videoFormat = MediaUtils.getVideoTrack(extractor);
            if (videoFormat != null) {
                if (videoFormat.containsKey("frame-rate")) {
                    float frameRate = (float) videoFormat.getInteger("frame-rate");
                    if (((double) frameRate) < 23.5d || ((double) frameRate) > 24.5d) {
                        videoFrameRate = 30;
                    } else {
                        videoFrameRate = 24;
                    }
                } else {
                    videoFrameRate = 30;
                }
            }
            extractor.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoFrameRate;
    }

    public void addVideoModels(List<LongVideosModel> videosModels) {
        initVideoModelList();
        this.videosModelList.clear();
        LongVideosModel model = (LongVideosModel) videosModels.get(0);
        int frameRate = getVideoFrameRate(model.getOriginalMediaPath());
        model.resetVideoInputRotateOnlyLocal();
        for (LongVideosModel model1 : videosModels) {
            model1.setInputRatio(model.cloneInputRatio());
            model1.setVideoFPS(frameRate);
        }
        this.videosModelList.addAll(videosModels);
    }

    public void addVideomodel(LongVideosModel videosModel, int position) {
        initVideoModelList();
        this.videosModelList.add(position, videosModel);
    }

    public void addVideoModelsByLocalEvent(List<LongVideosModel> mList, int position) {
        initVideoModelList();
        LongVideosModel model;
        int frameRate;
        LongVideosModel model1;
        if (this.videosModelList.size() <= 0) {
            model = (LongVideosModel) mList.get(0);
            frameRate = getVideoFrameRate(model.getOriginalMediaPath());
            model.setInputRatio(new VideoInputRatio(model.getVideoHeight(), model.getVideoWidth()));
            for (LongVideosModel model12 : mList) {
                model12.setVideoFPS(frameRate);
                model12.setInputRatio(model.cloneInputRatio());
            }
        } else {
            LongVideosModel model12 = this.videosModelList.get(0);
            frameRate = model12.getVideoFPS();
            for (LongVideosModel model2 : mList) {
                model2.setVideoFPS(frameRate);
                model2.setInputRatio(model12.cloneInputRatioWithRotation());
            }
        }
        this.videosModelList.addAll(position, mList);
    }

    public void addVideoModels(List<LongVideosModel> mList, int position) {
        initVideoModelList();
        LongVideosModel model;
        LongVideosModel model1;
        if (this.videosModelList.size() <= 0) {
            model = mList.get(0);
            model.setInputRatio(new VideoInputRatio(model.getVideoHeight(), model.getVideoWidth()));
            for (LongVideosModel model12 : mList) {
                model12.setInputRatio(model.cloneInputRatio());
            }
        } else {
            LongVideosModel model12 = this.videosModelList.get(0);
            for (LongVideosModel model2 : mList) {
                model2.setInputRatio(model12.cloneInputRatio());
            }
        }
        this.videosModelList.addAll(position, mList);
    }

    public int getVideoModelListSize() {
        if (this.videosModelList != null) {
            return this.videosModelList.size();
        }
        return 0;
    }

    public boolean removeVideoModel(int position, LongVideosModel model) {
        if (judgeCollection(this.videosModelList) || position >= this.videosModelList.size() - 1) {
            return false;
        }
        if (!model.getOriginalMediaPath().equals(((LongVideosModel) this.videosModelList.get(position)).getOriginalMediaPath())) {
            return false;
        }
        this.videosModelList.remove(position);
        return true;
    }

    public WaveformCacheUtils getWaveformCacheUtils() {
        return this.mWaveformCacheUtils;
    }

    public ImageCacheUtils getImageCacheUtils() {
        return this.mImageCacheUtils;
    }

    public void getSingleBitmapByVideo(ImageView imageViewm, LongVideosModel videosModel, long startTime, GetBitmapCallback callback) {
        this.mImageCacheUtils.getBitmap(imageViewm, videosModel.getOriginalMediaPath(), videosModel.mediaType, startTime, callback);
    }

    public void destroyManager() {
        if (this.mImageCacheUtils != null) {
            this.mImageCacheUtils.release();
            this.mImageCacheUtils = null;
        }
        if (this.mWaveformCacheUtils != null) {
            this.mWaveformCacheUtils.release();
            this.mWaveformCacheUtils = null;
        }
    }

    public long getPlaylistDuration() {
        long sumDuration = 0;
        for (int i = 0; i < this.videosModelList.size(); i++) {
            sumDuration += ((LongVideosModel) this.videosModelList.get(i)).getCurrentDuration();
        }
        return sumDuration;
    }

    public UploadVideoAllInfoBean getVideoUploadInfo() {
        Iterator it;
        UploadVideoAllInfoBean allInfoBean = new UploadVideoAllInfoBean();
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            it = this.videosModelList.iterator();
            while (it.hasNext()) {
                allInfoBean.addVideo(((LongVideosModel) it.next()).getUploadVideoBean());
            }
        }
        allInfoBean.addVideo(new UploadVideoBean("End"));
        if (this.textModelList != null && this.textModelList.size() > 0) {
            it = this.textModelList.iterator();
            while (it.hasNext()) {
                allInfoBean.addText(((LongVideosModel) it.next()).getVideoTextBean());
            }
        }
        if (this.musicModelList != null && this.musicModelList.size() > 0) {
            it = this.musicModelList.iterator();
            while (it.hasNext()) {
                allInfoBean.addAudio(((LongVideosModel) it.next()).getUploadAudioModel());
            }
        }
        return allInfoBean;
    }

    public void setUndoModels(ArrayList<UndoModel> undoModels) {
        if (undoModels == null) {
            undoModels = new ArrayList();
        }
        if (this.mUndoModels == null) {
            this.mUndoModels = new ArrayList();
        }
        this.mUndoModels.clear();
        this.mUndoModels.addAll(undoModels);
    }

    public void setUndoModel(int type, List<FilterEffectBean> mFilterList, String filterName) {
        int i;
        UndoModel undoModel = new UndoModel(type);
        UndoEditModel editModel = new UndoEditModel();
        List<LongVideosModel> videoList = new ArrayList();
        int size = this.videosModelList.size();
        for (i = 0; i < size; i++) {
            videoList.add(((LongVideosModel) this.videosModelList.get(i)).cloneVideoTypeModel());
        }
        editModel.videoModels = videoList;
        List<LongVideosModel> textList = new ArrayList();
        size = this.textModelList.size();
        for (i = 0; i < size; i++) {
            textList.add(((LongVideosModel) this.textModelList.get(i)).cloneVideoTypeModel());
        }
        editModel.textModels = textList;
        List<LongVideosModel> musicList = new ArrayList();
        size = this.musicModelList.size();
        for (i = 0; i < size; i++) {
            musicList.add(((LongVideosModel) this.musicModelList.get(i)).cloneVideoTypeModel());
        }
        editModel.musicModels = musicList;
        undoModel.setEditModel(editModel);
        UndoFilterModel filterModel = new UndoFilterModel();
        filterModel.filterName = filterName;
        List<FilterEffectBean> beans = new ArrayList();
        for (FilterEffectBean bean : mFilterList) {
            beans.add(bean.clone());
        }
        filterModel.filterBeanList = beans;
        undoModel.setFilterModel(filterModel);
        this.mUndoModels.add(undoModel);
    }

    @Nullable
    public UndoModel getDisplayUndoModel() {
        if (this.mUndoModels != null) {
            int size = this.mUndoModels.size();
            if (this.mUndoModels.size() > 1) {
                this.removeUndoModels.add((UndoModel) this.mUndoModels.remove(size - 1));
                return (UndoModel) this.mUndoModels.get(this.mUndoModels.size() - 1);
            }
        }
        return null;
    }

    public ArrayList<UndoModel> getUndoModels() {
        if (this.mUndoModels == null) {
            this.mUndoModels = new ArrayList();
        }
        return this.mUndoModels;
    }

    public ArrayList<UndoModel> draftCloneUndoModels() {
        ArrayList<UndoModel> mList = new ArrayList();
        Iterator it = this.mUndoModels.iterator();
        while (it.hasNext()) {
            mList.add(((UndoModel) it.next()).cloneData());
        }
        return mList;
    }

    public void recyclerTextBitmap() {
        if (this.textModelList != null && this.textModelList.size() > 0) {
            Iterator it = this.textModelList.iterator();
            while (it.hasNext()) {
                BitmapUtils.recycleBitmap(((LongVideosModel) it.next()).getBitmap());
            }
        }
    }

    public void recyclePicBitmap() {
        Iterator it;
        if (TextUtil.isValidate(this.videosModelList)) {
            it = this.videosModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (model.mediaType == 2) {
                    BitmapUtils.recycleBitmap(model.getBitmap());
                }
            }
        }
        if (TextUtil.isValidate(this.removeUndoModels)) {
            for (UndoModel model2 : this.removeUndoModels) {
                if (model2.type == 1) {
                    UndoEditModel editModel = model2.getEditModel();
                    List<LongVideosModel> videoModels = editModel.textModels;
                    List<LongVideosModel> textModels = editModel.videoModels;
                    if (TextUtil.isValidate(videoModels)) {
                        for (LongVideosModel model1 : videoModels) {
                            if (model1.mediaType == 2) {
                                BitmapUtils.recycleBitmap(model1.getBitmap());
                            }
                        }
                    }
                    if (TextUtil.isValidate(textModels)) {
                        for (LongVideosModel model12 : textModels) {
                            BitmapUtils.recycleBitmap(model12.getBitmap());
                        }
                    }
                }
            }
        }
    }

    public void judgeVideosNeedProxy() {
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            Iterator it = this.videosModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (model.mediaType == 0 && model.needProxyVideo()) {
                    model.startProxyVideo(false);
                }
            }
        }
    }

    public void updateProxyVideoCreationTime() {
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            Iterator it = this.videosModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (model.mediaType == 0) {
                    model.updateProxyCreationDate();
                    ProxyVideoManager.getInstance().updateProxySaveInfo(model.getProxyVideoCreateTime(), model.getPlaylistMediaPath());
                }
            }
        }
    }

    public void stopAllProxyVideo() {
        if (this.videosModelList != null && this.videosModelList.size() > 0) {
            Iterator it = this.videosModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (model.mediaType == 0) {
                    model.stopAllClient();
                }
            }
        }
    }
}