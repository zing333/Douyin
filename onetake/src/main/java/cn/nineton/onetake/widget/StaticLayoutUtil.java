package cn.nineton.onetake.widget;

import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;

import cn.nineton.onetake.App;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.FontsUtil;

public class StaticLayoutUtil {
    public static final Alignment DefaultAlignment = Alignment.ALIGN_NORMAL;
    public static final boolean DefaultIncludepad = true;
    public static final float DefaultSpacingadd = 0.0f;
    public static final float DefaultSpacingmult = 1.0f;
    public static final int DefaultTextSize = 14;
    public static final int LittleTextSize = 12;
    public static final float NormalTextSize = 15.5f;
    public static final float Spacingadd_2 = ((float) DensityUtil.dip2px(2.0f));
    public static final float Spacingadd_3 = ((float) DensityUtil.dip2px(3.0f));
    public static final String TAG = StaticLayoutUtil.class.getSimpleName();

    public static TextPaint getDefaultTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(14.0f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.colo.color66));
        return textPaint;
    }

    public static TextPaint getLittleTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(12.0f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.color66));
        return textPaint;
    }

    public static TextPaint getDefaultColorBlackTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(14.0f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.colorBlack));
        return textPaint;
    }

    public static TextPaint getNormalTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(15.5f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.color66));
        return textPaint;
    }

    public static TextPaint getColorBlackTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(15.5f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.colorBlack));
        return textPaint;
    }

    public static TextPaint getColor92TextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(15.5f));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.color92));
        return textPaint;
    }

    public static int getDefaultTextSize() {
        return 14;
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getColor92StaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getColor92TextPaint();	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getColor92StaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getColorBlackStaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getColorBlackTextPaint();	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getColorBlackStaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    public static android.text.StaticLayout getSpace2StaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r2 = getDefaultColorBlackTextPaint();	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = Spacingadd_2;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
    L_0x0018:
        if (r0 == 0) goto L_0x001d;
    L_0x001a:
        r0.draw(r8);
    L_0x001d:
        return r0;
    L_0x001e:
        r1 = move-exception;
    L_0x001f:
        r0 = r9;
        goto L_0x0018;
    L_0x0021:
        r1 = move-exception;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getSpace2StaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    public static android.text.StaticLayout getSpace3StaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r2 = getDefaultColorBlackTextPaint();	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = Spacingadd_3;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
    L_0x0018:
        if (r0 == 0) goto L_0x001d;
    L_0x001a:
        r0.draw(r8);
    L_0x001d:
        return r0;
    L_0x001e:
        r1 = move-exception;
    L_0x001f:
        r0 = r9;
        goto L_0x0018;
    L_0x0021:
        r1 = move-exception;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getSpace3StaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getDefaultStaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getDefaultTextPaint();	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getDefaultStaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0023  */
    public static android.text.StaticLayout getDefault2StaticLayout(android.text.Spannable r10, java.lang.String r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r2 = getDefaultTextPaint();	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r1 = getDefaultTextPaint();	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r1 = r1.measureText(r11);	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r1 = (int) r1;	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r3 = r1 + 10;
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x0027, IllegalArgumentException -> 0x002a }
    L_0x0021:
        if (r0 == 0) goto L_0x0026;
    L_0x0023:
        r0.draw(r8);
    L_0x0026:
        return r0;
    L_0x0027:
        r1 = move-exception;
    L_0x0028:
        r0 = r9;
        goto L_0x0021;
    L_0x002a:
        r1 = move-exception;
        goto L_0x0028;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getDefault2StaticLayout(android.text.Spannable, java.lang.String):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    public static android.text.StaticLayout getBadgeDialogStaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r2 = getLittleTextPaint();	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = Spacingadd_2;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
    L_0x0018:
        if (r0 == 0) goto L_0x001d;
    L_0x001a:
        r0.draw(r8);
    L_0x001d:
        return r0;
    L_0x001e:
        r1 = move-exception;
    L_0x001f:
        r0 = r9;
        goto L_0x0018;
    L_0x0021:
        r1 = move-exception;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getBadgeDialogStaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getNormalStaticLayout(android.text.Spannable r10, int r11) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getNormalTextPaint();	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getNormalStaticLayout(android.text.Spannable, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getDefaultStaticLayout(android.text.Spannable r10, int r11, int r12) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getTextPaint(r12);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getDefaultStaticLayout(android.text.Spannable, int, int):android.text.StaticLayout");
    }

    public static TextPaint getTextPaint(int textSize) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px((float) textSize));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(R.color.color66));
        return textPaint;
    }

    public static TextPaint getTextPaint(int colorId, int textSize) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px((float) textSize));
        textPaint.setTypeface(FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(colorId);
        return textPaint;
    }

    public static TextPaint getTextPaint(int colorId, boolean isMedium) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.density = App.getResource().getDisplayMetrics().density;
        textPaint.setTextSize((float) DensityUtil.dip2px(14.0f));
        textPaint.setTypeface(isMedium ? FontsUtil.setAMediumTypeFace() : FontsUtil.setDefaultTypeFace());
        textPaint.setFlags((textPaint.getFlags() | 128) | 1);
        textPaint.setColor(App.getResource().getColor(colorId));
        return textPaint;
    }

    public static StaticLayout getStaticLayout(Spannable spannable, int width, int colorId) {
        return getStaticLayout(spannable, 14, width, App.getResource().getColor(colorId));
    }

    public static StaticLayout getStaticLayoutForSpace2(Spannable spannable, int width, int colorId) {
        return getStaticLayoutForSpace2(spannable, 14, width, App.getResource().getColor(colorId));
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x001a  */
    public static android.text.StaticLayout getStaticLayoutForSpace2(android.text.Spannable r10, int r11, int r12, int r13) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r2 = getTextPaint(r13, r11);	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = Spacingadd_2;	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
        r7 = 1;
        r1 = r10;
        r3 = r12;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001e, IllegalArgumentException -> 0x0021 }
    L_0x0018:
        if (r0 == 0) goto L_0x001d;
    L_0x001a:
        r0.draw(r8);
    L_0x001d:
        return r0;
    L_0x001e:
        r1 = move-exception;
    L_0x001f:
        r0 = r9;
        goto L_0x0018;
    L_0x0021:
        r1 = move-exception;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getStaticLayoutForSpace2(android.text.Spannable, int, int, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getStaticLayout(android.text.Spannable r10, int r11, int r12, int r13) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getTextPaint(r13, r11);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r12;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getStaticLayout(android.text.Spannable, int, int, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getStaticLayout(android.text.Spannable r10, int r11, int r12, int r13, int r14) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getTextPaint(r13, r11);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = (float) r14;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r7 = 1;
        r1 = r10;
        r3 = r12;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getStaticLayout(android.text.Spannable, int, int, int, int):android.text.StaticLayout");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0019  */
    public static android.text.StaticLayout getStaticLayout(android.text.Spannable r10, int r11, int r12, boolean r13) {
        /*
        r8 = new android.graphics.Canvas;
        r8.<init>();
        r9 = 0;
        r0 = new android.text.StaticLayout;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r2 = getTextPaint(r12, r13);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r4 = DefaultAlignment;	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        r7 = 1;
        r1 = r10;
        r3 = r11;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ NullPointerException -> 0x001d, IllegalArgumentException -> 0x0020 }
    L_0x0017:
        if (r0 == 0) goto L_0x001c;
    L_0x0019:
        r0.draw(r8);
    L_0x001c:
        return r0;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r0 = r9;
        goto L_0x0017;
    L_0x0020:
        r1 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.StaticLayoutUtil.getStaticLayout(android.text.Spannable, int, int, boolean):android.text.StaticLayout");
    }

    public static StaticLayout getCommentMoreStaticLayout(int comments_count, boolean isGiphy) {
        return getStaticLayout(new SpannableString(String.format(App.getResource().getString(R.string.TEXT_VIEW_ALL_COMMENTS_BEFORE) + " %1$d " + App.getResource().getString(R.string.TEXT_VIEW_ALL_COMMENTS_AFTER), new Object[]{Integer.valueOf(comments_count)})), App.getHardCodeWidth(isGiphy), 2131755042, false);
    }
}