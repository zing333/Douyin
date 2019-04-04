package cn.nineton.onetake.bean;


public class BadgeEntity extends BaseEntity {
    private TimeLineBadgeBean badgeBean;
    private boolean isGray = false;

    public BadgeEntity(int viewType) {
        super(viewType);
    }

    public BadgeEntity(int viewType, TimeLineBadgeBean badgeBean) {
        super(viewType);
        this.badgeBean = badgeBean;
    }

    public TimeLineBadgeBean getBadgeBean() {
        return this.badgeBean;
    }

    public void setGray(boolean gray) {
        this.isGray = gray;
    }

    public boolean isGray() {
        return this.isGray;
    }
}