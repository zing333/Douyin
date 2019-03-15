package cn.nineton.onetake.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;
import java.io.FileOutputStream;

import cn.nineton.onetake.widget.ShareActionSheetDialog;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class GIFUtil {
    private static final int LongLength = 600;
    private static final int ShortLength = 300;
    private static final int SpecialLength = 200;
    private static final int SquareLength = 250;
    public static float addPicHeightProportion = 0.032f;
    public static float distanceProportion = 0.5f;
    public static float videoBtnProportion = 0.284f;

    public static void mp4ToGifForSave(Activity activity, String videoFilePath, int videoWidth, int videoHeight, int duration, ShareActionSheetDialog.OnHandlerMp4ToGifListener listener, int photoId) {
        mp4ToGif(activity, videoFilePath, videoWidth, videoHeight, duration, "", false, false, StaticLayoutUtil.DefaultSpacingadd, listener, true, photoId, false, true, false);
    }

    public static void mp4ToGifForSave(String videoFilePath, int videoWidth, int videoHeight, int duration, ShareActionSheetDialog.OnHandlerMp4ToGifListener listener, int photoId, boolean isNeedAddWaterMark) {
        mp4ToGif(null, videoFilePath, videoWidth, videoHeight, duration, "", false, false, StaticLayoutUtil.DefaultSpacingadd, listener, true, photoId, false, isNeedAddWaterMark, false);
    }

    public static void mp4ToGif(android.app.Activity r64, java.lang.String r65, int r66, int r67, int r68, java.lang.String r69, boolean r70, boolean r71, float r72, ShareActionSheetDialog.OnHandlerMp4ToGifListener r73, boolean r74, int r75, boolean r76, boolean r77, boolean r78) {
        /*
        if (r64 == 0) goto L_0x001c;
    L_0x0002:
        r6 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r0 = r64;
        r6 = android.support.v4.content.ContextCompat.checkSelfPermission(r0, r6);
        if (r6 == 0) goto L_0x001c;
    L_0x000c:
        r6 = 1;
        r6 = new java.lang.String[r6];
        r12 = 0;
        r13 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r6[r12] = r13;
        r12 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r0 = r64;
        android.support.v4.app.ActivityCompat.requestPermissions(r0, r6, r12);
    L_0x001b:
        return;
    L_0x001c:
        if (r73 == 0) goto L_0x0021;
    L_0x001e:
        r73.onStart();
    L_0x0021:
        r44 = java.lang.System.currentTimeMillis();
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "Start:duration:";
        r12 = r12.append(r13);
        r0 = r68;
        r12 = r12.append(r0);
        r12 = r12.toString();
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "mVideoFilePath:";
        r12 = r12.append(r13);
        r0 = r65;
        r12 = r12.append(r0);
        r12 = r12.toString();
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);
        r32 = 0;
        r46 = 0;
        r26 = 0;
        r6 = "decoder";
        r25 = com.blink.academy.onetake.VideoTools.EGL10Helper.create(r6);
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r5 = com.blink.academy.onetake.VideoTools.VideoDecoderFactory.createVideoDecoder(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r29 = com.blink.academy.onetake.VideoTools.MediaUtils.getFormat(r65);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r9 = 10;
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "dataWidth:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r66;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ", dataHeight:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r67;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r29 == 0) goto L_0x03bb;
    L_0x0099:
        r6 = "width";
        r0 = r29;
        r6 = r0.containsKey(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r6 == 0) goto L_0x03bb;
    L_0x00a4:
        r6 = "height";
        r0 = r29;
        r6 = r0.containsKey(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r6 == 0) goto L_0x03bb;
    L_0x00ae:
        r6 = "width";
        r0 = r29;
        r31 = r0.getInteger(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "height";
        r0 = r29;
        r30 = r0.getInteger(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r68 != 0) goto L_0x00f5;
    L_0x00c1:
        r6 = "durationUs";
        r0 = r29;
        r22 = r0.getLong(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r22;
        r6 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 * r12;
        r12 = 1232348160; // 0x49742400 float:1000000.0 double:6.088608896E-315;
        r21 = r6 / r12;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 * r21;
        r0 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r68 = r0;
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "duration:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r68;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
    L_0x00f5:
        r0 = r65;
        r1 = r29;
        r57 = com.blink.academy.onetake.VideoTools.VideoDecoder.getVideoMatrix(r0, r1);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "formatWidth:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r31;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ", formatHeight:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r30;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r71 == 0) goto L_0x044f;
    L_0x0125:
        if (r70 == 0) goto L_0x044f;
    L_0x0127:
        r4 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
    L_0x0129:
        r6 = "wangchen";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "is crop = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r71;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "isMoreShort = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r70;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ",duration = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r68;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ",isSpecial = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r76;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r76 == 0) goto L_0x046e;
    L_0x016a:
        r4 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
        r6 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = r68;
        if (r0 <= r6) goto L_0x0174;
    L_0x0172:
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
    L_0x0174:
        r0 = r31;
        r1 = r30;
        if (r0 <= r1) goto L_0x0457;
    L_0x017a:
        r0 = r30;
        if (r0 <= r4) goto L_0x0453;
    L_0x017e:
        r0 = r31;
        r6 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 * r12;
        r0 = r30;
        r12 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 / r12;
        r12 = (float) r4;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 * r12;
        r7 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r8 = r4;
    L_0x018c:
        r6 = "wangchen";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "videoWidth:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.append(r7);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ", videoHeight:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.append(r8);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = (float) r7;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = 0;
        r12 = r57[r12];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 * r12;
        r12 = (float) r8;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 4;
        r13 = r57[r13];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12 * r13;
        r6 = r6 + r12;
        r6 = java.lang.Math.abs(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r42 = r0;
        r6 = (float) r7;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = 1;
        r12 = r57[r12];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 * r12;
        r12 = (float) r8;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 5;
        r13 = r57[r13];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12 * r13;
        r6 = r6 + r12;
        r6 = java.lang.Math.abs(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r41 = r0;
        r35 = 0;
        r36 = 0;
        r37 = r42;
        r34 = r41;
        if (r71 == 0) goto L_0x0206;
    L_0x01de:
        r0 = r37;
        r1 = r34;
        if (r0 <= r1) goto L_0x0499;
    L_0x01e4:
        r37 = r34;
    L_0x01e6:
        r0 = r42;
        r1 = r41;
        if (r0 <= r1) goto L_0x049d;
    L_0x01ec:
        r0 = r42;
        r6 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 * r72;
        r0 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r35 = r0;
        r36 = 0;
    L_0x01f6:
        r6 = r42 - r37;
        r0 = r35;
        r35 = java.lang.Math.min(r0, r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r41 - r34;
        r0 = r36;
        r36 = java.lang.Math.min(r0, r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
    L_0x0206:
        r14 = new android.graphics.Rect;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r35 + r37;
        r12 = r36 + r34;
        r0 = r35;
        r1 = r36;
        r14.<init>(r0, r1, r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "pixelsWidth:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r42;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = ", pixelsHeight:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r41;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "wangchen";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "gif height = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = r14.bottom;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r14.top;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r17 = r0;
        r13 = r13 - r17;
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "gif width = ";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = r14.right;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r14.left;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r17 = r0;
        r13 = r13 - r17;
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r74 == 0) goto L_0x04a9;
    L_0x026e:
        r56 = new java.io.File;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r56;
        r1 = r65;
        r0.<init>(r1);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r56.getName();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = ".mp4";
        r13 = "";
        r47 = r6.replace(r12, r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "%1$s%2$s.gif";
        r12 = 2;
        r12 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 0;
        r17 = com.blink.academy.onetake.Config.getGifSavePath();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12[r13] = r17;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 1;
        r12[r13] = r47;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r20 = java.lang.String.format(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
    L_0x0296:
        r10 = 0;
        r0 = r68;
        r6 = (float) r0;
        r12 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 / r12;
        r12 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
        r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r6 >= 0) goto L_0x04df;
    L_0x02a5:
        r12 = 4591870180066957722; // 0x3fb999999999999a float:-1.5881868E-23 double:0.1;
    L_0x02aa:
        r6 = r65;
        r10 = r5.getFrames(r6, r7, r8, r9, r10, r12);	 Catch:{ Exception -> 0x04e8, all -> 0x04f4 }
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r12.<init>();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r13 = "cachePath:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r20;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        if (r10 == 0) goto L_0x0621;
    L_0x02cc:
        r18 = 0;
        r6 = com.blink.academy.onetake.App.getContext();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r6 = r6.getResources();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r12 = 2130838365; // 0x7f02035d float:1.728171E38 double:1.052774033E-314;
        r18 = android.graphics.BitmapFactory.decodeResource(r6, r12);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r37;
        r1 = r34;
        if (r0 <= r1) goto L_0x0514;
    L_0x02e3:
        r39 = r37;
    L_0x02e5:
        r0 = r39;
        r6 = (float) r0;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r12 = addPicHeightProportion;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r27 = r6 * r12;
        r38 = r18.getHeight();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r58 = r18.getWidth();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r58;
        r6 = (float) r0;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 * r12;
        r0 = r38;
        r12 = (float) r0;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r6 = r6 / r12;
        r28 = r6 * r27;
        r0 = r28;
        r6 = (int) r0;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r27;
        r12 = (int) r0;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r13 = 0;
        r0 = r18;
        r18 = android.graphics.Bitmap.createScaledBitmap(r0, r6, r12, r13);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r25;
        r1 = r42;
        r2 = r41;
        r46 = r0.createPBuffer(r1, r2);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r0 = r25;
        r1 = r46;
        r0.makeCurrent(r1);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r6 = 36160; // 0x8d40 float:5.0671E-41 double:1.78654E-319;
        r12 = 0;
        android.opengl.GLES20.glBindFramebuffer(r6, r12);	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r15 = new jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r15.<init>();	 Catch:{ Exception -> 0x061c, all -> 0x0614 }
        r15.init();	 Catch:{ Exception -> 0x0611 }
        r0 = r42;
        r1 = r41;
        r15.onOutputSizeChanged(r0, r1);	 Catch:{ Exception -> 0x0611 }
        r33 = new com.blink.academy.onetake.VideoTools.GIFEncoder;	 Catch:{ Exception -> 0x0611 }
        r6 = 10;
        r12 = r10.size();	 Catch:{ Exception -> 0x0611 }
        r0 = r33;
        r1 = r37;
        r2 = r34;
        r0.<init>(r1, r2, r6, r12);	 Catch:{ Exception -> 0x0611 }
        r6 = r37 * r34;
        r6 = r6 * 4;
        r16 = java.nio.ByteBuffer.allocateDirect(r6);	 Catch:{ Exception -> 0x0611 }
        r11 = 0;
    L_0x034e:
        r6 = r10.size();	 Catch:{ Exception -> 0x0611 }
        if (r11 >= r6) goto L_0x0526;
    L_0x0354:
        r0 = r41;
        r13 = -r0;
        r17 = 0;
        r12 = r42;
        r10.getPixels(r11, r12, r13, r14, r15, r16, r17);	 Catch:{ Exception -> 0x0611 }
        r6 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r69);	 Catch:{ Exception -> 0x0611 }
        if (r6 != 0) goto L_0x0366;
    L_0x0364:
        if (r77 == 0) goto L_0x039f;
    L_0x0366:
        r6 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Exception -> 0x0611 }
        r0 = r37;
        r1 = r34;
        r19 = android.graphics.Bitmap.createBitmap(r0, r1, r6);	 Catch:{ Exception -> 0x0611 }
        r0 = r19;
        r1 = r16;
        r0.copyPixelsFromBuffer(r1);	 Catch:{ Exception -> 0x0611 }
        r6 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r69);	 Catch:{ Exception -> 0x0611 }
        if (r6 == 0) goto L_0x038b;
    L_0x037d:
        if (r78 == 0) goto L_0x0518;
    L_0x037f:
        r6 = com.blink.academy.onetake.App.getContext();	 Catch:{ Exception -> 0x0611 }
        r0 = r19;
        r1 = r69;
        r19 = getBitmapWithAddFilmSubtitles(r6, r0, r1);	 Catch:{ Exception -> 0x0611 }
    L_0x038b:
        if (r77 == 0) goto L_0x0395;
    L_0x038d:
        r0 = r19;
        r1 = r18;
        r19 = getBitmapWithAddPic(r0, r1);	 Catch:{ Exception -> 0x0611 }
    L_0x0395:
        r16.clear();	 Catch:{ Exception -> 0x0611 }
        r0 = r19;
        r1 = r16;
        r0.copyPixelsToBuffer(r1);	 Catch:{ Exception -> 0x0611 }
    L_0x039f:
        r0 = r33;
        r1 = r16;
        r0.setFrameData(r11, r1);	 Catch:{ Exception -> 0x0611 }
        if (r73 == 0) goto L_0x03b8;
    L_0x03a8:
        r6 = (float) r11;	 Catch:{ Exception -> 0x0611 }
        r12 = 1050253722; // 0x3e99999a float:0.3 double:5.188942835E-315;
        r6 = r6 * r12;
        r12 = r10.size();	 Catch:{ Exception -> 0x0611 }
        r12 = (float) r12;	 Catch:{ Exception -> 0x0611 }
        r6 = r6 / r12;
        r0 = r73;
        r0.onProgressing(r6);	 Catch:{ Exception -> 0x0611 }
    L_0x03b8:
        r11 = r11 + 1;
        goto L_0x034e;
    L_0x03bb:
        r40 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r40.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = com.blink.academy.onetake.support.utils.MovieFileUtil.getOutputMediaFile(r65);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r40;
        r0.setDataSource(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = 18;
        r0 = r40;
        r6 = r0.extractMetadata(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r31 = java.lang.Integer.parseInt(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = 19;
        r0 = r40;
        r6 = r0.extractMetadata(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r30 = java.lang.Integer.parseInt(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        if (r68 != 0) goto L_0x00f5;
    L_0x03e3:
        r6 = 9;
        r0 = r40;
        r43 = r0.extractMetadata(r6);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r68 = java.lang.Integer.parseInt(r43);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = "sDuration:";
        r12 = r12.append(r13);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r43;
        r12 = r12.append(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        goto L_0x00f5;
    L_0x040b:
        r24 = move-exception;
        r15 = r26;
        r10 = r32;
    L_0x0410:
        r24.printStackTrace();	 Catch:{ all -> 0x0619 }
        if (r10 == 0) goto L_0x041b;
    L_0x0415:
        r10.releaseFrames();
        r10.releasePool();
    L_0x041b:
        if (r15 == 0) goto L_0x0421;
    L_0x041d:
        r15.destroy();
        r15 = 0;
    L_0x0421:
        if (r46 == 0) goto L_0x042a;
    L_0x0423:
        r0 = r25;
        r1 = r46;
        r0.destroySurface(r1);
    L_0x042a:
        r25.release();
    L_0x042d:
        r6 = "mp4ToGif";
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "End:";
        r12 = r12.append(r13);
        r62 = java.lang.System.currentTimeMillis();
        r62 = r62 - r44;
        r0 = r62;
        r12 = r12.append(r0);
        r12 = r12.toString();
        com.blink.academy.onetake.support.debug.LogUtil.d(r6, r12);
        goto L_0x001b;
    L_0x044f:
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        goto L_0x0129;
    L_0x0453:
        r4 = r30;
        goto L_0x017e;
    L_0x0457:
        r0 = r31;
        if (r0 <= r4) goto L_0x046b;
    L_0x045b:
        r0 = r30;
        r6 = (float) r0;
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 * r12;
        r0 = r31;
        r12 = (float) r0;
        r6 = r6 / r12;
        r12 = (float) r4;
        r6 = r6 * r12;
        r8 = (int) r6;
        r7 = r4;
        goto L_0x018c;
    L_0x046b:
        r4 = r31;
        goto L_0x045b;
    L_0x046e:
        r6 = r31 * r30;
        r12 = 200000; // 0x30d40 float:2.8026E-40 double:9.8813E-319;
        if (r6 <= r12) goto L_0x0493;
    L_0x0475:
        r6 = 1212370944; // 0x48435000 float:200000.0 double:5.989908334E-315;
        r0 = r31;
        r12 = (float) r0;
        r6 = r6 / r12;
        r0 = r30;
        r12 = (float) r0;
        r6 = r6 / r12;
        r12 = (double) r6;
        r60 = java.lang.Math.sqrt(r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r31;
        r12 = (double) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12 * r60;
        r7 = (int) r12;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r30;
        r12 = (double) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = r12 * r60;
        r8 = (int) r12;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        goto L_0x018c;
    L_0x0493:
        r7 = r31;
        r8 = r30;
        goto L_0x018c;
    L_0x0499:
        r34 = r37;
        goto L_0x01e6;
    L_0x049d:
        r35 = 0;
        r0 = r41;
        r6 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = r6 * r72;
        r0 = (int) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r36 = r0;
        goto L_0x01f6;
    L_0x04a9:
        r6 = "%1$s/%2$s.gif";
        r12 = 2;
        r12 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 0;
        r17 = com.blink.academy.onetake.Config.getGifCachePath();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12[r13] = r17;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r13 = 1;
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r17.<init>();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r59 = java.lang.String.valueOf(r75);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r17;
        r1 = r59;
        r17 = r0.append(r1);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r59 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r17;
        r1 = r59;
        r17 = r0.append(r1);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12[r13] = r17;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r20 = java.lang.String.format(r6, r12);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        goto L_0x0296;
    L_0x04df:
        r0 = r68;
        r6 = (float) r0;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r12 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 / r12;
        r12 = (double) r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        goto L_0x02aa;
    L_0x04e8:
        r24 = move-exception;
        r24.printStackTrace();	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r6 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        r0 = r24;
        r6.<init>(r0);	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
        throw r6;	 Catch:{ Exception -> 0x040b, all -> 0x04f4 }
    L_0x04f4:
        r6 = move-exception;
        r15 = r26;
        r10 = r32;
    L_0x04f9:
        if (r10 == 0) goto L_0x0501;
    L_0x04fb:
        r10.releaseFrames();
        r10.releasePool();
    L_0x0501:
        if (r15 == 0) goto L_0x0507;
    L_0x0503:
        r15.destroy();
        r15 = 0;
    L_0x0507:
        if (r46 == 0) goto L_0x0510;
    L_0x0509:
        r0 = r25;
        r1 = r46;
        r0.destroySurface(r1);
    L_0x0510:
        r25.release();
        throw r6;
    L_0x0514:
        r39 = r34;
        goto L_0x02e5;
    L_0x0518:
        r6 = com.blink.academy.onetake.App.getContext();	 Catch:{ Exception -> 0x0611 }
        r0 = r19;
        r1 = r69;
        r19 = getBitmapWithAddText(r6, r0, r1);	 Catch:{ Exception -> 0x0611 }
        goto L_0x038b;
    L_0x0526:
        r18.recycle();	 Catch:{ Exception -> 0x0611 }
        r15.destroy();	 Catch:{ Exception -> 0x0611 }
        r15 = 0;
        r12 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x0611 }
        r62 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r48 = r12 / r62;
        r33.choosePalette();	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x0611 }
        r62 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r50 = r12 / r62;
        r33.palettize();	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x0611 }
        r62 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r52 = r12 / r62;
        r6 = 1;
        r0 = r33;
        r1 = r20;
        r0.writeGIF(r1, r6);	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x0611 }
        r62 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r54 = r12 / r62;
        r6 = "GIF";
        r12 = "choose:%d";
        r13 = 1;
        r13 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0611 }
        r17 = 0;
        r62 = r50 - r48;
        r59 = java.lang.Long.valueOf(r62);	 Catch:{ Exception -> 0x0611 }
        r13[r17] = r59;	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.String.format(r12, r13);	 Catch:{ Exception -> 0x0611 }
        android.util.Log.d(r6, r12);	 Catch:{ Exception -> 0x0611 }
        r6 = "GIF";
        r12 = "palettize:%d";
        r13 = 1;
        r13 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0611 }
        r17 = 0;
        r62 = r52 - r50;
        r59 = java.lang.Long.valueOf(r62);	 Catch:{ Exception -> 0x0611 }
        r13[r17] = r59;	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.String.format(r12, r13);	 Catch:{ Exception -> 0x0611 }
        android.util.Log.d(r6, r12);	 Catch:{ Exception -> 0x0611 }
        r6 = "GIF";
        r12 = "write:%d";
        r13 = 1;
        r13 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0611 }
        r17 = 0;
        r62 = r54 - r52;
        r59 = java.lang.Long.valueOf(r62);	 Catch:{ Exception -> 0x0611 }
        r13[r17] = r59;	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.String.format(r12, r13);	 Catch:{ Exception -> 0x0611 }
        android.util.Log.d(r6, r12);	 Catch:{ Exception -> 0x0611 }
        r6 = "GIF";
        r12 = "total:%d";
        r13 = 1;
        r13 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0611 }
        r17 = 0;
        r62 = r54 - r48;
        r59 = java.lang.Long.valueOf(r62);	 Catch:{ Exception -> 0x0611 }
        r13[r17] = r59;	 Catch:{ Exception -> 0x0611 }
        r12 = java.lang.String.format(r12, r13);	 Catch:{ Exception -> 0x0611 }
        android.util.Log.d(r6, r12);	 Catch:{ Exception -> 0x0611 }
        if (r73 == 0) goto L_0x05c9;
    L_0x05c2:
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r73;
        r0.onProgressing(r6);	 Catch:{ Exception -> 0x0611 }
    L_0x05c9:
        if (r74 == 0) goto L_0x05dd;
    L_0x05cb:
        r6 = com.blink.academy.onetake.App.getContext();	 Catch:{ Exception -> 0x0611 }
        r12 = 1;
        r12 = new java.lang.String[r12];	 Catch:{ Exception -> 0x0611 }
        r13 = 0;
        r12[r13] = r20;	 Catch:{ Exception -> 0x0611 }
        r13 = 0;
        r17 = 0;
        r0 = r17;
        android.media.MediaScannerConnection.scanFile(r6, r12, r13, r0);	 Catch:{ Exception -> 0x0611 }
    L_0x05dd:
        if (r73 == 0) goto L_0x05eb;
    L_0x05df:
        r6 = -1;
        r0 = r75;
        if (r6 != r0) goto L_0x0607;
    L_0x05e4:
        r0 = r73;
        r1 = r20;
        r0.onFinish(r1);	 Catch:{ Exception -> 0x0611 }
    L_0x05eb:
        if (r10 == 0) goto L_0x05f3;
    L_0x05ed:
        r10.releaseFrames();
        r10.releasePool();
    L_0x05f3:
        if (r15 == 0) goto L_0x05f9;
    L_0x05f5:
        r15.destroy();
        r15 = 0;
    L_0x05f9:
        if (r46 == 0) goto L_0x0602;
    L_0x05fb:
        r0 = r25;
        r1 = r46;
        r0.destroySurface(r1);
    L_0x0602:
        r25.release();
        goto L_0x042d;
    L_0x0607:
        r0 = r73;
        r1 = r20;
        r2 = r75;
        r0.onFinish(r1, r2);	 Catch:{ Exception -> 0x0611 }
        goto L_0x05eb;
    L_0x0611:
        r24 = move-exception;
        goto L_0x0410;
    L_0x0614:
        r6 = move-exception;
        r15 = r26;
        goto L_0x04f9;
    L_0x0619:
        r6 = move-exception;
        goto L_0x04f9;
    L_0x061c:
        r24 = move-exception;
        r15 = r26;
        goto L_0x0410;
    L_0x0621:
        r15 = r26;
        goto L_0x05c9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.GIFUtil.mp4ToGif(android.app.Activity, java.lang.String, int, int, int, java.lang.String, boolean, boolean, float, com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog$OnHandlerMp4ToGifListener, boolean, int, boolean, boolean, boolean):void");
    }

    public static void saveJPGFile(String savePath, Bitmap originalBitmap) {
        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            originalBitmap.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
        }
    }

    private static TextPaint getFilmSubtitlesTextPaint(Context context, float fontSize) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TypedValue.applyDimension(1, fontSize, context.getResources().getDisplayMetrics()));
        textPaint.setColor(context.getResources().getColor(R.color.colorLighterGray));
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setShadowLayer(1.0f, 1.0f, 1.0f, context.getResources().getColor(R.color.colorHalfBlack));
        Typeface typeface = FontsUtil.setRobotoBoldTypeFace();
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        return textPaint;
    }

    private static Bitmap getBitmapWithAddFilmSubtitles(Context context, Bitmap bitmap, String addText) {
        if (addText == null || addText.length() == 0) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        String secondLine;
        int shortLenght;
        String[] split = addText.split("\n");
        String firstLine = split[0];
        if (split.length > 1) {
            secondLine = split[1];
        } else {
            secondLine = "";
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            shortLenght = height;
        } else {
            shortLenght = width;
        }
        float scale = (((float) shortLenght) * 1.0f) / ((float) DensityUtil.dip2px(200.0f));
        double fontSize = ((double) (17.0f * scale)) * 0.5d;
        TextPaint firstLineTextPaint = getFilmSubtitlesTextPaint(context, (float) fontSize);
        TextPaint secondLineTextPaint = getFilmSubtitlesTextPaint(context, (float) (fontSize * 0.6d));
        FontMetrics fontMetrics;
        if (TextUtil.isValidate(secondLine)) {
            fontMetrics = secondLineTextPaint.getFontMetrics();
            Canvas canvas = new Canvas(bitmap);
            float newY = ((float) (height - ((int) (((float) DensityUtil.dip2px(15.0f)) * scale)))) - fontMetrics.bottom;
            canvas.drawText(secondLine, (float) (width / 2), newY, secondLineTextPaint);
            canvas.drawText(firstLine, (float) (width / 2), (newY + fontMetrics.top) - firstLineTextPaint.getFontMetrics().bottom, firstLineTextPaint);
            return bitmap;
        }
        fontMetrics = firstLineTextPaint.getFontMetrics();
        new Canvas(bitmap).drawText(firstLine, (float) (width / 2), ((float) (height - ((int) (((float) DensityUtil.dip2px(15.0f)) * scale)))) - fontMetrics.bottom, firstLineTextPaint);
        return bitmap;
    }

    public static Bitmap getBitmapWithAddText(Context context, Bitmap bitmap, String addText) {
        if (addText == null || addText.length() == 0) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        int shortLenght;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            shortLenght = height;
        } else {
            shortLenght = width;
        }
        float scale = (((float) shortLenght) * 1.0f) / ((float) DensityUtil.dip2px(200.0f));
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TypedValue.applyDimension(1, 19.0f * scale, context.getResources().getDisplayMetrics()));
        textPaint.setColor(context.getResources().getColor(R.color.colorWhite));
        textPaint.setTextAlign(Align.CENTER);
        TextPaint strokePaint = new TextPaint(textPaint);
        strokePaint.setStrokeWidth(((float) DensityUtil.dip2px(4.0f)) * scale);
        strokePaint.setStyle(Style.STROKE);
        strokePaint.setStrokeJoin(Join.ROUND);
        strokePaint.setColor(context.getResources().getColor(R.color.colorBlack));
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        Canvas canvas = new Canvas(bitmap);
        float newY = ((float) (height - ((int) (((float) DensityUtil.dip2px(10.0f)) * scale)))) - fontMetrics.bottom;
        canvas.drawText(addText, (float) (width / 2), newY, strokePaint);
        canvas.drawText(addText, (float) (width / 2), newY, textPaint);
        return bitmap;
    }

    public static Bitmap getBitmapWithAddPic(Bitmap bitmap, Bitmap addPic) {
        if (addPic == null || addPic.isRecycled()) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        bitmap = bitmap.copy(Config.ARGB_8888, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int longLength;
        if (width > height) {
            longLength = width;
        } else {
            longLength = height;
        }
        float distance = ((float) addPic.getHeight()) * distanceProportion;
        float startX = ((float) (width - addPic.getWidth())) - distance;
        float startY = distance;
        Paint paint = new Paint();
        paint.setAlpha(154);
        new Canvas(bitmap).drawBitmap(addPic, startX, startY, paint);
        return bitmap;
    }

    public static Bitmap getBitmapWithAddPicAndVideoBtn(Bitmap bitmap, Bitmap addPic, Bitmap btBitmap) {
        if (addPic == null || addPic.isRecycled()) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int longLength;
        if (width > height) {
            longLength = width;
        } else {
            longLength = height;
        }
        float distance = ((float) addPic.getHeight()) * distanceProportion;
        float startX = ((float) (width - addPic.getWidth())) - distance;
        float startY = distance;
        Paint paint = new Paint();
        paint.setAlpha(154);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(addPic, startX, startY, paint);
        canvas.drawBitmap(btBitmap, (float) ((width - btBitmap.getWidth()) / 2), (float) ((height - btBitmap.getHeight()) / 2), paint);
        return bitmap;
    }
}