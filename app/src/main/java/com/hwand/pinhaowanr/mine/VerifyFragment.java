package com.hwand.pinhaowanr.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class VerifyFragment extends BaseFragment implements View.OnClickListener {

    public static VerifyFragment newInstance() {
        VerifyFragment fragment = new VerifyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private EditText mVerifyCode;
    private TextView mNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_verify_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
    }

    private void initView() {
        mVerifyCode = (EditText) mFragmentView.findViewById(R.id.code_input);
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mVerifyCode.requestFocus();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.btn_next:
                    // TODO:
                default:
                    break;
            }
        }
    }
}
