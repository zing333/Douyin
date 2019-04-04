package cn.nineton.onetake.bean;

import android.os.Parcel;
import java.io.Serializable;

public class DraftInfoBean implements Serializable {
    public static final int DRAFTINFOBEAN_VALID_VERSION = 33;
    public static final int DRAFT_LONG_VIDEO_VERSION = 44;
    public String timestamp;
    public int version;
    public int vtype;

    public DraftInfoBean() {
        this.version = 33;
    }

    protected DraftInfoBean(Parcel in) {
        this.version = in.readInt();
        this.timestamp = in.readString();
        this.vtype = in.readInt();
    }
}