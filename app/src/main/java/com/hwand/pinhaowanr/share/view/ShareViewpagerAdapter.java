package com.hwand.pinhaowanr.share.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import java.util.List;

/**
 * 
 * @author desheng.jd
 *
 */
public class ShareViewpagerAdapter  extends PagerAdapter{

	private List<GridView> mLists;
	
	public ShareViewpagerAdapter(Context context, List<GridView> array) {
		this.mLists=array;
	}
	
	@Override
	public int getCount() {
		return mLists.size();  
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;  
	}

	@Override
    public Object instantiateItem(View arg0, int arg1)
    {  
        ((ViewPager) arg0).addView(mLists.get(arg1));  
        return mLists.get(arg1);  
    }  
  
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2)
    {  
        ((ViewPager) arg0).removeView((View) arg2);
    }  
}
