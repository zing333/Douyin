package cn.nineton.onetake.media.videotool;

public class ResamplerCubic {
    int[] lp = new int[4];
    long mInToOutRatio;
    long mInputBaseRate;
    int mInputRate;
    long mNumOutput = 0;
    long mOutputBaseRate;
    int mOutputRate;
    long mPos;
    long mTimeBase;
    int[] rp = new int[4];

    public static int fixeddiv(long a, long b) {
        return (int) ((65536 * a) / b);
    }

    public static int fixedmul(int a, int b) {
        return (a * b) >> 16;
    }

    public static float getValue(int[] p, int x) {
        return (float) (p[1] + (fixedmul(x, (p[2] - p[0]) + fixedmul(x, ((((p[0] * 2) - (p[1] * 5)) + (p[2] * 4)) - p[3]) + fixedmul(x, (((p[1] - p[2]) * 3) + p[3]) - p[0]))) >> 1));
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long lcm(long a, long b) {
        return (b / gcd(a, b)) * a;
    }

    public int[] getLp() {
        return lp;
    }

    public void setLp(int[] lp) {
        this.lp = lp;
    }

    public long getmInToOutRatio() {
        return mInToOutRatio;
    }

    public void setmInToOutRatio(long mInToOutRatio) {
        this.mInToOutRatio = mInToOutRatio;
    }

    public long getmInputBaseRate() {
        return mInputBaseRate;
    }

    public void setmInputBaseRate(long mInputBaseRate) {
        this.mInputBaseRate = mInputBaseRate;
    }

    public int getmInputRate() {
        return mInputRate;
    }

    public void setmInputRate(int mInputRate) {
        this.mInputRate = mInputRate;
    }

    public long getmNumOutput() {
        return mNumOutput;
    }

    public void setmNumOutput(long mNumOutput) {
        this.mNumOutput = mNumOutput;
    }

    public long getmOutputBaseRate() {
        return mOutputBaseRate;
    }

    public void setmOutputBaseRate(long mOutputBaseRate) {
        this.mOutputBaseRate = mOutputBaseRate;
    }

    public int getmOutputRate() {
        return mOutputRate;
    }

    public void setmOutputRate(int mOutputRate) {
        this.mOutputRate = mOutputRate;
    }

    public long getmPos() {
        return mPos;
    }

    public void setmPos(long mPos) {
        this.mPos = mPos;
    }

    public long getmTimeBase() {
        return mTimeBase;
    }

    public void setmTimeBase(long mTimeBase) {
        this.mTimeBase = mTimeBase;
    }

    public int[] getRp() {
        return rp;
    }

    public void setRp(int[] rp) {
        this.rp = rp;
    }

    public void configure(int outputRate, int inputRate) {
        this.mOutputRate = outputRate;
        this.mInputRate = inputRate;
        this.mTimeBase = lcm((long) outputRate, (long) inputRate);
        this.mInputBaseRate = this.mTimeBase / ((long) this.mInputRate);
        this.mOutputBaseRate = this.mTimeBase / ((long) this.mOutputRate);
        this.mInToOutRatio = (this.mTimeBase / ((long) this.mOutputRate)) * ((long) this.mInputRate);
        this.mNumOutput = 0;
    }

    int getOutputSize(int inputSize) {
        return (int) ((((long) inputSize) * this.mOutputBaseRate) / ((long) this.mInputRate));
    }

    public int resampleMono(short[] out, short[] in, int inLength) {
        int outputIndex = 0;
        int inputIndex = 0;
        while (inputIndex < inLength) {
            int outputIndex2;
            if (this.mPos < this.mTimeBase) {
                int frac = fixeddiv(this.mPos, this.mTimeBase);
                if (this.mNumOutput >= 3) {
                    short val = (short) ((int) getValue(this.lp, frac));
                    outputIndex2 = outputIndex + 1;
                    out[outputIndex] = val;
                    outputIndex = outputIndex2 + 1;
                    out[outputIndex2] = val;
                }
                outputIndex2 = outputIndex;
                this.mPos += this.mInToOutRatio;
                this.mNumOutput++;
            } else {
                outputIndex2 = outputIndex;
            }
            if (this.mPos >= this.mTimeBase) {
                this.lp[0] = this.lp[1];
                this.lp[1] = this.lp[2];
                this.lp[2] = this.lp[3];
                int inputIndex2 = inputIndex + 1;
                this.lp[3] = in[inputIndex];
                this.mPos -= this.mTimeBase;
                outputIndex = outputIndex2;
                inputIndex = inputIndex2;
            } else {
                outputIndex = outputIndex2;
            }
        }
        return outputIndex;
    }

    public int resampleStereo(short[] out, short[] in, int inLength) {
        if (this.mInputRate == this.mOutputRate) {
            for (int i = 0; i < inLength; i++) {
                out[i] = in[i];
            }
            return inLength;
        }
        int outputIndex = 0;
        int inputIndex = 0;
        while (inputIndex < inLength) {
            int i2;
            if (this.mPos < this.mTimeBase) {
                int frac = fixeddiv(this.mPos, this.mTimeBase);
                if (this.mNumOutput >= 3) {
                    i2 = outputIndex + 1;
                    out[outputIndex] = (short) ((int) getValue(this.lp, frac));
                    outputIndex = i2 + 1;
                    out[i2] = (short) ((int) getValue(this.rp, frac));
                }
                i2 = outputIndex;
                this.mPos += this.mInToOutRatio;
                this.mNumOutput++;
            } else {
                i2 = outputIndex;
            }
            if (this.mPos >= this.mTimeBase) {
                this.lp[0] = this.lp[1];
                this.lp[1] = this.lp[2];
                this.lp[2] = this.lp[3];
                int inputIndex2 = inputIndex + 1;
                this.lp[3] = in[inputIndex];
                this.rp[0] = this.rp[1];
                this.rp[1] = this.rp[2];
                this.rp[2] = this.rp[3];
                inputIndex = inputIndex2 + 1;
                this.rp[3] = in[inputIndex2];
                this.mPos -= this.mTimeBase;
                outputIndex = i2;
            } else {
                outputIndex = i2;
            }
        }
        return outputIndex;
    }

}