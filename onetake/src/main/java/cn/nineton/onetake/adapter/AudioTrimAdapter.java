package cn.nineton.onetake.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.AudioTrimBean;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.videoeditimage.WaveformCacheUtils.WaveDataCallback;
//import com.blink.academy.onetake.widgets.AudioWave.AudioWaveView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.util.Config;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;
import cn.nineton.onetake.util.WaveformCacheUtils;
import cn.nineton.onetake.widget.AudioWaveView;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class AudioTrimAdapter extends Adapter<ViewHolder> {
    public static final int HALF_CENTER_CURSOR_WIDTH = DensityUtil.dip2px(3.0f);
    public static final int ITEM_VIEW_WIDTH = DensityUtil.dip2px(9.0f);
    private static final int NORMAL_ITEM_WIDTH = DensityUtil.dip2px(9.0f);
    private static final String WAVE_DARK_COLOR_STR = "#4c929292";
    private static final String WAVE_NORMAL_COLOR_STR = "#929292";
    private AudioTrackBean audioTrackBean;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private String mFilePath;
    private List<AudioTrimBean> mList;
    private float[] mPOI;
    private RecyclerView mRecyclerView;
    private int mScreenWidth;
    private VideoAudioPlaybackManager manager;
    private int selectPos = 0;
    private float totalDuration;
    private float unSelectPercent = StaticLayoutUtil.DefaultSpacingadd;

    private class AudioTrimViewHolder extends ViewHolder {
        View audio_trim_verse_cursor;
        AudioWaveView audio_trim_wave_view;

        private AudioTrimViewHolder(View itemView) {
            super(itemView);
            if (AudioTrimAdapter.this.manager != null) {
                this.audio_trim_wave_view = (AudioWaveView) itemView.findViewById(R.id.audio_trim_wave_view);
                this.audio_trim_verse_cursor = itemView.findViewById(R.id.audio_trim_verse_cursor);
                this.audio_trim_wave_view.setWaveDrawType(2);
                this.audio_trim_wave_view.setColor(Color.parseColor(AudioTrimAdapter.WAVE_NORMAL_COLOR_STR));
                this.audio_trim_wave_view.setControllSize(1);
            }
        }

        private void onBindHolder(int pos) {
            AudioTrimBean audioTrimBean = (AudioTrimBean) AudioTrimAdapter.this.mList.get(pos);
            setItemViewMargin(pos, audioTrimBean);
            bindWaveView(pos, audioTrimBean);
            bindCursor(audioTrimBean, pos);
        }

        private void bindCursor(AudioTrimBean audioTrimBean, int pos) {
            this.audio_trim_verse_cursor.setVisibility(View.GONE);
            if (TextUtil.isValidate(AudioTrimAdapter.this.mPOI)) {
                float startTime = audioTrimBean.getStartTime() / 1000000.0f;
                float endTime = startTime + (audioTrimBean.getDuration() / 1000000.0f);
                int i = 0;
                while (i < AudioTrimAdapter.this.mPOI.length) {
                    float point = AudioTrimAdapter.this.mPOI[i];
                    if (point < startTime || point > endTime) {
                        i++;
                    } else {
                        int itemWidth;
                        LayoutParams cursorLp = (LayoutParams) this.audio_trim_verse_cursor.getLayoutParams();
                        float percent = point % 1.0f;
                        if (pos == AudioTrimAdapter.this.mList.size() - 1) {
                            itemWidth = AudioTrimAdapter.this.getLastViewWidth();
                        } else {
                            itemWidth = AudioTrimAdapter.ITEM_VIEW_WIDTH;
                        }
                        cursorLp.leftMargin = (int) ((((float) itemWidth) * percent) - ((float) (cursorLp.width / 2)));
                        this.audio_trim_verse_cursor.setLayoutParams(cursorLp);
                        if (i != 0) {
                            this.audio_trim_verse_cursor.setVisibility(View.VISIBLE);
                            return;
                        }
                        return;
                    }
                }
            }
        }

        private void bindWaveView(int pos, AudioTrimBean audioTrimBean) {
            this.audio_trim_wave_view.clearDrawCanvas();
            this.audio_trim_wave_view.setTag(R.id.wave_tag_1, AudioTrimAdapter.this.mFilePath);
            this.audio_trim_wave_view.setTag(R.id.wave_tag_2, Long.valueOf((long) audioTrimBean.getStartTime()));
            resetWavePaintColor(pos);
            AudioTrimAdapter.this.manager.getWaveformCacheUtils().getAudioWaveData(true, this.audio_trim_wave_view, (long) audioTrimBean.getVerse(), AudioTrimAdapter.this.mFilePath, (long) audioTrimBean.getStartTime(), (long) audioTrimBean.getDuration(), (long) AudioTrimAdapter.this.totalDuration, new WaveformCacheUtils.WaveDataCallback() {
                public void onLoadFinished(String filePath, long startTimeUs, long durationUs, float[] data) {
                    final String str = filePath;
                    final long j = startTimeUs;
                    final float[] fArr = data;
                    App.runOnUiThread(new Runnable() {
                        public void run() {
                            Long tag2 = (Long) AudioTrimViewHolder.this.audio_trim_wave_view.getTag(2131689507);
                            if (TextUtils.equals((String) AudioTrimViewHolder.this.audio_trim_wave_view.getTag(2131689506), str) && tag2.longValue() == j) {
                                AudioTrimViewHolder.this.audio_trim_wave_view.setOriginData(fArr);
                            }
                        }
                    });
                }

                public void onLoadFailed(String filePath, long startTimeUs, long durationUs) {
                }
            });
        }

        private void setItemViewMargin(int pos, AudioTrimBean audioTrimBean) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
            float percent;
            if (pos == 0) {
                params.leftMargin = (AudioTrimAdapter.this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH;
                if (pos != AudioTrimAdapter.this.mList.size() - 1) {
                    params.rightMargin = 0;
                    params.width = AudioTrimAdapter.NORMAL_ITEM_WIDTH;
                } else {
                    percent = audioTrimBean.getDuration() / 1000000.0f;
                    params.rightMargin = (AudioTrimAdapter.this.mScreenWidth / 2) + AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH;
                    params.width = (int) (((float) AudioTrimAdapter.NORMAL_ITEM_WIDTH) * percent);
                }
            } else if (pos == AudioTrimAdapter.this.mList.size() - 1) {
                percent = audioTrimBean.getDuration() / 1000000.0f;
                params.leftMargin = 0;
                params.rightMargin = (AudioTrimAdapter.this.mScreenWidth / 2) + AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH;
                params.width = (int) (((float) AudioTrimAdapter.NORMAL_ITEM_WIDTH) * percent);
            } else {
                params.leftMargin = 0;
                params.rightMargin = 0;
                params.width = AudioTrimAdapter.NORMAL_ITEM_WIDTH;
            }
            this.itemView.setLayoutParams(params);
        }

        private void invalidateWaveView(int pos) {
            resetWavePaintColor(pos);
            this.audio_trim_wave_view.reDraw();
        }

        private void resetWavePaintColor(int pos) {
            this.audio_trim_wave_view.setNeedDrawDarkPath(false);
            if (pos == AudioTrimAdapter.this.selectPos) {
                this.audio_trim_wave_view.setColor(Color.parseColor(AudioTrimAdapter.WAVE_NORMAL_COLOR_STR));
                if (AudioTrimAdapter.this.unSelectPercent != StaticLayoutUtil.DefaultSpacingadd) {
                    this.audio_trim_wave_view.initDarkPaint(Color.parseColor(AudioTrimAdapter.WAVE_DARK_COLOR_STR));
                    this.audio_trim_wave_view.setNeedDrawDarkPath(true, AudioTrimAdapter.this.unSelectPercent);
                }
            } else if (pos > AudioTrimAdapter.this.selectPos) {
                this.audio_trim_wave_view.setColor(Color.parseColor(AudioTrimAdapter.WAVE_NORMAL_COLOR_STR));
            } else {
                this.audio_trim_wave_view.setColor(Color.parseColor(AudioTrimAdapter.WAVE_DARK_COLOR_STR));
            }
        }
    }

    public AudioTrimAdapter(Context context, int screenWidth, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        this.context = context;
        this.mScreenWidth = screenWidth;
        this.mRecyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setManager(VideoAudioPlaybackManager manager) {
        this.manager = manager;
    }

    public void setAudioTrackBean(AudioTrackBean bean) {
        if (bean != null) {
            this.audioTrackBean = bean;
            if (this.audioTrackBean.isLocal()) {
                this.mFilePath = this.audioTrackBean.getFull_url();
            } else {
                this.mFilePath = new File(Config.getAudiosDownloadPath(), String.valueOf(this.audioTrackBean.getId())).getAbsolutePath();
            }
            this.totalDuration = (Float.valueOf(this.audioTrackBean.getDuration()).floatValue() * 1000.0f) * 1000.0f;
        }
    }

    public void onRecyclerScrollOffsetChange(int offset) {
        if (TextUtil.isValidate(this.mList)) {
            this.selectPos = offset / ITEM_VIEW_WIDTH;
            if (this.selectPos == this.mList.size() - 1) {
                this.unSelectPercent = (((float) (offset % ITEM_VIEW_WIDTH)) * 1.0f) / ((float) getLastViewWidth());
            } else {
                this.unSelectPercent = (((float) (offset % ITEM_VIEW_WIDTH)) * 1.0f) / ((float) ITEM_VIEW_WIDTH);
            }
            invalidateWaveViews();
        }
    }

    public int getRecyclerContentWidth() {
        if (!TextUtil.isValidate(this.mList)) {
            return 0;
        }
        if (this.mList.size() == 1) {
            return getLastViewWidth();
        }
        return ((this.mList.size() - 1) * ITEM_VIEW_WIDTH) + getLastViewWidth();
    }

    public int getLastViewWidth() {
        return (int) (((float) NORMAL_ITEM_WIDTH) * (((AudioTrimBean) this.mList.get(this.mList.size() - 1)).getDuration() / 1000000.0f));
    }

    private void invalidateWaveViews() {
        int firstVisibleItemPosition = this.linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = this.linearLayoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            ViewHolder holder = this.mRecyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof AudioTrimViewHolder) {
                ((AudioTrimViewHolder) holder).invalidateWaveView(i);
            }
        }
    }

    public int getWaveHeight(int pos, float itemPercent) {
        if (itemPercent > 1.0f) {
            return 0;
        }
        ViewHolder holder = this.mRecyclerView.findViewHolderForAdapterPosition(pos);
        if (holder instanceof AudioTrimViewHolder) {
            return ((AudioTrimViewHolder) holder).audio_trim_wave_view.getDoubleWaveLineHeight(itemPercent);
        }
        return 0;
    }

    public AudioTrackBean getAudioTrackBean() {
        return this.audioTrackBean;
    }

    public void setData(List<AudioTrimBean> list) {
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        this.mList.clear();
        this.mList.addAll(list);
    }

    public void setPOI(float[] mPOI) {
        this.mPOI = mPOI;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AudioTrimViewHolder(LayoutInflater.from(this.context).inflate(R.layout.audio_trim_item_view, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof AudioTrimViewHolder) {
            ((AudioTrimViewHolder) holder).onBindHolder(position);
        }
    }

    public int getItemCount() {
        return this.mList == null ? 0 : this.mList.size();
    }
}