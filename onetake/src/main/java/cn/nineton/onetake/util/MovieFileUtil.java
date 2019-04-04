package cn.nineton.onetake.util;

import android.annotation.SuppressLint;
import java.io.File;

public class MovieFileUtil {
    public static String getOutputMediaFile(String timeStamp) {
        return String.format("%1$s%2$s.mp4", new Object[]{Config.getPrivatePath(), timeStamp});
    }

    public static String getOutputAvatarFile(String currentTime) {
        return String.format("%1$s%2$s.jpg", new Object[]{Config.getPrivatePath(), currentTime});
    }

    @SuppressLint({"DefaultLocale"})
    public static String getJPGFilePathWithIndex(String timeStamp, int index) {
        return String.format("%1$s/%2$d.jpg", new Object[]{getCurrentTimeStampFilePath(timeStamp), Integer.valueOf(index)});
    }

    public static String getCurrentTimeStampFilePath(String timeStamp) {
        String timeStampFilePath = String.format("%1$s%2$s", new Object[]{Config.getPrivatePath(), timeStamp});
        File file = new File(timeStampFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return timeStampFilePath;
    }

    public static String getMovieJsonFilePath(String timeStamp) {
        return String.format("%1$s/movieLocation.json", new Object[]{getCurrentTimeStampFilePath(timeStamp)});
    }

    public static String getLongThumbnailFilePath(String timeStamp) {
        return String.format("%1$s/long_thumbnail.jpg", new Object[]{getCurrentTimeStampFilePath(timeStamp)});
    }

    public static String getMoreShortLongThumbnailFilePath(String timeStamp) {
        return String.format("%1$s/more_short_long_thumbnail.jpg", new Object[]{getCurrentTimeStampFilePath(timeStamp)});
    }

    public static String getOriginalFilePath(String timeStamp) {
        return String.format("%1$s/original.jpg", new Object[]{getCurrentTimeStampFilePath(timeStamp)});
    }

    public static String getOriginalSDFilePath(String timeStamp) {
        return Config.getPhotoSavePath() + timeStamp + ".jpg";
    }
}