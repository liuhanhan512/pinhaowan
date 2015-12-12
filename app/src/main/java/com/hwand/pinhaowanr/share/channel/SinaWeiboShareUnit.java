package com.hwand.pinhaowanr.share.channel;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.alibaba.laiwang.tide.share.business.excutor.ShareToManager;


/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class SinaWeiboShareUnit extends BaseShareUnit {
    private Context mContext;
    public SinaWeiboShareUnit(Context context){
        super(new ShareUnitInfoFactory(context).createSinaWeiboInfo());
        mContext = context;
    }
    @Override
    public void share(ShareInfo shareInfo) {
        String title = shareInfo.getTitle();
        String content = shareInfo.getContent();
        String link = shareInfo.getLinkUrl();
        String picUrl = shareInfo.getPictureUrl();

        ShareToManager.getInstance().getSinaExecutor(mContext).doShareLink(title,content,null,link,null);
    }
    private String buildWeiBoContent(String content) {
        int length=129*2;
        if (TextUtils.isEmpty(content)){
            return "";
        }
        int count = 0;
        int offset = 0;
        char[] c = content.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] > 256) {
                offset = 2;
                count += 2;
            } else {
                offset = 1;
                count++;
            }
            if (count == length) {
                return content.substring(0, i + 1)+"...";
            }
            if ((count == length + 1 && offset == 2)) {
                return content.substring(0, i)+"...";
            }
        }
        return content;

    }
}
