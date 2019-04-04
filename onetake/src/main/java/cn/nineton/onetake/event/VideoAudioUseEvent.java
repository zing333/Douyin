package cn.nineton.onetake.event;


import cn.nineton.onetake.bean.AudioTrackBean;

public class VideoAudioUseEvent {
    private AudioTrackBean mAudioTrackBean;

    public VideoAudioUseEvent(AudioTrackBean audioTrackBean) {
        this.mAudioTrackBean = audioTrackBean;
    }

    public AudioTrackBean getAudioTrackBean() {
        return this.mAudioTrackBean;
    }
}