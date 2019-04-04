package cn.nineton.onetake.bean;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.text.TextPaint;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.global.Constants;
//import com.blink.academy.onetake.support.helper.GlobalHelper;
//import com.blink.academy.onetake.support.utils.BitmapUtil;
//import com.blink.academy.onetake.support.utils.OptionSizeUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil;
//import com.blink.academy.onetake.support.videoeditimage.ImageCacheUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.util.Constants;
import cn.nineton.onetake.util.GlobalHelper;
import cn.nineton.onetake.util.ImageCacheUtils;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.OptionSizeUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.WaterMarkBitmapUtil;

public class VideoBitmapsModel {
    private static VideoBitmapsModel mVideoBitmapsModel;
    private Bitmap activeBitmap;
    private Bitmap blackBitmap;
    private int currentVideoHeight;
    private int currentVideoWidth;
    private int lastVideoHeight;
    private int lastVideoWidth;
    private Bitmap logoBitmap;
    private Bitmap oneMinuteBitmap;
    private List<String> pathList = new ArrayList();
    private HashMap<String, Bitmap> videoBitmaps = new HashMap();

    public void setCurrentWH(int[] currentWH) {
        this.currentVideoWidth = currentWH[0];
        this.currentVideoHeight = currentWH[1];
        LogUtil.e("setCurrentWH", String.format("width:%s, height:%s", new Object[]{Integer.valueOf(this.currentVideoWidth), Integer.valueOf(this.currentVideoHeight)}));
    }

    public void initializeActiveBitmaps() {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = OptionSizeUtil.computeSampleSize(options, -1, 2073600);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = false;
        options.inPurgeable = true;
        this.oneMinuteBitmap = BitmapFactory.decodeResource(App.getResource(), 2130837682, options);
        this.logoBitmap = BitmapFactory.decodeResource(App.getResource(), 2130838364, options);
    }

    private VideoBitmapsModel() {
        if (Constants.OPEN_ACTIVE_ONE_MINUTE) {
            new Thread() {
                public void run() {
                    super.run();
                    VideoBitmapsModel.this.initializeActiveBitmaps();
                }
            }.start();
        }
    }

    public static VideoBitmapsModel instancesModel() {
        if (mVideoBitmapsModel == null) {
            mVideoBitmapsModel = new VideoBitmapsModel();
        }
        return mVideoBitmapsModel;
    }

    public void setBitmap(BitmapInfo bitmapInfo) {
        if (this.videoBitmaps == null) {
            this.videoBitmaps = new HashMap();
        }
        if (this.pathList == null) {
            this.pathList = new ArrayList();
        }
        String path = bitmapInfo.getFilePath();
        if (!this.videoBitmaps.containsKey(path)) {
            this.pathList.add(path);
            this.videoBitmaps.put(path, bitmapInfo.getBitmap());
            bitmapInfo.setBitmapNULL();
        }
    }

    public void setBitmaps(List<BitmapInfo> bitmaps) {
        if (bitmaps != null && bitmaps.size() >= 1) {
            if (this.videoBitmaps == null) {
                this.videoBitmaps = new HashMap();
            }
            if (this.pathList == null) {
                this.pathList = new ArrayList();
            }
            int size = bitmaps.size();
            for (int i = 0; i < size; i++) {
                BitmapInfo info = (BitmapInfo) bitmaps.get(i);
                String path = info.getFilePath();
                if (!this.videoBitmaps.containsKey(path)) {
                    this.pathList.add(path);
                    this.videoBitmaps.put(path, info.getBitmap());
                    info.setBitmapNULL();
                }
            }
        }
    }

    public Bitmap containsFile(String filePath) {
        if (this.videoBitmaps == null || this.videoBitmaps.size() < 1 || !this.videoBitmaps.containsKey(filePath)) {
            return null;
        }
        Bitmap bitmap = (Bitmap) this.videoBitmaps.get(filePath);
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        return bitmap;
    }

    private boolean equalCurrentLast() {
        if (this.lastVideoWidth == this.currentVideoWidth && this.lastVideoHeight == this.currentVideoHeight) {
            return true;
        }
        return false;
    }

