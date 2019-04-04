package cn.nineton.onetake.bean;

import android.text.Spannable;
import android.text.StaticLayout;
import java.util.List;

public class LikeEntity {
    private Spannable content;
    private boolean isEnoughTen;
    private StaticLayout layout;
    private int likeNum;
    private List<UserEntity> userEntityList;

    public LikeEntity(List<UserEntity> userEntityList, Spannable content, StaticLayout layout, int likeNum, boolean isEnoughTen) {
        this.userEntityList = userEntityList;
        this.content = content;
        this.layout = layout;
        this.likeNum = likeNum;
        this.isEnoughTen = isEnoughTen;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public void setContent(Spannable content) {
        this.content = content;
    }

    public StaticLayout getLayout() {
        return this.layout;
    }

    public void setLayout(StaticLayout layout) {
        this.layout = layout;
    }

    public void setIsEnoughTen(boolean isEnoughTen) {
        this.isEnoughTen = isEnoughTen;
    }

    public List<UserEntity> getUserEntityList() {
        return this.userEntityList;
    }

    public Spannable getContent() {
        return this.content;
    }

    public int getLikeNum() {
        return this.likeNum;
    }

    public boolean isEnoughTen() {
        return this.isEnoughTen;
    }
}