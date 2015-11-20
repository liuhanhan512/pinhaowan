package com.hwand.pinhaowanr.main;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class StarMomFragment extends BaseFragment {

    public static StarMomFragment newInstance(){
        StarMomFragment fragment = new StarMomFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star_mom_layout;
    }
}
