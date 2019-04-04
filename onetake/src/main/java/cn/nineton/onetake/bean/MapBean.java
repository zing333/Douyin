package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MapBean implements Parcelable {
    public static final Creator<MapBean> CREATOR = new Creator<MapBean>() {
        public MapBean createFromParcel(Parcel source) {
            return new MapBean(source);
        }

        public MapBean[] newArray(int size) {
            return new MapBean[size];
        }
    };
    public String city;
    public String country;
    public String district;
    public double latitude;
    public double longitude;
    public String province;
    public String streetName;
    public String streetNumber;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.district);
        dest.writeString(this.city);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.streetName);
        dest.writeString(this.streetNumber);
        dest.writeString(this.province);
    }

    protected MapBean(Parcel in) {
        this.country = in.readString();
        this.district = in.readString();
        this.city = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.streetName = in.readString();
        this.streetNumber = in.readString();
        this.province = in.readString();
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}