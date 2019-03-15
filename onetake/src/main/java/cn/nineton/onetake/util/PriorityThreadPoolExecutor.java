package cn.nineton.onetake.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolExecutor extends ForkThreadPoolExecutor {
    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
}