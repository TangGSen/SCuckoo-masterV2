package com.ourcompany.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.ourcompany.app.MApplication;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/16 17:02
 * Des    :
 */

public class ResourceUtils {
    public static String getString(int id) {
        return MApplication.mContext.getResources().getString(id);
    }

    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(MApplication.mContext,id);
    }


    public static String[] getStringArray( int id) {
        return MApplication.mContext.getResources().getStringArray(id);
    }

    public static int getResColor( int id){
        return MApplication.mContext.getResources().getColor(id);
    }
}
