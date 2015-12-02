package com.hwand.pinhaowanr.entity;

import com.hwand.pinhaowanr.utils.NonProguard;

/**
 * Created by duanjunlei on 2015/12/2.
 */
public class UserInfo implements NonProguard {

    /**
     * result : 0 失败(用户名或者密码错误) 1 已经登录 2 成功
     * birthday : 2015-01-01
     * childSex : 0 女 1 男
     * childName : ldd
     * name : dd
     * url : defaultHead.png
     * relation : 1 父亲 2 母亲 3 其他
     */

    private int result;

    private String birthday;
    private int childSex;
    private String childName;
    private String name;
    private String url;
    private int relation;

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
}
