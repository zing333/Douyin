package cn.nineton.onetake.db;

import android.content.Context;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.support.database.table.BatchUserTable;
//import com.blink.academy.onetake.support.debug.LogUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;

import cn.nineton.onetake.App;
import cn.nineton.onetake.db.table.BatchUserTable;
import cn.nineton.onetake.util.LogUtil;

public class DbUtil {
    private static final String TAG = "DbUtil";

    public static DbUtils getDB(Context context, String dbName) {
        DbUtils db = DbUtils.create(context, dbName, 3, new DbUpgradeListener() {
            public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                LogUtil.d(DbUtil.TAG, "onUpgrade : oldVersion : " + oldVersion + " newVersion : " + newVersion);
                switch (oldVersion) {
                    case 0:
                    case 1:
                    case 2:
                        try {
                            dbUtils.deleteAll(BatchUserTable.class);
                            dbUtils.dropTable(BatchUserTable.class);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    default:
                        return;
                }
            }
        });
        db.configAllowTransaction(true);
        db.configDebug(App.Debug);
        return db;
    }

    public static void dropDb(DbUtils dbUtils) {
        try {
            dbUtils.dropDb();
        } catch (DbException e) {
        }
    }
}