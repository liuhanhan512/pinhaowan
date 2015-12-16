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
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class SlidingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SlidingButtonView.OnSlidingButtonListener {

    private Context mContext;

    private OnSlidingViewClickListener mIDeleteBtnClickListener;

    private List<MsgInfo> mDatas = new ArrayList<MsgInfo>();

    private SlidingButtonView mMenu = null;

    public SlidingAdapter(Context context, List<MsgInfo> datas, OnSlidingViewClickListener listener) {
        mContext = context;
        mIDeleteBtnClickListener = listener;
        mDatas = datas;

    }

    public void update(List<MsgInfo> datas) {
        mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public MsgInfo getItem(int position) {
        final MsgInfo msg = mDatas.get(position);
        LogUtil.d("dxz", mDatas.size() + "");
        if (msg != null) {
            return msg;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            final MsgViewHolder msgHolder = (MsgViewHolder) holder;
            final MsgInfo msg = mDatas.get(position);
            //设置内容布局的宽为屏幕宽度
            msgHolder.layout_content.getLayoutParams().width = AndTools.getScreenWidth(mContext);

            ImageLoader.getInstance().displayImage(msg.getUrl(), msgHolder.head);
            msgHolder.tv_name.setText(msg.getName());
            long time = msg.getTime();
            Date date = new Date(time);
            Locale aLocale = Locale.US;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm", new DateFormatSymbols(aLocale));
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            msgHolder.tv_time.setText(fmt.format(date));
            msgHolder.tv_msg.setText(msg.getContent());

            msgHolder.layout_content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        int n = msgHolder.getPosition();
                        mIDeleteBtnClickListener.onItemClick(v, n);
                    }

                }
            });
            msgHolder.btn_Delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = msgHolder.getPosition();
                    mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_slid, parent, false);
        MsgViewHolder holder = new MsgViewHolder(view);

        return holder;
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView head;
        public TextView btn_Delete;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_msg;
        public ViewGroup layout_content;

        public MsgViewHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.head);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);

            ((SlidingButtonView) itemView).setSlidingButtonListener(SlidingAdapter.this);
        }
    }

    public void addData(int position) {
//        mDatas.add(position, "添加项");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        final MsgInfo msg = mDatas.get(position);
        delMsg(position, msg);
    }

    private void delMsg(final int position, final MsgInfo msg) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sendId", msg.getSendId() + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_DEL_MSG, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                // 结果（result）0 失败 1 成功
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    mDatas.remove(position);
                    notifyItemRemoved(position);
                    if (mDatas.size() <= 0) {
                        EventBus.getDefault().post(new DeleteAllEvent());
                    }
                    AndTools.showToast("删除成功！");
                } else {
                    AndTools.showToast("删除失败！");
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
