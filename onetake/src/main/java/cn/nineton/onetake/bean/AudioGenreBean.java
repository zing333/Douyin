package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;
import java.util.ArrayList;

public class AudioGenreBean implements Parcelable {
    public static final Creator<AudioGenreBean> CREATOR = new Creator<AudioGenreBean>() {
        public AudioGenreBean createFromParcel(Parcel source) {
            return new AudioGenreBean(source);
        }

        public AudioGenreBean[] newArray(int size) {
            return new AudioGenreBean[size];
        }
    };
    private ArrayList<AudioAlbumBean> albums;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<AudioAlbumBean> getAlbums() {
        return this.albums;
    }

    public void setAlbums(ArrayList<AudioAlbumBean> albums) {
        this.albums = albums;
    }

    public static AudioGenreBean objectFromData(String str) {
        return (AudioGenreBean) new Gson().fromJson(str, AudioGenreBean.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeTypedList(this.albums);
    }

    protected AudioGenreBean(Parcel in) {
        this.type = in.readString();
        this.albums = in.createTypedArrayList(AudioAlbumBean.CREATOR);
    }
}