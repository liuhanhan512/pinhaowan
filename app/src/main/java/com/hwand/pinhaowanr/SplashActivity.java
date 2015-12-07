package com.hwand.pinhaowanr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hwand.pinhaowanr.main.MainActivity;

/**
 * Created by dxz on 15/11/28.
 */
public class SplashActivity extends BaseActivity {

    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!MainApplication.getInstance().isLogin()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigator2LoginActivity();
                    finish();
                }
            }, 800);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void navigator2LoginActivity() {
        LoginActivity.launch(this, getTargetClass());
    }

    protected String getTargetClass() {
        return null;
    }
}
