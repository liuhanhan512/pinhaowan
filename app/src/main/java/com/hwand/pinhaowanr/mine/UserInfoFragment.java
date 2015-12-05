package com.hwand.pinhaowanr.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dxz on 15/12/01.
 */
public class UserInfoFragment extends BaseFragment {

    public static UserInfoFragment newInstance() {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private static final int MSG_INTENT_HEAD = 20;
    private static final int MSG_INTENT_CHILD_NAME = 21;
    private static final int MSG_INTENT_CHILD_SEX = 22;
    private static final int MSG_INTENT_CHILD_BIRTH = 23;
    private static final int MSG_INTENT_ADD = 24;
    private static final int MSG_INTENT_CONTENT = 25;


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isAdded()) {
                return;
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            switch (msg.what) {
                case MSG_INTENT_HEAD:
                    break;
                case MSG_INTENT_CHILD_NAME:
                    ChildNameFragment fragment = ChildNameFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragment, "ChildNameFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CHILD_SEX:
                    SexModifyFragment fragmentSex = SexModifyFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentSex, "SexModifyFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CHILD_BIRTH:
                    BirthdayModifyFragment fragmentBirth = BirthdayModifyFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentBirth, "BirthdayModifyFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_ADD:
                    AddressFragment fragmentAdd = AddressFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentAdd, "AddressFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                case MSG_INTENT_CONTENT:
                    ContentFragment fragmentCon = ContentFragment.newInstance();
                    tx.hide(UserInfoFragment.this);
                    tx.add(R.id.fragment_container, fragmentCon, "ContentFragment");
                    tx.addToBackStack(null);
                    tx.commit();
                    break;
                default:
                    break;

            }

        }
    };

    private ListView mListView;
    private View mHeader;
    private CircleImageView mHeadImageView;

    private MineAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_info_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("个人信息");
        mListView = (ListView) mFragmentView.findViewById(R.id.nv_list);
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.info_header_layout, null);
        mHeadImageView = (CircleImageView) mHeader.findViewById(R.id.head);

        List<MineAdapter.NaviEntity> list = new ArrayList<MineAdapter.NaviEntity>();
        list.add(new MineAdapter.NaviEntity("宝宝名", MSG_INTENT_CHILD_NAME));
        list.add(new MineAdapter.NaviEntity("宝宝性别", MSG_INTENT_CHILD_SEX));
        list.add(new MineAdapter.NaviEntity("宝宝出生", MSG_INTENT_CHILD_BIRTH));
        list.add(new MineAdapter.NaviEntity("家庭地址", MSG_INTENT_ADD));
        list.add(new MineAdapter.NaviEntity("个人介绍", MSG_INTENT_CONTENT));
        mAdapter = new MineAdapter(getActivity(), mHandler, list);
        mListView.addHeaderView(mHeader);
        mListView.setAdapter(mAdapter);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyHeadPic();
            }
        });
        try {
            String url = UrlConfig.PIC_URL + DataCacheHelper.getInstance().getUserInfo().getUrl();
            LogUtil.d("dxz", url);
            ImageLoader.getInstance().displayImage(url, mHeadImageView);
        } catch (Exception e) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }
    
    private void modifyHeadPic() {

    }
}
