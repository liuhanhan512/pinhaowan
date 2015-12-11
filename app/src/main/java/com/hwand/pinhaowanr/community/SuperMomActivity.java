package com.hwand.pinhaowanr.community;

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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.mine.UserInfoActivity;
import com.hwand.pinhaowanr.model.SuperMomModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
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
public class SuperMomActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public final static int SIZE = 20;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private List<SuperMomModel> superMomModelList = new ArrayList<SuperMomModel>();

    private int mCount = 0;
    private boolean isLoading;
    private boolean noData;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SuperMomActivity.class);
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

    private void initTitle() {

    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.listview);

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && view.getLastVisiblePosition() > (mAdapter
                        .getCount() - 2) && !isLoading) {
                    fetchData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void fetchData() {
        if (noData) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        isLoading = true;
        mSwipeRefreshLayout.setRefreshing(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startIndex", "" + mCount);
        params.put("endIndex", "" + mCount + SIZE);
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_SUPER_MOMMYS, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                if (!TextUtils.isEmpty(response)) {
                    List<SuperMomModel> superMomModels = SuperMomModel.arrayFromData(response);
                    if (superMomModels != null && superMomModels.size() > 0) {
                        superMomModelList.clear();
                        superMomModelList.addAll(superMomModels);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        AndTools.showToast("已经没有更多用户");
                        noData = true;
                    }
                } else {
                    AndTools.showToast("已经没有更多用户");
                    noData = true;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                final SuperMomModel user = superMomModelList.get(position);
                Intent intent = new Intent();
                intent.setClass(SuperMomActivity.this, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.KEY_INTENT_ID, user.getId());
                intent.putExtra(UserInfoActivity.KEY_INTENT_NAME, user.getName());
                SuperMomActivity.this.startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };

    @Override
    public void onRefresh() {
        if (isLoading) {
            LogUtil.d("dxz", "ignore manually update!");
        } else {
            fetchData();//这里多线程也要手动控制isLoading
        }
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
            try {
                if (convertView == null) {
                    convertView = LayoutInflater.from(SuperMomActivity.this)
                            .inflate(R.layout.super_mom_list_item_layout, viewGroup, false);
                }
                final SuperMomModel user = superMomModelList.get(position);
                TextView name = CommonViewHolder.get(convertView, R.id.name);
                CircleImageView avatar = CommonViewHolder.get(convertView, R.id.avatar);
                name.setText(user.getName());
                AndTools.displayImage(null, user.getUrl(), avatar);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return convertView;
        }
    }
}
