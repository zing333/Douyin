package cn.nineton.onetake.db.task;

//import com.blink.academy.onetake.support.database.table.PlayCountTable;
//import com.blink.academy.onetake.support.utils.TextUtil;
import java.util.List;

import cn.nineton.onetake.db.table.PlayCountTable;
import cn.nineton.onetake.util.TextUtil;

public class PlayCountDbTask extends BaseDbTask {
    public static void addOrUpdateTable(PlayCountTable playCountTable) {
        if (!TextUtil.isNull(playCountTable) && playCountTable.playCount <= 36000 && playCountTable.photoID != 0 && playCountTable.playCount != 0) {
            PlayCountDbInstance.getInstance().addOrUpdateTable(playCountTable);
        }
    }

    public static void addOrUpdateAllTable(List<PlayCountTable> playCountTableList) {
        if (!TextUtil.isNull(playCountTableList)) {
            PlayCountDbInstance.getInstance().addOrUpdateAllTable(playCountTableList);
        }
    }

    public static PlayCountTable searchTable(long photoID) {
        return PlayCountDbInstance.getInstance().searchTable(photoID);
    }

    public static void deleteAllTable() {
        PlayCountDbInstance.getInstance().deleteAllTable();
    }

    public static List<PlayCountTable> getAllTable() {
        return PlayCountDbInstance.getInstance().getAllTable();
    }
}