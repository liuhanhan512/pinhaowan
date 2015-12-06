package com.hwand.pinhaowanr.model;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/6.
 */
public class SpellDEntity {
    private List<SpellDCategoryModel> classTypeList;
    private List<SpellDModel> pinClassList;

    public List<SpellDCategoryModel> getClassTypeList() {
        return classTypeList;
    }

    public void setClassTypeList(List<SpellDCategoryModel> classTypeList) {
        this.classTypeList = classTypeList;
    }

    public List<SpellDModel> getPinClassList() {
        return pinClassList;
    }

    public void setPinClassList(List<SpellDModel> pinClassList) {
        this.pinClassList = pinClassList;
    }
}
