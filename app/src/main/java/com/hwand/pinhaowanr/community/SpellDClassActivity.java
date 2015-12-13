package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.fine.FineDetailActivity;
import com.hwand.pinhaowanr.model.ActivityModel;
import com.hwand.pinhaowanr.model.ClassDetailTitleModel;
import com.hwand.pinhaowanr.model.SpellDCategoryModel;
import com.hwand.pinhaowanr.model.SpellDClassModel;
import com.hwand.pinhaowanr.model.SpellDClassStageModel;
import com.hwand.pinhaowanr.model.SpellDClassTtileModel;
import com.hwand.pinhaowanr.model.SpellDModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.DateUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.SpellDTimeView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页--社区--拼拼--课程详情
 * Created by hanhanliu on 15/12/12.
 */
public class SpellDClassActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String ACTIVITY_ID_KEY = "activity_id_key";

    private int mClassId;

    private static final String SPELL_D_CATEGORY_KEY = "SPELL_D_CATEGORY_KEY";

    private SpellDModel mSpellDModel;

    private ImageView mImage;

    private TextView mAge;

    private LinearLayout mTimeContainer;

    private SpellDClassModel mSpellDClassModel;

    private ListView mListView;

    private Adapter mAdapter;

    private List<SpellDClassTtileModel> spellDClassTtileModels = new ArrayList<SpellDClassTtileModel>();

    public static void launch(Context context , int id){
        Intent intent = new Intent();
        intent.setClass(context , SpellDClassActivity.class);
        intent.putExtra(ACTIVITY_ID_KEY, id);
        context.startActivity(intent);
    }

    public static void launch(Context context , SpellDModel spellDModel){
        Intent intent = new Intent();
        intent.setClass(context, SpellDClassActivity.class);
        intent.putExtra(SPELL_D_CATEGORY_KEY, spellDModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_d_class_detail_layout);
        initIntentValue();
        initTitle();
        initViews();
        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();

    }

    private void initIntentValue(){
        mClassId = getIntent().getIntExtra(ACTIVITY_ID_KEY ,0);
        mSpellDModel = (SpellDModel) getIntent().getSerializableExtra(SPELL_D_CATEGORY_KEY);
        if(mSpellDModel != null){
            mClassId = mSpellDModel.getId();
        }
    }

    private void initTitle(){
        setActionBarTtile(getString(R.string.class_detail));
    }

    private void initViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.addHeaderView(initHeaderView());
        mListView.addFooterView(initFooterView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

    }

    private View initHeaderView() {

        View headerView = View.inflate(this, R.layout.spell_d_class_detail_listview_header_layout, null);
        mImage = (ImageView) headerView.findViewById(R.id.image);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mImage.getLayoutParams();
        layoutParams.height = AndTools.getScreenWidth(this) * 9 / 16;
        mImage.setLayoutParams(layoutParams);
        mAge = (TextView)headerView .findViewById(R.id.age);
        TextView address = (TextView)headerView.findViewById(R.id.address);

        mTimeContainer = (LinearLayout)headerView.findViewById(R.id.time_container);

        if(mSpellDModel != null){
            AndTools.displayImage(null , mSpellDModel.getPictureUrl() , mImage);

            address.setText(mSpellDModel.getDetailAddress());

        }

        return headerView;
    }

    private TextView mOnceCost, mStageCost, mAllYearCost;
    private View initFooterView(){
        View footerView = View.inflate(this , R.layout.spell_d_class_detail_listview_footer_layout , null);

        mOnceCost = (TextView)footerView.findViewById(R.id.one_visit_cost);
        mStageCost = (TextView)footerView.findViewById(R.id.stage_visit_cost);
        mAllYearCost = (TextView)footerView.findViewById(R.id.all_visit_cost);

        return footerView;
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private void fetchData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mClassId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PIN_CLASS_DETAIL, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mSpellDClassModel = gson.fromJson(response , SpellDClassModel.class);
                    if(mSpellDClassModel != null){
                        List<SpellDClassTtileModel> spellDClassTtileModelList = mSpellDClassModel.getTitleList();
                        if(spellDClassTtileModelList != null){
                            spellDClassTtileModels.clear();
                            spellDClassTtileModels.addAll(spellDClassTtileModelList);
                            mAdapter.notifyDataSetChanged();
                        }
                        updateViews();
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

    private void updateViews(){
        mAge.setText(getString(R.string.fine_detail_age , mSpellDClassModel.getMinAge() , mSpellDClassModel.getMaxAge()));

        List<SpellDClassStageModel> spellDClassStageModels = mSpellDClassModel.getStageTimeList();
        if(spellDClassStageModels != null){
            for(SpellDClassStageModel spellDClassStageModel : spellDClassStageModels){
                SpellDTimeView spellDTimeView = new SpellDTimeView(this);
                long startTime = spellDClassStageModel.getStartTime();
                long endTime = spellDClassStageModel.getEndTime();
                SimpleDateFormat formatter = new SimpleDateFormat("E  hh:mm");
                spellDTimeView.setTimeText(getString(R.string.reservation_time ,
                        DateUtil.convertLongToString(startTime , formatter), DateUtil.convertLongToString(endTime , formatter)));
                mTimeContainer.addView(spellDTimeView);
            }
        }

        mOnceCost.setText(mSpellDClassModel.getOncePrice() +"");
        mStageCost.setText(mSpellDClassModel.getStagePrice() +"");
        mAllYearCost.setText(mSpellDClassModel.getYearPrice() + "");
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return spellDClassTtileModels.size();
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
                convertView = LayoutInflater.from(SpellDClassActivity.this)
                        .inflate(R.layout.fine_detail_list_item_layout, viewGroup, false);
            }

            SpellDClassTtileModel spellDClassTtileModel = spellDClassTtileModels.get(position);
            TextView title = CommonViewHolder.get(convertView, R.id.title);
            TextView content = CommonViewHolder.get(convertView, R.id.content);
            ImageView image = CommonViewHolder.get(convertView, R.id.image);
            int screenWidth = AndTools.getScreenWidth(SpellDClassActivity.this);
            int height = screenWidth * 9 / 16;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
            layoutParams.height = height;
            image.setLayoutParams(layoutParams);

            title.setText(spellDClassTtileModel.getTitle());
            content.setText(spellDClassTtileModel.getContent());
            AndTools.displayImage(null, spellDClassTtileModel.getUrl(), image);
            return convertView;
        }
    }
}
