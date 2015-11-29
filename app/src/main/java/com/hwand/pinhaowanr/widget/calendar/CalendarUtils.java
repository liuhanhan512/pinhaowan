package com.hwand.pinhaowanr.widget.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hanhanliu on 15/11/23.
 */
public class CalendarUtils {
    public static final int DEFAULT_DATE_INT = 19690000;
    private static Calendar sCalendarCache = getPureCalendar();

    public CalendarUtils() {
    }

    public static Calendar getPureCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        cal.setTimeInMillis(0L);
        cal.set(10, 0);
        return cal;
    }

    public static void fillCacheCalendar(Calendar cal, int dayInt) {
        if(cal != null) {
            cal.setTimeInMillis(0L);
            cal.set(getDisplayYear(dayInt), getDisplayMonth(dayInt) - 1, getDisplayDay(dayInt), 0, 0, 0);
        }

    }

    public static long dateIntToTime(int dataInt) {
        fillCacheCalendar(sCalendarCache, dataInt);
        return sCalendarCache.getTimeInMillis();
    }

    public static int getDateInt(Calendar cal) {
        if(cal == null) {
            cal = getPureCalendar();
        }

        return (cal.get(1) - 1) * 10000 + cal.get(2) * 100 + cal.get(5) - 1;
    }

    public static int getDateInt(int displayYear, int displayMonth, int displayDay) {
        return (displayYear - 1) * 10000 + (displayMonth - 1) * 100 + (displayDay - 1);
    }

    public static int getDateInt(long time) {
        sCalendarCache.setTimeInMillis(time);
        return getDateInt(sCalendarCache);
    }

    public static int getToday() {
        sCalendarCache.setTime(new Date());
        return getDateInt(sCalendarCache);
    }

    public static int getPureMonthInt(int dayInt) {
        return dayInt / 100 * 100;
    }

    public static int getDisplayYear(int dateInt) {
        return dateInt / 10000 + 1;
    }

    public static int getDisplayMonth(int dateInt) {
        return dateInt % 10000 / 100 + 1;
    }

    public static int getDisplayDay(int dateInt) {
        return dateInt % 100 + 1;
    }

    public static boolean isSameMonth(int dateInt1, int dateInt2) {
        return dateInt1 / 100 == dateInt2 / 100;
    }

    public static int monthDiff(int monthInt1, int monthInt2) {
        int month1 = monthInt1 / 10000 * 12 + monthInt1 % 10000 / 100;
        int month2 = monthInt2 / 10000 * 12 + monthInt2 % 10000 / 100;
        return month1 - month2;
    }

    public static int addMonth(int monthInt, int num) {
        int month = monthInt / 10000 * 12 + monthInt % 10000 / 100;
        month += num;
        return month / 12 * 10000 + month % 12 * 100;
    }
}
