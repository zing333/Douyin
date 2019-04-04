package cn.nineton.onetake.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import cn.nineton.onetake.util.share.Platform;
import cn.nineton.onetake.util.share.PlatformActionListener;
import cn.nineton.onetake.widget.ShareActionSheetDialog;
//import cn.sharesdk.facebook.Facebook;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.Platform.ShareParams;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.instagram.Instagram;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.tumblr.Tumblr;
//import cn.sharesdk.twitter.Twitter;
//import cn.sharesdk.wechat.friends.Wechat;
//import cn.sharesdk.wechat.moments.WechatMoments;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.helper.UrlHelper;
//import com.blink.academy.onetake.support.info.AppInfo;
//import com.blink.academy.onetake.support.utils.UploadWeiboUtil;
//import com.blink.academy.onetake.support.weibo.AccessTokenKeeper;
//import com.blink.academy.onetake.support.weibo.ShareWeiboCallback;
//import com.blink.academy.onetake.support.weibo.UploadWeiboControll;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.WeiboSharePlatformActionListener;
//import com.facebook.internal.AnalyticsEvents;
//import com.facebook.share.internal.ShareConstants;
//import com.mob.MobSDK;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class ShareUtils {
    public static final int ALL_PLATFORM = 7;
    public static final int INSTAGRAM_WEIBO = 6;
    public static final int NO_PLATFORM = 0;
    public static final int ONLY_INSTAGRAM = 2;
    public static final int ONLY_WECHAT = 1;
    public static final int ONLY_WEIBO = 3;
    public static final int WECHAT_INSTAGRAM = 4;
    public static final int WECHAT_WEIBO = 5;

    public static void shareEmojiToWechat(Context context, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(9);
//            wechatShareParams.setImagePath(imagePath);
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享Emoji到微信:"+imagePath,Toast.LENGTH_LONG).show();
    }

    public static void shareImageToWechat(Context context, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(2);
//            wechatShareParams.setImagePath(imagePath);
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享图片到微信:"+imagePath,Toast.LENGTH_LONG).show();
    }

    public static void shareImageToWechatMoment(Context context, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(2);
//            wechatShareParams.setImagePath(imagePath);
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享图片到微信moment:"+imagePath,Toast.LENGTH_LONG).show();
    }

    public static void shareImageToWechatMoment(Context context, Bitmap bitmap, PlatformActionListener platformActionListener) {
//        if (context != null && bitmap != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(2);
//            wechatShareParams.setImageData(bitmap);
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享图片bitmap到微信:",Toast.LENGTH_LONG).show();

    }

    public static void shareLinkToWechat(Context context, String title, String text, String web_url, Bitmap bitmap, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(4);
//            wechatShareParams.setTitle(title);
//            wechatShareParams.setText(text);
//            wechatShareParams.setUrl(web_url);
//            if (bitmap != null) {
//                wechatShareParams.setImageData(bitmap);
//            }
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享连接到微信:"+title+","+text+","+web_url,Toast.LENGTH_LONG).show();
    }

    public static void shareWebPageToWechatMoments(Context context, String title, String web_url, Bitmap bitmap, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
//            ShareParams wechatShareParams = new ShareParams();
//            wechatShareParams.setShareType(4);
//            wechatShareParams.setTitle(title);
//            wechatShareParams.setUrl(web_url);
//            if (bitmap != null) {
//                wechatShareParams.setImageData(bitmap);
//            }
//            wechat.setPlatformActionListener(platformActionListener);
//            wechat.share(wechatShareParams);
//        }
        Toast.makeText(context,"分享H5到微信:"+title+","+web_url,Toast.LENGTH_LONG).show();

    }

    public static void shareWebPageToQQ(Context context, String title, String text, String web_url, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform qq = ShareSDK.getPlatform(QQ.NAME);
//            ShareParams qqShareParams = new ShareParams();
//            qqShareParams.setShareType(4);
//            qqShareParams.setTitle(title);
//            qqShareParams.setText(text);
//            qqShareParams.setImagePath(imagePath);
//            qqShareParams.setTitleUrl(web_url);
//            qq.setPlatformActionListener(platformActionListener);
//            qq.share(qqShareParams);
//        }
        Toast.makeText(context,"分享H5到qq:"+title+","+web_url,Toast.LENGTH_LONG).show();
    }

    public static void shareImageToQQ(Context context, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform qq = ShareSDK.getPlatform(QQ.NAME);
//            ShareParams qqShareParams = new ShareParams();
//            qqShareParams.setImagePath(imagePath);
//            qq.setPlatformActionListener(platformActionListener);
//            qq.share(qqShareParams);
//        }
        Toast.makeText(context,"分享图片到qq:"+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToWeibo(Context context, String text, String imagePath, String webUrl, PlatformActionListener accountValidActionListener, PlatformActionListener accountNotValidActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//            if (!weibo.isClientValid() || weibo.getDb() == null || new Date().getTime() >= weibo.getDb().getExpiresTime()) {
//                weibo.removeAccount(false);
//                weibo.SSOSetting(false);
//                weibo.setPlatformActionListener(accountNotValidActionListener);
//                weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                return;
//            }
//            HashMap<String, Object> map = new HashMap();
//            final Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
//            map.put(ShareConstants.FEED_SOURCE_PARAM, Constants.WEIBO_APP_KEY);
//            map.put("access_token", token.getToken());
//            map.put(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS, UrlHelper.URLEncoder(text));
//            map.put("pic", new File(imagePath));
//            map.put("visible", Integer.valueOf(0));
//            final Context context2 = context;
//            final PlatformActionListener platformActionListener = accountValidActionListener;
//            final PlatformActionListener platformActionListener2 = accountNotValidActionListener;
//            UploadWeiboControll.upLoadFile(map, new ShareWeiboCallback() {
//                public void onShareComplete() {
//                    new Thread() {
//                        public void run() {
//                            super.run();
//                            UploadWeiboUtil.uploadWeiboByToken(context2, token);
//                        }
//                    }.start();
//                    platformActionListener.onComplete(null, -1, null);
//                    LogUtil.d("huangweijie", "onShareComplete");
//                }
//
//                public void onShareError(boolean again) {
//                    if (again) {
//                        weibo.SSOSetting(false);
//                        weibo.setPlatformActionListener(platformActionListener2);
//                        weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                    } else {
//                        platformActionListener.onError(null, -1, null);
//                    }
//                    LogUtil.d("huangweijie", "onShareError");
//                }
//
//                public void onShareCancel() {
//                    LogUtil.d("huangweijie", "onShareCancel");
//                }
//            });
//        }
        Toast.makeText(context,"分享图片到微博:"+imagePath,Toast.LENGTH_LONG).show();

    }

//    public static void shareImageToWeiBoNoCheck2(final Context context, String text, String imagePath, String webUrl, final PlatformActionListener accountValidActionListener, final ShareActionSheetDialog.WeiboSharePlatformActionListener listener) {
//        if (context != null) {
//            HashMap<String, Object> map = new HashMap();
//            final Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
//            map.put(ShareConstants.FEED_SOURCE_PARAM, Constants.WEIBO_APP_KEY);
//            map.put("access_token", token.getToken());
//            map.put(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS, UrlHelper.URLEncoder(text));
//            map.put("pic", new File(imagePath));
//            map.put("visible", Integer.valueOf(0));
//            UploadWeiboControll.upLoadFile(map, new ShareWeiboCallback() {
//                public void onShareComplete() {
//                    AnonymousClass1 anonymousClass1 = new Thread() {
//                        public void run() {
//                            super.run();
//                            UploadWeiboUtil.uploadWeiboByToken(context, token);
//                        }
//                    };
//                    accountValidActionListener.onComplete(null, -1, null);
//                    LogUtil.d("huangweijie", "onShareComplete");
//                }
//
//                public void onShareError(boolean again) {
//                    if (again) {
//                        MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                        weibo.SSOSetting(false);
//                        weibo.setPlatformActionListener(listener);
//                        weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                    } else {
//                        accountValidActionListener.onError(null, -1, null);
//                    }
//                    LogUtil.d("huangweijie", "onShareError");
//                }
//
//                public void onShareCancel() {
//                    LogUtil.d("huangweijie", "onShareCancel");
//                }
//            });
//        }
//        Toast.makeText(context,"分享图片到微博2:"+imagePath,Toast.LENGTH_LONG).show();
//    }

    public static void shareImageToWeiBoNoCheck(final Context context, String text, String imagePath, String webUrl, final PlatformActionListener accountValidActionListener) {
//        if (context != null) {
//            HashMap<String, Object> map = new HashMap();
//            final Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
//            map.put(ShareConstants.FEED_SOURCE_PARAM, Constants.WEIBO_APP_KEY);
//            map.put("access_token", token.getToken());
//            map.put(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS, UrlHelper.URLEncoder(text));
//            map.put("pic", new File(imagePath));
//            map.put("visible", Integer.valueOf(0));
//            UploadWeiboControll.upLoadFile(map, new ShareWeiboCallback() {
//                public void onShareComplete() {
//                    AnonymousClass1 anonymousClass1 = new Thread() {
//                        public void run() {
//                            super.run();
//                            UploadWeiboUtil.uploadWeiboByToken(context, token);
//                        }
//                    };
//                    accountValidActionListener.onComplete(null, -1, null);
//                    LogUtil.d("huangweijie", "onShareComplete");
//                }
//
//                public void onShareError(boolean again) {
//                    if (!again) {
//                        accountValidActionListener.onError(null, -1, null);
//                    }
//                    LogUtil.d("huangweijie", "onShareError");
//                }
//
//                public void onShareCancel() {
//                    LogUtil.d("huangweijie", "onShareCancel");
//                }
//            });
//        }
        Toast.makeText(context,"分享图片到微博3:"+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToTwitterGetAccountCheck(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content);
//            sp.setImagePath(imagePath);
//            Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
//            twitter.SSOSetting(false);
//            twitter.setPlatformActionListener(platformActionListener);
//            twitter.showUser(null);
//        }
        Toast.makeText(context,"分享图片到Twitter:"+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToTwitter(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content);
//            sp.setImagePath(imagePath);
//            Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
//            twitter.SSOSetting(false);
//            twitter.setPlatformActionListener(platformActionListener);
//            twitter.share(sp);
//        }
        Toast.makeText(context,"分享图片到Twitter2:"+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToTwitter(Context context, Platform twitter, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null && twitter != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content);
//            sp.setImagePath(imagePath);
//            twitter.setPlatformActionListener(platformActionListener);
//            twitter.share(sp);
//        }
        Toast.makeText(context,"分享图片到Twitter3:"+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareVideoToTwitter(Context context, Platform twitter, String text, PlatformActionListener platformActionListener) {
//        if (context != null && twitter != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(text);
//            twitter.setPlatformActionListener(platformActionListener);
//            twitter.share(sp);
//        }
        Toast.makeText(context,"分享图片到Twitter3:"+text,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToTumblrGetAccountCheck(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content);
//            sp.setImagePath(imagePath);
//            Platform tumblr = ShareSDK.getPlatform(Tumblr.NAME);
//            tumblr.SSOSetting(false);
//            tumblr.setPlatformActionListener(platformActionListener);
//            tumblr.showUser(null);
//        }
        Toast.makeText(context,"分享图片到Tumblr:"+content+","+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToTumblr(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content);
//            sp.setImagePath(imagePath);
//            Platform tumblr = ShareSDK.getPlatform(Tumblr.NAME);
//            tumblr.SSOSetting(false);
//            tumblr.setPlatformActionListener(platformActionListener);
//            tumblr.share(sp);
//        }
        Toast.makeText(context,"分享图片到Tumblr2:"+content+","+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareVideoToTumblrGetAccountCheck(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            new ShareParams().setText(content + imagePath);
//            Platform tumblr = ShareSDK.getPlatform(Tumblr.NAME);
//            tumblr.SSOSetting(false);
//            tumblr.setPlatformActionListener(platformActionListener);
//            tumblr.showUser(null);
//        }
        Toast.makeText(context,"分享图片到Tumblr3:"+content+","+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareVideoToTumblr(Context context, String content, String imagePath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText(content + imagePath);
//            Platform tumblr = ShareSDK.getPlatform(Tumblr.NAME);
//            tumblr.SSOSetting(false);
//            tumblr.setPlatformActionListener(platformActionListener);
//            tumblr.share(sp);
//        }
        Toast.makeText(context,"分享图片到Tumblr4:"+content+","+imagePath,Toast.LENGTH_LONG).show();

    }

    public static void shareImageToInstagram(Context context, String filePath) {
//        if (context != null) {
//            Intent share = new Intent("android.intent.action.SEND");
//            share.setType("image/*");
//            share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(filePath)));
//            share.setPackage(AppInfo.InstagramPackage);
//            context.startActivity(Intent.createChooser(share, "Share to"));
//        }
        Toast.makeText(context,"分享图片到Instagram："+filePath,Toast.LENGTH_LONG).show();
    }

    public static void shareVideoToInstagram(Context context, String filePath, PlatformActionListener listener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            ShareParams sp = new ShareParams();
//            sp.setText("");
//            sp.setFilePath(filePath);
//            Platform ins = ShareSDK.getPlatform(Instagram.NAME);
//            ins.setPlatformActionListener(listener);
//            ins.share(sp);
//        }
        Toast.makeText(context,"分享图片到Instagram2："+filePath,Toast.LENGTH_LONG).show();
    }

    public static void shareVideoToQQ(Context context, String filePath) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video/*");
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(filePath)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName(AppInfo.QQPackage, "com.tencent.mobileqq.activity.JumpActivity"));
        context.startActivity(intent);
    }

    public static void shareVideoToTwitter(Context context, String text, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
//            ShareParams sp = new ShareParams();
//            sp.setText(text);
//            twitter.setPlatformActionListener(platformActionListener);
//            twitter.share(sp);
//        }
        Toast.makeText(context,"分享视频到Twitter："+text,Toast.LENGTH_LONG).show();
    }

    public static void shareVideoToWechat(Context context, String videoPath, PlatformActionListener platformActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//            ShareParams shareParams = new ShareParams();
//            shareParams.setShareType(8);
//            shareParams.setFilePath(videoPath);
//            shareParams.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//            shareParams.setText(context.getResources().getString(2131296817));
//            shareParams.setTitle(context.getResources().getString(2131296817));
//            platform.setPlatformActionListener(platformActionListener);
//            platform.share(shareParams);
//        }
        Toast.makeText(context,"分享视频到微信："+videoPath,Toast.LENGTH_LONG).show();

    }

    public static void shareTextWeiBo(Context context, String text, PlatformActionListener accountValidActionListener, PlatformActionListener accountNotValidActionListener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//            if (!weibo.isAuthValid() || weibo.getDb() == null || new Date().getTime() >= weibo.getDb().getExpiresTime()) {
//                weibo.removeAccount(true);
//                weibo.SSOSetting(false);
//                weibo.setPlatformActionListener(accountNotValidActionListener);
//                weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                return;
//            }
//            HashMap<String, Object> map = new HashMap();
//            final Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
//            map.put(ShareConstants.FEED_SOURCE_PARAM, Constants.WEIBO_APP_KEY);
//            map.put("access_token", token.getToken());
//            map.put(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS, text);
//            map.put("visible", Integer.valueOf(0));
//            final Context context2 = context;
//            final PlatformActionListener platformActionListener = accountValidActionListener;
//            final PlatformActionListener platformActionListener2 = accountNotValidActionListener;
//            UploadWeiboControll.upLoadInfo(map, new ShareWeiboCallback() {
//                public void onShareComplete() {
//                    new Thread() {
//                        public void run() {
//                            super.run();
//                            UploadWeiboUtil.uploadWeiboByToken(context2, token);
//                        }
//                    }.start();
//                    platformActionListener.onComplete(null, -1, null);
//                    LogUtil.d("huangweijie", "onShareComplete");
//                }
//
//                public void onShareError(boolean again) {
//                    if (again) {
//                        weibo.SSOSetting(false);
//                        weibo.setPlatformActionListener(platformActionListener2);
//                        weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                    } else {
//                        platformActionListener.onError(null, -1, null);
//                    }
//                    LogUtil.d("huangweijie", "onShareError");
//                }
//
//                public void onShareCancel() {
//                    LogUtil.d("huangweijie", "onShareCancel");
//                }
//            });
//        }
        Toast.makeText(context,"分享文字到微博："+text,Toast.LENGTH_LONG).show();
    }

    public static void shareTextWeiBoNoCheck(final Context context, String text, final PlatformActionListener accountValidActionListener, final PlatformActionListener listener) {
//        if (context != null) {
//            HashMap<String, Object> map = new HashMap();
//            final Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
//            map.put(ShareConstants.FEED_SOURCE_PARAM, Constants.WEIBO_APP_KEY);
//            map.put("access_token", token.getToken());
//            map.put(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS, text);
//            map.put("visible", Integer.valueOf(0));
//            UploadWeiboControll.upLoadInfo(map, new ShareWeiboCallback() {
//                public void onShareComplete() {
//                    AnonymousClass1 anonymousClass1 = new Thread() {
//                        public void run() {
//                            super.run();
//                            UploadWeiboUtil.uploadWeiboByToken(context, token);
//                        }
//                    };
//                    accountValidActionListener.onComplete(null, -1, null);
//                    LogUtil.d("huangweijie", "onShareComplete");
//                }
//
//                public void onShareError(boolean again) {
//                    if (again) {
//                        MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                        weibo.SSOSetting(false);
//                        weibo.setPlatformActionListener(listener);
//                        weibo.authorize(new String[]{"email,direct_messages_read,direct_messages_write,", "friendships_groups_read,friendships_groups_write,statuses_to_me_read,", "follow_app_official_microblog,", "invitation_write"});
//                    } else {
//                        accountValidActionListener.onError(null, -1, null);
//                    }
//                    LogUtil.d("huangweijie", "onShareError");
//                }
//
//                public void onShareCancel() {
//                    LogUtil.d("huangweijie", "onShareCancel");
//                }
//            });
//        }
        Toast.makeText(context,"分享文字到微博2："+text,Toast.LENGTH_LONG).show();

    }

    public static void shareLinkToFacebook(Context context, String web_url, String text, String bitmap, PlatformActionListener listener) {
//        if (context != null) {
//            MobSDK.init(context, Constants.MOBAPPKEY, Constants.MOBAPPSECRET);
//            Platform facebook = ShareSDK.getPlatform(Facebook.NAME);
//            ShareParams sp = new ShareParams();
//            sp.setShareType(4);
//            sp.setText(text);
//            sp.setUrl(web_url);
//            sp.setImageUrl(bitmap + "-200");
//            sp.setTitle(text);
//            facebook.setPlatformActionListener(listener);
//            facebook.share(sp);
//        }
        Toast.makeText(context,"分享连接到Facebook："+text+","+web_url,Toast.LENGTH_LONG).show();

    }
}