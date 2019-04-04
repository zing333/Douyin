package cn.nineton.onetake.event;

public class FontDownloadEvent {
    private int position;
    private int progress;
    private int status;
    private String url;

    public FontDownloadEvent(int status, int progress, String url, int position) {
        this.status = status;
        this.progress = progress;
        this.url = url;
        this.position = position;
    }

    public int getStatus() {
        return this.status;
    }

    public int getProgress() {
        return this.progress;
    }

    public String getUrl() {
        return this.url;
    }

    public int getPosition() {
        return this.position;
    }
}