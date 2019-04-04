package cn.nineton.onetake.util;

//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import cn.nineton.onetake.bean.IExceptionCallback;
import cn.nineton.onetake.bean.TimelineBean;

public class PhotoParams extends BaseParams implements Serializable, Cloneable {
    public static final String access_token_str = "access_token";
    public static final String comment_str = "comment";
    public static final String filter_id_str = "filter_id";
    public static final String filter_strongness_str = "filter_strongness";
    public static final String final_picture_url_str = "final_picture_url";
    public static final String fork_from_id_str = "fork_from_id";
    public static final String frame_id_str = "frame_id";
    public static final String hone_str = "hone";
    public static final String htwo_str = "htwo";
    public static final String only_self_visible_str = "only_self_visible";
    public static final String original_id_str = "original_id";
    public static final String original_photo_url_str = "original_photo_url";
    public static final String photos_str = "photos";
    public static final String screen_name_str = "screen_name";
    public static final String sticker_ids_str = "sticker_ids";
    public static final String story_fork_from_id_str = "story_fork_from_id";
    public static final String story_original_id_str = "story_original_id";
    public static final String story_unmodified_str = "story_unmodified";
    public static final String tags_str = "tags";
    public static final String text_str = "text";
    public static final String uuid_str = "uuid";
    public static final String vector_url_str = "vector_url";
    private Map<String, Object> mPhotoMap;
    private boolean needUpload = true;

    public static PhotoParams parse(String jsonString, IExceptionCallback callback) {
        if (!TextUtil.isValidate(jsonString)) {
            return null;
        }
        try {
            return (PhotoParams) JsonParserUtil.deserializeByJson(jsonString, new TypeToken<PhotoParams>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            callback.doException();
            return null;
        }
    }

    public static String getUUIDJSONObject(String uuid) {
        Map<String, String> uuidMap = new HashMap();
        uuidMap.put(uuid_str, uuid);
        return new JSONObject(uuidMap).toString();
    }

    public Map<String, Object> getPhotoMap() {
        if (TextUtil.isNull(this.mPhotoMap)) {
            this.mPhotoMap = new HashMap();
        }
        return this.mPhotoMap;
    }

    public void remove(String key) {
        if (TextUtil.isNull(this.mPhotoMap)) {
            this.mPhotoMap = new HashMap();
        }
        if (this.mPhotoMap.containsKey(key)) {
            this.mPhotoMap.remove(key);
        }
    }

    public void setPhotoMap(TimelineBean timelineBean) {
        if (TextUtil.isNull(this.mPhotoMap)) {
            this.mPhotoMap = new HashMap();
        }
        this.mPhotoMap.put(original_photo_url_str, timelineBean.video_url);
        this.mPhotoMap.put(final_picture_url_str, timelineBean.video_url);
        this.mPhotoMap.put(sticker_ids_str, timelineBean.sticker_ids);
        this.mPhotoMap.put(fork_from_id_str, Integer.valueOf(timelineBean.id));
        this.mPhotoMap.put(original_id_str, Integer.valueOf(timelineBean.original_id == 0 ? timelineBean.id : timelineBean.original_id));
        this.mPhotoMap.put(hone_str, timelineBean.hone);
        this.mPhotoMap.put(htwo_str, timelineBean.htwo);
        this.mPhotoMap.put(text_str, timelineBean.text);
        this.mPhotoMap.put(frame_id_str, Integer.valueOf(timelineBean.frame_id));
        this.mPhotoMap.put(filter_id_str, timelineBean.filter_id);
        this.mPhotoMap.put(filter_strongness_str, Float.valueOf(timelineBean.filter_strongness));
    }

    public void removePhotoMapWithForkInfo() {
        if (TextUtil.isNull(this.mPhotoMap)) {
            this.mPhotoMap = new HashMap();
            return;
        }
        if (this.mPhotoMap.containsKey(original_photo_url_str)) {
            this.mPhotoMap.remove(original_photo_url_str);
        }
        if (this.mPhotoMap.containsKey(vector_url_str)) {
            this.mPhotoMap.remove(vector_url_str);
        }
        if (this.mPhotoMap.containsKey(final_picture_url_str)) {
            this.mPhotoMap.remove(final_picture_url_str);
        }
        if (this.mPhotoMap.containsKey(fork_from_id_str)) {
            this.mPhotoMap.remove(fork_from_id_str);
        }
        if (this.mPhotoMap.containsKey(original_id_str)) {
            this.mPhotoMap.remove(original_id_str);
        }
        if (this.mPhotoMap.containsKey(hone_str)) {
            this.mPhotoMap.remove(hone_str);
        }
        if (this.mPhotoMap.containsKey(htwo_str)) {
            this.mPhotoMap.remove(htwo_str);
        }
    }

    public boolean isNeedUpload() {
        return this.needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    public Object clone() {
        try {
            PhotoParams photoParams = (PhotoParams) super.clone();
            photoParams.mPhotoMap = new HashMap();
            if (this.mPhotoMap == null) {
                return photoParams;
            }
            photoParams.mPhotoMap.putAll(this.mPhotoMap);
            return photoParams;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}