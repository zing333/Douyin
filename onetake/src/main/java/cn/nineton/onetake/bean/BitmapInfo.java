package cn.nineton.onetake.bean;

import android.graphics.Bitmap;

public class BitmapInfo {
    private Bitmap bitmap;
    private String filePath;

    public BitmapInfo(String filePath, Bitmap bitmap) {
        this.filePath = filePath;
        this.bitmap = bitmap;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmapNULL() {
        this.bitmap = null;
    }
}