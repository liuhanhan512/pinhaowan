package com.hwand.pinhaowanr;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/5/12.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public final ArrayList<LifeCycleListener> mListeners = new ArrayList<LifeCycleListener>();

    protected List<Activity> mActivityStatck = new ArrayList<Activity>();

    private boolean mIsWindowAttached = false;

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class BaseHandler extends Handler {
        private final WeakReference<BaseActivity> mActivity;

        public BaseHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity != null) {
                // ...
                activity.handleMessage(msg);
            }
        }
    }


    protected void handleMessage(Message msg) {

    }

    protected final BaseHandler mHandler = new BaseHandler(this);


    protected void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    protected void sendEmptyMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    protected void sendEmptyDelayMessage(int what, long delay) {
        mHandler.sendEmptyMessageDelayed(what, delay);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackClick();
                break;
        }
    }

    public interface LifeCycleListener {
        void onActivityCreated(BaseActivity activity);

        void onActivityDestroyed(BaseActivity activity);

        void onActivityStarted(BaseActivity activity);

        void onActivityStopped(BaseActivity activity);

        void onActivityResumed(BaseActivity activity);

        void onActivityPaused(BaseActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        public void onActivityCreated(BaseActivity activity) {
        }

        public void onActivityDestroyed(BaseActivity activity) {
        }

        public void onActivityStarted(BaseActivity activity) {
        }

        public void onActivityStopped(BaseActivity activity) {
        }

        @Override
        public void onActivityResumed(BaseActivity activity) {

        }

        @Override
        public void onActivityPaused(BaseActivity activity) {

        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        mListeners.remove(listener);
    }

    private RelativeLayout mActionBarLayout;
    protected ActionBar mActionBar;
    private RelativeLayout mBack;
    private TextView mTitle;
    private RelativeLayout mRightLayout;
    private ImageView mRightImage;
    private TextView mRightText;
    private TextView mLeftText;
    private ImageView mDeleteImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
        mActivityStatck.add(this);

        MainApplication.getInstance().addActivity(this);

        initActionBar();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        // create our manager instance after the content view is set
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        // enable status bar tint
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);
//        // set a custom tint color for all system bars
//        tintManager.setTintColor(R.color.yellow);
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {

            // 返回箭头（默认不显示）
            mActionBar.setDisplayHomeAsUpEnabled(false);
            // 左侧图标点击事件使能
            mActionBar.setHomeButtonEnabled(true);
            // 使左上角图标(系统)是否显示
            mActionBar.setDisplayShowHomeEnabled(false);
            // 显示标题
            mActionBar.setDisplayShowTitleEnabled(false);
            //显示自定义视图
            mActionBar.setDisplayShowCustomEnabled(true);
            View actionbarLayout = LayoutInflater.from(this).inflate(
                    R.layout.actionbar_layout, null);
            mBack = (RelativeLayout) actionbarLayout.findViewById(R.id.back);
            mBack.setOnClickListener(this);
            mRightLayout = (RelativeLayout) actionbarLayout.findViewById(R.id.right_layout);
            mTitle = (TextView) actionbarLayout.findViewById(R.id.tv_title);
            mRightImage = (ImageView) actionbarLayout.findViewById(R.id.right_image);
            mRightText = (TextView) actionbarLayout.findViewById(R.id.right_text);
            mActionBar.setCustomView(actionbarLayout);
            mDeleteImg = (ImageView) actionbarLayout.findViewById(R.id.delete_img);
            mLeftText = (TextView) actionbarLayout.findViewById(R.id.left_text);
            mActionBarLayout = (RelativeLayout) actionbarLayout.findViewById(R.id.title_layout);

        }
    }

    protected void setLeftText(String text) {
        mLeftText.setText(text);
        mLeftText.setVisibility(View.VISIBLE);
    }

    protected void setLeftTextOnclickListener(View.OnClickListener onclickListener) {
        mLeftText.setOnClickListener(onclickListener);

    }

    protected void setmDeleteImg(int resId) {
        mDeleteImg.setVisibility(View.VISIBLE);
        mDeleteImg.setImageResource(resId);
    }

    protected void setDeleImgOnclickListener(View.OnClickListener onclickListener) {
        mDeleteImg.setOnClickListener(onclickListener);
    }

    protected void setRightImageResId(int resId) {
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(resId);
    }

    protected void setRightImageOnclickListener(View.OnClickListener onclickListener) {
        mRightImage.setOnClickListener(onclickListener);
    }

    protected void setRightLayoutOnClickListener(View.OnClickListener onClickListener) {
        mRightLayout.setOnClickListener(onClickListener);
    }

    protected void setRightText(String text) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(text);
    }

    protected void setRightTextDrawable(int img) {
        Drawable db;
        db = getResources().getDrawable(img);
        db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
        mRightText.setCompoundDrawables(null, null, db, null);
        mRightText.setCompoundDrawablePadding(5);

    }

    protected void setRightTextOnclickListener(View.OnClickListener onclickListener) {
        mRightText.setOnClickListener(onclickListener);
    }

    protected void setmRightText(int resId) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(resId);
    }

    protected void setmRightTextEnable(boolean enable) {
        mRightText.setEnabled(enable);
        mRightText.setClickable(enable);
        if (enable) {
            mRightText.setTextColor(getResources().getColor(R.color.white));
        } else {
            mRightText.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    protected void setActionBarTtile(String title) {
        mTitle.setText(title);
    }

    protected void onBackClick() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsWindowAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsWindowAttached = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityStatck.remove(this);
        MainApplication.getInstance().removeActivity(this);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }

    }

    public boolean isAttachedToWindow() {
        return mIsWindowAttached;
    }

    public void clearAllActivity() {
        for (Activity activity : mActivityStatck) {
            activity.finish();
        }
        mActivityStatck.clear();
    }

    //解决viewpage下无响应
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }


    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
}
