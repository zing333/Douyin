package cn.nineton.onetake.db.task;

//import com.blink.academy.onetake.support.database.table.PlayCountTable;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.nineton.onetake.db.table.PlayCountTable;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

public class PlayCountDbInstance {
    private static PlayCountDbInstance sPlayCountDbInstance;
    private Map<Long, PlayCountTable> mLongPlayCountTableMap = new HashMap();

    private PlayCountDbInstance() {
    }

    public static PlayCountDbInstance getInstance() {
        if (sPlayCountDbInstance == null) {
            synchronized (PlayCountDbInstance.class) {
                if (sPlayCountDbInstance == null) {
                    sPlayCountDbInstance = new PlayCountDbInstance();
                }
            }
        }
        return sPlayCountDbInstance;
    }

    public void addOrUpdateTable(PlayCountTable playCountTable) {
        if (!TextUtil.isNull(playCountTable) && playCountTable.playCount <= 36000 && playCountTable.photoID != 0 && playCountTable.playCount != 0) {
            PlayCountTable table = searchTable(playCountTable.photoID);
            if (TextUtil.isValidate(table) && (playCountTable.playCount < table.playCount || playCountTable.playCount != 1)) {
                playCountTable.playCount += table.playCount;
            }
            this.mLongPlayCountTableMap.put(Long.valueOf(playCountTable.photoID), playCountTable);
        }
    }

    public void addOrUpdateAllTable(List<PlayCountTable> playCountTableList) {
        if (!TextUtil.isNull(playCountTableList)) {
            LogUtil.d("play", "==================================================================================================================");
            for (PlayCountTable playCountTable : playCountTableList) {
                LogUtil.d("play", "===================photoID:" + playCountTable.photoID + ", count:" + playCountTable.playCount);
            }
            for (PlayCountTable playCountTable2 : playCountTableList) {
                PlayCountTable table = searchTable(playCountTable2.photoID);
                if (TextUtil.isValidate(table)) {
                    playCountTable2.playCount += table.playCount;
                }
            }
            for (PlayCountTable playCountTable22 : playCountTableList) {
                LogUtil.d("play", "===================photoID:" + playCountTable22.photoID + ", count:" + playCountTable22.playCount);
            }
            LogUtil.d("play", "==================================================================================================================");
            for (PlayCountTable playCountTable222 : playCountTableList) {
                this.mLongPlayCountTableMap.put(Long.valueOf(playCountTable222.photoID), playCountTable222);
            }
        }
    }

    public PlayCountTable searchTable(long photoID) {
        if (this.mLongPlayCountTableMap.containsKey(Long.valueOf(photoID))) {
            return (PlayCountTable) this.mLongPlayCountTableMap.get(Long.valueOf(photoID));
        }
        return null;
    }

    public void deleteAllTable() {
        this.mLongPlayCountTableMap.clear();
    }

    public List<PlayCountTable> getAllTable() {
        List<PlayCountTable> playCountTableList = new ArrayList();
        for (Entry entry : this.mLongPlayCountTableMap.entrySet()) {
            playCountTableList.add((PlayCountTable) entry.getValue());
        }
        return playCountTableList;
    }
}