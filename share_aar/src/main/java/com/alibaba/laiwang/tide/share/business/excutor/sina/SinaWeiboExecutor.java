package com.alibaba.laiwang.tide.share.business.excutor.sina;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.laiwang.tide.share.business.excutor.ShareListener;
import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 新浪微博分享的执行类
 * Created by shiqi on 15/4/28.
 */
public class SinaWeiboExecutor {

    private Context mContext;
    /** 微博 Web 授权类，提供登陆等功能  */
    private WeiboAuth mWeiboAuth;

    private static ShareListener mShareListener;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI mWeiboShareAPI = null;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    public SinaWeiboExecutor (Context context, Constants constants){

        if(context instanceof Activity){
            mWeiboAuth = new WeiboAuth(context, constants.getSInaAppKey(), constants.getSinaRedirectRrl(), constants.getSinaScope());

            // 创建微博分享接口实例
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, constants.getSInaAppKey());
//        // 注册新浪微博
            mWeiboShareAPI.registerApp();

            mContext = context;
        } else {
            throw new IllegalArgumentException("context must be Activity instance");
        }

    }

    /**
     * 分享文本
     * @param text
     */
    public void doShareText(String text ,final ShareListener shareListener){
        mShareListener = shareListener;

        TextObject textObject = getTextObj(text);

        sendSingleMessage(textObject);

    }

    /**
     * 分享图片
     * @param bitmap
     */
    public void doShareImage(Bitmap bitmap , final ShareListener shareListener){
        mShareListener = shareListener;
        ImageObject imageObject = getImageObj(bitmap);
        sendSingleMessage(imageObject);
    }

    /**
     * 分享音乐
     * @param title
     * @param content
     * @param thumbBitmap
     * @param url
     */
    public void doShareMusic(String title , String content , Bitmap thumbBitmap , String url , final ShareListener shareListener){
        mShareListener = shareListener;
        // 创建媒体消息
        MusicObject musicObject = getMusicObj(title, content, thumbBitmap, url);
        sendSingleMessage(musicObject);

    }

    /**
     * 分享视频
     * @param title
     * @param content
     * @param thumbBitmap
     * @param url
     */
    public void doShareVideo(String title , String content , Bitmap thumbBitmap , String url , final ShareListener shareListener){
        mShareListener = shareListener;
        // 创建媒体消息
        VideoObject videoObject = getVideoObj(title, content, thumbBitmap, url);
        sendSingleMessage(videoObject);
    }

    /**
     * 分享voice
     * @param title
     * @param content
     * @param thumbBitmap
     * @param url
     */
    public void doShareVoice(String title , String content , Bitmap thumbBitmap , String url , final ShareListener shareListener){
        mShareListener = shareListener;
        // 创建媒体消息
        VoiceObject voiceObject = getVoiceObj(title, content, thumbBitmap, url);
        sendSingleMessage(voiceObject);
    }

    /**
     * 分享link到微博
     * @param title
     * @param content
     * @param thumbBitmap
     * @param linkUrl
     */
    public void doShareLink(String title , String content , Bitmap thumbBitmap , String linkUrl , final ShareListener shareListener){
        mShareListener = shareListener;

        WebpageObject mediaObject = getWebpageObj(title, content, thumbBitmap, linkUrl);
        sendSingleMessage(mediaObject);
    }

    /**
     * 分送基本的图文混排的内容
     * @param text
     * @param bitmap
     */
    public void doShareBaseMultiMessage(String text , Bitmap bitmap , final ShareListener shareListener){
        mShareListener = shareListener;

        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj(text);
            weiboMessage.imageObject = getImageObj(bitmap);

            sendMultiMessage(weiboMessage);

        } else {
            Log.e("SinaWeiboExecutor", "the version of weibo is low , so not support multi message,please update weibo client");
        }
    }

    /**
     * 分享支持图文混排 并且附带link的消息
     * @param text
     * @param bitmap
     * @param linkTitle
     * @param linkContent
     * @param linkThumbBitmap
     * @param linkUrl
     */
    public void doShareMultiAttachLink(String text , Bitmap bitmap , String linkTitle , String linkContent , Bitmap linkThumbBitmap , String linkUrl , final ShareListener shareListener){
        mShareListener = shareListener;
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj(text);
            weiboMessage.imageObject = getImageObj(bitmap);
            weiboMessage.mediaObject = getWebpageObj(linkTitle, linkContent, linkThumbBitmap, linkUrl);
            sendMultiMessage(weiboMessage);

        } else {
            Log.e("SinaWeiboExecutor", "the version of weibo is low , so not support multi message,please update weibo client");
        }
    }

    /**
     * 分享复合带音乐的消息
     * @param text
     * @param bitmap
     * @param musicTitle
     * @param musicContent
     * @param musicThumbBitmap
     * @param musicUrl
     */
    public void doShareMultiAttachMusic(String text , Bitmap bitmap , String musicTitle,String musicContent,Bitmap musicThumbBitmap,String musicUrl , final ShareListener shareListener){
        mShareListener = shareListener;
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj(text);
            weiboMessage.imageObject = getImageObj(bitmap);
            weiboMessage.mediaObject = getMusicObj(musicTitle, musicContent, musicThumbBitmap, musicUrl);
            sendMultiMessage(weiboMessage);

        } else {
            Log.e("SinaWeiboExecutor", "the version of weibo is low , so not support multi message,please update weibo client");
        }
    }

    /**
     * 分享复合带视频的消息
     * @param text
     * @param bitmap
     * @param videoTitle
     * @param videoContent
     * @param videoThumbBitmap
     * @param videoUrl
     */
    public void doShareMultiAttachVideo(String text , Bitmap bitmap , String videoTitle,String videoContent,Bitmap videoThumbBitmap,String videoUrl , final ShareListener shareListener){
        mShareListener = shareListener;
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj(text);
            weiboMessage.imageObject = getImageObj(bitmap);
            weiboMessage.mediaObject = getVideoObj(videoTitle, videoContent, videoThumbBitmap, videoUrl);
            sendMultiMessage(weiboMessage);

        } else {
            Log.e("SinaWeiboExecutor", "the version of weibo is low , so not support multi message,please update weibo client");
        }
    }

    /**
     * 分享复合带语音的消息
     * @param text
     * @param bitmap
     * @param voiceTitle
     * @param voiceContent
     * @param voiceThumbBitmap
     * @param voiceUrl
     */
    public void doShareMultiAttachVoice(String text , Bitmap bitmap , String voiceTitle,String voiceContent,Bitmap voiceThumbBitmap,String voiceUrl , final ShareListener shareListener){
        mShareListener = shareListener;
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj(text);
            weiboMessage.imageObject = getImageObj(bitmap);
            weiboMessage.mediaObject = getVoiceObj(voiceTitle, voiceContent, voiceThumbBitmap, voiceUrl);
            sendMultiMessage(weiboMessage);

        } else {
            Log.e("SinaWeiboExecutor", "the version of weibo is low , so not support multi message,please update weibo client");
        }
    }
    private void sendSingleMessage(BaseMediaObject mediaObject){
        if(!isWeiboAppSupportAPI()){
            Log.e("SinaWeiboExecutor", "weibo not support sdk share or weibo is not official version.");
            return;
        }
        // 1. 初始化微博的分享消息
        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = mediaObject;
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面

        mWeiboShareAPI.sendRequest(request);

    }
    private void sendMultiMessage(WeiboMultiMessage multiMessage){
        if(!isWeiboAppSupportAPI()){
            Log.e("SinaWeiboExecutor", "weibo not support sdk share or weibo is not official version.");
            return;
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = multiMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }
    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String title , String content , Bitmap thumbBitmap , String linkUrl) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = content;

        // 设置 Bitmap 类型的图片到视频对象里
        mediaObject.setThumbImage(thumbBitmap);
        mediaObject.actionUrl = linkUrl;
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj(String title , String content , Bitmap thumbBitmap , String url) {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = title;
        musicObject.description = content;

        // 设置 Bitmap 类型的图片到视频对象里
        musicObject.setThumbImage(thumbBitmap);
        musicObject.actionUrl = url;
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj(String title,String content,Bitmap thumbBitmap,String url) {

        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = title;
        videoObject.description = content;

        // 设置 Bitmap 类型的图片到视频对象里
        videoObject.setThumbImage(thumbBitmap);
        videoObject.actionUrl = url;

        return videoObject;
    }

    /**
     * 创建多媒体（音频）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj(String title , String content , Bitmap thumbBitmap , String url) {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = title;
        voiceObject.description = content;

        // 设置 Bitmap 类型的图片到视频对象里
        voiceObject.setThumbImage(thumbBitmap);
        voiceObject.actionUrl = url;
        return voiceObject;
    }

    private boolean isWeiboAppSupportAPI(){
        return mWeiboShareAPI.isWeiboAppSupportAPI();
    }

    public void callback(BaseResponse baseResp){
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                if(mShareListener!=null){
                    mShareListener.onSuccess();
                }
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                if(mShareListener!=null){
                    mShareListener.onCancel();
                }
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                if(mShareListener!=null){
                    mShareListener.onException(baseResp.errMsg);
                }
                break;
        }
        mShareListener=null;
    }
}
