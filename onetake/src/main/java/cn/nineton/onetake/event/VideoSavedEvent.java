package cn.nineton.onetake.event;

public class VideoSavedEvent {
    public String mCurrentTimeStamp;
    public String mFilterId;
    public int mFrameHeight;
    public int mFrameWidth;
    public int mMaxFaceCount;
    public long videoProgressTag;

    public VideoSavedEvent(String currentTimeStamp, String filterId, int width, int height, int maxFaceCount, long videoProgressTag) {
        this.mCurrentTimeStamp = currentTimeStamp;
        this.mFilterId = filterId;
        this.mFrameWidth = width;
        this.mFrameHeight = height;
        this.mMaxFaceCount = maxFaceCount;
        this.videoProgressTag = videoProgressTag;
    }
}