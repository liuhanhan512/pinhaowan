package com.hwand.pinhaowanr.model;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/2.
 */
public class ConfigModel {
    private String cityType;
    private String cityName;
    private List<RegionModel> regionMap;


    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
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
