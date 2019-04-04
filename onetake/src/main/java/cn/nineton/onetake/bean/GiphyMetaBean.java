package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyMetaBean implements Parcelable {
    public static final Creator<GiphyMetaBean> CREATOR = new Creator<GiphyMetaBean>() {
        public GiphyMetaBean createFromParcel(Parcel source) {
            return new GiphyMetaBean(source);
        }

        public GiphyMetaBean[] newArray(int size) {
            return new GiphyMetaBean[size];
        }
    };
    public String msg;
    public String response_id;
    public int status;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.msg);
        dest.writeString(this.response_id);
    }

    protected GiphyMetaBean(Parcel in) {
        this.status = in.readInt();
        this.msg = in.readString();
        this.response_id = in.readString();
    }
}