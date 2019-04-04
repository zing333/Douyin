package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;
import java.util.ArrayList;

public class AudioIndexBean implements Parcelable {
    public static final Creator<AudioIndexBean> CREATOR = new Creator<AudioIndexBean>() {
        public AudioIndexBean createFromParcel(Parcel source) {
            return new AudioIndexBean(source);
        }

        public AudioIndexBean[] newArray(int size) {
            return new AudioIndexBean[size];
        }
    };
    ArrayList<AudioGenreBean> genres;
    int user_wallet;

    public int getUser_wallet() {
        return this.user_wallet;
    }

    public void setUser_wallet(int user_wallet) {
        this.user_wallet = user_wallet;
    }

    public ArrayList<AudioGenreBean> getGenres() {
        return this.genres;
    }

    public void setGenres(ArrayList<AudioGenreBean> genres) {
        this.genres = genres;
    }

    public static AudioIndexBean objectFromData(String str) {
        return (AudioIndexBean) new Gson().fromJson(str, AudioIndexBean.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.genres);
        dest.writeInt(this.user_wallet);
    }

    protected AudioIndexBean(Parcel in) {
        this.genres = in.createTypedArrayList(AudioGenreBean.CREATOR);
        this.user_wallet = in.readInt();
    }
}