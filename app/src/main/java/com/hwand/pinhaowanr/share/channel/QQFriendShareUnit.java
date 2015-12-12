package com.hwand.pinhaowanr.share.channel;

import android.content.Context;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.alibaba.laiwang.tide.share.business.excutor.ShareToManager;
import com.alibaba.laiwang.tide.share.business.excutor.qq.QQExecutor;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class QQFriendShareUnit extends BaseShareUnit {
    private Context mContext;
    public QQFriendShareUnit(Context context){
        super(new ShareUnitInfoFactory(context).createQQFriendInfo());
        mContext = context;
    }
    @Override
    public void share(ShareInfo shareInfo) {
        String title = shareInfo.getTitle();
        String content = shareInfo.getContent();
        String link = shareInfo.getLinkUrl();
        String picUrl = shareInfo.getPictureUrl();
        ShareToManager shareToManager = ShareToManager.getInstance();
        QQExecutor qqExecutor = shareToManager.getQQExecutor(mContext);
//        qqExecutor.shareToQQ((Activity)mContext, title, content,null, picUrl);
        qqExecutor.shareLinkToQQ(title, content, link, picUrl, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
