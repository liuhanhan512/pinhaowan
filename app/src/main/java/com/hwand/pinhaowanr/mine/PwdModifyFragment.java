package com.hwand.pinhaowanr.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dxz on 15/12/2.
 */
public class PwdModifyFragment extends BaseFragment implements View.OnClickListener {

    public static PwdModifyFragment newInstance() {
        PwdModifyFragment fragment = new PwdModifyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.

    private EditText mOldPwd;
    private EditText mPwd;
    private EditText mCommitPwd;

    private TextView mNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pwd_modify_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("修改密码");
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void initView() {
        mOldPwd = (EditText) mFragmentView.findViewById(R.id.old_pwd_input);
        mPwd = (EditText) mFragmentView.findViewById(R.id.pwd_input);
        mCommitPwd = (EditText) mFragmentView.findViewById(R.id.commit_pwd_input);
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
        mOldPwd.requestFocus();
    }

    private void modify() {
        final String oldPwd = mOldPwd.getText().toString();
        String pwd = mPwd.getText().toString();
        String cPwd = mCommitPwd.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(oldPwd)) {
            mOldPwd.setError(getString(R.string.error_field_required));
            focusView = mOldPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(pwd)) {
            mPwd.setError(getString(R.string.error_field_required));
            focusView = mPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(pwd)) {
            mPwd.setError(getString(R.string.error_field_required));
            focusView = mPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(cPwd)) {
            mCommitPwd.setError(getString(R.string.error_field_required));
            focusView = mCommitPwd;
            cancel = true;
        } else if (!TextUtils.equals(pwd, cPwd)) {
            mCommitPwd.setError(getString(R.string.error_inconsistent_password));
            focusView = mCommitPwd;
            cancel = true;
        } else if (!StrUtils.isPasswordValid(oldPwd)) {
            mOldPwd.setError(getString(R.string.error_invalid_password));
            focusView = mOldPwd;
            cancel = true;
        } else if (!StrUtils.isPasswordValid(pwd)) {
            mPwd.setError(getString(R.string.error_invalid_password));
            focusView = mPwd;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("oldPassword", URLEncoder.encode(oldPwd));
            params.put("newPassword", URLEncoder.encode(pwd));
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_MODIFY_PWD, params);
            LogUtil.d("dxz", url);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.d("dxz", response);
                    // 结果（result）0 原密码输入的不对 1 新密码不合法 2 成功
                    if (!TextUtils.isEmpty(response) && response.contains("2")) {
                        AndTools.showToast("修改成功！");
                        hideImm();
                        getFragmentManager().popBackStack();
                    } else {
                        String msg = "网络问题请重试！";
                        if (TextUtils.isEmpty(response)) {

                        } else if (response.contains("0")) {
                            msg = "原密码输入的不对！";
                        } else if (response.contains("1")) {
                            msg = " 新密码不合法！";
                        }
                        new DDAlertDialog.Builder(getActivity())
                                .setTitle("提示").setMessage(msg)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.d("dxz", error.toString());
                    new DDAlertDialog.Builder(getActivity())
                            .setTitle("提示").setMessage("网络问题请重试！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }
    }

    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mNext.getApplicationWindowToken(), 0);

        }
    }
}
