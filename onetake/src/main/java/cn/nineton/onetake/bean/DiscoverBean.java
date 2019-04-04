package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscoverBean implements Parcelable {
    public static final Creator<DiscoverBean> CREATOR = new Creator<DiscoverBean>() {
        public DiscoverBean createFromParcel(Parcel source) {
            return new DiscoverBean(source);
        }

        public DiscoverBean[] newArray(int size) {
            return new DiscoverBean[size];
        }
    };
    public static final int DISCOVER_ALBUM_TYPE = 3;
    public static final int DISCOVER_CITY_TYPE = 4;
    public static final int DISCOVER_SINGLE_PHOTO_TYPE = 1;
    public static final int DISCOVER_SPECIAL_TOPIC_TYPE = 5;
    public static final int DISCOVER_TAG_TYPE = 2;
    public String created_at;
    public String explore_tag;
    public int id;
    public int is_publish;
    public TimelineBean item;
    public int item_id;
    public int item_type;
    public long published_at;
    public String updated_at;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.item_type);
        dest.writeInt(this.item_id);
        dest.writeInt(this.is_publish);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeLong(this.published_at);
        dest.writeString(this.explore_tag);
        dest.writeParcelable(this.item, flags);
    }

    protected DiscoverBean(Parcel in) {
        this.id = in.readInt();
        this.item_type = in.readInt();
        this.item_id = in.readInt();
        this.is_publish = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.published_at = in.readLong();
        this.explore_tag = in.readString();
        this.item = (TimelineBean) in.readParcelable(TimelineBean.class.getClassLoader());
    }
}