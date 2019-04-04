package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
import java.util.ArrayList;
import java.util.List;

public class UndoEditModel implements Parcelable {
    public static final Creator<UndoEditModel> CREATOR = new Creator<UndoEditModel>() {
        public UndoEditModel createFromParcel(Parcel in) {
            return new UndoEditModel(in);
        }

        public UndoEditModel[] newArray(int size) {
            return new UndoEditModel[size];
        }
    };
    public List<LongVideosModel> musicModels;
    public List<LongVideosModel> textModels;
    public List<LongVideosModel> videoModels;
    public UndoEditModel(){}
    protected UndoEditModel(Parcel in) {
        this.videoModels = in.createTypedArrayList(LongVideosModel.CREATOR);
        this.textModels = in.createTypedArrayList(LongVideosModel.CREATOR);
        this.musicModels = in.createTypedArrayList(LongVideosModel.CREATOR);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.videoModels);
        dest.writeTypedList(this.textModels);
        dest.writeTypedList(this.musicModels);
    }

    public int describeContents() {
        return 0;
    }

    public UndoEditModel cloneData() {
        UndoEditModel newModel = new UndoEditModel();
        if (this.videoModels == null || this.videoModels.size() <= 0) {
            newModel.videoModels = null;
        } else {
            List<LongVideosModel> videos = new ArrayList();
            for (LongVideosModel model : this.videoModels) {
                videos.add(model.cloneVideoTypeModel());
            }
            newModel.videoModels = videos;
        }
        if (this.textModels == null || this.textModels.size() <= 0) {
            newModel.textModels = null;
        } else {
            List<LongVideosModel> texts = new ArrayList();
            for (LongVideosModel model2 : this.textModels) {
                texts.add(model2.cloneVideoTypeModel());
            }
            newModel.textModels = texts;
        }
        if (this.musicModels == null || this.musicModels.size() <= 0) {
            newModel.musicModels = null;
        } else {
            List<LongVideosModel> musics = new ArrayList();
            for (LongVideosModel model22 : this.musicModels) {
                musics.add(model22.cloneVideoTypeModel());
            }
            newModel.musicModels = musics;
        }
        return newModel;
    }
}