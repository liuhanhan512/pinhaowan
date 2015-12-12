package com.alibaba.laiwang.tide.share.business.excutor.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.alibaba.laiwang.tide.share.business.excutor.ShareListener;
import com.alibaba.laiwang.tide.share.business.excutor.sina.models.StatusesAPI;
import com.alibaba.laiwang.tide.share.business.excutor.sina.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.ProvideMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.ProvideMultiMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 这个现在已经废弃不用，具体参考@link SinaWeiboExecutor
 */
public class SinaExecutor {
	private Context mContext;
	private Constants mConstants;
	/** 微博 Web 授权类，提供登陆等功能  */
	private WeiboAuth mWeiboAuth;
	
	 /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI  mWeiboShareAPI = null;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
	public SinaExecutor (Context mContext, Constants mConstants){
		mWeiboAuth = new WeiboAuth(mContext, mConstants.getSInaAppKey(), mConstants.getSinaRedirectRrl(), mConstants.getSinaScope());
		this.mConstants = mConstants;
		this.mContext = mContext;
	}
    public void callback(BaseResponse baseResp){
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                if(listener!=null){
                    listener.onSuccess();
                }
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                if(listener!=null){
                    listener.onCancel();
                }
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                if(listener!=null){
                    listener.onException(baseResp.errMsg);
                }
                break;
        }
        listener=null;
    }
	/**
	 * 此处必须为 Activity，不能为 Context   
	 * 如果已经 OAuth 过，并且 token 未失效，则不会跳转到OAuth 直接返回成功
     * 微博认证授权回调。
     * 1. SSO 授权时，需要在 {@link #} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
	 */
	public void doOAuth(Activity activity,final ShareListener listener) {
		if (checkToken()) {
			if (listener!=null) {
				listener.onSuccess();
			}
			return ;
		}
		mSsoHandler = new SsoHandler(activity, mWeiboAuth);
        mSsoHandler.authorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				if (listener!=null) {
					listener.onException(arg0.getMessage());
				}
			}
			
			@Override
			public void onComplete(Bundle values) {
	            // 从 Bundle 中解析 Token
	            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {
	                // 保存 Token 到 SharedPreferences
	                AccessTokenKeeper.writeAccessToken(mContext, mConstants.getUserID(), mAccessToken);
	            	if (listener!=null) {
	            		listener.onSuccess();
					}
	            } else {
	                // 以下几种情况，您会收到 Code：
	                // 1. 当您未在平台上注册的应用程序的包名与签名时；
	                // 2. 当您注册的应用程序包名与签名不正确时；
	                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
	                String code = values.getString("code");
	            	if (listener!=null) {
	            		listener.onException(code);
					}
	            }
	        }
			
			@Override
			public void onCancel() {
				if (listener!=null) {
					listener.onCancel();
				}
			}
		});
	}
	
	/**
	 * 分享到新浪微博，不唤起微博，不需要检测微博是否安装，但是要提前 OAuth
	 * @param content  		分享到新浪微博的内容
	 * @param bitmap			分享的图片
	 * @param imageUrl		分享的网络图片  必须以"http" 开头    如果bitmap 不为null 则此参数无效  
	 * @param listener
	 */
	public void doShare(String content, Bitmap bitmap ,String imageUrl ,final ShareListener listener){
		if (!checkToken() ) {
			if (listener!=null) {
				listener.onException("token 无效，请重新OAuth!");
			}
			return;
		}
		
		StatusesAPI api =new StatusesAPI(AccessTokenKeeper.readAccessToken(mContext,mConstants.getUserID()));
		RequestListener mListener = new RequestListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				if (listener!=null) {
					listener.onException(arg0.getMessage());
				}
			}
			
			@Override
			public void onComplete(String arg0) {
				if (listener!=null) {
					listener.onSuccess();
				}
			}
		};
		if (bitmap!=null) {
			api.upload(content, bitmap, null, null, mListener);
		}else {
			api.uploadUrlText(content, imageUrl, null, null, null, mListener);
		}
	}
	
	/**
	 * 检测token 是否有效
	 * @return
	 */
	public boolean checkToken(){
		if (mAccessToken==null) {
			mAccessToken = AccessTokenKeeper.readAccessToken(mContext,mConstants.getUserID());
		}
		System.out.println(System.currentTimeMillis());
		if ( mAccessToken !=null && mAccessToken.isSessionValid()) {
			return true;
		}
		return false;
	}
	/**
	 * OAuth 的回调
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authorizeCallBack(int requestCode, int resultCode, Intent data){
		if (mSsoHandler!=null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	/**
	 * 微博跳转分享后的回掉
	 * @param intent
	 * 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
	 * 来接收微博客户端返回的数据；执行成功，返回 true，并调用 
	 * 失败返回 false，不调用回调
	 */
	public void handleWeiboResponse(Intent intent ){
		if(mWeiboShareAPI ==null){
			return ;
		}
		mWeiboShareAPI.handleWeiboResponse(intent, new Response() {
			
			@Override
			public void onResponse(BaseResponse baseResp) {
				switch (baseResp.errCode) {
		        case WBConstants.ErrorCode.ERR_OK:
		        	if(listener!=null){
		        		listener.onSuccess();
		        		listener=null;
		        	}
		            break;
		        case WBConstants.ErrorCode.ERR_CANCEL:
		        	if(listener!=null){
		        		listener.onCancel();
		        		listener=null;
		        	}
		            break;
		        case WBConstants.ErrorCode.ERR_FAIL:
		        	if(listener!=null){
		        		listener.onException(baseResp.errMsg);
		        		listener=null;
		        	}
		            break;
		        }
			}
		});
	}
	
	
	
	private ShareListener listener=null;
	/**
	 * 调用此借口会唤起新浪的微博app
	 * @param title				  分享网页的标题
	 * @param description    分享的描述
	 * @param image			 分享的图片
	 * @param thumbImage  分享网页时候的缩略图
	 * @param url				 分享网页的url ，如果url 未空，则title 还有 thumbImage 无效
	 */
	public void doShareCallApp(Context context, String title, String description,Bitmap image, Bitmap thumbImage , String url,boolean needResponse, ShareListener listener){
		 // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, mConstants.getSInaAppKey());
//        // 注册新浪微博
        mWeiboShareAPI.registerApp();
		  if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
	            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
	            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    if(needResponse){
                        doShareForRespone(title, description, image, thumbImage, url, listener);
                    }else{
                        doshare(title, description, image, thumbImage, url,listener);
                    }
	            }else {
					Toast.makeText(mContext, "微博客户端版本不支持此分享，请下载最新微博客户端", Toast.LENGTH_SHORT).show();
				}
		  }else {
			  Toast.makeText(mContext, "微博客户端版本不支持此分享，请下载最新微博客户端", Toast.LENGTH_SHORT).show();
		  }
	}
	
	
	
	private void doshare(String title, String description,Bitmap image, Bitmap thumbImage ,String url,ShareListener listener){
		   // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (!TextUtils.isEmpty(description)) {
			  TextObject textObject = new TextObject();
			  textObject.text=description;
			  weiboMessage.textObject=textObject;
		}
		if (image!=null) {
			ImageObject imageObject=new ImageObject();
			imageObject.setImageObject(image);
			weiboMessage.imageObject=imageObject;
		}
		
		if (!TextUtils.isEmpty(url)) {
			
			    WebpageObject mediaObject = new WebpageObject();
		        mediaObject.identify = Utility.generateGUID();
		        mediaObject.title = title;
		        mediaObject.description = description;
		        // 设置 Bitmap 类型的图片到视频对象里
		        mediaObject.setThumbImage(thumbImage);
		        mediaObject.actionUrl = url;
		        mediaObject.defaultText = description;
		        weiboMessage.mediaObject=mediaObject;
		}
		
		  // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        if(mWeiboShareAPI.sendRequest(request)){
        	this.listener=listener;
        }
	}
    private void doShareForRespone(String title, String description,Bitmap image, Bitmap thumbImage ,String url,ShareListener listener){
        this.listener=listener;
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (!TextUtils.isEmpty(description)) {
            TextObject textObject = new TextObject();
            textObject.text=description;
            weiboMessage.textObject=textObject;
        }
        if (image!=null) {
            ImageObject imageObject=new ImageObject();
            imageObject.setImageObject(image);
            weiboMessage.imageObject=imageObject;
        }

        if (!TextUtils.isEmpty(url)) {

            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = title;
            mediaObject.description = description;
            // 设置 Bitmap 类型的图片到视频对象里
            mediaObject.setThumbImage(thumbImage);
            mediaObject.actionUrl = url;
            mediaObject.defaultText = description;
            weiboMessage.mediaObject=mediaObject;
        }

        /**
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
         */

        // 2. 初始化从微博到第三方的消息请求
        ProvideMultiMessageForWeiboResponse response = new ProvideMultiMessageForWeiboResponse();
        response.transaction = String.valueOf(System.currentTimeMillis());
        response.reqPackageName = mContext.getPackageName();
        response.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendResponse(response);
    }
}
