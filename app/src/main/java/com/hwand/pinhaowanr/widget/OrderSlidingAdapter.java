package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.DeleteAllEvent;
import com.hwand.pinhaowanr.model.OrderModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.greenrobot.event.EventBus;

/**
 * Created by dxz on 2015/12/4.
 */
public class OrderSlidingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SlidingButtonView.OnSlidingButtonListener {

    private Context mContext;

    private OnSlidingViewClickListener mIDeleteBtnClickListener;

    private List<OrderModel> mDatas = new ArrayList<OrderModel>();

    private SlidingButtonView mMenu = null;

    private static final int[] WEEK_WORDS = new int[]{
            R.string.calendar_sunday,
            R.string.calendar_monday,
            R.string.calendar_tuesday,
            R.string.calendar_wednesday,
            R.string.calendar_thursday,
            R.string.calendar_friday,
            R.string.calendar_saturday
    };

    public OrderSlidingAdapter(Context context, List<OrderModel> datas, OnSlidingViewClickListener listener) {
        mContext = context;
        mIDeleteBtnClickListener = listener;
        mDatas = datas;

    }

    public void update(List<OrderModel> datas) {
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
            final OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            final OrderModel order = mDatas.get(position);
            //设置内容布局的宽为屏幕宽度
            orderViewHolder.layout_content.getLayoutParams().width = AndTools.getScreenWidth(mContext);

            orderViewHolder.tv_name.setText(order.getName());
            long startTime = order.getStartTime();
            long endTime = order.getEndTime();
            Date dateS = new Date(startTime);
            Date dateE = new Date(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateS);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            SimpleDateFormat fmt = new SimpleDateFormat("hh:mm", Locale.US);
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            orderViewHolder.tv_time.setText(fmt.format(dateS) + "-" + fmt.format(dateE));
            orderViewHolder.tv_day.setText(month + "月" + day + "日" + "星期" + WEEK_WORDS[weekday]);

            orderViewHolder.layout_content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        int n = orderViewHolder.getPosition();
                        mIDeleteBtnClickListener.onItemClick(v, n);
                    }

                }
            });
            orderViewHolder.btn_Delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = orderViewHolder.getPosition();
                    mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_slid_order, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);

        return holder;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView btn_Delete;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_day;
        public ViewGroup layout_content;

        public OrderViewHolder(View itemView) {
            super(itemView);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);

            ((SlidingButtonView) itemView).setSlidingButtonListener(OrderSlidingAdapter.this);
        }
    }

    public void addData(int position) {
//        mDatas.add(position, "添加项");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        final OrderModel order = mDatas.get(position);
        delete(position, order.getSubscribeId());
    }

    private void delete(final int position, int subscribeId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("subscribeId", subscribeId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_DEL_MY_ORDER, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                // 结果（result）0 失败（本活动已经开始不可以删除） 1 成功
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    mDatas.remove(position);
                    notifyItemRemoved(position);
                    if (mDatas.size() <= 0) {
                        EventBus.getDefault().post(new DeleteAllEvent());
                    }
                    AndTools.showToast("成功取消！");
                } else {
                    new DDAlertDialog.Builder(mContext)
                            .setTitle("提示").setMessage("本活动已经开始不可以取消！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d("dxz", error.toString());
                new DDAlertDialog.Builder(mContext)
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

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        LogUtil.i("dxz", "onMenuIsOpen");
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        LogUtil.i("dxz", "closeMenu");
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        LogUtil.i("dxz", "mMenu为null");
        return false;
    }


    public interface OnSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }
}
