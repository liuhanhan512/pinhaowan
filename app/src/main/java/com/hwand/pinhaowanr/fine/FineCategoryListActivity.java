package com.hwand.pinhaowanr.fine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.adapter.AgeFilterAdapter;
import com.hwand.pinhaowanr.adapter.RegionFilterAdpter;
import com.hwand.pinhaowanr.model.AgeModel;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.model.HomePageModel;
import com.hwand.pinhaowanr.model.RegionModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.FilterListView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好玩分类列表
 * Created by hanhanliu on 15/11/22.
 */
public class FineCategoryListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private static final String VIEW_TYPE_KEY = "VIEW_TYPE_KEY";
    private static final String VIEW_NAME_KEY = "VIEW_NAME_KEY";

    private int mViewType = -1;

    private String mViewName;

    private int mCityType = 1;

    private int mRegionType = 0;

    private int mMinAge = 0;

    private int mMaxAge = 100;

    private int mStartIndex = 0;

    private int mEndIndex = 20;


    List<HomePageModel> mHomePageModels  = new ArrayList<HomePageModel>();

    private FilterListView mRegionFilterListView , mAgeFilterListView;

    private List<RegionModel> mRegionList = new ArrayList<RegionModel>();

    private List<AgeModel> mAgeList = new ArrayList<AgeModel>();

    private RegionFilterAdpter mRegionFilterAdpter;

    private AgeFilterAdapter mAgeFilterAdapter;

    private View mIndicatorView;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context, FineCategoryListActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, int viewType){
        Intent intent = new Intent();
        intent.setClass(context, FineCategoryListActivity.class);
        intent.putExtra(VIEW_TYPE_KEY, viewType);
        context.startActivity(intent);
    }

    public static void launch(Context context, int viewType , String typeName){
        Intent intent = new Intent();
        intent.setClass(context, FineCategoryListActivity.class);
        intent.putExtra(VIEW_TYPE_KEY, viewType);
        intent.putExtra(VIEW_NAME_KEY , typeName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_category_layout);
        initIntentValues();
        initData();
        initTitle();
        initViews();
        fetchData();
    }

    private void initData(){
        List<ConfigModel> configModels = DataCacheHelper.getInstance().getConfigModel();

        int configModelSize = configModels.size();
        for (int i = 0 ; i < configModelSize ; i++){
            ConfigModel configModel = configModels.get(i);
            if(configModel.getCityType() == mCityType){
                List<RegionModel> regionModels = configModel.getRegionMap();
                if(regionModels != null ){
                    mRegionList .addAll(regionModels);
                }
                break;
            }
        }

        AgeModel ageModel1 = new AgeModel();
        ageModel1.setMinAge(1);
        ageModel1.setMaxAge(3);

        AgeModel ageModel2 = new AgeModel();
        ageModel2.setMinAge(4);
        ageModel2.setMaxAge(6);

        AgeModel ageModel3 = new AgeModel();
        ageModel3.setMinAge(7);
        ageModel3.setMaxAge(9);

        AgeModel ageModel4 = new AgeModel();
        ageModel4.setMinAge(10);
        ageModel4.setMaxAge(12);

        mAgeList.add(ageModel1);
        mAgeList.add(ageModel2);
        mAgeList.add(ageModel3);
        mAgeList.add(ageModel4);
    }

    private void initIntentValues(){
        mViewType = getIntent().getIntExtra(VIEW_TYPE_KEY , -1);
        mViewName = getIntent().getStringExtra(VIEW_NAME_KEY );
    }

    private void initTitle(){
        // 显示类型（viewType）1 兴趣益教 2 演出展览 3 游乐
        setActionBarTtile(mViewName);

    }

    private void initViews(){

        mIndicatorView = findViewById(R.id.indicator);

        findViewById(R.id.area_layout).setOnClickListener(this);
        findViewById(R.id.age_layout).setOnClickListener(this);


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

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HomePageModel homePageModel = mHomePageModels.get(i);
            FineDetailActivity.launch(FineCategoryListActivity.this , homePageModel);
        }
    };

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("viewType" , mViewType+"");
        params.put("cityType" , mCityType + "");
        params.put("type" , mRegionType + "");
        params.put("minAge" ,mMinAge + "");
        params.put("maxAge" , mMaxAge + "");
        params.put("startIndex" , mStartIndex + "");
        params.put("endIndex" , mEndIndex + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_SEARCH_MORE, params);
        LogUtil.d("dxz",url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    List<HomePageModel> homePageModels = HomePageModel.arrayHomePageModelFromData(response);
                    if (homePageModels != null) {
                        mHomePageModels.clear();
                        mHomePageModels.addAll(homePageModels);
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

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.area_layout:
                triggerRegionFilterListView();
                break;
            case R.id.age_layout:
                triggerAgeFilterListView();
                break;
        }
    }

    private void triggerRegionFilterListView(){
        if(mRegionFilterListView == null){
            mRegionFilterListView = new FilterListView(this,mRegionList.size());
            mRegionFilterAdpter = new RegionFilterAdpter(this , mRegionList);
            mRegionFilterListView.setAdapter(mRegionFilterAdpter);
            mRegionFilterListView.setOnItemClickListener(new FilterListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    RegionModel regionModel = mRegionList.get(position);
                    mRegionType = regionModel.getType();
                    mSwipeRefreshLayout.setRefreshing(true);
                    fetchData();
                }
            });
        }
        if(!mRegionFilterListView.isShowing()){
            mRegionFilterListView.showAsDropDown(mIndicatorView,0, AndTools.dp2px(this , 1));
        } else {
            mRegionFilterListView.dismiss();
        }
    }

    private void triggerAgeFilterListView(){
        if(mAgeFilterListView == null){
            mAgeFilterListView = new FilterListView(this,mAgeList.size());
            mAgeFilterAdapter = new AgeFilterAdapter(this , mAgeList);
            mAgeFilterListView.setAdapter(mAgeFilterAdapter);
            mAgeFilterListView.setOnItemClickListener(new FilterListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AgeModel ageModel = mAgeList.get(position);
                    mMinAge = ageModel.getMinAge();
                    mMaxAge = ageModel.getMaxAge();
                    mSwipeRefreshLayout.setRefreshing(true);
                    fetchData();

                }
            });
        }
        if(!mAgeFilterListView.isShowing()){
            mAgeFilterListView.showAsDropDown(mIndicatorView,0,AndTools.dp2px(this , 1));
        } else {
            mAgeFilterListView.dismiss();
        }
    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mHomePageModels.size();
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
            HomePageModel homePageModel = mHomePageModels.get(position);
            ImageView imageView = CommonViewHolder.get(convertView, R.id.image);
            AndTools.displayImage(null, homePageModel.getPictureUrl(), imageView);

            TextView title = CommonViewHolder.get(convertView , R.id.title);
            title.setText(homePageModel.getTitle());

            TextView address = CommonViewHolder.get(convertView , R.id.address);
            address.setText(homePageModel.getClassName());

            TextView distance = CommonViewHolder.get(convertView , R.id.distance);
//            distance.setText(homePageModel.ge);

            TextView ticket = CommonViewHolder.get(convertView , R.id.tickets);
            ticket.setText(getString(R.string.remainder_tickets , homePageModel.getRemainTicket()));

            TextView payment = CommonViewHolder.get(convertView , R.id.payment);
            return convertView;
        }
    }
}
