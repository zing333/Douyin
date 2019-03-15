package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.http.params.PhotoParams;
//import com.blink.academy.onetake.support.events.TextInputEvent;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TextUtil;
//import com.blink.academy.onetake.ui.activity.video.TextInputActivity;
//import com.blink.academy.onetake.widgets.TextView.StrokeTextView;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.OnDialogDismissListener;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialog$.Lambda.1;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialog$.Lambda.2;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialog$.Lambda.3;
import java.io.IOException;

import cn.nineton.onetake.App;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.TextUtil;

public class ShareCropDialog implements OnClickListener {
    public static final String TAG = ShareCropDialog.class.getSimpleName();
    private StrokeTextView bottom_label_tv;
    private View bottom_label_view;
    private View bottom_view;
    private View content_layout_rl;
    private SurfaceView detail_surfaceView_sv;
    private Dialog dialog;
    private Display display;
    private View horizontal_bottom_view;
    private StrokeTextView horizontal_label_tv;
    private View horizontal_middle_view;
    private HorizontalScrollView horizontal_scroll_view;
    private View horizontal_top_view;
    private boolean isConfirm = false;
    private StrokeTextView label_tv;
    private Context mContext;
    private String mCurrentAddText = "";
    private boolean mCurrentCrop = false;
    private MediaPlayer mMediaPlayer;
    private OnShareCropClickListener mOnShareCropClickListener;
    private int mVideoHeigh;
    private int mVideoWidth;
    private View middle_view;
    private View paint_layout_btn_ll;
    private ScrollView scroll_view;
    private View top_view;

    public interface OnShareCropClickListener {
        void onShareCropCancelClick();

        void onShareCropConfirmClick(boolean z, float f, String str, boolean z2);

        void onShareCropDismiss();
    }

