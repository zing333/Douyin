package cn.nineton.onetake.bean;

import android.os.Environment;
import android.util.Log;
//import com.blink.academy.onetake.support.debug.LogUtil;
import java.io.File;
import java.io.IOException;

import cn.nineton.onetake.media.Ratio;
import cn.nineton.onetake.media.audiotool.AudioDecoder;
import cn.nineton.onetake.media.audiotool.AudioMixer;
import cn.nineton.onetake.media.audiotool.AudioOutput;
import cn.nineton.onetake.util.LogUtil;

public class WaveformOutput implements AudioOutput {
    private static final String TAG = "WaveformOutput";
    AudioDecoder mAudioDecoder = new AudioDecoder();
    AudioMixer mAudioMixer;
    AudioMixer.Track mAudioTrack;
    float[] mData;
    boolean mIsFinished;
    int mSampleRate;
    int mWriteOffset;

    public static void test() {
        LogUtil.d(TAG, String.format("test start ", new Object[0]));
        try {
            WaveformOutput output = new WaveformOutput(new File(new File(Environment.getExternalStorageDirectory() + "/tmp"), "audio2.mp4").getAbsolutePath(), 0, 6000000, 10000);
            output.waitUntilFinished();
            output.close();
            LogUtil.d(TAG, String.format("data.size : %s ", new Object[]{Integer.valueOf(output.getAvailableData().length)}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WaveformOutput(String filename, long startTimeUs, long durationUs, int rate) throws IOException {
        this.mSampleRate = rate;
        try {
            this.mAudioDecoder.open(filename);
            if (durationUs == 0) {
                durationUs = this.mAudioDecoder.getDurationUs();
            }
            long durationSamples = (((long) rate) * durationUs) / 1000000;
            this.mData = new float[((int) durationSamples)];
            this.mAudioMixer = new AudioMixer(this);
            this.mAudioTrack = this.mAudioMixer.createTrack(0, durationSamples, 1.0f, new Ratio(1, 1));
            this.mAudioDecoder.start(startTimeUs, this.mAudioMixer, this.mAudioTrack, durationSamples);
            this.mAudioMixer.enableMixing();
        } catch (Exception e) {
            e.printStackTrace();
            this.mAudioDecoder.close(true);
            this.mAudioDecoder = null;
            finished();
        }
    }

    public boolean isRealtime() {
        return false;
    }

    public boolean needsZeroTrack() {
        return false;
    }

    public void close() {
        if (this.mAudioDecoder != null) {
            this.mAudioDecoder.close(true);
            this.mAudioDecoder = null;
        }
        if (this.mAudioMixer != null) {
            this.mAudioMixer.close();
            this.mAudioMixer = null;
        }
    }

    public int getAvailableCount() {
        return this.mWriteOffset;
    }

    public float[] getAvailableData() {
        if (this.mData == null) {
            this.mData = new float[0];
        }
        return this.mData;
    }

    public synchronized void waitUntilFinished() {
        while (!this.mIsFinished) {
            try {
                Log.d(TAG, String.format("waiting for waveform, available:%d duration:%d", new Object[]{Integer.valueOf(this.mWriteOffset), Integer.valueOf(this.mData.length)}));
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public boolean isFinished() {
        return this.mIsFinished;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getRecommendedBufferSize() {
        return this.mSampleRate * 4;
    }

    public long getPlayPosition() {
        return 0;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void flush() {
    }

    public void finished() {
        synchronized (this) {
            this.mIsFinished = true;
            notifyAll();
        }
    }

    public void clear() {
    }

    public void writeAudio(long presentationSample, short[] data, int offset, int num_shorts) {
        int count = Math.min(this.mData.length - this.mWriteOffset, num_shorts / 2);
        for (int i = 0; i < count; i++) {
            this.mData[this.mWriteOffset] = ((((float) data[((i * 2) + offset) + 0]) / 32767.0f) + (((float) data[((i * 2) + offset) + 1]) / 32767.0f)) * 0.5f;
            this.mWriteOffset++;
        }
    }
}