package cn.nineton.onetake.media.videotool;

import android.media.MediaFormat;
import android.opengl.Matrix;
import com.lidroid.xutils.util.IOUtils;
import java.io.File;
import java.io.IOException;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.TrackBox;
import org.mp4parser.tools.Path;

import cn.nineton.onetake.media.OutputSurfaceArray;

public abstract class VideoDecoder {
    static final String TAG = "VideoDecoder";
    protected Callbacks mCallbacks;

    public interface Callbacks {
        void onFrameReady(OutputSurfaceArray outputSurfaceArray);

        boolean shouldAbort();
    }

    public abstract OutputSurfaceArray getFrames(String str, int i, int i2, int i3, double d, double d2) throws IOException;

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public static float[] getRotationMatrix(int degrees) {
        float[] mat = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        if (degrees != 0) {
            if (degrees == 90) {
                mat[0] = 0.0f;
                mat[1] = 1.0f;
                mat[4] = -1.0f;
                mat[5] = 0.0f;
            } else if (degrees == 180) {
                mat[0] = -1.0f;
                mat[1] = 0.0f;
                mat[4] = 0.0f;
                mat[5] = -1.0f;
            } else if (degrees == 270) {
                mat[0] = 0.0f;
                mat[1] = -1.0f;
                mat[4] = 1.0f;
                mat[5] = 0.0f;
            } else {
                Matrix.setRotateM(mat, 0, (float) degrees, 0.0f, 0.0f, 1.0f);
            }
        }
        return mat;
    }

    public static float[] getVideoMatrix(String filename, MediaFormat format) throws IOException {
        float[] mat = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        if (format.containsKey("rotation")) {
            return getRotationMatrix(format.getInteger("rotation"));
        }
        if (format.containsKey("rotation-degrees")) {
            return getRotationMatrix(format.getInteger("rotation-degrees"));
        }
        if (new File(filename).exists()) {
            try {
                IsoFile isoFile = new IsoFile(filename);
                org.mp4parser.support.Matrix trackMatrix = ((TrackBox) Path.getPath(isoFile, "moov/trak/")).getTrackHeaderBox().getMatrix();
                IOUtils.closeQuietly(isoFile);
                if (trackMatrix.equals(org.mp4parser.support.Matrix.ROTATE_90)) {
                    mat[0] = 0.0f;
                    mat[1] = 1.0f;
                    mat[4] = -1.0f;
                    mat[5] = 0.0f;
                }
                if (trackMatrix.equals(org.mp4parser.support.Matrix.ROTATE_180)) {
                    mat[0] = -1.0f;
                    mat[1] = 0.0f;
                    mat[4] = 0.0f;
                    mat[5] = -1.0f;
                }
                if (!trackMatrix.equals(org.mp4parser.support.Matrix.ROTATE_270)) {
                    return mat;
                }
                mat[0] = 0.0f;
                mat[1] = -1.0f;
                mat[4] = 1.0f;
                mat[5] = 0.0f;
                return mat;
            } catch (Exception e) {
                return mat;
            }
        }
        throw new IOException("file does not exist");
    }
}