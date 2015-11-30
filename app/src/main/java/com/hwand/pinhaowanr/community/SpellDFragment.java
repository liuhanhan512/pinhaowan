package com.hwand.pinhaowanr.community;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;

/**
 * Created by hanhanliu on 15/11/30.
 */
public class SpellDFragment extends BaseFragment {

    public static BaseFragment newInstance(){
        SpellDFragment fragment = new SpellDFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
