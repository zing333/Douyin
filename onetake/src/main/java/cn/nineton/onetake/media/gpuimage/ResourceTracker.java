package cn.nineton.onetake.media.gpuimage;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.util.Log;
import java.util.HashMap;

public class ResourceTracker {
    private static final int MEG = 1048576;
    private static final String TAG = "ResourceTracker";
    private static HashMap<Integer, Resource> framebuffers = new HashMap();
    private static HashMap<Integer, Resource> renderbuffers = new HashMap();
    private static HashMap<Integer, Resource> textures = new HashMap();
    private static int totalCount;
    private static int totalSize;
    private static HashMap<Integer, Resource> videoDecoders = new HashMap();

    static final class Resource {
        int height;
        int id;
        int nbpp;
        StackTraceElement[] stack;
        String tag;
        int width;

        Resource() {
        }
    }

    @TargetApi(17)
    private static void assertValidContext() {
        if (EGL14.eglGetCurrentContext() == null) {
            throw new RuntimeException("NO VALID CONTEXT TO FREE RESOURCES!");
        }
    }

    public static synchronized void markTexture(int id, String tag, int width, int height, int nbpp) {
        synchronized (ResourceTracker.class) {
            assertValidContext();
            if (textures.get(Integer.valueOf(id)) != null) {
                throw new RuntimeException(String.format("texture id:%d is already used during allocation", new Object[]{Integer.valueOf(id)}));
            }
            Resource r = new Resource();
            r.tag = tag;
            r.id = id;
            r.stack = Thread.currentThread().getStackTrace();
            r.width = width;
            r.height = height;
            r.nbpp = nbpp;
            textures.put(Integer.valueOf(id), r);
            totalCount++;
            totalSize += (width * height) * nbpp;
            Log.i(TAG, String.format("added texture id:%d tag:%s size:(%dx%dx%d)", new Object[]{Integer.valueOf(r.id), r.tag, Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(nbpp)}));
            printSummary();
        }
    }

    public static synchronized void freeTexture(int id) {
        synchronized (ResourceTracker.class) {
            OpenGlUtils.checkContextActive();
            GLES20.glDeleteTextures(1, new int[]{id}, 0);
            Resource r = (Resource) textures.get(Integer.valueOf(id));
            if (r == null) {
                throw new RuntimeException(String.format("texture id:%s is not used during free", new Object[]{Integer.valueOf(id)}));
            }
            textures.remove(Integer.valueOf(id));
            totalCount--;
            totalSize -= (r.width * r.height) * r.nbpp;
            Log.i(TAG, String.format("removed texture id:%d tag:%s size:(%dx%dx%d)", new Object[]{Integer.valueOf(r.id), r.tag, Integer.valueOf(r.width), Integer.valueOf(r.height), Integer.valueOf(r.nbpp)}));
            printSummary();
        }
    }

    public static synchronized boolean isTextureAlive(int id) {
        boolean z;
        synchronized (ResourceTracker.class) {
            z = ((Resource) textures.get(Integer.valueOf(id))) != null;
        }
        return z;
    }

    public static synchronized void markRenderbuffer(int id, String tag, int width, int height, int nbpp) {
        synchronized (ResourceTracker.class) {
            assertValidContext();
            if (renderbuffers.get(Integer.valueOf(id)) != null) {
                throw new RuntimeException(String.format("renderbuffer id:%d is already used during allocation", new Object[]{Integer.valueOf(id)}));
            }
            Resource r = new Resource();
            r.tag = tag;
            r.id = id;
            r.stack = Thread.currentThread().getStackTrace();
            r.width = width;
            r.height = height;
            r.nbpp = nbpp;
            renderbuffers.put(Integer.valueOf(id), r);
            totalCount++;
            totalSize += (width * height) * nbpp;
            Log.i(TAG, String.format("added renderbuffer id:%d tag:%s size:(%dx%dx%d)", new Object[]{Integer.valueOf(r.id), r.tag, Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(nbpp)}));
            printSummary();
        }
    }

    public static synchronized void freeRenderbuffer(int id) {
        synchronized (ResourceTracker.class) {
            assertValidContext();
            Resource r = (Resource) renderbuffers.get(Integer.valueOf(id));
            if (r == null) {
                throw new RuntimeException(String.format("renderbuffer id:%d is not used during free", new Object[]{Integer.valueOf(id)}));
            }
            renderbuffers.remove(Integer.valueOf(id));
            totalCount--;
            totalSize -= (r.width * r.height) * r.nbpp;
            Log.i(TAG, String.format("removed renderbuffer id:%d tag:%s size:(%dx%dx%d)", new Object[]{Integer.valueOf(r.id), r.tag, Integer.valueOf(r.width), Integer.valueOf(r.height), Integer.valueOf(r.nbpp)}));
            printSummary();
        }
    }

    public static void printSummary() {
        Log.i(TAG, String.format("summary: ntex:%d nrbs:%d nvideos:%d numResources:%d memTotal:%d", new Object[]{Integer.valueOf(textures.size()), Integer.valueOf(renderbuffers.size()), Integer.valueOf(videoDecoders.size()), Integer.valueOf(totalCount), Integer.valueOf(totalSize / MEG)}));
    }

    public static synchronized void markFramebuffer(int id, String tag) {
        synchronized (ResourceTracker.class) {
        }
    }

    public static synchronized void freeFramebuffer(int id) {
        synchronized (ResourceTracker.class) {
        }
    }

    public static synchronized void markVideo(int id, String tag) {
        synchronized (ResourceTracker.class) {
            if (videoDecoders.get(Integer.valueOf(id)) != null) {
                throw new RuntimeException(String.format("videodecoder id:%s is already active", new Object[]{Integer.valueOf(id)}));
            }
            Resource r = new Resource();
            r.tag = tag;
            r.id = id;
            r.stack = Thread.currentThread().getStackTrace();
            videoDecoders.put(Integer.valueOf(id), r);
            totalCount++;
            Log.i(TAG, String.format("added videodecoder id:%d tag:%s", new Object[]{Integer.valueOf(r.id), r.tag}));
            printSummary();
        }
    }

    public static synchronized void freeVideo(int id) {
        synchronized (ResourceTracker.class) {
            if (((Resource) videoDecoders.get(Integer.valueOf(id))) == null) {
                throw new RuntimeException(String.format("videodecoder id:%s is not used during free", new Object[]{Integer.valueOf(id)}));
            }
            videoDecoders.remove(Integer.valueOf(id));
            totalCount--;
            printSummary();
        }
    }

    public static synchronized void dump() {
        synchronized (ResourceTracker.class) {
            dump(0, "texture", textures);
            dump(0, "renderbuffer", renderbuffers);
            dump(0, "videodecoder", videoDecoders);
        }
    }

    public static synchronized void dumpFull() {
        synchronized (ResourceTracker.class) {
            dump(1, "texture", textures);
            dump(1, "renderbuffer", renderbuffers);
            dump(1, "videodecoder", videoDecoders);
        }
    }

    private static void dump(int verbosity, String type, HashMap<Integer, Resource> resource) {
        Log.d(TAG, String.format("----------------- %s ----------------", new Object[]{type}));
        for (Resource r : resource.values()) {
            if (verbosity >= 1) {
                for (StackTraceElement ste : r.stack) {
                    Log.d(TAG, "    " + ste.toString());
                }
            }
        }
    }
}