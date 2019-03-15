package com.xue.douyin.common.view.record;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.xue.douyin.common.preview.filters.ImageFilter;

/**
 * Created by 薛贤俊 on 2018/3/9.
 */

public class RecordSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    private OnSurfaceCreatedCallback mCreatedCallback;
    private RecordRenderer mRenderer;

    public RecordSurfaceView(Context context) {
        super(context);
        setup();
    }

    public RecordSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        mRenderer = new RecordRenderer(this);
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void setOnSurfaceCreatedCallback(OnSurfaceCreatedCallback callback) {
        this.mCreatedCallback = callback;
    }

    void onSurfaceCreated(SurfaceTexture texture, EGLContext context) {
        if (mCreatedCallback != null) {
            mCreatedCallback.onSurfaceCreated(texture, context);
        }
        texture.setOnFrameAvailableListener(this);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    public void setFrameListener(OnFrameAvailableListener listener) {
        mRenderer.setFrameListener(listener);
    }

    public void setPreviewSize(int width,int height){
        mRenderer.setPreviewSize(width,height);
    }

    public void setFilter(ImageFilter filter){
        mRenderer.setFilter(filter);
    }
}
