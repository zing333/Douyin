package cn.nineton.onetake.widget;

import android.graphics.Bitmap;

//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.media.MediaScannerConnection;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.WindowManager.LayoutParams;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.AnimationUtils;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import cn.nineton.onetake.App;
//import cn.nineton.onetake.R;
//import cn.nineton.onetake.bean.OfficialTagBean;
//import cn.nineton.onetake.event.StartTopViewDisMissEvent;
//import cn.nineton.onetake.event.StartTopViewShowEvent;
//import cn.nineton.onetake.util.AppMessage;
//import cn.nineton.onetake.util.Constants;
//import cn.nineton.onetake.util.GlobalHelper;
//import cn.nineton.onetake.util.LocaleUtil;
//import cn.nineton.onetake.util.LogUtil;
//import cn.nineton.onetake.util.PriorityRunnable;
//import cn.nineton.onetake.util.PriorityThreadPoolManager;
//import cn.nineton.onetake.util.ShareUtils;
//import cn.nineton.onetake.util.SharedPrefUtil;
//import cn.nineton.onetake.util.TextUtil;
//import cn.nineton.onetake.util.ToastMessageEvent;
//import cn.nineton.onetake.util.share.Platform;
//import cn.nineton.onetake.util.share.PlatformActionListener;
//import cn.nineton.onetake.util.share.ShareEditPageDialog;
//import cn.nineton.onetake.util.share.ShareViewHelper;
////import cn.sharesdk.framework.Platform;
////import cn.sharesdk.framework.PlatformActionListener;
////import cn.sharesdk.framework.PlatformDb;
////
////import com.blink.academy.onetake.App;
////import com.blink.academy.onetake.Config;
////import com.blink.academy.onetake.VideoTools.Player;
////import com.blink.academy.onetake.bean.tag.OfficialTagBean;
////import com.blink.academy.onetake.bean.utils.JsonParserUtil;
////import com.blink.academy.onetake.controller.UserController;
////import com.blink.academy.onetake.fresco.FrescoHelper;
////import com.blink.academy.onetake.http.params.PhotoParams;
////import com.blink.academy.onetake.http.params.UserParams;
////import com.blink.academy.onetake.model.ShareModel;
////import com.blink.academy.onetake.model.StatusBarModel;
////import com.blink.academy.onetake.support.callbacks.IControllerCallback;
////import com.blink.academy.onetake.support.debug.LogUtil;
////import com.blink.academy.onetake.support.events.ChangeShareEvent;
////import com.blink.academy.onetake.support.events.ChangeStatusBarColorEvent;
////import com.blink.academy.onetake.support.events.StartTopViewDisMissEvent;
////import com.blink.academy.onetake.support.events.StartTopViewShowEvent;
////import com.blink.academy.onetake.support.events.ToastMessageEvent;
////import com.blink.academy.onetake.support.global.Constants;
////import com.blink.academy.onetake.support.helper.GlobalHelper;
////import com.blink.academy.onetake.support.info.AppInfo;
////import com.blink.academy.onetake.support.share.ShareBitmapEntity;
////import com.blink.academy.onetake.support.share.ShareImageManager;
////import com.blink.academy.onetake.support.share.ShareUtils;
////import com.blink.academy.onetake.support.share.ShareViewHelper;
////import com.blink.academy.onetake.support.thread.PriorityRunnable;
////import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
////import com.blink.academy.onetake.support.twitter.Extractor;
////import com.blink.academy.onetake.support.twitter.Extractor.Entity;
////import com.blink.academy.onetake.support.twitter.TwitterInfo;
////import com.blink.academy.onetake.support.twitter.TwitterInfo.UserRawData;
////import com.blink.academy.onetake.support.utils.BitmapUtil;
////import com.blink.academy.onetake.support.utils.BitmapUtil.SaveBitmapCallback;
////import com.blink.academy.onetake.support.utils.BuglyLogUtil;
////import com.blink.academy.onetake.support.utils.ClipboardUtil;
////import com.blink.academy.onetake.support.utils.DensityUtil;
////import com.blink.academy.onetake.support.utils.FileUtil;
////import com.blink.academy.onetake.support.utils.GIFUtil;
////import com.blink.academy.onetake.support.utils.ImageUtil;
////import com.blink.academy.onetake.support.utils.JsonUtil;
////import com.blink.academy.onetake.support.utils.LocaleUtil;
////import com.blink.academy.onetake.support.utils.MP4Util;
////import com.blink.academy.onetake.support.utils.PhotoUtil;
////import com.blink.academy.onetake.support.utils.PhotoUtil.SavePhotoListener;
////import com.blink.academy.onetake.support.utils.SharedPrefUtil;
////import com.blink.academy.onetake.support.utils.SharedPrefUtils;
////import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
////import com.blink.academy.onetake.support.utils.StatusBarUtil;
////import com.blink.academy.onetake.support.utils.TextUtil;
////import com.blink.academy.onetake.support.utils.UploadWeiboUtil;
////import com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil;
////import com.blink.academy.onetake.support.weibo.AccessTokenKeeper;
////import com.blink.academy.onetake.widgets.AppMessage.AppMessage;
////import com.blink.academy.onetake.widgets.IOSDialog.IOSAlertDialog;
////import com.blink.academy.onetake.widgets.TextView.AvenirNextRegularTextView;
////import com.blink.academy.onetake.widgets.dialog.DeleteDialog.OnDeleteClickListener;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.10;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.11;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.12;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.13;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.14;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.15;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.3;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.4;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.5;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.6;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.7;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.8;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$.Lambda.9;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$BaseOnHandlerMp4ToGifListener$.Lambda.1;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$BaseOnHandlerMp4ToGifListener$.Lambda.2;
////import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$TumblrGetAccountCheckListener$.Lambda;
////import com.blink.academy.onetake.widgets.dialog.ShareCropDialog.OnShareCropClickListener;
////import com.blink.academy.onetake.widgets.dialog.ShareEditPageDialog.OnShareEditPageClickListener;
////import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//
//import org.greenrobot.eventbus.EventBus;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map.Entry;
//
public class ShareActionSheetDialog {
    public interface OnDialogDismissListener {
        void onDismiss(boolean z);
    }

    public interface OnHandleMP4ToVideoListener {
        void onFail(String str);

        void onFinish(String str, Bitmap bitmap);

        void onStart();
    }

    public static abstract class OnHandlerMp4ToGifListener {
        String content;

        public abstract void onFinish(String str);

        public abstract void onFinish(String str, int i);

        public abstract void onStart();

        public void setContent(String content) {
            this.content = content;
        }

