package com.ourcompany.utils;

import java.text.SimpleDateFormat;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/27 11:35
 * Des    :
 */

public class DateUtils {
    public static String getDateFormat(long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(time);
    }
}
