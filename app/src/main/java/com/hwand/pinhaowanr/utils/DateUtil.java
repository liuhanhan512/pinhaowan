package com.hwand.pinhaowanr.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hanhanliu on 15/12/8.
 */
public class DateUtil {

    public static String convertLongToString(long timeSTamp) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return convertLongToString(timeSTamp, formatter);
    }

    public static String convertLongToString(long timeSTamp, DateFormat dateFormat) {
        Date date = new Date(timeSTamp);
        String dateFormatted = dateFormat.format(date);

        return dateFormatted;
    }

}
