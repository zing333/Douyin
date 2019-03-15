package cn.nineton.onetake.media;

import android.opengl.GLES20;
import android.util.Log;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;

public class PixelsReader {
    static final String TAG = "PixelsReader";
    public static boolean haveBGRA = false;
    private static PixelsReader mInstance = new PixelsReader();
    private static Semaphore pixelsSemaphore = new Semaphore(1);
    private static boolean testedExtensions;
    private long mMaxPixels = 2073600;
    private IntBuffer mPixels = IntBuffer.allocate((int) this.mMaxPixels);

    interface LockPixels {
        void run(IntBuffer intBuffer);
    }

    public static synchronized PixelsReader get() {
        PixelsReader pixelsReader;
        synchronized (PixelsReader.class) {
            pixelsReader = mInstance;
        }
        return pixelsReader;
    }

    private PixelsReader() {
    }

    public void lockPixels(int width, int height, LockPixels r) {
        long size = (long) (width * height);
        if (!testedExtensions) {
            haveBGRA = GLES20.glGetString(7939).contains("EXT_read_format_bgra");
            Log.d(TAG, "haveBGRA:" + haveBGRA);
            testedExtensions = true;
        }
        if (size > this.mMaxPixels) {
            this.mMaxPixels = size;
            this.mPixels = IntBuffer.allocate((int) this.mMaxPixels);
        }
        try {
            pixelsSemaphore.acquire();
            this.mPixels.position(0);
            r.run(this.mPixels);
            pixelsSemaphore.release();
        } catch (InterruptedException e) {
        } catch (Throwable th) {
            pixelsSemaphore.release();
        }
    }
}