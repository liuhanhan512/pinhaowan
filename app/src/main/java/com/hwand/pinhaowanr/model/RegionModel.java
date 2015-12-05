package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * Created by hanhanliu on 15/12/2.
 */
public class RegionModel implements Serializable{
    private String type;
    private String typeName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
