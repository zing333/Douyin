package cn.nineton.onetake.adapter;

import android.app.Activity;
import android.view.View;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.ui.adapter.holder.ABRecyclerViewHolder;
import java.util.List;

import cn.nineton.onetake.App;

public abstract class NewABRecyclerViewHolder extends ABRecyclerViewHolder {
    private static final String TAG = NewABRecyclerViewHolder.class.getSimpleName();
    protected Activity mActivity;
    protected HolderHelper mHolderHelper;

    public interface HolderHelper<T> {
        List<T> getAllDatas();

        void shareAction(T t, int i);
    }

    public interface TimelineHolderHelper<T> extends HolderHelper<T> {
        void callNotifyItemRemoved(int i);
    }

    public interface AudioStoreHolderHelper<T> extends HolderHelper<T> {
        int getUserWallet();
    }

    public abstract void onBindViewHolder(Object obj, int i);

    protected Activity getActivity() {
        return this.mActivity;
    }

    public NewABRecyclerViewHolder(View itemView, Activity context, HolderHelper helper) {
        super(itemView);
        this.mActivity = context;
        this.mHolderHelper = helper;
    }

    public void onBindViewHolder(int position, List<Object> list) {
    }

    protected String getString(int resId) {
        return App.getResource().getString(resId);
    }

    /* renamed from: runOnUiThread */
    protected void access$000(Runnable runnable) {
        App.runOnUiThread(runnable);
    }
}