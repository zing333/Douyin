package cn.nineton.onetake.util.share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.OnDialogDismissListener;
//import com.blink.academy.onetake.widgets.dialog.ShareEditPageDialog.OnShareEditPageClickListener;
//import com.blink.academy.onetake.widgets.dialog.ShareNewEditPageDialog$.Lambda.1;
//import com.blink.academy.onetake.widgets.dialog.ShareNewEditPageDialog$.Lambda.2;
import cn.nineton.onetake.R;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.widget.ShareActionSheetDialog;

public class ShareNewEditPageDialog implements OnClickListener {
    private Dialog dialog;
    private boolean isConfirm = false;
    private Context mContext;
    private int mShareType;
    private ShareEditPageDialog.OnShareEditPageClickListener onShareEditPageClickListener;
    private EditText send_conten_et;

    public ShareNewEditPageDialog setOnShareEditPageClickListener(ShareEditPageDialog.OnShareEditPageClickListener onShareEditPageClickListener) {
        this.onShareEditPageClickListener = onShareEditPageClickListener;
        return this;
    }

    public ShareNewEditPageDialog(Context context) {
        this.mContext = context;
    }

    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    public ShareNewEditPageDialog builder(String videoLocation) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_edit_page, null);
        View dialog_layout_ll = view.findViewById(R.id.dialog_layout_ll);
        this.send_conten_et = view.findViewById(R.id.send_conten_et);
        view.findViewById(R.id.cancel_layout_fl).setOnClickListener(this);
        view.findViewById(R.id.cancel_layout_fl).setOnTouchListener(TouchFocusChange());
        view.findViewById(R.id.confirm_layout_fl).setOnClickListener(this);
        view.findViewById(R.id.confirm_layout_fl).setOnTouchListener(TouchFocusChange());
        this.send_conten_et.setFocusable(true);
        this.send_conten_et.setFocusableInTouchMode(true);
        this.send_conten_et.requestFocus();
        if (!videoLocation.equals("")) {
            this.send_conten_et.setText(videoLocation);
            this.send_conten_et.setSelection(videoLocation.length());
        }
        this.dialog = new Dialog(getContext(), R.style.NewEditPageShareDialogStyle);
        this.dialog.setContentView(view);
        this.dialog.getWindow().setSoftInputMode(4);
        dialog_layout_ll.setLayoutParams(new LayoutParams(DensityUtil.getMetricsWidth(getContext()) - (DensityUtil.dip2px(40.0f) * 2), -2));
        return this;
    }

    public ShareNewEditPageDialog setShareType(int shareType) {
        this.mShareType = shareType;
        return this;
    }

    public ShareNewEditPageDialog setCancelable(boolean cancel) {
        this.dialog.setCancelable(cancel);
        return this;
    }

    public ShareNewEditPageDialog setCanceledOnTouchOutside(boolean cancel) {
        this.dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ShareNewEditPageDialog setOnDialogDismissListener(ShareActionSheetDialog.OnDialogDismissListener listener) {
//        this.dialog.setOnDismissListener(1.lambdaFactory$(this, listener));
//        return this;
        if (listener != null) {
            listener.onDismiss(this.isConfirm);
            this.isConfirm = false;
        }
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
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha(0.3f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.setAlpha(1.0f);
                        break;
                }
                return false;
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_layout_fl:
                this.dialog.dismiss();
                if (this.onShareEditPageClickListener != null) {
                    this.onShareEditPageClickListener.onShareEditPageCancelClick();
                    return;
                }
                return;
            case R.id.confirm_layout_fl:
                this.isConfirm = true;
                if (this.onShareEditPageClickListener != null) {
                    String content = this.send_conten_et.getText().toString();
                    LogUtil.d("SHARE_CONTENT: " + content);
                    ShareEditPageDialog.OnShareEditPageClickListener onShareEditPageClickListener = this.onShareEditPageClickListener;
                    if (content.trim().equals("")) {
                        content = String.format(getContext().getResources().getString(R.string.TEXT_SHARE_JUST_CREATED), new Object[]{getOneShareTypeString(this.mShareType)});
                    }
                    onShareEditPageClickListener.onShareEditPageConfirmClick(content);
                }
                this.dialog.dismiss();
                return;
            default:
                return;
        }
    }

    private String getOneShareTypeString(int shareType) {
        if (shareType == 0) {
            return getContext().getResources().getString(R.string.MEDIA_TYPE_GIF);
        }
        if (shareType == 1) {
            return getContext().getResources().getString(R.string.MEDIA_TYPE_PHOTO);
        }
        if (shareType == 2) {
            return getContext().getResources().getString(R.string.MEDIA_TYPE_VIDEO);
        }
        return "";
    }
}