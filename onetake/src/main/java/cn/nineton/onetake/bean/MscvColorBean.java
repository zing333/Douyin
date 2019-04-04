package cn.nineton.onetake.bean;

import java.util.List;

public class MscvColorBean {
    private String accentColor;
    private String dominantColorBackground;
    private String dominantColorForeground;
    private List<String> dominantColors;
    private boolean isBWImg;

    public String getDominantColorForeground() {
        return this.dominantColorForeground;
    }

    public void setDominantColorForeground(String dominantColorForeground) {
        this.dominantColorForeground = dominantColorForeground;
    }

    public String getDominantColorBackground() {
        return this.dominantColorBackground;
    }

    public void setDominantColorBackground(String dominantColorBackground) {
        this.dominantColorBackground = dominantColorBackground;
    }

    public String getAccentColor() {
        return this.accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public boolean isIsBWImg() {
        return this.isBWImg;
    }

    public void setIsBWImg(boolean isBWImg) {
        this.isBWImg = isBWImg;
    }

    public List<String> getDominantColors() {
        return this.dominantColors;
    }

    public void setDominantColors(List<String> dominantColors) {
        this.dominantColors = dominantColors;
    }

    public String toString() {
        return "MscvColorBean{dominantColorForeground='" + this.dominantColorForeground + '\'' + ", dominantColorBackground='" + this.dominantColorBackground + '\'' + ", accentColor='" + this.accentColor + '\'' + ", isBWImg=" + this.isBWImg + ", dominantColors=" + this.dominantColors + '}';
    }
}