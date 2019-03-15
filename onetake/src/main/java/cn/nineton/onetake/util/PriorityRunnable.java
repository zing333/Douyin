package cn.nineton.onetake.util;

import android.support.annotation.NonNull;

public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
    private int priority;

    public PriorityRunnable(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException();
        }
        this.priority = priority;
    }

    public int compareTo(@NonNull PriorityRunnable another) {
        int my = getPriority();
        int other = another.getPriority();
        if (my < other) {
            return 1;
        }
        return my > other ? -1 : 0;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException();
        }
        this.priority = priority;
    }
}