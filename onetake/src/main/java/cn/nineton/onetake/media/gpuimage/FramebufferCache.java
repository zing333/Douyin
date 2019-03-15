package cn.nineton.onetake.media.gpuimage;

import android.util.Log;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.mp4parser.boxes.iso14496.part12.FreeBox;

public class FramebufferCache {
    static final String TAG = "FramebufferCache";
    static final boolean VERBOSE = false;
    public static FramebufferCache shared = new FramebufferCache();
    long mExpiryNanos = 1000000000;
    Queue<FramebufferItem> mFreePool = new LinkedList();
    long mMaxPressure = 838860800;
    Queue<FramebufferItem> mUsedPool = new LinkedList();

    static final class FramebufferItem {
        long boundContext;
        Framebuffer boundFramebuffer;
        long heartbeatNanos;
        boolean isExternal;
        int lockCount;
        FramebufferTexture storage;

        FramebufferItem(FramebufferTexture fb, boolean isExternal) {
            this.storage = fb;
            this.isExternal = isExternal;
        }

        void updateHeartbeat() {
            this.heartbeatNanos = System.nanoTime();
        }
    }

    private FramebufferCache() {
    }

    public synchronized void clear() {
    }

    public void dump() {
        dumpList("used", this.mUsedPool);
        dumpList(org.mp4parser.boxes.iso14496.part12.FreeBox.TYPE, this.mFreePool);
    }

    private void dumpList(String tag, Queue<FramebufferItem> list) {
        for (FramebufferItem item : list) {
            Log.d(TAG, String.format("%s item:%dx%d context:%d", new Object[]{tag, Integer.valueOf(item.storage.width), Integer.valueOf(item.storage.height)}));
        }
    }

    public synchronized void addExternal(Framebuffer fb) {
        long context = OpenGlUtils.currentContextHandle();
        FramebufferItem item = new FramebufferItem(null, true);
        item.boundFramebuffer = fb;
        item.boundContext = context;
        this.mUsedPool.add(item);
    }

    public synchronized boolean isUsed(Framebuffer fb) {
        boolean z;
        for (FramebufferItem item : this.mUsedPool) {
            if (item.boundFramebuffer == fb) {
                z = true;
                break;
            }
        }
        z = false;
        return z;
    }

    public synchronized void lock(Framebuffer fb) {
        for (FramebufferItem item : this.mUsedPool) {
            if (item.boundFramebuffer == fb) {
                item.lockCount++;
            }
        }
        throw new RenderException("framebuffer not found in used list");
    }

    public synchronized void unlock(Framebuffer fb) {
        for (FramebufferItem item : this.mUsedPool) {
            if (item.boundFramebuffer == fb) {
                item.lockCount--;
            }
        }
        throw new RenderException("framebuffer not found in used list");
    }

    public synchronized Framebuffer takeFramebuffer(int width, int height, int format) {
        Framebuffer framebuffer;
        FramebufferItem item;
        FramebufferTexture storage;
        long context = OpenGlUtils.currentContextHandle();
        Iterator<FramebufferItem> iter = this.mFreePool.iterator();
        while (iter.hasNext()) {
            item = (FramebufferItem) iter.next();
            storage = item.storage;
            if (storage.width == width && storage.height == height && storage.format == format) {
                iter.remove();
                this.mUsedPool.add(item);
                framebuffer = new Framebuffer("cache", storage, true);
                item.boundFramebuffer = framebuffer;
                item.boundContext = context;
                break;
            }
        }
        storage = new FramebufferTexture(width, height, format);
        storage.owned = false;
        framebuffer = new Framebuffer("cache", storage);
        item = new FramebufferItem(storage, false);
        item.boundFramebuffer = framebuffer;
        item.boundContext = context;
        this.mUsedPool.add(item);
        return framebuffer;
    }

    public synchronized void offerFramebuffer(Framebuffer fb) {
        fb.checkContext();
        Iterator<FramebufferItem> iter = this.mUsedPool.iterator();
        while (iter.hasNext()) {
            FramebufferItem item = (FramebufferItem) iter.next();
            if (item.boundFramebuffer == fb) {
                if (item.lockCount <= 0) {
                    fb.destroy();
                    iter.remove();
                    item.boundContext = 0;
                    item.boundFramebuffer = null;
                    if (!item.isExternal) {
                        item.updateHeartbeat();
                        this.mFreePool.add(item);
                    }
                }
            }
        }
        throw new RenderException("framebuffer offered but it's unknown");
    }

    public synchronized void endFrame() {
        collectGarbage();
    }

    synchronized void collectGarbage() {
        long now = System.nanoTime();
        Iterator<FramebufferItem> iter = this.mFreePool.iterator();
        while (iter.hasNext()) {
            FramebufferItem item = (FramebufferItem) iter.next();
            if (now - item.heartbeatNanos > this.mExpiryNanos) {
                FramebufferTexture storage = item.storage;
                Log.i(TAG, String.format("garbage collection removed framebuffer %dx%d", new Object[]{Integer.valueOf(storage.width), Integer.valueOf(storage.height)}));
                storage.owned = true;
                storage.destroy();
                iter.remove();
            }
        }
    }
}