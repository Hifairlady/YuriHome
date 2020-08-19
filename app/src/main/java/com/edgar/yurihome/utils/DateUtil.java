package com.edgar.yurihome.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    private static final String TAG = "====================" + DateUtil.class.getSimpleName();

    public static String getTimeString(long timeStamp) {
        timeStamp = timeStamp * 1000;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        try {
//            Log.d(TAG, "getTimeFromLong: " + simpleDateFormat.parse("2020-8-15 23:13:00").getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentTimeString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter.format(date);
    }


}
