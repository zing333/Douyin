package cn.nineton.onetake.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.text.TextPaint;

import cn.nineton.onetake.App;
import cn.nineton.onetake.widget.ShareBitmapEntity;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class WaterMarkBitmapUtil {
    private static final int TAIL_WATERMARK_WIDTH = 260;
    public static final int WATERMARK_CENTER = 0;
    private static final float WATERMARK_DISTANCE_RATIO = 0.035f;
    private static final float WATERMARK_HEIGHT_RATIO = 0.05f;
    public static final int WATERMARK_LEFT_BOTTOM = 2;
    public static final int WATERMARK_LEFT_TOP = 1;
    public static final int WATERMARK_RIGHT_BOTTOM = 4;
    public static final int WATERMARK_RIGHT_TOP = 3;

    public static ShareBitmapEntity getVideoShareWaterMarkBitmap(Context context, int videoWidth, int videoHeight, int posState) {
        int shortSide;
        int bmId;
        Bitmap srcBitmap;
        Float[] floats = new Float[5];
        if (videoWidth > videoHeight) {
            shortSide = videoHeight;
        } else {
            shortSide = videoWidth;
        }
        float reqHeight = ((float) shortSide) * WATERMARK_HEIGHT_RATIO;
        int border = (int) (((float) shortSide) * WATERMARK_DISTANCE_RATIO);
        if (LocaleUtil.isChinese()) {
            bmId = 2130838364;
            srcBitmap = BitmapFactory.decodeResource(context.getResources(), 2130838364);
        } else {
            bmId = 2130838365;
            srcBitmap = BitmapFactory.decodeResource(context.getResources(), 2130838365);
        }
        int waterMarkWidth = (int) (((((float) srcBitmap.getWidth()) * 1.0f) / ((float) srcBitmap.getHeight())) * reqHeight);
        int waterMarkHeight = (int) reqHeight;
        Bitmap waterMark = getScaledWaterMarkBitmap(context, bmId, (float) waterMarkWidth, (float) waterMarkHeight);
        if (!srcBitmap.isRecycled()) {
            srcBitmap.recycle();
        }
        switch (posState) {
            case 1:
                floats[0] = Float.valueOf(((float) border) / ((float) videoWidth));
                floats[1] = Float.valueOf(((float) ((videoHeight - border) - waterMarkHeight)) / ((float) videoHeight));
                break;
            case 2:
                floats[0] = Float.valueOf(((float) ((videoWidth - border) - waterMarkWidth)) / ((float) videoWidth));
                floats[1] = Float.valueOf(((float) ((videoHeight - border) - waterMarkHeight)) / ((float) videoHeight));
                break;
            case 3:
                floats[0] = Float.valueOf(((float) border) / ((float) videoWidth));
                floats[1] = Float.valueOf(((float) ((videoHeight - border) - waterMarkHeight)) / ((float) videoHeight));
                break;
            case 4:
                floats[0] = Float.valueOf(((float) ((videoWidth - border) - waterMarkWidth)) / ((float) videoWidth));
                floats[1] = Float.valueOf(((float) border) / ((float) videoHeight));
                break;
        }
        floats[2] = Float.valueOf(((float) waterMarkWidth) / ((float) videoWidth));
        floats[3] = Float.valueOf(((float) waterMarkHeight) / ((float) videoHeight));
        floats[4] = Float.valueOf(0.7f);
        ShareBitmapEntity entity = new ShareBitmapEntity();
        entity.setBitmap(waterMark);
        entity.setSize(floats);
        return entity;
    }

    private static TextPaint getPaint(float textSize, boolean light, boolean bold) {
        TextPaint paint = new TextPaint();
        if (light) {
            paint.setTypeface(App.getFoundersGroteskXCondensedappLightTypeFace());
        } else {
            paint.setTypeface(App.getFoundersGroteskXCondensedappMediumTypeFace());
        }
        paint.setTextSize(textSize);
        paint.setFakeBoldText(bold);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setTextAlign(Align.CENTER);
        paint.setAntiAlias(true);
        return paint;
    }

    public static Bitmap getTailWaterMarkBitmap(Context context, int videoWidth, int videoHeight, int posState) {
        return getTailWaterMarkBitmap(context, videoWidth, videoHeight, "", "", "", posState);
    }

    public static android.graphics.Bitmap getTailWaterMarkBitmap(android.content.Context r36, int r37, int r38, java.lang.String r39, java.lang.String r40, java.lang.String r41, int r42) {
        /*
        r0 = r38;
        r1 = r37;
        if (r0 <= r1) goto L_0x01c9;
    L_0x0006:
        r19 = r37;
    L_0x0008:
        r0 = r19;
        r0 = (float) r0;
        r32 = r0;
        r33 = 1149698048; // 0x44870000 float:1080.0 double:5.680263086E-315;
        r16 = r32 / r33;
        r32 = android.graphics.Bitmap.Config.ARGB_8888;
        r0 = r37;
        r1 = r38;
        r2 = r32;
        r7 = android.graphics.Bitmap.createBitmap(r0, r1, r2);
        r10 = new android.graphics.Canvas;
        r10.<init>(r7);
        r32 = new android.graphics.PaintFlagsDrawFilter;
        r33 = 0;
        r34 = 3;
        r32.<init>(r33, r34);
        r0 = r32;
        r10.setDrawFilter(r0);
        r32 = "#000000";
        r32 = android.graphics.Color.parseColor(r32);
        r0 = r32;
        r10.drawColor(r0);
        r32 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r32 = r32 * r16;
        r33 = 1;
        r34 = 1;
        r6 = getPaint(r32, r33, r34);
        r32 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r32 = r32 * r16;
        r33 = 0;
        r34 = 0;
        r22 = getPaint(r32, r33, r34);
        r5 = r6.getFontMetrics();
        r0 = r5.bottom;
        r32 = r0;
        r0 = r5.top;
        r33 = r0;
        r4 = r32 - r33;
        r21 = r22.getFontMetrics();
        r0 = r21;
        r0 = r0.bottom;
        r32 = r0;
        r0 = r21;
        r0 = r0.top;
        r33 = r0;
        r20 = r32 - r33;
        r32 = r36.getResources();
        r33 = 2130838365; // 0x7f02035d float:1.728171E38 double:1.052774033E-314;
        r23 = android.graphics.BitmapFactory.decodeResource(r32, r33);
        r8 = new android.graphics.Paint;
        r32 = 2;
        r0 = r32;
        r8.<init>(r0);
        r32 = 1;
        r0 = r32;
        r8.setAntiAlias(r0);
        r32 = 1;
        r0 = r32;
        r8.setDither(r0);
        r32 = 1;
        r0 = r32;
        r8.setFilterBitmap(r0);
        r32 = 1132593152; // 0x43820000 float:260.0 double:5.59575367E-315;
        r32 = r32 * r16;
        r0 = r32;
        r0 = (int) r0;
        r31 = r0;
        r32 = 1132593152; // 0x43820000 float:260.0 double:5.59575367E-315;
        r32 = r32 * r16;
        r33 = r23.getHeight();
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r32 = r32 * r33;
        r33 = r23.getWidth();
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r32 = r32 / r33;
        r0 = r32;
        r0 = (int) r0;
        r30 = r0;
        r32 = 2130838365; // 0x7f02035d float:1.728171E38 double:1.052774033E-314;
        r0 = r31;
        r0 = (float) r0;
        r33 = r0;
        r0 = r30;
        r0 = (float) r0;
        r34 = r0;
        r0 = r36;
        r1 = r32;
        r2 = r33;
        r3 = r34;
        r29 = getScaledWaterMarkBitmap(r0, r1, r2, r3);
        r32 = r23.isRecycled();
        if (r32 != 0) goto L_0x00e6;
    L_0x00e3:
        r23.recycle();
    L_0x00e6:
        r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r39);
        if (r32 != 0) goto L_0x0207;
    L_0x00ec:
        r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r40);
        if (r32 != 0) goto L_0x01cd;
    L_0x00f2:
        r11 = 0;
        r24 = 0;
    L_0x00f5:
        r17 = new android.graphics.Rect;
        r17.<init>();
        r0 = r19;
        r0 = (float) r0;
        r32 = r0;
        r33 = 1024416809; // 0x3d0f5c29 float:0.035 double:5.061291523E-315;
        r32 = r32 * r33;
        r0 = r32;
        r9 = (int) r0;
        r25 = new android.graphics.Rect;
        r25.<init>();
        switch(r42) {
            case 0: goto L_0x026c;
            case 1: goto L_0x030b;
            case 2: goto L_0x02d3;
            case 3: goto L_0x037b;
            case 4: goto L_0x033d;
            default: goto L_0x010f;
        };
    L_0x010f:
        r32 = r7.getHeight();
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r32 = r32 - r11;
        r33 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r32 = r32 / r33;
        r0 = r32;
        r0 = (int) r0;
        r27 = r0;
        if (r42 != 0) goto L_0x01ba;
    L_0x0125:
        r32 = 3;
        r0 = r24;
        r1 = r32;
        if (r0 != r1) goto L_0x03b3;
    L_0x012d:
        r32 = r7.getWidth();
        r32 = r32 - r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.left = r0;
        r32 = r7.getHeight();
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r33 = 1045220557; // 0x3e4ccccd float:0.2 double:5.164075695E-315;
        r34 = r7.getHeight();
        r0 = r34;
        r0 = (float) r0;
        r34 = r0;
        r33 = r33 * r34;
        r32 = r32 - r33;
        r33 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r30;
        r0 = (float) r0;
        r34 = r0;
        r33 = r33 * r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r33 = r33 / r34;
        r32 = r32 - r33;
        r0 = r32;
        r0 = (int) r0;
        r32 = r0;
        r0 = r32;
        r1 = r17;
        r1.top = r0;
        r32 = r7.getWidth();
        r32 = r32 + r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r7.getHeight();
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r33 = 1045220557; // 0x3e4ccccd float:0.2 double:5.164075695E-315;
        r34 = r7.getHeight();
        r0 = r34;
        r0 = (float) r0;
        r34 = r0;
        r33 = r33 * r34;
        r32 = r32 - r33;
        r33 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r30;
        r0 = (float) r0;
        r34 = r0;
        r33 = r33 * r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r33 = r33 / r34;
        r32 = r32 + r33;
        r0 = r32;
        r0 = (int) r0;
        r32 = r0;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
    L_0x01ba:
        r12 = "Courtesy of Original Artist";
        r28 = "DIRECTED BY ";
        switch(r24) {
            case 1: goto L_0x03f6;
            case 2: goto L_0x0455;
            case 3: goto L_0x0525;
            case 4: goto L_0x0597;
            case 5: goto L_0x0656;
            default: goto L_0x01c1;
        };
    L_0x01c1:
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
    L_0x01c8:
        return r7;
    L_0x01c9:
        r19 = r38;
        goto L_0x0008;
    L_0x01cd:
        r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r41);
        if (r32 != 0) goto L_0x01e5;
    L_0x01d3:
        r32 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r32 = r32 * r16;
        r32 = r32 + r20;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r11 = r32 + r33;
        r24 = 1;
        goto L_0x00f5;
    L_0x01e5:
        r32 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r32 = r32 * r16;
        r32 = r32 + r20;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r33 = r33 * r16;
        r11 = r32 + r33;
        r24 = 2;
        goto L_0x00f5;
    L_0x0207:
        r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r40);
        if (r32 != 0) goto L_0x0212;
    L_0x020d:
        r11 = r4;
        r24 = 3;
        goto L_0x00f5;
    L_0x0212:
        r32 = com.blink.academy.onetake.support.utils.TextUtil.isValidate(r41);
        if (r32 != 0) goto L_0x023a;
    L_0x0218:
        r32 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r32 = r32 * r16;
        r32 = r32 + r20;
        r32 = r32 + r4;
        r33 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r20;
        r33 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r11 = r32 + r33;
        r24 = 4;
        goto L_0x00f5;
    L_0x023a:
        r32 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r32 = r32 * r16;
        r32 = r32 + r20;
        r32 = r32 + r4;
        r33 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r20;
        r33 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r33 = r33 * r16;
        r32 = r32 + r33;
        r32 = r32 + r4;
        r33 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r33 = r33 * r16;
        r11 = r32 + r33;
        r24 = 5;
        goto L_0x00f5;
    L_0x026c:
        r32 = 0;
        r32 = (r11 > r32 ? 1 : (r11 == r32 ? 0 : -1));
        if (r32 != 0) goto L_0x02be;
    L_0x0272:
        r32 = r7.getWidth();
        r32 = r32 - r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.left = r0;
        r32 = r7.getHeight();
        r32 = r32 - r30;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.top = r0;
        r32 = r7.getWidth();
        r32 = r32 + r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r7.getHeight();
        r32 = r32 + r30;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
        goto L_0x01c8;
    L_0x02be:
        r32 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1));
        if (r32 != 0) goto L_0x02ca;
    L_0x02c2:
        r32 = 3;
        r0 = r24;
        r1 = r32;
        if (r0 == r1) goto L_0x010f;
    L_0x02ca:
        r0 = r30;
        r0 = (float) r0;
        r32 = r0;
        r11 = r11 + r32;
        goto L_0x010f;
    L_0x02d3:
        r0 = r17;
        r0.left = r9;
        r32 = r38 - r9;
        r32 = r32 - r30;
        r0 = r32;
        r1 = r17;
        r1.top = r0;
        r32 = r9 + r31;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r38 - r9;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        r32 = 0;
        r32 = (r11 > r32 ? 1 : (r11 == r32 ? 0 : -1));
        if (r32 != 0) goto L_0x010f;
    L_0x0302:
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
        goto L_0x01c8;
    L_0x030b:
        r0 = r17;
        r0.left = r9;
        r0 = r17;
        r0.top = r9;
        r32 = r9 + r31;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r9 + r30;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        r32 = 0;
        r32 = (r11 > r32 ? 1 : (r11 == r32 ? 0 : -1));
        if (r32 != 0) goto L_0x010f;
    L_0x0334:
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
        goto L_0x01c8;
    L_0x033d:
        r32 = r37 - r9;
        r32 = r32 - r31;
        r0 = r32;
        r1 = r17;
        r1.left = r0;
        r32 = r37 - r9;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r38 - r9;
        r32 = r32 - r30;
        r0 = r32;
        r1 = r17;
        r1.top = r0;
        r32 = r38 - r9;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        r32 = 0;
        r32 = (r11 > r32 ? 1 : (r11 == r32 ? 0 : -1));
        if (r32 != 0) goto L_0x010f;
    L_0x0372:
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
        goto L_0x01c8;
    L_0x037b:
        r32 = r37 - r9;
        r32 = r32 - r31;
        r0 = r32;
        r1 = r17;
        r1.left = r0;
        r32 = r37 - r9;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r0 = r17;
        r0.top = r9;
        r32 = r9 + r30;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        r32 = 0;
        r32 = (r11 > r32 ? 1 : (r11 == r32 ? 0 : -1));
        if (r32 != 0) goto L_0x010f;
    L_0x03aa:
        r32 = 0;
        r0 = r32;
        r10.setBitmap(r0);
        goto L_0x01c8;
    L_0x03b3:
        r32 = r7.getWidth();
        r32 = r32 - r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.left = r0;
        r32 = r7.getHeight();
        r32 = r32 - r27;
        r32 = r32 - r30;
        r0 = r32;
        r1 = r17;
        r1.top = r0;
        r32 = r7.getWidth();
        r32 = r32 + r31;
        r32 = r32 / 2;
        r0 = r32;
        r1 = r17;
        r1.right = r0;
        r32 = r7.getHeight();
        r32 = r32 - r27;
        r0 = r32;
        r1 = r17;
        r1.bottom = r0;
        r32 = 0;
        r0 = r29;
        r1 = r32;
        r2 = r17;
        r10.drawBitmap(r0, r1, r2, r8);
        goto L_0x01ba;
    L_0x03f6:
        r32 = "MUSIC";
        r33 = r37 / 2;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r0 = r27;
        r0 = (float) r0;
        r34 = r0;
        r35 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r35 = r20 / r35;
        r34 = r34 + r35;
        r0 = r32;
        r1 = r33;
        r2 = r34;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "\"";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r40;
        r32 = r0.append(r1);
        r33 = "\"";
        r32 = r32.append(r33);
        r14 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r14, r0, r1, r6);
        goto L_0x01c1;
    L_0x0455:
        r32 = "MUSIC";
        r33 = r37 / 2;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r0 = r27;
        r0 = (float) r0;
        r34 = r0;
        r35 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r35 = r20 / r35;
        r34 = r34 + r35;
        r0 = r32;
        r1 = r33;
        r2 = r34;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "\"";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r40;
        r32 = r0.append(r1);
        r33 = "\"";
        r32 = r32.append(r33);
        r14 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r14, r0, r1, r6);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "Performed by ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r41;
        r32 = r0.append(r1);
        r13 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r33 = r33 + r4;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r13, r0, r1, r6);
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r33 = r33 + r4;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r12, r0, r1, r6);
        goto L_0x01c1;
    L_0x0525:
        r32 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r32 = r32 * r16;
        r0 = r22;
        r1 = r32;
        r0.setTextSize(r1);
        r0 = r28;
        r1 = r22;
        r2 = r25;
        r26 = getTextWidth(r0, r1, r2);
        r0 = r39;
        r1 = r25;
        r15 = getTextWidth(r0, r6, r1);
        r32 = r37 - r26;
        r32 = r32 - r15;
        r18 = r32 / 2;
        r32 = r26 / 2;
        r32 = r32 + r18;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r33 = r38 / 2;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r0 = r28;
        r1 = r32;
        r2 = r33;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "  ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r39;
        r32 = r0.append(r1);
        r32 = r32.toString();
        r33 = r18 + r26;
        r34 = r15 / 2;
        r33 = r33 + r34;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r34 = r38 / 2;
        r0 = r34;
        r0 = (float) r0;
        r34 = r0;
        r0 = r32;
        r1 = r33;
        r2 = r34;
        r10.drawText(r0, r1, r2, r6);
        goto L_0x01c1;
    L_0x0597:
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r20 / r34;
        r33 = r33 + r34;
        r0 = r28;
        r1 = r32;
        r2 = r33;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r39;
        r1 = r32;
        r2 = r33;
        r10.drawText(r0, r1, r2, r6);
        r32 = "MUSIC";
        r33 = r37 / 2;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r0 = r27;
        r0 = (float) r0;
        r34 = r0;
        r34 = r34 + r20;
        r35 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r35 = r35 * r16;
        r34 = r34 + r35;
        r34 = r34 + r4;
        r35 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r35 = r35 * r16;
        r34 = r34 + r35;
        r35 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r35 = r20 / r35;
        r34 = r34 + r35;
        r0 = r32;
        r1 = r33;
        r2 = r34;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "\"";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r40;
        r32 = r0.append(r1);
        r33 = "\"";
        r32 = r32.append(r33);
        r14 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r14, r0, r1, r6);
        goto L_0x01c1;
    L_0x0656:
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r20 / r34;
        r33 = r33 + r34;
        r0 = r28;
        r1 = r32;
        r2 = r33;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r39;
        r1 = r32;
        r2 = r33;
        r10.drawText(r0, r1, r2, r6);
        r32 = "MUSIC";
        r33 = r37 / 2;
        r0 = r33;
        r0 = (float) r0;
        r33 = r0;
        r0 = r27;
        r0 = (float) r0;
        r34 = r0;
        r34 = r34 + r20;
        r35 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r35 = r35 * r16;
        r34 = r34 + r35;
        r34 = r34 + r4;
        r35 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r35 = r35 * r16;
        r34 = r34 + r35;
        r35 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r35 = r20 / r35;
        r34 = r34 + r35;
        r0 = r32;
        r1 = r33;
        r2 = r34;
        r3 = r22;
        r10.drawText(r0, r1, r2, r3);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "\"";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r40;
        r32 = r0.append(r1);
        r33 = "\"";
        r32 = r32.append(r33);
        r14 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r14, r0, r1, r6);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "Performed by ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r41;
        r32 = r0.append(r1);
        r13 = r32.toString();
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r13, r0, r1, r6);
        r32 = r37 / 2;
        r0 = r32;
        r0 = (float) r0;
        r32 = r0;
        r0 = r27;
        r0 = (float) r0;
        r33 = r0;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r20;
        r34 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r33 = r33 + r4;
        r34 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r34 = r34 * r16;
        r33 = r33 + r34;
        r34 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r34 = r4 / r34;
        r33 = r33 + r34;
        r0 = r32;
        r1 = r33;
        r10.drawText(r12, r0, r1, r6);
        goto L_0x01c1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil.getTailWaterMarkBitmap(android.content.Context, int, int, java.lang.String, java.lang.String, java.lang.String, int):android.graphics.Bitmap");
    }

    public static int getTextWidth(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        return rect.width();
    }

    public static int getTextHeight(String content, Paint paint, Rect rect) {
        paint.getTextBounds(content, 0, content.length(), rect);
        return rect.height();
    }

    public static Bitmap getScaledWaterMarkBitmap(Context context, int id, float pixelW, float pixelH) {
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > pixelW) {
            be = Math.round(((float) newOpts.outWidth) / pixelW);
        } else if (w < h && ((float) h) > pixelH) {
            be = Math.round(((float) newOpts.outHeight) / pixelH);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        return BitmapFactory.decodeResource(context.getResources(), id, newOpts);
    }

    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, paint);
        return outBitmap;
    }
}