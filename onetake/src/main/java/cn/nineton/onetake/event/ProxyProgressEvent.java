package cn.nineton.onetake.event;


import cn.nineton.onetake.bean.LongVideosModel;

public class ProxyProgressEvent {
    private LongVideosModel model;
    private float proxyProgress;
    private boolean proxySuccess;

    public ProxyProgressEvent(float proxyProgress, LongVideosModel model) {
        this.proxyProgress = proxyProgress;
        this.model = model;
        this.proxySuccess = false;
    }

    public ProxyProgressEvent(float proxyProgress, LongVideosModel model, boolean isSuccessful) {
        this.proxyProgress = proxyProgress;
        this.model = model;
        this.proxySuccess = isSuccessful;
    }

    public boolean isProxySuccess() {
        return this.proxySuccess;
    }

    public float getProxyProgress() {
        return this.proxyProgress;
    }

    public int getProxyProgressInt() {
        return (int) (this.proxyProgress * 100.0f);
    }

    public LongVideosModel getModel() {
        return this.model;
    }
}