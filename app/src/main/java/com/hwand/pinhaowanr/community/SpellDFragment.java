package com.hwand.pinhaowanr.community;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.SpellDCategoryModel;
import com.hwand.pinhaowanr.model.SpellDEntity;
import com.hwand.pinhaowanr.model.SpellDModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SubGridView;
import com.hwand.pinhaowanr.widget.SubListView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社区--拼拼页面
 * Created by hanhanliu on 15/11/30.
 */
public class SpellDFragment extends BaseCommunityFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollView mScrollView;

    private SubListView mListView;

    private Adapter mAdapter;

    private LinearLayout mCategoryLayout;

    private SubGridView mGridView;

    private GridAdapter mGridAdapter;

    private View mEmptyView;

    private List<SpellDCategoryModel> spellDCategoryModels = new ArrayList<SpellDCategoryModel>();
    private List<SpellDModel> spellDModels = new ArrayList<SpellDModel>();

    public static BaseCommunityFragment newInstance(){
        SpellDFragment fragment = new SpellDFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spell_d_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout)mFragmentView. findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mScrollView = (ScrollView)mFragmentView.findViewById(R.id.scrollview);

        mListView = (SubListView)mFragmentView . findViewById(R.id.listview);
//        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mGridView = (SubGridView) mFragmentView.findViewById(R.id.category_gird);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int categorySize = spellDCategoryModels.size();
                for (int i= 0 ; i < categorySize ; i++){
                    SpellDCategoryModel spellDCategoryModel = spellDCategoryModels.get(i);
                    if(i == position){
                        spellDCategoryModel.setIsSelected(true);
                    } else {
                        spellDCategoryModel.setIsSelected(false);
                    }
                }
                mGridAdapter.notifyDataSetChanged();
            }
        });
        mGridAdapter = new GridAdapter();
        mGridView.setAdapter(mGridAdapter);

        mEmptyView = mFragmentView.findViewById(R.id.empty_layout);
        mFragmentView.findViewById(R.id.empty_text).setOnClickListener(this);

        fetchData();
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private View initHeaderView(){
        View headerView = View.inflate(getActivity() , R.layout.spell_d_list_header_layout , null);

        return headerView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.empty_text:
                mSwipeRefreshLayout.setRefreshing(true);
                fetchData();
                break;
        }

    }

    @Override
    public void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("cityType" ,  "1");
//        params.put("cityType" , MainApplication.getInstance().getCityType() + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PINPIN_INFO, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    SpellDEntity spellDEntity = gson.fromJson(response, SpellDEntity.class);
                    if (spellDEntity != null) {

                        List<SpellDCategoryModel> spellDCategoryModels = spellDEntity.getClassTypeList();
                        wrapperSpellDCategoryData(spellDCategoryModels);

                        List<SpellDModel> spellDModels = spellDEntity.getPinClassList();
                        updatePinView(spellDModels);

                        if ((spellDCategoryModels != null && spellDCategoryModels.size() > 0) || (spellDModels != null && spellDModels.size() > 0)) {
                            mEmptyView.setVisibility(View.GONE);
                            mScrollView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mScrollView.setVisibility(View.GONE);
                        }
                    }

                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyView.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }
        });
    }

    public void filterData(){
        // TODO:
        Map<String, String> params = new HashMap<String, String>();
        params.put("cityType" , MainApplication.getInstance().getCityType() + "");
        params.put("classType" , 0 + "");
        params.put("type" , 0 + "");
        params.put("minAge" , 0 + "");
        params.put("maxAge" , 0 + "");
        params.put("startIndex" , 0 + "");
        params.put("endIndex" , 0 + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PIN_CLASS, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO:
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    List<SpellDModel> spellDModels = SpellDModel.arrayFromData(response);
                    if(spellDModels != null){
                        updatePinView(spellDModels);

                        if((spellDCategoryModels != null && spellDCategoryModels.size() > 0) || (spellDModels != null && spellDModels.size() > 0) ){
                            mEmptyView.setVisibility(View.GONE);
                            mScrollView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mScrollView.setVisibility(View.GONE);
                        }
                    }

                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyView.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }
        });
    }

    private void wrapperSpellDCategoryData(List<SpellDCategoryModel> spellDCategoryModels){
        if(spellDCategoryModels != null){
            SpellDCategoryModel all = new SpellDCategoryModel();
            all.setClassName(getString(R.string.all));
            all.setClassType(-1);
            all.setIsSelected(true);
            spellDCategoryModels.add(0, all);

            this.spellDCategoryModels.clear();
            this.spellDCategoryModels.addAll(spellDCategoryModels);
            mGridAdapter.notifyDataSetChanged();
        }
    }

    private void updatePinView(List<SpellDModel> spellDModels){
        if(spellDModels != null){
            this.spellDModels.clear();
            this.spellDModels.addAll(spellDModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return spellDModels.size();
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
                convertView = View.inflate(getActivity() , R.layout.spell_d_list_item_layout , null);
            }
            final SpellDModel spellDModel = spellDModels.get(position);

            RelativeLayout imageLayout = CommonViewHolder.get(convertView , R.id.image_layout);
            final int screenWidth = AndTools.getScreenWidth(getActivity());
            int height = screenWidth * 9 / 16;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageLayout.getLayoutParams();
            layoutParams.height = height;
            imageLayout.setLayoutParams(layoutParams);

            ImageView imageView = CommonViewHolder.get(convertView, R.id.image);
            AndTools.displayImage(null, spellDModel.getPictureUrl(), imageView);

            TextView description = CommonViewHolder.get(convertView , R.id.description);
            description.setText(spellDModel.getSubtitle());

            TextView className = CommonViewHolder.get(convertView , R.id.class_name);
            className.setText(spellDModel.getClassName());

            TextView address = CommonViewHolder.get(convertView , R.id.address);
            address.setText(spellDModel.getDetailAddress());

            convertView.findViewById(R.id.view_pin_class).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpellDListActivity.launch(getActivity() , spellDModel.getId());
                }
            });
            return convertView;
        }
    }

    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return spellDCategoryModels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getActivity() , R.layout.spell_d_category_item_layout ,null);
            }

            SpellDCategoryModel spellDCategoryModel = spellDCategoryModels.get(position);
            TextView category = CommonViewHolder.get(convertView , R.id.category_text);
            category.setText(spellDCategoryModel.getClassName());
            category.setSelected(spellDCategoryModel.isSelected());

            RelativeLayout categoryLayout = CommonViewHolder.get(convertView , R.id.category_layout);
            if(spellDCategoryModel.isSelected()){
                categoryLayout.setBackgroundResource(R.drawable.red_solid_corner_bg);
            } else {
                categoryLayout.setBackgroundResource(R.drawable.red_corner_bg);
            }

            return convertView;
        }
    }
}
