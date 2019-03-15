package cn.nineton.glsurfacedemo;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.EGLContext;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.nineton.glsurfacedemo.util.GetPathFromUri;
import cn.nineton.glsurfacedemo.util.ScreenUtils;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_VIDEO = 0x110;
    VideoView videoView;
    TextView bottom;
    Button btnShirk;
    LinearLayout bottonPannel;
    RxPermissions rxPermissions;
    Disposable perDisp;
    int pannelHeight = (int) (ScreenUtils.getScreenHeight() * 0.3f);
    int pannelInitHeight = (int) (ScreenUtils.getScreenHeight() * 0.1f);
    int effectMarginTop = (int) (ScreenUtils.getScreenHeight() * 0.1f);
    int effectMarginBottom = (int) (ScreenUtils.getScreenHeight() * 0.1f);

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoview);
        bottom = findViewById(R.id.bottom);
        btnShirk = findViewById(R.id.shirk_videoview);
        bottonPannel = findViewById(R.id.bottom_pannel);
        btnShirk.setOnClickListener(this);
        ViewGroup.LayoutParams params = bottonPannel.getLayoutParams();
        params.height = pannelInitHeight;
        bottonPannel.setLayoutParams(params);

        ViewGroup.LayoutParams videoPar = videoView.getLayoutParams();
        videoPar.height = ScreenUtils.getScreenHeight()-pannelInitHeight;
        videoView.setLayoutParams(videoPar);

        rxPermissions = new RxPermissions(this);
        checkPermission();
    }

    private void destoryPerDisp() {
        if (perDisp != null && !perDisp.isDisposed()) {
            perDisp.dispose();
        }
    }

    private void checkPermission() {
        destoryPerDisp();
        perDisp = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        Intent intentvideo = new Intent();
                        if (Build.VERSION.SDK_INT < 19) {
                            intentvideo.setAction(Intent.ACTION_GET_CONTENT);
                            intentvideo.setType("video/*");
                        } else {
                            intentvideo.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intentvideo.addCategory(Intent.CATEGORY_OPENABLE);
                            intentvideo.setType("video/*");
                        }
                        startActivityForResult(Intent.createChooser(intentvideo, "选择要导入的视频"), REQUEST_VIDEO);
                    } else {
                        showError("获取权限失败:被拒绝");
                    }
                }, throwable -> {
                    showError("获取权限失败:" + throwable.getMessage());
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                String path = GetPathFromUri.getPath(this, data.getData());
                if (!TextUtils.isEmpty(path)) {
                    startToPlay(path);
                } else {
                    showError("视频地址空");
                }
            } else {
                showError("未获取到视频地址");
            }
        }
    }

    public void startToPlay(String path) {
        videoView.setOnPreparedListener(mp -> {
            mp.start();
            mp.setLooping(true);
        });
        videoView.setVideoPath(path);
    }

    public void showError(String err) {
        Snackbar.make(bottom, TextUtils.isEmpty(err) ? "未知错误" : err, Snackbar.LENGTH_LONG).show();
    }

    SurfaceTexture surfaceTexture = null;

    enum SIZE_STATE {
        SIZE_EXPAND,
        SIZE_PACKUP;
    }

    SIZE_STATE currentSizeState = SIZE_STATE.SIZE_PACKUP;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shirk_videoview:
                switch (currentSizeState) {
                    case SIZE_EXPAND:
                        hideEffectPanel();
                        enlargeSurfaceView();
                        currentSizeState = SIZE_STATE.SIZE_PACKUP;
                        break;
                    case SIZE_PACKUP:
                        showEffectPanel();
                        shrinkSurfaceView();
                        currentSizeState = SIZE_STATE.SIZE_EXPAND;
                        break;
                }
                break;
        }
    }

    public void showEffectPanel() {
        ValueAnimator animator = ValueAnimator.ofInt(pannelInitHeight, pannelHeight);
        animator.addUpdateListener(animation -> {
            int height = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = bottonPannel.getLayoutParams();
            params.height = height;
            bottonPannel.setLayoutParams(params);
        });
        animator.setDuration(300);
        animator.start();
    }

    public void shrinkSurfaceView() {
        AnimatorSet set = new AnimatorSet();
        float scale = 1.0f * (ScreenUtils.getScreenHeight() - pannelHeight) / (ScreenUtils.getScreenHeight()-pannelInitHeight);
        ObjectAnimator x = ObjectAnimator.ofFloat(videoView, View.SCALE_X, 1.0f, scale);
        videoView.setPivotX(ScreenUtils.getScreenWidth() / 2);

        ObjectAnimator y = ObjectAnimator.ofFloat(videoView, View.SCALE_Y, 1.0f, scale);
        videoView.setPivotY(0);
        ObjectAnimator translate = ObjectAnimator.ofFloat(videoView, View.TRANSLATION_Y, 0,
                0);
        set.setDuration(300);
        set.playTogether(x, y, translate);
        set.start();
    }

    public void enlargeSurfaceView() {
        AnimatorSet set = new AnimatorSet();
        float scale = 1.0f * (ScreenUtils.getScreenHeight() - pannelHeight) / (ScreenUtils.getScreenHeight()-pannelInitHeight);
        ObjectAnimator x = ObjectAnimator.ofFloat(videoView, View.SCALE_X, scale, 1.0f);
        videoView.setPivotX(ScreenUtils.getScreenWidth() / 2);

        ObjectAnimator y = ObjectAnimator.ofFloat(videoView, View.SCALE_Y, scale, 1.0f);
        videoView.setPivotY(0);
        ObjectAnimator translate = ObjectAnimator.ofFloat(videoView, View.TRANSLATION_Y,
                0, 0);
        set.setDuration(300);
        set.playTogether(x, y, translate);
        set.start();
    }

    public void hideEffectPanel() {
        ValueAnimator animator = ValueAnimator.ofInt(pannelHeight, pannelInitHeight);
        animator.addUpdateListener(animation -> {
            int height = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = bottonPannel.getLayoutParams();
            params.height = height;
            bottonPannel.setLayoutParams(params);
        });
        animator.setDuration(300);
        animator.start();
    }

}
