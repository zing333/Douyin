package cn.nineton.onetake;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.nineton.onetake.bean.BitmapInfo;
import cn.nineton.onetake.bean.FilterModel;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoBitmapsModel;
import cn.nineton.onetake.bean.VideoInfo;
import cn.nineton.onetake.event.PublishDraftEvent;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.util.DraftModel;
import cn.nineton.onetake.util.GetPathFromUri;
import cn.nineton.onetake.util.PermissionUtil;
import cn.nineton.onetake.widget.LoadingDialog;
import cn.nineton.onetake.widget.StaticLayoutUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RxPermissions rxPermissions;
    LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxPermissions = new RxPermissions(this);
        dialog = new LoadingDialog();
    }

    public void toVideo(View view) {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    public void jump(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent(this, target);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0X100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            String path = GetPathFromUri.getPath(this, data.getData());
            genResult(path);
        }
    }
    int videoWidth = 0;
    int videoHeight = 0;
    private void genResult(String path) {
       Disposable dd = Observable.timer(2000,TimeUnit.MILLISECONDS).compose((ObservableTransformer<Long, ArrayList<LongVideosModel>>) upstream -> {
           ArrayList<LongVideosModel> temp = new ArrayList<>();
           String filePath = path;
           VideoInfo videoInfo = retreVideoInfo2(path);
           videoWidth = videoInfo.getWidth();
           videoHeight = videoInfo.getHeight();
           LongVideosModel model = new LongVideosModel(0,videoInfo.getPath(),videoInfo.getDuration());
           model.setVideoWidth(videoInfo.getWidth());
           model.setVideoHeight(videoInfo.getHeight());
           temp.add(model);
           return Observable.create(emitter -> {
               emitter.onNext(temp);
               emitter.onComplete();
           });
       }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    dialog.dismissAllowingStateLoss();
                    startFilterActivity2(o);
                }, throwable -> {
                    dialog.dismissAllowingStateLoss();
                    Toast.makeText(this, "解析数据失败", Toast.LENGTH_LONG).show();
                },() -> {},disposable -> {
                    dialog.showNow(getSupportFragmentManager(),"loading");
                });
    }

    public static VideoInfo retreVideoInfo2(String path) {
        VideoInfo videoInfo = new VideoInfo();
        long duration = 0;
        int width = 0;
        int height = 0;
        int rotation = -1;
        String mime = "";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        try {
            mime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            duration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            rotation = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        } catch (RuntimeException r) {
            Log.e("debug","获取视频信息失败:"+r.getMessage());
        }finally {
            retriever.release();
        }
        videoInfo.setPath(path);
        videoInfo.setMime(mime);
        videoInfo.setRotation(rotation);
        videoInfo.setHeight(height);
        videoInfo.setWidth(width);
        videoInfo.setDuration(duration);
        videoInfo.setHasAudio(true);
        videoInfo.setHasVideo(true);
        int sWidth = videoInfo.getWidth();
        int sHeight = videoInfo.getHeight();
        switch (videoInfo.getRotation()) {
            case 0:
            case 180:
                break;
            case 90:
            case 270:
                videoInfo.setWidth(sHeight);
                videoInfo.setHeight(sWidth);
                break;
        }
        return videoInfo;
    }

    private void requestPermission(String[] perGroup) {
        Disposable d = rxPermissions.request(perGroup)
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
                        startActivityForResult(Intent.createChooser(intentvideo, "选择要导入的视频"), 0x100);
                    } else {
                        Toast.makeText(this, "未获取授权", Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(this, "获取授权失败", Toast.LENGTH_LONG).show();
                });
    }

    ArrayList<LongVideosModel> mLongVideoModels = new ArrayList<>();
    private void startFilterActivity2(ArrayList<LongVideosModel> longVideosModels){
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra(FilterActivity.ORIENTATION_INTENT, 0);
        intent.putExtra(FilterActivity.DATA_FROM, 1);
        intent.putExtra(FilterActivity.DATA_TYPE, 2);
        DraftModel mCurDraftModel = new DraftModel(String.valueOf(new Date().getTime()));
        FilterModel filterModel = new FilterModel();
        filterModel.setFilterName("收藏");
        mCurDraftModel.setFilterModel(filterModel);
//        MediaMetadataRetriever re=null;
//        re.getFrameAtTime(0);
        EventBus.getDefault().postSticky(new PublishDraftEvent(mCurDraftModel));
        intent.putExtra(FilterActivity.OFFSETX_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        intent.putExtra(FilterActivity.OFFSETY_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        intent.putParcelableArrayListExtra(FilterActivity.LONG_VIDEO_MODELS, longVideosModels);
        intent.putParcelableArrayListExtra("LongVideoPaht", longVideosModels);
        intent.putExtra(FilterActivity.VIDEO_WIDTH_INTENT, videoWidth);
        String activityFr = "FromAvatar";
//        if (this.comeFrom == 1) {
//            activityFr = VideoActivity2.FromMeAvatar;
//        } else if (this.comeFrom == 2) {
//            activityFr = VideoActivity2.FromAvatar;
//        }
        intent.putExtra("ActivityFromBundle", activityFr);
        intent.putExtra(FilterActivity.VIDEO_HEIGHT_INTENT, videoHeight);
        startActivity(intent);
    }
    private void startFilterActivity(List<LongVideosModel> longVideosModels) {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra(FilterActivity.ORIENTATION_INTENT, 0);
        intent.putExtra(FilterActivity.TRANSFORM_INTENT, 0);
        intent.putExtra(FilterActivity.RECORD_VIDEO, false);
        intent.putExtra(FilterActivity.DATA_FROM, 2);
        intent.putExtra(FilterActivity.DATA_TYPE, 0);
        intent.putExtra("has_audio_permission", PermissionUtil.verifyAudioPermission());
        intent.putParcelableArrayListExtra("LongVideoPaht", this.mLongVideoModels);
        intent.putExtra(FilterActivity.BACK_OR_FRONT_CAMERA, false);
        intent.putExtra(FilterActivity.IS_SQUARE_INTENT, false);
//        DraftModel mCurDraftModel = new DraftModel(String.valueOf(new Date().getTime()));
//        FilterModel filterModel = new FilterModel();
//        filterModel.setFilterName(this.name_en);
//        mCurDraftModel.setFilterModel(filterModel);
//        EventBus.getDefault().postSticky(new PublishDraftEvent(mCurDraftModel));
//        if (this.mCaptureTime != 0) {
//            fps = 1.0f / (((1.0f * ((float) this.mCaptureTime)) / 1000000.0f) / 17.0f);
//        } else {
//            fps = StaticLayoutUtil.DefaultSpacingadd;
//        }
        intent.putExtra(FilterActivity.FPS_INTENT, ((float) ((int) (10.0f * 0.0f))) / 10.0f);
        intent.putExtra(FilterActivity.OFFSETX_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        intent.putExtra(FilterActivity.OFFSETY_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        intent.putExtra(FilterActivity.VIDEO_WIDTH_INTENT, 0);
        intent.putExtra(FilterActivity.VIDEO_HEIGHT_INTENT, 0);
        startActivity(intent);
    }

}
