package cn.nineton.onetake.media.videotool;

import java.nio.ByteBuffer;

public class VidStabAnalyzer {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VidStabAnalyzer(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidStabAnalyzer obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VidStab_WrapperJNI.delete_VidStabAnalyzer(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public boolean initialize(int width, int height) {
        return VidStab_WrapperJNI.VidStabAnalyzer_initialize(this.swigCPtr, this, width, height);
    }

    public void addFramebuffer(int frameIndex, ByteBuffer start) {
        VidStab_WrapperJNI.VidStabAnalyzer_addFramebuffer(this.swigCPtr, this, frameIndex, start);
    }

    public void destroy() {
        VidStab_WrapperJNI.VidStabAnalyzer_destroy(this.swigCPtr, this);
    }

    public VidStabAnalyzer() {
        this(VidStab_WrapperJNI.new_VidStabAnalyzer(), true);
    }
}