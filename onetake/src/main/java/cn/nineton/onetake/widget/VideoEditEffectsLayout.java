package cn.nineton.onetake.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.blink.academy.onetake.VideoTools.FilterView.VideoSpeedup;
import com.blink.academy.onetake.bean.longvideo.LongVideosModel;
import com.blink.academy.onetake.support.utils.ColorFilterUtil;
import com.blink.academy.onetake.support.utils.LocaleUtil;
import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
import com.blink.academy.onetake.support.utils.TextUtil;
import com.blink.academy.onetake.widgets.LinearLayout.VideoEditEffectsLayout$.Lambda.1;
import com.blink.academy.onetake.widgets.SeekBar.CustomSeekBar;
import com.blink.academy.onetake.widgets.SeekBar.CustomSeekBar.OnCustomProgressChangeListener;
import com.blink.academy.onetake.widgets.SeekBar.CustomSeekBar.OnCustomProgressProStateChangeListener;
import com.blink.academy.onetake.widgets.WheelPager.MeasureSizeFrameLayout;
import com.blink.academy.onetake.widgets.loop.HorizontalLoopView;
import com.blink.academy.onetake.widgets.loop.HorizontalLoopView.OnSelectItemChange;
import com.weigan.loopview.bean.Bean;
import java.util.ArrayList;
import java.util.List;

public class VideoEditEffectsLayout extends FrameLayout {
    public static final int CHANGE_AV = 3;
    public static final int CHANGE_SU = 0;
    public static final int CHANGE_SU_AV = 8;
    public static final int CHANGE_SU_VV = 5;
    public static final int CHANGE_SU_VV_AV = 12;
    public static final int CHANGE_SU_Z = 4;
    public static final int CHANGE_SU_Z_AV = 11;
    public static final int CHANGE_SU_Z_VV = 7;
    public static final int CHANGE_SU_Z_VV_AV = 14;
    public static final int CHANGE_VV = 2;
    public static final int CHANGE_VV_AV = 10;
    public static final int CHANGE_Z = 1;
    public static final int CHANGE_Z_AV = 9;
    public static final int CHANGE_Z_VV = 6;
    public static final int CHANGE_Z_VV_AV = 13;
    @BindView(R.id.bright_seekBar)
    CustomSeekBar bright_seekBar;
    @BindView(R.id.brightness_max_iv)
    ImageView brightness_max_iv;
    @BindView(R.id.brightness_min_iv)
    ImageView brightness_min_iv;
    @BindView(R.id.cancel_btn_iv)
    ImageView cancel_btn_iv;
    @BindView(R.id.confirm_btn_iv)
    ImageView confirm_btn_iv;
    private LongVideosModel mLastModel;
    private OnClickListener mListener;
    @BindView(R.id.music_max_iv)
    ImageView music_max_iv;
    @BindView(R.id.music_min_iv)
    ImageView music_min_iv;
    @BindView(R.id.music_seekBar)
    CustomSeekBar music_seekBar;
    @BindView(R.id.sound_max_iv)
    ImageView sound_max_iv;
    @BindView(R.id.sound_min_iv)
    ImageView sound_min_iv;
    @BindView(R.id.sound_seekBar)
    CustomSeekBar sound_seekBar;
    @BindView(R.id.speed_loop)
    HorizontalLoopView speed_loop;
    @BindView(R.id.speed_msfl)
    MeasureSizeFrameLayout speed_msfl;
    @BindView(R.id.video_edit_effect_root)
    View video_edit_effect_root;
    @BindView(R.id.zoom_loop)
    HorizontalLoopView zoom_loop;

    /* renamed from: com.blink.academy.onetake.widgets.LinearLayout.VideoEditEffectsLayout$3 */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup = new int[VideoSpeedup.values().length];

