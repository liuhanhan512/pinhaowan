package com.hwand.pinhaowanr.location;

import android.content.Context;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by hanhanliu on 15/7/7.
 */
public class POIValueFetch {
    private static POIValueFetch mPOIValueFetch;
    private Context mContext;

    private GeocodeSearch mGeocoderSearch;

    private POIValueFetch(Context context){
        mContext = context;
        init();
    }
    private void init(){
        mGeocoderSearch = new GeocodeSearch(mContext);
    }
    public static POIValueFetch newInstance(Context context) {
        if (mPOIValueFetch != null) {
            return mPOIValueFetch;
        } else {
            return new POIValueFetch(context);
        }
    }

    public interface OnPOIRegeoCodeFetchCallBack {

        public void onfail(String info, int errorCode);

        /**
         * RegeocodeAddress address = result.getRegeocodeAddress();
         *
         * StreetNumber strnum = address.getStreetNumber(); strnum里面包含经纬度等很多信息
         * address.getFormatAddress()，标准的时间地点
         *
         * @param result
         */
        public void onSuccess(RegeocodeResult result);

    }

    /**
     * 逆地理位置编码,根据经纬度返回最近的那个POI点点信息
     * @param point 地理位置
     * @param diatance 范围是多少米
     * @param callback 回调
     */
    public  void regecodeLatLng(LatLng point,final int diatance, final OnPOIRegeoCodeFetchCallBack callback) {

        mGeocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (callback == null) {
                    return;
                }
                switch (rCode) {
                    case 0:
                        callback.onSuccess(result);
                        break;
                    case 27:
                        callback.onfail("network exception", rCode);
                        break;
                    case 32:
                        callback.onfail("key invalid", rCode);
                        break;
                    default:
                        callback.onfail("unkown exception", rCode);
                        break;
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {

            }
        });

        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(point.latitude, point.longitude), diatance, GeocodeSearch.AMAP);
        mGeocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

}
