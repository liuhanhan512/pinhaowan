package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;

/**
 * 发起拼拼页面
 * Created by hanhanliu on 15/12/12.
 */
public class LaunchSpellDActivity extends BaseActivity {

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , LaunchSpellDActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_spell_d_layout);
    }
}
