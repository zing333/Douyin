package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TimeLineBadgeBean implements Parcelable {
    public static final Creator<TimeLineBadgeBean> CREATOR = new Creator<TimeLineBadgeBean>() {
        public TimeLineBadgeBean createFromParcel(Parcel in) {
            return new TimeLineBadgeBean(in);
        }

        public TimeLineBadgeBean[] newArray(int size) {
            return new TimeLineBadgeBean[size];
        }
    };
    public int amount;
    public String image_url;
    public String name_cn;
    public String name_en;
    public String reason_cn;
    public String reason_en;

    public TimeLineBadgeBean(String image_url, String name_cn, String name_en, String reason_cn, String reason_en, int amount) {
        this.image_url = image_url;
        this.name_cn = name_cn;
        this.name_en = name_en;
        this.reason_cn = reason_cn;
        this.reason_en = reason_en;
        this.amount = amount;
    }

    protected TimeLineBadgeBean(Parcel in) {
        this.image_url = in.readString();
        this.name_cn = in.readString();
        this.name_en = in.readString();
        this.reason_cn = in.readString();
        this.reason_en = in.readString();
        this.amount = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image_url);
        dest.writeString(this.name_cn);
        dest.writeString(this.name_en);
        dest.writeString(this.reason_cn);
        dest.writeString(this.reason_en);
        dest.writeInt(this.amount);
    }

    public int describeContents() {
        return 0;
    }
}