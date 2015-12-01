package com.hwand.pinhaowanr.community;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

/**
 * 社区--拼拼--小伙伴页面
 * Created by hanhanliu on 15/11/30.
 */
public class SmallPartnerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    public static BaseFragment newInstance(){
        SmallPartnerFragment fragment = new SmallPartnerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_small_partner_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout)mFragmentView. findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView)mFragmentView . findViewById(R.id.listview);
        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private View initHeaderView(){

        View headerView = View.inflate(getActivity() , R.layout.small_partner_list_header_layout , null);

        return headerView;
    }

    private void fetchData(){

    }

    @Override
    public void onRefresh() {

    }

    class Adapter extends BaseAdapter{

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
            if(convertView == null){
                convertView = View.inflate(getActivity() , R.layout.small_partner_list_item_layout , null);
            }
            return convertView;
        }
    }
}
