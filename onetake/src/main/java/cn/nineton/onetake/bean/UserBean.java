package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class UserBean implements Parcelable, Serializable {
    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
    public String access_token;
    public int album_count;
    public String avatar;
    public TimeLineBadgeBean badge;
    public int badge_count;
    public boolean be_followed;
    public String birthday;
    public String bot_coypright;
    public String bot_full_name;
    public String bot_id;
    public String bot_src;
    private int city_count;
    private int country_count;
    private List<List<Double>> country_gps;
    public String cover_url;
    public String created_at;
    public String deactive;
    public String email;
    public String facebook_access_token;
    public String facebook_graph;
    public String facebook_id;
    public String facebook_screen_name;
    public int favorite_count;
    public int followers_count;
    public int following_count;
    public int gender;
    public boolean hide_change_name;
    public int hot_score;
    public int id;
    public boolean is_blocked;
    public boolean is_following;
    public boolean is_new;
    private LastAddrAndGpsBean last_addr_and_gps;
    public int last_notification_id;
    public String last_sign_in_at;
    public ArrayList<String> lbs_provinces;
    public int likes_count;
    public int login_type;
    public String lower_case_name;
    public int max_video_len;
    public int my_fans_count;
    public boolean need_bbtn;
    public String phone_number;
    public int photos_count;
    public long published_at;
    public String register_source;
    public String reset_password_at;
    public String screen_name;
    public List<TimelineBean> showcase_photos;
    public String signature;
    public String silence_ids;
    public ArrayList<TimeLineBadgeBean> top_badges;
    public String twitter_access_token;
    public String twitter_graph;
    public String twitter_id;
    public String twitter_name;
    public String twitter_screen_name;
    public int unused_invite_code_count;
    public String updated_at;
    public boolean use_invite_code;
    public int user_rank;
    public String website;
    public String weibo_access_token;
    public String weibo_graph;
    public String weibo_id;
    public String weibo_screen_name;
    public String weixin_union_id;

    public static class LastAddrAndGpsBean implements Parcelable {
        public static final Creator<LastAddrAndGpsBean> CREATOR = new Creator<LastAddrAndGpsBean>() {
            public LastAddrAndGpsBean createFromParcel(Parcel source) {
                return new LastAddrAndGpsBean(source);
            }

            public LastAddrAndGpsBean[] newArray(int size) {
                return new LastAddrAndGpsBean[size];
            }
        };
        private String addr;
        private List<Double> gps;

        public String getAddr() {
            return this.addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public List<Double> getGps() {
            return this.gps;
        }

        public void setGps(List<Double> gps) {
            this.gps = gps;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addr);
            dest.writeList(this.gps);
        }

        protected LastAddrAndGpsBean(Parcel in) {
            this.addr = in.readString();
            this.gps = new ArrayList();
            in.readList(this.gps, Double.class.getClassLoader());
        }
    }

    public static UserBean parse(String jsonString, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (UserBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<UserBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static List<UserBean> parseList(String jsonString, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (List) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<List<UserBean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public boolean equals(Object o) {
        return (o instanceof UserBean) && this.screen_name.equals(((UserBean) o).screen_name);
    }

    public int hashCode() {
        return this.screen_name.hashCode();
    }

    public int getCity_count() {
        return this.city_count;
    }

    public void setCity_count(int city_count) {
        this.city_count = city_count;
    }

    public int getCountry_count() {
        return this.country_count;
    }

    public void setCountry_count(int country_count) {
        this.country_count = country_count;
    }

    public LastAddrAndGpsBean getLast_addr_and_gps() {
        return this.last_addr_and_gps;
    }

    public void setLast_addr_and_gps(LastAddrAndGpsBean last_addr_and_gps) {
        this.last_addr_and_gps = last_addr_and_gps;
    }

    public List<List<Double>> getCountry_gps() {
        return this.country_gps;
    }

    public void setCountry_gps(List<List<Double>> country_gps) {
        this.country_gps = country_gps;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.likes_count);
        dest.writeInt(this.followers_count);
        dest.writeInt(this.following_count);
        dest.writeInt(this.favorite_count);
        dest.writeInt(this.gender);
        dest.writeInt(this.id);
        dest.writeInt(this.last_notification_id);
        dest.writeInt(this.login_type);
        dest.writeInt(this.photos_count);
        dest.writeInt(this.album_count);
        dest.writeInt(this.badge_count);
        dest.writeInt(this.unused_invite_code_count);
        dest.writeStringList(this.lbs_provinces);
        dest.writeTypedList(this.top_badges);
        dest.writeLong(this.published_at);
        dest.writeByte(this.be_followed ? (byte) 1 : (byte) 0);
        if (this.is_following) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (this.is_blocked) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (this.need_bbtn) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        dest.writeString(this.access_token);
        dest.writeString(this.avatar);
        dest.writeString(this.created_at);
        dest.writeString(this.deactive);
        dest.writeString(this.email);
        dest.writeString(this.birthday);
        dest.writeString(this.facebook_access_token);
        dest.writeString(this.facebook_graph);
        dest.writeString(this.facebook_id);
        dest.writeString(this.facebook_screen_name);
        dest.writeString(this.last_sign_in_at);
        dest.writeString(this.lower_case_name);
        dest.writeString(this.phone_number);
        dest.writeString(this.register_source);
        dest.writeString(this.reset_password_at);
        dest.writeString(this.screen_name);
        dest.writeString(this.signature);
        dest.writeString(this.twitter_access_token);
        dest.writeString(this.twitter_graph);
        dest.writeString(this.twitter_id);
        dest.writeString(this.twitter_name);
        dest.writeString(this.twitter_screen_name);
        dest.writeString(this.updated_at);
        dest.writeString(this.website);
        dest.writeString(this.weibo_access_token);
        dest.writeString(this.weibo_graph);
        dest.writeString(this.weibo_id);
        dest.writeString(this.weibo_screen_name);
        dest.writeString(this.weixin_union_id);
        dest.writeTypedList(this.showcase_photos);
        dest.writeString(this.cover_url);
        dest.writeString(this.silence_ids);
        dest.writeInt(this.user_rank);
        dest.writeByte(this.is_new ? (byte) 1 : (byte) 0);
        dest.writeInt(this.hot_score);
        dest.writeParcelable(this.badge, flags);
        if (!this.use_invite_code) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
        dest.writeInt(this.city_count);
        dest.writeInt(this.country_count);
        dest.writeParcelable(this.last_addr_and_gps, flags);
        dest.writeList(this.country_gps);
        dest.writeInt(this.my_fans_count);
        dest.writeString(this.bot_src);
        dest.writeString(this.bot_id);
        dest.writeString(this.bot_full_name);
        dest.writeString(this.bot_coypright);
    }

    protected UserBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.likes_count = in.readInt();
        this.followers_count = in.readInt();
        this.following_count = in.readInt();
        this.favorite_count = in.readInt();
        this.gender = in.readInt();
        this.id = in.readInt();
        this.last_notification_id = in.readInt();
        this.login_type = in.readInt();
        this.photos_count = in.readInt();
        this.album_count = in.readInt();
        this.badge_count = in.readInt();
        this.unused_invite_code_count = in.readInt();
        this.lbs_provinces = in.createStringArrayList();
        this.top_badges = in.createTypedArrayList(TimeLineBadgeBean.CREATOR);
        this.published_at = in.readLong();
        this.be_followed = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.is_following = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.is_blocked = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.need_bbtn = z;
        this.access_token = in.readString();
        this.avatar = in.readString();
        this.created_at = in.readString();
        this.deactive = in.readString();
        this.email = in.readString();
        this.birthday = in.readString();
        this.facebook_access_token = in.readString();
        this.facebook_graph = in.readString();
        this.facebook_id = in.readString();
        this.facebook_screen_name = in.readString();
        this.last_sign_in_at = in.readString();
        this.lower_case_name = in.readString();
        this.phone_number = in.readString();
        this.register_source = in.readString();
        this.reset_password_at = in.readString();
        this.screen_name = in.readString();
        this.signature = in.readString();
        this.twitter_access_token = in.readString();
        this.twitter_graph = in.readString();
        this.twitter_id = in.readString();
        this.twitter_name = in.readString();
        this.twitter_screen_name = in.readString();
        this.updated_at = in.readString();
        this.website = in.readString();
        this.weibo_access_token = in.readString();
        this.weibo_graph = in.readString();
        this.weibo_id = in.readString();
        this.weibo_screen_name = in.readString();
        this.weixin_union_id = in.readString();
        this.showcase_photos = in.createTypedArrayList(TimelineBean.CREATOR);
        this.cover_url = in.readString();
        this.silence_ids = in.readString();
        this.user_rank = in.readInt();
        this.is_new = in.readByte() != (byte) 0;
        this.hot_score = in.readInt();
        this.badge = (TimeLineBadgeBean) in.readParcelable(TimeLineBadgeBean.class.getClassLoader());
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.use_invite_code = z2;
        this.city_count = in.readInt();
        this.country_count = in.readInt();
        this.last_addr_and_gps = (LastAddrAndGpsBean) in.readParcelable(LastAddrAndGpsBean.class.getClassLoader());
        this.country_gps = new ArrayList();
        in.readList(this.country_gps, List.class.getClassLoader());
        this.my_fans_count = in.readInt();
        this.bot_src = in.readString();
        this.bot_id = in.readString();
        this.bot_full_name = in.readString();
        this.bot_coypright = in.readString();
    }
}