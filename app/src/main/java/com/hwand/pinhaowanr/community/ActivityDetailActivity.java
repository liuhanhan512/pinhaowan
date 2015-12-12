package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ActivityModel;
import com.hwand.pinhaowanr.model.ClassDetailModel;
import com.hwand.pinhaowanr.model.TheCommunityActivityModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动详情
 * Created by hanhanliu on 15/12/2.
 */
public class ActivityDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String ACTIVITY_ID_KEY = "activity_id_key";

    private static final String COMMUNITY_ACTIVITy_KEY = "COMMUNITY_ACTIVITy_KEY";

    private int mActivityId;
    private TheCommunityActivityModel mTheCommunityActivityModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ActivityModel mActivityModel;

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context, ActivityDetailActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context , int id){
        Intent intent = new Intent();
        intent.setClass(context , ActivityDetailActivity.class);
        intent.putExtra(ACTIVITY_ID_KEY, id);
        context.startActivity(intent);
    }

    public static void launch(Context context , TheCommunityActivityModel theCommunityActivityModel){
        Intent intent = new Intent();
        intent.setClass(context, ActivityDetailActivity.class);
        intent.putExtra(COMMUNITY_ACTIVITy_KEY, theCommunityActivityModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail_layout);
        initIntentValues();
        initViews();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    private void initIntentValues(){
        mActivityId = getIntent().getIntExtra(ACTIVITY_ID_KEY , 0);
        mTheCommunityActivityModel = (TheCommunityActivityModel) getIntent().getSerializableExtra(COMMUNITY_ACTIVITy_KEY);
        if(mTheCommunityActivityModel != null){
            mActivityId = mTheCommunityActivityModel.getId();
        }
    }

    private void initViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        LinearLayout activityInfoLayout = (LinearLayout)findViewById(R.id.activity_info_layout);
        if(mTheCommunityActivityModel == null){
            activityInfoLayout.setVisibility(View.GONE);
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.image);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.height = AndTools.getScreenWidth(this) * 9 / 16;
            imageView.setLayoutParams(layoutParams);
            AndTools.displayImage(null, mTheCommunityActivityModel.getUrl(), imageView);
            TextView address = (TextView)findViewById(R.id.address);
            address.setText(mTheCommunityActivityModel.getDetailAddress());
            TextView time = (TextView)findViewById(R.id.time);
            time.setText(getString(R.string.sign_up_times , mTheCommunityActivityModel.getStratTime() ,mTheCommunityActivityModel.getEndTime()));

            TextView age = (TextView)findViewById(R.id.age);
//            age.setText(getString(R.string.fine_detail_age , mTheCommunityActivityModel.get));

            TextView peopleCount = (TextView)findViewById(R.id.people_count);
            peopleCount.setText(getString(R.string.activity_people_count , mTheCommunityActivityModel.getMaxRoles()));

        }
    }

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mActivityId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_ACTIVITY_DETAIL, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mTheCommunityActivityModel = gson.fromJson(response, TheCommunityActivityModel.class);
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

    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}