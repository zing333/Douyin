package cn.nineton.onetake.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.VideoEditImageEntity;
import cn.nineton.onetake.widget.StaticLayoutUtil;
import cn.nineton.onetake.widget.VideoEditHelper;

public class VideoModelHelper {
    private static final String TAG = VideoModelHelper.class.getSimpleName();

    public static void refreshVideoFileExists(List<LongVideosModel> modelList) {
        if (modelList != null) {
            for (LongVideosModel lvm : modelList) {
                String videoPath = lvm.getOriginalMediaPath();
                if (lvm.mediaType != 0) {
                    lvm.setVideoFileExist(true);
                } else if (!TextUtil.isValidate(videoPath)) {
                    lvm.setVideoFileExist(false);
                } else if (new File(videoPath).exists()) {
                    lvm.setVideoFileExist(true);
                } else {
                    lvm.setVideoFileExist(false);
                }
            }
        }
    }

    public static void removeInvalidVideoModel(List<LongVideosModel> longVideosModels) {
        if (longVideosModels != null) {
            for (int i = longVideosModels.size() - 1; i > -1; i--) {
                if (((LongVideosModel) longVideosModels.get(i)).getCurrentDuration() < 500) {
                    longVideosModels.remove(i);
                }
            }
        }
    }

    public static boolean removeInvalidAudioModel(List<LongVideosModel> audioLongVideoModels, float videoSumTime) {
        boolean hasInvalid = false;
        if (TextUtil.isValidate(audioLongVideoModels)) {
            for (int i = audioLongVideoModels.size() - 1; i >= 0; i--) {
                LongVideosModel model = (LongVideosModel) audioLongVideoModels.get(i);
                if (!model.isJustSeeForAudio() && ((float) (model.getAudioStartTime() + model.getAudioDuration())) > videoSumTime) {
                    audioLongVideoModels.remove(i);
                    hasInvalid = true;
                } else if (model.getAudioDuration() < ((long) VideoEditHelper.MIN_AUDIO_TIME_DURATION_MS)) {
                    audioLongVideoModels.remove(i);
                    hasInvalid = true;
                }
            }
        }
        return hasInvalid;
    }

    public static void removeInvalidTextModel(List<LongVideosModel> textVideosModels) {
        if (!TextUtil.isNull(textVideosModels)) {
            for (int i = textVideosModels.size() - 1; i >= 0; i--) {
                LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                if (model.getStartTimeMs() < 0 || model.getCurrentDurationValue() < 1000 || model.isHideForSpecial()) {
                    textVideosModels.remove(i);
                }
            }
        }
    }

    public static void removeInvalidTextModelLowLevel(List<LongVideosModel> textVideosModels) {
        if (!TextUtil.isNull(textVideosModels)) {
            for (int i = textVideosModels.size() - 1; i >= 0; i--) {
                LongVideosModel model = (LongVideosModel) textVideosModels.get(i);
                if (model.getStartTimeMs() < 0 || model.getCurrentDurationValue() < 0) {
                    textVideosModels.remove(i);
                }
            }
        }
    }

    public static long getVideosSumTime(List<LongVideosModel> longVideosModels) {
        long sum = 0;
        if (TextUtil.isNull(longVideosModels)) {
            return 0;
        }
        for (LongVideosModel model : longVideosModels) {
            sum += model.getCurrentDuration();
        }
        return sum;
    }

    public static float getInsertSeekTo(int insertPosition, List<LongVideosModel> longVideosModels) {
        float result = StaticLayoutUtil.DefaultSpacingadd;
        long sumTime = 0;
        long insertTime = 0;
        if (TextUtil.isValidate(longVideosModels)) {
            int size = longVideosModels.size();
            for (int i = 0; i < size; i++) {
                sumTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
                if (i < insertPosition) {
                    insertTime += ((LongVideosModel) longVideosModels.get(i)).getCurrentDuration();
                }
            }
        }
        if (sumTime != 0) {
            result = (((float) (1 + insertTime)) * 1.0f) / ((float) sumTime);
        }
        if (result > 1.0f) {
            result = 1.0f;
        }
        if (result < StaticLayoutUtil.DefaultSpacingadd) {
            return StaticLayoutUtil.DefaultSpacingadd;
        }
        return result;
    }

