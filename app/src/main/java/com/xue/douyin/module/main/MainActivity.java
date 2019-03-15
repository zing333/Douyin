package com.xue.douyin.module.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xue.douyin.R;
import com.xue.douyin.base.activity.BaseBlankActivity;
import com.xue.douyin.module.effect.activity.AfterEffectActivity;
import com.xue.douyin.module.record.activity.RecordActivity;
import com.xue.douyin.utils.GetPathFromUri;

import io.reactivex.disposables.Disposable;

/**
 * @Author: savion
 * @Date: 2019/3/13 17:44
 * @Des:
 **/
public class MainActivity extends BaseBlankActivity {
    public static final int REQUEST_VIDEO = 0x110;

    RxPermissions rxPermissions;
    Disposable perDisp;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxPermissions = new RxPermissions(this);
    }

    public void toRecord(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    private void destoryPerDisp() {
        if (perDisp != null && !perDisp.isDisposed()) {
            perDisp.dispose();
        }
    }

    public void choose(View view) {
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

    public void showError(String err) {
        Toast.makeText(this, TextUtils.isEmpty(err) ? "未知错误" : err, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                String path = GetPathFromUri.getPath(this, data.getData());
                if (!TextUtils.isEmpty(path)) {
                    AfterEffectActivity.start(this,path);
                } else {
                    showError("视频地址空");
                }
            } else {
                showError("未获取到视频地址");
            }
        }
    }
}
