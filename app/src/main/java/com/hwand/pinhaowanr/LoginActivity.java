package com.hwand.pinhaowanr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.main.MainActivity;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.DDProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends BaseActivity implements OnClickListener, OnLayoutChangeListener {

    // UI references.
    private View mRootView;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mBtnLogin;
    private TextView mForgetPwd;
    private TextView mRegister;

    private InputMethodManager mImm;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private static final String TARGET_CLASS_KEY = "target_class_key";
    private String mTargetClassName;

    public static void launch(Context context, String targetClassName) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.putExtra(TARGET_CLASS_KEY, targetClassName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTargetClassName = getIntent().getStringExtra(TARGET_CLASS_KEY);
        screenHeight = AndTools.getScreenHeight(this);
        keyHeight = screenHeight / 4;

        // Set up the login form.
        mRootView = findViewById(R.id.login_activity_view);
        mUserName = (EditText) findViewById(R.id.phone_input);
        mPassword = (EditText) findViewById(R.id.pwd_input);
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
        mForgetPwd = (TextView) findViewById(R.id.btn_forget_pwd);
        mRegister = (TextView) findViewById(R.id.btn_register);
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

        mRootView.addOnLayoutChangeListener(this);
        mBtnLogin.setOnClickListener(this);
        mForgetPwd.setOnClickListener(this);
        mRegister.setOnClickListener(this);

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
            DDProgressDialog.show(this, "登录中", "正在努力加载...", true);
            Map<String, String> params = new HashMap<String, String>();
            params.put("telephone", phone);
            params.put("passward", password);
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_LOGIN, params);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // TODO:
                    LogUtil.d("dxz", response);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new DDAlertDialog.Builder(LoginActivity.this)
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
        return password.length() > 4;
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
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.INTENT_KEY_TAB, MainActivity.MINE);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/4屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            showRegister(false);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            showRegister(true);
        }
    }
}

