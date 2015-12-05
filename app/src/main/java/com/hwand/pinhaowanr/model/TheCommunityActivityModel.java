package com.hwand.pinhaowanr.model;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/5.
 */
public class TheCommunityActivityModel {
    private int id;
    private String url;
    private String title;
    private String name;
    private String detailAddress;
    private String stratTime;
    private String endTime;
    private int maxRoles;
    private int currRoles;
    private int money;
    private long creatTime;
    private int distance;

    private List<RoleModel> roleList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getStratTime() {
        return stratTime;
    }

    public void setStratTime(String stratTime) {
        this.stratTime = stratTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMaxRoles() {
        return maxRoles;
    }

    public void setMaxRoles(int maxRoles) {
        this.maxRoles = maxRoles;
    }

    public int getCurrRoles() {
        return currRoles;
    }

    public void setCurrRoles(int currRoles) {
        this.currRoles = currRoles;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<RoleModel> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleModel> roleList) {
        this.roleList = roleList;
    }
}