package cn.nineton.onetake.media.videotool;

import java.nio.ByteBuffer;

public class VidStabLuminance {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VidStabLuminance(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidStabLuminance obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VidStab_WrapperJNI.delete_VidStabLuminance(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setSource(ByteBuffer start) {
        VidStab_WrapperJNI.VidStabLuminance_setSource(this.swigCPtr, this, start);
    }

    public void setTarget(ByteBuffer start) {
        VidStab_WrapperJNI.VidStabLuminance_setTarget(this.swigCPtr, this, start);
    }

    public void convert(int n) {
        VidStab_WrapperJNI.VidStabLuminance_convert(this.swigCPtr, this, n);
    }

    public VidStabLuminance() {
        this(VidStab_WrapperJNI.new_VidStabLuminance(), true);
    }
}