package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.TextUtil;

public class ErrorBean implements Parcelable {
    public static final Creator<ErrorBean> CREATOR = new Creator<ErrorBean>() {
        public ErrorBean createFromParcel(Parcel source) {
            return new ErrorBean(source);
        }

        public ErrorBean[] newArray(int size) {
            return new ErrorBean[size];
        }
    };
    public boolean error;
    public int error_code;
    public String error_msg;
    public boolean use_external_sms_service;
    public boolean use_invite_code;
    public boolean use_telesign;

    public static ErrorBean parse(String jsonString, IExceptionCallback callback) {
        if (!TextUtil.isValidate(jsonString)) {
            return null;
        }
        try {
            return (ErrorBean) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<ErrorBean>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeByte(this.error ? (byte) 1 : (byte) 0);
        dest.writeInt(this.error_code);
        dest.writeString(this.error_msg);
        if (this.use_external_sms_service) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (this.use_telesign) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (!this.use_invite_code) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
    }

    protected ErrorBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.error = in.readByte() != (byte) 0;
        this.error_code = in.readInt();
        this.error_msg = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.use_external_sms_service = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.use_telesign = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.use_invite_code = z2;
    }

    public String toString() {
        return "ErrorBean{error=" + this.error + ", error_code=" + this.error_code + ", error_msg='" + this.error_msg + '\'' + ", use_external_sms_service=" + this.use_external_sms_service + ", use_telesign=" + this.use_telesign + ", use_invite_code=" + this.use_invite_code + '}';
    }
}