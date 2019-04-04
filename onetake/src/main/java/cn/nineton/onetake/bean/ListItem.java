package cn.nineton.onetake.bean;

import android.graphics.Rect;
import android.view.View;

public interface ListItem {
    void deactivate(View view, int i, int i2);

    int getVisibilityPercents(View view);

    void processShowScreen(int i, Rect rect);

    void setActive(View view, int i, int i2, Rect rect);

    void sharePause(View view, int i);

    void shareStart(View view, int i);
}