package com.hwand.pinhaowanr.event;

import com.hwand.pinhaowanr.model.ConfigModel;

/**
 * Created by hanhanliu on 15/12/7.
 */
public class CityChooseEvent {
    public ConfigModel configModel;

    public CityChooseEvent(ConfigModel configModel){
        this.configModel = configModel;
    }
}
