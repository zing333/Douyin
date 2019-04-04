package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AudioAlbumBean extends AudioInfoBean implements Parcelable {
    public static final Creator<AudioAlbumBean> CREATOR = new Creator<AudioAlbumBean>() {
        public AudioAlbumBean createFromParcel(Parcel source) {
            return new AudioAlbumBean(source);
        }

        public AudioAlbumBean[] newArray(int size) {
            return new AudioAlbumBean[size];
        }
    };
    private AudioTrackBean first_track;

    public AudioTrackBean getFirst_track() {
        return this.first_track;
    }

    public void setFirst_track(AudioTrackBean first_track) {
        this.first_track = first_track;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.first_track, flags);
    }

    protected AudioAlbumBean(Parcel in) {
        super(in);
        this.first_track = (AudioTrackBean) in.readParcelable(AudioTrackBean.class.getClassLoader());
    }
}