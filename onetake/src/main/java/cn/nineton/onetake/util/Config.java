package cn.nineton.onetake.util;

import android.os.Build;
import java.io.File;

public class Config {
    public static String APP_FILER_PATH = null;
    public static String APP_FONTS_ASSETS_PATH = null;
    public static String APP_FONTS_JSON_ASSETS_PATH = null;
    public static String APP_SPECIFIC_COMBINATIONS_JSON_ASSETS_PATH = null;
    private static String AUDIOS_DOWNLOAD_PATH = null;
    public static String BASHIC_PATH = null;
    public static final String CACHE_CLOSED_BANNER_PATH = "close_banner_ids";
    public static final String CACHE_CLOSED_DISCOVER_BANNER_PATH = "close_discover_banner_ids";
    public static final String CACHE_TIMELINE_BANNER_DATA = "timeline_banner_data";
    public static String ChineseGifFileName = null;
    public static String ChinesePhotoFileName = null;
    public static String ChineseVideoFileName = null;
    private static String DRAFTS_PATH = null;
    public static String EnglishGifFileName = null;
    public static String EnglishPhotoFileName = null;
    public static String EnglishVideoFileName = null;
    private static String FILTERS_DOWNLOAD_PATH = null;
    public static final String FacebookInstall = "facebook_install";
    public static final String FacebookPackage = "com.facebook.katana";
    public static String GifFileName = null;
    public static final String InstagramInstall = "instagram_install";
    public static final String InstagramPackage = "com.instagram.android";
    private static String LONG_VIDEO_RECORD_PATH = null;
    public static final String MessengerInstall = "messenger_install";
    public static final String MessengerPackage = "com.facebook.orca";
    private static String PRIVATE_PATH = null;
    public static String PhotoFileName = null;
    public static final String QQInstall = "qq_install";
    public static final String QQPackage = "com.tencent.mobileqq";
    public static final boolean SHOW_ALBUM_ENTRANCE = false;
    public static final String TwitterInstall = "twitter_install";
    public static final String TwitterPackage = "com.twitter.android";
    public static String VideoFileName = null;
    public static final String WeChatInstall = "wechat_install";
    public static final String WeChatPackage = "com.tencent.mm";
    public static final String WeiboPackage = "com.sina.weibo";

    public static String getLocalFilterParenPath() {
        return FILTERS_DOWNLOAD_PATH;
    }

    public static boolean getLocalFilterPath(int filterId) {
        return new File(FILTERS_DOWNLOAD_PATH + filterId + "/" + filterId + ".zip").exists();
    }

    public static String getLongVideoRecordPath() {
        File file = new File(LONG_VIDEO_RECORD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getFiltersDownloadPath(int filterId) {
        File file = new File(FILTERS_DOWNLOAD_PATH + filterId + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getAudiosDownloadPath() {
        File file = new File(AUDIOS_DOWNLOAD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getPrivatePreviewFilterPath() {
        return getPrivatePath() + "FilterPreview.jpg";
    }

    public static String getPrivatePath() {
        File fileDir = new File(PRIVATE_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return PRIVATE_PATH;
    }

    public static String getDraftsPath() {
        File fileDir = new File(DRAFTS_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return DRAFTS_PATH;
    }

    public static String getFontFile(String language, String filename, String fonttype) {
        String root = getPrivatePath() + APP_FONTS_ASSETS_PATH;
        File cnDir = new File(root + "cn");
        File enDir = new File(root + "en");
        if (!cnDir.exists()) {
            cnDir.mkdirs();
        }
        if (!enDir.exists()) {
            enDir.mkdirs();
        }
        return root + language + "/" + filename + "." + fonttype;
    }

    public static void initFontDestPath() {
        String root = getPrivatePath() + APP_FONTS_ASSETS_PATH;
        File cnDir = new File(root + "cn");
        File enDir = new File(root + "en");
        if (!cnDir.exists()) {
            cnDir.mkdirs();
        }
        if (!enDir.exists()) {
            enDir.mkdirs();
        }
    }

    public static String getFontDestPath(String language) {
        String root = getPrivatePath() + APP_FONTS_ASSETS_PATH;
        File cnDir = new File(root + "cn");
        File enDir = new File(root + "en");
        if (!cnDir.exists()) {
            cnDir.mkdirs();
        }
        if (!enDir.exists()) {
            enDir.mkdirs();
        }
        return root + language + "/";
    }

    public static String getVideoSavePath() {
        String filename;
        if (new File(BASHIC_PATH + "/" + ChineseVideoFileName).exists()) {
            filename = ChineseVideoFileName;
        } else if (new File(BASHIC_PATH + "/" + EnglishVideoFileName).exists()) {
            filename = EnglishVideoFileName;
        } else {
            filename = VideoFileName;
        }
        String savePath = BASHIC_PATH + "/" + filename + "/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getGifSavePath() {
        String filename;
        if (new File(BASHIC_PATH + "/" + ChineseGifFileName).exists()) {
            filename = ChineseGifFileName;
        } else if (new File(BASHIC_PATH + "/" + EnglishGifFileName).exists()) {
            filename = EnglishGifFileName;
        } else {
            filename = GifFileName;
        }
        String savePath = BASHIC_PATH + "/" + filename + "/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getPhotoSavePath() {
        String filename;
        if (new File(BASHIC_PATH + "/" + ChinesePhotoFileName).exists()) {
            filename = ChinesePhotoFileName;
        } else if (new File(BASHIC_PATH + "/" + EnglishPhotoFileName).exists()) {
            filename = EnglishPhotoFileName;
        } else {
            filename = PhotoFileName;
        }
        String savePath = BASHIC_PATH + "/" + filename + "/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getVideoCachePath() {
        String videoCachePath = getVideoSavePath() + "/.cache";
        File fileDir = new File(videoCachePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return videoCachePath;
    }

    public static String getGifCachePath() {
        String gifCachePath = getGifSavePath() + "/.cache";
        File fileDir = new File(gifCachePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return gifCachePath;
    }

    public static String getPhotoCachePath() {
        long currentTimeMillis = System.currentTimeMillis();
        String gifCachePath = getPhotoSavePath() + "/.cache";
        File fileDir = new File(gifCachePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return gifCachePath + "/" + currentTimeMillis + ".jpg";
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getThumbnailPath() {
        String savePath = getGifSavePath() + "/.thumbail/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getTimelineBannerCachePath() {
        String savePath = getPrivatePath() + "/_cache/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getDiscoverBannerCachePath() {
        String savePath = getPrivatePath() + "/_cache/discover";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getTestSavePath() {
        String savePath = BASHIC_PATH + "/xiaoluo/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getFilterActivityTempImgSavePath() {
        String savePath = getPrivatePath() + "/filtertempimg/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getAlbumCoverSavePath() {
        String savePath = getPrivatePath() + "/albumcover/";
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return savePath;
    }

    public static String getOneMinuteAudioFilePath() {
        return getPrivatePath() + "/promotion/oneminute.wav";
    }

    public static String getProxyVideoFilePath() {
        String filePath = getPrivatePath() + "/proxy/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    public static String getProxySaveInfoPath() {
        String filePath = getPrivatePath() + "/proxyInfo/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }
}