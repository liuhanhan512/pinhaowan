package com.hwand.pinhaowanr.fine;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.widget.InterruptTouchView;
import com.hwand.pinhaowanr.widget.calendar.CalendarGridView;
import com.hwand.pinhaowanr.widget.calendar.CalendarUtils;
import com.hwand.pinhaowanr.widget.calendar.CalendarViewPager;
import com.hwand.pinhaowanr.widget.calendar.UniformGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约界面
 * Created by hanhanliu on 15/11/30.
 */
public class ReservationActivity extends BaseActivity {

    private CalendarViewPager mCalendarPager;

    private int mCurrentDateInt = CalendarUtils.DEFAULT_DATE_INT;
    private int mInitDateInt = CalendarUtils.getToday();

    private boolean mDisableCalendar = false;

    private static final int DATA_STATUS_COMPLETE = 1;

    private int mTheDataPostToOnCurrentPageChange = 0;

    private int mCurrentDayLine;

    private boolean mIsNotFirstRequest = false;

    private View mAttendanceDetail;


    private List<String> datas = new ArrayList<String>();

    private ExpandableListView mListView;

    private ValueAnimator mCalendarAnimator;
    private ValueAnimator mDetailAnimator;

    private int mCalendarHeight;
    private int mCalendarLineHeight;
    private int mAttendanceDetailMariginTop;

    private ViewTreeObserver.OnGlobalLayoutListener mFirstOnGlobalLayoutListener;


//    private TextView mCalendarYearMonthDay;

    private SparseArray<Boolean> mCalendarCache = new SparseArray<Boolean>();
    private SparseArray<Integer> mCalendarMonthDataStatus = new SparseArray<Integer>();

    private Drawable mCalendarItemTodayTip;
    private Drawable mCalendarItemCurrentTip;

    private static final int[] WEEK_WORDS = new int[] {
            R.string.calendar_sunday,
            R.string.calendar_monday,
            R.string.calendar_tuesday,
            R.string.calendar_wednesday,
            R.string.calendar_thursday,
            R.string.calendar_friday,
            R.string.calendar_saturday
    };

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , ReservationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_layout);

        initTitle();
        initViews();
    }

    private void initTitle(){
        setTitle(getString(R.string.reservation));
    }

    private void initViews(){
        mListView = (ExpandableListView)findViewById(R.id.listview);
        mListView.setAdapter(new Adapter());

        UniformGridView calendarTitle = (UniformGridView) findViewById(R.id.calendar_title);

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
                    view = View.inflate(ReservationActivity.this, R.layout.activity_attendance_calendar_week, null);
                } else {
                    view = oldView;
                }
                TextView tv = (TextView) view.findViewById(R.id.calendar_week_text);
                tv.setText(WEEK_WORDS[px]);
                return view;
            }
        });


        mAttendanceDetail = findViewById(R.id.attendance_detail);

//        mCalendarYearMonthDay = (TextView)findViewById(R.id.text);

        mCalendarPager = (CalendarViewPager) findViewById(R.id.calendar_body);
        mCalendarPager.setCurrentItem(CalendarViewPager.monthIntToPageIndex(mInitDateInt), false);
        mCalendarPager.notifyDataChanged(mInitDateInt);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIsNotFirstRequest = true;
