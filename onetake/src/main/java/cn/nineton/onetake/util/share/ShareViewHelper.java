package cn.nineton.onetake.util.share;

import android.content.Context;
//import com.blink.academy.onetake.widgets.dialog.ShareActionSheetDialog.OnDialogDismissListener;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialog;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialog.OnShareCropClickListener;
//import com.blink.academy.onetake.widgets.dialog.ShareCropDialogWithFilmSubtitles;
//import com.blink.academy.onetake.widgets.dialog.ShareEditPageDialog;
//import com.blink.academy.onetake.widgets.dialog.ShareEditPageDialog.OnShareEditPageClickListener;
//import com.blink.academy.onetake.widgets.dialog.ShareNewEditPageDialog;

import cn.nineton.onetake.widget.ShareActionSheetDialog;
import cn.nineton.onetake.widget.ShareCropDialog;

public class ShareViewHelper {
    public static void showShareCropDialog(Context context, boolean isFullScreen, String mVideoFilePath, int mVideoWidth, int mVideoHeight, boolean isNeedPaint, boolean canCancelOnTouchOutSide, ShareCropDialog.OnShareCropClickListener onShareCropClickListener, ShareActionSheetDialog.OnDialogDismissListener onDialogDismissListener) {
        if (context != null) {
            new ShareCropDialog(context).builder(isFullScreen, mVideoFilePath, mVideoWidth, mVideoHeight, isNeedPaint).setCanceledOnTouchOutside(canCancelOnTouchOutSide).setOnShareCropClickListener(onShareCropClickListener).setOnDialogDismissListener(onDialogDismissListener).show();
        }
    }

    public static void showShareCropDialogWithFilmSubtitles(Context context, boolean isFullScreen, String mVideoFilePath, int mVideoWidth, int mVideoHeight, boolean isNeedPaint, boolean canCancelOnTouchOutSide, ShareCropDialog.OnShareCropClickListener onShareCropClickListener, ShareActionSheetDialog.OnDialogDismissListener onDialogDismissListener) {
        if (context != null) {
            new ShareCropDialogWithFilmSubtitles(context).builder(isFullScreen, mVideoFilePath, mVideoWidth, mVideoHeight, isNeedPaint).setCanceledOnTouchOutside(canCancelOnTouchOutSide).setOnShareCropClickListener(onShareCropClickListener).setOnDialogDismissListener(onDialogDismissListener).show();
        }
    }

    public static void showShareEditPageDialog(Context context, String text, boolean canCancelOnTouchOutSide, ShareEditPageDialog.OnShareEditPageClickListener onShareEditPageClickListener, ShareActionSheetDialog.OnDialogDismissListener onDialogDismissListener) {
        if (context != null) {
            new ShareEditPageDialog(context).builder(text).setCanceledOnTouchOutside(canCancelOnTouchOutSide).setOnShareEditPageClickListener(onShareEditPageClickListener).setOnDialogDismissListener(onDialogDismissListener).show();
        }
    }

    public static void showShareNewEditPageDialog(Context context, String text, boolean canCancelOnTouchOutSide, ShareEditPageDialog.OnShareEditPageClickListener onShareEditPageClickListener, ShareActionSheetDialog.OnDialogDismissListener onDialogDismissListener) {
        if (context != null) {
            new ShareNewEditPageDialog(context).builder(text).setCanceledOnTouchOutside(canCancelOnTouchOutSide).setOnShareEditPageClickListener(onShareEditPageClickListener).setOnDialogDismissListener(onDialogDismissListener).show();
        }
    }
}