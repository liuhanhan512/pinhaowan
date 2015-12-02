package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * Created by hanhanliu on 15/12/2.
 */
public class RegionModel implements Serializable{
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
