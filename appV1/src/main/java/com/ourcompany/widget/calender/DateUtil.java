package com.ourcompany.widget.calender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by winson on 2015/2/22.
 */
public class DateUtil {
    /**
     * 取得指定月的第一日是星期几
     *
     * @return
     */
    public static int getFirstDayWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static List<Long> getDateList(int year, int month) {
        List<Long> data = new ArrayList<Long>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        int emptyLength = DateUtil.getFirstDayWeek(calendar);
        for (int i = 1; i < emptyLength; i++) {
            data.add(-1l);
        }

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天


        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);

        ca.set(Calendar.MONTH, month);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));

        for (int i = 0; i < ca.get(Calendar.DAY_OF_MONTH); i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            data.add(calendar.getTimeInMillis());
        }

        int currentSize = data.size();
        if (data.size() < (6 * 7)) {
            for (int i = 0; i < (6 * 7 - currentSize); i++) {
                data.add(-1l);
            }
        }
        return data;
    }

    /**
     * 解析出日
     *
     * @param time
     * @return
     */
    public static int parseDayOfMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return Integer.parseInt(format.format(new Date(time)));
    }
}
