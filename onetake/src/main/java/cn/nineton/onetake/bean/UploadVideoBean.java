package cn.nineton.onetake.bean;

public class UploadVideoBean {
    public static final String TYPE_END = "End";
    public static final String TYPE_VIDEO = "Video";
    long creationDate;
    String fps;
    String fullDuration;
    String height;
    UploadVideoLocationBean location = new UploadVideoLocationBean();
    String make = "";
    String model = "";
    String musicVolume;
    String preferFPS;
    String rate;
    String soundVolume;
    String timeRange;
    String type;
    String width;
    String zoomMode;

    static class UploadVideoLocationBean {
        int altitude;
        int horizontalAccuracy;
        double latitude;
        double longitude;
        int speed;

        public UploadVideoLocationBean clone() {
            UploadVideoLocationBean bean = new UploadVideoLocationBean();
            bean.horizontalAccuracy = this.horizontalAccuracy;
            bean.speed = this.speed;
            bean.latitude = this.latitude;
            bean.longitude = this.longitude;
            bean.altitude = this.altitude;
            return bean;
        }
    }

    public UploadVideoBean(String type) {
        this.type = type;
        if (TYPE_END.equals(type)) {
            this.timeRange = "0.000-3.000";
        }
    }

    public UploadVideoBean cloneData() {
        UploadVideoBean bean = new UploadVideoBean(this.type);
        bean.fullDuration = this.fullDuration;
        bean.timeRange = this.timeRange;
        bean.soundVolume = this.soundVolume;
        bean.musicVolume = this.musicVolume;
        bean.rate = this.rate;
        bean.preferFPS = this.preferFPS;
        bean.zoomMode = this.zoomMode;
        bean.fps = this.fps;
        bean.location = this.location.clone();
        bean.creationDate = this.creationDate;
        bean.make = this.make;
        bean.model = this.model;
        bean.width = this.width;
        bean.height = this.height;
        return bean;
    }
}