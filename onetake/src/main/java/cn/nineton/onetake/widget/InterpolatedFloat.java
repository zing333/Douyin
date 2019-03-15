package cn.nineton.onetake.widget;

import java.util.ArrayList;
import java.util.Iterator;

public class InterpolatedFloat {
    ArrayList<KeyFrame> mKeyFrames = new ArrayList();

    enum Ease {
        Linear,
        QuadIn,
        QuadOut,
        QuadInOut
    }

    static final class KeyFrame {
        Ease ease;
        long time;
        float value;

        KeyFrame(long _time, float _value, Ease _ease) {
            this.time = _time;
            this.value = _value;
            this.ease = _ease;
        }

        public KeyFrame clone() {
            return new KeyFrame(this.time, this.value, this.ease);
        }
    }

    static double easeInQuad(double t, double b, double c, double d) {
        t /= d;
        return ((c * t) * t) + b;
    }

    static double easeOutQuad(double t, double b, double c, double d) {
        t /= d;
        return (((-c) * t) * (t - 2.0d)) + b;
    }

    static double easeInOutQuad(double t, double b, double c, double d) {
        t /= d / 2.0d;
        if (t < 1.0d) {
            return (((c / 2.0d) * t) * t) + b;
        }
        t -= 1.0d;
        return (((-c) / 2.0d) * (((t - 2.0d) * t) - 1.0d)) + b;
    }

    static double CubicInterpolate(double y0, double y1, double y2, double y3, double mu) {
        double mu2 = mu * mu;
        double a0 = ((y3 - y2) - y0) + y1;
        return ((((a0 * mu) * mu2) + (((y0 - y1) - a0) * mu2)) + ((y2 - y0) * mu)) + y1;
    }

    public static InterpolatedFloat createLinear(long t0, float v0, long t1, float v1) {
        InterpolatedFloat value = new InterpolatedFloat();
        value.addKeyFrame(t0, v0, Ease.Linear);
        value.addKeyFrame(t1, v1, Ease.Linear);
        return value;
    }

    static InterpolatedFloat create(long t0, float v0, long t1, float v1, Ease ease) {
        InterpolatedFloat value = new InterpolatedFloat();
        value.addKeyFrame(t0, v0, ease);
        value.addKeyFrame(t1, v1, ease);
        return value;
    }

    InterpolatedFloat() {
    }

    InterpolatedFloat(ArrayList<KeyFrame> frames) {
        Iterator it = frames.iterator();
        while (it.hasNext()) {
            this.mKeyFrames.add(((KeyFrame) it.next()).clone());
        }
    }

    public InterpolatedFloat clone() {
        return new InterpolatedFloat(this.mKeyFrames);
    }

    KeyFrame addKeyFrame(long time, float value, Ease ease) {
        KeyFrame kf = new KeyFrame(time, value, ease);
        this.mKeyFrames.add(kf);
        return kf;
    }

    int findKeyframe(long time) {
        for (int i = 0; i < this.mKeyFrames.size(); i++) {
            if (time < ((KeyFrame) this.mKeyFrames.get(i)).time) {
                return i - 1;
            }
        }
        return this.mKeyFrames.size() - 1;
    }

    boolean hasEnded(long time) {
        if (this.mKeyFrames.size() == 0) {
            return true;
        }
        return time > ((KeyFrame) this.mKeyFrames.get(this.mKeyFrames.size() + -1)).time;
    }

    float getValue(long time) {
        if (this.mKeyFrames.size() == 0) {
            return Float.NaN;
        }
        int index = findKeyframe(time);
        int last = this.mKeyFrames.size() - 1;
        if (index < 0) {
            return ((KeyFrame) this.mKeyFrames.get(0)).value;
        }
        if (index == last) {
            return ((KeyFrame) this.mKeyFrames.get(last)).value;
        }
        KeyFrame kf0 = (KeyFrame) this.mKeyFrames.get(index);
        KeyFrame kf1 = (KeyFrame) this.mKeyFrames.get(index + 1);
        double t = ((double) (time - kf0.time)) / ((double) (kf1.time - kf0.time));
        switch (kf0.ease) {
            case QuadIn:
                t = easeInQuad(t, 0.0d, 1.0d, 1.0d);
                break;
            case QuadOut:
                t = easeOutQuad(t, 0.0d, 1.0d, 1.0d);
                break;
            case QuadInOut:
                t = easeInOutQuad(t, 0.0d, 1.0d, 1.0d);
                break;
        }
        return (float) ((((double) kf1.value) * t) + (((double) kf0.value) * (1.0d - t)));
    }
}