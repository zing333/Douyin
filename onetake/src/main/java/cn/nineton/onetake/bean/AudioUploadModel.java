package cn.nineton.onetake.bean;

import java.util.ArrayList;

public class AudioUploadModel {
    String UIDurationTime;
    ArrayList<String> audioModels;
    AudioUpLoadInfoBean trackModel;
    String type = "Audio";

    public ArrayList<String> getAudioModels() {
        if (this.audioModels == null) {
            this.audioModels = new ArrayList();
        }
        return this.audioModels;
    }

    public void setAudioModels(ArrayList<String> audioModels) {
        this.audioModels = audioModels;
    }

    public AudioUpLoadInfoBean getTrackModel() {
        return this.trackModel;
    }

    public void setTrackModel(AudioUpLoadInfoBean trackModel) {
        this.trackModel = trackModel;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUIDurationTime() {
        return this.UIDurationTime;
    }

    public void setUIDurationTime(String UIDurationTime) {
        this.UIDurationTime = UIDurationTime;
    }
}