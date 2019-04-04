package cn.nineton.onetake.event;

public class OfficialTagListEvent {
    public static final int NET_TAG_TYPE_FINISH = 4;
    public static final int RECOMMEND_TAG_TYPE_EXIF = 3;
    public static final int RECOMMEND_TAG_TYPE_EXIF_FAIL = -3;
    public static final int RECOMMEND_TAG_TYPE_GPS = 2;
    public static final int RECOMMEND_TAG_TYPE_GPS_FAIL = -2;
    public static final int RECOMMEND_TAG_TYPE_MSCV = 1;
    public static final int RECOMMEND_TAG_TYPE_MSCV_FAIL = -1;
    private int recommnedTagType;

    public OfficialTagListEvent(int recommnedTagType) {
        this.recommnedTagType = recommnedTagType;
    }

    public int getRecommnedTagType() {
        return this.recommnedTagType;
    }
}