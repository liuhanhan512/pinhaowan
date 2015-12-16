package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * 主页--小伙伴--活动详情--报名时间段model
 * Created by hanhanliu on 15/12/12.
 */
public class ActivitySignModel implements Serializable {
    private int id;
    private long startTime;
    private long endTime;
    private int remainTicket;
    // 这个活动是否报名 0 没有报名 1 已经报名
    private int isSignUp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getRemainTicket() {
        return remainTicket;
    }

    public void setRemainTicket(int remainTicket) {
        this.remainTicket = remainTicket;
    }

    public int getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(int isSignUp) {
        this.isSignUp = isSignUp;
    }
}
