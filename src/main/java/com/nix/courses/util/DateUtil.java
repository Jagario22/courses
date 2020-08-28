package com.nix.courses.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String dateFormat = "yyyy-MM-dd";
    private static String timeFormat = "HH:mm";

    public static Date formatDate(String date) {
        Date formattedDate = new Date();
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formattedDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Date formatTime(String time) {
        Date formattedTime = new Date();
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
            formattedTime = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }


    public static String formatDate(Date date) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static String formatTime(Date date) {
        DateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(date);
    }
}
