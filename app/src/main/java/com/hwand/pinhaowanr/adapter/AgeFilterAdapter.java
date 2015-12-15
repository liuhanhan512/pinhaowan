package com.hwand.pinhaowanr.adapter;

import android.content.Context;

import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.AgeModel;

import java.util.List;

/**
 * Created by hanhanliu on 15/12/13.
 */
public class AgeFilterAdapter extends FilterListAdapter<AgeModel> {

    private Context mContext;

    public AgeFilterAdapter(Context context, List<AgeModel> filterModelList) {
        super(context, filterModelList);
        mContext = context;
    }

    @Override
    protected void bindData(AgeModel object, ViewHolder viewHolder) {
        viewHolder.mFilterText.setText(mContext.getString(R.string.age_text , object.getMinAge() , object.getMaxAge()) + "岁");
    }
}
