package cn.nineton.onetake.media;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import java.io.File;
import java.io.IOException;

public class MediaUtils {
    static final String TAG = "MediaUtils";
    static int mID = 0;

    public static synchronized int getId() {
        int i;
        synchronized (MediaUtils.class) {
            i = mID;
            mID = i + 1;
        }
        return i;
    }

    public static synchronized MediaExtractor createExtractor(String filename) throws IOException {
        MediaExtractor extractor;
        synchronized (MediaUtils.class) {
            try {
                //BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("createExtractor: opening file:%s", new Object[]{filename}));
                extractor = new MediaExtractor();
                String label = String.format("id:%d file:%s", new Object[]{Integer.valueOf(getId()), new File(filename).getName()});
                extractor.setDataSource(filename);
                dumpExtractor(label, extractor);
            } catch (IOException e) {
                e.printStackTrace();
                File file = new File(filename);
                //BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("IOEXCEPTION OPENING EXTRACTOR: filename:%s, File is exists:%s", new Object[]{filename, Boolean.valueOf(file.exists())}));
                throw e;
            }
        }
        return extractor;
    }

    public static synchronized void dumpExtractor(String label, MediaExtractor extractor) {
        synchronized (MediaUtils.class) {
            //BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("%s: tracks:%d", new Object[]{label, Integer.valueOf(extractor.getTrackCount())}));
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                if (format != null) {
                    if (format.getString("mime") != null) {
                        //BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("%s: track:%d mime:%s", new Object[]{label, Integer.valueOf(i), mime}));
                    } else {
                        //BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("%s: track:%d NO MIME", new Object[]{label, Integer.valueOf(i)}));
                    }
                } else {
                   // BuglyLogUtil.writeKeyAndValueLog(TAG, String.format("%s: track:%d NO FORMAT", new Object[]{label, Integer.valueOf(i)}));
                }
            }
        }
    }

    public static MediaFormat getFormat(String filename) throws IOException {
        MediaExtractor extractor = createExtractor(filename);
        MediaFormat format = getVideoTrack(extractor);
        extractor.release();
        return format;
    }

    public static int getRotation(MediaFormat format) {
        if (format.containsKey("rotation-degrees")) {
            return format.getInteger("rotation-degrees");
        }
        return 0;
    }

    public static boolean isRotated(MediaFormat format) {
        int rotation = getRotation(format);
        return rotation == 90 || rotation == 270;
    }

    public static int getRotatedWidth(MediaFormat format) {
        return isRotated(format) ? format.getInteger("height") : format.getInteger("width");
    }

    public static int getRotatedHeight(MediaFormat format) {
        return isRotated(format) ? format.getInteger("width") : format.getInteger("height");
    }

    public static MediaFormat getVideoTrack(MediaExtractor extractor) {
        for (int retries = 0; retries < 3; retries++) {
            int i = 0;
            while (i < extractor.getTrackCount()) {
                try {
                    MediaFormat format = extractor.getTrackFormat(i);
                    //BuglyLogUtil.writeKeyAndValueLog("VideoMime", String.format("mime : %s", new Object[]{format.getString("mime")}));
                    if (format.getString("mime").startsWith("video/")) {
                        extractor.selectTrack(i);
                        return format;
                    }
                    i++;
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    //BuglyLogUtil.writeBuglyLog(e2.getMessage());
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static MediaFormat getAudioTrack(MediaExtractor extractor) {
        for (int retries = 0; retries < 3; retries++) {
            int i = 0;
            while (i < extractor.getTrackCount()) {
                try {
                    MediaFormat format = extractor.getTrackFormat(i);
                    if (format.getString("mime").startsWith("audio/")) {
                        extractor.selectTrack(i);
                        return format;
                    }
                    i++;
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static MediaCodec createCodec(String mime) {
        MediaCodec codec = createCodecSafe(mime);
        if (codec == null) {
            codec = createCodecSafe(mime);
        }
        if (codec != null) {
            return codec;
        }
        throw new RuntimeException(String.format("cannot create codec %s", new Object[]{mime}));
    }

    static MediaCodec createCodecSafe(String mime) {
        try {
            MediaCodec codec = MediaCodec.createDecoderByType(mime);
            if (codec == null) {
                return null;
            }
            codec.getName();
            return codec;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}