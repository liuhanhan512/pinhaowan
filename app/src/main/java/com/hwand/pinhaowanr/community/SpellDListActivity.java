package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;

/**
 * 某个课程的拼课列表
 * Created by hanhanliu on 15/12/2.
 */
public class SpellDListActivity extends BaseActivity {

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , SpellDListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_d_list_layout);
    }
}
