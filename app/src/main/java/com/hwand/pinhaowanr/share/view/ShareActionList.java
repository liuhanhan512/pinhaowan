package com.hwand.pinhaowanr.share.view;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.hwand.pinhaowanr.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zengchan.lzc on 2015/1/27.
 */
public class ShareActionList extends Dialog {

    private Context mContext;
    private ShareViewpagerAdapter mShareViewpagerAdapter;
    private ListView mListView;
    private View mRootView;

    private List<BaseShareUnit> mItems = new ArrayList<BaseShareUnit>();
    private onShareDismissListener onDismissListener=null;


    private int mCurrIndex = 0;
    private String mDialogTitle;

    private ShareInfo mShareInfo;

    private List<PackageInfo> mPackageInfoList;

    private ShareActionList(Context context, int theme) {
        super(context, theme);
        init(context);
        initShareChannelList();
    }
    /**
     * 分享控件的构造器 默认是2行4列
     *
     * @param context
     * @param arrayViewItme
     */
    public ShareActionList(Context context, List<BaseShareUnit> arrayViewItme) {
        this(context, R.style.Custom_Dialog);
        init(context);
        mItems = arrayViewItme;
        mContext = context;
        initShareChannelList();
    }
    public ShareActionList setShareInfo(ShareInfo shareInfo){
        this.mShareInfo = shareInfo;
        return this;
    }
    private void init(Context context){
        PackageManager pm = context.getPackageManager();
        mPackageInfoList = pm.getInstalledPackages(0);
    }
    public void setDialogAttributes(Object... dialogAttrs) {
        if(dialogAttrs == null) {
            return;
        }
        mDialogTitle = dialogAttrs.length > 0 ? (String) dialogAttrs[0] : null;
    }
    public void setShareList(List<BaseShareUnit> items){
        this.mItems = items;
        initShareChannelList();
    }

    public static String shareChannelKey="share_channel";
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

    private ShareActionList(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_share_list);
        /**
         * @modified renxia 2014-6-25
         *
         * @keludeID 5105318
         * @场景描述 点击分享组件，其他空白地方没有取消功能，其他dialog都可以，交互也要求改掉
         * @解决说明 调用setCanceledOnTouchOutside实现，android:windowIsFloating 设为true，把宽度拉掉边上
         */
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.gravity = Gravity.RIGHT;
//        WindowManager.LayoutParams a = this.getWindow().getAttributes();
//        a.dimAmount = 0; //去背景遮盖
//        this.getWindow().setAttributes(a);

        this.setCancelable(true);
        setCanceledOnTouchOutside(true);
        findViews();
        initialize();
    }


    private void findViews() {
        mRootView = findViewById(R.id.root);
        mListView = (ListView)mRootView.findViewById(R.id.listview);

        TextView title = (TextView)findViewById(R.id.title);
        if(title != null) {
            title.setText(mDialogTitle);
        }
    }


    private void initialize() {
        initListView();
        setWindowOnTouch();
//        setCancelButton();
    }
    private void initListView(){
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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mListView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mListView.setLayoutParams(params);
    }

    private void setWindowOnTouch() {
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mRootView.getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void setCancelButton() {
        mRootView.findViewById(R.id.btn_share_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }



    @Override
    public void dismiss() {
        super.dismiss();
        if (onDismissListener!=null) {
            onDismissListener.onDismiss();
        }
    }

    @Override
    public void show() {
        if (mItems!=null && mItems.size()>0) {
            super.show();
        }
    }

    public void setOnDismissListeren(onShareDismissListener ondDismissListener){
        this.onDismissListener=ondDismissListener;
    }

    /**
     * fen'xia
     * @author bingbing
     *
     */
    public interface onShareDismissListener{
        void onDismiss();
    }

}
