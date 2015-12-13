package com.hwand.pinhaowanr.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.mine.UserInfoActivity;
import com.hwand.pinhaowanr.model.FleaActivityModel;
import com.hwand.pinhaowanr.model.NewActivityModel;
import com.hwand.pinhaowanr.model.RoleModel;
import com.hwand.pinhaowanr.model.SmallPartnerModel;
import com.hwand.pinhaowanr.model.SuperMomModel;
import com.hwand.pinhaowanr.model.TheCommunityActivityModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.DateUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社区--小伙伴页面
 * Created by hanhanliu on 15/11/30.
 */
public class SmallPartnerFragment extends BaseCommunityFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private View mEmptyView;

    private Adapter mAdapter;

    private SmallPartnerModel mSmallPartnerModel;

    private List<TheCommunityActivityModel> theCommunityActivityModels = new ArrayList<TheCommunityActivityModel>();

    private List<SuperMomModel> superMomModels;

    private List<NewActivityModel> newActivityModels;

    private List<FleaActivityModel> fleaActivityModels;

    public static BaseCommunityFragment newInstance() {
        SmallPartnerFragment fragment = new SmallPartnerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_small_partner_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout) mFragmentView.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeRefreshLayout.setColorScheme(android.R.color.white, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mListView = (ListView) mFragmentView.findViewById(R.id.listview);
        mListView.addHeaderView(initHeaderView());

        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mEmptyView = mFragmentView.findViewById(R.id.empty_layout);
        mFragmentView.findViewById(R.id.empty_text).setOnClickListener(this);

        fetchData();
    }


    private LinearLayout superMomLayout;
    private RelativeLayout avatarLayout1, avatarLayout2, avatarLayout3, avatarLayout4, avatarLayout5, avatarLayout6;
    private CircleImageView avatar1, avatar2, avatar3, avatar4, avatar5, avatar6;
    private TextView name1, name2, name3, name4, name5, name6;

    private LinearLayout newPlayLayout;
    private RelativeLayout playLayout1, playLayout2;
    private ImageView playImage1, playImage2;
    private TextView playContent1, playContent2;

    private RelativeLayout fleaLayout;
    private ImageView fleaImage;

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ActivityDetailActivity.launch(getActivity(), theCommunityActivityModels.get(i - mListView.getHeaderViewsCount()));
        }
    };

    private View initHeaderView() {

        View headerView = View.inflate(getActivity(), R.layout.small_partner_list_header_layout, null);
        superMomLayout = (LinearLayout) headerView.findViewById(R.id.super_mom_layout);
        avatarLayout1 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout1);
        avatarLayout2 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout2);
        avatarLayout3 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout3);
        avatarLayout4 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout4);
        avatarLayout5 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout5);
        avatarLayout6 = (RelativeLayout) headerView.findViewById(R.id.avatar_layout6);

        avatarLayout1.setOnClickListener(this);
        avatarLayout2.setOnClickListener(this);
        avatarLayout3.setOnClickListener(this);
        avatarLayout4.setOnClickListener(this);
        avatarLayout5.setOnClickListener(this);
        avatarLayout6.setOnClickListener(this);


        avatar1 = (CircleImageView) headerView.findViewById(R.id.avatar1);
        avatar2 = (CircleImageView) headerView.findViewById(R.id.avatar2);
        avatar3 = (CircleImageView) headerView.findViewById(R.id.avatar3);
        avatar4 = (CircleImageView) headerView.findViewById(R.id.avatar4);
        avatar5 = (CircleImageView) headerView.findViewById(R.id.avatar5);
        avatar6 = (CircleImageView) headerView.findViewById(R.id.avatar6);

        name1 = (TextView) headerView.findViewById(R.id.name1);
        name2 = (TextView) headerView.findViewById(R.id.name2);
        name3 = (TextView) headerView.findViewById(R.id.name3);
        name4 = (TextView) headerView.findViewById(R.id.name4);
        name5 = (TextView) headerView.findViewById(R.id.name5);
        name6 = (TextView) headerView.findViewById(R.id.name6);


        newPlayLayout = (LinearLayout) headerView.findViewById(R.id.new_play_layout);
        playLayout1 = (RelativeLayout) headerView.findViewById(R.id.play_layout1);
        playLayout2 = (RelativeLayout) headerView.findViewById(R.id.play_layout2);
        playImage1 = (ImageView) headerView.findViewById(R.id.play_jmage1);
        playImage2 = (ImageView) headerView.findViewById(R.id.play_jmage2);
        playContent1 = (TextView) headerView.findViewById(R.id.play_text1);
        playContent2 = (TextView) headerView.findViewById(R.id.play_text2);

        fleaLayout = (RelativeLayout) headerView.findViewById(R.id.flea_market_layout);
        fleaImage = (ImageView) headerView.findViewById(R.id.flea_market_image);

        return headerView;
    }

    @Override
    public void fetchData() {
        Map<String, String> params = new HashMap<String, String>();
        /**
         params.put("cityType" , MainApplication.getInstance().getCityType() + "");
         AMapLocation mapLocation = MainApplication.getInstance().getAmapLocation();
         if(mapLocation != null){
         params.put("lng" , mapLocation.getLongitude() + "");
         params.put("lat" , mapLocation.getLatitude() + "");
         }
         */

        params.put("cityType", "1");
        params.put("lng", "121.430829");
        params.put("lat", "31.228781");

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_BUDDY_INFO, params);

        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!TextUtils.isEmpty(response)) {

                    mEmptyView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    Gson gson = new Gson();
                    mSmallPartnerModel = gson.fromJson(response, SmallPartnerModel.class);
                    updateViews();
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mEmptyView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        });
    }

    private void signUp(int activityId){
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", activityId + "");

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_APPLY_ACTIVITY, params);

        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndTools.showToast(getString(R.string.sign_up_success_tips));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AndTools.showToast(getString(R.string.sign_up_fail_tips));
            }
        });
    }

    private void updateViews() {
        if (mSmallPartnerModel != null) {
            // update listview
            updateListView();
            //update super mom
            updateSuperMom();

            //update new play
            updateNewPlay();

            //update Flea
            updateFleaView();
        }

    }

    private void updateListView() {
        List<TheCommunityActivityModel> theCommunityActivityModels = mSmallPartnerModel.getNaActivityList();
        if (theCommunityActivityModels != null) {
            this.theCommunityActivityModels.clear();
            this.theCommunityActivityModels.addAll(theCommunityActivityModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateSuperMom() {
        superMomModels = mSmallPartnerModel.getRoleList();
        if (superMomModels != null) {
            int size = superMomModels.size();
            if (size > 0) {
                superMomLayout.setVisibility(View.VISIBLE);
                if (size >= 1) {
                    avatarLayout1.setVisibility(View.VISIBLE);
                    avatarLayout2.setVisibility(View.INVISIBLE);
                    avatarLayout3.setVisibility(View.INVISIBLE);
                    avatarLayout4.setVisibility(View.INVISIBLE);
                    avatarLayout5.setVisibility(View.INVISIBLE);
                    avatarLayout6.setVisibility(View.INVISIBLE);

                    SuperMomModel superMomModel = superMomModels.get(0);
                    AndTools.displayImage(null, superMomModel.getUrl(), avatar1);
                    name1.setText(superMomModel.getName());
                }

                if (size >= 2) {
                    avatarLayout2.setVisibility(View.VISIBLE);
                    avatarLayout3.setVisibility(View.INVISIBLE);
                    avatarLayout4.setVisibility(View.INVISIBLE);
                    avatarLayout5.setVisibility(View.INVISIBLE);
                    avatarLayout6.setVisibility(View.INVISIBLE);
                    SuperMomModel superMomModel = superMomModels.get(1);
                    AndTools.displayImage(null, superMomModel.getUrl(), avatar2);
                    name2.setText(superMomModel.getName());
                }

                if (size >= 3) {
                    avatarLayout3.setVisibility(View.VISIBLE);
                    avatarLayout4.setVisibility(View.INVISIBLE);
                    avatarLayout5.setVisibility(View.INVISIBLE);
                    avatarLayout6.setVisibility(View.INVISIBLE);

                    SuperMomModel superMomModel = superMomModels.get(2);
                    AndTools.displayImage(null, superMomModel.getUrl(), avatar3);
                    name3.setText(superMomModel.getName());
                }

                if (size >= 4) {
                    avatarLayout4.setVisibility(View.VISIBLE);
                    avatarLayout5.setVisibility(View.INVISIBLE);
                    avatarLayout6.setVisibility(View.INVISIBLE);

                    SuperMomModel superMomModel = superMomModels.get(3);
                    AndTools.displayImage(null, superMomModel.getUrl(), avatar4);
                    name4.setText(superMomModel.getName());
                }

                if (size >= 5) {
                    avatarLayout5.setVisibility(View.VISIBLE);
                    avatarLayout6.setVisibility(View.INVISIBLE);

                    SuperMomModel superMomModel = superMomModels.get(3);
                    AndTools.displayImage(null, superMomModel.getUrl(), avatar5);
                    name5.setText(superMomModel.getName());
                }

                if (size > 5) {
                    avatarLayout6.setVisibility(View.VISIBLE);
                }
                // TODO:test
                avatarLayout6.setVisibility(View.VISIBLE);
            } else {
                superMomLayout.setVisibility(View.GONE);
            }

        }
    }

    private void updateNewPlay() {
        newActivityModels = mSmallPartnerModel.getNewActivityList();
        if (newActivityModels != null && newActivityModels.size() > 0) {
            newPlayLayout.setVisibility(View.VISIBLE);
            int size = newActivityModels.size();
            if (size >= 1) {
                playLayout1.setVisibility(View.VISIBLE);
                playLayout2.setVisibility(View.INVISIBLE);

                NewActivityModel newActivityModel = newActivityModels.get(0);
                AndTools.displayImage(null, newActivityModel.getUrl(), playImage1);
                playContent1.setText(newActivityModel.getTitle());
            }
            if (size >= 2) {
                playLayout1.setVisibility(View.VISIBLE);
                playLayout2.setVisibility(View.VISIBLE);

                NewActivityModel newActivityModel = newActivityModels.get(1);
                AndTools.displayImage(null, newActivityModel.getUrl(), playImage2);
                playContent2.setText(newActivityModel.getTitle());
            }

        } else {
            newPlayLayout.setVisibility(View.GONE);
        }
    }

    private void updateFleaView() {
        fleaActivityModels = mSmallPartnerModel.gettActivityList();
        if (fleaActivityModels != null && fleaActivityModels.size() > 0) {
            FleaActivityModel fleaActivityModel = fleaActivityModels.get(0);
            fleaLayout.setVisibility(View.VISIBLE);
            AndTools.displayImage(null, fleaActivityModel.getUrl(), fleaImage);
        } else {
            fleaLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.avatar_layout1:
                onAvatar1Click();
                break;
            case R.id.avatar_layout2:
                onAvatar2Click();
                break;
            case R.id.avatar_layout3:
                onAvatar3Click();
                break;
            case R.id.avatar_layout4:
                onAvatar4Click();
                break;
            case R.id.avatar_layout5:
                onAvatar5Click();
                break;
            case R.id.avatar_layout6:
                onAvatar6Click();
                break;
            case R.id.empty_text:
                mSwipeRefreshLayout.setRefreshing(true);
                fetchData();
                break;

        }
    }

    private void onAvatar1Click() {
        if (superMomModels != null && superMomModels.size() > 0) {
            SuperMomModel superMomModel = superMomModels.get(0);
            intent2UserInfo(superMomModel);
        }
    }

    private void onAvatar2Click() {
        if (superMomModels != null && superMomModels.size() > 1) {
            SuperMomModel superMomModel = superMomModels.get(1);
            intent2UserInfo(superMomModel);
        }
    }

    private void onAvatar3Click() {
        if (superMomModels != null && superMomModels.size() > 2) {
            SuperMomModel superMomModel = superMomModels.get(0);
            intent2UserInfo(superMomModel);
        }
    }

    private void onAvatar4Click() {
        if (superMomModels != null && superMomModels.size() > 3) {
            SuperMomModel superMomModel = superMomModels.get(3);
            intent2UserInfo(superMomModel);
        }
    }

    private void onAvatar5Click() {
        if (superMomModels != null && superMomModels.size() > 4) {
            SuperMomModel superMomModel = superMomModels.get(4);
            intent2UserInfo(superMomModel);
        }
    }

    private void intent2UserInfo(SuperMomModel superMomModel) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.KEY_INTENT_ID, superMomModel.getId());
        intent.putExtra(UserInfoActivity.KEY_INTENT_NAME, superMomModel.getName());
        getActivity().startActivity(intent);
    }

    private void onAvatar6Click() {
        // TODO:
        SuperMomActivity.launch(getActivity());
        if (superMomModels != null && superMomModels.size() > 5) {

            SuperMomActivity.launch(getActivity());
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return theCommunityActivityModels.size();
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
                convertView = View.inflate(getActivity(), R.layout.small_partner_list_item_layout, null);
            }

            final TheCommunityActivityModel theCommunityActivityModel = theCommunityActivityModels.get(position);

            RelativeLayout imageLayout = (RelativeLayout) convertView.findViewById(R.id.image_layout);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageLayout.getLayoutParams();
            layoutParams.height = AndTools.getScreenWidth(getActivity()) * 9 / 16;
            imageLayout.setLayoutParams(layoutParams);

            ImageView imageView = CommonViewHolder.get(convertView, R.id.image);
            AndTools.displayImage(null, theCommunityActivityModel.getUrl(), imageView);

            TextView signUpStatus = CommonViewHolder.get(convertView, R.id.sign_up_status);

            TextView description = CommonViewHolder.get(convertView, R.id.description);
            description.setText(theCommunityActivityModel.getTitle());

            TextView price = CommonViewHolder.get(convertView, R.id.price);
            price.setText(getString(R.string.price, theCommunityActivityModel.getMoney()));

            TextView topic = CommonViewHolder.get(convertView, R.id.topic);
            topic.setText(theCommunityActivityModel.getName());

            TextView address = CommonViewHolder.get(convertView, R.id.address);
            address.setText(theCommunityActivityModel.getDetailAddress());

            TextView distance = CommonViewHolder.get(convertView, R.id.distance);
            distance.setText(getString(R.string.distance, theCommunityActivityModel.getDistance()));

            TextView signUp = CommonViewHolder.get(convertView, R.id.sign_up);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp(theCommunityActivityModel.getId());
                }
            });

            TextView time = CommonViewHolder.get(convertView, R.id.time);
            String startTime = DateUtil.convertLongToString(theCommunityActivityModel.getStratTime());
            String endTime = DateUtil.convertLongToString(theCommunityActivityModel.getEndTime());
            time.setText(getString(R.string.small_partner_time, startTime, endTime));

            TextView peopleCount = CommonViewHolder.get(convertView, R.id.people_count);
            peopleCount.setText(getString(R.string.people_count, theCommunityActivityModel.getMaxRoles()));

            TextView signUoPeople = CommonViewHolder.get(convertView, R.id.sign_up_count);
            LinearLayout avatarLayout = CommonViewHolder.get(convertView, R.id.avatar_layout);
            CircleImageView avatar1 = CommonViewHolder.get(convertView, R.id.avatar1);
            CircleImageView avatar2 = CommonViewHolder.get(convertView, R.id.avatar2);
            CircleImageView avatar3 = CommonViewHolder.get(convertView, R.id.avatar3);
            CircleImageView avatar4 = CommonViewHolder.get(convertView, R.id.avatar4);
            CircleImageView avatar5 = CommonViewHolder.get(convertView, R.id.avatar5);
            CircleImageView avatar6 = CommonViewHolder.get(convertView, R.id.avatar6);
            avatar6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            List<RoleModel> roleModels = theCommunityActivityModel.getRoleList();
            if (roleModels != null) {
                int size = roleModels.size();
                signUoPeople.setText(getString(R.string.sign_up_people_count, size));
                if (size >= 1) {
                    avatar1.setVisibility(View.VISIBLE);
                    avatar2.setVisibility(View.GONE);
                    avatar3.setVisibility(View.GONE);
                    avatar4.setVisibility(View.GONE);
                    avatar5.setVisibility(View.GONE);
                    avatar6.setVisibility(View.VISIBLE);

                    AndTools.displayImage(null, roleModels.get(0).getUrl(), avatar1);
                }
                if (size >= 2) {
                    avatar1.setVisibility(View.VISIBLE);
                    avatar2.setVisibility(View.VISIBLE);
                    avatar3.setVisibility(View.GONE);
                    avatar4.setVisibility(View.GONE);
                    avatar5.setVisibility(View.GONE);
                    avatar6.setVisibility(View.VISIBLE);

                    AndTools.displayImage(null, roleModels.get(1).getUrl(), avatar2);
                }

                if (size >= 3) {
                    avatar1.setVisibility(View.VISIBLE);
                    avatar2.setVisibility(View.VISIBLE);
                    avatar3.setVisibility(View.VISIBLE);
                    avatar4.setVisibility(View.GONE);
                    avatar5.setVisibility(View.GONE);
                    avatar6.setVisibility(View.VISIBLE);

                    AndTools.displayImage(null, roleModels.get(2).getUrl(), avatar3);
                }

                if (size >= 4) {
                    avatar1.setVisibility(View.VISIBLE);
                    avatar2.setVisibility(View.VISIBLE);
                    avatar3.setVisibility(View.VISIBLE);
                    avatar4.setVisibility(View.VISIBLE);
                    avatar5.setVisibility(View.GONE);
                    avatar6.setVisibility(View.VISIBLE);

                    AndTools.displayImage(null, roleModels.get(3).getUrl(), avatar4);
                }

                if (size >= 4) {
                    avatar1.setVisibility(View.VISIBLE);
                    avatar2.setVisibility(View.VISIBLE);
                    avatar3.setVisibility(View.VISIBLE);
                    avatar4.setVisibility(View.VISIBLE);
                    avatar5.setVisibility(View.VISIBLE);
                    avatar6.setVisibility(View.VISIBLE);

                    AndTools.displayImage(null, roleModels.get(4).getUrl(), avatar5);
                }

            } else {
                signUoPeople.setText(getString(R.string.sign_up_people_count, 0));

                avatar1.setVisibility(View.GONE);
                avatar2.setVisibility(View.GONE);
                avatar3.setVisibility(View.GONE);
                avatar4.setVisibility(View.GONE);
                avatar5.setVisibility(View.GONE);
                avatar6.setVisibility(View.VISIBLE);

            }


            return convertView;
        }
    }
}
