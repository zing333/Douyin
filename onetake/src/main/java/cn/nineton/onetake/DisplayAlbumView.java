//package cn.nineton.onetake;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.CursorIndexOutOfBoundsException;
//import android.database.StaleDataException;
//import android.graphics.Bitmap;
//import android.graphics.Rect;
//import android.media.ExifInterface;
//import android.media.MediaFormat;
//import android.media.MediaMetadataRetriever;
//import android.os.Build;
//import android.os.Build.VERSION;
//import android.os.Bundle;
//import android.os.Handler.Callback;
//import android.os.Message;
//import android.provider.MediaStore.Images;
//import android.provider.MediaStore.Video.Media;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.Adapter;
//import android.support.v7.widget.RecyclerView.ItemDecoration;
//import android.support.v7.widget.RecyclerView.State;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.animation.LinearInterpolator;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
////import android.widget.RelativeLayout.LayoutParams;
////import com.blink.academy.onetake.App;
////import com.blink.academy.onetake.Config;
////import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
////import com.blink.academy.onetake.custom.WeakHandler;
////import com.blink.academy.onetake.model.DraftModel;
////import com.blink.academy.onetake.model.FilterModel;
////import com.blink.academy.onetake.model.MscvModel;
////import com.blink.academy.onetake.model.video.BitmapInfo;
////import com.blink.academy.onetake.model.video.VideoBitmapsModel;
////import com.blink.academy.onetake.support.callbacks.ValueAnimatorCallback;
////import com.blink.academy.onetake.support.debug.LogUtil;
////import com.blink.academy.onetake.support.events.AlbumVideoEvent;
////import com.blink.academy.onetake.support.events.ClickSimplePhotoEvent;
////import com.blink.academy.onetake.support.events.DestroyActivityEvent;
////import com.blink.academy.onetake.support.events.DestroyActivityEvent.ActivityState;
////import com.blink.academy.onetake.support.events.DismissEntranceEvent;
////import com.blink.academy.onetake.support.events.DraftChangeEvent;
////import com.blink.academy.onetake.support.events.DraftsClearedEvent;
////import com.blink.academy.onetake.support.events.OfficialTagListEvent;
////import com.blink.academy.onetake.support.events.PublishDraftEvent;
////import com.blink.academy.onetake.support.events.SelectedPhotoEvent;
////import com.blink.academy.onetake.support.events.SwitchCameraModeEvent;
////import com.blink.academy.onetake.support.global.Constants;
////import com.blink.academy.onetake.support.interfaces.AnimatorEndListener;
////import com.blink.academy.onetake.support.manager.DraftBoxManager;
////import com.blink.academy.onetake.support.manager.GlobalLocationManager;
////import com.blink.academy.onetake.support.thread.PriorityRunnable;
////import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
////import com.blink.academy.onetake.support.utils.AnimationUtil;
////import com.blink.academy.onetake.support.utils.BitmapUtil;
////import com.blink.academy.onetake.support.utils.BuglyLogUtil;
////import com.blink.academy.onetake.support.utils.DensityUtil;
////import com.blink.academy.onetake.support.utils.SharedPrefUtils;
////import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
////import com.blink.academy.onetake.support.utils.TextUtil;
////import com.blink.academy.onetake.support.utils.TintColorUtil;
////import com.blink.academy.onetake.ui.activity.video.FilterActivity;
////import com.blink.academy.onetake.ui.activity.video.VideoActivity2;
////import com.blink.academy.onetake.ui.adapter.GifAlbumAdapter;
////import com.blink.academy.onetake.ui.adapter.PhotoAlbumAdapter;
////import com.blink.academy.onetake.ui.adapter.VideoAlbumAdapter;
////import com.blink.academy.onetake.ui.adapter.VideoAlbumAdapter.OnSelectStateChange;
////import com.blink.academy.onetake.ui.adapter.VideoFolderAdapter;
////import com.blink.academy.onetake.ui.adapter.VideoFolderAdapter.OnFolderClickListener;
////import com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity;
////import com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity;
////import com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity;
////import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
////import com.blink.academy.onetake.ui.fragment.draft.DraftsFragment;
////import com.blink.academy.onetake.ui.fragment.draft.DraftsFragment.DraftsNeedDataCallback;
////import com.blink.academy.onetake.widgets.DisplayAlbumView$27$.Lambda.1;
////import com.blink.academy.onetake.widgets.TextView.AvenirNextRegularTextView;
////import com.blink.academy.onetake.widgets.XLPullToRefresh.PtrAlbumViewLayout;
//import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
//import com.facebook.common.media.MediaUtils;
////import com.facebook.share.internal.ShareConstants;
//import com.facebook.imageutils.BitmapUtil;
//import com.nineoldandroids.animation.ValueAnimator;
//import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
////import de.greenrobot.event.EventBus;
////import in.srain.cube.views.ptr.AlbumHeadView;
////import in.srain.cube.views.ptr.PtrAlbumHander;
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import cn.nineton.onetake.adapter.BaseAdapter;
//import cn.nineton.onetake.adapter.VideoAlbumAdapter;
//import cn.nineton.onetake.bean.BitmapInfo;
//import cn.nineton.onetake.bean.LongVideosModel;
//import cn.nineton.onetake.bean.VideoAlbumEntity;
//import cn.nineton.onetake.bean.VideoBitmapsModel;
//import cn.nineton.onetake.event.DestroyActivityEvent;
//import cn.nineton.onetake.util.PriorityRunnable;
//import cn.nineton.onetake.util.PriorityThreadPoolManager;
//import cn.nineton.onetake.util.TextUtil;
//import cn.nineton.onetake.util.WeakHandler;
//
//public class DisplayAlbumView extends RelativeLayout implements OnFolderClickListener, OnClickListener, DraftsNeedDataCallback {
//    private static final int CURMODULEINDEX_DRAFTS = 4;
//    public static final int FROM_AVATOR = 1;
//    public static final int FROM_AVATOR2 = 2;
//    public static final int FROM_DEFAULT = 0;
//    public static final int FROM_FILETER_ACTIVITY = 3;
//    public static final int FROM_FilETER_ADD = 4;
//    public static final String INTENT_FROM = "intent_from";
//    public static final int LOADER_ALL = 0;
//    public static final int LOADER_ALL_PIC = 2;
//    public static final int LOADER_CATEGORY = 1;
//    private static final String LOG_TAG = "PtrAlbumViewLayout";
//    private static final int MSG_EVENT_PIC_TO_FILTER_ACTIVITY = 3;
//    private static final int MSG_EVENT_TO_FILTER_ACTIVITY = 2;
//    private static final int MSG_INTENT_TO_FILTER_ACTIVITY = 1;
//    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE = 102;
//    private static final float TOP_ALPHA_VIEW_RATIO = 0.2f;
//    public static boolean canAdapterClick = true;
//    private int afterSize;
//    AvenirNextRegularTextView category_anrtv;
//    RelativeLayout category_layout_rl;
//    View choose_ll;
//    private int comeFrom;
//    private int curModuleIndex;
//    private int curNoDraftIndex;
//    private View currentItemImageView;
//    private int currentState;
//    AvenirNextRegularTextView draft_swap_icon;
//    ImageView draft_swap_icon_bottom;
//    RelativeLayout draft_swap_icon_parent;
//    private DraftsFragment draftsFragment;
//    AvenirNextRegularTextView drafts_anrtv;
//    FrameLayout drafts_container;
//    RelativeLayout drag_vertical_layout;
//    FrameLayout fl_multiple_select;
//    RecyclerView folder_grid_recyclerview;
//    private boolean fromFilterActivity;
//    AvenirNextRegularTextView gif_swap_icon;
//    ImageView gif_swap_icon_bottom;
//    RelativeLayout gif_swap_icon_parent;
//    private View headerView;
//    ImageView icon_iv;
//    View indicator_draft;
//    private int intentVideoHeight;
//    private int intentVideoWidth;
//    private boolean isFirstShowFolderAnim;
//    private boolean isFromVideoActivity2;
//    private boolean isLoadingPic;
//    private boolean isNoPicAndVideo;
//    private boolean isPrepareToNext;
//    private boolean isReadData;
//    private ItemDragAndSwipeCallback itemDragAndSwipeCallback;
//    View loading_cpb;
//    private boolean lockData;
//    private FragmentActivity mActivity;
//    private List<AlbumPictureEntity> mAlbumPictureEntityList;
//    private AlphaTouchListener mAlphaTouch;
//    private boolean mCanLoadData;
//    private boolean mChangeAlphaView;
//    private long mCreationDate;
//    private String mCurrentTimeStamp;
//    private GifAlbumAdapter mGifAlbumAdapter;
//    private List<VideoAlbumEntity> mGifAlbumEntityList;
//    private GridLayoutManager mGridLayoutManager;
//    private WeakHandler mHandler;
//    private int mItemSpace;
//    private LinearLayoutManager mLinearLayoutManager;
//    private LoaderCallbacks<Cursor> mLoaderCallback;
//    private ArrayList<VideoFolderEntity> mOnlyGifFolderList;
//    private ArrayList<VideoFolderEntity> mOnlyPictureFolderList;
//    private ArrayList<VideoFolderEntity> mOnlyVideoFolderList;
//    private OtherTouchListener mOtherTouch;
//    private PhotoAlbumAdapter mPhotoAlbumAdapter;
//    private LoaderCallbacks<Cursor> mPictureLoaderCallback;
//    private int mScreenHeight;
//    private int mScreenWidth;
//    private Map<String, List<VideoAlbumEntity>> mStringListMap;
//    private LayoutParams mTopAlphaViewParams;
//    private int mTouchAlphaHeight;
//    private float mTouchY;
//    private VideoAlbumAdapter mVideoAlbumAdapter;
//    private List<VideoAlbumEntity> mVideoAlbumEntityList;
//    private VideoFolderAdapter mVideoFolderAdapter;
//    private List<VideoFolderEntity> mVideoFolderEntityList;
//    private boolean mVideoIsEmpty;
//    View multiple_loading;
//    ImageView multiple_select_confirm;
//    private ArrayList<AlbumPictureEntity> nextCache;
//    private int paddingBottom;
//    private ArrayList<String> pathList;
//    AvenirNextRegularTextView photo_swap_icon;
//    ImageView photo_swap_icon_bottom;
//    RelativeLayout photo_swap_icon_parent;
//    PtrAlbumViewLayout pull_refresh_pcfl;
//    private boolean requestedPermission;
//    private ArrayList<AlbumPictureEntity> resetEntity;
//    AvenirNextRegularTextView select_anrtv;
//    private ArrayList<AlbumPictureEntity> selectedAlbumPictureEntity;
//    private boolean toFilter;
//    FrameLayout video_grid_container;
//    RecyclerView video_grid_recyclerview;
//    AvenirNextRegularTextView video_swap_icon;
//    ImageView video_swap_icon_bottom;
//    RelativeLayout video_swap_icon_parent;
//    private ArrayList<LongVideosModel> videosModels;
//
//    public interface OnHideAnimationListener {
//        void hide();
//    }
//
//    private class AlphaTouchListener implements OnTouchListener {
//        private View view;
//
//        private AlphaTouchListener() {
//        }
//
//        /* synthetic */ AlphaTouchListener(DisplayAlbumView x0, AnonymousClass1 x1) {
//            this();
//        }
//
//        public void setView(View view) {
//            this.view = view;
//        }
//
//        public View getView() {
//            return this.view;
//        }
//
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case 0:
//                    this.view.setAlpha(0.3f);
//                    pull_refresh_pcfl.setEnabled(false);
//                    return true;
//                case 1:
//                case 3:
//                    this.view.setAlpha(1.0f);
//                    pull_refresh_pcfl.setEnabled(true);
//                    break;
//            }
//            return false;
//        }
//    }
//
//    public class ItemSpaceDecoration extends ItemDecoration {
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
//            outRect.bottom = mItemSpace;
//        }
//    }
//
//    private class OtherTouchListener implements OnTouchListener {
//        private OtherTouchListener() {
//        }
//
//        /* synthetic */ OtherTouchListener(DisplayAlbumView x0, AnonymousClass1 x1) {
//            this();
//        }
//
//        public boolean onTouch(View v, MotionEvent event) {
//            return false;
//        }
//    }
//
//    public DisplayAlbumView(Context context) {
//        this(context, null);
//    }
//
//    public DisplayAlbumView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public DisplayAlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.mOnlyPictureFolderList = new ArrayList();
//        this.mOnlyVideoFolderList = new ArrayList();
//        this.mOnlyGifFolderList = new ArrayList();
//        this.requestedPermission = false;
//        this.isReadData = false;
//        this.nextCache = new ArrayList();
//        this.resetEntity = new ArrayList();
//        this.isNoPicAndVideo = true;
//        this.curModuleIndex = -1;
//        this.curNoDraftIndex = -1;
//        this.mChangeAlphaView = false;
//        this.isFromVideoActivity2 = false;
//        this.mHandler = new WeakHandler(new Callback() {
//            public boolean handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 1:
//                        startFilterActivity();
//                        setMultipleConfirmLoading(false);
//                        break;
//                    case 2:
//                        EventBus.getDefault().post(new DestroyActivityEvent(ActivityState.LONGVIDEOLOCAL, videosModels));
//                        setMultipleConfirmLoading(false);
//                        break;
//                    case 3:
//                        getActivity().lambda$verifyCameraPermission$3();
//                        break;
//                }
//                return false;
//            }
//        });
//        this.fromFilterActivity = false;
//        this.lockData = false;
//        this.isFirstShowFolderAnim = true;
//        this.currentState = 0;
//        this.videosModels = new ArrayList();
//        this.mLoaderCallback = new LoaderCallbacks<Cursor>() {
//            private final String[] VIDEO_PROJECTION = new String[]{"_data", "_display_name", "date_added", "duration", "width", "height", "_data", CacheHelper.ID};
//            private final String[] VideoThumbColumns = new String[]{"_data", "video_id"};
//
//            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//                if (id == 0) {
//                    return new CursorLoader(getActivity(), Media.EXTERNAL_CONTENT_URI, this.VIDEO_PROJECTION, null, null, this.VIDEO_PROJECTION[2] + " DESC");
//                } else if (id != 1) {
//                    return null;
//                } else {
//                    return new CursorLoader(getActivity(), Media.EXTERNAL_CONTENT_URI, this.VIDEO_PROJECTION, this.VIDEO_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, this.VIDEO_PROJECTION[2] + " DESC");
//                }
//            }
//
//            public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
//                new Thread(new Runnable() {
//                    public void run() {
//                        AnonymousClass19.this.sortOutVideoData(cursor);
//                    }
//                }).start();
//            }
//
//            private void sortOutVideoData(Cursor cursor) {
//                if (cursor == null || cursor.isClosed()) {
//                    mVideoIsEmpty = true;
//                    App.runOnUiThread(new Runnable() {
//                        public void run() {
//                            onLoaderFinish();
//                        }
//                    });
//                    return;
//                }
//                String parentName;
//                List<VideoAlbumEntity> videoAlbumEntityList;
//                String TITLE_CAMERA_ROLL = App.getResource().getString(2131297049);
//                List<VideoAlbumEntity> allVideoAlbumEntityList = new ArrayList();
//                if (!cursor.isClosed()) {
//                    try {
//                        mStringListMap.clear();
//                        cursor.moveToFirst();
//                        while (!cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast()) {
//                            if (mCanLoadData) {
//                                if (!cursor.isClosed()) {
//                                    long videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[3]));
//                                    if (videoDuration >= 1000) {
//                                        if (!cursor.isClosed()) {
//                                            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[0]));
//                                            if (videoPath != null && videoPath.length() != 0) {
//                                                File file = new File(videoPath);
//                                                if (file.exists()) {
//                                                    parentName = file.getParentFile().getName();
//                                                    if (!videoPath.contains("mkv")) {
//                                                        if (MediaUtils.isVideo(MediaUtils.extractMime(videoPath))) {
//                                                            if (cursor.isClosed()) {
//                                                                break;
//                                                            }
//                                                            String videoName = cursor.getString(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[1]));
//                                                            long videoDateTime = cursor.getLong(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[2]));
//                                                            int videoWidth = cursor.getInt(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[4]));
//                                                            int videoHeight = cursor.getInt(cursor.getColumnIndexOrThrow(this.VIDEO_PROJECTION[5]));
//                                                            if (VERSION.SDK_INT < 23) {
//                                                                if (videoWidth >= videoHeight && videoHeight >= 2000) {
//                                                                    cursor.moveToNext();
//                                                                } else if (videoHeight >= videoWidth && videoWidth >= 2000) {
//                                                                    cursor.moveToNext();
//                                                                }
//                                                            }
//                                                            String model = Build.MODEL;
//                                                            int currentMax = Math.max(videoWidth, videoHeight);
//                                                            if (!model.contains("SM-") || currentMax <= 3500) {
//                                                                String videoThumbnailPath = "";
//                                                                String localThumbnailPath = Config.getThumbnailPath() + "thumbnail_" + videoWidth + "_" + videoHeight + "_" + videoDateTime + "_" + videoDuration + ".jpg";
//                                                                if (new File(localThumbnailPath).exists()) {
//                                                                    videoThumbnailPath = localThumbnailPath;
//                                                                }
//                                                                LogUtil.d("wangchenvideo", ">>>>>>>>>>>   videoName:" + videoName + "\n, videoDuration:" + videoDuration + "\n, videoPath:" + videoPath + "\n, videoThumbnailPath:" + videoThumbnailPath + "\n, videoWidth:" + videoWidth + "\n, videoHeight:" + videoHeight);
//                                                                VideoAlbumEntity videoAlbumEntity = new VideoAlbumEntity();
//                                                                videoAlbumEntity.setVideoPath(videoPath);
//                                                                videoAlbumEntity.setVideoName(videoName);
//                                                                videoAlbumEntity.setVideoDataTime(videoDateTime);
//                                                                videoAlbumEntity.setVideoDuration(videoDuration);
//                                                                videoAlbumEntity.setVideoWidth(videoWidth);
//                                                                videoAlbumEntity.setVideoHeight(videoHeight);
//                                                                videoAlbumEntity.setVideoThumbnailPath(videoThumbnailPath, localThumbnailPath);
//                                                                allVideoAlbumEntityList.add(videoAlbumEntity);
//                                                                if (mStringListMap.containsKey(parentName)) {
//                                                                    videoAlbumEntityList = (List) mStringListMap.get(parentName);
//                                                                    videoAlbumEntityList.add(videoAlbumEntity);
//                                                                    mStringListMap.put(parentName, videoAlbumEntityList);
//                                                                } else {
//                                                                    videoAlbumEntityList = new ArrayList();
//                                                                    videoAlbumEntityList.add(videoAlbumEntity);
//                                                                    mStringListMap.put(parentName, videoAlbumEntityList);
//                                                                }
//                                                                cursor.moveToNext();
//                                                            } else {
//                                                                cursor.moveToNext();
//                                                            }
//                                                        } else {
//                                                            cursor.moveToNext();
//                                                        }
//                                                    } else {
//                                                        cursor.moveToNext();
//                                                    }
//                                                } else {
//                                                    cursor.moveToNext();
//                                                }
//                                            } else {
//                                                cursor.moveToNext();
//                                            }
//                                        } else {
//                                            break;
//                                        }
//                                    }
//                                    cursor.moveToNext();
//                                } else {
//                                    break;
//                                }
//                            }
//                            return;
//                        }
//                    } catch (StaleDataException e) {
//                        e.printStackTrace();
//                    } catch (IllegalStateException e2) {
//                        e2.printStackTrace();
//                    } catch (CursorIndexOutOfBoundsException e3) {
//                        e3.printStackTrace();
//                    }
//                }
//                if (TextUtil.isValidate(allVideoAlbumEntityList)) {
//                    List<VideoAlbumEntity> entityList = new ArrayList();
//                    entityList.addAll(allVideoAlbumEntityList);
//                    mStringListMap.put(TITLE_CAMERA_ROLL, entityList);
//                    mGifAlbumEntityList.clear();
//                    mGifAlbumEntityList.add(new VideoAlbumEntity((int) Item.PICTURE_SYNTHESIS_ENTER_TYPE));
//                    mVideoAlbumEntityList.clear();
//                    for (VideoAlbumEntity entity : allVideoAlbumEntityList) {
//                        if (entity.getVideoDuration() > 1000) {
//                            List<VideoAlbumEntity> selectVideoBeans = mVideoAlbumAdapter.getSelectVideoBeans();
//                            if (TextUtil.isValidate(selectVideoBeans)) {
//                                List<String> selectVideoPaths = mVideoAlbumAdapter.getSelectVideoPaths();
//                                if (!TextUtil.isValidate(selectVideoPaths)) {
//                                    mVideoAlbumEntityList.add(entity);
//                                } else if (selectVideoPaths.contains(entity.getVideoPath())) {
//                                    mVideoAlbumEntityList.add(selectVideoBeans.get(selectVideoPaths.indexOf(entity.getVideoPath())));
//                                } else {
//                                    mVideoAlbumEntityList.add(entity);
//                                }
//                            } else {
//                                mVideoAlbumEntityList.add(entity);
//                            }
//                        }
//                        if (entity.getVideoDuration() >= Constants.VIDEO_BLACK_DURATION) {
//                            mGifAlbumEntityList.add(entity);
//                        }
//                    }
//                    Map<String, List<VideoAlbumEntity>> map = new HashMap();
//                    map.putAll(mStringListMap);
//                    if (TextUtil.isValidate(mVideoFolderEntityList)) {
//                        App.runOnUiThread(new Runnable() {
//                            public void run() {
//                                mVideoFolderAdapter.setList(new ArrayList());
//                                mVideoFolderAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                    mVideoFolderEntityList.clear();
//                    for (Entry entry : map.entrySet()) {
//                        parentName = (String) entry.getKey();
//                        videoAlbumEntityList = (List) entry.getValue();
//                        int count = videoAlbumEntityList.size();
//                        if (count > 0) {
//                            VideoFolderEntity videoFolderEntity = new VideoFolderEntity();
//                            videoFolderEntity.setFolderName(parentName);
//                            videoFolderEntity.setThumbPath(((VideoAlbumEntity) videoAlbumEntityList.get(count - 1)).getVideoPath());
//                            videoFolderEntity.setVideoCount(count);
//                            videoFolderEntity.setVideoAlbumEntityList(videoAlbumEntityList);
//                            if (parentName.equals(TITLE_CAMERA_ROLL)) {
//                                mVideoFolderEntityList.add(0, videoFolderEntity);
//                            } else {
//                                mVideoFolderEntityList.add(videoFolderEntity);
//                            }
//                        }
//                    }
//                }
//                if (judgeActivity()) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            onLoaderFinish();
//                        }
//                    });
//                }
//            }
//
//            public void onLoaderReset(Loader<Cursor> loader) {
//            }
//        };
//        this.isLoadingPic = true;
//        this.mPictureLoaderCallback = new LoaderCallbacks<Cursor>() {
//            private final String[] IMAGE_PROJECTION = new String[]{"_data", "_display_name", "date_added", "width", "height", CacheHelper.ID};
//
//            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//                if (id == 2) {
//                    return new CursorLoader(getActivity(), Images.Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, null, null, this.IMAGE_PROJECTION[2] + " DESC");
//                } else if (id != 1) {
//                    return null;
//                } else {
//                    return new CursorLoader(getActivity(), Images.Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, this.IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, this.IMAGE_PROJECTION[2] + " DESC");
//                }
//            }
//
//            public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
//                new Thread() {
//                    public void run() {
//                        AnonymousClass21.this.sortOutPictureData(data);
//                    }
//                }.start();
//            }
//
//            /* JADX WARNING: Removed duplicated region for block: B:182:0x0211 A:{SYNTHETIC} */
//            /* JADX WARNING: Removed duplicated region for block: B:44:0x01b1  */
//            /* JADX WARNING: Removed duplicated region for block: B:55:0x0240 A:{Splitter: B:60:0x024c, ExcHandler: android.database.StaleDataException (r16_2 'e' android.database.StaleDataException)} */
//            /* JADX WARNING: Removed duplicated region for block: B:63:0x0251 A:{Splitter: B:8:0x0027, ExcHandler: android.database.CursorIndexOutOfBoundsException (r16_3 'e' android.database.CursorIndexOutOfBoundsException)} */
//            /* JADX WARNING: Failed to process nested try/catch */
//            /* JADX WARNING: Missing block: B:55:0x0240, code:
//            r16 = move-exception;
//     */
//            /* JADX WARNING: Missing block: B:56:0x0241, code:
//            r16.printStackTrace();
//     */
//            /* JADX WARNING: Missing block: B:63:0x0251, code:
//            r16 = move-exception;
//     */
//            /* JADX WARNING: Missing block: B:64:0x0252, code:
//            r16.printStackTrace();
//     */
//            /* JADX WARNING: Missing block: B:87:0x02b3, code:
//            r16 = move-exception;
//     */
//            /* JADX WARNING: Missing block: B:88:0x02b4, code:
//            r16.printStackTrace();
//     */
//            private void sortOutPictureData(android.database.Cursor r36) {
//                /*
//                r35 = this;
//                if (r36 == 0) goto L_0x0008;
//            L_0x0002:
//                r32 = r36.isClosed();
//                if (r32 == 0) goto L_0x0015;
//            L_0x0008:
//                r32 = new com.blink.academy.onetake.widgets.DisplayAlbumView$21$2;
//                r0 = r32;
//                r1 = r35;
//                r0.<init>();
//                com.blink.academy.onetake.App.runOnUiThread(r32);
//            L_0x0014:
//                return;
//            L_0x0015:
//                r12 = new java.util.ArrayList;
//                r12.<init>();
//                r14 = 0;
//                r32 = r36.isClosed();
//                if (r32 != 0) goto L_0x0025;
//            L_0x0021:
//                r14 = r36.getCount();
//            L_0x0025:
//                if (r14 <= 0) goto L_0x0716;
//            L_0x0027:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r32 = r32.mOnlyPictureFolderList;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32.clear();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r36.moveToFirst();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r13 = r36.getColumnCount();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r35;
//                r0 = r0.IMAGE_PROJECTION;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r33 = 1;
//                r32 = r32[r33];	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r25 = r0.getColumnIndexOrThrow(r1);	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r35;
//                r0 = r0.IMAGE_PROJECTION;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r33 = 2;
//                r32 = r32[r33];	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r15 = r0.getColumnIndexOrThrow(r1);	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r35;
//                r0 = r0.IMAGE_PROJECTION;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r33 = 3;
//                r32 = r32[r33];	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r31 = r0.getColumnIndexOrThrow(r1);	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r35;
//                r0 = r0.IMAGE_PROJECTION;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r33 = 4;
//                r32 = r32[r33];	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r22 = r0.getColumnIndexOrThrow(r1);	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//            L_0x0083:
//                r32 = r36.isClosed();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 != 0) goto L_0x00a7;
//            L_0x0089:
//                r32 = r36.isBeforeFirst();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 != 0) goto L_0x00a7;
//            L_0x008f:
//                r32 = r36.isAfterLast();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 != 0) goto L_0x00a7;
//            L_0x0095:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r32 = r32.mCanLoadData;	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 == 0) goto L_0x0014;
//            L_0x00a1:
//                r32 = r36.isClosed();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 == 0) goto L_0x0215;
//            L_0x00a7:
//                r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r12);
//                if (r32 == 0) goto L_0x0593;
//            L_0x00ad:
//                r23 = 0;
//            L_0x00af:
//                r32 = r12.size();
//                r0 = r23;
//                r1 = r32;
//                if (r0 >= r1) goto L_0x02c0;
//            L_0x00b9:
//                r0 = r23;
//                r5 = r12.get(r0);
//                r5 = (com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity) r5;
//                r32 = new java.io.File;
//                r33 = r5.getPath();
//                r32.<init>(r33);
//                r27 = r32.getParentFile();
//                r19 = r27.getAbsolutePath();
//                r7 = r27.getName();
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r28 = r32.size();
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r0 = r32;
//                r1 = r28;
//                r0.afterSize = r1;
//                r24 = 0;
//            L_0x00f3:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r32 = r32.size();
//                r0 = r24;
//                r1 = r32;
//                if (r0 >= r1) goto L_0x01a1;
//            L_0x0107:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r32 = r32.size();
//                r0 = r24;
//                r1 = r32;
//                if (r0 >= r1) goto L_0x02bc;
//            L_0x011b:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ Exception -> 0x02b9 }
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;	 Catch:{ Exception -> 0x02b9 }
//                r0 = r32;
//                r1 = r24;
//                r29 = r0.get(r1);	 Catch:{ Exception -> 0x02b9 }
//                r29 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r29;	 Catch:{ Exception -> 0x02b9 }
//                r0 = r29;
//                r6 = r0.folderName;	 Catch:{ Exception -> 0x02b9 }
//                if (r7 == 0) goto L_0x02bc;
//            L_0x0135:
//                r32 = r6.equals(r7);
//                if (r32 == 0) goto L_0x02bc;
//            L_0x013b:
//                r0 = r29;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                if (r32 != 0) goto L_0x014e;
//            L_0x0143:
//                r32 = new java.util.ArrayList;
//                r32.<init>();
//                r0 = r32;
//                r1 = r29;
//                r1.albumPictureEntities = r0;
//            L_0x014e:
//                r0 = r29;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                r0 = r29;
//                r0 = r0.albumPictureEntities;
//                r33 = r0;
//                r33 = r33.size();
//                r0 = r32;
//                r1 = r33;
//                r0.add(r1, r5);
//                r0 = r29;
//                r0 = r0.pictureCount;
//                r32 = r0;
//                r32 = r32 + 1;
//                r0 = r32;
//                r1 = r29;
//                r1.pictureCount = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyPictureFolderList;
//                r0 = r32;
//                r1 = r29;
//                r32 = r0.contains(r1);
//                if (r32 != 0) goto L_0x0198;
//            L_0x0187:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyPictureFolderList;
//                r0 = r32;
//                r1 = r29;
//                r0.add(r1);
//            L_0x0198:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32.afterSize = r32.afterSize + 1;
//            L_0x01a1:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.afterSize;
//                r0 = r28;
//                r1 = r32;
//                if (r0 != r1) goto L_0x0211;
//            L_0x01b1:
//                r26 = new com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity;
//                r26.<init>();
//                r32 = new java.util.ArrayList;
//                r32.<init>();
//                r0 = r32;
//                r1 = r26;
//                r1.albumPictureEntities = r0;
//                r0 = r26;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                r0 = r32;
//                r0.add(r5);
//                r0 = r19;
//                r1 = r26;
//                r1.foldPath = r0;
//                r0 = r26;
//                r0.folderName = r7;
//                r32 = r5.getPath();
//                r0 = r26;
//                r1 = r32;
//                r0.setThumbPath(r1);
//                r0 = r26;
//                r0 = r0.pictureCount;
//                r32 = r0;
//                r32 = r32 + 1;
//                r0 = r32;
//                r1 = r26;
//                r1.pictureCount = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r0 = r32;
//                r1 = r26;
//                r0.add(r1);
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyPictureFolderList;
//                r0 = r32;
//                r1 = r26;
//                r0.add(r1);
//            L_0x0211:
//                r23 = r23 + 1;
//                goto L_0x00af;
//            L_0x0215:
//                r0 = r35;
//                r0 = r0.IMAGE_PROJECTION;	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r33 = 0;
//                r32 = r32[r33];	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r32 = r0.getColumnIndexOrThrow(r1);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r36;
//                r1 = r32;
//                r6 = r0.getString(r1);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = com.blink.academy.onetake.LoaderHelper.checkPhotoFilePathIsValid(r6);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 != 0) goto L_0x0246;
//            L_0x0235:
//                r36.moveToNext();	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x023a:
//                r16 = move-exception;
//                r36.moveToNext();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x0240:
//                r16 = move-exception;
//                r16.printStackTrace();
//                goto L_0x00a7;
//            L_0x0246:
//                r0 = r25;
//                if (r0 >= r13) goto L_0x024c;
//            L_0x024a:
//                if (r25 >= 0) goto L_0x0257;
//            L_0x024c:
//                r36.moveToNext();	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x0251:
//                r16 = move-exception;
//                r16.printStackTrace();
//                goto L_0x00a7;
//            L_0x0257:
//                r0 = r36;
//                r1 = r25;
//                r7 = r0.getString(r1);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r15 >= r13) goto L_0x0263;
//            L_0x0261:
//                if (r15 >= 0) goto L_0x0268;
//            L_0x0263:
//                r36.moveToNext();	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x0268:
//                r0 = r36;
//                r8 = r0.getLong(r15);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r31;
//                if (r0 >= r13) goto L_0x0274;
//            L_0x0272:
//                if (r31 >= 0) goto L_0x0279;
//            L_0x0274:
//                r36.moveToNext();	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x0279:
//                r0 = r36;
//                r1 = r31;
//                r10 = r0.getInt(r1);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r0 = r22;
//                if (r0 >= r13) goto L_0x0287;
//            L_0x0285:
//                if (r22 >= 0) goto L_0x028c;
//            L_0x0287:
//                r36.moveToNext();	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x028c:
//                r0 = r36;
//                r1 = r22;
//                r11 = r0.getInt(r1);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r10 == 0) goto L_0x02ae;
//            L_0x0296:
//                if (r11 == 0) goto L_0x02ae;
//            L_0x0298:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r32 = r0;
//                r0 = r32;
//                r32 = r0.isWidthAndHeightIsValid(r10, r11);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                if (r32 == 0) goto L_0x02ae;
//            L_0x02a6:
//                r5 = new com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity;	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r5.<init>(r6, r7, r8, r10, r11);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//                r12.add(r5);	 Catch:{ IllegalStateException -> 0x023a, StaleDataException -> 0x0240, CursorIndexOutOfBoundsException -> 0x0251 }
//            L_0x02ae:
//                r36.moveToNext();	 Catch:{ StaleDataException -> 0x0240, IllegalStateException -> 0x02b3, CursorIndexOutOfBoundsException -> 0x0251 }
//                goto L_0x0083;
//            L_0x02b3:
//                r16 = move-exception;
//                r16.printStackTrace();
//                goto L_0x00a7;
//            L_0x02b9:
//                r16 = move-exception;
//                goto L_0x01a1;
//            L_0x02bc:
//                r24 = r24 + 1;
//                goto L_0x00f3;
//            L_0x02c0:
//                r20 = "";
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r32 = r32.size();
//                if (r32 == 0) goto L_0x02e8;
//            L_0x02d2:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r20 = r32.getFolderName();
//            L_0x02e8:
//                r32 = com.blink.academy.onetake.App.getResource();
//                r33 = 2131297049; // 0x7f090319 float:1.8212032E38 double:1.053000653E-314;
//                r4 = r32.getString(r33);
//                r0 = r20;
//                r32 = r0.equals(r4);
//                if (r32 != 0) goto L_0x03e5;
//            L_0x02fb:
//                r29 = new com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity;
//                r29.<init>();
//                r0 = r29;
//                r0.setFolderName(r4);
//                r32 = r12.size();
//                if (r32 == 0) goto L_0x0320;
//            L_0x030b:
//                r32 = 0;
//                r0 = r32;
//                r32 = r12.get(r0);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity) r32;
//                r32 = r32.getPath();
//                r0 = r29;
//                r1 = r32;
//                r0.setThumbPath(r1);
//            L_0x0320:
//                r32 = r12.size();
//                r0 = r29;
//                r1 = r32;
//                r0.setVideoCount(r1);
//                r32 = new java.util.ArrayList;
//                r32.<init>();
//                r0 = r32;
//                r1 = r29;
//                r1.albumPictureEntities = r0;
//                r0 = r29;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                r0 = r32;
//                r0.addAll(r12);
//                r32 = r12.size();
//                r0 = r32;
//                r1 = r29;
//                r1.pictureCount = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.curModuleIndex;
//                r33 = 1;
//                r0 = r32;
//                r1 = r33;
//                if (r0 != r1) goto L_0x0372;
//            L_0x035d:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r0 = r32;
//                r1 = r33;
//                r2 = r29;
//                r0.add(r1, r2);
//            L_0x0372:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyGifFolderList;
//                r32.clear();
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyVideoFolderList;
//                r32.clear();
//                r23 = 0;
//            L_0x038e:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r32 = r32.size();
//                r0 = r23;
//                r1 = r32;
//                if (r0 >= r1) goto L_0x04c2;
//            L_0x03a2:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r0 = r32;
//                r1 = r23;
//                r30 = r0.get(r1);
//                r30 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r30;
//                if (r30 == 0) goto L_0x03e2;
//            L_0x03b8:
//                r0 = r30;
//                r0 = r0.pictureCount;
//                r32 = r0;
//                if (r32 != 0) goto L_0x0461;
//            L_0x03c0:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyGifFolderList;
//                r0 = r32;
//                r1 = r30;
//                r0.add(r1);
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyVideoFolderList;
//                r0 = r32;
//                r1 = r30;
//                r0.add(r1);
//            L_0x03e2:
//                r23 = r23 + 1;
//                goto L_0x038e;
//            L_0x03e5:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r33 = new java.util.ArrayList;
//                r33.<init>();
//                r0 = r33;
//                r1 = r32;
//                r1.albumPictureEntities = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r0 = r32;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                r0 = r32;
//                r0.addAll(r12);
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r0 = r32;
//                r0 = r0.videoCount;
//                r33 = r0;
//                r34 = r12.size();
//                r33 = r33 + r34;
//                r0 = r33;
//                r1 = r32;
//                r1.videoCount = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r33 = r12.size();
//                r0 = r33;
//                r1 = r32;
//                r1.pictureCount = r0;
//                goto L_0x0372;
//            L_0x0461:
//                r0 = r30;
//                r0 = r0.videoCount;
//                r32 = r0;
//                if (r32 <= 0) goto L_0x03e2;
//            L_0x0469:
//                if (r23 == 0) goto L_0x03e2;
//            L_0x046b:
//                r29 = new com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity;
//                r29.<init>();
//                r32 = r30.getFolderName();
//                r0 = r29;
//                r1 = r32;
//                r0.setFolderName(r1);
//                r0 = r30;
//                r0 = r0.videoCount;
//                r32 = r0;
//                r0 = r29;
//                r1 = r32;
//                r0.setVideoCount(r1);
//                r32 = r30.getVideoAlbumEntityList();
//                r0 = r29;
//                r1 = r32;
//                r0.setVideoAlbumEntityList(r1);
//                r32 = r30.getThumbPath();
//                r0 = r29;
//                r1 = r32;
//                r0.setThumbPath(r1);
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyGifFolderList;
//                r0 = r32;
//                r1 = r29;
//                r0.add(r1);
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mOnlyVideoFolderList;
//                r0 = r32;
//                r1 = r29;
//                r0.add(r1);
//                goto L_0x03e2;
//            L_0x04c2:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.curModuleIndex;
//                r33 = 2;
//                r0 = r32;
//                r1 = r33;
//                if (r0 != r1) goto L_0x059c;
//            L_0x04d4:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r33 = r0;
//                r33 = r33.mOnlyVideoFolderList;
//                r32.resetFolderList(r33);
//            L_0x04e7:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r21 = r32.size();
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r32.clear();
//                if (r21 != 0) goto L_0x05d2;
//            L_0x0504:
//                r28 = 0;
//            L_0x0506:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r0 = r32;
//                r0.addAll(r12);
//                r18 = "";
//                r17 = "";
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoAlbumEntityList;
//                r32 = r32.size();
//                if (r32 <= 0) goto L_0x0638;
//            L_0x0529:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r32 = r32.getVideoThumbnailPath();
//                r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r32);
//                if (r32 == 0) goto L_0x0620;
//            L_0x0545:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r18 = r32.getVideoThumbnailPath();
//            L_0x055b:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r32 = r32.size();
//                if (r32 <= 0) goto L_0x06bb;
//            L_0x056b:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity) r32;
//                r17 = r32.getPath();
//            L_0x0581:
//                r32 = de.greenrobot.event.EventBus.getDefault();
//                r33 = new com.blink.academy.onetake.support.events.DraftsLoadFinishEvent;
//                r0 = r33;
//                r1 = r18;
//                r2 = r17;
//                r0.<init>(r1, r2);
//                r32.post(r33);
//            L_0x0593:
//                r32 = com.blink.academy.onetake.widgets.DisplayAlbumView$21$.Lambda.1.lambdaFactory$(r35);
//                com.blink.academy.onetake.App.runOnUiThread(r32);
//                goto L_0x0014;
//            L_0x059c:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.curModuleIndex;
//                if (r32 != 0) goto L_0x05bd;
//            L_0x05a8:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r33 = r0;
//                r33 = r33.mOnlyGifFolderList;
//                r32.resetFolderList(r33);
//                goto L_0x04e7;
//            L_0x05bd:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r33 = r0;
//                r33 = r33.mOnlyPictureFolderList;
//                r32.resetFolderList(r33);
//                goto L_0x04e7;
//            L_0x05d2:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                if (r32 == 0) goto L_0x061c;
//            L_0x05e4:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r0 = r32;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                if (r32 == 0) goto L_0x061c;
//            L_0x05fe:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoFolderEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoFolderEntity) r32;
//                r0 = r32;
//                r0 = r0.albumPictureEntities;
//                r32 = r0;
//                r28 = r32.size();
//                goto L_0x0506;
//            L_0x061c:
//                r28 = 0;
//                goto L_0x0506;
//            L_0x0620:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mVideoAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r18 = r32.getVideoPath();
//                goto L_0x055b;
//            L_0x0638:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r32 = r32.size();
//                if (r32 <= 0) goto L_0x0693;
//            L_0x0648:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r32 = r32.getVideoThumbnailPath();
//                r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r32);
//                if (r32 == 0) goto L_0x067c;
//            L_0x0664:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r18 = r32.getVideoThumbnailPath();
//            L_0x067a:
//                goto L_0x055b;
//            L_0x067c:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r18 = r32.getVideoPath();
//                goto L_0x067a;
//            L_0x0693:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r32 = r32.size();
//                if (r32 <= 0) goto L_0x055b;
//            L_0x06a3:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mAlbumPictureEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.AlbumPictureEntity) r32;
//                r18 = r32.getPath();
//                goto L_0x055b;
//            L_0x06bb:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r32 = r32.size();
//                if (r32 <= 0) goto L_0x0581;
//            L_0x06cb:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r32 = r32.getVideoThumbnailPath();
//                r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r32);
//                if (r32 == 0) goto L_0x06ff;
//            L_0x06e7:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r17 = r32.getVideoThumbnailPath();
//            L_0x06fd:
//                goto L_0x0581;
//            L_0x06ff:
//                r0 = r35;
//                r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                r32 = r0;
//                r32 = r32.mGifAlbumEntityList;
//                r33 = 0;
//                r32 = r32.get(r33);
//                r32 = (com.blink.academy.onetake.ui.adapter.entities.VideoAlbumEntity) r32;
//                r17 = r32.getVideoPath();
//                goto L_0x06fd;
//            L_0x0716:
//                r32 = new com.blink.academy.onetake.widgets.DisplayAlbumView$21$3;
//                r0 = r32;
//                r1 = r35;
//                r0.<init>();
//                com.blink.academy.onetake.App.runOnUiThread(r32);
//                goto L_0x0014;
//                */
//                throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.widgets.DisplayAlbumView.21.sortOutPictureData(android.database.Cursor):void");
//            }
//
//            private /* synthetic */ void lambda$sortOutPictureData$0() {
//                onPicLoaderFinish();
//                Adapter adapter = video_grid_recyclerview.getAdapter();
//                if (adapter != null && curModuleIndex == 1) {
//                    adapter.notifyDataSetChanged();
//                }
//                if (mVideoFolderAdapter != null) {
//                    mVideoFolderAdapter.setList(mVideoFolderEntityList);
//                    mVideoFolderAdapter.notifyDataSetChanged();
//                }
//            }
//
//            public void onLoaderReset(Loader<Cursor> loader) {
//            }
//        };
//        initViews();
//    }
//
//    public void setIsFromVideoActivity2(boolean b) {
//        this.isFromVideoActivity2 = b;
//    }
//
//    private void initViews() {
//        BuglyLogUtil.writeBuglyLog(LOG_TAG);
//        App.RegisterEventBus(this);
//        initializeViews();
//        initializeData();
//        initializeView();
//    }
//
//    public void setFromFilterActivity(boolean fromFilterActivity) {
//        this.fromFilterActivity = fromFilterActivity;
//        this.mVideoAlbumAdapter.setFromFilterActivity(fromFilterActivity);
//    }
//
//    public void setFilterName(String filterName) {
//        this.mVideoAlbumAdapter.setFilterName(filterName);
//    }
//
//    private void initializeViews() {
//        View inflate = LayoutInflater.from(getContext()).inflate(2130968666, this);
//        this.loading_cpb = inflate.findViewById(2131689643);
//        this.gif_swap_icon_parent = (RelativeLayout) inflate.findViewById(2131690246);
//        this.gif_swap_icon = (AvenirNextRegularTextView) inflate.findViewById(2131690247);
//        this.gif_swap_icon_bottom = (ImageView) inflate.findViewById(2131690248);
//        this.video_swap_icon_parent = (RelativeLayout) inflate.findViewById(2131690242);
//        this.video_swap_icon = (AvenirNextRegularTextView) inflate.findViewById(2131690004);
//        this.video_swap_icon_bottom = (ImageView) inflate.findViewById(2131690243);
//        this.photo_swap_icon_parent = (RelativeLayout) inflate.findViewById(2131690244);
//        this.photo_swap_icon = (AvenirNextRegularTextView) inflate.findViewById(2131690005);
//        this.photo_swap_icon_bottom = (ImageView) inflate.findViewById(2131690245);
//        this.draft_swap_icon_parent = (RelativeLayout) inflate.findViewById(2131690250);
//        this.draft_swap_icon = (AvenirNextRegularTextView) inflate.findViewById(2131690251);
//        this.draft_swap_icon_bottom = (ImageView) inflate.findViewById(2131690252);
//        this.icon_iv = (ImageView) inflate.findViewById(2131690002);
//        this.category_layout_rl = (RelativeLayout) inflate.findViewById(2131690000);
//        this.category_anrtv = (AvenirNextRegularTextView) inflate.findViewById(2131690001);
//        this.choose_ll = inflate.findViewById(2131690003);
//        this.drafts_anrtv = (AvenirNextRegularTextView) inflate.findViewById(2131690240);
//        this.select_anrtv = (AvenirNextRegularTextView) inflate.findViewById(2131690241);
//        this.video_grid_recyclerview = (RecyclerView) inflate.findViewById(2131690006);
//        this.folder_grid_recyclerview = (RecyclerView) inflate.findViewById(2131690007);
//        this.drag_vertical_layout = (RelativeLayout) inflate.findViewById(2131689999);
//        this.pull_refresh_pcfl = (PtrAlbumViewLayout) inflate.findViewById(2131689711);
//        this.video_grid_container = (FrameLayout) inflate.findViewById(2131690254);
//        this.drafts_container = (FrameLayout) inflate.findViewById(2131690253);
//        this.multiple_loading = inflate.findViewById(2131690258);
//        this.indicator_draft = inflate.findViewById(2131690249);
//        this.fl_multiple_select = (FrameLayout) inflate.findViewById(R.id.fl_multiple_select);
//        this.multiple_select_confirm = (ImageView) inflate.findViewById(2131690257);
//        this.gif_swap_icon_parent.setOnClickListener(this);
//        this.video_swap_icon_parent.setOnClickListener(this);
//        this.photo_swap_icon_parent.setOnClickListener(this);
//        this.draft_swap_icon_parent.setOnClickListener(this);
//        this.category_layout_rl.setOnClickListener(this);
//        this.mAlphaTouch = new AlphaTouchListener(this, null);
//        this.mOtherTouch = new OtherTouchListener(this, null);
//        this.select_anrtv.setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case 0:
//                        return true;
//                    case 1:
//                    case 3:
//                        String text = (String) select_anrtv.getText();
//                        if (curModuleIndex != 4) {
//                            return true;
//                        }
//                        if (getResources().getString(2131296429).equals(text)) {
//                            changeDraftSelectToCancel();
//                            return true;
//                        } else if (!getResources().getString(2131296430).equals(text)) {
//                            return true;
//                        } else {
//                            changeDraftCancelToSelect();
//                            return true;
//                        }
//                    default:
//                        return false;
//                }
//            }
//        });
//        this.fl_multiple_select.setOnClickListener(this);
//    }
//
//    private void initializeData() {
//        this.mScreenWidth = DensityUtil.getMetricsWidth(getContext());
//        this.mScreenHeight = DensityUtil.getMetricsHeight(getContext());
//        this.mItemSpace = DensityUtil.dip2px(2.0f);
//        if (this.mStringListMap == null) {
//            this.mStringListMap = new Hashtable();
//        }
//        if (this.mGifAlbumEntityList == null) {
//            this.mGifAlbumEntityList = new ArrayList();
//            this.mGifAlbumEntityList.add(new VideoAlbumEntity((int) Item.PICTURE_SYNTHESIS_ENTER_TYPE));
//        }
//        if (this.mGridLayoutManager == null) {
//            this.mGridLayoutManager = new GridLayoutManager(getContext(), 3, 1, false);
//        }
//        if (this.mVideoFolderEntityList == null) {
//            this.mVideoFolderEntityList = new ArrayList();
//        }
//        if (this.mVideoAlbumEntityList == null) {
//            this.mVideoAlbumEntityList = new ArrayList();
//        }
//        if (this.mLinearLayoutManager == null) {
//            this.mLinearLayoutManager = new LinearLayoutManager(getContext(), 1, false);
//        }
//        if (this.mAlbumPictureEntityList == null) {
//            this.mAlbumPictureEntityList = new ArrayList();
//        }
//    }
//
//    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
//        LogUtil.e("slim", "dis setVisibility:" + visibility);
//    }
//
//    public void setAlbumVisibleOrGone(boolean isVisible) {
//        if (isVisible) {
//            LogUtil.e("slim", "setAlbumVisibleOrGone setVisibility:true");
//            setVisibility(0);
//            return;
//        }
//        AnimationUtil.setValueAnimator(false, new ValueAnimatorCallback() {
//            public void updateCallback(float value) {
//                LogUtil.e("slim", "value:" + value);
//                setAlpha(value);
//            }
//
//            public void startCallback() {
//            }
//
//            public void endCallback() {
//                LogUtil.e("slim", "endCallback setVisibility:false");
//                setVisibility(8);
//            }
//        });
//    }
//
//    public void resetAlbumView() {
//        if (this.curModuleIndex == 2) {
//            if (this.mVideoAlbumAdapter != null && TextUtil.isValidate(this.mVideoAlbumEntityList)) {
//                for (VideoAlbumEntity entity : this.mVideoAlbumEntityList) {
//                    entity.setSelect(0);
//                }
//                this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                this.mVideoAlbumAdapter.clearSelList();
//                this.fl_multiple_select.setBackgroundResource(2130838320);
//                this.fl_multiple_select.setEnabled(false);
//                this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                this.mVideoAlbumAdapter.notifyDataSetChanged();
//            }
//        } else if (this.curModuleIndex == 1 && TextUtil.isValidate(this.pathList) && this.mPhotoAlbumAdapter != null && TextUtil.isValidate(this.mAlbumPictureEntityList)) {
//            for (AlbumPictureEntity entity2 : this.mAlbumPictureEntityList) {
//                entity2.selected = 0;
//            }
//            this.mPhotoAlbumAdapter.setCanAlphaShow(true);
//            this.mPhotoAlbumAdapter.clearSelList();
//            this.select_anrtv.setText(getResources().getString(2131296429));
//            this.fl_multiple_select.setBackgroundResource(2130838320);
//            this.fl_multiple_select.setEnabled(false);
//            this.mPhotoAlbumAdapter.setCanAlphaShow(true);
//            this.mPhotoAlbumAdapter.notifyDataSetChanged();
//        }
//        this.pull_refresh_pcfl.resetAlbumView();
//    }
//
//    public PtrAlbumViewLayout getPullRefreshPCFL() {
//        return this.pull_refresh_pcfl;
//    }
//
//    private void initializeView() {
//        final AlbumHeadView ptrClassicHeader = this.pull_refresh_pcfl.getPtrClassicHeader();
//        final int height = ptrClassicHeader.getLayoutParams().height;
//        this.pull_refresh_pcfl.setPtrHandler(new PtrAlbumHander() {
//            public void currentHeaderViewHeight(int headviewHeight) {
//                float f = 1.0f;
//                super.currentHeaderViewHeight(headviewHeight);
//                EventBus.getDefault().post(new AlbumVideoEvent(headviewHeight));
//                ptrClassicHeader.setAlbumHeadViewHeight(headviewHeight);
//                if (headviewHeight < height || headviewHeight > height * 2) {
//                    AlbumHeadView albumHeadView = ptrClassicHeader;
//                    if (headviewHeight >= height) {
//                        f = StaticLayoutUtil.DefaultSpacingadd;
//                    }
//                    albumHeadView.setALbumHeadViewAlpha(f);
//                    return;
//                }
//                ptrClassicHeader.setALbumHeadViewAlpha(2.0f - ((1.0f * ((float) headviewHeight)) / ((float) height)));
//            }
//
//            public void setAlbumVisisbleOrGone(boolean isVisible) {
//                Log.e("slim", "setAlbumVisisbleOrGone");
//                setAlbumVisibleOrGone(isVisible);
//            }
//
//            public void startAlbumVisisbleOrGone(boolean isVisible) {
//                Log.e("slim", "startAlbumVisisbleOrGone");
//                EventBus.getDefault().post(new AlbumVideoEvent(isVisible));
//                if (!isVisible && curModuleIndex != 4) {
//                    EventBus.getDefault().post(new SwitchCameraModeEvent(curModuleIndex));
//                }
//            }
//
//            public boolean checkCanDoRefresh(ViewGroup frame, View content, View header) {
//                if (currentState == 0) {
//                    if (folder_grid_recyclerview == null || folder_grid_recyclerview.getVisibility() != 0) {
//                        if (video_grid_recyclerview.canScrollVertically(-1)) {
//                            return false;
//                        }
//                        return true;
//                    } else if (folder_grid_recyclerview.canScrollVertically(-1)) {
//                        return false;
//                    } else {
//                        return true;
//                    }
//                } else if (draftsFragment.isDraftsSelectMode()) {
//                    return false;
//                } else {
//                    return draftsFragment.isCanScroll();
//                }
//            }
//
//            public void onRefreshBegin(ViewGroup frame) {
//            }
//        });
//        this.mCanLoadData = true;
//        this.loading_cpb.setVisibility(0);
//        this.category_anrtv.setText(getResources().getString(2131297049));
//        this.drafts_anrtv.setText(2131297058);
//        this.select_anrtv.setText(getResources().getString(2131296429));
//        TintColorUtil.tintDrawable(getContext(), this.icon_iv, 2131755096);
//        this.video_grid_recyclerview.setLayoutManager(this.mGridLayoutManager);
//        this.mGridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
//            public int getSpanSize(int position) {
//                if (video_grid_recyclerview.getAdapter() != mGifAlbumAdapter) {
//                    return 1;
//                }
//                if (!TextUtil.isValidate(mGifAlbumEntityList) || position >= mGifAlbumEntityList.size()) {
//                    return 1;
//                }
//                if (((VideoAlbumEntity) mGifAlbumEntityList.get(position)).getViewType() == Item.PICTURE_SYNTHESIS_ENTER_TYPE) {
//                    return 3;
//                }
//                return 1;
//            }
//        });
//        this.video_grid_recyclerview.addItemDecoration(new ItemSpaceDecoration());
//        resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//        resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//        resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//        bindTouchListener(this.video_swap_icon);
//        this.folder_grid_recyclerview.setLayoutManager(this.mLinearLayoutManager);
//    }
//
//    private void bindTouchListener(View v) {
//        this.mAlphaTouch.setView(v);
//        v.setOnTouchListener(this.mAlphaTouch);
//    }
//
//    private void resetImageTextViewState(AvenirNextRegularTextView textView, float tvAlpha, boolean bold, ImageView imageView, int visibility) {
//        textView.setAlpha(tvAlpha);
//        textView.getPaint().setFakeBoldText(bold);
//        textView.setText(textView.getText());
//        imageView.setVisibility(visibility);
//    }
//
//    public void setTimeStamp(String timeStamp) {
//        this.mCurrentTimeStamp = timeStamp;
//        if (this.mGifAlbumAdapter != null) {
//            this.mGifAlbumAdapter.setCurrentTimeStamp(timeStamp);
//        }
//    }
//
//    public void setComeFrom(int from) {
//        this.comeFrom = from;
//        if (this.mGifAlbumAdapter != null) {
//            this.mGifAlbumAdapter.setComeFrom(from);
//        }
//        if (this.mPhotoAlbumAdapter != null) {
//            this.mPhotoAlbumAdapter.setComeFrom(from);
//        }
//        if (this.mVideoAlbumAdapter != null) {
//            this.mVideoAlbumAdapter.setComeFrom(from);
//        }
//        if (this.comeFrom == 1 || this.comeFrom == 2) {
//            this.drafts_anrtv.setVisibility(8);
//            this.select_anrtv.setVisibility(8);
//            this.category_anrtv.setVisibility(0);
//            this.category_anrtv.setText(getResources().getString(2131297049));
//            resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//            resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//            this.video_swap_icon_parent.setVisibility(4);
//            this.draft_swap_icon_parent.setVisibility(4);
//            this.indicator_draft.setVisibility(4);
//            this.video_swap_icon.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
//            this.video_swap_icon_bottom.setVisibility(8);
//            if (!this.isFromVideoActivity2) {
//                this.curModuleIndex = 2;
//                this.curNoDraftIndex = 2;
//            }
//            this.video_grid_recyclerview.setAdapter(this.mVideoAlbumAdapter);
//            resetData(null);
//        } else if (this.comeFrom == 0) {
//            this.drafts_anrtv.setVisibility(8);
//            this.select_anrtv.setVisibility(8);
//            this.category_anrtv.setVisibility(0);
//            this.category_anrtv.setText(getResources().getString(2131297049));
//            resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//            resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//            resetImageTextViewState(this.video_swap_icon, 1.0f, true, this.video_swap_icon_bottom, 0);
//            if (!this.isFromVideoActivity2) {
//                this.curModuleIndex = 2;
//                this.curNoDraftIndex = 2;
//            }
//            this.video_grid_recyclerview.setAdapter(this.mVideoAlbumAdapter);
//            resetData(null);
//        } else if (this.comeFrom == 4) {
//            this.drafts_anrtv.setVisibility(8);
//            this.select_anrtv.setVisibility(8);
//            this.category_anrtv.setVisibility(0);
//            this.category_anrtv.setText(getResources().getString(2131297049));
//            this.gif_swap_icon_parent.setVisibility(8);
//            this.draft_swap_icon_parent.setVisibility(8);
//            this.indicator_draft.setVisibility(8);
//            resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//            resetImageTextViewState(this.video_swap_icon, 1.0f, true, this.video_swap_icon_bottom, 0);
//            this.fl_multiple_select.setVisibility(0);
//            this.curModuleIndex = 2;
//            this.curNoDraftIndex = 2;
//            this.mPhotoAlbumAdapter.setFromFilter(true);
//            this.video_grid_recyclerview.setAdapter(this.mVideoAlbumAdapter);
//            resetData(null);
//        } else {
//            this.video_grid_recyclerview.setAdapter(this.mPhotoAlbumAdapter);
//            if (DraftBoxManager.getInstance().getDraftNoPicModelList().size() == 0) {
//                this.drafts_anrtv.setVisibility(8);
//                this.select_anrtv.setVisibility(8);
//                this.category_anrtv.setVisibility(0);
//                this.category_anrtv.setText(getResources().getString(2131297049));
//                this.draft_swap_icon_parent.setVisibility(8);
//                this.indicator_draft.setVisibility(8);
//                resetImageTextViewState(this.photo_swap_icon, 1.0f, true, this.photo_swap_icon_bottom, 0);
//                resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//                resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//                if (!this.isFromVideoActivity2) {
//                    this.curModuleIndex = 1;
//                    this.curNoDraftIndex = 1;
//                    return;
//                }
//                return;
//            }
//            this.drafts_anrtv.setVisibility(8);
//            this.select_anrtv.setVisibility(8);
//            this.category_anrtv.setVisibility(0);
//            this.category_anrtv.setText(getResources().getString(2131297049));
//            this.draft_swap_icon_parent.setVisibility(0);
//            this.indicator_draft.setVisibility(0);
//            resetImageTextViewState(this.draft_swap_icon, 0.3f, false, this.draft_swap_icon_bottom, 8);
//            resetImageTextViewState(this.photo_swap_icon, 1.0f, true, this.photo_swap_icon_bottom, 0);
//            resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//            resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//            if (!this.isFromVideoActivity2) {
//                this.curModuleIndex = 1;
//                this.curNoDraftIndex = 1;
//            }
//            resetData(null);
//        }
//    }
//
//    public void setCurModuleIndex(int index) {
//        switch (index) {
//            case 0:
//                this.gif_swap_icon_parent.performClick();
//                return;
//            case 1:
//                if (TextUtil.isValidate(this.pathList) && this.mPhotoAlbumAdapter != null) {
//                    for (AlbumPictureEntity entity : this.mAlbumPictureEntityList) {
//                        entity.selected = 0;
//                    }
//                    this.mPhotoAlbumAdapter.setCanAlphaShow(true);
//                    this.mPhotoAlbumAdapter.clearSelList();
//                    this.select_anrtv.setText(getResources().getString(2131296429));
//                }
//                this.photo_swap_icon_parent.performClick();
//                return;
//            case 2:
//                if (this.mVideoAlbumAdapter != null) {
//                    for (VideoAlbumEntity entity2 : this.mVideoAlbumEntityList) {
//                        entity2.setSelect(0);
//                    }
//                    this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                    this.mVideoAlbumAdapter.clearSelList();
//                }
//                this.video_swap_icon_parent.performClick();
//                return;
//            default:
//                return;
//        }
//    }
//
//    private void videoSelectEffect() {
//        resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//        resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//        resetImageTextViewState(this.video_swap_icon, 1.0f, true, this.video_swap_icon_bottom, 0);
//        bindTouchListener(this.video_swap_icon);
//        this.gif_swap_icon.setOnTouchListener(this.mOtherTouch);
//        this.photo_swap_icon.setOnTouchListener(this.mOtherTouch);
//        if (this.draft_swap_icon_parent.getVisibility() == 0) {
//            resetImageTextViewState(this.draft_swap_icon, 0.3f, false, this.draft_swap_icon_bottom, 8);
//            this.draft_swap_icon.setOnTouchListener(this.mOtherTouch);
//        }
//    }
//
//    private void photoSelectEffect() {
//        resetImageTextViewState(this.photo_swap_icon, 1.0f, true, this.photo_swap_icon_bottom, 0);
//        resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//        resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//        bindTouchListener(this.photo_swap_icon);
//        this.gif_swap_icon.setOnTouchListener(this.mOtherTouch);
//        this.video_swap_icon.setOnTouchListener(this.mOtherTouch);
//        if (this.draft_swap_icon_parent.getVisibility() == 0) {
//            resetImageTextViewState(this.draft_swap_icon, 0.3f, false, this.draft_swap_icon_bottom, 8);
//            this.draft_swap_icon.setOnTouchListener(this.mOtherTouch);
//        }
//    }
//
//    private void gifSelectEffect() {
//        resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//        resetImageTextViewState(this.gif_swap_icon, 1.0f, true, this.gif_swap_icon_bottom, 0);
//        resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//        bindTouchListener(this.gif_swap_icon);
//        this.video_swap_icon.setOnTouchListener(this.mOtherTouch);
//        this.photo_swap_icon.setOnTouchListener(this.mOtherTouch);
//        if (this.draft_swap_icon_parent.getVisibility() == 0) {
//            resetImageTextViewState(this.draft_swap_icon, 0.3f, false, this.draft_swap_icon_bottom, 8);
//            this.draft_swap_icon.setOnTouchListener(this.mOtherTouch);
//        }
//    }
//
//    public void setPathList(ArrayList<String> list) {
//        this.pathList = list;
//        if (this.mPhotoAlbumAdapter != null) {
//            this.mPhotoAlbumAdapter.setPathList(this.pathList);
//        }
//        if (this.mVideoAlbumAdapter != null) {
//            this.mVideoAlbumAdapter.setPathList(this.pathList);
//        }
//    }
//
//    public void setActivity(FragmentActivity activity) {
//        boolean z = false;
//        this.mActivity = activity;
//        if (this.mGifAlbumAdapter == null) {
//            this.mGifAlbumAdapter = new GifAlbumAdapter(getActivity(), this.mGifAlbumEntityList, this.comeFrom, this.mCurrentTimeStamp);
//        }
//        if (this.mVideoAlbumAdapter == null) {
//            this.mVideoAlbumAdapter = new VideoAlbumAdapter(getActivity(), this.mVideoAlbumEntityList, this.comeFrom, this.mCurrentTimeStamp);
//            this.mVideoAlbumAdapter.setDisplayAlbumView(this);
//            this.mVideoAlbumAdapter.setOnSelectStateChange(new OnSelectStateChange() {
//                public void onSelect(List<VideoAlbumEntity> list) {
//                    fl_multiple_select.setEnabled(true);
//                    mVideoAlbumAdapter.setCanAlphaShow(false);
//                    if (list.size() == 1) {
//                        fl_multiple_select.setBackgroundResource(2130838319);
//                    } else if (list.size() == 9) {
//                        mVideoAlbumAdapter.setCanAdd(false);
//                    }
//                }
//
//                public void onCancel(List<VideoAlbumEntity> list) {
//                    if (list.size() == 0) {
//                        fl_multiple_select.setBackgroundResource(2130838320);
//                        fl_multiple_select.setEnabled(false);
//                        mVideoAlbumAdapter.setCanAlphaShow(true);
//                    } else if (list.size() == 8) {
//                        mVideoAlbumAdapter.setCanAdd(true);
//                    }
//                }
//            });
//        }
//        if (this.mGridLayoutManager == null) {
//            this.mGridLayoutManager = new GridLayoutManager(getActivity(), 3, 1, false);
//        }
//        if (this.mVideoFolderAdapter == null) {
//            this.mVideoFolderAdapter = new VideoFolderAdapter(getActivity(), this.mVideoFolderEntityList);
//            VideoFolderAdapter videoFolderAdapter = this.mVideoFolderAdapter;
//            if (this.curModuleIndex == 1) {
//                z = true;
//            }
//            videoFolderAdapter.setPicType(z);
//            this.mVideoFolderAdapter.setOnFolderClickListener(this);
//        }
//        if (this.mPhotoAlbumAdapter == null) {
//            this.mPhotoAlbumAdapter = new PhotoAlbumAdapter(getActivity(), this.mAlbumPictureEntityList, this.mCurrentTimeStamp);
//            this.mPhotoAlbumAdapter.setOnSelectStateChange(new PhotoAlbumAdapter.OnSelectStateChange() {
//                public void onSelect(List<AlbumPictureEntity> list) {
//                    fl_multiple_select.setEnabled(true);
//                    mPhotoAlbumAdapter.setCanAlphaShow(false);
//                    if (list.size() == 1) {
//                        fl_multiple_select.setBackgroundResource(2130838319);
//                    } else if (list.size() == 9) {
//                        mPhotoAlbumAdapter.setCanAdd(false);
//                    }
//                }
//
//                public void onCancel(List<AlbumPictureEntity> list) {
//                    if (list.size() == 0) {
//                        mPhotoAlbumAdapter.setCanAlphaShow(true);
//                        fl_multiple_select.setBackgroundResource(2130838320);
//                        fl_multiple_select.setEnabled(false);
//                    } else if (list.size() == 8) {
//                        mPhotoAlbumAdapter.setCanAdd(true);
//                    }
//                }
//            });
//        }
//        if (!this.isFromVideoActivity2) {
//            if (this.comeFrom == 1 || this.comeFrom == 2) {
//                this.video_grid_recyclerview.setAdapter(this.mGifAlbumAdapter);
//            } else if (this.comeFrom == 0) {
//                this.video_grid_recyclerview.setAdapter(this.mVideoAlbumAdapter);
//            } else {
//                this.video_grid_recyclerview.setAdapter(this.mPhotoAlbumAdapter);
//            }
//        }
//        if (!App.isHaveDraftData()) {
//            this.draft_swap_icon_parent.setVisibility(8);
//            this.indicator_draft.setVisibility(8);
//        }
//        this.draftsFragment = DraftsFragment.getNewInstance();
//        Bundle bundle = new Bundle();
//        bundle.putString(ShareConstants.FEED_SOURCE_PARAM, "displayAlbumView");
//        this.draftsFragment.setArguments(bundle);
//        this.mActivity.getSupportFragmentManager().beginTransaction().replace(2131690253, this.draftsFragment, DraftsFragment.TAG).commit();
//        this.draftsFragment.setDraftsNeedDataCallback(this);
//    }
//
//    public void loadLocalData() {
//        canAdapterClick = true;
//        this.isReadData = true;
//        this.mActivity.getSupportLoaderManager().initLoader(0, null, this.mLoaderCallback);
//    }
//
//    public void singleVideoClick(VideoAlbumEntity entity) {
//        this.videosModels.clear();
//        if (entity != null) {
//            canAdapterClick = false;
//            getActivity().closeCamera();
//            this.videosModels.add(initLongVideoModel(entity, 0));
//            if (TextUtil.isValidate(this.pathList)) {
//                EventBus.getDefault().post(new DestroyActivityEvent(DestroyActivityEvent.ActivityState.LONGVIDEOLOCAL, this.videosModels));
//            } else {
//                startFilterActivity();
//            }
//        }
//    }
//
//    public void onEventMainThread(SelectedPhotoEvent event) {
//        if (this.selectedAlbumPictureEntity == null) {
//            this.selectedAlbumPictureEntity = new ArrayList();
//        }
//        int count = event.count;
//        AlbumPictureEntity entity = event.entity;
//        if (entity.getCurrentNumber() == 1) {
//            this.selectedAlbumPictureEntity.add(entity);
//        } else {
//            this.selectedAlbumPictureEntity.remove(entity);
//        }
//        int size = this.selectedAlbumPictureEntity.size();
//    }
//
//    public void onEventMainThread(DraftsClearedEvent event) {
//        this.draft_swap_icon.setVisibility(8);
//        onClick(this.gif_swap_icon);
//    }
//
//    public void onEventMainThread(ClickSimplePhotoEvent event) {
//        int i = 0;
//        final AlbumPictureEntity selectPictureEntity = event.entity;
//        String str = LOG_TAG;
//        StringBuilder append = new StringBuilder().append("ClickSimplePhotoEvent :    width :  ").append(selectPictureEntity != null ? selectPictureEntity.width : 0).append("  height :  ");
//        if (selectPictureEntity != null) {
//            i = selectPictureEntity.height;
//        }
//        BuglyLogUtil.writeKeyAndValueLog(str, append.append(i).toString());
//        if (!this.isPrepareToNext && selectPictureEntity != null) {
//            this.isPrepareToNext = true;
//            if (this.fromFilterActivity) {
//                String filePath = selectPictureEntity.getPath();
//                Bitmap picBitmap = VideoBitmapsModel.instancesModel().containsFile(filePath);
//                if (picBitmap == null) {
//                    picBitmap = BitmapUtil.getLocalBitmap(new File(filePath));
//                }
//                if (picBitmap != null) {
//                    VideoBitmapsModel.instancesModel().setBitmap(new BitmapInfo(filePath, picBitmap));
//                    if (getActivity() != null) {
//                        getActivity().closeCamera();
//                    }
//                    LongVideosModel model = new LongVideosModel(2, picBitmap.getWidth(), picBitmap.getHeight());
//                    model.setVideoPath(selectPictureEntity.getPath());
//                    ArrayList<LongVideosModel> mList = new ArrayList();
//                    mList.add(model);
//                    EventBus.getDefault().post(new DestroyActivityEvent(DestroyActivityEvent.ActivityState.LONGVIDEOBITMAP, mList));
//                    getActivity().onBackWhenFromFilterActivity();
//                }
//            } else if (!event.pathListValidate) {
//                final File file = new File(selectPictureEntity.getPath());
//                new Thread() {
//                    /* JADX WARNING: Removed duplicated region for block: B:42:0x020b  */
//                    /* JADX WARNING: Removed duplicated region for block: B:62:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
//                    public void run() {
//                        /*
//                        r36 = this;
//                        super.run();
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.getActivity();
//                        if (r30 == 0) goto L_0x001c;
//                    L_0x000f:
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.getActivity();
//                        r30.closeCamera();
//                    L_0x001c:
//                        r30 = com.blink.academy.onetake.bean.imageproperty.ImagePropertyBean.getInstance();	 Catch:{ IOException -> 0x016e }
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x016e }
//                        r31 = r0;
//                        r31 = r31.getPath();	 Catch:{ IOException -> 0x016e }
//                        r30.setImagePath(r31);	 Catch:{ IOException -> 0x016e }
//                    L_0x002d:
//                        r30 = com.blink.academy.onetake.bean.imageproperty.ImagePropertyBean.getInstance();
//                        r13 = r30.getImagePropertyObject();
//                        com.blink.academy.onetake.controller.MSCVController.postExifGetOfficialTag(r13);
//                        r0 = r36;
//                        r0 = r0;
//                        r30 = r0;
//                        r16 = r30.lastModified();
//                        r11 = new android.support.media.ExifInterface;	 Catch:{ IOException -> 0x018c }
//                        r0 = r36;
//                        r0 = r0;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r30 = r30.getAbsolutePath();	 Catch:{ IOException -> 0x018c }
//                        r0 = r30;
//                        r11.<init>(r0);	 Catch:{ IOException -> 0x018c }
//                        r30 = "GPSLatitude";
//                        r0 = r30;
//                        r21 = r11.getAttribute(r0);	 Catch:{ IOException -> 0x018c }
//                        r30 = "GPSLongitude";
//                        r0 = r30;
//                        r24 = r11.getAttribute(r0);	 Catch:{ IOException -> 0x018c }
//                        r30 = "GPSLatitudeRef";
//                        r0 = r30;
//                        r20 = r11.getAttribute(r0);	 Catch:{ IOException -> 0x018c }
//                        r30 = "GPSLongitudeRef";
//                        r0 = r30;
//                        r23 = r11.getAttribute(r0);	 Catch:{ IOException -> 0x018c }
//                        r30 = "Orientation";
//                        r0 = r30;
//                        r28 = r11.getAttribute(r0);	 Catch:{ IOException -> 0x018c }
//                        r29 = java.lang.Integer.parseInt(r28);	 Catch:{ IOException -> 0x018c }
//                        r30 = 1;
//                        r0 = r30;
//                        r1 = r29;
//                        if (r0 != r1) goto L_0x0174;
//                    L_0x0087:
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r31 = 0;
//                        r0 = r31;
//                        r1 = r30;
//                        r1.oritation = r0;	 Catch:{ IOException -> 0x018c }
//                    L_0x0095:
//                        if (r21 == 0) goto L_0x01fa;
//                    L_0x0097:
//                        if (r20 == 0) goto L_0x01fa;
//                    L_0x0099:
//                        if (r24 == 0) goto L_0x01fa;
//                    L_0x009b:
//                        if (r23 == 0) goto L_0x01fa;
//                    L_0x009d:
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r30 = r0;
//                        r0 = r30;
//                        r1 = r21;
//                        r2 = r20;
//                        r18 = r0.convertRationalLatLonToFloat(r1, r2);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r30 = r0;
//                        r0 = r30;
//                        r1 = r24;
//                        r2 = r23;
//                        r22 = r0.convertRationalLatLonToFloat(r1, r2);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r19 = new com.amap.api.services.core.LatLonPoint;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r0 = r18;
//                        r0 = (double) r0;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r30 = r0;
//                        r0 = r22;
//                        r0 = (double) r0;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r32 = r0;
//                        r0 = r19;
//                        r1 = r30;
//                        r3 = r32;
//                        r0.<init>(r1, r3);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r26 = java.lang.System.currentTimeMillis();	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r8 = r26 - r16;
//                        r30 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//                        r30 = r8 / r30;
//                        r32 = 10800; // 0x2a30 float:1.5134E-41 double:5.336E-320;
//                        r30 = (r30 > r32 ? 1 : (r30 == r32 ? 0 : -1));
//                        if (r30 > 0) goto L_0x01d8;
//                    L_0x00e2:
//                        r30 = com.blink.academy.onetake.support.manager.GlobalLocationManager.getInstance();	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r0 = r30;
//                        r1 = r19;
//                        r0.startLocation(r1);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                    L_0x00ed:
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r30 = r0;
//                        r31 = 1;
//                        r30.setHasLocation(r31);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                    L_0x00f8:
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r32 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
//                        r32 = r16 / r32;
//                        r0 = r30;
//                        r1 = r32;
//                        r0.mCreationDate = r1;
//                        r30 = "creation_date";
//                        r31 = "lastModifiedTime : %s , mCreationDate : %s ";
//                        r32 = 2;
//                        r0 = r32;
//                        r0 = new java.lang.Object[r0];
//                        r32 = r0;
//                        r33 = 0;
//                        r34 = java.lang.Long.valueOf(r16);
//                        r32[r33] = r34;
//                        r33 = 1;
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r34 = r0;
//                        r34 = r34.mCreationDate;
//                        r34 = java.lang.Long.valueOf(r34);
//                        r32[r33] = r34;
//                        r31 = java.lang.String.format(r31, r32);
//                        com.blink.academy.onetake.support.debug.LogUtil.d(r30, r31);
//                        r0 = r36;
//                        r0 = r7;
//                        r30 = r0;
//                        r30 = r30.getPath();
//                        r6 = android.graphics.BitmapFactory.decodeFile(r30);
//                        r7 = new com.blink.academy.onetake.VideoTools.PictureDecoder;
//                        r30 = 2;
//                        r0 = r36;
//                        r0 = r7;
//                        r31 = r0;
//                        r0 = r31;
//                        r0 = r0.oritation;
//                        r31 = r0;
//                        r0 = r30;
//                        r1 = r31;
//                        r7.<init>(r6, r0, r1);
//                        r12 = r7.getPictureFrames();
//                        com.blink.academy.onetake.support.utils.BitmapUtil.recycleBitmap(r6);
//                        r30 = com.blink.academy.onetake.bean.FramesHolder.getInstance();
//                        r0 = r30;
//                        r0.setFrames(r12);
//                        if (r12 != 0) goto L_0x020b;
//                    L_0x016d:
//                        return;
//                    L_0x016e:
//                        r10 = move-exception;
//                        r10.printStackTrace();
//                        goto L_0x002d;
//                    L_0x0174:
//                        r30 = 6;
//                        r0 = r30;
//                        r1 = r29;
//                        if (r0 != r1) goto L_0x01a8;
//                    L_0x017c:
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r31 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
//                        r0 = r31;
//                        r1 = r30;
//                        r1.oritation = r0;	 Catch:{ IOException -> 0x018c }
//                        goto L_0x0095;
//                    L_0x018c:
//                        r10 = move-exception;
//                        r30 = "PtrAlbumViewLayout";
//                        r0 = r30;
//                        com.blink.academy.onetake.support.utils.BuglyLogUtil.writeKeyAndValueThrowableLog(r0, r10);
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r0 = r36;
//                        r0 = r7;
//                        r31 = r0;
//                        r30.setNoGPSdata(r31);
//                        r10.printStackTrace();
//                        goto L_0x00f8;
//                    L_0x01a8:
//                        r30 = 8;
//                        r0 = r30;
//                        r1 = r29;
//                        if (r0 != r1) goto L_0x01c0;
//                    L_0x01b0:
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r31 = 90;
//                        r0 = r31;
//                        r1 = r30;
//                        r1.oritation = r0;	 Catch:{ IOException -> 0x018c }
//                        goto L_0x0095;
//                    L_0x01c0:
//                        r30 = 3;
//                        r0 = r30;
//                        r1 = r29;
//                        if (r0 != r1) goto L_0x0095;
//                    L_0x01c8:
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r31 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
//                        r0 = r31;
//                        r1 = r30;
//                        r1.oritation = r0;	 Catch:{ IOException -> 0x018c }
//                        goto L_0x0095;
//                    L_0x01d8:
//                        r30 = com.blink.academy.onetake.support.manager.GlobalLocationManager.getInstance();	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        r0 = r30;
//                        r1 = r19;
//                        r0.startLocationWithout(r1);	 Catch:{ IllegalArgumentException -> 0x01e5 }
//                        goto L_0x00ed;
//                    L_0x01e5:
//                        r10 = move-exception;
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r31 = r0;
//                        r30.setNoGPSdata(r31);	 Catch:{ IOException -> 0x018c }
//                        r10.printStackTrace();	 Catch:{ IOException -> 0x018c }
//                        goto L_0x00f8;
//                    L_0x01fa:
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;	 Catch:{ IOException -> 0x018c }
//                        r30 = r0;
//                        r0 = r36;
//                        r0 = r7;	 Catch:{ IOException -> 0x018c }
//                        r31 = r0;
//                        r30.setNoGPSdata(r31);	 Catch:{ IOException -> 0x018c }
//                        goto L_0x00f8;
//                    L_0x020b:
//                        r25 = new android.os.Bundle;
//                        r25.<init>();
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.comeFrom;
//                        r31 = 1;
//                        r0 = r30;
//                        r1 = r31;
//                        if (r0 == r1) goto L_0x0234;
//                    L_0x0222:
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.comeFrom;
//                        r31 = 2;
//                        r0 = r30;
//                        r1 = r31;
//                        if (r0 != r1) goto L_0x03af;
//                    L_0x0234:
//                        r15 = 1;
//                    L_0x0235:
//                        if (r15 == 0) goto L_0x03b2;
//                    L_0x0237:
//                        r14 = new android.content.Intent;
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.getActivity();
//                        r31 = com.blink.academy.onetake.ui.activity.video.SelectHeadPhotoActivity.class;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.<init>(r0, r1);
//                        r30 = "data_from";
//                        r31 = 2;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = com.blink.academy.onetake.ui.activity.video.SelectHeadPhotoActivity.SELECT_PICTURE_PATH;
//                        r0 = r36;
//                        r0 = r7;
//                        r31 = r0;
//                        r31 = r31.getPath();
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                    L_0x026a:
//                        r0 = r12.mCaptureWidth;
//                        r30 = r0;
//                        r0 = r12.mCaptureHeight;
//                        r31 = r0;
//                        r0 = r30;
//                        r1 = r31;
//                        if (r0 <= r1) goto L_0x03d4;
//                    L_0x0278:
//                        r28 = 90;
//                    L_0x027a:
//                        r30 = "OrientationIntent";
//                        r0 = r30;
//                        r1 = r28;
//                        r14.putExtra(r0, r1);
//                        r30 = "TransformIntent";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.comeFrom;
//                        r31 = 1;
//                        r0 = r30;
//                        r1 = r31;
//                        if (r0 != r1) goto L_0x02d1;
//                    L_0x02a0:
//                        r30 = "ActivityFromBundle";
//                        r31 = "FromMeAvatar";
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                        r30 = "FromUserNameBundle";
//                        r31 = com.blink.academy.onetake.support.helper.GlobalHelper.getUserScreenName();
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                        r30 = "FromCurrentTimeStampBundle";
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r31 = r0;
//                        r31 = r31.mCurrentTimeStamp;
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                    L_0x02d1:
//                        r30 = "HasVideoLocationContent";
//                        r0 = r36;
//                        r0 = r7;
//                        r31 = r0;
//                        r31 = r31.isHasLocation();
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putBoolean(r1, r2);
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.comeFrom;
//                        r31 = 2;
//                        r0 = r30;
//                        r1 = r31;
//                        if (r0 != r1) goto L_0x0329;
//                    L_0x02f8:
//                        r30 = "ActivityFromBundle";
//                        r31 = "FromAvatar";
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                        r30 = "FromUserNameBundle";
//                        r31 = com.blink.academy.onetake.support.helper.GlobalHelper.getUserScreenName();
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                        r30 = "FromCurrentTimeStampBundle";
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r31 = r0;
//                        r31 = r31.mCurrentTimeStamp;
//                        r0 = r25;
//                        r1 = r30;
//                        r2 = r31;
//                        r0.putString(r1, r2);
//                    L_0x0329:
//                        r30 = "data_type";
//                        r31 = 1;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "BundleIntent";
//                        r0 = r30;
//                        r1 = r25;
//                        r14.putExtra(r0, r1);
//                        r30 = "BeautyIntent";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "SeilfieIntent";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "FPS_INTENT";
//                        r31 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "IS_SQUARE_INTENT";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "VIDEO_ROTATION";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "OffsetXIntent";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "OffsetYIntent";
//                        r31 = 0;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        r30 = "intent_key_creation_date";
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r31 = r0;
//                        r32 = r31.mCreationDate;
//                        r0 = r30;
//                        r1 = r32;
//                        r14.putExtra(r0, r1);
//                        r30 = "startFilter";
//                        r31 = "child thread";
//                        com.blink.academy.onetake.support.debug.LogUtil.e(r30, r31);
//                        r0 = r36;
//                        r30 = com.blink.academy.onetake.widgets.DisplayAlbumView$8$.Lambda.1.lambdaFactory$(r0, r14);
//                        com.blink.academy.onetake.App.runOnUiThread(r30);
//                        goto L_0x016d;
//                    L_0x03af:
//                        r15 = 0;
//                        goto L_0x0235;
//                    L_0x03b2:
//                        r14 = new android.content.Intent;
//                        r0 = r36;
//                        r0 = com.blink.academy.onetake.widgets.DisplayAlbumView.this;
//                        r30 = r0;
//                        r30 = r30.getActivity();
//                        r31 = com.blink.academy.onetake.ui.activity.video.FilterActivity.class;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.<init>(r0, r1);
//                        r30 = "data_from";
//                        r31 = 1;
//                        r0 = r30;
//                        r1 = r31;
//                        r14.putExtra(r0, r1);
//                        goto L_0x026a;
//                    L_0x03d4:
//                        r28 = 0;
//                        goto L_0x027a;
//                        */
//                        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.widgets.DisplayAlbumView.8.run():void");
//                    }
//
//                    private /* synthetic */ void lambda$run$0(Intent intent) {
//                        LogUtil.e("startFilter", "main thread");
//                        lockData = false;
//                        toFilter = true;
//                        isPrepareToNext = true;
//                        mPhotoAlbumAdapter.setShowAlpha(false);
//                        getActivity().startActivity(intent);
//                    }
//                }.start();
//            }
//        }
//    }
//
//    private void setNoGPSdata(AlbumPictureEntity selectPictureEntity) {
//        GlobalLocationManager.getInstance().startLocationWithout(null);
//        selectPictureEntity.setHasLocation(false);
//        MscvModel.getInstance().setGpsRecTagList(new ArrayList());
//        EventBus.getDefault().post(new OfficialTagListEvent(-2));
//    }
//
//    public void onEventMainThread(DestroyActivityEvent event) {
//    }
//
//    private float convertRationalLatLonToFloat(String rationalString, String ref) {
//        String[] parts = rationalString.split(",");
//        String[] pair = parts[0].split("/");
//        double degrees = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
//        pair = parts[1].split("/");
//        double minutes = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
//        pair = parts[2].split("/");
//        double result = ((minutes / 60.0d) + degrees) + ((Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim())) / 3600.0d);
//        if (!ref.equals("S")) {
//            if (!ref.equals("W")) {
//                return (float) result;
//            }
//        }
//        return (float) (-result);
//    }
//
//    public void release() {
//        App.UnregisterEventBus(this);
//    }
//
//    public void onEventMainThread(DraftChangeEvent event) {
//        if (DraftBoxManager.getInstance().getDraftNoPicModelList().size() <= 0) {
//            changeDraftCancelToSelectQuickly();
//            if (this.draft_swap_icon_parent.getVisibility() != 8) {
//                App.setHaveDraftData(false);
//                this.draft_swap_icon_parent.setVisibility(8);
//                this.indicator_draft.setVisibility(8);
//                setCurModuleIndex(this.curNoDraftIndex);
//            }
//        } else if (this.draft_swap_icon_parent.getVisibility() != 0) {
//            resetImageTextViewState(this.draft_swap_icon, 0.3f, false, this.draft_swap_icon_bottom, 8);
//            this.draft_swap_icon.setOnTouchListener(this.mOtherTouch);
//            this.indicator_draft.setVisibility(0);
//            this.draft_swap_icon_parent.setVisibility(0);
//            App.setHaveDraftData(true);
//        }
//    }
//
//    public void onActivityResume() {
//        this.toFilter = false;
//        this.mPhotoAlbumAdapter.notifyDataSetChanged();
//        this.mVideoAlbumAdapter.notifyDataSetChanged();
//        this.mGifAlbumAdapter.notifyDataSetChanged();
//        if (!this.lockData) {
//            this.isPrepareToNext = false;
//        }
//        this.lockData = false;
//    }
//
//    private void showFolderAnimation() {
//        if (this.folder_grid_recyclerview.getVisibility() != 0) {
//            if (this.isFirstShowFolderAnim) {
//                switch (this.curModuleIndex) {
//                    case 0:
//                        resetFolderList(this.mOnlyGifFolderList);
//                        break;
//                    case 1:
//                        resetFolderList(this.mOnlyPictureFolderList);
//                        break;
//                    case 2:
//                        resetFolderList(this.mOnlyVideoFolderList);
//                        break;
//                }
//                this.mVideoFolderAdapter.notifyDataSetChanged();
//                this.isFirstShowFolderAnim = false;
//            }
//            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, StaticLayoutUtil.DefaultSpacingadd});
//            PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", new float[]{StaticLayoutUtil.DefaultSpacingadd, 180.0f});
//            PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", new float[]{(float) (DensityUtil.getMetricsHeight(getActivity()) - DensityUtil.dip2px(60.0f)), StaticLayoutUtil.DefaultSpacingadd});
//            ObjectAnimator iconAnimator = ObjectAnimator.ofPropertyValuesHolder(this.icon_iv, new PropertyValuesHolder[]{rotation});
//            ObjectAnimator.ofPropertyValuesHolder(this.folder_grid_recyclerview, new PropertyValuesHolder[]{translationY}).addListener(new AnimatorListenerAdapter() {
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    folder_grid_recyclerview.setVisibility(0);
//                }
//
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                }
//            });
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.setDuration(200);
//            this.folder_grid_recyclerview.clearAnimation();
//            animatorSet.playTogether(new Animator[]{folderGridAnimator, iconAnimator});
//            animatorSet.start();
//        }
//    }
//
//    private void resetFolderList(List<VideoFolderEntity> list) {
//        boolean b = true;
//        VideoFolderEntity firstEntity = null;
//        VideoFolderEntity secondEntity = null;
//        if (TextUtil.isValidate(this.mVideoFolderEntityList)) {
//            firstEntity = (VideoFolderEntity) this.mVideoFolderEntityList.get(0);
//            if (!(firstEntity == null || firstEntity.getVideoAlbumEntityList() == null)) {
//                firstEntity.setVideoCount(firstEntity.getVideoAlbumEntityList().size());
//            }
//            if (this.mVideoFolderEntityList.size() > 1) {
//                secondEntity = (VideoFolderEntity) this.mVideoFolderEntityList.get(1);
//                if (!"Camera".equals(secondEntity.getFolderName())) {
//                    secondEntity = null;
//                }
//            }
//        }
//        this.mVideoFolderEntityList.clear();
//        if (firstEntity != null) {
//            this.mVideoFolderEntityList.add(firstEntity);
//        }
//        if (secondEntity != null) {
//            this.mVideoFolderEntityList.add(secondEntity);
//        }
//        this.mVideoFolderEntityList.addAll(list);
//        if (this.curModuleIndex != 1) {
//            b = false;
//        }
//        this.mVideoFolderAdapter.setPicType(b);
//        if (TextUtil.isValidate(this.mVideoFolderEntityList)) {
//            VideoFolderEntity entity;
//            int count = 0;
//            for (int i = 1; i < this.mVideoFolderEntityList.size(); i++) {
//                entity = (VideoFolderEntity) this.mVideoFolderEntityList.get(i);
//                if (entity != null) {
//                    if (b) {
//                        count += entity.pictureCount;
//                    } else {
//                        count += entity.getVideoCount();
//                    }
//                }
//            }
//            if (count > 0 && TextUtil.isValidate(this.mVideoFolderEntityList)) {
//                entity = (VideoFolderEntity) this.mVideoFolderEntityList.get(0);
//                if (entity != null) {
//                    entity.setVideoCount(count);
//                }
//            }
//        }
//    }
//
//    private void hideFolderAnimation(final OnHideAnimationListener listener) {
//        if (this.folder_grid_recyclerview.getVisibility() != 8) {
//            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{StaticLayoutUtil.DefaultSpacingadd, 1.0f});
//            PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", new float[]{180.0f, 360.0f});
//            PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", new float[]{StaticLayoutUtil.DefaultSpacingadd, (float) (DensityUtil.getMetricsHeight(getActivity()) - DensityUtil.dip2px(60.0f))});
//            ObjectAnimator iconAnimator = ObjectAnimator.ofPropertyValuesHolder(this.icon_iv, new PropertyValuesHolder[]{rotation});
//            ObjectAnimator.ofPropertyValuesHolder(this.folder_grid_recyclerview, new PropertyValuesHolder[]{translationY}).addListener(new AnimatorListenerAdapter() {
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                }
//
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    if (listener != null) {
//                        listener.hide();
//                    }
//                    folder_grid_recyclerview.setVisibility(8);
//                }
//            });
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.setDuration(200);
//            this.folder_grid_recyclerview.clearAnimation();
//            animatorSet.playTogether(new Animator[]{folderGridAnimator, iconAnimator});
//            animatorSet.start();
//        }
//    }
//
//    private void clearDraftsSelect(boolean hide) {
//        if (hide) {
//            this.select_anrtv.setVisibility(8);
//        }
//        if (getResources().getString(2131296430).equals((String) this.select_anrtv.getText())) {
//            changeDraftCancelToSelectQuickly();
//        }
//    }
//
//    private void changeDraftSelectToCancel() {
//        Fragment fragmentByTag = getActivity().getSupportFragmentManager().findFragmentByTag(DraftsFragment.TAG);
//        if (fragmentByTag != null && (fragmentByTag instanceof DraftsFragment)) {
//            ((DraftsFragment) fragmentByTag).enterSelectMode();
//        }
//        changeDraftSelectTextToCancel();
//    }
//
//    public void changeDraftSelectTextToCancel() {
//        AnimationUtil.alphaAnimate(this.select_anrtv, StaticLayoutUtil.DefaultSpacingadd, 100, new AnimatorEndListener() {
//            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                select_anrtv.setText(2131296430);
//                AnimationUtil.alphaAnimate(select_anrtv, 1.0f, 100, null);
//            }
//        });
//    }
//
//    private void changeDraftCancelToSelect() {
//        Fragment fragmentByTag = getActivity().getSupportFragmentManager().findFragmentByTag(DraftsFragment.TAG);
//        if (fragmentByTag != null && (fragmentByTag instanceof DraftsFragment)) {
//            ((DraftsFragment) fragmentByTag).cancelSelectMode();
//        }
//        changeDraftCancelTextToSelect();
//    }
//
//    private void changeDraftCancelToSelectQuickly() {
//        Fragment fragmentByTag = getActivity().getSupportFragmentManager().findFragmentByTag(DraftsFragment.TAG);
//        if (fragmentByTag != null && (fragmentByTag instanceof DraftsFragment)) {
//            ((DraftsFragment) fragmentByTag).cancelSelectModeQuickly();
//        }
//        changeDraftCancelTextToSelectQuickly();
//    }
//
//    public void changeDraftCancelTextToSelectQuickly() {
//        this.select_anrtv.setText(2131296429);
//    }
//
//    public void changeDraftCancelTextToSelect() {
//        AnimationUtil.alphaAnimate(this.select_anrtv, StaticLayoutUtil.DefaultSpacingadd, 100, new AnimatorEndListener() {
//            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                select_anrtv.setText(2131296429);
//                AnimationUtil.alphaAnimate(select_anrtv, 1.0f, 100, null);
//            }
//        });
//    }
//
//    public void onActivityPause() {
//        if (this.isPrepareToNext && !this.toFilter) {
//            this.lockData = true;
//        }
//        EventBus.getDefault().post(new DismissEntranceEvent());
//    }
//
//    public void onActivityDestroy() {
//        if (this.mPhotoAlbumAdapter != null) {
//            this.mPhotoAlbumAdapter.onDestroy();
//        }
//    }
//
//    private LongVideosModel initLongVideoModel(VideoAlbumEntity entity, int position) {
//        String videoPath = entity.getVideoPath();
//        LongVideosModel model = new LongVideosModel(0, videoPath, entity.getVideoDuration());
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        if (TextUtil.isValidate(videoPath)) {
//            try {
//                mmr.setDataSource(videoPath);
//                String sWidth = mmr.extractMetadata(18);
//                String sHeight = mmr.extractMetadata(19);
//                String sRotation = mmr.extractMetadata(24);
//                int videoWidth = 0;
//                int videoHeight = 0;
//                int tmp;
//                if (TextUtil.isNull(sWidth) || TextUtil.isNull(sHeight)) {
//                    MediaFormat format = cn.nineton.onetake.media.MediaUtils.getFormat(videoPath);
//                    if (format != null && format.containsKey("width") && format.containsKey("height")) {
//                        videoWidth = format.getInteger("width");
//                        videoHeight = format.getInteger("height");
//                        if (format.containsKey("rotation-degrees") && VERSION.SDK_INT >= 23) {
//                            int rotation = format.getInteger("rotation-degrees");
//                            if (rotation == 90 || rotation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
//                                tmp = videoWidth;
//                                videoWidth = videoHeight;
//                                videoHeight = tmp;
//                            }
//                        }
//                    }
//                } else {
//                    videoWidth = Integer.parseInt(sWidth);
//                    videoHeight = Integer.parseInt(sHeight);
//                    if (sRotation != null && ("90".equals(sRotation) || "270".equals(sRotation))) {
//                        tmp = videoWidth;
//                        videoWidth = videoHeight;
//                        videoHeight = tmp;
//                    }
//                }
//                model.setVideoWidth(videoWidth);
//                model.setVideoHeight(videoHeight);
//                mmr.release();
//                if (position == 0) {
//                    getFirstVideoWidthHeight(videoWidth, videoHeight);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                BuglyLogUtil.writeKeyAndValueLog("GetVideoInfo", e.getMessage());
//            }
//        }
//        return model;
//    }
//
//    private void getFirstVideoWidthHeight(int width, int height) {
//        this.intentVideoWidth = width;
//        this.intentVideoHeight = height;
//    }
//
//    private boolean getVideosData() {
//        this.videosModels.clear();
//        List<VideoAlbumEntity> beans = this.mVideoAlbumAdapter.getSelectVideoBeans();
//        if (beans == null || beans.size() <= 0) {
//            return false;
//        }
//        int size = beans.size();
//        for (int i = 0; i < size; i++) {
//            this.videosModels.add(initLongVideoModel((VideoAlbumEntity) beans.get(i), i));
//        }
//        return true;
//    }
//
//    private void startFilterActivity() {
//        Intent intent = new Intent(getActivity(), FilterActivity.class);
//        intent.putExtra(FilterActivity.ORIENTATION_INTENT, 0);
//        intent.putExtra(FilterActivity.DATA_FROM, 1);
//        intent.putExtra(FilterActivity.DATA_TYPE, 2);
//        DraftModel mCurDraftModel = new DraftModel(String.valueOf(new Date().getTime()));
//        FilterModel filterModel = new FilterModel();
//        filterModel.setFilterName(getFilterName());
//        mCurDraftModel.setFilterModel(filterModel);
//        EventBus.getDefault().postSticky(new PublishDraftEvent(mCurDraftModel));
//        intent.putExtra(FilterActivity.OFFSETX_INTENT, StaticLayoutUtil.DefaultSpacingadd);
//        intent.putExtra(FilterActivity.OFFSETY_INTENT, StaticLayoutUtil.DefaultSpacingadd);
//        intent.putParcelableArrayListExtra(FilterActivity.LONG_VIDEO_MODELS, this.videosModels);
//        intent.putExtra(FilterActivity.VIDEO_WIDTH_INTENT, this.intentVideoWidth);
//        String activityFr = "";
//        if (this.comeFrom == 1) {
//            activityFr = VideoActivity2.FromMeAvatar;
//        } else if (this.comeFrom == 2) {
//            activityFr = VideoActivity2.FromAvatar;
//        }
//        intent.putExtra(VideoActivity2.ActivityFromBundle, activityFr);
//        intent.putExtra(FilterActivity.VIDEO_HEIGHT_INTENT, this.intentVideoHeight);
//        getActivity().startActivity(intent);
//    }
//
//    private String getFilterName() {
//        return getActivity().getFilterName();
//    }
//
//    public void onClick(final View v) {
//        int lastIndex = this.curModuleIndex;
//        switch (v.getId()) {
//            case 2131690000:
//                if (v.isEnabled() && !this.isNoPicAndVideo) {
//                    if (this.folder_grid_recyclerview.getVisibility() == 0) {
//                        hideFolderAnimation(null);
//                        return;
//                    } else {
//                        showFolderAnimation();
//                        return;
//                    }
//                }
//                return;
//            case 2131690242:
//                this.currentState = 0;
//                this.video_grid_container.setVisibility(0);
//                videoSelectEffect();
//                this.drafts_container.setVisibility(8);
//                changeDraftCancelToSelectQuickly();
//                if (2 != this.curModuleIndex) {
//                    this.select_anrtv.setVisibility(8);
//                    this.curModuleIndex = 2;
//                    this.curNoDraftIndex = 2;
//                    SharedPrefUtils.putInt(Constants.TakePictureOrOther, this.curModuleIndex);
//                    resetFolderList(this.mOnlyVideoFolderList);
//                    if (this.fl_multiple_select.getVisibility() != 0) {
//                        showMultiConfirmIv();
//                        this.mHandler.postDelayed(new Runnable() {
//                            public void run() {
//                                mVideoFolderAdapter.notifyDataSetChanged();
//                                checkReSelectAllFolder(2);
//                                checkIsNoVideoOrPhoto(true);
//                                resetData(v);
//                                clearDraftsSelect(false);
//                            }
//                        }, 200);
//                        return;
//                    }
//                    this.mVideoFolderAdapter.notifyDataSetChanged();
//                    checkReSelectAllFolder(2);
//                    checkIsNoVideoOrPhoto(true);
//                    resetData(v);
//                    clearDraftsSelect(false);
//                    return;
//                }
//                return;
//            case 2131690244:
//                this.currentState = 0;
//                this.video_grid_container.setVisibility(0);
//                photoSelectEffect();
//                this.drafts_container.setVisibility(8);
//                changeDraftCancelToSelectQuickly();
//                if (1 != this.curModuleIndex) {
//                    boolean z;
//                    if (TextUtil.isValidate(this.pathList)) {
//                        if (this.fl_multiple_select.getVisibility() != 0) {
//                            showMultiConfirmIv();
//                        }
//                    } else if (this.fl_multiple_select.getVisibility() == 0) {
//                        hideMultiConfirmIv();
//                    }
//                    if (lastIndex == 2 && this.mVideoAlbumAdapter != null) {
//                        for (VideoAlbumEntity entity : this.mVideoAlbumEntityList) {
//                            if (entity != null) {
//                                entity.setSelect(0);
//                            }
//                        }
//                        this.mVideoAlbumAdapter.clearSelList();
//                        this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                        this.mVideoAlbumAdapter.notifyDataSetChanged();
//                    }
//                    this.select_anrtv.setVisibility(8);
//                    this.curModuleIndex = 1;
//                    this.curNoDraftIndex = 1;
//                    SharedPrefUtils.putInt(Constants.TakePictureOrOther, this.curModuleIndex);
//                    resetFolderList(this.mOnlyPictureFolderList);
//                    this.mVideoFolderAdapter.notifyDataSetChanged();
//                    checkReSelectAllFolder(1);
//                    checkIsNoVideoOrPhoto(true);
//                    if (!this.isPrepareToNext) {
//                        this.nextCache.clear();
//                    }
//                    switch_adapter(v);
//                    this.loading_cpb.setVisibility(8);
//                    if (TextUtil.isValidate(this.pathList)) {
//                        z = false;
//                    } else {
//                        z = true;
//                    }
//                    clearDraftsSelect(z);
//                    return;
//                }
//                return;
//            case 2131690246:
//                if (this.comeFrom != 4) {
//                    this.currentState = 0;
//                    this.video_grid_container.setVisibility(0);
//                    gifSelectEffect();
//                    changeDraftCancelToSelectQuickly();
//                    this.drafts_container.setVisibility(8);
//                    if (this.curModuleIndex != 0) {
//                        if (this.fl_multiple_select.getVisibility() == 0) {
//                            hideMultiConfirmIv();
//                        }
//                        if (lastIndex == 1 && TextUtil.isValidate(this.pathList) && this.mPhotoAlbumAdapter != null) {
//                            for (AlbumPictureEntity entity2 : this.mAlbumPictureEntityList) {
//                                entity2.setState(0);
//                            }
//                            this.mPhotoAlbumAdapter.setCanAlphaShow(true);
//                            this.mPhotoAlbumAdapter.notifyDataSetChanged();
//                        }
//                        if (lastIndex == 2 && this.mVideoAlbumAdapter != null) {
//                            for (VideoAlbumEntity entity3 : this.mVideoAlbumEntityList) {
//                                if (entity3 != null) {
//                                    entity3.setSelect(0);
//                                }
//                            }
//                            this.mVideoAlbumAdapter.clearSelList();
//                            this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                            this.mVideoAlbumAdapter.notifyDataSetChanged();
//                        }
//                        this.select_anrtv.setVisibility(8);
//                        this.curModuleIndex = 0;
//                        SharedPrefUtils.putInt(Constants.TakePictureOrOther, this.curModuleIndex);
//                        resetFolderList(this.mOnlyGifFolderList);
//                        this.mVideoFolderAdapter.notifyDataSetChanged();
//                        checkReSelectAllFolder(0);
//                        checkIsNoVideoOrPhoto(true);
//                        resetData(v);
//                        clearDraftsSelect(true);
//                        return;
//                    }
//                    return;
//                }
//                return;
//            case 2131690250:
//                this.currentState = 1;
//                if (4 == this.curModuleIndex) {
//                    resetImageTextViewState(this.draft_swap_icon, 1.0f, true, this.draft_swap_icon_bottom, 0);
//                    return;
//                }
//                if (this.fl_multiple_select.getVisibility() == 0) {
//                    hideMultiConfirmIv();
//                }
//                this.curModuleIndex = 4;
//                checkIsNoVideoOrPhoto(true);
//                this.video_grid_container.setVisibility(8);
//                this.drafts_container.setVisibility(0);
//                resetImageTextViewState(this.draft_swap_icon, 1.0f, true, this.draft_swap_icon_bottom, 0);
//                resetImageTextViewState(this.photo_swap_icon, 0.3f, false, this.photo_swap_icon_bottom, 8);
//                resetImageTextViewState(this.gif_swap_icon, 0.3f, false, this.gif_swap_icon_bottom, 8);
//                resetImageTextViewState(this.video_swap_icon, 0.3f, false, this.video_swap_icon_bottom, 8);
//                bindTouchListener(this.draft_swap_icon);
//                this.gif_swap_icon.setOnTouchListener(this.mOtherTouch);
//                this.photo_swap_icon.setOnTouchListener(this.mOtherTouch);
//                this.video_swap_icon.setOnTouchListener(this.mOtherTouch);
//                this.select_anrtv.setVisibility(0);
//                if (lastIndex == 1 && TextUtil.isValidate(this.pathList) && this.mPhotoAlbumAdapter != null) {
//                    for (AlbumPictureEntity entity22 : this.mAlbumPictureEntityList) {
//                        entity22.setState(0);
//                    }
//                    this.mPhotoAlbumAdapter.setCanAlphaShow(true);
//                    this.mPhotoAlbumAdapter.notifyDataSetChanged();
//                }
//                if (lastIndex == 2 && this.mVideoAlbumAdapter != null) {
//                    for (VideoAlbumEntity entity32 : this.mVideoAlbumEntityList) {
//                        if (entity32 != null) {
//                            entity32.setSelect(0);
//                        }
//                    }
//                    this.mVideoAlbumAdapter.clearSelList();
//                    this.mVideoAlbumAdapter.setCanAlphaShow(true);
//                    this.mVideoAlbumAdapter.notifyDataSetChanged();
//                }
//                this.select_anrtv.setText(getResources().getString(2131296429));
//                resetData(null);
//                return;
//            case R.id.fl_multiple_select://2131690256:
//                //getActivity().closeCamera();
//                setMultipleConfirmLoading(true);
//                if (!TextUtil.isValidate(this.pathList)) {
//                    PriorityThreadPoolManager.execute(new PriorityRunnable(8) {
//                        public void run() {
//                            if (getVideosData()) {
//                                mHandler.sendEmptyMessage(1);
//                            }
//                        }
//                    });
//                    return;
//                } else if (this.curModuleIndex == 2) {
//                    PriorityThreadPoolManager.execute(new PriorityRunnable(8) {
//                        public void run() {
//                            if (getVideosData()) {
//                                mHandler.sendEmptyMessage(2);
//                            }
//                        }
//                    });
//                    return;
//                } else {
//                    PriorityThreadPoolManager.execute(new PriorityRunnable(8) {
//                        public void run() {
//                            List<AlbumPictureEntity> beans = mPhotoAlbumAdapter.getSelectPictureBeans();
//                            ArrayList mList = new ArrayList();
//                            for (AlbumPictureEntity entity : beans) {
//                                String filePath = entity.getPath();
//                                Bitmap picBitmap = BitmapUtil.getLocalBitmap(new File(filePath));
//                                VideoBitmapsModel.instancesModel().setBitmap(new BitmapInfo(filePath, picBitmap));
//                                LongVideosModel model = new LongVideosModel(2, picBitmap.getWidth(), picBitmap.getHeight());
//                                model.setVideoPath(entity.getPath());
//                                model.setVideoWidth(picBitmap.getWidth());
//                                model.setVideoHeight(picBitmap.getHeight());
//                                mList.add(model);
//                            }
//                            EventBus.getDefault().post(new DestroyActivityEvent(ActivityState.LONGVIDEOBITMAP, mList));
//                            mHandler.sendEmptyMessage(3);
//                        }
//                    });
//                    return;
//                }
//            default:
//                return;
//        }
//    }
//
//    private void checkReSelectAllFolder(int type) {
//        String titleStr = getResources().getString(2131297049);
//        this.category_anrtv.setText(titleStr);
//        if (TextUtil.isValidate(this.mVideoFolderEntityList)) {
//            List<VideoFolderEntity> list = new ArrayList();
//            try {
//                list.addAll(this.mVideoFolderEntityList);
//            } catch (Exception e) {
//            }
//            if (TextUtil.isValidate(list)) {
//                switch (type) {
//                    case 0:
//                        if (this.curModuleIndex == 0) {
//                            for (VideoFolderEntity entity : list) {
//                                if (titleStr.equals(entity.folderName)) {
//                                    this.mGifAlbumEntityList.clear();
//                                    this.mGifAlbumEntityList.add(new VideoAlbumEntity((int) Item.PICTURE_SYNTHESIS_ENTER_TYPE));
//                                    if (entity.getVideoAlbumEntityList() != null) {
//                                        for (VideoAlbumEntity e2 : entity.getVideoAlbumEntityList()) {
//                                            if (e2.getVideoDuration() >= Constants.VIDEO_BLACK_DURATION) {
//                                                this.mGifAlbumEntityList.add(e2);
//                                            }
//                                        }
//                                        this.mGifAlbumAdapter.notifyDataSetChanged();
//                                        return;
//                                    }
//                                    return;
//                                }
//                            }
//                            return;
//                        }
//                        return;
//                    case 1:
//                        if (this.curModuleIndex == 1) {
//                            for (VideoFolderEntity entity2 : list) {
//                                if (titleStr.equals(entity2.folderName)) {
//                                    this.mAlbumPictureEntityList.clear();
//                                    if (entity2.albumPictureEntities == null) {
//                                        App.runOnUiThread(new Runnable() {
//                                            public void run() {
//                                                if (loading_cpb != null) {
//                                                    loading_cpb.setVisibility(0);
//                                                }
//                                            }
//                                        });
//                                        return;
//                                    }
//                                    this.mAlbumPictureEntityList.addAll(entity2.albumPictureEntities);
//                                    this.mPhotoAlbumAdapter.notifyDataSetChanged();
//                                    return;
//                                }
//                            }
//                            return;
//                        }
//                        return;
//                    case 2:
//                        if (this.curModuleIndex == 2) {
//                            for (VideoFolderEntity entity22 : list) {
//                                if (titleStr.equals(entity22.folderName)) {
//                                    this.mVideoAlbumEntityList.clear();
//                                    if (entity22.getVideoAlbumEntityList() != null) {
//                                        this.mVideoAlbumEntityList.addAll(entity22.getVideoAlbumEntityList());
//                                        this.mVideoAlbumAdapter.notifyDataSetChanged();
//                                        return;
//                                    }
//                                    return;
//                                }
//                            }
//                            return;
//                        }
//                        return;
//                    default:
//                        return;
//                }
//            }
//        }
//    }
//
//    private void setMultipleConfirmLoading(boolean loading) {
//        if (loading) {
//            this.multiple_loading.setVisibility(0);
//            this.multiple_select_confirm.setVisibility(8);
//            this.fl_multiple_select.setEnabled(false);
//            return;
//        }
//        this.mHandler.postDelayed(new Runnable() {
//            public void run() {
//                fl_multiple_select.setEnabled(true);
//                multiple_loading.setVisibility(8);
//                multiple_select_confirm.setVisibility(0);
//            }
//        }, 1000);
//    }
//
//    public int getHeadViewHeight() {
//        if (this.pull_refresh_pcfl != null) {
//            return this.pull_refresh_pcfl.getHeadViewHeight();
//        }
//        return 0;
//    }
//
//    public void changeSelectViewToSelected() {
//        changeDraftSelectTextToCancel();
//    }
//
//    private void onLoaderFinish() {
//        checkIsNoVideoOrPhoto();
//        this.loading_cpb.setVisibility(8);
//        if (!(this.curModuleIndex == 1 || this.category_anrtv == null)) {
//            String s = this.category_anrtv.getText().toString();
//            if (getResources().getString(2131297049).equalsIgnoreCase(s)) {
//                this.mGifAlbumAdapter.notifyDataSetChanged();
//                this.mVideoAlbumAdapter.notifyDataSetChanged();
//            } else if (TextUtil.isValidate(this.mVideoFolderEntityList)) {
//                for (VideoFolderEntity e : this.mVideoFolderEntityList) {
//                    if (s.equalsIgnoreCase(e.getFolderName())) {
//                        refreshFolderData(e);
//                    }
//                }
//            }
//        }
//        getActivity().getSupportLoaderManager().destroyLoader(0);
//        App.runOnUiThread(new Runnable() {
//            public void run() {
//                if (loading_cpb != null && curModuleIndex == 1 && !TextUtil.isValidate(mAlbumPictureEntityList)) {
//                    loading_cpb.setVisibility(0);
//                }
//            }
//        });
//        getActivity().getSupportLoaderManager().destroyLoader(2);
//        getActivity().getSupportLoaderManager().initLoader(2, null, this.mPictureLoaderCallback);
//    }
//
//    private VideoActivity2 getActivity() {
//        return (VideoActivity2) this.mActivity;
//    }
//
//    private boolean isWidthAndHeightIsValid(int width, int height) {
//        int needWidth = DensityUtil.getMetricsWidth(getContext()) / 3;
//        if (width == 0 || height == 0 || ((int) ((((float) (needWidth * height)) * 1.0f) / ((float) width))) >= 4000) {
//            return false;
//        }
//        return true;
//    }
//
//    private void onPicLoaderFinish() {
//        this.isLoadingPic = false;
//        checkIsNoVideoOrPhoto();
//        this.loading_cpb.setVisibility(8);
//        getActivity().getSupportLoaderManager().destroyLoader(0);
//        if (this.folder_grid_recyclerview.getAdapter() == null) {
//            this.folder_grid_recyclerview.setAdapter(this.mVideoFolderAdapter);
//        }
//        if (this.curModuleIndex == 1 && this.category_anrtv != null) {
//            String s = this.category_anrtv.getText().toString();
//            if (!getResources().getString(2131297049).equalsIgnoreCase(s) && TextUtil.isValidate(this.mVideoFolderEntityList)) {
//                for (VideoFolderEntity e : this.mVideoFolderEntityList) {
//                    if (s.equalsIgnoreCase(e.getFolderName())) {
//                        refreshFolderData(e);
//                    }
//                }
//            }
//        }
//    }
//
//    private void checkIsNoVideoOrPhoto(boolean isVideo) {
//        if (2 == this.curModuleIndex) {
//            this.category_layout_rl.setEnabled(true);
//            this.category_anrtv.setVisibility(0);
//            this.drafts_anrtv.setVisibility(8);
//            if (this.mVideoAlbumEntityList == null || this.mVideoAlbumEntityList.size() == 0) {
//                this.loading_cpb.setVisibility(4);
//                this.isNoPicAndVideo = true;
//                this.icon_iv.setVisibility(4);
//                return;
//            }
//            this.isNoPicAndVideo = false;
//            this.icon_iv.setVisibility(0);
//        } else if (this.curModuleIndex == 0) {
//            this.category_layout_rl.setEnabled(true);
//            this.category_anrtv.setVisibility(0);
//            this.drafts_anrtv.setVisibility(8);
//            if (this.mGifAlbumEntityList == null || this.mGifAlbumEntityList.size() == 0) {
//                this.loading_cpb.setVisibility(4);
//                this.isNoPicAndVideo = true;
//                this.icon_iv.setVisibility(4);
//                return;
//            }
//            this.isNoPicAndVideo = false;
//            this.icon_iv.setVisibility(0);
//        } else if (1 == this.curModuleIndex) {
//            if (!this.isLoadingPic) {
//                this.category_layout_rl.setEnabled(true);
//                this.category_anrtv.setVisibility(0);
//                this.drafts_anrtv.setVisibility(8);
//                LogUtil.d("wangchen543543", "图片 mAlbumPictureEntityList size = " + this.mAlbumPictureEntityList.size());
//                if (this.mAlbumPictureEntityList == null || this.mAlbumPictureEntityList.size() == 0) {
//                    this.isNoPicAndVideo = true;
//                    this.loading_cpb.setVisibility(4);
//                    this.icon_iv.setVisibility(4);
//                    return;
//                }
//                this.isNoPicAndVideo = false;
//                this.icon_iv.setVisibility(0);
//            }
//        } else if (4 == this.curModuleIndex) {
//            this.loading_cpb.setVisibility(4);
//            this.icon_iv.setVisibility(4);
//            this.category_anrtv.setVisibility(8);
//            this.drafts_anrtv.setVisibility(8);
//            this.category_layout_rl.setEnabled(false);
//        }
//    }
//
//    private void checkIsNoVideoOrPhoto() {
//        if (2 == this.curModuleIndex) {
//            this.category_layout_rl.setEnabled(true);
//            this.category_anrtv.setVisibility(0);
//            this.drafts_anrtv.setVisibility(8);
//            if (this.mVideoAlbumEntityList == null || this.mVideoAlbumEntityList.size() == 0) {
//                this.isNoPicAndVideo = true;
//                this.icon_iv.setVisibility(4);
//                return;
//            }
//            this.isNoPicAndVideo = false;
//            this.icon_iv.setVisibility(0);
//        } else if (this.curModuleIndex == 0) {
//            this.category_layout_rl.setEnabled(true);
//            this.category_anrtv.setVisibility(0);
//            this.drafts_anrtv.setVisibility(8);
//            if (this.mGifAlbumEntityList == null || this.mGifAlbumEntityList.size() == 0) {
//                this.isNoPicAndVideo = true;
//                this.icon_iv.setVisibility(4);
//                return;
//            }
//            this.isNoPicAndVideo = false;
//            this.icon_iv.setVisibility(0);
//        } else if (1 == this.curModuleIndex) {
//            if (!this.isLoadingPic) {
//                this.category_layout_rl.setEnabled(true);
//                this.category_anrtv.setVisibility(0);
//                this.drafts_anrtv.setVisibility(8);
//                LogUtil.d("wangchen543543", "图片 mAlbumPictureEntityList size = " + this.mAlbumPictureEntityList.size());
//                if (this.mAlbumPictureEntityList == null || this.mAlbumPictureEntityList.size() == 0) {
//                    this.isNoPicAndVideo = true;
//                    this.icon_iv.setVisibility(4);
//                    return;
//                }
//                this.isNoPicAndVideo = false;
//                this.icon_iv.setVisibility(0);
//            }
//        } else if (4 == this.curModuleIndex) {
//            this.loading_cpb.setVisibility(4);
//            this.icon_iv.setVisibility(4);
//            this.category_anrtv.setVisibility(8);
//            this.drafts_anrtv.setVisibility(8);
//            this.category_layout_rl.setEnabled(false);
//        }
//    }
//
//    private void hideMultiConfirmIv() {
//        if (this.fl_multiple_select.getVisibility() == 0 && this.fl_multiple_select.getAlpha() == 1.0f) {
//            final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.fl_multiple_select.getLayoutParams();
//            final int startValue = DensityUtil.dip2px(30.0f);
//            final int endValue = DensityUtil.dip2px(-60.0f);
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) startValue, (float) endValue);
//            valueAnimator.setInterpolator(new LinearInterpolator());
//            valueAnimator.setDuration(100);
//            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    layoutParams.setMargins(0, 0, 0, (int) ((Float) animation.getAnimatedValue()).floatValue());
//                    fl_multiple_select.setLayoutParams(layoutParams);
//                }
//            });
//            valueAnimator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
//                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                    super.onAnimationEnd(animation);
//                    layoutParams.setMargins(0, 0, 0, endValue);
//                    fl_multiple_select.setLayoutParams(layoutParams);
//                    fl_multiple_select.setVisibility(8);
//                }
//
//                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
//                    super.onAnimationStart(animation);
//                    layoutParams.setMargins(0, 0, 0, startValue);
//                    fl_multiple_select.setVisibility(0);
//                }
//            });
//            valueAnimator.start();
//        }
//    }
//
//    private void showMultiConfirmIv() {
//        this.fl_multiple_select.setBackgroundResource(2130838320);
//        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.fl_multiple_select.getLayoutParams();
//        final int startValue = DensityUtil.dip2px(-60.0f);
//        final int endValue = DensityUtil.dip2px(30.0f);
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) startValue, (float) endValue);
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setDuration(100);
//        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator animation) {
//                layoutParams.setMargins(0, 0, 0, (int) ((Float) animation.getAnimatedValue()).floatValue());
//                fl_multiple_select.setLayoutParams(layoutParams);
//            }
//        });
//        valueAnimator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
//            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                super.onAnimationEnd(animation);
//                layoutParams.setMargins(0, 0, 0, endValue);
//                fl_multiple_select.setLayoutParams(layoutParams);
//                fl_multiple_select.setVisibility(0);
//                fl_multiple_select.setEnabled(false);
//            }
//
//            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
//                super.onAnimationStart(animation);
//                layoutParams.setMargins(0, 0, 0, startValue);
//                fl_multiple_select.setVisibility(0);
//            }
//        });
//        valueAnimator.start();
//    }
//
//    private boolean judgeActivity() {
//        return (this.mActivity == null || this.mActivity.isDestroyed()) ? false : true;
//    }
//
//    public void onFolderClick(final VideoFolderEntity videoFolderEntity) {
//        this.loading_cpb.setVisibility(0);
//        hideFolderAnimation(new OnHideAnimationListener() {
//            public void hide() {
//                category_anrtv.setText(videoFolderEntity.getFolderName());
//                refreshFolderData(videoFolderEntity);
//                loading_cpb.setVisibility(8);
//            }
//        });
//    }
//
//    private void refreshFolderData(VideoFolderEntity videoFolderEntity) {
//        if (TextUtil.isValidate(videoFolderEntity)) {
//            List<VideoAlbumEntity> videoAlbumEntityList = videoFolderEntity.getVideoAlbumEntityList();
//            this.mGifAlbumEntityList.clear();
//            this.mGifAlbumEntityList.add(new VideoAlbumEntity((int) Item.PICTURE_SYNTHESIS_ENTER_TYPE));
//            this.mVideoAlbumEntityList.clear();
//            if (videoAlbumEntityList != null) {
//                for (VideoAlbumEntity entity : videoAlbumEntityList) {
//                    if (entity.getVideoDuration() >= 1000) {
//                        this.mVideoAlbumEntityList.add(entity);
//                    }
//                    if (entity.getVideoDuration() >= Constants.VIDEO_BLACK_DURATION) {
//                        this.mGifAlbumEntityList.add(entity);
//                    }
//                }
//            }
//            this.mGifAlbumAdapter.notifyDataSetChanged();
//            this.mVideoAlbumAdapter.notifyDataSetChanged();
//            List<AlbumPictureEntity> albumPictureEntities = videoFolderEntity.albumPictureEntities;
//            this.mAlbumPictureEntityList.clear();
//            if (albumPictureEntities != null) {
//                this.mAlbumPictureEntityList.addAll(albumPictureEntities);
//            }
//            this.mPhotoAlbumAdapter.notifyDataSetChanged();
//            this.loading_cpb.setVisibility(8);
//        }
//    }
//
//    public static int readPictureDegree(String path) {
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            int orientation = exifInterface.getAttributeInt("Orientation", 1);
//            int width = exifInterface.getAttributeInt("ImageWidth", 0);
//            int length = exifInterface.getAttributeInt("ImageLength", 0);
//            LogUtil.d(LOG_TAG, String.format("take picture's oritation %d , width : %d , length : %d", new Object[]{Integer.valueOf(orientation), Integer.valueOf(width), Integer.valueOf(length)}));
//            switch (orientation) {
//                case 3:
//                    return 180;
//                case 6:
//                    return 90;
//                case 8:
//                    return Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
//                default:
//                    return 0;
//            }
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "ExifInterface IO 异常");
//            return 0;
//        }
//    }
//
//    private void switch_adapter(View v) {
//        if (v != null) {
//            switch (v.getId()) {
//                case 2131690242:
//                    this.video_grid_recyclerview.setAdapter(this.mVideoAlbumAdapter);
//                    return;
//                case 2131690244:
//                    this.video_grid_recyclerview.setAdapter(this.mPhotoAlbumAdapter);
//                    return;
//                case 2131690246:
//                    this.video_grid_recyclerview.setAdapter(this.mGifAlbumAdapter);
//                    return;
//                default:
//                    return;
//            }
//        }
//    }
//
//    private void resetData(final View v) {
//        new Thread() {
//            public void run() {
//                if (resetEntity != null) {
//                    for (int i = 0; i < resetEntity.size(); i++) {
//                        AlbumPictureEntity albumPictureEntity = (AlbumPictureEntity) resetEntity.get(i);
//                        if (albumPictureEntity != null) {
//                            albumPictureEntity.selected = 0;
//                        }
//                    }
//                    resetEntity.clear();
//                }
//                App.runOnUiThread(1.lambdaFactory$(this, v));
//            }
//
//            private /* synthetic */ void lambda$run$0(View v) {
//                switch_adapter(v);
//                loading_cpb.setVisibility(8);
//            }
//        }.start();
//    }
//}