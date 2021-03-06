package com.hwand.pinhaowanr.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class MainActivity extends BaseActivity {

    public static final String INTENT_KEY_TAB = "intent_key_tab";

    private CustomViewPager mPager;
    private MainPagerAdapter mPageAdapter;

//    private static final int FINE = 0;
    private static final int COMMUNITY = 0;
//    private static final int STAR_MOM = 2;
    public static final int MINE = 1;

    private static final int TAB_COUNT = 2;

    private int mCurrentIndex = COMMUNITY;

    private List<RelativeLayout> mTabLayouts = new ArrayList<RelativeLayout>();

    private SparseArray<BaseFragment> mListFragments;

    private BaseFragment mCurrentFragment;

    private final int[] FRAGMENT_TITLES = {/**R.string.fine_text, R.string.community_text,*/ /**R.string.star_mom_text,*/ R.string.mine_text};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        mCurrentIndex = getIntent().getIntExtra(INTENT_KEY_TAB, COMMUNITY);
        initTitle();
        initViews();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initTitle() {

    }



    private void initViews() {

        mPager = (CustomViewPager) findViewById(R.id.view_pager);
        mPager.setPagingEnabled(false);
        mPager.setPageMargin(AndTools.dp2px(this, 1f));
        mPager.setPageMarginDrawable(getResources().getDrawable(R.drawable.default_divider));
        mPager.setOnPageChangeListener(mOnPageChangeListener);
        mPager.setOffscreenPageLimit(2);
        if (mPageAdapter == null) {
            mPageAdapter = new MainPagerAdapter(getSupportFragmentManager());
        }
        mPager.setAdapter(mPageAdapter);
        mPager.setCurrentItem(mCurrentIndex);

        mPager.post(new Runnable() {
            @Override
            public void run() {
                mOnPageChangeListener.onPageSelected(mCurrentIndex);
            }
        });

        initTabLayouts();
        changeTabLayoutState();
        setTabLayoutListener();
    }

    final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
//                case FINE:
//
//                    break;

                case COMMUNITY:
                    break;

//                case STAR_MOM:
//                    break;

                case MINE:
                    break;

            }
            mCurrentIndex = position;
            changeTabLayoutState();
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void initTabLayouts() {
//        RelativeLayout fineTab = (RelativeLayout) findViewById(R.id.fine_layout);
//        fineTab.setTag(Integer.valueOf(FINE));

        RelativeLayout communityTab = (RelativeLayout) findViewById(R.id.community_layout);
        communityTab.setTag(Integer.valueOf(COMMUNITY));

//        RelativeLayout starTab = (RelativeLayout) findViewById(R.id.star_mom_layout);
//        starTab.setTag(Integer.valueOf(STAR_MOM));

        RelativeLayout mineTab = (RelativeLayout) findViewById(R.id.mine_layout);
        mineTab.setTag(Integer.valueOf(MINE));

//        mTabLayouts.add(fineTab);
        mTabLayouts.add(communityTab);
//        mTabLayouts.add(starTab);
        mTabLayouts.add(mineTab);

    }

    private void changeTabLayoutState() {
        for (int i = 0; i < mTabLayouts.size(); i++) {
            RelativeLayout relativeLayout = mTabLayouts.get(i);
            if (mCurrentIndex == i) {
                relativeLayout.findViewById(R.id.tv_text).setSelected(true);
                relativeLayout.findViewById(R.id.image_indicator).setSelected(true);
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.findViewById(R.id.tv_text).setSelected(false);
                relativeLayout.findViewById(R.id.image_indicator).setSelected(false);
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


    class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (mListFragments == null) {
                mListFragments = new SparseArray<BaseFragment>(TAB_COUNT);
            }
            switch (position) {
//                case FINE:
//                    mCurrentFragment = FineFragment.newInstance();
//                    break;
                case COMMUNITY:
                    mCurrentFragment = CommunityFragment.newInstance();
                    break;
//                case STAR_MOM:
//                    mCurrentFragment = StarMomFragment.newInstance();
//                    break;
                case MINE:
                    mCurrentFragment = MineFragment.newInstance();
                    break;
            }
            mListFragments.append(position, mCurrentFragment);
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = getString(FRAGMENT_TITLES[position]);
            return title;

        }

    }


}
