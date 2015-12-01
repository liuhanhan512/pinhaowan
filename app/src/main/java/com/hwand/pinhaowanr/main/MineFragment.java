package com.hwand.pinhaowanr.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.mine.MineNaviFragment;
import com.hwand.pinhaowanr.mine.RegisterFragment;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private BaseFragment mCurFragment;

    private FragmentManager mFragmentManager;
    private MineNaviFragment mMineNaviFragment;
    private RegisterFragment mRegisterFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mFragmentManager = getFragmentManager();
        FragmentTransaction tx = mFragmentManager.beginTransaction();
        if (!MainApplication.getInstance().isLogin()) {
            mRegisterFragment = RegisterFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_content, mRegisterFragment, "RegisterFragment");
            mCurFragment = mRegisterFragment;
            tx.commit();
        } else {
            mMineNaviFragment = MineNaviFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_content, mMineNaviFragment, "MineNaviFragment");
            mCurFragment = mMineNaviFragment;
            tx.commit();
        }

    }

}
