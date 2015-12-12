package com.alibaba.laiwang.tide.share.business.excutor.qq;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;


/**
 * 分享到 QQ  QZONE   因为 QZONE 
 * @author bingbing
 *
 */
public class QQExecutor {

	private Activity mContext;
	private Tencent mTencent;

	/**
	 * 初始化分享执行器
	 * @param activity 当前activity
	 * @param mConstans
	 */
	public QQExecutor(Activity activity, Constants mConstans){
		this.mContext = activity;
		mTencent=Tencent.createInstance(mConstans.getQQAppID(), mContext);
	}

	/**
	 * 分享本地图片到QQ好友，单纯分享图片目前只支持本地图片，不支持网络图片
	 * @param path 图片本地路径
	 * @param qqShareListener 分享回调
	 */
	public void shareLocalImageToQQ(String path , final IUiListener qqShareListener){
		final Bundle params = initLocalImageBundle(path);

		mTencent.shareToQQ(mContext, params, qqShareListener);
	}

	/**
	 * 分享本地图片到QQ空间
	 * @param path 参考分享本地图片到QQ好友
	 * @param qqShareListener
	 */
	public void shareLocalImageToZone(String path , final IUiListener qqShareListener ){
		final Bundle params = initLocalImageBundle(path);

		mTencent.shareToQzone(mContext, params, qqShareListener);
	}

	/**
	 * 分享链接到QQ好友
	 * @param title 链接标题
	 * @param content 链接内容
	 * @param linkUrl 链接url
	 * @param imageUrl 链接展示图片url
	 * @param qqShareListener 分享之后的回调
	 */
	public void shareLinkToQQ(String title , String content, String linkUrl , String imageUrl , final IUiListener qqShareListener){
		final Bundle params = initLinkBundle(title,content,linkUrl,imageUrl);

		mTencent.shareToQQ(mContext, params, qqShareListener);
	}

	/**
	 * 分享链接到QQ空间
	 * @param title
	 * @param content
	 * @param linkUrl
	 * @param imageUrl
	 * @param qqShareListener
	 */
	public void shareLinkToZone(String title , String content, String linkUrl , String imageUrl , final IUiListener qqShareListener){
		final Bundle params = initLinkBundle(title,content,linkUrl,imageUrl);
		mTencent.shareToQzone(mContext, params, qqShareListener);
	}

	private Bundle initLocalImageBundle(String path){
		final Bundle params = new Bundle();
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getApplicationName());
		return params;
	}

	private Bundle initLinkBundle(String title , String content, String linkUrl , String imageUrl ){
		final Bundle params = new Bundle();
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, linkUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getApplicationName());
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

		return params;
	}

	private String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = mContext.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

}
