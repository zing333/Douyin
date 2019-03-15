package cn.nineton.onetake.widget;

import cn.nineton.onetake.media.audiotool.AudioMixer;

public interface TimingSource {

    public static final class AudioMixerTimingSource implements TimingSource {
        AudioMixer mAudioMixer;

        AudioMixerTimingSource(AudioMixer audioMixer) {
            this.mAudioMixer = audioMixer;
        }

        public long getPlayPosition() {
            return this.mAudioMixer.getPlayPosition();
        }

        public long getMixPosition() {
            return this.mAudioMixer.getMixPosition();
        }

        public long timeToSamples(long us) {
            return this.mAudioMixer.timeToSamples(us);
        }

        public long samplesToTime(long sample) {
            return this.mAudioMixer.samplesToTime(sample);
        }

        public void pause() {
            this.mAudioMixer.pause();
        }

        public void resume() {
            this.mAudioMixer.resume();
        }
    }

    public static final class ExplicitTimingSource implements TimingSource {
        long mPlayPosition = 0;
        long mSampleRate = 48000;

        public void setPlayTime(long timeus) {
            this.mPlayPosition = timeToSamples(timeus);
        }

        public long getPlayPosition() {
            return this.mPlayPosition;
        }

        public long getMixPosition() {
            return this.mPlayPosition;
        }

        public long timeToSamples(long us) {
            return (this.mSampleRate * us) / 1000000;
        }

        public long samplesToTime(long sample) {
            return (1000000 * sample) / this.mSampleRate;
        }

        public void pause() {
        }

        public void resume() {
        }
    }

    long getMixPosition();

    long getPlayPosition();

    void pause();

    void resume();

    long samplesToTime(long j);

    long timeToSamples(long j);
}