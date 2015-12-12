package com.hwand.pinhaowanr.share.channel;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;

public class ShareConstants implements Constants {
    private PackageInfo mPackageInfo;
    private PackageManager mPackageManager;
    public ShareConstants(final Context context){
        try {
            mPackageManager = context.getPackageManager();
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

	@Override
	public String getSInaAppKey() {
		return "3768920764";
	}

	@Override
	public String getSinaRedirectRrl() {
		return "https://api.weibo.com/oauth2/default.html";
	}

	@Override
	public String getSinaScope() {
		return "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	}

	@Override
	public String getQQAppID() {
		return "101043664";
	}

	@Override
	public String getWXAppID() {
		return "wxdaf0d0f803750315";
	}

	@Override
	public String getLWToken() {
		return "laiwang21798649";
	}

	@Override
	public String getLWSercetID() {
		return "3mc04ve21c91g9c9pd0l902x9m2n2uom";
	}

    /**
     * 应用名
     *
     * @return
     */
    @Override
    public String getAppName() {
        if(mPackageInfo != null && mPackageManager != null){
            return mPackageManager.getApplicationLabel(mPackageInfo.applicationInfo).toString();
        }
        return null;
    }

    /**
     * 获取应用package名字
     *
     * @return
     */
    @Override
    public String getPackageName() {
        if(mPackageInfo != null){
            return  mPackageInfo.packageName;
        }
        return null;
    }

    @Override
	public String getUserID() {
//		return LoginHelper.getInstance().getLoginId();
        return "";
	}


}
