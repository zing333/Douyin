package cn.nineton.onetake.widget;

public class PlayQueue {
    int mIndex;
    boolean mIsLooped;
    Playlist mPlaylist;
    TimingSource mTimingSource;

    public static class Entry {
        public long mEndSample;
        public long mMediaTimeUs;
        public Playlist.Entry mPlayListEntry;
        public long mStartSample;

        Entry(Playlist.Entry playlistEntry, long startSample, long endSample, long mediaTimeUs) {
            this.mPlayListEntry = playlistEntry;
            this.mStartSample = startSample;
            this.mEndSample = endSample;
            this.mMediaTimeUs = mediaTimeUs;
        }
    }

    PlayQueue(TimingSource timingSource) {
        this.mTimingSource = timingSource;
    }

    void setPlaylist(Playlist playlist, boolean isLooped) {
        this.mPlaylist = playlist;
        this.mIsLooped = isLooped;
    }

    void reset() {
        this.mIndex = 0;
    }

    int size() {
        return this.mPlaylist.sortedSize();
    }

    Entry head() {
        int size = this.mPlaylist.sortedSize();
        if (size == 0) {
            return null;
        }
        int index = this.mIndex;
        if (!this.mIsLooped && index >= size) {
            return null;
        }
        int loopCount = index / size;
        Playlist.Entry e = this.mPlaylist.getSorted(index % size);
        long durationUs = e.getDurationUs();
        long startTime = (((long) loopCount) * this.mPlaylist.getDurationUs()) + e.mTimelineStartTimeUs;
        long mediaTimeUs = e.mMediaStartTimeUs;
        long startSample = this.mTimingSource.timeToSamples(startTime);
        return new Entry(e, startSample, startSample + this.mTimingSource.timeToSamples(durationUs), mediaTimeUs);
    }

    void advance() {
        this.mIndex++;
    }

    void rewind(int n) {
        this.mIndex -= n;
    }
}