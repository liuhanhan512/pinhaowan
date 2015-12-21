package com.hwand.pinhaowanr.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.utils.AndTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dxz on 15/11/20.
 */
public class MinePlanFragment extends BaseFragment {

    private ViewPager mPager;
    private MinePlanPagerAdapter mPageAdapter;
    private int mCurrentIndex = ORDER;

    private static final int ORDER = 0;
    private static final int CLASS = 1;

    private final int[] FRAGMENT_TITLES = {R.string.title_order, R.string.title_class};

    private SparseArray<BaseFragment> mListFragments;

    private BaseFragment mCurrentFragment;

    private List<RelativeLayout> mTabLayouts = new ArrayList<RelativeLayout>();

    public static MinePlanFragment newInstance() {
        MinePlanFragment fragment = new MinePlanFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitleBarTtile("我的安排");
        initViewPager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initViewPager() {
        mPager = (ViewPager) mFragmentView.findViewById(R.id.view_pager);
        mPager.setPageMargin(AndTools.dp2px(getActivity(), 1f));
        mPager.setPageMarginDrawable(getResources().getDrawable(R.drawable.default_divider));
        mPager.setOnPageChangeListener(new ViewPagerListener());
        mPager.setOffscreenPageLimit(2);
        if (mPageAdapter == null) {
            mPageAdapter = new MinePlanPagerAdapter(getChildFragmentManager());
        }
        mPager.setAdapter(mPageAdapter);

        mPager.setCurrentItem(mCurrentIndex);

        initTabLayouts();
        changeTabLayoutState();
        setTabLayoutListener();
    }

    private void initTabLayouts() {


        RelativeLayout smallPartner = (RelativeLayout) mFragmentView.findViewById(R.id.order_layout);
        smallPartner.setTag(Integer.valueOf(ORDER));

        RelativeLayout spellD = (RelativeLayout) mFragmentView.findViewById(R.id.class_layout);
        spellD.setTag(Integer.valueOf(CLASS));


        mTabLayouts.add(smallPartner);
        mTabLayouts.add(spellD);
    }

    private void changeTabLayoutState() {
        for (int i = 0; i < mTabLayouts.size(); i++) {
            RelativeLayout relativeLayout = mTabLayouts.get(i);

            if (mCurrentIndex == i) {
                relativeLayout.getChildAt(0).setSelected(true);
                relativeLayout.getChildAt(1).setSelected(true);
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.getChildAt(0).setSelected(false);
                relativeLayout.getChildAt(1).setSelected(false);
                relativeLayout.setSelected(false);
            }
        }

    }

    private void setTabLayoutListener() {
        for (final RelativeLayout relativeLayout : mTabLayouts) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (Integer) relativeLayout.getTag();
                    mPager.setCurrentItem(index);
                }
            });
        }
    }


    class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case ORDER:

                    break;

                case CLASS:
                    break;


            }

            mCurrentIndex = position;
            changeTabLayoutState();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MinePlanPagerAdapter extends FragmentPagerAdapter {

        public MinePlanPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mListFragments == null) {
                mListFragments = new SparseArray<BaseFragment>(2);
            }
            switch (position) {
                case ORDER:
                    mCurrentFragment = OrderFragment.newInstance();
                    break;
                case CLASS:
                    mCurrentFragment = ClassFragment.newInstance();
                    break;

            }
            mListFragments.append(position, mCurrentFragment);
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = getString(FRAGMENT_TITLES[position]);
            return title;

        }


    }
}
