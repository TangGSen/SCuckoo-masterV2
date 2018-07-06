package com.ourcompany.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/23 14:36
 * Des    :
 */

public class BombUtils {
    public static BmobDate getBombDate(String moreTime){
        BmobDate bmobDate;
        if (TextUtils.isEmpty(moreTime)) {
            bmobDate = new BmobDate(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date0 = df2.parse(moreTime);
                bmobDate = new BmobDate(date0);
            } catch (ParseException e) {
                bmobDate = new BmobDate(new Date(System.currentTimeMillis()));
            }
        }
        return bmobDate;
    }
}
