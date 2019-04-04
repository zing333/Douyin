package cn.nineton.onetake.bean;

import java.util.List;

public class MscvResultBean {
    private MscvAdultBean adult;
    private List<MscvCategoriesBean> categories;
    private MscvColorBean color;
    private MscvDescriptionBean description;
    private List<MscvFacesBean> faces;
    private MscvImageTypeBean imageType;
    private MscvMetadataBean metadata;
    private String requestId;
    private List<MscvTagBean> tags;

    public MscvAdultBean getAdult() {
        return this.adult;
    }

    public void setAdult(MscvAdultBean adult) {
        this.adult = adult;
    }

    public MscvDescriptionBean getDescription() {
        return this.description;
    }

    public void setDescription(MscvDescriptionBean description) {
        this.description = description;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public MscvMetadataBean getMetadata() {
        return this.metadata;
    }

    public void setMetadata(MscvMetadataBean metadata) {
        this.metadata = metadata;
    }

    public MscvColorBean getColor() {
        return this.color;
    }

    public void setColor(MscvColorBean color) {
        this.color = color;
    }

    public MscvImageTypeBean getImageType() {
        return this.imageType;
    }

    public void setImageType(MscvImageTypeBean imageType) {
        this.imageType = imageType;
    }

    public List<MscvCategoriesBean> getCategories() {
        return this.categories;
    }

    public void setCategories(List<MscvCategoriesBean> categories) {
        this.categories = categories;
    }

    public List<MscvTagBean> getTags() {
        return this.tags;
    }

    public void setTags(List<MscvTagBean> tags) {
        this.tags = tags;
    }

    public List<MscvFacesBean> getFaces() {
        return this.faces;
    }

    public void setFaces(List<MscvFacesBean> faces) {
        this.faces = faces;
    }

    public String toString() {
        return "MscvResultBean{adult=" + this.adult + ", description=" + this.description + ", requestId='" + this.requestId + '\'' + ", metadata=" + this.metadata + ", color=" + this.color + ", imageType=" + this.imageType + ", categories=" + this.categories + ", tags=" + this.tags + ", faces=" + this.faces + '}';
    }
}