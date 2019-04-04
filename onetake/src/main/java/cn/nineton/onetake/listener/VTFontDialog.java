package cn.nineton.onetake.listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
//import com.blink.academy.onetake.bean.VTFontDesBean;
//import com.blink.academy.onetake.support.utils.ColorFilterUtil;
//import com.blink.academy.onetake.support.utils.DensityUtil;
//import com.blink.academy.onetake.ui.adapter.VTFontAdapter;
//import com.blink.academy.onetake.ui.adapter.VTFontAdapter.OnItemClickListener;
//import com.blink.academy.onetake.widgets.loading.RecyclerLoadingView;
import java.util.ArrayList;
import java.util.Iterator;

import cn.nineton.onetake.R;
import cn.nineton.onetake.adapter.VTFontAdapter;
import cn.nineton.onetake.bean.VTFontDesBean;
import cn.nineton.onetake.util.ColorFilterUtil;
import cn.nineton.onetake.util.DensityUtil;
import cn.nineton.onetake.widget.RecyclerLoadingView;

public class VTFontDialog implements OnClickListener {
    private Dialog dialog;
    private RecyclerLoadingView dialog_filter_font_list;
    private Display display;
    private boolean mCancelable;
    private Activity mContext;
    private VTFontAdapter mFilterFontAdapter;
    private Handler mHandler = new Handler();
    private LinearLayoutManager mLayoutManager;
    private int mMaxHeight;
    private OnFontClickListener mOnFontClickListener;
    private VTFontAdapter.OnItemClickListener mOnItemClickListener = new VTFontAdapter.OnItemClickListener() {
        public void onClick(View itemView, VTFontDesBean vtFontDesBean, int position) {
            if (VTFontDialog.this.mOnFontClickListener != null) {
                VTFontDialog.this.mOnFontClickListener.OnFontClick(vtFontDesBean, position);
                VTFontDialog.this.mSelected = position;
            }
            VTFontDialog.this.dismiss();
        }
    };
    private int mSelected;
    private ArrayList<VTFontDesBean> mVTFontDesBeanList;
    private boolean move;
    private int scrollLayoutHeight = 0;

    public interface OnFontClickListener {
        void OnFontClick(VTFontDesBean vTFontDesBean, int i);
    }

    class RecyclerViewListener extends OnScrollListener {
        RecyclerViewListener() {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (VTFontDialog.this.move) {
                VTFontDialog.this.move = false;
                int n = VTFontDialog.this.mSelected - VTFontDialog.this.mLayoutManager.findFirstVisibleItemPosition();
                if (n >= 0 && n < VTFontDialog.this.dialog_filter_font_list.getChildCount()) {
                    VTFontDialog.this.dialog_filter_font_list.scrollBy(0, VTFontDialog.this.dialog_filter_font_list.getChildAt(n).getTop());
                }
            }
        }
    }

    public VTFontDialog(Activity context, ArrayList<VTFontDesBean> VTFontDesBeanList) {
        this.mContext = context;
        this.mVTFontDesBeanList = VTFontDesBeanList;
        this.display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    @SuppressLint({"RtlHardcoded"})
    public VTFontDialog builder(int maxHeight) {
        this.mMaxHeight = maxHeight;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_font, null);
        view.setMinimumWidth(this.display.getWidth());
        this.dialog = new Dialog(getContext(), R.style.FilterFontDialogStyle);
        this.dialog.setContentView(view);
        Window dialogWindow = this.dialog.getWindow();
        dialogWindow.setGravity(17);
        LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        if (VERSION.SDK_INT >= 19) {
            dialogWindow.setFlags(1024, 1024);
        }
        initData();
        initView(view);
        return this;
    }

