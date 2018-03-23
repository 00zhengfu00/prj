package com.iask.yiyuanlegou1;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MessageEvent {
    private String title;
    private String content;

    public MessageEvent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
