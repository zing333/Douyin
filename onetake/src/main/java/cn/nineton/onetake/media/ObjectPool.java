package cn.nineton.onetake.media;

import java.util.concurrent.LinkedBlockingQueue;

public class ObjectPool<T> {
    LinkedBlockingQueue<T> mObjects = new LinkedBlockingQueue();

    public T take() {
        try {
            return this.mObjects.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void offer(T t) {
        this.mObjects.offer(t);
    }
}