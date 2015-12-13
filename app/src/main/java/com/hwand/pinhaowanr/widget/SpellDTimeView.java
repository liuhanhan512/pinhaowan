package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/12/13.
 */
public class SpellDTimeView extends LinearLayout {

    private TextView mTime;

    public SpellDTimeView(Context context) {
        super(context);
        initViews(context);
    }

    public SpellDTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SpellDTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.spell_d_class_detail_time_layout, this);
        mTime = (TextView)findViewById(R.id.time);
    }

    public void setTimeText(String text){
        mTime.setText(text);
    }

}
