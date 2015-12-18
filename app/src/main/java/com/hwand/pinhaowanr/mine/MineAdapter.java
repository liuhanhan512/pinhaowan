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
 * Created by dxz on 15/11/28.
 */

public class MineAdapter extends BaseAdapter {

    private Context mContext;

    private List<NaviEntity> mList;

    private Handler mHandler;

    public MineAdapter(Context context, Handler handler, List<NaviEntity> list) {
        mContext = context;
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
            final NaviEntity navi = mList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_navi, null);
                holder = new ViewHolder();
                holder.tip = (TextView) convertView.findViewById(R.id.tip);
                holder.desc = (TextView) convertView.findViewById(R.id.desc);
                holder.divider = convertView.findViewById(R.id.divider);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (holder == null) {
                return convertView;
            }

            holder.tip.setText(navi.content);
            holder.desc.setText(navi.desc);
            if (position == mList.size() -1) {
                holder.divider.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.divider.setBackgroundColor(mContext.getResources().getColor(R.color.item_divider));
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.sendEmptyMessage(navi.msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
        return convertView;
    }

    public static class NaviEntity {
        public String content;
        public int msg;
        public String desc;

        public NaviEntity(String str, int what) {
            this.content = str;
            this.msg = what;

        }

        public NaviEntity(String str,String desc, int what) {
            this.content = str;
            this.desc =desc;
            this.msg = what;

        }
    }

    public static class ViewHolder {
        public TextView tip;
        public TextView desc;
        public View divider;

    }
}
