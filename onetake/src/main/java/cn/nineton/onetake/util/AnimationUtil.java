package cn.nineton.onetake.util;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
//import com.blink.academy.onetake.support.callbacks.ValueAnimatorCallback;
//import com.blink.academy.onetake.support.interfaces.AnimatorEndListener;
//import com.blink.academy.onetake.support.utils.AnimationUtil$.Lambda.1;
//import com.blink.academy.onetake.support.utils.AnimationUtil$.Lambda.2;
//import com.blink.academy.onetake.ui.activity.video.VideoActivity2.AlphaAnimatorCallback;
//import com.blink.academy.onetake.widgets.RelativeLayout.FilterEffectSetRelativeLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

import cn.nineton.onetake.listener.AlphaAnimatorCallback;
import cn.nineton.onetake.listener.AnimatorEndListener;
import cn.nineton.onetake.listener.ValueAnimatorCallback;
import cn.nineton.onetake.widget.FilterEffectSetRelativeLayout;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class AnimationUtil {

    public interface AnimationCallback {
        void onAnimationEnd();
    }

    public static void filterEffectIconAlphaAnimation(final FilterEffectSetRelativeLayout view, float start, float end, long duration, final AlphaAnimatorCallback callback) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setProgressValue(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (callback != null) {
                    callback.onAnimationStart();
                }
            }
        });
        animator.start();
    }

    public static void setValueAnimator(boolean zeroStart, final ValueAnimatorCallback callback) {
        float end = 1.0f;
        float start = zeroStart ? StaticLayoutUtil.DefaultSpacingadd : 1.0f;
        if (!zeroStart) {
            end = StaticLayoutUtil.DefaultSpacingadd;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float vlaue = ((Float) animation.getAnimatedValue()).floatValue();
                if (callback != null) {
                    callback.updateCallback(vlaue);
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (callback != null) {
                    callback.endCallback();
                }
            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (callback != null) {
                    callback.startCallback();
                }
            }
        });
        animator.start();
    }

    public static void appearFromBottom(final int height, final View view, final AnimationCallback callback) {
        setValueAnimator(false, new ValueAnimatorCallback() {
            public void updateCallback(float value) {
                ViewCompat.setTranslationY(view, ((float) height) * value);
            }

            public void startCallback() {
                ViewCompat.setTranslationY(view, (float) height);
                view.setVisibility(View.VISIBLE);
            }

            public void endCallback() {
                ViewCompat.setTranslationY(view, StaticLayoutUtil.DefaultSpacingadd);
                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }
        });
    }

    public static void disappearFromBottom(final int height, final View view, final AnimationCallback callback) {
        setValueAnimator(true, new ValueAnimatorCallback() {
            public void updateCallback(float value) {
                ViewCompat.setTranslationY(view, ((float) height) * value);
            }

            public void startCallback() {
                ViewCompat.setTranslationY(view, StaticLayoutUtil.DefaultSpacingadd);
            }

            public void endCallback() {
                ViewCompat.setTranslationY(view, (float) height);
                view.setVisibility(View.GONE);
                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }
        });
    }

    public static void alphaValueAnimator(final View view, float start, float end, final long duration, final ValueAnimatorCallback callback) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
//        animator.addUpdateListener(1.lambdaFactory$(view));
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float va = (float) animation.getAnimatedValue();
                view.setAlpha(va);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (callback != null) {
                    callback.endCallback();
                }
            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (callback != null) {
                    callback.startCallback();
                }
            }
        });
        animator.start();
    }

    public static void floatValueAnimator(float start, float end, long duration, final ValueAnimatorCallback callback) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lambda$floatValueAnimator$1(callback,animation);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.endCallback();
            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                callback.startCallback();
            }
        });
        animator.start();
    }

    private static /* synthetic */ void lambda$floatValueAnimator$1(ValueAnimatorCallback callback, ValueAnimator animation) {
        if (callback != null) {
            callback.updateCallback(((Float) animation.getAnimatedValue()).floatValue());
        }
    }

    public static void alphaAnimate(View view, float alpha, int duration, AnimatorEndListener listener) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            view.setVisibility(View.VISIBLE);
        }
        ViewPropertyAnimator.animate(view).alpha(alpha).setDuration((long) duration).setInterpolator(new LinearInterpolator()).setListener(listener).start();
    }

    public static void scaleAnimate(View view, float scaleX, float scaleY, int duration, AnimatorEndListener listener) {
        ViewPropertyAnimator.animate(view).scaleX(scaleX).scaleY(scaleY).setDuration((long) duration).setInterpolator(new LinearInterpolator()).setListener(listener).start();
    }

    public static void translateAnimate(View view, float translateX, float translateY, int duration, AnimatorEndListener listener) {
        ViewPropertyAnimator.animate(view).translationX(translateX).translationY(translateY).setDuration((long) duration).setInterpolator(new LinearInterpolator()).setListener(listener).start();
    }

    public static void translateAnimateBy(View view, float translateX, float translateY, int duration, AnimatorEndListener listener) {
        ViewPropertyAnimator.animate(view).translationXBy(translateX).translationYBy(translateY).setDuration((long) duration).setInterpolator(new LinearInterpolator()).setListener(listener).start();
    }
}