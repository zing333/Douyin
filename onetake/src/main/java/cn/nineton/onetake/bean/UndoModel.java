package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UndoModel implements Parcelable {
    public static final Creator<UndoModel> CREATOR = new Creator<UndoModel>() {
        public UndoModel createFromParcel(Parcel in) {
            return new UndoModel(in);
        }

        public UndoModel[] newArray(int size) {
            return new UndoModel[size];
        }
    };
    public static final int EDIT_TYPE = 1;
    public static final int FILTER_TYPE = 0;
    public static final int FIRST_TYPE = 2;
    private UndoEditModel editModel;
    private UndoFilterModel filterModel;
    public int type;

    public UndoModel(int type) {
        this.type = type;
    }

    protected UndoModel(Parcel in) {
        this.type = in.readInt();
        this.filterModel = (UndoFilterModel) in.readParcelable(UndoFilterModel.class.getClassLoader());
        this.editModel = (UndoEditModel) in.readParcelable(UndoEditModel.class.getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeParcelable(this.filterModel, flags);
        dest.writeParcelable(this.editModel, flags);
    }

    public int describeContents() {
        return 0;
    }

    public UndoFilterModel getFilterModel() {
        return this.filterModel;
    }

    public void setFilterModel(UndoFilterModel filterModel) {
        this.filterModel = filterModel;
    }

    public UndoEditModel getEditModel() {
        return this.editModel;
    }

    public void setEditModel(UndoEditModel editModel) {
        this.editModel = editModel;
    }

    public UndoModel cloneData(int type) {
        UndoModel newModel = new UndoModel(type);
        newModel.filterModel = this.filterModel.cloneData();
        newModel.editModel = this.editModel.cloneData();
        return newModel;
    }

    public UndoModel cloneData() {
        UndoModel newModel = new UndoModel(this.type);
        newModel.filterModel = this.filterModel.cloneData();
        newModel.editModel = this.editModel.cloneData();
        return newModel;
    }
}