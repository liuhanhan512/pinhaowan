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
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ClassDetailModel;
import com.hwand.pinhaowanr.model.HomePageModel;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好玩详情页
 * Created by hanhanliu on 15/11/23.
 */
public class FineDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private static final String HOME_PAGE_MODEL_KEY = "HOME_PAGE_MODEL_KEY";

    private HomePageModel mHomePageModel;

    private ClassDetailModel mClassDetailModel;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , FineDetailActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context , HomePageModel homePageModel){
        Intent intent = new Intent();
        intent.setClass(context , FineDetailActivity.class);
        intent.putExtra(HOME_PAGE_MODEL_KEY, homePageModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_detail_layout);
        initIntentValues();
        initTitle();
        initViews();
        fetchData();
    }

    private void initIntentValues(){
        mHomePageModel = (HomePageModel) getIntent().getSerializableExtra(HOME_PAGE_MODEL_KEY);
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
        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        findViewById(R.id.contact_layout).setOnClickListener(this);
        findViewById(R.id.reservation_layout).setOnClickListener(this);
        findViewById(R.id.gift_ticket_layout).setOnClickListener(this);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private View initHeaderView(){

        View headerView = View.inflate(this , R.layout.fine_detail_list_header_layout , null);

        return headerView;
    }

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id" , mHomePageModel.getId()+"");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_CLASS_DETAIL, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mClassDetailModel = gson.fromJson(response , ClassDetailModel.class);
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
        setActionBarTtile(mClassDetailModel.getInstitutionName());
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.contact_layout:
                onContactClick();
                break;
            case R.id.reservation_layout:
                onReservationClick();
                break;
            case R.id.gift_ticket_layout:
                onGiftTicketClick();
                break;
        }
    }

    private void onContactClick(){

    }

    private void onReservationClick(){

    }

    private void onGiftTicketClick(){

    }

    class Adapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(FineDetailActivity.this)
                        .inflate(R.layout.fine_detail_list_item_layout, viewGroup, false);
            }

            TextView title = CommonViewHolder.get(convertView , R.id.title);
            TextView content = CommonViewHolder.get(convertView , R.id.content);
            ImageView image = CommonViewHolder.get(convertView , R.id.image);
            return convertView;
        }
    }

}
