package cn.nineton.onetake.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public abstract class VTBaseView extends View {
    protected int currentPosition = 0;
    protected OnChooseChangeListener mListener;

    public interface OnChooseChangeListener {
        void change(int i);
    }

    class PointBean {
        public float x;
        public float y;
        public PointBean(){}
        public PointBean(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void set(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    protected abstract void init();

    protected abstract void init(AttributeSet attributeSet);

    public VTBaseView(Context context) {
        super(context);
        init();
    }

    public VTBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VTBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public VTBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnChooseChangeListener(OnChooseChangeListener listener) {
        this.mListener = listener;
    }
}