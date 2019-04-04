package cn.nineton.onetake.util;

import android.media.MediaFormat;
import android.os.Build;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.VideoTools.FilterView.VideoSpeedup;
//import com.blink.academy.onetake.VideoTools.MediaUtils;
//import com.blink.academy.onetake.VideoTools.VideoDecoder18;
//import com.blink.academy.onetake.VideoTools.VideoTranscoder.Client;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.proxy.ProxyBaseLineBean;
//import com.blink.academy.onetake.bean.proxy.ProxyFileSaveInfos;
//import com.blink.academy.onetake.bean.proxy.ProxySaveBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.FileModifyTimeCompare;
//import com.blink.academy.onetake.support.utils.FileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.ProxyBaseLineBean;
import cn.nineton.onetake.bean.ProxyFileInfo;
import cn.nineton.onetake.bean.ProxyFileSaveInfos;
import cn.nineton.onetake.bean.ProxySaveBean;
import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.media.videotool.VideoTranscoder;
import cn.nineton.onetake.widget.FilterView;
import cn.nineton.onetake.widget.VideoDecoder18;

public class ProxyVideoManager {
    public static final int CACHE_FILE_SIZE = 300;
    public static final String TAG = "ProxyVideoManager";
    public static final long WEAK_TIME = 604800;
    private static ProxyVideoManager mProxyVideoManager;
    public static final String proxyVideoPath = Config.getProxyVideoFilePath();
    public static final String proxyBaseLineBeanPath = (proxyVideoPath + "baseLine.json");
    public static final String proxySaveInfoSavePath = (proxyVideoPath + "savePath.json");
    ProxyBaseLineBean proxyBaseLineBean;
    private List<ProxySaveBean> proxySaveBeanList;
    private double proxySpeedBaseLine = -1.0d;
    private HashMap<String, Integer> videoFrameRates = new HashMap();

    private ProxyVideoManager() {
        if (new File(proxyBaseLineBeanPath).exists()) {
            this.proxyBaseLineBean = (ProxyBaseLineBean) JsonParserUtil.deserializeByJson(FileUtil.readFile(proxyBaseLineBeanPath, Charset.defaultCharset()), ProxyBaseLineBean.class);
            if (this.proxyBaseLineBean != null) {
                if ((((float) (System.currentTimeMillis() - this.proxyBaseLineBean.getFileCreateTime())) * 1.0f) / 1000.0f > 604800.0f) {
                    this.proxyBaseLineBean = null;
                }
            }
        }
    }

    public static ProxyVideoManager getInstance() {
        if (mProxyVideoManager == null) {
            mProxyVideoManager = new ProxyVideoManager();
        }
        return mProxyVideoManager;
    }

    public void startInstance() {
        startProxyTestSpeed();
    }

    public double getProxyBaseLineSpeed() {
        if (this.proxyBaseLineBean != null) {
            return this.proxyBaseLineBean.getProxyBaseLine();
        }
        return 0.0d;
    }

    private double getProxyDirSize() {
        return FileUtil.getFileOrFilesSize(proxyVideoPath, 1048576);
    }

    private double startProxyTestSpeed() {
        if (this.proxyBaseLineBean != null) {
            return this.proxyBaseLineBean.getProxyBaseLine();
        }
        String testFilePath = proxyVideoPath + "proxyTest.mp4";
        if (!new File(testFilePath).exists()) {
            int success = saveProxyTestFileToLocal(testFilePath);
            if (success != 0) {
                return (double) success;
            }
        }
        double baseLineValue = VideoDecoder18.getProxyTestFileBaseLineSpeed(testFilePath);
        LogUtil.e("proxySpeed", String.format("speed: %s", new Object[]{Double.valueOf(baseLineValue)}));
        if (baseLineValue == 0.0d) {
            return baseLineValue;
        }
        this.proxyBaseLineBean = new ProxyBaseLineBean(System.currentTimeMillis(), baseLineValue);
        FileUtil.writeInfoToFile(proxyBaseLineBeanPath, JsonParserUtil.serializeToJson(this.proxyBaseLineBean));
        return baseLineValue;
    }

