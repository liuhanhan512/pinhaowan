package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 主页--社区--拼拼--拼客列表model
 * Created by hanhanliu on 15/12/12.
 */
public class PinClassModel implements Serializable {

    public static List<PinClassModel> arrayFromData(String str) {

        Type listType = new TypeToken<List<PinClassModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    private int id;
    private int roleID;
    private String createRoleURL;
    private String createRoleName;
    private String url;
    private String title;
    private String subtitle;
    private String className;
    private String detailAddress;
    private String pinTime;
    private int maxRole;
    private int currentRole;
    private String money;

    private int spellStatus ;

    private List<PinClassPeopleModel> attendList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpellStatus() {
        return spellStatus;
    }

    public void setSpellStatus(int spellStatus) {
        this.spellStatus = spellStatus;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<PinClassPeopleModel> getAttendList() {
        return attendList;
    }

    public void setAttendList(List<PinClassPeopleModel> attendList) {
        this.attendList = attendList;
    }
}
