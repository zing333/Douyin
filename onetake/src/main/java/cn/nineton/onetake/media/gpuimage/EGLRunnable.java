package cn.nineton.onetake.media.gpuimage;

public interface EGLRunnable<T> {
    T run(EGL10Helper eGL10Helper);
}