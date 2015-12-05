package com.hwand.pinhaowanr.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.widget.SlidingAdapter;

/**
 * Created by dxz on 15/12/01.
 */
public class OrderFragment extends BaseFragment {

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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

    private RecyclerView mRecyclerView;

    private SlidingAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("消息");
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

}
