package com.alibaba.laiwang.tide.share.business;

/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class ShareUnitInfo {
    private String title;
    private boolean defautCheck;
    private String pakName;
    private String value;
    private String ut;
    private int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDefautCheck() {
        return defautCheck;
    }

    public void setDefautCheck(boolean defautCheck) {
        this.defautCheck = defautCheck;
    }

    public String getPakName() {
        return pakName;
    }

    public void setPakName(String pakName) {
        this.pakName = pakName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
