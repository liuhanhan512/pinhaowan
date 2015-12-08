package com.hwand.pinhaowanr.mine;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.widget.SlidingAdapter;
import com.hwand.pinhaowanr.widget.calendar.CalendarGridView;
import com.hwand.pinhaowanr.widget.calendar.CalendarUtils;
import com.hwand.pinhaowanr.widget.calendar.CalendarViewPager;
import com.hwand.pinhaowanr.widget.calendar.UniformGridView;

/**
 * Created by dxz on 15/12/01.
 */
public class OrderFragment extends BaseFragment {

    private CalendarViewPager mCalendarPager;

    private int mCurrentDateInt = CalendarUtils.DEFAULT_DATE_INT;
    private int mInitDateInt = CalendarUtils.getToday();

    private boolean mDisableCalendar = false;
    private int mCurrentDayLine;

    private ValueAnimator mCalendarAnimator;
    private ValueAnimator mDetailAnimator;

    private int mCalendarHeight;
    private int mCalendarLineHeight;
    private int mAttendanceDetailMariginTop;

    private SparseArray<Boolean> mCalendarCache = new SparseArray<Boolean>();
    private SparseArray<Integer> mCalendarMonthDataStatus = new SparseArray<Integer>();

    private Drawable mCalendarItemTodayTip;
    private Drawable mCalendarItemCurrentTip;

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isAdded()) {
                return;
            }
            switch (msg.what) {
                default:

            }

        }
    };

    private RecyclerView mRecyclerView;

    private SlidingAdapter mAdapter;

    private static final int[] WEEK_WORDS = new int[]{
            R.string.calendar_sunday,
            R.string.calendar_monday,
            R.string.calendar_tuesday,
            R.string.calendar_wednesday,
            R.string.calendar_thursday,
            R.string.calendar_friday,
            R.string.calendar_saturday
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

//        mRecyclerView.setAdapter(mAdapter);

        UniformGridView calendarTitle = (UniformGridView) mFragmentView.findViewById(R.id.calendar_title);

        calendarTitle.setUniformGridViewAdapter(new UniformGridView.UniformGridViewAdapter() {
            @Override
            public int getWidthCount() {
                return CalendarGridView.COLUMN_COUNT;
            }

            @Override
            public int getHeightCount() {
                return 1;
            }

            @Override
            public View getView(int px, int py, UniformGridView parent, View oldView) {
                View view;
                if (oldView == null) {
                    view = View.inflate(getActivity(), R.layout.activity_attendance_calendar_week, null);
                } else {
                    view = oldView;
                }
                TextView tv = (TextView) view.findViewById(R.id.calendar_week_text);
                tv.setText(WEEK_WORDS[px]);
                return view;
            }
        });

        mCalendarPager = (CalendarViewPager) mFragmentView.findViewById(R.id.calendar_body);
        mCalendarPager.setCurrentItem(CalendarViewPager.monthIntToPageIndex(mInitDateInt), false);
        mCalendarPager.notifyDataChanged(mInitDateInt);

    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

}
