package cn.nineton.onetake.event;

public class VideoPreviewEvent {
    public long videoProgressTag;

    public VideoPreviewEvent(long videoProgressTag) {
        this.videoProgressTag = videoProgressTag;
    }
}