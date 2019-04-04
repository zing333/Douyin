package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.Config;
//import com.blink.academy.onetake.bean.VTFontDesBean;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.events.FontDownloadEvent;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.support.utils.LocaleUtil;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.support.utils.TintColorUtil;
//import com.blink.academy.onetake.ui.adapter.VTFontAdapter$FilterFontHolder$.Lambda.1;
//import com.blink.academy.onetake.ui.adapter.VTFontAdapter$FilterFontHolder$.Lambda.2;
//import com.blink.academy.onetake.ui.adapter.base.RecyclerBaseAdapter;
//import com.blink.academy.onetake.ui.adapter.holder.ABRecyclerViewHolder;
//import com.blink.academy.onetake.widgets.CircularProgressBar.CircleProgressBar;
//import com.blink.academy.onetake.widgets.IOSDialog.IOSAlertDialog;
//import com.lzy.okhttputils.OkHttpUtils;
//import com.lzy.okhttputils.cache.CacheMode;
//import com.lzy.okhttputils.callback.FileCallback;
//import com.lzy.okhttputils.request.GetRequest;
//import de.greenrobot.event.EventBus;
import java.io.File;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.VTFontDesBean;
import cn.nineton.onetake.event.FontDownloadEvent;
import cn.nineton.onetake.util.ColorFilterUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.util.LocaleUtil;
import cn.nineton.onetake.util.TintColorUtil;
import cn.nineton.onetake.widget.CircleProgressBar;
import cn.nineton.onetake.widget.StaticLayoutUtil;
//import okhttp3.Call;
//import okhttp3.Response;

public class VTFontAdapter extends RecyclerBaseAdapter<VTFontDesBean> {
    private boolean alerted;
    private OnItemClickListener mOnItemClickListener;
    private int mPadding = DensityUtil.dip2px(2.5f);

    private class FilterFontHolder extends ABRecyclerViewHolder {
        private final ImageView font_item_example_iv;
        private final ImageView font_item_language_iv;
        private final RelativeLayout font_item_layout_rl;
        private final CircleProgressBar font_item_progress_cpb;
        private final ImageView font_item_status_iv;
        private final View item_line_view;
        private long mCurProgress;
        private VTFontDesBean mVtFontDesBean;

        public FilterFontHolder(View itemView) {
            super(itemView);
            App.RegisterEventBus(this);
            this.font_item_layout_rl = (RelativeLayout) itemView.findViewById(R.id.font_item_layout_rl);
            this.font_item_language_iv = (ImageView) itemView.findViewById(R.id.font_item_language_iv);
            this.font_item_example_iv = (ImageView) itemView.findViewById(R.id.font_item_example_iv);
            this.font_item_progress_cpb = (CircleProgressBar) itemView.findViewById(R.id.font_item_progress_cpb);
            this.font_item_status_iv = (ImageView) itemView.findViewById(R.id.font_item_status_iv);
            this.item_line_view = itemView.findViewById(R.id.item_line_view);
        }