        static {
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.STEP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.SLOW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.EIGHTMM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.NORMAL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.CHAPLIN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.DOUBLE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.QUADRUPLE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[VideoSpeedup.TIMELAPSE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    private /* synthetic */ void lambda$new$0(View v) {
        switch (v.getId()) {
            case 2131691642:
                if (this.sound_seekBar.isNeedGesture()) {
                    this.sound_seekBar.setPro(StaticLayoutUtil.DefaultSpacingadd);
                    return;
                }
                return;
            case 2131691643:
                if (this.sound_seekBar.isNeedGesture()) {
                    this.sound_seekBar.setPro(1.0f);
                    return;
                }
                return;
            case 2131691645:
                if (this.music_seekBar.isNeedGesture()) {
                    this.music_seekBar.setPro(StaticLayoutUtil.DefaultSpacingadd);
                    return;
                }
                return;
            case 2131691646:
                if (this.music_seekBar.isNeedGesture()) {
                    this.music_seekBar.setPro(1.0f);
                    return;
                }
                return;
            case 2131691648:
                if (this.bright_seekBar.isNeedGesture()) {
                    this.bright_seekBar.setPro(StaticLayoutUtil.DefaultSpacingadd);
                    return;
                }
                return;
            case 2131691649:
                if (this.bright_seekBar.isNeedGesture()) {
                    this.bright_seekBar.setPro(1.0f);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setChildrenClickListener(OnClickListener listener) {
        this.video_edit_effect_root.setOnClickListener(listener);
    }

    public VideoEditEffectsLayout(Context context) {
        this(context, null);
    }

    public VideoEditEffectsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoEditEffectsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mListener = 1.lambdaFactory$(this);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(2130968935, this, true);
        ButterKnife.inject(this);
        initSpeedLoop();
        initZoomLoop();
        this.music_seekBar.setOnCustomProgressProStateChangeListener(new OnCustomProgressProStateChangeListener() {
            public void onPro0(boolean flag) {
            }

            public void onPro100(boolean flag) {
            }
        });
        this.sound_seekBar.setOnCustomProgressProStateChangeListener(new OnCustomProgressProStateChangeListener() {
            public void onPro0(boolean flag) {
            }

            public void onPro100(boolean flag) {
            }
        });
        this.music_max_iv.setOnClickListener(this.mListener);
        this.music_min_iv.setOnClickListener(this.mListener);
        this.sound_min_iv.setOnClickListener(this.mListener);
        this.sound_max_iv.setOnClickListener(this.mListener);
        this.brightness_max_iv.setOnClickListener(this.mListener);
        this.brightness_min_iv.setOnClickListener(this.mListener);
    }

    private void initZoomLoop() {
        List<Bean> beanList = new ArrayList();
        beanList.add(new Bean(getResources().getString(2131296563), "", null));
        beanList.add(new Bean(getResources().getString(2131296562), "", null));
        beanList.add(new Bean(getResources().getString(2131296561), "", null));
        this.zoom_loop.setItems(beanList);
        this.zoom_loop.setCurrentPosition(1);
        if (LocaleUtil.isChinese()) {
            this.zoom_loop.setItemsVisibleCount(9);
        } else {
            this.zoom_loop.setItemsVisibleCount(7);
        }
        this.zoom_loop.setNotLoop();
        this.zoom_loop.setTextSize(12.0f);
    }

    private void initSpeedLoop() {
        List<Bean> beanList = new ArrayList();
        beanList.add(new Bean(getResources().getString(2131296560), "", null));
        beanList.add(new Bean(getResources().getString(2131296559), "", null));
        beanList.add(new Bean(getResources().getString(2131296554), "", null));
        beanList.add(new Bean(getResources().getString(2131296558), "", null));
        beanList.add(new Bean(getResources().getString(2131296555), "", null));
        beanList.add(new Bean(getResources().getString(2131296552), "", null));
        beanList.add(new Bean(getResources().getString(2131296553), "", null));
        beanList.add(new Bean(getResources().getString(2131296551), "", null));
        this.speed_loop.setItems(beanList);
        this.speed_loop.setCurrentPosition(3);
        this.speed_loop.setNotLoop();
        this.speed_loop.setItemsVisibleCount(9);
        this.speed_loop.setTextSize(12.0f);
    }

    public void setThisVideoModel(LongVideosModel thisVideoModel, LongVideosModel videosModel) {
        resetUI(thisVideoModel, videosModel);
    }

    public void setMusic_seekBarEditable(boolean b, float percent) {
        if (this.music_seekBar != null) {
            this.music_seekBar.setEnabled(b);
            this.music_seekBar.setDAMP_FACTOR(percent);
            this.music_seekBar.setDrawDampLine(true);
        }
    }

    public void setSound_seekBarEditable(boolean b, float percent) {
        if (this.sound_seekBar != null) {
            this.sound_seekBar.setEnabled(b);
            this.sound_seekBar.setDAMP_FACTOR(percent);
            this.sound_seekBar.setDrawDampLine(true);
        }
    }

    public void resetUI(LongVideosModel model, LongVideosModel bgmModel) {
        if (this.speed_loop != null && this.zoom_loop != null && this.sound_seekBar != null && this.music_seekBar != null && this.bright_seekBar != null) {
            boolean b;
            this.mLastModel = model.cloneVideoTypeModel();
            switch (AnonymousClass3.$SwitchMap$com$blink$academy$onetake$VideoTools$FilterView$VideoSpeedup[model.getVideoSpeedUp().ordinal()]) {
                case 1:
                    this.speed_loop.setCurrentPosition(0);
                    break;
                case 2:
                    this.speed_loop.setCurrentPosition(1);
                    break;
                case 3:
                    this.speed_loop.setCurrentPosition(2);
                    break;
                case 4:
                    this.speed_loop.setCurrentPosition(3);
                    break;
                case 5:
                    this.speed_loop.setCurrentPosition(4);
                    break;
                case 6:
                    this.speed_loop.setCurrentPosition(5);
                    break;
                case 7:
                    this.speed_loop.setCurrentPosition(6);
                    break;
                case 8:
                    this.speed_loop.setCurrentPosition(7);
                    break;
            }
            if (model.getZoomStart() == 1.0f && model.getZoomEnd() == 1.0f) {
                this.zoom_loop.setCurrentPosition(1);
            } else if (model.getZoomStart() == 1.3f && model.getZoomEnd() == 1.0f) {
                this.zoom_loop.setCurrentPosition(0);
            } else if (model.getZoomStart() == 1.0f && model.getZoomEnd() == 1.3f) {
                this.zoom_loop.setCurrentPosition(2);
            }
            this.sound_seekBar.setDrawDampLine(true);
            this.sound_seekBar.setDAMP_FACTOR(model.getVideoDefaultVolume());
            this.music_seekBar.setDrawDampLine(true);
            this.music_seekBar.setDAMP_FACTOR(model.getAudioDefaultVolume());
            this.bright_seekBar.setDrawDampLine(true);
            this.bright_seekBar.setCanDrawText(false);
            this.bright_seekBar.setDAMP_FACTOR(0.5f);
            this.bright_seekBar.setNeedDamp01(true);
            this.sound_seekBar.setPro(model.getVideoVolume());
            this.bright_seekBar.setPro(getNeedBrightPro(model.getRealVideoExposure()));
            if (bgmModel != null) {
                b = true;
            } else {
                b = false;
            }
            if (b) {
                this.music_seekBar.setCanEdit(true);
                if (model.isHasEditAudioVolume()) {
                    this.music_seekBar.setPro(model.getAudioVolume());
                    return;
                } else {
                    this.music_seekBar.setPro(model.getAudioDefaultVolume());
                    return;
                }
            }
            this.music_seekBar.setCanEdit(false);
            this.music_seekBar.setPro(1.0f);
        }
    }

    private float getNeedBrightPro(float videoExposure) {
        return (1.0f * (5.0f + videoExposure)) / 10.0f;
    }

    public void cancelClick(LongVideosModel model) {
        if (TextUtil.isValidate(this.mLastModel) && TextUtil.isValidate(model)) {
            model.setVideoSpeedUp(this.mLastModel.getVideoSpeedUp());
            int value = -1;
            if (this.mLastModel.getZoomStart() == 1.3f && this.mLastModel.getZoomEnd() == 1.0f) {
                value = 2;
            } else if (this.mLastModel.getZoomStart() == 1.0f && this.mLastModel.getZoomEnd() == 1.0f) {
                value = 0;
            } else if (this.mLastModel.getZoomStart() == 1.0f && this.mLastModel.getZoomEnd() == 1.3f) {
                value = 1;
            }
            if (value != -1) {
                model.setZoomValue(value);
            }
            model.setAudioVolume(this.mLastModel.getAudioVolume());
            model.setVideoVolume(this.mLastModel.getVideoVolume());
            model.setVideoExposure(this.mLastModel.getRealVideoExposure());
        }
    }

    public int confirmClick(LongVideosModel model) {
        int state = -1;
        switch (this.speed_loop.getSelectedItem()) {
            case 0:
                if (model.getVideoSpeedUp() != VideoSpeedup.STEP) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.STEP);
                break;
            case 1:
                if (model.getVideoSpeedUp() != VideoSpeedup.SLOW) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.SLOW);
                break;
            case 2:
                if (model.getVideoSpeedUp() != VideoSpeedup.EIGHTMM) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.EIGHTMM);
                break;
            case 3:
                if (model.getVideoSpeedUp() != VideoSpeedup.NORMAL) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.NORMAL);
                break;
            case 4:
                if (model.getVideoSpeedUp() != VideoSpeedup.CHAPLIN) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.CHAPLIN);
                break;
            case 5:
                if (model.getVideoSpeedUp() != VideoSpeedup.DOUBLE) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.DOUBLE);
                break;
            case 6:
                if (model.getVideoSpeedUp() != VideoSpeedup.QUADRUPLE) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.QUADRUPLE);
                break;
            case 7:
                if (model.getVideoSpeedUp() != VideoSpeedup.TIMELAPSE) {
                    state = 0;
                }
                model.setVideoSpeedUp(VideoSpeedup.TIMELAPSE);
                break;
        }
        switch (this.zoom_loop.getSelectedItem()) {
            case 0:
                if (!(model.getZoomStart() == 1.3f && model.getZoomEnd() == 1.0f)) {
                    if (state == -1) {
                        state = 1;
                    } else {
                        state = 4;
                    }
                }
                model.setZoomValue(2);
                break;
            case 1:
                if (!(model.getZoomStart() == 1.0f && model.getZoomEnd() == 1.0f)) {
                    if (state == -1) {
                        state = 1;
                    } else {
                        state = 4;
                    }
                }
                model.setZoomValue(0);
                break;
            case 2:
                if (!(model.getZoomStart() == 1.0f && model.getZoomEnd() == 1.3f)) {
                    if (state == -1) {
                        state = 1;
                    } else {
                        state = 4;
                    }
                }
                model.setZoomValue(1);
                break;
        }
        if (model.isHasEditVideoVolume()) {
            if (model.getVideoVolume() != this.sound_seekBar.getProgressFloat()) {
                if (state == -1) {
                    state = 2;
                } else if (state == 0) {
                    state = 5;
                } else if (state == 1) {
                    state = 6;
                } else if (state == 4) {
                    state = 7;
                }
            }
        } else if (model.getVideoDefaultVolume() != this.sound_seekBar.getProgressFloat()) {
            if (state == -1) {
                state = 2;
            } else if (state == 0) {
                state = 5;
            } else if (state == 1) {
                state = 6;
            } else if (state == 4) {
                state = 7;
            }
            model.setHasEditVideoVolume(true);
        }
        model.setVideoVolume(this.sound_seekBar.getProgressFloat());
        if (this.music_seekBar.isNeedGesture()) {
            if (model.isHasEditAudioVolume()) {
                if (model.getAudioVolume() != this.music_seekBar.getProgressFloat()) {
                    if (state == -1) {
                        state = 3;
                    } else if (state == 0) {
                        state = 8;
                    } else if (state == 1) {
                        state = 9;
                    } else if (state == 2) {
                        state = 10;
                    } else if (state == 4) {
                        state = 11;
                    } else if (state == 5) {
                        state = 12;
                    } else if (state == 6) {
                        state = 13;
                    } else if (state == 7) {
                        state = 14;
                    }
                }
            } else if (model.getAudioDefaultVolume() != this.music_seekBar.getProgressFloat()) {
                if (state == -1) {
                    state = 3;
                } else if (state == 0) {
                    state = 8;
                } else if (state == 1) {
                    state = 9;
                } else if (state == 2) {
                    state = 10;
                } else if (state == 4) {
                    state = 11;
                } else if (state == 5) {
                    state = 12;
                } else if (state == 6) {
                    state = 13;
                } else if (state == 7) {
                    state = 14;
                }
                model.setHasEditAudioVolume(true);
            }
            model.setAudioVolume(this.music_seekBar.getProgressFloat());
        }
        return state;
    }

    public void setCancelOrConfirmClickListener(OnClickListener listener) {
        this.confirm_btn_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.cancel_btn_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.cancel_btn_iv.setOnClickListener(listener);
        this.confirm_btn_iv.setOnClickListener(listener);
    }

    public void setMusicSeekBarListener(OnCustomProgressChangeListener listener) {
        this.music_seekBar.setOnCustomProgressChangedListener(listener);
    }

    public void setBrightnessSeekBarListener(OnCustomProgressChangeListener listener) {
        this.bright_seekBar.setOnCustomProgressChangedListener(listener);
    }

    public void setSoundSeekBarListener(OnCustomProgressChangeListener listener) {
        this.sound_seekBar.setOnCustomProgressChangedListener(listener);
    }

    public void setOnSelectItemChange(OnSelectItemChange onSelectItemChange) {
        this.speed_loop.setOnSelectItemChange(onSelectItemChange);
        this.zoom_loop.setOnSelectItemChange(onSelectItemChange);
    }

    public void resetSpeedLoop(int size) {
        List<Bean> beanList = new ArrayList();
        beanList.add(new Bean(getResources().getString(2131296560), "", null));
        beanList.add(new Bean(getResources().getString(2131296559), "", null));
        beanList.add(new Bean(getResources().getString(2131296554), "", null));
        beanList.add(new Bean(getResources().getString(2131296558), "", null));
        beanList.add(new Bean(getResources().getString(2131296555), "", null));
        beanList.add(new Bean(getResources().getString(2131296552), "", null));
        beanList.add(new Bean(getResources().getString(2131296553), "", null));
        beanList.add(new Bean(getResources().getString(2131296551), "", null));
        List<Bean> finalList = new ArrayList();
        for (int i = 0; i < size; i++) {
            finalList.add(beanList.get(i));
        }
        this.speed_loop.setCurrentPosition(0);
        this.speed_loop.setItems(finalList);
    }
}