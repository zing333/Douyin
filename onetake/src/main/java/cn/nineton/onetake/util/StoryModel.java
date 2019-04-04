package cn.nineton.onetake.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Build.VERSION;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.im.IMVideoTokenBean;
//import com.blink.academy.onetake.bean.imageproperty.ImagePropertyBean;
//import com.blink.academy.onetake.bean.tag.OfficialTagBean;
//import com.blink.academy.onetake.http.params.PhotoParams;
//import com.blink.academy.onetake.http.upload.UploadRequestManager;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.helper.GlobalHelper;
//import com.blink.academy.onetake.support.response.BasePOIResponse.LBSPOIModel;
//import com.blink.academy.onetake.support.utils.JsonUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.IMVideoTokenBean;
import cn.nineton.onetake.bean.ImagePropertyBean;
import cn.nineton.onetake.bean.OfficialTagBean;

public class StoryModel implements Serializable {
    public static final int CameraType = 1;
    public static final int DraftType = 5;
    public static final int GiphyType = 4;
    public static final int LIVE_PHOTO = 3;
    public static final int LONG_VIDEO = 7;
    public static final int LocalPictureType = 6;
    public static final float MAXPERCENT = 100.0f;
    public static final String TAG = StoryModel.class.getSimpleName();
    public static final int VideoAlbumType = 2;
    private String accbody;
    private List<Integer> albumIds;
    private String amapbody;
    private int cameraType;
    private String comment;
    private int createPhotoID;
    private long creation_date;
    private String filterID;
    private String genbody;
    private String giphyGifUrl;
    private String giphyId;
    private String giphyKeyword;
    private String giphySlug;
    private String giphySourcePostUrl;
    private String giphySourceTld;
    private String gzhcnbody;
    private boolean hasVideoLocation;
    private int has_audio;
    private int height;
    private boolean isFailed = false;
    private boolean isLongThumbnailFailed = false;
    private boolean isPreviewFailed = false;
    private boolean isShareChatMoment;
    private boolean isShareToInstagram;
    private boolean isUploadSuc = false;
    private boolean isVideoFailed = false;
    private String latlngradius;
//    private LBSPOIModel lbsPOIModel;
    private double longThumbnailPercentage = 0.0d;
    private String longThumbnailToken;
    private IMVideoTokenBean mIMVideoTokenBean;
    private int mMaxFaceCount;
//    private NewDBLBSDataModel mNewDBLBSDataModel;
    private boolean onlySelfVisiable;
    private ArrayList<OfficialTagBean> ots;
    private double previewPercentage;
    private String previewToken;
    private String preview_ave_info;
    private String recbody;
    private int steps;
    private List<String> tags;
    private String timeStamp;
    private String title;
    private int totalSteps;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;
    private String uuid;
    private int vType;
    private String videoInfo;
    private double videoPercentage;
    private String videoSharePath;
    private int videoSource;
    private String videoToken;
    private String web_url;
    private String weiboAccessToken;
    private int width;

    public void setVideoSharePath(String videoSharePath) {
        this.videoSharePath = videoSharePath;
    }

    public String getVideoSharePath() {
        return this.videoSharePath;
    }

    public int getHas_audio() {
        return this.has_audio;
    }

    public void setHas_audio(int has_audio) {
        this.has_audio = has_audio;
    }

    public void setHas_audio(boolean has_audio) {
        this.has_audio = has_audio ? 1 : 0;
    }

    public boolean isShareToInstagram() {
        return this.isShareToInstagram;
    }

    public void setShareToInstagram(boolean shareToInstagram) {
        this.isShareToInstagram = shareToInstagram;
    }

    public float averagePercent() {
        LogUtil.d("xiaoxi", "storyModel totalSteps : " + this.totalSteps + "\n , video : " + this.videoPercentage + "\n , preview : " + this.previewPercentage + "\n , longThumb : " + this.longThumbnailPercentage + "\n , total -1 : " + (((float) (this.totalSteps - 1)) * 100.0f));
        if (this.totalSteps == 1) {
            return 100.0f;
        }
        return 100.0f * ((float) (((this.videoPercentage + this.previewPercentage) + this.longThumbnailPercentage) / ((double) (this.totalSteps - 1))));
    }

    public boolean hadFailed() {
        return this.isFailed || this.isPreviewFailed || this.isLongThumbnailFailed || this.isVideoFailed;
    }

    public boolean isShareChatMoment() {
        return this.isShareChatMoment;
    }

    public void setShareChatMoment(boolean shareChatMoment) {
        this.isShareChatMoment = shareChatMoment;
    }

