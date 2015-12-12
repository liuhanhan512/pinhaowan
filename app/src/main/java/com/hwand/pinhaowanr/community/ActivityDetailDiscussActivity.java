package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ActivityDiscussModel;
import com.hwand.pinhaowanr.model.ActivityModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页--小伙伴--活动详情--评论页
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityDetailDiscussActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String ACTIVITY_ID_KEY = "activity_id_key";

    private int mActivityId;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private List<ActivityDiscussModel> mActivityDiscussModels = new ArrayList<ActivityDiscussModel>();

    private EditText mEditText;

    private Button mSend;

    public static void launch(Context context , int id){
        Intent intent = new Intent();
        intent.setClass(context, ActivityDetailDiscussActivity.class);
        intent.putExtra(ACTIVITY_ID_KEY, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_discuss_activity_layout);
        initIntentValue();
        initTitle();
        initViews();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    private void initTitle(){
        setActionBarTtile(getString(R.string.activity_detail_discuss));
    }

    private void initIntentValue(){
        mActivityId = getIntent().getIntExtra(ACTIVITY_ID_KEY , 0);
    }

    private void initViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.listview);
//        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mEditText = (EditText)findViewById(R.id.edittext);
        mSend = (Button)findViewById(R.id.send);
        mSend.setOnClickListener(this);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private int mStartIndex = 0;
    private int mEndIndex = 20;
    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mActivityId + "");
        params.put("startIndex" , mStartIndex +"");
        params.put("endIndex" , mEndIndex +"");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_ACTIVITY_MSG, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    List<ActivityDiscussModel> activityDiscussModels = ActivityDiscussModel.arrayHomePageModelFromData(response);
                    if (activityDiscussModels != null) {
                        mActivityDiscussModels.clear();
                        mActivityDiscussModels.addAll(activityDiscussModels);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.send:
                onSendClick();
                break;
        }
    }

    private void onSendClick(){
        if(canSend()){
            mSend.setEnabled(false);
            String discuss = mEditText.getEditableText().toString();
            comment(discuss);
        }
    }

    private boolean canSend(){
        if(TextUtils.isEmpty(mEditText.getEditableText().toString())){

            AndTools.showToast(getString(R.string.discuss_null_tips));
            return false;
        } else {
            return true;
        }

    }

    private void comment(String discuss){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mActivityId + "");
        params.put("content" , discuss);
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_DISCUSS_ACTIVITY, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                fetchData();
                mSend.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSend.setEnabled(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mActivityDiscussModels.size();
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
                convertView = View.inflate(ActivityDetailDiscussActivity.this , R.layout.activity_detail_discuss_layout , null);
            }
            ActivityDiscussModel activityDiscussModel = mActivityDiscussModels.get(position);

            CircleImageView avatar = (CircleImageView)findViewById(R.id.avatar);
            AndTools.displayImage(null , activityDiscussModel.getUrl() , avatar);
            TextView name = (TextView)findViewById(R.id.name);
            name.setText(activityDiscussModel.getName());
            TextView content = (TextView)findViewById(R.id.content);
            content.setText(activityDiscussModel.getContent());
            return convertView;
        }
    }
}
