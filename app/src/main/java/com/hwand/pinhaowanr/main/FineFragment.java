package com.hwand.pinhaowanr.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.CityChooseEvent;
import com.hwand.pinhaowanr.event.LocationEvent;
import com.hwand.pinhaowanr.event.RegionChooseEvent;
import com.hwand.pinhaowanr.fine.FineCategoryListActivity;
import com.hwand.pinhaowanr.fine.FineDetailActivity;
import com.hwand.pinhaowanr.location.CityChooseActivity;
import com.hwand.pinhaowanr.location.RegionChooseActivity;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.model.HomePageEntity;
import com.hwand.pinhaowanr.model.HomePageModel;
import com.hwand.pinhaowanr.model.RegionModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;
import com.hwand.pinhaowanr.widget.hlistview.HListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class FineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ExpandableListView mExpandableListView;

    private ExpandAdapter mExpandAdapter;

    private List<HomePageEntity> mListData = new ArrayList<HomePageEntity>();

    private List<HomePageModel> topList = new ArrayList<HomePageModel>();

    private String[] fineCategorys;

    private int mCityType = -1;

    private View mEmptyView;

    public static FineFragment newInstance(){
        FineFragment fragment = new FineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fine_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        fineCategorys = getResources().getStringArray(R.array.fine_array);
        mCityType = MainApplication.getInstance().getCityType();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(LocationEvent event) {
        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if(aMapLocation != null){
            mCity.setText(aMapLocation.getCity());
            mRegion.setText(aMapLocation.getDistrict());

            List<ConfigModel> configModels = DataCacheHelper.getInstance().getConfigModel();
            for(ConfigModel configModel : configModels){
                if(TextUtils.equals(configModel.getCityName() , aMapLocation.getCity())){
                    mCityType = configModel.getCityType();
                    fetchData();
                    break;
                }
            }
        }
    }

    public void onEventMainThread(CityChooseEvent event) {
        ConfigModel configModel = event.configModel;
        if(configModel != null){
            mCityType = configModel.getCityType();
            fetchData();

            mCity.setText(configModel.getCityName());
            List<RegionModel> regionModels = configModel.getRegionMap();
            if(regionModels != null && regionModels.size() > 0){
                mRegion.setText(regionModels.get(0).getTypeName());
            }
        }
    }

    public void onEventMainThread(RegionChooseEvent event) {
        RegionModel regionModel = event.regionModel;
        if(regionModel != null){
            mRegion.setText(regionModel.getTypeName());
        }
    }

    private TextView mCity , mRegion;

    private void initView(){
        mSwipeRefreshLayout = (SwipeRefreshLayout)mFragmentView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mEmptyView = mFragmentView.findViewById(R.id.empty_layout);
        mFragmentView.findViewById(R.id.empty_text).setOnClickListener(this);

        mExpandableListView = (ExpandableListView) mFragmentView.findViewById(R.id.listview);

        mExpandableListView.addHeaderView(initHeaderView());
        mExpandableListView.setOnGroupClickListener(new GroupClickListener());

        mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                return false;
            }
        });

        mExpandAdapter = new ExpandAdapter(getActivity());
        mExpandableListView.setAdapter(mExpandAdapter);


        mCity = (TextView)mFragmentView.findViewById(R.id.city);
        mRegion = (TextView)mFragmentView.findViewById(R.id.region);

        mCity.setOnClickListener(this);
        mRegion.setOnClickListener(this);

        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if(aMapLocation != null){
            mCity.setText(aMapLocation.getCity());
            mRegion.setText(aMapLocation.getDistrict());
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.city:
                CityChooseActivity.launch(getActivity());
                break;
            case R.id.region:
                RegionChooseActivity.launch(getActivity() , mCityType);
                break;
            case R.id.empty_text:
                mSwipeRefreshLayout.setRefreshing(true);
                fetchData();
                break;
        }
    }

    private View headerView;
    private TextView headerAddress,headerDistance,headerTitle,headerSubTitle,headerTickets,headerPayment;
    private ImageView headerImage;

    private View initHeaderView(){
        headerView = View.inflate(getActivity() , R.layout.fine_expandlistview_header_layout2, null);
        headerAddress = (TextView)headerView.findViewById(R.id.address);
        headerDistance = (TextView)headerView.findViewById(R.id.distance);
        headerTitle = (TextView)headerView.findViewById(R.id.title);
        headerSubTitle = (TextView)headerView.findViewById(R.id.sub_title);
        headerTickets = (TextView)headerView.findViewById(R.id.tickets);

        headerImage = (ImageView)headerView.findViewById(R.id.image);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FineDetailActivity.launch(getActivity() , topList.get(0));
            }
        });
        return headerView;
    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return true;
        }
    }

    private void expandView() {
        for (int i = 0; i < mListData.size(); i++) {
            mExpandableListView.expandGroup(i);
        }
    }

    private void fetchData(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("cityType" , mCityType + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_HOME_PAGE, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(!TextUtils.isEmpty(response)){
                    List<HomePageModel> homePageModels  = HomePageModel.arrayHomePageModelFromData(response);
                    if(homePageModels != null && homePageModels.size() > 0){
                        mEmptyView.setVisibility(View.GONE);
                        mExpandableListView.setVisibility(View.VISIBLE);
                        wrapperData(homePageModels);
                    }  else {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mExpandableListView.setVisibility(View.GONE);
                    }
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mExpandableListView.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyView.setVisibility(View.VISIBLE);
                mExpandableListView.setVisibility(View.GONE);
            }
        });
    }

    private static final int INTEREST_TYPE = 1;
    private static final int SHOW_TYPE = 2;
    private static final int PLEASURE_TYPE = 3;

    private void wrapperData(List<HomePageModel> homePageModels){

        if(homePageModels != null && homePageModels.size() > 0){
            mListData.clear();
            for(HomePageModel homePageModel : homePageModels){
                if(homePageModel.getIsStick() == 1){//1置顶，0不置顶
                    topList.add(homePageModel);
                }
                int type = homePageModel.getViewType();
                int dataSize = mListData.size();
                if(dataSize <= 0){
                    addHomePageModel(homePageModel, type);
                } else {
                    boolean isAdd = false;
                    for (int i = 0 ; i < dataSize ; i++){
                        HomePageEntity homePageEntity = mListData.get(i);
                        if(homePageEntity.getType() == type){
                            homePageEntity.getHomePageModelList().add(homePageModel);
                            isAdd = true;
                            break;
                        }
                    }
                    if(!isAdd){
                        addHomePageModel(homePageModel ,type);
                    }

                }
            }
            mExpandAdapter.notifyDataSetChanged();
            expandView();

            if(topList.size() <= 0){
                headerView.setVisibility(View.GONE);
            } else {
                headerView.setVisibility(View.VISIBLE);
                updateHeaderView();
            }
        }
    }

    private void updateHeaderView(){
        HomePageModel homePageModel = topList.get(0);
        headerTitle.setText(homePageModel.getTitle());
        headerSubTitle.setText(homePageModel.getSubtitle());
        headerAddress.setText(homePageModel.getDetailAddress());
//        headerDistance.setText(homePageModel.);
        headerTickets.setText(getString(R.string.remainder_tickets, homePageModel.getRemainTicket()));

        AndTools.displayImage(null , homePageModel.getPictureUrl(), headerImage);
    }

    private void addHomePageModel(HomePageModel homePageModel , int type){
        HomePageEntity homePageEntity = new HomePageEntity();
        homePageEntity.setType(type);
        homePageEntity.setCategory(fineCategorys[type - 1]);
        List<HomePageModel> models = new ArrayList<HomePageModel>();
        models.add(homePageModel);
        homePageEntity.setHomePageModelList(models);
        mListData.add(homePageEntity);
    }


    @Override
    public void onRefresh() {
        fetchData();
    }

    class ExpandAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public ExpandAdapter(Context context) {

            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return mListData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
//            return mListData.get(groupPosition).getTopic().size();
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return mListData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return mListData.get(groupPosition).getHomePageModelList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return groupPosition << 32 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View groupView = null;
            if (convertView == null) {
                groupView = newGroupView(parent);
            } else {
                groupView = convertView;
            }
            bindGroupView(groupPosition, groupView);
            return groupView;
        }

        private View newGroupView(ViewGroup parent) {
            return inflater.inflate(R.layout.fine_group_item_layout, null);
        }

        private void bindGroupView(final int groupPosition, View groupView) {
            TextView title = (TextView)groupView.findViewById(R.id.title);
            title.setText(mListData.get(groupPosition).getCategory());
            ImageView more = (ImageView)groupView.findViewById(R.id.image_more);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FineCategoryListActivity.launch(getActivity() , mListData.get(groupPosition).getType());
                }
            });
        }
        private void onViewMoreClick(int groupPosition){

        }
        @Override
        public View getChildView(int groupPosition, int childPosition,boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View childView = null;
            if (convertView == null) {
                childView = newChildView(parent, groupPosition);
            } else {
                childView = convertView;
            }
            bindChildView(groupPosition, childPosition, childView);
            return childView;
        }

        private View newChildView(ViewGroup parent, final int groupPosition) {
            View v = inflater.inflate(R.layout.fine_child_item_layout, null);

            return v;
        }

        private void bindChildView(final int groupPosition, int childPosition,
                                   View groupView) {

            HListView listView = (HListView) groupView.findViewById(R.id.listview);

            final FineItemAdpater adapter  = new FineItemAdpater(groupPosition);

            listView.setOnItemClickListener(new com.hwand.pinhaowanr.widget.hlistview.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(com.hwand.pinhaowanr.widget.hlistview.AdapterView<?> parent, View view, int position, long id) {
                    HomePageModel homePageModel = mListData.get(groupPosition).getHomePageModelList().get(position);
                    FineDetailActivity.launch(getActivity() , homePageModel);
                }
            });

            listView.setAdapter(adapter);// 设置菜单Adapter

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    class FineItemAdpater extends BaseAdapter{

        List<HomePageModel> homePageModels = new ArrayList<HomePageModel>();

        public FineItemAdpater(int groupPosition ){
            List<HomePageModel> models = mListData.get(groupPosition).getHomePageModelList();
            if(models != null){
                homePageModels.addAll(models);
            }
        }

        @Override
        public int getCount() {
            return homePageModels.size();
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(FineFragment.this.getActivity())
                        .inflate(R.layout.fine_list_item_layout, viewGroup, false);
            }
            HomePageModel homePageModel = homePageModels.get(i);
            ImageView imageView = CommonViewHolder.get(convertView , R.id.image);
            AndTools.displayImage(null,homePageModel.getPictureUrl() ,imageView);
            TextView title = CommonViewHolder.get(convertView , R.id.title);
            title.setText(homePageModel.getTitle());
            TextView address = CommonViewHolder.get(convertView , R.id.address);
            address.setText(homePageModel.getDetailAddress());
            TextView ticket = CommonViewHolder.get(convertView , R.id.tickets);
            ticket.setText(getString(R.string.remainder_tickets , homePageModel.getRemainTicket()));
            TextView payment = CommonViewHolder.get(convertView , R.id.payment);
            return convertView;
        }
    }
}
