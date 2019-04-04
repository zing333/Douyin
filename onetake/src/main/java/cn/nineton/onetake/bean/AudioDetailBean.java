package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class AudioDetailBean extends AudioInfoBean {
    public static final Creator<AudioDetailBean> CREATOR = new Creator<AudioDetailBean>() {
        public AudioDetailBean createFromParcel(Parcel source) {
            return new AudioDetailBean(source);
        }

        public AudioDetailBean[] newArray(int size) {
            return new AudioDetailBean[size];
        }
    };
    private ArrayList<AudioTrackBean> audio_tracks;
    private int user_wallet;

    public int getUser_wallet() {
        return this.user_wallet;
    }

    public void setUser_wallet(int user_wallet) {
        this.user_wallet = user_wallet;
    }

    public ArrayList<AudioTrackBean> getAudio_tracks() {
        return this.audio_tracks;
    }

    public void setAudio_tracks(ArrayList<AudioTrackBean> audio_tracks) {
        this.audio_tracks = audio_tracks;
    }

    public static AudioDetailBean objectFromData(String str) {
        return (AudioDetailBean) new Gson().fromJson(str, AudioDetailBean.class);
    }

    public static List<AudioDetailBean> arrayAudioDetailBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<AudioDetailBean>>() {
        }.getType());
    }
    public AudioDetailBean(AudioAlbumBean bean) {
        this.cover_ave = bean.getCover_ave();
        this.id = bean.getId();
        this.cover = bean.getCover();
        this.bg_ave = bean.getBg_ave();
        this.title = bean.getTitle();
        this.title_s = bean.getTitle_s();
        this.t_count = bean.getT_count();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.audio_tracks);
        dest.writeInt(this.user_wallet);
    }

    protected AudioDetailBean(Parcel in) {
        super(in);
        this.audio_tracks = in.createTypedArrayList(AudioTrackBean.CREATOR);
        this.user_wallet = in.readInt();
    }
}