package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ActivityModel;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 主页--社区--拼拼--课程详情
 * Created by hanhanliu on 15/12/12.
 */
public class SpellDClassActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String ACTIVITY_ID_KEY = "activity_id_key";

    private int mClassId;

    public static void launch(Context context , int id){
        Intent intent = new Intent();
        intent.setClass(context , SpellDClassActivity.class);
        intent.putExtra(ACTIVITY_ID_KEY, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_d_class_detail_layout);
        initIntentValue();
        initTitle();
        initViews();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();

    }

    private void initIntentValue(){
        mClassId = getIntent().getIntExtra(ACTIVITY_ID_KEY ,0);
    }

    private void initTitle(){

    }

    private void initViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mClassId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PIN_CLASS_DETAIL, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
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
}
