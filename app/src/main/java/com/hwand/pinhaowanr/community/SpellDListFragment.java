package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.PinClass;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class SpellDListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static SpellDListFragment newInstance() {
        SpellDListFragment fragment = new SpellDListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mId;

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

    private LinearLayoutManager mLayoutManager;
    private CardAdapter mAdapter;

    private List<PinClass> mDatas = new ArrayList<PinClass>();
    private boolean isLoading;
    private boolean noData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mypinclass_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout) mFragmentView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CardAdapter(getActivity(), mDatas);
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
        mEmptyText.setText("对不起，该课程目前没有拼课");
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
        params.put("id", mId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PIN_CLASS_BY_ID, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                if (!TextUtils.isEmpty(response)) {
                    List<PinClass> datas = PinClass.arrayFromData(response);
                    if (datas != null && datas.size() > 0) {
                        mEmptyView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.update(datas);
                    } else {
                        noData = true;
                        mEmptyView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    noData = true;
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
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
            request();//这里多线程也要手动控制isLoading
        }
    }

    private static class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;

        private List<PinClass> mDatas = new ArrayList<PinClass>();

        public CardAdapter(Context context, List<PinClass> datas) {
            mContext = context;
            mDatas = datas;

        }

        public void update(List<PinClass> datas) {
            mDatas.clear();
            mDatas.addAll(datas);
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            try {
                CardViewHolder cardHolder = (CardViewHolder) holder;
                final PinClass pinClass = mDatas.get(position);
                ImageLoader.getInstance().displayImage(pinClass.getUrl(), cardHolder.avator);
                cardHolder.tv_name.setText(pinClass.getCreateRoleName());
//                Date date = new Date(time);
//                Locale aLocale = Locale.US;
//                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm", new DateFormatSymbols(aLocale));
//                fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
//                cardHolder.tv_time.setText(fmt.format(date));
                cardHolder.tv_add.setText(pinClass.getDetailAddress());
                cardHolder.tv_price.setText(Html.fromHtml(StrUtils.unescapeHtml("<font color='#f86150'><b>" + pinClass.getMoney())));
                cardHolder.tv_count.setText(Html.fromHtml(StrUtils.unescapeHtml("<font color='#f86150'><b>" + pinClass.getCurrentRole()) + "/" + pinClass.getMaxRole()));
                cardHolder.tv_add.setText(pinClass.getDetailAddress());

            } catch (Exception e) {

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardViewHolder holder;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_pin_class, parent, false);
            holder = new CardViewHolder(view);

            return holder;
        }

        class CardViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView avator;
            public TextView tv_name;
            public TextView tv_time;
            public TextView tv_add;
            public TextView tv_count;
            public TextView tv_price;
            public TextView btn_pin;
            public ImageView btn_more;
            public LinearLayout members;
            public CircleImageView member_1;
            public CircleImageView member_2;
            public CircleImageView member_3;
            public CircleImageView member_4;
            public CircleImageView member_5;

            public CardViewHolder(View itemView) {
                super(itemView);
                avator = (CircleImageView) itemView.findViewById(R.id.avator);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                tv_add = (TextView) itemView.findViewById(R.id.tv_add);
                tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                btn_pin = (TextView) itemView.findViewById(R.id.btn_pin);
                btn_more = (ImageView) itemView.findViewById(R.id.btn_more);
                members = (LinearLayout) itemView.findViewById(R.id.layout_member);
            }
        }

    }
}
