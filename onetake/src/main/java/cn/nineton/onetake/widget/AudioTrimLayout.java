package cn.nineton.onetake.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.AudioTrimAdapter;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.media.audiotool.AudioTrimBean;
import cn.nineton.onetake.util.ColorFilterUtil;
import cn.nineton.onetake.util.DateUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;
import cn.nineton.onetake.util.WaterMarkBitmapUtil;
//import butterknife.InjectView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.AudioTrimBean;
//import com.blink.academy.onetake.bean.audio.AudioTrackBean;
//import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.manager.VideoAudioPlaybackManager;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil;
//import com.blink.academy.onetake.support.utils.DateUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.support.utils.WaterMarkBitmapUtil;
//import com.blink.academy.onetake.ui.adapter.audiotrim.AudioTrimAdapter;
//import com.blink.academy.onetake.ui.helper.VideoEditHelper;
//import com.blink.academy.onetake.widgets.ShadowLayout;
//import com.blink.academy.onetake.widgets.TextView.AvenirNextCondensedRegularTextView;
import java.util.ArrayList;
import java.util.List;

public class AudioTrimLayout extends RelativeLayout {
    private static final int POI_ADJUST_OFFSET = 10;
    @BindView(R.id.audio_trim_cancel_iv)
    ImageView audio_trim_cancel_iv;
    @BindView(R.id.audio_trim_confirm_iv)
    ImageView audio_trim_confirm_iv;
    @BindView(R.id.audio_trim_cursor)
    View audio_trim_cursor;
    @BindView(R.id.audio_trim_duration_tv)
    TextView audio_trim_duration_tv;
    @BindView(R.id.audio_trim_play)
    ImageView audio_trim_play;
    @BindView(R.id.audio_trim_play_shadow)
    ShadowLayout audio_trim_play_shadow;
    @BindView(R.id.audio_trim_root)
    View audio_trim_root;
    @BindView(R.id.audio_trim_rv)
    RecyclerView audio_trim_rv;
    @BindView(R.id.audio_trim_start_time_tv)
    TextView audio_trim_start_time_tv;
    @BindView(R.id.audio_trim_tags_ll)
    FrameLayout audio_trim_tags_ll;
    private LongVideosModel curAudioVideosModel;
    private float curVerse;
    private LinearLayoutManager linearLayoutManager;
    private AudioTrimAdapter mAdapter;
    private float[] mPois;
    private int mScreenWidth;
    private VideoEditHelper mVideoEditHelper;
    private OnTrimButtonClick onTrimButtonClick;
    private boolean playing;
    private Rect tempRect;

    public interface OnTrimButtonClick {
        void onTrimCancel();

        void onTrimConfirm();

        void onTrimPlayClick(boolean z, LongVideosModel longVideosModel);
    }

    public AudioTrimLayout(Context context) {
        this(context, null);
    }

