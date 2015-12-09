package com.hwand.pinhaowanr.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.hwand.pinhaowanr.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by hanhanliu on 15/12/8.
 */
public class BizUtil {

    public static void share(Context context, String title, String content ,String mImgUrl, String mUrl, Bitmap bitmap, UMSocialService mController) {

        String packageName = context.getPackageName();

        String wxAppId = "wx2d0d5abbf6adbc47";
        String wxAppSecret = "450f1e91922ac95d79ced27ba14b4f06";
        UMWXHandler wxHandler = new UMWXHandler(context, wxAppId, wxAppSecret);
        wxHandler.addToSocialSDK();
        UMWXHandler wxCircleHandler = new UMWXHandler(context, wxAppId, wxAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();

        weixinContent.setTitle(title);
        if (mImgUrl == null) {
            weixinContent.setShareImage(new UMImage(context, bitmap));
        } else {
            weixinContent.setShareContent(title);
            weixinContent.setShareImage(new UMImage(context, mImgUrl));
        }
        weixinContent.setTargetUrl(mUrl);
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        if (mImgUrl == null) {
            circleMedia.setShareImage(new UMImage(context, bitmap));
        } else {
            circleMedia.setShareContent(content);
            circleMedia.setTitle(title);
            circleMedia.setShareImage(new UMImage(context, mImgUrl));
        }


        circleMedia.setTargetUrl(mUrl);
        mController.setShareMedia(circleMedia);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

        SinaShareContent sinaShareContent = new SinaShareContent();
        sinaShareContent.setShareContent(content);
        sinaShareContent.setTitle(title);
        if (mImgUrl == null) {
            sinaShareContent.setShareImage(new UMImage(context, bitmap));
        } else {
            sinaShareContent.setShareImage(new UMImage(context, mImgUrl));
        }

        mController.setShareMedia(sinaShareContent);

        String qqAppId = "1104728842";
        String qqSecret = "9VJI50pGqxXu4VCX";
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context, qqAppId,
                qqSecret);
        qqSsoHandler.addToSocialSDK();
        QQShareContent qqShareContent = new QQShareContent();

        qqShareContent.setTitle(title);
        if (mImgUrl == null) {
            qqShareContent.setShareImage(new UMImage(context, bitmap));
        } else {
            qqShareContent.setShareContent(title);
            qqShareContent.setShareImage(new UMImage(context, mImgUrl));
        }

        qqShareContent.setTargetUrl(mUrl);
        mController.setShareMedia(qqShareContent);

        //qq空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity)context, qqAppId,
                qqSecret);
        qZoneSsoHandler.addToSocialSDK();
        if (mImgUrl == null) {
            //设置显示平台
            mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
            //设置显示顺序
            mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
        } else {
            mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
            final QZoneShareContent qzone = new QZoneShareContent();
            qzone.setShareContent(title);
            qzone.setTargetUrl(mUrl);
            qzone.setTitle(title);
            qzone.setShareImage(new UMImage(context, bitmap));
            mController.setShareMedia(qzone);
            mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
        }


        //微信回调
        SocializeListeners.SnsPostListener mSnsPostListener = new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int stCode,
                                   SocializeEntity entity) {
                if (stCode == 200) {
                    AndTools.showToast("分享成功");

                } else {
                    AndTools.showToast("分享失败");
                }
            }
        };

        mController.registerListener(mSnsPostListener);

    }
}
