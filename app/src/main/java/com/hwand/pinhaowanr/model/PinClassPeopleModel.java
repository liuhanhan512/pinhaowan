package com.hwand.pinhaowanr.model;

import java.io.Serializable;

/**
 * Created by hanhanliu on 15/12/12.
 */
public class PinClassPeopleModel implements Serializable {
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
