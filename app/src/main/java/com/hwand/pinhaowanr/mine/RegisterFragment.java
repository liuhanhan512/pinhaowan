package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.CancelBackToMainEvent;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class RegisterFragment extends BaseFragment {

    public static RegisterFragment newInstance() {
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
        setTitleBarTtile("注册");
        EventBus.getDefault().post(new CancelBackToMainEvent(true));
    }

    private void initView() {
        mUserName = (EditText) mFragmentView.findViewById(R.id.phone_input);
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mUserName.getText().toString();
                boolean cancel = false;
                View focusView = null;
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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("telephone", phone);
                    String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_GET_CODE, params);
                    LogUtil.d("dxz", url);
                    NetworkRequest.get(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.d("dxz", response);
                            // 0 手机号已经注册过了 1 手机号不合法 2 发送短信失败 3 成功
                            if (!TextUtils.isEmpty(response) && response.contains("3")) {
                                VerifyFragment verifyFragment = VerifyFragment.newInstance();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction tx = fm.beginTransaction();
                                tx.hide(RegisterFragment.this);
                                tx.add(R.id.fragment_content, verifyFragment, "VerifyFragment");
                                tx.addToBackStack(null);
                                tx.commit();
                            } else {
                                String msg = "网络问题请重试！";
                                if (TextUtils.isEmpty(response)) {

                                } else if (response.contains("0")) {
                                    msg = "该手机号已注册，请直接登录！";
                                } else if (response.contains("1")) {
                                    msg = "手机号不合法！";
                                } else if (response.contains("2")) {
                                    msg = "发送短信失败！";
                                }
                                new DDAlertDialog.Builder(getActivity())
                                        .setTitle("提示").setMessage(msg)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                getFragmentManager().popBackStack();
                                            }
                                        }).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new DDAlertDialog.Builder(getActivity())
                                    .setTitle("提示").setMessage("网络问题请重试！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            VerifyFragment verifyFragment = VerifyFragment.newInstance();
//                                            FragmentManager fm = getFragmentManager();
//                                            FragmentTransaction tx = fm.beginTransaction();
//                                            tx.hide(RegisterFragment.this);
//                                            tx.add(R.id.fragment_content , verifyFragment, "VerifyFragment");
//                                            tx.addToBackStack(null);
//                                            tx.commit();
                                        }
                                    }).show();
                        }
                    });
                }
            }
        });
    }
}
