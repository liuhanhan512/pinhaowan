package com.hwand.pinhaowanr.event;

import com.hwand.pinhaowanr.model.RegionModel;

/**
 * Created by hanhanliu on 15/12/7.
 */
public class RegionChooseEvent {
    public RegionModel regionModel;

    public RegionChooseEvent(RegionModel regionModel){
        this.regionModel = regionModel;
    }
}
