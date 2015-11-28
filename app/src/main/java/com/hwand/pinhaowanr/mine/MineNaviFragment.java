package com.hwand.pinhaowanr.mine;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class MineNaviFragment extends BaseFragment {

    public static MineNaviFragment newInstance(){
        MineNaviFragment fragment = new MineNaviFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }
}
