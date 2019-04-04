package cn.nineton.onetake.http;

import android.annotation.SuppressLint;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.facebook.internal.AnalyticsEvents;
//import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.nineton.onetake.util.Constants;
import cn.nineton.onetake.util.GlobalHelper;
import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;

public class UrlHelper {
    private static final String AccuWeatherLBSApiKey = "feaad331420a4ec3b3fb5bbba7d1250d";
    public static int ChatServerPort = 0;
    public static String ChatServerUrl = null;
    public static String EllaServerUrl = null;
    public static String EmptyUrl = null;
    public static String MapServerUrl = null;
    public static final String OSVersion = null;
    public static String OnlineHost = null;
    public static String OnlineIP = null;
    public static final int Store = 1;
    private static final String TAG = null;
    public static String TestHost;
    public static String TestIP;
    public static String access_token_str;
    public static String associate_str;
    public static String limit_str;
    public static String new_password_str;
    public static String next_cursor_str;
    public static String page_str;
    public static String password_str;
    public static String prev_cursor_str;
    public static String q_str;
    public static String screen_name_str;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.support.helper.UrlHelper.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Unknown Source)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:293)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1217)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1224)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:595)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:79)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 8 more
        */
//    static {
//        /*
//        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.support.helper.UrlHelper.<clinit>():void, dex:
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.helper.UrlHelper.<clinit>():void");
//    }

    public static String timeline_current_path_get() {
        return EllaServerUrl + "/v4/timeline/current" + getCommonParams();
    }

    public static String timeline_current_path_get_cursor(long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/v4/timeline/current" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return timeline_current_path_get();
    }

    public static String timeline_current_recoment_get_cursor(long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/v4/timeline/current?" + getCursor(is_next_cursor) + "=" + cursor_id + getAndroidVersion(false);
        }
        return EllaServerUrl + "/v4/timeline/current" + getAndroidVersion(true);
    }

    public static String timeline_suggest_user_with_social_full_photos_path_get() {
        return EllaServerUrl + "/v4/timeline/suggest_user_with_social_full_photos" + getCommonParams();
    }

    public static String timeline_suggest_user_path_get(long hot_score) {
        if (hot_score > -1) {
            return EllaServerUrl + "/v4/timeline/suggest_user" + getCommonParams() + "&next_cursor=" + hot_score;
        }
        return EllaServerUrl + "/v4/timeline/suggest_user" + getCommonParams();
    }

    public static String timeline_far_place_path_get() {
        return EllaServerUrl + "/v2/timeline/far_place" + getCommonParams();
    }

    public static String timeline_discover_path_get(int contentType, long cursor_id) {
        String otherValue = "";
        Object[] objArr;
        switch (contentType) {
            case 1:
                objArr = new Object[1];
                objArr[0] = "gif";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 2:
                objArr = new Object[1];
                objArr[0] = "photo";//"photo";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 3:
                objArr = new Object[1];
                objArr[0] = "video";//"video";
                otherValue = String.format("&video_type=%s", objArr);
                break;
        }
        if (cursor_id > 0) {
            return EllaServerUrl + "/v4/timeline/discover" + getCommonParamsHasOther(otherValue) + "&next_cursor=" + cursor_id + "&include_special_article=1";
        }
        return EllaServerUrl + "/v4/timeline/discover" + getCommonParamsHasOther(otherValue) + "&include_special_article=1";
    }

    public static String timeline_change_description_privacy() {
        return EllaServerUrl + "/v2/photos" + getCommonParams();
    }

    public static String timeline_banner_path_get() {
        return EllaServerUrl + "/v2/timeline/banner" + getCommonParams();
    }

    public static String register_check_phone_path_post() {
        return EllaServerUrl + "/register/check_phone" + getAndroidVersion(true);
    }

    public static String register_phone_sign_in_path_post() {
        return EllaServerUrl + "/register/phone_signin" + getAndroidVersion(true);
    }

    public static String register_phone_sign_up_path_post() {
        return EllaServerUrl + "/register/phone_signup" + getAndroidVersion(true);
    }

    public static String register_check_name_path_post() {
        return EllaServerUrl + "/register/check_name" + getAndroidVersion(true);
    }

    public static String register_bind_path_post() {
        return EllaServerUrl + "/register/bind" + getAndroidVersion(true);
    }

    public static String register_weixin_verification_path_post() {
        return EllaServerUrl + "/register/weixin_verification" + getAndroidVersion(true);
    }

    public static String register_weibo_verification_path_post() {
        return EllaServerUrl + "/register/weibo_verification" + getAndroidVersion(true);
    }

    public static String register_qq_verification_path_post() {
        return EllaServerUrl + "/register/qq_verification" + getAndroidVersion(true);
    }

    public static String register_weixin_sign_in_path_post() {
        return EllaServerUrl + "/register/weixin_signin" + getAndroidVersion(true);
    }

    public static String register_tumblr_sign_in_path_post() {
        return EllaServerUrl + "/register/tumblr_signin" + getAndroidVersion(true);
    }

    public static String register_weibo_sign_in_path_post() {
        return EllaServerUrl + "/register/weibo_signin" + getAndroidVersion(true);
    }

    public static String register_facebook_sign_in_path_post() {
        return EllaServerUrl + "/register/facebook_signin" + getAndroidVersion(true);
    }

    public static String register_twitter_sign_in_path_post() {
        return EllaServerUrl + "/register/twitter_signin" + getAndroidVersion(true);
    }

    public static String register_qq_sign_in_path_post() {
        return EllaServerUrl + "/register/qq_signin" + getAndroidVersion(true);
    }

    public static String register_facebook_sign_up_path_post() {
        return EllaServerUrl + "/register/facebook_signup" + getAndroidVersion(true);
    }

    public static String register_weixin_sign_up_path_post() {
        return EllaServerUrl + "/register/weixin_signup" + getAndroidVersion(true);
    }

    public static String register_qq_sign_up_path_post() {
        return EllaServerUrl + "/register/qq_signup" + getAndroidVersion(true);
    }

    public static String register_weibo_sign_up_path_post() {
        return EllaServerUrl + "/register/weibo_signup" + getAndroidVersion(true);
    }

    public static String register_tumblr_sign_up_path_post() {
        return EllaServerUrl + "/register/tumblr_signup" + getAndroidVersion(true);
    }

    public static String register_twitter_sign_up_path_post() {
        return EllaServerUrl + "/register/twitter_signup" + getAndroidVersion(true);
    }

    public static String register_send_verify_sms_path_post() {
        return EllaServerUrl + "/register/send_verify_sms" + getAndroidVersion(true);
    }

    public static String register_send_verify_call_path_post() {
        return EllaServerUrl + "/register/send_verify_call" + getAndroidVersion(true);
    }

    public static String register_phone_verification_path_post() {
        return EllaServerUrl + "/register/phone_verification" + getAndroidVersion(true);
    }

    public static String registet_phone_me_update_post() {
        return EllaServerUrl + "/me/update" + getAndroidVersion(true);
    }

    public static String users_me_change_phone_path_post() {
        return EllaServerUrl + "/me/change_phone" + getCommonParams();
    }

    public static String users_me_upload_contacts_path_post() {
        return EllaServerUrl + "/me/upload_contacts" + getCommonParams();
    }

    public static String add_friend_me_upload_contacts_path_post() {
        return EllaServerUrl + "/me/upload_contacts_sync" + getCommonParams();
    }

    public static String add_friend_me_find_friends_post() {
        return EllaServerUrl + "/me/find_friends" + getCommonParams();
    }

    public static String users_me_not_interested_path_get(int user_id) {
        return EllaServerUrl + "/me/not_interested" + getCommonParamsAddOther("&id=" + user_id);
    }

    public static String users_me_update_push_settings_path_post() {
        return EllaServerUrl + "/me/update_push_settings" + getCommonParams();
    }

    public static String user_new_push_setting() {
        return EllaServerUrl + "/me/notification_push_type" + getCommonParams();
    }

    public static String users_me_push_settings_path_get() {
        return EllaServerUrl + "/me/push_settings" + getCommonParams();
    }

    public static String users_me_custom_push_path_get() {
        return EllaServerUrl + "/me/custom_push" + getCommonParams();
    }

    public static String users_me_avatar_change_path_get() {
        return EllaServerUrl + "/me/avatar_change" + getCommonParams();
    }

    public static String users_me_update_path_post() {
        return EllaServerUrl + "/me/update" + getCommonParams();
    }

    public static String users_me_change_password_path_get(String password, String new_password) {
        return EllaServerUrl + "/me/change_password_with_token_reset" + getCommonParams() + "&" + password_str + "=" + password + "&" + new_password_str + "=" + new_password;
    }

    public static String my_following_path_get_cursor(long cursor_id, boolean is_next_cursor, int limit) {
        if (cursor_id <= 0) {
            return my_following_path_get(limit);
        }
        if (limit == 0) {
            return EllaServerUrl + "/users/my_following" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return EllaServerUrl + "/users/my_following" + getLimitParams(String.valueOf(limit), getCursor(is_next_cursor), cursor_id);
    }

    private static String my_following_path_get(int limit) {
        if (limit == 0) {
            return EllaServerUrl + "/users/my_following" + getCommonParams();
        }
        return EllaServerUrl + "/users/my_following" + getLimitParams(String.valueOf(limit));
    }

    public static String users_following_path_get_cursor(String screen_name, long cursor_id, boolean is_next_cursor, int limit) {
        if (cursor_id <= 0) {
            return users_following_path_get(screen_name, limit);
        }
        if (limit == 0) {
            return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getLimitParams(String.valueOf(limit), getCursor(is_next_cursor), cursor_id);
    }

    public static String users_following_path_get(String screen_name, int limit) {
        if (limit == 0) {
            return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getCommonParams();
        }
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getLimitParams(String.valueOf(limit));
    }

    public static String users_follow_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/follow" + getCommonParams();
    }

    public static String users_unfollow_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/unfollow" + getCommonParams();
    }

    public static String users_followers_path_get_cursor(String screen_name, long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/followers" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return users_followers_path_get(screen_name);
    }

    public static String users_followers_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/followers" + getCommonParams();
    }

    public static String users_batch_following_path_get_cursor(String screen_name, long cursor_id, boolean is_next_cursor, int limit) {
        if (cursor_id <= 0) {
            return users_batch_following_path_get(screen_name, limit);
        }
        if (limit == 0) {
            return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getLimitParams(String.valueOf(limit), getCursor(is_next_cursor), cursor_id);
    }

    public static String users_batch_following_path_get(String screen_name, int limit) {
        if (limit == 0) {
            return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getCommonParams();
        }
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/following" + getLimitParams(String.valueOf(limit));
    }

    public static String users_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + getCommonParams();
    }

    public static String getOwnUserInfo(String screen_name, int userId) {
        return EllaServerUrl + "/me/info/" + getCommonParams();
    }

    public static String users_abuse_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/abuse" + getCommonParams();
    }

    public static String users_block_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/block" + getCommonParams();
    }

    public static String users_unblock_path_get(String screen_name) {
        return EllaServerUrl + "/users/" + URLEncoder(screen_name) + "/unblock" + getCommonParams();
    }

    public static String my_photos_path_get_cursor(long cursor_id, boolean is_next_cursor, int contentType) {
        String otherValue = "";
        Object[] objArr;
        switch (contentType) {
            case 1:
                objArr = new Object[1];
                objArr[0] = "gif";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 2:
                objArr = new Object[1];
                objArr[0] = "photo";//"photo";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 3:
                objArr = new Object[1];
                objArr[0] = "video";//"video";
                otherValue = String.format("&video_type=%s", objArr);
                break;
        }
        if (cursor_id <= 0) {
            return my_photos_path_get(contentType);
        }
        return EllaServerUrl + "/v4/photos/my" + getCommonParamsHaveOther(getCursor(is_next_cursor), cursor_id, otherValue + "&allow_vtype=1");
    }

    public static String my_photos_path_get(int contentType) {
        String otherValue = "";
        Object[] objArr;
        switch (contentType) {
            case 1:
                objArr = new Object[1];
                objArr[0] = "gif";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 2:
                objArr = new Object[1];
                objArr[0] = "photo";//"photo";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 3:
                objArr = new Object[1];
                objArr[0] = "video";//"video";
                otherValue = String.format("&video_type=%s", objArr);
                break;
        }
        return EllaServerUrl + "/v4/photos/my" + getCommonParamsHasOther(otherValue + "&allow_vtype=1");
    }

    public static String users_photos_path_get_cursor(String screen_name, long cursor_id, boolean is_next_cursor, int contentType) {
        String otherValue = "";
        Object[] objArr;
        switch (contentType) {
            case 1:
                objArr = new Object[1];
                objArr[0] = "gif";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 2:
                objArr = new Object[1];
                objArr[0] = "photo";//"photo";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 3:
                objArr = new Object[1];
                objArr[0] = "video";//"video";
                otherValue = String.format("&video_type=%s", objArr);
                break;
        }
        if (cursor_id > 0) {
            return EllaServerUrl + "/v4/users/" + URLEncoder(screen_name) + "/photos" + getCommonParamsHaveOther(getCursor(is_next_cursor), cursor_id, otherValue);
        }
        return users_photos_path_get(screen_name, contentType);
    }

    @SuppressLint({"DefaultLocale"})
    public static String user_tags_hot_photos_path_get(String tagName, long cursor_id) {
        Object[] objArr = new Object[2];
        objArr[0] = cursor_id == 0 ? "" : "&" + getCursor(true) + "=" + cursor_id;
        objArr[1] = URLEncoder(tagName);
        return EllaServerUrl + "/v4/user_tags/hot_photos" + getCommonParamsHasOther(String.format("%1$s&name=%2$s", objArr));
    }

    @SuppressLint({"DefaultLocale"})
    public static String user_tags_hot_photos_path_get(String tagName, double hot_score) {
        Object[] objArr = new Object[2];
        objArr[0] = hot_score == 0.0d ? "" : "&" + getCursor(true) + "=" + hot_score;
        objArr[1] = URLEncoder(tagName);
        return EllaServerUrl + "/v4/user_tags/hot_photos" + getCommonParamsAddOther(String.format("%1$s&name=%2$s", objArr));
    }

    @SuppressLint({"DefaultLocale"})
    public static String user_tags_photos_path_get(String tagName, long cursor_id) {
        Object[] objArr = new Object[2];
        objArr[0] = cursor_id == 0 ? "" : "&" + getCursor(true) + "=" + cursor_id;
        objArr[1] = URLEncoder(tagName);
        return EllaServerUrl + "/v4/user_tags/photos" + getCommonParamsHasOther(String.format("%1$s&name=%2$s", objArr));
    }

    @SuppressLint({"DefaultLocale"})
    public static String user_albums_path_get_page(String screen_name, int page) {
        String page_str = page == 0 ? "" : "&page=" + page;
        Object[] objArr = new Object[3];
        objArr[0] = EllaServerUrl;
        objArr[1] = URLEncoder(screen_name);
        objArr[2] = getCommonParamsAddOther(page_str);
        return String.format("%1$s/v2/users/%2$s/albums%3$s", objArr);
    }

    public static String user_badge_path_get(String screen_name) {
        return EllaServerUrl + "/v2/users/" + URLEncoder(screen_name) + "/badges" + getCommonParams();
    }

    public static String badges_my_path_get(String associateValue) {
        return EllaServerUrl + "/v2/badges/my" + getCommonParams() + associateValue;
    }

    public static String password_reset_path_get(String phoneNumber) {
        return EllaServerUrl + "/password/reset?phone=" + phoneNumber + getAndroidVersion(false);
    }

    public static String password_confirm_path_post() {
        return EllaServerUrl + "/password/confirm" + getAndroidVersion(true);
    }

    public static String users_notifications_count_path_get() {
        return EllaServerUrl + "/v4/notifications_count" + getCommonParams();
    }

    public static String users_notifications_path_get_cursor(String notificationType, int limit, long cursor_id, boolean is_next_cursor) {
        String notificationTypeStr = "";
        if (TextUtil.isValidate(notificationType) && !notificationType.equals("all")) {
            notificationTypeStr = "&notification_type=" + notificationType;
        }
        if (cursor_id <= 0) {
            return users_notifications_path_get(limit, notificationTypeStr);
        }
        if (limit <= 0) {
            return EllaServerUrl + "/v4/notifications" + getCommonParams(getCursor(is_next_cursor), cursor_id) + notificationTypeStr;
        }
        return EllaServerUrl + "/v4/notifications" + getLimitParams(String.valueOf(limit), getCursor(is_next_cursor), cursor_id) + notificationTypeStr;
    }

    public static String users_notifications_path_get(int limit, String notificationType) {
        if (limit <= 0) {
            return EllaServerUrl + "/v4/notifications" + getCommonParams() + notificationType;
        }
        return EllaServerUrl + "/v4/notifications" + getLimitParams(String.valueOf(limit)) + notificationType;
    }

    public static String photos_chain_path_get(int photo_id) {
        return EllaServerUrl + "/v2/photos/" + photo_id + "/chain" + getAndroidVersion(true);
    }

    public static String photos_path_post_create() {
        return EllaServerUrl + "/v2/photos/story" + getAndroidVersion(true);
    }

    public static String photos_path_post_retry_create() {
        return EllaServerUrl + "/photos/retry" + getCommonParams();
    }

    public static String photos_path_get(int photo_id) {
        return EllaServerUrl + "/v2/photos/" + photo_id + getCommonParams();
    }

    public static String photos_path_delete(int story_id) {
        return EllaServerUrl + "/v2/stories/" + story_id + "/del" + getCommonParams();
    }

    public static String medal_photo_path(int photo_id) {
        return EllaServerUrl + "/v2/photos/badge/" + getCommonParams() + "&photo_id=" + photo_id;
    }

    public static String photos_abuse_get(int photo_id) {
        return EllaServerUrl + "/photos/" + photo_id + "abuse" + getCommonParams();
    }

    public static String photos_likes_path_get(int photo_id) {
        return EllaServerUrl + "/v2/photos/" + photo_id + "/likes" + getCommonParams();
    }

    public static String photos_likes_path_get_cursor(int photo_id, long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/photos/" + photo_id + "/likes" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return photos_likes_path_get(photo_id);
    }

    public static String photos_comments_path_get(int photo_id) {
        return EllaServerUrl + "/v2/photos/" + photo_id + "/comments" + getCommonParams();
    }

    public static String photos_comments_path_get_cursor(int photo_id, long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/v2/photos/" + photo_id + "/comments" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return photos_comments_path_get(photo_id);
    }

    public static String photos_forks_path_get(int photo_id) {
        return EllaServerUrl + "/v2/photos/" + photo_id + "/forks" + getCommonParams();
    }

    public static String timeline_suggest_follow_get(int count, long next_cursor, long exclude_user_id, String exclude_by) {
        return EllaServerUrl + "/v2/timeline/suggest_follow" + getCommonParamsAddOther(count == 1 ? "&count=" + count + "&next_cursor=" + next_cursor + "&exclude_user_id=" + exclude_user_id + "&exclude_by" + exclude_by : "&count=" + count);
    }

    public static String photos_forks_path_get_cursor(int photo_id, long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/v2/photos/" + photo_id + "/forks" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return photos_forks_path_get(photo_id);
    }

    public static String photos_path_post_like(int photo_id) {
        return EllaServerUrl + "/photos/" + photo_id + "/like" + getCommonParams();
    }

    public static String photos_path_delete_like(int photo_id) {
        return EllaServerUrl + "/photos/" + photo_id + "/unlike" + getCommonParams();
    }

    public static String photos_path_post_comments(int photo_id) {
        return EllaServerUrl + "/photos/" + photo_id + "/comments" + getCommonParams();
    }

    public static String photos_path_delete_comments(int photo_id, int comment_id) {
        return EllaServerUrl + "/photos/" + photo_id + "/comments/" + comment_id + "/del" + getCommonParams();
    }

    public static String location_photos_path_get_cursor(String lat, String lng) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng;
    }

    public static String discover_farplace_get_cursor() {
        return EllaServerUrl + "/v2/timeline/far_place" + getCommonParams();
    }

    public static String nearby_hot_path_get_cursor(int page, String lat, String lng) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng + "&page=" + page + "&sort=hot";
    }

    public static String nearby_hot_path_get_cursor(int page, int distance, String lat, String lng) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng + "&distance=" + distance + "&page=" + page + "&sort=hot";
    }

    public static String city_choose_hot_path_get_cursor(int page, String lbs_city_id, String lbs_province_id) {
        String lbs_id;
        if (TextUtil.isValidate(lbs_city_id)) {
            lbs_id = "&lbs_city_id=" + lbs_city_id;
        } else {
            lbs_id = "&lbs_province_id=" + lbs_province_id;
        }
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + lbs_id + "&page=" + page + "&sort=hot";
    }

    public static String city_choose_newer_path_get_cursor(int page, String lbs_city_id, String lbs_province_id) {
        String lbs_id;
        if (TextUtil.isValidate(lbs_city_id)) {
            lbs_id = "&lbs_city_id=" + lbs_city_id;
        } else {
            lbs_id = "&lbs_province_id=" + lbs_province_id;
        }
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + lbs_id + "&page=" + page;
    }

    public static String nearby_newer_path_get_cursor(int page, String lat, String lng) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng + "&page=" + page;
    }

    public static String nearby_newer_path_get_cursor(int page, int distance, String lat, String lng) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng + "&distance=" + distance + "&page=" + page;
    }

    public static String users_photos_path_get(String screen_name, int contentType) {
        String otherValue = "";
        Object[] objArr;
        switch (contentType) {
            case 1:
                objArr = new Object[1];
                objArr[0] = "gif";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 2:
                objArr = new Object[1];
                objArr[0] = "photo";
                otherValue = String.format("&video_type=%s", objArr);
                break;
            case 3:
                objArr = new Object[1];
                objArr[0] = "video";
                otherValue = String.format("&video_type=%s", objArr);
                break;
        }
        return EllaServerUrl + "/v4/users/" + URLEncoder(screen_name) + "/photos" + getCommonParamsHasOther(otherValue);
    }

    public static String nearby_newest_three_recommend_photo_get(String lat, String lng, int distance, int is_recommend) {
        return EllaServerUrl + "/v4/search/location" + getCommonParams() + "&lat=" + lat + "&lng=" + lng + (distance > 0 ? "&distance=" + distance : "") + (is_recommend == 1 ? "&is_recommend=" + is_recommend : "");
    }

    public static String nearby_badge_path_get(String city_id, String province_id, String country_shortname, String lat, String lng) {
        return EllaServerUrl + "/v2/badges/surrounding" + getCommonParams() + "&latitude=" + lat + "&longitude=" + lng + "&city_id=" + city_id + "&province_id=" + province_id + "&country_shortname=" + country_shortname;
    }

    public static String maps_api_geocode_json_path_get(String latString, String lngString, String language) {
        Object[] objArr = new Object[5];
        objArr[0] = MapServerUrl;
        objArr[1] = "/maps/api/geocode/json";
        objArr[2] = latString;
        objArr[3] = lngString;
        objArr[4] = language;
        return String.format("%1$s%2$s?latlng=%3$s,%4$s&result_type=administrative_area_level_1|administrative_area_level_2|locality|country|political&language=%5$s", objArr);
    }

    @SuppressLint({"DefaultLocale"})
    public static String maps_api_place_nearbysearch_json_path_get(String latString, String lngString, int distance, String pagetoken) {
        Object[] objArr;
        if (TextUtil.isValidate(pagetoken)) {
            objArr = new Object[6];
            objArr[0] = MapServerUrl;
            objArr[1] = "/maps/api/place/nearbysearch/json";
            objArr[2] = latString;
            objArr[3] = lngString;
            objArr[4] = Integer.valueOf(distance);
            objArr[5] = pagetoken;
            return String.format("%1$s%2$s?location=%3$s,%4$s&rankby=prominence&radius=%5$d&pagetoken=%6$s", objArr);
        }
        objArr = new Object[5];
        objArr[0] = MapServerUrl;
        objArr[1] = "/maps/api/place/nearbysearch/json";
        objArr[2] = latString;
        objArr[3] = lngString;
        objArr[4] = Integer.valueOf(distance);
        return String.format("%1$s%2$s?location=%3$s,%4$s&rankby=prominence&radius=%5$d", objArr);
    }

    public static String search_maps_api_place_nearbysearch_json_path_get(String latString, String lngString, String keyword, String pagetoken) {
        String keywordStr;
        if (keyword == null || keyword.length() <= 0) {
            keywordStr = "&radius=50";
        } else {
            keywordStr = "&radius=1000&keyword=" + URLEncoder(keyword);
        }
        Object[] objArr;
        if (TextUtil.isValidate(pagetoken)) {
            objArr = new Object[6];
            objArr[0] = MapServerUrl;
            objArr[1] = latString;
            objArr[2] = lngString;
            objArr[3] = pagetoken;
            objArr[4] = keywordStr;
            objArr[5] = "120";
            return String.format("%1$s/maps/api/place/nearbysearch/json?location=%2$s,%3$s&rankby=prominence&pagetoken=%4$s%5$s", objArr);
        }
        objArr = new Object[5];
        objArr[0] = MapServerUrl;
        objArr[1] = latString;
        objArr[2] = lngString;
        objArr[3] = keywordStr;
        objArr[4] = "120";
        return String.format("%1$s/maps/api/place/nearbysearch/json?location=%2$s,%3$s&rankby=prominence%4$s", objArr);
    }

    @SuppressLint({"DefaultLocale"})
    public static String favorites_path_post(int story_id) {
        StringBuilder append = new StringBuilder().append(EllaServerUrl).append("/v2/favorites");
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(story_id);
        return append.append(getCommonParamsAddOther(String.format("&story_id=%1$d", objArr))).toString();
    }

    @SuppressLint({"DefaultLocale"})
    public static String favorites_path_delete(int story_id) {
        StringBuilder append = new StringBuilder().append(EllaServerUrl).append("/v2/favorites");
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(story_id);
        return append.append(getCommonParamsAddOther(String.format("&story_id=%1$d", objArr))).toString();
    }

    public static String favorites_path_get() {
        return EllaServerUrl + "/v2/favorites" + getCommonParams();
    }

    public static String favorites_path_get_cursor(long cursor_id, boolean is_next_cursor) {
        if (cursor_id > 0) {
            return EllaServerUrl + "/v2/favorites" + getCommonParams(getCursor(is_next_cursor), cursor_id);
        }
        return favorites_path_get();
    }

    public static String search_users_path_get(String screen_name, int page, long cursor_id) {
        return EllaServerUrl + "/v4/search/users/" + getSearchPageParams(screen_name, page, cursor_id == 0 ? "" : "&" + getCursor(true) + "=" + cursor_id);
    }

    public static String search_tags_path_get(String tag_name, int page, long cursor_id) {
        String cursor_str;
        if (cursor_id == 0) {
            cursor_str = "";
        } else {
            cursor_str = "&" + getCursor(true) + "=" + cursor_id;
        }
        return EllaServerUrl + "/v2/user_tags/suggest" + getCommonParams() + "&name=" + URLEncoder(tag_name);
    }

    public static String getCursor(boolean is_next_cursor) {
        if (is_next_cursor) {
            return next_cursor_str;
        }
        return prev_cursor_str;
    }

    public static String getCommonParams() {
        return getCommonParamsByAssociate(null);
    }

    public static String getCommonParamsAddOther(String otherValue) {
        return getCommonParamsByOther(otherValue);
    }

    public static String getSearchPageParams(String query, int page, String cursor_str) {
        return getSearchParamsByPage(query, page, cursor_str);
    }

    public static String getSearchPageParams(String query, String cursor_str) {
        return getSearchParamsByPage(query, cursor_str);
    }

    public static String getLimitParams(String limit) {
        return getLimitParamsByAssociate(limit, null);
    }

    public static String getCommonParams(String cursorStr, long cursorId) {
        return getCommonParamsByAssociate(null) + "&" + cursorStr + "=" + cursorId;
    }

    public static String getCommonParamsHaveOther(String cursorStr, long cursorId, String other) {
        return getCommonParamsHasOther(other) + "&" + cursorStr + "=" + cursorId;
    }

    public static String getLimitParams(String limit, String cursorStr, long cursorId) {
        return getLimitParamsByAssociate(limit, null) + "&" + cursorStr + "=" + cursorId;
    }

    public static String getSearchParamsByPage(String query) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        String userParams = "";
        if (TextUtil.isValidate(screen_name) && TextUtil.isValidate(access_token)) {
            userParams = "&" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token;
        }
        return "?" + q_str + "=" + URLEncoder(query) + userParams + getAndroidVersion(false);
    }

    public static String getSearchParamsByPage(String query, int page, String cursor_str) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        String userParams = "";
        if (TextUtil.isValidate(screen_name) && TextUtil.isValidate(access_token)) {
            userParams = "&" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token;
        }
        if (cursor_str == null) {
            cursor_str = "";
        }
        return "?" + q_str + "=" + URLEncoder(query) + "&" + page_str + "=" + page + cursor_str + userParams + getAndroidVersion(false);
    }

    public static String getSearchParamsByPage(String query, String cursor_str) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        String userParams = "";
        if (TextUtil.isValidate(screen_name) && TextUtil.isValidate(access_token)) {
            userParams = "&" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token;
        }
        if (cursor_str == null) {
            cursor_str = "";
        }
        return "?" + q_str + "=" + URLEncoder(query) + cursor_str + userParams + getAndroidVersion(false);
    }

    public static String getCommonParamsByAssociate(String associateValue) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        int user_id = GlobalHelper.getUserId();
        if (!TextUtil.isValidate(screen_name) || !TextUtil.isValidate(access_token)) {
            return getAndroidVersion(true);
        }
        return "?" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token + (associateValue == null ? "" : "&" + associate_str + "=" + associateValue) + getAndroidVersion(false);
    }

    public static String getCommonParamsByPage(String associateValue, int page) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        int user_id = GlobalHelper.getUserId();
        if (!TextUtil.isValidate(screen_name) || !TextUtil.isValidate(access_token)) {
            return getAndroidVersion(true);
        }
        return "?" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token + "&" + page_str + "=" + page + (associateValue == null ? "" : "&" + associate_str + "=" + associateValue) + getAndroidVersion(false);
    }

    public static String getCommonParamsHasOther(String otherValue) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        int user_id = GlobalHelper.getUserId();
        if (!TextUtil.isValidate(screen_name) || !TextUtil.isValidate(access_token)) {
            return getAndroidVersion(true) + otherValue;
        }
        if (otherValue == null) {
            otherValue = "";
        }
        return "?" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token + otherValue + getAndroidVersion(false);
    }

    public static String getCommonParamsByOther(String otherValue) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        int user_id = GlobalHelper.getUserId();
        if (!TextUtil.isValidate(screen_name) || !TextUtil.isValidate(access_token)) {
            return getAndroidVersion(true);
        }
        if (otherValue == null) {
            otherValue = "";
        }
        return "?" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token + otherValue + getAndroidVersion(false);
    }

    public static String getLimitParamsByAssociate(String limit, String associateValue) {
        String screen_name = GlobalHelper.getUserScreenName();
        String access_token = GlobalHelper.getUserAccessToken();
        if (!TextUtil.isValidate(screen_name) || !TextUtil.isValidate(access_token)) {
            return getAndroidVersion(true);
        }
        return "?" + limit_str + "=" + limit + "&" + screen_name_str + "=" + URLEncoder(screen_name) + "&" + access_token_str + "=" + access_token + (associateValue == null ? "" : "&" + associate_str + "=" + associateValue) + getAndroidVersion(false);
    }

    public static String getAndroidVersion(boolean isQuestion) {
        return (isQuestion ? "?" : "&") + "ANDROID_VERSION=" + Constants.SignInRequestCode + "&locale=" + LocaleUtil.getBcp47Language() + "&OSVersion=" + OSVersion + "&store=" + 1;
    }

    public static String URLEncoder(String encodeStr) {
        if (encodeStr == null) {
            return "";
        }
        try {
            return URLEncoder.encode(encodeStr, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return encodeStr;
        }
    }

    public static String album_invited_users_get(long album_id) {
        return EllaServerUrl + "/v2/albums/" + album_id + getCommonParams();
    }

    public static String album_photos_get(long album_id) {
        return EllaServerUrl + "/v2/albums/" + album_id + "/photos" + getCommonParams();
    }

    public static String album_photos_get_cursor(long album_id, long cursor_id, boolean is_next_cursor) {
        if (cursor_id <= 0 || !is_next_cursor) {
            return album_photos_get(album_id);
        }
        return EllaServerUrl + "/v2/albums/" + album_id + "/photos" + getCommonParams(getCursor(is_next_cursor), cursor_id);
    }

    public static String new_album_upload_info_path_post() {
        return EllaServerUrl + "/v2/albums" + getAndroidVersion(true);
    }

    public static String album_accept_invite_path_post() {
        return EllaServerUrl + "/v2/albums/accept_invite" + getAndroidVersion(true);
    }

    public static String edit_album_upload_info_path_put(long album_id) {
        return EllaServerUrl + "/v2/albums/" + album_id + getAndroidVersion(true);
    }

    public static String hstory_album_photos_get() {
        return EllaServerUrl + "/v2/albums/history" + getCommonParams();
    }

    public static String album_quit_album_path_post() {
        return EllaServerUrl + "/v2/albums/quit";
    }

    public static String tag_info_path_get(String tagName) {
        return EllaServerUrl + "/v2/user_tags/topic?name=" + URLEncoder(tagName) + getAndroidVersion(false);
    }

    public static String put_gifs_to_album_path_put() {
        return EllaServerUrl + "/v2/albums/batch_add" + getCommonParams();
    }

    public static String delete_gifs_from_album_path_post(String params) {
        return EllaServerUrl + "/v2/albums/batch_remove" + getCommonParams();
    }

    public static String me_need_upgrade_path_post() {
        return EllaServerUrl + "/me/beta_version" + getAndroidVersion(true);
    }

    public static String test_need_upgrade_path_send() {
        return EllaServerUrl + "/android_test/version_info" + getAndroidVersion(true);
    }

    public static String me_incr_path_post() {
        return EllaServerUrl + "/me/incr" + getAndroidVersion(true);
    }

    public static String me_invite_codes_path_get() {
        return EllaServerUrl + "/me/invite_codes" + getCommonParams();
    }

    public static String upload_weibo_twitter_name_post() {
        return EllaServerUrl + "/me/update_name_used_when_share" + getAndroidVersion(true);
    }

    public static String submit_feedback_post() {
        return EllaServerUrl + "/v2/feedback/upload" + getCommonParams();
    }

    public static String verify_twitter_token() {
        return EllaServerUrl + "/register/verify_token" + getCommonParams();
    }

    public static String request_weibo_access(String access_token) {
        return "https://api.weibo.com/oauth2/access_token?client_id=927658127&client_secret=c4bb19f183389011bd02539909165029&grant_type=refresh_token&redirect_uri=https://api.weibo.com/oauth2/default.html&refresh_token=" + access_token;
    }

    public static String get_article_by_id_path_get(String params) {
        return EllaServerUrl + "/v2/articles/" + params + getCommonParams();
    }

    public static String get_giphy_library_path_get(String params) {
        return EllaServerUrl + "/v2/materials/giphy" + getCommonParams();
    }

    public static String post_giphy_unfav_path_post() {
        return EllaServerUrl + "/v2/materials/unfav_giphy" + getAndroidVersion(true);
    }

    public static String post_test_user_code() {
        return EllaServerUrl + "/android_test/verify_code" + getAndroidVersion(true);
    }

    public static String get_google_translate_path_get(String word) {
        return EllaServerUrl + "/v2/materials/translate?q=" + word + getAndroidVersion(false);
    }

    public static String me_also_followed_get(int page) {
        return EllaServerUrl + "/me/also_followed" + getCommonParamsByPage(null, page);
    }

    public static String get_giphy_search_url(boolean isHot, String searchKey, int limit, int offset) {
        String str;
        StringBuilder append = new StringBuilder().append("http://api.giphy.com/v1/gifs/").append(isHot ? "trending" : "search").append("?api_key=3oEdv88I8GDdix6wh2&limit=").append(limit).append("&offset=").append(offset);
        if (isHot) {
            str = "";
        } else {
            str = "&q=" + URLEncoder(searchKey);
        }
        return append.append(str).toString();
    }

    public static String get_google_translate_url(String words) {
        return EllaServerUrl + "/v2/materials/translate?q=" + URLEncoder(words) + getAndroidVersion(false);
    }

    public static String get_giphy_favourite_url() {
        return EllaServerUrl + "/v2/materials/fav_giphy" + getAndroidVersion(true);
    }

    public static String get_giphy_unfavourite_url() {
        return EllaServerUrl + "/v2/materials/unfav_giphy" + getAndroidVersion(true);
    }

    public static String get_airbnb_price_url(String lat, String lng, String currency) {
        double dLat = Double.parseDouble(lat);
        double dLng = Double.parseDouble(lng);
        return "https://api.airbnb.com/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty&locale=en-US&currency=" + currency + "&_format=for_search_results_with_minimal_pricing&_limit=0&_offset=0&fetch_facets=false&guests=1&ib=false&ib_add_photo_flow=true&sw_lat=" + (dLat - 20.0d) + "&sw_lng=" + (dLng - 20.0d) + "&ne_lat=" + (dLat + 20.0d) + "&ne_lng=" + (dLng + 20.0d) + "&search_by_map=true&min_bathrooms=0&min_bedrooms=0&min_beds=1&sort=1";
    }

    public static String get_unbind_phone_and_info_url() {
        String str = EllaServerUrl + "/users/unbind_phone_and_info?screen_name=%s&access_token=%s";
        Object[] objArr = new Object[2];
        objArr[0] = GlobalHelper.getUserScreenName();
        objArr[1] = GlobalHelper.getUserAccessToken();
        return String.format(str, objArr);
    }

    public static String get_IM_recent_messages_url(long id, int fold) {
        String otherValue = "";
        if (id != -1) {
            otherValue = "&next_cursor=" + id;
        }
        return EllaServerUrl + "/im/recent_messages" + getCommonParamsAddOther((otherValue + "&fold=" + fold) + "&cursor_type=kafka");
    }

    public static String get_IM_delete_chat_url() {
        return EllaServerUrl + "/im/delete" + getAndroidVersion(true);
    }

    public static String get_IM_histroy_messages_url_for_user(int other_id, long kafka_id) {
        StringBuilder append;
        Object[] objArr;
        if (kafka_id == 0) {
            append = new StringBuilder().append(EllaServerUrl).append("/im/old_messages");
            objArr = new Object[1];
            objArr[0] = Integer.valueOf(other_id);
            return append.append(getCommonParamsAddOther(String.format("&target_user_id=%s", objArr))).toString();
        }
        append = new StringBuilder().append(EllaServerUrl).append("/im/old_messages");
        objArr = new Object[2];
        objArr[0] = Long.valueOf(kafka_id);
        objArr[1] = Integer.valueOf(other_id);
        return append.append(getCommonParamsAddOther(String.format("&next_cursor=%s&target_user_id=%s", objArr))).toString();
    }

    public static String get_IM_video_token_url() {
        return EllaServerUrl + "/im/video" + getCommonParams();
    }

    public static String get_IM_reply_by_notify_url() {
        return EllaServerUrl + "/im/http_send" + getCommonParams();
    }

    public static String get_IM_set_recent_msg_id_url() {
        return EllaServerUrl + "/im/set_read_to" + getCommonParams();
    }

    public static String get_IM_set_video_upload_done() {
        return EllaServerUrl + "/im/video_done" + getAndroidVersion(true);
    }

    public static String get_TimeLine_set_video_upload_done() {
        return EllaServerUrl + "/v2/photos/story_done" + getAndroidVersion(true);
    }

    public static String get_audio_played_url() {
        return EllaServerUrl + "/im/set_audioplayed" + getCommonParams();
    }

    public static String get_like_photo_url(long next_cursor) {
        String otherValue = "";
        if (next_cursor != 0) {
            otherValue = "&next_cursor=" + next_cursor;
        }
        return EllaServerUrl + "/v4/my_likes" + getCommonParamsAddOther(otherValue);
    }

    public static String get_filter_store_list_url(long next_cursor) {
        String otherValue = "";
        if (next_cursor != 0) {
            otherValue = "&next_cursor=" + next_cursor;
        }
        return EllaServerUrl + "/v2/filters" + getCommonParamsAddOther(null) + otherValue;
    }

    public static String getSingleFilterDetailUrl(int filterId) {
        StringBuilder append = new StringBuilder().append("url : ");
        Object[] objArr = new Object[3];
        objArr[0] = EllaServerUrl + "v2/filters/";
        objArr[1] = Integer.valueOf(filterId);
        objArr[2] = getCommonParams();
        LogUtil.d("huangweijie", append.append(String.format("%s%s%s", objArr)).toString());
        Object[] objArr2 = new Object[3];
        objArr2[0] = EllaServerUrl + "/v2/filters/";
        objArr2[1] = Integer.valueOf(filterId);
        objArr2[2] = getCommonParams();
        return String.format("%s%s%s", objArr2);
    }

    public static String getBuySingleFilterUrl() {
        return EllaServerUrl + "/v2/filters/purchase" + getCommonParams();
    }

    public static String getMSCSKeyUrl() {
        return EllaServerUrl + "/v2/mscv/key" + getCommonParams();
    }

    public static String getRecommendOt() {
        return EllaServerUrl + "/recommend_ot" + getAndroidVersion(true);
    }

    public static String getSearchOtUrl(String value, String intent, String type, long page) {
        Object[] objArr = new Object[4];
        objArr[0] = page == 0 ? "" : "&page=" + page;
        objArr[1] = URLEncoder(value);
        objArr[2] = intent;
        objArr[3] = URLEncoder(type);
        return EllaServerUrl + "/v2/search/ot" + getCommonParamsHasOther(String.format("%s&value=%s&intent=%s&type=%s", objArr));
    }

    public static String getNewTagTopicUrl(String value, String type, String zh, String en) {
        Object[] objArr = new Object[4];
        objArr[0] = URLEncoder(value);
        objArr[1] = URLEncoder(type);
        objArr[2] = URLEncoder(zh);
        objArr[3] = URLEncoder(en);
        return EllaServerUrl + "/ot/topic" + getCommonParamsHasOther(String.format("&value=%s&type=%s&zh=%s&en=%s", objArr));
    }

    public static String getAccuWeatherLBSUrl(String raw_gps) {
        Object[] objArr = new Object[2];
        objArr[0] = raw_gps;
        objArr[1] = AccuWeatherLBSApiKey;
        return String.format("https://api.accuweather.com/locations/v1/cities/geoposition/search.json?q=%s&apikey=%s&language=zh-cn", objArr);
    }

    public static String getRecommendCategories() {
        return EllaServerUrl + "/recommend_categories" + getAndroidVersion(true);
    }

    public static String getPromoteTagUrl() {
        return EllaServerUrl + "/ot/promotes" + getCommonParams();
    }

    public static String getShareWeiboUrl() {
        return "https://c.api.weibo.com/2/statuses/upload/biz.json";
    }

    public static String getShareTextWeiboUrl() {
        return "https://c.api.weibo.com/2/statuses/update/biz.json";
    }

    public static String getAudioIndexUrl() {
        return EllaServerUrl + "/v2/audios/index" + getCommonParams();
    }

    public static String getCollectionsIndexUrl() {
        return EllaServerUrl + "/v2/collections/index" + getCommonParams();
    }

    public static String getSubscribeUrl() {
        return EllaServerUrl + "/v2/collections/subscribe";
    }

    public static String getUnSubscribeUrl() {
        return EllaServerUrl + "/v2/collections/unsubscribe";
    }

    public static String getDiscoverCollectionsUrl() {
        return EllaServerUrl + "/v2/collections/my" + getCommonParams();
    }

    public static String getDiscoverEditorCollectionsUrl() {
        return EllaServerUrl + "/v2/editor_collections/show_on_discover" + getCommonParams();
    }

    public static String get_signature() {
        return EllaServerUrl + "/show_audios" + getCommonParams();
    }

    public static String getDiscoverPromotionsInfo() {
        return EllaServerUrl + "/my_photos" + getCommonParams();
    }

    public static String promotions_cancel_get(String id) {
        Object[] objArr = new Object[1];
        objArr[0] = id;
        return EllaServerUrl + "/videos_rej" + getCommonParamsHasOther(String.format("&id=%s", objArr));
    }

    public static String getDoPromotionsOpt(String id) {
        Object[] objArr = new Object[1];
        objArr[0] = id;
        return EllaServerUrl + "/videos_acc" + getCommonParamsHasOther(String.format("&id=%s", objArr));
    }

    public static String getAudioDetailUrl(int id, int page) {
        Object[] objArr = new Object[2];
        objArr[0] = page == 0 ? "" : "&page=" + page;
        objArr[1] = Integer.valueOf(id);
        return EllaServerUrl + "/v2/audios/show" + getCommonParamsHasOther(String.format("%s&id=%s", objArr));
    }

    public static String getPurchaseAudioUrl() {
        return EllaServerUrl + "/v2/audios/purchase" + getCommonParams();
    }

    public static String getCollectionDetail(int collection_id, long cursor_id, boolean isEditor) {
        String apiStr;
        Object[] objArr = new Object[2];
        objArr[0] = cursor_id == 0 ? "" : "&" + getCursor(true) + "=" + cursor_id;
        objArr[1] = Integer.valueOf(collection_id);
        String otherValue = String.format("%s&id=%s", objArr);
        if (isEditor) {
            apiStr = "/v2/editor_collections/show";
        } else {
            apiStr = "/v2/collections/show";
        }
        return EllaServerUrl + apiStr + getCommonParamsHasOther(otherValue);
    }

    public static String getCollectionDetailUsers(int collection_id, int page, boolean isEditor) {
        String apiStr;
        Object[] objArr = new Object[2];
        objArr[0] = "&page=" + page;
        objArr[1] = Integer.valueOf(collection_id);
        String otherValue = String.format("%s&id=%s", objArr);
        if (isEditor) {
            apiStr = "/v2/editor_collections/show_users";
        } else {
            apiStr = "/v2/collections/show_users";
        }
        return EllaServerUrl + apiStr + getCommonParamsHasOther(otherValue);
    }

    public static String getCollectionSaveOrder() {
        return EllaServerUrl + "/v2/collections/save_order";
    }

    public static String getCollectionsSubscribers(int collection_id, int page) {
        Object[] objArr = new Object[2];
        objArr[0] = "&page=" + page;
        objArr[1] = Integer.valueOf(collection_id);
        return EllaServerUrl + "/v2/collections/subscribers" + getCommonParamsHasOther(String.format("%s&id=%s", objArr));
    }

    public static String getSampleUrls(int id, int story_id) {
        return EllaServerUrl + "/v4/photos/" + id + getCommonParamsHasOther("&story_id=" + story_id);
    }

    public static String get_timeline_video_change_describe() {
        return EllaServerUrl + "/v2/photos/update_info" + getCommonParams();
    }

    public static String get_track_albums(String ids) {
        return EllaServerUrl + "/v2/audios/show_albums_of_tracks" + getCommonParamsHasOther(ids);
    }

    public static String getNewTimelineBanner() {
        return EllaServerUrl + "/v2/articles/timeline" + getCommonParams();
    }

    public static String getActiveList() {
        return EllaServerUrl + "/me/models_info" + getCommonParams();
    }

    public static String getDiamondPurchaseProductListUrl() {
        return EllaServerUrl + "/v2/in_app_purchase/android_product_list" + getCommonParams();
    }

    public static String getAliPayOrderInfoUrl() {
        return EllaServerUrl + "/v2/in_app_purchase/gen_alipay_android" + getAndroidVersion(true);
    }

    public static String getAliPayOrderStatusUrl(String other) {
        return EllaServerUrl + "/v2/in_app_purchase/query_alipay_status" + getCommonParamsHasOther(other);
    }

    public static String getBoundQueryPhotosUrl(String other) {
        return EllaServerUrl + "/v2/search/bound_query_photos" + getCommonParamsHasOther(other);
    }

    public static String getBoundQueryList(String other) {
        return EllaServerUrl + "/v2/search/bound_query" + getCommonParamsHasOther(other);
    }
}