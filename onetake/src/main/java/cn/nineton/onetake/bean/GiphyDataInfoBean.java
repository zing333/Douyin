package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiphyDataInfoBean implements Parcelable {
    public static final Creator<GiphyDataInfoBean> CREATOR = new Creator<GiphyDataInfoBean>() {
        public GiphyDataInfoBean createFromParcel(Parcel source) {
            return new GiphyDataInfoBean(source);
        }

        public GiphyDataInfoBean[] newArray(int size) {
            return new GiphyDataInfoBean[size];
        }
    };
    public String bitly_gif_url;
    public String bitly_url;
    public String content_url;
    public String embed_url;
    public String id;
    public GiphyImagesBean images;
    public String import_datetime;
    public int is_indexable;
    public String rating;
    public String slug;
    public String source;
    public String source_post_url;
    public String source_tld;
    public String trending_datetime;
    public String type;
    public String url;
    public String username;

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiphyDataInfoBean that = (GiphyDataInfoBean) o;
        if (this.is_indexable != that.is_indexable) {
            return false;
        }
        if (this.type != null) {
            if (!this.type.equals(that.type)) {
                return false;
            }
        } else if (that.type != null) {
            return false;
        }
        if (this.id != null) {
            if (!this.id.equals(that.id)) {
                return false;
            }
        } else if (that.id != null) {
            return false;
        }
        if (this.slug != null) {
            if (!this.slug.equals(that.slug)) {
                return false;
            }
        } else if (that.slug != null) {
            return false;
        }
        if (this.url != null) {
            if (!this.url.equals(that.url)) {
                return false;
            }
        } else if (that.url != null) {
            return false;
        }
        if (this.bitly_gif_url != null) {
            if (!this.bitly_gif_url.equals(that.bitly_gif_url)) {
                return false;
            }
        } else if (that.bitly_gif_url != null) {
            return false;
        }
        if (this.bitly_url != null) {
            if (!this.bitly_url.equals(that.bitly_url)) {
                return false;
            }
        } else if (that.bitly_url != null) {
            return false;
        }
        if (this.embed_url != null) {
            if (!this.embed_url.equals(that.embed_url)) {
                return false;
            }
        } else if (that.embed_url != null) {
            return false;
        }
        if (this.username != null) {
            if (!this.username.equals(that.username)) {
                return false;
            }
        } else if (that.username != null) {
            return false;
        }
        if (this.source != null) {
            if (!this.source.equals(that.source)) {
                return false;
            }
        } else if (that.source != null) {
            return false;
        }
        if (this.rating != null) {
            if (!this.rating.equals(that.rating)) {
                return false;
            }
        } else if (that.rating != null) {
            return false;
        }
        if (this.content_url != null) {
            if (!this.content_url.equals(that.content_url)) {
                return false;
            }
        } else if (that.content_url != null) {
            return false;
        }
        if (this.source_tld != null) {
            if (!this.source_tld.equals(that.source_tld)) {
                return false;
            }
        } else if (that.source_tld != null) {
            return false;
        }
        if (this.source_post_url != null) {
            if (!this.source_post_url.equals(that.source_post_url)) {
                return false;
            }
        } else if (that.source_post_url != null) {
            return false;
        }
        if (this.import_datetime != null) {
            if (!this.import_datetime.equals(that.import_datetime)) {
                return false;
            }
        } else if (that.import_datetime != null) {
            return false;
        }
        if (this.trending_datetime != null) {
            if (!this.trending_datetime.equals(that.trending_datetime)) {
                return false;
            }
        } else if (that.trending_datetime != null) {
            return false;
        }
        if (this.images != null) {
            z = this.images.equals(that.images);
        } else if (that.images != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.type != null) {
            result = this.type.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.id != null) {
            hashCode = this.id.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.slug != null) {
            hashCode = this.slug.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.url != null) {
            hashCode = this.url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.bitly_gif_url != null) {
            hashCode = this.bitly_gif_url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.bitly_url != null) {
            hashCode = this.bitly_url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.embed_url != null) {
            hashCode = this.embed_url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.username != null) {
            hashCode = this.username.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.source != null) {
            hashCode = this.source.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.rating != null) {
            hashCode = this.rating.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.content_url != null) {
            hashCode = this.content_url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.source_tld != null) {
            hashCode = this.source_tld.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.source_post_url != null) {
            hashCode = this.source_post_url.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (((i2 + hashCode) * 31) + this.is_indexable) * 31;
        if (this.import_datetime != null) {
            hashCode = this.import_datetime.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.trending_datetime != null) {
            hashCode = this.trending_datetime.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.images != null) {
            i = this.images.hashCode();
        }
        return hashCode + i;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeString(this.slug);
        dest.writeString(this.url);
        dest.writeString(this.bitly_gif_url);
        dest.writeString(this.bitly_url);
        dest.writeString(this.embed_url);
        dest.writeString(this.username);
        dest.writeString(this.source);
        dest.writeString(this.rating);
        dest.writeString(this.content_url);
        dest.writeString(this.source_tld);
        dest.writeString(this.source_post_url);
        dest.writeInt(this.is_indexable);
        dest.writeString(this.import_datetime);
        dest.writeString(this.trending_datetime);
        dest.writeParcelable(this.images, flags);
    }

    protected GiphyDataInfoBean(Parcel in) {
        this.type = in.readString();
        this.id = in.readString();
        this.slug = in.readString();
        this.url = in.readString();
        this.bitly_gif_url = in.readString();
        this.bitly_url = in.readString();
        this.embed_url = in.readString();
        this.username = in.readString();
        this.source = in.readString();
        this.rating = in.readString();
        this.content_url = in.readString();
        this.source_tld = in.readString();
        this.source_post_url = in.readString();
        this.is_indexable = in.readInt();
        this.import_datetime = in.readString();
        this.trending_datetime = in.readString();
        this.images = (GiphyImagesBean) in.readParcelable(GiphyImagesBean.class.getClassLoader());
    }
}