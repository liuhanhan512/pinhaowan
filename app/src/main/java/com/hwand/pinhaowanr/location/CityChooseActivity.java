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
import com.hwand.pinhaowanr.event.CityChooseEvent;
import com.hwand.pinhaowanr.model.ConfigModel;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by hanhanliu on 15/12/7.
 */
public class CityChooseActivity extends BaseActivity {

    private ListView mListView;

    private Adapter mAdapter;

    private List<ConfigModel> configModelList = new ArrayList<ConfigModel>();

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context, CityChooseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose_layout);
        initData();
        initTitle();
        initViews();
    }

    private void initData(){
        List<ConfigModel> configModels = DataCacheHelper.getInstance().getConfigModel();
        if(configModels != null){
            configModelList.addAll(configModels);
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
            ConfigModel configModel = configModelList.get(i);
            CityChooseEvent cityChooseEvent = new CityChooseEvent(configModel);
            EventBus.getDefault().post(cityChooseEvent);
            finish();
        }
    };

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return configModelList.size();
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
                convertView = LayoutInflater.from(CityChooseActivity.this)
                        .inflate(R.layout.city_choose_list_item_layout, viewGroup, false);
            }
            ConfigModel configModel = configModelList.get(position);
            TextView city = CommonViewHolder.get(convertView, R.id.city);
            city.setText(configModel.getCityName());
            return convertView;
        }
    }

}