//                int position = mCalendarPager.getCurrentItem();
//                mCalendarPager.setCurrentItem(position + 1 ,false);
            }
        }, 2000);


        OnInterruptTouchListener touchHandler = new OnInterruptTouchListener(this);
        InterruptTouchView touchView = (InterruptTouchView) findViewById(R.id.calendar_touch_container);
        touchView.setOnInterruptTouchListener(touchHandler);
        touchView.setOnTouchListener(touchHandler);

        mCalendarPager.setCalendarGridViewAdapter(new CalendarGridView.CalendarGridViewAdapter() {
            @Override
            public View getView(int dateInt, boolean isCurrentMonth, View oldView) {
                View view;
                CalendarItemHolder holder;
                if (oldView == null) {
                    view = View.inflate(ReservationActivity.this, R.layout.activity_attendance_calendar_day, null);
                    holder = new CalendarItemHolder(view);
                    view.setTag(holder);
                } else {
                    view = oldView;
                    holder = (CalendarItemHolder) view.getTag();
                }
                holder.setText(String.valueOf(CalendarUtils.getDisplayDay(dateInt)));
                if (dateInt == mCurrentDateInt) {
                    holder.setCurrentTip(true);
                } else {
                    holder.setCurrentTip(false);
                }
                if (dateInt == CalendarUtils.getToday()) {
                    holder.setTodayTip(true);
                } else {
                    holder.setTodayTip(false);
                }
                holder.setIsCurrentMonth(isCurrentMonth);

                int monthInt = CalendarUtils.getPureMonthInt(dateInt);
                Integer monthDataStatus = mCalendarMonthDataStatus.get(monthInt);
                if (monthDataStatus != null && monthDataStatus == DATA_STATUS_COMPLETE) {
                    Boolean calendarCacheData = mCalendarCache.get(dateInt);
                    if (calendarCacheData != null && calendarCacheData) {
                        holder.setLeaveTip(true);
                    } else {
                        holder.setLeaveTip(false);
                    }
                } else {
                    holder.setLeaveTip(false);
                }
                return view;
            }
        });
        mCalendarPager.setOnCalendarGridViewItemClickListener(new CalendarGridView.OnCalendarGridViewItemClickListener() {
            @Override
            public void onItemClick(int dateInt, boolean isCurrentMonth, View v) {
                if (!mDisableCalendar) {
                    navToDate(dateInt, true);
                }
            }
        });
        mCalendarPager.setOnCalendarPageChangeListener(new CalendarViewPager.OnCalendarPageChangeListener() {
            @Override
            public void onPageChange(int monthInt) {
                // 切换月份的时候，默认展示1号
                if (mIsNotFirstRequest) {
                    mCalendarPager.setTranslationY(0);
                }
                onCurrentPageChange(monthInt);
            }
        });

        mCalendarHeight = mCalendarPager.getLayoutParams().height;
        mCalendarLineHeight = mCalendarHeight / CalendarGridView.ROW_COUNT;
        mAttendanceDetailMariginTop = ((ViewGroup.MarginLayoutParams) mAttendanceDetail.getLayoutParams()).topMargin;

        mFirstOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mFirstOnGlobalLayoutListener != null) {
                    getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(mFirstOnGlobalLayoutListener);
                    mFirstOnGlobalLayoutListener = null;
                }
            }
        };
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mFirstOnGlobalLayoutListener);

        // 默认日历收起
        Point p = CalendarGridView.dateToGridPostion(mInitDateInt, mInitDateInt);
        if (p != null) {
            mCurrentDayLine = p.y;
        } else {
            mCurrentDayLine = 0;
        }
        mCalendarPager.setTranslationY(- mCalendarLineHeight * mCurrentDayLine);
    }

    private void cancelAllAnimator() {
        if (mCalendarAnimator != null) {
            mCalendarAnimator.cancel();
        }
        if (mDetailAnimator != null) {
            mDetailAnimator.cancel();
        }
    }

    private void touchScroll(float diffY) {
        cancelAllAnimator();
        float t = mAttendanceDetail.getTranslationY() + diffY;
        if (t < 0) {
            t = 0;
        } else if (t > mCalendarHeight - mAttendanceDetailMariginTop) {
            t = mCalendarHeight - mAttendanceDetailMariginTop;
        }
        mAttendanceDetail.setTranslationY(t);
        t = mCalendarPager.getTranslationY() + diffY;
        if (t < - mCalendarLineHeight * mCurrentDayLine) {
            t = - mCalendarLineHeight * mCurrentDayLine;
        } else if (t > 0) {
            t = 0;
        }
        mCalendarPager.setTranslationY(t);
        checkDisableCanlender();
    }

    private ValueAnimator startAnimator(CalendarAnimator listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(listener);
        animator.setDuration(1000000);
        animator.start();
        return animator;
    }

    private void touchScrollEnd() {
        cancelAllAnimator();
        if (mAttendanceDetail.getTranslationY() > (mCalendarHeight - mAttendanceDetailMariginTop) / 2) {
            CalendarAnimator ca = new CalendarAnimator(mAttendanceDetail, true, 0, mCalendarHeight - mAttendanceDetailMariginTop);
            mCalendarAnimator = startAnimator(ca);
            ca = new CalendarAnimator(mCalendarPager, true, 0, 0);
            mDetailAnimator = startAnimator(ca);
        } else {
            CalendarAnimator ca = new CalendarAnimator(mAttendanceDetail, false, 0, 0);
            mCalendarAnimator = startAnimator(ca);
            ca = new CalendarAnimator(mCalendarPager, false, 0, - mCurrentDayLine * mCalendarLineHeight);
            mDetailAnimator = startAnimator(ca);
        }
    }

    private void touchFling(float v) {
        cancelAllAnimator();
        v = v * 16 / 1000;
        if (v > 0) {
            CalendarAnimator ca = new CalendarAnimator(mAttendanceDetail, true, v, mCalendarHeight - mAttendanceDetailMariginTop);
            mCalendarAnimator = startAnimator(ca);
            ca = new CalendarAnimator(mCalendarPager, true, v, 0);
            mDetailAnimator = startAnimator(ca);
        } else {
            CalendarAnimator ca = new CalendarAnimator(mAttendanceDetail, false, v, 0);
            mCalendarAnimator = startAnimator(ca);
            ca = new CalendarAnimator(mCalendarPager, false, v, - mCurrentDayLine * mCalendarLineHeight);
            mDetailAnimator = startAnimator(ca);
        }
    }

    private void navToDate(int dateInt, boolean smooth) {
        if (dateInt != mCurrentDateInt) {
            int pageIndex = CalendarViewPager.monthIntToPageIndex(dateInt);
            if (pageIndex == mCalendarPager.getCurrentItem()) {
                int old = mCurrentDateInt;
                mCurrentDateInt = dateInt;
                onCurrentDayChange(mCurrentDateInt, old);
            } else {
                mTheDataPostToOnCurrentPageChange = dateInt;
                mCalendarPager.setCurrentItem(pageIndex, smooth);
            }
        }
    }

    private void onCurrentPageChange(int monthInt) {
        int dateInt = monthInt;
        if (CalendarUtils.isSameMonth(mTheDataPostToOnCurrentPageChange, monthInt)) {
            dateInt = mTheDataPostToOnCurrentPageChange;
        }
        mTheDataPostToOnCurrentPageChange = 0;
        int old = mCurrentDateInt;
        mCurrentDateInt = dateInt;
        onCurrentDayChange(mCurrentDateInt, old);
    }

    private void onCurrentDayChange(int dateInt, int oldDateInt) {
        if (dateInt != oldDateInt) {
            String year = String.valueOf(CalendarUtils.getDisplayYear(dateInt));
            String month = String.valueOf(CalendarUtils.getDisplayMonth(dateInt));
            String day = String.valueOf(CalendarUtils.getDisplayDay(dateInt));
            if (month.length() == 1) {
                month = "0" + month;
            }
            if (day.length() == 1) {
                day = "0" + day;
            }
//            mCalendarYearMonthDay.setText(year + "." + month + "." + day);

//            if (mCurrentDateInt == CalendarUtils.getToday()) {
//                mCalendarToday.setVisibility(View.INVISIBLE);
//            } else {
//                if (mDisableCalendar) {
//                    mCalendarToday.setVisibility(View.INVISIBLE);
//                } else {
//                    mCalendarToday.setVisibility(View.VISIBLE);
//                }
//            }

            mCalendarPager.notifyDataChanged(oldDateInt);
            mCalendarPager.notifyDataChanged(dateInt);

            Point p = CalendarGridView.dateToGridPostion(dateInt, dateInt);
            if (p != null) {
                mCurrentDayLine = p.y;
            } else {
                mCurrentDayLine = 0;
            }

            if (CalendarUtils.isSameMonth(dateInt, oldDateInt)) {
                loadCurrentDayData();
            } else {
                loadCurrentMonthData(false);
            }
        }
    }

    private void loadCurrentMonthData(boolean needReload) {
//        if (mMonthDataListener != null) {
//            mMonthDataListener.cancel();
//            mMonthDataListener = null;
//        }
//        if (mDayDataListener != null) {
//            mDayDataListener.cancel();
//            mDayDataListener = null;
//        }
        Integer status = mCalendarMonthDataStatus.get(CalendarUtils.getPureMonthInt(mCurrentDateInt));
        if (status != null && status == DATA_STATUS_COMPLETE && !needReload) {
            loadCurrentDayData();
        } else {
//            clearDetailData();
//            showLoading();
//            getManagerOrgData();
//            mMonthDataListener = new MonthDataLoadApiEventListener(mCurrentDateInt);
//            mCalendarService.getManagerCalFullInfos(mOrgId, mCurrentDeptId,
//                    CalendarUtils.dateIntToTime(CalendarUtils.getPureMonthInt(mCurrentDateInt)),
//                    CalendarUtils.dateIntToTime(CalendarUtils.addMonth(mCurrentDateInt, 1)),
//                    CalendarUtils.dateIntToTime(mCurrentDateInt), mIsNotFirstRequest, EventButler.newCallback(mMonthDataListener, ApiEventListener.class, this));
            mIsNotFirstRequest = true;
        }

    }

    //    private MonthDataLoadApiEventListener mMonthDataListener;
