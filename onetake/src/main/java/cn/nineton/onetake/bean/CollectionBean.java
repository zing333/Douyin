package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class CollectionBean implements Parcelable {
    public static final Creator<CollectionBean> CREATOR = new Creator<CollectionBean>() {
        public CollectionBean createFromParcel(Parcel source) {
            return new CollectionBean(source);
        }

        public CollectionBean[] newArray(int size) {
            return new CollectionBean[size];
        }
    };
    private String cover;
    private String cover_ave;
    private String created_at;
    private boolean has_all;
    private int id;
    private boolean isEditor;
    private boolean is_subscribe;
    private String note;
    private String note_s;
    private int photo_count;
    private List<TimelineBean> photos;
    private List<TimelineBean> recent_photos;
    private String screen_names;
    private int subscribe_count;
    private String title;
    private String title_s;
    private int unread_count;
    private String updated_at;
    private int users_count;
    private String viewTitle;

    public boolean isEditor() {
        return this.isEditor;
    }

    public void setEditor(boolean editor) {
        this.isEditor = editor;
    }

    public boolean isHas_all() {
        return this.has_all;
    }

    public void setHas_all(boolean has_all) {
        this.has_all = has_all;
    }

    public int getUsers_count() {
        return this.users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public int getUnread_count() {
        return this.unread_count;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    public List<TimelineBean> getRecent_photos() {
        return this.recent_photos;
    }

    public void setRecent_photos(List<TimelineBean> recent_photos) {
        this.recent_photos = recent_photos;
    }

    public List<TimelineBean> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<TimelineBean> photos) {
        this.photos = photos;
    }

    public static CollectionBean objectFromData(String str) {
        return (CollectionBean) new Gson().fromJson(str, CollectionBean.class);
    }

    public static CollectionBean objectFromData(String str, String key) {
        try {
            return (CollectionBean) new Gson().fromJson(new JSONObject(str).getString(str), CollectionBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<CollectionBean> arrayCollectionBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<CollectionBean>>() {
        }.getType());
    }

    public static List<CollectionBean> arrayCollectionBeanFromData(String str, String key) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            return (List) new Gson().fromJson(jsonObject.getString(str), new TypeToken<ArrayList<CollectionBean>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public String getViewTitle() {
        return this.viewTitle;
    }

    public void setViewTitle(String viewTitle) {
        this.viewTitle = viewTitle;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_s() {
        return this.title_s;
    }

    public void setTitle_s(String title_s) {
        this.title_s = title_s;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_s() {
        return this.note_s;
    }

    public void setNote_s(String note_s) {
        this.note_s = note_s;
    }

    public String getScreen_names() {
        return this.screen_names;
    }

    public void setScreen_names(String screen_names) {
        this.screen_names = screen_names;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_ave() {
        return this.cover_ave;
    }

    public void setCover_ave(String cover_ave) {
        this.cover_ave = cover_ave;
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

    public boolean is_subscribe() {
        return this.is_subscribe;
    }

    public void setIs_subscribe(boolean is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public int getSubscribe_count() {
        return this.subscribe_count;
    }

    public void setSubscribe_count(int subscribe_count) {
        this.subscribe_count = subscribe_count;
    }

    public int getPhoto_count() {
        return this.photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeString(this.viewTitle);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.title_s);
        dest.writeString(this.note);
        dest.writeString(this.note_s);
        dest.writeString(this.screen_names);
        dest.writeString(this.cover);
        dest.writeString(this.cover_ave);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.is_subscribe ? (byte) 1 : (byte) 0);
        dest.writeInt(this.users_count);
        dest.writeInt(this.subscribe_count);
        dest.writeInt(this.photo_count);
        dest.writeInt(this.unread_count);
        dest.writeTypedList(this.recent_photos);
        if (this.has_all) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        dest.writeTypedList(this.photos);
        if (!this.isEditor) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
    }

    protected CollectionBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.viewTitle = in.readString();
        this.id = in.readInt();
        this.title = in.readString();
        this.title_s = in.readString();
        this.note = in.readString();
        this.note_s = in.readString();
        this.screen_names = in.readString();
        this.cover = in.readString();
        this.cover_ave = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.is_subscribe = in.readByte() != (byte) 0;
        this.users_count = in.readInt();
        this.subscribe_count = in.readInt();
        this.photo_count = in.readInt();
        this.unread_count = in.readInt();
        this.recent_photos = in.createTypedArrayList(TimelineBean.CREATOR);
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.has_all = z;
        this.photos = in.createTypedArrayList(TimelineBean.CREATOR);
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.isEditor = z2;
    }
}