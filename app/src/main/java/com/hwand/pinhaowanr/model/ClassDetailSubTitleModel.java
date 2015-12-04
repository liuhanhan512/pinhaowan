package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * Created by hanhanliu on 15/12/4.
 */
public class ClassDetailSubTitleModel implements Serializable {
    private int subscribeId;
    private long startTime;
    private long endTime;
    private int state;

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
