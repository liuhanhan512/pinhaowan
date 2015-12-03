package com.hwand.pinhaowanr.mine;

import android.os.Bundle;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.CancelBackToMainEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class AboutFragment extends BaseFragment {

    public static AboutFragment newInstance(){
        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitleBarTtile("关于");
        EventBus.getDefault().post(new CancelBackToMainEvent(true));
    }

}
