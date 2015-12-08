
package com.hwand.pinhaowanr;

import com.google.gson.Gson;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NonProguard;

import java.util.ArrayList;
import java.util.List;

public class DataCacheHelper implements NonProguard {

    public static final String KEY_USER_INFO = "USER_INFO";
    public static final String KEY_CONFIG = "CONFIG";
    public static final String KEY_CURRENT_CITY_CODE = "KEY_CURRENT_CITY_CODE";


    public void setUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    private UserInfo mUserInfo;

    private String cityType;

    private List<ConfigModel> mConfigModels = new ArrayList<ConfigModel>();

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

    public List<ConfigModel> getConfigModel(){
        try {
            String config = AndTools.getCurrentData(MainApplication.getInstance(), KEY_CONFIG);
            Gson gson = new Gson();
            List<ConfigModel> configModels = ConfigModel.arrayHomePageModelFromData(config);
            if(configModels != null){
                mConfigModels.clear();
                mConfigModels.addAll(configModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mConfigModels;
    }

    public String getCityType(){
        return cityType;
    }

    public void setCityType(String cityType){
        this.cityType = cityType;
    }
}
