package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

public class GMapPOIResultBean implements Parcelable {
    public static final Creator<GMapPOIResultBean> CREATOR = new Creator<GMapPOIResultBean>() {
        public GMapPOIResultBean createFromParcel(Parcel source) {
            return new GMapPOIResultBean(source);
        }

        public GMapPOIResultBean[] newArray(int size) {
            return new GMapPOIResultBean[size];
        }
    };
    public GeometryBean geometry;
    public String icon;
    public String id;
    public String name;
    public String place_id;
    public String reference;
    public String scope;
    public List<String> types;
    public String vicinity;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.geometry, flags);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.place_id);
        dest.writeString(this.reference);
        dest.writeString(this.scope);
        dest.writeStringList(this.types);
        dest.writeString(this.vicinity);
    }

    protected GMapPOIResultBean(Parcel in) {
        this.geometry = (GeometryBean) in.readParcelable(GeometryBean.class.getClassLoader());
        this.icon = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.place_id = in.readString();
        this.reference = in.readString();
        this.scope = in.readString();
        this.types = in.createStringArrayList();
        this.vicinity = in.readString();
    }
}