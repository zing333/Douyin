package cn.nineton.onetake.util;

import java.util.Locale;

import cn.nineton.onetake.http.UrlHelper;

public class ImageUtil {
    public static final String DefaultAvatar = "";
    public static final String DefaultNotificationPhoto = getDefaultResource();
    public static final String DefaultPhoto = getDefaultResource();

    private static String getDefaultResource() {
        return UrlHelper.EmptyUrl;
    }

    public static String getFilterStoreUrl(String cover_url) {
        if (TextUtil.isNull(cover_url)) {
            return cover_url;
        }
        return String.format("%1$s/%2$s", new Object[]{cover_url, Constants.FilterImageSize2});
    }

    public static String getPreviewUrl(String previewUrl) {
        if (TextUtil.isNull(previewUrl)) {
            return previewUrl;
        }
        return String.format(Locale.getDefault(), "%1$s/%2$d", new Object[]{previewUrl, Integer.valueOf(600)});
    }

    public static String getImageTypeUrl(String imageUrl) {
        if (TextUtil.isNull(imageUrl)) {
            return imageUrl;
        }
        return String.format(Locale.getDefault(), "%1$s/%2$d", new Object[]{imageUrl, Integer.valueOf(Constants.ImageTypeSize)});
    }

    public static String getAvatarThumbnailsUrl(String avatarThumbnailsurl) {
        if (TextUtil.isNull(avatarThumbnailsurl)) {
            return avatarThumbnailsurl;
        }
        return String.format(Locale.getDefault(), "%1$s/%2$d", new Object[]{avatarThumbnailsurl, Integer.valueOf(100)});
    }

    public static String getLargeAvatarThumbnailsUrl(String avatarThumbnailsurl) {
        if (TextUtil.isNull(avatarThumbnailsurl)) {
            return avatarThumbnailsurl;
        }
        return String.format(Locale.getDefault(), "%1$s/%2$d", new Object[]{avatarThumbnailsurl, Integer.valueOf(200)});
    }

    public static String getUserBadgeThumbnailsUrl(String imageUrl) {
        if (TextUtil.isNull(imageUrl)) {
            return imageUrl;
        }
        return String.format(Locale.getDefault(), "%1$s/%2$d", new Object[]{imageUrl, Integer.valueOf(200)});
    }

    public static String getTagImageUrl(String imageUrl) {
        if (TextUtil.isNull(imageUrl)) {
            return imageUrl;
        }
        return String.format("%1$s/%2$s", new Object[]{imageUrl, Constants.TagImageSize});
    }

    public static String getMusicStoreImageUrl(String url) {
        if (TextUtil.isNull(url)) {
            return url;
        }
        return String.format("%1$s/%2$s", new Object[]{url, Constants.MusicStoreSize});
    }

    public static String getLongThumbnailUrl(String long_thumbnail_url) {
        if (TextUtil.isNull(long_thumbnail_url)) {
            return long_thumbnail_url;
        }
        return String.format("%1$s%2$s", new Object[]{long_thumbnail_url, getLongBitmapSize()});
    }

    public static String getCollectionBigThumbnailUrl(String long_thumbnail_url) {
        if (TextUtil.isNull(long_thumbnail_url)) {
            return long_thumbnail_url;
        }
        return String.format("%1$s%2$s", new Object[]{long_thumbnail_url, getCollectionBigThumbnailsSize()});
    }

    public static String getAvatorUrl(String avatarUrl) {
        return TextUtil.isNull(avatarUrl) ? null : avatarUrl + getAvatarThumbnailsSize();
    }

    public static String getCollectionCoverUrl(String cover) {
        return TextUtil.isNull(cover) ? cover : cover + getCoverImageSize();
    }

    public static String getPhotoSize() {
        return getSquareThumbSize(1000);
    }

    public static String getLongBitmapSize() {
        return getStickersThumbnailsSize();
    }

    public static String getAvatarThumbnailsSize() {
        return getSquareThumbSize(100);
    }

    public static String getCoverImageSize() {
        return "/100square";
    }

    public static String getUserThumbnailsSize() {
        return getSquareThumbSize(300);
    }

    public static String getStickersThumbnailsSize() {
        return getSquareThumbSize(200);
    }

    public static String getCollectionBigThumbnailsSize() {
        return getSquareThumbSize(600);
    }

    public static String getFilterImageSize() {
        return getSquareThumbSize(1500);
    }

    public static String getSquareThumbSize(int length) {
        return getThumbSize(length);
    }

    public static String getThumbSize(int length) {
        return "/" + length;
    }
}