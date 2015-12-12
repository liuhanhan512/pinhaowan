package com.hwand.pinhaowanr.share.channel;

import android.content.Context;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;

/**
 * Created by zengchan.lzc on 2015/1/18.
 */
public class QQZoneShareUnit extends BaseShareUnit {
    private Context mContext;
    public QQZoneShareUnit(Context context){
        super(new ShareUnitInfoFactory(context).createQQZoneInfo());
        mContext = context;
    }
    @Override
    public void share(ShareInfo shareInfo) {

    }
}
