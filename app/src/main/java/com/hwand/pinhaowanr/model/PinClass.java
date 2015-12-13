package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hwand.pinhaowanr.utils.NonProguard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dxz on 15/12/6.
 */
public class PinClass implements NonProguard {
    private String url;
    private String subtitle;
    private String className;
    private String detailAddress;
    private int id;
    private int roleID;
    private String createRoleURL;
    private String createRoleName;
    private String pinTime;
    private int maxRole;
    private int currentRole;
    private int money;
    private String title;

    private List<PinClassPeopleModel> attendList;

    public static List<PinClass> arrayFromData(String str) {

        Type listType = new TypeToken<ArrayList<PinClass>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getCreateRoleURL() {
        return createRoleURL;
    }

    public void setCreateRoleURL(String createRoleURL) {
        this.createRoleURL = createRoleURL;
    }

    public String getCreateRoleName() {
        return createRoleName;
    }

    public void setCreateRoleName(String createRoleName) {
        this.createRoleName = createRoleName;
    }

    public String getPinTime() {
        return pinTime;
    }

    public void setPinTime(String pinTime) {
        this.pinTime = pinTime;
    }

    public int getMaxRole() {
        return maxRole;
    }

    public void setMaxRole(int maxRole) {
        this.maxRole = maxRole;
    }

    public int getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(int currentRole) {
        this.currentRole = currentRole;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PinClassPeopleModel> getAttendList() {
        return attendList;
    }

    public void setAttendList(List<PinClassPeopleModel> attendList) {
        this.attendList = attendList;
    }
}
