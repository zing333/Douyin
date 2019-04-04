package cn.nineton.onetake.event;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoCoverInfo;

public class VideoSavingEvent {
    public VideoCoverInfo coverInfo;
    public LongVideosModel coverTextModel;
    public int mProgress;
    public String mProgressFrom;
    public VideoBean videoBean;
    public long videoProgressTag;

    public static class VideoBean {
        public int videoHeight;
        public String videoPath;
        public int videoWidth;
        public VideoBean(){}
    }
    public VideoSavingEvent(){}
    public VideoSavingEvent(int progress) {
        this.mProgress = progress;
    }

    public VideoSavingEvent(int progress, String progressFrom) {
        this.mProgress = progress;
        this.mProgressFrom = progressFrom;
    }

    public VideoSavingEvent(int progress, String progressFrom, long videoProgressTag) {
        this.mProgress = progress;
        this.mProgressFrom = progressFrom;
        this.videoProgressTag = videoProgressTag;
    }

    public VideoSavingEvent(String progressFrom) {
        this.mProgressFrom = progressFrom;
    }

    public VideoSavingEvent(String progressFrom, LongVideosModel textModel) {
        this.mProgressFrom = progressFrom;
        this.coverTextModel = textModel;
    }

    public VideoSavingEvent(int progress, String progressFrom, long videoProgressTag, VideoBean videoBean) {
        this.mProgress = progress;
        this.mProgressFrom = progressFrom;
        this.videoProgressTag = videoProgressTag;
        this.videoBean = videoBean;
    }
}