    private void initData() {
        int size = this.mVTFontDesBeanList.size();
        for (int i = 0; i < size; i++) {
            if (((VTFontDesBean) this.mVTFontDesBeanList.get(i)).defaultX == 1) {
                this.mSelected = i;
                break;
            }
        }
        if (this.mFilterFontAdapter == null) {
            this.mFilterFontAdapter = new VTFontAdapter(this.mContext, this.mVTFontDesBeanList);
        }
        if (this.mLayoutManager == null) {
            this.mLayoutManager = new LinearLayoutManager(getContext());
        }
        this.mFilterFontAdapter.setOnItemClickListener(this.mOnItemClickListener);
    }

    private void initView(View view) {
        View empty_view_fl = view.findViewById(R.id.empty_view_fl);
        View close_layout_fl = view.findViewById(R.id.close_layout_fl);
        View dialog_filter_font_root_rl = view.findViewById(R.id.dialog_filter_font_root_rl);
        View dialog_filter_font_rl = view.findViewById(R.id.dialog_filter_font_rl);
        this.dialog_filter_font_list = (RecyclerLoadingView) view.findViewById(R.id.dialog_filter_font_list);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dialog_filter_font_root_rl.getLayoutParams();
        this.scrollLayoutHeight = Math.min((int) (((float) this.mVTFontDesBeanList.size()) * (getContext().getResources().getDimension(R.dimen.shareSheetItemHeight) + 0.5f)), this.mMaxHeight);
        layoutParams.height = this.scrollLayoutHeight;
        dialog_filter_font_root_rl.setLayoutParams(layoutParams);
        empty_view_fl.setOnClickListener(this);
        close_layout_fl.setOnClickListener(this);
        close_layout_fl.setOnTouchListener(ColorFilterUtil.TouchFocusChange());
        this.dialog_filter_font_list.setLayoutManager(this.mLayoutManager);
        this.dialog_filter_font_list.setAdapter(this.mFilterFontAdapter);
        this.dialog_filter_font_list.setOnScrollListener(new RecyclerViewListener());
    }

    public VTFontDialog setCancelable(boolean cancel) {
        this.mCancelable = cancel;
        this.dialog.setCancelable(cancel);
        return this;
    }

    public VTFontDialog setCanceledOnTouchOutside(boolean cancel) {
        this.dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void show() {
        Iterator it = this.mVTFontDesBeanList.iterator();
        while (it.hasNext()) {
            VTFontDesBean vtfBean = (VTFontDesBean) it.next();
            if (vtfBean.status == 3) {
                vtfBean.status = 4;
            }
        }
        if (this.mFilterFontAdapter != null) {
            this.mFilterFontAdapter.notifyDataSetChanged();
        }
        this.dialog.show();
        int center = ((this.scrollLayoutHeight - DensityUtil.dip2px(55.0f)) / DensityUtil.dip2px(55.0f)) / 2;
        if (this.mSelected > this.mVTFontDesBeanList.size() - center) {
            scrollToMiddle(this.mSelected);
        } else {
            scrollToMiddle(this.mSelected - center);
        }
    }

    private void scrollToMiddle(int n) {
        this.dialog_filter_font_list.stopScroll();
        int firstItem = this.mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = this.mLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            this.dialog_filter_font_list.scrollToPosition(Math.max(n, 0));
        } else if (n <= lastItem) {
            this.dialog_filter_font_list.scrollBy(0, this.dialog_filter_font_list.getChildAt(n - firstItem).getTop());
        } else {
            this.dialog_filter_font_list.scrollToPosition(n);
            this.move = true;
        }
    }

    public boolean isShow() {
        return this.dialog.isShowing();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 2131690566:
                if (this.mCancelable) {
                    dismiss();
                    return;
                }
                return;
            case 2131690568:
                dismiss();
                return;
            default:
                return;
        }
    }

    public void dismiss() {
        this.dialog.dismiss();
    }

    public VTFontDialog setOnFontClickListener(OnFontClickListener onFontClickListener) {
        this.mOnFontClickListener = onFontClickListener;
        return this;
    }
}