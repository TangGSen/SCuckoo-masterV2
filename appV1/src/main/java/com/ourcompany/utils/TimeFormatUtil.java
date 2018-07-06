package com.ourcompany.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil {
    private static volatile SimpleDateFormat format;

    private static SimpleDateFormat getSimpleDateFormat() {
        if (format == null) {
            synchronized (TimeFormatUtil.class) {
                if (format == null) {
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
            }

        }
        return format;
    }

    public static String getIntervalFormString(String time) {
        try {
            return getIntervalFormDate(getSimpleDateFormat().parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }

    public static String getIntervalFormDate(Date createAt) {
        // 定义最终返回的结果字符串。
        String interval = null;

        long millisecond = new Date().getTime() - createAt.getTime();

        long second = millisecond / 1000;

        if (second <= 0) {
            second = 0;
        }
        //*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
//            interval = second + "秒以前"
            interval = "刚刚";
        } else if (second >= 30 && second < 60) {
//            interval = "半分钟前";
            interval = "刚刚";
        } else if (second >= 60 && second < 3600) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (second >= 3600 && second < 60 * 60 * 24) {//大于1小时 小于24小时
            long hour = (second / 60) / 60;
            if (hour <= 3) {
                interval = hour + "小时前";
            } else {
                interval = "今天" + getFormatTime(createAt, "HH:mm");
            }
        } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {//大于1D 小于2D
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {//大于2D小时 小于 7天
            long day = ((second / 60) / 60) / 24;
            interval = day + "天前";
        } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) {//大于7天小于365天
            interval = getFormatTime(createAt, "MM-dd HH:mm");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        } else {
            interval = "0";
        }
        return interval;
    }

    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }

    public static Date getDateFormTimeString(String time) {
        try {
            return getSimpleDateFormat().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



}
