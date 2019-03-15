package cn.nineton.onetake.util;

import com.blink.academy.onetake.support.utils.SharedPrefUtil;

public class GlobalHelper {
    public static void setUserId(int userId) {
        SharedPrefUtil.setUserInfoToInt("user_id", userId);
    }

    public static void setVersionCode(int versionCode) {
        SharedPrefUtil.setAppInfoToInt("version_code", versionCode);
    }

    public static int getVersionCode() {
        return SharedPrefUtil.getAppInfoToInt("version_code", 0);
    }

    public static void setSinaFirstAuto(boolean versionCode) {
        SharedPrefUtil.setAppInfoBoolean("sina_first_auto", versionCode);
    }

    public static boolean getSinaFirstAuto() {
        return SharedPrefUtil.getAppInfoBoolean("sina_first_auto");
    }

    public static void setUserScreenName(String screenName) {
        SharedPrefUtil.setUserInfoToString("user_screen_name", screenName);
    }

    public static void setUserPhoneNumber(String phoneNumber) {
        SharedPrefUtil.setUserInfoToString("user_phone_number", phoneNumber);
    }

    public static void setUserAccessToken(String accessToken) {
        SharedPrefUtil.setUserInfoToString("user_access_token", accessToken);
    }

    public static void setUserAvatar(String avatar) {
        SharedPrefUtil.setUserInfoToString("user_avatar", avatar);
    }

    public static void setUserEmail(String email) {
        SharedPrefUtil.setUserInfoToString("user_email", email);
    }

    public static void setUserUserGender(int gender) {
        SharedPrefUtil.setUserInfoToInt("user_gender", gender);
    }

    public static void setUserBirthDay(String userBirthDay) {
        SharedPrefUtil.setUserInfoToString("user_bitrh", userBirthDay);
    }

    public static void setUserSignature(String signature) {
        SharedPrefUtil.setUserInfoToString("user_signature", signature);
    }

    public static void setUserIsHieddeSearchPersonAddressBookAccessView(boolean isHidden) {
        SharedPrefUtil.setUserInfoToBoolean("user_is_hiedde_search_person_address_book_access_view", isHidden);
    }

    public static int getUserId() {
        return SharedPrefUtil.getUserInfoToInt("user_id");
    }

    public static String getUserScreenName() {
        return SharedPrefUtil.getUserInfoToString("user_screen_name");
    }

    public static String getUserPhoneNumber() {
        return SharedPrefUtil.getUserInfoToString("user_phone_number");
    }

    public static String getUserAccessToken() {
        return SharedPrefUtil.getUserInfoToString("user_access_token");
    }

    public static String getUserAvatar() {
        return SharedPrefUtil.getUserInfoToString("user_avatar");
    }

    public static String getUserEmail() {
        return SharedPrefUtil.getUserInfoToString("user_email");
    }

    public static int getUserGender() {
        return SharedPrefUtil.getUserInfoToInt("user_gender");
    }

    public static String getUserBirthDay() {
        return SharedPrefUtil.getUserInfoToString("user_bitrh");
    }

    public static String getUserSignature() {
        return SharedPrefUtil.getUserInfoToString("user_signature");
    }

    public static boolean getUserIsHieddeSearchPersonAddressBookAccessView() {
        return SharedPrefUtil.getUserInfoToBoolean("user_is_hiedde_search_person_address_book_access_view", true);
    }

    public static boolean getUploadFriends() {
        return SharedPrefUtil.getUserInfoToBoolean("UploadFriends", false);
    }

    public static void setUpdateLoadFriends(boolean isUpload) {
        SharedPrefUtil.setUserInfoToBoolean("UploadFriends", isUpload);
    }

    public static void clearUserSharePreferences() {
        SharedPrefUtil.clearUserSharePreferences();
    }

    public static void setUserWeiboNameWhenShare(String weibo_name_used_when_share) {
        SharedPrefUtil.setUserInfoToString("UserWeiboNameWhenShare", weibo_name_used_when_share);
    }

    public static void setUserTwitterNameWhenShare(String twitter_name_used_when_share) {
        SharedPrefUtil.setUserInfoToString("UserTwitterNameWhenShare", twitter_name_used_when_share);
    }

    public static String getUserWeiboNameWhenShare() {
        return SharedPrefUtil.getUserInfoToString("UserWeiboNameWhenShare");
    }

    public static String getUserTwitterNameWhenShare() {
        return SharedPrefUtil.getUserInfoToString("UserTwitterNameWhenShare");
    }

    public static void setUserLongVideoDuration(int duration) {
        SharedPrefUtil.setUserInfoToInt("user_longvideo_duration", duration);
    }

    public static int getUserLongVideoDuration() {
        return SharedPrefUtil.getUserInfoToInt("user_longvideo_duration");
    }
}