    public static void refreshAudioDurationAfterVideoSpeedSet(long beforeStartTime, long beforeEndTime, long currentDuration, List<LongVideosModel> audioLongVideoModels) {
        long nowEndTime = beforeStartTime + currentDuration;
        long changeTime = nowEndTime - beforeEndTime;
        if (changeTime != 0 && !TextUtil.isNull(audioLongVideoModels)) {
            LogUtil.d(TAG, String.format("recordVideoEditModelData nowEndTime : %s , changeTime : %s ", new Object[]{Long.valueOf(nowEndTime), Long.valueOf(changeTime)}));
            for (LongVideosModel audioModel : audioLongVideoModels) {
                if (audioModel.getAudioStartTime() >= beforeEndTime) {
                    audioModel.setAudioStartTime(audioModel.getAudioStartTime() + changeTime);
                } else if (audioModel.getAudioStartTime() == beforeStartTime && audioModel.getAudioStartTime() + audioModel.getAudioDuration() == beforeEndTime) {
                    audioModel.setAudioDuration(nowEndTime - beforeStartTime);
                } else if (audioModel.getAudioStartTime() < beforeEndTime && audioModel.getAudioStartTime() + audioModel.getAudioDuration() > beforeEndTime) {
                    audioModel.setAudioDuration(audioModel.getAudioDuration() + ((nowEndTime - audioModel.getAudioStartTime()) - (beforeEndTime - audioModel.getAudioStartTime())));
                } else if (audioModel.getAudioStartTime() <= beforeStartTime || audioModel.getAudioStartTime() + audioModel.getAudioDuration() > beforeEndTime) {
                    if (audioModel.getAudioStartTime() >= beforeStartTime || audioModel.getAudioStartTime() + audioModel.getAudioDuration() <= beforeStartTime || audioModel.getAudioStartTime() + audioModel.getAudioDuration() > beforeEndTime) {
                        if (audioModel.getAudioStartTime() < beforeStartTime && audioModel.getAudioStartTime() + audioModel.getAudioDuration() > beforeEndTime) {
                            audioModel.setAudioDuration(audioModel.getAudioDuration() + changeTime);
                        }
                    } else if (audioModel.getAudioStartTime() + audioModel.getAudioDuration() > nowEndTime) {
                        audioModel.setAudioDuration(nowEndTime - audioModel.getAudioStartTime());
                    }
                } else if (audioModel.getAudioStartTime() + audioModel.getAudioDuration() > nowEndTime) {
                    audioModel.setAudioDuration(nowEndTime - audioModel.getAudioStartTime());
                }
            }
        }
    }

