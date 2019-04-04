package cn.nineton.onetake.media;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.Framebuffer;
import cn.nineton.onetake.media.gpuimage.FramebufferTexture;
import cn.nineton.onetake.media.gpuimage.OpenGlUtils;
import cn.nineton.onetake.util.Constants;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.GIFUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.PriorityRunnable;
import cn.nineton.onetake.util.PriorityThreadPoolManager;

public class BitmapUtils {
    static final String TAG = "BitmapUtils";

    public static Bitmap captureBitmap(int targetWidth, int targetHeight, final int sourceWidth, final int sourceHeight) {
        final Bitmap bitmap = Bitmap.createBitmap(sourceWidth, sourceHeight, Config.ARGB_8888);
        PixelsReader.get().lockPixels(sourceWidth, sourceHeight, new PixelsReader.LockPixels() {
            @Override
            public void run(IntBuffer intBuffer) {
                lambda$captureBitmap$0(sourceWidth, sourceHeight, bitmap, intBuffer);
            }
        });
        return scaleBitmap(bitmap, targetWidth, targetHeight);
    }

    private static /* synthetic */ void lambda$captureBitmap$0(int sourceWidth, int sourceHeight, Bitmap bitmap, IntBuffer pixels) {
        if (OpenGlUtils.currentFramebuffer() != 0) {
            Log.d(TAG, "Reading attachment 0, is this correct?");
            GLES30.glReadBuffer(36064);
        }
        if (false) {
            GLES20.glReadPixels(0, 0, sourceWidth, sourceHeight, 32993, 5121, pixels);
        } else {
            GLES20.glReadPixels(0, 0, sourceWidth, sourceHeight, 6408, 5121, pixels);
            swizzle(pixels.array(), pixels.arrayOffset(), sourceWidth * sourceHeight);
        }
        GLES30.glReadBuffer(1029);
        bitmap.setPixels(pixels.array(), pixels.arrayOffset(), sourceWidth, 0, 0, sourceWidth, sourceHeight);
    }

    public static void swizzle(int[] pixels, int offset, int size) {
        for (int i = 0; i < size; i++) {
            int value = pixels[i + offset];
            int b = (value >> 16) & 255;
            pixels[i + offset] = (((((value >> 24) & 255) << 24) | (((value >> 0) & 255) << 16)) | (((value >> 8) & 255) << 8)) | b;
        }
    }

    static float squishyRatio(int targetWidth, int targetHeight, int sourceWidth, int sourceHeight) {
        return Math.min(1.5f, Math.max(((float) sourceWidth) / ((float) targetWidth), ((float) sourceHeight) / ((float) targetHeight)));
    }

