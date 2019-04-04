package cn.nineton.onetake.bean;

public class IMVideoTokenBean {
    public IMVideoToken tokens;
    public IMVideoBean video;

    public class IMVideoBean {
        public String created_at;
        public int height;
        public int id;
        public String preview_info;
        public String preview_url;
        public String updated_at;
        public int user_id;
        public String video_url;
        public int width;
    }

    public class IMVideoToken {
        public String preview_token;
        public String upload_url;
        public String video_token;
    }
}