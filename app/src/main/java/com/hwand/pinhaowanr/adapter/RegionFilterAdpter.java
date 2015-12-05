package com.hwand.pinhaowanr.adapter;

import android.content.Context;

import com.hwand.pinhaowanr.model.RegionModel;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/4.
 */
public class RegionFilterAdpter extends FilterListAdapter<RegionModel> {

    public RegionFilterAdpter(Context context, List<RegionModel> filterModelList) {
        super(context, filterModelList);
    }

    @Override
    protected void bindData(RegionModel object, ViewHolder viewHolder) {
        viewHolder.mFilterText.setText(object.getTypeName());
    }
}
