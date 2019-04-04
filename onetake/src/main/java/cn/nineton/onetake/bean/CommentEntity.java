package cn.nineton.onetake.bean;

import android.text.Layout;
import android.text.Spannable;

public class CommentEntity {
    private int commentId;
    private int commentToUserId;
    private String content;
    private Layout layout;
    private String screenName;

    public CommentEntity() {
        this.screenName = "";
        this.content = "";
        this.commentId = 0;
        this.commentToUserId = 0;
    }

    public CommentEntity(String screenName, String replyScreenName, String content, int commentId, int commentToUserId, boolean isReply, Spannable commentContent, Layout layout) {
        this.screenName = screenName;
        this.content = content;
        this.commentId = commentId;
        this.commentToUserId = commentToUserId;
        this.layout = layout;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public String getContent() {
        return this.content;
    }

    public int getCommentId() {
        return this.commentId;
    }

    public int getCommentToUserId() {
        return this.commentToUserId;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}