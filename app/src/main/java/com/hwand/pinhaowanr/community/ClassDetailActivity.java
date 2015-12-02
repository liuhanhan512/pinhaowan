package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;

/**
 * 课程详情
 * Created by hanhanliu on 15/12/2.
 */
public class ClassDetailActivity extends BaseActivity {

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , ClassDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_layout);
    }
}
