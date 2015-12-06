package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/5.
 */
public class SuperMomModel {
    private int id;
    private String name;
    private long time;
    private String url;

    public static List<SuperMomModel> arrayHomePageModelFromData(String str) {

        Type listType = new TypeToken<List<SuperMomModel>>() {}.getType();

        return new Gson().fromJson(str, listType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
