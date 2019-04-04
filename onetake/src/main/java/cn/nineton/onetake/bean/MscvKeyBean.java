package cn.nineton.onetake.bean;

public class MscvKeyBean {
    private AuthHeaderBean auth_header;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AuthHeaderBean getAuth_header() {
        return this.auth_header;
    }

    public void setAuth_header(AuthHeaderBean auth_header) {
        this.auth_header = auth_header;
    }
}