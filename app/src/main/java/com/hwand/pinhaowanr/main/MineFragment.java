package com.hwand.pinhaowanr.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.mine.LoginFragment;
import com.hwand.pinhaowanr.mine.MineNaviFragment;

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

    /**
     * 双击退出
     */
    private static Boolean noExit = false;

    public static boolean noExit() {
        if (noExit) {
            onBackPressed();
        }
        return noExit;
    }

    private static void onBackPressed() {
        if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
        }
    }

    public static void setNoExit(boolean no) {
        noExit = no;
    }

    private Fragment mCurFragment;

    private static FragmentManager mFragmentManager;
    private MineNaviFragment mMineNaviFragment;
    private LoginFragment mLoginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mFragmentManager = getChildFragmentManager();
        FragmentTransaction tx = mFragmentManager.beginTransaction();

        if (!MainApplication.getInstance().isLogin()) {
            mLoginFragment = LoginFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_container, mLoginFragment, "LoginFragment");
            mCurFragment = mLoginFragment;
            tx.commit();
        } else {
            mMineNaviFragment = MineNaviFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_container, mMineNaviFragment, "MineNaviFragment");
            mCurFragment = mMineNaviFragment;
            tx.commit();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
