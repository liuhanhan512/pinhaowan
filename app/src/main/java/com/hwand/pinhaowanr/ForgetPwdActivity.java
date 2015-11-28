package com.hwand.pinhaowanr;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hwand.pinhaowanr.widget.DDProgressDialog;

/**
 * A login screen that offers login via phone/password.
 */
public class ForgetPwdActivity extends BaseActivity {

    // UI references.
    private EditText mUserName;
    private EditText mPassword;
    private TextView mBtnLogin;
    private TextView mForgetPwd;
    private TextView mRegister;

    private InputMethodManager mImm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.phone_input);
        mPassword = (EditText) findViewById(R.id.pwd_input);
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
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
        mForgetPwd = (TextView) findViewById(R.id.btn_forget_pwd);
        mRegister = (TextView) findViewById(R.id.btn_register);

        mBtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
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
            DDProgressDialog.show(this, "登录中", "正在努力加载...");
//            NetworkRequest.get();
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void showRegister(boolean show) {
        //TODO: Replace this with your own logic
        mRegister.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}

