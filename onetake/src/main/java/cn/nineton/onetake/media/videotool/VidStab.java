package cn.nineton.onetake.media.videotool;

public class VidStab {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected VidStab(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(VidStab obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VidStab_WrapperJNI.delete_VidStab(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public VidStab() {
        this(VidStab_WrapperJNI.new_VidStab(), true);
    }

    public boolean initialize(int width, int height, int numFrames) {
        return VidStab_WrapperJNI.VidStab_initialize(this.swigCPtr, this, width, height, numFrames);
    }

    public void setFrameAnalysis(int frameIndex, VidStabAnalyzer analysis) {
        VidStab_WrapperJNI.VidStab_setFrameAnalysis(this.swigCPtr, this, frameIndex, VidStabAnalyzer.getCPtr(analysis), analysis);
    }

    public void createTransforms(int nFrames, int[] frames, boolean looped) {
        VidStab_WrapperJNI.VidStab_createTransforms(this.swigCPtr, this, nFrames, frames, looped);
    }

    public int getNumReadyFrames() {
        return VidStab_WrapperJNI.VidStab_getNumReadyFrames(this.swigCPtr, this);
    }

    public void getTransform(int frameIndex, double[] x) {
        VidStab_WrapperJNI.VidStab_getTransform(this.swigCPtr, this, frameIndex, x);
    }
}