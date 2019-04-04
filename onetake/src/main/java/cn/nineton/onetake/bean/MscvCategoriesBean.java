package cn.nineton.onetake.bean;

public class MscvCategoriesBean {
    private MscvDetailBean detail;
    private String name;
    private double score;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public MscvDetailBean getDetail() {
        return this.detail;
    }

    public void setDetail(MscvDetailBean detail) {
        this.detail = detail;
    }

    public String toString() {
        return "MscvCategoriesBean{name='" + this.name + '\'' + ", score=" + this.score + ", detail=" + this.detail + '}';
    }
}