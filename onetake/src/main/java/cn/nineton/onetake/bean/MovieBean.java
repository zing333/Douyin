package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class MovieBean implements Parcelable {
    public static final Creator<MovieBean> CREATOR = new Creator<MovieBean>() {
        public MovieBean createFromParcel(Parcel source) {
            return new MovieBean(source);
        }

        public MovieBean[] newArray(int size) {
            return new MovieBean[size];
        }
    };
    public MapBean mapInfo;

    public static MovieBean parse(String jsonString) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (MovieBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<MovieBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static List<MovieBean> parseList(String jsonString) {
        if (TextUtil.isNull(jsonString)) {
            return null;
        }
        try {
            return (List) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<List<MovieBean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mapInfo, flags);
    }

    protected MovieBean(Parcel in) {
        this.mapInfo = (MapBean) in.readParcelable(MapBean.class.getClassLoader());
    }

    public MapBean getMapInfo() {
        return this.mapInfo;
    }

    public void setMapInfo(MapBean mapInfo) {
        this.mapInfo = mapInfo;
    }
}