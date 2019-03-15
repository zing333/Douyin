package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class AlbumInfoBean implements Parcelable {
    public static final Creator<AlbumInfoBean> CREATOR = new Creator<AlbumInfoBean>() {
        public AlbumInfoBean createFromParcel(Parcel source) {
            return new AlbumInfoBean(source);
        }

        public AlbumInfoBean[] newArray(int size) {
            return new AlbumInfoBean[size];
        }
    };
    public String created_at;
    public int host_user_id;
    public int id;
    public List<UserBean> invite_users;
    public String name;
    public int photos_count;
    public long published_at;
    public String update_time;
    public String updated_at;
    public int user_size;
    public List<UserBean> users;

    public static AlbumInfoBean parse(String jsonString, IExceptionCallback callback) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (AlbumInfoBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<AlbumInfoBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.host_user_id);
        dest.writeInt(this.id);
        dest.writeInt(this.user_size);
        dest.writeInt(this.photos_count);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.name);
        dest.writeLong(this.published_at);
        dest.writeString(this.update_time);
        dest.writeTypedList(this.invite_users);
        dest.writeTypedList(this.users);
    }

    protected AlbumInfoBean(Parcel in) {
        this.host_user_id = in.readInt();
        this.id = in.readInt();
        this.user_size = in.readInt();
        this.photos_count = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.name = in.readString();
        this.published_at = in.readLong();
        this.update_time = in.readString();
        this.invite_users = in.createTypedArrayList(UserBean.CREATOR);
        this.users = in.createTypedArrayList(UserBean.CREATOR);
    }
}