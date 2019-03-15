package cn.nineton.onetake.widget;

import android.graphics.Bitmap;

public class ShareBitmapEntity {
    private Bitmap bitmap;
    private Float[] size;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Float[] getSize() {
        return this.size;
    }

    public void setSize(Float[] size) {
        this.size = size;
    }

    public void recyclerBitmap() {
        if (!(this.bitmap == null || this.bitmap.isRecycled())) {
            this.bitmap.recycle();
        }
        this.bitmap = null;
    }
}