package cn.nineton.onetake.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import cn.nineton.onetake.R;

public class LoadingFooterView {
    protected ProgressBar loading_pb;
    protected View mLoadingFooter;
    protected State mState = State.Idle;

    public enum State {
        Idle,
        TheEnd,
        Loading,
        TheOver
    }

    @SuppressLint({"InflateParams"})
    public LoadingFooterView(Context context) {
        this.mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.layout_loading_footer, null);
        this.loading_pb = (ProgressBar) this.mLoadingFooter.findViewById(R.id.loading_pb);
        setState(State.TheOver);
    }

    public void setBackgroundColor(int colorResID) {
        if (this.mLoadingFooter != null) {
            this.mLoadingFooter.setBackgroundColor(this.mLoadingFooter.getResources().getColor(colorResID));
        }
    }

    public View getLoadingFooterView() {
        return this.mLoadingFooter;
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State status) {
        if (this.mState != status) {
            this.mState = status;
            switch (status) {
                case Idle:
                    this.mLoadingFooter.setVisibility(View.GONE);
                    this.loading_pb.setVisibility(View.GONE);
                    return;
                case Loading:
                    this.mLoadingFooter.setVisibility(View.VISIBLE);
                    this.loading_pb.setVisibility(View.VISIBLE);
                    return;
                case TheEnd:
                    this.mLoadingFooter.setVisibility(View.GONE);
                    this.loading_pb.setVisibility(View.GONE);
                    return;
                case TheOver:
                    this.mLoadingFooter.setVisibility(View.GONE);
                    this.loading_pb.setVisibility(View.GONE);
                    return;
                default:
                    this.mLoadingFooter.setVisibility(View.GONE);
                    this.loading_pb.setVisibility(View.GONE);
                    return;
            }
        }
    }
}