    public boolean hadCompleteSuccess() {
        boolean result;
        if (this.isFailed || this.isPreviewFailed || this.isLongThumbnailFailed || this.isVideoFailed) {
            result = false;
        } else {
            result = true;
        }
        if (result && this.steps == this.totalSteps) {
            return true;
        }
        return false;
    }

    public boolean hadCompleteFailed() {
        boolean result;
        if (this.isPreviewFailed || this.isLongThumbnailFailed || this.isVideoFailed) {
            result = true;
        } else {
            result = false;
        }
        if (result && this.steps == this.totalSteps) {
            result = true;
        } else {
            result = false;
        }
        if (result || this.isFailed) {
            return true;
        }
        return false;
    }

    @SuppressLint({"DefaultLocale"})
    public void refreshUUID() {
        String time = String.valueOf(System.currentTimeMillis());
        this.uuid = String.format("%1$s.%2$s000-%3$s", new Object[]{time.substring(0, 10), time.substring(10, 13), String.valueOf(GlobalHelper.getUserId())});
    }

    public IMVideoTokenBean getIMVideoTokenBean() {
        return this.mIMVideoTokenBean;
    }

    public void setIMVideoTokenBean(IMVideoTokenBean IMVideoTokenBean) {
        this.mIMVideoTokenBean = IMVideoTokenBean;
    }

