package cn.nineton.onetake.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.VideoTools.EGL10Helper;
//import com.blink.academy.onetake.VideoTools.FilterView;
//import com.blink.academy.onetake.VideoTools.InterpolatedFloat;
//import com.blink.academy.onetake.VideoTools.Player.AspectMode;
//import com.blink.academy.onetake.VideoTools.Playlist;
//import com.blink.academy.onetake.VideoTools.Playlist.Entry;
//import com.blink.academy.onetake.VideoTools.Playlist.FileMedia;
//import com.blink.academy.onetake.VideoTools.Playlist.Rotation;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.bean.filterview.FilterPlayButtonBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.model.video.VideoBitmapsModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.FilterPlayButtonBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoBitmapsModel;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.widget.FilterView;
import cn.nineton.onetake.widget.InterpolatedFloat;
import cn.nineton.onetake.widget.Player;
import cn.nineton.onetake.widget.Playlist;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class FilterViewUtils {
    public static int[] getUploadLongVideoWidthHeight(int width, int height, boolean isLocation) {
        int mConstent;
        int[] mWidthHeights = new int[]{width, height};
        if (isLocation) {
            mConstent = 1080;
        } else {
            mConstent = 720;
        }
        if (width > height) {
            if (height > mConstent) {
                mWidthHeights[0] = (int) ((((float) (mConstent * width)) * 1.0f) / ((float) height));
                mWidthHeights[1] = mConstent;
            }
        } else if (width > mConstent) {
            mWidthHeights[1] = (int) ((((float) (mConstent * height)) * 1.0f) / ((float) width));
            mWidthHeights[0] = mConstent;
        }
        return mWidthHeights;
    }

    public static int[] getUploadVideoLongBitmapsWidthHeight(int width, int height) {
        int[] mWidthHeights = new int[]{width, height};
        if (width > height) {
            if (((float) height) > 400.0f) {
                mWidthHeights[0] = (int) (((((float) width) * 400.0f) * 1.0f) / ((float) height));
                mWidthHeights[1] = 400;
            }
        } else if (((float) width) > 400.0f) {
            mWidthHeights[1] = (int) (((((float) height) * 400.0f) * 1.0f) / ((float) width));
            mWidthHeights[0] = 400;
        }
        return mWidthHeights;
    }

    public static Playlist getCurrentPlaylist(FilterView mSurfaceView) {
        Playlist pl = new Playlist();
        Playlist playlist = mSurfaceView.getLongVideoPlaylist();
        if (playlist == null || playlist.size() <= 0) {
            return null;
        }
        int size = playlist.size();
        for (int i = 0; i < size; i++) {
            pl.add(playlist.get(i).clone());
        }
        return pl;
    }

    public static int[] getPreviewWidthHeight(int viewWidth, int viewHeight, int videoWidth, int videoHeight) {
        if (videoHeight > videoWidth) {
            if (videoHeight > viewHeight) {
                viewWidth = (int) ((((float) (viewHeight * videoWidth)) * 1.0f) / ((float) videoHeight));
            }
        } else if (videoHeight >= videoWidth) {
            viewHeight = Math.min(viewWidth, viewHeight);
            viewWidth = viewHeight;
        } else if (videoWidth > viewWidth) {
            viewHeight = (int) ((((float) (viewWidth * videoHeight)) * 1.0f) / ((float) videoWidth));
        }
        viewWidth += viewWidth % 2;
        viewHeight += viewHeight % 2;
        return new int[]{viewWidth, viewHeight};
    }

    public static Playlist initDraftPlaylist(List<LongVideosModel> videoModels, List<LongVideosModel> musicModels, @NonNull Bitmap mBitmap, boolean[] mediaMute) {
        if (videoModels == null || videoModels.size() == 0) {
            return null;
        }
        long totalDuration = 0;
        Playlist playlist = new Playlist();
        boolean allVideoVolumeGone = mediaMute[0];
        boolean allAudioVolumeGone = mediaMute[1];
        int size = videoModels.size();
        for (int i = 0; i < size; i++) {
            LongVideosModel model = (LongVideosModel) videoModels.get(i);
            if (!(model == null || model.mediaType == 1)) {
                long currentDuration = model.getCurrentDuration();
                String filePath = model.getPlaylistMediaPath();
                if (filePath == null) {
                    filePath = "";
                }
                File file = new File(filePath);
                float[] cropParams;
                if (model.mediaType == 0) {
                    if (file.exists()) {
                        Playlist.Entry entry = playlist.add(Playlist.FileMedia.create(file), totalDuration, model.getMediaStartTimeUs(), currentDuration * 1000).setMediaType(0);
                        model.setTimeLineStartTime(totalDuration);
                        entry.setVolume(allVideoVolumeGone ? StaticLayoutUtil.DefaultSpacingadd : model.getVideoVolume());
                        entry.setFilterIndex(i);
                        entry.setSpeed(model.getVideoSpeed());
                        entry.setFramerate(model.getVideoFrameRate());
                        Playlist.Rotation mRotation = getVideoRotation(model.getVideoRotate());
                        entry.setOutputRotation((float) model.getVideoRotate());
                        boolean shouldVertical = model.isShouldVertical();
                        if (model.isFrontCamera) {
                            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, !shouldVertical, shouldVertical);
                        } else {
                            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, shouldVertical, shouldVertical);
                        }
                        cropParams = model.getInputCropParams();
                        entry.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                        entry.setZoom(InterpolatedFloat.createLinear(model.getStartTimeUs(), model.getZoomStart(), model.getStartTimeUs() + (currentDuration * 1000), model.getZoomEnd()));
                    } else {
                        playlist = resetEntryWhenNoFile(model, totalDuration, 1000, playlist, i, mBitmap);
                    }
                } else if (model.mediaType == 2 || model.mediaType == 4) {
                    boolean fileIsExists = file.exists();
                    if (model.mediaType == 4) {
                        fileIsExists = true;
                    }
                    if (fileIsExists) {
                        Bitmap bitmap;
                        if (model.mediaType == 2) {
                            bitmap = VideoBitmapsModel.instancesModel().getBitmap(model.getPlaylistMediaPath());
                            cropParams = model.getInputCropParams();
                        } else {
                            bitmap = VideoBitmapsModel.instancesModel().getActivityBitmap();
                            cropParams = new float[4];
                            cropParams = new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f};
                        }
                        Playlist.Entry add = playlist.add(bitmap, totalDuration, 0, currentDuration * 1000, Player.AspectMode.RENDER_CROP);
                        model.setTimeLineStartTime(totalDuration);
                        add.setSpeed(model.getVideoSpeed());
                        add.setFilterIndex(i);
                        add.setFramerate(model.getVideoFrameRate());
                        if (model.isFrontCamera) {
                            add.setInputOrientation(Playlist.Rotation.ROTATE_0, true, false);
                        } else {
                            add.setInputOrientation(Playlist.Rotation.ROTATE_0, false, false);
                        }
                        add.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                        add.setZoom(InterpolatedFloat.createLinear(model.getStartTimeUs(), model.getZoomStart(), model.getStartTimeUs() + (currentDuration * 1000), model.getZoomEnd()));
                    } else {
                        playlist = resetEntryWhenNoFile(model, totalDuration, 1000, playlist, i, mBitmap);
                    }
                } else if (model.mediaType == 3) {
                }
                totalDuration += currentDuration * 1000;
            }
        }
        addActiveBGM(videoModels, playlist);
        if (musicModels == null || allAudioVolumeGone) {
            return playlist;
        }
        return setPlaylistBGMs(musicModels, playlist);
    }

    public static Playlist resetEntryWhenNoFile(LongVideosModel model, long totalDuration, long MS, Playlist playlist, int filterIndex, Bitmap mBlackBitmap) {
        model.mediaType = 2;
        Playlist.Entry entry = playlist.add(mBlackBitmap, totalDuration, 0, model.getCurrentDuration() * MS, Player.AspectMode.RENDER_CROP);
        entry.setSpeed(model.getVideoSpeed());
        entry.setFramerate(model.getVideoFrameRate());
        float[] cropParams = model.getInputCropParams();
        Playlist.Rotation mRotation = getVideoRotation(model.getVideoRotate());
        entry.setOutputRotation((float) model.getVideoRotate());
        if (model.isFrontCamera) {
            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, true, false);
        } else {
            entry.setInputOrientation(Playlist.Rotation.ROTATE_0, false, false);
        }
        entry.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
        entry.setZoom(InterpolatedFloat.createLinear(model.getStartTimeUs(), model.getZoomStart(), model.getStartTimeUs() + (model.getOriginalCurrentDuration() * 1000), model.getZoomEnd()));
        return playlist;
    }

    private static List<LongVideosModel> changeBgmListToPlayerUse(List<LongVideosModel> bgmModels) {
        if (TextUtil.isNull(bgmModels)) {
            return bgmModels;
        }
        ArrayList<LongVideosModel> curBgmModels = new ArrayList();
        curBgmModels.addAll(bgmModels);
        ArrayList<LongVideosModel> longVideosModels = new ArrayList();
        Iterator it = curBgmModels.iterator();
        while (it.hasNext()) {
            LongVideosModel model = (LongVideosModel) it.next();
            if (!model.isJustSeeForAudio()) {
                ArrayList<LongVideosModel.AudioVolume> audioVolumes = model.getAudioVolumes();
                if (!TextUtil.isNull(audioVolumes)) {
                    AudioTrackBean audioTrackBean = model.getAudioTrackBean();
                    if (audioTrackBean != null) {
                        long sumTimeMs = (long) (1000.0f * Float.valueOf(audioTrackBean.getDuration()).floatValue());
                        if (sumTimeMs > 0) {
                            long startTime = model.getStartTimeMs();
                            Iterator it2 = audioVolumes.iterator();
                            while (it2.hasNext()) {
                                LongVideosModel.AudioVolume audioVolume = (LongVideosModel.AudioVolume) it2.next();
                                LongVideosModel audioModel = new LongVideosModel(1);
                                audioModel.setVideoPath(model.getPlaylistMediaPath());
                                audioModel.setStartTime(startTime);
                                audioModel.verse = model.verse;
                                audioModel.setAudioStartTime(audioVolume.getStartTime());
                                audioModel.setAudioDuration(audioVolume.getDuration());
                                audioModel.setAudioVolume(audioVolume.getVolume());
                                longVideosModels.add(audioModel);
                                startTime += audioModel.getAudioDuration();
                                while (startTime >= sumTimeMs) {
                                    startTime = (startTime - sumTimeMs) + (audioModel.verse / 1000);
                                }
                            }
                        }
                    }
                }
            }
        }
        return longVideosModels;
    }

    public static Playlist setPlaylistBGMs(List<LongVideosModel> bgmModels, Playlist playlist) {
        if (bgmModels != null && bgmModels.size() > 0) {
            LongVideosModel endMusicModel = (LongVideosModel) bgmModels.get(bgmModels.size() - 1);
            bgmModels = changeBgmListToPlayerUse(bgmModels);
            String musicFilePath = "";
            int size = bgmModels.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel musicModel = (LongVideosModel) bgmModels.get(i);
                if (musicModel.getAudioVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                    Playlist.FileMedia musicMedia = Playlist.FileMedia.create(new File(musicModel.getPlaylistMediaPath()));
                    if (musicMedia != null) {
                        long musicStartUs = musicModel.getStartTimeUs();
                        long musicVerseUs = musicModel.verse;
                        long musicMediaDurationUs = musicMedia.getDurationUs();
                        long musicCurrentDurationUs = musicModel.getCurrentDuration() * 1000;
                        long musicTimeLineStartUs = musicModel.getTimeLineStartTimeUs();
                        long musicCurrentEndUs = musicStartUs + musicCurrentDurationUs;
                        while (musicCurrentDurationUs > 0) {
                            if (musicCurrentEndUs <= musicMediaDurationUs) {
                                playlist.add(musicMedia, musicTimeLineStartUs, musicStartUs, musicCurrentDurationUs).disableVideo().setVolume(musicModel.getAudioVolume()).setMediaType(1);
                                if (i == size - 1) {
                                    endMusicModel.setEndMusicStartAndDuration(musicStartUs, musicCurrentDurationUs);
                                }
                                musicCurrentDurationUs = 0;
                            } else {
                                long currentDuration = musicMediaDurationUs - musicStartUs;
                                if (currentDuration > musicCurrentDurationUs) {
                                    currentDuration = musicCurrentDurationUs;
                                }
                                playlist.add(musicMedia, musicTimeLineStartUs, musicStartUs, currentDuration).disableVideo().setVolume(musicModel.getAudioVolume()).setMediaType(1);
                                if (i == size - 1) {
                                    endMusicModel.setEndMusicStartAndDuration(musicStartUs, currentDuration);
                                }
                                musicStartUs = musicVerseUs;
                                musicCurrentDurationUs -= currentDuration;
                                musicTimeLineStartUs += currentDuration;
                                musicCurrentEndUs = (musicCurrentEndUs - currentDuration) + musicStartUs;
                            }
                        }
                    }
                }
            }
        }
        return playlist;
    }

    public static Playlist setPlaylistBGM(List<LongVideosModel> videosModels, LongVideosModel musicModel, Playlist playlist) {
        if (musicModel != null) {
            Playlist.FileMedia musicMedia = Playlist.FileMedia.create(new File(musicModel.getPlaylistMediaPath()));
            if (musicMedia != null) {
                long audioEndTimeUs = musicModel.getStartTimeUs();
                long musicVerseTimeUs = musicModel.verse;
                long musicCurrentDurationUs = musicMedia.getDurationUs() - audioEndTimeUs;
                int size = videosModels.size();
                for (int i = 0; i < size; i++) {
                    LongVideosModel videosModel = (LongVideosModel) videosModels.get(i);
                    if ((videosModel.mediaType == 0 || videosModel.mediaType == 2 || videosModel.mediaType == 4) && videosModel.getAudioVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                        long videoCurrentDurationUs = videosModel.getCurrentDuration() * 1000;
                        long timeLineStartTimeUs = videosModel.getTimeLineStartTimeUs();
                        while (musicCurrentDurationUs <= videoCurrentDurationUs) {
                            musicModel.setEndMusicStartAndDuration(audioEndTimeUs, videoCurrentDurationUs);
                            playlist.add(musicMedia, timeLineStartTimeUs, audioEndTimeUs, musicCurrentDurationUs).disableVideo().setVolume(videosModel.getAudioVolume()).setMediaType(1);
                            audioEndTimeUs = musicVerseTimeUs;
                            timeLineStartTimeUs += musicCurrentDurationUs;
                            videoCurrentDurationUs -= musicCurrentDurationUs;
                            musicCurrentDurationUs = musicMedia.getDurationUs() - musicVerseTimeUs;
                            if (videoCurrentDurationUs <= 0) {
                                break;
                            }
                        }
                        musicModel.setEndMusicStartAndDuration(audioEndTimeUs, videoCurrentDurationUs);
                        LogUtil.d("AddMusicTime", String.format("timeLine4 : %s, media : %s, duration : %s", new Object[]{Long.valueOf(timeLineStartTimeUs), Long.valueOf(audioEndTimeUs), Long.valueOf(videoCurrentDurationUs)}));
                        playlist.add(musicMedia, timeLineStartTimeUs, audioEndTimeUs, videoCurrentDurationUs).disableVideo().setVolume(videosModel.getAudioVolume()).setMediaType(1);
                        audioEndTimeUs += videoCurrentDurationUs;
                        musicCurrentDurationUs -= videoCurrentDurationUs;
                    }
                }
            }
        }
        return playlist;
    }

    public static boolean isVideoModel(int dataType) {
        return dataType == 2;
    }

    public static boolean isGifModel(int dataType) {
        return dataType == 0;
    }

    public static boolean isPictureModel(int dataType) {
        return dataType == 1;
    }

    public static long getUseDuration(long mDuration) {
        if (mDuration <= 100) {
            return mDuration;
        }
        String curD = "" + mDuration;
        return Long.parseLong(curD.substring(0, curD.length() - 2) + "00");
    }

    public static int[] computeCurrentClipIvMargin(FilterPlayButtonBean bean) {
        int viewHeight;
        int viewWidth;
        int shortSide;
        int surfaceWidth = bean.mSurfaceWidth;
        int surfaceHeight = bean.mSurfaceHeight;
        int mVideoWidth = bean.mVideoWidth;
        int mVideoHeight = bean.mVideoHeight;
        if (((((float) mVideoHeight) * 1.0f) / ((float) mVideoWidth)) - ((((float) surfaceHeight) * 1.0f) / ((float) surfaceWidth)) > StaticLayoutUtil.DefaultSpacingadd) {
            viewHeight = surfaceHeight;
            viewWidth = (int) ((((float) (viewHeight * mVideoWidth)) * 1.0f) / ((float) mVideoHeight));
            viewHeight += bean.mSurfaceTop;
        } else {
            viewWidth = surfaceWidth;
            viewHeight = (int) ((((float) (viewWidth * mVideoHeight)) * 1.0f) / ((float) mVideoWidth));
            viewHeight = (int) (((float) viewHeight) + (((((float) (surfaceHeight - viewHeight)) * 1.0f) / 2.0f) + ((float) bean.mSurfaceTop)));
        }
        if (viewWidth <= viewHeight) {
            shortSide = viewWidth;
        } else {
            shortSide = viewHeight;
        }
        int topMargin = (viewHeight - ((int) (((float) shortSide) * 0.03f))) - DensityUtil.dip2px(20.0f);
        return new int[]{((bean.mScreenWidth - viewWidth) / 2) + ((int) (((float) shortSide) * 0.03f)), topMargin};
    }

    public static int[] computePlayButtonLayout(FilterPlayButtonBean bean) {
        int viewHeight;
        int viewWidth;
        int surfaceWidth = bean.mSurfaceWidth;
        int surfaceHeight = bean.mSurfaceHeight;
        int mVideoWidth = bean.mVideoWidth;
        int mVideoHeight = bean.mVideoHeight;
        if (((((float) mVideoHeight) * 1.0f) / ((float) mVideoWidth)) - ((((float) surfaceHeight) * 1.0f) / ((float) surfaceWidth)) > StaticLayoutUtil.DefaultSpacingadd) {
            viewHeight = surfaceHeight;
            viewWidth = (int) ((((float) (viewHeight * mVideoWidth)) * 1.0f) / ((float) mVideoHeight));
            viewHeight += bean.mSurfaceTop;
        } else {
            viewWidth = surfaceWidth;
            viewHeight = (int) ((((float) (viewWidth * mVideoHeight)) * 1.0f) / ((float) mVideoWidth));
            viewHeight = (int) (((float) viewHeight) + (((((float) (surfaceHeight - viewHeight)) * 1.0f) / 2.0f) + ((float) bean.mSurfaceTop)));
        }
        return new int[]{viewWidth, viewHeight};
    }

    public static Playlist initializeVideo(List<LongVideosModel> videoPathList, boolean enableAudio) {
        Playlist pl = new Playlist();
        if (videoPathList != null && videoPathList.size() > 0) {
            long startDuration = 0;
            int size = videoPathList.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel model = (LongVideosModel) videoPathList.get(i);
                Playlist.FileMedia fileMedia = Playlist.FileMedia.create(model);
                long currentDuration = model.getCurrentDuration();
                long mediaStartTimeUs = model.getMediaStartTimeUs();
                model.setTimeLineStartTime(startDuration * 1000);
                Playlist.Entry entry = pl.add(fileMedia, startDuration * 1000, mediaStartTimeUs, currentDuration * 1000);
                entry.setFilterIndex(i);
                entry.setVolume(model.getVideoVolume());
                if (!enableAudio) {
                    entry.disableAudio();
                }
                entry.setSpeed(model.getVideoSpeed());
                entry.setFramerate(model.getVideoFrameRate());
                Playlist.Rotation mRotation = getVideoRotation(model.getVideoRotate());
                entry.setOutputRotation((float) model.getVideoRotate());
                boolean shouldVertical = model.isShouldVertical();
                if (model.isFrontCamera) {
                    entry.setInputOrientation(Playlist.Rotation.ROTATE_0, !shouldVertical, shouldVertical);
                } else {
                    entry.setInputOrientation(Playlist.Rotation.ROTATE_0, shouldVertical, shouldVertical);
                }
                if ((model.mediaType == 0 || model.mediaType == 2) && model.getInputRatio() != null) {
                    float[] cropParams = model.getInputCropParams();
                    entry.setInputCrop(cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
                }
                startDuration += currentDuration;
            }
        }
        return pl;
    }

    public static Playlist.Rotation getVideoRotation(int value) {
        switch (value) {
            case 0:
                return Playlist.Rotation.ROTATE_0;
            case 90:
                return Playlist.Rotation.ROTATE_90;
            case 180:
                return Playlist.Rotation.ROTATE_180;
            case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                return Playlist.Rotation.ROTATE_270;
            default:
                return Playlist.Rotation.ROTATE_0;
        }
    }

    public static int[] getSupportWidthHeight(int width, int height, EGL10Helper egl) {
        int[] mWidthHeight = new int[]{width, height};
        int maxWidth = egl.getMaxCaptureWidth();
        int maxHeight = egl.getMaxCaptureHeight();
        if (width > height) {
            if (width > maxWidth) {
                height = (int) ((((float) (maxWidth * height)) * 1.0f) / ((float) width));
                width = maxWidth;
            }
            if (height > maxHeight) {
                width = (int) ((((float) (maxHeight * width)) * 1.0f) / ((float) height));
                height = maxHeight;
            }
        } else {
            if (height > maxHeight) {
                width = (int) ((((float) (maxHeight * width)) * 1.0f) / ((float) height));
                height = maxHeight;
            }
            if (width > maxWidth) {
                height = (int) ((((float) (maxWidth * height)) * 1.0f) / ((float) width));
                width = maxWidth;
            }
        }
        height += height % 2;
        mWidthHeight[0] = width + (width % 2);
        mWidthHeight[1] = height;
        return mWidthHeight;
    }

    public static void addActiveBGM(List<LongVideosModel> videos, Playlist playlist) {
        int size = videos.size();
        for (int i = 0; i < size; i++) {
            LongVideosModel videosModel = (LongVideosModel) videos.get(i);
            if (videosModel.mediaType == 4) {
                long videoCurrentDurationUs = videosModel.getCurrentDuration() * 1000;
                long timeLineStartTimeUs = videosModel.getTimeLineStartTimeUs();
                if (videosModel.mediaType == 4) {
                    Playlist.FileMedia activeMedia = Playlist.FileMedia.create(new File(Config.getOneMinuteAudioFilePath()));
                    if (activeMedia != null) {
                        long activeMediaDurationUs = activeMedia.getDurationUs();
                        playlist.add(activeMedia, timeLineStartTimeUs, 0, activeMediaDurationUs).disableVideo().setVolume(videosModel.getAudioVolume()).setMediaType(1);
                        long activeTimeLineStartUs = timeLineStartTimeUs + (videosModel.getCurrentDuration() * 1000);
                        if (i == size - 1) {
                            activeTimeLineStartUs -= activeMediaDurationUs;
                        }
                        playlist.add(activeMedia, activeTimeLineStartUs, 0, activeMediaDurationUs).disableVideo().setVolume(videosModel.getAudioVolume()).setMediaType(1);
                    }
                }
            }
        }
    }
}