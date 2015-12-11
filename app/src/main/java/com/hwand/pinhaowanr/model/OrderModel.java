package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hwand.pinhaowanr.utils.NonProguard;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by dxz on 15/12/4.
 */
public class OrderModel implements NonProguard {
    private int subscribeId;
    private long startTime;
    private long endTime;
    private String name;

    public static List<OrderModel> arrayFromData(String str) {

        Type listType = new TypeToken<List<OrderModel>>() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
