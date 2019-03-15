package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.nineton.onetake.db.IMMsgTable;

public class IMMsgBean implements Parcelable {
    public static final Creator<IMMsgBean> CREATOR = new Creator<IMMsgBean>() {
        public IMMsgBean createFromParcel(Parcel source) {
            return new IMMsgBean(source);
        }

        public IMMsgBean[] newArray(int size) {
            return new IMMsgBean[size];
        }
    };
    public static final int IS_SEND_STATUS_RECEIVE = 0;
    public static final int IS_SEND_STATUS_SEND = 1;
    public static final int SEND_STATUS_FAIL = 0;
    public static final int SEND_STATUS_FAIL_IS_BLOCKED = 4;
    public static final int SEND_STATUS_FAIL_IS_REPEAT_TOO_MORE = 5;
    public static final int SEND_STATUS_RETRYING = 3;
    public static final int SEND_STATUS_SENDING = 1;
    public static final int SEND_STATUS_SUCCESS = 2;
    public boolean audio_played;
    public String content;
    public int is_send;
    public long kafka_id;
    public int msg_id;
    public boolean orphan;
    public int other_id;
    public String screen_name;
    public int send_status;
    public long server_id;
    public long ts;
    public String type;
    public String uuid;

    public void setData(IMMsgBean imMsgBean) {
        this.content = imMsgBean.content;
        this.is_send = imMsgBean.is_send;
        this.screen_name = imMsgBean.screen_name;
        this.type = imMsgBean.type;
        this.ts = imMsgBean.ts;
        this.send_status = imMsgBean.send_status;
        this.uuid = imMsgBean.uuid;
        this.server_id = imMsgBean.server_id;
        this.kafka_id = imMsgBean.kafka_id;
        this.orphan = imMsgBean.orphan;
        this.other_id = imMsgBean.other_id;
    }

    public IMMsgBean(IMMsgTable imMsgTable) {
        boolean z;
        boolean z2 = true;
        this.msg_id = imMsgTable.msg_id;
        this.content = imMsgTable.content;
        this.is_send = imMsgTable.is_send;
        this.screen_name = imMsgTable.screen_name;
        this.type = imMsgTable.type;
        this.ts = imMsgTable.ts;
        this.send_status = imMsgTable.send_status;
        this.uuid = imMsgTable.uuid;
        this.server_id = imMsgTable.server_id;
        if (imMsgTable.audio_played == 1) {
            z = true;
        } else {
            z = false;
        }
        this.audio_played = z;
        if (imMsgTable.orphan != 1) {
            z2 = false;
        }
        this.orphan = z2;
        this.kafka_id = imMsgTable.kafka_id;
        this.other_id = imMsgTable.user_id;
    }

    public IMMsgBean(String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid) {
        this.content = content;
        this.is_send = is_send;
        this.screen_name = screen_name;
        this.type = type;
        this.ts = ts;
        this.send_status = send_status;
        this.uuid = uuid;
    }

    public IMMsgBean(long kafka_id, String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid) {
        this.content = content;
        this.is_send = is_send;
        this.screen_name = screen_name;
        this.type = type;
        this.ts = ts;
        this.send_status = send_status;
        this.uuid = uuid;
        this.kafka_id = kafka_id;
    }

    public IMMsgBean(long kafka_id, String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid, long server_id, boolean orphan) {
        this(content, is_send, screen_name, type, ts, send_status, uuid);
        this.server_id = server_id;
        this.kafka_id = kafka_id;
        this.orphan = orphan;
    }

    public IMMsgBean(String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid, boolean audio_played) {
        this.content = content;
        this.is_send = is_send;
        this.screen_name = screen_name;
        this.type = type;
        this.ts = ts;
        this.send_status = send_status;
        this.uuid = uuid;
        this.audio_played = audio_played;
    }

    public IMMsgBean(String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid, long server_id) {
        this(content, is_send, screen_name, type, ts, send_status, uuid);
        this.server_id = server_id;
    }

    public IMMsgBean(String content, int is_send, String screen_name, String type, long ts, int send_status, String uuid, long server_id, int msg_id) {
        this(content, is_send, screen_name, type, ts, send_status, uuid, server_id);
        this.msg_id = msg_id;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.msg_id);
        dest.writeString(this.content);
        dest.writeInt(this.is_send);
        dest.writeString(this.screen_name);
        dest.writeString(this.type);
        dest.writeLong(this.ts);
        dest.writeInt(this.send_status);
        dest.writeString(this.uuid);
        dest.writeLong(this.server_id);
        if (this.audio_played) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (!this.orphan) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
        dest.writeInt(this.other_id);
    }

    protected IMMsgBean(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.msg_id = in.readInt();
        this.content = in.readString();
        this.is_send = in.readInt();
        this.screen_name = in.readString();
        this.type = in.readString();
        this.ts = in.readLong();
        this.send_status = in.readInt();
        this.uuid = in.readString();
        this.server_id = in.readLong();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.audio_played = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.orphan = z2;
        this.other_id = in.readInt();
    }
}