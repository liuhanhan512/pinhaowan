package com.hwand.pinhaowanr.fine;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.ClassDetailGroupTitleModel;
import com.hwand.pinhaowanr.model.ClassDetailModel;
import com.hwand.pinhaowanr.model.ClassDetailSubTitleModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.DateUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.InterruptTouchView;
import com.hwand.pinhaowanr.widget.calendar.CalendarGridView;
import com.hwand.pinhaowanr.widget.calendar.CalendarUtils;
import com.hwand.pinhaowanr.widget.calendar.CalendarViewPager;
import com.hwand.pinhaowanr.widget.calendar.UniformGridView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

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

    private Adapter mAdapter;

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

    private static final int[] WEEK_WORDS = new int[]{
            R.string.calendar_sunday,
            R.string.calendar_monday,
            R.string.calendar_tuesday,
            R.string.calendar_wednesday,
            R.string.calendar_thursday,
            R.string.calendar_friday,
            R.string.calendar_saturday
    };

    private static final String CLASS_DETAIL_MODEL_KEY = "CLASS_DETAIL_MODEL_KEY";
    private static final String CLASS_DETAIL_ID_KEY = "CLASS_DETAIL_ID_KEY";
    private ClassDetailModel mClassDetailModel;
    private int mId;

    private List<ClassDetailGroupTitleModel> classDetailGroupTitleModels = new ArrayList<ClassDetailGroupTitleModel>();

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ReservationActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, ClassDetailModel classDetailModel, int id) {
        Intent intent = new Intent();
        intent.setClass(context, ReservationActivity.class);
        intent.putExtra(CLASS_DETAIL_MODEL_KEY, classDetailModel);
        intent.putExtra(CLASS_DETAIL_ID_KEY, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_layout);
        initIntentValues();
        initTitle();
        initViews();
        getTimeList(mClassDetailModel.getStartMonth());
    }

    private void initIntentValues() {
        mClassDetailModel = (ClassDetailModel) getIntent().getSerializableExtra(CLASS_DETAIL_MODEL_KEY);
        mId = getIntent().getIntExtra(CLASS_DETAIL_ID_KEY, -1);
    }

    private void initTitle() {
        setActionBarTtile("选择日期");
    }

    private void initViews() {
        mListView = (ExpandableListView) findViewById(R.id.listview);
        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        expandView();

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

//                int monthInt = CalendarUtils.getPureMonthInt(dateInt);
//                Integer monthDataStatus = mCalendarMonthDataStatus.get(monthInt);
//                if (monthDataStatus != null && monthDataStatus == DATA_STATUS_COMPLETE) {
//                    Boolean calendarCacheData = mCalendarCache.get(dateInt);
//                    if (calendarCacheData != null && calendarCacheData) {
//                        holder.setLeaveTip(true);
//                    } else {
//                        holder.setLeaveTip(false);
//                    }
//                } else {
//                    holder.setLeaveTip(false);
//                }
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
        mCalendarPager.setTranslationY(-mCalendarLineHeight * mCurrentDayLine);
    }

    private void expandView() {
        for (int i = 0; i < classDetailGroupTitleModels.size(); i++) {
            mListView.expandGroup(i);
        }
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
        if (t < -mCalendarLineHeight * mCurrentDayLine) {
            t = -mCalendarLineHeight * mCurrentDayLine;
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
            ca = new CalendarAnimator(mCalendarPager, false, 0, -mCurrentDayLine * mCalendarLineHeight);
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
            ca = new CalendarAnimator(mCalendarPager, false, v, -mCurrentDayLine * mCalendarLineHeight);
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

        updateReservationList(dateInt);
    }

    private void updateReservationList(int dateInt){
        String year = String.valueOf(CalendarUtils.getDisplayYear(dateInt));

        String month = "";
        int monthInt = CalendarUtils.getDisplayMonth(dateInt);
        if(monthInt < 10){
            month = "0" + month;
        } else {
            month = String.valueOf(CalendarUtils.getDisplayMonth(dateInt));
        }
        String formatDate = year + "-" + month;
        getTimeList(formatDate);
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

        private boolean mIsToday;
        private boolean mIsCurrent;

        public CalendarItemHolder(View v) {
            mDateText = (TextView) v.findViewById(R.id.calendar_date_text);
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
                    mDateText.setSelected(false);
                }
            }
        }

        private void setTodayBackground() {
            if (mCalendarItemTodayTip == null) {
                mCalendarItemTodayTip = ReservationActivity.this.getResources().getDrawable(R.drawable.circle_solid_red_bg);
            }
            mDateText.setBackgroundDrawable(mCalendarItemTodayTip);
            mDateText.setSelected(true);
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
                    mDateText.setSelected(false);
                }
            }
        }

        private void setCurrentBackground() {
            if (mCalendarItemCurrentTip == null) {
                mCalendarItemCurrentTip = ReservationActivity.this.getResources().getDrawable(R.drawable.circle_solid_red_bg);
            }
            mDateText.setBackgroundDrawable(mCalendarItemCurrentTip);
            mDateText.setSelected(true);
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
                if (mAttendanceDetail.getTranslationY() <= 0) {
                    float innerDiffY = event.getY() - mInnerLastY;
                    if ((mListView.getScrollY() > 0) || (innerDiffY <= 0 && mListView.getScrollY() <= 0)) {
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
    }

    class Adapter extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return classDetailGroupTitleModels.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public Object getGroup(int i) {
            return classDetailGroupTitleModels.get(i);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return classDetailGroupTitleModels.get(groupPosition).getClassDetailSubTitleModelList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return groupPosition << 32 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View groupView = null;
            if (convertView == null) {
                groupView = newGroupView(parent);
            } else {
                groupView = convertView;
            }
            bindGroupView(groupPosition, groupView);
            return groupView;
        }

        private View newGroupView(ViewGroup parent) {
            return View.inflate(ReservationActivity.this, R.layout.reservation_group_item_layout, null);
        }

        private void bindGroupView(final int groupPosition, View groupView) {
            TextView title = (TextView) groupView.findViewById(R.id.day_time);
            title.setText(classDetailGroupTitleModels.get(groupPosition).getTime());
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View childView = null;
            if (convertView == null) {
                childView = newChildView(parent, groupPosition);
            } else {
                childView = convertView;
            }
            bindChildView(groupPosition, childPosition, childView);
            return childView;
        }

        private View newChildView(ViewGroup parent, final int groupPosition) {
            View v = View.inflate(ReservationActivity.this, R.layout.resevation_child_item_layout, null);

            return v;
        }

        private void bindChildView(final int groupPosition, int childPosition,
                                   View groupView) {

            ListView listView = (ListView) groupView.findViewById(R.id.listview);

            final ReservationAdapter adapter = new ReservationAdapter(groupPosition);

            listView.setAdapter(adapter);// 设置菜单Adapter

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

    private static final int STATE_RESERVATED = 1;//已经预约
    private static final int STATE_FULL = 2;
    private static final int STATE_CAN = 3;

    class ReservationAdapter extends BaseAdapter {

        private int groupPosition;

        private List<ClassDetailSubTitleModel> classDetailSubTitleModels = new ArrayList<ClassDetailSubTitleModel>();

        public ReservationAdapter(int groupPosition) {
            List<ClassDetailSubTitleModel> subTitleModels = classDetailGroupTitleModels.get(groupPosition).getClassDetailSubTitleModelList();
            this.groupPosition = groupPosition;
            if (subTitleModels != null) {
                classDetailSubTitleModels.clear();
                classDetailSubTitleModels.addAll(subTitleModels);
            }
        }

        @Override
        public int getCount() {
            return classDetailSubTitleModels.size();
        }

        @Override
        public Object getItem(int position) {
            return classDetailSubTitleModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ReservationActivity.this)
                        .inflate(R.layout.reservation_list_item_layout, viewGroup, false);
            }
            final ClassDetailSubTitleModel classDetailSubTitleModel = classDetailSubTitleModels.get(position);

            TextView time = CommonViewHolder.get(convertView, R.id.time);
            time.setText(getString(R.string.reservation_time, DateUtil.convertLongToString(classDetailSubTitleModel.getStartTime()),
                    DateUtil.convertLongToString(classDetailSubTitleModel.getEndTime())));

            TextView reservationStatus = CommonViewHolder.get(convertView, R.id.reservation_status);
            final int state = classDetailSubTitleModel.getState();
            switch (state) {
                case STATE_RESERVATED:
                    reservationStatus.setEnabled(true);
                    reservationStatus.setSelected(true);
                    reservationStatus.setText(getString(R.string.reserved));
                    reservationStatus.setBackgroundResource(R.drawable.red_solid_bg);
                    break;
                case STATE_FULL:
                    reservationStatus.setEnabled(false);
                    reservationStatus.setSelected(false);
                    reservationStatus.setText(getString(R.string.fully_booked));
                    reservationStatus.setBackgroundResource(R.drawable.gray_solid_corner_bg);
                    break;
                case STATE_CAN:
                    reservationStatus.setEnabled(true);
                    reservationStatus.setSelected(false);
                    reservationStatus.setText(getString(R.string.reservation));
                    reservationStatus.setBackgroundResource(R.drawable.yellow_solid_bg);
                    break;
            }

            reservationStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == STATE_CAN) {
                        Date date = new Date(classDetailSubTitleModel.getStartTime());
                        Locale aLocale = Locale.US;
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM", new DateFormatSymbols(aLocale));
                        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String month = fmt.format(date);
//                        final int gp = groupPosition;
                        apply(groupPosition,position ,mId, month, classDetailSubTitleModel.getSubscribeId());
                    }
                }
            });

            return convertView;
        }
    }

    private void getTimeList(String month) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mId + "");
        // TODO
