package com.hwand.pinhaowanr.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.community.SmallPartnerFragment;
import com.hwand.pinhaowanr.community.SpellDFragment;
import com.hwand.pinhaowanr.utils.AndTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class CommunityFragment extends BaseFragment {

    private ViewPager mPager;
    private ImageView mCompile;
    private CommunityPagerAdapter mPageAdapter;
    private int mCurrentIndex = SMALL_PARTNER;

    private static final int SMALL_PARTNER = 0;
    private static final int SPELL_D = 1;

    private final int[] FRAGMENT_TITLES = {R.string.small_partner, R.string.spell_d};

    private SparseArray<BaseFragment> mListFragments;

    private BaseFragment mCurrentFragment;

    private List<RelativeLayout> mTabLayouts = new ArrayList<RelativeLayout>();

    public static CommunityFragment newInstance(){
        CommunityFragment fragment = new CommunityFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        TextView city = (TextView)mFragmentView.findViewById(R.id.city);
        TextView region = (TextView)mFragmentView.findViewById(R.id.region);

        AMapLocation aMapLocation = MainApplication.getInstance().getAmapLocation();
        if(aMapLocation != null){
            city.setText(aMapLocation.getCity());
            region.setText(aMapLocation.getDistrict());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
    }

    private void initViewPager(){
        Log.d("lzc" ,"initViewPager==========>");

        mPager = (ViewPager) mFragmentView.findViewById(R.id.view_pager);
        mPager.setPageMargin(AndTools.dp2px(getActivity(), 1f));
        mPager.setPageMarginDrawable(getResources().getDrawable(R.drawable.default_divider));
        mPager.setOnPageChangeListener(new ViewPagerListener());
        mPager.setOffscreenPageLimit(4);
        if (mPageAdapter == null) {
            mPageAdapter = new CommunityPagerAdapter(getFragmentManager());
        }
        mPager.setAdapter(mPageAdapter);

        mPager.setCurrentItem(mCurrentIndex);

        initTabLayouts();
        changeTabLayoutState();
        setTabLayoutListener();
    }

    private void initTabLayouts() {


        RelativeLayout smallPartner = (RelativeLayout) mFragmentView.findViewById(R.id.small_partner_layout);
        smallPartner.setTag(Integer.valueOf(SMALL_PARTNER));

        RelativeLayout spellD = (RelativeLayout) mFragmentView.findViewById(R.id.spell_d_layout);
        spellD.setTag(Integer.valueOf(SPELL_D));


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
                case SMALL_PARTNER:

                    break;

                case SPELL_D:
                    break;


            }

            mCurrentIndex = position;
            changeTabLayoutState();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class CommunityPagerAdapter extends FragmentPagerAdapter {

        public CommunityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("lzc", "getItem=============>");
            if (mListFragments == null) {
                mListFragments = new SparseArray<BaseFragment>(2);
            }
            switch (position) {
                case SMALL_PARTNER:
                    mCurrentFragment = SmallPartnerFragment.newInstance();
                    break;
                case SPELL_D:
                    mCurrentFragment = SpellDFragment.newInstance();
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
