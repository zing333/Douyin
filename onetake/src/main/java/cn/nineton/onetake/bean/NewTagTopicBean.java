package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class NewTagTopicBean implements Parcelable {
    public static final Creator<NewTagTopicBean> CREATOR = new Creator<NewTagTopicBean>() {
        public NewTagTopicBean createFromParcel(Parcel source) {
            return new NewTagTopicBean(source);
        }

        public NewTagTopicBean[] newArray(int size) {
            return new NewTagTopicBean[size];
        }
    };
    private int count;
    private String created_at;
    private String desc_en;
    private String desc_zh;
    private String en;
    private String icon;
    private int id;
    private String updated_at;
    private String value;
    private String zh;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc_en() {
        return this.desc_en;
    }

    public void setDesc_en(String desc_en) {
        this.desc_en = desc_en;
    }

    public String getDesc_zh() {
        return this.desc_zh;
    }

    public void setDesc_zh(String desc_zh) {
        this.desc_zh = desc_zh;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.zh);
        dest.writeString(this.en);
        dest.writeString(this.icon);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.value);
        dest.writeString(this.desc_en);
        dest.writeString(this.desc_zh);
        dest.writeInt(this.count);
    }

    protected NewTagTopicBean(Parcel in) {
        this.id = in.readInt();
        this.zh = in.readString();
        this.en = in.readString();
        this.icon = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.value = in.readString();
        this.desc_en = in.readString();
        this.desc_zh = in.readString();
        this.count = in.readInt();
    }

    public static NewTagTopicBean parse(String jsonString, IExceptionCallback callback) {
        if (!TextUtil.isValidate(jsonString)) {
            return null;
        }
        try {
            return (NewTagTopicBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<NewTagTopicBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }
}