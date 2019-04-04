package cn.nineton.onetake.util;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.nineton.onetake.http.BasicRequestManager;
import cn.nineton.onetake.http.RequestCallback;
import cn.nineton.onetake.http.UrlHelper;

public class UserRequestManager extends BasicRequestManager {
    public static void getGiphyTest(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest("http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC&limit=5", callback);
    }

    public static void noInterested(int user_id, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_me_not_interested_path_get(user_id), callback);
    }

    public static void uploadContacts(String params, RequestCallback<JSONArray> callback) {
        mRequestManager.sendPostArrayRequest(UrlHelper.users_me_upload_contacts_path_post(), params, callback);
    }

    public static void changePhone(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.users_me_change_phone_path_post(), params, callback);
    }

    public static void getUserFollowing(String screen_name, long cursor_id, boolean is_next_cursor, int limit, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.users_following_path_get_cursor(screen_name, cursor_id, is_next_cursor, limit), callback);
    }

    public static void getMyFollowing(long cursor_id, boolean is_next_cursor, int limit, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.my_following_path_get_cursor(cursor_id, is_next_cursor, limit), callback);
    }

    public static void followUser(String screen_name, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_follow_path_get(screen_name), callback);
    }

    public static void unfollowUser(String screen_name, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_unfollow_path_get(screen_name), callback);
    }

    public static void updatePushNotificationSetting(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.users_me_update_push_settings_path_post(), params, callback);
    }

    public static void getPushNotificationSetting(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_me_push_settings_path_get(), callback);
    }

    public static void getPushNotificationType(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.user_new_push_setting(), callback);
    }

    public static void setToCustomNotification(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_me_custom_push_path_get(), callback);
    }

    public static void getNotificationsCount(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequestNoTimeout(UrlHelper.users_notifications_count_path_get(), callback);
    }

    public static void getNotifications(String notificationType, int limit, long cursor_id, boolean is_next_cursor, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.users_notifications_path_get_cursor(notificationType, limit, cursor_id, is_next_cursor), callback);
    }

    public static void getUserFollowers(String screen_name, long cursor_id, boolean is_next_cursor, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.users_followers_path_get_cursor(screen_name, cursor_id, is_next_cursor), callback);
    }

    public static void getUser(String screen_name, int userId, boolean isOwn, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(isOwn ? UrlHelper.getOwnUserInfo(screen_name, userId) : UrlHelper.users_path_get(screen_name), callback);
    }

    public static void getUserMeAvatarChange(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_me_avatar_change_path_get(), callback);
    }

    public static void getUserMeProfileUpdate(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.users_me_update_path_post(), params, callback);
    }

    public static void reportUserAbuse(String screen_name, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_abuse_path_get(screen_name), callback);
    }

    public static void getUserMeChangePassword(String password, String new_password, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.users_me_change_password_path_get(password, new_password), null, callback);
    }

    public static void blockUser(String screen_name, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_block_path_get(screen_name), callback);
    }

    public static void unblockUser(String screen_name, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.users_unblock_path_get(screen_name), callback);
    }

    public static void getUserPhotos(String screen_name, long cursor_id, boolean is_next_cursor, int contentType, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.users_photos_path_get_cursor(screen_name, cursor_id, is_next_cursor, contentType), callback);
    }

    public static void getMyPhotos(long cursor_id, boolean is_next_cursor, int contentType, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.my_photos_path_get_cursor(cursor_id, is_next_cursor, contentType), callback);
    }

    public static void getAlbumPhotos(long album_id, long cursor_id, boolean is_next_cursor, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.album_photos_get_cursor(album_id, cursor_id, is_next_cursor), callback);
    }

    public static void getAlbumInvitedUsers(long album_id, RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.album_invited_users_get(album_id), callback);
    }

    public static void getLocationPhotos(String lat, String lng, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.location_photos_path_get_cursor(lat, lng), callback);
    }

    public static void getBatchUserFollowing(String screen_name, long cursor_id, boolean is_next_cursor, int limit, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequestNoTimeout(UrlHelper.users_batch_following_path_get_cursor(screen_name, cursor_id, is_next_cursor, limit), callback);
    }

    public static void uploadNewAlbumInfo(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.new_album_upload_info_path_post(), params, callback);
    }

    public static void acceptInvite(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.album_accept_invite_path_post(), params, callback);
    }

    public static void uploadEditAlbumInfo(long album_id, String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPutObjectRequest(UrlHelper.edit_album_upload_info_path_put(album_id), params, callback);
    }

    public static void hstoryAlbumInfo(RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.hstory_album_photos_get(), callback);
    }

    public static void quitAlbum(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.album_quit_album_path_post(), params, callback);
    }

    public static void putGifToAlbum(String params, RequestCallback<String> callback) {
        mRequestManager.sendPutJsonRequest(UrlHelper.put_gifs_to_album_path_put(), params, callback);
    }

    public static void submitFeedBack(String params, RequestCallback<String> callback) {
        mRequestManager.sendPostJsonRequest(UrlHelper.submit_feedback_post(), params, callback);
    }

    public static void verifyTwitterToken(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.verify_twitter_token(), params, callback);
    }

    public static void deleteGifFromAlbum(String params, RequestCallback<String> callback) {
        mRequestManager.sendPostJsonRequest(UrlHelper.delete_gifs_from_album_path_post(params), params, callback);
    }

    public static void meNeedUpgrade(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.me_need_upgrade_path_post(), callback);
    }

    public static void mTestNeedUpgrade(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.test_need_upgrade_path_send(), callback);
    }

    public static void getInviteCodes(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.me_invite_codes_path_get(), callback);
    }

    public static void meIncr(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.me_incr_path_post(), params, callback);
    }

    public static void uploadWeiboTwitterName(String params, RequestCallback<String> callback) {
        mRequestManager.sendPostJsonRequest(UrlHelper.upload_weibo_twitter_name_post(), params, callback);
    }

    public static void getUserBadge(String screenName, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.user_badge_path_get(screenName), callback);
    }

    public static void getUserNearbyBadge(String associateValue, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.badges_my_path_get(associateValue), callback);
    }

    public static void getArticleById(String params, RequestCallback<JSONObject> requestCallback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.get_article_by_id_path_get(params), requestCallback);
    }

    public static void getGiphyLibrary(String params, RequestCallback<JSONObject> requestCallback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.get_giphy_library_path_get(params), requestCallback);
    }

    public static void unFav(String params, RequestCallback<String> callback) {
        mRequestManager.sendPostJsonRequest(UrlHelper.post_giphy_unfav_path_post(), params, callback);
    }

    public static void verifyValidateCode(String params, RequestCallback<JSONObject> callback) {
        mRequestManager.sendPostObjectRequest(UrlHelper.post_test_user_code(), params, callback);
    }

    public static void googleTranslate(String word, RequestCallback<JSONObject> requestCallback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.get_google_translate_path_get(word), requestCallback);
    }

    public static void unbindPhoneAndInfo(RequestCallback<JSONObject> requestCallback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.get_unbind_phone_and_info_url(), requestCallback);
    }

    public static void getUserLikes(long next_cursor, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.get_like_photo_url(next_cursor), callback);
    }

    public static void getCollectionsSubscribers(int collection_id, int page, RequestCallback<JSONArray> callback) {
        mRequestManager.sendGetArrayRequest(UrlHelper.getCollectionsSubscribers(collection_id, page), callback);
    }

    public static void getSignature(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.get_signature(), callback);
    }

    public static void getActiveList(RequestCallback<JSONObject> callback) {
        mRequestManager.sendGetObjectRequest(UrlHelper.getActiveList(), callback);
    }
}