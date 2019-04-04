package cn.nineton.onetake.bean;

import java.util.List;

public class MscvDetailBean {
    private List<MscvCelebritiesBean> celebrities;

    public List<MscvCelebritiesBean> getCelebrities() {
        return this.celebrities;
    }

    public void setCelebrities(List<MscvCelebritiesBean> celebrities) {
        this.celebrities = celebrities;
    }

    public String toString() {
        return "MscvDetailBean{celebrities=" + this.celebrities + '}';
    }
}