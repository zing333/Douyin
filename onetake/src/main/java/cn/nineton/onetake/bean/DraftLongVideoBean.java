package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.FirstVideoBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.model.video.UndoBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.adapter.UndoBean;

public class DraftLongVideoBean implements Parcelable {
    public static final Creator<DraftLongVideoBean> CREATOR = new Creator<DraftLongVideoBean>() {
        public DraftLongVideoBean createFromParcel(Parcel in) {
            return new DraftLongVideoBean(in);
        }

        public DraftLongVideoBean[] newArray(int size) {
            return new DraftLongVideoBean[size];
        }
    };
    private boolean allAudioMute;
    private boolean allVideoMute;
    private long duration;
    private FirstVideoBean mFirstVideoBean;
    private int mLongVideoHeight;
    private ArrayList<LongVideosModel> mLongVideoModels;
    private int mLongVideoWidth;
    private UndoBean mUndoBean;
    private ArrayList<LongVideosModel> musicModels;
    private ArrayList<LongVideosModel> textModels;
    private int undoCount;

    public int getUndoCount() {
        return this.undoCount;
    }

    public UndoBean getUndoBean() {
        if (this.mUndoBean == null) {
            this.mUndoBean = new UndoBean();
        }
        return this.mUndoBean;
    }

    public void setUndoBean(UndoBean mUndoBean) {
        this.mUndoBean = mUndoBean;
        this.undoCount = mUndoBean.getUndoModels().size();
    }

    public boolean isAllVideoMute() {
        return this.allVideoMute;
    }

    public void setAllVideoMute(boolean allVideoMute) {
        this.allVideoMute = allVideoMute;
    }

    public boolean isAllAudioMute() {
        return this.allAudioMute;
    }

    public void setAllAudioMute(boolean allAudioMute) {
        this.allAudioMute = allAudioMute;
    }
    public DraftLongVideoBean(){}
    protected DraftLongVideoBean(Parcel in) {
        boolean z = true;
        this.mLongVideoWidth = in.readInt();
        this.mLongVideoHeight = in.readInt();
        this.undoCount = in.readInt();
        this.allVideoMute = in.readByte() != (byte) 0;
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.allAudioMute = z;
        this.mLongVideoModels = in.createTypedArrayList(LongVideosModel.CREATOR);
        this.textModels = in.createTypedArrayList(LongVideosModel.CREATOR);
        this.musicModels = in.createTypedArrayList(LongVideosModel.CREATOR);
        this.mFirstVideoBean = (FirstVideoBean) in.readParcelable(FirstVideoBean.class.getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.mLongVideoWidth);
        dest.writeInt(this.mLongVideoHeight);
        dest.writeInt(this.undoCount);
        dest.writeTypedList(this.mLongVideoModels);
        dest.writeTypedList(this.textModels);
        dest.writeTypedList(this.musicModels);
        dest.writeParcelable(this.mFirstVideoBean, flags);
        if (this.allVideoMute) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.allAudioMute) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
    }

    public int describeContents() {
        return 0;
    }

    public List<LongVideosModel> getMusicModels() {
        return this.musicModels;
    }

    public void setMusicModel(List<LongVideosModel> musicModel) {
        if (this.musicModels == null) {
            this.musicModels = new ArrayList();
        }
        this.musicModels.clear();
        if (musicModel != null) {
            for (LongVideosModel model : musicModel) {
                this.musicModels.add(model.cloneVideoTypeModel());
            }
        }
    }

    public void setTextModels(ArrayList<LongVideosModel> textModels) {
        if (this.textModels == null) {
            this.textModels = new ArrayList();
        }
        this.textModels.clear();
        Iterator it = textModels.iterator();
        while (it.hasNext()) {
            this.textModels.add(((LongVideosModel) it.next()).cloneVideoTypeModel());
        }
    }

    public ArrayList<LongVideosModel> getTextModels() {
        if (this.textModels == null) {
            this.textModels = new ArrayList();
        }
        return this.textModels;
    }

    public int getLongVideoWidth() {
        return this.mLongVideoWidth;
    }

    public void setLongVideoWidth(int mLongVideoWidth) {
        this.mLongVideoWidth = mLongVideoWidth;
    }

    public int getLongVideoHeight() {
        return this.mLongVideoHeight;
    }

    public void setLongVideoHeight(int mLongVideoHeight) {
        this.mLongVideoHeight = mLongVideoHeight;
    }

    public ArrayList<LongVideosModel> getLongVideoModels() {
        if (this.mLongVideoModels == null) {
            this.mLongVideoModels = new ArrayList();
        }
        return this.mLongVideoModels;
    }

    public void setLongVideoModels(ArrayList<LongVideosModel> videoModels) {
        if (this.mLongVideoModels == null) {
            this.mLongVideoModels = new ArrayList();
        }
        this.mLongVideoModels.clear();
        Iterator it = videoModels.iterator();
        while (it.hasNext()) {
            this.mLongVideoModels.add(((LongVideosModel) it.next()).cloneVideoTypeModel());
        }
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }

    public FirstVideoBean getFirstVideoBean() {
        return this.mFirstVideoBean;
    }

    public void setFirstVideoBean(FirstVideoBean mFirstVideoBean) {
        this.mFirstVideoBean = mFirstVideoBean;
    }

    public void clearUndoModels() {
        this.mUndoBean = null;
    }
}