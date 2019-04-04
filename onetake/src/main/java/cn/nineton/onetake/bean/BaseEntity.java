package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.nineton.onetake.widget.LoadingFooterView;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.widgets.loading.LoadingFooterView.State;

public class BaseEntity implements Parcelable {
    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        public BaseEntity createFromParcel(Parcel source) {
            return new BaseEntity(source);
        }

        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };
    protected boolean pin;
    protected LoadingFooterView.State state = LoadingFooterView.State.TheEnd;
    protected int viewType;

    public BaseEntity(int viewType) {
        this.viewType = viewType;
    }

    public BaseEntity(boolean pin, int viewType) {
        this.pin = pin;
        this.viewType = viewType;
    }

    public boolean isPin() {
        return this.pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public int getViewType() {
        return this.viewType;
    }

    public LoadingFooterView.State getState() {
        return this.state;
    }

    public void setState(LoadingFooterView.State state) {
        this.state = state;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.pin ? (byte) 1 : (byte) 0);
        dest.writeInt(this.viewType);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
    }

    protected BaseEntity(Parcel in) {
        this.pin = in.readByte() != (byte) 0;
        this.viewType = in.readInt();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : LoadingFooterView.State.values()[tmpState];
    }
}