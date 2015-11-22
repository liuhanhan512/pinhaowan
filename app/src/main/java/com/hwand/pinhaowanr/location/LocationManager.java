package com.hwand.pinhaowanr.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

/**
 * 高德定位
 * Created by hanhanliu on 15/3/13.
 */
public class LocationManager implements AMapLocationListener {
    private LocationManagerProxy mLocationManagerProxy;
    private static LocationManager mLocationManager;
    private Context mContext;
    private LocationDataFeedbackListener mLocationDataFeedbackListener;

    private LocationManager(Context context){
        mContext = context.getApplicationContext();
        init();
    }

    public static LocationManager newInstance(Context context) {
        if (mLocationManager != null) {
            return mLocationManager;
        } else {
            return new LocationManager(context);
        }
    }
    public void setLocationDataFeedbackListener(LocationDataFeedbackListener locationDataFeedbackListener){
        mLocationDataFeedbackListener = locationDataFeedbackListener;
    }
    /**
     * 初始化定位
     */
    private void init() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
        mLocationManagerProxy.setGpsEnable(true);
    }
    public void startLocation(){
        // 移除定位请求
//		mLocationManagerProxy.removeUpdates(this);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 1, this);
    }
    public void stopLocation(){
        if(mLocationManagerProxy != null){
            // 销毁定位
            mLocationManagerProxy.destroy();
        }
        // 移除定位请求
        //mLocationManagerProxy.removeUpdates(this);

    }
    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            if(mLocationDataFeedbackListener != null){
                mLocationDataFeedbackListener.onReceiver(amapLocation);
            }
        } else if(amapLocation != null&& amapLocation.getAMapException()!=null){
            if(mLocationDataFeedbackListener != null){
                mLocationDataFeedbackListener.onError(amapLocation);
            }
        }
    }

}

