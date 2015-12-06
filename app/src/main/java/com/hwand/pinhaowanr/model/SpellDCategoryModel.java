package com.hwand.pinhaowanr.model;

/**
 * Created by hanhanliu on 15/12/6.
 */
public class SpellDCategoryModel {
    private String className;
    private int classType;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }
}
