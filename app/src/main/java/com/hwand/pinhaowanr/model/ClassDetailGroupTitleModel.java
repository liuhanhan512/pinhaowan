package com.hwand.pinhaowanr.model;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/8.
 */
public class ClassDetailGroupTitleModel {
    private String time;

    private List<ClassDetailSubTitleModel> classDetailSubTitleModelList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ClassDetailSubTitleModel> getClassDetailSubTitleModelList() {
        return classDetailSubTitleModelList;
    }

    public void setClassDetailSubTitleModelList(List<ClassDetailSubTitleModel> classDetailSubTitleModelList) {
        this.classDetailSubTitleModelList = classDetailSubTitleModelList;
    }
}
