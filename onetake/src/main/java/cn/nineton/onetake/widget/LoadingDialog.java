package cn.nineton.onetake.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nineton.onetake.R;

/**
 * @Author: savion
 * @Date: 2018/12/12 10:07
 * @Des:
 **/
public class LoadingDialog extends DialogFragment{
    private String msg="加载中...";
    @BindView(R.id.load_msg)
    public AppCompatTextView msgTv;
    Unbinder unbinder;
    public void setMsg(String msg){
        this.msg = msg;
        if (msgTv!=null)
            msgTv.setText(msg);
    }

    public void appendMsg(String msg){
        if (msgTv!=null)
            msgTv.setText(this.msg+msg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);
        unbinder = ButterKnife.bind(this, view);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.dialog);
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        msgTv.setText(TextUtils.isEmpty(msg)?"加载中...":msg);
    }
}
