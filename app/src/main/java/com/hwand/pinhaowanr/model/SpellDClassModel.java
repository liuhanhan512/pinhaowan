package com.hwand.pinhaowanr.model;

import java.io.Serializable;
import java.util.List;

/**
 * 拼拼--课程详情model
 * Created by hanhanliu on 15/12/13.
 */
public class SpellDClassModel implements Serializable {


    private int id ;
    private String busineTime;
    private int minAge;
    private int maxAge;
    private int stagePrice;
    private int oncePrice;
    private int yearPrice;

    private List<SpellDClassStageModel> stageTimeList;

    private List<SpellDClassTtileModel> titleList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusineTime() {
        return busineTime;
    }

    public void setBusineTime(String busineTime) {
        this.busineTime = busineTime;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getStagePrice() {
        return stagePrice;
    }

    public void setStagePrice(int stagePrice) {
        this.stagePrice = stagePrice;
    }

    public int getOncePrice() {
        return oncePrice;
    }

    public void setOncePrice(int oncePrice) {
        this.oncePrice = oncePrice;
    }

    public int getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(int yearPrice) {
        this.yearPrice = yearPrice;
    }

    public List<SpellDClassStageModel> getStageTimeList() {
        return stageTimeList;
    }

    public void setStageTimeList(List<SpellDClassStageModel> stageTimeList) {
        this.stageTimeList = stageTimeList;
    }

    public List<SpellDClassTtileModel> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<SpellDClassTtileModel> titleList) {
        this.titleList = titleList;
    }
}
