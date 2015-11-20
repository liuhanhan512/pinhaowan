package com.hwand.pinhaowanr;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;
import java.util.List;

public class MainApplication extends Application {

    private static long mLastTimeStamp = 0;
    private static final int THRESHOLD_RETURN2MAIN = 1000;
    private static MainApplication sMainApplication = null;

    private static final int CACHE_DEFAULT_IMAGE_NUM = 100;
    private static final int ONE_K = 1024;
    private static final int ONE_M = 1024 * ONE_K;
    private static final int CACHE_DEFAULT_SIZE = 50 * ONE_M;

    private List<Activity> mActivityStack = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (sMainApplication == null) {
            sMainApplication = this;
        }
        Context context = getApplicationContext();

        initImageLoader();
        initVolley();

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
        ImageLoader.getInstance().init(config);
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
        return true;
    }

}
