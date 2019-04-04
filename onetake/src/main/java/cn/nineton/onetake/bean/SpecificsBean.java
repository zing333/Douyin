package cn.nineton.onetake.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class SpecificsBean {
    public static final String TYPE_CONTRAST = "Contrast";
    public static final String TYPE_DATE = "Date";
    public static final String TYPE_DUST = "Dust";
    public static final String TYPE_EXPOSURE = "Exposure";
    public static final String TYPE_FADE = "Fade";
    public static final String TYPE_GRAINS = "Grains";
    public static final String TYPE_HIGHLIGHTSSAVE = "HighlightsSave";
    public static final String TYPE_LEAK = "Leak";
    public static final String TYPE_PRESET = "Preset";
    public static final String TYPE_PRISM = "Prism";
    public static final String TYPE_SATURATION = "Saturation";
    public static final String TYPE_SHADOWSSAVE = "ShadowsSave";
    public static final String TYPE_SHARPEN = "Sharpen";
    public static final String TYPE_SKY = "Sky";
    public static final String TYPE_TILT = "Tilt";
    public static final String TYPE_TINT = "Tint";
    public static final String TYPE_VIGNETTE = "Vignette";
    public static final String TYPE_WHITEBALANCE = "WhiteBalance";
    private String Mode;
    private String Type;
    private String Value;
    private String ValueMax;
    private String ValueMin;

    public String toString() {
        return "SpecificsBean{Type='" + this.Type + '\'' + ", Mode='" + this.Mode + '\'' + ", Value='" + this.Value + '\'' + ", ValueMin='" + this.ValueMin + '\'' + ", ValueMax='" + this.ValueMax + '\'' + '}';
    }

    public static List<SpecificsBean> arraySpecificsBeanFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<SpecificsBean>>() {
        }.getType());
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getMode() {
        return this.Mode;
    }

    public void setMode(String Mode) {
        this.Mode = Mode;
    }

    public String getValue() {
        return this.Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getValueMin() {
        return this.ValueMin;
    }

    public void setValueMin(String ValueMin) {
        this.ValueMin = ValueMin;
    }

    public String getValueMax() {
        return this.ValueMax;
    }

    public void setValueMax(String ValueMax) {
        this.ValueMax = ValueMax;
    }
}