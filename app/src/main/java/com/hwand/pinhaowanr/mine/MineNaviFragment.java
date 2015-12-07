package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dxz on 15/11/20.
 */
public class MineNaviFragment extends BaseFragment {

    public static MineNaviFragment newInstance() {
        MineNaviFragment fragment = new MineNaviFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final int MSG_LOGOUT = 4;

    private static final int MSG_INTENT_PLAN = 10;
    private static final int MSG_INTENT_MSG = 11;
    private static final int MSG_INTENT_PWD = 12;
    private static final int MSG_INTENT_ABOUT = 13;


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isAdded()) {
                return;
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            switch (msg.what) {
                case MSG_INTENT_PLAN:
                    MinePlanFragment fragmentPlan = MinePlanFragment.newInstance();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragmentPlan, "MinePlanFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_MSG:
                    MessageFragment fragmentMsg = MessageFragment.newInstance();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragmentMsg, "MessageFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_PWD:
                    PwdModifyFragment fragmentPwd = PwdModifyFragment.newInstance();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragmentPwd, "PwdModifyFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_ABOUT:
                    AboutFragment fragment = AboutFragment.newInstance();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragment, "AboutFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_LOGOUT:
                    logout();
                    break;
                default:
                    break;

            }

        }
    };

    private ListView mListView;
    private View mHeader;
    private View mFooter;
    private CircleImageView mHeadImageView;
    private TextView mUserName;

    private MineAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_navi_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("我的");
        mListView = (ListView) mFragmentView.findViewById(R.id.nv_list);
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.mine_header_layout, null);
        mHeadImageView = (CircleImageView) mHeader.findViewById(R.id.head);
        mUserName = (TextView) mHeader.findViewById(R.id.user_name);
        mFooter = getActivity().getLayoutInflater().inflate(R.layout.mine_footer_layout, null);

        List<MineAdapter.NaviEntity> list = new ArrayList<MineAdapter.NaviEntity>();
        list.add(new MineAdapter.NaviEntity("我的安排", MSG_INTENT_PLAN));
        list.add(new MineAdapter.NaviEntity("消息", MSG_INTENT_MSG));
        list.add(new MineAdapter.NaviEntity("修改密码", MSG_INTENT_PWD));
        list.add(new MineAdapter.NaviEntity("反馈", -1));
        list.add(new MineAdapter.NaviEntity("给个评价", -1));
        list.add(new MineAdapter.NaviEntity("清除缓存", -1));
        list.add(new MineAdapter.NaviEntity("联系我们", -1));
        list.add(new MineAdapter.NaviEntity("关于", MSG_INTENT_ABOUT));
        mAdapter = new MineAdapter(getActivity(), mHandler, list);
        mListView.addHeaderView(mHeader);
        mListView.addFooterView(mFooter);
        mListView.setAdapter(mAdapter);
        mFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        try {
            mUserName.setText(DataCacheHelper.getInstance().getUserInfo().getName());
            String url = DataCacheHelper.getInstance().getUserInfo().getUrl();
            LogUtil.d("dxz", url);
            ImageLoader.getInstance().displayImage(url, mHeadImageView);
            mHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoFragment fragment = UserInfoFragment.newInstance();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tx = fm.beginTransaction();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragment, "UserInfoFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                }
            });
        } catch (Exception e) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(false);
    }

    private void logout() {
        Map<String, String> params = new HashMap<String, String>();
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_LOGOUT, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    AndTools.saveCurrentData2Cache(MainApplication.getInstance(), DataCacheHelper.KEY_USER_INFO, "");
                    AndTools.saveCurrentData2Cache(MainApplication.getInstance(), NetworkRequest.SESSION_COOKIE, "");
                    LoginFragment fragment = LoginFragment.newInstance();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tx = fm.beginTransaction();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragment, "LoginFragment");
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    tx.addToBackStack("LoginFragment");
                    tx.commit();
                    AndTools.showToast("已退出登录");
                } else {
                    AndTools.saveCurrentData2Cache(MainApplication.getInstance(), DataCacheHelper.KEY_USER_INFO, "");
                    AndTools.saveCurrentData2Cache(MainApplication.getInstance(), NetworkRequest.SESSION_COOKIE, "");
                    LoginFragment fragment = LoginFragment.newInstance();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tx = fm.beginTransaction();
                    tx.hide(MineNaviFragment.this);
                    tx.add(R.id.fragment_container, fragment, "LoginFragment");
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    tx.addToBackStack("LoginFragment");
                    tx.commit();
                    AndTools.showToast("已退出登录");
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
                            }
                        }).show();
            }
        });
    }
}
