package cn.nineton.onetake.media.audiotool;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

import cn.nineton.onetake.App;
import cn.nineton.onetake.media.Ratio;
import cn.nineton.onetake.media.videotool.ResamplerCubic;

public final class AudioMixer {
    private static final String TAG = "AudioMixer";
    private boolean VERBOSE = false;
    private int mGeneration;
    private long mIdleCount;
    private boolean mIsClosing = false;
    private boolean mIsInputsEnabled = true;
    private boolean mIsMixingEnabled = false;
    private boolean mIsPaused = false;
    private boolean mIsRealtime;
    private int mMaxChunkDuration;
    private int mMinChunkDuration;
    private short[] mMixBuffer;
    private long mMixLimitTime = Long.MAX_VALUE;
    private long mMixTime;
    private Thread mMixerThread;
    private AudioOutput mOutput;
    private int mSampleRate;
    private ArrayList<Track> mTracks = new ArrayList();

    static final class Frame {
        short[] data;
        long duration;
        int offset = 0;
        long start;

        Frame(long _start, short[] _data, int _duration) {
            this.data = _data;
            this.start = _start;
            this.duration = (long) _duration;
        }

        void trimStart(long trimTime) {
            if (this.start < trimTime) {
                long delta = Math.min(this.duration, trimTime - this.start);
                this.start += delta;
                this.offset = (int) (((long) this.offset) + (2 * delta));
                this.duration -= delta;
            }
        }

        void trimEnd(long trimTime) {
            long end = this.start + this.duration;
            if (end > trimTime) {
                this.duration -= Math.min(this.duration, end - trimTime);
            }
        }
    }

    public static final class Track {
        long mAvailTime;
        long mFirstPresentationSample;
        Queue<Frame> mFrames;
        int mIndex;
        boolean mIsEnabled;
        boolean mIsFinished;
        long mLastPresentationSample;
        long mPresentationSample;
        ResamplerCubic mResampler;
        Ratio mSpeed;
        public float mVolume;

        private Track(int index, long firstPresentationSample, long lastPresentationSample, Ratio speed) {
            this.mFrames = new LinkedList();
            this.mIsEnabled = true;
            this.mVolume = 1.0f;
            this.mAvailTime = Long.MAX_VALUE;
            this.mSpeed = new Ratio(1, 1);
            this.mResampler = new ResamplerCubic();
            this.mIndex = index;
            this.mFirstPresentationSample = firstPresentationSample;
            this.mPresentationSample = Long.MAX_VALUE;
            this.mLastPresentationSample = lastPresentationSample;
            this.mAvailTime = this.mFirstPresentationSample;
            this.mSpeed = speed.clone();
            this.mResampler.configure(this.mSpeed.getDen(), this.mSpeed.getNum());
        }

        private void setSpeed(Ratio r) {
            this.mSpeed = r.clone();
            this.mResampler.configure(this.mSpeed.getDen(), this.mSpeed.getNum());
        }

        private void resample(Frame f) {
            if (!this.mSpeed.isOne()) {
                short[] resamplerOutput = new short[((((this.mResampler.getmOutputRate() * ((int) f.duration)) / this.mResampler.getmInputRate()) + 20) * 2)];
                int resampledLength = this.mResampler.resampleStereo(resamplerOutput, f.data, ((int) f.duration) * 2);
                if (this.mPresentationSample == Long.MAX_VALUE) {
                    this.mPresentationSample = f.start;
                }
                f.data = resamplerOutput;
                f.duration = (long) (resampledLength / 2);
                f.start = this.mPresentationSample;
                f.offset = 0;
                this.mPresentationSample += f.duration;
            }
        }

        public void push(Frame f) {
            this.mFrames.add(f);
            this.mAvailTime = f.start + f.duration;
        }

        public Frame peek() {
            return (Frame) this.mFrames.peek();
        }

        public void pop() {
            this.mFrames.poll();
        }

