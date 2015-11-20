package com.hwand.pinhaowanr;

import android.os.Bundle;
import android.os.Handler;

/**
 * 需要检验用户是否登录的基类界面
 * Created by hanhanliu on 15/5/24.
 */
public class BaseLoginCheckActivity  extends BaseActivity {

    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!MainApplication.getInstance().isLogin()){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    navigator2LoginActivity();
                    finish();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private void navigator2LoginActivity(){
        LoginActivity.launch(this, getTargetClass());
    }

    protected String getTargetClass(){
        return null;
    }
}
