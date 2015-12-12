package com.alibaba.laiwang.tide.share.business.excutor.lw;

import android.content.Context;

import com.alibaba.laiwang.tide.share.business.excutor.ShareListener;
import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.laiwang.sdk.message.IILWMessage;
import com.laiwang.sdk.message.LWMessageImage;
import com.laiwang.sdk.openapi.ILWAPI;
import com.laiwang.sdk.openapi.LWAPIDefine;
import com.laiwang.sdk.openapi.LWAPIFactory;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;


/**
 * 来往业务
 * @author bingbing
 *
 */
public class LWExecutor {
	
	private Context mContext;
	private Constants mConstants;
    private ILWAPI mILWAPI;
    private ShareListener mShareListener;
    private static List<String> mSupportImageFormat = new ArrayList<String>();
    static {
        mSupportImageFormat.add(".JGP");
        mSupportImageFormat.add(".JPEG");
        mSupportImageFormat.add(".PNG");
        mSupportImageFormat.add(".GIF");
        mSupportImageFormat.add(".BMP");
        mSupportImageFormat.add(".WBMP");
    }
	public LWExecutor(Context mContext, Constants constans){
		this.mContext=mContext;
		this.mConstants=constans;
        init();
	}
    private void init(){
        mILWAPI = LWAPIFactory.createLWAPI(mContext, mConstants.getLWToken(), mConstants.getLWSercetID(),
                LWAPIDefine.LW_SHARE_API_20130101, mConstants.getPackageName(),mConstants.getAppName());

        mILWAPI.registCallback(new ILWAPI.IILaiwangApiCallback() {
            @Override
            public int onResponceAnswer(int ret) {
                switch (ret){
                    case 0:
                        if(mShareListener != null){
                            mShareListener.onSuccess();
                        }
                        break;
                    default:
                        if(mShareListener != null){
                            mShareListener.onException("ret="+ret);
                        }
                        break;
                }
                return super.onResponceAnswer(ret);
            }
        });
    }

    /**
     * 分享纯文本内容到来往好友
     * @param content
     */
    public void shareTextToFriend(String content){
        IILWMessage iLwMessageText = LWAPIFactory.createTextMessage(content, LWAPIDefine.LW_SHARE_TYPE_SMS);
        mILWAPI.transactData(mContext,iLwMessageText, LWAPIDefine.LW_SHARE_API_1111);
    }

    /**
     * 分享纯文本到来往动态
     * @param content
     */
    public void shareTextToDynamic(String content){
        IILWMessage iLwMessageText = LWAPIFactory.createTextMessage(content, LWAPIDefine.LW_SHARE_TYPE_DYNAMIC2);
        mILWAPI.transactData(mContext,iLwMessageText, LWAPIDefine.LW_SHARE_API_1111);
    }
    /**
     * 分享本地图片到来往好友
     */
    public void shareLocalImageToFriend(String filePath,String msgTitle, String msgContent, String msgChat, String msgLinkUrl,String msgSource){
        LWMessageImage lwmsgImage = createLocalImageMessage(filePath);

        IILWMessage iLWMessage = LWAPIFactory
                .createComMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        msgLinkUrl,
                        lwmsgImage,
                        msgSource,
                        LWAPIDefine.LW_SHARE_TYPE_SMS);

