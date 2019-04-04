package cn.nineton.onetake.bean;

import android.app.Activity;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.collection.CollectionBean;
//import com.blink.academy.onetake.bean.collection.EditorCollectionBean;
//import com.blink.academy.onetake.bean.discover.DiscoverBean;
//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.glide.CacheKeyGlideUrl;
//import com.blink.academy.onetake.glide.LoadLocalCatchFileTask;
//import com.blink.academy.onetake.glide.VideoListGlideModule;
//import com.blink.academy.onetake.support.database.table.PlayCountTable;
//import com.blink.academy.onetake.support.database.task.PlayCountDbTask;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.ImageUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.adapter.entities.TimeLineCardEntity$.Lambda.1;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
//import com.blink.academy.onetake.visibility.calculator.LinkedListViewItemActiveCalculator;
//import com.blink.academy.onetake.visibility.items.ListItem;
//import com.blink.academy.onetake.widgets.TimeLineVolumeView;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZMediaManager;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZUtils;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer.OnMediaPlayCountListener;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer.OnPlayerStateChange;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer.PlayCountingCallBack;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerManager;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerStandard;
//import com.blink.academy.onetake.widgets.VideoView.TextureVideoView;
//import com.blink.academy.onetake.widgets.VideoView.TextureVideoView.MediaPlayerCallback;
//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.db.table.PlayCountTable;
import cn.nineton.onetake.db.task.PlayCountDbTask;
import cn.nineton.onetake.listener.LoadLocalFileCallback;
import cn.nineton.onetake.util.CacheKeyGlideUrl;
import cn.nineton.onetake.util.ImageUtil;
import cn.nineton.onetake.util.LoadLocalCatchFileTask;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.widget.TextureVideoView;
import cn.nineton.onetake.widget.TimeLineVolumeView;
import cn.nineton.onetake.widget.video.JZMediaManager;
import cn.nineton.onetake.widget.video.JZUtils;
import cn.nineton.onetake.widget.video.JZVideoPlayer;
import cn.nineton.onetake.widget.video.JZVideoPlayerManager;
import cn.nineton.onetake.widget.video.JZVideoPlayerStandard;

public class TimeLineCardEntity extends BaseEntity implements ListItem {
    public static final int PHOTO_EMPTY = 1;
    public static final int PHOTO_LOADING_TYPE = 5;
    public static final int PHOTO_MAP_TYPE = 6;
    public static final int PHOTO_NORMAL = 0;
    public static final int PHOTO_TITLE = 2;
    public static final int PHOTO_TITLE_HOT = 3;
    public static final int PHOTO_TITLE_NEWER = 4;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private static final int STATE_IDLE = 0;
    private static final String TAG = TimeLineCardEntity.class.getSimpleName();
    public static final int TYPE_GIF = 0;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    private Layout albumLayout;
    private String avatarUrl;
    private float banner_alpha;
    private String banner_article_id;
    private String banner_cover_url;
    private int banner_height;
    private String banner_id;
    private int banner_logo_height;
    private String banner_logo_url;
    private int banner_logo_width;
    private int banner_photo_id;
    private String banner_preview_ave_info;
    private String banner_subtitle;
    private String banner_title;
    private String banner_uniq_id;
    private int banner_video_height;
    private String banner_video_url;
    private int banner_video_width;
    private int banner_width;
    private List<CommentEntity> commentEntityList;
    private boolean createFloat;
    private DiscoverBean discoverBean;
    private int floatOffset;
    private int floatPos;
    private boolean followingLoadingVisible;
    private int gender;
    private boolean hideTopLineAndSpace;
    private boolean isClickFollow;
    private boolean isGiphy;
    private boolean isLess3_4;
    private boolean isLiked;
    private boolean isMultiLine;
    private boolean isRequestLike;
    private boolean isReset;
    private boolean isShowNearByItem;
    private boolean isShowScreen;
    private int lastPlayPosition;
    private LikeEntity likeEntity;
    private List<TimeLineCardEntity> lists;
    private String long_thumbnail_url;
    private WeakReference<Activity> mActivityWeakReference;
    private View mAveInfoColorView;
    private CollectionBean mCollectionBean;
    private List<CollectionBean> mCollectionBeanList;
    private int mContentType;
    private EditorCollectionBean mEditCollectionBean;
    private Runnable mFadeOutRunnable;
    private TimeLineVolumeView mLongVideoLeftBottomIV;
    public JZVideoPlayer.PlayCountingCallBack mLongVideoPlayCountingCallBack;
    int mLongVideoPlayNum;
    private JZVideoPlayerStandard mLongVideoPlayer;
    private ImageView mLongVideoRightBottomIV;
    int mMediaPlayNum;
    private TextureVideoView.PlayCountingCallBack mPlayCountingCallBack;
    private View mProgressBar;
    private int mScrollState;
    private int mState;
    private SuggestUserWithSocialEntity mSuggestUserWithSocialEntity;
    private LoadLocalCatchFileTask mTask;
    private String mVideoPath;
    private TextureVideoView mVideoView;
    public JZVideoPlayer.OnMediaPlayCountListener mediaPlayCountListener;
    private Layout moreLayout;
    private int onetakeCount;
    protected int photoId;
    private String photoTitle;
    private int photoType;
    protected String photoUrl;
    private boolean previewFinished;
    private List<String> sample_urls;
    private String screenName;
    private boolean showAllBt;
    private boolean showTopLine;
    private int signature;
    private TimelineBean timelineBean;
    private int unmodified;

