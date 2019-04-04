package cn.nineton.onetake.media.videotool;

import java.nio.ByteBuffer;

public class X264Encoder {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected X264Encoder(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(X264Encoder obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VidStab_WrapperJNI.delete_X264Encoder(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public X264Encoder(String filename, int width, int height, int numframes, int fps_num, int fps_den, int crf, int maxbitrate) {
        this(VidStab_WrapperJNI.new_X264Encoder(filename, width, height, numframes, fps_num, fps_den, crf, maxbitrate), true);
    }

    public boolean start(ByteBuffer start) {
        return VidStab_WrapperJNI.X264Encoder_start(this.swigCPtr, this, start);
    }

    public boolean addFrame(long pts, ByteBuffer start) {
        return VidStab_WrapperJNI.X264Encoder_addFrame(this.swigCPtr, this, pts, start);
    }

    public boolean finish() {
        return VidStab_WrapperJNI.X264Encoder_finish(this.swigCPtr, this);
    }

    public int totalBytes() {
        return VidStab_WrapperJNI.X264Encoder_totalBytes(this.swigCPtr, this);
    }
}