    public Bitmap getActivityBitmap() {
        if (this.activeBitmap != null && !this.activeBitmap.isRecycled() && equalCurrentLast()) {
            return this.activeBitmap;
        }
        int shortSide;
        this.lastVideoWidth = this.currentVideoWidth;
        this.lastVideoHeight = this.currentVideoHeight;
        if (this.oneMinuteBitmap == null || this.oneMinuteBitmap.isRecycled() || this.logoBitmap == null || this.logoBitmap.isRecycled()) {
            initializeActiveBitmaps();
        }
        String name = GlobalHelper.getUserScreenName();
        if (this.lastVideoHeight > this.lastVideoWidth) {
            shortSide = this.lastVideoWidth;
        } else {
            shortSide = this.lastVideoHeight;
        }
        LogUtil.d("slim", String.format("w:%s,h:%s,short:%s", new Object[]{Integer.valueOf(this.lastVideoWidth), Integer.valueOf(this.lastVideoHeight), Integer.valueOf(shortSide)}));
        this.activeBitmap = Bitmap.createBitmap(this.lastVideoWidth, this.lastVideoHeight, Config.ARGB_8888);
        Paint bitmapPaint = new Paint(2);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setDither(true);
        bitmapPaint.setFilterBitmap(true);
        Canvas canvas = new Canvas(this.activeBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.drawColor(Color.parseColor("#000000"));
        float threeWidth = ((float) shortSide) * 0.77f;
        float threeHeight = (((1.0f * ((float) this.oneMinuteBitmap.getHeight())) * ((float) shortSide)) * 0.77f) / ((float) this.oneMinuteBitmap.getWidth());
        Bitmap threeMinutesBitmap = WaterMarkBitmapUtil.getScaledWaterMarkBitmap(App.getContext(), 2130837682, threeWidth, threeHeight);
        Rect tempRect = new Rect();
        Rect textRect = new Rect();
        tempRect.left = (int) ((((float) this.activeBitmap.getWidth()) - threeWidth) / 2.0f);
        tempRect.right = (int) ((((float) this.activeBitmap.getWidth()) + threeWidth) / 2.0f);
        TextPaint namePaint = new TextPaint();
        namePaint.setTextSize(((float) shortSide) * 0.045f);
        namePaint.setFakeBoldText(false);
        namePaint.setColor(Color.parseColor("#ffffff"));
        namePaint.setTextAlign(Align.CENTER);
        namePaint.setAntiAlias(true);
        if (TextUtil.isValidate(name)) {
            String nameStr = "导演   " + name;
            tempRect.top = (int) ((((float) this.activeBitmap.getHeight()) - (((((float) shortSide) * 0.04f) + threeHeight) + ((float) WaterMarkBitmapUtil.getTextHeight(nameStr, namePaint, textRect)))) / 2.0f);
            tempRect.bottom = (int) (((float) tempRect.top) + threeHeight);
            canvas.drawText(nameStr, (float) (this.activeBitmap.getWidth() / 2), (((float) tempRect.bottom) + (((float) shortSide) * 0.04f)) + ((float) (WaterMarkBitmapUtil.getTextHeight(nameStr, namePaint, textRect) / 2)), namePaint);
        } else {
            tempRect.top = (int) ((((float) this.activeBitmap.getHeight()) - threeHeight) / 2.0f);
            tempRect.bottom = (int) ((((float) this.activeBitmap.getHeight()) + threeHeight) / 2.0f);
        }
        canvas.drawBitmap(threeMinutesBitmap, null, tempRect, bitmapPaint);
        float waterWidth = (((float) (this.logoBitmap.getWidth() * shortSide)) * 0.04f) / ((float) this.logoBitmap.getHeight());
        float waterHeight = ((float) shortSide) * 0.04f;
        Bitmap waterMark = WaterMarkBitmapUtil.tintBitmap(WaterMarkBitmapUtil.getScaledWaterMarkBitmap(App.getContext(), 2130838364, waterWidth, waterHeight), Color.parseColor("#80ffffff"));
        tempRect.left = (int) ((((float) this.activeBitmap.getWidth()) - waterWidth) / 2.0f);
        tempRect.right = (int) ((((float) this.activeBitmap.getWidth()) + waterWidth) / 2.0f);
        tempRect.bottom = (int) (((float) this.activeBitmap.getHeight()) - (((float) shortSide) * 0.08f));
        tempRect.top = (int) (((float) tempRect.bottom) - waterHeight);
        canvas.drawBitmap(waterMark, null, tempRect, bitmapPaint);
        namePaint.setTextSize(((float) shortSide) * 0.035f);
        namePaint.setColor(Color.parseColor("#80ffffff"));
        String useStr = "使用";
        String clipStr = "剪辑";
        FontMetrics fontMetrics = namePaint.getFontMetrics();
        float baseLineY = ((((float) tempRect.top) + (waterHeight / 2.0f)) + ((fontMetrics.bottom - fontMetrics.top) / 2.0f)) - fontMetrics.bottom;
        canvas.drawText(useStr, (((float) tempRect.left) - (((float) shortSide) * 0.03f)) - ((float) (WaterMarkBitmapUtil.getTextWidth(useStr, namePaint, textRect) / 2)), baseLineY, namePaint);
        canvas.drawText(clipStr, (((float) tempRect.right) + (((float) shortSide) * 0.03f)) + ((float) (WaterMarkBitmapUtil.getTextWidth(clipStr, namePaint, textRect) / 2)), baseLineY, namePaint);
        try {
            BitmapUtils.saveBitmapToLocal(this.activeBitmap, ImageCacheUtils.ONEMIN_BITMAP_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.activeBitmap;
    }

    public Bitmap getBitmap(String filePath) {
        if (TextUtil.isNull(filePath) || !new File(filePath).exists()) {
            if (this.blackBitmap == null || this.blackBitmap.isRecycled()) {
                this.blackBitmap = BitmapUtils.getBlackBitmap();
            }
            return this.blackBitmap;
        } else if (this.videoBitmaps.containsKey(filePath)) {
            return (Bitmap) this.videoBitmaps.get(filePath);
        } else {
            Bitmap pictureBitmap = BitmapUtils.getLocalBitmap(new File(filePath).getAbsolutePath());
            this.pathList.add(filePath);
            this.videoBitmaps.put(filePath, pictureBitmap);
            return pictureBitmap;
        }
    }

    public void recyclerBitmap() {
        if (!(this.blackBitmap == null || this.blackBitmap.isRecycled())) {
            this.blackBitmap.recycle();
            this.blackBitmap = null;
        }
        if (!(this.activeBitmap == null || this.activeBitmap.isRecycled())) {
            this.activeBitmap.recycle();
            this.activeBitmap = null;
        }
        if (this.videoBitmaps != null && this.videoBitmaps.size() >= 1) {
            int size = this.pathList.size();
            for (int i = 0; i < size; i++) {
                String filePath = (String) this.pathList.get(i);
                if (this.videoBitmaps.containsKey(filePath)) {
                    BitmapUtils.recycleBitmap((Bitmap) this.videoBitmaps.remove(filePath));
                }
            }
            this.pathList.clear();
        }
    }
}