package com.alibaba.laiwang.tide.share.business;

/**
 * 分享渠道的工具
 * @author bingbing
 *
 */
public class ShareChannelUtils {
	public static boolean getShow(String key) {
//        String result = BBLApplication.mPreferences.getString(ShareActionBox.shareChannelKey, null);
//        List<String> channelList = JSON.parseArray(result, String.class);
//		if (channelList !=null) {
//			return channelList.contains(key);
//		}
		return true;
	}
}
