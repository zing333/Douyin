package cn.nineton.onetake.listener;

//import com.blink.academy.onetake.ui.BasePresenter;
//import com.blink.academy.onetake.ui.BaseView;

import cn.nineton.onetake.presenter.BasePresenter;

public interface FilterActivityContract {

    public interface View extends BaseView<Presenter> {
        void addVideoViewChild();

        void hideAudioTrimView();

        void hideVideoEditEffectsView();

        void hideVideoEditView();

        void initAudioTrimView();

        void initVideoEditEffectsView();

        void initVideoOrderView();

        void refreshVideoEditView();

        void removeOneVideoView(int i);

        void showAudioTrimView();

        void showVideoEditEffectsView();

        void showVideoEditView();

        void showVideoOrderView();
    }

    public interface Presenter extends BasePresenter {
        void addVideo();

        void changeVideoOrder();

        void getBitmapsFromFrameBuffer();

        void hideAudioTrimView();

        void hideVideoEditEffectsView();

        void initVideoEditEffectsView();

        void onVideoEditTabClick();

        void openAudioTrimView();

        void openVideoEditEffectsView();

        void openVideoEditOrderView();

        void removeVideo(int i);
    }
}