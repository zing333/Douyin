package cn.nineton.onetake.presenter;


import cn.nineton.onetake.listener.FilterActivityContract;

public class FilterActivityPresenter implements FilterActivityContract.Presenter {
    private final FilterActivityContract.View mFilterActivityView;

    public FilterActivityPresenter(FilterActivityContract.View filterActivityView) {
        this.mFilterActivityView = filterActivityView;
        this.mFilterActivityView.setPresenter(this);
    }

    public void getBitmapsFromFrameBuffer() {
        this.mFilterActivityView.addVideoViewChild();
    }

    public void onVideoEditTabClick() {
        this.mFilterActivityView.showVideoEditView();
    }

    public void openVideoEditOrderView() {
        this.mFilterActivityView.initVideoOrderView();
        this.mFilterActivityView.showVideoOrderView();
    }

    public void removeVideo(int index) {
        this.mFilterActivityView.removeOneVideoView(index);
    }

    public void changeVideoOrder() {
    }

    public void addVideo() {
        this.mFilterActivityView.addVideoViewChild();
    }

    public void initVideoEditEffectsView() {
        this.mFilterActivityView.initVideoEditEffectsView();
    }

    public void openVideoEditEffectsView() {
        this.mFilterActivityView.showVideoEditEffectsView();
    }

    public void hideVideoEditEffectsView() {
        this.mFilterActivityView.hideVideoEditEffectsView();
    }

    public void openAudioTrimView() {
        this.mFilterActivityView.initAudioTrimView();
        this.mFilterActivityView.showAudioTrimView();
    }

    public void hideAudioTrimView() {
        this.mFilterActivityView.hideAudioTrimView();
    }

    public void start() {
    }
}