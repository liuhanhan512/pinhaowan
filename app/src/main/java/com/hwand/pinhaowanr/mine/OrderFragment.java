package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
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
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.OrderSlidingAdapter;
import com.hwand.pinhaowanr.widget.calendar.CalendarGridView;
import com.hwand.pinhaowanr.widget.calendar.UniformGridView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

/**
 * Created by dxz on 15/12/01.
 */
public class OrderFragment extends BaseFragment {

    private Map<String, List<OrderModel>> mDataMap = new ArrayMap<String, List<OrderModel>>();

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

    private OrderSlidingAdapter mAdapter;

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

        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

//        mRecyclerView.setAdapter(mAdapter);

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
                // TODO:
                if (!TextUtils.isEmpty(response)) {
                    List<OrderModel> datas = OrderModel.arrayFromData(response);
                    if (datas != null && datas.size() > 0) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        AndTools.showToast("已经没有更多预约");
                    }
                } else {
                    AndTools.showToast("已经没有更多预约");
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
            }
        });
    }

    private void filterData(List<OrderModel> datas) {
        mDataMap.clear();
        Set<String> keys = new TreeSet<String>();
        for (OrderModel order : datas) {
            Date start = new Date(order.getStartTime());
            Locale aLocale = Locale.US;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM", new DateFormatSymbols(aLocale));
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            String month = fmt.format(start);
            keys.add(month);
        }

        for (String key : keys) {
            List<OrderModel> orders = new ArrayList<OrderModel>();
            for (OrderModel order : datas) {
                Date start = new Date(order.getStartTime());
                Locale aLocale = Locale.US;
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM", new DateFormatSymbols(aLocale));
                fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                String month = fmt.format(start);
                if (TextUtils.equals(key, month)) {
                    orders.add(order);
                }
            }
            mDataMap.put(key, orders);
        }

    }

}
