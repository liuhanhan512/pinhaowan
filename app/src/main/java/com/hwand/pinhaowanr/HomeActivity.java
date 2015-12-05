package com.hwand.pinhaowanr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.event.LocationEvent;
import com.hwand.pinhaowanr.location.LocationDataFeedbackListener;
import com.hwand.pinhaowanr.location.LocationManager;
import com.hwand.pinhaowanr.model.ConfigModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 首页界面
 */
public class HomeActivity extends BaseActivity {

    private int mMaxLocation = 3;//最多进行三次定位

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    private int[] unSelectedTabIcons = {
            R.mipmap.fine_normal, R.mipmap.community_normal, /**R.drawable.tab_community*/ R.mipmap.mine_normal
    };
    private static int[] SelectedTabIcons = {
            R.mipmap.fine_selected, R.mipmap.community_selected, /**R.drawable.tab_community_cur*/ R.mipmap.mine_selected
    };

    public static int mSelectedIndex = 0;

    private final int[] FRAGMENT_TITLES = {R.string.fine_text, R.string.community_text, /**R.string.star_mom_text,*/ R.string.mine_text};

    TabHost tabHost = null;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tab1").setIndicator(getTabHost(0)).setContent(R.id.tab1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2").setIndicator(getTabHost(1)).setContent(R.id.tab2);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3").setIndicator(getTabHost(2)).setContent(R.id.tab3);
//        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("tab4").setIndicator(getTabHost(3)).setContent(R.id.tab4);
        tabHost.setBackgroundResource(R.color.white);
        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
//        tabHost.addTab(tabSpec4);
        tabHost.getTabWidget().setDividerDrawable(R.color.white);
        View view = tabHost.getTabWidget().getChildAt(0);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setTextColor(Color.parseColor("#fa6175"));
        imageView.setImageDrawable(getResources().getDrawable(SelectedTabIcons[0]));


        tabHost.setCurrentTab(mSelectedIndex);
        for (int i = 0; i < mSelectedIndex; i++) {
            View curr_view = tabHost.getTabWidget().getChildAt(i);
            ImageView curr_imageView = (ImageView) curr_view.findViewById(R.id.image);
            curr_imageView.setImageDrawable(getResources().getDrawable(unSelectedTabIcons[i]));
            TextView curr_textView = (TextView) curr_view.findViewById(R.id.text);
            curr_textView.setTextColor(Color.parseColor("#333333"));
        }
        int curr = tabHost.getCurrentTab();
        View view1 = tabHost.getTabWidget().getChildAt(curr);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.image);
        imageView1.setImageDrawable(getResources().getDrawable(SelectedTabIcons[curr]));

        TextView textView1 = (TextView) view1.findViewById(R.id.text);
        textView1.setTextColor(Color.parseColor("#fa6175"));

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                updateTab(tabHost);
            }

        });

    }

    private void updateTab(TabHost tabHost) {
        int curr = tabHost.getCurrentTab();
        mSelectedIndex = curr;
        View view = tabHost.getTabWidget().getChildAt(curr);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(SelectedTabIcons[curr]));

        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setTextColor(Color.parseColor("#fa6175"));

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            if (i != curr) {
                View curr_view = tabHost.getTabWidget().getChildAt(i);
                ImageView curr_imageView = (ImageView) curr_view.findViewById(R.id.image);
                curr_imageView.setImageDrawable(getResources().getDrawable(unSelectedTabIcons[i]));
                TextView curr_textView = (TextView) curr_view.findViewById(R.id.text);
                curr_textView.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    private View getTabHost(int i) {
        headerView = View.inflate(this, R.layout.tab_layout, null);
        ImageView img = (ImageView) headerView.findViewById(R.id.image);
        img.setImageResource(unSelectedTabIcons[i]);
        TextView text = (TextView) headerView.findViewById(R.id.text);
        text.setText(getString(FRAGMENT_TITLES[i]));
        return headerView;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            AndTools.showToast(R.string.back_out);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }


}
