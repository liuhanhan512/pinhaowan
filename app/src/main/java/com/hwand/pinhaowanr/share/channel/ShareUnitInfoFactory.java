package com.hwand.pinhaowanr.share.channel;

import android.content.Context;

import com.alibaba.laiwang.tide.share.business.ShareUnitInfo;
import com.hwand.pinhaowanr.R;


/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class ShareUnitInfoFactory {
    private Context mContext;
    public ShareUnitInfoFactory(Context context){
        this.mContext = context;
    }
    public ShareUnitInfo createWeixinFriendInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_wx_friend);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.weixin_friend));
        shareUnitInfo.setPakName("com.tencent.mm");
        shareUnitInfo.setValue("THIRD_WEIXIN_CONVERSATION");
        shareUnitInfo.setUt("wechat");
        return shareUnitInfo;
    }
    public ShareUnitInfo createWeixinGroupInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_wx_group);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.weixin_group));
        shareUnitInfo.setPakName("com.tencent.mm");
        shareUnitInfo.setValue("THIRD_WEIXIN_CIRCLE");
        shareUnitInfo.setUt("wechatcircle");
        return shareUnitInfo;
    }
    public ShareUnitInfo createQQFriendInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_qq);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.qq_friend));
        shareUnitInfo.setPakName("com.tencent.mobileqq");
        shareUnitInfo.setValue("THIRD_QQ_CONVERSATION");
        shareUnitInfo.setUt("qq");
        return shareUnitInfo;
    }
    public ShareUnitInfo createQQZoneInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_qzone);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.qq_zone));
        shareUnitInfo.setPakName("com.tencent.mobileqq");
        shareUnitInfo.setValue("THIRD_QQ_ZONE");
        shareUnitInfo.setUt("qzone");
        return shareUnitInfo;
    }
    public ShareUnitInfo createSinaWeiboInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_sina_weibo);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.sina_weibo));
        shareUnitInfo.setPakName("com.sina.weibo");
        shareUnitInfo.setValue("THIRD_SINA_WEIBO");
        shareUnitInfo.setUt("weibo");
        return shareUnitInfo;
    }
    public ShareUnitInfo createLaiwangFriendInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_sina_weibo);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.laiwang_friend));
        shareUnitInfo.setPakName("com.alibaba.android.babylon");
        shareUnitInfo.setValue("THIRD_SINA_WEIBO");
        shareUnitInfo.setUt("weibo");
        return shareUnitInfo;
    }
    public ShareUnitInfo createLaiwangDynamicInfo(){
        ShareUnitInfo shareUnitInfo = new ShareUnitInfo();
        shareUnitInfo.setDefautCheck(true);
        shareUnitInfo.setIcon(R.drawable.ic_share_sina_weibo);
        shareUnitInfo.setTitle(mContext.getResources().getString(R.string.laiwang_dynamic));
        shareUnitInfo.setPakName("com.alibaba.android.babylon");
        shareUnitInfo.setValue("THIRD_SINA_WEIBO");
        shareUnitInfo.setUt("laiwang_dynamic");
        return shareUnitInfo;
    }
}
