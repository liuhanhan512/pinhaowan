/*
* @Title:  ShareAdapter.java
* @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
* @data:  2014-7-21 下午2:30:32
* @version:  V1.0
*/

package com.hwand.pinhaowanr.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;

/**
 * TODO< 分享弹出框Adapter >
 *
 * @data: 2014-7-21 下午2:30:32
 * @version: V1.0
 */

public class ShareAdapter extends BaseAdapter {

    private static String[] shareNames = new String[]{"微信", "朋友圈", "微博", "QQ" ,"QQ空间"};
    private int[] shareIcons = new int[]{R.mipmap.umeng_socialize_wechat, R.mipmap.umeng_socialize_wxcircle, R.mipmap.umeng_socialize_sina_on,
            R.mipmap.umeng_socialize_qq_on, R.mipmap.umeng_socialize_qzone_on};

    private LayoutInflater inflater;

    public ShareAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shareNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_share, null);
        }
        ImageView shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
        TextView shareTitle = (TextView) convertView.findViewById(R.id.share_title);
        shareIcon.setImageResource(shareIcons[position]);
        shareTitle.setText(shareNames[position]);

        return convertView;
    }
}
