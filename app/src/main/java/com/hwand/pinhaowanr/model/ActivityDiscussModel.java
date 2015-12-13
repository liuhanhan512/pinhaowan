package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * 主页--小伙伴--活动详情--讨论model
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityDiscussModel implements Serializable {
    private int id;
    private String url;
    private String name;
    private String time;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
