package com.hwand.pinhaowanr.location;

import com.amap.api.location.AMapLocation;

/**
 * Created by hanhanliu on 15/3/13.
 */
public interface LocationDataFeedbackListener {
    public void onReceiver(AMapLocation amapLocation);
    public void onError(AMapLocation aMapLocation);
}
