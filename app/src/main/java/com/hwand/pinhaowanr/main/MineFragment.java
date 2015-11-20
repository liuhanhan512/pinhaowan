package com.hwand.pinhaowanr.main;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance(){
        MineFragment fragment = new MineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }
}
