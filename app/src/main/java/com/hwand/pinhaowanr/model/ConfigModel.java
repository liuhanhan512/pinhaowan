package com.hwand.pinhaowanr.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/2.
 */
public class ConfigModel  implements Serializable{
    private int cityType;
    private String cityName;
    private List<RegionModel> regionMap;

    public static  List<ConfigModel> arrayHomePageModelFromData(String str) {

        Type listType = new TypeToken<List<ConfigModel>>() {}.getType();

        return new Gson().fromJson(str, listType);
    }

    public int getCityType() {
        return cityType;
    }

    public void setCityType(int cityType) {
        this.cityType = cityType;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<RegionModel> getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(List<RegionModel> regionMap) {
        this.regionMap = regionMap;
    }
}
