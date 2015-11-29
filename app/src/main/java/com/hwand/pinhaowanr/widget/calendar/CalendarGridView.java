package com.hwand.pinhaowanr.widget.calendar;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.Calendar;

/**
 * Created by clifford on 15/5/12.
 */
public class CalendarGridView extends UniformGridView {

    public static final int COLUMN_COUNT = 7;
    public static final int ROW_COUNT = 6;

    public interface OnCalendarGridViewItemClickListener {
        void onItemClick(int dayInt, boolean isCurrentMonth, View v);
    }

    public interface CalendarGridViewAdapter {
        View getView(int dayInt, boolean isCurrentMonth, View oldView);
    }

    private int mMonthInt = CalendarUtils.DEFAULT_DATE_INT;

    private OnCalendarGridViewItemClickListener mOnCalendarGridViewItemClickListener;
    private CalendarGridViewAdapter mCalendarGridViewAdapter;

    public CalendarGridView(Context context) {
        super(context);
        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnUniformGridViewItemClickListener(new OnUniformGridViewItemClickListener() {
            @Override
            public void onItemClick(int px, int py, UniformGridView parent, View v) {
                if (mOnCalendarGridViewItemClickListener != null) {
                    int dayInt = gridPositionToDate(mMonthInt, px, py);
                    mOnCalendarGridViewItemClickListener.onItemClick(dayInt, CalendarUtils.isSameMonth(dayInt, mMonthInt), v);
                }
            }
        });
        super.setUniformGridViewAdapter(new UniformGridViewAdapter() {
            @Override
            public int getWidthCount() {
                return COLUMN_COUNT;
            }

            @Override
            public int getHeightCount() {
                return ROW_COUNT;
            }

            @Override
            public View getView(int px, int py, UniformGridView parent, View oldView) {
                if (mCalendarGridViewAdapter != null) {
                    int dayInt = gridPositionToDate(mMonthInt, px, py);
                    return mCalendarGridViewAdapter.getView(dayInt, CalendarUtils.isSameMonth(dayInt, mMonthInt), oldView);
                } else {
                    return null;
                }
            }
        });
    }

    private static SparseArray<Integer> sMonthGridMap = new SparseArray<Integer>();
    private static Calendar sCalendarCache = CalendarUtils.getPureCalendar();

    private static void checkMonthGridMap(int monthInt) {
        monthInt = CalendarUtils.getPureMonthInt(monthInt);
        Integer monthData = sMonthGridMap.get(monthInt);
        if (monthData == null) {
            CalendarUtils.fillCacheCalendar(sCalendarCache, monthInt);
            int maxMonthDay = sCalendarCache.getActualMaximum(Calendar.DAY_OF_MONTH);
            int index = sCalendarCache.get(Calendar.DAY_OF_WEEK) - 1;
            monthData = maxMonthDay * 10 + index;
            sMonthGridMap.put(monthInt, monthData);
        }
    }

    private static int getMaxMonthDay(int monthData) {
        return monthData / 10;
    }

    private static int getMonthFirstDayIndex(int monthData) {
        return monthData % 10;
    }

    public static int gridPositionToDate(int monthInt, int px, int py) {
        monthInt = CalendarUtils.getPureMonthInt(monthInt);
        checkMonthGridMap(monthInt);
        int monthData = sMonthGridMap.get(monthInt);
        int maxMonthDay = getMaxMonthDay(monthData);
        int firstDayIndex = getMonthFirstDayIndex(monthData);
        int lastDayIndex = firstDayIndex + maxMonthDay - 1;
        int targetIndex = py * COLUMN_COUNT + px;
        if (targetIndex < firstDayIndex) {
            int preMonth = CalendarUtils.addMonth(monthInt, -1);
            checkMonthGridMap(preMonth);
            int preMaxMonthDay = getMaxMonthDay(sMonthGridMap.get(preMonth));
            int resultDay = preMaxMonthDay - (firstDayIndex - targetIndex) + 1;
            return CalendarUtils.getDateInt(CalendarUtils.getDisplayYear(preMonth), CalendarUtils.getDisplayMonth(preMonth), resultDay);
        } else if (targetIndex > lastDayIndex) {
            int nextMonth = CalendarUtils.addMonth(monthInt, 1);
            int resultDay = targetIndex - lastDayIndex;
            return CalendarUtils.getDateInt(CalendarUtils.getDisplayYear(nextMonth), CalendarUtils.getDisplayMonth(nextMonth), resultDay);
        } else {
            int resultDay = targetIndex - firstDayIndex + 1;
            return CalendarUtils.getDateInt(CalendarUtils.getDisplayYear(monthInt), CalendarUtils.getDisplayMonth(monthInt), resultDay);
        }
    }

    public static Point dateToGridPostion(int monthInt, int dayInt) {
        monthInt = CalendarUtils.getPureMonthInt(monthInt);
        checkMonthGridMap(monthInt);
        int monthData = sMonthGridMap.get(monthInt);
        int maxMonthDay = getMaxMonthDay(monthData);
        int firstDayIndex = getMonthFirstDayIndex(monthData);
        int lastDayIndex = firstDayIndex + maxMonthDay - 1;
        int resultIndex;
        if (CalendarUtils.addMonth(dayInt, 1) == monthInt) {
            int preMonth = CalendarUtils.getPureMonthInt(dayInt);
            checkMonthGridMap(preMonth);
            int preMaxMonthDay = getMaxMonthDay(sMonthGridMap.get(preMonth));
            resultIndex = firstDayIndex - (preMaxMonthDay - CalendarUtils.getDisplayDay(dayInt)) - 1;
        } else if (CalendarUtils.addMonth(dayInt, -1) == monthInt) {
            resultIndex = lastDayIndex + CalendarUtils.getDisplayDay(dayInt);
        } else {
            resultIndex = firstDayIndex + CalendarUtils.getDisplayDay(dayInt) - 1;
        }
        if (resultIndex >= 0 && resultIndex < COLUMN_COUNT * ROW_COUNT) {
            return new Point(resultIndex % COLUMN_COUNT, resultIndex / COLUMN_COUNT);
        } else {
            return null;
        }
    }

    public void setCalendarGridViewAdapter(CalendarGridViewAdapter adapter) {
        if (mCalendarGridViewAdapter != adapter) {
            mCalendarGridViewAdapter = adapter;
            notifyDataChanged();
        }
    }

    public CalendarGridViewAdapter getCalendarGridViewAdapter() {
        return mCalendarGridViewAdapter;
    }

    public void setOnCalendarGridViewItemClickListener(OnCalendarGridViewItemClickListener listener) {
        mOnCalendarGridViewItemClickListener = listener;
    }

    public void setMonthInt(int monthInt) {
        monthInt = CalendarUtils.getPureMonthInt(monthInt);
        if (!CalendarUtils.isSameMonth(monthInt, mMonthInt)) {
            mMonthInt = monthInt;
            notifyDataChanged();
        }
    }

    public int getMonthInt() {
        return mMonthInt;
    }

    @Override
    public void setOnUniformGridViewItemClickListener(OnUniformGridViewItemClickListener listener) {
    }

    @Override
    public void setUniformGridViewAdapter(UniformGridViewAdapter adapter) {
    }
}