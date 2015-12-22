package com.hwand.pinhaowanr.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.LocationChooseEvent;
import com.hwand.pinhaowanr.event.LocationEvent;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.utils.AndTools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 地区选择页面
 * Created by hanhanliu on 15/11/30.
 */
public class LocationChooseActivity extends BaseActivity  {

    private List<PoiItem> mPoiItems = new ArrayList<PoiItem>();// poi数据

    private String mCurrentCity = "";

    private ProgressBar mProgressBar;

    private View mEmptyview;

    private EditText mSearchEdit;

    private Button mSearchBtn;

    private String mLocationAddress;

    private ListView mListView;

    private Adapter mAdapter;

    private int mCurrentPage = 0;

    private int mSelectIndex = 0;

    private PoiSearch.Query mQuery;// Poi查询条件类

    private PoiSearch mPoiSearch;

    private PoiResult mPoiResult; // poi返回的结果

    private static final String POI_SEARCH_TYPE = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
            "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
            "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    private static final int DEFAULT_NUM_PER_PAGE = 20;

    private static final int DEFAULT_DISTANCE = 5000;

    private boolean mIsSearching = false;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context, LocationChooseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choose_layout);
        initLocation();
        initTitle();
        initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initLocation(){
        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if (aMapLocation != null) {
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            regecodeLatLng(latLng ,DEFAULT_DISTANCE );
        }
    }

    public void onEventMainThread(LocationEvent event) {
        initLocation();
        initTitle();
    }

    /**
     * 逆地理位置编码,根据经纬度返回最近的那个POI点点信息
     * @param latLng
     */
    private void regecodeLatLng(final LatLng latLng , final int distance){

        POIValueFetch.newInstance(this).regecodeLatLng(latLng, distance, new POIValueFetch.OnPOIRegeoCodeFetchCallBack() {

            @Override
            public void onfail(String info, int errorCode) {

            }

            @Override
            public void onSuccess(RegeocodeResult regeocodeResult) {
                if (regeocodeResult != null) {
                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                    if (regeocodeAddress != null) {
                        mCurrentCity = regeocodeAddress.getCity();
                        mLocationAddress = regeocodeAddress.getFormatAddress();
                    }
                }
                initQuery();
                search(latLng);
            }

        });
    }

    private void initQuery(){
        mCurrentPage = 0;
        mSelectIndex = 0;
        mQuery = new PoiSearch.Query("", POI_SEARCH_TYPE, mCurrentCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(DEFAULT_NUM_PER_PAGE);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(mCurrentPage);// 设置查第一页
    }

    private void search(LatLng latLng){
        mIsSearching = true;//开始搜索
        mProgressBar.setVisibility(View.VISIBLE);
        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.setOnPoiSearchListener(mOnPoiSearchListener);


        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude , latLng.longitude);
        // 设置搜索区域为以latLonPoint点为圆心，其周围2000米范围
        mPoiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, DEFAULT_DISTANCE, true));


        mPoiSearch.searchPOIAsyn();// 异步搜索
    }

    private void search(String text){
        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if(aMapLocation != null){
            mCurrentPage = 0;
            mQuery = new PoiSearch.Query(text, POI_SEARCH_TYPE, aMapLocation.getCity());
            mQuery.setPageNum(mCurrentPage);
            mPoiSearch = new PoiSearch(this, mQuery);
            mPoiSearch.setOnPoiSearchListener(mOnPoiSearchListener);

            LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude() , aMapLocation.getLongitude());
            mPoiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, DEFAULT_DISTANCE, true));

            mPoiSearch.searchPOIAsyn();
        }

    }


    final PoiSearch.OnPoiSearchListener mOnPoiSearchListener = new PoiSearch.OnPoiSearchListener(){

        @Override
        public void onPoiSearched(PoiResult result, int rCode) {
            mListView.setEmptyView(mEmptyview);
            mProgressBar.setVisibility(View.GONE);
            mIsSearching = false;
            switch (rCode){
                case 0:

                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(mQuery)) {// 是否是同一条
                            mPoiResult = result;
                            List<PoiItem> list = result.getPois();
                            if (list != null && list.size() > 0) {
                                mLocationAddress = "";
                                if(mCurrentPage == 0){
                                    mPoiItems.clear();
                                }
                                mPoiItems.addAll(list);
                            }

                        }
                    } else {

                    }
                    break;
                case 27:
                case 32:
                default:
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int rCode) {
            mProgressBar.setVisibility(View.GONE);
            mIsSearching = false;
            mAdapter.notifyDataSetChanged();
        }
    };

    private void initTitle(){
        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if(aMapLocation != null){

            setActionBarTtile(aMapLocation.getCity());
        }
    }

    private void initViews(){
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mEmptyview = findViewById(R.id.no_result_layout);

        mSearchEdit = (EditText) findViewById(R.id.search_edit);
        mSearchBtn = (Button) findViewById(R.id.search_text);
        mSearchBtn.setOnClickListener(this);

        mListView = (ListView)findViewById(R.id.listview);

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && firstVisibleItem > 0) {

                    if (mQuery != null && mPoiSearch != null && mPoiResult != null) {
                        if (mPoiResult.getPageCount() - 1 > mCurrentPage && mProgressBar.getVisibility() == View.GONE) {
                            mProgressBar.setVisibility(View.VISIBLE);

                            mCurrentPage++;

                            mQuery.setPageNum(mCurrentPage);// 设置查后一页
                            mPoiSearch.searchPOIAsyn();
                        } else {

                        }
                    }
                }
            }
        });
    }
    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            LocationChooseEvent locationChooseEvent = new LocationChooseEvent();
            locationChooseEvent.setPoiItem(mPoiItems.get(position));
            EventBus.getDefault().post(locationChooseEvent);
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.search_text:
                onSearchClick();
                break;
        }
    }

    private void onSearchClick(){
        String text = mSearchEdit.getEditableText().toString();
        if(TextUtils.isEmpty(text)){
            AndTools.showToast(R.string.search_none_tips);
            return;
        }
        search(text);
    }


    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPoiItems.size();
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
                convertView = LayoutInflater.from(LocationChooseActivity.this)
                        .inflate(R.layout.location_choose_list_item_layout, viewGroup, false);
            }

            TextView name = CommonViewHolder.get(convertView , R.id.poiitem_name);
            TextView address = CommonViewHolder.get(convertView , R.id.poiitem_addr);
            TextView distance = CommonViewHolder.get(convertView, R.id.distance);

            PoiItem poiItem = mPoiItems.get(position);
            name.setText(poiItem.getTitle());

            String provinceName = poiItem.getProvinceName();
            String cityName = poiItem.getCityName();
            if(TextUtils.equals(provinceName ,cityName)){
                address.setText(cityName + poiItem.getAdName()+poiItem.getSnippet());
            } else {
                address.setText( provinceName + cityName + poiItem.getAdName() + poiItem.getSnippet());
            }
            distance.setText(""+poiItem.getDistance());
            return convertView;
        }
    }
}
