package com.hwand.pinhaowanr.model;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/5.
 */
public class SmallPartnerModel {
    private List<SuperMomModel> roleList;
    private List<NewActivityModel> newActivityList;
    private TheCommunityActivityModel market;
    private List<TheCommunityActivityModel> naActivityList;


    public List<SuperMomModel> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SuperMomModel> roleList) {
        this.roleList = roleList;
    }

    public List<NewActivityModel> getNewActivityList() {
        return newActivityList;
    }

    public void setNewActivityList(List<NewActivityModel> newActivityList) {
        this.newActivityList = newActivityList;
    }

    public TheCommunityActivityModel getMarket() {
        return market;
    }

    public void setMarket(TheCommunityActivityModel market) {
        this.market = market;
    }

    public List<TheCommunityActivityModel> getNaActivityList() {
        return naActivityList;
    }

    public void setNaActivityList(List<TheCommunityActivityModel> naActivityList) {
        this.naActivityList = naActivityList;
    }
}
