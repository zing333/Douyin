package cn.nineton.onetake.media.videotool;

import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
//import com.alipay.sdk.data.a;
//import com.blink.academy.onetake.VideoTools.VideoEncoder.Quality;
//import com.blink.academy.onetake.bean.AudioTrimBean;
//import com.blink.academy.onetake.bean.longvideo.UploadVideoBean;
//import com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;

import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.util.Arrays;

import cn.nineton.onetake.bean.UploadVideoBean;
import cn.nineton.onetake.event.VideoSavingEvent;
import cn.nineton.onetake.media.FrameRenderer;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;

public class VideoEncoderSW implements VideoEncoder {
    private static String TAG = "VideoEncoderSW";
    ByteBuffer encodedData;
    int mFrameRate;
    int mOutHeight;
    int mOutWidth;
    ByteBuffer mPixels;
    private long videoProgressTag;
    X264Encoder x264;

    public VideoEncoderSW() {
        this.videoProgressTag = 0;
    }

    public VideoEncoderSW(long videoProgressTag) {
        this.videoProgressTag = videoProgressTag;
    }

    public void start(int outWidth, int outHeight, int frameRate, int numFrames, Quality quality) {
        outWidth &= -2;
        outHeight &= -2;
        this.mOutWidth = outWidth;
        this.mOutHeight = outHeight;
        this.mFrameRate = frameRate;
        this.encodedData = ByteBuffer.allocateDirect(5242880);
        int crf = 23;
        int maxbitrate = 3500;
        int fps_num = frameRate;
        switch (quality) {
            case LOW:
                crf = 26;
                maxbitrate = 700;
                break;
            case HIGH:
                crf = 23;
                maxbitrate = 3500;
                break;
            case DRAFT:
                crf = 16;
                maxbitrate = AudioTrimBean.UNIT;
                break;
        }
        this.x264 = new X264Encoder(null, outWidth, outHeight, numFrames, fps_num, 1, crf, maxbitrate);
        if (this.x264.start(this.encodedData)) {
            this.mPixels = ByteBuffer.allocateDirect((outWidth * outHeight) * 4);
            return;
        }
        throw new RuntimeException("could not start encoder");
    }

    public void putFrame(long pts) {
        this.mPixels.position(0);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glReadPixels(0, 0, this.mOutWidth, this.mOutHeight, 6408, 5121, this.mPixels);
        this.x264.addFrame(pts, this.mPixels);
    }

    public void finish() {
        this.x264.finish();
        this.mPixels = null;
    }

    public void writeMP4(String filename, byte[] audioData) {
        int totalBytes = this.x264.totalBytes();
        this.encodedData.limit(totalBytes);
        MP4Writer.writeMP4(filename, Arrays.copyOfRange(this.encodedData.array(), this.encodedData.arrayOffset(), this.encodedData.arrayOffset() + totalBytes), audioData, (int) (1000000 / ((long) this.mFrameRate)));
        this.encodedData = null;
    }

    public void putFrames(final String filename, final int outWidth, final int outHeight, final FrameRenderer renderer, final boolean isFromDraft, final Quality quality) {
//        EGL10Helper.withContext("VideoEncoderSW.putFrames", VideoEncoderSW$$Lambda$1.lambdaFactory$(this, renderer, outWidth, outHeight, quality, isFromDraft, filename));
        EGL10Helper.withContext("VideoEncoderSW.putFrames", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$putFrames$0(renderer,outWidth,outHeight,quality,isFromDraft,filename,eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$putFrames$0(FrameRenderer renderer, int outWidth, int outHeight, Quality quality, boolean isFromDraft, String filename, EGL10Helper egl) {
        OutputSurfaceArray input = renderer.getVideoFrames();
        start(outWidth, outHeight, input.mFramerate, input.size(), quality);
        EGLSurface s = egl.createPBuffer(outWidth, outHeight);
        try {
            long t1;
            egl.makeCurrent(s);
            long pts = 0;
            long time = 0;
            int size = input.size();
            int wrapFrameIndex = renderer.getWrapFrameIndex();
            for (int i = (0 % (size - wrapFrameIndex)) + wrapFrameIndex; i < size; i++) {
                GLES20.glBindFramebuffer(36160, 0);
                if (isFromDraft) {
                    renderer.drawFrameAtIndex2(0, 0, 0, outWidth, outHeight, 0, 0, outWidth, outHeight, i);
                } else {
                    renderer.drawFrameAtIndex(0, 0, 0, outWidth, outHeight, 0, 0, outWidth, outHeight, i);
                }
                t1 = System.nanoTime();
                putFrame(pts);
                time += System.nanoTime() - t1;
                pts++;
                EventBus.getDefault().post(new VideoSavingEvent((int) ((20.0f * ((float) i)) / ((float) size)), UploadVideoBean.TYPE_VIDEO, this.videoProgressTag));
            }
            t1 = System.nanoTime();
            finish();
            time += System.nanoTime() - t1;
            Log.d(TAG, String.format("encoding took %dms", time / 1000000));
            writeMP4(filename, input.audioData);
            egl.destroySurface(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            egl.destroySurface(s);
        }
    }
}