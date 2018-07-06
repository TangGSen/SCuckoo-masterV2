package com.ourcompany.utils;

import com.orhanobut.logger.Logger;
import com.ourcompany.BuildConfig;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 23:25
 * Des    :
 */

public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug= BuildConfig.DEBUG;
    /**
     * 错误
     */
    public static void e(String tag,String msg){
        if(isDebug){
            Logger.t(tag).e(msg+"");
        }
    }
    /**
     * 调试
     */
    public static void d(String tag,String msg){
        if(isDebug){
            Logger.t(tag).d( msg+"");
        }
    }
    /**
     * 信息
     */
    public static void i(String tag,String msg){
        if(isDebug){
            Logger.t(tag).i( msg+"");
        }
    }
}
