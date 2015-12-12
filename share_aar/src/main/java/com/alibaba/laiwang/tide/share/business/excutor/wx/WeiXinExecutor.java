package com.alibaba.laiwang.tide.share.business.excutor.wx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.laiwang.tide.share.business.Util;
import com.alibaba.laiwang.tide.share.business.excutor.ShareListener;
import com.alibaba.laiwang.tide.share.business.excutor.common.Constants;
import com.alibaba.laiwang.tide.share.business.excutor.wx.utils.WeixinUtils;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import java.io.File;
import java.net.URL;

/**
 * 注意事项： 一、setup profile from amm_manifest.xml failed错误这个日志可以忽略
 * 二、SDK协议中对分享链接时缩略图大小作了限制，大小不能超过32K，另外限制的还有title、description等参数的大小。可查看：WXMediaMessage的文档
 * 三、微信打开指定页面的时候，会在原链接后加上参数isappinstalled，即http://xxxx/?isappinstalled=[1|0]，1为已安装，0为未安装。
 * 四、title限制长度不超过512Bytes。description限制长度不超过1KB 六、若修改了Android的申请信息，如包名、签名等，在审核完成之前，会出现调不起微信的问题。不要大惊小怪噢，亲！
 * 五、如果打包机失败，考虑以下几个地方： 1、pom.xml中是否添加了libmmsdk.jar的dependency； 2、是否添加了src_share为源文件夹； 3、是否在proguard.cfg中添加了-dontwarn
 * junit.**。 六、如果打包成功，但调用微信分享失败，考虑以下几个地方：1、是否在proguard.cfg中添加了微信混淆配置；2、线上打出的正式包要用微信的正式AppId，开发包要用开发AppId。
 * 
 * http://open.weixin.qq.com/document/gettingstart/android/?lang=zh_CN
 * 
 * @author shiqi
 * 
 */
public class WeiXinExecutor {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private static final int THUMB_SIZE = 150;

    private ShareListener mShareListener;

    private IWXAPI mAPI;
    public WeiXinExecutor(Context context, Constants mConstants) {
        mAPI = WXAPIFactory.createWXAPI(context, mConstants.getWXAppID(), true);
        mAPI.registerApp(mConstants.getWXAppID());
    }
    public IWXAPI getWXAPI(){
    	return mAPI;
    }
    public boolean isWXAppSupportSession() {
        return mAPI.isWXAppInstalled() && mAPI.isWXAppSupportAPI();
    }

    public boolean isWXAppSupportTimeline() {
        return mAPI.isWXAppInstalled() && mAPI.isWXAppSupportAPI()
                && mAPI.getWXAppSupportAPI() >= TIMELINE_SUPPORTED_VERSION;
    }

    /**
     * 无论是对话，还是朋友圈中，都只显示text字段，如果text中有链接，链接在对话和朋友圈中都可点击。
     * 
     * @param title
     *            这个字段传null就好，没有目前微信的SDK没有用到。
     * @param text
     *            要分享的文本。
     * @param isTimeline
     *            分享至微信聊天，还是朋友圈。true:分享至朋友圈;false:分享至微信聊天
     * @author lianbing.klb
     */
    public void doShareText(String title, String text, boolean isTimeline , final ShareListener shareListener) {

        if (text == null || text.length() == 0) {
            return;
        }

        mShareListener = shareListener;

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        mAPI.sendReq(req);
    }