    public AudioTrimLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioTrimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tempRect = new Rect();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_audio_trim, this, true);
        ButterKnife.bind(this);
        this.mScreenWidth = DensityUtil.getMetricsWidth(context);
        initViewPos();
        this.playing = false;
        initRecyclerView();
        bindListener();
    }

    private void initViewPos() {
        LayoutParams playIvLP = (LayoutParams) this.audio_trim_play.getLayoutParams();
        LayoutParams cancelIvLP = (LayoutParams) this.audio_trim_cancel_iv.getLayoutParams();
        LayoutParams confirmIvLP = (LayoutParams) this.audio_trim_confirm_iv.getLayoutParams();
        LayoutParams shadowLP = (LayoutParams) this.audio_trim_play_shadow.getLayoutParams();
        int marginValue = ((this.mScreenWidth - DensityUtil.dip2px(44.0f)) - DensityUtil.dip2px(44.0f)) / 4;
        playIvLP.setMargins(marginValue, 0, 0, 0);
        cancelIvLP.setMargins(marginValue, 0, 0, 0);
        confirmIvLP.setMargins(0, 0, marginValue, 0);
        shadowLP.setMargins(DensityUtil.dip2px(12.0f) + marginValue, 0, 0, 0);
        this.audio_trim_play.setLayoutParams(playIvLP);
        this.audio_trim_cancel_iv.setLayoutParams(cancelIvLP);
        this.audio_trim_confirm_iv.setLayoutParams(confirmIvLP);
        this.audio_trim_play_shadow.setLayoutParams(shadowLP);
    }

    private void initRecyclerView() {
        this.linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.mAdapter = new AudioTrimAdapter(getContext(), this.mScreenWidth, this.audio_trim_rv, this.linearLayoutManager);
        this.audio_trim_rv.setLayoutManager(this.linearLayoutManager);
        this.audio_trim_rv.setAdapter(this.mAdapter);
    }

    public void setVideoEditHelper(VideoEditHelper videoEditHelper) {
        this.mVideoEditHelper = videoEditHelper;
    }

    public void setVideoAudioManager(VideoAudioPlaybackManager mVideoAudioManager) {
        if (this.mAdapter != null) {
            this.mAdapter.setManager(mVideoAudioManager);
        }
    }

    public void changePlayState(boolean pause) {
        this.playing = !pause;
        if (this.playing) {
            this.audio_trim_play.setImageResource(R.drawable.icon_20_audio_stop);
            setCursorMargin(0.0d);
            this.audio_trim_cursor.setVisibility(VISIBLE);
            return;
        }
        this.audio_trim_play.setImageResource(R.drawable.icon_20_audio_play);
        this.audio_trim_cursor.setVisibility(GONE);
    }

    private void bindListener() {
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case 2131690934:
                        AudioTrimLayout.this.curAudioVideosModel.setTrueStartTime(AudioTrimLayout.this.curAudioVideosModel.getTrueStartTime());
                        if (AudioTrimLayout.this.onTrimButtonClick != null) {
                            AudioTrimLayout.this.onTrimButtonClick.onTrimCancel();
                            return;
                        }
                        return;
                    case 2131690939:
                        AudioTrimLayout.this.playing = !AudioTrimLayout.this.playing;
                        if (AudioTrimLayout.this.onTrimButtonClick != null) {
                            AudioTrimLayout.this.onTrimButtonClick.onTrimPlayClick(AudioTrimLayout.this.playing, AudioTrimLayout.this.curAudioVideosModel);
                            return;
                        }
                        return;
                    case 2131690940:
                        AudioTrimLayout.this.curAudioVideosModel.setTrueStartTime(AudioTrimLayout.this.curVerse);
                        if (AudioTrimLayout.this.onTrimButtonClick != null) {
                            AudioTrimLayout.this.onTrimButtonClick.onTrimConfirm();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.audio_trim_play.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.audio_trim_confirm_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.audio_trim_cancel_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.audio_trim_confirm_iv.setOnClickListener(listener);
        this.audio_trim_cancel_iv.setOnClickListener(listener);
        this.audio_trim_play.setOnClickListener(listener);
        this.audio_trim_root.setOnClickListener(listener);
        this.audio_trim_rv.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    AudioTrimLayout.this.audio_trim_play.setEnabled(true);
                    AudioTrimLayout.this.checkWhenOnScrollIdle();
                } else if (newState == 1 && AudioTrimLayout.this.playing) {
                    AudioTrimLayout.this.playing = false;
                    if (AudioTrimLayout.this.onTrimButtonClick != null) {
                        AudioTrimLayout.this.onTrimButtonClick.onTrimPlayClick(false, AudioTrimLayout.this.curAudioVideosModel);
                    }
                    AudioTrimLayout.this.audio_trim_play.setEnabled(false);
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = ((AudioTrimLayout.this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH) - AudioTrimLayout.this.getRecyclerFirstItemLeft();
                AudioTrimLayout.this.mAdapter.onRecyclerScrollOffsetChange(offset);
                AudioTrimLayout.this.changeDurationText(offset);
                AudioTrimLayout.this.resetTagsLlMargin(-offset);
                AudioTrimLayout.this.changeCurVerse(offset);
            }
        });
    }

    private void checkWhenOnScrollIdle() {
        if (TextUtil.isValidate(this.mPois) && this.mPois.length > 1) {
            final int offset = ((this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH) - getRecyclerFirstItemLeft();
            for (int i = 1; i < this.mPois.length; i++) {
                final int pointOffset = (int) (this.mPois[i] * ((float) AudioTrimAdapter.ITEM_VIEW_WIDTH));
                if (Math.abs(pointOffset - offset) <= 10) {
                    this.audio_trim_rv.post(new Runnable() {
                        public void run() {
                            AudioTrimLayout.this.audio_trim_rv.scrollBy(pointOffset - offset, 0);
                        }
                    });
                }
            }
        }
    }

    private void changeCurVerse(int offset) {
        this.curVerse = (1.0f * ((float) offset)) / ((float) AudioTrimAdapter.ITEM_VIEW_WIDTH);
        if (this.curAudioVideosModel != null) {
            this.curAudioVideosModel.setCurStartTime(this.curVerse);
        }
    }

    private void resetTagsLlMargin(int offset) {
        LayoutParams lp = (LayoutParams) this.audio_trim_tags_ll.getLayoutParams();
        lp.leftMargin = ((this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH) + offset;
        this.audio_trim_tags_ll.setLayoutParams(lp);
    }

    private void changeDurationText(int offset) {
        if (this.mAdapter != null) {
            int contentWidth = this.mAdapter.getRecyclerContentWidth();
            if (contentWidth != 0 && this.curAudioVideosModel != null && this.curAudioVideosModel.getAudioTrackBean() != null) {
                this.audio_trim_start_time_tv.setText(DateUtil.dealTime((((1.0f * ((float) offset)) / ((float) contentWidth)) * Float.valueOf(this.curAudioVideosModel.getAudioTrackBean().getDuration()).floatValue()) + ""));
            }
        }
    }

    private int getRecyclerFirstItemLeft() {
        if (this.linearLayoutManager == null) {
            return 0;
        }
        int firstVisibleItemPosition = this.linearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition < 0) {
            return 0;
        }
        if (firstVisibleItemPosition == 0) {
            return this.linearLayoutManager.findViewByPosition(0).getLeft();
        }
        return this.linearLayoutManager.findViewByPosition(firstVisibleItemPosition).getLeft() - (AudioTrimAdapter.ITEM_VIEW_WIDTH * firstVisibleItemPosition);
    }

    public boolean resetUI() {
        if (this.mAdapter != null) {
            if (this.curAudioVideosModel != null) {
                String lastFullUrl = this.curAudioVideosModel.getAudioTrackBean().getFull_url();
                LongVideosModel thisModel = this.mVideoEditHelper.getCurSelectAudioModel();
                if (thisModel != null) {
                    AudioTrackBean thisTrackBean = thisModel.getAudioTrackBean();
                    if (thisTrackBean != null && lastFullUrl.equals(thisTrackBean.getFull_url())) {
                        resetScroller(thisTrackBean);
                        LogUtil.d("slim", "same return");
                        return true;
                    }
                }
            }
            this.curAudioVideosModel = this.mVideoEditHelper.getCurSelectAudioModel();
            this.audio_trim_tags_ll.removeAllViews();
            if (this.curAudioVideosModel != null) {
                AudioTrackBean audioTrackBean = this.curAudioVideosModel.getAudioTrackBean();
                if (audioTrackBean != null) {
                    this.mAdapter.setAudioTrackBean(audioTrackBean);
                    this.mAdapter.setData(getRecyclerData(audioTrackBean));
                    String poi = audioTrackBean.getPoi();
                    if (!TextUtil.isValidate(poi) || "0.000".equals(poi)) {
                        this.mPois = null;
                        this.mAdapter.setPOI(null);
                    } else {
                        String[] split = poi.split(",");
                        int length = split.length;
                        if (length > 1) {
                            float[] pois = new float[length];
                            for (int i = 0; i < length; i++) {
                                pois[i] = Float.valueOf(split[i]).floatValue();
                                addTagToContainer(i, length, pois[i]);
                            }
                            this.mPois = pois;
                            this.mAdapter.setPOI(pois);
                        }
                    }
                    this.mAdapter.notifyDataSetChanged();
                    resetScroller(audioTrackBean);
                    return true;
                }
            }
        }
        return false;
    }

    private void resetScroller(AudioTrackBean audioTrackBean) {
        float nowStartTime = this.curAudioVideosModel.getTrueStartTime();
        this.audio_trim_duration_tv.setText(" / " + DateUtil.dealTime(audioTrackBean.getDuration()));
        this.audio_trim_start_time_tv.setText(DateUtil.dealTime(nowStartTime + ""));
        this.audio_trim_rv.post(new Runnable() {
            public void run() {
                int offset = ((AudioTrimLayout.this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH) - AudioTrimLayout.this.getRecyclerFirstItemLeft();
                int needOffset = (int) (AudioTrimLayout.this.curAudioVideosModel.getTrueStartTime() * ((float) AudioTrimAdapter.ITEM_VIEW_WIDTH));
                if (offset != needOffset) {
                    AudioTrimLayout.this.audio_trim_rv.scrollBy(needOffset - offset, 0);
                }
            }
        });
    }

    private void addTagToContainer(int i, int length, float time) {
        boolean z;
        AvenirNextCondensedRegularTextView tv = new AvenirNextCondensedRegularTextView(getContext());
        if (length - 1 == i) {
            z = true;
        } else {
            z = false;
        }
        tv.setText(getTagContent(length, i, z));
        tv.setIncludeFontPadding(false);
        tv.setTextColor(Color.parseColor("#929292"));
        tv.setTextSize(1, 10.0f);
        tv.setSingleLine();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.leftMargin = ((int) (((float) AudioTrimAdapter.ITEM_VIEW_WIDTH) * time)) - (WaterMarkBitmapUtil.getTextWidth(tv.getText().toString(), tv.getPaint(), this.tempRect) / 2);
        tv.setLayoutParams(lp);
        this.audio_trim_tags_ll.addView(tv);
    }

    public void onVideoPlay(double percent) {
        if (this.curAudioVideosModel != null && this.mAdapter != null) {
            setCursorMargin(percent);
        }
    }

    private void setCursorMargin(double percent) {
        LayoutParams lp = (LayoutParams) this.audio_trim_cursor.getLayoutParams();
        lp.leftMargin = getMoveViewLeftMargin(percent);
        lp.height = getMoveViewHeight(percent);
        this.audio_trim_cursor.setLayoutParams(lp);
    }

    private int getMoveViewHeight(double percent) {
        int recyclerScrollOffset = ((this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH) - getRecyclerFirstItemLeft();
        int contentWidth = this.mAdapter.getRecyclerContentWidth();
        int lastItemWidth = this.mAdapter.getLastViewWidth();
        int durationOffset = (int) (((((double) this.curAudioVideosModel.getAudioDuration()) * percent) / 1000.0d) * ((double) AudioTrimAdapter.ITEM_VIEW_WIDTH));
        int remainder = recyclerScrollOffset % AudioTrimAdapter.ITEM_VIEW_WIDTH;
        if (durationOffset > this.mScreenWidth / 2) {
            return 0;
        }
        int startPos;
        float itemPercent;
        int itemCount = this.mAdapter.getItemCount();
        if (itemCount == 1) {
            startPos = 0;
            if (durationOffset > lastItemWidth - remainder) {
                itemPercent = 1.1f;
            } else {
                itemPercent = (1.0f * ((float) (remainder + durationOffset))) / ((float) lastItemWidth);
            }
        } else if (recyclerScrollOffset > contentWidth - lastItemWidth) {
            startPos = itemCount - 1;
            if (durationOffset > lastItemWidth - remainder) {
                itemPercent = 1.1f;
            } else {
                itemPercent = (1.0f * ((float) (remainder + durationOffset))) / ((float) lastItemWidth);
            }
        } else {
            startPos = recyclerScrollOffset / AudioTrimAdapter.ITEM_VIEW_WIDTH;
            remainder = recyclerScrollOffset - (AudioTrimAdapter.ITEM_VIEW_WIDTH * startPos);
            int comNum = AudioTrimAdapter.ITEM_VIEW_WIDTH - remainder;
            if (durationOffset <= comNum) {
                itemPercent = (1.0f * ((float) (remainder + durationOffset))) / ((float) AudioTrimAdapter.ITEM_VIEW_WIDTH);
            } else {
                startPos++;
                durationOffset -= comNum;
                if (startPos == itemCount - 1) {
                    itemPercent = (1.0f * ((float) durationOffset)) / ((float) lastItemWidth);
                } else {
                    startPos += durationOffset / AudioTrimAdapter.ITEM_VIEW_WIDTH;
                    durationOffset %= AudioTrimAdapter.ITEM_VIEW_WIDTH;
                    if (startPos == itemCount - 1) {
                        itemPercent = (1.0f * ((float) durationOffset)) / ((float) lastItemWidth);
                    } else {
                        itemPercent = (1.0f * ((float) durationOffset)) / ((float) AudioTrimAdapter.ITEM_VIEW_WIDTH);
                    }
                }
            }
        }
        return this.mAdapter.getWaveHeight(startPos, itemPercent);
    }

    private String getTagContent(int length, int i, boolean isMain) {
        if (isMain) {
            return App.getResource().getString(R.string.TEXT_AUDIO_EDIT_VERSE);
        }
        char c = (char) (i + 65);
        if (length <= 2) {
            return App.getResource().getString(R.string.TEXT_AUDIO_EDIT_INTRO);
        }
        return String.format("%s%s", new Object[]{App.getResource().getString(R.string.TEXT_AUDIO_EDIT_INTRO), String.valueOf(c)});
    }

    private int getMoveViewLeftMargin(double percent) {
        return (int) (((double) ((this.mScreenWidth / 2) - AudioTrimAdapter.HALF_CENTER_CURSOR_WIDTH)) + (((((double) this.curAudioVideosModel.getAudioDuration()) * percent) / 1000.0d) * ((double) AudioTrimAdapter.ITEM_VIEW_WIDTH)));
    }

    public void setOnTrimButtonClick(OnTrimButtonClick onTrimButtonClick) {
        this.onTrimButtonClick = onTrimButtonClick;
    }

    public List<AudioTrimBean> getRecyclerData(AudioTrackBean audioTrackBean) {
        List<AudioTrimBean> list = new ArrayList();
        float duration = Float.valueOf(audioTrackBean.getDuration()).floatValue();
        int itemCount = (int) Math.ceil((double) duration);
        for (int i = 0; i < itemCount; i++) {
            if (i == itemCount - 1) {
                list.add(new AudioTrimBean((this.curVerse * 1000.0f) * 1000.0f, (float) ((i * 1000) * 1000), (duration - ((float) i)) * 1000000.0f));
            } else {
                list.add(new AudioTrimBean((this.curVerse * 1000.0f) * 1000.0f, (float) ((i * 1000) * 1000), 1000000.0f));
            }
        }
        return list;
    }

    public void performConfirmClick() {
        this.audio_trim_confirm_iv.performClick();
    }
}