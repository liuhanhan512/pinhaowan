package com.hwand.pinhaowanr.community;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/30.
 */
public class SmallPartnerFragment extends BaseFragment {

    public static BaseFragment newInstance(){
        SmallPartnerFragment fragment = new SmallPartnerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_small_partner_layout;
    }
}
