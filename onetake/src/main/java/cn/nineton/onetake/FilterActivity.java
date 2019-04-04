package cn.nineton.onetake;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;
import cn.nineton.onetake.adapter.BaseAdapter;
import cn.nineton.onetake.adapter.FilterAdapter;
import cn.nineton.onetake.adapter.FilterGroupNameAdapter;
import cn.nineton.onetake.adapter.SpecificCombinationAdapter;
import cn.nineton.onetake.adapter.UndoBean;
import cn.nineton.onetake.adapter.VideoEditOrderAdapter;
import cn.nineton.onetake.bean.AudioTrackBean;
import cn.nineton.onetake.bean.DraftInfoBean;
import cn.nineton.onetake.bean.DraftLongVideoBean;
import cn.nineton.onetake.bean.ErrorBean;
import cn.nineton.onetake.bean.FilterEffectBean;
import cn.nineton.onetake.bean.FilterEffectManager;
import cn.nineton.onetake.bean.FilterInfo;
import cn.nineton.onetake.bean.FilterModel;
import cn.nineton.onetake.bean.FilterPlayButtonBean;
import cn.nineton.onetake.bean.FirstVideoBean;
import cn.nineton.onetake.bean.ImagePropertyBean;
import cn.nineton.onetake.bean.LongVideosModel;
import cn.nineton.onetake.bean.MovieBean;
import cn.nineton.onetake.bean.MscvModel;
import cn.nineton.onetake.bean.OfficialTagBean;
import cn.nineton.onetake.bean.SpecificCombination;
import cn.nineton.onetake.bean.SpecificsBean;
import cn.nineton.onetake.bean.UndoEditModel;
import cn.nineton.onetake.bean.UndoFilterModel;
import cn.nineton.onetake.bean.UndoModel;
import cn.nineton.onetake.bean.VTFontDesBean;
import cn.nineton.onetake.bean.VideoBitmapsModel;
import cn.nineton.onetake.bean.VideoCoverInfo;
import cn.nineton.onetake.bean.VideoInputRatio;
import cn.nineton.onetake.controller.MSCVController;
import cn.nineton.onetake.event.CollectFilterEvent;
import cn.nineton.onetake.event.DeleteDownloadFilterEvent;
import cn.nineton.onetake.event.DestroyActivityEvent;
import cn.nineton.onetake.event.DraftAddEvent;
import cn.nineton.onetake.event.EnterCollectedModeEvent;
import cn.nineton.onetake.event.FilterActivityBackEvent;
import cn.nineton.onetake.event.FiltersDownloadEvent;
import cn.nineton.onetake.event.FinishActivityMessageEvent;
import cn.nineton.onetake.event.LongVideoBackEvent;
import cn.nineton.onetake.event.OfficialTagListEvent;
import cn.nineton.onetake.event.ProxyProgressEvent;
import cn.nineton.onetake.event.PublishDraftEvent;
import cn.nineton.onetake.event.RecyclePicModelEvent;
import cn.nineton.onetake.event.RefreshDisplayAlbumEvent;
import cn.nineton.onetake.event.RefreshFilterSortEvent;
import cn.nineton.onetake.event.VidAnalysisEvent;
import cn.nineton.onetake.event.VideoAudioUseEvent;
import cn.nineton.onetake.event.VideoDurationEvent;
import cn.nineton.onetake.event.VideoPreviewEvent;
import cn.nineton.onetake.event.VideoSavedEvent;
import cn.nineton.onetake.event.VideoSavingEvent;
import cn.nineton.onetake.listener.AlphaAnimatorCallback;
import cn.nineton.onetake.listener.AnimatorEndListener;
import cn.nineton.onetake.listener.FilterActivityContract;
import cn.nineton.onetake.listener.FilterGroupOnclickListener;
import cn.nineton.onetake.listener.FilterPlayButtonCallback;
import cn.nineton.onetake.listener.IControllerCallback;
import cn.nineton.onetake.listener.RecyclerViewItemClickListener;
import cn.nineton.onetake.listener.VTFontDialog;
import cn.nineton.onetake.media.BitmapUtils;
import cn.nineton.onetake.media.FrameRenderer;
import cn.nineton.onetake.media.MediaUtils;
import cn.nineton.onetake.media.OutputSurfaceArray;
import cn.nineton.onetake.media.VidStabilizer;
import cn.nineton.onetake.media.gpuimage.AFGPUImageMultiplyBlendFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageOverlayBlendFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageScreenBlendFilter;
import cn.nineton.onetake.media.gpuimage.AFGPUImageSkyFilter2;
import cn.nineton.onetake.media.gpuimage.AFGPUImageTransformFilter;
import cn.nineton.onetake.media.gpuimage.EGL10Helper;
import cn.nineton.onetake.media.gpuimage.EGLRunnableVoid;
import cn.nineton.onetake.media.gpuimage.Framebuffer;
import cn.nineton.onetake.media.gpuimage.FramebufferTexture;
import cn.nineton.onetake.media.gpuimage.GPUImageAlphaBlendFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageBilateralFilter0;
import cn.nineton.onetake.media.gpuimage.GPUImageCropFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageFilterGroup;
import cn.nineton.onetake.media.gpuimage.GPUImageGaussianSelectiveBlurFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageLookupFilter2;
import cn.nineton.onetake.media.gpuimage.GPUImageMirrorFilter;
import cn.nineton.onetake.media.gpuimage.GPUImagePrismFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageRenderer;
import cn.nineton.onetake.media.gpuimage.GPUImageSaturationFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageSharpenFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageToneCurveFilter;
import cn.nineton.onetake.media.gpuimage.GPUImageWhiteBalanceFilter;
import cn.nineton.onetake.media.gpuimage.Rotation;
import cn.nineton.onetake.media.interfaces.LongVideoPlayCallback;
import cn.nineton.onetake.media.videotool.AFGPUImageDateBlendFilter;
import cn.nineton.onetake.media.videotool.VidAnalysis;
import cn.nineton.onetake.media.videotool.VideoEncoder;
import cn.nineton.onetake.media.videotool.VideoEncoderSW;
import cn.nineton.onetake.presenter.FilterActivityPresenter;
import cn.nineton.onetake.util.ActiveListManager;
import cn.nineton.onetake.util.AnimationUtil;
import cn.nineton.onetake.util.AppMessage;
import cn.nineton.onetake.util.AudioStoreController;
import cn.nineton.onetake.util.ByteBufferUtils;
import cn.nineton.onetake.util.CameraVideoPathModel;
import cn.nineton.onetake.util.ColorFilterUtil;
import cn.nineton.onetake.util.Config;
import cn.nineton.onetake.util.Constants;
import cn.nineton.onetake.util.CornerDistanceModel;
import cn.nineton.onetake.util.CustomCropUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.DraftBoxManager;
import cn.nineton.onetake.util.DraftModel;
import cn.nineton.onetake.util.FileUtil;
import cn.nineton.onetake.util.FilterActivityHelper;
import cn.nineton.onetake.util.FilterDownloadModel;
import cn.nineton.onetake.util.FilterEffectCompare;
import cn.nineton.onetake.util.FilterViewUtils;
import cn.nineton.onetake.util.FontsUtil;
import cn.nineton.onetake.util.FramesHolder;
import cn.nineton.onetake.util.GlobalHelper;
import cn.nineton.onetake.util.ImageCacheUtils;
import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.util.MoveGestureDetector;
import cn.nineton.onetake.util.MovieFileUtil;
import cn.nineton.onetake.util.OptionSizeUtil;
import cn.nineton.onetake.util.PermissionUtil;
import cn.nineton.onetake.util.PriorityRunnable;
import cn.nineton.onetake.util.PriorityThreadPoolManager;
import cn.nineton.onetake.util.ProgramLoader;
import cn.nineton.onetake.util.SharedPrefUtil;
import cn.nineton.onetake.util.SharedPrefUtils;
import cn.nineton.onetake.util.SpannedUtil;
import cn.nineton.onetake.util.TextUtil;
import cn.nineton.onetake.util.TintColorUtil;
import cn.nineton.onetake.util.VideoAudioPlaybackManager;
import cn.nineton.onetake.util.VideoEditOrderLayoutManager;
import cn.nineton.onetake.util.VideoEditSwitchTabsHolder;
import cn.nineton.onetake.util.VideoModelHelper;
import cn.nineton.onetake.util.ViewUtil;
import cn.nineton.onetake.util.WaterMarkBitmapUtil;
import cn.nineton.onetake.util.WeakHandler;
import cn.nineton.onetake.util.ZHConverter;
import cn.nineton.onetake.widget.AudioTrimLayout;
import cn.nineton.onetake.widget.AudioWaveView;
import cn.nineton.onetake.widget.AvenirNextCondensedMediumTextView;
import cn.nineton.onetake.widget.AvenirNextCondensedRegularTextView;
import cn.nineton.onetake.widget.AvenirNextRegularTextView;
import cn.nineton.onetake.widget.CircleProgressBar;
import cn.nineton.onetake.widget.ColorChooseView;
import cn.nineton.onetake.widget.CustomCropAllView;
import cn.nineton.onetake.widget.CustomCropView;
import cn.nineton.onetake.widget.CustomSeekBar;
import cn.nineton.onetake.widget.FilterEffectSetRelativeLayout;
import cn.nineton.onetake.widget.FilterPlayButton;
import cn.nineton.onetake.widget.FilterView;
import cn.nineton.onetake.widget.HorizontalLoopView;
import cn.nineton.onetake.widget.InterpolatedFloat;
import cn.nineton.onetake.widget.LUTCreator;
import cn.nineton.onetake.widget.MirrorSeekBar;
import cn.nineton.onetake.widget.MyCustomSeekBar;
import cn.nineton.onetake.widget.Player;
import cn.nineton.onetake.widget.Playlist;
import cn.nineton.onetake.widget.RingBackgroundView;
import cn.nineton.onetake.widget.ShareBitmapEntity;
import cn.nineton.onetake.widget.StaticLayoutUtil;
import cn.nineton.onetake.widget.TextInputEvent;
import cn.nineton.onetake.widget.TextSizeView;
import cn.nineton.onetake.widget.VTBaseView;
import cn.nineton.onetake.widget.VTContainerView;
import cn.nineton.onetake.widget.VerticalTextView;
import cn.nineton.onetake.widget.VideoEditEffectsLayout;
import cn.nineton.onetake.widget.VideoEditHelper;
import cn.nineton.onetake.widget.VideoEditOrderItemDecoration;
import cn.nineton.onetake.widget.VideoMusicSplitLayout;
import cn.nineton.onetake.widget.VideoSplitSlideView;
import cn.nineton.onetake.widget.VolumeSlideView;
import jp.co.cyberagent.android.gpuimage.GPUImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint({"ClickableViewAccessibility"})
public class FilterActivity extends AppCompatActivity implements OnClickListener, FilterActivityContract.View, OnItemDragListener, VideoEditHelper.ChangeOtherCallback {
    public static final int ANIMATION_DURATION_100 = 100;
    public static final int ANIMATION_DURATION_200 = 200;
    public static final String BACK_OR_FRONT_CAMERA = "SeilfieIntent";
    public static final String BEAUTY_INTENT = "BeautyIntent";
    private static final long CHANEG_FILTER_TIME = 500;
    public static final String DATA_FROM = "data_from";
    public static final String DATA_TYPE = "data_type";
    public static final String DOWNLOAD_FILTER = "Downloads";
    private static final float DafaultScalePercent = 0.2f;
    private static final String DoubleTag = "double";
    public static final int EFFECT_STATE_CUTTING_CENTER_CIRCLE = 11;
    public static final int EFFECT_STATE_CUTTING_THREE_LAYER = 12;
    public static final int EFFECT_STATE_CUTTING_TRIANGLE_TRANSLATION = 13;
    public static final int EFFECT_STATE_DIM = 14;
    public static final int EFFECT_STATE_NONE = 0;
    public static float Encode_Video_Percent = 0.4f;
    private static final int FILTER_EFFECT_DATE_FONT_SIZE = 40;
    private static final int FILTER_EFFECT_DATE_PADDING = 15;
    private static final String FILTER_SOURCE = "filter_activity";
    public static final String FPS_INTENT = "FPS_INTENT";
    public static final String GIF_LOCAL_PATH = "gif_local_path";
    private static final String GestureTag = "gesture";
    public static final String HAS_AUDIO = "has_audio";
    public static final String IS_SQUARE_INTENT = "IS_SQUARE_INTENT";
    public static final String LOCATION_INTENT = "LocationIntent";
    public static final String LONG_VIDEO_MODELS = "longvideomodels";
    public static final float MORE_SHORT_LENGTH = 200.0f;
    private static final String MoveTag = "move";
    public static final String NONE_FILTER = "NONE";
    public static final String OFFSETX_INTENT = "OffsetXIntent";
    public static final String OFFSETY_INTENT = "OffsetYIntent";
    public static final String ORIENTATION_INTENT = "OrientationIntent";
    public static final String ORIENTATION_INTENT2 = "OrientationIntent2";
    public static final float PREVIEW_SHORT_LENGTH = 800.0f;
    public static final String RECORD_VIDEO = "record_video";
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE = 102;
    public static final float SHORT_LENGTH = 400.0f;
    private static final String ScaleTag = "scale";
    public static final int SlideLoopLevel_Normal = -1;
    public static final int SlideLoopLevel_Reverse = -2;
    public static final String TAG = FilterActivity.class.getSimpleName();
    public static final String TRANSFORM_INTENT = "TransformIntent";
    public static final String VIDEO_HEIGHT_INTENT = "VIDEO_HEIGHT_INTENT";
    public static final String VIDEO_ORITAION = "VIDEO_ORITAION";
    public static final String VIDEO_ROTATION = "VIDEO_ROTATION";
    public static final String VIDEO_WIDTH_INTENT = "VIDEO_WIDTH_INTENT";
    private int AnimationTime = 400;
    private int DafaultMoveXOffset = 100;
    private int DafaultMoveYOffset = 0;
    private int SLIDER_BORDER_WIDTH;
    private float SLIDER_DELTA;
    private int SLIDER_LINE_WIDTH;
    private int SLIDER_MAX_LEFT;
    private int SLIDER_MIN_LEFT;
    private int TAB_HEIGHT_1_4;
    private int UNLOOP_DELTA;
    private boolean addFinalTransformFilter = false;
    private boolean addFinalWhiteBalanceFilter = false;
    private int addMusicPosition = -1;
    private int addTextClickId;
    private OnClickListener addTextControllerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    TextView add_text_controller_chapter;
    TextView add_text_controller_info;
    TextView add_text_controller_reset;
    TextView add_text_controller_subtitle;
    TextView add_text_controller_title;
    private int alphaCount = 0;
    private Runnable alphaRannable = new Runnable() {
        public void run() {
            ValueAnimator.clearAllAnimations();
            capture_filter_name_rl.setAlpha(1.0f);
            capture_filter_name_rl.setVisibility(View.VISIBLE);
            valueAnimator = ValueAnimator.ofFloat(1.0f, StaticLayoutUtil.DefaultSpacingadd);
            valueAnimator.setDuration(200);
//            valueAnimator.addUpdateListener(Lambda.1.lambdaFactory$(this));
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    capture_filter_name_rl.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    capture_filter_name_rl.setVisibility(View.INVISIBLE);
                    capture_filter_name_rl.setAlpha(1.0f);
                    valueAnimator = null;
                }

                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    capture_filter_name_rl.setVisibility(View.VISIBLE);
                    capture_filter_name_rl.setAlpha(1.0f);
                }

                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    capture_filter_name_rl.setVisibility(View.VISIBLE);
                    capture_filter_name_rl.setAlpha(1.0f);
                    valueAnimator = null;
                }
            });
            valueAnimator.start();
        }

//        private /* synthetic */ void lambda$run$0(ValueAnimator animation) {
//            capture_filter_name_rl.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
//        }
    };
    GPUImageAlphaBlendFilter alpha_0;
    GPUImageAlphaBlendFilter alpha_1;
    private int animationHeight;
    private int animationWidth;
    @BindView(R.id.audio_trim_vs)
    ViewStub audio_trim_vs;
    private int beforeRotate_value_cropHeight;
    private float beforeRotate_value_cropPosition;
    private int beforeRotate_value_cropWidth;
    private int beforeRotate_value_height;
    private int beforeRotate_value_marginB;
    private int beforeRotate_value_marginL;
    private int beforeRotate_value_marginR;
    private int beforeRotate_value_marginT;
    private float beforeRotate_value_mirrorPercent;
    private float beforeRotate_value_mirrorValue;
    private int beforeRotate_value_width;
    @BindView(R.id.below_surface)
    android.view.View below_surface;
    private boolean canControlVideo;
    private boolean canVideoMuteIndicatorShow = true;
    private boolean canVideoOptDialogShow = true;
    @BindView(R.id.capture_filter_group_name_tv)
    AvenirNextRegularTextView capture_filter_group_name_tv;
    @BindView(R.id.capture_filter_name_rl)
    RelativeLayout capture_filter_name_rl;
    @BindView(R.id.capture_filter_name_tv)
    AvenirNextRegularTextView capture_filter_name_tv;
    private GestureDetector changeFilterGesture;
    private boolean changeSurfaceSize = false;
    private List<LongVideosModel> cloneTextModels;
    private List<FilterInfo> collectFilter = new ArrayList();
    private GPUImageToneCurveFilter contrastFilter;
    private float currentDegree = StaticLayoutUtil.DefaultSpacingadd;
    private FilterEffectManager.EffectType currentEffectType = FilterEffectManager.EffectType.NONE;
    GPUImageFilter currentFilter;
    String currentFilterName = NONE_FILTER;
    private float currentHvalue = StaticLayoutUtil.DefaultSpacingadd;
    private long currentPlayTimeUs;
    private float currentRotateValue = StaticLayoutUtil.DefaultSpacingadd;
    private float currentVvalue = StaticLayoutUtil.DefaultSpacingadd;
    @BindView(R.id.custom_crop_View)
    CustomCropView custom_crop_View;
    private AFGPUImageDateBlendFilter dateFilter;
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private GPUImageLookupFilter2 defaultVideoFilter;
    private ArrayList<Runnable> delayOrderRunnable;
    boolean displayUndoFilter = false;
    private float downX;
    private float downXLoop;
    private int drawTextViewOffsetX = 0;
    private VTContainerView draw_text_view;
    private AFGPUImageScreenBlendFilter dustFilter;
    private GPUImageToneCurveFilter exposureFilter;
    private GPUImageToneCurveFilter fadeFilter;
    Framebuffer fb0;
    Framebuffer fb1;
    private FilterAdapter filterAdapter;
    private int filterChoosePosition;
    private List<FilterEffectBean> filterEffectBeanList;
    private boolean filterEffectDetailIsShow;
    private ArrayList<ImageView> filterEffectLeakList;
    private FilterGroupNameAdapter filterGroupNameAdapter;
    private FilterInfo filterInfo;
    private boolean filterIsChanging;
    private boolean filterOnScroll;
    private android.view.View filter_add_text_parent;
    private android.view.View filter_addition_align;
    private android.view.View filter_addition_color;
    private android.view.View filter_addition_color_ll;
    private android.view.View filter_addition_delete;
    private android.view.View filter_addition_empty;
    private android.view.View filter_addition_font;
    private android.view.View filter_addition_line_space;
    private android.view.View filter_addition_loc;
    private android.view.View filter_addition_modify;
    private android.view.View filter_addition_parent;
    private android.view.View filter_addition_root_hsv;
    private android.view.View filter_addition_shader;
    private android.view.View filter_addition_shader_ll;
    private android.view.View filter_addition_size;
    private android.view.View filter_addition_size_ll;
    private RingBackgroundView filter_addition_size_ring;
    private android.view.View filter_addition_word_space;
    private RingBackgroundView filter_addition_word_space_ring;
    @BindView(R.id.filter_alpha_view)
    View filter_alpha_view;
    @BindView(R.id.filter_beauty_anim)
    View filter_beauty_anim;
    @BindView(R.id.filter_bottom_tab_move_point)
    ImageView filter_bottom_tab_move_point;
    CustomCropAllView filter_crop_ccav;
    @BindView(R.id.filter_crop_ccav_vs)
    ViewStub filter_crop_ccav_vs;
    @BindView(R.id.filter_current_clip_iv)
    android.view.View filter_current_clip_iv;
    MyCustomSeekBar filter_custom_sk;
    AvenirNextCondensedRegularTextView filter_detail_set_text;
    @BindView(R.id.filter_edit_icon_bottom)
    ImageView filter_edit_icon_bottom;
    FilterEffectSetRelativeLayout filter_effect_beauty;
    FilterEffectSetRelativeLayout filter_effect_contrast;
    FilterEffectSetRelativeLayout filter_effect_crop;
    FilterEffectSetRelativeLayout filter_effect_date;
    FilterEffectSetRelativeLayout filter_effect_dust;
    FilterEffectSetRelativeLayout filter_effect_exposure;
    FilterEffectSetRelativeLayout filter_effect_fade;
    FilterEffectSetRelativeLayout filter_effect_grain;
    FilterEffectSetRelativeLayout filter_effect_highlight_shadow;
    FilterEffectSetRelativeLayout filter_effect_horizontal;
    @BindView(R.id.filter_effect_icon_bottom)
    ImageView filter_effect_icon_bottom;
    FilterEffectSetRelativeLayout filter_effect_leak;
    @BindView(R.id.filter_effect_leak1)
    ImageView filter_effect_leak1;
    @BindView(R.id.filter_effect_leak2)
    ImageView filter_effect_leak2;
    @BindView(R.id.filter_effect_leak3)
    ImageView filter_effect_leak3;
    @BindView(R.id.filter_effect_leak4)
    ImageView filter_effect_leak4;
    @BindView(R.id.filter_effect_leak5)
    ImageView filter_effect_leak5;
    @BindView(R.id.filter_effect_leak_detail)
    android.view.View filter_effect_leak_detail;
    FilterEffectSetRelativeLayout filter_effect_mirror;
    FilterEffectSetRelativeLayout filter_effect_prism;
    @BindView(R.id.filter_effect_root_ll)
    View filter_effect_root_ll;
    FilterEffectSetRelativeLayout filter_effect_rotation;
    @BindView(R.id.filter_effect_rotation_rotate90)
    android.view.View filter_effect_rotation_rotate90;
    @BindView(R.id.filter_effect_rotation_rotate90_iv)
    ImageView filter_effect_rotation_rotate90_iv;
    @BindView(R.id.filter_effect_rotation_rotate90_tv)
    AvenirNextCondensedRegularTextView filter_effect_rotation_rotate90_tv;
    FilterEffectSetRelativeLayout filter_effect_saturation;
    android.view.View filter_effect_set_bts_hs;
    FilterEffectSetRelativeLayout filter_effect_set_five;
    FilterEffectSetRelativeLayout filter_effect_set_four;
    FilterEffectSetRelativeLayout filter_effect_set_one;
    FilterEffectSetRelativeLayout filter_effect_set_seven;
    FilterEffectSetRelativeLayout filter_effect_set_six;
    FilterEffectSetRelativeLayout filter_effect_set_three;
    FilterEffectSetRelativeLayout filter_effect_set_two;
    FilterEffectSetRelativeLayout filter_effect_set_zero;
    FilterEffectSetRelativeLayout filter_effect_shade_lighten;
    FilterEffectSetRelativeLayout filter_effect_sharpen;
    FilterEffectSetRelativeLayout filter_effect_sky;
    FilterEffectSetRelativeLayout filter_effect_temperature;
    FilterEffectSetRelativeLayout filter_effect_tilt;
    FilterEffectSetRelativeLayout filter_effect_tinge;
    FilterEffectSetRelativeLayout filter_effect_vertical;
    FilterEffectSetRelativeLayout filter_effect_vignette;
    @BindView(R.id.filter_filter_icon_bottom)
    ImageView filter_filter_icon_bottom;
    @BindView(R.id.filter_filter_root_rl)
    RelativeLayout filter_filter_root_rl;
    @BindView(R.id.filter_group_name_rv)
    RecyclerView filter_group_name_rv;
    @BindView(R.id.filter_list_recyclerview)
    RecyclerView filter_list_recyclerview;
    @BindView(R.id.filter_loop_gesture)
    android.view.View filter_loop_gesture;
    @BindView(R.id.filter_loop_preview_ll)
    LinearLayout filter_loop_preview_ll;
    @BindView(R.id.filter_loop_root_rl)
    android.view.View filter_loop_root_rl;
    @BindView(R.id.filter_loop_slider_root_rl)
    android.view.View filter_loop_slider_root_rl;
    @BindView(R.id.filter_loop_slider_root_rll)
    android.view.View filter_loop_slider_root_rll;
    @BindView(R.id.filter_loop_toggle_ancrt)
    AvenirNextCondensedRegularTextView filter_loop_toggle_ancrt;
    @BindView(R.id.filter_loop_toggle_img)
    ImageView filter_loop_toggle_img;
    @BindView(R.id.filter_loop_toggle_ll)
    android.view.View filter_loop_toggle_ll;
    @BindView(R.id.filter_max_click_view)
    android.view.View filter_max_click_view;
    @BindView(R.id.filter_missing_file_hint_rl)
    android.view.View filter_missing_file_hint_rl;
    FilterEffectSetRelativeLayout filter_music_add;
    FilterEffectSetRelativeLayout filter_music_delete;
    @BindView(R.id.filter_music_icon_bottom)
    ImageView filter_music_icon_bottom;
    LinearLayout filter_music_ll;
    FilterEffectSetRelativeLayout filter_music_split;
    FilterEffectSetRelativeLayout filter_music_volume;
    @BindView(R.id.filter_rule_ll)
    android.view.View filter_rule_ll;
    @BindView(R.id.filter_ruler_view_stub)
    ViewStub filter_ruler_view_stub;
    @BindView(R.id.filter_select_mode_current_iv)
    View filter_select_mode_current_iv;
    @BindView(R.id.filter_select_mode_current_rl)
    View filter_select_mode_current_rl;
    @BindView(R.id.filter_select_mode_current_tv)
    TextView filter_select_mode_current_tv;
    @BindView(R.id.filter_select_mode_global_iv)
    android.view.View filter_select_mode_global_iv;
    @BindView(R.id.filter_select_mode_global_rl)
    android.view.View filter_select_mode_global_rl;
    @BindView(R.id.filter_select_mode_global_tv)
    TextView filter_select_mode_global_tv;
    @BindView(R.id.filter_select_mode_rl)
    android.view.View filter_select_mode_rl;
    @BindView(R.id.filter_stablize_cpb)
    CircleProgressBar filter_stablize_cpb;
    @BindView(R.id.filter_stablize_iv)
    ImageView filter_stablize_iv;
    @BindView(R.id.filter_stablize_ll)
    android.view.View filter_stablize_ll;
    @BindView(R.id.filter_stablize_ra)
    android.view.View filter_stablize_ra;
    @BindView(R.id.filter_stablize_rl)
    android.view.View filter_stablize_rl;
    @BindView(R.id.filter_tab_bottom_cover_view)
    android.view.View filter_tab_bottom_cover_view;
    @BindView(R.id.filter_tab_center_area)
    android.view.View filter_tab_center_area;
    @BindView(R.id.filter_tab_center_line_parent)
    android.view.View filter_tab_center_line_parent;
    android.view.View filter_tab_choose_cancle;
    android.view.View filter_tab_choose_confirm;
    android.view.View filter_tab_choose_ll;
    @BindView(R.id.filter_tab_like_filter_ll)
    android.view.View filter_tab_like_filter_ll;
    @BindView(R.id.filter_tab_ll)
    android.view.View filter_tab_ll;
    @BindView(R.id.filter_tag_effect_iv)
    ImageView filter_tag_effect_iv;
    @BindView(R.id.filter_tag_effect_rl)
    RelativeLayout filter_tag_effect_rl;
    @BindView(R.id.filter_tag_filter_iv)
    ImageView filter_tag_filter_iv;
    @BindView(R.id.filter_tag_filter_rl)
    RelativeLayout filter_tag_filter_rl;
    @BindView(R.id.filter_tag_loop_iv)
    ImageView filter_tag_loop_iv;
    @BindView(R.id.filter_tag_loop_rl)
    RelativeLayout filter_tag_loop_rl;
    @BindView(R.id.filter_tag_music_iv)
    ImageView filter_tag_music_iv;
    @BindView(R.id.filter_tag_music_rl)
    RelativeLayout filter_tag_music_rl;
    @BindView(R.id.filter_title_bar)
    android.view.View filter_title_bar;
    @BindView(R.id.filter_unloop_gesture)
    android.view.View filter_unloop_gesture;
    @BindView(R.id.filter_unloop_root_rl)
    android.view.View filter_unloop_root_rl;
    @BindView(R.id.filter_unloop_slider_root_rl)
    android.view.View filter_unloop_slider_root_rl;
    @BindView(R.id.filter_view_stub)
    ViewStub filter_view_stub;
    private float[] finalTransformArray = null;
    Bitmap forFilterBitmap = null;
    private boolean fromEventShouldNotResume = false;
    private android.view.View gold_line_view;
    private GPUImageCropFilter gpuImageCropFilter;
    private AFGPUImageOverlayBlendFilter grainFilter;
    private boolean hadAddTransformFilter = false;
    private boolean hadAddWhiteBalanceFilter = false;
    private boolean hadInitializeFrameTexure = false;
    private boolean hadStartStablize = false;
    boolean hasAudioPermission = false;
    boolean hasRecordVideo = false;
    private boolean hasSavedDraft = false;
    private boolean hasTextBeforeShow = false;
    private GPUImageToneCurveFilter highLightFilter;
    private boolean isAfterInsertBlackAddText = false;
    private boolean isAudioReplace;
    private boolean isBackOrNextPressed = false;
    private boolean isCanChange = true;
    private boolean isChangeFilterCombination = false;
    private boolean isEditAddText = false;
    private boolean isEventFinish = false;
    private boolean isFingerOpenAnimation = false;
    private boolean isFirstClick = true;
    private boolean isFirstSaveDraft = false;
    boolean isIntentText = false;
    private boolean isMove = false;
    private boolean isPause2Publish = false;
    boolean isPressed;
    boolean isPressed2;
    private boolean isPressedVideoArea = false;
    private boolean isTextCenter = false;
    @BindView(R.id.iv_recyclerview_cache)
    ImageView iv_recyclerview_cache;
    private float lastDegree = -1.0f;
    private int lastDustIndex = -1;
    private int lastLeakIndex = -1;
    private int lastOrderDragEndPos = -1;
    private float lastProgressValue = -1.0f;
    private long lastSeekTime = 0;
    int lastSlideLevel = -2;
    private String lastTextChangeString = "";
    private AFGPUImageScreenBlendFilter leakFilter;
    private int leakFilterReqHeight;
    private int leakFilterReqWidth;
    private float leftEnd;
    private float leftEndLoop;
    private boolean leftSlide;
    private float leftStart;
    private float leftStartLoop;
    android.view.View leftTouchView;
    private Thread loadFramesThread;
    private Bitmap localPicture;
    int loopEnd = 5;
    private float loopLeftMargin;
    int loopStart;
    private String mActivityFrom;
    private AudioTrimLayout mAudioTrimLayout;
    private GPUImageBilateralFilter0 mBilateralFilter;
    private Bitmap mBitmapCamera;
    private Bitmap mBitmapDust1;
    private Bitmap mBitmapDust2;
    private Bitmap mBitmapDust3;
    private Bitmap mBitmapDust4;
    private Bitmap mBitmapDust5;
    private Bitmap mBitmapGrain;
    private Bitmap mBitmapLeak1;
    private Bitmap mBitmapLeak2;
    private Bitmap mBitmapLeak3;
    private Bitmap mBitmapLeak4;
    private Bitmap mBitmapSky;
    private Bundle mBundle;
    private int mCameraLensType;
    private int mCaptureMode;
    private int mCaptureOrientation;
    private int mChangeWHPercent = 0;
    private boolean mConntNext = false;
    private long mCreationDate;
    private long mCurClickTime = 0;
    public VTContainerView.AlignType mCurDefaultAlign;
    private VTContainerView.FontSizeType mCurDefaultFontSize;
    private int mCurDefaultVerticalPos = 0;
    private int mCurInsertPosition = 0;
    private FilterInfo mCurrentFilterInfo;
    private LongVideosModel mCurrentTextLongVideoModel;
    private String mCurrentTimeStamp;
    private int mDataFrom;//0:来自相机拍摄
    private int mDataType;//1:来自图片,0:来自视频，2:来自gif
    private Bitmap mDateFontBitmap;
    private int mDetermineVideoHeight;
    private int mDetermineVideoWidth;
    private DraftLongVideoBean mDraftLongVideoBean;
    private DraftModel mDraftModel;
    private boolean mEnableStabilizer;
    FilterActivityPresenter mFilterActivityPresenter;
    private ArrayList<FilterEffectSetRelativeLayout> mFilterEffectDetailSets = new ArrayList();
    private FilterEffectManager mFilterEffectManager;
    private LinearLayoutManager mFilterGroupNameManager;
    private List<FilterInfo> mFilterGroupNames;
    private ArrayList<FilterInfo> mFilterInfos;
    private LinearLayoutManager mFilterListLayoutManager;
    private FilterModel mFilterModel;
    private String mFilterName = NONE_FILTER;
    private int mFinalEffectState;
    private FirstVideoBean mFirstVideoBean = new FirstVideoBean();
    private float mFps;
    private int mFrameCount;
    private String mFromCurrentTimeStamp;
    private String mFromUserName;
    private GPUImageMirrorFilter mGPUImageMirrorFilter;
    private GestureDetectorCompat mGestureDetectorCompat;
    private String mGifPath;
    private WeakHandler mHandler = new WeakHandler(new Callback() {
        public boolean handleMessage(Message message) {
            return true;
        }
    });
    FilterActivityHelper mHelper = new FilterActivityHelper();
    private boolean mIsBackCamera;
    private boolean mIsCapitalized;
    boolean mIsPause = false;
    private boolean mIsShort;
    private boolean mIsSquare;
    private boolean mIsTraditional;
    private boolean mIsVideoLooped;
    @BindView(R.id.back_iv)
    android.view.View mIvBack;
    private GPUImageLookupFilter2 mLUTFilter = new GPUImageLookupFilter2();
    private FramebufferTexture mLUTTexture = null;
    private GestureDetector mLoopGestureDetector;
    private int mMaxSlideLevel;
    private int mMaxUnloopLevel;
    private MoveGestureDetector mMoveGestureDetector;
    private MovieBean mMovieBean;
    private float mOffsetX;
    private float mOffsetY;
    private VidAnalysis va = new VidAnalysis();
    OnClickListener mOnAdditionChangeListener = new OnClickListener() {
        public void onClick(android.view.View v) {
            clickAddition(v);
        }
    };
    private VTFontDialog.OnFontClickListener mOnFontClickListener = new VTFontDialog.OnFontClickListener() {
        public void OnFontClick(VTFontDesBean vtFontDesBean, int position) {
            boolean z = true;
            draw_text_view.setTypeface(vtFontDesBean);
            mIsTraditional = TextUtils.equals(vtFontDesBean.cnonly, Constants.IsTraditionalFont);
            FilterActivity filterActivity = FilterActivity.this;
            if (vtFontDesBean.capitalized != 1) {
                z = false;
            }
            filterActivity.mIsCapitalized = z;
            setTraditionalFont();
        }
    };
    OnClickListener mOnTextEditChangeListener = new OnClickListener() {
        public void onClick(android.view.View v) {
            clickTextEdit(v);
        }
    };
    private OnVideoTextSelectListener mOnVideoTextSelectListener = new OnVideoTextSelectListener() {
        public void cancel(FilterEffectManager.EffectType effectType) {
            onCancelPress(effectType);
        }

        public void confirm(FilterEffectManager.EffectType effectType) {
            onConfirmPress(effectType);
            currentEffectType = FilterEffectManager.EffectType.NONE;
            setNormalFilter();
            setUndoData(0);
        }

        public void videoCropConfirm(FilterEffectManager.EffectType effectType) {
            restoreSaveAndBack();
            lastProgressValue = -1.0f;
            FilterEffectBean bean = mFilterEffectManager.createBean(effectType, new float[]{filter_custom_sk.getMin(), filter_custom_sk.getProgressFloat(), filter_custom_sk.getMax()}, new float[]{StaticLayoutUtil.DefaultSpacingadd});
            if (effectType != FilterEffectManager.EffectType.STRENGTH) {
                alphaEnterAndExit(filter_tab_ll, filter_tab_choose_ll, new AnimatorEndListener() {
                    public void onAnimationEnd(Animator animation) {
                        filter_tab_ll.setVisibility(View.VISIBLE);
                        filter_tab_choose_confirm.setEnabled(true);
                    }
                });
                setFilterEffectGone();
            }
            saveVideoTypeOriginWHPercent(mChangeWHPercent);
            custom_crop_View.refreshPercentValue();
            custom_crop_View.setVisibility(View.GONE);
            for (int i = 0; i < mFilterEffectDetailSets.size(); i++) {
                if (((FilterEffectSetRelativeLayout) mFilterEffectDetailSets.get(i)).getIsSelect()) {
                    bean.value = new float[]{(float) i};
                }
            }
            setViewColor(filter_effect_crop, mFilterEffectManager.setSelectEffect(bean));
            if (mFilterEffectManager.setSelectEffect(bean)) {
                filter_effect_crop.setProgressValue(10.0f);
            } else {
                filter_effect_crop.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
            }
            mDetermineVideoHeight = mVideoHeight;
            mDetermineVideoWidth = mVideoWidth;
            saveVideoWHToBitmapManager();
            setUndoData(1);
            changeTextModelsSize(true);
        }

        public void videoCropCancel(FilterEffectManager.EffectType effectType) {
            restoreSaveAndBack();
            alphaEnterAndExit(filter_tab_ll, filter_tab_choose_ll, new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    filter_tab_ll.setVisibility(View.VISIBLE);
                    filter_tab_choose_cancle.setEnabled(true);
                }
            });
            setFilterEffectGone();
            changeVideoPercent(getVideoTypeOriginWHPercent());
            mVideoHeight = mDetermineVideoHeight;
            mVideoWidth = mDetermineVideoWidth;
            saveVideoWHToBitmapManager();
            initCurrentClipIv(mVideoWidth, mVideoHeight);
            initMissingFileHint();
            initPlayBtn(mVideoWidth, mVideoHeight);
            changeTextModelsSize(false);
        }
    };
    private int mOriginVideoHeight;
    private int mOriginVideoWidth;
    private String mOutPath;
    private FilterActivityContract.Presenter mPresenter;
    private boolean mRestart;
    private ScaleGestureDetector mScaleGestureDetector;
    private int mShareType;
    private SpecificCombinationAdapter mSpecificCombinationAdapter;
    @BindView(R.id.filter_surfaceview)
    FilterView mSurfaceView;
    private String mTextContent = "";
    @BindView(R.id.filter_save)
    AvenirNextCondensedMediumTextView mTvSave;
    private GestureDetector mUnLoopGestureDetector;
    private ArrayList<VTFontDesBean> mVTFontDesBeanList;
    private VTFontDialog mVTFontDialog;
    private VideoAudioPlaybackManager mVideoAudioManager;
    VideoEditEffectsLayout mVideoEditEffectsLayout;
    VideoEditHelper mVideoEditHelper;
    VideoEditOrderAdapter mVideoEditOrderAdapter;
    VideoEditOrderLayoutManager mVideoEditOrderLayoutManager;
    VideoEditSwitchTabsHolder mVideoEditSwitchTabsHolder;
    private OutputSurfaceArray mVideoFrames;
    private int mVideoHeight;
    //VideoMusicDialog mVideoMusicDialog;
//    private VideoOptDialog mVideoOptDialog;
    private int mVideoRotation;
    private int mVideoWidth;
    private ZHConverter mZHConverter;
    private int maxLoopCount = 16;
    private int metricsHeight;
    private int metricsWidth;
    private int minLoopCount = 6;
    private int minUnLoopCount;
    private int[] mirrorSetIcon = new int[]{R.drawable.icon_20_effect_new_off, R.drawable.icon_20_effect_new_mirror_top, R.drawable.icon_20_effect_new_mirror_bottom, R.drawable.icon_20_effect_new_mirror_left, R.drawable.icon_20_effect_new_mirror_right};
    private String[] mirrorSetText;
    MirrorSeekBar mirror_seek_bar;
    @BindView(R.id.mirror_seek_bar_vs)
    ViewStub mirror_seek_bar_vs;
    private float moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
    private float moveDistanceY = StaticLayoutUtil.DefaultSpacingadd;
    float movedX;
    float movedX2;
    private AFGPUImageMultiplyBlendFilter movieFilter;
    private int needScreenHeight;
    private int needScreenWidth;
    float needSeekTo = -1.0f;
    private GPUImageFilter[] newFilters = null;
    private GPUImageFilter[] oldFilters = null;
    private boolean onViewAnimating;
    private boolean openStackFrame = false;
    @BindView(R.id.order_video_vs)
    ViewStub order_video_vs;
    private int orientation;
    private float otherValue = StaticLayoutUtil.DefaultSpacingadd;
    private double playbackPercent = 0.0d;
    @BindView(R.id.player_rl)
    FilterPlayButton player_rl;
    private Runnable pressVideoRunnable = new Runnable() {
        public void run() {
            if (!FilterActivity.NONE_FILTER.equals(mFilterName)) {
                isPressedVideoArea = true;
                currentFilterName = mFilterName;
                mFilterName = FilterActivity.NONE_FILTER;
                mFilterEffectManager.getLongClickFilterBeans();
                setNormalFilter();
            }
        }
    };
    private GPUImageFilter[] previouFilters = null;
    private GPUImagePrismFilter prismFilter;
    private float rightEnd;
    private float rightEndLoop;
    private float rightStart;
    private float rightStartLoop;
    android.view.View rightTouchView;
    private int rotate_but;
    private int rotation;
    private GPUImageSaturationFilter saturationFilter;
    @BindView(R.id.save_bottom_effect)
    AvenirNextRegularTextView save_bottom_effect;
    private float scaleFactor = 1.0f;
    private int scaleLock = 0;
    public OnScrollListener scrollListener = new OnScrollListener() {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState != 0) {
                filterAdapter.forbidEnterCollectMode();
            }
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            FilterActivity filterActivity = FilterActivity.this;
            filterActivity.scrolledX += dx;
            int first = mFilterListLayoutManager.findFirstVisibleItemPosition();
            String showGroupName = "";
            if (filter_list_recyclerview.getChildAt(0).getLeft() > 0) {
                return;
            }
            if (((RelativeLayout) filter_list_recyclerview.getChildAt(0)).getChildAt(0) instanceof VerticalTextView) {
                String trim = ((VerticalTextView) ((RelativeLayout) filter_list_recyclerview.getChildAt(0)).getChildAt(0)).getText().toString().trim();
            } else if (((RelativeLayout) filter_list_recyclerview.getChildAt(1)).getChildAt(0) instanceof VerticalTextView) {
                String secoundString = ((VerticalTextView) ((RelativeLayout) filter_list_recyclerview.getChildAt(1)).getChildAt(0)).getText().toString().trim();
                List<FilterInfo> mList = filterAdapter.getInfos();
                for (int i = 0; i < mList.size(); i++) {
                    FilterInfo info = (FilterInfo) mList.get(i);
                    if (info.isGroup) {
                        if (!secoundString.equals(info.name_chs) && !secoundString.equals(info.name_cht) && !secoundString.equals(info.name_en)) {
                            int languageCode = LocaleUtil.getLanguageCode();
                            String nameChs = info.name_chs;
                            if (TextUtil.isNull(nameChs)) {
                                nameChs = "";
                            }
                            String nameCht = info.name_cht;
                            if (TextUtil.isNull(nameCht)) {
                                nameCht = "";
                            }
                            String nameEn = info.name_en;
                            if (TextUtil.isNull(nameEn)) {
                                nameEn = "";
                            }
                            if (languageCode == 1) {
                                showGroupName = nameChs;
                            } else if (languageCode == 2) {
                                showGroupName = nameCht;
                            } else {
                                showGroupName = nameEn;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    };
    int scrolledX;
    private GPUImageToneCurveFilter shadowFilter;
    private GPUImageSharpenFilter sharpenFilter;
    private boolean shouldPause = false;
    private AFGPUImageSkyFilter2 skyFilter;
    int slideLevel;
    RecyclerView specific_combination_rl;
    @BindView(R.id.specific_combination_stub)
    ViewStub specific_combination_stub;
    @BindView(R.id.stack_frame_switch_rl)
    LinearLayout stack_frame_switch_rl;
    private long startTime;
    private float strengthValue = 1.0f;
    private LayoutParams surfaceParams;
    @BindView(R.id.surface_click_view)
    android.view.View surface_click_view;
    private VTContainerView.FontSizeType tempFontSizeType;
    private VTContainerView.LetterSpacingType tempLetterSpacingType;
    private VTContainerView.LineSpacingType tempLineSpacingType;
    private android.view.View text_draw_rl;
    private ImageView text_finger_iv;
    @BindView(R.id.text_move_left_right)
    RelativeLayout text_move_left_right;
    private GPUImageGaussianSelectiveBlurFilter tiltFilter;
    @BindView(R.id.title_add_text)
    AvenirNextRegularTextView title_add_text;
    @BindView(R.id.title_alert_out_time)
    AvenirNextRegularTextView title_alert_out_time;
    @BindView(R.id.title_bg_view)
    android.view.View title_bg_view;
    @BindView(R.id.title_preview)
    AvenirNextRegularTextView title_preview;
    OnTouchListener touch_listener = new OnTouchListener() {
        public boolean onTouch(android.view.View view, MotionEvent event) {
            LogUtil.d(FilterActivity.GestureTag, "======MotionEvent======" + event.toString());
            if (filter_edit_icon_bottom == null || filter_edit_icon_bottom.getVisibility() != View.VISIBLE) {
                return false;
            }
            if (event.getAction() == 0) {
                if (!TextUtil.isValidate(mTextContent)) {
                    return false;
                }
                if (draw_text_view.isOnTouchArea(event.getX(), event.getY(), drawTextViewOffsetX)) {
                    if (!isEditAddText) {
                        isEditAddText = true;
                        showAddTextEditLayout();
                    }
                } else if (!isEditAddText && (video_add_text_control_parent == null || video_add_text_control_parent.getVisibility() != View.VISIBLE)) {
                    return false;
                } else {
                    isEditAddText = false;
                    confirmAddText();
                    return false;
                }
            }
            if (event.getPointerCount() > 1) {
                scaleLock = 2;
            } else if (scaleLock <= 1 && filter_add_text_parent.getVisibility() == View.VISIBLE) {
                try {
                    mMoveGestureDetector.onTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (event.getAction() == 1) {
                scaleLock = scaleLock - 1;
                LogUtil.d(FilterActivity.GestureTag, "======ACTION_UP======" + event.getPointerCount());
                hideMoveIconView();
            } else if (event.getAction() == 0) {
                LogUtil.d(FilterActivity.GestureTag, "======ACTION_DOWN======" + event.getPointerCount());
                text_finger_iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), 2130838036));
                showMoveIconView();
            } else if (event.getAction() == BaseAdapter.Item.COLLECTION_DETAIL_HEAD_TYPE) {
                LogUtil.d(FilterActivity.GestureTag, "======ACTION_POINTER_2_UP======" + event.getPointerCount());
                hideScaleIconView();
            } else if (event.getAction() == 261) {
                LogUtil.d(FilterActivity.GestureTag, "======ACTION_POINTER_2_DOWN======" + event.getPointerCount());
                text_finger_iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), 2130838037));
                showScaleIconView();
            }
            mGestureDetectorCompat.onTouchEvent(event);
            return true;
        }
    };
    private float transLastHValue = StaticLayoutUtil.DefaultSpacingadd;
    private float transLastRValue = StaticLayoutUtil.DefaultSpacingadd;
    private float transLastVValue = StaticLayoutUtil.DefaultSpacingadd;
    private int transformCallbackCount;
    private AFGPUImageTransformFilter transformFilter;
    private int transformNeedCount = 0;
    private VidStabilizer.Transform[] transformsLooped;
    private VidStabilizer.Transform[] transformsStraight;
    private TranslateAnimation translateAnimationHide;
    private TranslateAnimation translateAnimationShow;
    float unLoopDownX;
    float unLoopLeftMargin;
    int unLoopWidth;
    int unloopEnd;
    int unloopStart;
    private int userLongVideoDuration;
//    private VidAnalysis va;
    ValueAnimator valueAnimator = null;
    private List<ImageView> videoEditMissingFootageIvList;
    private float videoRatio;
    RelativeLayout video_add_text_control_parent;
    @BindView(R.id.video_add_text_control_vs)
    ViewStub video_add_text_control_vs;
    ImageView video_edit_add_album_iv;
    FrameLayout video_edit_add_album_tab;
    TextView video_edit_add_album_tv;
    ImageView video_edit_add_cancel_iv;
    TextView video_edit_add_music_tv;
    ImageView video_edit_add_onemin_iv;
    FrameLayout video_edit_add_onemin_tab;
    TextView video_edit_add_onemin_tv;
    FrameLayout video_edit_add_parent;
    ImageView video_edit_add_textgap_iv;
    FrameLayout video_edit_add_textgap_tab;
    TextView video_edit_add_textgap_tv;
    @BindView(R.id.video_edit_add_type_tv)
    TextView video_edit_add_type_tv;
    @BindView(R.id.video_edit_add_vs)
    ViewStub video_edit_add_vs;
    android.view.View video_edit_audio_mute_indicator;
    TextView video_edit_audio_mute_tv;
    RecyclerView video_edit_audio_rv;
    android.view.View video_edit_audio_volume_line;
    VolumeSlideView video_edit_audio_volume_touch;
    @BindView(R.id.video_edit_effects_vs)
    ViewStub video_edit_effects_vs;
    TextView video_edit_image_mute_tv;
    RecyclerView video_edit_image_rv;
    android.view.View video_edit_image_volume_line;
    VolumeSlideView video_edit_image_volume_touch;
    FrameLayout video_edit_missing_footage_fl;
    android.view.View video_edit_mute_indicator;
    android.view.View video_edit_mute_indicator_cover;
    FrameLayout video_edit_parent_ll;
    @BindView(R.id.video_edit_parent_new_view_stub)
    ViewStub video_edit_parent_new_view_stub;
    RelativeLayout video_edit_remove_parent;
    @BindView(R.id.video_edit_remove_vs)
    ViewStub video_edit_remove_vs;
    @BindView(R.id.video_edit_text_parent_vs)
    ViewStub video_edit_text_parent_vs;
    RecyclerView video_edit_text_rv;
    RecyclerView video_edit_time_rv;
    TextView video_edit_video_duration_tv;
    @BindView(R.id.video_filter_add_text_vs)
    ViewStub video_filter_add_text_vs;
    CustomSeekBar video_music_audio_sound_seek;
    ImageView video_music_cancel_iv;
    ImageView video_music_confirm_iv;
    ImageView video_music_name_iv;
    android.view.View video_music_name_parent;
    TextView video_music_name_tv;
    @BindView(R.id.video_music_name_vs)
    ViewStub video_music_name_vs;
    AudioWaveView video_music_split_awv;
    ImageView video_music_split_cancel_iv;
    ImageView video_music_split_confirm_iv;
    VideoMusicSplitLayout video_music_split_parent;
    FrameLayout video_music_split_tv_parent;
    @BindView(R.id.video_music_split_vs)
    ViewStub video_music_split_vs;
    CustomSeekBar video_music_video_sound_seek;
    @BindView(R.id.video_music_view_stub)
    ViewStub video_music_view_stub;
    FrameLayout video_music_volume_ll;
    @BindView(R.id.video_music_volume_vs)
    ViewStub video_music_volume_vs;
    ImageView video_order_cancel_iv;
    ImageView video_order_confirm_iv;
    RelativeLayout video_order_parent_rl;
    RecyclerView video_order_rv;
    private ImageView video_text_cancel_iv;
    private ImageView video_text_confirm_iv;
    @BindView(R.id.video_time_slide_bar)
    android.view.View video_time_slide_bar;
    @BindView(R.id.video_time_slide_bar_backgroud_view)
    VideoSplitSlideView video_time_slide_bar_backgroud_view;
    private TextView vt_color_hint;
    private ColorChooseView vt_colorchoose;
    private ImageView vt_shader_1;
    private ImageView vt_shader_2;
    private ImageView vt_shader_3;
    private ImageView vt_shader_4;
    private ImageView vt_shader_5;
    private ImageView vt_shader_6;
    private TextView vt_shader_hint;
    private TextSizeView vt_textsize;
    private TextView vt_textsize_hint;
    private GPUImageWhiteBalanceFilter whiteBalanceFilter;

    public interface OnVideoTextSelectListener {
        void cancel(FilterEffectManager.EffectType effectType);

        void confirm(FilterEffectManager.EffectType effectType);

        void videoCropCancel(FilterEffectManager.EffectType effectType);

        void videoCropConfirm(FilterEffectManager.EffectType effectType);
    }

    private class FilterEffectOnClick implements OnClickListener {
        private FilterEffectOnClick() {
        }

//        /* synthetic */ FilterEffectOnClick(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public void onClick(android.view.View v) {
            final FilterEffectBean filterEffectBean = mFilterEffectManager.getFilterEffectBeanById(v.getId());
            currentEffectType = filterEffectBean.effectType;
            if (filterEffectBean.getShowType() == 3) {
                filterEffectBean.value[1] = filterEffectBean.value[1] == StaticLayoutUtil.DefaultSpacingadd ? 10.0f : StaticLayoutUtil.DefaultSpacingadd;
                onConfirmPress(currentEffectType);
                if (filterEffectBean.getItemSelect()) {
                    if (dateFilter == null) {
                        dateFilter = getDateFilter();
                        dateFilter.setCaptureOrientation(mCaptureOrientation, mCameraLensType == 1);
                    }
                    dateFilter.setBitmap(getDateBitmap());
                }
                setNormalFilter();
                setUndoData(0);
                return;
            }
            new Thread() {
                public void run() {
                    super.run();
                    loadFilterEffect(filterEffectBean);
                }
            }.start();
            hideSaveAndBack();
            v.setEnabled(false);
            initRulerViewStub();
            alphaEnterAndExit(filter_tab_choose_ll, filter_tab_ll);
            showFilterEffectRootView(v, filterEffectBean);
            if (filterEffectBean.getShowType() == 1) {
                showFilterEffectButton();
            } else if (filterEffectBean.getShowType() != 3) {
                showFilterEffectSeekBar();
                filter_custom_sk.setOnCustomProgressChangedListener(new FilterOnCustomProgressChangedListener(filterEffectBean.getFilterEffectBeanString()));
                float value = filterEffectBean.value[1];
                float selectPosition;
                if (filterEffectBean.effectType == FilterEffectManager.EffectType.HORIZONTAL) {
                    selectPosition = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                    if (selectPosition == 90.0f || selectPosition == 270.0f) {
                        value = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL).value[1];
                    }
                } else {
                    if (filterEffectBean.effectType == FilterEffectManager.EffectType.VERTICAL) {
                        selectPosition = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                        if (selectPosition == 90.0f || selectPosition == 270.0f) {
                            value = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL).value[1];
                        }
                    }
                }
                filter_custom_sk.isCenter(filterEffectBean.isCenterAdsorb()).type(filterEffectBean.effectType).min(filterEffectBean.value[0]).max(filterEffectBean.value[2]).pro(value).build();
            }
            int select;
            int leakDepth;
            int i;
            ImageView imageView;
            switch (v.getId()) {
                case R.id.filter_effect_dust:
                    filter_stablize_ra.setVisibility(View.GONE);
                    filter_effect_rotation_rotate90.setVisibility(View.GONE);
                    filter_effect_leak_detail.setVisibility(View.VISIBLE);
                    if (specific_combination_rl != null) {
                        specific_combination_rl.setVisibility(View.GONE);
                    }
                    select = (int) filterEffectBean.selectPosition[0];
                    leakDepth = (int) filterEffectBean.selectPosition[1];
                    final int size = filterEffectLeakList.size();
                    filter_effect_leak5.setVisibility(View.VISIBLE);
                    lastDustIndex = select;
                    LogUtil.d("filterEffectLeakList", "select  : " + select + " leakDepth : " + leakDepth);
                    for (i = 0; i < size; i++) {
                        imageView = (ImageView) filterEffectLeakList.get(i);
                        if (i == select) {
                            imageView.setAlpha(1.0f);
                        } else {
                            imageView.setAlpha(0.4f);
                        }
                        //imageView.setOnClickListener(FilterActivity$FilterEffectOnClick$.Lambda.1.lambdaFactory$(this, size, i));
                        final int finalI = i;
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lambda$onClick$0(size, finalI,v);
                            }
                        });
                    }
                    return;
                case R.id.filter_effect_leak:
                    filter_stablize_ra.setVisibility(View.GONE);
                    filter_effect_rotation_rotate90.setVisibility(View.GONE);
                    filter_effect_leak_detail.setVisibility(View.VISIBLE);
                    if (specific_combination_rl != null) {
                        specific_combination_rl.setVisibility(View.GONE);
                    }
                    select = (int) filterEffectBean.selectPosition[0];
                    leakDepth = (int) filterEffectBean.selectPosition[1];
                    filter_effect_leak5.setVisibility(View.GONE);
                    lastLeakIndex = select;
                    LogUtil.d("filterEffectLeakList", "select  : " + select + " leakDepth : " + leakDepth);
                    for (i = 0; i < 4; i++) {
                        imageView = (ImageView) filterEffectLeakList.get(i);
                        if (i == select) {
                            imageView.setAlpha(1.0f);
                        } else {
                            imageView.setAlpha(0.4f);
                        }
//                        imageView.setOnClickListener(2.lambdaFactory$(this, i));
                        final int finalI1 = i;
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lambda$onClick$1(finalI1,v);
                            }
                        });
                    }
                    return;
                case R.id.filter_effect_date:
                    if (filterEffectBean.getItemSelect()) {
                        if (dateFilter == null) {
                            dateFilter = getDateFilter();
                            dateFilter.setCaptureOrientation(mCaptureOrientation, mCameraLensType == 1);
                        }
                        dateFilter.setBitmap(getDateBitmap());
                        return;
                    }
                    return;
                case R.id.filter_effect_crop:
                    if (isVideoType()) {
                        showCropView();
                        setSelectEffectDetail(getVideoTypeOriginWHPercent());
                        fixDateFilter();
                        for (i = 0; i < mFilterEffectDetailSets.size(); i++) {
                            ((FilterEffectSetRelativeLayout) mFilterEffectDetailSets.get(i)).setOnClickListener(new OnClickListener() {
                                public void onClick(android.view.View v) {
                                    setSelectEffectDetail(mFilterEffectDetailSets.indexOf(v));
                                }
                            });
                        }
                        return;
                    }
                    int width;
                    int height;
                    if (gpuImageCropFilter != null) {
                        gpuImageCropFilter.setCropRegion(new GPUImageCropFilter.CropRegion(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f));
                    }
                    fixCustomCropViewSize(currentDegree);
                    OutputSurfaceArray videoFrames = mSurfaceView.getFrameRenderer().getVideoFrames();
                    if (currentDegree == StaticLayoutUtil.DefaultSpacingadd || currentDegree == 180.0f) {
                        width = videoFrames.mCaptureWidth;
                        height = videoFrames.mCaptureHeight;
                    } else {
                        width = videoFrames.mCaptureHeight;
                        height = videoFrames.mCaptureWidth;
                    }
                    videoFrames.mCropWidth = width;
                    videoFrames.mCropHeight = height;
                    restoreLeakFilter();
                    showCropView();
                    setTransformFilterValue(mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).value[1]);
                    setSelectEffectDetail((int) filterEffectBean.value[0]);
                    if (filterEffectBean.value[0] == StaticLayoutUtil.DefaultSpacingadd) {
                        custom_crop_View.setVisibility(View.GONE);
                    } else {
                        custom_crop_View.setType((int) filterEffectBean.value[0]);
                        custom_crop_View.setLeftWidthHeight(filterEffectBean.selectPosition[0], filterEffectBean.selectPosition[1], filterEffectBean.selectPosition[2], filterEffectBean.selectPosition[3]);
                        custom_crop_View.setVisibility(View.VISIBLE);
                    }
                    for (i = 0; i < mFilterEffectDetailSets.size(); i++) {
                        ((FilterEffectSetRelativeLayout) mFilterEffectDetailSets.get(i)).setOnClickListener(new OnClickListener() {
                            public void onClick(android.view.View v) {
                                if (mFilterEffectDetailSets.indexOf(v) == 0) {
                                    custom_crop_View.setVisibility(View.GONE);
                                    setSelectEffectDetail(mFilterEffectDetailSets.indexOf(v));
                                    return;
                                }
                                custom_crop_View.setType(mFilterEffectDetailSets.indexOf(v));
                                custom_crop_View.setVisibility(View.VISIBLE);
                                setSelectEffectDetail(mFilterEffectDetailSets.indexOf(v));
                            }
                        });
                    }
                    fixDateFilter();
                    fixCropViewWhenHasRotate();
                    setNormalFilter();
                    return;
                case R.id.filter_effect_rotation:
                    initCropAllViewStub();
                    filter_stablize_ra.setVisibility(View.GONE);
                    filter_effect_rotation_rotate90.setVisibility(View.VISIBLE);
                    filter_effect_leak_detail.setVisibility(View.GONE);
                    if (specific_combination_rl != null) {
                        specific_combination_rl.setVisibility(View.GONE);
                    }
                    rotate_but = (int) filterEffectBean.selectPosition[0];
                    fixGridViewSize((float) rotate_but);
                    filter_crop_ccav.setVisibility(View.VISIBLE);
                    setValueBeforeRotate();
//                    filter_effect_rotation_rotate90.setOnTouchListener(3.lambdaFactory$(this));
//                    filter_effect_rotation_rotate90.setOnClickListener(4.lambdaFactory$(this));
                    filter_effect_rotation_rotate90.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return lambda$onClick$2(v,event);
                        }
                    });
                    filter_effect_rotation_rotate90.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lambda$onClick$3(v);
                        }
                    });
                    return;
                case R.id.filter_effect_vertical:
                    initCropAllViewStub();
                    fixGridViewSize(currentDegree);
                    filter_crop_ccav.setVisibility(View.VISIBLE);
                    return;
                case R.id.filter_effect_horizontal:
                    initCropAllViewStub();
                    fixGridViewSize(currentDegree);
                    filter_crop_ccav.setVisibility(View.VISIBLE);
                    return;
                case R.id.filter_effect_mirror:
                    initMirrorSeekViewStub();
                    for (i = 0; i < mFilterEffectDetailSets.size(); i++) {
                        if (i < 5) {
                            mFilterEffectDetailSets.get(i).setFilterEffectImage(mirrorSetIcon[i]);
                            mFilterEffectDetailSets.get(i).setFilterEffectText(mirrorSetText[i]);
                        } else {
                            mFilterEffectDetailSets.get(i).setVisibility(View.GONE);
                        }
                    }
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) filter_effect_set_four.getLayoutParams();
                    layoutParams.rightMargin = DensityUtil.dip2px(20.0f);
                    filter_effect_set_four.setLayoutParams(layoutParams);
                    fixMirrorViewSize(currentDegree);
                    final LayoutParams mirrorParams = (LayoutParams) mirror_seek_bar.getLayoutParams();
                    setSelectEffectDetail((int) filterEffectBean.value[0]);
                    for (i = 0; i < mFilterEffectDetailSets.size(); i++) {
//                        ((FilterEffectSetRelativeLayout) mFilterEffectDetailSets.get(i)).setOnClickListener(5.lambdaFactory$(this, mirrorParams));
                        mFilterEffectDetailSets.get(i).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lambda$onClick$4(mirrorParams,v);
                            }
                        });
                        if (((int) filterEffectBean.value[0]) == 0) {
                           mirror_seek_bar.setVisibility(View.GONE);
                        } else {
                            mirror_seek_bar.setMirrorSize((int) filterEffectBean.value[0]);
                           mirror_seek_bar.setVisibility(View.VISIBLE);
                            mirror_seek_bar.setProgress((int) filterEffectBean.selectPosition[0], mirrorParams.width, mirrorParams.height);
                        }
                    }
//                   mirror_seek_bar.setOnProgressChangedListener(6.lambdaFactory$(this));
                    mirror_seek_bar.setOnProgressChangedListener(new MirrorSeekBar.OnProgressChangedListener() {
                        @Override
                        public void onProgressChanged(float f, int i) {
                            lambda$onClick$5(f,i);
                        }
                    });
                    setNormalFilter();
                    return;
                default:
                    return;
            }
        }

        private /* synthetic */ void lambda$onClick$0(int size, int finalI, android.view.View v13) {
            int index = filterEffectLeakList.indexOf(v13);
            if (lastDustIndex != index) {
                lastDustIndex = index;
                dustFilter.restoreDepth();
            } else {
                dustFilter.onClickForDepth();
            }
            for (int j = 0; j < size; j++) {
                if (finalI == j) {
                    loadDustBitmap(finalI);
                    ((ImageView) filterEffectLeakList.get(j)).setAlpha(1.0f);
                } else {
                    ((ImageView) filterEffectLeakList.get(j)).setAlpha(0.4f);
                }
            }
        }

        private /* synthetic */ void lambda$onClick$1(int finalI, android.view.View v13) {
            int index = filterEffectLeakList.indexOf(v13);
            if (lastLeakIndex != index) {
                lastLeakIndex = index;
                leakFilter.restoreDepth();
            } else {
                leakFilter.onClickForDepth();
            }
            for (int j = 0; j < 4; j++) {
                if (finalI == j) {
                    loadLeakBitmap(finalI);
                    ((ImageView) filterEffectLeakList.get(j)).setAlpha(1.0f);
                } else {
                    ((ImageView) filterEffectLeakList.get(j)).setAlpha(0.4f);
                }
            }
        }

        private /* synthetic */ boolean lambda$onClick$2(android.view.View v12, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    filter_effect_rotation_rotate90_iv.setAlpha(0.4f);
                    break;
                case 1:
                case 3:
                    filter_effect_rotation_rotate90_iv.setAlpha(1.0f);
                    break;
            }
            return false;
        }

        private /* synthetic */ void lambda$onClick$3(android.view.View v1) {
            rotate_but = rotate_but + 90;
            if (rotate_but >= 360) {
                rotate_but = 0;
            }
            changeRotateValue((float) rotate_but);
        }

        private /* synthetic */ void lambda$onClick$4(LayoutParams mirrorParams, android.view.View v14) {
            int indexOf = mFilterEffectDetailSets.indexOf(v14);
            if (indexOf == 0) {
                mirror_seek_bar.setVisibility(View.GONE);
                setSelectEffectDetail(indexOf);
                mGPUImageMirrorFilter.setMirrorStartAndOrientation(1.0f, 1.0f, orientation);
                return;
            }
            setSelectEffectDetail(indexOf);
            mirror_seek_bar.setVisibility(View.VISIBLE);
            mirror_seek_bar.setMirrorSize(indexOf);
            mirror_seek_bar.setProgress(50, mirrorParams.width, mirrorParams.height);
            currentEffectType = FilterEffectManager.EffectType.MIRROR;
            switch (indexOf) {
                case 1:
                    mGPUImageMirrorFilter.setMirrorStartAndOrientation(0.5f, 1.0f, orientation);
                    return;
                case 2:
                    mGPUImageMirrorFilter.setMirrorStartAndOrientation(0.5f, 3.0f, orientation);
                    return;
                case 3:
                    mGPUImageMirrorFilter.setMirrorStartAndOrientation(0.5f, 4.0f, orientation);
                    return;
                case 4:
                    mGPUImageMirrorFilter.setMirrorStartAndOrientation(0.5f, 2.0f, orientation);
                    return;
                default:
                    return;
            }
        }

        private /* synthetic */ void lambda$onClick$5(float progress, int showType) {
            LogUtil.d("onProgressChanged", "progress/100f : " + progress);
            if (showType == 1) {
                mGPUImageMirrorFilter.setMirrorStartAndOrientation(progress, 1.0f, orientation);
            } else if (showType == 2) {
                mGPUImageMirrorFilter.setMirrorStartAndOrientation(progress, 3.0f, orientation);
            } else if (showType == 3) {
                mGPUImageMirrorFilter.setMirrorStartAndOrientation(progress, 4.0f, orientation);
            } else if (showType == 4) {
                mGPUImageMirrorFilter.setMirrorStartAndOrientation(progress, 2.0f, orientation);
            }
        }
    }

    private class FilterOnCustomProgressChangedListener implements MyCustomSeekBar.OnCustomProgressChangedListener {
        private String mFilterEffectTYpe;

        public FilterOnCustomProgressChangedListener(String filterEffectTYpe) {
            this.mFilterEffectTYpe = filterEffectTYpe;
        }

        public void setFilterEffectTYpe(String mFilterEffectTYpe) {
            this.mFilterEffectTYpe = mFilterEffectTYpe;
        }

        public void onProgressChanged(float progressFloat) {
            if (!(currentEffectType == FilterEffectManager.EffectType.ROTATE || currentEffectType == FilterEffectManager.EffectType.VERTICAL || currentEffectType == FilterEffectManager.EffectType.HORIZONTAL || mSpecificCombinationAdapter == null)) {
                int selectPosition = mSpecificCombinationAdapter.getSelected();
                if (!(selectPosition == -1 && selectPosition == filterGroupNameAdapter.getItemCount() - 1)) {
                    mSpecificCombinationAdapter.setSelected(-1);
                    mSpecificCombinationAdapter.notifyDataSetChanged();
                }
            }
            if (filterEffectDetailIsShow) {
                if (currentEffectType == FilterEffectManager.EffectType.ROTATE || currentEffectType == FilterEffectManager.EffectType.VERTICAL || currentEffectType == FilterEffectManager.EffectType.HORIZONTAL) {
                    if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                        filter_detail_set_text.setText(String.format("%s: %s", this.mFilterEffectTYpe, formatFloat1(progressFloat)));
                    } else {
                        filter_detail_set_text.setText(String.format("%s: +%s", this.mFilterEffectTYpe, formatFloat1(progressFloat)));
                    }
                } else if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                    filter_detail_set_text.setText(String.format("%s: %s", this.mFilterEffectTYpe, formatFloat(progressFloat)));
                } else {
                    filter_detail_set_text.setText(String.format("%s: +%s", this.mFilterEffectTYpe, formatFloat(progressFloat)));
                }
                if (lastProgressValue != progressFloat) {
                    lastProgressValue = progressFloat;
                    editFilterIntensity(progressFloat);
                }
            } else if (currentEffectType == FilterEffectManager.EffectType.ROTATE || currentEffectType == FilterEffectManager.EffectType.VERTICAL || currentEffectType == FilterEffectManager.EffectType.HORIZONTAL) {
                if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                    filter_detail_set_text.setText(String.format("%s: %s", new Object[]{this.mFilterEffectTYpe, Float.valueOf(formatFloat1(progressFloat))}));
                    return;
                }
                filter_detail_set_text.setText(String.format("%s: +%s", new Object[]{this.mFilterEffectTYpe, Float.valueOf(formatFloat1(progressFloat))}));
            } else if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                filter_detail_set_text.setText(String.format("%s: %s", new Object[]{this.mFilterEffectTYpe, Integer.valueOf(formatFloat(progressFloat))}));
            } else {
                filter_detail_set_text.setText(String.format("%s: +%s", new Object[]{this.mFilterEffectTYpe, Integer.valueOf(formatFloat(progressFloat))}));
            }
        }

        public void onProgressUp(float progressFloat) {
            if (currentEffectType == FilterEffectManager.EffectType.ROTATE || currentEffectType == FilterEffectManager.EffectType.VERTICAL || currentEffectType == FilterEffectManager.EffectType.HORIZONTAL) {
                if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                    filter_detail_set_text.setText(String.format("%s: %s", new Object[]{this.mFilterEffectTYpe, Float.valueOf(formatFloat1(progressFloat))}));
                } else {
                    filter_detail_set_text.setText(String.format("%s: +%s", new Object[]{this.mFilterEffectTYpe, Float.valueOf(formatFloat1(progressFloat))}));
                }
            } else if (progressFloat <= StaticLayoutUtil.DefaultSpacingadd) {
                filter_detail_set_text.setText(String.format("%s: %s", new Object[]{this.mFilterEffectTYpe, Integer.valueOf(formatFloat(progressFloat))}));
            } else {
                filter_detail_set_text.setText(String.format("%s: +%s", new Object[]{this.mFilterEffectTYpe, Integer.valueOf(formatFloat(progressFloat))}));
            }
            if (lastProgressValue != progressFloat) {
                lastProgressValue = progressFloat;
                editFilterIntensity(progressFloat);
            }
        }
    }

    public class FilterOnTouchListerner implements OnTouchListener {
        public boolean onTouch(android.view.View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    float lastAlahp = view.getAlpha();
                    view.setAlpha(0.3f);
                    break;
                case 1:
                case 3:
                    view.setAlpha(1.0f);
                    break;
            }
            return false;
        }
    }

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

//        /* synthetic */ GestureListener(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public boolean onDoubleTapEvent(MotionEvent event) {
            LogUtil.d(FilterActivity.GestureTag, "======onDoubleTapEvent======event.getAction():" + event.getAction());
            if (event.getAction() == 0) {
                mCurClickTime = System.currentTimeMillis();
            } else if (event.getAction() == 1) {
            }
            return super.onDoubleTapEvent(event);
        }
    }

    private class LoopListener implements OnGestureListener {
        private LoopListener() {
        }
//
//        /* synthetic */ LoopListener(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public synchronized boolean onDown(MotionEvent e) {
            boolean z = true;
            synchronized (this) {
                if (e.getRawX() < filter_loop_slider_root_rll.getX() - ((float) DensityUtil.dip2px(17.0f)) || e.getRawX() > (filter_loop_slider_root_rll.getX() + ((float) filter_loop_slider_root_rll.getMeasuredWidth())) + ((float) DensityUtil.dip2px(17.0f))) {
                    isPressed = false;
                    z = false;
                } else {
                    downXLoop = e.getRawX();
                    leftStartLoop = filter_loop_slider_root_rll.getX() - ((float) DensityUtil.dip2px(17.0f));
                    leftEndLoop = filter_loop_slider_root_rll.getX() + ((float) DensityUtil.dip2px(20.0f));
                    rightStartLoop = (filter_loop_slider_root_rll.getX() - ((float) DensityUtil.dip2px(10.0f))) + ((float) filter_loop_slider_root_rll.getMeasuredWidth());
                    rightEndLoop = (filter_loop_slider_root_rll.getX() + ((float) DensityUtil.dip2px(17.0f))) + ((float) filter_loop_slider_root_rll.getMeasuredWidth());
                    loopLeftMargin = filter_loop_slider_root_rll.getX();
                    isPressed = true;
                    movedX = StaticLayoutUtil.DefaultSpacingadd;
                }
            }
            return z;
        }

        public synchronized boolean onSingleTapUp(MotionEvent e) {
            LogUtil.d("LoopListener", "onSingleTapUp: " + e.getRawX());
            if (e.getRawX() < filter_loop_slider_root_rll.getX() - ((float) DensityUtil.dip2px(17.0f)) || e.getRawX() > (filter_loop_slider_root_rll.getX() + ((float) filter_loop_slider_root_rll.getMeasuredWidth())) + ((float) DensityUtil.dip2px(17.0f))) {
                float rawX = e.getRawX();
                int size = loopEnd - loopStart;
                int currentX = (int) ((rawX - ((float) DensityUtil.dip2px(84.0f))) / SLIDER_DELTA);
                int right = (int) (filter_loop_slider_root_rll.getX() + ((float) filter_loop_slider_root_rll.getMeasuredWidth()));
                if (rawX < ((float) ((int) filter_loop_slider_root_rll.getX()))) {
                    currentX -= size / 2;
                } else if (rawX > ((float) right)) {
                    currentX -= size / 2;
                }
                loopStart = currentX;
                loopEnd = currentX + size;
                if (loopStart < 0) {
                    loopStart = 0;
                    loopEnd = loopStart + size;
                }
                if (loopEnd > mFrameCount) {
                    loopEnd = mFrameCount;
                    loopStart = loopEnd - size;
                }
                if (loopEnd == mFrameCount) {
                    FilterActivity filterActivity = FilterActivity.this;
                    filterActivity.loopStart--;
                    filterActivity = FilterActivity.this;
                    filterActivity.loopEnd--;
                }
                LayoutParams layoutParams = (LayoutParams) filter_loop_slider_root_rll.getLayoutParams();
                layoutParams.leftMargin = (int) (((((float) loopStart) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(84.0f))) + 0.5f);
                int maxMargin = (int) ((((float) (metricsWidth - DensityUtil.dip2px(77.0f))) - ((((float) minLoopCount) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(20.0f)))) + 0.5f);
                if (layoutParams.leftMargin <= maxMargin) {
                    maxMargin = layoutParams.leftMargin;
                }
                layoutParams.leftMargin = maxMargin;
                filter_loop_slider_root_rll.setLayoutParams(layoutParams);
                slideLoop(loopStart, loopEnd);
            }
            return false;
        }

        public synchronized boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtil.d("joker", "e2.getX() : " + e2.getX() + " , e2.getRawX() : " + e2.getRawX());
            if (isPressed) {
                FilterActivity filterActivity = FilterActivity.this;
                filterActivity.movedX += distanceX;
                if (Math.abs(movedX) >= SLIDER_DELTA) {
                    int deltaLevel = -((int) (Math.abs(movedX) / movedX));
                    float deltaLevel2 = movedX / SLIDER_DELTA;
                    LayoutParams layoutParams = (LayoutParams) filter_loop_slider_root_rll.getLayoutParams();
                    FilterActivity filterActivity2;
                    int i;
                    int maxMargin;
                    int minMargin;
                    if (downXLoop > leftStartLoop && downXLoop < leftEndLoop) {
                        filterActivity = FilterActivity.this;
                        filterActivity.loopStart += deltaLevel;
                        if (loopStart < 0) {
                            loopStart = 0;
                        }
                        if (loopStart < (loopEnd + 1) - maxLoopCount) {
                            filterActivity2 = FilterActivity.this;
                            if ((loopEnd + 1) - maxLoopCount < 0) {
                                i = 0;
                            } else {
                                i = (loopEnd + 1) - maxLoopCount;
                            }
                            filterActivity2.loopStart = i;
                        } else {
                            if (loopStart > (loopEnd + 1) - minLoopCount) {
                                filterActivity2 = FilterActivity.this;
                                if ((loopEnd + 1) - minLoopCount < 0) {
                                    i = 0;
                                } else {
                                    i = (loopEnd + 1) - minLoopCount;
                                }
                                filterActivity2.loopStart = i;
                            }
                        }
                        if (loopStart >= 0 && loopEnd - loopStart >= minLoopCount - 1) {
                            int currentMin;
                            int currentMax;
                            if (loopEnd == mFrameCount) {
                                filterActivity = FilterActivity.this;
                                filterActivity.loopEnd--;
                                filterActivity = FilterActivity.this;
                                filterActivity.loopStart--;
                            }
                            int nowLeftmargin = layoutParams.leftMargin;
                            if ((loopEnd + 1) - maxLoopCount < 0) {
                                currentMin = 0;
                            } else {
                                currentMin = (loopEnd + 1) - maxLoopCount;
                            }
                            if ((loopEnd + 1) - minLoopCount < 0) {
                                currentMax = 0;
                            } else {
                                currentMax = (loopEnd + 1) - minLoopCount;
                            }
                            maxMargin = (int) ((((float) DensityUtil.dip2px(84.0f)) + (((float) currentMax) * SLIDER_DELTA)) + 0.5f);
                            minMargin = (int) ((((float) DensityUtil.dip2px(84.0f)) + (((float) currentMin) * SLIDER_DELTA)) + 0.5f);
                            int trueMargin = (int) ((((float) DensityUtil.dip2px(84.0f)) + (((float) ((int) ((e2.getRawX() - ((float) DensityUtil.dip2px(84.0f))) / SLIDER_DELTA))) * SLIDER_DELTA)) + 0.5f);
                            if (trueMargin < minMargin) {
                                trueMargin = minMargin;
                                filterActivity2 = FilterActivity.this;
                                if ((loopEnd + 1) - maxLoopCount < 0) {
                                    i = 0;
                                } else {
                                    i = (loopEnd + 1) - maxLoopCount;
                                }
                                filterActivity2.loopStart = i;
                            }
                            if (trueMargin > maxMargin) {
                                trueMargin = maxMargin;
                                filterActivity2 = FilterActivity.this;
                                if ((loopEnd + 1) - minLoopCount < 0) {
                                    i = 0;
                                } else {
                                    i = (loopEnd + 1) - minLoopCount;
                                }
                                filterActivity2.loopStart = i;
                            }
                            layoutParams.leftMargin = trueMargin;
                            layoutParams.width -= layoutParams.leftMargin - nowLeftmargin;
                            if (((float) layoutParams.width) < ((((float) minLoopCount) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f) {
                                layoutParams.width = (int) (((((float) minLoopCount) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f);
                            }
                            if (((float) layoutParams.width) > ((SLIDER_DELTA * ((float) maxLoopCount)) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f) {
                                layoutParams.width = (int) (((SLIDER_DELTA * ((float) maxLoopCount)) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f);
                            }
                            filter_loop_slider_root_rll.setLayoutParams(layoutParams);
                            slideLoop(loopStart, loopEnd);
                            mSurfaceView.pauseAtZero(false, true);
                        }
                    } else if (downXLoop <= rightStartLoop || downXLoop >= rightEndLoop) {
                        filterActivity = FilterActivity.this;
                        filterActivity.loopStart += deltaLevel;
                        if (loopStart < 0) {
                            filterActivity = FilterActivity.this;
                            filterActivity.loopStart++;
                            filterActivity = FilterActivity.this;
                            filterActivity.loopEnd++;
                        } else {
                            if (loopEnd - (loopStart - deltaLevel) < minLoopCount - 1) {
                                filterActivity = FilterActivity.this;
                                filterActivity.loopStart--;
                            }
                        }
                        filterActivity = FilterActivity.this;
                        filterActivity.loopEnd += deltaLevel;
                        if (loopEnd > mFrameCount) {
                            loopEnd = mFrameCount;
                            filterActivity = FilterActivity.this;
                            filterActivity.loopStart -= deltaLevel;
                        } else {
                            if ((loopEnd - deltaLevel) - (loopStart - deltaLevel) < minLoopCount - 1) {
                                filterActivity = FilterActivity.this;
                                filterActivity.loopEnd++;
                                filterActivity = FilterActivity.this;
                                filterActivity.loopStart -= deltaLevel;
                            }
                        }
                        if (loopStart >= 0 && loopEnd <= mFrameCount) {
                            if (loopEnd == mFrameCount) {
                                filterActivity = FilterActivity.this;
                                filterActivity.loopEnd--;
                                filterActivity = FilterActivity.this;
                                filterActivity.loopStart--;
                            }
                            int nowWidth = layoutParams.width;
                            layoutParams.leftMargin = (int) (((float) layoutParams.leftMargin) - (SLIDER_DELTA * deltaLevel2));
                            layoutParams.leftMargin = (int) ((loopLeftMargin + (e2.getRawX() - downXLoop)) + 0.5f);
                            minMargin = DensityUtil.dip2px(84.0f);
                            maxMargin = (metricsWidth - DensityUtil.dip2px(77.0f)) - nowWidth;
                            if (layoutParams.leftMargin < minMargin) {
                                layoutParams.leftMargin = minMargin;
                            }
                            if (layoutParams.leftMargin > maxMargin) {
                                layoutParams.leftMargin = maxMargin;
                            }
                            int currentStart = (int) ((((float) (layoutParams.leftMargin - DensityUtil.dip2px(84.0f))) / SLIDER_DELTA) + 0.5f);
                            layoutParams.leftMargin = (int) ((((float) DensityUtil.dip2px(84.0f)) + (((float) currentStart) * SLIDER_DELTA)) + 0.5f);
                            if (layoutParams.leftMargin < minMargin) {
                                layoutParams.leftMargin = minMargin;
                            }
                            if (layoutParams.leftMargin > maxMargin) {
                                layoutParams.leftMargin = maxMargin;
                            }
                            int size = loopEnd - loopStart;
                            filterActivity2 = FilterActivity.this;
                            if (currentStart < 0) {
                                i = 0;
                            } else {
                                i = currentStart;
                            }
                            filterActivity2.loopStart = i;
                            loopEnd = currentStart + size > mFrameCount ? mFrameCount - 1 : currentStart + size;
                            filter_loop_slider_root_rll.setLayoutParams(layoutParams);
                            slideLoop(loopStart, loopEnd);
                            mSurfaceView.pauseAtZero(false, true);
                        }
                    } else {
                        filterActivity = FilterActivity.this;
                        filterActivity.loopEnd += deltaLevel;
                        if (loopEnd > mFrameCount) {
                            loopEnd = mFrameCount;
                        }
                        if (loopEnd > (loopStart + maxLoopCount) - 1) {
                            filterActivity2 = FilterActivity.this;
                            if ((loopStart + maxLoopCount) - 1 > mFrameCount - 1) {
                                i = mFrameCount - 1;
                            } else {
                                i = (loopStart + maxLoopCount) - 1;
                            }
                            filterActivity2.loopEnd = i;
                        } else {
                            if (loopEnd < (loopStart + minLoopCount) - 1) {
                                filterActivity2 = FilterActivity.this;
                                if ((loopStart + minLoopCount) - 1 > mFrameCount - 1) {
                                    i = mFrameCount - 1;
                                } else {
                                    i = (loopStart + minLoopCount) - 1;
                                }
                                filterActivity2.loopEnd = i;
                            }
                        }
                        if (loopEnd <= mFrameCount && loopEnd - loopStart >= minLoopCount - 1) {
                            if (loopEnd == mFrameCount) {
                                filterActivity = FilterActivity.this;
                                filterActivity.loopEnd--;
                            }
                            int newWidth = (int) (((((float) ((int) ((((float) (((int) (e2.getRawX() - filter_loop_slider_root_rll.getX())) - DensityUtil.dip2px(20.0f))) / SLIDER_DELTA) + 0.5f))) * SLIDER_DELTA) + 0.5f) + ((float) DensityUtil.dip2px(20.0f)));
                            int maxWidth = (int) (((((float) maxLoopCount) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f);
                            int minWidth = (int) (((((float) minLoopCount) * SLIDER_DELTA) + ((float) DensityUtil.dip2px(20.0f))) + 0.5f);
                            if (newWidth < minWidth) {
                                newWidth = minWidth;
                                filterActivity2 = FilterActivity.this;
                                if ((loopStart + minLoopCount) - 1 > mFrameCount) {
                                    i = mFrameCount;
                                } else {
                                    i = (loopStart + minLoopCount) - 1;
                                }
                                filterActivity2.loopEnd = i;
                            }
                            if (newWidth > maxWidth) {
                                newWidth = maxWidth;
                                filterActivity2 = FilterActivity.this;
                                if ((loopStart + maxLoopCount) - 1 > mFrameCount) {
                                    i = mFrameCount;
                                } else {
                                    i = (loopStart + maxLoopCount) - 1;
                                }
                                filterActivity2.loopEnd = i;
                            }
                            layoutParams.width = newWidth;
                            filter_loop_slider_root_rll.setLayoutParams(layoutParams);
                            slideLoop(loopStart, loopEnd);
                            mSurfaceView.pauseAtZero(true, true);
                        }
                    }
                    movedX = StaticLayoutUtil.DefaultSpacingadd;
                }
            }
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        private MoveListener() {
        }

//        /* synthetic */ MoveListener(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public boolean onMoveBegin(MoveGestureDetector detector) {
            LogUtil.d(FilterActivity.GestureTag, "======onMoveBegin======");
            LogUtil.d(FilterActivity.MoveTag, "======onMoveBegin======");
            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
            moveDistanceY = StaticLayoutUtil.DefaultSpacingadd;
            DafaultMoveXOffset = (int) draw_text_view.getMoveXOffset();
            DafaultMoveYOffset = (int) App.getResource().getDimension(R.dimen.video_add_text_y_offset);
            mCurDefaultAlign = draw_text_view.getAlignType();
            mCurDefaultVerticalPos = draw_text_view.getVerticalPos();
            isMove = false;
            text_finger_iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_25_text_finger_move));
            showMoveIconView();
            return super.onMoveBegin(detector);
        }

        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            if (!(moveDistanceX == StaticLayoutUtil.DefaultSpacingadd && moveDistanceY == StaticLayoutUtil.DefaultSpacingadd && (Math.abs(d.x) > 50.0f || Math.abs(d.y) > 50.0f))) {
                moveDistanceX = moveDistanceX + d.x;
                moveDistanceY = moveDistanceY + d.y;
            }
            if (!isMove && Math.sqrt(Math.pow((double) moveDistanceX, 2.0d) + Math.pow((double) moveDistanceY, 2.0d)) >= 20.0d) {
                isMove = true;
            }
            if (isMove) {
                isMove = false;
                if (text_finger_iv.getVisibility() == View.GONE) {
                    text_finger_iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_25_text_finger_move));
                    showMoveIconView();
                }
            }
            switch (mCurDefaultAlign) {
                case L:
                    if (moveDistanceX >= StaticLayoutUtil.DefaultSpacingadd) {
                        if (moveDistanceX >= ((float) DafaultMoveXOffset)) {
                            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                            mCurDefaultAlign = VTContainerView.AlignType.M;
                            break;
                        }
                    }
                    moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                    break;

                case M:
                    if (moveDistanceX > ((float) (-DafaultMoveXOffset))) {
                        if (moveDistanceX >= ((float) DafaultMoveXOffset)) {
                            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                            mCurDefaultAlign = VTContainerView.AlignType.R;
                            break;
                        }
                    }
                    moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                    mCurDefaultAlign = VTContainerView.AlignType.L;
                    break;

                case R:
                    if (moveDistanceX > ((float) (-DafaultMoveXOffset))) {
                        if (moveDistanceX > StaticLayoutUtil.DefaultSpacingadd) {
                            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                            break;
                        }
                    }
                    moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
                    mCurDefaultAlign = VTContainerView.AlignType.M;
                    break;

            }
            LogUtil.d(VTContainerView.TAG, "mCurDefaultVerticalPos:" + mCurDefaultVerticalPos + ", verticalPos:" + (((float) mCurDefaultVerticalPos) + moveDistanceY));
            setTextAlign(mCurDefaultAlign, (int) (((double) mCurDefaultVerticalPos) + (Math.ceil((double) (moveDistanceY / ((float) DafaultMoveYOffset))) * ((double) DafaultMoveYOffset))));
            LogUtil.d(FilterActivity.GestureTag, "======onMove======PointF:x:" + d.x + ", y:" + d.y + ", moveDistanceX:" + moveDistanceX + ", moveDistanceY:" + moveDistanceY);
            LogUtil.d(FilterActivity.MoveTag, "======onMove======PointF:x:" + d.x + ", y:" + d.y + ", moveDistanceX:" + moveDistanceX + ", moveDistanceY:" + moveDistanceY);
            return true;
        }

        public void onMoveEnd(MoveGestureDetector detector) {
            LogUtil.d(FilterActivity.GestureTag, "======onMoveEnd======");
            LogUtil.d(FilterActivity.MoveTag, "======onMoveEnd======");
            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
            moveDistanceY = StaticLayoutUtil.DefaultSpacingadd;
            isMove = false;
            super.onMoveEnd(detector);
        }
    }

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

//        /* synthetic */ ScaleListener(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            LogUtil.d(FilterActivity.GestureTag, "======onScaleBegin======");
            LogUtil.d(FilterActivity.ScaleTag, "======onScaleBegin======");
            scaleFactor = 1.0f;
            mCurDefaultFontSize = draw_text_view.getFontSizeType();
            text_finger_iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_25_text_finger_scale));
            showScaleIconView();
            return super.onScaleBegin(detector);
        }

        public boolean onScale(ScaleGestureDetector detector) {
            LogUtil.d(FilterActivity.GestureTag, "======onScale======" + detector.getScaleFactor());
            LogUtil.d(FilterActivity.ScaleTag, "======onScale======" + detector.getScaleFactor());
            scaleFactor = scaleFactor * detector.getScaleFactor();
            LogUtil.d("test", "======onScale======scaleFactor:" + scaleFactor);
            boolean isChange = false;
            switch (mCurDefaultFontSize) {
                case XS:
                    if (scaleFactor >= 1.0f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.S;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    break;
                case S:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.M;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.XS;
                    isChange = true;
                    break;
                case M:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.L;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.S;
                    isChange = true;
                    break;
                case L:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.XL;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.M;
                    isChange = true;
                    break;
                case XL:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.XXL;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.L;
                    isChange = true;
                    break;
                case XXL:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.XL3;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.XL;
                    isChange = true;
                    break;
                case XL3:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.2f) {
                            scaleFactor = 1.0f;
                            mCurDefaultFontSize = VTContainerView.FontSizeType.XL4;
                            isChange = true;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.XXL;
                    isChange = true;
                    break;
                case XL4:
                    if (scaleFactor > 0.8f) {
                        if (scaleFactor >= 1.0f) {
                            scaleFactor = 1.0f;
                            break;
                        }
                    }
                    scaleFactor = 1.0f;
                    mCurDefaultFontSize = VTContainerView.FontSizeType.XL3;
                    isChange = true;
                    break;
            }
            if (isChange) {
                setFontSize(mCurDefaultFontSize);
            }
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            LogUtil.d(FilterActivity.GestureTag, "======onScaleEnd======");
            LogUtil.d(FilterActivity.ScaleTag, "======onScaleEnd======");
            scaleFactor = 1.0f;
            moveDistanceX = StaticLayoutUtil.DefaultSpacingadd;
            moveDistanceY = StaticLayoutUtil.DefaultSpacingadd;
            mCurDefaultAlign = draw_text_view.getAlignType();
            mCurDefaultVerticalPos = draw_text_view.getVerticalPos();
            isMove = false;
            super.onScaleEnd(detector);
        }
    }

    public class Size {
        public float height;
        public float width;

        public Size(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    private class UnLoopListener implements OnGestureListener {
        private UnLoopListener() {
        }

//        /* synthetic */ UnLoopListener(FilterActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        public boolean onDown(MotionEvent e) {
            if (e.getRawX() < filter_unloop_slider_root_rl.getX() - ((float) DensityUtil.dip2px(17.0f)) || e.getRawX() > (filter_unloop_slider_root_rl.getX() + ((float) filter_unloop_slider_root_rl.getMeasuredWidth())) + ((float) DensityUtil.dip2px(17.0f))) {
                isPressed2 = false;
                return false;
            }
            downX = e.getRawX();
            leftStart = filter_unloop_slider_root_rl.getX() - ((float) DensityUtil.dip2px(17.0f));
            leftEnd = filter_unloop_slider_root_rl.getX() + ((float) DensityUtil.dip2px(27.0f));
            rightStart = leftStart + ((float) filter_unloop_slider_root_rl.getMeasuredWidth());
            rightEnd = leftEnd + ((float) filter_unloop_slider_root_rl.getMeasuredWidth());
            unLoopDownX = e.getRawX() - filter_unloop_slider_root_rl.getX();
            unLoopLeftMargin = filter_unloop_slider_root_rl.getX();
            isPressed2 = true;
            movedX2 = StaticLayoutUtil.DefaultSpacingadd;
            unLoopWidth = filter_unloop_slider_root_rl.getLayoutParams().width;
            LogUtil.d("huangweijie", "unloop=====Width : " + ((LayoutParams) filter_unloop_slider_root_rl.getLayoutParams()).width);
            return true;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (e.getRawX() < filter_unloop_slider_root_rl.getX() - ((float) DensityUtil.dip2px(17.0f)) || e.getRawX() > (filter_unloop_slider_root_rl.getX() + ((float) filter_unloop_slider_root_rl.getMeasuredWidth())) + ((float) DensityUtil.dip2px(17.0f))) {
                float rawX = e.getRawX();
                int size = unloopEnd - unloopStart;
                int currentX = (int) ((rawX - ((float) DensityUtil.dip2px(84.0f))) / ((float) UNLOOP_DELTA));
                int right = (int) (filter_unloop_slider_root_rl.getX() + ((float) filter_unloop_slider_root_rl.getMeasuredWidth()));
                if (rawX < ((float) ((int) filter_unloop_slider_root_rl.getX()))) {
                    currentX -= size / 2;
                } else if (rawX > ((float) right)) {
                    currentX -= size / 2;
                }
                unloopStart = currentX;
                unloopEnd = currentX + size;
                if (unloopStart < 0) {
                    unloopStart = 0;
                    unloopEnd = unloopStart + size;
                }
                if (unloopEnd > mFrameCount) {
                    unloopEnd = mFrameCount;
                    unloopStart = unloopEnd - size;
                }
                if (unloopEnd == mFrameCount) {
                    FilterActivity filterActivity = FilterActivity.this;
                    filterActivity.unloopStart--;
                    filterActivity = FilterActivity.this;
                    filterActivity.unloopEnd--;
                }
                LayoutParams layoutParams = (LayoutParams) filter_unloop_slider_root_rl.getLayoutParams();
                layoutParams.leftMargin = (int) (rawX - ((float) (filter_unloop_slider_root_rl.getMeasuredWidth() / 2)));
                int minMargin = DensityUtil.dip2px(84.0f);
                int maxMargin = (metricsWidth - DensityUtil.dip2px(84.0f)) - filter_unloop_slider_root_rl.getMeasuredWidth();
                if (layoutParams.leftMargin <= maxMargin) {
                    maxMargin = layoutParams.leftMargin;
                }
                layoutParams.leftMargin = maxMargin;
                if (layoutParams.leftMargin >= minMargin) {
                    minMargin = layoutParams.leftMargin;
                }
                layoutParams.leftMargin = minMargin;
                filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                slideUnloop(unloopStart, unloopEnd, false, false);
            }
            return false;
        }

        public synchronized boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean z;
            if (isPressed2) {
                FilterActivity filterActivity = FilterActivity.this;
                filterActivity.movedX2 += distanceX;
                if (Math.abs(movedX2) >= ((float) UNLOOP_DELTA)) {
                    LogUtil.d("huangweijie", "Math.abs(movedX2) >= UNLOOP_DELTA");
                    int deltaLevel = -((int) (Math.abs(movedX2) / movedX2));
                    float deltaLevel2 = movedX2 / ((float) UNLOOP_DELTA);
                    LayoutParams layoutParams = (LayoutParams) filter_unloop_slider_root_rl.getLayoutParams();
                    if (downX > leftStart && downX < leftEnd) {
                        LogUtil.d("huangweijie", "downX > leftStart && downX < leftEnd");
                        filterActivity = FilterActivity.this;
                        filterActivity.unloopStart += deltaLevel;
                        if (unloopStart < 0) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart++;
                        } else if (unloopEnd - unloopStart < minUnLoopCount - 1) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart--;
                        }
                        if (unloopStart >= 0 && unloopEnd - unloopStart >= minUnLoopCount - 1) {
                            int nowLeftmargin = layoutParams.leftMargin;
                            int trueMargin = (int) (e2.getRawX() - unLoopDownX);
                            layoutParams.leftMargin = trueMargin;
                            if (layoutParams.leftMargin < DensityUtil.dip2px(84.0f)) {
                                layoutParams.leftMargin = DensityUtil.dip2px(84.0f);
                                LogUtil.d("message", "layoutParams.leftMargin < DensityUtil.dip2px(31)");
                            }
                            int maxLeftmargin = (int) (unLoopLeftMargin + ((float) (unLoopWidth - (minUnLoopCount * UNLOOP_DELTA))));
                            LogUtil.d("message", "maxLeftmargin : " + maxLeftmargin);
                            if (layoutParams.leftMargin > maxLeftmargin) {
                                LogUtil.d("message", "layoutParams.leftMargin > ((Dens");
                                layoutParams.leftMargin = maxLeftmargin;
                                unloopStart = (unloopEnd - minUnLoopCount) + 1;
                            }
                            layoutParams.width -= layoutParams.leftMargin - nowLeftmargin;
                            if (layoutParams.width < minUnLoopCount * UNLOOP_DELTA) {
                                layoutParams.width = minUnLoopCount * UNLOOP_DELTA;
                                LogUtil.d("message", "layoutParams.width < CameraView2.FILTER_");
                            }
                            if (layoutParams.width > metricsWidth - DensityUtil.dip2px(148.0f)) {
                                layoutParams.width = metricsWidth - DensityUtil.dip2px(148.0f);
                                LogUtil.d("message", "layoutParams.width > (DensityUtil.getMetricsWid");
                            }
                            LogUtil.d("huangweijie", "layoutParams.width : " + layoutParams.width + " , layoutParams.leftMargin : " + layoutParams.leftMargin + " , trueMargin : " + trueMargin + " ， nowLeftmargin ： " + nowLeftmargin);
                            filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                            slideUnloop(unloopStart, unloopEnd, false, true);
                        }
                    } else if (downX <= rightStart || downX >= rightEnd) {
                        filterActivity = FilterActivity.this;
                        filterActivity.unloopStart += deltaLevel;
                        if (unloopStart < 0) {
                            LogUtil.d("huangweijie", "unloopStart < 0");
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart++;
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopEnd++;
                        } else if (unloopEnd - (unloopStart - deltaLevel) < minUnLoopCount - 1) {
                            LogUtil.d("huangweijie", "unloopEnd - (unloopStart - deltaLevel) < minUnLoopCount - 1");
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart--;
                        }
                        filterActivity = FilterActivity.this;
                        filterActivity.unloopEnd += deltaLevel;
                        LogUtil.d("huangweijie", "unloopStart : " + unloopStart + " , unloopEnd : " + unloopEnd + " , deltaLevel : " + deltaLevel);
                        if (unloopEnd > mFrameCount - 1) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopEnd--;
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart -= deltaLevel;
                            LogUtil.d("huangweijie", "unloopEnd > mFrameCount - 1 : start : " + unloopStart + " , end : " + unloopEnd);
                        } else if ((unloopEnd - deltaLevel) - (unloopStart - deltaLevel) < minUnLoopCount - 1) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopEnd++;
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopStart -= deltaLevel;
                            LogUtil.d("huangweijie", "unloopEnd - deltaLevel - (unloopStart - deltaLevel) < minUnLoopCount - 1");
                        }
                        if (unloopStart >= 0 && unloopEnd <= mFrameCount - 1) {
                            LogUtil.d("huangweijie", "unloopStart >= 0 && unloopEnd <= mFrameCount - 1");
                            int nowWidth = layoutParams.width;
                            layoutParams.leftMargin = (int) (((float) layoutParams.leftMargin) - (((float) UNLOOP_DELTA) * deltaLevel2));
                            filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                            layoutParams.leftMargin = (int) (unLoopLeftMargin + (e2.getRawX() - downX));
                            LogUtil.d("huangweijie", "layoutParams.width : " + layoutParams.width + " , layoutParams.leftMargin : " + layoutParams.leftMargin);
                            if (layoutParams.leftMargin < DensityUtil.dip2px(84.0f)) {
                                layoutParams.leftMargin = DensityUtil.dip2px(84.0f);
                            }
                            if (layoutParams.leftMargin > (metricsWidth - DensityUtil.dip2px(84.0f)) - nowWidth) {
                                layoutParams.leftMargin = (metricsWidth - DensityUtil.dip2px(84.0f)) - nowWidth;
                            }
                            filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                            slideUnloop(unloopStart, unloopEnd, false, true);
                        }
                    } else {
                        LogUtil.d("huangweijie", "downX > rightStart && downX < rightEnd");
                        filterActivity = FilterActivity.this;
                        filterActivity.unloopEnd += deltaLevel;
                        if (unloopEnd > mFrameCount - 1) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopEnd--;
                        } else if (unloopEnd - unloopStart < minUnLoopCount - 1) {
                            filterActivity = FilterActivity.this;
                            filterActivity.unloopEnd++;
                        }
                        if (unloopEnd <= mFrameCount - 1 && unloopEnd - unloopStart >= minUnLoopCount - 1) {
                            layoutParams.width = (int) (((float) layoutParams.width) - (((float) UNLOOP_DELTA) * deltaLevel2));
                            filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                            layoutParams.width = (int) (((float) unLoopWidth) + (e2.getRawX() - downX));
                            if (layoutParams.width < minUnLoopCount * UNLOOP_DELTA) {
                                layoutParams.width = minUnLoopCount * UNLOOP_DELTA;
                                unloopEnd = (unloopStart + minUnLoopCount) - 1;
                            }
                            if (layoutParams.width > metricsWidth - DensityUtil.dip2px(148.0f)) {
                                layoutParams.width = metricsWidth - DensityUtil.dip2px(148.0f);
                            }
                            LogUtil.d("huangweijie", "layoutParams.width : " + layoutParams.width + " , layoutParams.leftMargin : " + layoutParams.leftMargin);
                            filter_unloop_slider_root_rl.setLayoutParams(layoutParams);
                            slideUnloop(unloopStart, unloopEnd, true, true);
                        }
                    }
                    movedX2 = StaticLayoutUtil.DefaultSpacingadd;
                }
                z = true;
            } else {
                z = false;
            }
            return z;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private GPUImageFilter _getDefaultVideoFilter(GPUImageLookupFilter2 lookupFilter2) {
        if (lookupFilter2 != null) {
            return lookupFilter2;
        }
        lookupFilter2 = new GPUImageLookupFilter2();
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        GPUImageToneCurveFilter mContrastFilter = getToneCurveFilter();
        mContrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - -0.024f), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - 187.392f), new PointF(1.0f, 1.0f)});
        filterGroup.addFilter(mContrastFilter);
        GPUImageSaturationFilter mSaturationFilter = getSaturationFilter();
        mSaturationFilter.setSaturation(0.88f);
        filterGroup.addFilter(mSaturationFilter);
        GPUImageToneCurveFilter mFadeFilter = getToneCurveFilter();
        float v2 = (1.0f + ((1.0f - 5.0f) / 5.0f)) / 2.0f;
        mFadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * v2), new PointF(0.25f, 0.25f + (0.09f * v2)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * v2)), new PointF(1.0f, 1.0f - (0.15f * v2))});
        filterGroup.addFilter(mFadeFilter);
        GPUImageToneCurveFilter mHighLightFilter = getToneCurveFilter();
        float v3 = (1.0f + ((1.0f - 5.0f) / 5.0f)) / 2.0f;
        mHighLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * v3)), new PointF(0.75f, 0.75f - (0.13f * v3)), new PointF(1.0f, 1.0f - (0.23f * v3))});
        filterGroup.addFilter(mHighLightFilter);
        FramebufferTexture framebufferTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
        lookupFilter2.setExternalTexture(framebufferTexture.texid());
        this.mSurfaceView.updateLUT(filterGroup, framebufferTexture);
        return lookupFilter2;
    }

    private GPUImageFilter getDefaultVideoFilter(boolean encodeVideo) {
        if (encodeVideo) {
            return _getDefaultVideoFilter(null);
        }
        return _getDefaultVideoFilter(this.defaultVideoFilter);
    }

    private void initAddTextEditLayout() {
        if (this.video_filter_add_text_vs != null) {
            this.filter_add_text_parent = this.video_filter_add_text_vs.inflate();
            this.filter_add_text_parent.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                }
            });
            this.video_filter_add_text_vs = null;
            this.filter_addition_root_hsv = this.filter_add_text_parent.findViewById(R.id.filter_addition_root_hsv);
            this.filter_addition_parent = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_parent);
            this.filter_addition_modify = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_modify);
            this.filter_addition_empty = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_empty);
            this.filter_addition_font = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_font);
            this.filter_addition_size = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_size);
            this.filter_addition_color = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_color);
            this.filter_addition_word_space = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_word_space);
            this.filter_addition_word_space_ring = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_word_space_ring);
            this.filter_addition_size_ring = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_size_ring);
            VTContainerView.LineSpacingType[] lineTypes = VTContainerView.LineSpacingType.values();
            List<String> lineList = new ArrayList();
            for (VTContainerView.LineSpacingType type : lineTypes) {
                lineList.add(type.name());
            }
            this.filter_addition_word_space_ring.setData(lineList);
            VTContainerView.FontSizeType[] fontTypes = VTContainerView.FontSizeType.values();
            List<String> fontList = new ArrayList();
            for (VTContainerView.FontSizeType type2 : fontTypes) {
                fontList.add(type2.name());
            }
            this.filter_addition_size_ring.setData(fontList);
            if (this.mCurrentTextLongVideoModel != null) {
                int pos = Arrays.binarySearch(fontTypes, this.mCurrentTextLongVideoModel.getTextFontSizeType());
                if (pos < 0) {
                    pos = 2;
                }
                int pos2 = Arrays.binarySearch(lineTypes, this.mCurrentTextLongVideoModel.getTextLineSpacingType());
                if (pos2 < 0) {
                    pos2 = 0;
                }
                this.filter_addition_size_ring.setNowValue((String) fontList.get(pos));
                this.filter_addition_word_space_ring.setNowValue((String) lineList.get(pos2));
            } else {
                this.filter_addition_word_space_ring.setNowValue((String) lineList.get(0));
                this.filter_addition_size_ring.setNowValue((String) fontList.get(2));
            }
            this.filter_addition_line_space = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_line_space);
            this.filter_addition_align = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_align);
            this.filter_addition_shader = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_shader);
            this.filter_addition_loc = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_loc);
            this.filter_addition_delete = this.filter_addition_root_hsv.findViewById(R.id.filter_addition_delete);
            this.filter_addition_modify.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_font.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_size.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_color.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_word_space.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_line_space.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_align.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_shader.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_loc.setOnClickListener(this.mOnTextEditChangeListener);
            this.filter_addition_delete.setOnClickListener(this.mOnTextEditChangeListener);
            this.video_text_confirm_iv = this.filter_add_text_parent.findViewById(R.id.video_text_confirm_iv);
            this.video_text_cancel_iv = this.filter_add_text_parent.findViewById(R.id.video_text_cancel_iv);
            this.filter_addition_color_ll = this.filter_add_text_parent.findViewById(R.id.filter_addition_color_ll);
            this.vt_color_hint = this.filter_addition_color_ll.findViewById(R.id.vt_color_hint);
            this.vt_colorchoose = this.filter_addition_color_ll.findViewById(R.id.vt_colorchoose);
            this.filter_addition_shader_ll = this.filter_add_text_parent.findViewById(R.id.filter_addition_shader_ll);
            this.vt_shader_hint = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_hint);
            this.vt_shader_1 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_1);
            this.vt_shader_2 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_2);
            this.vt_shader_3 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_3);
            this.vt_shader_4 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_4);
            this.vt_shader_5 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_5);
            this.vt_shader_6 = this.filter_addition_shader_ll.findViewById(R.id.vt_shader_6);
//            this.vt_shader_1.setOnClickListener(FilterActivity$.Lambda.2.lambdaFactory$(this));
//            this.vt_shader_2.setOnClickListener(FilterActivity$.Lambda.3.lambdaFactory$(this));
//            this.vt_shader_3.setOnClickListener(FilterActivity$.Lambda.4.lambdaFactory$(this));
//            this.vt_shader_4.setOnClickListener(FilterActivity$.Lambda.5.lambdaFactory$(this));
//            this.vt_shader_5.setOnClickListener(FilterActivity$.Lambda.6.lambdaFactory$(this));
//            this.vt_shader_6.setOnClickListener(7.lambdaFactory$(this));
            vt_shader_1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);
                }
            });
            vt_shader_2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);
                }
            });
            vt_shader_3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);

                }
            });
            vt_shader_4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);

                }
            });
            vt_shader_5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);

                }
            });
            vt_shader_6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   //lambda$initAddTextEditLayout$6(v);
                    clickShadder(v);

                }
            });
            this.vt_shader_1.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.vt_shader_2.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.vt_shader_3.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.vt_shader_4.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.vt_shader_5.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.vt_shader_6.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
            this.filter_addition_size_ll = this.filter_add_text_parent.findViewById(R.id.filter_addition_size_ll);
            this.vt_textsize = (TextSizeView) this.filter_addition_size_ll.findViewById(R.id.vt_textsize);
            this.vt_textsize_hint = (TextView) this.filter_addition_size_ll.findViewById(R.id.vt_textsize_hint);
            int padding = (int) (((float) DensityUtil.getMetricsWidth(this)) * 0.1f);
            this.vt_textsize.setPadding(padding, 0, padding, 0);
//            this.video_text_confirm_iv.setOnClickListener(8.lambdaFactory$(this));
            video_text_confirm_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initAddTextEditLayout$6(v);
                }
            });
            this.video_text_cancel_iv.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    filter_addition_root_hsv.setVisibility(View.VISIBLE);
                    switch (addTextClickId) {
                        case R.id.filter_addition_size:
                            filter_addition_size_ll.setVisibility(View.GONE);
                            alphaEnterAndExit(filter_addition_root_hsv, filter_addition_size_ll);
                            if (mCurrentTextLongVideoModel == null) {
                                draw_text_view.setFontSizeType(tempFontSizeType);
                                break;
                            }
                            draw_text_view.setFontSizeType(mCurrentTextLongVideoModel.getTextFontSizeType());
                            tempFontSizeType = draw_text_view.getFontSizeType();
                            break;
                        case R.id.filter_addition_color:
                            filter_addition_color_ll.setVisibility(View.GONE);
                            alphaEnterAndExit(filter_addition_root_hsv, filter_addition_color_ll);
                            break;
                        case R.id.filter_addition_shader:
                            filter_addition_shader_ll.setVisibility(View.GONE);
                            alphaEnterAndExit(filter_addition_root_hsv, filter_addition_shader_ll);
                            break;
                        case R.id.filter_addition_word_space:
                            filter_addition_size_ll.setVisibility(View.GONE);
                            alphaEnterAndExit(filter_addition_root_hsv, filter_addition_size_ll);
                            if (mCurrentTextLongVideoModel == null) {
                                draw_text_view.setLetterSpacingType(tempLetterSpacingType);
                                break;
                            }
                            draw_text_view.setLetterSpacingType(mCurrentTextLongVideoModel.getTextLetterSpacingType());
                            tempLetterSpacingType = draw_text_view.getLetterSpacingType();
                            break;
                        case R.id.filter_addition_line_space:
                            filter_addition_size_ll.setVisibility(View.GONE);
                            alphaEnterAndExit(filter_addition_root_hsv, filter_addition_size_ll);
                            if (mCurrentTextLongVideoModel == null) {
                                draw_text_view.setLineSpacingType(tempLineSpacingType);
                                break;
                            }
                            mTextContent = mCurrentTextLongVideoModel.getTextContent();
                            draw_text_view.setLineSpacingType(mCurrentTextLongVideoModel.getTextLineSpacingType());
                            tempLineSpacingType = draw_text_view.getLineSpacingType();
                            break;
                        default:
                            if (hasTextBeforeShow) {
                                cancelEditAddTextOpt();
                            }
                            hideAddTextEditLayout();
                            isAfterInsertBlackAddText = false;
                            break;
                    }
                    addTextClickId = 0;
                }
            });
        }
    }

    private /* synthetic */ void lambda$initAddTextEditLayout$6(android.view.View v) {
        this.filter_addition_root_hsv.setVisibility(View.VISIBLE);
        switch (this.addTextClickId) {
            case R.id.filter_addition_size:
                this.filter_addition_size_ring.setNowValue(this.lastTextChangeString);
                this.filter_addition_size_ll.setVisibility(View.GONE);
                alphaEnterAndExit(this.filter_addition_root_hsv, this.filter_addition_size_ll);
                break;
            case R.id.filter_addition_color:
                this.filter_addition_color_ll.setVisibility(View.GONE);
                alphaEnterAndExit(this.filter_addition_root_hsv, this.filter_addition_color_ll);
                break;
            case R.id.filter_addition_shader:
                this.filter_addition_shader_ll.setVisibility(View.GONE);
                alphaEnterAndExit(this.filter_addition_root_hsv, this.filter_addition_shader_ll);
                break;
            case R.id.filter_addition_word_space:
                this.filter_addition_word_space_ring.setNowValue(this.lastTextChangeString);
                this.filter_addition_size_ll.setVisibility(View.GONE);
                alphaEnterAndExit(this.filter_addition_root_hsv, this.filter_addition_size_ll);
                break;
            case R.id.filter_addition_line_space:
                this.filter_addition_size_ll.setVisibility(View.GONE);
                alphaEnterAndExit(this.filter_addition_root_hsv, this.filter_addition_size_ll);
                break;
            default:
                confirmAddText();
                this.isAfterInsertBlackAddText = false;
                break;
        }
        this.addTextClickId = 0;
    }

    private int getAddTextOptionLeft() {
        int count = 0;
        if (this.filter_addition_modify != null && this.filter_addition_modify.getVisibility() == View.VISIBLE) {
            count = 0 + 1;
        }
        if (this.filter_addition_font != null && this.filter_addition_font.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_size != null && this.filter_addition_size.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_color != null && this.filter_addition_color.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_shader != null && this.filter_addition_shader.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_word_space != null && this.filter_addition_word_space.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_line_space != null && this.filter_addition_line_space.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_align != null && this.filter_addition_align.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_loc != null && this.filter_addition_loc.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (this.filter_addition_delete != null && this.filter_addition_delete.getVisibility() == View.VISIBLE) {
            count++;
        }
        LogUtil.d("slim", "count:" + count);
        if (count == 0) {
            return 0;
        }
        int left;
        int temp = (DensityUtil.getMetricsWidth(this) - (DensityUtil.dip2px(44.0f) * count)) - ((count - 1) * DensityUtil.dip2px(10.0f));
        if (temp > DensityUtil.dip2px(40.0f)) {
            left = temp / 2;
        } else {
            left = DensityUtil.dip2px(20.0f);
        }
        LogUtil.d("slim", "left:" + left + "  20dp:" + DensityUtil.dip2px(20.0f));
        return left;
    }

    private void createNewTextLongVideoModel(boolean listEmpty) {
        this.mCurrentTextLongVideoModel = new LongVideosModel(3);
        this.mCurrentTextLongVideoModel.setStartTime(this.mVideoEditHelper.getCurrentCenterTime());
        if (this.isAfterInsertBlackAddText) {
            long textStartTimeAfterInsertBlack = this.mVideoEditHelper.getTextStartTimeAfterInsertBlack(this.mCurInsertPosition);
            if (textStartTimeAfterInsertBlack >= 0) {
                this.mCurrentTextLongVideoModel.setStartTime(textStartTimeAfterInsertBlack);
            }
        }
        float sumTime = this.mVideoEditHelper.getSumTime();
        if (((float) this.mCurrentTextLongVideoModel.getStartTimeMs()) > sumTime - 1000.0f) {
            this.mCurrentTextLongVideoModel.setStartTime((long) (sumTime - 1000.0f));
            this.mCurrentTextLongVideoModel.setCurrentDuration(1000);
        } else if (((float) (this.mCurrentTextLongVideoModel.getStartTimeMs() + this.mCurrentTextLongVideoModel.getCurrentDurationValue())) > sumTime) {
            this.mCurrentTextLongVideoModel.setCurrentDuration((long) (sumTime - ((float) this.mCurrentTextLongVideoModel.getStartTimeMs())));
        }
        this.mCurrentTextLongVideoModel.setCacheStartTime(this.mCurrentTextLongVideoModel.getStartTimeMs());
        this.mCurrentTextLongVideoModel.setCacheDuration(this.mCurrentTextLongVideoModel.getCurrentDurationValue());
        if (listEmpty) {
            this.mVideoAudioManager.addTextModel(0, this.mCurrentTextLongVideoModel);
            return;
        }
        int pos = 0;
        List<LongVideosModel> list = this.mVideoAudioManager.getTextModelList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStartTimeMs() < this.mCurrentTextLongVideoModel.getStartTimeMs()) {
                pos++;
            }
        }
        if (pos < this.mVideoAudioManager.getTextModelList().size()) {
            LongVideosModel nextModel = this.mVideoAudioManager.getTextModelList().get(pos);
            if (this.mCurrentTextLongVideoModel.getStartTimeMs() + this.mCurrentTextLongVideoModel.getCurrentDurationValue() > nextModel.getStartTimeMs()) {
                this.mCurrentTextLongVideoModel.setCurrentDuration(nextModel.getStartTimeMs() - this.mCurrentTextLongVideoModel.getStartTimeMs());
                this.mCurrentTextLongVideoModel.setCacheStartTime(this.mCurrentTextLongVideoModel.getStartTimeMs());
                this.mCurrentTextLongVideoModel.setCacheDuration(this.mCurrentTextLongVideoModel.getCurrentDurationValue());
            }
        }
        this.mVideoAudioManager.addTextModel(pos, this.mCurrentTextLongVideoModel);
    }

    private void confirmAddText() {
        int index;
        if (this.draw_text_view.hasText()) {
            if (this.mVideoAudioManager.getTextModelList().size() == 0) {
                createNewTextLongVideoModel(true);
            } else {
                index = this.mVideoAudioManager.getTextModelList().indexOf(this.mCurrentTextLongVideoModel);
                if (index >= 0) {
                    this.mCurrentTextLongVideoModel = this.mVideoAudioManager.getTextModelByPosition(index);
                } else {
                    createNewTextLongVideoModel(false);
                }
            }
            this.mCurrentTextLongVideoModel.setVtHeight(this.draw_text_view.getViewHeight());
            this.mCurrentTextLongVideoModel.setTextAlignType(this.draw_text_view.getAlignType());
            this.mCurrentTextLongVideoModel.setTextLineSpacingType(this.draw_text_view.getLineSpacingType());
            this.mCurrentTextLongVideoModel.setTextColorType(this.draw_text_view.getTextColorType());
            this.mCurrentTextLongVideoModel.setTextFontSizeType(this.draw_text_view.getFontSizeType());
            this.mCurrentTextLongVideoModel.setTextLetterSpacingType(this.draw_text_view.getLetterSpacingType());
            this.mCurrentTextLongVideoModel.setTextShadowType(this.draw_text_view.getShadowType());
            this.mCurrentTextLongVideoModel.setTextVerticalPos(this.draw_text_view.getVerticalPos());
            this.mCurrentTextLongVideoModel.setTextContent(this.draw_text_view.getText().toString());
            this.mCurrentTextLongVideoModel.setTextTypeface(this.draw_text_view.getVtFontDesBean());
            this.mCurrentTextLongVideoModel.setTextColorType(this.draw_text_view.getTextColorType());
            this.mCurrentTextLongVideoModel.setTextSize(this.draw_text_view.getTextSize());
            this.mCurrentTextLongVideoModel.setShadowColor(this.draw_text_view.getShadowColor());
            this.mCurrentTextLongVideoModel.setLineSpacing(this.draw_text_view.getLineSpacing());
            this.mCurrentTextLongVideoModel.setLetterSpacing(this.draw_text_view.getLetterSpacing());
            this.mCurrentTextLongVideoModel.setVerticalPaddingSpacing(this.draw_text_view.getVerticalPaddingSpacing());
            this.mCurrentTextLongVideoModel.setyOffset(this.draw_text_view.getYOffset());
            this.mCurrentTextLongVideoModel.setRadius(this.draw_text_view.getRadius());
            this.mCurrentTextLongVideoModel.setShadowDx(this.draw_text_view.getShadowDx());
            this.mCurrentTextLongVideoModel.setShadowDy(this.draw_text_view.getShadowDy());
            this.mCurrentTextLongVideoModel.setFinalTexts(this.draw_text_view.getFinalTexts());
            this.mCurrentTextLongVideoModel.setStartY(this.draw_text_view.getStartY());
            this.mVideoAudioManager.changeTextModel(this.mVideoAudioManager.getTextModelList().indexOf(this.mCurrentTextLongVideoModel), this.mCurrentTextLongVideoModel);
        } else {
            if (this.mCurrentTextLongVideoModel != null) {
                List<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
                if (textModelList != null && textModelList.size() > 0) {
                    index = textModelList.indexOf(this.mCurrentTextLongVideoModel);
                    if (index >= 0) {
                        this.mVideoAudioManager.removeTextModel(index);
                    }
                }
            }
            this.mCurrentTextLongVideoModel = null;
        }
        hideAddTextEditLayout();
        setUndoData(1);
        this.mVideoEditHelper.refreshTextView(this.mVideoAudioManager.getTextModelList(), this.mCurrentTextLongVideoModel, true, true, -1, 0, false, true, false);
        showBottomCoverView();
    }

    private void showAddTextEditLayout() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVideoPauseOrResume(true);
        }
        hideTitleBarIconResetTitle();
        if (this.draw_text_view != null) {
            this.hasTextBeforeShow = this.draw_text_view.hasText();
            if (this.hasTextBeforeShow) {
                this.draw_text_view.setEdit(true);
            }
        }
        setPlayButtonVisibility(8);
        hideVideoEditView();
        showAddTextControlLayout();
        initAddTextEditLayout();
        if (this.draw_text_view == null) {
            this.filter_addition_line_space.setVisibility(View.GONE);
        } else if (this.draw_text_view.isMultiLines()) {
            this.filter_addition_line_space.setVisibility(View.VISIBLE);
        } else {
            this.filter_addition_line_space.setVisibility(View.GONE);
        }
        calculateAddTextViewPos();
        this.filter_add_text_parent.setVisibility(View.VISIBLE);
    }

    private void calculateAddTextViewPos() {
        int left = getAddTextOptionLeft();
        ViewGroup.LayoutParams layoutParams = this.filter_addition_empty.getLayoutParams();
        layoutParams.width = left;
        this.filter_addition_empty.setLayoutParams(layoutParams);
    }

    private void hideTitleBarIconResetTitle() {
        hideSaveAndBack();
        this.title_add_text.setVisibility(View.VISIBLE);
        this.title_alert_out_time.setVisibility(View.GONE);
    }

    private void showTitleBarIconResetTitle() {
        restoreSaveAndBack();
        this.title_add_text.setVisibility(View.GONE);
        this.title_alert_out_time.setVisibility(View.VISIBLE);
    }

    private void setPlayButtonVisibility(int visibility) {
    }

    private void hideAddTextEditLayout() {
        showTitleBarIconResetTitle();
        if (this.draw_text_view != null && this.draw_text_view.hasText()) {
            this.draw_text_view.setEdit(false);
        }
        this.isEditAddText = false;
        setPlayButtonVisibility(0);
        showVideoEditView();
        hideAddTextControlLayout();
        this.filter_add_text_parent.setVisibility(View.GONE);
    }

    private void saveTextTempInfo() {
        this.tempLetterSpacingType = this.draw_text_view.getLetterSpacingType();
        this.tempLineSpacingType = this.draw_text_view.getLineSpacingType();
        this.tempFontSizeType = this.draw_text_view.getFontSizeType();
    }

    private /* synthetic */ void lambda$new$7(android.view.View v) {
        switch (v.getId()) {
            case R.id.add_text_controller_title:
                this.draw_text_view.setAlignType(VTContainerView.AlignType.M);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.XL4);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                this.draw_text_view.setVerticalPos(0);
                this.filter_addition_word_space_ring.setNowValue("XS");
                this.filter_addition_size_ring.setNowValue("XL4");
                saveTextTempInfo();
                setControlViewSelect(0);
                return;
            case R.id.add_text_controller_chapter:
                this.draw_text_view.setAlignType(VTContainerView.AlignType.L);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.XXL);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                this.draw_text_view.setVerticalPos(0);
                this.filter_addition_word_space_ring.setNowValue("XS");
                this.filter_addition_size_ring.setNowValue("XXL");
                saveTextTempInfo();
                setControlViewSelect(1);
                return;
            case R.id.add_text_controller_subtitle:
                this.draw_text_view.setAlignType(VTContainerView.AlignType.M);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.S);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                this.draw_text_view.setVerticalPos(10000);
                this.filter_addition_word_space_ring.setNowValue("XS");
                this.filter_addition_size_ring.setNowValue("S");
                saveTextTempInfo();
                setControlViewSelect(3);
                return;
            case R.id.add_text_controller_info:
                this.draw_text_view.setAlignType(VTContainerView.AlignType.L);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.M);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                this.draw_text_view.setVerticalPos(10000);
                this.filter_addition_word_space_ring.setNowValue("XS");
                this.filter_addition_size_ring.setNowValue("M");
                saveTextTempInfo();
                setControlViewSelect(2);
                return;
            case R.id.add_text_controller_reset:
                this.draw_text_view.setAlignType(VTContainerView.AlignType.M);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.M);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                this.draw_text_view.setVerticalPos(0);
                this.filter_addition_word_space_ring.setNowValue("XS");
                this.filter_addition_size_ring.setNowValue("M");
                saveTextTempInfo();
                resetControlViews();
                return;
            default:
                return;
        }
    }

    private void setControlViewSelect(int i) {
        switch (i) {
            case 0:
                this.add_text_controller_title.setAlpha(1.0f);
                this.add_text_controller_chapter.setAlpha(0.3f);
                this.add_text_controller_info.setAlpha(0.3f);
                this.add_text_controller_subtitle.setAlpha(0.3f);
                return;
            case 1:
                this.add_text_controller_title.setAlpha(0.3f);
                this.add_text_controller_chapter.setAlpha(1.0f);
                this.add_text_controller_info.setAlpha(0.3f);
                this.add_text_controller_subtitle.setAlpha(0.3f);
                return;
            case 2:
                this.add_text_controller_title.setAlpha(0.3f);
                this.add_text_controller_chapter.setAlpha(0.3f);
                this.add_text_controller_info.setAlpha(1.0f);
                this.add_text_controller_subtitle.setAlpha(0.3f);
                return;
            case 3:
                this.add_text_controller_title.setAlpha(0.3f);
                this.add_text_controller_chapter.setAlpha(0.3f);
                this.add_text_controller_info.setAlpha(0.3f);
                this.add_text_controller_subtitle.setAlpha(1.0f);
                return;
            default:
                return;
        }
    }

    private void resetControlViews() {
        this.add_text_controller_title.setAlpha(0.3f);
        this.add_text_controller_chapter.setAlpha(0.3f);
        this.add_text_controller_subtitle.setAlpha(0.3f);
        this.add_text_controller_info.setAlpha(0.3f);
    }

    private void initAddTextControlLayout() {
        if (this.video_add_text_control_parent == null && this.video_add_text_control_vs != null) {
            this.video_add_text_control_parent = (RelativeLayout) this.video_add_text_control_vs.inflate();
            this.add_text_controller_chapter = this.video_add_text_control_parent.findViewById(R.id.add_text_controller_chapter);
            this.add_text_controller_title = this.video_add_text_control_parent.findViewById(R.id.add_text_controller_title);
            this.add_text_controller_subtitle = this.video_add_text_control_parent.findViewById(R.id.add_text_controller_subtitle);
            this.add_text_controller_reset = this.video_add_text_control_parent.findViewById(R.id.add_text_controller_reset);
            this.add_text_controller_info = this.video_add_text_control_parent.findViewById(R.id.add_text_controller_info);
            this.add_text_controller_title.setAlpha(0.3f);
            this.add_text_controller_info.setAlpha(0.3f);
            this.add_text_controller_chapter.setAlpha(0.3f);
            this.add_text_controller_subtitle.setAlpha(0.3f);
            this.add_text_controller_reset.setAlpha(0.3f);
            this.add_text_controller_title.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false, 0.3f, 1.0f));
            this.add_text_controller_info.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false, 0.3f, 1.0f));
            this.add_text_controller_chapter.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false, 0.3f, 1.0f));
            this.add_text_controller_subtitle.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false, 0.3f, 1.0f));
            this.add_text_controller_reset.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false, 0.3f, 1.0f));
            this.add_text_controller_title.setOnClickListener(this.addTextControllerListener);
            this.add_text_controller_info.setOnClickListener(this.addTextControllerListener);
            this.add_text_controller_chapter.setOnClickListener(this.addTextControllerListener);
            this.add_text_controller_subtitle.setOnClickListener(this.addTextControllerListener);
            this.add_text_controller_reset.setOnClickListener(this.addTextControllerListener);
            this.video_add_text_control_vs = null;
        }
    }

    private void showAddTextControlLayout() {
        initAddTextControlLayout();
        if (this.video_add_text_control_parent.getVisibility() != View.VISIBLE) {
            this.video_add_text_control_parent.setVisibility(View.VISIBLE);
        }
    }

    private void hideAddTextControlLayout() {
        if (this.video_add_text_control_parent.getVisibility() != View.GONE) {
            this.video_add_text_control_parent.setVisibility(View.GONE);
        }
    }

    private void cancelEditAddTextOpt() {
        if (this.mCurrentTextLongVideoModel != null) {
            restoreDrawTextView();
            return;
        }
        this.mTextContent = "";
        this.draw_text_view.setText("");
        this.text_draw_rl.setVisibility(View.GONE);
    }

    private void refreshEditAddTextOpt() {
        if (this.mCurrentTextLongVideoModel != null) {
            restoreDrawTextView();
            return;
        }
        this.mTextContent = "";
        this.draw_text_view.setText("");
        this.text_draw_rl.setVisibility(View.GONE);
    }

    private void restoreDrawTextView() {
        this.mTextContent = this.mCurrentTextLongVideoModel.getTextContent();
        this.draw_text_view.setText(this.mCurrentTextLongVideoModel.getTextContent());
        this.draw_text_view.setFontSizeType(this.mCurrentTextLongVideoModel.getTextFontSizeType());
        this.draw_text_view.setLineSpacingType(this.mCurrentTextLongVideoModel.getTextLineSpacingType());
        this.draw_text_view.setTypeface(this.mCurrentTextLongVideoModel.getTextTypeface());
        this.draw_text_view.setVerticalPos(this.mCurrentTextLongVideoModel.getTextVerticalPos());
        this.draw_text_view.setAlignType(this.mCurrentTextLongVideoModel.getTextAlignType());
        this.draw_text_view.setShadowType(this.mCurrentTextLongVideoModel.getTextShadowType());
        this.draw_text_view.setLetterSpacingType(this.mCurrentTextLongVideoModel.getTextLetterSpacingType());
        saveTextTempInfo();
        if (TextUtil.isValidate(this.mCurrentTextLongVideoModel.getTextContent())) {
            this.text_draw_rl.setVisibility(View.VISIBLE);
        } else {
            this.text_draw_rl.setVisibility(View.GONE);
        }
    }

    private void clickTextEdit(android.view.View v) {
        switch (v.getId()) {
            case R.id.filter_addition_modify:
                toTextInputActivity(this.mTextContent);
                return;
            case R.id.filter_addition_font:
                if (this.mVTFontDialog == null) {
                    if (this.mVTFontDesBeanList == null) {
                        this.mVTFontDesBeanList = new ArrayList();
                    }
                    if (this.filter_title_bar != null) {
                        int[] locations = new int[2];
                        this.filter_title_bar.getLocationOnScreen(locations);
                        int curHeight = locations[1] + ((int) getResources().getDimension(R.dimen._63dp));
                    }
                    this.mVTFontDialog = new VTFontDialog(getActivity(), this.mVTFontDesBeanList).builder(DensityUtil.getMetricsHeight(getApplicationContext()) - DensityUtil.dip2px(40.0f)).setCancelable(true).setOnFontClickListener(this.mOnFontClickListener);
                }
                if (this.mVTFontDialog != null) {
                    this.mVTFontDialog.show();
                    return;
                }
                return;
            case R.id.filter_addition_size:
                VTContainerView.FontSizeType curFontSizeType;
                this.filter_addition_root_hsv.setVisibility(View.GONE);
                this.filter_addition_size_ll.setVisibility(View.VISIBLE);
                this.addTextClickId = R.id.filter_addition_size;
                alphaEnterAndExit(this.filter_addition_size_ll, this.filter_addition_root_hsv);
                if (this.mCurrentTextLongVideoModel == null) {
                    curFontSizeType = this.draw_text_view.getFontSizeType();
                } else {
                    curFontSizeType = this.mCurrentTextLongVideoModel.getTextFontSizeType();
                }
                final VTContainerView.FontSizeType[] fontTypes = VTContainerView.FontSizeType.values();
                final List<String> fontList = new ArrayList();
                for (VTContainerView.FontSizeType type : fontTypes) {
                    fontList.add(type.name());
                }
                this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_FONTSIZE), getTextName(curFontSizeType.ordinal())));
                this.vt_textsize.setHintText(fontList.size(), fontList.indexOf(curFontSizeType.name()));
                this.vt_textsize.setId(R.id.filter_addition_size);
//                this.vt_textsize.setOnChooseChangeListener(10.lambdaFactory$(this, fontTypes, fontList));
                vt_textsize.setOnChooseChangeListener(new VTBaseView.OnChooseChangeListener() {
                    @Override
                    public void change(int i) {
                        lambda$clickTextEdit$9(fontTypes,fontList,i);
                    }
                });
                return;
            case R.id.filter_addition_color:
                this.addTextClickId = R.id.filter_addition_color;
                this.filter_addition_root_hsv.setVisibility(View.GONE);
                this.filter_addition_color_ll.setVisibility(View.VISIBLE);
                alphaEnterAndExit(this.filter_addition_color_ll, this.filter_addition_root_hsv);
                VTContainerView.TextColorType curColorType = this.draw_text_view.getTextColorType();
                final VTContainerView.TextColorType[] colorTypes = VTContainerView.TextColorType.values();
                List<Integer> colorList = new ArrayList();
                colorList.add(Color.parseColor("#EEEEEE"));
                colorList.add(Color.parseColor("#131211"));
                this.vt_color_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_COLOR), getColorName(curColorType.ordinal())));
                this.vt_colorchoose.setColorOfCircle(colorList, curColorType.ordinal());
//                this.vt_colorchoose.setOnChooseChangeListener(9.lambdaFactory$(this, colorTypes));
                vt_colorchoose.setOnChooseChangeListener(new VTBaseView.OnChooseChangeListener() {
                    @Override
                    public void change(int i) {
                        lambda$clickTextEdit$8(colorTypes,i);
                    }
                });
                return;
            case R.id.filter_addition_shader:
                this.filter_addition_root_hsv.setVisibility(View.GONE);
                this.filter_addition_shader_ll.setVisibility(View.VISIBLE);
                this.addTextClickId = R.id.filter_addition_shader;
                alphaEnterAndExit(this.filter_addition_shader_ll, this.filter_addition_root_hsv);
//                NONE,
//                        AROUND,
//                        SMOOTH,
//                        UPADDDOWN,
//                        AROUNDBORDER,
//                        INLINE,
//                        SOLID,
//                        BOX
                switch (this.draw_text_view.getShadowType()) {
                    case NONE:
                        clickShadder(this.vt_shader_1);
                        return;
                    case AROUND:
                        clickShadder(this.vt_shader_2);
                        return;
                    case SMOOTH:
                        clickShadder(this.vt_shader_3);
                        return;
                    case UPADDDOWN:
                        clickShadder(this.vt_shader_4);
                        return;
                    case AROUNDBORDER:
                        clickShadder(this.vt_shader_5);
                        return;
                    case INLINE:
                        clickShadder(this.vt_shader_6);
                        return;
                    default:
                        return;
                }
            case R.id.filter_addition_word_space:
                this.filter_addition_root_hsv.setVisibility(View.GONE);
                this.filter_addition_size_ll.setVisibility(View.VISIBLE);
                this.addTextClickId = R.id.filter_addition_word_space;
                alphaEnterAndExit(this.filter_addition_size_ll, this.filter_addition_root_hsv);
                VTContainerView.LetterSpacingType curletterSizeType = this.draw_text_view.getLetterSpacingType();
                final VTContainerView.LetterSpacingType[] letterTypes = VTContainerView.LetterSpacingType.values();
                final List<String> wordList = new ArrayList();
                for (VTContainerView.LetterSpacingType type2 : letterTypes) {
                    wordList.add(type2.name());
                }
                this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_TRACKING), getTextName(curletterSizeType.ordinal())));
                this.vt_textsize.setHintText(wordList.size(), wordList.indexOf(curletterSizeType.name()));
                this.vt_textsize.setId(R.id.filter_addition_word_space);
                //this.vt_textsize.setOnChooseChangeListener(12.lambdaFactory$(this, letterTypes, wordList));
                vt_textsize.setOnChooseChangeListener(new VTBaseView.OnChooseChangeListener() {
                    @Override
                    public void change(int i) {
                        lambda$clickTextEdit$11(letterTypes,wordList,i);
                    }
                });
                return;
            case R.id.filter_addition_line_space:
                VTContainerView.LineSpacingType curLineSizeType;
                this.filter_addition_root_hsv.setVisibility(View.GONE);
                this.filter_addition_size_ll.setVisibility(View.VISIBLE);
                this.addTextClickId = R.id.filter_addition_line_space;
                alphaEnterAndExit(this.filter_addition_size_ll, this.filter_addition_root_hsv);
                if (this.mCurrentTextLongVideoModel != null) {
                    curLineSizeType = this.mCurrentTextLongVideoModel.getTextLineSpacingType();
                } else {
                    curLineSizeType = this.draw_text_view.getLineSpacingType();
                }
                final VTContainerView.LineSpacingType[] lineTypes = VTContainerView.LineSpacingType.values();
                List<String> lineList = new ArrayList();
                for (VTContainerView.LineSpacingType type3 : lineTypes) {
                    lineList.add(type3.name());
                }
                this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_LINESPACING), getTextName(curLineSizeType.ordinal())));
                this.vt_textsize.setHintText(lineList.size(), lineList.indexOf(curLineSizeType.name()));
                this.vt_textsize.setId(R.id.filter_addition_line_space);
//                this.vt_textsize.setOnChooseChangeListener(11.lambdaFactory$(this, lineTypes));
                vt_textsize.setOnChooseChangeListener(new VTBaseView.OnChooseChangeListener() {
                    @Override
                    public void change(int i) {
                        lambda$clickTextEdit$10(lineTypes,i);
                    }
                });
                return;
            case R.id.filter_addition_delete:
                this.mTextContent = "";
                this.draw_text_view.setText("");
                this.text_draw_rl.setVisibility(View.GONE);
                this.draw_text_view.setFontSizeType(VTContainerView.FontSizeType.M);
                this.draw_text_view.setAlignType(VTContainerView.AlignType.M);
                this.draw_text_view.setVerticalPos(0);
                this.draw_text_view.setTextColorType(VTContainerView.TextColorType.White);
                this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
                this.draw_text_view.setLineSpacingType(VTContainerView.LineSpacingType.S);
                this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
                if (this.mCurrentTextLongVideoModel != null) {
                    List<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
                    if (textModelList != null && textModelList.size() > 0) {
                        int index = textModelList.indexOf(this.mCurrentTextLongVideoModel);
                        if (index >= 0) {
                            this.mVideoAudioManager.removeTextModel(index);
                        }
                    }
                }
                saveTextTempInfo();
                animHideAddTag();
                hideAddTextEditLayout();
                return;
            default:
                return;
        }
    }

    private /* synthetic */ void lambda$clickTextEdit$8(VTContainerView.TextColorType[] colorTypes, int position) {
        this.vt_color_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_COLOR), getColorName(position)));
        this.draw_text_view.setTextColorType(colorTypes[position]);
    }

    private /* synthetic */ void lambda$clickTextEdit$9(VTContainerView.FontSizeType[] fontTypes, List fontList, int position) {
        this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_FONTSIZE), getTextName(position)));
        this.draw_text_view.setFontSizeType(fontTypes[position]);
        this.lastTextChangeString = (String) fontList.get(position);
        resetControlViews();
    }

    private /* synthetic */ void lambda$clickTextEdit$10(VTContainerView.LineSpacingType[] lineTypes, int position) {
        this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_LINESPACING), getTextName(position)));
        this.draw_text_view.setLineSpacingType(lineTypes[position]);
        resetControlViews();
    }

    private /* synthetic */ void lambda$clickTextEdit$11(VTContainerView.LetterSpacingType[] letterTypes, List wordList, int position) {
        this.vt_textsize_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_TRACKING), getTextName(position)));
        this.draw_text_view.setLetterSpacingType(letterTypes[position]);
        this.lastTextChangeString = (String) wordList.get(position);
        resetControlViews();
    }

    private String getTextName(int type) {
        switch (type) {
            case 0:
                return getString(R.string.TEXT_SIZE_XS);
            case 1:
                return getString(R.string.TEXT_SIZE_S);
            case 2:
                return getString(R.string.TEXT_SIZE_M);
            case 3:
                return getString(R.string.TEXT_SIZE_L);
            case 4:
                return getString(R.string.TEXT_SIZE_XL);
            case 5:
                return getString(R.string.TEXT_SIZE_XXL);
            case 6:
                return getString(R.string.TEXT_SIZE_3XS);
            default:
                return getString(R.string.TEXT_SIZE_4XL);
        }
    }

    private void clickShadder(android.view.View v) {
        this.vt_shader_1.setImageResource(R.drawable.icon_30_text_shadow_no);
        this.vt_shader_2.setImageResource(R.drawable.icon_30_text_shadow_around);
        this.vt_shader_3.setImageResource(R.drawable.icon_30_text_shadow_smooth);
        this.vt_shader_4.setImageResource(R.drawable.icon_30_text_shadow_upanddown);
        this.vt_shader_5.setImageResource(R.drawable.icon_30_text_shadow_4borders);
        this.vt_shader_6.setImageResource(R.drawable.icon_30_text_shadow_lineinline);
        VTContainerView.ShadowType[] shadowTypes = VTContainerView.ShadowType.values();
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_1, R.color.colorDate);
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_2, R.color.colorDate);
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_3,  R.color.colorDate);
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_4,  R.color.colorDate);
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_5,  R.color.colorDate);
        TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_6,  R.color.colorDate);
        switch (v.getId()) {
            case R.id.vt_shader_1:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_1, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_OFF)));
                this.draw_text_view.setShadowType(shadowTypes[0]);
                return;
            case R.id.vt_shader_2:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_2, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_AROUND)));
                this.draw_text_view.setShadowType(shadowTypes[1]);
                return;
            case R.id.vt_shader_3:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_3, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_SMOOTH)));
                this.draw_text_view.setShadowType(shadowTypes[2]);
                return;
            case R.id.vt_shader_4:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_4, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_UPANDDOWN)));
                this.draw_text_view.setShadowType(shadowTypes[3]);
                return;
            case R.id.vt_shader_5:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_5, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_4BORDERS)));
                this.draw_text_view.setShadowType(shadowTypes[4]);
                return;
            case R.id.vt_shader_6:
                TintColorUtil.tintDrawable(getApplicationContext(), this.vt_shader_6, R.color.colorWhite);
                this.vt_shader_hint.setText(String.format("%1$s: %2$s", getString(R.string.BUTTON_TEXT_SHADOW), getString(R.string.TEXT_SHADOW_INLINE)));
                this.draw_text_view.setShadowType(shadowTypes[5]);
                return;
            default:
                return;
        }
    }

    private String getColorName(int type) {
        if (type == 0) {
            return getString(R.string.TEXT_COLOR_WHITE);
        }
        return getString(R.string.TEXT_COLOR_BLACK);
    }

    private void hideSaveAndBack() {
        this.mTvSave.setVisibility(View.GONE);
        this.mIvBack.setVisibility(View.GONE);
    }

    private void restoreSaveAndBack() {
        this.mTvSave.setVisibility(View.VISIBLE);
        this.mIvBack.setVisibility(View.VISIBLE);
    }

    private void hidePlayButton() {
        this.player_rl.setVisibility(View.GONE);
    }

    private void showPlayButton() {
        this.player_rl.setVisibility(View.VISIBLE);
    }

    private Bitmap getDateBitmap() {
        if (this.mDateFontBitmap == null || this.mDateFontBitmap.isRecycled()) {
            getDateFontBitmap();
        }
        return this.mDateFontBitmap;
    }

    private void getDateFontBitmap() {
        long dateTime = 0;
        if (FilterViewUtils.isPictureModel(this.mDataType)) {
            dateTime = ImagePropertyBean.getInstance().getDateTime();
        } else if (FilterViewUtils.isGifModel(this.mDataType)) {
            if (TextUtil.isValidate(this.mGifPath)) {
                File file = new File(this.mGifPath);
                if (file.exists()) {
                    dateTime = file.lastModified();
                } else {
                    dateTime = System.currentTimeMillis();
                }
            } else {
                dateTime = System.currentTimeMillis();
            }
        }
        if (dateTime >= 0) {
            char[] chars = getPicDateStr(dateTime).toCharArray();
            Paint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(-36352);
            textPaint.setTypeface(FontsUtil.setALCDDotTRTypeFace(getActivity()));
            textPaint.setShadowLayer(10.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, -2130742784);
            int tSize = DensityUtil.dip2px(40.0f);
            textPaint.setTextSize((float) tSize);
            int tSpacing = (int) (((float) tSize) * 0.1f);
            int allLineHeight = ((int) (Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent()))) + 30;
            int lineWidth = 0;
            for (int i = 0; i < chars.length; i++) {
                float f;
                float measureText;
                if (i == chars.length - 1) {
                    f = (float) lineWidth;
                    measureText = textPaint.measureText(String.valueOf(chars[i]));
                } else {
                    f = (float) lineWidth;
                    measureText = ((float) tSpacing) + textPaint.measureText(String.valueOf(chars[i]));
                }
                lineWidth = (int) (f + measureText);
            }
            this.mDateFontBitmap = Bitmap.createBitmap(lineWidth + 30, allLineHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.mDateFontBitmap);
            canvas.drawColor(65280);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            int drawTextY = ((int) (Math.abs(textPaint.ascent()) + 15.0f)) + (tSpacing / 2);
            int drawTextX = 15;
            for (char c : chars) {
                canvas.drawText(String.valueOf(c), (float) drawTextX, (float) drawTextY, textPaint);
                drawTextX = (int) (((float) drawTextX) + (((float) tSpacing) + textPaint.measureText(String.valueOf(c))));
            }
        }
    }

    private String getPicDateStr(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy MM dd", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(date));
    }

    private void releaseViewHolder() {
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.release();
            this.mVideoEditSwitchTabsHolder = null;
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initVideoEditAddLayout() {
        if (this.video_edit_add_vs != null) {
            int finalWidth;
            this.video_edit_add_parent = (FrameLayout) this.video_edit_add_vs.inflate();
            this.video_edit_add_vs = null;
            this.video_edit_add_parent.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    Toast.makeText(getActivity(),"add video",Toast.LENGTH_LONG).show();
                }
            });
            this.video_edit_add_album_tab = this.video_edit_add_parent.findViewById(R.id.video_edit_add_album_tab);
            this.video_edit_add_album_iv = this.video_edit_add_album_tab.findViewById(R.id.video_edit_add_album_iv);
            this.video_edit_add_album_tv = this.video_edit_add_album_tab.findViewById(R.id.video_edit_add_album_tv);
            int addAlbumWidth = (int) ((((float) DensityUtil.dip2px(55.0f)) + this.video_edit_add_album_tv.getPaint().measureText(this.video_edit_add_album_tv.getText().toString())) + 1.0f);
            this.video_edit_add_textgap_tab = this.video_edit_add_parent.findViewById(R.id.video_edit_add_textgap_tab);
            this.video_edit_add_textgap_iv = this.video_edit_add_textgap_tab.findViewById(R.id.video_edit_add_textgap_iv);
            this.video_edit_add_textgap_tv = this.video_edit_add_textgap_tab.findViewById(R.id.video_edit_add_textgap_tv);
            this.video_edit_add_onemin_tab = this.video_edit_add_parent.findViewById(R.id.video_edit_add_onemin_tab);
            this.video_edit_add_onemin_iv = this.video_edit_add_parent.findViewById(R.id.video_edit_add_onemin_iv);
            this.video_edit_add_onemin_tv = this.video_edit_add_parent.findViewById(R.id.video_edit_add_onemin_tv);
            this.video_edit_add_onemin_tv.setText(ActiveListManager.OneMinuteActive);
            int addTextGapWidth = (int) ((((float) DensityUtil.dip2px(55.0f)) + this.video_edit_add_textgap_tv.getPaint().measureText(this.video_edit_add_textgap_tv.getText().toString())) + 1.0f);
            int addOneMineWidth = (int) ((((float) DensityUtil.dip2px(55.0f)) + this.video_edit_add_onemin_tv.getPaint().measureText(this.video_edit_add_onemin_tv.getText().toString())) + 1.0f);
            if (Constants.OPEN_ACTIVE_ONE_MINUTE) {
                finalWidth = Math.max(Math.max(addAlbumWidth, addTextGapWidth), addOneMineWidth);
                this.video_edit_add_onemin_tab.setVisibility(View.VISIBLE);
            } else {
                finalWidth = Math.max(addAlbumWidth, addTextGapWidth);
                this.video_edit_add_onemin_tab.setVisibility(View.GONE);
            }
            LogUtil.d(TAG, String.format("initVideoEditAddLayout addAlbumWidth : %s , addTextGapWidth : %s , finalWidth : %s ", addAlbumWidth, addTextGapWidth, finalWidth));
            ViewUtil.setViewWidth(this.video_edit_add_album_tab, finalWidth);
            ViewUtil.setViewWidth(this.video_edit_add_textgap_tab, finalWidth);
            ViewUtil.setViewWidth(this.video_edit_add_onemin_tab, finalWidth);
            this.video_edit_add_cancel_iv = this.video_edit_add_parent.findViewById(R.id.video_edit_add_cancel_iv);
            //this.video_edit_add_parent.setOnClickListener(13.lambdaFactory$(this));
            video_edit_add_parent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoEditAddLayout$13(v);
                }
            });
            this.video_edit_add_album_tab.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(android.view.View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            video_edit_add_album_iv.setAlpha(0.3f);
                            video_edit_add_album_tv.setAlpha(0.3f);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    video_edit_add_album_iv.setAlpha(1.0f);
                                    video_edit_add_album_tv.setAlpha(1.0f);
                                }
                            }, 100);
                            break;
                    }
                    return false;
                }
            });
//            this.video_edit_add_album_tab.setOnClickListener(14.lambdaFactory$(this));
            video_edit_add_album_tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoEditAddLayout$14(v);
                }
            });
            this.video_edit_add_textgap_tab.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(android.view.View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            video_edit_add_textgap_iv.setAlpha(0.3f);
                            video_edit_add_textgap_tv.setAlpha(0.3f);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    video_edit_add_textgap_iv.setAlpha(1.0f);
                                    video_edit_add_textgap_tv.setAlpha(1.0f);
                                }
                            }, 100);
                            break;
                    }
                    return false;
                }
            });
//            this.video_edit_add_textgap_tab.setOnClickListener(15.lambdaFactory$(this));
            video_edit_add_textgap_tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"video_edit_add_textgap_tab onclick",Toast.LENGTH_LONG).show();
                }
            });
            this.video_edit_add_onemin_tab.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(android.view.View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            video_edit_add_onemin_iv.setAlpha(0.3f);
                            video_edit_add_onemin_tv.setAlpha(0.3f);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    video_edit_add_onemin_iv.setAlpha(1.0f);
                                    video_edit_add_onemin_tv.setAlpha(1.0f);
                                }
                            }, 100);
                            break;
                    }
                    return false;
                }
            });
            this.video_edit_add_onemin_tab.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    callVideoPause();
                    insertBlackImageOnCurList(true);
                    hideVideoEditAddLayout();
                }
            });
            this.video_edit_add_cancel_iv.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
//            this.video_edit_add_cancel_iv.setOnClickListener(16.lambdaFactory$(this));
            video_edit_add_cancel_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"video_edit_add_cancel_iv onclick",Toast.LENGTH_LONG).show();
                    //onAddVideosClick(0);
                    hideSaveAndBack();
                }
            });
        }
    }

    private /* synthetic */ void lambda$initVideoEditAddLayout$13(android.view.View v) {
        //BuglyLogUtil.writeBuglyLog("video jump to add local");
        callVideoPause();
        ArrayList<String> pathList = new ArrayList();
        Iterator it = this.mVideoAudioManager.getVideosModelList().iterator();
        while (it.hasNext()) {
            pathList.add(((LongVideosModel) it.next()).getOriginalMediaPath());
        }
        //IntentUtil.toVideo2FromFilter(getActivity(), VideoActivity2.FromFilter, "", "", 0, "", 0, this.mFilterName, pathList);
        Toast.makeText(this,"go to VideoActivity2",Toast.LENGTH_LONG).show();
        hideVideoEditAddLayout();
    }

    private /* synthetic */ void lambda$initVideoEditAddLayout$14(android.view.View v) {
        callVideoPause();
        insertBlackImageOnCurList();
        hideVideoEditAddLayout();
    }

    private void insertBlackImageOnCurList(boolean isActive) {
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.changeAudioDurationAfterInsert(Constants.VIDEO_BLACK_DURATION);
        }
        LongVideosModel longVideosModel = new LongVideosModel(isActive ? 4 : 2, 720, 1280);
        if (isActive) {
            longVideosModel.setVideoPath(ImageCacheUtils.ONEMIN_BITMAP_PATH);
        }
        longVideosModel.setStartTime(1000000);
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.changeTextDurationAfterInsert(this.mCurInsertPosition, longVideosModel.getCurrentDuration(), true);
        }
        if (this.mVideoAudioManager.getVideosModelList().size() > 0) {
            LongVideosModel firstModel = this.mVideoAudioManager.getVideosModelList().get(0);
            VideoInputRatio inputRatio = firstModel.getInputRatioWithRotate();
            longVideosModel.setInputRatio(new VideoInputRatio(inputRatio.ratioHeight, inputRatio.ratioWidth));
            longVideosModel.setVideoFPS(firstModel.getVideoFPS());
            LogUtil.d(TAG, String.format("insertBlackImageOnCurList h : %s , w : %s ", inputRatio.ratioHeight, inputRatio.ratioWidth));
        }
        this.mVideoAudioManager.getVideosModelList().add(this.mCurInsertPosition, longVideosModel);
        setUndoData(1);
        callVideoChange(1000 * getNeedPlayTime(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList()));
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.clearCurSelectVideoModel();
        }
        refreshVideoEditView();
        float insertSeekTo = getInsertSeekTo(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList());
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.setViewScrollTo((double) insertSeekTo);
        }
        if (!isActive) {
            this.isAfterInsertBlackAddText = true;
            this.mCurrentTextLongVideoModel = null;
            toTextInputActivity("");
        }
        refreshTitleAlertOutTime();
    }

    private void insertBlackImageOnCurList() {
        insertBlackImageOnCurList(false);
    }

    private void showVideoEditAddLayout() {
        this.onViewAnimating = true;
        initVideoEditAddLayout();
        AnimationUtil.appearFromBottom(DensityUtil.dip2px(140.0f), this.video_edit_add_parent, new AnimationUtil.AnimationCallback() {
            public void onAnimationEnd() {
                onViewAnimating = false;
            }
        });
        showVideoEditAddTextLayout();
        hideVideoEditView();
    }

    private void showVideoEditAddTextLayout() {
        if (this.video_edit_remove_parent != null) {
            this.video_edit_remove_parent.setVisibility(View.GONE);
        }
        this.video_edit_add_type_tv.setVisibility(View.VISIBLE);
    }

    private void hideVideoEditAddTextLayout() {
        if (this.video_edit_remove_parent != null) {
            this.video_edit_remove_parent.setVisibility(View.VISIBLE);
        }
        this.video_edit_add_type_tv.setVisibility(View.GONE);
    }

    private void hideVideoEditAddLayout() {
        if (this.video_edit_add_parent != null) {
            this.onViewAnimating = true;
            AnimationUtil.disappearFromBottom(DensityUtil.dip2px(140.0f), this.video_edit_add_parent, new AnimationUtil.AnimationCallback() {
                public void onAnimationEnd() {
                    onViewAnimating = false;
                }
            });
            changeVideoEditVisible();
            hideVideoEditAddTextLayout();
            restoreSaveAndBack();
        }
    }

    private void changeVideoEditVisibleForMusicTab() {
        if (this.video_edit_parent_ll != null) {
            this.video_edit_parent_ll.setVisibility(View.VISIBLE);
        }
        if (this.video_edit_remove_parent != null) {
            this.video_edit_remove_parent.setVisibility(View.VISIBLE);
        }
        showVideoTimeSlideBar();
        if (this.video_edit_text_rv != null && this.mVideoEditHelper != null && this.mVideoEditHelper.isCanShowTextRV()) {
            this.video_edit_text_rv.setVisibility(View.VISIBLE);
        }
    }

    private void changeVideoEditVisible() {
        if (this.video_edit_parent_ll != null) {
            this.video_edit_parent_ll.setVisibility(View.VISIBLE);
        }
        showEditCenterLine();
        if (this.video_edit_remove_parent != null) {
            this.video_edit_remove_parent.setVisibility(View.VISIBLE);
        }
        showVideoTimeSlideBar();
        if (this.video_edit_text_rv != null && this.mVideoEditHelper != null && this.mVideoEditHelper.isCanShowTextRV()) {
            this.video_edit_text_rv.setVisibility(View.VISIBLE);
        }
    }

    private void showEditCenterLine() {
        this.filter_tab_center_line_parent.setVisibility(View.VISIBLE);
    }

    private void showVideoTimeSlideBar() {
        this.video_time_slide_bar.setVisibility(View.VISIBLE);
        this.video_time_slide_bar_backgroud_view.setVisibility(View.VISIBLE);
    }

    private void initVideoMusicVolumeLayout() {
    }

    private void changeLongVideosModelsDefaultVolume(float audioVolume, float videoVolume, boolean confirm) {
        Iterator it = this.mVideoAudioManager.getVideosModelList().iterator();
        while (it.hasNext()) {
            LongVideosModel longVideosModel = (LongVideosModel) it.next();
            longVideosModel.setAudioDefaultVolume(audioVolume);
            longVideosModel.setVideoDefaultVolume(videoVolume);
            if (confirm) {
                longVideosModel.setVideoConfirmVolume(videoVolume);
                longVideosModel.setAudioConfirmVolume(audioVolume);
            }
        }
        setUndoData(1);
    }

    private void showVideoMusicVolumeLayout() {
    }

    private void hideVideoMusicVolumeLayout() {
        restoreSaveAndBack();
        if (this.video_music_volume_ll != null) {
            final int height = this.video_music_volume_ll.getHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(200);
            //valueAnimator.addUpdateListener(17.lambdaFactory$(this, height));
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lambda$hideVideoMusicVolumeLayout$16(height,animation);
                }
            });
            valueAnimator.addListener(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ViewCompat.setTranslationY(video_music_volume_ll, (float) height);
                    video_music_volume_ll.setVisibility(View.GONE);
                }
            });
            valueAnimator.start();
        }
    }

    private /* synthetic */ void lambda$hideVideoMusicVolumeLayout$16(int height, ValueAnimator animation) {
        ViewCompat.setTranslationY(this.video_music_volume_ll, ((float) height) * (Float) animation.getAnimatedValue());
    }

    private void setVideoStartPause(boolean shouldPause) {
        if (this.playbackPercent == 1.0d) {
            this.player_rl.changePlayState(false);
            if (this.mAudioTrimLayout != null && this.mAudioTrimLayout.getVisibility() == View.VISIBLE) {
                this.mAudioTrimLayout.changePlayState(false);
            }
            if (this.mSurfaceView.getLongVideoPlayState()) {
                this.mSurfaceView.setLongVideoSeekTo(0);
                this.mSurfaceView.setVideoPauseOrResume(false);
                return;
            }
            return;
        }
        this.mSurfaceView.setVideoPauseOrResume(shouldPause);
    }

    private void showVideoMusicSplitLayout() {
        hideSaveAndBack();
        hidePlayButton();
        initVideoMusicSplitLayout();
        int viewHeight = this.video_music_split_parent.getHeight();
        if (viewHeight <= 0) {
            viewHeight = DensityUtil.dip2px(140.0f);
        }
        final int height = viewHeight;
        ViewCompat.setTranslationY(this.video_music_split_parent, (float) height);
        this.video_music_split_parent.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(100);
        //valueAnimator.addUpdateListener(18.lambdaFactory$(this, height));
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lambda$showVideoMusicSplitLayout$17(height,animation);
            }
        });
        valueAnimator.start();
    }

    private /* synthetic */ void lambda$showVideoMusicSplitLayout$17(int height, ValueAnimator animation) {
        ViewCompat.setTranslationY(this.video_music_split_parent, ((float) height) * (1.0f - ((Float) animation.getAnimatedValue()).floatValue()));
    }

    private void initVideoMusicSplitLayout() {
        if (this.video_music_split_vs != null) {
            this.video_music_split_parent = (VideoMusicSplitLayout) this.video_music_split_vs.inflate();
            this.video_music_split_vs = null;
            this.video_music_split_tv_parent = this.video_music_split_parent.findViewById(R.id.video_music_split_tv_parent);
            this.video_music_split_awv = this.video_music_split_parent.findViewById(R.id.video_music_split_awv);
            this.video_music_split_awv.setWaveDrawType(2);
            this.video_music_split_cancel_iv = this.video_music_split_parent.findViewById(R.id.video_music_split_cancel_iv);
            this.video_music_split_confirm_iv = this.video_music_split_parent.findViewById(R.id.video_music_split_confirm_iv);
            //this.video_music_split_cancel_iv.setOnClickListener(19.lambdaFactory$(this));
            video_music_split_cancel_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoMusicSplitLayout$18(v);
                }
            });
            //this.video_music_split_confirm_iv.setOnClickListener(20.lambdaFactory$(this));
            video_music_split_confirm_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoMusicSplitLayout$19(v);
                }
            });
            //this.video_music_split_parent.setPlayBtnClickListener(21.lambdaFactory$(this));
            video_music_split_parent.setPlayBtnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoMusicSplitLayout$20(v);
                }
            });
            //this.video_music_split_parent.setCallback(22.lambdaFactory$(this));
            video_music_split_parent.setCallback(new VideoMusicSplitLayout.VideoPlayControlCallback() {
                @Override
                public void pauseVideo() {
                    //lambda$initVideoOrderLayout$22();
                    Toast.makeText(getActivity(),"video_music_split_parent pauseVideo",Toast.LENGTH_LONG).show();
                    pauseVideoForSplitView();
                }
            });
        }
    }

    private /* synthetic */ void lambda$initVideoMusicSplitLayout$18(android.view.View v) {
        this.video_music_split_parent.restoreTrueVerse();
        hideVideoMusicSplitLayout();
    }

    private /* synthetic */ void lambda$initVideoMusicSplitLayout$19(android.view.View v) {
        this.video_music_split_parent.saveCurVerse();
        hideVideoMusicSplitLayout();
        changeVideoAudio(this.currentPlayTimeUs);
        if (this.mVideoEditHelper != null) {
        }
        setUndoData(1);
    }

    private /* synthetic */ void lambda$initVideoMusicSplitLayout$20(android.view.View v) {
        if (this.video_music_split_parent.isPlaying()) {
            this.video_music_split_parent.setPlaying(false);
            pauseVideoForSplitView();
            return;
        }
        this.video_music_split_parent.setPlaying(true);
        playVideoForSplitView();
    }

    private void pauseVideoForSplitView() {
        callVideoPause();
        this.mSurfaceView.setLongVideoSeekTo(0);
    }

    private void playVideoForSplitView() {
        changeVideoAudio(this.currentPlayTimeUs);
        this.mSurfaceView.setLongVideoSeekTo(0);
        callVideoPlay();
    }

    private void changeVideoAudio(long needPlayTimeUs) {
        this.mSurfaceView.changePlaylist(this.mVideoAudioManager.getVideosModelList(), getBgmList(), needPlayTimeUs, this.mVideoAudioManager.getMediaMute());
    }

    private void hideVideoMusicSplitLayout() {
        restoreSaveAndBack();
        showPlayButton();
        if (this.video_music_split_parent != null) {
            this.video_music_split_parent.setVisibility(View.GONE);
        }
    }

    private void hideMusicVolumeAndRemove() {
        this.filter_music_volume.setVisibility(View.GONE);
        this.filter_music_delete.setVisibility(View.GONE);
        this.filter_music_add.setFilterEffectText(getString(R.string.BUTTON_AUDIO_EDIT_ADD));
    }

    private void showMusicVolumeAndRemove() {
        this.filter_music_volume.setVisibility(View.VISIBLE);
        this.filter_music_delete.setVisibility(View.VISIBLE);
        this.filter_music_add.setFilterEffectText(getString(R.string.BUTTON_AUDIO_EDIT_REPLACE));
    }

    private void showVideoMusicDialog() {
        initVideoMusicDialog();
        //this.mVideoMusicDialog.show();
        Toast.makeText(getActivity(),"showVideoMusicDialog",Toast.LENGTH_LONG).show();
    }

    private void initVideoMusicDialog() {
//        if (this.mVideoMusicDialog == null) {
//            this.mVideoMusicDialog = new VideoMusicDialog(getActivity(), this.needScreenWidth, this.needScreenHeight).build();
//        }
        Toast.makeText(getActivity(),"initVideoMusicDialog",Toast.LENGTH_LONG).show();
    }

    private void showVideoMusicLayout() {
        Toast.makeText(getActivity(),"showVideoMusicLayout",Toast.LENGTH_LONG).show();
    }

    private void hideVideoMusicLayout() {
        if (this.filter_music_ll != null) {
            this.filter_music_ll.setVisibility(View.GONE);
            this.filter_music_icon_bottom.setVisibility(4);
        }
        hideVideoMusicNameLayout();
    }

    public boolean checkIsAddedThisAudio(int id) {
        if (this.mVideoAudioManager != null) {
            ArrayList<LongVideosModel> musicModelList = this.mVideoAudioManager.getMusicModelList();
            if (musicModelList != null) {
                Iterator it = musicModelList.iterator();
                while (it.hasNext()) {
                    AudioTrackBean audioTrackBean = ((LongVideosModel) it.next()).getAudioTrackBean();
                    if (audioTrackBean != null && id == audioTrackBean.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoAudioUseEvent event) {
        dealVideoAudioUseEvent(event);
    }

    private void addToUsedBgm(AudioTrackBean bean, LongVideosModel model, int position, boolean isAudioReplace) {
        if (bean != null) {
            LogUtil.d(TAG, String.format("addToUsedBgm position : %s ", position));
            getBgmList().add(position, model);
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.refreshAudioViewAfterInsertOrReplace(position, isAudioReplace);
            }
        }
    }

    public void clearAudioReplaceFlag() {
        this.isAudioReplace = false;
    }

    private void dealVideoAudioUseEvent(VideoAudioUseEvent event) {
        AudioTrackBean audioTrackBean = event.getAudioTrackBean();
        boolean isFirst = true;
        if (getBgmList().size() > 0) {
            isFirst = false;
        }
        if (audioTrackBean != null) {
            float curStartTime;
            String audioFileName = AudioStoreController.getAudioFileName(audioTrackBean);
            File file;
            if (audioTrackBean.isLocal()) {
                file = new File(audioFileName);
            } else {
                file = new File(Config.getAudiosDownloadPath(), audioFileName);
            }
            String filePath = file.getAbsolutePath();
            long duration = (long) (Float.valueOf(audioTrackBean.getDuration()).floatValue() * 1000.0f);
            float verse = StaticLayoutUtil.DefaultSpacingadd;
            try {
                curStartTime = Float.valueOf(audioTrackBean.getDefault_slice_point()).floatValue();
            } catch (Exception e) {
                e.printStackTrace();
                curStartTime = StaticLayoutUtil.DefaultSpacingadd;
            }
            String poi = audioTrackBean.getPoi();
            if (TextUtil.isValidate(poi)) {
                String[] split = poi.split(",");
                if (split.length > 1) {
                    try {
                        verse = Float.valueOf(split[split.length - 1]).floatValue();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        verse = StaticLayoutUtil.DefaultSpacingadd;
                    }
                }
            }
            LogUtil.d("dealVideoAudioUseEvent", String.format("filePath : %s , duration : %s , verse : %s ", new Object[]{filePath, Long.valueOf(duration), Float.valueOf(verse * 1000.0f)}));
            LongVideosModel audioVideosModel = new LongVideosModel(1, filePath, duration, (long) verse);
            ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
            if (this.isAudioReplace) {
                if (this.mVideoEditHelper != null) {
                    this.mVideoEditHelper.cancelGetWaveRunnableByModelPos(this.addMusicPosition);
                }
                LongVideosModel old = getBgmList().remove(this.addMusicPosition);
                audioVideosModel.setAudioVolumes(old.getAudioVolumes());
                audioVideosModel.setAudioStartTime(old.getAudioStartTime());
                audioVideosModel.setAudioDuration(old.getAudioDuration());
                audioVideosModel.setAudioVolumes(old.getAudioVolumes());
            } else {
                long sumTime = VideoModelHelper.getVideosSumTime(videosModelList);
                if (this.addMusicPosition == -1) {
                    audioVideosModel.setAudioStartTime(0);
                    audioVideosModel.setAudioDuration(sumTime);
                    this.addMusicPosition = 0;
                } else {
                    LongVideosModel model;
                    long audioStartTime;
                    if (this.addMusicPosition == getBgmList().size()) {
                        model = getBgmList().get(this.addMusicPosition - 1);
                        audioVideosModel.setAudioStartTime(model.getAudioStartTime() + model.getAudioDuration());
                        audioStartTime = audioVideosModel.getAudioStartTime();
                        if (audioStartTime >= sumTime) {
                            audioVideosModel.setAudioDuration((long) (1000.0f * VideoEditHelper.div((((float) DensityUtil.mScreenWidth) * 1.0f) / 2.0f, (float) VideoEditHelper.IMAGE_UNIT_WIDTH)));
                            audioVideosModel.setJustSeeForAudio(true);
                        } else {
                            audioVideosModel.setAudioDuration(sumTime - audioStartTime);
                        }
                    } else {
                        model = getBgmList().get(this.addMusicPosition);
                        if (this.addMusicPosition > 0) {
                            LongVideosModel prev = getBgmList().get(this.addMusicPosition - 1);
                            audioStartTime = prev.getAudioStartTime() + prev.getAudioDuration();
                            audioVideosModel.setAudioStartTime(audioStartTime);
                            audioVideosModel.setAudioDuration(model.getAudioStartTime() - audioStartTime);
                        } else {
                            audioVideosModel.setAudioStartTime(0);
                            audioVideosModel.setAudioDuration(model.getAudioStartTime());
                        }
                    }
                }
                audioVideosModel.setTrueStartTime(curStartTime);
            }
            audioVideosModel.setAudioTrackBean(audioTrackBean);
            addToUsedBgm(audioTrackBean, audioVideosModel, this.addMusicPosition, this.isAudioReplace);
            this.isAudioReplace = false;
        }
        changeVideoAudio(this.currentPlayTimeUs);
        if (isFirst) {
            selectMusicFromEditPointAnim(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    onMusicTabClick(true);
                }
            });
        }
        setUndoData(1);
    }

    private void changeVideoAudio() {
        changeVideoAudio(this.currentPlayTimeUs);
    }

    private synchronized void setUndoData(final int type) {
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            LogUtil.d("UNDOMODE", String.format("setUndoData type : %s", Integer.valueOf(type)));
            if (type != 2 && this.isFirstSaveDraft) {
                this.isFirstSaveDraft = false;
                SharedPrefUtil.setUserFirstSaveDraft();
                showAlertSaveDraftDialog();
            }
            PriorityThreadPoolManager.executeInSingleThreadPool(new PriorityRunnable(1) {
                public void run() {
                    mVideoAudioManager.setUndoModel(type, mFilterEffectManager.copyCurrentList(), mFilterName);
                    saveDraft(mFilterModel, mDraftLongVideoBean, false);
                }
            });
        }
    }

    private void hideVideoMusicNameLayout() {
        if (this.video_music_name_parent != null) {
            this.video_music_name_parent.setVisibility(View.GONE);
        }
    }

    private void showVideoMusicNameLayout() {
        initVideoMusicNameLayout();
        this.video_music_name_parent.setVisibility(View.VISIBLE);
    }

    private void setVideoMusicName(String name) {
        if (TextUtil.isValidate(name)) {
            this.video_music_name_iv.setImageResource(R.drawable.icon_15_current_music);
            this.video_music_name_tv.setText(name);
            this.video_music_name_parent.setAlpha(1.0f);
            showMusicVolumeAndRemove();
            return;
        }
        this.video_music_name_iv.setImageResource(R.drawable.icon_15_no_music);
        this.video_music_name_tv.setText(R.string.TEXT_NO_MUSIC);
        this.video_music_name_parent.setAlpha(0.3f);
        hideMusicVolumeAndRemove();
    }

    private void initVideoMusicNameLayout() {
        if (this.video_music_name_vs != null) {
            this.video_music_name_parent = this.video_music_name_vs.inflate();
            this.video_music_name_iv = this.video_music_name_parent.findViewById(R.id.video_music_name_iv);
            this.video_music_name_tv = this.video_music_name_parent.findViewById(R.id.video_music_name_tv);
            this.video_music_name_vs = null;
        }
    }

    private void initVideoEditLayout() {
        if (this.video_edit_parent_new_view_stub != null) {
            this.video_edit_parent_ll = (FrameLayout) this.video_edit_parent_new_view_stub.inflate();
            this.video_edit_time_rv = this.video_edit_parent_ll.findViewById(R.id.video_edit_time_rv);
            this.video_edit_image_rv = this.video_edit_parent_ll.findViewById(R.id.video_edit_image_rv);
            this.video_edit_audio_rv = this.video_edit_parent_ll.findViewById(R.id.video_edit_audio_rv);
            this.video_edit_audio_mute_tv = this.video_edit_parent_ll.findViewById(R.id.video_edit_audio_mute_tv);
            this.video_edit_image_mute_tv = this.video_edit_parent_ll.findViewById(R.id.video_edit_image_mute_tv);
            this.video_edit_mute_indicator = this.video_edit_parent_ll.findViewById(R.id.video_edit_mute_indicator);
            this.video_edit_mute_indicator_cover = this.video_edit_parent_ll.findViewById(R.id.video_edit_mute_indicator_cover);
            this.video_edit_audio_mute_indicator = this.video_edit_parent_ll.findViewById(R.id.video_edit_audio_mute_indicator);
            this.video_edit_video_duration_tv = this.video_edit_parent_ll.findViewById(R.id.video_edit_video_duration_tv);
            this.video_edit_add_music_tv = this.video_edit_parent_ll.findViewById(R.id.video_edit_add_music_tv);
            this.video_edit_missing_footage_fl = this.video_edit_parent_ll.findViewById(R.id.video_edit_missing_footage_fl);
            this.video_edit_image_volume_line = this.video_edit_parent_ll.findViewById(R.id.video_edit_image_volume_line);
            this.video_edit_image_volume_touch = this.video_edit_parent_ll.findViewById(R.id.video_edit_image_volume_touch);
            this.video_edit_audio_volume_line = this.video_edit_parent_ll.findViewById(R.id.video_edit_audio_volume_line);
            this.video_edit_audio_volume_touch = this.video_edit_parent_ll.findViewById(R.id.video_edit_audio_volume_touch);
            this.leftTouchView = this.video_edit_parent_ll.findViewById(R.id.left_touch_view);
            this.rightTouchView = this.video_edit_parent_ll.findViewById(R.id.right_touch_view);
            this.videoEditMissingFootageIvList = new ArrayList();
            this.video_edit_parent_new_view_stub = null;
        }
        if (this.video_edit_text_parent_vs != null) {
            this.video_edit_text_rv = (RecyclerView) this.video_edit_text_parent_vs.inflate();
            this.video_edit_text_parent_vs = null;
        }
        if (this.video_edit_remove_vs != null) {
            this.video_edit_remove_parent = (RelativeLayout) this.video_edit_remove_vs.inflate();
            this.video_edit_remove_vs = null;
            this.video_edit_remove_parent.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    if (mVideoEditHelper != null) {
                        mVideoEditHelper.clearIfExistSelect();
                    }
                }
            });
            this.mVideoEditSwitchTabsHolder = new VideoEditSwitchTabsHolder(this.video_edit_remove_parent);
            this.mVideoEditSwitchTabsHolder.setOnClickListener(this);
        }
    }

    private void showVideoSplitButton(boolean enable) {
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.switchSpitAndAdd(true, enable);
        }
    }

    private void showVideoAddButton() {
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.switchSpitAndAdd(false, false);
        }
    }

    private void initVideoOrderLayout() {
        if (this.order_video_vs != null) {
            this.video_order_parent_rl = (RelativeLayout) this.order_video_vs.inflate();
            this.video_order_parent_rl.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                }
            });
            this.video_order_rv = (RecyclerView) this.video_order_parent_rl.findViewById(R.id.video_order_rv);
            this.video_order_cancel_iv = (ImageView) this.video_order_parent_rl.findViewById(R.id.video_order_cancel_iv);
            this.video_order_confirm_iv = (ImageView) this.video_order_parent_rl.findViewById(R.id.video_order_confirm_iv);
//            this.video_order_cancel_iv.setOnClickListener(23.lambdaFactory$(this));
//            this.video_order_confirm_iv.setOnClickListener(24.lambdaFactory$(this));
            video_order_cancel_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoOrderLayout$21(v);
                }
            });
            video_order_confirm_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$initVideoOrderLayout$22(v);
                }
            });
            this.delayOrderRunnable = new ArrayList();
            this.order_video_vs = null;
        }
    }

    private /* synthetic */ void lambda$initVideoOrderLayout$21(android.view.View v) {
        List<LongVideosModel> adapterList = this.mVideoEditOrderAdapter.getData();
        adapterList.clear();
        adapterList.addAll(this.mVideoAudioManager.getVideosModelList());
        this.mVideoEditOrderAdapter.notifyDataSetChanged();
        this.delayOrderRunnable.clear();
        hideVideoOrderLayout();
    }

    private /* synthetic */ void lambda$initVideoOrderLayout$22(android.view.View v) {
        if (this.lastOrderDragEndPos != -1) {
            this.mVideoAudioManager.getVideosModelList().clear();
            this.mVideoAudioManager.getVideosModelList().addAll(this.mVideoEditOrderAdapter.getData());
            LogUtil.d("slim", "lastOrderDragEndPos:" + this.lastOrderDragEndPos);
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.clearCurSelectVideoModel();
                this.mVideoEditHelper.clearCurAudioSelected();
                this.mVideoEditHelper.resetTextPosAfterOrder(this.mVideoAudioManager.getVideosModelList());
            }
            refreshVideoEditView();
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.scrollToRelatePosAfterReorder(this.lastOrderDragEndPos);
            }
            if (this.mVideoEditHelper != null) {
                this.currentPlayTimeUs = this.mVideoEditHelper.getTextStartTimeAfterInsertBlack(this.lastOrderDragEndPos) * 1000;
            }
            this.mSurfaceView.changePlaylist(this.mVideoAudioManager.getVideosModelList(), getBgmList(), this.currentPlayTimeUs, this.mVideoAudioManager.getMediaMute());
            setUndoData(1);
            this.lastOrderDragEndPos = -1;
        }
        hideVideoOrderLayout();
    }

    private void hideVideoOrderLayout() {
        final int dp = DensityUtil.dip2px(140.0f);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.setDuration(100);
//        valueAnimator.addUpdateListener(25.lambdaFactory$(this, dp));
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lambda$hideVideoOrderLayout$23(dp,animation);
            }
        });
        valueAnimator.addListener(new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                video_order_parent_rl.setVisibility(View.GONE);
                if (mVideoEditSwitchTabsHolder != null) {
                    mVideoEditSwitchTabsHolder.setVisibility(View.VISIBLE);
                }
            }
        });
        valueAnimator.start();
        changeVideoEditVisible();
        restoreSaveAndBack();
    }

    private /* synthetic */ void lambda$hideVideoOrderLayout$23(int dp, ValueAnimator animation) {
        float value = ((Float) animation.getAnimatedValue()).floatValue();
        ViewCompat.setTranslationY(this.video_order_parent_rl, ((float) dp) * value);
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.setAlpha(value);
        }
    }

    private boolean shouldExcuteSeekTo() {
        return true;
    }

    private void initSpecificCombinationRl() {
        if (this.specific_combination_stub != null) {
            this.specific_combination_rl = (RecyclerView) this.specific_combination_stub.inflate();
            this.specific_combination_stub = null;
            if (this.mSpecificCombinationAdapter == null) {
                this.mSpecificCombinationAdapter = new SpecificCombinationAdapter(null,getActivity());//(App.sSpecificCombinations, getActivity());
                this.mSpecificCombinationAdapter.setListener(new RecyclerViewItemClickListener<SpecificCombination>() {
                    public void onItemClickListener(SpecificCombination specificCombination, final int position, android.view.View view) {
                        if (specificCombination.getCombination_id() == 0) {
                            clearCurrFilterEffectAndClearViewAnim(false);
                            loadFilterInfos(false, false, false);
                            setNormalFilter();
                        } else {
                            Iterator it;
                            List<FilterEffectBean> trueFinalResult = new ArrayList();
                            List<FilterEffectBean> lastFilterEffectBeanList = mFilterEffectManager.copyCurrentList();
                            ArrayList<FilterEffectManager.EffectType> lastEffectTypeList = new ArrayList();
                            for (FilterEffectBean effectBean : lastFilterEffectBeanList) {
                                lastEffectTypeList.add(effectBean.effectType);
                            }
                            List<FilterEffectBean> finalEffectBeanList = new ArrayList();
                            List<SpecificsBean> specifics = specificCombination.getSpecifics();
                            if (specifics != null) {
                                for (SpecificsBean bean : specifics) {
                                    FilterEffectManager.EffectType effectTypeByType;
                                    float valueMax;
                                    float valueMin;
                                    int value;
                                    int depth;
                                    int select;
                                    FilterEffectBean beanByName;
                                    FilterEffectBean defaultBean;
                                    if ("Preset".equals(bean.getType())) {
                                        if (TextUtil.isNull(mFilterName) || FilterActivity.NONE_FILTER.equals(mFilterName)) {
                                            mFilterName = bean.getMode();
                                            if (mFilterName == null) {
                                                mFilterName = FilterActivity.NONE_FILTER;
                                            }
                                            mCurrentFilterInfo = mHelper.getFilterInfoByFilterName(mFilterInfos, mFilterName);
                                            int filterPosition = mFilterInfos.indexOf(mCurrentFilterInfo);
                                            final int selectFilterGroupPosition = mHelper.shouldSelectFilterGroupPosition(mFilterGroupNames, mFilterInfos, filterPosition);
                                            filterGroupNameAdapter.setSelectPosition(selectFilterGroupPosition);
                                            filterGroupNameAdapter.notifyDataSetChanged();
                                            filter_group_name_rv.scrollToPosition(selectFilterGroupPosition);
                                            //filter_group_name_rv.post(FilterActivity$19$.Lambda.1.lambdaFactory$(this, selectFilterGroupPosition));
                                            filter_group_name_rv.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    lambda$onItemClickListener$0(selectFilterGroupPosition);
                                                }
                                            });
                                            isChangeFilterCombination = true;
                                            filterAdapter.firstClickFilter(filterPosition, mCurrentFilterInfo, null, true);
                                        }
                                    } else if ("Leak".equals(bean.getType())) {
                                        if (mDataType == 1) {
                                            effectTypeByType = FilterEffectManager.getEffectTypeByType(bean.getType());
                                            if (!isVideoType() || !FilterEffectManager.VideoHideEffectTypes.contains(effectTypeByType)) {
                                                valueMax = StaticLayoutUtil.DefaultSpacingadd;
                                                try {
                                                    valueMax = Float.parseFloat(bean.getValueMax());
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                valueMin = StaticLayoutUtil.DefaultSpacingadd;
                                                try {
                                                    valueMin = Float.parseFloat(bean.getValueMin());
                                                } catch (NumberFormatException e2) {
                                                    e2.printStackTrace();
                                                }
                                                value = (int) ((Math.random() * ((double) ((valueMax - valueMin) + 1.0f))) + ((double) valueMin));
                                                depth = (int) (Math.random() * 4.0d);
                                                select = (int) (Math.random() * 4.0d);
                                                FilterEffectBean leakItem = mFilterEffectManager.getDefaultBean(FilterEffectManager.EffectType.LEAK);
                                                beanByName = FilterEffectManager.getEffectBeanByName(lastFilterEffectBeanList, bean.getType());
                                                defaultBean = mFilterEffectManager.getDefaultBean(leakItem.effectType);
                                                if (beanByName != null) {
                                                    loadFilterEffectJustInit(beanByName);
                                                    lastFilterEffectBeanList.remove(beanByName);
                                                } else {
                                                    loadFilterEffectJustInit(defaultBean);
                                                    mFilterEffectManager.setCurrentListItem(defaultBean);
                                                }
                                                leakItem.value[1] = (float) value;
                                                leakItem.selectPosition[0] = (float) select;
                                                leakItem.selectPosition[1] = (float) depth;
                                                finalEffectBeanList.add(leakItem);
                                                trueFinalResult.add(leakItem);
                                                setFilterViewAnim(effectTypeByType, finalEffectBeanList);
                                                if (lastEffectTypeList.contains(effectTypeByType)) {
                                                    lastEffectTypeList.remove(effectTypeByType);
                                                }
                                                LogUtil.d("TYPE_LEAK", "value : " + value + ",  depth : " + depth + ", select : " + select);
                                            }
                                        }
                                    } else if (!"Dust".equals(bean.getType())) {
                                        String value2 = bean.getValue();
                                        effectTypeByType = FilterEffectManager.getEffectTypeByType(bean.getType());
                                        if (!isVideoType() || !FilterEffectManager.VideoHideEffectTypes.contains(effectTypeByType)) {
                                            FilterEffectBean filterEffectBeanByName = mFilterEffectManager.getFilterEffectBeanByName(bean.getType());
                                            beanByName = FilterEffectManager.getEffectBeanByName(lastFilterEffectBeanList, bean.getType());
                                            defaultBean = mFilterEffectManager.getDefaultBean(filterEffectBeanByName.effectType);
                                            if (beanByName != null) {
                                                loadFilterEffectJustInit(beanByName);
                                                lastFilterEffectBeanList.remove(beanByName);
                                            } else {
                                                loadFilterEffectJustInit(defaultBean);
                                                mFilterEffectManager.setCurrentListItem(defaultBean);
                                            }
                                            if (TextUtil.isValidate(value2)) {
                                                filterEffectBeanByName.value[1] = Float.parseFloat(value2);
                                            } else {
                                                String valueMax2 = bean.getValueMax();
                                                String valueMin2 = bean.getValueMin();
                                                if (TextUtil.isValidate(valueMax2) && TextUtil.isValidate(valueMin2)) {
                                                    float max = Float.parseFloat(valueMax2);
                                                    float min = Float.parseFloat(valueMin2);
                                                    filterEffectBeanByName.value[1] = (float) ((int) ((Math.random() * ((double) ((max - min) + 1.0f))) + ((double) min)));
                                                }
                                            }
                                            finalEffectBeanList.add(filterEffectBeanByName);
                                            trueFinalResult.add(filterEffectBeanByName);
                                            setFilterViewAnim(effectTypeByType, (List) finalEffectBeanList);
                                            if (lastEffectTypeList.contains(effectTypeByType)) {
                                                lastEffectTypeList.remove(effectTypeByType);
                                            }
                                        }
                                    } else if (mDataType == 1) {
                                        effectTypeByType = FilterEffectManager.getEffectTypeByType(bean.getType());
                                        if (!isVideoType() || !FilterEffectManager.VideoHideEffectTypes.contains(effectTypeByType)) {
                                            valueMax = StaticLayoutUtil.DefaultSpacingadd;
                                            try {
                                                valueMax = Float.parseFloat(bean.getValueMax());
                                            } catch (NumberFormatException e22) {
                                                e22.printStackTrace();
                                            }
                                            valueMin = StaticLayoutUtil.DefaultSpacingadd;
                                            try {
                                                valueMin = Float.parseFloat(bean.getValueMin());
                                            } catch (NumberFormatException e222) {
                                                e222.printStackTrace();
                                            }
                                            value = (int) ((Math.random() * ((double) ((valueMax - valueMin) + 1.0f))) + ((double) valueMin));
                                            depth = (int) (Math.random() * 4.0d);
                                            select = (int) (Math.random() * 3.0d);
                                            FilterEffectBean dustItem = mFilterEffectManager.getDefaultBean(FilterEffectManager.EffectType.DUST);
                                            beanByName = FilterEffectManager.getEffectBeanByName(lastFilterEffectBeanList, bean.getType());
                                            defaultBean = mFilterEffectManager.getDefaultBean(dustItem.effectType);
                                            if (beanByName != null) {
                                                loadFilterEffectJustInit(beanByName);
                                                lastFilterEffectBeanList.remove(beanByName);
                                            } else {
                                                loadFilterEffectJustInit(defaultBean);
                                                mFilterEffectManager.setCurrentListItem(defaultBean);
                                            }
                                            dustItem.value[1] = (float) value;
                                            dustItem.selectPosition[0] = (float) select;
                                            dustItem.selectPosition[1] = (float) depth;
                                            finalEffectBeanList.add(dustItem);
                                            trueFinalResult.add(dustItem);
                                            setFilterViewAnim(effectTypeByType, (List) finalEffectBeanList);
                                            if (lastEffectTypeList.contains(effectTypeByType)) {
                                                lastEffectTypeList.remove(effectTypeByType);
                                            }
                                            LogUtil.d("TYPE_DUST", "value : " + value + ",  depth : " + depth + ", select : " + select);
                                        }
                                    }
                                }
                                for (FilterEffectBean bean2 : lastFilterEffectBeanList) {
                                    if (bean2.effectType != FilterEffectManager.EffectType.BEAUTIFY &&
                                            bean2.effectType != FilterEffectManager.EffectType.CROP &&
                                            bean2.effectType != FilterEffectManager.EffectType.ROTATE &&
                                            bean2.effectType != FilterEffectManager.EffectType.VERTICAL &&
                                            bean2.effectType != FilterEffectManager.EffectType.HORIZONTAL &&
                                            bean2.effectType != FilterEffectManager.EffectType.MIRROR) {
                                        finalEffectBeanList.add(mFilterEffectManager.getDefaultBean(bean2.effectType));
                                    } else if (!finalEffectBeanList.contains(bean2)) {
                                        finalEffectBeanList.add(bean2);
                                    }
                                }
                                it = lastEffectTypeList.iterator();
                                while (it.hasNext()) {
                                    setFilterViewAnim((FilterEffectManager.EffectType) it.next(), (List) finalEffectBeanList);
                                }
                                if (isFirstClick) {
                                    final List<FilterEffectBean> list = finalEffectBeanList;
                                    final List<FilterEffectBean> list2 = trueFinalResult;
                                    PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                                        public void run() {
                                            setSpecialFilter();
                                            loadFilterInfosSpecial(list);
                                            mFilterEffectManager.clearCurrentFilterEffect();
                                            mFilterEffectManager.setDraftFilterData(list2);
                                        }
                                    });
                                    isFirstClick = false;
                                } else {
                                    setSpecialFilter();
                                    loadFilterInfosSpecial(finalEffectBeanList);
                                    mFilterEffectManager.clearCurrentFilterEffect();
                                    mFilterEffectManager.setDraftFilterData(trueFinalResult);
                                }
                            } else {
                                return;
                            }
                        }
                        specific_combination_rl.scrollToPosition(position);
                        //specific_combination_rl.post(FilterActivity$19$.Lambda.2.lambdaFactory$(this, position));
                        specific_combination_rl.post(new Runnable() {
                            @Override
                            public void run() {
                                lambda$onItemClickListener$1(position);
                            }
                        });
                    }

                    private /* synthetic */ void lambda$onItemClickListener$0(int selectFilterGroupPosition) {
                        android.view.View viewByPosition = mFilterGroupNameManager.findViewByPosition(selectFilterGroupPosition);
                        if (viewByPosition != null) {
                            filter_group_name_rv.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                        }
                    }

                    private /* synthetic */ void lambda$onItemClickListener$1(int position) {
                        android.view.View viewByPosition = specific_combination_rl.getLayoutManager().findViewByPosition(position);
                        if (viewByPosition != null) {
                            specific_combination_rl.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                        }
                    }

                    public void onItemLongClickListener(SpecificCombination specificCombination, int position, android.view.View view) {
                    }
                });
            }
            this.specific_combination_rl.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
            this.specific_combination_rl.setAdapter(this.mSpecificCombinationAdapter);
        }
    }

    private void clearCurrFilterEffectAndClearViewAnim(boolean fromUndo) {
        List<FilterEffectBean> currentList = this.mFilterEffectManager.getCurrentList();
        ArrayList<FilterEffectManager.EffectType> effectTypeList = new ArrayList();
        for (FilterEffectBean effectBean : currentList) {
            effectTypeList.add(effectBean.effectType);
        }
        this.mFilterEffectManager.clearCurrentFilterEffect();
        Iterator it = effectTypeList.iterator();
        while (it.hasNext()) {
            setFilterViewAnim((FilterEffectManager.EffectType) it.next(), fromUndo);
        }
    }

    private void onLoopTabClick() {
        hideWhiteAlphaCoverViewWhenSwitchTab();
        switchBottomIconAndCircleShowState(3);
        if (this.specific_combination_rl != null) {
            this.specific_combination_rl.setVisibility(View.GONE);
        }
        checkSurfaceNeedScale(false);
        this.filter_stablize_rl.setVisibility(View.VISIBLE);
    }

    private void onVideoEditTabClick() {
        switchBottomIconAndCircleShowState(4);
        if (this.specific_combination_rl != null) {
            this.specific_combination_rl.setVisibility(View.GONE);
        }
        this.filter_stablize_rl.setVisibility(View.GONE);
        checkSurfaceNeedScale(false);
        this.video_edit_parent_ll.setVisibility(View.VISIBLE);
        showEditCenterLine();
        this.video_edit_remove_parent.setVisibility(View.VISIBLE);
        hideVideoMusicLayout();
        showWhiteAlphaCoverViewWhenNeed();
        showTextRecyclerView();
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.onSwitchTabClick(false);
        }
    }

    private void hideWhiteAlphaCoverViewWhenSwitchTab() {
        refreshBottomCoverViewVisibility(false);
    }

    private void showWhiteAlphaCoverViewWhenNeed() {
        if (this.mVideoEditHelper != null && this.mVideoEditHelper.isInTextSelectStatus()) {
            refreshBottomCoverViewVisibility(true);
        }
    }

    private void showBottomCoverView() {
        this.filter_tab_bottom_cover_view.setVisibility(View.VISIBLE);
    }

    private void hideBottomCoverView() {
        this.filter_tab_bottom_cover_view.setVisibility(View.GONE);
    }

    private void onEffectTabShowHideOthers() {
        initSpecificCombinationRl();
        this.filter_stablize_rl.setVisibility(View.GONE);
        checkSurfaceNeedScale(false);
        this.specific_combination_rl.setVisibility(View.VISIBLE);
    }

    private void onEffectTabClick() {
        hideWhiteAlphaCoverViewWhenSwitchTab();
        switchBottomIconAndCircleShowState(2);
        initSpecificCombinationRl();
        this.filter_stablize_rl.setVisibility(View.GONE);
        checkSurfaceNeedScale(false);
        this.specific_combination_rl.setVisibility(View.VISIBLE);
        hideVideoEditView();
        hideVideoMusicLayout();
    }

    private void onFilterTabClick() {
        hideWhiteAlphaCoverViewWhenSwitchTab();
        switchBottomIconAndCircleShowState(1);
        if (this.specific_combination_rl != null) {
            this.specific_combination_rl.setVisibility(View.GONE);
        }
        this.filter_stablize_rl.setVisibility(View.GONE);
        checkSurfaceNeedScale(true);
        hideVideoEditView();
        hideVideoMusicLayout();
    }

    private void checkSurfaceNeedScale(boolean show) {
        int needChangeHeight = DensityUtil.dip2px(45.0f);
        LayoutParams layoutParams;
        ArrayList<LongVideosModel> textModelList;
        if (show) {
            this.filter_group_name_rv.setVisibility(View.VISIBLE);
            this.filter_select_mode_rl.setVisibility(View.VISIBLE);
            if (FilterViewUtils.isVideoModel(this.mDataType)) {
                int realHeight;
                resetFilterModeView();
                float usableWidth = (float) DensityUtil.getMetricsWidth(this);
                float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getVideoTopAndBottomSumViewHeight());
                float gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
                if (gridScale > usableWidth / usableHeight) {
                    realHeight = (int) (((float) ((int) usableWidth)) / gridScale);
                } else {
                    realHeight = (int) usableHeight;
                    int i = (int) (((float) realHeight) * gridScale);
                }
                if ((usableHeight - ((float) realHeight)) * 2.0f < ((float) needChangeHeight)) {
                    this.changeSurfaceSize = true;
                    layoutParams = this.surfaceParams;
                    layoutParams.height -= needChangeHeight;
                    this.mSurfaceView.setLayoutParams(this.surfaceParams);
                    this.filter_beauty_anim.setLayoutParams(this.surfaceParams);
                    this.capture_filter_name_rl.setLayoutParams(this.surfaceParams);
                    this.surface_click_view.setLayoutParams(this.surfaceParams);
                    initCurrentClipIv(this.mVideoWidth, this.mVideoHeight);
                    initMissingFileHint();
                    initPlayBtn(this.mVideoWidth, this.mVideoHeight);
                    if (TextUtil.isValidate(this.mCurrentTextLongVideoModel)) {
                        invalidateVtContainView(this.mCurrentTextLongVideoModel, true);
                        return;
                    }
                    textModelList = this.mVideoAudioManager.getTextModelList();
                    if (TextUtil.isValidate(textModelList)) {
                        invalidateVtContainView(textModelList.get(0), true);
                        return;
                    }
                    return;
                }
                return;
            }
            this.filter_select_mode_rl.setVisibility(View.GONE);
            return;
        }
        this.filter_group_name_rv.setVisibility(View.GONE);
        this.filter_select_mode_rl.setVisibility(View.GONE);
        if (!FilterViewUtils.isVideoModel(this.mDataType)) {
            this.filter_select_mode_rl.setVisibility(View.GONE);
        } else if (this.changeSurfaceSize) {
            this.changeSurfaceSize = false;
            layoutParams = this.surfaceParams;
            layoutParams.height += needChangeHeight;
            this.mSurfaceView.setLayoutParams(this.surfaceParams);
            this.filter_beauty_anim.setLayoutParams(this.surfaceParams);
            this.capture_filter_name_rl.setLayoutParams(this.surfaceParams);
            this.surface_click_view.setLayoutParams(this.surfaceParams);
            initCurrentClipIv(this.mVideoWidth, this.mVideoHeight);
            initMissingFileHint();
            initPlayBtn(this.mVideoWidth, this.mVideoHeight);
            if (TextUtil.isValidate(this.mCurrentTextLongVideoModel)) {
                invalidateVtContainView(this.mCurrentTextLongVideoModel, false);
                return;
            }
            textModelList = this.mVideoAudioManager.getTextModelList();
            if (TextUtil.isValidate(textModelList)) {
                invalidateVtContainView(textModelList.get(0), false);
            }
        }
    }

    private void setTabBottomSelected(boolean selected, ImageView iconBottom, ImageView circle) {
        if (selected) {
            if (iconBottom != null) {
                iconBottom.setAlpha(1.0f);
            }
            if (circle != null) {
                circle.setVisibility(View.VISIBLE);
                return;
            }
            return;
        }
        if (iconBottom != null) {
            iconBottom.setAlpha(0.4f);
        }
        if (circle != null) {
            circle.setVisibility(View.INVISIBLE);
        }
    }

    private void alphaEnterBottomView(android.view.View view) {
        FilterActivityHelper.alphaEnter(view);
    }

    private void alphaExitBottomView(android.view.View view) {
        FilterActivityHelper.alphaExit(view);
    }

    private void refreshBottomViewState(boolean selected, ImageView iconBottom, ImageView circle, android.view.View contentView) {
        setTabBottomSelected(selected, iconBottom, circle);
        if (selected) {
            alphaEnterBottomView(contentView);
        } else {
            alphaExitBottomView(contentView);
        }
    }

    private void switchBottomIconAndCircleShowState(int tabIndex) {
        refreshBottomViewState(tabIndex == 1, this.filter_tag_filter_iv, this.filter_filter_icon_bottom, this.filter_filter_root_rl);
        refreshBottomViewState(tabIndex==2, this.filter_tag_effect_iv, this.filter_effect_icon_bottom, this.filter_effect_root_ll);
        if (isVideoType()) {
            setTabBottomSelected(tabIndex==4, this.filter_tag_loop_iv, this.filter_edit_icon_bottom);
            if (tabIndex == 4) {
                if (this.video_edit_parent_ll.getVisibility() != View.VISIBLE) {
                    alphaEnterBottomView(this.video_edit_parent_ll);
                }
            } else if (tabIndex != 5 && this.video_edit_parent_ll.getVisibility() == View.VISIBLE) {
                alphaExitBottomView(this.video_edit_parent_ll);
            }
        } else {
            refreshBottomViewState(tabIndex==3, this.filter_tag_loop_iv, this.filter_edit_icon_bottom, this.filter_loop_root_rl);
        }
        if (isVideoType()) {
            setTabBottomSelected(tabIndex==5, this.filter_tag_music_iv, this.filter_music_icon_bottom);
            if (tabIndex == 5) {
                if (this.video_edit_parent_ll.getVisibility() != View.VISIBLE) {
                    alphaEnterBottomView(this.video_edit_parent_ll);
                }
            } else if (tabIndex != 4 && this.video_edit_parent_ll.getVisibility() == View.VISIBLE) {
                alphaExitBottomView(this.video_edit_parent_ll);
            }
        }
    }

    private void getIntentData(Intent intent) {
        this.mDataFrom = intent.getIntExtra(DATA_FROM, 0);
        this.mDataType = intent.getIntExtra(DATA_TYPE, 0);
        this.mBundle = intent.getExtras();
        this.orientation = intent.getIntExtra(ORIENTATION_INTENT, 0);
        this.mCreationDate = intent.getLongExtra("intent_key_creation_date", System.currentTimeMillis() / 1000);
        this.mGifPath = intent.getStringExtra(GIF_LOCAL_PATH);
        Bundle bundleExtra = intent.getBundleExtra("BundleIntent");
        if (bundleExtra != null) {
            this.mBundle.putString("ActivityFromBundle", bundleExtra.getString("ActivityFromBundle"));
            this.mBundle.putString("FromUserNameBundle", bundleExtra.getString("FromUserNameBundle"));
            this.mBundle.putString("FromCurrentTimeStampBundle", bundleExtra.getString("FromCurrentTimeStampBundle"));
        }
        this.mActivityFrom = this.mBundle.getString("ActivityFromBundle");
        this.hasAudioPermission = this.mBundle.getBoolean("has_audio_permission", false);
        this.mFromUserName = this.mBundle.getString("FromUserNameBundle");
        this.mFromCurrentTimeStamp = this.mBundle.getString("FromCurrentTimeStampBundle");
        this.mIsBackCamera = intent.getBooleanExtra(BACK_OR_FRONT_CAMERA, false);
        this.mIsSquare = intent.getBooleanExtra(IS_SQUARE_INTENT, false);
        this.mCameraLensType = intent.getIntExtra(TRANSFORM_INTENT, 0);
        this.mVideoRotation = intent.getIntExtra(VIDEO_ROTATION, 0);
        this.rotation = intent.getIntExtra(ORIENTATION_INTENT2, 0);
        this.mOffsetX = intent.getFloatExtra(OFFSETX_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        this.mOffsetY = intent.getFloatExtra(OFFSETY_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        this.mFps = intent.getFloatExtra(FPS_INTENT, StaticLayoutUtil.DefaultSpacingadd);
        this.hasRecordVideo = intent.getBooleanExtra(RECORD_VIDEO, false);
        this.mMovieBean = intent.getParcelableExtra(LOCATION_INTENT);
    }

    private void initSomeConstantAndSettings() {
        this.title_preview.getPaint().setFakeBoldText(true);
        this.metricsWidth = DensityUtil.getMetricsWidth(this);
        this.metricsHeight = DensityUtil.getMetricsHeight(this);
        this.userLongVideoDuration = GlobalHelper.getUserLongVideoDuration();
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            this.isFirstSaveDraft = SharedPrefUtil.getUserFirstSaveDraft();
        } else {
            this.isFirstSaveDraft = false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        App.RegisterEventBus(this);
//        BuglyLogUtil.writeBuglyLog(TAG);
        Intent intent = getIntent();
        getIntentData(intent);
        initSomeConstantAndSettings();
        initTestView();
        PublishDraftEvent publishDraftEvent = (PublishDraftEvent) EventBus.getDefault().getStickyEvent(PublishDraftEvent.class);
        this.mDraftLongVideoBean = new DraftLongVideoBean();
        if (publishDraftEvent != null) {
            this.mDraftModel = publishDraftEvent.getDraftModel();
            this.mFilterModel = this.mDraftModel.getFilterModel();
            this.mDraftLongVideoBean = this.mDraftModel.getmLongVideoBean();
            EventBus.getDefault().removeStickyEvent(PublishDraftEvent.class);
        }
        this.mCaptureMode = CornerDistanceModel.getInstance().captureModel;
        if (2 != this.mDataType) {
            this.mVideoFrames = FramesHolder.getInstance().getFrames();
            if (this.mVideoFrames != null && this.mVideoFrames.size() != 0) {
                this.mFrameCount = this.mVideoFrames.size();
                this.transformNeedCount = this.mFrameCount;
                setPlayButtonVisibility(8);
            } else {
                return;
            }
        }
        boolean isPortrait = this.orientation == 0 || this.orientation == 180;
        if (initDataWidthAndHeight(intent, isPortrait)) {
            int i;
            if (this.mDataFrom != 3) {
                this.mDraftModel = null;
                if (this.mDataFrom != 0) {
                    this.mFilterModel = new FilterModel();
                }
                this.mCurrentTimeStamp = String.valueOf(new Date().getTime());
            } else if (this.mDraftModel == null || this.mFilterModel == null) {
                this.mFilterModel = new FilterModel();
                this.mCurrentTimeStamp = String.valueOf(new Date().getTime());
            } else {
                this.mCurrentTimeStamp = this.mDraftModel.getTimeStamp();
                this.orientation = this.mFilterModel.getOrientation();
                this.mCreationDate = this.mFilterModel.getCreationDate();
            }
            this.mFilterEffectManager = new FilterEffectManager();
            if (3 == this.mDataFrom) {
                this.mFilterEffectManager.setDraftFilterData(this.mFilterModel.getSelectEffectTypeList());
            } else if (this.mIsBackCamera) {
                this.mFilterEffectManager.setCurrentListItem(this.mFilterEffectManager.getFrontBeautyBean(true));
            }
            this.mCaptureOrientation = this.orientation;
            boolean notFromCamera = this.mDataFrom != 0;
            boolean isPicture = 1 == this.mDataType;
            boolean notFromDraft = this.mDataFrom != 0;
            boolean shouldOrientation270 = (notFromDraft && notFromCamera && (this.mDataType == 0)) || ((notFromCamera && (3 == this.mDataType)) || (isPicture && notFromDraft));
            if (shouldOrientation270) {
                this.mCaptureOrientation = BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
            }
            boolean captureSquare = this.mCaptureMode == 5;
            boolean notVideoModel = !FilterViewUtils.isVideoModel(this.mDataType);
            boolean notFromLocal = 1 != this.mDataFrom;
            if (captureSquare && notFromLocal && notVideoModel) {
                this.mFilterEffectManager.setCurrentListItem(this.mFilterEffectManager.getModel5Bean());
                setCropFilterForModel5(this.mFilterEffectManager.getSelectEffectValues(FilterEffectManager.EffectType.CROP));
            }
            this.minUnLoopCount = 12;
            if (this.mFps == StaticLayoutUtil.DefaultSpacingadd) {
                i = 6;
            } else {
                i = this.minUnLoopCount;
            }
            this.minUnLoopCount = i;
            this.mOriginVideoWidth = this.mVideoWidth;
            this.mOriginVideoHeight = this.mVideoHeight;
            this.mDetermineVideoHeight = this.mVideoHeight;
            this.mDetermineVideoWidth = this.mVideoWidth;
            if (this.mVideoWidth > this.mVideoHeight) {
                this.videoRatio = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
            } else {
                this.videoRatio = (((float) this.mVideoHeight) * 1.0f) / ((float) this.mVideoWidth);
            }
            if (this.mFps == StaticLayoutUtil.DefaultSpacingadd) {
                this.stack_frame_switch_rl.setVisibility(View.INVISIBLE);
            }
            if (this.mDataType == 0 && 3 == this.mDataFrom) {
                this.mIsShort = this.mVideoFrames.size() == 10;
                this.openStackFrame = this.mFilterModel.getStackFrameState();
            } else if (this.mDataType != 2) {
                this.mIsShort = this.mVideoFrames.size() == 6;
                boolean z = this.mFps != StaticLayoutUtil.DefaultSpacingadd && this.mIsShort;
                this.mIsShort = z;
            }
            initView();
            initLeakFilterReqWidthHeight();
            initCurrentClipIv(this.mOriginVideoWidth, this.mOriginVideoHeight);
            initMissingFileHint();
            initPlayBtn(this.mOriginVideoWidth, this.mOriginVideoHeight);
            initData();
            if (this.mDataType == 0 || 3 == this.mDataType) {
                loadFrames();
            } else if (1 == this.mDataType) {
                loadFramesPicture();
            } else if (2 == this.mDataType) {
                initVideoAudioManager(intent);
            }
            initGestute();
            this.mEnableStabilizer = this.mFilterModel.ismEnableStabilizer();
            this.translateAnimationShow = new TranslateAnimation(1, StaticLayoutUtil.DefaultSpacingadd, 1, StaticLayoutUtil.DefaultSpacingadd, 1, 1.5f, 1, StaticLayoutUtil.DefaultSpacingadd);
            this.translateAnimationShow.setDuration(200);
            this.translateAnimationHide = new TranslateAnimation(1, StaticLayoutUtil.DefaultSpacingadd, 1, StaticLayoutUtil.DefaultSpacingadd, 1, StaticLayoutUtil.DefaultSpacingadd, 1, 1.5f);
            this.translateAnimationHide.setDuration(200);
            this.mirrorSetText = new String[]{getString(R.string.BUTTON_EFFECT_NONE), getString(R.string.BUTTON_EFFECT_MIRROR_TOP), getString(R.string.BUTTON_EFFECT_MIRROR_BOTTOM), getString(R.string.BUTTON_EFFECT_MIRROR_LEFT), getString(R.string.BUTTON_EFFECT_MIRROR_RIGHT)};
            this.mFilterActivityPresenter = new FilterActivityPresenter(this);
            final android.view.View viewById = findViewById(R.id.filter_root);
            //viewById.post(26.lambdaFactory$(this, viewById));
            viewById.post(new Runnable() {
                @Override
                public void run() {
                    lambda$onCreate$24(viewById);
                }
            });
            if (isVideoType()) {
                showVideoEditView();
                this.filter_filter_icon_bottom.setVisibility(View.INVISIBLE);
                this.filter_edit_icon_bottom.setVisibility(View.VISIBLE);
                getVTFontInfos(false, "");
                return;
            }
            return;
        }
        finish();
    }

    private /* synthetic */ void lambda$onCreate$24(android.view.View viewById) {
        this.needScreenWidth = viewById.getWidth();
        this.needScreenHeight = viewById.getHeight();
        LogUtil.d("needScreenSize", String.format("needScreenWidth : %s , needScreenHeight : %s ", this.needScreenWidth, this.needScreenHeight));
    }

    private void selectMusicFromEditPointAnim(AnimatorEndListener endListener) {
        if (this.filter_edit_icon_bottom.getVisibility() == View.VISIBLE) {
            final LayoutParams layoutParams = (LayoutParams) this.filter_bottom_tab_move_point.getLayoutParams();
            int[] ints = new int[2];
            final int[] ints2 = new int[2];
            this.filter_edit_icon_bottom.getLocationOnScreen(ints);
            this.filter_music_icon_bottom.getLocationOnScreen(ints2);
            layoutParams.leftMargin = ints[0];
            this.filter_bottom_tab_move_point.setLayoutParams(layoutParams);
            this.filter_bottom_tab_move_point.setVisibility(View.VISIBLE);
            ValueAnimator animator = ValueAnimator.ofInt(ints[0], ints2[0]);
            animator.setDuration(200);
            onMusicTabClick();
            this.filter_music_icon_bottom.setVisibility(View.INVISIBLE);
            animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int leftMargin = (Integer) animation.getAnimatedValue();
                    layoutParams.leftMargin = leftMargin;
                    filter_bottom_tab_move_point.setLayoutParams(layoutParams);
                    if (leftMargin == ints2[0]) {
                        filter_bottom_tab_move_point.setVisibility(View.GONE);
                        filter_music_icon_bottom.setVisibility(View.VISIBLE);
                    }
                }
            });
            animator.addListener(endListener);
            animator.start();
        }
    }

    private boolean initDataWidthAndHeight(Intent intent, boolean isPortrait) {
        if (this.mDataFrom == 0 || this.mDataFrom == 2) {
            if (this.mCaptureMode == 2) {
                if (this.mVideoFrames == null) {
                    return false;
                }
                if (isPortrait) {
                    this.mVideoWidth = this.mVideoFrames.mCaptureHeight;
                    this.mVideoHeight = this.mVideoFrames.mCaptureWidth;
                } else {
                    this.mVideoWidth = this.mVideoFrames.mCaptureWidth;
                    this.mVideoHeight = this.mVideoFrames.mCaptureHeight;
                }
            } else if (this.mDataType == 2) {
                this.mVideoWidth = intent.getIntExtra(VIDEO_WIDTH_INTENT, 0);
                this.mVideoHeight = intent.getIntExtra(VIDEO_HEIGHT_INTENT, 0);
            } else if (isPortrait) {
                this.mVideoWidth = this.mVideoFrames.mCaptureHeight;
                this.mVideoHeight = this.mVideoFrames.mCaptureWidth;
            } else {
                this.mVideoWidth = this.mVideoFrames.mCaptureWidth;
                this.mVideoHeight = this.mVideoFrames.mCaptureHeight;
            }
        } else if (this.mDataFrom == 1 && (this.mDataType == 0 || this.mDataType == 3)) {
            this.mVideoWidth = intent.getIntExtra(VIDEO_WIDTH_INTENT, 0);
            this.mVideoHeight = intent.getIntExtra(VIDEO_HEIGHT_INTENT, 0);
            if (this.mVideoWidth == 0 || this.mVideoHeight == 0) {
                if (isPortrait) {
                    this.mVideoWidth = this.mVideoFrames.mCaptureHeight;
                    this.mVideoHeight = this.mVideoFrames.mCaptureWidth;
                } else {
                    this.mVideoWidth = this.mVideoFrames.mCaptureWidth;
                    this.mVideoHeight = this.mVideoFrames.mCaptureHeight;
                }
            }
        } else {
            if (this.mDataType == 2) {
                if (1 == this.mDataFrom) {
                    this.mVideoWidth = intent.getIntExtra(VIDEO_WIDTH_INTENT, 0);
                    this.mVideoHeight = intent.getIntExtra(VIDEO_HEIGHT_INTENT, 0);
                } else if (TextUtil.isValidate(this.mDraftLongVideoBean)) {
                    this.mVideoWidth = this.mDraftLongVideoBean.getLongVideoWidth();
                    this.mVideoHeight = this.mDraftLongVideoBean.getLongVideoHeight();
                }
                LogUtil.d("longvideodraft", String.format("width:%s, height:%s,portrait:%s", this.mVideoWidth, this.mVideoHeight, isPortrait));
            } else {
                this.mVideoWidth = this.mVideoFrames.mCaptureWidth;
                this.mVideoHeight = this.mVideoFrames.mCaptureHeight;
            }
            this.openStackFrame = false;
            if (this.mDataType == 0 && 3 == this.mDataFrom) {
                this.stack_frame_switch_rl.setVisibility(View.VISIBLE);
            } else {
                this.stack_frame_switch_rl.setVisibility(View.INVISIBLE);
            }
        }
        saveVideoWHToBitmapManager();
        return true;
    }

    private void saveVideoWHToBitmapManager() {
        VideoBitmapsModel.instancesModel().setCurrentWH(FilterViewUtils.getUploadLongVideoWidthHeight(this.mVideoWidth, this.mVideoHeight, true));
    }

    private void initTestView() {
    }

    private void resetIntentVideoModels(List<LongVideosModel> mList) {
        if (mList != null && mList.size() > 0) {
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                LongVideosModel model = mList.get(i);
                model.setVideoPath(FileUtil.getPrivateAlbumFilePath(model.getOriginalMediaPath()));
                mList.set(i, model);
            }
        }
    }

    private void initVideoAudioManager(final Intent intent) {
        this.mVideoAudioManager = new VideoAudioPlaybackManager();
        this.mSurfaceView.setDataType(2, this.mDataFrom);
        this.mSurfaceView.setLongVideoCallback(new LongVideoPlayCallback() {
            public void playlistPercent(final double percent, final double sumPercent, final boolean moreThanOnce, long currentPlayingTimeUs) {
                currentPlayTimeUs = currentPlayingTimeUs;
                if (canControlVideo) {
                    canControlVideo = false;
                }
                if (video_music_split_parent != null && video_music_split_parent.getVisibility() == View.VISIBLE) {
                    //runOnUiThread(FilterActivity$21$.Lambda.1.lambdaFactory$(this, percent, sumPercent, moreThanOnce));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lambda$playlistPercent$0(percent,sumPercent,moreThanOnce);
                        }
                    });
                }
            }

            private /* synthetic */ void lambda$playlistPercent$0(double percent, double sumPercent, boolean moreThanOnce) {
                video_music_split_parent.onPlaying(percent, sumPercent, moreThanOnce);
            }

            public void playlistPercentForLocal(final double percent) {
                playbackPercent = percent;
                LogUtil.d("playbackPercent", playbackPercent + "");
                if (percent >= 0.0d && percent < 1.0d) {
                    if (mAudioTrimLayout != null && mAudioTrimLayout.getVisibility() == View.VISIBLE) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAudioTrimLayout.onVideoPlay(percent);
                            }
                        });
                    }
                    if (video_edit_parent_ll == null || video_edit_parent_ll.getVisibility() != View.VISIBLE) {
                        if (mVideoEditHelper != null) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (mVideoEditHelper != null) {
                                        mVideoEditHelper.refreshTextViewWhenInVisible(percent);
                                    }
                                }
                            });
                        }
                    } else if (mVideoEditHelper != null) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (mVideoEditHelper != null) {
                                    mVideoEditHelper.setViewScrollTo(percent);
                                    if (mVideoEditHelper.isMusicEdit()) {
                                        mVideoEditHelper.refreshTextViewWhenInVisible(percent);
                                    }
                                }
                            }
                        });
                    }
                } else if (percent == 1.0d) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            player_rl.changePlayState(true);
                            if (mAudioTrimLayout != null && mAudioTrimLayout. getVisibility() ==View.VISIBLE) {
                                mAudioTrimLayout.changePlayState(true);
                            }
                            mSurfaceView.setVideoPauseOrResume(true);
                        }
                    });
                }
            }

            public void startDrawFrame() {
                if (!mRestart) {
                    //runOnUiThread(FilterActivity$21$.Lambda.2.lambdaFactory$(this));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lambda$startDrawFrame$1();
                        }
                    });
                }
            }

            private /* synthetic */ void lambda$startDrawFrame$1() {
                if (mDraftModel != null) {
                    restoreFilters();
                }
                ViewPropertyAnimator.animate(filter_alpha_view).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(FilterActivity.CHANEG_FILTER_TIME).start();
                if (mConntNext) {
                    mTvSave.setEnabled(false);
                    mTvSave.setAlpha(0.4f);
                } else {
                    mTvSave.setEnabled(true);
                    mTvSave.setAlpha(1.0f);
                }
                mRestart = true;
            }

            public void couldRenderPreview() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mVideoAudioManager.compareTotalTime((float) userLongVideoDuration) <= StaticLayoutUtil.DefaultSpacingadd) {
                            mTvSave.setEnabled(true);
                        }
                    }
                });
                new Thread() {
                    public void run() {
                        super.run();
                        setSpecialFilter();
                    }
                }.start();
            }

            public void finishInitPlayer() {
            }

            public void couldUseRotateButton(boolean couldUse) {
            }
        });
        final ArrayList<LongVideosModel> longVideoModels = intent.getParcelableArrayListExtra("LongVideoPaht");
        new Thread() {
            public void run() {
                super.run();
                loadVideoFrame(intent, longVideoModels);
            }
        }.start();
    }

    private void getVideoModelFilterPreview() {
        //EGL10Helper.withContext("getVideoModelFilterPreview", 27.lambdaFactory$(this));
        EGL10Helper.withContext("getVideoModelFilterPreview", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$getVideoModelFilterPreview$25(eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$getVideoModelFilterPreview$25(EGL10Helper egl) {
        LongVideosModel firstModel = this.mVideoAudioManager.getVideosModelList().get(0);
        Playlist pl = new Playlist();
        Playlist.FileMedia media = Playlist.FileMedia.create(firstModel);
        if (media != null) {
            int previewFilterWidth;
            int previewFilterHeight;
            pl.add(media, 0);
            int titleHeight = DensityUtil.dip2px(63.0f);
            int bottomHeight = DensityUtil.dip2px(265.0f);
            int[] previewWidthHeight = FilterViewUtils.getPreviewWidthHeight(this.metricsWidth, this.metricsHeight - (titleHeight + bottomHeight), this.mVideoWidth, this.mVideoHeight);
            int viewWidth = previewWidthHeight[0];
            int viewHeight = previewWidthHeight[1];
            HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> hashMap = new HashMap();
            int size = this.mVideoAudioManager.getVideosModelList().size();
            GPUImageFilter publicFilter = new GPUImageFilter();
            HashMap<Integer, GPUImageFilter> publicArray = new HashMap();
            for (int i = 0; i < size; i++) {
                publicArray.put(Integer.valueOf(i), publicFilter);
                hashMap.put(publicArray, new HashMap());
            }
            this.forFilterBitmap = this.mSurfaceView.getLongVideoPreview(hashMap, viewWidth, viewHeight, pl);
            int shortLength = DensityUtil.dip2px(44.0f);
            if (this.mVideoHeight > this.mVideoWidth) {
                previewFilterWidth = shortLength;
                previewFilterHeight = (int) ((((float) (this.mVideoHeight * shortLength)) * 1.0f) / ((float) this.mVideoWidth));
            } else {
                previewFilterHeight = shortLength;
                previewFilterWidth = (int) ((((float) (this.mVideoWidth * shortLength)) * 1.0f) / ((float) this.mVideoHeight));
            }
            this.forFilterBitmap = Bitmap.createScaledBitmap(this.forFilterBitmap, previewFilterWidth, previewFilterHeight, false);
            BitmapUtils.saveBitmap2PrivateFile(this.forFilterBitmap);
            generatePreviewBitmaps(this.forFilterBitmap, 0, this.mFilterInfos.size(), 0);
            if (this.forFilterBitmap != null && !this.forFilterBitmap.isRecycled()) {
                this.forFilterBitmap.recycle();
                this.forFilterBitmap = null;
            }
        }
    }

    private void removeInvalidData(List<LongVideosModel> longVideosModels) {
        VideoModelHelper.removeInvalidVideoModel(longVideosModels);
    }

    private void removeInvalidAudioData(List<LongVideosModel> audioLongVideosModels, float videoSumTime) {
        VideoModelHelper.removeInvalidAudioModel(audioLongVideosModels, videoSumTime);
    }

    private void removeInvalidTextData(List<LongVideosModel> textModels) {
        VideoModelHelper.removeInvalidTextModel(textModels);
    }

    private void loadVideoFrame(Intent intent, ArrayList<LongVideosModel> longVideoModels) {
        loadFilterInfos(true, true, false);
        LongVideosModel model;
        if (3 == this.mDataFrom) {
            if (this.mFilterModel != null) {
                this.mVideoAudioManager.setAllVideoMute(this.mDraftLongVideoBean.isAllVideoMute());
                this.mVideoAudioManager.setAllAudioMute(this.mDraftLongVideoBean.isAllAudioMute());
                this.mVideoAudioManager.setMusicModelList(this.mDraftLongVideoBean.getMusicModels());
                this.mVideoAudioManager.setUndoModels(this.mDraftLongVideoBean.getUndoBean().getUndoModels());
                removeInvalidData(this.mDraftLongVideoBean.getLongVideoModels());
                removeInvalidAudioData(this.mDraftLongVideoBean.getMusicModels(), (float) VideoModelHelper.getVideosSumTime(this.mDraftLongVideoBean.getLongVideoModels()));
                CameraVideoPathModel.getInstance().addVideoPath(this.mDraftLongVideoBean.getLongVideoModels());
                List<LongVideosModel> modelList = this.mDraftLongVideoBean.getLongVideoModels();
                FirstVideoBean bean = this.mDraftLongVideoBean.getFirstVideoBean();
                if (bean != null) {
                    this.mFirstVideoBean = bean.cloneData();
                } else if (modelList.size() > 0) {
                    model = modelList.get(0);
                    this.mFirstVideoBean.setVideoWidth(model.getOriVideoWidth());
                    this.mFirstVideoBean.setVideoHeight(model.getOriVideoHeight());
                    this.mFirstVideoBean.setRotation(model.getVideoRotate());
                }
                VideoModelHelper.refreshVideoFileExists(modelList);
                this.mVideoAudioManager.addVideoModelsForDraft(modelList);
                this.mVideoAudioManager.updateProxyVideoCreationTime();
                this.mVideoAudioManager.judgeVideosNeedProxy();
                this.mVideoAudioManager.getFrameBitmapsFromVideoList();
                removeInvalidTextData(this.mDraftLongVideoBean.getTextModels());
                this.mVideoAudioManager.changeTextModels(this.mDraftLongVideoBean.getTextModels());
            }
        } else if (1 == this.mDataFrom) {
            ArrayList<LongVideosModel> mList = intent.getParcelableArrayListExtra(LONG_VIDEO_MODELS);
            if (mList.size() > 0) {
                model = mList.get(0);
                this.mFirstVideoBean.setRotation(model.getVideoRotate());
                this.mFirstVideoBean.setVideoWidth(model.getOriVideoWidth());
                this.mFirstVideoBean.setVideoHeight(model.getOriVideoHeight());
            }
            this.mVideoAudioManager.addVideoModels(mList);
            setUndoData(2);
            this.mVideoAudioManager.getFrameBitmapsFromVideoList();
        } else if (longVideoModels == null || longVideoModels.size() < 1) {
            getActivity().finish();
            return;
        } else {
            this.mVideoAudioManager.addVideoModelsForCamera(longVideoModels);
            if (this.mVideoAudioManager.getVideosModelList().size() <= 0) {
                getActivity().finish();
                return;
            }
            model = this.mVideoAudioManager.getVideosModelList().get(0);
            this.mFirstVideoBean.setRotation(model.getVideoRotate());
            this.mFirstVideoBean.setVideoWidth(model.getOriVideoWidth());
            this.mFirstVideoBean.setVideoHeight(model.getOriVideoHeight());
            setUndoData(2);
        }
        VideoBitmapsModel.instancesModel().setCurrentWH(FilterViewUtils.getUploadLongVideoWidthHeight(this.mFirstVideoBean.getWidthByRotation(), this.mFirstVideoBean.getHeightByRotation(), true));
        try {
            getVideoModelFilterPreview();
        } catch (Exception e) {
            Log.e(TAG, "loadVideoFrame", e);
            System.gc();
        }
        runOnUiThread(new Runnable() {
            public void run() {
                refreshVideoEditView();
            }
        });
        this.mSurfaceView.setVideoModelList(this.mVideoAudioManager.getVideosModelList(), this.mVideoAudioManager.getMusicModelList(), this.mVideoAudioManager.getMediaMute());
        runOnUiThread(new Runnable() {
            public void run() {
                getFirstPlaylistDuration();
            }
        });
    }

    private boolean getFirstPlaylistDuration() {
        long playlistDuration = this.mVideoAudioManager.getPlaylistDuration();
        if (playlistDuration > ((long) (this.userLongVideoDuration * 1000))) {
            this.title_alert_out_time.setVisibility(View.VISIBLE);
            this.title_preview.setVisibility(View.GONE);
            String format = new DecimalFormat("0.0").format((double) (((((float) playlistDuration) * 1.0f) / 1000.0f) - ((float) this.userLongVideoDuration)));
            this.title_alert_out_time.setTextColor(getResources().getColor(R.color.colorRed));
            this.title_alert_out_time.setText(String.format(getResources().getString(R.string.TEXT_VIDEO_EDIT_MAX_LENGTH_EXCEEDED), format));
            this.mConntNext = true;
            return true;
        }
        this.title_alert_out_time.setVisibility(View.VISIBLE);
        this.title_preview.setVisibility(View.GONE);
        this.title_alert_out_time.setTextColor(getResources().getColor(R.color.colorWhite));
        this.title_alert_out_time.setText(String.format(getResources().getString(R.string.TEXT_VIDEO_EDIT_MAX_LENGTH_NOT_EXCEEDED), "" + this.userLongVideoDuration));
        this.mConntNext = false;
        return false;
    }

    private void judgeNextButtonEnable() {
        boolean playVideoDuration;
        if (this.mSurfaceView.getPlaylistDurationMS() > ((long) (this.userLongVideoDuration * 1000))) {
            playVideoDuration = true;
        } else {
            playVideoDuration = false;
        }
        if (playVideoDuration) {
            this.mTvSave.setEnabled(true);
            this.mTvSave.setAlpha(1.0f);
            return;
        }
        this.mTvSave.setEnabled(false);
        this.mTvSave.setAlpha(0.4f);
    }

    private void dealGetMscvError() {
        runOnUiThread(new Runnable() {
            public void run() {
                MscvModel.getInstance().setMscvRecTagList(new ArrayList());
                EventBus.getDefault().post(new OfficialTagListEvent(-1));
            }
        });
    }

    private void restoreFilters() {
        int i = 8;
//        BuglyLogUtil.writeBuglyLog(TAG);
        if (this.mFilterModel != null && this.mVideoFrames != null) {
            this.orientation = this.mFilterModel.getOrientation();
            this.rotation = this.mFilterModel.getRotation();
            this.slideLevel = this.mFilterModel.getSlideLevel();
            this.unloopEnd = this.mFilterModel.getUnloopEnd();
            this.unloopStart = this.mFilterModel.getUnloopStart();
            if (this.mFilterModel.getLoopStart() < this.mFilterModel.getLoopEnd()) {
                this.loopStart = this.mFilterModel.getLoopStart();
                if (this.mFilterModel.getLoopEnd() != 0) {
                    this.loopEnd = this.mFilterModel.getLoopEnd();
                }
                if (this.loopEnd > this.mVideoFrames.size2() - 1) {
                    this.loopEnd = this.mVideoFrames.size2() - 1;
                }
            }
            if (this.slideLevel > 0 && this.slideLevel + 5 < this.mVideoFrames.size2()) {
                this.loopStart = this.slideLevel;
                this.loopEnd = this.loopStart + 5;
            }
            this.mIsVideoLooped = this.mFilterModel.ismLooped();
            this.mFilterName = this.mFilterModel.getFilterName();
            this.mCurrentFilterInfo = this.mHelper.getFilterInfoByFilterName(this.mFilterInfos, this.mFilterName);
            if (this.mEnableStabilizer) {
                refreshTransform();
                this.mSurfaceView.setEnableStabilizer(true);
            }
            LayoutParams lpUnloop = (LayoutParams) this.filter_unloop_slider_root_rl.getLayoutParams();
            lpUnloop.width = this.UNLOOP_DELTA * ((this.unloopEnd - this.unloopStart) + 1);
            lpUnloop.leftMargin = (this.unloopStart * this.UNLOOP_DELTA) + DensityUtil.dip2px(84.0f);
            LayoutParams layoutParams = (LayoutParams) this.filter_loop_slider_root_rll.getLayoutParams();
            layoutParams.width = (int) ((this.SLIDER_DELTA * ((float) ((this.loopEnd - this.loopStart) + 1))) + 0.5f);
            layoutParams.leftMargin = (int) ((((float) DensityUtil.dip2px(84.0f)) + (((float) this.loopStart) * this.SLIDER_DELTA)) + 0.5f);
            this.filter_loop_slider_root_rll.setLayoutParams(layoutParams);
            this.filter_unloop_slider_root_rl.setLayoutParams(lpUnloop);
            if (this.mFilterModel.getPlayType() != 3333) {
                int i2;
                this.mVideoFrames.setPlayType(this.mIsVideoLooped ? 2222 : 1111);
                android.view.View view = this.filter_loop_slider_root_rl;
                if (this.mIsVideoLooped) {
                    i2 = View.VISIBLE;
                } else {
                    i2 = View.GONE;
                }
                view.setVisibility(i2);
                android.view.View view2 = this.filter_unloop_root_rl;
                if (!this.mIsVideoLooped) {
                    i = 0;
                }
                view2.setVisibility(i);
                if (this.mIsVideoLooped) {
                    slideLoop(this.loopStart, this.loopEnd);
                } else {
                    slideLoop(-1, false);
                }
                this.filter_loop_toggle_img.setImageResource(this.mIsVideoLooped ? R.drawable.icon_20_loop_rebound : R.drawable.icon_20_loop_forward);
                this.filter_loop_toggle_ancrt.setText(this.mIsVideoLooped ? R.string.BUTTON_LOOP_REBOUND : R.string.BUTTON_LOOP_FORWARD);
            } else {
                this.mVideoFrames.setPlayType(3333);
                slideLoop(-2, false);
                this.filter_loop_toggle_img.setImageResource(R.drawable.icon_20_loop_reverse);
                this.filter_loop_toggle_ancrt.setText(R.string.BUTTON_LOOP_REVERSE);
            }
            LogUtil.i("restoreFilters", "mFinalEffectState = " + this.mFinalEffectState + ",orientation = " + this.orientation + ",slideLevel = " + this.slideLevel + ",unloopStart = " + this.unloopStart + ",unloopEnd = " + this.unloopEnd + ",mIsVideoLooped = " + this.mIsVideoLooped + ",mEnableStabilizer = " + this.mEnableStabilizer);
        }
    }

    private void initGestute() {
        this.mGestureDetectorCompat = new GestureDetectorCompat(this, new GestureListener());//new GestureListener(this, null));
        this.mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());//new ScaleListener(this, null));
        this.mMoveGestureDetector = new MoveGestureDetector(this, new MoveListener());//new MoveListener(this, null));
        this.text_draw_rl.setOnTouchListener(this.touch_listener);
        this.changeFilterGesture = new GestureDetector(getActivity(), new OnGestureListener() {
            public boolean onDown(MotionEvent e) {
                filterOnScroll = false;
                return false;
            }

            public void onShowPress(MotionEvent e) {
            }

            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (isPressedVideoArea) {
                    return true;
                }
                mHandler.removeCallbacks(pressVideoRunnable);
                if (e1 == null || e2 == null) {
                    return false;
                }
                boolean isVertical;
                if (Math.abs(e1.getY() - e2.getY()) - Math.abs(e1.getX() - e2.getX()) > StaticLayoutUtil.DefaultSpacingadd) {
                    isVertical = true;
                } else {
                    isVertical = false;
                }
                if (isVertical) {
                    return true;
                }
                //startChangeFilterH(e1, e2);
                return true;
            }

            public void onLongPress(MotionEvent e) {
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });
        this.mLoopGestureDetector = new GestureDetector(this, new LoopListener());//new LoopListener(this, null));
        this.filter_loop_gesture.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (filter_loop_slider_root_rl.getVisibility() != View.VISIBLE) {
                    return false;
                }
                if (event.getAction() == 1) {
                    isPressed = false;
                    mSurfaceView.start();
                    LogUtil.d(FilterActivity.TAG, "refreshTransform : onTouch_UP");
                    //if (va != null && hadStartStablize) {
                    if (hadStartStablize){
                        createTransformMethod();
                    }
                    refreshTransform();
                    saveDraft(mFilterModel, mDraftLongVideoBean, true);
                }
                mLoopGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        this.mUnLoopGestureDetector = new GestureDetector(this, new UnLoopListener());//new UnLoopListener(this, null));
        this.filter_unloop_gesture.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (filter_unloop_root_rl.getVisibility() != View.VISIBLE) {
                    return false;
                }
                if (event.getAction() == 1) {
                    isPressed2 = false;
                    mSurfaceView.start();
                    LogUtil.d(FilterActivity.TAG, "refreshTransform : onTouch_UP");
                    //if (va != null && hadStartStablize) {
                    if (hadStartStablize){
                        createTransformMethod();
                    }
                    refreshTransform();
                    setNormalFilter();
                    saveDraft(mFilterModel, mDraftLongVideoBean, true);
                }
                mUnLoopGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setTextAlign(VTContainerView.AlignType alignType, int verticalPos) {
        if (this.draw_text_view.getText() != null && this.draw_text_view.getText().length() != 0) {
            this.draw_text_view.setTextAlign(alignType, verticalPos);
            iconLocation();
        }
    }

    private void setFontSize(VTContainerView.FontSizeType fontSizeType) {
        if (this.draw_text_view.getText() != null && this.draw_text_view.getText().length() != 0) {
            this.draw_text_view.setFontSizeType(fontSizeType);
            iconLocation();
        }
    }

    private void showMoveIconView() {
        iconLocation();
        openTextFingerViewAnimation();
    }

    private void hideMoveIconView() {
        closeTextFingerViewAnimation();
    }

    private void showScaleIconView() {
        iconLocation();
        openTextFingerViewAnimation();
    }

    private void hideScaleIconView() {
        closeTextFingerViewAnimation();
    }

    private void openTextFingerViewAnimation() {
        if (this.text_finger_iv.getVisibility() != View.VISIBLE && this.draw_text_view.getText() != null && this.draw_text_view.getText().length() != 0) {
            this.text_finger_iv.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            if (this.isTextCenter) {
                this.gold_line_view.setVisibility(View.VISIBLE);
            }
            this.isFingerOpenAnimation = true;
            ViewPropertyAnimator.animate(this.text_finger_iv).alpha(1.0f).setDuration(200).setListener(new AnimatorEndListener() {
                public void onAnimationStart(Animator animation) {
                    text_finger_iv.setVisibility(View.VISIBLE);
                    ViewPropertyAnimator.animate(draw_text_view).alpha(0.7f).setDuration(200).start();
                }
            }).start();
        }
    }

    private void closeTextFingerViewAnimation() {
        if (this.isFingerOpenAnimation) {
            this.isFingerOpenAnimation = false;
            this.text_finger_iv.setAlpha(1.0f);
            this.gold_line_view.setVisibility(View.GONE);
            ViewPropertyAnimator.animate(this.text_finger_iv).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(200).setListener(new AnimatorEndListener() {
                public void onAnimationStart(Animator animation) {
                    ViewPropertyAnimator.animate(draw_text_view).alpha(1.0f).setDuration(200).start();
                }

                public void onAnimationEnd(Animator animation) {
                    text_finger_iv.setVisibility(View.GONE);
                }
            }).start();
        }
    }

    private void iconLocation() {
        //this.draw_text_view.geticonLocations(28.lambdaFactory$(this));
        draw_text_view.geticonLocations(new VTContainerView.OnFingerIconLocationListener() {
            @Override
            public void onLocation(int i, int i2, boolean z) {
                lambda$iconLocation$26(i,i2,z);
            }
        });
    }

    private /* synthetic */ void lambda$iconLocation$26(int x, int y, boolean isCenter) {
        this.isTextCenter = isCenter;
        x -= DensityUtil.dip2px(25.0f) / 2;
        y -= DensityUtil.dip2px(25.0f) / 2;
        LayoutParams layoutParams = (LayoutParams) this.text_finger_iv.getLayoutParams();
        layoutParams.leftMargin = this.drawTextViewOffsetX + x;
        layoutParams.topMargin = y;
        LogUtil.d("layout", "x:" + x + ", y:" + y);
        this.text_finger_iv.setLayoutParams(layoutParams);
        if (isCenter && this.text_finger_iv.getVisibility() == View.VISIBLE) {
            this.gold_line_view.setVisibility(View.VISIBLE);
        } else {
            this.gold_line_view.setVisibility(View.GONE);
        }
    }

    private void toTextInputActivity(String text) {
//        this.isIntentText = true;
//        Intent intent = new Intent(getActivity(), TextInputActivity.class);
//        intent.putExtra(TextInputActivity.InputTextIntent, text);
//        intent.putExtra(TextInputActivity.IsEnterIntent, false);
//        intent.putExtra(TextInputActivity.Source, FILTER_SOURCE);
//        startActivity(intent);
//        overridePendingTransition(2131034134, 2131034134);
//        getVTFontInfos(false, "");
        Toast.makeText(this,"go to TextInputActivity",Toast.LENGTH_LONG).show();
    }

    private void getVTFontInfos(final boolean restoreFilters, final String fontName) {
//        if (this.mZHConverter == null) {
//            this.mZHConverter = ZHConverter.getInstance(0);
//        }
        if (this.mVTFontDesBeanList == null) {
            this.mVTFontDesBeanList = new ArrayList();
        }
        if (this.mVTFontDesBeanList.size() <= 0) {
            PriorityThreadPoolManager.execute(new PriorityRunnable(6) {
                public void run() {
                    requestVTFontInfos(restoreFilters, fontName);
                }
            });
        }
    }

    private void requestVTFontInfos(final boolean restoreFilters, final String fontName) {
//        UserController.getVTFontInfos(new IControllerCallback<List<VTFontDesBean>>() {
//            public void success(final List<VTFontDesBean> vtFontDesBeanList, String jsonContent, long cursorId, boolean isAllDone) {
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        if (vtFontDesBeanList != null) {
//                            mVTFontDesBeanList.clear();
//                            mVTFontDesBeanList.addAll(vtFontDesBeanList);
//                            if (!restoreFilters) {
//                                int size = vtFontDesBeanList.size();
//                                for (int i = 0; i < size; i++) {
//                                    VTFontDesBean vtFontDesBean = (VTFontDesBean) vtFontDesBeanList.get(i);
//                                    if (vtFontDesBean.selected) {
//                                        draw_text_view.setTypeface(vtFontDesBean);
//                                        mIsTraditional = TextUtils.equals(vtFontDesBean.cnonly, Constants.IsTraditionalFont);
//                                        setTraditionalFont();
//                                        return;
//                                    }
//                                }
//                            } else if (mVTFontDesBeanList != null && mVTFontDesBeanList.size() > 0) {
//                                Iterator it = mVTFontDesBeanList.iterator();
//                                while (it.hasNext()) {
//                                    ((VTFontDesBean) it.next()).selected = false;
//                                }
//                                it = mVTFontDesBeanList.iterator();
//                                while (it.hasNext()) {
//                                    VTFontDesBean bean = (VTFontDesBean) it.next();
//                                    if (fontName.equals(bean.name)) {
//                                        draw_text_view.setTypeface(bean);
//                                        bean.selected = true;
//                                        return;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//
//            public void error(ErrorBean error) {
//                super.error(error);
//            }
//        });
        runOnUiThread(() -> Toast.makeText(getActivity(),"requestVTFontInfos",Toast.LENGTH_LONG).show());
    }

    public void onEventMainThread(TextInputEvent event) {
        if (FILTER_SOURCE.equals(event.getSource())) {
            dealTextInputEvent(event);
        }
    }

    private void dealTextInputEvent(TextInputEvent event) {
        this.mTextContent = event.getTextContent();
        if (TextUtil.isValidate(this.mTextContent)) {
            this.text_draw_rl.setVisibility(View.VISIBLE);
            setTraditionalFont();
            if (this.draw_text_view.hasText()) {
                animShowAddTag(false);
            } else {
                animHideAddTag();
            }
            this.gold_line_view.setVisibility(View.GONE);
            showAddTextEditLayout();
            this.filter_addition_parent.post(new Runnable() {
                public void run() {
                    int measuredWidth = filter_addition_parent.getMeasuredWidth();
                    LogUtil.d("filter_addition_parent", String.format("measuredWidth : %s , metricsWidth : %s ", new Object[]{Integer.valueOf(measuredWidth), Integer.valueOf(metricsWidth)}));
                    if (measuredWidth < metricsWidth) {
                        filter_addition_parent.setPadding(0, 0, 0, 0);
                        return;
                    }
                    filter_addition_parent.setPadding(0, 0, 0, 0);
                    filter_addition_root_hsv.scrollBy((measuredWidth - metricsWidth) / 2, 0);
                }
            });
            return;
        }
        this.isAfterInsertBlackAddText = false;
        this.text_draw_rl.setVisibility(View.GONE);
    }

    private void animHideAddTag() {
    }

    private void animShowAddTag(boolean restoreFilters) {
    }

    private void setTraditionalFont() {
        String convertText = this.mTextContent;
        if (this.mIsTraditional) {
            convertText = this.mZHConverter.convert(convertText);
        }
        if (this.mIsCapitalized) {
            convertText = convertText.toUpperCase();
        }
        this.draw_text_view.setText(convertText);
    }

    private void correctionFilterPosition() {
        if (this.filterChoosePosition < this.filterAdapter.getNoneFilterIndex()) {
            this.filterChoosePosition = this.mFilterInfos.size() - 1;
        } else if (this.filterChoosePosition > this.mFilterInfos.size() - 1) {
            this.filterChoosePosition = this.filterAdapter.getNoneFilterIndex();
        }
    }

    private boolean startChangeFilterH(MotionEvent e1, MotionEvent e2) {
        if (Math.abs(e2.getX() - e1.getX()) < 20.0f) {
            return false;
        }
        if (this.filterOnScroll) {
            return true;
        }
        this.filterOnScroll = true;
        if (e2.getX() - e1.getX() > StaticLayoutUtil.DefaultSpacingadd) {
            this.filterChoosePosition--;
            correctionFilterPosition();
            if (((FilterInfo) this.mFilterInfos.get(this.filterChoosePosition)).isGroup) {
                this.filterChoosePosition--;
                correctionFilterPosition();
            }
            this.leftSlide = false;
            LogUtil.d("huangweijie", "filterPosition : " + this.filterChoosePosition);
            this.filter_list_recyclerview.scrollToPosition(this.filterChoosePosition);
            this.filter_list_recyclerview.post(new Runnable() {
                public void run() {
                    if (mFilterListLayoutManager.findViewByPosition(filterChoosePosition) != null) {
                        filter_list_recyclerview.smoothScrollBy(mFilterListLayoutManager.findViewByPosition(filterChoosePosition).getLeft() - ((int) (((float) (metricsWidth - mFilterListLayoutManager.findViewByPosition(filterChoosePosition).getWidth())) / 2.0f)), 0);
                    }
                }
            });
        } else if (e2.getX() - e1.getX() < StaticLayoutUtil.DefaultSpacingadd) {
            this.filterChoosePosition++;
            correctionFilterPosition();
            if (((FilterInfo) this.mFilterInfos.get(this.filterChoosePosition)).isGroup) {
                this.filterChoosePosition++;
                correctionFilterPosition();
            }
            this.leftSlide = true;
            this.filter_list_recyclerview.scrollToPosition(this.filterChoosePosition);
            this.filter_list_recyclerview.post(new Runnable() {
                public void run() {
                    if (mFilterListLayoutManager.findViewByPosition(filterChoosePosition) != null) {
                        filter_list_recyclerview.smoothScrollBy(mFilterListLayoutManager.findViewByPosition(filterChoosePosition).getLeft() - ((int) (((float) (metricsWidth - mFilterListLayoutManager.findViewByPosition(filterChoosePosition).getWidth())) / 2.0f)), 0);
                    }
                }
            });
        } else {
            this.filterIsChanging = false;
            this.filterIsChanging = false;
            this.filterAdapter.setFilterChangeState(this.filterIsChanging);
            return true;
        }
        FilterInfo info = (FilterInfo) this.mFilterInfos.get(this.filterChoosePosition);
        this.mFilterName = info.name_en;
        setFilterNameForLongVideosModel();
        if (this.text_draw_rl.getVisibility() != View.VISIBLE) {
            showFilterNameInPreview(this.filterChoosePosition);
        }
        this.currentFilterName = this.mFilterName;
        setFilterGroupNameState(this.filterChoosePosition);
        this.filterAdapter.setCurrentPosition(this.filterChoosePosition);
        this.filterAdapter.setFilterName(this.mFilterName, info);
        this.filterAdapter.notifyDataSetChanged();
        if (!FilterViewUtils.isVideoModel(this.mDataType)) {
            setSelectedFilter(this.mFinalEffectState);
        } else if (this.mVideoAudioManager != null) {
            ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
            if (TextUtil.isValidate(videosModelList) && getCurrentVideoModel() != null) {
                setVideoAnimationFilter(this.mFinalEffectState, videosModelList, videosModelList.indexOf(getCurrentVideoModel()));
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                granted = false;
            }
            if (granted) {
                savePictureOriginal();
            }
        } else if (requestCode == 106) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                granted = false;
            }
//            if (this.mVideoMusicDialog == null) {
//                return;
//            }
//            if (granted) {
//                this.mVideoMusicDialog.doLoadLocalData();
//            } else {
//                this.mVideoMusicDialog.access$lambda$2();
//            }
        }
    }

    private void initFilters() {
        new Thread() {
            public void run() {
                super.run();
                exposureFilter = getToneCurveFilter();
                contrastFilter = getToneCurveFilter();
                saturationFilter = getSaturationFilter();
                whiteBalanceFilter = getWhiteBalanceFilter();
                tiltFilter = getGaussianFilter();
                mBilateralFilter = getBilateralFilter();
                grainFilter = getGrainFilter();
                movieFilter = getMoviewFilter2();
                if (FilterViewUtils.isPictureModel(mDataType)) {
                    initScreenFilter();
                }
                fadeFilter = getToneCurveFilter();
                shadowFilter = getToneCurveFilter();
                highLightFilter = getToneCurveFilter();
                skyFilter = getSkyFilter();
            }
        }.start();
    }

    private void initScreenFilter() {
        boolean z;
        boolean z2 = true;
        this.leakFilter = getScreenBlendFilter();
        this.leakFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
        this.leakFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
        this.dustFilter = getScreenBlendFilter();
        AFGPUImageScreenBlendFilter aFGPUImageScreenBlendFilter = this.dustFilter;
        int i = this.mCaptureOrientation;
        if (this.mCameraLensType == 1) {
            z = true;
        } else {
            z = false;
        }
        aFGPUImageScreenBlendFilter.setCaptureOrientation(i, z);
        this.dustFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
        this.dateFilter = getDateFilter();
        AFGPUImageDateBlendFilter aFGPUImageDateBlendFilter = this.dateFilter;
        int i2 = this.mCaptureOrientation;
        if (this.mCameraLensType != 1) {
            z2 = false;
        }
        aFGPUImageDateBlendFilter.setCaptureOrientation(i2, z2);
    }

    private void initView() {
        FrameRenderer.RenderMode mode;
        int i;
        this.mTvSave.setText(getResources().getString(R.string.BUTTON_DONE));
        this.text_draw_rl = findViewById(R.id.text_draw_rl);
        this.draw_text_view = findViewById(R.id.draw_text_view);
        this.text_finger_iv = findViewById(R.id.text_finger_iv);
        this.gold_line_view = findViewById(R.id.gold_line_view);
        this.text_draw_rl.setVisibility(View.GONE);
        initFilters();
        this.capture_filter_name_tv.getPaint().setFakeBoldText(true);
        if (this.mCaptureMode == 1 || 2 == this.mDataFrom || this.mIsSquare) {
            mode = FrameRenderer.RenderMode.RENDER_CROP;
        } else {
            mode = FrameRenderer.RenderMode.RENDER_FILL;
        }
        this.mSurfaceView.setCaptureMode(this.mCaptureMode);
        this.mSurfaceView.setFrameRendererMode(mode);
        Log.e("debug","surfaceStartGL started=====================>");
        this.mSurfaceView.startGL("filteractivity");
        Log.e("debug","surfaceStartGL end=====================>");
        this.filterEffectLeakList = new ArrayList();
        this.filterEffectLeakList.add(this.filter_effect_leak1);
        this.filterEffectLeakList.add(this.filter_effect_leak2);
        this.filterEffectLeakList.add(this.filter_effect_leak3);
        this.filterEffectLeakList.add(this.filter_effect_leak4);
        this.filterEffectLeakList.add(this.filter_effect_leak5);
        TintColorUtil.tintDrawable(getActivity(), (ImageView) this.mIvBack, R.color.colorWhite);
        TintColorUtil.tintDrawable(getActivity(), this.filter_tag_filter_iv, R.color.colorWhite);
        TintColorUtil.tintDrawable(getActivity(), this.filter_tag_effect_iv, R.color.colorWhite);
        TintColorUtil.tintDrawable(getActivity(), this.filter_tag_loop_iv, R.color.colorWhite);
        TintColorUtil.tintDrawable(getActivity(), this.filter_tag_music_iv, R.color.colorWhite);
        this.filter_tag_filter_iv.setAlpha(isVideoType() ? 0.4f : 1.0f);
        this.filter_tag_effect_iv.setAlpha(0.4f);
        this.filter_tag_loop_iv.setAlpha(isVideoType() ? 1.0f : 0.4f);
        this.filter_tag_music_iv.setAlpha(0.4f);
        this.filter_tag_filter_rl.setOnTouchListener(new FilterOnTouchListerner());
        this.filter_tag_effect_rl.setOnTouchListener(new FilterOnTouchListerner());
        this.filter_tag_loop_rl.setOnTouchListener(new FilterOnTouchListerner());
        this.filter_tag_music_rl.setOnTouchListener(new FilterOnTouchListerner());
        this.mIvBack.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.mTvSave.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.mTvSave.setEnabled(false);
        this.mTvSave.setAlpha(0.4f);
        this.filter_loop_toggle_ll.setOnClickListener(this);
        this.filter_loop_toggle_ll.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        if (this.mIsShort || 1 == this.mDataType) {
            this.filter_tag_filter_rl.getLayoutParams().width = this.metricsWidth / 2;
            this.filter_tag_effect_rl.getLayoutParams().width = this.metricsWidth / 2;
            this.filter_tag_loop_rl.getLayoutParams().width = 0;
            this.filter_tag_music_rl.getLayoutParams().width = 0;
        } else if (isVideoType()) {
            ((LinearLayout) this.filter_tab_ll).removeAllViews();
            ((LinearLayout) this.filter_tab_ll).addView(this.filter_tag_loop_rl);
            ((LinearLayout) this.filter_tab_ll).addView(this.filter_tag_music_rl);
            ((LinearLayout) this.filter_tab_ll).addView(this.filter_tag_filter_rl);
            ((LinearLayout) this.filter_tab_ll).addView(this.filter_tag_effect_rl);
            this.filter_tag_filter_rl.getLayoutParams().width = this.metricsWidth / 4;
            this.filter_tag_effect_rl.getLayoutParams().width = this.metricsWidth / 4;
            this.filter_tag_loop_rl.getLayoutParams().width = this.metricsWidth / 4;
            this.filter_tag_music_rl.getLayoutParams().width = this.metricsWidth / 4;
            this.filter_filter_root_rl.setVisibility(View.GONE);
        } else {
            this.filter_tag_filter_rl.getLayoutParams().width = this.metricsWidth / 3;
            this.filter_tag_effect_rl.getLayoutParams().width = this.metricsWidth / 3;
            this.filter_tag_loop_rl.getLayoutParams().width = this.metricsWidth / 3;
            this.filter_tag_music_rl.getLayoutParams().width = 0;
        }
        this.below_surface.setOnClickListener(this);
        this.stack_frame_switch_rl.setOnClickListener(this);
        this.filter_select_mode_current_rl.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.filter_select_mode_global_rl.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.filter_select_mode_current_rl.setOnClickListener(this);
        this.filter_select_mode_global_rl.setOnClickListener(this);
        this.stack_frame_switch_rl.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.filter_unloop_slider_root_rl.getLayoutParams().width = this.metricsWidth - DensityUtil.dip2px(148.0f);
        this.mFilterInfos = new ArrayList();
        this.mFilterGroupNames = new ArrayList();
        FilterAdapter.GROUP_COLLECT_POSITION = -1;
        FilterInfo downloads = new FilterInfo("下载", "下载", DOWNLOAD_FILTER, new GPUImageFilter(), false);
        App.mFilterMap.put(DOWNLOAD_FILTER, downloads);
        this.mFilterInfos.add(downloads);
        setPreviewParent();
        FilterInfo noneFilter = new FilterInfo("无", "無", NONE_FILTER, new GPUImageFilter(), false);
        this.mFilterInfos.add(noneFilter);
        this.mFilterGroupNames.add(new FilterInfo("无", "無", getString(R.string.LABEL_NO_FILTER), new GPUImageFilter(), false));
        this.mFilterGroupNames.addAll(App.jsonGroupInfoList2);
        App.mFilterMap.put(NONE_FILTER, noneFilter);
        if (App.mFilterInfos != null) {
            this.mFilterInfos.addAll(App.mFilterInfos);
            String collectString = SharedPrefUtils.getString(Constants.COLLECT_FILTER + GlobalHelper.getUserId(), "");
            if (!("".equals(collectString) || collectString.length() == 0)) {
                FilterInfo filterInfo = new FilterInfo(FilterAdapter.GROUP_COLLECT);
                filterInfo.name_chs = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo.name_cht = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo.filter_id = -10001;
                filterInfo.name_en = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo.filename = FilterAdapter.GROUP_COLLECT;
                this.mFilterInfos.add(this.filterAdapter.getNoneFilterIndex() + 1, filterInfo);
                this.mFilterGroupNames.add(1, filterInfo);
                long l = System.currentTimeMillis();
                List<FilterInfo> collectFilterInfos = JsonParserUtil.parseList(collectString, new TypeToken<ArrayList<FilterInfo>>() {
                }.getType(), null);
                List<FilterInfo> removeList = new ArrayList();
                if (collectFilterInfos != null && collectFilterInfos.size() > 0) {
                    for (i = 0; i < collectFilterInfos.size(); i++) {
                        FilterInfo filterInfo2 = collectFilterInfos.get(i);
                        if (App.mFilterMap.get(filterInfo2.name_en) == null) {
                            removeList.add(filterInfo2);
                        } else {
                            for (int j = 0; j < this.mFilterInfos.size(); j++) {
                                if (this.mFilterInfos.get(j).name_en.equals(filterInfo2.name_en)) {
                                    filterInfo2.filter = this.mFilterInfos.get(j).filter;
                                    this.mFilterInfos.get(j).isCollected = true;
                                    break;
                                }
                            }
                        }
                    }
                    collectFilterInfos.removeAll(removeList);
                    if (collectFilterInfos.size() == 0) {
                        if (this.mFilterInfos.size() > this.filterAdapter.getNoneFilterIndex() + 1) {
                            this.mFilterInfos.remove(this.filterAdapter.getNoneFilterIndex() + 1);
                        }
                        this.mFilterGroupNames.remove(1);
                    } else {
                        this.collectFilter.addAll(collectFilterInfos);
                        this.mFilterInfos.addAll(this.filterAdapter.getNoneFilterIndex() + 2, collectFilterInfos);
                    }
                    System.currentTimeMillis();
                }
            }
        }
        addOrRemoveSortItem();
        this.filterAdapter.notifyDataSetChanged();
        this.filterGroupNameAdapter.notifyDataSetChanged();
        if (3 == this.mDataFrom) {
            int effectState = this.mFilterModel.getmFinalEffectState();
            if (effectState == 11 || effectState == 12 || effectState == 13 || effectState == 14) {
                this.mFinalEffectState = 0;
            } else {
                this.mFinalEffectState = effectState;
            }
        } else {
            this.mFinalEffectState = this.mFilterModel.getmFinalEffectState();
        }
        this.mFilterName = this.mFilterModel.getFilterName();
        this.mCurrentFilterInfo = this.mHelper.getFilterInfoByFilterName(this.mFilterInfos, this.mFilterName);
        boolean hadName = false;
        i = 0;
        int size = this.filterAdapter.getItemCount();
        while (i < size) {
            if (this.mFilterInfos.get(i).name_en != null && this.mFilterInfos.get(i).name_en.equalsIgnoreCase(this.mFilterName)) {
                hadName = true;
                this.filterChoosePosition = i;
                break;
            }
            i++;
        }
        if (!hadName) {
            this.filterChoosePosition = 1;
        }
        if (this.filterAdapter != null) {
            this.filterAdapter.setFilterName(this.mFilterName, this.mCurrentFilterInfo);
            this.filterAdapter.notifyDataSetChanged();
        }
        if (this.mFilterName == null || this.mFilterName.equals(NONE_FILTER)) {
            this.filterGroupNameAdapter.setSelectPosition(0);
            this.filterGroupNameAdapter.notifyDataSetChanged();
        } else {
            //this.filter_list_recyclerview.post(29.lambdaFactory$(this));
            filter_list_recyclerview.post(new Runnable() {
                @Override
                public void run() {
                    lambda$initView$27();
                }
            });
        }
        try {
            int bitmapWidth;
            int bitmapHeight;
            if (2 == this.mDataFrom) {
                bitmapWidth = 300;
                bitmapHeight = 300;
            } else if (this.mIsSquare) {
                if (this.orientation == 0 || this.orientation == 180) {
                    bitmapWidth = 300;
                    bitmapHeight = (int) ((((float) (this.mVideoHeight * 300)) * 1.0f) / ((float) this.mVideoWidth));
                } else {
                    bitmapHeight = 300;
                    bitmapWidth = (int) ((((float) (this.mVideoWidth * 300)) * 1.0f) / ((float) this.mVideoHeight));
                }
            } else if (this.orientation == 0 || this.orientation == 180) {
                bitmapWidth = 300;
                bitmapHeight = (int) ((((float) (this.mVideoHeight * 300)) * 1.0f) / ((float) this.mVideoWidth));
            } else {
                bitmapHeight = 300;
                bitmapWidth = (int) ((((float) (this.mVideoWidth * 300)) * 1.0f) / ((float) this.mVideoHeight));
            }
            long t = System.nanoTime();
            int faceBitmapWidth = bitmapWidth + (bitmapWidth % 2);
            int faceBitmapHeight = bitmapHeight + (bitmapHeight % 2);
            LogUtil.d("MSCV", "getBitmapForMscv");
            dealGetMscvError();
        } catch (RuntimeException e) {
            LogUtil.d("MSCV", "RuntimeException");
            e.printStackTrace();
        }
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationX", (float) this.metricsWidth, StaticLayoutUtil.DefaultSpacingadd);
        ObjectAnimator.ofPropertyValuesHolder(this.filter_title_bar, translationY).setDuration((long) this.AnimationTime).start();
        if (this.title_preview.getVisibility() == View.VISIBLE) {
            ObjectAnimator.ofPropertyValuesHolder(this.title_preview, translationY).setDuration((long) this.AnimationTime).start();
        }
        if (this.title_alert_out_time.getVisibility() == View.VISIBLE) {
            ObjectAnimator.ofPropertyValuesHolder(this.title_alert_out_time, translationY).setDuration((long) this.AnimationTime).start();
        }
        if (this.title_add_text.getVisibility() == View.VISIBLE) {
            ObjectAnimator.ofPropertyValuesHolder(this.title_add_text, translationY).setDuration((long) this.AnimationTime).start();
        }
        ObjectAnimator.ofPropertyValuesHolder(this.filter_list_recyclerview, translationY).setDuration((long) this.AnimationTime).start();
        this.mTvSave.setOnClickListener(this);
        this.mIvBack.setOnClickListener(this);
        this.filter_tag_filter_rl.setOnClickListener(this);
        this.filter_tag_effect_rl.setOnClickListener(this);
        this.filter_tag_loop_rl.setOnClickListener(this);
        this.filter_tag_music_rl.setOnClickListener(this);
        this.filter_stablize_rl.setOnClickListener(this);
        this.filter_tab_like_filter_ll.setOnClickListener(this);
        this.surface_click_view.setOnClickListener(this);
        this.player_rl.setPlayButtonCallback(new FilterPlayButtonCallback() {
            public void playClick(boolean isPlaying) {
                setVideoStartPause(isPlaying);
            }

            public void leftClick() {
                final boolean isPause = mSurfaceView.getLongVideoPlayState();
                setVideoStartPause(true);
                mVideoEditHelper.getModelCurrentDuration(2);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!isPause) {
                            setVideoStartPause(false);
                        }
                    }
                }, 300);
            }

            public void rightClick() {
                final boolean isPause = mSurfaceView.getLongVideoPlayState();
                setVideoStartPause(true);
                mVideoEditHelper.getModelCurrentDuration(1);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!isPause) {
                            setVideoStartPause(false);
                        }
                    }
                }, 300);
            }
        });
        this.filter_beauty_anim.setOnTouchListener(new OnTouchListener() {
            private long downTime;
            private float downX;
            private float downY;

            public boolean onTouch(android.view.View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (filter_rule_ll != null && filter_rule_ll.getVisibility() == View.VISIBLE) {
                            onConfirmPress(currentEffectType);
                        }
                        if (isVideoType()) {
                            if (isInVideoArea(event)) {
                                this.downX = event.getX();
                                this.downY = event.getY();
                                this.downTime = System.currentTimeMillis();
                            } else {
                                if (mVideoEditHelper != null) {
                                    mVideoEditHelper.clearIfExistSelect();
                                }
                                if (!mSurfaceView.getLongVideoPlayState()) {
                                    callVideoPause();
                                }
                            }
                        }
                        mHandler.postDelayed(pressVideoRunnable, FilterActivity.CHANEG_FILTER_TIME);
                        break;
                    case 1:
                    case 3:
                        if (isVideoType() && System.currentTimeMillis() - this.downTime < FilterActivity.CHANEG_FILTER_TIME && isInVideoArea(event) && Math.abs(event.getX() - this.downX) <= 20.0f && Math.abs(event.getY() - this.downY) <= 20.0f) {
                            if (mSurfaceView.getLongVideoPlayState()) {
                                callVideoPlay();
                            } else {
                                callVideoPause();
                            }
                        }
                        mHandler.removeCallbacks(pressVideoRunnable);
                        if (!FilterActivity.NONE_FILTER.equals(currentFilterName) && isPressedVideoArea) {
                            isPressedVideoArea = false;
                            mFilterName = currentFilterName;
                            currentFilterName = FilterActivity.NONE_FILTER;
                            mFilterEffectManager.returnSelectFilterBeans();
                            if (!isBackOrNextPressed) {
                                setNormalFilter();
                                break;
                            }
                        }
                        break;
                }
                changeFilterGesture.onTouchEvent(event);
                return true;
            }
        });
        this.filter_max_click_view.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                if (mVideoEditHelper != null) {
                    mVideoEditHelper.clearIfExistSelect();
                }
            }
        });
        fixSurfaceViewSize();
        getPromoteTags();
        MscvModel.getInstance().getRecommendCategories(getVType());
    }

    private /* synthetic */ void lambda$initView$27() {
        if (this.mFilterListLayoutManager != null) {
            int historyPos = 1;
            int i = 0;
            while (i < this.mFilterInfos.size()) {
                if (this.mFilterInfos.get(i).name_en != null && this.mFilterInfos.get(i).name_en.equals(this.mFilterName)) {
                    historyPos = i;
                    break;
                }
                i++;
            }
            setFilterGroupNameState(historyPos);
            historyPos = (int) (((float) ((this.filter_list_recyclerview.getChildCount() / 2) + historyPos)) + 0.5f);
            if (historyPos > this.mFilterInfos.size() - 1) {
                historyPos = this.mFilterInfos.size() - 1;
            }
            if (historyPos < 3) {
                historyPos = 3;
            }
            this.mFilterListLayoutManager.scrollToPosition(historyPos);
        }
    }

    private int getVType() {
        if (isVideoType()) {
            return 2;
        }
        if (isGifType()) {
            return 0;
        }
        if (isPictureType()) {
            return 1;
        }
        return 2;
    }

    private boolean isInVideoArea(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        float x = event.getX();
        float y = event.getY();
        int width = this.filter_beauty_anim.getWidth();
        int startX = (this.metricsWidth - this.leakFilterReqWidth) / 2;
        int endX = startX + this.leakFilterReqWidth;
        int startY = (this.filter_beauty_anim.getHeight() - this.leakFilterReqHeight) / 2;
        int endY = startY + this.leakFilterReqHeight;
        boolean result = false;
        if (x > ((float) startX) && x < ((float) endX) && y > ((float) startY) && y < ((float) endY)) {
            result = true;
        }
        //LogUtil.d(TAG, "isInVideoArea  rawX : " + rawX + " , rawY : " + rawY + " , x : " + x + " , y : " + y + " , leakFilterReqWidth : " + this.leakFilterReqWidth + " , leakFilterReqHeight : " + this.leakFilterReqHeight + " , result : " + result);
        //LogUtil.d(TAG, String.format("isInVideoArea width : %s , height : %s , startX : %s , endX : %s , startY : %s , endY : %s", width, Integer.valueOf(height), startX, endX, startY, endY));
        return result;
    }

    private boolean isVideoType() {
        return 2 == this.mDataType;
    }

    private boolean isGifType() {
        return this.mDataType == 0 || 3 == this.mDataType;
    }

    private boolean isPictureType() {
        return 1 == this.mDataType;
    }

    private void getPromoteTags() {
        MSCVController.getPromoteTags(new IControllerCallback<List<OfficialTagBean>>() {
            public void success(final List<OfficialTagBean> officialTagBeen, String jsonContent, long cursorId, boolean isAllDone) {
                super.success(officialTagBeen, jsonContent, cursorId, isAllDone);
                runOnUiThread(new Runnable() {
                    public void run() {
                        MscvModel.getInstance().setPromoteTagsList(officialTagBeen);
                    }
                });
            }

            public void error(ErrorBean error) {
                super.error(error);
            }

            public void failure(VolleyError error) {
                super.failure(error);
            }
        });
    }

    private Bitmap getRatioBitmap(Bitmap bitmap) {
        int width = this.mVideoWidth;
        int height = this.mVideoHeight;
        if (this.mIsSquare) {
            height = Math.min(width, height);
            width = height;
        } else if (((double) ((((float) height) * 1.0f) / ((float) width))) > 1.2d) {
        }
        if (2 == this.mDataFrom) {
            return BitmapUtils.ImageCrop(bitmap, true);
        }
        int y;
        int x;
        int finalWidth;
        int finalHeight;
        if (this.mVideoHeight > this.mVideoWidth) {
            float newHeight = (float) ((bitmap.getWidth() * height) / width);
            y = Math.max(0, (int) ((((float) (bitmap.getHeight() / 2)) + ((this.mOffsetY * newHeight) / 2.0f)) - (newHeight / 2.0f)));
            x = Math.max(0, 0);
            finalWidth = bitmap.getWidth();
            finalHeight = (int) Math.min((float) bitmap.getHeight(), newHeight);
            if (x + finalWidth > bitmap.getWidth()) {
                x = bitmap.getWidth() - finalWidth;
                if (x < 0) {
                    x = 0;
                    finalWidth = bitmap.getWidth();
                }
            }
            if (y + finalHeight > bitmap.getHeight()) {
                y = bitmap.getHeight() - finalHeight;
                if (y < 0) {
                    y = 0;
                    finalHeight = bitmap.getHeight();
                }
            }
            return BitmapUtils.createBitmap(bitmap, x, y, finalWidth, finalHeight, null, false);
        }
        float newWidth = (float) ((bitmap.getHeight() * width) / height);
        x = (int) ((((float) (bitmap.getWidth() / 2)) - ((this.mOffsetX * newWidth) / 2.0f)) - (newWidth / 2.0f));
        y = Math.max(0, 0);
        x = Math.max(0, x);
        finalWidth = (int) Math.min((float) bitmap.getWidth(), newWidth);
        finalHeight = bitmap.getHeight();
        if (x + finalWidth > bitmap.getWidth()) {
            x = bitmap.getWidth() - finalWidth;
            if (x < 0) {
                x = 0;
                finalWidth = bitmap.getWidth();
            }
        }
        if (y + finalHeight > bitmap.getHeight()) {
            y = bitmap.getHeight() - finalHeight;
            if (y < 0) {
                y = 0;
                finalHeight = bitmap.getHeight();
            }
        }
        return BitmapUtils.createBitmap(bitmap, x, y, finalWidth, finalHeight, null, false);
    }

    private void clickAddition(android.view.View v) {
        switch (v.getId()) {
            case R.id.filter_tab_choose_cancle:
                this.filter_tab_choose_cancle.setEnabled(false);
                if (this.filter_effect_root_ll.getVisibility() == View.VISIBLE) {
                    this.filter_tab_ll.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
                    this.filter_tab_ll.setVisibility(View.VISIBLE);
                    startAnimation(this.filter_tab_ll, 1.0f, 200, 0);
                    startAnimation(this.filter_tab_choose_ll, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
                    this.mFilterEffectManager.masterCancelFilterEffect();
                    return;
                } else if (this.mOnVideoTextSelectListener == null) {
                    return;
                } else {
                    if (isVideoType() && this.currentEffectType == FilterEffectManager.EffectType.CROP) {
                        this.mOnVideoTextSelectListener.videoCropCancel(this.currentEffectType);
                        return;
                    } else {
                        this.mOnVideoTextSelectListener.cancel(this.currentEffectType);
                        return;
                    }
                }
            case R.id.filter_tab_choose_confirm:
                this.filter_tab_choose_confirm.setEnabled(false);
                if (this.mOnVideoTextSelectListener == null) {
                    return;
                }
                if (isVideoType() && this.currentEffectType == FilterEffectManager.EffectType.CROP) {
                    this.mOnVideoTextSelectListener.videoCropConfirm(this.currentEffectType);
                    return;
                } else {
                    this.mOnVideoTextSelectListener.confirm(this.currentEffectType);
                    return;
                }
            default:
                return;
        }
    }

    private void initData() {
        this.SLIDER_BORDER_WIDTH = DensityUtil.dip2px(3.0f);
        this.SLIDER_LINE_WIDTH = DensityUtil.dip2px(2.0f);
        this.SLIDER_MIN_LEFT = DensityUtil.dip2px(84.0f);
        this.SLIDER_MAX_LEFT = DensityUtil.dip2px(84.0f);
        this.unloopStart = 0;
        if (this.mDataType != 2) {
            this.mMaxSlideLevel = this.mVideoFrames.size() - 6;
            this.mMaxUnloopLevel = this.mVideoFrames.size() - this.minUnLoopCount;
            this.unloopEnd = this.mVideoFrames.size() - 1;
            this.UNLOOP_DELTA = (int) ((((((float) (this.metricsWidth - DensityUtil.dip2px(168.0f))) * 1.0f) * ((float) (this.mVideoFrames.size() - this.mMaxUnloopLevel))) / ((float) this.mVideoFrames.size())) / ((float) (this.mVideoFrames.size() - this.mMaxUnloopLevel)));
            this.SLIDER_DELTA = (((float) this.metricsWidth) - (((float) DensityUtil.dip2px(84.0f)) * 2.0f)) / ((float) this.mVideoFrames.size2());
        }
        this.filter_loop_slider_root_rll.getLayoutParams().width = (int) ((((float) this.minLoopCount) * this.SLIDER_DELTA) + 0.5f);
        this.TAB_HEIGHT_1_4 = DensityUtil.dip2px(12.5f);
        if (this.mIsShort || 1 == this.mDataType) {
            this.filter_tag_loop_iv.setVisibility(View.INVISIBLE);
        }
    }

    private void fixSurfaceViewSize(float scale, float xPercent, float yPercent, boolean setFrameCrop) {
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        float titleBarHeight = getResources().getDimension(R.dimen._63dp);
        if (scale > usableWidth / usableHeight) {
            int realHeight = (int) (((float) ((int) usableWidth)) / scale);
        } else {
            int i = (int) (((float) ((int) usableHeight)) * scale);
        }
        if (setFrameCrop) {
            int width1 = this.mVideoWidth;
            int height1 = this.mVideoHeight;
            int perWidth;
            int perHeight;
            int temp;
            if (this.currentDegree == StaticLayoutUtil.DefaultSpacingadd || this.currentDegree == 180.0f) {
                perWidth = (int) (((float) width1) * xPercent);
                perHeight = (int) (((float) height1) * yPercent);
                if (this.mCaptureOrientation == 0 || this.mCaptureOrientation == 180) {
                    temp = perWidth;
                    perWidth = perHeight;
                    perHeight = temp;
                }
                this.mVideoFrames.mCropWidth = perWidth;
                this.mVideoFrames.mCropHeight = perHeight;
            } else {
                perWidth = (int) (((float) height1) * xPercent);
                perHeight = (int) (((float) width1) * yPercent);
                if (this.mCaptureOrientation == 0 || this.mCaptureOrientation == 180) {
                    temp = perWidth;
                    perWidth = perHeight;
                    perHeight = temp;
                }
                this.mVideoFrames.mCropWidth = perWidth;
                this.mVideoFrames.mCropHeight = perHeight;
            }
        }
        LogUtil.d("GLRenderView", " mSurfaceView.setLayoutParams ");
    }

    private void fixGridViewSize(float degree) {
        fixGridViewSize(degree, true);
    }

    private void fixGridViewSize(float degree, boolean needCheckCrop) {
        boolean containItem = this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.CROP);
        if (!containItem || !needCheckCrop) {
            if (!containItem) {
                baseFixGridView(degree);
            } else if (this.filter_crop_ccav != null) {
                resetFilterCropCcavViewWidthHeight();
            }
        }
    }

    private void resetFilterCropCcavViewWidthHeight() {
        int realWidth;
        int realHeight;
        LayoutParams layoutParams = (LayoutParams) this.filter_crop_ccav.getLayoutParams();
        float gridScale = (((float) layoutParams.height) * 1.0f) / ((float) layoutParams.width);
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        int marginL = 0;
        int marginT = 0;
        int marginR = 0;
        int marginB = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
            marginT = marginB;
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginR = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
            marginL = marginR;
        }
        realWidth += realWidth % 2;
        realHeight += realHeight % 2;
        layoutParams.width = realWidth;
        layoutParams.height = realHeight;
        this.animationWidth = realWidth;
        this.animationHeight = realHeight;
        layoutParams.setMargins(marginL, marginT, marginR, marginB);
        if (this.filter_crop_ccav != null) {
            this.filter_crop_ccav.setLayoutParams(layoutParams);
        }
        if (this.mirror_seek_bar != null) {
            LayoutParams mirrorLayout = (LayoutParams) this.mirror_seek_bar.getLayoutParams();
            mirrorLayout.width = realWidth;
            mirrorLayout.height = realHeight;
            mirrorLayout.setMargins(marginL, marginT, marginR, marginB);
            this.mirror_seek_bar.setLayoutParams(mirrorLayout);
        }
    }

    private void initPlayBtn(int videoWidth, int videoHeight) {
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            int leftMargin;
            int rightMargin;
            LayoutParams layoutParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
            FilterPlayButtonBean bean = new FilterPlayButtonBean();
            bean.mScreenWidth = this.metricsWidth;
            bean.mScreenHeight = this.metricsHeight;
            bean.mSurfaceWidth = layoutParams.width;
            bean.mSurfaceHeight = layoutParams.height;
            bean.mSurfaceTop = layoutParams.topMargin;
            bean.mVideoWidth = videoWidth;
            bean.mVideoHeight = videoHeight;
            int[] ints = FilterViewUtils.computePlayButtonLayout(bean);
            LayoutParams buttonParams = (LayoutParams) this.player_rl.getLayoutParams();
            buttonParams.width = ints[0];
            buttonParams.topMargin = ints[1];
            if (buttonParams.width == this.metricsWidth) {
                leftMargin = DensityUtil.dip2px(20.0f);
                rightMargin = DensityUtil.dip2px(20.0f);
            } else {
                leftMargin = 0;
                rightMargin = 0;
            }
            buttonParams.leftMargin = leftMargin;
            buttonParams.rightMargin = rightMargin;
            this.player_rl.setLayoutParams(buttonParams);
            this.player_rl.setVisibility(View.VISIBLE);
        }
    }

    private void initCurrentClipIv(int videoWidth, int videoHeight) {
        LayoutParams layoutParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
        FilterPlayButtonBean bean = new FilterPlayButtonBean();
        bean.mScreenWidth = this.metricsWidth;
        bean.mScreenHeight = this.metricsHeight;
        bean.mSurfaceWidth = layoutParams.width;
        bean.mSurfaceHeight = layoutParams.height;
        bean.mSurfaceTop = layoutParams.topMargin;
        bean.mVideoWidth = videoWidth;
        bean.mVideoHeight = videoHeight;
        int[] ints = FilterViewUtils.computeCurrentClipIvMargin(bean);
        LayoutParams params = (LayoutParams) this.filter_current_clip_iv.getLayoutParams();
        params.leftMargin = ints[0];
        params.topMargin = ints[1];
        this.filter_current_clip_iv.setLayoutParams(params);
    }

    private void initMissingFileHint() {
        LayoutParams layoutParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
        LayoutParams params = (LayoutParams) this.filter_missing_file_hint_rl.getLayoutParams();
        int viewHeight = this.filter_missing_file_hint_rl.getMeasuredHeight();
        if (viewHeight <= 0) {
            this.filter_missing_file_hint_rl.measure(-2, -2);
            viewHeight = this.filter_missing_file_hint_rl.getMeasuredHeight();
        }
        params.topMargin = (layoutParams.topMargin + (layoutParams.height / 2)) - (viewHeight / 2);
        this.filter_missing_file_hint_rl.setLayoutParams(params);
    }

    private void showFootageMissingHint() {
        if (this.filter_missing_file_hint_rl != null && this.filter_missing_file_hint_rl.getVisibility() != View.VISIBLE) {
            this.filter_missing_file_hint_rl.setVisibility(View.VISIBLE);
        }
    }

    private void hideFootageMissingHint() {
        if (this.filter_missing_file_hint_rl != null && this.filter_missing_file_hint_rl.getVisibility() != View.GONE) {
            this.filter_missing_file_hint_rl.setVisibility(View.GONE);
        }
    }

    private void showCurrentClipIv() {
        if (this.filter_current_clip_iv != null && this.filter_current_clip_iv.getVisibility() != View.VISIBLE) {
            this.filter_current_clip_iv.setVisibility(View.VISIBLE);
        }
    }

    private void hideCurrentClipIv() {
        if (this.filter_current_clip_iv != null && this.filter_current_clip_iv.getVisibility() != View.GONE) {
            this.filter_current_clip_iv.setVisibility(View.GONE);
        }
    }

    private void initLeakFilterReqWidthHeight() {
        int realWidth;
        int realHeight;
        float gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
        }
        if (this.mIsSquare) {
            this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
            int surfaceWidth = this.surfaceParams.width;
            int surfaceHeight = this.surfaceParams.height;
            if (surfaceWidth > surfaceHeight) {
                usableWidth = (float) surfaceHeight;
            } else {
                usableWidth = (float) surfaceWidth;
            }
            realWidth = (int) usableWidth;
            realHeight = (int) usableWidth;
        }
        realHeight += realHeight % 2;
        this.leakFilterReqWidth = realWidth + (realWidth % 2);
        this.leakFilterReqHeight = realHeight;
    }

    private void baseFixGridView(float degree) {
        float gridScale;
        int realWidth;
        int realHeight;
        LayoutParams gridParams;
        int rotateWidth = this.mVideoWidth;
        int rotateHeight = this.mVideoHeight;
        if (3 == this.mDataType) {
            int tmp = rotateWidth;
            rotateWidth = rotateHeight;
            rotateHeight = tmp;
        }
        if (degree == StaticLayoutUtil.DefaultSpacingadd || degree == 180.0f) {
            gridScale = (((float) rotateWidth) * 1.0f) / ((float) rotateHeight);
        } else {
            gridScale = (((float) rotateHeight) * 1.0f) / ((float) rotateWidth);
        }
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        int marginL = 0;
        int marginT = 0;
        int marginR = 0;
        int marginB = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
            marginT = marginB;
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginR = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
            marginL = marginR;
        }
        if (this.filter_crop_ccav != null) {
            gridParams = (LayoutParams) this.filter_crop_ccav.getLayoutParams();
        } else {
            gridParams = new LayoutParams(-2, -2);
        }
        if (this.mIsSquare) {
            this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
            int surfaceWidth = this.surfaceParams.width;
            int surfaceHeight = this.surfaceParams.height;
            if (surfaceWidth > surfaceHeight) {
                usableWidth = (float) surfaceHeight;
                marginL = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                marginT = 0;
                marginR = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                marginB = 0;
            } else {
                usableWidth = (float) surfaceWidth;
                marginL = 0;
                marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                marginR = 0;
                marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
            }
            realWidth = (int) usableWidth;
            realHeight = (int) usableWidth;
        }
        realWidth += realWidth % 2;
        realHeight += realHeight % 2;
        gridParams.width = realWidth;
        gridParams.height = realHeight;
        this.animationWidth = realWidth;
        this.animationHeight = realHeight;
        gridParams.setMargins(marginL, marginT, marginR, marginB);
        if (this.filter_crop_ccav != null) {
            this.filter_crop_ccav.setLayoutParams(gridParams);
        }
    }

    private void fixMirrorViewSize(float degree) {
        float gridScale;
        int realWidth;
        int realHeight;
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        boolean containItem = this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP);
        int mirrorWidth = this.mVideoWidth;
        int mirrorHeight = this.mVideoHeight;
        if (containItem) {
            FilterEffectBean cropItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP);
            if (cropItem != null) {
                float[] selectPosition = cropItem.selectPosition;
                if (selectPosition != null && selectPosition.length >= 7) {
                    mirrorWidth = (int) selectPosition[2];
                    mirrorHeight = (int) selectPosition[3];
                }
            }
        }
        if (3 == this.mDataType) {
            int tmp = mirrorWidth;
            mirrorWidth = mirrorHeight;
            mirrorHeight = tmp;
        }
        if (degree == StaticLayoutUtil.DefaultSpacingadd || degree == 180.0f) {
            gridScale = (((float) mirrorWidth) * 1.0f) / ((float) mirrorHeight);
        } else {
            gridScale = (((float) mirrorHeight) * 1.0f) / ((float) mirrorWidth);
        }
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        int marginL = 0;
        int marginT = 0;
        int marginR = 0;
        int marginB = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
            marginT = marginB;
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginR = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
            marginL = marginR;
        }
        if (this.mirror_seek_bar != null) {
            LayoutParams gridParams = (LayoutParams) this.mirror_seek_bar.getLayoutParams();
            if (this.mIsSquare) {
                this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
                int surfaceWidth = this.surfaceParams.width;
                int surfaceHeight = this.surfaceParams.height;
                if (surfaceWidth > surfaceHeight) {
                    usableWidth = (float) surfaceHeight;
                    marginL = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                    marginT = 0;
                    marginR = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                    marginB = 0;
                } else {
                    usableWidth = (float) surfaceWidth;
                    marginL = 0;
                    marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                    marginR = 0;
                    marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                }
                realWidth = (int) usableWidth;
                realHeight = (int) usableWidth;
            }
            realHeight += realHeight % 2;
            gridParams.width = realWidth + (realWidth % 2);
            gridParams.height = realHeight;
            gridParams.setMargins(marginL, marginT, marginR, marginB);
            this.mirror_seek_bar.setLayoutParams(gridParams);
            if (containItem) {
            }
        }
    }

    private void setCropFilterForModel5(FilterEffectBean bean) {
        int realWidth;
        int realHeight;
        float gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        int marginL = 0;
        int marginT = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginT = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginL = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
        }
        bean.selectPosition = CustomCropUtil.getCropPercent((float) (realWidth + (realWidth % 2)), (float) (realHeight + (realHeight % 2)), (float) ((int) (((float) marginT) + getResources().getDimension(2131427423))), (float) marginL);
        this.mFilterEffectManager.setSelectEffect(bean);
        if (this.gpuImageCropFilter == null) {
            this.gpuImageCropFilter = new GPUImageCropFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
            onCropInitAndRestore(bean);
        }
    }

    private void fixCustomCropViewSize(float degree) {
        float gridScale;
        int realWidth;
        int realHeight;
        if (degree == StaticLayoutUtil.DefaultSpacingadd || degree == 180.0f) {
            gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
        } else {
            gridScale = (((float) this.mVideoHeight) * 1.0f) / ((float) this.mVideoWidth);
        }
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
        int marginL = 0;
        int marginT = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginT = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginL = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
        }
        if (this.custom_crop_View != null) {
            if (this.mIsSquare) {
                this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
                int surfaceWidth = this.surfaceParams.width;
                int surfaceHeight = this.surfaceParams.height;
                if (surfaceWidth > surfaceHeight) {
                    usableWidth = (float) surfaceHeight;
                    marginL = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                    marginT = 0;
                    int marginR = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                } else {
                    usableWidth = (float) surfaceWidth;
                    marginL = 0;
                    marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                    int marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                }
                realWidth = (int) usableWidth;
                realHeight = (int) usableWidth;
            }
            this.custom_crop_View.setWidthAndHeight((float) (realWidth + (realWidth % 2)), (float) (realHeight + (realHeight % 2)), (float) ((int) (((float) marginT) + getResources().getDimension(2131427423))), (float) marginL, (float) ((LayoutParams) this.filter_title_bar.getLayoutParams()).height, (float) ((LayoutParams) this.filter_beauty_anim.getLayoutParams()).bottomMargin);
        }
    }

    private int getTopAndBottomSumViewHeight() {
        return DensityUtil.dip2px((float) 285);
    }

    private int getVideoTopAndBottomSumViewHeight() {
        return getTopAndBottomSumViewHeight() + DensityUtil.dip2px((float) 60);
    }

    private void fixSurfaceViewSize() {
        int usableHeight;
        int usedScreenWidth = DensityUtil.getMetricsWidth(this);
        int usedScreenHeight = DensityUtil.getMetricsHeight(this);
        int usableWidth = usedScreenWidth;
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            usableHeight = usedScreenHeight - getVideoTopAndBottomSumViewHeight();
        } else {
            usableHeight = usedScreenHeight - getTopAndBottomSumViewHeight();
        }
        int marginT = DensityUtil.dip2px(63.0f);
        float degree = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
        this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
        if (degree == 90.0f || degree == 270.0f) {
            this.mVideoFrames.mCropWidth = this.mVideoFrames.mCaptureHeight;
            this.mVideoFrames.mCropHeight = this.mVideoFrames.mCaptureWidth;
        }
        usableHeight -= usableHeight % 2;
        this.surfaceParams.width = usableWidth - (usableWidth % 2);
        this.surfaceParams.height = usableHeight;
        this.surfaceParams.setMargins(0, marginT, 0, 0);
        this.mSurfaceView.setLayoutParams(this.surfaceParams);
        this.filter_beauty_anim.setLayoutParams(this.surfaceParams);
        this.capture_filter_name_rl.setLayoutParams(this.surfaceParams);
        this.surface_click_view.setLayoutParams(this.surfaceParams);
        initDrawTextView();
        this.filter_stablize_rl.setAlpha(this.mSurfaceView.isStabilizerEnabled() ? 1.0f : 0.4f);
        this.filter_alpha_view.setLayoutParams(this.surfaceParams);
        this.filter_alpha_view.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
    }

    private void initDrawTextView() {
        int realWidth;
        int realHeight;
        int marginL;
        float gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
        float usableWidth = (float) DensityUtil.getMetricsWidth(this);
        float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getVideoTopAndBottomSumViewHeight());
        int marginT = 0;
        int marginB = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
            marginT = marginB;
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
            marginL = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
        }
        if (this.text_draw_rl != null) {
            int i;
            if (this.mIsSquare) {
                this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
                int surfaceWidth = this.surfaceParams.width;
                int surfaceHeight = this.surfaceParams.height;
                if (surfaceWidth > surfaceHeight) {
                    usableWidth = (float) surfaceHeight;
                    marginL = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                    marginT = 0;
                    int marginR = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                    marginB = 0;
                } else {
                    usableWidth = (float) surfaceWidth;
                    marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                    marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                }
                realWidth = (int) usableWidth;
                realHeight = (int) usableWidth;
            }
            realWidth += realWidth % 2;
            realHeight += realHeight % 2;
            marginT = (int) (((float) marginT) + getResources().getDimension(R.dimen._63dp));
            LayoutParams layoutParams = (LayoutParams) this.text_draw_rl.getLayoutParams();
            layoutParams.width = this.metricsWidth;
            layoutParams.height = realHeight;
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = marginT;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = marginB;
            this.text_draw_rl.setLayoutParams(layoutParams);
            LayoutParams lp = (LayoutParams) this.gold_line_view.getLayoutParams();
            lp.width = realWidth;
            this.gold_line_view.setLayoutParams(lp);
            LayoutParams lp2 = (LayoutParams) this.draw_text_view.getLayoutParams();
            lp2.width = realWidth;
            lp2.height = realHeight;
            this.draw_text_view.setLayoutParams(lp2);
            this.drawTextViewOffsetX = (this.metricsWidth - realWidth) / 2;
            this.draw_text_view.setViewWidth(realWidth);
            this.draw_text_view.setViewHeight(realHeight);
            VTContainerView vTContainerView = this.draw_text_view;
            if (realWidth > realHeight) {
                i = realHeight;
            } else {
                i = realWidth;
            }
            vTContainerView.setFontSizeType(i, VTContainerView.FontSizeType.M);
            this.draw_text_view.setAlignType(VTContainerView.AlignType.M);
            this.draw_text_view.setVerticalPos(0);
            this.draw_text_view.setTextColorType(VTContainerView.TextColorType.White);
            this.draw_text_view.setLetterSpacingType(VTContainerView.LetterSpacingType.XS);
            this.draw_text_view.setLineSpacingType(VTContainerView.LineSpacingType.S);
            this.draw_text_view.setShadowType(VTContainerView.ShadowType.NONE);
            this.tempFontSizeType = this.draw_text_view.getFontSizeType();
            this.tempLineSpacingType = this.draw_text_view.getLineSpacingType();
            this.tempLetterSpacingType = this.draw_text_view.getLetterSpacingType();
        }
    }

    private void loadFramesPicture() {
        new Thread() {
            public void run() {
                boolean savePicture = true;
                super.run();
                try {
                    if (mVideoFrames == null || mVideoFrames.size() == 0) {
                        System.gc();
                        return;
                    }
                    //EGL10Helper.withContext("loadFramesPicture", FilterActivity$45$.Lambda.1.lambdaFactory$(this));
                    EGL10Helper.withContext("loadFramesPicture", new EGLRunnableVoid() {
                        @Override
                        public void run(EGL10Helper eGL10Helper) {
                            lambda$run$0(eGL10Helper);
                        }
                    });
                    LogUtil.d("loadFramesForPicture", "if (!mRestart) { pre");
                    if (!mRestart) {
                        LogUtil.d("loadFramesForPicture", "if (!mRestart) {");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (mVideoFrames == null || mVideoFrames.size() == 0) {
                                    LogUtil.d("loadFramesForPicture", "mVideoFrames == null || mVideoFrames.size() == 0");
                                    return;
                                }
                                setNormalFilter();
                                if (mDraftModel != null) {
                                    restoreFilters();
                                }
                                ViewPropertyAnimator.animate(filter_alpha_view).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(FilterActivity.CHANEG_FILTER_TIME).start();
                                mTvSave.setEnabled(true);
                                mTvSave.setAlpha(1.0f);
                                mRestart = true;
                                LogUtil.d("loadFramesForPicture", "mRestart = true;");
                            }
                        });
                    }
                    System.gc();
                    int shortLength = DensityUtil.dip2px(44.0f);
                    if (mVideoHeight > mVideoWidth) {
                        int previewHeight = (int) ((((float) (mVideoHeight * shortLength)) * 1.0f) / ((float) mVideoWidth));
                    } else {
                        int access$13500 = (int) ((((float) (mVideoWidth * shortLength)) * 1.0f) / ((float) mVideoHeight));
                    }
                    forFilterBitmap = getFilterPreviewBitmap();
                    if (forFilterBitmap != null) {
                        //EGL10Helper.withContext("forFilterBitmap", FilterActivity$45$.Lambda.2.lambdaFactory$(this));
                        EGL10Helper.withContext("forFilterBitmap", new EGLRunnableVoid() {
                            @Override
                            public void run(EGL10Helper eGL10Helper) {
                                lambda$run$1(eGL10Helper);
                            }
                        });
                        BitmapUtils.saveBitmap2PrivateFile(forFilterBitmap);
                    }
                    BitmapUtils.recycleBitmap(forFilterBitmap);
                    System.gc();
                    if (mDataFrom != 0) {
                        System.gc();
                        return;
                    }
                    boolean noHavePermission;
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        noHavePermission = true;
                    } else {
                        noHavePermission = false;
                    }
                    if (!(mDataFrom == 0 && 1 == mDataType)) {
                        savePicture = false;
                    }
                    if (mDraftModel == null && savePicture) {
                        if (VERSION.SDK_INT >= 23 && noHavePermission) {
                            ActivityCompat.requestPermissions(FilterActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 102);
                        } else if (!noHavePermission) {
                            savePictureOriginal();
                        }
                    }
                    System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                    BitmapUtils.recycleBitmap(forFilterBitmap);
                    System.gc();
                } catch (Throwable th) {
                    System.gc();
                    throw th;
                }
            }

            private /* synthetic */ void lambda$run$0(EGL10Helper egl) {
                mVideoFrames.limitDuration((long) (mFrameCount * 100000));
                mSurfaceView.setVideoFrames(mVideoFrames);
                mSurfaceView.setViewOffset(mOffsetX, mOffsetY);
                loadFilterInfos();
                setNormalFilter();
                mSurfaceView.waitForFrame();
            }

            private /* synthetic */ void lambda$run$1(EGL10Helper egl) {
                EGLSurface surface = egl.createPBuffer(forFilterBitmap.getWidth(), forFilterBitmap.getHeight());
                egl.makeCurrent(surface);
                if (mVideoFrames != null) {
                    int historyPos = 1;
                    int i = 0;
                    while (i < mFilterInfos.size()) {
                        if (((FilterInfo) mFilterInfos.get(i)).name_en != null && ((FilterInfo) mFilterInfos.get(i)).name_en.equals(mFilterName)) {
                            historyPos = i;
                            break;
                        }
                        i++;
                    }
                    generatePreviewBitmaps(forFilterBitmap, 0, mFilterInfos.size(), historyPos);
                } else {
                    generatePreviewBitmaps(forFilterBitmap, 0, mFilterInfos.size(), 0);
                }
                if (surface != null) {
                    egl.destroySurface(surface);
                }
            }
        }.start();
    }

    private Bitmap getFilterPreviewBitmap() {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        String filePath = "";
        byte[] imageDataBytes = null;
        int length = 0;
        if (1 == this.mDataFrom) {
            filePath = ImagePropertyBean.getInstance().getImagePath();
            if (TextUtil.isNull(filePath) || !new File(filePath).exists()) {
                return null;
            }
            BitmapFactory.decodeFile(filePath, options);
        } else {
            imageDataBytes = ImagePropertyBean.getInstance().getImageDataBytes();
            if (imageDataBytes == null) {
                return null;
            }
            length = imageDataBytes.length;
            BitmapFactory.decodeByteArray(imageDataBytes, 0, length, options);
        }
        int maxLength = DensityUtil.dip2px(44.0f) * 3;
        options.inSampleSize = OptionSizeUtil.computeSampleSize(options, -1, maxLength * maxLength);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = false;
        options.inPurgeable = true;
        int opWidth = options.outWidth;
        int opHeight = options.outHeight;
        if (opWidth > opHeight) {
            if (opWidth > maxLength) {
                options.outWidth = maxLength;
                options.outHeight = (int) (((float) maxLength) / ((((float) opWidth) * 1.0f) / ((float) opHeight)));
            }
        } else if (opHeight > maxLength) {
            options.outHeight = maxLength;
            options.outWidth = (int) (((float) maxLength) * ((((float) opWidth) * 1.0f) / ((float) opHeight)));
        }
        if (1 != this.mDataFrom) {
            return BitmapFactory.decodeByteArray(imageDataBytes, 0, length, options);
        }
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int mOrientation = BitmapUtils.getLocalPictureOrientation(filePath);
        if (1 == mOrientation) {
            mOrientation = 0;
        } else if (6 == mOrientation) {
            mOrientation = 90;
        } else if (8 == mOrientation) {
            mOrientation = BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE;
        } else if (3 == mOrientation) {
            mOrientation = 180;
        }
        return BitmapUtils.adjustPhotoRotation(bitmap, mOrientation);
    }

    private GPUImageFilterGroup getFramesFilter(FrameRenderer renderer, int index, GPUImageFilter finalGroup) {
        if (index != 0 && index != 1) {
            return null;
        }
        GPUImageFilter filter = new GPUImageFilter();
        Framebuffer fb = new Framebuffer("getFramesFilter", this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight);
        GLES20.glBindFramebuffer(36160, fb.fboid());
        GLES20.glDisable(3089);
        GLES20.glClearColor(StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, 1.0f);
        GLES20.glClear(16384);
        GLES20.glBindFramebuffer(36160, 0);
        this.mSurfaceView.drawFrameToBufferInCurrent(renderer, filter, index, fb);
        filter.destroy();
        filter.destroySecondary();
        float weight = 1.0f;
        if (index == 0) {
            weight = 0.33f;
        }
        if (index == 1) {
            weight = 0.66f;
        }
        GPUImageAlphaBlendFilter alpha = new GPUImageAlphaBlendFilter(weight);
        alpha.setSecondaryTexture(fb.transferTextureOwnership());
        alpha.setRotation(Rotation.NORMAL, false, false);
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(alpha);
        filterGroup.addFilter(finalGroup);
        fb.destroy();
        return filterGroup;
    }

    private void showFilterNameInPreview(int position) {
        if (this.valueAnimator != null) {
            this.valueAnimator.cancel();
        }
        FilterInfo filterInfo = (FilterInfo) App.mFilterMap.get(this.mFilterName);
        if (filterInfo != null) {
            String groupName;
            String filterName;
            switch (LocaleUtil.getLanguageCode()) {
                case 1:
                    groupName = filterInfo.groupNameChs;
                    filterName = filterInfo.name_chs;
                    break;
                case 2:
                    groupName = filterInfo.groupNameCht;
                    filterName = filterInfo.name_cht;
                    break;
                case 3:
                    groupName = filterInfo.groupNameEn;
                    filterName = filterInfo.name_en;
                    break;
                default:
                    groupName = filterInfo.groupNameEn;
                    filterName = filterInfo.name_en;
                    break;
            }
            if (TextUtil.isNull(groupName)) {
                groupName = "";
            }
            if (TextUtil.isNull(filterName)) {
                filterName = "";
            }
            if ("".equalsIgnoreCase(groupName) || NONE_FILTER.equalsIgnoreCase(groupName) || "无".equalsIgnoreCase(groupName) || "無".equalsIgnoreCase(groupName)) {
                this.capture_filter_group_name_tv.setVisibility(View.GONE);
                this.capture_filter_name_tv.setText(getActivity().getResources().getString(R.string.BUTTON_NO_PRESET));
                ((LinearLayout.LayoutParams) this.capture_filter_name_tv.getLayoutParams()).topMargin = 0;
            } else {
                this.capture_filter_group_name_tv.setVisibility(View.VISIBLE);
                this.capture_filter_group_name_tv.setText(groupName);
                this.capture_filter_name_tv.setText(filterName);
                ((LinearLayout.LayoutParams) this.capture_filter_name_tv.getLayoutParams()).topMargin = DensityUtil.dip2px(2.0f);
            }
            this.capture_filter_name_rl.setVisibility(View.VISIBLE);
            this.mHandler.removeCallbacks(this.alphaRannable);
            this.mHandler.postDelayed(this.alphaRannable, CHANEG_FILTER_TIME);
        }
    }

    void createTempbuffers(int width, int height) {
        if (!(this.fb0 != null && this.fb0.width == width && this.fb0.height == height)) {
            if (this.fb0 != null) {
                this.mSurfaceView.destroyFramebuffer(this.fb0);
            }
            this.fb0 = this.mSurfaceView.newFramebuffer(width, height);
        }
        if (this.fb1 == null || this.fb1.width != width || this.fb1.height != height) {
            if (this.fb1 != null) {
                this.mSurfaceView.destroyFramebuffer(this.fb1);
            }
            this.fb1 = this.mSurfaceView.newFramebuffer(width, height);
        }
    }

    void destroyTempBuffers() {
        if (this.fb0 != null) {
            this.mSurfaceView.destroyFramebuffer(this.fb0);
            this.fb0 = null;
        }
        if (this.fb1 != null) {
            this.mSurfaceView.destroyFramebuffer(this.fb1);
            this.fb1 = null;
        }
    }

    private void setNewFilterAnimate(GPUImageFilter[] newfilters) {
        if (this.oldFilters == null) {
            this.oldFilters = newfilters;
            this.previouFilters = newfilters;
            this.mSurfaceView.setFilters(this.oldFilters);
            return;
        }
        int aWidth;
        int aHeight;
        this.startTime = System.currentTimeMillis();
        this.newFilters = newfilters;
        if (this.leftSlide) {
            this.mSurfaceView.setViewport(1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
        } else {
            this.mSurfaceView.setViewport(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
        }
        this.mSurfaceView.setFilters(this.previouFilters, this.newFilters);
        fixGridViewSize(this.currentDegree);
        if (this.filter_crop_ccav == null) {
            aWidth = this.animationWidth;
            aHeight = this.animationHeight;
        } else {
            aWidth = this.filter_crop_ccav.getLayoutParams().width;
            aHeight = this.filter_crop_ccav.getLayoutParams().height;
        }
        final int width = aWidth;
        final int height = aHeight;
        this.previouFilters = this.newFilters;
        if (!this.filterIsChanging) {
            this.filterIsChanging = true;
            this.filterAdapter.setFilterChangeState(true);
            this.mSurfaceView.requestAnimation(new Runnable() {
                public void run() {
                    long t = System.currentTimeMillis() - startTime;
                    float durationTime = 500.0f;
                    if (width < height) {
                        durationTime = 500.0f * ((((float) width) * 1.0f) / ((float) metricsWidth));
                    }
                    float alpha = (float) ((1.054d * Math.exp(-2.544d * ((double) ((((float) t) * 1.0f) / durationTime)))) - 0.06807d);
                    if (alpha <= 0.017f) {
                        oldFilters = newFilters;
                        newFilters = null;
                        mSurfaceView.setFilters(oldFilters);
                        mSurfaceView.setViewport(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f);
                        filterIsChanging = false;
                        filterAdapter.setFilterChangeState(false);
                        return;
                    }
                    float x;
                    float w;
                    if (leftSlide) {
                        x = alpha;
                        w = 1.0f - alpha;
                    } else {
                        x = StaticLayoutUtil.DefaultSpacingadd;
                        w = 1.0f - alpha;
                    }
                    LogUtil.d("filterAnimation", "x : " + x + " , w : " + w);
                    mSurfaceView.setViewport(x, StaticLayoutUtil.DefaultSpacingadd, w, 1.0f);
                    mSurfaceView.postOnAnimation(this);
                }
            });
        }
    }

    void setNormalFilters(GPUImageFilter filterGroup) {
        if (this.mVideoFrames != null) {
            GPUImageFilter[] filters;
            int i;
            if (!this.openStackFrame || this.mFps == StaticLayoutUtil.DefaultSpacingadd || this.mVideoFrames.size() <= 3 || this.mVideoFrames.mIsLooped || 1 == this.mDataType) {
                this.mSurfaceView.destroyFilters();
                filters = new GPUImageFilter[this.mVideoFrames.size()];
                int size = filters.length;
                for (i = 0; i < size; i++) {
                    filters[i] = filterGroup;
                }
                this.mSurfaceView.setWrapFrame(0);
                this.oldFilters = filters;
                this.previouFilters = filters;
                this.mSurfaceView.setFilters(filters);
                return;
            }
            this.mSurfaceView.destroyFilters();
            long t1 = System.nanoTime();
            createTempbuffers(this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight);
            long tx = System.nanoTime();
            if (!this.hadInitializeFrameTexure || this.mEnableStabilizer) {
                this.hadInitializeFrameTexure = true;
                GPUImageFilter filter = new GPUImageFilter();
                this.mSurfaceView.drawFrameToBuffer(this.mSurfaceView.getFrameRenderer(), filter, 0, this.fb0);
                this.mSurfaceView.drawFrameToBuffer(this.mSurfaceView.getFrameRenderer(), filter, 1, this.fb1);
                this.mSurfaceView.destroyFilter(filter);
            }
            long t2 = System.nanoTime();
            this.mSurfaceView.setWrapFrame(2);
            filters = new GPUImageFilter[this.mVideoFrames.size()];
            for (i = 0; i < this.mVideoFrames.size(); i++) {
                filters[i] = filterGroup;
            }
            if (this.alpha_0 == null) {
                this.alpha_0 = new GPUImageAlphaBlendFilter(0.33f);
            }
            this.alpha_0.setExternalTexture(this.fb0.texid());
            this.alpha_0.setRotation(Rotation.NORMAL, false, false);
            if (this.alpha_1 == null) {
                this.alpha_1 = new GPUImageAlphaBlendFilter(0.66f);
            }
            this.alpha_1.setExternalTexture(this.fb1.texid());
            this.alpha_1.setRotation(Rotation.NORMAL, false, false);
            GPUImageFilterGroup filterGroup_0 = new GPUImageFilterGroup();
            filterGroup_0.addFilter(this.alpha_0);
            filterGroup_0.addFilter(filterGroup);
            filters[this.mVideoFrames.size() - 2] = filterGroup_0;
            GPUImageFilterGroup filterGroup_1 = new GPUImageFilterGroup();
            filterGroup_1.addFilter(this.alpha_1);
            filterGroup_1.addFilter(filterGroup);
            filters[this.mVideoFrames.size() - 1] = filterGroup_1;
            long ty = System.nanoTime();
            this.oldFilters = filters;
            this.previouFilters = filters;
            this.mSurfaceView.setFilters(filters);
            long t3 = System.nanoTime();
            LogUtil.d(String.format("alpha filter create FrameTexture time : %s", (tx - t1) / 1000000));
            LogUtil.d(String.format("alpha filter getFrameTexture time : %s", (t2 - tx) / 1000000));
            LogUtil.d(String.format("alpha filter time : %s", (t3 - t2) / 1000000));
            LogUtil.d(String.format("alpha filter only set filter time : %s", (t3 - ty) / 1000000));
        }
    }

    void setFilters(GPUImageFilter filterGroup) {
        if (this.mVideoFrames != null) {
            GPUImageFilter[] filters;
            int i;
            if (!this.openStackFrame || this.mFps == StaticLayoutUtil.DefaultSpacingadd || this.mVideoFrames.size() <= 3 || this.mVideoFrames.mIsLooped || 1 == this.mDataType) {
                this.mSurfaceView.destroyFilters();
                filters = new GPUImageFilter[this.mVideoFrames.size()];
                int size = filters.length;
                for (i = 0; i < size; i++) {
                    filters[i] = filterGroup;
                }
                this.mSurfaceView.setWrapFrame(0);
                setNewFilterAnimate(filters);
                return;
            }
            long t1 = System.nanoTime();
            createTempbuffers(this.mVideoFrames.mCaptureWidth, this.mVideoFrames.mCaptureHeight);
            long tx = System.nanoTime();
            if (!this.hadInitializeFrameTexure || this.mEnableStabilizer) {
                this.hadInitializeFrameTexure = true;
                GPUImageFilter filter = new GPUImageFilter();
                this.mSurfaceView.drawFrameToBuffer(this.mSurfaceView.getFrameRenderer(), filter, 0, this.fb0);
                this.mSurfaceView.drawFrameToBuffer(this.mSurfaceView.getFrameRenderer(), filter, 1, this.fb1);
                this.mSurfaceView.destroyFilter(filter);
            }
            long t2 = System.nanoTime();
            this.mSurfaceView.setWrapFrame(2);
            filters = new GPUImageFilter[this.mVideoFrames.size()];
            for (i = 0; i < this.mVideoFrames.size(); i++) {
                filters[i] = filterGroup;
            }
            if (this.alpha_0 == null) {
                this.alpha_0 = new GPUImageAlphaBlendFilter(0.33f);
            }
            this.alpha_0.setExternalTexture(this.fb0.texid());
            this.alpha_0.setRotation(Rotation.NORMAL, false, false);
            if (this.alpha_1 == null) {
                this.alpha_1 = new GPUImageAlphaBlendFilter(0.66f);
            }
            this.alpha_1.setExternalTexture(this.fb1.texid());
            this.alpha_1.setRotation(Rotation.NORMAL, false, false);
            GPUImageFilterGroup filterGroup_0 = new GPUImageFilterGroup();
            filterGroup_0.addFilter(this.alpha_0);
            filterGroup_0.addFilter(filterGroup);
            filters[this.mVideoFrames.size() - 2] = filterGroup_0;
            GPUImageFilterGroup filterGroup_1 = new GPUImageFilterGroup();
            filterGroup_1.addFilter(this.alpha_1);
            filterGroup_1.addFilter(filterGroup);
            filters[this.mVideoFrames.size() - 1] = filterGroup_1;
            long ty = System.nanoTime();
            setNewFilterAnimate(filters);
            long t3 = System.nanoTime();
            LogUtil.d(String.format("alpha filter create FrameTexure time : %s", (tx - t1) / 1000000));
            LogUtil.d(String.format("alpha filter getFrameTexure time : %s", (t2 - tx) / 1000000));
            LogUtil.d(String.format("alpha filter time : %s", (t3 - t2) / 1000000));
            LogUtil.d(String.format("alpha filter only set filter time : %s", (t3 - ty) / 1000000));
        }
    }

    private void loadFilterInfos() {
        loadFilterInfos(true, true, true);
    }

    private void loadFilterInfosSpecial(List<FilterEffectBean> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                loadFilterEffect(list.get(i), false, false);
            }
        }
    }

    private void loadFilterInfos(boolean needClear, boolean needRefresh, boolean needSetNormal) {
        List<FilterEffectBean> mList = this.mFilterEffectManager.getCurrentList();
        if (this.tiltFilter == null) {
            this.tiltFilter = getGaussianFilter();
        }
        if (mList != null && mList.size() > 0) {
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                loadFilterEffect(mList.get(i), needRefresh, needSetNormal);
            }
            if (needClear) {
                this.currentEffectType = FilterEffectManager.EffectType.NONE;
            }
        }
    }

    private Bitmap getOriginalFilterBitmap(int resId, boolean crop, boolean needSquare) {
        Bitmap bitmap = null;
        try {
            Matrix m = new Matrix();
            if (needSquare) {
                m.postRotate(180.0f);
            } else if (this.mIsBackCamera) {
                if (this.orientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                    m.postRotate(180.0f);
                }
            } else if (this.orientation == 0) {
                m.postRotate(270.0f);
            } else if (this.orientation == 90) {
                m.postScale(-1.0f, 1.0f);
            } else if (this.orientation == 180) {
                m.postRotate(90.0f);
            } else if (this.orientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                m.postRotate(180.0f);
            }
            bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), resId, this.metricsWidth, this.metricsHeight);
            if (bitmap == null) {
                return null;
            }
            System.gc();
            bitmap = BitmapUtils.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
            if (bitmap == null) {
                return null;
            }
            bitmap = BitmapUtils.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
            if (bitmap == null) {
                return null;
            }
            System.gc();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            width -= width % 2;
            height -= height % 2;
            if (this.mIsSquare || needSquare) {
                height = Math.min(width, height);
                width = height;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            System.gc();
            return bitmap;
        } catch (OutOfMemoryError e) {
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            e.printStackTrace();
            System.gc();
            return null;
        }
    }

    public Bitmap getBitmapForFilterEffect(int resId, boolean crop, boolean needSquare) {
        Bitmap bitmap = null;
        try {
            Matrix m = new Matrix();
            if (needSquare) {
                m.postRotate(180.0f);
            } else if (this.mIsBackCamera) {
                if (this.orientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                    m.postRotate(180.0f);
                }
            } else if (this.orientation == 0) {
                m.postRotate(-180.0f);
                m.postScale(1.0f, -1.0f);
            } else if (this.orientation == 90) {
                m.postScale(-1.0f, 1.0f);
            } else if (this.orientation == 180) {
                m.postRotate(-180.0f);
                m.postScale(-1.0f, 1.0f);
            } else if (this.orientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                m.postScale(1.0f, -1.0f);
            }
            bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), resId, this.metricsWidth, this.metricsHeight);
            bitmap = BitmapUtils.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
            if (bitmap == null) {
                return null;
            }
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int bitmapX = 0;
            int bitmapY = 0;
            if (crop) {
                float ratio;
                float bitmapRatio;
                int frameWidth = this.mVideoFrames.mCaptureWidth;
                int frameHeight = this.mVideoFrames.mCaptureHeight;
                if (frameWidth < frameHeight) {
                    ratio = (((float) frameHeight) * 1.0f) / ((float) frameWidth);
                } else {
                    ratio = (((float) frameWidth) * 1.0f) / ((float) frameHeight);
                }
                if (bitmapWidth > bitmapHeight) {
                    bitmapRatio = (((float) bitmapWidth) * 1.0f) / ((float) bitmapHeight);
                } else {
                    bitmapRatio = (((float) bitmapHeight) * 1.0f) / ((float) bitmapWidth);
                }
                if (ratio <= bitmapRatio) {
                    bitmapWidth = (int) (((float) bitmapHeight) * ratio);
                    bitmapX = (bitmap.getWidth() - bitmapWidth) / 2;
                    if (bitmapX < 0) {
                        bitmapX = 0;
                        bitmapWidth = bitmap.getWidth();
                    }
                } else {
                    bitmapHeight = (int) ((((float) bitmapWidth) * 1.0f) / ratio);
                    bitmapY = (bitmap.getHeight() - bitmapHeight) / 2;
                    if (bitmapY < 0) {
                        bitmapY = 0;
                        bitmapHeight = bitmap.getHeight();
                    }
                }
            }
            bitmap = BitmapUtils.createBitmap(bitmap, bitmapX, bitmapY, bitmapWidth, bitmapHeight);
            if (bitmap == null) {
                return null;
            }
            System.gc();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            width -= width % 2;
            height -= height % 2;
            if (this.mIsSquare || needSquare) {
                height = Math.min(width, height);
                width = height;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            System.gc();
            return bitmap;
        } catch (OutOfMemoryError e) {
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            e.printStackTrace();
            System.gc();
            return null;
        }
    }

    private void changeFilterEffectSelectState(List<FilterEffectBean> mList) {
        if (this.filter_effect_exposure != null) {
            this.filter_effect_exposure.setDateColor(getActivity());
            this.filter_effect_beauty.setDateColor(getActivity());
            this.filter_effect_contrast.setDateColor(getActivity());
            this.filter_effect_saturation.setDateColor(getActivity());
            this.filter_effect_temperature.setDateColor(getActivity());
            this.filter_effect_grain.setDateColor(getActivity());
            this.filter_effect_dust.setDateColor(getActivity());
            this.filter_effect_vignette.setDateColor(getActivity());
            this.filter_effect_leak.setDateColor(getActivity());
            this.filter_effect_date.setDateColor(getActivity());
            this.filter_effect_tilt.setDateColor(getActivity());
            this.filter_effect_prism.setDateColor(getActivity());
            this.filter_effect_sharpen.setDateColor(getActivity());
            this.filter_effect_fade.setDateColor(getActivity());
            this.filter_effect_highlight_shadow.setDateColor(getActivity());
            this.filter_effect_shade_lighten.setDateColor(getActivity());
            this.filter_effect_crop.setDateColor(getActivity());
            this.filter_effect_rotation.setDateColor(getActivity());
            this.filter_effect_vertical.setDateColor(getActivity());
            this.filter_effect_horizontal.setDateColor(getActivity());
            this.filter_effect_mirror.setDateColor(getActivity());
            int size = 0;
            if (mList != null) {
                size = mList.size();
            }
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    FilterEffectBean bean = (FilterEffectBean) mList.get(i);
                    if (bean.getItemSelect()) {
                        whichViewIsSelected(bean.effectType);
                    }
                }
            }
        }
    }

    private void initFilterEffectProgress(FilterEffectSetRelativeLayout view, int defaultMultiplier, float value) {
        view.setProgressDefaultMultiplier(defaultMultiplier);
        view.setProgressValue(Math.abs(value));
    }

    private void whichViewIsSelected(FilterEffectManager.EffectType effectType) {
        FilterEffectBean bean = this.mFilterEffectManager.getAssignedBeanByType(effectType);
        if (bean != null && bean.value != null && bean.value.length > 1) {
            float value = bean.value[1];
            int valueMoreThanZero = value >= StaticLayoutUtil.DefaultSpacingadd ? 1 : -1;
            switch (effectType) {
                case HIGHLIGHT:
                    initFilterEffectProgress(this.filter_effect_shade_lighten, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_shade_lighten, true);
                    return;
                case SKY:
                    initFilterEffectProgress(this.filter_effect_sky, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_sky, true);
                    return;
                case SHADOW:
                    initFilterEffectProgress(this.filter_effect_highlight_shadow, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_highlight_shadow, true);
                    return;
                case FADED:
                    initFilterEffectProgress(this.filter_effect_fade, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_fade, true);
                    return;
                case TILT:
                    initFilterEffectProgress(this.filter_effect_tilt, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_tilt, true);
                    return;
                case SHARPEN:
                    initFilterEffectProgress(this.filter_effect_sharpen, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_sharpen, true);
                    return;
                case GRAIN:
                    initFilterEffectProgress(this.filter_effect_grain, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_grain, true);
                    return;
                case TEMPERATURE:
                    initFilterEffectProgress(this.filter_effect_temperature, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_temperature, true);
                    return;
                case SATURATION:
                    initFilterEffectProgress(this.filter_effect_saturation, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_saturation, true);
                    return;
                case ROTATE:
                    this.filter_effect_rotation.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_rotation, true);
                    return;
                case CROP:
                    this.filter_effect_crop.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_crop, true);
                    return;
                case CONTRAST:
                    initFilterEffectProgress(this.filter_effect_contrast, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_contrast, true);
                    return;
                case MIRROR:
                    this.filter_effect_crop.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_mirror, true);
                    return;
                case HORIZONTAL:
                    this.filter_effect_crop.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_horizontal, true);
                    return;
                case VERTICAL:
                    this.filter_effect_crop.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_vertical, true);
                    return;
                case EXPOSURE:
                    initFilterEffectProgress(this.filter_effect_exposure, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_exposure, true);
                    return;
                case BEAUTIFY:
                    initFilterEffectProgress(this.filter_effect_beauty, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_beauty, true);
                    return;
                case VIGNETTE:
                    initFilterEffectProgress(this.filter_effect_vignette, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_vignette, true);
                    return;
                case LEAK:
                    this.filter_effect_crop.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_leak, true);
                    return;
                case TINGE:
                    initFilterEffectProgress(this.filter_effect_tinge, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_tinge, true);
                    return;
                case DATE:
                    this.filter_effect_date.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_date, true);
                    return;
                case DUST:
                    this.filter_effect_dust.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_dust, true);
                    return;
                case PRISM:
                    initFilterEffectProgress(this.filter_effect_prism, valueMoreThanZero, value);
                    setViewColor(this.filter_effect_prism, true);
                    return;
                default:
                    return;
            }
        }
    }

    private void setViewColor(android.view.View view, boolean isChoose) {
        if (view != null) {
            if (isChoose) {
                ((FilterEffectSetRelativeLayout) view).setWhiteColor(getActivity());
            } else {
                ((FilterEffectSetRelativeLayout) view).setDateColor(getActivity());
            }
        }
    }

    private void initFilterGroupNameInfo() {
        if (this.filterGroupNameAdapter == null) {
            this.filterGroupNameAdapter = new FilterGroupNameAdapter(this, this.mFilterGroupNames);
        }
        this.filter_group_name_rv.setHasFixedSize(true);
        this.filter_group_name_rv.setAdapter(this.filterGroupNameAdapter);
        this.mFilterGroupNameManager = new LinearLayoutManager(this);
        this.mFilterGroupNameManager.setOrientation(0);
        this.filter_group_name_rv.setLayoutManager(this.mFilterGroupNameManager);
        //this.filterGroupNameAdapter.setOnClickListener(30.lambdaFactory$(this));
        filterGroupNameAdapter.setOnClickListener(new FilterGroupOnclickListener() {
            @Override
            public void onClick(View view, int i) {
                lambda$initFilterGroupNameInfo$28(view,i);
            }
        });
    }

    private /* synthetic */ void lambda$initFilterGroupNameInfo$28(android.view.View view, int position) {
        if (position != this.filterGroupNameAdapter.getSelectPosition() && !this.filterAdapter.isCollectedMode) {
            onFilterGroupNameClick(position);
        }
    }

    private void onFilterGroupNameClick(final int position) {
        this.filterGroupNameAdapter.setSelectPosition(position);
        this.filterGroupNameAdapter.notifyDataSetChanged();
        //this.filter_group_name_rv.post(31.lambdaFactory$(this, position));
        filter_group_name_rv.post(new Runnable() {
            @Override
            public void run() {
                lambda$onFilterGroupNameClick$29(position);
            }
        });
        String groupName = ((FilterInfo) this.mFilterGroupNames.get(position)).name_en;
        if (TextUtil.isNull(groupName)) {
            groupName = "";
        }
        int shouldPosition = this.mHelper.shouldSelectFilterPosition(this.mFilterInfos, groupName);
        if (shouldPosition == 1) {
            shouldPosition = this.filterAdapter.getNoneFilterIndex();
        }
        this.filterAdapter.firstClickFilter(shouldPosition, (FilterInfo) this.mFilterInfos.get(shouldPosition), null, true);
    }

    private /* synthetic */ void lambda$onFilterGroupNameClick$29(int position) {
        android.view.View viewByPosition = this.mFilterGroupNameManager.findViewByPosition(position);
        if (viewByPosition != null) {
            this.filter_group_name_rv.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (this.metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
        }
    }

    private void setFilterGroupNameState(int position) {
        final int groupPosition = this.mHelper.shouldSelectFilterGroupPosition(this.mFilterGroupNames, this.mFilterInfos, position);
        this.filterGroupNameAdapter.setSelectPosition(groupPosition);
        this.filterGroupNameAdapter.notifyDataSetChanged();
        this.filter_group_name_rv.scrollToPosition(groupPosition);
        this.filter_group_name_rv.post(new Runnable() {
            public void run() {
                android.view.View viewByPosition = mFilterGroupNameManager.findViewByPosition(groupPosition);
                if (viewByPosition != null) {
                    filter_group_name_rv.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                }
            }
        });
    }

    private void initFilterListInfo() {
        if (this.filterAdapter == null) {
            this.filterAdapter = new FilterAdapter(this, this.mFilterInfos);
        }
        this.filterAdapter.setFilterName(NONE_FILTER, this.mHelper.getFilterInfoByFilterName(this.mFilterInfos, NONE_FILTER));
        this.filter_list_recyclerview.setHasFixedSize(true);
        this.filter_list_recyclerview.setAdapter(this.filterAdapter);
        this.mFilterListLayoutManager = new LinearLayoutManager(this);
        this.mFilterListLayoutManager.setOrientation(0);
        this.filter_list_recyclerview.setLayoutManager(this.mFilterListLayoutManager);
        this.filter_list_recyclerview.setOnScrollListener(this.scrollListener);
        this.filterAdapter.setOnClickListener(new FilterAdapter.OnClickListener() {
            public void onClick(android.view.View view, final int position, String filterName, boolean isLeftSlide) {
                setFilterGroupNameState(position);
                LogUtil.d("filterAdapter", "mFilterName : " + mFilterName + " filterName : " + filterName);
                if (mFilterName == null || !mFilterName.equals(filterName)) {
                    mFilterName = filterName;
                    LogUtil.d("adapterOnclick", "position : " + position);
                    if (mDataType == 2 || mVideoFrames != null) {
                        setFilterNameForLongVideosModel();
                        filterAdapter.notifyDataSetChanged();
                        filter_list_recyclerview.scrollToPosition(position);
                        //filter_list_recyclerview.post(FilterActivity$49$.Lambda.1.lambdaFactory$(this, position));
                        filter_list_recyclerview.post(new Runnable() {
                            @Override
                            public void run() {
                                lambda$onClick$0(position);
                            }
                        });
                        if (mFilterModel != null) {
                            mFilterModel.setmFinalEffectState(mFinalEffectState);
                            mFilterModel.setFilterName(mFilterName);
                        }
                        currentFilterName = filterName;
                        if (!isPressedVideoArea) {
                            leftSlide = isLeftSlide;
                            filterChoosePosition = position;
                            if (isChangeFilterCombination) {
                                isChangeFilterCombination = false;
                            } else {
                                if (text_draw_rl.getVisibility() != View.VISIBLE) {
                                    showFilterNameInPreview(filterChoosePosition);
                                }
                                setNormalFilter();
                                setUndoData(0);
                            }
                        }
                        if (filter_effect_root_ll.getVisibility() != View.VISIBLE) {
                            filter_tag_effect_iv.setAlpha(0.4f);
                            filter_tag_loop_iv.setAlpha(0.4f);
                            filter_tag_filter_iv.setAlpha(1.0f);
                            filter_tag_music_iv.setAlpha(0.4f);
                            return;
                        }
                        return;
                    }
                    return;
                }
                filterEffectBeanList = mFilterEffectManager.getCurrentList();
                if (position != filterAdapter.getNoneFilterIndex()) {
                    float pro;
                    initRulerViewStub();
                    changeFilterEffectSelectState(filterEffectBeanList);
                    FilterEffectBean filterEffectBean = mFilterEffectManager.getStrengthBean();
                    currentEffectType = filterEffectBean.effectType;
                    alphaStrengthEnterAndExit(filter_tab_choose_ll, filter_tab_ll, true);
                    hideSaveAndBack();
                    alphaEnterAndExit(filter_rule_ll, filter_filter_root_rl, filter_filter_root_rl, filter_loop_root_rl);
                    filter_custom_sk.setOnCustomProgressChangedListener(new FilterOnCustomProgressChangedListener(filterEffectBean.getFilterEffectBeanString()));
                    if (mDataType == 2) {
                        LongVideosModel currentVideoModel = getCurrentVideoModel();
                        if (currentVideoModel == null) {
                            pro = filterEffectBean.value[1];
                        } else if (currentVideoModel.getFilterState() == 0) {
                            pro = currentVideoModel.getPublicIntensity() * 10.0f;
                        } else {
                            pro = currentVideoModel.getPrivateIntensity() * 10.0f;
                        }
                    } else {
                        pro = filterEffectBean.value[1];
                    }
                    filter_custom_sk.isCenter(filterEffectBean.isCenterAdsorb()).min(filterEffectBean.value[0]).max(filterEffectBean.value[2]).pro(pro).type(filterEffectBean.effectType).build();
                    showFilterEffectSeekBar();
                }
            }

            private /* synthetic */ void lambda$onClick$0(int position) {
                android.view.View viewByPosition = mFilterListLayoutManager.findViewByPosition(position);
                if (viewByPosition != null) {
                    filter_list_recyclerview.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                }
            }

            public void onClickForGroupNameClick(android.view.View view, final int position, String filterName, boolean isLeftSlide) {
                LogUtil.d("onClickForGroupNameClick", String.format("position : %s ,", new Object[]{Integer.valueOf(position)}));
                mFilterName = filterName;
                if (mDataType == 2 || mVideoFrames != null) {
                    setFilterNameForLongVideosModel();
                    filterAdapter.notifyDataSetChanged();
                    filter_list_recyclerview.scrollToPosition(position);
                    //filter_list_recyclerview.post(FilterActivity$49$.Lambda.2.lambdaFactory$(this, position));
                    filter_list_recyclerview.post(new Runnable() {
                        @Override
                        public void run() {
                            lambda$onClickForGroupNameClick$1(position);
                        }
                    });
                    if (mFilterModel != null) {
                        mFilterModel.setmFinalEffectState(mFinalEffectState);
                        mFilterModel.setFilterName(mFilterName);
                    }
                    currentFilterName = filterName;
                    displayFilterName(position, isLeftSlide);
                }
            }

            private /* synthetic */ void lambda$onClickForGroupNameClick$1(int position) {
                android.view.View viewByPosition = mFilterListLayoutManager.findViewByPosition(position);
                LogUtil.d("onClickForGroupNameClick", String.format("viewByPosition position : %s ,", position));
                if (viewByPosition != null) {
                    filter_list_recyclerview.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                    LogUtil.d("onClickForGroupNameClick", String.format("position : %s , offsetX : %s ", position, Integer.valueOf(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)))));
                }
            }

            public void onUndoReset(final int position, String filterName) {
                setFilterGroupNameState(position);
                LogUtil.d("filterAdapter", "mFilterName : " + mFilterName + " filterName : " + filterName);
                mFilterName = filterName;
                LogUtil.d("adapterOnclick", "position : " + position);
                filterAdapter.notifyDataSetChanged();
                filter_list_recyclerview.scrollToPosition(position);
                //filter_list_recyclerview.post(FilterActivity$49$.Lambda.3.lambdaFactory$(this, position));
                filter_list_recyclerview.post(new Runnable() {
                    @Override
                    public void run() {
                        lambda$onUndoReset$2(position);
                    }
                });
                if (mFilterModel != null) {
                    mFilterModel.setmFinalEffectState(mFinalEffectState);
                    mFilterModel.setFilterName(mFilterName);
                }
                currentFilterName = filterName;
                if (!isPressedVideoArea) {
                    filterChoosePosition = position;
                    if (isChangeFilterCombination) {
                        isChangeFilterCombination = false;
                    } else if (!(text_draw_rl.getVisibility() == View.VISIBLE || displayUndoFilter)) {
                        showFilterNameInPreview(filterChoosePosition);
                    }
                }
                displayUndoFilter = false;
            }

            private /* synthetic */ void lambda$onUndoReset$2(int position) {
                android.view.View viewByPosition = mFilterListLayoutManager.findViewByPosition(position);
                if (viewByPosition != null) {
                    filter_list_recyclerview.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                }
            }
        });
    }

    private void displayFilterName(int position, boolean isLeftSlide) {
        if (!this.isPressedVideoArea) {
            this.leftSlide = isLeftSlide;
            this.filterChoosePosition = position;
            if (this.text_draw_rl.getVisibility() != View.VISIBLE) {
                showFilterNameInPreview(this.filterChoosePosition);
            }
            setNormalFilter();
            setUndoData(0);
        }
    }

    private void setPreviewParent() {
        initFilterGroupNameInfo();
        initFilterListInfo();
    }

    protected void onPause() {
        App.onActivityPausePenaltyArea(getActivity());
        super.onPause();
        this.mIsPause = true;
        this.hadInitializeFrameTexure = false;
//        MobclickAgent.onPageEnd(FilterActivity.class.getSimpleName());
//        MobclickAgent.onPause(this);
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVideoFrames(null);
        }
        if (!(2 != this.mDataType || this.isIntentText || this.mSurfaceView == null)) {
            this.mSurfaceView.stopLongVideo();
        }
        this.isIntentText = false;
        if (this.mVideoAudioManager != null) {
            this.mVideoAudioManager.getImageCacheUtils().pause();
            this.mVideoAudioManager.getWaveformCacheUtils().pause();
        }
        videoOptDialogOnPause();
        System.gc();
    }

    private void videoOptDialogOnPause() {
//        if (this.mVideoOptDialog != null && this.mVideoOptDialog.isShowing()) {
//            LongVideosModel model = this.mVideoOptDialog.getModel();
//            if (model != null) {
//                model.stopCurrentClient();
//            }
            dismissVideoOptDialog();
//        }
        Toast.makeText(getActivity(),"videoOptDialogOnPause",Toast.LENGTH_LONG).show();
    }

    protected void onRestart() {
        super.onRestart();
        if (this.mDataType == 2) {
        }
    }

    protected void onResume() {
        long t1 = System.nanoTime();
        App.onActivityResumePenaltyArea(getActivity());
        super.onResume();
        this.isBackOrNextPressed = false;
        this.isPressedVideoArea = false;
//        MobclickAgent.onPageStart(FilterActivity.class.getSimpleName());
//        MobclickAgent.onResume(this);
//        NotificationClickReceiver.clearNotification(this);
        saving(true);
        if (this.va != null && this.isPause2Publish && this.hadStartStablize) {
            this.va.resume();
            this.isPause2Publish = false;
        }
        if (this.mRestart && this.mVideoFrames != null && this.mVideoFrames.size() > 0) {
            this.mTvSave.setEnabled(true);
            this.mSurfaceView.setVideoFrames(this.mVideoFrames);
            this.mSurfaceView.setViewOffset(this.mOffsetX, this.mOffsetY);
            this.mSurfaceView.waitForFrame();
            setNormalFilter();
            if (this.mFilterModel != null && this.mFilterModel.getPlayType() == 3333) {
                this.mVideoFrames.setPlayType(3333);
                slideLoop(-2, false);
                if (this.va != null && this.hadStartStablize) {
                    createTransformMethod();
                }
                this.mSurfaceView.setTransforms(this.transformsStraight);
            } else if (this.mVideoFrames.mIsLooped) {
                slideLoop(this.loopStart, this.loopEnd);
                if (this.va != null && this.hadStartStablize) {
                    createTransformMethod();
                }
                this.mSurfaceView.setTransforms(this.transformsLooped);
            } else {
                slideUnloop(this.unloopStart, this.unloopEnd, false, false);
                if (this.va != null && this.hadStartStablize) {
                    createTransformMethod();
                }
                this.mSurfaceView.setTransforms(this.transformsStraight);
            }
            this.mSurfaceView.start();
        }
        if (2 == this.mDataType) {
            if (this.mSurfaceView != null) {
                this.mSurfaceView.setPlaybackPercent(this.playbackPercent);
                this.mSurfaceView.shouldInitPlayer = true;
                if (this.mSurfaceView.getLongVideoPlayState()) {
                    this.mSurfaceView.setVideoPauseOrResume(true);
                }
            }
            this.mIsPause = false;
        }
        if (this.mVideoAudioManager != null) {
            this.mVideoAudioManager.getImageCacheUtils().resume();
            this.mVideoAudioManager.getWaveformCacheUtils().resume();
        }
    }

    private synchronized void setSpecialFilter() {
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            setVideoFilter(false);
        } else {
            GPUImageLookupFilter2 mLUTFilter = new GPUImageLookupFilter2();
            if (this.mLUTTexture == null) {
                this.mLUTTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
            }
            mLUTFilter.setExternalTexture(this.mLUTTexture.texid());
            GPUImageFilter filterGroup = collapseFilters(getSpecialSelectedFilter(mLUTFilter, this.mLUTTexture, this.mFilterName, this.strengthValue));
            if (this.mDataType != 2) {
                setNormalFilters(filterGroup);
            }
        }
    }

    private synchronized void setVideoFilter(boolean isNormal) {
        List<LongVideosModel> videoModels = this.mVideoAudioManager.getVideosModelList();
        boolean addedPublicFilter = false;
        List<Integer> publicArray = new ArrayList();
        HashMap<Integer, GPUImageFilter> privateArray = new HashMap();
        HashMap<String, Integer> privateCache = new HashMap();
        String publicFilterName = "";
        String privateFilterName = "";
        GPUImageFilterGroup publicFilter = null;
        int size = videoModels.size();
        for (int i = 0; i < size; i++) {
            LongVideosModel videoModel = videoModels.get(i);
            GPUImageLookupFilter2 mLutFilter;
            FramebufferTexture mLUTTexture;
            if (videoModel.getFilterState() == 0) {
                publicArray.add(Integer.valueOf(i));
                if (!addedPublicFilter) {
                    addedPublicFilter = true;
                    publicFilterName = videoModel.getPublicFilterName();
                    mLutFilter = new GPUImageLookupFilter2();
                    mLUTTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
                    mLutFilter.setExternalTexture(mLUTTexture.texid());
                    float publicIntensity = videoModel.getPublicIntensity();
                    new GPUImageFilterGroup().addFilter(getDefaultVideoFilter(false));
                    if (isNormal) {
                        publicFilter = getNormalSelectedFilter(mLutFilter, mLUTTexture, publicFilterName, publicIntensity, videoModel);
                    } else {
                        publicFilter = getSpecialSelectedFilter(mLutFilter, mLUTTexture, publicFilterName, publicIntensity, videoModel);
                    }
                }
            } else if (videoModel.getFilterState() == 1) {
                privateFilterName = videoModel.getPrivateFilterName();
                if (privateCache.containsKey(privateFilterName)) {
                    GPUImageFilter filter = privateArray.get(Integer.valueOf(((Integer) privateCache.get(privateFilterName)).intValue()));
                    privateArray.put(Integer.valueOf(i), filter);
                } else {
                    GPUImageFilterGroup privateFilter;
                    privateCache.put(privateFilterName, Integer.valueOf(i));
                    mLutFilter = new GPUImageLookupFilter2();
                    mLUTTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
                    mLutFilter.setExternalTexture(mLUTTexture.texid());
                    float privateIntensity = videoModel.getPrivateIntensity();
                    new GPUImageFilterGroup().addFilter(getDefaultVideoFilter(false));
                    if (isNormal) {
                        privateFilter = getNormalSelectedFilter(mLutFilter, mLUTTexture, privateFilterName, privateIntensity, videoModel);
                    } else {
                        privateFilter = getSpecialSelectedFilter(mLutFilter, mLUTTexture, privateFilterName, privateIntensity, videoModel);
                    }
                    privateArray.put(Integer.valueOf(i), privateFilter);
                }
            } else {
                continue;
            }
        }
        privateCache.clear();
        this.mSurfaceView.changePlaylistFilters(publicFilter, publicArray, privateArray);
    }

    private boolean isFilterGlobalMode() {
        return getCurrentVideoModel() != null && getCurrentVideoModel().getFilterState() == 0;
    }

    private void setFilterNameForLongVideosModel() {
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            FilterInfo filterInfo = App.mFilterMap.get(this.mFilterName);
            if (filterInfo == null) {
                return;
            }
            if (isFilterGlobalMode()) {
                Iterator it = this.mVideoAudioManager.getVideosModelList().iterator();
                while (it.hasNext()) {
                    ((LongVideosModel) it.next()).setPublicFilterName(filterInfo.name_en);
                }
                return;
            }
            LongVideosModel currentVideoModel = getCurrentVideoModel();
            if (currentVideoModel != null) {
                currentVideoModel.setPrivateFilterName(filterInfo.name_en);
            }
        }
    }

    private synchronized void setNormalFilter() {
        if (FilterViewUtils.isVideoModel(this.mDataType)) {
            setVideoFilter(true);
        } else {
            GPUImageLookupFilter2 mLUTFilter = new GPUImageLookupFilter2();
            if (this.mLUTTexture == null) {
                this.mLUTTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
            }
            mLUTFilter.setExternalTexture(this.mLUTTexture.texid());
            GPUImageFilter filterGroup = collapseFilters(getNormalSelectedFilter(mLUTFilter, this.mLUTTexture, this.mFilterName, this.strengthValue));
            if (this.mDataType != 2) {
                setNormalFilters(filterGroup);
            }
        }
    }

    private void setVideoAnimationFilter(int effectState, List<LongVideosModel> videoModels, int position) {
        String filterName;
        float filterIntensity;
        GPUImageLookupFilter2 mLutFilter = new GPUImageLookupFilter2();
        FramebufferTexture mLutTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
        mLutFilter.setExternalTexture(mLutTexture.texid());
        LongVideosModel model = videoModels.get(position);
        List<Integer> filterIndexList = new ArrayList();
        if (model.getFilterState() == 0) {
            filterName = model.getPublicFilterName();
            filterIntensity = model.getPublicIntensity();
            int size = videoModels.size();
            for (int i = 0; i < size; i++) {
                if (videoModels.get(i).getFilterState() == 0) {
                    filterIndexList.add(Integer.valueOf(i));
                }
            }
        } else {
            filterIndexList.add(Integer.valueOf(position));
            filterName = model.getPrivateFilterName();
            filterIntensity = model.getPrivateIntensity();
        }
        this.mSurfaceView.setLongVideoFilterWithAnimation(collapseFilters(getCurrentSelectedFilter(mLutTexture, mLutFilter, effectState, filterName, filterIntensity)), !this.leftSlide, filterIndexList);
    }

    private void setSelectedFilter(int effectState) {
        GPUImageLookupFilter2 mLUTFilter = new GPUImageLookupFilter2();
        FramebufferTexture mLUTTexture = this.mSurfaceView.newFramebufferTexture(1024, 32);
        mLUTFilter.setExternalTexture(mLUTTexture.texid());
        long t1 = System.nanoTime();
        GPUImageFilter filterGroup = getCurrentSelectedFilter(mLUTTexture, mLUTFilter, effectState, this.mFilterName, this.strengthValue);
        long t2 = System.nanoTime();
        filterGroup = collapseFilters(filterGroup);
        long t3 = System.nanoTime();
        if (this.mDataType != 2) {
            setFilters(filterGroup);
        }
        long t4 = System.nanoTime();
        LogUtil.d("ChangFilterTime", String.format("get filters time : %s", Long.valueOf((t2 - t1) / 1000000)));
        LogUtil.d("ChangFilterTime", String.format("sort out filters time : %s", Long.valueOf((t3 - t2) / 1000000)));
        LogUtil.d("ChangFilterTime", String.format("set filters time : %s", Long.valueOf((t4 - t3) / 1000000)));
    }

    GPUImageFilter collapseFilters(GPUImageFilter filter) {
        if (!(filter instanceof GPUImageFilterGroup)) {
            return filter;
        }
        GPUImageFilterGroup group = (GPUImageFilterGroup) filter;
        if (group.mFilters.size() == 1) {
            return collapseFilters((GPUImageFilter) group.mFilters.get(0));
        }
        return filter;
    }

    private void getSaveFilterGroup(GPUImageFilterGroup group, FilterEffectBean bean, GPUImageFilter mFinalFilter, float strengthValue) {
        getSaveFilterGroup(group, bean, mFinalFilter, strengthValue, null);
    }

    private void getSaveFilterGroup(GPUImageFilterGroup group, FilterEffectBean bean, GPUImageFilter mFinalFilter, float strengthValue, LongVideosModel videoModel) {
        LogUtil.d(String.format("final Filter Effect Name is %s", new Object[]{bean.effectType}));
        float value;
        if (bean.effectType == FilterEffectManager.EffectType.STRENGTH) {
            mFinalFilter.setIntensity(strengthValue);
            group.addFilter(mFinalFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.EXPOSURE) {
            GPUImageToneCurveFilter exposureFilter = getToneCurveFilter();
            value = bean.value[1] / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = this.filter_custom_sk.getProgressFloat() / 5.0f;
            }
            value *= 0.12f;
            if (videoModel != null) {
                value += (videoModel.getVideoExposure() / 5.0f) * 0.12f;
            }
            exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + value), new PointF(1.0f, 1.0f)});
            group.addFilter(exposureFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.CONTRAST) {
            GPUImageToneCurveFilter contrastFilter = getToneCurveFilter();
            value = bean.value[1] / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = this.filter_custom_sk.getProgressFloat() / 5.0f;
            }
            value *= 0.12f;
            contrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - value), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f + value), new PointF(1.0f, 1.0f)});
            group.addFilter(contrastFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.SATURATION) {
            GPUImageSaturationFilter saturationFilter = getSaturationFilter();
            value = bean.value[1] / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = this.filter_custom_sk.getProgressFloat() / 5.0f;
            }
            saturationFilter.setSaturation((0.6f * value) + 1.0f);
            group.addFilter(saturationFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.TEMPERATURE || bean.effectType == FilterEffectManager.EffectType.TINGE) {
            if (!this.addFinalWhiteBalanceFilter) {
                LogUtil.d(String.format("final Filter Effect Name is %s", new Object[]{bean.effectType}));
                this.addFinalWhiteBalanceFilter = true;
                GPUImageWhiteBalanceFilter whiteBalanceFilter = getWhiteBalanceFilter();
                float sValue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.TEMPERATURE).value[1];
                if (isCurrentTypeAndShow(bean.effectType)) {
                    sValue = this.filter_custom_sk.getProgressFloat();
                }
                sValue /= 5.0f;
                if (sValue < StaticLayoutUtil.DefaultSpacingadd) {
                    sValue = (500.0f * sValue) + 5000.0f;
                } else {
                    sValue = (1500.0f * sValue) + 5000.0f;
                }
                whiteBalanceFilter.setTemperature(sValue);
                LogUtil.d(String.format("whiteBalanceFilter final value : %s", new Object[]{Float.valueOf(whiteBalanceFilter.getTemperatureValue())}));
                float tValue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.TINGE).value[1];
                if (isCurrentTypeAndShow(bean.effectType)) {
                    tValue = this.filter_custom_sk.getProgressFloat();
                }
                whiteBalanceFilter.setTint((tValue / 5.0f) * 40.0f);
                group.addFilter(whiteBalanceFilter);
            }
        } else if (bean.effectType == FilterEffectManager.EffectType.HIGHLIGHT) {
            GPUImageToneCurveFilter highLightFilter = getToneCurveFilter();
            value = (bean.value[1] - 5.0f) / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = (this.filter_custom_sk.getProgressFloat() - 5.0f) / 5.0f;
            }
            value = (1.0f + value) / 2.0f;
            highLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * value)), new PointF(0.75f, 0.75f - (0.13f * value)), new PointF(1.0f, 1.0f - (0.23f * value))});
            group.addFilter(highLightFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.SHADOW) {
            GPUImageToneCurveFilter shadowFilter = getToneCurveFilter();
            value = (bean.value[1] - 5.0f) / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = (this.filter_custom_sk.getProgressFloat() - 5.0f) / 5.0f;
            }
            value = (1.0f + value) / 2.0f;
            shadowFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.14f * value), new PointF(0.25f, 0.25f + (0.13f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f), new PointF(1.0f, 1.0f)});
            group.addFilter(shadowFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.FADED) {
            GPUImageToneCurveFilter fadeFilter = getToneCurveFilter();
            value = (bean.value[1] - 5.0f) / 5.0f;
            if (isCurrentTypeAndShow(bean.effectType)) {
                value = (this.filter_custom_sk.getProgressFloat() - 5.0f) / 5.0f;
            }
            value = (1.0f + value) / 2.0f;
            fadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * value), new PointF(0.25f, 0.25f + (0.09f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * value)), new PointF(1.0f, 1.0f - (0.15f * value))});
            group.addFilter(fadeFilter);
        }
    }

    private void addFilter2Group(GPUImageFilterGroup group, FilterEffectBean bean, float filterIntensity) {
        if (bean.effectType == FilterEffectManager.EffectType.STRENGTH) {
            this.currentFilter.setIntensity(filterIntensity);
            if (this.filterInfo == null || !(this.filterInfo.filter_id == 7 || this.filterInfo.isBnW)) {
                if (this.currentFilter instanceof GPUImageLookupFilter2) {
                    ((GPUImageLookupFilter2) this.currentFilter).setIsBnW(StaticLayoutUtil.DefaultSpacingadd);
                }
            } else if (this.currentFilter instanceof GPUImageLookupFilter2) {
                ((GPUImageLookupFilter2) this.currentFilter).setIsBnW(1.0f);
            }
            group.addFilter(this.currentFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.EXPOSURE) {
            group.addFilter(this.exposureFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.CONTRAST) {
            group.addFilter(this.contrastFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.SATURATION) {
            group.addFilter(this.saturationFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.TEMPERATURE || bean.effectType == FilterEffectManager.EffectType.TINGE) {
            if (!this.hadAddWhiteBalanceFilter) {
                this.hadAddWhiteBalanceFilter = true;
                group.addFilter(this.whiteBalanceFilter);
            }
        } else if (bean.effectType == FilterEffectManager.EffectType.HIGHLIGHT) {
            group.addFilter(this.highLightFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.SHADOW) {
            group.addFilter(this.shadowFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.FADED) {
            group.addFilter(this.fadeFilter);
        }
    }

    private void getNonLUTfilters(GPUImageFilterGroup filterGroup, FilterEffectBean bean) {
        if (bean.effectType == FilterEffectManager.EffectType.BEAUTIFY) {
            filterGroup.addFilter(this.mBilateralFilter);
        }
        if (bean.effectType == FilterEffectManager.EffectType.ROTATE || bean.effectType == FilterEffectManager.EffectType.VERTICAL || bean.effectType == FilterEffectManager.EffectType.HORIZONTAL) {
            if (this.transformFilter != null && !this.hadAddTransformFilter) {
                this.hadAddTransformFilter = true;
                LogUtil.d("TransformFilter", "addFilter");
                this.transformFilter.setVideoRatio(this.mVideoFrames.mCropWidth, this.mVideoFrames.mCropHeight);
                filterGroup.addFilter(this.transformFilter);
            }
        } else if (bean.effectType == FilterEffectManager.EffectType.SKY) {
            filterGroup.addFilter(this.skyFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.GRAIN) {
            filterGroup.addFilter(this.grainFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.TILT) {
            filterGroup.addFilter(this.tiltFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.VIGNETTE) {
            filterGroup.addFilter(this.movieFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.LEAK) {
            filterGroup.addFilter(this.leakFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.CROP) {
            filterGroup.addFilter(this.gpuImageCropFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.MIRROR) {
            filterGroup.addFilter(this.mGPUImageMirrorFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.SHARPEN) {
            filterGroup.addFilter(this.sharpenFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.DATE) {
            if (bean.getItemSelect()) {
                filterGroup.addFilter(this.dateFilter);
            }
        } else if (bean.effectType == FilterEffectManager.EffectType.DUST) {
            filterGroup.addFilter(this.dustFilter);
        } else if (bean.effectType == FilterEffectManager.EffectType.PRISM) {
            filterGroup.addFilter(this.prismFilter);
        }
    }

    private GPUImageFilterGroup getSpecialSelectedFilter(GPUImageLookupFilter2 mLUTFilter, FramebufferTexture mLUTTexture, String filterName, float filterIntensity) {
        return getSpecialSelectedFilter(mLUTFilter, mLUTTexture, filterName, filterIntensity, null);
    }

    private GPUImageFilterGroup getSpecialSelectedFilter(GPUImageLookupFilter2 mLUTFilter, FramebufferTexture mLUTTexture, String filterName, float filterIntensity, LongVideosModel videoModel) {
        int i;
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        GPUImageFilterGroup group = new GPUImageFilterGroup();
        this.filterInfo = (FilterInfo) App.mFilterMap.get(filterName);
        if (this.filterInfo == null || this.filterInfo.filter == null) {
            this.currentFilter = new GPUImageFilter();
        } else {
            this.currentFilter = this.filterInfo.filter;
        }
        List mList = this.mFilterEffectManager.getSpecialShowFilterEffectList(this.currentEffectType);
        if (videoModel != null) {
            float videoExposure = videoModel.getVideoExposure();
            if (videoExposure != StaticLayoutUtil.DefaultSpacingadd) {
                if (!this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.EXPOSURE)) {
                    mList.add(this.mFilterEffectManager.getDefaultBean(FilterEffectManager.EffectType.EXPOSURE));
                }
                videoExposure = (videoExposure / 5.0f) * 0.12f;
                if (this.exposureFilter == null) {
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + videoExposure), new PointF(1.0f, 1.0f)});
                } else {
                    float exposureFileCurrentValue = this.mFilterEffectManager.getExposureFileCurrentValue();
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, (0.5f + videoExposure) + exposureFileCurrentValue), new PointF(1.0f, 1.0f)});
                }
            }
        }
        Collections.sort(mList, new FilterEffectCompare());
        int beanSize = mList.size();
        long t1 = System.nanoTime();
        for (i = 0; i < beanSize; i++) {
            getNonLUTfilters(filterGroup, (FilterEffectBean) mList.get(i));
        }
        this.hadAddTransformFilter = false;
        long t2 = System.nanoTime();
        for (i = 0; i < beanSize; i++) {
            addFilter2Group(group, (FilterEffectBean) mList.get(i), filterIntensity);
        }
        this.hadAddWhiteBalanceFilter = false;
        long t3 = System.nanoTime();
        LogUtil.d(String.format("finalFilter's size : %s", new Object[]{Integer.valueOf(filterGroup.mFilters.size())}));
        long t4 = System.nanoTime();
        if (this.filterInfo == null || !(this.filterInfo.filter_id == 7 || this.filterInfo.isBnW)) {
            mLUTFilter.setIsBnW(StaticLayoutUtil.DefaultSpacingadd);
        } else {
            mLUTFilter.setIsBnW(1.0f);
        }
        this.mSurfaceView.updateLUT(group, mLUTTexture);
        filterGroup.addFilter(mLUTFilter);
        mList.clear();
        collapseFilters(filterGroup);
        return filterGroup;
    }

    @NonNull
    private GPUImageFilterGroup getNormalSelectedFilter(GPUImageLookupFilter2 mLUTFilter, FramebufferTexture mLUTTexture, String filterName, float filterIntensity) {
        return getNormalSelectedFilter(mLUTFilter, mLUTTexture, filterName, filterIntensity, null);
    }

    @NonNull
    private GPUImageFilterGroup getNormalSelectedFilter(GPUImageLookupFilter2 mLUTFilter, FramebufferTexture mLUTTexture, String filterName, float filterIntensity, LongVideosModel videoModel) {
        int i;
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        GPUImageFilterGroup group = new GPUImageFilterGroup();
        this.filterInfo = (FilterInfo) App.mFilterMap.get(filterName);
        if (this.filterInfo == null || this.filterInfo.filter == null) {
            this.currentFilter = new GPUImageFilter();
        } else {
            this.currentFilter = this.filterInfo.filter;
        }
        List mList = this.mFilterEffectManager.getShowFilterEffectList(this.currentEffectType);
        if (videoModel != null) {
            float videoExposure = videoModel.getVideoExposure();
            if (videoExposure != StaticLayoutUtil.DefaultSpacingadd) {
                if (!this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.EXPOSURE)) {
                    mList.add(this.mFilterEffectManager.getDefaultBean(FilterEffectManager.EffectType.EXPOSURE));
                }
                videoExposure = (videoExposure / 5.0f) * 0.12f;
                if (this.exposureFilter == null) {
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + videoExposure), new PointF(1.0f, 1.0f)});
                } else {
                    float exposureFileCurrentValue = this.mFilterEffectManager.getExposureFileCurrentValue();
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, (0.5f + videoExposure) + exposureFileCurrentValue), new PointF(1.0f, 1.0f)});
                }
            }
        }
        Collections.sort(mList, new FilterEffectCompare());
        int beanSize = mList.size();
        for (i = 0; i < beanSize; i++) {
            getNonLUTfilters(filterGroup, (FilterEffectBean) mList.get(i));
        }
        this.hadAddTransformFilter = false;
        long t2 = System.nanoTime();
        for (i = 0; i < beanSize; i++) {
            addFilter2Group(group, (FilterEffectBean) mList.get(i), filterIntensity);
        }
        this.hadAddWhiteBalanceFilter = false;
        if (this.filterInfo == null || !(this.filterInfo.filter_id == 7 || this.filterInfo.isBnW)) {
            mLUTFilter.setIsBnW(StaticLayoutUtil.DefaultSpacingadd);
        } else {
            mLUTFilter.setIsBnW(1.0f);
        }
        this.mSurfaceView.updateLUT(group, mLUTTexture);
        filterGroup.addFilter(mLUTFilter);
        mList.clear();
        collapseFilters(filterGroup);
        return filterGroup;
    }

    @NonNull
    private GPUImageFilterGroup getCurrentSelectedFilter(FramebufferTexture mLUTTexture, GPUImageLookupFilter2 mLUTFilter, int effectState, String filterName, float strengthValue) {
        int i;
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        GPUImageFilterGroup group = new GPUImageFilterGroup();
        FilterInfo filterInfo = (FilterInfo) App.mFilterMap.get(filterName);
        if (filterInfo == null || filterInfo.filter == null) {
            this.currentFilter = new GPUImageFilter();
        } else {
            this.currentFilter = filterInfo.filter;
        }
        List<FilterEffectBean> mList = this.mFilterEffectManager.getShowFilterEffectList(this.currentEffectType);
        Collections.sort(mList, new FilterEffectCompare());
        int beanSize = mList.size();
        long t1 = System.nanoTime();
        for (i = 0; i < beanSize; i++) {
            getNonLUTfilters(filterGroup, (FilterEffectBean) mList.get(i));
        }
        this.hadAddTransformFilter = false;
        long t2 = System.nanoTime();
        for (i = 0; i < beanSize; i++) {
            addFilter2Group(group, (FilterEffectBean) mList.get(i), strengthValue);
        }
        this.hadAddWhiteBalanceFilter = false;
        long t3 = System.nanoTime();
        this.mSurfaceView.updateLUT2(group, mLUTTexture);
        long t4 = System.nanoTime();
        filterGroup.addFilter(mLUTFilter);
        mList.clear();
        LogUtil.d("LUTFiltersAndOthrer", String.format("get non-lutFilters time : %s", (t2 - t1) / 1000000));
        LogUtil.d("LUTFiltersAndOthrer", String.format("get lutFilters time : %s", (t3 - t2) / 1000000));
        LogUtil.d("LUTFiltersAndOthrer", String.format("merge lutFilters time : %s", (t4 - t3) / 1000000));
        collapseFilters(filterGroup);
        return filterGroup;
    }

    public void onDestroy() {
//        BuglyLogUtil.writeBuglyLog(TAG);
        App.UnregisterEventBus(this);
        releaseViewHolder();
//        if (this.mVideoMusicDialog != null) {
//            this.mVideoMusicDialog.release();
//        }
//        if (this.mVideoOptDialog != null) {
//            this.mVideoOptDialog.release();
//            this.mVideoOptDialog = null;
//        }
        super.onDestroy();
        destroyTempBuffers();
        if (this.mLUTTexture != null) {
            this.mSurfaceView.destroyFramebufferTexture(this.mLUTTexture);
            this.mLUTTexture = null;
        }
        this.mSurfaceView.destroyCreator();
        if (this.alpha_0 != null) {
            this.alpha_0.destroy();
            this.alpha_0.destroySecondary();
            this.alpha_0 = null;
        }
        if (this.alpha_1 != null) {
            this.alpha_1.destroy();
            this.alpha_1.destroySecondary();
            this.alpha_1 = null;
        }
        recycleBitmap();
        if (this.filter_list_recyclerview != null) {
            this.filter_list_recyclerview.setAdapter(null);
            this.filter_list_recyclerview = null;
        }
        ImagePropertyBean.getInstance().clearBitmapArrayData();
        destoryFilters();
        this.mFilterListLayoutManager = null;
        if (this.mVideoAudioManager != null) {
            this.mVideoAudioManager.recyclePicBitmap();
            this.mVideoAudioManager.destroyManager();
        }
        VideoBitmapsModel.instancesModel().recyclerBitmap();
        System.gc();
    }

    private void dFilter(GPUImageFilter filter) {
        if (filter != null) {
            this.mSurfaceView.destroyFilter(filter);
        }
    }

    private void destoryFilters() {
        dFilter(this.whiteBalanceFilter);
        dFilter(this.saturationFilter);
        dFilter(this.fadeFilter);
        dFilter(this.highLightFilter);
        dFilter(this.shadowFilter);
        dFilter(this.contrastFilter);
        dFilter(this.exposureFilter);
        dFilter(this.grainFilter);
        dFilter(this.leakFilter);
        dFilter(this.dustFilter);
        dFilter(this.prismFilter);
        dFilter(this.transformFilter);
        dFilter(this.skyFilter);
        dFilter(this.mBilateralFilter);
        dFilter(this.movieFilter);
        dFilter(this.tiltFilter);
        dFilter(this.gpuImageCropFilter);
        dFilter(this.mGPUImageMirrorFilter);
    }

    private void createTransformMethod() {
        LogUtil.d("transformNeedCount", "callbackCount : " + this.transformCallbackCount + " , transformNeedCount : " + this.transformNeedCount);
        int[] ints;
        if (this.transformsStraight == null) {
            ints = new int[]{0};
            LogUtil.d("transformNeedCount", "callbackCount == 1");
            this.transformsStraight = this.va.createTransforms(false, ints);
        } else if (this.transformsStraight.length < this.transformNeedCount && this.transformNeedCount <= this.transformCallbackCount) {
            int forSize = this.transformNeedCount;
            ints = new int[forSize];
            for (int i = 0; i < forSize; i++) {
                ints[i] = i;
            }
            LogUtil.d("transformNeedCount", "callbackCount != 1");
            this.transformsStraight = this.va.createTransforms(false, ints);
            refreshTransform();
        }
    }

    private void loadFrames() {
//        BuglyLogUtil.writeBuglyLog(TAG);
        this.loadFramesThread = new Thread() {
            public void run() {
                mVideoFrames.limitDuration((long) (mFrameCount * 100000));
                if (mVideoFrames != null) {
                    if (mIsShort) {
                        slideLevel = mFilterModel.getSlideLevel();
                        unloopEnd = mFilterModel.getUnloopEnd();
                        unloopStart = mFilterModel.getUnloopStart();
                        if (mFilterModel.getLoopStart() < mFilterModel.getLoopEnd()) {
                            loopStart = mFilterModel.getLoopStart();
                            if (mFilterModel.getLoopEnd() != 0) {
                                loopEnd = mFilterModel.getLoopEnd();
                            }
                            if (loopEnd > mVideoFrames.size2() - 1) {
                                loopEnd = mVideoFrames.size2() - 1;
                            }
                        }
                        if (slideLevel > 0 && slideLevel + 5 < mVideoFrames.size2()) {
                            loopStart = slideLevel;
                            loopEnd = loopStart + 5;
                        }
                        mVideoFrames.toggleLoop(loopStart, loopEnd);
                        mIsVideoLooped = mVideoFrames.mIsLooped;
                    }
                    //EGL10Helper.withContext("loadFrames", FilterActivity$50$.Lambda.1.lambdaFactory$(this));
                    EGL10Helper.withContext("loadFrames", new EGLRunnableVoid() {
                        @Override
                        public void run(EGL10Helper eGL10Helper) {
                            lambda$run$2(eGL10Helper);
                        }
                    });
                }
            }

            private /* synthetic */ void lambda$run$2(EGL10Helper egl) {
                try {
                    float previewWidth;
                    float previewHeight;
                    int i;
                    int shortLength = DensityUtil.dip2px(44.0f);
                    if (mVideoHeight > mVideoWidth) {
                        previewWidth = (float) shortLength;
                        previewHeight = (((float) mVideoHeight) * previewWidth) / ((float) mVideoWidth);
                    } else {
                        previewHeight = (float) shortLength;
                        previewWidth = (((float) mVideoWidth) * previewHeight) / ((float) mVideoHeight);
                    }
                    GPUImageFilter gpuImageFilter = new GPUImageFilter();
                    if (mIsShort) {
                        Log.d(FilterActivity.TAG, String.format("foo2: %d", new Object[]{Integer.valueOf(egl.numSurfaces())}));
                        forFilterBitmap = (Bitmap) mVideoFrames.getBitmaps((int) previewWidth, (int) previewHeight, new GPUImageFilter[]{gpuImageFilter}, new int[]{0}, true).get(0);
                        forFilterBitmap = getRatioBitmap(forFilterBitmap);
                        Log.d(FilterActivity.TAG, String.format("foo3: %d", new Object[]{Integer.valueOf(egl.numSurfaces())}));
                    } else {
                        int count = ((int) ((((float) (metricsWidth - (DensityUtil.dip2px(64.0f) * 2))) * 1.0f) / ((float) shortLength))) + 1;
                        int originalSize = mVideoFrames.originalSize();
                        i = 0;
                        while (i < count && ((int) (((((float) (mVideoFrames.size() * i)) * 1.0f) / ((float) count)) + 0.5f)) <= originalSize) {
                            int index = i;
                            Bitmap preBitmap = getRatioBitmap((Bitmap) mVideoFrames.getBitmaps((int) previewWidth, (int) previewHeight, new GPUImageFilter[]{gpuImageFilter}, new int[]{index}, true).get(0));
                            final ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(shortLength, shortLength));
                            imageView.setScaleType(ScaleType.CENTER_CROP);
                            imageView.setImageBitmap(preBitmap);
                            //runOnUiThread(FilterActivity$50$.Lambda.2.lambdaFactory$(this, imageView));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lambda$null$0(imageView);
                                }
                            });
                            if (i == 0) {
                                forFilterBitmap = preBitmap;
                            }
                            i++;
                        }
                    }
                    Log.d(FilterActivity.TAG, String.format("foo4: %d", Integer.valueOf(egl.numSurfaces())));
                    LogUtil.d(FilterActivity.TAG, "filterView start time : " + System.nanoTime());
                    mSurfaceView.setVideoFrames(mVideoFrames);
                    mSurfaceView.setViewOffset(mOffsetX, mOffsetY);
                    loadFilterInfos();
                    setNormalFilter();
                    Log.d(FilterActivity.TAG, "waiting first frame");
                    Log.d(FilterActivity.TAG, "got first frame");
                    if (!mRestart) {
                        //runOnUiThread(FilterActivity$50$.Lambda.3.lambdaFactory$(this));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lambda$null$1();
                            }
                        });
                    }
                    if (mVideoFrames == null || mVideoFrames.size() < 3) {
                        generatePreviewBitmaps(forFilterBitmap, 0, mFilterInfos.size(), 0);
                    } else {
                        int historyPos = 1;
                        i = 0;
                        while (i < mFilterInfos.size()) {
                            if (((FilterInfo) mFilterInfos.get(i)).name_en != null && ((FilterInfo) mFilterInfos.get(i)).name_en.equals(mFilterName)) {
                                historyPos = i;
                                break;
                            }
                            i++;
                        }
                        generatePreviewBitmaps(forFilterBitmap, 0, mFilterInfos.size(), historyPos);
                    }
                    System.gc();
                    Log.d(FilterActivity.TAG, String.format("foo5: %d", egl.numSurfaces()));
                    if (mDraftModel == null && mDataFrom == 0 && mDataType == 0) {
                        saveVideoAndPreViewForDraft(egl, false);
                        LogUtil.d("infoinfo", "first saveVideoAndPreViewForDraft");
                    }
                    Log.d(FilterActivity.TAG, String.format("foo6: %d", new Object[]{Integer.valueOf(egl.numSurfaces())}));
                } catch (Exception e) {
                    e.printStackTrace();
                    BitmapUtils.recycleBitmap(forFilterBitmap);
                }
                System.gc();
            }

            private /* synthetic */ void lambda$null$0(ImageView imageView) {
                filter_loop_preview_ll.addView(imageView);
            }

            private /* synthetic */ void lambda$null$1() {
                if (mVideoFrames != null) {
                    if (mDraftModel != null) {
                        restoreFilters();
                    }
                    ViewPropertyAnimator.animate(filter_alpha_view).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(FilterActivity.CHANEG_FILTER_TIME).start();
                    mTvSave.setEnabled(true);
                    mTvSave.setAlpha(1.0f);
                    mRestart = true;
                }
            }
        };
        this.loadFramesThread.start();
    }

    private Bitmap getFilterPreviewDraft() {
        String previewFilePath = this.mDraftModel.previewPath();
        if (!new File(previewFilePath).exists()) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(previewFilePath, options);
        int maxLength = DensityUtil.dip2px(44.0f) * 3;
        options.inSampleSize = OptionSizeUtil.computeSampleSize(options, -1, maxLength * maxLength);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = false;
        options.inPurgeable = true;
        int opWidth = options.outWidth;
        int opHeight = options.outHeight;
        if (opWidth > opHeight) {
            if (opWidth > maxLength) {
                options.outWidth = maxLength;
                options.outHeight = (int) (((float) maxLength) / ((((float) opWidth) * 1.0f) / ((float) opHeight)));
            }
        } else if (opHeight > maxLength) {
            options.outHeight = maxLength;
            options.outWidth = (int) (((float) maxLength) * ((((float) opWidth) * 1.0f) / ((float) opHeight)));
        }
        return BitmapFactory.decodeFile(previewFilePath, options);
    }

    private void generatePreviewBitmaps(Bitmap bitmap, int start, int end, int histroyPos) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        IntBuffer pixels = IntBuffer.allocate(width * height);
        GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(new GPUImageFilter());
        Rotation rotation = Rotation.ROTATION_180;
        if (FilterViewUtils.isPictureModel(this.mDataType)) {
            boolean isBackCamera = this.mIsBackCamera;
            boolean isFromLocal = 1 == this.mDataFrom;
            switch (this.orientation) {
                case 0:
                    if (isBackCamera) {
                        rotation = Rotation.ROTATION_270;
                    } else {
                        rotation = Rotation.ROTATION_90;
                    }
                    if (isFromLocal) {
                        rotation = Rotation.ROTATION_180;
                        break;
                    }
                    break;
                case 90:
                    rotation = Rotation.NORMAL;
                    if (isFromLocal) {
                        rotation = Rotation.ROTATION_180;
                        break;
                    }
                    break;
                case 180:
                    if (isBackCamera) {
                        rotation = Rotation.ROTATION_90;
                    } else {
                        rotation = Rotation.ROTATION_270;
                    }
                    if (isFromLocal) {
                        rotation = Rotation.NORMAL;
                        break;
                    }
                    break;
                case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                    rotation = Rotation.ROTATION_180;
                    if (isFromLocal) {
                        rotation = Rotation.NORMAL;
                        break;
                    }
                    break;
            }
        }
        gPUImageRenderer.setRotation(rotation, true, false);
        gPUImageRenderer.setScaleType(GPUImage.ScaleType.CENTER_CROP);
        gPUImageRenderer.setImageBitmap(bitmap, false);
        gPUImageRenderer.onSurfaceCreated(null, null);
        gPUImageRenderer.onSurfaceChanged(null, width, height);
        HashMap<String, Bitmap> temporaryMap = new HashMap();
        FilterInfo filterInfo;
        final int finalInt;
        GPUImageFilter filter;
        Bitmap preview;
        if (histroyPos > 1) {
            int currentCount = 0;
            boolean needRevert = true;
            boolean startLoadEnd = false;
            boolean endLoadEnd = false;
            while (true) {
                int currentPos = histroyPos;
                if (currentPos <= 0) {
                    currentPos = 0;
                    startLoadEnd = true;
                }
                if (currentPos >= this.mFilterInfos.size() - 1) {
                    currentPos = this.mFilterInfos.size() - 1;
                    endLoadEnd = true;
                }
                filterInfo = (FilterInfo) this.mFilterInfos.get(currentPos);
                if (checkNotFilterType(filterInfo)) {
                    currentCount++;
                    if (needRevert) {
                        histroyPos += currentCount;
                    } else {
                        histroyPos -= currentCount;
                    }
                    needRevert = !needRevert;
                } else {
                    final int ffint = currentPos;
                    if (temporaryMap.size() > 0) {
                        if (temporaryMap.containsKey(filterInfo.name_en)) {
                            filterInfo.bitmap = (Bitmap) temporaryMap.get(filterInfo.name_en);
                            //runOnUiThread(32.lambdaFactory$(this, finalInt));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lambda$generatePreviewBitmaps$30(ffint);
                                }
                            });
                            currentCount++;
                            if (needRevert) {
                                histroyPos += currentCount;
                            } else {
                                histroyPos -= currentCount;
                            }
                            needRevert = !needRevert;
                            if (startLoadEnd && endLoadEnd) {
                            }
                        }
                    }
                    filter = filterInfo.createFilterInstance(getAssets());
                    if (filter != null) {
                        gPUImageRenderer.setFilter(filter);
                        gPUImageRenderer.onDrawFrame(null);
                        pixels.position(0);
                        GLES20.glReadPixels(0, 0, width, height, 6408, 5121, pixels);
                        preview = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        preview.copyPixelsFromBuffer(pixels);
                        filterInfo.bitmap = preview;
                        temporaryMap.put(filterInfo.name_en, preview);
                        //runOnUiThread(33.lambdaFactory$(this, finalInt));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lambda$generatePreviewBitmaps$31(ffint);
                            }
                        });
                        filter.destroy();
                    }
                    currentCount++;
                    if (needRevert) {
                        histroyPos += currentCount;
                    } else {
                        histroyPos -= currentCount;
                    }
                    needRevert = !needRevert;
                    if (startLoadEnd && endLoadEnd) {
                    }
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                filterInfo = (FilterInfo) this.mFilterInfos.get(i);
                if (!checkNotFilterType(filterInfo)) {
                    final int ffi = i;
                    if (temporaryMap.size() > 0) {
                        if (temporaryMap.containsKey(filterInfo.name_en)) {
                            filterInfo.bitmap = (Bitmap) temporaryMap.get(filterInfo.name_en);
                            //runOnUiThread(34.lambdaFactory$(this, finalInt));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lambda$generatePreviewBitmaps$32(ffi);
                                }
                            });
                        }
                    }
                    filter = filterInfo.createFilterInstance(getAssets());
                    if (filter != null) {
                        gPUImageRenderer.setFilter(filter);
                        gPUImageRenderer.onDrawFrame(null);
                        pixels.position(0);
                        GLES20.glReadPixels(0, 0, width, height, 6408, 5121, pixels);
                        preview = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        preview.copyPixelsFromBuffer(pixels);
                        filterInfo.bitmap = preview;
                        temporaryMap.put(filterInfo.name_en, preview);
                        //runOnUiThread(35.lambdaFactory$(this, finalInt));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lambda$generatePreviewBitmaps$33(ffi);
                            }
                        });
                        filter.destroy();
                    }
                }
            }
        }
        temporaryMap.clear();
        gPUImageRenderer.deleteImageNow();
        if (this.mDataType == 2) {
            saveVideoPreviewDraft();
            saveDraft(this.mFilterModel, this.mDraftLongVideoBean, true);
        }
    }

    private /* synthetic */ void lambda$generatePreviewBitmaps$30(int finalInt) {
        this.filterAdapter.notifyItemChanged(finalInt);
    }

    private /* synthetic */ void lambda$generatePreviewBitmaps$31(int finalInt) {
        this.filterAdapter.notifyItemChanged(finalInt);
    }

    private /* synthetic */ void lambda$generatePreviewBitmaps$32(int finalInt) {
        this.filterAdapter.notifyItemChanged(finalInt);
    }

    private /* synthetic */ void lambda$generatePreviewBitmaps$33(int finalInt) {
        this.filterAdapter.notifyItemChanged(finalInt);
    }

    private boolean checkNotFilterType(FilterInfo filterInfo) {
        return filterInfo.isGroup || filterInfo.isDownloading || filterInfo.type == 5;
    }

    private VidStabilizer.Transform[] getTransforms(int[] frames) {
        int length = frames.length;
        VidStabilizer.Transform[] transforms = new VidStabilizer.Transform[length];
        int i = 0;
        while (i < length) {
            if (this.transformsStraight == null || frames[i] >= this.transformsStraight.length) {
                return null;
            }
            transforms[i] = this.transformsStraight[frames[i]];
            if (transforms[i] == null) {
                return null;
            }
            LogUtil.d(TAG, "refresh transform: " + frames[i]);
            i++;
        }
        return transforms;
    }

    private void deletePublishVideo() {
        String videoPath = MovieFileUtil.getOutputMediaFile(this.mCurrentTimeStamp);
        String longPicPath = MovieFileUtil.getLongThumbnailFilePath(this.mCurrentTimeStamp);
        final File videoFile = new File(videoPath);
        final File longPicFile = new File(longPicPath);
        if (videoFile.exists() || longPicFile.exists()) {
            new Thread() {
                public void run() {
                    super.run();
                    if (videoFile.exists()) {
                        videoFile.delete();
                    }
                    if (longPicFile.exists()) {
                        longPicFile.delete();
                    }
                }
            }.start();
        }
    }

    private void clearFrameBuffer() {
        if (this.mVideoFrames != null) {
            this.mVideoFrames.releaseFrames();
            this.mSurfaceView.setVideoFrames(null);
            this.mSurfaceView.releasePool(this.mVideoFrames);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.filter_rule_ll.getVisibility() == View.VISIBLE) {
            onCancelPress(this.currentEffectType);
            return true;
        }
        ImagePropertyBean.getInstance().clear();
        if (this.mDataType == 2) {
            saveLongVideoPreview();
            if (this.mSurfaceView != null) {
                this.mSurfaceView.setVideoPauseOrResume(true);
            }
            onBackPressed();
            EventBus.getDefault().post(new LongVideoBackEvent());
            return true;
        }
        setResult(-1);
        deletePublishVideo();
        if (this.mDataFrom != 0 && 3 != this.mDataFrom) {
            clearFrameBuffer();
        } else if (this.mDataType == 0) {
            saveVideoPreview();
        } else if (2 != this.mDataType) {
            clearFrameBuffer();
        }
        onBackPressed();
        return true;
    }

    private int[] getPublishMediaWidthAndHeight() {
        boolean isPortrait;
        int width;
        int height;
        if (this.orientation == 0 || this.orientation == 180) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }
        if (2 == this.mDataType) {
            width = this.mVideoWidth;
            height = this.mVideoHeight;
        } else if (this.mDataFrom != 0) {
            width = this.mVideoFrames.mCropWidth;
            height = this.mVideoFrames.mCropHeight;
        } else if (this.mCaptureMode == 2) {
            if (isPortrait) {
                width = this.mVideoFrames.mCropHeight;
                height = this.mVideoFrames.mCropWidth;
            } else {
                width = this.mVideoFrames.mCropWidth;
                height = this.mVideoFrames.mCropHeight;
            }
        } else if (isPortrait) {
            width = this.mVideoFrames.mCropHeight;
            height = this.mVideoFrames.mCropWidth;
        } else {
            width = this.mVideoFrames.mCropWidth;
            height = this.mVideoFrames.mCropHeight;
        }
        return new int[]{width, height};
    }

    private void intentPublishActivity(long videoProgressTag, int width, int height) {
        Toast.makeText(getActivity(),"go to PublishActivity",Toast.LENGTH_LONG).show();
//        this.mBundle.putLong("videoprogresstag", videoProgressTag);
//        String value = this.mTextContent.replaceAll("[\\t\\n\\r]", " ");
//        if (TextUtil.isValidate(value.trim())) {
//            while (value.startsWith(" ")) {
//                value = value.substring(1);
//            }
//        }
//        this.mBundle.putString("TextContent", value);
//        Intent intent = new Intent(this, PublishActivity.class);
//        intent.putExtra(DATA_FROM, this.mDataFrom);
//        intent.putExtra(DATA_TYPE, this.mDataType);
//        intent.putExtra(PublishActivity.TIME_STAMP_INTENT, this.mCurrentTimeStamp);
//        intent.putExtra(PublishActivity.CAMERA_TYPE_INTENT, this.mCameraLensType);
//        intent.putExtra("VideoWidthIntent", width);
//        intent.putExtra("VideoHeightIntent", height);
//        intent.putExtra(PublishActivity.INTENT_KEY_CREATION_DATE, this.mCreationDate);
//        intent.putExtra(VideoActivity2.BundleIntent, this.mBundle);
//        if (this.mDataType == 2) {
//            intent.putParcelableArrayListExtra(PublishActivity.LONG_VIDEOMODEL_LIST, this.mVideoAudioManager.getVideosModelList());
//            intent.putExtra(PublishActivity.VIDEO_DURATION, this.mSurfaceView.getPlayer().getPlaylist().getDurationUs() / 1000);
//            intent.putExtra(HAS_AUDIO, getLongVideoHasAudioTrack());
//            UploadVideoAllInfoBean uploadBean = this.mVideoAudioManager.getVideoUploadInfo();
//            uploadBean.setDuration((((((float) this.mSurfaceView.getPlaylistDurationMS()) * 1.0f) / 1000.0f) + 3.0f) + "");
//            uploadBean.setFPS("30");
//            int[] mVideoPreviewWidthHeights = FilterViewUtils.getUploadLongVideoWidthHeight(width, height, false);
//            int mVideoPreviewWidth = mVideoPreviewWidthHeights[0];
//            mVideoPreviewWidth += mVideoPreviewWidth % 2;
//            int mVideoPreviewHeight = mVideoPreviewWidthHeights[1];
//            mVideoPreviewHeight += mVideoPreviewHeight % 2;
//            uploadBean.setSize("{" + mVideoPreviewWidth + "," + mVideoPreviewHeight + "}");
//            uploadBean.setRatio("" + ((((float) mVideoPreviewWidth) * 1.0f) / ((float) mVideoPreviewHeight)));
//            intent.putExtra(PublishActivity.UPLOAD_VIDEO_INFO, JsonParserUtil.serializeToJson(uploadBean));
//        }
//        if (3 == this.mDataFrom) {
//            GlobalLocationManager.getInstance().refreshLocationData(this.mDraftModel.getGlobalLocation());
//            EventBus.getDefault().postSticky(new PublishDraftEvent(this.mDraftModel));
//        }
//        IntentUtil.toPublishActivity(getActivity(), intent);
    }

    private boolean getLongVideoHasAudioTrack() {
        boolean hasAudio = false;
        if (this.mVideoAudioManager.isAllAudioMute() && this.mVideoAudioManager.isAllVideoMute()) {
            return false;
        }
        Iterator it;
        LongVideosModel model;
        if (!this.mVideoAudioManager.isAllAudioMute()) {
            boolean hasMusicModel;
            List<LongVideosModel> musicModels = this.mVideoAudioManager.getMusicModelList();
            if (musicModels == null || musicModels.size() <= 0) {
                hasMusicModel = false;
            } else {
                hasMusicModel = true;
            }
            if (hasMusicModel) {
                for (LongVideosModel model2 : musicModels) {
                    if (model2.getAudioVolume() != StaticLayoutUtil.DefaultSpacingadd) {
                        hasAudio = true;
                        break;
                    }
                }
            }
        }
        if (!this.mVideoAudioManager.isAllVideoMute()) {
            ArrayList<LongVideosModel> videosModels = this.mVideoAudioManager.getVideosModelList();
            if (!hasAudio) {
                it = videosModels.iterator();
                while (it.hasNext()) {
                    LongVideosModel model2 = (LongVideosModel) it.next();
                    if (model2.mediaType == 0 && model2.getVideoVolume() != StaticLayoutUtil.DefaultSpacingadd && hasAudioFormat(model2.getPlaylistMediaPath())) {
                        hasAudio = true;
                        break;
                    }
                }
            }
        }
        return hasAudio;
    }

    private boolean hasAudioFormat(String videoPath) {
        try {
            MediaExtractor extractor = MediaUtils.createExtractor(videoPath);
            MediaFormat audioFormat = MediaUtils.getAudioTrack(extractor);
            extractor.release();
            if (audioFormat != null) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MediaFormat getVideoFormat(String videoPath) {
        try {
            MediaExtractor extractor = MediaUtils.createExtractor(videoPath);
            MediaFormat videoFormat = MediaUtils.getVideoTrack(extractor);
            extractor.release();
            return videoFormat;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getCurVideoEditModuleViewModelPosition() {
        if (this.mVideoEditHelper != null) {
            return this.mVideoEditHelper.getCurSelectVideoPosition();
        }
        return 0;
    }
//    @OnClick({R.id.below_surface,R.id.filter_save,R.id.surface_click_view,R.id.back_iv,R.id.filter_loop_toggle_ll,R.id.cancel_btn_iv,R.id.confirm_btn_iv
//    ,R.id.stack_frame_switch_rl,R.id.filter_select_mode_global_rl,R.id.filter_select_mode_current_rl,R.id.filter_stablize_rl
//    ,R.id.filter_tag_filter_rl,R.id.filter_tag_effect_rl,R.id.filter_tag_loop_rl,R.id.filter_tag_music_rl,R.id.filter_tab_like_filter_ll
//    ,R.id.video_edit_effect_ll,R.id.video_edit_text_ll,R.id.video_edit_split_ll,R.id.video_edit_add_ll,R.id.video_edit_reorder_ll
//    ,R.id.video_edit_remove_ll,R.id.video_edit_music_replace_ll,R.id.video_edit_music_trim_ll,R.id.video_edit_music_remove_ll
//    ,R.id.video_edit_back_image})
    public void onClick(android.view.View view) {
        int position = 0;
        if (this.mVideoEditHelper != null) {
            position = getCurVideoEditModuleViewModelPosition();
        }
        switch (view.getId()) {
            case R.id.below_surface:
                if (!this.mSurfaceView.getLongVideoPlayState()) {
                    callVideoPause();
                    return;
                }
                return;
            case R.id.filter_save:
                this.isBackOrNextPressed = true;
                if (!NONE_FILTER.equals(this.currentFilterName)) {
                    this.mFilterName = this.currentFilterName;
                }
                saving(false);
                this.mTvSave.setEnabled(false);
                long videoProgressTag = System.currentTimeMillis();
                this.isPause2Publish = true;
                try {
                    if (TextUtils.isEmpty(this.mOutPath) || this.mOutPath.length() == 0) {
                        if (1 == this.mDataType) {
                            this.mOutPath = MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0);
                        } else {
                            this.mOutPath = MovieFileUtil.getOutputMediaFile(this.mCurrentTimeStamp);
                        }
                    }
                    FileUtil.deleteFile(this.mOutPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mSurfaceView.setVideoFrames(null);
                int[] trueWidthAndHeight = getPublishMediaWidthAndHeight();
                int width = trueWidthAndHeight[0];
                int height = trueWidthAndHeight[1];
                if (this.mIsSquare) {
                    height = Math.min(width, height);
                    width = height;
                }
                if (2 == this.mDataFrom) {
                    saveVideo(videoProgressTag, width, height);
                    return;
                }
                saveVideo(videoProgressTag, width, height);
                intentPublishActivity(videoProgressTag, width, height);
                return;
            case R.id.surface_click_view:
                if (this.mVideoEditEffectsLayout != null && this.mVideoEditEffectsLayout.getVisibility() == View.VISIBLE) {
                    editConfirmClick(position);
                    return;
                } else if (this.mAudioTrimLayout != null && this.mAudioTrimLayout.getVisibility() == View.VISIBLE) {
                    this.mAudioTrimLayout.performConfirmClick();
                    return;
                } else {
                    return;
                }
            case R.id.back_iv:
                if (this.mDataType == 2) {
                    saveLongVideoPreview();
                    ImagePropertyBean.getInstance().clear();
                    if (this.mSurfaceView != null) {
                        this.mSurfaceView.setVideoPauseOrResume(true);
                    }
                    onBackPressed();
                    EventBus.getDefault().post(new LongVideoBackEvent());
                    return;
                }
                deletePublishVideo();
                setResult(-1);
                boolean saveVideoPreview = (this.mDataFrom == 0 || 3 == this.mDataFrom) && 1 != this.mDataType;
                if (saveVideoPreview) {
                    if (this.mDataType == 0) {
                        saveVideoPreview();
                    } else if (2 != this.mDataType) {
                        clearFrameBuffer();
                    }
                }
                ImagePropertyBean.getInstance().clear();
                onBackPressed();
                return;
            case R.id.filter_loop_toggle_ll:
                if (this.mVideoFrames != null) {
                    switch (this.mVideoFrames.getPlayType()) {
                        case OutputSurfaceArray.PLAY_TYPE_NORMAL:
                            if (this.mFps == StaticLayoutUtil.DefaultSpacingadd && this.mVideoFrames.size() < 6) {
                                this.mVideoFrames.setPlayType(OutputSurfaceArray.PLAY_TYPE_REVERSE);
                                slideLoop(-2, false);
                                this.mIsVideoLooped = this.mVideoFrames.mIsLooped;
                                if (this.va != null && this.hadStartStablize) {
                                    createTransformMethod();
                                }
                                this.mSurfaceView.setTransforms(this.transformsStraight);
                                this.mSurfaceView.start();
                                this.filter_loop_slider_root_rl.setVisibility(View.GONE);
                                this.filter_unloop_root_rl.setVisibility(View.VISIBLE);
                                this.filter_loop_toggle_img.setImageResource(R.drawable.icon_20_loop_reverse);
                                this.filter_loop_toggle_ancrt.setText(R.string.BUTTON_LOOP_REVERSE);
                                break;
                            }
                            this.mVideoFrames.setPlayType(OutputSurfaceArray.PLAY_TYPE_CYCLES);
                            slideLoop(this.loopStart, this.loopEnd);
                            this.mIsVideoLooped = this.mVideoFrames.mIsLooped;
                            if (this.va != null && this.hadStartStablize) {
                                createTransformMethod();
                            }
                            this.mSurfaceView.setTransforms(this.transformsLooped);
                            this.mSurfaceView.start();
                            this.filter_loop_slider_root_rl.setVisibility(View.VISIBLE);
                            this.filter_unloop_root_rl.setVisibility(View.GONE);
                            this.filter_loop_toggle_img.setImageResource(R.drawable.icon_20_loop_rebound);
                            this.filter_loop_toggle_ancrt.setText(R.string.BUTTON_LOOP_REBOUND);
                            break;
                        case OutputSurfaceArray.PLAY_TYPE_CYCLES:
                            this.mVideoFrames.setPlayType(OutputSurfaceArray.PLAY_TYPE_REVERSE);
                            slideLoop(-2, false);
                            this.mIsVideoLooped = this.mVideoFrames.mIsLooped;
                            if (this.va != null && this.hadStartStablize) {
                                createTransformMethod();
                            }
                            this.mSurfaceView.setTransforms(this.transformsStraight);
                            this.mSurfaceView.start();
                            this.filter_loop_slider_root_rl.setVisibility(View.GONE);
                            this.filter_unloop_root_rl.setVisibility(View.VISIBLE);
                            this.filter_loop_toggle_img.setImageResource(R.drawable.icon_20_loop_reverse);
                            this.filter_loop_toggle_ancrt.setText(R.string.BUTTON_LOOP_REVERSE);
                            break;
                        case OutputSurfaceArray.PLAY_TYPE_REVERSE:
                            this.mVideoFrames.setPlayType(OutputSurfaceArray.PLAY_TYPE_NORMAL);
                            slideLoop(-1, false);
                            this.mIsVideoLooped = this.mVideoFrames.mIsLooped;
                            if (this.va != null && this.hadStartStablize) {
                                createTransformMethod();
                            }
                            this.mSurfaceView.start();
                            this.filter_loop_slider_root_rl.setVisibility(View.GONE);
                            this.filter_unloop_root_rl.setVisibility(View.VISIBLE);
                            this.filter_loop_toggle_img.setImageResource(R.drawable.icon_20_loop_forward);
                            this.filter_loop_toggle_ancrt.setText(R.string.BUTTON_LOOP_FORWARD);
                            break;
                    }
                    setNormalFilter();
                    if (this.mFilterModel != null) {
                        this.mFilterModel.setmLooped(this.mIsVideoLooped);
                        return;
                    }
                    return;
                }
                return;
            case R.id.cancel_btn_iv:
                LongVideosModel longVideosModel = this.mVideoAudioManager.getVideosModelList().get(position);
                float realVideoExposure = longVideosModel.getRealVideoExposure();
                this.mVideoEditEffectsLayout.cancelClick(longVideosModel);
                if (realVideoExposure != longVideosModel.getRealVideoExposure()) {
                    setVideoFilter(false);
                }
                this.mSurfaceView.changeSpeedZoomVideoVolume(this.mVideoAudioManager.getVideosModelList(), getBgmList(), this.currentPlayTimeUs, this.mVideoAudioManager.getMediaMute());
                this.mPresenter.hideVideoEditEffectsView();
                refreshTitleAlertOutTime();
                return;
            case R.id.confirm_btn_iv:
                editConfirmClick(position);
                return;
            case R.id.stack_frame_switch_rl:
                this.openStackFrame = !this.openStackFrame;
                if (this.openStackFrame) {
                    this.stack_frame_switch_rl.setAlpha(1.0f);
                } else {
                    this.stack_frame_switch_rl.setAlpha(0.4f);
                }
                setNormalFilter();
                this.mFilterModel.setStackFrameState(this.openStackFrame);
                return;
            case R.id.filter_select_mode_global_rl:
                LongVideosModel currentVideoModel1 = getCurrentVideoModel();
                if (currentVideoModel1 != null) {
                    currentVideoModel1.setFilterState(0);
                    resetFilterModeView();
                    setFilterNameForLongVideosModel();
                    setNormalFilter();
                    return;
                }
                return;
            case R.id.filter_select_mode_current_rl:
                LongVideosModel currentVideoModel = getCurrentVideoModel();
                if (currentVideoModel != null) {
                    currentVideoModel.setFilterState(1);
                    resetFilterModeView();
                    setFilterNameForLongVideosModel();
                    setNormalFilter();
                    return;
                }
                return;
            case R.id.filter_stablize_rl:
                if (!this.hadStartStablize) {
                    this.hadStartStablize = true;
                    new Thread() {
                        public void run() {
                            super.run();
                            va = new VidAnalysis();
                            va.start(mVideoFrames, new Runnable() {
                                public void run() {
                                    int count = va.getNumReadyFrames();
                                    transformCallbackCount = count;
                                    LogUtil.d(FilterActivity.TAG, String.format(Locale.getDefault(), "%d frames ready for transform", new Object[]{Integer.valueOf(count)}));
                                    createTransformMethod();
                                    EventBus.getDefault().post(new VidAnalysisEvent(count));
                                }
                            });
                        }
                    }.start();
                }
                this.isCanChange = false;
                this.mSurfaceView.toggleStabilizer();
                String str = TAG;
                String str2 = "stabilizer:%s";
                Object[] objArr = new Object[1];
                objArr[0] = this.mSurfaceView.isStabilizerEnabled() ? "yes" : "no";
                LogUtil.d(str, String.format(str2, objArr));
                this.mEnableStabilizer = this.mSurfaceView.isStabilizerEnabled();
                refreshTransform();
                setNormalFilter();
                if (this.mFilterModel != null) {
                    this.mFilterModel.setmEnableStabilizer(this.mEnableStabilizer);
                    return;
                }
                return;
            case R.id.filter_tag_filter_rl:
                if (this.filter_filter_root_rl.getVisibility() != View.VISIBLE) {
                    onFilterTabClick();
                    return;
                }
                return;
            case R.id.filter_tag_effect_rl:
                if (this.filter_view_stub != null) {
                    initEffectView(this.filter_view_stub.inflate());
                    this.filter_view_stub = null;
                }
                if (this.filter_effect_root_ll.getVisibility() != View.VISIBLE) {
                    this.filterEffectBeanList = this.mFilterEffectManager.getCurrentSelectEffectTypeList();
                    changeFilterEffectSelectState(this.filterEffectBeanList);
                    onEffectTabClick();
                    if (!isVideoType()) {
                        return;
                    }
                    if (getVideoTypeOriginWHPercent() != 0) {
                        this.filter_effect_crop.setProgressValue(10.0f);
                        setViewColor(this.filter_effect_crop, true);
                        return;
                    }
                    this.filter_effect_crop.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                    setViewColor(this.filter_effect_crop, false);
                    return;
                }
                return;
            case R.id.filter_tag_loop_rl:
                if (isVideoType()) {
                    this.mFilterActivityPresenter.onVideoEditTabClick();
                    return;
                } else if (this.filter_loop_root_rl.getVisibility() != View.VISIBLE) {
                    if (this.filter_tab_choose_ll != null) {
                        this.filter_tab_choose_ll.setVisibility(View.GONE);
                    }
                    if (!(this.mIsShort || 1 == this.mDataType)) {
                        this.filter_tag_loop_rl.setVisibility(View.VISIBLE);
                    }
                    onLoopTabClick();
                    return;
                } else {
                    return;
                }
            case R.id.filter_tag_music_rl:
                onMusicTabClick();
                return;
            case R.id.filter_tab_like_filter_ll:
                this.filterAdapter.isCollectedMode = false;
                this.filterAdapter.notifyDataSetChanged();
                alphaEnterAndExit(this.filter_tab_ll, this.filter_tab_like_filter_ll);
                return;
            case R.id.video_edit_effect_ll:
                LongVideosModel model;
                this.mSurfaceView.setVideoPauseOrResume(true);
                this.mPresenter.initVideoEditEffectsView();
                if (this.mVideoEditEffectsLayout != null) {
                    model = this.mVideoAudioManager.getVideosModelList().get(position);
                    this.mVideoEditEffectsLayout.resetSpeedLoop(model.getEffectsViewShowSpeedCount());
                    this.mVideoEditEffectsLayout.resetUI(model, null);
                }
                this.mPresenter.openVideoEditEffectsView();
                model = this.mVideoAudioManager.getVideosModelList().get(position);
                if (this.mVideoEditHelper != null) {
                    this.mVideoEditHelper.recordVideoEditModelData(model);
                    return;
                }
                return;
            case R.id.video_edit_text_ll:
                callVideoPause();
                toTextInputActivity(this.mTextContent);
                return;
            case R.id.video_edit_split_ll:
                callVideoPause();
                onVideoSplitClick();
                return;
            case R.id.video_edit_add_ll:
                if (this.mVideoEditHelper != null) {
                    this.mCurInsertPosition = this.mVideoEditHelper.getInsertPosition();
                }
                onAddVideosClick(this.mCurInsertPosition);
                return;
            case R.id.video_edit_reorder_ll:
                callVideoPause();
                this.mPresenter.openVideoEditOrderView();
                return;
            case R.id.video_edit_remove_ll:
                callVideoPause();
                if (this.mVideoEditHelper != null) {
                    this.mVideoEditHelper.removeCurSelectVideo();
                    setUndoData(1);
                }
                refreshLineView();
                onVideoSelectAreaShownOrHide(false, false, null);
                return;
            case R.id.video_edit_music_replace_ll:
                if (this.mVideoEditHelper != null) {
                    LongVideosModel curSelectAudioModel = this.mVideoEditHelper.getCurSelectAudioModel();
                    if (curSelectAudioModel != null) {
                        this.isAudioReplace = true;
                        onAddMusicClick(getBgmList().indexOf(curSelectAudioModel));
                        return;
                    }
                    return;
                }
                return;
            case R.id.video_edit_music_trim_ll:
                callVideoPause();
                this.mPresenter.openAudioTrimView();
                return;
            case R.id.video_edit_music_remove_ll:
                if (this.mVideoEditHelper != null) {
                    this.mVideoEditHelper.removeCurSelectAudio();
                    return;
                }
                return;
            case R.id.video_edit_back_image:
                displayUndo();
                return;
            default:
                return;
        }
    }

    private void editConfirmClick(int position) {
        LogUtil.d("slim", "resultInt:" + this.mVideoEditEffectsLayout.confirmClick(this.mVideoAudioManager.getVideosModelList().get(position)));
        List<LongVideosModel> videoModels = this.mVideoAudioManager.getVideosModelList();
        this.mSurfaceView.changeSpeedZoomVideoVolume(videoModels, getBgmList(), this.currentPlayTimeUs, this.mVideoAudioManager.getMediaMute());
        this.mPresenter.hideVideoEditEffectsView();
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.refreshForVideoEditConfirm();
        }
        refreshTitleAlertOutTime();
        this.canVideoOptDialogShow = true;
        videoModels.get(position).judgeAndStartProxyVideo();
        this.mVideoAudioManager.setUndoModel(1, this.mFilterEffectManager.copyCurrentList(), this.mFilterName);
    }

    private void resetFilterModeView() {
        LongVideosModel currentVideoModel = getCurrentVideoModel();
        if (TextUtil.isValidate(currentVideoModel)) {
            setFilterSelectModeGlobal(currentVideoModel.getFilterState() == 0);
        }
        LongVideosModel videoModel = getCurrentVideoModel();
        if (this.mFilterListLayoutManager != null && videoModel != null) {
            String mFilterName;
            int historyPos = 1;
            if (videoModel.getFilterState() == 0) {
                mFilterName = videoModel.getPublicFilterName();
            } else {
                mFilterName = videoModel.getPrivateFilterName();
            }
            int i = 0;
            while (i < this.mFilterInfos.size()) {
                if (this.mFilterInfos.get(i).name_en != null && this.mFilterInfos.get(i).name_en.equals(mFilterName)) {
                    historyPos = i;
                    break;
                }
                i++;
            }
            this.mFilterName = mFilterName;
            setFilterGroupNameState(historyPos);
            this.filterAdapter.setCurrentPosition(historyPos);
            this.filterAdapter.setFilterName(mFilterName, (FilterInfo) this.mFilterInfos.get(historyPos));
            this.filterAdapter.notifyDataSetChanged();
            this.filter_list_recyclerview.scrollToPosition(historyPos);
            //this.filter_list_recyclerview.postDelayed(36.lambdaFactory$(this, historyPos), 50);
            final int finalHistoryPos = historyPos;
            filter_list_recyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lambda$resetFilterModeView$34(finalHistoryPos);
                }
            },50);
        }
    }

    private /* synthetic */ void lambda$resetFilterModeView$34(int finalHistoryPos) {
        android.view.View viewByPosition = this.mFilterListLayoutManager.findViewByPosition(finalHistoryPos);
        if (viewByPosition != null) {
            this.filter_list_recyclerview.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (this.metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
        }
    }

    private LongVideosModel getCurrentVideoModel() {
        if (this.mVideoAudioManager == null || this.mVideoAudioManager.getCurrentVideoModel() == null) {
            return null;
        }
        return this.mVideoAudioManager.getCurrentVideoModel();
    }

    private void setFilterSelectModeGlobal(boolean global) {
        if (global) {
            hideCurrentClipIv();
            this.filter_select_mode_global_iv.setVisibility(View.VISIBLE);
            this.filter_select_mode_current_iv.setVisibility(View.INVISIBLE);
            this.filter_select_mode_global_tv.setTypeface(FontsUtil.setAvenirNextCondensedMediumTypeFace());
            this.filter_select_mode_current_tv.setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
            this.filter_select_mode_global_tv.setAlpha(1.0f);
            this.filter_select_mode_current_tv.setAlpha(0.4f);
            return;
        }
        showCurrentClipIv();
        this.filter_select_mode_global_iv.setVisibility(View.INVISIBLE);
        this.filter_select_mode_current_iv.setVisibility(View.VISIBLE);
        this.filter_select_mode_current_tv.setTypeface(FontsUtil.setAvenirNextCondensedMediumTypeFace());
        this.filter_select_mode_global_tv.setTypeface(FontsUtil.setAveNextCondensedRegularTypeFace());
        this.filter_select_mode_global_tv.setAlpha(0.4f);
        this.filter_select_mode_current_tv.setAlpha(1.0f);
    }

    private void onVideoSplitClick() {
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.splitCurrentCenter();
            refreshLineView();
            setUndoData(1);
        }
    }

    private void refreshLineView() {
        if (this.mVideoEditHelper != null) {
            this.video_time_slide_bar_backgroud_view.setLongVideosModels(this.mVideoEditHelper.getLongVideosModelsTime());
        }
    }

    private void updateViewWidth(android.view.View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    private void hideMusicTabArea() {
        hideVideoMusicNameLayout();
    }

    private void onMusicTabClick() {
        onMusicTabClick(false);
    }

    private void onMusicTabClick(boolean isSpecial) {
        if (isSpecial || this.video_edit_parent_ll == null || this.video_edit_parent_ll.getVisibility() != View.VISIBLE || this.mVideoEditHelper == null || !this.mVideoEditHelper.isMusicEdit()) {
            hideEditCenterLine();
            showVideoTimeSlideBar();
            hideWhiteAlphaCoverViewWhenSwitchTab();
            switchBottomIconAndCircleShowState(5);
            if (this.specific_combination_rl != null) {
                this.specific_combination_rl.setVisibility(View.GONE);
            }
            if (this.filter_tab_choose_ll != null) {
                this.filter_tab_choose_ll.setVisibility(View.GONE);
            }
            this.filter_stablize_rl.setVisibility(View.GONE);
            checkSurfaceNeedScale(false);
            if (this.filter_tab_choose_ll != null) {
                this.filter_tab_choose_ll.setVisibility(View.GONE);
            }
            if (this.mVideoEditSwitchTabsHolder != null) {
                this.mVideoEditSwitchTabsHolder.onSwitchTabClick(true);
                refreshAudioEditButton();
            }
            hideTextRecyclerView();
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.onSwitchTabClick(true);
            }
        }
    }

    private void hideTextRecyclerView() {
        if (this.video_edit_text_rv != null) {
            this.video_edit_text_rv.setVisibility(View.GONE);
        }
    }

    private void showTextRecyclerView() {
        if (this.video_edit_text_rv != null) {
            this.video_edit_text_rv.setVisibility(View.VISIBLE);
        }
    }

    private void initEffectView(android.view.View v) {
        this.filter_effect_exposure = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_exposure);
        this.filter_effect_beauty = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_beauty);
        this.filter_effect_contrast = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_contrast);
        this.filter_effect_saturation = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_saturation);
        this.filter_effect_temperature = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_temperature);
        this.filter_effect_tinge = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_tinge);
        this.filter_effect_tilt = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_tilt);
        this.filter_effect_sharpen = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_sharpen);
        this.filter_effect_prism = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_prism);
        this.filter_effect_grain = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_grain);
        this.filter_effect_dust = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_dust);
        this.filter_effect_vignette = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_vignette);
        this.filter_effect_leak = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_leak);
        this.filter_effect_date = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_date);
        this.filter_effect_fade = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_fade);
        this.filter_effect_highlight_shadow = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_highlight_shadow);
        this.filter_effect_sky = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_sky);
        this.filter_effect_shade_lighten = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_shade_lighten);
        this.filter_effect_crop = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_crop);
        this.filter_effect_rotation = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_rotation);
        this.filter_effect_vertical = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_vertical);
        this.filter_effect_horizontal = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_horizontal);
        this.filter_effect_mirror = (FilterEffectSetRelativeLayout) v.findViewById(R.id.filter_effect_mirror);
        FilterEffectOnClick filterEffectOnClick = new FilterEffectOnClick();//new FilterEffectOnClick(this, null);
        this.filter_effect_rotation.setOnClickListener(filterEffectOnClick);
        this.filter_effect_exposure.setOnClickListener(filterEffectOnClick);
        this.filter_effect_beauty.setOnClickListener(filterEffectOnClick);
        this.filter_effect_saturation.setOnClickListener(filterEffectOnClick);
        this.filter_effect_contrast.setOnClickListener(filterEffectOnClick);
        this.filter_effect_temperature.setOnClickListener(filterEffectOnClick);
        this.filter_effect_tinge.setOnClickListener(filterEffectOnClick);
        this.filter_effect_tilt.setOnClickListener(filterEffectOnClick);
        this.filter_effect_sharpen.setOnClickListener(filterEffectOnClick);
        this.filter_effect_prism.setOnClickListener(filterEffectOnClick);
        this.filter_effect_grain.setOnClickListener(filterEffectOnClick);
        this.filter_effect_dust.setOnClickListener(filterEffectOnClick);
        this.filter_effect_vignette.setOnClickListener(filterEffectOnClick);
        this.filter_effect_leak.setOnClickListener(filterEffectOnClick);
        this.filter_effect_date.setOnClickListener(filterEffectOnClick);
        this.filter_effect_fade.setOnClickListener(filterEffectOnClick);
        this.filter_effect_highlight_shadow.setOnClickListener(filterEffectOnClick);
        this.filter_effect_sky.setOnClickListener(filterEffectOnClick);
        this.filter_effect_shade_lighten.setOnClickListener(filterEffectOnClick);
        this.filter_effect_crop.setOnClickListener(filterEffectOnClick);
        this.filter_effect_vertical.setOnClickListener(filterEffectOnClick);
        this.filter_effect_horizontal.setOnClickListener(filterEffectOnClick);
        this.filter_effect_mirror.setOnClickListener(filterEffectOnClick);
        if (1 == this.mDataType) {
            this.filter_stablize_rl.setVisibility(View.GONE);
        } else {
            this.filter_effect_grain.setVisibility(View.GONE);
            this.filter_effect_leak.setVisibility(View.GONE);
            this.filter_effect_sharpen.setVisibility(View.GONE);
            this.filter_effect_crop.setVisibility(View.GONE);
            this.filter_effect_dust.setVisibility(View.GONE);
            if (this.mDataType == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.filter_effect_date.getLayoutParams();
                layoutParams.rightMargin = DensityUtil.dip2px(15.0f);
                this.filter_effect_date.setLayoutParams(layoutParams);
            }
        }
        if (2 == this.mDataFrom) {
            this.filter_effect_crop.setVisibility(View.GONE);
        }
        hideVideoTypeFilters();
    }

    private void hideVideoTypeFilters() {
        if (isVideoType()) {
            this.filter_effect_beauty.setVisibility(View.GONE);
            this.filter_effect_grain.setVisibility(View.GONE);
            this.filter_effect_leak.setVisibility(View.GONE);
            this.filter_effect_tilt.setVisibility(View.GONE);
            this.filter_effect_sharpen.setVisibility(View.GONE);
            this.filter_effect_sky.setVisibility(View.GONE);
            this.filter_effect_crop.setVisibility(View.VISIBLE);
            this.filter_effect_rotation.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams cropLP = (LinearLayout.LayoutParams) this.filter_effect_crop.getLayoutParams();
            cropLP.leftMargin = DensityUtil.dip2px(10.0f);
            cropLP.rightMargin = DensityUtil.dip2px(30.0f);
            this.filter_effect_crop.setLayoutParams(cropLP);
            this.filter_effect_vertical.setVisibility(View.GONE);
            this.filter_effect_horizontal.setVisibility(View.GONE);
            this.filter_effect_mirror.setVisibility(View.GONE);
            this.filter_effect_rotation.setVisibility(View.GONE);
            this.filter_effect_date.setVisibility(View.GONE);
        }
    }

    public void printSysMemInfo() {
//        final ActivityManager mActivityManager = (ActivityManager) getSystemService(com.tencent.android.tpush.common.Constants.FLAG_ACTIVITY_NAME);
//        final MemoryInfo outInfo = new MemoryInfo();
//        new Thread() {
//            public void run() {
//                while (true) {
//                    mActivityManager.getMemoryInfo(outInfo);
//                    long availMem = outInfo.availMem;
//                    System.out.println("system meminfo: " + Formatter.formatFileSize(getApplicationContext(), availMem) + "  (" + availMem + ")");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    private void alphaEnterAndExit(android.view.View enterView, android.view.View exitView) {
        enterView.clearAnimation();
        exitView.clearAnimation();
        enterView.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
        exitView.setAlpha(1.0f);
        enterView.setVisibility(View.VISIBLE);
        startAnimation(enterView, 1.0f, 200, 0);
        if (exitView.getVisibility() == View.VISIBLE) {
            startAnimation(exitView, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
        }
    }

    private void alphaStrengthEnterAndExit(final android.view.View enterView, final android.view.View exitView, final boolean isShow) {
        if (enterView != null && exitView != null) {
            enterView.clearAnimation();
            exitView.clearAnimation();
            enterView.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
            exitView.setAlpha(1.0f);
            enterView.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(enterView).alpha(1.0f).setDuration(200).setListener(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    filterEffectDetailIsShow = isShow;
                    enterView.setVisibility(View.VISIBLE);
                }
            }).start();
            if (exitView.getVisibility() == View.VISIBLE) {
                ViewPropertyAnimator.animate(exitView).alpha(StaticLayoutUtil.DefaultSpacingadd).setDuration(200).setListener(new AnimatorEndListener() {
                    public void onAnimationEnd(Animator animation) {
                        exitView.setVisibility(View.GONE);
                    }
                }).start();
            }
        }
    }

    private void alphaEnterAndExit(android.view.View enterView, android.view.View exitView, android.view.View exitView2, android.view.View exitView3) {
        ViewCompat.animate(enterView).cancel();
        enterView.clearAnimation();
        exitView.clearAnimation();
        exitView2.clearAnimation();
        exitView3.clearAnimation();
        enterView.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
        exitView.setAlpha(1.0f);
        exitView2.setAlpha(1.0f);
        exitView3.setAlpha(1.0f);
        enterView.setVisibility(View.VISIBLE);
        startAnimation(enterView, 1.0f, 200, 0);
        if (exitView.getVisibility() == View.VISIBLE) {
            startAnimation(exitView, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
        }
        if (exitView2.getVisibility() == View.VISIBLE) {
            startAnimation(exitView2, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
        }
        if (exitView3.getVisibility() == View.VISIBLE) {
            startAnimation(exitView3, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
        }
    }

    private void startAnimation(final android.view.View view, float value, long time, final int visibility) {
        ViewPropertyAnimator.animate(view).alpha(value).setDuration(time).setListener(new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(visibility);
            }
        }).start();
    }

    private void startAnimation(android.view.View view, float value, long time, AnimatorEndListener animatorEndListener) {
        ViewPropertyAnimator.animate(view).alpha(value).setDuration(time).setListener(animatorEndListener).start();
    }

    private void alphaEnterAndExit(android.view.View enterView, android.view.View exitView, AnimatorEndListener animatorEndListener) {
        enterView.clearAnimation();
        exitView.clearAnimation();
        enterView.setAlpha(StaticLayoutUtil.DefaultSpacingadd);
        exitView.setAlpha(1.0f);
        enterView.setVisibility(View.VISIBLE);
        startAnimation(enterView, 1.0f, 200, animatorEndListener);
        if (exitView.getVisibility() == View.VISIBLE) {
            startAnimation(exitView, StaticLayoutUtil.DefaultSpacingadd, 200, 8);
        }
    }

    private synchronized void saveVideoPreviewDraft() {
        if (this.mFilterInfos != null && this.mFilterInfos.size() >= 0) {
            Iterator it = this.mFilterInfos.iterator();
            while (it.hasNext()) {
                FilterInfo info = (FilterInfo) it.next();
                if (info.name_en != null && info.name_en.equals(this.mFilterName)) {
                    try {
                        String savePath = MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0);
                        if (!(info.bitmap == null || info.bitmap.isRecycled())) {
                            info.bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(savePath));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return;
    }

    private synchronized void saveDraft(FilterModel filterModel, DraftLongVideoBean longVideoBean, boolean notifyFresh) {
        boolean fromLocalGif = 1 == this.mDataFrom && (this.mDataType == 0 || this.mDataType == 3);
        boolean shouldReturn = fromLocalGif || (1 == this.mDataType) || (2 == this.mDataFrom);
        if (!shouldReturn) {
            if (filterModel == null) {
                filterModel = new FilterModel();
            }
            if (longVideoBean == null) {
                longVideoBean = new DraftLongVideoBean();
            }
            if (this.mVideoAudioManager != null) {
                longVideoBean.setAllVideoMute(this.mVideoAudioManager.isAllVideoMute());
                longVideoBean.setAllAudioMute(this.mVideoAudioManager.isAllAudioMute());
                longVideoBean.setLongVideoModels(this.mVideoAudioManager.draftCloneVideoModels());
                longVideoBean.setMusicModel(this.mVideoAudioManager.draftCloneAudioModels());
                longVideoBean.setTextModels(this.mVideoAudioManager.draftCloneTextModels());
                UndoBean undoBean = new UndoBean();
                undoBean.setUndoModels(this.mVideoAudioManager.draftCloneUndoModels());
                longVideoBean.setUndoBean(undoBean);
            }
            if (this.mDataType == 2) {
                longVideoBean.setLongVideoWidth(this.mVideoWidth);
                longVideoBean.setLongVideoHeight(this.mVideoHeight);
                longVideoBean.setFirstVideoBean(this.mFirstVideoBean);
            }
            if (this.mDataType == 0) {
                filterModel.setPlayType(this.mVideoFrames.getPlayType());
            }
            filterModel.setmFinalEffectState(this.mFinalEffectState);
            filterModel.setmEnableStabilizer(this.mSurfaceView.isStabilizerEnabled());
            filterModel.setmLooped(this.mIsVideoLooped);
            filterModel.setSlideLevel(this.slideLevel);
            filterModel.setUnloopStart(this.unloopStart);
            filterModel.setUnloopEnd(this.unloopEnd);
            filterModel.setRotation(this.rotation);
            filterModel.setFps(this.mFps);
            filterModel.setOrientation(this.orientation);
            boolean isLocalGIF = 1 == this.mDataFrom && this.mDataType == 0;
            filterModel.setmIsFromLocalGIF(isLocalGIF);
            filterModel.setLoopStart(this.loopStart);
            filterModel.setLoopEnd(this.loopEnd);
            filterModel.setFilterName(this.mFilterName);
            filterModel.setSelectEffectTypeList(this.mFilterEffectManager.getCurrentList());
            filterModel.setCreationDate(this.mCreationDate);
            double duration = 0.0d;
            if (FilterViewUtils.isGifModel(this.mDataType)) {
                filterModel.setDuration((long) (1000.0d * (((double) this.mVideoFrames.size()) * 0.1d)));
            } else if (FilterViewUtils.isVideoModel(this.mDataType)) {
                ArrayList<LongVideosModel> models = this.mVideoAudioManager.getVideosModelList();
                if (models != null && models.size() > 0) {
                    Iterator it = models.iterator();
                    while (it.hasNext()) {
                        duration += (double) ((LongVideosModel) it.next()).getCurrentDuration();
                    }
                }
                longVideoBean.setDuration((long) duration);
            }
            if (this.mDraftModel == null) {
                this.mDraftModel = new DraftModel(this.mCurrentTimeStamp);
                DraftInfoBean draftInfoBean = new DraftInfoBean();
                draftInfoBean.version = 33;
                draftInfoBean.timestamp = this.mCurrentTimeStamp;
                if (1 == this.mDataType) {
                    draftInfoBean.vtype = 1;
                } else if (this.mDataType == 0) {
                    draftInfoBean.vtype = 0;
                } else {
                    draftInfoBean.vtype = 2;
                    draftInfoBean.version = 44;
                }
                this.mDraftModel.setDraftInfoBean(draftInfoBean);
                this.mDraftModel.setFilterModel(filterModel);
                this.mDraftModel.setmLongVideoBean(longVideoBean);
                LogUtil.d("SaveDraftVideo", String.format("timeStamp : %s, longVideoBean : %s", new Object[]{this.mCurrentTimeStamp, longVideoBean}));
                this.mDraftModel.setSignature(System.currentTimeMillis() + "");
                DraftBoxManager.getInstance().savePreviewToSDcard(this.mDraftModel);
                DraftBoxManager.getInstance().saveDraftLocationAndMsvToSDcard(this.mDraftModel);
                DraftBoxManager.getInstance().saveDraftWithStory(this.mDraftModel);
            } else {
                this.mDraftModel.setmLongVideoBean(longVideoBean);
                this.mDraftModel.setFilterModel(filterModel);
                this.mDraftModel.setSignature(System.currentTimeMillis() + "");
                DraftBoxManager.getInstance().savePreviewToSDcard(this.mDraftModel);
            }
            if (notifyFresh) {
                EventBus.getDefault().post(new DraftAddEvent(this.mDraftModel.getTimeStamp()));
            }
            this.hasSavedDraft = true;
        }
    }

    private void saveLongVideoPreview() {
        CameraVideoPathModel.getInstance().removeVideoPath(this.mVideoAudioManager.getVideosModelList());
        App.encoders.addTask("saveLongVideoPreview", new Runnable() {
            public void run() {
                _saveLongVideoPreview();
            }
        });
    }

    private void _saveLongVideoPreview() {
//        EGL10Helper.withContext("_saveLongVideoPreview", 37.lambdaFactory$(this));
        EGL10Helper.withContext("_saveLongVideoPreview", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$_saveLongVideoPreview$35(eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$_saveLongVideoPreview$35(EGL10Helper egl) {
        int previewWidth;
        int previewHeight;
        ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
        if (this.mVideoHeight > this.mVideoWidth) {
            previewWidth = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
            previewHeight = (int) ((((float) (this.mVideoHeight * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) this.mVideoWidth));
        } else {
            previewHeight = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
            previewWidth = (int) ((((float) (this.mVideoWidth * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) this.mVideoHeight));
        }
        if (this.mIsSquare) {
            previewHeight = Math.min(previewWidth, previewHeight);
            previewWidth = previewHeight;
        }
        Playlist playlist = FilterViewUtils.getCurrentPlaylist(this.mSurfaceView);
        if (playlist != null) {
            int i;
            String filterName;
            float filterIntensity;
            Bitmap videoPreview;
            List<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
            if (textModelList != null && textModelList.size() > 0) {
                int size = textModelList.size();
                for (i = 0; i < size; i++) {
                    LongVideosModel textModel = textModelList.get(i);
                    Bitmap textBitmap = textModel.getTextBitmap(this, this.mVideoWidth, this.mVideoHeight);
                    textModel.setTextBitmap(textBitmap);
                    Playlist.Entry entry = playlist.add(textBitmap, textModel.getMediaStartTimeUs(), 0, textModel.getOriginalCurrentDuration() * 1000);
                    entry.setSpeed(textModel.getVideoSpeed());
                    float[] textSizeInfo = textModel.getTextSizeInfo(textBitmap, this.mVideoWidth, this.mVideoHeight);
                    entry.setOutputRect(textSizeInfo[0], textSizeInfo[1], textSizeInfo[2], textSizeInfo[3]);
                    entry.setAlpha(textSizeInfo[4]);
                }
            }
            int size1 = videosModelList.size();
            LongVideosModel videoModel = videosModelList.get(0);
            if (videoModel.getFilterState() == 0) {
                filterName = videoModel.getPublicFilterName();
                filterIntensity = videoModel.getPublicIntensity();
            } else {
                filterName = videoModel.getPrivateFilterName();
                filterIntensity = videoModel.getPrivateIntensity();
            }
            GPUImageFilter finalFilterNormal = getFinalFilterEffect(filterName, filterIntensity);
            HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> hashMap = new HashMap();
            HashMap<Integer, GPUImageFilter> publicArray = new HashMap();
            for (i = 0; i < size1; i++) {
                publicArray.put(Integer.valueOf(i), finalFilterNormal);
                hashMap.put(publicArray, new HashMap());
            }
            if (TextUtil.isValidate(this.mSurfaceView.getPlayer())) {
                videoPreview = this.mSurfaceView.getLongVideoPreview(hashMap, previewWidth, previewHeight, playlist);
            } else {
                if (!(this.mVideoAudioManager == null)) {
                    if (!(this.mVideoAudioManager.getVideosModelList().size() <= 0)) {
                        if (!(this.mVideoAudioManager.getVideosModelList().get(0) == null)) {
                            playlist.add(Playlist.FileMedia.create((LongVideosModel) this.mVideoAudioManager.getVideosModelList().get(0)), 0);
                            videoPreview = this.mSurfaceView.getLongVideoPreview(hashMap, playlist, previewWidth, previewHeight);
                        } else {
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            this.mVideoAudioManager.recycleTextBitmap();
            if (videoPreview != null) {
                try {
                    videoPreview.compress(CompressFormat.JPEG, 92, new FileOutputStream(MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                videoPreview.recycle();
            }
            saveDraft(this.mFilterModel, this.mDraftLongVideoBean, true);
            finalFilterNormal.destroy();
            finalFilterNormal.destroySecondary();
        }
    }

    private void saveVideoPreview() {
        boolean shouldReturn = 2 == this.mDataFrom || this.mVideoFrames == null || this.mVideoFrames.size() < 3;
        if (!shouldReturn) {
            Thread t = new Thread() {
                public void run() {
                    long t1 = System.nanoTime();
                    //EGL10Helper.withContext("saveVideoPreview", FilterActivity$58$.Lambda.1.lambdaFactory$(this));
                    EGL10Helper.withContext("saveVideoPreview", new EGLRunnableVoid() {
                        @Override
                        public void run(EGL10Helper eGL10Helper) {
                            lambda$run$0(eGL10Helper);
                        }
                    });
                    LogUtil.d("message", "saveDraftPreview time : " + (System.nanoTime() - t1));
                }

                private /* synthetic */ void lambda$run$0(EGL10Helper egl) {
                    GPUImageFilter finalFilterNormal=null;
                    FrameRenderer renderer=null;
                    try {
                        int previewWidth;
                        int previewHeight;
                        finalFilterNormal = getFinalFilterEffect(mFilterName, strengthValue);
                        if (mVideoHeight > mVideoWidth) {
                            previewWidth = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
                            previewHeight = (int) ((((float) (mVideoHeight * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) mVideoWidth));
                        } else {
                            previewHeight = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
                            previewWidth = (int) ((((float) (mVideoWidth * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) mVideoHeight));
                        }
                        if (mIsSquare) {
                            previewHeight = Math.min(previewWidth, previewHeight);
                            previewWidth = previewHeight;
                        }
                        renderer = new FrameRenderer();
                        renderer.setVideoFrames(mVideoFrames);
                        renderer.xOffset = StaticLayoutUtil.DefaultSpacingadd;
                        renderer.yOffset = StaticLayoutUtil.DefaultSpacingadd;
                        previewHeight += previewHeight % 2;
                        previewWidth += previewWidth % 2;
                        renderer.setEnableStabilizer(mSurfaceView.isStabilizerEnabled());
                        int pictureIndex = 0;
                        if (stack_frame_switch_rl != null && stack_frame_switch_rl.getVisibility() == View.VISIBLE && openStackFrame) {
                            pictureIndex = 2;
                        }
                        LogUtil.d("FilterActivityGetBitmaps", String.format("saveVideoPreview  pictureIndex : %s ", new Object[]{Integer.valueOf(pictureIndex)}));
                        int outWidth = previewWidth;
                        int outHeight = previewHeight;
                        float degree = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                        if (degree == 90.0f || degree == 270.0f) {
                            outWidth = previewHeight;
                            outHeight = previewWidth;
                        }
                        Bitmap preBitmap = (Bitmap) renderer.getBitmaps(egl, outWidth, outHeight, new GPUImageFilter[]{finalFilterNormal}, new int[]{pictureIndex}).get(0);
                        if (preBitmap != null) {
                            preBitmap.compress(CompressFormat.JPEG, 92, new FileOutputStream(MovieFileUtil.getJPGFilePathWithIndex(mCurrentTimeStamp, 0)));
                            preBitmap.recycle();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Throwable th) {
                        if (mVideoFrames != null) {
                            if (mSurfaceView != null) {
                                mSurfaceView.setVideoFrames(null);
                            }
                            mVideoFrames.releaseFrames();
                            mVideoFrames.releasePool();
                        }
                    }
                    saveDraft(mFilterModel, mDraftLongVideoBean, true);
                    renderer.destroy();
                    finalFilterNormal.destroy();
                    finalFilterNormal.destroySecondary();
                    if (mVideoFrames != null) {
                        if (mSurfaceView != null) {
                            mSurfaceView.setVideoFrames(null);
                        }
                        mVideoFrames.releaseFrames();
                        mVideoFrames.releasePool();
                    }
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void savePictureOriginal() {
        if (this.mVideoFrames != null) {
            final String imagePath = MovieFileUtil.getOriginalSDFilePath(String.valueOf(System.currentTimeMillis()));
            new Thread() {
                public void run() {
                    boolean z = true;
                    if (FileUtil.isCanWirte(new File(imagePath).getParentFile())) {
                        byte[] imageDataBytes = ImagePropertyBean.getInstance().getImageDataBytes();
                        if (imageDataBytes != null && imageDataBytes.length > 0) {
                            try {
                                FileOutputStream outputStream = new FileOutputStream(imagePath);
                                outputStream.write(imageDataBytes);
                                outputStream.close();
                                ImagePropertyBean instance = ImagePropertyBean.getInstance();
                                String str = imagePath;
                                if (mCameraLensType != 1) {
                                    z = false;
                                }
                                instance.readGropTag(str, z);
                                MSCVController.postExifGetOfficialTag(ImagePropertyBean.getInstance().getImagePropertyObject());
                                FileUtil.notifySystemFileFlesh(getActivity(), imagePath);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }.start();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:88:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01e0  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01fd  */
    private void saveVideoAndPreViewForDraft(EGL10Helper r23, boolean r24) {
        /*
        r22 = this;
        r0 = r22;
        r7 = r0.mDataFrom;
        if (r7 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r18 = 0;
        r0 = r22;
        r7 = r0.mVideoFrames;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        if (r7 == 0) goto L_0x0207;
    L_0x000f:
        r0 = r22;
        r7 = r0.mVideoFrames;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = r7.size();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8 = 3;
        if (r7 < r8) goto L_0x0207;
    L_0x001a:
        r0 = r22;
        r4 = r0.mVideoWidth;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r5 = r0.mVideoHeight;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r7 = r0.mIsSquare;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        if (r7 == 0) goto L_0x002d;
    L_0x0028:
        r5 = java.lang.Math.min(r4, r5);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r4 = r5;
    L_0x002d:
        r2 = com.blink.academy.onetake.VideoTools.VideoEncoderFactory.createEncoder(r23);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r7 = r0.mCurrentTimeStamp;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r3 = com.blink.academy.onetake.support.manager.DraftBoxManager.dirWithTimeStamp(r7);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r11 = new java.io.File;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r11.<init>(r3);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = r11.exists();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        if (r7 != 0) goto L_0x0047;
    L_0x0044:
        r11.mkdirs();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
    L_0x0047:
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7.<init>();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r8 = r0.mCurrentTimeStamp;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8 = com.blink.academy.onetake.support.manager.DraftBoxManager.dirWithTimeStamp(r8);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8 = "/video.mp4";
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r3 = r7.toString();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = "before encode";
        com.blink.academy.onetake.VideoTools.EGL10Helper.clearGLError(r7);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = TAG;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8.<init>();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r20 = "size:::";
        r0 = r20;
        r8 = r8.append(r0);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r0 = r0.mVideoFrames;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r20 = r0;
        r20 = r20.size();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r20;
        r8 = r8.append(r0);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r0 = r0.mOutPath;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r20 = r0;
        r0 = r20;
        r8 = r8.append(r0);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        com.blink.academy.onetake.support.debug.LogUtil.d(r7, r8);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r13 = new jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r13.<init>();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7.<init>();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r13.addFilter(r7);	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r7 = r0.mVideoFrames;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r7 = r7.size();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r15 = new int[r7];	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r14 = 0;
    L_0x00b1:
        r7 = r15.length;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        if (r14 >= r7) goto L_0x00b9;
    L_0x00b4:
        r15[r14] = r14;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r14 = r14 + 1;
        goto L_0x00b1;
    L_0x00b9:
        r6 = new com.blink.academy.onetake.VideoTools.FrameRenderer;	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r6.<init>();	 Catch:{ Exception -> 0x0203, all -> 0x01e5 }
        r0 = r22;
        r7 = r0.mVideoFrames;	 Catch:{ Exception -> 0x01c4 }
        r6.setVideoFrames(r7);	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.mOffsetX;	 Catch:{ Exception -> 0x01c4 }
        r6.xOffset = r7;	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.mOffsetY;	 Catch:{ Exception -> 0x01c4 }
        r6.yOffset = r7;	 Catch:{ Exception -> 0x01c4 }
        if (r24 != 0) goto L_0x011c;
    L_0x00d3:
        r0 = r22;
        r7 = r0.mVideoFrames;	 Catch:{ Exception -> 0x01c4 }
        r19 = r7.size();	 Catch:{ Exception -> 0x01c4 }
        r0 = r19;
        r12 = new jp.co.cyberagent.android.gpuimage.GPUImageFilter[r0];	 Catch:{ Exception -> 0x01c4 }
        r14 = 0;
    L_0x00e0:
        r0 = r19;
        if (r14 >= r0) goto L_0x00e9;
    L_0x00e4:
        r12[r14] = r13;	 Catch:{ Exception -> 0x01c4 }
        r14 = r14 + 1;
        goto L_0x00e0;
    L_0x00e9:
        r7 = 0;
        r6.setWrapFrame(r7);	 Catch:{ Exception -> 0x01c4 }
        r6.setFilters(r12);	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.va;	 Catch:{ Exception -> 0x01c4 }
        if (r7 == 0) goto L_0x0103;
    L_0x00f6:
        r0 = r22;
        r7 = r0.hadStartStablize;	 Catch:{ Exception -> 0x01c4 }
        if (r7 == 0) goto L_0x0103;
    L_0x00fc:
        r0 = r22;
        r7 = r0.va;	 Catch:{ Exception -> 0x01c4 }
        r7.pause();	 Catch:{ Exception -> 0x01c4 }
    L_0x0103:
        r7 = 1;
        r8 = com.blink.academy.onetake.VideoTools.VideoEncoder.Quality.HIGH;	 Catch:{ Exception -> 0x01c4 }
        r2.putFrames(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.va;	 Catch:{ Exception -> 0x01c4 }
        if (r7 == 0) goto L_0x011c;
    L_0x010f:
        r0 = r22;
        r7 = r0.hadStartStablize;	 Catch:{ Exception -> 0x01c4 }
        if (r7 == 0) goto L_0x011c;
    L_0x0115:
        r0 = r22;
        r7 = r0.va;	 Catch:{ Exception -> 0x01c4 }
        r7.resume();	 Catch:{ Exception -> 0x01c4 }
    L_0x011c:
        r0 = r22;
        r7 = r0.mVideoHeight;	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r8 = r0.mVideoWidth;	 Catch:{ Exception -> 0x01c4 }
        if (r7 <= r8) goto L_0x01a8;
    L_0x0126:
        r17 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        r0 = r22;
        r7 = r0.mVideoHeight;	 Catch:{ Exception -> 0x01c4 }
        r7 = r7 * r17;
        r7 = (float) r7;	 Catch:{ Exception -> 0x01c4 }
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = r7 * r8;
        r0 = r22;
        r8 = r0.mVideoWidth;	 Catch:{ Exception -> 0x01c4 }
        r8 = (float) r8;	 Catch:{ Exception -> 0x01c4 }
        r7 = r7 / r8;
        r0 = (int) r7;	 Catch:{ Exception -> 0x01c4 }
        r16 = r0;
    L_0x013b:
        r7 = 0;
        r6.xOffset = r7;	 Catch:{ Exception -> 0x01c4 }
        r7 = 0;
        r6.yOffset = r7;	 Catch:{ Exception -> 0x01c4 }
        r7 = r17 % 2;
        r17 = r17 + r7;
        r7 = r16 % 2;
        r16 = r16 + r7;
        r0 = r22;
        r7 = r0.mCurrentTimeStamp;	 Catch:{ Exception -> 0x01c4 }
        r9 = com.blink.academy.onetake.model.DraftModel.getDraftPreviewPath(r7);	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.forFilterBitmap;	 Catch:{ Exception -> 0x01c4 }
        if (r7 == 0) goto L_0x0177;
    L_0x0157:
        r0 = r22;
        r7 = r0.forFilterBitmap;	 Catch:{ Exception -> 0x01c4 }
        r7 = r7.isRecycled();	 Catch:{ Exception -> 0x01c4 }
        if (r7 != 0) goto L_0x0177;
    L_0x0161:
        r0 = r22;
        r7 = r0.forFilterBitmap;	 Catch:{ FileNotFoundException -> 0x01bf }
        r8 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ FileNotFoundException -> 0x01bf }
        r20 = 92;
        r21 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x01bf }
        r0 = r21;
        r0.<init>(r9);	 Catch:{ FileNotFoundException -> 0x01bf }
        r0 = r20;
        r1 = r21;
        r7.compress(r8, r0, r1);	 Catch:{ FileNotFoundException -> 0x01bf }
    L_0x0177:
        r13.destroy();	 Catch:{ Exception -> 0x01c4 }
        r13.destroySecondary();	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r7 = r0.mFilterModel;	 Catch:{ Exception -> 0x01c4 }
        r0 = r22;
        r8 = r0.mDraftLongVideoBean;	 Catch:{ Exception -> 0x01c4 }
        r20 = 1;
        r0 = r22;
        r1 = r20;
        r0.saveDraft(r7, r8, r1);	 Catch:{ Exception -> 0x01c4 }
    L_0x018e:
        r0 = r22;
        r7 = r0.va;
        if (r7 == 0) goto L_0x01a1;
    L_0x0194:
        r0 = r22;
        r7 = r0.hadStartStablize;
        if (r7 == 0) goto L_0x01a1;
    L_0x019a:
        r0 = r22;
        r7 = r0.va;
        r7.resume();
    L_0x01a1:
        if (r6 == 0) goto L_0x0006;
    L_0x01a3:
        r6.destroy();
        goto L_0x0006;
    L_0x01a8:
        r16 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        r0 = r22;
        r7 = r0.mVideoWidth;	 Catch:{ Exception -> 0x01c4 }
        r7 = r7 * r16;
        r7 = (float) r7;	 Catch:{ Exception -> 0x01c4 }
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = r7 * r8;
        r0 = r22;
        r8 = r0.mVideoHeight;	 Catch:{ Exception -> 0x01c4 }
        r8 = (float) r8;	 Catch:{ Exception -> 0x01c4 }
        r7 = r7 / r8;
        r0 = (int) r7;	 Catch:{ Exception -> 0x01c4 }
        r17 = r0;
        goto L_0x013b;
    L_0x01bf:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x01c4 }
        goto L_0x0177;
    L_0x01c4:
        r10 = move-exception;
    L_0x01c5:
        r10.printStackTrace();	 Catch:{ all -> 0x0201 }
        java.lang.System.gc();	 Catch:{ all -> 0x0201 }
        r0 = r22;
        r7 = r0.va;
        if (r7 == 0) goto L_0x01de;
    L_0x01d1:
        r0 = r22;
        r7 = r0.hadStartStablize;
        if (r7 == 0) goto L_0x01de;
    L_0x01d7:
        r0 = r22;
        r7 = r0.va;
        r7.resume();
    L_0x01de:
        if (r6 == 0) goto L_0x0006;
    L_0x01e0:
        r6.destroy();
        goto L_0x0006;
    L_0x01e5:
        r7 = move-exception;
        r6 = r18;
    L_0x01e8:
        r0 = r22;
        r8 = r0.va;
        if (r8 == 0) goto L_0x01fb;
    L_0x01ee:
        r0 = r22;
        r8 = r0.hadStartStablize;
        if (r8 == 0) goto L_0x01fb;
    L_0x01f4:
        r0 = r22;
        r8 = r0.va;
        r8.resume();
    L_0x01fb:
        if (r6 == 0) goto L_0x0200;
    L_0x01fd:
        r6.destroy();
    L_0x0200:
        throw r7;
    L_0x0201:
        r7 = move-exception;
        goto L_0x01e8;
    L_0x0203:
        r10 = move-exception;
        r6 = r18;
        goto L_0x01c5;
    L_0x0207:
        r6 = r18;
        goto L_0x018e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.ui.activity.video.FilterActivity.saveVideoAndPreViewForDraft(com.blink.academy.onetake.VideoTools.EGL10Helper, boolean):void");
    }

    private void saveFilterInfoAndLongBitmapForDraft() {
        if (2 != this.mDataFrom) {
            DraftBoxManager.getInstance().saveMoreShortLongThumbnail(this.mDraftModel);
        }
    }

    private void pauseVidAnalysis() {
        if (this.va != null && this.hadStartStablize) {
            long t1 = System.nanoTime();
            this.va.pause();
            long t2 = System.nanoTime();
            Log.d(TAG, String.format("stopped video analysis in %d ms", Long.valueOf((t2 - t1) / 1000000)));
        }
    }

    private LongVideosModel.AudioVolume getLastAudioVolume(List<LongVideosModel> musicModels) {
        if (TextUtil.isValidate(musicModels)) {
            ArrayList<LongVideosModel.AudioVolume> volumes = musicModels.get(musicModels.size() - 1).getAudioVolumes();
            if (TextUtil.isValidate(volumes)) {
                return volumes.get(volumes.size() - 1);
            }
        }
        return null;
    }

    private boolean encodeLocalVideo(int frameRate, long videoProgressTag, int localWidth, int localHeight, Playlist playlist, int startProgress, boolean needStop, String localFilePath) {
        //boolean encodeSuccess2 = this.mSurfaceView.saveLongVideo((HashMap) EGL10Helper.withContext("encodeLocalVideo", 38.lambdaFactory$(this)), videoProgressTag, localWidth, localHeight, localFilePath, frameRate, playlist, startProgress, true, needStop);
        LogUtil.d("savevideo", "getlocalvideo");
        //return encodeSuccess2;
        Toast.makeText(this,"encodeLocalVideo...",Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean encodeUploadVideo(int frameRate, long videoProgressTag, int mVideoPreviewWidth, int mVideoPreviewHeight, Playlist playlist, boolean needStop) {
//        boolean encodeSuccess = this.mSurfaceView.saveLongVideo((HashMap) EGL10Helper.withContext("encodeUploadVideo", 39.lambdaFactory$(this)), videoProgressTag, mVideoPreviewWidth, mVideoPreviewHeight, this.mOutPath, frameRate, playlist, 20, false, needStop);
//        LogUtil.d("savevideo", "getuploadvideo");
//        return encodeSuccess;
        Toast.makeText(this,"encodeUploadVideo...",Toast.LENGTH_LONG).show();
        return false;
    }

    private void encodeVideoAndSavePicture(final long videoProgressTag, final int width, final int height) {
        this.mSurfaceView.stopVideoFilterAnimation();
        this.mSurfaceView.stopLongVideo();
        stopProxyVideo();
        final String saveLongThumbnailPath = MovieFileUtil.getLongThumbnailFilePath(this.mCurrentTimeStamp);
        final String saveMoreShortLongThumbnailPath = MovieFileUtil.getMoreShortLongThumbnailFilePath(this.mCurrentTimeStamp);
        String previewPicturePath = MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0);
        this.mOutPath = MovieFileUtil.getOutputMediaFile(this.mCurrentTimeStamp);
        String userName = GlobalHelper.getUserScreenName();
        String artistStr = "";
        String musicStr = "";
        if (this.mVideoAudioManager != null) {
            ArrayList<LongVideosModel> musicModelList = this.mVideoAudioManager.getMusicModelList();
            if (TextUtil.isValidate(musicModelList)) {
                LongVideosModel model = musicModelList.get(0);
                if (model != null) {
                    AudioTrackBean audioTrackBean = model.getAudioTrackBean();
                    if (audioTrackBean != null) {
                        artistStr = audioTrackBean.getPerformer_en();
                        musicStr = audioTrackBean.getTitle_en();
                    }
                }
            }
        }
        int[] mVideoPreviewWidthHeights = FilterViewUtils.getUploadLongVideoWidthHeight(width, height, false);
        int mVideoPreviewWidth = mVideoPreviewWidthHeights[0];
        mVideoPreviewWidth += mVideoPreviewWidth % 2;
        int mVideoPreviewHeight = mVideoPreviewWidthHeights[1];
        mVideoPreviewHeight += mVideoPreviewHeight % 2;
        Playlist playlist = FilterViewUtils.getCurrentPlaylist(this.mSurfaceView);
        if (playlist != null) {
            int i;
            long lastDurationUs = playlist.getDurationUs();
            int[] mWidthHeights = FilterViewUtils.getUploadVideoLongBitmapsWidthHeight(width, height);
            int longWidth = mWidthHeights[0];
            int longHeight = mWidthHeights[1];
            longWidth += longWidth % 2;
            longHeight += longHeight % 2;
            if (TextUtil.isValidate(musicStr)) {
                musicStr = musicStr.toUpperCase();
            }
            Bitmap endBitmap = WaterMarkBitmapUtil.getTailWaterMarkBitmap(this, mVideoPreviewWidth, mVideoPreviewHeight, userName, musicStr, artistStr, 0);
            long videoSumDurations = playlist.getDurationUs();
            playlist.add(endBitmap, lastDurationUs, 0, 3000000, Player.AspectMode.RENDER_CROP).setZIndex(1000).setOutputRect(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f).setAlpha(1.0f);
            checkSetEndBgmFadeOut(playlist, videoSumDurations);
            ArrayList<LongVideosModel> models = this.mVideoAudioManager.getVideosModelList();
            List<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
            if (textModelList != null && textModelList.size() > 0) {
                int size = textModelList.size();
                for (i = 0; i < size; i++) {
                    LongVideosModel textModel = (LongVideosModel) textModelList.get(i);
                    Bitmap textBitmap = textModel.getTextBitmap(this, this.mVideoWidth, this.mVideoHeight);
                    textModel.setTextBitmap(textBitmap);
                    Playlist.Entry entry = playlist.add(textBitmap, textModel.getMediaStartTimeUs(), 0, textModel.getOriginalCurrentDuration() * 1000);
                    entry.setSpeed(textModel.getVideoSpeed());
                    float[] textSizeInfo = textModel.getTextSizeInfo(textBitmap, this.mVideoWidth, this.mVideoHeight);
                    entry.setOutputRect(textSizeInfo[0], textSizeInfo[1], textSizeInfo[2], textSizeInfo[3]);
                    entry.setAlpha(textSizeInfo[4]);
                    entry.setZIndex(1000);
                }
            }
            //this.mSurfaceView.saveLongVideoPreviewPicture((HashMap) EGL10Helper.withContext("saveLongVideoPreviewPicture", 40.lambdaFactory$(this)), 0, mVideoPreviewWidth, mVideoPreviewHeight, previewPicturePath, playlist);
            Toast.makeText(this,"fake saveLongVideoPreviewPicture",Toast.LENGTH_LONG).show();
            LogUtil.d("savevideo", "getpreview");
            EventBus.getDefault().post(new VideoSavingEvent(5, "previewBimtap", videoProgressTag));
            HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> longFilter = null;//(HashMap) EGL10Helper.withContext("getFinalFilterFotVideo", 41.lambdaFactory$(this));
            File file = new File(saveLongThumbnailPath);
            if (file.exists()) {
                file.delete();
            }
            file = new File(saveMoreShortLongThumbnailPath);
            if (file.exists()) {
                file.delete();
            }
            List<Bitmap> videoLongBitmaps = this.mSurfaceView.getVideoLongBitmaps(longFilter, 15, longWidth, longHeight, videoProgressTag, playlist);
            if (videoLongBitmaps == null || videoLongBitmaps.size() <= 0) {
                this.mVideoAudioManager.recyclerTextBitmap();
                return;
            }
            int bitmapSize = videoLongBitmaps.size();
            LogUtil.d("savevideo", "getlongbitmaps");
            Bitmap longThumbnailBitmap = Bitmap.createBitmap(400, (int) (400.0f * ((float) bitmapSize)), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(longThumbnailBitmap);
            Bitmap moreShortLongThumbnailBitmap = Bitmap.createBitmap(200, (int) (200.0f * ((float) bitmapSize)), Bitmap.Config.RGB_565);
            Canvas moreShortCanvas = new Canvas(moreShortLongThumbnailBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int top = 0;
            int x = 0;
            int y = 0;
            int targetWidthHeight;
            if (longWidth > longHeight) {
                targetWidthHeight = longHeight;
                x = (int) ((((float) (longWidth - longHeight)) * 1.0f) / 2.0f);
            } else {
                targetWidthHeight = longWidth;
                y = (int) ((((float) (longHeight - longWidth)) * 1.0f) / 2.0f);
            }
            int moreShortTop = 0;
            for (i = 0; i < bitmapSize; i++) {
                Bitmap bitmap = (Bitmap) videoLongBitmaps.get(i);
                if (bitmap != null) {
                    int bitmapWidth = bitmap.getWidth();
                    int bitmapHeight = bitmap.getHeight();
                    int moreShort = Math.min(bitmapWidth, bitmapHeight);
                    float finalLength = 400.0f > ((float) moreShort) ? (float) moreShort : 400.0f;
                    if (((int) finalLength) + x > bitmapWidth) {
                        x = bitmapWidth - ((int) finalLength);
                    }
                    if (((int) finalLength) + y > bitmapHeight) {
                        y = bitmapHeight - ((int) finalLength);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, Math.max(0, x), Math.max(0, y), (int) finalLength, (int) finalLength, null, false);
                    canvas.drawBitmap(bitmap, StaticLayoutUtil.DefaultSpacingadd, (float) top, paint);
                    Bitmap moreShortBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                    moreShortCanvas.drawBitmap(moreShortBitmap, StaticLayoutUtil.DefaultSpacingadd, (float) moreShortTop, paint);
                    moreShortTop = (int) (((float) moreShortTop) + 200.0f);
                    top = (int) (((float) top) + 400.0f);
                    bitmap.recycle();
                    moreShortBitmap.recycle();
                }
            }
//            FileUtil.writeJPGFileToDisk(saveLongThumbnailPath, longThumbnailBitmap, 92, true);
//            FileUtil.writeJPGFileToDisk(saveMoreShortLongThumbnailPath, moreShortLongThumbnailBitmap, 92, true);
            for (i = videoLongBitmaps.size() - 1; i >= 0; i--) {
                BitmapUtils.recycleBitmap((Bitmap) videoLongBitmaps.remove(i));
            }
            EventBus.getDefault().post(new VideoSavingEvent(20, "longBitmaps", videoProgressTag));
            int frameRate = ((LongVideosModel) models.get(0)).getVideoFPS();
            if (SharedPrefUtil.getAppInfoBoolean(Constants.IF_SAVE_WATER_MARK, true)) {
                Encode_Video_Percent = 0.4f;
                if (encodeUploadVideo(frameRate, videoProgressTag, mVideoPreviewWidth, mVideoPreviewHeight, playlist, false)) {
                    String localFilePath;
                    int[] mLocalWidthHeights = FilterViewUtils.getUploadLongVideoWidthHeight(width, height, true);
                    int localWidth = mLocalWidthHeights[0];
                    int localHeight = mLocalWidthHeights[1];
                    localWidth += localWidth % 2;
                    localHeight += localHeight % 2;
                    ShareBitmapEntity entity = WaterMarkBitmapUtil.getVideoShareWaterMarkBitmap(getActivity(), localWidth, localHeight, 4);
                    playlist.add(entity.getBitmap(), 0, 0, lastDurationUs).setZIndex(1000).setOutputRect(entity.getSize()[0].floatValue(), entity.getSize()[1].floatValue(), entity.getSize()[2].floatValue(), entity.getSize()[3].floatValue()).setAlpha(entity.getSize()[4].floatValue());
                    if (PermissionUtil.verifyReadExternalPermission()) {
                        localFilePath = Config.getVideoSavePath() + "" + System.currentTimeMillis() + ".mp4";
                    } else {
                        localFilePath = Config.getLongVideoRecordPath() + "/" + System.currentTimeMillis() + ".mp4";
                    }
                    if (encodeLocalVideo(frameRate, videoProgressTag, localWidth, localHeight, playlist, 60, false, localFilePath)) {
                        this.mVideoAudioManager.recyclerTextBitmap();
                        VideoSavingEvent videoSavingEvent = new VideoSavingEvent();
                        videoSavingEvent.getClass();
                        VideoSavingEvent.VideoBean videoBean = new VideoSavingEvent.VideoBean();
                        videoBean.videoWidth = localWidth;
                        videoBean.videoHeight = localHeight;
                        videoBean.videoPath = localFilePath;
                        VideoSavingEvent savingEvent = new VideoSavingEvent(100, "longPic", videoProgressTag, videoBean);
                        VideoCoverInfo coverInfo = new VideoCoverInfo();
                        coverInfo.videoWidth = mVideoPreviewWidth;
                        coverInfo.videoHeight = mVideoPreviewHeight;
                        coverInfo.uploadVideoPath = this.mOutPath;
                        coverInfo.uploadPreivewPath = previewPicturePath;
                        coverInfo.uploadLongPath = saveLongThumbnailPath;
                        coverInfo.uploadShortPath = saveMoreShortLongThumbnailPath;
                        savingEvent.coverInfo = coverInfo;
                        EventBus.getDefault().post(savingEvent);
                        //runOnUiThread(42.lambdaFactory$(this, videoProgressTag, saveLongThumbnailPath, saveMoreShortLongThumbnailPath, width, height));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lambda$run$0(videoProgressTag,saveLongThumbnailPath,saveMoreShortLongThumbnailPath,width,height);
                            }
                        });
                        FileUtil.notifySystemFileFlesh(getActivity(), localFilePath);
                        return;
                    }
                    this.mVideoAudioManager.recyclerTextBitmap();
                    if (entity != null) {
                        entity.recyclerBitmap();
                        return;
                    }
                    return;
                }
                this.mVideoAudioManager.recyclerTextBitmap();
                return;
            }
            Encode_Video_Percent = 0.4f;
            final int videoPreviewWidth = mVideoPreviewWidth;
            final int videoPreviewHeight = mVideoPreviewHeight;
            final int i2 = frameRate;
            final long j = videoProgressTag;
            final Playlist playlist2 = playlist;
            Thread t1 = new Thread() {
                public void run() {
                    if (!encodeUploadVideo(i2, j, videoPreviewWidth, videoPreviewHeight, playlist2, false)) {
                        mVideoAudioManager.recyclerTextBitmap();
                    }
                }
            };
            final int i3 = width;
            final int i4 = height;
            final int i5 = frameRate;
            final long j2 = videoProgressTag;
            final Playlist playlist3 = playlist;
            final int i6 = videoPreviewWidth;
            final int i7 = videoPreviewHeight;
            final String str = previewPicturePath;
            final String str2 = saveLongThumbnailPath;
            final String str3 = saveMoreShortLongThumbnailPath;
            Thread t2 = new Thread() {
                public void run() {
                    String localFilePath;
                    int[] mLocalWidthHeights = FilterViewUtils.getUploadLongVideoWidthHeight(i3, i4, true);
                    int localWidth = mLocalWidthHeights[0];
                    int localHeight = mLocalWidthHeights[1];
                    localWidth += localWidth % 2;
                    localHeight += localHeight % 2;
                    if (PermissionUtil.verifyReadExternalPermission()) {
                        localFilePath = Config.getVideoSavePath() + "" + System.currentTimeMillis() + ".mp4";
                    } else {
                        localFilePath = Config.getLongVideoRecordPath() + "/" + System.currentTimeMillis() + ".mp4";
                    }
                    if (encodeLocalVideo(i5, j2, localWidth, localHeight, playlist3, 60, false, localFilePath)) {
                        mVideoAudioManager.recyclerTextBitmap();
                        VideoSavingEvent videoSavingEvent = new VideoSavingEvent();
                        videoSavingEvent.getClass();
                        VideoSavingEvent.VideoBean videoBean = new VideoSavingEvent.VideoBean();
                        videoBean.videoWidth = localWidth;
                        videoBean.videoHeight = localHeight;
                        videoBean.videoPath = localFilePath;
                        VideoSavingEvent savingEvent = new VideoSavingEvent(100, "longPic", j2, videoBean);
                        VideoCoverInfo coverInfo = new VideoCoverInfo();
                        coverInfo.videoWidth = i6;
                        coverInfo.videoHeight = i7;
                        coverInfo.uploadVideoPath = mOutPath;
                        coverInfo.uploadPreivewPath = str;
                        coverInfo.uploadLongPath = str2;
                        coverInfo.uploadShortPath = str3;
                        savingEvent.coverInfo = coverInfo;
                        EventBus.getDefault().post(savingEvent);
                        //runOnUiThread(FilterActivity$61$.Lambda.1.lambdaFactory$(this, j2, str2, str3, i3, i4));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"无法执行1",Toast.LENGTH_LONG).show();
                            }
                        });
                        FileUtil.notifySystemFileFlesh(getActivity(), localFilePath);
                        return;
                    }
                    mVideoAudioManager.recyclerTextBitmap();
                }


            };
            t1.setName("encodeUploadVideo");
            t2.setName("encodeLocalVideo");
            try {
                t1.start();
                t1.join();
                t2.start();
                t2.join();
            } catch (InterruptedException e) {
            }
        }
    }
    public  /* synthetic */ void lambda$run$0(long videoProgressTag, String saveLongThumbnailPath, String saveMoreShortLongThumbnailPath, int width, int height) {
        saveFinishAndPostEvent(videoProgressTag, saveLongThumbnailPath, saveMoreShortLongThumbnailPath, width, height);
    }
    private void checkSetEndBgmFadeOut(Playlist playlist, long videoSumDurations) {
        LongVideosModel endMusicModel = null;
        ArrayList<LongVideosModel> musicModelList = this.mVideoAudioManager.getMusicModelList();
        if (musicModelList != null && musicModelList.size() > 0) {
            endMusicModel = musicModelList.get(musicModelList.size() - 1);
        }
        if (endMusicModel != null && (endMusicModel.getAudioStartTime() + endMusicModel.getAudioDuration()) * 1000 == videoSumDurations) {
            long totalD = endMusicModel.getTotalDuration();
            long shouldStartTime = endMusicModel.getEndMusicStart() + endMusicModel.getEndMusicDuration();
            long shouldDu = 2000000;
            if (2000000 + shouldStartTime > 1000 * totalD) {
                shouldDu = (1000 * totalD) - shouldStartTime;
            }
            LongVideosModel.AudioVolume audioVolume = getLastAudioVolume(musicModelList);
            Playlist.Entry entry = playlist.add(Playlist.FileMedia.create(new File(endMusicModel.getPlaylistMediaPath())), playlist.getDurationUs() - 3000000, shouldStartTime, shouldDu);
            entry.disableVideo();
            float start = 0.7f;
            float end = StaticLayoutUtil.DefaultSpacingadd;
            if (audioVolume != null) {
                start = audioVolume.getVolume();
                end = StaticLayoutUtil.DefaultSpacingadd;
            }
            entry.setVolume(InterpolatedFloat.createLinear(0, start, shouldDu, end));
        }
    }

    private void encodeGifOrPicture(final long videoProgressTag, final int width, final int height) {
        //EGL10Helper.withContext("encodeGifOrPicture", 43.lambdaFactory$(this, videoProgressTag, width, height));
        EGL10Helper.withContext("encodeGifOrPicture", new EGLRunnableVoid() {
            @Override
            public void run(EGL10Helper eGL10Helper) {
                lambda$encodeGifOrPicture$42(videoProgressTag,width,height,eGL10Helper);
            }
        });
    }

    private /* synthetic */ void lambda$encodeGifOrPicture$42(long videoProgressTag, int width, int height, EGL10Helper egl) {
        int degree;
        int i;
        VidStabilizer.Transform[] transforms;
        int bitmapWidth;
        int bitmapHeight;
        Bitmap bitmap;
        int size;
        long t1 = System.nanoTime();
        if (this.currentEffectType == FilterEffectManager.EffectType.ROTATE) {
            degree = this.rotate_but;
        } else {
            degree = (int) this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
        }
        GPUImageFilter finalFilterNormal = getFinalFilterEffect(this.mFilterName, this.strengthValue);
        LogUtil.d("savinggg before video");
        VideoEncoder encoder = new VideoEncoderSW(videoProgressTag);
        if (1 == this.mDataType) {
            this.mOutPath = MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0);
        } else {
            this.mOutPath = MovieFileUtil.getOutputMediaFile(this.mCurrentTimeStamp);
        }
        EGL10Helper.clearGLError("before encode");
        LogUtil.d(TAG, "size:::" + this.mVideoFrames.size() + this.mOutPath);
        int[] indexes = new int[this.mVideoFrames.size()];
        for (i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        if (this.mVideoFrames.mIsLooped) {
            transforms = this.transformsLooped;
        } else {
            transforms = getTransforms(indexes);
        }
        FrameRenderer renderer = new FrameRenderer();
        renderer.setVideoFrames(this.mVideoFrames);
        renderer.xOffset = this.mOffsetX;
        renderer.yOffset = this.mOffsetY;
        renderer.setTransforms(transforms);
        renderer.setEnableStabilizer(this.mSurfaceView.isStabilizerEnabled());
        GPUImageFilterGroup finalFilter_0 = null;
        GPUImageFilterGroup finalFilter_1 = null;
        int cropWidth = this.mVideoFrames.mCropWidth;
        int cropHeight = this.mVideoFrames.mCropHeight;
        String saveLongThumbnailPath = MovieFileUtil.getLongThumbnailFilePath(this.mCurrentTimeStamp);
        String saveMoreShortLongThumbnailPath = MovieFileUtil.getMoreShortLongThumbnailFilePath(this.mCurrentTimeStamp);
        GPUImageFilter[] filters;
        if (1 == this.mDataType) {
            double ratio;
            List<FilterEffectBean> mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
            boolean containItem = this.mFilterEffectManager.listAContainItem((List) mList, FilterEffectManager.EffectType.CROP);
            boolean aContainItem = this.mFilterEffectManager.listAContainItem((List) mList, FilterEffectManager.EffectType.ROTATE);
            if (cropWidth > cropHeight) {
                ratio = (((double) cropWidth) * 1.0d) / ((double) cropHeight);
            } else {
                ratio = (((double) cropHeight) * 1.0d) / ((double) cropWidth);
            }
            if (ratio > 1.5d) {
                containItem = true;
            }
            int _width = width;
            int _height = height;
            if (containItem || aContainItem) {
                _width = this.mVideoFrames.mCropWidth;
                _height = this.mVideoFrames.mCropHeight;
                _width += _width % 2;
                _height += _height % 2;
            }
            if (this.mVideoFrames.isRotated()) {
                int tmp = _width;
                _width = _height;
                _height = tmp;
            }
            filters = new GPUImageFilter[]{finalFilterNormal};
            renderer.setFilter(finalFilterNormal);
            int[] iArr = new int[]{0};
            boolean z = containItem || aContainItem;
            List<Bitmap> localBitmaps = renderer.getBitmaps2(egl, _width, _height, filters, iArr, true, z);
            if (localBitmaps != null && localBitmaps.size() != 0) {
                this.localPicture = (Bitmap) localBitmaps.remove(0);
                try {
                    this.localPicture.compress(CompressFormat.JPEG, 75, new FileOutputStream(MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0)));
                    ImagePropertyBean.getInstance().setFilterName(this.mFilterName);
                    //BitmapModel.getInstance().setFilterLocalBitmap(this.localPicture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmapWidth = this.localPicture.getWidth();
                bitmapHeight = this.localPicture.getHeight();
                if (bitmapWidth > bitmapHeight) {
                    bitmapHeight = (int) ((((float) bitmapHeight) * 400.0f) / ((float) bitmapWidth));
                    bitmapWidth = 400;
                } else {
                    bitmapWidth = (int) ((((float) bitmapWidth) * 400.0f) / ((float) bitmapHeight));
                    bitmapHeight = 400;
                }
                bitmap = Bitmap.createScaledBitmap(this.localPicture, bitmapWidth, bitmapHeight, false);
//                FileUtil.writeJPGFileToDisk(saveLongThumbnailPath, bitmap, 92, false);
//                FileUtil.writeJPGFileToDisk(saveMoreShortLongThumbnailPath, bitmap, 92, true);
                BitmapUtils.recycleBitmap(bitmap);
                System.gc();
                saveDraft(this.mFilterModel, this.mDraftLongVideoBean, true);
                EventBus.getDefault().post(new VideoPreviewEvent(videoProgressTag));
            } else {
                return;
            }
        }
        size = this.mVideoFrames.size();
        filters = new GPUImageFilter[size];
        boolean unStackFrame = this.mVideoFrames.mIsLooped || 1 == this.mDataType || this.mFps == StaticLayoutUtil.DefaultSpacingadd || !this.openStackFrame;
        if (unStackFrame) {
            GPUImageFilterGroup finalGroup0 = new GPUImageFilterGroup();
            finalGroup0.addFilter(finalFilterNormal);
            for (i = 0; i < size; i++) {
                filters[i] = finalGroup0;
            }
            renderer.setWrapFrame(0);
            renderer.setFilters(filters);
            LogUtil.d("huangweijie", String.format("video path saveVideo %s", new Object[]{this.mOutPath}));
            encoder.putFrames(this.mOutPath, width, height, renderer, false, VideoEncoder.Quality.HIGH);
        } else {
            finalFilter_0 = getFramesFilter(renderer, 0, finalFilterNormal);
            finalFilter_1 = getFramesFilter(renderer, 1, finalFilterNormal);
            for (i = 0; i < size; i++) {
                if (i == size - 2) {
                    filters[i] = finalFilter_0;
                } else if (i == size - 1) {
                    filters[i] = finalFilter_1;
                } else {
                    filters[i] = finalFilterNormal;
                }
            }
            renderer.setWrapFrame(2);
            renderer.setFilters(filters);
            encoder.putFrames(this.mOutPath, width, height, renderer, false, VideoEncoder.Quality.HIGH);
        }
        for (GPUImageFilter filter : filters) {
            filter.destroy();
            filter.destroySecondary();
        }
        finalFilterNormal.destroy();
        finalFilterNormal.destroySecondary();
        long t2 = System.nanoTime();
        Log.d(TAG, String.format("saving took %d ms", new Object[]{Long.valueOf((t2 - t1) / 1000000)}));
        EGL10Helper.clearGLError("before bitmaps");
        if (1 != this.mDataType) {
            int previewWidth;
            int previewHeight;
            if (this.mVideoHeight > this.mVideoWidth) {
                previewWidth = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
                previewHeight = (int) ((((float) (this.mVideoHeight * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) this.mVideoWidth));
            } else {
                previewHeight = Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2;
                previewWidth = (int) ((((float) (this.mVideoWidth * Constants.PHOTO_TYPE_RIGHT_TOP_HIDE_TIME2)) * 1.0f) / ((float) this.mVideoHeight));
            }
            LogUtil.d("xiaoluo", "previewWidth:" + previewWidth + ", previewHeight:" + previewHeight);
            renderer.xOffset = StaticLayoutUtil.DefaultSpacingadd;
            renderer.yOffset = StaticLayoutUtil.DefaultSpacingadd;
            previewWidth &= -2;
            previewHeight &= -2;
            int pictureIndex = 0;
            if (this.stack_frame_switch_rl != null && this.stack_frame_switch_rl.getVisibility() == View.VISIBLE && this.openStackFrame) {
                pictureIndex = 2;
            }
            int outWidth = previewWidth;
            int outHeight = previewHeight;
            if (degree == 90 || degree == 270) {
                outWidth = previewHeight;
                outHeight = previewWidth;
            }
            GPUImageFilter previewFilters = getFinalFilterEffect(this.mFilterName, this.strengthValue);
            ArrayList<Bitmap> bitmapList = renderer.getBitmaps(egl, outWidth, outHeight, new GPUImageFilter[]{previewFilters}, new int[]{pictureIndex});
            previewFilters.destroy();
            previewFilters.destroySecondary();
            if (bitmapList != null && bitmapList.size() != 0) {
                Bitmap preBitmap = (Bitmap) bitmapList.remove(0);
                if (preBitmap != null) {
                    try {
                        File file = new File(MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0));
                        if (file.exists()) {
                            file.delete();
                        }
                        preBitmap.compress(CompressFormat.JPEG, 92, new FileOutputStream(MovieFileUtil.getJPGFilePathWithIndex(this.mCurrentTimeStamp, 0)));
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                    }
                    preBitmap.recycle();
                    EventBus.getDefault().post(new VideoPreviewEvent(videoProgressTag));
                    LogUtil.d("infoinfo", "saveDraft : saveVideo ");
                    if (3 != this.mDataType) {
                        saveDraft(this.mFilterModel, this.mDraftLongVideoBean, true);
                    }
                }
            } else {
                return;
            }
        }
        if (this.mVideoFrames != null && this.mVideoFrames.size() != 0) {
            int jpgWidth;
            int jpgHeight;
            size = this.mVideoFrames.size();
            if (size > 15) {
                size = 15;
            }
            if (this.mVideoHeight > this.mVideoWidth) {
                jpgWidth = 400;
                jpgHeight = (int) ((((float) (this.mVideoHeight * 400)) * 1.0f) / ((float) this.mVideoWidth));
                float jpgMoreShortHeight = (((float) this.mVideoHeight) * 200.0f) / ((float) this.mVideoWidth);
            } else {
                jpgHeight = 400;
                jpgWidth = (int) ((((float) (this.mVideoWidth * 400)) * 1.0f) / ((float) this.mVideoHeight));
                float f = (((float) this.mVideoWidth) * 200.0f) / ((float) this.mVideoHeight);
            }
            if (size != 1) {
                LogUtil.d("xiaoluo", "jpgWidth:" + jpgWidth + ", jpgHeight:" + jpgHeight);
                Bitmap longThumbnailBitmap = Bitmap.createBitmap(400, (int) (400.0f * ((float) size)), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(longThumbnailBitmap);
                Bitmap moreShortLongThumbnailBitmap = Bitmap.createBitmap(200, (int) (200.0f * ((float) size)), Bitmap.Config.RGB_565);
                Canvas moreShortCanvas = new Canvas(moreShortLongThumbnailBitmap);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                int top = 0;
                int x = 0;
                int y = 0;
                if (this.mVideoHeight > this.mVideoWidth) {
                    y = (int) ((((((float) jpgHeight) * 1.0f) / 2.0f) + ((this.mOffsetY * ((((float) (jpgWidth * height)) * 1.0f) / ((float) width))) / 2.0f)) - ((((float) jpgWidth) * 1.0f) / 2.0f));
                } else {
                    x = (int) ((((((float) jpgWidth) * 1.0f) / 2.0f) - ((this.mOffsetX * ((((float) (jpgHeight * width)) * 1.0f) / ((float) height))) / 2.0f)) - ((((float) jpgHeight) * 1.0f) / 2.0f));
                }
                jpgWidth += jpgWidth % 2;
                jpgHeight += jpgHeight % 2;
                int moreShortTop = 0;
                LogUtil.d("xiaoluo", "x:" + x + ", y:" + y);
                int outjpgWidth = jpgWidth;
                int outjpgHeight = jpgHeight;
                if (degree == 90 || degree == 270) {
                    outjpgWidth = jpgHeight;
                    outjpgHeight = jpgWidth;
                }
                GPUImageFilter longPicFilters = getFinalFilterEffect(this.mFilterName, this.strengthValue);
                i = 0;
                while (i < size) {
                    ArrayList<Bitmap> bitmaps = renderer.getBitmaps(egl, outjpgWidth, outjpgHeight, new GPUImageFilter[]{longPicFilters}, new int[]{i});
                    if (bitmaps != null && bitmaps.size() >= 1) {
                        bitmap = (Bitmap) bitmaps.remove(0);
                        if (bitmap != null) {
                            bitmapWidth = bitmap.getWidth();
                            bitmapHeight = bitmap.getHeight();
                            int moreShort = Math.min(bitmapWidth, bitmapHeight);
                            float finalLength = 400.0f > ((float) moreShort) ? (float) moreShort : 400.0f;
                            if (((int) finalLength) + x > bitmapWidth) {
                                x = bitmapWidth - ((int) finalLength);
                            }
                            if (((int) finalLength) + y > bitmapHeight) {
                                y = bitmapHeight - ((int) finalLength);
                            }
                            LogUtil.d("huangweijie", "progress event.Progress4 : " + videoProgressTag);
                            EventBus.getDefault().post(new VideoSavingEvent((int) ((20.0f + ((70.0f * ((float) i)) / ((float) size))) + 5.0f), "longPic", videoProgressTag));
                            bitmap = Bitmap.createBitmap(bitmap, Math.max(0, x), Math.max(0, y), (int) finalLength, (int) finalLength, null, false);
                            canvas.drawBitmap(bitmap, StaticLayoutUtil.DefaultSpacingadd, (float) top, paint);
                            Bitmap moreShortBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                            moreShortCanvas.drawBitmap(moreShortBitmap, StaticLayoutUtil.DefaultSpacingadd, (float) moreShortTop, paint);
                            moreShortTop = (int) (((float) moreShortTop) + 200.0f);
                            top = (int) (((float) top) + 400.0f);
                            bitmap.recycle();
                            moreShortBitmap.recycle();
                        }
                        i++;
                    } else {
                        return;
                    }
                }
                longPicFilters.destroy();
                longPicFilters.destroySecondary();
                //FileUtil.writeJPGFileToDisk(saveLongThumbnailPath, longThumbnailBitmap, 92, true);
                longThumbnailBitmap.recycle();
                //FileUtil.writeJPGFileToDisk(saveMoreShortLongThumbnailPath, moreShortLongThumbnailBitmap, 92, true);
                moreShortLongThumbnailBitmap.recycle();
            }
            renderer.partialReset();
            renderer.destroy();
            saveFilterInfoAndLongBitmapForDraft();
            LogUtil.d("huangweijie", "progress event.Progress3 : " + videoProgressTag);
            EventBus.getDefault().post(new VideoSavingEvent(100, "longPic", videoProgressTag));
            if (finalFilter_0 != null) {
                finalFilter_0.destroy();
                finalFilter_0.destroySecondary();
            }
            if (finalFilter_1 != null) {
                finalFilter_1.destroy();
                finalFilter_1.destroySecondary();
            }
            //runOnUiThread(47.lambdaFactory$(this, videoProgressTag, saveLongThumbnailPath, saveMoreShortLongThumbnailPath, width, height));
            Toast.makeText(this,"fake 47.lambdaFactory$",Toast.LENGTH_LONG).show();
        }
    }

    private void saveFinishAndPostEvent(long videoProgressTag, String saveLongThumbnailPath, String saveMoreShortLongThumbnailPath, int finalWidth, int finalHeight) {
        if (2 != this.mDataFrom) {
            EventBus eventBus = EventBus.getDefault();
            int i = (this.mVideoFrames == null || !this.mVideoFrames.mIsLooped) ? 1500 : 1000;
            eventBus.post(new VideoDurationEvent(i));
            if (this.mDataFrom == 0) {
                EventBus.getDefault().post(new VideoSavedEvent(this.mCurrentTimeStamp, getFilterId(), finalWidth, finalHeight, 0, videoProgressTag));
            } else {
                EventBus.getDefault().post(new VideoSavedEvent(this.mCurrentTimeStamp, getFilterId(), finalWidth, finalHeight, 0, videoProgressTag));
            }
        } else if (!new File(this.mOutPath).exists()) {
            String msg = "";
            if (this.mShareType == 0) {
                msg = String.format(getActivity().getResources().getString(R.string.ALERT_FILE_SAVED_FAIL), getActivity().getResources().getString(R.string.MEDIA_TYPE_GIF));
            } else if (this.mShareType == 1) {
                msg = String.format(getActivity().getResources().getString(R.string.ALERT_FILE_SAVED_FAIL), getActivity().getResources().getString(R.string.MEDIA_TYPE_PHOTO));
            } else if (this.mShareType == 2) {
                msg = String.format(getActivity().getResources().getString(R.string.ALERT_FILE_SAVED_FAIL), getActivity().getResources().getString(R.string.MEDIA_TYPE_VIDEO));
            }
            AppMessage.makeAlertTextHigherNoStatusBarChange(getActivity(), msg);
        } else if (TextUtils.equals(this.mActivityFrom, "FromAvatar")) {
            EventBus.getDefault().post(new FinishActivityMessageEvent(FinishActivityMessageEvent.AvatarChangePath, saveLongThumbnailPath, saveMoreShortLongThumbnailPath, this.mFromUserName, this.mFromCurrentTimeStamp));
        } else if (TextUtils.equals(this.mActivityFrom, "FromAvatar")) {
            EventBus.getDefault().post(new FinishActivityMessageEvent(FinishActivityMessageEvent.AvatarMeChangePath, saveLongThumbnailPath, saveMoreShortLongThumbnailPath, this.mFromUserName, this.mFromCurrentTimeStamp));
        }
    }

    private void saveVideo(long videoProgressTag, int width, int height) {
        pauseVidAnalysis();
        final long j = videoProgressTag;
        final int i = width;
        final int i2 = height;
        App.encoders.addTask("saveVideo", new Runnable() {
            public void run() {
                if (mDataType == 2) {
                    encodeVideoAndSavePicture(j, i, i2);
                } else if (mVideoFrames.size() != 0) {
                    encodeGifOrPicture(j, i, i2);
                }
            }
        });
    }

    private boolean isCurrentTypeAndShow(FilterEffectManager.EffectType type) {
        return this.currentEffectType == type && this.filter_rule_ll.getVisibility() == View.VISIBLE;
    }

    private void getFinalNonLUTFilters(GPUImageFilterGroup filterGroup, FilterEffectBean bean) {
        if (bean.effectType != FilterEffectManager.EffectType.HORIZONTAL && bean.effectType != FilterEffectManager.EffectType.VERTICAL && bean.effectType != FilterEffectManager.EffectType.ROTATE) {
            if (bean.effectType == FilterEffectManager.EffectType.BEAUTIFY) {
                GPUImageBilateralFilter0 bilateralFilter = getBilateralFilter();
                bilateralFilter.setBlurWeightLocation(bean.value[1] / 10.0f);
                filterGroup.addFilter(bilateralFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.SHARPEN) {
                GPUImageSharpenFilter sharpenFilter = new GPUImageSharpenFilter();
                sharpenFilter.setSharpness(bean.value[1] * 0.26f);
                filterGroup.addFilter(sharpenFilter);
                return;
            }
            float value;
            if (bean.effectType == FilterEffectManager.EffectType.SKY) {
                AFGPUImageSkyFilter2 skyFilter = getSkyFilter();
                if (this.mBitmapSky == null) {
                    this.mBitmapSky = getOriginalFilterBitmap(R.drawable.sky_gradient, false, false);
                }
                skyFilter.setBitmap(this.mBitmapSky);
                value = bean.value[1] / 10.0f;
                if (isCurrentTypeAndShow(bean.effectType)) {
                    value = this.filter_custom_sk.getProgressFloat() / 10.0f;
                }
                skyFilter.setOverPercent(value);
                filterGroup.addFilter(skyFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.GRAIN) {
                AFGPUImageOverlayBlendFilter grainFilter = getGrainFilter();
                if (this.mBitmapGrain == null) {
                    this.mBitmapGrain = getBitmapForFilterEffect(R.drawable.grains_iso_400_jpg_50, true, false);
                }
                grainFilter.setBitmap(this.mBitmapGrain);
                value = bean.value[1] / 10.0f;
                if (isCurrentTypeAndShow(bean.effectType)) {
                    value = this.filter_custom_sk.getProgressFloat() / 10.0f;
                }
                grainFilter.setOverPercent(value);
                filterGroup.addFilter(grainFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.TILT) {
                GPUImageGaussianSelectiveBlurFilter tiltFilter = getGaussianFilter();
                value = (bean.value[1] * 0.4f) + 1.0f;
                if (isCurrentTypeAndShow(bean.effectType)) {
                    value = (this.filter_custom_sk.getProgressFloat() * 0.4f) + 1.0f;
                }
                tiltFilter.setBlurSize(value);
                filterGroup.addFilter(tiltFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.VIGNETTE) {
                AFGPUImageMultiplyBlendFilter movieFilter = getMoviewFilter2();
                if (this.mBitmapCamera == null) {
                    this.mBitmapCamera = getBitmapForFilterEffect(R.drawable.vignette_020_camera_jpg_70, false, false);
                }
                movieFilter.setBitmap(this.mBitmapCamera);
                value = bean.value[1] / 10.0f;
                if (isCurrentTypeAndShow(bean.effectType)) {
                    value = this.filter_custom_sk.getProgressFloat() / 10.0f;
                }
                movieFilter.setOverPercent(value);
                filterGroup.addFilter(movieFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.PRISM) {
                GPUImagePrismFilter finalPrismFilter = getPrismFilter();
                finalPrismFilter.setStrength(bean.value[1] / 10.0f);
                filterGroup.addFilter(finalPrismFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.DATE) {
                AFGPUImageDateBlendFilter finalDateFilter;
                if (this.dateFilter != null) {
                    finalDateFilter = this.dateFilter.cloneFilter();
                } else {
                    finalDateFilter = getDateFilter();
                    finalDateFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    finalDateFilter.setBitmap(getDateBitmap());
                }
                filterGroup.addFilter(finalDateFilter);
                return;
            }
            float[] fArr;
            float[] selectPosition;
            if (bean.effectType == FilterEffectManager.EffectType.DUST) {
                AFGPUImageScreenBlendFilter finalDustFilter;
                if (this.dustFilter != null) {
                    finalDustFilter = this.dustFilter.cloneFilter();
                } else {
                    finalDustFilter = new AFGPUImageScreenBlendFilter(StaticLayoutUtil.DefaultSpacingadd);
                    finalDustFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    fArr = new float[8];
                    finalDustFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                    selectPosition = bean.selectPosition;
                    finalDustFilter.setDepth((int) selectPosition[1]);
                    if (selectPosition[0] == StaticLayoutUtil.DefaultSpacingadd) {
                        finalDustFilter.setBitmap(this.mBitmapDust1, 0);
                    } else if (selectPosition[0] == 1.0f) {
                        finalDustFilter.setBitmap(this.mBitmapDust2, 0);
                    } else if (selectPosition[0] == 2.0f) {
                        finalDustFilter.setBitmap(this.mBitmapDust3, 0);
                    } else if (selectPosition[0] == 3.0f) {
                        finalDustFilter.setBitmap(this.mBitmapDust4, 0);
                    } else if (selectPosition[0] == 4.0f) {
                        finalDustFilter.setBitmap(this.mBitmapDust5, 0);
                    }
                    finalDustFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                    if (!checkHasCropAndFixLeak()) {
                        finalDustFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(finalDustFilter.getCurBuffer(), (int) this.currentDegree));
                    }
                    value = bean.value[1] / 10.0f;
                    if (isCurrentTypeAndShow(bean.effectType)) {
                        value = this.filter_custom_sk.getProgressFloat() / 10.0f;
                    }
                    finalDustFilter.setOverPercent(value);
                }
                filterGroup.addFilter(finalDustFilter);
                return;
            }
            if (bean.effectType == FilterEffectManager.EffectType.LEAK) {
                AFGPUImageScreenBlendFilter finalLeakFilter;
                if (this.leakFilter != null) {
                    finalLeakFilter = this.leakFilter.cloneFilter();
                } else {
                    finalLeakFilter = new AFGPUImageScreenBlendFilter(StaticLayoutUtil.DefaultSpacingadd);
                    finalLeakFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    fArr = new float[8];
                    finalLeakFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                    selectPosition = bean.selectPosition;
                    finalLeakFilter.setDepth((int) selectPosition[1]);
                    if (selectPosition[0] == StaticLayoutUtil.DefaultSpacingadd) {
                        finalLeakFilter.setBitmap(this.mBitmapLeak1, 1);
                    } else if (selectPosition[0] == 1.0f) {
                        finalLeakFilter.setBitmap(this.mBitmapLeak2, 1);
                    } else if (selectPosition[0] == 2.0f) {
                        finalLeakFilter.setBitmap(this.mBitmapLeak3, 2);
                    } else if (selectPosition[0] == 3.0f) {
                        finalLeakFilter.setBitmap(this.mBitmapLeak4, 3);
                    }
                    finalLeakFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                    if (!checkHasCropAndFixLeak()) {
                        finalLeakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(finalLeakFilter.getCurBuffer(), (int) this.currentDegree));
                    }
                    value = bean.value[1] / 10.0f;
                    if (isCurrentTypeAndShow(bean.effectType)) {
                        value = this.filter_custom_sk.getProgressFloat() / 10.0f;
                    }
                    finalLeakFilter.setOverPercent(value);
                }
                filterGroup.addFilter(finalLeakFilter);
                return;
            }
            if (bean.effectType != FilterEffectManager.EffectType.CROP) {
                if (bean.effectType == FilterEffectManager.EffectType.MIRROR) {
                    GPUImageMirrorFilter gpuImageMirrorFilter = new GPUImageMirrorFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
                    float result = bean.selectPosition[0] / 100.0f;
                    switch ((int) bean.value[0]) {
                        case 0:
                            gpuImageMirrorFilter.setMirrorStartAndOrientation(1.0f, 1.0f, this.orientation);
                            break;
                        case 1:
                            gpuImageMirrorFilter.setMirrorStartAndOrientation(result, 1.0f, this.orientation);
                            break;
                        case 2:
                            gpuImageMirrorFilter.setMirrorStartAndOrientation(result, 3.0f, this.orientation);
                            break;
                        case 3:
                            gpuImageMirrorFilter.setMirrorStartAndOrientation(result, 4.0f, this.orientation);
                            break;
                        case 4:
                            gpuImageMirrorFilter.setMirrorStartAndOrientation(result, 2.0f, this.orientation);
                            break;
                    }
                    filterGroup.addFilter(gpuImageMirrorFilter);
                }
            } else if (this.gpuImageCropFilter != null) {
                filterGroup.addFilter(this.gpuImageCropFilter.cloneFilter());
            } else {
                float[] selectPosition1 = bean.selectPosition;
                if (selectPosition1 != null && selectPosition1.length > 7) {
                    float x = selectPosition1[4];
                    float y = selectPosition1[5];
                    float width = selectPosition1[6];
                    float height = selectPosition1[7];
                    if (x >= StaticLayoutUtil.DefaultSpacingadd && y >= StaticLayoutUtil.DefaultSpacingadd && width <= 1.0f && height <= 1.0f && x + width <= 1.0f && y + height <= 1.0f) {
                        filterGroup.addFilter(new GPUImageCropFilter(new GPUImageCropFilter.CropRegion(x, y, width, height), 0, this.mCaptureOrientation, this.mCameraLensType == 1));
                    }
                }
            }
        } else if (!this.addFinalTransformFilter) {
            this.addFinalTransformFilter = true;
            AFGPUImageTransformFilter transformFilter = getTransformFilter();
            if (this.transformFilter != null) {
                transformFilter.setTransform3D(this.transformFilter.getTransform3D());
                transformFilter.setOrientation(this.transformFilter.getOrientation());
            }
            transformFilter.setIgnoreAspectRatio(false);
            filterGroup.addFilter(transformFilter);
        }
    }

    private synchronized HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> getFinalFilterForVideo() {
        HashMap<HashMap<Integer, GPUImageFilter>, HashMap<Integer, GPUImageFilter>> finalFilters;
        EGL10Helper.clearGLError("before finalFilter");
        GLES20.glDisable(3042);
        List<LongVideosModel> videoModels = this.mVideoAudioManager.getVideosModelList();
        boolean addedPublicFilter = false;
        GPUImageFilterGroup publicFilter = null;
        HashMap<Integer, GPUImageFilter> publicArray = new HashMap();
        HashMap<Integer, GPUImageFilter> privateArray = new HashMap();
        HashMap<String, Integer> privateCache = new HashMap();
        finalFilters = new HashMap();
        int size = videoModels.size();
        for (int i = 0; i < size; i++) {
            LongVideosModel videoModel = (LongVideosModel) videoModels.get(i);
            int filterState = videoModel.getFilterState();
            if (filterState == 0) {
                String publicFilterName = videoModel.getPublicFilterName();
                float publicIntensity = videoModel.getPublicIntensity();
                if (!addedPublicFilter) {
                    addedPublicFilter = true;
                    new GPUImageFilterGroup().addFilter(getDefaultVideoFilter(true));
                    publicFilter = getFinalFilterEffect(publicFilterName, publicIntensity, videoModel);
                }
                publicArray.put(Integer.valueOf(i), publicFilter);
            } else if (filterState == 1) {
                String privateFilterName = videoModel.getPrivateFilterName();
                float privateIntensity = videoModel.getPrivateIntensity();
                if (privateCache.containsKey(privateFilterName)) {
                    privateArray.put(Integer.valueOf(i), (GPUImageFilter) privateArray.get(Integer.valueOf(((Integer) privateCache.get(privateFilterName)).intValue())));
                } else {
                    new GPUImageFilterGroup().addFilter(getDefaultVideoFilter(true));
                    GPUImageFilterGroup privateFilter = getFinalFilterEffect(privateFilterName, privateIntensity, videoModel);
                    privateCache.put(privateFilterName, Integer.valueOf(i));
                    privateArray.put(Integer.valueOf(i), privateFilter);
                }
            } else {
                continue;
            }
        }
        finalFilters.put(publicArray, privateArray);
        privateCache.clear();
        EGL10Helper.clearGLError("after finalFilter");
        return finalFilters;
    }

    private synchronized GPUImageFilterGroup getFinalFilterEffect(String filterName, float mFilterIntensity) {
        return getFinalFilterEffect(filterName, mFilterIntensity, null);
    }

    private synchronized GPUImageFilterGroup getFinalFilterEffect(String filterName, float mFilterIntensity, LongVideosModel videoModel) {
        GPUImageFilterGroup filterGroup;
        int i;
        filterGroup = new GPUImageFilterGroup();
        GPUImageFilterGroup group = new GPUImageFilterGroup();
        GPUImageFilter currentFilter = getSavingSelectedFilter(filterName);
        if (this.filterInfo == null || !(this.filterInfo.filter_id == 7 || this.filterInfo.isBnW)) {
            currentFilter.setIsBnW(StaticLayoutUtil.DefaultSpacingadd);
        } else {
            currentFilter.setIsBnW(1.0f);
        }
        List<FilterEffectBean> mList = this.mFilterEffectManager.getShowFilterEffectList(this.currentEffectType);
        Collections.sort(mList, new FilterEffectCompare());
        int size = mList.size();
        for (i = 0; i < size; i++) {
            getFinalNonLUTFilters(filterGroup, (FilterEffectBean) mList.get(i));
        }
        this.addFinalTransformFilter = false;
        for (i = 0; i < size; i++) {
            getSaveFilterGroup(group, (FilterEffectBean) mList.get(i), currentFilter, mFilterIntensity);
        }
        this.addFinalWhiteBalanceFilter = false;
        FramebufferTexture saveLUTTexture = new FramebufferTexture(1024, 32, 6407);
        LUTCreator.initialize();
        LUTCreator lutCreator = new LUTCreator();
        lutCreator.update(group, saveLUTTexture);
        GLES20.glFinish();
        GPUImageLookupFilter2 saveLUTFilter = new GPUImageLookupFilter2();
        saveLUTFilter.setSecondaryTexture(saveLUTTexture.transferOwnership());
        saveLUTTexture.destroy();
        lutCreator.destroy();
        LogUtil.d(String.format("finalFilter's size : %s", new Object[]{Integer.valueOf(filterGroup.mFilters.size())}));
        filterGroup.addFilter(saveLUTFilter);
        LogUtil.d(String.format("finalFilter's size : %s", new Object[]{Integer.valueOf(filterGroup.mFilters.size())}));
        mList.clear();
        return filterGroup;
    }

    private String getFilterId() {
        FilterInfo filterInfo = (FilterInfo) App.mFilterMap.get(this.mFilterName);
        if (!TextUtil.isValidate(filterInfo)) {
            return "";
        }
        return this.mFilterEffectManager.getUploadFilterInfoString(this.currentEffectType, this.strengthValue, filterInfo.filter_id + SpannedUtil.IMG_REPLACE_STR + filterInfo.groupNameEn + SpannedUtil.IMG_REPLACE_STR + filterInfo.name_en);
    }

    @NonNull
    private GPUImageFilter getSavingSelectedFilter(String mFilterName) {
        return null;//FilterUtils.getFilterByLocalName(mFilterName);
    }

    @NonNull
    private GPUImageToneCurveFilter getBrightFilter() {
        return ProgramLoader.getBrightFilter();
    }

    @NonNull
    private GPUImageBilateralFilter0 getBilateralFilter() {
        return ProgramLoader.getBilateralFilter();
    }

    @NonNull
    private GPUImageGaussianSelectiveBlurFilter getGaussianFilter() {
        float ratio;
        int width = this.mVideoWidth;
        int height = this.mVideoHeight;
        if (this.mIsSquare) {
            height = Math.min(width, height);
            width = height;
        } else if ((((float) height) * 1.0f) / ((float) width) > 1.2f) {
        }
        GPUImageGaussianSelectiveBlurFilter gaussianSelectiveBlurFilter = new GPUImageGaussianSelectiveBlurFilter();
        float x = (((float) (this.mVideoHeight / 2)) + ((this.mOffsetY * ((float) height)) / 2.0f)) / ((float) this.mVideoHeight);
        float y = (((float) (this.mVideoWidth / 2)) + ((this.mOffsetX * ((float) width)) / 2.0f)) / ((float) this.mVideoWidth);
        if (this.mVideoHeight > this.mVideoWidth) {
            ratio = (((((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight)) * ((float) height)) / ((float) width);
            gaussianSelectiveBlurFilter.setExcludeCirclePoint(x, y);
        } else {
            ratio = (((((float) this.mVideoHeight) * 1.0f) / ((float) this.mVideoWidth)) * ((float) width)) / ((float) height);
            gaussianSelectiveBlurFilter.setExcludeCirclePoint(y, x);
        }
        gaussianSelectiveBlurFilter.setExcludeCircleRadius(0.5f * ratio);
        gaussianSelectiveBlurFilter.setExcludeBlurSize(0.3f * ratio);
        gaussianSelectiveBlurFilter.setAspectRatio(1.0f * ratio);
        LogUtil.d(TAG, "setAspectRatio()");
        return gaussianSelectiveBlurFilter;
    }

    @NonNull
    private GPUImageToneCurveFilter getToneCurveFilter() {
        return ProgramLoader.getLUTFilters();
    }

    @NonNull
    private GPUImageWhiteBalanceFilter getWhiteBalanceFilter() {
        return ProgramLoader.getWhiteBalanceFilter();
    }

    @NonNull
    private GPUImageSaturationFilter getSaturationFilter() {
        return ProgramLoader.getSaturationFilter();
    }

    @NonNull
    private AFGPUImageTransformFilter getTransformFilter() {
        return new AFGPUImageTransformFilter(0);
    }

    @NonNull
    private AFGPUImageSkyFilter2 getSkyFilter() {
        return new AFGPUImageSkyFilter2();
    }

    @NonNull
    public AFGPUImageOverlayBlendFilter getGrainFilter() {
        return ProgramLoader.getGrainFilter();
    }

    @NonNull
    private GPUImageToneCurveFilter getFadeFilter() {
        return ProgramLoader.getFadeFilter();
    }

    @NonNull
    public AFGPUImageMultiplyBlendFilter getMoviewFilter2() {
        return new AFGPUImageMultiplyBlendFilter();
    }

    @NonNull
    public GPUImagePrismFilter getPrismFilter() {
        return ProgramLoader.getPrismFilter();
    }

    public AFGPUImageScreenBlendFilter getScreenBlendFilter() {
        return ProgramLoader.getScreenBlendFilter();
    }

    public AFGPUImageDateBlendFilter getDateFilter() {
        return ProgramLoader.getDateFilter();
    }

    @NonNull
    private GPUImageAlphaBlendFilter getAlphaBlendFilterForText(Bitmap bitmap, float alpha) {
        boolean z = true;
        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter(alpha);
        if (this.mDataFrom == 0) {
            Rotation fromInt;
            if (this.mIsBackCamera) {
                int i;
                int i2 = (this.orientation + 360) - 90;
                if (this.mVideoHeight > this.mVideoWidth) {
                    i = 180;
                } else {
                    i = 0;
                }
                fromInt = Rotation.fromInt((i + i2) % 360);
            } else {
                fromInt = Rotation.fromInt((this.orientation + BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) % 360);
            }
            if (this.mIsBackCamera) {
                z = false;
            }
            alphaBlendFilter.setRotation(fromInt, z, false);
        } else {
            alphaBlendFilter.setRotation(Rotation.fromInt(this.mVideoRotation), false, true);
        }
        alphaBlendFilter.setBitmap(bitmap);
        return alphaBlendFilter;
    }

    private void saveMovieInfo(String timeStamp) {
        FileUtil.writeInfoToFile(MovieFileUtil.getMovieJsonFilePath(timeStamp), JsonParserUtil.serializeToJson(this.mMovieBean));
    }

    public void saving(boolean enable) {
        if (this.mIvBack != null) {
            this.mIvBack.setEnabled(enable);
            this.mSurfaceView.setEnabled(enable);
        }
    }

    private void stopProxyVideo() {
        if (this.mVideoAudioManager != null) {
            this.mVideoAudioManager.stopAllProxyVideo();
        }
    }

    public void onBackPressed() {
        if (this.hasSavedDraft && !this.isEventFinish) {
            SharedPrefUtil.setAppInfoBoolean(Constants.SHOW_DRAFTS_POP, true);
        }
        EventBus.getDefault().post(new FilterActivityBackEvent());
        this.isBackOrNextPressed = true;
        if (!NONE_FILTER.equals(this.currentFilterName)) {
            this.mFilterName = this.currentFilterName;
        }
        if (this.hasRecordVideo) {
            EventBus.getDefault().post(new RefreshDisplayAlbumEvent());
        }
        saveCollectedFilters(true);
//        BuglyLogUtil.writeBuglyLog(TAG);
        finish();
        stopProxyVideo();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        new Thread() {
            public void run() {
                if (va != null && hadStartStablize) {
                    va.stop();
                    va = null;
                }
            }
        }.start();
    }

    private void recycleBitmap() {
        if (this.mFilterInfos != null && this.mFilterInfos.size() > 0) {
            Iterator it = this.mFilterInfos.iterator();
            while (it.hasNext()) {
                ((FilterInfo) it.next()).bitmap = null;
            }
        }
        if (this.filter_loop_preview_ll != null) {
            int childCount = this.filter_loop_preview_ll.getChildCount();
            if (childCount != 0) {
                for (int i = 0; i < childCount; i++) {
                    BitmapUtils.release((ImageView) this.filter_loop_preview_ll.getChildAt(i));
                }
            }
        }
        BitmapUtils.recycleBitmap(this.mBitmapGrain);
        BitmapUtils.recycleBitmap(this.mBitmapLeak1);
        BitmapUtils.recycleBitmap(this.mBitmapLeak2);
        BitmapUtils.recycleBitmap(this.mBitmapLeak3);
        BitmapUtils.recycleBitmap(this.mBitmapLeak4);
        BitmapUtils.recycleBitmap(this.mBitmapDust1);
        BitmapUtils.recycleBitmap(this.mBitmapDust2);
        BitmapUtils.recycleBitmap(this.mBitmapDust3);
        BitmapUtils.recycleBitmap(this.mBitmapDust4);
        BitmapUtils.recycleBitmap(this.mBitmapDust5);
        BitmapUtils.recycleBitmap(this.mDateFontBitmap);
        BitmapUtils.recycleBitmap(this.mBitmapCamera);
        BitmapUtils.recycleBitmap(this.forFilterBitmap);
    }

    public void onEventMainThread(RecyclePicModelEvent event) {
        if (this.mVideoAudioManager != null) {
            this.mVideoAudioManager.recyclePicBitmap();
        }
    }

    public void onEventMainThread(FinishActivityMessageEvent event) {
        if (event.getActivityPath().equals(FinishActivityMessageEvent.PublishVideoPath) || event.getActivityPath().equals(FinishActivityMessageEvent.AvatarChangePath) || event.getActivityPath().equals(FinishActivityMessageEvent.AvatarMeChangePath) || event.getActivityPath().equals(FinishActivityMessageEvent.SaveVideoDraftPath)) {
            this.mSurfaceView.destroyCreator();
            this.isEventFinish = true;
            clearFrameBuffer();
            onBackPressed();
        }
    }

    private Activity getActivity() {
        return this;
    }

    public void slideUnloop(int start, int end, boolean pauseAtEnd, boolean isSliding) {
        if (this.mVideoFrames != null) {
            LogUtil.d("transformNeedCount", "unLoop : " + end);
            this.transformNeedCount = end + 1;
            this.mVideoFrames.slideUnloop(start, end);
            this.mSurfaceView.pauseAtZero(pauseAtEnd, isSliding);
        }
    }

    public void slideLoop(int level, boolean pause) {
        boolean shouldReturn = true;
        if (!(this.mVideoFrames == null || 3 == this.mDataFrom || this.mVideoFrames.size() < 6 || 1 == this.mDataType)) {
            shouldReturn = false;
        }
        if (!shouldReturn) {
            LogUtil.d("LoopListener", "slideLevel: " + level);
            if (level == -2) {
                LogUtil.d("transformNeedCount", "unloopStart : " + this.unloopStart + " unloopEnd : " + this.unloopEnd);
                this.transformNeedCount = this.unloopEnd + 1;
                this.mVideoFrames.slideUnloop(this.unloopStart, this.unloopEnd);
            } else if (level == -1) {
                LogUtil.d("transformNeedCount", "Loop : " + this.unloopEnd);
                this.transformNeedCount = this.unloopEnd + 1;
                this.mVideoFrames.slideUnloop(this.unloopStart, this.unloopEnd);
            }
        }
    }

    public void slideLoop(int start, int end) {
        this.transformNeedCount = end + 1;
        this.mVideoFrames.loop(start, end);
    }

    private void refreshTransform() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (!mSurfaceView.isStabilizerEnabled()) {
                    filter_stablize_rl.setAlpha(0.4f);
                    filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_off);
                } else if (transformsStraight == null || transformsStraight.length < transformNeedCount) {
                    filter_stablize_rl.setAlpha(0.4f);
                    filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_on);
                    filter_stablize_iv.setVisibility(View.GONE);
                    filter_stablize_cpb.setVisibility(View.VISIBLE);
                } else {
                    filter_stablize_rl.setAlpha(1.0f);
                    filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_on);
                    filter_stablize_iv.setVisibility(View.VISIBLE);
                    filter_stablize_cpb.setVisibility(View.GONE);
                }
            }
        });
        new Thread() {
            public synchronized void run() {
                if (mVideoFrames == null) {
                    LogUtil.d(FilterActivity.TAG, "refreshTransform : slidelevel=" + slideLevel);
                    if (lastSlideLevel == slideLevel) {
                        LogUtil.d(FilterActivity.TAG, "refreshTransform : return");
                    } else if (mVideoFrames != null) {
                        int i;
                        synchronized (mVideoFrames) {
                            int[] frames = new int[10];
                            int flag = 0;
                            for (int i2 = slideLevel; i2 < slideLevel + 10; i2++) {
                                if (flag <= 5) {
                                    frames[Math.max(i2 - slideLevel, 0)] = i2;
                                } else {
                                    frames[Math.max(i2 - slideLevel, 0)] = (slideLevel + 10) - flag;
                                }
                                flag++;
                            }
                            transformsLooped = null;
                            transformsLooped = getTransforms(frames);
                        }
                        LogUtil.d(FilterActivity.TAG, "refreshTransform : refreshed transformsLooped=" + transformsLooped);
                        if (transformsLooped != null) {
                            VidStabilizer.Transform[] access$20200;
                            FilterView filterView = mSurfaceView;
                            if (mVideoFrames.mIsLooped) {
                                access$20200 = transformsLooped;
                            } else {
                                access$20200 = transformsStraight;
                            }
                            filterView.setTransforms(access$20200);
                        }
                        FilterActivity filterActivity = FilterActivity.this;
                        if (transformsLooped == null) {
                            i = -2;
                        } else {
                            i = slideLevel;
                        }
                        filterActivity.lastSlideLevel = i;
                        if (transformsLooped != null && mSurfaceView.isStabilizerEnabled()) {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    filter_stablize_iv.setVisibility(View.VISIBLE);
                                    filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_on);
                                    filter_stablize_cpb.setVisibility(View.GONE);
                                    filter_stablize_rl.setAlpha(1.0f);
                                }
                            });
                        }
                    }
                } else {
                    if (transformsStraight == null || transformsStraight.length < mFrameCount) {
                        mSurfaceView.setTransforms(null);
                    } else {
                        mSurfaceView.setTransforms(transformsStraight);
                    }
                    setNormalFilter();
                }
            }
        }.start();
    }

    private void _VidAnalysisEvent(VidAnalysisEvent event) {
        float progress = ((((float) event.index) * 1.0f) / ((float) this.mFrameCount)) * 100.0f;
        this.filter_stablize_cpb.setProgress(progress);
        if (event.index >= this.transformNeedCount) {
            if (!this.mSurfaceView.isStabilizerEnabled() || event.index < this.transformNeedCount) {
                LogUtil.d(TAG, "refreshTransform  2" + progress);
                if (this.mSurfaceView.isStabilizerEnabled()) {
                    LogUtil.d(TAG, "refreshTransform  3" + progress);
                    this.filter_stablize_iv.setVisibility(View.GONE);
                    this.filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_on);
                    this.filter_stablize_cpb.setVisibility(View.VISIBLE);
                    this.filter_stablize_rl.setAlpha(0.4f);
                    return;
                }
                LogUtil.d(TAG, "refreshTransform  4" + progress);
                this.filter_stablize_iv.setVisibility(View.VISIBLE);
                this.filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_off);
                this.filter_stablize_cpb.setVisibility(View.GONE);
                this.filter_stablize_rl.setAlpha(0.4f);
                return;
            }
            LogUtil.d(TAG, "refreshTransform  1");
            this.filter_stablize_iv.setVisibility(View.VISIBLE);
            this.filter_stablize_iv.setImageResource(R.drawable.icon_25_stable_on);
            this.filter_stablize_cpb.setVisibility(View.GONE);
            this.filter_stablize_rl.setAlpha(1.0f);
            refreshTransform();
        }
    }

    public void onEventMainThread(VidAnalysisEvent event) {
        _VidAnalysisEvent(event);
    }

    public void onEventMainThread(EnterCollectedModeEvent event) {
        if (this.filter_tab_like_filter_ll.getVisibility() != View.VISIBLE) {
            alphaEnterAndExit(this.filter_tab_like_filter_ll, this.filter_tab_ll);
        }
    }

    public void onEventMainThread(CollectFilterEvent event) {
        if (event.position == 0) {
            //IntentUtil.toOrderFilterActivity(this, this.collectFilter);
            Toast.makeText(getActivity(),"go to OrderFilterActivity",Toast.LENGTH_LONG).show();
        } else {
            addOrRemoveCollectFilterInfo(event.filterInfo, event.position);
        }
    }

    private void addOrRemoveCollectFilterInfo(FilterInfo currentFilterInfo, int position) {
        int i;
        FilterInfo filterInfo;
        boolean isRemove;
        boolean hasCollect = false;
        boolean isNeedAnimator = false;
        int notifyCount = 0;
        boolean isFirstCollect = false;
        int insertOrRemovePos = 0;
        int firstPosVisible = this.mFilterListLayoutManager.findFirstVisibleItemPosition();
        int lastPosVisible = this.mFilterListLayoutManager.findLastVisibleItemPosition();
        if (currentFilterInfo != null && currentFilterInfo.isCollected) {
            for (i = firstPosVisible; i <= lastPosVisible; i++) {
                filterInfo = this.filterAdapter.getInfos().get(i);
                boolean abc = (filterInfo == null || filterInfo.name_en == null) ? false : true;
                if (abc && filterInfo.isCollected && FilterAdapter.GROUP_COLLECT.equals(filterInfo.group) && filterInfo.name_en.equals(currentFilterInfo.name_en)) {
                    isNeedAnimator = true;
                    break;
                }
            }
        } else {
            for (i = firstPosVisible; i <= lastPosVisible; i++) {
                filterInfo = this.filterAdapter.getInfos().get(i);
                if (filterInfo.isCollected && FilterAdapter.GROUP_COLLECT.equals(filterInfo.group)) {
                    isNeedAnimator = true;
                    break;
                }
            }
        }
        int firstPos = firstPosVisible + 3;
        final int left = this.mFilterListLayoutManager.findViewByPosition(firstPos).getLeft();
        FilterInfo filterInfo1;
        if (currentFilterInfo == null || currentFilterInfo.isCollected) {
            i = 0;
            while (i < this.collectFilter.size()) {
                boolean nameChsNotNull = this.collectFilter.get(i).name_chs != null;
                if (currentFilterInfo != null && nameChsNotNull && this.collectFilter.get(i).name_chs.equalsIgnoreCase(currentFilterInfo.name_chs)) {
                    hasCollect = true;
                    break;
                }
                i++;
            }
            if (hasCollect && this.mFilterInfos != null) {
                isRemove = true;
                firstPos--;
                if (this.collectFilter.size() == 1) {
                    insertOrRemovePos = this.filterAdapter.getNoneFilterIndex() + 1;
                    this.collectFilter.clear();
                    firstPos--;
                    notifyCount = 2;
                    this.filterAdapter.getInfos().remove(this.filterAdapter.getNoneFilterIndex() + 1);
                    filterInfo = this.filterAdapter.getInfos().remove(this.filterAdapter.getNoneFilterIndex() + 1);
                    for (i = 0; i < this.filterAdapter.getInfos().size(); i++) {
                        filterInfo1 = this.filterAdapter.getInfos().get(i);
                        if (filterInfo1.name_en != null && filterInfo1.name_en.equals(filterInfo.name_en)) {
                            filterInfo1.isCollected = false;
                            FilterAdapter.GROUP_COLLECT_POSITION = -1;
                            break;
                        }
                    }
                    this.filterAdapter.addOrReducePosition(-2);
                } else {
                    notifyCount = 1;
                    position--;
                    for (i = this.filterAdapter.getNoneFilterIndex() + 2; i < this.filterAdapter.getInfos().size(); i++) {
                        filterInfo1 = this.filterAdapter.getInfos().get(i);
                        if (filterInfo1.name_en != null && filterInfo1.name_en.equals(currentFilterInfo.name_en)) {
                            insertOrRemovePos = i;
                            break;
                        }
                    }
                    upDateFilters();
                    for (i = 0; i < this.collectFilter.size(); i++) {
                        filterInfo = this.collectFilter.get(i);
                        if ((filterInfo.name_en != null) && filterInfo.name_en.equals(currentFilterInfo.name_en)) {
                            this.collectFilter.remove(filterInfo);
                            break;
                        }
                    }
                    for (i = 0; i < this.filterAdapter.getInfos().size(); i++) {
                        filterInfo1 = this.filterAdapter.getInfos().get(i);
                        if ((filterInfo1.name_en != null) && filterInfo1.name_en.equals(currentFilterInfo.name_en)) {
                            filterInfo1.isCollected = false;
                            break;
                        }
                    }
                    this.filterAdapter.addOrReducePosition(-1);
                    this.filterAdapter.getInfos().addAll(this.filterAdapter.getNoneFilterIndex() + 2, this.collectFilter);
                }
            } else {
                return;
            }
        }
        i = 0;
        while (i < this.collectFilter.size()) {
            if ((this.collectFilter.get(i).name_en != null) && this.collectFilter.get(i).name_en.equalsIgnoreCase(currentFilterInfo.name_en)) {
                hasCollect = true;
                break;
            }
            i++;
        }
        if (!hasCollect && this.mFilterInfos != null) {
            isRemove = false;
            if (!getResources().getString(R.string.BUTTON_FAV_PRESETS).equals(((FilterInfo) this.filterAdapter.getInfos().get(this.filterAdapter.getNoneFilterIndex() + 1)).name_en)) {
                filterInfo = new FilterInfo(FilterAdapter.GROUP_COLLECT);
                if (firstPosVisible <= 1) {
                    isNeedAnimator = true;
                }
                isFirstCollect = true;
                filterInfo.name_chs = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo.name_cht = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                notifyCount = 0 + 1;
                filterInfo.name_en = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                this.filterAdapter.getInfos().add(this.filterAdapter.getNoneFilterIndex() + 1, filterInfo);
                firstPos++;
                this.filterAdapter.addOrReducePosition(1);
                FilterAdapter.GROUP_COLLECT_POSITION = 2;
            }
            notifyCount++;
            firstPos++;
            filterInfo = new FilterInfo(currentFilterInfo);
            filterInfo.originalGroupEn = currentFilterInfo.groupNameEn;
            filterInfo.originalGroupChs = currentFilterInfo.groupNameChs;
            filterInfo.originalGroupCht = currentFilterInfo.groupNameCht;
            filterInfo.group = FilterAdapter.GROUP_COLLECT;
            filterInfo.isGroup = false;
            filterInfo.selected = false;
            filterInfo.isCollected = true;
            filterInfo.groupNameChs = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            filterInfo.groupNameCht = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            filterInfo.groupNameEn = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            for (i = 0; i < this.filterAdapter.getInfos().size(); i++) {
                filterInfo1 = this.filterAdapter.getInfos().get(i);
                if (filterInfo1.name_en != null && filterInfo1.name_en.equals(filterInfo.name_en)) {
                    filterInfo1.isCollected = true;
                    break;
                }
            }
            for (i = this.filterAdapter.getNoneFilterIndex() + 2; i < this.filterAdapter.getInfos().size(); i++) {
                if (!this.filterAdapter.getInfos().get(i).isCollected) {
                    insertOrRemovePos = i;
                    break;
                }
            }
            this.collectFilter.add(filterInfo);
            this.filterAdapter.addOrReducePosition(1);
            upDateFilters();
            this.filterAdapter.getInfos().addAll(this.filterAdapter.getNoneFilterIndex() + 2, this.collectFilter);
        } else {
            return;
        }
        boolean hasConstains = filterGroupConstainsFavorite();
        int selectPosition;
        if (this.collectFilter == null || this.collectFilter.size() == 0) {
            if (hasConstains) {
                this.mFilterGroupNames.remove(1);
                selectPosition = this.filterGroupNameAdapter.getSelectPosition();
                if (selectPosition != 0) {
                    this.filterGroupNameAdapter.setSelectPosition(selectPosition - 1);
                }
                this.filterGroupNameAdapter.notifyDataSetChanged();
            }
        } else if (!hasConstains) {
            filterInfo = new FilterInfo(FilterAdapter.GROUP_COLLECT);
            filterInfo.name_chs = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            filterInfo.name_cht = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            filterInfo.filter_id = -10001;
            filterInfo.name_en = getResources().getString(R.string.BUTTON_FAV_PRESETS);
            filterInfo.filename = FilterAdapter.GROUP_COLLECT;
            this.mFilterGroupNames.add(1, filterInfo);
            selectPosition = this.filterGroupNameAdapter.getSelectPosition();
            if (selectPosition != 0) {
                this.filterGroupNameAdapter.setSelectPosition(selectPosition + 1);
            }
            this.filterGroupNameAdapter.notifyDataSetChanged();
        }
        if (!isNeedAnimator) {
            this.filter_filter_root_rl.destroyDrawingCache();
            this.filter_filter_root_rl.setDrawingCacheEnabled(true);
            this.filter_filter_root_rl.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            this.iv_recyclerview_cache.setImageBitmap(this.filter_filter_root_rl.getDrawingCache(false));
            this.iv_recyclerview_cache.setVisibility(View.VISIBLE);
            this.filter_filter_root_rl.setVisibility(View.INVISIBLE);
            this.filterAdapter.notifyDataSetChanged();
            int left2 = this.mFilterListLayoutManager.findViewByPosition(firstPos).getLeft();
            //this.iv_recyclerview_cache.post(44.lambdaFactory$(this, firstPos, left));
            final int finalFirstPos = firstPos;
            iv_recyclerview_cache.post(new Runnable() {
                @Override
                public void run() {
                    lambda$addOrRemoveCollectFilterInfo$43(finalFirstPos,left);
                }
            });
        } else if (isRemove) {
            this.filterAdapter.notifyItemRangeRemoved(insertOrRemovePos, notifyCount);
            this.filterAdapter.notifyItemRangeChanged(insertOrRemovePos, this.filterAdapter.getInfos().size());
        } else if (isFirstCollect) {
            this.filterAdapter.notifyItemRangeInserted(this.filterAdapter.getNoneFilterIndex() + 1, notifyCount);
            this.filterAdapter.notifyItemRangeChanged(this.filterAdapter.getNoneFilterIndex() + 1, this.filterAdapter.getInfos().size());
        } else {
            this.filterAdapter.notifyItemRangeInserted(insertOrRemovePos, notifyCount);
            this.filterAdapter.notifyItemRangeChanged(insertOrRemovePos, this.filterAdapter.getInfos().size());
        }
    }

    private /* synthetic */ void lambda$addOrRemoveCollectFilterInfo$43(int pos, int left) {
        this.filter_list_recyclerview.scrollBy(this.mFilterListLayoutManager.findViewByPosition(pos).getLeft() - left, 0);
        this.filter_filter_root_rl.setVisibility(View.VISIBLE);
        this.iv_recyclerview_cache.setVisibility(View.INVISIBLE);
        this.iv_recyclerview_cache.setImageBitmap(null);
    }

    private boolean filterGroupConstainsFavorite() {
        String pavoriteString = getString(R.string.BUTTON_FAV_PRESETS);
        int size = this.mFilterGroupNames.size();
        for (int i = 0; i < size; i++) {
            boolean nameEnNotNull;
            boolean nameChsNotNull;
            FilterInfo info = this.mFilterGroupNames.get(i);
            if (info.name_en != null) {
                nameEnNotNull = true;
            } else {
                nameEnNotNull = false;
            }
            if (info.name_chs != null) {
                nameChsNotNull = true;
            } else {
                nameChsNotNull = false;
            }
            if (nameEnNotNull && info.name_en.equalsIgnoreCase(pavoriteString)) {
                return true;
            }
            if (nameChsNotNull && info.name_chs.equalsIgnoreCase(pavoriteString)) {
                return true;
            }
        }
        return false;
    }

    private void upDateFilters() {
        int i = 0;
        while (i < this.filterAdapter.getInfos().size()) {
            FilterInfo filterInfo1 = this.filterAdapter.getInfos().get(i);
            if (!filterInfo1.isGroup && FilterAdapter.GROUP_COLLECT.equals(filterInfo1.group)) {
                this.mFilterInfos.remove(i);
                i--;
            }
            i++;
        }
    }

    private void saveCollectedFilters(final boolean isExit) {
        //new Thread(45.lambdaFactory$(this, isExit)).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lambda$saveCollectedFilters$44(isExit);
            }
        });
    }

    private /* synthetic */ void lambda$saveCollectedFilters$44(boolean isExit) {
        if (this.collectFilter.size() == 0) {
            SharedPrefUtils.putString(Constants.COLLECT_FILTER + GlobalHelper.getUserId(), "");
            return;
        }
        String s = "";
        int i;
        if (isExit) {
            for (i = 0; i < this.collectFilter.size(); i++) {
                ((FilterInfo) this.collectFilter.get(i)).bitmap = null;
                ((FilterInfo) this.collectFilter.get(i)).filter = null;
            }
            s = JsonParserUtil.fromList(this.collectFilter, null);
        } else {
            ArrayList<FilterInfo> filterInfos = new ArrayList();
            for (i = 0; i < this.collectFilter.size(); i++) {
                FilterInfo filterInfo = (FilterInfo) this.collectFilter.get(i);
                FilterInfo filterInfo1 = new FilterInfo(filterInfo);
                filterInfo1.bitmap = null;
                filterInfo1.filter = null;
                filterInfo1.isCollected = true;
                filterInfo1.group = FilterAdapter.GROUP_COLLECT;
                filterInfo1.isGroup = false;
                filterInfo1.selected = false;
                filterInfo1.filter_id = filterInfo.filter_id;
                filterInfo1.originalGroupEn = filterInfo.originalGroupEn;
                filterInfo1.originalGroupCht = filterInfo.originalGroupCht;
                filterInfo1.originalGroupChs = filterInfo.originalGroupChs;
                filterInfo1.groupNameChs = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo1.groupNameCht = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfo1.groupNameEn = getResources().getString(R.string.BUTTON_FAV_PRESETS);
                filterInfos.add(filterInfo1);
            }
            s = JsonParserUtil.fromList(filterInfos, null);
        }
        SharedPrefUtils.putString(Constants.COLLECT_FILTER + GlobalHelper.getUserId(), s);
    }

    public void onEventMainThread(FiltersDownloadEvent event) {
        boolean state1;
        boolean state2 = true;
        int filters_id = event.getFilters_id();
        int downloadState = FilterDownloadModel.getInstance().getDownloadState(filters_id);
        if (downloadState == 1) {
            state1 = true;
        } else {
            state1 = false;
        }
        if (downloadState != 2) {
            state2 = false;
        }
        if (!state1 && !state2) {
            if (FilterDownloadModel.getInstance().isHasDownloadingFilters()) {
                addLoadingFilterItem();
            } else {
                removeLoadingFilterItem();
            }
            if (downloadState == 3) {
                int startPos;
                FilterInfo groupFilterInfo = event.getFilterInfo();
                List<FilterInfo> filterInfos = event.getFilterInfos();
                int insertPos = getInsertPos();
                if (insertPos == 0) {
                    startPos = this.mFilterInfos.size();
                    this.mFilterInfos.add(groupFilterInfo);
                    this.mFilterInfos.addAll(filterInfos);
                } else {
                    startPos = this.mFilterInfos.size() - insertPos;
                    this.mFilterInfos.add(this.mFilterInfos.size() - insertPos, groupFilterInfo);
                    this.mFilterInfos.addAll(this.mFilterInfos.size() - insertPos, filterInfos);
                }
                this.filterAdapter.notifyDataSetChanged();
                refreshFiltersBitmap(startPos, this.mFilterInfos.size());
                this.mFilterGroupNames.add(groupFilterInfo);
                onFilterGroupNameClick(this.mFilterGroupNames.size() - 1);
            }
        } else if (!localHasThisFilterGroup(filters_id)) {
            boolean isDownloading = false;
            boolean lastItemDownloading = this.mFilterInfos.get(this.mFilterInfos.size() - 1).isDownloading;
            boolean preLastDownloading = this.mFilterInfos.get(this.mFilterInfos.size() - 2).isDownloading;
            if (lastItemDownloading || preLastDownloading) {
                isDownloading = true;
            }
            if (!isDownloading) {
                addLoadingFilterItem();
            }
        }
    }

    private int getInsertPos() {
        if (this.mFilterInfos.get(this.mFilterInfos.size() - 1).isDownloading) {
            return 1;
        }
        return 0;
    }

    private void refreshFiltersBitmap(final int startPos, final int end) {
        new Thread() {
            public void run() {
                //EGL10Helper.withContext("wc", FilterActivity$66$.Lambda.1.lambdaFactory$(this, startPos, end));
                EGL10Helper.withContext("wc", new EGLRunnableVoid() {
                    @Override
                    public void run(EGL10Helper eGL10Helper) {
                        lambda$run$0(startPos,end,eGL10Helper);
                    }
                });
            }

            private /* synthetic */ void lambda$run$0(int startPos, int end, EGL10Helper egl) {
                forFilterBitmap = FileUtil.getFilterPreview();
                EGLSurface surface = null;
                if (!(forFilterBitmap == null || forFilterBitmap.isRecycled())) {
                    surface = egl.createPBuffer(forFilterBitmap.getWidth(), forFilterBitmap.getHeight());
                    egl.makeCurrent(surface);
                }
                generatePreviewBitmaps(forFilterBitmap, startPos, end, 0);
                BitmapUtils.recycleBitmap(forFilterBitmap);
                if (surface != null) {
                    egl.destroySurface(surface);
                }
            }
        }.start();
    }

    private void removeLoadingFilterItem() {
        FilterInfo filterInfo = this.mFilterInfos.get(this.mFilterInfos.size() - 1);
        if (filterInfo.isDownloading) {
            this.mFilterInfos.remove(filterInfo);
        }
        this.filterAdapter.notifyDataSetChanged();
    }

    public void addLoadingFilterItem() {
        if (!this.mFilterInfos.get(this.mFilterInfos.size() - 1).isDownloading) {
            FilterInfo filterInfo = new FilterInfo("");
            filterInfo.isDownloading = true;
            this.mFilterInfos.add(filterInfo);
            this.filterAdapter.notifyDataSetChanged();
        }
    }

    private boolean localHasThisFilterGroup(int filter_id) {
        if (App.jsonGroupInfoList2 != null) {
            for (int i = 0; i < App.jsonGroupInfoList2.size(); i++) {
                if (((FilterInfo) App.jsonGroupInfoList2.get(i)).filter_id == filter_id) {
                    return true;
                }
            }
        }
        return false;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshFilterSortEvent event) {
        if (event.isNeedRefresh) {
            List<FilterInfo> orderedCollectedList = event.orderedCollectedList;
            List<FilterInfo> orderedDownloadlist = event.orderedDownloadlist;
            if (orderedCollectedList.size() > 0) {
                upDateFilters();
                this.collectFilter.clear();
                this.collectFilter.addAll(orderedCollectedList);
                saveCollectedFilters(false);
                this.mFilterInfos.addAll(this.filterAdapter.getNoneFilterIndex() + 2, this.collectFilter);
            }
            int insertPos = 0;
            if (orderedDownloadlist.size() > 0) {
                int selectPosition = this.filterGroupNameAdapter.getSelectPosition();
                FilterInfo selectInfo = (FilterInfo) this.mFilterGroupNames.get(selectPosition);
                FilterInfo info = (FilterInfo) this.mFilterGroupNames.get(0);
                this.mFilterGroupNames.clear();
                this.mFilterGroupNames.add(info);
                this.mFilterGroupNames.addAll(orderedDownloadlist);
                ArrayList<FilterInfo> filterInfos = new ArrayList();
                for (int i = 0; i < orderedDownloadlist.size(); i++) {
                    FilterInfo filterInfo = (FilterInfo) orderedDownloadlist.get(i);
                    if (selectPosition != 0 && selectInfo.groupNameEn.equals(filterInfo.groupNameEn)) {
                        this.filterGroupNameAdapter.setSelectPosition(i + 1);
                    }
                    int j = 0;
                    while (j < App.mFilterInfos.size()) {
                        if (filterInfo.groupNameEn != null && filterInfo.groupNameEn.equals(((FilterInfo) App.mFilterInfos.get(j)).groupNameEn)) {
                            filterInfos.add(App.mFilterInfos.get(j));
                        }
                        j++;
                    }
                }
                this.filterGroupNameAdapter.notifyDataSetChanged();
                final int newSelect = this.filterGroupNameAdapter.getSelectPosition();
                this.filter_group_name_rv.scrollToPosition(newSelect);
                this.filter_group_name_rv.post(new Runnable() {
                    public void run() {
                        android.view.View viewByPosition = mFilterGroupNameManager.findViewByPosition(newSelect);
                        if (viewByPosition != null) {
                            filter_group_name_rv.smoothScrollBy(viewByPosition.getLeft() - ((int) (((float) (metricsWidth - viewByPosition.getWidth())) / 2.0f)), 0);
                        }
                    }
                });
                this.mFilterInfos.removeAll(App.mFilterInfos);
                App.mFilterInfos.clear();
                App.mFilterInfos.addAll(filterInfos);
                if (((FilterInfo) this.mFilterInfos.get(this.mFilterInfos.size() - 1)).isDownloading) {
                    insertPos = 1;
                }
                if (insertPos == 0) {
                    this.mFilterInfos.addAll(filterInfos);
                } else {
                    this.mFilterInfos.addAll(this.mFilterInfos.size() - insertPos, filterInfos);
                }
            }
            this.filterAdapter.notifyDataSetChanged();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteDownloadFilterEvent event) {
        FilterInfo removGroup = event.removGroup;
        if (App.mFilterInfos != null) {
            int i;
            FilterInfo filterInfo;
            ArrayList<FilterInfo> removeFilter = new ArrayList();
            for (i = 0; i < App.mFilterInfos.size(); i++) {
                filterInfo = (FilterInfo) App.mFilterInfos.get(i);
                if (filterInfo.groupNameEn != null && filterInfo.groupNameEn.equals(removGroup.groupNameEn)) {
                    removeFilter.add(filterInfo);
                }
            }
            App.mFilterInfos.removeAll(removeFilter);
            this.mFilterInfos.removeAll(removeFilter);
            for (i = 0; i < removeFilter.size(); i++) {
                filterInfo = (FilterInfo) removeFilter.get(i);
                if (this.mFilterName.equals(filterInfo.name_en)) {
                    this.mFilterName = NONE_FILTER;
                    this.filterAdapter.setFilterName(this.mFilterName, filterInfo);
                    if (this.mFilterModel != null) {
                        this.mFilterModel.setmFinalEffectState(this.mFinalEffectState);
                        this.mFilterModel.setFilterName(this.mFilterName);
                        setNormalFilter();
                        setUndoData(0);
                    }
                }
            }
            for (i = 0; i < removeFilter.size(); i++) {
                filterInfo = (FilterInfo) removeFilter.get(i);
                for (int j = 0; j < this.collectFilter.size(); j++) {
                    FilterInfo filterInfo1 = (FilterInfo) this.collectFilter.get(j);
                    if (filterInfo.name_en.equals(filterInfo1.name_en)) {
                        this.collectFilter.remove(filterInfo1);
                        break;
                    }
                }
            }
            upDateFilters();
            if (this.collectFilter.size() != 0) {
                this.mFilterInfos.addAll(this.filterAdapter.getNoneFilterIndex() + 2, this.collectFilter);
            } else if (this.mFilterInfos.size() > this.filterAdapter.getNoneFilterIndex() + 1) {
                this.mFilterInfos.remove(this.filterAdapter.getNoneFilterIndex() + 1);
            }
            saveCollectedFilters(false);
        }
        addOrRemoveSortItem();
        this.filterAdapter.notifyDataSetChanged();
    }

    private void addOrRemoveSortItem() {
//        if (App.isLogin()) {
            if (App.mFilterInfos.size() == 0) {
                if (this.mFilterInfos.size() > 1 && ((FilterInfo) this.mFilterInfos.get(1)).type == 5) {
                    this.mFilterInfos.remove(1);
                }
            } else if (((FilterInfo) this.mFilterInfos.get(1)).type != 5) {
                FilterInfo filterInfo = new FilterInfo("");
                filterInfo.type = 5;
                this.mFilterInfos.add(1, filterInfo);
            }
            this.filterAdapter.notifyDataSetChanged();
//        } else if (this.mFilterInfos.size() > 1 && ((FilterInfo) this.mFilterInfos.get(1)).type == 5) {
//            this.mFilterInfos.remove(1);
//        }
    }

    private void changeTextModelsSize(boolean change) {
        if (TextUtil.isValidate(this.cloneTextModels)) {
            if (!change) {
                this.mVideoAudioManager.getTextModelList().clear();
                this.mVideoAudioManager.setTextModelList(this.cloneTextModels);
            }
            this.cloneTextModels.clear();
            this.cloneTextModels = null;
        }
    }

    private void setFilterViewAnim(FilterEffectManager.EffectType effectType, List<FilterEffectBean> list) {
        this.lastProgressValue = -1.0f;
        FilterEffectBean bean = this.mFilterEffectManager.getFilterEffectBeanByEffectType(list, effectType);
        if (this.currentEffectType == effectType) {
            LogUtil.d("setProAndInvalidate", "currentEffectType : " + this.currentEffectType);
            if (this.filter_custom_sk != null && this.filter_custom_sk.getVisibility() == View.VISIBLE) {
                this.filter_custom_sk.setProAndInvalidate((int) bean.value[1]);
            }
        }
        switch (effectType) {
            case HIGHLIGHT:
                setViewColor(this.filter_effect_shade_lighten, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_shade_lighten, bean);
                return;
            case SKY:
                setViewColor(this.filter_effect_sky, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sky, bean);
                return;
            case SHADOW:
                setViewColor(this.filter_effect_highlight_shadow, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_highlight_shadow, bean);
                return;
            case FADED:
                setViewColor(this.filter_effect_fade, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_fade, bean);
                return;
            case TILT:
                setViewColor(this.filter_effect_tilt, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tilt, bean);
                return;
            case SHARPEN:
                setViewColor(this.filter_effect_sharpen, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sharpen, bean);
                return;
            case GRAIN:
                setViewColor(this.filter_effect_grain, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_grain, bean);
                return;
            case TEMPERATURE:
                setViewColor(this.filter_effect_temperature, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_temperature, bean);
                return;
            case SATURATION:
                setViewColor(this.filter_effect_saturation, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_saturation, bean);
                return;
            case CONTRAST:
                setViewColor(this.filter_effect_contrast, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_contrast, bean);
                return;
            case EXPOSURE:
                setViewColor(this.filter_effect_exposure, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_exposure, bean);
                return;
            case BEAUTIFY:
                setViewColor(this.filter_effect_beauty, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_beauty, bean);
                return;
            case VIGNETTE:
                setViewColor(this.filter_effect_vignette, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_vignette, bean);
                return;
            case LEAK:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                setViewColor(this.filter_effect_leak, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_leak, bean);
                return;
            case TINGE:
                setViewColor(this.filter_effect_tinge, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tinge, bean);
                return;
            case DATE:
                if (this.mFilterEffectManager.setSelectEffect(bean)) {
                    this.filter_effect_date.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_date, true);
                    return;
                }
                this.currentEffectType = FilterEffectManager.EffectType.NONE;
                this.filter_effect_date.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                setViewColor(this.filter_effect_date, false);
                return;
            case DUST:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                setViewColor(this.filter_effect_dust, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_dust, bean);
                return;
            case PRISM:
                setViewColor(this.filter_effect_prism, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_prism, bean);
                return;
            case STRENGTH:
                alphaStrengthEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll, false);
                alphaEnterAndExit(this.filter_filter_root_rl, this.filter_rule_ll);
                this.mFilterEffectManager.setSelectEffect(bean);
                return;
            default:
                return;
        }
    }

    private void setFilterViewAnim(FilterEffectManager.EffectType effectType, boolean fromUndo) {
        this.lastProgressValue = -1.0f;
        FilterEffectBean bean = this.mFilterEffectManager.getCurrentListItem(effectType);
        if (this.currentEffectType == effectType) {
            LogUtil.d("setProAndInvalidate", "currentEffectType : " + this.currentEffectType);
            if (!(this.filter_custom_sk == null || this.filter_custom_sk.getVisibility() != View.VISIBLE || bean == null)) {
                this.filter_custom_sk.setProAndInvalidate((int) bean.value[1]);
            }
        }
        switch (effectType) {
            case HIGHLIGHT:
                setViewColor(this.filter_effect_shade_lighten, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_shade_lighten, bean);
                return;
            case SKY:
                setViewColor(this.filter_effect_sky, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sky, bean);
                return;
            case SHADOW:
                setViewColor(this.filter_effect_highlight_shadow, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_highlight_shadow, bean);
                return;
            case FADED:
                setViewColor(this.filter_effect_fade, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_fade, bean);
                return;
            case TILT:
                setViewColor(this.filter_effect_tilt, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tilt, bean);
                return;
            case SHARPEN:
                setViewColor(this.filter_effect_sharpen, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sharpen, bean);
                return;
            case GRAIN:
                setViewColor(this.filter_effect_grain, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_grain, bean);
                return;
            case TEMPERATURE:
                setViewColor(this.filter_effect_temperature, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_temperature, bean);
                return;
            case SATURATION:
                setViewColor(this.filter_effect_saturation, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_saturation, bean);
                return;
            case CONTRAST:
                setViewColor(this.filter_effect_contrast, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_contrast, bean);
                return;
            case EXPOSURE:
                setViewColor(this.filter_effect_exposure, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_exposure, bean);
                return;
            case BEAUTIFY:
                setViewColor(this.filter_effect_beauty, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_beauty, bean);
                return;
            case VIGNETTE:
                setViewColor(this.filter_effect_vignette, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_vignette, bean);
                return;
            case LEAK:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                setViewColor(this.filter_effect_leak, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_leak, bean);
                return;
            case TINGE:
                setViewColor(this.filter_effect_tinge, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tinge, bean);
                return;
            case DATE:
                if (this.mFilterEffectManager.setSelectEffect(bean)) {
                    this.filter_effect_date.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_date, true);
                    return;
                }
                this.currentEffectType = FilterEffectManager.EffectType.NONE;
                this.filter_effect_date.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                setViewColor(this.filter_effect_date, false);
                return;
            case DUST:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                setViewColor(this.filter_effect_dust, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_dust, bean);
                return;
            case PRISM:
                setViewColor(this.filter_effect_prism, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_prism, bean);
                return;
            case STRENGTH:
                if (!fromUndo) {
                    alphaStrengthEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll, false);
                    alphaEnterAndExit(this.filter_filter_root_rl, this.filter_rule_ll);
                }
                this.mFilterEffectManager.setSelectEffect(bean);
                return;
            default:
                return;
        }
    }

    private void onConfirmPress(FilterEffectManager.EffectType effectType) {
        FilterEffectBean bean;
        restoreSaveAndBack();
        this.lastProgressValue = -1.0f;
        if (this.filter_effect_date != null && this.filter_effect_date.getVisibility() == View.VISIBLE && effectType == FilterEffectManager.EffectType.DATE) {
            FilterEffectManager filterEffectManager = this.mFilterEffectManager;
            float[] fArr = new float[2];
            fArr[0] = StaticLayoutUtil.DefaultSpacingadd;
            fArr[1] = this.filter_effect_date.getProgressValue() == 10.0f ? StaticLayoutUtil.DefaultSpacingadd : 10.0f;
            bean = filterEffectManager.createBean(effectType, fArr, new float[]{StaticLayoutUtil.DefaultSpacingadd});
            this.mFilterEffectManager.setSelectEffect(bean);
        } else {
            bean = this.mFilterEffectManager.createBean(effectType, new float[]{this.filter_custom_sk.getMin(), this.filter_custom_sk.getProgressFloat(), this.filter_custom_sk.getMax()}, new float[]{StaticLayoutUtil.DefaultSpacingadd});
            this.mFilterEffectManager.setSelectEffect(bean);
        }
        if (!(effectType == FilterEffectManager.EffectType.STRENGTH || effectType == FilterEffectManager.EffectType.DATE)) {
            alphaEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll, new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    filter_tab_ll.setVisibility(View.VISIBLE);
                    filter_tab_choose_confirm.setEnabled(true);
                }
            });
            setFilterEffectGone();
        }
        float selectPosition;
        FilterEffectBean hBean;
        FilterEffectBean vBean;
        int i;
        int depth;
        switch (effectType) {
            case HIGHLIGHT:
                setViewColor(this.filter_effect_shade_lighten, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_shade_lighten, bean);
                return;
            case SKY:
                setViewColor(this.filter_effect_sky, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sky, bean);
                return;
            case SHADOW:
                setViewColor(this.filter_effect_highlight_shadow, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_highlight_shadow, bean);
                return;
            case FADED:
                setViewColor(this.filter_effect_fade, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_fade, bean);
                return;
            case TILT:
                setViewColor(this.filter_effect_tilt, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tilt, bean);
                return;
            case SHARPEN:
                setViewColor(this.filter_effect_sharpen, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_sharpen, bean);
                return;
            case GRAIN:
                setViewColor(this.filter_effect_grain, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_grain, bean);
                return;
            case TEMPERATURE:
                setViewColor(this.filter_effect_temperature, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_temperature, bean);
                return;
            case SATURATION:
                setViewColor(this.filter_effect_saturation, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_saturation, bean);
                return;
            case ROTATE:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_rotation_rotate90.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                this.filter_crop_ccav.setVisibility(View.GONE);
                bean.selectPosition = new float[]{(float) this.rotate_but};
                this.filter_crop_ccav.setVisibility(View.GONE);
                selectPosition = bean.selectPosition[0];
                hBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL);
                vBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL);
                float hValue = hBean.value[1];
                float vValue = vBean.value[1];
                if (selectPosition == 90.0f || selectPosition == 270.0f) {
                    setViewColor(this.filter_effect_vertical, hValue != StaticLayoutUtil.DefaultSpacingadd);
                    setViewColor(this.filter_effect_horizontal, vValue != StaticLayoutUtil.DefaultSpacingadd);
                } else {
                    setViewColor(this.filter_effect_vertical, vValue != StaticLayoutUtil.DefaultSpacingadd);
                    setViewColor(this.filter_effect_horizontal, hValue != StaticLayoutUtil.DefaultSpacingadd);
                }
                setViewColor(this.filter_effect_rotation, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_rotation, bean);
                return;
            case CROP:
                onCropConfirmPress(bean);
                return;
            case CONTRAST:
                setViewColor(this.filter_effect_contrast, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_contrast, bean);
                return;
            case MIRROR:
                this.mirror_seek_bar.setVisibility(View.GONE);
                bean.selectPosition = new float[]{(float) this.mirror_seek_bar.getProgress()};
                for (i = 0; i < this.mFilterEffectDetailSets.size(); i++) {
                    if (((FilterEffectSetRelativeLayout) this.mFilterEffectDetailSets.get(i)).getIsSelect()) {
                        bean.value = new float[]{(float) i};
                    }
                }
                setViewColor(this.filter_effect_mirror, this.mFilterEffectManager.setSelectEffect(bean));
                if (this.mFilterEffectManager.setSelectEffect(bean)) {
                    this.filter_effect_mirror.setProgressValue(10.0f);
                    return;
                } else {
                    this.filter_effect_mirror.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                    return;
                }
            case HORIZONTAL:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                selectPosition = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                if (selectPosition == 90.0f || selectPosition == 270.0f) {
                    vBean = this.mFilterEffectManager.createBean(FilterEffectManager.EffectType.VERTICAL, new float[]{this.filter_custom_sk.getMin(), this.filter_custom_sk.getProgressFloat(), this.filter_custom_sk.getMax()}, new float[]{StaticLayoutUtil.DefaultSpacingadd});
                    this.mFilterEffectManager.setSelectEffect(vBean);
                    hBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL);
                    setViewColor(this.filter_effect_vertical, hBean.value[1] != StaticLayoutUtil.DefaultSpacingadd);
                    setViewColor(this.filter_effect_horizontal, vBean.value[1] != StaticLayoutUtil.DefaultSpacingadd);
                    setFilterIconProgress(this.filter_effect_vertical, hBean);
                    setFilterIconProgress(this.filter_effect_horizontal, vBean);
                    return;
                }
                setViewColor(this.filter_effect_horizontal, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_horizontal, bean);
                return;
            case VERTICAL:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                selectPosition = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                if (selectPosition == 90.0f || selectPosition == 270.0f) {
                    hBean = this.mFilterEffectManager.createBean(FilterEffectManager.EffectType.HORIZONTAL, new float[]{this.filter_custom_sk.getMin(), this.filter_custom_sk.getProgressFloat(), this.filter_custom_sk.getMax()}, new float[]{StaticLayoutUtil.DefaultSpacingadd});
                    this.mFilterEffectManager.setSelectEffect(hBean);
                    vBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL);
                    setViewColor(this.filter_effect_vertical, hBean.value[1] != StaticLayoutUtil.DefaultSpacingadd);
                    setViewColor(this.filter_effect_horizontal, vBean.value[1] != StaticLayoutUtil.DefaultSpacingadd);
                    setFilterIconProgress(this.filter_effect_vertical, hBean);
                    setFilterIconProgress(this.filter_effect_horizontal, vBean);
                    return;
                }
                setViewColor(this.filter_effect_vertical, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_vertical, bean);
                return;
            case EXPOSURE:
                setViewColor(this.filter_effect_exposure, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_exposure, bean);
                return;
            case BEAUTIFY:
                setViewColor(this.filter_effect_beauty, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_beauty, bean);
                return;
            case VIGNETTE:
                setViewColor(this.filter_effect_vignette, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_vignette, bean);
                return;
            case LEAK:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                for (i = 0; i < 4; i++) {
                    if (((ImageView) this.filterEffectLeakList.get(i)).getAlpha() == 1.0f) {
                        if (this.leakFilter != null) {
                            depth = this.leakFilter.getDepth();
                        } else {
                            depth = 0;
                        }
                        bean.selectPosition = new float[]{(float) i, (float) depth};
                    }
                }
                setViewColor(this.filter_effect_leak, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_leak, bean);
                return;
            case TINGE:
                setViewColor(this.filter_effect_tinge, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_tinge, bean);
                return;
            case DATE:
                if (bean.getItemSelect()) {
                    this.filter_effect_date.setProgressValue(10.0f);
                    setViewColor(this.filter_effect_date, true);
                    return;
                }
                this.currentEffectType = FilterEffectManager.EffectType.NONE;
                this.filter_effect_date.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                setViewColor(this.filter_effect_date, false);
                return;
            case DUST:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                for (i = 0; i < this.filterEffectLeakList.size(); i++) {
                    if (((ImageView) this.filterEffectLeakList.get(i)).getAlpha() == 1.0f) {
                        if (this.dustFilter != null) {
                            depth = this.dustFilter.getDepth();
                        } else {
                            depth = 0;
                        }
                        bean.selectPosition = new float[]{(float) i, (float) depth};
                    }
                }
                setViewColor(this.filter_effect_dust, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_dust, bean);
                return;
            case PRISM:
                setViewColor(this.filter_effect_prism, this.mFilterEffectManager.setSelectEffect(bean));
                setFilterIconProgress(this.filter_effect_prism, bean);
                return;
            case STRENGTH:
                alphaStrengthEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll, false);
                alphaEnterAndExit(this.filter_filter_root_rl, this.filter_rule_ll, new AnimatorEndListener() {
                    public void onAnimationEnd(Animator animation) {
                        filter_filter_root_rl.setVisibility(View.VISIBLE);
                        filter_tab_choose_confirm.setEnabled(true);
                    }
                });
                this.mFilterEffectManager.setSelectEffect(bean);
                LongVideosModel currentVideoModel = getCurrentVideoModel();
                if (currentVideoModel == null) {
                    return;
                }
                if (currentVideoModel.getFilterState() == 0) {
                    Iterator it = this.mVideoAudioManager.getVideosModelList().iterator();
                    while (it.hasNext()) {
                        ((LongVideosModel) it.next()).setPublicIntensity((1.0f * bean.value[1]) / 10.0f);
                    }
                    return;
                }
                currentVideoModel.setPrivateIntensity((1.0f * bean.value[1]) / 10.0f);
                return;
            default:
                return;
        }
    }

    private void setFilterIconProgress(final FilterEffectSetRelativeLayout view, FilterEffectBean bean) {
        if (view != null) {
            float currentValue = view.getProgressValue() * ((float) view.getDefaultMultiplier());
            final float targetValue = bean.value[1];
            LogUtil.d("setFilterIconProgress", String.format("effectType :  %s ,  currentValue : %s , targetValue : %s ", new Object[]{bean.effectType, Float.valueOf(currentValue), Float.valueOf(targetValue)}));
            if (FilterEffectManager.EffectType.ROTATE == bean.effectType) {
                float select = bean.selectPosition[0];
                float value = bean.value[1];
                if (select == StaticLayoutUtil.DefaultSpacingadd && value == StaticLayoutUtil.DefaultSpacingadd) {
                    view.setProgressDefaultMultiplier(1);
                    view.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                    return;
                }
                view.setProgressDefaultMultiplier(1);
                view.setProgressValue(10.0f);
            } else if (currentValue == targetValue) {
            } else {
                if (currentValue * targetValue < StaticLayoutUtil.DefaultSpacingadd) {
                    view.setProgressDefaultMultiplier(currentValue > StaticLayoutUtil.DefaultSpacingadd ? 1 : -1);
                    AnimationUtil.filterEffectIconAlphaAnimation(view, Math.abs(currentValue), StaticLayoutUtil.DefaultSpacingadd, 100, new AlphaAnimatorCallback() {
                        public void onAnimationStart() {
                        }

                        public void onAnimationEnd() {
                            view.setProgressDefaultMultiplier(targetValue > StaticLayoutUtil.DefaultSpacingadd ? 1 : -1);
                            AnimationUtil.filterEffectIconAlphaAnimation(view, StaticLayoutUtil.DefaultSpacingadd, Math.abs(targetValue), 100, null);
                        }
                    });
                    return;
                }
                int multiplier;
                if (currentValue > StaticLayoutUtil.DefaultSpacingadd) {
                    multiplier = 1;
                } else if (currentValue < StaticLayoutUtil.DefaultSpacingadd) {
                    multiplier = -1;
                } else if (targetValue < StaticLayoutUtil.DefaultSpacingadd) {
                    multiplier = -1;
                } else {
                    multiplier = 1;
                }
                view.setProgressDefaultMultiplier(multiplier);
                AnimationUtil.filterEffectIconAlphaAnimation(view, Math.abs(currentValue), Math.abs(targetValue), 200, null);
            }
        }
    }

    private boolean checkHasCropAndFixLeak() {
        return checkHasCropAndFixFilter(this.leakFilter);
    }

    private boolean checkHasCropAndFixDust() {
        return checkHasCropAndFixFilter(this.dustFilter);
    }

    private boolean checkHasCropAndFixFilter(AFGPUImageScreenBlendFilter leakFilter) {
        if (!this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.CROP)) {
            return false;
        }
        FilterEffectBean cropItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP);
        if (cropItem == null) {
            return false;
        }
        float[] selectPosition = cropItem.selectPosition;
        if (selectPosition == null || selectPosition.length < 7) {
            return false;
        }
        fixLeakFilter(leakFilter, selectPosition[2], selectPosition[3]);
        return true;
    }

    private void onCropConfirmPress(FilterEffectBean bean) {
        int i;
        if (isVideoType()) {
            this.custom_crop_View.refreshPercentValue();
            this.custom_crop_View.setVisibility(View.GONE);
            for (i = 0; i < this.mFilterEffectDetailSets.size(); i++) {
                if (((FilterEffectSetRelativeLayout) this.mFilterEffectDetailSets.get(i)).getIsSelect()) {
                    bean.value = new float[]{(float) i};
                }
            }
            setViewColor(this.filter_effect_crop, this.mFilterEffectManager.setSelectEffect(bean));
            if (this.mFilterEffectManager.setSelectEffect(bean)) {
                this.filter_effect_crop.setProgressValue(10.0f);
                return;
            } else {
                this.filter_effect_crop.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
                return;
            }
        }
        this.custom_crop_View.refreshPercentValue();
        float[] cropPercent = this.custom_crop_View.getCropPercent();
        float x = cropPercent[0];
        float y = cropPercent[1];
        float width = cropPercent[2];
        float height = cropPercent[3];
        float[] cropDetail = this.custom_crop_View.getCropDetail();
        float changeWidth = cropDetail[2];
        float changeHeight = cropDetail[3];
        float scale = changeWidth / changeHeight;
        this.custom_crop_View.setVisibility(View.GONE);
        LogUtil.d("OnProgressChanged", String.format("cropPercent , 0 : %s , 1 : %s , 2 : %s , 3 : %s ,scale : %s ", new Object[]{Float.valueOf(x), Float.valueOf(y), Float.valueOf(width), Float.valueOf(height), Float.valueOf(scale)}));
        float finalX = x;
        float finalY = y;
        float finalW = width;
        float finalH = height;
        switch ((int) this.currentDegree) {
            case 90:
                finalX = (1.0f - y) - height;
                finalY = x;
                finalW = height;
                finalH = width;
                break;
            case 180:
                finalX = (1.0f - x) - width;
                finalY = (1.0f - y) - height;
                finalW = width;
                finalH = height;
                break;
            case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                finalX = y;
                finalY = (1.0f - x) - width;
                finalW = height;
                finalH = width;
                break;
        }
        LogUtil.d("fixCropViewWhenHasRotate", String.format(" 3: finalX : %s , finalY : %s , finalW : %s , finalH : %s ", new Object[]{Float.valueOf(finalX), Float.valueOf(finalY), Float.valueOf(finalW), Float.valueOf(finalH)}));
        cropDetail[4] = finalX;
        cropDetail[5] = finalY;
        cropDetail[6] = finalW;
        cropDetail[7] = finalH;
        this.gpuImageCropFilter.setCropRegion(new GPUImageCropFilter.CropRegion(finalX, finalY, finalW, finalH));
        bean.selectPosition = cropDetail;
        for (i = 0; i < this.mFilterEffectDetailSets.size(); i++) {
            if (((FilterEffectSetRelativeLayout) this.mFilterEffectDetailSets.get(i)).getIsSelect()) {
                bean.value = new float[]{(float) i};
            }
        }
        setViewColor(this.filter_effect_crop, this.mFilterEffectManager.setSelectEffect(bean));
        if (this.mFilterEffectManager.setSelectEffect(bean)) {
            this.filter_effect_crop.setProgressValue(10.0f);
        } else {
            this.filter_effect_crop.setProgressValue(StaticLayoutUtil.DefaultSpacingadd);
        }
        fixSurfaceViewSize(scale, width, height, true);
        fixDateFilter();
        fixLeakFilter(this.leakFilter, changeWidth, changeHeight);
        setTransformFilterValue(this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).value[1]);
        setNormalFilter();
        setUndoData(0);
        initCropAllViewStub();
        fixGridViewSizeAfterCrop();
        initMirrorSeekViewStub();
        fixMirrorViewSizeAfterCrop();
    }

    private void fixDateFilter() {
        fixDateFilter(false);
    }

    private void fixDateFilter(boolean rotate) {
        boolean z = true;
        if (this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.DATE) && this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.DATE).getItemSelect()) {
            if (this.dateFilter == null) {
                this.dateFilter = getDateFilter();
                AFGPUImageDateBlendFilter aFGPUImageDateBlendFilter = this.dateFilter;
                int i = this.mCaptureOrientation;
                if (this.mCameraLensType != 1) {
                    z = false;
                }
                aFGPUImageDateBlendFilter.setCaptureOrientation(i, z);
            }
            this.dateFilter.setBitmap(getDateBitmap());
        }
    }

    private void fixLeakFilter(AFGPUImageScreenBlendFilter leakFilter, float changeWidth, float changeHeight) {
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.LEAK)) {
            FilterEffectBean leakItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.LEAK);
            if (leakFilter != null) {
                float result = changeWidth / changeHeight;
                if (this.currentDegree == 90.0f || this.currentDegree == 270.0f) {
                    result = 1.0f / result;
                }
                int change = (int) result;
                int origin = (int) ((((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight));
                if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
                    float value = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).value[0];
                    if (result < 1.0f) {
                        result = 1.0f / result;
                    }
                    leakFilter.refreshCropLeakFilter(result);
                    if (change != origin) {
                        leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueAfterCrop(leakFilter.getCurBuffer(), this.mCaptureOrientation));
                    }
                } else {
                    leakFilter.setByteBuffer(leakFilter.getFirstBuffer());
                }
                leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(leakFilter.getCurBuffer(), (int) this.currentDegree));
            }
        }
    }

    private void fixMirrorViewSizeAfterCrop() {
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
            FilterEffectBean cropItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP);
            if (cropItem != null) {
                float[] selectPosition = cropItem.selectPosition;
                if (selectPosition != null && selectPosition.length >= 7) {
                    int realWidth;
                    int realHeight;
                    float gridScale = selectPosition[2] / selectPosition[3];
                    float usableWidth = (float) DensityUtil.getMetricsWidth(this);
                    float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
                    int marginL = 0;
                    int marginT = 0;
                    int marginR = 0;
                    int marginB = 0;
                    if (gridScale > usableWidth / usableHeight) {
                        realWidth = (int) usableWidth;
                        realHeight = (int) (((float) realWidth) / gridScale);
                        marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
                        marginT = marginB;
                    } else {
                        realHeight = (int) usableHeight;
                        realWidth = (int) (((float) realHeight) * gridScale);
                        marginR = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
                        marginL = marginR;
                    }
                    if (this.mirror_seek_bar != null) {
                        LayoutParams gridParams = (LayoutParams) this.mirror_seek_bar.getLayoutParams();
                        if (this.mIsSquare) {
                            this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
                            int surfaceWidth = this.surfaceParams.width;
                            int surfaceHeight = this.surfaceParams.height;
                            if (surfaceWidth > surfaceHeight) {
                                usableWidth = (float) surfaceHeight;
                                marginL = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                                marginT = 0;
                                marginR = (int) (((float) (surfaceWidth - surfaceHeight)) / 2.0f);
                                marginB = 0;
                            } else {
                                usableWidth = (float) surfaceWidth;
                                marginL = 0;
                                marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                                marginR = 0;
                                marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                            }
                            realWidth = (int) usableWidth;
                            realHeight = (int) usableWidth;
                        }
                        realHeight += realHeight % 2;
                        gridParams.width = realWidth + (realWidth % 2);
                        gridParams.height = realHeight;
                        gridParams.setMargins(marginL, marginT, marginR, marginB);
                        this.mirror_seek_bar.setLayoutParams(gridParams);
                    }
                }
            }
        }
    }

    private void fixGridViewSizeAfterCrop() {
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
            float[] selectPosition = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).selectPosition;
            if (selectPosition != null && selectPosition.length > 7) {
                int realWidth;
                int realHeight;
                LayoutParams gridParams;
                float scale = selectPosition[2] / selectPosition[3];
                float usableWidth = (float) DensityUtil.getMetricsWidth(this);
                float usableHeight = (float) (DensityUtil.getMetricsHeight(this) - getTopAndBottomSumViewHeight());
                int marginL = 0;
                int marginT = 0;
                int marginR = 0;
                int marginB = 0;
                if (scale > usableWidth / usableHeight) {
                    realWidth = (int) usableWidth;
                    realHeight = (int) (((float) realWidth) / scale);
                    marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
                    marginT = marginB;
                } else {
                    realHeight = (int) usableHeight;
                    realWidth = (int) (((float) realHeight) * scale);
                    marginR = (int) ((usableWidth - ((float) realWidth)) / 2.0f);
                    marginL = marginR;
                }
                if (this.filter_crop_ccav != null) {
                    gridParams = (LayoutParams) this.filter_crop_ccav.getLayoutParams();
                } else {
                    gridParams = new LayoutParams(-2, -2);
                }
                realWidth += realWidth % 2;
                realHeight += realHeight % 2;
                gridParams.width = realWidth;
                gridParams.height = realHeight;
                this.animationWidth = realWidth;
                this.animationHeight = realHeight;
                gridParams.setMargins(marginL, marginT, marginR, marginB);
                if (this.filter_crop_ccav != null) {
                    this.filter_crop_ccav.setLayoutParams(gridParams);
                }
            }
        }
    }

    private void fixCropViewWhenHasRotate() {
        if (this.gpuImageCropFilter != null && this.custom_crop_View != null) {
            if (this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.CROP)) {
                float[] selectPosition1 = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).selectPosition;
                float x = StaticLayoutUtil.DefaultSpacingadd;
                float y = StaticLayoutUtil.DefaultSpacingadd;
                float width = 1.0f;
                float height = 1.0f;
                if (selectPosition1 != null && selectPosition1.length > 7) {
                    x = selectPosition1[4];
                    y = selectPosition1[5];
                    width = selectPosition1[6];
                    height = selectPosition1[7];
                    LogUtil.d("fixCropViewWhenHasRotate", String.format(" 1 : x : %s , y : %s , width : %s , height : %s ", new Object[]{Float.valueOf(x), Float.valueOf(y), Float.valueOf(width), Float.valueOf(height)}));
                }
                float finalX = StaticLayoutUtil.DefaultSpacingadd;
                float finalY = StaticLayoutUtil.DefaultSpacingadd;
                float finalW = 1.0f;
                float finalH = 1.0f;
                switch ((int) this.currentDegree) {
                    case 0:
                        finalX = x;
                        finalY = y;
                        finalW = width;
                        finalH = height;
                        break;
                    case 90:
                        finalX = y;
                        finalY = (1.0f - x) - width;
                        finalW = height;
                        finalH = width;
                        break;
                    case 180:
                        finalX = (1.0f - x) - width;
                        finalY = (1.0f - y) - height;
                        finalW = width;
                        finalH = height;
                        break;
                    case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                        finalX = (1.0f - y) - height;
                        finalY = x;
                        finalW = height;
                        finalH = width;
                        break;
                }
                LogUtil.d("fixCropViewWhenHasRotate", String.format(" 2: finalX : %s , finalY : %s , finalW : %s , finalH : %s ", Float.valueOf(finalX), Float.valueOf(finalY), Float.valueOf(finalW), Float.valueOf(finalH)));
                this.custom_crop_View.setLeftWidthHeightPercent(finalX, finalY, finalW, finalH);
            }
        }
    }

    private void onCancelPress(FilterEffectManager.EffectType effectType) {
        restoreSaveAndBack();
        FilterEffectBean filterEffectBean = this.mFilterEffectManager.getAssignedBeanByType(effectType);
        this.lastProgressValue = -1.0f;
        alphaEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll, new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                filter_tab_ll.setVisibility(View.VISIBLE);
                filter_tab_choose_cancle.setEnabled(true);
            }
        });
        if (effectType != FilterEffectManager.EffectType.STRENGTH) {
            setFilterEffectGone();
        }
        float value = 0;
        float selectPosition;
        switch (effectType) {
            case HIGHLIGHT:
                if (this.highLightFilter != null) {
                    value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                    this.highLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * value)), new PointF(0.75f, 0.75f - (0.13f * value)), new PointF(1.0f, 1.0f - (0.23f * value))});
                    break;
                }
                break;
            case SKY:
                if (this.skyFilter != null) {
                    this.skyFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                    break;
                }
                break;
            case SHADOW:
                if (this.shadowFilter != null) {
                    value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                    this.shadowFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.14f * value), new PointF(0.25f, 0.25f + (0.13f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case FADED:
                if (this.fadeFilter != null) {
                    value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                    this.fadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * value), new PointF(0.25f, 0.25f + (0.09f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * value)), new PointF(1.0f, 1.0f - (0.15f * value))});
                    break;
                }
                break;
            case TILT:
                if (this.tiltFilter != null) {
                    this.tiltFilter.setBlurSize((0.4f * filterEffectBean.value[1]) + 1.0f);
                    break;
                }
                break;
            case SHARPEN:
                if (this.sharpenFilter != null) {
                    this.sharpenFilter.setSharpness(filterEffectBean.value[1] * 0.26f);
                    break;
                }
                break;
            case GRAIN:
                if (this.grainFilter != null) {
                    this.grainFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                    break;
                }
                break;
            case TEMPERATURE:
                if (this.whiteBalanceFilter != null) {
                    value = filterEffectBean.value[1] / 5.0f;
                    if (value < StaticLayoutUtil.DefaultSpacingadd) {
                        value = (500.0f * value) + 5000.0f;
                    } else {
                        value = (1500.0f * value) + 5000.0f;
                    }
                    this.whiteBalanceFilter.setTemperature(value);
                    break;
                }
                break;
            case SATURATION:
                if (this.saturationFilter != null) {
                    this.saturationFilter.setSaturation((0.6f * (filterEffectBean.value[1] / 5.0f)) + 1.0f);
                    break;
                }
                break;
            case ROTATE:
                this.currentDegree = filterEffectBean.selectPosition[0];
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_rotation_rotate90.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                this.filter_crop_ccav.setVisibility(View.GONE);
                setCancelRotate(FilterEffectManager.EffectType.ROTATE);
                fixDateFilter(true);
                break;
            case CROP:
                onCropCancelPress();
                break;
            case CONTRAST:
                if (this.contrastFilter != null) {
                    value = (filterEffectBean.value[1] / 5.0f) * 0.12f;
                    this.contrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - value), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f + value), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case MIRROR:
                this.mirror_seek_bar.setVisibility(View.GONE);
                float[] floats = filterEffectBean.value;
                float percent = filterEffectBean.selectPosition[0];
                float result = percent / 100.0f;
                LogUtil.d("Drafts", String.format("percent : %s , aFloat : %s , result : %s ", filterEffectBean.selectPosition[0], floats[0], result));
                switch ((int) floats[0]) {
                    case 0:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(1.0f, 1.0f, this.orientation);
                        break;
                    case 1:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 1.0f, this.orientation);
                        break;
                    case 2:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 3.0f, this.orientation);
                        break;
                    case 3:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 4.0f, this.orientation);
                        break;
                    case 4:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 2.0f, this.orientation);
                        break;
                }
                break;
            case HORIZONTAL:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                setCancleFilterState(FilterEffectManager.EffectType.HORIZONTAL);
                break;
            case VERTICAL:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_crop_ccav.setVisibility(View.GONE);
                setCancleFilterState(FilterEffectManager.EffectType.VERTICAL);
                break;
            case EXPOSURE:
                if (this.exposureFilter != null) {
                    this.mFilterEffectManager.setExposureFileCurrentValue((filterEffectBean.value[1] / 5.0f) * 0.12f);
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + value), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case BEAUTIFY:
                if (this.mBilateralFilter != null) {
                    this.mBilateralFilter.setBlurWeightLocation(filterEffectBean.value[1] / 10.0f);
                    break;
                }
                break;
            case VIGNETTE:
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                if (this.movieFilter != null) {
                    if (this.mBitmapCamera == null) {
                        this.mBitmapCamera = getBitmapForFilterEffect(R.drawable.vignette_020_camera_jpg_70, false, false);
                    }
                    this.movieFilter.setBitmap(this.mBitmapCamera);
                    this.movieFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                    break;
                }
                break;
            case LEAK:
                if (this.leakFilter != null) {
                    selectPosition = filterEffectBean.selectPosition[0];
                    this.leakFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                    if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                        if (this.mBitmapLeak1 == null) {
                            this.mBitmapLeak1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_020_g05_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.leakFilter.setBitmap(this.mBitmapLeak1, 1);
                    } else if (selectPosition == 1.0f) {
                        if (this.mBitmapLeak2 == null) {
                            this.mBitmapLeak2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_035_g69_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.leakFilter.setBitmap(this.mBitmapLeak2, 1);
                    } else if (selectPosition == 2.0f) {
                        if (this.mBitmapLeak3 == null) {
                            this.mBitmapLeak3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_040_g55_jpg_50_right, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.leakFilter.setBitmap(this.mBitmapLeak3, 2);
                    } else if (selectPosition == 3.0f) {
                        if (this.mBitmapLeak4 == null) {
                            this.mBitmapLeak4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_060_g09_jpg_50_bottom, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.leakFilter.setBitmap(this.mBitmapLeak4, 3);
                    }
                    this.leakFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                    if (!checkHasCropAndFixLeak()) {
                        this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                    }
                }
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                break;
            case TINGE:
                if (this.whiteBalanceFilter != null) {
                    this.whiteBalanceFilter.setTint((filterEffectBean.value[1] / 5.0f) * 40.0f);
                    break;
                }
                break;
            case DATE:
                if (this.dateFilter != null) {
                    this.dateFilter.setOverPercent(1.0f);
                    this.dateFilter.setBitmap(getDateBitmap());
                    break;
                }
                break;
            case DUST:
                if (this.dustFilter != null) {
                    selectPosition = filterEffectBean.selectPosition[0];
                    this.dustFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                    if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                        if (this.mBitmapDust1 == null) {
                            this.mBitmapDust1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust01, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.dustFilter.setBitmap(this.mBitmapDust1, 0);
                    } else if (selectPosition == 1.0f) {
                        if (this.mBitmapDust2 == null) {
                            this.mBitmapDust2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust02, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.dustFilter.setBitmap(this.mBitmapDust2, 0);
                    } else if (selectPosition == 2.0f) {
                        if (this.mBitmapDust3 == null) {
                            this.mBitmapDust3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust03, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.dustFilter.setBitmap(this.mBitmapDust3, 0);
                    } else if (selectPosition == 3.0f) {
                        if (this.mBitmapDust4 == null) {
                            this.mBitmapDust4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust04, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.dustFilter.setBitmap(this.mBitmapDust4, 0);
                    } else if (selectPosition == 4.0f) {
                        if (this.mBitmapDust5 == null) {
                            this.mBitmapDust5 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust05, this.leakFilterReqWidth, this.leakFilterReqHeight);
                        }
                        this.dustFilter.setBitmap(this.mBitmapDust5, 0);
                    }
                    this.dustFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                    if (!checkHasCropAndFixLeak()) {
                        this.dustFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.dustFilter.getCurBuffer(), (int) this.currentDegree));
                    }
                }
                this.filter_stablize_ra.setVisibility(View.VISIBLE);
                this.filter_effect_leak_detail.setVisibility(View.GONE);
                onEffectTabShowHideOthers();
                break;
            case PRISM:
                if (this.prismFilter != null) {
                    this.prismFilter.setStrength(filterEffectBean.value[1] / 10.0f);
                    break;
                }
                break;
            case STRENGTH:
                alphaEnterAndExit(this.filter_tab_ll, this.filter_tab_choose_ll);
                alphaEnterAndExit(this.filter_filter_root_rl, this.filter_rule_ll);
                this.strengthValue = filterEffectBean.value[1] / 10.0f;
                changeLongVideosModelStrengthValue(this.strengthValue);
                this.currentFilter.setIntensity(this.strengthValue);
                break;
        }
        this.currentEffectType = FilterEffectManager.EffectType.NONE;
        setNormalFilter();
    }

    private void onCropCancelPress() {
        this.custom_crop_View.setVisibility(View.GONE);
        if (!isVideoType()) {
            List<FilterEffectBean> mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
            if (this.mFilterEffectManager.listAContainItem((List) mList, FilterEffectManager.EffectType.CROP)) {
                float[] selectPosition = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).selectPosition;
                float x = selectPosition[4];
                float y = selectPosition[5];
                float width = selectPosition[6];
                float height = selectPosition[7];
                float changeWidth = selectPosition[2];
                float changeHeight = selectPosition[3];
                float scale = changeWidth / changeHeight;
                fixLeakFilterOnCropCancel(changeWidth, changeHeight);
                LogUtil.d("OnProgressChanged", String.format("cropPercent , 0 : %s , 1 : %s , 2 : %s , 3 : %s ,scale : %s ", x, y, width, height, scale));
                this.gpuImageCropFilter.setCropRegion(new GPUImageCropFilter.CropRegion(x, y, width, height));
                float finalW = 1.0f;
                float finalH = 1.0f;
                boolean containRotateItem = this.mFilterEffectManager.listAContainItem((List) mList, FilterEffectManager.EffectType.ROTATE);
                float degree = StaticLayoutUtil.DefaultSpacingadd;
                if (containRotateItem) {
                    degree = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                }
                float finalX;
                float finalY;
                switch ((int) degree) {
                    case 0:
                        finalX = x;
                        finalY = y;
                        finalW = width;
                        finalH = height;
                        break;
                    case 90:
                        finalX = y;
                        finalY = (1.0f - x) - width;
                        finalW = height;
                        finalH = width;
                        break;
                    case 180:
                        finalX = (1.0f - x) - width;
                        finalY = (1.0f - y) - height;
                        finalW = width;
                        finalH = height;
                        break;
                    case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                        finalX = (1.0f - y) - height;
                        finalY = x;
                        finalW = height;
                        finalH = width;
                        break;
                }
                fixSurfaceViewSize(scale, finalW, finalH, true);
                initCropAllViewStub();
                fixGridViewSizeAfterCrop();
                initMirrorSeekViewStub();
                fixMirrorViewSizeAfterCrop();
            }
        }
    }

    private void fixLeakFilterOnCropCancel(float changeWidth, float changeHeight) {
        LogUtil.d("fixLeakFilterOnCropCancel", String.format("changeWidth : %s , changeHeight : %s ", new Object[]{Float.valueOf(changeWidth), Float.valueOf(changeHeight)}));
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.LEAK)) {
            FilterEffectBean leakItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.LEAK);
            if (this.leakFilter != null) {
                float changeF = changeWidth / changeHeight;
                if (this.currentDegree == 90.0f || this.currentDegree == 270.0f) {
                    changeF = 1.0f / changeF;
                }
                int change = (int) changeF;
                int origin = this.mVideoWidth / this.mVideoHeight;
                if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
                    FilterEffectBean cropItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP);
                    if (changeF < 1.0f) {
                        changeF = 1.0f / changeF;
                    }
                    this.leakFilter.refreshCropLeakFilter(changeF);
                    if (change != origin) {
                        this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueAfterCrop(this.leakFilter.getCurBuffer(), this.mCaptureOrientation));
                    }
                } else {
                    this.leakFilter.setByteBuffer(this.leakFilter.getFirstBuffer());
                }
                this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
            }
        }
    }

    private void setValueBeforeRotate() {
        float[] value;
        this.beforeRotate_value_cropWidth = this.mVideoFrames.mCropWidth;
        this.beforeRotate_value_cropHeight = this.mVideoFrames.mCropHeight;
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
            value = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).value;
            if (value != null && value.length > 1) {
                this.beforeRotate_value_cropPosition = value[0];
            }
        }
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.MIRROR)) {
            FilterEffectBean mirrorItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.MIRROR);
            value = mirrorItem.value;
            float[] percent = mirrorItem.selectPosition;
            this.beforeRotate_value_mirrorValue = value[0];
            this.beforeRotate_value_mirrorPercent = percent[0];
        }
        if (this.filter_crop_ccav != null) {
            LayoutParams layoutParams = (LayoutParams) this.filter_crop_ccav.getLayoutParams();
            this.beforeRotate_value_width = layoutParams.width;
            this.beforeRotate_value_height = layoutParams.height;
            this.beforeRotate_value_marginT = layoutParams.topMargin;
            this.beforeRotate_value_marginL = layoutParams.leftMargin;
            this.beforeRotate_value_marginR = layoutParams.rightMargin;
            this.beforeRotate_value_marginB = layoutParams.bottomMargin;
        }
    }

    private void setCancelRotate(FilterEffectManager.EffectType effectType) {
        float[] value;
        this.mVideoFrames.mCropWidth = this.beforeRotate_value_cropWidth;
        this.mVideoFrames.mCropHeight = this.beforeRotate_value_cropHeight;
        this.currentEffectType = effectType;
        List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.CROP)) {
            value = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).value;
            if (value != null && value.length > 1) {
                value[0] = this.beforeRotate_value_cropPosition;
            }
        }
        if (this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.MIRROR)) {
            FilterEffectBean mirrorItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.MIRROR);
            value = mirrorItem.value;
            float[] percent = mirrorItem.selectPosition;
            value[0] = this.beforeRotate_value_mirrorValue;
            percent[0] = this.beforeRotate_value_mirrorPercent;
            mirrorItem.value = value;
            mirrorItem.selectPosition = percent;
            float orientation = StaticLayoutUtil.DefaultSpacingadd;
            switch ((int) this.beforeRotate_value_mirrorValue) {
                case 0:
                    orientation = 1.0f;
                    break;
                case 1:
                    orientation = 1.0f;
                    break;
                case 2:
                    orientation = 3.0f;
                    break;
                case 3:
                    orientation = 4.0f;
                    break;
                case 4:
                    orientation = 2.0f;
                    break;
            }
            if (this.mGPUImageMirrorFilter != null) {
                this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(this.beforeRotate_value_mirrorPercent / 100.0f, orientation, this.mCaptureOrientation);
            }
        }
        if (this.filter_crop_ccav != null) {
            LayoutParams layoutParams = (LayoutParams) this.filter_crop_ccav.getLayoutParams();
            layoutParams.width = this.beforeRotate_value_width;
            layoutParams.height = this.beforeRotate_value_height;
            layoutParams.topMargin = this.beforeRotate_value_marginT;
            layoutParams.leftMargin = this.beforeRotate_value_marginL;
            layoutParams.rightMargin = this.beforeRotate_value_marginR;
            layoutParams.bottomMargin = this.beforeRotate_value_marginB;
        }
        if (this.mirror_seek_bar != null) {
            LayoutParams mirrorParams = (LayoutParams) this.mirror_seek_bar.getLayoutParams();
            mirrorParams.width = this.beforeRotate_value_width;
            mirrorParams.height = this.beforeRotate_value_height;
            mirrorParams.topMargin = this.beforeRotate_value_marginT;
            mirrorParams.leftMargin = this.beforeRotate_value_marginL;
            mirrorParams.rightMargin = this.beforeRotate_value_marginR;
            mirrorParams.bottomMargin = this.beforeRotate_value_marginB;
        }
        getAnnalisticTransformArray();
    }

    private void setCancleFilterState(FilterEffectManager.EffectType effectType) {
        if (effectType == FilterEffectManager.EffectType.ROTATE) {
            if (this.currentDegree == 90.0f || this.currentDegree == 270.0f) {
                this.mVideoFrames.mCropWidth = this.mVideoFrames.mCaptureHeight;
                this.mVideoFrames.mCropHeight = this.mVideoFrames.mCaptureWidth;
            } else {
                this.mVideoFrames.mCropWidth = this.mVideoFrames.mCaptureWidth;
                this.mVideoFrames.mCropHeight = this.mVideoFrames.mCaptureHeight;
            }
        }
        this.currentEffectType = effectType;
        getAnnalisticTransformArray();
    }

    private void getAnnalisticTransformArray() {
        FilterEffectBean rBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE);
        FilterEffectBean hBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL);
        FilterEffectBean vBean = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL);
        float degree = rBean.selectPosition[0];
        this.currentDegree = degree;
        this.transformFilter.setOrientation(this.currentDegree);
        setAnnalisticTransformMatrix(degree, rBean.value[1], hBean.value[1], vBean.value[1]);
    }

    private void setAnnalisticTransformMatrix(float degree, float rValue, float hValue, float vValue) {
        float scale1 = (((float) this.mVideoFrames.mCropHeight) * 1.0f) / ((float) this.mVideoFrames.mCropWidth);
        Matrix matrix = new Matrix();
        this.currentRotateValue = rValue;
        if (rValue != StaticLayoutUtil.DefaultSpacingadd) {
            double cMovieScale;
            rValue = (this.currentRotateValue / 5.0f) * 10.0f;
            double fRotate = Math.abs(((double) (rValue / 180.0f)) * 3.141592653589793d);
            if (scale1 < 1.0f) {
                cMovieScale = (double) (1.0f / scale1);
            } else {
                cMovieScale = (double) scale1;
            }
            float scale = (float) (Math.sin(1.5707963267948966d - fRotate) + (Math.sin(fRotate) * cMovieScale));
            matrix.postRotate(rValue);
            matrix.postScale(scale, scale);
        }
        float[] a = new float[9];
        matrix.getValues(a);
        float[] b = new float[]{a[0], a[1], a[2], StaticLayoutUtil.DefaultSpacingadd, a[3], a[4], a[5], StaticLayoutUtil.DefaultSpacingadd, a[6], a[7], a[8], StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f};
        Size size = null;
        float chValue = StaticLayoutUtil.DefaultSpacingadd;
        float cvValue = StaticLayoutUtil.DefaultSpacingadd;
        boolean fromDraftOrLocalVideo = 3 == this.mDataFrom || (1 == this.mDataFrom && this.mDataType == 0);
        if (!(hValue == StaticLayoutUtil.DefaultSpacingadd && vValue == StaticLayoutUtil.DefaultSpacingadd)) {
            Size size2;
            if (degree == StaticLayoutUtil.DefaultSpacingadd || degree == 180.0f) {
                if (this.orientation != 0 && this.orientation != 180) {
                    chValue = hValue / 50.0f;
                    cvValue = vValue / 50.0f;
                } else if (fromDraftOrLocalVideo) {
                    chValue = hValue / 50.0f;
                    cvValue = vValue / 50.0f;
                } else {
                    chValue = vValue / 50.0f;
                    cvValue = hValue / 50.0f;
                }
                size2 = new Size(2.0f, 2.0f / ((((float) this.mVideoFrames.mCropWidth) * 1.0f) / ((float) this.mVideoFrames.mCropHeight)));
            } else {
                if (this.orientation != 0 && this.orientation != 180) {
                    chValue = vValue / 50.0f;
                    cvValue = hValue / 50.0f;
                } else if (fromDraftOrLocalVideo) {
                    chValue = vValue / 50.0f;
                    cvValue = hValue / 50.0f;
                } else {
                    chValue = hValue / 50.0f;
                    cvValue = vValue / 50.0f;
                }
                size2 = new Size(2.0f, 2.0f / ((((float) this.mVideoFrames.mCropWidth) * 1.0f) / ((float) this.mVideoFrames.mCropHeight)));
            }
            boolean orientation90 = this.orientation == 90;
            boolean orientation270 = this.orientation == 270;
            if (degree == 180.0f || degree == 270.0f || orientation90) {
                chValue = -chValue;
            }
            if (degree == StaticLayoutUtil.DefaultSpacingadd || degree == 90.0f || degree == 180.0f) {
                boolean shouldChange = 3 != this.mDataFrom || ((1 == this.mDataFrom && this.mDataType == 0) || !orientation270);
                if (shouldChange) {
                    cvValue = -cvValue;
                }
            }
        }
        if (size != null) {
            b[3] = (chValue / size.width) * 2.0f;
            b[7] = (cvValue / size.height) * 2.0f;
            b[15] = (1.0f - Math.abs(chValue)) - Math.abs(cvValue);
        }
        this.transformFilter.setTransform3D(b);
        this.transformFilter.setIgnoreAspectRatio(false);
        this.finalTransformArray = this.transformFilter.getTransform3D();
    }

    private void loadFilterEffectJustInit(FilterEffectBean filterEffectBean) {
        float value = 0;
        float selectPosition;
        switch (filterEffectBean.effectType) {
            case HIGHLIGHT:
                if (this.highLightFilter == null) {
                    this.highLightFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.highLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * value)), new PointF(0.75f, 0.75f - (0.13f * value)), new PointF(1.0f, 1.0f - (0.23f * value))});
                break;
            case SKY:
                break;
            case SHADOW:
                if (this.shadowFilter == null) {
                    this.shadowFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.shadowFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.14f * value), new PointF(0.25f, 0.25f + (0.13f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f), new PointF(1.0f, 1.0f)});
                return;
            case FADED:
                if (this.fadeFilter == null) {
                    this.fadeFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.fadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * value), new PointF(0.25f, 0.25f + (0.09f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * value)), new PointF(1.0f, 1.0f - (0.15f * value))});
                return;
            case TILT:
                if (this.tiltFilter == null) {
                    this.tiltFilter = getGaussianFilter();
                }
                this.tiltFilter.setBlurSize((0.4f * filterEffectBean.value[1]) + 1.0f);
                return;
            case SHARPEN:
                if (this.sharpenFilter == null) {
                    this.sharpenFilter = new GPUImageSharpenFilter();
                }
                this.sharpenFilter.setSharpness(filterEffectBean.value[1] * 0.26f);
                return;
            case GRAIN:
                if (this.grainFilter == null) {
                    this.grainFilter = getGrainFilter();
                }
                if (this.mBitmapGrain == null) {
                    this.mBitmapGrain = getBitmapForFilterEffect(R.drawable.grains_iso_400_jpg_50, true, false);
                }
                this.grainFilter.setBitmap(this.mBitmapGrain);
                this.grainFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                return;
            case TEMPERATURE:
                if (this.whiteBalanceFilter == null) {
                    this.whiteBalanceFilter = getWhiteBalanceFilter();
                }
                value = filterEffectBean.value[1] / 5.0f;
                if (value < StaticLayoutUtil.DefaultSpacingadd) {
                    value = (500.0f * value) + 5000.0f;
                } else {
                    value = (1500.0f * value) + 5000.0f;
                }
                this.whiteBalanceFilter.setTemperature(value);
                return;
            case SATURATION:
                if (this.saturationFilter == null) {
                    this.saturationFilter = getSaturationFilter();
                }
                this.saturationFilter.setSaturation((0.6f * (filterEffectBean.value[1] / 5.0f)) + 1.0f);
                return;
            case ROTATE:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentDegree = filterEffectBean.selectPosition[0];
                this.transformFilter.setOrientation(this.currentDegree);
                this.currentEffectType = FilterEffectManager.EffectType.ROTATE;
                editFilterIntensity(filterEffectBean.value[1]);
                return;
            case CROP:
                if (this.gpuImageCropFilter == null) {
                    this.gpuImageCropFilter = new GPUImageCropFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
                    onCropInitAndRestore(filterEffectBean);
                    return;
                }
                return;
            case CONTRAST:
                if (this.contrastFilter == null) {
                    this.contrastFilter = getToneCurveFilter();
                }
                value = (filterEffectBean.value[1] / 5.0f) * 0.12f;
                this.contrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - value), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f + value), new PointF(1.0f, 1.0f)});
                return;
            case MIRROR:
                if (this.mGPUImageMirrorFilter == null) {
                    this.mGPUImageMirrorFilter = new GPUImageMirrorFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
                }
                float result = filterEffectBean.selectPosition[0] / 100.0f;
                LogUtil.d("Drafts", String.format("percent : %s , aFloat : %s , result : %s ", filterEffectBean.selectPosition[0], filterEffectBean.value[0], result));
                switch ((int) filterEffectBean.value[0]) {
                    case 0:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(1.0f, 1.0f, this.orientation);
                        return;
                    case 1:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 1.0f, this.orientation);
                        return;
                    case 2:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 3.0f, this.orientation);
                        return;
                    case 3:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 4.0f, this.orientation);
                        return;
                    case 4:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 2.0f, this.orientation);
                        return;
                    default:
                        return;
                }
            case HORIZONTAL:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentEffectType = FilterEffectManager.EffectType.HORIZONTAL;
                float degree = this.mFilterEffectManager.getCurrentListItem(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                float value1 = filterEffectBean.value[1];
                if (degree == 90.0f || degree == 270.0f) {
                    value1 = this.mFilterEffectManager.getCurrentListItem(FilterEffectManager.EffectType.VERTICAL).value[1];
                }
                editFilterIntensity(value1);
                return;
            case VERTICAL:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentEffectType = FilterEffectManager.EffectType.VERTICAL;
                editFilterIntensity(filterEffectBean.value[1]);
                return;
            case EXPOSURE:
                if (this.exposureFilter == null) {
                    this.exposureFilter = getToneCurveFilter();
                }
                this.mFilterEffectManager.setExposureFileCurrentValue((filterEffectBean.value[1] / 5.0f) * 0.12f);
                this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + value), new PointF(1.0f, 1.0f)});
                return;
            case BEAUTIFY:
                if (this.mBilateralFilter == null) {
                    this.mBilateralFilter = getBilateralFilter();
                }
                this.mBilateralFilter.setBlurWeightLocation(filterEffectBean.value[1] / 10.0f);
                return;
            case VIGNETTE:
                if (this.movieFilter == null) {
                    this.movieFilter = getMoviewFilter2();
                }
                if (this.mBitmapCamera == null) {
                    this.mBitmapCamera = getBitmapForFilterEffect(2130838363, false, false);
                }
                this.movieFilter.setBitmap(this.mBitmapCamera);
                this.movieFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                return;
            case LEAK:
                if (this.leakFilter == null) {
                    this.leakFilter = getScreenBlendFilter();
                    this.leakFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    this.leakFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                }
                selectPosition = filterEffectBean.selectPosition[0];
                this.leakFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                    if (this.mBitmapLeak1 == null) {
                        this.mBitmapLeak1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_020_g05_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak1, 1);
                } else if (selectPosition == 1.0f) {
                    if (this.mBitmapLeak2 == null) {
                        this.mBitmapLeak2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_035_g69_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak2, 1);
                } else if (selectPosition == 2.0f) {
                    if (this.mBitmapLeak3 == null) {
                        this.mBitmapLeak3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_040_g55_jpg_50_right, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak3, 2);
                } else if (selectPosition == 3.0f) {
                    if (this.mBitmapLeak4 == null) {
                        this.mBitmapLeak4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_060_g09_jpg_50_bottom, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak4, 3);
                }
                this.leakFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                this.leakFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                return;
            case TINGE:
                if (this.whiteBalanceFilter == null) {
                    this.whiteBalanceFilter = getWhiteBalanceFilter();
                }
                this.whiteBalanceFilter.setTint((filterEffectBean.value[1] / 5.0f) * 40.0f);
                return;
            case DATE:
                if (this.dateFilter == null) {
                    this.dateFilter = getDateFilter();
                    this.dateFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                }
                this.dateFilter.setBitmap(getDateBitmap());
                return;
            case DUST:
                if (this.dustFilter == null) {
                    this.dustFilter = getScreenBlendFilter();
                    this.dustFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    this.dustFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                }
                selectPosition = filterEffectBean.selectPosition[0];
                this.dustFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                    if (this.mBitmapDust1 == null) {
                        this.mBitmapDust1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust01, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust1, 0);
                } else if (selectPosition == 1.0f) {
                    if (this.mBitmapDust2 == null) {
                        this.mBitmapDust2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust02, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust2, 0);
                } else if (selectPosition == 2.0f) {
                    if (this.mBitmapDust3 == null) {
                        this.mBitmapDust3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust03, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust3, 0);
                } else if (selectPosition == 3.0f) {
                    if (this.mBitmapDust4 == null) {
                        this.mBitmapDust4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust04, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust4, 0);
                } else if (selectPosition == 4.0f) {
                    if (this.mBitmapDust5 == null) {
                        this.mBitmapDust5 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust05, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust5, 0);
                }
                this.dustFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                if (!checkHasCropAndFixLeak()) {
                    this.dustFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.dustFilter.getCurBuffer(), (int) this.currentDegree));
                }
                this.dustFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                return;
            case PRISM:
                if (this.prismFilter == null) {
                    this.prismFilter = getPrismFilter();
                }
                this.prismFilter.setStrength(filterEffectBean.value[1] / 10.0f);
                return;
            default:
                return;
        }
        if (this.skyFilter == null) {
            this.skyFilter = getSkyFilter();
        }
        if (this.mBitmapSky == null) {
            this.mBitmapSky = getOriginalFilterBitmap(R.drawable.sky_gradient, false, false);
        }
        this.skyFilter.setBitmap(this.mBitmapSky);
        this.skyFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
    }

    private void loadFilterEffect(FilterEffectBean filterEffectBean) {
        loadFilterEffect(filterEffectBean, true, true);
    }

    private void loadFilterEffect(FilterEffectBean filterEffectBean, boolean needRefreshEffectType, boolean needSetNormal) {
        FilterEffectManager.EffectType type = filterEffectBean.effectType;
        boolean isSelect = filterEffectBean.getItemSelect();
        if (needRefreshEffectType) {
            this.currentEffectType = filterEffectBean.effectType;
        }
        float value = 0;
        float[] fArr;
        float selectPosition;
        switch (type) {
            case HIGHLIGHT:
                if (this.highLightFilter == null) {
                    this.highLightFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.highLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * value)), new PointF(0.75f, 0.75f - (0.13f * value)), new PointF(1.0f, 1.0f - (0.23f * value))});
                break;
            case SKY:
                if (this.skyFilter == null) {
                    this.skyFilter = getSkyFilter();
                }
                if (this.mBitmapSky == null) {
                    this.mBitmapSky = getOriginalFilterBitmap(2130838324, false, false);
                }
                this.skyFilter.setBitmap(this.mBitmapSky);
                this.skyFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                break;
            case SHADOW:
                if (this.shadowFilter == null) {
                    this.shadowFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.shadowFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.14f * value), new PointF(0.25f, 0.25f + (0.13f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f), new PointF(1.0f, 1.0f)});
                break;
            case FADED:
                if (this.fadeFilter == null) {
                    this.fadeFilter = getToneCurveFilter();
                }
                value = (1.0f + ((filterEffectBean.value[1] - 5.0f) / 5.0f)) / 2.0f;
                this.fadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * value), new PointF(0.25f, 0.25f + (0.09f * value)), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * value)), new PointF(1.0f, 1.0f - (0.15f * value))});
                break;
            case TILT:
                if (this.tiltFilter == null) {
                    this.tiltFilter = getGaussianFilter();
                }
                this.tiltFilter.setBlurSize((0.4f * filterEffectBean.value[1]) + 1.0f);
                break;
            case SHARPEN:
                if (this.sharpenFilter == null) {
                    this.sharpenFilter = new GPUImageSharpenFilter();
                }
                this.sharpenFilter.setSharpness(filterEffectBean.value[1] * 0.26f);
                break;
            case GRAIN:
                if (this.grainFilter == null) {
                    this.grainFilter = getGrainFilter();
                }
                if (this.mBitmapGrain == null) {
                    this.mBitmapGrain = getBitmapForFilterEffect(R.drawable.grains_iso_400_jpg_50, true, false);
                }
                this.grainFilter.setBitmap(this.mBitmapGrain);
                this.grainFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                break;
            case TEMPERATURE:
                if (this.whiteBalanceFilter == null) {
                    this.whiteBalanceFilter = getWhiteBalanceFilter();
                }
                value = filterEffectBean.value[1] / 5.0f;
                if (value < StaticLayoutUtil.DefaultSpacingadd) {
                    value = (500.0f * value) + 5000.0f;
                } else {
                    value = (1500.0f * value) + 5000.0f;
                }
                this.whiteBalanceFilter.setTemperature(value);
                break;
            case SATURATION:
                if (this.saturationFilter == null) {
                    this.saturationFilter = getSaturationFilter();
                }
                this.saturationFilter.setSaturation((0.6f * (filterEffectBean.value[1] / 5.0f)) + 1.0f);
                break;
            case ROTATE:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentDegree = filterEffectBean.selectPosition[0];
                this.transformFilter.setOrientation(this.currentDegree);
                this.currentEffectType = FilterEffectManager.EffectType.ROTATE;
                editFilterIntensity(filterEffectBean.value[1]);
                break;
            case CROP:
                if (this.gpuImageCropFilter == null) {
                    this.gpuImageCropFilter = new GPUImageCropFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
                    onCropInitAndRestore(filterEffectBean);
                    break;
                }
                break;
            case CONTRAST:
                if (this.contrastFilter == null) {
                    this.contrastFilter = getToneCurveFilter();
                }
                value = (filterEffectBean.value[1] / 5.0f) * 0.12f;
                this.contrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - value), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f + value), new PointF(1.0f, 1.0f)});
                break;
            case MIRROR:
                if (this.mGPUImageMirrorFilter == null) {
                    this.mGPUImageMirrorFilter = new GPUImageMirrorFilter(this.mCaptureOrientation, this.mCameraLensType == 1);
                }
                float result = filterEffectBean.selectPosition[0] / 100.0f;
                LogUtil.d("Drafts", String.format("percent : %s , aFloat : %s , result : %s ",filterEffectBean.selectPosition[0], filterEffectBean.value[0], result));
                switch ((int) filterEffectBean.value[0]) {
                    case 0:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(1.0f, 1.0f, this.orientation);
                        break;
                    case 1:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 1.0f, this.orientation);
                        break;
                    case 2:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 3.0f, this.orientation);
                        break;
                    case 3:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 4.0f, this.orientation);
                        break;
                    case 4:
                        this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, 2.0f, this.orientation);
                        break;
                }
                break;
            case HORIZONTAL:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentEffectType = FilterEffectManager.EffectType.HORIZONTAL;
                float degree = this.mFilterEffectManager.getCurrentListItem(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                float value1 = filterEffectBean.value[1];
                if (degree == 90.0f || degree == 270.0f) {
                    value1 = this.mFilterEffectManager.getCurrentListItem(FilterEffectManager.EffectType.VERTICAL).value[1];
                }
                editFilterIntensity(value1);
                break;
            case VERTICAL:
                if (this.transformFilter == null) {
                    this.transformFilter = getTransformFilter();
                    this.transformFilter.setVideoRatio(this.mVideoWidth, this.mVideoHeight);
                    this.transformFilter.setIgnoreAspectRatio(false);
                }
                this.currentEffectType = FilterEffectManager.EffectType.VERTICAL;
                editFilterIntensity(filterEffectBean.value[1]);
                break;
            case EXPOSURE:
                if (this.exposureFilter == null) {
                    this.exposureFilter = getToneCurveFilter();
                }
                this.mFilterEffectManager.setExposureFileCurrentValue((filterEffectBean.value[1] / 5.0f) * 0.12f);
                this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + value), new PointF(1.0f, 1.0f)});
                break;
            case BEAUTIFY:
                if (this.mBilateralFilter == null) {
                    this.mBilateralFilter = getBilateralFilter();
                }
                this.mBilateralFilter.setBlurWeightLocation(filterEffectBean.value[1] / 10.0f);
                break;
            case VIGNETTE:
                if (this.movieFilter == null) {
                    this.movieFilter = getMoviewFilter2();
                }
                if (this.mBitmapCamera == null) {
                    this.mBitmapCamera = getBitmapForFilterEffect(R.drawable.vignette_020_camera_jpg_70, false, false);
                }
                this.movieFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                this.movieFilter.setBitmap(this.mBitmapCamera);
                break;
            case LEAK:
                if (this.leakFilter == null) {
                    this.leakFilter = getScreenBlendFilter();
                    this.leakFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    fArr = new float[8];
                    this.leakFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                }
                selectPosition = filterEffectBean.selectPosition[0];
                this.leakFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                    if (this.mBitmapLeak1 == null) {
                        this.mBitmapLeak1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_020_g05_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak1, 1);
                } else if (selectPosition == 1.0f) {
                    if (this.mBitmapLeak2 == null) {
                        this.mBitmapLeak2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_035_g69_jpg_50_left, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak2, 1);
                } else if (selectPosition == 2.0f) {
                    if (this.mBitmapLeak3 == null) {
                        this.mBitmapLeak3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_040_g55_jpg_50_right, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak3, 2);
                } else if (selectPosition == 3.0f) {
                    if (this.mBitmapLeak4 == null) {
                        this.mBitmapLeak4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_060_g09_jpg_50_bottom, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.leakFilter.setBitmap(this.mBitmapLeak4, 3);
                }
                this.leakFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                this.leakFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                break;
            case TINGE:
                if (this.whiteBalanceFilter == null) {
                    this.whiteBalanceFilter = getWhiteBalanceFilter();
                }
                this.whiteBalanceFilter.setTint((filterEffectBean.value[1] / 5.0f) * 40.0f);
                break;
            case DATE:
                if (this.dateFilter == null) {
                    this.dateFilter = getDateFilter();
                    this.dateFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                }
                this.dateFilter.setBitmap(getDateBitmap());
                break;
            case DUST:
                if (this.dustFilter == null) {
                    this.dustFilter = getScreenBlendFilter();
                    this.dustFilter.setCaptureOrientation(this.mCaptureOrientation, this.mCameraLensType == 1);
                    fArr = new float[8];
                    this.dustFilter.setByteBufferFirst(ByteBufferUtils.getFloatByCaptureOrientationValue(new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f, 1.0f, 1.0f}, this.mCaptureOrientation));
                }
                selectPosition = filterEffectBean.selectPosition[0];
                this.dustFilter.setDepth((int) filterEffectBean.selectPosition[1]);
                if (selectPosition == StaticLayoutUtil.DefaultSpacingadd) {
                    if (this.mBitmapDust1 == null) {
                        this.mBitmapDust1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust01, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust1, 0);
                } else if (selectPosition == 1.0f) {
                    if (this.mBitmapDust2 == null) {
                        this.mBitmapDust2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust02, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust2, 0);
                } else if (selectPosition == 2.0f) {
                    if (this.mBitmapDust3 == null) {
                        this.mBitmapDust3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust03, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust3, 0);
                } else if (selectPosition == 3.0f) {
                    if (this.mBitmapDust4 == null) {
                        this.mBitmapDust4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust04, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust4, 0);
                } else if (selectPosition == 4.0f) {
                    if (this.mBitmapDust5 == null) {
                        this.mBitmapDust5 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust05, this.leakFilterReqWidth, this.leakFilterReqHeight);
                    }
                    this.dustFilter.setBitmap(this.mBitmapDust5, 0);
                }
                this.dustFilter.refreshCropLeakFilterFirst((((float) this.leakFilterReqWidth) * 1.0f) / ((float) this.leakFilterReqHeight));
                if (!checkHasCropAndFixLeak()) {
                    this.dustFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.dustFilter.getCurBuffer(), (int) this.currentDegree));
                }
                this.dustFilter.setOverPercent(filterEffectBean.value[1] / 10.0f);
                break;
            case PRISM:
                if (this.prismFilter == null) {
                    this.prismFilter = getPrismFilter();
                }
                this.prismFilter.setStrength(filterEffectBean.value[1] / 10.0f);
                break;
        }
        if (needSetNormal) {
            setNormalFilter();
        }
    }

    private void onCropInitAndRestore(FilterEffectBean filterEffectBean) {
        float[] selectPosition1 = filterEffectBean.selectPosition;
        if (selectPosition1 != null && selectPosition1.length > 7) {
            float x = selectPosition1[4];
            float y = selectPosition1[5];
            float width = selectPosition1[6];
            float height = selectPosition1[7];
            if (x >= StaticLayoutUtil.DefaultSpacingadd && y >= StaticLayoutUtil.DefaultSpacingadd && width <= 1.0f && height <= 1.0f && x + width <= 1.0f && y + height <= 1.0f) {
                this.gpuImageCropFilter.setCropRegion(new GPUImageCropFilter.CropRegion(x, y, width, height));
                float finalW = 1.0f;
                float finalH = 1.0f;
                List mList = this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE);
                boolean containItem = this.mFilterEffectManager.listAContainItem(mList, FilterEffectManager.EffectType.ROTATE);
                float degree = StaticLayoutUtil.DefaultSpacingadd;
                if (containItem) {
                    degree = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                }
                float finalX;
                float finalY;
                switch ((int) degree) {
                    case 0:
                        finalX = x;
                        finalY = y;
                        finalW = width;
                        finalH = height;
                        break;
                    case 90:
                        finalX = y;
                        finalY = (1.0f - x) - width;
                        finalW = height;
                        finalH = width;
                        break;
                    case 180:
                        finalX = (1.0f - x) - width;
                        finalY = (1.0f - y) - height;
                        finalW = width;
                        finalH = height;
                        break;
                    case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                        finalX = (1.0f - y) - height;
                        finalY = x;
                        finalW = height;
                        finalH = width;
                        break;
                }
                final float fixW = finalW;
                final float fixH = finalH;
                final float f = selectPosition1[2] / selectPosition1[3];
                runOnUiThread(new Runnable() {
                    public void run() {
                        fixSurfaceViewSize(f, fixW, fixH, true);
                        initCropAllViewStub();
                        fixGridViewSizeAfterCrop();
                        initMirrorSeekViewStub();
                        fixMirrorViewSizeAfterCrop();
                    }
                });
            }
        }
    }

    private int getVideoTypeOriginWHPercent() {
        if (this.mVideoAudioManager == null) {
            return 0;
        }
        ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
        if (videosModelList == null) {
            return 0;
        }
        Iterator it = videosModelList.iterator();
        while (it.hasNext()) {
            LongVideosModel model = (LongVideosModel) it.next();
            if (0 != model.mOriginWHPercent) {
                return model.mOriginWHPercent;
            }
        }
        return 0;
    }

    private void saveVideoTypeOriginWHPercent(int originWHPercent) {
        if (this.mVideoAudioManager != null) {
            ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
            if (videosModelList != null) {
                Iterator it = videosModelList.iterator();
                while (it.hasNext()) {
                    ((LongVideosModel) it.next()).mOriginWHPercent = originWHPercent;
                }
            }
        }
    }

    private void restoreLeakFilter() {
        if (this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.LEAK)) {
            FilterEffectBean leakItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.LEAK);
            if (this.leakFilter != null) {
                this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getFirstBuffer(), (int) this.currentDegree));
            }
        }
    }

    private void loadDustBitmap(int finalI) {
        if (finalI == 0) {
            if (this.mBitmapDust1 == null) {
                PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                    public void run() {
                        mBitmapDust1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust01, leakFilterReqWidth, leakFilterReqHeight);
                        setDustFilterData(mBitmapDust1, 0);
                    }
                });
            } else {
                setDustFilterData(this.mBitmapDust1, 0);
            }
        } else if (finalI == 1) {
            if (this.mBitmapDust2 == null) {
                PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                    public void run() {
                        mBitmapDust2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust02, leakFilterReqWidth, leakFilterReqHeight);
                        setDustFilterData(mBitmapDust2, 0);
                    }
                });
            } else {
                setDustFilterData(this.mBitmapDust2, 0);
            }
        } else if (finalI == 2) {
            if (this.mBitmapDust3 == null) {
                PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                    public void run() {
                        mBitmapDust3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust03, leakFilterReqWidth, leakFilterReqHeight);
                        setDustFilterData(mBitmapDust3, 0);
                    }
                });
            } else {
                setDustFilterData(this.mBitmapDust3, 0);
            }
        } else if (finalI == 3) {
            if (this.mBitmapDust4 == null) {
                PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                    public void run() {
                        mBitmapDust4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust04, leakFilterReqWidth, leakFilterReqHeight);
                        setDustFilterData(mBitmapDust4, 0);
                    }
                });
            } else {
                setDustFilterData(this.mBitmapDust4, 0);
            }
        } else if (finalI != 4) {
        } else {
            if (this.mBitmapDust5 == null) {
                PriorityThreadPoolManager.execute(new PriorityRunnable(5) {
                    public void run() {
                        mBitmapDust5 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.dust05, leakFilterReqWidth, leakFilterReqHeight);
                        setDustFilterData(mBitmapDust5, 0);
                    }
                });
            } else {
                setDustFilterData(this.mBitmapDust5, 0);
            }
        }
    }

    private void setDustFilterData(Bitmap bitmap, int orientation) {
        if (this.dustFilter != null) {
            this.dustFilter.setBitmap(bitmap, orientation, (int) this.currentDegree);
            if (!checkHasCropAndFixDust()) {
                this.dustFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.dustFilter.getCurBuffer(), (int) this.currentDegree));
            }
            setNormalFilter();
        }
    }

    private void loadLeakBitmap(int finalI) {
        if (finalI == 0) {
            if (this.mBitmapLeak1 == null) {
                new Thread() {
                    public void run() {
                        super.run();
                        mBitmapLeak1 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_020_g05_jpg_50_left, leakFilterReqWidth, leakFilterReqHeight);
                        if (leakFilter != null) {
                            leakFilter.setBitmap(mBitmapLeak1, 1, (int) currentDegree);
                            if (!checkHasCropAndFixLeak()) {
                                leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(leakFilter.getCurBuffer(), (int) currentDegree));
                            }
                            setNormalFilter();
                        }
                    }
                }.start();
            } else if (this.leakFilter != null) {
                this.leakFilter.setBitmap(this.mBitmapLeak1, 1, (int) this.currentDegree);
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                setNormalFilter();
            }
        } else if (finalI == 1) {
            if (this.mBitmapLeak2 == null) {
                new Thread() {
                    public void run() {
                        super.run();
                        mBitmapLeak2 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_035_g69_jpg_50_left, leakFilterReqWidth, leakFilterReqHeight);
                        if (leakFilter != null) {
                            leakFilter.setBitmap(mBitmapLeak2, 1, (int) currentDegree);
                            if (!checkHasCropAndFixLeak()) {
                                leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(leakFilter.getCurBuffer(), (int) currentDegree));
                            }
                            setNormalFilter();
                        }
                    }
                }.start();
            } else if (this.leakFilter != null) {
                this.leakFilter.setBitmap(this.mBitmapLeak2, 1, (int) this.currentDegree);
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                setNormalFilter();
            }
        } else if (finalI == 2) {
            if (this.mBitmapLeak3 == null) {
                new Thread() {
                    public void run() {
                        super.run();
                        mBitmapLeak3 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_040_g55_jpg_50_right, leakFilterReqWidth, leakFilterReqHeight);
                        if (leakFilter != null) {
                            leakFilter.setBitmap(mBitmapLeak3, 2, (int) currentDegree);
                            if (!checkHasCropAndFixLeak()) {
                                leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(leakFilter.getCurBuffer(), (int) currentDegree));
                            }
                            setNormalFilter();
                        }
                    }
                }.start();
            } else if (this.leakFilter != null) {
                this.leakFilter.setBitmap(this.mBitmapLeak3, 2, (int) this.currentDegree);
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                setNormalFilter();
            }
        } else if (finalI != 3) {
        } else {
            if (this.mBitmapLeak4 == null) {
                new Thread() {
                    public void run() {
                        super.run();
                        mBitmapLeak4 = BitmapUtils.getBitmapForLeakFilter(getResources(), R.drawable.leak_060_g09_jpg_50_bottom, leakFilterReqWidth, leakFilterReqHeight);
                        if (leakFilter != null) {
                            leakFilter.setBitmap(mBitmapLeak4, 3, (int) currentDegree);
                            if (!checkHasCropAndFixLeak()) {
                                leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(leakFilter.getCurBuffer(), (int) currentDegree));
                            }
                            setNormalFilter();
                        }
                    }
                }.start();
            } else if (this.leakFilter != null) {
                this.leakFilter.setBitmap(this.mBitmapLeak4, 3, (int) this.currentDegree);
                if (!checkHasCropAndFixLeak()) {
                    this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueByRotateValue(this.leakFilter.getCurBuffer(), (int) this.currentDegree));
                }
                setNormalFilter();
            }
        }
    }

    private void showFilterEffectRootView(final android.view.View v, final FilterEffectBean filterEffectBean) {
        this.filter_rule_ll.setAlpha(1.0f);
        this.filter_rule_ll.setAnimation(this.translateAnimationShow);
        this.filter_tab_choose_ll.startAnimation(this.translateAnimationShow);
        this.translateAnimationShow.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                filterEffectDetailIsShow = true;
                v.setEnabled(true);
                if (filterEffectBean.getShowType() == 0) {
                    float value = filterEffectBean.value[1];
                    float selectPosition;
                    if (filterEffectBean.effectType == FilterEffectManager.EffectType.HORIZONTAL) {
                        selectPosition = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                        if (selectPosition == 90.0f || selectPosition == 270.0f) {
                            value = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL).value[1];
                        }
                    } else if (filterEffectBean.effectType == FilterEffectManager.EffectType.VERTICAL) {
                        selectPosition = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.ROTATE).selectPosition[0];
                        if (selectPosition == 90.0f || selectPosition == 270.0f) {
                            value = mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL).value[1];
                        }
                    }
                    filter_custom_sk.isCenter(filterEffectBean.isCenterAdsorb()).min(filterEffectBean.value[0]).max(filterEffectBean.value[2]).pro(value).build();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.filter_rule_ll.setVisibility(View.VISIBLE);
        this.filter_effect_root_ll.setVisibility(View.GONE);
    }

    public void setSelectEffectDetail(int position) {
        for (int i = 0; i < this.mFilterEffectDetailSets.size(); i++) {
            if (i == position) {
                this.mFilterEffectDetailSets.get(i).setWhiteColor(getApplicationContext());
            } else {
                this.mFilterEffectDetailSets.get(i).setDateColor(getApplicationContext());
            }
        }
        if (isVideoType()) {
            this.mChangeWHPercent = position;
            changeVideoPercent(this.mChangeWHPercent);
        }
    }

    private void changeVideoPercent(int position) {
        int[] whPercent = getWHPercent(position);
        ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
        if (TextUtil.isValidate(videosModelList)) {
            int h = whPercent[0];
            int w = whPercent[1];
            Iterator it = videosModelList.iterator();
            while (it.hasNext()) {
                LongVideosModel model = (LongVideosModel) it.next();
                if (h == -1 || w == -1) {
                    VideoInputRatio ratio = new VideoInputRatio();
                    if (this.mFirstVideoBean != null) {
                        int rotate = this.mFirstVideoBean.getRotation();
                        int width = this.mFirstVideoBean.getVideoWidth();
                        int height = this.mFirstVideoBean.getVideoHeight();
                        boolean firstH = rotate == 90 || rotate == 270;
                        if (firstH) {
                            int temp = width;
                            width = height;
                            height = temp;
                        }
                        ratio.ratioWidth = width;
                        ratio.ratioHeight = height;
                    } else {
                        ratio.ratioWidth = this.mVideoWidth;
                        ratio.ratioHeight = this.mVideoHeight;
                    }
                    model.setInputRatio(ratio);
                } else {
                    int videoRotate = model.getVideoRotate();
                    model.setInputRatio(new VideoInputRatio(h, w));
                }
            }
            callVideoChange(this.currentPlayTimeUs);
            refreshPlayBtn(w, h, videosModelList.get(0));
            if (TextUtil.isValidate(this.mCurrentTextLongVideoModel)) {
                invalidateVtContainView(this.mCurrentTextLongVideoModel);
                return;
            }
            ArrayList<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
            if (TextUtil.isValidate(textModelList)) {
                invalidateVtContainView(textModelList.get(0));
            }
        }
    }

    private void invalidateVtContainView(LongVideosModel model, boolean scaleForFilter) {
        float usableHeight;
        int realWidth;
        int realHeight;
        float gridScale = (((float) this.mVideoWidth) * 1.0f) / ((float) this.mVideoHeight);
        int usedScreenWidth = DensityUtil.getMetricsWidth(this);
        int usedScreenHeight = DensityUtil.getMetricsHeight(this);
        float usableWidth = (float) usedScreenWidth;
        if (scaleForFilter) {
            usableHeight = (float) ((usedScreenHeight - getVideoTopAndBottomSumViewHeight()) - DensityUtil.dip2px(45.0f));
        } else {
            usableHeight = (float) (usedScreenHeight - getVideoTopAndBottomSumViewHeight());
        }
        int marginT = 0;
        int marginB = 0;
        if (gridScale > usableWidth / usableHeight) {
            realWidth = (int) usableWidth;
            realHeight = (int) (((float) realWidth) / gridScale);
            marginB = (int) ((usableHeight - ((float) realHeight)) / 2.0f);
            marginT = marginB;
        } else {
            realHeight = (int) usableHeight;
            realWidth = (int) (((float) realHeight) * gridScale);
        }
        if (this.text_draw_rl != null) {
            int i;
            if (this.mIsSquare) {
                this.surfaceParams = (LayoutParams) this.mSurfaceView.getLayoutParams();
                int surfaceWidth = this.surfaceParams.width;
                int surfaceHeight = this.surfaceParams.height;
                if (surfaceWidth > surfaceHeight) {
                    usableWidth = (float) surfaceHeight;
                    marginT = 0;
                    marginB = 0;
                } else {
                    usableWidth = (float) surfaceWidth;
                    marginT = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                    marginB = (int) (((float) (surfaceHeight - surfaceWidth)) / 2.0f);
                }
                realWidth = (int) usableWidth;
                realHeight = (int) usableWidth;
            }
            realWidth += realWidth % 2;
            realHeight += realHeight % 2;
            marginT = (int) (((float) marginT) + getResources().getDimension(R.dimen._63dp));
            LayoutParams layoutParams = (LayoutParams) this.text_draw_rl.getLayoutParams();
            layoutParams.width = this.metricsWidth;
            layoutParams.height = realHeight;
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = marginT;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = marginB;
            this.text_draw_rl.setLayoutParams(layoutParams);
            LayoutParams lp = (LayoutParams) this.gold_line_view.getLayoutParams();
            lp.width = realWidth;
            this.gold_line_view.setLayoutParams(lp);
            LayoutParams lp2 = (LayoutParams) this.draw_text_view.getLayoutParams();
            lp2.width = realWidth;
            lp2.height = realHeight;
            this.draw_text_view.setLayoutParams(lp2);
            this.drawTextViewOffsetX = (this.metricsWidth - realWidth) / 2;
            this.draw_text_view.setViewWidth(realWidth);
            this.draw_text_view.setViewHeight(realHeight, model.getTextVerticalPos());
            VTContainerView vTContainerView = this.draw_text_view;
            if (realWidth > realHeight) {
                i = realHeight;
            } else {
                i = realWidth;
            }
            vTContainerView.setFontSizeType(i, model.getTextFontSizeType());
            createOrUpdateCloneTextModels(realHeight, realWidth);
        }
    }

    private void invalidateVtContainView(LongVideosModel model) {
        invalidateVtContainView(model, false);
    }

    private void createOrUpdateCloneTextModels(int viewHeight, int viewWidth) {
        if (!TextUtil.isValidate(this.cloneTextModels)) {
            this.cloneTextModels = new ArrayList();
            ArrayList<LongVideosModel> textModelList = this.mVideoAudioManager.getTextModelList();
            if (TextUtil.isValidate(textModelList)) {
                this.cloneTextModels.addAll(textModelList);
            } else {
                return;
            }
        }
        Iterator it = this.mVideoAudioManager.getTextModelList().iterator();
        while (it.hasNext()) {
            LongVideosModel model = (LongVideosModel) it.next();
            float HRatio = (1.0f * ((float) viewHeight)) / ((float) model.getVtHeight());
            model.setTextVerticalPos((int) (((float) model.getTextVerticalPos()) * HRatio));
            model.setVtHeight(viewHeight);
            model.setLineSpacing((int) (((float) model.getLetterSpacing()) * HRatio));
            float scaledFontSize = VTContainerView.getScaledFontSize(getApplicationContext(), model, viewHeight, viewWidth);
            model.setTextSize(scaledFontSize);
            int scaledStartY = VTContainerView.getScaledStartY(getApplicationContext(), model, viewHeight, viewWidth);
            model.setStartY(scaledStartY);
            LogUtil.d("slim", "scaledFontSize:" + scaledFontSize + "scaledStartY:" + scaledStartY);
        }
    }

    private void refreshPlayBtn(int w, int h, LongVideosModel longVideosModel) {
        if (h == -1 || w == -1) {
            int rotate = this.mFirstVideoBean.getRotation();
            int height = this.mFirstVideoBean.getVideoHeight();
            int width = this.mFirstVideoBean.getVideoWidth();
            if (rotate == 90 || rotate == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE) {
                int temp = width;
                width = height;
                height = temp;
            }
            initCurrentClipIv(width, height);
            initMissingFileHint();
            initPlayBtn(width, height);
            this.mVideoWidth = width;
            this.mVideoHeight = height;
            saveVideoWHToBitmapManager();
            return;
        }
        if ((((float) this.mOriginVideoWidth) * 1.0f) / ((float) this.mOriginVideoHeight) > (((float) w) * 1.0f) / ((float) h)) {
            this.mVideoWidth = (int) (((((float) this.mOriginVideoHeight) * 1.0f) * ((float) w)) / ((float) h));
            this.mVideoHeight = this.mOriginVideoHeight;
        } else if ((((float) this.mOriginVideoWidth) * 1.0f) / ((float) this.mOriginVideoHeight) < (((float) w) * 1.0f) / ((float) h)) {
            this.mVideoWidth = this.mOriginVideoWidth;
            this.mVideoHeight = (int) (((((float) this.mOriginVideoWidth) * 1.0f) * ((float) h)) / ((float) w));
        } else {
            this.mVideoHeight = this.mOriginVideoHeight;
            this.mVideoWidth = this.mOriginVideoWidth;
        }
        saveVideoWHToBitmapManager();
        initCurrentClipIv(this.mVideoWidth, this.mVideoHeight);
        initMissingFileHint();
        initPlayBtn(this.mVideoWidth, this.mVideoHeight);
    }

    private int[] getWHPercent(int position) {
        int[] ints_h_w = new int[2];
        switch (position) {
            case 1:
                ints_h_w[0] = 9;
                ints_h_w[1] = 16;
                break;
            case 2:
                ints_h_w[0] = 2;
                ints_h_w[1] = 3;
                break;
            case 3:
                ints_h_w[0] = 3;
                ints_h_w[1] = 4;
                break;
            case 4:
                ints_h_w[0] = 1;
                ints_h_w[1] = 1;
                break;
            case 5:
                ints_h_w[0] = 4;
                ints_h_w[1] = 3;
                break;
            case 6:
                ints_h_w[0] = 3;
                ints_h_w[1] = 2;
                break;
            case 7:
                ints_h_w[0] = 16;
                ints_h_w[1] = 9;
                break;
            default:
                ints_h_w[0] = -1;
                ints_h_w[1] = -1;
                break;
        }
        return ints_h_w;
    }

    public void showFilterEffectButton() {
        this.filter_detail_set_text.setVisibility(View.GONE);
        this.filter_custom_sk.setVisibility(View.GONE);
        this.filter_effect_set_bts_hs.setVisibility(View.VISIBLE);
    }

    public void showFilterEffectSeekBar() {
        this.filter_detail_set_text.setVisibility(View.VISIBLE);
        this.filter_custom_sk.setVisibility(View.VISIBLE);
        this.filter_effect_set_bts_hs.setVisibility(View.GONE);
    }

    private void editFilterIntensity(float progressFloat) {
        if (this.currentEffectType != FilterEffectManager.EffectType.STRENGTH) {
            this.otherValue = progressFloat;
        }
        float value = 0;
        float currentVaule;
        switch (this.currentEffectType) {
            case HIGHLIGHT:
                if (this.highLightFilter != null) {
                    value = (((progressFloat - 5.0f) / 5.0f) + 1.0f) / 2.0f;
                    this.highLightFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f), new PointF(0.5f, 0.5f - (0.02f * value)), new PointF(0.75f, 0.75f - (0.13f * value)), new PointF(1.0f, 1.0f - (0.23f * value))});
                    break;
                }
                break;
            case SKY:
                if (this.skyFilter != null) {
                    this.skyFilter.setOverPercent(progressFloat / 10.0f);
                    break;
                }
                break;
            case SHADOW:
                if (this.shadowFilter != null) {
                    value = (((progressFloat - 5.0f) / 5.0f) + 1.0f) / 2.0f;
                    this.shadowFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.14f * value), new PointF(0.25f, (0.13f * value) + 0.25f), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case FADED:
                if (this.fadeFilter != null) {
                    value = (((progressFloat - 5.0f) / 5.0f) + 1.0f) / 2.0f;
                    this.fadeFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, 0.27f * value), new PointF(0.25f, (0.09f * value) + 0.25f), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f - (0.04f * value)), new PointF(1.0f, 1.0f - (0.15f * value))});
                    break;
                }
                break;
            case TILT:
                if (this.tiltFilter != null) {
                    this.tiltFilter.setBlurSize((0.4f * progressFloat) + 1.0f);
                    break;
                }
                break;
            case SHARPEN:
                if (this.sharpenFilter != null) {
                    this.sharpenFilter.setSharpness(progressFloat * 0.26f);
                    break;
                }
                break;
            case GRAIN:
                if (this.grainFilter != null) {
                    this.grainFilter.setOverPercent(progressFloat / 10.0f);
                    break;
                }
                break;
            case TEMPERATURE:
                if (this.whiteBalanceFilter != null) {
                    value = progressFloat / 5.0f;
                    if (value < StaticLayoutUtil.DefaultSpacingadd) {
                        value = (500.0f * value) + 5000.0f;
                    } else {
                        value = (1500.0f * value) + 5000.0f;
                    }
                    this.whiteBalanceFilter.setTemperature(value);
                    break;
                }
                break;
            case SATURATION:
                if (this.saturationFilter != null) {
                    this.saturationFilter.setSaturation((0.6f * (progressFloat / 5.0f)) + 1.0f);
                    break;
                }
                break;
            case ROTATE:
                currentVaule = Float.parseFloat(this.decimalFormat.format((double) progressFloat));
                if (this.transLastRValue != currentVaule || this.lastDegree != this.currentDegree) {
                    this.lastDegree = this.currentDegree;
                    this.transLastRValue = currentVaule;
                    setTransformFilterValue(progressFloat);
                    break;
                }
                return;
            case CONTRAST:
                if (this.contrastFilter != null) {
                    value = (progressFloat / 5.0f) * 0.12f;
                    this.contrastFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.25f, 0.25f - value), new PointF(0.5f, 0.5f), new PointF(0.75f, 0.75f + value), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case HORIZONTAL:
                currentVaule = Float.parseFloat(this.decimalFormat.format((double) progressFloat));
                if (this.transLastHValue != currentVaule || this.lastDegree != this.currentDegree) {
                    this.lastDegree = this.currentDegree;
                    this.transLastHValue = currentVaule;
                    setTransformFilterValue(progressFloat);
                    break;
                }
                return;
            case VERTICAL:
                currentVaule = Float.parseFloat(this.decimalFormat.format((double) progressFloat));
                if (this.transLastVValue != currentVaule || this.lastDegree != this.currentDegree) {
                    this.lastDegree = this.currentDegree;
                    this.transLastVValue = currentVaule;
                    setTransformFilterValue(progressFloat);
                    break;
                }
                return;
            case EXPOSURE:
                LogUtil.d("progressFloat", "progressFloat : " + progressFloat);
                if (this.exposureFilter != null) {
                    this.mFilterEffectManager.setExposureFileCurrentValue((progressFloat / 5.0f) * 0.12f);
                    this.exposureFilter.setRgbCompositeControlPoints(new PointF[]{new PointF(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd), new PointF(0.5f, 0.5f + value), new PointF(1.0f, 1.0f)});
                    break;
                }
                break;
            case BEAUTIFY:
                if (this.mBilateralFilter != null) {
                    this.mBilateralFilter.setBlurWeightLocation(progressFloat / 10.0f);
                    break;
                }
                break;
            case VIGNETTE:
                if (this.movieFilter != null) {
                    this.movieFilter.setOverPercent(progressFloat / 10.0f);
                    break;
                }
                break;
            case LEAK:
                if (this.leakFilter != null) {
                    this.leakFilter.setOverPercent(progressFloat / 10.0f);
                    break;
                }
                break;
            case TINGE:
                if (this.whiteBalanceFilter != null) {
                    this.whiteBalanceFilter.setTint((progressFloat / 5.0f) * 40.0f);
                    break;
                }
                break;
            case DUST:
                if (this.dustFilter != null) {
                    this.dustFilter.setOverPercent(progressFloat / 10.0f);
                    break;
                }
                break;
            case PRISM:
                if (this.prismFilter != null) {
                    this.prismFilter.setStrength((progressFloat * 1.0f) / 10.0f);
                    break;
                }
                break;
            case STRENGTH:
                this.strengthValue = progressFloat / 10.0f;
                changeLongVideosModelStrengthValue(this.strengthValue);
                this.currentFilter.setIntensity(this.strengthValue);
                break;
        }
        setNormalFilter();
    }

    private void changeLongVideosModelStrengthValue(float strengthValue) {
        if (this.mDataType == 2 && this.mVideoAudioManager != null && getCurrentVideoModel() != null) {
            if (getCurrentVideoModel().getFilterState() == 1) {
                getCurrentVideoModel().setPrivateIntensity(strengthValue);
                return;
            }
            ArrayList<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
            if (TextUtil.isValidate(videosModelList)) {
                Iterator it = videosModelList.iterator();
                while (it.hasNext()) {
                    ((LongVideosModel) it.next()).setPublicIntensity(strengthValue);
                }
            }
        }
    }

    private void setTransformFilterValue(float progressFloat) {
        if (this.transformFilter != null) {
            float value;
            boolean hadCrop = this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(this.currentEffectType), FilterEffectManager.EffectType.CROP);
            float scale1 = (((float) this.mVideoFrames.mCropHeight) * 1.0f) / ((float) this.mVideoFrames.mCropWidth);
            Matrix matrix = new Matrix();
            if (this.currentEffectType == FilterEffectManager.EffectType.ROTATE) {
                this.currentRotateValue = progressFloat;
            }
            if (this.currentRotateValue != StaticLayoutUtil.DefaultSpacingadd) {
                double cMovieScale;
                value = (this.currentRotateValue / 5.0f) * 10.0f;
                double fRotate = Math.abs(((double) (value / 180.0f)) * 3.141592653589793d);
                if (scale1 < 1.0f) {
                    cMovieScale = (double) (1.0f / scale1);
                } else {
                    cMovieScale = (double) scale1;
                }
                float scale = (float) (Math.sin(1.5707963267948966d - fRotate) + (Math.sin(fRotate) * cMovieScale));
                matrix.postRotate(value);
                matrix.postScale(scale, scale);
            }
            float[] a = new float[9];
            matrix.getValues(a);
            float[] b = new float[]{a[0], a[1], a[2], StaticLayoutUtil.DefaultSpacingadd, a[3], a[4], a[5], StaticLayoutUtil.DefaultSpacingadd, a[6], a[7], a[8], StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 1.0f};
            Size size = null;
            value = progressFloat / 5.0f;
            float hValue = StaticLayoutUtil.DefaultSpacingadd;
            float vValue = StaticLayoutUtil.DefaultSpacingadd;
            if (this.currentEffectType == FilterEffectManager.EffectType.VERTICAL) {
                this.currentVvalue = value;
                this.currentHvalue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL).value[1] / 5.0f;
                if (this.currentDegree == 90.0f || this.currentDegree == 270.0f) {
                    this.currentVvalue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL).value[1] / 5.0f;
                    this.currentHvalue = value;
                }
            } else {
                if (this.currentEffectType == FilterEffectManager.EffectType.HORIZONTAL) {
                    this.currentHvalue = value;
                    this.currentVvalue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.VERTICAL).value[1] / 5.0f;
                    if (this.currentDegree == 90.0f || this.currentDegree == 270.0f) {
                        this.currentHvalue = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.HORIZONTAL).value[1] / 5.0f;
                        this.currentVvalue = value;
                    }
                }
            }
            boolean isDraftOrLocalVideo = 3 == this.mDataFrom || 1 == this.mDataFrom;
            if (!(this.currentVvalue == StaticLayoutUtil.DefaultSpacingadd && this.currentHvalue == StaticLayoutUtil.DefaultSpacingadd)) {
                Size size2;
                if (this.currentDegree == StaticLayoutUtil.DefaultSpacingadd || this.currentDegree == 180.0f) {
                    if (this.orientation != 0 && this.orientation != 180) {
                        hValue = this.currentHvalue / 10.0f;
                        vValue = this.currentVvalue / 10.0f;
                    } else if (isDraftOrLocalVideo) {
                        hValue = this.currentHvalue / 10.0f;
                        vValue = this.currentVvalue / 10.0f;
                    } else {
                        hValue = this.currentVvalue / 10.0f;
                        vValue = this.currentHvalue / 10.0f;
                    }
                    size2 = new Size(2.0f, 2.0f / ((((float) this.mVideoFrames.mCropWidth) * 1.0f) / ((float) this.mVideoFrames.mCropHeight)));
                } else {
                    if (this.orientation != 0 && this.orientation != 180) {
                        hValue = this.currentVvalue / 10.0f;
                        vValue = this.currentHvalue / 10.0f;
                    } else if (isDraftOrLocalVideo) {
                        hValue = this.currentVvalue / 10.0f;
                        vValue = this.currentHvalue / 10.0f;
                    } else {
                        hValue = this.currentHvalue / 10.0f;
                        vValue = this.currentVvalue / 10.0f;
                    }
                    size2 = new Size(2.0f, 2.0f / ((((float) this.mVideoFrames.mCropWidth) * 1.0f) / ((float) this.mVideoFrames.mCropHeight)));
                }
                boolean orientation90 = this.orientation == 90;
                boolean orientation270 = this.orientation == 270;
                if (this.currentDegree == 180.0f || this.currentDegree == 270.0f || orientation90) {
                    hValue = -hValue;
                }
                if (!((this.currentDegree != StaticLayoutUtil.DefaultSpacingadd && this.currentDegree != 90.0f && this.currentDegree != 180.0f) || isDraftOrLocalVideo || orientation270)) {
                    vValue = -vValue;
                }
            }
            if (size != null) {
                b[3] = (hValue / size.width) * 2.0f;
                b[7] = (vValue / size.height) * 2.0f;
                b[15] = (1.0f - Math.abs(hValue)) - Math.abs(vValue);
            }
            this.transformFilter.setTransform3D(b);
            this.transformFilter.setIgnoreAspectRatio(false);
            this.finalTransformArray = this.transformFilter.getTransform3D();
        }
    }

    private void changeRotateValue(float value) {
        this.currentDegree = value;
        this.transformFilter.setOrientation(value);
        int currentCropWidth = this.mVideoFrames.mCropWidth;
        this.mVideoFrames.mCropWidth = this.mVideoFrames.mCropHeight;
        this.mVideoFrames.mCropHeight = currentCropWidth;
        if (this.leakFilter != null) {
            this.leakFilter.setByteBuffer(ByteBufferUtils.changeBufferValueAfterRotate(this.leakFilter.getCurBuffer()));
        }
        editFilterIntensity(this.filter_custom_sk.getProgressFloat());
        fixDateFilter(true);
        fixMirrorOrientationByRotateValue(value);
        fixCropBottomView();
        fixGridViewSize(value, false);
    }

    private void fixCropBottomView() {
        if (this.gpuImageCropFilter != null && this.custom_crop_View != null) {
            if (this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.CROP)) {
                float[] value = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.CROP).value;
                if (value != null) {
                    value[0] = 8.0f - value[0];
                }
            }
        }
    }

    private void fixMirrorOrientationByRotateValue(float value) {
        LogUtil.d("fixMirrorOrientationByRotateValue", String.format("value : %s ", value));
        if (this.mFilterEffectManager.listAContainItem(this.mFilterEffectManager.getShowFilterEffectList(FilterEffectManager.EffectType.NONE), FilterEffectManager.EffectType.MIRROR)) {
            FilterEffectBean mirrorItem = this.mFilterEffectManager.getAssignedBeanByType(FilterEffectManager.EffectType.MIRROR);
            LogUtil.d("fixMirrorOrientationByRotateValue", "mirror : " + mirrorItem);
            float[] floats = mirrorItem.value;
            float percent = mirrorItem.selectPosition[0];
            float aFloat = floats[0];
            float result = percent / 100.0f;
            float orientation = StaticLayoutUtil.DefaultSpacingadd;
            LogUtil.d("fixMirrorOrientationByRotateValue", String.format("percent : %s , aFloat : %s , result : %s ", percent, aFloat, result));
            switch ((int) aFloat) {
                case 0:
                    orientation = 1.0f;
                    break;
                case 1:
                    orientation = 1.0f;
                    break;
                case 2:
                    orientation = 3.0f;
                    break;
                case 3:
                    orientation = 4.0f;
                    break;
                case 4:
                    orientation = 2.0f;
                    break;
            }
            if (orientation == 1.0f || orientation == 3.0f) {
                orientation -= 1.0f;
            } else {
                orientation -= 1.0f;
                result = 1.0f - result;
            }
            if (orientation < 1.0f) {
                orientation += 4.0f;
            }
            LogUtil.d("fixMirrorOrientationByRotateValue", String.format("after percent : %s , orientation : %s , result : %s ", percent, orientation, result));
            this.mGPUImageMirrorFilter.setMirrorStartAndOrientation(result, orientation, this.mCaptureOrientation);
            float resultValue = StaticLayoutUtil.DefaultSpacingadd;
            switch ((int) orientation) {
                case 1:
                    resultValue = 1.0f;
                    break;
                case 2:
                    resultValue = 4.0f;
                    break;
                case 3:
                    resultValue = 2.0f;
                    break;
                case 4:
                    resultValue = 3.0f;
                    break;
            }
            mirrorItem.value[0] = resultValue;
            mirrorItem.selectPosition[0] = 100.0f * result;
            LogUtil.d("fixMirrorOrientationByRotateValue", String.format("after mirrorItem.value[0] : %s , mirrorItem.selectPosition[0] : %s  ", mirrorItem.value[0], mirrorItem.selectPosition[0]));
        }
    }

    private void showCropView() {
        this.filter_effect_set_five.setVisibility(View.VISIBLE);
        this.filter_effect_set_six.setVisibility(View.VISIBLE);
        this.filter_effect_set_seven.setVisibility(View.VISIBLE);
        this.filter_effect_set_zero.setVisibility(View.VISIBLE);
        this.filter_effect_set_zero.setFilterEffectImage(R.drawable.icon_20_effect_new_off);
        this.filter_effect_set_zero.setFilterEffectText(getString(R.string.BUTTON_EFFECT_NONE));
        this.filter_effect_set_one.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_169);
        this.filter_effect_set_one.setFilterEffectText("16:9");
        this.filter_effect_set_two.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_32);
        this.filter_effect_set_two.setFilterEffectText("3:2");
        this.filter_effect_set_three.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_43);
        this.filter_effect_set_three.setFilterEffectText("4:3");
        this.filter_effect_set_four.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_11);
        this.filter_effect_set_four.setFilterEffectText("1:1");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.filter_effect_set_four.getLayoutParams();
        layoutParams.rightMargin = 0;
        this.filter_effect_set_four.setLayoutParams(layoutParams);
        this.filter_effect_set_five.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_34);
        this.filter_effect_set_five.setFilterEffectText("3:4");
        this.filter_effect_set_six.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_23);
        this.filter_effect_set_six.setFilterEffectText("2:3");
        this.filter_effect_set_seven.setFilterEffectImage(R.drawable.icon_20_effect_new_crop_916);
        this.filter_effect_set_seven.setFilterEffectText("9:16");
    }

    private int formatFloat(float value) {
        BigDecimal bigDecimal = BigDecimal.valueOf((double) value);
        return Math.round(value);
    }

    private float formatFloat1(float value) {
        return BigDecimal.valueOf((double) value).setScale(1, 4).floatValue();
    }

    private void setFilterEffectColor(float progress, FilterEffectSetRelativeLayout filterEffectSetRelativeLayout) {
        if (progress == StaticLayoutUtil.DefaultSpacingadd) {
            filterEffectSetRelativeLayout.setDateColor(getApplicationContext());
        } else {
            filterEffectSetRelativeLayout.setWhiteColor(getApplicationContext());
        }
    }

    private void setFilterEffectGone() {
        this.filter_effect_root_ll.setVisibility(View.VISIBLE);
        this.filter_rule_ll.setAnimation(this.translateAnimationHide);
        this.translateAnimationHide.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                filterEffectDetailIsShow = false;
                filter_rule_ll.setVisibility(View.GONE);
                filter_tab_choose_ll.setVisibility(4);
                filter_tab_ll.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.filter_tab_choose_ll.startAnimation(this.translateAnimationHide);
    }

    private void initRulerViewStub() {
        if (this.filter_ruler_view_stub != null) {
            initRulerView(this.filter_ruler_view_stub.inflate());
            this.filter_ruler_view_stub = null;
        }
    }

    private void initCropAllViewStub() {
        if (this.filter_crop_ccav_vs != null) {
            this.filter_crop_ccav = this.filter_crop_ccav_vs.inflate().findViewById(R.id.filter_crop_ccav);
            this.filter_crop_ccav_vs = null;
        }
    }

    private void initMirrorSeekViewStub() {
        if (this.mirror_seek_bar_vs != null) {
            this.mirror_seek_bar = this.mirror_seek_bar_vs.inflate().findViewById(R.id.mirror_seek_bar);
            this.mirror_seek_bar_vs = null;
        }
    }

    private void initRulerView(android.view.View ruler_inflate) {
        this.filter_effect_set_bts_hs = ruler_inflate.findViewById(R.id.filter_effect_set_bts_hs);
        this.filter_tab_choose_ll = ruler_inflate.findViewById(R.id.filter_tab_choose_ll);
        this.filter_tab_choose_cancle = ruler_inflate.findViewById(R.id.filter_tab_choose_cancle);
        this.filter_tab_choose_confirm = ruler_inflate.findViewById(R.id.filter_tab_choose_confirm);
        this.filter_custom_sk = ruler_inflate.findViewById(R.id.filter_custom_sk);
        this.filter_detail_set_text = ruler_inflate.findViewById(R.id.filter_detail_set_text);
        this.filter_effect_set_zero = ruler_inflate.findViewById(R.id.filter_effect_set_zero);
        this.filter_effect_set_one = ruler_inflate.findViewById(R.id.filter_effect_set_one);
        this.filter_effect_set_two = ruler_inflate.findViewById(R.id.filter_effect_set_two);
        this.filter_effect_set_three = ruler_inflate.findViewById(R.id.filter_effect_set_three);
        this.filter_effect_set_four = ruler_inflate.findViewById(R.id.filter_effect_set_four);
        this.filter_effect_set_five = ruler_inflate.findViewById(R.id.filter_effect_set_five);
        this.filter_effect_set_six = ruler_inflate.findViewById(R.id.filter_effect_set_six);
        this.filter_effect_set_seven = ruler_inflate.findViewById(R.id.filter_effect_set_seven);
        if (this.mFilterEffectDetailSets.size() == 0) {
            this.mFilterEffectDetailSets.add(this.filter_effect_set_zero);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_one);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_two);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_three);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_four);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_five);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_six);
            this.mFilterEffectDetailSets.add(this.filter_effect_set_seven);
        }
        int padding = (int) (((float) this.metricsWidth) * 0.1f);
        this.filter_custom_sk.setPadding(padding, 0, padding, 0);
        this.filter_tab_choose_cancle.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
        this.filter_tab_choose_confirm.setOnTouchListener(ColorFilterUtil.TouchFocusChange(false));
        this.filter_tab_choose_cancle.setOnClickListener(this.mOnAdditionChangeListener);
        this.filter_tab_choose_confirm.setOnClickListener(this.mOnAdditionChangeListener);
    }

    private void initMissingFootageChildViews() {
        if (this.video_edit_missing_footage_fl != null && this.mVideoAudioManager != null) {
            this.videoEditMissingFootageIvList.clear();
            this.video_edit_missing_footage_fl.removeAllViews();
            Iterator it = this.mVideoAudioManager.getVideosModelList().iterator();
            while (it.hasNext()) {
                if (!((LongVideosModel) it.next()).isVideoFileExist()) {
                    ImageView iv = getMissingFootageHintIv();
                    this.videoEditMissingFootageIvList.add(iv);
                    this.video_edit_missing_footage_fl.addView(iv);
                }
            }
        }
    }

    private ImageView getMissingFootageHintIv() {
        ImageView iv = new ImageView(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DensityUtil.dip2px(20.0f), DensityUtil.dip2px(20.0f));
        params.gravity = 16;
        iv.setLayoutParams(params);
        iv.setImageResource(R.drawable.icon_20_warning);
        TintColorUtil.tintDrawable(iv, Color.parseColor("#ffffff"));
        iv.setVisibility(View.GONE);
        return iv;
    }

    public void showVideoEditView() {
        initVideoEditLayout();
        if (this.video_edit_parent_ll == null || this.video_edit_parent_ll.getVisibility() != View.VISIBLE || this.mVideoEditHelper == null || this.mVideoEditHelper.isMusicEdit()) {
            showVideoTimeSlideBar();
            if (this.filter_tab_choose_ll != null) {
                this.filter_tab_choose_ll.setVisibility(View.GONE);
            }
            if (this.mVideoEditSwitchTabsHolder != null) {
                this.mVideoEditSwitchTabsHolder.onSwitchTabClick(false);
            }
            onVideoEditTabClick();
            refreshVideoEditView();
        }
    }

    public void refreshVideoEditView() {
        initVideoEditLayout();
        List<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
        LogUtil.d("videosModelList : " + videosModelList.toString());
        if (this.mVideoEditHelper == null) {
            this.mVideoEditHelper = new VideoEditHelper(getActivity(), this.video_edit_image_rv, this.video_edit_time_rv, this.video_edit_audio_rv, this.video_edit_text_rv, this.video_edit_video_duration_tv, this.video_edit_image_volume_line, this.video_edit_image_volume_touch, this.leftTouchView, this.rightTouchView, this.video_edit_audio_volume_line, this.video_edit_audio_volume_touch, this.metricsWidth, this.mVideoAudioManager, this.video_edit_mute_indicator, this.video_edit_mute_indicator_cover, this.video_edit_audio_mute_indicator, this.video_edit_image_mute_tv, this.video_edit_audio_mute_tv, this.video_edit_missing_footage_fl, this);
            initMissingFootageChildViews();
            this.mVideoEditHelper.setMissingFootageIvs(this.videoEditMissingFootageIvList);
        }
        this.mVideoEditHelper.refreshVideosView(videosModelList);
        refreshLineView();
    }

    public void setVideoSlideBarWidthMargin(int width, int marginLeft) {
        if (this.video_time_slide_bar != null && this.video_time_slide_bar.getVisibility() == View.VISIBLE) {
            LogUtil.d(TAG, String.format("setVideoSlideBarWidthMargin width : %s , marginLeft : %s ", width, marginLeft));
            LayoutParams layoutParams = (LayoutParams) this.video_time_slide_bar.getLayoutParams();
            if (width != layoutParams.width || layoutParams.leftMargin != marginLeft) {
                layoutParams.width = width;
                layoutParams.leftMargin = marginLeft;
                this.video_time_slide_bar.setLayoutParams(layoutParams);
            }
        }
    }

    public void setVideoSeekTo(float percent) {
        if (!this.mSurfaceView.getLongVideoPlayState()) {
            this.mSurfaceView.setVideoPauseOrResume(true);
        }
        if (percent < StaticLayoutUtil.DefaultSpacingadd) {
            percent = StaticLayoutUtil.DefaultSpacingadd;
        }
        if (percent > 1.0f) {
            percent = 1.0f;
        }
        if (shouldExcuteSeekTo() || percent == StaticLayoutUtil.DefaultSpacingadd || percent == 1.0f) {
            this.lastSeekTime = System.currentTimeMillis();
            long playlistTimeUs = (long) ((this.mVideoEditHelper.getSumTime() * percent) * 1000.0f);
            this.currentPlayTimeUs = playlistTimeUs;
            this.mSurfaceView.setPlaylistTimeUs(playlistTimeUs);
            this.mSurfaceView.setLongVideoSeekTo(percent);
        }
    }

    public void callSetVideoPause() {
        callVideoPause();
    }

    private void callVideoPause() {
        this.player_rl.changePlayState(true);
        if (this.mAudioTrimLayout != null && this.mAudioTrimLayout.getVisibility() == View.VISIBLE) {
            this.mAudioTrimLayout.changePlayState(true);
        }
        if (!this.mSurfaceView.getLongVideoPlayState()) {
            setVideoStartPause(true);
        }
    }

    private void callVideoPlay() {
        this.player_rl.changePlayState(false);
        if (this.mAudioTrimLayout != null && this.mAudioTrimLayout.getVisibility() == View.VISIBLE) {
            this.mAudioTrimLayout.changePlayState(false);
        }
        if (this.mSurfaceView.getLongVideoPlayState()) {
            setVideoStartPause(false);
        }
    }

    public void setSplitBtnVisible(boolean visible, boolean enable, boolean isEditMusic) {
        this.canVideoMuteIndicatorShow = visible;
        if (isEditMusic) {
            checkMuteIndicatorVisibility();
        }
        if (visible) {
            showVideoSplitButton(enable);
        } else {
            showVideoAddButton();
        }
    }

    private void checkMuteIndicatorVisibility() {
        if (this.mVideoAudioManager != null && this.video_edit_mute_indicator != null) {
            if (this.canVideoMuteIndicatorShow) {
                this.video_edit_mute_indicator.setVisibility(View.VISIBLE);
                this.video_edit_mute_indicator_cover.setVisibility(View.VISIBLE);
                return;
            }
            this.video_edit_mute_indicator.setVisibility(View.GONE);
            this.video_edit_mute_indicator_cover.setVisibility(View.GONE);
        }
    }

    public void onVideoSelectAreaShownOrHide(boolean isShow, boolean isOnlyOne, LongVideosModel longVideosModel) {
        boolean showEffects = isShow;
        boolean showRemove = isShow;
        if (longVideosModel == null || longVideosModel.isBlackModelType()) {
            showEffects = false;
        }
        if (isOnlyOne) {
            showRemove = false;
        }
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.showOptButton(showEffects, showRemove);
        }
        setCenterLineSelectedStatus(isShow);
    }

    private void setCenterLineSelectedStatus(boolean isSelected) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.filter_tab_center_area.getLayoutParams();
        if (isSelected) {
            layoutParams.height = DensityUtil.dip2px(52.0f);
            layoutParams.bottomMargin = DensityUtil.dip2px(76.0f);
        } else {
            layoutParams.height = DensityUtil.dip2px(48.0f);
            layoutParams.bottomMargin = DensityUtil.dip2px(78.0f);
        }
        this.filter_tab_center_area.setLayoutParams(layoutParams);
    }

    public void onAddVideosClick(int position) {
        callVideoPause();
        this.mCurInsertPosition = position;
        hideSaveAndBack();
        showVideoEditAddLayout();
    }

    public void callVideoChange(long needPlayTimeUs) {
        if (needPlayTimeUs < 0) {
            needPlayTimeUs = this.currentPlayTimeUs;
        }
        this.mSurfaceView.changePlaylist(this.mVideoAudioManager.getVideosModelList(), getBgmList(), needPlayTimeUs, this.mVideoAudioManager.getMediaMute());
    }

    public void callVideoChangeAfterSplit(long needPlayTimeUs) {
        if (needPlayTimeUs < 0) {
            needPlayTimeUs = this.currentPlayTimeUs;
        }
        this.mSurfaceView.changePlaylist(this.mVideoAudioManager.getVideosModelList(), getBgmList(), needPlayTimeUs, this.mVideoAudioManager.getMediaMute());
    }

    public void callVideoChangeAfterSlide(long needPlayTimeUs, LongVideosModel changedModel) {
        if (needPlayTimeUs < 0) {
            needPlayTimeUs = this.currentPlayTimeUs;
        }
        this.mSurfaceView.changePlaylist(this.mVideoAudioManager.getVideosModelList(), getBgmList(), needPlayTimeUs, this.mVideoAudioManager.getMediaMute());
        this.canVideoOptDialogShow = true;
        changedModel.judgeAndStartProxyVideo();
    }

    private ArrayList<LongVideosModel> getBgmList() {
        return this.mVideoAudioManager.getMusicModelList();
    }

    public void onVideoEditSaveToDraft() {
        setUndoData(1);
    }

    public void callVideoEditSwitchTabsVisibility(boolean visible) {
        if (this.mVideoEditSwitchTabsHolder != null) {
            this.mVideoEditSwitchTabsHolder.setVisibility(visible ? 0 : 8);
        }
    }

    public void onVideoEditTextClick(LongVideosModel textVideoModel) {
        this.mCurrentTextLongVideoModel = textVideoModel;
        this.mTextContent = this.mCurrentTextLongVideoModel.getTextContent();
        toTextInputActivity(this.mTextContent);
    }

    public void refreshCuTextModel(LongVideosModel textVideoModel, boolean canAdd) {
        if (!this.isAfterInsertBlackAddText) {
            if (this.mCurrentTextLongVideoModel != textVideoModel) {
                this.mCurrentTextLongVideoModel = textVideoModel;
                refreshEditAddTextOpt();
            }
            if (this.mVideoEditSwitchTabsHolder != null) {
                this.mVideoEditSwitchTabsHolder.setAddTextCanClick(canAdd);
            }
        }
    }

    public void refreshBottomCoverViewVisibility(boolean isVisible) {
        if (isVisible) {
            showBottomCoverView();
        } else {
            hideBottomCoverView();
        }
    }

    public void onShowSlideText() {
        LogUtil.d(TAG, String.format("SlideTextSwitch   onShowSlideText"));
        this.video_edit_remove_parent.clearAnimation();
        this.text_move_left_right.clearAnimation();
        startAnimationForRemoveParent(this.video_edit_remove_parent.getAlpha(), StaticLayoutUtil.DefaultSpacingadd);
    }

    public void onHideSlideText() {
        LogUtil.d(TAG, String.format("SlideTextSwitch   onHideSlideText"));
        this.video_edit_remove_parent.clearAnimation();
        this.text_move_left_right.clearAnimation();
        startAnimationForRemoveParent(this.video_edit_remove_parent.getAlpha(), 1.0f);
    }

    public void openVideoMusicStore() {
        callVideoPause();
        showVideoMusicDialog();
    }

    public void videoTimeChange() {
        refreshLineView();
        refreshTitleAlertOutTime();
    }

    public void showVideoAddMusicTv() {
        if (this.video_edit_add_music_tv.getVisibility() != View.VISIBLE) {
            this.video_edit_add_music_tv.setVisibility(View.VISIBLE);
        }
    }

    public void hideVideoAddMusicTv() {
        if (this.video_edit_add_music_tv.getVisibility() == View.VISIBLE) {
            this.video_edit_add_music_tv.setVisibility(View.GONE);
        }
    }

    public void refreshVideoAddMusicTv(int leftMargin, int height) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.video_edit_add_music_tv.getLayoutParams();
        layoutParams.leftMargin = leftMargin;
        if (layoutParams.height != height) {
            layoutParams.height = height;
        }
        this.video_edit_add_music_tv.setLayoutParams(layoutParams);
    }

    public void refreshVideoAddMusicTvVisible(int visible) {
        this.video_edit_add_music_tv.setVisibility(visible);
    }

    public int getVideoAddMusicTvWidth() {
        int width = this.video_edit_add_music_tv.getWidth();
        if (width <= 0) {
            return (int) this.video_edit_add_music_tv.getPaint().measureText(getString(R.string.BUTTON_AUDIO_TAP_TO_ADD_MUSIC));
        }
        return width;
    }

    public void onAudioMuteClick(boolean mute, boolean isEditMusic) {
        if (isEditMusic) {
            if (TextUtil.isValidate(this.mVideoAudioManager.getMusicModelList())) {
                if (mute) {
                    this.video_edit_audio_mute_tv.setVisibility(View.VISIBLE);
                } else {
                    this.video_edit_audio_mute_tv.setVisibility(View.GONE);
                }
            }
            callVideoChange(this.currentPlayTimeUs);
            return;
        }
        this.video_edit_audio_mute_tv.setVisibility(View.GONE);
    }

    public void onVideoMuteClick(boolean mute, boolean isEditMusic) {
        if (isEditMusic) {
            if (mute) {
                if (this.canVideoMuteIndicatorShow) {
                    this.video_edit_mute_indicator.setVisibility(View.VISIBLE);
                    this.video_edit_mute_indicator_cover.setVisibility(View.VISIBLE);
                } else {
                    this.video_edit_mute_indicator.setVisibility(View.GONE);
                    this.video_edit_mute_indicator_cover.setVisibility(View.GONE);
                }
                this.video_edit_image_mute_tv.setVisibility(View.VISIBLE);
            } else {
                this.video_edit_image_mute_tv.setVisibility(View.GONE);
                if (this.canVideoMuteIndicatorShow) {
                    this.video_edit_mute_indicator.setVisibility(View.VISIBLE);
                    this.video_edit_mute_indicator_cover.setVisibility(View.VISIBLE);
                } else {
                    this.video_edit_mute_indicator.setVisibility(View.GONE);
                    this.video_edit_mute_indicator_cover.setVisibility(View.GONE);
                }
            }
            callVideoChange(this.currentPlayTimeUs);
            return;
        }
        this.video_edit_mute_indicator.setVisibility(View.GONE);
        this.video_edit_mute_indicator_cover.setVisibility(View.GONE);
        this.video_edit_image_mute_tv.setVisibility(View.GONE);
    }

    public void refreshAudioEditButton() {
        if (this.mVideoEditHelper != null) {
            LongVideosModel curSelectAudioModel = this.mVideoEditHelper.getCurSelectAudioModel();
            if (curSelectAudioModel == null) {
                this.mVideoEditSwitchTabsHolder.setMusicTopClickable(false);
            } else if (curSelectAudioModel.isJustSeeForAudio()) {
                this.mVideoEditSwitchTabsHolder.setMusicTopOnlyRemoveEnable();
            } else {
                this.mVideoEditSwitchTabsHolder.setMusicTopClickable(true);
            }
        }
    }

    public void callAudioChange() {
        changeVideoAudio();
    }

    public void animationJumpToMusicEdit() {
        selectMusicFromEditPointAnim(new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                onMusicTabClick(true);
            }
        });
    }

    public boolean checkAddTextVisible(float width) {
        int addMusicTvWidth = getVideoAddMusicTvWidth();
        if (this.video_edit_add_music_tv.getVisibility() != View.VISIBLE) {
            if (width <= ((float) (DensityUtil.dip2px(25.0f) + addMusicTvWidth))) {
                return false;
            }
            this.video_edit_add_music_tv.setVisibility(View.VISIBLE);
            if (this.mVideoEditHelper == null) {
                return true;
            }
            this.mVideoEditHelper.refreshAddMusicView();
            return true;
        } else if (width >= ((float) (DensityUtil.dip2px(25.0f) + addMusicTvWidth))) {
            return true;
        } else {
            this.video_edit_add_music_tv.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public boolean onNeedAddOneMissingFootageIv() {
        if (this.video_edit_missing_footage_fl == null || this.videoEditMissingFootageIvList == null || this.mVideoEditHelper == null) {
            return false;
        }
        ImageView iv = getMissingFootageHintIv();
        this.videoEditMissingFootageIvList.add(iv);
        this.video_edit_missing_footage_fl.addView(iv);
        this.mVideoEditHelper.setMissingFootageIvs(this.videoEditMissingFootageIvList);
        return true;
    }

    public void onAddMusicClick(int position) {
        this.addMusicPosition = position;
        callVideoPause();
        showVideoMusicDialog();
    }

    public void addVideoAddMusicTvLeftMargin(int changeWidth) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.video_edit_add_music_tv.getLayoutParams();
        layoutParams.leftMargin = ((DensityUtil.mScreenWidth / 2) + DensityUtil.dip2px(15.0f)) + changeWidth;
        this.video_edit_add_music_tv.setLayoutParams(layoutParams);
    }

    public void onVideoModelChange(LongVideosModel model) {
        if (this.mVideoAudioManager != null && model != null && TextUtil.isValidate(this.mVideoAudioManager.getVideosModelList()) && !model.equals(getCurrentVideoModel())) {
            this.mVideoAudioManager.setCurrentVideoModel(model);
            if (getCurrentVideoModel() == null) {
                return;
            }
            if (getCurrentVideoModel().isVideoFileExist()) {
                hideFootageMissingHint();
                if (getCurrentVideoModel().getFilterState() == 1) {
                    showCurrentClipIv();
                    resetFilterChoosePositionByFilterName(getCurrentVideoModel().getPrivateFilterName());
                    return;
                }
                hideCurrentClipIv();
                resetFilterChoosePositionByFilterName(getCurrentVideoModel().getPublicFilterName());
                return;
            }
            hideCurrentClipIv();
            showFootageMissingHint();
        }
    }

    private void resetFilterChoosePositionByFilterName(String filterName) {
        if (TextUtil.isValidate(this.mFilterInfos)) {
            if ("".equals(filterName)) {
                filterName = NONE_FILTER;
            }
            int i = 0;
            while (i < this.mFilterInfos.size()) {
                if (this.mFilterInfos.get(i).name_en != null && this.mFilterInfos.get(i).name_en.equals(filterName)) {
                    this.filterChoosePosition = i;
                }
                i++;
            }
        }
    }

    private void startAnimationForRemoveParent(float startValue, float endValue) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue);
        valueAnimator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();
            video_edit_remove_parent.setAlpha(value);
            text_move_left_right.setAlpha(1.0f - value);
        });
        valueAnimator.setDuration(100);
        valueAnimator.start();
    }

    public void hideVideoEditView() {
        if (this.video_edit_parent_ll != null) {
            this.video_edit_parent_ll.setVisibility(View.GONE);
        }
        hideEditCenterLine();
        if (this.video_edit_remove_parent != null) {
            this.video_edit_remove_parent.setVisibility(View.GONE);
        }
        hideVideoTimeSlideBar();
        if (this.video_edit_text_rv != null) {
            this.video_edit_text_rv.setVisibility(View.GONE);
        }
    }

    private void hideEditCenterLine() {
        this.filter_tab_center_line_parent.setVisibility(View.GONE);
    }

    private void hideVideoTimeSlideBar() {
        this.video_time_slide_bar.setVisibility(View.GONE);
        this.video_time_slide_bar_backgroud_view.setVisibility(View.GONE);
    }

    public void initVideoOrderView() {
        initVideoOrderLayout();
        if (this.mVideoEditOrderAdapter == null) {
            List<LongVideosModel> videosModelList = new ArrayList();
            videosModelList.addAll(this.mVideoAudioManager.getVideosModelList());
            LogUtil.d("videosModelList : " + videosModelList.toString());
            this.mVideoEditOrderAdapter = new VideoEditOrderAdapter(0, videosModelList, this.mVideoAudioManager);
            this.mVideoEditOrderLayoutManager = new VideoEditOrderLayoutManager(this, 0, false, this.metricsWidth, DensityUtil.dip2px(44.0f), videosModelList.size(), DensityUtil.dip2px(5.0f));
            this.video_order_rv.setLayoutManager(this.mVideoEditOrderLayoutManager);
            this.video_order_rv.setAdapter(this.mVideoEditOrderAdapter);
            this.video_order_rv.addItemDecoration(new VideoEditOrderItemDecoration());
            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(this.mVideoEditOrderAdapter);
            itemDragAndSwipeCallback.setMoveThreshold(1.0f);
            itemDragAndSwipeCallback.setDragMoveFlags(12);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(this.video_order_rv);
            this.mVideoEditOrderAdapter.enableDragItem(itemTouchHelper, R.id.video_order_item_iv, true);
            this.mVideoEditOrderAdapter.setOnItemDragListener(this);
        }
    }

    public void showVideoOrderView() {
        if (this.video_order_parent_rl != null) {
            List<LongVideosModel> videosModelList = this.mVideoAudioManager.getVideosModelList();
            this.mVideoEditOrderLayoutManager.updateChange(videosModelList.size());
            List<LongVideosModel> adapterList = this.mVideoEditOrderAdapter.getData();
            adapterList.clear();
            adapterList.addAll(videosModelList);
            this.mVideoEditOrderAdapter.notifyDataSetChanged();
            final int dip2px = DensityUtil.dip2px(140.0f);
            ViewCompat.setTranslationY(this.video_order_parent_rl, (float) dip2px);
            this.video_order_parent_rl.setVisibility(View.VISIBLE);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(100);
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    ViewCompat.setTranslationY(video_order_parent_rl, ((float) dip2px) * (1.0f - value));
                    if (mVideoEditSwitchTabsHolder != null) {
                        mVideoEditSwitchTabsHolder.setAlpha(1.0f - value);
                    }
                }
            });
            valueAnimator.addListener(new AnimatorEndListener() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mVideoEditSwitchTabsHolder != null) {
                        mVideoEditSwitchTabsHolder.setVisibility(View.GONE);
                    }
                }
            });
            valueAnimator.start();
            hideSaveAndBack();
            hideVideoEditView();
        }
    }

    public void removeOneVideoView(int index) {
    }

    public void addVideoViewChild() {
    }

    public void initVideoEditEffectsView() {
        if (this.video_edit_effects_vs != null) {
            this.mVideoEditEffectsLayout = (VideoEditEffectsLayout) this.video_edit_effects_vs.inflate();
            this.video_edit_effects_vs = null;
            int position = 0;
            if (this.mVideoEditHelper != null) {
                position = getCurVideoEditModuleViewModelPosition();
            }
            this.mVideoEditEffectsLayout.setThisVideoModel((LongVideosModel) this.mVideoAudioManager.getVideosModelList().get(position), null);
            this.mVideoEditEffectsLayout.setCancelOrConfirmClickListener(this);
            this.mVideoEditEffectsLayout.setChildrenClickListener(this);
            this.mVideoEditEffectsLayout.setOnSelectItemChange(new HorizontalLoopView.OnSelectItemChange() {
                public void onSelect(int pos) {
                }
            });
            this.mVideoEditEffectsLayout.setBrightnessSeekBarListener(new CustomSeekBar.OnCustomProgressChangeListener() {
                public void onProgressChanged(float progressFloat) {
                    float realFloat = (10.0f * progressFloat) - 5.0f;
                    LongVideosModel model = (LongVideosModel) mVideoAudioManager.getVideosModelList().get(getCurVideoEditModuleViewModelPosition());
                    if (realFloat != model.getRealVideoExposure()) {
                        model.setVideoExposure(realFloat);
                        setVideoFilter(false);
                    }
                }

                public void onProgressUp(float progressFloat) {
                }

                public void onVolumeTouchUp(float progressFloat) {
                }

                public void onVolumeTouchDown() {
                }
            });
        }
    }

    public void showVideoEditEffectsView() {
        this.mVideoEditEffectsLayout.setVisibility(View.VISIBLE);
        callVideoPause();
        doSurfaceAndTitleBarAnim(false);
    }

    public void hideVideoEditEffectsView() {
        this.mVideoEditEffectsLayout.setVisibility(View.GONE);
        doSurfaceAndTitleBarAnim(true);
    }

    public void initAudioTrimView() {
        if (this.audio_trim_vs != null) {
            this.mAudioTrimLayout = (AudioTrimLayout) this.audio_trim_vs.inflate();
            this.mAudioTrimLayout.setVideoEditHelper(this.mVideoEditHelper);
            this.mAudioTrimLayout.setVideoAudioManager(this.mVideoAudioManager);
            this.mAudioTrimLayout.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                }
            });
            this.mAudioTrimLayout.setOnTrimButtonClick(new AudioTrimLayout.OnTrimButtonClick() {
                public void onTrimCancel() {
                    trimCancelClick();
                }

                public void onTrimConfirm() {
                    trimConfirmClick();
                }

                public void onTrimPlayClick(boolean play, LongVideosModel audioModel) {
                    if (play) {
                        changeVideoAudio(audioModel.getAudioStartTime());
                        mSurfaceView.setLongVideoSeekTo(audioModel.getAudioStartTime() * 1000);
                        callVideoPlay();
                        return;
                    }
                    callVideoPause();
                }
            });
            AnimationUtil.translateAnimate(this.mAudioTrimLayout, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(160.0f), 0, null);
            this.audio_trim_vs = null;
        }
    }

    private void trimCancelClick() {
        this.mPresenter.hideAudioTrimView();
        changeVideoAudio(this.currentPlayTimeUs);
    }

    private void trimConfirmClick() {
        this.mPresenter.hideAudioTrimView();
        if (this.mVideoEditHelper != null) {
            this.mVideoEditHelper.refreshAudioView();
        }
        changeVideoAudio(this.currentPlayTimeUs);
        setUndoData(1);
    }

    public void showAudioTrimView() {
        if (this.mAudioTrimLayout.getVisibility() != View.VISIBLE && this.mAudioTrimLayout.resetUI()) {
            this.mVideoAudioManager.getWaveformCacheUtils().setCanRunnableRun(true);
            this.mAudioTrimLayout.setVisibility(View.VISIBLE);
            callVideoPause();
            doAudioTrimAnim(true);
            hideVideoEditView();
        }
    }

    public void hideAudioTrimView() {
        if (this.mAudioTrimLayout.getVisibility() == View.VISIBLE) {
            this.mVideoAudioManager.getWaveformCacheUtils().setCanRunnableRun(false);
            doAudioTrimAnim(false);
            changeVideoEditVisibleForMusicTab();
        }
    }

    private void doAudioTrimAnim(boolean show) {
        if (show) {
            this.mIvBack.setEnabled(false);
            this.mTvSave.setEnabled(false);
            this.surface_click_view.setVisibility(View.VISIBLE);
            AnimationUtil.alphaAnimate(this.mIvBack, StaticLayoutUtil.DefaultSpacingadd, 100, null);
            AnimationUtil.alphaAnimate(this.mTvSave, StaticLayoutUtil.DefaultSpacingadd, 100, null);
            AnimationUtil.alphaAnimate(this.player_rl, StaticLayoutUtil.DefaultSpacingadd, 100, null);
            AnimationUtil.alphaAnimate(this.video_edit_remove_parent, StaticLayoutUtil.DefaultSpacingadd, 100, null);
            AnimationUtil.translateAnimate(this.mAudioTrimLayout, StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 100, null);
            return;
        }
        this.mIvBack.setEnabled(true);
        this.mTvSave.setEnabled(true);
        this.surface_click_view.setVisibility(View.GONE);
        AnimationUtil.alphaAnimate(this.mIvBack, 1.0f, 100, null);
        AnimationUtil.alphaAnimate(this.mTvSave, 1.0f, 100, null);
        AnimationUtil.alphaAnimate(this.player_rl, 1.0f, 100, null);
        AnimationUtil.alphaAnimate(this.video_edit_remove_parent, 1.0f, 100, null);
        AnimationUtil.translateAnimate(this.mAudioTrimLayout, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(160.0f), 100, new AnimatorEndListener() {
            public void onAnimationEnd(Animator animation) {
                mAudioTrimLayout.setVisibility(View.GONE);
            }
        });
    }

    private void doSurfaceAndTitleBarAnim(boolean reverse) {
        if (reverse) {
            this.mIvBack.setEnabled(true);
            this.mTvSave.setEnabled(true);
            AnimationUtil.translateAnimate(this.mSurfaceView, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.translateAnimate(this.below_surface, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.translateAnimate(this.surface_click_view, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.translateAnimate(this.player_rl, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.translateAnimate(this.title_bg_view, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.translateAnimate(this.filter_current_clip_iv, StaticLayoutUtil.DefaultSpacingadd, (float) DensityUtil.dip2px(StaticLayoutUtil.DefaultSpacingadd), 100, null);
            AnimationUtil.alphaAnimate(this.mIvBack, 1.0f, 100, null);
            AnimationUtil.alphaAnimate(this.mTvSave, 1.0f, 100, null);
            this.surface_click_view.setVisibility(View.GONE);
            return;
        }
        this.mIvBack.setEnabled(false);
        this.mTvSave.setEnabled(false);
        AnimationUtil.translateAnimate(this.mSurfaceView, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.translateAnimate(this.below_surface, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.translateAnimate(this.surface_click_view, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.translateAnimate(this.player_rl, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.translateAnimate(this.title_bg_view, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.translateAnimate(this.filter_current_clip_iv, StaticLayoutUtil.DefaultSpacingadd, (float) (-DensityUtil.dip2px(30.0f)), 100, null);
        AnimationUtil.alphaAnimate(this.mIvBack, StaticLayoutUtil.DefaultSpacingadd, 100, null);
        AnimationUtil.alphaAnimate(this.mTvSave, StaticLayoutUtil.DefaultSpacingadd, 100, null);
        this.surface_click_view.setVisibility(View.VISIBLE);
    }

    public void setPresenter(FilterActivityContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void onItemDragStart(ViewHolder viewHolder, int pos) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
        lp.topMargin = -DensityUtil.dip2px(5.0f);
        viewHolder.itemView.setLayoutParams(lp);
    }

    public void onItemDragMoving(ViewHolder source, int from, ViewHolder target, int to) {
    }

    public void onItemDragEnd(ViewHolder viewHolder, int pos) {
        this.lastOrderDragEndPos = pos;
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
        lp.topMargin = 0;
        viewHolder.itemView.setLayoutParams(lp);
    }

    public void onEventMainThread(ProxyProgressEvent event) {
        dealProxyProgressEvent(event);
    }

    private void dealProxyProgressEvent(ProxyProgressEvent event) {
        setVideoOptProgress(event.getProxyProgressInt(), event.isProxySuccess());
//        if (this.mVideoOptDialog != null) {
//            this.mVideoOptDialog.setModel(event.getModel());
//        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DestroyActivityEvent event) {
        _DestroyActivityEvent(event);
    }

    private void _DestroyActivityEvent(DestroyActivityEvent event) {
        long videosSumTime;
        if (DestroyActivityEvent.ActivityState.LONGVIDEOLOCAL == event.state || DestroyActivityEvent.ActivityState.LONGVIDEOBITMAP == event.state) {
            List<LongVideosModel> listVideoModels = event.listVideoModels;
            this.fromEventShouldNotResume = true;
            videosSumTime = VideoModelHelper.getVideosSumTime(listVideoModels);
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.changeTextDurationAfterInsert(this.mCurInsertPosition, videosSumTime);
                this.mVideoEditHelper.changeAudioDurationAfterInsert(videosSumTime);
            }
            this.mVideoAudioManager.addVideoModelsByLocalEvent(listVideoModels, this.mCurInsertPosition);
            callVideoPause();
            this.mSurfaceView.setLastPlaylistData(this.mVideoAudioManager.getVideosModelList(), getBgmList(), getNeedPlayTime(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList()) * 1000, this.mVideoAudioManager.getMediaMute());
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.clearCurSelectVideoModel();
            }
            refreshVideoEditView();
            this.needSeekTo = getInsertSeekTo(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList());
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.setViewScrollTo((double) this.needSeekTo);
            }
            setUndoData(1);
        } else if (DestroyActivityEvent.ActivityState.LONGVIDEOCAMERA == event.state) {
            List<LongVideosModel> videosModels = event.listVideoModels;
            this.mVideoAudioManager.addAllVideoModelsByPaths2(videosModels, this.mCurInsertPosition);
            videosSumTime = VideoModelHelper.getVideosSumTime(this.mVideoAudioManager.getVideosModelList().subList(this.mCurInsertPosition, this.mCurInsertPosition + videosModels.size()));
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.changeTextDurationAfterInsert(this.mCurInsertPosition, videosSumTime);
                this.mVideoEditHelper.changeAudioDurationAfterInsert(videosSumTime);
            }
            callVideoPause();
            this.mSurfaceView.setLastPlaylistData(this.mVideoAudioManager.getVideosModelList(), getBgmList(), getNeedPlayTime(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList()) * 1000, this.mVideoAudioManager.getMediaMute());
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.clearCurSelectVideoModel();
            }
            refreshVideoEditView();
            this.needSeekTo = getInsertSeekTo(this.mCurInsertPosition, this.mVideoAudioManager.getVideosModelList());
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.setViewScrollTo((double) this.needSeekTo);
            }
            setUndoData(1);
        }
        refreshTitleAlertOutTime();
    }

    private void refreshTitleAlertOutTime() {
        float differenceTime = this.mVideoAudioManager.compareTotalTime((float) this.userLongVideoDuration);
        if (differenceTime > StaticLayoutUtil.DefaultSpacingadd) {
            String format = new DecimalFormat("0.0").format((double) differenceTime);
            this.title_alert_out_time.setTextColor(getResources().getColor(R.color.colorRed));
            this.title_alert_out_time.setText(String.format(getResources().getString(R.string.TEXT_VIDEO_EDIT_MAX_LENGTH_EXCEEDED), format));
            if (this.mTvSave.isEnabled()) {
                this.mTvSave.setEnabled(false);
                this.mTvSave.setAlpha(0.4f);
                return;
            }
            return;
        }
        this.title_alert_out_time.setTextColor(getResources().getColor(R.color.colorWhite));
        this.title_alert_out_time.setText(String.format(getResources().getString(R.string.TEXT_VIDEO_EDIT_MAX_LENGTH_NOT_EXCEEDED), "" + this.userLongVideoDuration));
        if (!this.mTvSave.isEnabled()) {
            this.mTvSave.setEnabled(true);
            this.mTvSave.setAlpha(1.0f);
        }
    }

    private long getNeedPlayTime(int insertPosition, List<LongVideosModel> longVideosModels) {
        return VideoEditHelper.getNeedPlayTime(insertPosition, longVideosModels);
    }

    private float getInsertSeekTo(int insertPosition, List<LongVideosModel> longVideosModels) {
        return VideoModelHelper.getInsertSeekTo(insertPosition, longVideosModels);
    }

    private void displayUndo() {
        UndoModel displayUndoModel = this.mVideoAudioManager.getDisplayUndoModel();
        if (displayUndoModel != null) {
            int type = displayUndoModel.type;
            if (type == 1) {
                undoVideoEdit(displayUndoModel);
            } else if (type == 0) {
                undoVideoFilter(displayUndoModel);
            } else if (type == 2) {
                undoVideoEdit(displayUndoModel);
                undoVideoFilter(displayUndoModel);
            }
        }
    }

    private void undoVideoFilter(UndoModel displayUndoModel) {
        this.displayUndoFilter = true;
        UndoFilterModel filterModel = displayUndoModel.getFilterModel();
        UndoEditModel editModel = displayUndoModel.getEditModel();
        List<LongVideosModel> videoModels = editModel.videoModels;
        List<LongVideosModel> textModels = editModel.textModels;
        List<LongVideosModel> musicModels = editModel.musicModels;
        if (videoModels != null && videoModels.size() > 0) {
            this.mVideoAudioManager.setMusicModelList(musicModels);
            this.mVideoAudioManager.changeAllPlaylistForUndo(videoModels);
            this.mVideoAudioManager.changeTextModelsWhenUndo(textModels);
        }
        LongVideosModel currentVideoModel = getCurrentVideoModel();
        if (currentVideoModel != null) {
            if (currentVideoModel.getFilterState() == 0) {
                this.mFilterName = currentVideoModel.getPublicFilterName();
            } else {
                this.mFilterName = currentVideoModel.getPrivateFilterName();
            }
            resetFilterChoosePositionByFilterName(this.mFilterName);
            List<FilterEffectBean> filterBeanList = filterModel.filterBeanList;
            clearCurrFilterEffectAndClearViewAnim(true);
            this.mFilterEffectManager.changeFilterEffectList(filterBeanList);
            loadFilterInfos(false, false, false);
            FilterInfo info = this.mHelper.getFilterInfoByFilterName(this.mFilterInfos, this.mFilterName);
            this.filterAdapter.displayUndoFilter(this.mFilterInfos.indexOf(info), info);
            setNormalFilter();
        }
    }

    private void undoVideoEdit(UndoModel displayUndoModel) {
        UndoEditModel editModel = displayUndoModel.getEditModel();
        List<LongVideosModel> videoModels = editModel.videoModels;
        List<LongVideosModel> textModels = editModel.textModels;
        List<LongVideosModel> musicModels = editModel.musicModels;
        if (videoModels != null && videoModels.size() > 0) {
            this.mVideoAudioManager.setMusicModelList(musicModels);
            this.mVideoAudioManager.changeAllPlaylistForUndo(videoModels);
            this.mVideoAudioManager.changeTextModelsWhenUndo(textModels);
            changeVideoPercent(getVideoTypeOriginWHPercent());
            if (this.mVideoEditHelper != null) {
                this.mVideoEditHelper.clearCurSelectVideoModel();
                this.mVideoEditHelper.clearCurSelectAudioModel();
                this.mVideoEditHelper.refreshVideosView(this.mVideoAudioManager.getVideosModelList(), true);
                this.mVideoEditHelper.refreshTextView(this.mVideoAudioManager.getTextModelList(), null, true, true, -1, 0, false, true, false);
                this.mVideoEditHelper.refreshSlideTouchView();
            }
            refreshLineView();
        }
    }

    private void showAlertSaveDraftDialog() {
        //new IOSAlertDialog(getActivity()).builder().setCancelable(false).setTitle(getString(2131296781)).setMsg(getString(2131296747)).setPositiveButton(46.lambdaFactory$()).show();
        new AlertDialog.Builder(getActivity()).setCancelable(false)
                .setTitle(getString(R.string.POPUP_TITLE_REALTIME_DRAFT))
                .setMessage(getString(R.string.POPUP_LABEL_REALTIME_DRAFT))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private static /* synthetic */ void lambda$showAlertSaveDraftDialog$45(android.view.View v) {
    }

    private void showVideoOptDialog() {
//        if (this.mVideoOptDialog == null) {
//            this.mVideoOptDialog = new VideoOptDialog(getActivity()).builder();
//            this.mVideoOptDialog.setOnCancelClick(new OnCancelClick() {
//                public void cancel() {
//                    if (mVideoOptDialog != null) {
//                        canVideoOptDialogShow = false;
//                        LongVideosModel model = mVideoOptDialog.getModel();
//                        if (model != null) {
//                            model.stopCurrentClient();
//                        }
//                        dismissVideoOptDialog();
//                    }
//                }
//            });
//        }
//        if (!this.mVideoOptDialog.isShowing() && this.canVideoOptDialogShow) {
//            this.mVideoOptDialog.show();
//        }
        Toast.makeText(getActivity(),"showVideoOptDialog",Toast.LENGTH_LONG).show();
    }

    private void dismissVideoOptDialog() {
//        if (this.mVideoOptDialog != null) {
//            this.mVideoOptDialog.dismiss();
//        }
        Toast.makeText(getActivity(),"dismissVideoOptDialog",Toast.LENGTH_LONG).show();

        changeVideoAudio(this.currentPlayTimeUs);
    }

    private void setVideoOptProgress(int progress, boolean suc) {
        if (progress >= 0) {
            showVideoOptDialog();
        }
//        if (this.mVideoOptDialog != null) {
//            this.mVideoOptDialog.setProgress(progress);
            Log.e("debug","setVideoOptProgress:"+progress);
            if (suc) {
                dismissVideoOptDialog();
            }
//        }
    }
}