        public void trimStart(long mixTime) {
            while (true) {
                Frame f = peek();
                if (f != null) {
                    f.trimStart(mixTime);
                    if (f.duration <= 0) {
                        pop();
                    } else {
                        return;
                    }
                }
                return;
            }
        }
    }

    public AudioMixer(AudioOutput output) {
        this.mOutput = output;
        this.mIsRealtime = output.isRealtime();
        this.mSampleRate = output.getSampleRate();
        this.mMixTime = 0;
        int mixBufferCount = Math.max(this.mSampleRate / 4, this.mOutput.getRecommendedBufferSize() * 4);
        this.mMaxChunkDuration = mixBufferCount / 2;
        this.mMinChunkDuration = Math.min(this.mMaxChunkDuration / 2, this.mSampleRate / 10);
        Log.d(TAG, String.format("mix buffer is %d samples, %f secs. mMinChunkDuration:%d mMaxChunkDuration:%d", new Object[]{Integer.valueOf(mixBufferCount), Double.valueOf(((double) mixBufferCount) / ((double) this.mSampleRate)), Integer.valueOf(this.mMinChunkDuration), Integer.valueOf(this.mMaxChunkDuration)}));
        this.mMixBuffer = new short[mixBufferCount];
        if (output.needsZeroTrack()) {
            createZeroTrack();
        }
//        this.mMixerThread = new Thread(AudioMixer$$Lambda$1.lambdaFactory$(this));
        mMixerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                access$lambda$0();
            }
        });
        this.mMixerThread.setName(TAG);
        this.mMixerThread.start();
    }

    public boolean isAudioEnabled() {
        if (this.mOutput instanceof DummyAudioOutput) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:16:0x002b, code:
            if (r0 <= 0) goto L_0x0001;
     */
    /* JADX WARNING: Missing block: B:18:?, code:
            r8.mOutput.writeAudio(r8.mMixTime, r8.mMixBuffer, 0, r0 * 2);
            java.util.Arrays.fill(r8.mMixBuffer, 0, r0 * 2, (short) 0);
     */
    /* renamed from: mixerThreadEntry */
    void access$lambda$0() {
        /*
        r8 = this;
        r0 = 0;
    L_0x0001:
        monitor-enter(r8);	 Catch:{ InterruptedException -> 0x0043 }
        r1 = r8.mIsClosing;	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x000f;
    L_0x0006:
        monitor-exit(r8);	 Catch:{ all -> 0x0048 }
        r1 = "AudioMixer";
        r2 = "mixer thread exit";
        android.util.Log.d(r1, r2);	 Catch:{ InterruptedException -> 0x0043 }
    L_0x000e:
        return;
    L_0x000f:
        if (r0 <= 0) goto L_0x001a;
    L_0x0011:
        r2 = r8.mMixTime;	 Catch:{ all -> 0x0048 }
        r4 = (long) r0;	 Catch:{ all -> 0x0048 }
        r2 = r2 + r4;
        r8.mMixTime = r2;	 Catch:{ all -> 0x0048 }
        r8.notifyAll();	 Catch:{ all -> 0x0048 }
    L_0x001a:
        r0 = r8.mix();	 Catch:{ all -> 0x0048 }
        if (r0 != 0) goto L_0x002a;
    L_0x0020:
        r2 = r8.mIdleCount;	 Catch:{ all -> 0x0048 }
        r4 = 1;
        r2 = r2 + r4;
        r8.mIdleCount = r2;	 Catch:{ all -> 0x0048 }
        r8.wait();	 Catch:{ all -> 0x0048 }
    L_0x002a:
        monitor-exit(r8);	 Catch:{ all -> 0x0048 }
        if (r0 <= 0) goto L_0x0001;
    L_0x002d:
        r1 = r8.mOutput;	 Catch:{ InterruptedException -> 0x0043 }
        r2 = r8.mMixTime;	 Catch:{ InterruptedException -> 0x0043 }
        r4 = r8.mMixBuffer;	 Catch:{ InterruptedException -> 0x0043 }
        r5 = 0;
        r6 = r0 * 2;
        r1.writeAudio(r2, r4, r5, r6);	 Catch:{ InterruptedException -> 0x0043 }
        r1 = r8.mMixBuffer;	 Catch:{ InterruptedException -> 0x0043 }
        r2 = 0;
        r3 = r0 * 2;
        r4 = 0;
        java.util.Arrays.fill(r1, r2, r3, r4);	 Catch:{ InterruptedException -> 0x0043 }
        goto L_0x0001;
    L_0x0043:
        r7 = move-exception;
        r7.printStackTrace();
        goto L_0x000e;
    L_0x0048:
        r1 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0048 }
        throw r1;	 Catch:{ InterruptedException -> 0x0043 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.AudioMixer.mixerThreadEntry():void");
    }

    public synchronized void flush(long endTimeUs) {
        try {
            this.mMixLimitTime = timeToSamples(endTimeUs);
            long idleCount = this.mIdleCount;
            notifyAll();
            while (idleCount == this.mIdleCount) {
                wait();
            }
        } catch (InterruptedException e) {
            App.warnException(e);
        }
        this.mOutput.flush();
    }

    public void close() {
        try {
            synchronized (this) {
                this.mIsClosing = true;
                notifyAll();
            }
            this.mMixerThread.join();
        } catch (InterruptedException e) {
            App.warnException(e);
        }
    }

    public long getPlayPosition() {
        return this.mOutput.getPlayPosition();
    }

    public long getMixPosition() {
        return this.mMixTime;
    }

    public long getSampleRate() {
        return (long) this.mSampleRate;
    }

    public long timeToSamples(long us) {
        return (((long) this.mSampleRate) * us) / 1000000;
    }

    public long samplesToTime(long sample) {
        return (1000000 * sample) / ((long) this.mSampleRate);
    }

    private void createZeroTrack() {
        Track t = createTrack(0, 9223372036854775806L, 0.0f, new Ratio(1, 1));
        t.mAvailTime = t.mLastPresentationSample;
    }

    public synchronized Track createTrack(long firstPresentationSample, long lastPresentationSample, float volume, Ratio speed) {
        Track t;
        if (firstPresentationSample < this.mMixTime) {
            Log.d(TAG, String.format("missed initial presentation time: first:%d mixTime:%d", new Object[]{Long.valueOf(firstPresentationSample), Long.valueOf(this.mMixTime)}));
        }
        t = new Track(this.mTracks.size(), firstPresentationSample, lastPresentationSample, speed);
        Log.d(TAG, String.format("createTrack: index:%d samples:[%d->%d]", new Object[]{Integer.valueOf(t.mIndex), Long.valueOf(t.mFirstPresentationSample), Long.valueOf(t.mLastPresentationSample)}));
        t.mVolume = volume;
        this.mTracks.add(t);
        notifyAll();
        return t;
    }

    public synchronized void disableTrack(Track t) {
        t.mIsEnabled = false;
        notifyAll();
    }

    public synchronized void deleteTrack(Track t) {
        this.mTracks.remove(t);
        notifyAll();
    }

    public void setTrackSpeed(Track t, Ratio r) {
        if (!t.mSpeed.equals(r)) {
            synchronized (this) {
                t.setSpeed(r);
            }
        }
    }

    public synchronized void addFrame(Track t, Frame f) {
        while (!this.mIsMixingEnabled && t.mIsEnabled && this.mIsInputsEnabled) {
            try {
                wait();
            } catch (InterruptedException e) {
                App.warnException(e);
            }
        }
        if (t.mIsEnabled && this.mIsInputsEnabled) {
            if (this.VERBOSE) {
                Log.d(TAG, String.format("addFrame: track:%d sample:[%d->%d] duration:%d", new Object[]{Integer.valueOf(t.mIndex), Long.valueOf(f.start), Long.valueOf(f.start + f.duration), Long.valueOf(f.duration)}));
            }
            t.resample(f);
            f.trimStart(t.mFirstPresentationSample);
            f.trimEnd(t.mLastPresentationSample);
            if (f.duration > 0) {
                while (f.start > this.mMixTime + ((long) this.mSampleRate) && t.mIsEnabled && this.mIsInputsEnabled) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (t.mIsEnabled && this.mIsInputsEnabled) {
                    t.push(f);
                } else {
                    Log.d(TAG, String.format("dropping frame: trackEnabled:%b inputsEnabled:%b", new Object[]{Boolean.valueOf(t.mIsEnabled), Boolean.valueOf(this.mIsInputsEnabled)}));
                }
            }
            notifyAll();
        } else {
            Log.d(TAG, String.format("dropping frame: trackEnabled:%b inputsEnabled:%b", new Object[]{Boolean.valueOf(t.mIsEnabled), Boolean.valueOf(this.mIsInputsEnabled)}));
        }
        return;
    }

    public synchronized void trackFinished(Track t) {
        t.mIsFinished = true;
        Log.d(TAG, String.format("trackFinished: index:%d", new Object[]{Integer.valueOf(t.mIndex)}));
        notifyAll();
    }

    public void waitUntilTime(long target) {
        target = Math.min(target, this.mMixLimitTime);
        try {
            if (this.mMixTime < target) {
                Log.d(TAG, String.format("waiting for target:%d, now:%d", new Object[]{Long.valueOf(target), Long.valueOf(this.mMixTime)}));
                synchronized (this) {
                    int generation = this.mGeneration;
                    while (this.mMixTime < target && !this.mIsPaused && generation == this.mGeneration) {
                        wait(250);
                    }
                }
                Log.d(TAG, String.format("waited for target:%d, now:%d", new Object[]{Long.valueOf(target), Long.valueOf(this.mMixTime)}));
            }
        } catch (InterruptedException e) {
            App.warnException(e);
        }
    }

    public synchronized void disableInputs() {
        this.mIsInputsEnabled = false;
        for (int i = 0; i < this.mTracks.size(); i++) {
            ((Track) this.mTracks.get(i)).mFrames.clear();
        }
        notifyAll();
    }

    public synchronized void enableInputs() {
        this.mIsInputsEnabled = true;
        notifyAll();
    }

    public synchronized void clear() {
        this.mOutput.clear();
        this.mTracks.clear();
        this.mMixTime = 0;
        this.mGeneration++;
        if (this.mOutput.needsZeroTrack()) {
            createZeroTrack();
        }
    }

    public synchronized void disableMixing() {
        this.mIsMixingEnabled = false;
        this.mGeneration++;
        notifyAll();
    }

    public void enableMixing() {
        if (!this.mIsMixingEnabled && !this.mIsPaused) {
            long time = this.mMixTime;
            synchronized (this) {
                this.mIsMixingEnabled = true;
                this.mGeneration++;
                notifyAll();
            }
            time += (long) this.mMaxChunkDuration;
            if (this.mIsRealtime) {
                Log.d(TAG, String.format("enableMixing waiting until %d", new Object[]{Long.valueOf(time)}));
                waitUntilTime(time);
            }
        }
    }

    public synchronized void pause() {
        this.mIsPaused = true;
        this.mOutput.pause();
    }

    public synchronized void resume() {
        this.mIsPaused = false;
        this.mOutput.resume();
        notifyAll();
    }

    public synchronized void setMixLimit(long maxTime) {
        this.mMixLimitTime = maxTime;
        notifyAll();
    }

    private int mix() {
        if (this.mIsPaused || !this.mIsMixingEnabled) {
            return 0;
        }
        Track t;
        ListIterator<Track> iter = this.mTracks.listIterator();
        while (iter.hasNext()) {
            t = (Track) iter.next();
            if ((t.mFrames.size() == 0 && t.mIsFinished) || this.mMixTime >= t.mLastPresentationSample) {
                Log.d(TAG, String.format("track removed: index:%d", new Object[]{Integer.valueOf(t.mIndex)}));
                iter.remove();
            }
        }
        if (this.mTracks.size() == 0) {
            this.mOutput.finished();
            return 0;
        }
        int i;
        long availTime = Long.MAX_VALUE;
        boolean someTracksFinished = false;
        for (i = 0; i < this.mTracks.size(); i++) {
            t = (Track) this.mTracks.get(i);
            if (t.mIsFinished) {
                someTracksFinished = true;
            }
            if (this.mMixTime < t.mLastPresentationSample) {
                availTime = Math.min(availTime, t.mAvailTime);
            }
        }
        if (availTime == Long.MAX_VALUE) {
            Log.w(TAG, String.format("missed mix (nothing avail): mixTime:%d maxTime:%d", new Object[]{Long.valueOf(this.mMixTime), Long.valueOf(this.mMixLimitTime)}));
            return 0;
        }
        availTime = Math.min(availTime, this.mMixLimitTime);
        if (availTime <= this.mMixTime) {
            if (this.VERBOSE) {
                Log.d(TAG, String.format("missed mix (tracks not ready): mixTime:%d availTime:%d maxTime:%d", new Object[]{Long.valueOf(this.mMixTime), Long.valueOf(availTime), Long.valueOf(this.mMixLimitTime)}));
            }
            return 0;
        }
        long playPosition = this.mOutput.getPlayPosition();
        long chunkTime = Math.min(this.mMixTime + ((long) this.mMaxChunkDuration), availTime);
        int chunkDuration = (int) (chunkTime - this.mMixTime);
        if (!this.mIsRealtime || someTracksFinished || chunkDuration >= this.mMinChunkDuration) {
            Log.d(TAG, String.format("Mixing from %d to %d samples(%d), max:%d avail:%d mTracks:%d playslack:%d", new Object[]{Long.valueOf(this.mMixTime), Long.valueOf(chunkTime), Integer.valueOf(chunkDuration), Long.valueOf(this.mMixLimitTime), Long.valueOf(availTime), Integer.valueOf(this.mTracks.size()), Long.valueOf(this.mMixTime - playPosition)}));
            i = 0;
            while (i < this.mTracks.size()) {
                if (!this.mIsRealtime || i > 0) {
                    t = (Track) this.mTracks.get(i);
                    t.trimStart(this.mMixTime);
                    mixTrack(t, chunkTime);
                }
                i++;
            }
            return chunkDuration;
        }
        if (this.VERBOSE) {
            Log.d(TAG, String.format("skip mix (chunk too short): mixTime:%d availTime:%d chunkDuration:%d", new Object[]{Long.valueOf(this.mMixTime), Long.valueOf(availTime), Integer.valueOf(chunkDuration)}));
        }
        return 0;
    }

    private void mixTrack(Track track, long maxTime) {
        int out = 0;
        long mixTime = this.mMixTime;
        while (true) {
            Frame f = track.peek();
            if (f != null && f.start < maxTime) {
                long zpDelta = f.start - mixTime;
                if (zpDelta > 0) {
                    zpDelta = Math.min(zpDelta, maxTime - mixTime);
                    mixTime += zpDelta;
                    out = (int) (((long) out) + (2 * zpDelta));
                    if (mixTime < f.start) {
                        return;
                    }
                }
                long length = Math.min(f.duration, maxTime - f.start);
                if (length > 0) {
                    int out2;
                    float volume = track.mVolume;
                    int i = 0;
                    while (true) {
                        out2 = out;
                        if (((long) i) >= 2 * length) {
                            break;
                        }
                        out = out2 + 1;
                        this.mMixBuffer[out2] = (short) Math.max(Math.min(((int) (((float) f.data[f.offset + i]) * volume)) + this.mMixBuffer[out2], 32767), -32768);
                        i++;
                    }
                    mixTime += length;
                    out = out2;
                }
                f.duration -= length;
                f.offset = (int) (((long) f.offset) + (2 * length));
                f.start += length;
                if (f.duration == 0) {
                    track.pop();
                }
            } else {
                return;
            }
        }
    }
}