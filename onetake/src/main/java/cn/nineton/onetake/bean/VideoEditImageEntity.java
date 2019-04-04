package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel.AudioVolume;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;

import cn.nineton.onetake.widget.StaticLayoutUtil;

public class VideoEditImageEntity extends BaseEntity {
    public static final int AUDIO_COVER_SHOW_TYPE_LEFT = 0;
    public static final int AUDIO_COVER_SHOW_TYPE_RIGHT = 1;
    public static final int CIRCLE_COVER_TYPE_LEFT = 1;
    public static final int CIRCLE_COVER_TYPE_LEFT_AND_RIGHT = 3;
    public static final int CIRCLE_COVER_TYPE_NONE = 0;
    public static final int CIRCLE_COVER_TYPE_RIGHT = 2;
    public static final int WHITE_AREA_TYPE_CENTER = 1;
    public static final int WHITE_AREA_TYPE_LEFT = 2;
    public static final int WHITE_AREA_TYPE_LEFT_AND_RGIHT = 4;
    public static final int WHITE_AREA_TYPE_NONE = 0;
    public static final int WHITE_AREA_TYPE_RIGHT = 3;
    private int add_type_width;
    private int audioCoverShowType;
    private int audioCoverWidth;
    private long audioDuration;
    private String audioFilePath;
    private boolean audioMuteOn;
    private long audioStartTime;
    private long audioSumTime;
    private float audioWaveSecondDuration;
    private float audioWaveSecondStartTime;
    private float audioWaveStartDuration;
    private float audioWaveStartTime;
    private int bgColor;
    private int circleCoverType = 0;
    private int empty_type_width;
    private boolean hideLeftArrowImage;
    private boolean hideRightArrowImage;
    private long imageTime;
    private int innerCircleCoverType = 0;
    private boolean leftNeedShowAnim;
    private int leftWidth;
    private LongVideosModel.AudioVolume mAudioVolume;
    private LongVideosModel mLongVideosModel;
    private int musicBgCoverWidth;
    private int musicWaveContentWidth;
    private int muteTvLeftMargin;
    private boolean needImageShowAnim;
    private boolean rightNeedShowAnim;
    private int rightWidth;
    private boolean showEffectImage;
    private float showEnd = 1.0f;
    private boolean showMuteTv;
    private float showStart = StaticLayoutUtil.DefaultSpacingadd;
    private int showTimeText;
    private int timeIndex;
    private boolean videoMuteOn;
    private int white_area_type = 0;

