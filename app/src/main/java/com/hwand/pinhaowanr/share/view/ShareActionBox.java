package com.hwand.pinhaowanr.share.view;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.laiwang.tide.share.business.BaseShareUnit;
import com.alibaba.laiwang.tide.share.business.ShareInfo;
import com.hwand.pinhaowanr.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ShareActionBox extends Dialog {

	private Context mContext;
	private ViewPager mPager;
	private ShareViewpagerAdapter mShareViewpagerAdapter;
	private List<GridView> mGridViewLists;
	private View mRootView;

	private List<BaseShareUnit> mItems = new ArrayList<BaseShareUnit>();
	private onShareDismissListener onDismissListener=null;


    private int page_size ; //每一页显示的个数
	private int lineCount=2;	//行数 默认2行
    private int rowCount=4;   //列数 默认4列
	private int mCurrIndex = 0;
	private String mDialogTitle;

    private ShareInfo mShareInfo;

    private List<PackageInfo> mPackageInfoList;

	private ShareViewpagerIndicator mCirclePageIndicator;
	private int mPageCount;
    private ShareActionBox(Context context, int theme) {
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
	public ShareActionBox(Context context, List<BaseShareUnit> arrayViewItme) {
		this(context, R.style.share_box_float);
        init(context);
		mItems = arrayViewItme;
		mContext = context;
		page_size = lineCount * rowCount;
        initShareChannelList();
	}
    public ShareActionBox setShareInfo(ShareInfo shareInfo){
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

	private ShareActionBox(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_shares);
		/**
		 * @modified renxia 2014-6-25 
		 * 
		 * @keludeID 5105318
		 * @场景描述 点击分享组件，其他空白地方没有取消功能，其他dialog都可以，交互也要求改掉
		 * @解决说明 调用setCanceledOnTouchOutside实现，android:windowIsFloating 设为true，把宽度拉掉边上
		 */
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LayoutParams a = getWindow().getAttributes();
	    a.gravity = Gravity.BOTTOM;
	    setCanceledOnTouchOutside(true);
		findViews();
		initialize();
	}


	private void findViews() {
		mRootView = findViewById(R.id.root);
		mPager = (ViewPager) findViewById(R.id.pager);
		mCirclePageIndicator = (ShareViewpagerIndicator) findViewById(R.id.indicator);
		
		TextView title = (TextView)findViewById(R.id.title);
		if(title != null) {
			title.setText(mDialogTitle);
		}
	}

	private void initIndicator() {

		if (mItems.size() <= rowCount || page_size <= rowCount) {
			RelativeLayout.LayoutParams scalpageLP = (RelativeLayout.LayoutParams) mPager
					.getLayoutParams();
			scalpageLP.height = (int) mContext.getResources().getDimension(
					R.dimen.default_viewpager_singlehight);
			this.mPager.setLayoutParams(scalpageLP);
		}

		if (mPageCount == 1) {
			mCirclePageIndicator.setVisibility(View.INVISIBLE);
			if (mItems.size() <= rowCount || page_size <= rowCount) {
				RelativeLayout.LayoutParams scalpageLP = (RelativeLayout.LayoutParams) mPager
						.getLayoutParams();
				scalpageLP.height = (int) mContext.getResources().getDimension(
						R.dimen.default_viewpager_singlehight);
				this.mPager.setLayoutParams(scalpageLP);
			}
		} else {
			mCirclePageIndicator.setViewPager(mPager);
			mCirclePageIndicator.setVisibility(View.VISIBLE);
		}
	}

	private void initialize() {
		initPageCount();
		initGrid();
		setPager();
		setWindowOnTouch();
		initIndicator();
		setCancelButton();
	}

	private void initGrid() {
		mGridViewLists = new ArrayList<GridView>();

		for (int i = 0; i < mPageCount; i++) {
			GridView gv = new GridView(mContext);

			gv.setAdapter(new ShareGridviewAdapter(mContext, mItems, i,
					page_size));
			gv.setBackgroundResource(R.drawable.btn_gray_bg);
			gv.setGravity(Gravity.CENTER);
			gv.setClickable(true);
			gv.setEnabled(true);
			gv.setFocusable(false);
			gv.setNumColumns(rowCount);
			gv.setPadding(0, 0, 0, 0);
			if (page_size > rowCount) {
				gv.setVerticalSpacing((int) mContext
						.getResources()
						.getDimension(R.dimen.default_gridview_vertical_spacing));
			} else {
				gv.setVerticalSpacing((int) mContext.getResources()
						.getDimension(R.dimen.default_viewpager_singlehight));
			}
			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
                    BaseShareUnit viewItem = mItems.get(mCurrIndex * page_size + position);
                    viewItem.share(mShareInfo);
					dismiss();
				}

			});
			mGridViewLists.add(gv);
		}
	}
	private void initPageCount() {
		mPageCount = (int) Math.ceil(mItems.size() / page_size * 1.0f);
		float remainder = mItems.size() % page_size * 1.0f;

		if (remainder > 0) {
			mPageCount++;
		}
		if (mPageCount == 0)
			mPageCount = 1;
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

	
	private void setPager() {

		mShareViewpagerAdapter = new ShareViewpagerAdapter(mContext,
				mGridViewLists);
		mPager.setAdapter(mShareViewpagerAdapter);
		mPager.setClickable(true);
		mPager.setEnabled(true);
		mPager.setFocusable(true);
		mPager.setCurrentItem(0);

		RelativeLayout.LayoutParams defaultLP = (RelativeLayout.LayoutParams) mPager
				.getLayoutParams();
		defaultLP.height = (int) mContext.getResources().getDimension(
				R.dimen.default_viewpager_hight);
		this.mPager.setLayoutParams(defaultLP);
		mCirclePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrIndex = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
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