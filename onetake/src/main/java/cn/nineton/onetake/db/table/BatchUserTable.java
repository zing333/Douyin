package cn.nineton.onetake.db.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;

@Table(name = "batchuser")
public class BatchUserTable extends BaseTable {
    public static int NEWADD = 1;
    @Transient
    public static final String screenNameStr = "screenName";
    @Transient
    public static final String userIdStr = "userId";
    @Column(column = "avatarUrl")
    public String avatarUrl;
    @Column(column = "botSrc")
    public String botSrc;
    @Column(column = "gender")
    public int gender;
    @Column(column = "namePingyin")
    public String namePingyin;
    @Column(column = "newAdd")
    public int newAdd;
    @Column(column = "screenName")
    @Unique
    public String screenName;
    @Column(column = "userId")
    public int userId;

    public BatchUserTable(String screenName, String avatarUrl, int gender, String botSrc) {
        this.screenName = screenName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.newAdd = 0;
        this.botSrc = botSrc;
    }
}