package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VideoAlbumEntity extends BaseEntity implements Parcelable {
    public static final Creator<VideoAlbumEntity> CREATOR = new Creator<VideoAlbumEntity>() {
        public VideoAlbumEntity createFromParcel(Parcel source) {
            return new VideoAlbumEntity(source);
        }

        public VideoAlbumEntity[] newArray(int size) {
            return new VideoAlbumEntity[size];
        }
    };
    private String localThumbnailPath;
    private int select;
    private long videoDataTime;
    private long videoDuration;
    private int videoHeight;
    private String videoLocation;
    private String videoName;
    private String videoPath;
    private String videoThumbnailPath;
    private int videoWidth;

    public VideoAlbumEntity(int viewtype) {
        super(viewtype);
    }

    public VideoAlbumEntity() {
        this(0);
    }

    public long getVideoDataTime() {
        return this.videoDataTime;
    }

    public String getVideoLocation() {
        return this.videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoThumbnailPath() {
        return this.videoThumbnailPath;
    }

    public void setVideoThumbnailPath(String videoThumbnailPath) {
        this.videoThumbnailPath = videoThumbnailPath;
    }

    public void setVideoThumbnailPath(String thumbnailPath, String localThumbnailPath) {
        this.videoThumbnailPath = thumbnailPath;
        this.localThumbnailPath = localThumbnailPath;
    }

    public String getLocalThumbnailPath() {
        return this.localThumbnailPath;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoDataTime(long videoDataTime) {
        this.videoDataTime = videoDataTime;
    }

    public long getVideoDuration() {
        return this.videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getVideoWidth() {
        return this.videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return this.videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getSelect() {
        return this.select;
    }

    public String toString() {
        return "VideoAlbumEntity{videoPath='" + this.videoPath + '\'' + ", videoThumbnailPath='" + this.videoThumbnailPath + '\'' + ", localThumbnailPath='" + this.localThumbnailPath + '\'' + ", videoName='" + this.videoName + '\'' + ", videoLocation='" + this.videoLocation + '\'' + ", videoDataTime=" + this.videoDataTime + ", videoDuration=" + this.videoDuration + ", videoWidth=" + this.videoWidth + ", videoHeight=" + this.videoHeight + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.videoPath);
        dest.writeString(this.videoThumbnailPath);
        dest.writeString(this.localThumbnailPath);
        dest.writeString(this.videoName);
        dest.writeString(this.videoLocation);
        dest.writeLong(this.videoDataTime);
        dest.writeLong(this.videoDuration);
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeInt(this.select);
    }

    protected VideoAlbumEntity(Parcel in) {
        super(in);
        this.videoPath = in.readString();
        this.videoThumbnailPath = in.readString();
        this.localThumbnailPath = in.readString();
        this.videoName = in.readString();
        this.videoLocation = in.readString();
        this.videoDataTime = in.readLong();
        this.videoDuration = in.readLong();
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.select = in.readInt();
    }
}