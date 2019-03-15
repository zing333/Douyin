package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.io.Serializable;

import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.TextUtil;

public class OfficialTagBean implements Parcelable, Serializable {
    public static final Creator<OfficialTagBean> CREATOR = new Creator<OfficialTagBean>() {
        public OfficialTagBean createFromParcel(Parcel source) {
            return new OfficialTagBean(source);
        }

        public OfficialTagBean[] newArray(int size) {
            return new OfficialTagBean[size];
        }
    };
    public static final int TYPE_RECOMMEND_ID = -1000;
    public static final String TYPE_USER_INPUT = "usergenerate";
    public static final String TYPE_USER_PROMOTE = "promote";
    public static final String TYPE_USER_SPECIAL_PROMOTE = "promotes_at_last";
    private String article_id;
    private String comment;
    private String en;
    private String icon;
    private long id;
    private boolean join;
    private String type;
    private String value;
    private int width;
    private String zh;

    public String getArticle_id() {
        return this.article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isNotValid() {
        return TextUtils.isEmpty(this.value) && TextUtils.isEmpty(this.zh) && TextUtils.isEmpty(this.en);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrueValue() {
        return this.value;
    }

    public String getValue() {
        if (TYPE_USER_INPUT.equals(this.type)) {
            return this.value;
        }
        if (TextUtil.isNull(this.zh) && TextUtil.isNull(this.en)) {
            return this.value;
        }
        if (LocaleUtil.isChinese()) {
            if (TextUtil.isValidate(this.zh)) {
                return this.zh;
            }
            if (TextUtil.isValidate(this.en)) {
                return this.en;
            }
            return this.value;
        } else if (TextUtil.isValidate(this.en)) {
            return this.en;
        } else {
            if (TextUtil.isValidate(this.zh)) {
                return this.zh;
            }
            return this.value;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getZh() {
        return this.zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return this.en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getIcon() {
        return "";
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return "OfficialTagBean{type='" + this.type + '\'' + ", value='" + this.value + '\'' + ", comment='" + this.comment + '\'' + ", zh='" + this.zh + '\'' + ", en='" + this.en + '\'' + ", icon='" + this.icon + '\'' + ", id=" + this.id + '}';
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfficialTagBean bean = (OfficialTagBean) o;
        if (this.id != bean.id) {
            return false;
        }
        if (this.type != null) {
            if (!this.type.equals(bean.type)) {
                return false;
            }
        } else if (bean.type != null) {
            return false;
        }
        if (this.value != null) {
            if (!this.value.equals(bean.value)) {
                return false;
            }
        } else if (bean.value != null) {
            return false;
        }
        if (this.comment != null) {
            if (!this.comment.equals(bean.comment)) {
                return false;
            }
        } else if (bean.comment != null) {
            return false;
        }
        if (this.zh != null) {
            if (!this.zh.equals(bean.zh)) {
                return false;
            }
        } else if (bean.zh != null) {
            return false;
        }
        if (this.en != null) {
            if (!this.en.equals(bean.en)) {
                return false;
            }
        } else if (bean.en != null) {
            return false;
        }
        if (this.icon != null) {
            z = this.icon.equals(bean.icon);
        } else if (bean.icon != null) {
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
        if (this.value != null) {
            hashCode = this.value.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.comment != null) {
            hashCode = this.comment.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.zh != null) {
            hashCode = this.zh.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.en != null) {
            hashCode = this.en.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.icon != null) {
            i = this.icon.hashCode();
        }
        return ((hashCode + i) * 31) + ((int) (this.id ^ (this.id >>> 32)));
    }

    public OfficialTagBean() {
        this.type = TYPE_USER_INPUT;
    }

    public boolean isJoin() {
        return this.join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.value);
        dest.writeString(this.comment);
        dest.writeString(this.zh);
        dest.writeString(this.en);
        dest.writeString(this.icon);
        dest.writeLong(this.id);
        dest.writeInt(this.width);
        dest.writeString(this.article_id);
        dest.writeByte(this.join ? (byte) 1 : (byte) 0);
    }

    protected OfficialTagBean(Parcel in) {
        this.type = in.readString();
        this.value = in.readString();
        this.comment = in.readString();
        this.zh = in.readString();
        this.en = in.readString();
        this.icon = in.readString();
        this.id = in.readLong();
        this.width = in.readInt();
        this.article_id = in.readString();
        this.join = in.readByte() != (byte) 0;
    }
}