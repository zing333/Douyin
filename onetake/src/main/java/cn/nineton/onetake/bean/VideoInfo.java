package cn.nineton.onetake.bean;

import java.io.Serializable;

/**
 * @Author: savion
 * @Date: 2019/3/5 19:18
 * @Des:
 **/
public class VideoInfo implements Serializable {
    private int width;
    private int height;
    private long duration;
    private String path;
    private String album;
    private boolean hasVideo;
    private boolean hasAudio;
    private int rotation  = -1;
    private String mime;

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {

        this.hasAudio = hasAudio;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "width=" + width +
                ", height=" + height +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", album='" + album + '\'' +
                ", hasVideo=" + hasVideo +
                ", hasAudio=" + hasAudio +
                ", rotation=" + rotation +
                '}';
    }
}
