package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class EditorCollectionBean implements Parcelable {
    public static final Creator<EditorCollectionBean> CREATOR = new Creator<EditorCollectionBean>() {
        public EditorCollectionBean createFromParcel(Parcel source) {
            return new EditorCollectionBean(source);
        }

        public EditorCollectionBean[] newArray(int size) {
            return new EditorCollectionBean[size];
        }
    };
    private String cover;
    private String cover_ave;
    private int id;
    private boolean is_publish;
    private String note;
    private boolean on_discover;
    private int photo_count;
    private int photo_id;
    private List<TimelineBean> photos;
    private String promote_url;
    private String promote_url_ave;
    private String promote_url_key;
    private List<SelectImageBean> selected_imgs;
    private String title;
    private String title_s;
    private boolean to_client;
    private String username;
    private int users_count;

    public String getPromote_url() {
        return this.promote_url;
    }

    public void setPromote_url(String promote_url) {
        this.promote_url = promote_url;
    }

    public String getPromote_url_ave() {
        return this.promote_url_ave;
    }

    public void setPromote_url_ave(String promote_url_ave) {
        this.promote_url_ave = promote_url_ave;
    }

    public String getPromote_url_key() {
        return this.promote_url_key;
    }

    public void setPromote_url_key(String promote_url_key) {
        this.promote_url_key = promote_url_key;
    }

    public int getPhoto_count() {
        return this.photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public boolean isOn_discover() {
        return this.on_discover;
    }

    public void setOn_discover(boolean on_discover) {
        this.on_discover = on_discover;
    }

    public boolean isIs_publish() {
        return this.is_publish;
    }

    public void setIs_publish(boolean is_publish) {
        this.is_publish = is_publish;
    }

    public boolean isTo_client() {
        return this.to_client;
    }

    public void setTo_client(boolean to_client) {
        this.to_client = to_client;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPhoto_id() {
        return this.photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public List<SelectImageBean> getSelected_imgs() {
        return this.selected_imgs;
    }

    public void setSelected_imgs(List<SelectImageBean> selected_imgs) {
        this.selected_imgs = selected_imgs;
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

    public int getUsers_count() {
        return this.users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public List<TimelineBean> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<TimelineBean> photos) {
        this.photos = photos;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.photo_count);
        dest.writeByte(this.on_discover ? (byte) 1 : (byte) 0);
        if (this.is_publish) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (!this.to_client) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeInt(this.photo_id);
        dest.writeTypedList(this.selected_imgs);
        dest.writeString(this.cover);
        dest.writeString(this.cover_ave);
        dest.writeString(this.title);
        dest.writeString(this.title_s);
        dest.writeString(this.note);
        dest.writeInt(this.users_count);
        dest.writeTypedList(this.photos);
        dest.writeString(this.promote_url);
        dest.writeString(this.promote_url_key);
        dest.writeString(this.promote_url_ave);
    }

    protected EditorCollectionBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.photo_count = in.readInt();
        this.on_discover = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.is_publish = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.to_client = z2;
        this.id = in.readInt();
        this.username = in.readString();
        this.photo_id = in.readInt();
        this.selected_imgs = in.createTypedArrayList(SelectImageBean.CREATOR);
        this.cover = in.readString();
        this.cover_ave = in.readString();
        this.title = in.readString();
        this.title_s = in.readString();
        this.note = in.readString();
        this.users_count = in.readInt();
        this.photos = in.createTypedArrayList(TimelineBean.CREATOR);
        this.promote_url = in.readString();
        this.promote_url_key = in.readString();
        this.promote_url_ave = in.readString();
    }

    public static List<EditorCollectionBean> arrayEditorCollectionBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<EditorCollectionBean>>() {
        }.getType());
    }
}