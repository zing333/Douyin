package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VideoInputRatio implements Parcelable {
    public static final Creator<VideoInputRatio> CREATOR = new Creator<VideoInputRatio>() {
        public VideoInputRatio createFromParcel(Parcel in) {
            return new VideoInputRatio(in);
        }

        public VideoInputRatio[] newArray(int size) {
            return new VideoInputRatio[size];
        }
    };
    public int ratioHeight;
    public int ratioWidth;

    public VideoInputRatio(int height, int width) {
        this.ratioHeight = height;
        this.ratioWidth = width;
    }

    public float getRatio() {
        return (((float) this.ratioHeight) * 1.0f) / ((float) this.ratioWidth);
    }
    public VideoInputRatio(){}
    protected VideoInputRatio(Parcel in) {
        this.ratioHeight = in.readInt();
        this.ratioWidth = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ratioHeight);
        dest.writeInt(this.ratioWidth);
    }

    public int describeContents() {
        return 0;
    }
}