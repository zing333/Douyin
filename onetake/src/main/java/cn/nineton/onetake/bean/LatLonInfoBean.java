package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LatLonInfoBean implements Parcelable {
    public static final Creator<LatLonInfoBean> CREATOR = new Creator<LatLonInfoBean>() {
        public LatLonInfoBean createFromParcel(Parcel source) {
            return new LatLonInfoBean(source);
        }

        public LatLonInfoBean[] newArray(int size) {
            return new LatLonInfoBean[size];
        }
    };
    public double lat;
    public double lng;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    protected LatLonInfoBean(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }
}