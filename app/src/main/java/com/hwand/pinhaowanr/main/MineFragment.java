package com.hwand.pinhaowanr.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.LogoutEvent;
import com.hwand.pinhaowanr.mine.LoginFragment;
import com.hwand.pinhaowanr.mine.MineNaviFragment;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;

import de.greenrobot.event.EventBus;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mFragmentManager = getChildFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() <= 1) {
                    setNoExit(false);
                }
            }
        });
        FragmentTransaction tx = mFragmentManager.beginTransaction();

        if (!MainApplication.getInstance().isLogin()) {
            mLoginFragment = LoginFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_container, mLoginFragment, "LoginFragment");
            tx.addToBackStack(null);
            mCurFragment = mLoginFragment;
            tx.commit();
        } else {
            mMineNaviFragment = MineNaviFragment.newInstance();
            if (mCurFragment != null && mCurFragment.isVisible()) {
                tx.hide(mCurFragment);
            }
            tx.add(R.id.fragment_container, mMineNaviFragment, "MineNaviFragment");
            tx.addToBackStack(null);
            mCurFragment = mMineNaviFragment;
            tx.commit();
        }


    }

    public void onEventMainThread(LogoutEvent event) {
        LogUtil.d("dxz","LogoutEvent");
        if (mFragmentManager != null) {
            mFragmentManager.popBackStack();
            AndTools.showToast("登录信息失效，请重新登录！");
            if (!MainApplication.getInstance().isLogin()) {
                FragmentTransaction tx = mFragmentManager.beginTransaction();
                mLoginFragment = LoginFragment.newInstance();
                if (mCurFragment != null && mCurFragment.isVisible()) {
                    tx.hide(mCurFragment);
                }
                tx.add(R.id.fragment_container, mLoginFragment, "LoginFragment");
                tx.addToBackStack(null);
                mCurFragment = mLoginFragment;
                tx.commit();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