        public void onBindViewHolder(final int position) {
            this.mVtFontDesBean = (VTFontDesBean) VTFontAdapter.this.getList().get(position);
            this.itemView.setVisibility(View.VISIBLE);
            if (this.mVtFontDesBean.selected) {
                this.font_item_language_iv.setImageResource(R.drawable.icon_15_confirm);
                this.font_item_language_iv.setPadding(VTFontAdapter.this.mPadding, VTFontAdapter.this.mPadding, VTFontAdapter.this.mPadding, VTFontAdapter.this.mPadding);
            } else {
                this.font_item_language_iv.setPadding(0, 0, 0, 0);
                this.font_item_language_iv.setImageResource(this.mVtFontDesBean.language.equals(LocaleUtil.Chinese) ? R.drawable.icon_20_text_cnfont : R.drawable.icon_20_text_enfont);
            }
            this.font_item_example_iv.setImageResource(VTFontAdapter.this.getActivity().getResources().getIdentifier(VTFontAdapter.this.getDrawableName(this.mVtFontDesBean.filename), "drawable", VTFontAdapter.this.getActivity().getPackageName()));
            if (this.mVtFontDesBean.status == 1) {
                this.font_item_status_iv.setImageResource(R.drawable.icon_20_save_camera_roll);
                this.font_item_status_iv.setVisibility(View.INVISIBLE);
                this.font_item_progress_cpb.setVisibility(View.INVISIBLE);
                this.font_item_progress_cpb.setProgress(StaticLayoutUtil.DefaultSpacingadd);
                VTFontAdapter.this.setStatusColor(this.font_item_layout_rl, R.color.colorWhite);
            } else if (this.mVtFontDesBean.status == 0) {
                this.font_item_status_iv.setImageResource(R.drawable.icon_20_save_camera_roll);
                this.font_item_status_iv.setVisibility(View.VISIBLE);
                this.font_item_progress_cpb.setVisibility(View.INVISIBLE);
                this.font_item_progress_cpb.setProgress(StaticLayoutUtil.DefaultSpacingadd);
                VTFontAdapter.this.setStatusColor(this.font_item_layout_rl, R.color.colorDate);
            } else if (this.mVtFontDesBean.status == 4) {
                this.font_item_status_iv.setImageResource(R.drawable.icon_20_text_retry);
                this.font_item_status_iv.setVisibility(View.VISIBLE);
                this.font_item_progress_cpb.setVisibility(View.INVISIBLE);
                this.font_item_progress_cpb.setProgress(StaticLayoutUtil.DefaultSpacingadd);
                VTFontAdapter.this.setStatusColor(this.font_item_layout_rl, R.color.colorDate);
            } else {
                this.font_item_status_iv.setImageResource(R.drawable.icon_20_text_retry);
                this.font_item_status_iv.setVisibility(View.INVISIBLE);
                this.font_item_progress_cpb.setVisibility(View.VISIBLE);
                this.font_item_progress_cpb.setProgress((float) this.mVtFontDesBean.progress);
                VTFontAdapter.this.setStatusColor(this.font_item_layout_rl, R.color.colorDate);
            }
            this.font_item_layout_rl.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
            //this.font_item_layout_rl.setOnClickListener(1.lambdaFactory$(this, position));
            font_item_layout_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lambda$onBindViewHolder$1(position,v);
                }
            });
        }

        private /* synthetic */ void lambda$onBindViewHolder$1(int position, View v) {
            if (this.mVtFontDesBean.status == 1) {
                if (VTFontAdapter.this.mOnItemClickListener != null) {
                    VTFontAdapter.this.mOnItemClickListener.onClick(this.itemView, this.mVtFontDesBean, position);
                }
                for (VTFontDesBean vtFontDesBean1 : VTFontAdapter.this.getList()) {
                    vtFontDesBean1.selected = false;
                }
                this.mVtFontDesBean.selected = true;
                VTFontAdapter.this.notifyDataSetChanged();
            } else if (this.mVtFontDesBean.status == 4 || this.mVtFontDesBean.status == 0) {
                Activity activity = VTFontAdapter.this.getActivity();
                VTFontAdapter.this.getActivity();
                NetworkInfo networkInfo = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (networkInfo == null) {
                    downloadFonts(this.mVtFontDesBean, position);
                } else if (!networkInfo.isConnected() || networkInfo.getType() != 0) {
                    downloadFonts(this.mVtFontDesBean, position);
                } else if (VTFontAdapter.this.alerted) {
                    downloadFonts(this.mVtFontDesBean, position);
                } else {
                    //new IOSAlertDialog(VTFontAdapter.this.getActivity()).builder().setTitle(VTFontAdapter.this.getActivity().getResources().getString(2131296775)).setMsg(String.format(VTFontAdapter.this.getActivity().getResources().getString(2131296742), new Object[]{Double.valueOf(this.mVtFontDesBean.size)})).setCancelable(true).setPositiveButton(2.lambdaFactory$(this, position)).setNegativeButton(null).show();
                    final int mPos = position;
                    new AlertDialog.Builder(getActivity()).setTitle(getActivity().getResources().getString(R.string.POPUP_TITLE_DOWNLOAD_FONT))
                            .setMessage(R.string.POPUP_LABEL_DOWNLOAD_FONT)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadFonts(mVtFontDesBean, mPos);
                                    VTFontAdapter.this.alerted = true;
                                }
                            }).show();
                }
            }
        }

        private /* synthetic */ void lambda$null$0(int position, View v1) {
            downloadFonts(this.mVtFontDesBean, position);
            VTFontAdapter.this.alerted = true;
        }

        private void downloadFonts(VTFontDesBean vtFontDesBean, int position) {
//            vtFontDesBean.status = 3;
//            this.font_item_status_iv.setVisibility(View.INVISIBLE);
//            this.font_item_progress_cpb.setVisibility(View.VISIBLE);
//            VTFontAdapter.this.setStatusColor(this.font_item_layout_rl, R.color.colorDate);
//            final VTFontDesBean vTFontDesBean = vtFontDesBean;
//            final int i = position;
//            ((GetRequest) ((GetRequest) OkHttpUtils.get(vtFontDesBean.url).tag(this)).cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)).execute(new FileCallback(Config.getFontDestPath(vtFontDesBean.language), String.format("%1$s.%2$s", new Object[]{vtFontDesBean.filename, vtFontDesBean.fonttype})) {
//                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                    super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
//                    int curProgress = (int) (100.0f * progress);
//                    if (curProgress == 100) {
//                        LogUtil.d("font downloading...", "onSuccess:" + vTFontDesBean.filename);
//                        EventBus.getDefault().post(new FontDownloadEvent(1, 0, vTFontDesBean.url, i));
//                        return;
//                    }
//                    LogUtil.d("font downloading...", "===========================================curProgress:" + curProgress + ", filename:" + vTFontDesBean.filename);
//                    EventBus.getDefault().post(new FontDownloadEvent(3, curProgress, vTFontDesBean.url, i));
//                }
//
//                public void onError(Call call, Response response, Exception e) {
//                    super.onError(call, response, e);
//                    LogUtil.d("font downloading...", "onError:" + vTFontDesBean.filename);
//                    EventBus.getDefault().post(new FontDownloadEvent(4, 0, vTFontDesBean.url, i));
//                }
//
//                public void onSuccess(File file, Call call, Response response) {
//                    OkHttpUtils.delete(vTFontDesBean.url);
//                    ConnectivityManager cm = (ConnectivityManager) VTFontAdapter.this.getActivity().getSystemService("connectivity");
//                    if (cm == null) {
//                        return;
//                    }
//                    if (!cm.getActiveNetworkInfo().isConnectedOrConnecting() || file == null) {
//                        EventBus.getDefault().post(new FontDownloadEvent(4, 0, vTFontDesBean.url, i));
//                    }
//                }
//            });
            Toast.makeText(getActivity(),"下载字体包",Toast.LENGTH_LONG).show();
        }

        public void onEventMainThread(FontDownloadEvent event) {
            if (this.mVtFontDesBean.url != null && this.mVtFontDesBean.url.equals(event.getUrl())) {
                this.mVtFontDesBean.status = event.getStatus();
                this.mVtFontDesBean.progress = event.getProgress();
                this.mCurProgress = (long) event.getProgress();
                if (this.mVtFontDesBean.status == 1) {
                    VTFontAdapter.this.notifyItemChanged(event.getPosition());
                } else if (this.mVtFontDesBean.status == 4) {
                    VTFontAdapter.this.notifyItemChanged(event.getPosition());
                } else if (this.mVtFontDesBean.status == 3) {
                    this.font_item_status_iv.setVisibility(View.INVISIBLE);
                    this.font_item_progress_cpb.setVisibility(View.VISIBLE);
                    this.font_item_progress_cpb.setProgress((float) this.mVtFontDesBean.progress);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, VTFontDesBean vTFontDesBean, int i);
    }

    public VTFontAdapter(Activity mActivity, List list) {
        super(mActivity, list);
    }

    public ABRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        parent.setPadding(0, 0, 0, 0);
        return new FilterFontHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_font_item, parent, false));
    }

    protected void onBindHeaderView(View headerView) {
    }

    protected void onBindFooterView(View footerView) {
    }

    protected void onBindItemView(ABRecyclerViewHolder holder, VTFontDesBean item, int position) {
    }

    public void onBindViewHolder(ABRecyclerViewHolder holder, int position) {
        holder.onBindViewHolder(position);
    }

    private String getDrawableName(String filename) {
        char c = filename.charAt(0);
        if (c >= '0' && c <= '9') {
            filename = "_" + filename;
        } else if (c >= 'A' && c <= 'Z') {
            filename = filename.replace(String.valueOf(c), String.valueOf((char) ((c + 97) - 65)));
        }
        for (int i = 1; i < filename.length(); i++) {
            char c1 = filename.charAt(i);
            if (c1 >= 'A' && c1 <= 'Z') {
                filename = filename.replace(String.valueOf(c1), "_" + String.valueOf((char) ((c1 + 97) - 65)));
            }
        }
        return filename;
    }

    private void setStatusColor(ViewGroup group, int colorId) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = group.getChildAt(i);
            if (childAt instanceof ImageView) {
                TintColorUtil.tintDrawable(getActivity(), (ImageView) childAt, colorId);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}