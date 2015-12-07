package com.hwand.pinhaowanr.model;

import com.hwand.pinhaowanr.utils.NonProguard;

/**
 * Created by dxz on 2015/12/2.
 */
public class MsgInfo implements NonProguard {

    /**
     * id : 消息ID
     * type : 消息类型 1 人与人聊天 2 系统消息
     * acceptId : 接受消息者ID
     * sendId : 发消息者ID
     * content : 消息内容:最新的一条记录
     * time : 发送时间
     * name : 发消息者用户名
     * url : 发消息者头像URL
     */

    private int id;

    private int type;
    private int acceptId;
    private int sendId;
    private String content;
    private long time;
    private String name;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(int acceptId) {
        this.acceptId = acceptId;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
