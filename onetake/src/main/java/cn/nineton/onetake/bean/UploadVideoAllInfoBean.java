package cn.nineton.onetake.bean;

import java.util.ArrayList;
import java.util.List;

public class UploadVideoAllInfoBean {
    private List<AudioUploadModel> Audios = new ArrayList();
    private String Duration;
    private String FPS;
    private String MusicVolume = "1.000";
    private String Ratio;
    private String Size;
    private String SoundVolume = "1.000";
    private List<VideoTextBean> Text = new ArrayList();
    private List<UploadVideoBean> Videos = new ArrayList();

    public List<UploadVideoBean> getVideos() {
        return this.Videos;
    }

    public void setVideos(List<UploadVideoBean> videos) {
        this.Videos = videos;
    }

    public void addVideo(UploadVideoBean bean) {
        if (this.Videos == null) {
            this.Videos = new ArrayList();
        }
        this.Videos.add(bean);
    }

    public void addAudio(AudioUploadModel model) {
        this.Audios.add(model);
    }

    public String getDuration() {
        return this.Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public String getSoundVolume() {
        return this.SoundVolume;
    }

    public void setSoundVolume(String soundVolume) {
        this.SoundVolume = soundVolume;
    }

    public String getMusicVolume() {
        return this.MusicVolume;
    }

    public void setMusicVolume(String musicVolume) {
        this.MusicVolume = musicVolume;
    }

    public String getSize() {
        return this.Size;
    }

    public void setSize(String size) {
        this.Size = size;
    }

    public String getFPS() {
        return this.FPS;
    }

    public void setFPS(String FPS) {
        this.FPS = FPS;
    }

    public String getRatio() {
        return this.Ratio;
    }

    public void setRatio(String ratio) {
        this.Ratio = ratio;
    }

    public List<VideoTextBean> getText() {
        return this.Text;
    }

    public void setText(List<VideoTextBean> text) {
        this.Text = text;
    }

    public void addText(VideoTextBean bean) {
        if (this.Text == null) {
            this.Text = new ArrayList();
        }
        this.Text.add(bean);
    }
}