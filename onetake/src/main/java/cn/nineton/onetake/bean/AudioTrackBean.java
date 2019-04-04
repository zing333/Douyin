package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioTrackBean implements Parcelable {
    public static final Creator<AudioTrackBean> CREATOR = new Creator<AudioTrackBean>() {
        public AudioTrackBean createFromParcel(Parcel source) {
            return new AudioTrackBean(source);
        }

        public AudioTrackBean[] newArray(int size) {
            return new AudioTrackBean[size];
        }
    };
    private int bpm;
    private int creation_date;
    private String default_slice_point;
    private String duration;
    private int full_size;
    private String full_url;
    private int id;
    private boolean local;
    private String performer;
    private String performer_en;
    private String poi;
    private int price;
    private boolean purchased;
    private String title;
    private String title_en;
    private String title_s;
    private String title_s_en;
    private String trial_url;

    public static AudioTrackBean objectFromData(String str) {
        return (AudioTrackBean) new Gson().fromJson(str, AudioTrackBean.class);
    }

    public static AudioTrackBean objectFromData(String str, String key) {
        try {
            return (AudioTrackBean) new Gson().fromJson(new JSONObject(str).getString(str), AudioTrackBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<AudioTrackBean> arrayAudioTrackBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<AudioTrackBean>>() {
        }.getType());
    }

    public static List<AudioTrackBean> arrayAudioTrackBeanFromData(String str, String key) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            return (List) new Gson().fromJson(jsonObject.getString(str), new TypeToken<ArrayList<AudioTrackBean>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrial_url() {
        return this.trial_url;
    }

    public void setTrial_url(String trial_url) {
        this.trial_url = trial_url;
    }

    public String getFull_url() {
        return this.full_url;
    }

    public void setFull_url(String full_url) {
        this.full_url = full_url;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return this.title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_s() {
        return this.title_s;
    }

    public void setTitle_s(String title_s) {
        this.title_s = title_s;
    }

    public String getTitle_s_en() {
        return this.title_s_en;
    }

    public void setTitle_s_en(String title_s_en) {
        this.title_s_en = title_s_en;
    }

    public int getBpm() {
        return this.bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getPoi() {
        return this.poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public int getFull_size() {
        return this.full_size;
    }

    public void setFull_size(int full_size) {
        this.full_size = full_size;
    }

    public String getDefault_slice_point() {
        return this.default_slice_point;
    }

    public void setDefault_slice_point(String default_slice_point) {
        this.default_slice_point = default_slice_point;
    }

    public int getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(int creation_date) {
        this.creation_date = creation_date;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPerformer() {
        return this.performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getPerformer_en() {
        return this.performer_en;
    }

    public void setPerformer_en(String performer_en) {
        this.performer_en = performer_en;
    }

    public boolean isPurchased() {
        return this.purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public boolean isLocal() {
        return this.local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.id);
        dest.writeString(this.trial_url);
        dest.writeString(this.full_url);
        dest.writeString(this.duration);
        dest.writeString(this.title);
        dest.writeString(this.title_en);
        dest.writeString(this.title_s);
        dest.writeString(this.title_s_en);
        dest.writeInt(this.bpm);
        dest.writeString(this.poi);
        dest.writeInt(this.full_size);
        dest.writeString(this.default_slice_point);
        dest.writeInt(this.creation_date);
        dest.writeInt(this.price);
        dest.writeString(this.performer);
        dest.writeString(this.performer_en);
        if (this.purchased) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (!this.local) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
    }
    public AudioTrackBean(){}
    public AudioTrackBean cloneBean() {
        AudioTrackBean audioTrackBean = new AudioTrackBean();
        audioTrackBean.setId(getId());
        audioTrackBean.setTrial_url(getTrial_url());
        audioTrackBean.setFull_url(getFull_url());
        audioTrackBean.setDuration(getDuration());
        audioTrackBean.setTitle(getTitle());
        audioTrackBean.setTitle_en(getTitle_en());
        audioTrackBean.setTitle_s(getTitle_s());
        audioTrackBean.setTitle_s_en(getTitle_s_en());
        audioTrackBean.setBpm(getBpm());
        audioTrackBean.setPoi(getPoi());
        audioTrackBean.setFull_size(getFull_size());
        audioTrackBean.setDefault_slice_point(getDefault_slice_point());
        audioTrackBean.setCreation_date(getCreation_date());
        audioTrackBean.setPrice(getPrice());
        audioTrackBean.setPerformer(getPerformer());
        audioTrackBean.setPerformer_en(getPerformer_en());
        audioTrackBean.setPurchased(isPurchased());
        audioTrackBean.setLocal(isLocal());
        return audioTrackBean;
    }

    protected AudioTrackBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.id = in.readInt();
        this.trial_url = in.readString();
        this.full_url = in.readString();
        this.duration = in.readString();
        this.title = in.readString();
        this.title_en = in.readString();
        this.title_s = in.readString();
        this.title_s_en = in.readString();
        this.bpm = in.readInt();
        this.poi = in.readString();
        this.full_size = in.readInt();
        this.default_slice_point = in.readString();
        this.creation_date = in.readInt();
        this.price = in.readInt();
        this.performer = in.readString();
        this.performer_en = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.purchased = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.local = z2;
    }
}