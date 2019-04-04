package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyPaginationBean implements Parcelable {
    public static final Creator<GiphyPaginationBean> CREATOR = new Creator<GiphyPaginationBean>() {
        public GiphyPaginationBean createFromParcel(Parcel source) {
            return new GiphyPaginationBean(source);
        }

        public GiphyPaginationBean[] newArray(int size) {
            return new GiphyPaginationBean[size];
        }
    };
    public int count;
    public int offset;
    public int total_count;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_count);
        dest.writeInt(this.count);
        dest.writeInt(this.offset);
    }

    protected GiphyPaginationBean(Parcel in) {
        this.total_count = in.readInt();
        this.count = in.readInt();
        this.offset = in.readInt();
    }
}