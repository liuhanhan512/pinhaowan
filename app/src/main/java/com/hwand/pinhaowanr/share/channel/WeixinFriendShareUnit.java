package com.hwand.pinhaowanr.share.channel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.alibaba.laiwang.tide.share.business.excutor.ShareToManager;
import com.hwand.pinhaowanr.R;


/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class WeixinFriendShareUnit extends BaseShareUnit {
    private Context mContext;
    public WeixinFriendShareUnit(Context context){
        super(new ShareUnitInfoFactory(context).createWeixinFriendInfo());
        mContext = context;
    }
    @Override
    public void share(ShareInfo shareInfo) {
        String title = shareInfo.getTitle();
        String content = shareInfo.getContent();
        String link = shareInfo.getLinkUrl();
        String picUrl = shareInfo.getPictureUrl();

        Bitmap defaultBitmap= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_share_wx_group_default);

        ShareToManager.getInstance().getWeiXinExecutor().doShareHypeLink(title, content, null,
                compressImageBySize(defaultBitmap, 30),picUrl, false, null);
    }
}
