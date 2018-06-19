package com.tim.pollution.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeString {

    public static String switchTime(String time) {
        try {
            Date date2 = new SimpleDateFormat("yyyy年MM月dd日").parse(time);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date2);
        } catch (Exception e) {
            Log.e("lili", "时间错误：" + e);
        }
        return null;
    }

    public static String switchTimes(String time) {
        try {
            Date date2 = new SimpleDateFormat("yyyy年MM月dd日HH时").parse(time);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date2);
        } catch (Exception e) {
            Log.e("lili", "时间错误：" + e);
        }
        return null;
    }

    /**
     * 2018.06.19 1:00--2018.06.19 15:00
     * @param time
     * @return
     */
    public static String switchAllTimes(String time) {
        try {
            String []strs = strTime(time);
            StringBuffer str = new StringBuffer();
            for (int i = 0 ;i<strs.length;i++){
                Date date2 = new SimpleDateFormat("yyyy年MM月dd日HH时").parse(strs[i]);
                if(i==0){
                    str.append(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date2));
                }else{
                    str.append("--"+new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date2));
                }
            }
            return str.toString();
        } catch (Exception e) {
            Log.e("lili", "时间错误：" + e);
        }
        return null;
    }

    public static  String switchYearTimes(String time) {
        try {
            String []strs = strTime(time);
            StringBuffer str = new StringBuffer();
            for (int i = 0 ;i<strs.length;i++){
                Date date2 = new SimpleDateFormat("yyyy年MM月dd日").parse(strs[i]);
                if(i==0){
                    str.append(new SimpleDateFormat("yyyy.MM.dd").format(date2));
                }else{
                    str.append("--"+new SimpleDateFormat("yyyy.MM.dd").format(date2));
                }
            }
            return str.toString();
        } catch (Exception e) {
            Log.e("lili", "时间错误：" + e);
        }
        return null;
    }

    public static String[] strTime (String time){
        return  time.split("至");
    }

}
