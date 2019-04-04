package cn.nineton.onetake.bean;

import java.io.Serializable;

import cn.nineton.onetake.util.ImageUtil;
import cn.nineton.onetake.util.TextUtil;

public class UserCardEntity implements Serializable {
    public String avatar;
    private String avatarUrl;
    private String botSrc;
    public int followed;
    private boolean following;
    private int gender;
    public int id;
    private boolean invited;
    public boolean is_blocked;
    private UserBean mUserBean;
    private String screenName;

    public String getBotSrc() {
        return this.botSrc;
    }

    public void setBotSrc(String botSrc) {
        this.botSrc = botSrc;
    }

    public UserBean getUserBean() {
        return this.mUserBean;
    }

    public void setUserBean(UserBean userBean) {
        this.mUserBean = userBean;
    }

    public boolean equals(Object o) {
        return this.screenName != null && (o instanceof UserCardEntity) && this.screenName.equals(((UserCardEntity) o).screenName);
    }

    public UserCardEntity(String avatarUrl, String screenName) {
        this(avatarUrl, screenName, true, 0);
    }

    public UserCardEntity(String avatarUrl, String screenName, int gender) {
        this(avatarUrl, screenName, true, gender);
    }

    public UserCardEntity setBotSrcData(String botSrc) {
        setBotSrc(botSrc);
        return this;
    }

    public UserCardEntity(String avatarUrl, String screenName, boolean following, int gender) {
        this.avatarUrl = TextUtil.isNull(avatarUrl) ? "" : avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        this.following = following;
        this.gender = gender;
    }

    public UserCardEntity(String avatarUrl, String screenName, boolean following, int gender, int id, int followed, boolean is_blocked) {
        this.avatarUrl = TextUtil.isNull(avatarUrl) ? "" : avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        this.avatar = avatarUrl;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        this.following = following;
        this.gender = gender;
        this.id = id;
        this.followed = followed;
        this.is_blocked = is_blocked;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setIsFollowing(boolean following) {
        this.following = following;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public boolean isFollowing() {
        return this.following;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean getInvited() {
        return this.invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }
}