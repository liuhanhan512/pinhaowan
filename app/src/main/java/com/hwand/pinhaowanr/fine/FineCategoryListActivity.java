package com.hwand.pinhaowanr.fine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;

/**
 * 好玩分类列表
 * Created by hanhanliu on 15/11/22.
 */
public class FineCategoryListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , FineCategoryListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_category_layout);

        initTitle();
        initViews();
        fetchData();
    }

    private void initTitle(){

    }

    private void initViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView)findViewById(R.id.listview);

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        findViewById(R.id.area_layout).setOnClickListener(this);
        findViewById(R.id.age_layout).setOnClickListener(this);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private void fetchData(){

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.area_layout:
                break;
            case R.id.age_layout:
                break;
        }
    }

    private void onAreaClick(){

    }

    private void onAgeClick(){

    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
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
                convertView = LayoutInflater.from(FineCategoryListActivity.this)
                        .inflate(R.layout.fine_category_list_item_layout, viewGroup, false);
            }
            ImageView imageView = CommonViewHolder.get(convertView, R.id.image);
            TextView title = CommonViewHolder.get(convertView , R.id.title);
            TextView address = CommonViewHolder.get(convertView , R.id.address);
            TextView ticket = CommonViewHolder.get(convertView , R.id.tickets);
            TextView payment = CommonViewHolder.get(convertView , R.id.payment);
            return convertView;
        }
    }
}
