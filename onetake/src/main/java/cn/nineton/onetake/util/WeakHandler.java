package cn.nineton.onetake.util;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WeakHandler {
    private final Callback mCallback;
    private final ExecHandler mExec;
    private Lock mLock;
    @VisibleForTesting
    final ChainedRef mRunnables;

    static class ChainedRef {
        @NonNull
        Lock lock;
        @Nullable
        ChainedRef next;
        @Nullable
        ChainedRef prev;
        @NonNull
        final Runnable runnable;
        @NonNull
        final WeakRunnable wrapper;

        public ChainedRef(@NonNull Lock lock, @NonNull Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WeakRunnable(new WeakReference(r), new WeakReference(this));
        }

        public WeakRunnable remove() {
            this.lock.lock();
            try {
                if (this.prev != null) {
                    this.prev.next = this.next;
                }
                if (this.next != null) {
                    this.next.prev = this.prev;
                }
                this.prev = null;
                this.next = null;
                return this.wrapper;
            } finally {
                this.lock.unlock();
            }
        }

        public void insertAfter(@NonNull ChainedRef candidate) {
            this.lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }
                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                this.lock.unlock();
            }
        }

        @Nullable
        public WeakRunnable remove(Runnable obj) {
            this.lock.lock();
            try {
                for (ChainedRef curr = this.next; curr != null; curr = curr.next) {
                    if (curr.runnable == obj) {
                        WeakRunnable remove = curr.remove();
                        return remove;
                    }
                }
                this.lock.unlock();
                return null;
            } finally {
                try {
                    this.lock.unlock();
                }catch (Exception e){}
            }
        }
    }

    private static class ExecHandler extends Handler {
        private final WeakReference<Callback> mCallback;

        ExecHandler() {
            this.mCallback = null;
        }

        ExecHandler(WeakReference<Callback> callback) {
            this.mCallback = callback;
        }

        ExecHandler(Looper looper) {
            super(looper);
            this.mCallback = null;
        }

        ExecHandler(Looper looper, WeakReference<Callback> callback) {
            super(looper);
            this.mCallback = callback;
        }

        public void handleMessage(@NonNull Message msg) {
            if (this.mCallback != null) {
                Callback callback = (Callback) this.mCallback.get();
                if (callback != null) {
                    callback.handleMessage(msg);
                }
            }
        }
    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            this.mDelegate = delegate;
            this.mReference = reference;
        }

        public void run() {
            Runnable delegate = (Runnable) this.mDelegate.get();
            ChainedRef reference = (ChainedRef) this.mReference.get();
            if (reference != null) {
                reference.remove();
            }
            if (delegate != null) {
                delegate.run();
            }
        }
    }

    public WeakHandler() {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = null;
        this.mExec = new ExecHandler();
    }

    public WeakHandler(@Nullable Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new ExecHandler(new WeakReference(callback));
    }

    public WeakHandler(@NonNull Looper looper) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = null;
        this.mExec = new ExecHandler(looper);
    }

    public WeakHandler(@NonNull Looper looper, @NonNull Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new ExecHandler(looper, new WeakReference(callback));
    }

    public final boolean post(@NonNull Runnable r) {
        return this.mExec.post(wrapRunnable(r));
    }

    public final boolean postAtTime(@NonNull Runnable r, long uptimeMillis) {
        return this.mExec.postAtTime(wrapRunnable(r), uptimeMillis);
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return this.mExec.postAtTime(wrapRunnable(r), token, uptimeMillis);
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return this.mExec.postDelayed(wrapRunnable(r), delayMillis);
    }

    public final boolean postAtFrontOfQueue(Runnable r) {
        return this.mExec.postAtFrontOfQueue(wrapRunnable(r));
    }

    public final void removeCallbacks(Runnable r) {
        WeakRunnable runnable = this.mRunnables.remove(r);
        if (runnable != null) {
            this.mExec.removeCallbacks(runnable);
        }
    }

    public final void removeCallbacks(Runnable r, Object token) {
        WeakRunnable runnable = this.mRunnables.remove(r);
        if (runnable != null) {
            this.mExec.removeCallbacks(runnable, token);
        }
    }

    public final boolean sendMessage(Message msg) {
        return this.mExec.sendMessage(msg);
    }

    public final boolean sendEmptyMessage(int what) {
        return this.mExec.sendEmptyMessage(what);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return this.mExec.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return this.mExec.sendEmptyMessageAtTime(what, uptimeMillis);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return this.mExec.sendMessageDelayed(msg, delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return this.mExec.sendMessageAtTime(msg, uptimeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return this.mExec.sendMessageAtFrontOfQueue(msg);
    }

    public final void removeMessages(int what) {
        this.mExec.removeMessages(what);
    }

    public final void removeMessages(int what, Object object) {
        this.mExec.removeMessages(what, object);
    }

    public final void removeCallbacksAndMessages(Object token) {
        this.mExec.removeCallbacksAndMessages(token);
    }

    public final boolean hasMessages(int what) {
        return this.mExec.hasMessages(what);
    }

    public final boolean hasMessages(int what, Object object) {
        return this.mExec.hasMessages(what, object);
    }

    public final Looper getLooper() {
        return this.mExec.getLooper();
    }

    private WeakRunnable wrapRunnable(@NonNull Runnable r) {
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        ChainedRef hardRef = new ChainedRef(this.mLock, r);
        this.mRunnables.insertAfter(hardRef);
        return hardRef.wrapper;
    }
}