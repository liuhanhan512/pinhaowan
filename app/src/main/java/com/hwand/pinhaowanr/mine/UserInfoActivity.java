package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends BaseActivity {

    // UI references.
    private EditText mUserName;
    private TextView mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        setActionBarTtile("忘记密码");

        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.phone_input);
        mNext = (TextView) findViewById(R.id.btn_next);

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
                    String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_GET_PWD, params);
                    LogUtil.d("dxz", url);
                    NetworkRequest.get(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    LogUtil.d("dxz", response);
                                    // 0 没有对应的角色 1 手机号不合法 2 发送短信失败 3 成功
                                    if (!TextUtils.isEmpty(response) && response.contains("3")) {
                                        new DDAlertDialog.Builder(UserInfoActivity.this)
                                                .setTitle("提示")
                                                .setMessage("新的密码已发送到您的手机，请注意查收")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                UserInfoActivity.this.finish();
                                                            }
                                                        }
                                                ).show();
                                    } else {
                                        String msg = "网络问题请重试！";
                                        if (TextUtils.isEmpty(response)) {

                                        } else if (response.contains("0")) {
                                            msg = "没有对应的角色！";
                                        } else if (response.contains("1")) {
                                            msg = "手机号不合法！";
                                        } else if (response.contains("2")) {
                                            msg = "发送短信失败！";
                                        }
                                        new DDAlertDialog.Builder(UserInfoActivity.this)
                                                .setTitle("提示").setMessage(msg)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                    }
                                }
                            }, new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    new DDAlertDialog.Builder(UserInfoActivity.this)
                                            .setTitle("提示").setMessage("网络问题请重试！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            }

                    );
                }
            }
        });

    }
}

