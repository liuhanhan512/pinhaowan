package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.OrderModel;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.OrderSlidingAdapter;
import com.hwand.pinhaowanr.widget.calendar.CalendarGridView;
import com.hwand.pinhaowanr.widget.calendar.CalendarUtils;
import com.hwand.pinhaowanr.widget.calendar.UniformGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class OrderFragment extends BaseFragment implements OrderSlidingAdapter.OnSlidingViewClickListener {

    private List<Integer> mDays = new ArrayList<Integer>();
    private List<OrderModel> orders = new ArrayList<OrderModel>();
    private int curMonthInt;

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

    private View mEmptyView;
    private TextView mEmptyText;
    private CalendarGridView mCalendarGridView;
    private View mContentView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private OrderSlidingAdapter mAdapter;

    private Drawable mCalendarItemSelectedTip;

    private static final int[] WEEK_WORDS = new int[]{
            R.string.calendar_sunday,
            R.string.calendar_monday,
            R.string.calendar_tuesday,
            R.string.calendar_wednesday,
            R.string.calendar_thursday,
            R.string.calendar_friday,
            R.string.calendar_saturday
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mEmptyView = mFragmentView.findViewById(R.id.empty_layout);
        mEmptyText = (TextView) mFragmentView.findViewById(R.id.empty_text);
        mEmptyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
        mEmptyView.setVisibility(View.GONE);
        mContentView = mFragmentView.findViewById(R.id.content_layout);
        mCalendarGridView = (CalendarGridView) mFragmentView.findViewById(R.id.calendar_body);
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrderSlidingAdapter(getActivity(), this);

        mRecyclerView.setAdapter(mAdapter);

        UniformGridView calendarTitle = (UniformGridView) mFragmentView.findViewById(R.id.calendar_title);

        calendarTitle.setUniformGridViewAdapter(new UniformGridView.UniformGridViewAdapter() {
            @Override
            public int getWidthCount() {
                return CalendarGridView.COLUMN_COUNT;
            }

            @Override
            public int getHeightCount() {
                return 1;
            }

            @Override
            public View getView(int px, int py, UniformGridView parent, View oldView) {
                View view;
                if (oldView == null) {
                    view = View.inflate(getActivity(), R.layout.activity_attendance_calendar_week, null);
                } else {
                    view = oldView;
                }
                TextView tv = (TextView) view.findViewById(R.id.calendar_week_text);
                tv.setText(WEEK_WORDS[px]);
                return view;
            }
        });


        mCalendarGridView.setCalendarGridViewAdapter(new CalendarGridView.CalendarGridViewAdapter() {
            @Override
            public View getView(int dayInt, boolean isCurrentMonth, View oldView) {
                View view;
                CalendarItemHolder holder;
                if (oldView == null) {
                    view = View.inflate(getActivity(), R.layout.item_calendar_day, null);
                    holder = new CalendarItemHolder(view);
                    view.setTag(holder);
                } else {
                    view = oldView;
                    holder = (CalendarItemHolder) view.getTag();
                }
                holder.setText(String.valueOf(CalendarUtils.getDisplayDay(dayInt)));
                LogUtil.d("dxz", curMonthInt + "");
                LogUtil.d("dxz",CalendarUtils.getPureMonthInt(dayInt)+"");
                holder.setIsYesterday(dayInt < CalendarUtils.getToday());
                holder.setIsCurrentMonth(CalendarUtils.getPureMonthInt(dayInt) == curMonthInt);
                holder.setSelectedTip(mDays.contains(dayInt));

                return view;
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    OrderModel order = orders.get(lastVisibleItem);
                    int datInt = CalendarUtils.getDateInt(order.getStartTime());
                    int monthInt = CalendarUtils.getPureMonthInt(datInt);
                    if (monthInt != curMonthInt) {
                        switchMonth(monthInt);
                    }
                } catch (Exception ex) {

                }
            }
        });
        mCalendarGridView.setMonthInt(CalendarUtils.getPureMonthInt(CalendarUtils.getToday()));
        request();

    }

    private void switchMonth(int monthInt) {
        LogUtil.d("dxz", monthInt + "");
        this.curMonthInt = monthInt;
        mCalendarGridView.setMonthInt(curMonthInt);
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void request() {
        Map<String, String> params = new HashMap<String, String>();
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_QUERY_MY_ORDERS, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                if (!TextUtils.isEmpty(response)) {
                    List<OrderModel> datas = OrderModel.arrayFromData(response);
                    if (datas != null && datas.size() > 0) {
                        LogUtil.d("dxz", datas.size() + "");
                        mDays.clear();
                        for (OrderModel order : datas) {
                            int datInt = CalendarUtils.getDateInt(order.getStartTime());
                            mDays.add(datInt);
                        }
                        orders.clear();
                        orders.addAll(datas);
                        mEmptyView.setVisibility(View.GONE);
                        mContentView.setVisibility(View.VISIBLE);
                        int datInt = CalendarUtils.getDateInt(orders.get(0).getStartTime());
                        int monthInt = CalendarUtils.getPureMonthInt(datInt);
                        switchMonth(monthInt);
                        mAdapter.update(orders);
                    } else {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mEmptyText.setText("还没有预约,快去首页看看吧！");
                        mContentView.setVisibility(View.GONE);
                    }
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyText.setText("还没有预约,快去首页看看吧！");
                    mContentView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new DDAlertDialog.Builder(getActivity())
                        .setTitle("提示").setMessage("网络问题请重试！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mEmptyView.setVisibility(View.VISIBLE);
                                mEmptyText.setText("内容为空,点击刷新");
                                mContentView.setVisibility(View.GONE);
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        mAdapter.removeData(position);
    }

    private class CalendarItemHolder {

        private TextView mDateText;

        public CalendarItemHolder(View v) {
            mDateText = (TextView) v.findViewById(R.id.calendar_date_text);
        }

        public void setText(String text) {
            mDateText.setText(text);
        }

        private void setSelectedBackground() {
            if (mCalendarItemSelectedTip == null) {
                mCalendarItemSelectedTip = getActivity().getResources().getDrawable(R.drawable.circle_solid_red_bg);
            }
            mDateText.setBackgroundDrawable(mCalendarItemSelectedTip);
            mDateText.setSelected(true);
        }

        public void setSelectedTip(boolean isSelected) {
            if (isSelected) {
                setSelectedBackground();
            } else {
                mDateText.setBackgroundDrawable(null);
                mDateText.setSelected(false);
            }
        }

        public void setIsCurrentMonth(boolean isCurrentMonth) {
            if (isCurrentMonth) {
                mDateText.setAlpha(1);
            } else {
                mDateText.setAlpha(0.5f);
            }
        }

        public void setIsYesterday(boolean isYesterday) {
            if (isYesterday) {
                mDateText.setAlpha(0.5f);
            } else {
                mDateText.setAlpha(1);
            }
        }
    }
}
