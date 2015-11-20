package com.hwand.pinhaowanr.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.hwand.pinhaowanr.utils.AndTools;

/**
 * Created by jake on 3/6/15.
 */
public class DDDialog extends Dialog implements DDDismissRequestContextLifeCycle.OnDismissImmeRequest{

    /** life cycle */
    private DDDismissRequestContextLifeCycle mLifeCycle = null;

    /** the context */
    private Activity mContext = null;

    /**
     * Constructor
     * @param context
     */
    public DDDialog(Context context) {
        super(context);
        mContext = (Activity)context;
    }

    /**
     * Constructor
     * @param context
     * @param theme
     */
    public DDDialog(Context context, int theme) {
        super(context, theme);
        mContext = (Activity)context;
    }

    /**
     * Constructor
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    protected DDDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = (Activity)context;
    }

    /**
     * show
     */
    public void show(){
        if(AndTools.isActivitySafeForDialog(mContext)){
            super.show();

            if(null == mLifeCycle){
                mLifeCycle = new DDDismissRequestContextLifeCycle();
            }

            mLifeCycle.start(mContext, this);
        }
    }

    /**
     * dismiss
     */
    public void dismiss(){

        if(null != mLifeCycle){
            mLifeCycle.remove();
        }

        super.dismiss();
    }

    /**
     * this method is called for case that activity is destroying but the dialog is still showing, so dismiss it directly
     * some times user do animation in the dismiss function, in order to make sure it does not crash, so donot call the dismiss instead of super.dismiss
     */
    @Override
    public final void onDismissRequest() {
        super.dismiss();
    }
}
