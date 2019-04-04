package cn.nineton.onetake.media.videotool;

import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.VidStabilizer;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;

public class VidAnalysis {
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    static final String TAG = "VidAnalysis";
    private BlockingQueue<PixelBuffer> mFreeBuffers = new LinkedBlockingQueue();
    private BlockingQueue<ThreadToken> mFreeTokens = new LinkedBlockingQueue();
    private boolean mPaused;
    private boolean mStopped;
    private BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue();
    private ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 2, TimeUnit.SECONDS, this.mWorkQueue, new AnalyzerThreadFactory(1));
    private OutputSurfaceArray mVideoFrames;
    private Thread mainWorker;
    private VidStab vs;

    static final class AnalyzerThreadFactory implements ThreadFactory {
        int priority;

        AnalyzerThreadFactory(int priority) {
            this.priority = priority;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            if (t != null) {
                t.setPriority(this.priority);
                t.setName("VidAnalysisThread");
            }
            return t;
        }
    }

    final class FrameAnalysisWorker implements Runnable {
        Runnable callback;
        PixelBuffer frameA;
        PixelBuffer frameB;
        int frameIndex;
        ThreadToken token;

        FrameAnalysisWorker(ThreadToken _token, int _frameIndex, PixelBuffer _frameA, PixelBuffer _frameB, Runnable _callback) {
            this.token = _token;
            this.frameIndex = _frameIndex;
            this.frameA = _frameA;
            this.frameB = _frameB;
            this.callback = _callback;
        }

        public void run() {
            Log.d(VidAnalysis.TAG, String.format("thread worker %d started frame:%d refs:%d,%d", new Object[]{Integer.valueOf(this.token.index), Integer.valueOf(this.frameIndex), Integer.valueOf(this.frameA.refs), Integer.valueOf(this.frameB.refs)}));
            long t1 = System.nanoTime();
            VidStabAnalyzer analyzer = new VidStabAnalyzer();
            analyzer.initialize(VidAnalysis.this.mVideoFrames.mCaptureWidth, VidAnalysis.this.mVideoFrames.mCaptureHeight);
            analyzer.addFramebuffer(this.frameIndex, this.frameA.bytebuffer);
            analyzer.addFramebuffer(this.frameIndex, this.frameB.bytebuffer);
            synchronized (VidAnalysis.this.vs) {
                VidAnalysis.this.vs.setFrameAnalysis(this.frameIndex, analyzer);
                this.callback.run();
            }
            analyzer.destroy();
            long t2 = System.nanoTime();
            Log.d(VidAnalysis.TAG, String.format("thread worker %d finished frame:%d in %d ms", new Object[]{Integer.valueOf(this.token.index), Integer.valueOf(this.frameIndex), Long.valueOf((t2 - t1) / 1000000)}));
            this.frameA.release();
            this.frameB.release();
            VidAnalysis.this.mFreeTokens.add(this.token);
        }
    }

    final class PixelBuffer {
        public ByteBuffer bytebuffer;
        int id;
        int refs = 0;

        public PixelBuffer(int size, int _id) {
            this.id = _id;
            this.bytebuffer = ByteBuffer.allocateDirect(size);
        }

        public synchronized void addref() {
            this.refs++;
            Log.d(VidAnalysis.TAG, String.format("pixelbuffer:%d addref:%d", new Object[]{Integer.valueOf(this.id), Integer.valueOf(this.refs)}));
        }

        public synchronized void release() {
            this.refs--;
            Log.d(VidAnalysis.TAG, String.format("pixelbuffer:%d release:%d", new Object[]{Integer.valueOf(this.id), Integer.valueOf(this.refs)}));
            if (this.refs == 0) {
                VidAnalysis.this.mFreeBuffers.add(this);
            }
        }
    }

    final class ThreadToken {
        int index;

        ThreadToken(int index) {
            this.index = index;
        }

        void destroy() {
        }
    }

    public boolean start(OutputSurfaceArray videoFrames, Runnable callback) {
        int i;
        this.mVideoFrames = videoFrames;
        final long t1 = System.nanoTime();
        final ArrayList<OutputSurfaceArray.Frame> frameList = new ArrayList();
        for (i = 0; i < this.mVideoFrames.size(); i++) {
            OutputSurfaceArray.Frame frame = this.mVideoFrames.get(i);
            if (frame != null) {
                frameList.add(frame.clone(true));
            }
        }
        final int numThreads = NUMBER_OF_CORES;
        final int numBuffers = NUMBER_OF_CORES + 2;
        Log.d(TAG, String.format("using %d cores", new Object[]{Integer.valueOf(numThreads)}));
        final int w = this.mVideoFrames.mCaptureWidth;
        final int h = this.mVideoFrames.mCaptureHeight;
        final int nFrames = frameList.size();
        if (nFrames < 1) {
            return false;
        }
        for (i = 0; i < numThreads; i++) {
            this.mFreeTokens.add(new ThreadToken(i));
        }
        for (i = 0; i < numBuffers; i++) {
            this.mFreeBuffers.add(new PixelBuffer(w * h, i));
        }
        this.vs = new VidStab();
        this.vs.initialize(w, h, nFrames);
        final OutputSurfaceArray outputSurfaceArray = videoFrames;
        final Runnable runnable = callback;
        this.mainWorker = new Thread() {
            public void run() {
                try {
                    ByteBuffer rgbaPixels = ByteBuffer.allocateDirect((w * h) * 4);
                    VidStabLuminance lumaConverter = new VidStabLuminance();
                    lumaConverter.setSource(rgbaPixels);
                    EGL10Helper egl = EGL10Helper.create("analysis");
                    EGLSurface surface = egl.createPBuffer(w, h);
                    egl.makeCurrent(surface);
                    GPUImageFilter filter2 = new GPUImageFilter();
                    filter2.init();
                    filter2.onOutputSizeChanged(VidAnalysis.this.mVideoFrames.mCaptureWidth, VidAnalysis.this.mVideoFrames.mCaptureHeight);
                    PixelBuffer prev = null;
                    int i = 0;
                    while (i < nFrames && !VidAnalysis.this.mStopped) {
                        OutputSurfaceArray.Frame f = (OutputSurfaceArray.Frame) frameList.get(i);
                        if (VidAnalysis.this.mPaused) {
                            if (VidAnalysis.this.mainWorker != null) {
                                synchronized (VidAnalysis.this.mainWorker) {
                                    while (VidAnalysis.this.mPaused) {
                                        VidAnalysis.this.mainWorker.wait();
                                    }
                                    if (VidAnalysis.this.mStopped) {
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        if (VidAnalysis.this.mFreeBuffers.size() == 0) {
                            Log.d(VidAnalysis.TAG, "stalled on free buffer\n");
                        }
                        PixelBuffer buffer = (PixelBuffer) VidAnalysis.this.mFreeBuffers.take();
                        buffer.addref();
                        if (i > 0 && i != nFrames - 1) {
                            buffer.addref();
                        }
                        GLES20.glBindFramebuffer(36160, 0);
                        outputSurfaceArray.drawFrameUnrotated((float) w, (float) h, filter2, f);
                        rgbaPixels.position(0);
                        GLES20.glReadPixels(0, 0, w, h, 6408, 5121, rgbaPixels);
                        EGL10Helper.checkGLError("readpixels");
                        lumaConverter.setTarget(buffer.bytebuffer);
                        lumaConverter.convert(w * h);
                        if (i > 0) {
                            FrameAnalysisWorker worker = new FrameAnalysisWorker((ThreadToken) VidAnalysis.this.mFreeTokens.take(), i, prev, buffer, runnable);
                            if (!VidAnalysis.this.mStopped) {
                                VidAnalysis.this.mThreadPool.execute(worker);
                            }
                        }
                        prev = buffer;
                        i++;
                    }
                    if (!VidAnalysis.this.mStopped) {
                        for (i = 0; i != numThreads; i++) {
                            Log.d(VidAnalysis.TAG, String.format("waiting for worker:%d", new Object[]{Integer.valueOf(i)}));
                            ((ThreadToken) VidAnalysis.this.mFreeTokens.take()).destroy();
                        }
                        for (i = 0; i != numBuffers; i++) {
                            VidAnalysis.this.mFreeBuffers.take();
                        }
                    }
                    filter2.destroy();
                    egl.destroySurface(surface);
                    egl.release();
                    long t2 = System.nanoTime();
                    Log.d(VidAnalysis.TAG, String.format("analysis took %d ms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
                    if (VidAnalysis.this.mainWorker != null) {
                        synchronized (VidAnalysis.this.mainWorker) {
                            while (!VidAnalysis.this.mStopped) {
                                VidAnalysis.this.mainWorker.wait();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        this.mainWorker.setName("VidAnalysisMain");
        this.mainWorker.start();
        return true;
    }

    public void pause() {
        if (this.mainWorker != null) {
            synchronized (this.mainWorker) {
                this.mPaused = true;
                this.mainWorker.notifyAll();
            }
        }
    }

    public void resume() {
        if (this.mainWorker != null) {
            synchronized (this.mainWorker) {
                this.mPaused = false;
                this.mainWorker.notifyAll();
            }
        }
    }

    public void stop() {
        if (this.mainWorker != null) {
            synchronized (this.mainWorker) {
                this.mPaused = false;
                this.mStopped = true;
                this.mainWorker.notifyAll();
            }
            this.mThreadPool.shutdownNow();
            while (!this.mThreadPool.isTerminated()) {
                try {
                    this.mThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                }
            }
            try {
                if (this.mainWorker.isAlive()) {
                    this.mainWorker.join();
                }
            } catch (InterruptedException e2) {
            }
        }
    }

    public int getNumReadyFrames() {
        return this.vs.getNumReadyFrames();
    }

    public VidStabilizer.Transform[] createTransforms(boolean looped, int[] frameIndices) {
        int nFrames = frameIndices.length;
        this.vs.createTransforms(nFrames, frameIndices, looped);
        VidStabilizer.Transform[] xforms = new VidStabilizer.Transform[nFrames];
        for (int i = 0; i < nFrames; i++) {
            double[] p = new double[4];
            this.vs.getTransform(i, p);
            VidStabilizer.Transform t = new VidStabilizer.Transform();
            t.x = (float) p[0];
            t.y = (float) p[1];
            t.alpha = (float) p[2];
            t.zoom = (float) p[3];
            Log.d(TAG, String.format("frame:%d x:%f y:%f alpha:%f zoom:%f", i, t.x, t.y, t.alpha, t.zoom));
            xforms[i] = t;
        }
        return xforms;
    }
}