    public VideoEditImageEntity(int viewType) {
        super(viewType);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getWhite_area_type() {
        return this.white_area_type;
    }

    public void setWhite_area_type(int white_area_type) {
        this.white_area_type = white_area_type;
    }

    public int getAdd_type_width() {
        return this.add_type_width;
    }

    public void setAdd_type_width(int add_type_width) {
        this.add_type_width = add_type_width;
    }

    public int getEmpty_type_width() {
        return this.empty_type_width;
    }

    public void setEmpty_type_width(int empty_type_width) {
        this.empty_type_width = empty_type_width;
    }

    public int getCircleCoverType() {
        return this.circleCoverType;
    }

    public void setCircleCoverType(int circleCoverType) {
        this.circleCoverType = circleCoverType;
    }

    public int getInnerCircleCoverType() {
        return this.innerCircleCoverType;
    }

    public void setInnerCircleCoverType(int innerCircleCoverType) {
        this.innerCircleCoverType = innerCircleCoverType;
    }

    public LongVideosModel getLongVideosModel() {
        return this.mLongVideosModel;
    }

    public void setLongVideosModel(LongVideosModel longVideosModel) {
        this.mLongVideosModel = longVideosModel;
    }

    public long getImageTime() {
        return this.imageTime;
    }

    public void setImageTime(long imageTime) {
        this.imageTime = imageTime;
    }

    public boolean isHideLeftArrowImage() {
        return this.hideLeftArrowImage;
    }

    public void setHideLeftArrowImage(boolean hideLeftArrowImage) {
        this.hideLeftArrowImage = hideLeftArrowImage;
    }

    public boolean isHideRightArrowImage() {
        return this.hideRightArrowImage;
    }

    public void setHideRightArrowImage(boolean hideRightArrowImage) {
        this.hideRightArrowImage = hideRightArrowImage;
    }

    public int getTimeIndex() {
        return this.timeIndex;
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }

    public boolean isNeedImageShowAnim() {
        return this.needImageShowAnim;
    }

    public void setNeedImageShowAnim(boolean needImageShowAnim) {
        this.needImageShowAnim = needImageShowAnim;
    }

    public boolean isLeftNeedShowAnim() {
        return this.leftNeedShowAnim;
    }

    public void setLeftNeedShowAnim(boolean leftNeedShowAnim) {
        this.leftNeedShowAnim = leftNeedShowAnim;
    }

    public boolean isRightNeedShowAnim() {
        return this.rightNeedShowAnim;
    }

    public void setRightNeedShowAnim(boolean rightNeedShowAnim) {
        this.rightNeedShowAnim = rightNeedShowAnim;
    }

    public int getShowTimeText() {
        return this.showTimeText;
    }

    public void setShowTimeText(int showTimeText) {
        this.showTimeText = showTimeText;
    }

    public float getAudioWaveStartTime() {
        return this.audioWaveStartTime;
    }

    public void setAudioWaveStartTime(float audioWaveStartTime) {
        this.audioWaveStartTime = audioWaveStartTime;
    }

    public float getAudioWaveStartDuration() {
        return this.audioWaveStartDuration;
    }

    public void setAudioWaveStartDuration(float audioWaveStartDuration) {
        this.audioWaveStartDuration = audioWaveStartDuration;
    }

    public float getAudioWaveSecondStartTime() {
        return this.audioWaveSecondStartTime;
    }

    public void setAudioWaveSecondStartTime(float audioWaveSecondStartTime) {
        this.audioWaveSecondStartTime = audioWaveSecondStartTime;
    }

    public float getAudioWaveSecondDuration() {
        return this.audioWaveSecondDuration;
    }

    public void setAudioWaveSecondDuration(float audioWaveSecondDuration) {
        this.audioWaveSecondDuration = audioWaveSecondDuration;
    }

    public String getAudioFilePath() {
        return this.audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public int getAudioCoverWidth() {
        return this.audioCoverWidth;
    }

    public void setAudioCoverWidth(int audioCoverWidth) {
        this.audioCoverWidth = audioCoverWidth;
    }

    public long getAudioSumTime() {
        return this.audioSumTime;
    }

    public void setAudioSumTime(long audioSumTime) {
        this.audioSumTime = audioSumTime;
    }

    public int getAudioCoverShowType() {
        return this.audioCoverShowType;
    }

    public void setAudioCoverShowType(int audioCoverShowType) {
        this.audioCoverShowType = audioCoverShowType;
    }

    public float getShowStart() {
        return this.showStart;
    }

    public void setShowStart(float showStart) {
        this.showStart = showStart;
    }

    public float getShowEnd() {
        return this.showEnd;
    }

    public void setShowEnd(float showEnd) {
        this.showEnd = showEnd;
    }

    public boolean isShowEffectImage() {
        return this.showEffectImage;
    }

    public void setShowEffectImage(boolean showEffectImage) {
        this.showEffectImage = showEffectImage;
    }

    public boolean isVideoMuteOn() {
        return this.videoMuteOn;
    }

    public void setVideoMuteOn(boolean videoMuteOn) {
        this.videoMuteOn = videoMuteOn;
    }

    public boolean isAudioMuteOn() {
        return this.audioMuteOn;
    }

    public void setAudioMuteOn(boolean audioMuteOn) {
        this.audioMuteOn = audioMuteOn;
    }

    public int getLeftWidth() {
        return this.leftWidth;
    }

    public void setLeftWidth(int leftWidth) {
        this.leftWidth = leftWidth;
    }

    public int getRightWidth() {
        return this.rightWidth;
    }

    public void setRightWidth(int rightWidth) {
        this.rightWidth = rightWidth;
    }

    public int getMusicBgCoverWidth() {
        return this.musicBgCoverWidth;
    }

    public void setMusicBgCoverWidth(int musicBgCoverWidth) {
        this.musicBgCoverWidth = musicBgCoverWidth;
    }

    public int getMusicWaveContentWidth() {
        return this.musicWaveContentWidth;
    }

    public void setMusicWaveContentWidth(int musicWaveContentWidth) {
        this.musicWaveContentWidth = musicWaveContentWidth;
    }

    public boolean isShowMuteTv() {
        return this.showMuteTv;
    }

    public void setShowMuteTv(boolean showMuteTv) {
        this.showMuteTv = showMuteTv;
    }

    public int getMuteTvLeftMargin() {
        return this.muteTvLeftMargin;
    }

    public void setMuteTvLeftMargin(int muteTvLeftMargin) {
        this.muteTvLeftMargin = muteTvLeftMargin;
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public LongVideosModel.AudioVolume getAudioVolume() {
        return this.mAudioVolume;
    }

    public void setAudioVolume(LongVideosModel.AudioVolume audioVolume) {
        this.mAudioVolume = audioVolume;
    }

    public long getAudioStartTime() {
        return this.audioStartTime;
    }

    public void setAudioStartTime(long audioStartTime) {
        this.audioStartTime = audioStartTime;
    }

    public long getAudioDuration() {
        return this.audioDuration;
    }

    public void setAudioDuration(long audioDuration) {
        this.audioDuration = audioDuration;
    }
}