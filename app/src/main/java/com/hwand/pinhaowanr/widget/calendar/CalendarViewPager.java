package com.hwand.pinhaowanr.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hwand.pinhaowanr.R;

import java.util.LinkedList;

public class CalendarViewPager extends ViewPager {

    private static final int HALF_PAGE_MAX_LENGTH = 100000;
    private static final int BASE_MONTH_INT = 20140400;

    public interface OnCalendarPageChangeListener {
        void onPageChange(int monthInt);
    }

    private int mHorizontalLayoutMode;
    private int mVerticalLayoutMode;
    private float mHorizontalSpaceProportion;
    private float mVerticalSpaceProportion;
    private float mHorizontalSpace;
    private float mVerticalSpace;
    private float mItemWidth;
    private float mItemHeight;

    private LinkedList<CalendarGridView> mPagerCache = new LinkedList<CalendarGridView>();

    private CalendarGridView.OnCalendarGridViewItemClickListener mOnCalendarGridViewItemClickListener;
    private CalendarGridView.CalendarGridViewAdapter mCalendarGridViewAdapter;
    private OnCalendarPageChangeListener mOnCalendarPageChangeListener;

    private boolean mTouchDisabled = false;

    public CalendarViewPager(Context context) {
        super(context);
        init(context);
    }
    
    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UniformGridView);
        mHorizontalLayoutMode = ta.getInt(R.styleable.UniformGridView_horizontal_layout_mode, UniformGridView.LAYOUT_MODE_SCALE_ITEM);
        mVerticalLayoutMode = ta.getInt(R.styleable.UniformGridView_vertical_layout_mode, UniformGridView.LAYOUT_MODE_SCALE_ITEM);
        mHorizontalSpaceProportion = ta.getFloat(R.styleable.UniformGridView_horizontal_space_proportion, 0);
        mVerticalSpaceProportion = ta.getFloat(R.styleable.UniformGridView_vertical_space_proportion, 0);
        mHorizontalSpace = ta.getDimension(R.styleable.UniformGridView_horizontal_space, 0);
        mVerticalSpace = ta.getDimension(R.styleable.UniformGridView_vertical_space, 0);
        mItemWidth = ta.getDimension(R.styleable.UniformGridView_item_width, 0);
        mItemHeight = ta.getDimension(R.styleable.UniformGridView_item_height, 0);
        ta.recycle();
        init(context);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mTouchDisabled) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTouchDisabled) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public int getHorizontalLayoutMode() {
        return mHorizontalLayoutMode;
    }

    public int getVerticalLayoutMode() {
        return mVerticalLayoutMode;
    }

    public float getHorizontalSpaceProportion() {
        return mHorizontalSpaceProportion;
    }

    public float getVerticalSpaceProportion() {
        return mVerticalSpaceProportion;
    }

    public float getHorizontalSpace() {
        return mHorizontalSpace;
    }

    public float getVerticalSpace() {
        return mVerticalSpace;
    }

    public float getItemWidth() {
        return mItemWidth;
    }

    public float getItemHeight() {
        return mItemHeight;
    }

    public void setTouchDisabled(boolean value) {
        mTouchDisabled = value;
    }

    public boolean isTouchDisabled() {
        return mTouchDisabled;
    }

    private void init(Context context) {
        super.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return HALF_PAGE_MAX_LENGTH * 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                CalendarGridView view;
                if (mPagerCache.size() > 0) {
                    view = mPagerCache.remove();
                    view.setMonthInt(pageIndexToMonthInt(position));
                } else {
                    view = new CalendarGridView(getContext());
                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.setHorizontalLayoutMode(mHorizontalLayoutMode);
                    view.setVerticalLayoutMode(mVerticalLayoutMode);
                    view.setHorizontalSpaceProportion(mHorizontalSpaceProportion);
                    view.setVerticalSpaceProportion(mVerticalSpaceProportion);
                    view.setHorizontalSpace(mHorizontalSpace);
                    view.setVerticalSpace(mVerticalSpace);
                    view.setItemWidth(mItemWidth);
                    view.setItemHeight(mItemHeight);
                    view.setMonthInt(pageIndexToMonthInt(position));
                    view.setOnCalendarGridViewItemClickListener(mInnerOnCalendarGridViewItemClickListener);
                    view.setCalendarGridViewAdapter(mInnerCalendarGridViewAdapter);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object != null && object instanceof CalendarGridView) {
                    CalendarGridView view = (CalendarGridView) object;
                    container.removeView(view);
                    mPagerCache.add(view);
                }
            }
        });
        super.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnCalendarPageChangeListener != null) {
                    mOnCalendarPageChangeListener.onPageChange(pageIndexToMonthInt(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private CalendarGridView.OnCalendarGridViewItemClickListener mInnerOnCalendarGridViewItemClickListener = new CalendarGridView.OnCalendarGridViewItemClickListener() {
        @Override
        public void onItemClick(int dateInt, boolean isCurrentMonth, View v) {
            if (mOnCalendarGridViewItemClickListener != null) {
                mOnCalendarGridViewItemClickListener.onItemClick(dateInt, isCurrentMonth, v);
            }
        }
    };

    private CalendarGridView.CalendarGridViewAdapter mInnerCalendarGridViewAdapter = new CalendarGridView.CalendarGridViewAdapter() {
        @Override
        public View getView(int dateInt, boolean isCurrentMonth, View oldView) {
            if (mCalendarGridViewAdapter != null) {
                return mCalendarGridViewAdapter.getView(dateInt, isCurrentMonth, oldView);
            } else {
                return null;
            }
        }
    };

    public void notifyDataChanged() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v != null && v instanceof CalendarGridView) {
                CalendarGridView cgv = (CalendarGridView) v;
                cgv.notifyDataChanged();
            }
        }
    }

    public void notifyDataChanged(int dateInt) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v != null && v instanceof CalendarGridView) {
                CalendarGridView cgv = (CalendarGridView) v;
                Point p = CalendarGridView.dateToGridPostion(cgv.getMonthInt(), dateInt);
                if (p != null) {
                    cgv.notifyDataChanged(p.x, p.y);
                }
            }
        }
    }

    public void notifyDataChangedMonth(int dateInt) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v != null && v instanceof CalendarGridView) {
                CalendarGridView cgv = (CalendarGridView) v;
                if (CalendarUtils.isSameMonth(dateInt, cgv.getMonthInt())) {
                    cgv.notifyDataChanged();
                }
            }
        }
    }

    public void setOnCalendarPageChangeListener(OnCalendarPageChangeListener listener) {
        mOnCalendarPageChangeListener = listener;
    }

    public void setOnCalendarGridViewItemClickListener(CalendarGridView.OnCalendarGridViewItemClickListener listener) {
        mOnCalendarGridViewItemClickListener = listener;
    }

    public void setCalendarGridViewAdapter(CalendarGridView.CalendarGridViewAdapter adapter) {
        if (mCalendarGridViewAdapter != adapter) {
            mCalendarGridViewAdapter = adapter;
            notifyDataChanged();
        }
    }

    public static int pageIndexToMonthInt(int i) {
        return CalendarUtils.addMonth(BASE_MONTH_INT, i - HALF_PAGE_MAX_LENGTH);
    }

    public static int monthIntToPageIndex(int monthInt) {
        return HALF_PAGE_MAX_LENGTH + CalendarUtils.monthDiff(monthInt, BASE_MONTH_INT);
    }
}