package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyImagesBean implements Parcelable {
    public static final Creator<GiphyImagesBean> CREATOR = new Creator<GiphyImagesBean>() {
        public GiphyImagesBean createFromParcel(Parcel source) {
            return new GiphyImagesBean(source);
        }

        public GiphyImagesBean[] newArray(int size) {
            return new GiphyImagesBean[size];
        }
    };
    public GiphyImageInfoBean downsized;
    public GiphyImageInfoBean fixed_height_downsampled;
    public GiphyImageInfoBean fixed_width_downsampled;
    public GiphyImageInfoBean original;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.fixed_width_downsampled, flags);
        dest.writeParcelable(this.fixed_height_downsampled, flags);
        dest.writeParcelable(this.downsized, flags);
        dest.writeParcelable(this.original, flags);
    }

    protected GiphyImagesBean(Parcel in) {
        this.fixed_width_downsampled = (GiphyImageInfoBean) in.readParcelable(GiphyImageInfoBean.class.getClassLoader());
        this.fixed_height_downsampled = (GiphyImageInfoBean) in.readParcelable(GiphyImageInfoBean.class.getClassLoader());
        this.downsized = (GiphyImageInfoBean) in.readParcelable(GiphyImageInfoBean.class.getClassLoader());
        this.original = (GiphyImageInfoBean) in.readParcelable(GiphyImageInfoBean.class.getClassLoader());
    }
}