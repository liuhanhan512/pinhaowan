package com.hwand.pinhaowanr.mine;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.util.HashMap;
import java.util.Map;

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
    private TextView mTip;

    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == VerifyCodeTimer.IN_RUNNING) {// 正在倒计时
                mTip.setText(msg.obj.toString());
            } else if (msg.what == VerifyCodeTimer.END_RUNNING) {// 完成倒计时
                mTip.setText(msg.obj.toString());
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_verify_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("注册");
    }

    private void initView() {
        mVerifyCode = (EditText) mFragmentView.findViewById(R.id.code_input);
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mTip = (TextView) mFragmentView.findViewById(R.id.text_tip);
        mVerifyCode.requestFocus();
        mNext.setOnClickListener(this);
        VerifyCodeTimer mCodeTimer = new VerifyCodeTimer(60000, 1000, mHandler);
        mCodeTimer.start();

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.btn_next:
                    verify();
                default:
                    break;
            }
        }
    }

    private void verify() {
        boolean cancel = false;
        View focusView = null;
        String code = mVerifyCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            mVerifyCode.setError(getString(R.string.error_field_required));
            focusView = mVerifyCode;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("verifyCode", code);
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_PUSH_CODE, params);
            LogUtil.d("dxz", url);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.d("dxz", response);
                    // 结果（result）0 失败 1 成功
                    if (!TextUtils.isEmpty(response) && response.contains("1")) {
                        FinalRegisterFragment finalRegisterFragment = FinalRegisterFragment.newInstance();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.hide(VerifyFragment.this);
                        tx.add(R.id.fragment_content, finalRegisterFragment, "FinalRegisterFragment");
                        tx.commit();

                    } else {
                        new DDAlertDialog.Builder(getActivity())
                                .setTitle("提示").setMessage("验证失败！")
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
                                }
                            }).show();
                }
            });
        }
    }
}
