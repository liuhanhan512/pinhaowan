package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/2.
 */
public class HomePageModel implements Serializable{


    /**
     * lng : 121.441442
     * createTime : 1449022380000
     * isStick : 0
     * pictureUrl : 2.png
     * remainTicket : 200
     * subtitle : 副标题
     * viewType : 1 兴趣益教 2 演出展览 3 游乐
     * detailAddress : 上海市普陀区长寿路155号
     * className : 跳舞
     * id : 2
     * title : 主题1
     * lat : 31.24276
     */

    private double lng;
    private long createTime;
    private int isStick;
    private String pictureUrl;
    private int remainTicket;
    private String subtitle;
    private int viewType;
    private String detailAddress;
    private String className;
    private int id;
    private String title;
    private double lat;

    public static List<HomePageModel> arrayHomePageModelFromData(String str) {

        Type listType = new TypeToken<ArrayList<HomePageModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setIsStick(int isStick) {
        this.isStick = isStick;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setRemainTicket(int remainTicket) {
        this.remainTicket = remainTicket;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getIsStick() {
        return isStick;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getRemainTicket() {
        return remainTicket;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getViewType() {
        return viewType;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getClassName() {
        return className;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }
}
