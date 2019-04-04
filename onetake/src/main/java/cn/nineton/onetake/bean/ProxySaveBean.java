package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProxySaveBean implements Parcelable {
    public static final Creator<ProxySaveBean> CREATOR = new Creator<ProxySaveBean>() {
        public ProxySaveBean createFromParcel(Parcel in) {
            return new ProxySaveBean(in);
        }

        public ProxySaveBean[] newArray(int size) {
            return new ProxySaveBean[size];
        }
    };
    String savePath;
    long saveTime;

    public ProxySaveBean(long saveTime, String savePath) {
        this.saveTime = saveTime;
        this.savePath = savePath;
    }

    protected ProxySaveBean(Parcel in) {
        this.saveTime = in.readLong();
        this.savePath = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.saveTime);
        dest.writeString(this.savePath);
    }

    public int describeContents() {
        return 0;
    }

    public long getSaveTime() {
        return this.saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}