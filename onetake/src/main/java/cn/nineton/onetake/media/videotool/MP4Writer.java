package cn.nineton.onetake.media.videotool;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import org.mp4parser.Container;
import org.mp4parser.muxer.MemoryDataSourceImpl;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.tracks.AACTrackImpl;
import org.mp4parser.muxer.tracks.h264.H264TrackImpl;

public class MP4Writer {
    static void writeMP4(String filename, byte[] videoData, byte[] audioData, int frameDurationUs) {
        String TAG = "writeMP4";
        try {
            Log.d("writeMP4", "output file is " + filename);
            Movie movie = new Movie();
            if (videoData != null && videoData.length > 0) {
                movie.addTrack(new H264TrackImpl(new MemoryDataSourceImpl(videoData), "eng", 1000000, frameDurationUs));
            }
            if (audioData != null && audioData.length > 0) {
                movie.addTrack(new AACTrackImpl(new MemoryDataSourceImpl(audioData)));
            }
            Container mp4file = new DefaultMp4Builder().build(movie);
            FileOutputStream os = new FileOutputStream(new File(filename));
            FileChannel fc = os.getChannel();
            mp4file.writeContainer(fc);
            fc.close();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}