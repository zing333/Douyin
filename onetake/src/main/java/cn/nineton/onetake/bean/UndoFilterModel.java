package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.FilterInfo;
//import com.blink.academy.onetake.bean.filterview.FilterEffectBean;
import java.util.ArrayList;
import java.util.List;

public class UndoFilterModel implements Parcelable {
    public static final Creator<UndoFilterModel> CREATOR = new Creator<UndoFilterModel>() {
        public UndoFilterModel createFromParcel(Parcel in) {
            return new UndoFilterModel(in);
        }

        public UndoFilterModel[] newArray(int size) {
            return new UndoFilterModel[size];
        }
    };
    public List<FilterEffectBean> filterBeanList;
    public String filterName;
    public FilterInfo info;
    public UndoFilterModel(){}
    protected UndoFilterModel(Parcel in) {
        this.filterName = in.readString();
        this.info = (FilterInfo) in.readParcelable(FilterInfo.class.getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filterName);
        dest.writeParcelable(this.info, flags);
    }

    public int describeContents() {
        return 0;
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<FilterEffectBean> getFilterBeanList() {
        return this.filterBeanList;
    }

    public void setFilterBeanList(List<FilterEffectBean> filterBeanList) {
        this.filterBeanList = filterBeanList;
    }

    public UndoFilterModel cloneData() {
        UndoFilterModel model = new UndoFilterModel();
        model.filterName = this.filterName;
        if (this.filterBeanList == null || this.filterBeanList.size() <= 0) {
            model.filterBeanList = null;
        } else {
            List<FilterEffectBean> beans = new ArrayList();
            int size = this.filterBeanList.size();
            for (int i = 0; i < size; i++) {
                beans.add(((FilterEffectBean) this.filterBeanList.get(i)).clone());
            }
            model.filterBeanList = beans;
        }
        return model;
    }
}