    public boolean hadCreatePhotoID() {
        return getCreatePhotoID() != 0;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getVideoPercentage() {
        return this.videoPercentage;
    }

    public void setVideoPercentage(double videoPercentage) {
        this.videoPercentage = videoPercentage;
    }

    public double getPreviewPercentage() {
        return this.previewPercentage;
    }

    public void setPreviewPercentage(double previewPercentage) {
        this.previewPercentage = previewPercentage;
    }

    public double getLongThumbnailPercentage() {
        return this.longThumbnailPercentage;
    }

    public void setLongThumbnailPercentage(double longThumbnailPercentage) {
        this.longThumbnailPercentage = longThumbnailPercentage;
    }

    public boolean isHasVideoLocation() {
        return this.hasVideoLocation;
    }

    public void setHasVideoLocation(boolean hasVideoLocation) {
        this.hasVideoLocation = hasVideoLocation;
    }

    public boolean isFailed() {
        return this.isFailed;
    }

    public void setFailed(boolean failed) {
        this.isFailed = failed;
    }

    public boolean isVideoFailed() {
        return this.isVideoFailed;
    }

    public void setVideoFailed(boolean videoFailed) {
        this.isVideoFailed = videoFailed;
    }

    public boolean isPreviewFailed() {
        return this.isPreviewFailed;
    }

    public void setPreviewFailed(boolean previewFailed) {
        this.isPreviewFailed = previewFailed;
    }

    public boolean isLongThumbnailFailed() {
        return this.isLongThumbnailFailed;
    }

    public void setLongThumbnailFailed(boolean longThumbnailFailed) {
        this.isLongThumbnailFailed = longThumbnailFailed;
    }

    public synchronized int getSteps() {
        return this.steps;
    }

    public synchronized void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTotalSteps() {
        return this.totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getCreatePhotoID() {
        return this.createPhotoID;
    }

    public void setCreatePhotoID(int createPhotoID) {
        this.createPhotoID = createPhotoID;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVideoToken() {
        return this.videoToken;
    }

    public void setVideoToken(String videoToken) {
        this.videoToken = videoToken;
    }

    public String getPreviewToken() {
        return this.previewToken;
    }

    public void setPreviewToken(String previewToken) {
        this.previewToken = previewToken;
    }

    public String getLongThumbnailToken() {
        return this.longThumbnailToken;
    }

    public void setLongThumbnailToken(String longThumbnailToken) {
        this.longThumbnailToken = longThumbnailToken;
    }

    public int getCameraType() {
        return this.cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getTagName() {
        return this.tags;
    }

    public void setTagList(List<String> tags) {
        this.tags = tags;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFilterID() {
        return this.filterID;
    }

    public int getVideoSource() {
        return this.videoSource;
    }

    public void setVideoSource(int videoSource) {
        this.videoSource = videoSource;
    }

    public void setVideoSource(int dataFrom, int dataType) {
        if (3 == dataFrom) {
            this.videoSource = 5;
        } else if (dataFrom == 0) {
            this.videoSource = 1;
        } else if (1 != dataFrom) {
        } else {
            if (1 == dataType) {
                this.videoSource = 6;
            } else if (dataType == 0) {
                this.videoSource = 2;
            } else if (2 == dataType) {
                this.videoSource = 7;
            }
        }
    }

    public String getGiphyGifUrl() {
        return this.giphyGifUrl;
    }

    public void setGiphyGifUrl(String giphyGifUrl) {
        this.giphyGifUrl = giphyGifUrl;
    }

    public String getGiphyId() {
        return this.giphyId;
    }

    public void setGiphyId(String giphyId) {
        this.giphyId = giphyId;
    }

    public String getGiphySourceTld() {
        return this.giphySourceTld;
    }

    public void setGiphySourceTld(String giphySourceTld) {
        this.giphySourceTld = giphySourceTld;
    }

    public String getGiphySourcePostUrl() {
        return this.giphySourcePostUrl;
    }

    public void setGiphySourcePostUrl(String giphySourcePostUrl) {
        this.giphySourcePostUrl = giphySourcePostUrl;
    }

    public String getGiphySlug() {
        return this.giphySlug;
    }

    public void setGiphySlug(String giphySlug) {
        this.giphySlug = giphySlug;
    }

    public String getGiphyKeyword() {
        return this.giphyKeyword;
    }

    public void setGiphyKeyword(String giphyKeyword) {
        this.giphyKeyword = giphyKeyword;
    }

    public void setWeiboAccessToken(String weiboAccessToken) {
        this.weiboAccessToken = weiboAccessToken;
    }

    public void setTwitterAccessToken(String twitterAccessToken) {
        this.twitterAccessToken = twitterAccessToken;
    }

    public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
        this.twitterAccessTokenSecret = twitterAccessTokenSecret;
    }

    public Map<String, Object> getSyncTo() {
        Map<String, Object> value;
        Map<String, Object> tokens = new HashMap();
        if (TextUtil.isValidate(this.weiboAccessToken)) {
            value = new HashMap();
            value.put("access_token", this.weiboAccessToken);
            tokens.put("weibo_my", value);
        }
        if (TextUtil.isValidate(this.twitterAccessToken) && TextUtil.isValidate(this.twitterAccessTokenSecret)) {
            value = new HashMap();
            value.put("access_token", this.twitterAccessToken);
            value.put("access_token_secret", this.twitterAccessTokenSecret);
            tokens.put("twitter", value);
        }
        return tokens;
    }

    public void setFilterID(String filterID) {
        this.filterID = filterID;
    }

    public boolean isOnlySelfVisiable() {
        return this.onlySelfVisiable;
    }

    public void setOnlySelfVisiable(boolean onlySelfVisiable) {
        this.onlySelfVisiable = onlySelfVisiable;
    }

    public void setAlbumIds(List<Integer> albumIds) {
        this.albumIds = albumIds;
    }

    public List<Integer> getAlbumIds() {
        return this.albumIds;
    }

    public int[] getAlbumIdsArray() {
        int[] ids = new int[getAlbumIds().size()];
        for (int i = 0; i < getAlbumIds().size(); i++) {
            ids[i] = ((Integer) getAlbumIds().get(i)).intValue();
        }
        return ids;
    }

//    public NewDBLBSDataModel getNewDBLBSDataModel() {
//        if (this.mNewDBLBSDataModel == null) {
//            this.mNewDBLBSDataModel = new NewDBLBSDataModel();
//        }
//        return this.mNewDBLBSDataModel;
//    }
//
//    public void setNewDBLBSDataModel(NewDBLBSDataModel newDBLBSDataModel) {
//        this.mNewDBLBSDataModel = newDBLBSDataModel;
//    }
//
//    public LBSPOIModel getLbsPOIModel() {
//        return this.lbsPOIModel;
//    }
//
//    public void setLbsPOIModel(LBSPOIModel lbsPOIModel) {
//        this.lbsPOIModel = lbsPOIModel;
//    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public long getCreation_date() {
        return this.creation_date;
    }

    public String getPreview_ave_info() {
        return this.preview_ave_info;
    }

    public void setPreview_ave_info(String preview_ave_info) {
        this.preview_ave_info = preview_ave_info;
    }

    public ArrayList<OfficialTagBean> getOts() {
        return this.ots;
    }

    public void setOts(ArrayList<OfficialTagBean> ots) {
        this.ots = ots;
    }

    public void setRecbody(JSONObject recbody) {
        if (recbody != null) {
            this.recbody = recbody.toString();
        }
    }

    public void setGzhcnbody(JSONObject gzhcnbody) {
        if (gzhcnbody != null) {
            this.gzhcnbody = gzhcnbody.toString();
        }
    }

    public void setGenbody(JSONObject genbody) {
        if (genbody != null) {
            this.genbody = genbody.toString();
        }
    }

    public void setAmapbody(JSONObject amapbody) {
        if (amapbody != null) {
            this.amapbody = amapbody.toString();
        }
    }

    public void setAccbody(JSONObject accbody) {
        if (accbody != null) {
            this.accbody = accbody.toString();
        }
    }

    public void clearAccbody() {
        this.accbody = null;
    }

    public void setLatlngradius(String latlngradius) {
        this.latlngradius = latlngradius;
    }

    public String getVideoInfo() {
        return this.videoInfo;
    }

    public void setVideoInfo(String uploadString) {
        this.videoInfo = uploadString;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getWeb_url() {
        return this.web_url;
    }

    public String getParams() {
        if (TextUtil.isNull(getUuid())) {
            refreshUUID();
        }
        Map<String, Object> params = new HashMap();
        Map<String, Object> photosParams = new HashMap();
        if (!isOnlySelfVisiable()) {
            params.put("album_ids", getAlbumIdsArray());
        }
        if (getvType() == 1) {
            photosParams.put("meta_data", ImagePropertyBean.getInstance().getMapPragram());
            ImagePropertyBean.getInstance().clear();
        }
        photosParams.put("has_audio", getHas_audio());
        photosParams.put(PhotoParams.text_str, getComment());
        photosParams.put("title", getTitle());
        photosParams.put("width", getWidth());
        photosParams.put("height", getHeight());
        photosParams.put(PhotoParams.uuid_str, getUuid());
        photosParams.put(PhotoParams.filter_id_str, getFilterID());
        photosParams.put("preview_ave_info", getPreview_ave_info());
        photosParams.put("creation_date", getCreation_date());
        List<Map<String, Object>> photosParamsList = new ArrayList();
        photosParamsList.add(photosParams);
        params.put(PhotoParams.photos_str, photosParamsList);
        params.put("sync_to", getSyncTo());
        params.put(PhotoParams.only_self_visible_str, isOnlySelfVisiable());
        params.put("camera_type", getCameraType());
        if (getvType() == 2) {
            params.put("edit_info", getVideoInfo());
        }
        params.put("video_source", getVideoSource());
        if (getVideoSource() == 4) {
            params.put("giphy_downsized_url", getGiphyGifUrl());
            params.put("giphy_id", getGiphyId());
            params.put("giphy_source_tld", getGiphySourceTld());
            params.put("giphy_source_post_url", getGiphySourcePostUrl());
            params.put("giphy_slug", getGiphySlug());
            params.put("giphy_keyword", getGiphyKeyword());
        }
        params.put("geo_new", "fake_geo_new");
        if (TextUtil.isValidate(getTagName())) {
            params.put(PhotoParams.tags_str, getTagName());
        }
        params.put("screen_name", GlobalHelper.getUserScreenName());
        params.put("access_token", GlobalHelper.getUserAccessToken());
        params.put("phone_model", Build.MODEL);
        params.put("phone_os_version", VERSION.RELEASE);
        params.put("ANDROID_VERSION", Integer.valueOf(Constants.SignInRequestCode));
        params.put("vtype", Integer.valueOf(getvType()));
        params.put("phone_model_android", Build.MANUFACTURER + "_" + Build.DEVICE);
        params.put("client_callback", Integer.valueOf(1));
        if (this.ots != null && this.ots.size() > 0) {
            params.put("ots", this.ots);
        }
        if (this.recbody != null) {
            params.put("recbody", this.recbody);
        }
        if (this.gzhcnbody != null) {
            params.put("gzhcnbody", this.gzhcnbody);
        }
        if (this.genbody != null) {
            params.put("genbody", this.genbody);
        }
        if (this.amapbody != null) {
            params.put("amapbody", this.amapbody);
        }
        if (this.accbody != null) {
            params.put("accbody", this.accbody);
        }
        if (TextUtil.isValidate(this.latlngradius)) {
            params.put("latlngradius", this.latlngradius);
        }
        String jsonContent = JsonUtil.mapToJsonString(params);
        LogUtil.d("StoryModel", "========>>>>>>>>>>>>>>>>>>>>>> jsonContent:" + jsonContent);
        return jsonContent;
    }

    public void setUploadSuc(boolean uploadSuc) {
        this.isUploadSuc = uploadSuc;
    }

    public boolean isUploadSuc() {
        return this.isUploadSuc;
    }

    public int getvType() {
        return this.vType;
    }

    public void setvType(int mDataType) {
        if (1 == mDataType) {
            this.vType = 1;
        } else if (mDataType == 0) {
            this.vType = 0;
        } else if (2 == mDataType) {
            this.vType = 2;
        }
    }

    public boolean mapIsValidate(HashMap<String, String> map) {
        return (map == null || map.keySet().size() == 0) ? false : true;
    }
}