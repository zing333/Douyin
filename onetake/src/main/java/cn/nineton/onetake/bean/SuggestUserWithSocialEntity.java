package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.bean.user.UserBean;
//import com.blink.academy.onetake.support.enums.SuggestUserType;
//import com.blink.academy.onetake.support.utils.ImageUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.util.ImageUtil;
import cn.nineton.onetake.util.TextUtil;

public class SuggestUserWithSocialEntity {
    private String avatarUrl;
    private int fansCount;
    private int gender;
    private boolean isEmpty;
    private boolean isEmptyLine;
    private boolean isFollowing;
    private boolean isHeader;
    private boolean isHeaderText;
    private boolean is_new;
    private UserBean mUserBean;
    private String phone;
    private int rankPosition;
    private String screenName;
    private String suggestFrom;
    private List<TimelineBean> timelineBeanList;
    private int userId;
    private SuggestUserType userType;
    private int user_rank;

    public UserBean getUserBean() {
        return this.mUserBean;
    }

    public void setUserBean(UserBean userBean) {
        this.mUserBean = userBean;
    }

    public SuggestUserWithSocialEntity(boolean isEmpty, boolean isEmptyLine, String suggestFrom) {
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        this.isHeaderText = true;
        this.isEmpty = isEmpty;
        this.isEmptyLine = isEmptyLine;
        this.suggestFrom = suggestFrom;
    }

    public SuggestUserWithSocialEntity(int userId, String avatarUrl, int gender, boolean isFollowing, boolean isHeader, List<TimelineBean> timelineBeanList, String screenName, String suggestFrom, SuggestUserType userType, String phone, UserBean userBean) {
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        this.userId = userId;
        this.gender = gender;
        this.avatarUrl = TextUtil.isNull(avatarUrl) ? null : avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        this.isFollowing = isFollowing;
        this.isHeader = isHeader;
        if (timelineBeanList == null) {
            timelineBeanList = null;
        } else if (timelineBeanList.size() >= 4) {
            timelineBeanList = timelineBeanList.subList(0, 3);
        }
        this.timelineBeanList = timelineBeanList;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(suggestFrom)) {
            suggestFrom = "";
        }
        this.suggestFrom = suggestFrom;
        this.userType = userType;
        if (TextUtil.isNull(phone)) {
            phone = "";
        }
        this.phone = phone;
        this.mUserBean = userBean;
    }

    public SuggestUserWithSocialEntity(int userId, String avatarUrl, int gender, boolean isFollowing, boolean isHeader, List<TimelineBean> timelineBeanList, String screenName, String suggestFrom, SuggestUserType userType, String phone, int user_rank, int rankPosition, boolean is_new, UserBean userBean) {
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        this.userId = userId;
        this.gender = gender;
        this.avatarUrl = TextUtil.isNull(avatarUrl) ? null : avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        this.isFollowing = isFollowing;
        this.isHeader = isHeader;
        if (timelineBeanList == null) {
            timelineBeanList = null;
        } else if (timelineBeanList.size() >= 4) {
            timelineBeanList = timelineBeanList.subList(0, 3);
        }
        this.timelineBeanList = timelineBeanList;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(suggestFrom)) {
            suggestFrom = "";
        }
        this.suggestFrom = suggestFrom;
        this.userType = userType;
        if (TextUtil.isNull(phone)) {
            phone = "";
        }
        this.phone = phone;
        this.user_rank = user_rank;
        this.rankPosition = rankPosition;
        this.is_new = is_new;
        this.mUserBean = userBean;
    }

    public SuggestUserWithSocialEntity(int userId, String avatarUrl, int gender, boolean isFollowing, boolean isHeader, List<TimelineBean> timelineBeanList, String screenName, String suggestFrom, SuggestUserType userType, String phone, int user_rank, int rankPosition, boolean is_new, int fansCount, UserBean userBean) {
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        this.userId = userId;
        this.gender = gender;
        this.avatarUrl = TextUtil.isNull(avatarUrl) ? null : avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        this.isFollowing = isFollowing;
        this.isHeader = isHeader;
        if (timelineBeanList == null) {
            timelineBeanList = null;
        } else if (timelineBeanList.size() >= 4) {
            timelineBeanList = timelineBeanList.subList(0, 3);
        }
        this.timelineBeanList = timelineBeanList;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(suggestFrom)) {
            suggestFrom = "";
        }
        this.suggestFrom = suggestFrom;
        this.userType = userType;
        if (TextUtil.isNull(phone)) {
            phone = "";
        }
        this.phone = phone;
        this.user_rank = user_rank;
        this.rankPosition = rankPosition;
        this.is_new = is_new;
        this.fansCount = fansCount;
        this.mUserBean = userBean;
    }

    public SuggestUserWithSocialEntity(int userId, String avatarUrl, int gender, boolean isFollowing, boolean isHeader, List<TimelineBean> timelineBeanList, String screenName, String suggestFrom, SuggestUserType userType, String phone, int user_rank, boolean is_new, UserBean userBean) {
        String str;
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        this.userId = userId;
        this.gender = gender;
        if (TextUtil.isNull(avatarUrl)) {
            str = null;
        } else {
            str = avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        }
        this.avatarUrl = str;
        this.isFollowing = isFollowing;
        this.isHeader = isHeader;
        if (timelineBeanList == null) {
            timelineBeanList = null;
        } else if (timelineBeanList.size() >= 4) {
            timelineBeanList = timelineBeanList.subList(0, 3);
        }
        this.timelineBeanList = timelineBeanList;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(suggestFrom)) {
            suggestFrom = "";
        }
        this.suggestFrom = suggestFrom;
        this.userType = userType;
        if (TextUtil.isNull(phone)) {
            phone = "";
        }
        this.phone = phone;
        this.user_rank = user_rank;
        this.is_new = is_new;
        this.mUserBean = userBean;
    }

    public SuggestUserWithSocialEntity(String avatarUrl, String screenName, int gender, boolean isFollowing, SuggestUserType userType) {
        this.isEmptyLine = false;
        this.isEmpty = false;
        this.isHeader = false;
        if (TextUtil.isNull(avatarUrl)) {
            avatarUrl = null;
        }
        this.avatarUrl = avatarUrl;
        this.isFollowing = isFollowing;
        this.gender = gender;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        this.suggestFrom = "";
        this.timelineBeanList = new ArrayList();
        this.phone = "";
        this.userType = userType;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public void setSuggestFrom(String suggestFrom) {
        if (TextUtil.isNull(suggestFrom)) {
            suggestFrom = "";
        }
        this.suggestFrom = suggestFrom;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public String getSuggestFrom() {
        return this.suggestFrom;
    }

    public boolean isFollowing() {
        return this.isFollowing;
    }

    public List<TimelineBean> getTimelineBeanList() {
        return this.timelineBeanList;
    }

    public boolean isHeader() {
        return this.isHeader;
    }

    public boolean isEmptyLine() {
        return this.isEmptyLine;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public boolean isHeaderText() {
        return this.isHeaderText;
    }

    public SuggestUserType getUserType() {
        return this.userType;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setIsFooter(boolean isFooter) {
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public int getGender() {
        return this.gender;
    }

    public int getFansCount() {
        return this.fansCount;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getUserRank() {
        return this.user_rank;
    }

    public int getRankPosition() {
        return this.rankPosition;
    }

    public boolean isNew() {
        return this.is_new;
    }
}