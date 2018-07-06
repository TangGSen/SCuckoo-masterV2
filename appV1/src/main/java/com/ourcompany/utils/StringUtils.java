package com.ourcompany.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 20:29
 * Des    :
 */

public class StringUtils {
    /**
     * 判断传入的字符串是否是一个手机号码
     *
     * @param strPhone
     * @return
     */
    public static boolean isPhoneNumber(String strPhone) {

        String str = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(strPhone);
        return m.find();
    }

    public static String getDealPhone(String phone){
        char[] chars = phone.toCharArray();
        for(int i=0;i<chars.length;i++){
            if(i>2 && i<8){
                chars[i]='*';
            }
        }
        return String.valueOf(chars );
    }

    public static String getFormatFileSize(double size) {
        double kiloByte = size/1024;
        if(kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte/1024;
        if(megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte/1024;
        if(gigaByte < 1) {
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte/1024;
        if(teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
