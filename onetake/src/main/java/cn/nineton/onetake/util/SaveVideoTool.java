package cn.nineton.onetake.util;

public class SaveVideoTool {
    public static final long MIN_DURATION_LIMIT = 1500000;

    public static void saveJpgsFromGihpyVideo(final String videoPath, final String timeStemp, final int videoWidth, final int videoHeight) {
//        new Thread() {
//            @TargetApi(19)
//            public void run() {
//                EventBus.getDefault().post(new VideoSavingEvent(21, "longPic"));
//                EGL10Helper.withContext("saveJpgsFromGihpyVideo", SaveVideoTool$1$$Lambda$1.lambdaFactory$(System.currentTimeMillis(), videoPath, videoWidth, videoHeight, VideoDecoderFactory.createVideoDecoder(), timeStemp));
//            }
//
//            /* JADX WARNING: Removed duplicated region for block: B:44:0x02c7 A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:9:0x004e A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:45:0x02d1 A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:12:0x005c A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:46:0x02db A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:15:0x006c A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:49:0x02e9 A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:20:0x007e A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:50:0x02ed A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:23:0x00bd A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:51:0x02fb A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:26:0x014c A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:29:0x015a A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:52:0x02ff A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:32:0x0178 A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:53:0x030d A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:35:0x01c2 A:{Catch:{ Exception -> 0x038e }} */
//            /* JADX WARNING: Removed duplicated region for block: B:39:0x01f8 A:{Catch:{ Exception -> 0x038e }} */
//            private static /* synthetic */ void lambda$run$0(long r66, java.lang.String r68, int r69, int r70, com.blink.academy.onetake.VideoTools.VideoDecoder r71, java.lang.String r72, com.blink.academy.onetake.VideoTools.EGL10Helper r73) {
//                /*
//                r50 = java.lang.System.currentTimeMillis();
//                r5 = "infoinfo";
//                r6 = new java.lang.StringBuilder;
//                r6.<init>();
//                r7 = "l7-l6 : ";
//                r6 = r6.append(r7);
//                r14 = r50 - r66;
//                r6 = r6.append(r14);
//                r6 = r6.toString();
//                com.blink.academy.onetake.support.debug.LogUtil.d(r5, r6);
//                r34 = com.blink.academy.onetake.VideoTools.MediaUtils.getFormat(r68);	 Catch:{ Exception -> 0x038e }
//                r5 = "width";
//                r0 = r34;
//                r5 = r0.containsKey(r5);	 Catch:{ Exception -> 0x038e }
//                if (r5 == 0) goto L_0x029f;
//            L_0x002c:
//                r5 = "height";
//                r0 = r34;
//                r5 = r0.containsKey(r5);	 Catch:{ Exception -> 0x038e }
//                if (r5 == 0) goto L_0x029f;
//            L_0x0036:
//                r5 = "width";
//                r0 = r34;
//                r36 = r0.getInteger(r5);	 Catch:{ Exception -> 0x038e }
//                r5 = "height";
//                r0 = r34;
//                r35 = r0.getInteger(r5);	 Catch:{ Exception -> 0x038e }
//            L_0x0046:
//                r9 = 10;
//                r0 = r69;
//                r1 = r70;
//                if (r0 < r1) goto L_0x02c7;
//            L_0x004e:
//                r0 = r36;
//                r1 = r35;
//                r23 = java.lang.Math.max(r0, r1);	 Catch:{ Exception -> 0x038e }
//            L_0x0056:
//                r0 = r69;
//                r1 = r70;
//                if (r0 >= r1) goto L_0x02d1;
//            L_0x005c:
//                r0 = r36;
//                r1 = r35;
//                r24 = java.lang.Math.max(r0, r1);	 Catch:{ Exception -> 0x038e }
//            L_0x0064:
//                r28 = 1137180672; // 0x43c80000 float:400.0 double:5.61841903E-315;
//                r0 = r24;
//                r1 = r23;
//                if (r0 <= r1) goto L_0x02db;
//            L_0x006c:
//                r0 = r23;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = (r28 > r5 ? 1 : (r28 == r5 ? 0 : -1));
//                if (r5 <= 0) goto L_0x0078;
//            L_0x0073:
//                r0 = r23;
//                r0 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r28 = r0;
//            L_0x0078:
//                r5 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
//                r5 = (r28 > r5 ? 1 : (r28 == r5 ? 0 : -1));
//                if (r5 <= 0) goto L_0x02e9;
//            L_0x007e:
//                r4 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
//            L_0x0080:
//                r42 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x038e }
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;	 Catch:{ Exception -> 0x038e }
//                r7 = 22;
//                r8 = "longPic";
//                r6.<init>(r7, r8);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r7 = 0;
//                r8 = 0;
//                r10 = 0;
//                r12 = 4591870180066957722; // 0x3fb999999999999a float:-1.5881868E-23 double:0.1;
//                r5 = r71;
//                r6 = r68;
//                r37 = r5.getFrames(r6, r7, r8, r9, r10, r12);	 Catch:{ Exception -> 0x038e }
//                r10 = new com.blink.academy.onetake.VideoTools.FrameRenderer;	 Catch:{ Exception -> 0x038e }
//                r10.<init>();	 Catch:{ Exception -> 0x038e }
//                r0 = r37;
//                r10.setVideoFrames(r0);	 Catch:{ Exception -> 0x038e }
//                r5 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter;	 Catch:{ Exception -> 0x038e }
//                r5.<init>();	 Catch:{ Exception -> 0x038e }
//                r10.setFilter(r5);	 Catch:{ Exception -> 0x038e }
//                r0 = r24;
//                r1 = r23;
//                if (r0 <= r1) goto L_0x02ed;
//            L_0x00bd:
//                r60 = 1145569280; // 0x44480000 float:800.0 double:5.65986426E-315;
//                r0 = r24;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 * r60;
//                r0 = r23;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r59 = r5 / r6;
//            L_0x00c9:
//                r0 = r60;
//                r12 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r59;
//                r13 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = 1;
//                r14 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter[r5];	 Catch:{ Exception -> 0x038e }
//                r5 = 0;
//                r6 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter;	 Catch:{ Exception -> 0x038e }
//                r6.<init>();	 Catch:{ Exception -> 0x038e }
//                r14[r5] = r6;	 Catch:{ Exception -> 0x038e }
//                r5 = 1;
//                r15 = new int[r5];	 Catch:{ Exception -> 0x038e }
//                r5 = 0;
//                r6 = 0;
//                r15[r5] = r6;	 Catch:{ Exception -> 0x038e }
//                r11 = r73;
//                r5 = r10.getBitmaps(r11, r12, r13, r14, r15);	 Catch:{ Exception -> 0x038e }
//                r6 = 0;
//                r58 = r5.get(r6);	 Catch:{ Exception -> 0x038e }
//                r58 = (android.graphics.Bitmap) r58;	 Catch:{ Exception -> 0x038e }
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;	 Catch:{ Exception -> 0x038e }
//                r7 = 23;
//                r8 = "longPic";
//                r6.<init>(r7, r8);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r44 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x038e }
//                r5 = "infoinfo";
//                r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x038e }
//                r6.<init>();	 Catch:{ Exception -> 0x038e }
//                r7 = "l1 - l : ";
//                r6 = r6.append(r7);	 Catch:{ Exception -> 0x038e }
//                r14 = r44 - r42;
//                r6 = r6.append(r14);	 Catch:{ Exception -> 0x038e }
//                r6 = r6.toString();	 Catch:{ Exception -> 0x038e }
//                com.blink.academy.onetake.support.debug.LogUtil.d(r5, r6);	 Catch:{ Exception -> 0x038e }
//                r5 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x038e }
//                r6 = 92;
//                r7 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x038e }
//                r8 = 0;
//                r0 = r72;
//                r8 = com.blink.academy.onetake.support.utils.MovieFileUtil.getJPGFilePathWithIndex(r0, r8);	 Catch:{ Exception -> 0x038e }
//                r7.<init>(r8);	 Catch:{ Exception -> 0x038e }
//                r0 = r58;
//                r0.compress(r5, r6, r7);	 Catch:{ Exception -> 0x038e }
//                r46 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x038e }
//                r5 = "durationUs";
//                r0 = r34;
//                r32 = r0.getLong(r5);	 Catch:{ Exception -> 0x038e }
//                r0 = r32;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r6 = 1232348160; // 0x49742400 float:1000000.0 double:6.088608896E-315;
//                r30 = r5 / r6;
//                r6 = 1500000; // 0x16e360 float:2.101948E-39 double:7.410985E-318;
//                r5 = (r32 > r6 ? 1 : (r32 == r6 ? 0 : -1));
//                if (r5 >= 0) goto L_0x02fb;
//            L_0x014c:
//                r0 = r30;
//                r0 = (double) r0;	 Catch:{ Exception -> 0x038e }
//                r18 = r0;
//            L_0x0151:
//                r6 = 4591870180066957722; // 0x3fb999999999999a float:-1.5881868E-23 double:0.1;
//                r5 = (r18 > r6 ? 1 : (r18 == r6 ? 0 : -1));
//                if (r5 >= 0) goto L_0x015f;
//            L_0x015a:
//                r18 = 4591870180066957722; // 0x3fb999999999999a float:-1.5881868E-23 double:0.1;
//            L_0x015f:
//                r16 = 0;
//                r11 = r71;
//                r12 = r68;
//                r13 = r23;
//                r14 = r24;
//                r15 = r9;
//                r37 = r11.getFrames(r12, r13, r14, r15, r16, r18);	 Catch:{ Exception -> 0x038e }
//                r63 = r37.size();	 Catch:{ Exception -> 0x038e }
//                r0 = r24;
//                r1 = r23;
//                if (r0 <= r1) goto L_0x02ff;
//            L_0x0178:
//                r40 = r28;
//                r0 = r24;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 * r40;
//                r0 = r23;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r39 = r5 / r6;
//            L_0x0184:
//                r0 = r28;
//                r5 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r63;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r6 = r6 * r28;
//                r6 = (int) r6;	 Catch:{ Exception -> 0x038e }
//                r7 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Exception -> 0x038e }
//                r41 = android.graphics.Bitmap.createBitmap(r5, r6, r7);	 Catch:{ Exception -> 0x038e }
//                r29 = new android.graphics.Canvas;	 Catch:{ Exception -> 0x038e }
//                r0 = r29;
//                r1 = r41;
//                r0.<init>(r1);	 Catch:{ Exception -> 0x038e }
//                r5 = (int) r4;	 Catch:{ Exception -> 0x038e }
//                r0 = r63;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r6 = r6 * r4;
//                r6 = (int) r6;	 Catch:{ Exception -> 0x038e }
//                r7 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Exception -> 0x038e }
//                r55 = android.graphics.Bitmap.createBitmap(r5, r6, r7);	 Catch:{ Exception -> 0x038e }
//                r54 = new android.graphics.Canvas;	 Catch:{ Exception -> 0x038e }
//                r54.<init>(r55);	 Catch:{ Exception -> 0x038e }
//                r57 = new android.graphics.Paint;	 Catch:{ Exception -> 0x038e }
//                r57.<init>();	 Catch:{ Exception -> 0x038e }
//                r5 = 1;
//                r0 = r57;
//                r0.setAntiAlias(r5);	 Catch:{ Exception -> 0x038e }
//                r64 = 0;
//                r12 = 0;
//                r13 = 0;
//                r0 = r24;
//                r1 = r23;
//                if (r0 <= r1) goto L_0x030d;
//            L_0x01c2:
//                r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
//                r5 = r39 / r5;
//                r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
//                r6 = r40 / r6;
//                r5 = r5 - r6;
//                r13 = (int) r5;	 Catch:{ Exception -> 0x038e }
//            L_0x01cc:
//                r56 = 0;
//                r5 = "xiaoluo";
//                r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x038e }
//                r6.<init>();	 Catch:{ Exception -> 0x038e }
//                r7 = "x:";
//                r6 = r6.append(r7);	 Catch:{ Exception -> 0x038e }
//                r6 = r6.append(r12);	 Catch:{ Exception -> 0x038e }
//                r7 = ", y:";
//                r6 = r6.append(r7);	 Catch:{ Exception -> 0x038e }
//                r6 = r6.append(r13);	 Catch:{ Exception -> 0x038e }
//                r6 = r6.toString();	 Catch:{ Exception -> 0x038e }
//                com.blink.academy.onetake.support.debug.LogUtil.d(r5, r6);	 Catch:{ Exception -> 0x038e }
//                r38 = 0;
//            L_0x01f2:
//                r0 = r38;
//                r1 = r63;
//                if (r0 >= r1) goto L_0x0319;
//            L_0x01f8:
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;	 Catch:{ Exception -> 0x038e }
//                r7 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
//                r8 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
//                r0 = r38;
//                r14 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r8 = r8 * r14;
//                r0 = r63;
//                r14 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r8 = r8 / r14;
//                r7 = r7 + r8;
//                r7 = (int) r7;	 Catch:{ Exception -> 0x038e }
//                r8 = "longPic";
//                r6.<init>(r7, r8);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r0 = r40;
//                r5 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r39;
//                r6 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r7 = 1;
//                r7 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter[r7];	 Catch:{ Exception -> 0x038e }
//                r8 = 0;
//                r14 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter;	 Catch:{ Exception -> 0x038e }
//                r14.<init>();	 Catch:{ Exception -> 0x038e }
//                r7[r8] = r14;	 Catch:{ Exception -> 0x038e }
//                r8 = 1;
//                r8 = new int[r8];	 Catch:{ Exception -> 0x038e }
//                r14 = 0;
//                r8[r14] = r38;	 Catch:{ Exception -> 0x038e }
//                r0 = r37;
//                r5 = r0.getBitmaps(r5, r6, r7, r8);	 Catch:{ Exception -> 0x038e }
//                r6 = 0;
//                r11 = r5.get(r6);	 Catch:{ Exception -> 0x038e }
//                r11 = (android.graphics.Bitmap) r11;	 Catch:{ Exception -> 0x038e }
//                if (r11 == 0) goto L_0x029b;
//            L_0x023a:
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;	 Catch:{ Exception -> 0x038e }
//                r7 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
//                r8 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
//                r0 = r38;
//                r14 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r8 = r8 * r14;
//                r0 = r63;
//                r14 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r8 = r8 / r14;
//                r7 = r7 + r8;
//                r8 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
//                r7 = r7 + r8;
//                r7 = (int) r7;	 Catch:{ Exception -> 0x038e }
//                r8 = "longPic";
//                r6.<init>(r7, r8);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r0 = r28;
//                r14 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r28;
//                r15 = (int) r0;	 Catch:{ Exception -> 0x038e }
//                r16 = 0;
//                r17 = 0;
//                r11 = android.graphics.Bitmap.createBitmap(r11, r12, r13, r14, r15, r16, r17);	 Catch:{ Exception -> 0x038e }
//                r5 = 0;
//                r0 = r64;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r29;
//                r1 = r57;
//                r0.drawBitmap(r11, r5, r6, r1);	 Catch:{ Exception -> 0x038e }
//                r5 = (int) r4;	 Catch:{ Exception -> 0x038e }
//                r6 = (int) r4;	 Catch:{ Exception -> 0x038e }
//                r7 = 1;
//                r53 = android.graphics.Bitmap.createScaledBitmap(r11, r5, r6, r7);	 Catch:{ Exception -> 0x038e }
//                r5 = 0;
//                r0 = r56;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r0 = r54;
//                r1 = r53;
//                r2 = r57;
//                r0.drawBitmap(r1, r5, r6, r2);	 Catch:{ Exception -> 0x038e }
//                r0 = r56;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 + r4;
//                r0 = (int) r5;	 Catch:{ Exception -> 0x038e }
//                r56 = r0;
//                r0 = r64;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 + r28;
//                r0 = (int) r5;	 Catch:{ Exception -> 0x038e }
//                r64 = r0;
//                r11.recycle();	 Catch:{ Exception -> 0x038e }
//                r53.recycle();	 Catch:{ Exception -> 0x038e }
//            L_0x029b:
//                r38 = r38 + 1;
//                goto L_0x01f2;
//            L_0x029f:
//                r52 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x038e }
//                r52.<init>();	 Catch:{ Exception -> 0x038e }
//                r5 = com.blink.academy.onetake.support.utils.MovieFileUtil.getOutputMediaFile(r68);	 Catch:{ Exception -> 0x038e }
//                r0 = r52;
//                r0.setDataSource(r5);	 Catch:{ Exception -> 0x038e }
//                r5 = 18;
//                r0 = r52;
//                r5 = r0.extractMetadata(r5);	 Catch:{ Exception -> 0x038e }
//                r36 = java.lang.Integer.parseInt(r5);	 Catch:{ Exception -> 0x038e }
//                r5 = 19;
//                r0 = r52;
//                r5 = r0.extractMetadata(r5);	 Catch:{ Exception -> 0x038e }
//                r35 = java.lang.Integer.parseInt(r5);	 Catch:{ Exception -> 0x038e }
//                goto L_0x0046;
//            L_0x02c7:
//                r0 = r36;
//                r1 = r35;
//                r23 = java.lang.Math.min(r0, r1);	 Catch:{ Exception -> 0x038e }
//                goto L_0x0056;
//            L_0x02d1:
//                r0 = r36;
//                r1 = r35;
//                r24 = java.lang.Math.min(r0, r1);	 Catch:{ Exception -> 0x038e }
//                goto L_0x0064;
//            L_0x02db:
//                r0 = r24;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = (r28 > r5 ? 1 : (r28 == r5 ? 0 : -1));
//                if (r5 <= 0) goto L_0x02e7;
//            L_0x02e2:
//                r0 = r24;
//                r0 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r28 = r0;
//            L_0x02e7:
//                goto L_0x0078;
//            L_0x02e9:
//                r4 = r28;
//                goto L_0x0080;
//            L_0x02ed:
//                r59 = 1145569280; // 0x44480000 float:800.0 double:5.65986426E-315;
//                r0 = r23;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 * r59;
//                r0 = r24;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r60 = r5 / r6;
//                goto L_0x00c9;
//            L_0x02fb:
//                r18 = 4609434218613702656; // 0x3ff8000000000000 float:0.0 double:1.5;
//                goto L_0x0151;
//            L_0x02ff:
//                r39 = r28;
//                r0 = r23;
//                r5 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r5 = r5 * r39;
//                r0 = r24;
//                r6 = (float) r0;	 Catch:{ Exception -> 0x038e }
//                r40 = r5 / r6;
//                goto L_0x0184;
//            L_0x030d:
//                r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
//                r5 = r40 / r5;
//                r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
//                r6 = r39 / r6;
//                r5 = r5 - r6;
//                r12 = (int) r5;	 Catch:{ Exception -> 0x038e }
//                goto L_0x01cc;
//            L_0x0319:
//                r48 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x038e }
//                r5 = "infoinfo";
//                r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x038e }
//                r6.<init>();	 Catch:{ Exception -> 0x038e }
//                r7 = "l5 -l4 : ";
//                r6 = r6.append(r7);	 Catch:{ Exception -> 0x038e }
//                r14 = r48 - r46;
//                r6 = r6.append(r14);	 Catch:{ Exception -> 0x038e }
//                r6 = r6.toString();	 Catch:{ Exception -> 0x038e }
//                com.blink.academy.onetake.support.debug.LogUtil.d(r5, r6);	 Catch:{ Exception -> 0x038e }
//                r61 = com.blink.academy.onetake.support.utils.MovieFileUtil.getLongThumbnailFilePath(r72);	 Catch:{ Exception -> 0x038e }
//                r5 = 92;
//                r6 = 1;
//                r0 = r61;
//                r1 = r41;
//                com.blink.academy.onetake.support.utils.FileUtil.writeJPGFileToDisk(r0, r1, r5, r6);	 Catch:{ Exception -> 0x038e }
//                r62 = com.blink.academy.onetake.support.utils.MovieFileUtil.getMoreShortLongThumbnailFilePath(r72);	 Catch:{ Exception -> 0x038e }
//                r5 = 92;
//                r6 = 1;
//                r0 = r62;
//                r1 = r55;
//                com.blink.academy.onetake.support.utils.FileUtil.writeJPGFileToDisk(r0, r1, r5, r6);	 Catch:{ Exception -> 0x038e }
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;	 Catch:{ Exception -> 0x038e }
//                r7 = 100;
//                r8 = "longPic";
//                r6.<init>(r7, r8);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r6 = new com.blink.academy.onetake.support.events.VideoDurationEvent;	 Catch:{ Exception -> 0x038e }
//                r14 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
//                r14 = r14 * r18;
//                r7 = (int) r14;	 Catch:{ Exception -> 0x038e }
//                r6.<init>(r7);	 Catch:{ Exception -> 0x038e }
//                r5.post(r6);	 Catch:{ Exception -> 0x038e }
//                r5 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x038e }
//                r20 = new com.blink.academy.onetake.ui.activity.video.VideoSavedEvent;	 Catch:{ Exception -> 0x038e }
//                r22 = "";
//                r25 = 0;
//                r26 = 0;
//                r21 = r72;
//                r20.<init>(r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x038e }
//                r0 = r20;
//                r5.post(r0);	 Catch:{ Exception -> 0x038e }
//            L_0x038d:
//                return;
//            L_0x038e:
//                r31 = move-exception;
//                r31.printStackTrace();
//                goto L_0x038d;
//                */
//                throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.SaveVideoTool.1.lambda$run$0(long, java.lang.String, int, int, com.blink.academy.onetake.VideoTools.VideoDecoder, java.lang.String, com.blink.academy.onetake.VideoTools.EGL10Helper):void");
//            }
//        }.start();
    }
}