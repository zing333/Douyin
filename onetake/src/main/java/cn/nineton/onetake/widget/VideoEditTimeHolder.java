package cn.nineton.onetake.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
//import com.blink.academy.onetake.ui.adapter.entities.VideoEditImageEntity;
//import com.blink.academy.onetake.ui.adapter.holder.videoedit.VideoEditViewHolder.VideoEditHolderHelper;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
//import com.blink.academy.onetake.widgets.LinearLayout.LeftTextCenterLayout;

import cn.nineton.onetake.adapter.VideoEditViewHolder;
import cn.nineton.onetake.bean.VideoEditImageEntity;

public class VideoEditTimeHolder extends VideoEditViewHolder {
    public VideoEditTimeHolder(View itemView, Activity context, VideoEditHolderHelper<VideoEditImageEntity> helper) {
        super(itemView, context, helper);
    }

    public void onBindViewHolder(int position) {
        int width;
        getContentView().setText(String.valueOf(((VideoEditImageEntity) this.mHelper.getAllDatas().get(position)).getShowTimeText()));
        if (position == 1) {
            width = VideoEditHelper.IMAGE_UNIT_WIDTH / 2;
            getContentView().removeCenterInParent();
        } else {
            width = VideoEditHelper.IMAGE_UNIT_WIDTH;
            getContentView().restoreCenterInParent();
        }
        LayoutParams layoutParams = getContentView().getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(width, -2);
        } else {
            layoutParams.width = width;
        }
        getContentView().setLayoutParams(layoutParams);
    }

    private LeftTextCenterLayout getContentView() {
        return (LeftTextCenterLayout) this.itemView;
    }

    public void onBindViewHolder(Object data, int groupPosition) {
        onBindViewHolder(groupPosition);
    }
}