    public static void refreshAudioModelVolumes(List<LongVideosModel> videoLongVideosModels, int videoSize, LongVideosModel audioModel) {
        if (!TextUtil.isNull(videoLongVideosModels) && audioModel != null) {
            long audioStartTime = audioModel.getAudioStartTime();
            long audioEndTime = audioStartTime + audioModel.getAudioDuration();
            ArrayList<LongVideosModel.AudioVolume> oldVolumes = audioModel.getAudioVolumes();
            ArrayList<LongVideosModel.AudioVolume> audioCacheVolumes = audioModel.getAudioCacheVolumes();
            if (oldVolumes != null) {
                LogUtil.d(TAG, String.format("oldVolumes : %s ", new Object[]{oldVolumes}));
            }
            ArrayList<LongVideosModel.AudioVolume> audioVolumes = new ArrayList();
            audioModel.setAudioVolumes(audioVolumes);
            LongVideosModel.AudioVolume audioVolume = null;
            long videoSumTime = 0;
            for (int j = 0; j < videoSize; j++) {
                LongVideosModel videosModel = (LongVideosModel) videoLongVideosModels.get(j);
                long videoStartTime = videoSumTime;
                long videoEndTime = videoSumTime + videosModel.getCurrentDuration();
                if (videoEndTime > audioStartTime && videoStartTime < audioEndTime) {
                    if (videoEndTime < audioEndTime) {
                        audioVolume = new LongVideosModel.AudioVolume();
                        if (videoStartTime > audioStartTime) {
                            audioVolume.setStartTime(videoStartTime);
                        } else {
                            audioVolume.setStartTime(audioStartTime);
                        }
                        audioVolume.setEndTime(videoEndTime);
                    } else {
                        audioVolume = new LongVideosModel.AudioVolume();
                        if (videoStartTime > audioStartTime) {
                            audioVolume.setStartTime(videoStartTime);
                        } else {
                            audioVolume.setStartTime(audioStartTime);
                        }
                        audioVolume.setEndTime(audioEndTime);
                    }
                    audioVolume.setVolume(audioModel.getAudioVolume());
                    audioVolume.setUserChooseStart((long) ((audioModel.getTrueStartTime() * 1000.0f) * 1000.0f));
                    LongVideosModel.AudioVolume resultVolume = checkIsInCacheAudioVolumes(audioCacheVolumes, audioVolume.getStartTime(), audioVolume.getEndTime());
                    if (resultVolume == null) {
                        resultVolume = getAudioVolumeFromOldAudioVolume(oldVolumes, audioVolume.getStartTime(), audioVolume.getEndTime());
                    }
                    if (resultVolume != null) {
                        audioVolume.setVolume(resultVolume.getVolume());
                        audioVolume.setSelected(resultVolume.isSelected());
                    } else {
                        audioVolume.setVolume(audioModel.getAudioVolume());
                    }
                    audioVolumes.add(audioVolume);
                }
                if (audioVolume != null && audioVolume.getStartTime() == videoStartTime && audioVolume.getEndTime() == videoEndTime && videosModel.isHasEditAudioVolume()) {
                    audioVolume.setVolume(videosModel.getAudioVolume());
                }
                videoSumTime += videosModel.getCurrentDuration();
            }
            checkAudioVolumesSelected(audioVolumes);
            if (audioModel.isJustSeeForAudio()) {
                audioVolume = new LongVideosModel.AudioVolume();
                audioVolume.setStartTime(audioModel.getAudioStartTime());
                audioVolume.setEndTime(audioModel.getAudioStartTime() + audioModel.getAudioDuration());
                audioVolume.setVolume(audioModel.getAudioVolume());
                audioVolumes.add(audioVolume);
            }
        }
    }

