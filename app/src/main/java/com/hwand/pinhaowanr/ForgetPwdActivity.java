package com.hwand.pinhaowanr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hwand.pinhaowanr.utils.StrUtils;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

/**
 * A login screen that offers login via phone/password.
 */
public class ForgetPwdActivity extends BaseActivity {

    // UI references.
    private EditText mUserName;
    private TextView mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

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
                    //            NetworkRequest.get();
                    new DDAlertDialog.Builder(ForgetPwdActivity.this)
                            .setTitle("提示").setMessage("新的密码已发送到您的手机，请注意查收")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ForgetPwdActivity.this.finish();
                        }
                    }).show();
                }
            }
        });

    }
}

