package cn.nineton.onetake.listener;


import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.widget.VideoEditModuleView;

public interface VideoEditModuleParentListener {
    void initCurrentScrollX();

    void onChangeLastViewWidth(float f);

    void onEndSlide();

    void onScrollViewSmoothScrollTo(int i);

    void onSlideLeftParentScroll(int i);

    void onSliding();

    void onStartGetBitmap(long j, LongVideosModel longVideosModel);

    void onStartSlide();

    void onVideoEditModuleViewClick(VideoEditModuleView videoEditModuleView, int i, boolean z);

    void onVideoEditModuleViewLongClick(VideoEditModuleView videoEditModuleView);
}