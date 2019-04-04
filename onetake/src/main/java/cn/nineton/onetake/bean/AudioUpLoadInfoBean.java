package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;

public class AudioUpLoadInfoBean implements Parcelable {
    public static final Creator<AudioUpLoadInfoBean> CREATOR = new Creator<AudioUpLoadInfoBean>() {
        public AudioUpLoadInfoBean createFromParcel(Parcel source) {
            return new AudioUpLoadInfoBean(source);
        }

        public AudioUpLoadInfoBean[] newArray(int size) {
            return new AudioUpLoadInfoBean[size];
        }
    };
    private int bpm;
    private String duration;
    private int full_size;
    private int id;
    private String performer;
    private String poi;
    private String start;
    private String title;
    private String title_s;

    public static AudioUpLoadInfoBean objectFromData(String str) {
        return (AudioUpLoadInfoBean) new Gson().fromJson(str, AudioUpLoadInfoBean.class);
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTitle_s() {
        return this.title_s;
    }

    public void setTitle_s(String title_s) {
        this.title_s = title_s;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBpm() {
        return this.bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPerformer() {
        return this.performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public int getFull_size() {
        return this.full_size;
    }

    public void setFull_size(int full_size) {
        this.full_size = full_size;
    }

    public String getPoi() {
        return this.poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start);
        dest.writeString(this.title_s);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.bpm);
        dest.writeString(this.duration);
        dest.writeString(this.performer);
        dest.writeInt(this.full_size);
        dest.writeString(this.poi);
    }

    public AudioUpLoadInfoBean(){}
    protected AudioUpLoadInfoBean(Parcel in) {
        this.start = in.readString();
        this.title_s = in.readString();
        this.id = in.readInt();
        this.title = in.readString();
        this.bpm = in.readInt();
        this.duration = in.readString();
        this.performer = in.readString();
        this.full_size = in.readInt();
        this.poi = in.readString();
    }
}