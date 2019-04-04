package cn.nineton.onetake.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager.GetBitmapCallback;
//import com.chad.library.adapter.base.BaseItemDraggableAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.listener.GetBitmapCallback;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;

public class VideoEditOrderAdapter extends com.chad.library.adapter.base.BaseItemDraggableAdapter<LongVideosModel, BaseViewHolder> {
    VideoAudioPlaybackManager manager;

    public VideoEditOrderAdapter(int layoutResId, List<LongVideosModel> data, VideoAudioPlaybackManager mVideoAudioPlaybackManager) {
        super(R.layout.layout_video_order_item, data);
        this.manager = mVideoAudioPlaybackManager;
    }

    public int getItemCount() {
        return getData().size();
    }

    protected void convert(BaseViewHolder helper, LongVideosModel item) {
        ((TextView) helper.getView(R.id.video_order_item_tv)).setText(String.valueOf((1.0f * ((float) ((int) (item.getCurrentDuration() / 100)))) / 10.0f) + "″");
        final ImageView ivIcon = helper.getView(R.id.video_order_item_iv);
        ivIcon.setImageBitmap(null);
        ivIcon.setTag(R.id.image_tag_1, item.getOriginalMediaPath());
        ivIcon.setTag(R.id.image_tag_2, Long.valueOf(item.getStartTimeMs()));
        this.manager.getSingleBitmapByVideo(null, item, item.getStartTimeMs(), new GetBitmapCallback() {
            public void onGetBitmapFinished(String filePath, long bitmapTime, Bitmap bitmap) {
                final String str = filePath;
                final long j = bitmapTime;
                final Bitmap bitmap2 = bitmap;
                App.runOnUiThread(() -> {
                    Long time = (Long) ivIcon.getTag(R.id.image_tag_2);
                    if (TextUtils.equals((String) ivIcon.getTag(R.id.image_tag_1), str) && j == time.longValue()) {
                        ivIcon.setImageBitmap(bitmap2);
                    }
                });
            }
        });
    }
}