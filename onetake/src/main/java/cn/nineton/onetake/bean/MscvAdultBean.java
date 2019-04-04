package cn.nineton.onetake.bean;

public class MscvAdultBean {
    private double adultScore;
    private boolean isAdultContent;
    private boolean isRacyContent;
    private double racyScore;

    public boolean isIsAdultContent() {
        return this.isAdultContent;
    }

    public void setIsAdultContent(boolean isAdultContent) {
        this.isAdultContent = isAdultContent;
    }

    public boolean isIsRacyContent() {
        return this.isRacyContent;
    }

    public void setIsRacyContent(boolean isRacyContent) {
        this.isRacyContent = isRacyContent;
    }

    public double getAdultScore() {
        return this.adultScore;
    }

    public void setAdultScore(double adultScore) {
        this.adultScore = adultScore;
    }

    public double getRacyScore() {
        return this.racyScore;
    }

    public void setRacyScore(double racyScore) {
        this.racyScore = racyScore;
    }

    public String toString() {
        return "MscvAdultBean{isAdultContent=" + this.isAdultContent + ", isRacyContent=" + this.isRacyContent + ", adultScore=" + this.adultScore + ", racyScore=" + this.racyScore + '}';
    }
}