        public void onProgressing(float progress) {
        }
    }
}
//public class ShareActionSheetDialog implements OnClickListener {
//    private static final String FACEBOOK = "facebook";
//    private static final int FACEBOOKHANDLER = 4;
//    private static final String INSTAGRAM = "instagram";
//    private static final int INSTAGRAMHANDLER = 8;
//    private static final String MESSENGER = "messenger";
//    private static final int MESSENGERHANDLER = 9;
//    public static final String QIUQIU = "qq";
//    private static final int QIUQIUHANDLER = 1;
//    private static final String SAVEPHOTO = "savephoto";
//    private static final int SAVEPHOTOHANDLER = 7;
//    private static final String SYSTEM = "system";
//    private static final int SYSTEMHANDLER = 5;
//    private static String[] ShareNotUseTagType = null;
//    private static List<String> ShareNotUseTagTypeList = null;
//    public static final String TAG = null;
//    private static final String TUMBLR = "tumblr";
//    private static final int TUMBLRHANDLER = 6;
//    private static final String TWITTER = "twitter";
//    private static final int TWITTERHANDLER = 3;
//    public static final String WECHAT = "wechat";
//    private static final int WECHATHANDLER = 0;
//    public static final String WECHATMOMENTS = "wechatmoments";
//    private static final int WECHATMOMENTSHANDLER = 10;
//    public static final String WEIBO = "weibo";
//    private static final int WEIBOHANDLER = 2;
//    private AvenirNextRegularTextView app_message_anrtv;
//    private View app_message_ll;
//    private Dialog dialog;
//    private View dialog_background_view;
//    private Display display;
//    private View english_share_wechat_moments_layout_ll;
//    private boolean isChinese;
//    private boolean isDismiss;
//    private boolean isFullScreen;
//    private boolean isOnlySelf;
//    private boolean isOpenWaterMark;
//    private Activity mContext;
//    private int mDuration;
//    private String mEnOtherText;
//    private View mEnglishQQLLayout;
//    private View mEnglishWeChatLLayout;
//    private View mEnglishWeiboLLayout;
//    private Handler mHandler;
//    private boolean mIsGyphy;
//    private List<View> mItems;
//    private ShareCropDialog.OnShareCropClickListener mOnShareCropClickListener;
//    private String mOtherText;
//    private View mPermissionView;
//    private String mPhotoText;
//    private View mShareTumblrLinearLayout;
//    private int mShareType;
//    private View mSystemLinearLayout;
//    private String mTitle;
//    private String mTwitterName;
//    private String mUserName;
//    private String mVideoFilePath;
//    private int mVideoHeight;
//    private int mVideoWidth;
//    private String mWeiboName;
//    private String mZhOtherText;
//    private OnShareSheetItemClickListener onShareSheetItemClickListener;
//    private ArrayList<OfficialTagBean> ots;
//    private int photoId;
//    private String preview_url;
//    private View rootView;
//    private View save_video_camera_layout_ll;
//    private View share_facebook_layout_ll;
//    private View share_instagram_layout_ll;
//    private View share_messenger_layout_ll;
//    private View share_qq_layout_ll;
//    private View share_twitter_layout_ll;
//    private View share_wechat_layout_ll;
//    private View share_wechat_moments_layout_ll;
//    private View share_weibo_layout_ll;
//    private String web_url;
//
//    public static abstract class OnHandlerMp4ToGifListener {
//        String content;
//
//        public abstract void onFinish(String str);
//
//        public abstract void onFinish(String str, int i);
//
//        public abstract void onStart();
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public void onProgressing(float progress) {
//        }
//    }
//
//    public interface OnDialogCropListener {
//        void onShareCropDismiss();
//
//        void onShareCropShow();
//    }
//
//    public interface OnShareSheetItemClickListener {
//        void onCloseClick();
//
//        void onDeleteClick(String str);
//
//        void onShareCropClick();
//
//        void onShareQQConfirmClick();
//
//        void onShareWechatConfirmClick();
//
//        void onShareWechatMomentsConfirmClick();
//
//        void onShareWeiboConfirmClick();
//    }
//
//    private abstract class BaseOnHandlerMp4ToGifListener extends OnHandlerMp4ToGifListener {
//        int baseStringResId;
//        int cnIconViewId;
//        int cnLoadingViewId;
//        int cnTextViewId;
//        int enIconViewId;
//        int enLoadingViewId;
//        int enTextViewId;
//        boolean isChinese;
//        String key;
//        WeakReference<Context> mWeakReference;
//        int photoId;
//        int prepareResId;
//        int uploadResId;
//        View view;
//
//        public BaseOnHandlerMp4ToGifListener(Context context, View view, String key, boolean isChinese, int photoId, int cnIconViewId, int cnLoadingViewId, int cnTextViewId, int enIconViewId, int enLoadingViewId, int enTextViewId, int prepareResId, int uploadResId, int baseStringResId) {
//            this.mWeakReference = new WeakReference(context);
//            this.view = view;
//            this.key = key;
//            this.isChinese = isChinese;
//            this.photoId = photoId;
//            this.cnIconViewId = cnIconViewId;
//            this.cnLoadingViewId = cnLoadingViewId;
//            this.cnTextViewId = cnTextViewId;
//            this.enIconViewId = enIconViewId;
//            this.enLoadingViewId = enLoadingViewId;
//            this.enTextViewId = enTextViewId;
//            this.prepareResId = prepareResId;
//            this.uploadResId = uploadResId;
//            this.baseStringResId = baseStringResId;
//        }
//
//        public void finishViewRever() {
//            this.view.setEnabled(true);
//            this.view.setAlpha(1.0f);
//            TextView share_wechat_tv = null;
//            if (this.isChinese) {
//                View cnIconView = this.view.findViewById(this.cnIconViewId);
//                if (cnIconView != null) {
//                    cnIconView.setVisibility(View.VISIBLE);
//                }
//                View cnLoadingView = this.view.findViewById(this.cnLoadingViewId);
//                if (cnLoadingView != null) {
//                    cnLoadingView.setVisibility(View.GONE);
//                }
//                View cnTextView = this.view.findViewById(this.cnTextViewId);
//                if (cnTextView != null && (cnTextView instanceof TextView)) {
//                    share_wechat_tv = (TextView) cnTextView;
//                }
//            } else {
//                View enIconView = this.view.findViewById(this.enIconViewId);
//                if (enIconView != null) {
//                    enIconView.setVisibility(View.VISIBLE);
//                }
//                View enLoadingView = this.view.findViewById(this.enLoadingViewId);
//                if (enLoadingView != null) {
//                    enLoadingView.setVisibility(View.GONE);
//                }
//                View enTextView = this.view.findViewById(this.enTextViewId);
//                if (enTextView != null && (enTextView instanceof TextView)) {
//                    share_wechat_tv = (TextView) enTextView;
//                }
//            }
//            if (share_wechat_tv != null) {
//                share_wechat_tv.setText(ShareActionSheetDialog.getResourcesString(this.baseStringResId));
//            }
//        }
//
//        public void onStart() {
//            if (getContext() != null) {
////                ((Activity) getContext()).runOnUiThread(1.lambdaFactory$(this));
//                ((Activity) getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "ShareActionSheetDialog:onStart", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        }
//
//        private /* synthetic */ void lambda$onStart$0() {
//            ShareActionSheetDialog.this.updateShareState(this.photoId, this.key, 1);
//            TextView share_wechat_tv = null;
//            if (this.isChinese) {
//                View cnIconView = this.view.findViewById(this.cnIconViewId);
//                if (cnIconView != null) {
//                    cnIconView.setVisibility(View.INVISIBLE);
//                }
//                View cnLoadingView = this.view.findViewById(this.cnLoadingViewId);
//                if (cnLoadingView != null) {
//                    cnLoadingView.setVisibility(View.VISIBLE);
//                }
//                View cnTextView = this.view.findViewById(this.cnTextViewId);
//                if (cnTextView != null && (cnTextView instanceof TextView)) {
//                    share_wechat_tv = (TextView) cnTextView;
//                }
//            } else {
//                View enIconView = this.view.findViewById(this.enIconViewId);
//                if (enIconView != null) {
//                    enIconView.setVisibility(View.INVISIBLE);
//                }
//                View enLoadingView = this.view.findViewById(this.enLoadingViewId);
//                if (enLoadingView != null) {
//                    enLoadingView.setVisibility(View.VISIBLE);
//                }
//                View enTextView = this.view.findViewById(this.enTextViewId);
//                if (enTextView != null) {
//                    share_wechat_tv = (TextView) enTextView;
//                }
//            }
//            if (share_wechat_tv != null) {
//                share_wechat_tv.setText(ShareActionSheetDialog.getShareFormatString(this.baseStringResId, this.prepareResId));
//            }
//        }
//
//        public void onFinish(String path) {
//        }
//
//        public void onFinish(String path, int photoId) {
//            if (getContext() != null) {
////                App.runOnUiThread(2.lambdaFactory$(this, photoId));
//                App.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "ShareActionSheetDialog:onFinish", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        }
//
//        private /* synthetic */ void lambda$onFinish$1(int photoId) {
//            if (ShareActionSheetDialog.this.onShareSheetItemClickListener != null) {
//                if (ShareActionSheetDialog.WECHAT.equals(this.key)) {
//                    ShareActionSheetDialog.this.onShareSheetItemClickListener.onShareWechatConfirmClick();
//                } else if (ShareActionSheetDialog.QIUQIU.equals(this.key)) {
//                    ShareActionSheetDialog.this.onShareSheetItemClickListener.onShareQQConfirmClick();
//                }
//            }
//            EventBus.getDefault().post(new ChangeShareEvent(photoId, this.key, 2, 3));
//            ShareActionSheetDialog.this.updateShareState(photoId, this.key, 2);
//            TextView share_wechat_tv = null;
//            if (this.isChinese) {
//                View cnTextView = this.view.findViewById(this.cnTextViewId);
//                if (cnTextView != null && (cnTextView instanceof TextView)) {
//                    share_wechat_tv = (TextView) cnTextView;
//                }
//            } else {
//                View enTextView = this.view.findViewById(this.enTextViewId);
//                if (enTextView != null && (enTextView instanceof TextView)) {
//                    share_wechat_tv = (TextView) enTextView;
//                }
//            }
//            if (share_wechat_tv != null) {
//                share_wechat_tv.setText(ShareActionSheetDialog.getShareFormatString(this.baseStringResId, this.uploadResId));
//            }
//        }
//
//        private Context getContext() {
//            if (this.mWeakReference != null) {
//                return (Context) this.mWeakReference.get();
//            }
//            return null;
//        }
//    }
//
//    private abstract class BaseShareCropListener implements ShareCropDialog.OnShareCropClickListener {
//        boolean isFilmSubtitles;
//        boolean isMoreShort;
//        boolean isNeedAddWaterMark;
//        boolean isSpecial;
//        OnHandlerMp4ToGifListener mOnHandlerMp4ToGifListener;
//        View mView;
//
//        public BaseShareCropListener(View view, boolean isSpecial, boolean isMoreShort, boolean isNeedAddWaterMark, boolean isFilmSubtitles, OnHandlerMp4ToGifListener onHandlerMp4ToGifListener) {
//            this.mView = view;
//            this.isSpecial = isSpecial;
//            this.isMoreShort = isMoreShort;
//            this.isNeedAddWaterMark = isNeedAddWaterMark;
//            this.isFilmSubtitles = isFilmSubtitles;
//            this.mOnHandlerMp4ToGifListener = onHandlerMp4ToGifListener;
//        }
//
//        public void onShareCropCancelClick() {
//            this.mView.setEnabled(true);
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//            }
//        }
//
//        public void onShareCropDismiss() {
//        }
//
//        public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
//            final String str = addText;
//            final boolean z = isCrop;
//            final float f = startScale;
//            PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                public void run() {
//                    ShareActionSheetDialog.this.mp4ToGif(BaseShareCropListener.this.isSpecial, str, BaseShareCropListener.this.isMoreShort, z, f, BaseShareCropListener.this.mOnHandlerMp4ToGifListener, ShareActionSheetDialog.this.photoId, BaseShareCropListener.this.isNeedAddWaterMark, BaseShareCropListener.this.isFilmSubtitles);
//                }
//            });
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//            }
//        }
//    }
//
//    public interface OnDialogDismissListener {
//        void onDismiss(boolean z);
//    }
//
//    public interface OnHandleMP4ToVideoListener {
//        void onFail(String str);
//
//        void onFinish(String str, Bitmap bitmap);
//
//        void onStart();
//    }
//
//    private class QQSharePlatformActionListener implements PlatformActionListener {
//        String path;
//
//        public QQSharePlatformActionListener(String path) {
//            this.path = path;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            File file = new File(this.path);
//            if (file.exists()) {
//                file.delete();
//            }
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.QIUQIU);
//            ShareActionSheetDialog.this.removeEmptyMessage(1);
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296799));
//            } else {
//                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                Object[] objArr = new Object[1];
//                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296799);
//                shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//            }
//            ShareActionSheetDialog.this.qqViewRever();
//        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            File file = new File(this.path);
//            if (file.exists()) {
//                file.delete();
//            }
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.QIUQIU);
//            ShareActionSheetDialog.this.removeEmptyMessage(1);
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, false, 2131296799));
//            } else {
//                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
//                Object[] objArr = new Object[1];
//                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296799);
//                shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//            }
//            ShareActionSheetDialog.this.qqViewRever();
//        }
//
//        public void onCancel(Platform platform, int i) {
//            File file = new File(this.path);
//            if (file.exists()) {
//                file.delete();
//            }
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.QIUQIU);
//            ShareActionSheetDialog.this.removeEmptyMessage(1);
//            ShareActionSheetDialog.this.qqViewRever();
//        }
//    }
//
//    private class TumblrGetAccountCheckListener implements PlatformActionListener {
//        String content;
//        String path;
//
//        public TumblrGetAccountCheckListener(String content, String path) {
//            this.content = content;
//            this.path = path;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            //App.runOnUiThreadDelay(Lambda.1.lambdaFactory$(this), 100);
//            App.runOnUiThreadDelay(new Runnable() {
//                @Override
//                public void run() {
//                    lambda$onComplete$0();
//                }
//            }, 100);
//        }
//
//        private /* synthetic */ void lambda$onComplete$0() {
//            if (ShareActionSheetDialog.this.mShareType == 2) {
//                ShareUtils.shareVideoToTumblr(ShareActionSheetDialog.this.getContext(), this.content, ShareActionSheetDialog.this.web_url, new TumblrShareActionListener(this.path));
//            } else {
//                ShareUtils.shareImageToTumblr(ShareActionSheetDialog.this.getContext(), this.content, this.path, new TumblrShareActionListener(this.path));
//            }
//        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            platform.removeAccount(true);
//            ShareActionSheetDialog.this.onShareTumblrCancel(this.path, ShareActionSheetDialog.this.photoId);
//        }
//
//        public void onCancel(Platform platform, int i) {
//            ShareActionSheetDialog.this.onShareTumblrCancel(this.path, ShareActionSheetDialog.this.photoId);
//        }
//    }
//
//    private class TumblrMp4ToGifListener extends OnHandlerMp4ToGifListener {
//        View view;
//
//        public TumblrMp4ToGifListener(View view) {
//            this.view = view;
//        }
//
//        public void onStart() {
//            if (ShareActionSheetDialog.this.getContext() != null) {
////                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$TumblrMp4ToGifListener$.Lambda
////                .1.lambdaFactory$(this));
//                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        lambda$onStart$0();
//                    }
//                });
//            }
//        }
//
//        private /* synthetic */ void lambda$onStart$0() {
//            ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 1);
//            this.view.findViewById(R.id.tumblr_icon_iv).setVisibility(View.INVISIBLE);
//            this.view.findViewById(R.id.tumblr_loading_pb).setVisibility(View.VISIBLE);
//            TextView mShareTumblrTextView = this.view.findViewById(R.id.share_tumblr_tv);
//            Object[] objArr = new Object[2];
//            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296800);
//            objArr[1] = ShareActionSheetDialog.getResourcesString(2131296494);
//            mShareTumblrTextView.setText(String.format("%1$s%2$s", objArr));
//        }
//
//        public void onFinish(String path) {
//        }
//
//        public void onFinish(final String path, final int photoId) {
//            if (ShareActionSheetDialog.this.getContext() != null) {
////                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$TumblrMp4ToGifListener$.Lambda
////                .2.lambdaFactory$(this, photoId, path));
//                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        lambda$onFinish$1(photoId,path);
//                    }
//                });
//            }
//        }
//
//        private /* synthetic */ void lambda$onFinish$1(int photoId, String path) {
//            ShareActionSheetDialog.this.updateShareState(photoId, ShareActionSheetDialog.TUMBLR, 2);
//            ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.TUMBLR, 6, photoId);
//            EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.TUMBLR, 2, 3));
//            TextView mShareTumblrTextView = (TextView) this.view.findViewById(R.id.share_tumblr_tv);
//            Object[] objArr = new Object[2];
//            objArr[0] = ShareActionSheetDialog.getResourcesString(R.string.SOCIAL_TUMBLR);
//            objArr[1] = ShareActionSheetDialog.getResourcesString(R.string.BUTTON_SAVE_UPLOADING);
//            mShareTumblrTextView.setText(String.format("%1$s%2$s", objArr));
//            ShareUtils.shareImageToTumblrGetAccountCheck(ShareActionSheetDialog.this.getContext(), this.content, path, new TumblrGetAccountCheckListener(this.content, path));
//        }
//    }
//
//    private class TumblrShareActionListener implements PlatformActionListener {
//        String path;
//
//        public TumblrShareActionListener(String path) {
//            this.path = path;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            File file = new File(this.path);
//            if (file.exists()) {
//                file.delete();
//            }
////            App.runOnUiThread(ShareActionSheetDialog$TumblrShareActionListener$.Lambda
////            .1.lambdaFactory$(this));
//            App.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    lambda$onComplete$0();
//                }
//            });
//        }
//
//        private /* synthetic */ void lambda$onComplete$0() {
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR);
//            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 3, 1));
//            ShareActionSheetDialog.this.removeEmptyMessage(6);
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296800));
//            } else {
//                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                String access$600 = ShareActionSheetDialog.getResourcesString(R.string.TEXT_SHARE_SUCCESS);
//                Object[] objArr = new Object[1];
//                objArr[0] = ShareActionSheetDialog.getResourcesString(R.string.SOCIAL_TUMBLR);
//                shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//            }
//            ShareActionSheetDialog.this.setAllViewEnabled(null);
//            ShareActionSheetDialog.this.tumblrViewRever();
//        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            File file = new File(this.path);
//            if (file.exists()) {
//                file.delete();
//            }
////            App.runOnUiThread(ShareActionSheetDialog$TumblrShareActionListener$.Lambda
////            .2.lambdaFactory$(this));
//            App.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    lambda$onError$1();
//                }
//            });
//        }
//
//        private /* synthetic */ void lambda$onError$1() {
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR);
//            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 3, 2));
//            ShareActionSheetDialog.this.removeEmptyMessage(6);
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, false, 2131296800));
//            } else {
//                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                String access$600 = ShareActionSheetDialog.getResourcesString(R.string.TEXT_SHARE_FAILED);
//                Object[] objArr = new Object[1];
//                objArr[0] = ShareActionSheetDialog.getResourcesString(R.string.SOCIAL_TUMBLR);
//                shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//            }
//            ShareActionSheetDialog.this.tumblrViewRever();
//        }
//
//        public void onCancel(Platform platform, int i) {
//            ShareActionSheetDialog.this.onShareTumblrCancel(this.path, ShareActionSheetDialog.this.photoId);
//        }
//    }
//
//    private class TumblrShareCropClickListener implements ShareCropDialog.OnShareCropClickListener {
//        String text;
//        View view;
//
//        public TumblrShareCropClickListener(View view, String text) {
//            this.view = view;
//            this.text = text;
//        }
//
//        public void onShareCropCancelClick() {
//            this.view.setEnabled(true);
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//            }
//        }
//
//        public void onShareCropDismiss() {
//        }
//
//        public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
////            ShareViewHelper.showShareEditPageDialog(ShareActionSheetDialog.this.getContext(), this.text, false, new TumblrShareEditPageClickListener(this.view, addText, isCrop, startScale), ShareActionSheetDialog$TumblrShareCropClickListener$.Lambda
////            .1.lambdaFactory$(this));
//            ShareViewHelper.showShareEditPageDialog(getContext(), text, false, new TumblrShareEditPageClickListener(view, addText, isCrop, startScale), new OnDialogDismissListener() {
//                @Override
//                public void onDismiss(boolean z) {
//                    if (!z) {
//                        view.setEnabled(true);
//                        ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR);
//                        ShareActionSheetDialog.this.removeEmptyMessage(6);
//                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 3, 2));
//                        ShareActionSheetDialog.this.tumblrViewRever();
//                    }
//                }
//            });
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//            }
//        }
//
//    }
//
//    private class TumblrShareEditPageClickListener implements ShareEditPageDialog.OnShareEditPageClickListener {
//        String addText;
//        boolean isCrop;
//        float startScale;
//        View view;
//
//        public TumblrShareEditPageClickListener(View view, String addText, boolean isCrop, float startScale) {
//            this.view = view;
//            this.addText = addText;
//            this.isCrop = isCrop;
//            this.startScale = startScale;
//        }
//
//        public void onShareEditPageCancelClick() {
//            this.view.setEnabled(true);
//        }
//
//        public void onShareEditPageConfirmClick(String content) {
//            if (ShareActionSheetDialog.this.onShareSheetItemClickListener != null) {
//                ShareActionSheetDialog.this.onShareSheetItemClickListener.onShareWeiboConfirmClick();
//            }
//            final boolean needWatermark = ShareActionSheetDialog.this.isNeedWatermark();
//            PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                public void run() {
//                    ShareActionSheetDialog.this.mp4ToGif(TumblrShareEditPageClickListener.this.addText, TumblrShareEditPageClickListener.this.isCrop, TumblrShareEditPageClickListener.this.startScale, new TumblrMp4ToGifListener(TumblrShareEditPageClickListener.this.view), ShareActionSheetDialog.this.photoId, needWatermark, true);
//                }
//            });
//        }
//    }
//
//    private class TwitterCheckAccountPlatformActionListener implements PlatformActionListener {
//        String content;
//        boolean isVideo;
//        String path;
//
//        public TwitterCheckAccountPlatformActionListener(String content, String path, boolean video) {
//            this.content = content;
//            this.path = path;
//            this.isVideo = video;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER);
////            TwitterInfo twitterInfo = new TwitterInfo();
////            PlatformDb db = platform.getDb();
////            String token = db.getToken();
////            String tokenSecret = db.getTokenSecret();
////            long expiresIn = db.getExpiresIn();
////            String userId = db.getUserId();
////            twitterInfo.credential_raw_data.oauth_token = token;
////            twitterInfo.credential_raw_data.oauth_token_secret = tokenSecret;
////            twitterInfo.credential_raw_data.x_auth_expires = expiresIn;
////            twitterInfo.credential_raw_data.user_id = userId;
////            twitterInfo.user_raw_data = (UserRawData) JsonParserUtil.deserializeByJson(JsonUtil.mapToJsonString(hashMap), UserRawData.class);
////            if (hashMap != null) {
////                Object name = hashMap.get("screen_name");
////                if (name != null) {
////                    SharedPrefUtils.putString(Constants.NewTwitterNameWhenShare, name.toString());
////                }
////            }
////            App.runOnUiThreadDelay(ShareActionSheetDialog$TwitterCheckAccountPlatformActionListener$.Lambda
////            .1.lambdaFactory$(this, platform, twitterInfo), 100);
//            Toast.makeText(getContext(),"onTwitterComplete:",Toast.LENGTH_LONG).show();
//        }
//
////        private /* synthetic */ void lambda$onComplete$0(Platform platform, TwitterInfo twitterInfo) {
////            if (this.isVideo) {
////                ShareUtils.shareVideoToTwitter(ShareActionSheetDialog.this.getContext(), this.content, new TwitterSharePlatformActionListener("", twitterInfo, this.isVideo));
////            } else {
////                ShareUtils.shareImageToTwitter(ShareActionSheetDialog.this.getContext(), platform, this.content, this.path, new TwitterSharePlatformActionListener(this.path, twitterInfo, this.isVideo));
////            }
////        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            platform.removeAccount(true);
//            ShareActionSheetDialog.this.onShareTwitterCancel(this.path, ShareActionSheetDialog.this.photoId, this.isVideo);
//        }
//
//        public void onCancel(Platform platform, int i) {
//            ShareActionSheetDialog.this.onShareTwitterCancel(this.path, ShareActionSheetDialog.this.photoId, this.isVideo);
//        }
//    }
//
//    private class TwitterMp4ToGifListener extends OnHandlerMp4ToGifListener {
//        View view;
//
//        public TwitterMp4ToGifListener(View view, String content) {
//            this.view = view;
//            this.content = content;
//        }
//
//        public void onStart() {
////            if (ShareActionSheetDialog.this.getContext() != null) {
////                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$TwitterMp4ToGifListener$.Lambda
////                .1.lambdaFactory$(this));
////            }
//            Toast.makeText(getContext(),"TwitterMp4ToGif onStart",Toast.LENGTH_LONG).show();
//        }
//
////        private /* synthetic */ void lambda$onStart$0() {
////            ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER, 1);
////            this.view.findViewById(2131690085).setVisibility(4);
////            this.view.findViewById(2131690640).setVisibility(0);
////            TextView share_twitter_tv = (TextView) this.view.findViewById(2131690641);
////            Object[] objArr = new Object[2];
////            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296801);
////            objArr[1] = ShareActionSheetDialog.getResourcesString(2131296494);
////            share_twitter_tv.setText(String.format("%1$s%2$s", objArr));
////        }
//
//        public void onFinish(String path) {
//        }
//
//        public void onFinish(String path, int photoId) {
////            if (ShareActionSheetDialog.this.getContext() != null) {
////                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$TwitterMp4ToGifListener$.Lambda
////                .2.lambdaFactory$(this, photoId, path));
////            }
//            Toast.makeText(getContext(),"TwitterMp4ToGif onFinish",Toast.LENGTH_LONG).show();
//        }
////
////        private /* synthetic */ void lambda$onFinish$1(int photoId, String path) {
////            ShareActionSheetDialog.this.updateShareState(photoId, ShareActionSheetDialog.TWITTER, 2);
////            ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.TWITTER, 3, photoId);
////            EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.TWITTER, 2, 3));
////            TextView share_twitter_tv = (TextView) this.view.findViewById(2131690641);
////            Object[] objArr = new Object[2];
////            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296801);
////            objArr[1] = ShareActionSheetDialog.getResourcesString(2131296496);
////            share_twitter_tv.setText(String.format("%1$s%2$s", objArr));
////            ShareUtils.shareImageToTwitterGetAccountCheck(ShareActionSheetDialog.this.getContext(), this.content, path, new TwitterCheckAccountPlatformActionListener(this.content, path, false));
////        }
//    }
//
//    private class TwitterShareCropClickListener implements ShareCropDialog.OnShareCropClickListener {
//        String text;
//        View view;
//
//        public TwitterShareCropClickListener(View view, String text) {
//            this.view = view;
//            this.text = text;
//        }
//
//        public void onShareCropCancelClick() {
//            this.view.setEnabled(true);
//            ShareActionSheetDialog.this.finishTwitterViewRever(this.view);
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//            }
//        }
//
//        public void onShareCropDismiss() {
//        }
//
//        public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
////            ShareViewHelper.showShareEditPageDialog(ShareActionSheetDialog.this.getContext(), this.text, false, new TwitterShareEditClickListener(this.view, addText, isCrop, startScale), ShareActionSheetDialog$TwitterShareCropClickListener$.Lambda
////            .1.lambdaFactory$(this));
//            ShareViewHelper.showShareEditPageDialog(getContext(), text, false, new TwitterShareEditClickListener(view, addText, isCrop, startScale), new OnDialogDismissListener() {
//                @Override
//                public void onDismiss(boolean z) {
//                    if (!z) {
//                        view.setEnabled(true);
//                        ShareActionSheetDialog.this.finishTwitterViewRever(this.view);
//                    }
//                }
//            });
//            if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//            }
//        }
//    }
//
//    private class TwitterShareEditClickListener implements ShareEditPageDialog.OnShareEditPageClickListener {
//        String addText;
//        boolean isCrop;
//        float startScale;
//        View view;
//
//        public TwitterShareEditClickListener(View view, String addText, boolean isCrop, float startScale) {
//            this.view = view;
//            this.addText = addText;
//            this.isCrop = isCrop;
//            this.startScale = startScale;
//        }
//
//        public void onShareEditPageCancelClick() {
//            this.view.setEnabled(true);
//            ShareActionSheetDialog.this.finishTwitterViewRever(this.view);
//        }
//
//        public void onShareEditPageConfirmClick(final String content) {
//            final boolean needWatermark = ShareActionSheetDialog.this.isNeedWatermark();
//            PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                public void run() {
//                    String officialTagNameString;
//                    if (TextUtils.equals(ShareActionSheetDialog.this.mUserName, GlobalHelper.getUserScreenName())) {
//                        officialTagNameString = ShareActionSheetDialog.this.getTwitterOfficialTagNameString(ShareActionSheetDialog.this.ots);
//                    } else {
//                        officialTagNameString = "";
//                    }
//                    ShareActionSheetDialog.this.mp4ToGif(TwitterShareEditClickListener.this.addText, TwitterShareEditClickListener.this.isCrop, TwitterShareEditClickListener.this.startScale, new TwitterMp4ToGifListener(TwitterShareEditClickListener.this.view, content + "   " + ShareActionSheetDialog.this.web_url + "   " + ShareActionSheetDialog.this.getTwitterTagAppNameString() + " " + officialTagNameString), ShareActionSheetDialog.this.photoId, needWatermark, true);
//                }
//            });
//        }
//    }
//
////    private class TwitterSharePlatformActionListener implements PlatformActionListener {
////        boolean isVideo;
////        String path;
////        TwitterInfo twitterInfo;
////
////        public TwitterSharePlatformActionListener(String path, TwitterInfo twitterInfo, boolean isVideo) {
////            this.path = path;
////            this.twitterInfo = twitterInfo;
////            this.isVideo = isVideo;
////        }
////
////        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////            File file = new File(this.path);
////            if (file.exists()) {
////                file.delete();
////            }
////            new Handler(Looper.getMainLooper()).post(ShareActionSheetDialog$TwitterSharePlatformActionListener$.Lambda
////            .1.lambdaFactory$(this));
////        }
////
////        private /* synthetic */ void lambda$onComplete$0() {
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER);
////            ShareActionSheetDialog.this.removeEmptyMessage(3);
////            if (ShareActionSheetDialog.this.isDismiss) {
////                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296801));
////            } else {
////                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
////                String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
////                Object[] objArr = new Object[1];
////                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296801);
////                shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
////            }
////            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER, 3, 1));
////            final String userName = SharedPrefUtils.getString(Constants.NewTwitterNameWhenShare, null);
////            if (App.isLogin() && userName != null) {
////                UserController.uploadWeiboTwitterName(UserParams.getWeiboTwitterNameWhenShare(null, userName, this.twitterInfo), new IControllerCallback<String>() {
////                    public void success(String s, String jsonContent, long cursorId, boolean isAllDone) {
////                        GlobalHelper.setUserTwitterNameWhenShare(userName);
////                    }
////                });
////            }
////            ShareActionSheetDialog.this.twitterViewRever();
////        }
////
////        public void onError(Platform platform, int i, Throwable throwable) {
////            File file = new File(this.path);
////            if (file.exists()) {
////                file.delete();
////            }
////            App.runOnUiThread(ShareActionSheetDialog$TwitterSharePlatformActionListener$.Lambda
////            .2.lambdaFactory$(this));
////        }
////
////        private /* synthetic */ void lambda$onError$1() {
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER);
////            ShareActionSheetDialog.this.removeEmptyMessage(3);
////            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER, 3, 2));
////            ShareActionSheetDialog.this.twitterViewRever();
////        }
////
////        public void onCancel(Platform platform, int i) {
////            ShareActionSheetDialog.this.onShareTwitterCancel(this.path, ShareActionSheetDialog.this.photoId, this.isVideo);
////        }
////    }
//
////    private class WeChatMomentsPlatformActionListener implements PlatformActionListener {
////        String path;
////
////        public WeChatMomentsPlatformActionListener(String path) {
////            this.path = path;
////        }
////
////        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////            if (TextUtil.isValidate(this.path)) {
////                File file = new File(this.path);
////                if (file.exists()) {
////                    file.delete();
////                }
////            }
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHATMOMENTS);
////            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHATMOMENTS, 3, 1));
////            if (ShareActionSheetDialog.this.isDismiss) {
////                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296797));
////            } else {
////                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
////                String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
////                Object[] objArr = new Object[1];
////                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296797);
////                shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
////            }
////            ShareActionSheetDialog.this.wechatMomentsViewRever();
////        }
////
////        public void onError(Platform platform, int i, Throwable throwable) {
////            if (TextUtil.isValidate(this.path)) {
////                File file = new File(this.path);
////                if (file.exists()) {
////                    file.delete();
////                }
////            }
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHATMOMENTS);
////            if (ShareActionSheetDialog.this.isDismiss) {
////                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, false, 2131296797));
////            } else {
////                EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHATMOMENTS, 3, 2));
////            }
////            ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
////            String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
////            Object[] objArr = new Object[1];
////            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296797);
////            shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
////            ShareActionSheetDialog.this.wechatMomentsViewRever();
////        }
////
////        public void onCancel(Platform platform, int i) {
////            if (TextUtil.isValidate(this.path)) {
////                File file = new File(this.path);
////                if (file.exists()) {
////                    file.delete();
////                }
////            }
////            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT, 3, 3));
////            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT);
////            ShareActionSheetDialog.this.wechatMomentsViewRever();
////        }
////    }
//
//    private class WechatSharePlatformActionListener implements PlatformActionListener {
//        String path;
//
//        public void setPath(String path) {
//            this.path = path;
//        }
//
//        public WechatSharePlatformActionListener(String path) {
//            this.path = path;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            if (TextUtil.isValidate(this.path)) {
//                File file = new File(this.path);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            LogUtil.d("WechatSharePlatformActionListener", "onFinish:onComplete");
//            ShareActionSheetDialog.this.removeEmptyMessage(0);
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT);
//            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT, 3, 1));
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296802));
//            } else {
//                ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                Object[] objArr = new Object[1];
//                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296802);
//                shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//            }
//            ShareActionSheetDialog.this.wechatMomentsViewRever();
//        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            if (TextUtil.isValidate(this.path)) {
//                File file = new File(this.path);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            LogUtil.d("WechatSharePlatformActionListener", "c:onError, i:" + i);
//            ShareActionSheetDialog.this.removeEmptyMessage(0);
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT);
//            if (ShareActionSheetDialog.this.isDismiss) {
//                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, false, 2131296802));
//            } else {
//                EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT, 3, 2));
//            }
//            ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//            String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
//            Object[] objArr = new Object[1];
//            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296802);
//            shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//            ShareActionSheetDialog.this.wechatMomentsViewRever();
//        }
//
//        public void onCancel(Platform platform, int i) {
//            if (TextUtil.isValidate(this.path)) {
//                File file = new File(this.path);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            LogUtil.d("WechatSharePlatformActionListener", "onFinish:onCancel");
//            ShareActionSheetDialog.this.removeEmptyMessage(0);
//            EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT, 3, 3));
//            ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WECHAT);
//            ShareActionSheetDialog.this.wechatMomentsViewRever();
//        }
//    }
//
//    class WeiboCheckAccountOrSharePlatformActionListener implements PlatformActionListener {
//        String path;
//        View view;
//
//        public WeiboCheckAccountOrSharePlatformActionListener(String path, View view) {
//            this.path = path;
//            this.view = view;
//        }
//
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            if (TextUtil.isValidate(this.path)) {
//                File file = new File(this.path);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            App.runOnUiThread(new Runnable() {
//                public void run() {
//                    ShareActionSheetDialog.this.removeEmptyMessage(2);
//                    if (ShareActionSheetDialog.this.isDismiss) {
//                        EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296803));
//                    } else {
//                        ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                        String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                        Object[] objArr = new Object[1];
//                        objArr[0] = ShareActionSheetDialog.getResourcesString(2131296803);
//                        shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//                    }
//                    ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO);
//                    EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO, 3, 1));
//                    ShareActionSheetDialog.this.setAllViewEnabled(WeiboCheckAccountOrSharePlatformActionListener.this.view);
//                    ShareActionSheetDialog.this.finishWeiBoViewRever(WeiboCheckAccountOrSharePlatformActionListener.this.view);
//                }
//            });
//        }
//
//        public void onError(Platform platform, int i, Throwable throwable) {
//            ShareActionSheetDialog.this.onShareWeiboError(this.view, this.path, ShareActionSheetDialog.this.photoId);
//        }
//
//        public void onCancel(Platform platform, int i) {
//            ShareActionSheetDialog.this.onShareWeiboCancel(this.view, this.path, ShareActionSheetDialog.this.photoId);
//        }
//    }
//
////    public class WeiboSharePlatformActionListener implements PlatformActionListener {
////        String path;
////        String text1;
////        View view;
////
////        public WeiboSharePlatformActionListener(String path, String text1, View view) {
////            this.path = path;
////            this.text1 = text1;
////            this.view = view;
////        }
////
////        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////            PlatformDb weiboDB = platform.getDb();
////            if (weiboDB != null) {
////                Oauth2AccessToken token = new Oauth2AccessToken();
////                token.setExpiresIn("" + weiboDB.getExpiresIn());
////                token.setExpiresTime(weiboDB.getExpiresTime());
////                token.setToken(weiboDB.getToken());
////                token.setRefreshToken(weiboDB.get("refresh_token"));
////                token.setUid(weiboDB.getUserId());
////                AccessTokenKeeper.writeAccessToken(ShareActionSheetDialog.this.mContext, token);
////            }
////            if (PhotoParams.text_str.equals(this.path)) {
////                ShareUtils.shareTextWeiBoNoCheck(ShareActionSheetDialog.this.mContext, this.text1, new PlatformActionListener() {
////                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////                        LogUtil.d("slim", "wb suc");
////                        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(ShareActionSheetDialog.this.mContext);
////                        if (token != null && token.isSessionValid() && App.isLogin()) {
////                            UploadWeiboUtil.uploadWeiboByToken(ShareActionSheetDialog.this.mContext, token);
////                        }
////                        App.runOnUiThread(ShareActionSheetDialog$WeiboSharePlatformActionListener$1$.Lambda
////                        .1.lambdaFactory$(this));
////                    }
////
////                    private /* synthetic */ void lambda$onComplete$0() {
////                        ShareActionSheetDialog.this.removeEmptyMessage(2);
////                        if (ShareActionSheetDialog.this.isDismiss) {
////                            EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296803));
////                        } else {
////                            ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
////                            String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
////                            Object[] objArr = new Object[1];
////                            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296803);
////                            shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
////                        }
////                        ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO);
////                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO, 3, 1));
////                        ShareActionSheetDialog.this.finishWeiBoViewRever(WeiboSharePlatformActionListener.this.view);
////                    }
////
////                    public void onError(Platform platform, int i, Throwable throwable) {
////                        LogUtil.d("slim", "wb fail");
////                        ShareActionSheetDialog.this.onShareWeiboError(WeiboSharePlatformActionListener.this.view, WeiboSharePlatformActionListener.this.path, ShareActionSheetDialog.this.photoId);
////                    }
////
////                    public void onCancel(Platform platform, int i) {
////                        LogUtil.d("slim", "wb cancel");
////                        ShareActionSheetDialog.this.onShareWeiboCancel(WeiboSharePlatformActionListener.this.view, WeiboSharePlatformActionListener.this.path, ShareActionSheetDialog.this.photoId);
////                    }
////                }, new WeiboCheckAccountOrSharePlatformActionListener("", this.view));
////            } else {
////                ShareUtils.shareImageToWeiBoNoCheck2(ShareActionSheetDialog.this.getContext(), this.text1, this.path, ShareActionSheetDialog.this.web_url, new PlatformActionListener() {
////                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
////                        File file = new File(WeiboSharePlatformActionListener.this.path);
////                        if (file.exists()) {
////                            file.delete();
////                        }
////                        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(ShareActionSheetDialog.this.mContext);
////                        if (token != null && token.isSessionValid() && App.isLogin()) {
////                            UploadWeiboUtil.uploadWeiboByToken(ShareActionSheetDialog.this.mContext, token);
////                        }
////                        App.runOnUiThread(ShareActionSheetDialog$WeiboSharePlatformActionListener$2$.Lambda
////                        .1.lambdaFactory$(this));
////                    }
////
////                    private /* synthetic */ void lambda$onComplete$0() {
////                        ShareActionSheetDialog.this.removeEmptyMessage(2);
////                        if (ShareActionSheetDialog.this.isDismiss) {
////                            EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296803));
////                        } else {
////                            ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
////                            String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
////                            Object[] objArr = new Object[1];
////                            objArr[0] = ShareActionSheetDialog.getResourcesString(2131296803);
////                            shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
////                        }
////                        ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO);
////                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO, 3, 1));
////                        ShareActionSheetDialog.this.finishWeiBoViewRever(WeiboSharePlatformActionListener.this.view);
////                    }
////
////                    public void onError(Platform platform, int i, Throwable throwable) {
////                        ShareActionSheetDialog.this.onShareWeiboError(WeiboSharePlatformActionListener.this.view, WeiboSharePlatformActionListener.this.path, ShareActionSheetDialog.this.photoId);
////                    }
////
////                    public void onCancel(Platform platform, int i) {
////                        ShareActionSheetDialog.this.onShareWeiboCancel(WeiboSharePlatformActionListener.this.view, WeiboSharePlatformActionListener.this.path, ShareActionSheetDialog.this.photoId);
////                    }
////                }, new WeiboSharePlatformActionListener(this.path, this.text1, this.view));
////            }
////        }
////
////        public void onError(Platform platform, int i, Throwable throwable) {
////            ShareActionSheetDialog.this.onShareWeiboCancel(this.view, this.path, ShareActionSheetDialog.this.photoId);
////        }
////
////        public void onCancel(Platform platform, int i) {
////            ShareActionSheetDialog.this.onShareWeiboCancel(this.view, this.path, ShareActionSheetDialog.this.photoId);
////        }
////    }
//
//    /*  JADX ERROR: Method load error
//        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.<clinit>():void, dex:
//        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
//        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
//        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
//        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
//        	at java.lang.Iterable.forEach(Unknown Source)
//        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
//        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
//        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:293)
//        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
//        	at jadx.api.JavaClass.getCode(JavaClass.java:48)
//        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
//        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1217)
//        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1224)
//        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:595)
//        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:79)
//        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
//        	... 9 more
//        */
////    static {
////        /*
////        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.<clinit>():void, dex:
////        */
////        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.<clinit>():void");
////    }
//
//    public ShareActionSheetDialog(Activity context) {
//        this.isChinese = true;
//        this.isDismiss = false;
//        this.isOpenWaterMark = SharedPrefUtil.getAppInfoBoolean(Constants.IF_SAVE_WATER_MARK, true);
//        this.mPermissionView = null;
//        this.mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.obj != null) {
//                    Bundle bundle = (Bundle) msg.obj;
//                    bundle.putInt("photoid", ShareActionSheetDialog.this.photoId);
//                    String type = bundle.getString("type", "");
//                    int photoId = bundle.getInt("photoid", -1);
//                    if (!"".equals(type) && -1 != photoId) {
//                        LogUtil.d("handleMessage", "photoId : " + photoId + " , type : " + type);
//                        ShareActionSheetDialog.this.removeShareState(photoId, type);
//                        EventBus.getDefault().post(new ChangeShareEvent(photoId, type, 3, 3));
//                    }
//                }
//            }
//        };
//        this.mContext = context;
//        this.display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//    }
//
//    private void displayItemView(View view) {
//        view.setOnClickListener(this);
//        view.setOnTouchListener(TouchFocusChange());
//    }
//
//    @SuppressLint({"RtlHardcoded"})
//    public ShareActionSheetDialog builder(int maxHeight, boolean isNeedDelete, int photoId, boolean isOnlySelf) {
//        LogUtil.d("sharesheet", "photoId : " + photoId);
//        App.RegisterEventBus(this);
//        this.photoId = photoId;
//        this.isOnlySelf = isOnlySelf;
//        SharedPrefUtil.setIsShowSharePop(Constants.SHOW_SHARE_POP, false);
//        return builder(maxHeight, isNeedDelete);
//    }
//
//    @SuppressLint({"RtlHardcoded"})
//    public ShareActionSheetDialog builder(int maxHeight, boolean isNeedDelete) {
////        int scrollViewHeight;
////        //App.getAppInfo();
////        View view = LayoutInflater.from(getContext()).inflate(2130968747, null);
////        this.rootView = view;
////        view.setMinimumWidth(this.display.getWidth());
////        this.app_message_ll = view.findViewById(2131689852);
////        this.app_message_anrtv = (AvenirNextRegularTextView) view.findViewById(2131689853);
////        this.app_message_ll.setVisibility(8);
////        this.dialog = new Dialog(getContext(), 2131493106);
////        this.dialog.setContentView(view);
////        this.dialog.setOnDismissListener(ShareActionSheetDialog$.Lambda .1.lambdaFactory$(this));
////        Window dialogWindow = this.dialog.getWindow();
////        dialogWindow.setGravity(119);
////        LayoutParams lp = dialogWindow.getAttributes();
////        lp.width = this.display.getWidth();
////        lp.height = this.display.getHeight();
////        dialogWindow.setAttributes(lp);
////        this.isChinese = LocaleUtil.isChinese();
////        if (this.mItems != null) {
////            this.mItems.clear();
////            this.mItems = null;
////        }
////        this.mItems = new ArrayList();
////        if (this.isChinese) {
////            view.findViewById(2131690569).setVisibility(0);
////            view.findViewById(2131690643).setVisibility(8);
////        } else {
////            view.findViewById(2131690569).setVisibility(8);
////            view.findViewById(2131690643).setVisibility(0);
////        }
////        view.findViewById(2131690568).setOnClickListener(this);
////        this.dialog_background_view = view.findViewById(2131690683);
////        this.mEnglishWeiboLLayout = view.findViewById(2131690644);
////        this.share_weibo_layout_ll = view.findViewById(2131690602);
////        displayItemView(this.mEnglishWeiboLLayout);
////        displayItemView(this.share_weibo_layout_ll);
////        int itemCount = 0 + 1;
////        this.mEnglishWeChatLLayout = view.findViewById(2131690649);
////        this.share_wechat_layout_ll = view.findViewById(2131690606);
////        this.share_wechat_moments_layout_ll = view.findViewById(2131690611);
////        this.english_share_wechat_moments_layout_ll = view.findViewById(2131690654);
////        if (SharedPrefUtils.getBoolean(Constants.WeChatInstall, false)) {
////            displayItemView(this.share_wechat_layout_ll);
////            displayItemView(this.mEnglishWeChatLLayout);
////            displayItemView(this.share_wechat_moments_layout_ll);
////            displayItemView(this.english_share_wechat_moments_layout_ll);
////            if (this.isOnlySelf) {
////                itemCount++;
////            } else {
////                itemCount += 2;
////            }
////            if (this.isChinese) {
////                this.mItems.add(this.share_wechat_layout_ll);
////                this.mItems.add(this.share_wechat_moments_layout_ll);
////                this.mItems.add(this.share_weibo_layout_ll);
////            } else {
////                this.mItems.add(this.mEnglishWeChatLLayout);
////                this.mItems.add(this.english_share_wechat_moments_layout_ll);
////                this.mItems.add(this.mEnglishWeiboLLayout);
////            }
////        } else {
////            view.findViewById(2131690653).setVisibility(8);
////            view.findViewById(2131690610).setVisibility(8);
////            view.findViewById(2131690615).setVisibility(8);
////            view.findViewById(2131690658).setVisibility(8);
////            this.share_wechat_layout_ll.setVisibility(8);
////            this.mEnglishWeChatLLayout.setVisibility(8);
////            this.share_wechat_moments_layout_ll.setVisibility(8);
////            this.english_share_wechat_moments_layout_ll.setVisibility(8);
////        }
////        if (this.isOnlySelf) {
////            view.findViewById(2131690615).setVisibility(8);
////            view.findViewById(2131690658).setVisibility(8);
////            this.share_wechat_moments_layout_ll.setVisibility(8);
////            this.english_share_wechat_moments_layout_ll.setVisibility(8);
////        }
////        this.mEnglishQQLLayout = view.findViewById(2131690659);
////        this.share_qq_layout_ll = view.findViewById(2131690616);
////        if (SharedPrefUtils.getBoolean(Constants.QQInstall, false)) {
////            displayItemView(this.mEnglishQQLLayout);
////            displayItemView(this.share_qq_layout_ll);
////            itemCount++;
////            if (this.isChinese) {
////                this.mItems.add(this.share_qq_layout_ll);
////            } else {
////                this.mItems.add(this.mEnglishQQLLayout);
////            }
////        } else {
////            view.findViewById(2131690663).setVisibility(8);
////            view.findViewById(2131690620).setVisibility(8);
////            this.share_qq_layout_ll.setVisibility(8);
////            this.mEnglishQQLLayout.setVisibility(8);
////        }
////        this.share_instagram_layout_ll = view.findViewById(2131690621);
////        if (!SharedPrefUtils.getBoolean(Constants.InstagramInstall, false) || this.mShareType == 2) {
////            view.findViewById(2131690624).setVisibility(8);
////            this.share_instagram_layout_ll.setVisibility(8);
////        } else {
////            this.share_instagram_layout_ll.setOnClickListener(this);
////            this.share_instagram_layout_ll.setOnTouchListener(TouchFocusChange());
////            itemCount++;
////            this.mItems.add(this.share_instagram_layout_ll);
////        }
////        this.share_facebook_layout_ll = view.findViewById(2131690630);
////        if (SharedPrefUtils.getBoolean(Constants.FacebookInstall, false)) {
////            this.share_facebook_layout_ll.setOnClickListener(this);
////            this.share_facebook_layout_ll.setOnTouchListener(TouchFocusChange());
////            itemCount++;
////            this.mItems.add(this.share_facebook_layout_ll);
////        } else {
////            view.findViewById(2131690633).setVisibility(8);
////            this.share_facebook_layout_ll.setVisibility(8);
////        }
////        this.share_messenger_layout_ll = view.findViewById(2131690634);
////        if (SharedPrefUtils.getBoolean("messenger_install", false)) {
////            this.share_messenger_layout_ll.setOnClickListener(this);
////            this.share_messenger_layout_ll.setOnTouchListener(TouchFocusChange());
////            this.mItems.add(this.share_messenger_layout_ll);
////            itemCount++;
////        } else {
////            view.findViewById(2131690638).setVisibility(8);
////            this.share_messenger_layout_ll.setVisibility(8);
////        }
////        this.share_twitter_layout_ll = view.findViewById(2131690639);
////        this.share_twitter_layout_ll.setOnClickListener(this);
////        this.share_twitter_layout_ll.setOnTouchListener(TouchFocusChange());
////        itemCount++;
////        this.mItems.add(this.share_twitter_layout_ll);
////        this.mShareTumblrLinearLayout = view.findViewById(2131690625);
////        this.mShareTumblrLinearLayout.setOnClickListener(this);
////        this.mShareTumblrLinearLayout.setOnTouchListener(TouchFocusChange());
////        itemCount++;
////        this.mItems.add(this.mShareTumblrLinearLayout);
////        View mShareLinkLinearLayout = view.findViewById(2131690668);
////        mShareLinkLinearLayout.setOnClickListener(this);
////        mShareLinkLinearLayout.setOnTouchListener(TouchFocusChange());
////        itemCount++;
////        this.mItems.add(mShareLinkLinearLayout);
////        if (this.mShareType != 1 || TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
////            this.mSystemLinearLayout = view.findViewById(2131690664);
////            this.mSystemLinearLayout.setOnClickListener(this);
////            this.mSystemLinearLayout.setOnTouchListener(TouchFocusChange());
////            itemCount++;
////            this.mItems.add(this.mSystemLinearLayout);
////            this.save_video_camera_layout_ll = view.findViewById(2131690673);
////            if (this.mShareType != 2) {
////                TextView save_video_camera_tv = (TextView) view.findViewById(2131690676);
////                this.save_video_camera_layout_ll.setOnClickListener(this);
////                this.save_video_camera_layout_ll.setOnTouchListener(TouchFocusChange());
////                this.mItems.add(this.save_video_camera_layout_ll);
////                itemCount++;
////            } else {
////                view.findViewById(2131690672).setVisibility(8);
////                this.save_video_camera_layout_ll.setVisibility(8);
////            }
////        } else {
////            this.mSystemLinearLayout = view.findViewById(2131690664);
////            this.mSystemLinearLayout.setVisibility(8);
////            this.save_video_camera_layout_ll = view.findViewById(2131690673);
////            view.findViewById(2131690672).setVisibility(8);
////            this.save_video_camera_layout_ll.setVisibility(8);
////        }
////        View delete_layout_ll = view.findViewById(2131690682);
////        if (isNeedDelete) {
////            delete_layout_ll.setOnClickListener(this);
////            delete_layout_ll.setOnTouchListener(TouchFocusChange());
////            itemCount++;
////        } else {
////            view.findViewById(2131690681).setVisibility(8);
////            delete_layout_ll.setVisibility(8);
////        }
////        int statusBarHeight = getStatusBarHeight();
////        int windowHeight = DensityUtil.getMetricsHeight(getContext());
////        int allHeight = (int) (((getContext().getResources().getDimension(2131427594) + 0.5f) + 1.0f) * ((float) (itemCount + 1)));
////        int maxHeights = ((windowHeight - statusBarHeight) - DensityUtil.dip2px(getContext(), 50.0f)) - DensityUtil.dip2px(20.0f);
////        if (allHeight > maxHeights) {
////            scrollViewHeight = maxHeights;
////        } else {
////            scrollViewHeight = allHeight;
////        }
////        View share_root_view_rl = view.findViewById(2131690567);
////        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) share_root_view_rl.getLayoutParams();
////        layoutParams.height = scrollViewHeight;
////        layoutParams.bottomMargin = DensityUtil.dip2px(20.0f);
////        share_root_view_rl.setLayoutParams(layoutParams);
////        View empty_view_fl = view.findViewById(2131690566);
////        empty_view_fl.getLayoutParams().height = ((DensityUtil.getMetricsHeight(getContext()) - DensityUtil.getStatusBarHeight()) - scrollViewHeight) - DensityUtil.dip2px(20.0f);
////        empty_view_fl.setOnClickListener(this);
////        HashMap hashMap = ShareModel.getInstance().getShareMap();
////        if (hashMap != null && hashMap.size() > 0) {
////            for (Entry entry : hashMap.entrySet()) {
////                LogUtil.d("sharesheet", "photoId : " + this.photoId + " , keyId : " + ((Integer) entry.getKey()).intValue());
////                HashMap valueMap = (HashMap) entry.getValue();
////                if (valueMap != null && valueMap.size() > 0) {
////                    for (Entry valueEntry : valueMap.entrySet()) {
////                        String shareType = (String) valueEntry.getKey();
////                        int shareState = ((Integer) valueEntry.getValue()).intValue();
////                        setAllViewDisabled(null);
////                        changeShareState(view, shareType, shareState);
////                    }
////                }
////            }
////        }
//        return this;
//    }
//
//    private /* synthetic */ void lambda$builder$0(DialogInterface d) {
//        this.isDismiss = true;
//        this.app_message_ll.clearAnimation();
//        this.app_message_ll.setVisibility(View.GONE);
//        if (this.onShareSheetItemClickListener != null) {
//            this.onShareSheetItemClickListener.onCloseClick();
//        }
//    }
//
//    private void changeShareState(View view, String shareType, int shareState) {
//        if (shareState != 1 && shareState != 2) {
//            return;
//        }
//        if (WECHAT.equals(shareType)) {
//            if (this.isChinese) {
//                displayShareState(view, 2131690606, 2131690607, 2131690608, 2131690609, 2131296802, shareState);
//            } else {
//                displayShareState(view, 2131690649, 2131690650, 2131690651, 2131690652, 2131296802, shareState);
//            }
//        } else if (QIUQIU.equals(shareType)) {
//            if (this.isChinese) {
//                displayShareState(view, 2131690616, 2131690617, 2131690618, 2131690619, 2131296799, shareState);
//            } else {
//                displayShareState(view, 2131690659, 2131690660, 2131690661, 2131690662, 2131296799, shareState);
//            }
//        } else if (WEIBO.equals(shareType)) {
//            if (this.isChinese) {
//                displayShareState(view, 2131690602, 2131690071, 2131690603, 2131690604, 2131296803, shareState);
//            } else {
//                displayShareState(view, 2131690644, 2131690645, 2131690646, 2131690647, 2131296803, shareState);
//            }
//        } else if (TWITTER.equals(shareType)) {
//            displayShareState(view, 2131690639, 2131690085, 2131690640, 2131690641, 2131296801, shareState);
//        } else if (FACEBOOK.equals(shareType)) {
//            displayShareState(view, 2131690630, 2131690082, 2131690631, 2131690632, 2131296794, shareState);
//        } else if (SYSTEM.equals(shareType)) {
//            displayShareState(view, 2131690664, 2131690665, 2131690666, 2131690667, 2131296519, shareState);
//        } else if (TUMBLR.equals(shareType)) {
//            displayShareState(view, 2131690625, 2131690626, 2131690627, 2131690628, 2131296800, shareState);
//        } else if (SAVEPHOTO.equals(shareType)) {
//            displayShareState(view, 2131690673, 2131690674, 2131690675, 2131690676, 2131296490, shareState);
//        } else if ("instagram".equals(shareType)) {
//            displayShareState(view, 2131690621, 2131690079, 2131690622, 2131690623, 2131296796, shareState);
//        } else if (MESSENGER.equals(shareType)) {
//            displayShareState(view, 2131690634, 2131690635, 2131690636, 2131690637, 2131296795, shareState);
//        }
//    }
//
//    private void displayShareState(View view, int clickLayout, int clickImage, int clickProgress, int clickText, int shareName, int shareState) {
//        view.findViewById(clickLayout).setAlpha(1.0f);
//        view.findViewById(clickLayout).setEnabled(false);
//        view.findViewById(clickImage).setVisibility(4);
//        view.findViewById(clickProgress).setVisibility(0);
//        TextView share_messenger_tv = (TextView) view.findViewById(clickText);
//        Object[] objArr;
//        if (1 == shareState) {
//            objArr = new Object[2];
//            objArr[0] = getResourcesString(shareName);
//            objArr[1] = getResourcesString(2131296494);
//            share_messenger_tv.setText(String.format("%1$s%2$s", objArr));
//            return;
//        }
//        objArr = new Object[2];
//        objArr[0] = getResourcesString(shareName);
//        objArr[1] = getResourcesString(2131296496);
//        share_messenger_tv.setText(String.format("%1$s%2$s", objArr));
//    }
//
//    public ShareActionSheetDialog setShareInfo(boolean isGyphy, String photoText, String userName, String twitterName, String weiboName, String otherText, String web_url, String preview_url, String title) {
//        this.mIsGyphy = isGyphy;
//        this.mPhotoText = photoText;
//        this.mUserName = userName;
//        this.mTwitterName = twitterName;
//        this.mWeiboName = weiboName;
//        this.mOtherText = otherText;
//        this.web_url = web_url;
//        this.preview_url = preview_url;
//        this.mTitle = title;
//        return this;
//    }
//
//    public ShareActionSheetDialog setZhEnOtherText(String zhOtherText, String enOtherText) {
//        this.mZhOtherText = zhOtherText;
//        this.mEnOtherText = enOtherText;
//        return this;
//    }
//
//    public ShareActionSheetDialog setOfficialTags(ArrayList<OfficialTagBean> officialTags) {
//        this.ots = officialTags;
//        return this;
//    }
//
//    public ShareActionSheetDialog setFullScreen(boolean isFullScreen) {
//        this.isFullScreen = isFullScreen;
//        return this;
//    }
//
//    public ShareActionSheetDialog setShareType(int shareType) {
//        this.mShareType = shareType;
//        return this;
//    }
//
//    public ShareActionSheetDialog setVideoWidth(int videoWidth) {
//        this.mVideoWidth = videoWidth;
//        return this;
//    }
//
//    public ShareActionSheetDialog setVideoHeight(int videoHeight) {
//        this.mVideoHeight = videoHeight;
//        return this;
//    }
//
//    public ShareActionSheetDialog setDuration(int duration) {
//        this.mDuration = duration;
//        return this;
//    }
//
//    public ShareActionSheetDialog setVideoFilePath(String videoFilePath) {
//        this.mVideoFilePath = videoFilePath;
//        return this;
//    }
//
//    public ShareActionSheetDialog setOnShareCropClickListener(OnShareCropClickListener onShareCropClickListener) {
//        this.mOnShareCropClickListener = onShareCropClickListener;
//        return this;
//    }
//
//    public ShareActionSheetDialog setOnShareSheetItemClickListener(OnShareSheetItemClickListener onShareSheetItemClickListener) {
//        this.onShareSheetItemClickListener = onShareSheetItemClickListener;
//        return this;
//    }
//
//    public ShareActionSheetDialog setCancelable(boolean cancel) {
//        this.dialog.setCancelable(cancel);
//        return this;
//    }
//
//    public ShareActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
//        this.dialog.setCanceledOnTouchOutside(cancel);
//        return this;
//    }
//
//    public Context getContext() {
//        return this.mContext;
//    }
//
//    public void show() {
//        this.dialog.show();
//    }
//
//    public void dismiss() {
//        App.UnregisterEventBus(this);
//        this.dialog.dismiss();
//        this.mPermissionView = null;
//    }
//
//    public boolean isShow() {
//        return this.dialog.isShowing();
//    }
//
//    public static OnTouchListener TouchFocusChange() {
//        return new OnTouchListener() {
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case 0:
//                        view.setAlpha(0.3f);
//                        break;
//                    case 1:
//                    case 3:
//                        view.setAlpha(1.0f);
//                        break;
//                }
//                return false;
//            }
//        };
//    }
//
//    private void showAppMessageAlert(String message) {
//        if (this.isDismiss) {
//            AppMessage.makeAlertText((Activity) App.getContext(), message).show();
//            return;
//        }
//        int color = App.getContext().getResources().getColor(2131755045);
//        this.app_message_ll.setBackgroundResource(2131755091);
//        showAppMessage(message, color);
//        AppMessage.makeAlertText(this.mContext, message).show();
//    }
//
//    private void showAppMessageSuccess(String message) {
//        LogUtil.d("ljc", Boolean.valueOf(this.isDismiss));
//        if (this.isDismiss) {
//            AppMessage.makeSuccessText((Activity) getContext(), message).show();
//            return;
//        }
//        LogUtil.d("ChangeStatusBarColorEvent", "showAppMessageSuccess : " + getContext().getResources().getColor(2131755085));
//        AppMessage.makeSuccessText((Activity) getContext(), message).show();
//    }
//
//    public void onEventMainThread(StartTopViewShowEvent event) {
//        this.app_message_ll.setBackgroundResource(R.color.colorTransparent);
//        showAppMessage(event.message, event.color);
//    }
//
//    public void onEventMainThread(StartTopViewDisMissEvent event) {
//        hideAppMessage();
//    }
//
//    private void showAppMessage(String message, int color) {
//        this.app_message_anrtv.setText(message);
//        if (this.app_message_ll.getVisibility() == View.GONE) {
//            this.app_message_ll.setVisibility(View.VISIBLE);
//            this.app_message_anrtv.setBackgroundDrawable(AppMessage.getRoundShape(color));
//            LogUtil.d("ljc", "start1" + System.currentTimeMillis());
//            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.actionbar_translate_enter);
//            animation.setAnimationListener(new AnimationListener() {
//                public void onAnimationStart(Animation animation) {
//                }
//
//                public void onAnimationEnd(Animation animation) {
//                }
//
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//            this.app_message_ll.startAnimation(animation);
//        }
//    }
//
//    public void onEventMainThread(ToastMessageEvent event) {
//        if (this.app_message_ll != null) {
//            int color;
//            String resourcesString;
//            Object[] objArr;
//            if (event.witchOne == 2131296311) {
//                color = getContext().getResources().getColor(2131755085);
//                this.app_message_ll.setBackgroundColor(color);
//                showAppMessage(this.mContext.getString(2131296311), color);
//                AppMessage.makeSuccessText((Activity) getContext(), this.mContext.getString(2131296311)).show();
//            } else if (event.isSuccess) {
//                color = getContext().getResources().getColor(2131755085);
//                this.app_message_ll.setBackgroundColor(color);
//                resourcesString = getResourcesString(2131296985);
//                objArr = new Object[1];
//                objArr[0] = getResourcesString(event.witchOne);
//                showAppMessage(String.format(resourcesString, objArr), color);
//            } else {
//                color = App.getContext().getResources().getColor(2131755045);
//                this.app_message_ll.setBackgroundColor(color);
//                resourcesString = getResourcesString(2131296978);
//                objArr = new Object[1];
//                objArr[0] = getResourcesString(event.witchOne);
//                showAppMessage(String.format(resourcesString, objArr), color);
//            }
//        }
//    }
//
//    private void hideAppMessage() {
//        if (this.app_message_ll.getVisibility() == 0) {
//            Animation animation = AnimationUtils.loadAnimation(getContext(), 2131034123);
//            animation.setAnimationListener(new AnimationListener() {
//                public void onAnimationStart(Animation animation) {
//                }
//
//                public void onAnimationEnd(Animation animation) {
//                    StatusBarModel.getInstance().clearColorAndState();
//                    ShareActionSheetDialog.this.app_message_ll.setVisibility(8);
//                    StatusBarUtil.setColorNav(ShareActionSheetDialog.this.mContext);
//                    EventBus.getDefault().post(new ChangeStatusBarColorEvent(-1));
//                }
//
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//            this.app_message_ll.startAnimation(animation);
//            LogUtil.d("ljc", "start2" + System.currentTimeMillis());
//        }
//    }
//
//    public static void clearWechatWeiboQiuQiu(String witchOne) {
//        HashMap hashMap = ShareModel.getInstance().getShareMap();
//        if (hashMap != null && hashMap.size() > 0) {
//            for (Entry entry : hashMap.entrySet()) {
//                HashMap valueMap = (HashMap) entry.getValue();
//                if (valueMap != null && valueMap.size() > 0) {
//                    for (Entry valueEntry : valueMap.entrySet()) {
//                        if (witchOne.equals((String) valueEntry.getKey())) {
//                            valueMap.remove(witchOne);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public void wechatViewRever() {
//        removeShareState(this.photoId, WECHAT);
//        clearWechatWeiboQiuQiu(WECHAT);
//        if (this.isChinese) {
//            if (this.share_wechat_layout_ll != null) {
//                this.share_wechat_layout_ll.setEnabled(true);
//                this.share_wechat_layout_ll.setAlpha(1.0f);
//                this.share_wechat_layout_ll.findViewById(2131690607).setVisibility(0);
//                this.share_wechat_layout_ll.findViewById(2131690608).setVisibility(8);
//                ((TextView) this.share_wechat_layout_ll.findViewById(2131690609)).setText(getResourcesString(2131296802));
//            } else {
//                return;
//            }
//        } else if (this.mEnglishWeChatLLayout != null) {
//            this.mEnglishWeChatLLayout.setEnabled(true);
//            this.mEnglishWeChatLLayout.setAlpha(1.0f);
//            this.mEnglishWeChatLLayout.findViewById(2131690650).setVisibility(0);
//            this.mEnglishWeChatLLayout.findViewById(2131690651).setVisibility(8);
//            ((TextView) this.mEnglishWeChatLLayout.findViewById(2131690652)).setText(getResourcesString(2131296802));
//        } else {
//            return;
//        }
//        setAllViewEnabled(null);
//    }
//
//    public void wechatMomentsViewRever() {
//        clearWechatWeiboQiuQiu(WECHATMOMENTS);
//        if (this.isChinese) {
//            if (this.share_wechat_moments_layout_ll != null) {
//                this.share_wechat_moments_layout_ll.setEnabled(true);
//                this.share_wechat_moments_layout_ll.setAlpha(1.0f);
//                this.share_wechat_moments_layout_ll.findViewById(2131690612).setVisibility(0);
//                this.share_wechat_moments_layout_ll.findViewById(2131690613).setVisibility(8);
//                ((TextView) this.share_wechat_moments_layout_ll.findViewById(2131690614)).setText(getResourcesString(2131296797));
//            } else {
//                return;
//            }
//        } else if (this.english_share_wechat_moments_layout_ll != null) {
//            this.english_share_wechat_moments_layout_ll.setEnabled(true);
//            this.english_share_wechat_moments_layout_ll.setAlpha(1.0f);
//            this.english_share_wechat_moments_layout_ll.findViewById(2131690655).setVisibility(0);
//            this.english_share_wechat_moments_layout_ll.findViewById(2131690656).setVisibility(8);
//            ((TextView) this.english_share_wechat_moments_layout_ll.findViewById(2131690657)).setText(getResourcesString(2131296797));
//        } else {
//            return;
//        }
//        setAllViewEnabled(null);
//    }
//
//    public void qqViewRever() {
//        clearWechatWeiboQiuQiu(QIUQIU);
//        if (this.isChinese) {
//            if (this.share_qq_layout_ll != null) {
//                this.share_qq_layout_ll.setEnabled(true);
//                this.share_qq_layout_ll.setAlpha(1.0f);
//                this.share_qq_layout_ll.findViewById(2131690617).setVisibility(0);
//                this.share_qq_layout_ll.findViewById(2131690618).setVisibility(8);
//                ((TextView) this.share_qq_layout_ll.findViewById(2131690619)).setText(getResourcesString(2131296799));
//            } else {
//                return;
//            }
//        } else if (this.mEnglishQQLLayout != null) {
//            this.mEnglishQQLLayout.setEnabled(true);
//            this.mEnglishQQLLayout.setAlpha(1.0f);
//            this.mEnglishQQLLayout.findViewById(2131690660).setVisibility(0);
//            this.mEnglishQQLLayout.findViewById(2131690661).setVisibility(8);
//            ((TextView) this.mEnglishQQLLayout.findViewById(2131690662)).setText(getResourcesString(2131296799));
//        } else {
//            return;
//        }
//        setAllViewEnabled(null);
//    }
//
//    public void weiboViewRever() {
//        clearWechatWeiboQiuQiu(WEIBO);
//        if (this.isChinese) {
//            if (this.share_weibo_layout_ll != null) {
//                this.share_weibo_layout_ll.setEnabled(true);
//                this.share_weibo_layout_ll.setAlpha(1.0f);
//                this.share_weibo_layout_ll.findViewById(2131690071).setVisibility(0);
//                this.share_weibo_layout_ll.findViewById(2131690603).setVisibility(8);
//                ((TextView) this.share_weibo_layout_ll.findViewById(2131690604)).setText(getResourcesString(2131296803));
//            } else {
//                return;
//            }
//        } else if (this.mEnglishWeiboLLayout != null) {
//            this.mEnglishWeiboLLayout.setEnabled(true);
//            this.mEnglishWeiboLLayout.setAlpha(1.0f);
//            this.mEnglishWeiboLLayout.findViewById(2131690645).setVisibility(0);
//            this.mEnglishWeiboLLayout.findViewById(2131690646).setVisibility(8);
//            ((TextView) this.mEnglishWeiboLLayout.findViewById(2131690647)).setText(getResourcesString(2131296803));
//        } else {
//            return;
//        }
//        setAllViewEnabled(null);
//    }
//
//    private void ShareCropDialogClick() {
//        this.dialog_background_view.setVisibility(0);
//    }
//
//    private void ShareCropDialogDismiss() {
//        this.dialog_background_view.setVisibility(8);
//    }
//
//    private void removeShareState(int photoId, String key) {
//        HashMap hashMap1 = ShareModel.getInstance().getShareMap();
//        HashMap<String, Integer> stateMap = (HashMap) hashMap1.get(Integer.valueOf(photoId));
//        if (stateMap != null && stateMap.size() > 0 && stateMap.containsKey(key)) {
//            LogUtil.d("removeShare", "photoId : " + photoId + " , key : " + key);
//            stateMap.remove(key);
//            hashMap1.put(Integer.valueOf(photoId), stateMap);
//            ShareModel.getInstance().setShareMap(hashMap1);
//        }
//    }
//
//    private void updateShareState(int photoId, String key, int value) {
//        HashMap hashMap = ShareModel.getInstance().getShareMap();
//        HashMap<String, Integer> stateMap = new HashMap();
//        stateMap.put(key, Integer.valueOf(value));
//        hashMap.put(Integer.valueOf(photoId), stateMap);
//        ShareModel.getInstance().setShareMap(hashMap);
//    }
//
//    private void sendEmptyMessage(String type, int typeHander, int photoId) {
//        Message msg = new Message();
//        Bundle bundle = new Bundle();
//        bundle.putInt("photoid", photoId);
//        bundle.putString("type", type);
//        msg.what = typeHander;
//        msg.obj = bundle;
//        this.mHandler.sendMessageDelayed(msg, StatisticConfig.MIN_UPLOAD_INTERVAL);
//    }
//
//    private void removeEmptyMessage(int typeHandler) {
//        this.mHandler.removeMessages(typeHandler);
//    }
//
//    public boolean dialogIsShow() {
//        return this.dialog != null && this.dialog.isShowing();
//    }
//
//    public void permissionClickView() {
//        onClick(this.mPermissionView);
//    }
//
//    private static String getShareFormatString(int resId1, int resId2) {
//        Object[] objArr = new Object[2];
//        objArr[0] = getResourcesString(resId1);
//        objArr[1] = getResourcesString(resId2);
//        return String.format("%1$s%2$s", objArr);
//    }
//
//    private static String getResourcesString(int redId) {
//        return App.getResource().getString(redId);
//    }
//
//    private static String getChineseString(int resId) {
//        return App.getZhResource().getString(resId);
//    }
//
//    private static String getEnglishString(int resId) {
//        return App.getEnResource().getString(resId);
//    }
//
//    public void onClick(View view) {
//        if (view != null) {
//            switch (view.getId()) {
//                case 2131690566:
//                    this.isDismiss = true;
//                    this.app_message_ll.clearAnimation();
//                    this.app_message_ll.setVisibility(8);
//                    this.dialog.dismiss();
//                    this.mPermissionView = null;
//                    App.UnregisterEventBus(this);
//                    if (this.onShareSheetItemClickListener != null) {
//                        this.onShareSheetItemClickListener.onCloseClick();
//                        return;
//                    }
//                    return;
//                case 2131690568:
//                    this.isDismiss = true;
//                    this.app_message_ll.clearAnimation();
//                    this.app_message_ll.setVisibility(8);
//                    this.dialog.dismiss();
//                    this.mPermissionView = null;
//                    App.UnregisterEventBus(this);
//                    if (this.onShareSheetItemClickListener != null) {
//                        this.onShareSheetItemClickListener.onCloseClick();
//                        return;
//                    }
//                    return;
//                case 2131690602:
//                case 2131690644:
//                    onShareWeiboClick(view);
//                    return;
//                case 2131690606:
//                case 2131690649:
//                    onShareWechatClick(view);
//                    return;
//                case 2131690611:
//                case 2131690654:
//                    onShareWechatMomentsClick(view);
//                    return;
//                case 2131690616:
//                case 2131690659:
//                    onShareQQClick(view);
//                    return;
//                case 2131690621:
//                    onShareInstagramClick(view);
//                    return;
//                case 2131690625:
//                    onShareTumblrClick(view);
//                    return;
//                case 2131690630:
//                    onShareFacebookClick(view);
//                    return;
//                case 2131690634:
//                    onShareMessengerClick(view);
//                    return;
//                case 2131690639:
//                    onShareTwitterClick(view);
//                    return;
//                case 2131690664:
//                    onShareSystemClick(view);
//                    return;
//                case 2131690668:
//                    ClipboardUtil.copy(getContext(), this.web_url);
//                    if (this.isDismiss) {
//                        EventBus.getDefault().post(new ToastMessageEvent(this.mContext, true, 2131296323));
//                        return;
//                    } else {
//                        showAppMessageSuccess(getResourcesString(2131296323));
//                        return;
//                    }
//                case 2131690673:
//                    onSaveVideoCameraClick(view);
//                    return;
//                case 2131690682:
//                    new DeleteDialog(getContext()).builder().setCanceledOnTouchOutside(false).setOnDeleteClickListener(new OnDeleteClickListener() {
//                        public void onDeleteCancelClick() {
//                        }
//
//                        public void onDeleteConfirmClick() {
//                            ShareActionSheetDialog.this.deleteVidoeInfo();
//                            ShareActionSheetDialog.this.dialog.dismiss();
//                            ShareActionSheetDialog.this.mPermissionView = null;
//                            App.UnregisterEventBus(ShareActionSheetDialog.this);
//                            if (ShareActionSheetDialog.this.onShareSheetItemClickListener != null) {
//                                ShareActionSheetDialog.this.onShareSheetItemClickListener.onDeleteClick(ShareActionSheetDialog.this.mVideoFilePath);
//                            }
//                        }
//                    }).show();
//                    return;
//                default:
//                    return;
//            }
//        }
//    }
//
//    public void savePhotoViewRever() {
//        if (this.save_video_camera_layout_ll != null) {
//            this.save_video_camera_layout_ll.setEnabled(true);
//            this.save_video_camera_layout_ll.setAlpha(1.0f);
//            this.save_video_camera_layout_ll.findViewById(2131690674).setVisibility(0);
//            this.save_video_camera_layout_ll.findViewById(2131690675).setVisibility(8);
//            ((TextView) this.save_video_camera_layout_ll.findViewById(2131690676)).setText(getResourcesString(2131296490));
//            setAllViewEnabled(null);
//        }
//    }
//
//    private void onSaveVideoCameraClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            setAllViewDisabled(view);
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1) {
//                updateShareState(this.photoId, SAVEPHOTO, 1);
//                view.findViewById(2131690674).setVisibility(4);
//                view.findViewById(2131690675).setVisibility(0);
//                ((TextView) view.findViewById(2131690676)).setText(getResourcesString(2131296490) + getResourcesString(2131296494));
//                PhotoUtil.savePhotoToLocal(this.mContext, FrescoHelper.getBitmapFromFrescoCache(ImageUtil.getImageTypeUrl(this.preview_url), this), Config.getPhotoSavePath() + String.valueOf(System.currentTimeMillis()) + ".jpg", needWatermark, true, new SavePhotoListener() {
//                    public void onSaveFinished(String timeStamp) {
//                        ShareActionSheetDialog.this.removeShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.SAVEPHOTO);
//                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.SAVEPHOTO, 3, 1));
//                        if (ShareActionSheetDialog.this.getContext() != null) {
//                            ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$6$.Lambda
//                            .1.lambdaFactory$(this));
//                        }
//                    }
//
//                    private /* synthetic */ void lambda$onSaveFinished$0() {
//                        if (ShareActionSheetDialog.this.isDismiss) {
//                            EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296311));
//                        } else {
//                            ShareActionSheetDialog.this.showAppMessageSuccess(ShareActionSheetDialog.getResourcesString(2131296311));
//                        }
//                        ShareActionSheetDialog.this.savePhotoViewRever();
//                    }
//
//                    public void onSaveFailed(String timeStamp) {
//                    }
//                });
//                return;
//            }
//            PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                public void run() {
//                    ShareActionSheetDialog.this.saveMp4ToSD(true, new OnHandlerMp4ToGifListener() {
//                        public void onStart() {
//                            if (ShareActionSheetDialog.this.getContext() != null) {
//                                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$7$1$.Lambda
//                                .1.lambdaFactory$(this, view));
//                            }
//                        }
//
//                        private /* synthetic */ void lambda$onStart$0(View view) {
//                            ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.SAVEPHOTO, 1);
//                            view.findViewById(2131690674).setVisibility(4);
//                            view.findViewById(2131690675).setVisibility(0);
//                            ((TextView) view.findViewById(2131690676)).setText(ShareActionSheetDialog.getResourcesString(2131296490) + ShareActionSheetDialog.getResourcesString(2131296494));
//                        }
//
//                        public void onFinish(String path, final int photoId) {
//                            PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                                public void run() {
//                                    ShareActionSheetDialog.this.mp4ToGifForSave(new OnHandlerMp4ToGifListener() {
//                                        public void onStart() {
//                                        }
//
//                                        public void onFinish(String path1, int photoId) {
//                                            ShareActionSheetDialog.this.removeShareState(photoId, ShareActionSheetDialog.SAVEPHOTO);
//                                            EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.SAVEPHOTO, 3, 1));
//                                            if (ShareActionSheetDialog.this.getContext() != null) {
//                                                ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$7$1$1$1$.Lambda
//                                                .1.lambdaFactory$(this));
//                                            }
//                                        }
//
//                                        private /* synthetic */ void lambda$onFinish$0() {
//                                            if (ShareActionSheetDialog.this.isDismiss) {
//                                                EventBus.getDefault().post(new ToastMessageEvent(ShareActionSheetDialog.this.mContext, true, 2131296311));
//                                            } else {
//                                                ShareActionSheetDialog.this.showAppMessageSuccess(ShareActionSheetDialog.getResourcesString(2131296311));
//                                            }
//                                            ShareActionSheetDialog.this.savePhotoViewRever();
//                                        }
//
//                                        public void onFinish(String path) {
//                                        }
//                                    }, photoId, needWatermark, false);
//                                }
//                            });
//                        }
//
//                        public void onFinish(String path) {
//                        }
//                    }, ShareActionSheetDialog.this.photoId, needWatermark);
//                }
//            });
//            return;
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private void onShareTumblrCancel(String path, int photoId) {
//        File file = new File(path);
//        if (file.exists()) {
//            file.delete();
//        }
//        new Handler(Looper.getMainLooper()).post(ShareActionSheetDialog$.Lambda
//        .2.lambdaFactory$(this, photoId));
//    }
//
//    private /* synthetic */ void lambda$onShareTumblrCancel$1(int photoId) {
//        removeShareState(photoId, TUMBLR);
//        removeEmptyMessage(6);
//        EventBus.getDefault().post(new ChangeShareEvent(photoId, TUMBLR, 3, 3));
//        tumblrViewRever();
//    }
//
//    private void tumblrViewRever() {
//        if (this.mShareTumblrLinearLayout != null) {
//            this.mShareTumblrLinearLayout.setEnabled(true);
//            this.mShareTumblrLinearLayout.setAlpha(1.0f);
//            this.mShareTumblrLinearLayout.findViewById(2131690626).setVisibility(0);
//            this.mShareTumblrLinearLayout.findViewById(2131690627).setVisibility(8);
//            ((TextView) this.mShareTumblrLinearLayout.findViewById(2131690628)).setText(getResourcesString(2131296800));
//            setAllViewEnabled(null);
//        }
//    }
//
//    private void onShareTumblrClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            String text;
//            this.mPermissionView = null;
//            String resourcesString;
//            Object[] objArr;
//            StringBuilder stringBuilder;
//            String resourcesString2;
//            if (this.mIsGyphy) {
//                Object[] objArr2;
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        text = this.mPhotoText;
//                    } else {
//                        resourcesString = getResourcesString(2131296981);
//                        objArr2 = new Object[1];
//                        objArr2[0] = getOneShareTypeString(this.mShareType);
//                        text = String.format(resourcesString, objArr2);
//                    }
//                } else if (this.isChinese) {
//                    resourcesString = getResourcesString(2131296983);
//                    objArr2 = new Object[2];
//                    objArr2[0] = this.mUserName;
//                    objArr2[1] = getOneShareTypeString(this.mShareType);
//                    text = String.format(resourcesString, objArr2);
//                } else {
//                    resourcesString = getResourcesString(2131296983);
//                    objArr2 = new Object[2];
//                    objArr2[0] = getOneShareTypeString(this.mShareType);
//                    objArr2[1] = this.mUserName;
//                    text = String.format(resourcesString, objArr2);
//                }
//            } else if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                if (this.mShareType == 2) {
//                    text = getResourcesString(2131296980) + this.mOtherText;
//                } else {
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        resourcesString = this.mPhotoText;
//                    } else {
//                        resourcesString = getResourcesString(2131296979);
//                        objArr = new Object[1];
//                        objArr[0] = getOneShareTypeString(this.mShareType);
//                        resourcesString = String.format(resourcesString, objArr);
//                    }
//                    text = stringBuilder2.append(resourcesString).append(this.mOtherText).toString();
//                }
//            } else if (this.isChinese) {
//                stringBuilder = new StringBuilder();
//                resourcesString2 = getResourcesString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = this.mUserName;
//                objArr[1] = getOneShareTypeString(this.mShareType);
//                text = stringBuilder.append(String.format(resourcesString2, objArr)).append(this.mOtherText).toString();
//            } else {
//                stringBuilder = new StringBuilder();
//                resourcesString2 = getResourcesString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = getOneShareTypeString(this.mShareType);
//                objArr[1] = this.mUserName;
//                text = stringBuilder.append(String.format(resourcesString2, objArr)).append(this.mOtherText).toString();
//            }
//            view.setEnabled(false);
//            ShareCropDialogClick();
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1 || this.mShareType == 2) {
//                ShareViewHelper.showShareNewEditPageDialog(getContext(), text, false, new OnShareEditPageClickListener() {
//                    public void onShareEditPageCancelClick() {
//                        view.setEnabled(true);
//                        if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                            ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//                        }
//                    }
//
//                    public void onShareEditPageConfirmClick(String content) {
//                        ShareActionSheetDialog.this.setAllViewDisabled(view);
//                        ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 2);
//                        ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.TUMBLR, 6, ShareActionSheetDialog.this.photoId);
//                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TUMBLR, 2, 3));
//                        ((TextView) view.findViewById(2131690628)).setText(ShareActionSheetDialog.getShareFormatString(2131296800, 2131296496));
//                        if (ShareActionSheetDialog.this.mShareType == 2) {
//                            ShareUtils.shareVideoToTumblrGetAccountCheck(ShareActionSheetDialog.this.getContext(), content, ShareActionSheetDialog.this.web_url, new TumblrGetAccountCheckListener(content, ShareActionSheetDialog.this.mVideoFilePath));
//                            return;
//                        }
//                        String filePath = Config.getPhotoCachePath();
//                        ShareActionSheetDialog.this.getBitmapPathStartShare(ShareActionSheetDialog.TUMBLR, filePath, new TumblrGetAccountCheckListener(content, filePath), null, content, needWatermark);
//                    }
//                }, 3.lambdaFactory$(this, view));
//                return;
//            }
//            setAllViewDisabled(view);
//            updateShareState(this.photoId, TUMBLR, 1);
//            sendEmptyMessage(TUMBLR, 6, this.photoId);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, TUMBLR, 1, 3));
//            ShareViewHelper.showShareCropDialogWithFilmSubtitles(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, true, false, new TumblrShareCropClickListener(view, text), 4.lambdaFactory$(this, view));
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareCropClick();
//                return;
//            }
//            return;
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private /* synthetic */ void lambda$onShareTumblrClick$2(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private /* synthetic */ void lambda$onShareTumblrClick$3(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//            removeShareState(this.photoId, TUMBLR);
//            removeEmptyMessage(6);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, TUMBLR, 3, 2));
//            tumblrViewRever();
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private void onShareTwitterCancel(String path, int photoId, boolean isVideo) {
//        File file = new File(path);
//        if (file.exists() && !isVideo) {
//            file.delete();
//        }
//        App.runOnUiThread(5.lambdaFactory$(this, photoId));
//    }
//
//    private /* synthetic */ void lambda$onShareTwitterCancel$4(int photoId) {
//        removeShareState(photoId, TWITTER);
//        removeEmptyMessage(3);
//        EventBus.getDefault().post(new ChangeShareEvent(photoId, TWITTER, 3, 3));
//        if (this.isDismiss) {
//            EventBus.getDefault().post(new ToastMessageEvent(this.mContext, false, 2131296801));
//        } else {
//            String resourcesString = getResourcesString(2131296978);
//            Object[] objArr = new Object[1];
//            objArr[0] = getResourcesString(2131296801);
//            showAppMessageAlert(String.format(resourcesString, objArr));
//        }
//        setAllViewEnabled(null);
//        twitterViewRever();
//    }
//
//    private void twitterViewRever() {
//        if (this.share_twitter_layout_ll != null) {
//            setAllViewEnabled(null);
//            this.share_twitter_layout_ll.findViewById(2131690085).setVisibility(0);
//            this.share_twitter_layout_ll.findViewById(2131690640).setVisibility(8);
//            ((TextView) this.share_twitter_layout_ll.findViewById(2131690641)).setText(getResourcesString(2131296801));
//        }
//    }
//
//    private void onShareTwitterClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            String text;
//            this.mPermissionView = null;
//            String englishString;
//            StringBuilder stringBuilder;
//            String englishString2;
//            Object[] objArr;
//            if (this.mIsGyphy) {
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        text = this.mPhotoText;
//                    } else {
//                        englishString = getEnglishString(2131296981);
//                        Object[] objArr2 = new Object[1];
//                        objArr2[0] = getENOneShareTypeString(this.mShareType);
//                        text = String.format(englishString, objArr2);
//                    }
//                } else if (TextUtil.isValidate(this.mTwitterName)) {
//                    stringBuilder = new StringBuilder();
//                    englishString2 = getEnglishString(2131296983);
//                    objArr = new Object[2];
//                    objArr[0] = getENOneShareTypeString(this.mShareType);
//                    objArr[1] = "@" + this.mTwitterName;
//                    text = stringBuilder.append(String.format(englishString2, objArr)).append(this.mEnOtherText).toString();
//                } else {
//                    stringBuilder = new StringBuilder();
//                    englishString2 = getEnglishString(2131296983);
//                    objArr = new Object[2];
//                    objArr[0] = getENOneShareTypeString(this.mShareType);
//                    objArr[1] = this.mUserName;
//                    text = stringBuilder.append(String.format(englishString2, objArr)).append(this.mEnOtherText).toString();
//                }
//            } else if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                if (this.mShareType == 2) {
//                    text = getEnglishString(2131296980) + this.mEnOtherText;
//                } else {
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        englishString = this.mPhotoText;
//                    } else {
//                        englishString = getEnglishString(2131296979);
//                        objArr = new Object[1];
//                        objArr[0] = getENOneShareTypeString(this.mShareType);
//                        englishString = String.format(englishString, objArr);
//                    }
//                    text = stringBuilder2.append(englishString).append(this.mEnOtherText).toString();
//                }
//            } else if (TextUtil.isValidate(this.mTwitterName)) {
//                stringBuilder = new StringBuilder();
//                englishString2 = getEnglishString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = getENOneShareTypeString(this.mShareType);
//                objArr[1] = "@" + this.mTwitterName;
//                text = stringBuilder.append(String.format(englishString2, objArr)).append(this.mEnOtherText).toString();
//            } else {
//                stringBuilder = new StringBuilder();
//                englishString2 = getEnglishString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = getENOneShareTypeString(this.mShareType);
//                objArr[1] = this.mUserName;
//                text = stringBuilder.append(String.format(englishString2, objArr)).append(this.mEnOtherText).toString();
//            }
//            view.setEnabled(false);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, TWITTER, 1, 3));
//            updateShareState(this.photoId, TWITTER, 1);
//            setAllViewDisabled(view);
//            if (this.mShareType == 2) {
//                String officialTagNameString;
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    officialTagNameString = getTwitterOfficialTagNameString(this.ots);
//                } else {
//                    officialTagNameString = "";
//                }
//                String shareText = text + "   " + this.web_url + "   " + getTwitterTagAppNameString() + " " + officialTagNameString;
//                ShareUtils.shareVideoToTwitter(getContext(), shareText, new TwitterCheckAccountPlatformActionListener(shareText, "", true));
//                return;
//            }
//            ShareCropDialogClick();
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1) {
//                ShareViewHelper.showShareNewEditPageDialog(getContext(), text, false, new OnShareEditPageClickListener() {
//                    public void onShareEditPageCancelClick() {
//                        view.setEnabled(true);
//                        ShareActionSheetDialog.this.finishTwitterViewRever(view);
//                        if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                            ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//                        }
//                    }
//
//                    public void onShareEditPageConfirmClick(String content) {
//                        String officialTagNameString;
//                        ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER, 2);
//                        ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.TWITTER, 3, ShareActionSheetDialog.this.photoId);
//                        EventBus.getDefault().post(new ChangeShareEvent(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.TWITTER, 2, 3));
//                        ((TextView) view.findViewById(2131690641)).setText(ShareActionSheetDialog.getShareFormatString(2131296801, 2131296496));
//                        String filePath = Config.getPhotoCachePath();
//                        if (TextUtils.equals(ShareActionSheetDialog.this.mUserName, GlobalHelper.getUserScreenName())) {
//                            officialTagNameString = ShareActionSheetDialog.this.getTwitterOfficialTagNameString(ShareActionSheetDialog.this.ots);
//                        } else {
//                            officialTagNameString = "";
//                        }
//                        String text = content + "   " + ShareActionSheetDialog.this.web_url + "   " + ShareActionSheetDialog.this.getTwitterTagAppNameString() + " " + officialTagNameString;
//                        ShareActionSheetDialog.this.getBitmapPathStartShare(ShareActionSheetDialog.TWITTER, filePath, new TwitterCheckAccountPlatformActionListener(text, filePath, false), null, text, needWatermark);
//                    }
//                }, 6.lambdaFactory$(this, view));
//                return;
//            }
//            ShareViewHelper.showShareCropDialogWithFilmSubtitles(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, true, false, new TwitterShareCropClickListener(view, text), 7.lambdaFactory$(this, view));
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareCropClick();
//                return;
//            }
//            return;
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private /* synthetic */ void lambda$onShareTwitterClick$5(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private /* synthetic */ void lambda$onShareTwitterClick$6(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            removeShareState(this.photoId, TWITTER);
//            removeEmptyMessage(3);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, TWITTER, 3, 2));
//            twitterViewRever();
//            view.setEnabled(true);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    public void messengerViewRever() {
//        if (this.share_messenger_layout_ll != null) {
//            this.share_messenger_layout_ll.setEnabled(true);
//            this.share_messenger_layout_ll.setAlpha(1.0f);
//            this.share_messenger_layout_ll.findViewById(2131690635).setVisibility(0);
//            this.share_messenger_layout_ll.findViewById(2131690636).setVisibility(8);
//            ((TextView) this.share_messenger_layout_ll.findViewById(2131690637)).setText(getResourcesString(2131296795));
//            removeShareState(this.photoId, MESSENGER);
//            setAllViewEnabled(null);
//        }
//    }
//
//    private void onShareMessengerClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            setAllViewDisabled(view);
//            if (this.mShareType == 1 || this.mShareType == 2) {
//                updateShareState(this.photoId, MESSENGER, 1);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, MESSENGER, 1, 3));
//                ((TextView) view.findViewById(2131690637)).setText(getShareFormatString(2131296795, 2131296496));
//                if (this.mShareType == 2) {
//                    setAllViewDisabled(view);
//                    shareLinkTo(MESSENGER);
//                    messengerViewRever();
//                    removeShareState(this.photoId, MESSENGER);
//                    EventBus.getDefault().post(new ChangeShareEvent(this.photoId, MESSENGER, 3, 3));
//                    setAllViewEnabled(null);
//                    return;
//                }
//                getBitmapPathStartShare(MESSENGER, Config.getPhotoCachePath(), null, false);
//                return;
//            }
//            ShareCropDialogClick();
//            ShareViewHelper.showShareCropDialog(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, true, false, new OnShareCropClickListener() {
//                public void onShareCropCancelClick() {
//                    view.setEnabled(true);
//                    ShareActionSheetDialog.this.messengerViewRever();
//                    if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                        ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//                    }
//                }
//
//                public void onShareCropDismiss() {
//                }
//
//                public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
//                    final String str = addText;
//                    final boolean z = isCrop;
//                    final float f = startScale;
//                    PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                        public void run() {
//                            ShareActionSheetDialog.this.mp4ToGif(true, str, z, f, new OnHandlerMp4ToGifListener() {
//                                public void onStart() {
//                                    if (ShareActionSheetDialog.this.getContext() != null) {
//                                        ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$10$1$1$.Lambda
//                                        .1.lambdaFactory$(this, view));
//                                    }
//                                }
//
//                                private /* synthetic */ void lambda$onStart$0(View view) {
//                                    ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.MESSENGER, 1);
//                                    view.findViewById(2131690635).setVisibility(4);
//                                    view.findViewById(2131690636).setVisibility(0);
//                                    ((TextView) view.findViewById(2131690637)).setText(ShareActionSheetDialog.getShareFormatString(2131296795, 2131296494));
//                                }
//
//                                public void onFinish(String path, int photoId) {
//                                    if (ShareActionSheetDialog.this.getContext() != null) {
//                                        ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$10$1$1$.Lambda
//                                        .2.lambdaFactory$(this, photoId, view, path));
//                                    }
//                                }
//
//                                private /* synthetic */ void lambda$onFinish$1(int photoId, View view, String path) {
//                                    ShareActionSheetDialog.this.updateShareState(photoId, ShareActionSheetDialog.MESSENGER, 2);
//                                    EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.MESSENGER, 2, 3));
//                                    ((TextView) view.findViewById(2131690637)).setText(ShareActionSheetDialog.getShareFormatString(2131296795, 2131296496));
//                                    ShareActionSheetDialog.this.shareTo(path, ShareActionSheetDialog.getResourcesString(2131296817));
//                                    ShareActionSheetDialog.this.messengerViewRever();
//                                    ShareActionSheetDialog.this.removeShareState(photoId, ShareActionSheetDialog.MESSENGER);
//                                    EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.MESSENGER, 3, 3));
//                                }
//
//                                public void onFinish(String path) {
//                                }
//                            }, ShareActionSheetDialog.this.photoId, false, false);
//                        }
//                    });
//                    if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                        ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//                    }
//                }
//            }, 8.lambdaFactory$(this, view));
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareCropClick();
//                return;
//            }
//            return;
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private /* synthetic */ void lambda$onShareMessengerClick$7(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private void faceBookViewRever() {
//        if (this.share_facebook_layout_ll != null) {
//            this.share_facebook_layout_ll.setEnabled(true);
//            this.share_facebook_layout_ll.setAlpha(1.0f);
//            this.share_facebook_layout_ll.findViewById(2131690082).setVisibility(0);
//            this.share_facebook_layout_ll.findViewById(2131690631).setVisibility(8);
//            ((TextView) this.share_facebook_layout_ll.findViewById(2131690632)).setText(getResourcesString(2131296794));
//        }
//    }
//
//    private void onShareFacebookClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1) {
//                updateShareState(this.photoId, FACEBOOK, 2);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, FACEBOOK, 2, 3));
//                ((TextView) view.findViewById(2131690632)).setText(getShareFormatString(2131296794, 2131296494));
//                getBitmapPathStartShare(FACEBOOK, Config.getPhotoCachePath(), null, needWatermark);
//                return;
//            } else if (this.mShareType == 2) {
//                updateShareState(this.photoId, FACEBOOK, 1);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, FACEBOOK, 1, 3));
//                setAllViewDisabled(view);
//                Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(ImageUtil.getPreviewUrl(this.preview_url), this);
//                shareLinkTo(FACEBOOK);
//                messengerViewRever();
//                removeShareState(this.photoId, FACEBOOK);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, FACEBOOK, 3, 3));
//                setAllViewEnabled(null);
//                return;
//            } else {
//                PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                    public void run() {
//                        ShareActionSheetDialog.this.saveMp4ToSD(new OnHandlerMp4ToGifListener() {
//                            public void onStart() {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$11$1$.Lambda
//                                    .1.lambdaFactory$(this, view));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onStart$0(View view) {
//                                ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.FACEBOOK, 1);
//                                view.findViewById(2131690082).setVisibility(4);
//                                view.findViewById(2131690631).setVisibility(0);
//                                TextView share_facebook_tv = (TextView) view.findViewById(2131690632);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296794);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296494);
//                                share_facebook_tv.setText(String.format("%1$s%2$s", objArr));
//                            }
//
//                            public void onFinish(String path, int photoId) {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$11$1$.Lambda
//                                    .2.lambdaFactory$(this, photoId, view, path));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onFinish$1(int photoId, View view, String path) {
//                                ShareActionSheetDialog.this.updateShareState(photoId, ShareActionSheetDialog.FACEBOOK, 2);
//                                EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.FACEBOOK, 2, 3));
//                                TextView share_facebook_tv = (TextView) view.findViewById(2131690632);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296794);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296496);
//                                share_facebook_tv.setText(String.format("%1$s%2$s", objArr));
//                                ShareActionSheetDialog.this.shareTo(path, ShareActionSheetDialog.getResourcesString(2131296817));
//                                ShareActionSheetDialog.this.faceBookViewRever();
//                                ShareActionSheetDialog.this.removeShareState(photoId, ShareActionSheetDialog.FACEBOOK);
//                                EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.FACEBOOK, 3, 3));
//                            }
//
//                            public void onFinish(String path) {
//                            }
//                        }, ShareActionSheetDialog.this.photoId, needWatermark);
//                    }
//                });
//                return;
//            }
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private void systemViewRever() {
//        if (this.mSystemLinearLayout != null) {
//            this.mSystemLinearLayout.setEnabled(true);
//            this.mSystemLinearLayout.setAlpha(1.0f);
//            this.mSystemLinearLayout.findViewById(2131690665).setVisibility(0);
//            this.mSystemLinearLayout.findViewById(2131690666).setVisibility(8);
//            ((TextView) this.mSystemLinearLayout.findViewById(2131690667)).setText(getResourcesString(2131296519));
//        }
//    }
//
//    private void onShareSystemClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1) {
//                updateShareState(this.photoId, SYSTEM, 2);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 2, 3));
//                ((TextView) view.findViewById(2131690667)).setText(getShareFormatString(2131296519, 2131296494));
//                getBitmapPathStartShare(SYSTEM, Config.getPhotoCachePath(), null, needWatermark);
//                return;
//            } else if (this.mShareType == 2) {
//                updateShareState(this.photoId, SYSTEM, 2);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 2, 3));
//                TextView mSystemTextView = (TextView) view.findViewById(2131690667);
//                mSystemTextView.setText(getShareFormatString(2131296519, 2131296494));
//                shareLinkTo(SYSTEM);
//                updateShareState(this.photoId, SYSTEM, 2);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 2, 3));
//                Object[] objArr = new Object[2];
//                objArr[0] = getResourcesString(2131296519);
//                objArr[1] = getResourcesString(2131296496);
//                mSystemTextView.setText(String.format("%1$s%2$s", objArr));
//                systemViewRever();
//                removeShareState(this.photoId, SYSTEM);
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 3, 3));
//                return;
//            } else {
//                PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                    public void run() {
//                        ShareActionSheetDialog.this.mp4ToGif("", false, 1.0f, new OnHandlerMp4ToGifListener() {
//                            public void onStart() {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$12$1$.Lambda
//                                    .1.lambdaFactory$(this, view));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onStart$0(View view) {
//                                ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.SYSTEM, 1);
//                                view.findViewById(2131690665).setVisibility(4);
//                                view.findViewById(2131690666).setVisibility(0);
//                                TextView mSystemTextView = (TextView) view.findViewById(2131690667);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296519);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296494);
//                                mSystemTextView.setText(String.format("%1$s%2$s", objArr));
//                            }
//
//                            public void onFinish(String path, int photoId) {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$12$1$.Lambda
//                                    .2.lambdaFactory$(this, photoId, view, path));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onFinish$1(int photoId, View view, String path) {
//                                ShareActionSheetDialog.this.updateShareState(photoId, ShareActionSheetDialog.SYSTEM, 2);
//                                EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.SYSTEM, 2, 3));
//                                TextView mSystemTextView = (TextView) view.findViewById(2131690667);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296519);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296496);
//                                mSystemTextView.setText(String.format("%1$s%2$s", objArr));
//                                ShareActionSheetDialog.this.shareTo(path);
//                                ShareActionSheetDialog.this.systemViewRever();
//                                ShareActionSheetDialog.this.removeShareState(photoId, ShareActionSheetDialog.SYSTEM);
//                                EventBus.getDefault().post(new ChangeShareEvent(photoId, ShareActionSheetDialog.SYSTEM, 3, 3));
//                            }
//
//                            public void onFinish(String path) {
//                            }
//                        }, ShareActionSheetDialog.this.photoId, needWatermark, false);
//                    }
//                });
//                return;
//            }
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    /* JADX WARNING: Missing block: B:15:0x007f, code:
//            if (r9.equals(FACEBOOK) != false) goto L_0x0022;
//     */
//    private void shareLinkTo(java.lang.String r9) {
//        /*
//        r8 = this;
//        r4 = 2;
//        r3 = 1;
//        r1 = 0;
//        r2 = r8.mUserName;
//        r5 = com.blink.academy.onetake.support.helper.GlobalHelper.getUserScreenName();
//        r2 = android.text.TextUtils.equals(r2, r5);
//        if (r2 == 0) goto L_0x0041;
//    L_0x000f:
//        r2 = r8.mTitle;
//        r2 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r2);
//        if (r2 == 0) goto L_0x0026;
//    L_0x0017:
//        r0 = r8.mTitle;
//    L_0x0019:
//        r2 = -1;
//        r5 = r9.hashCode();
//        switch(r5) {
//            case -1436108013: goto L_0x0082;
//            case -887328209: goto L_0x008c;
//            case 497130182: goto L_0x0079;
//            default: goto L_0x0021;
//        };
//    L_0x0021:
//        r1 = r2;
//    L_0x0022:
//        switch(r1) {
//            case 0: goto L_0x0096;
//            case 1: goto L_0x00a8;
//            case 2: goto L_0x00c6;
//            default: goto L_0x0025;
//        };
//    L_0x0025:
//        return;
//    L_0x0026:
//        r2 = new java.lang.StringBuilder;
//        r2.<init>();
//        r5 = 2131296980; // 0x7f0902d4 float:1.8211892E38 double:1.053000619E-314;
//        r5 = getResourcesString(r5);
//        r2 = r2.append(r5);
//        r5 = r8.mOtherText;
//        r2 = r2.append(r5);
//        r0 = r2.toString();
//        goto L_0x0019;
//    L_0x0041:
//        r2 = r8.mTitle;
//        r2 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r2);
//        if (r2 == 0) goto L_0x004c;
//    L_0x0049:
//        r0 = r8.mTitle;
//        goto L_0x0019;
//    L_0x004c:
//        r2 = new java.lang.StringBuilder;
//        r2.<init>();
//        r5 = 2131296983; // 0x7f0902d7 float:1.8211898E38 double:1.0530006204E-314;
//        r5 = getResourcesString(r5);
//        r6 = new java.lang.Object[r4];
//        r7 = r8.mUserName;
//        r6[r1] = r7;
//        r7 = r8.mShareType;
//        r7 = r8.getOneShareTypeString(r7);
//        r6[r3] = r7;
//        r5 = java.lang.String.format(r5, r6);
//        r2 = r2.append(r5);
//        r5 = r8.mOtherText;
//        r2 = r2.append(r5);
//        r0 = r2.toString();
//        goto L_0x0019;
//    L_0x0079:
//        r3 = "facebook";
//        r3 = r9.equals(r3);
//        if (r3 == 0) goto L_0x0021;
//    L_0x0081:
//        goto L_0x0022;
//    L_0x0082:
//        r1 = "messenger";
//        r1 = r9.equals(r1);
//        if (r1 == 0) goto L_0x0021;
//    L_0x008a:
//        r1 = r3;
//        goto L_0x0022;
//    L_0x008c:
//        r1 = "system";
//        r1 = r9.equals(r1);
//        if (r1 == 0) goto L_0x0021;
//    L_0x0094:
//        r1 = r4;
//        goto L_0x0022;
//    L_0x0096:
//        r1 = r8.getContext();
//        r2 = r8.web_url;
//        r3 = r8.preview_url;
//        r4 = new com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$13;
//        r4.<init>();
//        com.blink.academy.onetake.support.share.ShareUtils.shareLinkToFacebook(r1, r2, r0, r3, r4);
//        goto L_0x0025;
//    L_0x00a8:
//        r1 = new java.lang.StringBuilder;
//        r1.<init>();
//        r1 = r1.append(r0);
//        r2 = " ";
//        r1 = r1.append(r2);
//        r2 = r8.web_url;
//        r1 = r1.append(r2);
//        r1 = r1.toString();
//        r8.shareLink(r1);
//        goto L_0x0025;
//    L_0x00c6:
//        r1 = new java.lang.StringBuilder;
//        r1.<init>();
//        r1 = r1.append(r0);
//        r2 = " ";
//        r1 = r1.append(r2);
//        r2 = r8.web_url;
//        r1 = r1.append(r2);
//        r1 = r1.toString();
//        r8.shareLink(r1);
//        goto L_0x0025;
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.shareLinkTo(java.lang.String):void");
//    }
//
//    private void onShareWeiboClick(View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            String text;
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, WEIBO, 1, 3));
//            updateShareState(this.photoId, WEIBO, 1);
//            setAllViewDisabled(view);
//            this.mPermissionView = null;
//            String chineseString;
//            Object[] objArr;
//            StringBuilder stringBuilder;
//            String chineseString2;
//            if (this.mIsGyphy) {
//                Object[] objArr2;
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        text = this.mPhotoText;
//                    } else {
//                        chineseString = getChineseString(2131296981);
//                        objArr2 = new Object[1];
//                        objArr2[0] = getZHOneShareTypeString(this.mShareType);
//                        text = String.format(chineseString, objArr2);
//                    }
//                } else if (TextUtil.isValidate(this.mWeiboName)) {
//                    chineseString = getChineseString(2131296983);
//                    objArr2 = new Object[2];
//                    objArr2[0] = "@" + this.mWeiboName;
//                    objArr2[1] = getZHOneShareTypeString(this.mShareType);
//                    text = String.format(chineseString, objArr2);
//                } else {
//                    chineseString = getChineseString(2131296983);
//                    objArr2 = new Object[2];
//                    objArr2[0] = this.mUserName;
//                    objArr2[1] = getZHOneShareTypeString(this.mShareType);
//                    text = String.format(chineseString, objArr2);
//                }
//            } else if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                if (this.mShareType == 2) {
//                    text = getChineseString(2131296980) + this.mZhOtherText;
//                } else {
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    if (TextUtil.isValidate(this.mPhotoText)) {
//                        chineseString = this.mPhotoText;
//                    } else {
//                        chineseString = getChineseString(2131296979);
//                        objArr = new Object[1];
//                        objArr[0] = getZHOneShareTypeString(this.mShareType);
//                        chineseString = String.format(chineseString, objArr);
//                    }
//                    text = stringBuilder2.append(chineseString).append(this.mZhOtherText).toString();
//                }
//            } else if (TextUtil.isValidate(this.mWeiboName)) {
//                stringBuilder = new StringBuilder();
//                chineseString2 = getChineseString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = "@" + this.mWeiboName;
//                objArr[1] = getZHOneShareTypeString(this.mShareType);
//                text = stringBuilder.append(String.format(chineseString2, objArr)).append(this.mZhOtherText).toString();
//            } else {
//                stringBuilder = new StringBuilder();
//                chineseString2 = getChineseString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = this.mUserName;
//                objArr[1] = getZHOneShareTypeString(this.mShareType);
//                text = stringBuilder.append(String.format(chineseString2, objArr)).append(this.mZhOtherText).toString();
//            }
//            view.setEnabled(false);
//            ShareCropDialogClick();
//            List<Entity> hashtagEntityList = new Extractor().extractHashtagsWithIndices(text);
//            StringBuilder stringBuilder3 = new StringBuilder(text);
//            if (hashtagEntityList.size() > 0) {
//                for (int i = hashtagEntityList.size() - 1; i > -1; i--) {
//                    stringBuilder3.insert(((Entity) hashtagEntityList.get(i)).getEnd().intValue(), "#");
//                }
//            }
//            showShareCropDialogWithFilmSubtitles(view, stringBuilder3.toString());
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareCropClick();
//                return;
//            }
//            return;
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    public void instagramViewRever() {
//        if (this.share_instagram_layout_ll != null) {
//            this.share_instagram_layout_ll.setEnabled(true);
//            this.share_instagram_layout_ll.setAlpha(1.0f);
//            this.share_instagram_layout_ll.findViewById(2131690079).setVisibility(0);
//            this.share_instagram_layout_ll.findViewById(2131690622).setVisibility(8);
//            ((TextView) this.share_instagram_layout_ll.findViewById(2131690623)).setText(getResourcesString(2131296796));
//        }
//    }
//
//    private void onShareInstagramClick(final View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            final boolean needWatermark = isNeedWatermark();
//            if (this.mShareType == 1) {
//                updateShareState(this.photoId, "instagram", 2);
//                ((TextView) view.findViewById(2131690623)).setText(getShareFormatString(2131296796, 2131296494));
//                getBitmapPathStartShare("instagram", Config.getPhotoCachePath(), null, needWatermark);
//                return;
//            } else if (this.mShareType == 2) {
//                setAllViewDisabled(view);
//                shareLinkTo("instagram");
//                messengerViewRever();
//                removeShareState(this.photoId, "instagram");
//                EventBus.getDefault().post(new ChangeShareEvent(this.photoId, "instagram", 3, 3));
//                setAllViewEnabled(null);
//                return;
//            } else {
//                PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                    public void run() {
//                        ShareActionSheetDialog.this.saveMp4ToSD(new OnHandlerMp4ToGifListener() {
//                            public void onStart() {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$14$1$.Lambda
//                                    .1.lambdaFactory$(this, view));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onStart$0(View view) {
//                                ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, "instagram", 1);
//                                view.findViewById(2131690079).setVisibility(4);
//                                view.findViewById(2131690622).setVisibility(0);
//                                TextView share_instagram_tv = (TextView) view.findViewById(2131690623);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296796);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296494);
//                                share_instagram_tv.setText(String.format("%1$s%2$s", objArr));
//                            }
//
//                            public void onFinish(String path, int photoId) {
//                                if (ShareActionSheetDialog.this.getContext() != null) {
//                                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$14$1$.Lambda
//                                    .2.lambdaFactory$(this, photoId, view, path));
//                                }
//                            }
//
//                            private /* synthetic */ void lambda$onFinish$1(int photoId, View view, String path) {
//                                ShareActionSheetDialog.this.updateShareState(photoId, "instagram", 2);
//                                TextView share_instagram_tv = (TextView) view.findViewById(2131690623);
//                                Object[] objArr = new Object[2];
//                                objArr[0] = ShareActionSheetDialog.getResourcesString(2131296796);
//                                objArr[1] = ShareActionSheetDialog.getResourcesString(2131296496);
//                                share_instagram_tv.setText(String.format("%1$s%2$s", objArr));
//                                ShareActionSheetDialog.this.createInstagramVideoIntent(path);
//                                ShareActionSheetDialog.this.instagramViewRever();
//                                ShareActionSheetDialog.this.removeShareState(photoId, "instagram");
//                            }
//
//                            public void onFinish(String path) {
//                            }
//                        }, ShareActionSheetDialog.this.photoId, needWatermark);
//                    }
//                });
//                return;
//            }
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private void onShareQQClick(View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, QIUQIU, 1, 3));
//            updateShareState(this.photoId, QIUQIU, 1);
//            TextView share_qq_tv;
//            if (this.mShareType == 1) {
//                if (this.onShareSheetItemClickListener != null) {
//                    this.onShareSheetItemClickListener.onShareQQConfirmClick();
//                }
//                if (this.isChinese) {
//                    share_qq_tv = (TextView) view.findViewById(2131690619);
//                } else {
//                    share_qq_tv = (TextView) view.findViewById(2131690662);
//                }
//                share_qq_tv.setText(getShareFormatString(2131296799, 2131296496));
//                String filePath = Config.getPhotoCachePath();
//                getBitmapPathStartShare(QIUQIU, filePath, new QQSharePlatformActionListener(filePath), false);
//                return;
//            } else if (this.mShareType == 2) {
//                if (this.onShareSheetItemClickListener != null) {
//                    this.onShareSheetItemClickListener.onShareQQConfirmClick();
//                }
//                if (this.isChinese) {
//                    share_qq_tv = (TextView) view.findViewById(2131690619);
//                } else {
//                    share_qq_tv = (TextView) view.findViewById(2131690662);
//                }
//                share_qq_tv.setText(getShareFormatString(2131296799, 2131296496));
//                setAllViewDisabled(view);
//                Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(ImageUtil.getPreviewUrl(this.preview_url), this);
//                Bitmap finalBitmap = null;
//                if (bitmap != null) {
//                    Bitmap centerBitmap;
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//                    if (height > width) {
//                        centerBitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
//                    } else {
//                        centerBitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
//                    }
//                    finalBitmap = Bitmap.createScaledBitmap(centerBitmap, 200, 200, false);
//                }
//                final View view2 = view;
//                PhotoUtil.saveVideoPhotoToLocal(this.mContext, finalBitmap, Config.getPhotoCachePath(), true, false, new SavePhotoListener() {
//                    public void onSaveFinished(final String filePath) {
//                        App.runOnUiThread(new Runnable() {
//                            public void run() {
//                                ShareUtils.shareWebPageToQQ(ShareActionSheetDialog.this.getContext(), "One Take", "One Take", ShareActionSheetDialog.this.web_url, filePath, new PlatformActionListener() {
//                                    void finishViewRever() {
//                                        ShareActionSheetDialog.this.setAllViewEnabled(view2);
//                                        ShareActionSheetDialog.this.changeViewStatusToBase(view2, ShareActionSheetDialog.this.isChinese, 2131690617, 2131690618, 2131690619, 2131690660, 2131690661, 2131690662, 2131296799);
//                                    }
//
//                                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                                        finishViewRever();
//                                        ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                                        String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                                        Object[] objArr = new Object[1];
//                                        objArr[0] = ShareActionSheetDialog.getResourcesString(2131296799);
//                                        shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//                                    }
//
//                                    public void onError(Platform platform, int i, Throwable throwable) {
//                                        finishViewRever();
//                                        ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                                        String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
//                                        Object[] objArr = new Object[1];
//                                        objArr[0] = ShareActionSheetDialog.getResourcesString(2131296799);
//                                        shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//                                    }
//
//                                    public void onCancel(Platform platform, int i) {
//                                        finishViewRever();
//                                    }
//                                });
//                            }
//                        });
//                    }
//
//                    public void onSaveFailed(String filePath) {
//                        ShareActionSheetDialog.this.setAllViewEnabled(view2);
//                        ShareActionSheetDialog.this.changeViewStatusToBase(view2, ShareActionSheetDialog.this.isChinese, 2131690617, 2131690618, 2131690619, 2131690660, 2131690661, 2131690662, 2131296799);
//                    }
//                });
//                return;
//            } else {
//                setAllViewDisabled(view);
//                ShareCropDialogClick();
//                ShareViewHelper.showShareCropDialog(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, true, false, new BaseShareCropListener(view, true, false, false, false, new BaseOnHandlerMp4ToGifListener(getContext(), view, QIUQIU, this.isChinese, this.photoId, 2131690617, 2131690618, 2131690619, 2131690660, 2131690661, 2131690662, 2131296494, 2131296496, 2131296799) {
//                    public void finishViewRever() {
//                        super.finishViewRever();
//                    }
//
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    public void onFinish(String path) {
//                        super.onFinish(path);
//                    }
//
//                    public void onFinish(final String path, final int photoId) {
//                        super.onFinish(path, photoId);
//                        App.runOnUiThread(new Runnable() {
//                            public void run() {
//                                ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.QIUQIU, 1, photoId);
//                                ShareUtils.shareImageToQQ(ShareActionSheetDialog.this.getContext(), path, new QQSharePlatformActionListener(path));
//                            }
//                        });
//                    }
//                }) {
//                    public void onShareCropCancelClick() {
//                        super.onShareCropCancelClick();
//                    }
//
//                    public void onShareCropDismiss() {
//                        super.onShareCropDismiss();
//                    }
//
//                    public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
//                        super.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//                    }
//                }, 9.lambdaFactory$(this, view));
//                if (this.onShareSheetItemClickListener != null) {
//                    this.onShareSheetItemClickListener.onShareCropClick();
//                    return;
//                }
//                return;
//            }
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private /* synthetic */ void lambda$onShareQQClick$8(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//            removeShareState(this.photoId, QIUQIU);
//            removeEmptyMessage(1);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, QIUQIU, 3, 2));
//            qqViewRever();
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private void changeViewStatusToPreparing(View view, boolean isChinese, int cnIconViewId, int cnLoadingViewId, int cnTextViewId, int enIconViewId, int enLoadingViewId, int enTextViewId, int baseStringResId, int prepareStringResId) {
//        TextView textView;
//        if (isChinese) {
//            view.findViewById(cnIconViewId).setVisibility(4);
//            view.findViewById(cnLoadingViewId).setVisibility(0);
//            textView = (TextView) view.findViewById(cnTextViewId);
//        } else {
//            view.findViewById(enIconViewId).setVisibility(4);
//            view.findViewById(enLoadingViewId).setVisibility(0);
//            textView = (TextView) view.findViewById(enTextViewId);
//        }
//        textView.setText(getShareFormatString(baseStringResId, prepareStringResId));
//    }
//
//    private void changeViewStatusToBase(View view, boolean isChinese, int cnIconViewId, int cnLoadingViewId, int cnTextViewId, int enIconViewId, int enLoadingViewId, int enTextViewId, int baseStringResId) {
//        TextView textView;
//        view.setEnabled(true);
//        view.setAlpha(1.0f);
//        if (isChinese) {
//            view.findViewById(cnIconViewId).setVisibility(0);
//            view.findViewById(cnLoadingViewId).setVisibility(8);
//            textView = (TextView) view.findViewById(cnTextViewId);
//        } else {
//            view.findViewById(enIconViewId).setVisibility(0);
//            view.findViewById(enLoadingViewId).setVisibility(8);
//            textView = (TextView) view.findViewById(enTextViewId);
//        }
//        textView.setText(getResourcesString(baseStringResId));
//    }
//
//    private void onShareWechatMomentsClick(View view) {
//        if (view.isEnabled()) {
//            StringBuilder stringBuilder;
//            String resourcesString;
//            Object[] objArr;
//            String text;
//            changeViewStatusToPreparing(view, this.isChinese, 2131690612, 2131690613, 2131690614, 2131690655, 2131690656, 2131690657, 2131296797, 2131296494);
//            view.setEnabled(false);
//            if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                stringBuilder = new StringBuilder();
//                resourcesString = getResourcesString(2131296979);
//                objArr = new Object[1];
//                objArr[0] = getOneShareTypeString(this.mShareType);
//                text = stringBuilder.append(String.format(resourcesString, objArr)).append(this.mOtherText).toString();
//            } else if (this.isChinese) {
//                stringBuilder = new StringBuilder();
//                resourcesString = getResourcesString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = this.mUserName;
//                objArr[1] = getOneShareTypeString(this.mShareType);
//                text = stringBuilder.append(String.format(resourcesString, objArr)).append(this.mOtherText).toString();
//            } else {
//                stringBuilder = new StringBuilder();
//                resourcesString = getResourcesString(2131296983);
//                objArr = new Object[2];
//                objArr[0] = getOneShareTypeString(this.mShareType);
//                objArr[1] = this.mUserName;
//                text = stringBuilder.append(String.format(resourcesString, objArr)).append(this.mOtherText).toString();
//            }
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareWechatMomentsConfirmClick();
//            }
//            setAllViewDisabled(view);
//            if (this.mShareType == 1) {
//                String filePath = Config.getPhotoCachePath();
//                getBitmapPathStartShare(WECHATMOMENTS, filePath, new WeChatMomentsPlatformActionListener(filePath), true);
//                return;
//            }
//            Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(ImageUtil.getPreviewUrl(this.preview_url), this);
//            Bitmap finalBitmap = null;
//            if (bitmap != null) {
//                Bitmap centerBitmap;
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//                if (height > width) {
//                    centerBitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
//                } else {
//                    centerBitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
//                }
//                finalBitmap = Bitmap.createScaledBitmap(centerBitmap, 200, 200, false);
//                if (this.mShareType == 0) {
//                    Canvas canvas = new Canvas(finalBitmap);
//                    Bitmap coverBitmap = BitmapFactory.decodeResource(getContext().getResources(), 2130838321);
//                    canvas.drawBitmap(coverBitmap, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, null);
//                    canvas.save();
//                    coverBitmap.recycle();
//                }
//            }
//            if (this.mShareType == 2) {
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    if (TextUtil.isValidate(this.mTitle)) {
//                        text = this.mTitle;
//                    } else {
//                        text = getChineseString(2131296980) + this.mOtherText;
//                    }
//                } else if (TextUtil.isValidate(this.mTitle)) {
//                    text = this.mTitle;
//                } else {
//                    stringBuilder = new StringBuilder();
//                    resourcesString = getChineseString(2131296983);
//                    objArr = new Object[2];
//                    objArr[0] = this.mUserName;
//                    objArr[1] = getOneShareTypeString(this.mShareType);
//                    text = stringBuilder.append(String.format(resourcesString, objArr)).append(this.mOtherText).toString();
//                }
//            }
//            final View view2 = view;
//            ShareUtils.shareWebPageToWechatMoments(getContext(), text, this.web_url, finalBitmap, new PlatformActionListener() {
//                void finishViewRever() {
//                    ShareActionSheetDialog.this.setAllViewEnabled(view2);
//                    ShareActionSheetDialog.this.changeViewStatusToBase(view2, ShareActionSheetDialog.this.isChinese, 2131690612, 2131690613, 2131690614, 2131690655, 2131690656, 2131690657, 2131296797);
//                }
//
//                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                    finishViewRever();
//                    ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                    String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                    Object[] objArr = new Object[1];
//                    objArr[0] = ShareActionSheetDialog.getResourcesString(2131296797);
//                    shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//                }
//
//                public void onError(Platform platform, int i, Throwable throwable) {
//                    finishViewRever();
//                    ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                    String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
//                    Object[] objArr = new Object[1];
//                    objArr[0] = ShareActionSheetDialog.getResourcesString(2131296797);
//                    shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//                }
//
//                public void onCancel(Platform platform, int i) {
//                    finishViewRever();
//                }
//            });
//        }
//    }
//
//    private void getVideoPathStartShare(View view, final boolean isNeedAddWaterMarket, final OnHandleMP4ToVideoListener listener) {
//        setAllViewDisabled(view);
//        PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//            public void run() {
//                ShareActionSheetDialog.this.saveMp4ToSDForShare(isNeedAddWaterMarket, listener);
//            }
//        });
//    }
//
//    private void setAllViewDisabled(@Nullable View view) {
//        if (this.mItems != null && this.mItems.size() > 0) {
//            int index = -1;
//            if (TextUtil.isValidate(view)) {
//                index = this.mItems.indexOf(view);
//            }
//            for (int i = 0; i < this.mItems.size(); i++) {
//                if (i != index) {
//                    View v = (View) this.mItems.get(i);
//                    v.setAlpha(0.3f);
//                    v.setEnabled(false);
//                }
//            }
//        }
//    }
//
//    private void setAllViewEnabled(@Nullable View view) {
//        if (this.mItems != null && this.mItems.size() > 0) {
//            int index = -1;
//            if (view != null) {
//                index = this.mItems.indexOf(view);
//            }
//            for (int i = 0; i < this.mItems.size(); i++) {
//                if (i != index) {
//                    View v = (View) this.mItems.get(i);
//                    v.setAlpha(1.0f);
//                    v.setEnabled(true);
//                }
//            }
//        }
//    }
//
//    private void getBitmapPathStartShare(String key, String filePath, PlatformActionListener platformActionListener, boolean isNeedAddWaterMarket) {
//        getBitmapPathStartShare(key, filePath, platformActionListener, null, null, isNeedAddWaterMarket);
//    }
//
//    private void getBitmapPathStartVideoShare(String key, String filePath, PlatformActionListener platformActionListener, PlatformActionListener weiboPlatformActionListener2, String shareText) {
//        String imageTypeUrl = ImageUtil.getPreviewUrl(this.preview_url);
//        String pathFromFrescoCache = FrescoHelper.getFilePathFromFrescoCache(imageTypeUrl);
//        LogUtil.d("saveBitmapToLocal", "start upload filepath : " + filePath);
//        Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(imageTypeUrl, this);
//        final String str = key;
//        final PlatformActionListener platformActionListener2 = platformActionListener;
//        final PlatformActionListener platformActionListener3 = weiboPlatformActionListener2;
//        final String str2 = shareText;
//        PhotoUtil.saveVideoPhotoToLocal(this.mContext, bitmap, filePath, true, false, new SavePhotoListener() {
//            public void onSaveFinished(final String filePath) {
//                App.runOnUiThread(new Runnable() {
//                    public void run() {
//                        ShareActionSheetDialog.this.afterGetImageResolveShareAction(str, filePath, platformActionListener2, platformActionListener3, str2);
//                    }
//                });
//            }
//
//            public void onSaveFailed(String filePath) {
//            }
//        });
//    }
//
//    private void getBitmapPathStartShare(String key, String filePath, PlatformActionListener platformActionListener, PlatformActionListener weiboPlatformActionListener2, String shareText, boolean isNeedAddWaterMarket) {
//        String imageTypeUrl = ImageUtil.getImageTypeUrl(this.preview_url);
//        String pathFromFrescoCache = FrescoHelper.getFilePathFromFrescoCache(imageTypeUrl);
//        LogUtil.d("saveBitmapToLocal", "start upload filepath : " + filePath);
//        final String str;
//        final PlatformActionListener platformActionListener2;
//        final PlatformActionListener platformActionListener3;
//        final String str2;
//        if (isNeedAddWaterMarket) {
//            Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(imageTypeUrl, this);
//            str = key;
//            platformActionListener2 = platformActionListener;
//            platformActionListener3 = weiboPlatformActionListener2;
//            str2 = shareText;
//            PhotoUtil.savePhotoToLocal(this.mContext, bitmap, filePath, isNeedAddWaterMarket, false, new SavePhotoListener() {
//                public void onSaveFinished(final String filePath) {
//                    App.runOnUiThread(new Runnable() {
//                        public void run() {
//                            ShareActionSheetDialog.this.afterGetImageResolveShareAction(str, filePath, platformActionListener2, platformActionListener3, str2);
//                        }
//                    });
//                }
//
//                public void onSaveFailed(String filePath) {
//                }
//            });
//        } else if (TextUtil.isValidate(pathFromFrescoCache)) {
//            LogUtil.d("saveBitmapToLocal", "start copy pathFromFrescoCache : " + pathFromFrescoCache);
//            FileUtil.copyFile(pathFromFrescoCache, filePath);
//            LogUtil.d("saveBitmapToLocal", "end copy filePath : " + filePath);
//            afterGetImageResolveShareAction(key, filePath, platformActionListener, weiboPlatformActionListener2, shareText);
//        } else {
//            str = key;
//            platformActionListener2 = platformActionListener;
//            platformActionListener3 = weiboPlatformActionListener2;
//            str2 = shareText;
//            BitmapUtil.saveBitmapToLocal(FrescoHelper.getBitmapFromFrescoCache(imageTypeUrl, this), filePath, new SaveBitmapCallback() {
//                public void onSaveFinish(final String filePath) {
//                    App.runOnUiThread(new Runnable() {
//                        public void run() {
//                            ShareActionSheetDialog.this.afterGetImageResolveShareAction(str, filePath, platformActionListener2, platformActionListener3, str2);
//                        }
//                    });
//                }
//
//                public void onSaveError(String filePath) {
//                }
//            });
//        }
//    }
//
//    private void afterGetPublicMP4ShareAction(String key, String filePath, PlatformActionListener platformActionListener, String shareText, View view) {
//        LogUtil.d("slim", "key:" + key + "  filePath:" + filePath + "  web_url:" + this.web_url);
//        Context context = App.getContext();
//        String[] strArr = new String[1];
//        strArr[0] = filePath;
//        MediaScannerConnection.scanFile(context, strArr, null, null);
//        if (WECHAT.equals(key)) {
//            ShareUtils.shareVideoToWechat(getContext(), filePath, platformActionListener);
//        } else if (WECHATMOMENTS.equals(key)) {
//            new IOSAlertDialog(getContext()).builder().setTitle(getContext().getString(2131296772)).setMsg(getContext().getString(2131296739)).setPositiveButton(10.lambdaFactory$(this)).show();
//        } else if (QIUQIU.equals(key)) {
//            ShareUtils.shareVideoToQQ(getContext(), filePath);
//            removeShareState(this.photoId, QIUQIU);
//            removeEmptyMessage(1);
//            qqViewRever();
//        } else if (SYSTEM.equals(key)) {
//            updateShareState(this.photoId, SYSTEM, 2);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 2, 3));
//            if (view != null) {
//                TextView mSystemTextView = (TextView) view.findViewById(2131690667);
//                Object[] objArr = new Object[2];
//                objArr[0] = getResourcesString(2131296519);
//                objArr[1] = getResourcesString(2131296496);
//                mSystemTextView.setText(String.format("%1$s%2$s", objArr));
//            }
//            shareFileTo(filePath);
//            systemViewRever();
//            removeShareState(this.photoId, SYSTEM);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 3, 3));
//        } else if (MESSENGER.equals(key)) {
//            shareTo(filePath, getResourcesString(2131296817));
//            messengerViewRever();
//            removeShareState(this.photoId, MESSENGER);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, MESSENGER, 3, 3));
//            setAllViewEnabled(null);
//        } else if ("instagram".equals(key)) {
//            createInstagramImageIntent(filePath);
//            messengerViewRever();
//            removeShareState(this.photoId, "instagram");
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, "instagram", 3, 3));
//            setAllViewEnabled(null);
//        } else if (FACEBOOK.equals(key)) {
//            shareTo(filePath, getResourcesString(2131296817));
//            messengerViewRever();
//            removeShareState(this.photoId, FACEBOOK);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, FACEBOOK, 3, 3));
//            setAllViewEnabled(null);
//        }
//    }
//
//    private /* synthetic */ void lambda$afterGetPublicMP4ShareAction$9(View v) {
//        if (startWeChatMoments()) {
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, WECHATMOMENTS, 3, 3));
//        } else {
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, WECHATMOMENTS, 3, 2));
//        }
//        removeShareState(this.photoId, WECHATMOMENTS);
//        wechatMomentsViewRever();
//    }
//
//    private boolean startWeChatMoments() {
//        try {
//            Intent intent = new Intent();
//            ComponentName cmp = new ComponentName(AppInfo.WeChatPackage, "com.tencent.mm.ui.LauncherUI");
//            intent.setAction("android.intent.action.MAIN");
//            intent.addCategory("android.intent.category.LAUNCHER");
//            intent.addFlags(268435456);
//            intent.setComponent(cmp);
//            getContext().startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private void afterGetImageResolveShareAction(String key, String filePath, PlatformActionListener platformActionListener, PlatformActionListener weiboPlatformActionListener2, String shareText) {
//        if (WECHAT.equals(key)) {
//            ShareUtils.shareImageToWechat(getContext(), filePath, platformActionListener);
//        } else if (WECHATMOMENTS.equals(key)) {
//            ShareUtils.shareImageToWechatMoment(getContext(), filePath, platformActionListener);
//        } else if (QIUQIU.equals(key)) {
//            ShareUtils.shareImageToQQ(getContext(), filePath, platformActionListener);
//        } else if (WEIBO.equals(key)) {
//            ShareUtils.shareImageToWeibo(getContext(), shareText, filePath, this.web_url, platformActionListener, weiboPlatformActionListener2);
//        } else if ("instagram".equals(key)) {
//            createInstagramImageIntent(filePath);
//            instagramViewRever();
//            removeShareState(this.photoId, "instagram");
//        } else if (SYSTEM.equals(key)) {
//            shareTo(filePath);
//            systemViewRever();
//            removeShareState(this.photoId, SYSTEM);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, SYSTEM, 3, 3));
//        } else if (FACEBOOK.equals(key)) {
//            shareTo(filePath, getResourcesString(2131296817));
//            faceBookViewRever();
//            removeShareState(this.photoId, FACEBOOK);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, FACEBOOK, 3, 3));
//        } else if (MESSENGER.equals(key)) {
//            shareTo(filePath, getResourcesString(2131296817));
//            messengerViewRever();
//            removeShareState(this.photoId, MESSENGER);
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, MESSENGER, 3, 3));
//        } else if (TWITTER.equals(key)) {
//            ShareUtils.shareImageToTwitterGetAccountCheck(getContext(), shareText, filePath, platformActionListener);
//        } else if (TUMBLR.equals(key)) {
//            ShareUtils.shareImageToTumblrGetAccountCheck(getContext(), shareText, filePath, platformActionListener);
//        }
//    }
//
//    private void onShareWechatClick(View view) {
//        if (!view.isEnabled()) {
//            return;
//        }
//        if (this.mContext == null || ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            this.mPermissionView = null;
//            view.setEnabled(false);
//            setAllViewDisabled(view);
//            if (this.onShareSheetItemClickListener != null) {
//                this.onShareSheetItemClickListener.onShareWechatConfirmClick();
//            }
//            EventBus.getDefault().post(new ChangeShareEvent(this.photoId, WECHAT, 1, 3));
//            updateShareState(this.photoId, WECHAT, 1);
//            TextView share_wechat_tv;
//            if (this.mShareType == 1) {
//                if (this.isChinese) {
//                    share_wechat_tv = (TextView) view.findViewById(2131690609);
//                } else {
//                    share_wechat_tv = (TextView) view.findViewById(2131690652);
//                }
//                share_wechat_tv.setText(getShareFormatString(2131296802, 2131296496));
//                String filePath = Config.getPhotoCachePath();
//                getBitmapPathStartShare(WECHAT, filePath, new WechatSharePlatformActionListener(filePath), false);
//                return;
//            } else if (this.mShareType == 2) {
//                if (this.isChinese) {
//                    share_wechat_tv = (TextView) view.findViewById(2131690609);
//                } else {
//                    share_wechat_tv = (TextView) view.findViewById(2131690652);
//                }
//                share_wechat_tv.setText(getShareFormatString(2131296802, 2131296496));
//                Bitmap bitmap = FrescoHelper.getBitmapFromFrescoCache(ImageUtil.getPreviewUrl(this.preview_url), this);
//                Bitmap finalBitmap = null;
//                if (bitmap != null) {
//                    Bitmap centerBitmap;
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//                    if (height > width) {
//                        centerBitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
//                    } else {
//                        centerBitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
//                    }
//                    finalBitmap = Bitmap.createScaledBitmap(centerBitmap, 200, 200, false);
//                }
//                String title = getChineseString(2131296982);
//                String text = getChineseString(2131296984);
//                if (TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName())) {
//                    if (TextUtil.isValidate(this.mTitle)) {
//                        title = this.mTitle;
//                    }
//                } else if (TextUtil.isValidate(this.mTitle)) {
//                    title = this.mTitle;
//                }
//                if (TextUtil.isValidate(this.mPhotoText)) {
//                    text = this.mPhotoText;
//                }
//                final View view2 = view;
//                ShareUtils.shareLinkToWechat(getContext(), title, text, this.web_url, finalBitmap, new PlatformActionListener() {
//                    void finishViewRever() {
//                        ShareActionSheetDialog.this.setAllViewEnabled(view2);
//                        ShareActionSheetDialog.this.changeViewStatusToBase(view2, ShareActionSheetDialog.this.isChinese, 2131690607, 2131690608, 2131690609, 2131690650, 2131690651, 2131690652, 2131296802);
//                    }
//
//                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        finishViewRever();
//                        ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                        String access$600 = ShareActionSheetDialog.getResourcesString(2131296985);
//                        Object[] objArr = new Object[1];
//                        objArr[0] = ShareActionSheetDialog.getResourcesString(2131296802);
//                        shareActionSheetDialog.showAppMessageSuccess(String.format(access$600, objArr));
//                    }
//
//                    public void onError(Platform platform, int i, Throwable throwable) {
//                        finishViewRever();
//                        ShareActionSheetDialog shareActionSheetDialog = ShareActionSheetDialog.this;
//                        String access$600 = ShareActionSheetDialog.getResourcesString(2131296978);
//                        Object[] objArr = new Object[1];
//                        objArr[0] = ShareActionSheetDialog.getResourcesString(2131296802);
//                        shareActionSheetDialog.showAppMessageAlert(String.format(access$600, objArr));
//                    }
//
//                    public void onCancel(Platform platform, int i) {
//                        finishViewRever();
//                    }
//                });
//                return;
//            } else {
//                ShareCropDialogClick();
//                ShareViewHelper.showShareCropDialog(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, false, false, new BaseShareCropListener(view, true, true, false, false, new BaseOnHandlerMp4ToGifListener(getContext(), view, WECHAT, this.isChinese, this.photoId, 2131690607, 2131690608, 2131690609, 2131690650, 2131690608, 2131690652, 2131296494, 2131296496, 2131296802) {
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    public void onFinish(String path) {
//                        super.onFinish(path);
//                    }
//
//                    public void onFinish(final String path, final int photoId) {
//                        super.onFinish(path, photoId);
//                        App.runOnUiThread(new Runnable() {
//                            public void run() {
//                                ShareActionSheetDialog.this.sendEmptyMessage(ShareActionSheetDialog.WECHAT, 0, photoId);
//                                ShareUtils.shareEmojiToWechat(ShareActionSheetDialog.this.getContext(), path, new WechatSharePlatformActionListener(path));
//                            }
//                        });
//                    }
//                }) {
//                    public void onShareCropCancelClick() {
//                        super.onShareCropCancelClick();
//                        ShareActionSheetDialog.this.wechatViewRever();
//                    }
//
//                    public void onShareCropDismiss() {
//                        super.onShareCropDismiss();
//                    }
//
//                    public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
//                        super.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//                    }
//                }, 11.lambdaFactory$(this, view));
//                return;
//            }
//        }
//        this.mPermissionView = view;
//        Activity activity = this.mContext;
//        String[] strArr = new String[1];
//        strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//        ActivityCompat.requestPermissions(activity, strArr, 102);
//    }
//
//    private /* synthetic */ void lambda$onShareWechatClick$10(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    public void finishWeiBoViewRever(View view) {
//        setAllViewEnabled(view);
//        removeShareState(this.photoId, WEIBO);
//        changeViewStatusToBase(view, this.isChinese, 2131690071, 2131690603, 2131690604, 2131690645, 2131690646, 2131690647, 2131296803);
//    }
//
//    public void finishTwitterViewRever(View view) {
//        setAllViewEnabled(view);
//        removeShareState(this.photoId, TWITTER);
//        changeViewStatusToBase(view, this.isChinese, 2131690085, 2131690640, 2131690641, 2131690085, 2131690640, 2131690641, 2131296801);
//    }
//
//    private void onShareWeiboError(View view, String path, int photoId) {
//        if (TextUtil.isValidate(path)) {
//            File file = new File(path);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//        App.runOnUiThread(12.lambdaFactory$(this, photoId, view));
//    }
//
//    private /* synthetic */ void lambda$onShareWeiboError$11(int photoId, View view) {
//        removeShareState(photoId, WEIBO);
//        removeEmptyMessage(2);
//        EventBus.getDefault().post(new ChangeShareEvent(photoId, WEIBO, 3, 2));
//        if (this.isDismiss) {
//            EventBus.getDefault().post(new ToastMessageEvent(this.mContext, false, 2131296803));
//        } else {
//            String resourcesString = getResourcesString(2131296978);
//            Object[] objArr = new Object[1];
//            objArr[0] = getResourcesString(2131296803);
//            showAppMessageAlert(String.format(resourcesString, objArr));
//        }
//        finishWeiBoViewRever(view);
//    }
//
//    private void onShareWeiboCancel(View view, String path, int photoId) {
//        if (TextUtil.isValidate(path)) {
//            File file = new File(path);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//        App.runOnUiThread(13.lambdaFactory$(this, photoId, view));
//    }
//
//    private /* synthetic */ void lambda$onShareWeiboCancel$12(int photoId, View view) {
//        removeEmptyMessage(2);
//        removeShareState(photoId, WEIBO);
//        EventBus.getDefault().post(new ChangeShareEvent(photoId, WEIBO, 3, 3));
//        finishWeiBoViewRever(view);
//    }
//
//    private void whenWeiboShareGifCropConfirmClick(String addText, float startScale, boolean isCrop, String text, View view, OnHandlerMp4ToGifListener weiboMp4ToGIfListener) {
//        final View view2 = view;
//        final OnHandlerMp4ToGifListener onHandlerMp4ToGifListener = weiboMp4ToGIfListener;
//        final String str = addText;
//        final boolean z = isCrop;
//        final float f = startScale;
//        ShareViewHelper.showShareEditPageDialog(getContext(), text, true, new OnShareEditPageClickListener() {
//            public void onShareEditPageCancelClick() {
//                view2.setEnabled(true);
//                ShareActionSheetDialog.this.finishWeiBoViewRever(view2);
//            }
//
//            public void onShareEditPageConfirmClick(String content) {
//                if (ShareActionSheetDialog.this.onShareSheetItemClickListener != null) {
//                    ShareActionSheetDialog.this.onShareSheetItemClickListener.onShareWeiboConfirmClick();
//                }
//                onHandlerMp4ToGifListener.setContent(content);
//                PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
//                    public void run() {
//                        ShareActionSheetDialog.this.mp4ToGif(str, z, f, onHandlerMp4ToGifListener, ShareActionSheetDialog.this.photoId, false, true);
//                    }
//                });
//            }
//        }, 14.lambdaFactory$(this, view));
//    }
//
//    private /* synthetic */ void lambda$whenWeiboShareGifCropConfirmClick$13(View view, boolean isConfirm) {
//        if (!isConfirm) {
//            view.setEnabled(true);
//            finishWeiBoViewRever(view);
//        }
//    }
//
//    private void showShareCropDialogWithFilmSubtitles(final View view, final String text) {
//        if (this.mShareType == 1 || this.mShareType == 2) {
//            ShareViewHelper.showShareNewEditPageDialog(getContext(), text, true, new OnShareEditPageClickListener() {
//                public void onShareEditPageCancelClick() {
//                    view.setEnabled(true);
//                    ShareActionSheetDialog.this.finishWeiBoViewRever(view);
//                    if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                        ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//                    }
//                }
//
//                public void onShareEditPageConfirmClick(String content) {
//                    String officialTagNameString;
//                    String text1;
//                    if (ShareActionSheetDialog.this.onShareSheetItemClickListener != null) {
//                        ShareActionSheetDialog.this.onShareSheetItemClickListener.onShareWeiboConfirmClick();
//                    }
//                    ShareActionSheetDialog.this.changeWeiboViewToUploadStatus(view);
//                    String tag = ShareActionSheetDialog.this.getWeiboTagAppNameString();
//                    if (TextUtils.equals(ShareActionSheetDialog.this.mUserName, GlobalHelper.getUserScreenName())) {
//                        officialTagNameString = ShareActionSheetDialog.this.getWeiboOfficialTagNameString(ShareActionSheetDialog.this.ots);
//                    } else if (TextUtil.isValidate(ShareActionSheetDialog.this.ots)) {
//                        ArrayList<OfficialTagBean> tempList = new ArrayList();
//                        Iterator it = ShareActionSheetDialog.this.ots.iterator();
//                        while (it.hasNext()) {
//                            OfficialTagBean bean = (OfficialTagBean) it.next();
//                            if ("promotes_at_last".equals(bean.getType())) {
//                                tempList.add(bean);
//                            }
//                        }
//                        officialTagNameString = ShareActionSheetDialog.this.getWeiboOfficialTagNameString(tempList);
//                    } else {
//                        officialTagNameString = "";
//                    }
//                    if (ShareActionSheetDialog.this.mShareType == 2) {
//                        text1 = content + "   ▶️ " + ShareActionSheetDialog.this.mContext.getResources().getString(2131296987) + ShareActionSheetDialog.this.web_url + "   " + tag + " " + officialTagNameString;
//                    } else {
//                        text1 = content + "   " + ShareActionSheetDialog.this.web_url + "   " + tag + " " + officialTagNameString;
//                    }
//                    String filePath = Config.getPhotoCachePath();
//                    ShareActionSheetDialog.this.setAllViewDisabled(view);
//                    if (ShareActionSheetDialog.this.mShareType == 2) {
//                        ShareActionSheetDialog.this.getBitmapPathStartVideoShare(ShareActionSheetDialog.WEIBO, filePath, new WeiboCheckAccountOrSharePlatformActionListener(filePath, view), new WeiboSharePlatformActionListener(filePath, text1, view), text1);
//                    } else {
//                        ShareActionSheetDialog.this.getBitmapPathStartShare(ShareActionSheetDialog.WEIBO, filePath, new WeiboCheckAccountOrSharePlatformActionListener(filePath, view), new WeiboSharePlatformActionListener(filePath, text1, view), text1, false);
//                    }
//                }
//            }, 15.lambdaFactory$(this, view));
//            return;
//        }
//        final OnHandlerMp4ToGifListener weiboMp4ToGIfListener = new OnHandlerMp4ToGifListener() {
//            public void onStart() {
//                if (ShareActionSheetDialog.this.getContext() != null) {
//                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$28$.Lambda
//                    .1.lambdaFactory$(this, view));
//                }
//            }
//
//            private /* synthetic */ void lambda$onStart$0(View view) {
//                ShareActionSheetDialog.this.updateShareState(ShareActionSheetDialog.this.photoId, ShareActionSheetDialog.WEIBO, 1);
//                ShareActionSheetDialog.this.changeViewStatusToPreparing(view, ShareActionSheetDialog.this.isChinese, 2131690071, 2131690603, 2131690604, 2131690645, 2131690646, 2131690647, 2131296803, 2131296494);
//            }
//
//            public void onFinish(String path) {
//            }
//
//            public void onFinish(String path, int photoId) {
//                if (ShareActionSheetDialog.this.getContext() != null) {
//                    ((Activity) ShareActionSheetDialog.this.getContext()).runOnUiThread(ShareActionSheetDialog$28$.Lambda
//                    .2.lambdaFactory$(this, view, path));
//                }
//            }
//
//            private /* synthetic */ void lambda$onFinish$1(View view, String path) {
//                String officialTagNameString;
//                ShareActionSheetDialog.this.changeWeiboViewToUploadStatus(view);
//                String tag = ShareActionSheetDialog.this.getWeiboTagAppNameString();
//                if (TextUtils.equals(ShareActionSheetDialog.this.mUserName, GlobalHelper.getUserScreenName())) {
//                    officialTagNameString = ShareActionSheetDialog.this.getWeiboOfficialTagNameString(ShareActionSheetDialog.this.ots);
//                } else {
//                    officialTagNameString = "";
//                }
//                String text1 = this.content + "   " + ShareActionSheetDialog.this.web_url + "   " + tag + " " + officialTagNameString;
//                ShareUtils.shareImageToWeibo(ShareActionSheetDialog.this.getContext(), text1, path, ShareActionSheetDialog.this.web_url, new WeiboCheckAccountOrSharePlatformActionListener(path, view), new WeiboSharePlatformActionListener(path, text1, view));
//            }
//        };
//        ShareViewHelper.showShareCropDialogWithFilmSubtitles(getContext(), this.isFullScreen, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, true, false, new OnShareCropClickListener() {
//            public void onShareCropCancelClick() {
//                view.setEnabled(true);
//                ShareActionSheetDialog.this.finishWeiBoViewRever(view);
//                if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                    ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropCancelClick();
//                }
//            }
//
//            public void onShareCropDismiss() {
//            }
//
//            public void onShareCropConfirmClick(boolean isHorizontal, float startScale, String addText, boolean isCrop) {
//                ShareActionSheetDialog.this.whenWeiboShareGifCropConfirmClick(addText, startScale, isCrop, text, view, weiboMp4ToGIfListener);
//                if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                    ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, addText, isCrop);
//                }
//            }
//        }, new OnDialogDismissListener() {
//            public void onDismiss(boolean isConfirm) {
//                ShareActionSheetDialog.this.ShareCropDialogDismiss();
//                if (!isConfirm) {
//                    view.setEnabled(true);
//                    ShareActionSheetDialog.this.finishWeiBoViewRever(view);
//                }
//                if (ShareActionSheetDialog.this.mOnShareCropClickListener != null) {
//                    ShareActionSheetDialog.this.mOnShareCropClickListener.onShareCropDismiss();
//                }
//            }
//        });
//    }
//
//    private /* synthetic */ void lambda$showShareCropDialogWithFilmSubtitles$14(View view, boolean isConfirm) {
//        ShareCropDialogDismiss();
//        if (!isConfirm) {
//            view.setEnabled(true);
//            finishWeiBoViewRever(view);
//        }
//        if (this.mOnShareCropClickListener != null) {
//            this.mOnShareCropClickListener.onShareCropDismiss();
//        }
//    }
//
//    private String getWeiboOfficialTagNameString(ArrayList<OfficialTagBean> tagBeanArrayList) {
//        StringBuilder stringBuilder = new StringBuilder();
//        Iterator it = tagBeanArrayList.iterator();
//        while (it.hasNext()) {
//            OfficialTagBean bean = (OfficialTagBean) it.next();
//            if (!ShareNotUseTagTypeList.contains(bean.getType())) {
//                if ("usergenerate".equals(bean.getType())) {
//                    stringBuilder.append("#").append(bean.getValue()).append("#").append(" ");
//                } else if (TextUtil.isValidate(bean.getEn())) {
//                    stringBuilder.append("#").append(bean.getZh()).append("#").append(" ");
//                }
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    private String getTwitterOfficialTagNameString(ArrayList<OfficialTagBean> tagBeanArrayList) {
//        StringBuilder stringBuilder = new StringBuilder();
//        Iterator it = tagBeanArrayList.iterator();
//        while (it.hasNext()) {
//            OfficialTagBean bean = (OfficialTagBean) it.next();
//            if (!ShareNotUseTagTypeList.contains(bean.getType())) {
//                if ("usergenerate".equals(bean.getType())) {
//                    stringBuilder.append("#").append(bean.getValue()).append(" ");
//                } else if (TextUtil.isValidate(bean.getEn())) {
//                    stringBuilder.append("#").append(bean.getEn()).append(" ");
//                }
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    @NonNull
//    private String getWeiboTagAppNameString() {
//        return "#" + getChineseString(2131296598) + "#";
//    }
//
//    private String getTwitterTagAppNameString() {
//        return "#" + getEnglishString(2131296598);
//    }
//
//    private void changeWeiboViewToUploadStatus(View view) {
//        updateShareState(this.photoId, WEIBO, 2);
//        EventBus.getDefault().post(new ChangeShareEvent(this.photoId, WEIBO, 2, 3));
//        sendEmptyMessage(WEIBO, 2, this.photoId);
//        Object[] objArr;
//        if (this.isChinese) {
//            TextView share_weibo_tv = (TextView) view.findViewById(2131690604);
//            objArr = new Object[2];
//            objArr[0] = getResourcesString(2131296803);
//            objArr[1] = getResourcesString(2131296496);
//            share_weibo_tv.setText(String.format("%1$s%2$s", objArr));
//            return;
//        }
//        TextView english_share_weibo_tv = (TextView) view.findViewById(2131690647);
//        objArr = new Object[2];
//        objArr[0] = getResourcesString(2131296803);
//        objArr[1] = getResourcesString(2131296496);
//        english_share_weibo_tv.setText(String.format("%1$s%2$s", objArr));
//    }
//
//    public void onEventMainThread(ChangeShareEvent event) {
//        if (this.rootView != null) {
//            int photo_id = event.photoId;
//            String photo_type = event.type;
//            int photo_state = event.state;
//            if (photo_id != this.photoId) {
//                return;
//            }
//            if (WECHATMOMENTS.equals(photo_type)) {
//                if (photo_state == 1) {
//                    if (this.isChinese) {
//                        displayShareState(this.rootView, 2131690611, 2131690612, 2131690613, 2131690614, 2131296797, photo_state);
//                        return;
//                    }
//                    displayShareState(this.rootView, 2131690654, 2131690655, 2131690656, 2131690657, 2131296797, photo_state);
//                } else if (this.isChinese) {
//                    nomalShareState(2131690611, 2131690612, 2131690613, 2131690614, 2131296797);
//                } else {
//                    nomalShareState(2131690654, 2131690655, 2131690656, 2131690657, 2131296797);
//                }
//            } else if (WECHAT.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    if (this.isChinese) {
//                        displayShareState(this.rootView, 2131690606, 2131690607, 2131690608, 2131690609, 2131296802, photo_state);
//                        return;
//                    }
//                    displayShareState(this.rootView, 2131690649, 2131690650, 2131690651, 2131690652, 2131296802, photo_state);
//                } else if (this.isChinese) {
//                    nomalShareState(2131690606, 2131690607, 2131690608, 2131690609, 2131296802);
//                } else {
//                    nomalShareState(2131690649, 2131690650, 2131690651, 2131690652, 2131296802);
//                }
//            } else if (QIUQIU.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    if (this.isChinese) {
//                        displayShareState(this.rootView, 2131690616, 2131690617, 2131690618, 2131690619, 2131296799, photo_state);
//                        return;
//                    }
//                    displayShareState(this.rootView, 2131690659, 2131690660, 2131690661, 2131690662, 2131296799, photo_state);
//                } else if (this.isChinese) {
//                    nomalShareState(2131690616, 2131690617, 2131690618, 2131690619, 2131296799);
//                } else {
//                    nomalShareState(2131690659, 2131690660, 2131690661, 2131690662, 2131296799);
//                }
//            } else if (WEIBO.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    if (this.isChinese) {
//                        displayShareState(this.rootView, 2131690602, 2131690071, 2131690603, 2131690604, 2131296803, photo_state);
//                        return;
//                    }
//                    displayShareState(this.rootView, 2131690644, 2131690645, 2131690646, 2131690647, 2131296803, photo_state);
//                } else if (this.isChinese) {
//                    nomalShareState(2131690602, 2131690071, 2131690603, 2131690604, 2131296803);
//                } else {
//                    nomalShareState(2131690644, 2131690645, 2131690646, 2131690647, 2131296803);
//                }
//            } else if (TWITTER.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690639, 2131690085, 2131690640, 2131690641, 2131296801, photo_state);
//                    return;
//                }
//                nomalShareState(2131690639, 2131690085, 2131690640, 2131690641, 2131296801);
//            } else if (FACEBOOK.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690630, 2131690082, 2131690631, 2131690632, 2131296794, photo_state);
//                    return;
//                }
//                nomalShareState(2131690630, 2131690082, 2131690631, 2131690632, 2131296794);
//            } else if (SYSTEM.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690664, 2131690665, 2131690666, 2131690667, 2131296519, photo_state);
//                    return;
//                }
//                nomalShareState(2131690664, 2131690665, 2131690666, 2131690667, 2131296519);
//            } else if (TUMBLR.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690625, 2131690626, 2131690627, 2131690628, 2131296800, photo_state);
//                    return;
//                }
//                nomalShareState(2131690625, 2131690626, 2131690627, 2131690628, 2131296800);
//            } else if (SAVEPHOTO.equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690673, 2131690674, 2131690675, 2131690676, 2131296490, photo_state);
//                    return;
//                }
//                nomalShareState(2131690673, 2131690674, 2131690675, 2131690676, 2131296490);
//            } else if ("instagram".equals(photo_type)) {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690621, 2131690079, 2131690622, 2131690623, 2131296796, photo_state);
//                    return;
//                }
//                nomalShareState(2131690621, 2131690079, 2131690622, 2131690623, 2131296796);
//            } else if (!MESSENGER.equals(photo_type)) {
//            } else {
//                if (photo_state == 1 || photo_state == 2) {
//                    displayShareState(this.rootView, 2131690634, 2131690635, 2131690636, 2131690637, 2131296795, photo_state);
//                    return;
//                }
//                nomalShareState(2131690634, 2131690635, 2131690636, 2131690637, 2131296795);
//            }
//        }
//    }
//
//    private void nomalShareState(int share_wechat_layout_ll, int wechat_icon_iv, int wechat_loading_pb, int share_wechat_tv, int socialWechat) {
//        this.rootView.findViewById(share_wechat_layout_ll).setAlpha(1.0f);
//        this.rootView.findViewById(share_wechat_layout_ll).setEnabled(true);
//        this.rootView.findViewById(wechat_icon_iv).setVisibility(0);
//        this.rootView.findViewById(wechat_loading_pb).setVisibility(4);
//        ((TextView) this.rootView.findViewById(share_wechat_tv)).setText(getResourcesString(socialWechat));
//    }
//
//    private void deleteVidoeInfo() {
//        if (this.mVideoFilePath != null && this.mVideoFilePath.length() != 0) {
//            File vidoFile = new File(this.mVideoFilePath);
//            if (vidoFile.exists() && !vidoFile.isDirectory()) {
//                vidoFile.delete();
//            }
//            String timeStamp = vidoFile.getName().replace(".mp4", "");
//            Object[] objArr = new Object[2];
//            objArr[0] = Config.getPrivatePath();
//            objArr[1] = timeStamp;
//            String timeStampPath = String.format("%1$s%2$s", objArr);
//            File timeStampFile = new File(timeStampPath);
//            if (!timeStampFile.exists()) {
//                return;
//            }
//            if (timeStampFile.isDirectory()) {
//                FileUtil.deleteDirectory(timeStampPath);
//            } else {
//                timeStampFile.delete();
//            }
//        }
//    }
//
//    private void shareFileTo(String path) {
//        Intent share = new Intent("android.intent.action.SEND");
//        share.setType("file/*");
//        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(path)));
//        getContext().startActivity(Intent.createChooser(share, "Share to"));
//    }
//
//    private void shareTo(String path) {
//        Intent share = new Intent("android.intent.action.SEND");
//        share.setType("image/*");
//        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(path)));
//        getContext().startActivity(Intent.createChooser(share, "Share to"));
//    }
//
//    private void shareLink(String text) {
//        Intent share_intent = new Intent();
//        share_intent.setAction("android.intent.action.SEND");
//        share_intent.setType("text/plain");
//        share_intent.putExtra("android.intent.extra.SUBJECT", "Sharing URL");
//        share_intent.putExtra("android.intent.extra.TEXT", text);
//        getContext().startActivity(Intent.createChooser(share_intent, "Share to"));
//    }
//
//    private void shareTo(String path, String text) {
//        Intent share = new Intent("android.intent.action.SEND");
//        share.setType("image/*");
//        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(path)));
//        share.putExtra("android.intent.extra.TEXT", text);
//        getContext().startActivity(Intent.createChooser(share, "Share to"));
//    }
//
//    private void createInstagramVideoIntent(String mediaPath) {
//        Intent share = new Intent("android.intent.action.SEND");
//        share.setType("video/*");
//        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mediaPath)));
//        getContext().startActivity(Intent.createChooser(share, "Share to"));
//    }
//
//    private void createInstagramImageIntent(String filePath) {
//        Intent share = new Intent("android.intent.action.SEND");
//        share.setType("image/*");
//        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(filePath)));
//        getContext().startActivity(Intent.createChooser(share, "Share to"));
//    }
//
//    private void saveMp4ToSD(OnHandlerMp4ToGifListener listener, int photoId, boolean needWatermark) {
//        saveMp4ToSD(false, listener, photoId, needWatermark);
//    }
//
//    private void saveMp4ToSD(boolean isSaveVideoFile, OnHandlerMp4ToGifListener listener, int photoId, boolean needWatermark) {
//        MP4Util.saveMp4ToSD(this.mContext, this.mVideoFilePath, isSaveVideoFile, listener, photoId, needWatermark);
//    }
//
//    private void saveMp4ToSDForShare(boolean needWatermark, OnHandleMP4ToVideoListener listener) {
//        ShareImageManager instance = ShareImageManager.getInstance();
//        String cachePath = instance.checkCache(this.mVideoFilePath);
//        if (TextUtil.isValidate(cachePath)) {
//            if (listener != null) {
//                listener.onFinish(cachePath, null);
//            }
//        } else if (needWatermark) {
//            ShareBitmapEntity entity = WaterMarkBitmapUtil.getVideoShareWaterMarkBitmap(this.mContext, this.mVideoWidth, this.mVideoHeight, 4);
//            Object[] objArr = new Object[2];
//            objArr[0] = instance.getShareDirPath();
//            objArr[1] = Long.valueOf(System.nanoTime());
//            String dstPath = String.format("%1$s%2$s.mp4", objArr);
//            try {
//                Player.encodeMP4withWatermark(entity, this.mVideoFilePath, dstPath, this.mVideoWidth, this.mVideoHeight, listener);
//            } catch (IOException e) {
//                if (listener != null) {
//                    listener.onFail(dstPath);
//                }
//                e.printStackTrace();
//            }
//        } else {
//            String copyFilePath = instance.copyFile(this.mVideoFilePath);
//            if (TextUtil.isValidate(copyFilePath) && listener != null) {
//                listener.onFinish(copyFilePath, null);
//            }
//        }
//    }
//
//    private void mp4ToGifForSave(OnHandlerMp4ToGifListener listener, int photoId, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        mp4ToGif("", false, false, StaticLayoutUtil.DefaultSpacingadd, listener, true, photoId, false, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private void mp4ToGif(String addText, boolean isCrop, float startScale, OnHandlerMp4ToGifListener listener, int photoId, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        mp4ToGif(addText, false, isCrop, startScale, listener, photoId, false, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private void mp4ToGif(boolean isSpecial, String addText, boolean isCrop, float startScale, OnHandlerMp4ToGifListener listener, int photoId, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        mp4ToGif(addText, false, isCrop, startScale, listener, photoId, isSpecial, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private void mp4ToGif(String addText, boolean isMoreShort, boolean isCrop, float startScale, OnHandlerMp4ToGifListener listener, int photoId, boolean isSpecial, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        mp4ToGif(addText, isMoreShort, isCrop, startScale, listener, false, photoId, isSpecial, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private void mp4ToGif(boolean isSpecial, String addText, boolean isMoreShort, boolean isCrop, float startScale, OnHandlerMp4ToGifListener listener, int photoId, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        mp4ToGif(addText, isMoreShort, isCrop, startScale, listener, false, photoId, isSpecial, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private void mp4ToGif(String addText, boolean isMoreShort, boolean isCrop, float startScale, OnHandlerMp4ToGifListener listener, boolean isSaveGifFile, int photoId, boolean isSpecial, boolean isNeedAddWaterMark, boolean isFilmSubtitles) {
//        BuglyLogUtil.writeKeyAndValueLog(TAG, "GIFUtil.mp4ToGifForSave");
//        GIFUtil.mp4ToGif(this.mContext, this.mVideoFilePath, this.mVideoWidth, this.mVideoHeight, this.mDuration, addText, isMoreShort, isCrop, startScale, listener, isSaveGifFile, photoId, isSpecial, isNeedAddWaterMark, isFilmSubtitles);
//    }
//
//    private int getStatusBarHeight() {
//        return Resources.getSystem().getDimensionPixelOffset(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
//    }
//
//    private String getENOneShareTypeString(int shareType) {
//        if (shareType == 0) {
//            return getEnglishString(2131296705);
//        }
//        if (shareType == 1) {
//            return getEnglishString(2131296707);
//        }
//        if (shareType == 2) {
//            return getEnglishString(2131296711);
//        }
//        return "";
//    }
//
//    private String getZHOneShareTypeString(int shareType) {
//        if (shareType == 0) {
//            return getChineseString(2131296705);
//        }
//        if (shareType == 1) {
//            return getChineseString(2131296707);
//        }
//        if (shareType == 2) {
//            return getChineseString(2131296711);
//        }
//        return "";
//    }
//
//    private String getOneShareTypeString(int shareType) {
//        if (shareType == 0) {
//            return getResourcesString(2131296705);
//        }
//        if (shareType == 1) {
//            return getResourcesString(2131296707);
//        }
//        if (shareType == 2) {
//            return getResourcesString(2131296711);
//        }
//        return "";
//    }
//
//    private boolean isNeedWatermark() {
//        return (App.isLogin() && TextUtils.equals(this.mUserName, GlobalHelper.getUserScreenName()) && !this.isOpenWaterMark) ? false : true;
//    }
//}