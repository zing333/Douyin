package cn.nineton.onetake.util;

import android.os.Environment;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.thread.PriorityRunnable;
//import com.blink.academy.onetake.support.thread.PriorityThreadPoolManager;
//import com.blink.academy.onetake.support.utils.FileUtil;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.bean.LongVideosModel;

public class CameraVideoPathModel {
    private static CameraVideoPathModel mVideoPathModel;
    private List<String> mNeedDeleteVideoPath = new ArrayList();

    private CameraVideoPathModel() {
    }

    public static CameraVideoPathModel getInstance() {
        if (mVideoPathModel == null) {
            mVideoPathModel = new CameraVideoPathModel();
        }
        return mVideoPathModel;
    }

    public void addVideoPath(String videoPath) {
        if (videoPath != null) {
            this.mNeedDeleteVideoPath.add(videoPath);
        }
    }

    public void addVideoPaths(List<String> videoPaths) {
        if (this.mNeedDeleteVideoPath == null) {
            this.mNeedDeleteVideoPath = new ArrayList();
        }
        if (videoPaths != null && videoPaths.size() > 0) {
            this.mNeedDeleteVideoPath.addAll(videoPaths);
        }
    }

    public void removeVideoPath(List<LongVideosModel> videoPath) {
        if (this.mNeedDeleteVideoPath != null) {
            int allVideoSize = this.mNeedDeleteVideoPath.size();
            if (allVideoSize > 0 && videoPath != null) {
                int removeSize = videoPath.size();
                if (allVideoSize > 0) {
                    for (int i = allVideoSize - 1; i >= 0; i--) {
                        for (int j = 0; j < removeSize; j++) {
                            if (this.mNeedDeleteVideoPath.contains(videoPath.get(j).getOriginalMediaPath())) {
                                this.mNeedDeleteVideoPath.remove(videoPath.get(j).getOriginalMediaPath());
                            }
                        }
                    }
                }
            }
        }
    }

    public void addVideoPath(List<LongVideosModel> mList) {
        if (mList != null && mList.size() > 0) {
            for (LongVideosModel model : mList) {
                String videoPath = model.getOriginalMediaPath();
                String path = Environment.getExternalStorageDirectory().getPath();
                if (!(videoPath == null || videoPath.contains(path))) {
                    this.mNeedDeleteVideoPath.add(model.getOriginalMediaPath());
                }
            }
        }
    }

    public void deleteCameraVideo() {
        if (this.mNeedDeleteVideoPath != null && this.mNeedDeleteVideoPath.size() > 0) {
            final int size = this.mNeedDeleteVideoPath.size();
            PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                public void run() {
                    for (int i = size - 1; i >= 0; i--) {
                        FileUtil.deleteFile((String) CameraVideoPathModel.this.mNeedDeleteVideoPath.remove(i));
                    }
                    CameraVideoPathModel.this.mNeedDeleteVideoPath.clear();
                }
            });
        }
    }
}