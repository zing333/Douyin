package cn.nineton.onetake.util;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class WorkGroup {
    static final String TAG = "WorkGroup";
    private ArrayList<WorkItem> mWorkItems = new ArrayList();

    private static final class WorkItem {
        boolean mIsFinished = false;
        String mName;
        Runnable mRunnable;
        Thread mThread;

        WorkItem(String name, Runnable r) {
            this.mName = name;
            this.mRunnable = r;
        }

        void run() {
//            this.mThread = new Thread(WorkGroup$WorkItem$$Lambda$1.lambdaFactory$(this));
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lambda$run$0();
                }
            });
            this.mThread.setName("WorkGroup:" + this.mName);
            this.mThread.start();
        }

        private /* synthetic */ void lambda$run$0() {
            try {
                Log.i(WorkGroup.TAG, String.format("starting work:%s", new Object[]{this.mName}));
                this.mRunnable.run();
                Log.i(WorkGroup.TAG, String.format("finished work:%s", new Object[]{this.mName}));
            } finally {
                this.mIsFinished = true;
            }
        }

        void join() throws InterruptedException {
            this.mThread.join();
        }
    }

    public synchronized void addTask(String name, Runnable r) {
        WorkItem wi = new WorkItem(name, r);
        wi.run();
        this.mWorkItems.add(wi);
    }

    public synchronized void waitUntilDone() {
        if (this.mWorkItems.size() > 0) {
            Iterator it = this.mWorkItems.iterator();
            while (it.hasNext()) {
                WorkItem wi = (WorkItem) it.next();
                try {
                    Log.i(TAG, String.format("waiting on work:%s", new Object[]{wi.mName}));
                    wi.join();
                } catch (InterruptedException e) {
                    Log.w(TAG, "failed to join thread (interrupted)");
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "finished waiting on all items.");
            this.mWorkItems.clear();
        }
    }
}