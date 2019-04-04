package cn.nineton.onetake.event;

public class FinishActivityMessageEvent {
    public static final String AvatarChangePath = "avatar_change_path";
    public static final String AvatarMeChangePath = "avatar_me_change_path";
    public static final String AvatarMeChangePathEnd = "avatar_me_change_path_end";
    public static final String AvatarMeChangePathStart = "avatar_me_change_path_start";
    public static final String ForgetPWDPath = "forget_pwd_path";
    public static final String PhoneSignUpPath = "phone_sign_up_path";
    public static final String PublishVideoPath = "PublishVideoPath";
    public static final String SaveVideoDraftPath = "SaveVideoDraftPath";
    public static final String SearchFindFriendPath = "search_find_friend_path";
    private String ActivityPath;
    private String fromCurrentTimeStamp;
    private String fromUserName;
    private String longThumbnailPath;
    private String moreShortLongThumbnailPath;
    private byte[] pictureBytes;

    public FinishActivityMessageEvent(String activityPath) {
        this.ActivityPath = activityPath;
    }

    public FinishActivityMessageEvent(String activityPath, byte[] pictureBytes) {
        this.ActivityPath = activityPath;
        this.pictureBytes = pictureBytes;
    }

    public FinishActivityMessageEvent(String activityPath, String longThumbnailPath, String moreShortLongThumbnailPath, String fromUserName, String fromCurrentTimeStamp) {
        this.ActivityPath = activityPath;
        this.longThumbnailPath = longThumbnailPath;
        this.moreShortLongThumbnailPath = moreShortLongThumbnailPath;
        this.fromUserName = fromUserName;
        this.fromCurrentTimeStamp = fromCurrentTimeStamp;
    }

    public String getActivityPath() {
        return this.ActivityPath;
    }

    public byte[] getPictureBytes() {
        return this.pictureBytes;
    }

    public String getLongThumbnailPath() {
        return this.longThumbnailPath;
    }

    public String getMoreShortLongThumbnailPath() {
        return this.moreShortLongThumbnailPath;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public String getFromCurrentTimeStamp() {
        return this.fromCurrentTimeStamp;
    }
}