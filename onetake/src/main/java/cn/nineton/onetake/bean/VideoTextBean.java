package cn.nineton.onetake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VideoTextBean implements Parcelable {
    public static final Creator<VideoTextBean> CREATOR = new Creator<VideoTextBean>() {
        public VideoTextBean createFromParcel(Parcel in) {
            return new VideoTextBean(in);
        }

        public VideoTextBean[] newArray(int size) {
            return new VideoTextBean[size];
        }
    };
    private String align;
    private float durationPercent;
    private String fontName;
    private String fontSize;
    private String letterSpace;
    private String lineSpace;
    private float pos;
    private String shadow;
    private float showDuration;
    private long showStart;
    private float startPercent;
    private String text;
    private String textColor;
    public VideoTextBean(){}
    protected VideoTextBean(Parcel in) {
        this.showStart = in.readLong();
        this.pos = in.readFloat();
        this.shadow = in.readString();
        this.startPercent = in.readFloat();
        this.textColor = in.readString();
        this.letterSpace = in.readString();
        this.text = in.readString();
        this.lineSpace = in.readString();
        this.fontSize = in.readString();
        this.fontName = in.readString();
        this.align = in.readString();
        this.showDuration = in.readFloat();
        this.durationPercent = in.readFloat();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.showStart);
        dest.writeFloat(this.pos);
        dest.writeString(this.shadow);
        dest.writeFloat(this.startPercent);
        dest.writeString(this.textColor);
        dest.writeString(this.letterSpace);
        dest.writeString(this.text);
        dest.writeString(this.lineSpace);
        dest.writeString(this.fontSize);
        dest.writeString(this.fontName);
        dest.writeString(this.align);
        dest.writeFloat(this.showDuration);
        dest.writeFloat(this.durationPercent);
    }

    public int describeContents() {
        return 0;
    }

    public long getShowStart() {
        return this.showStart;
    }

    public void setShowStart(long showStart) {
        this.showStart = showStart;
    }

    public float getPos() {
        return this.pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public String getShadow() {
        return this.shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public float getStartPercent() {
        return this.startPercent;
    }

    public void setStartPercent(float startPercent) {
        this.startPercent = startPercent;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLetterSpace() {
        return this.letterSpace;
    }

    public void setLetterSpace(String letterSpace) {
        this.letterSpace = letterSpace;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLineSpace() {
        return this.lineSpace;
    }

    public void setLineSpace(String lineSpace) {
        this.lineSpace = lineSpace;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public float getShowDuration() {
        return this.showDuration;
    }

    public void setShowDuration(float showDuration) {
        this.showDuration = showDuration;
    }

    public float getDurationPercent() {
        return this.durationPercent;
    }

    public void setDurationPercent(float durationPercent) {
        this.durationPercent = durationPercent;
    }
}