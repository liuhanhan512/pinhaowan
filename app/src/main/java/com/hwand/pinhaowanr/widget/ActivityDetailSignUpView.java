package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;

/**
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityDetailSignUpView extends LinearLayout {

    private TextView mTime , mTickets;

    public ActivityDetailSignUpView(Context context) {
        super(context);
        initViews(context);
    }

    public ActivityDetailSignUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ActivityDetailSignUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.activity_detail_sign_up_layout, this);
        mTime = (TextView)findViewById(R.id.time);
        mTickets = (TextView)findViewById(R.id.tickets);
        TextView signUp = (TextView)findViewById(R.id.sign_up);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSignUpClickListener != null){
                    mOnSignUpClickListener.onSignUpClick();
                }
            }
        });
    }

    private OnSignUpClickListener mOnSignUpClickListener;

    public void setOnSignUpClickListener(OnSignUpClickListener onSignUpClickListener){
        mOnSignUpClickListener = onSignUpClickListener;
    }

    public void setTimeText(String text){
        mTime.setText(text);
    }

    public void setTicketsText(String text){
        mTickets.setText(text);
    }

    public interface OnSignUpClickListener{
        public void onSignUpClick();
    }

}
