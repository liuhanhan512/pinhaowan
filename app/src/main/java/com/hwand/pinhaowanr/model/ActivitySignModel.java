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
}
