package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.utils.AndTools;

/**
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityDetailDiscussView extends LinearLayout {

    private TextView mName , mContent;

    private CircleImageView mAvatar;

    public ActivityDetailDiscussView(Context context) {
        super(context);
        initViews(context);
    }

    public ActivityDetailDiscussView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ActivityDetailDiscussView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.activity_detail_discuss_layout, this);
        mAvatar = (CircleImageView)findViewById(R.id.avatar);
        mName = (TextView)findViewById(R.id.name);
        mContent = (TextView)findViewById(R.id.content);
    }

    public void setNameText(String text){
        mName.setText(text);
    }

    public void setContent(String text){
        mContent.setText(text);
    }

    public void displayAvatar(String url){
        AndTools.displayImage(null , url , mAvatar);
    }

}
