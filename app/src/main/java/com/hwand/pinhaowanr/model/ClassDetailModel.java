package com.hwand.pinhaowanr.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/4.
 */
public class ClassDetailModel implements Serializable {
    private int maxAge;
    private int minAge;
    private String institutionName;
    private String busineTime;
    private String telephone;

    private List<ClassDetailTitleModel> titleList;
    private List<ClassDetailSubTitleModel> subTimeList;

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getBusineTime() {
        return busineTime;
    }

    public void setBusineTime(String busineTime) {
        this.busineTime = busineTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<ClassDetailTitleModel> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<ClassDetailTitleModel> titleList) {
        this.titleList = titleList;
    }

    public List<ClassDetailSubTitleModel> getSubTimeList() {
        return subTimeList;
    }

    public void setSubTimeList(List<ClassDetailSubTitleModel> subTimeList) {
        this.subTimeList = subTimeList;
    }
}
