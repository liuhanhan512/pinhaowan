
package com.hwand.pinhaowanr;

import com.google.gson.Gson;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NonProguard;

public class DataCacheHelper implements NonProguard {

    public static final String KEY_USER_INFO = "USER_INFO";
    public static final String KEY_CONFIG = "CONFIG";

    private UserInfo mUserInfo;

    private ConfigModel mConfigModel;

    private static class SingletonHolder {
        public final static DataCacheHelper instance = new DataCacheHelper();
    }

    public static DataCacheHelper getInstance() {
        return SingletonHolder.instance;
    }

    private DataCacheHelper() {
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            try {
                String info = AndTools.getCurrentData(MainApplication.getInstance(), KEY_USER_INFO);
                Gson gson = new Gson();
                mUserInfo = gson.fromJson(info, UserInfo.class);
            } catch (Exception e) {
                mUserInfo = null;
            }
        }
        return mUserInfo;
    }

    public void saveUserInfo(String info) {
        AndTools.saveCurrentData2Cache(MainApplication.getInstance(), KEY_USER_INFO, info);
    }

    public void saveConfig(String config){
        AndTools.saveCurrentData2Cache(MainApplication.getInstance(), KEY_CONFIG, config);
    }

    public ConfigModel getConfigModel(){
        if (mConfigModel == null) {
            try {
                String config = AndTools.getCurrentData(MainApplication.getInstance(), KEY_CONFIG);
                Gson gson = new Gson();
                mConfigModel = gson.fromJson(config, ConfigModel.class);
            } catch (Exception e) {
                mConfigModel = null;
            }
        }
        return mConfigModel;
    }
}
