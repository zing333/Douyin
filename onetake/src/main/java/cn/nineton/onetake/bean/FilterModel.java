package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.bean.filterview.FilterEffectBean;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
//import com.blink.academy.onetake.widgets.VideoText.VTContainerView.DecoModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.FilterActivity;
import cn.nineton.onetake.widget.VTContainerView;

public class FilterModel implements Serializable {
    private VTContainerView.DecoModel decoModel;
    private long duration;
    private String filterName = FilterActivity.NONE_FILTER;
    private int loopEnd;
    private int loopStart;
    private long mCreationDate;
    private boolean mEnableStabilizer;
    private int mFinalEffectState;
    private float mFps;
    private boolean mIsFromLocalVideo;
    private boolean mLooped;
    private int mSelected;
    private String mSignature;
    private int orientation;
    private int playType;
    private int rotation;
    private List<FilterEffectBean> selectEffectTypeList;
    private int slideLevel;
    private boolean stackFrameState = true;
    private int unloopEnd;
    private int unloopStart;

    public long getCreationDate() {
        return this.mCreationDate;
    }

    public void setCreationDate(long creationDate) {
        this.mCreationDate = creationDate;
    }

    public String toString() {
        return "FilterModel{, mLooped=" + this.mLooped + ", mEnableStabilizer=" + this.mEnableStabilizer + ", mIsFromLocalVideo=" + this.mIsFromLocalVideo + ", mFinalEffectState=" + this.mFinalEffectState + ", rotation=" + this.rotation + ", unloopStart=" + this.unloopStart + ", unloopEnd=" + this.unloopEnd + ", slideLevel=" + this.slideLevel + ", loopStart=" + this.loopStart + ", loopEnd=" + this.loopEnd + ", mFps=" + this.mFps + ", filterName='" + this.filterName + '\'' + ", playType=" + this.playType + ", stackFrameState=" + this.stackFrameState + ", selectEffectTypeList=" + this.selectEffectTypeList + ", mSignature='" + this.mSignature + '\'' + ", decoModel=" + this.decoModel + ", orientation=" + this.orientation + ", mSelected=" + this.mSelected + ", duration=" + this.duration + '}';
    }

    public List<FilterEffectBean> getSelectEffectTypeList() {
        if (this.selectEffectTypeList == null) {
            this.selectEffectTypeList = new ArrayList();
        }
        return this.selectEffectTypeList;
    }

    public void setSelectEffectTypeList(List<FilterEffectBean> selectEffectTypeList) {
        if (selectEffectTypeList != null) {
            this.selectEffectTypeList = selectEffectTypeList;
        }
    }

    public boolean getStackFrameState() {
        return this.stackFrameState;
    }

    public void setStackFrameState(boolean stackFrameState) {
        this.stackFrameState = stackFrameState;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public void setSignature(String signature) {
        this.mSignature = signature;
    }

    public int getPlayType() {
        return this.playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public float getFps() {
        return this.mFps;
    }

    public void setFps(float fps) {
        this.mFps = fps;
    }

    public int getSlideLevel() {
        return this.slideLevel;
    }

    public void setSlideLevel(int slideLevel) {
        this.slideLevel = slideLevel;
    }

    public boolean ismLooped() {
        return this.mLooped;
    }

    public void setmLooped(boolean mLooped) {
        this.mLooped = mLooped;
    }

    public int getUnloopStart() {
        return this.unloopStart;
    }

    public void setUnloopStart(int unloopStart) {
        this.unloopStart = unloopStart;
    }

    public int getUnloopEnd() {
        return this.unloopEnd;
    }

    public void setUnloopEnd(int unloopEnd) {
        this.unloopEnd = unloopEnd;
    }

    public boolean ismEnableStabilizer() {
        return this.mEnableStabilizer;
    }

    public void setmEnableStabilizer(boolean mEnableStabilizer) {
        this.mEnableStabilizer = mEnableStabilizer;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getmSelected() {
        return this.mSelected;
    }

    public void setmSelected(int mSelected) {
        this.mSelected = mSelected;
    }

    public boolean ismIsFromLocalVideo() {
        return this.mIsFromLocalVideo;
    }

    public void setmIsFromLocalGIF(boolean mIsFromLocalVideo) {
        this.mIsFromLocalVideo = mIsFromLocalVideo;
    }

    public int getmFinalEffectState() {
        return this.mFinalEffectState;
    }

    public void setmFinalEffectState(int mFinalEffectState) {
        this.mFinalEffectState = mFinalEffectState;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public VTContainerView.DecoModel getDecoModel() {
        return this.decoModel;
    }

    public void setDecoModel(VTContainerView.DecoModel decoModel) {
        this.decoModel = decoModel;
    }

    public int getLoopEnd() {
        return this.loopEnd;
    }

    public void setLoopEnd(int loopEnd) {
        this.loopEnd = loopEnd;
    }

    public int getLoopStart() {
        return this.loopStart;
    }

    public void setLoopStart(int loopStart) {
        this.loopStart = loopStart;
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}