package cn.nineton.onetake.db.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

@Table(name = "playCount")
public class PlayCountTable extends BaseTable {
    @Column(column = "photoID")
    @Unique
    public long photoID;
    @Column(column = "playCount")
    public long playCount;

    public PlayCountTable(long photoID, long playCount) {
        this.photoID = photoID;
        this.playCount = playCount;
    }
}