package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.PinClassModel;
import com.hwand.pinhaowanr.model.PinClassPeopleModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 某个课程的拼课列表
 * Created by hanhanliu on 15/12/2.
 */
public class SpellDListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Adapter mAdapter;

    private static final String CLASS_ID_KEY = "CLASS_ID_KEY";
    private static final String CLASS_NAME_KEY = "CLASS_NAME_KEY";

    private int mClassId = 0;

    private List<PinClassModel> mPinClassModels = new ArrayList<PinClassModel>();

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SpellDListActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, int classId, String name) {
        Intent intent = new Intent();
        intent.putExtra(CLASS_ID_KEY, classId);
        intent.putExtra(CLASS_NAME_KEY, name);
        intent.setClass(context, SpellDListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_d_list_layout);
        initIntentValues();
        initTitle();
        initViews();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    private void initIntentValues() {
        mClassId = getIntent().getIntExtra(CLASS_ID_KEY, 0);
    }

    private void initTitle() {
        String name = getIntent().getStringExtra(CLASS_NAME_KEY);
        setActionBarTtile(name);
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.listview);
//        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private void fetchData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mClassId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PIN_CLASS_BY_ID, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    List<PinClassModel> pinClassModels = PinClassModel.arrayFromData(response);
                    if (pinClassModels != null) {
                        mPinClassModels.clear();
                        mPinClassModels.addAll(pinClassModels);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPinClassModels.size();
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
                convertView = View.inflate(SpellDListActivity.this, R.layout.spell_d_class_list_item_layout, null);
            }
            PinClassModel pinClassModel = mPinClassModels.get(position);
            CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            AndTools.displayImage(null, pinClassModel.getCreateRoleURL(), avatar);

            TextView launchName = (TextView) convertView.findViewById(R.id.tv_name);
            launchName.setText(pinClassModel.getCreateRoleName());

            TextView time = (TextView) convertView.findViewById(R.id.tv_time);
            time.setText(getString(R.string.spell_d_class_time, pinClassModel.getPinTime()));

            TextView address = (TextView) convertView.findViewById(R.id.tv_address);
            address.setText(pinClassModel.getDetailAddress());

            TextView peopleCount = (TextView) convertView.findViewById(R.id.tv_count);
            peopleCount.setText(getString(R.string.spell_d_people_count, pinClassModel.getCurrentRole(), pinClassModel.getMaxRole()));

            TextView price = (TextView) convertView.findViewById(R.id.tv_price);
            price.setText(getString(R.string.price, pinClassModel.getMoney()));

            convertView.findViewById(R.id.btn_pin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            List<PinClassPeopleModel> pinClassPeopleModels = pinClassModel.getAttendList();
            if (pinClassPeopleModels != null) {
                int peopleSize = pinClassPeopleModels.size();
                if (peopleSize > 0) {
                    CircleImageView avatar1 = (CircleImageView) convertView.findViewById(R.id.avatar1);
                    AndTools.displayImage(null, pinClassPeopleModels.get(0).getUrl(), avatar1);
                }

                if (peopleSize > 1) {
                    CircleImageView avatar2 = (CircleImageView) convertView.findViewById(R.id.avatar2);
                    AndTools.displayImage(null, pinClassPeopleModels.get(1).getUrl(), avatar2);
                }

                if (peopleSize > 2) {
                    CircleImageView avatar3 = (CircleImageView) convertView.findViewById(R.id.avatar3);
                    AndTools.displayImage(null, pinClassPeopleModels.get(2).getUrl(), avatar3);
                }

                if (peopleSize > 3) {
                    CircleImageView avatar4 = (CircleImageView) convertView.findViewById(R.id.avatar4);
                    AndTools.displayImage(null, pinClassPeopleModels.get(3).getUrl(), avatar4);
                }

                if (peopleSize > 4) {
                    CircleImageView avatar5 = (CircleImageView) convertView.findViewById(R.id.avatar5);
                    AndTools.displayImage(null, pinClassPeopleModels.get(4).getUrl(), avatar5);
                }

                if (peopleSize > 5) {
                    convertView.findViewById(R.id.btn_more).setVisibility(View.VISIBLE);
                } else {
                    convertView.findViewById(R.id.btn_more).setVisibility(View.GONE);
                }
            }
            return convertView;
        }
    }
}
