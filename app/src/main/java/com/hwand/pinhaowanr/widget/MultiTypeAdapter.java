package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dxz on 2015/12/4.
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM_FROM = 0;
    private static final int TYPE_ITEM_TO = 1;

    private Context mContext;

    private List<MsgInfo> mDatas = new ArrayList<MsgInfo>();

    public MultiTypeAdapter(Context context, List<MsgInfo> datas) {
        mContext = context;
        mDatas.clear();
        mDatas.addAll(datas);

    }

    public void update(List<MsgInfo> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void update(MsgInfo data) {
        mDatas.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        final MsgInfo msg = mDatas.get(position);
        LogUtil.d("dxz",mDatas.size()+"");
        if (msg != null) {
            LogUtil.d("dxz","getItemViewType");
            if (msg.getSendId() == DataCacheHelper.getInstance().getUserInfo().getRoleId()) {
                return TYPE_ITEM_TO;
            } else {
                return TYPE_ITEM_FROM;
            }
        }
        return TYPE_ITEM_FROM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            MsgViewHolder msgHolder = (MsgViewHolder) holder;
            final MsgInfo msg = mDatas.get(position);
            ImageLoader.getInstance().displayImage(msg.getUrl(), msgHolder.avator);
            msgHolder.tv_name.setText(msg.getName());
            long time = msg.getTime();
            Date date = new Date(time);
            Locale aLocale = Locale.US;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm", new DateFormatSymbols(aLocale));
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            msgHolder.tv_time.setText(fmt.format(date));
            msgHolder.tv_msg.setText(msg.getContent());

        } catch (Exception e) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MsgViewHolder holder;
        LogUtil.d("dxz","viewType"+viewType);
        if (viewType == TYPE_ITEM_FROM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_from, parent, false);
            holder = new MsgViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_to, parent, false);
            holder = new MsgViewHolder(view);
        }

        return holder;
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avator;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_msg;

        public MsgViewHolder(View itemView) {
            super(itemView);
            avator = (CircleImageView) itemView.findViewById(R.id.avator);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
        }
    }

}
