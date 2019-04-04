package cn.nineton.onetake.bean;

import java.util.List;

public class MscvDescriptionBean {
    private List<MscvCaptionsBean> captions;
    private List<String> tags;

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<MscvCaptionsBean> getCaptions() {
        return this.captions;
    }

    public void setCaptions(List<MscvCaptionsBean> captions) {
        this.captions = captions;
    }

    public String toString() {
        return "MscvDescriptionBean{tags=" + this.tags + ", captions=" + this.captions + '}';
    }
}