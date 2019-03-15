package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.album.AlbumInfoBean;
//import com.blink.academy.onetake.bean.discover.ArticleMeta;
//import com.blink.academy.onetake.bean.notification.NotificationBean;
//import com.blink.academy.onetake.bean.tag.OfficialTagBean;
//import com.blink.academy.onetake.bean.user.UserBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.model.DBLBSDataModel;
//import com.blink.academy.onetake.model.NewDBLBSDataModel;
//import com.blink.academy.onetake.support.utils.DateUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.util.DateUtil;
import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class TimelineBean implements Parcelable, Serializable, Cloneable {
    public static final Creator<TimelineBean> CREATOR = new Creator<TimelineBean>() {
        public TimelineBean createFromParcel(Parcel source) {
            return new TimelineBean(source);
        }

        public TimelineBean[] newArray(int size) {
            return new TimelineBean[size];
        }
    };
    public List<AlbumInfoBean> albums;
    public float alpha_on_discover;
    public String article_id;
    public String article_type;
    public String bot_coypright;
    public String bot_full_name;
    public String bot_id;
    public String bot_src;
    public int comments_count;
    public String created_at;
//    public DBLBSDataModel dblbsDataModel;
    public String discover_addr;
    public String discover_title;
    public String filter_id;
    public float filter_strongness;
    public int forks_count;
    public int frame_id;
    public String geo_json;
    public String geo_new;
    public String giphy_keyword;
    public int has_audio;
    public int height;
    public double height_on_discover;
    public String hone;
    public double hot_score;
    public String htwo;
    public int id;
    public String instagram_url;
    public boolean is_favorited;
    public boolean is_following;
    public boolean is_liked;
    public int is_recommend;
    public int is_scaned;
    public int join_count;
    public double lat;
    public String lbs_city_id;
    public String lbs_province_id;
    public long like_cursor;
    public int likes_count;
    public double lng;
    public String location_name_cn;
    public String location_name_en;
    public String long_thumbnail_url;
//    public ArticleMeta meta;
    public String name;
    public String name_en;
//    public NewDBLBSDataModel newDBLBSDataModel;
    public boolean only_self_visible;
    public int original_id;
    public ArrayList<OfficialTagBean> ots;
    public int photo_id;
    public List<TimelineBean> photos;
    public int photos_count;
    public int place_distance;
    public double place_lat;
    public double place_lng;
    public String place_name_cn;
    public String place_name_en;
    public String preview_ave_info;
    public String preview_url;
    public long published_at;
    public int radius;
    public List<String> roll_urls;
    public List<String> sample_urls;
    public String sticker_ids;
    public int story_id;
    public String tag_description;
    public String tag_description_en;
    public String text = "";
    public TimeLineBadgeBean timeline_badge;
//    public List<CommentBean> timeline_comments;
//    public List<LikeBean> timeline_likes;
    public String title;
    public int unmodified;
    public int unused_invite_code_count;
    public String updated_at;
    public boolean use_invite_code;
    public String user_avatar;
    public int user_gender;
    public int user_id;
    public List<Integer> user_ids;
    public String user_screen_name;
    public String user_time;
    public String user_twitter_name;
    public String user_weibo_name;
    public List<UserBean> users;
    public int vduration;
    public int video_source;
    public String video_url;
    public int vtype;
    public String web_url;
    public int width;
    public double width_on_discover;

    public void parseUserTime() {
        this.user_time = DateUtil.parseDates(this.created_at);
    }

    public String getUser_time() {
        return this.user_time;
    }

//    public DBLBSDataModel getDBLBSDataModel() {
//        if (this.dblbsDataModel == null) {
//            this.dblbsDataModel = DBLBSDataModel.parse(this.geo_json, null);
//        }
//        return this.dblbsDataModel;
//    }
//
//    public NewDBLBSDataModel getNewDBLBSDataModel() {
//        if (this.newDBLBSDataModel == null) {
//            this.newDBLBSDataModel = NewDBLBSDataModel.parse(this.geo_new);
//        }
//        return this.newDBLBSDataModel;
//    }

    public static TimelineBean parse(String jsonString, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (TimelineBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<TimelineBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static List<TimelineBean> parseList(String jsonString, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (List) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<List<TimelineBean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static TimelineBean toTimelineBean(NotificationBean notificationBean) {
        if (TextUtil.isNull((Object) notificationBean)) {
            return null;
        }
        TimelineBean timelineBean = new TimelineBean();
        timelineBean.user_screen_name = notificationBean.user_screen_name;
        timelineBean.user_avatar = notificationBean.user_avatar;
        timelineBean.created_at = notificationBean.created_at;
        timelineBean.width = notificationBean.photo_width;
        timelineBean.height = notificationBean.photo_height;
        timelineBean.preview_ave_info = notificationBean.photo_preview_ave_info;
        return timelineBean;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
//        dest.writeTypedList(this.timeline_likes);
//        dest.writeTypedList(this.timeline_comments);
        dest.writeInt(this.user_id);
        dest.writeInt(this.unmodified);
        dest.writeString(this.created_at);
        dest.writeString(this.user_time);
        dest.writeString(this.updated_at);
        dest.writeString(this.user_avatar);
        dest.writeString(this.user_screen_name);
        dest.writeString(this.user_weibo_name);
        dest.writeString(this.user_twitter_name);
        dest.writeInt(this.user_gender);
        dest.writeInt(this.original_id);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.forks_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.id);
        dest.writeString(this.text);
        dest.writeString(this.hone);
        dest.writeString(this.htwo);
        dest.writeString(this.filter_id);
        dest.writeString(this.sticker_ids);
        dest.writeByte(this.is_liked ? (byte) 1 : (byte) 0);
        if (this.is_following) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        dest.writeFloat(this.filter_strongness);
        dest.writeLong(this.published_at);
        dest.writeInt(this.story_id);
        dest.writeInt(this.frame_id);
        dest.writeString(this.video_url);
        dest.writeString(this.preview_url);
        dest.writeString(this.long_thumbnail_url);
        dest.writeString(this.preview_ave_info);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.geo_json);
        dest.writeByte(this.is_favorited ? (byte) 1 : (byte) 0);
        if (this.only_self_visible) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        dest.writeInt(this.is_recommend);
        dest.writeString(this.geo_new);
        dest.writeString(this.name);
        dest.writeString(this.name_en);
        dest.writeString(this.tag_description);
        dest.writeString(this.tag_description_en);
        dest.writeTypedList(this.photos);
        dest.writeString(this.place_name_cn);
        dest.writeString(this.place_name_en);
        dest.writeDouble(this.place_lat);
        dest.writeDouble(this.place_lng);
        dest.writeInt(this.place_distance);
        dest.writeInt(this.join_count);
        dest.writeInt(this.photos_count);
        dest.writeList(this.user_ids);
        dest.writeTypedList(this.users);
        dest.writeTypedList(this.albums);
        dest.writeParcelable(this.timeline_badge, flags);
//        dest.writeParcelable(this.dblbsDataModel, flags);
//        dest.writeParcelable(this.newDBLBSDataModel, flags);
        dest.writeDouble(this.hot_score);
        dest.writeString(this.lbs_city_id);
        dest.writeString(this.lbs_province_id);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.location_name_cn);
        dest.writeString(this.location_name_en);
        dest.writeInt(this.radius);
        dest.writeInt(this.photo_id);
        dest.writeInt(this.video_source);
        dest.writeDouble(this.width_on_discover);
        dest.writeDouble(this.height_on_discover);
        dest.writeString(this.article_id);
//        dest.writeParcelable(this.meta, flags);
        dest.writeString(this.web_url);
        dest.writeInt(this.unused_invite_code_count);
        if (!this.use_invite_code) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
        dest.writeString(this.giphy_keyword);
        dest.writeString(this.discover_addr);
        dest.writeLong(this.like_cursor);
        dest.writeTypedList(this.ots);
        dest.writeInt(this.vtype);
        dest.writeString(this.bot_src);
        dest.writeString(this.bot_id);
        dest.writeString(this.instagram_url);
        dest.writeString(this.bot_full_name);
        dest.writeString(this.bot_coypright);
        dest.writeInt(this.has_audio);
        dest.writeList(this.sample_urls);
        dest.writeList(this.roll_urls);
        dest.writeString(this.title);
        dest.writeInt(this.vduration);
        dest.writeString(this.discover_title);
        dest.writeInt(this.is_scaned);
        dest.writeString(this.article_type);
        dest.writeFloat(this.alpha_on_discover);
    }

    public TimelineBean(){}

    protected TimelineBean(Parcel in) {
        boolean z;
        boolean z2 = true;
//        this.timeline_likes = in.createTypedArrayList(LikeBean.CREATOR);
//        this.timeline_comments = in.createTypedArrayList(CommentBean.CREATOR);
        this.user_id = in.readInt();
        this.unmodified = in.readInt();
        this.created_at = in.readString();
        this.user_time = in.readString();
        this.updated_at = in.readString();
        this.user_avatar = in.readString();
        this.user_screen_name = in.readString();
        this.user_weibo_name = in.readString();
        this.user_twitter_name = in.readString();
        this.user_gender = in.readInt();
        this.original_id = in.readInt();
        this.likes_count = in.readInt();
        this.forks_count = in.readInt();
        this.comments_count = in.readInt();
        this.id = in.readInt();
        this.text = in.readString();
        this.hone = in.readString();
        this.htwo = in.readString();
        this.filter_id = in.readString();
        this.sticker_ids = in.readString();
        this.is_liked = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.is_following = z;
        this.filter_strongness = in.readFloat();
        this.published_at = in.readLong();
        this.story_id = in.readInt();
        this.frame_id = in.readInt();
        this.video_url = in.readString();
        this.preview_url = in.readString();
        this.long_thumbnail_url = in.readString();
        this.preview_ave_info = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.geo_json = in.readString();
        this.is_favorited = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.only_self_visible = z;
        this.is_recommend = in.readInt();
        this.geo_new = in.readString();
        this.name = in.readString();
        this.name_en = in.readString();
        this.tag_description = in.readString();
        this.tag_description_en = in.readString();
        this.photos = in.createTypedArrayList(CREATOR);
        this.place_name_cn = in.readString();
        this.place_name_en = in.readString();
        this.place_lat = in.readDouble();
        this.place_lng = in.readDouble();
        this.place_distance = in.readInt();
        this.join_count = in.readInt();
        this.photos_count = in.readInt();
        this.user_ids = new ArrayList();
        in.readList(this.user_ids, Integer.class.getClassLoader());
        this.users = in.createTypedArrayList(UserBean.CREATOR);
        this.albums = in.createTypedArrayList(AlbumInfoBean.CREATOR);
        this.timeline_badge = (TimeLineBadgeBean) in.readParcelable(TimeLineBadgeBean.class.getClassLoader());
//        this.dblbsDataModel = (DBLBSDataModel) in.readParcelable(DBLBSDataModel.class.getClassLoader());
//        this.newDBLBSDataModel = (NewDBLBSDataModel) in.readParcelable(NewDBLBSDataModel.class.getClassLoader());
        this.hot_score = in.readDouble();
        this.lbs_city_id = in.readString();
        this.lbs_province_id = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.location_name_cn = in.readString();
        this.location_name_en = in.readString();
        this.radius = in.readInt();
        this.photo_id = in.readInt();
        this.video_source = in.readInt();
        this.width_on_discover = in.readDouble();
        this.height_on_discover = in.readDouble();
        this.article_id = in.readString();
//        this.meta = (ArticleMeta) in.readParcelable(ArticleMeta.class.getClassLoader());
        this.web_url = in.readString();
        this.unused_invite_code_count = in.readInt();
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.use_invite_code = z2;
        this.giphy_keyword = in.readString();
        this.discover_addr = in.readString();
        this.like_cursor = in.readLong();
        this.ots = in.createTypedArrayList(OfficialTagBean.CREATOR);
        this.vtype = in.readInt();
        this.bot_src = in.readString();
        this.bot_id = in.readString();
        this.instagram_url = in.readString();
        this.bot_full_name = in.readString();
        this.bot_coypright = in.readString();
        this.has_audio = in.readInt();
        this.sample_urls = new ArrayList();
        in.readList(this.sample_urls, String.class.getClassLoader());
        this.roll_urls = new ArrayList();
        in.readList(this.roll_urls, String.class.getClassLoader());
        this.title = in.readString();
        this.vduration = in.readInt();
        this.discover_title = in.readString();
        this.is_scaned = in.readInt();
        this.article_type = in.readString();
        this.alpha_on_discover = in.readFloat();
    }
}