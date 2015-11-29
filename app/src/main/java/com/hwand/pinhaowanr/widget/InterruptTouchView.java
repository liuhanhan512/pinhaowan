package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by hanhanliu on 15/11/29.
 */
public class InterruptTouchView extends FrameLayout {

    private OnTouchListener mOnInterruptTouchListener;

    public InterruptTouchView(Context context) {
        super(context);
    }

    public InterruptTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterruptTouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnInterruptTouchListener(OnTouchListener listener) {
        mOnInterruptTouchListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnInterruptTouchListener != null) {
            return mOnInterruptTouchListener.onTouch(this, ev);
        } else {
            return false;
        }
    }
}