    private int saveProxyTestFileToLocal(String saveFilePath) {
        try {
            InputStream is = App.getContext().getAssets().open("proxy/proxytest.mp4");
            FileOutputStream fos = new FileOutputStream(new File(saveFilePath));
            byte[] buffer = new byte[1024];
            int count = 0;
            while (true) {
                count++;
                int len = is.read(buffer);
                if (len == -1) {
                    is.close();
                    fos.close();
                    return 0;
                }
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private int[] getOutputWidthHeight(int inWidth, int inHeight) {
        int[] outValue = new int[]{inWidth, inHeight};
        int outWidth = 0;
        int outHeight = 0;
        if (inWidth > inHeight) {
            if (inHeight > 1080) {
                outWidth = (int) ((((float) (inWidth * 1080)) * 1.0f) / ((float) inHeight));
                outHeight = 1080;
            }
        } else if (inWidth > 1080) {
            outHeight = (int) ((((float) (inHeight * 1080)) * 1.0f) / ((float) inWidth));
            outWidth = 1080;
        }
        if (!(outWidth == 0 || outHeight == 0)) {
            outHeight -= outHeight % 2;
            outValue[0] = outWidth - (outWidth % 2);
            outValue[1] = outHeight;
        }
        return outValue;
    }

    private boolean isSumsangPhone(int videoWidth, int videoHeight) {
        return Build.MODEL.contains("SM-") && Math.max(videoWidth, videoHeight) > 3500;
    }

    public boolean needProxyVideo(LongVideosModel videoModel) {
        if (isSumsangPhone(videoModel.getVideoWidth(), videoModel.getVideoHeight())) {
            return true;
        }
        FilterView.VideoSpeedup videoSpeedUp = videoModel.getVideoSpeedUp();
        if (videoSpeedUp == FilterView.VideoSpeedup.EIGHTMM || videoSpeedUp == FilterView.VideoSpeedup.NORMAL || videoSpeedUp == FilterView.VideoSpeedup.SLOW || videoSpeedUp == FilterView.VideoSpeedup.STEP) {
            return false;
        }
        String videoPath = videoModel.getOriginalMediaPath();
        int videoFrameRate = -1;
        if (this.videoFrameRates != null && this.videoFrameRates.size() > 0 && this.videoFrameRates.containsKey(videoPath)) {
            videoFrameRate = ((Integer) this.videoFrameRates.get(videoPath)).intValue();
        }
        if (-1 == videoFrameRate) {
            try {
                MediaFormat videoFormat = MediaUtils.getVideoTrack(MediaUtils.createExtractor(videoPath));
                if (videoFormat == null) {
                    return false;
                }
                try {
                    videoFrameRate = videoFormat.getInteger("frame-rate");
                    this.videoFrameRates.put(videoPath, Integer.valueOf(videoFrameRate));
                } catch (NullPointerException e) {
                    return true;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        }
        int videoWidth = videoModel.getVideoWidth();
        int videoHeight = videoModel.getVideoHeight();
        if (0.4d * ((((4280000.0d * Math.pow((double) (videoWidth * videoHeight), -0.8289d)) + 40.0d) * this.proxyBaseLineBean.getProxyBaseLine()) / 64.8605d) < ((double) (((float) videoFrameRate) * videoModel.getSpeed()))) {
            return true;
        }
        return false;
    }

    public void resumeProxyFile(VideoTranscoder.Client client) {
        if (client != null) {
            client.resume();
        }
    }

    public void pauseProxyVideo(VideoTranscoder.Client client) {
        if (client != null) {
            client.pause();
        }
    }

    public void stopProxyVideo(VideoTranscoder.Client client) {
        if (client != null) {
            client.abort();
        }
    }

    public void startOutputProxyFile(LongVideosModel videosModel, ProxyFileInfo info, VideoTranscoder.Client client,boolean needSendProgressEvent){

    }
//    /* JADX WARNING: Removed duplicated region for block: B:9:0x007a  */
//    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
//    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
//    /* JADX WARNING: Removed duplicated region for block: B:9:0x007a  */
//    public void startOutputProxyFile(com.blink.academy.onetake.bean.longvideo.LongVideosModel r37, com.blink.academy.onetake.bean.proxy.ProxyFileInfo r38, com.blink.academy.onetake.VideoTools.VideoTranscoder.Client r39, boolean r40) {
//        /*
//        r36 = this;
//        r6 = 0;
//        r22 = 0;
//        r10 = r38.getInputVideoPath();	 Catch:{ IOException -> 0x0075 }
//        r18 = r38.getOutputVideoPath();	 Catch:{ IOException -> 0x0075 }
//        r8 = r38.getInputVideoDurationUs();	 Catch:{ IOException -> 0x0075 }
//        r12 = r38.getInputVideoStartTimeUs();	 Catch:{ IOException -> 0x0075 }
//        r6 = com.blink.academy.onetake.VideoTools.MediaUtils.getFormat(r10);	 Catch:{ IOException -> 0x0075 }
//        r11 = com.blink.academy.onetake.VideoTools.MediaUtils.getRotatedWidth(r6);	 Catch:{ IOException -> 0x0075 }
//        r7 = com.blink.academy.onetake.VideoTools.MediaUtils.getRotatedHeight(r6);	 Catch:{ IOException -> 0x0075 }
//        r21 = r38.getProxyVideoSpeed();	 Catch:{ IOException -> 0x0075 }
//        r0 = r36;
//        r16 = r0.getOutputWidthHeight(r11, r7);	 Catch:{ IOException -> 0x0075 }
//        r28 = 0;
//        r17 = r16[r28];	 Catch:{ IOException -> 0x0075 }
//        r28 = 1;
//        r14 = r16[r28];	 Catch:{ IOException -> 0x0075 }
//        r15 = 0;
//        r23 = new com.blink.academy.onetake.VideoTools.VideoTranscoder$Request;	 Catch:{ IOException -> 0x0075 }
//        r23.<init>();	 Catch:{ IOException -> 0x0075 }
//        r0 = r23;
//        r0.setInputPath(r10);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r0.setInputTime(r12);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r0.setInputDuration(r8);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r1 = r21;
//        r0.setInputRatio(r1);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r0.setOutputRotation(r15);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r1 = r18;
//        r0.setOutputPath(r1);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r1 = r17;
//        r0.setOutputSize(r1, r14);	 Catch:{ IOException -> 0x0131 }
//        r28 = 1;
//        r0 = r23;
//        r1 = r28;
//        r0.setOutputKeyframeInterval(r1);	 Catch:{ IOException -> 0x0131 }
//        r0 = r23;
//        r1 = r21;
//        r0.setOutputRatio(r1);	 Catch:{ IOException -> 0x0131 }
//        r22 = r23;
//    L_0x0072:
//        if (r22 != 0) goto L_0x007a;
//    L_0x0074:
//        return;
//    L_0x0075:
//        r4 = move-exception;
//    L_0x0076:
//        r4.printStackTrace();
//        goto L_0x0072;
//    L_0x007a:
//        r24 = java.lang.System.nanoTime();
//        r20 = r37;
//        r19 = new com.blink.academy.onetake.support.manager.ProxyVideoManager$1;	 Catch:{ Exception -> 0x0124 }
//        r0 = r19;
//        r1 = r36;
//        r2 = r40;
//        r3 = r20;
//        r0.<init>(r2, r3);	 Catch:{ Exception -> 0x0124 }
//        r0 = r22;
//        r1 = r19;
//        r39 = com.blink.academy.onetake.VideoTools.VideoTranscoder.runInService(r0, r1);	 Catch:{ Exception -> 0x0124 }
//        r0 = r37;
//        r1 = r39;
//        r0.setProxyClient(r1);	 Catch:{ Exception -> 0x0124 }
//        r5 = r39.waitUntilFinished();	 Catch:{ Exception -> 0x0124 }
//        if (r5 == 0) goto L_0x0109;
//    L_0x00a2:
//        r28 = "ProxyVideoManager";
//        r29 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0124 }
//        r29.<init>();	 Catch:{ Exception -> 0x0124 }
//        r30 = "errorManager:";
//        r29 = r29.append(r30);	 Catch:{ Exception -> 0x0124 }
//        r0 = r29;
//        r29 = r0.append(r5);	 Catch:{ Exception -> 0x0124 }
//        r29 = r29.toString();	 Catch:{ Exception -> 0x0124 }
//        android.util.Log.d(r28, r29);	 Catch:{ Exception -> 0x0124 }
//    L_0x00bc:
//        r28 = "quadrupleClient";
//        r29 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0124 }
//        r29.<init>();	 Catch:{ Exception -> 0x0124 }
//        r30 = "quadrupleClient: client ";
//        r29 = r29.append(r30);	 Catch:{ Exception -> 0x0124 }
//        r0 = r29;
//        r1 = r39;
//        r29 = r0.append(r1);	 Catch:{ Exception -> 0x0124 }
//        r29 = r29.toString();	 Catch:{ Exception -> 0x0124 }
//        com.blink.academy.onetake.support.debug.LogUtil.e(r28, r29);	 Catch:{ Exception -> 0x0124 }
//        r28 = 0;
//        r0 = r37;
//        r1 = r28;
//        r0.setProxyClient(r1);	 Catch:{ Exception -> 0x0124 }
//    L_0x00e1:
//        r26 = java.lang.System.nanoTime();
//        r28 = "ProxyVideoManager";
//        r29 = "done transcode test, took %d ms";
//        r30 = 1;
//        r0 = r30;
//        r0 = new java.lang.Object[r0];
//        r30 = r0;
//        r31 = 0;
//        r32 = r26 - r24;
//        r34 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
//        r32 = r32 / r34;
//        r32 = java.lang.Long.valueOf(r32);
//        r30[r31] = r32;
//        r29 = java.lang.String.format(r29, r30);
//        android.util.Log.d(r28, r29);
//        goto L_0x0074;
//    L_0x0109:
//        if (r40 == 0) goto L_0x00bc;
//    L_0x010b:
//        r28 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ Exception -> 0x0124 }
//        r29 = new com.blink.academy.onetake.support.events.proxy.ProxyProgressEvent;	 Catch:{ Exception -> 0x0124 }
//        r30 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
//        r31 = 1;
//        r0 = r29;
//        r1 = r30;
//        r2 = r20;
//        r3 = r31;
//        r0.<init>(r1, r2, r3);	 Catch:{ Exception -> 0x0124 }
//        r28.post(r29);	 Catch:{ Exception -> 0x0124 }
//        goto L_0x00bc;
//    L_0x0124:
//        r4 = move-exception;
//        r28 = "ProxyVideoManager";
//        r29 = "Exception in test";
//        r0 = r28;
//        r1 = r29;
//        android.util.Log.e(r0, r1, r4);
//        goto L_0x00e1;
//    L_0x0131:
//        r4 = move-exception;
//        r22 = r23;
//        goto L_0x0076;
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.manager.ProxyVideoManager.startOutputProxyFile(com.blink.academy.onetake.bean.longvideo.LongVideosModel, com.blink.academy.onetake.bean.proxy.ProxyFileInfo, com.blink.academy.onetake.VideoTools.VideoTranscoder$Client, boolean):void");
//    }

    public void updateProxySaveInfo(long saveTime, String savePath) {
        if (this.proxySaveBeanList == null || this.proxySaveBeanList.size() <= 0) {
            this.proxySaveBeanList = new ArrayList();
            this.proxySaveBeanList.add(new ProxySaveBean(saveTime, savePath));
        } else {
            for (int i = this.proxySaveBeanList.size() - 1; i >= 0; i--) {
                if (((ProxySaveBean) this.proxySaveBeanList.get(i)).getSaveTime() == saveTime) {
                    this.proxySaveBeanList.remove(i);
                    this.proxySaveBeanList.add(i, new ProxySaveBean(saveTime, savePath));
                    break;
                }
            }
        }
        updateProxySaveInfo();
    }

    private void updateProxySaveInfo() {
        FileUtil.writeInfoToFile(proxySaveInfoSavePath, JsonParserUtil.serializeToJson(new ProxyFileSaveInfos(this.proxySaveBeanList)));
    }

    public void checkProxyVideoFile() {
        double proxyDirSize = FileUtil.getFileOrFilesSize(proxySaveInfoSavePath, 1048576);
        if (proxyDirSize > 300.0d) {
            if (this.proxySaveBeanList == null || this.proxySaveBeanList.size() <= 0) {
                ProxyFileSaveInfos proxyFileSaveInfos = (ProxyFileSaveInfos) JsonParserUtil.deserializeByJson(FileUtil.readFile(proxySaveInfoSavePath, Charset.defaultCharset()), ProxyFileSaveInfos.class);
                if (proxyFileSaveInfos != null) {
                    this.proxySaveBeanList = proxyFileSaveInfos.getProxySaveBeans();
                    Collections.sort(this.proxySaveBeanList, new FileModifyTimeCompare());
                }
            }
            for (int i = this.proxySaveBeanList.size() - 1; i >= 0; i--) {
                ProxySaveBean bean = (ProxySaveBean) this.proxySaveBeanList.get(i);
                if (System.currentTimeMillis() - bean.getSaveTime() <= 86400000) {
                    break;
                }
                File proxyFile = new File(bean.getSavePath());
                if (proxyFile.exists()) {
                    if (proxyDirSize - FileUtil.FormetFileSize((long) ((double) proxyFile.length()), 1048576) < 300.0d) {
                        break;
                    }
                } else {
                    this.proxySaveBeanList.remove(i);
                }
            }
            updateProxySaveInfo();
        }
    }
}