//    private CancelableApiEventListener mDayDataListener;
//    private OrgDataApiEventListener mOrgDataListener;
    private void loadCurrentDayData() {
//        if (mMonthDataListener != null) {
//            mMonthDataListener.cancelDetail();
//        }
//        if (mDayDataListener != null) {
//            mDayDataListener.cancel();
//            mDayDataListener = null;
//        }
//        clearDetailData();
//        showLoading();
//        getManagerOrgData();
//        mDayDataListener = new DayDataLoadApiEventListener(mCurrentDateInt);
//        mCalendarService.getManagerCalDayFullInfos(mOrgId, mCurrentDeptId, CalendarUtils.dateIntToTime(mCurrentDateInt), EventButler.newCallback(mDayDataListener, ApiEventListener.class, this));
    }

    private class CalendarItemHolder {

        private TextView mDateText;
        private View mLeaveTip;

        private boolean mIsToday;
        private boolean mIsCurrent;

        public CalendarItemHolder(View v) {
            mDateText = (TextView) v.findViewById(R.id.calendar_date_text);
            mLeaveTip = v.findViewById(R.id.calendar_date_leave_tip);
        }

        public void setText(String text) {
            mDateText.setText(text);
        }

        public void setTodayTip(boolean isToday) {
            mIsToday = isToday;
            if (!mIsCurrent) {
                if (mIsToday) {
                    setTodayBackground();
                } else {
                    mDateText.setBackgroundDrawable(null);
                }
            }
        }

        private void setTodayBackground() {
            if (mCalendarItemTodayTip == null) {
                mCalendarItemTodayTip = ReservationActivity.this.getResources().getDrawable(R.mipmap.calendar_today_date_tip);
            }
            mDateText.setBackgroundDrawable(mCalendarItemTodayTip);
        }

        public void setCurrentTip(boolean isCurrent) {
            mIsCurrent = isCurrent;
            if (mIsCurrent) {
                setCurrentBackground();
            } else {
                if (mIsToday) {
                    setTodayBackground();
                } else {
                    mDateText.setBackgroundDrawable(null);
                }
            }
        }

        private void setCurrentBackground() {
            if (mCalendarItemCurrentTip == null) {
                mCalendarItemCurrentTip = ReservationActivity.this.getResources().getDrawable(R.mipmap.calendar_today_date_tip);
            }
            mDateText.setBackgroundDrawable(mCalendarItemCurrentTip);
        }

        public void setLeaveTip(boolean hasLeave) {
            if (hasLeave) {
                mLeaveTip.setVisibility(View.VISIBLE);
            } else {
                mLeaveTip.setVisibility(View.INVISIBLE);
            }
        }

        public void setIsCurrentMonth(boolean isCurrentMonth) {
            if (isCurrentMonth) {
                mDateText.setAlpha(1);
            } else {
                mDateText.setAlpha(0.5f);
            }
        }
    }

    private class CalendarAnimator implements ValueAnimator.AnimatorUpdateListener {

        //加速度
        private static final float ACC = 1;

        private View mView;
        private boolean mDirection;
        private float mStartSpeed;
        private float mEndValue;

        public CalendarAnimator(View view, boolean direction, float startSpeed, float endValue) {
            mView = view;
            mDirection = direction;
            mStartSpeed = Math.abs(startSpeed);
            mEndValue = endValue;
            if (mView != null) {
                if (mDirection) {
                    if (mView.getTranslationY() > mEndValue) {
                        mView.setTranslationY(mEndValue);
                    }
                } else {
                    if (mView.getTranslationY() < mEndValue) {
                        mView.setTranslationY(mEndValue);
                    }
                }
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (mView != null) {
                boolean showStop = false;
                mStartSpeed += ACC;
                float posY = mView.getTranslationY();
                if (mDirection) {
                    posY += mStartSpeed;
                    if (posY >= mEndValue) {
                        posY = mEndValue;
                        showStop = true;
                    }
                } else {
                    posY -= mStartSpeed;
                    if (posY <= mEndValue) {
                        posY = mEndValue;
                        showStop = true;
                    }
                }
                mView.setTranslationY(posY);
                if (showStop) {
                    animation.cancel();
                }
                checkDisableCanlender();
            }
        }
    }

    private void checkDisableCanlender() {
//        if (mAttendanceDetail.getTranslationY() != mCalendarHeight - mAttendanceDetailMariginTop) {
//            // 本来日历收起来，是不可以切换日期的，2.3版本改成收起来也可以切换日期
//            setDisableCanlendar(false);
//        } else {
//            setDisableCanlendar(false);
//        }
        setDisableCanlendar(false);
    }

    private void setDisableCanlendar(boolean disableCanlendar) {
        if (mDisableCalendar == disableCanlendar) {
            return;
        }
        mDisableCalendar = disableCanlendar;
        if (mDisableCalendar) {
            mCalendarPager.setTouchDisabled(true);
//             mCalendarToday.setVisibility(View.INVISIBLE);
        } else {
            mCalendarPager.setTouchDisabled(false);
//            if (mCurrentDateInt == CalendarUtils.getToday()) {
//                mCalendarToday.setVisibility(View.INVISIBLE);
//            } else {
//                mCalendarToday.setVisibility(View.VISIBLE);
//            }
        }
    }

    private class OnInterruptTouchListener implements View.OnTouchListener {

        private final int TOUCH_THRESHOLD;
        private final int FLING_THRESHOLD;
        private final int FLING_MAX_VALUE;

        private static final int TOUCH_STATUS_UNKNOW = 0;
        private static final int TOUCH_STATUS_SCROLL_V = 1;
        private static final int TOUCH_STATUS_SCROLL_H = 2;

        private int mTouchState = 0;
        private float mLastX;
        private float mLastY;

        private float mInnerLastX;
        private float mInnerLastY;

        private VelocityTracker mVelocityTracker;

        public OnInterruptTouchListener(Context context) {
            ViewConfiguration config = ViewConfiguration.get(context);
            TOUCH_THRESHOLD = config.getTouchSlop();
            FLING_THRESHOLD = config.getMinimumFlingVelocity() * 2;
            FLING_MAX_VALUE = config.getMaximumFlingVelocity();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if (action == MotionEvent.ACTION_DOWN) {
                mTouchState = TOUCH_STATUS_UNKNOW;
                mLastX = event.getX();
                mLastY = event.getY();

                mInnerLastX = event.getX();
                mInnerLastY = event.getY();
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                if (mTouchState == TOUCH_STATUS_SCROLL_V) {
                    float velocityY = 0;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.computeCurrentVelocity(1000, FLING_MAX_VALUE);
                        int pointerId = event.getPointerId(0);
                        velocityY = mVelocityTracker.getYVelocity(pointerId);
                    }
                    if (Math.abs(velocityY) >= FLING_THRESHOLD) {
                        touchFling(velocityY);
                    } else {
                        touchScrollEnd();
                    }
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                float diffX = event.getX() - mLastX;
                float diffY = event.getY() - mLastY;

                // 当日历收起时，listview可以进行滑动；如果listview没有滑到起始的地方，日历无法展开
                if(mAttendanceDetail.getTranslationY() <= 0){
                    float innerDiffY = event.getY() - mInnerLastY;
                    if((mListView.getScrollY() > 0) || (innerDiffY <= 0 && mListView.getScrollY() <= 0)){
                        mInnerLastX = event.getX();
                        mInnerLastY = event.getY();
                        return false;
                    }
                }
                if (mTouchState == TOUCH_STATUS_UNKNOW) {
                    if (Math.abs(diffX) * 0.5 > Math.abs(diffY)) {
                        if (Math.abs(diffX) >= TOUCH_THRESHOLD) {
                            mTouchState = TOUCH_STATUS_SCROLL_H;
                            mLastX = event.getX();
                            mLastY = event.getY();
                        }
                    } else {
                        if (Math.abs(diffY) >= TOUCH_THRESHOLD) {
                            mTouchState = TOUCH_STATUS_SCROLL_V;
                            mLastX = event.getX();
                            mLastY = event.getY();
                            return true;
                        }
                    }
                } else if (mTouchState == TOUCH_STATUS_SCROLL_V) {
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(event);
                    touchScroll(diffY);
                    mLastX = event.getX();
                    mLastY = event.getY();
                    return true;
                }
            }
            return false;
        }
    };


    class Adapter extends BaseExpandableListAdapter{


        @Override
        public int getGroupCount() {
            return 0;
        }

        @Override
        public int getChildrenCount(int i) {
            return 0;
        }

        @Override
        public Object getGroup(int i) {
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }
}
