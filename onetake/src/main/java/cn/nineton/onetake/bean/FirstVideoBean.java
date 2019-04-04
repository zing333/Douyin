package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FirstVideoBean implements Parcelable {
    public static final Creator<FirstVideoBean> CREATOR = new Creator<FirstVideoBean>() {
        public FirstVideoBean createFromParcel(Parcel in) {
            return new FirstVideoBean(in);
        }

        public FirstVideoBean[] newArray(int size) {
            return new FirstVideoBean[size];
        }
    };
    private int rotation;
    private int videoHeight;
    private int videoWidth;
    public FirstVideoBean(){}
    protected FirstVideoBean(Parcel in) {
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.rotation = in.readInt();
    }

    public FirstVideoBean cloneData() {
        FirstVideoBean bean = new FirstVideoBean();
        bean.videoWidth = this.videoWidth;
        bean.videoHeight = this.videoHeight;
        bean.rotation = this.rotation;
        return bean;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeInt(this.rotation);
    }

    public int describeContents() {
        return 0;
    }

    public int getVideoWidth() {
        return this.videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return this.videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getWidthByRotation() {
        if (this.rotation == 90 || this.rotation == 270) {
            return this.videoHeight;
        }
        return this.videoWidth;
    }

    public int getHeightByRotation() {
        if (this.rotation == 90 || this.rotation == 270) {
            return this.videoWidth;
        }
        return this.videoHeight;
    }
}