        mILWAPI.transactData(mContext, iLWMessage, LWAPIDefine.LW_SHARE_API_1111);

    }
    /**
     * 分享本地图片到来往好友
     */
    public void shareLocalImageToDynamic(String filePath,String msgTitle, String msgContent, String msgChat, String msgLinkUrl,String msgSource){
        LWMessageImage lwmsgImage = createLocalImageMessage(filePath);

        IILWMessage iLWMessage = LWAPIFactory
                .createComMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        msgLinkUrl,
                        lwmsgImage,
                        msgSource,
                        LWAPIDefine.LW_SHARE_TYPE_DYNAMIC2);

        mILWAPI.transactData(mContext, iLWMessage, LWAPIDefine.LW_SHARE_API_1111);

    }
    private LWMessageImage createLocalImageMessage(String path){
        checkImageFormatlegal(path);
        LWMessageImage lwmsgImage = LWAPIFactory.creatImageMessage(
                LWMessageImage.IMAGE_TYPE_PATH,
                null,
                path,
                null);
        return lwmsgImage;
    }
    private void checkImageFormatlegal(String path){
        if(!mSupportImageFormat.contains(getFileType(path))){
            throw new IllegalArgumentException("invalide image format!");
        }
    }
    private String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex).toUpperCase();
                return fileType;
            }
        }
        return "";
    }

    /**
     * 分享图片url到来往好友
     * @param picUrl
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgLinkUrl
     * @param msgSource
     */
    public void shareImageUrlToFriend(String picUrl,String msgTitle,
                                      String msgContent,String msgChat,
                                      String msgLinkUrl,String msgSource,
                                      final ShareListener shareListener){
        mShareListener = shareListener;
        LWMessageImage lwmsgImage = LWAPIFactory
                .creatImageMessage(
                        LWMessageImage.IMAGE_TYPE_URL,
                        picUrl,
                        null, null);

        IILWMessage iLWMessage = LWAPIFactory
                .createComMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        msgLinkUrl,
                        lwmsgImage,
                        msgSource,
                        LWAPIDefine.LW_SHARE_TYPE_SMS);

        mILWAPI.transactData(mContext, iLWMessage, LWAPIDefine.LW_SHARE_API_1111);

    }
    /**
     * 分享图片url到来往动态
     * @param picUrl
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgLinkUrl
     * @param msgSource
     */
    public void shareImageUrlToDynamic(String picUrl,String msgTitle,
                                       String msgContent, String msgChat,
                                       String msgLinkUrl,String msgSource,
                                       final ShareListener shareListener){
        mShareListener = shareListener;
        LWMessageImage lwmsgImage = LWAPIFactory
                .creatImageMessage(
                        LWMessageImage.IMAGE_TYPE_URL,
                        picUrl,
                        null, null);

        IILWMessage iLWMessage = LWAPIFactory
                .createComMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        msgLinkUrl,
                        lwmsgImage,
                        msgSource,
                        LWAPIDefine.LW_SHARE_TYPE_DYNAMIC2);

        mILWAPI.transactData(mContext, iLWMessage, LWAPIDefine.LW_SHARE_API_1111);

    }

    /**
     * 分享音乐到来往好友
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgPicture
     * @param msgDescription
     * @param msgThumbnail
     * @param msgExtra
     * @param msgPlaylink
     * @param msgLink
     * @param msgSource
     * @param msgDuration
     * @param msgFlag
     */
    public void shareMusicToFriend(String msgTitle,String msgContent,String msgChat,String msgPicture, String msgDescription, String msgThumbnail,
                                   String msgExtra, String msgPlaylink, String msgLink,
                                   String msgSource, double msgDuration, String msgFlag){
        IILWMessage iLwMessageMedia = LWAPIFactory
                .createMediaMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        LWAPIDefine.TYPE_MUSIC,
                        msgPicture,
                        msgDescription,
                        msgThumbnail,
                        msgExtra,
                        msgPlaylink,
                        msgLink,
                        msgSource, 3.4, msgFlag, LWAPIDefine.LW_SHARE_TYPE_SMS);

        mILWAPI.transactData(mContext, iLwMessageMedia, LWAPIDefine.LW_SHARE_API_1111);
    }
    /**
     * 分享音乐到来往动态
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgPicture
     * @param msgDescription
     * @param msgThumbnail
     * @param msgExtra
     * @param msgPlaylink
     * @param msgLink
     * @param msgSource
     * @param msgDuration
     * @param msgFlag
     */
    public void shareMusicToDynamic(String msgTitle,String msgContent,String msgChat,String msgPicture, String msgDescription, String msgThumbnail,
                                   String msgExtra, String msgPlaylink, String msgLink,
                                   String msgSource, double msgDuration, String msgFlag){
        IILWMessage iLwMessageMedia = LWAPIFactory
                .createMediaMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        LWAPIDefine.TYPE_MUSIC,
                        msgPicture,
                        msgDescription,
                        msgThumbnail,
                        msgExtra,
                        msgPlaylink,
                        msgLink,
                        msgSource, msgDuration, msgFlag, LWAPIDefine.LW_SHARE_TYPE_DYNAMIC2);

        mILWAPI.transactData(mContext, iLwMessageMedia, LWAPIDefine.LW_SHARE_API_1111);
    }

    /**
     * 分享视频到来往好友
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgPicture
     * @param msgDescription
     * @param msgThumbnail
     * @param msgExtra
     * @param msgPlaylink
     * @param msgLink
     * @param msgSource
     * @param msgDuration
     * @param msgFlag
     */
    public void shareVideoToFriend(String msgTitle,String msgContent,String msgChat,String msgPicture, String msgDescription, String msgThumbnail,
                                   String msgExtra, String msgPlaylink, String msgLink,
                                   String msgSource, double msgDuration, String msgFlag){
        IILWMessage iLwMessageMedia = LWAPIFactory
                .createMediaMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        LWAPIDefine.TYPE_VIDEO,
                        msgPicture,
                        msgDescription,
                        msgThumbnail,
                        msgExtra,
                        msgPlaylink,
                        msgLink,
                        msgSource, msgDuration, msgFlag,LWAPIDefine.LW_SHARE_TYPE_SMS);

        mILWAPI.transactData(mContext, iLwMessageMedia, LWAPIDefine.LW_SHARE_API_1111);
    }
    /**
     * 分享视频到来往动态
     * @param msgTitle
     * @param msgContent
     * @param msgChat
     * @param msgPicture
     * @param msgDescription
     * @param msgThumbnail
     * @param msgExtra
     * @param msgPlaylink
     * @param msgLink
     * @param msgSource
     * @param msgDuration
     * @param msgFlag
     */
    public void shareVideoToDynamic(String msgTitle,String msgContent,String msgChat,String msgPicture, String msgDescription, String msgThumbnail,
                                   String msgExtra, String msgPlaylink, String msgLink,
                                   String msgSource, double msgDuration, String msgFlag){
        IILWMessage iLwMessageMedia = LWAPIFactory
                .createMediaMessage(
                        msgTitle,
                        msgContent,
                        msgChat,
                        LWAPIDefine.TYPE_VIDEO,
                        msgPicture,
                        msgDescription,
                        msgThumbnail,
                        msgExtra,
                        msgPlaylink,
                        msgLink,
                        msgSource, msgDuration, msgFlag,LWAPIDefine.LW_SHARE_TYPE_DYNAMIC2);

        mILWAPI.transactData(mContext, iLwMessageMedia, LWAPIDefine.LW_SHARE_API_1111);
    }
}
