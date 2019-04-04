package cn.nineton.onetake.bean;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.VideoTools.GPUImageLookupFilter2;
import com.google.gson.annotations.SerializedName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageLookupFilter2;
import cn.nineton.onetake.util.Config;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class FilterInfo implements Parcelable {
    public static final Creator<FilterInfo> CREATOR = new Creator<FilterInfo>() {
        public FilterInfo createFromParcel(Parcel in) {
            return new FilterInfo(in);
        }

        public FilterInfo[] newArray(int size) {
            return new FilterInfo[size];
        }
    };
    public Bitmap bitmap;
    @SerializedName("package")
    public String color;
    public int count;
    public String filename;
    public GPUImageFilter filter;
    public boolean filterIsChoose;
    public int filter_id;
    public String group;
    public String groupNameChs;
    public String groupNameCht;
    public String groupNameEn;
    public boolean isBnW;
    public boolean isCollected;
    public boolean isDownloadFromStore;
    public boolean isDownloading;
    public boolean isGroup;
    public String name_chs;
    public String name_cht;
    public String name_en;
    public String originalGroupChs;
    public String originalGroupCht;
    public String originalGroupEn;
    public boolean selected;
    public int type;
    public int visible;

    private FilterInfo() {
        this.isDownloading = false;
        this.isBnW = false;
    }

    protected FilterInfo(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.isDownloading = false;
        this.isBnW = false;
        this.color = in.readString();
        this.visible = in.readInt();
        this.isGroup = in.readByte() != (byte) 0;
        this.group = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.selected = z;
        this.bitmap = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
        this.name_chs = in.readString();
        this.name_cht = in.readString();
        this.name_en = in.readString();
        this.filename = in.readString();
        this.groupNameChs = in.readString();
        this.groupNameCht = in.readString();
        this.groupNameEn = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isCollected = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isDownloading = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isDownloadFromStore = z;
        this.filter_id = in.readInt();
        this.type = in.readInt();
        this.originalGroupEn = in.readString();
        this.originalGroupChs = in.readString();
        this.originalGroupCht = in.readString();
        this.count = in.readInt();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isBnW = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.filterIsChoose = z2;
    }

    public FilterInfo cloneInfo() {
        FilterInfo info = new FilterInfo();
        info.color = this.color;
        info.filterIsChoose = this.filterIsChoose;
        info.originalGroupEn = this.originalGroupEn;
        info.originalGroupChs = this.originalGroupChs;
        info.originalGroupCht = this.originalGroupCht;
        info.count = this.count;
        info.isBnW = this.isBnW;
        info.type = this.type;
        info.filter_id = this.filter_id;
        info.isDownloadFromStore = this.isDownloadFromStore;
        info.isDownloading = this.isDownloading;
        info.isCollected = this.isCollected;
        info.groupNameEn = this.groupNameEn;
        info.groupNameCht = this.groupNameCht;
        info.groupNameChs = this.groupNameChs;
        info.filename = this.filename;
        info.name_en = this.name_en;
        info.name_cht = this.name_cht;
        info.name_chs = this.name_chs;
        info.selected = this.selected;
        info.group = this.group;
        info.isGroup = this.isGroup;
        info.visible = this.visible;
        return info;
    }

    public FilterInfo(String name_chs, String name_cht, String name_en, String filename, int filter_id) {
        this.isDownloading = false;
        this.isBnW = false;
        this.name_chs = name_chs;
        this.name_cht = name_cht;
        this.name_en = name_en;
        this.filename = filename;
        this.isGroup = false;
        this.filter_id = filter_id;
        this.isBnW = false;
    }

    public FilterInfo(String name_chs, String name_cht, String name_en, GPUImageFilter filter, boolean selected) {
        this.isDownloading = false;
        this.isBnW = false;
        this.name_chs = name_chs;
        this.name_cht = name_cht;
        this.name_en = name_en;
        this.selected = selected;
        this.filter = filter;
        this.group = "none";
        this.groupNameEn = name_en;
        this.isBnW = false;
    }

    public FilterInfo(String groupName) {
        this.isDownloading = false;
        this.isBnW = false;
        this.isGroup = true;
        this.filename = groupName;
    }

    public FilterInfo(FilterInfo filterInfo) {
        this.isDownloading = false;
        this.isBnW = false;
        this.name_chs = filterInfo.name_chs;
        this.name_cht = filterInfo.name_cht;
        this.name_en = filterInfo.name_en;
        this.filename = filterInfo.filename;
        this.bitmap = filterInfo.bitmap;
        this.filter = filterInfo.filter;
        this.group = filterInfo.group;
        this.visible = filterInfo.visible;
        this.selected = filterInfo.selected;
        this.filter_id = filterInfo.filter_id;
        this.isDownloadFromStore = filterInfo.isDownloadFromStore;
        this.isBnW = false;
    }

    public GPUImageFilter createFilterInstance(AssetManager assets) {
        FileNotFoundException e;
        if (this.filename == null) {
            return new GPUImageFilter();
        }
        if (this.isDownloadFromStore) {
            try {
                InputStream inputStream = new FileInputStream(Config.getFiltersDownloadPath(this.filter_id) + "/" + this.filename + ".dat");
                return new GPUImageLookupFilter2(inputStream);
            } catch (FileNotFoundException e3) {
                e = e3;
                e.printStackTrace();
                return new GPUImageFilter();
            }
        }
        try {
            return new GPUImageLookupFilter2(assets.open("dat/" + this.filename + ".dat"));
        } catch (IOException e4) {
            e4.printStackTrace();
            return new GPUImageFilter();
        }
    }

    public boolean equailFilterGroup(FilterInfo filterInfo) {
        if (filterInfo != null && this.name_chs != null && this.name_cht != null && this.name_en != null && this.filename != null && this.name_chs.equals(filterInfo.name_chs) && this.name_cht.equals(filterInfo.name_cht) && this.name_en.equals(filterInfo.name_en) && this.filename.equals(filterInfo.filename) && this.filter_id == filterInfo.filter_id) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "FilterInfo{color='" + this.color + '\'' + ", visible=" + this.visible + ", isGroup=" + this.isGroup + ", group='" + this.group + '\'' + ", filter=" + this.filter + ", selected=" + this.selected + ", bitmap=" + this.bitmap + ", name_chs='" + this.name_chs + '\'' + ", name_cht='" + this.name_cht + '\'' + ", name_en='" + this.name_en + '\'' + ", filename='" + this.filename + '\'' + ", groupNameChs='" + this.groupNameChs + '\'' + ", groupNameCht='" + this.groupNameCht + '\'' + ", groupNameEn='" + this.groupNameEn + '\'' + ", isCollected=" + this.isCollected + ", isDownloading=" + this.isDownloading + ", isDownloadFromStore=" + this.isDownloadFromStore + ", filter_id=" + this.filter_id + ", type=" + this.type + ", originalGroupEn='" + this.originalGroupEn + '\'' + ", originalGroupChs='" + this.originalGroupChs + '\'' + ", originalGroupCht='" + this.originalGroupCht + '\'' + ", count=" + this.count + ", isBnW=" + this.isBnW + ", filterIsChoose=" + this.filterIsChoose + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.color);
        dest.writeInt(this.visible);
        dest.writeByte((byte) (this.isGroup ? 1 : 0));
        dest.writeString(this.group);
        if (this.selected) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeString(this.name_chs);
        dest.writeString(this.name_cht);
        dest.writeString(this.name_en);
        dest.writeString(this.filename);
        dest.writeString(this.groupNameChs);
        dest.writeString(this.groupNameCht);
        dest.writeString(this.groupNameEn);
        if (this.isCollected) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.isDownloading) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.isDownloadFromStore) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeInt(this.filter_id);
        dest.writeInt(this.type);
        dest.writeString(this.originalGroupEn);
        dest.writeString(this.originalGroupChs);
        dest.writeString(this.originalGroupCht);
        dest.writeInt(this.count);
        if (this.isBnW) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.filterIsChoose) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
    }
}