package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.DeleteAllEvent;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.SlidingAdapter;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by dxz on 15/12/01.
 */
public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, SlidingAdapter.OnSlidingViewClickListener {

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
    private View mEmptyView;
    private TextView mEmptyText;

    private SlidingAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mCount = 0;
    private List<MsgInfo> mDatas = new ArrayList<MsgInfo>();
    private boolean isLoading;
    private boolean noData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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
        mAdapter = new SlidingAdapter(getActivity(), mDatas, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 2 表示剩下2个item自动加载，各位自由选择
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
        mEmptyView = mFragmentView.findViewById(R.id.empty_layout);
        mEmptyText = (TextView) mFragmentView.findViewById(R.id.empty_text);
        mEmptyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
        mEmptyView.setVisibility(View.GONE);
        request();
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void request() {
        if (noData) {
            mSwipeRefreshLayout.setRefreshing(false);
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
                LogUtil.d("dxz", response);
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                if (!TextUtils.isEmpty(response)) {
                    List<MsgInfo> datas = MsgInfo.arrayFromData(response);
                    if (datas != null && datas.size() > 0) {
                        mCount += datas.size();
                        mEmptyView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.update(datas);
                    } else {
                        AndTools.showToast("已经没有更多消息");
                        if (mCount == 0) {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            return;
                        }
                        noData = true;
                    }
                } else {
                    AndTools.showToast("已经没有更多消息");
                    if (mCount == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        return;
                    }
                    noData = true;
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
        if (isLoading) {
            LogUtil.d("dxz", "ignore manually update!");
        } else {
            mCount = 0;
            noData = false;
            mAdapter.clear();
            request();//这里多线程也要手动控制isLoading
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MessageActivity.class);
        final MsgInfo msg = mAdapter.getItem(position);

        if (msg.getSendId() == DataCacheHelper.getInstance().getUserInfo().getRoleId()) {
            intent.putExtra(MessageActivity.KEY_INTENT_ID, msg.getAcceptId());
        } else {
            intent.putExtra(MessageActivity.KEY_INTENT_ID, msg.getSendId());
        }
        intent.putExtra(MessageActivity.KEY_INTENT_TYPE, msg.getType());
        startActivity(intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        mAdapter.removeData(position);
        mCount = mAdapter.getItemCount();
    }

    public void onEventMainThread(DeleteAllEvent event) {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        AndTools.showToast("已经没有更多消息");
    }
}