    /**
     * 发送app文件本地路径
     * @param path
     * @param extInfo
     * @param title
     * @param content
     * @param isTimeLine 是否发送到朋友圈
     */
    public void doShareAppData(String path ,String extInfo , String title , String content , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        final WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.filePath = path;
        appdata.extInfo = extInfo;

        final WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(Util.extractThumbNail(path, THUMB_SIZE, THUMB_SIZE, true));
        msg.title = title;
        msg.description = content;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送app二进制数据
     * @param fileData app 二进制数据
     * @param thumbImage app 缩略图
     * @param extInfo
     * @param title
     * @param content
     * @param isTimeLine 是否发布到微信朋友圈
     */
    public void doShareByteAppData(byte[] fileData ,final Bitmap thumbImage , String extInfo , String title , String content , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        final WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.fileData = fileData;
        appdata.extInfo = extInfo;

        final WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(thumbImage);
        msg.title = title;
        msg.description = content;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送无附件的app
     * @param extInfo
     * @param title
     * @param content
     * @param isTimeLine 是否发布到微信朋友圈
     */
    public void doShareNoAttachmentApp(String extInfo , String title , String content , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        final WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.extInfo = extInfo;
        final WXMediaMessage msg = new WXMediaMessage();
        msg.title = title;
        msg.description = content;
        msg.mediaObject = appdata;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("appdata");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送图片的二进制数据
     * @param bitmap
     * @param isTimeLine
     */
    public void doShareByteImage(Bitmap bitmap , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送图片的本地路径
     * @param path
     * @param isTimeLine
     */
    public void doShareLocalImage(String path , boolean isTimeLine , final ShareListener shareListener){

        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        mShareListener = shareListener;
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);
    }

    /**
     * 发送图片的URL
     * @param url
     * @param isTimeLine
     */
    public void doShareURLImage(String url , boolean isTimeLine , final  ShareListener shareListener){
        mShareListener = shareListener;
        try{
            WXImageObject imgObj = new WXImageObject();
            imgObj.imageUrl = url;

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            mAPI.sendReq(req);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送音乐url
     * @param musicUrl
     * @param title
     * @param content
     * @param thumbBitmap
     * @param thumbData
     * @param isTimeLine
     */
    public void doShareMusic(String musicUrl , String title , String content , Bitmap thumbBitmap ,byte[] thumbData , boolean isTimeLine , final ShareListener shareListener) {
        mShareListener = shareListener;
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title ;
        msg.description = content;

        if( thumbData.length > 0){
            msg.thumbData = thumbData ;
        } else if (thumbBitmap != null){
            msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送低带宽音乐url
     * @param musicUrl
     * @param title
     * @param content
     * @param thumbBitmap
     * @param thumbData
     * @param isTimeLine
     */
    public void doShareLowBandMusic(String musicUrl , String title , String content , Bitmap thumbBitmap , byte[] thumbData , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        WXMusicObject music = new WXMusicObject();
        music.musicLowBandUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = content;

        if( thumbData.length > 0){
            msg.thumbData = thumbData ;
        } else if (thumbBitmap != null){
            msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送视频url
     * @param videoUrl
     * @param title
     * @param content
     * @param thumbBitmap
     * @param thumbData
     * @param isTimeLine
     */
    public void doShareVideo(String videoUrl , String title , String content , Bitmap thumbBitmap , byte[] thumbData , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = content;
        if( thumbData.length > 0){
            msg.thumbData = thumbData ;
        } else if (thumbBitmap != null){
            msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);

    }

    /**
     * 发送低带宽视频url
     * @param videoUrl
     * @param title
     * @param content
     * @param thumbBitmap
     * @param thumbData
     * @param isTimeLine
     */
    public void doShareLowBandVideo(String videoUrl , String title , String content , Bitmap thumbBitmap , byte[] thumbData , boolean isTimeLine , final ShareListener shareListener){
        mShareListener = shareListener;
        WXVideoObject video = new WXVideoObject();
        video.videoLowBandUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = content;
        if( thumbData.length > 0){
            msg.thumbData = thumbData ;
        } else if (thumbBitmap != null){
            msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);
    }
    /**
     * 分享链接。在对话中，显示title，pic，description三个元素。在朋友圈中，显示title、pic两个元素。
     * 
     * @param title
     * @param text
     *            朋友圈中不显示。
     *            缩略图的本地路径
     * @param url
     *            链接的地址
     * @param isTimeline
     *            分享至微信聊天，还是朋友圈。true:分享至朋友圈;false:分享至微信聊天
     * @author lianbing.klb
     */
    public void doShareHypeLink(String title, String text, Bitmap thumb ,byte[]  thumbByte, String url, boolean isTimeline , final ShareListener shareListener) {
        mShareListener = shareListener;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;  
        msg.description = text;

        if (thumbByte!=null) {
			msg.thumbData = thumbByte;
		}else {
			msg.setThumbImage(thumb);
		}
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WeixinUtils.buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mAPI.sendReq(req);


    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void callback(BaseResp resp){
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (mShareListener!=null) {
                    mShareListener.onSuccess();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (mShareListener!=null) {
                    mShareListener.onCancel();
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (mShareListener!=null) {
                    mShareListener.onException(null);
                }
                break;
            default:
                break;
        }
        mShareListener = null;
    }
}
