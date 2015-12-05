package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.ForgetPwdActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.DDProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private View mRootView;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mBtnLogin;
    private TextView mForgetPwd;
    private TextView mRegister;

    private DDProgressDialog mDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {

        // Set up the login form.
        mRootView = mFragmentView.findViewById(R.id.login_activity_view);
        mUserName = (EditText) mFragmentView.findViewById(R.id.phone_input);
        mPassword = (EditText) mFragmentView.findViewById(R.id.pwd_input);
        mBtnLogin = (TextView) mFragmentView.findViewById(R.id.btn_login);
        mForgetPwd = (TextView) mFragmentView.findViewById(R.id.btn_forget_pwd);
        mRegister = (TextView) mFragmentView.findViewById(R.id.btn_register);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mBtnLogin.setOnClickListener(this);
        mForgetPwd.setOnClickListener(this);
        mRegister.setOnClickListener(this);
//        controlKeyboardLayout(mRootView, mRootView);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserName.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String phone = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid phone.
        if (TextUtils.isEmpty(phone)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!StrUtils.isPhone(phone)) {
            mUserName.setError(getString(R.string.error_invalid_phone));
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mDialog = DDProgressDialog.show(getActivity(), "登录中", "正在努力加载...", true);
            Map<String, String> params = new HashMap<String, String>();
            params.put("telephone", phone);
            params.put("passward", password);
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_LOGIN, params);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }

                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();
                        UserInfo  info = gson.fromJson(response,UserInfo.class);
                        switch (info.getResult()){
                            case 0:
                            case 1:
                                new DDAlertDialog.Builder(getActivity())
                                        .setTitle("提示").setMessage("用户名或密码错误")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                break;
                            case 2:
                                AndTools.showToast("登陆成功");
                                DataCacheHelper.getInstance().saveUserInfo(response);
                                MineNaviFragment fragment = MineNaviFragment.newInstance();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction tx = fm.beginTransaction();
                                tx.hide(LoginFragment.this);
                                tx.add(R.id.fragment_container, fragment, "MineNaviFragment");
                                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                tx.commit();
                        }

                    } else {
                        new DDAlertDialog.Builder(getActivity())
                                .setTitle("提示").setMessage("登录失败请重试")
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
                    new DDAlertDialog.Builder(getActivity())
                            .setTitle("提示").setMessage("登录失败请重试")
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

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private void showRegister(boolean show) {
        mRegister.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_forget_pwd:
                startActivity(new Intent(getActivity(), ForgetPwdActivity.class));
                break;
            case R.id.btn_register:
                RegisterFragment fragment = RegisterFragment.newInstance();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                tx.hide(LoginFragment.this);
                tx.add(R.id.fragment_container , fragment, "RegisterFragment");
                tx.addToBackStack(null);
                tx.commit();
                break;
        }
    }

}