    public static Bitmap flipBitmap(Bitmap bitmap) {
        Matrix flipm = new Matrix();
        flipm.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), flipm, false);
    }

    static Bitmap scaleBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        while (bitmap.getWidth() != targetWidth && bitmap.getHeight() != targetHeight) {
            float ratio = squishyRatio(targetWidth, targetHeight, bitmap.getWidth(), bitmap.getHeight());
            if (((double) ratio) < 1.5d) {
                return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            }
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, (int) Math.ceil((double) (((float) bitmap.getWidth()) / ratio)), (int) Math.ceil((double) (((float) bitmap.getHeight()) / ratio)), true);
            bitmap.recycle();
            bitmap = bitmap2;
        }
        return bitmap;
    }

    public static void savePixels(String filename, int width, int height) {
        saveBitmap(filename, captureBitmap(width, height, width, height));
    }

    public static void saveBitmap(String filename, Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory() + "/" + filename;
        Log.d(TAG, String.format("saving bitmap to %s", new Object[]{path}));
        try {
            FileOutputStream os = new FileOutputStream(path);
            bitmap.compress(CompressFormat.PNG, 100, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void saveTexture(String filename, OutputSurfaceArray.Buffer buffer) {
        saveTexture(filename, buffer.mTextureId, buffer.mWidth, buffer.mHeight);
    }

    public static void saveTexture(String filename, FramebufferTexture tex) {
        saveTexture(filename, tex.texid(), tex.getWidth(), tex.getHeight());
    }

    public static void saveTexture(final String filename, final int texid, final int width, final int height) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                EGL10Helper egl = null;
                try {
                    egl = EGL10Helper.create("save-texture");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                try {
                    egl.makeDefaultCurrent();
                    Framebuffer fb = new Framebuffer("saveTexture", new FramebufferTexture(width, height, 0, texid));
                    GLES20.glBindFramebuffer(36160, fb.fboid());
                    GLES30.glReadBuffer(36064);
                    Bitmap b = BitmapUtils.captureBitmap(width, height, width, height);
                    BitmapUtils.saveBitmap(filename, b);
                    b.recycle();
                    fb.destroy();
                } finally {
                    egl.release();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }


    public interface SaveBitmapCallback {
        void onSaveError(String str);

        void onSaveFinish(String str);
    }

    public static void release(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && (drawable instanceof BitmapDrawable)) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (!(bmp == null || bmp.isRecycled())) {
                    bmp.recycle();
                }
            }
            imageView.setImageBitmap(null);
            if (drawable != null) {
                drawable.setCallback(null);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0068 A:{SYNTHETIC, Splitter: B:41:0x0068} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x006d A:{SYNTHETIC, Splitter: B:44:0x006d} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0068 A:{SYNTHETIC, Splitter: B:41:0x0068} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x006d A:{SYNTHETIC, Splitter: B:44:0x006d} */
    public static android.graphics.Bitmap getBitmapFromFile(java.lang.String r12, int r13) {
        /*
        r9 = 1;
        r2 = 0;
        r4 = 0;
        r0 = 0;
        r1 = new java.io.File;	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r1.<init>(r12);	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r6 = new android.graphics.BitmapFactory$Options;	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r6.<init>();	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r10 = 1;
        r6.inJustDecodeBounds = r10;	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r3 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r3.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0057, all -> 0x0065 }
        r10 = 0;
        android.graphics.BitmapFactory.decodeStream(r3, r10, r6);	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r8 = 1;
        r10 = r6.outWidth;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        if (r10 > r13) goto L_0x0023;
    L_0x001f:
        r10 = r6.outHeight;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        if (r10 <= r13) goto L_0x002d;
    L_0x0023:
        r10 = r6.outWidth;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r11 = r6.outHeight;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        if (r10 <= r11) goto L_0x004e;
    L_0x0029:
        r10 = r6.outWidth;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r8 = r10 / r13;
    L_0x002d:
        r7 = new android.graphics.BitmapFactory$Options;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r7.<init>();	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        if (r8 != 0) goto L_0x0035;
    L_0x0034:
        r8 = r9;
    L_0x0035:
        r7.inSampleSize = r8;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r5 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r5.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r9 = 0;
        r0 = android.graphics.BitmapFactory.decodeStream(r5, r9, r7);	 Catch:{ FileNotFoundException -> 0x0083, all -> 0x007c }
        if (r3 == 0) goto L_0x0046;
    L_0x0043:
        r3.close();	 Catch:{ IOException -> 0x0071 }
    L_0x0046:
        if (r5 == 0) goto L_0x0087;
    L_0x0048:
        r5.close();	 Catch:{ IOException -> 0x0053 }
        r4 = r5;
        r2 = r3;
    L_0x004d:
        return r0;
    L_0x004e:
        r10 = r6.outHeight;	 Catch:{ FileNotFoundException -> 0x0080, all -> 0x0079 }
        r8 = r10 / r13;
        goto L_0x002d;
    L_0x0053:
        r9 = move-exception;
        r4 = r5;
        r2 = r3;
        goto L_0x004d;
    L_0x0057:
        r9 = move-exception;
    L_0x0058:
        if (r2 == 0) goto L_0x005d;
    L_0x005a:
        r2.close();	 Catch:{ IOException -> 0x0073 }
    L_0x005d:
        if (r4 == 0) goto L_0x004d;
    L_0x005f:
        r4.close();	 Catch:{ IOException -> 0x0063 }
        goto L_0x004d;
    L_0x0063:
        r9 = move-exception;
        goto L_0x004d;
    L_0x0065:
        r9 = move-exception;
    L_0x0066:
        if (r2 == 0) goto L_0x006b;
    L_0x0068:
        r2.close();	 Catch:{ IOException -> 0x0075 }
    L_0x006b:
        if (r4 == 0) goto L_0x0070;
    L_0x006d:
        r4.close();	 Catch:{ IOException -> 0x0077 }
    L_0x0070:
        throw r9;
    L_0x0071:
        r9 = move-exception;
        goto L_0x0046;
    L_0x0073:
        r9 = move-exception;
        goto L_0x005d;
    L_0x0075:
        r10 = move-exception;
        goto L_0x006b;
    L_0x0077:
        r10 = move-exception;
        goto L_0x0070;
    L_0x0079:
        r9 = move-exception;
        r2 = r3;
        goto L_0x0066;
    L_0x007c:
        r9 = move-exception;
        r4 = r5;
        r2 = r3;
        goto L_0x0066;
    L_0x0080:
        r9 = move-exception;
        r2 = r3;
        goto L_0x0058;
    L_0x0083:
        r9 = move-exception;
        r4 = r5;
        r2 = r3;
        goto L_0x0058;
    L_0x0087:
        r4 = r5;
        r2 = r3;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.BitmapUtil.getBitmapFromFile(java.lang.String, int):android.graphics.Bitmap");
    }

    public static void release(Drawable drawable) {
        if (drawable != null && (drawable instanceof BitmapDrawable)) {
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            if (!(bmp == null || bmp.isRecycled())) {
                bmp.recycle();
            }
        }
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    public static Bitmap getBitmapFromResource(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    public static Bitmap getBitmapFromByteArray(byte[] array) {
        Bitmap bitmap = null;
        if (array == null) {
            return bitmap;
        }
        try {
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError e2) {
        }
        GC();
        return bitmap;
    }

    public static Bitmap squareScaleBitmap(Bitmap bitmap, float width, float height) {
        if (bitmap == null) {
            return null;
        }
        try {
            return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        } catch (OutOfMemoryError e) {
            GC();
            return bitmap;
        }
    }

    public static void GC() {
        System.gc();
    }

    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int wh;
        int retX;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w > h) {
            wh = h;
        } else {
            wh = w;
        }
        if (w > h) {
            retX = (w - h) / 2;
        } else {
            retX = 0;
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, w > h ? 0 : (h - w) / 2, wh, wh, null, false);
        if (!(!isRecycled || bitmap.equals(bmp) || bitmap.isRecycled())) {
            bitmap.recycle();
        }
        return bmp;
    }

    public static Bitmap adjustPhotoRotation(Bitmap bitmap, int orientationDegree) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float) orientationDegree);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        System.gc();
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Config.RGB_565;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        LogUtil.d("decodeSampleBitmapFromResource", String.format("inSampleSize : %s ", new Object[]{Integer.valueOf(options.inSampleSize)}));
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static void saveBitmapToLocal(Bitmap bitmap, String path) throws IOException {
        File pathFile = new File(path);
        if (!pathFile.getParentFile().exists()) {
            pathFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(path)) {
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            Log.e("debug", "saveBitmap success:" + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048 A:{SYNTHETIC, Splitter: B:15:0x0048} */
//    public static void saveBitmapToLocal(android.graphics.Bitmap r8, java.lang.String r9) throws java.io.IOException {
        /*
        r1 = new java.io.File;
        r1.<init>(r9);
        r4 = r1.getParentFile();
        r5 = r1.exists();
        if (r5 != 0) goto L_0x0012;
    L_0x000f:
        r4.mkdirs();
    L_0x0012:
        r2 = 0;
        r3 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0045 }
        r3.<init>(r9);	 Catch:{ all -> 0x0045 }
        r5 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ all -> 0x0051 }
        r6 = 100;
        r8.compress(r5, r6, r3);	 Catch:{ all -> 0x0051 }
        r3.flush();	 Catch:{ all -> 0x0051 }
        r5 = "saveBitmapToLocal";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0051 }
        r6.<init>();	 Catch:{ all -> 0x0051 }
        r7 = "save success : filePath : ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0051 }
        r6 = r6.append(r9);	 Catch:{ all -> 0x0051 }
        r6 = r6.toString();	 Catch:{ all -> 0x0051 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r5, r6);	 Catch:{ all -> 0x0051 }
        if (r3 == 0) goto L_0x003f;
    L_0x003c:
        r3.close();	 Catch:{ IOException -> 0x0040 }
    L_0x003f:
        return;
    L_0x0040:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x003f;
    L_0x0045:
        r5 = move-exception;
    L_0x0046:
        if (r2 == 0) goto L_0x004b;
    L_0x0048:
        r2.close();	 Catch:{ IOException -> 0x004c }
    L_0x004b:
        throw r5;
    L_0x004c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x004b;
    L_0x0051:
        r5 = move-exception;
        r2 = r3;
        goto L_0x0046;
        */
    //throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.BitmapUtil.saveBitmapToLocal(android.graphics.Bitmap, java.lang.String):void");
//    }

    public static void saveBitmapToLocal(final Bitmap bitmap, final String filePath, final SaveBitmapCallback saveBitmapCallback) {
        PriorityThreadPoolManager.execute(new PriorityRunnable(9) {
            public void run() {
                try {
                    saveBitmapToLocal(bitmap, filePath);
                    if (saveBitmapCallback != null) {
                        saveBitmapCallback.onSaveFinish(filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (saveBitmapCallback != null) {
                        saveBitmapCallback.onSaveError(filePath);
                    }
                }
            }
        });
    }

    private static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.PNG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > size && options != 0) {
            baos.reset();
            image.compress(CompressFormat.PNG, options, baos);
            options -= 10;
        }
        LogUtil.d("compressImage", "baos.toByteArray().length / 1024 : " + (baos.toByteArray().length / 1024));
        return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        if (x < 0 || y < 0 || width <= 0 || height <= 0 || x + width > source.getWidth() || y + height > source.getHeight()) {
            return null;
        }
        return Bitmap.createBitmap(source, x, y, width, height, m, filter);
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        return createBitmap(source, x, y, width, height, null, false);
    }

    public static Bitmap getBitmapForLeakFilter(Resources resources, int resId, int reqWidth, int reqHeight) {
        int tempWidth;
        int tempHeight;
        Bitmap bitmap = null;
        if (reqWidth == 0 || reqHeight == 0) {
            reqWidth = DensityUtil.getMetricsWidth(App.getContext());
            reqHeight = DensityUtil.getMetricsHeight(App.getContext());
        }
        if (reqWidth > reqHeight) {
            tempWidth = reqWidth;
        } else {
            tempWidth = reqHeight;
        }
        if (reqWidth > reqHeight) {
            tempHeight = reqHeight;
        } else {
            tempHeight = reqWidth;
        }
        LogUtil.d("decodeSampleBitmapFromResource", String.format("tempWidth : %s , tempHeiht : %s ", new Object[]{Integer.valueOf(tempWidth), Integer.valueOf(tempHeight)}));
        try {
            bitmap = decodeSampledBitmapFromResource(resources, resId, tempWidth, tempHeight);
            LogUtil.d("decodeSampleBitmapFromResource", String.format("1 Width : %s , Height : %s ", new Object[]{Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight())}));
            int finalWidth = bitmap.getWidth();//.width;
            int finalHeight = bitmap.getHeight();//height;
            bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth - (finalWidth & 1), finalHeight - (finalHeight & 1), false);
            LogUtil.d("decodeSampleBitmapFromResource", String.format("3 Width : %s , Height : %s ", new Object[]{Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight())}));
            System.gc();
            return bitmap;
        } catch (OutOfMemoryError e) {
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            e.printStackTrace();
            System.gc();
            return null;
        }
    }

    public static Bitmap compressBitmap1200(String path, boolean isNeedAddWaterWarket) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            return null;
        }
        Bitmap compressBitmap;
        Bitmap finalBitmap;
        int photoWidth = bitmap.getWidth();
        int photoHeight = bitmap.getHeight();
        if (photoWidth <= Constants.ImageTypeSize || photoHeight <= Constants.ImageTypeSize) {
            compressBitmap = bitmap;
        } else {
            float scale;
            if (photoWidth > photoHeight) {
                scale = 1200.0f / ((float) photoHeight);
            } else {
                scale = 1200.0f / ((float) photoWidth);
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            compressBitmap = Bitmap.createBitmap(bitmap, 0, 0, photoWidth, photoHeight, matrix, true);
            recycleBitmap(bitmap);
        }
        int width1 = compressBitmap.getWidth();
        LogUtil.d("compressBitmap1200", "compressBitmap : width1 : " + width1 + "   height1 : " + compressBitmap.getHeight());
        int compressWidth = compressBitmap.getWidth();
        int compressHeight = compressBitmap.getHeight();
        if (isNeedAddWaterWarket) {
            int longSide;
            if (compressWidth > compressHeight) {
                longSide = compressWidth;
            } else {
                longSide = compressHeight;
            }
            float finalHeight = ((float) longSide) * GIFUtil.addPicHeightProportion;
            Bitmap addPic = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.watermark_en);
            addPic = Bitmap.createScaledBitmap(addPic, (int) (((((float) addPic.getWidth()) * 1.0f) / ((float) addPic.getHeight())) * finalHeight), (int) finalHeight, false);
            finalBitmap = GIFUtil.getBitmapWithAddPic(compressBitmap, addPic);
            recycleBitmap(addPic);
        } else {
            finalBitmap = compressBitmap;
        }
        return finalBitmap;
    }

    public static Bitmap getTargetWidthHeightCropCenterBitmap(String filePath, int tarWidth, int tarHeight) {
        if (filePath == null || !new File(filePath).exists()) {
            return null;
        }
        Bitmap resultBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, tarWidth, tarHeight);
        LogUtil.d("getTargetWidthHeightCropCenterBitmap", String.format("inSampleSize : %s ", new Object[]{Integer.valueOf(options.inSampleSize)}));
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            tarWidth = (int) (((((double) width) * 1.0d) / ((double) height)) * ((double) tarHeight));
        } else {
            tarHeight = (int) (((double) tarWidth) / ((((double) width) * 1.0d) / ((double) height)));
        }
        LogUtil.d("getTargetWidthHeightCropCenterBitmap", String.format("tarWidth : %s , tarHeight : %s , width : %s , height : %s ", new Object[]{Integer.valueOf(tarHeight), Integer.valueOf(tarHeight), Integer.valueOf(width), Integer.valueOf(height)}));
        tarWidth -= tarWidth & 1;
        tarHeight -= tarHeight & 1;
        bitmap = Bitmap.createScaledBitmap(bitmap, tarWidth, tarHeight, false);
        if (tarHeight > tarWidth) {
            resultBitmap = Bitmap.createBitmap(bitmap, 0, (tarHeight - tarWidth) / 2, tarWidth, tarWidth);
        } else {
            resultBitmap = Bitmap.createBitmap(bitmap, (tarWidth - tarHeight) / 2, 0, tarHeight, tarHeight);
        }
        bitmap.recycle();
        return resultBitmap;
    }

    private static Bitmap getLocalPreviewBitmap(String filePath, int needLength) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            return null;
        }
        int i;
        LogUtil.d("getLocalBitmap", "Start measure time");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int scale = 1;
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > needLength && height > needLength) {
            scale = width > height ? height / needLength : width / needLength;
        }
        LogUtil.d("getLocalBitmap", String.format("width : %s , height : %s , scale : %s ", new Object[]{Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(scale)}));
        if (scale > 4) {
            scale = 4;
        }
        if (scale == 0) {
            i = 1;
        } else {
            i = scale;
        }
        options.inSampleSize = i;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        Bitmap result = createScaleTotalWidthAndHeight(bitmap, needLength);
        if (result == null) {
            return null;
        }
        int mOrientation = getLocalPictureOrientation(filePath);
        if (1 == mOrientation) {
            mOrientation = 0;
        } else if (6 == mOrientation) {
            mOrientation = 90;
        } else if (8 == mOrientation) {
            mOrientation = BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
        } else if (3 == mOrientation) {
            mOrientation = 180;
        }
        result = adjustPhotoRotation(result, mOrientation);
        LogUtil.d("getLocalBitmap", String.format("result width : %s , height : %s , scale : %s ", new Object[]{Integer.valueOf(result.getWidth()), Integer.valueOf(result.getHeight()), Integer.valueOf(scale)}));
        if (result != bitmap) {
            recycleBitmap(bitmap);
        }
        LogUtil.d("getLocalBitmap", "End measure time");
        return result;
    }

    public static Bitmap getScaledBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public static Bitmap getLocalBitmap(String filePath) {
        return getLocalPreviewBitmap(filePath, DensityUtil.dip2px(44.0f));
    }

    public static Bitmap getMediaBitmap(String filePath, long startDuration) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            return null;
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filePath);
        long duration = Long.parseLong(mmr.extractMetadata(9));
        long start = startDuration;
        if (start > duration) {
            start = duration;
        }
        Bitmap frameBitmap = mmr.getFrameAtTime(1000 * start, 0);
        mmr.release();
        Bitmap result = createScaleTotalWidthAndHeight(frameBitmap, DensityUtil.dip2px(44.0f));
        if (result == frameBitmap) {
            return result;
        }
        recycleBitmap(frameBitmap);
        return result;
    }

    private static Bitmap createScaleTotalWidthAndHeight(Bitmap bitmap, int totalLength) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= totalLength || height <= totalLength) {
            return bitmap;
        }
        if (width > height) {
            int needWidth = (int) (((double) width) / ((((double) height) * 1.0d) / ((double) totalLength)));
            return Bitmap.createScaledBitmap(bitmap, needWidth - (needWidth & 1), totalLength - (totalLength & 1), false);
        }
        int needHeight = (int) (((double) height) / ((((double) width) * 1.0d) / ((double) totalLength)));
        return Bitmap.createScaledBitmap(bitmap, totalLength - (totalLength & 1), needHeight - (needHeight & 1), false);
    }

    @TargetApi(19)
    public static int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap getBlackBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(180, 320, Config.RGB_565);
        bitmap.eraseColor(Color.parseColor("#000000"));
        return bitmap;
    }

    public static int getLocalPictureOrientation(String filePath) {
        IOException e;
        int i = 0;
        if (!new File(filePath).exists()) {
            return i;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            return Integer.parseInt(exifInterface.getAttribute("Orientation"));
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return i;
        }
    }

    public static void saveBitmap2PrivateFile(Bitmap bitmap) {
        String path = cn.nineton.onetake.util.Config.getPrivatePreviewFilterPath();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        } else {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                boolean b = bitmap.compress(CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}