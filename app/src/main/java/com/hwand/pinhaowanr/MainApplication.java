package com.hwand.pinhaowanr;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.event.LocationEvent;
import com.hwand.pinhaowanr.location.LocationDataFeedbackListener;
import com.hwand.pinhaowanr.location.LocationManager;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MainApplication extends Application {

    private static long mLastTimeStamp = 0;
    private static final int THRESHOLD_RETURN2MAIN = 1000;
    private static MainApplication sMainApplication = null;

    private static final int CACHE_DEFAULT_IMAGE_NUM = 100;
    private static final int ONE_K = 1024;
    private static final int ONE_M = 1024 * ONE_K;
    private static final int CACHE_DEFAULT_SIZE = 50 * ONE_M;

    private List<Activity> mActivityStack = new LinkedList<Activity>();

    private AMapLocation mAMapLocation;

    private int mCityType = 1;

    private int mMaxLocation = 3;//最多进行三次定位

    @Override
    public void onCreate() {
        super.onCreate();
        if (sMainApplication == null) {
            sMainApplication = this;
        }
        initImageLoader();
        initVolley();

        getConfig();
        getLocation();
    }

    private void initImageLoader() {
        /**
         * 开源框架 Image-Loader
         */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.null_bg) //
                .showImageOnFail(R.mipmap.null_bg) //
                .showStubImage(R.mipmap.null_bg)
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .discCacheSize(CACHE_DEFAULT_SIZE)//
                .discCacheFileCount(CACHE_DEFAULT_IMAGE_NUM)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    private void initVolley() {
        NetworkRequest.init(this);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private String getVersionName() {
        // 显示当下的版本信息
        String versionName = null;
        try {
            PackageManager pm = this.getPackageManager();
            String pkgName = this.getPackageName();
            versionName = String.valueOf(pm.getPackageInfo(pkgName,
                    PackageManager.GET_UNINSTALLED_PACKAGES).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return versionName;
    }

    private void getConfig(){
        Map<String, String> params = new HashMap<String, String>();

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_CONFIG, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!TextUtils.isEmpty(response)) {
                    DataCacheHelper.getInstance().saveConfig(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void getLocation(){
        final LocationManager locationManager = LocationManager.newInstance(this);
        locationManager.setLocationDataFeedbackListener(new LocationDataFeedbackListener() {
            @Override
            public void onReceiver(AMapLocation amapLocation) {
                mAMapLocation = amapLocation;
                calcCityType();
                locationManager.stopLocation();
                EventBus.getDefault().post(new LocationEvent());
            }

            @Override
            public void onError(AMapLocation aMapLocation) {
                locationManager.stopLocation();
                if(mMaxLocation > 0){
                    locationManager.startLocation();
                    mMaxLocation --;
                }
            }
        });
        locationManager.startLocation();
    }

    /**
     * 匹配服务端的cityType
     */
    private void calcCityType(){
        if(mAMapLocation != null){
            List<ConfigModel> configModels = DataCacheHelper.getInstance().getConfigModel();
            for(ConfigModel configModel : configModels){
                if(TextUtils.equals(configModel.getCityName() , mAMapLocation.getCity())){
                    mCityType = configModel.getCityType();
                    break;
                }
            }
        }

    }

    public AMapLocation getAmapLocation(){
        return mAMapLocation;
    }

    public int getCityType(){
        return mCityType;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void returnMainActivity(String aFrom) {
        long curtime = System.currentTimeMillis();
        if ((curtime - mLastTimeStamp) > THRESHOLD_RETURN2MAIN) {
            // TODO:
            Intent intent = new Intent("com.sogou.androidtool.action.extend");
            intent.putExtra("from", aFrom);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sMainApplication.startActivity(intent);
        }
    }


    public static void setMainActivityQuitTime(long aTime) {
        mLastTimeStamp = aTime;
    }

    public static synchronized MainApplication getInstance() {
        return sMainApplication;
    }

    public void addActivity(Activity activity) {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            if (!mActivityStack.contains(activity)) {
                mActivityStack.add(activity);
            }
        } else {
            mActivityStack.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
            }
        }
    }

    public void exit() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    public void clearActivity() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }

    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(AndTools.getCurrentData(this, NetworkRequest.SESSION_COOKIE))
                    && DataCacheHelper.getInstance().getUserInfo() != null;
    }

}
