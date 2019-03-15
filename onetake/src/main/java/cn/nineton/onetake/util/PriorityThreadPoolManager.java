package cn.nineton.onetake.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolManager {
    public static final int EDIT_PRIORITY = 9;
    public static final int HIGH_PRIORITY = 8;
    public static final int LOW_PRIORITY = 1;
    public static final int NET_PRIORITY = 6;
    public static final int NORM_PRIORITY = 5;
    public static final String TAG = PriorityThreadPoolManager.class.getSimpleName();
    static PriorityThreadPoolManager sPriorityThreadPoolManager;
    int CPUCount = Runtime.getRuntime().availableProcessors();
    int corePoolSize;
    PriorityThreadPoolExecutor mPriorityThreadPoolExecutor;
    PriorityThreadPoolExecutor mSingleThreadLinkedPoolExecutor;
    PriorityThreadPoolExecutor mSingleThreadPoolExecutor;
    int maximumPoolSize;

    public PriorityThreadPoolManager() {
        int i;
        if (this.CPUCount - 1 > 0) {
            i = this.CPUCount - 1;
        } else {
            i = 1;
        }
        this.corePoolSize = i;
        this.maximumPoolSize = (this.CPUCount * 2) + 1;
        this.mPriorityThreadPoolExecutor = new PriorityThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, 0, TimeUnit.SECONDS, new PriorityBlockingQueue());
        this.mSingleThreadPoolExecutor = new PriorityThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new PriorityBlockingQueue());
    }

    public static PriorityThreadPoolManager getInstance() {
        if (sPriorityThreadPoolManager == null) {
            sPriorityThreadPoolManager = new PriorityThreadPoolManager();
        }
        return sPriorityThreadPoolManager;
    }

    public PriorityThreadPoolExecutor getPriorityThreadPoolExecutor() {
        if (this.mPriorityThreadPoolExecutor == null) {
            this.mPriorityThreadPoolExecutor = new PriorityThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, 0, TimeUnit.SECONDS, new PriorityBlockingQueue());
        }
        return this.mPriorityThreadPoolExecutor;
    }

    public static void execute(PriorityRunnable runnable) {
        getInstance().getPriorityThreadPoolExecutor().execute(runnable);
    }

    private PriorityThreadPoolExecutor getSingleThreadPoolExecutor() {
        if (this.mSingleThreadPoolExecutor == null) {
            this.mSingleThreadPoolExecutor = new PriorityThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new PriorityBlockingQueue());
        }
        return this.mSingleThreadPoolExecutor;
    }

    private PriorityThreadPoolExecutor getLinkedThreadPoolExecutor() {
        if (this.mSingleThreadLinkedPoolExecutor == null) {
            this.mSingleThreadLinkedPoolExecutor = new PriorityThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, 0, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        return this.mSingleThreadLinkedPoolExecutor;
    }

    public static void executeInSingleThreadPool(PriorityRunnable runnable) {
        getInstance().getSingleThreadPoolExecutor().execute(runnable);
    }

    public static void executeInLinkedThreadPool(PriorityRunnable runnable) {
        getInstance().getLinkedThreadPoolExecutor().execute(runnable);
    }
}