package com.tim.pollution.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeString {

    public static String switchTime(String time) {
        try {
            Date date2 = new SimpleDateFormat("yyyy年MM月dd日HH时").parse(time);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date2);
        } catch (Exception e) {
            Log.e("lili", "时间错误：" + e);
        }
        return null;
    }

}
