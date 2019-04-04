package cn.nineton.onetake.db.task;

//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.support.database.DbHelper;
//import com.blink.academy.onetake.support.database.DbUtil;
import com.lidroid.xutils.DbUtils;

import cn.nineton.onetake.App;
import cn.nineton.onetake.db.DbHelper;
import cn.nineton.onetake.db.DbUtil;

public class BaseDbTask {
    protected static DbUtils dbUtils = DbUtil.getDB(App.getContext(), DbHelper.AppDbName);

    public static void dropDb() {
        DbUtil.dropDb(dbUtils);
    }
}