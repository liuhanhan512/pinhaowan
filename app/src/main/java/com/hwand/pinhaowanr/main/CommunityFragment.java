package com.hwand.pinhaowanr.main;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class CommunityFragment extends BaseFragment {

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
}
