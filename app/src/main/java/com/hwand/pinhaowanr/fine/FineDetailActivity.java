package com.hwand.pinhaowanr.fine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ClassDetailModel;
import com.hwand.pinhaowanr.model.ClassDetailTitleModel;
import com.hwand.pinhaowanr.model.HomePageModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.BizUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好玩详情页
 * Created by hanhanliu on 15/11/23.
 */
public class FineDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private TextView mGiftTickets;

    private static final String HOME_PAGE_MODEL_KEY = "HOME_PAGE_MODEL_KEY";

    private HomePageModel mHomePageModel;

    private ClassDetailModel mClassDetailModel;

    private List<ClassDetailTitleModel> classDetailTitleModels = new ArrayList<ClassDetailTitleModel>();

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FineDetailActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, HomePageModel homePageModel) {
        Intent intent = new Intent();
        intent.setClass(context, FineDetailActivity.class);
        intent.putExtra(HOME_PAGE_MODEL_KEY, homePageModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_detail_layout);
        initIntentValues();
        initTitle();
        initViews();
        fetchData();
    }

    private void initIntentValues() {
        mHomePageModel = (HomePageModel) getIntent().getSerializableExtra(HOME_PAGE_MODEL_KEY);
    }

    private void initTitle() {
        setActionBarTtile(mHomePageModel.getClassName());
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mGiftTickets = (TextView) findViewById(R.id.gift_ticket_text);
        if (mHomePageModel.getViewType() != 3) {//游戏类叫赠票，其他叫分享
            mGiftTickets.setText(getString(R.string.share_text));
        }

        findViewById(R.id.contact_layout).setOnClickListener(this);
        findViewById(R.id.reservation_layout).setOnClickListener(this);
        findViewById(R.id.gift_ticket_layout).setOnClickListener(this);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private TextView mTel;
    private TextView mHour;
    private TextView mAge;

    private View initHeaderView() {

        View headerView = View.inflate(this, R.layout.fine_detail_list_header_layout, null);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.image);
        AndTools.displayImage(null, mHomePageModel.getPictureUrl(), imageView);

        TextView name = (TextView) headerView.findViewById(R.id.name);
        name.setText(mHomePageModel.getClassName());

        TextView tickets = (TextView) headerView.findViewById(R.id.tickets);
        tickets.setText(getString(R.string.remainder_tickets, mHomePageModel.getRemainTicket()));

        TextView address = (TextView) headerView.findViewById(R.id.address);
        address.setText(mHomePageModel.getDetailAddress());

        mTel = (TextView) headerView.findViewById(R.id.tel);
        mHour = (TextView) headerView.findViewById(R.id.hours);
        mAge = (TextView) headerView.findViewById(R.id.age);

        return headerView;
    }

    private void fetchData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mHomePageModel.getId() + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_CLASS_DETAIL, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mClassDetailModel = gson.fromJson(response, ClassDetailModel.class);
                    updateViews();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateViews() {
        if (mClassDetailModel != null) {

            mTel.setText(getString(R.string.fine_detail_tel, mClassDetailModel.getTelephone()));
            mHour.setText(getString(R.string.fine_detail_hour, mClassDetailModel.getBusineTime()));
            mAge.setText(getString(R.string.fine_detail_age, mClassDetailModel.getMinAge(), mClassDetailModel.getMinAge()));

            List<ClassDetailTitleModel> classDetailTitleModels = mClassDetailModel.getTitleList();
            if (classDetailTitleModels != null) {
                this.classDetailTitleModels.clear();
                this.classDetailTitleModels.addAll(classDetailTitleModels);
                mAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.contact_layout:
                onContactClick();
                break;
            case R.id.reservation_layout:
                onReservationClick();
                break;
            case R.id.gift_ticket_layout:
                onGiftTicketClick();
                break;
        }
    }

    private void onContactClick() {
        if (mClassDetailModel != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mClassDetailModel.getTelephone()));
            startActivity(intent);
        }

    }

    private void onReservationClick() {
        ReservationActivity.launch(this, mClassDetailModel,mHomePageModel.getId());
    }

    private void onGiftTicketClick() {
        if (mHomePageModel.getViewType() == 3) {//赠票

        } else {//分享
            onShare();
        }
    }

    private void onShare() {
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (mHomePageModel != null) {

            BizUtil.share(this, "title", "content", mHomePageModel.getPictureUrl(), "http://www.baidu.com", bitmap, mController);
        }
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, qqAppId,
//                qqSecret);
//        qqSsoHandler.addToSocialSDK();
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, qqAppId,
//                qqSecret);
//        qZoneSsoHandler.addToSocialSDK();
        mController.openShare(this, false);
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return classDetailTitleModels.size();
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
            if (convertView == null) {
                convertView = LayoutInflater.from(FineDetailActivity.this)
                        .inflate(R.layout.fine_detail_list_item_layout, viewGroup, false);
            }

            ClassDetailTitleModel classDetailTitleModel = classDetailTitleModels.get(position);
            TextView title = CommonViewHolder.get(convertView, R.id.title);
            TextView content = CommonViewHolder.get(convertView, R.id.content);
            ImageView image = CommonViewHolder.get(convertView, R.id.image);
            int screenWidth = AndTools.getScreenWidth(FineDetailActivity.this);
            int height = screenWidth * 9 / 16;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
            layoutParams.height = height;
            image.setLayoutParams(layoutParams);

            title.setText(classDetailTitleModel.getTitle());
            content.setText(classDetailTitleModel.getContent());
            AndTools.displayImage(null, classDetailTitleModel.getUrl(), image);
            return convertView;
        }
    }

}
