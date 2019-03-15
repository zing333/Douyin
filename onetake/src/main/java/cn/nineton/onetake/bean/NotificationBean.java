package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class NotificationBean implements Parcelable {
    public static final Creator<NotificationBean> CREATOR = new Creator<NotificationBean>() {
        public NotificationBean createFromParcel(Parcel in) {
            NotificationBean bean = new NotificationBean();
            bean.id = in.readInt();
            bean.user_id = in.readInt();
            bean.friend_id = in.readInt();
            bean.photo_id = in.readInt();
            bean.comment_id = in.readInt();
            bean.album_id = in.readLong();
            bean.album_name = in.readString();
            bean.notification_type = in.readInt();
            bean.description = in.readString();
            bean.created_at = in.readString();
            bean.updated_at = in.readString();
            bean.friend_avatar = in.readString();
            bean.user_gender = in.readInt();
            bean.friend_gender = in.readInt();
            bean.friend_screen_name = in.readString();
            bean.user_avatar = in.readString();
            bean.user_screen_name = in.readString();
            bean.photo_long_thumbnail = in.readString();
            bean.phone_number = in.readString();
            bean.photo_preview_ave_info = in.readString();
            bean.published_at = in.readLong();
            boolean[] booleans = new boolean[4];
            in.readBooleanArray(booleans);
            bean.checked = booleans[0];
            bean.is_following = booleans[1];
            bean.be_followed = booleans[2];
            bean.had_multi_photos = booleans[3];
            return bean;
        }

        public NotificationBean[] newArray(int size) {
            return new NotificationBean[size];
        }
    };
    public long album_id;
    public String album_name;
    public boolean be_followed;
    public boolean checked;
    public int comment_id;
    public String created_at;
    public String description;
    public String friend_avatar;
    public int friend_gender;
    public int friend_id;
    public String friend_screen_name;
    public boolean had_multi_photos;
    public int id;
    public boolean is_following;
    public int notification_type;
    public String phone_number;
    public int photo_height;
    public int photo_id;
    public String photo_long_thumbnail;
    public String photo_preview_ave_info;
    public int photo_vtype;
    public int photo_width;
    public long published_at;
    public String updated_at;
    public String user_avatar;
    public int user_gender;
    public int user_id;
    public String user_screen_name;

    public static NotificationBean parse(String jsonString, IExceptionCallback callback) {
        if (!TextUtil.isValidate(jsonString)) {
            return null;
        }
        try {
            return (NotificationBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<NotificationBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static List<NotificationBean> parseList(String jsonString, IExceptionCallback callback) {
        if (!TextUtil.isValidate(jsonString)) {
            return null;
        }
        try {
            return (List) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<List<NotificationBean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeInt(this.user_id);
        out.writeInt(this.friend_id);
        out.writeInt(this.photo_id);
        out.writeInt(this.comment_id);
        out.writeLong(this.album_id);
        out.writeString(this.album_name);
        out.writeInt(this.notification_type);
        out.writeString(this.description);
        out.writeString(this.created_at);
        out.writeString(this.updated_at);
        out.writeString(this.friend_avatar);
        out.writeInt(this.user_gender);
        out.writeInt(this.friend_gender);
        out.writeString(this.friend_screen_name);
        out.writeString(this.user_avatar);
        out.writeString(this.user_screen_name);
        out.writeString(this.photo_long_thumbnail);
        out.writeString(this.phone_number);
        out.writeString(this.photo_preview_ave_info);
        out.writeLong(this.published_at);
        out.writeBooleanArray(new boolean[]{this.checked, this.is_following, this.be_followed, this.had_multi_photos});
    }
}