    private Activity getActivity() {
        if (this.mActivityWeakReference != null) {
            return (Activity) this.mActivityWeakReference.get();
        }
        return null;
    }

    public boolean isShowScreen() {
        return this.showTopLine;
    }

    public boolean isHideTopLineAndSpace() {
        return this.hideTopLineAndSpace;
    }

    public void setHideTopLineAndSpace(boolean hideTopLineAndSpace) {
        this.hideTopLineAndSpace = hideTopLineAndSpace;
    }

    public int getSignature() {
        return this.signature;
    }

    public void setSignature(int signature) {
        this.signature = signature;
    }

    public boolean isPreviewFinished() {
        return this.previewFinished;
    }

    public void setPreviewFinished(boolean previewFinished) {
        this.previewFinished = previewFinished;
    }

    public String getBanner_id() {
        return this.banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public int getBanner_width() {
        return this.banner_width;
    }

    public void setBanner_preview_ave_info(String banner_preview_ave_info) {
        this.banner_preview_ave_info = banner_preview_ave_info;
    }

    public String getBanner_preview_ave_info() {
        return this.banner_preview_ave_info;
    }

    public void setBanner_width(int banner_width) {
        this.banner_width = banner_width;
    }

    public int getBanner_height() {
        return this.banner_height;
    }

    public void setBanner_height(int banner_height) {
        this.banner_height = banner_height;
    }

    public void setBanner_logo_url(String banner_logo_url) {
        this.banner_logo_url = banner_logo_url;
    }

    public String getBanner_logo_url() {
        return this.banner_logo_url;
    }

    public int getBanner_video_height() {
        return this.banner_video_height;
    }

    public void setBanner_video_height(int banner_video_height) {
        this.banner_video_height = banner_video_height;
    }

    public int getBanner_video_width() {
        return this.banner_video_width;
    }

    public void setBanner_video_width(int banner_video_width) {
        this.banner_video_width = banner_video_width;
    }

    public int getBanner_logo_height() {
        return this.banner_logo_height;
    }

    public void setBanner_logo_height(int banner_logo_height) {
        this.banner_logo_height = banner_logo_height;
    }

    public int getBanner_logo_width() {
        return this.banner_logo_width;
    }

    public void setBanner_logo_width(int banner_logo_width) {
        this.banner_logo_width = banner_logo_width;
    }

    public void setBanner_alpha(float banner_alpha) {
        this.banner_alpha = banner_alpha;
    }

    public float getBanner_alpha() {
        return this.banner_alpha;
    }

    public void setBanner_gif_url(String banner_gif_url) {
        this.banner_video_url = banner_gif_url;
    }

    public String getBanner_gif_url() {
        return this.banner_video_url;
    }

    public String getBanner_cover_url() {
        return this.banner_cover_url;
    }

    public void setBanner_cover_url(String banner_cover_url) {
        this.banner_cover_url = banner_cover_url;
    }

    public int getBanner_photo_id() {
        return this.banner_photo_id;
    }

    public void setBanner_photo_id(int banner_photo_id) {
        this.banner_photo_id = banner_photo_id;
    }

    public String getBanner_title() {
        return this.banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_subtitle() {
        return this.banner_subtitle;
    }

    public void setBanner_subtitle(String banner_subtitle) {
        this.banner_subtitle = banner_subtitle;
    }

    public String getBanner_article_id() {
        return this.banner_article_id;
    }

    public void setBanner_article_id(String banner_article_id) {
        this.banner_article_id = banner_article_id;
    }

    public String getBanner_uniq_id() {
        return this.banner_uniq_id;
    }

    public void setBanner_uniq_id(String banner_uniq_id) {
        this.banner_uniq_id = banner_uniq_id;
    }

    public boolean isLongVideoType() {
        return TextUtil.isValidate(this.timelineBean) && this.timelineBean.vtype == 2;
    }

    public CollectionBean getCollectionBean() {
        return this.mCollectionBean;
    }

    public void setCollectionBean(CollectionBean collectionBean) {
        this.mCollectionBean = collectionBean;
    }

    public List<CollectionBean> getCollectionBeanList() {
        return this.mCollectionBeanList;
    }

    public void setCollectionBeanList(List<CollectionBean> collectionBeanList) {
        this.mCollectionBeanList = collectionBeanList;
    }

    public EditorCollectionBean getEditCollectionBean() {
        return this.mEditCollectionBean;
    }

    public void setEditCollectionBean(EditorCollectionBean editCollectionBean) {
        this.mEditCollectionBean = editCollectionBean;
    }

    public SuggestUserWithSocialEntity getSuggestUserWithSocialEntity() {
        return this.mSuggestUserWithSocialEntity;
    }

    public void setSuggestUserWithSocialEntity(SuggestUserWithSocialEntity suggestUserWithSocialEntity) {
        this.mSuggestUserWithSocialEntity = suggestUserWithSocialEntity;
        if (this.mSuggestUserWithSocialEntity != null) {
            setScreenName(this.mSuggestUserWithSocialEntity.getScreenName());
        }
    }

    public int getContentType() {
        return this.mContentType;
    }

    public TimeLineCardEntity setContentType(int contentType) {
        this.mContentType = contentType;
        return this;
    }

    public TimeLineCardEntity(int viewType) {
        super(viewType);
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = new TextureVideoView.PlayCountingCallBack() {
            public void UpdateCount(int newCount) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (TimeLineCardEntity.this.isShowScreen) {
                            TimeLineCardEntity timeLineCardEntity = TimeLineCardEntity.this;
                            timeLineCardEntity.mMediaPlayNum++;
                        }
                        LogUtil.d(TimeLineCardEntity.TAG + 3, "photoID:" + TimeLineCardEntity.this.getPhotoId() + ", count:" + TimeLineCardEntity.this.mMediaPlayNum);
                    }
                });
            }
        };
        this.mLongVideoPlayCountingCallBack = new JZVideoPlayer.PlayCountingCallBack() {
            public void UpdateCount() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        TimeLineCardEntity timeLineCardEntity = TimeLineCardEntity.this;
                        timeLineCardEntity.mLongVideoPlayNum++;
                    }
                });
            }
        };
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = new JZVideoPlayer.OnMediaPlayCountListener() {
            public void onStart() {
                PlayCountTable playCountTable = PlayCountDbTask.searchTable((long) TimeLineCardEntity.this.getPhotoId());
                TimeLineCardEntity.this.mLongVideoPlayNum = 0;
                if (playCountTable != null) {
                    TimeLineCardEntity.this.mLongVideoPlayNum = (int) playCountTable.playCount;
                }
            }

            public void onEnd(int duration) {
            }
        };
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
    }

    public void setShowNearByItem(boolean isShowNearByItem) {
        this.isShowNearByItem = isShowNearByItem;
    }

    public TimeLineCardEntity(int viewType, boolean isShowNearByItem) {
        super(viewType);
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null;/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = null/* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.isShowNearByItem = isShowNearByItem;
    }

    public TimeLineCardEntity(int viewType, DiscoverBean discoverBean) {
        super(viewType);
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = null/* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.discoverBean = discoverBean;
    }

    public TimeLineCardEntity(int viewType, TimelineBean timelineBean) {
        super(viewType);
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null;/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null;/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = null;/* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.timelineBean = timelineBean;
    }

    public TimeLineCardEntity(int viewType, DiscoverBean discoverBean, TimelineBean timelineBean, int photoId, String avatarUrl, String screenName, String photoUrl) {
        this(viewType, discoverBean, timelineBean, photoId, avatarUrl, screenName, photoUrl, false);
    }

    public TimeLineCardEntity(int viewType, DiscoverBean discoverBean, TimelineBean timelineBean, int photoId, String avatarUrl, String screenName, String photoUrl, boolean isMultiLine) {
        super(viewType);
        boolean z;
        String str = null;
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null;/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null;/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = null;/* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.discoverBean = discoverBean;
        this.timelineBean = timelineBean;
        this.photoId = photoId;
        if (!TextUtil.isNull(avatarUrl)) {
            str = avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        }
        this.avatarUrl = str;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(photoUrl)) {
            photoUrl = ImageUtil.DefaultPhoto;
        }
        this.photoUrl = photoUrl;
        if (timelineBean == null || !timelineBean.is_liked) {
            z = false;
        } else {
            z = true;
        }
        this.isLiked = z;
        this.isReset = false;
        this.isMultiLine = isMultiLine;
    }

    public TimeLineCardEntity(TimelineBean timelineBean, int photoId, String avatarUrl, String screenName, String photoUrl, LikeEntity likeEntity, int unmodified, boolean isLiked, List<CommentEntity> commentEntityList, int onetakeCount, Layout albumLayout, Layout moreLayout, int gender, boolean isGiphy) {
        super(208);
        String str;
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null;/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null;/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener =null; /* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.timelineBean = timelineBean;
        this.photoId = photoId;
        if (TextUtil.isNull(avatarUrl)) {
            str = null;
        } else {
            str = avatarUrl + ImageUtil.getAvatarThumbnailsSize();
        }
        this.avatarUrl = str;
        if (TextUtil.isNull(screenName)) {
            screenName = "";
        }
        this.screenName = screenName;
        if (TextUtil.isNull(photoUrl)) {
            photoUrl = ImageUtil.DefaultPhoto;
        }
        this.photoUrl = photoUrl;
        this.likeEntity = likeEntity;
        this.unmodified = unmodified;
        this.isLiked = isLiked;
        this.commentEntityList = commentEntityList;
        this.onetakeCount = onetakeCount;
        this.albumLayout = albumLayout;
        this.moreLayout = moreLayout;
        this.isReset = false;
        if (TextUtil.isNull(timelineBean.long_thumbnail_url)) {
            str = timelineBean.long_thumbnail_url;
        } else {
            str = String.format("%1$s%2$s", timelineBean.long_thumbnail_url, ImageUtil.getLongBitmapSize());
        }
        this.long_thumbnail_url = str;
        this.gender = gender;
        this.isGiphy = isGiphy;
    }

    public TimeLineCardEntity(int photoType, String photoTitle, boolean showAllBt) {
        super(208);
        this.mState = 0;
        this.followingLoadingVisible = false;
        this.isGiphy = false;
        this.isShowNearByItem = false;
        this.showAllBt = false;
        this.isShowScreen = true;
        this.showTopLine = false;
        this.hideTopLineAndSpace = false;
        this.mPlayCountingCallBack = null;/* anonymous class already generated */;
        this.mLongVideoPlayCountingCallBack = null;/* anonymous class already generated */;
        this.isRequestLike = false;
        this.mContentType = 0;
        this.lastPlayPosition = 0;
        this.mTask = null;
        this.mediaPlayCountListener = null;/* anonymous class already generated */;
        this.createFloat = false;
        this.floatPos = -1;
        this.floatOffset = 0;
        this.lists = new ArrayList();
        this.photoType = photoType;
        this.photoTitle = photoTitle;
        this.showAllBt = showAllBt;
    }

    public TimeLineCardEntity setShowTopLine(boolean showTopLine) {
        this.showTopLine = showTopLine;
        return this;
    }

    public void setShowScreen(boolean showScreen) {
        this.isShowScreen = showScreen;
    }

    public DiscoverBean getDiscoverBean() {
        return this.discoverBean;
    }

    public void setTimelineBean(TimelineBean timelineBean) {
        this.timelineBean = timelineBean;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setLikeEntity(LikeEntity likeEntity) {
        this.likeEntity = likeEntity;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return this.isLiked;
    }

    public void setCommentEntityList(List<CommentEntity> commentEntityList) {
        this.commentEntityList = commentEntityList;
    }

    public TimelineBean getTimelineBean() {
        return this.timelineBean;
    }

    public int getPhotoId() {
        return this.photoId;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public LikeEntity getLikeEntity() {
        return this.likeEntity;
    }

    public String getLong_thumbnail_url() {
        return this.long_thumbnail_url;
    }

    public List<CommentEntity> getCommentEntityList() {
        return this.commentEntityList;
    }

    public Layout getMoreLayout() {
        return this.moreLayout;
    }

    public void setMoreLayout(Layout moreLayout) {
        this.moreLayout = moreLayout;
    }

    public boolean isReset() {
        return this.isReset;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isFollowingLoadingVisible() {
        return this.followingLoadingVisible;
    }

    public void setFollowingLoadingVisible(boolean followingLoadingVisible) {
        this.followingLoadingVisible = followingLoadingVisible;
    }

    public boolean isShowNearByItem() {
        return this.isShowNearByItem;
    }

    public boolean isRequestLike() {
        return this.isRequestLike;
    }

    public void setRequestLike(boolean requestLike) {
        this.isRequestLike = requestLike;
    }

    public void bindView(TextureVideoView videoView, View aveInfoColorView) {
        this.mVideoView = videoView;
        if (this.mVideoView != null) {
            this.mVideoView.setVideoID(getPhotoId());
        }
        this.mAveInfoColorView = aveInfoColorView;
        this.mVideoPath = null;
        this.mState = 0;
    }

    public int getPhotoType() {
        return this.photoType;
    }

    public void setPhotoType(int photoType) {
        this.photoType = photoType;
    }

    public String getPhotoTitle() {
        return this.photoTitle;
    }

    public boolean isShowAllBt() {
        return this.showAllBt;
    }

    public boolean isClickFollow() {
        return this.isClickFollow;
    }

    public void setClickFollow(boolean clickFollow) {
        this.isClickFollow = clickFollow;
    }

    public boolean isMultiLine() {
        return this.isMultiLine;
    }

    public String getVideoPath() {
        return this.mVideoPath;
    }

    public boolean isGiphy() {
        return this.isGiphy;
    }

    public void setGiphy(boolean giphy) {
        this.isGiphy = giphy;
    }

    public void bindLongVideoView(TimeLineVolumeView leftBottom, ImageView rightBottom, View progressBar, Runnable runnable, JZVideoPlayerStandard longVideoPlayer, boolean isLess3_4) {
        this.mLongVideoLeftBottomIV = leftBottom;
        this.mLongVideoRightBottomIV = rightBottom;
        this.mProgressBar = progressBar;
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.GONE);
        }
        this.mFadeOutRunnable = runnable;
        this.mLongVideoPlayer = longVideoPlayer;
        if (this.mAveInfoColorView != null) {
            this.mAveInfoColorView.setVisibility(View.GONE);
        }
        this.isLess3_4 = isLess3_4;
    }

    public void setVideoPath(String videoPath) {
        LogUtil.d("jiaban", "setVideoPath=========scrollState:" + this.mScrollState + ",     videoPath:" + videoPath);
        this.mVideoView.setPlayCountingCallBack(this.mPlayCountingCallBack);
        this.mVideoPath = videoPath;
        if (this.mScrollState != 2 && this.mVideoPath != null && this.mVideoView != null && this.mVideoView.canSeeInWindow()) {
            LogUtil.d("jiaban", "setVideoPath:start=========scrollState:" + this.mScrollState + ",     videoPath:" + videoPath);
            this.mVideoView.updateTextureViewSize();
            this.mVideoView.requestLayout();
            this.mVideoView.invalidate();
            this.mVideoView.setVideoPath(this.mVideoPath);
            if (this.mLongVideoLeftBottomIV != null) {
                this.mLongVideoLeftBottomIV.removeCallbacks(this.mFadeOutRunnable);
                this.mLongVideoLeftBottomIV.resetView();
                this.mLongVideoLeftBottomIV.postDelayed(this.mFadeOutRunnable, 3000);
            }
            if (this.mState != 1) {
                this.mState = 1;
                this.mVideoView.setOnMediaPlayCountListener(new TextureVideoView.OnMediaPlayCountListener() {
                    public void onStart() {
                        PlayCountTable playCountTable = PlayCountDbTask.searchTable((long) TimeLineCardEntity.this.getPhotoId());
                        TimeLineCardEntity.this.mMediaPlayNum = 0;
                        if (playCountTable != null) {
                            TimeLineCardEntity.this.mMediaPlayNum = (int) playCountTable.playCount;
                        }
                        LogUtil.d("play", "start:===========================photoID:" + TimeLineCardEntity.this.getPhotoId() + ", count:" + TimeLineCardEntity.this.mMediaPlayNum);
                    }

                    public void onEnd(int duration) {
                    }
                });
                if (TextUtil.isValidate(this.timelineBean) && this.timelineBean.vtype == 2) {
                    this.mVideoView.setMediaPlayerCallback(new TextureVideoView.MediaPlayerCallback() {
                        public void onMediaplayer(MediaPlayer mp) {
                        }

                        public void onPrepared(MediaPlayer mp) {
                            if (TextUtil.isValidate(TimeLineCardEntity.this.mLongVideoLeftBottomIV) && JZUtils.isWifiConnected(App.getContext())) {
                                TimeLineCardEntity.this.mLongVideoLeftBottomIV.setVisibility(View.VISIBLE);
                            }
                            if (TextUtil.isValidate(TimeLineCardEntity.this.mLongVideoRightBottomIV)) {
                                if (TimeLineCardEntity.this.isLess3_4) {
                                    TimeLineCardEntity.this.mLongVideoRightBottomIV.setVisibility(View.VISIBLE);
                                } else {
                                    TimeLineCardEntity.this.mLongVideoRightBottomIV.setVisibility(View.GONE);
                                }
                            }
                            Activity activity = TimeLineCardEntity.this.getActivity();
                            if (activity != null && !activity.isDestroyed()) {
//                                Glide.with(activity).using(VideoListGlideModule.getOkHttpUrlLoader(), InputStream.class).load(new CacheKeyGlideUrl(TimeLineCardEntity.this.photoUrl)).as(File.class).downloadOnly(Integer.MIN_VALUE, Integer.MIN_VALUE);
                                //Glide.with(activity).load(new CacheKeyGlideUrl(photoUrl)).as(File.class).downloadOnly(Integer.MIN_VALUE, Integer.MIN_VALUE);
                                Log.e("debug","TimeLineCardEntity onPrepared  Glide in file : "+photoUrl);
                            }
                        }

                        public void onCompletion(MediaPlayer mp) {
                        }

                        public void onBufferingUpdate(MediaPlayer mMediaPlayer, int percent) {
                            int currentPosition = mMediaPlayer.getCurrentPosition();
                            if (currentPosition == TimeLineCardEntity.this.lastPlayPosition) {
                                if (!(TimeLineCardEntity.this.mProgressBar == null || TimeLineCardEntity.this.mProgressBar.getVisibility() == View.VISIBLE)) {
                                    TimeLineCardEntity.this.mProgressBar.setVisibility(View.VISIBLE);
                                }
                            } else if (TimeLineCardEntity.this.mProgressBar != null && TimeLineCardEntity.this.mProgressBar.getVisibility() == View.VISIBLE) {
                                TimeLineCardEntity.this.mProgressBar.setVisibility(View.GONE);
                            }
                            if (TimeLineCardEntity.this.mVideoView.isPlaying()) {
                                TimeLineCardEntity.this.lastPlayPosition = currentPosition;
                            }
                        }

                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        }

                        public void onSeekComplete(MediaPlayer mp) {
                        }

                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (3 == what) {
                                TimeLineCardEntity.this.mAveInfoColorView.setVisibility(View.GONE);
                            }
                            return true;
                        }

                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            return false;
                        }
                    });
                }
                this.mVideoView.start();
                LogUtil.e("debug", "start==============================setVideoPath:" + videoPath);
                return;
            }
            this.mVideoView.checkIsPlaying();
        }
    }

    public int getVisibilityPercents(View currentView) {
        return 0;
    }

    public void setTask() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
        this.mTask = null;
    }

    public void setActive(View currentView, final int newActiveViewPosition, int scrollState, Rect showRect) {
        this.mScrollState = scrollState;
        processShowScreen(scrollState, showRect);
        if (TextUtil.isValidate(this.timelineBean) && this.timelineBean.vtype == 2) {
            if (this.mLongVideoLeftBottomIV != null && JZUtils.isWifiConnected(App.getContext())) {
                this.mLongVideoLeftBottomIV.setVisibility(View.VISIBLE);
            }
            if (this.mLongVideoPlayer == null || this.mLongVideoPlayer.currentState != 5) {
                Activity activity = getActivity();
                if ((this.mTask == null || this.mTask.isCancelled()) && activity != null) {
                    //this.mTask = new LoadLocalCatchFileTask(activity, 1.lambdaFactory$(this, newActiveViewPosition));
                    mTask = new LoadLocalCatchFileTask(activity, new LoadLocalFileCallback() {
                        @Override
                        public void localHasCatch(File file) {
                            lambda$setActive$0(newActiveViewPosition,file);
                        }
                    });
                    this.mTask.execute(new String[]{this.photoUrl});
                } else if (this.mLongVideoPlayer != null && this.mLongVideoPlayer.currentState == 3) {
                    try {
                        if (!JZMediaManager.instance().mediaPlayer.isPlaying()) {
                            JZMediaManager.instance().mediaPlayer.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (JZUtils.isWifiConnected(App.getContext()) || this.viewType == BaseAdapter.Item.DISCOVER_SPECIAL_TOPIC_TYPE) {
                this.mLongVideoPlayer.startButton.performClick();
                if (this.mLongVideoLeftBottomIV != null) {
                    this.mLongVideoLeftBottomIV.removeCallbacks(this.mFadeOutRunnable);
                    this.mLongVideoLeftBottomIV.resetView();
                    this.mLongVideoLeftBottomIV.postDelayed(this.mFadeOutRunnable, 3000);
                }
            } else {
                this.mLongVideoPlayer.checkPauseState();
            }
        } else if (this.mVideoPath != null) {
            setVideoPath(this.mVideoPath);
        }
    }

    private /* synthetic */ void lambda$setActive$0(int newActiveViewPosition, File resultFile) {
        if (resultFile != null && resultFile.exists()) {
            setLongVideoPath(resultFile.getAbsolutePath(), newActiveViewPosition);
        } else if (TextUtil.isValidate(this.photoUrl)) {
            setLongVideoPath(this.photoUrl, newActiveViewPosition);
        }
    }

    private void setLongVideoPath(String videoPath, int pos) {
        this.mVideoPath = videoPath;
        if (this.mScrollState != 2 && this.mLongVideoPlayer != null) {
            this.mLongVideoPlayer.setPlayCountingCallBack(this.mLongVideoPlayCountingCallBack);
            JZMediaManager.listPos = pos;
            this.mLongVideoPlayer.setvDuration(this.timelineBean.vduration);
            this.mLongVideoPlayer.setUp(videoPath, 0, new Object[0]);
            if (this.mLongVideoLeftBottomIV != null && JZUtils.isWifiConnected(App.getContext())) {
                this.mLongVideoLeftBottomIV.removeCallbacks(this.mFadeOutRunnable);
                this.mLongVideoLeftBottomIV.resetView();
                this.mLongVideoLeftBottomIV.postDelayed(this.mFadeOutRunnable, 3000);
            }
            if (this.mState != 1) {
                this.mState = 1;
                if (this.mLongVideoPlayer == null) {
                    return;
                }
                if (this.mLongVideoPlayer.currentState == 5) {
                    if (JZUtils.isWifiConnected(App.getContext()) || this.viewType == BaseAdapter.Item.DISCOVER_SPECIAL_TOPIC_TYPE) {
                        JZVideoPlayer.goOnPlayOnResume();
                    }
                } else if (this.mLongVideoPlayer.currentState != 3) {
                    this.mLongVideoPlayer.setOnMediaPlayCountListener(this.mediaPlayCountListener);
                    this.mLongVideoPlayer.setOnPlayerStateChange(new JZVideoPlayer.OnPlayerStateChange() {
                        public void onPlaying() {
                            if (TimeLineCardEntity.this.createFloat) {
                                TimeLineCardEntity.this.createFloat = false;
                                TimeLineCardEntity.this.mLongVideoPlayer.setResetActive(true);
                                List<String> list = new ArrayList();
                                if (TextUtil.isValidate(TimeLineCardEntity.this.sample_urls)) {
                                    for (String s : TimeLineCardEntity.this.sample_urls) {
                                        list.add(s + "/preview");
                                    }
                                }
                                TimeLineCardEntity.this.mLongVideoPlayer.startWindowFullscreen(list, TimeLineCardEntity.this.getTimelineBean().story_id, TimeLineCardEntity.this.getTimelineBean().id, TimeLineCardEntity.this.mLongVideoPlayCountingCallBack);
                            }
                        }

                        public void onPauseOrStop() {
                        }

                        public void onCloseFullscreen() {
                            TimeLineCardEntity.this.deactivate(null, TimeLineCardEntity.this.floatPos, 0);
                            if (TimeLineCardEntity.this.floatOffset + TimeLineCardEntity.this.floatPos >= 0 && TimeLineCardEntity.this.lists.size() > TimeLineCardEntity.this.floatPos + TimeLineCardEntity.this.floatOffset) {
                                ((TimeLineCardEntity) TimeLineCardEntity.this.lists.get(TimeLineCardEntity.this.floatPos + TimeLineCardEntity.this.floatOffset)).setActive(null, TimeLineCardEntity.this.floatPos + TimeLineCardEntity.this.floatOffset, 0, new Rect());
                            }
                            TimeLineCardEntity.this.floatPos = -1;
                            TimeLineCardEntity.this.floatOffset = 0;
                            if (TimeLineCardEntity.this.sample_urls != null) {
                                TimeLineCardEntity.this.sample_urls.clear();
                            }
                        }
                    });
                    this.mLongVideoPlayer.setMediaPlayerCallback(new TextureVideoView.MediaPlayerCallback() {
                        public void onMediaplayer(MediaPlayer mp) {
                        }

                        public void onPrepared(MediaPlayer mp) {
                            if (TextUtil.isValidate(TimeLineCardEntity.this.mLongVideoLeftBottomIV) && JZUtils.isWifiConnected(App.getContext())) {
                                TimeLineCardEntity.this.mLongVideoLeftBottomIV.setVisibility(View.VISIBLE);
                            }
                            if (!TextUtil.isValidate(TimeLineCardEntity.this.mLongVideoRightBottomIV)) {
                                return;
                            }
                            if (TimeLineCardEntity.this.isLess3_4) {
                                TimeLineCardEntity.this.mLongVideoRightBottomIV.setAlpha(1.0f);
                                TimeLineCardEntity.this.mLongVideoRightBottomIV.setVisibility(View.VISIBLE);
                                return;
                            }
                            TimeLineCardEntity.this.mLongVideoRightBottomIV.setVisibility(View.GONE);
                        }

                        public void onCompletion(MediaPlayer mp) {
                        }

                        public void onBufferingUpdate(MediaPlayer mMediaPlayer, int percent) {
                            if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentState == 3) {
                                int currentPosition = mMediaPlayer.getCurrentPosition();
                                if (TimeLineCardEntity.this.mVideoView.isPlaying()) {
                                    TimeLineCardEntity.this.lastPlayPosition = currentPosition;
                                }
                            }
                        }

                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        }

                        public void onSeekComplete(MediaPlayer mp) {
                        }

                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            return true;
                        }

                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            return false;
                        }
                    });
                    if (JZUtils.isWifiConnected(App.getContext()) || this.viewType == BaseAdapter.Item.DISCOVER_SPECIAL_TOPIC_TYPE) {
                        this.mLongVideoPlayer.startButton.performClick();
                    } else {
                        this.mLongVideoPlayer.checkPauseState();
                    }
                }
            }
        }
    }

    public void processShowScreen(int scrollState, Rect showRect) {
        boolean z = true;
        if (showRect != null) {
            this.mScrollState = scrollState;
            if (!(this.mScrollState == 2 || this.mVideoView == null)) {
                int[] locations = new int[2];
                this.mVideoView.getLocationOnScreen(locations);
                if (!(showRect.contains(showRect.left, locations[1] - 1) || showRect.contains(showRect.left, locations[1] + this.mVideoView.getHeight()))) {
                    z = false;
                }
                this.isShowScreen = z;
            }
            LogUtil.d("showRect", "photoID:" + this.photoId + ", scrollState:" + scrollState + ", isShowScreen:" + this.isShowScreen + ", showRect:" + showRect.toString());
        }
    }

    public void deactivate(View currentView, int position, int scrollState) {
        this.mScrollState = scrollState;
        this.lastPlayPosition = 0;
        LogUtil.d(TAG + "2", "deactivate=========scrollState:" + scrollState + " , path : " + this.mVideoPath + " position : " + position);
        this.mState = 2;
        if (this.mVideoView != null) {
            this.mVideoView.stop();
        }
        if (this.mLongVideoLeftBottomIV != null) {
            this.mLongVideoLeftBottomIV.setVisibility(View.GONE);
        }
        if (!(this.mTask == null || this.mTask.isCancelled())) {
            this.mTask.cancel(false);
            this.mTask = null;
        }
        refreshData();
        if (JZUtils.isWifiConnected(App.getContext()) || this.viewType == BaseAdapter.Item.DISCOVER_SPECIAL_TOPIC_TYPE) {
            if (scrollState == 0 && this.mLongVideoPlayer != null && this.mLongVideoPlayer.currentState == 3) {
                JZVideoPlayer.goOnPlayOnPause();
            }
        } else if ((scrollState == 0 || scrollState == 1) && this.mLongVideoPlayer != null && this.mLongVideoPlayer.currentState == 3) {
            JZVideoPlayer.goOnPlayOnPause();
        }
        if (this.mAveInfoColorView == null) {
            return;
        }
        if (this.timelineBean == null || this.timelineBean.vtype != 2) {
            this.mAveInfoColorView.setVisibility(View.VISIBLE);
            this.mAveInfoColorView.setAlpha(1.0f);
        }
    }

    public void refreshData() {
        if (this.timelineBean == null) {
            int count = this.mMediaPlayNum;
            PlayCountDbTask.addOrUpdateTable(new PlayCountTable((long) getPhotoId(), (long) count));
            this.mMediaPlayNum = 0;
            LogUtil.d("play", "photoID:" + getPhotoId() + ", count:" + count);
        } else if (2 == this.timelineBean.vtype) {
            PlayCountDbTask.addOrUpdateTable(new PlayCountTable((long) getPhotoId(), (long) this.mLongVideoPlayNum));
            this.mLongVideoPlayNum = 0;
        }
    }

    public void shareStart(View currentView, int position) {
        this.mState = 1;
        if (this.mVideoView != null) {
            this.mVideoView.shareStart();
        }
    }

    public void sharePause(View currentView, int position) {
        this.mState = 2;
        if (this.mVideoView != null) {
            this.mVideoView.sharePause();
        }
    }

    public void setActivity(Activity activity) {
        this.mActivityWeakReference = new WeakReference(activity);
    }

    public void setCreateFloat(boolean createFloat, int floatPos, int offset, List<String> list) {
        this.createFloat = createFloat;
        this.floatPos = floatPos;
        this.floatOffset = offset;
        this.sample_urls = list;
    }

    public void setLists(List<TimeLineCardEntity> lists) {
        this.lists = lists;
    }
}