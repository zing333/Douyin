package cn.nineton.onetake.media.gpuimage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera.Size;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import cn.nineton.onetake.util.BinTools;

public class OpenGlUtils {
    public static final int NO_TEXTURE = -1;
    private static final String TAG = "OpenGlUtils";
    static HashMap<String, Integer> fshaders = new HashMap();
    protected static final char[] hexArray = BinTools.hex.toCharArray();
    static HashMap<String, CachedProgram> programs = new HashMap();
    static HashMap<Integer, CachedProgram> revprograms = new HashMap();
    static HashMap<String, Integer> vshaders = new HashMap();

    static final class CachedProgram {
        boolean mIsUsed;
        CachedProgram mNext;
        int mProgId;

        CachedProgram() {
        }
    }

    public static int loadTexture(Bitmap img, int usedTexId) {
        return loadTexture(img, usedTexId, true);
    }

    public static int loadTexture(Bitmap img, int usedTexId, boolean recycle) {
        int[] textures = new int[1];
        if (usedTexId == -1) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, textures[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9987.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLUtils.texImage2D(3553, 0, img, 0);
            GLES20.glGenerateMipmap(3553);
            Log.d(TAG, String.format("loaded texture:%d (bitmap)", new Object[]{Integer.valueOf(textures[0])}));
            ResourceTracker.markTexture(textures[0], "bitmap", img.getWidth(), img.getHeight(), 4);
        } else {
            GLES20.glBindTexture(3553, usedTexId);
            GLUtils.texSubImage2D(3553, 0, 0, 0, img);
            textures[0] = usedTexId;
        }
        if (recycle) {
            img.recycle();
        }
        return textures[0];
    }

