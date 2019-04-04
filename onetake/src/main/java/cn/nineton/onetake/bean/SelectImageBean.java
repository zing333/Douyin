package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SelectImageBean implements Parcelable {
    public static final Creator<SelectImageBean> CREATOR = new Creator<SelectImageBean>() {
        public SelectImageBean createFromParcel(Parcel source) {
            return new SelectImageBean(source);
        }

        public SelectImageBean[] newArray(int size) {
            return new SelectImageBean[size];
        }
    };
    private int index;
    private boolean is_selected;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIs_selected() {
        return this.is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeByte(this.is_selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.index);
    }

    protected SelectImageBean(Parcel in) {
        this.url = in.readString();
        this.is_selected = in.readByte() != (byte) 0;
        this.index = in.readInt();
    }
}