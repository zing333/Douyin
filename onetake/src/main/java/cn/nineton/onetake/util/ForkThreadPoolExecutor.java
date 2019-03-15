package cn.nineton.onetake.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ForkThreadPoolExecutor extends ThreadPoolExecutor {
    public static final String TAG = ForkThreadPoolExecutor.class.getSimpleName();

    public ForkThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    protected void terminated() {
        super.terminated();
    }
}