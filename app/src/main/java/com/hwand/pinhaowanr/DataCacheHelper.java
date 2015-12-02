
package com.hwand.pinhaowanr;

import com.google.gson.Gson;
import com.hwand.pinhaowanr.entity.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NonProguard;

public class DataCacheHelper implements NonProguard {

    private static final String KEY_USER_INFO = "USER_INFO";

    private UserInfo mUserInfo;

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
}
