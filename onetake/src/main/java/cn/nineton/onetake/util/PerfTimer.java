package cn.nineton.onetake.util;

import android.util.Log;

public class PerfTimer {
    long t = System.nanoTime();

    public void printElapsedGreater(long minElapsedUs, String tag, String desc) {
        long us = (System.nanoTime() - this.t) / 1000;
        if (us > minElapsedUs) {
            long ms = us / 1000;
            double secs = ((double) us) / 1000000.0d;
            String str = tag;
            Log.d(str, String.format("%s took %dus %dms %fs", new Object[]{desc, Long.valueOf(us), Long.valueOf(ms), Double.valueOf(secs)}));
        }
    }

    public void printElapsed(String tag, String desc) {
        printElapsedGreater(0, tag, desc);
    }
}