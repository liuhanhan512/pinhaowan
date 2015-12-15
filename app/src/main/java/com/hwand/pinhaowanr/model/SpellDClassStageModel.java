package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * Created by hanhanliu on 15/12/13.
 */
public class SpellDClassStageModel implements Serializable {
    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
