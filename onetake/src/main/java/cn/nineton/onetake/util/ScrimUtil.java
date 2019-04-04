package cn.nineton.onetake.util;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.shapes.RectShape;
import android.util.LruCache;

import cn.nineton.onetake.widget.StaticLayoutUtil;

public class ScrimUtil {
    private static final LruCache<Integer, Drawable> cubicGradientScrimCache = new LruCache(10);

    private ScrimUtil() {
    }

    public static Drawable makeCubicGradientScrimDrawable(int baseColor, int numStops, int gravity) {
        int cacheKeyHash = (((baseColor * 31) + numStops) * 31) + gravity;
        Drawable cachedGradient = (Drawable) cubicGradientScrimCache.get(Integer.valueOf(cacheKeyHash));
        if (cachedGradient != null) {
            return cachedGradient;
        }
        float x0;
        float x1;
        float y0;
        float y1;
        numStops = Math.max(numStops, 2);
        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());
        final int[] stopColors = new int[numStops];
        int red = Color.red(baseColor);
        int green = Color.green(baseColor);
        int blue = Color.blue(baseColor);
        int alpha = Color.alpha(baseColor);
        for (int i = 0; i < numStops; i++) {
            float f = (float) alpha;
            stopColors[i] = Color.argb((int) (f * constrain(StaticLayoutUtil.DefaultSpacingadd, 1.0f, (float) Math.pow((double) ((((float) i) * 1.0f) / ((float) (numStops - 1))), 3.0d))), red, green, blue);
        }
        switch (gravity & 7) {
            case 3:
                x0 = 1.0f;
                x1 = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case 5:
                x0 = StaticLayoutUtil.DefaultSpacingadd;
                x1 = 1.0f;
                break;
            default:
                x0 = StaticLayoutUtil.DefaultSpacingadd;
                x1 = StaticLayoutUtil.DefaultSpacingadd;
                break;
        }
        switch (gravity & 112) {
            case 48:
                y0 = 1.0f;
                y1 = StaticLayoutUtil.DefaultSpacingadd;
                break;
            case 80:
                y0 = StaticLayoutUtil.DefaultSpacingadd;
                y1 = 1.0f;
                break;
            default:
                y0 = StaticLayoutUtil.DefaultSpacingadd;
                y1 = StaticLayoutUtil.DefaultSpacingadd;
                break;
        }
        paintDrawable.setShaderFactory(new ShaderFactory() {
            public Shader resize(int width, int height) {
                return new LinearGradient(((float) width) * x0, ((float) height) * y0, ((float) width) * x1, ((float) height) * y1, stopColors, null, TileMode.CLAMP);
            }
        });
        cubicGradientScrimCache.put(Integer.valueOf(cacheKeyHash), paintDrawable);
        return paintDrawable;
    }

    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }
}