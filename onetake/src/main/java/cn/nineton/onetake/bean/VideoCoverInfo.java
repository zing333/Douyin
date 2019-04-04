package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VideoCoverInfo implements Parcelable {
    public static final Creator<VideoCoverInfo> CREATOR = new Creator<VideoCoverInfo>() {
        public VideoCoverInfo createFromParcel(Parcel in) {
            return new VideoCoverInfo(in);
        }

        public VideoCoverInfo[] newArray(int size) {
            return new VideoCoverInfo[size];
        }
    };
    public String uploadLongPath;
    public String uploadPreivewPath;
    public String uploadShortPath;
    public String uploadVideoPath;
    public long videoDuration;
    public int videoHeight;
    public int videoWidth;
    public VideoCoverInfo(){}
    protected VideoCoverInfo(Parcel in) {
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.videoDuration = in.readLong();
        this.uploadVideoPath = in.readString();
        this.uploadPreivewPath = in.readString();
        this.uploadLongPath = in.readString();
        this.uploadShortPath = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeLong(this.videoDuration);
        dest.writeString(this.uploadVideoPath);
        dest.writeString(this.uploadPreivewPath);
        dest.writeString(this.uploadLongPath);
        dest.writeString(this.uploadShortPath);
    }
}