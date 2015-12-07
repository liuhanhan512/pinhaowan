package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.AndTools;
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

    private static final int TYPE_ITEM_LEFT = 0;
    private static final int TYPE_ITEM_RIGHT = 1;

    private Context mContext;

    private List<MsgInfo> mDatas = new ArrayList<MsgInfo>();

    public MultiTypeAdapter(Context context, List<MsgInfo> datas) {
        mContext = context;
        mDatas = datas;

    }

    public void update(List<MsgInfo> datas) {
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
            MsgViewHolder msgHolder = (MsgViewHolder) holder;
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

                }
            });
            msgHolder.btn_Delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = holder.getLayoutPosition();
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
        }
    }

}
