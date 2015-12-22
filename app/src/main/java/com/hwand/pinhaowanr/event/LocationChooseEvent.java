package com.hwand.pinhaowanr.event;

import com.amap.api.services.core.PoiItem;

/**
 * Created by hanhanliu on 15/12/22.
 */
public class LocationChooseEvent {
    private PoiItem poiItem;

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }
}
