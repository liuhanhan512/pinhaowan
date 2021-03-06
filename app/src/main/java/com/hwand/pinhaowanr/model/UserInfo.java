package com.hwand.pinhaowanr.model;

import com.hwand.pinhaowanr.utils.NonProguard;

/**
 * Created by dxz on 2015/12/2.
 */
public class UserInfo implements NonProguard {

    /**
     * result : 0 失败(用户名或者密码错误) 1 已经登录 2 成功
     * birthday : 2015-01-01
     * familyAddress : 家庭住址
     * childSex : 0 女 1 男
     * childName : ldd
     * name : dd
     * roleId : 3
     * url : defaultHead.png
     * relation : 1 父亲 2 母亲 3 其他
     * content：个人简绍
     */

    private int result;

    private String birthday;
    private int childSex;
    private String childName;
    private String familyAddress;
    private String name;
    private String url;
    private int relation;
    private int roleId;
    private String content;

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setChildSex(int childSex) {
        this.childSex = childSex;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getResult() {
        return result;
    }

    public int getChildSex() {
        return childSex;
    }

    public String getChildName() {
        return childName;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getRelation() {
        return relation;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
