package com.hwand.pinhaowanr.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanhanliu on 15/12/3.
 */
public class HomePageEntity implements Serializable {

    private String category;

    private int type;

    private List<HomePageModel> homePageModelList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<HomePageModel> getHomePageModelList() {
        return homePageModelList;
    }

    public void setHomePageModelList(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }
}
