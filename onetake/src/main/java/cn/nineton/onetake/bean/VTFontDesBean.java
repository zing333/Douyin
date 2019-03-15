package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.LocaleUtil;

public class VTFontDesBean implements Parcelable, Comparable<VTFontDesBean> {
    public static final Creator<VTFontDesBean> CREATOR = new Creator<VTFontDesBean>() {
        public VTFontDesBean createFromParcel(Parcel source) {
            return new VTFontDesBean(source);
        }

        public VTFontDesBean[] newArray(int size) {
            return new VTFontDesBean[size];
        }
    };
    public static final int STATUS_DOWNLOADED = 1;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_DOWNLOAD_FAILED = 4;
    public static final int STATUS_NEVER_DOWNLOAD = 0;
    public int borderwidth;
    public int capitalized;
    public String cnonly;
    @SerializedName("default")
    public int defaultX;
    public String filename;
    public String fonttype;
    public int invisible;
    public String language;
    public double lineheightoffset;
    public String name;
    public int progress;
    public boolean selected;
    public double size;
    public int status;
    public String url;

    public VTFontDesBean() {
    }

    public VTFontDesBean cloneData() {
        VTFontDesBean bean = new VTFontDesBean();
        bean.name = this.name;
        bean.filename = this.filename;
        bean.fonttype = this.fonttype;
        bean.language = this.language;
        bean.url = this.url;
        bean.status = this.status;
        bean.cnonly = this.cnonly;
        bean.size = this.size;
        bean.defaultX = this.defaultX;
        bean.borderwidth = this.borderwidth;
        bean.progress = this.progress;
        bean.capitalized = this.capitalized;
        bean.lineheightoffset = this.lineheightoffset;
        bean.selected = this.selected;
        bean.invisible = this.invisible;
        return bean;
    }

    public static ArrayList<VTFontDesBean> parseList(Reader reader, IExceptionCallback callback) {
        try {
            return JsonParserUtil.parseList(reader, new TypeToken<ArrayList<VTFontDesBean>>() {
            }.getType(), callback);
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static List<VTFontDesBean> parseList(String json, IExceptionCallback callback) {
        try {
            return JsonParserUtil.parseList(json, new TypeToken<ArrayList<VTFontDesBean>>() {
            }.getType(), callback);
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static String fromList(List<VTFontDesBean> list, IExceptionCallback callback) {
        try {
            return JsonParserUtil.fromList(list, callback);
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.filename);
        dest.writeString(this.fonttype);
        dest.writeString(this.language);
        dest.writeString(this.url);
        dest.writeInt(this.status);
        dest.writeString(this.cnonly);
        dest.writeInt(this.defaultX);
        dest.writeInt(this.progress);
        dest.writeInt(this.borderwidth);
        dest.writeInt(this.capitalized);
        dest.writeInt(this.invisible);
        dest.writeDouble(this.lineheightoffset);
        dest.writeByte((byte) (this.selected ? 1 : 0));
    }

    protected VTFontDesBean(Parcel in) {
        boolean z = true;
        this.name = in.readString();
        this.filename = in.readString();
        this.fonttype = in.readString();
        this.language = in.readString();
        this.url = in.readString();
        this.status = in.readInt();
        this.cnonly = in.readString();
        this.defaultX = in.readInt();
        this.progress = in.readInt();
        this.borderwidth = in.readInt();
        this.capitalized = in.readInt();
        this.invisible = in.readInt();
        this.lineheightoffset = in.readDouble();
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.selected = z;
    }

    public int compareTo(VTFontDesBean another) {
        if (LocaleUtil.isChineseCode()) {
            return this.language.charAt(0) - another.language.charAt(0);
        }
        return (-this.language.charAt(0)) + another.language.charAt(0);
    }
}