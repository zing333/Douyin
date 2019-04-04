package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BoundsBean implements Parcelable {
    public static final Creator<BoundsBean> CREATOR = new Creator<BoundsBean>() {
        public BoundsBean createFromParcel(Parcel source) {
            return new BoundsBean(source);
        }

        public BoundsBean[] newArray(int size) {
            return new BoundsBean[size];
        }
    };
    public LatLonInfoBean northeast;
    public LatLonInfoBean southwest;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.northeast, flags);
        dest.writeParcelable(this.southwest, flags);
    }

    protected BoundsBean(Parcel in) {
        this.northeast = (LatLonInfoBean) in.readParcelable(LatLonInfoBean.class.getClassLoader());
        this.southwest = (LatLonInfoBean) in.readParcelable(LatLonInfoBean.class.getClassLoader());
    }
}