//        Date date = new Date(System.currentTimeMillis());
//        Locale aLocale = Locale.US;
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM", new DateFormatSymbols(aLocale));
//        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String month = fmt.format(date);
        params.put("month", month);
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_APPLY_TIMES, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                classDetailGroupTitleModels.clear();
                mAdapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(response)) {

                    List<ClassDetailSubTitleModel> list = ClassDetailSubTitleModel.arrayFromData(response);
                    if (list != null && list.size() > 0) {
                        try {
                            filterData(list);
                        } catch (ParseException e) {
                            e.printStackTrace();

                        }

                    } else {

                    }
                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void filterData(List<ClassDetailSubTitleModel> list) throws ParseException {
        Set<String> keys = new TreeSet<String>();
        for (ClassDetailSubTitleModel order : list) {
            Date start = new Date(order.getStartTime());
            Locale aLocale = Locale.US;
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-DD", new DateFormatSymbols(aLocale));
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            String day = fmt.format(start);
            keys.add(day);
        }
        for (String key : keys) {
            ClassDetailGroupTitleModel classDetailGroupTitleModel = new ClassDetailGroupTitleModel();
            List<ClassDetailSubTitleModel> orders = new ArrayList<ClassDetailSubTitleModel>();
            for (ClassDetailSubTitleModel order : list) {
                Date start = new Date(order.getStartTime());
                Locale aLocale = Locale.US;
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-DD", new DateFormatSymbols(aLocale));
                fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                String month = fmt.format(start);
                if (TextUtils.equals(key, month)) {
                    orders.add(order);
                }
            }
            classDetailGroupTitleModel.setClassDetailSubTitleModelList(orders);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
            Date date = sdf.parse(key);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            String str_day = "" + month + "月" + day + "日" + "星期" + getResources().getString(WEEK_WORDS[weekday - 1]);
            classDetailGroupTitleModel.setTime(str_day);
            classDetailGroupTitleModels.add(classDetailGroupTitleModel);
        }
        expandView();


    }

    private void apply(final int groupPosition , final int position , int classId, String month, int subscribeId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", classId + "");
        params.put("month", month + "");
        params.put("subscribeId", subscribeId + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_APPLY_CLASS, params);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //结果（result） 1 已经预约  2 人已经满 3 你不是会员 4 成功
                if (!TextUtils.isEmpty(response) && response.contains("4")) {
                    AndTools.showToast("成功预约！");
                    classDetailGroupTitleModels.get(groupPosition).getClassDetailSubTitleModelList().get(position).setState(STATE_RESERVATED);
                    mAdapter.notifyDataSetChanged();
                    // TODO:设置已预约
                } else {
                    String msg = "网络问题请重试！";
                    if (TextUtils.isEmpty(response)) {

                    } else if (response.contains("1")) {
                        msg = "已经预约！";
                    } else if (response.contains("2")) {
                        msg = "预约人已经满！";
                    } else if (response.contains("3")) {
                        msg = "你不是会员！";
                    }
                    new DDAlertDialog.Builder(ReservationActivity.this)
                            .setTitle("提示").setMessage(msg)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getFragmentManager().popBackStack();
                                }
                            }).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new DDAlertDialog.Builder(ReservationActivity.this)
                        .setTitle("提示").setMessage("网络问题请重试！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getFragmentManager().popBackStack();
                            }
                        }).show();
            }
        });
    }
}
