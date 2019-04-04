package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyElementModel implements Parcelable {
    public static final Creator<GiphyElementModel> CREATOR = new Creator<GiphyElementModel>() {
        public GiphyElementModel createFromParcel(Parcel source) {
            return new GiphyElementModel(source);
        }

        public GiphyElementModel[] newArray(int size) {
            return new GiphyElementModel[size];
        }
    };
    public static final String TYPE_FAV = "fav";
    public static final String TYPE_HOT = "hot";
    public static final String TYPE_KEYWORD = "keyword";
    public String cover_url;
    public String keyword;
    public String name;
    public String type;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.cover_url);
        dest.writeString(this.keyword);
        dest.writeString(this.type);
    }

    public GiphyElementModel(String name, String cover_url, String keyword, String type) {
        this.name = name;
        this.cover_url = cover_url;
        this.keyword = keyword;
        this.type = type;
    }

    protected GiphyElementModel(Parcel in) {
        this.name = in.readString();
        this.cover_url = in.readString();
        this.keyword = in.readString();
        this.type = in.readString();
    }
}