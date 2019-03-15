package cn.nineton.onetake.media.videotool;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.Range;
import android.view.Surface;
//import com.alipay.sdk.data.a;
//import com.amap.api.services.core.AMapException;
//import com.blink.academy.onetake.VideoTools.VideoEncoder.Quality;
//import com.blink.academy.onetake.bean.AudioTrimBean;
//import com.blink.academy.onetake.bean.longvideo.UploadVideoBean;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.ui.activity.video.VideoSavingEvent;

import org.greenrobot.eventbus.EventBus;

import cn.nineton.onetake.bean.UploadVideoBean;
import cn.nineton.onetake.event.VideoSavingEvent;
import cn.nineton.onetake.media.FrameRenderer;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VideoEncoderHW implements VideoEncoder {
    static final float[] CUBE = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    private static final String MIME_TYPE = "video/avc";
    private static String TAG = "VideoEncoderHW";
    public static final float[] TEXTURE_NO_ROTATION = new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static final boolean VERBOSE = false;
    ByteArrayOutputStream encodedData;
    private int mBitRate = -1;
    private BufferInfo mBufferInfo;
    private Callbacks mCallbacks;
    ByteBuffer[] mCodecInputBuffers = null;
    ByteBuffer[] mCodecOutputBuffers = null;
    public MediaCodec mEncoder;
    Surface mEncoderSurface;
    private String mFilename;
    private Map<String, Object> mFormatProps;
    private int mFrameDurationUs = -1;
    private int mFrameRate = -1;
    private final FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final FloatBuffer mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private int mHeight = -1;
    CodecInputSurface mInputSurface;
    private boolean mMuxerStarted;
    private int mNumFramesIn = 0;
    private int mWidth = -1;

    public interface Callbacks {
        void onVideoFormatChanged(MediaFormat mediaFormat);

        void onVideoFrameAvailable(BufferInfo bufferInfo, ByteBuffer byteBuffer);
    }

    enum DrainStatus {
        OUT_OF_WORK,
        END_OF_STREAM,
        WORKING
    }

    final class LevelLimits {
        int BR;
        int DPB;
        int FS;
        int MBPS;

        LevelLimits() {
        }
    }

    public VideoEncoderHW() {
        this.mGLCubeBuffer.put(CUBE).position(0);
        this.mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);
    }

    public void start(String filename, int width, int height, int frameDurationUs, int bitRate) {
        start(filename, width, height, frameDurationUs, bitRate, new HashMap());
    }

    public void start(String filename, int width, int height, int frameDurationUs, int bitRate, Map<String, Object> props) {
        this.mWidth = width;
        this.mHeight = height;
        this.mBitRate = bitRate;
        this.mFrameDurationUs = frameDurationUs;
        this.mFrameRate = AudioTrimBean.UNIT / frameDurationUs;
        this.mFilename = filename;
        this.mFormatProps = props;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void putFrame2(final FrameRenderer renderer, final long timeUs, final int frameIndex) {
//        EGL10Helper.withContext("putFrame2", VideoEncoderHW$$Lambda$1.lambdaFactory$(this, timeUs, renderer, frameIndex));
        EGL10Helper.withContext("putFrame2", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$putFrame2$0(timeUs,renderer,frameIndex,eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$putFrame2$0(long timeUs, FrameRenderer renderer, int frameIndex, EGL10Helper egl) {
        this.mInputSurface.makeCurrent(egl, timeUs);
        renderer.drawFrameAtIndex(0, 0, 0, this.mWidth, this.mHeight, 0, 0, this.mWidth, this.mHeight, frameIndex);
        EGL10Helper.clearGLError("putFrame2 after draw");
        this.mInputSurface.swap(egl);
        this.mNumFramesIn++;
    }

    public void close() {
        Log.d(TAG, "releasing encoder objects");
        if (this.mEncoder != null) {
            try {
                this.mEncoder.stop();
            } catch (IllegalStateException e) {
                Log.d(TAG, "got an illegal state exception but it's probably fine");
            }
            this.mEncoder.release();
            this.mEncoder = null;
        }
        if (this.mInputSurface != null) {
            this.mInputSurface.release();
            this.mInputSurface = null;
        }
    }

    public void flush() {
        if (this.mMuxerStarted && drainEncoder(true) != DrainStatus.END_OF_STREAM) {
            do {
            } while (drainEncoder(false) == DrainStatus.END_OF_STREAM);
        }
    }

    public void finish(OutputSurfaceArray input) {
        flush();
        close();
        if (this.mFilename != null) {
            byte[] audioData = null;
            if (input != null) {
                audioData = input.audioData;
            }
            MP4Writer.writeMP4(this.mFilename, this.encodedData.toByteArray(), audioData, this.mFrameDurationUs);
            this.encodedData = null;
        }
    }

    public void putFrames(String filename, int outWidth, int outHeight, FrameRenderer renderer, boolean isFromDraft, VideoEncoder.Quality quality) {
        int bitRate = 5000000;
        switch (quality) {
            case LOW:
                bitRate = AudioTrimBean.UNIT;
                break;
            case HIGH:
                bitRate = 5000000;
                break;
        }
        OutputSurfaceArray input = renderer.getVideoFrames();
        int size = input.size();
        if (size >= 2) {
            this.mFrameDurationUs = (int) (input.get(1).mTimestampUs - input.get(0).mTimestampUs);
            start(filename, outWidth, outHeight, this.mFrameDurationUs, bitRate);
            prepareEncoder(null, false);
            int wrapFrameIndex = renderer.getWrapFrameIndex();
            int frameIndex = (0 % (size - wrapFrameIndex)) + wrapFrameIndex;
            LogUtil.d(TAG, "drawFrameAtTime index: " + frameIndex);
            while (frameIndex < size) {
                putFrame2(renderer, input.get(frameIndex).mTimestampUs, frameIndex);
                drainEncoder(false);
                frameIndex++;
                EventBus.getDefault().post(new VideoSavingEvent((int) ((20.0f * ((float) frameIndex)) / ((float) size)), UploadVideoBean.TYPE_VIDEO));
            }
            finish(input);
        }
    }

    DrainStatus drainEncoder(boolean endOfStream) {
        return drainEncoder(endOfStream, 10000);
    }

    DrainStatus drainEncoder(boolean endOfStream, int timeOutUs) {
        if (endOfStream) {
            this.mEncoder.signalEndOfInputStream();
        }
        int encoderStatus = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, (long) timeOutUs);
        byte[] bytes;
        if (encoderStatus == -1) {
            if (!endOfStream) {
                Log.d(TAG, "TRY AGAIN!!");
                return DrainStatus.OUT_OF_WORK;
            }
        } else if (encoderStatus == -2) {
            MediaFormat newFormat = this.mEncoder.getOutputFormat();
            Log.d(TAG, "encoder output format changed: " + newFormat);
            if (this.mCallbacks != null) {
                this.mCallbacks.onVideoFormatChanged(newFormat);
            }
            if (this.encodedData != null) {
                for (int i = 0; i < 16; i++) {
                    ByteBuffer csd = newFormat.getByteBuffer("csd-" + i);
                    if (csd == null) {
                        break;
                    }
                    bytes = csd.array();
                    this.encodedData.write(bytes, 0, bytes.length);
                }
            }
            this.mMuxerStarted = true;
        } else if (encoderStatus < 0) {
            Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
        } else {
            ByteBuffer encodedBuffer;
            if (VERSION.SDK_INT >= 21) {
                encodedBuffer = this.mEncoder.getOutputBuffer(encoderStatus);
            } else {
                encodedBuffer = this.mCodecOutputBuffers[encoderStatus];
            }
            if (encodedBuffer == null) {
                throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
            }
            if (this.mCallbacks != null) {
                this.mCallbacks.onVideoFrameAvailable(this.mBufferInfo, encodedBuffer);
            }
            if ((this.mBufferInfo.flags & 2) != 0) {
                this.mBufferInfo.size = 0;
            }
            if (!(this.encodedData == null || this.mBufferInfo.size == 0)) {
                if (this.mMuxerStarted) {
                    encodedBuffer.position(this.mBufferInfo.offset);
                    encodedBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                    bytes = new byte[this.mBufferInfo.size];
                    encodedBuffer.get(bytes);
                    this.encodedData.write(bytes, 0, this.mBufferInfo.size);
                } else {
                    throw new RuntimeException("muxer hasn't started");
                }
            }
            this.mEncoder.releaseOutputBuffer(encoderStatus, true);
            if ((this.mBufferInfo.flags & 4) != 0) {
                return DrainStatus.END_OF_STREAM;
            }
        }
        return DrainStatus.WORKING;
    }

    private int align(int val, int align) {
        return ((val + (align - 1)) / align) * align;
    }

    public void prepareEncoder(Callbacks callbacks, boolean directSurface) {
        this.mCallbacks = callbacks;
        if (this.mCallbacks == null) {
            this.encodedData = new ByteArrayOutputStream();
        }
        this.mBufferInfo = new BufferInfo();
        try {
            this.mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
            CodecCapabilities caps = this.mEncoder.getCodecInfo().getCapabilitiesForType(MIME_TYPE);
            int maxLevel = 0;
            for (CodecProfileLevel profileLevel : caps.profileLevels) {
                maxLevel = Math.max(maxLevel, profileLevel.level);
            }
            LevelLimits limits = getLevel(maxLevel);
            this.mBitRate = Math.min(this.mBitRate, limits.BR * 1000);//a.c);
            int inWidth = this.mWidth;
            int inHeight = this.mHeight;
            if (VERSION.SDK_INT >= 21) {
                VideoCapabilities vcaps = caps.getVideoCapabilities();
                int walign = vcaps.getWidthAlignment();
                int halign = vcaps.getHeightAlignment();
                Range<Integer> heightCaps;
                Range<Integer> widthCaps;
                if (inWidth < inHeight) {
                    heightCaps = vcaps.getSupportedHeights();
                    this.mHeight = align(this.mHeight, halign);
                    this.mHeight = Math.min(this.mHeight, ((Integer) heightCaps.getUpper()).intValue());
                    widthCaps = vcaps.getSupportedWidthsFor(this.mHeight);
                    this.mWidth = (this.mHeight * inWidth) / inHeight;
                    this.mWidth = align(this.mWidth, walign);
                    this.mWidth = Math.min(this.mWidth, ((Integer) widthCaps.getUpper()).intValue());
                } else {
                    widthCaps = vcaps.getSupportedWidths();
                    this.mWidth = align(this.mWidth, walign);
                    this.mWidth = Math.min(this.mWidth, ((Integer) widthCaps.getUpper()).intValue());
                    heightCaps = vcaps.getSupportedHeightsFor(this.mWidth);
                    this.mHeight = (this.mWidth * inHeight) / inWidth;
                    this.mHeight = align(this.mHeight, halign);
                    this.mHeight = Math.min(this.mHeight, ((Integer) heightCaps.getUpper()).intValue());
                }
                this.mFrameRate = Math.min(this.mFrameRate, limits.MBPS / (((this.mWidth + 15) / 16) * ((this.mHeight + 15) / 16)));
            } else {
                int widthInMBS = (this.mWidth + 15) / 16;
                int sizeInMBS = widthInMBS * ((this.mHeight + 15) / 16);
                if (sizeInMBS > limits.FS) {
                    widthInMBS = (int) Math.ceil(((double) widthInMBS) / Math.sqrt(((double) sizeInMBS) / ((double) limits.FS)));
                    int heightInMBS = limits.FS / widthInMBS;
                    sizeInMBS = widthInMBS * heightInMBS;
                    this.mWidth = Math.min(inWidth, widthInMBS * 16);
                    this.mHeight = Math.min(inHeight, heightInMBS * 16);
                }
                this.mFrameRate = Math.min(this.mFrameRate, limits.MBPS / sizeInMBS);
            }
            MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, this.mWidth, this.mHeight);
            format.setInteger("color-format", 2130708361);
            format.setInteger("bitrate", this.mBitRate);
            format.setInteger("frame-rate", this.mFrameRate);
            format.setInteger("i-frame-interval", 1);
            if (this.mFormatProps != null) {
                for (Entry<String, Object> entry : this.mFormatProps.entrySet()) {
                    if (entry.getValue() instanceof Integer) {
                        format.setInteger((String) entry.getKey(), ((Integer) entry.getValue()).intValue());
                    }
                }
            }
            this.mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            this.mEncoderSurface = this.mEncoder.createInputSurface();
            if (!directSurface) {
                this.mInputSurface = new CodecInputSurface(this.mEncoderSurface, this.mWidth, this.mHeight);
            }
            if (VERSION.SDK_INT < 21) {
                this.mCodecInputBuffers = this.mEncoder.getInputBuffers();
                this.mCodecOutputBuffers = this.mEncoder.getOutputBuffers();
            }
            this.mEncoder.start();
            this.mMuxerStarted = false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    MediaFormat getOutputFormat() {
        return this.mEncoder.getOutputFormat();
    }

    private LevelLimits getLevel(int level) {
        int MBPS = 0;
        int FS = 0;
        int BR = 0;
        int DPB = 0;
        switch (level) {
            case 1:
                MBPS = 1485;
                FS = 99;
                BR = 64;
                DPB = 396;
                break;
            case 2:
                MBPS = 1485;
                FS = 99;
                BR = 128;
                DPB = 396;
                break;
            case 4:
                MBPS = 3000;//AMapException.CODE_AMAP_ROUTE_OUT_OF_SERVICE;
                FS = 396;
                BR = 192;
                DPB = 900;
                break;
            case 8:
                MBPS = 6000;
                FS = 396;
                BR = 384;
                DPB = 2376;
                break;
            case 16:
                MBPS = 11880;
                FS = 396;
                BR = 768;
                DPB = 2376;
                break;
            case 32:
                MBPS = 11880;
                FS = 396;
                BR = 2000;//AMapException.CODE_AMAP_SERVICE_TABLEID_NOT_EXIST;
                DPB = 2376;
                break;
            case 64:
                MBPS = 19800;
                FS = 792;
                BR = 4000;//AMapException.CODE_AMAP_SHARE_LICENSE_IS_EXPIRED;
                DPB = 4752;
                break;
            case 128:
                MBPS = 20250;
                FS = 1620;
                BR = 4000;//AMapException.CODE_AMAP_SHARE_LICENSE_IS_EXPIRED;
                DPB = 8100;
                break;
            case 256:
                MBPS = 40500;
                FS = 1620;
                BR = 10000;
                DPB = 8100;
                break;
            case 512:
                MBPS = 108000;
                FS = 3600;
                BR = 14000;
                DPB = 18000;
                break;
            case 1024:
                MBPS = 216000;
                FS = 5120;
                BR = 20000;//a.d;
                DPB = 20480;
                break;
            case 2048:
                MBPS = 245760;
                FS = 8192;
                BR = 20000;//a.d;
                DPB = 32768;
                break;
            case 4096:
                MBPS = 245760;
                FS = 8192;
                BR = 50000;
                DPB = 32768;
                break;
            case 8192:
                MBPS = 522240;
                FS = 8704;
                BR = 50000;
                DPB = 34816;
                break;
            case 16384:
                MBPS = 589824;
                FS = 22080;
                BR = 135000;
                DPB = 110400;
                break;
            case 32768:
                MBPS = 983040;
                FS = 36864;
                BR = 240000;
                DPB = 184320;
                break;
            case 65536:
                MBPS = 2073600;
                FS = 36864;
                BR = 240000;
                DPB = 184320;
                break;
        }
        LevelLimits l = new LevelLimits();
        l.MBPS = MBPS;
        l.FS = FS;
        l.BR = BR;
        l.DPB = DPB;
        return l;
    }
}