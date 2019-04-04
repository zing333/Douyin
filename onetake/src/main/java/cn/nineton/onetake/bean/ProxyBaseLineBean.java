package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProxyBaseLineBean implements Parcelable {
    public static final Creator<ProxyBaseLineBean> CREATOR = new Creator<ProxyBaseLineBean>() {
        public ProxyBaseLineBean createFromParcel(Parcel in) {
            return new ProxyBaseLineBean(in);
        }

        public ProxyBaseLineBean[] newArray(int size) {
            return new ProxyBaseLineBean[size];
        }
    };
    private long fileCreateTime;
    private double proxyBaseLine;

    public ProxyBaseLineBean(long fileCreateTime, double proxyBaseLine) {
        this.fileCreateTime = fileCreateTime;
        this.proxyBaseLine = proxyBaseLine;
    }

    protected ProxyBaseLineBean(Parcel in) {
        this.fileCreateTime = in.readLong();
        this.proxyBaseLine = in.readDouble();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fileCreateTime);
        dest.writeDouble(this.proxyBaseLine);
    }

    public int describeContents() {
        return 0;
    }

    public long getFileCreateTime() {
        return this.fileCreateTime;
    }

    public void setFileCreateTime(long fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public double getProxyBaseLine() {
        return this.proxyBaseLine;
    }

    public void setProxyBaseLine(double proxyBaseLine) {
        this.proxyBaseLine = proxyBaseLine;
    }
}