package com.hwand.pinhaowanr.share.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.hwand.pinhaowanr.R;

import java.util.List;


/**
 * Created by zengchan.lzc on 2015/1/27.
 */
public class ShareListviewAdapter extends BaseAdapter {

    private Context mContext;
    private List<BaseShareUnit> mArrayViewItem;

    public ShareListviewAdapter(Context pContext, List<BaseShareUnit> arrayViewItem) {
        this.mContext = pContext;
        mArrayViewItem =arrayViewItem;
    }

    @Override
    public int getCount() {
        return mArrayViewItem.size();
    }

    @Override
    public Object getItem(int position) {

        return mArrayViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder _Holder = null;
        if (null == convertView) {
            _Holder = new Holder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.share_list_item_layout, null);
            _Holder.txt_gv_item = (TextView) convertView.findViewById(R.id.txt_gv_item);
            _Holder.img_gv_item = (ImageView) convertView.findViewById(R.id.img_gv_item);
            convertView.setTag(_Holder);
        } else {
            _Holder = (Holder) convertView.getTag();
        }

        _Holder.txt_gv_item.setText(mArrayViewItem.get(position).getmShareUnitInfo().getTitle());
        _Holder.img_gv_item.setBackgroundResource(mArrayViewItem.get(position).getmShareUnitInfo().getIcon());

        return convertView;
    }

    private static class Holder {
        ImageView img_gv_item;
        TextView txt_gv_item;
    }

}