    public ShareCropDialog(Context context) {
        this.mContext = context;
        this.display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    @SuppressLint({"RtlHardcoded"})
    public ShareCropDialog builder(boolean isFullScreen, final String vidioPath, final int videoWidth, final int videoHeight, boolean isNeedPaint) {
        App.RegisterEventBus(this);
        this.mVideoWidth = videoWidth;
        this.mVideoHeigh = videoHeight;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_wechat, null);
        view.setMinimumWidth(this.display.getWidth());
        this.dialog = new Dialog(getContext(), R.style.ShareCropDialogStyle);
        this.dialog.setContentView(view);
        Window dialogWindow = this.dialog.getWindow();
        dialogWindow.setGravity(17);
        LayoutParams lp = dialogWindow.getAttributes();
        lp.x = DensityUtil.getMetricsWidth(this.mContext);
        lp.y = DensityUtil.getMetricsHeight(this.mContext);
        dialogWindow.setAttributes(lp);
        if (VERSION.SDK_INT >= 19) {
            dialogWindow.setFlags(1024, 1024);
        }
        View share_wechat_root_layout_rl = view.findViewById(R.id.share_wechat_root_layout_rl);
        share_wechat_root_layout_rl.getLayoutParams().width = this.display.getWidth();
        share_wechat_root_layout_rl.getLayoutParams().height = this.display.getHeight() - (isFullScreen ? 0 : DensityUtil.getStatusBarHeight());
//        share_wechat_root_layout_rl.setOnClickListener(1.lambdaFactory$(this));
        share_wechat_root_layout_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"ShareCropDialog:share_wechat_root_layout_rl click",Toast.LENGTH_LONG).show();
            }
        });
        final View share_wechat_crop_layout_ll = view.findViewById(R.id.share_wechat_crop_layout_ll);
        view.findViewById(R.id.cancel_layout_fl).setOnClickListener(this);
        view.findViewById(R.id.cancel_layout_fl).setOnTouchListener(TouchFocusChange());
        view.findViewById(R.id.confirm_layout_fl).setOnClickListener(this);
        view.findViewById(R.id.confirm_layout_fl).setOnTouchListener(TouchFocusChange());
        view.findViewById(R.id.text_layout_btn_ll).setOnClickListener(this);
        view.findViewById(R.id.text_layout_btn_ll).setOnTouchListener(TouchFocusChange());
        this.paint_layout_btn_ll = view.findViewById(R.id.paint_layout_btn_ll);
        if (isNeedPaint) {
            this.paint_layout_btn_ll.setOnClickListener(this);
            this.paint_layout_btn_ll.setOnTouchListener(TouchFocusChange());
        } else {
            this.paint_layout_btn_ll.setVisibility(View.GONE);
            ((LinearLayout) view.findViewById(R.id.text_layout_btn_ll)).setGravity(17);
        }
        this.scroll_view = (ScrollView) view.findViewById(R.id.scroll_view);
        this.top_view = view.findViewById(R.id.top_view);
        this.middle_view = view.findViewById(R.id.middle_view);
        this.label_tv = (StrokeTextView) view.findViewById(R.id.label_tv);
        this.bottom_view = view.findViewById(R.id.bottom_view);
        this.horizontal_scroll_view = (HorizontalScrollView) view.findViewById(R.id.horizontal_scroll_view);
        this.horizontal_top_view = view.findViewById(R.id.horizontal_top_view);
        this.horizontal_middle_view = view.findViewById(R.id.horizontal_middle_view);
        this.horizontal_label_tv = (StrokeTextView) view.findViewById(R.id.horizontal_label_tv);
        this.horizontal_bottom_view = view.findViewById(R.id.horizontal_bottom_view);
        this.content_layout_rl = view.findViewById(R.id.content_layout_rl);
        this.detail_surfaceView_sv = (SurfaceView) view.findViewById(R.id.detail_surfaceView_sv);
        this.bottom_label_tv = (StrokeTextView) view.findViewById(R.id.bottom_label_tv);
        this.bottom_label_view = view.findViewById(R.id.bottom_label_view);
        view.findViewById(R.id.label_view).setOnClickListener(this);
        view.findViewById(R.id.horizontal_label_view).setOnClickListener(this);
        view.findViewById(R.id.bottom_label_view).setOnClickListener(this);
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                ShareCropDialog.this.mMediaPlayer.start();
            }
        });
        fixSurfaceViewSize(videoWidth, videoHeight, share_wechat_crop_layout_ll);
        if (isNeedPaint && !this.mCurrentCrop) {
            this.scroll_view.setVisibility(View.INVISIBLE);
            this.horizontal_scroll_view.setVisibility(View.INVISIBLE);
            this.bottom_label_tv.setVisibility(View.VISIBLE);
            this.bottom_label_view.setVisibility(View.VISIBLE);
        }
        this.mMediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                if (videoWidth == 0 || videoHeight == 0) {
                    ShareCropDialog.this.fixSurfaceViewSize(width, height, share_wechat_crop_layout_ll);
                }
            }
        });
        this.detail_surfaceView_sv.getHolder().addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ShareCropDialog.this.mMediaPlayer.isPlaying()) {
                    ShareCropDialog.this.mMediaPlayer.stop();
                }
                ShareCropDialog.this.mMediaPlayer.reset();
                try {
                    ShareCropDialog.this.mMediaPlayer.setDisplay(ShareCropDialog.this.detail_surfaceView_sv.getHolder());
                    ShareCropDialog.this.mMediaPlayer.setDataSource(String.format("file://%1$s", new Object[]{vidioPath}));
                    ShareCropDialog.this.mMediaPlayer.setLooping(true);
                    ShareCropDialog.this.mMediaPlayer.setAudioStreamType(0);
                    ShareCropDialog.this.mMediaPlayer.setVolume(StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd);
                    ShareCropDialog.this.mMediaPlayer.prepare();
                    ShareCropDialog.this.mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            }
        });
        return this;
    }

    public ShareCropDialog setOnShareCropClickListener(OnShareCropClickListener onShareCropClickListener) {
        this.mOnShareCropClickListener = onShareCropClickListener;
        return this;
    }

    public ShareCropDialog setCancelable(boolean cancel) {
        this.dialog.setCancelable(cancel);
        return this;
    }

    public ShareCropDialog setOnDialogDismissListener(final ShareActionSheetDialog.OnDialogDismissListener listener) {
        this.dialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.onDismiss(ShareCropDialog.this.isConfirm);
                    App.UnregisterEventBus(this);
                    ShareCropDialog.this.isConfirm = false;
                }
            }
        });
        return this;
    }

    public ShareCropDialog setCanceledOnTouchOutside(boolean cancel) {
        this.dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void show() {
        this.dialog.show();
    }

    public static OnTouchListener TouchFocusChange() {
        return new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        view.setAlpha(0.3f);
                        break;
                    case 1:
                    case 3:
                        view.setAlpha(1.0f);
                        break;
                }
                return false;
            }
        };
    }

    private void closeDialog() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
        }
        this.dialog.dismiss();
        if (this.mOnShareCropClickListener != null) {
            this.mOnShareCropClickListener.onShareCropCancelClick();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 2131690521:
                closeDialog();
                return;
            case 2131690522:
                boolean isHorizontal;
                float startScale;
                this.isConfirm = true;
                if (this.paint_layout_btn_ll.getVisibility() != View.VISIBLE || this.paint_layout_btn_ll.getAlpha() < 1.0f) {
                    changeSelectArea2();
                }
                if (this.mVideoWidth > this.mVideoHeigh) {
                    isHorizontal = true;
                    int[] horizontal_scroll_view_location = new int[2];
                    this.horizontal_scroll_view.getLocationOnScreen(horizontal_scroll_view_location);
                    int[] horizontal_middle_view_location = new int[2];
                    this.horizontal_middle_view.getLocationOnScreen(horizontal_middle_view_location);
                    startScale = (((float) (horizontal_middle_view_location[0] - horizontal_scroll_view_location[0])) * 1.0f) / ((float) this.horizontal_scroll_view.getWidth());
                } else {
                    isHorizontal = false;
                    int[] scroll_view_location = new int[2];
                    this.scroll_view.getLocationOnScreen(scroll_view_location);
                    int[] middle_view_location = new int[2];
                    this.middle_view.getLocationOnScreen(middle_view_location);
                    startScale = (((float) (middle_view_location[1] - scroll_view_location[1])) * 1.0f) / ((float) this.scroll_view.getHeight());
                }
                if (this.mMediaPlayer != null) {
                    this.mMediaPlayer.reset();
                    this.mMediaPlayer.release();
                }
                this.dialog.dismiss();
                if (this.mOnShareCropClickListener != null) {
                    this.mOnShareCropClickListener.onShareCropConfirmClick(isHorizontal, startScale, this.mCurrentAddText, this.mCurrentCrop);
                    return;
                }
                return;
            case 2131690689:
                toTextInputActivity();
                return;
            case 2131690691:
                toTextInputActivity();
                return;
            case 2131690693:
                toTextInputActivity();
                return;
            case 2131690695:
                toTextInputActivity();
                return;
            case 2131690699:
                toTextInputActivity();
                return;
            case 2131690701:
                toTextInputActivity();
                return;
            case 2131690703:
                toTextInputActivity();
                return;
            case 2131690704:
                changeSelectArea();
                return;
            default:
                return;
        }
    }

    private void changeSelectArea2() {
        boolean z = true;
        if (this.mVideoWidth > this.mVideoHeigh) {
            if (this.horizontal_scroll_view.getVisibility() == View.GONE || this.horizontal_scroll_view.getVisibility() == View.INVISIBLE) {
                z = false;
            }
            this.mCurrentCrop = z;
            return;
        }
        if (this.scroll_view.getVisibility() == View.GONE || this.scroll_view.getVisibility() == View.INVISIBLE) {
            z = false;
        }
        this.mCurrentCrop = z;
    }

    private void changeSelectArea() {
        if (this.mVideoWidth > this.mVideoHeigh) {
            if (this.horizontal_scroll_view.getVisibility() == View.GONE || this.horizontal_scroll_view.getVisibility() == View.INVISIBLE) {
                this.mCurrentCrop = true;
                this.horizontal_scroll_view.setVisibility(View.VISIBLE);
                this.bottom_label_tv.setVisibility(View.GONE);
                this.bottom_label_view.setVisibility(View.GONE);
                return;
            }
            this.mCurrentCrop = false;
            this.horizontal_scroll_view.setVisibility(View.GONE);
            this.bottom_label_tv.setVisibility(View.VISIBLE);
            this.bottom_label_view.setVisibility(View.VISIBLE);
        } else if (this.scroll_view.getVisibility() == View.GONE || this.scroll_view.getVisibility() == View.INVISIBLE) {
            this.mCurrentCrop = true;
            this.scroll_view.setVisibility(View.VISIBLE);
            this.bottom_label_tv.setVisibility(View.GONE);
            this.bottom_label_view.setVisibility(View.GONE);
        } else {
            this.mCurrentCrop = false;
            this.scroll_view.setVisibility(View.GONE);
            this.bottom_label_tv.setVisibility(View.VISIBLE);
            this.bottom_label_view.setVisibility(View.VISIBLE);
        }
    }

    private void toTextInputActivity() {
//        Activity activity = (Activity) getContext();
//        Intent intent = new Intent(activity, TextInputActivity.class);
//        intent.putExtra(TextInputActivity.InputTextIntent, this.mCurrentAddText);
//        activity.startActivity(intent);
//        activity.overridePendingTransition(2131034134, 2131034134);
        Toast.makeText(getContext(),"去往:TextInputActivity",Toast.LENGTH_LONG).show();
    }

    private void fixSurfaceViewSize(int videoWidth, int videoHeight, View share_wechat_root_layout_ll) {
        final float realHeight;
        float usableWidth = (float) this.display.getWidth();
        int padding;
        RelativeLayout.LayoutParams layoutParams;
        final float realWidth;
        float scaleSize;
        int realTextSize;
        int paddingBottom;
        if ((((float) videoWidth) * 1.0f) / ((float) videoHeight) > 1.0f) {
            this.scroll_view.setVisibility(View.GONE);
            padding = (int) (((float) DensityUtil.getMetricsWidth(getContext())) * 0.15f);
            layoutParams = (RelativeLayout.LayoutParams) share_wechat_root_layout_ll.getLayoutParams();
            layoutParams.leftMargin = padding;
            layoutParams.rightMargin = padding;
            share_wechat_root_layout_ll.setLayoutParams(layoutParams);
            realWidth = usableWidth - ((float) (padding * 2));
            realHeight = ((((float) videoHeight) * realWidth) * 1.0f) / ((float) videoWidth);
            this.horizontal_scroll_view.getLayoutParams().width = (int) realWidth;
            this.horizontal_scroll_view.getLayoutParams().height = (int) realHeight;
            this.horizontal_top_view.getLayoutParams().width = (int) ((realWidth - realHeight) + 1.0f);
            this.horizontal_middle_view.getLayoutParams().width = (int) realHeight;
            this.horizontal_bottom_view.getLayoutParams().width = (int) ((realWidth - realHeight) + 1.0f);
            scaleSize = (1.0f * realHeight) / ((float) DensityUtil.dip2px(200.0f));
            realTextSize = getRealTextSize(scaleSize);
            paddingBottom = (int) (((float) DensityUtil.dip2px(10.0f)) * scaleSize);
            this.horizontal_label_tv.setStrokeWidth(scaleSize);
            ((RelativeLayout.LayoutParams) this.horizontal_label_tv.getLayoutParams()).bottomMargin = paddingBottom;
            ((RelativeLayout.LayoutParams) this.bottom_label_tv.getLayoutParams()).bottomMargin = paddingBottom;
            //Log.d(PhotoParams.text_str, "aaaa realTextSize:" + realTextSize);
            this.horizontal_label_tv.setTextSize(1, (float) realTextSize);
            this.bottom_label_tv.setTextSize(1, (float) realTextSize);
            this.bottom_label_tv.setStrokeWidth(scaleSize);
//            new Handler().postDelayed(2.lambdaFactory$(this, realWidth, realHeight), 100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lambda$fixSurfaceViewSize$2(realHeight,realWidth);
                }
            }, 100);
        } else {
            this.horizontal_scroll_view.setVisibility(View.GONE);
            padding = DensityUtil.dip2px(90.0f);
            layoutParams = (RelativeLayout.LayoutParams) share_wechat_root_layout_ll.getLayoutParams();
            layoutParams.leftMargin = padding;
            layoutParams.rightMargin = padding;
            share_wechat_root_layout_ll.setLayoutParams(layoutParams);
            realWidth = usableWidth - ((float) (padding * 2));
            realHeight = ((((float) videoHeight) * 1.0f) / ((float) videoWidth)) * realWidth;
            this.scroll_view.getLayoutParams().height = (int) realHeight;
            this.top_view.getLayoutParams().height = (int) (realHeight - realWidth);
            this.middle_view.getLayoutParams().height = (int) realWidth;
            this.bottom_view.getLayoutParams().height = (int) (realHeight - realWidth);
            scaleSize = (1.0f * realWidth) / ((float) DensityUtil.dip2px(200.0f));
            realTextSize = getRealTextSize(scaleSize);
            paddingBottom = (int) (((float) DensityUtil.dip2px(10.0f)) * scaleSize);
            ((RelativeLayout.LayoutParams) this.label_tv.getLayoutParams()).bottomMargin = paddingBottom;
            ((RelativeLayout.LayoutParams) this.bottom_label_tv.getLayoutParams()).bottomMargin = paddingBottom;
            this.label_tv.setStrokeWidth(scaleSize);
            this.bottom_label_tv.setStrokeWidth(scaleSize);
            //Log.d(PhotoParams.text_str, "realTextSize:" + realTextSize);
            this.label_tv.setTextSize(1, (float) realTextSize);
            this.bottom_label_tv.setTextSize(1, (float) realTextSize);
//            new Handler().postDelayed(3.lambdaFactory$(this, realHeight, realWidth), 100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lambda$fixSurfaceViewSize$2(realHeight,realWidth);
                }
            }, 100);
        }
        this.content_layout_rl.getLayoutParams().height = (int) realHeight;
    }

    private /* synthetic */ void lambda$fixSurfaceViewSize$1(float realWidth, float realHeight) {
        this.horizontal_scroll_view.scrollBy((int) ((realWidth - realHeight) / 2.0f), 0);
    }

    private /* synthetic */ void lambda$fixSurfaceViewSize$2(float realHeight, float realWidth) {
        this.scroll_view.scrollBy(0, (int) ((realHeight - realWidth) / 2.0f));
    }

    public void onEvent(TextInputEvent event) {
        this.mCurrentAddText = event.getTextContent();
        if (this.mVideoWidth > this.mVideoHeigh) {
            if (TextUtil.isValidate(this.mCurrentAddText)) {
                this.horizontal_label_tv.setAlpha(1.0f);
                this.horizontal_label_tv.setText(this.mCurrentAddText);
            } else {
                this.horizontal_label_tv.setAlpha(0.5f);
                this.horizontal_label_tv.setText(getContext().getString(R.string.BUTTON_TAP_TO_ADD_TEXT));
            }
        } else if (TextUtil.isValidate(this.mCurrentAddText)) {
            this.label_tv.setAlpha(1.0f);
            this.label_tv.setText(this.mCurrentAddText);
        } else {
            this.label_tv.setAlpha(0.5f);
            this.label_tv.setText(getContext().getString(R.string.BUTTON_TAP_TO_ADD_TEXT));
        }
        if (TextUtil.isValidate(this.mCurrentAddText)) {
            this.bottom_label_tv.setAlpha(1.0f);
            this.bottom_label_tv.setText(this.mCurrentAddText);
            return;
        }
        this.bottom_label_tv.setAlpha(0.5f);
        this.bottom_label_tv.setText(getContext().getString(R.string.BUTTON_TAP_TO_ADD_TEXT));
    }

    public int getRealTextSize(float textSizeScale) {
        //Log.d(PhotoParams.text_str, "textSizeScale:" + textSizeScale);
        return (int) (19.0f * textSizeScale);
    }
}