    public static int loadTexture(IntBuffer data, int width, int height, int usedTexId) {
        int[] textures = new int[1];
        if (usedTexId == -1) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(3553, textures[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, data);
            Log.d(TAG, String.format("loaded texture:%d (intbuffer)", new Object[]{Integer.valueOf(textures[0])}));
            ResourceTracker.markTexture(textures[0], "intbuffer", width, height, 4);
        } else {
            GLES20.glBindTexture(3553, usedTexId);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, width, height, 6408, 5121, data);
            textures[0] = usedTexId;
        }
        return textures[0];
    }

    public static int loadTextureAsBitmap(IntBuffer data, Size size, int usedTexId) {
        return loadTexture(Bitmap.createBitmap(data.array(), size.width, size.height, Config.ARGB_8888), usedTexId);
    }

    private static byte[] toBytes(ByteBuffer sum) {
        sum.rewind();
        byte[] array = new byte[sum.remaining()];
        sum.get(array, 0, sum.remaining());
        return array;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static String md5sum(byte[] input) {
        try {
            return bytesToHex(MessageDigest.getInstance("md5").digest(input));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5sum(String input) {
        try {
            return md5sum(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized int loadProgram(String strVSource, String strFSource) {
        int iProgId;
        synchronized (OpenGlUtils.class) {
            String str;
            CachedProgram cp;
            int iVShader;
            int iFShader;
            boolean progEnabled = true;
            boolean shaderEnabled = true;
            if (GLES20.glGetString(7937).startsWith("Adreno")) {
                progEnabled = false;
                shaderEnabled = false;
            }
            String str2 = TAG;
            String str3 = "program cache:%s  shader cache:%s";
            Object[] objArr = new Object[2];
            objArr[0] = progEnabled ? "enabled" : "disabled";
            if (shaderEnabled) {
                str = "enabled";
            } else {
                str = "disabled";
            }
            objArr[1] = str;
            Log.d(str2, String.format(str3, objArr));
            String vsDigest = md5sum(strVSource);
            String fsDigest = md5sum(strFSource);
            String progDigest = vsDigest + "_" + fsDigest;
            Log.d(TAG, String.format("program digest:%s", new Object[]{progDigest}));
            if (progEnabled && programs.containsKey(progDigest)) {
                for (cp = (CachedProgram) programs.get(progDigest); cp != null; cp = cp.mNext) {
                    if (!cp.mIsUsed) {
                        Log.d(TAG, String.format("found program:%d with digest:%s", new Object[]{Integer.valueOf(cp.mProgId), progDigest}));
                        cp.mIsUsed = true;
                        iProgId = cp.mProgId;
                        break;
                    }
                }
                Log.d(TAG, String.format("found one or more cached programs but used, digest:%s", new Object[]{progDigest}));
            }
            if (shaderEnabled && vshaders.containsKey(vsDigest)) {
                iVShader = ((Integer) vshaders.get(vsDigest)).intValue();
                Log.d(TAG, String.format("found vshader:%d for digest:%s", new Object[]{Integer.valueOf(iVShader), vsDigest}));
            } else {
                iVShader = loadShader(strVSource, 35633);
                if (iVShader == 0) {
                    Log.e("Load Program", "Vertex Shader Failed");
                    iProgId = 0;
                } else if (shaderEnabled) {
                    vshaders.put(vsDigest, Integer.valueOf(iVShader));
                    Log.d(TAG, String.format("put vshader:%d for digest:%s", new Object[]{Integer.valueOf(iVShader), vsDigest}));
                }
            }
            if (shaderEnabled && fshaders.containsKey(fsDigest)) {
                iFShader = ((Integer) fshaders.get(fsDigest)).intValue();
                Log.d(TAG, String.format("found fshader:%d for digest:%s", new Object[]{Integer.valueOf(iFShader), fsDigest}));
            } else {
                iFShader = loadShader(strFSource, 35632);
                if (iFShader == 0) {
                    Log.e("Load Program", "Fragment Shader Failed");
                    iProgId = 0;
                } else if (shaderEnabled) {
                    fshaders.put(fsDigest, Integer.valueOf(iFShader));
                    Log.d(TAG, String.format("put fshader:%d for digest:%s", new Object[]{Integer.valueOf(iFShader), fsDigest}));
                }
            }
            int[] link = new int[1];
            iProgId = GLES20.glCreateProgram();
            GLES20.glAttachShader(iProgId, iVShader);
            GLES20.glAttachShader(iProgId, iFShader);
            GLES20.glLinkProgram(iProgId);
            GLES20.glGetProgramiv(iProgId, 35714, link, 0);
            if (link[0] <= 0) {
                Log.e(TAG, "Linking Failed: " + GLES20.glGetProgramInfoLog(iProgId));
                iProgId = 0;
            } else if (progEnabled) {
                cp = new CachedProgram();
                cp.mProgId = iProgId;
                cp.mIsUsed = true;
                cp.mNext = (CachedProgram) programs.get(progDigest);
                programs.put(progDigest, cp);
                revprograms.put(Integer.valueOf(iProgId), cp);
                Log.d(TAG, String.format("put program:%d with digest:%s, total_used:%d total_free:%d", new Object[]{Integer.valueOf(iProgId), progDigest, Integer.valueOf(getTotalUsed()), Integer.valueOf(getTotalFree())}));
            } else {
                Log.d(TAG, String.format("create uncached program:%d", new Object[]{Integer.valueOf(iProgId)}));
            }
        }
        return iProgId;
    }

    public static int loadShader(String strSource, int iType) {
        int[] compiled = new int[1];
        int iShader = GLES20.glCreateShader(iType);
        GLES20.glShaderSource(iShader, strSource);
        GLES20.glCompileShader(iShader);
        GLES20.glGetShaderiv(iShader, 35713, compiled, 0);
        if (compiled[0] != 0) {
            return iShader;
        }
        Log.e(TAG, "Compilation failed:\n" + GLES20.glGetShaderInfoLog(iShader));
        Log.e(TAG, "foo");
        return 0;
    }

    public static synchronized void unloadProgram(int progid) {
        synchronized (OpenGlUtils.class) {
            boolean progEnabled = true;
            if (GLES20.glGetString(7937).startsWith("Adreno")) {
                progEnabled = false;
            }
            if (progEnabled) {
                Log.d(TAG, String.format("marking program:%d as unused, total_used:%d total_free:%d", new Object[]{Integer.valueOf(progid), Integer.valueOf(getTotalUsed()), Integer.valueOf(getTotalFree())}));
                CachedProgram cp = (CachedProgram) revprograms.get(Integer.valueOf(progid));
                if (cp == null) {
                    Log.d(TAG, "foo");
                } else {
                    cp.mIsUsed = false;
                }
            } else {
                GLES20.glDeleteProgram(progid);
                Log.d(TAG, String.format("deleted uncached program:%d", new Object[]{Integer.valueOf(progid)}));
            }
        }
    }

    static void printUsed() {
        for (CachedProgram cp : programs.values()) {
            for (CachedProgram cp2 = (CachedProgram) r1.next(); cp2 != null; cp2 = cp2.mNext) {
                if (cp2.mIsUsed) {
                    Log.d(TAG, String.format("inuse program, id:%d", new Object[]{Integer.valueOf(cp2.mProgId)}));
                }
            }
        }
    }

    static int getTotalUsed() {
        int count = 0;
        for (CachedProgram cp : programs.values()) {
            for (CachedProgram cp2 = (CachedProgram) r2.next(); cp2 != null; cp2 = cp2.mNext) {
                if (cp2.mIsUsed) {
                    count++;
                }
            }
        }
        return count;
    }

    static int getTotalFree() {
        int count = 0;
        for (CachedProgram cp : programs.values()) {
            for (CachedProgram cp2 = (CachedProgram) r2.next(); cp2 != null; cp2 = cp2.mNext) {
                if (!cp2.mIsUsed) {
                    count++;
                }
            }
        }
        return count;
    }

    public static float rnd(float min, float max) {
        return ((max - min) * ((float) Math.random())) + min;
    }

    public static void checkGLError(String msg) {
        boolean failed = false;
        while (true) {
            int error = GLES20.glGetError();
            if (error == 0) {
                break;
            }
            Log.e(TAG, msg + ": GL error: 0x" + Integer.toHexString(error));
            failed = true;
        }
        if (failed) {
            throw new RuntimeException("GL error encountered (see log)");
        }
    }

    public static void clearGLError(String msg) {
        while (true) {
            int error = GLES20.glGetError();
            if (error != 0) {
                Log.e(TAG, msg + ": GL error: 0x" + Integer.toHexString(error));
            } else {
                return;
            }
        }
    }

    public static void checkContextActive() {
        if (EGL14.eglGetCurrentContext() == null) {
            throw new RuntimeException("no egl context");
        } else if (EGL14.eglGetCurrentContext().equals(EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("no egl context");
        }
    }

    public static long contextHandle(EGLContext context) {
        if (VERSION.SDK_INT >= 21) {
            return context.getNativeHandle();
        }
        return 0;
    }

    public static long currentContextHandle() {
        return contextHandle(EGL14.eglGetCurrentContext());
    }

    public static int currentFramebuffer() {
        int[] currentFBO = new int[1];
        GLES20.glGetIntegerv(36006, currentFBO, 0);
        return currentFBO[0];
    }
}