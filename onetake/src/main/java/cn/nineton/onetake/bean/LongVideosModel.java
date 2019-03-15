package cn.nineton.onetake.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.media.TransportMediator;
import com.blink.academy.onetake.VideoTools.FilterView.VideoSpeedup;
import com.blink.academy.onetake.VideoTools.MediaUtils;
import com.blink.academy.onetake.VideoTools.Ratio;
import com.blink.academy.onetake.VideoTools.VideoTranscoder.Client;
import com.blink.academy.onetake.VideoTools.WaveformOutput;
import com.blink.academy.onetake.bean.VTFontDesBean;
import com.blink.academy.onetake.bean.audio.AudioTrackBean;
import com.blink.academy.onetake.bean.audio.AudioUpLoadInfoBean;
import com.blink.academy.onetake.bean.audio.AudioUploadModel;
import com.blink.academy.onetake.bean.proxy.ProxyFileInfo;
import com.blink.academy.onetake.model.video.VideoInputRatio;
import com.blink.academy.onetake.support.debug.LogUtil;
import com.blink.academy.onetake.support.events.proxy.ProxyProgressEvent;
import com.blink.academy.onetake.support.manager.ProxyVideoManager;
import com.blink.academy.onetake.support.utils.FilterViewUtils;
import com.blink.academy.onetake.support.utils.LocaleUtil;
import com.blink.academy.onetake.support.utils.NumberUtil;
import com.blink.academy.onetake.support.utils.TextUtil;
import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
import com.blink.academy.onetake.ui.helper.VideoEditHelper;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.AlignType;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.FontSizeType;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.LetterSpacingType;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.LineSpacingType;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.ShadowType;
import com.blink.academy.onetake.widgets.VideoText.VTContainerView.TextColorType;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LongVideosModel implements Parcelable {
    public static final Creator<LongVideosModel> CREATOR = new Creator<LongVideosModel>() {
        public LongVideosModel createFromParcel(Parcel in) {
            return new LongVideosModel(in);
        }

        public LongVideosModel[] newArray(int size) {
            return new LongVideosModel[size];
        }
    };
    public static final int MIN_DURATION_VALUE = 500;
    public static final int MODEL_TYPE_ACTIVITY = 4;
    public static final int MODEL_TYPE_AUDIO = 1;
    public static final int MODEL_TYPE_BITMAP = 2;
    public static final int MODEL_TYPE_TEXT = 3;
    public static final int MODEL_TYPE_VIDEO = 0;
    public static final int PRIVATE_FILTER = 1;
    public static final int PUBLIC_FILTER = 0;
    private static final String TAG = LongVideosModel.class.getSimpleName();
    private long audioCacheDuration;
    private long audioCacheStartTime;
    private ArrayList<AudioVolume> audioCacheVolumes;
    private float audioConfirmVolume;
    private float audioDefaultVolume;
    private long audioDuration;
    public MediaFormat audioFormat;
    private long audioStartTime;
    private float audioVolume;
    private long cacheDuration;
    private long cacheRelateStartTime;
    private long cacheStartTime;
    private Client chaplinClient;
    private int circleCoverType;
    private float curStartTime;
    private long currentDuration;
    private Client doubleClient;
    private long endMusicDuration;
    private long endMusicStart;
    private LongVideosModel endRelateTextModel;
    private int filterState;
    private List<List<String>> finalTexts;
    private List<Integer> hasChangedBGMVolumePosition;
    private boolean hasEditAudioVolume;
    private boolean hasEditVideoVolume;
    private boolean hideForSpecial;
    private VideoInputRatio inputRatio;
    public boolean isFrontCamera;
    public boolean isVideo;
    private boolean justSeeForAudio;
    private int letterSpacing;
    private int lineSpacing;
    private Bitmap localFileBitmap;
    private boolean longPressing;
    private AudioTrackBean mAudioTrackBean;
    private AudioUpLoadInfoBean mAudioUpLoadInfoBean;
    private ArrayList<AudioVolume> mAudioVolumes;
    public int mOriginWHPercent;
    private VideoEditImageEntity mVideoEditImageEntity;
    private WaveformOutput mWaveformOutput;
    public int mediaType;
    private long musicEndTimeInTimeLine;
    private long musicStartTimeInTimeLine;
    private float[] needData;
    private Client normalClient;
    private LongVideosModel pointToVideoModel;
    private String privateFilterName;
    private float privateIntensity;
    private HashMap<VideoSpeedup, ProxyFileInfo> proxyFileMap;
    private long proxyVideoCreateTime;
    private String publicFilterName;
    private float publicIntensity;
    private Client quadrupleClient;
    private float radius;
    private long relateStartTime;
    private boolean selectedForSplit;
    private int shadowColor;
    private float shadowDx;
    private float shadowDy;
    private boolean shouldVertical;
    private long showDurationValue;
    private long startTime;
    private long startTimeForTimeline;
    private int startY;
    private AlignType textAlignType;
    private TextColorType textColorType;
    private String textContent;
    private FontSizeType textFontSizeType;
    private LetterSpacingType textLetterSpacingType;
    private LineSpacingType textLineSpacingType;
    private int textPos;
    private ShadowType textShadowType;
    private float textSize;
    private VTFontDesBean textTypeface;
    private int textVerticalPos;
    private Client timelapseClient;
    public long totalDuration;
    private float trueStartTime;
    public UploadVideoBean uploadVideoBean;
    private long usageDurationValue;
    private long usageStartTime;
    public long verse;
    private int verticalPaddingSpacing;
    private float videoConfirmVolume;
    private float videoDefaultVolume;
    private float videoExposure;
    private int videoFPS;
    private boolean videoFileExist;
    public MediaFormat videoFormat;
    private int videoHeight;
    public int videoOrientation;
    private String videoPath;
    private int videoRotate;
    public VideoSpeedup videoSpeedUp;
    private long videoSumTime;
    public String videoTag;
    private float videoVolume;
    private int videoWidth;
    private int vtHeight;
    private int yOffset;
    private float zoomEnd;
    private float zoomStart;

    /* renamed from: com.blink.academy.onetake.bean.longvideo.LongVideosModel$3 */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$AlignType = new int[AlignType.values().length];

        static {
            try {
                $SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$AlignType[AlignType.L.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$AlignType[AlignType.M.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$AlignType[AlignType.R.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static class AudioVolume implements Parcelable {
        public static final Creator<AudioVolume> CREATOR = new Creator<AudioVolume>() {
            public AudioVolume createFromParcel(Parcel source) {
                return new AudioVolume(source);
            }

            public AudioVolume[] newArray(int size) {
                return new AudioVolume[size];
            }
        };
        long endTime;
        boolean selected;
        long startTime;
        long userChooseStart;
        float volume;

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public long getUserChooseStart() {
            return this.userChooseStart;
        }

        public void setUserChooseStart(long userChooseStart) {
            this.userChooseStart = userChooseStart;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AudioVolume that = (AudioVolume) o;
            if (this.startTime != that.startTime) {
                return false;
            }
            if (this.endTime != that.endTime) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((int) (this.startTime ^ (this.startTime >>> 32))) * 31) + ((int) (this.endTime ^ (this.endTime >>> 32)));
        }

        public String toString() {
            return "AudioVolume{startTime=" + this.startTime + ", endTime=" + this.endTime + ", volume=" + this.volume + '}';
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public float getVolume() {
            return this.volume;
        }

        public void setVolume(float volume) {
            this.volume = volume;
        }

        public long getDuration() {
            return getEndTime() - getStartTime();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.userChooseStart);
            dest.writeLong(this.startTime);
            dest.writeLong(this.endTime);
            dest.writeFloat(this.volume);
            dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        }

        protected AudioVolume(Parcel in) {
            this.userChooseStart = in.readLong();
            this.startTime = in.readLong();
            this.endTime = in.readLong();
            this.volume = in.readFloat();
            this.selected = in.readByte() != (byte) 0;
        }
    }

    public boolean isBlackModelType() {
        if (this.mediaType != 2) {
            return false;
        }
        if (TextUtil.isValidate(this.videoPath) && new File(this.videoPath).exists()) {
            return false;
        }
        return true;
    }

    public long getProxyVideoCreateTime() {
        return this.proxyVideoCreateTime;
    }

    public void setProxyVideoCreateTime(long proxyVideoCreateTime) {
        this.proxyVideoCreateTime = proxyVideoCreateTime;
    }

    public long getMusicStartTimeInTimeLine() {
        return this.musicStartTimeInTimeLine;
    }

    public void setMusicStartTimeInTimeLine(long musicStartTimeInTimeLine) {
        this.musicStartTimeInTimeLine = musicStartTimeInTimeLine;
    }

    public long getMusicEndTimeInTimeLine() {
        return this.musicEndTimeInTimeLine;
    }

    public void setMusicEndTimeInTimeLine(long musicEndTimeInTimeLine) {
        this.musicEndTimeInTimeLine = musicEndTimeInTimeLine;
    }

    public int getVideoFPS() {
        if (this.videoFPS == 0) {
            this.videoFPS = 30;
        }
        return this.videoFPS;
    }

    public void setVideoFPS(int videoFPS) {
        this.videoFPS = videoFPS;
    }

    public float getVideoExposure() {
        return getSetVideoExposure();
    }

    public float getRealVideoExposure() {
        return this.videoExposure;
    }

    public void setVideoExposure(float videoExposure) {
        this.videoExposure = videoExposure;
    }

    public void cacheAudioData() {
        setAudioCacheStartTime(getAudioStartTime());
        setAudioCacheDuration(getAudioDuration());
        setAudioCacheVolumes(getAudioVolumes());
    }

    public void clearAudioVolumesCache() {
        setAudioCacheVolumes(null);
    }

    private void setAudioCacheVolumes(ArrayList<AudioVolume> volumes) {
        if (this.audioCacheVolumes == null) {
            this.audioCacheVolumes = new ArrayList();
        }
        this.audioCacheVolumes.clear();
        if (volumes != null) {
            this.audioCacheVolumes.addAll(volumes);
        }
    }

    public ArrayList<AudioVolume> getAudioCacheVolumes() {
        return this.audioCacheVolumes;
    }

    public boolean isAudioDataValid() {
        if (this.audioStartTime < 0 || this.audioDuration <= 0) {
            return false;
        }
        return true;
    }

    private void judgeProxyMapNull() {
        if (this.proxyFileMap == null) {
            this.proxyFileMap = new HashMap();
        }
    }

    public String getPlaylistMediaPath() {
        judgeProxyMapNull();
        if (this.mediaType != 0) {
            return this.videoPath;
        }
        ProxyFileInfo proxyFileInfo = getProxyFileInfo();
        if (proxyFileInfo == null) {
            return this.videoPath;
        }
        String proxyPath = proxyFileInfo.getOutputVideoPath();
        if (new File(proxyPath).exists()) {
            return proxyPath;
        }
        return this.videoPath;
    }

    private synchronized ProxyFileInfo getProxyFileInfo() {
        return (ProxyFileInfo) this.proxyFileMap.get(this.videoSpeedUp);
    }

    public String getOriginalMediaPath() {
        return this.videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getAudioCacheStartTime() {
        return this.audioCacheStartTime;
    }

    public void setAudioCacheStartTime(long audioCacheStartTime) {
        this.audioCacheStartTime = audioCacheStartTime;
    }

    public long getAudioCacheDuration() {
        return this.audioCacheDuration;
    }

    public void setAudioCacheDuration(long audioCacheDuration) {
        this.audioCacheDuration = audioCacheDuration;
    }

    public long getAudioStartTime() {
        return this.audioStartTime;
    }

    public void setAudioStartTime(long audioStartTime) {
        this.audioStartTime = audioStartTime;
        setTimeLineStartTime(1000 * audioStartTime);
    }

    public long getAudioDuration() {
        return this.audioDuration;
    }

    public void setAudioDuration(long audioDuration) {
        this.audioDuration = audioDuration;
        setCurrentDuration(audioDuration);
    }

    public boolean isJustSeeForAudio() {
        return this.justSeeForAudio;
    }

    public void setJustSeeForAudio(boolean justSeeForAudio) {
        this.justSeeForAudio = justSeeForAudio;
    }

    public VideoEditImageEntity getVideoEditImageEntity() {
        return this.mVideoEditImageEntity;
    }

    public void setVideoEditImageEntity(VideoEditImageEntity videoEditImageEntity) {
        this.mVideoEditImageEntity = videoEditImageEntity;
    }

    public boolean isShouldVertical() {
        return this.shouldVertical;
    }

    public void setShouldVertical(boolean shouldVertical) {
        this.shouldVertical = shouldVertical;
    }

    public long getEndMusicStart() {
        return this.endMusicStart;
    }

    public void setVideoFileExist(boolean videoFileExist) {
        this.videoFileExist = videoFileExist;
    }

    public boolean isVideoFileExist() {
        return this.videoFileExist;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getOriVideoWidth() {
        return this.videoWidth;
    }

    public int getOriVideoHeight() {
        return this.videoHeight;
    }

    public float getPublicIntensity() {
        return this.publicIntensity;
    }

    public void setPublicIntensity(float publicIntensity) {
        this.publicIntensity = publicIntensity;
    }

    public float getPrivateIntensity() {
        return this.privateIntensity;
    }

    public void setPrivateIntensity(float privateIntensity) {
        this.privateIntensity = privateIntensity;
    }

    public int getFilterState() {
        return this.filterState;
    }

    public void setFilterState(int filterState) {
        this.filterState = filterState;
    }

    public String getPublicFilterName() {
        return this.publicFilterName;
    }

    public void setPublicFilterName(String publicFilterName) {
        this.publicFilterName = publicFilterName;
    }

    public String getPrivateFilterName() {
        return this.privateFilterName;
    }

    public void setPrivateFilterName(String privateFilterName) {
        this.privateFilterName = privateFilterName;
    }

    public int getVideoWidth() {
        if (this.videoRotate == 90 || this.videoRotate == 270) {
            return this.videoHeight;
        }
        return this.videoWidth;
    }

    public int getVideoHeight() {
        if (this.videoRotate == 90 || this.videoRotate == 270) {
            return this.videoWidth;
        }
        return this.videoHeight;
    }

    public void resetVideoInputRotateOnlyLocal() {
        this.inputRatio = new VideoInputRatio(getVideoHeight(), getVideoWidth());
    }

    public VideoInputRatio getInputRatioWithRotate() {
        VideoInputRatio ratio = new VideoInputRatio();
        if (this.inputRatio == null) {
            ratio.ratioHeight = getVideoHeight();
            ratio.ratioWidth = getVideoWidth();
        } else {
            int width = this.inputRatio.ratioWidth;
            ratio.ratioHeight = this.inputRatio.ratioHeight;
            ratio.ratioWidth = width;
        }
        return ratio;
    }

    public int getVideoRotate() {
        return this.videoRotate;
    }

    public void setVideoRotate(int videoRotate) {
        this.videoRotate = videoRotate;
    }

    public void setTimeLineStartTime(long startTimeForTimeline) {
        this.startTimeForTimeline = startTimeForTimeline;
    }

    public long getTimeLineStartTimeMs() {
        return (long) ((((float) this.startTimeForTimeline) * 1.0f) / 1000.0f);
    }

    public long getTimeLineStartTimeUs() {
        return this.startTimeForTimeline;
    }

    public int getVtHeight() {
        return this.vtHeight;
    }

    public void setVtHeight(int height) {
        this.vtHeight = height;
    }

    public int getStartY() {
        return this.startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public List<List<String>> getFinalTexts() {
        return this.finalTexts;
    }

    public void setFinalTexts(List<List<String>> finalTexts) {
        this.finalTexts = finalTexts;
    }

    public float getShadowDx() {
        return this.shadowDx;
    }

    public void setShadowDx(float shadowDx) {
        this.shadowDx = shadowDx;
    }

    public void setShadowDy(float shadowDy) {
        this.shadowDy = shadowDy;
    }

    public float getShadowDy() {
        return this.shadowDy;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getyOffset() {
        return this.yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getVerticalPaddingSpacing() {
        return this.verticalPaddingSpacing;
    }

    public void setVerticalPaddingSpacing(int verticalPaddingSpacing) {
        this.verticalPaddingSpacing = verticalPaddingSpacing;
    }

    public int getLetterSpacing() {
        return this.letterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public int getLineSpacing() {
        return this.lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public TextColorType getTextColorType() {
        return this.textColorType;
    }

    public void setTextColorType(TextColorType textColorType) {
        this.textColorType = textColorType;
    }

    public void setTextLineSpacingType(LineSpacingType textLineSpacingType) {
        this.textLineSpacingType = textLineSpacingType;
    }

    public LineSpacingType getTextLineSpacingType() {
        return this.textLineSpacingType;
    }

    public FontSizeType getTextFontSizeType() {
        return this.textFontSizeType;
    }

    public void setTextFontSizeType(FontSizeType textFontSizeType) {
        this.textFontSizeType = textFontSizeType;
    }

    public AlignType getTextAlignType() {
        return this.textAlignType;
    }

    public void setTextAlignType(AlignType textAlignType) {
        this.textAlignType = textAlignType;
    }

    public int getTextVerticalPos() {
        return this.textVerticalPos;
    }

    public void setTextVerticalPos(int textVerticalPos) {
        this.textVerticalPos = textVerticalPos;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public LetterSpacingType getTextLetterSpacingType() {
        return this.textLetterSpacingType;
    }

    public void setTextLetterSpacingType(LetterSpacingType textLetterSpacingType) {
        this.textLetterSpacingType = textLetterSpacingType;
    }

    public VTFontDesBean getTextTypeface() {
        return this.textTypeface;
    }

    public void setTextTypeface(VTFontDesBean textTypeface) {
        this.textTypeface = textTypeface;
    }

    public ShadowType getTextShadowType() {
        return this.textShadowType;
    }

    public void setTextShadowType(ShadowType textShadowType) {
        this.textShadowType = textShadowType;
    }

    public VideoSpeedup getVideoSpeedUp() {
        return this.videoSpeedUp;
    }

    public void setVideoSpeedUp(VideoSpeedup videoSpeedUp) {
        this.videoSpeedUp = videoSpeedUp;
        refreshDurationShowValue();
        refreshDurationTrueValue();
    }

    public void setVideoDefaultVolume(float videoDefaultVolume) {
        this.videoDefaultVolume = videoDefaultVolume;
    }

    public float getVideoDefaultVolume() {
        return this.videoDefaultVolume;
    }

    public void setAudioDefaultVolume(float audioDefaultVolume) {
        this.audioDefaultVolume = audioDefaultVolume;
    }

    public float getAudioDefaultVolume() {
        return this.audioDefaultVolume;
    }

    public void setVideoConfirmVolume(float videoConfirmVolume) {
        this.videoConfirmVolume = videoConfirmVolume;
    }

    public float getVideoConfirmVolume() {
        return this.videoConfirmVolume;
    }

    public void setAudioConfirmVolume(float audioConfirmVolume) {
        this.audioConfirmVolume = audioConfirmVolume;
    }

    public float getAudioConfirmVolume() {
        return this.audioConfirmVolume;
    }

    public LongVideosModel(String videoPath, boolean isFrontCamera, boolean shouldVertical) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.videoPath = videoPath;
        this.isFrontCamera = isFrontCamera;
        this.shouldVertical = shouldVertical;
        this.videoFileExist = true;
    }

    public void setEndMusicStartAndDuration(long start, long duration) {
        this.endMusicStart = start;
        this.endMusicDuration = duration;
    }

    public void setEndMusicStart(long endMusicStart) {
        this.endMusicStart = endMusicStart;
    }

    public long getEndMusicDuration() {
        return this.endMusicDuration;
    }

    public void setEndMusicDuration(long endMusicDuration) {
        this.endMusicDuration = endMusicDuration;
    }

    public boolean judgeChangedBGMvolum() {
        return this.hasChangedBGMVolumePosition != null && this.hasChangedBGMVolumePosition.size() > 0;
    }

    public void addChangeBGMVolumPosition(int position) {
        this.hasChangedBGMVolumePosition.add(Integer.valueOf(position));
    }

    public List<Integer> getChangeBGMVolumePosition() {
        return this.hasChangedBGMVolumePosition;
    }

    public float getVideoVolume() {
        return this.hasEditVideoVolume ? this.videoVolume : this.videoDefaultVolume;
    }

    public void setVideoVolume(float videoDefaultVolume) {
        this.videoVolume = videoDefaultVolume;
    }

    public float getAudioVolume() {
        return this.hasEditAudioVolume ? this.audioVolume : this.audioDefaultVolume;
    }

    public void setAudioVolume(float audioDefaultVolume) {
        if (!this.hasEditAudioVolume) {
            this.hasEditAudioVolume = true;
        }
        this.audioVolume = audioDefaultVolume;
    }

    public void setTextBitmap(Bitmap bitmap) {
        this.localFileBitmap = bitmap;
    }

    public LongVideosModel(int mediaType) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.mediaType = mediaType;
        this.videoFileExist = true;
        if (mediaType == 2) {
            this.startTime = 0;
            this.currentDuration = 3000;
            this.totalDuration = Long.MAX_VALUE;
        } else if (mediaType == 3) {
            this.currentDuration = 2000;
            this.totalDuration = Long.MAX_VALUE;
        }
        this.videoFormat = null;
        this.audioFormat = null;
    }

    public Bitmap getBitmap() {
        return this.localFileBitmap;
    }

    public void setBitmapNull() {
        this.localFileBitmap = null;
    }

    public LongVideosModel(int mediaType, String bitmapPath) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.mediaType = mediaType;
        this.videoFileExist = true;
        this.startTime = 0;
        this.currentDuration = 3000;
        this.totalDuration = Long.MAX_VALUE;
        this.videoPath = bitmapPath;
        this.videoFormat = null;
        this.audioFormat = null;
    }

    public LongVideosModel(int mediaType, int width, int height) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.mediaType = mediaType;
        this.videoFileExist = true;
        this.startTime = 0;
        this.currentDuration = 2000;
        this.totalDuration = Long.MAX_VALUE;
        this.videoWidth = width;
        this.videoHeight = height;
        this.videoFormat = null;
        this.audioFormat = null;
    }

    protected LongVideosModel(Parcel in) {
        TextColorType textColorType;
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.videoConfirmVolume = 1.0f;
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.audioConfirmVolume = 0.5f;
        this.videoPath = in.readString();
        this.totalDuration = in.readLong();
        this.startTime = in.readLong();
        this.currentDuration = in.readLong();
        this.showDurationValue = in.readLong();
        this.videoTag = in.readString();
        this.isVideo = in.readByte() != (byte) 0;
        this.zoomStart = in.readFloat();
        this.zoomEnd = in.readFloat();
        this.videoExposure = in.readFloat();
        this.audioStartTime = in.readLong();
        this.audioDuration = in.readLong();
        this.justSeeForAudio = in.readByte() != (byte) 0;
        this.videoVolume = in.readFloat();
        this.audioVolume = in.readFloat();
        this.verse = in.readLong();
        this.mediaType = in.readInt();
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.videoRotate = in.readInt();
        this.needData = in.createFloatArray();
        this.isFrontCamera = in.readByte() != (byte) 0;
        this.shouldVertical = in.readByte() != (byte) 0;
        this.videoOrientation = in.readInt();
        this.mAudioTrackBean = (AudioTrackBean) in.readParcelable(AudioTrackBean.class.getClassLoader());
        this.inputRatio = (VideoInputRatio) in.readParcelable(VideoInputRatio.class.getClassLoader());
        this.textPos = in.readInt();
        this.vtHeight = in.readInt();
        int tmpTFST = in.readInt();
        this.textFontSizeType = tmpTFST == -1 ? null : FontSizeType.values()[tmpTFST];
        int tmpTAT = in.readInt();
        this.textAlignType = tmpTAT == -1 ? null : AlignType.values()[tmpTAT];
        this.textVerticalPos = in.readInt();
        this.textContent = in.readString();
        int tmpTLST = in.readInt();
        this.textLetterSpacingType = tmpTLST == -1 ? null : LetterSpacingType.values()[tmpTLST];
        this.textTypeface = (VTFontDesBean) in.readParcelable(VTFontDesBean.class.getClassLoader());
        int tmpST = in.readInt();
        this.textShadowType = tmpST == -1 ? null : ShadowType.values()[tmpST];
        int tmpLST = in.readInt();
        this.textLineSpacingType = tmpLST == -1 ? null : LineSpacingType.values()[tmpLST];
        int tmpTCT = in.readInt();
        if (tmpTCT == -1) {
            textColorType = null;
        } else {
            textColorType = TextColorType.values()[tmpTCT];
        }
        this.textColorType = textColorType;
        this.textSize = in.readFloat();
        this.shadowColor = in.readInt();
        this.videoFPS = in.readInt();
        this.letterSpacing = in.readInt();
        this.lineSpacing = in.readInt();
        this.verticalPaddingSpacing = in.readInt();
        this.radius = in.readFloat();
        this.yOffset = in.readInt();
        this.shadowDx = in.readFloat();
        this.shadowDy = in.readFloat();
        this.startY = in.readInt();
        int finalTextsSize = in.readInt();
        if (finalTextsSize > 0) {
            this.finalTexts = new ArrayList();
            for (int i = 0; i < finalTextsSize; i++) {
                List<String> stringList = new ArrayList();
                in.readStringList(stringList);
                this.finalTexts.add(stringList);
            }
        } else {
            this.finalTexts = null;
        }
        if ("check_volume".equals(in.readString())) {
            this.videoConfirmVolume = in.readFloat();
            this.videoDefaultVolume = in.readFloat();
            this.audioDefaultVolume = in.readFloat();
            this.audioConfirmVolume = in.readFloat();
        }
        this.filterState = in.readInt();
        this.publicIntensity = in.readFloat();
        this.privateIntensity = in.readFloat();
        this.publicFilterName = in.readString();
        this.privateFilterName = in.readString();
        this.videoFileExist = in.readByte() != (byte) 0;
        this.mAudioVolumes = new ArrayList();
        in.readTypedList(this.mAudioVolumes, AudioVolume.CREATOR);
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.videoPath);
        dest.writeLong(this.totalDuration);
        dest.writeLong(this.startTime);
        dest.writeLong(this.currentDuration);
        dest.writeLong(this.showDurationValue);
        dest.writeString(this.videoTag);
        if (this.isVideo) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeFloat(this.zoomStart);
        dest.writeFloat(this.zoomEnd);
        dest.writeFloat(this.videoExposure);
        dest.writeLong(this.audioStartTime);
        dest.writeLong(this.audioDuration);
        if (this.justSeeForAudio) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeFloat(this.videoVolume);
        dest.writeFloat(this.audioVolume);
        dest.writeLong(this.verse);
        dest.writeInt(this.mediaType);
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeInt(this.videoRotate);
        dest.writeFloatArray(this.needData);
        if (this.isFrontCamera) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.shouldVertical) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        dest.writeInt(this.videoOrientation);
        dest.writeParcelable(this.mAudioTrackBean, flags);
        dest.writeParcelable(this.inputRatio, flags);
        dest.writeInt(this.textPos);
        dest.writeInt(this.vtHeight);
        dest.writeInt(this.textFontSizeType == null ? -1 : this.textFontSizeType.ordinal());
        dest.writeInt(this.textAlignType == null ? -1 : this.textAlignType.ordinal());
        dest.writeInt(this.textVerticalPos);
        dest.writeString(this.textContent);
        dest.writeInt(this.textLetterSpacingType == null ? -1 : this.textLetterSpacingType.ordinal());
        dest.writeParcelable(this.textTypeface, flags);
        dest.writeInt(this.textShadowType == null ? -1 : this.textShadowType.ordinal());
        dest.writeInt(this.textLineSpacingType == null ? -1 : this.textLineSpacingType.ordinal());
        if (this.textColorType == null) {
            i = -1;
        } else {
            i = this.textColorType.ordinal();
        }
        dest.writeInt(i);
        dest.writeFloat(this.textSize);
        dest.writeInt(this.shadowColor);
        dest.writeInt(this.videoFPS);
        dest.writeInt(this.letterSpacing);
        dest.writeInt(this.lineSpacing);
        dest.writeInt(this.verticalPaddingSpacing);
        dest.writeFloat(this.radius);
        dest.writeInt(this.yOffset);
        dest.writeFloat(this.shadowDx);
        dest.writeFloat(this.shadowDy);
        dest.writeInt(this.startY);
        if (TextUtil.isValidate(this.finalTexts)) {
            dest.writeInt(this.finalTexts.size());
            for (int i3 = 0; i3 < this.finalTexts.size(); i3++) {
                dest.writeStringList((List) this.finalTexts.get(i3));
            }
        } else {
            dest.writeInt(-1);
        }
        dest.writeString("check_volume");
        dest.writeFloat(this.videoConfirmVolume);
        dest.writeFloat(this.videoDefaultVolume);
        dest.writeFloat(this.audioDefaultVolume);
        dest.writeFloat(this.audioConfirmVolume);
        dest.writeInt(this.filterState);
        dest.writeFloat(this.publicIntensity);
        dest.writeFloat(this.privateIntensity);
        dest.writeString(this.publicFilterName);
        dest.writeString(this.privateFilterName);
        if (!this.videoFileExist) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeTypedList(this.mAudioVolumes);
    }

    public int describeContents() {
        return 0;
    }

    public void setCurrentDuration(long currentDuration) {
        this.currentDuration = currentDuration;
        refreshDurationShowValue();
    }

    public void setDurationShowValue(long showDurationValue) {
        this.showDurationValue = showDurationValue;
        refreshDurationTrueValue();
    }

    public void setZoomValue(int zoomState) {
        switch (zoomState) {
            case 0:
                this.zoomStart = 1.0f;
                this.zoomEnd = 1.0f;
                return;
            case 1:
                this.zoomStart = 1.0f;
                this.zoomEnd = 1.3f;
                return;
            case 2:
                this.zoomStart = 1.3f;
                this.zoomEnd = 1.0f;
                return;
            default:
                return;
        }
    }

    public float getZoomStart() {
        return this.zoomStart;
    }

    public float getZoomEnd() {
        return this.zoomEnd;
    }

    public long getTotalDuration() {
        return displayDuration(this.totalDuration);
    }

    public long getTotalDurationValue() {
        return this.totalDuration;
    }

    public long getCurrentDuration() {
        if (this.showDurationValue == 0) {
            refreshDurationShowValue();
        } else {
            this.showDurationValue = dealTimeValue(this.showDurationValue);
        }
        return this.showDurationValue;
    }

    private void refreshDurationShowValue() {
        this.showDurationValue = dealTimeValue(displayDuration(this.currentDuration));
        LogUtil.d(TAG, String.format("refreshDurationShowValue    currentDuration : %s , value : %s , showDurationValue : %s ", new Object[]{Long.valueOf(this.currentDuration), Long.valueOf(value), Long.valueOf(this.showDurationValue)}));
    }

    private void refreshDurationTrueValue() {
        this.currentDuration = (long) (((float) this.showDurationValue) * getSpeed());
    }

    private long dealTimeValue(long value) {
        return NumberUtil.discardTheLastTwoDigits(value);
    }

    public long getOriginalCurrentDuration() {
        return this.currentDuration;
    }

    public int reduceCurrentDuration(int number, boolean needMin) {
        LogUtil.d(TAG, String.format("reduceCurrentDuration time : %s ", new Object[]{Long.valueOf(((long) (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f)) * ((long) number))}));
        long minDuration = needMin ? 500 : 0;
        long duration = this.showDurationValue + time;
        if (minDuration > duration) {
            number = -((int) (((float) (this.showDurationValue - minDuration)) / (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f)));
            duration = this.showDurationValue + (((long) (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f)) * ((long) number));
        }
        this.showDurationValue = duration;
        refreshDurationTrueValue();
        return number;
    }

    private long getShowTotalDuration() {
        return dealTimeValue((long) (((float) this.totalDuration) / getSpeed()));
    }

    public int addCurrentDuration(int number) {
        long time = ((long) (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f)) * ((long) number);
        LogUtil.d(TAG, String.format("addCurrentDuration time : %s ", new Object[]{Long.valueOf(time)}));
        long duration = this.showDurationValue + time;
        long value = dealTimeValue((long) (((float) (this.totalDuration - this.startTime)) / getSpeed()));
        if (duration > value) {
            number = (int) (((float) (value - this.showDurationValue)) / (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f));
            duration = this.showDurationValue + (((long) (VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * 1000.0f)) * ((long) number));
        }
        this.showDurationValue = duration;
        refreshDurationTrueValue();
        return number;
    }

    public int addStartTime(int number, boolean needMin) {
        number = -reduceCurrentDuration(-number, needMin);
        setStartTime(this.startTime + (((long) ((VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * getSpeed()) * 1000.0f)) * ((long) number)));
        return number;
    }

    public int reduceStartTime(int number) {
        long newStartTime = this.startTime + (((long) ((VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * getSpeed()) * 1000.0f)) * ((long) number));
        if (newStartTime < 0) {
            number = (int) (((float) (-this.startTime)) / ((VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * getSpeed()) * 1000.0f));
            newStartTime = this.startTime + (((long) ((VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.floatValue() * getSpeed()) * 1000.0f)) * ((long) number));
        }
        setStartTime(newStartTime);
        addCurrentDuration(-number);
        return number;
    }

    public boolean isCanSlideRight() {
        long value = dealTimeValue((long) (((float) (this.totalDuration - this.startTime)) / getSpeed()));
        LogUtil.d(TAG, String.format("isCanSlideRight  totalDuration : %s , startTime : %s ,  leftDuration : %s , value : %s , current : %s ", new Object[]{Long.valueOf(this.totalDuration), Long.valueOf(this.startTime), Long.valueOf(leftDuration), Long.valueOf(value), Long.valueOf(getCurrentDuration())}));
        if (getCurrentDuration() < value) {
            return true;
        }
        return false;
    }

    public boolean isCanSlideLeft() {
        if (((float) this.startTime) >= 0.0f + VideoEditHelper.IMAGE_SLIDE_MIN_EXPANSION.multiply(BigDecimal.valueOf((double) (getSpeed() * 1000.0f))).setScale(1, 4).floatValue()) {
            return true;
        }
        return false;
    }

    public boolean isSelectedForSplit() {
        return this.selectedForSplit;
    }

    public void setSelectedForSplit(boolean selectedForSplit) {
        this.selectedForSplit = selectedForSplit;
    }

    public int getEffectsViewShowSpeedCount() {
        int count = VideoSpeedup.values().length;
        long currentDurationValue = getCurrentDurationValue();
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.TIMELAPSE) * 500.0f) {
            return count;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.QUADRUPLE) * 500.0f) {
            return count - 1;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.DOUBLE) * 500.0f) {
            return count - 2;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.CHAPLIN) * 500.0f) {
            return count - 3;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.NORMAL) * 500.0f) {
            return count - 4;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.EIGHTMM) * 500.0f) {
            return count - 4;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.SLOW) * 500.0f) {
            return count - 6;
        }
        if (((float) currentDurationValue) >= getSpeedByVideoSpeedup(VideoSpeedup.STEP) * 500.0f) {
            return count - 6;
        }
        return count;
    }

    public long getCurrentDurationValue() {
        return this.currentDuration;
    }

    private long displayDuration(long value) {
        return (long) (((float) value) / getSpeed());
    }

    public Ratio getVideoSpeed() {
        if (VideoSpeedup.STEP == this.videoSpeedUp) {
            return new Ratio(50, 100);
        }
        if (VideoSpeedup.SLOW == this.videoSpeedUp) {
            return new Ratio(50, 100);
        }
        if (VideoSpeedup.NORMAL == this.videoSpeedUp) {
            return new Ratio(100, 100);
        }
        if (getProxyFileInfo() != null) {
            return new Ratio(1, 1);
        }
        if (VideoSpeedup.CHAPLIN == this.videoSpeedUp) {
            return new Ratio(TransportMediator.KEYCODE_MEDIA_RECORD, 100);
        }
        if (VideoSpeedup.EIGHTMM == this.videoSpeedUp) {
            return new Ratio(100, 100);
        }
        if (VideoSpeedup.DOUBLE == this.videoSpeedUp) {
            return new Ratio(200, 100);
        }
        if (VideoSpeedup.QUADRUPLE == this.videoSpeedUp) {
            return new Ratio(400, 100);
        }
        if (VideoSpeedup.TIMELAPSE == this.videoSpeedUp) {
            return new Ratio(1500, 100);
        }
        return new Ratio(100, 100);
    }

    private long getPreferFPS() {
        if (VideoSpeedup.STEP == this.videoSpeedUp) {
            return 6;
        }
        if (VideoSpeedup.SLOW == this.videoSpeedUp) {
            return 30;
        }
        if (VideoSpeedup.NORMAL == this.videoSpeedUp) {
            return 30;
        }
        if (VideoSpeedup.CHAPLIN == this.videoSpeedUp) {
            return 16;
        }
        if (VideoSpeedup.EIGHTMM == this.videoSpeedUp) {
            return 16;
        }
        if (VideoSpeedup.DOUBLE == this.videoSpeedUp) {
            return 30;
        }
        if (VideoSpeedup.QUADRUPLE == this.videoSpeedUp) {
            return 30;
        }
        if (VideoSpeedup.TIMELAPSE == this.videoSpeedUp) {
            return 30;
        }
        return 30;
    }

    public void changeVideoWidthHeight(int oriWidth) {
    }

    public long getVideoFrameRate() {
        long frameRate;
        if (VideoSpeedup.STEP == this.videoSpeedUp) {
            frameRate = 6;
        } else if (VideoSpeedup.SLOW == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else if (VideoSpeedup.NORMAL == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else if (getProxyFileInfo() != null) {
            frameRate = 30;
        } else if (VideoSpeedup.CHAPLIN == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else if (VideoSpeedup.EIGHTMM == this.videoSpeedUp) {
            frameRate = 16;
        } else if (VideoSpeedup.DOUBLE == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else if (VideoSpeedup.QUADRUPLE == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else if (VideoSpeedup.TIMELAPSE == this.videoSpeedUp) {
            frameRate = (long) getVideoFPS();
        } else {
            frameRate = (long) getVideoFPS();
        }
        return 1000000 / frameRate;
    }

    public float getSpeed() {
        return getSpeedByVideoSpeedup(this.videoSpeedUp);
    }

    private float getSpeedByVideoSpeedup(VideoSpeedup speedup) {
        if (speedup == VideoSpeedup.STEP) {
            return 0.5f;
        }
        if (speedup == VideoSpeedup.SLOW) {
            return 0.5f;
        }
        if (speedup == VideoSpeedup.EIGHTMM) {
            return 1.0f;
        }
        if (speedup == VideoSpeedup.NORMAL) {
            return 1.0f;
        }
        if (speedup == VideoSpeedup.CHAPLIN) {
            return 1.3f;
        }
        if (speedup == VideoSpeedup.DOUBLE) {
            return 2.0f;
        }
        if (speedup == VideoSpeedup.QUADRUPLE) {
            return 4.0f;
        }
        if (speedup == VideoSpeedup.TIMELAPSE) {
            return 15.0f;
        }
        return 1.0f;
    }

    public LongVideosModel(int mediaType, String audioPath, long totalDuration, long verse) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.verse = 1000 * verse;
        this.videoPath = audioPath;
        this.totalDuration = totalDuration;
        this.isVideo = false;
        this.mediaType = mediaType;
        this.videoFileExist = true;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public LongVideosModel(int mediaType, String videoPath, long mDuration) {
        this.videoDefaultVolume = 1.0f;
        this.audioDefaultVolume = 0.5f;
        this.videoConfirmVolume = 1.0f;
        this.audioConfirmVolume = 0.5f;
        this.musicStartTimeInTimeLine = 0;
        this.musicEndTimeInTimeLine = 0;
        this.proxyVideoCreateTime = 0;
        this.proxyFileMap = new HashMap();
        this.videoFPS = 30;
        this.videoSpeedUp = VideoSpeedup.NORMAL;
        this.zoomStart = 1.0f;
        this.zoomEnd = 1.0f;
        this.videoExposure = 0.0f;
        this.videoVolume = 1.0f;
        this.audioVolume = 0.7f;
        this.verse = 0;
        this.hasChangedBGMVolumePosition = new ArrayList();
        this.videoFormat = null;
        this.audioFormat = null;
        this.endMusicStart = 0;
        this.endMusicDuration = 0;
        this.isFrontCamera = false;
        this.shouldVertical = false;
        this.videoOrientation = 0;
        this.uploadVideoBean = new UploadVideoBean(UploadVideoBean.TYPE_VIDEO);
        this.startTimeForTimeline = 0;
        this.videoRotate = 0;
        this.filterState = 0;
        this.publicFilterName = "";
        this.privateFilterName = "";
        this.publicIntensity = 1.0f;
        this.privateIntensity = 1.0f;
        this.mOriginWHPercent = 0;
        this.mediaType = 0;
        this.hasEditVideoVolume = false;
        this.hasEditAudioVolume = false;
        this.circleCoverType = 0;
        this.videoPath = videoPath;
        this.totalDuration = mDuration;
        this.currentDuration = FilterViewUtils.getUseDuration(mDuration);
        this.startTime = 0;
        this.isVideo = true;
        this.mediaType = mediaType;
        this.videoFileExist = true;
    }

    public LongVideosModel cloneVideoTypeModel() {
        LongVideosModel model = new LongVideosModel(this.mediaType);
        model.uploadVideoBean = this.uploadVideoBean.cloneData();
        model.videoPath = this.videoPath;
        model.totalDuration = this.totalDuration;
        model.startTime = this.startTime;
        model.currentDuration = this.currentDuration;
        model.videoTag = this.videoTag;
        model.videoSpeedUp = this.videoSpeedUp;
        model.isVideo = this.isVideo;
        model.zoomStart = this.zoomStart;
        model.zoomEnd = this.zoomEnd;
        model.videoExposure = this.videoExposure;
        model.audioStartTime = this.audioStartTime;
        model.audioDuration = this.audioDuration;
        model.justSeeForAudio = this.justSeeForAudio;
        model.proxyFileMap = this.proxyFileMap;
        model.videoVolume = getVideoVolume();
        model.audioVolume = getAudioVolume();
        if (this.mAudioVolumes != null) {
            model.mAudioVolumes = new ArrayList();
            Iterator it = this.mAudioVolumes.iterator();
            while (it.hasNext()) {
                AudioVolume volume = (AudioVolume) it.next();
                AudioVolume newVolume = new AudioVolume();
                newVolume.setSelected(volume.isSelected());
                newVolume.setVolume(volume.getVolume());
                newVolume.setEndTime(volume.getEndTime());
                newVolume.setStartTime(volume.getStartTime());
                newVolume.setUserChooseStart(volume.getUserChooseStart());
                model.mAudioVolumes.add(newVolume);
            }
        }
        model.verse = this.verse;
        model.videoFPS = this.videoFPS;
        model.videoFormat = this.videoFormat;
        model.audioFormat = this.audioFormat;
        model.endMusicStart = this.endMusicStart;
        model.endMusicDuration = this.endMusicDuration;
        model.isFrontCamera = this.isFrontCamera;
        model.shouldVertical = this.shouldVertical;
        model.videoWidth = this.videoWidth;
        model.videoHeight = this.videoHeight;
        model.inputRatio = cloneInputRatio();
        model.videoRotate = this.videoRotate;
        model.mOriginWHPercent = this.mOriginWHPercent;
        model.videoSumTime = this.videoSumTime;
        model.curStartTime = this.curStartTime;
        model.trueStartTime = this.trueStartTime;
        if (this.mAudioTrackBean != null) {
            model.mAudioTrackBean = this.mAudioTrackBean.cloneBean();
        }
        model.textPos = this.textPos;
        model.vtHeight = this.vtHeight;
        model.textFontSizeType = this.textFontSizeType;
        model.textAlignType = this.textAlignType;
        model.textVerticalPos = this.textVerticalPos;
        model.textContent = this.textContent;
        model.textLetterSpacingType = this.textLetterSpacingType;
        model.textTypeface = this.textTypeface == null ? null : this.textTypeface.cloneData();
        model.textShadowType = this.textShadowType;
        model.textLineSpacingType = this.textLineSpacingType;
        model.textColorType = this.textColorType;
        model.textSize = this.textSize;
        model.shadowColor = this.shadowColor;
        model.letterSpacing = this.letterSpacing;
        model.lineSpacing = this.lineSpacing;
        model.verticalPaddingSpacing = this.verticalPaddingSpacing;
        model.radius = this.radius;
        model.yOffset = this.yOffset;
        model.shadowDx = this.shadowDx;
        model.shadowDy = this.shadowDy;
        model.finalTexts = this.finalTexts;
        model.startY = this.startY;
        model.setHasEditAudioVolume(isHasEditAudioVolume());
        model.setHasEditVideoVolume(isHasEditVideoVolume());
        model.videoDefaultVolume = this.videoDefaultVolume;
        model.videoConfirmVolume = this.videoConfirmVolume;
        model.audioConfirmVolume = this.audioConfirmVolume;
        model.audioDefaultVolume = this.audioDefaultVolume;
        model.filterState = this.filterState;
        model.privateIntensity = this.privateIntensity;
        model.publicIntensity = this.publicIntensity;
        model.privateFilterName = this.privateFilterName;
        model.publicFilterName = this.publicFilterName;
        model.videoFileExist = this.videoFileExist;
        return model;
    }

    public boolean getFileState() {
        if (TextUtil.isNull(this.videoPath)) {
            return false;
        }
        return new File(this.videoPath).exists();
    }

    public LongVideosModel splitModel() {
        LongVideosModel longVideosModel = cloneVideoTypeModel();
        longVideosModel.totalDuration = this.totalDuration;
        longVideosModel.startTime = this.startTime;
        return longVideosModel;
    }

    public long getMediaStartTimeMs() {
        judgeProxyMapNull();
        if (this.mediaType != 0) {
            return this.startTime;
        }
        ProxyFileInfo proxyFileInfo = getProxyFileInfo();
        if (proxyFileInfo == null) {
            return this.startTime;
        }
        if (new File(proxyFileInfo.getOutputVideoPath()).exists()) {
            return proxyFileInfo.getTimeLineStartTimeMs();
        }
        return this.startTime;
    }

    public long getMediaStartTimeUs() {
        judgeProxyMapNull();
        if (this.mediaType != 0) {
            return this.startTime * 1000;
        }
        ProxyFileInfo proxyFileInfo = getProxyFileInfo();
        if (proxyFileInfo == null) {
            return this.startTime * 1000;
        }
        if (new File(proxyFileInfo.getOutputVideoPath()).exists()) {
            return proxyFileInfo.getTimeLineStartTimeUs();
        }
        return this.startTime * 1000;
    }

    public long getStartTimeMs() {
        return this.startTime;
    }

    public long getStartTimeUs() {
        return this.startTime * 1000;
    }

    public long getShowStartTime() {
        return (long) (((float) this.startTime) / getSpeed());
    }

    public String toString() {
        return "LongVideosModel{videoPath='" + this.videoPath + '\'' + ", totalDuration=" + this.totalDuration + ", startTime=" + this.startTime + ", currentDuration=" + this.currentDuration + ", videoTag='" + this.videoTag + '\'' + '}';
    }

    public float[] getNeedData() {
        return this.needData;
    }

    public WaveformOutput getWaveformOutput() {
        return this.mWaveformOutput;
    }

    public long getVideoSumTime() {
        return this.videoSumTime;
    }

    public void setVideoSumTime(long videoSumTime) {
        this.videoSumTime = videoSumTime;
    }

    public float getCurStartTime() {
        return this.curStartTime;
    }

    public void setCurStartTime(float curStartTime) {
        this.curStartTime = curStartTime;
        this.startTime = (long) (1000.0f * curStartTime);
    }

    public float getTrueStartTime() {
        return this.trueStartTime;
    }

    public void setTrueStartTime(float trueStartTime) {
        this.trueStartTime = trueStartTime;
        setCurStartTime(trueStartTime);
        if (getAudioUpLoadInfoBean() != null) {
            getAudioUpLoadInfoBean().setStart(String.valueOf(getTrueStartTime()));
        }
    }

    public AudioTrackBean getAudioTrackBean() {
        return this.mAudioTrackBean;
    }

    public void setAudioTrackBean(AudioTrackBean audioTrackBean) {
        this.mAudioTrackBean = audioTrackBean;
        if (this.mAudioTrackBean != null) {
            refreshAudioUploadInfoBean();
        } else {
            setAudioUpLoadInfoBean(null);
        }
    }

    private void refreshAudioUploadInfoBean() {
        AudioUpLoadInfoBean upLoadInfoBean = new AudioUpLoadInfoBean();
        upLoadInfoBean.setStart(String.valueOf(getTrueStartTime()));
        upLoadInfoBean.setTitle_s(LocaleUtil.isChinese() ? this.mAudioTrackBean.getTitle_s_en() : this.mAudioTrackBean.getTitle_s());
        upLoadInfoBean.setId(this.mAudioTrackBean.getId());
        upLoadInfoBean.setTitle(LocaleUtil.isChinese() ? this.mAudioTrackBean.getTitle_en() : this.mAudioTrackBean.getTitle());
        upLoadInfoBean.setBpm(this.mAudioTrackBean.getBpm());
        upLoadInfoBean.setDuration(this.mAudioTrackBean.getDuration());
        upLoadInfoBean.setPerformer(LocaleUtil.isChinese() ? this.mAudioTrackBean.getPerformer_en() : this.mAudioTrackBean.getPerformer());
        upLoadInfoBean.setFull_size(this.mAudioTrackBean.getFull_size());
        upLoadInfoBean.setPoi(this.mAudioTrackBean.getPoi());
        setAudioUpLoadInfoBean(upLoadInfoBean);
    }

    public void setHasEditAudioVolume(boolean hasEditAudioVolume) {
        this.hasEditAudioVolume = hasEditAudioVolume;
    }

    public boolean isHasEditAudioVolume() {
        return this.hasEditAudioVolume;
    }

    public boolean isHasEditVideoVolume() {
        return this.hasEditVideoVolume;
    }

    public void setHasEditVideoVolume(boolean hasEditVideoVolume) {
        this.hasEditVideoVolume = hasEditVideoVolume;
    }

    public Bitmap getTextBitmap(Context context, int videoWidth, int videoHeight) {
        return VTContainerView.getFontBitmap(context, this.vtHeight, videoWidth, videoHeight, this);
    }

    public float[] getTextSizeInfo(Bitmap textBitmap, int videoWidth, int videoHeight) {
        int shortSide;
        float[] sizes = new float[5];
        if (videoHeight > videoWidth) {
            shortSide = videoWidth;
        } else {
            shortSide = videoHeight;
        }
        float border = (((float) shortSide) * 1.0f) / 10.0f;
        switch (AnonymousClass3.$SwitchMap$com$blink$academy$onetake$widgets$VideoText$VTContainerView$AlignType[getTextAlignType().ordinal()]) {
            case 1:
                sizes[0] = (1.0f * border) / ((float) videoWidth);
                break;
            case 2:
                sizes[0] = ((((float) (videoWidth - textBitmap.getWidth())) * 1.0f) / 2.0f) / ((float) videoWidth);
                break;
            case 3:
                sizes[0] = (((((float) videoWidth) * 1.0f) - border) - ((float) textBitmap.getWidth())) / ((float) videoWidth);
                break;
        }
        float f = (float) videoHeight;
        sizes[1] = ((f - (((float) getStartY()) * ((((float) videoHeight) * 1.0f) / ((float) this.vtHeight)))) - ((float) textBitmap.getHeight())) / ((float) videoHeight);
        sizes[2] = (((float) textBitmap.getWidth()) * 1.0f) / ((float) videoWidth);
        sizes[3] = (((float) textBitmap.getHeight()) * 1.0f) / ((float) videoHeight);
        sizes[4] = 1.0f;
        return sizes;
    }

    public VideoTextBean getVideoTextBean() {
        VideoTextBean bean = new VideoTextBean();
        bean.setShowStart(this.startTime);
        bean.setShadow(this.textShadowType.toString());
        bean.setStartPercent((((float) this.startTime) * 1.0f) / ((float) this.currentDuration));
        bean.setTextColor(this.textColorType.toString());
        bean.setLetterSpace(this.textLetterSpacingType.toString());
        bean.setText(this.textContent);
        bean.setLineSpace(this.textLineSpacingType.toString());
        bean.setFontSize(this.textFontSizeType.toString());
        bean.setFontName(this.textTypeface.name);
        bean.setAlign(this.textAlignType.toString());
        bean.setShowDuration((float) this.currentDuration);
        bean.setDurationPercent(1.0f);
        return bean;
    }

    public UploadVideoBean getUploadVideoBean() {
        this.uploadVideoBean.preferFPS = getPreferFPS() + "";
        this.uploadVideoBean.rate = getSpeed() + "";
        this.uploadVideoBean.timeRange = "" + ((((float) this.startTime) * 1.0f) / 1000.0f) + "-" + ((((float) getOriginalCurrentDuration()) * 1.0f) / 1000.0f);
        this.uploadVideoBean.soundVolume = this.videoVolume + "";
        this.uploadVideoBean.musicVolume = this.audioVolume + "";
        UploadVideoBean uploadVideoBean = this.uploadVideoBean;
        String str = getZoomEnd() > getZoomStart() ? "Out" : getZoomEnd() == getZoomStart() ? "Off" : "In";
        uploadVideoBean.zoomMode = str;
        return this.uploadVideoBean;
    }

    public AudioUploadModel getUploadAudioModel() {
        AudioUploadModel audioUploadModel = new AudioUploadModel();
        DecimalFormat df = new DecimalFormat("0.000");
        audioUploadModel.setTrackModel(getAudioUpLoadInfoBean());
        audioUploadModel.setUIDurationTime(df.format((((double) getAudioDuration()) * 1.0d) / 1000.0d));
        ArrayList<AudioVolume> audioVolumes = getAudioVolumes();
        if (audioVolumes != null) {
            Iterator it = audioVolumes.iterator();
            while (it.hasNext()) {
                AudioVolume audioVolume = (AudioVolume) it.next();
                StringBuilder result = new StringBuilder();
                result.append(df.format((double) audioVolume.getVolume())).append("_").append(df.format((((double) audioVolume.getStartTime()) * 1.0d) / 1000.0d)).append("-").append(df.format((((double) audioVolume.getDuration()) * 1.0d) / 1000.0d));
                audioUploadModel.getAudioModels().add(result.toString());
            }
        }
        return audioUploadModel;
    }

    public void initUploadVideoBean() {
        long t1 = System.currentTimeMillis();
        if (!TextUtil.isNull(this.videoPath)) {
            File file = new File(this.videoPath);
            if (file.exists() && this.mediaType == 0) {
                Date creationDate = new Date(file.lastModified());
                this.uploadVideoBean.creationDate = creationDate.getTime();
                try {
                    MediaExtractor extractor = MediaUtils.createExtractor(this.videoPath);
                    MediaFormat videoTrack = MediaUtils.getVideoTrack(extractor);
                    this.audioFormat = MediaUtils.getAudioTrack(extractor);
                    this.videoFormat = videoTrack;
                    if (videoTrack != null) {
                        if (videoTrack.containsKey("durationUs")) {
                            long aLong = videoTrack.getLong("durationUs");
                            this.uploadVideoBean.fullDuration = "" + ((((float) aLong) * 1.0f) / 1000000.0f);
                        }
                        int rotation = 0;
                        if (videoTrack.containsKey("rotation-degrees")) {
                            rotation = videoTrack.getInteger("rotation-degrees");
                        }
                        if (videoTrack.containsKey("frame-rate")) {
                            int frameRate = videoTrack.getInteger("frame-rate");
                            this.uploadVideoBean.fps = "" + frameRate;
                        }
                        if (videoTrack.containsKey("width")) {
                            int width = videoTrack.getInteger("width");
                            if (rotation == 90 || rotation == 270) {
                                this.uploadVideoBean.width = "" + width;
                            } else {
                                this.uploadVideoBean.height = "" + width;
                            }
                        }
                        if (videoTrack.containsKey("height")) {
                            int height = videoTrack.getInteger("height");
                            if (rotation == 90 || rotation == 270) {
                                this.uploadVideoBean.height = "" + height;
                            } else {
                                this.uploadVideoBean.width = "" + height;
                            }
                        }
                    }
                    extractor.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(this.videoPath);
                String location = mmr.extractMetadata(23);
                if (location != null) {
                    if (location.startsWith("+")) {
                        location = location.substring(1);
                    }
                    String[] strings = location.split("\\+");
                    if (strings.length >= 2) {
                        String lat = strings[0];
                        String lng = strings[1];
                        this.uploadVideoBean.location.latitude = (double) Float.valueOf(lat).floatValue();
                        this.uploadVideoBean.location.longitude = (double) Float.valueOf(lng).floatValue();
                    }
                }
                mmr.release();
                LogUtil.d("initVideoInfo", String.format("excute time: %s", new Object[]{Long.valueOf(System.currentTimeMillis() - t1)}));
            }
        }
    }

    public AudioUpLoadInfoBean getAudioUpLoadInfoBean() {
        if (this.mAudioUpLoadInfoBean == null && this.mAudioTrackBean != null) {
            refreshAudioUploadInfoBean();
        }
        return this.mAudioUpLoadInfoBean;
    }

    public void setAudioUpLoadInfoBean(AudioUpLoadInfoBean audioUpLoadInfoBean) {
        this.mAudioUpLoadInfoBean = audioUpLoadInfoBean;
    }

    public boolean isLongPressing() {
        return this.longPressing;
    }

    public void setLongPressing(boolean longPressing) {
        this.longPressing = longPressing;
    }

    public int getCircleCoverType() {
        return this.circleCoverType;
    }

    public void setCircleCoverType(int circleCoverType) {
        this.circleCoverType = circleCoverType;
    }

    public long getUsageStartTime() {
        return this.usageStartTime;
    }

    public void setUsageStartTime(long usageStartTime) {
        this.usageStartTime = usageStartTime;
    }

    public long getUsageDurationValue() {
        return this.usageDurationValue;
    }

    public void setUsageDurationValue(long usageDurationValue) {
        this.usageDurationValue = usageDurationValue;
    }

    public LongVideosModel getEndRelateTextModel() {
        return this.endRelateTextModel;
    }

    public void setEndRelateTextModel(LongVideosModel endRelateTextModel) {
        this.endRelateTextModel = endRelateTextModel;
    }

    public LongVideosModel getPointToVideoModel() {
        return this.pointToVideoModel;
    }

    public void setPointToVideoModel(LongVideosModel pointToVideoModel) {
        this.pointToVideoModel = pointToVideoModel;
    }

    public long getRelateStartTime() {
        return this.relateStartTime;
    }

    public void setRelateStartTime(long relateStartTime) {
        this.relateStartTime = relateStartTime;
    }

    public long getCacheStartTime() {
        return this.cacheStartTime;
    }

    public void setCacheStartTime(long cacheStartTime) {
        this.cacheStartTime = cacheStartTime;
    }

    public long getCacheDuration() {
        return this.cacheDuration;
    }

    public void setCacheDuration(long cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    public long getCacheRelateStartTime() {
        return this.cacheRelateStartTime;
    }

    public void setCacheRelateStartTime(long cacheRelateStartTime) {
        this.cacheRelateStartTime = cacheRelateStartTime;
    }

    public boolean isHideForSpecial() {
        return this.hideForSpecial;
    }

    public void setHideForSpecial(boolean hideForSpecial) {
        this.hideForSpecial = hideForSpecial;
    }

    public void cacheTextRelateData() {
        setCacheStartTime(getStartTimeMs());
        setCacheDuration(getCurrentDurationValue());
        setCacheRelateStartTime(getRelateStartTime());
    }

    public VideoInputRatio getInputRatio() {
        return this.inputRatio;
    }

    public void setInputRatio(VideoInputRatio inputRatio) {
        this.inputRatio = inputRatio;
    }

    private float getVideoRatio() {
        return (((float) this.videoHeight) * 1.0f) / ((float) this.videoWidth);
    }

    public VideoInputRatio cloneInputRatio() {
        VideoInputRatio videoRatio = new VideoInputRatio();
        if (this.inputRatio != null) {
            videoRatio.ratioWidth = this.inputRatio.ratioWidth;
            videoRatio.ratioHeight = this.inputRatio.ratioHeight;
        }
        return videoRatio;
    }

    public VideoInputRatio cloneInputRatioWithRotation() {
        VideoInputRatio videoRatio = new VideoInputRatio();
        if (this.inputRatio != null) {
            videoRatio.ratioWidth = this.inputRatio.ratioWidth;
            videoRatio.ratioHeight = this.inputRatio.ratioHeight;
        }
        return videoRatio;
    }

    public void changeInputRatio() {
        int temp = this.inputRatio.ratioWidth;
        int height = temp;
        this.inputRatio.ratioWidth = this.inputRatio.ratioHeight;
        this.inputRatio.ratioHeight = height;
    }

    public float[] getInputCropParams() {
        if (this.inputRatio == null || this.videoWidth <= 0 || this.videoHeight <= 0) {
            return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        }
        if (this.inputRatio.ratioWidth == 0 || this.inputRatio.ratioHeight == 0) {
            return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        }
        int targetWidth;
        int targetHeight;
        int ratioWidth = this.inputRatio.ratioWidth;
        int ratioHeight = this.inputRatio.ratioHeight;
        float ratio = this.inputRatio.getRatio();
        if (getVideoRotate() == 90 || getVideoRotate() == 270) {
            ratio = (((float) ratioWidth) * 1.0f) / ((float) ratioHeight);
        }
        if (getVideoRatio() - ratio > 0.0f) {
            targetWidth = this.videoWidth;
            targetHeight = (int) (((float) targetWidth) * ratio);
        } else if (getVideoRatio() - ratio < 0.0f) {
            targetHeight = this.videoHeight;
            targetWidth = (int) ((((float) targetHeight) * 1.0f) / ratio);
        } else {
            targetHeight = this.videoHeight;
            targetWidth = this.videoWidth;
        }
        float x = ((((float) (this.videoWidth - targetWidth)) * 1.0f) / 2.0f) / ((float) this.videoWidth);
        float y = ((((float) (this.videoHeight - targetHeight)) * 1.0f) / 2.0f) / ((float) this.videoHeight);
        float w = (((float) targetWidth) * 1.0f) / ((float) this.videoWidth);
        float h = (((float) targetHeight) * 1.0f) / ((float) this.videoHeight);
        return new float[]{x, y, w, h};
    }

    public ArrayList<AudioVolume> getAudioVolumes() {
        return this.mAudioVolumes;
    }

    public void setAudioVolumes(ArrayList<AudioVolume> audioVolumes) {
        this.mAudioVolumes = audioVolumes;
    }

    private float getSetVideoExposure() {
        return BigDecimal.valueOf((double) this.videoExposure).setScale(1, RoundingMode.HALF_UP).floatValue();
    }

    public boolean needProxyVideo() {
        if (this.proxyFileMap != null && this.proxyFileMap.size() > 0) {
            ProxyFileInfo proxyFileInfo = getProxyFileInfo();
            if (proxyFileInfo != null) {
                boolean dontCreateProxy;
                long videoStartTimeUs = proxyFileInfo.getInputVideoStartTimeUs();
                long videoDurationUs = proxyFileInfo.getInputVideoDurationUs();
                if (this.startTime * 1000 < videoStartTimeUs || this.currentDuration * 1000 > videoDurationUs) {
                    dontCreateProxy = false;
                } else {
                    dontCreateProxy = true;
                }
                if (dontCreateProxy) {
                    return false;
                }
                return true;
            }
        }
        return ProxyVideoManager.getInstance().needProxyVideo(this);
    }

    private String getLastName() {
        String lastName = "";
        if (this.videoSpeedUp == VideoSpeedup.CHAPLIN) {
            return "chaplin";
        }
        if (this.videoSpeedUp == VideoSpeedup.DOUBLE) {
            return "double";
        }
        if (this.videoSpeedUp == VideoSpeedup.TIMELAPSE) {
            return "timelapse";
        }
        return "quadruple";
    }

    private String getProxyVideoPath(String inputFilePath) {
        String fileName = new File(inputFilePath).getName();
        int indexOfPoint = fileName.lastIndexOf(".");
        String preFileName = fileName.substring(0, indexOfPoint);
        String fileFormatName = fileName.substring(indexOfPoint, fileName.length());
        preFileName = preFileName + getLastName();
        String proxyRootPath = ProxyVideoManager.proxyVideoPath + getProxyFileDirName() + "/";
        File file = new File(proxyRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return proxyRootPath + preFileName + fileFormatName;
    }

    private Client getProxyClient() {
        if (this.videoSpeedUp == VideoSpeedup.CHAPLIN) {
            return this.chaplinClient;
        }
        if (this.videoSpeedUp == VideoSpeedup.DOUBLE) {
            return this.doubleClient;
        }
        if (this.videoSpeedUp == VideoSpeedup.QUADRUPLE) {
            return this.quadrupleClient;
        }
        if (this.videoSpeedUp == VideoSpeedup.TIMELAPSE) {
            return this.timelapseClient;
        }
        return this.normalClient;
    }

    public void stopAllClient() {
        if (this.chaplinClient != null) {
            this.chaplinClient.abort();
        }
        if (this.doubleClient != null) {
            this.doubleClient.abort();
        }
        if (this.quadrupleClient != null) {
            this.quadrupleClient.abort();
        }
        if (this.normalClient != null) {
            this.normalClient.abort();
        }
        if (this.timelapseClient != null) {
            this.timelapseClient.abort();
        }
        removeProxyFileInfo();
    }

    public void setProxyClient(Client client) {
        if (this.videoSpeedUp == VideoSpeedup.CHAPLIN) {
            this.chaplinClient = client;
        } else if (this.videoSpeedUp == VideoSpeedup.DOUBLE) {
            this.doubleClient = client;
        } else if (this.videoSpeedUp == VideoSpeedup.QUADRUPLE) {
            this.quadrupleClient = client;
        } else if (this.videoSpeedUp == VideoSpeedup.TIMELAPSE) {
            this.timelapseClient = client;
        } else {
            this.normalClient = client;
        }
    }

    public void stopCurrentClient() {
        if (this.videoSpeedUp == VideoSpeedup.CHAPLIN) {
            if (this.chaplinClient != null) {
                this.chaplinClient.abort();
            }
        } else if (this.videoSpeedUp == VideoSpeedup.DOUBLE) {
            if (this.doubleClient != null) {
                this.doubleClient.abort();
            }
        } else if (this.videoSpeedUp == VideoSpeedup.QUADRUPLE) {
            LogUtil.e("quadrupleClient", "quadrupleClient: " + this.quadrupleClient);
            if (this.quadrupleClient != null) {
                this.quadrupleClient.abort();
            }
        } else if (this.videoSpeedUp == VideoSpeedup.TIMELAPSE) {
            if (this.timelapseClient != null) {
                this.timelapseClient.abort();
            }
        } else if (this.normalClient != null) {
            this.normalClient.abort();
        }
        removeProxyFileInfo();
    }

    private synchronized void removeProxyFileInfo() {
        this.proxyFileMap.remove(this.videoSpeedUp);
    }

    public void judgeAndStartProxyVideo() {
        if (needProxyVideo()) {
            new Thread() {
                public void run() {
                    super.run();
                    EventBus.getDefault().post(new ProxyProgressEvent(0.0f, LongVideosModel.this, false));
                    LongVideosModel.this.startProxyVideo(true);
                }
            }.start();
        }
    }

    public void startProxyVideo(boolean needSendProgressEvent) {
        judgeProxyMapNull();
        ProxyFileInfo proxyFileInfo = getProxyFileInfo();
        long inputStartUs;
        long inputDurationUs;
        long mediaStartUs;
        if (proxyFileInfo != null) {
            boolean dontCreateProxy = this.startTime * 1000 > proxyFileInfo.getInputVideoStartTimeUs() && this.currentDuration * 1000 <= proxyFileInfo.getInputVideoDurationUs();
            if (!dontCreateProxy) {
                removeProxyFileInfo();
                inputStartUs = (this.startTime * 1000) - 5000000;
                if (inputStartUs < 0) {
                    inputStartUs = 0;
                }
                inputDurationUs = this.currentDuration * 1000;
                mediaStartUs = (this.startTime * 1000) - inputStartUs;
                proxyFileInfo = new ProxyFileInfo(this.videoPath, getProxyVideoPath(this.videoPath), inputStartUs, inputDurationUs, mediaStartUs, inputDurationUs + mediaStartUs, this.videoWidth, this.videoHeight, getSpeed());
                putProxyFileInfo(proxyFileInfo);
                if (getProxyClient() != null) {
                    getProxyClient().abort();
                }
                ProxyVideoManager.getInstance().startOutputProxyFile(this, proxyFileInfo, getProxyClient(), needSendProgressEvent);
                return;
            }
            return;
        }
        inputStartUs = (this.startTime * 1000) - 5000000;
        if (inputStartUs < 0) {
            inputStartUs = 0;
        }
        inputDurationUs = this.currentDuration * 1000;
        mediaStartUs = (this.startTime * 1000) - inputStartUs;
        proxyFileInfo = new ProxyFileInfo(this.videoPath, getProxyVideoPath(this.videoPath), inputStartUs, inputDurationUs, mediaStartUs, inputDurationUs + mediaStartUs, this.videoWidth, this.videoHeight, getSpeed());
        putProxyFileInfo(proxyFileInfo);
        ProxyVideoManager.getInstance().startOutputProxyFile(this, proxyFileInfo, getProxyClient(), needSendProgressEvent);
    }

    private synchronized void putProxyFileInfo(ProxyFileInfo proxyFileInfo) {
        this.proxyFileMap.put(this.videoSpeedUp, proxyFileInfo);
    }

    public void updateProxyCreationDate() {
        File file = new File(ProxyVideoManager.proxyVideoPath + this.proxyVideoCreateTime);
        boolean fileIsExists = file.exists();
        this.proxyVideoCreateTime = System.currentTimeMillis();
        File newFile = new File(ProxyVideoManager.proxyVideoPath + this.proxyVideoCreateTime);
        if (fileIsExists) {
            file.renameTo(newFile);
        } else {
            newFile.mkdirs();
        }
    }

    private String getProxyFileDirName() {
        if (this.proxyVideoCreateTime <= 0) {
            this.proxyVideoCreateTime = System.currentTimeMillis();
        }
        return this.proxyVideoCreateTime + "";
    }
}