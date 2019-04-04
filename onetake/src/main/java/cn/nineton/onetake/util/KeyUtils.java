package cn.nineton.onetake.util;

import com.bumptech.glide.util.Util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyUtils {
    public static String getGlideCacheFileName(String videoUrl) {
        String fileName = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(videoUrl.getBytes());
            return Util.sha256BytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return fileName;
        }
    }
}