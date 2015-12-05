package com.hwand.pinhaowanr.community;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.HomePageModel;
import com.hwand.pinhaowanr.model.SmallPartnerModel;
import com.hwand.pinhaowanr.model.SuperMomModel;
import com.hwand.pinhaowanr.model.TheCommunityActivityModel;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社区--小伙伴页面
 * Created by hanhanliu on 15/11/30.
 */
public class SmallPartnerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private SmallPartnerModel mSmallPartnerModel;

    private List<TheCommunityActivityModel> theCommunityActivityModels = new ArrayList<TheCommunityActivityModel>();

    public static BaseFragment newInstance(){
        SmallPartnerFragment fragment = new SmallPartnerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_small_partner_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout)mFragmentView. findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView)mFragmentView . findViewById(R.id.listview);
        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        fetchData();
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private View initHeaderView(){

        View headerView = View.inflate(getActivity() , R.layout.small_partner_list_header_layout , null);

        return headerView;
    }

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("cityType" , MainApplication.getInstance().getCityType() + "");
        AMapLocation mapLocation = MainApplication.getInstance().getAmapLocation();
        if(mapLocation != null){
            params.put("lng" , mapLocation.getLongitude() + "");
            params.put("lat" , mapLocation.getLatitude() + "");
        }

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_BUDDY_INFO, params);

        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mSmallPartnerModel = gson.fromJson(response , SmallPartnerModel.class);
                    updateViews();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateViews(){
        if(mSmallPartnerModel != null){
            // update listview
            List<TheCommunityActivityModel> theCommunityActivityModels = mSmallPartnerModel.getNaActivityList();
            if(theCommunityActivityModels != null){
                this.theCommunityActivityModels.clear();
                this.theCommunityActivityModels.addAll(theCommunityActivityModels);
                mAdapter.notifyDataSetChanged();
            }
            //update super mom
            List<SuperMomModel> superMomModels = mSmallPartnerModel.getRoleList();
            if(superMomModels.size() >= 5){

            } else {

            }
        }

    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return theCommunityActivityModels.size();
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
            if(convertView == null){
                convertView = View.inflate(getActivity() , R.layout.small_partner_list_item_layout , null);
            }
            return convertView;
        }
    }
}
