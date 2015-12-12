package com.hwand.pinhaowanr.share.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.hwand.pinhaowanr.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zengchan.lzc on 2015/1/27.
 */
public class ShareActionPopList extends PopupWindow {
    private ListView mListView;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BaseShareUnit> mItems = new ArrayList<BaseShareUnit>();
    private List<PackageInfo> mPackageInfoList;
    private ShareInfo mShareInfo;

    private final static int MAX_LIST_SIZE = 6;

    public ShareActionPopList(Context context, List<BaseShareUnit> arrayViewItme) {
        super(context);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        initPackInfoList();
        mItems = arrayViewItme;
        initShareChannelList();
        initListView();
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(mListView);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_gray_bg));
        setWidth(dip2px(170));
        // 最大6行
        setHeight((mItems.size() > MAX_LIST_SIZE ? MAX_LIST_SIZE : mItems.size()) * dip2px(48)
                + (mItems.size() > 1 ? ((mItems.size() > MAX_LIST_SIZE ? MAX_LIST_SIZE : mItems.size()) - 1) * dip2px(1): 0));
    }
    public ShareActionPopList setShareInfo(ShareInfo shareInfo){
        this.mShareInfo = shareInfo;
        return this;
    }
    private void initPackInfoList(){
        PackageManager pm = mContext.getPackageManager();
        mPackageInfoList = pm.getInstalledPackages(0);
    }
    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * 初始化分享渠道,下次点击分享的时候生效
     */
    private void initShareChannelList() {
        Iterator iterator = mItems.iterator();
        while(iterator.hasNext()){
            BaseShareUnit unit = (BaseShareUnit) iterator.next();
            if(!checkShowItem(unit)){
                iterator.remove();
            }
        }
    }
    private void initListView(){
        mListView = (ListView) mLayoutInflater.inflate(R.layout.popup_listview, null);
        ShareListviewAdapter shareListviewAdapter = new ShareListviewAdapter(mContext,mItems);
        mListView.setAdapter(shareListviewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BaseShareUnit viewItem = mItems.get(position);
                viewItem.share(mShareInfo);
                dismiss();
            }

        });
        mListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ShareActionPopList.this.dismiss();
                }
                return true;
            }
        });
    }
    /**
     * 检测是否安装当前应用
     *
     * @return
     */
    private boolean checkShowItem(BaseShareUnit unit) {

        // 跳过检测,也就是说,默认的不检测,直接返回
        if (!unit.getmShareUnitInfo().isDefautCheck()) {
            return true;
        }
        // 检测当前手机是否安装此应用
        for (PackageInfo info : mPackageInfoList) {
            if (info.packageName.equals(unit.getmShareUnitInfo().getPakName())) {
                return true;
            }
        }
        return false;
    }

}
