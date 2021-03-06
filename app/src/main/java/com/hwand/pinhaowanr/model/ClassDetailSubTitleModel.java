package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/4.
 */
public class ClassDetailSubTitleModel implements Serializable {
    private int subscribeId;
    private long startTime;
    private long endTime;
    private int state;

    public static List<ClassDetailSubTitleModel> arrayFromData(String str) {

        Type listType = new TypeToken<List<ClassDetailSubTitleModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

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
