package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.SlidingAdapter;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static int SIZE = 20;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isAdded()) {
                return;
            }
            switch (msg.what) {
                default:

            }

        }
    };

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private SlidingAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mCount = 0;
    private boolean isLoading;
    private boolean onData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("消息");
        mSwipeRefreshLayout = (SwipeRefreshLayout) mFragmentView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SlidingAdapter(getActivity(), new ArrayList<MsgInfo>());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    if (isLoading) {
                        LogUtil.d("dxz", "ignore manually update!");
                    } else {
                        request();//这里多线程也要手动控制isLoading
                    }
                }
            }
        });

        request();
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void request() {
        if (onData) {
            return;
        }
        isLoading = true;
        mSwipeRefreshLayout.setRefreshing(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startIndex", mCount + "");
        params.put("endIndex", mCount + SIZE + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_QUERY_MSGS, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                LogUtil.d("dxz", response);
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    // TODO:
                } else {
                    new DDAlertDialog.Builder(getActivity())
                            .setTitle("提示").setMessage("网络问题请重试！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getFragmentManager().popBackStack();
                                }
                            }).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d("dxz", error.toString());
                new DDAlertDialog.Builder(getActivity())
                        .setTitle("提示").setMessage("网络问题请重试！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        request();
    }
}
