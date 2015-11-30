package com.hwand.pinhaowanr.mine;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class RegisterFragment extends BaseFragment {

    public static RegisterFragment newInstance(){
        RegisterFragment fragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private EditText mUserName;
    private TextView mNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
    }

    private void initView() {
        mUserName = (EditText) mFragmentView.findViewById(R.id.phone_input);
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
    }
}
