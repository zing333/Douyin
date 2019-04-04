package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyImageInfoBean implements Parcelable {
    public static final Creator<GiphyImageInfoBean> CREATOR = new Creator<GiphyImageInfoBean>() {
        public GiphyImageInfoBean createFromParcel(Parcel source) {
            return new GiphyImageInfoBean(source);
        }

        public GiphyImageInfoBean[] newArray(int size) {
            return new GiphyImageInfoBean[size];
        }
    };
    public String frames;
    public String height;
    public String mp4;
    public String mp4_size;
    public String size;
    public String url;
    public String webp;
    public String webp_size;
    public String width;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.width);
        dest.writeString(this.height);
        dest.writeString(this.size);
        dest.writeString(this.frames);
        dest.writeString(this.mp4);
        dest.writeString(this.mp4_size);
        dest.writeString(this.webp);
        dest.writeString(this.webp_size);
    }

    protected GiphyImageInfoBean(Parcel in) {
        this.url = in.readString();
        this.width = in.readString();
        this.height = in.readString();
        this.size = in.readString();
        this.frames = in.readString();
        this.mp4 = in.readString();
        this.mp4_size = in.readString();
        this.webp = in.readString();
        this.webp_size = in.readString();
    }
}