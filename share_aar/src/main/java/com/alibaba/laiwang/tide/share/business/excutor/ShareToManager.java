package com.alibaba.laiwang.tide.share.business.excutor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.alibaba.laiwang.tide.share.business.excutor.lw.LWExecutor;
import com.alibaba.laiwang.tide.share.business.excutor.qq.QQExecutor;
import com.alibaba.laiwang.tide.share.business.excutor.sina.SinaExecutor;
import com.alibaba.laiwang.tide.share.business.excutor.sina.SinaWeiboExecutor;
import com.alibaba.laiwang.tide.share.business.excutor.wx.WeiXinExecutor;


public class ShareToManager {
	private static ShareToManager manager;
	private Constants mConstants;
	private Context mContext;
	private SinaWeiboExecutor mSinaWeiboExecutor;
	private QQExecutor mQQExecutor;

	private ShareToManager(Context mContext, Constants mConstants){
		if (mConstants==null) {
			throw new IllegalArgumentException("constans 不能为null");
		}
		this.mContext=mContext;
		this.mConstants=mConstants;
	}
	/**
	 * 初始化
	 * @param context
	 * @param constants
	 */
	public static void init(Context context, Constants constants){
		if (manager==null) {
			manager=new ShareToManager(context, constants);
		}
	}
	public static ShareToManager getInstance(){
		return manager;
	}
	
	public void authorizeCallBack(int requestCode, int resultCode, Intent data){
//		getSinaExecutor().authorizeCallBack(requestCode, resultCode, data);
	}
	
	public void handleWeiboResponse(Intent intent ){
//		getSinaExecutor().handleWeiboResponse(intent);
	}
	
	public SinaWeiboExecutor getSinaExecutor(Context context){

		/**
		if (mSinaWeiboExecutor == null) {
			mSinaWeiboExecutor = new SinaWeiboExecutor(context,mConstants);
		}

		return mSinaWeiboExecutor;
		 */
        //为了保证不引起内存泄露，这里每次获取都重新生成一个对象。因为context不是application的
		return new SinaWeiboExecutor(context,mConstants);
	}
	
	public QQExecutor getQQExecutor(Context context){

		if (mQQExecutor==null) {
			if(context instanceof Activity){
				mQQExecutor = new QQExecutor((Activity) context, mConstants);
			} else {
				throw new IllegalArgumentException("qq excutor need context as Activity");
			}
		}
		return mQQExecutor;


		/**
		if(context instanceof Activity){
			return new QQExecutor((Activity) context, mConstants);
		} else {
			throw new IllegalArgumentException("qq excutor need context as Activity");
		}
		 */
	}
	
	private WeiXinExecutor mWeiXinExecutor;
	public WeiXinExecutor getWeiXinExecutor() {
		if (mWeiXinExecutor == null) {
			mWeiXinExecutor = new WeiXinExecutor(mContext, mConstants);
		}
		return mWeiXinExecutor;
	}
	
	private LWExecutor mLwExecutor;
	public LWExecutor getLWExecutor(){
		if (mLwExecutor == null) {
			mLwExecutor=new LWExecutor(mContext, mConstants);
		}
		return mLwExecutor;
	}
}
