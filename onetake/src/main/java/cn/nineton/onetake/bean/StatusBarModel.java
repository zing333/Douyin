package cn.nineton.onetake.bean;


import cn.nineton.onetake.util.LogUtil;

public class StatusBarModel {
    private static StatusBarModel mStatusBarModel;
    private int color;
    private int state = -1;

    private StatusBarModel() {
    }

    public static StatusBarModel getInstance() {
        if (mStatusBarModel == null) {
            mStatusBarModel = new StatusBarModel();
        }
        return mStatusBarModel;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setColorAndState(int color, int state) {
        LogUtil.d("lookforcolorandstate", "color : " + color + " , state : " + state);
        this.color = color;
        this.state = state;
    }

    public void clearColorAndState() {
        this.color = 0;
        this.state = 1;
    }
}