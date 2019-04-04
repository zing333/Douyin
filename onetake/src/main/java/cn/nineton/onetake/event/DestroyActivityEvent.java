package cn.nineton.onetake.event;

import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.bean.LongVideosModel;

public class DestroyActivityEvent {
    public List<LongVideosModel> listVideoModels;
    public ActivityState state;
    public List<String> stringVideosModels;

    public enum ActivityState {
        LONGVIDEOLOCAL,
        LONGVIDEOCAMERA,
        LONGVIDEOBITMAP
    }

    public DestroyActivityEvent(ActivityState state, List<String> videosModels) {
        this.state = state;
        this.stringVideosModels = videosModels;
    }

    public DestroyActivityEvent(ActivityState state, ArrayList<LongVideosModel> videosModels) {
        this.state = state;
        this.listVideoModels = videosModels;
    }
}