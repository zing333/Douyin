package cn.nineton.onetake.media.gpuimage;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class GPUImageFilter {
    public static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     highp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = color;\n}";
    public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
    private static final String TAG = "GPUImageFilter";
    static long lastFinish = 0;
    private static AtomicInteger mNextUniformLocation = new AtomicInteger(1000);
    protected float isBnW;
    private final String mFragmentShader;
    protected int mGLAttribPosition;
    protected int mGLAttribTextureCoordinate;
    protected int mGLProgId;
    protected int mGLUniformTexture;
    private volatile boolean mIsInitialized;
    protected int mOutputHeight;
    protected int mOutputWidth;
    private HashMap<Integer, Integer> mPhysicalUniforms;
    protected final LinkedList<Runnable> mRunOnDraw;
    private final String mVertexShader;
    private BidirectionalMap<String, Integer> mVirtualUniforms;

    private static final class BidirectionalMap<KeyType, ValueType> {
        private HashMap<KeyType, ValueType> keyToValueMap;
        private HashMap<ValueType, KeyType> valueToKeyMap;

        private BidirectionalMap() {
            this.keyToValueMap = new HashMap();
            this.valueToKeyMap = new HashMap();
        }

//        /* synthetic */ BidirectionalMap(AnonymousClass1 x0) {
//            this();
//        }

        public synchronized void put(KeyType key, ValueType value) {
            this.keyToValueMap.put(key, value);
            this.valueToKeyMap.put(value, key);
        }

        public synchronized ValueType removeByKey(KeyType key) {
            ValueType removedValue;
            removedValue = this.keyToValueMap.remove(key);
            this.valueToKeyMap.remove(removedValue);
            return removedValue;
        }

        public synchronized KeyType removeByValue(ValueType value) {
            KeyType removedKey;
            removedKey = this.valueToKeyMap.remove(value);
            this.keyToValueMap.remove(removedKey);
            return removedKey;
        }

        public boolean containsKey(KeyType key) {
            return this.keyToValueMap.containsKey(key);
        }

        public boolean containsValue(ValueType value) {
            return this.keyToValueMap.containsValue(value);
        }

        public KeyType getKey(ValueType value) {
            return this.valueToKeyMap.get(value);
        }

        public ValueType get(KeyType key) {
            return this.keyToValueMap.get(key);
        }
    }

    public void setIsBnW(float mIsBnW) {
    }

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(String vertexShader, String fragmentShader) {
        this.mGLUniformTexture = -1;
        this.isBnW = 0.0f;
        this.mVirtualUniforms = new BidirectionalMap();
        this.mPhysicalUniforms = new HashMap();
        this.mRunOnDraw = new LinkedList();
        this.mVertexShader = vertexShader;
        this.mFragmentShader = fragmentShader;
        this.mGLUniformTexture = getUniformLocation("inputImageTexture");
    }

    public final void init() {
        onInit();
        this.mIsInitialized = true;
        onInitialized();
    }

    public void onInit() {
        if (this.mIsInitialized) {
            Log.e("GPUImagefilter", "already initialized");
            return;
        }
        this.mGLProgId = OpenGlUtils.loadProgram(this.mVertexShader, this.mFragmentShader);
        this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
        this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
        this.mIsInitialized = true;
        Log.d(TAG, String.format("allocated program:%d", new Object[]{Integer.valueOf(this.mGLProgId)}));
    }

    public void onInitialized() {
    }

    public final void destroy() {
        this.mIsInitialized = false;
        if (this.mGLProgId != 0) {
            Log.d(TAG, String.format("destroyed program:%d", new Object[]{Integer.valueOf(this.mGLProgId)}));
            OpenGlUtils.unloadProgram(this.mGLProgId);
            this.mGLProgId = 0;
        }
        onDestroy();
    }

    public void onDestroy() {
    }

    public void destroySecondary() {
    }

    public void onOutputSizeChanged(int width, int height) {
        this.mOutputWidth = width;
        this.mOutputHeight = height;
    }

    public final void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        int[] currentFBO = new int[1];
        GLES20.glGetIntegerv(36006, currentFBO, 0);
        Framebuffer target = Framebuffer.createTarget(currentFBO[0]);
        Framebuffer source = Framebuffer.createSource(textureId, 0, 0);
        FramebufferCache cache = FramebufferCache.shared;
        cache.addExternal(target);
        cache.addExternal(source);
        try {
            onDraw2(target, source, cubeBuffer, textureBuffer);
        } catch (RenderException e) {
            Log.e(TAG, "RenderException", e);
        }
        if (cache.isUsed(source)) {
            Log.w(TAG, "source still in use!");
            cache.offerFramebuffer(source);
        }
        if (!cache.isUsed(target)) {
            Log.w(TAG, "target has been freed!");
        }
        cache.offerFramebuffer(target);
    }

    public Framebuffer onDraw2(Framebuffer target, Framebuffer source, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        if (this.mIsInitialized) {
            if (target == null) {
                target = FramebufferCache.shared.takeFramebuffer(this.mOutputWidth, this.mOutputHeight, 6408);
            }
            GLES20.glBindFramebuffer(36160, target.fboid());
            GLES20.glUseProgram(this.mGLProgId);
            runPendingOnDrawTasks();
            cubeBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            textureBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, textureBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
            if (source.texid() != -1) {
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, source.texid());
                GLES20.glUniform1i(getPhysUniformLocation(this.mGLUniformTexture), 0);
            }
            onDrawArraysPre();
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
            if (this.mGLAttribTextureCoordinate != -1) {
                GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
            }
            GLES20.glBindTexture(3553, 0);
            FramebufferCache.shared.offerFramebuffer(source);
            long now = System.nanoTime();
            if (lastFinish == 0) {
                lastFinish = now;
            }
            if (now - lastFinish > 8000000) {
                lastFinish = now;
                GLES20.glFinish();
                Thread.yield();
            }
            return target;
        }
        Log.e(TAG, "rendering filter without initialization!");
        return source;
    }

    protected void onDrawArraysPre() {
    }

    protected void runPendingOnDrawTasks() {
        while (!this.mRunOnDraw.isEmpty()) {
            ((Runnable) this.mRunOnDraw.removeFirst()).run();
        }
    }

    public synchronized boolean isInitialized() {
        return this.mIsInitialized;
    }

    public void setIntensity(float intensity) {
    }

    public int getOutputWidth() {
        return this.mOutputWidth;
    }

    public int getOutputHeight() {
        return this.mOutputHeight;
    }

    public int getProgram() {
        return this.mGLProgId;
    }

    public int getAttribPosition() {
        return this.mGLAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return this.mGLAttribTextureCoordinate;
    }

    public int getUniformLocation(String name) {
        Integer vloc = (Integer) this.mVirtualUniforms.get(name);
        if (vloc == null) {
            vloc = new Integer(mNextUniformLocation.getAndIncrement());
            this.mVirtualUniforms.put(name, vloc);
        }
        return vloc.intValue();
    }

    public int getPhysUniformLocation(int vloc) {
        Integer ploc = (Integer) this.mPhysicalUniforms.get(Integer.valueOf(vloc));
        if (ploc == null) {
            String name = (String) this.mVirtualUniforms.getKey(Integer.valueOf(vloc));
            if (name == null) {
                throw new RuntimeException("vloc was never retrieved");
            }
            ploc = new Integer(GLES20.glGetUniformLocation(this.mGLProgId, name));
            if (ploc.intValue() == -1) {
                Log.e(TAG, "missing uniform");
            }
            this.mPhysicalUniforms.put(Integer.valueOf(vloc), ploc);
        }
        return ploc.intValue();
    }

    public int getUniformTexture() {
        return this.mGLUniformTexture;
    }

    private void assertLocation(int vloc) {
        if (vloc < 1000) {
            throw new RuntimeException("invalid VLOC");
        }
    }

    protected void setInteger(final int location, final int intValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1i(GPUImageFilter.this.getPhysUniformLocation(location), intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1f(GPUImageFilter.this.getPhysUniformLocation(location), floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform3fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform4fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1fv(GPUImageFilter.this.getPhysUniformLocation(location), arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setPoint(final int location, final PointF point) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, new float[]{point.x, point.y}, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix3fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        assertLocation(location);
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix4fv(GPUImageFilter.this.getPhysUniformLocation(location), 1, false, matrix, 0);
            }
        });
    }

    protected void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }

    public static String loadShader(String file, Context context) {
        try {
            InputStream ims = context.getAssets().open(file);
            String re = convertStreamToString(ims);
            ims.close();
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void onDrawAtOneFrame(int frameIndex) {
    }
}