    private static void checkAudioVolumesSelected(ArrayList<LongVideosModel.AudioVolume> audioVolumes) {
        if (!TextUtil.isNull(audioVolumes)) {
            int i;
            LongVideosModel.AudioVolume audioVolume;
            int size = audioVolumes.size();
            int needPos = -1;
            LongVideosModel.AudioVolume selectVolume = null;
            boolean hasFind = false;
            for (i = 0; i < size; i++) {
                audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
                if (!audioVolume.isSelected()) {
                    audioVolume.setSelected(false);
                } else if (hasFind) {
                    audioVolume.setSelected(false);
                } else {
                    selectVolume = audioVolume;
                    needPos = i;
                    hasFind = true;
                }
            }
            if (selectVolume != null) {
                for (i = needPos - 1; i >= 0; i--) {
                    audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
                    if (audioVolume.getVolume() != selectVolume.getVolume()) {
                        break;
                    }
                    audioVolume.setSelected(true);
                }
                i = needPos + 1;
                while (i < size) {
                    audioVolume = (LongVideosModel.AudioVolume) audioVolumes.get(i);
                    if (audioVolume.getVolume() == selectVolume.getVolume()) {
                        audioVolume.setSelected(true);
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private static LongVideosModel.AudioVolume checkIsInCacheAudioVolumes(ArrayList<LongVideosModel.AudioVolume> audioCacheVolumes, long startTime, long endTime) {
        if (TextUtil.isValidate(audioCacheVolumes)) {
            Iterator it = audioCacheVolumes.iterator();
            while (it.hasNext()) {
                LongVideosModel.AudioVolume audioVolume = (LongVideosModel.AudioVolume) it.next();
                if (startTime >= audioVolume.getStartTime() && endTime <= audioVolume.getEndTime()) {
                    return audioVolume;
                }
            }
        }
        return null;
    }

    private static LongVideosModel.AudioVolume getAudioVolumeFromOldAudioVolume(ArrayList<LongVideosModel.AudioVolume> oldVolumes, long startTime, long endTime) {
        if (TextUtil.isValidate(oldVolumes)) {
            Iterator it = oldVolumes.iterator();
            while (it.hasNext()) {
                LongVideosModel.AudioVolume audioVolume = (LongVideosModel.AudioVolume) it.next();
                if ((startTime == audioVolume.getStartTime() && endTime == audioVolume.getEndTime()) || startTime == audioVolume.getStartTime()) {
                    return audioVolume;
                }
                if (endTime == audioVolume.getEndTime()) {
                    return audioVolume;
                }
            }
        }
        return null;
    }

    public static void changeRelateAudioData(long curSlideSumTime, long textChangeTime, List<LongVideosModel> audioLongVideoModels) {
        if (!TextUtil.isNull(audioLongVideoModels)) {
            LongVideosModel audioModel;
            int endRelateAudioPos = -2;
            int size = audioLongVideoModels.size();
            int i = 0;
            while (i < size) {
                audioModel = (LongVideosModel) audioLongVideoModels.get(i);
                long audioStartTime = audioModel.getAudioStartTime();
                long audioEndTime = audioModel.getAudioStartTime() + audioModel.getAudioDuration();
                if (curSlideSumTime >= audioStartTime && curSlideSumTime <= audioEndTime) {
                    endRelateAudioPos = i;
                    audioModel.setAudioDuration(audioModel.getAudioDuration() + textChangeTime);
                    break;
                } else if (audioStartTime > curSlideSumTime) {
                    endRelateAudioPos = i - 1;
                    break;
                } else {
                    i++;
                }
            }
            if (endRelateAudioPos != -2 && endRelateAudioPos + 1 >= 0) {
                for (i = endRelateAudioPos + 1; i < size; i++) {
                    audioModel = (LongVideosModel) audioLongVideoModels.get(i);
                    audioModel.setAudioStartTime(audioModel.getAudioStartTime() + textChangeTime);
                }
            }
        }
    }

    public static void refreshAudioHolderMute(LongVideosModel model, long audioMuteDuration, int audioMuteStartPos, int audioMuteEndPos, float muteTextWidth, ArrayList<VideoEditImageEntity> mAudioEntities) {
        if (model != null && !TextUtil.isNull(mAudioEntities)) {
            LogUtil.d(TAG, String.format("audioMuteDuration : %s , audioMuteStartPos : %s , audioMuteEndPos : %s ", new Object[]{Long.valueOf(audioMuteDuration), Integer.valueOf(audioMuteStartPos), Integer.valueOf(audioMuteEndPos)}));
            int start = audioMuteStartPos;
            int end = audioMuteEndPos;
            float textWidth = muteTextWidth * 1.0f;
            float sumLeftMargin = ((float) (((int) (((float) VideoEditHelper.IMAGE_UNIT_WIDTH) * VideoEditHelper.div((float) audioMuteDuration, 1000.0f))) / 2)) - (textWidth / 2.0f);
            int needPos = audioMuteStartPos + ((int) (sumLeftMargin / ((float) VideoEditHelper.IMAGE_UNIT_WIDTH)));
            float endLeftMargin = sumLeftMargin % ((float) VideoEditHelper.IMAGE_UNIT_WIDTH);
            if (endLeftMargin + textWidth > ((float) VideoEditHelper.IMAGE_UNIT_WIDTH)) {
                needPos++;
                endLeftMargin -= (float) VideoEditHelper.IMAGE_UNIT_WIDTH;
            }
            clearMuteTvShowStatus(start, end, mAudioEntities);
            VideoEditImageEntity needEntity = (VideoEditImageEntity) mAudioEntities.get(needPos);
            needEntity.setShowMuteTv(true);
            needEntity.setMuteTvLeftMargin((int) endLeftMargin);
            LogUtil.d(TAG, String.format("needPos : %s , endLeftMargin : %s ", new Object[]{Integer.valueOf(needPos), Float.valueOf(endLeftMargin)}));
        }
    }

    public static void clearMuteTvShowStatus(int start, int end, ArrayList<VideoEditImageEntity> mAudioEntities) {
        if (!TextUtil.isNull(mAudioEntities)) {
            int size = mAudioEntities.size();
            if (start >= 0 && start < size && end >= 0 && end < size) {
                for (int i = start; i <= end; i++) {
                    ((VideoEditImageEntity) mAudioEntities.get(i)).setShowMuteTv(false);
                }
            }
        }
    }

    public static void refreshVideoHolderMute(LongVideosModel model, int videoMuteStartPos, int videoMuteEndPos, float muteTextWidth, ArrayList<VideoEditImageEntity> mImageEntities) {
        if (model != null && !TextUtil.isNull(mImageEntities)) {
            int start = videoMuteStartPos;
            int end = videoMuteEndPos;
            float showSum = VideoEditHelper.div((float) model.getCurrentDuration(), 1000.0f);
            int sumWidth = (int) (((float) VideoEditHelper.IMAGE_UNIT_WIDTH) * showSum);
            float textWidth = muteTextWidth;
            float startX = (((float) (sumWidth / 2)) - (textWidth / 2.0f)) / ((float) sumWidth);
            float endX = (((float) (sumWidth / 2)) + (textWidth / 2.0f)) / ((float) sumWidth);
            float curShowSum = StaticLayoutUtil.DefaultSpacingadd;
            for (int i = start; i <= end; i++) {
                VideoEditImageEntity entity = (VideoEditImageEntity) mImageEntities.get(i);
                float showStartX = curShowSum / showSum;
                curShowSum += entity.getShowEnd() - entity.getShowStart();
                float showEndX = curShowSum / showSum;
                if (startX >= showStartX && endX <= showEndX) {
                    entity.setShowMuteTv(true);
                    entity.setMuteTvLeftMargin((int) ((startX - showStartX) * ((float) sumWidth)));
                } else if (startX <= showEndX && endX > showEndX) {
                    entity.setShowMuteTv(false);
                    int nextPos = i + 1;
                    if (nextPos >= 0 && nextPos < mImageEntities.size()) {
                        VideoEditImageEntity next = (VideoEditImageEntity) mImageEntities.get(nextPos);
                        next.setShowMuteTv(true);
                        next.setMuteTvLeftMargin(-((int) ((showEndX - startX) * ((float) sumWidth))));
                    } else {
                        return;
                    }
                } else if (startX > showStartX || endX <= showStartX || endX >= showEndX) {
                    entity.setShowMuteTv(false);
                }
            }
        }
    }

    public static void refreshVisibleRecyclerView(LinearLayoutManager layoutManager, Adapter adapter, int preCount, int sumSize) {
        int first;
        int last;
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int temp = firstVisibleItemPosition - preCount;
        if (temp >= 0) {
            first = temp;
        } else {
            first = 0;
        }
        temp = lastVisibleItemPosition + preCount;
        int end = sumSize - 1;
        if (temp <= end) {
            last = temp;
        } else {
            last = end;
        }
        adapter.notifyItemRangeChanged(first, (last - first) + 1);
    }

    public static void refreshMuteVolumeAreaAndHolder(LongVideosModel model, long resultVolumeStart, long resultVolumeEnd, int muteStartPos, int muteEndPos, float muteTextWidth, ArrayList<VideoEditImageEntity> mAudioEntities) {
        if (!TextUtil.isNull(model) && !TextUtil.isNull(mAudioEntities)) {
            int i;
            LongVideosModel.AudioVolume audioVolume;
            int audioMuteStartPos = muteStartPos;
            int audioMuteEndPos = muteEndPos;
            if (muteStartPos - 1 >= 0) {
                for (i = muteStartPos - 1; i >= 0; i--) {
                    audioVolume = ((VideoEditImageEntity) mAudioEntities.get(i)).getAudioVolume();
                    if (audioVolume == null || audioVolume.getVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                        break;
                    }
                    audioMuteStartPos = i;
                    resultVolumeStart = audioVolume.getStartTime();
                }
            }
            int size = mAudioEntities.size();
            if (muteEndPos + 1 < size) {
                for (i = muteEndPos + 1; i < size; i++) {
                    audioVolume = ((VideoEditImageEntity) mAudioEntities.get(i)).getAudioVolume();
                    if (audioVolume == null || audioVolume.getVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                        break;
                    }
                    audioMuteEndPos = i;
                    resultVolumeEnd = audioVolume.getEndTime();
                }
            }
            refreshAudioHolderMute(model, resultVolumeEnd - resultVolumeStart, audioMuteStartPos, audioMuteEndPos, muteTextWidth, mAudioEntities);
        }
    }

    public static void refreshAudioMuteTv(LongVideosModel audioModel, ArrayList<VideoEditImageEntity> mAudioEntities, int start, int end, float muteTextWidth) {
        if (audioModel != null && !TextUtil.isNull(mAudioEntities)) {
            ArrayList<LongVideosModel.AudioVolume> audioVolumes = audioModel.getAudioVolumes();
            if (audioVolumes != null) {
                Iterator it = audioVolumes.iterator();
                while (it.hasNext()) {
                    LongVideosModel.AudioVolume audioVolumeModel = (LongVideosModel.AudioVolume) it.next();
                    int audioStartPos = end;
                    int audioEndPos = start;
                    if (audioVolumeModel.getVolume() == StaticLayoutUtil.DefaultSpacingadd) {
                        for (int i = start; i <= end; i++) {
                            if (((VideoEditImageEntity) mAudioEntities.get(i)).getAudioVolume() == audioVolumeModel) {
                                if (i < audioStartPos) {
                                    audioStartPos = i;
                                }
                                if (i > audioEndPos) {
                                    audioEndPos = i;
                                }
                            }
                        }
                        int audioMuteStartPos = audioStartPos;
                        int audioMuteEndPos = audioEndPos;
                        long resultVolumeStart = audioVolumeModel.getStartTime();
                        long resultVolumeEnd = audioVolumeModel.getEndTime();
                        LogUtil.d(TAG, String.format("audioMuteStartPos : %s , audioMuteEndPos : %s , resultVolumeStart : %s , resultVolumeEnd : %s ", new Object[]{Integer.valueOf(audioMuteStartPos), Integer.valueOf(audioMuteEndPos), Long.valueOf(resultVolumeStart), Long.valueOf(resultVolumeEnd)}));
                        refreshMuteVolumeAreaAndHolder(audioModel, resultVolumeStart, resultVolumeEnd, audioMuteStartPos, audioMuteEndPos, muteTextWidth, mAudioEntities);
                    }
                }
            }
        }
    }
}