package com.hwand.pinhaowanr.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;

import java.util.List;

/**
 * Created by shiqi on 15/4/23.
 */
public abstract class FilterListAdapter<T> extends BaseAdapter {

    private List<T> mFilterModelList;

    private Context mContext ;

    public FilterListAdapter(Context context, List<T> filterModelList){
        mContext = context;
        mFilterModelList = filterModelList;
    }

    @Override
    public int getCount() {
        if(mFilterModelList != null){
            return mFilterModelList.size();
        }
        return 0;
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
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.filter_item_layout, null);
            viewHolder.mFilterText = (TextView) convertView.findViewById(R.id.filter_textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        T filterModel = mFilterModelList.get(position);


        bindData(filterModel , viewHolder);


        return convertView;
    }
    public class ViewHolder{
        TextView mFilterText;
    }

    protected abstract void bindData(T object , ViewHolder viewHolder);
}
