package com.hwand.pinhaowanr.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.RegionChooseEvent;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.model.RegionModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by hanhanliu on 15/12/7.
 */
public class RegionChooseActivity extends BaseActivity {
    private ListView mListView;

    private Adapter mAdapter;

    private List<RegionModel> regionModels = new ArrayList<RegionModel>();

    private int mCityType = 1;

    private static final String CITY_TYPE_KEY = "CITY_TYPE_KEY";

    public static void launch(Context context , int cityType){
        Intent intent = new Intent();
        intent.setClass(context, RegionChooseActivity.class);
        intent.putExtra(CITY_TYPE_KEY, cityType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose_layout);
        getIntentValue();
        initData();
        initTitle();
        initViews();
    }

    private void getIntentValue(){
        mCityType = getIntent().getIntExtra(CITY_TYPE_KEY , -1);
    }

    private void initData(){
        List<ConfigModel> configModels = DataCacheHelper.getInstance().getConfigModel();
        if(configModels != null){
            for(ConfigModel configModel : configModels){
                if(mCityType == configModel.getCityType()){
                    List<RegionModel> regionModels = configModel.getRegionMap();
                    if(regionModels != null){
                        this.regionModels.addAll(regionModels);
                    }
                }
            }
        }
    }

    private void initTitle(){
        setActionBarTtile(getString(R.string.city_choose_title));
    }

    private void initViews(){
        mListView = (ListView)findViewById(R.id.listview);

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            RegionModel regionModel = regionModels.get(i);
            RegionChooseEvent regionChooseEvent = new RegionChooseEvent(regionModel);
            EventBus.getDefault().post(regionChooseEvent);
            finish();
        }
    };

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return regionModels.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(RegionChooseActivity.this)
                        .inflate(R.layout.location_choose_list_item_layout, viewGroup, false);
            }
            RegionModel configModel = regionModels.get(position);
            TextView city = CommonViewHolder.get(convertView, R.id.city);
            city.setText(configModel.getTypeName());
            return convertView;
        }
    }
}
