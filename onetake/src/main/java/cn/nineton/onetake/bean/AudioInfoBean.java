package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AudioInfoBean implements Parcelable {
    protected String bg_ave;
    protected String cover;
    protected String cover_ave;
    protected int id;
    protected int t_count;
    protected String title;
    protected String title_s;

    public static AudioInfoBean objectFromData(String str) {
        return (AudioInfoBean) new Gson().fromJson(str, AudioInfoBean.class);
    }

    public static AudioInfoBean objectFromData(String str, String key) {
        try {
            return (AudioInfoBean) new Gson().fromJson(new JSONObject(str).getString(str), AudioInfoBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<AudioInfoBean> arrayAudioInfoBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<AudioInfoBean>>() {
        }.getType());
    }

    public static List<AudioInfoBean> arrayAudioInfoBeanFromData(String str, String key) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            return (List) new Gson().fromJson(jsonObject.getString(str), new TypeToken<ArrayList<AudioInfoBean>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public String getCover_ave() {
        return this.cover_ave;
    }

    public void setCover_ave(String cover_ave) {
        this.cover_ave = cover_ave;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBg_ave() {
        return this.bg_ave;
    }

    public void setBg_ave(String bg_ave) {
        this.bg_ave = bg_ave;
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

    public int getT_count() {
        return this.t_count;
    }

    public void setT_count(int t_count) {
        this.t_count = t_count;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cover_ave);
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.bg_ave);
        dest.writeString(this.title);
        dest.writeString(this.title_s);
        dest.writeInt(this.t_count);
    }
    public AudioInfoBean(){}
    protected AudioInfoBean(Parcel in) {
        this.cover_ave = in.readString();
        this.id = in.readInt();
        this.cover = in.readString();
        this.bg_ave = in.readString();
        this.title = in.readString();
        this.title_s = in.readString();
        this.t_count = in.readInt();
    }
}