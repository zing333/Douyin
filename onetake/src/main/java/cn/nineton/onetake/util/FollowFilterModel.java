package cn.nineton.onetake.util;

//import com.blink.academy.onetake.support.callbacks.LoadFilterCallBack;
//import com.blink.academy.onetake.support.database.task.BatchUserDbTask;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.ui.adapter.entities.UserCardEntity;
import java.util.ArrayList;

import cn.nineton.onetake.bean.UserCardEntity;
import cn.nineton.onetake.listener.LoadFilterCallBack;

public class FollowFilterModel implements LoadFilterCallBack {
    private static FollowFilterModel followFilterModel;
    private boolean filter1NeedReset;
    private boolean filter2NeedReset;
    public ArrayList<FollowInfo> followList = new ArrayList();
    private boolean isGetData;

    public class FollowInfo {
        public String avatarUrl;
        public int gender;
        public String group;
        public boolean isFollow;
        public String screen_name;

        public FollowInfo(String screen_name, String group) {
            this.screen_name = screen_name;
            this.group = group;
        }
    }

    public static FollowFilterModel getInstance() {
        if (followFilterModel == null) {
            synchronized (FollowFilterModel.class) {
                if (followFilterModel == null) {
                    followFilterModel = new FollowFilterModel();
                }
            }
        }
        return followFilterModel;
    }

    private FollowFilterModel() {
    }

    public void finish() {
        new Thread() {
            public void run() {
                FollowFilterModel.this.getFollowFilterData();
            }
        }.start();
    }

    public void getFollowFilterData() {
        LogUtil.d("wangchen12345", "exefollowList = " + this.followList);
        if (this.followList != null && this.followList.size() != 0 && !this.isGetData) {
            for (int i = 0; i < this.followList.size(); i++) {
                FollowInfo followInfo = (FollowInfo) this.followList.get(i);
                LogUtil.d("wangchen12345", "must follow = " + followInfo.screen_name);
                UserCardEntity userCardEntity = null;//BatchUserDbTask.searchBatchUserTableForEntity(followInfo.screen_name);
                if (userCardEntity != null) {
                    followInfo.isFollow = userCardEntity.isFollowing();
                    followInfo.avatarUrl = userCardEntity.getAvatarUrl();
                    followInfo.gender = userCardEntity.getGender();
                }
            }
            this.isGetData = true;
        }
    }

    public void addFollowInfo(String name, String group) {
        int i = 0;
        while (i < this.followList.size()) {
            if (!group.equals(((FollowInfo) this.followList.get(i)).group)) {
                i++;
            } else {
                return;
            }
        }
        this.followList.add(new FollowInfo(name, group));
    }

    public void updateFollowInfo(String name, boolean isFollowing) {
        for (int i = 0; i < this.followList.size(); i++) {
            FollowInfo followInfo = (FollowInfo) this.followList.get(i);
            if (followInfo.screen_name.equals(name)) {
                followInfo.isFollow = isFollowing;
                followInfo.screen_name = name;
                return;
            }
        }
    }

    public void prepare() {
    }

    public void success() {
    }

    public void reset() {
        this.isGetData = false;
        this.followList.clear();
        this.filter1NeedReset = true;
        this.filter2NeedReset = true;
        LogUtil.d("wangchen12345", "reset");
    }

    public boolean isNeedResetFilter1() {
        return this.filter1NeedReset;
    }

    public void setNeedResetFilter1(boolean filter1NeedReset) {
        this.filter1NeedReset = filter1NeedReset;
    }

    public boolean isNeedResetFilter2() {
        return this.filter2NeedReset;
    }

    public void setNeedResetFilter2(boolean filter2NeedReset) {
        this.filter2NeedReset = filter2NeedReset;
    }
}