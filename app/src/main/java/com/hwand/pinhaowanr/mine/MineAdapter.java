/*
* @Title:  ShareAdapter.java
* @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
* @data:  2014-7-21 下午2:30:32
* @version:  V1.0
*/

package com.hwand.pinhaowanr.mine;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;

import java.util.List;

/**
 * @data: 2014-7-21 下午2:30:32
 * @version: V1.0
 */

public class MineAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<NaviEntity> mList;

    private Handler mHandler;

    public MineAdapter(Context context, Handler handler, List<NaviEntity> list) {
        inflater = LayoutInflater.from(context);
        mHandler = handler;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        try {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_mine, null);
            }
            final NaviEntity navi = mList.get(position);
            TextView text = (TextView) convertView.findViewById(R.id.tip);
            text.setText(navi.content);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.sendEmptyMessage(navi.msg);
                }
            });
        } catch (Exception e) {

        }


        return convertView;
    }

    public static class NaviEntity {
        public String content;
        public int msg;

        public NaviEntity(String str, int what) {
            this.content = str;
            this.msg = what;

        }
    }
}
