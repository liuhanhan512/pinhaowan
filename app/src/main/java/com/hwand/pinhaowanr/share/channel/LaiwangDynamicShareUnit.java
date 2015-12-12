package com.hwand.pinhaowanr.share.channel;

import android.content.Context;
import android.util.Log;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.alibaba.laiwang.tide.share.business.excutor.ShareListener;
import com.alibaba.laiwang.tide.share.business.excutor.ShareToManager;


/**
 * Created by zengchan.lzc on 2015/1/20.
 */
public class LaiwangDynamicShareUnit extends BaseShareUnit {
    private Context mContext;
    public LaiwangDynamicShareUnit(Context context){
        super(new ShareUnitInfoFactory(context).createLaiwangDynamicInfo());
        mContext = context;
    }

    @Override
    public void share(ShareInfo shareInfo) {
        String title = shareInfo.getTitle();
        String content = shareInfo.getContent();
        String picUrl = shareInfo.getPictureUrl();

        ShareToManager.getInstance().getLWExecutor().shareImageUrlToDynamic(picUrl, title, content, null, picUrl,null,new ShareListener() {
            @Override
            public void onSuccess() {
                Log.d("lzc", "share to dynamic succuss");
            }

            @Override
            public void onException(String message) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
