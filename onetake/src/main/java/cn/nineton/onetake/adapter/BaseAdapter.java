package cn.nineton.onetake.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;

public abstract class BaseAdapter<VH extends ViewHolder> extends Adapter<VH> {

    public interface Item {
        public static final int ALBUM_MODEL_TYPE = 222;
        public static final int BANNER_TYPE = 240;
        public static final int CHOOSE_LOC_STAUTS_TYPE = 234;
        public static final int COLLECTION_DETAIL_HEAD_TYPE = 262;
        public static final int CONTENT_LAYOUT_TYPE = 500;
        public static final int DIAMOND_PURCHASE_CONTENT_TYPE = 504;
        public static final int DIAMOND_PURCHASE_HEAD_TYPE = 502;
        public static final int DISCOVER_COLLECTION_TYPE = 252;
        public static final int DISCOVER_EDITOR_COLLECTION = 501;
        public static final int DISCOVER_SELECT_TITLE_TYPE = 254;
        public static final int DISCOVER_SINGLE_PHOTO_TYPE = 220;
        public static final int DISCOVER_SPECIAL_TOPIC_TYPE = 239;
        public static final int DISCOVER_TAG_TYPE = 212;
        public static final int FOOTER_TYPE = 224;
        public static final int HEADER_BAR = 241;
        public static final int HEADER_TYPE = 202;
        public static final int HEAD_CELL_TYPE = 228;
        public static final int HEAD_CITY_TYPE = 230;
        public static final int HEAD_LINE_TYPE = 232;
        public static final int HIDE_TYPE = 204;
        public static final int INFO_TYPE = 208;
        public static final int LINE_TYPE = 206;
        public static final int LOGIN_BUTTON_TYPE = 236;
        public static final int NEARBY_GRID_TYPE = 244;
        public static final int NEARBY_LOADING_TYPE = 246;
        public static final int NEARBY_TITLE_TYPE = 242;
        public static final int NEW_TAG_HEADER_TYPE = 248;
        public static final int NO_DATA_TYPE = 226;
        public static final int PICTURE_SYNTHESIS_ENTER_TYPE = 388;
        public static final int SPECIAL_TOPIC_TYPE = 238;
        public static final int SUGGEST_USERS_TYPE = 264;
        public static final int TIMELINE_EMPTY_TYPE = 250;
        public static final int TYPE_BIG_PHOTO = 6;
        public static final int TYPE_ERROR = -1;
        public static final int TYPE_FOOTER = 11;
        public static final int TYPE_GRID = 4;
        public static final int TYPE_HEADER = 10;
        public static final int TYPE_LOADING = 5;
        public static final int TYPE_NORMAR = 2;
        public static final int TYPE_TITLE = 3;
        public static final int VIDEO_EDIT_ADD_TYPE = 276;
        public static final int VIDEO_EDIT_AUDIO_ADD_TYPE = 294;
        public static final int VIDEO_EDIT_AUDIO_EDIT_TYPE = 292;
        public static final int VIDEO_EDIT_AUDIO_EMPTY_TYPE = 288;
        public static final int VIDEO_EDIT_AUDIO_ICON_TYPE = 284;
        public static final int VIDEO_EDIT_AUDIO_MUTE_TYPE = 283;
        public static final int VIDEO_EDIT_AUDIO_TYPE = 282;
        public static final int VIDEO_EDIT_EMPTY_TYPE = 278;
        public static final int VIDEO_EDIT_IMAGE_MUTE_TYPE = 275;
        public static final int VIDEO_EDIT_IMAGE_TYPE = 274;
        public static final int VIDEO_EDIT_MUSIC_EDIT_TYPE = 290;
        public static final int VIDEO_EDIT_TEXT_TYPE = 286;
        public static final int VIDEO_EDIT_TIME_TYPE = 280;
        public static final int VIDEO_MUSIC_DETAIL_HEAD_TYPE = 270;
        public static final int VIDEO_MUSIC_DETAIL_TYPE = 272;
        public static final int VIDEO_MUSIC_INDEX_TITLE_BUTTONS = 267;
        public static final int VIDEO_MUSIC_INDEX_TITLE_TYPE = 268;
        public static final int VIDEO_MUSIC_INDEX_TYPE = 266;
        public static final int VIDEO_MUSIC_INDEX_WHITE_LINE = 269;
        public static final int WORTH_COLLECTION_HEADER_TYPE = 256;
        public static final int WORTH_FOLLOW_COLLECTION_TITLE_TYPE = 260;
        public static final int WORTH_FOLLOW_COLLECTION_TYPE = 258;
    }
}