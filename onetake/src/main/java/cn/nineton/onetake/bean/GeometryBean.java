package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GeometryBean implements Parcelable {
    public static final Creator<GeometryBean> CREATOR = new Creator<GeometryBean>() {
        public GeometryBean createFromParcel(Parcel source) {
            return new GeometryBean(source);
        }

        public GeometryBean[] newArray(int size) {
            return new GeometryBean[size];
        }
    };
    public BoundsBean bounds;
    public LatLonInfoBean location;
    public String location_type;
    public BoundsBean viewport;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bounds, flags);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.location_type);
        dest.writeParcelable(this.viewport, flags);
    }

    protected GeometryBean(Parcel in) {
        this.bounds = (BoundsBean) in.readParcelable(BoundsBean.class.getClassLoader());
        this.location = (LatLonInfoBean) in.readParcelable(LatLonInfoBean.class.getClassLoader());
        this.location_type = in.readString();
        this.viewport = (BoundsBean) in.readParcelable(BoundsBean.class.getClassLoader());
    }
}