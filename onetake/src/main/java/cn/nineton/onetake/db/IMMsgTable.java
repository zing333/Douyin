package cn.nineton.onetake.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import cn.nineton.onetake.bean.IMMsgBean;

@Table(name = "msg")
public class IMMsgTable {
    public static final String Column_Audio_Played = "audio_played";
    public static final String Column_Content = "content";
    public static final String Column_Is_Send = "is_send";
    public static final String Column_Kafka_Id = "kafka_id";
    public static final String Column_Msg_Id = "msg_id";
    public static final String Column_Orphan = "orphan";
    public static final String Column_Screen_Name = "screen_name";
    public static final String Column_Send_Status = "send_status";
    public static final String Column_Server_Id = "server_id";
    public static final String Column_Ts = "ts";
    public static final String Column_Type = "type";
    public static final String Column_User_Id = "user_id";
    public static final String Column_Uuid = "uuid";
    public static final String Column_from_user_id = "from_user_id";
    public static final String TYPE_IMG = "img";
    public static final String TYPE_MSG = "msg";
    public static final String TYPE_VIDEO = "video";
    @Column(column = "audio_played")
    public int audio_played;
    @Column(column = "content")
    public String content;
    @Column(column = "is_send")
    public int is_send;
    @Column(column = "kafka_id")
    public long kafka_id;
    @Column(column = "msg_id")
    @Id
    public int msg_id;
    @Column(column = "orphan")
    public int orphan;
    @Column(column = "screen_name")
    public String screen_name;
    @Column(column = "send_status")
    public int send_status;
    @Column(column = "server_id")
    public long server_id;
    @Column(column = "ts")
    public long ts;
    @Column(column = "type")
    public String type;
    @Column(column = "user_id")
    public int user_id;
    @Column(column = "uuid")
    public String uuid;

    public IMMsgTable(IMMsgBean imMsgBean) {
        int i;
        int i2 = 1;
        this.msg_id = imMsgBean.msg_id;
        this.content = imMsgBean.content;
        this.is_send = imMsgBean.is_send;
        this.user_id = imMsgBean.other_id;
        this.screen_name = imMsgBean.screen_name;
        this.type = imMsgBean.type;
        this.ts = imMsgBean.ts;
        this.send_status = imMsgBean.send_status;
        this.uuid = imMsgBean.uuid;
        this.server_id = imMsgBean.server_id;
        if (imMsgBean.audio_played) {
            i = 1;
        } else {
            i = 0;
        }
        this.audio_played = i;
        if (!imMsgBean.orphan) {
            i2 = 0;
        }
        this.orphan = i2;
        this.kafka_id = imMsgBean.kafka_id;
    }
}