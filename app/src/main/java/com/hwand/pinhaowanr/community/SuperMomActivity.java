package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.SmallPartnerModel;
import com.hwand.pinhaowanr.model.SuperMomModel;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级妈咪页面
 * Created by hanhanliu on 15/11/30.
 */
public class SuperMomActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private List<SuperMomModel> superMomModelList = new ArrayList<SuperMomModel>();

    private int startIndex = 0 ;
    private int endIndex = 20;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , SuperMomActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_mom_layout);

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
    }

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("startIndex" , "" +startIndex);
        params.put("endIndex" , "" + endIndex);

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_BUDDY_INFO, params);

        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    List<SuperMomModel> superMomModels = SuperMomModel.arrayHomePageModelFromData(response);
                    if(superMomModels != null){
                        superMomModelList.clear();
                        superMomModelList.addAll(superMomModels);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return superMomModelList.size();
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
                convertView = LayoutInflater.from(SuperMomActivity.this)
                        .inflate(R.layout.super_mom_list_item_layout, viewGroup, false);
            }

            TextView name = CommonViewHolder.get(convertView, R.id.name);
            CircleImageView avatar = CommonViewHolder.get(convertView , R.id.avatar);

            return convertView;
        }
    }
}
